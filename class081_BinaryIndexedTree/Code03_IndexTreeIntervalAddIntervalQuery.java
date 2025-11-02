package class108;

/**
 * 树状数组范围增加、范围查询模板
 * 
 * 本实现通过结合差分思想与树状数组，使用两个树状数组来维护差分数组的信息，
 * 从而实现O(log n)时间复杂度的区间更新和区间查询操作。
 * 
 * 核心思想：
 * 利用差分思想的扩展，推导出前缀和查询公式，需要维护两个树状数组：
 * 1. info1[i]：维护d[i]，其中d是差分数组
 * 2. info2[i]：维护d[i]*(i-1)
 * 通过这两个数组的组合，可以计算出原数组的前缀和和区间和
 * 
 * 测试链接: https://www.luogu.com.cn/problem/P3372
 * 提交时请把类名改成"Main"，可以直接通过
 * 
 * 时间复杂度分析：
 * - 区间更新(add): O(log n)
 * - 区间查询(range): O(log n)
 * 空间复杂度: O(n)
 * 
 * 数学原理分析：
 * 设差分数组为d，原数组为a，则有：
 * 1. a[i] = d[1] + d[2] + ... + d[i]
 * 2. 原数组前缀和sum(a[1..i]) = i*d[1] + (i-1)*d[2] + ... + 1*d[i]
 * 3. 可以拆分为: i*(d[1]+d[2]+...+d[i]) - (0*d[1] + 1*d[2] + ... + (i-1)*d[i])
 * 4. 即: i*sum1(i) - sum2(i)
 * 其中，sum1(i)是info1的前缀和，sum2(i)是info2的前缀和
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code03_IndexTreeIntervalAddIntervalQuery {

	/**
	 * 最大数据范围，根据题目要求设置
	 */
	public static int MAXN = 100001;

	/**
	 * 两个树状数组，用于维护差分数组的信息
	 * info1[i]：维护d[i]，其中d是差分数组
	 */
	public static long[] info1 = new long[MAXN];

	/**
	 * info2[i]：维护d[i]*(i-1)
	 */
	public static long[] info2 = new long[MAXN];

	/**
	 * n: 数组长度
	 * m: 操作次数
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
	 * 更新指定树状数组在位置i的值
	 * 
	 * @param tree 要更新的树状数组
	 * @param i 要更新的位置（从1开始）
	 * @param v 要增加的值
	 */
	public static void add(long[] tree, int i, long v) {
		while (i <= n) {
			tree[i] += v;
			i += lowbit(i);
		}
	}

	/**
	 * 查询树状数组前i个元素的前缀和
	 * 
	 * @param tree 要查询的树状数组
	 * @param i 查询的范围（从1到i）
	 * @return 树状数组的前缀和
	 */
	public static long sum(long[] tree, int i) {
		long ans = 0;
		while (i > 0) {
			ans += tree[i];
			i -= lowbit(i);
		}
		return ans;
	}

	/**
	 * 在区间[l,r]上增加值v
	 * 利用差分数组的特性，将区间更新转换为两个单点更新
	 * 
	 * @param l 区间左边界（从1开始）
	 * @param r 区间右边界（从1开始）
	 * @param v 要增加的值
	 */
	public static void add(int l, int r, long v) {
		// 对差分数组的影响：d[l] += v，d[r+1] -= v
		add(info1, l, v);
		add(info1, r + 1, -v);
		// 对应info2数组的更新：d[l]*(l-1) += v*(l-1)，d[r+1]*r -= v*r
		add(info2, l, (l - 1) * v);
		add(info2, r + 1, -(r * v));
	}

	/**
	 * 计算原数组前缀和[1..i]
	 * 
	 * @param i 查询的右边界（从1开始）
	 * @return 原数组前i项的和
	 */
	public static long prefixSum(int i) {
		return sum(info1, i) * i - sum(info2, i);
	}

	/**
	 * 查询原始数组中[l..r]范围上的累加和
	 * 通过两次前缀和相减得到区间和
	 * 
	 * @param l 区间左边界（从1开始）
	 * @param r 区间右边界（从1开始）
	 * @return 原数组区间[l,r]的和
	 */
	public static long range(int l, int r) {
		return prefixSum(r) - prefixSum(l - 1);
	}

	/**
	 * 主函数，处理输入输出
	 */
	public static void main(String[] args) throws IOException {
		// 初始化输入输出流
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取数组长度n和操作次数m
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		m = (int) in.nval;
		
		// 初始化数组：直接使用原始值进行单点更新
		long cur;
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			cur = (long) in.nval;
			// 对每个位置进行单点更新，相当于构造初始差分数组
			add(i, i, cur);
		}
		
		// 处理m个操作
		long v;
		for (int i = 1, op, l, r; i <= m; i++) {
			in.nextToken();
			op = (int) in.nval;  // 操作类型
			
			// 判断操作类型
			if (op == 1) {
				// 操作1：区间更新
				in.nextToken(); l = (int) in.nval;  // 区间左边界
				in.nextToken(); r = (int) in.nval;  // 区间右边界
				in.nextToken(); v = (long) in.nval;  // 要增加的值
				add(l, r, v);  // 调用add方法进行区间更新
			} else {
				// 操作2：区间查询
				in.nextToken(); l = (int) in.nval;  // 区间左边界
				in.nextToken(); r = (int) in.nval;  // 区间右边界
				out.println(range(l, r));  // 调用range方法进行区间查询
			}
		}
		
		// 刷新输出流并关闭资源
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * 区间更新+区间查询的应用场景：
	 * 1. 区间加法操作，频繁查询区间和
	 * 2. 二维前缀和等复杂的统计问题
	 * 3. 大型数据集合的区间统计和修改
	 * 
	 * 实现要点：
	 * 1. 使用两个树状数组维护差分数组的信息
	 * 2. 通过数学推导得出前缀和查询公式
	 * 3. 特别注意数据范围，使用long类型避免溢出
	 * 
	 * 与其他数据结构对比：
	 * - 相比线段树，代码更简洁，常数更小，但灵活性稍差
	 * - 相比普通前缀和，支持高效的区间修改
	 * - 特别适合需要频繁区间修改和区间查询的场景
	 */

	/**
	 * 区间更新区间查询树状数组的数学原理深度推导：
	 * 
	 * 1. 前缀和公式推导：
	 *    设原数组为a，差分数组为d（d[1] = a[1], d[i] = a[i] - a[i-1] for i>1）
	 *    则a[i] = d[1] + d[2] + ... + d[i] = sum1(i) ，其中sum1(i)是d数组的前i项和
	 *    
	 * 2. 原数组前缀和sum(a[1..i])计算：
	 *    sum(a[1..i]) = a[1] + a[2] + ... + a[i]
	 *                = d[1] + (d[1]+d[2]) + ... + (d[1]+d[2]+...+d[i])
	 *                = i*d[1] + (i-1)*d[2] + ... + 1*d[i]
	 *                = i*(d[1]+d[2]+...+d[i]) - (0*d[1] + 1*d[2] + ... + (i-1)*d[i])
	 *                = i*sum1(i) - sum2(i)，其中sum2(i)是d[k]*(k-1)的前i项和
	 *    
	 * 3. 区间更新的影响：
	 *    当对区间[l,r]加上v时，差分数组的变化是：d[l] += v, d[r+1] -= v
	 *    因此info1数组的更新为：info1[l] += v, info1[r+1] -= v
	 *    info2数组的更新为：info2[l] += v*(l-1), info2[r+1] -= v*r
	 * 
	 * 4. 时间复杂度分析：
	 *    - 区间更新操作需要更新两个树状数组，每次操作的时间复杂度为O(log n)
	 *    - 区间查询操作需要两次前缀和计算，每次查询的时间复杂度为O(log n)
	 *    - 空间复杂度为O(n)，需要两个大小为n的数组
	 */
}

