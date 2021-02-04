package me.david.tskmanager.commands.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.commands.CommandModel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class GetPointsCommand extends CommandModel {

	public GetPointsCommand() {
		super("getpoints|getp", "Gets the amount of points you have", "getpoints|getp");
	}

	@Override
	public void onCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (cache.getMrHrPointLeaderboard().containsKey(event.getMember().getEffectiveName()) || cache.getLrPointLeaderboard().containsKey(event.getMember().getEffectiveName())) {
			if (event.getMember().getRoles().contains(cache.getLrRole()))
				event.getChannel().sendMessage("You have " + cache.getLrPointLeaderboard().get(event.getMember().getEffectiveName()).getPoints() + " points.").queue();
			else if (event.getMember().getRoles().contains(cache.getHrRole()) || event.getMember().getRoles().contains(cache.getMrRole()))
				event.getChannel().sendMessage("You have " + cache.getMrHrPointLeaderboard().get(event.getMember().getEffectiveName()).getPoints() + " points.").queue();
		} else
			event.getChannel().sendMessage("You do not have any points yet!").queue();
	}
}
