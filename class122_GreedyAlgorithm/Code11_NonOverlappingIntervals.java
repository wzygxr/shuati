package class091;

import java.util.Arrays;

// 无重叠区间
// 给定一个区间的集合 intervals ，其中 intervals[i] = [starti, endi] 。
// 返回需要移除区间的最小数量，使剩余区间互不重叠。
// 测试链接 : https://leetcode.cn/problems/non-overlapping-intervals/
public class Code11_NonOverlappingIntervals {

	/**
	 * 无重叠区间
	 * 
	 * 算法思路：
	 * 使用贪心策略：
	 * 1. 按照区间的结束位置进行升序排序
	 * 2. 贪心选择结束时间最早的区间，这样能为后续区间留出最多空间
	 * 3. 遍历排序后的区间，统计重叠的区间数量
	 * 
	 * 正确性分析：
	 * 1. 为了保留最多的区间，我们应该优先选择结束时间早的区间
	 * 2. 这样可以为后面的区间留出更多空间
	 * 3. 重叠的区间需要被移除
	 * 
	 * 时间复杂度：O(n*logn) - 主要是排序的时间复杂度
	 * 空间复杂度：O(logn) - 排序所需的额外空间
	 * 
	 * @param intervals 区间数组
	 * @return 需要移除的区间数量
	 */
	public static int eraseOverlapIntervals(int[][] intervals) {
		// 边界情况处理
		if (intervals == null || intervals.length == 0) {
			return 0;
		}
		
		// 按照区间的结束位置进行升序排序
		Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
		
		int count = 0;        // 需要移除的区间数量
		int end = intervals[0][1];  // 当前选择区间的结束位置
		
		// 从第二个区间开始遍历
		for (int i = 1; i < intervals.length; i++) {
			// 如果当前区间的开始位置小于前一个区间的结束位置，说明重叠
			if (intervals[i][0] < end) {
				// 需要移除这个区间
				count++;
			} else {
				// 更新结束位置
				end = intervals[i][1];
			}
		}
		
		// 返回需要移除的区间数量
		return count;
	}

	// 测试用例
	public static void main(String[] args) {
		// 测试用例1: intervals = [[1,2],[2,3],[3,4],[1,3]] -> 输出: 1
		int[][] intervals1 = {{1, 2}, {2, 3}, {3, 4}, {1, 3}};
		System.out.println("测试用例1:");
		System.out.println("区间数组: " + Arrays.deepToString(intervals1));
		System.out.println("需要移除的区间数量: " + eraseOverlapIntervals(intervals1)); // 期望输出: 1
		
		// 测试用例2: intervals = [[1,2],[1,2],[1,2]] -> 输出: 2
		int[][] intervals2 = {{1, 2}, {1, 2}, {1, 2}};
		System.out.println("\n测试用例2:");
		System.out.println("区间数组: " + Arrays.deepToString(intervals2));
		System.out.println("需要移除的区间数量: " + eraseOverlapIntervals(intervals2)); // 期望输出: 2
		
		// 测试用例3: intervals = [[1,2],[2,3]] -> 输出: 0
		int[][] intervals3 = {{1, 2}, {2, 3}};
		System.out.println("\n测试用例3:");
		System.out.println("区间数组: " + Arrays.deepToString(intervals3));
		System.out.println("需要移除的区间数量: " + eraseOverlapIntervals(intervals3)); // 期望输出: 0
		
		// 测试用例4: intervals = [] -> 输出: 0
		int[][] intervals4 = {};
		System.out.println("\n测试用例4:");
		System.out.println("区间数组: " + Arrays.deepToString(intervals4));
		System.out.println("需要移除的区间数量: " + eraseOverlapIntervals(intervals4)); // 期望输出: 0
	}
}