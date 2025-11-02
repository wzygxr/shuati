package class090;

import java.util.PriorityQueue;

/**
 * IPO问题 - 贪心算法 + 优先队列
 * 
 * 题目描述：
 * 给你n个项目，对于每个项目i，它都有一个纯利润profits[i]和启动该项目需要的最小资本capital[i]。
 * 最初你的资本为w，当你完成一个项目时，你将获得纯利润，添加到你的总资本中。
 * 总而言之，从给定项目中选择最多k个不同项目的列表，以最大化最终资本，并输出最终可获得的最多资本。
 * 
 * 解题思路：
 * 1. 使用两个优先队列：
 *    - 小根堆heap1：按启动资金排序，存储当前无法启动的项目（被锁住的项目）
 *    - 大根堆heap2：按利润排序，存储当前可以启动的项目（被解锁的项目）
 * 2. 贪心策略：在每一步选择当前可启动项目中利润最大的项目
 * 3. 循环k次，每次：
 *    - 将heap1中所有可启动的项目转移到heap2
 *    - 从heap2中选择利润最大的项目执行
 * 
 * 算法原理：
 * - 贪心选择性质：每次选择当前可执行项目中利润最大的项目是最优的
 * - 优先队列：高效维护可执行项目和不可执行项目的集合
 * 
 * 时间复杂度：O(n*logn + k*logn) - 排序和k次操作的时间复杂度
 * 空间复杂度：O(n) - 两个优先队列的空间
 * 
 * 相关题目：
 * - LeetCode 502: https://leetcode.cn/problems/ipo/
 * - 资源分配问题的经典变种
 */
public class Code05_IPO {

	/**
	 * 项目类
	 */
	public static class Project {
		public int p; // 纯利润
		public int c; // 需要的启动金

		public Project(int profit, int cost) {
			p = profit;
			c = cost;
		}
	}

	/**
	 * 计算最终可获得的最多资本
	 * 
	 * @param k      最多可完成的项目数
	 * @param w      初始资本
	 * @param profit 项目利润数组
	 * @param cost   项目启动资金数组
	 * @return 最终可获得的最多资本
	 */
	public static int findMaximizedCapital(int k, int w, int[] profit, int[] cost) {
		int n = profit.length;
		// 需要的启动金小根堆
		// 代表被锁住的项目
		PriorityQueue<Project> heap1 = new PriorityQueue<>((a, b) -> a.c - b.c);
		// 利润大根堆
		// 代表被解锁的项目
		PriorityQueue<Project> heap2 = new PriorityQueue<>((a, b) -> b.p - a.p);
		
		// 将所有项目加入小根堆（按启动资金排序）
		for (int i = 0; i < n; i++) {
			heap1.add(new Project(profit[i], cost[i]));
		}
		
		// 最多执行k个项目
		while (k > 0) {
			// 将所有当前可启动的项目从heap1转移到heap2
			while (!heap1.isEmpty() && heap1.peek().c <= w) {
				heap2.add(heap1.poll());
			}
			
			// 如果没有可启动的项目，结束循环
			if (heap2.isEmpty()) {
				break;
			}
			
			// 选择利润最大的项目执行
			w += heap2.poll().p;
			k--;
		}
		return w;
	}

}	 * 
	 * @param k      最多可完成的项目数
	 * @param w      初始资本
	 * @param profit 项目利润数组
	 * @param cost   项目启动资金数组
	 * @return 最终可获得的最多资本
	 */
	public static int findMaximizedCapital(int k, int w, int[] profit, int[] cost) {
		int n = profit.length;
		// 需要的启动金小根堆
		// 代表被锁住的项目
		PriorityQueue<Project> heap1 = new PriorityQueue<>((a, b) -> a.c - b.c);
		// 利润大根堆
		// 代表被解锁的项目
		PriorityQueue<Project> heap2 = new PriorityQueue<>((a, b) -> b.p - a.p);
		
		// 将所有项目加入小根堆（按启动资金排序）
		for (int i = 0; i < n; i++) {
			heap1.add(new Project(profit[i], cost[i]));
		}
		
		// 最多执行k个项目
		while (k > 0) {
			// 将所有当前可启动的项目从heap1转移到heap2
			while (!heap1.isEmpty() && heap1.peek().c <= w) {
				heap2.add(heap1.poll());
			}
			
			// 如果没有可启动的项目，结束循环
			if (heap2.isEmpty()) {
				break;
			}
			
			// 选择利润最大的项目执行
			w += heap2.poll().p;
			k--;
		}
		return w;
	}

}