/**
 * 以下是C++实现的树状数组（Fenwick Tree）区间更新区间查询代码
 * 
 * #include <iostream>
 * #include <vector>
 * using namespace std;
 * 
 * const int MAXN = 100001; // 最大数据范围
 * 
 * class FenwickTree {
 * private:
 *     vector<long long> info1; // 维护差分数组d[i]
 *     vector<long long> info2; // 维护差分数组d[i]*(i-1)
 *     int n; // 数组长度
 * 
 *     // lowbit函数
 *     int lowbit(int x) {
 *         return x & -x;
 *     }
 * 
 *     // 更新树状数组
 *     void add(vector<long long>& tree, int i, long long v) {
 *         while (i <= n) {
 *             tree[i] += v;
 *             i += lowbit(i);
 *         }
 *     }
 * 
 *     // 查询树状数组前缀和
 *     long long sum(vector<long long>& tree, int i) {
 *         long long ans = 0;
 *         while (i > 0) {
 *             ans += tree[i];
 *             i -= lowbit(i);
 *         }
 *         return ans;
 *     }
 * 
 *     // 计算原数组前缀和[1..i]
 *     long long prefixSum(int i) {
 *         return sum(info1, i) * i - sum(info2, i);
 *     }
 * 
 * public:
 *     // 构造函数
 *     FenwickTree(int size) {
 *         n = size;
 *         info1.resize(n + 2, 0); // 多分配空间避免越界
 *         info2.resize(n + 2, 0);
 *     }
 * 
 *     // 区间更新：对区间[l,r]加上v
 *     void rangeAdd(int l, int r, long long v) {
 *         // 更新info1数组
 *         add(info1, l, v);
 *         add(info1, r + 1, -v);
 *         
 *         // 更新info2数组
 *         add(info2, l, v * (l - 1));
 *         add(info2, r + 1, -v * r);
 *     }
 * 
 *     // 区间查询：查询[l,r]的和
 *     long long rangeQuery(int l, int r) {
 *         return prefixSum(r) - prefixSum(l - 1);
 *     }
 * };
 * 
 * int main() {
 *     ios::sync_with_stdio(false); // 关闭同步，加速输入输出
 *     cin.tie(nullptr);
 *     cout.tie(nullptr);
 * 
 *     int n, m;
 *     cin >> n >> m;
 * 
 *     FenwickTree ft(n);
 * 
 *     // 初始化数组
 *     for (int i = 1; i <= n; ++i) {
 *         long long v;
 *         cin >> v;
 *         ft.rangeAdd(i, i, v); // 单点初始化相当于区间[i,i]加v
 *     }
 * 
 *     // 处理操作
 *     while (m--) {
 *         int op, l, r;
 *         long long v;
 *         cin >> op;
 *         
 *         if (op == 1) {
 *             // 区间更新
 *             cin >> l >> r >> v;
 *             ft.rangeAdd(l, r, v);
 *         } else {
 *             // 区间查询
 *             cin >> l >> r;
 *             cout << ft.rangeQuery(l, r) << '\n';
 *         }
 *     }
 * 
 *     return 0;
 * }
 */

