/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.client.storage;

/**
 * <p>
 * Field references which can be used to limit the search criteria. For example, "signature"
 * set to true in a Criteria would mean only records which represent signatures will match. "signatureAlgorithm" set
 * to SignatureAlgorithms.RSA would limit the criteria to only records which are signatures but also only
 * the ones signed using RSA.  
 * </p>
 * 
 * <p>
 * registrationHandle is one of the most useful - it limits the criteria to all the records under a particular Registrant. 
 * </p>
 * 
 * 
 * @author Dave
 *
 */
public enum MetadataTokens {
	
	key, handle, forPublication, contact, signature,
	namedList, namedMap, keyGenerationAlgorithm,
	signatureAlgorithm, createdOn, registrationHandle,
	curveName, NTRUParamName, RSAKeySize;
	
}
