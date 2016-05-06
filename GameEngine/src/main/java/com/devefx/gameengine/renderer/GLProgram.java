package com.devefx.gameengine.renderer;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLContext;

public class GLProgram {
	
	protected int program;
	protected int vertexShader;
	protected int fragmentShader;
	
	public boolean init(String vShaderString, String fShaderString) {
		final GL2 gl = GLContext.getCurrentGL().getGL2();
		
		program = gl.glCreateProgram();
		
		if (vShaderString != null) {
			if (!compileShader(GL2.GL_VERTEX_SHADER, vShaderString)) {
				System.err.println("ERROR: Failed to compile vertex shader");
				return false;
			}
		}
		if (fShaderString != null) {
			if (!compileShader(GL2.GL_FRAGMENT_SHADER, fShaderString)) {
				System.err.println("ERROR: Failed to compile vertex shader");
				return false;
			}
		}
		
		gl.glAttachShader(program, vertexShader);
        gl.glAttachShader(program, fragmentShader);
		
		return true;
	}
	
	public int getVertexShader() {
		return vertexShader;
	}
	public int getFragmentShader() {
		return fragmentShader;
	}
	public int getProgram() {
		return program;
	}
	
	public void link() {
		final GL2 gl = GLContext.getCurrentGL().getGL2();
		
		bindPredefinedVertexAttribs();
		
		gl.glLinkProgram(program);
		
		parseVertexAttribs();
		parseUniforms();
		
		if (vertexShader != 0) {
			gl.glDeleteShader(vertexShader);
			vertexShader = 0;
		}
		if (fragmentShader != 0) {
			gl.glDeleteShader(fragmentShader);
			fragmentShader = 0;
		}
	}
	
	public void use() {
		final GL2 gl = GLContext.getCurrentGL().getGL2();
		gl.glUseProgram(program);
	}
	
	protected boolean compileShader(int type, String source) {
		final GL2 gl = GLContext.getCurrentGL().getGL2();
		String[] sources = {
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
		        "uniform sampler2D CC_Texture3;\n",
				source
		};
		// Create GPU shader handles
		int shader = gl.glCreateShader(type);
		// Compile the vertexShader String into a program.
        int[] lengths = new int[] { sources[0].length(), sources[1].length() };
		gl.glShaderSource(shader, sources.length, sources, lengths, 0);
		gl.glCompileShader(shader);
		// Check compile status.
		int[] compiled = new int[1];
        gl.glGetShaderiv(shader, GL2.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == GL2.GL_FALSE) {
        	int[] logLength = new int[1];
        	gl.glGetShaderiv(shader, GL2.GL_INFO_LOG_LENGTH, logLength, 0);
        	byte[] log = new byte[logLength[0]];
        	gl.glGetShaderInfoLog(shader, logLength[0], (int[])null, 0, log, 0);
        	System.err.println("ERROR: Failed to compile shader: " + new String(log));
        	return false;
		}
        return (type == GL2.GL_VERTEX_SHADER && (vertexShader = shader) != 0) ||
        		(type == GL2.GL_FRAGMENT_SHADER && (fragmentShader = shader) != 0);
	}
	
	protected void bindPredefinedVertexAttribs() {
		final GL2 gl = GLContext.getCurrentGL().getGL2();
		gl.glBindAttribLocation(program, VERTEX_ATTRIB_POSITION, ATTRIBUTE_NAME_POSITION);
		gl.glBindAttribLocation(program, VERTEX_ATTRIB_COLOR, ATTRIBUTE_NAME_COLOR);
		gl.glBindAttribLocation(program, VERTEX_ATTRIB_TEX_COORD, ATTRIBUTE_NAME_TEX_COORD);
		gl.glBindAttribLocation(program, VERTEX_ATTRIB_TEX_COORD1, ATTRIBUTE_NAME_TEX_COORD1);
		gl.glBindAttribLocation(program, VERTEX_ATTRIB_TEX_COORD2, ATTRIBUTE_NAME_TEX_COORD2);
		gl.glBindAttribLocation(program, VERTEX_ATTRIB_TEX_COORD3, ATTRIBUTE_NAME_TEX_COORD3);
		gl.glBindAttribLocation(program, VERTEX_ATTRIB_NORMAL, ATTRIBUTE_NAME_NORMAL);
		gl.glBindAttribLocation(program, VERTEX_ATTRIB_BLEND_WEIGHT, ATTRIBUTE_NAME_BLEND_WEIGHT);
		gl.glBindAttribLocation(program, VERTEX_ATTRIB_BLEND_INDEX, ATTRIBUTE_NAME_BLEND_INDEX);
	}
	
