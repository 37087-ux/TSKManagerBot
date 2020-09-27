package me.david.tskmanager.eventchannelcommands;

import me.david.tskmanager.EventData;
import me.david.tskmanager.GuildCache;
import me.david.tskmanager.commands.impl.HelpCommand;
import me.david.tskmanager.eventlisteners.EventChannelsEventListener;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class EventCommandModel extends ListenerAdapter {
	private List<String> names = new ArrayList<>();
	private String description;
	private String usage;

	public EventCommandModel(String name, String description, String usage) {
		this.names = Arrays.asList(name.split("\\|"));
		this.description = description;
		this.usage = usage;
		HelpCommand.eventCommands.add(this);
	}

	@Override
	public void onMessageReceived(@Nonnull MessageReceivedEvent event) {

		//check if the channel is a private channel
		if (event.getChannel() instanceof PrivateChannel) return;

		//get arguments
		List<String> args = new ArrayList<>() {
			{
				addAll(Arrays.asList(event.getMessage().getContentRaw().split(" ")));
			}
		};

		//check if the message starts with the bot's prefix and it is an actual command
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (args.get(0).startsWith(cache.getPrefix()) && names.contains(args.get(0).replaceFirst(cache.getPrefix(), "").toLowerCase())) {
			boolean isInEventChannel = false;
			boolean isOriginalEventCreator = false;
			for (Map.Entry entry : EventChannelsEventListener.events.entrySet()) {
				if (event.getChannel().equals(((EventData) entry.getValue()).getEventChannel()))
					isInEventChannel = true;
				if (event.getMember().equals(((EventData) entry.getValue()).getOriginalEventCreator()))
					isOriginalEventCreator = true;
			}
			if (isInEventChannel == true && isOriginalEventCreator == true) {
				int eventNumber = Integer.parseInt(String.valueOf(event.getChannel().getName().charAt(0)));
				String eventMessageID = null;
				for (Map.Entry entry : EventChannelsEventListener.events.entrySet()) {
					if (((EventData) entry.getValue()).getEventNumber() == eventNumber)
						eventMessageID = ((EventData) entry.getValue()).getEventMessageID();
				}
				if (eventMessageID != null)
					onCommand(event, args, eventMessageID);
			}
		}
	}

	//getters
	public String getNames() {
		return String.join("|", names);
	}

	public String getDescription() {
		return description;
	}

	public String getUsage() {
		return usage;
	}

	public abstract void onCommand(MessageReceivedEvent event, List<String> args, String eventMessageID);
}
