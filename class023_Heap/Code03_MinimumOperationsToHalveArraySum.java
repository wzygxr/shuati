package class027;

import java.util.PriorityQueue;

// 将数组和减半的最少操作次数
// 测试链接 : https://leetcode.cn/problems/minimum-operations-to-halve-array-sum/
public class Code03_MinimumOperationsToHalveArraySum {

	// 提交时把halveArray1改名为halveArray
	public static int halveArray1(int[] nums) {
		// 大根堆
		PriorityQueue<Double> heap = new PriorityQueue<>((a, b) -> b.compareTo(a));
		double sum = 0;
		for (int num : nums) {
			heap.add((double) num);
			sum += num;
		}
		// sum，整体累加和，-> 要减少的目标！
		sum /= 2;
		int ans = 0;
		for (double minus = 0, cur; minus < sum; ans++, minus += cur) {
			cur = heap.poll() / 2;
			heap.add(cur);
		}
		return ans;
	}

	public static int MAXN = 100001;

	public static long[] heap = new long[MAXN];

	public static int size;

	// 提交时把halveArray2改名为halveArray
	public static int halveArray2(int[] nums) {
		size = nums.length;
		long sum = 0;
		for (int i = size - 1; i >= 0; i--) {
			heap[i] = (long) nums[i] << 20;
			sum += heap[i];
			heapify(i);
		}
		sum /= 2;
		int ans = 0;
		for (long minus = 0; minus < sum; ans++) {
			heap[0] /= 2;
			minus += heap[0];
			heapify(0);
		}
		return ans;
	}

	public static void heapify(int i) {
		int l = i * 2 + 1;
		while (l < size) {
			int best = l + 1 < size && heap[l + 1] > heap[l] ? l + 1 : l;
			best = heap[best] > heap[i] ? best : i;
			if (best == i) {
				break;
			}
			swap(best, i);
			i = best;
			l = i * 2 + 1;
		}
	}

	public static void swap(int i, int j) {
		long tmp = heap[i];
		heap[i] = heap[j];
		heap[j] = tmp;
	}

	/**
	 * 相关题目1: LeetCode 1792. 最大平均通过率
	 * 题目链接: https://leetcode.cn/problems/maximum-average-pass-ratio/
	 * 题目描述: 有 classes 个班级，每个班级有 pass_i 个通过考试的学生和 total_i 个学生。
	 * 给你一个整数 extraStudents，表示额外有 extraStudents 个聪明学生，
	 * 他们一定能通过考试。你需要给这些学生分配班级，使得所有班级的平均通过率最大。
	 * 解题思路: 使用最大堆维护每个班级增加一个学生后的通过率提升值
	 * 时间复杂度: O((n + extraStudents) * log n)
	 * 空间复杂度: O(n)
	 * 是否最优解: 是，这是处理增量优化问题的经典解法
	 */
	public static double maxAverageRatio(int[][] classes, int extraStudents) {
		// 最大堆，按通过率提升值排序
		PriorityQueue<double[]> maxHeap = new PriorityQueue<>((a, b) -> 
			Double.compare(b[2], a[2])); // 按提升值降序排列
		
		// 初始化堆
		for (int[] c : classes) {
			double pass = c[0], total = c[1];
			// 计算增加一个学生后的通过率提升值
			double gain = (pass + 1) / (total + 1) - pass / total;
			maxHeap.offer(new double[]{pass, total, gain});
		}
		
		// 分配额外学生
		for (int i = 0; i < extraStudents; i++) {
			double[] current = maxHeap.poll();
			double pass = current[0] + 1;
			double total = current[1] + 1;
			// 计算再次增加一个学生后的通过率提升值
			double gain = (pass + 1) / (total + 1) - pass / total;
			maxHeap.offer(new double[]{pass, total, gain});
		}
		
		// 计算最终平均通过率
		double sum = 0;
		while (!maxHeap.isEmpty()) {
			double[] current = maxHeap.poll();
			sum += current[0] / current[1];
		}
		
		return sum / classes.length;
	}

