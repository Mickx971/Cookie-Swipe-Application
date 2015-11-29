package view;

import interfaces.IJFrame;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.mail.Message;
import javax.swing.GroupLayout;
import javax.swing.JScrollPane;

import model.ListMailAccountListener;
import model.MailAccount;
import module.ihm.MainFrameInitializer;
import view.component.CookieSwipeButton;
import view.component.CookieSwipeButtonSprite;
import view.component.CookieSwipeFrame;
import view.component.CookieSwipeList;
import view.component.CookieSwipePanel;
import view.component.CookieSwipeTree;
import cookie.swipe.application.CookieSwipeApplication;
import java.awt.Toolkit;
import java.io.File;
import model.CustomMessage;
import view.component.CookieSwipeColor;

public class MainCSFrame extends CookieSwipeFrame implements IJFrame, ListMailAccountListener {

	private static final long serialVersionUID = 8736726751871593606L;
	
	private CookieSwipeTree cookieSwipeTreeAccountMail;
    private CookieSwipeList<CustomMessage> jListMail;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPaneMailList;
    private CookieSwipePanel spritePanel;
    
    //private boolean isMenuActivated = true;

    private CookieSwipeButton cookieSwipeButtonAddMailAccount;
    private CookieSwipeButton cookieSwipeButtonDeleteMailAccount;
    private CookieSwipeButton cookieSwipeButtonLogout;
    private CookieSwipeButton cookieSwipeButtonUpdateCSAccount;
    private CookieSwipeButton cookieSwipeButtonUpdateMailAccount;
    private CookieSwipeButton cookieSwipeButtonBlacklist;

    private CookieSwipeButtonSprite cookieSwipeButtonDeleteMail;
    private CookieSwipeButtonSprite cookieSwipeButtonForward;
    private CookieSwipeButtonSprite cookieSwipeButtonNewMail;
    private CookieSwipeButtonSprite cookieSwipeButtonReply;
    private CookieSwipeButtonSprite cookieSwipeButtonReplyToAll;
    private CookieSwipeButtonSprite cookieSwipeButtonRefresh;
    private CookieSwipeButtonSprite cookieSwipeButtonToBlacklist;

    
    public MainCSFrame() {

        initFrame();
        
    }
    
    private void initFrame(){
    	
    	initComponents();
    	doWhenResized();
    	createMenu();
    	placeComponents();    	
    	putComponents();
    	configFrame();
        
    	refresh();
    	
    }
    
    private void putComponents(){
    	
    	hsJcomponent.put("cookieSwipeButtonReply", cookieSwipeButtonReply);
        hsJcomponent.put("cookieSwipeButtonReplyToAll", cookieSwipeButtonReplyToAll);
        hsJcomponent.put("cookieSwipeButtonDeleteMail", cookieSwipeButtonDeleteMail);
        hsJcomponent.put("cookieSwipeButtonForward", cookieSwipeButtonForward);
        hsJcomponent.put("cookieSwipeButtonNewMail", cookieSwipeButtonNewMail);
        hsJcomponent.put("cookieSwipeTreeAccountMail", cookieSwipeTreeAccountMail);
        hsJcomponent.put("cookieSwipeButtonAddMailAccount",cookieSwipeButtonAddMailAccount);
        hsJcomponent.put("cookieSwipeButtonDeleteMailAccount", cookieSwipeButtonDeleteMailAccount);
        hsJcomponent.put("cookieSwipeButtonLogout", cookieSwipeButtonLogout);
        hsJcomponent.put("cookieSwipeButtonUpdateCSAccount", cookieSwipeButtonUpdateCSAccount);
        hsJcomponent.put("cookieSwipeButtonUpdateMailAccount", cookieSwipeButtonUpdateMailAccount);
        hsJcomponent.put("cookieSwipeButtonRefresh", cookieSwipeButtonRefresh);
        hsJcomponent.put("cookieSwipeButtonToBlacklist", cookieSwipeButtonToBlacklist);
        hsJcomponent.put("cookieSwipeButtonBlacklist", cookieSwipeButtonBlacklist);
        hsJcomponent.put("jScrollPaneMailList",jScrollPaneMailList);
        hsJcomponent.put("jListMail", jListMail);
        
    }
    
