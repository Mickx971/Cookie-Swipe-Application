/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import module.backoffice.ConnectAccountAction;
import module.backoffice.CreateMailAccountAction;
import module.backoffice.DeleteMailAccountAction;
import module.backoffice.DisconectAccountAction;
import module.backoffice.UpdateMailAccountAction;
import module.ihm.AddMailAccountFrameInitializer;
import module.ihm.MainFrameInitializer;
import module.ihm.UpdateAccountCSFrameInitializer;
import module.ihm.UpdateMailAccountFrameInitializer;
import module.ihm.WriteMailFrameInitializer;
import network.messageFramework.DeliverySystem;
import network.messageFramework.FrameworkMessage;
import view.AccountCSFrame;
import view.AcountMailCSFrame;
import view.MailCSFrame;
import view.MainCSFrame;
import view.component.CookieSwipeFrame;
import view.component.CookieSwipePasswordField;
import view.component.CookieSwipeTextField;
import cookie.swipe.application.CookieSwipeApplication;
import dao.DAOUser;
import java.io.File;
import java.util.List;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import model.CustomMessage;
import model.MailAccount;
import model.User;
import module.backoffice.CreateCSAccountAction;
import module.backoffice.ForgottenAction;
import module.backoffice.MailAction;
import module.backoffice.ReadMailAction;
import module.backoffice.SendMailAction;
import module.backoffice.UpdateCSAccountAction;
import module.ihm.BlacklistFrameInitializer;
import module.ihm.CreateAccountFrameInitializer;
import module.ihm.LoginForgottenFrameInitializer;
import module.ihm.PasswordForgottenFrameInitializer;
import network.mail.FolderManager;
import view.BlacklistCSFrame;
import view.LoginForgottenCSFrame;
import view.PasswordForgottenCSFrame;


public class Dispatcher implements ActionListener {

