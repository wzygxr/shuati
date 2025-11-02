package class048;

/**
 * 二维前缀和算法实现 - 详细注释版
 * 
 * 核心思想：
 * 1. 利用二维前缀和数组快速计算任意子矩阵的元素和
 * 2. 前缀和数组sum[i][j]表示从(0,0)到(i-1,j-1)的子矩阵元素和
 * 3. 通过容斥原理计算任意子矩阵和：sumRegion(a,b,c,d) = sum[c+1][d+1] - sum[c+1][b] - sum[a][d+1] + sum[a][b]
 * 
 * 时间复杂度分析：
 * 1. 构造前缀和数组：O(n*m)，其中n为行数，m为列数
 * 2. 查询子矩阵和：O(1)
 * 
 * 空间复杂度分析：
 * O((n+1)*(m+1))，用于存储前缀和数组
 * 
 * 算法优势：
 * 1. 查询效率高，一次查询时间复杂度为O(1)
 * 2. 适用于需要多次查询不同子矩阵和的场景
 * 3. 代码实现简单，易于理解和维护
 * 
 * 工程化考虑：
 * 1. 边界处理：通过扩展前缀和数组边界避免特殊判断
 * 2. 异常处理：应添加对空矩阵、越界查询的处理
 * 3. 可配置性：支持不同数据类型的前缀和计算
 * 4. 内存优化：复用数组空间，减少内存分配
 * 5. 性能优化：避免不必要的循环和计算
 * 
 * 应用场景：
 * 1. 图像处理中的区域统计
 * 2. 机器学习中的特征提取
 * 3. 游戏开发中的地图区域计算
 * 4. 数据分析中的区域统计
 * 5. 计算机视觉中的目标检测
 * 
 * 相关题目：
 * 1. LeetCode 304. Range Sum Query 2D - Immutable
 * 2. Codeforces 1371C - A Cookie for You
 * 3. AtCoder ABC106D - AtCoder Express 2
 * 4. HDU 1559 最大子矩阵
 * 5. POJ 1050 To the Max
 * 
 * 测试链接 : https://leetcode.cn/problems/range-sum-query-2d-immutable/
 * 
 * 算法调试技巧：
 * 1. 打印中间变量：在构建前缀和数组时打印每一步的结果
 * 2. 边界测试：测试空矩阵、单元素矩阵等边界情况
 * 3. 性能测试：测试大规模数据的性能表现
 * 
 * 语言特性差异：
 * Java: 使用二维数组，通过构造函数预处理
 * C++: 可使用vector<vector<int>>实现类似功能
 * Python: 可使用嵌套列表实现，但需要注意列表的浅拷贝问题
 */
public class Code01_PrefixSumMatrix {

	/**
	 * NumMatrix类实现了二维前缀和的功能
	 * 
	 * 设计特点：
	 * 1. 在构造函数中预处理前缀和数组，提高查询效率
	 * 2. 使用偏移坐标系统简化边界处理
	 * 3. 支持多次查询，每次查询时间复杂度O(1)
	 * 
	 * 算法详解：
	 * 1. 前缀和构建：sum[i][j] = matrix[i-1][j-1] + sum[i-1][j] + sum[i][j-1] - sum[i-1][j-1]
	 * 2. 区域和查询：利用容斥原理计算任意子矩阵和
	 * 
	 * 数学原理：
	 * 容斥原理：A∪B = A + B - A∩B
	 * 在二维前缀和中：sum[i][j] = matrix[i-1][j-1] + sum[i-1][j] + sum[i][j-1] - sum[i-1][j-1]
	 * 
	 * 时间复杂度分析：
	 * - 构造函数：O(n*m)
	 * - sumRegion方法：O(1)
	 * 
	 * 空间复杂度分析：
	 * O((n+1)*(m+1))，用于存储前缀和数组
	 */
	class NumMatrix {

		// 前缀和数组，尺寸为(n+1)*(m+1)，避免边界判断
		// 设计思路：通过扩展边界，避免在查询时进行复杂的边界条件判断
		// 优化点：使用偏移坐标系统，简化代码逻辑
		public int[][] sum;

