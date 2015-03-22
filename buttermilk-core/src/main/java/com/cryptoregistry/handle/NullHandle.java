package com.cryptoregistry.handle;

public class NullHandle implements Handle {

	private static final long serialVersionUID = 1L;

	private NullHandle() {
		
	}
	
	public static final NullHandle UNKNOWN = new NullHandle();

	@Override
	public int length() {
		return 0;
	}

	@Override
	public char charAt(int index) {
		return 0;
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return null;
	}

	@Override
	public int count() {
		return 0;
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public String[] handleParts() {
		return null;
	}

}
