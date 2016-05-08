package com.devefx.gameengine.renderer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.devefx.gameengine.base.types.Color4F;
import com.devefx.gameengine.base.types.Types;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLContext;

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
	
	protected boolean isDepthTest;
	protected boolean isRendering;
	
	public static GL2 gl;
	private int attributeFlags = 0;
	
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
		quadVerts = Buffers.newDirectByteBuffer(Types.SIZEOF_V3F_C4B_T2F * VBO_SIZE);
		quadIndices = Buffers.newDirectShortBuffer(Buffers.SIZEOF_SHORT * INDEX_VBO_SIZE);
	}
	
	public void initGLView() {
		gl = GLContext.getCurrentGL().getGL2();
		
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
	
	public void addCommand(RenderCommand command) {
		renderQueue.push(command);
	}
	
	public void render() {
		isRendering = true;
		
		if (glViewAssigned) {
			renderQueue.sort();
			visitRenderQueue(renderQueue);
		}
		
		clean();
		isRendering = false;
	}
	
	public void clean() {
		renderQueue.clear();
		batchQuadCommands.clear();
		numberQuads = 0;
	}
	
	public void clear() {
		gl.glDepthMask(true);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glDepthMask(false);
	}
	
	public void setClearColor(Color4F clearColor) {
		gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
		this.clearColor = clearColor;
	}
	
	public void setDepthTest(boolean enable) {
		if (enable) {
			gl.glClearDepth(1.0f);
			gl.glEnable(GL.GL_DEPTH_TEST);
		} else {
			gl.glDisable(GL.GL_DEPTH_TEST);
		}
		isDepthTest = enable;
	}
	
	protected void setupBuffer() {
		if (supportsShareableVAO) {
			setupVBOAndVAO();
		} else {
			setupVBO();
		}
	}
	
	protected void setupVBOAndVAO() {
		// generate vao for trianglesCommand
		gl.glGenVertexArrays(1, buffersVAO);
		gl.glBindVertexArray(buffersVAO.get(0));
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
		gl.glBindVertexArray(0);
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
		
		//generate vao for quadCommand
		gl.glGenVertexArrays(1, quadVAO);
		gl.glBindVertexArray(quadVAO.get(0));
		
		// generate vbo for quadCommand
		gl.glGenBuffers(2, quadbuffersVBO);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, quadbuffersVBO.get(0));
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, Types.SIZEOF_V3F_C4B_T2F * VBO_SIZE, quadVerts, GL2.GL_DYNAMIC_DRAW);
		
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
		gl.glBindVertexArray(0);
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
	}
	
	protected void setupVBO() {
		gl.glGenBuffers(2, buffersVBO);
		gl.glGenBuffers(2, quadbuffersVBO);
		mapBuffers();
	}
	
	protected void mapBuffers() {
		gl.glBindVertexArray(0);
		
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffersVBO.get(0));
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, Types.SIZEOF_V3F_C4B_T2F * VBO_SIZE, verts, GL2.GL_DYNAMIC_DRAW);
	    
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, quadbuffersVBO.get(0));
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, Types.SIZEOF_V3F_C4B_T2F * VBO_SIZE, quadVerts, GL2.GL_DYNAMIC_DRAW);
	    
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buffersVBO.get(1));
		gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, Buffers.SIZEOF_SHORT * INDEX_VBO_SIZE, indices, GL2.GL_STATIC_DRAW);

		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, quadbuffersVBO.get(1));
		gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, Buffers.SIZEOF_SHORT * INDEX_VBO_SIZE, quadIndices, GL2.GL_STATIC_DRAW);
	    
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	protected void flush() {
		flush2D();
		flush3D();
	}
	
	protected void flush2D() {
		flushQuads();
		flushTriangles();
	}
	
	protected void flush3D() {
		
	}
	
	protected void flushQuads() {
		if (numberQuads > 0) {
			drawBatchedQuads();
		}
	}
	
	protected void flushTriangles() {
		
	}
	
	protected void visitRenderQueue(RenderQueue queue) {
		queue.saveRenderState();
		
		if (queue.size() > 0) {
			if (isDepthTest) {
				gl.glEnable(GL.GL_DEPTH_TEST);
				gl.glDepthMask(true);
			} else {
				gl.glDisable(GL.GL_DEPTH_TEST);
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
	
	protected void processRenderCommand(RenderCommand command) {
		
		if (command instanceof QuadCommand) {
			
			QuadCommand cmd = (QuadCommand) command;
			
			if (cmd.isSkipBatching() || cmd.getQuadCount() * 4 > VBO_SIZE) {
				// Draw batched quads if VBO is full
				drawBatchedQuads();
			}
			
			fillQuads(cmd);
			
			batchQuadCommands.add(cmd);
			
			if (cmd.isSkipBatching()) {
				drawBatchedQuads();
			}
		}
		
	}
	
	protected void fillQuads(QuadCommand cmd) {
		
		
		
		cmd.getQuads();
		
		
		numberQuads += cmd.getQuadCount();
	}
	
	protected void drawBatchedQuads() {
		
		if (batchQuadCommands.isEmpty()) {
			return;
		}

		if (supportsShareableVAO) {
			// 绑定VAO
			gl.glBindVertexArray(quadVAO.get(0));
			// 绑定VBO
			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, quadbuffersVBO.get(0));
			gl.glBufferData(GL2.GL_ARRAY_BUFFER, Types.SIZEOF_V3F_C4B_T2F_QUAD * numberQuads, null, GL2.GL_DYNAMIC_DRAW);
			// 提交VBO数据
			ByteBuffer buffer = gl.glMapBuffer(GL2.GL_ARRAY_BUFFER, GL2.GL_WRITE_ONLY);
			for (QuadCommand cmd : batchQuadCommands) {
				cmd.getQuads()[0].write(buffer);
			}
			gl.glUnmapBuffer(GL2.GL_ARRAY_BUFFER);
			// 解除VBO绑定
			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
		} else {
			// 绑定VBO
			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, quadbuffersVBO.get(0));
			gl.glBufferData(GL2.GL_ARRAY_BUFFER, Types.SIZEOF_V3F_C4B_T2F_QUAD * numberQuads, quadVerts, GL2.GL_DYNAMIC_DRAW);
			
			enableVertexAttribs(7);
			
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
			if (gl.glIsTexture(cmd.textureID)) {
				gl.glBindTexture(GL2.GL_TEXTURE_2D, cmd.textureID);
			}
		}
		gl.glDrawElements(GL2.GL_TRIANGLES, numberQuads * 6, GL2.GL_UNSIGNED_SHORT, 0);
		
		if (supportsShareableVAO) {
			gl.glBindVertexArray(0);
		} else {
			gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
		}
	}
	
	protected void enableVertexAttribs(int flags) {
		gl.glBindVertexArray(0);
		
		final int MAX_ATTRIBUTES = 16;
		for (int i = 0; i < MAX_ATTRIBUTES; i++) {
			int bit = 1 << i;
			boolean enabled = (flags & bit) != 0;
			boolean enabledBefore = (attributeFlags & bit) != 0;
			if(enabled != enabledBefore) {
				if (enabled) {
					gl.glEnableVertexAttribArray(i);
				} else {
					gl.glDisableVertexAttribArray(i);
				}
			}
		}
		attributeFlags = flags;
	}
	
}
