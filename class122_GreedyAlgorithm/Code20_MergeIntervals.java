package class091;

import java.util.Arrays;

// 合并区间
// 以数组 intervals 表示若干个区间的集合，其中单个区间为 intervals[i] = [starti, endi] 。
// 请你合并所有重叠的区间，并返回一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间。
// 测试链接 : https://leetcode.cn/problems/merge-intervals/
public class Code20_MergeIntervals {

	/**
	 * 合并区间
	 * 
	 * 算法思路：
	 * 使用贪心策略：
	 * 1. 按照区间的开始位置进行升序排序
	 * 2. 遍历排序后的区间：
	 *    - 如果当前区间与前一个区间重叠，合并它们
	 *    - 否则将前一个区间加入结果集
	 * 
	 * 正确性分析：
	 * 1. 按开始位置排序后，重叠的区间会相邻
	 * 2. 贪心选择：尽可能合并重叠区间
	 * 3. 合并后的区间能覆盖所有被合并的区间
	 * 
	 * 时间复杂度：O(n*logn) - 主要是排序的时间复杂度
	 * 空间复杂度：O(logn) - 排序所需的额外空间
	 * 
	 * @param intervals 区间数组
	 * @return 合并后的区间数组
	 */
	public static int[][] merge(int[][] intervals) {
		// 边界情况处理
		if (intervals == null || intervals.length == 0) {
			return new int[0][0];
		}
		
		// 按照区间的开始位置进行升序排序
		Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
		
		// 初始化结果列表
		java.util.List<int[]> result = new java.util.ArrayList<>();
		// 第一个区间作为当前合并区间
		int[] currentInterval = intervals[0];
		
		// 从第二个区间开始遍历
		for (int i = 1; i < intervals.length; i++) {
			// 如果当前区间与前一个区间重叠
			if (intervals[i][0] <= currentInterval[1]) {
				// 合并区间，更新结束位置为两者较大值
				currentInterval[1] = Math.max(currentInterval[1], intervals[i][1]);
			} else {
				// 不重叠，将前一个区间加入结果集
				result.add(currentInterval);
				// 更新当前合并区间
				currentInterval = intervals[i];
			}
		}
		
		// 将最后一个区间加入结果集
		result.add(currentInterval);
		
		// 转换为数组返回
		return result.toArray(new int[result.size()][]);
	}

	// 测试用例
	public static void main(String[] args) {
		// 测试用例1: intervals = [[1,3],[2,6],[8,10],[15,18]] -> 输出: [[1,6],[8,10],[15,18]]
		int[][] intervals1 = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
		System.out.println("测试用例1:");
		System.out.println("区间数组: " + Arrays.deepToString(intervals1));
		int[][] result1 = merge(intervals1);
		System.out.println("合并结果: " + Arrays.deepToString(result1)); // 期望输出: [[1,6],[8,10],[15,18]]
		
		// 测试用例2: intervals = [[1,4],[4,5]] -> 输出: [[1,5]]
		int[][] intervals2 = {{1, 4}, {4, 5}};
		System.out.println("\n测试用例2:");
		System.out.println("区间数组: " + Arrays.deepToString(intervals2));
		int[][] result2 = merge(intervals2);
		System.out.println("合并结果: " + Arrays.deepToString(result2)); // 期望输出: [[1,5]]
		
		// 测试用例3: intervals = [[1,4],[2,3]] -> 输出: [[1,4]]
		int[][] intervals3 = {{1, 4}, {2, 3}};
		System.out.println("\n测试用例3:");
		System.out.println("区间数组: " + Arrays.deepToString(intervals3));
		int[][] result3 = merge(intervals3);
		System.out.println("合并结果: " + Arrays.deepToString(result3)); // 期望输出: [[1,4]]
		
		// 测试用例4: intervals = [[1,3]] -> 输出: [[1,3]]
		int[][] intervals4 = {{1, 3}};
		System.out.println("\n测试用例4:");
		System.out.println("区间数组: " + Arrays.deepToString(intervals4));
		int[][] result4 = merge(intervals4);
		System.out.println("合并结果: " + Arrays.deepToString(result4)); // 期望输出: [[1,3]]
	}
}