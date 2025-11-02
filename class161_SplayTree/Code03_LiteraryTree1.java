package class153;

/**
 * 文艺平衡树 - Splay实现范围翻转，Java版本
 * 
 * 【题目来源】洛谷 P3391
 * 【题目链接】https://www.luogu.com.cn/problem/P3391
 * 【题目大意】
 * 长度为n的序列，下标从1开始，一开始序列为1, 2, ..., n
 * 接下来会有k个操作，每个操作给定l，r，表示从l到r范围上的所有数字翻转
 * 做完k次操作后，从左到右打印所有数字
 * 
 * 【数据范围】
 * 1 <= n, k <= 10^5
 * 
 * 【算法分析】
 * 使用Splay树维护序列，通过懒标记实现区间翻转操作
 * Splay树是一种自调整的二叉搜索树，通过将访问过的节点旋转到根附近来优化后续访问
 * 
 * 【时间复杂度】
 * - 所有操作均摊时间复杂度为O(log n)
 * - 单次操作最坏情况可能达到O(n)
 * 
 * 【空间复杂度】O(n)
 * 
 * 【实现特点】
 * - 使用数组模拟节点结构，避免对象创建开销
 * - 实现懒标记（延迟传播）机制处理区间翻转
 * - 使用迭代方式实现中序遍历防止递归爆栈
 * - 添加哨兵节点简化边界情况处理
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

/**
 * Splay树实现文艺平衡树
 * 支持区间翻转操作的平衡树数据结构
 * 
 * 【核心思想】
 * 1. 使用Splay树维护序列的有序性
 * 2. 通过懒标记实现区间翻转操作
 * 3. 利用Splay操作将目标区间提取到树的特定位置进行操作
 * 
 * 【应用场景】
 * - 需要频繁进行区间翻转操作的序列维护问题
 * - 算法竞赛中的数据结构问题
 * - 序列变换相关的应用场景
 */

public class Code03_LiteraryTree1 {

	public static int MAXN = 100005;

	public static int head = 0;

	public static int cnt = 0;

	public static int[] num = new int[MAXN];

	public static int[] father = new int[MAXN];

	public static int[] left = new int[MAXN];

	public static int[] right = new int[MAXN];

	public static int[] size = new int[MAXN];

	public static boolean[] reverse = new boolean[MAXN];

	public static int[] stack = new int[MAXN];

	public static int si;

	public static int[] ans = new int[MAXN];

	public static int ai;

	/**
	 * 【自底向上维护】更新节点子树大小
	 * 时间复杂度: O(1)
	 * 
	 * @param i 需要更新的节点索引
	 */
	public static void up(int i) {
		size[i] = size[left[i]] + size[right[i]] + 1;
	}

