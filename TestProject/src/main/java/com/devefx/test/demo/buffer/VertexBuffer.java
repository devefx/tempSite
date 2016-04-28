package com.devefx.test.demo.buffer;

import java.nio.ByteBuffer;

public class VertexBuffer {

	private ByteBuffer verticesBuffer;
	private ByteBuffer indicesBuffer;
	
	public void init(int vertexSize, int indexSize) {
		verticesBuffer = ByteBuffer.allocate(vertexSize);
		indicesBuffer = ByteBuffer.allocate(indexSize);
	}
	
	public ByteBuffer getVerticesBuffer() {
		return verticesBuffer;
	}
	
	public ByteBuffer getIndicesBuffer() {
		return indicesBuffer;
	}
}
