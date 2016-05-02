package test;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import sun.misc.Unsafe;

import com.devefx.gameengine.base.Director;
import com.devefx.gameengine.base.types.Color4B;
import com.devefx.gameengine.base.types.Rect;
import com.devefx.gameengine.base.types.Types;
import com.devefx.gameengine.base.types.V3F_C4B_T2F;
import com.devefx.gameengine.base.types.Vec3;
import com.devefx.gameengine.memory.Memory;
import com.devefx.gameengine.platform.GLView;
import com.devefx.gameengine.platform.desktop.GLViewImpl;
import com.jogamp.common.nio.Buffers;

@SuppressWarnings({ "restriction" })
public class Test {
	
	public static void main(String[] args) throws Exception {
		/*
		Director director = Director.getInstance();
		
		GLView glView = director.getOpenGLView();
		
		if (glView == null) {
			
			glView = GLViewImpl.create("OpenGLView", new Rect(0, 0, 800, 600));
			
			director.setOpenGLView(glView);
		}
		
		glView.setDesignResolutionSize(800, 600);
		*/
		Unsafe unsafe = getUnsafe();
		V3F_C4B_T2F v = (V3F_C4B_T2F) unsafe.allocateInstance(V3F_C4B_T2F.class);
		v.vertices = new Vec3();
		
		
		
		Field field = V3F_C4B_T2F.class.getField("vertices");
		long offset = unsafe.objectFieldOffset(field);
		Object obj = unsafe.getObjectVolatile(v, offset);
		
		
		T t = new T();
		t.i = 5;
		t.is = new int[10];
		t.is[0] = 3;
		t.is[9] = 3;
		t.c = 11;
		t.t = new T();
		t.t.i = 15;
		
		
		//System.out.println(sizeOf(t));
		
		int[] s = new int[5];
		s[0] = 8;
		s[1] = 4;
	   	
	   	
	 
		T t2 = new T();
		t2.i = 77;
		t2.c = 11;
		
		int[] ss = new int[5];
		//unsafe.copyMemory(s, 0, null, address, 0);
		
		
		long address = unsafe.allocateMemory(40);
	   	unsafe.setMemory(address, 40, (byte) 1);
	   	
	   	unsafe.copyMemory(t, 0, null, address, 20);
	   	
		for (int i = 0; i < 40; i++) {
			System.out.print(unsafe.getByte(t, i) + " ");
		}
		System.out.println();
		for (int i = 0; i < 40; i++) {
			System.out.print(unsafe.getByte(address + i) + " ");
		}
		
		
		
	//	t.s = ";";
		/*
		System.out.println(unsafe.objectFieldOffset(T.class.getDeclaredField("is")));
		for (int i = 0; i < 44 + 8 + 4 + 20; i++) {
			System.out.print(unsafe.getByte(t, i + 8L + 12L + 12L + 4L) + "(" + (i + 8L) + ") ");
		}
		System.out.println();
		*/
		/*
		int baseOffset = unsafe.arrayBaseOffset(t.is.getClass());
		int size = unsafe.getInt(t.is, baseOffset - 4L);
		int sizeof = unsafe.arrayIndexScale(t.is.getClass());
		for (long i = 0; i < size * sizeof; i++) {
			System.out.print(unsafe.getByte(t.is, i + baseOffset) + " ");
		}
		System.out.println();
		
		for (long i = 0; i < size; i++) {
			System.out.print(unsafe.getInt(t.is, i * 4L + baseOffset) + " ");
		}
		System.out.println();*/
		/*
		ByteBuffer buffer = Buffers.newDirectByteBuffer(200);
		t.copyMemory(buffer);
		buffer.rewind();
		*/
		//System.out.println(unsafe.arrayIndexScale(t.is.getClass()));
		
		
	/*	
		Vec3 vec3 = new Vec3();
		vec3.x = 5.0f;
		vec3.y = 1.0f;
		vec3.z = 11.0f;
		for (int i = 0; i < 3; i++) {
			System.out.print(unsafe.getFloat(vec3, i * 4L + 8) + " ");
		}
		System.out.println();
		

		
		ByteBuffer buffer = Buffers.newDirectByteBuffer(12);
		vec3.copyComplex(buffer);
		
		buffer.rewind();
		
		System.out.println(buffer.getFloat());
		System.out.println(buffer.getFloat());
		System.out.println(buffer.getFloat());*/
	}
	
	static class T extends Memory {
		int i;
		T t;
		int[] is;
		char c;
		
	}
	
	public static Unsafe getUnsafe() {
		try {
			Field field = Unsafe.class.getDeclaredField("theUnsafe");
			field.setAccessible(true);
			return (Unsafe) field.get(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static long sizeOf(Object object) {
	   Unsafe unsafe = getUnsafe();
	   return unsafe.getAddress( normalize( unsafe.getInt(object, 4L) ) + 12L );
	}
		 
	public static long normalize(int value) {
	   if(value >= 0) return value;
	   return (~0L >>> 32) & value;
	}
}
