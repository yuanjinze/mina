/**
* yplatform-2016年8月15日
*/
package org.yplatform.ymina.util;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

/**
 * Finds currently available server ports
 * 
 * @author yuanjinze
 *
 */
public class AvailablePortFinder {

	/**
	 * The minimum number of server port number.
	 */
	public static final int MIN_PORT_NUMBER = 1;

	/**
	 * The maximum number of server port number.
	 */
	public static final int MAX_PORT_NUMBER = 49151;

	private AvailablePortFinder() {
	}

	/**
	 * Returns the {@link Set} of currently available port numbers.
	 * 
	 * 
	 * 
	 * WARNING: this can take a very long time.
	 * 
	 * @return
	 */
	public static Set getAvailablePorts() {
		return getAvailablePorts(MIN_PORT_NUMBER, MAX_PORT_NUMBER);
	}

	public static int getNextAvailable() {
		return getNextAvailable(MIN_PORT_NUMBER);
	}

	public static int getNextAvailable(int formPort) {
		if ((formPort < MIN_PORT_NUMBER) || (formPort > MAX_PORT_NUMBER)) {
			throw new IllegalArgumentException("Invalid start port: " + formPort);
		}
		for (int i = formPort; i <= MAX_PORT_NUMBER; i++) {
			ServerSocket ss = null;
			DatagramSocket ds = null;
			try {
				ss = new ServerSocket(i);
				ds = new DatagramSocket(i);
				return i;
			} catch (IOException e) {

			} finally {
				if (ds != null) {
					ds.close();
				}

				if (ss != null) {
					try {
						ss.close();
					} catch (IOException e) {
						// should not be thrown.
					}
				}
			}
		}

		throw new NoSuchElementException("Could not find an available port above " + formPort);
	}

	/**
	 * Checks to see if a specific port is available.
	 * 
	 * @param port
	 *            the port to check for availability.
	 * @return
	 */
	public static boolean available(int port) {
		if ((port < MIN_PORT_NUMBER) || (port > MAX_PORT_NUMBER)) {
			throw new IllegalArgumentException("Invalid start port: " + port);
		}
		ServerSocket s = null;
		try {
			s = new ServerSocket(port);
			return true;
		} catch (IOException e) {
			return false;
		} finally {
			if (s != null) {
				try {
					s.close();
				} catch (IOException e) {
					// should not be thrown.
				}
			}
		}

	}

	/**
	 * Returns the {@link Set} of currently avaliable port numbers (
	 * {@link Integer}) between the specified port range.
	 * 
	 * @throws IllegalArgumentException
	 *             if port range is not between {@link #MIN_PORT_NUMBER} and
	 *             {@link #MAX_PORT_NUMBER} or <code>formPort</code> if greater
	 *             than <code>toPort</code>.
	 * @param formPort
	 * @param toPort
	 * @return
	 */
	public static Set getAvailablePorts(int formPort, int toPort) {
		if ((formPort < MIN_PORT_NUMBER) || (toPort > MAX_PORT_NUMBER) || (formPort > toPort)) {
			throw new IllegalArgumentException("Invalid port range: " + formPort + " ~ " + toPort);
		}
		Set result = new TreeSet();
		for (int i = formPort; i <= toPort; i++) {
			ServerSocket s = null;
			try {
				s = new ServerSocket(i);
				result.add(new Integer(i));
			} catch (IOException e) {

			} finally {
				if (s != null) {
					try {
						s.close();
					} catch (IOException e) {
						// should not be thrown.
					}
				}
			}
		}
		return result;
	}

}
