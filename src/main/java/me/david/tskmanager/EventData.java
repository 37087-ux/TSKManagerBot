package me.david.tskmanager;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class EventData {

	private int eventNumber;
	private Role eventRole;
	private TextChannel eventChannel;

	public EventData(int eventNumber, Role eventRole, TextChannel eventChannel) {
		this.eventNumber = eventNumber;
		this.eventRole = eventRole;
		this.eventChannel = eventChannel;
	}

	public int getEventNumber() {
		return eventNumber;
	}

	public Role getEventRole() {
		return eventRole;
	}

	public TextChannel getEventChannel() {
		return eventChannel;
	}

}
