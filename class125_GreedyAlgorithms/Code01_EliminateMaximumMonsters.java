package class094;

import java.util.Arrays;
import java.util.PriorityQueue;

// 消灭怪物的最大数量
// 你正在玩一款电子游戏，在游戏中你需要保护城市免受怪物侵袭
// 给定两个大小为n的整数数组dist、speed
// 其中dist[i]是第i个怪物与城市的初始距离
// 其中speed[i]是第i个怪物的速度
// 你有一种武器，一旦充满电，就可以消灭一个怪物，但是，武器需要1的时间才能充电完成
// 武器在游戏开始时是充满电的状态，怪物从0时刻开始移动，一旦任何怪物到达城市，就输掉了这场游戏
// 如果某个怪物恰好在某一分钟开始时到达城市，这也会被视为输掉游戏
// 返回在你输掉游戏前可以消灭的怪物的最大数量，如果消灭所有怪兽了返回n
// 测试链接 : https://leetcode.cn/problems/eliminate-maximum-number-of-monsters/

/*
 * 题目解析：
 * 这是一个典型的贪心算法问题。我们需要在每一轮中选择最优策略来最大化消灭怪物的数量。
 * 关键在于理解游戏规则：
 * 1. 武器初始状态是充满电的，可以立即消灭一个怪物
 * 2. 武器充电需要1个时间单位
 * 3. 怪物从时刻0开始移动
 * 4. 如果任何怪物在某一分钟开始时到达城市，游戏失败
 * 5. 我们需要找出能消灭的最大怪物数量
 *
 * 解题思路：
 * 1. 贪心策略：优先消灭最早到达城市的怪物
 * 2. 计算每个怪物到达城市的时间
 * 3. 按时间排序，依次消灭怪物
 * 4. 在第i分钟，如果第i个怪物还未到达，则可以消灭它
 * 5. 如果第i个怪物已经到达，则游戏失败
 */
public class Code01_EliminateMaximumMonsters {

	/*
	 * 算法思路：
	 * 1. 贪心策略：优先消灭最早到达城市的怪物
	 * 2. 计算每个怪物到达城市的时间：time[i] = ceil(dist[i] / speed[i])
	 * 3. 将时间排序，按顺序消灭怪物
	 * 4. 在第i分钟（从0开始），如果第i个怪物还未到达城市，则可以消灭它
	 * 5. 一旦发现第i分钟时第i个怪物已经到达城市，则游戏失败
	 *
	 * 时间复杂度：O(n * logn) - 主要是排序的时间复杂度
	 * 空间复杂度：O(n) - 存储到达时间数组
	 * 是否最优解：是，这是处理此类问题的最优解法
	 *
	 * 工程化考量：
	 * 1. 异常处理：检查输入是否为空或长度不一致
	 * 2. 边界条件：处理空数组、单个元素等特殊情况
	 * 3. 性能优化：使用向上取整公式 (a + b - 1) / b 避免浮点运算
	 * 4. 可读性：清晰的变量命名和注释
	 *
	 * 算法详解：
	 * 1. 计算到达时间：使用向上取整公式计算每个怪物到达城市的时间
	 * 2. 排序：按到达时间升序排列
	 * 3. 贪心选择：在每一分钟选择最早到达的未消灭怪物
	 * 4. 游戏结束条件：当前分钟有怪物已到达城市
	 */
	public static int eliminateMaximum(int[] dist, int[] speed) {
		// 异常处理：检查输入是否为空
		if (dist == null || speed == null || dist.length != speed.length) {
			return 0;
		}
		
		int n = dist.length;
		
		// 边界条件：没有怪物
		if (n == 0) {
			return 0;
		}
		
		// 计算每个怪物到达城市的时间
		// 使用向上取整公式：ceil(a/b) = (a + b - 1) / b
		// 这样可以避免浮点运算，提高性能和精度
		int[] time = new int[n];
		for (int i = 0; i < n; i++) {
			// 向上取整公式：ceil(dist[i]/speed[i]) = (dist[i] + speed[i] - 1) / speed[i]
			// 例如：ceil(5/3) = (5+3-1)/3 = 8/3 = 2
			time[i] = (dist[i] + speed[i] - 1) / speed[i];
		}
		
		// 按到达时间排序，优先消灭最早到达的怪物
		Arrays.sort(time);
		
		// 按顺序尝试消灭怪物
		// 在第i分钟（从0开始），我们可以消灭一个怪物
		// 如果第i个怪物已经到达城市（time[i] <= i），则游戏失败
		for (int i = 0; i < n; i++) {
			// 当前来到第i分钟（从0开始）
			// 如果第i个怪物已经到达城市（time[i] <= i），则游戏失败
			if (time[i] <= i) {
				return i; // 返回已消灭的怪物数量
			}
		}
		
		// 成功消灭所有怪物
		return n;
	}
	
