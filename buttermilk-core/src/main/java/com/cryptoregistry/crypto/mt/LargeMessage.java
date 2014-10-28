package com.cryptoregistry.crypto.mt;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The concept is to break up a larger message into roughly equal pieces and encrypt these pieces separately on different
 * threads. They are then sent over the socket and reconstructed into a whole (decrypted, etc) on the other side. 
 * 
 * The definition of "Large" is actually rather precise - a "Large" message is one we can process faster with 
 * multiple threads than we can with one thread. The exact size of such a message is machine dependent and best
 * determined by experimentation.
 * 
 * @author Dave
 *
 */
public class LargeMessage {
	
	private final int CORE_COUNT = Runtime.getRuntime().availableProcessors(); // not static so we can adjust cores dynamically, e.g. with VMWare

	private static final SecureRandom rand = new SecureRandom();
	
	final LargeMessageHeader header;
	final List<Segment> segments;
	
	// assume UTF-8
	public LargeMessage(String str) {
		segments = new ArrayList<Segment>();
		createSegments(CORE_COUNT,str.getBytes(StandardCharsets.UTF_8));
		byte [] ivBytes = new byte[16];
		rand.nextBytes(ivBytes);
		header = new LargeMessageHeader(InputType.STRING,StandardCharsets.UTF_8,ivBytes);
	}
	
	public LargeMessage(String str, Charset charset) {
		segments = new ArrayList<Segment>();
		createSegments(CORE_COUNT,str.getBytes(charset));
		byte [] ivBytes = new byte[16];
		rand.nextBytes(ivBytes);
		header = new LargeMessageHeader(InputType.STRING,charset,ivBytes);
	}
	
	public LargeMessage(char [] input) {
		segments = new ArrayList<Segment>();
		CharBuffer charBuffer = CharBuffer.wrap(input);
		ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
		byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
		createSegments(CORE_COUNT,bytes);
		byte [] ivBytes = new byte[16];
		rand.nextBytes(ivBytes);
		header = new LargeMessageHeader(InputType.CHAR_ARRAY,null,ivBytes);
	}
	
	public LargeMessage(byte [] input) {
		segments = new ArrayList<Segment>();
		createSegments(CORE_COUNT,input);
		byte [] ivBytes = new byte[16];
		rand.nextBytes(ivBytes);
		header = new LargeMessageHeader(InputType.BYTE_ARRAY,null,ivBytes);
	}
	
	public LargeMessage(LargeMessageHeader header, List<Segment> segments) {
		this.segments = segments;
		this.header = header;
	}
	
	public int count() {
		return segments.size();
	}
	
	enum InputType {
		STRING,CHAR_ARRAY,BYTE_ARRAY;
	}
	
	/**
	 * Create a list of Segments from a byte array
	 * 
	 * @param count
	 * @param totalBytes
	 * @return
	 */
	private void createSegments(int count, byte[]totalBytes){
		int bufSize = totalBytes.length/count;
		int remainder = totalBytes.length - (bufSize *count);
		
		for(int i = 0;i<count;i++){
			byte [] b = new byte[bufSize];
			System.arraycopy(totalBytes, i*bufSize, b, 0, bufSize);
			segments.add(new Segment(b));
		}
		
		// do the last bits if required
		if(remainder > 0){
			byte [] remainderBytes = new byte[remainder];
			System.arraycopy(totalBytes, totalBytes.length-remainder, remainderBytes, 0, remainderBytes.length);
			segments.add(new Segment(remainderBytes));
		}
	}

	public List<Segment> getSegments() {
		return segments;
	}

	public LargeMessageHeader getHeader() {
		return header;
	}
	
	public void rotate() {
		for(Segment s: segments){
			s.rotate();
		}
	}
	
	public int totalSizeOutput() {
		int size = 0;
		for(Segment s: segments){
			size+=s.getOutput().length;
		}
		return size;
	}
	
	/**
	 * Build the segments into a String based on the info in the LargeMessageHeader
	 * @return
	 */
	public String stringResult() {
		if(this.header.type != InputType.STRING) throw new RuntimeException("Input was a String");
		
		StringBuilder builder = new StringBuilder();
		for(Segment s: this.segments){
			builder.append(new String(s.getOutput(),this.header.charset));
		}
		return builder.toString();
	}
	
	/**
	 * Build the segments into a byte array based on the info in the LargeMessageHeader
	 * @return
	 */
	public byte [] byteResult() {
		if(this.header.type != InputType.BYTE_ARRAY) throw new RuntimeException("Input was a byte array");
		byte [] sz = new byte[this.totalSizeOutput()];
		int c = 0;
		for(Segment s: this.segments){
			System.arraycopy(s.getOutput(), 0, sz, c, s.getOutput().length);
			c+=s.getOutput().length;
		}
		
		return sz;
		
	}
	
}
