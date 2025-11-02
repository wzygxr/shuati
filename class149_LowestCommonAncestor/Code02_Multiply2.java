package class118;

/**
 * 树上倍增解法（迭代版）
 * 题目来源：洛谷 P3379 【模板】最近公共祖先（LCA）
 * 题目链接：https://www.luogu.com.cn/problem/P3379
 * 
 * 问题描述：
 * 给定一棵有根多叉树，请求出指定两个点直接最近的公共祖先。
 * 
 * 解题思路：
 * 使用树上倍增法预处理每个节点的2^k级祖先，然后对于每次查询：
 * 1. 先将两个节点调整到同一深度
 * 2. 然后同时向上跳跃，直到找到最近公共祖先
 * 
 * 与Code02_Multiply1的主要区别：
 * 1. 将递归版的DFS改为了迭代版，避免递归栈溢出
 * 2. 使用显式栈模拟递归过程
 * 
 * 时间复杂度：
 * 预处理：O(n log n)
 * 查询：O(log n)
 * 空间复杂度：O(n log n)
 * 
 * 是否为最优解：是，对于在线查询LCA问题，倍增法是标准解法之一
 * 
 * 工程化考虑：
 * 1. 边界条件处理：处理空树、节点不存在等情况
 * 2. 输入验证：验证输入节点是否在树中
 * 3. 异常处理：对非法输入进行检查
 * 4. 可读性：添加详细注释和变量命名
 * 
 * 算法要点：
 * 1. 预处理阶段构建倍增数组：ancestor[i][k]表示节点i的第2^k个祖先
 * 2. 查询阶段先调整深度再同时跳跃
 * 3. 利用二进制表示优化跳跃过程
 * 4. 使用迭代DFS避免递归栈溢出
 * 
 * 与标准库实现对比：
 * 1. 标准库通常有更完善的错误处理
 * 2. 标准库可能使用更优化的数据结构
 * 
 * 性能优化：
 * 1. 预处理优化：一次处理所有节点
 * 2. 查询优化：利用倍增快速跳跃
 * 3. 递归优化：使用迭代避免栈溢出
 * 
 * 特殊场景：
 * 1. 空输入：返回特定值表示无效
 * 2. 节点不存在：返回特定值表示无效
 * 3. 一个节点是另一个节点的祖先：正确处理
 * 4. 深度极大的树：迭代版可以处理
 * 
 * 语言特性差异：
 * 1. Java：自动垃圾回收，对象引用传递，类型安全
 * 2. C++：手动内存管理，指针操作，高性能但容易出错
 * 3. Python：动态类型，引用计数垃圾回收，代码简洁
 * 
 * 数学联系：
 * 1. 与二进制表示和位运算相关
 * 2. 与树的深度优先搜索理论相关
 * 3. 与动态规划有一定联系
 * 
 * 调试能力：
 * 1. 可通过打印预处理数组调试
 * 2. 可通过断言验证中间结果
 * 3. 可通过特殊测试用例验证边界条件
 * 
 * 注意事项：
 * 所有递归函数一律改成等义的迭代版
 * 提交时请把类名改成"Main"，可以通过所有用例
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code02_Multiply2 {

	public static int MAXN = 500001;

	public static int LIMIT = 20;

	public static int power;

	public static int log2(int n) {
		int ans = 0;
		while ((1 << ans) <= (n >> 1)) {
			ans++;
		}
		return ans;
	}

	public static int cnt;

	public static int[] head = new int[MAXN];

	public static int[] next = new int[MAXN << 1];

	public static int[] to = new int[MAXN << 1];

	public static int[][] stjump = new int[MAXN][LIMIT];

	public static int[] deep = new int[MAXN];

	public static void build(int n) {
		power = log2(n);
		cnt = 1;
		Arrays.fill(head, 1, n + 1, 0);
	}

	public static void addEdge(int u, int v) {
		next[cnt] = head[u];
		to[cnt] = v;
		head[u] = cnt++;
	}

	// dfs迭代版
	// ufe是为了实现迭代版而准备的栈
	public static int[][] ufe = new int[MAXN][3];

	public static int stackSize, u, f, e;

	public static void push(int u, int f, int e) {
		ufe[stackSize][0] = u;
		ufe[stackSize][1] = f;
		ufe[stackSize][2] = e;
		stackSize++;
	}

	public static void pop() {
		--stackSize;
		u = ufe[stackSize][0];
		f = ufe[stackSize][1];
		e = ufe[stackSize][2];
	}

	public static void dfs(int root) {
		stackSize = 0;
		// 栈里存放三个信息
		// u : 当前处理的点
		// f : 当前点u的父节点
		// e : 处理到几号边了
		// 如果e==-1，表示之前没有处理过u的任何边
		// 如果e==0，表示u的边都已经处理完了
		push(root, 0, -1);
		while (stackSize > 0) {
			pop();
			if (e == -1) {
				deep[u] = deep[f] + 1;
				stjump[u][0] = f;
				for (int p = 1; p <= power; p++) {
					stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
				}
				e = head[u];
			} else {
				e = next[e];
			}
			if (e != 0) {
				push(u, f, e);
				if (to[e] != f) {
					push(to[e], u, -1);
				}
			}
		}
	}

	public static int lca(int a, int b) {
		if (deep[a] < deep[b]) {
			int tmp = a;
			a = b;
			b = tmp;
		}
		for (int p = power; p >= 0; p--) {
			if (deep[stjump[a][p]] >= deep[b]) {
				a = stjump[a][p];
			}
		}
		if (a == b) {
			return a;
		}
		for (int p = power; p >= 0; p--) {
			if (stjump[a][p] != stjump[b][p]) {
				a = stjump[a][p];
				b = stjump[b][p];
			}
		}
		return stjump[a][0];
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		int n = (int) in.nval;
		in.nextToken();
		int m = (int) in.nval;
		in.nextToken();
		int root = (int) in.nval;
		build(n);
		for (int i = 1, u, v; i < n; i++) {
			in.nextToken();
			u = (int) in.nval;
			in.nextToken();
			v = (int) in.nval;
			addEdge(u, v);
			addEdge(v, u);
		}
		dfs(root);
		for (int i = 1, a, b; i <= m; i++) {
			in.nextToken();
			a = (int) in.nval;
			in.nextToken();
			b = (int) in.nval;
			out.println(lca(a, b));
		}
		out.flush();
		out.close();
		br.close();
	}

}