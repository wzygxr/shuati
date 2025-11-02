package class094;

import java.util.Arrays;

// 无重叠区间 (Non-overlapping Intervals)
// 给定一个区间的集合，找到需要移除区间的最小数量，使剩余区间互不重叠。
// 
// 算法标签: 贪心算法(Greedy Algorithm)、区间调度(Interval Scheduling)、排序(Sorting)
// 时间复杂度: O(n*log(n))，其中n是区间数量
// 空间复杂度: O(1)，仅使用常数额外空间
// 测试链接 : https://leetcode.cn/problems/non-overlapping-intervals/
// 相关题目: LeetCode 56. 合并区间、LeetCode 452. 用最少数量的箭引爆气球
// 贪心算法专题 - 区间调度问题集合
public class Code09_NonOverlappingIntervals {

	/*
	 * 算法思路详解：
	 * 1. 贪心策略核心：按区间结束位置排序，优先选择结束位置早的区间，
	 *    这样可以为后续区间留出更多空间，最大化不重叠区间数量
	 * 2. 排序优化：通过按结束位置排序，将问题转化为选择最多不重叠区间
	 * 3. 区间选择：遍历排序后的区间，统计不重叠的区间数量
	 * 4. 结果计算：总区间数减去不重叠区间数即为需要移除的区间数
	 *
	 * 时间复杂度分析：
	 * - O(n*log(n))，其中n是区间数量，主要消耗在排序阶段
	 * - 遍历阶段时间复杂度为O(n)
	 * 空间复杂度分析：
	 * - O(1)，仅使用了常数级别的额外空间存储count和end变量
	 * 是否最优解：是，这是处理此类区间调度问题的最优解法
	 *
	 * 工程化最佳实践：
	 * 1. 输入验证：严格检查输入参数的有效性，防止空指针异常
	 * 2. 边界处理：妥善处理各种边界情况，如空数组、单区间等
	 * 3. 性能优化：采用贪心策略避免复杂的动态规划解法
	 * 4. 代码可读性：使用语义明确的变量名和详尽的注释
	 * 5. 比较器优化：注意处理整数溢出问题
	 *
	 * 极端场景与边界情况处理：
	 * 1. 空输入场景：intervals为空数组或null时直接返回0
	 * 2. 单区间场景：只有一个区间时不需要移除
	 * 3. 重复区间场景：多个相同区间的处理
	 * 4. 特殊序列场景：区间按开始位置或结束位置排序的情况
	 * 5. 嵌套区间场景：大区间包含小区间的处理
	 * 6. 相邻区间场景：区间端点相接但不重叠的情况
	 *
	 * 跨语言实现差异与优化：
	 * 1. Java实现：使用Arrays.sort和Lambda表达式进行排序
	 * 2. C++实现：使用std::sort和自定义比较函数
	 * 3. Python实现：使用sorted函数和key参数
	 * 4. 内存管理：不同语言的垃圾回收机制对性能的影响
	 *
	 * 调试与测试策略：
	 * 1. 过程可视化：在关键节点打印当前区间和已选择区间
	 * 2. 断言验证：在循环体内添加断言确保区间不重叠
	 * 3. 性能监控：跟踪排序和遍历的实际执行时间
	 * 4. 边界测试：设计覆盖所有边界条件的测试用例
	 * 5. 压力测试：使用大规模数据验证算法稳定性
	 *
	 * 实际应用场景与拓展：
	 * 1. 会议调度：会议室资源的最优分配
	 * 2. 任务调度：CPU任务的时间片分配
	 * 3. 广告投放：广告位的最优选择
	 * 4. 课程安排：教室资源的合理利用
	 * 5. 网络带宽：数据传输的时隙分配
	 *
	 * 算法深入解析：
	 * 1. 贪心策略原理：选择结束位置早的区间是最优的局部选择
	 * 2. 最优性证明：通过交换论证法可以证明贪心策略的正确性
	 * 3. 策略变体：可按开始位置排序选择结束位置晚的区间
	 * 4. 问题转换：最小移除数 = 总数 - 最大不重叠数
	 */
	public static int eraseOverlapIntervals(int[][] intervals) {
		// 异常处理：检查输入是否为空
		if (intervals == null || intervals.length == 0) {
			return 0;
		}
		
		// 边界条件：只有一个区间，不需要移除
		if (intervals.length == 1) {
			return 0;
		}
		
		// 按区间结束位置排序
		Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
		
		int count = 1;        // 不重叠区间数量，初始选择第一个区间
		int end = intervals[0][1];  // 上一个选择区间的结束位置
		
		// 遍历排序后的区间
		for (int i = 1; i < intervals.length; i++) {
			// 如果当前区间与上一个选择的区间不重叠
			if (intervals[i][0] >= end) {
				count++;              // 不重叠区间数加1
				end = intervals[i][1]; // 更新结束位置
			}
		}
		
		// 需要移除的区间数 = 总区间数 - 不重叠区间数
		return intervals.length - count;
	}
	
