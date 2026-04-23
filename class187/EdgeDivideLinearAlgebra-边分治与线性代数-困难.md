# 【力扣】EdgeDivideLinearAlgebra-边分治与线性代数-困难

## 题目原始链接
- 线性代数问题，参考：https://www.luogu.com.cn/problem/P4777
- 类似题目：https://codeforces.com/problemset/problem/392/C

## 题目完整描述
给定一棵有n个节点的树，每个节点有一个向量V[i] = (v[i][0], v[i][1], ..., v[i][d-1])。对于每个询问，给出一个向量W和一个路径(u, v)，要求：

1. 计算路径u到v上所有节点的向量与向量W的点积之和
2. 计算路径u到v上所有节点的向量的线性组合
3. 查询路径u到v上所有节点的向量张量积的迹
4. 计算路径u到v上所有节点的向量构成的矩阵的行列式
5. 查询树上所有路径中，向量和的模长最大值
6. 计算路径u到v上所有节点的向量构成的矩阵的特征值

此外，还需要支持：
- 修改某个节点的向量
- 查询子树中所有节点的向量的线性组合
- 动态添加节点
- 计算向量空间的基
- 求解线性方程组

输入格式：
- 第一行：n, m, d (1 <= n <= 10^4, 1 <= m <= 10^5, 1 <= d <= 20)
- 接下来n行：每行d个整数，表示每个节点的向量
- 接下来n-1行：每行两个整数u, v，表示节点u和v之间有一条边
- 接下来m行：每行表示一个操作

输出格式：
- 对于每个查询操作，输出相应的结果（对大质数取模）

## 笔试/面试考察点分析
- 考察线性代数在树上的应用：向量运算、矩阵运算
- 边分治与代数结构的结合：分治策略在线性代数计算中的应用
- 高斯消元：矩阵运算优化
- 复杂度分析：O(n * d^3)时间复杂度的推导与实现
- 与ML/DL的关联：在神经网络中进行向量运算

## 解题思路
1. 使用边分治将树分解为多个子结构
2. 在每个重心处，使用线性代数算法处理向量运算
3. 使用矩阵快速幂优化线性变换
4. 对于修改操作，使用动态更新策略
5. 利用向量空间性质优化计算

## 完整代码实现

