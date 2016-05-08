package test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import com.devefx.gameengine.base.types.Color4B;
import com.devefx.gameengine.base.types.Tex2F;
import com.devefx.gameengine.base.types.Types;
import com.devefx.gameengine.base.types.V3F_C4B_T2F_Quad;
import com.devefx.gameengine.base.types.Vec3;
import com.devefx.gameengine.math.Mat4;
import com.devefx.gameengine.renderer.GLProgram;
import com.jogamp.common.nio.Buffers;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

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
		
		final FPSAnimator animator = new FPSAnimator(60);
		//animator.setUpdateFPSFrames(60, System.err);
		animator.add(window);
		animator.start();
		
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowDestroyed(WindowEvent e) {
				animator.stop();
			}
		});
	}
	
	static class Renderer implements GLEventListener {
		
		protected GLProgram glProgram;
		
		protected IntBuffer quadVAO;
		protected IntBuffer quadbuffersVBO;
		protected ByteBuffer quadVerts;
		protected ShortBuffer quadIndices;
		
		protected int texture;
		protected V3F_C4B_T2F_Quad quad;
		
		public static final boolean supportsShareableVAO = true;
		
		public static final int VBO_SIZE = 65536;
		public static final int INDEX_VBO_SIZE = VBO_SIZE * 6 / 4;
		
		public static final int BATCH_QUADCOMMAND_RESEVER_SIZE = 64;
		public static final int MATERIAL_ID_DO_NOT_BATCH = 0;
		
		@Override
		public void init(GLAutoDrawable drawable) {
			glProgram = new GLProgram();
			 
			quadVAO = Buffers.newDirectIntBuffer(1);
			quadbuffersVBO = Buffers.newDirectIntBuffer(2);
			quadVerts = ByteBuffer.allocate(Types.SIZEOF_V3F_C4B_T2F * VBO_SIZE);
			quadIndices = ShortBuffer.allocate(INDEX_VBO_SIZE);
			
			initData();
			
			setupVAOAndVBO();
			
			try {
				initCreateProgram();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void initData() {
			// setup index data for quads
			for (int i = 0; i < VBO_SIZE / 4; i++) {
				quadIndices.put((short) (i * 4 + 0));
				quadIndices.put((short) (i * 4 + 1));
				quadIndices.put((short) (i * 4 + 2));
				quadIndices.put((short) (i * 4 + 3));
				quadIndices.put((short) (i * 4 + 2));
				quadIndices.put((short) (i * 4 + 1));
			}
			quadIndices.rewind();
			
			// setup quad data
			quad = new V3F_C4B_T2F_Quad();
			
			quad.bl.vertices = new Vec3(10, 10, 1);
			quad.bl.colors = new Color4B(255, 0, 0, 255);
			
			quad.br.vertices = new Vec3(40, 10, 1);
			quad.br.colors = new Color4B(0, 255, 0, 255);
			
			quad.tl.vertices = new Vec3(10, 40, 1);
			quad.tl.colors = new Color4B(0, 0, 255, 255);
			
			quad.tr.vertices = new Vec3(40, 40, 1);
			quad.tr.colors = new Color4B(255, 255, 0, 255);
			
			try {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				URL url = loader.getResource("1.jpg");
				
				Texture tex = TextureIO.newTexture(new File(url.getPath()), true);
				texture = tex.getTextureObject();
				
				quad.bl.texCoords = new Tex2F(0, 0);
				quad.br.texCoords = new Tex2F(1, 0);
				quad.tl.texCoords = new Tex2F(0, 1);
				quad.tr.texCoords = new Tex2F(1, 1);
				
			} catch (GLException | IOException e) {
				e.printStackTrace();
			}
		}
		
		private void initCreateProgram() throws IOException {
			final GL2 gl = GLContext.getCurrentGL().getGL2();
			
			if (glProgram.initWithFilename("shader_PositionTextureColor.vert", "shader_PositionTextureColor.frag")) {
				glProgram.link();
				glProgram.use();
			}
			
			float[] matrix = {
					0.02f,  0.0f,   0.0f, 0.0f,
					 0.0f, 0.02f,   0.0f, 0.0f,
					 0.0f,  0.0f, -0.01f, 0.0f,
					-1.0f, -1.0f,   0.0f, 1.0f
				};
			
			gl.glOrtho(0, 800, 0, 600, -1024, 1024);
			
			float[] m = new float[16];
	        gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, m, 0);
	        
	        Mat4 mat4 = new Mat4();
	        Mat4.createOrthographicOffCenter(0, 800, 0, 600, -1024, 1024, mat4);
	        
			int location = gl.glGetUniformLocation(glProgram.getProgram(), "CC_MVPMatrix");
			gl.glUniformMatrix4fv(location, 1, false, mat4.m, 0);
		}
		
		private void setupVAOAndVBO() {
			final GL2 gl = GLContext.getCurrentGL().getGL2();
			//generate vao for quadCommand
			gl.glGenVertexArrays(1, quadVAO);
			gl.glBindVertexArray(quadVAO.get(0));
			
			// generate vbo for quadCommand
			gl.glGenBuffers(2, quadbuffersVBO);
			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, quadbuffersVBO.get(0));
			gl.glBufferData(GL2.GL_ARRAY_BUFFER, Types.SIZEOF_V3F_C4B_T2F * VBO_SIZE, quadVerts, GL2.GL_STATIC_DRAW);
			
			// vertices
			gl.glEnableVertexAttribArray(GLProgram.VERTEX_ATTRIB_POSITION);
			gl.glVertexAttribPointer(GLProgram.VERTEX_ATTRIB_POSITION, 3, GL2.GL_FLOAT, false, Types.SIZEOF_V3F_C4B_T2F, 0);
			// colors
			gl.glEnableVertexAttribArray(GLProgram.VERTEX_ATTRIB_COLOR);
			gl.glVertexAttribPointer(GLProgram.VERTEX_ATTRIB_COLOR, 4, GL2.GL_UNSIGNED_BYTE, true, Types.SIZEOF_V3F_C4B_T2F, 12);
			// tex coords
			gl.glEnableVertexAttribArray(GLProgram.VERTEX_ATTRIB_TEX_COORD);
			gl.glVertexAttribPointer(GLProgram.VERTEX_ATTRIB_TEX_COORD, 2, GL2.GL_FLOAT, false, Types.SIZEOF_V3F_C4B_T2F, 16);
			
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
			gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
			
			
			// 绑定纹理
			if (gl.glIsTexture(texture)) {
				gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);
			}
			// 绑定VAO
			gl.glBindVertexArray(quadVAO.get(0));
			// 绑定VBO
			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, quadbuffersVBO.get(0));
			gl.glBufferData(GL2.GL_ARRAY_BUFFER, Types.SIZEOF_V3F_C4B_T2F_QUAD, null, GL2.GL_DYNAMIC_DRAW);
			// 访问VBO数据
			ByteBuffer buffer = gl.glMapBuffer(GL2.GL_ARRAY_BUFFER, GL2.GL_WRITE_ONLY);
			quad.write(buffer);
			gl.glUnmapBuffer(GL2.GL_ARRAY_BUFFER);
			// 解除VBO绑定
			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
			// 绘制图元
			gl.glDrawElements(GL2.GL_TRIANGLES, 6, GL2.GL_UNSIGNED_SHORT, 0);
			// 解除VAO绑定
			gl.glBindVertexArray(0);
			
			
			gl.glFlush();
		}
		@Override
		public void reshape(GLAutoDrawable drawable, int x, int y, int width,
				int height) {
			final GL2 gl = drawable.getGL().getGL2();
			gl.glViewport(0, 0, width, height);
			
		}
		@Override
		public void dispose(GLAutoDrawable drawable) {
			
		}
	}
}
