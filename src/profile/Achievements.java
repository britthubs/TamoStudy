package profile;

/**
 * 
 * @author Anthony
 * @description Achievements for profile
 *
 */

public class Achievements {
	private final int ACHIEVEMENT_COUNT = 8;
	
	//Title of Achievement
	private String[] title = 
		{"The Beginning",
		"Nothing can stop you!",
		"Never give up!",
		"Customizer 1",
		"Customizer 2",
		"From the beginning",
		"Tamo full",
		"Tamo Love"}; 
	//Description of Achievement
	private String[] desc = 
		{"Reach Total focus time of 3 hours",
		"Reach Total focus time of 1 day",
		"Reach Total focus time of 7 days",
		"Change your background color",
		"Change your Tamo's background",
		"Updated profile from a version before beta",
		"Entering the food shop when Tamo is full",
		"Achieve maximum Tamo happiness"};
	
	//Indicator of Achievement
	private int[] indicator; 
	
	/*
	 * New Profile: Achievements will not be filled in
	 */
	public Achievements() {
		this.indicator = new int[ACHIEVEMENT_COUNT];
		updateAchievements();
	}
	
	/*
	 * Loaded Profile: Achievements will be updated
	 */
	public Achievements(String aline) {
		this.indicator = new int[ACHIEVEMENT_COUNT];
		updateAchievements(aline);
	}

	public void updateAchievements() {
		for(int i = 0; i < indicator.length; i++) {
			this.indicator[i] = 0;
		}
	}
	
	public void updateAchievements(String aline) {
		/*
		 * Program will split the String into substring for recognition
		 * Then will set the achievements according to their values
		 * 
		 * Ex: 10101010
		 * This maps to the profile having achievements 0,3,5, and 7
		 */
		String[] achline = new String[ACHIEVEMENT_COUNT];
		for(int i = 0; i < achline.length; i++) {
			achline[i] = aline.substring(i,i);
		}
		 
		for(int i = 0; i < indicator.length; i++) {
			this.indicator[i] = Integer.parseInt(achline[i]);
		}
	}
	
	public String getTitle(int index) {
		return this.title[index];
	}
	
	public String getDesc(int index) {
		return this.desc[index];
	}
	
	public int getIndicator(int index) {
		return this.indicator[index];
	}
	
}
