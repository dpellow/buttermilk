package com.cryptoregistry.proto.compat;

import com.cryptoregistry.crypto.mt.SecureMessage;
import com.cryptoregistry.protos.Buttermilk.SecureMessageProto;

public class InputTypeAdapter {

	public static SecureMessageProto.InputTypeProto getInputTypeProtoFor(SecureMessage.InputType inputType) {
		switch(inputType) {
			case STRING: return SecureMessageProto.InputTypeProto.STRING;
			case BYTE_ARRAY: return SecureMessageProto.InputTypeProto.BYTE_ARRAY;
			case CHAR_ARRAY: return SecureMessageProto.InputTypeProto.CHAR_ARRAY;
			default: return null;
		}
	}
	
	public static SecureMessage.InputType getInputTypefor(SecureMessageProto.InputTypeProto proto){
		switch(proto) {
			case STRING: return SecureMessage.InputType.STRING;
			case BYTE_ARRAY: return SecureMessage.InputType.BYTE_ARRAY;
			case CHAR_ARRAY: return SecureMessage.InputType.CHAR_ARRAY;
		}
		return null;
	}

}
