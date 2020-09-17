package me.david.tskmanager.commands.eventlisteners.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.Main;
import me.david.tskmanager.commands.eventlisteners.CommandListenerModel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AddDefaultJoinRolesEventListener extends CommandListenerModel {

	public AddDefaultJoinRolesEventListener(Member member, MessageChannel channel) {
		super(member, channel);
	}

	@Override
	public void onTextReceived(MessageReceivedEvent event) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());

		if (event.getMessage().getMentionedRoles().size() == 1 && !event.getMessage().getContentRaw().toLowerCase().equals("finish")) {

			cache.getDefaultJoinRoles().add(event.getMessage().getMentionedRoles().get(0));
			getChannel().sendMessage("Added the role " + event.getMessage().getMentionedRoles().get(0).getName() + ". Type finish to stop adding roles").queue();
		} else if (event.getMessage().getContentRaw().toLowerCase().equals("finish")) {
			cache.serialize();
			event.getChannel().sendMessage("Successfully added the roles").queue();
			Main.jda.removeEventListener(this);
		} else
			event.getChannel().sendMessage("That is not a role! Ping a role to add a role or type finish to finish").queue();

	}
}
