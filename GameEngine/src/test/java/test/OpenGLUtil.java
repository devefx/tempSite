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
import com.devefx.gameengine.renderer.GLProgram;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class OpenGLUtil {
	
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

	public OpenGLUtil() {
		glProgram = new GLProgram();
		
		quadVAO = Buffers.newDirectIntBuffer(1);
		quadbuffersVBO = Buffers.newDirectIntBuffer(2);
		quadVerts = ByteBuffer.allocate(Types.SIZEOF_V3F_C4B_T2F * VBO_SIZE);
		quadIndices = ShortBuffer.allocate(INDEX_VBO_SIZE);
	}
	
	protected void initData() {
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
		
		float x = 0.01f;
		float y = 0.01f;
		
		// setup quad data
		quad = new V3F_C4B_T2F_Quad();
		
		quad.bl.vertices = new Vec3(10 * x, 10 * y, 1);
		quad.bl.colors = new Color4B(255, 0, 0, 255);
		
		quad.br.vertices = new Vec3(40 * x, 10 * y, 1);
		quad.br.colors = new Color4B(0, 255, 0, 255);
		
		quad.tl.vertices = new Vec3(10 * x, 40 * y, 1);
		quad.tl.colors = new Color4B(0, 0, 255, 255);
		
		quad.tr.vertices = new Vec3(40 * x, 40 * y, 1);
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
	
	protected void initCreateProgram() throws IOException {
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
		gl.glUniformMatrix4fv(GLProgram.UNIFORM_AMBIENT_COLOR, 1, false, matrix, 0);
		
	}
	
	protected void setupVAOAndVBO() {
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
	
}
