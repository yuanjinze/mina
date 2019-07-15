/**
*
*/
package org.yplatform.ymina.v083v1;

/**
 * @author yuanjinze
 *
 */
public interface IoFilterChain {
	
	void addFirst(String name,IoFilter filter);

}
