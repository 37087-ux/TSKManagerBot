package me.david.tskmanager.commands.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.PointLeaderboardData;
import me.david.tskmanager.commands.CommandModel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class GivePointsCommand extends CommandModel {

	public GivePointsCommand() {
		super("givepoints|gp", "Gives points to a member", "givepoints|gp {@member} {points} {leaderboardType: hmr|lr}");
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
						if (cache.getMrHrPointLeaderboard().get(event.getMessage().getMentionedMembers().get(0).getEffectiveName()) != null)
							cache.getMrHrPointLeaderboard().get(event.getMessage().getMentionedMembers().get(0).getEffectiveName()).addPoints(points);
						else {
							PointLeaderboardData data = new PointLeaderboardData(event.getMessage().getMentionedMembers().get(0), points);
							cache.getMrHrPointLeaderboard().put(event.getMessage().getMentionedMembers().get(0).getEffectiveName(), data);
						}
					} else if (args.get(3).equalsIgnoreCase("lr")) {
						if (cache.getLrPointLeaderboard().get(event.getMessage().getMentionedMembers().get(0).getEffectiveName()) != null)
							cache.getLrPointLeaderboard().get(event.getMessage().getMentionedMembers().get(0).getEffectiveName()).addPoints(points);
						else {
							PointLeaderboardData data = new PointLeaderboardData(event.getMessage().getMentionedMembers().get(0), points);
							cache.getLrPointLeaderboard().put(event.getMessage().getMentionedMembers().get(0).getEffectiveName(), data);
						}
					} else {
						event.getChannel().sendMessage("Please provide which leaderboard you want to add points for a member to!").queue();
						return;
					}

					cache.serialize();
					event.getChannel().sendMessage("Added " + points + " points to " + event.getMessage().getMentionedMembers().get(0).getEffectiveName()).queue();
				} catch (NumberFormatException e) {
					event.getChannel().sendMessage("Please provide the amount of points you want to add to that user!").queue();
				}
			} else
				event.getChannel().sendMessage("Please provide a member to add points to!").queue();
		} else
			event.getChannel().sendMessage("Usage: " + cache.getPrefix() + getUsage()).queue();
	}

}
