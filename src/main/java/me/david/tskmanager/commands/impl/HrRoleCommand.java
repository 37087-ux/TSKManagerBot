package me.david.tskmanager.commands.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.commands.SetterCommandModel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class HrRoleCommand extends SetterCommandModel {
	public HrRoleCommand() {
		super("hrrole", "Sets the HR role", "hrrole (@role)");
		setRankUse(false, true);
	}

	@Override
	public void getterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (cache.getHrRole() != null)
			event.getChannel().sendMessage("The HR role is " + cache.getHrRole().getName()).queue();
		else
			event.getChannel().sendMessage("You have not set the HR role yet!").queue();
	}

	@Override
	public void setterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (event.getMessage().getMentionedRoles().size() == 1) {
			cache.setHrRole(event.getMessage().getMentionedRoles().get(0));
			cache.serialize();
			event.getChannel().sendMessage("You have set the HR role to " + cache.getHrRole().getName()).queue();
		} else
			event.getChannel().sendMessage("Please ping a role to set it as the HR role").queue();
	}
}
