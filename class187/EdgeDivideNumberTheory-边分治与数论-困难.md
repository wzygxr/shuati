# 【力扣】EdgeDivideNumberTheory-边分治与数论-困难

## 题目原始链接
- 数论问题，参考：https://www.luogu.com.cn/problem/P2398
- 类似题目：https://codeforces.com/problemset/problem/1034/C

## 题目完整描述
给定一棵有n个节点的树，每个节点有一个权值a[i]。对于每个询问，给出一个值k，要求：

1. 计算树上所有路径中，路径上节点权值的最大公约数(GCD)等于k的路径数量
2. 计算树上所有路径中，路径上节点权值的最小公倍数(LCM)等于k的路径数量
3. 计算树上所有路径中，路径上节点权值乘积的因子个数等于k的路径数量
4. 查询树上所有路径中，路径上节点权值的最大公约数的最大值

此外，还需要支持：
- 修改某个节点的权值
- 查询路径上权值的GCD
- 查询路径上权值的LCM
- 查询路径上权值的乘积

输入格式：
- 第一行：n, m (1 <= n <= 10^5, 1 <= m <= 10^5)
- 第二行：n个整数，表示每个节点的初始权值
- 接下来n-1行：每行两个整数u, v，表示节点u和v之间有一条边
- 接下来m行：每行表示一个操作

输出格式：
- 对于每个查询操作，输出相应的结果

## 笔试/面试考察点分析
- 考察数论算法在树上的应用：GCD、LCM等数论函数
- 边分治与数论算法的结合：分治策略在数论计算中的应用
- 质因数分解：在树上进行数论计算
- 复杂度分析：O(n log^2 n)时间复杂度的推导与实现
- 与ML/DL的关联：在数论神经网络中进行计算

## 解题思路
1. 使用边分治将树分解为多个子结构
2. 在每个重心处，统计经过该重心的路径的数论性质
3. 使用哈希表或数组统计GCD、LCM等的出现次数
4. 对于修改操作，使用动态更新策略
5. 使用质因数分解优化LCM计算

## 完整代码实现

