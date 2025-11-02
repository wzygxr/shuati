package class089;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 会议室II - 贪心算法 + 最小堆解决方案
 * 
 * 题目描述：
 * 给你一个会议时间安排的数组 intervals
 * 每个会议时间都会包括开始和结束的时间intervals[i]=[starti, endi]
 * 返回所需会议室的最小数量
 * 
 * 测试链接：https://leetcode.cn/problems/meeting-rooms-ii/
 * 
 * 算法思想：
 * 贪心算法 + 最小堆（优先队列）
 * 1. 按照会议开始时间对会议进行排序
 * 2. 使用最小堆来存储当前正在进行的会议的结束时间
 * 3. 对于每个会议：
 *    - 如果堆顶的会议已经结束（结束时间 <= 当前会议开始时间），则弹出堆顶
 *    - 将当前会议的结束时间加入堆中
 *    - 更新所需会议室的最大数量
 * 
 * 时间复杂度分析：
 * O(n*logn) - 排序需要O(n*logn)，每个会议进出堆一次需要O(logn)
 * 
 * 空间复杂度分析：
 * O(n) - 最坏情况下所有会议都需要同时进行，堆的大小为n
 * 
 * 是否为最优解：
 * 是，这是解决该问题的最优解
 * 
 * 工程化考量：
 * 1. 边界条件处理：处理空数组、单个会议等特殊情况
 * 2. 输入验证：检查会议时间是否有效（开始时间 < 结束时间）
 * 3. 异常处理：对非法输入进行检查
 * 4. 可读性：使用清晰的变量命名和详细的注释
 * 
 * 贪心策略证明：
 * 每次选择最早结束的会议室来安排新会议，这样可以最大化会议室的使用效率
 * 这种策略可以保证所需会议室数量最少
 */
public class Code04_MeetingRoomsII {

	/**
	 * 计算所需会议室的最小数量
	 * 
	 * @param intervals 会议时间数组，每个元素是[starti, endi]
	 * @return 所需会议室的最小数量
	 * 
	 * 算法步骤：
	 * 1. 按照会议开始时间排序
	 * 2. 使用最小堆存储当前会议的结束时间
	 * 3. 遍历每个会议，释放已结束的会议室，分配新的会议室
	 * 4. 跟踪所需会议室的最大数量
	 */
	public static int minMeetingRooms(int[][] intervals) {
		// 输入验证
		if (intervals == null || intervals.length == 0) {
			return 0;
		}
		
		int n = intervals.length;
		
		// 按照会议开始时间排序
		Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
		
		// 最小堆，存储当前正在进行的会议的结束时间
		PriorityQueue<Integer> heap = new PriorityQueue<>();
		int maxRooms = 0;  // 所需会议室的最大数量
		
		for (int i = 0; i < n; i++) {
			int start = intervals[i][0];
			int end = intervals[i][1];
			
			// 验证会议时间有效性
			if (start >= end) {
				throw new IllegalArgumentException("会议开始时间必须小于结束时间");
			}
			
			// 释放已经结束的会议室（结束时间 <= 当前会议开始时间）
			while (!heap.isEmpty() && heap.peek() <= start) {
				heap.poll();
			}
			
			// 分配新的会议室
			heap.add(end);
			
			// 更新所需会议室的最大数量
			maxRooms = Math.max(maxRooms, heap.size());
		}
		
		return maxRooms;
	}

	/**
	 * 调试版本：打印计算过程中的中间结果
	 * 
	 * @param intervals 会议时间数组
	 * @return 所需会议室的最小数量
	 */
	public static int debugMinMeetingRooms(int[][] intervals) {
		if (intervals == null || intervals.length == 0) {
			System.out.println("空数组，不需要会议室");
			return 0;
		}
		
		int n = intervals.length;
		
		System.out.println("原始会议安排:");
		for (int i = 0; i < n; i++) {
			System.out.println("会议" + i + ": [" + intervals[i][0] + ", " + intervals[i][1] + "]");
		}
		
		// 按照会议开始时间排序
		Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
		
		System.out.println("排序后的会议安排:");
		for (int i = 0; i < n; i++) {
			System.out.println("会议" + i + ": [" + intervals[i][0] + ", " + intervals[i][1] + "]");
		}
		
		PriorityQueue<Integer> heap = new PriorityQueue<>();
		int maxRooms = 0;
		
		System.out.println("分配过程:");
		for (int i = 0; i < n; i++) {
			int start = intervals[i][0];
			int end = intervals[i][1];
			
			System.out.println("处理会议" + i + ": [" + start + ", " + end + "]");
			
			// 释放已经结束的会议室
			System.out.print("释放会议室: ");
			boolean released = false;
			while (!heap.isEmpty() && heap.peek() <= start) {
				int finished = heap.poll();
				System.out.print("结束时间" + finished + " ");
				released = true;
			}
			if (!released) {
				System.out.print("无会议室可释放");
			}
			System.out.println();
			
			// 分配新的会议室
			heap.add(end);
			System.out.println("分配新会议室，当前会议室数量: " + heap.size());
			
			// 更新最大数量
			if (heap.size() > maxRooms) {
				maxRooms = heap.size();
				System.out.println("更新最大会议室数量: " + maxRooms);
			}
		}
		
		System.out.println("最终结果: " + maxRooms + " 个会议室");
		return maxRooms;
	}

