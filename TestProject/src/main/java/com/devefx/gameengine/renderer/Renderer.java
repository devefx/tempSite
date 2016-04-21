package com.devefx.gameengine.renderer;

import com.devefx.gameengine.base.Color.Color4F;
import com.devefx.gameengine.math.Mat4;
import com.devefx.gameengine.math.Size;

public interface Renderer {
	
	public void initGLView();
	
	public void addCommand(RenderCommand command);
	
	public void addCommand(RenderCommand command, int renderQueue);
	
	public void pushGroup(int renderQueueID);
	
	public void popGroup();
	
	public int createRenderQueue();
	
	public void render();
	
	public void clean();
	
	public void clear();
	
	public void setClearColor(Color4F clearColor);
	
	public int getDrawnBatches();
	
	public void addDrawnBatches(int number);
	
	public int getDrawnVertices();
	
	public void addDrawnVertices(int number);
	
	public void clearDrawStats();
	
	public void setDepthTest(boolean enable);
	
	public GroupCommandManager getGroupCommandManager();
	
	public boolean checkVisibility(Mat4 transform, Size size);
	
	// Setup VBO or VAO based on OpenGL extensions
	void setupBuffer();
	void setupVBOAndVAO();
	void setupVBO();
	void mapBuffers();
	void drawBatchedTriangles();
	void drawBatchedQuads();
	
	// Draw the previews queued quads and flush previous context
	void flush();
	
	void flush2D();
	
	void flush3D();
	
	void flushQuads();
	void flushTriangles();
	
	void processRenderCommand(RenderCommand command);
	void visitRenderQueue(RenderQueue queue);
	
	void fillVerticesAndIndices(final TrianglesCommand cmd);
	void fillQuads(final QuadCommand cmd);
}
