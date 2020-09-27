package me.david.tskmanager.eventchannelcommands.impl;

import me.david.tskmanager.EventData;
import me.david.tskmanager.eventchannelcommands.EventCommandModel;
import me.david.tskmanager.eventlisteners.EventChannelsEventListener;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class RenameEventCommand extends EventCommandModel {

	public RenameEventCommand() {
		super("renameevent|re", "Renames the event channel", "re {name}");
	}

	@Override
	public void onCommand(MessageReceivedEvent event, List<String> args, String eventMessageID) {
		if (args.size() > 1) {
			EventData eventData = EventChannelsEventListener.events.get(eventMessageID);
			String name = String.join("-", new ArrayList<String>() {
				{
					addAll(args);
					remove(0);
				}
			});
			eventData.getEventChannel().getManager().setName(eventData.getEventNumber() + "-" + name).queue();
			eventData.getEventRole().getManager().setName(eventData.getEventNumber() + "-" + name).queue();
		} else
			event.getChannel().sendMessage("Please specify the name you want to set the event channel to!").queue();
	}
}
