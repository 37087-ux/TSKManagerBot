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
		EmbedBuilder embedBuilder2 = new EmbedBuilder();
		EmbedBuilder embedBuilder3 = new EmbedBuilder();

		embedBuilder.setTitle("Universal Commands");
		embedBuilder.setFooter("legend: [] is optional; {} is required");
		embedBuilder.setColor(Main.defaultEmbedColor);
		for (CommandModel command : commands) {
			if (!(command.isHrOnly() || command.isShrOnly()))
				embedBuilder.addField(command.getNames(), command.getDescription() + "; Usage: " + command.getUsage(), false);
		}
		embedBuilder2.setTitle("HR Commands");
		embedBuilder2.setFooter("legend: [] is optional; {} is required");
		embedBuilder2.setColor(Main.defaultEmbedColor);
		for (CommandModel command : commands) {
			if (command.isHrOnly())
				embedBuilder2.addField(command.getNames(), command.getDescription() + "; Usage: " + command.getUsage(), false);
		}

		embedBuilder3.setTitle("SHR Commands");
		embedBuilder3.setFooter("legend: [] is optional; {} is required");
		embedBuilder3.setColor(Main.defaultEmbedColor);
		for (CommandModel command : commands) {
			if (command.isShrOnly())
				embedBuilder3.addField(command.getNames(), command.getDescription() + "; Usage: " + command.getUsage(), false);
		}
		/*embedBuilder.addField("Event commands", "--------------------------------------", false);
		for (EventCommandModel eventCommandModel : eventCommands)
			embedBuilder.addField(eventCommandModel.getNames(), eventCommandModel.getDescription() + "; Usage: " + eventCommandModel.getUsage(), false);
		 */

		event.getMember().getUser().openPrivateChannel().queue((channel) -> channel.sendMessage(embedBuilder.build()).queue());
		event.getMember().getUser().openPrivateChannel().queue((channel) -> channel.sendMessage(embedBuilder2.build()).queue());
		event.getMember().getUser().openPrivateChannel().queue((channel) -> channel.sendMessage(embedBuilder3.build()).queue());
		event.getChannel().sendMessage("Message sent in DMs.").queue();
	}
}
