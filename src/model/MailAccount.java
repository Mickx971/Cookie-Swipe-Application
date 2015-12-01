/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;
import javax.mail.event.FolderEvent;
import javax.mail.event.FolderListener;
import javax.mail.event.MessageChangedEvent;
import javax.mail.event.MessageChangedListener;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.SwingUtilities;

import module.ihm.CustomJListModel;
import module.ihm.MainFrameInitializer;
import network.messageFramework.AbstractSender;
import network.messageFramework.FrameworkMessage;

import com.sun.mail.imap.IMAPFolder;

import cookie.swipe.application.CookieSwipeApplication;
import cookie.swipe.application.SystemSettings;
import cookie.swipe.application.utils.LinkedHashSetPriorityQueueObserver;
import cookie.swipe.application.utils.ObservableLinkedHashSetPriorityQueue;
import errorMessage.CodeError;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.UIDFolder;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import javax.swing.JOptionPane;

/**
 *
 * @author Mary
 */
public class MailAccount implements ConnectionListener, MessageChangedListener, MessageCountListener, FolderListener {

    public static final String ALL = "Tous";
    public static final String ROOT_CACHE_DIR = "ROOT_CACHE_DIRECTORY";

    // Variable membre
    private int id;
    private String address, CSName, password, color, mailSignature;
    private Domain domain;
    private Date lastSynch;
    private Session session;
    private Message currentMessage;
    private HashMap<String, ObservableLinkedHashSetPriorityQueue> folderListModels;
    private List<String> folderNames;
    private List<File> attachments;
    private Multipart multipart;
    private MimeBodyPart messageBodyPart;
    private String defaultFolderName;
    private volatile boolean isReady = false;
    private CacheManager cacheManager;
    private String mailAccountPath;

    // Constructeur
    /**
     * Constructeur par defaut
     */
    public MailAccount() {
        domain = new Domain();
        this.folderNames = new ArrayList<>();
        this.attachments = new ArrayList<>();
        this.folderListModels = new HashMap<>();
        cacheManager = new CacheManager();
    }
    
    private String createCachePath() {
        String rootPath = (String) 
                CookieSwipeApplication.getApplication().getParam(ROOT_CACHE_DIR);
        return rootPath + SystemSettings.SEPARATOR + address;
    }

