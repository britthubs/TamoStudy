package panels;

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MessagePanel extends JPanel {
	
	//How to use:
	//new MessagePanel(JFrame, title, message, indicator);
	
	public MessagePanel(JComponent parent, String title, String message, int indicator) {
		
		if(indicator == 0) {
			//Display Info
			JOptionPane.showMessageDialog(parent, 
					title, 
					message, 
					JOptionPane.INFORMATION_MESSAGE, 
					new ImageIcon(getClass().getClassLoader().getResource("INFO.png")));
		} else if(indicator == 1) { 
			//Display Error
			JOptionPane.showMessageDialog(parent, 
					title, 
					message, 
					JOptionPane.INFORMATION_MESSAGE, 
					new ImageIcon(getClass().getClassLoader().getResource("INFO_ERROR.png")));
		}
	}
	
}