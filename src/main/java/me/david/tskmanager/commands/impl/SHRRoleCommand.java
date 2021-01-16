package me.david.tskmanager.commands.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.commands.SetterCommandModel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class SHRRoleCommand extends SetterCommandModel {

	public SHRRoleCommand() {
		super("superhighrank|shr", "Sets or returns the super high rank role.", "superhighrank|shr [@role]");
		setRankUse(false, true);
	}

	@Override
	public void getterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (cache.getShrRole() != null) {
			event.getChannel().sendMessage("The SHR role is " + cache.getShrRole().getName() + ".").queue();
		} else
			event.getChannel().sendMessage("You have not set the SHR role yet!").queue();
	}

	@Override
	public void setterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (event.getMessage().getMentionedRoles().size() == 1) {
			cache.setShrRole(event.getMessage().getMentionedRoles().get(0));
			cache.serialize();
			event.getChannel().sendMessage("You have set the HR role to " + cache.getShrRole().getName()).queue();
		} else
			event.getChannel().sendMessage("Please ping a role to set it as the SHR role").queue();
	}
}
