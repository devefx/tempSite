package com.devefx.edge.math;

import com.devefx.gameengine.base.types.Vec3;

public class Mat4 {
	
	public float[] m = new float[16];
	
	public Mat4() {
		loadIdentity();
	}
	
	public Mat4(Mat4 mat) {
		set( mat.m[0],  mat.m[1],  mat.m[2],  mat.m[3],
			 mat.m[4],  mat.m[5],  mat.m[6],  mat.m[7],
			 mat.m[8],  mat.m[9], mat.m[10], mat.m[11],
			mat.m[12], mat.m[13], mat.m[14], mat.m[15]);
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
	
	public Mat4 clone() {
		return new Mat4(this);
	}
	
	public void loadIdentity() {
		set(1.0f, 0.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 0.0f, 0.0f,
			0.0f, 0.0f, 1.0f, 0.0f,
			0.0f, 0.0f, 0.0f, 1.0f);
	}
	
	public void multiply(Mat4 matrix) {
		float product[] = new float[16];
		
	    product[0]  = m[0] * matrix.m[0]  + m[4] * matrix.m[1] + m[8]   * matrix.m[2]  + m[12] * matrix.m[3];
	    product[1]  = m[1] * matrix.m[0]  + m[5] * matrix.m[1] + m[9]   * matrix.m[2]  + m[13] * matrix.m[3];
	    product[2]  = m[2] * matrix.m[0]  + m[6] * matrix.m[1] + m[10]  * matrix.m[2]  + m[14] * matrix.m[3];
	    product[3]  = m[3] * matrix.m[0]  + m[7] * matrix.m[1] + m[11]  * matrix.m[2]  + m[15] * matrix.m[3];
	    
	    product[4]  = m[0] * matrix.m[4]  + m[4] * matrix.m[5] + m[8]   * matrix.m[6]  + m[12] * matrix.m[7];
	    product[5]  = m[1] * matrix.m[4]  + m[5] * matrix.m[5] + m[9]   * matrix.m[6]  + m[13] * matrix.m[7];
	    product[6]  = m[2] * matrix.m[4]  + m[6] * matrix.m[5] + m[10]  * matrix.m[6]  + m[14] * matrix.m[7];
	    product[7]  = m[3] * matrix.m[4]  + m[7] * matrix.m[5] + m[11]  * matrix.m[6]  + m[15] * matrix.m[7];
	    
	    product[8]  = m[0] * matrix.m[8]  + m[4] * matrix.m[9] + m[8]   * matrix.m[10] + m[12] * matrix.m[11];
	    product[9]  = m[1] * matrix.m[8]  + m[5] * matrix.m[9] + m[9]   * matrix.m[10] + m[13] * matrix.m[11];
	    product[10] = m[2] * matrix.m[8]  + m[6] * matrix.m[9] + m[10]  * matrix.m[10] + m[14] * matrix.m[11];
	    product[11] = m[3] * matrix.m[8]  + m[7] * matrix.m[9] + m[11]  * matrix.m[10] + m[15] * matrix.m[11];
	    
	    product[12] = m[0] * matrix.m[12] + m[4] * matrix.m[13] + m[8]  * matrix.m[14] + m[12] * matrix.m[15];
	    product[13] = m[1] * matrix.m[12] + m[5] * matrix.m[13] + m[9]  * matrix.m[14] + m[13] * matrix.m[15];
	    product[14] = m[2] * matrix.m[12] + m[6] * matrix.m[13] + m[10] * matrix.m[14] + m[14] * matrix.m[15];
	    product[15] = m[3] * matrix.m[12] + m[7] * matrix.m[13] + m[11] * matrix.m[14] + m[15] * matrix.m[15];
	    
	    this.m = product;
	}
	
	public void translate(float x, float y, float z) {
		Mat4 matrix = new Mat4();
		createTranslation(x, y, z, matrix);
		multiply(matrix);
	}
	
	public void transformPoint(Vec3 point, Vec3 dst) {
		assert(dst != null);
		transformVector(point.x, point.y, point.z, 1.0f, dst);
	}
	
	public void transformVector(float x, float y, float z, float w, Vec3 dst) {
		dst.x = x * m[0] + y * m[4] + z * m[8] + w * m[12];
	    dst.y = x * m[1] + y * m[5] + z * m[9] + w * m[13];
	    dst.z = x * m[2] + y * m[6] + z * m[10] + w * m[14];
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
	
	public static void createTranslation(Vec3 translation, Mat4 dst) {
		dst.m[12] = translation.x;
		dst.m[13] = translation.y;
		dst.m[14] = translation.z;
	}
	
	public static void createTranslation(float xTranslation, float yTranslation, float zTranslation, Mat4 dst) {
		dst.m[12] = xTranslation;
		dst.m[13] = yTranslation;
		dst.m[14] = zTranslation;
	}
	
	public static Mat4 multiplyMatrix(Mat4 m1, Mat4 m2) {
		Mat4 res = new Mat4(m1);
		res.multiply(m2);
		return res;
	}
}
