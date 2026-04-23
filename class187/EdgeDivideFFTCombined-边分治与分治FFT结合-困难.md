# 【力扣】EdgeDivideFFTCombined-边分治与分治FFT结合-困难

## 题目原始链接
- 综合性难题，参考：https://www.luogu.com.cn/problem/P4889
- 类似题目：https://codeforces.com/problemset/problem/590/E

## 题目完整描述
给定一棵有n个节点的树，每条边有权值。对于每个节点，需要统计经过该节点的路径数量，其中路径长度为k的路径数量需要快速计算。

更具体地，对于每个k (1 <= k <= n-1)，计算树上有多少条简单路径的长度恰好为k。

输入格式：
- 第一行：n (1 <= n <= 10^5)
- 接下来n-1行：每行两个整数u, v，表示节点u和v之间有一条边

输出格式：
- 输出n-1行，第k行表示长度为k的路径数量

## 笔试/面试考察点分析
- 考察边分治与FFT的结合：两种高级算法的综合应用
- 分治思想的深度应用：在树结构上进行分治，并在每层使用FFT加速卷积计算
- 复杂度分析：O(n log^2 n)时间复杂度的推导与实现
- FFT在树问题中的应用：如何将FFT用于路径统计问题
- 与ML/DL的关联：在图神经网络中使用FFT进行特征变换

## 解题思路
1. 使用边分治将树分解为多个子结构
2. 在每个重心处，统计经过该重心的所有路径
3. 使用FFT加速子树间路径长度的卷积计算
4. 对于每个子树，计算其内部路径和连接两个子树的路径
5. 合并所有结果得到最终答案

## 完整代码实现

