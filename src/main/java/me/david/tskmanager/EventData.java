package me.david.tskmanager;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class EventData {

	private int eventNumber;
	private Role eventRole;
	private TextChannel eventChannel;
	private Member originalEventCreator;
	private String eventMessageID;
	private StringBuilder logs = new StringBuilder();

	public EventData(int eventNumber, Role eventRole, TextChannel eventChannel, Member originalEventCreator, String eventMessageID) {
		this.eventNumber = eventNumber;
		this.eventRole = eventRole;
		this.eventChannel = eventChannel;
		this.originalEventCreator = originalEventCreator;
		this.eventMessageID = eventMessageID;
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

	public Member getOriginalEventCreator() {
		return originalEventCreator;
	}

	public String getEventMessageID() {
		return eventMessageID;
	}

	public StringBuilder getLogs() {
		return logs;
	}
}
