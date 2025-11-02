package class108;

/**
 * 二维树状数组单点增加、范围查询模板
 * 
 * 本实现提供了二维平面上的高效单点更新和矩形区域查询功能，
 * 通过树状数组（Binary Indexed Tree / Fenwick Tree）数据结构实现。
 * 
 * 核心思想：
 * 二维树状数组是一维树状数组在二维平面上的扩展，每个节点维护一个矩形区域的信息，
 * 通过lowbit操作在二维空间构建树状结构，使得单点更新和区间查询的时间复杂度均为O(log n * log m)。
 * 
 * 测试链接: https://leetcode.cn/problems/range-sum-query-2d-mutable/
 * 
 * 时间复杂度分析：
 * - 构造函数(NumMatrix): O(n*m*log n*log m)，其中n和m分别是矩阵的行数和列数
 * - 单点更新(update): O(log n * log m)
 * - 区间查询(sumRegion): O(log n * log m)
 * 空间复杂度: O(n*m)，用于存储树状数组和原始数据数组
 */
public class Code04_TwoDimensionSingleAddIntervalQuery {

	/**
	 * NumMatrix类实现了二维树状数组的核心功能
	 * 支持单点更新和区域求和查询
	 */
	class NumMatrix {

		/**
		 * 二维树状数组，tree[i][j]维护特定矩形区域的信息
		 * 树状数组的索引从1开始
		 */
		public int[][] tree;

		/**
		 * 原始数值数组，存储每个位置的当前值
		 * 同样从索引1开始存储，对应树状数组的位置
		 */
		public int[][] nums;

		/**
		 * 二维数组的行数
		 */
		public int n;

		/**
		 * 二维数组的列数
		 */
		public int m;