	/**
	 * 相关题目2: LeetCode 1353. 最多可以参加的会议数目
	 * 题目链接: https://leetcode.cn/problems/maximum-number-of-events-that-can-be-attended/
	 * 题目描述: 给你一个数组 events，其中 events[i] = [startDayi, endDayi]，
	 * 表示会议 i 开始于 startDayi，结束于 endDayi。
	 * 你可以在满足 startDayi <= d <= endDayi 的任意一天 d 参加会议 i。
	 * 在任意一天 d 中只能参加一场会议。返回你可以参加的最大会议数目。
	 * 解题思路: 贪心算法，按开始时间排序，使用最小堆维护当前可参加的会议的结束时间
	 * 时间复杂度: O(n log n)
	 * 空间复杂度: O(n)
	 * 是否最优解: 是，这是处理区间调度问题的经典贪心解法
	 */
	public static int maxEvents(int[][] events) {
		// 按开始时间排序
		java.util.Arrays.sort(events, (a, b) -> a[0] - b[0]);
		
		// 最小堆，维护当前可参加的会议的结束时间
		PriorityQueue<Integer> minHeap = new PriorityQueue<>();
		
		int i = 0, n = events.length, res = 0;
		// 遍历每一天
		for (int d = 1; d <= 100000; d++) {
			// 移除已过期的会议
			while (!minHeap.isEmpty() && minHeap.peek() < d) {
				minHeap.poll();
			}
			
			// 添加当天开始的会议
			while (i < n && events[i][0] == d) {
				minHeap.offer(events[i++][1]);
			}
			
			// 参加结束时间最早的会议
			if (!minHeap.isEmpty()) {
				minHeap.poll();
				res++;
			}
		}
		
		return res;
	}

	/**
	 * 相关题目3: LeetCode 1642. 可以到达的最远建筑
	 * 题目链接: https://leetcode.cn/problems/furthest-building-you-can-reach/
	 * 题目描述: 给你一个整数数组 heights，表示建筑物的高度。
	 * 从建筑物 0 开始，你可以按顺序访问其他建筑物。
	 * 如果当前建筑物的高度大于等于下一个建筑物的高度，则不需要梯子或砖块。
	 * 如果当前建筑物的高度小于下一个建筑物的高度，你可以用一架梯子或 (h[i+1] - h[i]) 个砖块。
	 * 返回你可以到达的最远建筑物的下标（下标从 0 开始）。
	 * 解题思路: 贪心算法，优先使用梯子，当梯子不够时用砖块替换需要最少的那次使用
	 * 时间复杂度: O(n log ladders)
	 * 空间复杂度: O(ladders)
	 * 是否最优解: 是，这是处理资源分配问题的经典贪心解法
	 */
	public static int furthestBuilding(int[] heights, int bricks, int ladders) {
		// 最小堆，维护使用梯子的跳跃高度
		PriorityQueue<Integer> minHeap = new PriorityQueue<>();
		
		for (int i = 0; i < heights.length - 1; i++) {
			int diff = heights[i + 1] - heights[i];
			
			// 如果需要向上爬
			if (diff > 0) {
				// 使用梯子
				if (minHeap.size() < ladders) {
					minHeap.offer(diff);
				} else {
					// 如果梯子用完了，决定是否用砖块替换
					if (!minHeap.isEmpty() && diff > minHeap.peek()) {
						// 用砖块替换需要最少的那次使用
						bricks -= minHeap.poll();
						minHeap.offer(diff);
					} else {
						// 直接用砖块
						bricks -= diff;
					}
					
					// 如果砖块不够，无法继续
					if (bricks < 0) {
						return i;
					}
				}
			}
		}
		
		return heights.length - 1;
	}

