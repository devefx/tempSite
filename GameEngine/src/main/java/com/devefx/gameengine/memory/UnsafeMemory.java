package com.devefx.gameengine.memory;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import sun.misc.Unsafe;

@SuppressWarnings({ "restriction" })
public class UnsafeMemory {
	
	private static Unsafe unsafe;
	
	protected static Unsafe getUnsafe() {
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
	
	protected static int primitiveSize(Type t) {
		return t == byte.class ? 1 : (t == short.class || t == char.class) ? 2 : 
			(t == int.class || t == float.class) ? 4 : (t == long.class || t == double.class) ? 8 :
				unsafe.addressSize();
	}
	
	protected static boolean isPrimitiveArray(Type t) {
		return t == byte[].class || t == short[].class || t == char[].class || t == int[].class
				|| t == float[].class || t == long[].class || t == double[].class;
	}
	
	
}
