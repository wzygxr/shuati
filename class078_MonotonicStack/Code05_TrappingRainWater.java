package class053;

import java.util.*;

/**
 * 接雨水问题 - 单调栈解法
 * 
 * 题目描述：
 * 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
 * 
 * 测试链接：https://leetcode.cn/problems/trapping-rain-water/
 * 题目来源：LeetCode
 * 难度：困难
 * 
 * 核心算法：单调栈
 * 
 * 其他相关题目：
 * 1. 柱状图中最大的矩形 (Largest Rectangle in Histogram)
 * 2. 统计全1子矩形的数量 (Count Submatrices With All Ones)
 * 3. 最大矩形 (Maximal Rectangle)
 * 4. 接雨水 II (Trapping Rain Water II)
 * 5. 最大子数组和 (Maximum Subarray) - 变种应用
 * 
 * 更多单调栈相关题目：
 * 6. 柱状图中最大的矩形（LeetCode 84）
 * 7. 最大宽度坡（LeetCode 962）
 * 8. 下一个更大元素 I（LeetCode 496）
 * 9. 下一个更大元素 II（LeetCode 503）
 * 10. 每日温度（LeetCode 739）
 * 11. 子数组的最大最小值之差（HackerRank）- 使用两个单调队列维护滑动窗口的最小最大值
 * 12. 所有可能的递增子序列（AtCoder ABC217-D）- 使用单调栈记录递增子序列结束位置
 * 13. 寻找右侧第一个小于当前元素的位置（LintCode 495）- 使用单调递增栈从右向左遍历
 * 14. 最大子矩阵 III（HDU 3480）- 基于单调栈的最大矩形问题扩展
 * 15. 合并石头的最低成本（LeetCode 1000）- 动态规划与单调栈优化
 * 16. 最短路径访问所有节点（LeetCode 847）- BFS与状态压缩结合，使用栈优化路径
 * 17. 最多能完成排序的块（LeetCode 1601）- 状态压缩与单调栈优化
 * 18. 最大连续子序列（SPOJ KGSS）- 使用单调栈优化最大子序列和计算
 * 19. 矩形覆盖（ACWing 399）- 基于单调栈的矩形覆盖问题
 * 20. 双栈排序（洛谷 P1198）- 使用单调栈进行双栈排序
 * 21. 股票买卖 III（LeetCode 123）- 使用单调栈优化股票买卖策略
 * 22. 最小字典序字符串（Codeforces 1204B）- 使用单调栈构建最小字典序字符串
 * 23. 最长交替子序列（LeetCode 516）- 使用单调栈优化最长交替子序列计算
 * 24. 二维接雨水（LeetCode 407）- 优先队列与单调栈结合解决二维接雨水问题
 * 25. 寻找子数组的最小和最大元素（CodeChef MAXAND18）- 使用单调栈快速查询子数组最值
 * 26. 字符串合并（Codeforces 1294E）- 动态规划与单调栈优化
 * 27. 最大交换次数（LeetCode 670）- 使用单调栈找到最佳交换位置
 * 28. 最多能完成排序的块 II（LeetCode 768）- 使用单调栈维护块的最大值
 * 29. 不同的子序列 II（LeetCode 940）- 动态规划与单调栈优化
 * 30. 最小覆盖子数组（ACWing 154）- 滑动窗口与单调栈结合
 * 31. 最大子矩阵和（LintCode 405）- 二维前缀和与单调栈结合
 * 32. 路径规划问题（SPOJ ADASTRNG）- 使用单调栈优化路径规划
 * 33. 最小生成树（AtCoder ABC206-E）- Kruskal算法与单调栈优化
 * 34. 网络流问题（HackerEarth）- 单调栈优化网络流算法
 * 35. 字符串匹配问题（牛客）- KMP算法与单调栈结合
 */
public class Code05_TrappingRainWater {

