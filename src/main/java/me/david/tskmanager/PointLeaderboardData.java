package me.david.tskmanager;

import net.dv8tion.jda.api.entities.Member;

public class PointLeaderboardData {

	private Member member;
	private Long points;

	public PointLeaderboardData(Member member, Long points) {
		this.member = member;
		this.points = points;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Long getPoints() {
		return points;
	}

	public void setPoints(Long points) {
		this.points = points;
	}

	public void addPoints(Long points) {
		this.points = this.points + points;
	}

	public void removePoints(Long points) {
		this.points = this.points - points;
		if (this.points < 0)
			this.points = 0L;
	}
}
