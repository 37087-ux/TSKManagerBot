package me.david.tskmanager.commands.impl;

import me.david.tskmanager.Main;
import me.david.tskmanager.commands.CommandModel;
import me.david.tskmanager.eventchannelcommands.EventCommandModel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends CommandModel {

	public static List<CommandModel> commands = new ArrayList<>();
	public static List<EventCommandModel> eventCommands = new ArrayList<>();

	public HelpCommand() {
		super("help", "Shows all the commands", "help");
	}

	@Override
	public void onCommand(MessageReceivedEvent event, List<String> args) {

		//create the embed
		EmbedBuilder embedBuilder = new EmbedBuilder();

		embedBuilder.setTitle("Commands");
		embedBuilder.setFooter("legend: [] is optional; {} is required");
		embedBuilder.setColor(Main.defaultEmbedColor);
		embedBuilder.addField("Universal commands", "--------------------------------------", false);
		for (CommandModel command : commands) {
			if (!command.isHrOnly())
				embedBuilder.addField(command.getNames(), command.getDescription() + "; Usage: " + command.getUsage(), false);
		}
		embedBuilder.addField("HR commands", "--------------------------------------", false);
		for (CommandModel command : commands) {
			if (command.isHrOnly())
				embedBuilder.addField(command.getNames(), command.getDescription() + "; Usage: " + command.getUsage(), false);
		}

		embedBuilder.addField("SHR commands", "--------------------------------------", false);
		for (CommandModel command : commands) {
			if (command.isShrOnly())
				embedBuilder.addField(command.getNames(), command.getDescription() + "; Usage: " + command.getUsage(), false);
		}
		/*embedBuilder.addField("Event commands", "--------------------------------------", false);
		for (EventCommandModel eventCommandModel : eventCommands)
			embedBuilder.addField(eventCommandModel.getNames(), eventCommandModel.getDescription() + "; Usage: " + eventCommandModel.getUsage(), false);
		 */

		event.getMember().getUser().openPrivateChannel().queue((channel) -> channel.sendMessage(embedBuilder.build()).queue());
		event.getChannel().sendMessage("Message sent in DMs.").queue();
	}
}
