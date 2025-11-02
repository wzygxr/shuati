package class117;

/**
 * 有序数组区间内出现次数最多的数的个数
 * 结合分块思想和Sparse Table（稀疏表）
 * 
 * 【算法核心思想】
 * 利用数组有序的特性，将相同元素的连续区间作为一个块，结合Sparse Table实现高效查询
 * 该算法是分块思想与Sparse Table结合的典型应用
 * 
 * 【核心原理】
 * - 有序数组的特性：相同元素必然连续，这使得分块处理变得高效
 * - 分块处理：将相同元素的连续区间视为一个块，每个块内的元素完全相同
 * - Sparse Table：用于快速查询任意区间内的块大小最大值
 * - 查询分解：将任意查询区间分解为三部分：左不完整块、中间完整块、右不完整块
 * 
 * 【位运算常用技巧】
 * 1. 位运算求对数：log2[i] = log2[i >> 1] + 1
 * 2. 位移操作计算区间长度：1 << p 表示2^p
 * 3. 位运算优化区间查询：通过位移计算区间的最大覆盖长度
 * 4. 快速判断元素变化：通过比较相邻元素的值检测块边界
 * 5. 位移操作避免乘法：用位移代替乘除操作提高效率
 * 6. 位掩码处理边界条件：利用位运算处理边界情况
 * 7. 二分思想结合位运算：通过位运算实现高效的区间分割
 * 
 * 【时间复杂度分析】
 * - 构建分块结构：O(n) - 只需一次线性扫描
 * - 构建Sparse Table：O(n log n) - 需要填充log n层的表
 * - 单次查询：O(1) - 常数时间查询，无论数据规模多大
 * - 总时间复杂度：预处理O(n log n)，查询O(1)
 * - 对于m次查询，总时间复杂度为O(n log n + m)
 * 
 * 【空间复杂度分析】
 * - 分块相关数组（bucket, left, right）：O(n)
 * - Sparse Table数组：O(n log n)
 * - 预计算log2数组：O(n)
 * - 总空间复杂度：O(n log n)
 * 
 * 【应用场景】
 * 1. 有序数组的频率统计问题
 * 2. 区间模式查询（查询区间内出现次数最多的元素）
 * 3. 大规模数据的快速频率分析
 * 4. 数据挖掘中的模式识别
 * 5. 统计分析中的频率分布查询
 * 6. 数据库系统中的区间统计优化
 * 7. 金融数据分析中的频率查询
 * 8. 信号处理中的频率分析
 * 
 * 【相关题目】
 * 1. POJ 3368 Frequent values
 * 2. LeetCode 1838. Frequency of the Most Frequent Element
 * 3. Codeforces 868E. Policeman and a Tree
 * 4. HDU 6440 Dream
 * 5. SPOJ FREQUENT - Frequent values
 * 6. LeetCode 2009. Minimum Number of Operations to Make Array Continuous
 * 7. LeetCode 2405. Optimal Partition of String
 * 8. Codeforces 1263E. Editor
 * 9. AtCoder ABC127E. Cell Distance
 * 10. Google Kickstart Round G 2020 Problem C
 * 11. Topcoder SRM 790 Div1 Easy
 * 12. Facebook Hacker Cup 2021 Problem B1
 * 13. CodeChef SEP20A Problem CHFNSWAP
 * 
 * 【算法设计思路】
 * 1. 分块处理：将相同元素的连续区间视为一个块
 * 2. 预处理：记录每个元素所属的块号、每个块的左右边界、每个块的元素个数
 * 3. 构建Sparse Table：用于快速查询中间完整块中的最大频率
 * 4. 查询优化：区间查询拆分为左边界不完整块、中间完整块、右边界不完整块三部分处理
 */

// 出现次数最多的数有几个
// 给定一个长度为n的数组arr，该数组一定是有序的
// 一共有m次查询，每次查询arr[l~r]上出现次数最多的数有几个
// 对数器验证

import java.util.Arrays;
import java.util.HashMap;

public class Code04_FrequentValues1 {

	public static int MAXN = 100001; // 数组最大长度

