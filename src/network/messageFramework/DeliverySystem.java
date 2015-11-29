/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network.messageFramework;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Cette classe permet de lancer des taches asynchrones.
 * Ces taches peuvent ou pas notifier la fin de leur executions.
 * 
 * Le fonctionnement de la classe est le suivant:
 * 1) Des taches sont envoyes au singleton
 * 2) Elles sont stockees dans une queue
 * 3) Executees par un pool de threads
 * 4) Lorsqu'une tache executee, on notifie eventuellement l'envoyeur   
 * @author mickx
 */
public class DeliverySystem {

    private static DeliverySystem INSTANCE;
    private final Logger LOGGER;
    private ExecutorCompletionService<Object> completionService;
    private final ConcurrentLinkedQueue<Future<?>> futures;
    private ExecutorService slaveExecutor;
    private ExecutorService masterExecutor;
    private final Map<Future<?>, FrameworkMessage<?>> matcher;
    private volatile boolean isLaunched;
    private volatile boolean shouldStop;
    private volatile boolean safeStop;

    static {
        INSTANCE = new DeliverySystem();
    }

    private DeliverySystem() {
        LOGGER = Logger.getLogger(DeliverySystem.class.getName());
        slaveExecutor = Executors.newFixedThreadPool(1);
        completionService = new ExecutorCompletionService<>(slaveExecutor);
        futures = new ConcurrentLinkedQueue<>();
        matcher = new HashMap<>();
    }

    /**
     * Cette methode permet de lie la tache a executer à son Future
     * afin de pouvoir ensuite notifier à l'envoyeur que la tache a ete executee
     *  
     * @param callable
     * @param f
     */
    private void link(FrameworkMessage<?> callable, Future<?> f) {
        matcher.put(f, callable);
    }
    
    /**
     * Ajout d'une tache
     * @param callable
     */
    private void addTask(FrameworkMessage<?> callable) {
    	addTask(callable,true);
    }

    /**
     * Ajout d'une tache
     * @param callable
     */
    private void addTask(FrameworkMessage<?> callable, boolean shouldAnswer) {     
    	if(slaveExecutor.isShutdown()) {
            slaveExecutor = Executors.newFixedThreadPool(4);
            completionService = new ExecutorCompletionService<>(slaveExecutor);
        }
        if(callable != null) {
            @SuppressWarnings("unchecked")
			Future<Object> f = completionService.submit((FrameworkMessage<Object>) callable);
            if(shouldAnswer) link(callable, f);
            futures.add(f);
        }
    }
    
    public static void launchTask(FrameworkMessage<?> callable) {
    	if (INSTANCE.isActived() == false) {
            INSTANCE.launchListener();
        }
        INSTANCE.addTask(callable,false);
    }

    /**
     * Cette methode permet de notifier l'envoyeur quand la tache a ete executee
     * @param f
     */
    private void onRecieveResponse(Future<?> f) {
        futures.remove(f);
        if(matcher.get(f) != null) {
        	Postman.sendResponse(matcher.get(f).getSenderId(), f, matcher.get(f));
        }
        matcher.remove(f);
    }

    /**
     * Cette methode lance le thread qui repartit les threads entre les threads esclaves
     */
    private void launchListener() {
        masterExecutor = Executors.newSingleThreadExecutor();
        masterExecutor.execute(new Runnable() {

            @Override
            public void run() {
                safeStop = false;
                shouldStop = false;
                isLaunched = true;
                while (!shouldStop) {
                    try {
                        Thread.sleep(200);
                        if (!futures.isEmpty()) {
                            onRecieveResponse(completionService.take());
                        }
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Erreur :", e);
                    }
                }
                if (safeStop) {
                    saveState();
                }
                isLaunched = false;
                slaveExecutor.shutdownNow();
            }
        });
    }

    public int getTaskNumber() {
        return futures.size();
    }

    private boolean isActived() {
        return isLaunched;
    }

    private void hardStop() {
        shouldStop = true;
    }

    private void safeStop() {
        safeStop = true;
        shouldStop = true;
    }

    /**
     * Cette methode permet lors de l'arret du systeme 
     * de sérialiser le message qui n'ont pas ete traite
     */
    private void saveState() {
        List<FrameworkMessage<?>> frameworkMessages = new ArrayList<>();
        
        for(Future<?> f : futures) {
            frameworkMessages.add(matcher.get(f));
        }
        
        Postman.serializeMessages(frameworkMessages);
    }

    /**
     * Cette methode permet de deserialiser les messages 
     * non executes quand le systeme a ete arrete
     */
    private void retreiveState() {
        Postman.retreiveSavedMessages();
    }

    public static boolean isLaunched() {
        return INSTANCE.isActived();
    }

    /**
     * Arret du systeme
     */
    public static void stop() {
        if (INSTANCE != null) {
            INSTANCE.safeStop();
        }
    }

    /**
     * Arret brutal du systeme
     */
    public static void kill() {
        if (INSTANCE != null) {
            INSTANCE.hardStop();
        }
    }

    static int getCurrentTaskNumber() {
        return INSTANCE.getTaskNumber();
    }

    /**
     * Ajout d'un message a executer
     * @param message
     */
    static void launch(FrameworkMessage<?> message) {
        if (INSTANCE.isActived() == false) {
            INSTANCE.launchListener();
        }
        INSTANCE.addTask((FrameworkMessage<?>) message);
    }
    
    public static void init() {
        INSTANCE.retreiveState();
    }
}
