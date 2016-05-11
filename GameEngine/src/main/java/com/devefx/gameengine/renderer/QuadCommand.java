package com.devefx.gameengine.renderer;

import java.util.UUID;

import com.devefx.gameengine.base.types.BlendFunc;
import com.devefx.gameengine.base.types.V3F_C4B_T2F_Quad;
import com.devefx.gameengine.math.Mat4;
import com.devefx.gameengine.renderer.GLStateCache.GL;

public class QuadCommand extends RenderCommand {
	
	protected int materialID;
	
	protected int textureID;
	
	protected GLProgramState glProgramState;
	
	protected BlendFunc blendType;
	
	protected V3F_C4B_T2F_Quad quads;
	
	protected Mat4 mv;
	
	public void init(float globalOrder, int textureID, GLProgramState shader, BlendFunc blendType, Mat4 mv,
			V3F_C4B_T2F_Quad quads) {
		this.globalOrder = globalOrder;
		this.textureID = textureID;
		this.glProgramState = shader;
		this.blendType = blendType;
		this.mv = mv;
		this.quads = quads;
		
		generateMaterialID();
	}
	
	public int getMaterialID() {
		return materialID;
	}
	
	public int getTextureID() {
		return textureID;
	}
	
	public V3F_C4B_T2F_Quad getQuads() {
		return quads;
	}
	
	public int getQuadCount() {
		return 1;
	}
	
	public GLProgramState getGlProgramState() {
		return glProgramState;
	}
	
	public BlendFunc getBlendType() {
		return blendType;
	}
	
	public Mat4 getModelView() {
		return mv;
	}
	
	protected void generateMaterialID() {
		
		int glprogram = glProgramState.getGLProgram().getProgram();
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(glprogram);
		buffer.append(textureID);
		buffer.append(blendType.src);
		buffer.append(blendType.dst);
		materialID = Integer.parseInt(buffer.toString());
		
	}
	
	public void useMaterial() {
		GL.bindTexture2D(textureID);
		
		GL.blendFunc(blendType.src, blendType.dst);
		
		glProgramState.apply(mv);
	}
}
