package class153;

/**
 * Splay树提根操作演示与长链高度变化实验
 * 【实验目的】
 * 演示Splay树的提根操作对不同形态树结构高度的影响
 * 通过构建一字型长链和之字型长链，观察提根操作对树高度的优化效果
 * 
 * 【实验内容】
 * 1. 构建一字型长链（链状结构）
 * 2. 构建之字型长链（锯齿状结构）
 * 3. 对两种结构的最下方节点执行提根操作
 * 4. 观察提根前后树的高度变化
 * 
 * 【算法分析】
 * Splay树通过提根操作将频繁访问的节点移动到树的顶部，优化后续访问
 * 对于链状结构，提根操作可以显著降低树的高度，提高访问效率
 * 
 * 【时间复杂度】
 * - 构建操作：O(n)
 * - 提根操作：均摊O(log n)
 * - 高度计算：O(n)
 * 
 * 【空间复杂度】O(n)
 */

/**
 * Splay树提根操作演示
 * 
 * 【实验原理】
 * Splay树的核心思想是"自调整"，通过将访问过的节点旋转到根附近来优化后续访问
 * 这使得频繁访问的节点能够更快地被再次访问，利用了访问的局部性原理
 * 
 * 【实验设计】
 * 1. 一字型长链：节点按顺序连接形成一条直线
 *    这种结构在未优化时访问最下方节点需要O(n)时间
 *    经过提根操作后，树的高度会显著降低
 *    
 * 2. 之字型长链：节点按锯齿状连接形成之字形结构
 *    这种结构在未优化时访问最下方节点也需要O(n)时间
 *    经过提根操作后，树的高度同样会降低
 *    
 * 【预期结果】
 * 两种长链在提根操作后，树的高度都会显著降低，证明Splay树的自调整特性
 */
public class ShowDetail {

	/**
	 * 【空间配置】预分配的最大节点数量
	 */
	public static int MAXN = 100001;

	/**
	 * 【树结构标识】
	 * head: 根节点索引
	 * cnt: 当前已分配的节点计数器
	 */
	public static int head = 0;
	public static int cnt = 0;

	/**
	 * 【节点属性数组】使用数组模拟节点，避免对象创建开销
	 * key: 节点存储的值
	 * father: 父节点索引
	 * left: 左子节点索引
	 * right: 右子节点索引
	 * size: 以该节点为根的子树大小
	 */
	public static int[] key = new int[MAXN];
	public static int[] father = new int[MAXN];
	public static int[] left = new int[MAXN];
	public static int[] right = new int[MAXN];
	public static int[] size = new int[MAXN];

	/**
	 * 【自底向上维护】更新节点子树大小
	 * 时间复杂度: O(1)
	 * @param i 需要更新的节点索引
	 */
	public static void up(int i) {
		size[i] = size[left[i]] + size[right[i]] + 1;
	}

	/**
	 * 【方向判断】确定节点i是其父节点的左子节点还是右子节点
	 * 时间复杂度: O(1)
	 * @param i 需要判断的节点索引
	 * @return 1表示右子节点，0表示左子节点
	 */
	public static int lr(int i) {
		return right[father[i]] == i ? 1 : 0;
	}

	/**
	 * 【核心旋转操作】将节点i旋转至其父节点的位置
	 * 这是Splay树维护平衡的基本操作
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 * @param i 需要旋转的节点索引
	 */
	public static void rotate(int i) {
		int f = father[i];     // 父节点索引
		int g = father[f];     // 祖父节点索引
		int soni = lr(i);      // 当前节点是父节点的左子还是右子
		int sonf = lr(f);      // 父节点是祖父节点的左子还是右子
		
		// 【旋转逻辑】根据当前节点是左子还是右子执行不同的旋转操作
		if (soni == 1) {       // 右子节点，执行右旋
			right[f] = left[i];
			if (right[f] != 0) {
				father[right[f]] = f;
			}
			left[i] = f;
		} else {               // 左子节点，执行左旋
			left[f] = right[i];
			if (left[f] != 0) {
				father[left[f]] = f;
			}
			right[i] = f;
		}
		
		// 更新祖父节点的子节点指针
		if (g != 0) {
			if (sonf == 1) {
				right[g] = i;
			} else {
				left[g] = i;
			}
		}
		
		// 更新父指针
		father[f] = i;
		father[i] = g;
		
		// 【重要】更新节点信息，先更新被旋转的父节点，再更新当前节点
		up(f);
		up(i);
	}

