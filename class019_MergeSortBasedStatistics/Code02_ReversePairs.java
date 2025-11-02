package class022;

// 翻转对数量
// 测试链接 : https://leetcode.cn/problems/reverse-pairs/
/**
 * ============================================================================
 * 题目2: 翻转对 (Reverse Pairs)
 * ============================================================================
 * 
 * 题目来源: LeetCode 493
 * 题目链接: https://leetcode.cn/problems/reverse-pairs/
 * 难度级别: 困难
 * 
 * 问题描述:
 * 给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] ，我们就将 (i,j) 称作一个翻转对。
 * 你需要返回数组中的翻转对的数量。
 * 
 * 示例输入输出:
 * 输入: [1,3,2,3,1]
 * 输出: 2
 * 解释:
 * (1,4) -> 3 > 2*1
 * (3,4) -> 3 > 2*1
 * 
 * 输入: [2,4,3,5,1]
 * 输出: 3
 * 解释:
 * (1,4) -> 4 > 2*1
 * (2,4) -> 3 > 2*1
 * (3,4) -> 5 > 2*1
 * 
 * ============================================================================
 * 核心算法思想: 归并排序分治统计
 * ============================================================================
 * 
 * 方法1: 暴力解法 (不推荐)
 * - 思路: 双重循环遍历所有 i < j 的情况，判断 nums[i] > 2*nums[j]
 * - 时间复杂度: O(N^2) - 双重循环
 * - 空间复杂度: O(1) - 不需要额外空间
 * - 问题: 数据量大时超时
 * 
 * 方法2: 归并排序思想 (最优解) ★★★★★
 * - 核心洞察: 利用归并排序的分治过程，在合并两个有序子数组之前，
 *   统计左侧子数组中满足 nums[i] > 2*nums[j] 的元素对数量
 * 
 * - 归并排序过程:
 *   1. 分治: 将数组不断二分，直到只有一个元素
 *   2. 统计: 在合并前，统计左侧子数组中每个元素能与右侧子数组形成的翻转对数量
 *   3. 合并: 合并两个有序子数组
 * 
 * - 优化技巧:
 *   - 由于左右子数组已经各自有序，可以使用双指针技巧高效统计
 *   - 对于左侧子数组的每个元素nums[i]，找到右侧子数组中最大的j，使得 nums[j] < nums[i]/2
 *   - 这样，右侧子数组中从start到j的元素都可以与nums[i]形成翻转对
 * 
 * - 时间复杂度详细计算:
 *   T(n) = 2T(n/2) + O(n)  [Master定理 case 2]
 *   = O(n log n)
 *   - 递归深度: log n
 *   - 每层统计和合并: O(n)
 * 
 * - 空间复杂度详细计算:
 *   S(n) = O(n) + O(log n)
 *   - O(n): 辅助数组help
 *   - O(log n): 递归调用栈
 *   总计: O(n)
 * 
 * - 是否最优解: ★ 是 ★
 *   理由: 基于比较的算法下界为O(n log n)，本算法已达到最优
 * 
 * ============================================================================
 * 相关题目列表 (基于归并排序的统计问题)
 * ============================================================================
 * 1. LeetCode 315 - 计算右侧小于当前元素的个数
 *    https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
 *    问题：统计每个元素右侧比它小的元素个数
 *    解法：归并排序过程中记录元素原始索引，统计右侧小于当前元素的数量
 * 
 * 2. LeetCode 327 - 区间和的个数
 *    https://leetcode.cn/problems/count-of-range-sum/
 *    问题：统计区间和在[lower, upper]范围内的区间个数
 *    解法：前缀和+归并排序，统计满足条件的前缀和对
 * 
 * 3. 剑指Offer 51 / LCR 170 - 数组中的逆序对
 *    https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
 *    问题：统计数组中逆序对的总数
 *    解法：归并排序过程中统计逆序对数量
 * 
 * 4. POJ 2299 - Ultra-QuickSort
 *    http://poj.org/problem?id=2299
 *    问题：计算将数组排序所需的最小交换次数（即逆序对数量）
 *    解法：归并排序统计逆序对
 * 
 * 5. HDU 1394 - Minimum Inversion Number
 *    http://acm.hdu.edu.cn/showproblem.php?pid=1394
 *    问题：将数组循环左移，求所有可能排列中的最小逆序对数量
 *    解法：归并排序+逆序对性质分析
 * 
 * 6. 洛谷 P1908 - 逆序对
 *    https://www.luogu.com.cn/problem/P1908
 *    问题：统计数组中逆序对的总数
 *    解法：归并排序统计逆序对
 * 
 * 7. HackerRank - Merge Sort: Counting Inversions
 *    https://www.hackerrank.com/challenges/merge-sort/problem
 *    问题：统计逆序对数量
 *    解法：归并排序统计逆序对
 * 
 * 8. SPOJ - INVCNT
 *    https://www.spoj.com/problems/INVCNT/
 *    问题：统计逆序对数量
 *    解法：归并排序统计逆序对
 * 
 * 9. CodeChef - INVCNT
 *    https://www.codechef.com/problems/INVCNT
 *    问题：统计逆序对数量
 *    解法：归并排序或树状数组
 * 
 * 10. UVa 10810 - Ultra-QuickSort
 *     https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1751
 *     问题：计算逆序对数量
 *     解法：归并排序统计逆序对
 * 
 * 11. LeetCode 2751 - Robot Collisions
 *     https://leetcode.cn/problems/robot-collisions/
 *     问题：机器人碰撞问题，可使用归并思想分析碰撞顺序
 * 
 * 12. LeetCode 406 - Queue Reconstruction by Height
 *     https://leetcode.cn/problems/queue-reconstruction-by-height/
 *     问题：根据身高重建队列，可使用类似归并的分治思想
 * 
 * 13. LeetCode 88. Merge Sorted Array
 *     https://leetcode.cn/problems/merge-sorted-array/
 *     问题：合并两个有序数组
 *     解法：归并排序的合并步骤应用
 * 
 * 14. LeetCode 23. Merge k Sorted Lists
 *     https://leetcode.cn/problems/merge-k-sorted-lists/
 *     问题：合并K个有序链表
 *     解法：多路归并（归并排序的扩展）
 * 
 * 15. LeetCode 56. Merge Intervals
 *     https://leetcode.cn/problems/merge-intervals/
 *     问题：合并重叠区间
 *     解法：排序后合并（归并思想的应用）
 * 
 * 16. HackerEarth - Merge Sort Variations
 *     https://www.hackerearth.com/practice/algorithms/sorting/merge-sort/practice-problems/
 *     问题：归并排序的各种变体和应用
 * 
 * 17. 杭电多校赛 - 各种归并排序应用问题
 *     问题：竞赛中的归并排序应用题目
 * 
 * 18. Codeforces - Various Merge Sort Applications
 *     https://codeforces.com/problemset/tags/merge-sort
 *     问题：Codeforces上的归并排序应用题目
 * 
 * 19. AtCoder - Merge Sort Problems
 *     问题：AtCoder上的归并排序相关题目
 * 
 * 20. USACO -归并排序应用
 *     问题：USACO竞赛中的归并排序应用
 * 
 * 这些题目虽然具体形式不同，但核心思想都是利用归并排序的分治特性和合并过程，高效统计满足特定条件的元素对数量。对于翻转对问题，关键在于理解如何在归并排序的过程中，利用双指针技巧高效统计满足 nums[i] > 2*nums[j] 的元素对。
 */
