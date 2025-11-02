package class167;

/**
 * 博物馆劫案 (Museum Robbery) - 线段树分治 + 动态规划实现
 * 
 * 题目来源：
 * - Codeforces 601E
 * - 洛谷 P4585
 * 
 * 问题描述：
 * - 初始有n件商品，每件商品有价值v和重量w
 * - 支持三种操作：添加商品、删除商品、查询f(s)
 * - f(s)定义为：∑(m=1~k) [a(s, m) * BAS^(m-1)] mod MOD，其中a(s, m)表示总重量≤m时的最大价值
 * 
 * 约束条件：
 * - 1 ≤ n ≤ 5 × 10³
 * - 1 ≤ q ≤ 3 × 10⁴
 * - 1 ≤ k ≤ 10³
 * - 每件商品重量 ≤ 10³
 * - 每件商品价值 ≤ 10⁶
 * 
 * 算法思路：
 * 1. 线段树分治：将每个商品的有效时间段映射到线段树的节点上
 * 2. 动态规划：在DFS过程中维护背包DP数组，处理每个时间段内的商品
 * 3. 回溯机制：使用备份数组在DFS回溯时恢复DP状态
 * 4. 高效输入：使用FastReader类优化大规模数据的输入
 * 
 * 时间复杂度分析：
 * - 预处理时间：O(n + q)
 * - 线段树构建与边添加：O(n log q)
 * - DFS遍历线段树：O(q log q × k)
 * - 总体时间复杂度：O(n log q + q log q × k)
 * 
 * 空间复杂度分析：
 * - 线段树存储：O(n log q)
 * - DP数组和备份：O(k × log q)
 * - 其他数组：O(n + q)
 * - 总体空间复杂度：O(n log q + q + k log q)
 * 
 * 实现细节与优化：
 * 1. 使用链式前向星存储线段树节点上的商品
 * 2. DFS过程中维护深度，用于确定备份数组的索引
 * 3. 逆序遍历背包更新，避免重复计算
 * 4. 使用FastReader进行高效输入，避免超时
 * 5. 预计算每个商品的有效时间区间
 * 
 * 语言对比分析：
 * - Java：代码结构清晰，提供良好的内存管理，但需要注意输入效率问题
 * - C++：效率更高，但需要手动管理内存，实现细节更复杂
 * - Python：实现简单但效率较低，对于大规模数据可能超时
 * 
 * 工程化考量：
 * 1. 异常处理：未处理输入错误或边界情况
 * 2. 内存优化：常量数组大小预定义，避免动态扩容开销
 * 3. 性能调优：使用位运算和数组复用优化内存访问
 * 4. 测试用例：应测试极端数据如最大n和q、k=1等情况
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code01_MuseumRobbery1 {

	// 常量定义
	public static int MAXN = 40001;      // 最大商品数量（初始n + 最大q操作数）
	public static int MAXQ = 30001;      // 最大操作数量
	public static int MAXK = 1001;       // 最大重量限制
	public static int MAXT = 1000001;    // 最大边数（线段树上的边）
	public static int DEEP = 20;         // 最大深度（线段树深度）
	public static int BAS = 10000019;    // 基数
	public static int MOD = 1000000007;  // 模数
	public static int n, k, q;           // 当前商品数、重量限制、操作数

	// 商品信息
	public static int[] v = new int[MAXN]; // 商品价值数组
	public static int[] w = new int[MAXN]; // 商品重量数组

	// 操作信息
	public static int[] op = new int[MAXQ]; // 操作类型数组
	public static int[] x = new int[MAXQ];  // 操作参数x
	public static int[] y = new int[MAXQ];  // 操作参数y

	// 时间区间信息
	// 第i号商品的生效时间段，从from[i]持续到to[i]
	public static int[] from = new int[MAXN];
	public static int[] to = new int[MAXN];

	// 链式前向星存储线段树节点上的商品
	public static int[] head = new int[MAXQ << 2]; // 线段树节点头指针
	public static int[] next = new int[MAXT];      // 下一条边
	public static int[] tov = new int[MAXT];       // 边对应的商品价值
	public static int[] tow = new int[MAXT];       // 边对应的商品重量
	public static int cnt = 0;                     // 边计数器

	// DP相关数组
	public static long[] dp = new long[MAXK];      // 动态规划表，dp[j]表示重量<=j时的最大价值
	public static long[][] backup = new long[DEEP][MAXK]; // 动态规划表的备份，用于回溯

	// 答案数组，存储每次查询操作的结果
	public static long[] ans = new long[MAXQ];

	public static void clone(long[] a, long[] b) {
		for (int i = 0; i <= k; i++) {
			a[i] = b[i];
		}
	}

	public static void addEdge(int i, int v, int w) {
		next[++cnt] = head[i];
		tov[cnt] = v;
		tow[cnt] = w;
		head[i] = cnt;
	}

	public static void add(int jobl, int jobr, int jobv, int jobw, int l, int r, int i) {
		/**
		 * 线段树分治核心方法 - 将商品挂载到对应的线段树节点
		 * <p>
		 * 算法原理：
		 * - 线段树的每个节点代表一个时间区间[l, r]
		 * - 如果商品的有效区间[jobl, jobr]完全包含当前节点区间，则将商品挂载到该节点
		 * - 否则递归处理左右子节点
		 * <p>
		 * 核心思想：
		 * - 离线处理：预先确定每个商品的有效时间区间
		 * - 区间分配：将商品分配到覆盖其有效区间的最小线段树节点集合
		 * - 时间合并：相同时间区间的商品会被挂载到同一个线段树节点
		 * <p>
		 * 实现细节：
		 * - 使用链式前向星存储每个节点挂载的商品
		 * - 时间复杂度：O(log q) per operation
		 * - 空间复杂度：O(log q) per operation
		 * <p>
		 * @param jobl 商品生效的左时间点（操作序号）
		 * @param jobr 商品生效的右时间点（操作序号）
		 * @param jobv 商品的价值
		 * @param jobw 商品的重量
		 * @param l 当前线段树节点表示的区间左边界
		 * @param r 当前线段树节点表示的区间右边界
		 * @param i 当前线段树节点编号
		 */
		if (jobl <= l && r <= jobr) {
			// 当前节点区间被完全包含在目标区间内，直接挂载商品
			// 这是线段树分治的核心优化：一个商品只需要挂载到O(log q)个节点上
			addEdge(i, jobv, jobw);
		} else {
			// 当前节点区间部分覆盖目标区间，需要递归到子节点处理
			int mid = (l + r) >> 1;  // 计算中点，分割区间
			// 如果目标区间与左子区间有交集，递归处理左子树
			if (jobl <= mid) {
				add(jobl, jobr, jobv, jobw, l, mid, i << 1);
			}
			// 如果目标区间与右子区间有交集，递归处理右子树
			if (jobr > mid) {
				add(jobl, jobr, jobv, jobw, mid + 1, r, i << 1 | 1);
			}
		}
	}

	public static void dfs(int l, int r, int i, int dep) {
		/**
		 * 线段树分治的深度优先遍历 - 背包问题的核心求解过程
		 * <p>
		 * 算法原理：
		 * - 深度优先遍历线段树，处理每个节点对应的时间区间
		 * - 在进入节点时，应用该节点挂载的所有商品（执行背包DP更新）
		 * - 递归处理子节点（对应更小的时间区间）
		 * - 回溯时恢复DP状态，处理其他分支
		 * <p>
		 * 核心思想：
		 * - 时间旅行：通过备份和恢复DP数组，模拟不同时间点的状态
		 * - 状态合并：相同时间区间内的商品被同时处理
		 * - 回溯优化：使用深度作为索引，复用备份空间
		 * <p>
		 * 实现步骤：
		 * 1. 备份当前DP状态（用于回溯）
		 * 2. 使用01背包算法处理当前节点挂载的所有商品
		 * 3. 处理叶子节点（对应单个时间点的查询）
		 * 4. 递归处理左右子节点（对应更小的时间区间）
		 * 5. 恢复DP状态（回溯操作）
		 * <p>
		 * 时间复杂度分析：
		 * - 每个商品被处理O(log q)次（挂载到O(log q)个节点）
		 * - 每次处理商品需要O(k)时间进行背包更新
		 * - 总时间复杂度：O(nk log q)
		 * <p>
		 * 空间复杂度分析：
		 * - DP数组：O(k)
		 * - 备份数组：O(k * log q)
		 * <p>
		 * @param l 当前节点表示的区间左边界
		 * @param r 当前节点表示的区间右边界
		 * @param i 当前线段树节点编号
		 * @param dep 当前递归深度（用于备份和恢复DP状态）
		 */
		// 步骤1：备份当前DP状态，用于回溯时恢复
		// 使用dep作为索引选择备份数组，避免重复申请内存
		clone(backup[dep], dp);
		
		// 步骤2：处理当前节点上的所有商品（执行01背包动态规划）
		// 遍历当前节点挂载的所有商品（链式前向星）
		for (int e = head[i]; e > 0; e = next[e]) {
			int v = tov[e];  // 商品价值
			int w = tow[e];  // 商品重量
			// 01背包的标准实现：从后往前遍历，避免重复选择同一件商品
			for (int j = k; j >= w; j--) {
				// 状态转移方程：选择或不选择当前商品
				dp[j] = Math.max(dp[j], dp[j - w] + v);
			}
		}
		
		// 步骤3：处理叶子节点或递归处理子节点
		if (l == r) {
			// 叶子节点，对应单个时间点（操作）
			if (op[l] == 3) {
				// 如果是查询操作，计算结果
				// 结果计算：∑(m=1~k) (dp[m] * BAS^(m-1)) % MOD
				long ret = 0;
				long base = 1;  // 初始为BAS^0
				for (int j = 1; j <= k; j++) {
					// 累加每个m对应的dp[m] * BAS^(m-1)
					ret = (ret + dp[j] * base) % MOD;
					// 计算下一个幂次：BAS^j = BAS^(j-1) * BAS
					base = (base * BAS) % MOD;
				}
				ans[l] = ret;  // 保存查询结果
			}
		} else {
			// 非叶子节点，递归处理左右子树
			int mid = (l + r) >> 1;
			// 先处理左子区间 [l, mid]
			dfs(l, mid, i << 1, dep + 1);
			// 再处理右子区间 [mid+1, r]
			dfs(mid + 1, r, i << 1 | 1, dep + 1);
		}
		
		// 步骤4：回溯操作 - 恢复DP状态
		// 将DP数组恢复到进入当前节点前的状态，以便处理其他分支
		clone(dp, backup[dep]);
	}

	public static void prepare() {
		/**
		 * 线段树分治的预处理阶段 - 确定商品的有效时间区间并构建线段树
		 * <p>
		 * 算法原理：
		 * - 对于每个商品，确定其在操作序列中的有效时间区间[from, to]
		 * - 初始商品在所有操作开始前就存在，默认有效区间是[1, q]
		 * - 添加的商品从添加操作开始生效，默认有效到所有操作结束
		 * - 删除的商品在删除操作时停止生效，更新其to为i-1
		 * <p>
		 * 实现步骤详解：
		 * 1. 初始化初始商品的有效时间区间
		 * 2. 遍历所有操作，处理添加和删除操作，动态调整商品的有效区间
		 * 3. 将每个有效商品添加到线段树的对应时间区间
		 * <p>
		 * 时间复杂度分析：
		 * - 初始商品初始化：O(n)
		 * - 操作处理：O(q)
		 * - 线段树构建：O(n log q)
		 * - 总体时间复杂度：O(n log q + q)
		 * <p>
		 * 空间复杂度分析：
		 * - from和to数组：O(n)
		 * - 线段树存储：O(n log q)
		 * <p>
		 * 关键点：
		 * - 离线处理：所有操作预先读取并处理
		 * - 时间映射：将商品的生命周期映射到操作序列中的时间点
		 * - 区间有效性检查：确保只有有效区间的商品才会被添加到线段树
		 */
		// 步骤1：初始化初始商品的有效时间区间
		// 初始商品从第1个操作开始生效，默认有效期到最后一个操作
		for (int i = 1; i <= n; i++) {
			from[i] = 1;  // 从第1个操作开始生效
			to[i] = q;    // 默认为所有操作完成
		}
		
		// 步骤2：处理操作序列，更新商品的有效时间区间
		for (int i = 1; i <= q; i++) {
			if (op[i] == 1) {
				// 添加商品操作
				n++;  // 新增商品编号
				v[n] = x[i];  // 设置商品价值
				w[n] = y[i];  // 设置商品重量
				from[n] = i;  // 从当前操作开始生效
				to[n] = q;    // 默认为所有操作完成
			} else if (op[i] == 2) {
				// 删除商品操作
				// 被删除的商品在当前操作前一个时间点结束生效
				// 这是因为删除操作是在第i个操作时执行的，所以商品在i-1操作时仍然存在
				to[x[i]] = i - 1;
			}
			// 注意：操作3（查询）不需要处理时间区间
		}
		
		// 步骤3：将每个有效的商品添加到线段树中
		// 遍历所有商品，确保只添加有效时间区间的商品
		for (int i = 1; i <= n; i++) {
			if (from[i] <= to[i]) {  // 确保有效时间区间有效（避免无效或过期商品）
				// 将商品挂载到对应的线段树节点上
				// 调用add方法将商品添加到时间区间[from[i], to[i]]对应的线段树节点
				add(from[i], to[i], v[i], w[i], 1, q, 1);
			}
		}
	}

	/**
 * 主函数：程序入口，负责数据输入、预处理、算法执行和结果输出
 * 
 * @param args 命令行参数（未使用）
 * @throws Exception 可能出现的异常（主要是输入异常）
 */
