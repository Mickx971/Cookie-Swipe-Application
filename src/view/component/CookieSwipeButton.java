package view.component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class CookieSwipeButton extends JButton{

	private static final long serialVersionUID = 1671314658637614873L;
	private int inset = 5;
//	private Color buttonColor = new Color(12, 125, 175);
	private Color buttonColor = CookieSwipeColor.BUTTON;
	private int cell = 0;
	private int height, width;
	private boolean sizeUpdated = false;
 
	public CookieSwipeButton(String text){
		super(text);
		initComponent();
	}
 
	public CookieSwipeButton(){
	    super();
	    initComponent();
	}
 
	protected void initComponent(){	
		setForeground(Color.WHITE);
		
		setFocusPainted(false);
		
		Border thickBorder = new LineBorder(buttonColor, 2);
		setBorder(thickBorder);
	
		setContentAreaFilled(false);
		
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		setLayout(null);
		
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed()) {
            g.setColor(buttonColor.darker().darker());
        	
        } else if (getModel().isRollover()) {
            g.setColor(buttonColor.darker());
        } else {
            g.setColor(buttonColor);
        }
        g.fillRect(0, 0, getWidth(), getHeight());
        
        if(!sizeUpdated){
        	height = (int) getSize().getHeight() + 20;
        	width = (int) getSize().getWidth() + 20;
        	sizeUpdated = true;
        }
        setSize(width, height);
        
        super.paintComponent(g);
    }

}
