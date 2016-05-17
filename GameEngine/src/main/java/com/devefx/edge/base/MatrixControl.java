package com.devefx.edge.base;

import java.util.Stack;

import com.devefx.edge.math.Mat4;

public class MatrixControl {
	
	public enum MatrixStackType {
		MATRIX_STACK_MODELVIEW,
		MATRIX_STACK_PROJECTION,
		MATRIX_STACK_TEXTURE
	}
	
	protected Stack<Mat4> modelViewMatrixStack;
	protected Stack<Mat4> projectionMatrixStack;
	protected Stack<Mat4> textureMatrixStack;
	
	protected void initMatrixStack() {
		
	}
	
	public void pushMatrix(MatrixStackType type) {
		
	}
	
	public void popMatrix(MatrixStackType type) {
		
	}
	
	public void loadIdentityMatrix(MatrixStackType type) {
		
	}
	
	public void loadMatrix(MatrixStackType type, final Mat4 mat) {
		
	}
	
	public void multiplyMatrix(MatrixStackType type, final Mat4 mat) {
		
	}
	
	public Mat4 getMatrix(MatrixStackType type) {
		return null;
	}
	
	public void resetMatrixStack() {
		
	}
}
