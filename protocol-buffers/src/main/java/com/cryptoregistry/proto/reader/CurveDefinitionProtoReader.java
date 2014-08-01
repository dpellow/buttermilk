/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.reader;

import java.util.LinkedHashMap;
import java.util.Map;

import com.cryptoregistry.ec.ECCustomParameters;
import com.cryptoregistry.ec.ECF2MCustomParameters;
import com.cryptoregistry.ec.ECFPCustomParameters;
import com.cryptoregistry.protos.Buttermilk.CurveDefinitionProto;
import com.cryptoregistry.protos.Buttermilk.CurveDefinitionProto.ECFieldProto;
import com.cryptoregistry.protos.Buttermilk.MapProto;

public class CurveDefinitionProtoReader {

	final CurveDefinitionProto proto;
	
	public CurveDefinitionProtoReader(CurveDefinitionProto proto) {
		this.proto = proto;
	}
	
	public ECCustomParameters read() {
		MapProto mp = proto.getMap();
		String uuid = proto.getUuid();
		ECFieldProto fieldProto = proto.getField();
		MapProtoReader mpr = new MapProtoReader(mp);
		Map<String,String> map = mpr.read();
		if(fieldProto == ECFieldProto.FP) {
			return new ECFPCustomParameters(uuid,(LinkedHashMap<String,String>)map);
		}else{
			return new ECF2MCustomParameters(uuid,(LinkedHashMap<String,String>)map);
		}
	
		
	}
}
