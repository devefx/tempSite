package com.devefx.test.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import com.devefx.gameengine.base.types.Color4B;
import com.devefx.gameengine.base.types.Types;
import com.devefx.gameengine.base.types.V3F_C4B_T2F_Quad;
import com.devefx.gameengine.base.types.Vec3;
import com.devefx.gameengine.renderer.GLProgram;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.gl2.GLUgl2;

public class Triangle implements GLEventListener {

	// VBO对象集合
	protected IntBuffer buffersVBO = IntBuffer.allocate(2);
	protected IntBuffer buffersVAO = IntBuffer.allocate(1);
	
	protected FloatBuffer quadVerts;
	protected ShortBuffer quadIndices;
	
	protected V3F_C4B_T2F_Quad quad;
	
	public static String readFile(String filename) throws IOException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream is = loader.getResourceAsStream(filename);
		byte[] buf = new byte[is.available()];
		is.read(buf);
		return new String(buf);
	}
	
	float[] glMatrixLoadIdentity() {
		return new float[] {
			1.0f, 0.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 0.0f, 0.0f,
			0.0f, 0.0f, 1.0f, 0.0f,
			0.0f, 0.0f, 0.0f, 1.0f,
		};
	}
	
	
	@Override
	public void init(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		
		float[] vertexs = {
				0.0f, 0.0f, 1, 0, 0, 0,
				0.5f, 0.0f, 1, 0, 0, 0,
				0.0f, 0.5f, 1, 0, 0, 0,
				0.5f, 0.5f, 1, 0, 0, 0,
		};
		
		short[] indices = new short[] {
				0, 1, 2, 3, 2, 1
		};
		
		
		quad = new V3F_C4B_T2F_Quad();

		quad.bl.vertices = new Vec3(10, 10, 1);
		quad.bl.colors = new Color4B(255, 0, 0, 255);
		
		quad.br.vertices = new Vec3(40, 10, 1);
		quad.br.colors = new Color4B(0, 255, 0, 255);
		
		quad.tl.vertices = new Vec3(10, 40, 1);
		quad.tl.colors = new Color4B(0, 0, 255, 255);
		
		quad.tr.vertices = new Vec3(40, 40, 1);
		quad.tr.colors = new Color4B(255, 255, 0, 255);
		
		
		quadVerts = FloatBuffer.allocate(vertexs.length/* * Buffers.SIZEOF_FLOAT*/);
		for (int i = 0; i < vertexs.length; i++) {
			quadVerts.put(vertexs[i]);
		}
		quadVerts.rewind();
		
		ShortBuffer quadIndices = ShortBuffer.allocate(indices.length);
		for (int i = 0; i < indices.length; i++) {
			quadIndices.put(indices[i]);
		}
		quadIndices.rewind();
		
		try {
			GLProgram glProgram = new GLProgram();
			glProgram.init(readFile("test.vert"), readFile("test.frag"));
			glProgram.link();
			glProgram.use();
			
			float[] matrix = {
					0.02f,  0.0f,   0.0f, 0.0f,
					 0.0f, 0.02f,   0.0f, 0.0f,
					 0.0f,  0.0f, -0.01f, 0.0f,
					-1.0f, -1.0f,   0.0f, 1.0f
				};
			
			//gl.glOrthof(0, 100, 0, 100, -100, 100);
			//gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, matrix, 0);
			
			int a = gl.glGetUniformLocation(glProgram.getProgram(), "CC_MVPMatrix");
			gl.glUniformMatrix4fv(a, 1, false, matrix, 0);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		gl.glGenVertexArrays(1, buffersVAO);
		gl.glBindVertexArray(buffersVAO.get(0));
		// 开辟一个vbo
		gl.glGenBuffers(2, buffersVBO);
		
		// 绑定vbos中的vbo[0]对象
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffersVBO.get(0));
		// 把buffer拷贝到vbo(显卡)中  
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, Types.SIZEOF_V3F_C4B_T2F_QUAD,
				null, GL2.GL_STATIC_DRAW);
		
		gl.glEnableVertexAttribArray(0);
		gl.glVertexAttribPointer(0, 3, GL2.GL_FLOAT, false, Types.SIZEOF_V3F_C4B_T2F, 0);
		
		
		
		
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buffersVBO.get(1));
		gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, 6 * Buffers.SIZEOF_SHORT,
				quadIndices, GL2.GL_STATIC_DRAW);
		
		
		// Must unbind the VAO before changing the element buffer.
		gl.glBindVertexArray(0);
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
		
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		
		gl.glBindVertexArray(buffersVAO.get(0));
		
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffersVBO.get(0));
		// 把buffer拷贝到vbo(显卡)中  
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, Types.SIZEOF_V3F_C4B_T2F_QUAD,
				null, GL2.GL_DYNAMIC_DRAW);
		ByteBuffer buffer = gl.glMapBuffer(GL2.GL_ARRAY_BUFFER, GL2.GL_WRITE_ONLY);
		quad.write(buffer);
		gl.glUnmapBuffer(GL2.GL_ARRAY_BUFFER);
		
		
		gl.glDrawElements(GL2.GL_TRIANGLES, 6, GL2.GL_UNSIGNED_SHORT, 0);
		
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	
	public static void main1(String[] args) throws UnsupportedEncodingException {
		String s = "这是一个水群";
		
		byte[] b = s.getBytes("gbk"); // gbk比utf-8更小
		
		ByteBuffer buf = ByteBuffer.allocate(b.length); // int 为 4 bytes
		buf.put(b);
		buf.rewind();
		
		buf.order(ByteOrder.nativeOrder());
		IntBuffer intBuffer = buf.asIntBuffer();
		
		int len = intBuffer.capacity();
		int[] val = new int[len];
		for (int i = 0; i < len; i++) {
			val[i] = intBuffer.get();
			System.out.print(val[i] + ",");
		}
		System.out.println();
		// int[]转String
		ByteBuffer buffer = ByteBuffer.allocate(val.length * 4);
		for (int i = 0; i < val.length; i++) {
			int v = val[i];
			buffer.put((byte) ((v >>  0) & 0xff));
			buffer.put((byte) ((v >>  8) & 0xff));
			buffer.put((byte) ((v >> 16) & 0xff));
			buffer.put((byte) ((v >> 24) & 0xff));
		}
		buffer.rewind();
		
		byte[] dst = new byte[buffer.capacity()];
		buffer.get(dst);
		
		String text = new String(dst, "gbk");
		
		System.out.println(text);
	}
	
}
