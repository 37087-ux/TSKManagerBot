package me.david.tskmanager.commands.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.commands.SetterCommandModel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class LrRoleCommand extends SetterCommandModel {

	public LrRoleCommand() {
		super("lrrole", "Sets or gets the LR role", "lrrole (@role)");
		setHrOnly(true);
	}

	@Override
	public void getterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (cache.getLrRole() != null)
			event.getChannel().sendMessage("The LR role is " + cache.getLrRole().getName()).queue();
		else
			event.getChannel().sendMessage("You have not set the LR role yet!").queue();
	}

	@Override
	public void setterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (event.getMessage().getMentionedRoles().size() == 1) {
			cache.setLrRole(event.getMessage().getMentionedRoles().get(0));
			event.getChannel().sendMessage("You have set the LR role to " + event.getMessage().getMentionedRoles().get(0).getName()).queue();
			cache.serialize();
		} else
			event.getChannel().sendMessage("Usage: " + cache.getPrefix() + getUsage()).queue();
	}
}