	public static int LIMIT = 17;   // 2^17足够覆盖大部分情况

	public static int[] arr = new int[MAXN];      // 原始有序数组

	public static int[] log2 = new int[MAXN];     // 预计算的对数数组，用于快速查询

	public static int[] bucket = new int[MAXN];   // 记录每个位置属于哪个块

	public static int[] left = new int[MAXN];     // 记录每个块的左边界

	public static int[] right = new int[MAXN];    // 记录每个块的右边界

	public static int[][] stmax = new int[MAXN][LIMIT]; // Sparse Table，存储块的大小

	/**
	 * 构建分块结构和Sparse Table
	 * 
	 * @param n 数组长度
	 */
	public static void build(int n) {
		// 边界处理：设置arr[0]为一个不可能出现在数组中的值，用于判断元素变化
		// 题目给定的数值范围-100000 ~ +100000，所以选择一个更小的值
		arr[0] = -23333333;
		
		// 分块处理：将相同元素的连续区间作为一个块
		int cnt = 0; // 块的计数器
		for (int i = 1; i <= n; i++) {
			// 当元素变化时，说明进入了一个新的块
			if (arr[i - 1] != arr[i]) {
				// 记录上一个块的右边界
				right[cnt] = i - 1;
				// 开启新块并记录左边界
				left[++cnt] = i;
			}
			// 记录当前位置所属的块号
			bucket[i] = cnt;
		}
		// 记录最后一个块的右边界
		right[cnt] = n;
		
		// 预处理log2数组，用于Sparse Table查询
		log2[0] = -1; // 0的对数定义为-1（方便计算）
		for (int i = 1; i <= cnt; i++) {
			log2[i] = log2[i >> 1] + 1;
			// Sparse Table的第0层存储每个块的大小
			stmax[i][0] = right[i] - left[i] + 1;
		}
		
		// 构建Sparse Table的其余层
		for (int p = 1; p <= log2[cnt]; p++) {
			for (int i = 1; i + (1 << p) - 1 <= cnt; i++) {
				// 状态转移：区间[i, i+2^p-1]的最大值 = max(区间[i, i+2^(p-1)-1], 区间[i+2^(p-1), i+2^p-1])
				stmax[i][p] = Math.max(stmax[i][p - 1], stmax[i + (1 << (p - 1))][p - 1]);
			}
		}
	}

	/**
	 * 查询区间[l, r]中出现次数最多的数的个数
	 * 
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @return 区间内出现次数最多的数的个数
	 */
	public static int query(int l, int r) {
		// 处理l > r的情况，交换端点
		if (l > r) {
			int tmp = l;
			l = r;
			r = tmp;
		}
		
		// 获取左右端点所属的块号
		int lbucket = bucket[l];
		int rbucket = bucket[r];
		
		// 情况1：左右端点在同一个块中，直接返回区间长度
		if (lbucket == rbucket) {
			return r - l + 1;
		}
		
		// 情况2：左右端点在不同块中，需要分三部分处理
		// a: 左端点所在块中属于查询区间的元素个数
		// b: 右端点所在块中属于查询区间的元素个数
		// c: 中间完整块中的最大块大小
		int a = right[lbucket] - l + 1;  // 左不完整块的元素个数
		int b = r - left[rbucket] + 1;   // 右不完整块的元素个数
		int c = 0;  // 中间完整块的最大频率
		
		// 如果存在中间完整块，使用Sparse Table查询最大频率
		if (lbucket + 1 < rbucket) {
			int from = lbucket + 1;  // 第一个完整块的块号
			int to = rbucket - 1;    // 最后一个完整块的块号
			int p = log2[to - from + 1];  // 计算查询的区间长度对应的log值
			// 区间最大值查询：max(左边2^p个元素, 右边2^p个元素)
			c = Math.max(stmax[from][p], stmax[to - (1 << p) + 1][p]);
		}
		
		// 返回三部分中的最大值
		return Math.max(Math.max(a, b), c);
	}
	
