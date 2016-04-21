package com.devefx.gameengine.renderer;

import java.util.ArrayList;
import java.util.List;

/**
 Class that knows how to sort `RenderCommand` objects.
 Since the commands that have `z == 0` are "pushed back" in
 the correct order, the only `RenderCommand` objects that need to be sorted,
 are the ones that have `z < 0` and `z > 0`.
 */
public class RenderQueue {
	
	public class QUEUE_GROUP {
		/**Objects with globalZ smaller than 0.*/
		public static final int GLOBALZ_NEG = 0;
		/**Opaque 3D objects with 0 globalZ.*/
		public static final int OPAQUE_3D = 1;
		/**Transparent 3D objects with 0 globalZ.*/
		public static final int TRANSPARENT_3D = 2;
		/**2D objects with 0 globalZ.*/
		public static final int GLOBALZ_ZERO = 3;
		/**Objects with globalZ bigger than 0.*/
		public static final int GLOBALZ_POS = 4;
		public static final int QUEUE_COUNT = 5;
	}
	
	protected List<RenderCommand> commands = new ArrayList<RenderCommand>(QUEUE_GROUP.QUEUE_COUNT);
	protected boolean isCullEnabled;
	protected boolean isDepthEnabled;
	protected boolean isDepthWrite;
	/**
	 * 添加渲染命令到当前渲染队列
	 * @param command
	 */
	public void push_back(RenderCommand command) {}
	/**
	 * 返回渲染命令数量
	 * @return int
	 */
	public int size() { return 0; }
	/**
	 * 排序渲染队列
	 */
	public void sort() {}
	/**
	 * 清空全部渲染命令
	 */
	public void clear() {}
	/**
	 * 重新分配队列的大小
	 * @param reserveSize
	 */
	public void realloc(int reserveSize) {}
	/**
	 * 保存当前DepthState、CullState DepthWriteState呈现的状态
	 */
	public void saveRenderState() {}
	/**
	 * 恢复保存的DepthState、CullState DepthWriteState呈现的状态
	 */
	public void restoreRenderState() {}
}
