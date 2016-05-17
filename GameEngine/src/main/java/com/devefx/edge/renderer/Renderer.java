package com.devefx.edge.renderer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import com.devefx.edge.base.types.Color4F;
import com.devefx.edge.base.types.Size;
import com.devefx.edge.base.types.Types;
import com.devefx.edge.math.Mat4;
import com.devefx.edge.renderer.GLStateCache.GL;
import com.devefx.gameengine.renderer.QuadCommand;
import com.devefx.gameengine.renderer.RenderQueue;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;

public class Renderer {
	
	public static final boolean supportsShareableVAO = true;
	
	public static final int VBO_SIZE = 65536;
	public static final int INDEX_VBO_SIZE = VBO_SIZE * 6 / 4;
	
	public static final int BATCH_QUADCOMMAND_RESEVER_SIZE = 64;
	public static final int MATERIAL_ID_DO_NOT_BATCH = 0;
	
	public static final int VERTEX_ATTRIB_POSITION	= 0;
	public static final int VERTEX_ATTRIB_COLOR		= 1;
	public static final int VERTEX_ATTRIB_TEX_COORD = 2;
	
	protected RenderQueue renderQueue;
	protected Color4F clearColor;

	protected List<QuadCommand> batchQuadCommands;
	// for TrianglesCommand
	protected IntBuffer buffersVAO;
	protected IntBuffer buffersVBO;
	protected ByteBuffer verts;
	protected ShortBuffer indices;
	// for QuadCommand
	protected IntBuffer quadVAO;
	protected IntBuffer quadbuffersVBO;
	protected ByteBuffer quadVerts;
	protected ShortBuffer quadIndices;
	protected int numberQuads;
	
	protected boolean glViewAssigned;
	
	protected int drawnBatches;
	protected int drawnVertices;
	
	protected boolean isDepthTest;
	protected boolean isRendering;
	
	protected int lastMaterialID;
	