    private void configFrame(){
    	
    	setTitle("Cookie Swipe");
    	
    	setLocationRelativeTo(null);
        WindowListener exitListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                CookieSwipeApplication.getApplication().stop();
            }
        };
        addWindowListener(exitListener);
        setVisible(true);
        
//        setExtendedState(MAXIMIZED_BOTH); //fullscreen
        setSize(720, 480);
	
	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	setLocation((dim.width / 2) - (getSize().width/2), (dim.height / 2) - (getSize().height / 2));
        
    }

    private void initComponents() {
	
		cookieSwipeButtonUpdateMailAccount = new CookieSwipeButton();
		cookieSwipeButtonDeleteMailAccount = new CookieSwipeButton();
		cookieSwipeButtonUpdateCSAccount = new CookieSwipeButton();
		cookieSwipeButtonAddMailAccount = new CookieSwipeButton();
		cookieSwipeButtonLogout = new CookieSwipeButton();
		cookieSwipeButtonBlacklist = new CookieSwipeButton();
	
		cookieSwipeButtonLogout.setText("Deconnexion");
		cookieSwipeButtonUpdateCSAccount.setText("Modifer profil");
		cookieSwipeButtonAddMailAccount.setText("Nouvelle boite");
		cookieSwipeButtonUpdateMailAccount.setText("Modifier boite");
		cookieSwipeButtonDeleteMailAccount.setText("Supprimer boite");
		cookieSwipeButtonBlacklist.setText("Voir la blacklist");
	
		cookieSwipeButtonForward = new CookieSwipeButtonSprite();
		cookieSwipeButtonDeleteMail = new CookieSwipeButtonSprite();
		cookieSwipeButtonNewMail = new CookieSwipeButtonSprite();
		cookieSwipeButtonReply = new CookieSwipeButtonSprite();
		cookieSwipeButtonReplyToAll = new CookieSwipeButtonSprite();
		cookieSwipeButtonRefresh = new CookieSwipeButtonSprite();
		cookieSwipeButtonToBlacklist = new CookieSwipeButtonSprite();
	
		cookieSwipeButtonForward.setText(CookieSwipeButtonSprite.FORWARD);
		cookieSwipeButtonDeleteMail.setText(CookieSwipeButtonSprite.DELETE);
		cookieSwipeButtonNewMail.setText(CookieSwipeButtonSprite.NEW);
		cookieSwipeButtonReply.setText(CookieSwipeButtonSprite.REPLY);
		cookieSwipeButtonReplyToAll.setText(CookieSwipeButtonSprite.REPLY_ALL);
		cookieSwipeButtonRefresh.setText(CookieSwipeButtonSprite.REFRESH);
		cookieSwipeButtonToBlacklist.setText(CookieSwipeButtonSprite.TO_BLACKLIST);
	
		jListMail = new view.component.CookieSwipeList<CustomMessage>();
	    
		cookieSwipeTreeAccountMail = new CookieSwipeTree();
	
		jScrollPane1 = new JScrollPane();
		jScrollPaneMailList = new JScrollPane();
	
		jScrollPane1.setViewportView(cookieSwipeTreeAccountMail);
		jScrollPaneMailList.setViewportView(jListMail);

    }
    
    private void doWhenResized(){
    	
		addComponentListener(new ComponentListener() {
		    public void componentResized(ComponentEvent e) {
		    	
				jListMail.setFixedCellWidth(getWidth() - 45);
				jListMail.setFixedCellHeight(43);
				
				jScrollPaneMailList.setPreferredSize(new Dimension(getWidth() - 258, getHeight() - 140));
				jScrollPaneMailList.setMinimumSize(new Dimension(getWidth() - 258, getHeight() - 140));
				jScrollPaneMailList.setMaximumSize(new Dimension(getWidth() - 258, getHeight() - 140));
				jScrollPaneMailList.setMinimumSize(new Dimension(0, 0));
				
				jScrollPane1.setPreferredSize(new Dimension(200, getHeight() - 140));
				jScrollPane1.setPreferredSize(new Dimension(200, getHeight() - 140));
				jScrollPane1.setMaximumSize(new Dimension(getWidth() - 200, getHeight() - 140));
				jScrollPane1.setMinimumSize(new Dimension(0, 0));
			
				refresh();
				
		    }
	
		    @Override
		    public void componentMoved(ComponentEvent e) {
			// TODO Auto-generated method stub
	
		    }
	
		    @Override
		    public void componentShown(ComponentEvent e) {
			// TODO Auto-generated method stub
	
		    }
	
		    @Override
		    public void componentHidden(ComponentEvent e) {
			// TODO Auto-generated method stub
	
		    }
		});
		
	}
    
    private void createMenu(){
    	CookieSwipePanel logoPanel = new CookieSwipePanel(new File("cookieSwipe_menu.png"), CookieSwipeColor.BUTTON);
        
    	spritePanel = new CookieSwipePanel();
		spritePanel.setPreferredSize(new Dimension((int) spritePanel.getPreferredSize().getWidth() + 2500, 43)); //Lorsque la résolution est petite: 40 grande: 43
		GroupLayout spriteLayout = new GroupLayout(spritePanel);
		spritePanel.setLayout(spriteLayout);
		spriteLayout.setHorizontalGroup(
			spriteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(spriteLayout.createSequentialGroup()
                                .addGap(50, 50, 50)
				.addComponent(logoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50)
				.addComponent(cookieSwipeButtonLogout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addComponent(cookieSwipeButtonUpdateCSAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addComponent(cookieSwipeButtonAddMailAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addComponent(cookieSwipeButtonUpdateMailAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addComponent(cookieSwipeButtonDeleteMailAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addComponent(cookieSwipeButtonBlacklist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
			)
		);
	
		spriteLayout.setVerticalGroup(
			spriteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
			.addGroup(spriteLayout.createSequentialGroup()
				.addGap(2, 2, 2)
				.addGroup(spriteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addGroup(spriteLayout.createSequentialGroup()
                                            .addGap(5, 5, 5)
                                            .addComponent(logoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        )
					.addComponent(cookieSwipeButtonLogout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(cookieSwipeButtonAddMailAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(cookieSwipeButtonUpdateCSAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(cookieSwipeButtonUpdateMailAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(cookieSwipeButtonDeleteMailAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(cookieSwipeButtonBlacklist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				)
			)
		);
		
    }
    
    private void placeComponents(){
    	
    	GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(layout.createSequentialGroup()
						.addGap(15, 15, 15)
						.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					)
					.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
							.addGap(230, 230, 230)
							.addComponent(jScrollPaneMailList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						)
					)
					.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
							.addComponent(spritePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						)
						.addGroup(layout.createSequentialGroup()
							.addGap(230, 230, 230)
							.addComponent(cookieSwipeButtonRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addComponent(cookieSwipeButtonNewMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addComponent(cookieSwipeButtonReply, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addComponent(cookieSwipeButtonReplyToAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addComponent(cookieSwipeButtonForward, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addComponent(cookieSwipeButtonDeleteMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addComponent(cookieSwipeButtonToBlacklist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						)
					)
				)
			)
		);
		
		layout.setVerticalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
			.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
					.addComponent(spritePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
					.addComponent(cookieSwipeButtonRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(cookieSwipeButtonNewMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(cookieSwipeButtonReply, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(cookieSwipeButtonReplyToAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(cookieSwipeButtonForward, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(cookieSwipeButtonDeleteMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(cookieSwipeButtonToBlacklist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
					.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
						.addComponent(jScrollPaneMailList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					)
				)
			)
		);
		
    }

    public static void main(String args[]) {
    	
		try {
		    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
			if ("Nimbus".equals(info.getName())) {
			    javax.swing.UIManager.setLookAndFeel(info.getClassName());
			    break;
			}
		    }
		} catch (ClassNotFoundException ex) {
		    java.util.logging.Logger.getLogger(MainCSFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
		    java.util.logging.Logger.getLogger(MainCSFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
		    java.util.logging.Logger.getLogger(MainCSFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
		    java.util.logging.Logger.getLogger(MainCSFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
		    public void run() {
			new MainCSFrame().setVisible(true);
		    }
		});
		
    }

    @Override
    public void notifyMailAccountDeleted(MailAccount mc) {
        new MainFrameInitializer(this).deleteMailAccountInTree(mc);
    }

    @Override
    public void notifyMailAccountAdded(MailAccount mc) {
        new MainFrameInitializer(this).addMailAccountInTree(mc);
    }
    
}
