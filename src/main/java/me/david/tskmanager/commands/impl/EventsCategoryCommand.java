package me.david.tskmanager.commands.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.commands.SetterCommandModel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class EventsCategoryCommand extends SetterCommandModel {

	public EventsCategoryCommand() {
		super("eventscategory", "Sets or gets the events category", "eventscategory (category)");
		setHrOnly(true);
	}

	@Override
	public void getterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (cache.getEventsCategory() != null)
			event.getChannel().sendMessage("The events category is " + cache.getEventsCategory().getName()).queue();
		else
			event.getChannel().sendMessage("Please set the events category first").queue();
	}

	@Override
	public void setterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		String categoryName = String.join(" ", new ArrayList<String>() {
			{
				addAll(args);
				remove(0);
			}
		});
		if (!event.getGuild().getCategoriesByName(categoryName, true).isEmpty()) {
			cache.setEventsCategory(event.getGuild().getCategoriesByName(categoryName, true).get(0));
			cache.serialize();
		} else
			event.getChannel().sendMessage("That category does not exist!").queue();
	}
}
