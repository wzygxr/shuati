package class094;

import java.util.PriorityQueue;
import java.util.Comparator;

// 最大平均通过率
// 一所学校里有一些班级，每个班级里有一些学生，现在每个班都会进行一场期末考试
// 给你一个二维数组classes，其中classes[i]=[passi, totali]
// 表示你提前知道了第i个班级总共有totali个学生
// 其中只有 passi 个学生可以通过考试
// 给你一个整数extraStudents，表示额外有extraStudents个聪明的学生，一定能通过期末考
// 你需要给这extraStudents个学生每人都安排一个班级，使得所有班级的平均通过率最大
// 一个班级的 通过率 等于这个班级通过考试的学生人数除以这个班级的总人数
// 平均通过率 是所有班级的通过率之和除以班级数目
// 请你返回在安排这extraStudents个学生去对应班级后的最大平均通过率
// 测试链接 : https://leetcode.cn/problems/maximum-average-pass-ratio/

/*
 * 题目解析：
 * 这是一个资源分配问题，要求将额外的学生分配到各个班级以最大化平均通过率。
 * 关键在于理解如何衡量分配的收益：
 * 1. 每个班级的通过率 = 通过人数 / 总人数
 * 2. 平均通过率 = 所有班级通过率之和 / 班级数
 * 3. 分配一个学生到班级的收益 = 分配后的通过率 - 分配前的通过率
 *
 * 解题思路：
 * 1. 贪心策略：每次分配学生时，选择能带来最大收益的班级
 * 2. 使用优先队列维护班级，按收益降序排列
 * 3. 重复分配过程，直到所有学生都被分配
 * 4. 计算最终的平均通过率
 */
public class Code03_MaximumAveragePassRatio {

	/*
	 * 算法思路：
	 * 1. 贪心策略：每次分配一个学生时，选择能使得通过率提升最大的班级
	 * 2. 通过率提升计算：对于班级(a,b)，增加一个学生后通过率提升为 (a+1)/(b+1) - a/b
	 * 3. 使用优先队列维护所有班级，按通过率提升量排序
	 * 4. 每次取出提升量最大的班级，分配一个学生，然后重新计算提升量并放回队列
	 * 5. 重复extraStudents次，最后计算平均通过率
	 *
	 * 时间复杂度：O((n + m) * logn) - n是班级数，m是额外学生数
	 * 空间复杂度：O(n) - 优先队列的空间
	 * 是否最优解：是，这是处理此类问题的最优解法
	 *
	 * 工程化考量：
	 * 1. 异常处理：检查输入是否为空
	 * 2. 边界条件：处理班级数为0、额外学生数为0等特殊情况
	 * 3. 性能优化：使用double数组避免创建对象的开销
	 * 4. 可读性：清晰的变量命名和注释
	 *
	 * 算法详解：
	 * 1. 收益计算：对于班级(a,b)，增加一个学生后的收益为 (a+1)/(b+1) - a/b
	 * 2. 优先队列：使用大根堆维护班级，按收益降序排列
	 * 3. 贪心分配：每次选择收益最大的班级分配学生
	 * 4. 动态更新：分配学生后重新计算班级收益并更新队列
	 */
	public static double maxAverageRatio(int[][] classes, int m) {
		// 异常处理：检查输入是否为空
		if (classes == null || classes.length == 0) {
			return 0.0;
		}
		
		int n = classes.length;
		// double[] c1 : {a, b, c}
		// a : c1班级有多少人通过
		// b : c1班级总人数
		// c : 如果再来一人，c1班级通过率提升多少，(a+1)/(b+1) - a/b
		// 通过率提升的大根堆
		// 使用自定义比较器实现大根堆：c1[2] >= c2[2] ? -1 : 1
		// 当c1[2] >= c2[2]时返回-1，表示c1优先级更高
		PriorityQueue<double[]> heap = new PriorityQueue<>((c1, c2) -> c1[2] >= c2[2] ? -1 : 1);
		
		// 初始化堆，计算每个班级增加一个学生后的通过率提升
		// 对于班级(pass, total)，收益为 (pass+1)/(total+1) - pass/total
		for (int[] c : classes) {
			double a = c[0];  // 通过人数
			double b = c[1];  // 总人数
			// 计算增加一个学生后的通过率提升
			// 提升量 = (a+1)/(b+1) - a/b
			heap.add(new double[] { a, b, (a + 1) / (b + 1) - a / b });
		}
		
		// 逐个分配额外的学生
		// 贪心策略：每次分配一个学生到能带来最大通过率提升的班级
		while (m-- > 0) {
			// 取出能带来最大通过率提升的班级
			double[] cur = heap.poll();
			double a = cur[0] + 1;  // 通过人数增加1
			double b = cur[1] + 1;  // 总人数增加1
			// 重新计算通过率提升并放回堆中
			// 分配学生后，需要重新计算该班级的收益
			heap.add(new double[] { a, b, (a + 1) / (b + 1) - a / b });
		}
		
		// 计算最终的通过率累加和
		double ans = 0;
		// 遍历所有班级，计算最终的通过率之和
		while (!heap.isEmpty()) {
			double[] cur = heap.poll();
			// 累加每个班级的通过率
			ans += cur[0] / cur[1];
		}
		
		// 返回最大平均通过率
		// 平均通过率 = 所有班级通过率之和 / 班级数
		return ans / n;
	}
	