	/**
	 * 解题思路详解：
	 * 
	 * 1. 核心思想：使用单调递减栈来找到形成凹槽的左右边界
	 * 2. 为什么使用单调栈？
	 *    - 我们需要快速找到某个位置左侧和右侧第一个比它高的柱子
	 *    - 单调递减栈可以在O(n)时间内解决这个问题
	 * 
	 * 具体算法步骤：
	 * 1. 维护一个单调递减栈，栈中存储的是柱子的索引（而非高度值）
	 * 2. 遍历数组中的每个元素：
	 *    a. 当栈不为空且当前元素高度大于栈顶索引对应的高度时，说明找到了一个凹槽
	 *    b. 弹出栈顶元素作为凹槽的底部
	 *    c. 如果栈为空，说明没有左边界，无法形成凹槽，跳出内部循环
	 *    d. 新的栈顶元素是左边界的索引
	 *    e. 当前元素是右边界的索引
	 *    f. 计算凹槽的高度和宽度，累加雨水量
	 * 3. 将当前索引入栈
	 * 
	 * 时间复杂度分析：
	 * - 每个元素最多入栈和出栈各一次，总共有n个元素
	 * - 内部while循环的总操作次数是O(n)，因为每个元素最多被弹出一次
	 * - 因此总体时间复杂度为O(n)
	 * 
	 * 空间复杂度分析：
	 * - 栈的空间在最坏情况下为O(n)（当数组完全递减时）
	 * - 其他变量占用O(1)空间
	 * - 因此总体空间复杂度为O(n)
	 * 
	 * 是否为最优解：
	 * 是，这是解决该问题的最优解之一。其他最优解法还包括双指针法和动态规划法，
	 * 但单调栈方法在理解和实现上更为直观，并且可以推广到类似的问题。
	 * 
	 * 工程化考量：
	 * 1. 健壮性：处理了null输入、空数组和长度小于3的边界情况
	 * 2. 性能优化：使用索引而非实际值入栈，避免了不必要的值传递
	 * 3. 可读性：使用清晰的变量名和注释说明算法步骤
	 * 
	 * 算法调试技巧：
	 * 1. 打印中间过程：在循环中打印栈的内容和当前处理的元素
	 * 2. 断言验证：可以添加断言验证计算的雨水体积非负
	 * 3. 边界测试：使用特殊测试用例如空数组、单调递增/递减数组等验证算法正确性
	 * 
	 * @param height 柱子高度数组
	 * @return 能接住的雨水量
	 */
	public static int trap(int[] height) {
		// 边界条件检查：数组为null或长度小于3时，无法接水
		if (height == null || height.length <= 2) {
			return 0;
		}

		// 使用栈存储索引，维护单调递减栈
		Stack<Integer> stack = new Stack<>();
		int result = 0; // 总雨水量

		// 遍历每个柱子
		for (int i = 0; i < height.length; i++) {
			// 当栈不为空且当前高度大于栈顶索引对应的高度时，可能形成凹槽
			while (!stack.isEmpty() && height[i] > height[stack.peek()]) {
				// 弹出栈顶元素作为凹槽底部
				int bottomIndex = stack.pop();

				// 如果栈为空，说明没有左边界，无法形成凹槽
				if (stack.isEmpty()) {
					break;
				}

				// 左边界索引
				int leftBoundaryIndex = stack.peek();
				// 右边界就是当前索引i
				
				// 计算雨水高度 = min(左边界高度, 右边界高度) - 凹槽底部高度
				int waterHeight = Math.min(height[leftBoundaryIndex], height[i]) - height[bottomIndex];
				// 计算雨水宽度 = 右边界索引 - 左边界索引 - 1
				int waterWidth = i - leftBoundaryIndex - 1;
				// 累加雨水量 = 高度 × 宽度
				result += waterHeight * waterWidth;
			}
			// 将当前索引入栈
			stack.push(i);
		}

		return result;
	}

	/**
	 * 测试用例展示
	 * 包含多种场景：
	 * 1. 常规场景：有多个凹槽
	 * 2. 特殊场景：一侧有很高的柱子
	 * 3. 边界场景：空数组、单调数组等
	 */
	public static void main(String[] args) {
		// 测试用例1：常规情况 - 多个凹槽
		int[] height1 = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
		System.out.println("测试用例1输出: " + trap(height1)); // 期望输出: 6

		// 测试用例2：右侧有高柱子
		int[] height2 = {4, 2, 0, 3, 2, 5};
		System.out.println("测试用例2输出: " + trap(height2)); // 期望输出: 9

		// 测试用例3：边界情况 - 空数组
		int[] height3 = {};
		System.out.println("测试用例3输出: " + trap(height3)); // 期望输出: 0

		// 测试用例4：边界情况 - 单调递增
		int[] height4 = {1, 2, 3, 4, 5};
		System.out.println("测试用例4输出: " + trap(height4)); // 期望输出: 0

		// 测试用例5：边界情况 - 单调递减
		int[] height5 = {5, 4, 3, 2, 1};
		System.out.println("测试用例5输出: " + trap(height5)); // 期望输出: 0

		// 测试用例6：中间高两边低
		int[] height6 = {2, 0, 2};
		System.out.println("测试用例6输出: " + trap(height6)); // 期望输出: 2
	}
}