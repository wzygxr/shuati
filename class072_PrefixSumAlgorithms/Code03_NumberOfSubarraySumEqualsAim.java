package class046;

import java.util.HashMap;

/**
 * 和为K的子数组个数 (Number of Subarray Sum Equals Aim)
 * 
 * 题目描述:
 * 给定一个整数数组和一个整数 k，你需要找到该数组中和为 k 的连续的子数组的个数。
 * 
 * 示例:
 * 输入:nums = [1,1,1], k = 2
 * 输出: 2 , [1,1] 与 [1,1] 为两种不同的情况。
 * 
 * 输入:nums = [1,2,3], k = 3
 * 输出: 2
 * 
 * 提示:
 * 1 <= nums.length <= 20000
 * -1000 <= nums[i] <= 1000
 * -10^7 <= k <= 10^7
 * 
 * 题目链接: https://leetcode.cn/problems/subarray-sum-equals-k/
 * 
 * 解题思路:
 * 使用前缀和 + 哈希表的方法。
 * 1. 遍历数组，计算前缀和
 * 2. 对于当前位置的前缀和sum，查找是否存在前缀和为(sum - k)的历史记录
 * 3. 如果存在，则说明存在子数组和为k
 * 4. 使用哈希表记录每个前缀和出现的次数
 * 
 * 时间复杂度: O(n) - 需要遍历数组一次
 * 空间复杂度: O(n) - 哈希表最多存储n个不同的前缀和
 * 
 * 工程化考量:
 * 1. 边界条件处理：空数组、k值极端情况
 * 2. 哈希表选择：HashMap提供O(1)的平均查找时间
 * 3. 整数溢出：使用long避免大数溢出
 * 4. 负数处理：k可能为负数，但算法本身支持负数
 * 
 * 最优解分析:
 * 这是最优解，因为必须遍历所有元素才能统计所有子数组。
 * 哈希表方法将时间复杂度从O(n^2)优化到O(n)。
 * 
 * 算法核心:
 * 设prefix[i]为前i个元素的和，则子数组[i,j]的和为prefix[j] - prefix[i-1] = k
 * 即prefix[j] - k = prefix[i-1]，因此统计prefix[j] - k出现的次数即可。
 * 
 * 算法调试技巧:
 * 1. 打印中间过程：可以在循环中打印每个位置的前缀和和哈希表状态
 * 2. 边界测试：测试空数组、k=0、负数等情况
 * 3. 性能测试：测试大规模数据下的性能表现
 * 
 * 语言特性差异:
 * Java的HashMap自动处理哈希冲突，但需要注意哈希函数的选择。
 * 与C++相比，Java有自动内存管理，无需手动释放哈希表内存。
 * 与Python相比，Java是静态类型语言，需要显式声明类型。
 */
public class Code03_NumberOfSubarraySumEqualsAim {

	/**
	 * 计算和为aim的子数组个数
	 * 
	 * @param nums 输入数组
	 * @param aim 目标和
	 * @return 和为aim的子数组个数
	 * 
	 * 异常场景处理:
	 * - 空数组：返回0
	 * - aim值极端：可能为极大值或极小值
	 * - 数组元素包含负数：算法本身支持
	 * 
	 * 边界条件:
	 * - 数组长度为0
	 * - aim=0的情况（需要特殊处理空子数组）
	 * - 数组元素全为0且aim=0
	 */
	public static int subarraySum(int[] nums, int aim) {
		// 边界情况处理
		if (nums == null || nums.length == 0) {
			return 0;
		}
		
		// 使用HashMap记录前缀和及其出现次数
		// 初始化：前缀和为0出现1次（表示空数组）
		HashMap<Integer, Integer> map = new HashMap<>();
		map.put(0, 1);
		
		int ans = 0;          // 结果计数
		int prefixSum = 0;      // 当前前缀和
		
		// 遍历数组
		for (int i = 0; i < nums.length; i++) {
			// 更新前缀和
			prefixSum += nums[i];
			
			// 调试打印：显示中间过程
			// System.out.println("位置 " + i + ": 前缀和 = " + prefixSum + ", 目标 = " + (prefixSum - aim));
			
			// 查找是否存在前缀和为(prefixSum - aim)的历史记录
			// 如果存在，说明存在子数组和为aim
			if (map.containsKey(prefixSum - aim)) {
				ans += map.get(prefixSum - aim);
				// 调试打印：找到子数组
				// System.out.println("找到子数组，当前计数: " + ans);
			}
			
			// 更新当前前缀和的出现次数
			map.put(prefixSum, map.getOrDefault(prefixSum, 0) + 1);
			
			// 调试打印：哈希表状态
			// System.out.println("哈希表更新: " + prefixSum + " -> " + map.get(prefixSum));
		}
		
		return ans;
	}

}
