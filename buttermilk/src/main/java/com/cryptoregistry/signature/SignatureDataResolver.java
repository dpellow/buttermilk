package com.cryptoregistry.signature;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface SignatureDataResolver {
	
	public void resolve(String ref, ByteArrayOutputStream collector) throws RefNotFoundException ;
	
	public void resolve(List<String> refs, ByteArrayOutputStream collector) throws RefNotFoundException ;
}
