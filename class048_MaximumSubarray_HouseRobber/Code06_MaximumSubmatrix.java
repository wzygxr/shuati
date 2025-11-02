package class070;

import java.util.Arrays;

/**
 * 子矩阵最大累加和问题
 * 题目描述：给定一个二维数组grid，找到其中子矩阵的最大累加和，返回拥有最大累加和的子矩阵左上角和右下角坐标
 * 如果有多个子矩阵都有最大累加和，返回哪一个都可以
 * 测试链接：https://leetcode.cn/problems/max-submatrix-lcci/
 * 
 * 算法核心思想：
 * 1. 将二维问题转换为一维问题：通过枚举子矩阵的上下边界，将二维矩阵压缩成一维数组
 * 2. 对于每一对上下边界，计算每一列的累加和，形成一个一维数组
 * 3. 对这个一维数组应用Kadane算法的变种，同时记录最大子数组的起始和结束位置
 * 4. 根据这些信息，确定二维子矩阵的四个边界坐标
 * 
 * 时间复杂度：O(n² * m)，其中n是矩阵的行数，m是矩阵的列数
 * 空间复杂度：O(m)，用于存储压缩后的一维数组
 */
public class Code06_MaximumSubmatrix {

	/**
	 * 寻找拥有最大累加和的子矩阵，并返回其左上角和右下角坐标
	 * 
	 * @param grid 输入的二维数组
	 * @return 长度为4的数组，依次为左上角行、列坐标和右下角行、列坐标
	 */
	public static int[] getMaxMatrix(int[][] grid) {
		// 边界检查
		if (grid == null || grid.length == 0 || grid[0].length == 0) {
			return new int[] {0, 0, 0, 0}; // 返回默认坐标
		}
		
		int n = grid.length;      // 矩阵的行数
		int m = grid[0].length;   // 矩阵的列数
		int max = Integer.MIN_VALUE; // 记录全局最大子矩阵和
		int a = 0, b = 0;         // 最大子矩阵左上角坐标(a, b)
		int c = 0, d = 0;         // 最大子矩阵右下角坐标(c, d)
		int[] nums = new int[m];  // 用于存储每一列的累加和
		
		// 枚举子矩阵的上边界（行）
		for (int up = 0; up < n; up++) {
			// 重置累加数组为0，准备下一轮计算
			Arrays.fill(nums, 0);
			
			// 枚举子矩阵的下边界（行）
			for (int down = up; down < n; down++) {
				// 这部分实现了Kadane算法的变种，同时记录子数组的起始和结束位置
				int pre = Integer.MIN_VALUE; // 当前子数组和
				int left = 0;  // 当前子数组的左边界
				
				// 从左到右遍历每一列
				for (int right = 0; right < m; right++) {
					// 将当前行的第right列元素累加到nums数组中
					nums[right] += grid[down][right];
					
					// Kadane算法核心逻辑：如果之前的和为正，则继续累加；否则，重新开始
					if (pre >= 0) {
						pre += nums[right];
					} else {
						pre = nums[right];
						left = right; // 重新开始的子数组左边界就是当前位置
					}
					
					// 如果找到更大的子数组和，更新全局最大值和边界坐标
					if (pre > max) {
						max = pre;
						a = up;    // 上边界为当前枚举的up
						b = left;  // 左边界为当前子数组的left
						c = down;  // 下边界为当前枚举的down
						d = right; // 右边界为当前的right
					}
				}
			}
		}
		
		return new int[] { a, b, c, d };
	}
	
