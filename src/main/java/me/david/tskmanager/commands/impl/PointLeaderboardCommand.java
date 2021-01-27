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
		super("pointleaderboard|pboard", "Prints the point leaderboard", "pointleaderbord|pboard");
	}

	@Override
	public void onCommand(MessageReceivedEvent event, List<String> args) {
		List<Long> pointList = new ArrayList<>();
		Map<Long, String> pointToNameMap = new HashMap<>();
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());

		if (cache.getPointLeaderboard().isEmpty()) {
			event.getChannel().sendMessage("There is no one on the point leaderboard yet!").queue();
			return;
		}

		for (Map.Entry<String, PointLeaderboardData> entry : cache.getPointLeaderboard().entrySet()) {
			pointList.add(entry.getValue().getPoints());
			pointToNameMap.put(entry.getValue().getPoints(), entry.getValue().getMember().getEffectiveName());
		}
		Collections.sort(pointList, Collections.reverseOrder());
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setTitle("Point Leaderboard");
		embedBuilder.setColor(Main.defaultEmbedColor);
		for (int i = 0; i < 10 && i < pointList.size(); i++) {
			int rankNumber = i + 1;
			embedBuilder.addField(rankNumber + ". " + pointToNameMap.get(pointList.get(i)), "Points: " + pointList.get(i), false);
		}
		event.getChannel().sendMessage(embedBuilder.build()).queue();
	}
}