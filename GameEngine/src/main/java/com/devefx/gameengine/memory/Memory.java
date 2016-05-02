package com.devefx.gameengine.memory;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

import sun.misc.Unsafe;

@SuppressWarnings({ "restriction" })
public abstract class Memory {

	public void write(ByteBuffer buffer, long length, long offset) {
		Unsafe unsafe = UnsafeMemory.getUnsafe();
		for (long i = 0; i < length; i++) {
			buffer.put(unsafe.getByte(this, offset + i));
		}
	}
	
	public void copyMemory(ByteBuffer buffer) throws IllegalArgumentException, IllegalAccessException {
		System.out.println("b:");
		copyMemory(buffer, this);
	}
	
	void copyMemory(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
		if (object == null) {
			return;
		}
		Unsafe unsafe = UnsafeMemory.getUnsafe();
		for (Field field : object.getClass().getDeclaredFields()) {
			Class<?> type = field.getType();
			long fieldOffset = unsafe.objectFieldOffset(field);
			if (type.isPrimitive()) {
				copyMemory(buffer, object, UnsafeMemory.primitiveSize(type), fieldOffset);
			} else {
				if (!field.isAccessible()) {
					field.setAccessible(true);
				}
				if (UnsafeMemory.isPrimitiveArray(type)) {
					Object obj = field.get(object);
					if (obj != null) {
						int offset = unsafe.arrayBaseOffset(type);
						int sizeof = unsafe.arrayIndexScale(type);
						int size = unsafe.getInt(obj, offset - 4L);
						copyMemory(buffer, obj, size * sizeof, offset);
					}
				} else {
					copyMemory(buffer, field.get(object));
				}
			}
			System.out.println();
			/*if (UnsafeMemory.isPrimitiveArray(type)) {
				int offset = unsafe.arrayBaseOffset(type);
				int sizeof = unsafe.arrayIndexScale(type);
				int size = unsafe.getInt(this, fieldOffset + offset + unsafe.addressSize());
				copyMemory(buffer, this, size * sizeof, fieldOffset + offset + unsafe.addressSize() + 4);
			} else {
				if (!field.isAccessible()) {
					field.setAccessible(true);
				}
				copyMemory(buffer, field.get(object));
			}*/
		}
	}
	
	void copyMemory(ByteBuffer buffer, Object object, long length, long offset) {
		Unsafe unsafe = UnsafeMemory.getUnsafe();
		for (long i = 0; i < length; i++) {
			buffer.put(unsafe.getByte(object, offset + i));
			System.out.print(unsafe.getByte(object, offset + i) + " ");
		}
	}
	
}