	/*
	 * 相关题目1: LeetCode 1705. 吃苹果的最大数目
	 * 题目链接: https://leetcode.cn/problems/maximum-number-of-eaten-apples/
	 * 题目描述: 有一棵特殊的苹果树，一连 n 天，每天都可以长出若干个苹果。
	 * 在第 i 天，树上会长出 apples[i] 个苹果，这些苹果将会在 days[i] 天后（也就是说，第 i + days[i] 天时）腐烂，变得无法食用。
	 * 也可能有那么几天树上不会长出新的苹果，此时用 apples[i] == 0 且 days[i] == 0 表示。
	 * 你打算每天最多吃一个苹果来保证营养均衡。注意，你可以在这 n 天之后继续吃苹果。
	 * 给你两个长度为 n 的整数数组 days 和 apples ，返回你可以吃掉的苹果的最大数目。
	 * 解题思路: 贪心算法，使用最小堆维护苹果的过期时间
	 * 时间复杂度: O(n log n)
	 * 空间复杂度: O(n)
	 * 是否最优解: 是，这是处理此类问题的最优解法
	 *
	 * 算法详解：
	 * 1. 贪心策略：每天吃最容易腐烂的苹果
	 * 2. 数据结构：使用最小堆维护苹果的过期时间
	 * 3. 过程模拟：按天模拟苹果的生长和腐烂过程
	 * 4. 优化选择：优先吃最早过期的苹果
	 */
	public static int eatenApples(int[] apples, int[] days) {
		// 最小堆，维护苹果的过期时间和数量
		// 堆中元素为int[2]数组：{过期时间, 苹果数量}
		// 按过期时间升序排列，最早过期的在堆顶
		PriorityQueue<int[]> minHeap = new PriorityQueue<>(new Comparator<int[]>() {
			public int compare(int[] a, int[] b) {
				return a[0] - b[0]; // 按过期时间升序排列
			}
		});
		
		int eaten = 0; // 已吃苹果数量
		int day = 0;   // 当前天数
		
		// 处理前n天长出的苹果
		// 在这n天中，每天可能长出新苹果，也可能有苹果腐烂
		while (day < apples.length) {
			// 移除过期的苹果
			// 检查堆顶苹果是否已过期（过期时间 <= 当前天数）
			while (!minHeap.isEmpty() && minHeap.peek()[0] <= day) {
				minHeap.poll(); // 移除过期苹果
			}
			
			// 添加当天新长出的苹果
			int expireDay = day + days[day]; // 计算过期时间
			int count = apples[day];         // 当天长出的苹果数量
			if (count > 0) {
				// 只有当长出苹果时才添加到堆中
				minHeap.offer(new int[]{expireDay, count});
			}
			
			// 吃一个苹果
			// 贪心策略：吃最早过期的苹果
			if (!minHeap.isEmpty()) {
				int[] apple = minHeap.peek(); // 获取最早过期的苹果
				apple[1]--; // 苹果数量减1
				eaten++;    // 已吃苹果数量加1
				// 如果苹果吃完了，移除
				if (apple[1] == 0) {
					minHeap.poll(); // 移除空的苹果记录
				}
			}
			
			day++; // 进入下一天
		}
		
		// 处理n天后剩余的苹果
		// 在n天之后，不再长出新苹果，但可以继续吃剩余的苹果
		while (!minHeap.isEmpty()) {
			// 移除过期的苹果
			// 继续检查并移除过期苹果
			while (!minHeap.isEmpty() && minHeap.peek()[0] <= day) {
				minHeap.poll();
			}
			
			if (!minHeap.isEmpty()) {
				int[] apple = minHeap.poll(); // 取出最早过期的苹果
				// 计算可以吃的苹果数量
				// 取min(苹果数量, 剩余可吃天数)
				// 剩余可吃天数 = 过期时间 - 当前天数
				int eat = Math.min(apple[0] - day, apple[1]);
				eaten += eat; // 增加已吃苹果数量
				day += eat;   // 更新天数
			}
		}
		
		return eaten; // 返回总共吃掉的苹果数量
	}
	
