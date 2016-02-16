package view;

import interfaces.IJFrame;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JScrollPane;
import view.component.CookieSwipeButtonAttach;

import view.component.CookieSwipeButtonSprite;
import view.component.CookieSwipeColor;
import view.component.CookieSwipeFrame;
import view.component.CookieSwipeLabel;
import view.component.CookieSwipePanel;
import view.component.CookieSwipeTextArea;
import view.component.CookieSwipeTextField;

public class ReadMailCSFrame extends CookieSwipeFrame implements IJFrame {

	private static final long serialVersionUID = 4622543138874352769L;

    private view.component.CookieSwipeButtonAttach[] cookieSwipeButtonAttach;
    
    private CookieSwipeButtonSprite cookieSwipeButtonReply;
    private CookieSwipeButtonSprite cookieSwipeButtonReplyToAll;
    private CookieSwipeButtonSprite cookieSwipeButtonForward;
    private CookieSwipeButtonSprite cookieSwipeButtonDeleteMail;
    private CookieSwipeButtonSprite cookieSwipeButtonToBlacklist;
    
    private CookieSwipePanel csPanel;

    private CookieSwipeLabel cookieSwipeLabelCc;
    private CookieSwipeLabel cookieSwipeLabelTo;
    private CookieSwipeLabel cookieSwipeLabelObject;
    private CookieSwipeLabel cookieSwipeLabelFrom;
    
    private CookieSwipeTextField cookieSwipeTextFieldObject;
    private CookieSwipeTextField cookieSwipeTextFieldTo;
    private CookieSwipeTextField cookieSwipeTextFieldToCc;
    private CookieSwipeTextField cookieSwipeTextFieldFrom;
   
    private JScrollPane jScrollPane1;
   
    private CookieSwipeTextArea jTextAreaMail;

    
    public ReadMailCSFrame() {
    	
    	initFrame();
    	
    }

    public void setCookieSwipeTextFieldObject(String cookieSwipeTextFieldObject) {
        this.cookieSwipeTextFieldObject.setText(cookieSwipeTextFieldObject);
    }

    public void setCookieSwipeTextFieldTo(String cookieSwipeTextFieldTo) {
        this.cookieSwipeTextFieldTo.setText(cookieSwipeTextFieldTo);
    }

    public void setCookieSwipeTextFieldToCc(String cookieSwipeTextFieldToCc) {
        this.cookieSwipeTextFieldToCc.setText(cookieSwipeTextFieldToCc);
    }

    public void setCookieSwipeTextFieldFrom(String cookieSwipeTextFieldFrom) {
        this.cookieSwipeTextFieldFrom.setText(cookieSwipeTextFieldFrom);
    }

    public void setCookieSwipeButtonAttach(List<CookieSwipeButtonAttach> buttons) {
        this.cookieSwipeButtonAttach = new CookieSwipeButtonAttach[buttons.size()];
        for(int i = 0; i < buttons.size(); i++)
            this.cookieSwipeButtonAttach[i] = buttons.get(i);
        csPanel.removeAll();
        for(int i = 0; i < buttons.size(); i++){
        	csPanel.add(buttons.get(i));
        }
    }

    public void setjTextAreaMail(String jTextAreaMail) {
        this.jTextAreaMail.setText(jTextAreaMail);
    }
    
    public void initFrame(){
    	
    	initComponents();
    	doWhenResized();
    	placeComponents();
    	
    	putComponents();
    	configFrame();
    	
    	refresh();
    	
    }
    
    private void putComponents(){
    	
    	hsJcomponent.put("cookieSwipeLabelCc", cookieSwipeLabelCc);
        hsJcomponent.put("cookieSwipeLabelTo", cookieSwipeLabelTo);
        hsJcomponent.put("cookieSwipeTextFieldTo", cookieSwipeTextFieldTo);
        hsJcomponent.put("cookieSwipeTextFieldToCc", cookieSwipeTextFieldToCc);
        hsJcomponent.put("cookieSwipeButtonReply", cookieSwipeButtonReply);
        hsJcomponent.put("cookieSwipeButtonReplyToAll", cookieSwipeButtonReplyToAll);
        hsJcomponent.put("cookieSwipeButtonForward", cookieSwipeButtonForward);
        hsJcomponent.put("cookieSwipeButtonDeleteMail", cookieSwipeButtonDeleteMail);
        hsJcomponent.put("cookieSwipeButtonToBlacklist", cookieSwipeButtonToBlacklist);
        hsJcomponent.put("cookieSwipeLabelObject", cookieSwipeLabelObject);
        
    }
    
