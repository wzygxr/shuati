# 【力扣】BasicEdgeDivideTemplate-边分治模板题-中等

## 题目原始链接
- 模板题，参考：https://hydro.ac/p/bzoj-P2870
- 类似题目：https://darkbzoj.cc/problem/2870

## 题目完整描述
给定一棵有n个节点的树，每个节点有点权，给定n-1条边。树上任何一条链的指标 = 链的节点数 * 链上节点的最小值。求这个指标的最大值。

输入格式：
- 第一行：n (1 <= n <= 5 * 10^4)
- 第二行：n个整数，表示每个节点的点权
- 接下来n-1行：每行两个整数u, v，表示节点u和v之间有一条边

输出格式：
- 一个整数，表示指标的最大值

## 笔试/面试考察点分析
- 考察边的重心求解：通过寻找边的重心来分割树结构，降低复杂度
- 边分治分割逻辑：将树通过边分割成更小的子树，递归处理
- 边分树构建：构建边分树以支持路径查询
- 复杂度分析：O(n log n)时间复杂度的推导与实现
- 多叉树转二叉树：避免重儿子导致的复杂度退化
- 与ML/DL的关联：树结构数据在机器学习中的表示与处理

## 解题思路
1. 首先将多叉树转化为二叉树，避免边分治中重儿子导致的复杂度退化
2. 通过边分治找到边的重心，递归分割树结构
3. 对于每条分割边，统计经过该边的路径信息
4. 使用双指针或排序技术统计路径，计算指标最大值

## 完整代码实现