	/**
	 * 相关题目4: LeetCode 871. 最低加油次数
	 * 题目链接: https://leetcode.cn/problems/minimum-number-of-refueling-stops/
	 * 题目描述: 汽车从起点出发驶向目的地，该目的地位于出发位置东面 target 英里处。
	 * 沿途有加油站，用数组 stations 表示。其中 stations[i] = [positioni, fueli]
	 * 表示第 i 个加油站位于出发位置东面 positioni 英里处，并且有 fueli 升汽油。
	 * 假设汽车油箱的容量是无限的，其中最初有 startFuel 升燃料。
	 * 它每行驶 1 英里就会用掉 1 升汽油。当汽车到达加油站时，它可能停下来加油。
	 * 返回汽车到达目的地所需的最少加油次数。如果无法到达目的地，则返回 -1。
	 * 解题思路: 贪心算法，使用最大堆维护经过的加油站的油量
	 * 时间复杂度: O(n log n)
	 * 空间复杂度: O(n)
	 * 是否最优解: 是，这是处理资源补充问题的经典贪心解法
	 */
	public static int minRefuelStops(int target, int startFuel, int[][] stations) {
		// 最大堆，维护经过的加油站的油量
		PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
		
		int i = 0; // 加油站索引
		int res = 0; // 加油次数
		int curr = startFuel; // 当前油量
		
		while (curr < target) {
			// 将当前能到达的加油站加入堆中
			while (i < stations.length && stations[i][0] <= curr) {
				maxHeap.offer(stations[i++][1]);
			}
			
			// 如果没有可加油的加油站，无法到达目的地
			if (maxHeap.isEmpty()) {
				return -1;
			}
			
			// 在油量最多的加油站加油
			curr += maxHeap.poll();
			res++;
		}
		
		return res;
	}

	/**
	 * 工程化考量总结：
	 * 1. 异常处理：
	 *    - 空输入处理：检查输入是否为空或null
	 *    - 边界条件：处理数组为空、单个元素等特殊情况
	 * 2. 性能优化：
	 *    - 堆大小控制：维护固定大小的堆以控制内存使用
	 *    - 避免重复计算：缓存计算结果
	 * 3. 可配置性：
	 *    - 比较器定制：通过自定义比较器支持不同的排序需求
	 *    - 参数化设计：通过参数控制行为
	 * 4. 线程安全：
	 *    - 在多线程环境中使用时，需要考虑同步机制
	 * 5. 内存管理：
	 *    - 及时清理不需要的对象，避免内存泄漏
	 *    - 合理选择堆的实现方式（内置PriorityQueue vs 自实现堆）
	 * 6. 代码可读性：
	 *    - 清晰的变量命名和注释
	 *    - 模块化设计，将复杂逻辑分解为独立方法
	 * 7. 单元测试：
	 *    - 覆盖各种边界情况和异常输入
	 *    - 验证时间和空间复杂度是否符合预期
	 * 8. 跨语言特性：
	 *    - Java: PriorityQueue
	 *    - Python: heapq
	 *    - C++: priority_queue
	 * 9. 调试技巧：
	 *    - 打印堆的状态用于调试
	 *    - 使用断言验证中间结果
	 * 10. 与标准库对比：
	 *     - 理解标准库实现的优势和局限性
	 *     - 在性能要求极高时考虑自实现堆
	 */

	/**
	 * 贪心+堆问题总结与技巧：
	 * 1. 识别贪心+堆问题的关键特征：
	 *    - "最大化/最小化"目标函数
	 *    - "动态决策"过程
	 *    - "局部最优"能导向全局最优
	 * 2. 解题策略：
	 *    - 贪心选择：每步选择当前最优策略
	 *    - 堆维护：使用堆维护候选方案
	 *    - 回退机制：在必要时能够回退之前的选择
	 * 3. 常见题型：
	 *    - 资源分配：1792, 1642
	 *    - 调度问题：1353, 871
	 *    - 优化问题：本题（将数组和减半）
	 * 4. 复杂度分析：
	 *    - 时间复杂度通常为O(n log n)，主要消耗在堆操作上
	 *    - 空间复杂度通常为O(n)，用于存储堆
	 * 5. 陷阱与注意事项：
	 *    - 注意贪心策略的正确性证明
	 *    - 注意堆中元素的比较逻辑
	 *    - 注意边界条件处理
	 */
}