    private void configFrame(){
    	
    	setTitle("Lecture de mail");
    	
    	setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        
        
//       setExtendedState(MAXIMIZED_BOTH);
        setSize(720, 480);
    	
    }
     
    public CookieSwipeTextArea getjTextAreaMail() {
        return jTextAreaMail;
    }
    
    public void initComponents() {

        cookieSwipeButtonReply = new CookieSwipeButtonSprite();
        cookieSwipeButtonReplyToAll = new CookieSwipeButtonSprite();
        cookieSwipeButtonForward = new CookieSwipeButtonSprite();
        cookieSwipeButtonDeleteMail = new CookieSwipeButtonSprite();
        cookieSwipeButtonToBlacklist = new CookieSwipeButtonSprite();
        
        cookieSwipeButtonReply.setText(CookieSwipeButtonSprite.REPLY);
        cookieSwipeButtonReplyToAll.setText(CookieSwipeButtonSprite.REPLY_ALL);
        cookieSwipeButtonForward.setText(CookieSwipeButtonSprite.FORWARD);
        cookieSwipeButtonDeleteMail.setText(CookieSwipeButtonSprite.DELETE);
        cookieSwipeButtonToBlacklist.setText(CookieSwipeButtonSprite.TO_BLACKLIST);
        
        csPanel = new view.component.CookieSwipePanel(CookieSwipeColor.BACKGROUND_FRAME);

        cookieSwipeTextFieldTo = new CookieSwipeTextField();
	cookieSwipeTextFieldTo.setEditable(false);
        cookieSwipeTextFieldToCc = new CookieSwipeTextField();
	cookieSwipeTextFieldToCc.setEditable(false);
        cookieSwipeTextFieldObject = new CookieSwipeTextField();
	cookieSwipeTextFieldObject.setEditable(false);
        cookieSwipeTextFieldFrom = new CookieSwipeTextField();
	cookieSwipeTextFieldFrom.setEditable(false);
        
        cookieSwipeLabelFrom = new CookieSwipeLabel();
        cookieSwipeLabelTo = new CookieSwipeLabel();
        cookieSwipeLabelCc = new CookieSwipeLabel();
        cookieSwipeLabelObject = new CookieSwipeLabel();
        
        jScrollPane1 = new JScrollPane();
        jTextAreaMail = new CookieSwipeTextArea();
	jTextAreaMail.setEditable(false);
        
        cookieSwipeLabelTo.setText("À :");
        cookieSwipeLabelCc.setText("Cc :");
        cookieSwipeLabelObject.setText("Objet :");
        cookieSwipeLabelFrom.setText("De :");

        //jTextAreaMail.setColumns(20);
        //jTextAreaMail.setRows(5);
        jScrollPane1.setViewportView(jTextAreaMail);

    }
    
    
    public void placeComponents(){
    	
    	GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cookieSwipeLabelCc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cookieSwipeLabelTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cookieSwipeLabelObject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cookieSwipeLabelFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        		)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 2000, 2000)
                        .addGap(60, 60, 60)
                    )
                    .addGroup(layout.createSequentialGroup()
						.addComponent(cookieSwipeButtonReply, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(cookieSwipeButtonReplyToAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(cookieSwipeButtonForward, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(cookieSwipeButtonDeleteMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(cookieSwipeButtonToBlacklist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					)
                    .addGroup(layout.createSequentialGroup()
                		.addComponent(cookieSwipeTextFieldTo, javax.swing.GroupLayout.DEFAULT_SIZE, 2000, 2000)
                        .addGap(60, 60, 60)
                    )
                    .addGroup(layout.createSequentialGroup()
                		.addComponent(cookieSwipeTextFieldToCc, javax.swing.GroupLayout.DEFAULT_SIZE, 2000, 2000)
                        .addGap(60, 60, 60)
                    )
                    .addGroup(layout.createSequentialGroup()
                		.addComponent(cookieSwipeTextFieldObject, javax.swing.GroupLayout.DEFAULT_SIZE, 2000, 2000)
                        .addGap(60, 60, 60)
                    )
                    .addGroup(layout.createSequentialGroup()
                		.addComponent(cookieSwipeTextFieldFrom, javax.swing.GroupLayout.DEFAULT_SIZE, 2000, 2000)
                        .addGap(60, 60, 60)
                    )
                    .addGroup(layout.createSequentialGroup()
                		.addComponent(csPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 2000, 2000)
                        .addGap(60, 60, 60)
                    )
        		)
                .addContainerGap(103, Short.MAX_VALUE)
    		)
        );
        
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addGroup(layout.createSequentialGroup()
                		.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
    						.addComponent(cookieSwipeButtonReply, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
    						.addComponent(cookieSwipeButtonReplyToAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
    						.addComponent(cookieSwipeButtonForward, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
    						.addComponent(cookieSwipeButtonDeleteMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
    						.addComponent(cookieSwipeButtonToBlacklist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
    					)
    					.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cookieSwipeLabelFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cookieSwipeTextFieldFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        )
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cookieSwipeLabelTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cookieSwipeTextFieldTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        )
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cookieSwipeTextFieldToCc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cookieSwipeLabelCc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        )
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cookieSwipeLabelObject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cookieSwipeTextFieldObject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        )
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    		.addComponent(csPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                        )
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 2000, 2000)
                        .addGap(15, 15, 15)
                    )
                )
                .addContainerGap(29, Short.MAX_VALUE)
            )
        );
        
    }
    
    public void doWhenResized(){
    	
    	addComponentListener(new ComponentListener() {
		    public void componentResized(ComponentEvent e) {
		    	
		    	cookieSwipeTextFieldToCc.setPreferredSize(new Dimension(getWidth() - 150, 35));
		    	cookieSwipeTextFieldToCc.setMinimumSize(new Dimension(getWidth() - 150, 35));
		    	cookieSwipeTextFieldToCc.setMaximumSize(new Dimension(getWidth() - 150, 35));
		    	cookieSwipeTextFieldToCc.setMinimumSize(new Dimension(0, 0));
		    	
		    	cookieSwipeTextFieldObject.setPreferredSize(new Dimension(getWidth() - 150, 35));
		    	cookieSwipeTextFieldObject.setMinimumSize(new Dimension(getWidth() - 150, 35));
		    	cookieSwipeTextFieldObject.setMaximumSize(new Dimension(getWidth() - 150, 35));
		    	cookieSwipeTextFieldObject.setMinimumSize(new Dimension(0, 0));
		    	
		    	cookieSwipeTextFieldTo.setPreferredSize(new Dimension(getWidth() - 150, 35));
		    	cookieSwipeTextFieldTo.setMinimumSize(new Dimension(getWidth() - 150, 35));
		    	cookieSwipeTextFieldTo.setMaximumSize(new Dimension(getWidth() - 150, 35));
		    	cookieSwipeTextFieldTo.setMinimumSize(new Dimension(0, 0));
		    	
		    	cookieSwipeTextFieldFrom.setPreferredSize(new Dimension(getWidth() - 150, 35));
		    	cookieSwipeTextFieldFrom.setMinimumSize(new Dimension(getWidth() - 150, 35));
		    	cookieSwipeTextFieldFrom.setMaximumSize(new Dimension(getWidth() - 150, 35));
		    	cookieSwipeTextFieldFrom.setMinimumSize(new Dimension(0, 0));
				
		    	jScrollPane1.setPreferredSize(new Dimension(getWidth() - 150, getHeight() - 200));
				jScrollPane1.setMinimumSize(new Dimension(getWidth() - 150, getHeight() - 200));
				jScrollPane1.setMaximumSize(new Dimension(getWidth() - 150, getHeight() - 200));
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
    	    	new ReadMailCSFrame().setVisible(true);
    	    }
    	});
    	
    }

    @Override
    public void refresh() {
    	
    	validate();
        repaint();
        revalidate();
        
    }
}
