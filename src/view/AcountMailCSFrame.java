package view;

import interfaces.IJFrame;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import view.component.CookieSwipeButton;
import view.component.CookieSwipeFrame;
import view.component.CookieSwipeLabel;
import view.component.CookieSwipePasswordField;
import view.component.CookieSwipeTextField;

public class AcountMailCSFrame extends CookieSwipeFrame implements IJFrame {

    private CookieSwipeButton cookieSwipeButtonCancel;
    private CookieSwipeButton cookieSwipeButtonValidate;
    
    private CookieSwipeLabel cookieSwipeLabel1;
    private CookieSwipeLabel cookieSwipeLabelMailAddress;
    private CookieSwipeLabel cookieSwipeLabelNameAcountMail;
    private CookieSwipeLabel cookieSwipeLabelPasswordAccountMail;
    
    private CookieSwipePasswordField cookieSwipePasswordFieldPasswordAccountMail;
    
    private CookieSwipeTextField cookieSwipeTextFieldMailAddress;
    private CookieSwipeTextField cookieSwipeTextFieldNameAcountMail;
   
    private JComboBox jComboBoxColor;
    
    
    public AcountMailCSFrame() {
    	setTitle("Ajout de compte mail");
    	
        initComponents();
        
        this.pack();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
        this.setVisible(true); 
        setResizable(false);
        
        hsJcomponent.put("cookieSwipeButtonCancel", cookieSwipeButtonCancel);
        hsJcomponent.put("cookieSwipeButtonValidate", cookieSwipeButtonValidate);
        hsJcomponent.put("cookieSwipeLabelMailAddress", cookieSwipeLabelMailAddress);
        hsJcomponent.put("cookieSwipeLabelNameAcountMail", cookieSwipeLabelNameAcountMail);
        hsJcomponent.put("cookieSwipeLabelPasswordAccountMail", cookieSwipeLabelPasswordAccountMail);
        hsJcomponent.put("cookieSwipePasswordFieldPasswordAccountMail", cookieSwipePasswordFieldPasswordAccountMail);
        hsJcomponent.put("cookieSwipeTextFieldMailAddress", cookieSwipeTextFieldMailAddress);
        hsJcomponent.put("cookieSwipeTextFieldNameAcountMail", cookieSwipeTextFieldNameAcountMail);
        hsJcomponent.put("jComboBoxColor", jComboBoxColor);

    }
    
    private void initFrame(){
    	initComponents();
    	placeComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        cookieSwipeLabelNameAcountMail = new view.component.CookieSwipeLabel();
        cookieSwipeLabelMailAddress = new view.component.CookieSwipeLabel();
        cookieSwipeLabelPasswordAccountMail = new view.component.CookieSwipeLabel();
        cookieSwipeTextFieldNameAcountMail = new view.component.CookieSwipeTextField();
        cookieSwipeTextFieldMailAddress = new view.component.CookieSwipeTextField();
        cookieSwipePasswordFieldPasswordAccountMail = new view.component.CookieSwipePasswordField();
        cookieSwipeButtonValidate = new view.component.CookieSwipeButton();
        cookieSwipeButtonCancel = new view.component.CookieSwipeButton();
        jComboBoxColor = new JComboBox();
        cookieSwipeLabel1 = new view.component.CookieSwipeLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        cookieSwipeLabelNameAcountMail.setText("Nom de la boite courriel :");

        cookieSwipeLabelMailAddress.setText("Adresse courriel :");

        cookieSwipeLabelPasswordAccountMail.setText("Mot de passe :");

        cookieSwipeTextFieldNameAcountMail.setText("xyz");
        cookieSwipeTextFieldNameAcountMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cookieSwipeTextFieldNameAcountMailActionPerformed(evt);
            }
        });

        cookieSwipeTextFieldMailAddress.setText("xyz@mail.com");
        cookieSwipeTextFieldMailAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cookieSwipeTextFieldMailAddressActionPerformed(evt);
            }
        });

        cookieSwipeButtonValidate.setText("Valider");

        cookieSwipeButtonCancel.setText("Annuler");

        jComboBoxColor.setModel(new DefaultComboBoxModel(new String[] {"Aucune", "Bleu", "Rouge", "Vert", "Jaune" }));

        cookieSwipeLabel1.setText("Couleur :");

    }
    
    private void placeComponents(){
    	GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(180, 180, 180)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(cookieSwipeLabelNameAcountMail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(cookieSwipeLabelMailAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(cookieSwipeLabelPasswordAccountMail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(cookieSwipeLabel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(cookieSwipeTextFieldNameAcountMail, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cookieSwipeTextFieldMailAddress, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cookieSwipePasswordFieldPasswordAccountMail, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(jComboBoxColor, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(260, 260, 260)
                        .addComponent(cookieSwipeButtonValidate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(125, 125, 125)
                        .addComponent(cookieSwipeButtonCancel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(217, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(cookieSwipeTextFieldNameAcountMail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(cookieSwipeLabelNameAcountMail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(cookieSwipeTextFieldMailAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(cookieSwipeLabelMailAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(cookieSwipePasswordFieldPasswordAccountMail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(cookieSwipeLabelPasswordAccountMail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxColor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(cookieSwipeLabel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(cookieSwipeButtonValidate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(cookieSwipeButtonCancel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49))
        );
    }

    private void cookieSwipeTextFieldNameAcountMailActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void cookieSwipeTextFieldMailAddressActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    public static void main(String args[]) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AcountMailCSFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AcountMailCSFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AcountMailCSFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AcountMailCSFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AcountMailCSFrame().setVisible(true);
            }
        });
    }

    @Override
    public void refresh() {
        this.repaint();
        this.pack();
    }
}