	// 测试函数
	public static void main(String[] args) {
		// 测试用例1
		int[][] intervals1 = {{1, 2}, {2, 3}, {3, 4}, {1, 3}};
		System.out.println("测试用例1结果: " + eraseOverlapIntervals(intervals1)); // 期望输出: 1
		
		// 测试用例2
		int[][] intervals2 = {{1, 2}, {1, 2}, {1, 2}};
		System.out.println("测试用例2结果: " + eraseOverlapIntervals(intervals2)); // 期望输出: 2
		
		// 测试用例3
		int[][] intervals3 = {{1, 2}, {2, 3}};
		System.out.println("测试用例3结果: " + eraseOverlapIntervals(intervals3)); // 期望输出: 0
		
		// 测试用例4：边界情况
		int[][] intervals4 = {};
		System.out.println("测试用例4结果: " + eraseOverlapIntervals(intervals4)); // 期望输出: 0
		
		// 测试用例5：极端情况
		int[][] intervals5 = {{1, 100}, {2, 3}, {4, 5}, {6, 7}};
		System.out.println("测试用例5结果: " + eraseOverlapIntervals(intervals5)); // 期望输出: 1
	}

	// 补充题目1: LeetCode 56. 合并区间 (Merge Intervals)
	// 题目描述: 以数组 intervals 表示若干个区间的集合，其中单个区间为 intervals[i] = [starti, endi] 。
	// 请你合并所有重叠的区间，并返回一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间。
	// 
	// 算法标签: 贪心算法(Greedy Algorithm)、区间合并(Interval Merging)、排序(Sorting)
	// 时间复杂度: O(n*log(n))，其中n是区间数量
	// 空间复杂度: O(n)，需要额外空间存储合并后的区间
	// 链接: https://leetcode.cn/problems/merge-intervals/
	/*
	 * 算法详解与策略分析：
	 * 1. 贪心策略核心：按区间起始位置排序，然后依次合并重叠区间，
	 *    这样可以确保所有可能重叠的区间都被正确合并
	 * 2. 合并判断：当前区间与上一个合并后的区间重叠当且仅当
	 *    当前区间起始位置 ≤ 上一个区间结束位置
	 * 3. 合并操作：更新合并后区间的结束位置为两个区间结束位置的最大值
	 *
	 * 算法步骤详解：
	 * 1. 预处理：按区间起始位置升序排序
	 * 2. 初始化：将第一个区间添加到结果列表
	 * 3. 合并过程：
	 *    - 获取结果列表中的最后一个区间
	 *    - 若当前区间与最后区间重叠则合并
	 *    - 否则直接添加当前区间到结果列表
	 * 4. 结果生成：将List转换为数组返回
	 *
	 * 算法优化与正确性：
	 * 贪心选择性质：按起始位置排序后顺序处理是最优的
	 * 最优子结构：合并完前k个区间后，剩余问题仍保持最优性
	 */
	public static int[][] merge(int[][] intervals) {
		if (intervals == null || intervals.length <= 1) {
			return intervals; // 空数组或只有一个区间，无需合并
		}
		
		// 贪心策略：按区间起始位置排序，然后依次合并重叠区间
		Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
		
		// 使用动态数组存储合并后的区间
		java.util.List<int[]> merged = new java.util.ArrayList<>();
		merged.add(intervals[0]); // 添加第一个区间
		
		// 遍历其余区间
		for (int i = 1; i < intervals.length; i++) {
			int[] last = merged.get(merged.size() - 1); // 获取上一个合并后的区间
			int[] current = intervals[i]; // 当前区间
			
			// 如果当前区间与上一个合并后的区间重叠，则合并它们
			if (current[0] <= last[1]) {
				// 更新合并后的区间结束位置为两个区间结束位置的较大值
				last[1] = Math.max(last[1], current[1]);
			} else {
				// 否则直接添加当前区间
				merged.add(current);
			}
		}
		
		// 转换为二维数组返回
		return merged.toArray(new int[merged.size()][]);
	}

