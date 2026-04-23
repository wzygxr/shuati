# 【力扣】Code03-PersistentEdgeDecompositionTree1-边分治与边分树-困难

## 题目原始链接
- https://www.luogu.com.cn/problem/CF757G
- https://codeforces.com/problemset/problem/757/G

## 题目完整描述
树上有n个节点，给定n-1条边，每条边有边权。给定长度为n的数组arr，代表点编号组成的一个排列。接下来有q条操作，每条操作是如下两种类型中的一种：
- 操作 1 x y z : 打印arr[x..y]中每个节点到节点z的简单路径距离之和
- 操作 2 x     : 交换arr[x]和arr[x+1]的值，输入保证 1 <= x < n

数据范围：1 <= n、q <= 2 * 10^5

本题要求强制在线，得到操作参数的规则，打开测试链接查看。

## 笔试/面试考察点分析
- 考察边的重心求解：通过寻找边的重心来分割树结构，降低复杂度
- 边分治分割逻辑：将树通过边分割成更小的子树，递归处理
- 边分树构建：构建可持久化数据结构来维护路径信息
- 复杂度分析：O(n log n)时间复杂度的推导与实现
- 可持久化数据结构：使用可持久化线段树来处理动态修改操作
- 与ML/DL的关联：树结构数据在机器学习中的表示与处理

## 解题思路
1. 首先将多叉树转化为二叉树，避免边分治中重儿子导致的复杂度退化
2. 通过边分治找到边的重心，递归分割树结构
3. 使用可持久化线段树维护从每个节点到其子树中节点的路径距离信息
4. 对于操作1，利用可持久化线段树快速查询区间内节点到目标节点的距离和
5. 对于操作2，更新可持久化线段树以反映数组元素交换

## 完整代码实现

