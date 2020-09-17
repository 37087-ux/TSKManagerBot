package me.david.tskmanager.commands.eventlisteners.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.Main;
import me.david.tskmanager.commands.eventlisteners.CommandListenerModel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AddHrRolesEventListener extends CommandListenerModel {
	public AddHrRolesEventListener(Member member, MessageChannel channel) {
		super(member, channel);
	}

	@Override
	public void onTextReceived(MessageReceivedEvent event) {

		GuildCache cache = GuildCache.getCache(event.getGuild().getId());

		if (!event.getMessage().getContentRaw().equals("finish") && event.getMessage().getMentionedRoles().size() == 1) {

			cache.getHRRoles().add(event.getMessage().getMentionedRoles().get(0));
			event.getChannel().sendMessage("Set the role " + event.getMessage().getMentionedRoles().get(0).getName() + " as a hr role. Type finish to finish").queue();
		} else if (event.getMessage().getContentRaw().equals("finish")) {

			cache.serialize();
			event.getChannel().sendMessage("Successfully set the roles as a hr role").queue();
			Main.jda.removeEventListener(this);
		} else
			event.getChannel().sendMessage("That is not a role! Ping a role or type finish to finish").queue();
	}
}