/**
 * 以下是Python实现的树状数组（Fenwick Tree）区间更新区间查询代码
 * 
 * class FenwickTree:
 *     def __init__(self, size):
 *         self.n = size
 *         # 树状数组下标从1开始，多分配空间避免越界
 *         self.info1 = [0] * (self.n + 2)  # 维护差分数组d[i]
 *         self.info2 = [0] * (self.n + 2)  # 维护差分数组d[i]*(i-1)
 *     
 *     # lowbit函数
 *     def lowbit(self, x):
 *         return x & -x
 *     
 *     # 更新树状数组
 *     def _add(self, tree, i, v):
 *         while i <= self.n:
 *             tree[i] += v
 *             i += self.lowbit(i)
 *     
 *     # 查询树状数组前缀和
 *     def _sum(self, tree, i):
 *         ans = 0
 *         while i > 0:
 *             ans += tree[i]
 *             i -= self.lowbit(i)
 *         return ans
 *     
 *     # 计算原数组前缀和[1..i]
 *     def _prefix_sum(self, i):
 *         return self._sum(self.info1, i) * i - self._sum(self.info2, i)
 *     
 *     # 区间更新：对区间[l,r]加上v
 *     def range_add(self, l, r, v):
 *         # 更新info1数组
 *         self._add(self.info1, l, v)
 *         self._add(self.info1, r + 1, -v)
 *         
 *         # 更新info2数组
 *         self._add(self.info2, l, v * (l - 1))
 *         self._add(self.info2, r + 1, -v * r)
 *     
 *     # 区间查询：查询[l,r]的和
 *     def range_query(self, l, r):
 *         return self._prefix_sum(r) - self._prefix_sum(l - 1)
 * 
 * # 主函数
 * def main():
 *     import sys
 *     input = sys.stdin.read().split()
 *     ptr = 0
 *     
 *     n = int(input[ptr])
 *     ptr += 1
 *     m = int(input[ptr])
 *     ptr += 1
 *     
 *     ft = FenwickTree(n)
 *     
 *     # 初始化数组
 *     for i in range(1, n + 1):
 *         v = int(input[ptr])
 *         ptr += 1
 *         ft.range_add(i, i, v)  # 单点初始化相当于区间[i,i]加v
 *     
 *     # 处理操作
 *     output = []
 *     for _ in range(m):
 *         op = int(input[ptr])
 *         ptr += 1
 *         
 *         if op == 1:
 *             # 区间更新
 *             l = int(input[ptr])
 *             ptr += 1
 *             r = int(input[ptr])
 *             ptr += 1
 *             v = int(input[ptr])
 *             ptr += 1
 *             ft.range_add(l, r, v)
 *         else:
 *             # 区间查询
 *             l = int(input[ptr])
 *             ptr += 1
 *             r = int(input[ptr])
 *             ptr += 1
 *             output.append(str(ft.range_query(l, r)))
 *     
 *     # 批量输出结果，提高效率
 *     print('\n'.join(output))
 * 
 * if __name__ == '__main__':
 *     main()
 */

