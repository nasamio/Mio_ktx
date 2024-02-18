/**
 * 
 */
package net.sourceforge.capcode.S57Library.files;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author cyrille
 *
 */
public class S57ByteBuffer {
	private ByteBuffer buffer = null;
	
	public S57ByteBuffer(){
		super();
		buffer = null;
	}
	
	public S57ByteBuffer(ByteBuffer bf){
		this();
		buffer = bf;
	}

	public S57ByteBuffer(byte[] array){
		this();
		buffer = ByteBuffer.wrap(array);
	}

	public S57ByteBuffer(int lg){
		this();
		buffer = ByteBuffer.allocate(lg);
	}
	
	public byte getByte(int o){
		return buffer.get(o);
	}
	
	public byte getByte() {
		return buffer.get();
	}
	
	public String getString(int size){
		return S57ByteBuffer.getString(buffer, size);
	}
	
	public static String getString(ByteBuffer b, int size){
		if (b == null) return "";
		byte[] c = new byte[size];
		b.get(c);
		String s = "";
		for (int i=0; i<c.length; i++){
			s = s + (char)c[i];
		}
		return s;
	}
	
	public static String getString(byte[] b, int size){
		if (b == null) return "";
		String s = "";
		for (int i = 0; i < size; i++){
			s = s + (char) b[i];
		}
		return s;
	}

	public static String getString(byte[] b, int offset, int size) {
		if (b == null) return "";
		String s = "";
		for (int i = 0; i < size && offset + i < b.length; i++){
			s = s + (char) b[offset + i];
		}
		return s;
	}
	
	public static int getInteger(byte[] array, int offset, int size) {
		int res = 0;
		try{
			res = Integer.parseInt(getString(array, offset, size));			
		}catch(NumberFormatException e){
		}
		return res;
	}
	
	public String getString(int offset, int size) {
		buffer.position(offset);
		return getString(size);
	}
	
	public static String getString(ByteBuffer b, int offset, int size) {
		b.position(offset);
		return getString(b, size);
	}

	public int decodeInteger(int offset, int size) {
		if (buffer == null) return 0;
		try{
			return Integer.parseInt(getString(offset, size));			
		}catch (NumberFormatException e){
			return 0;
		}
	}
		
	public int decodeInteger(int size) {
		if (buffer == null) return 0;
		try{
			return Integer.parseInt(getString(size));			
		}catch (NumberFormatException e){
			return 0;
		}
	}

	public void setSize(int size){
		buffer.limit(size);	
	}
	
	public void rewind(){
		buffer.rewind();
	}

	public long position() {
		return buffer.position();
	}
	
	public void gotoPosition(int newPosition){
		buffer.position(newPosition);
	}

	public byte[] getByteArray() {
		return buffer.array();
	}

	public byte[] getByteArray(byte[] array, int offset) {
		buffer.position(offset);
		buffer.get(array);
		return array;
	}

	public byte[] getByteArray(int offset, int size) {
		return getByteArray(new byte[size], offset);
	}


	public String getFieldAsString(byte[] terminator) {
		if (buffer == null) return "";
		String s = "";
		boolean terminated = false;
		while(buffer.position()<buffer.capacity() && !terminated){
			byte b = buffer.get();
			for (int j = 0; j < terminator.length; j++){
				terminated = (b == terminator[j]);
				if (terminated) break;
			}
			if (!terminated){
				s = s + (char)b;
			}
		}
		return s;
	}

	public long getSize() {
		return buffer.limit();
	}

	/**
	 * reads the content of fc, at the current position into the buffer.
	 * @param fc
	 * @throws IOException 
	 */
	public void readFrom(FileChannel fc) throws IOException {
		fc.read(buffer);
	}





}
