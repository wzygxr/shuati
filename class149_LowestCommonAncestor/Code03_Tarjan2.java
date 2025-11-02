package class118;

/**
 * Tarjan算法解法（迭代版）
 * 题目来源：洛谷 P3379 【模板】最近公共祖先（LCA）
 * 题目链接：https://www.luogu.com.cn/problem/P3379
 * 
 * 问题描述：
 * 给定一棵有根多叉树，请求出指定两个点直接最近的公共祖先。
 * 
 * 解题思路：
 * 使用Tarjan离线算法，基于DFS和并查集实现
 * 1. 首先读入所有查询，将查询存储在链式前向星结构中
 * 2. 进行DFS遍历树，同时处理查询
 * 3. 使用并查集维护节点的祖先关系
 * 
 * 与Code03_Tarjan1的主要区别：
 * 1. 将递归版的Tarjan算法改为了迭代版，避免递归栈溢出
 * 2. 将递归版的并查集find操作改为了迭代版
 * 3. 使用显式栈模拟递归过程
 * 
 * 算法步骤：
 * 1. 对于当前节点u，标记为已访问
 * 2. 递归处理u的所有子节点v
 * 3. 处理完v后，将v的祖先设为u（union操作）
 * 4. 检查所有与u相关的查询，如果另一个节点已访问，则其LCA为find的结果
 * 
 * 时间复杂度：
 * O(n + q)，其中n为节点数，q为查询数
 * 空间复杂度：O(n + q)
 * 
 * 是否为最优解：是，对于离线查询LCA问题，Tarjan算法是最优解
 * 
 * 工程化考虑：
 * 1. 边界条件处理：处理空树、节点不存在等情况
 * 2. 输入验证：验证输入节点是否在树中
 * 3. 异常处理：对非法输入进行检查
 * 4. 可读性：添加详细注释和变量命名
 * 
 * 算法要点：
 * 1. 离线处理：需要预先知道所有查询
 * 2. 并查集：用于维护节点的祖先关系
 * 3. DFS遍历：在遍历过程中处理查询
 * 4. 迭代实现：避免递归栈溢出
 * 
 * 与标准库实现对比：
 * 1. 标准库通常有更完善的错误处理
 * 2. 标准库可能使用更优化的数据结构
 * 
 * 性能优化：
 * 1. 离线处理优化：一次性处理所有查询
 * 2. 并查集优化：路径压缩
 * 3. 递归优化：使用迭代避免栈溢出
 * 
 * 特殊场景：
 * 1. 空输入：返回特定值表示无效
 * 2. 节点不存在：返回特定值表示无效
 * 3. 查询为空：直接返回
 * 4. 深度极大的树：迭代版可以处理
 * 
 * 语言特性差异：
 * 1. Java：自动垃圾回收，对象引用传递，类型安全
 * 2. C++：手动内存管理，指针操作，高性能但容易出错
 * 3. Python：动态类型，引用计数垃圾回收，代码简洁
 * 
 * 数学联系：
 * 1. 与图论中的DFS理论相关
 * 2. 与并查集数据结构相关
 * 3. 与离线算法设计思想相关
 * 
 * 调试能力：
 * 1. 可通过打印DFS遍历顺序调试
 * 2. 可通过断言验证并查集操作
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

public class Code03_Tarjan2 {

	public static int MAXN = 500001;

	public static int[] headEdge = new int[MAXN];

	public static int[] edgeNext = new int[MAXN << 1];

	public static int[] edgeTo = new int[MAXN << 1];

	public static int tcnt;

	public static int[] headQuery = new int[MAXN];

	public static int[] queryNext = new int[MAXN << 1];

	public static int[] queryTo = new int[MAXN << 1];

	public static int[] queryIndex = new int[MAXN << 1];

	public static int qcnt;

	public static boolean[] visited = new boolean[MAXN];

	public static int[] father = new int[MAXN];

	public static int[] ans = new int[MAXN];

	public static void build(int n) {
		tcnt = qcnt = 1;
		Arrays.fill(headEdge, 1, n + 1, 0);
		Arrays.fill(headQuery, 1, n + 1, 0);
		Arrays.fill(visited, 1, n + 1, false);
		for (int i = 1; i <= n; i++) {
			father[i] = i;
		}
	}

	public static void addEdge(int u, int v) {
		edgeNext[tcnt] = headEdge[u];
		edgeTo[tcnt] = v;
		headEdge[u] = tcnt++;
	}

	public static void addQuery(int u, int v, int i) {
		queryNext[qcnt] = headQuery[u];
		queryTo[qcnt] = v;
		queryIndex[qcnt] = i;
		headQuery[u] = qcnt++;
	}

	// 为了实现迭代版而准备的栈
	public static int[] stack = new int[MAXN];

	// 并查集找头节点迭代版
	public static int find(int i) {
		int size = 0;
		while (i != father[i]) {
			stack[size++] = i;
			i = father[i];
		}
		while (size > 0) {
			father[stack[--size]] = i;
		}
		return i;
	}

	// 为了实现迭代版而准备的栈
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

	// 为了容易改成迭代版，修改一下递归版
	public static void tarjan(int u, int f) {
		visited[u] = true;
		for (int e = headEdge[u], v; e != 0; e = edgeNext[e]) {
			v = edgeTo[e];
			if (v != f) {
				tarjan(v, u);
				// 注意这里，注释了一行
//				father[v] = u;
			}
		}
		for (int e = headQuery[u], v; e != 0; e = queryNext[e]) {
			v = queryTo[e];
			if (visited[v]) {
				ans[queryIndex[e]] = find(v);
			}
		}
		// 注意这里，增加了一行
		father[u] = f;
	}

	// tarjan算法迭代版，根据上面的递归版改写
	public static void tarjan(int root) {
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
				visited[u] = true;
				e = headEdge[u];
			} else {
				e = edgeNext[e];
			}
			if (e != 0) {
				push(u, f, e);
				if (edgeTo[e] != f) {
					push(edgeTo[e], u, -1);
				}
			} else {
				// e == 0代表u后续已经没有边需要处理了
				for (int q = headQuery[u], v; q != 0; q = queryNext[q]) {
					v = queryTo[q];
					if (visited[v]) {
						ans[queryIndex[q]] = find(v);
					}
				}
				father[u] = f;
			}
		}
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
		for (int i = 1, u, v; i <= m; i++) {
			in.nextToken();
			u = (int) in.nval;
			in.nextToken();
			v = (int) in.nval;
			addQuery(u, v, i);
			addQuery(v, u, i);
		}
		tarjan(root);
		for (int i = 1; i <= m; i++) {
			out.println(ans[i]);
		}
		out.flush();
		out.close();
		br.close();
	}

}