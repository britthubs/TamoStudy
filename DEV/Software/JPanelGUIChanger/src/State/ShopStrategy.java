package State;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import resources.TextBubbleBorder;

/**
 * @author Anthony Narlock
 * ShopStrategy
 * 
 * This is a concrete StateStrategy which is a JPanel
 * that will display the user interface for Shop
 */

public class ShopStrategy extends StateStrategy {
	
	/**
	 * Shop Panel
	 * 
	 * Left Panel is Food Panel
	 * Center Panel is Main Panel
	 * Right Panel is Background Panel
	 */
	
	//Separators
	
	//foodPanel
	private JPanel foodPanel;
	private JLabel foodShopLabel;
	private JPanel[] foodItems;
	
	//mainPanel
	private JPanel mainPanel, tokenPanel;
	private JLabel shopImageLabel, tokenImageLabel;
	private JLabel tokenDisplayLabel;
	
	private JPanel messagePanel;
	private JLabel kathImage;
	private JLabel messageText;
	
	//bgPanel
	private JPanel bgPanel;
	private JLabel bgShopLabel;
	private JPanel[] bgItems;

	@Override
	public void setPanel() {
		this.setLayout(new GridLayout(1,3,20,20));
		this.setBackground(subColor);
		createFoodPanel();
		createMainPanel();
		createBgPanel();
	}
	
	//Creates the food panel
	public void createFoodPanel() {
		foodPanel = new JPanel();
			foodPanel.setBackground(subColor);
			foodPanel.setLayout(new BoxLayout(foodPanel, BoxLayout.Y_AXIS));
		
		foodShopLabel = new JLabel("Food Shop");
			foodShopLabel.setForeground(textColor);
			foodShopLabel.setFont(fontBoldReg);
			foodShopLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		
		foodPanel.add(foodShopLabel);
		
		//For each food item, we will add them
		
		//TODO
		//Make it so depending on the month, there will be different
		//backgrounds in which the player can choose to have....
		
		foodItems = new JPanel[3];
		foodItems[0] = createFoodItemPanel(1, "FOOD_1.png", 100);
		foodItems[1] = createFoodItemPanel(3, "FOOD_3.png", 200);
		foodItems[2] = createFoodItemPanel(10, "FOOD_10.png", 800);
		
		foodPanel.add(createSpaceLabel());
		for(int i = 0; i < foodItems.length; i++) {
			foodPanel.add(foodItems[i]);
			foodPanel.add(createSpaceLabel());
		}
		
		this.add(foodPanel);
	}
	
	//Creates the main panel
	public void createMainPanel() {
		mainPanel = new JPanel();
			mainPanel.setBackground(subColor);
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		shopImageLabel = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("KATH_SHOP.png")));
			shopImageLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		mainPanel.add(shopImageLabel);
		
		messagePanel = new JPanel();
			messagePanel.setBackground(subColor);
			messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
			messagePanel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		kathImage = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("KATH_MSG.png")));
			kathImage.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		messageText = new JLabel("<html>Hello!<br>Welcome to the Shop!</html>");
			messageText.setOpaque(true);
			messageText.setBackground(Color.WHITE);
			messageText.setForeground(Color.BLACK);
			messageText.setBorder(new TextBubbleBorder(Color.BLACK, 2, 6, 10, false));
			messageText.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		messagePanel.add(messageText);
		messagePanel.add(kathImage);
		mainPanel.add(messagePanel);
		
		mainPanel.add(createSpaceLabel());
		
		tokenPanel = new JPanel();
			tokenPanel.setBackground(subColor);
		tokenImageLabel = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("TAMO_TOKEN.png")));
		tokenDisplayLabel = new JLabel("1727");
			tokenDisplayLabel.setForeground(Color.WHITE);
			tokenDisplayLabel.setFont(fontBoldReg);
		tokenPanel.add(tokenImageLabel);
		tokenPanel.add(tokenDisplayLabel);
		mainPanel.add(tokenPanel);
		
		
		this.add(mainPanel);
	}
	
	//Creates the background panel
	public void createBgPanel() {
		bgPanel = new JPanel();
			bgPanel.setBackground(subColor);
			bgPanel.setLayout(new BoxLayout(bgPanel, BoxLayout.Y_AXIS));
		bgShopLabel = new JLabel("Backgrounds");
			bgShopLabel.setForeground(Color.WHITE);
			bgShopLabel.setFont(fontBoldReg);
			bgShopLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		bgPanel.add(bgShopLabel);
		
		//For each food item, we will add them
		bgItems = new JPanel[4];
		bgItems[0] = createItemPanel(2, "SHOP_BG_2.png", 1000);
		bgItems[1] = createItemPanel(3, "SHOP_BG_3.png", 1000);
		bgItems[2] = createItemPanel(4, "SHOP_BG_4.png", 1000);
		
		bgPanel.add(createSpaceLabel());
		for(int i = 0; i < foodItems.length; i++) {
			bgPanel.add(bgItems[i]);
			bgPanel.add(createSpaceLabel());
		}
		
		this.add(bgPanel);
	}

	@Override
	public void setActions() {
		// TODO Auto-generated method stub
		
	}
	
	public JPanel createItemPanel(int indicator, String imageUrl, int price) {
		JPanel itemPanel = new JPanel();
		itemPanel.setLayout(new GridLayout(1,2));
		itemPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		itemPanel.setBackground(layerColor);
		
		JLabel imageLabel = new JLabel(new ImageIcon(getClass().getClassLoader().getResource(imageUrl)));
		itemPanel.add(imageLabel);
		JPanel infoPanel = new JPanel();
			infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
			infoPanel.setBackground(layerColor);
		JPanel tokenPanel = new JPanel();
			tokenPanel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
			tokenPanel.setBackground(layerColor);
		JLabel tokenImage = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("TAMO_TOKEN.png")));
		JLabel priceLabel = new JLabel("" + price);
			priceLabel.setForeground(textColor);
		tokenPanel.add(tokenImage);
		tokenPanel.add(priceLabel);
		infoPanel.add(tokenPanel);
		JButton purchaseButton = new JButton("Purchase");
			purchaseButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		infoPanel.add(purchaseButton);
		itemPanel.add(infoPanel);
		
		return itemPanel;
	}
	
	public JPanel createFoodItemPanel(int hunger, String imageUrl, int price) {
		JPanel foodPanel = new JPanel();
			foodPanel.setLayout(new GridLayout(1,2));
			foodPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
			foodPanel.setBackground(layerColor);
			
		JLabel foodImageLabel = new JLabel(new ImageIcon(getClass().getClassLoader().getResource(imageUrl)));
		foodPanel.add(foodImageLabel);
		JPanel infoPanel = new JPanel();
			infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
			infoPanel.setBackground(layerColor);
		JPanel tokenPanel = new JPanel();
			tokenPanel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
			tokenPanel.setBackground(layerColor);
		JLabel tokenImage = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("TAMO_TOKEN.png")));
		JLabel foodPriceLabel = new JLabel("" + price + " | " + hunger);
			foodPriceLabel.setForeground(textColor);
		tokenPanel.add(tokenImage);
		tokenPanel.add(foodPriceLabel);
		infoPanel.add(tokenPanel);
		JButton foodPurchaseButton = new JButton("Purchase");
			foodPurchaseButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		infoPanel.add(foodPurchaseButton);
		foodPanel.add(infoPanel);
		
		return foodPanel;
	}
	
	public JLabel createSpaceLabel() {
		//Space Component
		JLabel transparentComponent = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("TRANSPARENT.png")));
		transparentComponent.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		
		return transparentComponent;
	}

}