```java
package class187;

// 最长道路tree，java版
// 一共n个节点，每个点给定点权，给定n-1条边，所有节点组成一棵树
// 树上任何一条链的指标 = 链的节点数 * 链上节点的最小值
// 打印这个指标的最大值
// 1 <= n <= 5 * 10^4
// 测试链接 : https://hydro.ac/p/bzoj-P2870
// 测试链接 : https://darkbzoj.cc/problem/2870
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class BasicEdgeDivideTemplate {

	public static int MAXN = 100001; // 定义数组最大长度，边分治处理多叉树转二叉树后节点数可能翻倍，故设100001，笔试需根据数据范围调整
	public static int n, cntn; // n为原始树节点数，cntn为多叉树转二叉树后的总节点数，笔试中需注意节点编号扩展
	public static int[] arr = new int[MAXN]; // 存储每个节点的点权值

	public static int[] head1 = new int[MAXN]; // 原始树的邻接表头指针，ML中动态树数据常用邻接表预处理
	public static int[] next1 = new int[MAXN << 1]; // 原始树的邻接表next指针
	public static int[] to1 = new int[MAXN << 1]; // 原始树的邻接表目标节点
	public static int[] weight1 = new int[MAXN << 1]; // 原始树的边权（本题边权为1）
	public static int cnt1; // 原始树边的计数

	public static int[] head2 = new int[MAXN]; // 重构后树的邻接表头指针
	public static int[] next2 = new int[MAXN << 1]; // 重构后树的邻接表next指针
	public static int[] to2 = new int[MAXN << 1]; // 重构后树的邻接表目标节点
	public static int[] weight2 = new int[MAXN << 1]; // 重构后树的边权
	public static int cnt2; // 重构后树边的计数

	public static boolean[] vis = new boolean[MAXN]; // 标记边是否被分割（边分治核心标记数组），面试中需说明边标记的作用
	public static int[] siz = new int[MAXN]; // 存储子树大小，用于求解边的重心

	public static int[] ledge = new int[MAXN]; // 存储左子树的路径信息（边长）
	public static int[] lminv = new int[MAXN]; // 存储左子树的路径信息（最小值）
	public static int cntl; // 左子树路径信息计数

	public static int[] redge = new int[MAXN]; // 存储右子树的路径信息（边长）
	public static int[] rminv = new int[MAXN]; // 存储右子树的路径信息（最小值）
	public static int cntr; // 右子树路径信息计数

	public static long ans; // 存储最终答案

	// 讲解118，递归函数改成迭代所需要的栈
	// 由于Java递归深度限制，使用手动栈模拟递归，笔试中需考虑栈溢出问题
	public static int[][] stack = new int[MAXN][6]; // 手动栈，存储递归参数
	public static int u, f, edge, minv, op, e; // 递归参数变量
	public static int stacksize; // 栈大小

	// 入栈操作
	public static void push(int u, int f, int edge, int minv, int op, int e) {
		stack[stacksize][0] = u; // 存储当前节点
		stack[stacksize][1] = f; // 存储父节点
		stack[stacksize][2] = edge; // 存储边信息
		stack[stacksize][3] = minv; // 存储最小值
		stack[stacksize][4] = op; // 存储操作类型
		stack[stacksize][5] = e; // 存储边编号
		stacksize++; // 栈大小增加
	}

	// 出栈操作
	public static void pop() {
		--stacksize; // 栈大小减少
		u = stack[stacksize][0]; // 恢复当前节点
		f = stack[stacksize][1]; // 恢复父节点
		edge = stack[stacksize][2]; // 恢复边信息
		minv = stack[stacksize][3]; // 恢复最小值
		op = stack[stacksize][4]; // 恢复操作类型
		e = stack[stacksize][5]; // 恢复边编号
	}

	// 添加原始树的边
	// 笔试中邻接表建图是基础操作，需熟练掌握
	public static void addEdge1(int u, int v, int w) {
		next1[++cnt1] = head1[u]; // 头插法添加边
		to1[cnt1] = v; // 记录目标节点
		weight1[cnt1] = w; // 记录边权
		head1[u] = cnt1; // 更新头指针
	}

	// 添加重构后树的边
	// 重构后树的邻接表构建，用于边分治处理
	public static void addEdge2(int u, int v, int w) {
		next2[++cnt2] = head2[u]; // 头插法添加边
		to2[cnt2] = v; // 记录目标节点
		weight2[cnt2] = w; // 记录边权
		head2[u] = cnt2; // 更新头指针
	}

	// 多叉树转二叉树：将多叉树转化为二叉树，避免边分治中重儿子导致的复杂度退化
	// 笔试中该步骤是边分治的关键预处理，需熟练实现；ML中可用于树结构的标准化处理
	public static void rebuild1(int u, int fa) {
		int last = 0; // 记录当前节点的最后一个子节点（用于构建右兄弟指针）
		for (int e = head1[u]; e > 0; e = next1[e]) { // 遍历当前节点的所有邻接边
			int v = to1[e];
			int w = weight1[e];
			if (v != fa) { // 排除父节点，避免循环访问
				if (last == 0) { // 第一个子节点作为左儿子
					last = u; // 更新last为当前节点
					addEdge2(u, v, w); // 连接当前节点和第一个子节点
					addEdge2(v, u, w); // 添加反向边
				} else { // 后续子节点作为前一个子节点的右兄弟
					int add = ++cntn; // 创建新的辅助节点
					arr[add] = arr[u]; // 新节点的权值等于父节点
					addEdge2(last, add, 0); // 连接前一个子节点和新节点（边权设为0）
					addEdge2(add, last, 0); // 添加反向边
					addEdge2(add, v, w); // 连接新节点和当前子节点
					addEdge2(v, add, w); // 添加反向边
					last = add; // 更新last为新节点
				}
				rebuild1(v, u); // 递归处理子节点的多叉树转二叉树
			}
		}
	}

	// rebuild1迭代版：避免Java栈溢出问题
	// 面试中需要说明：当递归深度过大时，如何手动模拟递归栈
	public static void rebuild2(int cur, int fa) {
		stacksize = 0; // 初始化栈大小
		push(cur, fa, 0, 0, 0, -1); // 将初始状态压入栈
		while (stacksize > 0) { // 当栈不为空时继续处理
			pop(); // 弹出栈顶元素
			if (e == -1) { // 如果是初次访问该节点
				int last = 0; // 记录当前节点的最后一个子节点
				for (int ei = head1[u]; ei > 0; ei = next1[ei]) { // 遍历当前节点的所有邻接边
					int v = to1[ei];
					int w = weight1[ei];
					if (v != f) { // 排除父节点
						if (last == 0) { // 第一个子节点作为左儿子
							last = u; // 更新last为当前节点
							addEdge2(u, v, w); // 连接当前节点和第一个子节点
							addEdge2(v, u, w); // 添加反向边
						} else { // 后续子节点作为前一个子节点的右兄弟
							int add = ++cntn; // 创建新的辅助节点
							arr[add] = arr[u]; // 新节点的权值等于父节点
							addEdge2(last, add, 0); // 连接前一个子节点和新节点（边权设为0）
							addEdge2(add, last, 0); // 添加反向边
							addEdge2(add, v, w); // 连接新节点和当前子节点
							addEdge2(v, add, w); // 添加反向边
							last = add; // 更新last为新节点
						}
					}
				}
				e = head1[u]; // 设置边索引为当前节点的第一条边
			} else {
				e = next1[e]; // 移动到下一条边
			}
			if (e != 0) { // 如果还有边未处理
				push(u, f, 0, 0, 0, e); // 将当前状态压入栈
				int v = to1[e];
				if (v != f) { // 如果目标节点不是父节点
					push(v, u, 0, 0, 0, -1); // 将子节点信息压入栈
				}
			}
		}
	}

	// 得到子树大小递归版，java会爆栈，C++可以通过
	// 笔试中需要考虑递归深度限制，面试中可能被问到如何处理栈溢出
	public static void getSize1(int u, int fa) {
		siz[u] = 1; // 初始化子树大小为1（包含当前节点）
		for (int e = head2[u]; e > 0; e = next2[e]) { // 遍历当前节点的所有邻接边
			int v = to2[e];
			if (v != fa && !vis[e >> 1]) { // 排除父节点和已分割的边
				getSize1(v, u); // 递归计算子树大小
				siz[u] += siz[v]; // 累加子树大小
			}
		}
	}

	// getSize1的迭代版：避免Java栈溢出问题
	// 笔试中该函数是边分治的基础，需快速手写；ML中可用于提取子树规模特征
	public static void getSize2(int cur, int fa) {
		stacksize = 0; // 初始化栈大小
		push(cur, fa, 0, 0, 0, -1); // 将初始状态压入栈
		while (stacksize > 0) { // 当栈不为空时继续处理
			pop(); // 弹出栈顶元素
			if (e == -1) { // 如果是初次访问该节点
				siz[u] = 1; // 初始化子树大小为1
				e = head2[u]; // 设置边索引为当前节点的第一条边
			} else {
				e = next2[e]; // 移动到下一条边
			}
			if (e != 0) { // 如果还有边未处理
				push(u, f, 0, 0, 0, e); // 将当前状态压入栈
				int v = to2[e];
				if (v != f && !vis[e >> 1]) { // 如果目标节点不是父节点且边未被分割
					push(v, u, 0, 0, 0, -1); // 将子节点信息压入栈
				}
			} else { // 如果所有边都处理完了
				for (int ei = head2[u]; ei > 0; ei = next2[ei]) { // 再次遍历所有边，累加子树大小
					int v = to2[ei];
					if (v != f && !vis[ei >> 1]) { // 排除父节点和已分割的边
						siz[u] += siz[v]; // 累加子树大小
					}
				}
			}
		}
	}

	// 寻找边的重心：输入当前节点、父节点、总子树大小，找到使分割后最大子树最小的边
	// 面试高频考点：边的重心定义、寻找逻辑，与点的重心的区别；ML中边重心可作为树的关键分割特征
	public static int getCentroidEdge(int u, int fa) {
		// getSize1(u, fa); // 递归版本（可能导致栈溢出）
		getSize2(u, fa); // 迭代版本（避免栈溢出）
		int total = siz[u]; // 获取总子树大小
		int half = total >> 1; // 计算一半大小（用于判断是否为重心）
		boolean find = false; // 标记是否找到边重心
		while (!find) { // 循环直到找到边重心
			find = true; // 假设已经找到
			for (int e = head2[u]; e > 0; e = next2[e]) { // 遍历当前节点的所有邻接边
				int v = to2[e];
				if (v != fa && !vis[e >> 1] && siz[v] > half) { // 如果子节点大小超过一半
					fa = u; // 更新父节点
					u = v; // 更新当前节点
					find = false; // 未找到，继续循环
					break; // 跳出内层循环
				}
			}
		}
		int best = 0, edge = 0; // best记录最大子树大小，edge记录边重心
		for (int e = head2[u]; e > 0; e = next2[e]) { // 遍历当前节点的所有邻接边
			if (!vis[e >> 1]) { // 排除已分割的边
				int v = to2[e];
				int sub = v == fa ? (total - siz[u]) : siz[v]; // 分割该边后，两个子树的较小大小
				if (sub > best) { // 寻找最大子树最小的边（边重心）
					best = sub; // 更新最大子树大小
					edge = e; // 记录边重心
				}
			}
		}
		return edge; // 返回边重心对应的边编号
	}

	// 收集信息递归版，java会爆栈，C++可以通过
	// 笔试中需要考虑递归深度限制，面试中可能被问到如何处理栈溢出
	public static void dfs1(int u, int fa, int edge, int minv, int op) {
		if (op == 0) { // 如果是左子树
			ledge[++cntl] = edge; // 记录路径长度
			lminv[cntl] = minv; // 记录路径上的最小值
		} else { // 如果是右子树
			redge[++cntr] = edge; // 记录路径长度
			rminv[cntr] = minv; // 记录路径上的最小值
		}
		for (int e = head2[u]; e > 0; e = next2[e]) { // 遍历当前节点的所有邻接边
			int v = to2[e];
			if (v != fa && !vis[e >> 1]) { // 排除父节点和已分割的边
				dfs1(v, u, edge + weight2[e], Math.min(minv, arr[v]), op); // 递归处理子节点
			}
		}
	}

	// dfs1的迭代版：避免Java栈溢出问题
	// 面试中需要说明：如何手动实现递归逻辑，这是边分治中的重要技巧
	public static void dfs2(int cur, int fa, int pedge, int pminv, int opt) {
		stacksize = 0; // 初始化栈大小
		push(cur, fa, pedge, pminv, opt, -1); // 将初始状态压入栈
		while (stacksize > 0) { // 当栈不为空时继续处理
			pop(); // 弹出栈顶元素
			if (e == -1) { // 如果是初次访问该节点
				if (op == 0) { // 如果是左子树
					ledge[++cntl] = edge; // 记录路径长度
					lminv[cntl] = minv; // 记录路径上的最小值
				} else { // 如果是右子树
					redge[++cntr] = edge; // 记录路径长度
					rminv[cntr] = minv; // 记录路径上的最小值
				}
				e = head2[u]; // 设置边索引为当前节点的第一条边
			} else {
				e = next2[e]; // 移动到下一条边
			}
			if (e != 0) { // 如果还有边未处理
				push(u, f, edge, minv, op, e); // 将当前状态压入栈
				int v = to2[e];
				if (v != f && !vis[e >> 1]) { // 如果目标节点不是父节点且边未被分割
					push(v, u, edge + weight2[e], Math.min(minv, arr[v]), op, -1); // 将子节点信息压入栈
				}
			}
		}
	}

	// 根据minv的值从小到大排序
	// java自带的排序慢，手撸双指针快排，C++实现时可以用自带的排序
	// 笔试中排序算法的优化是重要考点，面试中可能被问到排序算法的比较
	public static void sort(int[] edge, int[] minv, int l, int r) {
		if (l >= r) return; // 如果区间长度小于等于1，直接返回
		int i = l, j = r, pivot = minv[(l + r) >> 1], tmp; // 设置分区点
		while (i <= j) { // 双指针分区
			while (minv[i] < pivot) i++; // 左指针找到大于等于pivot的元素
			while (minv[j] > pivot) j--; // 右指针找到小于等于pivot的元素
			if (i <= j) { // 如果指针未交错
				// 交换元素
				tmp = edge[i]; edge[i] = edge[j]; edge[j] = tmp;
				tmp = minv[i]; minv[i] = minv[j]; minv[j] = tmp;
				i++; j--; // 移动指针
			}
		}
		sort(edge, minv, l, j); // 递归排序左半部分
		sort(edge, minv, i, r); // 递归排序右半部分
	}

	// 计算通过当前分割边的路径信息
	// 面试中需要说明：如何统计经过分割边的路径，这是边分治的核心步骤
	public static void calc(int edge) {
		cntl = cntr = 0; // 重置左右子树路径信息计数器
		int v1 = to2[edge]; // 获取边重心的一个端点
		int v2 = to2[edge ^ 1]; // 获取边重心的另一个端点
		// dfs1(v1, 0, 0, arr[v1], 0); // 递归版本（可能导致栈溢出）
		// dfs1(v2, 0, weight2[edge], arr[v2], 1); // 递归版本（可能导致栈溢出）
		dfs2(v1, 0, 0, arr[v1], 0); // 迭代版本收集左子树信息
		dfs2(v2, 0, weight2[edge], arr[v2], 1); // 迭代版本收集右子树信息
		sort(ledge, lminv, 1, cntl); // 对左子树路径按最小值排序
		sort(redge, rminv, 1, cntr); // 对右子树路径按最小值排序
		
		// 计算左子树到右子树的路径指标最大值
		long maxEdge = 0; // 记录当前最大路径长度
		for (int i = cntl, j = cntr; i >= 1; i--) { // 从后往前遍历左子树路径
			while (j >= 1 && rminv[j] >= lminv[i]) { // 找到右子树中最小值>=当前左子树最小值的路径
				maxEdge = Math.max(maxEdge, redge[j]); // 更新最大路径长度
				j--; // 移动右子树指针
			}
			if (j < cntr) { // 如果找到了符合条件的右子树路径
				// 计算指标：最小值 * (左路径长度 + 右路径长度 + 1)
				ans = Math.max(ans, 1L * lminv[i] * (maxEdge + ledge[i] + 1));
			}
		}
		
		// 计算右子树到左子树的路径指标最大值
		maxEdge = 0; // 重置最大路径长度
		for (int i = cntr, j = cntl; i >= 1; i--) { // 从后往前遍历右子树路径
			while (j >= 1 && lminv[j] >= rminv[i]) { // 找到左子树中最小值>=当前右子树最小值的路径
				maxEdge = Math.max(maxEdge, ledge[j]); // 更新最大路径长度
				j--; // 移动左子树指针
			}
			if (j < cntl) { // 如果找到了符合条件的左子树路径
				// 计算指标：最小值 * (右路径长度 + 左路径长度 + 1)
				ans = Math.max(ans, 1L * rminv[i] * (maxEdge + redge[i] + 1));
			}
		}
	}

	// 边分治核心处理函数：输入当前节点、总子树大小，递归分割树并处理路径问题
	// 笔试中边分治的核心逻辑，需结合具体题目补充路径统计；ML中可用于拆分树结构提取局部边特征
	public static void solve(int u) {
		int edge = getCentroidEdge(u, 0); // 寻找当前子树的边重心
		if (edge > 0) { // 如果找到了边重心
			vis[edge >> 1] = true; // 标记该边已被分割（避免重复处理）
			calc(edge); // 计算通过该分割边的路径信息
			solve(to2[edge]); // 递归处理第一个子树
			solve(to2[edge ^ 1]); // 递归处理第二个子树
		}
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		n = in.nextInt(); // 读取节点数
		for (int i = 1; i <= n; i++) {
			arr[i] = in.nextInt(); // 读取每个节点的权值
		}
		for (int i = 1, u, v; i < n; i++) {
			u = in.nextInt(); // 读取边的起点
			v = in.nextInt(); // 读取边的终点
			addEdge1(u, v, 1); // 添加原始树的边（边权为1）
			addEdge1(v, u, 1); // 添加反向边
		}
		cntn = n; // 初始化节点数（多叉树转二叉树前）
		cnt2 = 1; // 初始化重构后树的边计数
		// rebuild1(1, 0); // 递归版本多叉树转二叉树（可能导致栈溢出）
		rebuild2(1, 0); // 迭代版本多叉树转二叉树，避免边分治复杂度退化，笔试中需说明该步骤的必要性
		solve(1); // 执行边分治（处理静态路径问题）
		out.println(ans); // 输出结果
		out.flush();
		out.close();
	}

	// 读写工具类
	static class FastReader {
		private final byte[] buffer = new byte[1 << 16];
		private int ptr = 0, len = 0;
		private final InputStream in;

		FastReader(InputStream in) {
			this.in = in;
		}

		private int readByte() throws IOException {
			if (ptr >= len) {
				len = in.read(buffer);
				ptr = 0;
				if (len <= 0)
					return -1;
			}
			return buffer[ptr++];
		}

		int nextInt() throws IOException {
			int c;
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			boolean neg = false;
			if (c == '-') {
				neg = true;
				c = readByte();
			}
			int val = 0;
			while (c > ' ' && c != -1) {
				val = val * 10 + (c - '0');
				c = readByte();
			}
			return neg ? -val : val;
		}
	}

}
```

