package com.devefx.gameengine.memory;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.Buffer;

import sun.misc.Unsafe;

@SuppressWarnings({ "restriction" })
public final class Memory {

	private static final long BUFFER_ADDRESS_OFFSET = 16;
	
	private static final long ARRAY_OFFSET = 16;
	
	private static Unsafe unsafe;
	
	public static void copy(Object srcObject, long srcOffset, Buffer destObject, long destOffset, long count) {
		Unsafe unsafe = getUnsafe();
		if (destObject.isDirect()) { // is direct buffers
			long address = unsafe.getLong(destObject, BUFFER_ADDRESS_OFFSET);
			unsafe.copyMemory(srcObject, srcOffset, null, address + destOffset, count);
		} else if (destObject.hasArray()) { // is heap buffers
			unsafe.copyMemory(srcObject, srcOffset, destObject.array(), ARRAY_OFFSET + destOffset, count);
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
