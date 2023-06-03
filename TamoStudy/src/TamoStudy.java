import gui.WelcomeGUI;
import resources.Debug;

/**
 * Runner - TamoStudy
 * 
 * @author Anthony Narlock (narlock)
 * 
 * @brief The main class that instantiates
 * the welcome GUI for TamoStudy
 */

public class TamoStudy {
	
	public static void main(String[] args) {
		Debug.info("TamoStudy.main", "TamoStudy program starting to run.");
		new WelcomeGUI();

	}
	
}