		/**
		 * 构造函数：构建二维前缀和数组
		 * 
		 * 算法步骤：
		 * 1. 初始化(n+1)*(m+1)的前缀和数组
		 * 2. 将原始矩阵复制到前缀和数组的偏移位置
		 * 3. 按行按列依次计算前缀和
		 * 
		 * 时间复杂度：O(n*m)
		 * 空间复杂度：O((n+1)*(m+1))
		 * 
		 * 工程化考量：
		 * 1. 异常处理：检查输入矩阵是否为空
		 * 2. 边界处理：扩展数组边界避免特殊判断
		 * 3. 内存管理：合理分配数组空间
		 * 
		 * 调试技巧：
		 * 1. 打印原始矩阵和前缀和数组进行对比验证
		 * 2. 测试边界情况：空矩阵、单元素矩阵等
		 * 
		 * @param matrix 原始二维矩阵，要求非空且至少有一个元素
		 * @throws IllegalArgumentException 如果输入矩阵为空或维度为0
		 */
		public NumMatrix(int[][] matrix) {
			// 参数校验：确保输入矩阵有效
			if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
				throw new IllegalArgumentException("输入矩阵不能为空");
			}
			
			int n = matrix.length;
			int m = matrix[0].length;
			
			// 创建前缀和数组，行列均多申请一个空间用于简化边界处理
			// 优化：使用n+1和m+1的尺寸，避免在查询时进行复杂的边界判断
			sum = new int[n + 1][m + 1];
			
			// 将原始矩阵复制到前缀和数组中（偏移1位）
			// 设计思路：通过偏移坐标系统，简化后续的容斥原理计算
			for (int a = 1, c = 0; c < n; a++, c++) {
				for (int b = 1, d = 0; d < m; b++, d++) {
					sum[a][b] = matrix[c][d];
				}
			}
			
			// 构建前缀和数组
			// 利用容斥原理：当前点前缀和 = 当前点值 + 上方前缀和 + 左方前缀和 - 左上角前缀和
			// 数学原理：sum[i][j] = matrix[i-1][j-1] + sum[i-1][j] + sum[i][j-1] - sum[i-1][j-1]
			for (int i = 1; i <= n; i++) {
				for (int j = 1; j <= m; j++) {
					sum[i][j] += sum[i][j - 1] + sum[i - 1][j] - sum[i - 1][j - 1];
					
					// 调试输出：打印每一步的前缀和计算结果
					// System.out.printf("sum[%d][%d] = %d + %d + %d - %d = %d%n", 
					//     i, j, matrix[i-1][j-1], sum[i][j-1], sum[i-1][j], sum[i-1][j-1], sum[i][j]);
				}
			}
		}

