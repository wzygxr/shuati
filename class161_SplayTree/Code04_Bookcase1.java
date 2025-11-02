package class153;

/**
 * 书架 - Splay树实现，Java版本
 * 
 * 【题目来源】洛谷 P2596 [ZJOI2006]
 * 【题目链接】https://www.luogu.com.cn/problem/P2596
 * 【题目大意】
 * 给定一个长度为n的排列，由数字1、2、3...n组成，实现如下五种操作：
 * 1. Top s      : 数字s移动到最左边
 * 2. Bottom s   : 数字s移动到最右边
 * 3. Insert s t : 数字s位置假设为rank，现在移动到rank+t位置
 * 4. Ask s      : 查询数字s左边有多少数字
 * 5. Query s    : 查询从左往右第s位的数字
 * 
 * 【数据范围】
 * 3 <= n, m <= 8 * 10^4
 * 
 * 【算法分析】
 * 使用Splay树维护序列，支持按值和按排名的快速查找
 * 通过Splay操作将目标节点旋转到根附近优化后续访问
 * 
 * 【时间复杂度】
 * - 所有操作均摊时间复杂度为O(log n)
 * - 单次操作最坏情况可能达到O(n)
 * 
 * 【空间复杂度】O(n)
 * 
 * 【实现特点】
 * - 使用数组模拟节点结构，避免对象创建开销
 * - 添加哨兵节点简化边界情况处理
 * - 实现位置映射数组快速定位节点
 * - 使用Kattio类优化IO效率
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * Splay树实现书架问题
 * 支持书籍位置的动态维护和查询操作
 * 
 * 【核心思想】
 * 1. 使用Splay树维护书籍的顺序关系
 * 2. 通过位置映射数组实现O(1)按值查找
 * 3. 利用Splay操作优化频繁访问节点的访问速度
 * 4. 添加哨兵节点处理边界情况
 * 
 * 【应用场景】
 * - 动态维护序列中元素位置的操作
 * - 需要频繁查询元素排名和按排名查找元素的问题
 * - 算法竞赛中的数据结构问题
 */

public class Code04_Bookcase1 {

	public static int MAXN = 80005;

	public static int head = 0;

	public static int cnt = 0;

	public static int[] num = new int[MAXN];

	public static int[] father = new int[MAXN];

	public static int[] left = new int[MAXN];

	public static int[] right = new int[MAXN];

	public static int[] size = new int[MAXN];

	// pos[num] : 数字num所在节点的编号
	public static int[] pos = new int[MAXN];

