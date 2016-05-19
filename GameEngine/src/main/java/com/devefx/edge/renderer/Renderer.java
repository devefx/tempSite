package com.devefx.edge.renderer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import com.devefx.edge.base.types.Color4F;
import com.devefx.edge.base.types.Size;
import com.devefx.edge.base.types.Types;
import com.devefx.edge.base.types.V3F_C4B_T2F_Quad;
import com.devefx.edge.math.Mat4;
import com.devefx.edge.renderer.GLStateCache.GL;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;

public class Renderer {
	
	public static final boolean supportsShareableVAO = true;
	
	
	public static final int VBO_SIZE = 65536;
	public static final int INDEX_VBO_SIZE = VBO_SIZE * 6 / 4;
	
	public static final int DEFAULT_RENDER_QUEUE = 0;
	public static final int BATCH_QUADCOMMAND_RESEVER_SIZE = 64;
	public static final int MATERIAL_ID_DO_NOT_BATCH = 0;
	
	public static final int VERTEX_ATTRIB_POSITION	= 0;
	public static final int VERTEX_ATTRIB_COLOR		= 1;
	public static final int VERTEX_ATTRIB_TEX_COORD = 2;
	
	protected Color4F clearColor;

	protected Stack<Integer> commandGroupStack;
	protected List<RenderQueue> renderGroups;
	
	// for QuadCommand
	protected List<QuadCommand> batchQuadCommands;
	protected IntBuffer quadVAO;
	protected IntBuffer quadbuffersVBO;
	protected ByteBuffer quadVerts;
	protected ShortBuffer quadIndices;
	protected int numberQuads;
	
	protected boolean glViewAssigned;
	
	protected int drawnBatches;
	protected int drawnVertices;
	
	protected boolean isRendering;
	protected boolean isDepthTestFor2D;
	
	protected int lastMaterialID;
	
