/**
* yplatform-2016年8月10日
*/
package org.yplatform.ymina.util;

import static org.junit.Assert.fail;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * @author yuanjinze
 *
 */
public class QueueTest extends TestCase {

	private int pushCount;
	private int popCount;

	@Override
	protected void setUp() throws Exception {
		pushCount = 0;
		popCount = 0;
	}

	public void testRotation() {
		Queue q = new Queue();
		testRotation0(q);
	}

	private void testRotation0(Queue q) {
		// for(int i=0;i<q.ca){

		// }
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
