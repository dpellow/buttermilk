package com.cryptoregistry.btls.io;

public interface AlertListener {

	public void alertReceived(AlertEvent evt);
}
