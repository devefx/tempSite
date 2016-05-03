package com.devefx.test.demo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import sun.misc.Unsafe;

import com.devefx.gamedata.common.TGAUtils;
import com.devefx.gamedata.parser.WASFile;
import com.devefx.gamedata.parser.struct.SequenceFrame;
import com.devefx.test.demo.buffer.Vertex;
import com.devefx.test.demo.buffer.VertexBuffer;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class GLRenderer implements GLEventListener {

	protected IntBuffer buffersVBO = IntBuffer.allocate(1);
	
	protected ByteBuffer meshArraybuffer;
	
	
	
	
	
	
	public static VertexBuffer buffer;
	
	private static int WIDTH = 800;
	private static int HEIGHT = 600;
	
	private float scale = 1.0f;
	private float x, y, rot;
	
	private int primitiveCount = 0;
	
	private IntBuffer colors;
	private FloatBuffer normals;
	private FloatBuffer texCoords;
	private FloatBuffer vertices;
	
	private ShortBuffer indexList;
	
	private int texture;
	
	private static final String directByteBufferClassName = "java.nio.DirectByteBuffer";
	
	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		gl.glClearColor(0.0f, 0.0f, 0.4f, 1.0f);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		
		gl.glClearDepth(1.0f);
		gl.glDepthFunc(GL2.GL_LEQUAL);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL2.GL_BLEND);
		gl.glClearColor(0.0f, 0.0f, 0.4f, 0.0f);
		gl.glViewport(0, 0, WIDTH, HEIGHT);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		
		
		gl.glBindVertexArray(1);
		
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 1);
		
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, 20 * 4, null, GL2.GL_DYNAMIC_DRAW);
		
		ByteBuffer buffer = gl.glMapBuffer(GL2.GL_ARRAY_BUFFER, GL2.GL_WRITE_ONLY);
		
		ByteBuffer buffer2 = ByteBuffer.allocateDirect(36);
		
		try {
			Class<?> s = Class.forName("java.nio.DirectByteBuffer");
			
			System.out.println(buffer.getClass().getName());
			
			if (directByteBufferClassName.equals(buffer.getClass().getName())) {
				
				Field field = Buffer.class.getDeclaredField("address");
				field.setAccessible(true);
				System.out.println(field.get(buffer));
				
				Unsafe unsafe = getUnsafe();
				
				int[] a = new int[5];
				a[1] = 6;
				a[3] = 7;
				int offset = unsafe.arrayBaseOffset(int[].class);
				
				for (long i = 0; i < a.length * 4 + offset; i++) {
					System.out.print(unsafe.getByte(a, i) + " ");
				}
				System.out.println();
				
				long address = unsafe.getLong(buffer2, 16L);
				unsafe.copyMemory(a, offset, null, address, 20);
				
				for (int i = 0; i < 20 + 16; i++) {
					System.out.print(unsafe.getByte(address + i) + " ");
				}
				System.out.println();
				
				
				
				
				
				
				System.out.println(16);
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(buffer);
		//loadTexture("F:\\t.was", gl, 0, 0, true);
		
		loadTexture("f:\\1.jpg", gl, 200, 400, false);
		

	}
	
	public static Unsafe getUnsafe() {
		try {
			Field field = Unsafe.class.getDeclaredField("theUnsafe");
			field.setAccessible(true);
			return (Unsafe) field.get(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public void loadTexture(String filename, GL2 gl, int width, int height, boolean isWas) {
		WASFile was = new WASFile();
		try {
			if (was.open(filename) || isWas == false) {
				
				Texture tex = null;
				if (isWas) {
					SequenceFrame frame = was.getSequenceFrame();
					byte[] buf = TGAUtils.toTGA(frame.pixels, frame.width * frame.frame, frame.height * frame.group);
					InputStream is = new ByteArrayInputStream(buf);
					tex = TextureIO.newTexture(is, true, TextureIO.TGA);
					width = frame.width;
					height = frame.height;
				} else {
					tex = TextureIO.newTexture(new File(filename), true);
				}
				
				if (tex != null) {
					this.texture = tex.getTextureObject(gl);
					float tx = 0;
					float ty = 0;
					
					float x = 100;
					float y = 100;
					float z = 100;
					
					float w = tex.getWidth();
					float h = tex.getHeight();
					
					
					int[] i_colors = new int[] {
						0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF
					};
					
					float p = (h - height) / h;
					float[] f_texCoords = new float[] {
							tx / w, ty / h + p,
							(tx + width) / w, ty / h + p,
							(tx + width) / w, (ty + height) / h + p,
							tx / w, (ty + height) / h + p
					};
					
					float[] f_vertices = new float[] {
						x, y, z,
						x + width, y, z,
						x + width, y + height, z,
						x, y + height, z
					};

					float[] f_normals = new float[] {
						0.0f, 0.0f, 0.0f,
						0.0f, 0.0f, 0.0f,
						0.0f, 0.0f, 0.0f,
						0.0f, 0.0f, 0.0f,
					};
					
					short[] s_index = new short[] {
						(short) 2, (short) 1, (short) 0, (short) 0,
						//(short) 0, (short) 1, (short) 2, (short) 3,
					};
					
					// color
					colors = Buffers.newDirectIntBuffer(i_colors);
					colors.rewind();
					
					// tx ty
					texCoords = Buffers.newDirectFloatBuffer(f_texCoords);
					texCoords.rewind();
					
					// x y z
					vertices = Buffers.newDirectFloatBuffer(f_vertices);
					vertices.rewind();

					// nx ny nz
					normals = Buffers.newDirectFloatBuffer(f_normals);
					normals.rewind();
					
					// index
					indexList = Buffers.newDirectShortBuffer(s_index);
					indexList.rewind();
					
					primitiveCount ++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		// GL2.GL_T2F_C3F_V3F
/*		try {
			Texture tex = TextureIO.newTexture(new File(filename), true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
	
	@Override
	public void dispose(GLAutoDrawable drawable) {

	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		// benScene
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0, WIDTH, 0, HEIGHT, -5000.0f, 5000.0f);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glScalef(scale, scale, 1.0f);
		gl.glTranslatef((WIDTH / 2.0f) - x, (HEIGHT / 2.0f) - y, 0.0f);
		gl.glRotatef(rot, 0.0f, 0.0f, 1.0f);
		gl.glTranslatef(- (WIDTH / 2.0f), - (HEIGHT / 2.0f), 0.0f);
		// clear
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		
		// renderer
		for (int i = 0; i < primitiveCount; i++) {
			
			if (gl.glIsTexture(texture)) {
				gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);
			}
			
			gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
			gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
			
			gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
			
			gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
			

			gl.glColorPointer(4, GL2.GL_UNSIGNED_BYTE, 0, colors);	// col
			gl.glNormalPointer(GL2.GL_FLOAT, 0, normals);			// nx, ny, nz
			gl.glTexCoordPointer(2, GL2.GL_FLOAT, 0, texCoords);	// tx, ty
			gl.glVertexPointer(3, GL2.GL_FLOAT, 0, vertices);		// x, y, z
			
			
			gl.glDrawElements(GL2.GL_QUADS, primitiveCount * 4, GL2.GL_UNSIGNED_SHORT, indexList);
			
			//gl.glDrawElements(GL2.GL_TRIANGLES, primitiveCount * 2 * 3, GL2.GL_UNSIGNED_SHORT, indexList);
			
			
			gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
			gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
			gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
		}
		
		// endScene
		gl.glFlush();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		
		WIDTH = width;
		HEIGHT = height;

	}

}
