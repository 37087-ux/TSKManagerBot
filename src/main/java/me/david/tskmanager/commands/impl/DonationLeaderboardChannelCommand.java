package me.david.tskmanager.commands.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.commands.SetterCommandModel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class DonationLeaderboardChannelCommand extends SetterCommandModel {

	public DonationLeaderboardChannelCommand() {
		super("donationleaderboardchannel|dboardchannel", "Sets or prints the donation leaderboard channel", "donationleaderboardchannel|dboardchannel (#channel)");
		setRankUse(true, false);
	}

	@Override
	public void getterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (cache.getDonationLeaderboardChannel() != null)
			event.getChannel().sendMessage("The donation leaderboard channel is " + cache.getDonationLeaderboardChannel().getAsMention()).queue();
		else
			event.getChannel().sendMessage("You have not set the donation leaderboard channel yet!").queue();
	}

	@Override
	public void setterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (event.getMessage().getMentionedChannels().size() == 1) {
			cache.setDonationLeaderboardChannel(event.getMessage().getMentionedChannels().get(0));
			cache.serialize();
			event.getChannel().sendMessage("Set " + cache.getDonationLeaderboardChannel().getAsMention() + " as the donation leaderboard channel.").queue();
		} else
			event.getChannel().sendMessage("Please mention a channel to set it as the donation leaderboard channel!").queue();
	}
}