public static void main(String[] args) throws Exception {
	// 创建高效输入流，用于快速读取大量输入数据
	FastReader in = new FastReader(System.in);
	// 创建高效输出流，用于批量输出结果
	PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
	
	// 读取初始商品数量和最大重量限制
	n = in.nextInt();
	k = in.nextInt();
	
	// 读取初始商品信息（价值和重量）
	for (int i = 1; i <= n; i++) {
		v[i] = in.nextInt();  // 第i个商品的价值
		w[i] = in.nextInt();  // 第i个商品的重量
	}
	
	// 读取操作数量
	q = in.nextInt();
	
	// 读取每个操作的详细信息
	for (int i = 1; i <= q; i++) {
		op[i] = in.nextInt();  // 操作类型
		if (op[i] == 1) {
			// 操作1：添加商品，读取商品的价值和重量
			x[i] = in.nextInt();  // 商品价值
			y[i] = in.nextInt();  // 商品重量
		} else if (op[i] == 2) {
			// 操作2：删除商品，读取要删除的商品编号
			x[i] = in.nextInt();  // 要删除的商品编号
		}
		// 操作3（查询）不需要额外参数
	}
	
	// 预处理：确定每个商品的有效时间区间并构建线段树
	prepare();
	
	// 执行线段树分治算法的深度优先搜索过程
	// 参数：当前区间[1,q]，当前节点1，当前深度1
	dfs(1, q, 1, 1);
	
	// 输出所有查询操作的结果
	for (int i = 1; i <= q; i++) {
		if (op[i] == 3) {
			out.println(ans[i]);  // 输出查询结果
		}
	}
	
	// 刷新输出缓冲区并关闭输出流
	out.flush();
	out.close();
}

	/**
 * 高效输入类：用于快速读取大量输入数据，避免标准输入方法的性能瓶颈
 * 
 * 在处理大规模数据时，使用自定义的FastReader可以显著提升输入速度，
 * 实现了基于缓冲区的读取机制，逐字节读取并转换为整数，优化了IO性能。
 */