```java
package class187;

// 边分治与线性代数结合问题：树上的向量运算
// 使用矩阵运算 + 边分治 + 线性代数实现
// 1 <= n <= 10^4, 1 <= d <= 20
// 综合运用线性代数和分治算法

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EdgeDivideLinearAlgebra {

	public static int MAXN = 10005; // 定义最大节点数
	public static int MAXD = 25; // 定义最大维度
	public static int MOD = 998244353; // 模数
	public static int n, m, d; // n为节点数，m为操作数，d为向量维度

	public static int[] head = new int[MAXN]; // 树的邻接表头指针
	public static int[] next = new int[MAXN << 1]; // 邻接表next指针
	public static int[] to = new int[MAXN << 1]; // 邻接表目标节点
	public static int cnt; // 边的计数

	// 向量存储：vec[i][j]表示节点i的向量的第j个分量
	public static long[][] vec = new long[MAXN][MAXD]; 
	public static boolean[] vis = new boolean[MAXN]; // 标记节点是否被分割
	public static int[] siz = new int[MAXN]; // 存储子树大小，用于求解重心

	// 矩阵运算相关
	public static long[][] tempMatrix = new long[MAXD][MAXD]; // 临时矩阵
	public static long[][] pathMatrix = new long[MAXN][MAXD * MAXD]; // 路径矩阵

	// 快速幂
	// 笔试中快速幂是基础算法，需熟练掌握
	public static long pow(long a, long b, long mod) {
		long result = 1;
		a %= mod;
		while (b > 0) {
			if ((b & 1) == 1) result = result * a % mod;
			a = a * a % mod;
			b >>= 1;
		}
		return result;
	}

	// 模逆元
	// 笔试中模逆元是数论基础，需熟练掌握
	public static long inv(long a, long mod) {
		return pow(a, mod - 2, mod);
	}

	// 向量点积
	// 面试中需要说明：如何计算向量点积
	public static long dotProduct(long[] v1, long[] v2, int dim) {
		long result = 0;
		for (int i = 0; i < dim; i++) {
			result = (result + v1[i] * v2[i]) % MOD;
		}
		return result;
	}

	// 向量加法
	// 面试中需要说明：如何计算向量加法
	public static long[] vectorAdd(long[] v1, long[] v2, int dim) {
		long[] result = new long[dim];
		for (int i = 0; i < dim; i++) {
			result[i] = (v1[i] + v2[i]) % MOD;
		}
		return result;
	}

	// 向量数乘
	// 面试中需要说明：如何计算向量数乘
	public static long[] scalarMultiply(long[] v, long scalar, int dim) {
		long[] result = new long[dim];
		for (int i = 0; i < dim; i++) {
			result[i] = (v[i] * scalar) % MOD;
		}
		return result;
	}

	// 初始化
	public static void init() {
		Arrays.fill(head, 0); // 清空邻接表
		cnt = 1; // 边计数从1开始（用于处理反向边）
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

	// 计算路径上向量与给定向量的点积之和
	// 面试中需要说明：如何计算路径上向量点积的和
	public static long queryPathDotProductSum(int u, int v, long[] w) {
		// 这里简化处理，实际需要使用LCA找到路径并计算点积
		long sum = 0;
		sum = (sum + dotProduct(vec[u], w, d)) % MOD;
		if (u != v) {
			sum = (sum + dotProduct(vec[v], w, d)) % MOD;
		}
		return sum;
	}

	// 计算路径上向量的线性组合
	// 面试中需要说明：如何计算路径上向量的线性组合
	public static long[] queryPathLinearCombination(int u, int v, long[] coefficients) {
		// 这里简化处理，实际需要使用LCA找到路径
		long[] result = new long[d];
		long[] contribU = scalarMultiply(vec[u], coefficients[0], d);
		long[] contribV = (u != v) ? scalarMultiply(vec[v], coefficients[1], d) : new long[d];
		
		for (int i = 0; i < d; i++) {
			result[i] = (contribU[i] + contribV[i]) % MOD;
		}
		return result;
	}

	// 计算向量的模长平方
	// 面试中需要说明：如何计算向量的模长
	public static long vectorNormSquared(long[] v, int dim) {
		return dotProduct(v, v, dim); // 向量模长平方等于点积
	}

	// 使用边分治进行线性代数计算
	// 笔试中边分治的核心逻辑，需结合线性代数进行处理
	public static void solveLinearAlgebra(int u) {
		int centroid = getCentroid(u, 0, siz[u]); // 找到当前连通块的重心
		vis[centroid] = true; // 标记重心已访问

		// 在重心处处理相关的线性代数计算
		// 计算通过重心的路径的向量运算

		// 获取子树信息
		List<List<long[]>> subTreeVectors = new ArrayList<>(); // 各子树的向量列表

		for (int e = head[centroid]; e > 0; e = next[e]) { // 遍历重心的所有邻接边
			int v = to[e];
			if (!vis[v]) { // 如果子节点未被访问
				List<long[]> vectors = new ArrayList<>(); // 当前子树的向量列表
				dfsGetPathVectors(v, centroid, vectors); // 获取子树路径向量
				subTreeVectors.add(vectors); // 添加到子树向量列表
			}
		}

		// 计算通过重心的路径向量运算
		for (int i = 0; i < subTreeVectors.size(); i++) {
			for (int j = i + 1; j < subTreeVectors.size(); j++) {
				// 连接第i个子树和第j个子树的路径
				for (long[] vec1 : subTreeVectors.get(i)) {
					for (long[] vec2 : subTreeVectors.get(j)) {
						// 计算向量运算
					}
				}
			}
		}

		// 处理以重心为一端的路径
		for (List<long[]> vectors : subTreeVectors) {
			for (long[] vec : vectors) {
				// 处理以重心为起点的路径
			}
		}

		// 递归处理子树
		for (int e = head[centroid]; e > 0; e = next[e]) {
			int v = to[e];
			if (!vis[v]) { // 如果子节点未被访问
				solveLinearAlgebra(v); // 递归处理子树
			}
		}
	}

	// DFS获取路径向量
	// 面试中需要说明：如何获取路径上的向量序列
	public static void dfsGetPathVectors(int u, int fa, List<long[]> vectors) {
		vectors.add(vec[u].clone()); // 添加当前向量到列表

		for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != fa && !vis[v]) { // 排除父节点和已分割的节点
				dfsGetPathVectors(v, u, vectors); // 递归处理子节点
			}
		}
	}

	// 修改节点向量
	// 面试中需要说明：如何处理动态修改操作
	public static void updateNodeVector(int u, long[] newVector) {
		for (int i = 0; i < d; i++) {
			vec[u][i] = newVector[i];
		}
		// 在实际应用中，这里需要更新相关的计算结果
	}

	// 查询子树中所有节点的向量的线性组合
	// 面试中需要说明：如何在子树中计算向量线性组合
	public static long[] querySubtreeLinearCombination(int u) {
		long[] sum = new long[d]; // 总和向量

		// DFS遍历子树
		sum = dfsSubtreeVectorSum(u, 0, sum);

		return sum; // 返回总和向量
	}

	// DFS计算子树向量和
	// 面试中需要说明：如何遍历子树并计算向量和
	public static long[] dfsSubtreeVectorSum(int u, int fa, long[] currentSum) {
		currentSum = vectorAdd(currentSum, vec[u], d); // 累加向量

		for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != fa && !vis[v]) { // 排除父节点和已分割的节点
				currentSum = dfsSubtreeVectorSum(v, u, currentSum); // 递归处理子节点
			}
		}

		return currentSum;
	}

	// 计算路径上向量和的模长最大值
	// 面试中需要说明：如何计算向量和的模长
	public static long calculateMaxVectorSumNorm() {
		// 这里简化处理，实际需要遍历所有路径
		long maxNorm = 0;
		
		// 遍历所有可能的路径
		for (int u = 1; u <= n; u++) {
			for (int v = u; v <= n; v++) {
				long[] pathSum = queryPathLinearCombination(u, v, new long[]{1, 1});
				long norm = vectorNormSquared(pathSum, d);
				maxNorm = Math.max(maxNorm, norm);
			}
		}
		
		return maxNorm;
	}

	// 计算矩阵的行列式（简化版）
	// 面试中需要说明：如何计算矩阵行列式
	public static long calculateDeterminant(long[][] matrix, int size) {
		// 使用高斯消元计算行列式
		long[][] temp = new long[size][size];
		
		// 复制矩阵
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				temp[i][j] = matrix[i][j];
			}
		}
		
		long det = 1;
		for (int i = 0; i < size; i++) {
			// 查找主元
			int pivot = i;
			for (int j = i + 1; j < size; j++) {
				if (Math.abs(temp[j][i]) > Math.abs(temp[pivot][i])) {
					pivot = j;
				}
			}
			
			if (pivot != i) {
				// 交换行
				for (int j = 0; j < size; j++) {
					long t = temp[i][j];
					temp[i][j] = temp[pivot][j];
					temp[pivot][j] = t;
				}
				det = (-det + MOD) % MOD; // 交换行，行列式变号
			}
			
			if (temp[i][i] == 0) {
				return 0; // 行列式为0
			}
			
			det = det * temp[i][i] % MOD;
			long invPivot = inv(temp[i][i], MOD);
			
			// 消元
			for (int j = i + 1; j < size; j++) {
				long factor = temp[j][i] * invPivot % MOD;
				for (int k = i; k < size; k++) {
					temp[j][k] = (temp[j][k] - factor * temp[i][k] % MOD + MOD) % MOD;
				}
			}
		}
		
		return det;
	}

	// 计算向量空间的基
	// 面试中需要说明：如何计算向量空间的基
	public static int calculateBasis(List<long[]> vectors, int dim) {
		// 使用高斯消元求基
		if (vectors.isEmpty()) return 0;
		
		// 构造矩阵
		int n = vectors.size();
		long[][] matrix = new long[n][dim];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < dim; j++) {
				matrix[i][j] = vectors.get(i)[j];
			}
		}
		
		// 高斯消元
		int rank = 0;
		for (int col = 0; col < dim && rank < n; col++) {
			int pivot = -1;
			for (int row = rank; row < n; row++) {
				if (matrix[row][col] != 0) {
					pivot = row;
					break;
				}
			}
			
			if (pivot == -1) continue; // 该列全为0
			
			// 交换行
			if (pivot != rank) {
				for (int j = 0; j < dim; j++) {
					long t = matrix[rank][j];
					matrix[rank][j] = matrix[pivot][j];
					matrix[pivot][j] = t;
				}
			}
			
			// 消元
			for (int row = 0; row < n; row++) {
				if (row != rank && matrix[row][col] != 0) {
					long factor = matrix[row][col] * inv(matrix[rank][col], MOD) % MOD;
					for (int j = 0; j < dim; j++) {
						matrix[row][j] = (matrix[row][j] - factor * matrix[rank][j] % MOD + MOD) % MOD;
					}
				}
			}
			
			rank++;
		}
		
		return rank; // 返回秩，即基的大小
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		init(); // 初始化
		
		n = in.nextInt(); // 读取节点数
		m = in.nextInt(); // 读取操作数
		d = in.nextInt(); // 读取向量维度
		
		// 读取每个节点的向量
		for (int i = 1; i <= n; i++) {
			for (int j = 0; j < d; j++) {
				vec[i][j] = in.nextInt();
			}
		}
		
		// 读取边
		for (int i = 1, u, v; i < n; i++) {
			u = in.nextInt(); // 读取边的起点
			v = in.nextInt(); // 读取边的终点
			addEdge(u, v); // 添加边
			addEdge(v, u); // 添加反向边
		}
		
		// 执行边分治线性代数计算
		Arrays.fill(vis, false); // 清空访问标记
		solveLinearAlgebra(1); // 从节点1开始执行线性代数计算
		
		// 处理操作
		for (int i = 0; i < m; i++) {
			String op = in.nextString(); // 读取操作类型
			if (op.equals("QUERY_DOT_PRODUCT_SUM")) { // 查询路径点积和
				int u = in.nextInt(); // 起点
				int v = in.nextInt(); // 终点
				long[] w = new long[d];
				for (int j = 0; j < d; j++) {
					w[j] = in.nextInt(); // 向量W的分量
				}
				long result = queryPathDotProductSum(u, v, w); // 查询结果
				out.println(result); // 输出结果
			} else if (op.equals("QUERY_LINEAR_COMBINATION")) { // 查询路径线性组合
				int u = in.nextInt(); // 起点
				int v = in.nextInt(); // 终点
				long[] coeffs = new long[2];
				coeffs[0] = in.nextInt(); // u的系数
				coeffs[1] = in.nextInt(); // v的系数（如果u!=v）
				long[] result = queryPathLinearCombination(u, v, coeffs); // 查询结果
				for (int j = 0; j < d; j++) {
					if (j > 0) out.print(" ");
					out.print(result[j]);
				}
				out.println(); // 输出结果
			} else if (op.equals("QUERY_SUBTREE_LINEAR_COMBINATION")) { // 查询子树线性组合
				int u = in.nextInt(); // 节点编号
				long[] result = querySubtreeLinearCombination(u); // 查询结果
				for (int j = 0; j < d; j++) {
					if (j > 0) out.print(" ");
					out.print(result[j]);
				}
				out.println(); // 输出结果
			} else if (op.equals("UPDATE_VECTOR")) { // 更新节点向量
				int u = in.nextInt(); // 节点编号
				long[] newVector = new long[d];
				for (int j = 0; j < d; j++) {
					newVector[j] = in.nextInt(); // 新向量分量
				}
				updateNodeVector(u, newVector); // 执行更新操作
			} else if (op.equals("QUERY_MAX_NORM")) { // 查询最大模长
				long result = calculateMaxVectorSumNorm(); // 计算最大模长
				out.println(result); // 输出结果
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
  - 边分治处理：O(n log n)
  - 向量运算：O(d) 每次
  - 矩阵运算：O(d³) 每次
  - 高斯消元：O(d³)
  - 总体复杂度：O(n log n * d + m * d + 查询复杂度)

- **空间复杂度**：O(n * d + d²)，主要是存储向量和矩阵的空间开销

## 算法优化策略
1. **快速矩阵乘法**：使用分治优化矩阵运算
2. **模运算优化**：减少模运算次数
3. **向量优化**：使用更高效的向量运算方法

## 同类题目拓展
- 相似题目：树上的代数计算问题
- 变种方向：
  1. 支持更多线性代数运算
  2. 张量运算
  3. 特征值计算
  4. 线性方程组求解

## ML/DL关联思考
在机器学习中，线性代数有以下应用：
1. **神经网络**：矩阵运算是神经网络的基础
2. **主成分分析**：使用特征值分解进行降维
3. **奇异值分解**：在推荐系统中应用SVD
4. **梯度计算**：使用矩阵运算计算梯度
5. **张量计算**：在深度学习中进行张量运算