```java
package class187;

// 边分治与分治FFT结合问题：统计树上所有长度的路径数量
// 树上有n个节点，需要统计长度为k的路径数量（k=1,2,...,n-1）
// 1 <= n <= 10^5
// 使用边分治 + FFT实现快速路径统计

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class EdgeDivideFFTCombined {

	public static int MAXN = 262144; // 定义最大节点数，为FFT需要的2的幂次
	public static int MAXM = 100005; // 实际最大节点数
	public static int n; // n为节点数

	public static int[] head = new int[MAXM]; // 树的邻接表头指针
	public static int[] next = new int[MAXM << 1]; // 邻接表next指针
	public static int[] to = new int[MAXM << 1]; // 邻接表目标节点
	public static int cnt; // 边的计数

	public static boolean[] vis = new boolean[MAXM]; // 标记节点是否被分割
	public static int[] siz = new int[MAXM]; // 存储子树大小，用于求解重心
	public static long[] ans = new long[MAXM]; // 存储长度为k的路径数量

	// FFT相关变量
	public static Complex[] A = new Complex[MAXN];
	public static Complex[] B = new Complex[MAXN];
	public static int[] rev = new int[MAXN];

	// 复数类，用于FFT计算
	static class Complex {
		double x, y;

		Complex(double x, double y) {
			this.x = x;
			this.y = y;
		}

		// 复数加法
		public Complex add(Complex other) {
			return new Complex(this.x + other.x, this.y + other.y);
		}

		// 复数减法
		public Complex sub(Complex other) {
			return new Complex(this.x - other.x, this.y - other.y);
		}

		// 复数乘法
		public Complex mul(Complex other) {
			return new Complex(
				this.x * other.x - this.y * other.y,
				this.x * other.y + this.y * other.x
			);
		}
	}

	// 初始化FFT相关数组
	public static void initFFT(int len) {
		int bit = 0; // 位数
		while ((1 << bit) < len) bit++; // 计算需要的位数

		// 计算反转数组
		for (int i = 0; i < (1 << bit); i++) {
			rev[i] = (rev[i >> 1] >> 1) | ((i & 1) << (bit - 1));
		}
	}

	// FFT主函数
	// 笔试中FFT是重要的多项式运算工具，需熟练掌握；ML中FFT可用于信号处理和卷积运算
	public static void fft(Complex[] a, int n, int inv) {
		// 按位反转重排数组
		for (int i = 0; i < n; i++) {
			if (i < rev[i]) {
				Complex temp = a[i];
				a[i] = a[rev[i]];
				a[rev[i]] = temp;
			}
		}

		// 迭代进行FFT
		for (int mid = 1; mid < n; mid <<= 1) { // 当前处理的区间长度的一半
			// 计算单位根
			Complex w1 = new Complex(Math.cos(Math.PI / mid), inv * Math.sin(Math.PI / mid));
			for (int i = 0; i < n; i += (mid << 1)) { // 每次处理一个大区间
				Complex wk = new Complex(1, 0); // 当前权重
				for (int j = 0; j < mid; j++) { // 处理当前区间内的每一对
					Complex x = a[i + j]; // 左半部分
					Complex y = wk.mul(a[i + j + mid]); // 右半部分乘以权重
					a[i + j] = x.add(y); // 更新左半部分
					a[i + j + mid] = x.sub(y); // 更新右半部分
					wk = wk.mul(w1); // 更新权重
				}
			}
		}

		if (inv == -1) { // 如果是逆变换，需要除以n
			for (int i = 0; i < n; i++) {
				a[i] = new Complex(a[i].x / n, a[i].y / n);
			}
		}
	}

	// 多项式乘法，使用FFT加速
	// 面试中需要说明：FFT如何将O(n^2)的卷积计算优化到O(n log n)
	public static long[] multiply(long[] a, int lena, long[] b, int lenb) {
		if (lena == 0 || lenb == 0) { // 如果有一个数组为空
			return new long[1]; // 返回空结果
		}

		int len = 1; // 计算FFT需要的长度
		while (len < lena + lenb - 1) len <<= 1; // 扩展到2的幂次

		initFFT(len); // 初始化FFT

		// 将long数组转换为Complex数组
		for (int i = 0; i < len; i++) {
			if (i < lena) {
				A[i] = new Complex(a[i], 0);
			} else {
				A[i] = new Complex(0, 0);
			}
			if (i < lenb) {
				B[i] = new Complex(b[i], 0);
			} else {
				B[i] = new Complex(0, 0);
			}
		}

		// 执行FFT
		fft(A, len, 1); // 正向FFT
		fft(B, len, 1); // 正向FFT

		// 点值相乘
		for (int i = 0; i < len; i++) {
			A[i] = A[i].mul(B[i]); // 点值相乘
		}

		// 逆FFT
		fft(A, len, -1); // 逆向FFT

		// 转换回long数组
		long[] result = new long[lena + lenb - 1];
		for (int i = 0; i < lena + lenb - 1; i++) {
			result[i] = (long) (A[i].x + 0.5); // 四舍五入
		}

		return result; // 返回结果
	}

	// 添加边
	// 笔试中邻接表建图是基础操作，需熟练掌握
	public static void addEdge(int u, int v) {
		next[++cnt] = head[u]; // 头插法添加边
		to[cnt] = v; // 记录目标节点
		head[u] = cnt; // 更新头指针
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

	// DFS获取子树中每个深度的节点数量
	// 面试中需要说明：如何统计子树中各深度的节点分布
	public static void getDep(int u, int fa, int depth, List<Integer> depths) {
		if (depth >= depths.size()) { // 如果深度超出了当前列表大小
			while (depth >= depths.size()) {
				depths.add(0); // 扩展列表
			}
		}
		depths.set(depth, depths.get(depth) + 1); // 增加该深度的节点数

		for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != fa && !vis[v]) { // 排除父节点和已分割的节点
				getDep(v, u, depth + 1, depths); // 递归处理子节点
			}
		}
	}

	// 边分治处理函数
	// 笔试中边分治的核心逻辑，需结合FFT进行路径统计
	public static void solve(int u) {
		int centroid = getCentroid(u, 0, siz[u]); // 找到当前连通块的重心
		vis[centroid] = true; // 标记重心已访问

		// 统计经过重心的所有路径
		List<Integer> allDep = new ArrayList<>(); // 所有子树的深度分布
		allDep.add(1); // 重心本身，深度为0的节点数为1

		for (int e = head[centroid]; e > 0; e = next[e]) { // 遍历重心的所有邻接边
			int v = to[e];
			if (!vis[v]) { // 如果子节点未被访问
				List<Integer> subDep = new ArrayList<>(); // 当前子树的深度分布
				getDep(v, centroid, 1, subDep); // 获取子树深度分布

				// 使用FFT计算当前子树与之前所有子树之间的路径
				long[] arr1 = new long[subDep.size()];
				for (int i = 0; i < subDep.size(); i++) {
					arr1[i] = subDep.get(i);
				}

				long[] arr2 = new long[allDep.size()];
				for (int i = 0; i < allDep.size(); i++) {
					arr2[i] = allDep.get(i);
				}

				long[] convolution = multiply(arr1, arr1.length, arr2, arr2.length); // FFT卷积

				// 更新答案数组
				for (int i = 0; i < convolution.length; i++) {
					ans[i] += convolution[i];
				}

				// 将当前子树的深度分布合并到总分布中
				while (allDep.size() < subDep.size() + allDep.size()) {
					allDep.add(0);
				}
				for (int i = 0; i < subDep.size(); i++) {
					allDep.set(i, allDep.get(i) + subDep.get(i));
				}
			}
		}

		// 递归处理子树
		for (int e = head[centroid]; e > 0; e = next[e]) {
			int v = to[e];
			if (!vis[v]) { // 如果子节点未被访问
				solve(v); // 递归处理子树
			}
		}
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		n = in.nextInt(); // 读取节点数
		
		// 读取边
		for (int i = 1, u, v; i < n; i++) {
			u = in.nextInt(); // 读取边的起点
			v = in.nextInt(); // 读取边的终点
			addEdge(u, v); // 添加边
			addEdge(v, u); // 添加反向边
		}
		
		// 初始化
		for (int i = 0; i < MAXN; i++) {
			A[i] = new Complex(0, 0);
			B[i] = new Complex(0, 0);
		}
		
		// 计算整棵树的大小
		getSize(1, 0);
		
		// 执行边分治
		solve(1); // 开始边分治处理
		
		// 输出结果，注意路径被计算了两次（正向和反向），所以要除以2
		for (int i = 1; i < n; i++) {
			out.println(ans[i] / 2); // 输出长度为i的路径数量
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
- 时间复杂度：
  - 边分治部分：O(n log n)，每次分割将问题规模减半，总共log n层
  - FFT卷积部分：每层需要对子树进行卷积计算，每对子树的卷积为O(m log m)，其中m是子树大小
  - 总体复杂度：O(n log^2 n)，其中包含分治的log n层和FFT的log n复杂度
- 空间复杂度：O(n)，主要是存储树结构和FFT计算所需的空间

## 同类题目拓展
- 相似题目：树分治结合多项式运算的问题
- 变种方向：
  1. 带边权的路径统计
  2. 特定路径属性的统计（如路径上点权的和、积等）
  3. 动态树上的路径统计
  4. 限制路径端点类型的统计

## ML/DL关联思考
该题的解题思路可以迁移到机器学习中的图结构数据处理：
1. **图卷积网络中的快速计算**：在GNN中，节点的更新需要聚合邻居信息，FFT可以加速这种聚合计算
2. **图信号处理**：将节点特征看作图上的信号，使用FFT进行频域分析
3. **核方法**：在图核方法中，使用FFT加速子图匹配等计算
4. **图神经网络中的快速注意力**：使用FFT加速图注意力机制的计算
5. **大规模图的近似计算**：在处理大规模图时，使用FFT进行快速近似计算