	/**
	 * 计算子矩阵的最大累加和（返回和值而非坐标）
	 * 这是对getMaxMatrix方法的补充，用于只需要和值的场景
	 * 
	 * @param grid 输入的二维数组
	 * @return 子矩阵的最大累加和
	 */
	public static int maxSubmatrixSum(int[][] grid) {
		if (grid == null || grid.length == 0 || grid[0].length == 0) {
			return 0;
		}
		
		int n = grid.length;
		int m = grid[0].length;
		int max = Integer.MIN_VALUE;
		int[] nums = new int[m];
		
		for (int up = 0; up < n; up++) {
			Arrays.fill(nums, 0);
			for (int down = up; down < n; down++) {
				int pre = Integer.MIN_VALUE;
				for (int right = 0; right < m; right++) {
					nums[right] += grid[down][right];
					pre = pre >= 0 ? pre + nums[right] : nums[right];
					max = Math.max(max, pre);
				}
			}
		}
		
		return max;
	}
	
	/**
	 * 主函数用于测试
	 */
	public static void main(String[] args) {
		// 测试用例1: 简单矩阵
		int[][] test1 = {
				{1, -2, 3},
				{4, -5, 6},
				{7, -8, 9}
		};
		int[] result1 = getMaxMatrix(test1);
		System.out.println("测试用例1坐标: [" + result1[0] + "," + result1[1] + "," + result1[2] + "," + result1[3] + "]");
		System.out.println("测试用例1最大和: " + maxSubmatrixSum(test1));
		
		// 测试用例2: 全是负数的矩阵
		int[][] test2 = {
				{-1, -2, -3},
				{-4, -5, -6},
				{-7, -8, -9}
		};
		int[] result2 = getMaxMatrix(test2);
		System.out.println("测试用例2坐标: [" + result2[0] + "," + result2[1] + "," + result2[2] + "," + result2[3] + "]");
		System.out.println("测试用例2最大和: " + maxSubmatrixSum(test2));
		
		// 测试用例3: 全是正数的矩阵
		int[][] test3 = {
				{1, 2, 3},
				{4, 5, 6},
				{7, 8, 9}
		};
		int[] result3 = getMaxMatrix(test3);
		System.out.println("测试用例3坐标: [" + result3[0] + "," + result3[1] + "," + result3[2] + "," + result3[3] + "]");
		System.out.println("测试用例3最大和: " + maxSubmatrixSum(test3));
	}
	
	/*
	 * 扩展思考：
	 * 1. 为什么这种方法是最优的？
	 *    - 对于n行m列的矩阵，时间复杂度为O(n² * m)，这已经是目前已知的最优算法之一
	 *    - 当矩阵的行数远大于列数时，可以交换行列，使算法更高效
	 * 
	 * 2. 本题的变体：
	 *    - 矩形区域不超过K的最大数值和（LeetCode 363）
	 *    - 元素和为目标值的子矩阵数量（LeetCode 1074）
	 *    - 统计全为1的正方形子矩阵（LeetCode 1277）
	 * 
	 * 3. 工程应用场景：
	 *    - 图像处理：寻找图像中最亮或最暗的区域
	 *    - 数据分析：在二维数据中寻找具有特定性质的子区域
	 *    - 金融分析：分析二维时间序列数据中的模式
	 */
	
