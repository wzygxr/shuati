package class092;

import java.util.Arrays;

// 使数组相似的最少操作次数
// 给你两个正整数数组 nums 和 target ，两个数组长度相等
// 在一次操作中，你可以选择两个 不同 的下标 i 和 j
// 其中 0 <= i, j < nums.length ，并且：
// 令 nums[i] = nums[i] + 2 且
// 令 nums[j] = nums[j] - 2
// 如果两个数组中每个元素出现的频率相等，我们称两个数组是 相似 的
// 请你返回将 nums 变得与 target 相似的最少操作次数
// 测试数据保证nums一定能变得与target相似
// 测试链接 : https://leetcode.cn/problems/minimum-number-of-operations-to-make-arrays-similar/
public class Code03_MinimumOperationsMakeSimilar {

	/*
	 * 贪心算法解法
	 * 
	 * 核心思想：
	 * 1. 奇数只能通过+2/-2操作变成其他奇数，偶数只能通过+2/-2操作变成其他偶数
	 * 2. 将数组按奇偶性分组，分别排序后进行匹配
	 * 3. 贪心策略：将排序后的奇数与奇数匹配，偶数与偶数匹配
	 * 4. 计算总差值，除以4得到最少操作次数
	 * 
	 * 时间复杂度：O(n log n) - 排序的时间复杂度为O(n log n)
	 * 空间复杂度：O(1) - 只使用了常数级别的额外空间
	 * 
	 * 为什么这是最优解？
	 * 1. 贪心策略保证了每一步都做出了当前看起来最好的选择
	 * 2. 通过数学方法可以证明这种策略能得到全局最优解
	 * 3. 无法在更优的时间复杂度内解决此问题，因为至少需要排序
	 * 
	 * 工程化考虑：
	 * 1. 边界条件处理：空数组、单元素数组
	 * 2. 特殊情况处理：数组中全为奇数或全为偶数的情况
	 * 3. 算法效率：利用奇偶性分组减少不必要的计算
	 * 
	 * 算法调试技巧：
	 * 1. 可以打印分组后的数组来观察算法执行过程
	 * 2. 注意处理整数溢出问题，使用long类型
	 */

	public static long makeSimilar(int[] nums, int[] target) {
		int n = nums.length;
		
		// 将数组按奇偶性分组，返回奇数部分的长度
		int oddSize = split(nums, n);
		split(target, n);
		
		// 分别对奇数部分和偶数部分进行排序
		Arrays.sort(nums, 0, oddSize);      // 对奇数部分排序
		Arrays.sort(nums, oddSize, n);      // 对偶数部分排序
		Arrays.sort(target, 0, oddSize);    // 对目标数组奇数部分排序
		Arrays.sort(target, oddSize, n);    // 对目标数组偶数部分排序
		
		long ans = 0;
		
		// 计算所有元素差值的绝对值之和
		for (int i = 0; i < n; i++) {
			ans += Math.abs((long) nums[i] - target[i]);
		}
		
		// 每次操作可以减少总差值4（一个数+2，另一个数-2），所以除以4得到操作次数
		return ans / 4;
	}

	// 把数组分割成左部分全是奇数，右部分全是偶数
	// 返回左部分的长度
	public static int split(int[] arr, int n) {
		int oddSize = 0;
		
		// 遍历数组，将奇数移到左侧
		for (int i = 0; i < n; i++) {
			// 判断是否为奇数（使用位运算提高效率）
			if ((arr[i] & 1) == 1) {
				// 将奇数交换到左侧
				swap(arr, i, oddSize++);
			}
		}
		
		return oddSize;
	}

	public static void swap(int[] arr, int i, int j) {
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

}