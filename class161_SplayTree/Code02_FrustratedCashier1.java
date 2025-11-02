package class153;

/**
 * Splay树实现 - 郁闷的出纳员问题解决方案
 * 【题目来源】洛谷 P1486
 * 【题目链接】https://www.luogu.com.cn/problem/P1486
 * 【算法分析】
 * 使用Splay树维护员工薪水信息，支持动态插入、整体加减、查询第k大等操作
 * 通过懒标记技术优化整体加减操作，避免对每个节点逐一修改
 * 【时间复杂度】
 * - 所有操作均摊时间复杂度为O(log n)
 * - 单次操作最坏情况可能达到O(n)
 * 【空间复杂度】O(n)
 * 【实现特点】使用全局变量change记录整体薪水变化，避免对每个节点逐一修改
 */

/**
 * 郁闷的出纳员问题
 * 【题目大意】
 * 维护一个公司员工的薪水系统，支持以下操作：
 * 1. I x : 新来员工初始薪水是x，如果x低于最低薪水limit，该员工不会入职当然也不算离职
 * 2. A x : 所有员工的薪水都加上x
 * 3. S x : 所有员工的薪水都减去x，一旦有员工低于limit那么就会离职
 * 4. F x : 查询第x多的工资，如果x大于当前员工数量，打印-1
 * 所有操作完成后，打印有多少员工在操作期间离开了公司
 * 
 * 【解题思路】
 * 使用Splay树维护员工薪水信息，通过懒标记技术优化整体加减操作
 * 1. 使用全局变量change记录整体薪水变化，避免对每个节点逐一修改
 * 2. 对于减薪操作，通过查找薪水低于limit-change-1的节点并删除来实现员工离职
 * 3. 对于查询操作，通过计算排名来实现第k大查询
 * 
 * 【关键技巧】
 * 1. 使用哨兵节点简化边界处理
 * 2. 通过全局变量change记录整体变化，避免对每个节点逐一修改
 * 3. 离职员工计数通过enter - size[head]计算
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Code02_FrustratedCashier1 {

	/**
	 * 【空间配置】预分配的最大节点数量
	 * 设置为300001是因为题目保证操作次数不超过3*10^5，额外+1处理边界情况
	 */
	public static int MAXN = 300001;

	/**
	 * 【树结构标识】
	 * head: 根节点索引
	 * cnt: 当前已分配的节点计数器
	 */
	public static int head = 0;
	public static int cnt = 0;

	/**
	 * 【节点属性数组】使用数组模拟节点，避免对象创建开销
	 * key: 节点存储的值（员工薪水）
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
	 * 【问题参数】
	 * limit: 最低薪水要求
	 * change: 全局薪水变化量（用于优化整体加减操作）
	 * enter: 入职员工总数（用于计算离职员工数）
	 */
	public static int limit;
	public static int change = 0;
	public static int enter = 0;

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
	 * 【插入操作】向Splay树中插入一个新元素（员工薪水）
	 * 插入后将新节点提至根，以优化后续访问
	 * 时间复杂度: 均摊O(log n)
	 * 空间复杂度: O(1)
	 * @param num 需要插入的元素值（员工薪水）
	 */
	public static void add(int num) {
		// 创建新节点
		key[++cnt] = num;
		size[cnt] = 1;
		
		// 【空树处理】如果树为空，直接设置为根节点
		if (head == 0) {
			head = cnt;
		} else {
			// 【查找插入位置】根据BST性质找到合适的插入位置
			int f = 0, i = head, son = 0;
			while (i != 0) {
				f = i;
				if (key[i] <= num) {
					son = 1;
					i = right[i];
				} else {
					son = 0;
					i = left[i];
				}
			}
			
			// 插入节点到找到的位置
			if (son == 1) {
				right[f] = cnt;
			} else {
				left[f] = cnt;
			}
			father[cnt] = f;
			
			// 【重要优化】将刚插入的节点旋转至根，优化后续访问
			splay(cnt, 0);
		}
	}

	/**
	 * 【查询第k大元素】查询排名为x的元素值
	 * 时间复杂度: 均摊O(log n)
	 * @param x 目标排名
	 * @return 对应排名的元素值
	 */
	public static int index(int x) {
		int i = head, last = head;
		while (i != 0) {
			last = i;
			if (size[left[i]] >= x) {
				i = left[i];
			} else if (size[left[i]] + 1 < x) {
				x -= size[left[i]] + 1;
				i = right[i];
			} else {
				i = 0;
			}
		}
		splay(last, 0);
		return key[last];
	}

	/**
	 * 【员工离职处理】处理减薪操作导致的员工离职
	 * 时间复杂度: 均摊O(log n)
	 */
	public static void departure() {
		// 计算离职薪水阈值
		int num = limit - change - 1;
		int i = head, ans = 0;
		
		// 查找薪水低于阈值的节点
		while (i != 0) {
			if (key[i] > num) {
				ans = i;
				i = left[i];
			} else {
				i = right[i];
			}
		}
		
		// 如果有员工需要离职
		if (ans == 0) {
			// 所有员工都离职了
			head = 0;
		} else {
			// 将找到的节点旋转到根
			splay(ans, 0);
			// 删除根节点的左子树（薪水低于阈值的员工）
			left[head] = 0;
			// 更新根节点信息
			up(head);
		}
	}

	/**
	 * 【主函数】处理输入输出和操作调用
	 * 【输入输出优化】使用Kattio提高读取效率
	 * @param args 命令行参数
	 */
	public static void main(String[] args) {
		Kattio io = new Kattio();
		int n = io.nextInt();
		limit = io.nextInt();
		String op;
		int x;
		for (int i = 1; i <= n; i++) {
			op = io.next();
			x = io.nextInt();
			if (op.equals("I")) {
				// 操作I: 新来员工初始薪水是x
				// 如果x低于limit，该员工不会入职当然也不算离职
				if (x >= limit) {
					enter++;
					// 插入时需要减去当前的全局变化量
					add(x - change);
				}
			} else if (op.equals("A")) {
				// 操作A: 所有员工的薪水都加上x
				change += x;
			} else if (op.equals("S")) {
				// 操作S: 所有员工的薪水都减去x
				change -= x;
				// 处理员工离职
				departure();
			} else {
				// 操作F: 查询第x多的工资
				if (x > size[head]) {
					io.println(-1);
				} else {
					// 查询第x多的工资，需要加上全局变化量
					io.println(index(size[head] - x + 1) + change);
				}
			}
		}
		// 打印离职员工数量
		io.println(enter - size[head]);
		io.flush();
		io.close();
	}

	// Kattio类IO效率很好，但还是不如StreamTokenizer
	// 只有StreamTokenizer无法正确处理时，才考虑使用这个类
	// 参考链接 : https://oi-wiki.org/lang/java-pro/
	public static class Kattio extends PrintWriter {
		private BufferedReader r;
		private StringTokenizer st;

		public Kattio() {
			this(System.in, System.out);
		}

		public Kattio(InputStream i, OutputStream o) {
			super(o);
			r = new BufferedReader(new InputStreamReader(i));
		}

		public Kattio(String intput, String output) throws IOException {
			super(output);
			r = new BufferedReader(new FileReader(intput));
		}

		public String next() {
			try {
				while (st == null || !st.hasMoreTokens())
					st = new StringTokenizer(r.readLine());
				return st.nextToken();
			} catch (Exception e) {
			}
			return null;
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

		public double nextDouble() {
			return Double.parseDouble(next());
		}

		public long nextLong() {
			return Long.parseLong(next());
		}
	}

}