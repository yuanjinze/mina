package org.yplatform.ymina.common;
/**
 * Provides utiltiy methods for ByteBuffers.
 * @author jinze-yuan
 *
 */
public class ByteBufferHexDumper {
	private static final byte[] highDigits;
	private static final byte[] lowDigits;
	
	// initialize lookup tables
	static{
		final byte[] digits={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		
		int i;
		byte[] high=new byte[256];
		byte[] low=new byte[256];
		for(i=0;i<256;i++){
			high[i]=digits[i>>>4];
			low[i]=digits[i&0x0f];
		}
		highDigits=high;
		lowDigits=low;
	}
	
	static String getHexdump(ByteBuffer in){
		int size=in.remaining();
		if(size==0){
			return "empty";
		}
		StringBuffer out=new StringBuffer((in.remaining()*3)-1);
		
		int mark=in.position();
		
		//fill the first
		int byteValue=in.get()&0xFF;
		out.append((char)highDigits[byteValue]);
		out.append((char)lowDigits[byteValue]);
		size--;
		
		//and the others, too
		for(;size>0;size--){
			out.append(' ');
			byteValue=in.get()&0xFF;
			out.append((char)highDigits[byteValue]);
			out.append((char)lowDigits[byteValue]);
		}
		in.position(mark);
		return out.toString();
	}

}
