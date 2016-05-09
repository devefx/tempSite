package com.devefx.gameengine.renderer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.devefx.gameengine.base.Director;
import com.devefx.gameengine.base.Director.MatrixStackType;
import com.devefx.gameengine.math.Mat4;
import com.devefx.gameengine.renderer.GLStateCache.GL;
import com.devefx.gameengine.resources.Resources;
import com.jogamp.opengl.GL2;

public class GLProgram {
	
	protected int program;
	protected int vertexShader;
	protected int fragmentShader;
	
	protected int[] builtInUniforms;
	protected Map<String, VertexAttrib> vertexAttribs;
	protected Map<String, Uniform> userUniforms;
	
	protected UniformFlag flag = new UniformFlag();
	
	protected Map<Integer, Object> hashForUniforms;
	
	protected Director director;
	
	public static GLProgram createWithString(String vShaderString, String fShaderString) {
		GLProgram program = new GLProgram();
		if (program.initWithString(vShaderString, fShaderString)) {
			program.link();
			program.updateUniforms();
			return program;
		}
		return null;
	}
	
	public static GLProgram createWithFilename(String vShaderFilename, String fShaderFilename) {
		GLProgram program = new GLProgram();
		if (program.initWithFilename(vShaderFilename, fShaderFilename)) {
			program.link();
			program.updateUniforms();
			return program;
		}
		return null;
	}
	
	public GLProgram() {
		program = 0;
		vertexShader = 0;
		fragmentShader = 0;
		
		builtInUniforms = new int[UNIFORM_MAX];
		vertexAttribs = new HashMap<String, GLProgram.VertexAttrib>();
		userUniforms = new HashMap<String, GLProgram.Uniform>();
		
		hashForUniforms = new HashMap<Integer, Object>();
		
		director = Director.getInstance();
		assert(director != null);
	}
	
	public boolean initWithString(String vShaderString, String fShaderString) {
		final GL2 gl = GL.getGL();
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
        
        hashForUniforms.clear();
		return true;
	}
	
