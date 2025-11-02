package class055;

import java.util.ArrayDeque;
import java.util.Deque;

// 和至少为K的最短子数组
// 给定一个数组arr，其中的值有可能正、负、0
// 给定一个正数k
// 返回累加和>=k的所有子数组中，最短的子数组长度
// 测试链接 : https://leetcode.cn/problems/shortest-subarray-with-sum-at-least-k/
public class Code01_ShortestSubarrayWithSumAtLeastK {

	public static int MAXN = 100001;

	// sum[0] : 前0个数的前缀和
	// sum[i] : 前i个数的前缀和
	public static long[] sum = new long[MAXN];

	public static int[] deque = new int[MAXN];

	public static int h, t;

	/*
	 * 解题思路：
	 * 使用单调队列解决该问题。核心思想是利用前缀和将问题转化为寻找满足条件的两个前缀和之差。
	 * 对于前缀和数组，我们需要找到最小的 j-i，使得 sum[j] - sum[i] >= k。
	 * 为了高效查找，我们维护一个单调递增队列，队列中存储前缀和的索引。
	 *
	 * 算法步骤：
	 * 1. 计算前缀和数组
	 * 2. 遍历前缀和数组，维护单调递增队列
	 * 3. 对于每个前缀和，检查是否能与队首元素构成满足条件的子数组
	 * 4. 维护队列的单调性
	 *
	 * 时间复杂度分析：
	 * O(n) - 每个元素最多入队出队一次
	 *
	 * 空间复杂度分析：
	 * O(n) - 存储前缀和和单调队列
	 *
	 * 是否最优解：
	 * 是，这是处理此类问题的最优解法
	 */
	public static int shortestSubarray(int[] arr, int K) {
		int n = arr.length;
		for (int i = 0; i < n; i++) {
			// [3,4,5]
			//  0 1 2
			// sum[0] = 0
			// sum[1] = 3
			// sum[2] = 7
			// sum[3] = 12
			sum[i + 1] = sum[i] + arr[i];
		}
		h = t = 0;
		int ans = Integer.MAX_VALUE;
		for (int i = 0; i <= n; i++) {
			// 前0个数前缀和
			// 前1个数前缀和
			// 前2个数前缀和
			// ...
			// 前n个数前缀和
			while (h != t && sum[i] - sum[deque[h]] >= K) {
				// 如果当前的前缀和 - 头前缀和，达标！
				ans = Math.min(ans, i - deque[h++]);
			}
			// 前i个数前缀和，从尾部加入
			// 小 大
			while (h != t && sum[deque[t - 1]] >= sum[i]) {
				t--;
			}
			deque[t++] = i;
		}
		return ans != Integer.MAX_VALUE ? ans : -1;
	}

	/*
	 * C++版本实现
	 * 
	 * #include <vector>
	 * #include <deque>
	 * #include <climits>
	 * #include <algorithm>
	 * using namespace std;
	 * 
	 * class Solution {
	 * public:
	 *     int shortestSubarray(vector<int>& nums, int k) {
	 *         int n = nums.size();
	 *         vector<long long> prefixSum(n + 1, 0);
	 *         
	 *         // 计算前缀和
	 *         for (int i = 0; i < n; i++) {
	 *             prefixSum[i + 1] = prefixSum[i] + nums[i];
	 *         }
	 *         
	 *         deque<int> dq;
	 *         int result = INT_MAX;
	 *         
	 *         for (int i = 0; i <= n; i++) {
	 *             // 检查是否有满足条件的子数组
	 *             while (!dq.empty() && prefixSum[i] - prefixSum[dq.front()] >= k) {
	 *                 result = min(result, i - dq.front());
	 *                 dq.pop_front();
	 *             }
	 *             
	 *             // 维护单调递增队列
	 *             while (!dq.empty() && prefixSum[dq.back()] >= prefixSum[i]) {
	 *                 dq.pop_back();
	 *             }
	 *             
	 *             dq.push_back(i);
	 *         }
	 *         
	 *         return result != INT_MAX ? result : -1;
	 *     }
	 * };
	 */

	/*
	 * Python版本实现
	 * 
	 * from collections import deque
	 * import sys
	 * 
	 * def shortestSubarray(nums, k):
	 *     n = len(nums)
	 *     # 计算前缀和
	 *     prefix_sum = [0] * (n + 1)
	 *     for i in range(n):
	 *         prefix_sum[i + 1] = prefix_sum[i] + nums[i]
	 *     
	 *     dq = deque()
	 *     result = sys.maxsize
	 *     
	 *     for i in range(n + 1):
	 *         # 检查是否有满足条件的子数组
	 *         while dq and prefix_sum[i] - prefix_sum[dq[0]] >= k:
	 *             result = min(result, i - dq.popleft())
	 *         
	 *         # 维护单调递增队列
	 *         while dq and prefix_sum[dq[-1]] >= prefix_sum[i]:
	 *             dq.pop()
	 *         
	 *         dq.append(i)
	 *     
	 *     return result if result != sys.maxsize else -1
	 */
}