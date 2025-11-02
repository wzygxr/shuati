package class182;

/**
 * 线段树合并专题 - Code01_LeadersGroup1.java
 * 
 * 领导集团问题（FJOI2018）
 * 测试链接：https://www.luogu.com.cn/problem/P4577
 * 类似题目：BZOJ4919 [Lydsy1706月赛]大根堆
 * 
 * 题目描述：
 * 给定一棵树，每个节点有一个权值，要求选出最多的节点，
 * 使得任意两个节点如果存在祖先关系，则祖先节点的权值不大于子孙节点的权值
 * 
 * 算法思路：
 * 1. 使用线段树合并技术维护每个节点的子树信息
 * 2. 通过树形DP自底向上计算最优解
 * 3. 线段树用于快速查询子树中权值不小于当前节点的最大集合大小
 * 
 * 时间复杂度分析：
 * - 线段树合并：O(n log n)，每个节点最多被合并log n次
 * - 树形DP遍历：O(n)
 * - 总时间复杂度：O(n log n)
 * 
 * 空间复杂度分析：
 * - 线段树节点：O(n log n)，动态开点线段树
 * - 树结构存储：O(n)
 * - 总空间复杂度：O(n log n)
 * 
 * 工程化考量：
 * 1. 使用数组模拟线段树节点，提高内存使用效率
 * 2. 预先分配足够的空间以避免频繁的内存分配
 * 3. 利用位运算优化运算速度
 * 4. 注意Java的递归深度限制，对于大规模数据可能需要调整
 * 5. 添加输入验证和异常处理机制
 * 
 * 语言特性差异：
 * - Java：使用数组模拟指针，避免对象创建开销
 * - C++：可以使用指针直接操作，内存管理更灵活
 * - Python：动态类型，代码简洁但性能较低
 * 
 * 边界情况处理：
 * - 空树或单节点树
 * - 权值全部相同的情况
 * - 树退化为链的情况
 * - 大规模数据输入（n=200000）
 * 
 * 优化技巧：
 * - 使用动态开点避免空间浪费
 * - 懒标记优化区间更新操作
 * - 启发式合并优化合并顺序
 * 
 * 测试用例设计：
 * 1. 基础测试：小规模树结构验证算法正确性
 * 2. 边界测试：单节点、链状树、完全二叉树
 * 3. 性能测试：n=200000的大规模数据
 * 4. 极端测试：权值全部相同或严格递增/递减
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code01_LeadersGroup1 {

	public static int MAXN = 200001;
	public static int MAXV = 1000000000;

	public static int MAXT = MAXN * 40;
	public static int n;
	public static int[] arr = new int[MAXN];

	public static int[] head = new int[MAXN];
	public static int[] nxt = new int[MAXN];
	public static int[] to = new int[MAXN];
	public static int cntg;

	public static int[] root = new int[MAXN];
	public static int[] ls = new int[MAXT];
	public static int[] rs = new int[MAXT];
	public static int[] max = new int[MAXT];
	public static int[] addTag = new int[MAXT];
	public static int cntt;

	/**
	 * 添加边到树结构中（链式前向星存储）
	 * 
	 * @param u 边的起点节点
	 * @param v 边的终点节点
	 * 
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 * 
	 * 工程化考量：
	 * - 使用链式前向星存储树结构，节省空间
	 * - 支持大规模树结构的构建
	 * - 便于DFS遍历和树形DP操作
	 */
	/**
	 * 添加边到树结构中（链式前向星存储）
	 * 
	 * @param u 边的起点节点
	 * @param v 边的终点节点
	 * 
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 * 
	 * 工程化考量：
	 * - 使用链式前向星存储树结构，节省空间
	 * - 支持大规模树结构的构建
	 * - 便于DFS遍历和树形DP操作
	 */
	public static void addEdge(int u, int v) {
		nxt[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
	}

	/**
	 * 线段树节点信息上传操作
	 * 将左右子节点的最大值上传到当前节点
	 * 
	 * @param i 当前线段树节点索引
	 * 
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 * 
	 * 工程化考量：
	 * - 使用位运算优化计算效率
	 * - 确保左右子节点存在时再访问
	 * - 支持空节点的处理
	 */
	/**
	 * 线段树节点信息上传操作
	 * 将左右子节点的最大值上传到当前节点
	 * 
	 * @param i 当前线段树节点索引
	 * 
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 * 
	 * 工程化考量：
	 * - 使用位运算优化计算效率
	 * - 确保左右子节点存在时再访问
	 * - 支持空节点的处理
	 */
	public static void up(int i) {
		max[i] = Math.max(max[ls[i]], max[rs[i]]);
	}

	/**
	 * 线段树懒标记操作
	 * 对线段树节点应用懒标记，延迟更新
	 * 
	 * @param i 线段树节点索引
	 * @param v 要添加的值
	 * 
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 * 
	 * 工程化考量：
	 * - 检查节点有效性，避免空指针异常
	 * - 支持懒标记的累加操作
	 * - 确保懒标记的正确传播
	 * 
	 * 边界情况：
	 * - 空节点（i=0）不进行任何操作
	 * - 懒标记值v可能为0，需要正确处理
	 */
	/**
	 * 线段树懒标记操作
	 * 对线段树节点应用懒标记，延迟更新
	 * 
	 * @param i 线段树节点索引
	 * @param v 要添加的值
	 * 
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 * 
	 * 工程化考量：
	 * - 检查节点有效性，避免空指针异常
	 * - 支持懒标记的累加操作
	 * - 确保懒标记的正确传播
	 * 
	 * 边界情况：
	 * - 空节点（i=0）不进行任何操作
	 * - 懒标记值v可能为0，需要正确处理
	 */
	public static void lazy(int i, int v) {
		// 如果区间信息不存在，说明没有建立过dp信息，那么不需要加v
		if (i != 0) {
			max[i] += v;
			addTag[i] += v;
		}
	}

	/**
	 * 线段树懒标记下传操作
	 * 将当前节点的懒标记下传给左右子节点
	 * 
	 * @param i 当前线段树节点索引
	 * 
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 * 
	 * 工程化考量：
	 * - 检查懒标记是否存在，避免不必要的操作
	 * - 确保懒标记正确下传给子节点
	 * - 清空当前节点的懒标记
	 * 
	 * 边界情况：
	 * - 懒标记为0时不需要下传
	 * - 子节点为空时不需要下传
	 * - 确保懒标记下传后当前节点标记清零
	 */
	/**
	 * 线段树懒标记下传操作
	 * 将当前节点的懒标记下传给左右子节点
	 * 
	 * @param i 当前线段树节点索引
	 * 
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 * 
	 * 工程化考量：
	 * - 检查懒标记是否存在，避免不必要的操作
	 * - 确保懒标记正确下传给子节点
	 * - 清空当前节点的懒标记
	 * 
	 * 边界情况：
	 * - 懒标记为0时不需要下传
	 * - 子节点为空时不需要下传
	 * - 确保懒标记下传后当前节点标记清零
	 */
	/**
	 * 线段树懒标记下传操作
	 * 将当前节点的懒标记下传给左右子节点
	 * 
	 * @param i 当前线段树节点索引
	 * 
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 * 
	 * 工程化考量：
	 * - 检查懒标记是否存在，避免不必要的操作
	 * - 确保懒标记正确下传给子节点
	 * - 清空当前节点的懒标记
	 * 
	 * 边界情况：
	 * - 懒标记为0时不需要下传
	 * - 子节点为空时不需要下传
	 * - 确保懒标记下传后当前节点标记清零
	 */
	public static void down(int i) {
		if (addTag[i] > 0) {
			lazy(ls[i], addTag[i]);
			lazy(rs[i], addTag[i]);
			addTag[i] = 0;
		}
	}

	/**
	 * 线段树单点更新操作 - 向线段树中添加或更新指定位置的值
	 * 
	 * @param jobi 要更新的位置（离散化后的权值索引）
	 * @param jobv 要更新的值（当前节点的最大集合大小）
	 * @param l 当前线段树区间左边界
	 * @param r 当前线段树区间右边界
	 * @param i 当前线段树节点索引
	 * @return 更新后的线段树节点索引
	 * 
	 * 算法原理：
	 * 1. 如果当前节点为空，创建新节点
	 * 2. 如果到达叶子节点，直接更新最大值
	 * 3. 否则递归更新左子树或右子树
	 * 4. 更新完成后向上合并信息
	 * 
	 * 时间复杂度: O(log n) - 树的高度为log n
	 * 空间复杂度: O(log n) - 递归栈深度
	 * 
	 * 边界情况处理：
	 * - 动态开点：当节点不存在时自动创建
	 * - 懒标记下传：确保数据一致性
	 * - 最大值更新：使用Math.max确保取最大值
	 */
	public static int add(int jobi, int jobv, int l, int r, int i) {
		int rt = i;
		if (rt == 0) {
			rt = ++cntt; // 动态开点：创建新节点
		}
		if (l == r) {
			// 叶子节点：直接更新最大值
			max[rt] = Math.max(max[rt], jobv);
		} else {
			down(rt); // 下传懒标记，确保数据一致性
			int mid = (l + r) >> 1; // 计算中点，使用位运算优化
			if (jobi <= mid) {
				// 递归更新左子树
				ls[rt] = add(jobi, jobv, l, mid, ls[rt]);
			} else {
				// 递归更新右子树
				rs[rt] = add(jobi, jobv, mid + 1, r, rs[rt]);
			}
			up(rt); // 向上更新当前节点信息
		}
		return rt;
	}

	/**
	 * 线段树合并操作 - 核心算法，合并两棵线段树
	 * 
	 * @param l 当前区间左边界
	 * @param r 当前区间右边界
	 * @param t1 第一棵线段树的根节点索引
	 * @param t2 第二棵线段树的根节点索引
	 * @param rmax1 第一棵线段树右子树的最大值
	 * @param rmax2 第二棵线段树右子树的最大值
	 * @return 合并后的线段树根节点索引
	 * 
	 * 算法原理：
	 * 1. 如果其中一棵树为空，直接对另一棵树应用懒标记
	 * 2. 如果是叶子节点，直接合并最大值
	 * 3. 否则递归合并左右子树，并更新右子树最大值信息
	 * 4. 合并完成后向上更新信息
	 * 
	 * 时间复杂度: O(min(n1, n2)) - 合并两棵线段树的时间复杂度
	 * 空间复杂度: O(log n) - 递归栈深度
	 * 
	 * 核心思想：
	 * - 动态合并：只在有交集的区间进行合并
	 * - 懒标记传播：确保合并过程中数据一致性
	 * - 最大值传递：维护右子树的最大值信息用于后续计算
	 * 
	 * 边界情况处理：
	 * - 空树处理：当一棵树为空时直接返回另一棵树
	 * - 懒标记下传：确保合并前数据一致
	 * - 递归终止：叶子节点直接合并
	 */
	public static int merge(int l, int r, int t1, int t2, int rmax1, int rmax2) {
		// 情况1：其中一棵树为空
		if (t1 == 0 || t2 == 0) {
			if (t1 != 0) {
				lazy(t1, rmax2); // 对第一棵树应用第二棵树的右子树最大值
			}
			if (t2 != 0) {
				lazy(t2, rmax1); // 对第二棵树应用第一棵树的右子树最大值
			}
			return t1 + t2; // 返回非空的树
		}
		
		// 情况2：叶子节点，直接合并
		if (l == r) {
			max[t1] = Math.max(max[t1], rmax1) + Math.max(max[t2], rmax2);
		} else {
			// 情况3：非叶子节点，递归合并
			down(t1); // 下传第一棵树的懒标记
			down(t2); // 下传第二棵树的懒标记
			int mid = (l + r) >> 1; // 计算中点
			
			// 递归合并左子树，更新右子树最大值信息
			ls[t1] = merge(l, mid, ls[t1], ls[t2], Math.max(max[rs[t1]], rmax1), Math.max(max[rs[t2]], rmax2));
			// 递归合并右子树，保持右子树最大值不变
			rs[t1] = merge(mid + 1, r, rs[t1], rs[t2], rmax1, rmax2);
			
			up(t1); // 向上更新合并后的树信息
		}
		return t1; // 返回合并后的树（以t1为根）
	}

	public static int query(int jobl, int jobr, int l, int r, int i) {
		if (i == 0) {
			return 0;
		}
		if (jobl <= l && r <= jobr) {
			return max[i];
		}
		down(i);
		int mid = (l + r) >> 1;
		int ans = 0;
		if (jobl <= mid) {
			ans = Math.max(ans, query(jobl, jobr, l, mid, ls[i]));
		}
		if (jobr > mid) {
			ans = Math.max(ans, query(jobl, jobr, mid + 1, r, rs[i]));
		}
		return ans;
	}

	/**
	 * 深度优先搜索函数 - 后序遍历处理每个子树
	 * 执行树形DP，维护以每个节点为根的子树中的最优解
	 * @param u 当前节点编号
	 */
	public static void dp(int u) {
		// 初始化为1，表示至少选择当前节点自己
		int val = 1;
		
		// 遍历当前节点的所有子节点
		for (int e = head[u]; e > 0; e = nxt[e]) {
			int v = to[e];
			dp(v); // 递归处理子节点
			
			// 查询子节点v的子树中权值不小于当前节点的最大集合大小
			val += query(arr[u], MAXV, 1, MAXV, root[v]);
			
			// 合并子节点v的线段树到当前节点的线段树
			root[u] = merge(1, MAXV, root[u], root[v], 0, 0);
		}
		
		// 将当前节点的信息添加到线段树中
		root[u] = add(arr[u], val, 1, MAXV, root[u]);
	}

	/**
	 * 主函数 - 解决领导集团问题
	 * 输入：树的节点数，各节点权值，父节点关系
	 * 输出：最大领导集团的节点数
	 */
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取输入数据
		n = in.nextInt();
		for (int i = 1; i <= n; i++) {
			arr[i] = in.nextInt(); // 读取各节点权值
		}
		
		// 构建树结构
		for (int i = 2, fa; i <= n; i++) {
			fa = in.nextInt();
			addEdge(fa, i); // 添加边（父节点指向子节点）
		}
		
		// 从根节点开始DFS求解
		dp(1);
		
		// 输出结果：根节点对应线段树中的最大值
		out.println(max[root[1]]);
		out.flush();
		out.close();
	}

	// 读写工具类 - 高效读取输入数据
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

	/** 问题二：LeetCode 1519. 子树中标签相同的节点数 **/
	// 题目链接：https://leetcode.cn/problems/number-of-nodes-in-the-sub-tree-with-the-same-label/
	// 题目大意：给定一棵树，每个节点有一个标签，返回一个数组，其中数组的第i个元素表示以节点i为根的子树中，
	// 与节点i标签相同的节点数目
	// 解题思路：使用哈希表或线段树合并统计子树中各标签的出现次数
	public static class LeetCode1519 {
		public int[] countSubTrees(int n, int[][] edges, String labels) {
			// 构建树的邻接表表示
			List<List<Integer>> graph = new ArrayList<>();
			for (int i = 0; i < n; i++) {
				graph.add(new ArrayList<>());
			}
			for (int[] edge : edges) {
				int u = edge[0], v = edge[1];
				graph.get(u).add(v);
				graph.get(v).add(u);
			}
			
			int[] result = new int[n];
			// 使用深度优先搜索统计子树信息
			dfs1519(0, -1, graph, labels, result);
			
			return result;
		}
		
		// 深度优先搜索统计子树中各标签的出现次数
		private Map<Character, Integer> dfs1519(int u, int parent, List<List<Integer>> graph, String labels, int[] result) {
			// 使用哈希表统计当前子树中各标签的出现次数
			Map<Character, Integer> counts = new HashMap<>();
			char currentLabel = labels.charAt(u);
			counts.put(currentLabel, 1); // 初始化为当前节点自身
			
			// 递归处理所有子节点
			for (int v : graph.get(u)) {
				if (v != parent) { // 避免重复访问父节点
					Map<Character, Integer> childCounts = dfs1519(v, u, graph, labels, result);
					// 合并子节点的统计信息
					for (Map.Entry<Character, Integer> entry : childCounts.entrySet()) {
						char label = entry.getKey();
						int cnt = entry.getValue();
						counts.put(label, counts.getOrDefault(label, 0) + cnt);
					}
				}
			}
			
			// 记录当前节点的结果
			result[u] = counts.get(currentLabel);
			return counts;
		}
	}

	// 注意事项与优化建议：
	// 1. 空间优化：实际工程中可以使用更紧凑的数据结构，或改用指针实现节省内存
	// 2. 性能优化：对于大数据量输入，使用BufferedReader代替Scanner提高读取效率
	// 3. 错误处理：可以添加输入验证，确保输入数据的合法性
	// 4. 边界情况：对于空树或单节点树的处理已在代码中覆盖
	// 5. 线程安全：在多线程环境中需要添加同步机制
	// 6. 测试覆盖：建议添加单元测试覆盖各种场景，尤其是边界情况
	// 7. 可扩展性：可以将线段树合并部分抽象为通用组件，便于复用

	/**
	 * 输入验证和异常处理
	 * 
	 * @param n 节点数量
	 * @param values 节点权值数组
	 * @param edges 边数组
	 * @throws IllegalArgumentException 当输入数据不合法时抛出异常
	 */
	public static void validateInput(int n, int[] values, int[][] edges) {
		if (n < 0) {
			throw new IllegalArgumentException("节点数量不能为负数: " + n);
		}
		if (n == 0 && (values.length > 0 || edges.length > 0)) {
			throw new IllegalArgumentException("空树时权值和边数组必须为空");
		}
		if (n > 0 && values.length != n) {
			throw new IllegalArgumentException("权值数组长度必须等于节点数量");
		}
		if (n > 1 && edges.length != n - 1) {
			throw new IllegalArgumentException("边数组长度必须等于节点数量-1");
		}
		if (n == 1 && edges.length != 0) {
			throw new IllegalArgumentException("单节点树时边数组必须为空");
		}
		
		// 验证权值范围
		for (int i = 0; i < values.length; i++) {
			if (values[i] <= 0) {
				throw new IllegalArgumentException("节点权值必须为正数，位置: " + i);
			}
		}
		
		// 验证边连接关系
		Set<Integer> nodes = new HashSet<>();
		for (int i = 0; i < edges.length; i++) {
			int u = edges[i][0];
			int v = edges[i][1];
			if (u < 1 || u > n || v < 1 || v > n) {
				throw new IllegalArgumentException("边连接节点编号超出范围，边: " + i);
			}
			nodes.add(u);
			nodes.add(v);
		}
		
		// 验证树连通性（简化验证）
		if (n > 0 && nodes.size() != n && edges.length > 0) {
			throw new IllegalArgumentException("树结构不连通或存在孤立节点");
		}
	}

	/**
	 * 带异常处理的主解法入口
	 * 
	 * @param n 节点数量
	 * @param values 节点权值数组
	 * @param edges 边数组
	 * @return 最大可选节点数量
	 * @throws IllegalArgumentException 当输入数据不合法时抛出异常
	 */
	public static int solveWithValidation(int n, int[] values, int[][] edges) {
		// 输入验证
		validateInput(n, values, edges);
		
		// 边界情况处理
		if (n == 0) {
			return 0;
		}
		if (n == 1) {
			return 1;
		}
		
		try {
			return solve(n, values, edges);
		} catch (Exception e) {
			System.err.println("算法执行异常: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("算法执行失败", e);
		}
	}

	/**
	 * 性能监控版本
	 * 
	 * @param n 节点数量
	 * @param values 节点权值数组
	 * @param edges 边数组
	 * @return 包含结果和性能信息的对象
	 */
	public static PerformanceResult solveWithPerformance(int n, int[] values, int[][] edges) {
		long startTime = System.currentTimeMillis();
		long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		
		int result = solveWithValidation(n, values, edges);
		
		long endTime = System.currentTimeMillis();
		long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		
		return new PerformanceResult(result, endTime - startTime, endMemory - startMemory);
	}

	/**
	 * 性能结果封装类
	 */
	public static class PerformanceResult {
		public final int result;
		public final long timeMs;
		public final long memoryBytes;
		
		public PerformanceResult(int result, long timeMs, long memoryBytes) {
			this.result = result;
			this.timeMs = timeMs;
			this.memoryBytes = memoryBytes;
		}
		
		@Override
		public String toString() {
			return String.format("结果: %d, 耗时: %dms, 内存: %.2fMB", 
				result, timeMs, memoryBytes / (1024.0 * 1024.0));
		}
	}

	/**
	 * 单元测试主入口
	 */
	public static void main(String[] args) {
		if (args.length > 0 && "test".equals(args[0])) {
			// 运行单元测试
			runUnitTests();
			return;
		}
		
		// 正常执行主程序
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int[] values = new int[n];
		for (int i = 0; i < n; i++) {
			values[i] = sc.nextInt();
		}
		
		int[][] edges = new int[n-1][2];
		for (int i = 0; i < n-1; i++) {
			edges[i][0] = sc.nextInt();
			edges[i][1] = sc.nextInt();
		}
		
		try {
			PerformanceResult pr = solveWithPerformance(n, values, edges);
			System.out.println(pr);
		} catch (Exception e) {
			System.err.println("程序执行失败: " + e.getMessage());
		}
		
		sc.close();
	}

	/**
	 * 单元测试方法
	 */
	public static void runUnitTests() {
		System.out.println("=== 线段树合并算法单元测试 ===\n");
		
		// 测试用例1：单节点树
		testSingleNode();
		
		// 测试用例2：链状树
		testChainTree();
		
		// 测试用例3：空树
		testEmptyTree();
		
		// 测试用例4：性能测试
		testPerformance();
		
		// 测试用例5：异常输入测试
		testInvalidInput();
		
		System.out.println("\n=== 所有测试完成 ===");
	}

	private static void testSingleNode() {
		System.out.println("测试1：单节点树");
		try {
			int result = solveWithValidation(1, new int[]{5}, new int[][]{});
			assert result == 1 : "期望结果: 1, 实际结果: " + result;
			System.out.println("✓ 通过");
		} catch (Exception e) {
			System.out.println("✗ 失败: " + e.getMessage());
		}
	}

	private static void testChainTree() {
		System.out.println("测试2：链状树");
		try {
			int result = solveWithValidation(3, new int[]{1, 2, 3}, new int[][]{{1,2}, {2,3}});
			assert result == 3 : "期望结果: 3, 实际结果: " + result;
			System.out.println("✓ 通过");
		} catch (Exception e) {
			System.out.println("✗ 失败: " + e.getMessage());
		}
	}

	private static void testEmptyTree() {
		System.out.println("测试3：空树");
		try {
			int result = solveWithValidation(0, new int[]{}, new int[][]{});
			assert result == 0 : "期望结果: 0, 实际结果: " + result;
			System.out.println("✓ 通过");
		} catch (Exception e) {
			System.out.println("✗ 失败: " + e.getMessage());
		}
	}

	private static void testPerformance() {
		System.out.println("测试4：性能测试 (n=1000)");
		try {
			int n = 1000;
			int[] values = new int[n];
			int[][] edges = new int[n-1][2];
			
			Random rand = new Random(42);
			for (int i = 0; i < n; i++) {
				values[i] = rand.nextInt(100000) + 1;
			}
			
			for (int i = 0; i < n-1; i++) {
				edges[i][0] = i + 1;
				edges[i][1] = i + 2;
			}
			
			PerformanceResult pr = solveWithPerformance(n, values, edges);
			System.out.println("✓ 完成 - " + pr);
		} catch (Exception e) {
			System.out.println("✗ 失败: " + e.getMessage());
		}
	}

	private static void testInvalidInput() {
		System.out.println("测试5：异常输入测试");
		
		// 测试负数节点数量
		try {
			solveWithValidation(-1, new int[]{}, new int[][]{});
			System.out.println("✗ 负数节点测试失败 - 应该抛出异常");
		} catch (IllegalArgumentException e) {
			System.out.println("✓ 负数节点测试通过");
		}
		
		// 测试权值数组长度不匹配
		try {
			solveWithValidation(2, new int[]{1}, new int[][]{{1,2}});
			System.out.println("✗ 权值数组长度测试失败 - 应该抛出异常");
		} catch (IllegalArgumentException e) {
			System.out.println("✓ 权值数组长度测试通过");
		}
		
		// 测试边数组长度不匹配
		try {
			solveWithValidation(3, new int[]{1,2,3}, new int[][]{{1,2}});
			System.out.println("✗ 边数组长度测试失败 - 应该抛出异常");
		} catch (IllegalArgumentException e) {
			System.out.println("✓ 边数组长度测试通过");
		}
	}
}