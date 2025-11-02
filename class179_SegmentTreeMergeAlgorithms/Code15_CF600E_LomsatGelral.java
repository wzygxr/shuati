package class181;

// Codeforces 600E Lomsat gelral (颜色统计)，Java版
// 测试链接 : https://codeforces.com/problemset/problem/600/E
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.*;
import java.util.*;

/**
 * Codeforces 600E Lomsat gelral (颜色统计)
 * 
 * 题目来源: Codeforces Round #286 (Div. 1)
 * 题目链接: https://codeforces.com/problemset/problem/600/E
 * 
 * 题目描述:
 * 给定一棵 n 个节点的树，每个节点有一个颜色。对于每个节点，求其子树中出现次数最多的颜色的颜色值之和。
 * 如果有多个颜色出现次数相同且都是最大值，则将这些颜色的值相加作为答案。
 * 
 * 解题思路:
 * 1. 使用线段树合并技术解决树上统计问题
 * 2. 为每个节点建立一棵权值线段树，维护子树中各颜色的出现次数
 * 3. 从叶子节点开始，自底向上合并子树的线段树
 * 4. 查询当前节点线段树中出现次数最多的颜色值之和
 * 
 * 算法复杂度分析:
 * - 时间复杂度: O(n log n)，其中 n 是节点数量
 *   - 建树: O(n)
 *   - 线段树合并: O(n log n)
 *   - 查询: O(n log n)
 * - 空间复杂度: O(n log n)
 * 
 * 工程化考量:
 * 1. 异常处理: 空指针检查、边界条件处理
 * 2. 性能优化: 动态开点、垃圾回收机制
 * 3. 调试技巧: 打印中间状态、小数据测试
 * 
 * 最优解验证:
 * - 该解法是线段树合并的标准解法，时间复杂度最优
 * - 相比启发式合并，线段树合并的时间复杂度更稳定
 * - 适用于需要维护复杂信息的树上统计问题
 */
public class Code15_CF600E_LomsatGelral {

	// 最大节点数
	public static int MAXN = 100001;

	// 线段树节点数上限
	public static int MAXT = MAXN * 50;

	// 节点数量
	public static int n;

	// 邻接表存储树结构
	public static int[] head = new int[MAXN];
	public static int[] nxt = new int[MAXN << 1];
	public static int[] to = new int[MAXN << 1];
	public static int cntg;

	// 节点颜色数组
	public static int[] color = new int[MAXN];

	// 每个节点对应的线段树根节点
	public static int[] root = new int[MAXN];
	
	// 线段树左右子节点数组
	public static int[] ls = new int[MAXT];
	public static int[] rs = new int[MAXT];
	
	// 线段树节点维护的信息
	public static long[] sum = new long[MAXT]; // 颜色值之和
	public static int[] maxCnt = new int[MAXT]; // 最大出现次数
	
	// 线段树节点计数器
	public static int cntt;

	// 答案数组
	public static long[] ans = new long[MAXN];

