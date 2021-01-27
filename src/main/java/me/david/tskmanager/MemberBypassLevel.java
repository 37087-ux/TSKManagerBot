package me.david.tskmanager;

import net.dv8tion.jda.api.entities.Member;

public class MemberBypassLevel {

	private Member member;
	private boolean hrBypass = false;
	private boolean shrBypass = false;

	public MemberBypassLevel(Member member, boolean bypassLevel) {
		this.member = member;
		if (bypassLevel)
			this.hrBypass = true;
		else
			this.shrBypass = true;
	}

	public Member getMember() {
		return member;
	}

	public boolean isHrBypass() {
		return hrBypass;
	}

	public boolean isShrBypass() {
		return shrBypass;
	}

	public boolean getMemberBypassType() {
		if (hrBypass)
			return true;
		else if (shrBypass)
			return false;
		else
			return false;
	}
}
