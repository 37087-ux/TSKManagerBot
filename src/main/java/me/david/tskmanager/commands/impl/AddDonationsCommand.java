package me.david.tskmanager.commands.impl;

import me.david.tskmanager.DonationLeaderboardData;
import me.david.tskmanager.GuildCache;
import me.david.tskmanager.Main;
import me.david.tskmanager.commands.CommandModel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;

public class AddDonationsCommand extends CommandModel {

	public AddDonationsCommand() {
		super("adddonation|adonate", "Adds a donation for a person to the leaderboard", "adddonation|adonate {@member} {credits}");
		setRankUse(true, false);
	}

	@Override
	public void onCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (args.size() == 3) {
			if (event.getMessage().getMentionedMembers().size() == 1) {
				try {
					long credits = Long.parseLong(args.get(2));

					if (cache.getDonationLeaderboard().get(event.getMessage().getMentionedMembers().get(0).getNickname()) != null)
						cache.getDonationLeaderboard().get(event.getMessage().getMentionedMembers().get(0).getNickname()).addCredits(credits);
					else {
						DonationLeaderboardData data = new DonationLeaderboardData(event.getMessage().getMentionedMembers().get(0), credits);
						cache.getDonationLeaderboard().put(event.getMessage().getMentionedMembers().get(0).getEffectiveName(), data);
					}

					cache.serialize();
					event.getChannel().sendMessage("Added " + credits + " credits for " + event.getMessage().getMentionedMembers().get(0).getEffectiveName()).queue();

					if (cache.getDonationLeaderboardChannel() == null)
						return;

					List<Long> donationList = new ArrayList<>();
					Map<Long, String> donationToNameMap = new HashMap<>();

					for (Map.Entry<String, DonationLeaderboardData> entry : cache.getDonationLeaderboard().entrySet()) {
						donationList.add(entry.getValue().getCredits());
						donationToNameMap.put(entry.getValue().getCredits(), entry.getValue().getMember().getEffectiveName());
					}

					Collections.sort(donationList, Collections.reverseOrder());
					EmbedBuilder embedBuilder = new EmbedBuilder();
					embedBuilder.setTitle("Donation Leaderboard");
					embedBuilder.setColor(Main.defaultEmbedColor);
					for (int i = 0; i < 10 && i < donationList.size(); i++) {
						int rankNumber = i + 1;
						embedBuilder.addField(rankNumber + ". " + donationToNameMap.get(donationList.get(i)), "Credits: " + donationList.get(i), false);
					}

					if (cache.getDonationLeaderboardMessageID() != null)
						cache.getDonationLeaderboardChannel().retrieveMessageById(cache.getDonationLeaderboardMessageID()).queue((message) -> {
							message.editMessage(embedBuilder.build()).queue();
						});
					else {
						cache.getDonationLeaderboardChannel().sendMessage(embedBuilder.build()).queue((message) -> {
							cache.setDonationLeaderboardMessageID(message.getId());
							cache.serialize();
						});
					}

				} catch (NumberFormatException e) {
					event.getChannel().sendMessage("Please provide the amount of credits you want to add to that user!").queue();
				}
			} else
				event.getChannel().sendMessage("Please provide a member to add credits to!").queue();
		} else
			event.getChannel().sendMessage("Usage: " + cache.getPrefix() + getUsage()).queue();
	}
}