	/**
	 * 测试函数：验证会议室分配算法的正确性
	 */
	public static void testMinMeetingRooms() {
		System.out.println("会议室II算法测试开始");
		System.out.println("===================");
		
		// 测试用例1: [[0,30],[5,10],[15,20]]
		int[][] intervals1 = {{0,30}, {5,10}, {15,20}};
		int result1 = minMeetingRooms(intervals1);
		System.out.println("输入: [[0,30],[5,10],[15,20]]");
		System.out.println("输出: " + result1);
		System.out.println("预期: 2");
		System.out.println((result1 == 2 ? "✓ 通过" : "✗ 失败"));
		System.out.println();
		
		// 测试用例2: [[7,10],[2,4]]
		int[][] intervals2 = {{7,10}, {2,4}};
		int result2 = minMeetingRooms(intervals2);
		System.out.println("输入: [[7,10],[2,4]]");
		System.out.println("输出: " + result2);
		System.out.println("预期: 1");
		System.out.println((result2 == 1 ? "✓ 通过" : "✗ 失败"));
		System.out.println();
		
		// 测试用例3: 空数组
		int[][] intervals3 = {};
		int result3 = minMeetingRooms(intervals3);
		System.out.println("输入: []");
		System.out.println("输出: " + result3);
		System.out.println("预期: 0");
		System.out.println((result3 == 0 ? "✓ 通过" : "✗ 失败"));
		System.out.println();
		
		// 测试用例4: [[1,5],[8,9],[8,9]]
		int[][] intervals4 = {{1,5}, {8,9}, {8,9}};
		int result4 = minMeetingRooms(intervals4);
		System.out.println("输入: [[1,5],[8,9],[8,9]]");
		System.out.println("输出: " + result4);
		System.out.println("预期: 2");
		System.out.println((result4 == 2 ? "✓ 通过" : "✗ 失败"));
		System.out.println();
		
		System.out.println("测试结束");
	}

	/**
	 * 性能测试：测试算法在大规模数据下的表现
	 */
	public static void performanceTest() {
		System.out.println("性能测试开始");
		System.out.println("============");
		
		// 生成大规模测试数据
		int n = 10000;
		int[][] intervals = new int[n][2];
		for (int i = 0; i < n; i++) {
			int start = i * 10;
			int end = start + (int)(Math.random() * 10) + 1;
			intervals[i][0] = start;
			intervals[i][1] = end;
		}
		
		long startTime = System.currentTimeMillis();
		int result = minMeetingRooms(intervals);
		long endTime = System.currentTimeMillis();
		
		System.out.println("数据规模: " + n + " 个会议");
		System.out.println("执行时间: " + (endTime - startTime) + " 毫秒");
		System.out.println("所需会议室数量: " + result);
		System.out.println("性能测试结束");
	}

	/**
	 * 主函数：运行测试
	 */
	public static void main(String[] args) {
		System.out.println("会议室II - 贪心算法 + 最小堆解决方案");
		System.out.println("=================================");
		
		// 运行基础测试
		testMinMeetingRooms();
		
		System.out.println("调试模式示例:");
		int[][] debugIntervals = {{0,30}, {5,10}, {15,20}};
		System.out.println("对测试用例 [[0,30],[5,10],[15,20]] 进行调试跟踪:");
		int debugResult = debugMinMeetingRooms(debugIntervals);
		System.out.println("最终结果: " + debugResult);
		
		System.out.println("算法分析:");
		System.out.println("- 时间复杂度: O(n*logn) - 排序需要O(n*logn)，每个会议进出堆一次需要O(logn)");
		System.out.println("- 空间复杂度: O(n) - 最坏情况下所有会议都需要同时进行，堆的大小为n");
		System.out.println("- 贪心策略: 每次选择最早结束的会议室来安排新会议");
		System.out.println("- 最优性: 这种贪心策略能够得到全局最优解");
		System.out.println("- 数据结构: 使用最小堆（优先队列）来高效管理会议室");
		
		// 可选：运行性能测试
		// System.out.println("性能测试:");
		// performanceTest();
	}
}
