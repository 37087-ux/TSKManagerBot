package me.david.tskmanager.commands.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.commands.SetterCommandModel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class AttendingEventRoleCommand extends SetterCommandModel {

	public AttendingEventRoleCommand() {
		super("attendingeventrole", "Sets the attending event role", "attendingeventrole (@role)");
		setHrOnly(true);
	}

	@Override
	public void getterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (cache.getAttendingEventRole() != null)
			event.getChannel().sendMessage("The attending event role is " + cache.getAttendingEventRole().getName()).queue();
		else
			event.getChannel().sendMessage("You have not set the attending event role yet!").queue();
	}

	@Override
	public void setterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (event.getMessage().getMentionedRoles().size() == 1) {
			cache.setAttendingEventRole(event.getMessage().getMentionedRoles().get(0));
			cache.serialize();
			event.getChannel().sendMessage("You have set the attending event role to " + cache.getAttendingEventRole().getName()).queue();
		} else
			event.getChannel().sendMessage("Please ping a role to set it as the attending event role").queue();
	}
}
