package me.david.tskmanager.commands.impl;

import me.david.tskmanager.commands.CommandModel;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class PrivateMessageCommand extends CommandModel {

	public PrivateMessageCommand() {
		super("privatemessage|pm", "Private messages a user without having to go to their user", "privatemessage|pm {@user} {message...}");
	}

	@Override
	public void onCommand(MessageReceivedEvent event, List<String> args) {
		if (args.size() > 2 && event.getMessage().getMentionedMembers().size() == 1) {
			event.getMessage().delete().queue();
			PrivateChannel channel = event.getMessage().getMentionedMembers().get(0).getUser().openPrivateChannel().complete();
			String message = String.join(" ", new ArrayList<String>() {
				{
					addAll(args);
					removeRange(0, 2);
				}
			});
			channel.sendMessage("from: **" + event.getAuthor().getName() + "**\n**----------------------------------------------------------------------**\n" + message).queue();
		}
	}
}