		/**
		 * 构造函数，初始化二维树状数组
		 * 
		 * @param matrix 输入的二维矩阵，下标从0开始
		 */
		public NumMatrix(int[][] matrix) {
			n = matrix.length;
			m = matrix[0].length;
			// 初始化树状数组和原始数值数组，大小为(n+1)×(m+1)，因为索引从1开始
			tree = new int[n + 1][m + 1];
			nums = new int[n + 1][m + 1];
			// 初始化原始矩阵中的所有元素
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) {
					update(i, j, matrix[i][j]);
				}
			}
		}

		/**
		 * lowbit函数：获取数字i的二进制表示中最低位的1所对应的值
		 * 
		 * @param i 输入的整数
		 * @return i的二进制表示中最低位的1所对应的值
		 */
		private int lowbit(int i) {
			return i & -i;
		}

		/**
		 * 在树状数组的(x,y)位置增加v值
		 * 时间复杂度：O(log n * log m)
		 * 
		 * @param x 树状数组的行坐标（从1开始）
		 * @param y 树状数组的列坐标（从1开始）
		 * @param v 要增加的值
		 */
		private void add(int x, int y, int v) {
			// 遍历所有需要更新的行
			for (int i = x; i <= n; i += lowbit(i)) {
				// 遍历该行中所有需要更新的列
				for (int j = y; j <= m; j += lowbit(j)) {
					// 更新当前节点的值
					tree[i][j] += v;
				}
			}
		}

		/**
		 * 计算从树状数组(1,1)到(x,y)矩形区域的累加和
		 * 时间复杂度：O(log n * log m)
		 * 
		 * @param x 树状数组的行坐标（从1开始）
		 * @param y 树状数组的列坐标（从1开始）
		 * @return (1,1)到(x,y)矩形区域的累加和
		 */
		private int sum(int x, int y) {
			int ans = 0;
			// 遍历所有需要查询的行
			for (int i = x; i > 0; i -= lowbit(i)) {
				// 遍历该行中所有需要查询的列
				for (int j = y; j > 0; j -= lowbit(j)) {
					// 累加当前节点的值
					ans += tree[i][j];
				}
			}
			return ans;
		}

		/**
		 * 更新原始矩阵中(x,y)位置的值为v
		 * 通过计算差值来更新树状数组
		 * 
		 * @param x 原始矩阵的行坐标（从0开始）
		 * @param y 原始矩阵的列坐标（从0开始）
		 * @param v 新的值
		 */
		public void update(int x, int y, int v) {
			// 树状数组的坐标比原始矩阵多1，所以需要+1
			// 计算新旧值的差值，并更新树状数组
			add(x + 1, y + 1, v - nums[x + 1][y + 1]);
			// 更新原始数值数组中的值
			nums[x + 1][y + 1] = v;
		}

		/**
		 * 查询原始矩阵中从(a,b)到(c,d)矩形区域的累加和
		 * 利用二维前缀和的思想，通过四个前缀和的加减操作得到目标区域的和
		 * 
		 * @param a 左上区域行坐标（原始矩阵，从0开始）
		 * @param b 左上区域列坐标（原始矩阵，从0开始）
		 * @param c 右下区域行坐标（原始矩阵，从0开始）
		 * @param d 右下区域列坐标（原始矩阵，从0开始）
		 * @return (a,b)到(c,d)矩形区域的累加和
		 */
		public int sumRegion(int a, int b, int c, int d) {
			// 注意转换到树状数组的坐标
			return sum(c + 1, d + 1) - sum(a, d + 1) - sum(c + 1, b) + sum(a, b);
		}
	}

	/**
 * 二维树状数组的原理与可视化解析：
 * 
 * 1. 树状结构可视化：
 *    二维树状数组可以看作是一维树状数组在二维平面上的扩展，每个节点tree[i][j]维护一个矩形区域的和。
 *    例如，当n=4, m=4时，各节点覆盖的区域如下：
 *    - tree[4][4]: 覆盖整个4×4矩阵
 *    - tree[3][3]: 覆盖行[1-3]，列[1-3]的区域
 *    - tree[2][4]: 覆盖行[1-2]，列[1-4]的区域
 *    - tree[4][2]: 覆盖行[1-4]，列[1-2]的区域
 * 
 * 2. 区间覆盖规则：
 *    每个节点tree[i][j]维护的区间为 [i-lowbit(i)+1, i] × [j-lowbit(j)+1, j]
 *    例如，当i=10 (二进制1010)，lowbit(i)=2 (二进制10)
 *    则i-lowbit(i)+1=9，所以tree[10][j]维护行范围为[9,10]的区间
 *    这种设计保证了通过O(log n × log m)次操作即可完成更新和查询
 * 
 * 3. 单点更新传播路径：
 *    当更新点(x,y)时，所有包含该点的矩形区域都需要更新
 *    例如，更新(3,3)时，需要更新：
 *    tree[3][3], tree[4][3], tree[3][4], tree[4][4] 等节点
 *    每个方向上的更新步数均为O(log n)
 * 
 * 4. 前缀和查询原理：
 *    计算从(1,1)到(x,y)的区域和时，通过累加多个不重叠的矩形区域实现
 *    例如，查询sum(3,3)时，会累加：
 *    tree[3][3], tree[2][3], tree[3][2], tree[2][2] 等节点
 * 
 * 5. 区间查询的容斥应用：
 *    sumRegion(a,b,c,d) = sum(c,d) - sum(a-1,d) - sum(c,b-1) + sum(a-1,b-1)
 *    这利用了二维平面上的容斥原理，通过四个前缀和的组合计算出任意矩形区域的和
 */

	/**
 * 二维树状数组的高级应用与工程优化：
 * 
 * 1. 典型应用案例详解：
 *    - 案例一：二维平面统计
 *      应用场景：图像像素统计、二维区域热度图、地理信息系统中的范围查询
 *      操作模式：频繁更新少量点，多次查询不同区域的统计值
 *      性能优势：比直接遍历O(n²)更快，适用于中等规模矩阵
 * 
 *    - 案例二：二维频率统计
 *      应用场景：文本词频矩阵、多维数据统计分析
 *      优化方法：结合离散化技术处理大范围稀疏数据
 *      输入输出示例：
 *      输入：3x3矩阵初始化为0
 *           update(1,1,5) // 在位置(1,1)增加5
 *           update(2,2,3) // 在位置(2,2)增加3
 *           sumRegion(0,0,2,2) // 查询整个矩阵的和
 *      输出：8
 * 
 *    - 案例三：二维差分应用
 *      应用场景：二维区间更新问题
 *      实现思路：通过二维差分数组转换，将区间更新转化为四个角点的更新
 *      这种方法在Code05_TwoDimensionIntervalAddIntervalQuery1.java和Code05_TwoDimensionIntervalAddIntervalQuery2.java中有详细实现
 * 
 * 2. 性能优化技术：
 *    - 内存优化：对于大型稀疏矩阵，可以考虑使用稀疏矩阵表示
 *    - 数据类型优化：根据实际数据范围选择合适的数据类型，如int、long或long long
 *    - 并行处理：对于超大型矩阵，可以考虑分块并行处理
 *    - 缓存优化：调整循环顺序，提高缓存命中率
 * 
 * 3. 工程实现注意事项：
 *    - 索引转换：注意原始矩阵（从0开始）和树状数组（从1开始）的索引转换
 *    - 边界检查：实现时要确保不会越界，特别是对于接近数组边界的查询
 *    - 初始化效率：初始化时可以使用二维前缀和技术提高效率
 *    - 异常处理：添加对非法输入的检查和处理
 * 
 * 4. 与其他数据结构对比：
 *    - 二维线段树：功能更强大但实现复杂，常数较大
 *    - 二维前缀和数组：静态数据高效，但无法处理动态更新
 *    - 块状数组：实现简单但时间复杂度为O(√n × √m)
 *    - 二维平衡树：实现复杂，不适合此类特定场景
 * 
 * 5. 常见错误与调试技巧：
 *    - 索引越界：特别注意边界条件下的处理
 *    - 数组初始化错误：确保初始化时正确转换索引
 *    - 容斥计算错误：检查区间查询时四个前缀和的加减是否正确
 *    - 性能瓶颈：对于大规模数据，考虑分块或稀疏表示
 */
}

