/**
 * 
 */
package org.yplatform.yminav2.core.session;

/**
 * @author jinze-yuan Represents the type of idleness of {@link IoSession} or
 *         {@link IoSession}. There are three types of idleness:
 *         <ul>
 *         <li>{@link #READ_IDLE} - No data is coming from the remote peer.</li>
 *         <li>{@link #WRITER_IDLE} - Session is not writting any data.</li>
 *         <li>{@link #BOTH_IDLE} - Both{@link #READ_IDLE} and
 *         {@link WRITER_IDLE}.</li>
 *         </ul>
 *         <p>
 *         Idle time settings are all disabled by default. You can enable them
 *         using {@link IoSessionConfig#setIdleTime(IdleStatus, int)}.
 */
public class IdleStatus {

	public static final IdleStatus READ_IDLE = new IdleStatus("read idle");
	public static final IdleStatus WRITER_IDLE = new IdleStatus("write idle");
	public static final IdleStatus BOTH_IDLE = new IdleStatus("both idle");

	private final String strValue;

	private IdleStatus(String strValue) {
		this.strValue = strValue;
	}

	@Override
	public String toString() {
		return strValue;
	}

}