	public static int n, m;

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
	 * 【查找操作】在整棵树中找到中序遍历排名为rank的节点
	 * 时间复杂度: O(log n)
	 * 
	 * @param rank 目标排名（从1开始）
	 * @return 对应排名的节点索引
	 */
	// 返回中序排名为rank的节点编号
	public static int find(int rank) {
		int i = head;
		while (i != 0) {
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
	 * @param s 需要插入的元素值
	 */
	public static void add(int s) {
		// 创建新节点
		num[++cnt] = s;
		
		// 更新位置映射数组
		pos[s] = cnt;
		
		// 初始化节点信息
		size[cnt] = 1;
		
		// 将新节点连接到树中
		father[cnt] = head;
		right[head] = cnt;
		
		// 【重要优化】将刚插入的节点旋转至根，优化后续访问
		splay(cnt, 0);
	}

	/**
	 * 【查询排名】查询元素s在序列中的排名（从0开始）
	 * 时间复杂度: 均摊O(log n)
	 * 
	 * @param s 要查询的元素值
	 * @return s的排名（从0开始）
	 */
	public static int ask(int s) {
		// 通过位置映射数组O(1)找到节点
		int i = pos[s];
		
		// 【重要优化】将访问的节点旋转至根，优化后续访问
		splay(i, 0);
		
		// 返回左子树大小即为排名（从0开始）
		return size[left[i]];
	}

	/**
	 * 【按排名查询】查询排名为s的元素值
	 * 时间复杂度: 均摊O(log n)
	 * 
	 * @param s 目标排名（从1开始）
	 * @return 对应排名的元素值
	 */
	public static int query(int s) {
		// 找到排名为s的节点
		int i = find(s);
		
		// 【重要优化】将找到的节点旋转至根，优化后续访问
		splay(i, 0);
		
		// 返回节点存储的值
		return num[i];
	}

	/**
	 * 【移动操作】将排名为a的节点移动到排名为b的位置
	 * 时间复杂度: 均摊O(log n)
	 * 
	 * 【实现原理】
	 * 1. 首先将节点从原位置分离：找到其前驱和后继，通过两次Splay操作将其分离
	 * 2. 然后将节点插入到新位置：找到新位置的前驱和后继，通过两次Splay操作将其插入
	 * 
	 * 【特殊说明】
	 * 注意a不会是1和n位置，b也如此
	 * 因为1位置和n位置提前加入了预备值(哨兵节点)，永远不会修改
	 * 
	 * @param a 原始排名（从1开始）
	 * @param b 目标排名（从1开始）
	 */
	// 中序排名为a的节点，移动到中序排名为b的位置
	public static void move(int a, int b) {
		// 第一步：将节点从原位置分离
		// 找到节点的前驱(排名a-1)和后继(排名a+1)
		int l = find(a - 1);
		int r = find(a + 1);
		
		// 将前驱旋转到根
		splay(l, 0);
		
		// 将后继旋转到根的右子节点
		splay(r, l);
		
		// 此时目标节点就是r的左子节点，将其分离
		int i = left[r];
		left[r] = 0;
		
		// 更新相关节点信息
		up(r);
		up(l);
		
		// 第二步：将节点插入到新位置
		// 找到新位置的前驱(排名b-1)和后继(排名b)
		l = find(b - 1);
		r = find(b);
		
		// 将前驱旋转到根
		splay(l, 0);
		
		// 将后继旋转到根的右子节点
		splay(r, l);
		
		// 将节点i连接到r的左子节点位置
		left[r] = i;
		father[i] = r;
		
		// 更新相关节点信息
		up(r);
		up(l);
	}

	/**
	 * 【主函数】处理输入输出和操作调用
	 * 【输入输出优化】使用Kattio提高读取效率
	 * 
	 * @param args 命令行参数
	 */
	public static void main(String[] args) {
		// 【IO优化】使用Kattio提高读取效率
		Kattio io = new Kattio();
		
		// 读取书籍数量和操作数量
		n = io.nextInt();
		m = io.nextInt();
		
		// 【边界处理】添加哨兵节点
		// 在首尾添加哨兵节点，使原始数据从位置2开始，方便区间操作
		add(0); // 添加头部哨兵
		for (int i = 1; i <= n; i++) {
			add(io.nextInt());
		}
		add(n + 1); // 添加尾部哨兵
		
		// 注意在最左插入了0，最右插入了n+1，作为准备值，所以一共n+2个数
		// 下面操作时，不要忘了最左是0，最右是n+1，并且永远不修改
		n = n + 2;
		
		// 处理每个操作
		String op;
		for (int i = 1, s, t, rank; i <= m; i++) {
			op = io.next();
			s = io.nextInt();
			
			// 获取当前书籍的排名（从1开始）
			rank = ask(s) + 1;
			
			if (op.equals("Top")) {
				// Top操作：将书籍移动到最上面
				// 因为有最左侧的准备值，所以开头是中序排名2的位置
				move(rank, 2);
			} else if (op.equals("Bottom")) {
				// Bottom操作：将书籍移动到最下面
				// 因为有最右侧的准备值，所以结尾是中序排名n-1的位置
				move(rank, n - 1);
			} else if (op.equals("Insert")) {
				// Insert操作：将书籍移动指定位置
				t = io.nextInt();
				move(rank, rank + t);
			} else if (op.equals("Ask")) {
				// Ask操作：查询书籍左边有多少本书
				// rank代表当前数字的排名，因为有最左侧的准备值
				// 所以排名其实是rank-1，题目要返回小于的数量，所以是rank - 2
				io.println(rank - 2);
			} else {
				// Query操作：查询指定位置的书籍编号
				// 因为有最左侧的准备值，所以查s+1名的数字
				io.println(query(s + 1));
			}
		}
		
		// 【工程化考量】确保所有输出都被刷新并关闭资源
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
