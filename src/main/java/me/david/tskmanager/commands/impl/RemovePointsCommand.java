package me.david.tskmanager.commands.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.commands.CommandModel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class RemovePointsCommand extends CommandModel {

	public RemovePointsCommand() {
		super("removepoints|rp", "Removes points from a member", "removepoints|rp {@member} {points}");
		setRankUse(true, false);
	}

	@Override
	public void onCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (args.size() == 3) {
			if (event.getMessage().getMentionedMembers().size() == 1) {
				try {
					long points = Long.parseLong(args.get(2));

					if (cache.getPointLeaderboard().get(event.getMessage().getMentionedMembers().get(0).getEffectiveName()) != null)
						cache.getPointLeaderboard().get(event.getMessage().getMentionedMembers().get(0).getEffectiveName()).removePoints(points);
					else {
						event.getChannel().sendMessage("You cannot remove points from someone with no points!").queue();
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
