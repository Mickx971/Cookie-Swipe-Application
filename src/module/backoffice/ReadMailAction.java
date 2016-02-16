/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package module.backoffice;

import cookie.swipe.application.CookieSwipeApplication;
import interfaces.IAction;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.JPanel;
import model.CustomMessage;
import module.ihm.ReadMailFrameInitializer;
import view.ReadMailCSFrame;
import view.component.CookieSwipeButtonAttach;

/**
 * @author Yehouda
 */
public class ReadMailAction implements IAction {
    
    private static ReadMailCSFrame frame;
    private static List<CookieSwipeButtonAttach> buttons;
    
    @Override
    public boolean execute(Object... object) {
        try {
            CustomMessage message = (CustomMessage) object[0];
            CookieSwipeApplication application = CookieSwipeApplication.getApplication();
            
            frame = new ReadMailCSFrame();
            new ReadMailFrameInitializer(frame).execute();
            // set frame
            frame.setCookieSwipeTextFieldObject(message.getSubject());

            frame.setCookieSwipeTextFieldFrom(getMailFromAddressArray(message.getFrom()));
            frame.setCookieSwipeTextFieldTo(getMailFromAddressArray(message.getRecipients(Message.RecipientType.TO)));
            frame.setCookieSwipeTextFieldToCc(getMailFromAddressArray(message.getRecipients(Message.RecipientType.CC)));
            
            String content = message.getMailAccount().getStoredContent(message);
            
            frame.setjTextAreaMail(content);
            if(buttons != null && buttons.size() > 0)
                frame.setCookieSwipeButtonAttach(buttons);
            application.setFocusFrame(frame);
            return true;
        } catch (MessagingException ex) {
            Logger.getLogger(ReadMailAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static String getMailFromAddressArray(Address[] address) {
        if (address == null) return "";
        String ret = "";
        int i = 0;
        for (Address addr : address) {
            String adresse = parseMail(addr);
            ret += i == 0 ? adresse : "; " + adresse;
            i++;
        }
        return ret;
    }
    
    public static String parseMail(Address address) {
        String adresse = address.toString();
        if (adresse.contains("[")) {
            adresse = adresse.substring(adresse.lastIndexOf("[") + 1, adresse.lastIndexOf("]"));
        }
        if (adresse.contains("<")) {
            adresse = adresse.substring(adresse.lastIndexOf("<") + 1, adresse.lastIndexOf(">"));
        }
        return adresse;
    }
    
    public static class ImagePanel extends JPanel {

        private BufferedImage image;

        public ImagePanel(String file) throws IOException {
            image = ImageIO.read(new File(file));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, null);
        }
    }

}
