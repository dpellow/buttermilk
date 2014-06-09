package com.cryptoregistry.formats;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cryptoregistry.signature.CryptoSignature;
import com.cryptoregistry.util.TimeUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

class SignatureFormatter {

	private List<CryptoSignature> list;
	
	public SignatureFormatter() {
		list = new ArrayList<CryptoSignature>();
	}

	public SignatureFormatter(List<CryptoSignature> list) {
		super();
		this.list = list;
	}
	
	public void add(CryptoSignature sig){
		list.add(sig);
	}
	
	public void format(JsonGenerator g, Writer writer) throws JsonGenerationException, IOException{
		for(CryptoSignature s: list){
			g.writeObjectFieldStart(s.getHandle());
			g.writeStringField("CreatedOn", TimeUtil.format(s.getCreatedOn()));
			g.writeStringField("SignedWith", s.getSignedWith());
			g.writeStringField("SignedBy", s.getSignedBy());
			g.writeStringField("SignatureAlgorithm", s.getSigAlg().toString());
			
			s.getSignatureData().formatJSON(g, writer);
			
			g.writeObjectFieldStart("DataReferences");
			Iterator<Integer> iter = s.getDataRefs().keySet().iterator();
			while(iter.hasNext()){
				Integer key = iter.next();
				String value = s.getDataRefs().get(key);
				g.writeStringField(String.valueOf(key), value);
			}
			g.writeEndObject();
			g.writeEndObject();
		}
	}

}