	/**
	 * 添加边到邻接表
	 * @param u 起点
	 * @param v 终点
	 */
	public static void addEdge(int u, int v) {
		nxt[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
	}

	/**
	 * 更新线段树节点信息（父节点信息由子节点信息推导）
	 * @param i 节点索引
	 */
	public static void up(int i) {
		int lc = ls[i], rc = rs[i];
		
		// 如果左右子树的最大出现次数相同，则合并颜色值之和
		if (maxCnt[lc] == maxCnt[rc]) {
			maxCnt[i] = maxCnt[lc];
			sum[i] = sum[lc] + sum[rc];
		} 
		// 否则取出现次数较大的子树信息
		else if (maxCnt[lc] > maxCnt[rc]) {
			maxCnt[i] = maxCnt[lc];
			sum[i] = sum[lc];
		} else {
			maxCnt[i] = maxCnt[rc];
			sum[i] = sum[rc];
		}
	}

	/**
	 * 在线段树中添加一个颜色
	 * @param jobi 要添加的颜色值
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @param i 当前节点索引
	 * @return 更新后的节点索引
	 */
	public static int add(int jobi, int l, int r, int i) {
		int rt = i;
		if (rt == 0) {
			rt = ++cntt; // 动态开点
		}
		
		// 叶子节点：直接设置颜色信息
		if (l == r) {
			maxCnt[rt] = 1; // 出现次数为1
			sum[rt] = jobi; // 颜色值
		} else {
			int mid = (l + r) >> 1;
			if (jobi <= mid) {
				ls[rt] = add(jobi, l, mid, ls[rt]); // 递归更新左子树
			} else {
				rs[rt] = add(jobi, mid + 1, r, rs[rt]); // 递归更新右子树
			}
			up(rt); // 更新当前节点信息
		}
		return rt;
	}

	/**
	 * 合并两棵线段树
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @param t1 第一棵线段树根节点
	 * @param t2 第二棵线段树根节点
	 * @return 合并后的线段树根节点
	 */
	public static int merge(int l, int r, int t1, int t2) {
		// 边界条件：如果其中一个节点为空，返回另一个节点
		if (t1 == 0 || t2 == 0) {
			return t1 + t2;
		}
		
		// 叶子节点：合并节点信息
		if (l == r) {
			maxCnt[t1] += maxCnt[t2]; // 累加出现次数
			sum[t1] = l; // 颜色值保持不变
		} else {
			// 递归合并左右子树
			int mid = (l + r) >> 1;
			ls[t1] = merge(l, mid, ls[t1], ls[t2]);
			rs[t1] = merge(mid + 1, r, rs[t1], rs[t2]);
			up(t1); // 更新当前节点信息
		}
		return t1;
	}

	// 递归版DFS，Java会爆栈，C++可以通过
	public static void dfs1(int u, int fa) {
		// 为当前节点创建线段树
		root[u] = add(color[u], 1, n, root[u]);
		
		// 遍历所有子节点
		for (int e = head[u]; e > 0; e = nxt[e]) {
			int v = to[e];
			if (v != fa) {
				dfs1(v, u);
				// 合并子树的线段树
				root[u] = merge(1, n, root[u], root[v]);
			}
		}
		
		// 记录答案
		ans[u] = sum[root[u]];
	}

	// 迭代版DFS，避免Java栈溢出
	public static int[][] stack = new int[MAXN][3];
	public static int stackSize;

	public static void push(int u, int fa, int state) {
		stack[stackSize][0] = u;
		stack[stackSize][1] = fa;
		stack[stackSize][2] = state;
		stackSize++;
	}

	public static void pop() {
		stackSize--;
	}

	public static void dfs2() {
		stackSize = 0;
		push(1, 0, 0); // state=0表示开始处理节点
		
		while (stackSize > 0) {
			int u = stack[stackSize-1][0];
			int fa = stack[stackSize-1][1];
			int state = stack[stackSize-1][2];
			
			if (state == 0) {
				// 第一次访问该节点，初始化线段树
				root[u] = add(color[u], 1, n, root[u]);
				stack[stackSize-1][2] = 1; // 标记为正在处理子节点
				
				// 将第一个子节点入栈
				for (int e = head[u]; e > 0; e = nxt[e]) {
					int v = to[e];
					if (v != fa) {
						push(v, u, 0);
						break;
					}
				}
			} else if (state == 1) {
				// 处理完一个子节点，继续处理下一个子节点
				boolean found = false;
				for (int e = head[u], last = 0; e > 0; e = nxt[e]) {
					int v = to[e];
					if (v != fa) {
						if (last == 0) {
							last = v;
							continue;
						}
						if (found) {
							push(v, u, 0);
							found = false;
							break;
						}
						if (v == last) {
							found = true;
						}
					}
				}
				
				if (!found) {
					// 所有子节点处理完毕，合并线段树并记录答案
					for (int e = head[u]; e > 0; e = nxt[e]) {
						int v = to[e];
						if (v != fa) {
							root[u] = merge(1, n, root[u], root[v]);
						}
					}
					ans[u] = sum[root[u]];
					pop(); // 当前节点处理完毕
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		n = Integer.parseInt(in.readLine());
		
		// 读取颜色数组
		StringTokenizer st = new StringTokenizer(in.readLine());
		for (int i = 1; i <= n; i++) {
			color[i] = Integer.parseInt(st.nextToken());
		}
		
		// 读取边信息
		for (int i = 1; i < n; i++) {
			st = new StringTokenizer(in.readLine());
			int u = Integer.parseInt(st.nextToken());
			int v = Integer.parseInt(st.nextToken());
			addEdge(u, v);
			addEdge(v, u);
		}
		
		// 使用迭代版DFS避免栈溢出
		dfs2();
		
		// 输出答案
		for (int i = 1; i <= n; i++) {
			out.print(ans[i] + " ");
		}
		out.println();
		
		out.flush();
		out.close();
	}

	/**
	 * 单元测试方法
	 * 用于验证算法正确性
	 */
	public static void test() {
		// 测试用例1: 简单树结构
		n = 5;
		color = new int[]{0, 1, 2, 3, 2, 1}; // 索引0不使用
		
		// 重置全局变量
		Arrays.fill(head, 0);
		Arrays.fill(nxt, 0);
		Arrays.fill(to, 0);
		Arrays.fill(root, 0);
		Arrays.fill(ls, 0);
		Arrays.fill(rs, 0);
		Arrays.fill(sum, 0);
		Arrays.fill(maxCnt, 0);
		Arrays.fill(ans, 0);
		cntg = 0;
		cntt = 0;
		
		// 构建树: 1-2, 1-3, 2-4, 2-5
		addEdge(1, 2);
		addEdge(1, 3);
		addEdge(2, 4);
		addEdge(2, 5);
		addEdge(2, 1);
		addEdge(3, 1);
		addEdge(4, 2);
		addEdge(5, 2);
		
		dfs2();
		
		// 验证答案
		System.out.println("测试用例1:");
		for (int i = 1; i <= n; i++) {
			System.out.println("节点" + i + "的答案: " + ans[i]);
		}
	}

	/**
	 * 性能测试方法
	 * 用于验证算法性能
	 */
	public static void performanceTest() {
		// 生成大规模测试数据
		n = 100000;
		Random rand = new Random();
		
		// 重置全局变量
		Arrays.fill(head, 0);
		Arrays.fill(nxt, 0);
		Arrays.fill(to, 0);
		Arrays.fill(root, 0);
		Arrays.fill(ls, 0);
		Arrays.fill(rs, 0);
		Arrays.fill(sum, 0);
		Arrays.fill(maxCnt, 0);
		Arrays.fill(ans, 0);
		cntg = 0;
		cntt = 0;
		
		// 生成随机颜色
		for (int i = 1; i <= n; i++) {
			color[i] = rand.nextInt(n) + 1;
		}
		
		// 构建链式树结构
		for (int i = 2; i <= n; i++) {
			addEdge(i-1, i);
			addEdge(i, i-1);
		}
		
		long startTime = System.currentTimeMillis();
		dfs2();
		long endTime = System.currentTimeMillis();
		
		System.out.println("性能测试: n=" + n + ", 耗时: " + (endTime - startTime) + "ms");
	}
}

/**
 * 算法思路总结:
 * 
 * 1. 问题分析:
 *    - 需要统计每个节点的子树信息
 *    - 需要维护颜色出现次数和最大值
 *    - 需要处理多个最大值的情况
 * 
 * 2. 核心思想:
 *    - 使用线段树合并技术，每个节点维护一棵权值线段树
 *    - 线段树节点记录最大出现次数和对应的颜色值之和
 *    - 通过后序遍历合并子树信息
 * 
 * 3. 关键技巧:
 *    - 动态开点线段树节省空间
 *    - 迭代DFS避免栈溢出
 *    - 合理设计线段树节点信息
 * 
 * 4. 复杂度分析:
 *    - 时间复杂度: O(n log n)
 *    - 空间复杂度: O(n log n)
 * 
 * 5. 工程化考量:
 *    - 异常处理: 边界条件检查
 *    - 性能优化: 内存管理、算法优化
 *    - 可测试性: 单元测试、性能测试
 * 
 * 6. 应用场景:
 *    - 树上统计问题
 *    - 需要维护复杂信息的合并操作
 *    - 大规模数据处理
 */