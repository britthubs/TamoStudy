package model.profile;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import model.language.Language;
import util.Utils;

public class Profile {
	
	private long id;
	
	private String name;
	
	private String previousDateString;
	
	private long time;
	
	private long tokens;
	
	private ProfileSettings settings;
	
	private long backgroundIndicator;
	
	private long borderIndicator;
	
	private List<Long> achievementList; 
	
	private List<Long> foodInventoryList;
	
	private List<Long> backgroundInventoryList;
	
	private List<Long> borderInventoryList;
	
	private Tamo tamo;
	
	private List<Tamo> tamoHistory;
	
	public Profile(String name, Language language, long focusMode, long difficulty, String tamoName) {
		super();
		this.id = generateRandomProfileId();
		this.name = name;
		this.previousDateString = Utils.todayAsString();
		this.time = 0;
		this.tokens = 0;
		this.settings = new ProfileSettings(
					language,
					focusMode,
					difficulty
				);
		this.backgroundIndicator = 0;
		this.borderIndicator = 0;
		this.achievementList = Collections.emptyList();
		this.foodInventoryList = Collections.emptyList();
		this.backgroundInventoryList = List.of((long) 0);
		this.borderInventoryList = List.of((long) 0);
		this.tamo = new Tamo(
					name
				);
		this.tamoHistory = Collections.emptyList();
	}

	public Profile(long id, String name, String previousDateString, long time, long tokens, ProfileSettings settings,
			long backgroundIndicator, long borderIndicator, List<Long> achievementList, List<Long> foodInventoryList,
			List<Long> backgroundInventoryList, List<Long> borderInventoryList, Tamo tamo, List<Tamo> tamoHistory) {
		super();
		this.id = id;
		this.name = name;
		this.previousDateString = previousDateString;
		this.time = time;
		this.tokens = tokens;
		this.settings = settings;
		this.backgroundIndicator = backgroundIndicator;
		this.borderIndicator = borderIndicator;
		this.achievementList = achievementList;
		this.foodInventoryList = foodInventoryList;
		this.backgroundInventoryList = backgroundInventoryList;
		this.borderInventoryList = borderInventoryList;
		this.tamo = tamo;
		this.tamoHistory = tamoHistory;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPreviousDateString() {
		return previousDateString;
	}

	public void setPreviousDateString(String previousDateString) {
		this.previousDateString = previousDateString;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getTokens() {
		return tokens;
	}

	public void setTokens(long tokens) {
		this.tokens = tokens;
	}

	public ProfileSettings getSettings() {
		return settings;
	}

	public void setSettings(ProfileSettings settings) {
		this.settings = settings;
	}

	public long getBackgroundIndicator() {
		return backgroundIndicator;
	}

	public void setBackgroundIndicator(long backgroundIndicator) {
		this.backgroundIndicator = backgroundIndicator;
	}

	public long getBorderIndicator() {
		return borderIndicator;
	}

	public void setBorderIndicator(long borderIndicator) {
		this.borderIndicator = borderIndicator;
	}

	public List<Long> getAchievementList() {
		return achievementList;
	}

	public void setAchievementList(List<Long> achievementList) {
		this.achievementList = achievementList;
	}

	public List<Long> getFoodInventoryList() {
		return foodInventoryList;
	}

	public void setFoodInventoryList(List<Long> foodInventoryList) {
		this.foodInventoryList = foodInventoryList;
	}

	public List<Long> getBackgroundInventoryList() {
		return backgroundInventoryList;
	}

	public void setBackgroundInventoryList(List<Long> backgroundInventoryList) {
		this.backgroundInventoryList = backgroundInventoryList;
	}

	public List<Long> getBorderInventoryList() {
		return borderInventoryList;
	}

	public void setBorderInventoryList(List<Long> borderInventoryList) {
		this.borderInventoryList = borderInventoryList;
	}

	public Tamo getTamo() {
		return tamo;
	}

	public void setTamo(Tamo tamo) {
		this.tamo = tamo;
	}

	public List<Tamo> getTamoHistory() {
		return tamoHistory;
	}

	public void setTamoHistory(List<Tamo> tamoHistory) {
		this.tamoHistory = tamoHistory;
	}
	
	private long generateRandomProfileId() {
		Random random = new Random();
        int id = 0;
        for (int i = 0; i < 5; i++) {
            id = id * 10 + random.nextInt(10);
        }
        return id;
	}
}
