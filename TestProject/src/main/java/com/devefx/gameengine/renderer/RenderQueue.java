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
	 * �����Ⱦ�����ǰ��Ⱦ����
	 * @param command
	 */
	public void push_back(RenderCommand command) {}
	/**
	 * ������Ⱦ��������
	 * @return int
	 */
	public int size() { return 0; }
	/**
	 * ������Ⱦ����
	 */
	public void sort() {}
	/**
	 * ���ȫ����Ⱦ����
	 */
	public void clear() {}
	/**
	 * ���·�����еĴ�С
	 * @param reserveSize
	 */
	public void realloc(int reserveSize) {}
	/**
	 * ���浱ǰDepthState��CullState DepthWriteState���ֵ�״̬
	 */
	public void saveRenderState() {}
	/**
	 * �ָ������DepthState��CullState DepthWriteState���ֵ�״̬
	 */
	public void restoreRenderState() {}
}