	/**
	 * 【算法优化技巧】
	 * 1. 利用数组有序性：将相同元素的连续区间视为一个块，减少计算量
	 * 2. 分块查询策略：将查询区间拆分为三个部分，分别处理
	 * 3. Sparse Table预处理：支持O(1)时间查询任意区间内的最大值
	 * 4. log2数组预处理：避免重复计算log2值，提高查询效率
	 * 5. 哨兵值技术：在数组开头设置哨兵值，简化块边界检测
	 * 6. 位运算优化：使用位移操作代替乘除运算，提高执行效率
	 * 7. 区间分解优化：将复杂区间查询分解为简单子问题
	 * 8. 预处理与查询分离：将计算密集型操作放在预处理阶段
	 * 9. 缓存友好的数据结构：优化数据访问模式，提高缓存命中率
	 * 10. 边界条件优化：处理特殊情况（如l > r）以提高鲁棒性
	 * 
	 * 【常见错误点】
	 * 1. 数组索引越界：注意分块处理时的边界条件
	 * 2. 块号计算错误：确保每个元素正确分配到对应的块中
	 * 3. Sparse Table构建错误：注意状态转移方程的正确性
	 * 4. 查询逻辑错误：处理好左右端点在同一块和不同块的情况
	 * 5. 哨兵值设置不当：导致块边界检测失败
	 * 6. 1-based vs 0-based索引混用：注意数组索引的一致性
	 * 7. 中间块范围计算错误：确保from和to的正确性
	 * 8. log2数组计算错误：可能导致Sparse Table查询出错
	 * 9. 数组未正确排序：该算法要求输入数组必须有序
	 * 10. 初始化顺序错误：分块处理必须在Sparse Table构建之前完成
	 * 
	 * 【工程化考量】
	 * 1. 内存优化：对于大规模数据，可以调整MAXN和LIMIT的值
	 * 2. 可扩展性：可以封装为通用类，支持不同类型的元素
	 * 3. 异常处理：添加输入验证，处理非法查询
	 * 4. 性能优化：对于频繁查询的场景，此算法比暴力方法效率高得多
	 * 5. 代码可读性：添加详细注释，提高可维护性
	 * 6. 测试覆盖：使用对数器验证正确性
	 * 7. 边界情况处理：确保算法在各种边界情况下都能正确工作
	 * 8. 多语言实现：提供不同编程语言的实现，满足不同需求
	 * 9. 性能基准测试：对比不同算法实现的性能差异
	 * 10. 文档完善：提供详细的使用说明和示例
	 * 
	 * 【实际应用注意事项】
	 * 1. 输入验证：确保输入数组已经排序，否则算法将无法正确工作
	 * 2. 数据规模评估：对于小型数组，暴力方法可能更高效
	 * 3. 内存限制：对于非常大的数组，需要评估内存使用情况
	 * 4. 查询频率：该算法适合大量查询的场景，预处理成本可以被摊销
	 * 5. 实时性要求：对于实时系统，需要评估预处理时间是否可接受
	 * 6. 数据更新：该算法不支持动态数据更新，适合静态数据集
	 * 7. 精度问题：在使用浮点数时需要注意精度问题
	 * 8. 多线程环境：需要考虑线程安全问题
	 * 9. 跨平台兼容性：确保代码在不同平台上都能正确运行
	 * 10. 与其他数据结构对比：根据具体场景选择最合适的数据结构
	 */
	
	/*
	// C++版本实现
	/**
	 * @file FrequentValues.cpp
	 * @brief 有序数组区间频率查询算法实现 (C++)
	 * 
	 * 该实现结合分块思想和Sparse Table，解决有序数组区间内出现次数最多的数的个数问题
	 * 时间复杂度：预处理O(n log n)，单次查询O(1)
	 * 空间复杂度：O(n log n)
	 */
	#include <iostream>
	#include <vector>
	#include <algorithm>
	#include <climits>
	using namespace std;
	
	const int MAXN = 100001;   // 数组最大长度
	const int LIMIT = 17;      // 2^17足够覆盖大部分情况
	
