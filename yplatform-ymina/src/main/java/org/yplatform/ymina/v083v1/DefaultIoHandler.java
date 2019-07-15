package org.yplatform.ymina.v083v1;

public class DefaultIoHandler implements IoHandler {

	@Override
	public void dataRead(IoSession session, ByteBuffer buf) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("代码处理走到业务处理层。。。哈哈！！！");
		// 打印信息方法一：
		// if (buf.limit() > 0) {
		// java.nio.ByteBuffer db = (java.nio.ByteBuffer) buf.buf();
		// byte[] array = new byte[db.limit()];
		// buf.buf().get(array, db.position(), db.limit());
		// System.out.println("服务端收到的消息:" + new String(array).trim());
		// }

		// 打印信息方法二：
		buf.clear();
		while (buf.hasRemaining()) {
			System.out.print((char) buf.get());
		}
		System.out.flush();
		System.out.println("完成读的内容!!!!");

		// yjz 调写入方法
		// session.write(buf, new Object());
//		final IoSession session0 = session;
//		Thread t = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					dataWritten(session0, "xiaoyuan");
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//		t.start();
	}

	@Override
	public void dataWritten(IoSession session, Object marker) throws Exception {
		// TODO Auto-generated method stub
		System.out.print("yjz: 开始往通道写数据到客户端...(yuanjinze)");
		ByteBuffer buf = ByteBuffer.allocate(1024);
		buf.put(new String("yuanjinze").getBytes());

		session.write(buf, marker);

	}

}
