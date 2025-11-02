package class089;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 课程表III - 贪心算法 + 最大堆解决方案
 * 
 * 题目描述：
 * 这里有n门不同的在线课程，按从1到n编号
 * 给你一个数组courses，其中courses[i]=[durationi, lastDayi]表示第i门课将会持续上durationi天课
 * 并且必须在不晚于lastDayi的时候完成
 * 你的学期从第 1 天开始，且不能同时修读两门及两门以上的课程
 * 返回你最多可以修读的课程数目
 * 
 * 测试链接：https://leetcode.cn/problems/course-schedule-iii/
 * 
 * 算法思想：
 * 贪心算法 + 最大堆（优先队列）
 * 1. 按照课程的截止时间进行排序（截止时间早的排在前面）
 * 2. 使用最大堆来存储已选课程的持续时间
 * 3. 遍历每个课程：
 *    - 如果当前时间加上课程持续时间不超过截止时间，则选择该课程
 *    - 如果超过截止时间，则检查是否可以替换已选课程中持续时间最长的课程
 * 
 * 时间复杂度分析：
 * O(n*logn) - 排序需要O(n*logn)，每个课程进出堆一次需要O(logn)
 * 
 * 空间复杂度分析：
 * O(n) - 最坏情况下所有课程都被选中，堆的大小为n
 * 
 * 是否为最优解：
 * 是，这是解决该问题的最优解
 * 
 * 工程化考量：
 * 1. 边界条件处理：处理空数组、单个课程等特殊情况
 * 2. 输入验证：检查课程时间是否有效（持续时间 > 0，截止时间 > 0）
 * 3. 异常处理：对非法输入进行检查
 * 4. 可读性：使用清晰的变量命名和详细的注释
 * 
 * 贪心策略证明：
 * 按照截止时间排序可以保证我们优先考虑截止时间早的课程
 * 使用最大堆来管理已选课程，当遇到冲突时替换持续时间最长的课程，可以最大化课程数量
 */
public class Code05_CourseScheduleIII {

	/**
	 * 计算最多可以修读的课程数目
	 * 
	 * @param courses 课程数组，每个元素是[durationi, lastDayi]
	 * @return 最多可以修读的课程数目
	 * 
	 * 算法步骤：
	 * 1. 按照课程的截止时间进行排序
	 * 2. 使用最大堆存储已选课程的持续时间
	 * 3. 维护当前已使用的时间
	 * 4. 遍历每个课程，动态调整已选课程
	 */
	public static int scheduleCourse(int[][] courses) {
		// 输入验证
		if (courses == null || courses.length == 0) {
			return 0;
		}
		
		int n = courses.length;
		
		// 按照课程的截止时间进行排序（截止时间早的排在前面）
		Arrays.sort(courses, (a, b) -> a[1] - b[1]);
		
		// 最大堆，存储已选课程的持续时间（持续时间长的在堆顶）
		PriorityQueue<Integer> heap = new PriorityQueue<>((a, b) -> b - a);
		int currentTime = 0;  // 当前已使用的时间
		
		for (int[] course : courses) {
			int duration = course[0];
			int lastDay = course[1];
			
			// 验证课程时间有效性
			if (duration <= 0 || lastDay <= 0) {
				throw new IllegalArgumentException("课程持续时间和截止时间必须大于0");
			}
			
			// 如果当前时间加上课程持续时间不超过截止时间
			if (currentTime + duration <= lastDay) {
				heap.add(duration);
				currentTime += duration;
			} 
			// 如果超过截止时间，但当前课程的持续时间比已选课程中最长的短
			else if (!heap.isEmpty() && heap.peek() > duration) {
				// 替换策略：用当前课程替换已选课程中持续时间最长的课程
				int longestDuration = heap.poll();
				currentTime += duration - longestDuration;
				heap.add(duration);
			}
			// 其他情况：不选择当前课程
		}
		
		return heap.size();
	}

