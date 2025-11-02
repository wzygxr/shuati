package class108;

/**
 * 树状数组范围增加、单点查询模板
 * 
 * 本实现通过结合差分思想与树状数组，实现了区间更新和单点查询操作，
 * 两者的时间复杂度均为 O(log n)。
 * 
 * 核心思想：利用差分数组的性质，将区间更新转换为两个单点更新操作，
 * 然后通过树状数组维护差分数组，使得单点查询也能在 O(log n) 时间内完成。
 * 
 * 测试链接: https://www.luogu.com.cn/problem/P3368
 * 提交时请把类名改成"Main"，可以直接通过
 * 
 * 时间复杂度分析：
 * - 区间更新(add): O(log n)，每次区间更新只需要两次单点更新操作
 * - 单点查询(sum): O(log n)，即树状数组的前缀和查询操作
 * 空间复杂度: O(n)
 * 
 * 差分数组原理解析：
 * 
 * 1. 差分数组定义：
 *    设原数组为 a，差分数组为 d，则：
 *    - d[1] = a[1]
 *    - d[i] = a[i] - a[i-1] (i > 1)
 *    - 原数组的每个元素等于差分数组的前缀和：a[i] = d[1] + d[2] + ... + d[i]
 * 
 * 2. 区间更新转换：
 *    当需要对区间 [l, r] 加上一个值 v 时，只需：
 *    - d[l] += v （表示从位置 l 开始的所有元素都增加 v）
 *    - d[r+1] -= v （表示从位置 r+1 开始抵消之前的增加）
 *    这样，原数组中区间 [l, r] 的所有元素都会增加 v
 * 
 * 3. 单点查询原理：
 *    查询原数组 a[i] 相当于查询差分数组的前缀和 sum(d[1..i])
 *    这恰好可以通过树状数组高效实现
 * 
 * 4. 数学证明：
 *    假设对区间 [l, r] 加上 v，即更新差分数组 d[l] += v, d[r+1] -= v
 *    则：
 *    - 当 i < l: a[i] 不变，因为 d 的前缀和不变
 *    - 当 l <= i <= r: a[i] 增加 v，因为 d[l] += v 影响前缀和
 *    - 当 i > r: a[i] 不变，因为 d[l] += v 和 d[r+1] -= v 相互抵消
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code02_IndexTreeIntervalAddSingleQuery {

	/**
	 * 最大数据范围，根据题目要求设置
	 * 注意：这里设置为500002而不是500001，是因为区间操作时需要访问r+1的位置
	 */
	public static int MAXN = 500002;

	/**
	 * 树状数组，此处维护的是差分数组的前缀和
	 * 注意：树状数组的下标必须从1开始，不从0开始
	 */
	public static int[] tree = new int[MAXN];

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
	 * 在差分数组的位置i上增加v
	 * 时间复杂度：O(log n)
	 * 
	 * @param i 要更新的差分位置（从1开始）
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
	 * 查询原数组中位置i的值
	 * 由于树状数组维护的是差分数组的前缀和，所以这里的sum(i)直接返回原数组a[i]
	 * 时间复杂度：O(log n)
	 * 
	 * @param i 要查询的位置（从1开始）
	 * @return 原数组中位置i的值
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
		
		// 初始化数组：使用差分思想构建初始树状数组
		for (int i = 1, v; i <= n; i++) {
			in.nextToken();
			v = (int) in.nval;
			// 在位置i增加v，在位置i+1减少v（差分数组的构建方式）
			// 这相当于在原数组的位置i设置值v
			add(i, v);
			add(i + 1, -v);
		}
		
		// 处理m个操作
		for (int i = 1; i <= m; i++) {
			in.nextToken();
			int op = (int) in.nval;  // 操作类型
			
			// 判断操作类型
			if (op == 1) {
				// 操作1：区间更新
				in.nextToken(); int l = (int) in.nval;  // 区间左边界
				in.nextToken(); int r = (int) in.nval;  // 区间右边界
				in.nextToken(); int v = (int) in.nval;  // 要增加的值
				
				// 在差分数组的l位置增加v
				add(l, v);
				// 在差分数组的r+1位置减少v
				add(r + 1, -v);
			} else {
				// 操作2：单点查询
				in.nextToken();
				int index = (int) in.nval;  // 要查询的位置
				// 直接调用sum(index)获取原数组中位置index的值
				out.println(sum(index));
			}
		}
		
		// 刷新输出流并关闭资源
		out.flush();
		out.close();
		br.close();
	}

	/**
 * 区间更新+单点查询的应用场景与优缺点分析：
 * 
 * 适用场景：
 * 1. 区间加法操作，频繁查询单个元素的值
 * 2. 区间标记问题，需要统计特定位置被覆盖的次数
 * 3. 动态区间修改，静态单点查询
 * 4. 需要高效处理大量区间更新操作的场景
 * 
 * 优势：
 * - 实现简洁，代码量少
 * - 常数因子小，实际运行效率高
 * - 空间利用率好，只需一个数组
 * - 对于特定问题比线段树更高效
 * 
 * 劣势：
 * - 功能相对局限，无法直接处理区间查询
 * - 需要理解差分思想，实现门槛略高
 * - 对于复杂操作需要额外的数学转换
 * 
 * 算法进阶方向：
 * 1. 扩展到区间更新区间查询（需要维护两个树状数组）
 * 2. 与其他数据结构结合使用
 * 3. 应用到更高维度的问题中
 * 4. 处理离线问题时的优化策略
 */

	/**
 * 差分树状数组的工程实现深度解析：
 * 
 * 1. 数组初始化技术：
 *    在初始化阶段，我们通过对每个位置 i 执行 add(i, v) 和 add(i+1, -v) 操作，
 *    相当于在差分数组中为原数组的每个元素建立了正确的初始状态。
 *    这样处理的原因是原数组的初始化可以看作对区间 [i,i] 执行 v 的增量操作。
 * 
 * 2. 边界处理策略：
 *    - MAXN 设为 500002 而不是 500001，预留 r+1 的位置
 *    - 在C++实现中，tree 数组大小为 n+2，确保 r+1 不会越界
 *    - 在处理 r+1 时，即使 r+1 > n，树状数组的循环条件 (i <= n) 也会自动处理边界
 * 
 * 3. 性能优化关键点：
 *    - 数据类型选择：使用 long 或 long long 避免大数值计算时的溢出
 *    - 快速输入输出：在大规模数据情况下尤为重要
 *    - 预处理和批处理：减少函数调用和系统IO次数
 * 
 * 4. 常见错误分析：
 *    - 初始数组构建错误：未正确应用差分原理初始化
 *    - 数组越界：未为 r+1 预留空间
 *    - 整数溢出：未使用足够大的数据类型
 *    - 索引错误：混淆从0开始和从1开始的数组索引
 * 
 * 5. 与其他实现方式的对比：
 *    - 线段树：功能更强大但实现复杂，常数较大
 *    - 前缀和数组：适合静态数据，无法高效处理动态更新
 *    - 块状数组：实现简单但时间复杂度通常为O(√n)
 *    - 平衡树：实现复杂，不适合此类特定场景
 */
}

