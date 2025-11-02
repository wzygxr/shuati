package class156;

// 银河英雄传说
// 一共有30000搜战舰，编号1~30000，一开始每艘战舰各自成一队
// 如果若干战舰变成一队，那么队伍里的所有战舰竖直地排成一列
// 实现如下两种操作，操作一共调用t次
// M l r : 合并l号战舰所在队伍和r号战舰所在队伍
//         l号战舰的队伍，整体移动到，r号战舰所在队伍的最末尾战舰的后面
//         如果l号战舰和r号战舰已经是一队，不进行任何操作
// C l r : 如果l号战舰和r号战舰不在一个队伍，打印-1
//         如果l号战舰和r号战舰在一个队伍，打印它俩中间隔着几艘战舰
// 1 <= t <= 5 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P1196
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * 带权并查集解决队列合并与查询问题
 * 
 * 问题分析：
 * 维护战舰队列的合并和查询操作，需要支持：
 * 1. 将一个队列整体合并到另一个队列末尾
 * 2. 查询同一队列中两艘战舰之间间隔的战舰数量
 * 
 * 核心思想：
 * 1. 使用带权并查集维护战舰之间的相对位置关系
 * 2. dist[i] 表示战舰i到其所在队列队首的距离（以战舰数量为单位）
 * 3. size[i] 表示以战舰i为根的队列中战舰的数量
 * 
 * 时间复杂度分析：
 * - prepare: O(n)
 * - find: O(α(n)) 近似O(1)
 * - union: O(α(n)) 近似O(1)
 * - query: O(α(n)) 近似O(1)
 * - 总体: O(n + t * α(n))
 * 
 * 空间复杂度: O(n) 用于存储father、dist和size数组
 * 
 * 应用场景：
 * - 队列合并与查询
 * - 动态维护序列位置关系
 * - 游戏中的编队系统
 */
public class Code04_LegendOfHeroes {

	public static int MAXN = 30001;

	public static int n = 30000;

	// father[i] 表示战舰i的父节点
	public static int[] father = new int[MAXN];

	// dist[i] 表示战舰i到其所在队列队首的距离
	public static int[] dist = new int[MAXN];

	// size[i] 表示以战舰i为根的队列中战舰的数量
	public static int[] size = new int[MAXN];

	// 递归会爆栈，所以用迭代来寻找并查集代表节点
	public static int[] stack = new int[MAXN];

	/**
	 * 初始化并查集
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n)
	 */
	public static void prepare() {
		// 初始化每艘战舰为自己所在队列的代表
		for (int i = 1; i <= n; i++) {
			father[i] = i;
			// 初始时每艘战舰到队首的距离为0
			dist[i] = 0;
			// 初始时每个队列只有1艘战舰
			size[i] = 1;
		}
	}

	/**
	 * 查找战舰i所在队列的代表（队首），并进行路径压缩
	 * 同时更新dist[i]为战舰i到队首的距离
	 * 使用迭代而非递归，避免栈溢出
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param i 要查找的战舰编号
	 * @return 战舰i所在队列的代表（队首）
	 */
	// 迭代的方式实现find，递归方式实现会爆栈
	public static int find(int i) {
		// 使用栈模拟递归过程
		int si = 0;
		// 找到根节点
		while (i != father[i]) {
			stack[++si] = i;
			i = father[i];
		}
		stack[si + 1] = i;
		// 从根节点开始，向上更新距离
		for (int j = si; j >= 1; j--) {
			father[stack[j]] = i;
			// 更新距离：当前战舰到队首的距离 = 当前战舰到父节点的距离 + 父节点到队首的距离
			dist[stack[j]] += dist[stack[j + 1]];
		}
		return i;
	}

	/**
	 * 合并两个队列，将l号战舰所在队列整体移动到r号战舰所在队列末尾
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param l 战舰l的编号
	 * @param r 战舰r的编号
	 */
	public static void union(int l, int r) {
		// 查找两个战舰所在队列的代表
		int lf = find(l), rf = find(r);
		// 如果不在同一队列中
		if (lf != rf) {
			// 将l所在队列合并到r所在队列末尾
			father[lf] = rf;
			// 更新l所在队列队首到r所在队列队首的距离
			// 距离 = r所在队列的战舰数量（即r所在队列末尾到新队首的距离）
			dist[lf] += size[rf];
			// 更新新队列的战舰数量
			size[rf] += size[lf];
		}
	}

	/**
	 * 查询两艘战舰之间间隔的战舰数量
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param l 战舰l的编号
	 * @param r 战舰r的编号
	 * @return 间隔的战舰数量，如果不在同一队列中返回-1
	 */
	public static int query(int l, int r) {
		// 如果两艘战舰不在同一队列中
		if (find(l) != find(r)) {
			return -1;
		}
		// 间隔数量 = 两艘战舰到队首距离的差值的绝对值 - 1
		// 减1是因为不计算两艘战舰本身
		return Math.abs(dist[l] - dist[r]) - 1;
	}

	public static void main(String[] args) {
		prepare();
		Kattio io = new Kattio();
		int t = io.nextInt();
		String op;
		// 处理t个操作
		for (int i = 1, l, r; i <= t; i++) {
			op = io.next();
			l = io.nextInt();
			r = io.nextInt();
			// 根据操作类型执行相应操作
			if (op.equals("M")) {
				// 合并队列
				union(l, r);
			} else {
				// 查询间隔
				io.println(query(l, r));
			}
		}
		io.flush();
		io.close();
	}

	// 读写工具类
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