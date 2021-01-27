package me.david.tskmanager.commands.impl;

import me.david.tskmanager.GuildCache;
import me.david.tskmanager.MemberBypassLevel;
import me.david.tskmanager.commands.SetterCommandModel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class MemberBypassCommand extends SetterCommandModel {

	public MemberBypassCommand() {
		super("memberbypass|mbypass", "Adds/removes a member to the command permission bypass list or shows the member bypass list", "memberpass|mbypass (add|a|remove|r) (@member) (hr|shr)");
		setRankUse(false, true);
	}

	@Override
	public void getterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (!cache.getMemberBypassList().isEmpty()) {
			StringBuilder message = new StringBuilder();
			for (MemberBypassLevel memberBypass : cache.getMemberBypassList()) {
				String bypassType = (memberBypass.getMemberBypassType()) ? "HR" : "SHR";
				message.append("Member: " + memberBypass.getMember().getEffectiveName() + " Type: " + bypassType + "\n");
			}
			event.getChannel().sendMessage(message).queue();
		} else
			event.getChannel().sendMessage("You have not added anyone to the member bypass list yet!").queue();
	}

	@Override
	public void setterCommand(MessageReceivedEvent event, List<String> args) {
		GuildCache cache = GuildCache.getCache(event.getGuild().getId());
		if (args.size() == 4) {
			if (event.getMessage().getMentionedMembers().size() == 1) {

				if (event.getMessage().getMentionedMembers().get(0).equals(event.getMember())) {
					event.getChannel().sendMessage("You cannot add yourself to the member bypass list!").queue();
					return;
				}

				boolean bypassType;

				if (args.get(3).equalsIgnoreCase("hr"))
					bypassType = true;
				else if (args.get(3).equalsIgnoreCase("shr"))
					bypassType = false;
				else {
					event.getChannel().sendMessage("Please specify if you want to allow the member to bypass hr commands or shr commands!").queue();
					return;
				}

				if (args.get(1).equalsIgnoreCase("add|a") || args.get(1).equalsIgnoreCase("a")) {

					for (MemberBypassLevel bypassLevel : cache.getMemberBypassList()) {
						if (bypassLevel.getMember().equals(event.getMessage().getMentionedMembers().get(0))) {
							cache.getMemberBypassList().remove(bypassLevel);
							cache.getMemberBypassList().add(new MemberBypassLevel(event.getMessage().getMentionedMembers().get(0), bypassType));
							cache.serialize();
							event.getChannel().sendMessage("Gave " + event.getMessage().getMentionedMembers().get(0).getEffectiveName() + " permission to bypass command perms for " + args.get(3) + " commands").queue();
							return;
						}
					}

					cache.getMemberBypassList().add(new MemberBypassLevel(event.getMessage().getMentionedMembers().get(0), bypassType));
					cache.serialize();
					event.getChannel().sendMessage("Gave " + event.getMessage().getMentionedMembers().get(0).getEffectiveName() + " permission to bypass command perms for " + args.get(3) + " commands").queue();
				} else if (args.get(1).equalsIgnoreCase("remove") || args.get(1).equalsIgnoreCase("r")) {

					for (MemberBypassLevel bypassLevel : cache.getMemberBypassList()) {
						if (bypassLevel.getMember().equals(event.getMessage().getMentionedMembers().get(0))) {

							cache.getMemberBypassList().remove(bypassLevel);
							cache.serialize();
							event.getChannel().sendMessage("Removed the member bypass for " + event.getMessage().getMentionedMembers().get(0).getEffectiveName()).queue();
							return;
						} else
							event.getChannel().sendMessage("That user is not in the member bypass list!").queue();
					}
				} else
					event.getChannel().sendMessage("Please specify if you want to add or remove someone from the member bypass list!").queue();
			} else
				event.getChannel().sendMessage("Please ping a member to add/remove them from the member bypass list!").queue();
		} else
			event.getChannel().sendMessage("Usage: " + cache.getPrefix() + getUsage()).queue();
	}
}