	public Renderer() {
		renderQueue = new RenderQueue();
		batchQuadCommands = new ArrayList<QuadCommand>();
		clearColor = Color4F.BLACK;
		
		buffersVAO = Buffers.newDirectIntBuffer(1);
		buffersVBO = Buffers.newDirectIntBuffer(2);
		verts = Buffers.newDirectByteBuffer(Types.SIZEOF_V3F_C4B_T2F * VBO_SIZE);
		indices = Buffers.newDirectShortBuffer(Buffers.SIZEOF_SHORT * INDEX_VBO_SIZE);
		
		quadVAO = Buffers.newDirectIntBuffer(1);
		quadbuffersVBO = Buffers.newDirectIntBuffer(2);
		quadVerts = Buffers.newDirectByteBuffer(Types.SIZEOF_V3F_C4B_T2F_QUAD * VBO_SIZE);
		quadIndices = Buffers.newDirectShortBuffer(Buffers.SIZEOF_SHORT * INDEX_VBO_SIZE);
	}
	/**
	 * 初始化
	 */
	public void initGLView() {
		for (int i = 0; i < VBO_SIZE / 4; i++) {
			quadIndices.put((short) (i * 4 + 0));
			quadIndices.put((short) (i * 4 + 1));
			quadIndices.put((short) (i * 4 + 2));
			quadIndices.put((short) (i * 4 + 3));
			quadIndices.put((short) (i * 4 + 2));
			quadIndices.put((short) (i * 4 + 1));
		}
		quadIndices.rewind();
		
		setupBuffer();
		
		glViewAssigned = true;
	}
	/**
	 * 添加‘RenderCommand’到渲染队列
	 * @param command
	 */
	public void addCommand(RenderCommand command) {
		
	}
	/**
	 * 绘画全部‘RenderCommand’
	 */
	public void render() {
		
	}
	/**
	 * 清理全部‘RenderCommand’
	 */
	public void clean() {
		
	}
	/**
	 * 清屏
	 */
	public void clear() {
		
	}
	/**
	 * 清除绘画状态
	 */
	public void clearDrawStats() {
		
	}
	/**
	 * 设置清屏颜色
	 * @param clearColor 
	 */
	public void setClearColor(final Color4F clearColor) {
		
	}
	/**
	 * 启用或停用深度测试
	 * @param enable
	 */
	public void setDepthTest(boolean enable) {
		
	}
	/**
	 * 设置缓冲
	 */
	protected void setupBuffer() {
		if (supportsShareableVAO) {
			setupVBOAndVAO();
		} else {
			setupVBO();
		}
	}
	/**
	 * 设置VAO和VBO
	 */
	protected void setupVBOAndVAO() {
		GL2 gl = GL.getGL();
		// generate vao for trianglesCommand
		gl.glGenVertexArrays(1, buffersVAO);
		GL.bindVAO(buffersVAO.get(0));
		// generate vbo for trianglesCommand
		gl.glGenBuffers(2, buffersVBO);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffersVBO.get(0));
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, Types.SIZEOF_V3F_C4B_T2F * VBO_SIZE, verts, GL2.GL_DYNAMIC_DRAW);
	
		// vertices
		gl.glEnableVertexAttribArray(VERTEX_ATTRIB_POSITION);
		gl.glVertexAttribPointer(VERTEX_ATTRIB_POSITION, 3, GL2.GL_FLOAT, false, Types.SIZEOF_V3F_C4B_T2F, 0);
		// colors
		gl.glEnableVertexAttribArray(VERTEX_ATTRIB_COLOR);
		gl.glVertexAttribPointer(VERTEX_ATTRIB_COLOR, 4, GL2.GL_UNSIGNED_BYTE, true, Types.SIZEOF_V3F_C4B_T2F, 12);
		// tex coords
		gl.glEnableVertexAttribArray(VERTEX_ATTRIB_TEX_COORD);
		gl.glVertexAttribPointer(VERTEX_ATTRIB_TEX_COORD, 2, GL2.GL_FLOAT, false, Types.SIZEOF_V3F_C4B_T2F, 16);
		
		// generate vio for trianglesCommand
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buffersVBO.get(1));
		gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, Buffers.SIZEOF_SHORT * INDEX_VBO_SIZE, indices, GL2.GL_STATIC_DRAW);
		
		// Must unbind the VAO before changing the element buffer.
		GL.bindVAO(0);
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
		
		//generate vao for quadCommand
		gl.glGenVertexArrays(1, quadVAO);
		GL.bindVAO(quadVAO.get(0));
		
		// generate vbo for quadCommand
		gl.glGenBuffers(2, quadbuffersVBO);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, quadbuffersVBO.get(0));
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, Types.SIZEOF_V3F_C4B_T2F * VBO_SIZE, null, GL2.GL_DYNAMIC_DRAW);
		
		// vertices
		gl.glEnableVertexAttribArray(VERTEX_ATTRIB_POSITION);
		gl.glVertexAttribPointer(VERTEX_ATTRIB_POSITION, 3, GL2.GL_FLOAT, false, Types.SIZEOF_V3F_C4B_T2F, 0);
		// colors
		gl.glEnableVertexAttribArray(VERTEX_ATTRIB_COLOR);
		gl.glVertexAttribPointer(VERTEX_ATTRIB_COLOR, 4, GL2.GL_UNSIGNED_BYTE, true, Types.SIZEOF_V3F_C4B_T2F, 12);
		// tex coords
		gl.glEnableVertexAttribArray(VERTEX_ATTRIB_TEX_COORD);
		gl.glVertexAttribPointer(VERTEX_ATTRIB_TEX_COORD, 2, GL2.GL_FLOAT, false, Types.SIZEOF_V3F_C4B_T2F, 16);
		
		// generate vio for quadCommand
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, quadbuffersVBO.get(1));
		gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, Buffers.SIZEOF_SHORT * INDEX_VBO_SIZE, quadIndices, GL2.GL_STATIC_DRAW);
		
		// Must unbind the VAO before changing the element buffer.
		GL.bindVAO(0);
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
	}
	/**
	 * 设置VBO
	 */
	protected void setupVBO() {
		GL2 gl = GL.getGL();
		gl.glGenBuffers(2, buffersVBO);
		gl.glGenBuffers(2, quadbuffersVBO);
		mapBuffers();
	}
	/**
	 * 映射VBO
	 */
	protected void mapBuffers() {
		GL2 gl = GL.getGL();
		GL.bindVAO(0);
		
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffersVBO.get(0));
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, Types.SIZEOF_V3F_C4B_T2F * VBO_SIZE, verts, GL2.GL_DYNAMIC_DRAW);
	    
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, quadbuffersVBO.get(0));
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, Types.SIZEOF_V3F_C4B_T2F * VBO_SIZE, null, GL2.GL_DYNAMIC_DRAW);
	    
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buffersVBO.get(1));
		gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, Buffers.SIZEOF_SHORT * INDEX_VBO_SIZE, indices, GL2.GL_STATIC_DRAW);

		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, quadbuffersVBO.get(1));
		gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, Buffers.SIZEOF_SHORT * INDEX_VBO_SIZE, quadIndices, GL2.GL_STATIC_DRAW);
	    
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	/**
	 * 检查矩形是否在屏幕可视范围
	 * @param transform
	 * @param size
	 * @return boolean
	 */
	public boolean checkVisibility(final Mat4 transform, final Size size) {
		return false;
	}
	
}
