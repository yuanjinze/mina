/**
* yplatform-2016年8月17日
*/
package org.yplatform.ymina.common;

/**
 * Represents the type of idleness of {@link IoSession} or
 * {@link ProtocolSession}. There are three types of idleness:
 * <ul>
 * <li>{@link #READER_IDLE} - No data is coming from the remote peer.</li>
 * <li>{@link #WRITER_IDLE} - Session is not writing any data.</li>
 * <li>{@link #BOTH_IDLE} - Both {@link #READER_IDLE} and {@link #WRITER_IDLE}.</li>
 * </ul>
 * <p>
 * Idle time settings are all disabled by default. You can enable them
 * using {@link SessionConfig#setIdleTime(IdleStatus,int)}.
 * @author yuanjinze
 *
 */
public class IdleStatus {
	/**
	 * Represents the session status that no data is coming from the remote
	 * peer.
	 */
	public static final IdleStatus READER_IDLE=new IdleStatus("reader idle");

	/**
	 * Represents the session status that the session is not writing any data.
	 */
	public static final IdleStatus WRITER_IDLE=new IdleStatus("writer idle");
	/**
	 * Represents both {@link #READER_IDLE} and {@link #WRITER_IDLE}.
	 */
	public static final IdleStatus BOTH_IDLE=new IdleStatus("both idle");
	
	private final String strValue;
	
	/**
	 * Creates a new instance.
	 * @param strValue
	 */
	private IdleStatus(String strValue){
		this.strValue=strValue;
	}
	/**
	 * Returns the string representation of this status.
	 * <ul>
	 * <li>{@link #READER_IDLE} -<tt>"reader idle"</tt></li>
	 * <li>{@link #WRITER_IDLE} -<tt>"writer idle"</tt></li>
	 * <li>{@link #BOTH_IDLE} -<tt>"both idle"</tt></li>
	 * </ul>
	 */
	public String toString(){
		return strValue;
	}
}
