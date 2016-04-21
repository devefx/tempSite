package com.devefx.gameengine.renderer.impl;

import java.nio.Buffer;
import java.nio.IntBuffer;

import com.devefx.gameengine.base.Color.Color4F;
import com.devefx.gameengine.base.Configuration;
import com.devefx.gameengine.math.Mat4;
import com.devefx.gameengine.math.Size;
import com.devefx.gameengine.renderer.GroupCommandManager;
import com.devefx.gameengine.renderer.QuadCommand;
import com.devefx.gameengine.renderer.RenderCommand;
import com.devefx.gameengine.renderer.RenderQueue;
import com.devefx.gameengine.renderer.Renderer;
import com.devefx.gameengine.renderer.TrianglesCommand;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLContext;

public class GLRenderer implements Renderer {

	public static final int VBO_SIZE = 65536;
	public static final int INDEX_VBO_SIZE = VBO_SIZE * 6 / 4;
	
	public static final int BUF_SIZE = 12 + 16 + 8;
	
	private final GL2 gl = GLContext.getCurrentGL().getGL2();
	
	private IntBuffer 		buffersVBO;
	private IntBuffer 		quadbuffersVBO;
	private Buffer			verts;
	private Buffer			indices;
	
	public GLRenderer() {
		buffersVBO = IntBuffer.allocate(2);
		quadbuffersVBO = IntBuffer.allocate(2);
		verts = Buffers.newDirectByteBuffer(BUF_SIZE * VBO_SIZE);
		indices = Buffers.newDirectShortBuffer(INDEX_VBO_SIZE);
	}
	
	@Override
	public void initGLView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addCommand(RenderCommand command) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addCommand(RenderCommand command, int renderQueue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pushGroup(int renderQueueID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void popGroup() {
		// TODO Auto-generated method stub

	}

	@Override
	public int createRenderQueue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

	@Override
	public void clean() {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setClearColor(Color4F clearColor) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getDrawnBatches() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addDrawnBatches(int number) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getDrawnVertices() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addDrawnVertices(int number) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearDrawStats() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDepthTest(boolean enable) {
		// TODO Auto-generated method stub

	}

	@Override
	public GroupCommandManager getGroupCommandManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkVisibility(Mat4 transform, Size size) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setupBuffer() {
		if (Configuration.getInstance().supportsShareableVAO()) {
			setupVBOAndVAO();
		} else {
			setupVBO();
		}
	}

	@Override
	public void setupVBOAndVAO() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setupVBO() {
		// 创建生成两个VBO
		gl.glGenBuffers(2, buffersVBO);
		gl.glGenBuffers(2, quadbuffersVBO);
		// 调用函数绑定buffer
		mapBuffers();
	}

	@Override
	public void mapBuffers() {
		Configuration.bindVAO(0);
		// 绑定id顶点数据
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffersVBO.get(0));
		// 为改id制定一段内存区域
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, BUF_SIZE * VBO_SIZE, verts, GL2.GL_DYNAMIC_DRAW);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
		// 第二个VBO索引数据
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buffersVBO.get(1));
		gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, 2 * VBO_SIZE * 6, indices, GL2.GL_STATIC_DRAW);
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	@Override
	public void drawBatchedTriangles() {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawBatchedQuads() {
		// TODO Auto-generated method stub

	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub

	}

	@Override
	public void flush2D() {
		// TODO Auto-generated method stub

	}

	@Override
	public void flush3D() {
		// TODO Auto-generated method stub

	}

	@Override
	public void flushQuads() {
		// TODO Auto-generated method stub

	}

	@Override
	public void flushTriangles() {
		// TODO Auto-generated method stub

	}

	@Override
	public void processRenderCommand(RenderCommand command) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visitRenderQueue(RenderQueue queue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fillVerticesAndIndices(TrianglesCommand cmd) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fillQuads(QuadCommand cmd) {
		// TODO Auto-generated method stub

	}

}