	/**
	 * 【方向判断】确定节点i是其父节点的左子节点还是右子节点
	 * 时间复杂度: O(1)
	 * 
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
	 * 
	 * @param i 需要旋转的节点索引
	 */
	public static void rotate(int i) {
		int f = father[i], g = father[f], soni = lr(i), sonf = lr(f);
		
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
	 * 
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
	 * 【懒标记下传】将懒标记传播到子节点
	 * 时间复杂度: O(1)
	 * 功能：
	 * - 处理翻转标记：交换左右子节点
	 * 
	 * @param i 需要下传懒标记的节点
	 */
	public static void down(int i) {
		if (reverse[i]) {
			// 将翻转标记传递给子节点
			reverse[left[i]] = !reverse[left[i]];
			reverse[right[i]] = !reverse[right[i]];
			
			// 交换左右子节点
			int tmp = left[i];
			left[i] = right[i];
			right[i] = tmp;
			
			// 清除当前节点的翻转标记
			reverse[i] = false;
		}
	}

	/**
	 * 【查找操作】在整棵树中找到中序遍历排名为rank的节点
	 * 时间复杂度: O(log n)
	 * 
	 * @param rank 目标排名（从1开始）
	 * @return 对应排名的节点索引
	 */
	public static int find(int rank) {
		int i = head;
		while (i != 0) {
			// 下传懒标记
			down(i);
			
			if (size[left[i]] + 1 == rank) {
				return i;
			} else if (size[left[i]] >= rank) {
				i = left[i];
			} else {
				rank -= size[left[i]] + 1;
				i = right[i];
			}
		}
		return 0; // 未找到对应排名的节点
	}

	/**
	 * 【插入操作】向Splay树中插入一个新元素
	 * 插入后将新节点提至根，以优化后续访问
	 * 时间复杂度: 均摊O(log n)
	 * 空间复杂度: O(1)
	 * 
	 * @param x 需要插入的元素值
	 */
	public static void add(int x) {
		// 创建新节点
		num[++cnt] = x;
		size[cnt] = 1;
		
		// 将新节点连接到树中
		father[cnt] = head;
		right[head] = cnt;
		
		// 【重要优化】将刚插入的节点旋转至根，优化后续访问
		splay(cnt, 0);
	}

	/**
	 * 【区间翻转】翻转区间[l,r]内的元素
	 * 时间复杂度: 均摊O(log n)
	 * 
	 * 【实现原理】
	 * 1. 通过添加哨兵节点，原始序列的第i个元素在Splay树中的排名为i+1
	 * 2. 要翻转区间[l,r]，需要找到排名为l和r+2的节点
	 * 3. 通过两次Splay操作将这两个节点分别旋转到根和根的右子节点
	 * 4. 此时目标区间就是右子节点的左子树，对其设置翻转标记
	 * 
	 * 【特殊说明】
	 * 注意l永远不会是最左位置，r永远不会是最右位置
	 * 因为最左和最右位置提前加入了预备值(哨兵节点)，永远不会修改
	 * 
	 * @param l 区间左端点（从1开始）
	 * @param r 区间右端点（从1开始）
	 */
	public static void reverse(int l, int r) {
		// 找到区间前驱节点(排名为l-1+1=l)和后继节点(排名为r+1+1=r+2)
		int i = find(l - 1);
		int j = find(r + 1);
		
		// 将前驱节点旋转到根
		splay(i, 0);
		
		// 将后继节点旋转到根的右子节点
		splay(j, i);
		
		// 对目标区间(即right[head]的左子树)设置翻转标记
		reverse[left[right[head]]] = !reverse[left[right[head]]];
	}

	/**
	 * 【递归中序遍历】实现二叉树中序遍历
	 * 对本题来说，递归不会爆栈，但其实是有风险的
	 * 
	 * @param i 当前遍历的节点索引
	 */
	public static void inorder(int i) {
		if (i != 0) {
			// 下传懒标记
			down(i);
			
			// 递归遍历左子树
			inorder(left[i]);
			
			// 访问当前节点
			ans[++ai] = num[i];
			
			// 递归遍历右子树
			inorder(right[i]);
		}
	}

	/**
	 * 【迭代中序遍历】实现二叉树中序遍历，防止递归爆栈
	 * 遍历时候懒更新任务也要下发
	 * 
	 * 【算法原理】
	 * 使用栈模拟递归过程，按照左-根-右的顺序访问节点
	 * 在访问每个节点前都需要下传懒标记
	 */
	public static void inorder() {
		si = 0;
		int i = head;
		while (si != 0 || i != 0) {
			if (i != 0) {
				// 下传懒标记
				down(i);
				
				// 将当前节点入栈，继续向左遍历
				stack[++si] = i;
				i = left[i];
			} else {
				// 弹出栈顶节点并访问
				i = stack[si--];
				ans[++ai] = num[i];
				
				// 转向右子树
				i = right[i];
			}
		}
	}

	/**
	 * 【主函数】处理输入输出和操作调用
	 * 【输入输出优化】使用BufferedReader和StreamTokenizer提高读取效率
	 * 
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		// 【IO优化】使用BufferedReader和StreamTokenizer提高读取效率
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		// 【IO优化】使用PrintWriter提高输出效率
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取序列长度和操作次数
		in.nextToken();
		int n = (int) in.nval;
		in.nextToken();
		int m = (int) in.nval;
		
		// 【边界处理】添加哨兵节点
		// 在首尾添加哨兵节点，使原始数据从位置2开始，方便区间操作
		add(0); // 添加头部哨兵
		for (int i = 1; i <= n; i++) {
			add(i);
		}
		add(0); // 添加尾部哨兵
		
		// 处理每个翻转操作
		for (int i = 1, x, y; i <= m; i++) {
			in.nextToken();
			x = (int) in.nval;
			in.nextToken();
			y = (int) in.nval;
			
			// 执行区间翻转操作
			// 由于添加了哨兵节点，原始区间[l,r]在Splay树中的位置需要偏移1
			reverse(x + 1, y + 1);
		}
		
		// 【结果输出】进行中序遍历获取结果
		ai = 0;
		// inorder(head); // 递归版本，可能爆栈
		inorder(); // 迭代版本，更安全
		
		// 输出结果，跳过两个哨兵节点
		for (int i = 2; i < ai; i++) {
			out.print(ans[i] + " ");
		}
		out.println();
		
		// 【工程化考量】确保所有输出都被刷新并关闭资源
		out.flush();
		out.close();
		br.close();
	}

}