	// 补充题目2: LeetCode 57. 插入区间 (Insert Interval)
	// 题目描述: 给你一个 无重叠的 ，按照区间起始端点排序的区间列表。
	// 在列表中插入一个新的区间，你需要确保列表中的区间仍然有序且不重叠（如果有必要的话，可以合并区间）。
	// 
	// 算法标签: 贪心算法(Greedy Algorithm)、区间插入(Interval Insertion)、三段处理(Three-phase Processing)
	// 时间复杂度: O(n)，其中n是区间数量
	// 空间复杂度: O(n)，需要额外空间存储结果
	// 链接: https://leetcode.cn/problems/insert-interval/
	/*
	 * 算法详解与策略分析：
	 * 1. 三段处理策略：将问题分解为三个阶段：
	 *    - 添加所有在新区间之前且不重叠的区间
	 *    - 合并所有与新区间重叠的区间
	 *    - 添加剩余的区间
	 * 2. 合并机制：通过更新新区间的边界来实现区间合并
	 * 3. 有序性维护：利用输入区间列表已排序的特性
	 *
	 * 算法步骤详解：
	 * 1. 初始化：创建结果列表和双指针
	 * 2. 前段处理：添加所有在新区间之前且不重叠的区间
	 * 3. 中段合并：合并所有与新区间重叠的区间
	 * 4. 后段处理：添加剩余的区间
	 * 5. 结果返回：将List转换为数组返回
	 *
	 * 算法优化与边界处理：
	 * 边界情况：处理新区间在最前、最后或中间位置的情况
	 * 时间优化：利用已排序特性避免排序操作
	 */
	public static int[][] insert(int[][] intervals, int[] newInterval) {
		if (intervals == null) {
			return new int[][]{newInterval}; // 空数组，直接返回新区间
		}
		
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
		
		// 添加合并后的区间
		result.add(newInterval);
		
		// 添加剩余的区间
		while (i < n) {
			result.add(intervals[i]);
			i++;
		}
		
		return result.toArray(new int[result.size()][]);
	}

	// 补充题目3: LeetCode 452. 用最少数量的箭引爆气球 (Minimum Number of Arrows to Burst Balloons)
	// 题目描述: 有一些球形气球贴在一堵用XY平面表示的墙面上。气球可以用区间表示为 [start, end]，
	// 飞镖必须从整个区间的内部穿过才能引爆气球。求解把所有气球射爆所需的最小飞镖数。
	// 
	// 算法标签: 贪心算法(Greedy Algorithm)、区间覆盖(Interval Covering)、排序(Sorting)
	// 时间复杂度: O(n*log(n))，其中n是气球数量
	// 空间复杂度: O(1)，仅使用常数额外空间
	// 链接: https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/
	/*
	 * 算法详解与策略分析：
	 * 1. 贪心策略核心：按区间结束位置排序，尽可能引爆更多气球，
	 *    每支箭都射在能引爆最多气球的位置
	 * 2. 覆盖判断：当前气球能被上一支箭引爆当且仅当
	 *    当前气球起始位置 ≤ 上一支箭位置
	 * 3. 射箭决策：当无法被上一支箭引爆时需要新箭
	 *
	 * 算法步骤详解：
	 * 1. 预处理：按区间结束位置升序排序
	 * 2. 初始化：设置箭数为1，第一支箭位置为第一个区间结束位置
	 * 3. 射箭过程：
	 *    - 若当前气球能被上一支箭引爆则跳过
	 *    - 否则需要新箭，更新箭数和箭位置
	 * 4. 结果返回：返回累计箭数
	 *
	 * 算法优化与实现细节：
	 * 比较器优化：使用显式比较避免整数溢出
	 * 正确性保证：贪心策略确保最少箭数
	 */
	public static int findMinArrowShots(int[][] points) {
		if (points == null || points.length == 0) {
			return 0; // 没有气球，需要0支箭
		}
		
		// 贪心策略：按区间结束位置排序，尽可能引爆更多气球
		Arrays.sort(points, (a, b) -> {
			// 注意处理整数溢出
			if (a[1] < b[1]) return -1;
			else if (a[1] > b[1]) return 1;
			else return 0;
		});
		
		int count = 1; // 需要的箭数，初始为1
		int end = points[0][1]; // 第一支箭的位置
		
		// 遍历排序后的区间
		for (int i = 1; i < points.length; i++) {
			// 如果当前气球的开始位置大于上一支箭的位置，需要一支新箭
			if (points[i][0] > end) {
				count++;
				end = points[i][1];
			}
			// 否则，当前气球会被上一支箭引爆，不需要额外的箭
		}
		
		return count;
	}

