package me.david.tskmanager.eventlisteners;

import me.david.tskmanager.EventData;
import me.david.tskmanager.GuildCache;
import me.david.tskmanager.Main;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.*;

public class EventChannelsEventListener extends ListenerAdapter {

	private String emoteID;
	public static Map<String, EventData> events = new HashMap<>();

	@Override
	public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
		if (event.isFromGuild()) {
			GuildCache cache = GuildCache.getCache(event.getGuild().getId());
			if (event.getMessage().getContentRaw().toLowerCase().contains("event:") && cache.getEventChannels().contains(event.getChannel())) {
				if (cache.getEventsCategory() != null) {
					if (emoteID.isEmpty())
						emoteID = event.getGuild().getEmotesByName("tsk_logo", true).get(0).getId();
					event.getMessage().addReaction(event.getGuild().getEmoteById(emoteID)).queue();
					List<Integer> takenEventNumbers = new ArrayList<>();
					for (Map.Entry entry : events.entrySet())
						takenEventNumbers.add(((EventData) entry.getValue()).getEventNumber());
					Collections.sort(takenEventNumbers);
					int eventNumber = takenEventNumbers.get(takenEventNumbers.size() - 1);
					Role eventRole = event.getGuild().createRole().setName("event" + eventNumber).setHoisted(false).setMentionable(false).complete();
					TextChannel eventChannel = event.getGuild().createTextChannel("event" + eventNumber).setParent(cache.getEventsCategory()).addPermissionOverride(eventRole, EnumSet.of(Permission.VIEW_CHANNEL), null)
							.addPermissionOverride(eventRole.getGuild().getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL)).complete();
					events.put(event.getMessageId(), new EventData(eventNumber, eventRole, eventChannel));
				} else
					event.getChannel().sendMessage("Please set the events category first").queue();
			}
		}

	}

	@Override
	public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
		if (event.getReactionEmote().getEmote().equals(event.getGuild().getEmoteById(emoteID)) && !Main.jda.getSelfUser().equals(event.getMember().getUser()) && events.containsKey(event.getMessageId())) {
			event.getGuild().addRoleToMember(event.getMember(), events.get(event.getMessageId()).getEventRole()).queue();
		}
	}

	@Override
	public void onMessageReactionRemove(@Nonnull MessageReactionRemoveEvent event) {
		if (event.getReactionEmote().getEmote().equals(event.getGuild().getEmoteById(emoteID)) && events.containsKey(event.getMessageId())) {
			event.getGuild().removeRoleFromMember(event.getUserId(), events.get(event.getMessageId()).getEventRole()).queue();
		}
	}

	@Override
	public void onMessageUpdate(@Nonnull MessageUpdateEvent event) {
		if (event.getMessage().getContentRaw().startsWith("[FINISHED]")) {
			events.get(event.getMessageId()).getEventRole().delete().queue();
			events.get(event.getMessageId()).getEventChannel().delete().queue();
			events.remove(event.getMessageId());
		}
	}
}
