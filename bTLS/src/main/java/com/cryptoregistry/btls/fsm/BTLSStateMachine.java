package com.cryptoregistry.btls.fsm;

import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.delegates.Action;

public class BTLSStateMachine {

	protected StateMachineConfig<State, Trigger> config;
	protected StateMachine<State, Trigger> machine;
	
	public BTLSStateMachine(boolean server) {
			init(server);
	}
	
	protected void init(boolean isServer) {
		
		config = new StateMachineConfig<>();
		
		Action handshakeStartClock = new Action() {

			@Override
			public void doIt() {
				System.out.println("Handshake Start");
			}
			
		};
		
		Action handshakeStopClock = new Action() {

			@Override
			public void doIt() {
				System.out.println("Handshake End");
			}
			
		};
		
		config.configure(State.NotConnected).permit(Trigger.StartingClientSocket, State.Connected);
		
	

		
		if(isServer){
			machine = new StateMachine<>(State.Listening, config);
		}else{
			machine = new StateMachine<>(State.NotConnected, config);
		}
	}
	
	public enum Trigger{
		StartingClientSocket,
		CreatingServerSocket,
		HandshakeStarted, 
		HandshakeCompleted, 
		SendingMessage,
		ReceivingMessage,
		SendingAlert,
		ReceivingAlert,
		ErrorDetected,
		Bail;
	}
	
    public enum State {
	     NotConnected,
	     Listening,
	     Connected,
	     Handshake,
	     ApplicationProcessing,
	     Closing,
	     Alert;
    }

	public StateMachine<State, Trigger> getMachine() {
		return machine;
	}
    
}
