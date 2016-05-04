package com.devefx.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class VBORendererBak implements GLEventListener {

	static final int VERTEX_ATTRIB_POSITION = 0;
	static final int VERTEX_ATTRIB_COLOR = 1;
	
	
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
	
	@Override
	public void init(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		final GLU glu = new GLU();
		// 设置背景颜色  
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
		// 视点大小
		gl.glViewport(0, 0, 100, 100);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		// 裁剪横坐标
		glu.gluOrtho2D(0f, 100f, 0f, 100f);
		
		
		// 数组，包含了meshArray.length/2对二维坐标
		array = new float[] {
				/*1, 1, 0, 1, 0, */10, 10, 1, 255, 255, 0, 255,
				/*1, 1, 0, 1, 0, */25, 20, 1,   0, 255, 0, 255,
				/*1, 1, 0, 1, 0, */20, 30, 1,   0, 255, 0, 255,
			    
				/*1, 1, 1, 1, 0, */40, 40, 1, 255, 255, 0, 255,
				/*1, 1, 1, 1, 0, */50, 50, 1, 255, 255, 0, 255,
				/*1, 1, 0, 1, 0, */50, 60, 1,   0, 255, 0, 255,
			    
				/*1, 1, 1, 0, 0, */72, 70, 1, 255,   0, 0, 255,
				/*1, 1, 1, 0, 0, */80, 80, 1, 255,   0, 0, 255,
				/*1, 1, 1, 0, 0, */80, 90, 1, 255,   0, 0, 255,
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
	int ModelViewProjectionMatrix_location;
	
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
	        "uniform sampler2D CC_Texture3;\n" +
	        "//CC INCLUDES END\n\n";
	
	String vertexShaderString;
	
	String fragmentShaderString;
	
	static final int i = 1282;
	
	String readFile(String filename) throws IOException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream is = loader.getResourceAsStream(filename);
		byte[] buf = new byte[is.available()];
		is.read(buf);
		return new String(buf);
	}
	
	void initCreateProgram(GL2 gl) {
		
		try {
			vertexShaderString = readFile("shader_PositionTextureColor.vert");
			fragmentShaderString = readFile("shader_PositionTextureColor.vert");
		} catch (Exception e) {
		}
		
		if (gl.isGL3core()) {
			vertexShaderString = "#version 130\n"+vertexShaderString;
            fragmentShaderString = "#version 130\n"+fragmentShaderString;
		}
		
		// Create GPU shader handles
        // OpenGL ES retuns a index id to be stored for future reference.
		vertShader = gl.glCreateShader(GL2.GL_VERTEX_SHADER);
		fragShader = gl.glCreateShader(GL2.GL_FRAGMENT_SHADER);
		
		// Compile the vertexShader String into a program.
        String[] vlines = new String[] { source, vertexShaderString };
        int[] vlengths = new int[] { vlines[0].length() };
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
        int[] flengths = new int[] { flines[0].length() };
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
        	System.out.println("Horray! fragment link program");
		} else {
			int[] logLength = new int[1];
	        gl.glGetProgramiv(shaderProgram, GL2.GL_INFO_LOG_LENGTH, logLength, 0);
	        byte[] log = new byte[logLength[0]];
	        gl.glGetProgramInfoLog(shaderProgram, logLength[0], null, 0, log, 0);
	        
	        System.err.println("Error link the program: " + new String(log));
	        System.exit(1);
		}
        
        
        System.out.println(gl.glGetError());
        gl.glUseProgram(shaderProgram); 
        System.out.println(gl.glGetError());
	
        
        //Get a id number to the uniform_Projection matrix
        //so that we can update it.
        ModelViewProjectionMatrix_location = gl.glGetUniformLocation(shaderProgram, "CC_MVMatrix");
        
     //   System.out.println(ModelViewProjectionMatrix_location);
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
		
		// vertices
		gl.glEnableVertexAttribArray(VERTEX_ATTRIB_POSITION);
		gl.glVertexAttribPointer(VERTEX_ATTRIB_POSITION, 3, GL2.GL_FLOAT, false, Buffers.SIZEOF_FLOAT * BUFFER_SIZE, 0);
		
		// colors
		gl.glEnableVertexAttribArray(VERTEX_ATTRIB_COLOR);
		gl.glVertexAttribPointer(VERTEX_ATTRIB_COLOR, 4, GL2.GL_FLOAT, true, Buffers.SIZEOF_FLOAT * BUFFER_SIZE, 12);

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
		for (int i = 0; i < array.length; i++) {
			buffer.putFloat(array[i]);
		}
		gl.glUnmapBuffer(GL2.GL_ARRAY_BUFFER);
		// 绑定vio
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buffersVBO.get(1));
		// 绘制图形
		gl.glDrawElements(GL2.GL_TRIANGLES, meshArraybuffer.capacity() / BUFFER_SIZE, GL2.GL_UNSIGNED_INT, 0);
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
