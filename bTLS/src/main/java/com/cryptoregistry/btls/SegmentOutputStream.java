package com.cryptoregistry.btls;

import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.util.List;

import com.cryptoregistry.crypto.mt.Segment;

public class SegmentOutputStream extends FilterOutputStream {

	public SegmentOutputStream(OutputStream out) {
		super(out);
	}
	
	public void writeSegment(Segment segment) {
		
	}
	
	public void writeSegments(List<Segment> segments) {
		
	}
	
	public void writeSegments(Segment [] segments) {
		
	}

}
