// 删除一次得到子数组最大和
// 给你一个整数数组，返回它的某个 非空 子数组（连续元素）在执行一次可选的删除操作后，
// 所能得到的最大元素总和。换句话说，你可以从原数组中选出一个子数组，并可以决定要不要
// 从中删除一个元素（只能删一次哦），（删除后）子数组中至少应当有一个元素，然后该子数组
// （剩下）的元素总和是所有子数组之中最大的。
// 注意，删除一个元素后，子数组不能为空。
// 测试链接 : https://leetcode.cn/problems/maximum-subarray-sum-with-one-deletion/
class Code07_MaximumSubarraySumWithOneDeletion {
public:
	/*
	 * 解题思路:
	 * 这是一个典型的动态规划问题。我们可以定义状态来表示在某个位置时，
	 * 在不同条件下的最大子数组和。
	 * 
	 * 状态定义:
	 * dp[i][0] 表示以 arr[i] 结尾且未删除任何元素的最大子数组和
	 * dp[i][1] 表示以 arr[i] 结尾且已删除一个元素的最大子数组和
	 * 
	 * 状态转移方程:
	 * dp[i][0] = max(arr[i], dp[i-1][0] + arr[i])
	 *   - 要么从当前元素重新开始，要么将当前元素加入之前的子数组
	 * 
	 * dp[i][1] = max(dp[i-1][0], dp[i-1][1] + arr[i])
	 *   - 要么删除当前元素(此时最大和为dp[i-1][0])，要么将当前元素加入已删除过一个元素的子数组
	 * 
	 * 最终结果:
	 * max(dp[i][0], dp[i][1]) for all i
	 * 
	 * 优化:
	 * 由于当前状态只与前一个状态有关，可以使用两个变量代替二维数组
	 * 
	 * 时间复杂度: O(n) - 需要遍历数组一次
	 * 空间复杂度: O(1) - 只需要常数个变量存储状态
	 * 
	 * 是否最优解: 是，这是该问题的最优解法
	 */

	static int maximumSum(int arr[], int n) {
		if (n == 0) {
			return 0;
		}
		
		if (n == 1) {
			return arr[0];
		}
		
		// 未删除元素时以当前位置结尾的最大子数组和
		int dp0 = arr[0];
		// 删除一个元素时以当前位置结尾的最大子数组和
		int dp1 = 0;
		// 全局最大值
		int maxSum = arr[0];
		
		// 自定义max函数
		auto myMax = [](int a, int b) -> int {
			return a > b ? a : b;
		};
		
		for (int i = 1; i < n; i++) {
			// 更新删除一个元素时的最大子数组和
			// 要么删除当前元素(值为dp0)，要么将当前元素加入之前的已删除数组
			dp1 = myMax(dp0, dp1 + arr[i]);
			
			// 更新未删除元素时的最大子数组和
			// 要么从当前元素重新开始，要么将当前元素加入之前的子数组
			dp0 = myMax(dp0 + arr[i], arr[i]);
			
			// 更新全局最大值
			maxSum = myMax(maxSum, myMax(dp0, dp1));
		}
		
		return maxSum;
	}
	
	/*
	 * 相关题目扩展:
	 * 1. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
	 * 2. LeetCode 152. 乘积最大子数组 - https://leetcode.cn/problems/maximum-product-subarray/
	 * 3. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
	 * 4. LeetCode 1186. 删除一次得到子数组最大和 - https://leetcode.cn/problems/maximum-subarray-sum-with-one-deletion/
	 * 5. LeetCode 198. 打家劫舍 - https://leetcode.cn/problems/house-robber/
	 */
};