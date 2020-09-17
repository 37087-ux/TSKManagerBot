package me.david.tskmanager.eventlisteners;

import me.david.tskmanager.GuildCache;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class EventChannelsEventListener extends ListenerAdapter {

	private List<String> trackedMessages = new ArrayList<>();
	private String emoteID;

	@Override
	public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
		if (event.isFromGuild()) {
			GuildCache cache = GuildCache.getCache(event.getGuild().getId());
			if (event.getMessage().getContentRaw().toLowerCase().contains("event:") && cache.getEventChannels().contains(event.getChannel())) {
				emoteID = event.getGuild().getEmotesByName("tsk_logo", true).get(0).getId();
				event.getMessage().addReaction(event.getGuild().getEmoteById(emoteID)).queue();
				trackedMessages.add(event.getMessageId());
			}
		}
	}

	@Override
	public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
		if (trackedMessages.contains(event.getMessageId()) && event.getReactionEmote().getEmote().equals(event.getGuild().getEmoteById(emoteID))) {
			GuildCache cache = GuildCache.getCache(event.getGuild().getId());
			event.getGuild().addRoleToMember(event.getMember(), cache.getAttendingEventRole()).queue();
		}
	}

	@Override
	public void onMessageReactionRemove(@Nonnull MessageReactionRemoveEvent event) {
		if (trackedMessages.contains(event.getMessageId()) && event.getReactionEmote().getEmote().equals(event.getGuild().getEmoteById(emoteID))) {
			GuildCache cache = GuildCache.getCache(event.getGuild().getId());
			event.getGuild().removeRoleFromMember(event.getMember(), cache.getAttendingEventRole()).queue();
		}
	}

	@Override
	public void onMessageUpdate(@Nonnull MessageUpdateEvent event) {
		if (trackedMessages.contains(event.getMessageId()) && event.getMessage().getContentRaw().startsWith("[FINISHED]")) {
			GuildCache cache = GuildCache.getCache(event.getGuild().getId());
			List<Member> members = new ArrayList<>();
			members.addAll(event.getGuild().getMembersWithRoles(cache.getAttendingEventRole()));
			for (Member member : members)
				event.getGuild().removeRoleFromMember(member, cache.getAttendingEventRole()).queue();
			trackedMessages.remove(event.getMessageId());
		}
	}
}
