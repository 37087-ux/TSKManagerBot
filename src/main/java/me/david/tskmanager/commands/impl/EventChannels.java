package me.david.tskmanager.commands.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.Main;
import me.david.tskmanager.commands.SetterCommandModel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class EventChannels extends SetterCommandModel {

	public EventChannels() {
		super("eventchannels", "Sets a channel as an event channel or gets event channels", "eventchannels (add|a|remove|r) (#channel)");
		setRankUse(true, false);
	}

	@Override
	public void getterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());

		if (!cache.getEventChannels().isEmpty()) {
			EmbedBuilder embedBuilder = new EmbedBuilder();
			embedBuilder.setTitle("Event Channels");
			embedBuilder.setColor(Main.defaultEmbedColor);
			for (MessageChannel channel : cache.getEventChannels())
				embedBuilder.addField(channel.getName(), "", false);
			event.getChannel().sendMessage(embedBuilder.build()).queue();
		} else
			event.getChannel().sendMessage("You have not set any channels as an event channel yet!").queue();
	}

	@Override
	public void setterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());

		if (event.getMessage().getMentionedChannels().size() == 1 && args.get(1).startsWith("a")) {
			cache.getEventChannels().add(event.getMessage().getMentionedChannels().get(0));
			cache.serialize();
			event.getChannel().sendMessage("Set the channel " + event.getMessage().getMentionedChannels().get(0).getAsMention() + " as an event channel").queue();
		} else if (event.getMessage().getMentionedChannels().size() == 1 && args.get(1).startsWith("r")) {
			int index = cache.getEventChannels().indexOf(event.getMessage().getMentionedChannels().get(0));
			cache.getEventChannels().remove(index);
			cache.serialize();
			event.getChannel().sendMessage("Unset the channel " + event.getMessage().getMentionedChannels().get(0).getAsMention() + " as an event channel").queue();
		} else
			event.getChannel().sendMessage("Please mention a channel to set it as an event channel").queue();
	}
}
