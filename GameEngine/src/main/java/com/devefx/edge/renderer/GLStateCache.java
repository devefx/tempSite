package com.devefx.edge.renderer;

import com.devefx.gameengine.base.Director;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLContext;

public interface GLStateCache {

	public static final int MAX_ATTRIBUTES = 16;
	public static final int MAX_ACTIVE_TEXTURE = 16;
	
	public class GL {
		
		public static final int VERTEX_ATTRIB_FLAG_POS_COLOR_TEX = 7;
		
		static int currentProjectionMatrix = -1;
		static int attributeFlags = 0;
		
		static int currentShaderProgram = -1;
		static int currentBoundTexture[] = new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
		static int blendingSource = -1;
		static int blendingDest = -1;
		static int vaoId = 0;
		static int activeTexture = -1;
		
		public static GL2 getGL() {
			return GLContext.getCurrentGL().getGL2();
		}
		
		public static void invalidateStateCache() {
			Director.getInstance().initMatrixStack();
			currentProjectionMatrix = -1;
			attributeFlags = 0;
			
			currentShaderProgram = -1;
			for (int i = 0; i < MAX_ACTIVE_TEXTURE; i++) {
				currentBoundTexture[i] = -1;
			}
			blendingSource = -1;
			blendingDest = -1;
			vaoId = 0;
		}
		
		public static void deleteProgram(int program) {
			if (program == currentShaderProgram) {
				currentShaderProgram = -1;
			}
			getGL().glDeleteProgram(program);
		}
		
		public static void useProgram(int program) {
			if (program != currentShaderProgram) {
				currentShaderProgram = program;
				getGL().glUseProgram(program);
			}
		}
		
		public static void blendFunc(int sfactor, int dfactor) {
			if (sfactor != blendingSource || dfactor != blendingDest) {
				blendingSource = sfactor;
				blendingDest = dfactor;
				if (sfactor == GL2.GL_ONE && dfactor == GL2.GL_ZERO) {
					getGL().glDisable(GL2.GL_BLEND);
				} else {
					getGL().glEnable(GL2.GL_BLEND);
					getGL().glBlendFunc(sfactor, dfactor);
				}
			}
		}
		
		public static void bindTexture2D(int textureId) {
			bindTexture2DN(0, textureId);
		}
		
		public static void bindTexture2DN(int textureUnit, int textureId) {
			assert(textureUnit < MAX_ACTIVE_TEXTURE);
			if (currentBoundTexture[textureUnit] != textureId) {
				currentBoundTexture[textureUnit] = textureId;
				activeTexture(GL2.GL_TEXTURE0 + textureUnit);
				getGL().glBindTexture(GL2.GL_TEXTURE_2D, textureId);
			}
		}
		
		public static void deleteTexture(int textureId) {
			for (int i = 0; i < MAX_ACTIVE_TEXTURE; i++) {
				if (currentBoundTexture[i] == textureId) {
					currentBoundTexture[i] = -1;
				}
			}
			getGL().glDeleteTextures(1, new int[] {textureId}, 0);
		}
		
		public static void activeTexture(int texture) {
			if (activeTexture != texture) {
				activeTexture = texture;
				getGL().glActiveTexture(texture);
			}
		}
		
		public static void bindVAO(int vaoId) {
			if (GL.vaoId != vaoId) {
				GL.vaoId = vaoId;
				getGL().glBindVertexArray(vaoId);
			}
		}
		
		public static void enableVertexAttribs(int flags) {
			bindVAO(0);
			for (int i = 0; i < MAX_ATTRIBUTES; i++) {
				int bit = 1 << i;
				boolean enabled = (flags & bit) != 0;
				boolean enabledBefore = (attributeFlags & bit) != 0;
				if(enabled != enabledBefore) {
					if(enabled) {
						getGL().glEnableVertexAttribArray(i);
					} else {
						getGL().glDisableVertexAttribArray(i);
					}
				}
			}
			attributeFlags = flags;
		}
		
		public static void setProjectionMatrixDirty() {
			currentProjectionMatrix = -1;
		}
		
	}
}
