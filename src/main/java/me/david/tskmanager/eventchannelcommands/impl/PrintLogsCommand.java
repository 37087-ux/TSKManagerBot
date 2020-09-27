package me.david.tskmanager.eventchannelcommands.impl;

import me.david.tskmanager.eventchannelcommands.EventCommandModel;
import me.david.tskmanager.eventlisteners.EventChannelsEventListener;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class PrintLogsCommand extends EventCommandModel {

	public PrintLogsCommand() {
		super("printlogs", "Prints the logs of who reacted/unreacted to an event message", "printlogs");
	}

	@Override
	public void onCommand(MessageReceivedEvent event, List<String> args, String eventMessageID) {
		event.getChannel().sendMessage(EventChannelsEventListener.events.get(eventMessageID).getLogs().toString()).queue();
	}
}