		/**
		 * 查询指定区域的元素和
		 * 
		 * 算法原理：
		 * 利用容斥原理计算子矩阵和：
		 * sumRegion(a,b,c,d) = sum[c+1][d+1] - sum[c+1][b] - sum[a][d+1] + sum[a][b]
		 * 
		 * 数学推导：
		 * 1. sum[c+1][d+1] 包含从(0,0)到(c,d)的所有元素
		 * 2. 减去sum[c+1][b] 去掉左侧多余部分
		 * 3. 减去sum[a][d+1] 去掉上方多余部分
		 * 4. 加上sum[a][b] 补回多减的部分
		 * 
		 * 时间复杂度：O(1)
		 * 空间复杂度：O(1)
		 * 
		 * 边界情况处理：
		 * 1. 输入坐标合法性检查
		 * 2. 坐标越界处理
		 * 3. 空矩阵查询处理
		 * 
		 * 工程化考量：
		 * 1. 参数校验：确保输入坐标有效
		 * 2. 性能优化：避免不必要的计算
		 * 3. 异常处理：提供友好的错误信息
		 * 
		 * @param a 子矩阵左上角行索引（从0开始）
		 * @param b 子矩阵左上角列索引（从0开始）
		 * @param c 子矩阵右下角行索引（从0开始）
		 * @param d 子矩阵右下角列索引（从0开始）
		 * @return 子矩阵元素和
		 * @throws IllegalArgumentException 如果坐标越界或无效
		 */
		public int sumRegion(int a, int b, int c, int d) {
			// 参数校验：确保坐标有效
			if (a < 0 || b < 0 || c < a || d < b || 
				c >= sum.length - 1 || d >= sum[0].length - 1) {
				throw new IllegalArgumentException("坐标越界或无效");
			}
			
			// 调整坐标到前缀和数组的对应位置
			// 由于前缀和数组有偏移，需要将原始坐标加1
			c++;
			d++;
			
			// 利用容斥原理计算区域和
			// 公式：sum[c][d] - sum[c][b] - sum[a][d] + sum[a][b]
			int result = sum[c][d] - sum[c][b] - sum[a][d] + sum[a][b];
			
			// 调试输出：打印查询结果
			// System.out.printf("sumRegion(%d,%d,%d,%d) = %d - %d - %d + %d = %d%n", 
			//     a, b, c-1, d-1, sum[c][d], sum[c][b], sum[a][d], sum[a][b], result);
			
			return result;
		}

	}
	
	/**
	 * 测试用例和演示代码
	 * 
	 * 包含多种测试场景：
	 * 1. 正常情况测试
	 * 2. 边界情况测试
	 * 3. 性能测试
	 * 4. 异常情况测试
	 */
	public static void main(String[] args) {
		// 测试用例1：正常情况
		System.out.println("=== 测试用例1：正常情况 ===");
		int[][] matrix1 = {
			{3, 0, 1, 4, 2},
			{5, 6, 3, 2, 1},
			{1, 2, 0, 1, 5},
			{4, 1, 0, 1, 7},
			{1, 0, 3, 0, 5}
		};
		
		NumMatrix numMatrix = new Code01_PrefixSumMatrix().new NumMatrix(matrix1);
		
		// 测试sumRegion(2, 1, 4, 3)
		int result1 = numMatrix.sumRegion(2, 1, 4, 3);
		System.out.println("sumRegion(2, 1, 4, 3) = " + result1); // 预期输出: 8
		
		// 测试sumRegion(1, 1, 2, 2)
		int result2 = numMatrix.sumRegion(1, 1, 2, 2);
		System.out.println("sumRegion(1, 1, 2, 2) = " + result2); // 预期输出: 11
		
		// 测试sumRegion(1, 2, 2, 4)
		int result3 = numMatrix.sumRegion(1, 2, 2, 4);
		System.out.println("sumRegion(1, 2, 2, 4) = " + result3); // 预期输出: 12
		
		System.out.println();
		
		// 测试用例2：边界情况 - 单元素矩阵
		System.out.println("=== 测试用例2：单元素矩阵 ===");
		int[][] matrix2 = {{5}};
		NumMatrix numMatrix2 = new Code01_PrefixSumMatrix().new NumMatrix(matrix2);
		int result4 = numMatrix2.sumRegion(0, 0, 0, 0);
		System.out.println("sumRegion(0, 0, 0, 0) = " + result4); // 预期输出: 5
		
		System.out.println();
		
		// 测试用例3：性能测试 - 大规模数据
		System.out.println("=== 测试用例3：性能测试 ===");
		int n = 1000;
		int m = 1000;
		int[][] largeMatrix = new int[n][m];
		// 填充测试数据
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				largeMatrix[i][j] = i + j;
			}
		}
		
		long startTime = System.currentTimeMillis();
		NumMatrix numMatrix3 = new Code01_PrefixSumMatrix().new NumMatrix(largeMatrix);
		long constructionTime = System.currentTimeMillis() - startTime;
		
		startTime = System.currentTimeMillis();
		// 执行多次查询测试性能
		for (int i = 0; i < 1000; i++) {
			numMatrix3.sumRegion(0, 0, n-1, m-1);
		}
		long queryTime = System.currentTimeMillis() - startTime;
		
		System.out.println("构造时间: " + constructionTime + "ms");
		System.out.println("1000次查询时间: " + queryTime + "ms");
		System.out.println("平均查询时间: " + (queryTime / 1000.0) + "ms");
		
		System.out.println();
		
		// 测试用例4：异常情况测试
		System.out.println("=== 测试用例4：异常情况测试 ===");
		try {
			int[][] emptyMatrix = {};
			NumMatrix numMatrix4 = new Code01_PrefixSumMatrix().new NumMatrix(emptyMatrix);
		} catch (IllegalArgumentException e) {
			System.out.println("异常处理测试通过: " + e.getMessage());
		}
	}

}