	/**
	 * 【核心伸展操作】将节点i旋转到goal的子节点位置
	 * 如果goal为0，则将i旋转到根节点
	 * 这是Splay树的核心操作，通过一系列旋转使被访问节点移动到树的顶部
	 * 时间复杂度: 均摊O(log n)，最坏情况O(n)
	 * 空间复杂度: O(1)
	 * @param i 需要旋转的节点索引
	 * @param goal 目标父节点索引
	 */
	public static void splay(int i, int goal) {
		int f = father[i], g = father[f];
		
		// 当当前节点的父节点不是目标节点时，继续旋转
		while (f != goal) {
			// 【旋转策略】根据Zig-Zig和Zig-Zag情况选择不同的旋转顺序
			if (g != goal) {
				// 如果父节点和当前节点在同侧，先旋转父节点（Zig-Zig情况）
				// 否则直接旋转当前节点（Zig-Zag情况）
				if (lr(i) == lr(f)) {
					rotate(f);
				} else {
					rotate(i);
				}
			}
			// 最后旋转当前节点
			rotate(i);
			
			// 更新父节点和祖父节点
			f = father[i];
			g = father[f];
		}
		
		// 如果旋转到根节点，更新根节点指针
		if (goal == 0) {
			head = i;
		}
	}

	/**
	 * 【构建一字型长链】构建从l到r的一字型链状结构
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n)
	 * @param l 起始值
	 * @param r 结束值
	 * @return 链的头节点索引
	 */
	public static int build1(int l, int r) {
		// 记录链的头节点
		int h = cnt + 1;
		// 逐个创建节点并连接成链
		for (int i = l, last = 0; i <= r; i++, last = cnt) {
			key[++cnt] = i;
			father[cnt] = last;
			left[cnt] = right[cnt] = 0;
			size[cnt] = r - i + 1;
			if (last != 0) {
				right[last] = cnt;
			}
		}
		return h;
	}

	/**
	 * 【构建之字型长链】构建从l到r的之字型锯齿状结构
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n)
	 * @param l 起始值
	 * @param r 结束值
	 * @param fa 父节点索引
	 * @return 链的头节点索引
	 */
	public static int build2(int l, int r, int fa) {
		// 递归终止条件
		if (l > r) {
			return 0;
		}
		
		// 创建当前节点
		key[++cnt] = l;
		father[cnt] = fa;
		left[cnt] = right[cnt] = 0;
		int h = cnt;
		
		// 如果还有后续节点
		if (l < r) {
			// 创建下一个节点
			key[++cnt] = r;
			father[cnt] = h;
			left[cnt] = right[cnt] = 0;
			int c = cnt;
			right[h] = c;
			// 递归构建中间部分
			left[c] = build2(l + 1, r - 1, c);
			up(c);
		}
		
		// 更新当前节点信息
		up(h);
		return h;
	}

	/**
	 * 【计算树高度】计算以i为根的树的高度
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(log n)（递归栈空间）
	 * @param i 树的根节点索引
	 * @return 树的高度
	 */
	public static int height(int i) {
		// 空节点高度为0
		if (i == 0) {
			return 0;
		}
		// 递归计算左右子树高度，取较大值加1
		return Math.max(height(left[i]), height(right[i])) + 1;
	}

	/**
	 * 【主函数】执行实验并输出结果
	 * @param args 命令行参数
	 */
	public static void main(String[] args) {
		System.out.println("构建一字型长链");
		System.out.println("最下方节点执行splay，观察高度变化");
		// 构建一字型长链
		head = build1(1, 1000);
		System.out.println("splay之前的链长度 : " + height(head));
		// 对最下方节点执行提根操作
		splay(cnt, 0);
		System.out.println("splay之后的链长度 : " + height(head));

		System.out.println("==================");

		System.out.println("构建之字型长链");
		System.out.println("最下方节点执行splay，观察高度变化");
		// 构建之字型长链
		head = build2(1, 1000, 0);
		System.out.println("splay之前的链长度 : " + height(head));
		// 对最下方节点执行提根操作
		splay(cnt, 0);
		System.out.println("splay之后的链长度 : " + height(head));
	}

}