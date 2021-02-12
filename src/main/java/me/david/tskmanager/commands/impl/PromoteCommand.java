package me.david.tskmanager.commands.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.PointLeaderboardData;
import me.david.tskmanager.commands.CommandModel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class PromoteCommand extends CommandModel {

	public PromoteCommand() {
		super("promote", "Promotes a faction member to the next rank", "promote {@user}");
		setRankUse(false, true);
	}

	@Override
	public void onCommand(MessageReceivedEvent event, List<String> args) {

		GuildCache cache = GuildCache.getCache(event.getGuild().getId());

		if (args.size() == 2 && event.getMessage().getMentionedMembers().size() == 1) {

			if (cache.getLrRole() == null || cache.getMrRole() == null || cache.getHrRole() == null || cache.getLRRoles().isEmpty() || cache.getMrRoles().isEmpty() || cache.getHRRoles().isEmpty()) {
				event.getChannel().sendMessage("You have not set the LR|MR|HR role yet or have not set any roles as either LR|MR|HR!").queue();
				return;
			}

			if (event.getMessage().getMentionedMembers().get(0).equals(event.getMessage().getMember())) {
				event.getChannel().sendMessage("You cannot promote yourself!").queue();
				return;
			}

			Role rank = null;
			Role selfRank = null;
			Member member = event.getMessage().getMentionedMembers().get(0);

			for (Role role : member.getRoles())
				if (cache.getRanksTrack().getRankTrack().contains(role))
					rank = role;

			for (Role role : event.getMember().getRoles())
				if (cache.getRanksTrack().getRankTrack().contains(role))
					selfRank = role;

			if (cache.getRanksTrack().getRankTrack().indexOf(selfRank) < cache.getRanksTrack().getRankTrack().indexOf(rank)) {
				event.getChannel().sendMessage("You cannot promote someone higher rank than you!").queue();
				return;
			}

			if (rank != null) {
				Role nextRank = cache.getRanksTrack().getNextRank(rank);
				if (nextRank == null) {
					event.getChannel().sendMessage(event.getMessage().getMentionedMembers().get(0).getEffectiveName() + " is the highest rank possible!").queue();
					return;
				}
				if (member.getRoles().contains(cache.getLrRole()) && cache.getMrRoles().contains(nextRank)) {
					event.getGuild().removeRoleFromMember(member, cache.getLrRole()).queue();
					event.getGuild().addRoleToMember(member, cache.getMrRole()).queue();
				} else if (member.getRoles().contains(cache.getMrRole()) && cache.getHRRoles().contains(nextRank)) {
					event.getGuild().removeRoleFromMember(member, cache.getMrRole()).queue();
					event.getGuild().addRoleToMember(member, cache.getHrRole()).queue();
				} else if (!member.getRoles().contains(cache.getLrRole()) && !member.getRoles().contains(cache.getHrRole()) && cache.getLRRoles().contains(nextRank))
					event.getGuild().addRoleToMember(member, cache.getLrRole()).queue();

				if (nextRank.equals(cache.getLeaderboardTransitionRole())) {
					long points = cache.getLrPointLeaderboard().get(member.getEffectiveName()).getPoints();
					cache.getLrPointLeaderboard().remove(member.getEffectiveName());
					cache.getMrHrPointLeaderboard().put(member.getEffectiveName(), new PointLeaderboardData(member, points));
				}

				event.getGuild().addRoleToMember(member, nextRank).queue();
				event.getGuild().removeRoleFromMember(member, rank).queue();
				event.getChannel().sendMessage("Successfully updated the roles for " + member.getEffectiveName()).queue();
			} else
				event.getChannel().sendMessage("That member does not have a rank role!").queue();
		} else
			event.getChannel().sendMessage("Usage: " + cache.getPrefix() + getUsage()).queue();
	}
}
