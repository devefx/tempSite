package com.devefx.gameengine.renderer;

import com.devefx.gameengine.base.types.BlendFunc;
import com.devefx.gameengine.base.types.V3F_C4B_T2F_Quad;
import com.devefx.gameengine.math.Mat4;
import com.devefx.gameengine.renderer.GLStateCache.GL;

public class QuadCommand extends RenderCommand {
	
	protected int materialID;
	
	protected int textureID;
	
	protected GLProgramState glProgramState;
	
	protected BlendFunc blendType;
	
	protected V3F_C4B_T2F_Quad[] quads;
	
	protected Mat4 mv;
	
	public void init(float globalOrder, int textureID, GLProgramState shader, BlendFunc blendType, Mat4 mv,
			V3F_C4B_T2F_Quad... quads) {
		this.globalOrder = globalOrder;
		this.textureID = textureID;
		this.glProgramState = shader;
		this.blendType = blendType;
		this.mv = mv;
		this.quads = quads;
	}
	
	public int getMaterialID() {
		return materialID;
	}
	
	public V3F_C4B_T2F_Quad[] getQuads() {
		return quads;
	}
	
	public int getQuadCount() {
		return quads.length;
	}
	
	public BlendFunc getBlendType() {
		return blendType;
	}
	
	protected void generateMaterialID() {
		
	}
	
	public void useMaterial() {
		GL.bindTexture2D(textureID);
		
		GL.blendFunc(blendType.src, blendType.dst);
		
		glProgramState.apply(mv);
	}
}
