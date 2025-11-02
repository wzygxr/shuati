package class108;

/**
 * 树状数组单点增加、范围查询模板
 * 
 * 树状数组（Binary Indexed Tree 或 Fenwick Tree）是一种高效的数据结构，
 * 用于处理数组的前缀和查询和单点更新操作，两者的时间复杂度均为 O(log n)。
 * 
 * 本实现支持两种操作：
 * 1. 单点更新：在数组的某个位置增加一个值
 * 2. 区间查询：查询数组中某一区间内所有元素的和
 * 
 * 测试链接: https://www.luogu.com.cn/problem/P3374
 * 提交时请把类名改成"Main"，可以直接通过
 * 
 * 时间复杂度分析：
 * - 单点更新(add): O(log n)，因为每次循环i的变化量至少翻倍，树高为log n
 * - 前缀和查询(sum): O(log n)，因为每次循环i至少减少一半，最多查询log n个节点
 * - 区间查询(range): O(log n)，由两次前缀和查询组成
 * 空间复杂度: O(n)
 * 
 * 树状数组的核心思想深度解析：
 * 
 * 1. 二进制索引表示法：
 *    树状数组巧妙利用了数字的二进制表示特性。对于任何正整数i，
 *    我们可以通过lowbit(i) = i & -i操作找到其二进制表示中最低位的1。
 *    例如：6的二进制是110，lowbit(6)=2，即10
 * 
 * 2. 树状数组的树形结构：
 *    树状数组构建了一个虚拟的树形结构，其中：
 *    - 每个节点tree[i]存储原始数组中某一段连续区间的和
 *    - 该区间的范围是[i - lowbit(i) + 1, i]
 *    - 每个节点的父节点是i + lowbit(i)
 *    
 * 3. 树形结构可视化（以n=8为例）：
 *    tree[1] (0001) 管辖范围: [1, 1]
 *    tree[2] (0010) 管辖范围: [1, 2]
 *    tree[3] (0011) 管辖范围: [3, 3]
 *    tree[4] (0100) 管辖范围: [1, 4]
 *    tree[5] (0101) 管辖范围: [5, 5]
 *    tree[6] (0110) 管辖范围: [5, 6]
 *    tree[7] (0111) 管辖范围: [7, 7]
 *    tree[8] (1000) 管辖范围: [1, 8]
 *    
 * 4. 更新与查询的过程：
 *    - 更新操作：从当前节点开始，一直向上更新所有包含它的父节点
 *    - 查询操作：从当前节点开始，一直向上累加所有父节点的值
 *    这两个过程都只需要O(log n)时间
 * 
 * 树状数组与其他数据结构的对比：
 * - 相比前缀和数组：支持高效更新，但查询操作略慢
 * - 相比线段树：代码更简洁，常数更小，但功能较局限
 * - 相比平衡二叉搜索树：实现简单，适用于特定场景
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code01_IndexTreeSingleAddIntervalQuery {

	/**
	 * 最大数据范围，根据题目要求设置
	 * 注意：在实际应用中，应根据具体问题调整此值，避免内存浪费
	 */
	public static int MAXN = 500001;

	/**
	 * 树状数组的核心数组，用于存储区间和信息
	 * 注意：树状数组的下标必须从1开始，不从0开始，这是由树状数组的性质决定的
	 */
	public static int[] tree = new int[MAXN];

	/**
	 * n: 数组长度
	 * m: 操作次数
	 */
	public static int n, m;

	/**
	 * lowbit函数：获取数字i的二进制表示中最低位的1所对应的值
	 * 例如：lowbit(12) = lowbit(1100) = 4 = 100
	 * 
	 * 实现原理：利用计算机补码的特性，-i 是i的补码表示
	 * 当i & (-i)时，只有最低位的1会被保留
	 * 
	 * @param i 输入的整数
	 * @return i的二进制表示中最低位的1所对应的值
	 */
	public static int lowbit(int i) {
		return i & -i;
	}

	/**
	 * 单点更新操作：在位置i上增加v
	 * 时间复杂度：O(log n)，因为每次循环i的变化量至少翻倍
	 * 
	 * @param i 要更新的位置（从1开始）
	 * @param v 要增加的值
	 */
	public static void add(int i, int v) {
		// 当i超过数组长度时停止更新
		while (i <= n) {
			// 更新当前节点的值
			tree[i] += v;
			// 移动到父节点继续更新
			i += lowbit(i);
		}
	}

	/**
	 * 前缀和查询操作：查询从1到i的元素和
	 * 时间复杂度：O(log n)，因为每次循环i至少减少一半
	 * 
	 * @param i 前缀的结束位置（从1开始）
	 * @return 1到i位置的元素和
	 */
	public static int sum(int i) {
		int ans = 0;
		// 当i小于等于0时停止查询
		while (i > 0) {
			// 累加当前节点的值
			ans += tree[i];
			// 移动到父节点继续查询
			i -= lowbit(i);
		}
		return ans;
	}

	/**
	 * 区间查询操作：查询从l到r的元素和
	 * 时间复杂度：O(log n)，由两次前缀和查询组成
	 * 
	 * @param l 区间的起始位置（从1开始）
	 * @param r 区间的结束位置（从1开始）
	 * @return l到r位置的元素和
	 */
	public static int range(int l, int r) {
		// 利用前缀和的性质：区间和 = 前缀和(r) - 前缀和(l-1)
		return sum(r) - sum(l - 1);
	}

	/**
	 * 主函数，处理输入输出
	 * 使用高效的输入输出方式（BufferedReader + StreamTokenizer + PrintWriter）
	 * 以应对大规模数据输入输出的情况
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
		
		// 初始化数组：逐个读取初始值并插入树状数组
		for (int i = 1, v; i <= n; i++) {
			in.nextToken();
			v = (int) in.nval;
			// 调用add函数将初始值插入树状数组
			add(i, v);
		}
		
		// 处理m个操作
		for (int i = 1, a, b, c; i <= m; i++) {
			in.nextToken(); 
			a = (int) in.nval;  // 操作类型
			in.nextToken(); 
			b = (int) in.nval;  // 第一个参数（位置或左边界）
			in.nextToken(); 
			c = (int) in.nval;  // 第二个参数（值或右边界）
			
			// 判断操作类型
			if (a == 1) {
				// 操作1：单点更新，在位置b增加c
				add(b, c);
			} else {
				// 操作2：区间查询，查询区间[b, c]的和
				out.println(range(b, c));
			}
		}
		
		// 刷新输出流并关闭资源
		out.flush();
		out.close();
		br.close();
	}

	/**
 * 树状数组的性能优化与工程实践：
 * 
 * 1. 数据类型优化：
 *    - 使用long或long long代替int避免溢出
 *    - 对于不同问题，选择合适的数据类型平衡内存和精度
 * 
 * 2. 输入输出优化：
 *    - 对于大规模数据，使用快速IO方法（如C++的scanf/printf或关闭同步）
 *    - 在Java中使用BufferedReader和PrintWriter
 *    - 在Python中一次性读取所有输入再处理
 * 
 * 3. 离散化技术：
 *    - 当元素范围很大（如1e9）但数量有限（如1e5）时，必须离散化
 *    - 离散化步骤：去重、排序、映射
 *    - 注意处理相等元素的情况
 * 
 * 4. 边界条件处理：
 *    - 树状数组下标从1开始，需要注意转换
 *    - 处理区间端点时避免越界
 *    - 考虑初始条件和特殊输入（如空数组）
 * 
 * 5. 常见陷阱：
 *    - 整数溢出：忘记使用大整数类型
 *    - 下标错误：混淆从0开始和从1开始的索引
 *    - 范围错误：查询或更新时超出数组范围
 *    - 离散化错误：未正确处理重复元素或排序
 * 
 * 6. 测试用例设计：
 *    - 基本功能测试：确保单点更新和区间查询正确
 *    - 边界测试：空数组、单元素数组、最大范围
 *    - 性能测试：大规模数据下的运行时间
 *    - 极端测试：多次更新同一位置、大数值操作
 * 
 * 7. 线程安全考虑：
 *    - 树状数组默认不是线程安全的
 *    - 在并发环境中，需要添加适当的同步机制
 *    - 考虑使用读写锁提高并发性能
 */
}