## 时间/空间复杂度分析
- 时间复杂度：O(n log^2 n)
  - 边分治构建过程：O(n log n)，每次分割将问题规模减半，总共log n层，每层处理O(n)个节点
  - 每次calc操作：O(m log m)，其中m是子树大小，用于排序和双指针统计
  - 总体复杂度：O(n log^2 n)
- 空间复杂度：O(n)，存储树结构和递归栈空间

## 同类题目拓展
- 相似题目：Code01_LongestPathTree1 - 最长路径树问题，同样使用边分治思想
- 变种方向：
  1. 修改指标定义（如路径最大值、路径和等）
  2. 增加边权，计算路径上边权的函数值
  3. 路径上第k大/小值查询
  4. 带修改的动态边分治问题

## ML/DL关联思考
该题的解题思路可以迁移到动态树结构数据的机器学习任务中：
1. 多叉树转二叉树的预处理步骤，可以帮助深度学习模型（如CNN、RNN）处理树结构数据，将不规则的树结构标准化为二叉树结构
2. 边分治的分割逻辑，可以辅助GNN模型提取树的局部边特征，提升模型对树结构关系的理解
3. 在处理知识图谱、语法树等树形结构数据时，边分治能提供高效的路径统计能力
4. 指标计算的思想可以用于设计树结构数据的特征提取函数，为机器学习模型提供有效的输入