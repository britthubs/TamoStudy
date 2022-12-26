import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Stack;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;

import profile.Profile;
import profile.ProfileReaderWriter;
import resources.CommunicateThemeAction;
import resources.DiscordRP;
import resources.Theme;
import state.AboutStrategy;
import state.AchievementsStrategy;
import state.InventoryStrategy;
import state.SettingsStrategy;
import state.ShopStrategy;
import state.StateStrategy;
import state.StatisticsStrategy;
import state.StudyFocusStrategy;
import state.ThemeStrategy;
import state.TitleStrategy;

/**
 * MainGUI
 * 
 * @author Anthony Narlock (narlock)
 * 
 * @brief The main graphical user interface component
 * of TamoStudy. This user interface has a top bar which
 * contains a menu button to open the menu, along with
 * a side bar, which acts as the menu. Upon selecting an
 * option from the menu, the StateStrategy will change
 * depending on the selected menu option.
 */

public class MainGUI extends JFrame {

	/**
	 * openedSideBar
	 * @brief true if the side bar is open
	 * false if the side bar is not opened
	 */
	private boolean openedSideBar;
	
	/**
	 * sidePanel
	 * @brief The side panel JPanel
	 */
	private JPanel sidePanel;
	
	/**
	 * openSidePanel
	 * @brief The top panel that contains
	 * the menu button that opens side panel
	 */
	private JPanel openSidePanel;
	
	/**
	 * strategy
	 * @brief The state strategy: main portion of
	 * the graphical user interface
	 */
	private StateStrategy strategy;
	
	/**
	 * DIVIDER_STRING
	 * @brief The divider line between state options
	 * on the menu bar. Operating system dependent
	 */
	private String DIVIDER_STRING;

	/**
	 * profile
	 * @brief The user profile that is loaded
	 * into the application
	 */
	private Profile profile;
	
	/**
	 * theme
	 * @brief The user's theme choice
	 */
	public Theme theme;

	/**
	 * discordRP
	 * @brief Discord Rich Presence object
	 * Only for Windows devices
	 */
	private DiscordRP discordRP;
	
	/**
	 * The following are Stacks of the components
	 * utilized in the MainGUI. The panels contain
	 * panels to organize the components. The buttons
	 * represent the buttons to select strategies.
	 */
	private Stack<JPanel> panels;
	private Stack<JButton> buttons;
	private Stack<JLabel> labels;
	private JButton openMenuButton;
	private Stack<JLabel> breaks;
	
	/**
	 * Specific Function Components
	 * 
	 * These are member variables that correspond to
	 * the objects that are in the specific strategies
	 * in which we need to interact with the MainGUI.
	 */
	
	/**
	 * StudyFocusStrategy Components
	 */
	private Timer timer;
	private int min, sec, studyMin, studySec, tempSec, tempMin;
	private JLabel minuteTime, secondTime;
	private JComboBox<String> minuteBox, secondBox, pomoSessionBox, pomoBreakBox, pomoNumberSessionBox;
	
	private JLabel currentSessionLabel;
	private int currentPomodoroSession, totalPomodoroSessions;
	private boolean breakCondition;
	
	private JLabel  tamoImageLabel;
	private JButton startFocusButton;
	private JButton breakFocusButton;
	
	/**
	 * ThemeStrategy Components
	 */
	private Stack<JButton> selectButtons;
	private CommunicateThemeAction themeAction;
	
	/**
	 * SettingsStrategy Components
	 */
	private JButton saveChangesButton;	
	
