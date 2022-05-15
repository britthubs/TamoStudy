package State;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatisticsStrategy extends StateStrategy {

	//GridBagConstraints for the Tamo's image and background labels
	private GridBagConstraints gbc = new GridBagConstraints();
	
	/**
	 * Statistics Components
	 */
	private JPanel headerPanel;
	private JLabel headerLabel;
	
	//Main Panel - panel that will be in center
	private JPanel mainPanel;
	
	//Tamo Panel
	private JPanel tamoPanel;
	private JLabel tamoNameLabel;
	private JLabel imageLabel, backgroundImageLabel;
	
	//Stats Panel
	private JPanel statsPanel;
	private JLabel usernameLabel, joinDateLabel, 
		totalFocusHoursLabel, unlockedAchievementsLabel,
		tamoLevelLabel;
	
	private JLabel profileStarsImage;
	
	@Override
	public void setPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBackground(subColor);
	
		//Add Space
		this.add(createSpaceLabel(0));
		
		//Add Header
		headerLabel = new JLabel("Statistics");
			headerLabel.setFont(fontBoldReg);
			headerLabel.setForeground(textColor);
			headerLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		this.add(headerLabel);
		
		mainPanel = new JPanel();
			mainPanel.setLayout(new GridLayout(1,2));
		this.add(mainPanel, BorderLayout.CENTER);
		
		createTamoPanel();
		createStatsPanel();
	}
	
	public void createTamoPanel() {
		tamoPanel = new JPanel();
			tamoPanel.setLayout(new BoxLayout(tamoPanel, BoxLayout.Y_AXIS));
			tamoPanel.setBackground(subColor);
		
		//Name Component
		tamoNameLabel = new JLabel("Lisa");
			tamoNameLabel.setForeground(textColor);
			tamoNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
			tamoNameLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		tamoPanel.add(tamoNameLabel);
		
		//Tamo-Image Components
		imageLabel = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("TAMO_NORMAL_1.gif")));
		backgroundImageLabel = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("BG_1.png")));
		
		backgroundImageLabel.setLayout(new GridBagLayout());
		imageLabel.setSize(imageLabel.getPreferredSize());
		backgroundImageLabel.add(imageLabel, gbc);
		backgroundImageLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		tamoPanel.add(backgroundImageLabel); //Add to tamoPanel
		
		mainPanel.add(tamoPanel);
	}
	
	public void createStatsPanel() {
		statsPanel = new JPanel();
			statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
			statsPanel.setBackground(subColor);
		
		//Space Component
		statsPanel.add(createSpaceLabel(0));
		
		//Statistics Components
		usernameLabel = new JLabel("Username: Anthony");
		setUpLabelComponent(usernameLabel);
		statsPanel.add(usernameLabel);
		
		joinDateLabel = new JLabel("Join Date: 2022-05-07");
		setUpLabelComponent(joinDateLabel);
		statsPanel.add(joinDateLabel);
		
			//space
		statsPanel.add(createSpaceLabel(0));
		
		totalFocusHoursLabel = new JLabel("Total Focus Hours: 1727");
		setUpLabelComponent(totalFocusHoursLabel);
		statsPanel.add(totalFocusHoursLabel);
		
		unlockedAchievementsLabel = new JLabel("Achievements Unlocked: 12/20");
		setUpLabelComponent(unlockedAchievementsLabel);
		statsPanel.add(unlockedAchievementsLabel);
		
		tamoLevelLabel = new JLabel("Tamo Level: 24");
		setUpLabelComponent(tamoLevelLabel);
		statsPanel.add(tamoLevelLabel);
		
		//TODO
		//Make this into a for loop, say, for each star, lets
		//create a new jlabel with the star!
		statsPanel.add(createStarLabel());
		
		mainPanel.add(statsPanel);
	}

	@Override
	public void setActions() {
		// TODO Auto-generated method stub
		
	}
	
	public JLabel createStarLabel() {
		JLabel star = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("STAR.png")));
			star.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		return star;
	}
	
	public void setUpLabelComponent(JLabel label) {
		label.setForeground(textColor);
		label.setFont(fontPlainReg);
		label.setAlignmentX(JComponent.CENTER_ALIGNMENT);
	}

}