	/*
	 * 相关题目1: LeetCode 871. 最低加油次数
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
	 *
	 * 算法详解：
	 * 1. 贪心策略：在油用完之前，选择经过的加油站中油量最多的
	 * 2. 使用最大堆维护所有经过但未加油的加油站
	 * 3. 当油不够到达下一个加油站或目的地时，从堆中选择油量最多的加油
	 * 4. 重复直到到达目的地或无法继续前进
	 */
	public static int minRefuelStops(int target, int startFuel, int[][] stations) {
		// 最大堆，维护经过的加油站的油量
		// 使用自定义比较器 (a, b) -> b - a 实现最大堆
		PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
		
		int i = 0; // 加油站索引，用于遍历所有加油站
		int res = 0; // 加油次数计数器
		int curr = startFuel; // 当前油量，初始值为起始油量
		
		// 当当前油量不足以到达目的地时继续循环
		while (curr < target) {
			// 将当前能到达的所有加油站加入最大堆中
			// stations[i][0] 是加油站位置，stations[i][1] 是油量
			while (i < stations.length && stations[i][0] <= curr) {
				maxHeap.offer(stations[i++][1]); // 将油量加入堆中
			}
			
			// 如果没有可加油的加油站，无法到达目的地
			if (maxHeap.isEmpty()) {
				return -1; // 无法到达目的地
			}
			
			// 在油量最多的加油站加油
			// poll() 方法移除并返回堆顶元素（最大油量）
			curr += maxHeap.poll();
			res++; // 加油次数加1
		}
		
		return res; // 返回最少加油次数
	}
	
	/*
	 * 相关题目2: LeetCode 1885. 统计数对
	 * 题目链接: https://leetcode.cn/problems/count-pairs-in-two-arrays/
	 * 题目描述: 给你两个长度为 n 的整数数组 nums1 和 nums2，找出所有满足 i < j 且 
	 * nums1[i] + nums1[j] > nums2[i] + nums2[j] 的数对 (i, j)。
	 * 返回满足条件的数对总数。
	 * 解题思路: 数学变换 + 排序 + 双指针
	 * 将不等式变换为 (nums1[i] - nums2[i]) + (nums1[j] - nums2[j]) > 0
	 * 令 diff[i] = nums1[i] - nums2[i]，问题转化为在diff数组中找出满足 diff[i] + diff[j] > 0 且 i < j 的数对
	 * 排序后使用双指针技术
	 * 时间复杂度: O(n log n)
	 * 空间复杂度: O(n)
	 * 是否最优解: 是，这是处理此类问题的最优解法
	 *
	 * 算法详解：
	 * 1. 数学变换：将原不等式转换为更易处理的形式
	 * 2. 构造差值数组：diff[i] = nums1[i] - nums2[i]
	 * 3. 排序：对差值数组进行排序
	 * 4. 双指针：使用双指针技术统计满足条件的数对
	 * 5. 优化：利用排序后的单调性减少计算量
	 */
	public static long countPairs(int[] nums1, int[] nums2) {
		int n = nums1.length;
		// 构造差值数组，diff[i] = nums1[i] - nums2[i]
		int[] diff = new int[n];
		
		// 计算差值数组
		for (int i = 0; i < n; i++) {
			diff[i] = nums1[i] - nums2[i];
		}
		
		// 对差值数组进行排序，为双指针技术做准备
		Arrays.sort(diff);
		
		long count = 0; // 使用long类型防止整数溢出
		int left = 0, right = n - 1; // 双指针，left指向最小值，right指向最大值
		
		// 双指针统计满足条件的数对
		// 条件：diff[left] + diff[right] > 0
		while (left < right) {
			if (diff[left] + diff[right] > 0) {
				// 如果diff[left] + diff[right] > 0
				// 由于数组已排序，diff[left]到diff[right-1]与diff[right]的和都大于0
				// 所以有right-left个满足条件的数对
				count += right - left;
				right--; // 移动右指针，尝试更小的值
			} else {
				// 如果diff[left] + diff[right] <= 0
				// 说明diff[left]太小，需要增大
				left++; // 移动左指针，尝试更大的值
			}
		}
		
		return count; // 返回满足条件的数对总数
	}
}