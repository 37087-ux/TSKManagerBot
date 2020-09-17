package me.david.tskmanager.commands;

import me.david.tskmanager.GuildCache;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public abstract class SetterCommandModel extends CommandModel {
	public SetterCommandModel(String name, String description, String usage) {
		super(name, description, usage);
	}

	@Override
	public void onCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = new GuildCache(event.getGuild().getId());
		//check if the person wants to set or get the value
		if (args.size() == 1) {
			getterCommand(event, args);
		} else if (args.size() > 1) {
			setterCommand(event, args);
		} else
			event.getChannel().sendMessage("Usage: " + cache.getPrefix() + getUsage()).queue();
	}

	public abstract void getterCommand(MessageReceivedEvent event, List<String> args);

	public abstract void setterCommand(MessageReceivedEvent event, List<String> args);
}
