/**
* yplatform-2016年8月15日
*/
package org.yplatform.ymina.util;

import java.io.IOException;

/**
 * @author yuanjinze
 *
 */
public class ExceptionUtil {
	private ExceptionUtil() {
	}

	public static void throwException(Throwable t) throws IOException {
		if (t instanceof IOException) {
			throw (IOException) t;
		} else if (t instanceof RuntimeException) {
			throw (RuntimeException) t;
		} else if (t instanceof Error) {
			throw (Error) t;
		} else {
			throw new RuntimeException(t);
		}
	}
}
