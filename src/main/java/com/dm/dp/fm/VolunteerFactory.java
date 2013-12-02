package com.dm.dp.fm;


public class VolunteerFactory implements IFactory {

	@Override
	public LeiFeng createLeiFeng() {
		return new Volunteer();
	}

}