	int arr[MAXN];             // 原始有序数组
	int log2_[MAXN];           // 预计算的对数数组（注意：命名为log2_以避免与标准库冲突）
	int bucket[MAXN];          // 记录每个位置属于哪个块
	int left_[MAXN];           // 记录每个块的左边界
	int right_[MAXN];          // 记录每个块的右边界
	int stmax[MAXN][LIMIT];    // Sparse Table，存储块的大小
	
	/**
	 * @brief 构建分块结构和Sparse Table
	 * @param n 数组长度
	 * @note 时间复杂度：O(n log n)
	 */
	void build(int n) {
		// 设置边界哨兵值，用于检测元素变化
		arr[0] = INT_MIN;
		int cnt = 0;  // 块计数器
		
		// 分块处理：将相同元素的连续区间视为一个块
		for (int i = 1; i <= n; ++i) {
			// 当元素变化时，说明进入了一个新的块
			if (arr[i-1] != arr[i]) {
				right_[cnt] = i - 1;  // 记录上一个块的右边界
				left_[++cnt] = i;     // 开启新块并记录左边界
			}
			bucket[i] = cnt;  // 记录当前位置所属的块号
		}
		right_[cnt] = n;  // 记录最后一个块的右边界
		
		// 预处理log2数组，用于Sparse Table查询
		log2_[0] = -1;  // 0的对数定义为-1（方便计算）
		for (int i = 1; i <= cnt; ++i) {
			log2_[i] = log2_[i >> 1] + 1;  // 位运算计算log2值
			stmax[i][0] = right_[i] - left_[i] + 1;  // 初始化Sparse Table第0层
		}
		
		// 构建Sparse Table的其余层
		for (int p = 1; p <= log2_[cnt]; ++p) {
			for (int i = 1; i + (1 << p) - 1 <= cnt; ++i) {
				// 状态转移：区间最大值 = max(左半区间最大值, 右半区间最大值)
				stmax[i][p] = max(stmax[i][p-1], stmax[i + (1 << (p-1))][p-1]);
			}
		}
	}
	
	/**
	 * @brief 查询区间[l, r]中出现次数最多的数的个数
	 * @param l 区间左端点（1-based）
	 * @param r 区间右端点（1-based）
	 * @return 区间内出现次数最多的数的个数
	 * @note 时间复杂度：O(1)
	 */
	int query(int l, int r) {
		// 处理l > r的情况
		if (l > r) {
			swap(l, r);
		}
		
		// 获取左右端点所属的块号
		int lbucket = bucket[l];
		int rbucket = bucket[r];
		
		// 情况1：左右端点在同一个块中，直接返回区间长度
		if (lbucket == rbucket) {
			return r - l + 1;
		}
		
		// 情况2：左右端点在不同块中，需要分三部分处理
		int a = right_[lbucket] - l + 1;  // 左不完整块的元素个数
		int b = r - left_[rbucket] + 1;   // 右不完整块的元素个数
		int c = 0;  // 中间完整块的最大频率
		
		// 如果存在中间完整块，使用Sparse Table查询最大频率
		if (lbucket + 1 < rbucket) {
			int from = lbucket + 1;  // 第一个完整块的块号
			int to = rbucket - 1;    // 最后一个完整块的块号
			int p = log2_[to - from + 1];  // 计算查询的区间长度对应的log值
			// 区间最大值查询：max(左边2^p个元素, 右边2^p个元素)
			c = max(stmax[from][p], stmax[to - (1 << p) + 1][p]);
		}
		
		// 返回三部分中的最大值
		return max(max(a, b), c);
	}
	
	/**
	 * @brief 主函数，处理输入输出
	 * @return 0表示程序正常结束
	 * @note 优化输入输出速度，使用ios::sync_with_stdio(false)和cin.tie(0)
	 */
	int main() {
		// 优化输入输出速度
		ios::sync_with_stdio(false);
		cin.tie(0);
		
		int n, q;
		// 读取输入直到n为0
		while (cin >> n && n != 0) {
			cin >> q;
			// 读取数组元素
			for (int i = 1; i <= n; ++i) {
				cin >> arr[i];
			}
			// 构建数据结构
			build(n);
			// 处理查询
			while (q--) {
				int l, r;
				cin >> l >> r;
				// 输出查询结果，使用\n代替endl以提高输出效率
				cout << query(l, r) << '\n';
			}
		}
		return 0;
	}
	*/
	
