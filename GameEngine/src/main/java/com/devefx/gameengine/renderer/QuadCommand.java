package com.devefx.gameengine.renderer;

import com.devefx.gameengine.base.Director;
import com.devefx.gameengine.base.types.BlendFunc;
import com.devefx.gameengine.base.types.V3F_C4B_T2F_Quad;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

public class QuadCommand extends RenderCommand {
	
	protected int materialID;
	
	protected int textureID;
	
	protected BlendFunc blendType;
	
	protected V3F_C4B_T2F_Quad[] quads;
	
	public void init(float globalOrder, int textureID, BlendFunc blendType, V3F_C4B_T2F_Quad... quads) {
		this.globalOrder = globalOrder;
		this.textureID = textureID;
		this.blendType = blendType;
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
		GL gl = Director.getInstance().getGL();
		if (gl.glIsTexture(textureID)) {
			gl.glBindTexture(GL2.GL_TEXTURE_2D, textureID);
		}
		
		
	}
}
