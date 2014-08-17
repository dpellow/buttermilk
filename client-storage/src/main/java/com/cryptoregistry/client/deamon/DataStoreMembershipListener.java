package com.cryptoregistry.client.deamon;

import org.jgroups.Address;
import org.jgroups.MembershipListener;
import org.jgroups.View;

public class DataStoreMembershipListener implements MembershipListener {

	public DataStoreMembershipListener() {
	}

	@Override
	public void viewAccepted(View new_view) {
		System.err.println("View accepted: "+new_view.getMembers());

	}

	@Override
	public void suspect(Address suspected_mbr) {
		System.err.println("Member suspected: "+suspected_mbr);

	}

	@Override
	public void block() {
		// TODO Auto-generated method stub

	}

	@Override
	public void unblock() {
		// TODO Auto-generated method stub

	}

}
