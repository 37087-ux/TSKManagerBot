package me.david.tskmanager.commands.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.Main;
import me.david.tskmanager.commands.SetterCommandModel;
import me.david.tskmanager.commands.eventlisteners.impl.AddDefaultJoinRolesEventListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class DefaultJoinRoles extends SetterCommandModel {

	public DefaultJoinRoles() {
		super("defaultjoinroles", "Adds or gets the default join roles", "defaultjoinroles (add|a|remove|r) (@RoleToRemove)");
		setHrOnly(true);
	}

	@Override
	public void getterCommand(MessageReceivedEvent event, List<String> args) {

		GuildCache cache = GuildCache.getCache(event.getGuild().getId());

		if (cache.getDefaultJoinRoles().size() > 0) {
			EmbedBuilder embedBuilder = new EmbedBuilder();
			embedBuilder.setTitle("Default join roles");
			embedBuilder.setColor(Main.defaultEmbedColor);
			for (Role role : cache.getDefaultJoinRoles())
				embedBuilder.addField(role.getName(), "", true);
			event.getChannel().sendMessage(embedBuilder.build()).queue();
		} else
			event.getChannel().sendMessage("You have not added any default join roles yet!").queue();
	}

	@Override
	public void setterCommand(MessageReceivedEvent event, List<String> args) {

		GuildCache cache = GuildCache.getCache(event.getGuild().getId());

		if (args.get(1).startsWith("a")) {
			event.getChannel().sendMessage("Type the roles you want to add. Type finish to finish").queue();
			Main.jda.addEventListener(new AddDefaultJoinRolesEventListener(event.getMember(), event.getChannel()));
		} else if (args.get(1).startsWith("r")) {

			if (event.getMessage().getMentionedRoles().size() == 1) {
				cache.getDefaultJoinRoles().remove(event.getMessage().getMentionedRoles().get(0));
				event.getChannel().sendMessage("Removed the role " + event.getMessage().getMentionedRoles().get(0).getAsMention() + " from default join roles").queue();
				cache.serialize();
			}
		}
	}
}
