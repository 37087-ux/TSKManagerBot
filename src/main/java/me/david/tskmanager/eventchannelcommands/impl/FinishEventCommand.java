package me.david.tskmanager.eventchannelcommands.impl;

import me.david.tskmanager.eventchannelcommands.EventCommandModel;
import me.david.tskmanager.eventlisteners.EventChannelsEventListener;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class FinishEventCommand extends EventCommandModel {

	public FinishEventCommand() {
		super("finish", "Finishes an event", "finish");
	}

	@Override
	public void onCommand(MessageReceivedEvent event, List<String> args, String eventMessageID) {
		EventChannelsEventListener.events.get(eventMessageID).getEventRole().delete().queue();
		EventChannelsEventListener.events.get(eventMessageID).getEventChannel().delete().queue();
		EventChannelsEventListener.events.remove(eventMessageID);
	}
}