	protected void parseVertexAttribs() {
		
	}
	
	protected void parseUniforms() {
		
	}
	
	// Vertex attribute
	public static final int VERTEX_ATTRIB_POSITION = 0;
	public static final int VERTEX_ATTRIB_COLOR = 1;
	public static final int VERTEX_ATTRIB_TEX_COORD = 2;
	public static final int VERTEX_ATTRIB_TEX_COORD1 = 3;
	public static final int VERTEX_ATTRIB_TEX_COORD2 = 4;
	public static final int VERTEX_ATTRIB_TEX_COORD3 = 5;
	public static final int VERTEX_ATTRIB_NORMAL = 6;
	public static final int VERTEX_ATTRIB_BLEND_WEIGHT = 7;
	public static final int VERTEX_ATTRIB_BLEND_INDEX = 8;
	
	// Uniform handle
	public static final int UNIFORM_AMBIENT_COLOR = 0;
	public static final int UNIFORM_P_MATRIX = 1;
	public static final int UNIFORM_MV_MATRIX = 2;
	public static final int UNIFORM_MVP_MATRIX = 3;
	public static final int UNIFORM_NORMAL_MATRIX = 4;
	public static final int UNIFORM_TIME = 5;
	public static final int UNIFORM_SIN_TIME = 6;
	public static final int UNIFORM_COS_TIME = 7;
	public static final int UNIFORM_RANDOM01 = 8;
	public static final int UNIFORM_SAMPLER0 = 9;
	public static final int UNIFORM_SAMPLER1 = 10;
	public static final int UNIFORM_SAMPLER2 = 11;
	public static final int UNIFORM_SAMPLER3 = 12;
	
    /**Ambient Color uniform.*/
    public static final String UNIFORM_NAME_AMBIENT_COLOR = "CC_AmbientColor";
    /**Projection Matrix uniform.*/
    public static final String UNIFORM_NAME_P_MATRIX = "CC_PMatrix";
    /**Model view matrix uniform.*/
    public static final String UNIFORM_NAME_MV_MATRIX = "CC_MVMatrix";
    /**Model view projection uniform.*/
    public static final String UNIFORM_NAME_MVP_MATRIX = "CC_MVPMatrix";
    /**Normal matrix uniform.*/
    public static final String UNIFORM_NAME_NORMAL_MATRIX = "CC_NormalMatrix";
    /**Time uniform.*/
    public static final String UNIFORM_NAME_TIME = "CC_Time";
    /**Sin time uniform.*/
    public static final String UNIFORM_NAME_SIN_TIME = "CC_SinTime";
    /**Cos time uniform.*/
    public static final String UNIFORM_NAME_COS_TIME = "CC_CosTime";
    /**Random number uniform.*/
    public static final String UNIFORM_NAME_RANDOM01 = "CC_Random01";
    /**
     @Sampler uniform 0-3, used for textures.
    */
    public static final String UNIFORM_NAME_SAMPLER0 = "CC_Texture0";
    public static final String UNIFORM_NAME_SAMPLER1 = "CC_Texture1";
    public static final String UNIFORM_NAME_SAMPLER2 = "CC_Texture2";
    public static final String UNIFORM_NAME_SAMPLER3 = "CC_Texture3";
    
    
    /**Attribute color.*/
    public static final String ATTRIBUTE_NAME_COLOR = "a_color";
    /**Attribute position.*/
    public static final String ATTRIBUTE_NAME_POSITION = "a_position";
    /**@Attribute Texcoord 0-3.*/
    public static final String ATTRIBUTE_NAME_TEX_COORD = "a_texCoord";
    public static final String ATTRIBUTE_NAME_TEX_COORD1 = "a_texCoord1";
    public static final String ATTRIBUTE_NAME_TEX_COORD2 = "a_texCoord2";
    public static final String ATTRIBUTE_NAME_TEX_COORD3 = "a_texCoord3";
    /**Attribute normal.*/
    public static final String ATTRIBUTE_NAME_NORMAL = "a_normal";
    /**Attribute blend weight.*/
    public static final String ATTRIBUTE_NAME_BLEND_WEIGHT = "a_blendWeight";
    /**Attribute blend index.*/
    public static final String ATTRIBUTE_NAME_BLEND_INDEX = "a_blendIndex";
}
