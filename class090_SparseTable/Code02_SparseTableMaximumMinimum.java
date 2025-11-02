package class117;

/**
 * Sparse Table（稀疏表）算法详解 - 区间最大最小值查询
 * 
 * 【算法核心思想】
 * Sparse Table基于倍增思想，通过预处理所有长度为2的幂次的区间答案，实现O(n log n)预处理，O(1)查询
 * 对于可重复贡献的问题（如最大值、最小值、GCD等），任何区间查询都可以通过两个重叠的预处理区间覆盖
 * 核心原理是利用动态规划，从长度为1的区间开始，逐步构建更长区间的答案
 * 
 * 【核心原理说明】
 * 1. 倍增思想：将任意长度的区间分解为2的幂次长度的区间组合
 * 2. 动态规划构建：st[i][j]表示从位置i开始，长度为2^j的区间的结果
 * 3. 状态转移方程：st[i][j] = merge(st[i][j-1], st[i+2^(j-1)][j-1])，其中merge操作根据问题性质定义（如max、min、gcd等）
 * 4. 查询原理：对于区间[l,r]，找到最大的k满足2^k ≤ (r-l+1)，然后查询覆盖区间的两个预处理区间
 * 
 * 【位运算常用技巧】
 * 1. 位移运算：1 << k 等价于 2^k，比Math.pow(2,k)更高效
 * 2. 整数除法：i >> 1 等价于 i / 2，用于快速计算log2值
 * 3. 位掩码：使用位运算快速判断和计算区间覆盖
 * 4. 快速幂：利用位运算优化幂次计算
 * 5. 奇偶判断：i & 1 等价于 i % 2，用于判断奇偶性
 * 6. 区间长度计算：r - l + 1 表示闭区间的长度
 * 7. 最大值2的幂次查找：利用log_table快速获取不超过某个数的最大2的幂次
 * 
 * 【时间复杂度分析】
 * - 预处理时间复杂度：O(n log n) - 需要预处理log n层，每层处理n个元素
 * - 查询时间复杂度：O(1) - 每次查询只需查表两次并取最值
 * - 空间复杂度：O(n log n) - 需要存储n个元素的log n层信息
 * 
 * 【应用场景】
 * 适用于静态数据的区间查询问题，不支持动态修改操作
 * 主要用于RMQ（Range Maximum/Minimum Query）问题，也可用于区间GCD查询等
 * 特别适合需要进行大量查询的场景，如在线查询系统、数据分析等
 * 1. 大数据分析中的快速区间统计
 * 2. 游戏开发中的范围检测和碰撞计算
 * 3. 网络流量监控中的异常检测
 * 4. 金融数据分析中的价格波动区间查询
 * 5. 图像处理中的区域特征提取
 * 6. 数据库系统中的索引优化
 * 7. 科学计算中的区间最值快速查询
 * 8. 竞赛编程中的算法优化
 * 
 * 【相关题目】
 * 1. 洛谷P2880 - 给定数组，多次查询区间最大值与最小值的差
 * 2. LeetCode 1893 - 检查是否区域内所有整数都被覆盖（可使用ST表优化）
 * 3. LeetCode 2448 - 使数组相等的最小开销（结合ST表和贪心算法）
 * 4. Codeforces 1311E - Concatenation with Beautiful Strings（可使用ST表预处理最值）
 * 5. UVA 12532 - Interval Product（区间乘积符号查询，可使用ST表）
 * 6. AtCoder ABC189 C - Mandarin Orange（结合ST表和单调栈的题目）
 * 7. SPOJ RMQSQ - 标准的区间最小值查询问题
 * 8. POJ 3264 - Balanced Lineup（区间最大值与最小值之差）
 * 9. HackerRank Maximum Element in a Subarray（使用ST表高效查询）
 * 10. HDU 1548 - A strange lift（可使用ST表优化最短路径查询）
 * 11. CodeChef XORSUBAR - XOR Subarray（结合ST表和位运算）
 * 12. USACO 2017 January Contest, Bronze - Promotion Counting（可使用ST表优化）
 * 13. LintCode 425 - Letter Combinations of a Phone Number（可结合ST表优化）

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code02_SparseTableMaximumMinimum {

	// 最大数据规模
	public static int MAXN = 50001;

	// 2的15次方是<=50001且最接近的
	// 所以次方可能是0~15
	// 于是准备16长度够用了
	public static int LIMIT = 16;

	// 存储原始数组数据
	public static int[] arr = new int[MAXN];

	// log2[i] : 查询<=i情况下，最大的2的幂，是2的几次方
	// 预处理log数组以避免重复计算，提高效率
	public static int[] log2 = new int[MAXN];

	// 最大值ST表：stmax[i][j]表示从位置i开始，长度为2^j的区间的最大值
	public static int[][] stmax = new int[MAXN][LIMIT];

	// 最小值ST表：stmin[i][j]表示从位置i开始，长度为2^j的区间的最小值
	public static int[][] stmin = new int[MAXN][LIMIT];

	/**
	 * 构建Sparse Table
	 * @param n 数组长度
	 * 核心功能：预处理所有长度为2的幂次的区间最大值和最小值
	 * 实现原理：
	 * 1. 首先预处理log2数组，用于快速计算区间长度对应的最大2的幂次
	 * 2. 初始化ST表的第0层（长度为1的区间）
	 * 3. 动态规划构建更高层的ST表，每层依赖于前一层的结果
	 * 时间复杂度：O(n log n)
	 */
	public static void build(int n) {
		// 预处理log2数组，log2[0]初始化为-1是为了计算方便
		log2[0] = -1;
		for (int i = 1; i <= n; i++) {
			// 使用位运算高效计算log2值
			log2[i] = log2[i >> 1] + 1;
			// 初始化长度为1的区间（j=0），即每个元素自身
			stmax[i][0] = arr[i];
			stmin[i][0] = arr[i];
		}
		
		// 动态规划构建ST表
		// p表示区间长度为2^p
		for (int p = 1; p <= log2[n]; p++) {
			// i表示区间起始位置，确保区间不越界
			for (int i = 1; i + (1 << p) - 1 <= n; i++) {
				// 状态转移方程：当前区间的最值由两个子区间的最值合并而来
				// 子区间1: [i, i + 2^(p-1) - 1]，对应stmax[i][p-1]
				// 子区间2: [i + 2^(p-1), i + 2^p - 1]，对应stmax[i + (1 << (p-1))][p-1]
				stmax[i][p] = Math.max(stmax[i][p - 1], stmax[i + (1 << (p - 1))][p - 1]);
				stmin[i][p] = Math.min(stmin[i][p - 1], stmin[i + (1 << (p - 1))][p - 1]);
			}
		}
	}

	/**
	 * 查询区间[l,r]的最大值与最小值的差
	 * @param l 区间左边界（1-based）
	 * @param r 区间右边界（1-based）
	 * @return 区间最大值与最小值的差值
	 * 实现原理：
	 * 1. 计算区间长度对应的最大2的幂次p
	 * 2. 找到两个覆盖整个查询区间的预处理区间：
	 *    - 第一个区间：从l开始，长度为2^p
	 *    - 第二个区间：以r结束，长度为2^p
	 * 3. 分别查询这两个区间的最大值和最小值
	 * 4. 返回最大值与最小值的差
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 */
	public static int query(int l, int r) {
		// 计算区间长度对应的最大2的幂次p
		// 例如：区间长度为5，则p=2（因为2^2=4是不超过5的最大2的幂）
		int p = log2[r - l + 1];
		
		// 找到两个覆盖整个查询区间的预处理区间
		// 区间1: [l, l + 2^p - 1]
		// 区间2: [r - 2^p + 1, r]
		// 这两个区间的并集正好覆盖整个查询区间[l, r]
		int a = Math.max(stmax[l][p], stmax[r - (1 << p) + 1][p]);
		int b = Math.min(stmin[l][p], stmin[r - (1 << p) + 1][p]);
		
		// 返回区间最大值与最小值的差
		return a - b;
	}

	/**
	 * 扩展问题1：区间最大值查询
	 * @param l 区间左边界（1-based）
	 * @param r 区间右边界（1-based）
	 * @return 区间内的最大值
	 * 实现原理：利用两个重叠的预处理区间覆盖查询区间，取最大值
	 * 时间复杂度：O(1)
	 */
	public static int queryMax(int l, int r) {
		// 计算区间长度对应的最大2的幂次
		int p = log2[r - l + 1];
		// 返回两个覆盖区间的最大值
		return Math.max(stmax[l][p], stmax[r - (1 << p) + 1][p]);
	}

	/**
	 * 扩展问题2：区间最小值查询
	 * @param l 区间左边界（1-based）
	 * @param r 区间右边界（1-based）
	 * @return 区间内的最小值
	 * 实现原理：利用两个重叠的预处理区间覆盖查询区间，取最小值
	 * 时间复杂度：O(1)
	 */
	public static int queryMin(int l, int r) {
		// 计算区间长度对应的最大2的幂次
		int p = log2[r - l + 1];
		// 返回两个覆盖区间的最小值
		return Math.min(stmin[l][p], stmin[r - (1 << p) + 1][p]);
	}

	/**
	 * 主函数 - 处理输入输出
	 * 对应题目：洛谷P2880
	 */
	public static void main(String[] args) throws IOException {
		// 使用高效的输入输出方式
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取数组长度n和查询次数m
		in.nextToken();
		int n = (int) in.nval;
		in.nextToken();
		int m = (int) in.nval;
		
		// 读取数组元素
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			arr[i] = (int) in.nval;
		}
		
		// 构建Sparse Table
		build(n);
		
		// 处理每个查询
		for (int i = 1, l, r; i <= m; i++) {
			in.nextToken();
			l = (int) in.nval;
			in.nextToken();
			r = (int) in.nval;
			// 输出查询结果
			out.println(query(l, r));
		}
		
		// 刷新输出缓冲区
		out.flush();
		// 关闭资源
		out.close();
		br.close();
	}

	/**
 * 【算法优化技巧】
 * 1. 预处理log数组避免重复计算，提高查询效率
 * 2. 使用位移运算提高效率（1 << p 代替 Math.pow(2, p)），避免浮点数运算
 * 3. 采用1-based索引简化边界处理，避免数组越界错误
 * 4. 使用高效的IO方式（BufferedReader、StreamTokenizer、PrintWriter）处理大规模数据
 * 5. 预处理时将log2数组与ST表初始化合并，减少遍历次数
 * 6. 对于多次查询的场景，预处理所有可能的查询结果以实现真正的O(1)查询
 * 7. 对于小规模数据，可以使用更简单的暴力方法，避免ST表的额外空间开销
 * 8. 对于不同类型的查询，考虑使用不同的合并操作（如max、min、gcd、sum等）
 * 
 * 【常见错误点】
 * 1. 数组越界：构建ST表时未正确检查区间边界，确保i + (1 << p) - 1 <= n
 * 2. 索引错误：混淆0-based和1-based索引，导致逻辑错误
 * 3. log数组初始化错误：log2[0]应初始化为-1，避免计算错误
 * 4. 数据范围不足：MAXN和LIMIT设置过小导致无法处理大规模数据
 * 5. 位运算错误：位移运算优先级问题，需使用括号确保计算顺序
 * 6. 内存溢出：对于大数据规模，需合理设置数组大小，避免OutOfMemoryError
 * 7. 边界条件处理：对于长度为0或1的区间处理不当
 * 8. 数据类型溢出：使用整型时未考虑最大值可能溢出
 * 
 * 【工程化考量】
 * 1. 异常处理：添加输入验证和边界检查，提高代码鲁棒性
 * 2. 内存优化：对于大规模数据，考虑使用动态数组或适当的内存池管理
 * 3. 线程安全：在多线程环境下，考虑添加同步机制或使用线程局部变量
 * 4. 单元测试：编写全面的测试用例，覆盖各种边界情况和异常输入
 * 5. 性能监控：添加性能监控代码，评估实际运行效率
 * 6. 可配置性：将MAXN、LIMIT等参数设为可配置项，提高代码灵活性
 * 7. 代码复用：将ST表封装为独立类，提供通用接口供其他模块使用
 * 8. 文档完善：添加详细的API文档和使用示例
 * 
 * 【实际应用注意事项】
 * 1. 数据规模评估：根据实际数据规模选择合适的MAXN和LIMIT值
 * 2. 查询频率分析：如果查询频率极高，考虑进一步优化预处理策略
 * 3. 数据更新频率：如果数据需要频繁更新，ST表可能不是最佳选择，考虑线段树等支持动态更新的数据结构
 * 4. 缓存友好性：注意数据访问模式，尽量提高缓存命中率
 * 5. 并行处理：对于大规模数据预处理，可以考虑并行化处理以提高效率
 * 6. 硬件特性：考虑目标硬件的缓存大小和架构特点，优化内存访问模式
 * 7. 数据压缩：对于某些场景，考虑使用压缩技术减少空间占用
 * 8. 算法选择：根据实际问题特性，合理选择ST表、线段树、前缀和等数据结构
 */
}