	public MainGUI(Profile profile) {
		//Initialize Discord Rich Presence
		discordRP = new DiscordRP();
		discordRP.start();
		discordRP.update("Idle", "Title Card");
		
		//Initialize GUI Attributes
		DIVIDER_STRING = (System.getProperty("os.name").startsWith("Linux") || System.getProperty("os.name").startsWith("Windows"))
				? "     ━━━━━━━━━━━━━━━━━━━━━     " : "     ━━━━━━━━━━     ";
		openedSideBar = true;
		this.profile = profile;
		theme = this.profile.getThemeIndicator(); //For colors
		strategy = new TitleStrategy(this.profile);
		panels = new Stack<JPanel>();
		buttons = new Stack<JButton>();
		labels = new Stack<JLabel>();
		breaks = new Stack<JLabel>();
		
		//UI Manager to change option pane colors
		UIManager.put("OptionPane.background", theme.layerColor);
		UIManager.put("OptionPane.messageForeground", theme.textColor);
		UIManager.put("Panel.background", theme.layerColor);
		
		//Initializes the GUI components
		initFrame();
		initSidePanels();
		initComponentsToFrame();
		
		//Hopefully fixes Swing issues on painting
		this.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("ICON.png")).getImage());
		this.setSize(800,600);	//Resize properly so display is correct
		this.repaint();
		
		//Check for new Achievements
		checkForNewAchievements();
	}
	
	/**
	 * recall
	 * @param newStrategy
	 * @brief Changes the StateStrategy
	 * Will 'repaint' the main panel.
	 */
	public void recall(StateStrategy newStrategy) {
		this.remove(strategy);
		strategy = newStrategy;
		this.add(strategy, BorderLayout.CENTER);
		this.repaint();
	}
	
	/**
	 * initFrame
	 * @brief Initializes the main JFrame
	 */
	public void initFrame() {
		this.setTitle("TamoStudy Beta v4.2");
		this.setVisible(true);
		this.setSize(800,599);
		this.setLocationRelativeTo(null);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				System.out.println("TamoStudy Closing...");
			    discordRP.shutdown();
			    System.exit(0);
			}
		});
		this.setResizable(false);
		this.setBackground(Color.DARK_GRAY);
	}
	
	/**
	 * initSidePanels
	 * @brief Initializes the side panel
	 */
	public void initSidePanels() {
		//Initializes the thematic breaks
		JLabel thematicBreak = new JLabel(DIVIDER_STRING);
		JLabel thematicBreak2 = new JLabel(DIVIDER_STRING);
		setUpLabelComponent(thematicBreak, 1);
		setUpLabelComponent(thematicBreak2, 1);
		breaks.push(thematicBreak);
		breaks.push(thematicBreak2);
		
		//Initializes Side Panel
		sidePanel = new JPanel();
		sidePanel.setBackground(theme.mainColor);
		sidePanel.setLayout(new GridLayout(15,1));
		panels.push(sidePanel);
		
		//Initializes each of the side buttons
		//Includes dynamic functionality as needed to communicate
		//with its respective strategy
		JButton titleCardButton = new JButton(profile.getLanguage().text[2]);
		setUpButtonComponent(titleCardButton);
		titleCardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("[TAMOSTUDY] Changing Strategy to Title Card");
				discordRP.update("Idle", "Title Card");
				
				updateSideBar();
				StateStrategy newStrategy = new TitleStrategy(profile);
				recall(newStrategy);
			}
		});
		buttons.push(titleCardButton);
		
		JButton focusButton = new JButton(profile.getLanguage().text[3]);
		setUpButtonComponent(focusButton);
		focusButton.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("[TAMOSTUDY] Changing Strategy to StudyFocus");
				discordRP.update("Idle", "Focus");
				
				updateSideBar();
				StateStrategy newStrategy = new StudyFocusStrategy(profile);
				
				//Components to communicate with this GUI
				startFocusButton = ((StudyFocusStrategy) newStrategy).getStartButton();
				breakFocusButton = ((StudyFocusStrategy) newStrategy).getBreakButton();
				minuteTime = ((StudyFocusStrategy) newStrategy).getMinuteTimeLabel();
				secondTime = ((StudyFocusStrategy) newStrategy).getSecondTimeLabel();
				tamoImageLabel = ((StudyFocusStrategy) newStrategy).getTamoImageLabel();
				
				//Interval Count down
				if(profile.getSettings().getFocusMode() == 0 || profile.getSettings().getFocusMode() == 1) {
					minuteBox = ((StudyFocusStrategy) newStrategy).getMinuteBox();
					secondBox = ((StudyFocusStrategy) newStrategy).getSecondBox();
				}
				
				//Pomodoro
				if(profile.getSettings().getFocusMode() == 2) {
					pomoSessionBox = ((StudyFocusStrategy) newStrategy).getPomoSessionBox();
					pomoBreakBox = ((StudyFocusStrategy) newStrategy).getPomoBreakBox();
					pomoNumberSessionBox = ((StudyFocusStrategy) newStrategy).getPomoNumberSessionBox();
					currentSessionLabel = ((StudyFocusStrategy) newStrategy).getCurrentSessionLabel();
				}
				setStudyComponentFunctions();
				
				recall(newStrategy);
			}
		});
		buttons.push(focusButton);
		
		JButton shopButton = new JButton(profile.getLanguage().text[4]);
		setUpButtonComponent(shopButton);
		shopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("[TAMOSTUDY] Changing Strategy to Shop");
				discordRP.update("Idle", "Shopping");
				
				updateSideBar();
				StateStrategy newStrategy = new ShopStrategy(profile);
				recall(newStrategy);
			}
		});
		buttons.push(shopButton);
		
		JButton themesButton = new JButton(profile.getLanguage().text[5]);
		setUpButtonComponent(themesButton);
		themesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("[TAMOSTUDY] Changing Strategy to Themes");
				discordRP.update("Idle", "Browsing Themes");
				
				updateSideBar();
				StateStrategy newStrategy = new ThemeStrategy(profile);
				
				//Actions that affect MainGUI
				selectButtons = ((ThemeStrategy) newStrategy).getSelectButtons();
				for(JButton button : selectButtons) {
					button.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							if(button.getText().equals("<html>" + profile.getLanguage().themesText[9] + "<br>" + profile.getLanguage().themesText[0] + "</html>")) { themeChanged(0); }
							else if(button.getText().equals("<html>" + profile.getLanguage().themesText[9] + "<br>" + profile.getLanguage().themesText[1] + "</html>")) { themeChanged(1); }
							else if(button.getText().equals("<html>" + profile.getLanguage().themesText[9] + "<br>" + profile.getLanguage().themesText[2] + "</html>")) { themeChanged(2); }
							else if(button.getText().equals("<html>" + profile.getLanguage().themesText[9] + "<br>" + profile.getLanguage().themesText[3] + "</html>")) { themeChanged(3); }
							else if(button.getText().equals("<html>" + profile.getLanguage().themesText[9] + "<br>" + profile.getLanguage().themesText[4] + "</html>")) { themeChanged(4); }
							else if(button.getText().equals("<html>" + profile.getLanguage().themesText[9] + "<br>" + profile.getLanguage().themesText[5] + "</html>")) { themeChanged(5); }
							else if(button.getText().equals("<html>" + profile.getLanguage().themesText[9] + "<br>" + profile.getLanguage().themesText[6] + "</html>")) { themeChanged(6); }
							else if(button.getText().equals("<html>" + profile.getLanguage().themesText[9] + "<br>" + profile.getLanguage().themesText[7] + "</html>")) { themeChanged(7); }
						}
					});
				}
				recall(newStrategy);
			}
		});
		buttons.push(themesButton);
		
		JButton inventoryButton = new JButton(profile.getLanguage().text[6]);
		setUpButtonComponent(inventoryButton);
		inventoryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("[TAMOSTUDY] Changing Strategy to Inventory");
				discordRP.update("Idle", "Viewing Inventory");
				
				updateSideBar();
				StateStrategy newStrategy = new InventoryStrategy(profile);
				recall(newStrategy);
			}
		});
		buttons.push(inventoryButton);
		
		JButton statsButton = new JButton(profile.getLanguage().text[7]);
		setUpButtonComponent(statsButton);
		statsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("[TAMOSTUDY] Changing Strategy to Statistics");
				discordRP.update("Idle", "Viewing Statistics");
				
				updateSideBar();
				StateStrategy newStrategy = new StatisticsStrategy(profile);
				recall(newStrategy);
			}
		});
		buttons.push(statsButton);
		
		JButton achievementsButton = new JButton(profile.getLanguage().text[8]);
		setUpButtonComponent(achievementsButton);
		achievementsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("[TAMOSTUDY] Changing Strategy to Achievements");
				discordRP.update("Idle", "Viewing Achievements");
				
				updateSideBar();
				StateStrategy newStrategy = new AchievementsStrategy(profile);
				recall(newStrategy);
			}
		});
		buttons.push(achievementsButton);
		
		JButton settingsButton = new JButton(profile.getLanguage().text[9]);
		setUpButtonComponent(settingsButton);
		settingsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("[TAMOSTUDY] Changing Strategy to Settings");
				discordRP.update("Idle", "Changing Settings");
				
				updateSideBar();
				StateStrategy newStrategy = new SettingsStrategy(profile);
				
				saveChangesButton = ((SettingsStrategy) newStrategy).getSaveChangesButton();
				saveChangesButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) { updateLanguageChange(); }
				});
				
				recall(newStrategy);
			}
		});
		buttons.push(settingsButton);
		
		JButton aboutButton = new JButton(profile.getLanguage().text[10]);
		setUpButtonComponent(aboutButton);
		aboutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("[TAMOSTUDY] Changing Strategy to About");
				discordRP.update("Idle", "Reading About");
				
				updateSideBar();
				StateStrategy newStrategy = new AboutStrategy(profile);
				recall(newStrategy);
			}
		});
		buttons.push(aboutButton);
		
		//Adds components to the sidePanel
		sidePanel.add(titleCardButton);
		sidePanel.add(focusButton);
		sidePanel.add(thematicBreak);
		sidePanel.add(shopButton);
		sidePanel.add(themesButton);
		sidePanel.add(inventoryButton);
		sidePanel.add(statsButton);
		sidePanel.add(achievementsButton);
		sidePanel.add(thematicBreak2);
		sidePanel.add(settingsButton);
		sidePanel.add(aboutButton);
		
		//Initializing Open Side Panel
		openSidePanel = new JPanel();
		openSidePanel.setBackground(theme.mainColor);
		panels.add(openSidePanel);
		
		//OpenSidePanel components
		openMenuButton = new JButton(profile.getLanguage().text[0]);
		setUpButtonComponent(openMenuButton, 1);
		openMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("[TAMOSTUDY] Menu was updated!");
				updateSideBar();
			}
		});
		
		JLabel welcomeUserLabel = new JLabel(profile.getLanguage().text[1] + profile.getUsername());
		setUpLabelComponent(welcomeUserLabel);
		labels.push(welcomeUserLabel);
		
		openSidePanel.add(openMenuButton);
		openSidePanel.add(welcomeUserLabel);
	}
	
	/**
	 * initComponentsToFrame
	 * @brief adds the main components to the frame
	 */
	public void initComponentsToFrame() {
		this.add(openSidePanel, BorderLayout.NORTH);
		this.add(sidePanel, BorderLayout.WEST);
		this.add(strategy, BorderLayout.CENTER);
	}
	
	/**
	 * updateSideBar
	 * @brief Opens/Closes Side Bar Menu
	 */
	public void updateSideBar() {
		if (this.openedSideBar) {
			//Close the side bar
			this.openedSideBar = false;
			this.sidePanel.setVisible(false);
		} else {
			//Open the side bar
			this.openedSideBar = true;
			this.sidePanel.setVisible(true);
		}
	}
	
	/**
	 * setUpLabelComponent
	 * @brief specifies foreground and font of JLabel
	 * @param label
	 */
	public void setUpLabelComponent(JLabel label) {
		label.setForeground(theme.layerTextColor);
		label.setFont(new Font("Arial", Font.BOLD, 16));
	}
	
	/**
	 * setUpLabelComponent
	 * @brief specifies foreground of JLabel
	 * @param label
	 * @param num
	 */
	public void setUpLabelComponent(JLabel label, int num) {
		label.setForeground(theme.layerTextColor);
	}
	
	/**
	 * setUpButtonComponent
	 * @brief specifies JButton attributes
	 * @param button
	 */
	public void setUpButtonComponent(JButton button) {
		button.setBorderPainted(false);
		button.setBackground(theme.mainColor);
		button.setForeground(theme.layerTextColor);
		button.setFocusPainted(false);
		button.setFont(new Font("Arial", Font.BOLD, 16));
	}
	
	/**
	 * setUpButtonComponent
	 * @brief specifies JButton attributes
	 * @param button
	 * @param num
	 */
	public void setUpButtonComponent(JButton button, int num) {
		button.setOpaque(true);
		button.setBackground(theme.mainColor);
		button.setForeground(theme.layerTextColor);
		button.setFocusPainted(false);
		button.setFont(new Font("Arial", Font.BOLD, 16));
	}
	
	/**
	 * themeChanged
	 * @brief Indicates the theme has changed
	 * @param i representing theme index
	 */
	public void themeChanged(int i) {
		profile.setThemeIndicator(i);
		theme = profile.getThemeIndicator();
		themeAction = new CommunicateThemeAction(theme, openMenuButton, panels, buttons, labels, breaks);
		themeAction.updateMainGUI();
		
		UIManager.put("OptionPane.background", theme.layerColor);
		UIManager.put("Panel.background", theme.layerColor);
	}
	
	/**
	 * setStudyComponentFunctions
	 * The point of this is to disable the buttons
	 * on the outside when the start button is selected
	 */
	public void setStudyComponentFunctions() {
		breakFocusButton.setEnabled(false);
		
		//Start Button
		startFocusButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Update Timer Information
				updateTimerInformation();
				
				//Set Information, TamoImage, study/Temp Min/Sec
				setFocusInformation();
				
				//Disable Buttons
				disableFocusButtons();
				
				//Create Timer
				createTimer();
			}
			
		});
		
		breakFocusButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				discordRP.update("Idle", "Focus");
				
				String studyMessage = profile.getLanguage().focusText[8] + " " + tempMin + " " + profile.getLanguage().focusText[9] + " " + tempSec  + " " + profile.getLanguage().focusText[10];
				profile.updateStudyStats(tempMin, tempSec);
				
				resetTimer();
				timer.stop();
				
				JOptionPane.showMessageDialog(rootPane, studyMessage, profile.getLanguage().focusText[7], JOptionPane.INFORMATION_MESSAGE,  new ImageIcon(getClass().getClassLoader().getResource("INFO.png")));
			}
			
		});
	}
	
	//Step 1 - Update Timer Information
	public void updateTimerInformation() {
		
		//Custom Count down
		if(profile.getSettings().getFocusMode() == 0 ) {
			minuteTime.setText(""+minuteBox.getSelectedItem());
			secondTime.setText(""+secondBox.getSelectedItem());
		}
		
		//5 min interval
		if(profile.getSettings().getFocusMode() == 1) {
			minuteTime.setText(""+minuteBox.getSelectedItem());
			minuteTime.setText(minuteTime.getText().substring(0,2));
		}
		
		//pomodoro
		if(profile.getSettings().getFocusMode() == 2) {
			totalPomodoroSessions = pomoNumberSessionBox.getSelectedIndex();
			System.out.println("pomoSessions: " + totalPomodoroSessions);
			minuteTime.setText(""+pomoSessionBox.getSelectedItem());
			minuteTime.setText(minuteTime.getText().substring(0,2));
		}
		
		min = Integer.parseInt(minuteTime.getText());
		sec = Integer.parseInt(secondTime.getText());
	}
	
	//Step 2 - Set Information, TamoImage, etc.
	public void setFocusInformation() {
		//Pomodoro sets the time information
		if(profile.getSettings().getFocusMode() == 2) { setCurrentSession(); }
		
		//TODO Update Tamo Image to focus mode
		tamoImageLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource(profile.getTamo().getImageUrl(true))));
		
		//Initialize other values
		studyMin = Integer.parseInt(minuteTime.getText());
		studySec = Integer.parseInt(secondTime.getText());
		tempSec = -1;
		tempMin = 0;
	}
	
	//Step 3 - Disable Buttons
	public void disableFocusButtons() {
		//Disable Focus Components
		
		if(profile.getSettings().getFocusMode() == 0 || profile.getSettings().getFocusMode() == 1)
			minuteBox.setEnabled(false);
		if(profile.getSettings().getFocusMode() == 0)
			secondBox.setEnabled(false);
		if(profile.getSettings().getFocusMode() == 2) {
			pomoNumberSessionBox.setEnabled(false);
			pomoSessionBox.setEnabled(false);
			pomoBreakBox.setEnabled(false);
		}
		
		startFocusButton.setEnabled(false);
		breakFocusButton.setEnabled(true);
		
		//Disable openSideBar Components
		for(JButton button : buttons) { button.setEnabled(false); }
	}
	
	//Step 4 - Create Timer
	public void createTimer() {
		timer = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Set how long studied for variables
				tempSec = tempSec + 1;
				if(tempSec == 60) {
					tempMin = tempMin + 1;
					tempSec = 0;
				}
				
				if(sec == 0) {
					sec = 60;
					min--;
				}
				//Timer Completed
				if(min < 0) {
					
					profile.updateStudyStats(tempMin, tempSec);
					String studyMessage = profile.getLanguage().focusText[8] + " " + tempMin + " " + profile.getLanguage().focusText[9] + " " + tempSec + " " + profile.getLanguage().focusText[10];
					
					tempMin = 0;
					tempSec = 0;

					if(profile.getSettings().getSessionSoundIndicator() >= 1) {
					
						try {
							//Get the url for the sound clip
							String soundPath = profile.getSettings().getSoundPath();
							
							URL url = this.getClass().getClassLoader().getResource(soundPath);
							AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
							
							//get the clip from the url
							Clip clip = AudioSystem.getClip();
							clip.open(audioIn);
							
							//volume control - make the sound quieter
							FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
					        volume.setValue(-1 * 20);
							
					        //start and loop the clip
							clip.start();
							clip.loop(Clip.LOOP_CONTINUOUSLY);
							
							//loop will end when user hits ok dialog
							discordRP.update("Idle", "Focus");
							JOptionPane.showMessageDialog(rootPane, studyMessage, profile.getLanguage().focusText[6], JOptionPane.INFORMATION_MESSAGE,  new ImageIcon(getClass().getClassLoader().getResource("INFO.png")));
							clip.stop();
							
						} catch (Exception ex2) {
							ex2.printStackTrace();
						}
					
					} else {
						discordRP.update("Idle", "Focus");
						JOptionPane.showMessageDialog(rootPane, studyMessage, profile.getLanguage().focusText[6], JOptionPane.INFORMATION_MESSAGE,  new ImageIcon(getClass().getClassLoader().getResource("INFO.png")));
						
					}
					//TODO Display Completed message, in the future, it will do a calculation to show amount of points earned in the session
					
					if(profile.getSettings().getFocusMode() == 2 && totalPomodoroSessions != 0) {
						System.out.println("pomoSessionNumber: " + totalPomodoroSessions);
						nextSession();
						setCurrentSession();
					} else {
						resetTimer();
						timer.stop();
					}
				} 
				//Timer still running
				else {
					sec--;
					if(sec < 10) {
						secondTime.setText("0" + sec);
					}
					else {
						secondTime.setText("" + sec);
					}
					if(min < 10) {
						minuteTime.setText("0" + min);
					}
					else {
						minuteTime.setText("" + min);
					}
					updateRPCTimer();
				}
			}
		});
		
		//Start timer
		timer.start();
	}
	
	/**
	 * resetTimer
	 * @brief Resets the timer either when 'Break' is clicked or
	 * end of a regular session
	 */
	public void resetTimer() {
		//Custom Interval Timer Reset
		if(profile.getSettings().getFocusMode() == 0) {
			minuteBox.setEnabled(true);
			secondBox.setEnabled(true);
			
			minuteTime.setText(minuteBox.getSelectedItem().toString());
			secondTime.setText(secondBox.getSelectedItem().toString());
		}
		
		//5-Interval Countdown Reset
		if(profile.getSettings().getFocusMode() == 1) {
			minuteBox.setEnabled(true);
			minuteBox.setSelectedIndex(0);
			
			minuteTime.setText(minuteBox.getSelectedItem().toString().toString().substring(0,2));
			secondTime.setText("00");
		}
		
		//Pomodoro Reset
		if(profile.getSettings().getFocusMode() == 2) {
			currentSessionLabel.setText(profile.getLanguage().focusText[11]);
			
			minuteTime.setText("00");
			secondTime.setText("00");
			
			pomoNumberSessionBox.setEnabled(true);
			pomoSessionBox.setEnabled(true);
			pomoBreakBox.setEnabled(true);
		}
		
		//Update Tamo Image
		tamoImageLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource(profile.getTamo().getImageUrl(false))));
		
		//Enable focus/break buttons
		startFocusButton.setEnabled(true);
		breakFocusButton.setEnabled(false);
		
		//Disable openSideBar Components
		for(JButton button : buttons) { button.setEnabled(true); }
		
		//Check Happiness/Study Achievement
		checkForNewAchievements();
		
		//Update info to file
		ProfileReaderWriter.updateProfileInfoToFile(profile);
	}
	
	/**
	 * nextSession
	 * @brief Indicates next session of Pomodoro mode
	 */
	public void nextSession() {
		if(breakCondition == false) {
			//Start Break Timer
			minuteTime.setText(""+pomoBreakBox.getSelectedItem());
			minuteTime.setText(minuteTime.getText().substring(0,2));
			min = Integer.parseInt(minuteTime.getText());
			sec = Integer.parseInt(secondTime.getText());
			breakCondition = true;
			
			startFocusButton.doClick();
		}
		else {
			//End Break Timer, begin next session timer
			minuteTime.setText(""+pomoSessionBox.getSelectedItem());
			minuteTime.setText(minuteTime.getText().substring(0,2));
			min = Integer.parseInt(minuteTime.getText());
			sec = Integer.parseInt(secondTime.getText());
			breakCondition = false;
			this.totalPomodoroSessions--;
			
			//Update Tamo Image
			tamoImageLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource(profile.getTamo().getImageUrl(false))));
			startFocusButton.doClick();
		}
	}
	
	/**
	 * setCurrentSession
	 * @brief Sets the current session in Pomodoro Mode
	 */
	public void setCurrentSession() {
		if(breakCondition) {
			currentSessionLabel.setText(profile.getLanguage().focusText[12]);
		} else {
			currentPomodoroSession = pomoNumberSessionBox.getSelectedIndex() - totalPomodoroSessions;
			System.out.println("currentSession = " + (currentPomodoroSession + 1));
			currentSessionLabel.setText((currentPomodoroSession + 1) + " / " + (pomoNumberSessionBox.getSelectedIndex() + 1));
		}
		
	}
	
	/**
	 * checkForNewAchievements
	 * @brief This message runs on route changes
	 * to update the user in case they have received
	 * a new achievement.
	 */
	public void checkForNewAchievements() {
		System.out.println("[TAMOSTUDY] Checking for new Achievements");
		
		//Reach 3 hours focus time
		if(profile.getTotalTime() >= 10800 && profile.getAhmIndicator(0).equals("0")) {
			profile.getAchievement(0);
			
			if(profile.getSettings().getShowAhmNotifications() == 1)
				JOptionPane.showMessageDialog(rootPane, profile.getLanguage().ahmTitle[0], profile.getLanguage().text[11], JOptionPane.INFORMATION_MESSAGE,  new ImageIcon(getClass().getClassLoader().getResource("INFO.png")));
		}
		//Reach 1 day focus time
		if(profile.getTotalTime() >= 86400 && profile.getAhmIndicator(1).equals("0")) {
			profile.getAchievement(1);
			
			if(profile.getSettings().getShowAhmNotifications() == 1)
				JOptionPane.showMessageDialog(rootPane, profile.getLanguage().ahmTitle[1], profile.getLanguage().text[11], JOptionPane.INFORMATION_MESSAGE,  new ImageIcon(getClass().getClassLoader().getResource("INFO.png")));
		}
			
		//Reach 7 day focus time
		if(profile.getTotalTime() >= 604800 && profile.getAhmIndicator(2).equals("0")) {
			profile.getAchievement(2);
			
			if(profile.getSettings().getShowAhmNotifications() == 1)
				JOptionPane.showMessageDialog(rootPane, profile.getLanguage().ahmTitle[2], profile.getLanguage().text[11], JOptionPane.INFORMATION_MESSAGE,  new ImageIcon(getClass().getClassLoader().getResource("INFO.png")));
		}

		//Reach 30 day focus time
		if(profile.getTotalTime() >= 2592000 && profile.getAhmIndicator(3).equals("0")) {
			profile.getAchievement(3);
			
			if(profile.getSettings().getShowAhmNotifications() == 1)
				JOptionPane.showMessageDialog(rootPane, profile.getLanguage().ahmTitle[3], profile.getLanguage().text[11], JOptionPane.INFORMATION_MESSAGE,  new ImageIcon(getClass().getClassLoader().getResource("INFO.png")));
		}
		
		//Check if Tamo is full Happiness
		if(profile.getTamo().getHappiness() == 10 && profile.getAhmIndicator(8).equals("0")) {
			profile.getAchievement(8);
			
			if(profile.getSettings().getShowAhmNotifications() == 1)
				JOptionPane.showMessageDialog(this, profile.getLanguage().ahmTitle[8], profile.getLanguage().text[11], JOptionPane.INFORMATION_MESSAGE,  new ImageIcon(getClass().getClassLoader().getResource("INFO.png")));
		}
		
		ProfileReaderWriter.updateProfileInfoToFile(profile);
	}
	
	/**
	 * updateLanguageChange
	 * @brief Updates language components
	 * on a change in the language
	 */
	public void updateLanguageChange() {
		int indicator = ((SettingsStrategy) strategy).getLanguageIndicatorFromBox();
		profile.setLanguageIndicator(indicator);
		profile.setLanguageStrategy(indicator);
		profile.getLanguage().printCurrentLanguage();
		
		//Menu Label
		openMenuButton.setText(profile.getLanguage().text[0]);
		
		//Button Labels
		int buttonIndicator = 0;
		for(JButton button : buttons) {
			if(buttonIndicator == 0) { button.setText(profile.getLanguage().text[2]); }
			else if(buttonIndicator == 1) { button.setText(profile.getLanguage().text[3]); }
			else if(buttonIndicator == 2) { button.setText(profile.getLanguage().text[4]); }
			else if(buttonIndicator == 3) { button.setText(profile.getLanguage().text[5]); }
			else if(buttonIndicator == 4) { button.setText(profile.getLanguage().text[6]); }
			else if(buttonIndicator == 5) { button.setText(profile.getLanguage().text[7]); }
			else if(buttonIndicator == 6) { button.setText(profile.getLanguage().text[8]); }
			else if(buttonIndicator == 7) { button.setText(profile.getLanguage().text[9]); }
			else if(buttonIndicator == 8) { button.setText(profile.getLanguage().text[10]); }
			
			buttonIndicator++;
		}
		
		//Other Labels
		int labelIndicator = 0;
		for(JLabel label : labels) {
			if(labelIndicator == 0) { label.setText(profile.getLanguage().text[1] + profile.getUsername()); }
			
			labelIndicator++;
		}
	}
	
	/**
	 * updateRPCTimer
	 * @brief Updates Discord Rich Presence
	 * timer on focus or break session
	 */
	public void updateRPCTimer() {
		if(breakCondition) {
			discordRP.update("Break", minuteTime.getText() + ":" + secondTime.getText() + " Remaining");
		} else {
			discordRP.update("Focusing", minuteTime.getText() + ":" + secondTime.getText() + " Remaining");
		}
	}
}