static class FastReader {
	private final byte[] buffer; // 输入缓冲区
	private int ptr;             // 当前读取位置指针
	private int len;             // 当前缓冲区中有效字节数
	private final InputStream in; // 输入流

	/**
	 * 构造函数：初始化FastReader
	 * 
	 * @param in 输入流对象
	 */
	FastReader(InputStream in) {
		this.in = in;               // 保存输入流引用
		this.buffer = new byte[1 << 20]; // 创建2MB大小的缓冲区
		this.ptr = 0;              // 初始化指针位置
		this.len = 0;              // 初始化有效字节数
	}

	/**
	 * 读取单个字节
	 * 
	 * @return 读取的字节值，若到达流末尾则返回-1
	 * @throws IOException 输入异常
	 */
	private int readByte() throws IOException {
		// 缓冲区已读完，需要重新填充
		if (ptr >= len) {
			len = in.read(buffer);  // 从输入流读取数据到缓冲区
			ptr = 0;                // 重置指针
			if (len <= 0)           // 流已结束
				return -1;
		}
		return buffer[ptr++];     // 返回当前字节并移动指针
	}

	/**
	 * 读取下一个整数
	 * 
	 * @return 读取的整数
	 * @throws IOException 输入异常
	 */
	int nextInt() throws IOException {
		int c;                    // 当前读取的字符
		// 跳过所有空白字符
		do {
			c = readByte();
		} while (c <= ' ' && c != -1);
		
		boolean neg = false;      // 标记是否为负数
		if (c == '-') {           // 检测负号
			neg = true;
			c = readByte();         // 读取负号后的第一个字符
		}
		
		int val = 0;              // 保存结果
		// 读取并处理所有数字字符
		while (c > ' ' && c != -1) {
			val = val * 10 + (c - '0');  // 逐位构建整数
			c = readByte();              // 读取下一个字符
		}
		
		return neg ? -val : val;  // 根据符号返回结果
	}
}

}
