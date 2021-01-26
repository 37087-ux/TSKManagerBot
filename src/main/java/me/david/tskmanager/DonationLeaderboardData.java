package me.david.tskmanager;

import net.dv8tion.jda.api.entities.Member;

public class DonationLeaderboardData {

	private Member member;
	private Long credits;

	public DonationLeaderboardData(Member member, Long credits) {
		this.member = member;
		this.credits = credits;
	}

	public Member getMember() {
		return member;
	}

	public Long getCredits() {
		return credits;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public void setCredits(Long credits) {
		this.credits = credits;
	}

	public void addCredits(Long credits) {
		this.credits = this.credits + credits;
	}

	public void removeCredits(Long credits) {
		this.credits = this.credits - credits;
		if (this.credits < 0)
			this.credits = 0L;
	}
}
