package net.yoedtos.docman.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


public class FormatConverter {

	private FormatConverter() {
		
	}
	
	public static InputStream byteToInputStream(byte[] bytes) {
		return new ByteArrayInputStream(bytes);
	}
}
