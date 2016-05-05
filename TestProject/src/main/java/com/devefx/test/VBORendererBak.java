package com.devefx.test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.devefx.gameengine.base.types.Color4B;
import com.devefx.gameengine.base.types.Types;
import com.devefx.gameengine.base.types.V3F_C4B_T2F;
import com.devefx.gameengine.base.types.V3F_C4B_T2F_Quad;
import com.devefx.test.demo.quad.Quad;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public class VBORendererBak implements GLEventListener {

	static int VERTEX_ATTRIB_POSITION = 0;
	static int VERTEX_ATTRIB_COLOR = 1;
	static int MVP_MATRIX = 0;
	
	GLProgram glProgram = new GLProgram();
	
	// 由数组meshArray转换成的缓存buffer 
	protected FloatBuffer meshArraybuffer;
	
	// vao
	protected IntBuffer quadVAO = IntBuffer.allocate(1);
	// VBO/VIO对象集合
	protected IntBuffer buffersVBO = IntBuffer.allocate(2);
	
	protected IntBuffer indices;
	
	protected int texture;
	
	protected int BUFFER_SIZE = 7;
	
	float[] array;
	
	V3F_C4B_T2F_Quad quad = new V3F_C4B_T2F_Quad();
	
	boolean isQuad = false;
	
	@Override
	public void init(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		// 设置背景颜色  
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
		// 视点大小
		gl.glViewport(0, 0, 100, 100);
	//	gl.glMatrixMode(GL2.GL_PROJECTION);
	//	gl.glLoadIdentity();
		// 裁剪横坐标
		gl.glOrtho(0, 200, 0, 100, -100, 100);
		
		quad.bl.vertices.x = 10;
		quad.bl.vertices.y = 10;
		quad.bl.vertices.z = 1;
		quad.bl.colors = new Color4B(255, 255, 0, 255);
		
		quad.br.vertices.x = 25;
		quad.br.vertices.y = 20;
		quad.br.vertices.z = 1;
		quad.br.colors = new Color4B(0, 255, 0, 255);
		
		quad.tl.vertices.x = 20;
		quad.tl.vertices.y = 30;
		quad.tl.vertices.z = 1;
		quad.tl.colors = new Color4B(0, 255, 0, 255);
		
		quad.tr.vertices.x = 40;
		quad.tr.vertices.y = 40;
		quad.tr.vertices.z = 1;
		quad.tr.colors = new Color4B(255, 255, 0, 255);
		
		//[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -128, 63, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
		
		// 数组，包含了meshArray.length/2对二维坐标
		array = new float[] {
				10, 10, 1,   1, 1, 0, 1,
				25, 20, 1,   0, 1, 0, 1,
				20, 30, 1,   0, 1, 0, 1,
			    
				40, 40, 1,   1, 1, 0, 1,
				50, 50, 1,   1, 1, 0, 1,
				50, 60, 1,   0, 1, 0, 1,
			    
				72, 70, 1,   1, 0, 0, 1,
				80, 80, 1,   1, 0, 0, 1,
				80, 90, 1,   1, 0, 0, 1,
		};
		
		
		// 把数组的元素按顺序逐个放进buffer中
		meshArraybuffer = FloatBuffer.allocate(array.length);
		
		// 顶点索引
		int[] int_array = {
			0, 1, 2,
			3, 4, 5,
			6, 7, 8
		};
		indices = IntBuffer.allocate(int_array.length);
		for (int i = 0; i < int_array.length; i++) {
			indices.put(int_array[i]);
		}
		indices.rewind();
		
		initCreateProgram(gl);
		
		setupBuffer(gl);
	}

	int vertShader;
	int fragShader;
	int shaderProgram;
	
	String source = 
			"uniform mat4 CC_PMatrix;\n" +
	        "uniform mat4 CC_MVMatrix;\n" +
	        "uniform mat4 CC_MVPMatrix;\n" +
	        "uniform mat3 CC_NormalMatrix;\n" +
	        "uniform vec4 CC_Time;\n" +
	        "uniform vec4 CC_SinTime;\n" +
	        "uniform vec4 CC_CosTime;\n" +
	        "uniform vec4 CC_Random01;\n"  +
	        "uniform sampler2D CC_Texture0;\n" +
	        "uniform sampler2D CC_Texture1;\n" +
	        "uniform sampler2D CC_Texture2;\n" +
	        "uniform sampler2D CC_Texture3;\n";
	
	private String vertexShaderString;
	
	private String fragmentShaderString;
	
	
	String readFile(String filename) throws IOException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream is = loader.getResourceAsStream(filename);
		byte[] buf = new byte[is.available()];
		is.read(buf);
		return new String(buf);
	}
	
	float[] glOrtho(float[] matrix, float left, float right, float bottom, float top, float nearZ, float farZ) {
		float deltaX = right - left;
		float deltaY = top - bottom;
		float deltaZ = farZ - nearZ;
		if ((deltaX == 0.0f ) || (deltaY == 0.0f) || (deltaZ == 0.0f)) {
			return matrix;
		}
		float[] ortho = glMatrixLoadIdentity();
		ortho[0] = 2.0f / deltaX;
		ortho[12] = -(right + left) / deltaX;//ortho[3] = -(right + left) / deltaX;
		ortho[5] = 2.0f / deltaY;
		ortho[13] = -(top + bottom) / deltaY;//ortho[7] = -(top + bottom) / deltaY;
		ortho[10] = -2.0f / deltaZ;
		ortho[11] = -(nearZ + farZ) / deltaZ;
		return glMatrixMultiply(ortho, matrix);
	}
	
	float[] glMatrixMultiply(float[] srcA, float[] srcB) {
		float[] tmp = new float[16];
		for (int i = 0; i < 4; i++ ) {
			tmp[i + 0] = (srcA[i +  0] * srcB[0]) +
                    	 (srcA[i +  4] * srcB[1]) +
                    	 (srcA[i +  8] * srcB[2]) +
                    	 (srcA[i + 12] * srcB[3]);
			
			tmp[i + 4] = (srcA[i +  0] * srcB[4]) +
                    	 (srcA[i +  4] * srcB[5]) +
                    	 (srcA[i +  8] * srcB[6]) +
                    	 (srcA[i + 12] * srcB[7]);
			
			tmp[i + 8] = (srcA[i +  0] * srcB[8]) +
                    	 (srcA[i +  4] * srcB[9]) +
                    	 (srcA[i +  8] * srcB[10]) +
                    	 (srcA[i + 12] * srcB[11]) ;

			tmp[i +12] = (srcA[i +  0] * srcB[12]) +
                    	 (srcA[i +  4] * srcB[13]) +
                    	 (srcA[i +  8] * srcB[14]) +
                    	 (srcA[i + 12] * srcB[15]) ;
		}
		return tmp;
	}
	
	float[] glMatrixLoadIdentity() {
		return new float[] {
			1.0f, 0.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 0.0f, 0.0f,
			0.0f, 0.0f, 1.0f, 0.0f,
			0.0f, 0.0f, 0.0f, 1.0f,
		};
	}
	
	
	void initCreateProgram(GL2 gl) {
		
		try {
			vertexShaderString = readFile("shader_PositionTextureColor.vert");
			fragmentShaderString = readFile("shader_PositionTextureColor.frag");
		} catch (Exception e) {
		}
		
		if (gl.isGL3core()) {
			vertexShaderString = "#version 130\n"+vertexShaderString;
            fragmentShaderString = "#version 130\n"+fragmentShaderString;
		}
		
		if (glProgram.init(vertexShaderString, fragmentShaderString)) {
			glProgram.link();
			glProgram.use();
			
			VERTEX_ATTRIB_POSITION = GLProgram.VERTEX_ATTRIB_POSITION;
			VERTEX_ATTRIB_COLOR = GLProgram.VERTEX_ATTRIB_COLOR;
			MVP_MATRIX = GLProgram.UNIFORM_AMBIENT_COLOR;
		}
		
		/*
		// Create GPU shader handles
        // OpenGL ES retuns a index id to be stored for future reference.
		vertShader = gl.glCreateShader(GL2.GL_VERTEX_SHADER);
		fragShader = gl.glCreateShader(GL2.GL_FRAGMENT_SHADER);
		
		// Compile the vertexShader String into a program.
        String[] vlines = new String[] { source, vertexShaderString };
        int[] vlengths = new int[] { vlines[0].length(), vlines[1].length() };
        gl.glShaderSource(vertShader, vlines.length, vlines, vlengths, 0);
        gl.glCompileShader(vertShader);
        
        // Check compile status.
        int[] compiled = new int[1];
        gl.glGetShaderiv(vertShader, GL2.GL_COMPILE_STATUS, compiled,0);
        if (compiled[0]!=0) {
        	System.out.println("Horray! vertex shader compiled");
        } else {
            int[] logLength = new int[1];
            gl.glGetShaderiv(vertShader, GL2.GL_INFO_LOG_LENGTH, logLength, 0);
            byte[] log = new byte[logLength[0]];
            gl.glGetShaderInfoLog(vertShader, logLength[0], (int[])null, 0, log, 0);

            System.err.println("Error compiling the vertex shader: " + new String(log));
            System.exit(1);
        }
        
        // Compile the fragmentShader String into a program.
        String[] flines = new String[] { source, fragmentShaderString };
        int[] flengths = new int[] { flines[0].length(), flines[1].length() };
        gl.glShaderSource(fragShader, flines.length, flines, flengths, 0);
        gl.glCompileShader(fragShader);

        // Check compile status.
        gl.glGetShaderiv(fragShader, GL2.GL_COMPILE_STATUS, compiled,0);
        if (compiled[0]!=0) {
        	System.out.println("Horray! fragment shader compiled");
        } else {
            int[] logLength = new int[1];
            gl.glGetShaderiv(fragShader, GL2.GL_INFO_LOG_LENGTH, logLength, 0);
            byte[] log = new byte[logLength[0]];
            gl.glGetShaderInfoLog(fragShader, logLength[0], (int[])null, 0, log, 0);

            System.err.println("Error compiling the fragment shader: " + new String(log));
            System.exit(1);
        }
       
        //Each shaderProgram must have
        //one vertex shader and one fragment shader.
        shaderProgram = gl.glCreateProgram();
        gl.glAttachShader(shaderProgram, vertShader);
        gl.glAttachShader(shaderProgram, fragShader);
        
        
        //Associate attribute ids with the attribute names inside
        //the vertex shader.
        gl.glBindAttribLocation(shaderProgram, 0, "a_position");
        gl.glBindAttribLocation(shaderProgram, 1, "a_texCoord");
        gl.glBindAttribLocation(shaderProgram, 2, "a_color");
        
        gl.glLinkProgram(shaderProgram);
		
        // Check link status
        int[] linked = new int[1];
        gl.glGetProgramiv(shaderProgram, GL2.GL_LINK_STATUS, linked, 0);
        if (linked[0]!=0) {
        	System.out.println("Horray! program linked");
		} else {
			int[] logLength = new int[1];
	        gl.glGetProgramiv(shaderProgram, GL2.GL_INFO_LOG_LENGTH, logLength, 0);
	        byte[] log = new byte[logLength[0]];
	        gl.glGetProgramInfoLog(shaderProgram, logLength[0], null, 0, log, 0);
	        
	        System.err.println("Error link the program: " + new String(log));
	        System.exit(1);
		}
        
        gl.glDeleteShader(vertShader);
        gl.glDeleteShader(fragShader);
        
        VERTEX_ATTRIB_POSITION = gl.glGetAttribLocation(shaderProgram, "a_position");
        VERTEX_ATTRIB_COLOR = gl.glGetAttribLocation(shaderProgram, "a_color");
        MVP_MATRIX = gl.glGetUniformLocation(shaderProgram, "CC_MVPMatrix");
        
        gl.glUseProgram(shaderProgram);
        
		float[] m = new float[16];
        gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, m, 0);
        */
        float[] matrix = glOrtho(glMatrixLoadIdentity(), 0f, 100f, 0f, 100f, -100f, 100f);
        gl.glUniformMatrix4fv(MVP_MATRIX, 1, false, matrix, 0);
        
        System.out.println(gl.glGetError());
	}
	
	void setupBuffer(GL2 gl) {
		// 开辟一个vao
		gl.glGenVertexArrays(1, quadVAO);
		gl.glBindVertexArray(quadVAO.get(0));
		
		// 开辟一个vbo
		gl.glGenBuffers(2, buffersVBO);
		// 绑定vbos中的vbo[0]对象
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffersVBO.get(0));
		// 把buffer拷贝到vbo(显卡)中  
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, meshArraybuffer.capacity() * Buffers.SIZEOF_FLOAT,
				meshArraybuffer, GL2.GL_STATIC_DRAW);
		
		if (isQuad) {
			// vertices
			gl.glEnableVertexAttribArray(VERTEX_ATTRIB_POSITION);
			gl.glVertexAttribPointer(VERTEX_ATTRIB_POSITION, 3, GL2.GL_FLOAT, false, Types.SIZEOF_V3F_C4B_T2F, 0);
			
			// colors
			gl.glEnableVertexAttribArray(VERTEX_ATTRIB_COLOR);
			gl.glVertexAttribPointer(VERTEX_ATTRIB_COLOR, 4, GL2.GL_UNSIGNED_BYTE, true, Types.SIZEOF_V3F_C4B_T2F, 12);
		} else {
			// vertices
			gl.glEnableVertexAttribArray(VERTEX_ATTRIB_POSITION);
			gl.glVertexAttribPointer(VERTEX_ATTRIB_POSITION, 3, GL2.GL_FLOAT, false, Buffers.SIZEOF_FLOAT * BUFFER_SIZE, 0);
			
			// colors
			gl.glEnableVertexAttribArray(VERTEX_ATTRIB_COLOR);
			gl.glVertexAttribPointer(VERTEX_ATTRIB_COLOR, 4, GL2.GL_FLOAT, true, Buffers.SIZEOF_FLOAT * BUFFER_SIZE, 12);
		}
		
		// 开辟一个vio
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buffersVBO.get(1));
		gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, Buffers.SIZEOF_INT * 9, indices, GL2.GL_STATIC_DRAW);
		
		// unbind vao/vbo/vio
		gl.glBindVertexArray(0);
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
	}
	
	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		// 从GLAutoDrawable获取GL
		final GL2 gl = drawable.getGL().getGL2();
		// 填充背景颜色
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		
		
		// 绑定vao
		gl.glBindVertexArray(quadVAO.get(0));
		// 绑定vbo
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffersVBO.get(0));
		// 提交数据
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, meshArraybuffer.capacity() * Buffers.SIZEOF_FLOAT, null, GL2.GL_DYNAMIC_DRAW);
		ByteBuffer buffer = gl.glMapBuffer(GL2.GL_ARRAY_BUFFER, GL2.GL_WRITE_ONLY);
		
		if (isQuad) {
			quad.write(buffer);
			buffer.rewind();
		} else {
			for (int i = 0; i < array.length; i++) {
				buffer.putFloat(array[i]);
			}
		}
		
		gl.glUnmapBuffer(GL2.GL_ARRAY_BUFFER);
		// 绑定vio
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buffersVBO.get(1));
		// 绘制图形
		//gl.glDrawElements(GL2.GL_TRIANGLES, meshArraybuffer.capacity() / BUFFER_SIZE, GL2.GL_UNSIGNED_INT, 0);
		
		gl.glDrawElements(GL2.GL_QUADS, 4, GL2.GL_UNSIGNED_INT, 0);
		
		// 解除绑定
		gl.glBindVertexArray(0);
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
	}
}
