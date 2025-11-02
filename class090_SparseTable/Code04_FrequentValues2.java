package class117;

/**
 * 区间内出现次数最多的数的数量查询
 * <p>
 * 算法核心思想：分块 + Sparse Table
 * 对于有序数组，将相同元素划分成连续的块（bucket），对块内元素进行预处理：
 * 1. 每个位置i所属的桶号存储在bucket数组
 * 2. 每个桶的左右边界存储在left和right数组
 * 3. 使用Sparse Table预处理块之间的最大频率
 * 这样对于查询[l,r]，可以分为三种情况：
 * - l和r在同一个桶中：直接返回区间长度
 * - l和r在不同桶中：比较左边界桶部分频率、右边界桶部分频率、中间完整桶区间的最大频率
 * <p>
 * 核心原理：
 * 1. 利用数组有序性将相同元素划分为连续的块，每个块对应一个值
 * 2. 对于每个块预处理其长度（即元素出现次数）
 * 3. 使用Sparse Table数据结构预处理块间的最大值查询
 * 4. 查询时分为三种情况处理：全在同一块、跨越块边界、中间包含完整块
 * <p>
 * 位运算常用技巧：
 * 1. 计算log2值：log2[i] = log2[i >> 1] + 1，利用右移运算快速计算
 * 2. 计算2^p：使用位运算 (1 << p) 代替Math.pow(2, p)，提高效率
 * 3. 区间分割：利用位运算确定最佳分割点，确保覆盖整个查询区间
 * 4. 桶索引计算：利用整数除法将数组元素映射到对应的桶
 * 5. 判断奇偶性：使用 & 1 运算快速判断一个数的奇偶性
 * 6. 快速乘除2的幂：使用位运算 << 和 >> 代替乘除法，提高效率
 * 7. 边界处理：利用位运算避免浮点数计算，确保索引计算的精确性
 * <p>
 * 时间复杂度分析：
 * - 构建预处理：O(n)
 * - 构建Sparse Table：O(b log b)，其中b为不同元素的数量
 * - 每次查询：O(1)
 * <p>
 * 空间复杂度：O(n + b log b)
 * <p>
 * 应用场景：
 * - 有序数组中频繁进行区间内元素频率查询
 * - 数据分析中的频率统计
 * - 大数据处理中的快速聚合查询
 * - 数据库中的范围查询优化
 * - 网络流量分析中的频繁模式识别
 * - 文本处理中的词频统计和查询
 * - 传感器数据的异常检测和模式匹配
 * - 金融数据分析中的频繁交易模式识别
 * <p>
 * 相关题目：
 * 1. UVA 11235 - Frequent values (本题)
 * 2. Codeforces 475D - CGCDSSQ (类似区间查询问题，但针对GCD)
 * 3. LeetCode 930 - Binary Subarrays With Sum (滑动窗口解决频率问题)
 * 4. LeetCode 697 - Degree of an Array (相关频率统计)
 * 5. SPOJ FREQUENT (类似的区间频率查询问题)
 * 6. 洛谷 P1890 - gcd区间查询（类似的ST表应用）
 * 7. LeetCode 2080 - Range Frequency Queries
 * 8. Codeforces 1312E - Array Shrinking
 * 9. POJ 3264 - Balanced Lineup
 * 10. HDU 1045 - Fire Net
 * 11. 牛客网 NC13495 - 区间最大频率
 * 12. LeetCode 1395 - Count Number of Teams
 * 13. AtCoder ABC 123 - C Five Transportations
 * <p>
 * 题目来源：
 * - 洛谷查看: https://www.luogu.com.cn/problem/UVA11235
 * - Vjudge提交: https://vjudge.net/problem/UVA-11235
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code04_FrequentValues2 {

	/** 数组最大长度 */
	public static int MAXN = 100001;

	/** 最大幂次，2^17 > 100000，足够存储所有可能的log2值 */
	public static int LIMIT = 17;

	/** 原始数组，存储输入的有序序列 */
	public static int[] arr = new int[MAXN];

	/** 预处理的log2数组，用于快速计算区间长度对应的最大2的幂次 */
	public static int[] log2 = new int[MAXN];

	/** 每个位置所属的桶号，相同元素的连续序列属于同一个桶 */
	public static int[] bucket = new int[MAXN];

	/** 每个桶的左边界索引 */
	public static int[] left = new int[MAXN];

	/** 每个桶的右边界索引 */
	public static int[] right = new int[MAXN];

	/** Sparse Table结构，存储区间内的最大频率 */
	public static int[][] stmax = new int[MAXN][LIMIT];

	/**
	 * 构建分块结构和Sparse Table预处理
	 * @param n 数组长度
	 * 1. 将相同元素划分为连续的桶，记录每个位置所属的桶号
	 * 2. 记录每个桶的左右边界
	 * 3. 预处理log2数组和Sparse Table以支持区间最大频率查询
	 */
	public static void build(int n) {
		// 设置边界哨兵值，确保arr[1]必然是新元素的开始
		arr[0] = -23333333; // 超出题目给定的数值范围(-100000 ~ +100000)
		int cnt = 0; // 桶的计数器
		
		// 遍历数组，将相同元素连续序列划分为同一个桶
		for (int i = 1; i <= n; i++) {
			// 当元素值发生变化时，创建新桶
			if (arr[i - 1] != arr[i]) {
				right[cnt] = i - 1; // 结束上一个桶
				left[++cnt] = i;    // 开始新桶
			}
			bucket[i] = cnt; // 记录当前位置所属桶号
		}
		right[cnt] = n; // 设置最后一个桶的右边界
		
		// 预处理log2数组，用于快速计算区间长度对应的二进制位数
		log2[0] = -1; // 初始化哨兵位
		for (int i = 1; i <= cnt; i++) {
			log2[i] = log2[i >> 1] + 1;
			// 初始化Sparse Table的第0层，存储每个桶的元素数量
			stmax[i][0] = right[i] - left[i] + 1;
		}
		
		// 构建Sparse Table，支持区间最大频率O(1)查询
		for (int p = 1; p <= log2[cnt]; p++) {
			for (int i = 1; i + (1 << p) - 1 <= cnt; i++) {
				// 区间[i, i+2^p-1]的最大值等于两个子区间的最大值
				stmax[i][p] = Math.max(
						stmax[i][p - 1],
						stmax[i + (1 << (p - 1))][p - 1]
				);
			}
		}
	}

	/**
	 * 查询区间[l, r]中出现频率最高的元素的数量
	 * @param l 查询区间左边界（1-based索引）
	 * @param r 查询区间右边界（1-based索引）
	 * @return 区间内出现频率最高的元素的数量
	 * 处理逻辑：
	 * 1. 确保l <= r
	 * 2. 如果查询区间完全位于一个桶内，直接返回区间长度
	 * 3. 否则，分别计算：
	 *    a. 左边界所在桶内的有效部分
	 *    b. 右边界所在桶内的有效部分
	 *    c. 中间完整桶区间中的最大频率（通过Sparse Table查询）
	 *    d. 返回三者中的最大值
	 */
	public static int query(int l, int r) {
		// 确保l <= r
		if (l > r) {
			int tmp = l;
			l = r;
			r = tmp;
		}
		
		// 获取查询区间左右端点所在的桶号
		int lbucket = bucket[l];
		int rbucket = bucket[r];
		
		// 情况1：查询区间完全位于同一个桶内
		if (lbucket == rbucket) {
			return r - l + 1; // 直接返回区间长度
		}
		
		// 情况2：查询区间跨越多个桶
		// a: 左边界所在桶内的有效部分（从l到桶右边界）
		int a = right[lbucket] - l + 1;
		// b: 右边界所在桶内的有效部分（从桶左边界到r）
		int b = r - left[rbucket] + 1;
		// c: 中间完整桶区间的最大频率
		int c = 0;
		
		// 如果存在中间完整的桶区间，使用Sparse Table查询最大值
		if (lbucket + 1 < rbucket) {
			int from = lbucket + 1; // 起始桶号
			int to = rbucket - 1;   // 结束桶号
			int p = log2[to - from + 1]; // 确定使用的Sparse Table层数
			// 将区间[from, to]分成两个等长的子区间，取最大值
			c = Math.max(
					stmax[from][p],
					stmax[to - (1 << p) + 1][p]
			);
		}
		
		// 返回三种情况中的最大值
		return Math.max(Math.max(a, b), c);
	}

	/**
	 * 主方法 - 处理输入输出和执行查询
	 * 使用BufferedReader和StreamTokenizer提高输入效率，PrintWriter提高输出效率
	 */
	public static void main(String[] args) throws IOException {
		// 使用缓冲流提高输入效率
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		// 使用StreamTokenizer解析输入数据
		StreamTokenizer in = new StreamTokenizer(br);
		// 使用PrintWriter提高输出效率
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 循环处理多组测试用例，直到文件结束
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			// 读取数组长度
			int n = (int) in.nval;
			// n=0表示结束
			if (n == 0) {
				break;
			}
			// 读取查询次数
			in.nextToken();
			int m = (int) in.nval;
			
			// 读取数组元素（1-based索引）
			for (int i = 1; i <= n; i++) {
				in.nextToken();
				arr[i] = (int) in.nval;
			}
			
			// 构建分块结构和Sparse Table
			build(n);
			
			// 处理每个查询
			for (int i = 1, l, r; i <= m; i++) {
				in.nextToken();
				l = (int) in.nval; // 查询左边界
				in.nextToken();
				r = (int) in.nval; // 查询右边界
				// 执行查询并输出结果
				out.println(query(l, r));
			}
		}
		
		// 刷新输出并关闭资源
		out.flush();
		out.close();
		br.close();
	}

	/*
	 * 算法优化技巧：
	 * 1. 利用数组有序性：将相同元素划分为连续块，避免重复计算
	 * 2. 预处理log2数组：使用位运算高效计算log2值
	 * 3. Sparse Table优化：将区间查询复杂度从O(n)降为O(1)
	 * 4. 分块处理：查询时分别处理跨越桶的情况，保证常数时间复杂度
	 * 5. 输入输出优化：使用BufferedReader和PrintWriter提高处理大输入的效率
	 * 6. 哨兵值优化：设置arr[0]为特殊值，简化桶划分逻辑
	 * 7. 预处理桶边界：预先计算每个桶的左右边界，避免重复计算
	 * 8. 批量输出：将多个查询结果收集后一次性输出，减少I/O操作
	 * 9. 惰性计算：只在需要时计算log2值和构建ST表
	 * 10. 内存复用：在多次查询之间复用预处理的数组结构
	 *
	 * 常见错误点：
	 * 1. 索引越界：桶数组和ST表的索引范围容易出错，需要仔细检查边界条件
	 * 2. 桶划分错误：在遍历数组创建桶时，容易漏掉最后一个桶的右边界设置
	 * 3. 查询参数检查：需要处理l > r的特殊情况
	 * 4. 数组初始化：确保arr[0]设置为一个不会出现在输入中的值
	 * 5. 内存使用：对于大规模数据，需要确保MAXN足够大但不过度消耗内存
	 * 6. 桶计数错误：cnt变量的初始值和递增时机容易出错
	 * 7. ST表构建错误：第二层循环的边界条件计算容易出错
	 * 8. 查询逻辑错误：处理中间完整块的条件判断容易遗漏
	 * 9. 数组索引偏移：1-based和0-based索引混用导致的错误
	 * 10. 数据类型溢出：在处理大规模数据时可能发生整数溢出
	 *
	 * 工程化考量：
	 * 1. 异常处理：添加参数验证和异常捕获以提高代码健壮性
	 * 2. 可扩展性：可以将算法封装为独立的类，便于复用和集成到其他项目
	 * 3. 性能优化：在处理超大数组时，考虑使用动态数组或分批处理
	 * 4. 代码可读性：使用有意义的变量名和详细注释，提高可维护性
	 * 5. 内存管理：对于频繁调用的场景，考虑对象池模式避免频繁的内存分配
	 * 6. 线程安全：在多线程环境中添加同步机制确保数据一致性
	 * 7. 测试覆盖：编写全面的单元测试和边界测试用例
	 * 8. 文档完善：提供详细的API文档和使用示例
	 * 9. 配置灵活性：允许用户自定义MAXN等参数以适应不同场景
	 * 10. 兼容性处理：确保代码在不同编译器和运行环境下的兼容性
	 *
	 * 实际应用注意事项：
	 * 1. 数据规模评估：根据实际数据量选择合适的预处理策略和内存分配
	 * 2. 输入数据验证：确保输入数组确实是有序的，否则需要先排序
	 * 3. 边界情况处理：特别注意空数组、单元素数组等特殊情况
	 * 4. 性能监控：在生产环境中添加性能监控点，及时发现瓶颈
	 * 5. 资源限制：考虑内存限制，对于极大数组可能需要使用外部存储
	 * 6. 查询频率优化：对于频繁查询的场景，考虑缓存常用查询结果
	 * 7. 动态数据处理：如果数据需要动态更新，考虑使用其他数据结构如线段树
	 * 8. 并行处理：对于大规模数据，可以考虑并行构建预处理结构
	 * 9. 精度要求：确保使用足够精度的数据类型避免计算错误
	 * 10. 错误恢复：添加故障恢复机制，确保在异常情况下能够优雅降级
	 */

	/*
	【C++版本代码】
	#include <iostream>
	#include <vector>
	#include <algorithm>
	using namespace std;

	const int MAXN = 100001;
	const int LIMIT = 17;

	int arr[MAXN];        // 原始数组
	int log2_[MAXN];      // log2数组，注意与C++标准库中的log2函数冲突，使用下划线区分
	int bucket[MAXN];     // 每个位置所属的桶号
	int left_[MAXN];      // 每个桶的左边界
	int right_[MAXN];     // 每个桶的右边界
	int stmax[MAXN][LIMIT]; // Sparse Table

	/**
	 * 基于分块思想和Sparse Table的有序数组区间频率查询类
	 * 适用于处理有序数组中区间内出现次数最多的元素的数量查询
	 * 时间复杂度：预处理O(n)，查询O(1)
	 * 空间复杂度：O(n log n)
	 */

	/**
	 * 构建分块结构和Sparse Table
	 * 预处理桶的边界、log2数组和Sparse Table数据结构
	 * @param n 数组长度
	 * @time O(n)
	 * @space O(n log n)
	 */
	void build(int n) {
	    // 设置边界哨兵
	    arr[0] = -23333333;
	    int cnt = 0;
	    
	    // 分桶处理
	    for (int i = 1; i <= n; ++i) {
	        if (arr[i - 1] != arr[i]) {
	            right_[cnt] = i - 1;
	            left_[++cnt] = i;
	        }
	        bucket[i] = cnt;
	    }
	    right_[cnt] = n;
	    
	    // 预处理log2数组
	    log2_[0] = -1;
	    for (int i = 1; i <= cnt; ++i) {
	        log2_[i] = log2_[i >> 1] + 1;
	        stmax[i][0] = right_[i] - left_[i] + 1;
	    }
	    
	    // 构建Sparse Table
	    for (int p = 1; p <= log2_[cnt]; ++p) {
	        for (int i = 1; i + (1 << p) - 1 <= cnt; ++i) {
	            stmax[i][p] = max(stmax[i][p - 1], stmax[i + (1 << (p - 1))][p - 1]);
	        }
	    }
	}

	/**
	 * 查询区间[l,r]中出现频率最高的元素的数量
	 * 处理三种情况：同一块内、跨块边界、包含完整块
	 * @param l 查询左边界（1-based索引）
	 * @param r 查询右边界（1-based索引）
	 * @return 区间内出现次数最多的元素的数量
	 * @time O(1)
	 * @note 自动处理l > r的情况
	 */
	int query(int l, int r) {
	    if (l > r) swap(l, r);
	    
	    int lbucket = bucket[l];
	    int rbucket = bucket[r];
	    
	    if (lbucket == rbucket) {
	        return r - l + 1;
	    }
	    
	    int a = right_[lbucket] - l + 1;
	    int b = r - left_[rbucket] + 1;
	    int c = 0;
	    
	    if (lbucket + 1 < rbucket) {
	        int from = lbucket + 1;
	        int to = rbucket - 1;
	        int p = log2_[to - from + 1];
	        c = max(stmax[from][p], stmax[to - (1 << p) + 1][p]);
	    }
	    
	    return max(max(a, b), c);
	}

	int main() {
	    // 关闭同步和取消绑定，加速输入输出
	    ios::sync_with_stdio(false);
	    cin.tie(0);
	    
	    int n;
	    // 循环处理多组测试用例，直到n为0
	    while (cin >> n && n != 0) {
	        int m;
	        cin >> m;
	        
	        for (int i = 1; i <= n; ++i) {
	            cin >> arr[i];
	        }
	        
	        build(n);
	        
	        for (int i = 0; i < m; ++i) {
	            int l, r;
	            cin >> l >> r;
	            cout << query(l, r) << '\n';
	        }
	    }
	    
	    return 0;
	}
	*/

	/*
	【Python版本代码】
	import sys

	class FrequentValuesSolver:
	    def __init__(self):
	        # 设置常量
	        self.MAXN = 100001
	        self.LIMIT = 17
	    
	    def build(self, arr, n):
	        """
	        构建分块结构和Sparse Table
        
	        Args:
	            arr: 原始数组（1-based索引）
	            n: 数组长度
        
	        Returns:
	            预处理后的数据结构
	        """
	        # 创建副本避免修改原数组
	        arr_copy = [-23333333] * (n + 1)
	        for i in range(1, n + 1):
	            arr_copy[i] = arr[i]
	        
	        # 初始化数据结构
	        log2_ = [0] * (n + 1)
	        bucket = [0] * (n + 1)
	        left_ = [0] * (n + 1)  # 足够大的空间存储桶的边界
	        right_ = [0] * (n + 1)
	        
	        # 分桶处理
	        cnt = 0
	        for i in range(1, n + 1):
	            if arr_copy[i - 1] != arr_copy[i]:
	                right_[cnt] = i - 1
	                cnt += 1
	                left_[cnt] = i
	            bucket[i] = cnt
	        right_[cnt] = n
	        
	        # 预处理log2数组和构建Sparse Table
	        # 计算实际需要的桶数量
	        bucket_count = cnt
	        log2_[0] = -1
	        for i in range(1, bucket_count + 1):
	            log2_[i] = log2_[i // 2] + 1
	        
	        # 创建Sparse Table
	        max_level = 0
	        if bucket_count > 0:
	            max_level = log2_[bucket_count] + 1
	        stmax = [[0] * max_level for _ in range(bucket_count + 1)]
	        
	        # 初始化第0层
	        for i in range(1, bucket_count + 1):
	            stmax[i][0] = right_[i] - left_[i] + 1
	        
	        # 构建上层
	        for p in range(1, max_level):
	            for i in range(1, bucket_count - (1 << p) + 2):
	                stmax[i][p] = max(
	                    stmax[i][p-1], 
	                    stmax[i + (1 << (p-1))][p-1]
	                )
	        
	        return bucket, left_, right_, log2_, stmax, bucket_count
    
	    def query(self, l, r, bucket, left_, right_, log2_, stmax, bucket_count):
	        """
	        查询区间[l,r]中出现频率最高的元素的数量
        
	        Args:
	            l: 查询左边界（1-based）
	            r: 查询右边界（1-based）
	            bucket: 桶号数组
	            left_: 桶左边界数组
	            right_: 桶右边界数组
	            log2_: log2预处理数组
	            stmax: Sparse Table
	            bucket_count: 桶的数量
        
	        Returns:
	            出现次数最多的元素的数量
	        """
	        if l > r:
	            l, r = r, l
	        
	        lbucket = bucket[l]
	        rbucket = bucket[r]
	        
	        if lbucket == rbucket:
	            return r - l + 1
	        
	        a = right_[lbucket] - l + 1
	        b = r - left_[rbucket] + 1
	        c = 0
	        
	        if lbucket + 1 < rbucket:
	            from_bucket = lbucket + 1
	            to_bucket = rbucket - 1
	            p = log2_[to_bucket - from_bucket + 1]
	            c = max(
	                stmax[from_bucket][p], 
	                stmax[to_bucket - (1 << p) + 1][p]
	            )
	        
	        return max(max(a, b), c)

	def test_frequent_values():
	    """
	    测试FrequentValuesSolver类的正确性
	    包含多种边界情况和典型场景的测试用例
	    """
	    solver = FrequentValuesSolver()
	    
	    # 测试用例1：基本有序数组
	    arr1 = [0, 1, 1, 1, 2, 2, 3]
	    n1 = 6
	    bucket1, left1, right1, log2_1, stmax1, bucket_count1 = solver.build(arr1, n1)
	    assert solver.query(1, 3, bucket1, left1, right1, log2_1, stmax1, bucket_count1) == 3
	    assert solver.query(4, 6, bucket1, left1, right1, log2_1, stmax1, bucket_count1) == 2
	    assert solver.query(1, 6, bucket1, left1, right1, log2_1, stmax1, bucket_count1) == 3
	    print("测试用例1通过")
	    
	    # 测试用例2：所有元素相同
	    arr2 = [0, 5, 5, 5, 5, 5, 5]
	    n2 = 6
	    bucket2, left2, right2, log2_2, stmax2, bucket_count2 = solver.build(arr2, n2)
	    assert solver.query(1, 6, bucket2, left2, right2, log2_2, stmax2, bucket_count2) == 6
	    print("测试用例2通过")
	    
	    # 测试用例3：每个元素都不同
	    arr3 = [0, 1, 2, 3, 4, 5, 6]
	    n3 = 6
	    bucket3, left3, right3, log2_3, stmax3, bucket_count3 = solver.build(arr3, n3)
	    assert solver.query(1, 6, bucket3, left3, right3, log2_3, stmax3, bucket_count3) == 1
	    print("测试用例3通过")
	    
	    # 测试用例4：跨多个块的查询
	    arr4 = [0, 1, 1, 2, 2, 2, 3, 3, 4]
	    n4 = 8
	    bucket4, left4, right4, log2_4, stmax4, bucket_count4 = solver.build(arr4, n4)
	    assert solver.query(1, 8, bucket4, left4, right4, log2_4, stmax4, bucket_count4) == 3
	    print("测试用例4通过")
	    print("所有测试用例通过！")

	def main():
	    # 优化输入
	    input = sys.stdin.read().split()
	    ptr = 0
	    solver = FrequentValuesSolver()
	    
	    while True:
	        if ptr >= len(input):
	            break
	        n = int(input[ptr])
	        ptr += 1
	        if n == 0:
	            break
	        m = int(input[ptr])
	        ptr += 1
	        
	        # 读取数组（1-based索引）
	        arr = [0] * (n + 1)
	        for i in range(1, n + 1):
	            arr[i] = int(input[ptr])
	            ptr += 1
	        
	        # 构建预处理结构
	        bucket, left_, right_, log2_, stmax, bucket_count = solver.build(arr, n)
	        
	        # 处理查询
	        results = []
	        for _ in range(m):
	            l = int(input[ptr])
	            ptr += 1
	            r = int(input[ptr])
	            ptr += 1
	            res = solver.query(l, r, bucket, left_, right_, log2_, stmax, bucket_count)
	            results.append(str(res))
	        
	        # 批量输出
	        print('\n'.join(results))

	# 如果作为主程序运行，执行测试
	if __name__ == "__main__":
	    # 可以选择运行测试或处理输入
	    # test_frequent_values()  # 取消注释以运行测试
	    main()
	*/
}