	/**
	 * 调试版本：打印计算过程中的中间结果
	 * 
	 * @param courses 课程数组
	 * @return 最多可以修读的课程数目
	 */
	public static int debugScheduleCourse(int[][] courses) {
		if (courses == null || courses.length == 0) {
			System.out.println("空数组，无法修读任何课程");
			return 0;
		}
		
		int n = courses.length;
		
		System.out.println("原始课程安排:");
		for (int i = 0; i < n; i++) {
			System.out.println("课程" + i + ": [持续时间=" + courses[i][0] + ", 截止时间=" + courses[i][1] + "]");
		}
		
		// 按照课程的截止时间进行排序
		Arrays.sort(courses, (a, b) -> a[1] - b[1]);
		
		System.out.println("按截止时间排序后的课程安排:");
		for (int i = 0; i < n; i++) {
			System.out.println("课程" + i + ": [持续时间=" + courses[i][0] + ", 截止时间=" + courses[i][1] + "]");
		}
		
		PriorityQueue<Integer> heap = new PriorityQueue<>((a, b) -> b - a);
		int currentTime = 0;
		int selectedCount = 0;
		
		System.out.println("选课过程:");
		for (int i = 0; i < n; i++) {
			int duration = courses[i][0];
			int lastDay = courses[i][1];
			
			System.out.println("考虑课程" + i + ": [持续时间=" + duration + ", 截止时间=" + lastDay + "]");
			System.out.println("当前时间: " + currentTime);
			
			// 如果当前时间加上课程持续时间不超过截止时间
			if (currentTime + duration <= lastDay) {
				heap.add(duration);
				currentTime += duration;
				selectedCount++;
				System.out.println("选择该课程，当前已选课程数: " + selectedCount + ", 当前时间更新为: " + currentTime);
			} 
			// 如果超过截止时间，但当前课程的持续时间比已选课程中最长的短
			else if (!heap.isEmpty() && heap.peek() > duration) {
				int longestDuration = heap.poll();
				System.out.println("替换策略: 用当前课程(持续时间=" + duration + ")替换已选课程中持续时间最长的课程(持续时间=" + longestDuration + ")");
				
				currentTime += duration - longestDuration;
				heap.add(duration);
				System.out.println("替换完成，当前时间更新为: " + currentTime + ", 已选课程数保持不变: " + selectedCount);
			}
			else {
				System.out.println("无法选择该课程，跳过");
			}
			
			System.out.println("当前已选课程的持续时间: " + heap);
		}
		
		System.out.println("最终结果: 最多可以修读 " + heap.size() + " 门课程");
		return heap.size();
	}

	/**
	 * 测试函数：验证课程表III算法的正确性
	 */
	public static void testScheduleCourse() {
		System.out.println("课程表III算法测试开始");
		System.out.println("====================");
		
		// 测试用例1: [[100,200],[200,1300],[1000,1250],[2000,3200]]
		int[][] courses1 = {{100,200}, {200,1300}, {1000,1250}, {2000,3200}};
		int result1 = scheduleCourse(courses1);
		System.out.println("输入: [[100,200],[200,1300],[1000,1250],[2000,3200]]");
		System.out.println("输出: " + result1);
		System.out.println("预期: 3");
		System.out.println((result1 == 3 ? "✓ 通过" : "✗ 失败"));
		System.out.println();
		
		// 测试用例2: [[1,2]]
		int[][] courses2 = {{1,2}};
		int result2 = scheduleCourse(courses2);
		System.out.println("输入: [[1,2]]");
		System.out.println("输出: " + result2);
		System.out.println("预期: 1");
		System.out.println((result2 == 1 ? "✓ 通过" : "✗ 失败"));
		System.out.println();
		
		// 测试用例3: [[3,2],[4,3]]
		int[][] courses3 = {{3,2}, {4,3}};
		int result3 = scheduleCourse(courses3);
		System.out.println("输入: [[3,2],[4,3]]");
		System.out.println("输出: " + result3);
		System.out.println("预期: 0");
		System.out.println((result3 == 0 ? "✓ 通过" : "✗ 失败"));
		System.out.println();
		
		// 测试用例4: [[5,5],[4,6],[2,6]]
		int[][] courses4 = {{5,5}, {4,6}, {2,6}};
		int result4 = scheduleCourse(courses4);
		System.out.println("输入: [[5,5],[4,6],[2,6]]");
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
		int[][] courses = new int[n][2];
		for (int i = 0; i < n; i++) {
			int duration = (int)(Math.random() * 100) + 1;
			int lastDay = (int)(Math.random() * 1000) + duration;
			courses[i][0] = duration;
			courses[i][1] = lastDay;
		}
		
		long startTime = System.currentTimeMillis();
		int result = scheduleCourse(courses);
		long endTime = System.currentTimeMillis();
		
		System.out.println("数据规模: " + n + " 门课程");
		System.out.println("执行时间: " + (endTime - startTime) + " 毫秒");
		System.out.println("最多可以修读的课程数: " + result);
		System.out.println("性能测试结束");
	}

	/**
	 * 主函数：运行测试
	 */
	public static void main(String[] args) {
		System.out.println("课程表III - 贪心算法 + 最大堆解决方案");
		System.out.println("=================================");
		
		// 运行基础测试
		testScheduleCourse();
		
		System.out.println("调试模式示例:");
		int[][] debugCourses = {{100,200}, {200,1300}, {1000,1250}, {2000,3200}};
		System.out.println("对测试用例 [[100,200],[200,1300],[1000,1250],[2000,3200]] 进行调试跟踪:");
		int debugResult = debugScheduleCourse(debugCourses);
		System.out.println("最终结果: " + debugResult);
		
		System.out.println("算法分析:");
		System.out.println("- 时间复杂度: O(n*logn) - 排序需要O(n*logn)，每个课程进出堆一次需要O(logn)");
		System.out.println("- 空间复杂度: O(n) - 最坏情况下所有课程都被选中，堆的大小为n");
		System.out.println("- 贪心策略: 按照截止时间排序，使用最大堆管理已选课程");
		System.out.println("- 最优性: 这种贪心策略能够得到全局最优解");
		System.out.println("- 替换策略: 当遇到冲突时，用短课程替换长课程可以最大化课程数量");
		
		// 可选：运行性能测试
		// System.out.println("性能测试:");
		// performanceTest();
	}
}