	// 补充题目4: LeetCode 986. 区间列表的交集 (Interval List Intersections)
	// 题目描述: 给定两个由一些 闭区间 组成的列表，firstList 和 secondList，
	// 其中 firstList[i] = [starti, endi] 而 secondList[j] = [startj, endj]。
	// 每个列表中的区间是不相交的，并且已经排序。
	// 返回这 两个区间列表的交集 。
	// 
	// 算法标签: 双指针(Double Pointers)、区间交集(Interval Intersection)、有序列表处理(Sorted List Processing)
	// 时间复杂度: O(m+n)，其中m和n分别是两个列表的长度
	// 空间复杂度: O(m+n)，需要额外空间存储交集结果
	// 链接: https://leetcode.cn/problems/interval-list-intersections/
	/*
	 * 算法详解与策略分析：
	 * 1. 双指针策略：利用两个列表均已排序的特性，
	 *    使用双指针分别遍历两个列表
	 * 2. 交集计算：两个区间的交集为[max(start1,start2), min(end1,end2)]
	 * 3. 指针移动：移动结束位置较小的区间的指针
	 *
	 * 算法步骤详解：
	 * 1. 初始化：创建双指针和结果列表
	 * 2. 交集计算：
	 *    - 计算当前两个区间的交集起始和结束位置
	 *    - 若起始位置≤结束位置则存在交集
	 * 3. 指针移动：移动结束位置较小的区间的指针
	 * 4. 结果生成：将List转换为数组返回
	 *
	 * 算法优化与边界处理：
	 * 边界情况：处理空列表和无交集情况
	 * 时间优化：利用有序性避免重复比较
	 */
	public static int[][] intervalIntersection(int[][] firstList, int[][] secondList) {
		if (firstList == null || secondList == null || firstList.length == 0 || secondList.length == 0) {
			return new int[0][]; // 任一列表为空，交集为空
		}
		
		java.util.List<int[]> result = new java.util.ArrayList<>();
		int i = 0, j = 0; // 两个指针分别指向两个列表
		int m = firstList.length, n = secondList.length;
		
		// 双指针遍历两个列表
		while (i < m && j < n) {
			int start = Math.max(firstList[i][0], secondList[j][0]); // 交集的起始位置
			int end = Math.min(firstList[i][1], secondList[j][1]);   // 交集的结束位置
			
			// 如果有交集
			if (start <= end) {
				result.add(new int[]{start, end});
			}
			
			// 移动结束位置较小的区间的指针
			if (firstList[i][1] < secondList[j][1]) {
				i++;
			} else {
				j++;
			}
		}
		
		return result.toArray(new int[result.size()][]);
	}

	// 补充题目5: LeetCode 1288. 删除被覆盖区间 (Remove Covered Intervals)
	// 题目描述: 给你一个区间列表，请你删除列表中被其他区间完全覆盖的区间。
	// 只有当 c <= a 且 b <= d 时，我们才认为区间 [a,b] 被区间 [c,d] 覆盖。
	// 在完成所有删除操作后，请你返回列表中剩余区间的数目。
	// 
	// 算法标签: 贪心算法(Greedy Algorithm)、区间覆盖(Interval Covering)、排序(Sorting)
	// 时间复杂度: O(n*log(n))，其中n是区间数量
	// 空间复杂度: O(1)，仅使用常数额外空间
	// 链接: https://leetcode.cn/problems/remove-covered-intervals/
	/*
	 * 算法详解与策略分析：
	 * 1. 贪心策略核心：按起始位置升序排序，起始位置相同时按结束位置降序排序，
	 *    这样可以确保在遍历过程中能正确识别被覆盖的区间
	 * 2. 覆盖判断：当前区间被覆盖当且仅当其结束位置≤已遍历区间的最大结束位置
	 * 3. 计数机制：只统计未被覆盖的区间
	 *
	 * 算法步骤详解：
	 * 1. 预处理：按起始位置升序排序，起始位置相同时按结束位置降序排序
	 * 2. 初始化：设置剩余区间数为1，最大结束位置为第一个区间结束位置
	 * 3. 遍历过程：
	 *    - 若当前区间结束位置>最大结束位置则未被覆盖
	 *    - 更新剩余区间数和最大结束位置
	 * 4. 结果返回：返回累计剩余区间数
	 *
	 * 算法优化与正确性：
	 * 排序策略：特殊排序确保覆盖关系正确判断
	 * 贪心选择：每次选择结束位置最大的区间是最优的
	 */
	public static int removeCoveredIntervals(int[][] intervals) {
		if (intervals == null || intervals.length <= 1) {
			return intervals == null ? 0 : intervals.length; // 空数组或只有一个区间
		}
		
		// 贪心策略：按起始位置升序排序，起始位置相同时按结束位置降序排序
		Arrays.sort(intervals, (a, b) -> {
			if (a[0] != b[0]) {
				return a[0] - b[0];
			} else {
				return b[1] - a[1]; // 起始位置相同时，结束位置降序
			}
		});
		
		int count = 1; // 剩余区间数，至少有一个区间
		int end = intervals[0][1]; // 当前最大的结束位置
		
		// 遍历排序后的区间
		for (int i = 1; i < intervals.length; i++) {
			// 如果当前区间的结束位置大于最大结束位置，说明不被覆盖
			if (intervals[i][1] > end) {
				count++;
				end = intervals[i][1];
			}
		}
		
		return count;
	}
}