    /**
     * Distribue les actions de l'utilsiateur à des traitements
     *
     * @param e Evénement décrivant l'action à réaliser
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionName = e.getActionCommand() + "Action";
        try {
            Method actionToPerform = Dispatcher.class.getDeclaredMethod(actionName);
            actionToPerform.invoke(this);
        } catch (InvocationTargetException 
                |IllegalArgumentException 
                |IllegalAccessException 
                |NoSuchMethodException 
                |SecurityException ex ) {
            Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, "Unknown action: " + actionName, ex);
        }
    }

    public void logAccountAction() {
        CookieSwipeApplication application = CookieSwipeApplication.getApplication();
        String login    = ((CookieSwipeTextField)application.getMainFrameJComponent("cookieSwipeTextFieldLogin")).getText();
        String password = new String(((CookieSwipePasswordField)application.getMainFrameJComponent("cookieSwipePasswordFieldPassword")).getPassword());
        if (new ConnectAccountAction().execute(login, password)) {
            MainCSFrame frame = new MainCSFrame();
            application.getUser().addListMailAccountListeneur(frame);
            application.setMainFrame(frame);
            new MainFrameInitializer(frame).execute();
            DeliverySystem.launchTask(new FrameworkMessage<Object>() {
				private static final long serialVersionUID = -1840411908522351726L;
				@Override
				public Object call() throws Exception {
					application.getUser().retrieveMails();
					return null;
				}
			});
        }
    }

    public void updateAccountAction() {
        AccountCSFrame focusFrame = new AccountCSFrame();
        CookieSwipeApplication.getApplication().setFocusFrame(focusFrame);
        new UpdateAccountCSFrameInitializer(focusFrame).execute();
    }

    public void updateCSAccountAction() {
        CookieSwipeFrame focusFrame = CookieSwipeApplication.getApplication().getFocusFrame();
        if (new UpdateCSAccountAction(focusFrame).execute()) {
            focusFrame.dispose();
        }
    }
    
    public void forgottenPasswordAction() {
        PasswordForgottenCSFrame frame = new PasswordForgottenCSFrame();
        CookieSwipeApplication.getApplication().setFocusFrame(frame);
        new PasswordForgottenFrameInitializer(frame).execute();
    }
    
    public void sendMailForgottenPasswordAction() {
        PasswordForgottenCSFrame frame = (PasswordForgottenCSFrame) CookieSwipeApplication.getApplication().getFocusFrame();
        ForgottenAction action = new ForgottenAction(frame);
        if (action.execute("password"))
            frame.dispose();
    }

    public void forgottenLoginAction() {
        LoginForgottenCSFrame frame = new LoginForgottenCSFrame();
        CookieSwipeApplication.getApplication().setFocusFrame(frame);
        new LoginForgottenFrameInitializer(frame).execute();
    }
    
    public void sendMailForgottenLoginAction() {
        LoginForgottenCSFrame frame = (LoginForgottenCSFrame) CookieSwipeApplication.getApplication().getFocusFrame();
        ForgottenAction action = new ForgottenAction(frame);
        if (action.execute("login"))
            frame.dispose();
    }

    public void addMailAccountAction() {
        AcountMailCSFrame focusFrame = new AcountMailCSFrame();
        CookieSwipeApplication.getApplication().setFocusFrame(focusFrame);
        new AddMailAccountFrameInitializer(focusFrame).execute();
    }

    public void logoutAction() {
        new DisconectAccountAction().execute();
    }

    public void createMailAccountAction() {
        CookieSwipeFrame focusFrame = CookieSwipeApplication.getApplication().getFocusFrame();
        if (new CreateMailAccountAction(focusFrame).execute()) {
            focusFrame.dispose();
        }
    }

    public void forwardMailAction() {
        CookieSwipeFrame focusFrame = new MailCSFrame();
        CookieSwipeApplication.getApplication().setFocusFrame(focusFrame);
        new WriteMailFrameInitializer(focusFrame).execute();
    }

    public void answerMailAction() {
        CookieSwipeFrame focusFrame = new MailCSFrame();
        CookieSwipeApplication.getApplication().setFocusFrame(focusFrame);
        new WriteMailFrameInitializer(focusFrame).execute();
    }

    public void removeBlacklistSenderAction() {
        User usr = CookieSwipeApplication.getApplication().getUser();
        usr.removeBlackListSender((String) CookieSwipeApplication.getApplication().getParam("blackListedElementSelected"));
        DAOUser.updateBlackListUser(usr);
    }
    
    public void showBlacklistSenderAction() {
        BlacklistCSFrame frame = new BlacklistCSFrame();
        new BlacklistFrameInitializer(frame).execute();
    }

    public void addBlackListSenderAction() throws MessagingException {
        Message message = (Message) CookieSwipeApplication.getApplication().getParam("selectedMail");
        User usr = CookieSwipeApplication.getApplication().getUser();
        for (Address address : message.getFrom()) {
            usr.blackListSender(address.toString());
        }
        JOptionPane.showMessageDialog(null, "Cet/Ces adresse mail à bien été ajouté a la black list",
                "black list", JOptionPane.INFORMATION_MESSAGE);
    }

    public void deleteMailAction() {
        MailAccount mailAccount = (MailAccount) CookieSwipeApplication.getApplication().getParam("mailAccountSelected");
        mailAccount.removeToListOfmail((String)CookieSwipeApplication.getApplication().getParam("folderName"),
                                        (CustomMessage) CookieSwipeApplication.getApplication().getParam("selectedMail"));
    }

    public void selectMailAction() { // ne sert a rien
        System.err.println("NOT IMPLEMENTED");
    }

    public void readMailAction() {
        Object message = CookieSwipeApplication.getApplication().getParam("selectedMail");
        if (message != null) {
            new ReadMailAction().execute(message);
        }
    }
    
    public void replyAction() {
        Object message = CookieSwipeApplication.getApplication().getParam("selectedMail");
        if(message != null)
            new MailAction().execute("reply", message);
    }
    
    public void replyAllAction() {
        Object message = CookieSwipeApplication.getApplication().getParam("selectedMail");
        if(message != null)
            new MailAction().execute("replyAll", message);
    }
    
    public void forwardAction() {
        Object message = CookieSwipeApplication.getApplication().getParam("selectedMail");
        if(message != null)
            new MailAction().execute("forward", message);
    }
    
    public void addAttachementAction() {
        MailAccount mailAccount = (MailAccount) CookieSwipeApplication.getApplication().getParam("mailAccountSelected");
        JFileChooser choose = new JFileChooser();
        choose.setMultiSelectionEnabled(true);
        MailCSFrame frame = (MailCSFrame) CookieSwipeApplication.getApplication().getFocusFrame();
        int val = choose.showOpenDialog(frame);
        File[] files = choose.getSelectedFiles();
        if(val == JFileChooser.APPROVE_OPTION)  {
            mailAccount.addAttachements(files);
            for(File file : files)
                frame.addAttach(file.getName());
        }
    }
    
    public void writeMailAction() {
        CookieSwipeFrame focusFrame = new MailCSFrame();
        CookieSwipeApplication.getApplication().setFocusFrame(focusFrame);
        new WriteMailFrameInitializer(focusFrame).execute();
    }

    public void deleteMailAccountAction() {
        new DeleteMailAccountAction().execute();
    }

    public void updateMailAccountAction() {
        CookieSwipeFrame focusFrame = CookieSwipeApplication.getApplication().getFocusFrame();
        if (new UpdateMailAccountAction(focusFrame).execute()) {
            focusFrame.dispose();
        }
    }

    public void selectMailAccountAction() {
        AcountMailCSFrame focusFrame = new AcountMailCSFrame();
        CookieSwipeApplication.getApplication().setFocusFrame(focusFrame);
        new UpdateMailAccountFrameInitializer(focusFrame).execute();
    }

    public void createAccountAction() { // compte cookie swipe a créé
        CookieSwipeApplication application = CookieSwipeApplication.getApplication();
        CookieSwipeFrame frame = application.getFocusFrame();
        String login    = ((CookieSwipeTextField)application.getFocusFrameJComponent("cookieSwipeTextFieldLoginAdressMail")).getText();
        String pwd    = new String( ((CookieSwipePasswordField)application.getFocusFrameJComponent("cookieSwipePasswordFieldPassword")).getPassword() );
        String backup    = ((CookieSwipeTextField)application.getFocusFrameJComponent("cookieSwipeTextFieldBackupMail")).getText();
        boolean created = new CreateCSAccountAction().execute(login, pwd, backup);
        if(created) 
            frame.dispose();
        if (created && new ConnectAccountAction().execute(login, pwd)) {
            MainCSFrame mainFrame = new MainCSFrame();
            application.getUser().addListMailAccountListeneur(mainFrame);
            application.setMainFrame(mainFrame);
            new MainFrameInitializer(mainFrame).execute();
            DeliverySystem.launchTask(new FrameworkMessage<Object>() {
				@Override
				public Object call() throws Exception {
					application.getUser().retrieveMails();
					return null;
				}
			});
        }
    }
    
    public void inscriptionAction() {
        CookieSwipeApplication application = CookieSwipeApplication.getApplication();
        AccountCSFrame frame = new AccountCSFrame();
        application.setFocusFrame(frame);
        new CreateAccountFrameInitializer(frame).execute();
    }

    public void sendMailAction() {
        CookieSwipeFrame focusFrame = CookieSwipeApplication.getApplication().getFocusFrame();
        if (new SendMailAction(focusFrame).execute())
            focusFrame.dispose();
    }
    
    public void refreshAction() {
        FolderManager fm = (FolderManager) CookieSwipeApplication.getApplication().getParam("FolderManager");
        fm.refresh();
    }
}
