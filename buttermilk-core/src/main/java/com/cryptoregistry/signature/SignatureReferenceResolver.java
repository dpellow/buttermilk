/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.signature;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Interface for classes that resolve the data in signatures so signatures can be validated
 * @author Dave
 *
 */
public interface SignatureReferenceResolver {
	
	public void resolve(String ref, ByteArrayOutputStream collector) throws RefNotFoundException ;
	
	public void resolve(List<String> refs, ByteArrayOutputStream collector) throws RefNotFoundException ;
}
