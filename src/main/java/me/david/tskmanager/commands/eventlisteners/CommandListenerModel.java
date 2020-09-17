package me.david.tskmanager.commands.eventlisteners;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public abstract class CommandListenerModel extends ListenerAdapter {

	Member member;
	MessageChannel channel;

	public CommandListenerModel(Member member, MessageChannel channel) {
		this.member = member;
		this.channel = channel;
	}

	@Override
	public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
		if (event.getMember().equals(member) && event.getChannel().equals(channel))
			onTextReceived(event);
	}

	public abstract void onTextReceived(MessageReceivedEvent event);

	public Member getMember() {
		return member;
	}

	public MessageChannel getChannel() {
		return channel;
	}
}
