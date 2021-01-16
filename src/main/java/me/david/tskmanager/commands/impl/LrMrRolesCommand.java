package me.david.tskmanager.commands.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.Main;
import me.david.tskmanager.commands.SetterCommandModel;
import me.david.tskmanager.commands.eventlisteners.impl.AddLrRolesEventListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class LrMrRolesCommand extends SetterCommandModel {

	public LrMrRolesCommand() {
		super("lrmrroles|lrroles", "Sets roles as a lr/mr role", "lrmrroles|lrroles (add|a|remove|r) (@role)");
		setRankUse(true, false);
	}

	@Override
	public void getterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (!cache.getLRMRRoles().isEmpty()) {
			EmbedBuilder embedBuilder = new EmbedBuilder();
			embedBuilder.setTitle("LR Roles");
			embedBuilder.setColor(Main.defaultEmbedColor);
			for (Role role : cache.getLRMRRoles())
				embedBuilder.addField(role.getName(), "", true);
			event.getChannel().sendMessage(embedBuilder.build()).queue();
		} else
			event.getChannel().sendMessage("You have not set any ranks as a lr/mr role yet!").queue();
	}

	@Override
	public void setterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (args.get(1).startsWith("a")) {

			event.getChannel().sendMessage("Ping roles to set as a lr/mr role. Type finish to finish").queue();
			Main.jda.addEventListener(new AddLrRolesEventListener(event.getMember(), event.getChannel()));
		} else if (args.get(1).startsWith("r") && event.getMessage().getMentionedRoles().size() == 1) {

			int index = cache.getLRMRRoles().indexOf(event.getMessage().getMentionedRoles().get(0));
			cache.getLRMRRoles().remove(index);
			event.getChannel().sendMessage("Removed the role " + event.getMessage().getMentionedRoles().get(0).getName() + " from lr/mr roles").queue();
			cache.serialize();
		} else
			event.getChannel().sendMessage("Usage: " + cache.getPrefix() + getUsage()).queue();
	}
}
