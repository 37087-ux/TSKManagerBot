package me.david.tskmanager.commands.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.Main;
import me.david.tskmanager.commands.SetterCommandModel;
import me.david.tskmanager.commands.eventlisteners.impl.AddMrRolesEventListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class MrRolesCommand extends SetterCommandModel {

	public MrRolesCommand() {
		super("mrroles", "Sets roles as MR roles or returns the MR roles", "mrroles (add|a|remove|r) (@role)");
		setRankUse(true, false);
	}

	@Override
	public void getterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (!cache.getMrRoles().isEmpty()) {
			EmbedBuilder embedBuilder = new EmbedBuilder();
			embedBuilder.setTitle("MR Roles");
			embedBuilder.setColor(Main.defaultEmbedColor);
			for (Role role : cache.getMrRoles())
				embedBuilder.addField(role.getName(), "", true);
			event.getChannel().sendMessage(embedBuilder.build()).queue();
		} else
			event.getChannel().sendMessage("You have not set any ranks as a MR role yet!").queue();
	}

	@Override
	public void setterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (args.get(1).startsWith("a")) {

			event.getChannel().sendMessage("Ping roles to set as a MR role. Type finish to finish").queue();
			Main.jda.addEventListener(new AddMrRolesEventListener(event.getMember(), event.getChannel()));
		} else if (args.get(1).startsWith("r") && event.getMessage().getMentionedRoles().size() == 1) {

			int index = cache.getMrRoles().indexOf(event.getMessage().getMentionedRoles().get(0));
			cache.getMrRoles().remove(index);
			event.getChannel().sendMessage("Removed the role " + event.getMessage().getMentionedRoles().get(0).getName() + " from mr roles").queue();
			cache.serialize();
		} else
			event.getChannel().sendMessage("Usage: " + cache.getPrefix() + getUsage()).queue();
	}
}
