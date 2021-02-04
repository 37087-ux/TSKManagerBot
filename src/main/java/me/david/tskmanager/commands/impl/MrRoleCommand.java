package me.david.tskmanager.commands.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.commands.SetterCommandModel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class MrRoleCommand extends SetterCommandModel {

	public MrRoleCommand() {
		super("mrrole", "Sets or returns the MR role", "mrrole (@role)");
		setRankUse(true, false);
	}

	@Override
	public void getterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (cache.getMrRole() != null)
			event.getChannel().sendMessage("The MR role is " + cache.getMrRole().getName()).queue();
		else
			event.getChannel().sendMessage("You have not set the MR role yet!").queue();
	}

	@Override
	public void setterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (event.getMessage().getMentionedRoles().size() == 1) {
			cache.setMrRole(event.getMessage().getMentionedRoles().get(0));
			cache.serialize();
			event.getChannel().sendMessage("Set the MR role to " + cache.getMrRole().getName()).queue();
		} else
			event.getChannel().sendMessage("Please ping a role to set it as the MR role!").queue();
	}
}