	/*
	# Python版本实现
	import sys
	import math
	from typing import List, Tuple
	
	MAXN = 100001
	LIMIT = 17
	
	class FrequentValuesRMQ:
	    """
	    有序数组区间频率查询类 (Range Maximum Query for Frequent Values)
	    
	    该类实现了基于分块思想和Sparse Table的高效区间频率查询算法
	    适用于处理已排序数组的频繁区间查询问题
	    
	    时间复杂度：
	    - 构建：O(n log n)
	    - 查询：O(1)
	    
	    空间复杂度：O(n log n)
	    """
	    
	    def __init__(self):
	        """初始化数据结构"""
	        self.arr = [0] * MAXN              # 原始有序数组
	        self.log2 = [0] * MAXN             # 预计算的对数数组
	        self.bucket = [0] * MAXN           # 记录每个位置属于哪个块
	        self.left = [0] * MAXN             # 记录每个块的左边界
	        self.right = [0] * MAXN            # 记录每个块的右边界
	        self.stmax = [[0] * LIMIT for _ in range(MAXN)]  # Sparse Table
	    
	    def build(self, n: int) -> None:
	        """
	        构建分块结构和Sparse Table
	        
	        Args:
	            n: 数组长度
	        """
	        # 设置边界哨兵值
	        self.arr[0] = -float('inf')
	        cnt = 0
	        
	        # 分块处理：将相同元素的连续区间视为一个块
	        for i in range(1, n + 1):
	            if self.arr[i-1] != self.arr[i]:
	                self.right[cnt] = i - 1
	                cnt += 1
	                self.left[cnt] = i
	            self.bucket[i] = cnt
	        self.right[cnt] = n
	        
	        # 预处理log2数组
	        self.log2[0] = -1
	        for i in range(1, cnt + 1):
	            self.log2[i] = self.log2[i >> 1] + 1
	            self.stmax[i][0] = self.right[i] - self.left[i] + 1
	        
	        # 构建Sparse Table
	        for p in range(1, self.log2[cnt] + 1):
	            for i in range(1, cnt - (1 << p) + 2):
	                self.stmax[i][p] = max(
	                    self.stmax[i][p-1], 
	                    self.stmax[i + (1 << (p-1))][p-1]
	                )
    
	    def query(self, l: int, r: int) -> int:
	        """
	        查询区间[l, r]中出现次数最多的数的个数
	        
	        Args:
	            l: 区间左端点（1-based）
	            r: 区间右端点（1-based）
	            
	        Returns:
	            int: 区间内出现次数最多的数的个数
	        """
	        # 处理l > r的情况
	        if l > r:
	            l, r = r, l
	        
	        # 获取左右端点所属的块号
	        lbucket = self.bucket[l]
	        rbucket = self.bucket[r]
	        
	        # 情况1：左右端点在同一个块中
	        if lbucket == rbucket:
	            return r - l + 1
	        
	        # 情况2：左右端点在不同块中
	        a = self.right[lbucket] - l + 1  # 左不完整块的元素个数
	        b = r - self.left[rbucket] + 1   # 右不完整块的元素个数
	        c = 0  # 中间完整块的最大频率
	        
	        # 查询中间完整块中的最大频率
	        if lbucket + 1 < rbucket:
	            from_ = lbucket + 1
	            to_ = rbucket - 1
	            p = self.log2[to_ - from_ + 1]
	            c = max(
	                self.stmax[from_][p], 
	                self.stmax[to_ - (1 << p) + 1][p]
	            )
	        
	        # 返回三部分中的最大值
	        return max(max(a, b), c)
	    
	    def set_array(self, data: List[int], n: int) -> None:
	        """
	        设置要处理的数组
	        
	        Args:
	            data: 输入数据列表
	            n: 数组长度
	        """
	        for i in range(1, n + 1):
	            self.arr[i] = data[i-1]  # 注意索引转换（Python是0-based）
	

def test_sparse_table():
    """测试FrequentValuesRMQ类的功能"""
    print("开始测试FrequentValuesRMQ...")
    
    # 测试用例1：基本测试
    test1 = FrequentValuesRMQ()
    data1 = [1, 1, 2, 2, 2, 3, 3]
    test1.set_array(data1, len(data1))
    test1.build(len(data1))
    
    assert test1.query(1, 7) == 3  # 整个数组中2出现3次
    assert test1.query(1, 2) == 2  # [1,1]中1出现2次
    assert test1.query(3, 5) == 3  # [2,2,2]中2出现3次
    assert test1.query(2, 6) == 3  # [1,2,2,2,3]中2出现3次
    print("测试用例1通过!")
    
    # 测试用例2：边界情况测试
    test2 = FrequentValuesRMQ()
    data2 = [5]
    test2.set_array(data2, len(data2))
    test2.build(len(data2))
    
    assert test2.query(1, 1) == 1  # 单元素数组
    print("测试用例2通过!")
    
    # 测试用例3：l > r的情况
    test3 = FrequentValuesRMQ()
    data3 = [1, 1, 1, 2, 2, 3, 3, 3, 3]
    test3.set_array(data3, len(data3))
    test3.build(len(data3))
    
    assert test3.query(9, 1) == 4  # l > r时自动交换，返回3出现的次数
    print("测试用例3通过!")
    
    # 测试用例4：多个相同元素
    test4 = FrequentValuesRMQ()
    data4 = [7, 7, 7, 7, 7]
    test4.set_array(data4, len(data4))
    test4.build(len(data4))
    
    assert test4.query(1, 5) == 5  # 所有元素都相同
    assert test4.query(2, 4) == 3  # 部分区间
    print("测试用例4通过!")
    
    print("所有测试用例通过!")


def main():
    """主函数，处理输入输出"""
    # 运行测试（可选）
    # test_sparse_table()
    
    # 读取输入
    input = sys.stdin.read().split()
    ptr = 0
    
    # 创建查询对象
    solver = FrequentValuesRMQ()
    
    while True:
        n = int(input[ptr])
        ptr += 1
        if n == 0:
            break
        q = int(input[ptr])
        ptr += 1
        
        # 读取数组数据
        data = []
        for _ in range(n):
            data.append(int(input[ptr]))
            ptr += 1
        
        # 设置数组并构建数据结构
        solver.set_array(data, n)
        solver.build(n)
        
        # 处理查询
        for _ in range(q):
            l = int(input[ptr])
            ptr += 1
            r = int(input[ptr])
            ptr += 1
            print(solver.query(l, r))

	if __name__ == "__main__":
	    main()
	*/

