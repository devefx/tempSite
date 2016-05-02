package com.devefx.gameengine.renderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLContext;

public class RenderQueue {

	protected List<RenderCommand> commands;
	
	protected boolean isCullEnabled;
	protected boolean isDepthEnabled;
	protected byte[] isDepthWrite;
	
	public RenderQueue() {
		commands = new ArrayList<RenderCommand>();
		isDepthWrite = new byte[1];
	}
	
	public void push(RenderCommand command) {
		commands.add(command);
	}
	
	public void sort() {
		Collections.sort(commands, new Comparator<RenderCommand>() {
			@Override
			public int compare(RenderCommand cmd1, RenderCommand cmd2) {
				if (cmd1.globalOrder > cmd1.globalOrder) {
					return 1;
				} else if (cmd1.globalOrder == cmd1.globalOrder) {
					return 0;
				}
				return -1;
			}
		});
	}
	
	public int size() {
		return commands.size();
	}
	
	public void clear() {
		commands.clear();
	}
	
	public void realloc(int reserveSize) {
		commands = new ArrayList<RenderCommand>(reserveSize);
	}
	
	public Iterator<RenderCommand> getQueue() {
		return commands.iterator();
	}
	
	public void saveRenderState() {
		final GL gl = GLContext.getCurrentGL().getGL2();
		isCullEnabled = gl.glIsEnabled(GL.GL_CULL_FACE);
		isDepthEnabled = gl.glIsEnabled(GL.GL_DEPTH_TEST);
		gl.glGetBooleanv(GL.GL_DEPTH_WRITEMASK, isDepthWrite, 0);
	}
	
	public void restoreRenderState() {
		final GL gl = GLContext.getCurrentGL().getGL2();
		if (isCullEnabled) {
			gl.glEnable(GL.GL_CULL_FACE);
		} else {
			gl.glDisable(GL.GL_CULL_FACE);
		}
		if (isDepthEnabled) {
			gl.glEnable(GL.GL_DEPTH_TEST);
		} else {
			gl.glDisable(GL.GL_DEPTH_TEST);
		}
		gl.glDepthMask(isDepthWrite[0] != 0);
	}
}
