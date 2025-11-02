package class049;

import java.util.*;

/**
 * 滑动窗口最大值问题解决方案
 * 
 * 问题描述：
 * 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
 * 你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
 * 返回 滑动窗口中的最大值 。
 * 
 * 解题思路：
 * 使用双端队列实现单调队列，维护窗口中的最大值：
 * 1. 双端队列中存储数组元素的下标
 * 2. 队列保持单调递减特性，队首始终是当前窗口的最大值下标
 * 3. 遍历数组时，维护队列的单调性并及时移除窗口外的元素下标
 * 4. 当窗口形成后，队列头部元素就是当前窗口的最大值
 * 
 * 算法复杂度分析：
 * 时间复杂度: O(n) - 每个元素最多入队和出队一次
 * 空间复杂度: O(k) - 双端队列最多存储k个元素
 * 
 * 是否最优解: 是，这是处理滑动窗口最大值的最优解法
 * 
 * 相关题目链接：
 * LeetCode 239. 滑动窗口最大值
 * https://leetcode.cn/problems/sliding-window-maximum/
 * 
 * 其他平台类似题目：
 * 1. 牛客网 - 滑动窗口最大值
 *    https://www.nowcoder.com/practice/1624bc35a45c42c0bc17d17fa0cba788
 * 2. LintCode 362. 滑动窗口的最大值
 *    https://www.lintcode.com/problem/362/
 * 3. HackerRank - Sliding Window Maximum
 *    https://www.hackerrank.com/challenges/sliding-window-maximum/problem
 * 4. CodeChef - MAXSWINDOW - Maximum in Sliding Window
 *    https://www.codechef.com/problems/MAXSWINDOW
 * 5. AtCoder - ABC146 D - Enough Array
 *    https://atcoder.jp/contests/abc146/tasks/abc146_d
 * 6. 洛谷 P1886 滑动窗口
 *    https://www.luogu.com.cn/problem/P1886
 * 7. 杭电OJ 4193 Sliding Window
 *    http://acm.hdu.edu.cn/showproblem.php?pid=4193
 * 8. POJ 2823 Sliding Window
 *    http://poj.org/problem?id=2823
 * 9. UVa OJ 11536 - Smallest Sub-Array
 *    https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2531
 * 10. SPOJ - ADAFRIEN - Ada and Friends
 *     https://www.spoj.com/problems/ADAFRIEN/
 * 
 * 工程化考量：
 * 1. 异常处理：处理空数组、k为负数或0等边界情况
 * 2. 性能优化：使用单调队列避免重复计算，达到线性时间复杂度
 * 3. 可读性：变量命名清晰，添加详细注释，提供测试用例
 */
public class Code08_SlidingWindowMaximum {

	/**
	 * 计算滑动窗口中的最大值
	 * 
	 * @param nums 输入的整数数组
	 * @param k    滑动窗口的大小
	 * @return 每个滑动窗口中的最大值组成的数组
	 */
	public static int[] maxSlidingWindow(int[] nums, int k) {
		// 异常情况处理
		if (nums == null || nums.length == 0 || k <= 0) {
			return new int[0];
		}
		
		int n = nums.length;
		// 结果数组，大小为 n-k+1
		int[] result = new int[n - k + 1];
		// 双端队列，存储数组下标，队列头部是当前窗口的最大值下标
		Deque<Integer> deque = new ArrayDeque<>();
		
		// 遍历数组中的每个元素
		for (int i = 0; i < n; i++) {
			// 移除队列中超出窗口范围的下标
			// 当前窗口范围是 [i-k+1, i]，所以队首下标小于 i-k+1 的元素已经不在窗口内
			while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
				deque.pollFirst();
			}
			
			// 维护队列单调性，移除所有小于当前元素的下标
			// 保持队列单调递减，队首始终是最大值
			while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
				deque.pollLast();
			}
			
			// 将当前元素下标加入队列尾部
			deque.offerLast(i);
			
			// 当窗口形成后（i >= k-1），记录当前窗口的最大值
			// 窗口形成的条件是已经遍历了至少k个元素
			if (i >= k - 1) {
				result[i - k + 1] = nums[deque.peekFirst()];
			}
		}
		
		return result;
	}
	
	/**
	 * 测试用例
	 */
	public static void main(String[] args) {
		// 测试用例1
		int[] nums1 = {1, 3, -1, -3, 5, 3, 6, 7};
		int k1 = 3;
		int[] result1 = maxSlidingWindow(nums1, k1);
		System.out.println("输入数组: " + Arrays.toString(nums1));
		System.out.println("窗口大小: " + k1);
		System.out.println("最大值序列: " + Arrays.toString(result1));
		// 预期输出: [3, 3, 5, 5, 6, 7]
		
		// 测试用例2
		int[] nums2 = {1};
		int k2 = 1;
		int[] result2 = maxSlidingWindow(nums2, k2);
		System.out.println("\n输入数组: " + Arrays.toString(nums2));
		System.out.println("窗口大小: " + k2);
		System.out.println("最大值序列: " + Arrays.toString(result2));
		// 预期输出: [1]
	}
}