	public boolean initWithFilename(String vShaderFilename, String fShaderFilename) {
		try {
			return initWithString(Resources.getResourceAsString(vShaderFilename),
					Resources.getResourceAsString(fShaderFilename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	protected void bindPredefinedVertexAttribs() {
		final GL2 gl = GL.getGL();
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
		final GL2 gl = GL.getGL();
		
		int[] activeAttributes = new int[1];
		gl.glGetProgramiv(program, GL2.GL_ACTIVE_ATTRIBUTES, activeAttributes, 0);
		if (activeAttributes[0] > 0) {
			int[] length = new int[1];
			gl.glGetProgramiv(program, GL2.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH, length, 0);
			if (length[0] > 0) {
				byte[] attrName = new byte[length[0]];
				int[] lengthAndSizeAndType = new int[3];
				for (int i = 0; i < activeAttributes[0]; i++) {
					gl.glGetActiveAttrib(program, i, length[0], lengthAndSizeAndType, 0, lengthAndSizeAndType, 1, lengthAndSizeAndType, 2, attrName, 0);
					VertexAttrib vertexAttrib = new VertexAttrib();
					vertexAttrib.size = lengthAndSizeAndType[1];
					vertexAttrib.type = lengthAndSizeAndType[2];
					vertexAttrib.name = new String(attrName, 0, lengthAndSizeAndType[0]);
					vertexAttrib.index = gl.glGetAttribLocation(program, vertexAttrib.name);
					vertexAttribs.put(vertexAttrib.name, vertexAttrib);
				}
			}
		} else {
			int[] length = new int[1];
			byte[] errorLog = new byte[1024]; 
			gl.glGetProgramInfoLog(program, 1024, length, 0, errorLog, 0);
			System.err.println("Error linking shader program: " + new String(errorLog, 0, length[0]));
		}
	}
	
	protected void parseUniforms() {
		final GL2 gl = GL.getGL();
		
		int[] activeUniforms = new int[1];
		gl.glGetProgramiv(program, GL2.GL_ACTIVE_UNIFORMS, activeUniforms, 0);
		if (activeUniforms[0] > 0) {
			int[] length = new int[1];
			gl.glGetProgramiv(program, GL2.GL_ACTIVE_UNIFORM_MAX_LENGTH, length, 0);
			if (length[0] > 0) {
				byte[] uniformName = new byte[length[0]];
				int[] lengthAndSizeAndType = new int[3];
				for (int i = 0; i < activeUniforms[0]; i++) {
					gl.glGetActiveUniform(program, i, length[0], lengthAndSizeAndType, 0, lengthAndSizeAndType, 1, lengthAndSizeAndType, 2, uniformName, 0);
					Uniform uniform = new Uniform();
					uniform.size = lengthAndSizeAndType[1];
					uniform.type = lengthAndSizeAndType[2];
					uniform.name = new String(uniformName, 0, lengthAndSizeAndType[0]);
					uniform.location = gl.glGetUniformLocation(program, uniform.name);
					userUniforms.put(uniform.name, uniform);
				}
			}
		} else {
			int[] length = new int[1];
			byte[] errorLog = new byte[1024]; 
			gl.glGetProgramInfoLog(program, 1024, length, 0, errorLog, 0);
			System.err.println("Error linking shader program: " + new String(errorLog, 0, length[0]));
		}
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
	
	public Uniform getUniform(String key) {
		return userUniforms.get(key);
	}
	
	public VertexAttrib getVertexAttrib(String key) {
		return vertexAttribs.get(key);
	}
	
	protected boolean compileShader(int type, String source) {
		final GL2 gl = GL.getGL();
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

	public int getAttribLocation(String attributName) {
		return GL.getGL().glGetAttribLocation(program, attributName);
	}
	public int getUniformLocation(String uniformName) {
		return GL.getGL().glGetUniformLocation(program, uniformName);
	}
	public void bindAttribLocation(String attributeName, int index) {
		GL.getGL().glBindAttribLocation(program, index, attributeName);
	}
	public void updateUniforms() {
		builtInUniforms[UNIFORM_AMBIENT_COLOR] = getUniformLocation(UNIFORM_NAME_AMBIENT_COLOR);
	    builtInUniforms[UNIFORM_P_MATRIX] = getUniformLocation(UNIFORM_NAME_P_MATRIX);
	    builtInUniforms[UNIFORM_MV_MATRIX] = getUniformLocation(UNIFORM_NAME_MV_MATRIX);
	    builtInUniforms[UNIFORM_MVP_MATRIX] = getUniformLocation(UNIFORM_NAME_MVP_MATRIX);
	    builtInUniforms[UNIFORM_NORMAL_MATRIX] = getUniformLocation(UNIFORM_NAME_NORMAL_MATRIX);
	    
	    builtInUniforms[UNIFORM_TIME] = getUniformLocation(UNIFORM_NAME_TIME);
	    builtInUniforms[UNIFORM_SIN_TIME] = getUniformLocation(UNIFORM_NAME_SIN_TIME);
	    builtInUniforms[UNIFORM_COS_TIME] = getUniformLocation(UNIFORM_NAME_COS_TIME);

	    builtInUniforms[UNIFORM_RANDOM01] = getUniformLocation(UNIFORM_NAME_RANDOM01);

	    builtInUniforms[UNIFORM_SAMPLER0] = getUniformLocation(UNIFORM_NAME_SAMPLER0);
	    builtInUniforms[UNIFORM_SAMPLER1] = getUniformLocation(UNIFORM_NAME_SAMPLER1);
	    builtInUniforms[UNIFORM_SAMPLER2] = getUniformLocation(UNIFORM_NAME_SAMPLER2);
	    builtInUniforms[UNIFORM_SAMPLER3] = getUniformLocation(UNIFORM_NAME_SAMPLER3);
	    
	    flag.usesP = builtInUniforms[UNIFORM_P_MATRIX] != -1;
	    flag.usesMV = builtInUniforms[UNIFORM_MV_MATRIX] != -1;
	    flag.usesMVP = builtInUniforms[UNIFORM_MVP_MATRIX] != -1;
	    flag.usesNormal = builtInUniforms[UNIFORM_NORMAL_MATRIX] != -1;
	    flag.usesTime = (builtInUniforms[UNIFORM_TIME] != -1 ||
	    		builtInUniforms[UNIFORM_SIN_TIME] != -1 ||
	    		builtInUniforms[UNIFORM_COS_TIME] != -1);
	    flag.usesRandom = builtInUniforms[UNIFORM_RANDOM01] != -1;
	    
	    this.use();
	    
	    if (builtInUniforms[UNIFORM_SAMPLER0] != -1) {
	    	setUniformLocationWith1i(builtInUniforms[UNIFORM_SAMPLER0], 0);
		}
	    if (builtInUniforms[UNIFORM_SAMPLER1] != -1) {
	    	setUniformLocationWith1i(builtInUniforms[UNIFORM_SAMPLER1], 1);
	    }
	    if (builtInUniforms[UNIFORM_SAMPLER2] != -1) {
	    	setUniformLocationWith1i(builtInUniforms[UNIFORM_SAMPLER2], 2);
	    }
	    if (builtInUniforms[UNIFORM_SAMPLER3] != -1) {
	    	setUniformLocationWith1i(builtInUniforms[UNIFORM_SAMPLER3], 3);
	    }
	}
	
	public void link() {
		GL2 gl = GL.getGL();
		
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
		GL.useProgram(program);
	}
	
	public boolean updateUniformLocation(int location, Object data) {
		if (location > -1) {
			Object element = hashForUniforms.get(location);
			if (element != data) {
				hashForUniforms.put(location, data);
				return true;
			}
		}
		return false;
	}
	
	public void setUniformLocationWith1i(int location, int i1) {
		if (updateUniformLocation(location, i1)) {
			GL.getGL().glUniform1i(location, i1);
		}
	}
	
	public void setUniformLocationWith1f(int location, float f1) {
		if (updateUniformLocation(location, f1)) {
			GL.getGL().glUniform1f(location, f1);
		}
	}
	
	public void setUniformLocationWith2f(int location, float f1, float f2) {
		if (updateUniformLocation(location, new float[] { f1, f2 })) {
			GL.getGL().glUniform2f(location, f1, f2);
		}
	}
	
	public void setUniformLocationWith3f(int location, float f1, float f2, float f3) {
		if (updateUniformLocation(location, new float[] { f1, f2, f3 })) {
			GL.getGL().glUniform3f(location, f1, f2, f3);
		}
	}
	
	public void setUniformLocationWith4f(int location, float f1, float f2, float f3, float f4) {
		if (updateUniformLocation(location, new float[] { f1, f2, f3, f4 })) {
			GL.getGL().glUniform4f(location, f1, f2, f3, f4);
		}
	}
	
	public void setUniformLocationWithMatrix3fv(int location, float[] matrixArray, int numberOfMatrices) {
		if (updateUniformLocation(location, matrixArray)) {
			GL.getGL().glUniformMatrix3fv(location, numberOfMatrices, false, matrixArray, 0);
		}
	}
	
	public void setUniformLocationWithMatrix4fv(int location, float[] matrixArray, int numberOfMatrices) {
		if (updateUniformLocation(location, matrixArray)) {
			GL.getGL().glUniformMatrix4fv(location, numberOfMatrices, false, matrixArray, 0);
		}
	}
	
	public void setUniformsForBuiltins() {
		setUniformsForBuiltins(director.getMatrix(MatrixStackType.MATRIX_STACK_MODELVIEW));
	}
	
	public void setUniformsForBuiltins(Mat4 matrixMV) {
		Mat4 matrixP = director.getMatrix(MatrixStackType.MATRIX_STACK_PROJECTION);
		if(flag.usesP) {
			setUniformLocationWithMatrix4fv(builtInUniforms[UNIFORM_P_MATRIX], matrixP.m, 1);
		}
		if(flag.usesMV) {
			setUniformLocationWithMatrix4fv(builtInUniforms[UNIFORM_MV_MATRIX], matrixMV.m, 1);
		}
		if(flag.usesMVP) {
			Mat4 matrixMVP = Mat4.multiplyMatrix(matrixP, matrixMV);
			setUniformLocationWithMatrix4fv(builtInUniforms[UNIFORM_MVP_MATRIX], matrixMVP.m, 1);
		}
		
		if (flag.usesNormal) {
			// TODO
		}
		
		if (flag.usesTime) {
			// TODO
		}
		
		if (flag.usesRandom) {
			// TODO
		}
	}
	
	public void reset() {
		program = 0;
		vertexShader = 0;
		fragmentShader = 0;
		builtInUniforms = new int[UNIFORM_MAX];
		hashForUniforms.clear();
	}
	
	public class VertexAttrib {
		int index;
		int size;
		int type;
		String name;
	}
	
	public class Uniform {
		int location;
		int size;
		int type;
		String name;
	}
	
	class UniformFlag {
		boolean usesTime;
		boolean usesNormal;
		boolean usesMVP;
		boolean usesMV;
		boolean usesP;
		boolean usesRandom;
	}
	// Shader name
	public static final String SHADER_NAME_POSITION_TEXTURE_COLOR = "ShaderPositionTextureColor";
	public static final String SHADER_NAME_POSITION_TEXTURE_COLOR_NO_MVP = "ShaderPositionTextureColor_noMVP";
	public static final String SHADER_NAME_POSITION_TEXTURE = "ShaderPositionTexture";
	
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
	public static final int UNIFORM_MAX = 13;
	
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
