package class027;

// 最多线段重合问题
// 测试链接 : https://www.nowcoder.com/practice/1ae8d0b6bb4e4bcdbf64ec491f63fc37
// 测试链接 : https://leetcode.cn/problems/meeting-rooms-ii/
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;
import java.util.PriorityQueue;

public class Code02_MaxCover {

	public static int MAXN = 10001;

	public static int[][] line = new int[MAXN][2];

	public static int n;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			n = (int) in.nval;
			for (int i = 0; i < n; i++) {
				in.nextToken();
				line[i][0] = (int) in.nval;
				in.nextToken();
				line[i][1] = (int) in.nval;
			}
			out.println(compute());
		}
		out.flush();
		out.close();
		br.close();
	}

	public static int compute() {
		// 堆的清空
		size = 0;

		// 线段一共有n条，line[0...n-1][2] : line[i][0] line[i][1], 左闭右闭
		// 所有线段，根据开始位置排序，结束位置无所谓
		// 比较器的用法
		// line [0...n) 排序 : 所有小数组，开始位置谁小谁在前
		Arrays.sort(line, 0, n, (a, b) -> a[0] - b[0]);
		int ans = 0;
		for (int i = 0; i < n; i++) {
			// i : line[i][0] line[i][1]
			while (size > 0 && heap[0] <= line[i][0]) {
				pop();
			}
			add(line[i][1]);
			ans = Math.max(ans, size);
		}
		return ans;
	}

	// 小根堆，堆顶0位置
	public static int[] heap = new int[MAXN];

	// 堆的大小
	public static int size;

	public static void add(int x) {
		heap[size] = x;
		int i = size++;
		while (heap[i] < heap[(i - 1) / 2]) {
			swap(i, (i - 1) / 2);
			i = (i - 1) / 2;
		}
	}

	public static void pop() {
		swap(0, --size);
		int i = 0, l = 1;
		while (l < size) {
			int best = l + 1 < size && heap[l + 1] < heap[l] ? l + 1 : l;
			best = heap[best] < heap[i] ? best : i;
			if (best == i) {
				break;
			}
			swap(i, best);
			i = best;
			l = i * 2 + 1;
		}
	}

	public static void swap(int i, int j) {
		int tmp = heap[i];
		heap[i] = heap[j];
		heap[j] = tmp;
	}

	// 也找到了leetcode测试链接
	// 测试链接 : https://leetcode.cn/problems/meeting-rooms-ii/
	// 提交如下代码可以直接通过
	public static int minMeetingRooms(int[][] meeting) {
		int n = meeting.length;
		Arrays.sort(meeting, (a, b) -> a[0] - b[0]);
		PriorityQueue<Integer> heap = new PriorityQueue<>();
		int ans = 0;
		for (int i = 0; i < n; i++) {
			while (!heap.isEmpty() && heap.peek() <= meeting[i][0]) {
				heap.poll();
			}
			heap.add(meeting[i][1]);
			ans = Math.max(ans, heap.size());
		}
		return ans;
	}

	// 上面的leetcode题目是会员题，需要付费
	// 如果不想开通leetcode会员，还有一个类似的题，但是注意题意，和课上讲的有细微差别
	// 课上讲的题目，认为[1,4]、[4、5]可以严丝合缝接在一起，不算有重合
	// 但是如下链接的题目，认为[1,4]、[4、5]有重合部分，也就是4
	// 除此之外再无差别
	// 测试链接 : https://leetcode.cn/problems/divide-intervals-into-minimum-number-of-groups/
	// 提交如下代码可以直接通过
	public static int minGroups(int[][] meeting) {
		int n = meeting.length;
		Arrays.sort(meeting, (a, b) -> a[0] - b[0]);
		PriorityQueue<Integer> heap = new PriorityQueue<>();
		int ans = 0;
		for (int i = 0; i < n; i++) {
			// 注意这里的判断
			while (!heap.isEmpty() && heap.peek() < meeting[i][0]) {
				heap.poll();
			}
			heap.add(meeting[i][1]);
			ans = Math.max(ans, heap.size());
		}
		return ans;
	}

	/**
	 * 相关题目1: LeetCode 435. 无重叠区间
	 * 题目链接: https://leetcode.cn/problems/non-overlapping-intervals/
	 * 题目描述: 给定一个区间的集合 intervals ，其中 intervals[i] = [starti, endi] 。返回需要移除区间的最小数量，使剩余区间互不重叠。
	 * 解题思路: 贪心算法，按结束时间排序，优先选择结束时间早的区间
	 * 时间复杂度: O(n log n)
	 * 空间复杂度: O(1)
	 * 是否最优解: 是，这是处理区间调度问题的经典贪心解法
	 */
	public static int eraseOverlapIntervals(int[][] intervals) {
		if (intervals.length == 0) {
			return 0;
		}
		
		// 按结束时间排序
		Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
		
		int count = 0;
		int end = intervals[0][1];
		
		for (int i = 1; i < intervals.length; i++) {
			// 如果当前区间与前一个区间重叠，需要移除
			if (intervals[i][0] < end) {
				count++;
			} else {
				// 更新结束时间
				end = intervals[i][1];
			}
		}
		
		return count;
	}

	/**
	 * 相关题目2: LeetCode 452. 用最少数量的箭引爆气球
	 * 题目链接: https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/
	 * 题目描述: 一些球形的气球贴在一堵用 XY 平面表示的墙面上。墙面上的气球记录在整数数组 points，
	 * 其中 points[i] = [xstart, xend] 表示水平直径在 xstart 和 xend 之间的气球。
	 * 返回引爆所有气球所必须射出的最小弓箭数。
	 * 解题思路: 贪心算法，按结束位置排序，尽可能多地引爆重叠的气球
	 * 时间复杂度: O(n log n)
	 * 空间复杂度: O(1)
	 * 是否最优解: 是，这是处理区间覆盖问题的经典贪心解法
	 */
	public static int findMinArrowShots(int[][] points) {
		if (points.length == 0) {
			return 0;
		}
		
		// 按结束位置排序
		Arrays.sort(points, (a, b) -> Integer.compare(a[1], b[1]));
		
		int arrows = 1;
		int end = points[0][1];
		
		for (int i = 1; i < points.length; i++) {
			// 如果当前气球的起始位置大于前一个气球的结束位置，需要额外的箭
			if (points[i][0] > end) {
				arrows++;
				end = points[i][1];
			}
		}
		
		return arrows;
	}

	/**
	 * 相关题目3: LeetCode 56. 合并区间
	 * 题目链接: https://leetcode.cn/problems/merge-intervals/
	 * 题目描述: 以数组 intervals 表示若干个区间的集合，其中单个区间为 intervals[i] = [starti, endi] 。
	 * 合并所有重叠的区间，并返回一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间。
	 * 解题思路: 先按起始位置排序，然后依次合并重叠区间
	 * 时间复杂度: O(n log n)
	 * 空间复杂度: O(n)
	 * 是否最优解: 是，这是处理区间合并问题的经典解法
	 */
	public static int[][] merge(int[][] intervals) {
		if (intervals.length == 0) {
			return new int[0][2];
		}
		
		// 按起始位置排序
		Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
		
		java.util.List<int[]> merged = new java.util.ArrayList<>();
		merged.add(intervals[0]);
		
		for (int i = 1; i < intervals.length; i++) {
			int[] last = merged.get(merged.size() - 1);
			// 如果当前区间与上一个区间重叠，合并它们
			if (intervals[i][0] <= last[1]) {
				last[1] = Math.max(last[1], intervals[i][1]);
			} else {
				// 否则添加新区间
				merged.add(intervals[i]);
			}
		}
		
		return merged.toArray(new int[merged.size()][]);
	}

	/**
	 * 相关题目4: LeetCode 57. 插入区间
	 * 题目链接: https://leetcode.cn/problems/insert-interval/
	 * 题目描述: 给你一个无重叠的，按照区间起始端点排序的区间列表 intervals，
	 * 其中 intervals[i] = [starti, endi] 表示第 i 个区间的开始和结束，
	 * 并且 intervals 按照 starti 升序排列。同样给定一个区间 newInterval = [start, end] 。
	 * 在 intervals 中插入区间 newInterval，使得 intervals 依然按照 starti 升序排列，且区间之间不重叠。
	 * 解题思路: 找到合适的位置插入新区间，然后合并重叠区间
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n)
	 * 是否最优解: 是，这是处理区间插入问题的经典解法
	 */
	public static int[][] insert(int[][] intervals, int[] newInterval) {
		java.util.List<int[]> result = new java.util.ArrayList<>();
		int i = 0;
		int n = intervals.length;
		
		// 添加所有在新区间之前且不重叠的区间
		while (i < n && intervals[i][1] < newInterval[0]) {
			result.add(intervals[i]);
			i++;
		}
		
		// 合并所有与新区间重叠的区间
		while (i < n && intervals[i][0] <= newInterval[1]) {
			newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
			newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
			i++;
		}
		result.add(newInterval);
		
		// 添加所有在新区间之后的区间
		while (i < n) {
			result.add(intervals[i]);
			i++;
		}
		
		return result.toArray(new int[result.size()][]);
	}

	/**
	 * 相关题目5: LeetCode 759. 员工空闲时间
	 * 题目链接: https://leetcode.cn/problems/employee-free-time/
	 * 题目描述: 给定员工的 schedule 列表，表示每个员工的工作时间。
	 * 每个员工都有一个非重叠的时间段 Intervals 列表，这些时间段已经排好序。
	 * 返回表示所有员工的共同、正数长度的空闲时间的有限时间段的列表，同样需要排好序。
	 * 解题思路: 使用最小堆维护所有员工的下一个工作时间段，找出空闲时间段
	 * 时间复杂度: O(n log k)，n为所有时间段总数，k为员工数
	 * 空间复杂度: O(k)
	 * 是否最优解: 是，这是处理多个有序序列合并问题的经典解法
	 */
	static class Interval {
		public int start;
		public int end;

		public Interval() {}

		public Interval(int _start, int _end) {
			start = _start;
			end = _end;
		}
	};
	
	public static java.util.List<Interval> employeeFreeTime(java.util.List<java.util.List<Interval>> schedule) {
		// 最小堆，按开始时间排序
		PriorityQueue<Interval> heap = new PriorityQueue<>((a, b) -> a.start - b.start);
		
		// 将所有员工的第一个时间段加入堆中
		for (java.util.List<Interval> employee : schedule) {
			if (!employee.isEmpty()) {
				heap.offer(employee.get(0));
			}
		}
		
		java.util.List<Interval> result = new java.util.ArrayList<>();
		// 记录当前最大的结束时间
		int prevEnd = heap.isEmpty() ? 0 : heap.peek().start;
		
		// 遍历所有时间段
		while (!heap.isEmpty()) {
			Interval current = heap.poll();
			
			// 如果当前时间段的开始时间大于前一个时间段的结束时间，说明有空闲时间
			if (current.start > prevEnd) {
				result.add(new Interval(prevEnd, current.start));
			}
			
			// 更新最大的结束时间
			prevEnd = Math.max(prevEnd, current.end);
		}
		
		return result;
	}

	/**
	 * 工程化考量总结：
	 * 1. 异常处理：
	 *    - 空输入处理：检查输入是否为空或null
	 *    - 边界条件：处理区间为空、单个区间等特殊情况
	 * 2. 性能优化：
	 *    - 排序优化：合理选择排序算法和比较器
	 *    - 避免重复计算：缓存计算结果
	 * 3. 可配置性：
	 *    - 比较器定制：通过自定义比较器支持不同的排序需求
	 *    - 参数化设计：通过参数控制行为
	 * 4. 线程安全：
	 *    - 在多线程环境中使用时，需要考虑同步机制
	 * 5. 内存管理：
	 *    - 及时清理不需要的对象，避免内存泄漏
	 * 6. 代码可读性：
	 *    - 清晰的变量命名和注释
	 *    - 模块化设计，将复杂逻辑分解为独立方法
	 * 7. 单元测试：
	 *    - 覆盖各种边界情况和异常输入
	 *    - 验证时间和空间复杂度是否符合预期
	 * 8. 跨语言特性：
	 *    - Java: Arrays.sort, PriorityQueue
	 *    - Python: sorted, heapq
	 *    - C++: sort, priority_queue
	 * 9. 调试技巧：
	 *    - 打印区间状态用于调试
	 *    - 使用断言验证中间结果
	 * 10. 与标准库对比：
	 *    - 理解标准库实现的优势和局限性
	 */

	/**
	 * 区间问题总结与技巧：
	 * 1. 识别区间问题的关键特征：
	 *    - "重叠区间"处理
	 *    - "区间合并"需求
	 *    - "区间调度"优化
	 *    - "区间覆盖"计算
	 * 2. 解题策略：
	 *    - 贪心算法：按起始位置或结束位置排序
	 *    - 堆结构：维护动态区间信息
	 *    - 双指针：处理两个有序区间序列
	 * 3. 常见题型：
	 *    - 区间调度：435, 452
	 *    - 区间合并：56
	 *    - 区间插入：57
	 *    - 区间统计：本题（最多线段重合）
	 * 4. 复杂度分析：
	 *    - 时间复杂度通常为O(n log n)，主要消耗在排序上
	 *    - 空间复杂度通常为O(n)，用于存储结果
	 * 5. 陷阱与注意事项：
	 *    - 注意区间的开闭性
	 *    - 注意边界条件处理
	 *    - 注意整数溢出问题
	 */
}