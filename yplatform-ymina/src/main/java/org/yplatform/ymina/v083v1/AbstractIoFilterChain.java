/**
*
*/
package org.yplatform.ymina.v083v1;

import org.yplatform.ymina.v083v1.IoFilter.NextFilter;

/**
 * @author yuanjinze
 *
 */
public abstract class AbstractIoFilterChain implements IoFilterChain {
	private final Entry head;// 头
	private final Entry tail;// 尾

	protected AbstractIoFilterChain() {
		head = new Entry(null, null, "head", createHeadFilter());
		tail = new Entry(head, null, "tail", createTailFilter());
		head.nextEntry = tail;
	}

	protected abstract void doWrite(IoSession session, ByteBuffer buffer, Object marker);

	@Override
	public void addFirst(String name, IoFilter filter) {
		// TODO Auto-generated method stub
		checkAddable(name);// 检查能不能添加这个过滤对象
		register(head, name, filter);

	}

	public void filterWrite(IoSession session, ByteBuffer buf, Object marker) {
		Entry tail = this.tail;
		callPreviousFilterWrite(tail, session, buf, marker);
	}

	private void callPreviousFilterWrite(Entry entry, IoSession session, ByteBuffer buf, Object marker) {

		try {
			entry.filter.filterWrite(entry.nextFilter, session, buf, marker);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkAddable(String name) {
		// TODO yjz...

	}

	private void register(Entry prevEntry, String name, IoFilter filter) {
		Entry newEntry = new Entry(prevEntry, prevEntry.nextEntry, name, filter);
		prevEntry.nextEntry.prevEntry = newEntry;
		prevEntry.nextEntry = newEntry;
		// TODO yjz

	}

	public void dataRead(IoSession session, ByteBuffer buf) {
		Entry entry = this.head;
		callNextDataRead(entry, session, buf);
	}

	private IoFilter createTailFilter() {
		return new IoFilter() {

			@Override
			public void dataRead(NextFilter nextFilter, IoSession session, ByteBuffer buf) throws Exception {
				// 处理链尾部，说明没有处理链了，则直接将数据交由IoHandler来进行处理了。
				// TODO yjz
				IoHandler handler = session.getHandler();
				handler.dataRead(session, buf);
			}

			@Override
			public void filterWrite(NextFilter nextFilter, IoSession session, ByteBuffer buf, Object marker)
					throws Exception {
				nextFilter.filterWrite(session, buf, marker);

			}

		};
	}

	private IoFilter createHeadFilter() {
		return new IoFilter() {

			@Override
			public void dataRead(NextFilter nextFilter, IoSession session, ByteBuffer buf) throws Exception {

				nextFilter.dataRead(session, buf);
			}

			@Override
			public void filterWrite(NextFilter nextFilter, IoSession session, ByteBuffer buf, Object marker)
					throws Exception {
				doWrite(session, buf, marker);
			}

		};
	}

	private void callNextDataRead(Entry entry, IoSession session, ByteBuffer buf) {
		try {
			entry.filter.dataRead(entry.nextFilter, session, buf);
		} catch (Throwable e) {
			// TODO yjz
		}
	}

	/**
	 * 节点（双向链表)
	 * 
	 * @author yuanjinze
	 *
	 */
	private class Entry {
		private Entry prevEntry;
		private Entry nextEntry;
		private final String name;// 加入final的意思是，从外表传入的值就什么就什么，你内部方法不能修改我外部传进来的值了

		private final IoFilter filter;
		private final NextFilter nextFilter;// 这个其实表示的是在下一个Entry里面的filter

		public Entry(Entry prevEntry, Entry nextEntry, String name, IoFilter filter) {
			this.prevEntry = prevEntry;
			this.nextEntry = nextEntry;
			this.name = name;
			this.filter = filter;
			this.nextFilter = new NextFilter() {

				@Override
				public void dataRead(IoSession session, ByteBuffer buf) {
					Entry nextEntry = Entry.this.nextEntry;
					callNextDataRead(nextEntry, session, buf);
				}

				@Override
				public void filterWrite(IoSession session, ByteBuffer buf, Object marker) {

					Entry nextEntry = Entry.this.prevEntry;// 倒起的，跟读是相反的
					callPreviousFilterWrite(nextEntry, session, buf, marker);
				}
			};
		}
	}
}
