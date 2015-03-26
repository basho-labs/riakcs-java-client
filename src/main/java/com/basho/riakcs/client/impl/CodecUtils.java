/*
 * Copyright (c) 2012 Basho Technologies, Inc. All Rights Reserved. This file is provided to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain
 * a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.basho.riakcs.client.impl;

import java.lang.reflect.*;

public class CodecUtils {
	public static String encodeBase64(byte[] data) throws Exception {
		// Try loading Sun's Base64 encoder implementation
		try {
			Class<?> b64Class = ClassLoader.getSystemClassLoader().loadClass("sun.misc.BASE64Encoder");
			if (b64Class != null) {
				Method encodeMethod = b64Class.getMethod("encode", new Class[] {
					byte[].class
				});
				return (String) encodeMethod.invoke(b64Class.newInstance(), new Object[] {
					data
				});
			}
		} catch (ClassNotFoundException cnfe) {
		}

		// Try loading the Apache Commons Base64 encoder implementation
		try {
			Class<?> b64Class = ClassLoader.getSystemClassLoader().loadClass("org.apache.commons.codec.binary.Base64");
			if (b64Class != null) {
				Method encodeMethod = b64Class.getMethod("encodeBase64", new Class[] {
					byte[].class
				});
				byte[] encodedData = (byte[]) encodeMethod.invoke(b64Class, new Object[] {
					data
				});
				return new String(encodedData, "UTF-8");
			}
		} catch (ClassNotFoundException cnfe) {
		}

		throw new ClassNotFoundException(
				"Cannot find a recognized Base64 decoder implementation, please include Apache Commons Codec library in your classpath.");
	}

	public static byte[] decodeBase64(String data) throws Exception {
		// Try loading Sun's Base64 decoder implementation
		try {
			Class<?> b64Class = ClassLoader.getSystemClassLoader().loadClass("sun.misc.BASE64Decoder");
			if (b64Class != null) {
				Method decodeMethod = b64Class.getMethod("decodeBuffer", new Class[] {
					String.class
				});
				return (byte[]) decodeMethod.invoke(b64Class.newInstance(), new Object[] {
					data
				});
			}
		} catch (ClassNotFoundException cnfe) {
		}

		// Try loading the Apache Commons Base64 decoder implementation
		try {
			Class<?> b64Class = ClassLoader.getSystemClassLoader().loadClass("org.apache.commons.codec.binary.Base64");
			if (b64Class != null) {
				Method decodeMethod = b64Class.getMethod("decodeBase64", new Class[] {
					byte[].class
				});
				return (byte[]) decodeMethod.invoke(b64Class, new Object[] {
					data.getBytes("UTF-8")
				});
			}
		} catch (ClassNotFoundException cnfe) {
		}

		throw new ClassNotFoundException(
				"Cannot find a recognized Base64 decoder implementation, please include Apache Commons Codec library in your classpath.");
	}

}