/**
 * 区间更新区间查询树状数组的高级分析与应用：
 * 
 * 1. 树状数组区间操作的可视化解释：
 *    - 差分数组视角：当在区间[l,r]添加v时，差分数组只有两个位置变化，这使得树状数组能够高效处理区间更新
 *    - 树状结构视图：
 *      树状数组的二进制结构保证每次更新和查询操作都能以O(log n)的时间访问O(log n)个节点
 *      对于区间操作，通过差分数组转换，将复杂的区间操作转化为对两个端点的操作
 * 
 * 2. 与线段树的详细对比：
 *    - 功能对比：两者都能处理区间更新和区间查询，但线段树可以支持更复杂的区间操作（如区间乘法、区间最值）
 *    - 代码复杂度：树状数组实现更简洁，代码量少，常数更小
 *    - 实际效率：对于基本的区间加和区间求和操作，树状数组通常比线段树快20%-30%
 *    - 内存占用：树状数组内存占用略少于线段树（O(n) vs O(4n)）
 * 
 * 3. 扩展到多维：
 *    - 二维区间更新和区间查询需要四个树状数组，如Code05_TwoDimensionIntervalAddIntervalQuery2.java中实现
 *    - 三维及以上场景中，线段树的优势更为明显，树状数组实现复杂度指数增长
 *    
 * 4. 工程实现要点：
 *    - 必须使用long类型，避免整数溢出
 *    - 数组索引从1开始，简化边界处理
 *    - 预分配足够空间，避免动态扩容带来的性能开销
 *    - 大批量数据处理时注意输入输出效率
 * 
 * 5. 典型应用案例详解：
 *    - 案例一：区间更新区间求和（如本题）
 *      应用场景：学生成绩统计、区间增减操作、股票价格变动
 *      输入输出示例：
 *      输入：n=5 m=3
 *           1 2 3 4 5
 *           1 1 3 2  // 区间[1,3]加2
 *           2 1 5    // 查询区间[1,5]和
 *           1 2 4 -1 // 区间[2,4]减1
 *           2 1 5    // 查询区间[1,5]和
 *      输出：25
 *           22
 *    
 *    - 案例二：二维区域求和
 *      应用场景：图像处理、矩阵操作、地理信息系统
 *      通过扩展到二维树状数组实现
 * 
 *    - 案例三：逆序对统计的优化版本
 *      应用场景：排序算法分析、数据分析
 *      结合离散化和树状数组，处理重复元素的情况
 * 
 *    - 案例四：动态区间频率统计
 *      应用场景：数据流分析、实时监控系统
 *      结合离散化和树状数组，支持动态区间频率查询
 * 
 * 6. 深入优化技巧：
 *    - 数据类型优化：根据实际问题规模选择合适的数据类型，避免不必要的内存开销
 *    - 输入输出优化：使用快速IO方法，批量读取输入和输出结果
 *    - 离散化技术：将大范围稀疏数据映射到小范围连续数据
 *    - 懒加载思想：对于特殊情况，可以结合懒加载思想进一步优化
 *    - 并行化处理：对于大规模数据，可以考虑将树状数组分割成多个独立部分并行处理
 *    - 内存访问模式优化：预分配连续内存，减少缓存未命中
 *    - 混合数据结构：针对复杂问题，考虑树状数组与其他数据结构的结合使用
 * 
 * 7. 常见陷阱与调试技巧：
 *    - 索引从1开始的边界处理
 *    - 数据溢出问题（特别是使用int而不是long）
 *    - 差分数组更新时(r+1)越界问题
 *    - 批量操作时的性能优化
 */