/**
 * 以下是C++实现的树状数组（Fenwick Tree）单点更新区间查询代码
 * 
 * #include <iostream>
 * #include <vector>
 * using namespace std;
 * 
 * const int MAXN = 500001; // 最大数据范围
 * 
 * class IndexTree {
 * private:
 *     vector<long long> tree; // 使用long long防止溢出
 *     int n; // 数组长度
 * 
 *     // lowbit函数：获取数字i的二进制表示中最低位的1所对应的值
 *     int lowbit(int i) {
 *         return i & -i;
 *     }
 * 
 * public:
 *     // 构造函数
 *     IndexTree(int size) {
 *         n = size;
 *         tree.resize(n + 1, 0); // 树状数组下标从1开始
 *     }
 * 
 *     // 单点更新操作：在位置i上增加v
 *     void add(int i, long long v) {
 *         while (i <= n) {
 *             tree[i] += v;
 *             i += lowbit(i);
 *         }
 *     }
 * 
 *     // 前缀和查询操作：查询从1到i的元素和
 *     long long sum(int i) {
 *         long long ans = 0;
 *         while (i > 0) {
 *             ans += tree[i];
 *             i -= lowbit(i);
 *         }
 *         return ans;
 *     }
 * 
 *     // 区间查询操作：查询从l到r的元素和
 *     long long range(int l, int r) {
 *         return sum(r) - sum(l - 1);
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
 *     IndexTree indexTree(n);
 * 
 *     // 初始化数组
 *     for (int i = 1, v; i <= n; i++) {
 *         cin >> v;
 *         indexTree.add(i, v);
 *     }
 * 
 *     // 处理操作
 *     for (int i = 1, a, b, c; i <= m; i++) {
 *         cin >> a >> b >> c;
 *         if (a == 1) {
 *             // 单点更新
 *             indexTree.add(b, c);
 *         } else {
 *             // 区间查询
 *             cout << indexTree.range(b, c) << '\n';
 *         }
 *     }
 * 
 *     return 0;
 * }
 */

