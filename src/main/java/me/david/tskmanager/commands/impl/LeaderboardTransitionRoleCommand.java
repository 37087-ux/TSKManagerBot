package me.david.tskmanager.commands.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.commands.SetterCommandModel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class LeaderboardTransitionRoleCommand extends SetterCommandModel {

	public LeaderboardTransitionRoleCommand() {
		super("leaderboardtransitionrole|ltr", "Sets or gets the leaderboard transition role", "leaderboardtransitionrole|ltr (@role)");
		setRankUse(true, false);
	}

	@Override
	public void getterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (cache.getLeaderboardTransitionRole() != null)
			event.getChannel().sendMessage("The leaderboard transition role is " + cache.getLeaderboardTransitionRole().getName()).queue();
		else
			event.getChannel().sendMessage("You have not set the leaderboard transition role yet!").queue();
	}

	@Override
	public void setterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (event.getMessage().getMentionedRoles().size() == 1) {
			cache.setLeaderboardTransitionRole(event.getMessage().getMentionedRoles().get(0));
			cache.serialize();
			event.getChannel().sendMessage("Set the leaderboard transition role as " + cache.getLeaderboardTransitionRole().getName()).queue();
		} else
			event.getChannel().sendMessage("Please ping a role to set it as the leaderboard transition role!").queue();
	}
}
