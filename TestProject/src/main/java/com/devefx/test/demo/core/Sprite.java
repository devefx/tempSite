package com.devefx.test.demo.core;

import java.nio.ByteBuffer;

import com.devefx.test.demo.GLRenderer;
import com.devefx.test.demo.buffer.Vertex;
import com.devefx.test.demo.buffer.VertexBuffer;
import com.devefx.test.demo.quad.Quad;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.util.texture.Texture;

public class Sprite {
	
	private Quad quad;
	private int width;
	private int height;
	
	
	public Sprite(Texture texture, float texx, float texy) {
		
		quad = new Quad();
		
		for (int i = 0; i < 4; i++) {
			quad.v[i].z = 0.5f;
			quad.v[i].col = 0xFFFFFFFF;
		}
		
		width = texture.getWidth();
		height = texture.getHeight();
	}
	
	public void render(int x, int y) {
		
		quad.v[0].x = x;
		quad.v[0].y = y;
		
		quad.v[1].x = x + width;
		quad.v[1].y = y;
		
		quad.v[2].x = x + width;
		quad.v[2].y = y + height;
		
		quad.v[3].x = x;
		quad.v[3].y = y + height;
		
		GL2 gl = GLContext.getCurrentGL().getGL2();
		
		VertexBuffer buffer = GLRenderer.buffer;
		drawVertexPrimitiveList(buffer.getVerticesBuffer(), 1, buffer.getIndicesBuffer(), 1, 0);
		
		
		if (gl.glIsTexture(quad.tex)) {
			gl.glBindTexture(GL2.GL_TEXTURE_2D, quad.tex);
		}
		
		
	}
	
	private void drawVertexPrimitiveList(ByteBuffer vertices, int vertexCount,
			ByteBuffer indexList, int primitiveCount, int pType) {
		
		GL2 gl = GLContext.getCurrentGL().getGL2();
		
		gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		
		gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
		
		gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
		
		
		
		gl.glColorPointer(4, GL2.GL_UNSIGNED_BYTE, Vertex.SIZE, vertices);//col
		gl.glNormalPointer(GL2.GL_FLOAT, Vertex.SIZE, vertices);//nx
		gl.glTexCoordPointer(2, GL2.GL_FLOAT, Vertex.SIZE, vertices);//tx
		gl.glVertexPointer(3, GL2.GL_FLOAT, Vertex.SIZE, vertices);//x
		
		
		
		gl.glDrawElements(GL2.GL_TRIANGLES, primitiveCount * 3, GL2.GL_UNSIGNED_SHORT, indexList);
		
		
		
		gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
		gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
	}
	
}