	/*
	 * Python实现参考：
	 '''
	 def getMaxMatrix(grid):
	     if not grid or not grid[0]:
	         return [0, 0, 0, 0]
	     
	     n, m = len(grid), len(grid[0])
	     max_sum = float('-inf')
	     a, b, c, d = 0, 0, 0, 0
	     nums = [0] * m
	     
	     for up in range(n):
	         # 重置累加数组
	         nums = [0] * m
	         for down in range(up, n):
	             pre = float('-inf')
	             left = 0
	             for right in range(m):
	                 # 累加当前行的元素
	                 nums[right] += grid[down][right]
	                 
	                 # Kadane算法核心逻辑
	                 if pre >= 0:
	                     pre += nums[right]
	                 else:
	                     pre = nums[right]
	                     left = right
	                 
	                 # 更新最大和及其位置
	                 if pre > max_sum:
	                     max_sum = pre
	                     a, b, c, d = up, left, down, right
	     
	     return [a, b, c, d]
	 
	 def maxSubmatrixSum(grid):
	     if not grid or not grid[0]:
	         return 0
	     
	     n, m = len(grid), len(grid[0])
	     max_sum = float('-inf')
	     nums = [0] * m
	     
	     for up in range(n):
	         nums = [0] * m
	         for down in range(up, n):
	             pre = float('-inf')
	             for right in range(m):
	                 nums[right] += grid[down][right]
	                 pre = pre + nums[right] if pre >= 0 else nums[right]
	                 max_sum = max(max_sum, pre)
	     
	     return max_sum
	 '''
	 
	 * C++实现参考：
	 '''
	 #include <vector>
	 #include <climits>
	 #include <algorithm>
	 
	 std::vector<int> getMaxMatrix(std::vector<std::vector<int>>& grid) {
	     if (grid.empty() || grid[0].empty()) {
	         return {0, 0, 0, 0};
	     }
	     
	     int n = grid.size();
	     int m = grid[0].size();
	     int max_sum = INT_MIN;
	     int a = 0, b = 0, c = 0, d = 0;
	     std::vector<int> nums(m, 0);
	     
	     for (int up = 0; up < n; ++up) {
	         // 重置累加数组
	         std::fill(nums.begin(), nums.end(), 0);
	         for (int down = up; down < n; ++down) {
	             int pre = INT_MIN;
	             int left = 0;
	             
	             for (int right = 0; right < m; ++right) {
	                 // 累加当前行的元素
	                 nums[right] += grid[down][right];
	                 
	                 // Kadane算法核心逻辑
	                 if (pre >= 0) {
	                     pre += nums[right];
	                 } else {
	                     pre = nums[right];
	                     left = right;
	                 }
	                 
	                 // 更新最大和及其位置
	                 if (pre > max_sum) {
	                     max_sum = pre;
	                     a = up;
	                     b = left;
	                     c = down;
	                     d = right;
	                 }
	             }
	         }
	     }
	     
	     return {a, b, c, d};
	 }
	 
	 int maxSubmatrixSum(std::vector<std::vector<int>>& grid) {
	     if (grid.empty() || grid[0].empty()) {
	         return 0;
	     }
	     
	     int n = grid.size();
	     int m = grid[0].size();
	     int max_sum = INT_MIN;
	     std::vector<int> nums(m, 0);
	     
	     for (int up = 0; up < n; ++up) {
	         std::fill(nums.begin(), nums.end(), 0);
	         for (int down = up; down < n; ++down) {
	             int pre = INT_MIN;
	             for (int right = 0; right < m; ++right) {
	                 nums[right] += grid[down][right];
	                 pre = (pre >= 0) ? (pre + nums[right]) : nums[right];
	                 max_sum = std::max(max_sum, pre);
	             }
	         }
	     }
	     
	     return max_sum;
	 }
	 '''
	 */
	
	/*
	 * 相关题目扩展:
	 * 1. LeetCode 面试题 17.24. 最大子矩阵 - https://leetcode.cn/problems/max-submatrix-lcci/
	 * 2. LeetCode 304. 二维区域和检索 - 矩阵不可变 - https://leetcode.cn/problems/range-sum-query-2d-immutable/
	 * 3. LeetCode 363. 矩形区域不超过 K 的最大数值和 - https://leetcode.cn/problems/max-sum-of-rectangle-no-larger-than-k/
	 * 4. LeetCode 1074. 元素和为目标值的子矩阵数量 - https://leetcode.cn/problems/number-of-submatrices-that-sum-to-target/
	 * 5. LeetCode 1277. 统计全为 1 的正方形子矩阵 - https://leetcode.cn/problems/count-square-submatrices-with-all-ones/
	 * 6. LeetCode 1504. 统计全 1 子矩形 - https://leetcode.cn/problems/count-submatrices-with-all-ones/
	 * 7. LeetCode 1292. 元素和小于等于阈值的正方形的最大边长 - https://leetcode.cn/problems/maximum-side-length-of-a-square-with-sum-less-than-or-equal-to-threshold/
	 * 8. LintCode 944. 最大子矩阵 - https://www.lintcode.com/problem/944/
	 */
}