package com.devefx.gameengine.memory;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

import sun.misc.Unsafe;

@SuppressWarnings({ "restriction" })
public final class Memory {

	private static final long BUFFER_ADDRESS_OFFSET = 16;
	
	private static final long ARRAY_OFFSET = 16;
	
	private static Unsafe unsafe;
	
	public static void copy(Object srcObject, long srcOffset, Buffer destObject, long destOffset, long count) {
		int size = memberSize(destObject);
		if (size != 0) {
			Unsafe unsafe = getUnsafe();
			if (destObject.isDirect()) { // is direct buffers
				long address = unsafe.getLong(destObject, BUFFER_ADDRESS_OFFSET);
				unsafe.copyMemory(srcObject, srcOffset, null, address + destOffset, count * size);
			} else if (destObject.hasArray()) { // is heap buffers
				unsafe.copyMemory(srcObject, srcOffset, destObject.array(), ARRAY_OFFSET + destOffset, count * size);
			}
			destObject.position(destObject.position() + (int)count * size);
		}
	}
	
	public static int ix(Buffer buffer) {
		if (buffer.isDirect()) {
			return buffer.position();
		} else if (buffer.hasArray()) {
			return buffer.position() + buffer.arrayOffset();
		}
		return 0;
	}
	
	
	
	public static Unsafe getUnsafe() {
		try {
			if (unsafe == null) {
				Field field = Unsafe.class.getDeclaredField("theUnsafe");
				field.setAccessible(true);
				unsafe = (Unsafe) field.get(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return unsafe;
	}
	
	static int memberSize(Buffer b) {
		return b instanceof ByteBuffer ? 1 : (b instanceof ShortBuffer || b instanceof CharBuffer) ? 2 :
			(b instanceof IntBuffer || b instanceof FloatBuffer) ? 4 : 
				(b instanceof LongBuffer || b instanceof DoubleBuffer) ? 8 : 0;
	}
	
	static int primitiveSize(Type t) {
		return t == byte.class ? 1 : (t == short.class || t == char.class) ? 2 : 
			(t == int.class || t == float.class) ? 4 : (t == long.class || t == double.class) ? 8 :
				unsafe.addressSize();
	}
	
	static boolean isPrimitiveArray(Type t) {
		return t == byte[].class || t == short[].class || t == char[].class || t == int[].class
				|| t == float[].class || t == long[].class || t == double[].class;
	}
}
