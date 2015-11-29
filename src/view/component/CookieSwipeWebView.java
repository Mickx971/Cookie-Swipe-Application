package view.component;

import java.awt.Color;
import javafx.embed.swing.JFXPanel;

import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class CookieSwipeWebView extends JFXPanel {
    
    {
        setOpaque(false);
    }

	public CookieSwipeWebView() {
		super();
		initComponent();
	    }

	    public CookieSwipeWebView(String arg0) {
			super();
			initComponent();
	    }

	    public void initComponent() {
	    	//setSize(110, 20);
			Border thickBorder = new LineBorder(Color.white, 5);
			setBorder(thickBorder);
			setBackground(new Color(255, 255, 255));

	    }
}
