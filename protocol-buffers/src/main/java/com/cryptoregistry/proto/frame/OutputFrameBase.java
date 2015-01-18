/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2015 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.frame;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Useful utility methods for writing to a stream
 * 
 * @author Dave
 *
 */
public class OutputFrameBase {

	public final void writeInt(OutputStream out, int v) throws IOException {
        out.write((v >>> 24) & 0xFF);
        out.write((v >>> 16) & 0xFF);
        out.write((v >>>  8) & 0xFF);
        out.write((v >>>  0) & 0xFF);
    }
	
	public final void writeShort(OutputStream out, int v) throws IOException {
		short s = (short) v;
        out.write((s >>>  8) & 0xFF);
        out.write((s >>>  0) & 0xFF);
    }
	
	public final void writeByte(OutputStream out, int v) throws IOException {
        out.write(v & 0xFF);
    }

}