    /**
     * Constructeur a utiliser lors de l'ajout d'une boite courriel
     *
     * @param CSName nom donne a la botie courriel
     * @param address adresse courriel de la boite
     * @param password mot de passe pour acceder a la boite courriel
     * @param color couleur donne a la boite courriel pour l'affichage des
     * courriels
     */
    public MailAccount(String CSName, String address, Domain domain,
            String password) {

        this.CSName = CSName;
        this.address = address;
        this.domain = domain;
        try {
            this.password = new Encryption().encrypt(password);
        } catch (Exception ex) {
            Logger.getLogger(MailAccount.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        this.color = color;
        this.folderNames = new ArrayList<>();
        this.folderListModels = new HashMap<>();
    }

    /**
     * Constructeur a utiliser pour charger les comptes courriel dans
     * l'application
     *
     * @param id identifiant unique de la boite mail
     * @param CSName nom donne a la boite courriel
     * @param address adresse courriel de la boite
     * @param password mot de passe pour acceder a la boite mail
     * @param color couleur donne a la boite courriel pour l'affichage des
     * courriels
     * @param domain domaine de l'adresse courriel
     * @param mailSignature signature a mettre a la fin d'un courriel
     * @param lastSynch date de la derniere synchronisation reussi de la boite
     * courriel
     * @param listOfMail Liste des courriels de la boite mail
     */
    public MailAccount(int id, String CSName, String address, String password,
            String color, Domain domain, String mailSignature, Date lastSynch,
            Collection<Message> listOfMail) {
        this.id = id;
        this.address = address;
        this.CSName = CSName;
        try {
            this.password = new Encryption().encrypt(password);
        } catch (Exception ex) {
            Logger.getLogger(MailAccount.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        this.color = color;
        this.domain = domain;
        this.mailSignature = mailSignature;
        this.lastSynch = lastSynch;
        this.folderNames = new ArrayList<>();
        this.folderListModels = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public void createModelFor(String folder) {
        this.folderNames.add(folder);

        ObservableLinkedHashSetPriorityQueue observableList
                = new ObservableLinkedHashSetPriorityQueue(new MailComparator());

        folderListModels.put(folder, observableList);

        HashMap<String, HashMap<String, CustomJListModel>> models
                = (HashMap<String, HashMap<String, CustomJListModel>>) CookieSwipeApplication.getApplication().getParam("jListMailModels");

        HashMap<String, CustomJListModel> jListModels = models.get(getCSName());

        if (jListModels == null) {
            jListModels = new HashMap<>();
            models.put(getCSName(), jListModels);
        }

        jListModels.put(folder, new CustomJListModel(observableList));

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrameInitializer().addFolderInTree(MailAccount.this, folder);
            }
        });
    }

    public boolean connectionIsOk() {
        try {
            getClientConnection();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public String getDefaultFolderName() {
        return defaultFolderName;
    }
    
    public void setDefaultFolderName(String name) {
        this.defaultFolderName = name;
    }

    public Store getClientConnection() throws MessagingException, Exception {
        // Get a Properties object
        Properties props = System.getProperties();

        // Get a Session object
        Session session = Session.getInstance(props, null);
        session.setDebug(false);

        Store store = session.getStore(domain.getStoreProtocol());

        // Ouvrir la connexion
        store.connect(domain.getServerIn(), address, new Encryption().decrypt(password));

        return store;
    }

    public Properties getProperties(int i) {
        Properties props = new Properties();
        if(i == 2)            
            props.setProperty("mail.transport.protocol", "smtp");
        else
            props.setProperty("mail.transport.protocol", domain.getStoreProtocol());
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.host", domain.getServerOut());
        props.setProperty("mail.smtp.socketFactory.port", domain.getPortOut());
        props.put("mail.smtp.socketFactory.fallback", "true");
        try{
            if(Integer.parseInt(domain.getPortOut()) == 465)
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }catch(NumberFormatException e){
            e.printStackTrace();
        }
        props.put("mail.smtp.user", address);
        props.put("mail.smtp.port", domain.getPortOut());
        props.put("mail.from", domain.getServerIn());
        return props;
    }

    public Session getSession(int i) {
        if (session == null) {
            session = Session.getDefaultInstance(getProperties(i),
                    new javax.mail.Authenticator() {
                        @Override
                        public javax.mail.PasswordAuthentication getPasswordAuthentication() {
                            String pwd = "";
                            try {
                                pwd = new Encryption().decrypt(password);
                            } catch (Exception ex) {
                                Logger.getLogger(MailAccount.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            return new javax.mail.PasswordAuthentication(address, pwd);
                        }
                    }
            );
        }
        return session;
    }

    /**
     * Sert a creer un nouveau courriel
     *
     * @return courriel cree
     */
    public void createNewMail() {
        messageBodyPart = new MimeBodyPart();
        currentMessage = new MimeMessage(getSession(2));
    }

    public void addDestinataire(Address[] destinataire) {
        try {
            currentMessage.addRecipients(Message.RecipientType.TO, destinataire);
        } catch (MessagingException ex) {
            Logger.getLogger(MailAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addDestinataire(String destinataire) {
        try {        
            try {
                currentMessage.setFrom(new InternetAddress(address, address));
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(MailAccount.class.getName()).log(Level.SEVERE, null, ex);
            }
            for(String dest : destinataire.split(";")) 
                currentMessage.addRecipients(Message.RecipientType.TO, InternetAddress.parse(dest.trim()));
        } catch (MessagingException ex) {
            Logger.getLogger(MailAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addSubject(String subject) {
        try {
            currentMessage.setSubject(subject);
        } catch (MessagingException ex) {
            Logger.getLogger(MailAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void clearAttachments() {
        attachments.clear();
    }

    public void addBody(String body) {
        try {
            if(attachments.size() > 0)
                messageBodyPart.setText(body);
            else
                currentMessage.setText(body);
        } catch (MessagingException ex) {
            Logger.getLogger(MailAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addCopie(String copy) {
        try {
            currentMessage.addRecipients(Message.RecipientType.CC, InternetAddress.parse(copy));
        } catch (MessagingException ex) {
            Logger.getLogger(MailAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sert a envoyer un courriel, delegue l'envoi a un domaine )
     */
    public void sendMail() {
        System.out.println("pj length : " + attachments.size());
        AbstractSender<Boolean> s = new AbstractSender<Boolean>() {
            @Override
            public void onMessageReceived(Future<Boolean> receivedMessage) {
                try {
                    boolean sended = receivedMessage.get();
                    int error;
                    if (sended) {
                        error = CodeError.SUCESS;
                    } else {
                        error = CodeError.CONNEXION_FAIL;
                    }
                    switch (error) {
                        case CodeError.SUCESS:
                            JOptionPane.showMessageDialog(null, "Votre compte mail à bien été envoyé",
                                    "Envoi d'un mail", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        case CodeError.CONNEXION_FAIL:
                            JOptionPane.showMessageDialog(null, "Problème de connexion \nCode erreur : " + error,
                                    "Envoi d'un mail", JOptionPane.ERROR_MESSAGE);
                            break;
                        
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(MailAccount.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(MailAccount.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        };
        
        s.sendMessage(new FrameworkMessage< Boolean>() {
            @Override
            public Boolean call() throws Exception {

                try {
                    if ( attachments.size() > 0 ) {
                        multipart = getMultiPart();
                        if(multipart != null)
                            currentMessage.setContent(multipart);
                    }                    
                    Transport.send(currentMessage);
                    return true;
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }
    
    public Multipart getMultiPart() {
        if( multipart != null )
            return multipart;
        
        try {
            multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            for (File file : attachments) {
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource( file );
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName( file.getName() );
                multipart.addBodyPart(messageBodyPart);
            }
            return multipart;
        } catch (MessagingException ex) {
            Logger.getLogger(MailAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void setMessage(Message mess) {
        currentMessage = mess;
    }

    /**
     * Sert a supprimer un courriel de la boite courriel
     *
     * @param mail courriel a supprimer
     * @return Si la suppresion c'est bien passe
     */
    public boolean deleteMail(Mail mail) {
        return false;
    }

    public void addAttachements(File[] files) {
        attachments.addAll(Arrays.asList(files));
    }
    public void addAttachement(File file) {
        attachments.add(file);
    }

    public void addAttachement(String filename) {
        attachments.add(new File(filename));
    }

    // Getter & setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public int setAddress(String address) {
        String mailDomain = address.substring(address.indexOf('@') + 1);
        try {
            domain = Domain.getDomainFor(mailDomain);
        } catch (Exception ex) {
            Logger.getLogger(MailAccount.class.getName()).log(Level.SEVERE, "", ex);
            return CodeError.FAILLURE;
        }
        this.address = address;
        mailAccountPath = createCachePath();
        return CodeError.SUCESS;
    }

    public String getCSName() {
        return CSName;
    }

    public void setCSName(String CSName) {
        this.CSName = CSName;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMailSignature() {
        return mailSignature;
    }

    public void setMailSignature(String mailSignature) {
        this.mailSignature = mailSignature;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Date getLastSynch() {
        return lastSynch;
    }

    public void setLastSynch(Date lastSynch) {
        this.lastSynch = lastSynch;
    }

    public Mail[] getListOfmail(String folderName) {
        return folderListModels.get(folderName).toArray(new Mail[folderListModels.get(folderName).size()]);
    }

    public void addToListOfmail(String folderName, Message message) throws MessagingException {
        if (!isInBlackList(message)) {
            folderListModels.get(folderName).add(new CustomMessage(message, this));
            cacheManager.storeMessage(message);
        }
        
    }

    public void removeToListOfmail(String folderName, Message message) {
        try {
            message.setFlag(Flags.Flag.DELETED, true);
        } catch (MessagingException ex) {
            Logger.getLogger(MailAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        folderListModels.get(folderName).remove(message);
    }

    public void addToListOfmail(String folderName, Message[] listOfmail) throws MessagingException {
        for(Message message : listOfmail) {
            if(!isInBlackList(message))
                folderListModels.get(folderName).add(new CustomMessage(message, this));
        }
    }

    public void removeToListOfmail(String folderName, List<Message> listOfmail) {
        folderListModels.get(folderName).removeAll(listOfmail);
    }

    public void removeToListOfmail(String folderName, Message[] listOfmail) {
        folderListModels.get(folderName).removeAll(Arrays.asList(listOfmail));
    }

    public List<String> getFolderNames() {
        return this.folderNames;
    }

    public void addObservableTo(String observableName, LinkedHashSetPriorityQueueObserver obs) {
        if (folderNames.contains(observableName) || ALL.equals(observableName)) {
            folderListModels.get(observableName).addObserver(obs);
        } else {
            throw new IllegalArgumentException("Ask for unknown observable: " + observableName);
        }
    }

    public ObservableLinkedHashSetPriorityQueue getListModelFor(String folderName) {
        return this.folderListModels.get(folderName);
    }

    @Override
    public void messagesAdded(MessageCountEvent event) {
        System.out.println("added");
        IMAPFolder f = (IMAPFolder) event.getSource();

        Message[] messages = event.getMessages();
        User usr = CookieSwipeApplication.getApplication().getUser();
        for(Message message : messages) {
            try {
                boolean addToMail = true;
                for (Address address : message.getFrom()) {
                    if (usr.getBlackList().contains(address.toString())) {
                        addToMail = false;
                    }
                }
                if (addToMail) {
                    addToListOfmail(f.getName(), message);
                } else {
                    removeToListOfmail(f.getName(), message);
                }
            } catch (MessagingException ex) {
                Logger.getLogger(MailAccount.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public boolean isInBlackList(Message message) throws MessagingException {
        boolean inBlacklist = false;
        User usr = CookieSwipeApplication.getApplication().getUser();
        for (Address address : message.getFrom()) {
            if (usr.getBlackList().contains(address.toString())) {
                inBlacklist = true;
                break;
            }
        }
        return inBlacklist;
    }

    @Override
    public void messagesRemoved(MessageCountEvent event) {
        IMAPFolder f = (IMAPFolder) event.getSource();
        removeToListOfmail(f.getName(), event.getMessages());
    }

    @Override
    public void messageChanged(MessageChangedEvent arg0) {
        Message msg  = arg0.getMessage();
        CookieSwipeApplication.getApplication().getFocusFrame().refresh();        
    }

    @Override
    public void closed(ConnectionEvent arg0) {
        System.out.println("closed");
    }

    @Override
    public void disconnected(ConnectionEvent arg0) {
        System.out.println("disconnected");
    }

    @Override
    public void opened(ConnectionEvent arg0) {
        System.out.println("ConnectionEvent");
    }

    @Override
    public void folderCreated(FolderEvent arg0) {
        System.out.println("FolderEvent");
    }

    @Override
    public void folderDeleted(FolderEvent arg0) {
        System.out.println("folderDeleted");
    }

    @Override
    public void folderRenamed(FolderEvent arg0) {
        System.out.println("folderRenamed");
    }

    // equals & hashcode
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.id;
        hash = 97 * hash + Objects.hashCode(this.address);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MailAccount other = (MailAccount) obj;
        if (this.id != other.id) {
            return false;
        }
        return Objects.equals(this.address, other.address);
    }

    @Override
    public String toString() {
        return CSName;
    }

    public boolean isReady() {
        return this.isReady;
    }
    
    public void setReady(boolean b) {
        this.isReady = b;
    }

    public void clearMailFrom(String sender) {
        Collection<ObservableLinkedHashSetPriorityQueue> folders = folderListModels.values();
        for(ObservableLinkedHashSetPriorityQueue folder : folders) {
            try {
                folder.clearMailFrom(sender);
            } catch (MessagingException ex) {
                Logger.getLogger(MailAccount.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getStoredContent(CustomMessage message) {
        try {
            String content = cacheManager.getContentFor(message);
            if(!content.isEmpty()) {
                System.out.println(content);
            }
        } catch (Exception ex) {
            Logger.getLogger(MailAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "ERROR";
    }

    public class CacheManager {
        private String CONTENT_FILE_NAME = "messageContent.html";
        
        public void storeMessage(Message message) {
            try {
                if(isStored(message)) 
                    return;
                Path dirName = getCacheFor(message);
                Files.createDirectories(dirName);
                Path contentDir = Paths.get(dirName.toString(), "content");
                Path attachment = Paths.get(dirName.toString(), "attachment");
                Files.createDirectory(contentDir);
                Files.createDirectory(attachment);
                serializeText(getMessageText(message), contentDir);
                downLoadMessageAttachements(message, attachment);
            }
            catch(Exception ex) {
                Logger.getLogger(MailAccount.class.getName()).log(Level.SEVERE, "ChacheManager Exception", ex);
            }
        }
        
        private String getMessageText(Message message) throws Exception {
            String content = null;
            if(message.getContent() instanceof Multipart) {
                content = getTextContentMessageFormMultipart((Multipart)message.getContent());
            }
            else {
                content = (String) message.getContent(); 
            }
            return content;
        }
        
        private String getTextContentMessageFormMultipart(Multipart multipart) throws Exception {
            StringBuilder html = new StringBuilder();
            StringBuilder text = new StringBuilder();
            
            for (int i = 0; i < multipart.getCount(); ++i) {
                
                BodyPart bodyPart = multipart.getBodyPart(i);
                String htmlContent = getPartContent(bodyPart, "text/html");
                String textContent = getPartContent(bodyPart, "text/text");
                
                if(htmlContent != null && !htmlContent.isEmpty())
                    html.append(htmlContent);
                
                if(textContent != null && !textContent.isEmpty())
                    text.append(textContent);
            }
            
            if(html.length() != 0)
                return html.toString();
            return text.toString();
        }
        
    public String getPartContent(Part p, String type) throws MessagingException, IOException {
        if (p.isMimeType(type)) return (String) p.getContent();

        if (p.isMimeType("multipart/alternative")) {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/*")) {
                    String text = getPartContent(bp,type);
                    if(text != null) return text;
                } 
                else return getPartContent(bp,type);
            }
        } 
        else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getPartContent(mp.getBodyPart(i), type);
                if (s != null) return s;
            }
        }
        return null;
    }
        
        public void download(BodyPart bodyPart, Path path) throws Exception {
            InputStream is = bodyPart.getInputStream();
            path = path.resolve(bodyPart.getFileName());
            File file = Files.createFile(path).toFile();

            try (FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buf = new byte[4096];
                int bytesRead;
                while((bytesRead = is.read(buf))!=-1) {
                    fos.write(buf, 0, bytesRead);
                }
            }
        }
        
        private void serializeText(String messageText, Path contentDir) throws Exception {
            Path path = contentDir.resolve(CONTENT_FILE_NAME);
            PrintWriter printer = new PrintWriter(Files.createFile(path).toFile());
            printer.println(messageText);
            printer.close();
        }

        private void downLoadMessageAttachements(Message message, Path attachmentPath) throws Exception {
            if(message.getContent() instanceof Multipart) {
                multipart = (Multipart) message.getContent();
                for (int i = 0; i < multipart.getCount(); ++i) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())
                            && !bodyPart.getFileName().trim().isEmpty()) {
                        download(bodyPart, attachmentPath);
                    }
                }
            }
        }
        
        private String getMessageId(Message message) throws MessagingException {
            UIDFolder uidf = (UIDFolder) message.getFolder();
            long msgId = 0;
            if(message instanceof CustomMessage)
                msgId = uidf.getUID(((CustomMessage)message).getMessage());
            else 
                msgId = uidf.getUID(message);
            return String.valueOf(msgId);
        }

        private Path getCacheFor(Message message) throws Exception {
            String msgId = getMessageId(message);
            return Paths.get( mailAccountPath, message.getFolder().getName(), msgId);
        }

        private boolean isStored(Message message) throws Exception {
            Path dirPath = getCacheFor(message);
            return Files.exists(dirPath);
        }

        private String getContentFor(Message message) throws Exception {
            Path p = getCacheFor(message);
            p = Paths.get(p.toString(), "content/" + CONTENT_FILE_NAME);
            byte[] encoded = Files.readAllBytes(p);
            return new String(encoded, Charset.defaultCharset());
        }
    }

}
