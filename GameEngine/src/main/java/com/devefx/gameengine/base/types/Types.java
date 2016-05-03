package com.devefx.gameengine.base.types;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Types {
	private static final Map<Type, Integer> sizeMap = new HashMap<>();
	
	static {
		sizeMap.put(BlendFunc.class, 8);
		sizeMap.put(Color4B.class, 4);
		sizeMap.put(Color4F.class, 16);
		sizeMap.put(Rect.class, 16);
		sizeMap.put(Size.class, 8);
		sizeMap.put(Tex2F.class, 8);
		sizeMap.put(Vec2.class, 8);
		sizeMap.put(Vec3.class, 12);
		sizeMap.put(V3F_C4B_T2F.class, 24);
		sizeMap.put(V3F_C4B_T2F_Quad.class, 96);
	}
	
	public static int sizeof(Type type) {
		Integer size = sizeMap.get(type);
		if (size == null)
			throw new RuntimeException("unknow type of " + type);
		return size;
	}
	
	public static final int SIZEOF_BLEND_FUNC = sizeof(BlendFunc.class);
	public static final int SIZEOF_COLOR_4B = sizeof(Color4B.class);
	public static final int SIZEOF_COLOR_4F = sizeof(Color4F.class);
	public static final int SIZEOF_RECT = sizeof(Rect.class);
	public static final int SIZEOF_SIZE = sizeof(Size.class);
	public static final int SIZEOF_TEX_2F = sizeof(Tex2F.class);
	public static final int SIZEOF_VEC2 = sizeof(Vec2.class);
	public static final int SIZEOF_VEC3 = sizeof(Vec3.class);
	public static final int SIZEOF_V3F_C4B_T2F = sizeof(V3F_C4B_T2F.class);
	public static final int SIZEOF_V3F_C4B_T2F_QUAD = sizeof(V3F_C4B_T2F_Quad.class);
}
