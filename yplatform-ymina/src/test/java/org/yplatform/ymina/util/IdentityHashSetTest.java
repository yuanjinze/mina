/**
*
*/
package org.yplatform.ymina.util;

import static org.junit.Assert.fail;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * @author yuanjinze
 *
 */
public class IdentityHashSetTest extends TestCase {

	@Test
	public void testConstruct() {
		IdentityHashSet set = new IdentityHashSet();
		set.add("xiaoxiao");
		set.add("yuanjinze");
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(IdentityHashSetTest.class);
	}
}
