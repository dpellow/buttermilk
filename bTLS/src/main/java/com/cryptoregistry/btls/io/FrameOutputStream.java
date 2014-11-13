package com.cryptoregistry.btls.io;

import java.io.FilterOutputStream;
import java.io.OutputStream;

import com.cryptoregistry.proto.frame.OutputFrame;

/**
 * Write an "Application" frame to the underlying stream for each normal "write" operation. Also
 * provides other write methods for supporting the protocol.
 * 
 * @author Dave
 *
 */
public class FrameOutputStream extends FilterOutputStream {

	public FrameOutputStream(OutputStream out) {
		super(out);
	}

	public void write(int b){
		
	}
	
	public void write(byte[]bytes){
		
	}
	
	public void write(byte[]bytes,int offset,int length){
		
	}
	
	public void writeOutputFrame(OutputFrame frame){
		
	}

}
