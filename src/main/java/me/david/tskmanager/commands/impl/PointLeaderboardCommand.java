package me.david.tskmanager.commands.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.Main;
import me.david.tskmanager.PointLeaderboardData;
import me.david.tskmanager.commands.CommandModel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;

public class PointLeaderboardCommand extends CommandModel {

	public PointLeaderboardCommand() {
		super("pointleaderboard|pboard", "Prints the point leaderboard", "pointleaderbord|pboard {leaderboardType: hmr|lr}");
	}

	@Override
	public void onCommand(MessageReceivedEvent event, List<String> args) {
		Map<String, Long> unsortedMap = new HashMap<>();
		Map<String, Long> pointToNameMap = new LinkedHashMap<>();
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());

		if (args.size() != 2) {
			event.getChannel().sendMessage("Usage: " + cache.getPrefix() + getUsage()).queue();
			return;
		}

		Map<String, PointLeaderboardData> pointLeaderboard;
		if (args.get(1).equalsIgnoreCase("hmr"))
			pointLeaderboard = cache.getMrHrPointLeaderboard();
		else if (args.get(1).equalsIgnoreCase("lr"))
			pointLeaderboard = cache.getLrPointLeaderboard();
		else {
			event.getChannel().sendMessage("Please provide which leaderboard you want to add points for a member to!").queue();
			return;
		}

		if (pointLeaderboard.isEmpty()) {
			event.getChannel().sendMessage("There is no one on the point leaderboard yet!").queue();
			return;
		}

		for (Map.Entry<String, PointLeaderboardData> entry : pointLeaderboard.entrySet())
			unsortedMap.put(entry.getValue().getMember().getEffectiveName(), entry.getValue().getPoints());

		unsortedMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x -> pointToNameMap.put(x.getKey(), x.getValue()));
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setTitle("Point Leaderboard");
		embedBuilder.setColor(Main.defaultEmbedColor);
		System.out.println(pointToNameMap);
		int rankNumber = 1;
		for (Map.Entry<String, Long> entry : pointToNameMap.entrySet()) {
			embedBuilder.addField(rankNumber + ". " + entry.getKey(), "Points: " + entry.getValue(), false);
			if (rankNumber == 10)
				break;
			rankNumber++;
		}
		event.getChannel().sendMessage(embedBuilder.build()).queue();
	}
}
