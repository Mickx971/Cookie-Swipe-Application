/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package module.backoffice;

import cookie.swipe.application.CookieSwipeApplication;
import interfaces.IAction;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import model.CustomMessage;
import model.MailAccount;
import static module.backoffice.ReadMailAction.getMailFromAddressArray;
import module.ihm.WriteMailFrameInitializer;
import view.MailCSFrame;

/**
 *
 * @author Yehouda
 */
public class MailAction implements IAction {

    @Override
    public boolean execute(Object... object) {
        String action = (String) object[0];
        CustomMessage message = (CustomMessage) object[1];
        MailAccount mailAccount = (MailAccount) CookieSwipeApplication.getApplication().getParam("mailAccountSelected");
        String startSubject = "";
        MailCSFrame mailFrame = new MailCSFrame();
        try {
            switch (action) {
                case "forward":
                    startSubject = "FW : ";
                case "replyAll":
                    mailFrame.setCookieSwipeTextFieldToCc(getMailFromAddressArray(message.getRecipients(Message.RecipientType.CC)));
                case "reply":
                    if (action.startsWith("reply")) {
                        startSubject = "RE : ";
                        mailFrame.setCookieSwipeTextFieldTo(getMailFromAddressArray(message.getFrom()));
                    }
                    mailFrame.setCookieSwipeTextFieldSubject(startSubject + message.getSubject());
                    mailFrame.setjTextAreaMail(mailTranfer(message));

                    CookieSwipeApplication.getApplication().setFocusFrame(mailFrame);
                    new WriteMailFrameInitializer(mailFrame).execute();

                    break;
            }
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(MailAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    private String mailTranfer(CustomMessage message) throws MessagingException, IOException {
        return "\n\n\n----------------------------------------\n"
                + "From : " + Arrays.toString(message.getFrom()) + "\n"
                + "To : " + Arrays.toString(message.getRecipients(Message.RecipientType.TO)) + "\n"
                + "Date : " + message.getReceivedDate() + "\n\n"
                + message.getMailAccount().getStoredContent(message)
                + "\n\n";
    }
}