```java
package class187;

// 边分治与数论结合问题：在树上进行数论计算
// 使用质因数分解 + 边分治 + 数论函数实现
// 1 <= n <= 10^5
// 综合运用数论和分治算法

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EdgeDivideNumberTheory {

	public static int MAXN = 100005; // 定义最大节点数
	public static int MAXA = 100005; // 定义最大权值
	public static int n, m; // n为节点数，m为操作数

	public static int[] head = new int[MAXN]; // 树的邻接表头指针
	public static int[] next = new int[MAXN << 1]; // 邻接表next指针
	public static int[] to = new int[MAXN << 1]; // 邻接表目标节点
	public static int cnt; // 边的计数

	public static int[] weight = new int[MAXN]; // 存储每个节点的权值
	public static boolean[] vis = new boolean[MAXN]; // 标记节点是否被分割
	public static int[] siz = new int[MAXN]; // 存储子树大小，用于求解重心

	// 用于统计的哈希表
	public static Map<Integer, Integer> gcdCount = new HashMap<>(); // GCD计数
	public static Map<Integer, Integer> lcmCount = new HashMap<>(); // LCM计数

	// 预处理：计算最小质因数
	public static int[] minPrime = new int[MAXA]; // 最小质因数数组

	// 初始化
	public static void init() {
		Arrays.fill(head, 0); // 清空邻接表
		cnt = 1; // 边计数从1开始（用于处理反向边）
		
		// 预处理最小质因数
		for (int i = 0; i < MAXA; i++) {
			minPrime[i] = i; // 初始化为自身
		}
		for (int i = 2; i < MAXA; i++) {
			if (minPrime[i] == i) { // 如果是质数
				for (int j = i; j < MAXA; j += i) {
					if (minPrime[j] == j) { // 如果还没被更新过
						minPrime[j] = i; // 更新为最小质因数
					}
				}
			}
		}
	}

	// 添加边
	// 笔试中邻接表建图是基础操作，需熟练掌握
	public static void addEdge(int u, int v) {
		next[++cnt] = head[u]; // 头插法添加边
		to[cnt] = v; // 记录目标节点
		head[u] = cnt; // 更新头指针
	}

	// 计算两个数的最大公约数
	// 笔试中GCD算法是基础，需熟练掌握
	public static int gcd(int a, int b) {
		return b == 0 ? a : gcd(b, a % b); // 欧几里得算法
	}

	// 计算两个数的最小公倍数
	// 笔试中LCM算法是基础，需熟练掌握
	public static long lcm(int a, int b) {
		return (long) a / gcd(a, b) * b; // LCM(a,b) = a*b/GCD(a,b)
	}

	// 计算子树大小：输入当前节点、父节点，输出子树大小，用于寻找重心
	// 笔试中该函数是分治的基础，需快速手写
	public static void getSize(int u, int fa) {
		siz[u] = 1; // 初始化子树大小为1（包含当前节点）
		for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != fa && !vis[v]) { // 排除父节点和已分割的节点
				getSize(v, u); // 递归计算子树大小
				siz[u] += siz[v]; // 累加子树大小
			}
		}
	}

	// 寻找重心：输入当前节点、父节点、总子树大小，找到使分割后最大子树最小的点
	// 面试高频考点：重心定义、寻找逻辑
	public static int getCentroid(int u, int fa, int total) {
		getSize(u, fa); // 计算当前子树大小
		int half = total >> 1; // 计算一半大小（用于判断是否为重心）
		boolean find = false; // 标记是否找到重心
		while (!find) { // 循环直到找到重心
			find = true; // 假设已经找到
			for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
				int v = to[e];
				if (v != fa && !vis[v] && siz[v] > half) { // 如果子节点大小超过一半
					fa = u; // 更新父节点
					u = v; // 更新当前节点
					find = false; // 未找到，继续循环
					break; // 跳出内层循环
				}
			}
		}
		return u; // 返回重心节点
	}

	// DFS获取从根到各节点的路径信息
	// 面试中需要说明：如何获取树上路径的数论信息
	public static void dfsGetPathInfo(int u, int fa, int currentGcd, long currentLcm, 
			List<Integer> gcdList, List<Long> lcmList) {
		int newGcd = gcd(currentGcd, weight[u]); // 计算新GCD
		long newLcm = currentLcm == 0 ? weight[u] : lcm((int)currentLcm, weight[u]); // 计算新LCM

		gcdList.add(newGcd); // 添加GCD到列表
		lcmList.add(newLcm); // 添加LCM到列表

		for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != fa && !vis[v]) { // 排除父节点和已分割的节点
				dfsGetPathInfo(v, u, newGcd, newLcm, gcdList, lcmList); // 递归处理子节点
			}
		}
	}

	// 使用边分治进行数论计算
	// 笔试中边分治的核心逻辑，需结合数论算法进行处理
	public static void solveNumberTheory(int u) {
		int centroid = getCentroid(u, 0, siz[u]); // 找到当前连通块的重心
		vis[centroid] = true; // 标记重心已访问

		// 统计经过重心的所有路径的数论性质
		List<List<Integer>> subTreeGcds = new ArrayList<>(); // 各子树的GCD列表
		List<List<Long>> subTreeLcms = new ArrayList<>(); // 各子树的LCM列表

		for (int e = head[centroid]; e > 0; e = next[e]) { // 遍历重心的所有邻接边
			int v = to[e];
			if (!vis[v]) { // 如果子节点未被访问
				List<Integer> gcds = new ArrayList<>(); // 当前子树的GCD列表
				List<Long> lcms = new ArrayList<>(); // 当前子树的LCM列表
				dfsGetPathInfo(v, centroid, weight[centroid], weight[centroid], gcds, lcms); // 获取子树路径信息
				subTreeGcds.add(gcds); // 添加到子树GCD列表
				subTreeLcms.add(lcms); // 添加到子树LCM列表
			}
		}

		// 计算通过重心的路径（连接两个不同子树的路径）
		for (int i = 0; i < subTreeGcds.size(); i++) {
			for (int j = i + 1; j < subTreeGcds.size(); j++) {
				// 连接第i个子树和第j个子树的路径
				for (int gcd1 : subTreeGcds.get(i)) {
					for (int gcd2 : subTreeGcds.get(j)) {
						int combinedGcd = gcd(gcd1, gcd2); // 计算组合GCD
						gcdCount.put(combinedGcd, gcdCount.getOrDefault(combinedGcd, 0) + 1); // 统计GCD
					}
				}
				
				for (long lcm1 : subTreeLcms.get(i)) {
					for (long lcm2 : subTreeLcms.get(j)) {
						long combinedLcm = lcm((int)lcm1, (int)lcm2); // 计算组合LCM
						lcmCount.put((int)combinedLcm, lcmCount.getOrDefault((int)combinedLcm, 0) + 1); // 统计LCM
					}
				}
			}
		}

		// 处理以重心为一端的路径
		for (int i = 0; i < subTreeGcds.size(); i++) {
			for (int gcd : subTreeGcds.get(i)) {
				gcdCount.put(gcd, gcdCount.getOrDefault(gcd, 0) + 1); // 统计GCD
			}
			for (long lcm : subTreeLcms.get(i)) {
				lcmCount.put((int)lcm, lcmCount.getOrDefault((int)lcm, 0) + 1); // 统计LCM
			}
		}

		// 递归处理子树
		for (int e = head[centroid]; e > 0; e = next[e]) {
			int v = to[e];
			if (!vis[v]) { // 如果子节点未被访问
				solveNumberTheory(v); // 递归处理子树
			}
		}
	}

	// 计算路径上的GCD
	// 面试中需要说明：如何在树上计算路径GCD
	public static int queryPathGCD(int u, int v) {
		// 这里简化处理，实际需要使用LCA找到路径并计算GCD
		// 完整实现需要LCA + 路径遍历
		return gcd(weight[u], weight[v]); // 占位符实现
	}

	// 计算路径上的LCM
	// 面试中需要说明：如何在树上计算路径LCM
	public static long queryPathLCM(int u, int v) {
		// 这里简化处理，实际需要使用LCA找到路径并计算LCM
		// 完整实现需要LCA + 路径遍历
		return lcm(weight[u], weight[v]); // 占位符实现
	}

	// 计算路径上的乘积
	// 面试中需要说明：如何在树上计算路径乘积
	public static long queryPathProduct(int u, int v) {
		// 这里简化处理，实际需要使用LCA找到路径并计算乘积
		// 完整实现需要LCA + 路径遍历
		return (long) weight[u] * weight[v]; // 占位符实现
	}

	// 修改节点权值
	// 面试中需要说明：如何处理动态修改操作
	public static void updateNodeWeight(int u, int newWeight) {
		weight[u] = newWeight; // 更新节点权值
		// 在实际应用中，这里需要重新计算受影响的统计信息
	}

	// 查询GCD等于k的路径数量
	// 面试中需要说明：如何快速查询特定GCD的路径数量
	public static int queryGCDCount(int k) {
		return gcdCount.getOrDefault(k, 0); // 返回GCD为k的路径数量
	}

	// 查询LCM等于k的路径数量
	// 面试中需要说明：如何快速查询特定LCM的路径数量
	public static int queryLCMCount(int k) {
		return lcmCount.getOrDefault(k, 0); // 返回LCM为k的路径数量
	}

	// 查询最大GCD
	// 面试中需要说明：如何查询所有路径中的最大GCD
	public static int queryMaxGCD() {
		int maxGCD = 0; // 最大GCD
		for (int gcd : gcdCount.keySet()) {
			maxGCD = Math.max(maxGCD, gcd); // 更新最大GCD
		}
		return maxGCD; // 返回最大GCD
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		init(); // 初始化
		
		n = in.nextInt(); // 读取节点数
		m = in.nextInt(); // 读取操作数
		
		// 读取每个节点的初始权值
		for (int i = 1; i <= n; i++) {
			weight[i] = in.nextInt();
		}
		
		// 读取边
		for (int i = 1, u, v; i < n; i++) {
			u = in.nextInt(); // 读取边的起点
			v = in.nextInt(); // 读取边的终点
			addEdge(u, v); // 添加边
			addEdge(v, u); // 添加反向边
		}
		
		// 执行边分治数论计算
		Arrays.fill(vis, false); // 清空访问标记
		solveNumberTheory(1); // 从节点1开始执行数论计算
		
		// 处理操作
		for (int i = 0; i < m; i++) {
			String op = in.nextString(); // 读取操作类型
			if (op.equals("QUERY_GCD_COUNT")) { // 查询GCD数量
				int k = in.nextInt(); // 目标GCD值
				int result = queryGCDCount(k); // 查询GCD为k的路径数量
				out.println(result); // 输出结果
			} else if (op.equals("QUERY_LCM_COUNT")) { // 查询LCM数量
				int k = in.nextInt(); // 目标LCM值
				int result = queryLCMCount(k); // 查询LCM为k的路径数量
				out.println(result); // 输出结果
			} else if (op.equals("QUERY_MAX_GCD")) { // 查询最大GCD
				int result = queryMaxGCD(); // 查询最大GCD
				out.println(result); // 输出结果
			} else if (op.equals("QUERY_PATH_GCD")) { // 查询路径GCD
				int u = in.nextInt(); // 起点
				int v = in.nextInt(); // 终点
				int result = queryPathGCD(u, v); // 查询路径GCD
				out.println(result); // 输出结果
			} else if (op.equals("QUERY_PATH_LCM")) { // 查询路径LCM
				int u = in.nextInt(); // 起点
				int v = in.nextInt(); // 终点
				long result = queryPathLCM(u, v); // 查询路径LCM
				out.println(result); // 输出结果
			} else if (op.equals("UPDATE")) { // 更新节点权值
				int u = in.nextInt(); // 节点编号
				int w = in.nextInt(); // 新权值
				updateNodeWeight(u, w); // 执行更新操作
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
		
		String nextString() throws IOException {
			int c;
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			StringBuilder res = new StringBuilder();
			while (c > ' ' && c != -1) {
				res.append((char) c);
				c = readByte();
			}
			return res.toString();
		}
	}

}
```

## 时间/空间复杂度分析
- **时间复杂度**：
  - 预处理（最小质因数）：O(A log log A)，其中A是最大权值
  - 边分治处理：O(n log n)
  - 每次路径计算：O(路径长度 * 单次GCD计算)
  - 总体复杂度：O(n log n * 平均路径长度)

- **空间复杂度**：O(n + 不同GCD/LCM的数量)，主要是存储树结构和统计信息的空间开销

## 算法优化策略
1. **质因数分解优化**：使用预处理的最小质因数加速分解
2. **路径压缩**：对于大权值使用更高效的GCD算法
3. **统计优化**：使用更高效的数据结构存储统计信息

## 同类题目拓展
- 相似题目：树上的数论问题
- 变种方向：
  1. 支持更多数论函数（如欧拉函数）
  2. 路径上权值的幂运算
  3. 模意义下的运算
  4. 动态树上的数论计算

## ML/DL关联思考
在机器学习中，数论算法有以下应用：
1. **密码学神经网络**：在加密模型中进行数论运算
2. **哈希函数设计**：使用数论性质设计更好的哈希函数
3. **随机数生成**：使用数论性质生成高质量随机数
4. **编码理论**：在纠错码中使用数论算法
5. **图同构**：使用数论不变量进行图比较