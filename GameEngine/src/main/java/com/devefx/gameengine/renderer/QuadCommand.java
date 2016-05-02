package com.devefx.gameengine.renderer;

import com.devefx.gameengine.base.types.BlendFunc;
import com.devefx.gameengine.base.types.V3F_C4B_T2F_Quad;

public class QuadCommand extends RenderCommand {
	
	protected int materialID;
	
	protected int textureID;
	
	protected BlendFunc blendType;
	
	protected V3F_C4B_T2F_Quad quads;
	
	public void init(float globalOrder, int textureID, BlendFunc blendType, V3F_C4B_T2F_Quad quads) {
		this.globalOrder = globalOrder;
		this.textureID = textureID;
		this.blendType = blendType;
		this.quads = quads;
	}
	
	public int getMaterialID() {
		return materialID;
	}
	
	public V3F_C4B_T2F_Quad getQuads() {
		return quads;
	}
	
	public int getQuadCount() {
		return 1;
	}
	
	public BlendFunc getBlendType() {
		return blendType;
	}
	
	protected void generateMaterialID() {
		
	}
}