/**
 * 以下是C++实现的树状数组（Fenwick Tree）区间更新单点查询代码
 * 
 * #include <iostream>
 * #include <vector>
 * using namespace std;
 * 
 * const int MAXN = 500002; // 最大数据范围
 * 
 * class FenwickTree {
 * private:
 *     vector<long long> tree; // 使用long long防止溢出
 *     int n; // 数组长度
 * 
 *     // lowbit函数
 *     int lowbit(int x) {
 *         return x & -x;
 *     }
 * 
 * public:
 *     // 构造函数
 *     FenwickTree(int size) {
 *         n = size;
 *         tree.resize(n + 2, 0); // 多分配一个空间处理r+1的情况
 *     }
 * 
 *     // 在差分数组的位置i上增加v
 *     void add(int i, long long v) {
 *         while (i <= n) {
 *             tree[i] += v;
 *             i += lowbit(i);
 *         }
 *     }
 * 
 *     // 查询原数组中位置i的值（即差分数组的前缀和）
 *     long long query(int i) {
 *         long long ans = 0;
 *         while (i > 0) {
 *             ans += tree[i];
 *             i -= lowbit(i);
 *         }
 *         return ans;
 *     }
 * 
 *     // 区间更新：对区间[l,r]加上v
 *     void rangeAdd(int l, int r, long long v) {
 *         add(l, v);
 *         add(r + 1, -v);
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
 *     for (int i = 1, v; i <= n; i++) {
 *         cin >> v;
 *         ft.rangeAdd(i, i, v); // 单点初始化相当于区间[i,i]加v
 *     }
 * 
 *     // 处理操作
 *     for (int i = 1, op, l, r, v, idx; i <= m; i++) {
 *         cin >> op;
 *         if (op == 1) {
 *             // 区间更新
 *             cin >> l >> r >> v;
 *             ft.rangeAdd(l, r, v);
 *         } else {
 *             // 单点查询
 *             cin >> idx;
 *             cout << ft.query(idx) << '\n';
 *         }
 *     }
 * 
 *     return 0;
 * }
 */

