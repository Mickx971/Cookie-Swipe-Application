package cookie.swipe.application;


import java.util.HashMap;

import javax.swing.UnsupportedLookAndFeelException;
import model.MailAccount;

import model.User;
import module.ihm.LoginFrameInitializer;
import network.mail.FolderManager;
import network.messageFramework.DeliverySystem;
import view.LoginJFrame;
import view.MainCSFrame;
import view.component.CookieSwipeFrame;

/**
 * Point de démarage de l'application
 *
 * @author Mary
 */
public class CookieSwipeApplication {

    private static CookieSwipeApplication application;
    
    private User user;
    
    private CookieSwipeFrame mainFrame, focusFrame;
    private final HashMap<String,Object> params;
    //private HashMap<String,Object> params; Compatibilité Java 7
    
    public CookieSwipeApplication() {
        user = new User();
        params = new HashMap<>();
    }
    
    private void start() {
    	initLookAndFeel();       
        setParam("FolderManager", new FolderManager());
        setParam(MailAccount.ROOT_CACHE_DIR, getTempFolder());
        LoginJFrame loginFrame = new LoginJFrame();
        this.mainFrame = loginFrame;
        new LoginFrameInitializer(loginFrame).execute();
    }
    
    public void stop() {
        DeliverySystem.stop();
        System.exit(0);
    }
    
    private void initLookAndFeel() {
    	try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException
        		|InstantiationException
        		|IllegalAccessException
        		|UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainCSFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }	
    }

	public void setMainFrame(CookieSwipeFrame mainCSFrame) {
        if(this.mainFrame != null) this.mainFrame.dispose();
        this.mainFrame = mainCSFrame;
    }
    
    public void setFocusFrame(CookieSwipeFrame focusFrame) {
        if(this.focusFrame != null) this.focusFrame.dispose();
        this.focusFrame = focusFrame;
    }
    
    public CookieSwipeFrame getFocusFrame() {
        return focusFrame;
    }
    
    public CookieSwipeFrame getMainFrame() {
        return mainFrame;
    }
    
    public Object getMainFrameJComponent(String identifier) {
        return mainFrame.getJComponent().get(identifier);
    }
    
    public Object getFocusFrameJComponent(String identifier) {
        return focusFrame.getJComponent().get(identifier);
    }
    
    public void setParam(String key, Object value) {
        params.put(key,value);
    }
    
    public Object getParam(String key) {
        return params.get(key);
    }
    
    public User getUser() {
        return user;
    }
    
    public static CookieSwipeApplication getApplication() {
        if(application == null) {
            application = new CookieSwipeApplication();
        }
        return application;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        CookieSwipeApplication.getApplication().start();
    }

    private String getTempFolder() {
        return System.getProperty("java.io.tmpdir");
    }
}
