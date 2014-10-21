package com.cryptoregistry.btls;

import java.io.FilterInputStream;
import java.io.InputStream;

import com.cryptoregistry.crypto.mt.Segment;

public class SegmentInputStream extends FilterInputStream {

	protected SegmentInputStream(InputStream in) {
		super(in);
	}

	public Segment readSegment(){
		
		return null;
	}

}
