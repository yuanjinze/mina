/**
*
*/
package org.yplatform.ymina.v083v1;

/**
 * @author yuanjinze
 *
 */
public class IdleStatus {

	public static final IdleStatus READER_IDLE = new IdleStatus("reader idle");

	public static final IdleStatus WRITER_IDLE = new IdleStatus("writer idle");

	public static final IdleStatus BOTH_IDLE = new IdleStatus("both idle");
	private final String strValue;

	public IdleStatus(String strValue) {
		this.strValue = strValue;
	}

	@Override
	public String toString() {
		return strValue;
	}

}
