/**
*
*/
package org.yplatform.ymina.registry;

import java.io.Serializable;
import java.net.SocketAddress;

/**
 * 表示一个服务。
 * 
 * <pre>
 * <li>实现{@code Serializable } 进行序列和反序列的接口标准
 * <li>实现{@code Cloneable} 一个类要调用{@code Object} clone 方法就必须实现该接口
 * 
 * @author yuanjinze
 *
 */
public class Service implements Serializable, Cloneable {

	private static final long serialVersionUID = 4708100491896715978L;

	private final String name;// 服务的名字
	private final SocketAddress address;// 利用jdk 提供的api创建一个socket服务

	/**
	 * 创建一个新的实例 将这个名字和socke地址进行绑定.
	 * 
	 * @param name
	 */
	public Service(String name, SocketAddress address) {
		if (name == null)
			throw new NullPointerException("name");

		if (address == null)
			throw new NullPointerException("address");

		this.name = name;
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public SocketAddress getAddress() {
		return address;
	}

	public int hashCode() {
		return (name.hashCode() * 37) ^ address.hashCode();
	}

	public boolean equal(Object o) {
		if (o == null)
			return false;
		if (this == o)
			return true;
		if (!(o instanceof Service))
			return false;
		Service that = (Service) o;

		return this.name.equals(that.name) && this.address.equals(that.address);

	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}

	public String toString() {
		return "(" + name + "," + address + ")";
	}

}
