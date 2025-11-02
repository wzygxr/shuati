package class115;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 包含每个查询的最小区间 (LeetCode 1851)
 * 题目链接: https://leetcode.cn/problems/minimum-interval-to-include-each-query/
 * 
 * 题目描述:
 * 给你一个二维整数数组intervals，其中intervals[i] = [l, r]
 * 表示第i个区间开始于l，结束于r，区间的长度是r-l+1
 * 给你一个整数数组queries，queries[i]表示要查询的位置
 * 答案是所有包含queries[i]的区间中，最小长度的区间是多长
 * 返回数组对应查询的所有答案，如果不存在这样的区间那么答案是-1
 * 
 * 解题思路:
 * 使用扫描线算法结合最小堆实现最小区间查询。
 * 1. 将区间按起始位置排序
 * 2. 将查询按位置排序
 * 3. 使用最小堆维护当前可能包含查询点的区间（按长度排序）
 * 4. 对于每个查询点，将起始位置小于等于该点的区间加入堆中
 * 5. 移除堆中结束位置小于该点的区间
 * 6. 堆顶元素即为包含该点的最小区间
 * 
 * 时间复杂度: O(n log n + m log m) - 排序和堆操作
 * 穩間复杂度: O(n + m) - 存储区间和查询信息
 * 
 * 工程化考量:
 * 1. 异常处理: 检查输入数组合法性
 * 2. 边界条件: 处理空数组和无效区间
 * 3. 性能优化: 使用扫描线减少重复计算
 * 4. 可读性: 详细注释和模块化设计
 */
public class Code01_MinimumIntervalQuery1 {

	/**
	 * 计算每个查询点的最小区间长度
	 * 使用系统提供的PriorityQueue作为最小堆
	 * 
	 * @param intervals 区间数组，每个元素为[left, right]
	 * @param queries 查询点数组
	 * @return 每个查询点对应的最小区间长度数组
	 */
	public static int[] minInterval(int[][] intervals, int[] queries) {
		// 边界条件检查
		if (intervals == null || queries == null) {
			throw new IllegalArgumentException("输入数组不能为空");
		}
		
		int n = intervals.length;
		int m = queries.length;
		
		// 按区间起始位置排序
		// 时间复杂度: O(n log n)
		Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
		
		// 将查询点与其原始索引配对并排序
		// 时间复杂度: O(m log m)
		int[][] ques = new int[m][2];
		for (int i = 0; i < m; i++) {
			ques[i][0] = queries[i]; // 查询点位置
			ques[i][1] = i;        // 原始索引
		}
		Arrays.sort(ques, (a, b) -> a[0] - b[0]);
		
		// 使用最小堆维护当前可能包含查询点的区间
		// 堆中元素: [区间长度, 区间结束位置]
		// 按区间长度排序，长度小的在堆顶
		PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
		
		// 存储结果
		int[] ans = new int[m];
		
		// 扫描线算法处理每个查询点
		// i: 查询点索引, j: 区间索引
		for (int i = 0, j = 0; i < m; i++) {
			// 将起始位置小于等于当前查询点的所有区间加入堆中
			for (; j < n && intervals[j][0] <= ques[i][0]; j++) {
				heap.add(new int[] { intervals[j][1] - intervals[j][0] + 1, intervals[j][1] });
			}
			
			// 移除堆中结束位置小于当前查询点的区间
			while (!heap.isEmpty() && heap.peek()[1] < ques[i][0]) {
				heap.poll();
			}
			
			// 堆顶元素即为包含当前查询点的最小区间
			if (!heap.isEmpty()) {
				ans[ques[i][1]] = heap.peek()[0];
			} else {
				ans[ques[i][1]] = -1;
			}
		}
		
		return ans;
	}

}
