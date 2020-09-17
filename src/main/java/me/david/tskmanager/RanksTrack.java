package me.david.tskmanager;

import net.dv8tion.jda.api.entities.Role;

import java.util.ArrayList;
import java.util.List;

public class RanksTrack {

	List<Role> rankTrack = new ArrayList<>();

	public List<Role> getRankTrack() {
		return rankTrack;
	}

	public boolean addRank(Role role) {
		return rankTrack.add(role);
	}

	public void insertRank(Role role, int index) {
		rankTrack.add(index, role);
	}

	public Role removeRank(int index) {
		return rankTrack.remove(index);
	}

	public Role getNextRank(Role role) {
		int index = rankTrack.indexOf(role);
		index++;
		if (rankTrack.size() > index)
			return rankTrack.get(index);
		else
			return null;
	}
}
