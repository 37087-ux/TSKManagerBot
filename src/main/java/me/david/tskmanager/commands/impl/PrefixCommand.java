package me.david.tskmanager.commands.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.commands.SetterCommandModel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class PrefixCommand extends SetterCommandModel {

	public PrefixCommand() {
		super("prefix", "Sets or gets the prefix for the server", "prefix (prefix)");
		setHrOnly(true);
	}

	private final List<String> blockedPrefixes = Arrays.asList("#", "$", "@", "\\", "(", ")", "|");

	@Override
	public void getterCommand(MessageReceivedEvent event, List<String> args) {

		GuildCache cache = GuildCache.getCache(event.getGuild().getId());

		if (cache.getPrefix() != null)
			event.getChannel().sendMessage("The prefix is " + cache.getPrefix()).queue();
		else
			event.getChannel().sendMessage("You have not set a prefix yet!").queue();
	}

	@Override
	public void setterCommand(MessageReceivedEvent event, List<String> args) {

		GuildCache cache = GuildCache.getCache(event.getGuild().getId());

		if (!blockedPrefixes.contains(args.get(1))) {
			cache.setPrefix(args.get(1));
			event.getChannel().sendMessage("Set the prefix to " + cache.getPrefix()).queue();
			cache.serialize();
		} else
			event.getChannel().sendMessage("That prefix is blocked!").queue();
	}
}