/**
 * 以下是C++实现的二维树状数组（Fenwick Tree）单点更新区域查询代码
 * 
 * #include <iostream>
 * #include <vector>
 * using namespace std;
 * 
 * class NumMatrix {
 * private:
 *     vector<vector<int>> tree;  // 二维树状数组，从索引1开始
 *     vector<vector<int>> nums;  // 原始数值数组，从索引1开始
 *     int n, m;  // 行数和列数
 * 
 *     // lowbit函数
 *     int lowbit(int i) {
 *         return i & -i;
 *     }
 * 
 *     // 在树状数组的(x,y)位置增加v值
 *     void add(int x, int y, int v) {
 *         for (int i = x; i <= n; i += lowbit(i)) {
 *             for (int j = y; j <= m; j += lowbit(j)) {
 *                 tree[i][j] += v;
 *             }
 *         }
 *     }
 * 
 *     // 计算从(1,1)到(x,y)矩形区域的累加和
 *     int sum(int x, int y) {
 *         int ans = 0;
 *         for (int i = x; i > 0; i -= lowbit(i)) {
 *             for (int j = y; j > 0; j -= lowbit(j)) {
 *                 ans += tree[i][j];
 *             }
 *         }
 *         return ans;
 *     }
 * 
 * public:
 *     // 构造函数
 *     NumMatrix(vector<vector<int>>& matrix) {
 *         if (matrix.empty() || matrix[0].empty()) {
 *             n = 0;
 *             m = 0;
 *             return;
 *         }
 *         
 *         n = matrix.size();
 *         m = matrix[0].size();
 *         
 *         // 初始化树状数组和原始数值数组，大小为(n+1)×(m+1)
 *         tree.resize(n + 1, vector<int>(m + 1, 0));
 *         nums.resize(n + 1, vector<int>(m + 1, 0));
 *         
 *         // 初始化所有元素
 *         for (int i = 0; i < n; ++i) {
 *             for (int j = 0; j < m; ++j) {
 *                 update(i, j, matrix[i][j]);
 *             }
 *         }
 *     }
 * 
 *     // 更新原始矩阵中(x,y)位置的值为v
 *     void update(int x, int y, int v) {
 *         // 计算新旧值的差值，并更新树状数组
 *         add(x + 1, y + 1, v - nums[x + 1][y + 1]);
 *         // 更新原始数值数组
 *         nums[x + 1][y + 1] = v;
 *     }
 * 
 *     // 查询从(a,b)到(c,d)矩形区域的累加和
 *     int sumRegion(int a, int b, int c, int d) {
 *         // 转换到树状数组的索引并应用容斥原理
 *         return sum(c + 1, d + 1) - sum(a, d + 1) - sum(c + 1, b) + sum(a, b);
 *     }
 * };
 * 
 * // 测试代码（可选）
 * int main() {
 *     // 示例矩阵
 *     vector<vector<int>> matrix = {
 *         {3, 0, 1, 4, 2},
 *         {5, 6, 3, 2, 1},
 *         {1, 2, 0, 1, 5},
 *         {4, 1, 0, 1, 7},
 *         {1, 0, 3, 0, 5}
 *     };
 *     
 *     NumMatrix numMatrix(matrix);
 *     
 *     // 测试查询
 *     cout << "查询[2,1,4,3]区域的和: " << numMatrix.sumRegion(2, 1, 4, 3) << endl;
 *     
 *     // 测试更新
 *     numMatrix.update(3, 2, 2);
 *     cout << "更新(3,2)为2后，查询[2,1,4,3]区域的和: " << numMatrix.sumRegion(2, 1, 4, 3) << endl;
 *     
 *     return 0;
 * }
 */

