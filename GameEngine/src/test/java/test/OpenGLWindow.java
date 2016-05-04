package test;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import com.devefx.gameengine.base.types.Types;
import com.devefx.gameengine.base.types.V3F_C4B_T2F;
import com.devefx.gameengine.base.types.V3F_C4B_T2F_Quad;
import com.jogamp.common.nio.Buffers;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;

public class OpenGLWindow {
	
	public static void main(String[] args) throws InterruptedException {
		// getting the capabilities object of GL2 profile
		GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);
		// The Windows
		GLWindow window = GLWindow.create(capabilities);
		window.setSize(800, 600);
		window.setTitle("OpenGL");
		window.setResizable(false);
		window.addGLEventListener(new Renderer());
		window.setVisible(true);
		
		FPSAnimator animator = new FPSAnimator(60);
		animator.setUpdateFPSFrames(60, System.err);
		animator.add(window);
		animator.start();
		
		while (animator.isAnimating() && window.isVisible()) {
			Thread.sleep(1);
		}
		animator.stop();
		window.destroy();
	}
	
	
	
	
	
	static class Renderer implements GLEventListener {
		
		protected IntBuffer quadVAO;
		protected IntBuffer quadbuffersVBO;
		protected ByteBuffer quadVerts;
		protected ShortBuffer quadIndices;
		
		protected V3F_C4B_T2F_Quad quad;
		
		
		public static final boolean supportsShareableVAO = true;
		
		public static final int VBO_SIZE = 65536;
		public static final int INDEX_VBO_SIZE = VBO_SIZE * 6 / 4;
		
		public static final int BATCH_QUADCOMMAND_RESEVER_SIZE = 64;
		public static final int MATERIAL_ID_DO_NOT_BATCH = 0;
		
		public static final int VERTEX_ATTRIB_POSITION	= 0;
		public static final int VERTEX_ATTRIB_COLOR		= 1;
		public static final int VERTEX_ATTRIB_TEX_COORD = 2;
		
		@Override
		public void init(GLAutoDrawable drawable) {
			quadVAO = Buffers.newDirectIntBuffer(1);
			quadbuffersVBO = Buffers.newDirectIntBuffer(2);
			quadVerts = Buffers.newDirectByteBuffer(Types.SIZEOF_V3F_C4B_T2F * VBO_SIZE);
			quadIndices = Buffers.newDirectShortBuffer(Buffers.SIZEOF_SHORT * INDEX_VBO_SIZE);
			
			quad = new V3F_C4B_T2F_Quad();
			quad.bl.vertices.x = 0.25f;
			quad.bl.vertices.y = 0.25f;
			quad.bl.vertices.z = 1f;
			
			quad.br.vertices.x = 0.75f;
			quad.br.vertices.y = 0.25f;
			quad.br.vertices.z = 1f;
			
			quad.tl.vertices.x = 0.25f;
			quad.tl.vertices.y = 0.75f;
			quad.tl.vertices.z = 1f;
			
			quad.tr.vertices.x = 0.75f;
			quad.tr.vertices.y = 0.75f;
			quad.tr.vertices.z = 1f;
			
			
			setupVAOAndVBO();
		}
		
		private void setupVAOAndVBO() {
			final GL2 gl = GLContext.getCurrentGL().getGL2();
			//generate vao for quadCommand
			gl.glGenVertexArrays(1, quadVAO);
			gl.glBindVertexArray(quadVAO.get(0));
			
			// generate vbo for quadCommand
			gl.glGenBuffers(2, quadbuffersVBO);
			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, quadbuffersVBO.get(0));
			gl.glBufferData(GL2.GL_ARRAY_BUFFER, Types.SIZEOF_V3F_C4B_T2F * VBO_SIZE, quadVerts, GL2.GL_DYNAMIC_DRAW);
			
			// vertices
			gl.glEnableVertexAttribArray(VERTEX_ATTRIB_POSITION);
			gl.glVertexAttribPointer(VERTEX_ATTRIB_POSITION, 3, GL2.GL_FLOAT, false, Types.SIZEOF_V3F_C4B_T2F, 0);	// 0
			// colors
			gl.glEnableVertexAttribArray(VERTEX_ATTRIB_COLOR);
			gl.glVertexAttribPointer(VERTEX_ATTRIB_COLOR, 4, GL2.GL_UNSIGNED_BYTE, true, Types.SIZEOF_V3F_C4B_T2F, 12);	// 12
			// tex coords
			gl.glEnableVertexAttribArray(VERTEX_ATTRIB_TEX_COORD);
			gl.glVertexAttribPointer(VERTEX_ATTRIB_TEX_COORD, 2, GL2.GL_FLOAT, false, Types.SIZEOF_V3F_C4B_T2F, 16);	// 16
			
			// generate vio for quadCommand
			gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, quadbuffersVBO.get(1));
			gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, Buffers.SIZEOF_SHORT * INDEX_VBO_SIZE, quadIndices, GL2.GL_STATIC_DRAW);
			
			// Must unbind the VAO before changing the element buffer.
			gl.glBindVertexArray(0);
			gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
		}
		@Override
		public void display(GLAutoDrawable drawable) {
			final GL2 gl = drawable.getGL().getGL2();
			gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			
			// 绑定VAO
			gl.glBindVertexArray(quadVAO.get(0));
			
			// 绑定VBO
			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, quadbuffersVBO.get(0));
			gl.glBufferData(GL2.GL_ARRAY_BUFFER, Types.SIZEOF_V3F_C4B_T2F * 4, null, GL2.GL_DYNAMIC_DRAW);
			// 访问VBO数据
			ByteBuffer buffer = gl.glMapBuffer(GL2.GL_ARRAY_BUFFER, GL2.GL_WRITE_ONLY);
			quad.write(buffer);
			gl.glUnmapBuffer(GL2.GL_ARRAY_BUFFER);
			// 解除VBO绑定
			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
			
			// 绑定VIO
			gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, quadbuffersVBO.get(1));
			
			// 绘制图元
			gl.glDrawElements(GL2.GL_TRIANGLES, 4, GL2.GL_UNSIGNED_SHORT, 0);
			
			gl.glDrawArrays(GL2.GL_QUADS, 0, 4);
			
			// Must unbind the VAO before changing the element buffer.
			gl.glBindVertexArray(0);
			gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
			
			
			gl.glFlush();
		}
		@Override
		public void reshape(GLAutoDrawable drawable, int x, int y, int width,
				int height) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void dispose(GLAutoDrawable drawable) {
			// TODO Auto-generated method stub
			
		}
	}
}
