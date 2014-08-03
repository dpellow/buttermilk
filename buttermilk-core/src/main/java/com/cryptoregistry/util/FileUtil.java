/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.util;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {

	/**
	 * The Java 7 idiom
	 * 
	 * @param path
	 * @return
	 */
	public static String readFile(String path) {
		try {
			return readFile(path, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// http://stackoverflow.com/questions/326390/how-to-create-a-java-string-from-the-contents-of-a-file/326440#326440
	public static String readFile(String path, Charset encoding)
			throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}

	/**
	 * Read sequentially all the paths and return as one logical continuation.
	 * Note this is intended for relatively small files.
	 * 
	 * @param encoding
	 * @param paths
	 * @return
	 * @throws IOException
	 */
	public static String readFiles(Charset encoding, String... paths)
			throws IOException {

		StringWriter writer = new StringWriter();
		for (String path : paths) {
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			writer.write(encoding.decode(ByteBuffer.wrap(encoded)).toString());
		}

		return writer.toString();
	}

}
