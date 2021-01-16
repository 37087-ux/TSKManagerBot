package me.david.tskmanager.commands.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.Main;
import me.david.tskmanager.commands.SetterCommandModel;
import me.david.tskmanager.commands.eventlisteners.impl.AddProfilesSusList;
import me.david.tskmanager.commands.eventlisteners.impl.RemoveProfilesSusList;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class SusListCommand extends SetterCommandModel {

	public SusListCommand() {
		super("suslist|slist", "Add or remove profiles from the sus list or return the profiles in the sus list.", "suslist|slist (add|remove)");
		setRankUse(true, false);
	}

	@Override
	public void getterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (!cache.getSusList().isEmpty()) {
			EmbedBuilder embedBuilder = new EmbedBuilder();
			embedBuilder.setTitle("Sus list");
			embedBuilder.setColor(Main.defaultEmbedColor);

			for (Long id : cache.getSusList()) {
				try {
					URL url = new URL("https://users.roblox.com/v1/users/" + id);
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

					if (jsonObject.has("errors")) {
						event.getChannel().sendMessage("Something went wrong! Please DM Burgboi.").queue();
					} else {
						embedBuilder.addField(jsonObject.getString("name"), "User id: " + id, true);
					}
				} catch (Exception e) {
					e.printStackTrace();
					event.getChannel().sendMessage("Something went wrong! Please DM Burgboi.").queue();
				}
			}

			event.getChannel().sendMessage(embedBuilder.build()).queue();
		} else
			event.getChannel().sendMessage("You have not added any profiles to the sus list!").queue();
	}

	@Override
	public void setterCommand(MessageReceivedEvent event, List<String> args) {
		if (args.get(1).equalsIgnoreCase("add")) {
			event.getChannel().sendMessage("Type roblox user usernames or user IDs to add them to the sus list.").queue();
			Main.jda.addEventListener(new AddProfilesSusList(event.getMember(), event.getChannel()));
		} else if (args.get(1).equalsIgnoreCase("remove")) {
			event.getChannel().sendMessage("Type roblox user usernames or user IDs to remove them from the sus list.").queue();
			Main.jda.addEventListener(new RemoveProfilesSusList(event.getMember(), event.getChannel()));
		}
	}
}
