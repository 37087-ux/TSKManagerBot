package me.david.tskmanager.commands.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.commands.CommandModel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ScanRobloxAccountCommand extends CommandModel {

	public ScanRobloxAccountCommand() {
		super("scanrobloxaccount|scrblx", "Scans the followers, following, friends, or people in a group for players added to a sus list.", "scanrobloxaccount|scrblx " +
				"{following|followers|friends|group} {rblxUsername|groupID}");
		setRankUse(true, false);
	}

	@Override
	public void onCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());

		if (args.size() == 3)
			try {
				Long userID;
				if (!args.get(1).equalsIgnoreCase("group")) {
					URL url = new URL("http://api.roblox.com/users/get-by-username?username=" + args.get(2));
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setRequestProperty("User-agent", "TSKManagerBot");
					StringBuffer response = new StringBuffer();
					try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
						String line;
						while ((line = reader.readLine()) != null)
							response.append(line);
					}

					JSONObject jsonObject = new JSONObject(response.toString());

					if (jsonObject.has("errorMessage")) {
						event.getChannel().sendMessage("That user does not exist!").queue();
						return;
					} else {
						userID = jsonObject.getLong("Id");
					}
				} else
					userID = 0L;

				if (args.get(1).equalsIgnoreCase("followers")) {
					String nextPageCursor = "none";
					List<String> susFollowers = new ArrayList<>();

					while (nextPageCursor != null) {
						URL followersURL;
						if (!nextPageCursor.equalsIgnoreCase("none"))
							followersURL = new URL("https://friends.roblox.com/v1/users/" + userID + "/followers?sortOrder=Asc&limit=100&cursor=" + nextPageCursor);
						else
							followersURL = new URL("https://friends.roblox.com/v1/users/" + userID + "/followers?sortOrder=Asc&limit=100");

						HttpURLConnection followersConnection = (HttpURLConnection) followersURL.openConnection();
						followersConnection.setRequestProperty("User-agent", "TSKManagerBot");
						StringBuffer followersResponse = new StringBuffer();
						try (BufferedReader reader = new BufferedReader(new InputStreamReader(followersConnection.getInputStream()))) {
							String line;
							while ((line = reader.readLine()) != null)
								followersResponse.append(line);
						} finally {
							followersConnection.disconnect();
						}

						JSONObject followersJsonObject = new JSONObject(followersResponse.toString());

						try {
							nextPageCursor = followersJsonObject.getString("nextPageCursor");
						} catch (JSONException e) {
							nextPageCursor = null;
						}

						JSONArray jsonArray = followersJsonObject.getJSONArray("data");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject userJsonObject = jsonArray.getJSONObject(i);
							if (cache.getSusList().contains(userJsonObject.getLong("id")))
								susFollowers.add(userJsonObject.getString("name"));
						}
					}

					if (!susFollowers.isEmpty()) {
						StringBuilder message = new StringBuilder();
						message.append("Found these sus followers: ");
						for (String user : susFollowers)
							message.append(user + ", ");
						event.getChannel().sendMessage(message.toString()).queue();
					} else
						event.getChannel().sendMessage("Found no sus followers for " + args.get(2)).queue();

				} else if (args.get(1).equalsIgnoreCase("following")) {
					String nextPageCursor = "none";
					List<String> susFollowing = new ArrayList<>();

					while (nextPageCursor != null) {
						URL followingURL;
						if (!nextPageCursor.equalsIgnoreCase("none"))
							followingURL = new URL("https://friends.roblox.com/v1/users/" + userID + "/followings?sortOrder=Asc&limit=100&cursor=" + nextPageCursor);
						else
							followingURL = new URL("https://friends.roblox.com/v1/users/" + userID + "/followings?sortOrder=Asc&limit=100");

						HttpURLConnection followingConnection = (HttpURLConnection) followingURL.openConnection();
						followingConnection.setRequestProperty("User-agent", "TSKManagerBot");
						StringBuffer followingResponse = new StringBuffer();
						try (BufferedReader reader = new BufferedReader(new InputStreamReader(followingConnection.getInputStream()))) {
							String line;
							while ((line = reader.readLine()) != null)
								followingResponse.append(line);
						} finally {
							followingConnection.disconnect();
						}

						JSONObject followingJsonObject = new JSONObject(followingResponse.toString());

						try {
							nextPageCursor = followingJsonObject.getString("nextPageCursor");
						} catch (JSONException e) {
							nextPageCursor = null;
						}

						JSONArray jsonArray = followingJsonObject.getJSONArray("data");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject userJsonObject = jsonArray.getJSONObject(i);
							if (cache.getSusList().contains(userJsonObject.getLong("id")))
								susFollowing.add(userJsonObject.getString("name"));
						}
					}

					if (!susFollowing.isEmpty()) {
						StringBuilder message = new StringBuilder();
						message.append("Found these sus followings: ");
						for (String user : susFollowing)
							message.append(user + ", ");
						event.getChannel().sendMessage(message.toString()).queue();
					} else
						event.getChannel().sendMessage("Found no sus followings for " + args.get(2)).queue();
				} else if (args.get(1).equalsIgnoreCase("friends")) {
					URL friendsURL = new URL("https://friends.roblox.com/v1/users/" + userID + "/friends");
					HttpURLConnection friendsConnection = (HttpURLConnection) friendsURL.openConnection();
					friendsConnection.setRequestProperty("User-agent", "TSKManagerBot");
					StringBuffer friendsResponse = new StringBuffer();
					try (BufferedReader reader = new BufferedReader(new InputStreamReader(friendsConnection.getInputStream()))) {
						String line;
						while ((line = reader.readLine()) != null)
							friendsResponse.append(line);
					} finally {
						friendsConnection.disconnect();
					}

					JSONObject friendsJsonObject = new JSONObject(friendsResponse.toString());

					JSONArray jsonArray = friendsJsonObject.getJSONArray("data");

					List<String> susFriends = new ArrayList<>();

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject userJsonObject = jsonArray.getJSONObject(i);
						if (cache.getSusList().contains(userJsonObject.getLong("id")))
							susFriends.add(userJsonObject.getString("name"));
					}

					if (!susFriends.isEmpty()) {
						StringBuilder message = new StringBuilder();
						message.append("Found these sus friends: ");
						for (String user : susFriends)
							message.append(user + ", ");
						event.getChannel().sendMessage(message.toString()).queue();
					} else
						event.getChannel().sendMessage("Found no sus friends for " + args.get(2)).queue();

				} else if (args.get(1).equalsIgnoreCase("group")) {

					String nextPageCursor = "none";
					List<String> susGroupMember = new ArrayList<>();

					while (nextPageCursor != null) {
						URL groupURL;
						if (!nextPageCursor.equalsIgnoreCase("none"))
							groupURL = new URL("https://groups.roblox.com/v1/groups/" + args.get(2) + "/users?sortOrder=Asc&limit=100&cursor=" + nextPageCursor);
						else
							groupURL = new URL("https://groups.roblox.com/v1/groups/" + args.get(2) + "/users?sortOrder=Asc&limit=100");

						HttpURLConnection groupConnection = (HttpURLConnection) groupURL.openConnection();
						groupConnection.setRequestProperty("User-agent", "TSKManagerBot");
						StringBuffer groupResponse = new StringBuffer();
						try (BufferedReader reader = new BufferedReader(new InputStreamReader(groupConnection.getInputStream()))) {
							String line;
							while ((line = reader.readLine()) != null)
								groupResponse.append(line);
						} finally {
							groupConnection.disconnect();
						}

						JSONObject groupJsonObject = new JSONObject(groupResponse.toString());

						if (groupJsonObject.has("errors"))
							event.getChannel().sendMessage("That group does not exist!").queue();

						try {
							nextPageCursor = groupJsonObject.getString("nextPageCursor");
						} catch (JSONException e) {
							nextPageCursor = null;
						}

						JSONArray jsonArray = groupJsonObject.getJSONArray("data");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject userJsonObject = jsonArray.getJSONObject(i).getJSONObject("user");
							if (cache.getSusList().contains(userJsonObject.getLong("userId")))
								susGroupMember.add(userJsonObject.getString("username"));
						}
					}

					if (!susGroupMember.isEmpty()) {
						StringBuilder message = new StringBuilder();
						message.append("Found these sus group members: ");
						for (String user : susGroupMember)
							message.append(user + ", ");
						event.getChannel().sendMessage(message.toString()).queue();
					} else
						event.getChannel().sendMessage("Found no sus group members").queue();
				}
			} catch (Exception e) {
				e.printStackTrace();
				event.getChannel().sendMessage("Something went wrong! Please DM Burgboi.").queue();
			}
		else
			event.getChannel().sendMessage("Usage: " + cache.getPrefix() + getUsage()).queue();
	}
}
