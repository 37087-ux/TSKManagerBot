package me.david.tskmanager.commands.eventlisteners.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.Main;
import me.david.tskmanager.commands.eventlisteners.CommandListenerModel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddProfilesSusList extends CommandListenerModel {

	public AddProfilesSusList(Member member, MessageChannel channel) {
		super(member, channel);
	}

	@Override
	public void onTextReceived(MessageReceivedEvent event) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());

		if (!event.getMessage().getContentRaw().equals("finish")) {

			Long userId = null;
			try {
				userId = Long.parseLong(event.getMessage().getContentRaw());
			} catch (NumberFormatException e) {
			}

			if (userId == null) {
				try {
					URL url = new URL("http://api.roblox.com/users/get-by-username?username=" + event.getMessage().getContentRaw());
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setRequestProperty("User-agent", "TSKManagerBot");
					StringBuffer response = new StringBuffer();
					try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
						String line;
						while ((line = reader.readLine()) != null)
							response.append(line);
					} finally {
						connection.disconnect();
					}

					JSONObject jsonObject = new JSONObject(response.toString());

					if (jsonObject.has("errorMessage"))
						event.getChannel().sendMessage("That user does not exist! Type finish to finish.").queue();
					else {
						cache.getSusList().add(jsonObject.getLong("Id"));
						event.getChannel().sendMessage("Added " + event.getMessage().getContentRaw() + " to the sus list. Type finish to finish.").queue();
					}
				} catch (Exception e) {
					e.printStackTrace();
					event.getChannel().sendMessage("Something went wrong! Please DM Burgboi").queue();
				}
			} else {
				try {
					URL url = new URL("https://users.roblox.com/v1/users/" + userId);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setRequestProperty("User-agent", "TSKManagerBot");
					StringBuffer response = new StringBuffer();

					try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
						String line;
						while ((line = reader.readLine()) != null)
							response.append(line);
					} finally {
						connection.disconnect();
					}

					JSONObject jsonObject = new JSONObject(response.toString());

					if (jsonObject.has("errors"))
						event.getChannel().sendMessage("That user does not exist! Type finish to finish.").queue();
					else {
						cache.getSusList().add(userId);
						event.getChannel().sendMessage("Added " + jsonObject.getString("name") + " to the sus list. Type finish to finish").queue();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} else {

			cache.serialize();
			event.getChannel().sendMessage("Successfully added profiles to the sus list").queue();
			Main.jda.removeEventListener(this);
		}
	}
}
