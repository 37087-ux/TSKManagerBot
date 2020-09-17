package me.david.tskmanager.commands.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.Main;
import me.david.tskmanager.commands.SetterCommandModel;
import me.david.tskmanager.commands.eventlisteners.impl.AddRanksTrackEventListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class RanksTrackCommand extends SetterCommandModel {

	public RanksTrackCommand() {
		super("rankstrack", "Creates a rank track", "rankstrack (add|a|insert|i|remove|r) (@role&index) (index)");
		setHrOnly(true);
	}

	@Override
	public void getterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (!cache.getRanksTrack().getRankTrack().isEmpty()) {
			EmbedBuilder embedBuilder = new EmbedBuilder();
			embedBuilder.setTitle("Ranks track");
			embedBuilder.setColor(Main.defaultEmbedColor);
			for (Role role : cache.getRanksTrack().getRankTrack()) {
				if (cache.getRanksTrack().getRankTrack().indexOf(role) != cache.getRanksTrack().getRankTrack().size() - 1)
					embedBuilder.addField(role.getName(), "\\\\/", false);
				else
					embedBuilder.addField(role.getName(), "", false);
			}
			event.getChannel().sendMessage(embedBuilder.build()).queue();
		} else
			event.getChannel().sendMessage("You have not added any ranks to the rank track yet!").queue();
	}

	@Override
	public void setterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (args.get(1).startsWith("a")) {

			event.getChannel().sendMessage("Ping roles to add to the rank track. Type finish to finish").queue();
			Main.jda.addEventListener(new AddRanksTrackEventListener(event.getMember(), event.getChannel()));
		} else if (args.get(1).startsWith("i") && event.getMessage().getMentionedRoles().size() == 1) {
			cache.getRanksTrack().insertRank(event.getMessage().getMentionedRoles().get(0), Integer.parseInt(args.get(3)));
			cache.serialize();
		} else if (args.get(1).startsWith("r")) {
			cache.getRanksTrack().removeRank(Integer.parseInt(args.get(2)));
			cache.serialize();
		} else
			event.getChannel().sendMessage("Usage: " + cache.getPrefix() + getUsage()).queue();
	}
}
