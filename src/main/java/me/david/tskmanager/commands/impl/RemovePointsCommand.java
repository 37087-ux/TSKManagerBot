package me.david.tskmanager.commands.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.commands.CommandModel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class RemovePointsCommand extends CommandModel {

	public RemovePointsCommand() {
		super("removepoints|rp", "Removes points from a member", "removepoints|rp {@member} {points} {leaderboardType: hmr|lr}");
		setRankUse(true, false);
	}

	@Override
	public void onCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (args.size() == 4) {
			if (event.getMessage().getMentionedMembers().size() == 1) {

				if (cache.getHRRoles() == null || cache.getMrRole() == null || cache.getLrRole() == null) {
					event.getChannel().sendMessage("Please set the LR/MR/HR roles first!").queue();
					return;
				}

				try {
					long points = Long.parseLong(args.get(2));

					if (args.get(3).equalsIgnoreCase("hmr")) {
						if (!(event.getMessage().getMentionedMembers().get(0).getRoles().contains(cache.getHrRole()) || event.getMessage().getMentionedMembers().get(0).getRoles().contains(cache.getMrRole()))) {
							event.getChannel().sendMessage("You cannot remove points from someone that is not a MR/HR from the MR/HR point leaderboard!").queue();
							return;
						}
						if (cache.getMrHrPointLeaderboard().get(event.getMessage().getMentionedMembers().get(0).getEffectiveName()) != null)
							cache.getMrHrPointLeaderboard().get(event.getMessage().getMentionedMembers().get(0).getEffectiveName()).removePoints(points);
						else {
							event.getChannel().sendMessage("You cannot remove points from someone with no points!").queue();
							return;
						}
					} else if (args.get(3).equalsIgnoreCase("lr")) {
						if (!event.getMessage().getMentionedMembers().get(0).getRoles().contains(cache.getLrRole())) {
							event.getChannel().sendMessage("You cannot remove points from someone that is not a LR from the LR point leaderboard!").queue();
							return;
						}
						if (cache.getLrPointLeaderboard().get(event.getMessage().getMentionedMembers().get(0).getEffectiveName()) != null)
							cache.getLrPointLeaderboard().get(event.getMessage().getMentionedMembers().get(0).getEffectiveName()).removePoints(points);
						else {
							event.getChannel().sendMessage("You cannot remove points from someone with no points!").queue();
							return;
						}
					} else {
						event.getChannel().sendMessage("Please provide which leaderboard you want to add points for a member to!").queue();
						return;
					}

					cache.serialize();
					event.getChannel().sendMessage("removed " + points + " points from " + event.getMessage().getMentionedMembers().get(0).getEffectiveName()).queue();
				} catch (NumberFormatException e) {
					event.getChannel().sendMessage("Please provide the amount of points you want to remove from that user!").queue();
				}
			} else
				event.getChannel().sendMessage("Please provide a member to remove points from!").queue();
		} else
			event.getChannel().sendMessage("Usage: " + cache.getPrefix() + getUsage()).queue();
	}
}