```java
package class187;

// 可持久化边分树，java版
// 树上有n个节点，给定n-1条边，每条边有边权
// 给定长度为n的数组arr，代表点编号组成的一个排列
// 接下来有q条操作，每条操作是如下两种类型中的一种
// 操作 1 x y z : 打印arr[x..y]中每个节点到节点z的简单路径距离之和
// 操作 2 x     : 交换arr[x]和arr[x+1]的值，输入保证 1 <= x < n
// 1 <= n、q <= 2 * 10^5
// 本题要求强制在线，得到操作参数的规则，打开测试链接查看
// 测试链接 : https://www.luogu.com.cn/problem/CF757G
// 测试链接 : https://codeforces.com/problemset/problem/757/G
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code03_PersistentEdgeDecompositionTree1 {

	public static int MAXN = 400001; // 定义最大节点数，边分治处理多叉树转二叉树后节点数可能翻倍
	public static int MAXT = MAXN * 30; // 可持久化线段树最大节点数，每个节点最多需要log n个节点
	public static int n, q, cntn; // n为节点数，q为操作数，cntn为重构后的节点计数

	public static int[] head1 = new int[MAXN]; // 原始树的邻接表头指针
	public static int[] next1 = new int[MAXN << 1]; // 原始树的邻接表next指针
	public static int[] to1 = new int[MAXN << 1]; // 原始树的邻接表目标节点
	public static int[] weight1 = new int[MAXN << 1]; // 原始树的边权
	public static int cnt1; // 原始树边的计数

	public static int[] head2 = new int[MAXN]; // 重构后树的邻接表头指针
	public static int[] next2 = new int[MAXN << 1]; // 重构后树的邻接表next指针
	public static int[] to2 = new int[MAXN << 1]; // 重构后树的邻接表目标节点
	public static int[] weight2 = new int[MAXN << 1]; // 重构后树的边权
	public static int cnt2; // 重构后树边的计数

	public static boolean[] vis = new boolean[MAXN]; // 标记边是否被分割（边分治核心标记数组）
	public static int[] siz = new int[MAXN]; // 存储子树大小，用于求解边的重心

	public static int[] up = new int[MAXN]; // 节点在可持久化线段树中的位置
	public static int[] root = new int[MAXN]; // 每个节点对应的线段树根节点
	public static int[] ls = new int[MAXT]; // 可持久化线段树左子节点
	public static int[] rs = new int[MAXT]; // 可持久化线段树右子节点
	public static int[] lcnt = new int[MAXT]; // 左子树节点计数
	public static int[] rcnt = new int[MAXT]; // 右子树节点计数
	public static long[] lsum = new long[MAXT]; // 左子树距离和
	public static long[] rsum = new long[MAXT]; // 右子树距离和
	public static int cntt; // 可持久化线段树节点计数

	public static int[] arr = new int[MAXN]; // 输入的排列数组
	public static int[] pre = new int[MAXN]; // 前缀和数组，用于区间查询

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
	public static void rebuild(int u, int fa) {
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
					addEdge2(last, add, 0); // 连接前一个子节点和新节点（边权设为0）
					addEdge2(add, last, 0); // 添加反向边
					addEdge2(add, v, w); // 连接新节点和当前子节点
					addEdge2(v, add, w); // 添加反向边
					last = add; // 更新last为新节点
				}
				rebuild(v, u); // 递归处理子节点的多叉树转二叉树
			}
		}
	}

	// 计算子树大小：输入当前节点、父节点，输出子树大小，用于寻找边的重心
	// 笔试中该函数是边分治的基础，需快速手写；ML中可用于提取子树规模特征
	public static void getSize(int u, int fa) {
		siz[u] = 1; // 初始化子树大小为1（包含当前节点）
		for (int e = head2[u]; e > 0; e = next2[e]) { // 遍历当前节点的所有邻接边
			int v = to2[e];
			if (v != fa && !vis[e >> 1]) { // 排除父节点和已分割的边
				getSize(v, u); // 递归计算子树大小
				siz[u] += siz[v]; // 累加子树大小
			}
		}
	}

	// 寻找边的重心：输入当前节点、父节点、总子树大小，找到使分割后最大子树最小的边
	// 面试高频考点：边的重心定义、寻找逻辑，与点的重心的区别；ML中边重心可作为树的关键分割特征
	public static int getCentroidEdge(int u, int fa) {
		getSize(u, fa); // 计算当前子树大小
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

	// DFS收集路径信息：从当前节点开始，收集到各子节点的距离信息
	// 面试中需要说明：此函数用于构建可持久化线段树，存储路径距离信息
	public static void dfs(int u, int fa, long dist, int op) {
		if (u <= n) { // 如果是原树中的节点
			if (up[u] == 0) { // 如果该节点还未在可持久化线段树中
				up[u] = ++cntt; // 分配新的线段树节点编号
				root[u] = cntt; // 记录该节点的线段树根
			}
			int cur = up[u]; // 获取当前线段树节点
			int nxt = ++cntt; // 创建新的线段树节点
			if (op == 0) { // 左子树操作
				ls[cur] = nxt; // 设置左子节点
				lsum[cur] = dist; // 存储距离和
				lcnt[cur] = 1; // 设置节点计数
			} else { // 右子树操作
				rs[cur] = nxt; // 设置右子节点
				rsum[cur] = dist; // 存储距离和
				rcnt[cur] = 1; // 设置节点计数
			}
			up[u] = nxt; // 更新该节点在可持久化线段树中的位置
		}
		for (int e = head2[u]; e > 0; e = next2[e]) { // 遍历当前节点的所有邻接边
			int v = to2[e];
			if (v != fa && !vis[e >> 1]) { // 排除父节点和已分割的边
				dfs(v, u, dist + weight2[e], op); // 递归处理子节点
			}
		}
	}

	// 边分治核心处理函数：递归分割树并构建可持久化线段树
	// 笔试中边分治的核心逻辑，需结合具体题目补充路径统计；ML中可用于拆分树结构提取局部边特征
	public static void solve(int u) {
		int edge = getCentroidEdge(u, 0); // 寻找当前子树的边重心
		if (edge > 0) { // 如果找到了边重心
			vis[edge >> 1] = true; // 标记该边已被分割（避免重复处理）
			int v1 = to2[edge]; // 获取边重心的一个端点
			int v2 = to2[edge ^ 1]; // 获取边重心的另一个端点
			dfs(v1, 0, 0, 0); // 处理第一个子树，op=0表示左子树
			dfs(v2, 0, weight2[edge], 1); // 处理第二个子树，op=1表示右子树
			solve(v1); // 递归处理第一个子树
			solve(v2); // 递归处理第二个子树
		}
	}

	// 可持久化线段树合并操作：合并两个线段树节点
	// 面试中需要说明：该操作用于合并交换操作后的线段树信息
	public static int add(int p, int i) {
		if (p == 0 || i == 0) { // 如果其中一个节点为空
			return p + i; // 直接返回另一个节点
		}
		int rt = ++cntt; // 创建新的线段树节点
		ls[rt] = ls[p]; // 复制左子节点
		rs[rt] = rs[p]; // 复制右子节点
		lcnt[rt] = lcnt[p] + lcnt[i]; // 合并左子树节点计数
		rcnt[rt] = rcnt[p] + rcnt[i]; // 合并右子树节点计数
		lsum[rt] = lsum[p] + lsum[i]; // 合并左子树距离和
		rsum[rt] = rsum[p] + rsum[i]; // 合并右子树距离和
		if (ls[i] > 0) { // 如果i节点有左子树
			ls[rt] = add(ls[rt], ls[i]); // 递归合并左子树
		} else { // 如果i节点有右子树
			rs[rt] = add(rs[rt], rs[i]); // 递归合并右子树
		}
		return rt; // 返回合并后的节点
	}

	// 查询操作：查询区间内节点到目标节点的距离和
	// 笔试高频动态查询场景，核心是可持久化线段树的区间查询；ML中可用于动态提取树路径的边特征
	public static long query(int p1, int p2, int i) {
		if (ls[i] == 0 && rs[i] == 0) { // 如果查询节点是叶子节点
			return 0; // 返回0
		} else if (ls[i] > 0) { // 如果查询节点有左子树
			// 递归查询左子树，并加上右子树的贡献
			return query(ls[p1], ls[p2], ls[i]) + rsum[p2] - rsum[p1] + lsum[i] * (rcnt[p2] - rcnt[p1]);
		} else { // 如果查询节点有右子树
			// 递归查询右子树，并加上左子树的贡献
			return query(rs[p1], rs[p2], rs[i]) + lsum[p2] - lsum[p1] + rsum[i] * (lcnt[p2] - lcnt[p1]);
		}
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		n = in.nextInt(); // 读取节点数
		q = in.nextInt(); // 读取操作数
		for (int i = 1; i <= n; i++) {
			arr[i] = in.nextInt(); // 读取排列数组
		}
		for (int i = 1, u, v, w; i < n; i++) {
			u = in.nextInt(); // 读取边的起点
			v = in.nextInt(); // 读取边的终点
			w = in.nextInt(); // 读取边权
			addEdge1(u, v, w); // 添加原始树的边
			addEdge1(v, u, w); // 添加反向边
		}
		cntn = n; // 初始化节点数（多叉树转二叉树前）
		cnt2 = 1; // 初始化重构后树的边计数
		rebuild(1, 0); // 多叉树转二叉树，避免边分治复杂度退化，笔试中需说明该步骤的必要性
		solve(1); // 执行边分治（构建可持久化线段树）
		for (int i = 1; i <= n; i++) {
			pre[i] = add(pre[i - 1], root[arr[i]]); // 构建前缀和数组，用于区间查询
		}
		long mask = (1L << 30) - 1; // 用于强制在线的掩码
		long lastAns = 0; // 上一次查询的结果
		long a, b, c; // 临时变量，用于解码操作参数
		int op, x, y, z, tmp; // 操作类型、查询参数、临时变量
		for (int i = 1; i <= q; i++) {
			op = in.nextInt(); // 读取操作类型
			if (op == 1) { // 查询操作
				a = in.nextInt();
				b = in.nextInt();
				c = in.nextInt();
				a ^= lastAns; // 解码参数（强制在线）
				b ^= lastAns; // 解码参数（强制在线）
				c ^= lastAns; // 解码参数（强制在线）
				x = (int) a; // 转换为整数
				y = (int) b; // 转换为整数
				z = (int) c; // 转换为整数
				lastAns = query(pre[x - 1], pre[y], root[z]); // 执行区间查询
				out.println(lastAns); // 输出结果
				lastAns &= mask; // 应用掩码
			} else { // 交换操作
				a = in.nextInt();
				a ^= lastAns; // 解码参数（强制在线）
				x = (int) a; // 转换为整数
				tmp = arr[x]; // 交换数组元素
				arr[x] = arr[x + 1];
				arr[x + 1] = tmp;
				pre[x] = add(pre[x - 1], root[arr[x]]); // 更新前缀和数组
			}
		}
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
- 时间复杂度：O((n+q) log n)，其中n是节点数，q是操作数
  - 边分治构建过程：O(n log n)，每次分割将问题规模减半，总共log n层，每层处理O(n)个节点
  - 查询操作：O(log n)，通过可持久化线段树进行区间查询
  - 交换操作：O(log n)，更新可持久化线段树
- 空间复杂度：O(n log n)，可持久化线段树需要O(n log n)的空间

## 同类题目拓展
- 相似题目：Code01_LongestPathTree1 - 最长路径树问题，同样使用边分治思想
- 变种方向：
  1. 修改距离定义（如路径最大边权、路径边数等）
  2. 增加点权或边权修改操作
  3. 路径上第k大/小值查询
  4. 路径上满足特定条件的节点计数

## ML/DL关联思考
该题的解题思路可以迁移到动态树结构数据的机器学习任务中：
1. 多叉树转二叉树的预处理步骤，可以帮助深度学习模型（如CNN、RNN）处理树结构数据，将不规则的树结构标准化为二叉树结构
2. 边分治的分割逻辑，可以辅助GNN模型提取树的局部边特征，提升模型对树结构关系的理解
3. 可持久化数据结构的思想，可以用于维护动态图神经网络中的节点状态，支持高效的动态更新和查询
4. 在处理知识图谱、语法树等树形结构数据时，边分治与可持久化结构的结合能提供高效的查询能力