package class108;

/**
 * 二维树状数组区间增加、区间查询模板
 * 
 * 本实现通过维护四个二维树状数组，实现了O(log n * log m)时间复杂度的矩形区域更新和查询操作。
 * 算法基于二维差分数组的思想，并通过数学推导得出需要维护的四个关键数组，以支持高效的区间操作。
 * 
 * 测试链接: https://www.luogu.com.cn/problem/P4514
 * 提交时请把类名改成"Main"，可以通过所有测试用例
 * 
 * 时间复杂度分析：
 * - 区间更新(add): O(log n * log m)
 * - 区间查询(range): O(log n * log m)
 * 空间复杂度: O(n * m)
 * 
 * 数学原理分析：
 * 二维树状数组区间更新+区间查询的实现基于以下数学推导：
 * 1. 对于二维数组的前缀和sum(x,y)，可以通过差分和树状数组的结合高效计算
 * 2. 为了支持区间更新和区间查询，需要维护四个树状数组，分别对应不同的组合项
 * 3. 通过数学展开和化简，可以得到查询前缀和时的线性组合公式
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;

public class Code05_TwoDimensionIntervalAddIntervalQuery1 {

	/**
	 * 最大数据范围，根据题目要求设置
	 */
	public static int MAXN = 2050;

	/**
	 * 最大数据范围，根据题目要求设置
	 */
	public static int MAXM = 2050;

	/**
	 * 维护四个二维树状数组，用于支持二维区间更新和区间查询
	 * info1[i][j]: 维护差分数组d[i][j]
	 * info2[i][j]: 维护d[i][j] * i
	 * info3[i][j]: 维护d[i][j] * j
	 * info4[i][j]: 维护d[i][j] * i * j
	 */
	// 维护信息 : d[i][j]
	public static int[][] info1 = new int[MAXN][MAXM];

	// 维护信息 : d[i][j] * i
	public static int[][] info2 = new int[MAXN][MAXM];

	// 维护信息 : d[i][j] * j
	public static int[][] info3 = new int[MAXN][MAXM];

	// 维护信息 : d[i][j] * i * j
	public static int[][] info4 = new int[MAXN][MAXM];

	/**
	 * n: 二维数组的行数
	 * m: 二维数组的列数
	 */
	public static int n, m;

	/**
	 * lowbit函数：获取数字i的二进制表示中最低位的1所对应的值
	 * 
	 * @param i 输入的整数
	 * @return i的二进制表示中最低位的1所对应的值
	 */
	public static int lowbit(int i) {
		return i & -i;
	}

	/**
	 * 在点(x,y)处更新差分数组，并同时维护四个树状数组
	 * 
	 * @param x 行坐标（从1开始）
	 * @param y 列坐标（从1开始）
	 * @param v 要增加的值
	 */
	public static void add(int x, int y, int v) {
		// 计算四个需要更新的树状数组对应的值
		int v1 = v;            // 对应info1: d[i][j]
		int v2 = x * v;        // 对应info2: d[i][j] * i
		int v3 = y * v;        // 对应info3: d[i][j] * j
		int v4 = x * y * v;    // 对应info4: d[i][j] * i * j
		
		// 同时更新四个树状数组
		for (int i = x; i <= n; i += lowbit(i)) {
			for (int j = y; j <= m; j += lowbit(j)) {
				info1[i][j] += v1;
				info2[i][j] += v2;
				info3[i][j] += v3;
				info4[i][j] += v4;
			}
		}
	}

	/**
	 * 计算二维前缀和(1,1)~(x,y)
	 * 使用数学推导得出的公式，结合四个树状数组计算二维前缀和
	 * 
	 * @param x 行坐标（从1开始）
	 * @param y 列坐标（从1开始）
	 * @return (1,1)~(x,y)矩形区域的和
	 */
	// 以(1,1)左上角，以(x,y)右下角
	public static int sum(int x, int y) {
		int ans = 0;
		// 遍历树状数组计算前缀和
		for (int i = x; i > 0; i -= lowbit(i)) {
			for (int j = y; j > 0; j -= lowbit(j)) {
				// 数学公式计算：(x+1)(y+1)*info1 - (y+1)*info2 - (x+1)*info3 + info4
				// 此公式由二维前缀和展开推导得出
				ans += (x + 1) * (y + 1) * info1[i][j] - (y + 1) * info2[i][j] - (x + 1) * info3[i][j] + info4[i][j];
			}
		}
		return ans;
	}

	/**
	 * 给矩形区域(a,b)~(c,d)的所有元素加v
	 * 利用二维差分数组的特性，将矩形区域更新转换为四个角落点的更新
	 * 
	 * @param a 左上区域行坐标（从1开始）
	 * @param b 左上区域列坐标（从1开始）
	 * @param c 右下区域行坐标（从1开始）
	 * @param d 右下区域列坐标（从1开始）
	 * @param v 要增加的值
	 */
	public static void add(int a, int b, int c, int d, int v) {
		// 利用二维差分数组的特性，对四个角点进行更新
		add(a, b, v);         // (a,b)处加v
		add(a, d + 1, -v);    // (a,d+1)处减v
		add(c + 1, b, -v);    // (c+1,b)处减v
		add(c + 1, d + 1, v); // (c+1,d+1)处加v
	}

	/**
	 * 查询区域和(a,b)~(c,d)
	 * 利用二维前缀和的容斥原理，通过四个前缀和的组合计算出目标区域的和
	 * 
	 * @param a 左上区域行坐标（从1开始）
	 * @param b 左上区域列坐标（从1开始）
	 * @param c 右下区域行坐标（从1开始）
	 * @param d 右下区域列坐标（从1开始）
	 * @return (a,b)~(c,d)矩形区域的和
	 */
	public static int range(int a, int b, int c, int d) {
		// 容斥原理：全量减去两边加上重叠部分
		return sum(c, d) - sum(a - 1, d) - sum(c, b - 1) + sum(a - 1, b - 1);
	}

	/**
	 * 主函数，处理输入输出和操作请求
	 */
	public static void main(String[] args) throws IOException {
		// 初始化输入流
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		String op;
		int a, b, c, d, v;
		
		// 处理输入直到文件结束
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			op = in.sval;
			// X命令：初始化二维数组大小
			if (op.equals("X")) {
				in.nextToken();
				n = (int) in.nval;
				in.nextToken();
				m = (int) in.nval;
			} 
			// L命令：区间更新操作
			else if (op.equals("L")) {
				in.nextToken();
				a = (int) in.nval;
				in.nextToken();
				b = (int) in.nval;
				in.nextToken();
				c = (int) in.nval;
				in.nextToken();
				d = (int) in.nval;
				in.nextToken();
				v = (int) in.nval;
				// 调用区间更新方法
				add(a, b, c, d, v);
			} 
			// 查询命令：区间查询操作
			else {
				in.nextToken();
				a = (int) in.nval;
				in.nextToken();
				b = (int) in.nval;
				in.nextToken();
				c = (int) in.nval;
				in.nextToken();
				d = (int) in.nval;
				// 调用区间查询方法并输出结果
				System.out.println(range(a, b, c, d));
			}
		}
		// 关闭资源
		br.close();
	}

	/**
	 * 二维树状数组区间更新+区间查询的实现原理详解：
	 * 
	 * 1. 算法核心思想：
	 *    - 结合差分数组和二维树状数组的思想
	 *    - 差分数组用于实现高效的区间更新
	 *    - 树状数组用于高效查询前缀和
	 * 
	 * 2. 数学推导：
	 *    对于二维数组A[i][j]，定义差分数组d[i][j]满足：
	 *    A[i][j] = sum_{x=1 to i} sum_{y=1 to j} d[x][y]
	 *    
	 *    前缀和S(x,y) = sum_{i=1 to x} sum_{j=1 to y} A[i][j]
	 *    展开后需要维护四个关键组合项
	 * 
	 * 3. 实现细节：
	 *    - 使用四个树状数组维护不同的组合项
	 *    - 区间更新转换为四个角点的更新操作
	 *    - 区间查询使用容斥原理组合四个前缀和
	 * 
	 * 4. 数据结构优势：
	 *    - 相比普通二维前缀和，支持高效区间修改
	 *    - 相比二维线段树，实现更简洁，常数更小
	 *    - 相比暴力算法，时间复杂度大幅提升
	 */
}