	/*
	 * 相关题目2: LeetCode 1353. 最多可以参加的会议数目
	 * 题目链接: https://leetcode.cn/problems/maximum-number-of-events-that-can-be-attended/
	 * 题目描述: 给你一个数组 events，其中 events[i] = [startDayi, endDayi] ，表示会议 i 开始于 startDayi ，结束于 endDayi 。
	 * 你可以在满足 startDayi <= d <= endDayi 的任意一天 d 参加会议 i 。在任意一天 d 中只能参加一场会议。
	 * 请你返回你可以参加的最大会议数目。
	 * 解题思路: 贪心算法，使用最小堆维护当天可以参加的会议的结束时间
	 * 时间复杂度: O(n log n)
	 * 空间复杂度: O(n)
	 * 是否最优解: 是，这是处理此类问题的最优解法
	 *
	 * 算法详解：
	 * 1. 贪心策略：每天参加结束时间最早的会议
	 * 2. 数据结构：使用最小堆维护可参加会议的结束时间
	 * 3. 过程模拟：按天模拟会议的开始和结束
	 * 4. 优化选择：优先参加最早结束的会议
	 */
	public static int maxEvents(int[][] events) {
		// 按开始时间排序
		// 将会议按开始时间升序排列，便于按时间顺序处理
		java.util.Arrays.sort(events, new Comparator<int[]>() {
			public int compare(int[] a, int[] b) {
				return a[0] - b[0]; // 按开始时间升序排列
			}
		});
		
		// 最小堆，维护可以参加的会议的结束时间
		// 堆中存储会议的结束时间，最早结束的会议在堆顶
		PriorityQueue<Integer> minHeap = new PriorityQueue<>();
		
		int i = 0, day = 1, count = 0; // i: 会议索引, day: 当前天数, count: 参加会议数
		int n = events.length; // 会议总数
		
		// 当还有未处理的会议或堆中还有可参加会议时继续循环
		while (i < n || !minHeap.isEmpty()) {
			// 将当天开始的会议加入堆中
			// 将所有在当前天开始的会议添加到堆中
			while (i < n && events[i][0] == day) {
				// 将会议的结束时间加入堆中
				minHeap.offer(events[i][1]);
				i++; // 处理下一个会议
			}
			
			// 移除已经结束的会议
			// 移除所有在当前天之前结束的会议
			while (!minHeap.isEmpty() && minHeap.peek() < day) {
				minHeap.poll(); // 移除过期会议
			}
			
			// 参加结束时间最早的会议
			// 贪心策略：参加结束时间最早的会议
			if (!minHeap.isEmpty()) {
				minHeap.poll(); // 参加会议（从堆中移除）
				count++;        // 参加会议数加1
			}
			
			day++; // 进入下一天
		}
		
		return count; // 返回最多可以参加的会议数目
	}
}