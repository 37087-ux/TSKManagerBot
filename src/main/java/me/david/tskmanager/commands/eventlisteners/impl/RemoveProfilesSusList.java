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

public class RemoveProfilesSusList extends CommandListenerModel {

	public RemoveProfilesSusList(Member member, MessageChannel channel) {
		super(member, channel);
	}

	@Override
	public void onTextReceived(MessageReceivedEvent event) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());

		if (!event.getMessage().getContentRaw().equalsIgnoreCase("finish")) {

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
						event.getChannel().sendMessage("That user is not on the sus list!").queue();
					else {
						if (cache.getSusList().contains(jsonObject.getLong("Id")))
							cache.getSusList().remove(jsonObject.get("Id"));
						else
							event.getChannel().sendMessage("That user is not on the sus list!").queue();
					}
				} catch (Exception e) {
					e.printStackTrace();
					event.getChannel().sendMessage("Something went wrong! Please DM Burgboi.").queue();
				}
			} else {
				if (cache.getSusList().contains(userId)) {
					cache.getSusList().remove(userId);
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
							event.getChannel().sendMessage("Something went wrong! Please DM Burgboi.").queue();
						else {
							event.getChannel().sendMessage("Removed " + jsonObject.getString("name") + " from the sus list.");
						}
					} catch (Exception e) {
						e.printStackTrace();
						event.getChannel().sendMessage("Something went wrong! Please DM Burgboi.").queue();
					}
				} else
					event.getChannel().sendMessage("That user is not on the sus list!.").queue();
			}
		} else {
			cache.serialize();
			event.getChannel().sendMessage("Successfully removed profiles from the sus list.").queue();
			Main.jda.removeEventListener(this);
		}
	}
}
