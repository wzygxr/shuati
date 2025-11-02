package class051;

import java.util.PriorityQueue;

// 计算等位时间
// 给定一个数组arr长度为n，表示n个服务员，每服务一个人的时间
// 给定一个正数m，表示有m个人等位，如果你是刚来的人，请问你需要等多久？
// 假设m远远大于n，比如n <= 10^3, m <= 10^9，该怎么做是最优解？
// 谷歌的面试，这个题连考了2个月
// 找不到测试链接，所以用对数器验证
public class Code06_WaitingTime {

	// 堆模拟
	// 验证方法，不是重点
	// 如果m很大，该方法会超时
	// 时间复杂度O(m * log(n))，额外空间复杂度O(n)
	public static int waitingTime1(int[] arr, int m) {
		// 一个一个对象int[]
		// [醒来时间，服务一个客人要多久]
		PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> (a[0] - b[0]));
		int n = arr.length;
		for (int i = 0; i < n; i++) {
			heap.add(new int[] { 0, arr[i] });
		}
		for (int i = 0; i < m; i++) {
			int[] cur = heap.poll();
			cur[0] += cur[1];
			heap.add(cur);
		}
		return heap.peek()[0];
	}

	// 二分答案法
	// 最优解
	// 时间复杂度O(n * log(min * w))，额外空间复杂度O(1)
	public static int waitingTime2(int[] arr, int w) {
		int min = Integer.MAX_VALUE;
		for (int x : arr) {
			min = Math.min(min, x);
		}
		int ans = 0;
		for (int l = 0, r = min * w, m; l <= r;) {
			// m中点，表示一定要让服务员工作的时间！
			m = l + ((r - l) >> 1);
			// 能够给几个客人提供服务
			if (f(arr, m) >= w + 1) {
				ans = m;
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return ans;
	}

	// 如果每个服务员工作time，可以接待几位客人（结束的、开始的客人都算）
	public static int f(int[] arr, int time) {
		int ans = 0;
		for (int num : arr) {
			ans += (time / num) + 1;
		}
		return ans;
	}

	// 对数器测试
	public static void main(String[] args) {
		System.out.println("测试开始");
		int N = 50;
		int V = 30;
		int M = 3000;
		int testTime = 20000;
		for (int i = 0; i < testTime; i++) {
			int n = (int) (Math.random() * N) + 1;
			int[] arr = randomArray(n, V);
			int m = (int) (Math.random() * M);
			int ans1 = waitingTime1(arr, m);
			int ans2 = waitingTime2(arr, m);
			if (ans1 != ans2) {
				System.out.println("出错了!");
			}
		}
		System.out.println("测试结束");
	}

	// 对数器测试
	public static int[] randomArray(int n, int v) {
		int[] arr = new int[n];
		for (int i = 0; i < n; i++) {
			arr[i] = (int) (Math.random() * v) + 1;
		}
		return arr;
	}
	
	/*
	 * 补充说明：
	 * 
	 * 问题解析：
	 * 这是一个典型的二分答案问题。需要计算第(m+1)个客人需要等多久。
	 * 
	 * 解题思路：
	 * 1. 转换思路：不是模拟服务员服务过程，而是二分搜索答案
	 * 2. 确定答案范围：最少等待时间是0，最多等待时间是min * (m+1)（最慢服务员服务所有客人）
	 * 3. 二分搜索：在[left, right]范围内二分搜索，对每个中间值t，计算在时间t内能服务多少客人
	 * 4. 判断函数：f(arr, time)计算在time时间内所有服务员能服务的客人总数
	 * 5. 根据f的结果调整搜索范围，最终找到能服务(m+1)个客人的最少时间
	 * 
	 * 时间复杂度分析：
	 * 1. 二分搜索范围是[0, min * (m+1)]，二分次数是O(log(min * (m+1)))
	 * 2. 每次二分需要调用f函数，f函数遍历数组一次，时间复杂度是O(n)
	 * 3. 总时间复杂度：O(n * log(min * (m+1)))
	 * 
	 * 空间复杂度分析：
	 * 1. waitingTime1使用堆模拟，空间复杂度是O(n)
	 * 2. waitingTime2只使用常数个额外变量，空间复杂度是O(1)
	 * 
	 * 工程化考虑：
	 * 1. 算法选择：当m很大时，模拟方法会超时，必须使用二分答案法
	 * 2. 数学思维：将模拟问题转化为数学计算问题
	 * 3. 整除运算：time/num表示一个服务员在time时间内能服务的客人数
	 * 4. 边界处理：+1表示在time时间开始服务的客人也算
	 * 
	 * 相关题目扩展：
	 * 1. LeetCode 1283. 使结果不超过阈值的最小除数 - https://leetcode.cn/problems/find-the-smallest-divisor-given-a-threshold/
	 * 2. LeetCode 875. 爱吃香蕉的珂珂 - https://leetcode.cn/problems/koko-eating-bananas/
	 * 3. LeetCode 410. 分割数组的最大值 - https://leetcode.cn/problems/split-array-largest-sum/
	 * 4. LeetCode 1011. 在D天内送达包裹的能力 - https://leetcode.cn/problems/capacity-to-ship-packages-within-d-days/
	 * 5. HackerRank - Minimum Time Required - https://www.hackerrank.com/challenges/minimum-time-required/problem
	 * 6. Codeforces 1373C - Pluses and Minuses - https://codeforces.com/problemset/problem/1373/C
	 * 7. AtCoder ABC146 - F - Sugoroku - https://atcoder.jp/contests/abc146/tasks/abc146_f
	 */

}