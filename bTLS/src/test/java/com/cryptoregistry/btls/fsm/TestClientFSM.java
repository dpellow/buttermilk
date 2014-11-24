package com.cryptoregistry.btls.fsm;

import org.junit.Test;

import com.cryptoregistry.btls.fsm.BTLSStateMachine.Trigger;

public class TestClientFSM {

	@Test
	public void test0() {
		BTLSStateMachine cFSM = new BTLSStateMachine(false);
		cFSM.getMachine().fire(Trigger.StartingClientSocket);
		System.err.println(cFSM.getMachine().getState());
	}

}