	public Renderer() {
		commandGroupStack = new Stack<Integer>();
		commandGroupStack.push(DEFAULT_RENDER_QUEUE);
		
		renderGroups = new ArrayList<RenderQueue>();
		renderGroups.add(new RenderQueue());
		
		batchQuadCommands = new ArrayList<QuadCommand>(BATCH_QUADCOMMAND_RESEVER_SIZE);
		clearColor = new Color4F(Color4F.BLACK);
		
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
		addCommand(command, commandGroupStack.firstElement());
	}
	/**
	 * 添加‘RenderCommand’到渲染队列
	 * @param command
	 * @param renderQueue
	 */
	public void addCommand(RenderCommand command, int renderQueue) {
		assert(!isRendering) : "Cannot add command while rendering";
		assert(renderQueue > 0) : "Invalid render queue";
		assert(command == null) : "command should not be null";
		
		renderGroups.get(0).push(command);
	}
	/**
	 * 添加一个渲染组ID
	 * @param renderQueueID 
	 */
	public void pushGroup(int renderQueueID) {
		assert(!isRendering) : "Cannot add command while rendering";
		commandGroupStack.push(renderQueueID);
	}
	/**
	 * 移除一个渲染组ID
	 */
	public void popGroup() {
		assert(!isRendering) : "Cannot add command while rendering";
		commandGroupStack.pop();
	}
	/**
	 * 创建一个渲染组
	 * @return int 渲染组ID
	 */
	public int createRenderQueue() {
		renderGroups.add(new RenderQueue());
		return renderGroups.size() - 1;
	}
	/**
	 * 处理渲染命令
	 * @param command
	 */
	protected void processRenderCommand(RenderCommand command) {
		
		if (command instanceof QuadCommand) {
			// Process quad command
			QuadCommand cmd = (QuadCommand) command;
			// Draw batched quads if necessary
			if (cmd.isSkipBatching() || (numberQuads + cmd.getQuadCount()) * 4 > VBO_SIZE) {
				// Draw batched quads if VBO is full
				drawBatchedQuads();
			}
			// Batch Quads
			batchQuadCommands.add(cmd);
			
			fillQuads(cmd);
			
			if (cmd.isSkipBatching()) {
				drawBatchedQuads();
			}
		}
	}
	/**
	 * 访问渲染队列
	 * @param queue 
	 */
	protected void visitRenderQueue(RenderQueue queue) {
		queue.saveRenderState();
		
		GL2 gl = GL.getGL();
		if (queue.size() > 0) {
			if (isDepthTestFor2D) {
				gl.glEnable(GL2.GL_DEPTH_TEST);
				gl.glDepthMask(true);
			} else {
				gl.glDisable(GL2.GL_DEPTH_TEST);
				gl.glDepthMask(false);
			}
			Iterator<RenderCommand> it = queue.getQueue();
			while (it.hasNext()) {
				processRenderCommand(it.next());
			}
			flush();
		}
		
		queue.restoreRenderState();
	}
	/**
	 * 绘画全部‘RenderCommand’
	 */
	public void render() {
		isRendering = true;
		
		if (glViewAssigned) {
			for (RenderQueue queue : renderGroups) {
				queue.sort();
			}
			visitRenderQueue(renderGroups.get(0));
		}
		clean();
		isRendering = false;
	}
	/**
	 * 添加QuadCommand到顶点数据
	 * @param cmd
	 */
	protected void fillQuads(QuadCommand cmd) {
		final Mat4 modelView = cmd.getModelView();
		
		quadVerts.position(Types.SIZEOF_V3F_C4B_T2F_QUAD * numberQuads);
		for (V3F_C4B_T2F_Quad quad : cmd.getQuads()) {
			quad = quad.clone();
			modelView.transformPoint(quad.bl.vertices, quad.bl.vertices);
			modelView.transformPoint(quad.br.vertices, quad.br.vertices);
			modelView.transformPoint(quad.tl.vertices, quad.tl.vertices);
			modelView.transformPoint(quad.tr.vertices, quad.tr.vertices);
			quad.write(quadVerts);
		}
		quadVerts.rewind();
		
		numberQuads += cmd.getQuadCount();
	}
	/**
	 * 批量渲染Quad
	 */
	protected void drawBatchedQuads() {
		
		int indexToDraw = 0;
	    int startIndex = 0;
		
	    if(numberQuads <= 0 || batchQuadCommands.isEmpty()) {
	    	return;
	    }
	    
	    GL2 gl = GL.getGL();
	    
	    if (supportsShareableVAO) {
			// 绑定VAO
			GL.bindVAO(quadVAO.get(0));
			// 绑定VBO
			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, quadbuffersVBO.get(0));
			gl.glBufferData(GL2.GL_ARRAY_BUFFER, Types.SIZEOF_V3F_C4B_T2F_QUAD * numberQuads, quadVerts, GL2.GL_DYNAMIC_DRAW);
			// 解除VBO绑定
			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
		} else {
			// 绑定VBO
			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, quadbuffersVBO.get(0));
			gl.glBufferData(GL2.GL_ARRAY_BUFFER, Types.SIZEOF_V3F_C4B_T2F_QUAD * numberQuads, quadVerts, GL2.GL_DYNAMIC_DRAW);
			// 开启顶点属性
			GL.enableVertexAttribs(GL.VERTEX_ATTRIB_FLAG_POS_COLOR_TEX);
			// vertices
			gl.glVertexAttribPointer(VERTEX_ATTRIB_POSITION, 3, GL2.GL_FLOAT, false, Types.SIZEOF_V3F_C4B_T2F, 0);
	        // colors
			gl.glVertexAttribPointer(VERTEX_ATTRIB_COLOR, 4, GL2.GL_UNSIGNED_BYTE, true, Types.SIZEOF_V3F_C4B_T2F, 12);
	        // tex coords
			gl.glVertexAttribPointer(VERTEX_ATTRIB_TEX_COORD, 2, GL2.GL_FLOAT, false, Types.SIZEOF_V3F_C4B_T2F, 16);
			// 绑定VIO
			gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, quadbuffersVBO.get(1));
		}
	    // Start drawing verties in batch
	    for (QuadCommand cmd : batchQuadCommands) {
			
			int newMaterialID = cmd.getMaterialID();
			if (lastMaterialID != newMaterialID || newMaterialID == MATERIAL_ID_DO_NOT_BATCH) {
				// Draw quads
				if (indexToDraw > 0) {
					gl.glDrawElements(GL2.GL_TRIANGLES, indexToDraw, GL2.GL_UNSIGNED_SHORT, startIndex * Buffers.SIZEOF_SHORT);
					drawnBatches ++;
					drawnVertices += indexToDraw;
					
					startIndex += indexToDraw;
					indexToDraw = 0;
				}
				// Use new material
				cmd.useMaterial();
				lastMaterialID = newMaterialID;
			}
			
			indexToDraw += cmd.getQuadCount() * 6;
		}
	    
		if (indexToDraw > 0) {
			gl.glDrawElements(GL2.GL_TRIANGLES, indexToDraw, GL2.GL_UNSIGNED_SHORT, startIndex * Buffers.SIZEOF_SHORT);
			drawnBatches ++;
			drawnVertices += indexToDraw;
		}
		
		if (supportsShareableVAO) {
			GL.bindVAO(0);
		} else {
			gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
		}
	    
	    batchQuadCommands.clear();
	    numberQuads = 0;
	}
	/**
	 * 清理全部‘RenderCommand’
	 */
	public void clean() {
		for (RenderQueue queue : renderGroups) {
			queue.clear();
		}
		batchQuadCommands.clear();
		numberQuads = 0;
		lastMaterialID = 0;
	}
	/**
	 * 清屏
	 */
	public void clear() {
		final GL2 gl = GL.getGL();
		gl.glDepthMask(true);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glDepthMask(false);
	}
	/**
	 * 清除绘画状态
	 */
	public void clearDrawStats() {
		drawnBatches = 0;
		drawnVertices = 0;
	}
	/**
	 * 设置清屏颜色
	 * @param clearColor 
	 */
	public void setClearColor(final Color4F clearColor) {
		this.clearColor = clearColor;
		GL.getGL().glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
	}
	/**
	 * 启用或停用深度测试
	 * @param enable
	 */
	public void setDepthTest(boolean enable) {
		final GL2 gl = GL.getGL();
		if (enable) {
			gl.glClearDepthf(1.0f);
			gl.glEnable(GL2.GL_DEPTH_TEST);
			gl.glDepthFunc(GL2.GL_LEQUAL);
		} else {
			gl.glDisable(GL2.GL_DEPTH_TEST);
		}
		isDepthTestFor2D = enable;
	}
	/**
	 * 刷新
	 */
	protected void flush() {
		flush2D();
		flush3D();
	}
	/**
	 * 刷新2D
	 */
	protected void flush2D() {
		flushQuads();
	}
	/**
	 * 刷新3D
	 */
	protected void flush3D() {
	}
	/**
	 * 刷新四边形
	 */
	protected void flushQuads() {
		if (numberQuads > 0) {
			
			lastMaterialID = 0;
		}
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
		gl.glGenBuffers(2, quadbuffersVBO);
		mapBuffers();
	}
	/**
	 * 映射VBO
	 */
	protected void mapBuffers() {
		GL2 gl = GL.getGL();
		GL.bindVAO(0);
		
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, quadbuffersVBO.get(0));
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, Types.SIZEOF_V3F_C4B_T2F * VBO_SIZE, null, GL2.GL_DYNAMIC_DRAW);
	    
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