/**
 * 以下是Python实现的树状数组（Fenwick Tree）区间更新单点查询代码
 * 
 * class FenwickTree:
 *     def __init__(self, size):
 *         self.n = size
 *         # 树状数组下标从1开始，多分配空间处理r+1的情况
 *         self.tree = [0] * (self.n + 2)
 *     
 *     # lowbit函数
 *     def lowbit(self, x):
 *         return x & -x
 *     
 *     # 在差分数组的位置i上增加v
 *     def add(self, i, v):
 *         while i <= self.n:
 *             self.tree[i] += v
 *             i += self.lowbit(i)
 *     
 *     # 查询原数组中位置i的值（即差分数组的前缀和）
 *     def query(self, i):
 *         ans = 0
 *         while i > 0:
 *             ans += self.tree[i]
 *             i -= self.lowbit(i)
 *         return ans
 *     
 *     # 区间更新：对区间[l,r]加上v
 *     def range_add(self, l, r, v):
 *         self.add(l, v)
 *         self.add(r + 1, -v)
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
 *             # 单点查询
 *             idx = int(input[ptr])
 *             ptr += 1
 *             output.append(str(ft.query(idx)))
 *     
 *     # 批量输出结果，提高效率
 *     print('\n'.join(output))
 * 
 * if __name__ == '__main__':
 *     main()
 */

/**
 * 区间更新单点查询树状数组的应用案例详解：
 * 
 * 1. 区间标记与查询问题：
 *    - 问题描述：对数组的多个区间进行标记或计数，然后查询特定位置的标记次数
 *    - 应用场景：会议预约系统、活动参与统计、资源占用分析
 *    - 解决方案：每次预约区间[l,r]时，执行rangeAdd(l, r, 1)，查询时执行query(pos)
 * 
 * 2. 离线二维范围查询：
 *    - 问题描述：在二维平面上，多次对矩形区域增加一个值，然后查询单点的值
 *    - 解决思路：扩展一维差分树状数组到二维，维护二维差分数组
 *    - 时间复杂度：O(log n * log m)，其中n和m是二维数组的维度
 * 
 * 3. 区间增量的累计查询：
 *    - 问题描述：多次对区间增加不同的值，需要频繁查询单点的当前值
 *    - 应用场景：游戏开发中的区域效果系统、金融中的区间累计收益计算
 * 
 * 4. 离线处理问题：
 *    - 问题描述：处理离线的区间更新和单点查询请求
 *    - 优化策略：对查询进行排序，批量处理同类操作
 */
