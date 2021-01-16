package me.david.tskmanager.exceptions;

public class TwoRankUseTypeException extends Exception {
	public TwoRankUseTypeException() {
		super("You can only set a command as HR only or SHR only!");
	}
}