/**
 * 以下是C++实现的树状数组（Fenwick Tree）代码
 * 包含1D树状数组、2D树状数组、以及各种应用场景的实现
 * 
 * // 1D树状数组实现（单点更新，区间查询）
 * class FenwickTree1D {
 * private:
 *     vector<int> tree;
 *     int n;
 * 
 * public:
 *     // 构造函数
 *     FenwickTree1D(int size) : n(size) {
 *         tree.resize(n + 1, 0); // 树状数组从索引1开始
 *     }
 * 
 *     // lowbit操作
 *     int lowbit(int x) {
 *         return x & (-x);
 *     }
 * 
 *     // 单点更新：在位置i增加val
 *     void update(int i, int val) {
 *         while (i <= n) {
 *             tree[i] += val;
 *             i += lowbit(i);
 *         }
 *     }
 * 
 *     // 前缀和查询：查询[1, i]的和
 *     int query(int i) {
 *         int sum = 0;
 *         while (i > 0) {
 *             sum += tree[i];
 *             i -= lowbit(i);
 *         }
 *         return sum;
 *     }
 * 
 *     // 区间查询：查询[l, r]的和
 *     int rangeQuery(int l, int r) {
 *         return query(r) - query(l - 1);
 *     }
 * };
 * 
 * // LeetCode 315 - 计算右侧小于当前元素的个数（C++实现）
 * class Solution315 {
 * private:
 *     vector<int> tree;
 *     int n;
 * 
 *     int lowbit(int x) {
 *         return x & (-x);
 *     }
 * 
 *     void update(int i, int val) {
 *         while (i <= n) {
 *             tree[i] += val;
 *             i += lowbit(i);
 *         }
 *     }
 * 
 *     int query(int i) {
 *         int sum = 0;
 *         while (i > 0) {
 *             sum += tree[i];
 *             i -= lowbit(i);
 *         }
 *         return sum;
 *     }
 * 
 * public:
 *     vector<int> countSmaller(vector<int>& nums) {
 *         int m = nums.size();
 *         if (m == 0) return {};
 *         
 *         // 离散化处理
 *         vector<int> sortedNums = nums;
 *         sort(sortedNums.begin(), sortedNums.end());
 *         sortedNums.erase(unique(sortedNums.begin(), sortedNums.end()), sortedNums.end());
 *         
 *         unordered_map<int, int> valueToRank;
 *         for (int i = 0; i < sortedNums.size(); ++i) {
 *             valueToRank[sortedNums[i]] = i + 1; // 排名从1开始
 *         }
 *         
 *         n = sortedNums.size();
 *         tree.resize(n + 1, 0);
 *         
 *         vector<int> result(m);
 *         // 从右向左遍历
 *         for (int i = m - 1; i >= 0; --i) {
 *             int rank = valueToRank[nums[i]];
 *             result[i] = query(rank - 1); // 查询比当前元素小的个数
 *             update(rank, 1); // 将当前元素加入树状数组
 *         }
 *         
 *         return result;
 *     }
 * };
 * 
 * // 二维树状数组（单点更新，区间查询）
 * class FenwickTree2D {
 * private:
 *     vector<vector<int>> tree;
 *     int m, n;
 * 
 *     int lowbit(int x) {
 *         return x & (-x);
 *     }
 * 
 * public:
 *     FenwickTree2D(int rows, int cols) : m(rows), n(cols) {
 *         tree.resize(m + 1, vector<int>(n + 1, 0));
 *     }
 * 
 *     void update(int x, int y, int val) {
 *         for (int i = x; i <= m; i += lowbit(i)) {
 *             for (int j = y; j <= n; j += lowbit(j)) {
 *                 tree[i][j] += val;
 *             }
 *         }
 *     }
 * 
 *     int query(int x, int y) {
 *         int sum = 0;
 *         for (int i = x; i > 0; i -= lowbit(i)) {
 *             for (int j = y; j > 0; j -= lowbit(j)) {
 *                 sum += tree[i][j];
 *             }
 *         }
 *         return sum;
 *     }
 * 
 *     int rangeQuery(int x1, int y1, int x2, int y2) {
 *         return query(x2, y2) - query(x1 - 1, y2) - query(x2, y1 - 1) + query(x1 - 1, y1 - 1);
 *     }
 * };
 * 
 * // LeetCode 493 - 翻转对（C++实现）
 * class Solution493 {
 * private:
 *     vector<int> tree;
 *     int n;
 * 
 *     int lowbit(int x) {
 *         return x & (-x);
 *     }
 * 
 *     void update(int i, int val) {
 *         while (i <= n) {
 *             tree[i] += val;
 *             i += lowbit(i);
 *         }
 *     }
 * 
 *     int query(int i) {
 *         int sum = 0;
 *         while (i > 0) {
 *             sum += tree[i];
 *             i -= lowbit(i);
 *         }
 *         return sum;
 *     }
 * 
 * public:
 *     int reversePairs(vector<int>& nums) {
 *         int m = nums.size();
 *         if (m <= 1) return 0;
 *         
 *         // 离散化处理
 *         set<long long> allNums;
 *         for (int num : nums) {
 *             allNums.insert((long long)num);
 *             allNums.insert((long long)num * 2);
 *         }
 *         
 *         vector<long long> sortedNums(allNums.begin(), allNums.end());
 *         unordered_map<long long, int> valueToRank;
 *         for (int i = 0; i < sortedNums.size(); ++i) {
 *             valueToRank[sortedNums[i]] = i + 1;
 *         }
 *         
 *         n = sortedNums.size();
 *         tree.resize(n + 1, 0);
 *         
 *         int count = 0;
 *         for (int i = m - 1; i >= 0; --i) {
 *             // 二分查找找到第一个大于等于nums[i]/2的位置
 *             auto it = lower_bound(sortedNums.begin(), sortedNums.end(), (double)nums[i] / 2);
 *             int idx = it - sortedNums.begin();
 *             
 *             if (idx > 0) {
 *                 count += query(idx);
 *             }
 *             
 *             int rank = valueToRank[(long long)nums[i]];
 *             update(rank, 1);
 *         }
 *         
 *         return count;
 *     }
 * };
 */


