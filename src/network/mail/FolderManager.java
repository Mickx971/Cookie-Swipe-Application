package network.mail;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.event.ConnectionListener;
import javax.mail.event.FolderListener;
import javax.mail.event.MessageChangedListener;
import javax.mail.event.MessageCountListener;

import model.MailAccount;
import network.messageFramework.AbstractSender;
import network.messageFramework.DeliverySystem;
import network.messageFramework.FrameworkMessage;

import com.sun.mail.imap.IMAPFolder;
import cookie.swipe.application.CookieSwipeApplication;

/**
 * Cette classe est responsable de la synchronisation des boites mails avec le serveur mail
 * @author mickx
 *
 */

public class FolderManager {

    private static final int preloadedMessagesCount = 5;
    private Map<IMAPFolder, Entry<Store, MailAccount>> folders;
    private Map<IMAPFolder, Integer> messagesCount;
    private ScheduledExecutorService synchronizer;

    public FolderManager() {
        this.folders = new ConcurrentHashMap<>();
        this.synchronizer = Executors.newSingleThreadScheduledExecutor();
        messagesCount = new HashMap<>();
    }

    /**
     * Cette methode permet d'ajouter des comptes mail a synchronise
     * @param mailAccounts
     */
    public void addMailAccounts(Collection<MailAccount> mailAccounts) {

        AbstractSender<Object> sender = new AbstractSender<Object>() {
            @Override
            public void onMessageReceived(Future<Object> receivedMessage) {
            }

            @Override
            public void onAllMessagesReceived() {
                start();
            }
        };

        List<FrameworkMessage<?>> sentMessages = new ArrayList<>();

        for (MailAccount mc : mailAccounts) {
            sentMessages.add(new FrameworkMessage<Object>() {
                private static final long serialVersionUID = 5861187087219031673L;

                @Override
                public Object call() throws Exception {
                    try {
                        addMailAccount(mc);
                    } catch (Exception e) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, "An error occured while opening store", e);
                    }
                    return null;
                }
            });
        }

        sender.sendMessages(sentMessages);
    }

    /**
     * Cette methode permet d'ajouter un compte mail a synchronise
     * @param mc
     */
    private void addMailAccount(MailAccount mc) {
        try {
            Store store = mc.getClientConnection();
            for (Folder f : store.getDefaultFolder().list()) {
                if (f.getName().toUpperCase().equals("INBOX")) {
                    mc.setDefaultFolderName( f.getName() );
                }
                if (open(f)) {
                    folders.put((IMAPFolder) f, new AbstractMap.SimpleEntry<Store, MailAccount>(store, mc));
                    addListeners((IMAPFolder) f, mc);
                    mc.createModelFor(f.getName());
                }
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "An error occured while opening folder", e);
        }
    }

    /**
     * Cette methode permet d'ajouter un compte mail a synchronise
     * et a précharger quelques mails
     * 
     * @param mc
     * @return
     */
    public boolean addNewMailAccount(MailAccount mc) {
        try {
            addMailAccount(mc);
            mc.setReady(true);
            retrieveMailsFor(mc);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "An error occured while opening store", e);
            return false;
        }
        return true;
    }

    /**
     * Cette methode permet de supprimer un compte mail
     * On arrete la synchronisation pour celui-ci
     * @param mc
     */
    public void removeMailAccount(MailAccount mc) {
        List<IMAPFolder> foldersToRemove = new ArrayList<>();
        for (IMAPFolder f : folders.keySet()) {
            if (folders.get(f).getValue().equals(mc)) {
                foldersToRemove.add(f);
            }
        }
        removeFolders(foldersToRemove);
    }

    /**
     * Cette methode permet d'arreter la synchronisation d'une collection de dossier IMAP
     * @param foldersToRemove
     */
    private void removeFolders(Collection<IMAPFolder> foldersToRemove) {
        removeFolderListener(foldersToRemove);
        closeFolders(foldersToRemove);
        for (IMAPFolder f : foldersToRemove) {
            folders.remove(f);
        }
    }

    /**
     * Cette methode permet de supprimer les listeners des dossiers IMAP passes en parametres
     * @param foldersToRemove
     */
    private void removeFolderListener(Collection<IMAPFolder> foldersToRemove) {
        for (IMAPFolder f : foldersToRemove) {
            removeListeners(f, folders.get(f).getValue());
        }
    }

    /**
     * Cette methode permet de fermer les dossier IMAP passes en parametres
     * @param foldersToRemove
     */
    private void closeFolders(Collection<IMAPFolder> foldersToRemove) {
        for (IMAPFolder f : foldersToRemove) {
            try {
                folders.get(f).getKey().close();
            } catch (MessagingException e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "An error occured while closing folder", e);
            }
        }
    }

    /**
     * Cette methode permet de demarrer le FolderManager
     * On precharge les mails des comptes et on commence la synchronisation
     */
    private void start() {
        retrieveMails();
        startSync();
    }

    /**
     * Cette methode permet de recuper les mails d'un compte mail
     * @param mc
     */
    private void retrieveMailsFor(MailAccount mc) {
        List<IMAPFolder> newFolders = new ArrayList<>();
        for (Folder f : folders.keySet()) {
            if (folders.get(f).getValue().equals(mc)) {
                newFolders.add((IMAPFolder) f);
            }
        }
        DeliverySystem.launchTask(new FrameworkMessage<Object>() {

            private static final long serialVersionUID = 7108005018953413117L;

            @Override
            public Object call() throws Exception {
                Map<IMAPFolder, Integer> lengths = new HashMap<>();
                int index = 0;

                for (IMAPFolder f : newFolders) {
                    lengths.put(f, f.getMessageCount());
                }

                for (int i = 0; i < preloadedMessagesCount; ++i) {
                    for (IMAPFolder f : newFolders) {
                        index = lengths.get(f) - i;
                        if (index > 0) {
                            mc.addToListOfmail(f.getName(), f.getMessage(index));
                        }
                    }
                }
                return null;
            }

        });
    }

    /**
     * Cette methode permet de recuper les mails de tous les comptes mail enregistres
     */
    private void retrieveMails() {
        try {
            Map<IMAPFolder, Integer> lengths = new HashMap<>();

            for (IMAPFolder f : folders.keySet()) {
                lengths.put(f, f.getMessageCount());
            }

            List<FrameworkMessage<?>> tasks = new ArrayList<>();
            for (int i = 0; i < preloadedMessagesCount; ++i) {
                tasks.add(new RetreiveMailMessage(lengths, i));
            }
            
            AbstractSender<Object> sender = new AbstractSender<Object>() {
                @Override
                public void onMessageReceived(Future<Object> receivedMessage) {
                }

                @Override
                public void onAllMessagesReceived() {
                    setAccountsReady();
                }
            };
            
            sender.sendMessages(tasks);
            
        } catch (MessagingException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "An error occured while retreiving mails", e);
        }
    }
    
    /**
     * Cette methode permet de recuperer les mails suivant d'une boite mail
     * @param mc
     */
    public void retrieveNextMailsFor(MailAccount mc, String folderName) {
    	try {
	    	IMAPFolder folderToLoad = null;
	    	for(IMAPFolder f : folders.keySet()) {
	    		if(f.getName().equals(folderName) && folders.get(f).getValue().equals(mc)) {
	    			folderToLoad = f;
	    			break;
	    		}
	    	}
                
                int lastDownloadIndex = 0;

                lastDownloadIndex = messagesCount.get(folderToLoad);
	    	
	    	Map<IMAPFolder, Integer> lengths = new HashMap<>();
	    	int maxCount = folderToLoad.getMessageCount();
	    	lengths.put(folderToLoad, maxCount);
	    	
	    	for(int j = lastDownloadIndex; j < lastDownloadIndex + preloadedMessagesCount && j < maxCount; ++j)
	    		DeliverySystem.launchTask(new RetreiveMailMessage(lengths,j));
    	}
    	catch(MessagingException e) {
    		Logger.getLogger(getClass().getName()).log(Level.SEVERE, "An error occured while retreiving mails", e);
    	}
	}

    /**
     * Cette methode permet de notifier que les comptes mails sont près a etre utilisés
     */
    private void setAccountsReady() {
        List<MailAccount> mailAccouts = CookieSwipeApplication.getApplication().getUser().getListOfMailAccount();
        for(MailAccount ma : mailAccouts) {
            ma.setReady(true);
        }
    }

    /**
     * Cette classe est responsable du téléchargement des mails des dossiers IMAP
     * @author mickx
     *
     */
    class RetreiveMailMessage extends FrameworkMessage<Object> {

        private static final long serialVersionUID = 3123849369769712930L;

        private Map<IMAPFolder, Integer> lengths;
        private int i;

        RetreiveMailMessage(Map<IMAPFolder, Integer> lengths, int i) {
            this.lengths = lengths;
            this.i = i;
        }

        @Override
        public Object call() throws Exception {
            for (IMAPFolder f : lengths.keySet()) {
                int index = lengths.get(f) - i;
                if (index > 0) {
                    MailAccount mc = folders.get(f).getValue();
                    mc.addToListOfmail(f.getName(), f.getMessage(index));
                }
                messagesCount.put(f,i);
            }
            return null;
        }
    }

    /**
     * Cette methode permet d'arreter la synchronisation
     * et d'effacer les dossiers qui etaient geres
     */
    public void closeSync() {
        stopSync();
        removeFolders(folders.keySet());
    }

    /**
     * Cette methode permet d'arret la synchronisation
     */
    public void stopSync() {
        synchronizer.shutdown();
    }

    /**
     * Cette methode permet de demarrer la synchronisation
     */
    public void startSync() {
        synchronizer.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                refresh();
            }
        }, 0, 60, TimeUnit.SECONDS);
    }
    
    /**
     * Cette methode permet de forcer un synchronisation
     */
    public void refresh() {
        for (IMAPFolder f : folders.keySet()) {
            DeliverySystem.launchTask(new FolderSynchronizationRequest(f));
        }
    }

    /**
     * Cette methode permet d'ouvrir un dossier IMAP
     * @param folder
     * @return
     * @throws MessagingException
     */
    private boolean open(Folder folder) throws MessagingException {
        if ((folder.getType() & Folder.HOLDS_MESSAGES) == Folder.HOLDS_MESSAGES) {
            if (!folder.isOpen()) {
                folder.open(Folder.READ_WRITE);
            }
            return true;
        }
        return false;
    }

    /**
     * Cette methode permet d'ajout un listener d'evenement a un dossier IMAP
     * @param folder
     * @param listener
     */
    public void addListeners(IMAPFolder folder, Object listener) {
        if (listener instanceof ConnectionListener) {
            folder.addConnectionListener((ConnectionListener) listener);
        }
        if (listener instanceof FolderListener) {
            folder.addFolderListener((FolderListener) listener);
        }
        if (listener instanceof MessageCountListener) {
            folder.addMessageCountListener((MessageCountListener) listener);
        }
        if (listener instanceof MessageChangedListener) {
            folder.addMessageChangedListener((MessageChangedListener) listener);
        }
    }

    /**
     * Cette methode permet d'enlever un listener au dossier IMAP passe en parametre
     * @param folder
     * @param listener
     */
    public void removeListeners(IMAPFolder folder, Object listener) {
        if (listener instanceof ConnectionListener) {
            folder.removeConnectionListener((ConnectionListener) listener);
        }
        if (listener instanceof FolderListener) {
            folder.removeFolderListener((FolderListener) listener);
        }
        if (listener instanceof MessageCountListener) {
            folder.removeMessageCountListener((MessageCountListener) listener);
        }
        if (listener instanceof MessageChangedListener) {
            folder.removeMessageChangedListener((MessageChangedListener) listener);
        }
    }

    /**
     * Cette classe est reponsable de la demande de notification de changement
     * auprès du serveur mail
     * @author mickx
     *
     */
    class FolderSynchronizationRequest extends FrameworkMessage<Object> {

        private static final long serialVersionUID = 764857642308858407L;
        private IMAPFolder folder;

        public FolderSynchronizationRequest(IMAPFolder folder) {
            this.folder = folder;
        }

        @Override
        public Boolean call() throws Exception {
            try {
                folder.getMessageCount();
            } catch (MessagingException e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "An error occured while synchronizing folder", e);
            }
            return null;
        }
    }
}
