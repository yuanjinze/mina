package org.yplatform.yminav2.core.session;

/**
 * 先定义客户端和服务端都需要的明白的交互信息（即通用凭证)
 * 
 * A handle which represents connection between two end-points regardless of
 * transport type.
 * <p>
 * {@link IoSession} provides user-defined attributes. User-defined attributes 
 * are application-specific data which are associated with a session.
 * It often contains objects that reprents the state of a higher-level protocol
 * and becomes a way to exchange data between filters and handlers.
 * <p/>
 * <h3> Adjusting Transport Type Specific Properties</h3>
 * <p/>
 * You can simply downcast the session to an appropriate subclass.
 * </p>
 * <h3> Thread Safety</h3>
 * 
 * <p/>
 * {@link IoSession} is thread-safe. But please not that performing
 * more than one {@link #write(Object)} calls at the same time will
 * cause the {@link IoFilter#filterWrite(IoFilter.NextFilter,IoSession,WriteRequest)}
 * to be executed simultaneously, and therefore you have to make sure the 
 * {@link IoFilter} implementations you're using are thread-safe,too.
 * </p>
 * <p/>
 * <h3> Equality of Sessions</h3>
 * @author jinze-yuan
 *
 */
public interface IoSession {
	// 服务端
	IoHandler getHandler();

//	IoService getService();

//	IoFilterChain getFilterChain();
	// 客户端
}