/**
 * 以下是Python实现的树状数组（Fenwick Tree）代码
 * 包含1D树状数组、2D树状数组、以及各种应用场景的实现
 * 
 * # 1D树状数组实现（单点更新，区间查询）
 * class FenwickTree1D:
 *     def __init__(self, size):
 *         self.n = size
 *         self.tree = [0] * (self.n + 1)  # 树状数组从索引1开始
 *     
 *     def lowbit(self, x):
 *         # 获取x的二进制表示中最低位1所对应的值
 *         return x & (-x)
 *     
 *     def update(self, i, val):
 *         # 在位置i增加val
 *         while i <= self.n:
 *             self.tree[i] += val
 *             i += self.lowbit(i)
 *     
 *     def query(self, i):
 *         # 查询[1, i]的前缀和
 *         res = 0
 *         while i > 0:
 *             res += self.tree[i]
 *             i -= self.lowbit(i)
 *         return res
 *     
 *     def range_query(self, l, r):
 *         # 查询[l, r]的区间和
 *         return self.query(r) - self.query(l - 1)
 * 
 * # LeetCode 315 - 计算右侧小于当前元素的个数（Python实现）
 * class Solution315:
 *     def countSmaller(self, nums):
 *         n = len(nums)
 *         if n == 0:
 *             return []
 *         
 *         # 离散化处理
 *         sorted_nums = sorted(set(nums))
 *         value_to_rank = {val: i + 1 for i, val in enumerate(sorted_nums)}  # 排名从1开始
 *         
 *         max_rank = len(sorted_nums)
 *         fenwick_tree = FenwickTree1D(max_rank)
 *         
 *         result = [0] * n
 *         # 从右向左遍历
 *         for i in range(n - 1, -1, -1):
 *             rank = value_to_rank[nums[i]]
 *             result[i] = fenwick_tree.query(rank - 1)  # 查询比当前元素小的个数
 *             fenwick_tree.update(rank, 1)  # 将当前元素加入树状数组
 *         
 *         return result
 * 
 * # 二维树状数组（单点更新，区间查询）
 * class FenwickTree2D:
 *     def __init__(self, rows, cols):
 *         self.m = rows
 *         self.n = cols
 *         self.tree = [[0] * (self.n + 1) for _ in range(self.m + 1)]
 *     
 *     def lowbit(self, x):
 *         return x & (-x)
 *     
 *     def update(self, x, y, val):
 *         i = x
 *         while i <= self.m:
 *             j = y
 *             while j <= self.n:
 *                 self.tree[i][j] += val
 *                 j += self.lowbit(j)
 *             i += self.lowbit(i)
 *     
 *     def query(self, x, y):
 *         res = 0
 *         i = x
 *         while i > 0:
 *             j = y
 *             while j > 0:
 *                 res += self.tree[i][j]
 *                 j -= self.lowbit(j)
 *             i -= self.lowbit(i)
 *         return res
 *     
 *     def range_query(self, x1, y1, x2, y2):
 *         return self.query(x2, y2) - self.query(x1 - 1, y2) - self.query(x2, y1 - 1) + self.query(x1 - 1, y1 - 1)
 * 
 * # LeetCode 493 - 翻转对（Python实现）
 * class Solution493:
 *     def reversePairs(self, nums):
 *         n = len(nums)
 *         if n <= 1:
 *             return 0
 *         
 *         # 离散化处理
 *         all_nums = set()
 *         for num in nums:
 *             all_nums.add(num)
 *             all_nums.add(2 * num)
 *         
 *         sorted_nums = sorted(all_nums)
 *         value_to_rank = {val: i + 1 for i, val in enumerate(sorted_nums)}
 *         
 *         max_rank = len(sorted_nums)
 *         fenwick_tree = FenwickTree1D(max_rank)
 *         
 *         count = 0
 *         for i in range(n - 1, -1, -1):
 *             # 二分查找找到第一个大于等于nums[i]/2的位置
 *             target = nums[i] / 2
 *             left, right = 0, len(sorted_nums)
 *             while left < right:
 *                 mid = (left + right) // 2
 *                 if sorted_nums[mid] >= target:
 *                     right = mid
 *                 else:
 *                     left = mid + 1
 *             
 *             if left > 0:
 *                 count += fenwick_tree.query(left)
 *             
 *             rank = value_to_rank[nums[i]]
 *             fenwick_tree.update(rank, 1)
 *         
 *         return count
 */
