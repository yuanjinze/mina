/**
* yplatform-2016年8月16日
*/
package org.yplatform.ymina.common;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents network transport types
 * 
 * <ul>
 * <li>{@link #SOCKET} -TCP/IP</li>
 * 
 * </ul>
 * <p>
 * You can also create your own transport type. Please refer to
 * {@linke #TransportType(String[] ,boolean)}.
 * 
 * @author yuanjinze
 *
 */
public class TransportType implements Serializable {

	private static final long serialVersionUID = 4944816561253765302L;

	private final String[] names;
	private final transient boolean connectionless;
	private static final Map name2type = new HashMap();

	/**
	 * Transport type:TCP/IP
	 */
	public static final TransportType SOCKET=
			new TransportType(new String[]{"SOCKET","TCP"},false);
	
	/**
	 * Transport type:UDP/IP
	 */
	public static final TransportType DATAGRAM=
			new TransportType(new String[]{"DATAGRAM","UDP"},true);
	
	/**
	 * Transport type: in-VM pipe
	 */
	public static final TransportType VM_PIPE=
			new TransportType(new String[]{"VM_PIPE"},false);
	/**
	 * Creates a new instance. New transport type is automatically registered to
	 * internal registry so that you can look it up using
	 * {@link #getInstance(String)}.
	 * 
	 * @param names
	 *            the name or aliases of this transport type.
	 * @param connectionless
	 *            <tt>true</tt> if and only if this transport type is
	 *            connectionless
	 * @throws IllegalArgumentException
	 *             if <tt>names</tt> are already registered or empty.
	 */
	public TransportType(String[] names, boolean connectionless) {
		if (names == null) {
			throw new NullPointerException("names");
		}
		if (names.length == 0) {
			throw new IllegalArgumentException("names is empty");
		}
		for (int i = 0; i < names.length; i++) {
			if (names[i] == null) {
				throw new NullPointerException("strVals[" + i + "]");
			}
			names[i] = names[i].toUpperCase();
		}
		register(names, this);
		this.names = names;
		this.connectionless = connectionless;
	}

	/**
	 * Returns the transport type of the specified name.
	 * All names are case-insensitive.
	 * @param name the name of the transport type
	 * @return the transport type
	 * @throws IllegalArgumentException if the specified name is not available.
	 */
	public static TransportType getInstance(String name) {
		TransportType type = (TransportType) name2type.get(name);
		if (type != null) {
			return type;
		}
		throw new IllegalArgumentException("Unknown transport type name: " + name);
	}

	/**
	 * Returns <code>true</code> if the session of this transport type is
	 * connectionless.
	 * @return
	 */
	public boolean isConnectionless() {
		return connectionless;
	}

	/**
	 * Returns the known names of this transport type.
	 * @return
	 */
	public Set getNames() {
		Set result = new TreeSet();
		for (int i = names.length - 1; i >= 0; i--) {
			result.add(names[i]);
		}
		return result;
	}

	public String toString() {
		return names[0];
	}

	private void register(String[] names2, TransportType type) {
		synchronized (name2type) {
			for (int i = names.length - 1; i >= 0; i--) {
				if (name2type.containsKey(names[i])) {
					throw new IllegalArgumentException("Transprot type name '" + names[i] + "' is already taken.");
				}
			}

			for (int i = names.length - 1; i >= 0; i--) {
				name2type.put(names[i], type);
			}
		}
	}

	private Object readResolve() throws ObjectStreamException {
		for (int i = names.length - 1; i >= 0; i--) {
			try {
				return getInstance(names[i]);
			} catch (IllegalArgumentException e) {
				// ignore
			}
		}
		throw new InvalidObjectException("Unknown transport type.");

	}

}
