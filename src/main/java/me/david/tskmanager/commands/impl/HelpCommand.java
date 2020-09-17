package me.david.tskmanager.commands.impl;

import me.david.tskmanager.Main;
import me.david.tskmanager.commands.CommandModel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends CommandModel {

	public static List<CommandModel> commands = new ArrayList<>();

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

		event.getChannel().sendMessage(embedBuilder.build()).queue();
	}
}
