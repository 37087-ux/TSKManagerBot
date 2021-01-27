package me.david.tskmanager;

import net.dv8tion.jda.api.entities.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuildCache {

	public static final Map<String, GuildCache> CACHE_MAP = new HashMap<>();

	private String guildID;
	private String prefix = "&";
	private Role shrRole;
	private Role hrRole;
	private Role lrRole;
	private List<Role> defaultJoinRoles = new ArrayList<>();
	private RanksTrack ranksTrack = new RanksTrack();
	private List<Role> LRMRRoles = new ArrayList<>();
	private List<Role> HRRoles = new ArrayList<>();
	private List<MessageChannel> eventChannels = new ArrayList<>();
	private Category eventsCategory;
	private List<Long> susList = new ArrayList<>();
	private Map<String, PointLeaderboardData> pointLeaderboard = new HashMap<>();
	private Map<String, DonationLeaderboardData> donationLeaderboard = new HashMap<>();
	private TextChannel donationLeaderboardChannel;
	private String donationLeaderboardMessageID;
	private List<MemberBypassLevel> memberBypassList = new ArrayList<>();

	public GuildCache(String guildID) {
		this.guildID = guildID;
	}

	//get the cache and if there isn't a cache, put a cache and deserialize the data from a disk
	public static GuildCache getCache(String guildID) {
		GuildCache cache = CACHE_MAP.get(guildID);

		if (cache == null) {

			cache = new GuildCache(guildID);
			cache.deserialize();
			CACHE_MAP.put(guildID, cache);
		}

		return cache;
	}

	//serialize the cache
	public void serialize() {
		try {

			//create new file and folder if does not exist
			File file = new File("./GuildData/", guildID + ".json");
			File directory = new File("./GuildData/");

			if (!directory.exists())
				directory.mkdir();
			if (!file.exists())
				file.createNewFile();

			//putting the data into a JSONObject
			JSONObject jsonObject = new JSONObject();
			if (prefix != null)
				jsonObject.put(JsonDataKeys.PREFIX.getKey(), prefix);

			if (hrRole != null)
				jsonObject.put(JsonDataKeys.HRROLE.getKey(), hrRole.getId());

			if (lrRole != null)
				jsonObject.put(JsonDataKeys.LR_ROLE.getKey(), lrRole.getId());

			if (!defaultJoinRoles.isEmpty()) {
				JSONArray jsonArray = new JSONArray();
				for (Role role : defaultJoinRoles)
					jsonArray.add(role.getId());
				jsonObject.put(JsonDataKeys.DEFAULT_JOIN_ROLES.getKey(), jsonArray);
			}

			if (!ranksTrack.getRankTrack().isEmpty()) {
				JSONArray jsonArray = new JSONArray();
				for (Role role : ranksTrack.getRankTrack())
					jsonArray.add(role.getId());
				jsonObject.put(JsonDataKeys.RANKS_TRACK.getKey(), jsonArray);
			}

			if (!LRMRRoles.isEmpty()) {
				JSONArray jsonArray = new JSONArray();
				for (Role role : LRMRRoles)
					jsonArray.add(role.getId());
				jsonObject.put(JsonDataKeys.LRMR_ROLES.getKey(), jsonArray);
			}

			if (!HRRoles.isEmpty()) {
				JSONArray jsonArray = new JSONArray();
				for (Role role : HRRoles)
					jsonArray.add(role.getId());
				jsonObject.put(JsonDataKeys.HRROLES.getKey(), jsonArray);
			}

			if (!eventChannels.isEmpty()) {
				JSONArray jsonArray = new JSONArray();
				for (MessageChannel channel : eventChannels)
					jsonArray.add(channel.getId());
				jsonObject.put(JsonDataKeys.EVENT_CHANNELS.getKey(), jsonArray);
			}

			if (eventsCategory != null)
				jsonObject.put(JsonDataKeys.EVENTS_CATEGORY.getKey(), eventsCategory.getId());

			if (shrRole != null)
				jsonObject.put(JsonDataKeys.SHR_ROLE.getKey(), shrRole.getId());

			if (!susList.isEmpty()) {
				JSONArray jsonArray = new JSONArray();
				for (Long id : susList)
					jsonArray.add(id);
				jsonObject.put(JsonDataKeys.SUS_LIST.getKey(), jsonArray);
			}

			if (!pointLeaderboard.isEmpty()) {
				JSONArray jsonArray = new JSONArray();
				for (Map.Entry<String, PointLeaderboardData> data : pointLeaderboard.entrySet()) {
					JSONObject jsonData = new JSONObject();
					jsonData.put("MemberID", data.getValue().getMember().getId());
					jsonData.put("Points", data.getValue().getPoints());
					jsonArray.add(jsonData);
				}
				jsonObject.put(JsonDataKeys.POINT_LEADERBOARD.getKey(), jsonArray);
			}

			if (!donationLeaderboard.isEmpty()) {
				JSONArray jsonArray = new JSONArray();
				for (Map.Entry<String, DonationLeaderboardData> data : donationLeaderboard.entrySet()) {
					JSONObject jsonData = new JSONObject();
					jsonData.put("MemberID", data.getValue().getMember().getId());
					jsonData.put("Credits", data.getValue().getCredits());
					jsonArray.add(jsonData);
				}
				jsonObject.put(JsonDataKeys.DONATION_LEADERBOARD.getKey(), jsonArray);
			}

			if (donationLeaderboardChannel != null)
				jsonObject.put(JsonDataKeys.DONATION_LEADERBOARD_CHANNEL.getKey(), donationLeaderboardChannel.getId());

			if (donationLeaderboardMessageID != null)
				jsonObject.put(JsonDataKeys.DONATION_LEADERBOARD_MESSAGE_ID.getKey(), donationLeaderboardMessageID);

			if (!memberBypassList.isEmpty()) {
				JSONArray jsonArray = new JSONArray();
				for (MemberBypassLevel memberBypass : memberBypassList) {
					JSONObject data = new JSONObject();
					data.put("MemberID", memberBypass.getMember().getId());
					data.put("BypassType", memberBypass.getMemberBypassType());
					jsonArray.add(data);
				}
				jsonObject.put(JsonDataKeys.MEMBER_BYPASS_LIST.getKey(), jsonArray);
			}


			//write the JSONObject to a file
			try (FileWriter fileWriter = new FileWriter(file)) {
				fileWriter.write(jsonObject.toString());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//deserialize data from a file
	public void deserialize() {

		//get the file
		File file = new File("./GuildData/", guildID + ".json");

		if (file.exists()) {
			//reading the file
			try (FileReader fileReader = new FileReader(file)) {

				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObject = (JSONObject) jsonParser.parse(fileReader);

				//parsing the JSONObject into variables
				if (isSet(jsonObject, JsonDataKeys.PREFIX.getKey()))
					this.prefix = (String) jsonObject.get(JsonDataKeys.PREFIX.getKey());

				if (isSet(jsonObject, JsonDataKeys.HRROLE.getKey()))
					this.hrRole = Main.jda.getGuildById(guildID).getRoleById((String) jsonObject.get(JsonDataKeys.HRROLE.getKey()));

				if (isSet(jsonObject, JsonDataKeys.LR_ROLE.getKey()))
					this.lrRole = Main.jda.getGuildById(guildID).getRoleById((String) jsonObject.get(JsonDataKeys.LR_ROLE.getKey()));

				if (isSet(jsonObject, JsonDataKeys.DEFAULT_JOIN_ROLES.getKey())) {
					JSONArray jsonArray = (JSONArray) jsonObject.get(JsonDataKeys.DEFAULT_JOIN_ROLES.getKey());
					for (int i = 0; i < jsonArray.size(); i++)
						this.defaultJoinRoles.add(Main.jda.getGuildById(guildID).getRoleById((String) jsonArray.get(i)));
				}

				if (isSet(jsonObject, JsonDataKeys.RANKS_TRACK.getKey())) {
					JSONArray jsonArray = (JSONArray) jsonObject.get(JsonDataKeys.RANKS_TRACK.getKey());
					for (int i = 0; i < jsonArray.size(); i++)
						this.ranksTrack.addRank(Main.jda.getGuildById(guildID).getRoleById((String) jsonArray.get(i)));
				}

				if (isSet(jsonObject, JsonDataKeys.LRMR_ROLES.getKey())) {
					JSONArray jsonArray = (JSONArray) jsonObject.get(JsonDataKeys.LRMR_ROLES.getKey());
					for (int i = 0; i < jsonArray.size(); i++)
						this.LRMRRoles.add(Main.jda.getGuildById(guildID).getRoleById((String) jsonArray.get(i)));
				}

				if (isSet(jsonObject, JsonDataKeys.HRROLES.getKey())) {
					JSONArray jsonArray = (JSONArray) jsonObject.get(JsonDataKeys.HRROLES.getKey());
					for (int i = 0; i < jsonArray.size(); i++)
						this.HRRoles.add(Main.jda.getGuildById(guildID).getRoleById((String) jsonArray.get(i)));
				}

				if (isSet(jsonObject, JsonDataKeys.EVENT_CHANNELS.getKey())) {
					JSONArray jsonArray = (JSONArray) jsonObject.get(JsonDataKeys.EVENT_CHANNELS.getKey());
					for (int i = 0; i < jsonArray.size(); i++)
						this.eventChannels.add(Main.jda.getGuildById(guildID).getTextChannelById((String) jsonArray.get(i)));
				}

				if (isSet(jsonObject, JsonDataKeys.EVENTS_CATEGORY.getKey()))
					this.eventsCategory = Main.jda.getGuildById(guildID).getCategoryById((String) jsonObject.get(JsonDataKeys.EVENTS_CATEGORY.getKey()));

				if (isSet(jsonObject, JsonDataKeys.SHR_ROLE.getKey()))
					this.shrRole = Main.jda.getGuildById(guildID).getRoleById((String) jsonObject.get(JsonDataKeys.SHR_ROLE.getKey()));

				if (isSet(jsonObject, JsonDataKeys.SUS_LIST.getKey())) {
					JSONArray jsonArray = (JSONArray) jsonObject.get(JsonDataKeys.SUS_LIST.getKey());
					for (int i = 0; i < jsonArray.size(); i++)
						this.susList.add((long) jsonArray.get(i));
				}

				if (isSet(jsonObject, JsonDataKeys.POINT_LEADERBOARD.getKey())) {
					JSONArray jsonArray = (JSONArray) jsonObject.get(JsonDataKeys.POINT_LEADERBOARD.getKey());
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject data = (JSONObject) jsonArray.get(i);
						Member member = Main.jda.getGuildById(guildID).retrieveMemberById((String) data.get("MemberID")).complete();
						pointLeaderboard.put(member.getNickname(), new PointLeaderboardData(member, (Long) data.get("Points")));
					}
				}

				if (isSet(jsonObject, JsonDataKeys.DONATION_LEADERBOARD.getKey())) {
					JSONArray jsonArray = (JSONArray) jsonObject.get(JsonDataKeys.DONATION_LEADERBOARD.getKey());
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject data = (JSONObject) jsonArray.get(i);
						Member member = Main.jda.getGuildById(guildID).retrieveMemberById((String) data.get("MemberID")).complete();
						this.donationLeaderboard.put(member.getNickname(), new DonationLeaderboardData(member, (Long) data.get("Credits")));
					}
				}

				if (isSet(jsonObject, JsonDataKeys.DONATION_LEADERBOARD_CHANNEL.getKey())) {
					this.donationLeaderboardChannel = Main.jda.getGuildById(guildID).getTextChannelById((String) jsonObject.get(JsonDataKeys.DONATION_LEADERBOARD_CHANNEL.getKey()));
				}

				if (isSet(jsonObject, JsonDataKeys.DONATION_LEADERBOARD_MESSAGE_ID.getKey()))
					this.donationLeaderboardMessageID = (String) jsonObject.get(JsonDataKeys.DONATION_LEADERBOARD_MESSAGE_ID.getKey());

				if (isSet(jsonObject, JsonDataKeys.MEMBER_BYPASS_LIST.getKey())) {
					JSONArray jsonArray = (JSONArray) jsonObject.get(JsonDataKeys.MEMBER_BYPASS_LIST.getKey());
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject data = (JSONObject) jsonArray.get(i);
						Member member = Main.jda.getGuildById(guildID).retrieveMemberById((String) data.get("MemberID")).complete();
						this.memberBypassList.add(new MemberBypassLevel(member, (Boolean) data.get("BypassType")));
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			//if the file doesn't exist create a new file for it
			serialize();
		}
	}

	//check if a key in a JSONObject has a value
	private boolean isSet(JSONObject jsonObject, String key) {
		if (jsonObject.get(key) != null) {
			return true;
		} else
			return false;
	}

	//create an enum of json data keys so I don't have to memorize all the data keys
	public enum JsonDataKeys {

		PREFIX("prefix"),
		HRROLE("hr-role"),
		LR_ROLE("lr-role"),
		DEFAULT_JOIN_ROLES("default-join-roles"),
		RANKS_TRACK("ranks-track"),
		LRMR_ROLES("lrmr-roles"),
		HRROLES("hr-roles"),
		EVENT_CHANNELS("event-channels"),
		EVENTS_CATEGORY("events-category"),
		SHR_ROLE("shr-role"),
		SUS_LIST("sus-list"),
		POINT_LEADERBOARD("point-leaderboard"),
		DONATION_LEADERBOARD("donation-leaderboard"),
		DONATION_LEADERBOARD_CHANNEL("donation-leaderboard-channel"),
		DONATION_LEADERBOARD_MESSAGE_ID("donation-leaderboard-message-id"),
		MEMBER_BYPASS_LIST("member-bypass-list");

		private final String key;

		JsonDataKeys(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}
	}

	//getters
	public String getPrefix() {
		return prefix;
	}

	public Role getHrRole() {
		return hrRole;
	}

	public List<Role> getDefaultJoinRoles() {
		return defaultJoinRoles;
	}

	public RanksTrack getRanksTrack() {
		return ranksTrack;
	}

	public List<Role> getLRMRRoles() {
		return LRMRRoles;
	}

	public List<Role> getHRRoles() {
		return HRRoles;
	}

	public List<MessageChannel> getEventChannels() {
		return eventChannels;
	}

	public Role getLrRole() {
		return lrRole;
	}

	public Category getEventsCategory() {
		return eventsCategory;
	}

	public Role getShrRole() {
		return shrRole;
	}

	public List<Long> getSusList() {
		return susList;
	}

	public Map<String, PointLeaderboardData> getPointLeaderboard() {
		return pointLeaderboard;
	}

	public Map<String, DonationLeaderboardData> getDonationLeaderboard() {
		return donationLeaderboard;
	}

	public TextChannel getDonationLeaderboardChannel() {
		return donationLeaderboardChannel;
	}

	public String getDonationLeaderboardMessageID() {
		return donationLeaderboardMessageID;
	}

	public List<MemberBypassLevel> getMemberBypassList() {
		return memberBypassList;
	}

	//setters
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setHrRole(Role hrRole) {
		this.hrRole = hrRole;
	}

	public void setLrRole(Role lrRole) {
		this.lrRole = lrRole;
	}

	public void setEventsCategory(Category eventsCategory) {
		this.eventsCategory = eventsCategory;
	}

	public void setShrRole(Role shrRole) {
		this.shrRole = shrRole;
	}

	public void setDonationLeaderboardChannel(TextChannel donationLeaderboardChannel) {
		this.donationLeaderboardChannel = donationLeaderboardChannel;
	}

	public void setDonationLeaderboardMessageID(String donationLeaderboardMessageID) {
		this.donationLeaderboardMessageID = donationLeaderboardMessageID;
	}
}
