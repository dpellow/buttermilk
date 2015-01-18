/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.reader;

import x.org.bouncycastle.math.ec.ECPoint;

import com.cryptoregistry.ec.ECCustomParameters;
import com.cryptoregistry.ec.ECKeyForPublication;
import com.cryptoregistry.ec.ECKeyMetadata;
import com.cryptoregistry.formats.FormatUtil;
import com.cryptoregistry.protos.Buttermilk.CurveDefinitionProto;
import com.cryptoregistry.protos.Buttermilk.ECKeyForPublicationProto;


public class ECKeyForPublicationProtoReader {

	final ECKeyForPublicationProto proto;

	public ECKeyForPublicationProtoReader(ECKeyForPublicationProto proto) {
		super();
		this.proto = proto;
	}

	public ECKeyForPublication read() {
		
		// we have either a curve name or curve parameters
		String curveName = null;
		ECCustomParameters params = null;
		ECKeyMetadata meta = (ECKeyMetadata) new KeyMetadataProtoReader(proto.getMeta()).read();
		if(proto.hasCurveName()) {
			curveName = proto.getCurveName();
		}else{
			CurveDefinitionProto cdp = proto.getCurveDefinition();
			CurveDefinitionProtoReader reader = new CurveDefinitionProtoReader(cdp);
			params = reader.read();
		}
		
		// Q
		String _Q = proto.getQ();
		ECPoint Q = null;
		if(curveName != null) {
			Q = FormatUtil.parseECPoint(curveName, meta.format.encodingHint, _Q);
		}else{
			Q = FormatUtil.parseECPoint(params.getParameters().getCurve(), meta.format.encodingHint, _Q);
		}
		
		
		if(curveName != null) {
			return new ECKeyForPublication(meta,Q,curveName);
		}else{
			return new ECKeyForPublication(meta,Q,params);
		}
	}
}
