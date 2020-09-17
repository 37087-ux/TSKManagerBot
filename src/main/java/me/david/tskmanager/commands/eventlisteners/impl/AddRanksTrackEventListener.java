package me.david.tskmanager.commands.eventlisteners.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.Main;
import me.david.tskmanager.commands.eventlisteners.CommandListenerModel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AddRanksTrackEventListener extends CommandListenerModel {

	public AddRanksTrackEventListener(Member member, MessageChannel channel) {
		super(member, channel);
	}

	@Override
	public void onTextReceived(MessageReceivedEvent event) {

		GuildCache cache = GuildCache.getCache(event.getGuild().getId());

		if (!event.getMessage().getContentRaw().equals("finish") && event.getMessage().getMentionedRoles().size() == 1) {

			cache.getRanksTrack().addRank(event.getMessage().getMentionedRoles().get(0));
			event.getChannel().sendMessage("Added the rank " + event.getMessage().getMentionedRoles().get(0).getName() + " to the rank track. Type finish to finish").queue();
		} else if (event.getMessage().getContentRaw().equals("finish")) {

			cache.serialize();
			event.getChannel().sendMessage("Successfully added the ranks").queue();
			Main.jda.removeEventListener(this);
		} else
			event.getChannel().sendMessage("That is not a valid role! Ping a role or type finish to finish").queue();
	}
}