	// 对数器
	// 为了验证
	public static void main(String[] args) {
		System.out.println("测试开始");
		int n = 10000;
		int v = 100;
		int m = 5000;
		randomArray(n, v);
		build(n);
		for (int i = 1, l, r; i <= m; i++) {
			l = (int) (Math.random() * n) + 1;
			r = (int) (Math.random() * n) + 1;
			if (query(l, r) != checkQuery(l, r)) {
				System.out.println("出错了!");
			}
		}
		System.out.println("测试结束");
	}

	// 得到随机数组
	// 为了验证
	public static void randomArray(int n, int v) {
		for (int i = 1; i <= n; i++) {
			arr[i] = (int) (Math.random() * 2 * v) - v;
		}
		Arrays.sort(arr, 1, n + 1);
	}

	// 暴力方法
	// 直接遍历统计词频
	// 为了验证
	public static int checkQuery(int l, int r) {
		if (l > r) {
			int tmp = l;
			l = r;
			r = tmp;
		}
		HashMap<Integer, Integer> map = new HashMap<>();
		for (int i = l; i <= r; i++) {
			map.put(arr[i], map.getOrDefault(arr[i], 0) + 1);
		}
		int ans = 0;
		for (int v : map.values()) {
			ans = Math.max(ans, v);
		}
		return ans;
	}

}
