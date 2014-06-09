package com.cryptoregistry.signature;

import java.io.IOException;
import java.io.Writer;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

public interface SignatureData {
	public void formatJSON(JsonGenerator g, Writer writer) throws JsonGenerationException, IOException ;
}