/**
 * 以下是Python实现的二维树状数组（Fenwick Tree）单点更新区域查询代码
 * 
 * class NumMatrix:
 *     def __init__(self, matrix):
 *         if not matrix or not matrix[0]:
 *             self.n = 0
 *             self.m = 0
 *             return
 *         
 *         self.n = len(matrix)
 *         self.m = len(matrix[0])
 *         
 *         # 初始化树状数组和原始数值数组，索引从1开始
 *         self.tree = [[0] * (self.m + 1) for _ in range(self.n + 1)]
 *         self.nums = [[0] * (self.m + 1) for _ in range(self.n + 1)]
 *         
 *         # 初始化所有元素
 *         for i in range(self.n):
 *             for j in range(self.m):
 *                 self.update(i, j, matrix[i][j])
 *     
 *     def _lowbit(self, i):
 *         # lowbit函数
 *         return i & -i
 *     
 *     def _add(self, x, y, v):
 *         # 在树状数组的(x,y)位置增加v值
 *         i = x
 *         while i <= self.n:
 *             j = y
 *             while j <= self.m:
 *                 self.tree[i][j] += v
 *                 j += self._lowbit(j)
 *             i += self._lowbit(i)
 *     
 *     def _sum(self, x, y):
 *         # 计算从(1,1)到(x,y)矩形区域的累加和
 *         ans = 0
 *         i = x
 *         while i > 0:
 *             j = y
 *             while j > 0:
 *                 ans += self.tree[i][j]
 *                 j -= self._lowbit(j)
 *             i -= self._lowbit(i)
 *         return ans
 *     
 *     def update(self, row, col, val):
 *         # 更新原始矩阵中(row,col)位置的值为val
 *         delta = val - self.nums[row + 1][col + 1]
 *         self._add(row + 1, col + 1, delta)
 *         self.nums[row + 1][col + 1] = val
 *     
 *     def sumRegion(self, row1, col1, row2, col2):
 *         # 查询从(row1,col1)到(row2,col2)矩形区域的累加和
 *         return (self._sum(row2 + 1, col2 + 1) - 
 *                 self._sum(row1, col2 + 1) - 
 *                 self._sum(row2 + 1, col1) + 
 *                 self._sum(row1, col1))
 * 
 * # 测试代码（可选）
 * if __name__ == "__main__":
 *     # 示例矩阵
 *     matrix = [
 *         [3, 0, 1, 4, 2],
 *         [5, 6, 3, 2, 1],
 *         [1, 2, 0, 1, 5],
 *         [4, 1, 0, 1, 7],
 *         [1, 0, 3, 0, 5]
 *     ]
 *     
 *     num_matrix = NumMatrix(matrix)
 *     
 *     # 测试查询
 *     print(f"查询[2,1,4,3]区域的和: {num_matrix.sumRegion(2, 1, 4, 3)}")
 *     
 *     # 测试更新
 *     num_matrix.update(3, 2, 2)
 *     print(f"更新(3,2)为2后，查询[2,1,4,3]区域的和: {num_matrix.sumRegion(2, 1, 4, 3)}")
 */

/**
 * 二维树状数组的高级分析与应用：
 * 
 * 1. 与其他二维数据结构的对比：
 *    - 二维线段树：二维树状数组实现更简单，常数更小，但线段树可以支持更复杂的区间操作
 *    - 二维块状数组：二维树状数组对于单点更新和范围查询更高效
 *    - 简单二维数组暴力查询：对于静态数据预处理后的前缀和查询更快，但无法高效处理动态更新
 * 
 * 2. 性能优化技巧：
 *    - 缓存热点区域的数据，减少重复查询
 *    - 对于大规模数据，考虑使用离散化技术
 *    - 使用迭代而非递归实现，减少函数调用开销
 *    - 对于频繁查询的场景，可以实现批量查询的优化
 * 
 * 3. 内存优化策略：
 *    - 仅在需要时初始化树状数组
 *    - 对于稀疏矩阵，可以考虑使用哈希表实现的树状数组
 *    - 对于非常大的矩阵，考虑分块处理或使用压缩存储
 * 
 * 4. 扩展应用：
 *    - 二维频率统计：统计某矩形区域内满足条件的元素个数
 *    - 二维逆序对：计算二维平面上的逆序对数量
 *    - 二维区间最大/最小值：通过维护最大值/最小值的树状数组实现（非标准树状数组）
 *    - 高维扩展：可以扩展到三维及以上，但性能会指数下降
 * 
 * 5. 实际应用场景：
 *    - 图像处理中的像素值统计和更新
 *    - 二维数据库中的范围查询和更新
 *    - 地理信息系统中的区域查询
 *    - 矩阵中的动态子矩阵和查询
 */
