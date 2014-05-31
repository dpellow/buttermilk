package com.cryptoregistry.signature;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.Assert;

import net.iharder.Base64;

import org.junit.Test;

import x.org.bouncycastle.util.Arrays;

public class DefaultResolverTest {
	
	 public static final String modulus = "AJEov3X7BBys0bfEzsq5lIz-6ZIOho71d5fJ6cM915zFgk"+
	 "KtEY4hOgQDSbW5aRKtw8RQESzh05J978GjpkQawwqSpqv2H88pK1yRqU8JoaVMKNEYw6IjpkbVg2LCsHmP7X"+
	 "OHvbtV3U7w2_aqDj_E65TOdm2B7wOec2JE0pvh0ogItI0UTMCel3d_ddXBq-d5UUHOJnG-HClcal-IW5xkxxK"+
	 "bfbDbagQ00FrmQ8_LtsV4LjTHZqa79K2zguKI3EHOfHeKJ3SZ3978JsSG6CUHJZLDm9DkeGfOO2POef7-ai7eo"+
	 "V0uqH_arjeH5pXupSy7ea1du4KvOouynrKNJcMBqeM=";
     public static final String publicExponent = "EQ==";

     /**
      * Extract the values from the given folder
      * @throws IOException
      */
	@Test
	public void test0() throws IOException {
		DefaultResolver resolver = new DefaultResolver("./src/test/resources/com/cryptoregistry/signature");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			resolver.resolve("ee80ca18-676d-495c-9c4c-0862dac74989:Modulus", baos);
			resolver.resolve("ee80ca18-676d-495c-9c4c-0862dac74989:PublicExponent", baos);
			byte [] bytes = baos.toByteArray();
			
			baos = new ByteArrayOutputStream();
			baos.write(Base64.decode(modulus, Base64.URL_SAFE));
			baos.write(Base64.decode(publicExponent, Base64.URL_SAFE));
			
			Assert.assertTrue(Arrays.areEqual(bytes, baos.toByteArray()));
			
		} catch (RefNotFoundException e) {
			e.printStackTrace();
		}
	}

}
