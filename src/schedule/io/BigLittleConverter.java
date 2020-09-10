package schedule.io;

public class BigLittleConverter {
	public static byte[] toMinByte(char c) {
		byte[] result = new byte[1];
		// 由高位到低位
		result[0] = (byte) (c & 0xFF);
		return result;
	}

	public static byte[] toMinByte(short c) {
		byte[] result = new byte[2];
		// 由高位到低位
		result[1] = (byte) ((c >> 8) & 0xFF);
		result[0] = (byte) (c & 0xFF);
		return result;
	}

	public static byte[] toMinByte(int c) {
		byte[] result = new byte[4];
		// 由高位到低位
		result[3] = (byte) ((c >> 24) & 0xFF);
		result[2] = (byte) ((c >> 16) & 0xFF);
		result[1] = (byte) ((c >> 8) & 0xFF);
		result[0] = (byte) (c & 0xFF);
		return result;
	}
	
	public static byte[] intToByteArr(int x){
		byte[] arr = new byte[4];
		arr[3]= (byte)(x & 0xff);
		arr[2]= (byte)(x>>8 & 0xff);
		arr[1]= (byte)(x>>16 & 0xff);
		arr[0]= (byte)(x>>24 & 0xff);
		return arr;
	}
	
	public static int byteArrToInt(byte[] arr){
		int x = ((arr[0] & 0xff) << 24 )|((arr[1]& 0xff) <<16 )|((arr[2] & 0xff)<<8)|(arr[3] & 0xff);
		return x;
	}


	public static byte[] concatBytes(byte[]... bytes) {
		int len = 0;
		int index = 0;
		for (byte[] b : bytes)
			len += b.length;
		byte[] buffer = new byte[len];
		for (byte[] b : bytes) {
			System.arraycopy(b, 0, buffer, index, b.length);
			index += b.length;
		}
		return buffer;
	}
}