public class Code02_ReversePairs {

	public static int MAXN = 50001;

	public static int[] help = new int[MAXN];

	public static int reversePairs(int[] arr) {
		return counts(arr, 0, arr.length - 1);
	}

	// 统计l...r范围上，翻转对的数量，同时l...r范围统计完后变有序
	// 时间复杂度O(n * logn)
	public static int counts(int[] arr, int l, int r) {
		if (l == r) {
			return 0;
		}
		int m = (l + r) / 2;
		return counts(arr, l, m) + counts(arr, m + 1, r) + merge(arr, l, m, r);
	}

	public static int merge(int[] arr, int l, int m, int r) {
		// 统计部分
		int ans = 0;
		for (int i = l, j = m + 1; i <= m; i++) {
			while (j <= r && (long) arr[i] > (long) arr[j] * 2) {
				j++;
			}
			ans += j - m - 1;
		}
		// 正常merge
		int i = l;
		int a = l;
		int b = m + 1;
		while (a <= m && b <= r) {
			help[i++] = arr[a] <= arr[b] ? arr[a++] : arr[b++];
		}
		while (a <= m) {
			help[i++] = arr[a++];
		}
		while (b <= r) {
			help[i++] = arr[b++];
		}
		for (i = l; i <= r; i++) {
			arr[i] = help[i];
		}
		return ans;
	}

}