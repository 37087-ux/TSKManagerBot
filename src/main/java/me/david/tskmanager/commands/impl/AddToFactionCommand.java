package me.david.tskmanager.commands.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.commands.CommandModel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class AddToFactionCommand extends CommandModel {

	public AddToFactionCommand() {
		super("addtofaction|atf", "Gives a user the roles for them to see core faction channels", "addtofaction|atf {@user}");
		setRankUse(true, false);
	}

	@Override
	public void onCommand(MessageReceivedEvent event, List<String> args) {
		if (args.size() == 2 && event.getMessage().getMentionedMembers().size() == 1) {
			GuildCache cache = GuildCache.getCache(event.getGuild().getId());
			for (Role role : cache.getDefaultJoinRoles())
				event.getGuild().addRoleToMember(event.getMessage().getMentionedMembers().get(0), role).queue();
			event.getChannel().sendMessage("Successfully updated the roles for " + event.getMessage().getMentionedMembers().get(0).getNickname()).queue();
		}
	}
}
