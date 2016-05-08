package com.devefx.gameengine.math;

public class Mat4 {
	public float[] m = new float[16];
	
	public Mat4() {
		m = new float[] {
				1.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 1.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 1.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 1.0f
		};
	}
	
	public Mat4(float m11, float m12, float m13, float m14, float m21, float m22, float m23, float m24,
	           float m31, float m32, float m33, float m34, float m41, float m42, float m43, float m44) {
		set(m11, m12, m13, m14, m21, m22, m23, m24, m31, m32, m33, m34, m41, m42, m43, m44);
	}
	
	public void set(float m11, float m12, float m13, float m14, float m21, float m22, float m23, float m24,
            float m31, float m32, float m33, float m34, float m41, float m42, float m43, float m44) {
		m[0]  = m11; m[1]  = m21; m[2]  = m31; m[3]  = m41;
	    m[4]  = m12; m[5]  = m22; m[6]  = m32; m[7]  = m42;
	    m[8]  = m13; m[9]  = m23; m[10] = m33; m[11] = m43;
	    m[12] = m14; m[13] = m24; m[14] = m34; m[15] = m44;
	}
	
	public static void createPerspective(float fieldOfView, float aspectRatio, float zNearPlane, float zFarPlane, Mat4 dst) {
		float f_n = 1.0f / (zFarPlane - zNearPlane);
		float theta = fieldOfView * 0.0174532925f * 0.5f;
		if (Math.abs(theta % 57079632679489661923f) < 0.000001f) {
			System.err.println(String.format("Invalid field of view value (%s) causes attempted calculation tan(%s), which is undefined.", fieldOfView, theta));
			return;
		}
		float divisor = (float) Math.tan(theta);
		float factor = 1.0f / divisor;
		dst.m = new float[16];
		dst.m[0] = (1.0f / aspectRatio) * factor;
	    dst.m[5] = factor;
	    dst.m[10] = (-(zFarPlane + zNearPlane)) * f_n;
	    dst.m[11] = -1.0f;
	    dst.m[14] = -2.0f * zFarPlane * zNearPlane * f_n;
	}
	
	public static void createOrthographic(float width, float height, float zNearPlane, float zFarPlane, Mat4 dst) {
		float halfWidth = width / 2.0f;
	    float halfHeight = height / 2.0f;
	    createOrthographicOffCenter(-halfWidth, halfWidth, -halfHeight, halfHeight, zNearPlane, zFarPlane, dst);
	}
	
	public static void createOrthographicOffCenter(float left, float right, float bottom, float top,
            float zNearPlane, float zFarPlane, Mat4 dst) {
		dst.m = new float[16];
		dst.m[0] = 2 / (right - left);
	    dst.m[5] = 2 / (top - bottom);
	    dst.m[10] = 2 / (zNearPlane - zFarPlane);
	    dst.m[12] = (left + right) / (left - right);
	    dst.m[13] = (top + bottom) / (bottom - top);
	    dst.m[14] = (zNearPlane + zFarPlane) / (zNearPlane - zFarPlane);
	    dst.m[15] = 1;
	}
}