/**
 * 以下是Python实现的树状数组（Fenwick Tree）单点更新区间查询代码
 * 
 * class IndexTree:
 *     def __init__(self, size):
 *         self.n = size
 *         # 树状数组下标从1开始，因此长度为n+1
 *         self.tree = [0] * (self.n + 1)
 *     
 *     # lowbit函数：获取数字i的二进制表示中最低位的1所对应的值
 *     def lowbit(self, x):
 *         return x & -x
 *     
 *     # 单点更新操作：在位置i上增加v
 *     def add(self, i, v):
 *         while i <= self.n:
 *             self.tree[i] += v
 *             i += self.lowbit(i)
 *     
 *     # 前缀和查询操作：查询从1到i的元素和
 *     def sum(self, i):
 *         ans = 0
 *         while i > 0:
 *             ans += self.tree[i]
 *             i -= self.lowbit(i)
 *         return ans
 *     
 *     # 区间查询操作：查询从l到r的元素和
 *     def range(self, l, r):
 *         return self.sum(r) - self.sum(l - 1)
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
 *     index_tree = IndexTree(n)
 *     
 *     # 初始化数组
 *     for i in range(1, n + 1):
 *         v = int(input[ptr])
 *         ptr += 1
 *         index_tree.add(i, v)
 *     
 *     # 处理操作
 *     output = []
 *     for _ in range(m):
 *         a = int(input[ptr])
 *         ptr += 1
 *         b = int(input[ptr])
 *         ptr += 1
 *         c = int(input[ptr])
 *         ptr += 1
 *         
 *         if a == 1:
 *             # 单点更新
 *             index_tree.add(b, c)
 *         else:
 *             # 区间查询
 *             output.append(str(index_tree.range(b, c)))
 *     
 *     # 批量输出结果，提高效率
 *     print('\n'.join(output))
 * 
 * if __name__ == '__main__':
 *     main()
 */

/**
 * 树状数组的典型应用案例详解：
 * 
 * 1. 逆序对统计（LeetCode 315）：
 *    - 问题描述：计算一个数组中逆序对的数量（i<j且nums[i]>nums[j]）
 *    - 解法思路：离散化数组元素，从右到左遍历，每次查询已处理元素中小于当前元素的数量
 *    - 代码实现：
 *      ```java
 *      // 离散化数组
 *      // 从右向左遍历，对于每个元素nums[i]，查询树状数组中小于nums[i]的元素个数
 *      // 将nums[i]添加到树状数组中
 *      ```
 *    - 时间复杂度：O(n log n)
 * 
 * 2. 区间更新，单点查询（使用差分数组）：
 *    - 问题描述：对数组的某区间[l,r]增加一个值v，然后查询单点的值
 *    - 解法思路：使用树状数组维护差分数组
 *    - 实现方法：区间更新时，add(l, v)和add(r+1, -v)；单点查询时，求前缀和sum(i)
 *    - 应用场景：区间更新操作频繁，需要高效单点查询
 * 
 * 3. 离线查询处理：
 *    - 问题描述：处理多个离线区间查询，每个查询返回区间和
 *    - 解法思路：将查询按右端点排序，逐个处理元素并回答查询
 *    - 优点：对于大量查询，可以更高效地批量处理
 * 
 * 4. 二维扩展 - 矩阵操作：
 *    - 问题描述：处理二维矩阵的单点更新和子矩阵和查询
 *    - 解法思路：扩展一维树状数组到二维，每个操作嵌套两层循环
 *    - 时间复杂度：O(log n * log m)，其中n和m是矩阵的维度
 */
