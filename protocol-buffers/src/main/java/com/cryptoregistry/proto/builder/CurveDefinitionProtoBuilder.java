/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.builder;

import java.util.Iterator;
import java.util.Map;

import com.cryptoregistry.ec.ECCustomParameters;
import com.cryptoregistry.protos.Buttermilk.CurveDefinitionProto;
import com.cryptoregistry.protos.Buttermilk.CurveDefinitionProto.ECFieldProto;
import com.cryptoregistry.protos.Buttermilk.EntryProto;
import com.cryptoregistry.protos.Buttermilk.MapProto;

public class CurveDefinitionProtoBuilder {

	final ECCustomParameters params;
	
	public CurveDefinitionProtoBuilder(ECCustomParameters params) {
		this.params = params;
	}
	
	public CurveDefinitionProto build() {
		ECCustomParameters.FIELD field = params.field;
		Map<String,String> map = params.parameters;
		String uuid = params.uuid;
		
		MapProto.Builder mapProtoBuilder = MapProto.newBuilder();
		Iterator<String> iter = map.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			String value = map.get(key);
			EntryProto entry = EntryProto.newBuilder().setKey(key).setValue(value).build();
			mapProtoBuilder.addEntries(entry);
		}
		
		ECFieldProto fieldProto = null;
		if(field.equals(ECCustomParameters.FIELD.FP)){
			fieldProto = ECFieldProto.FP;
		}else{
			fieldProto = ECFieldProto.F2M;
		}
		
		CurveDefinitionProto proto = CurveDefinitionProto.newBuilder()
				.setUuid(uuid)
				.setMap(mapProtoBuilder.build())
				.setField(fieldProto)
				.build();
		
		return proto;
	}

}
