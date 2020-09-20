package me.david.tskmanager;

import me.david.tskmanager.commands.impl.*;
import me.david.tskmanager.eventlisteners.EventChannelsEventListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.awt.*;

public class Main {

	public static JDA jda;
	public static Color defaultEmbedColor = new Color(235, 183, 52);

	public static void main(String[] args) throws LoginException {
		JDABuilder jdaBuilder = JDABuilder.createDefault("");
		jdaBuilder.setStatus(OnlineStatus.ONLINE);
		jdaBuilder.setActivity(Activity.listening("commands"));
		jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
		jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS);
		addEventListeners(jdaBuilder);
		jda = jdaBuilder.build();
	}

	private static void addEventListeners(JDABuilder jdaBuilder) {
		jdaBuilder.addEventListeners(new HelpCommand(), new AddToFactionCommand(), new DefaultJoinRoles(), new EventChannels(), new HrRoleCommand(), new HrRolesCommand(), new LrRoleCommand(), new LrMrRolesCommand(), new PrefixCommand(),
				new PromoteCommand(), new RanksTrackCommand(), new EventChannelsEventListener(), new PrivateMessageCommand(), new EventsCategoryCommand());
	}
}
