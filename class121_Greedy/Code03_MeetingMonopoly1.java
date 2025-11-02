package class090;

import java.util.Arrays;

/**
 * 会议必须独占时间段的最大会议数量 - 贪心算法
 * 
 * 题目描述：
 * 给定若干会议的开始、结束时间，你参加某个会议的期间，不能参加其他会议。
 * 返回你能参加的最大会议数量。
 * 
 * 解题思路：
 * 1. 使用贪心策略：按会议结束时间排序
 * 2. 优先选择结束时间早的会议，为后续会议留出更多时间
 * 3. 遍历排序后的会议，如果当前会议的开始时间不早于上一个选择会议的结束时间，则选择该会议
 * 
 * 算法原理：
 * - 贪心选择性质：选择结束时间最早的会议是最优的
 * - 最优子结构：在选择了一个会议后，剩余问题仍然是求解最大不重叠区间数
 * 
 * 时间复杂度：O(n*logn) - 排序的时间复杂度
 * 空间复杂度：O(1) - 只使用了常数额外空间
 * 
 * 相关题目：
 * - LeetCode 435: https://leetcode.cn/problems/non-overlapping-intervals/
 * - 会议安排问题的经典变种
 */
public class Code03_MeetingMonopoly1 {

	/**
	 * LeetCode 435题解法：计算需要删除的最少会议数
	 * 
	 * @param meeting 会议数组，meeting[i][0]为开始时间，meeting[i][1]为结束时间
	 * @return 需要删除的最少会议数
	 */
	// 测试链接 :https://leetcode.cn/problems/non-overlapping-intervals/
	// 测试链接中，问至少删除多少会议，可以让剩下的会议都不重合
	// 那么求出，最多能不重合的参加多少会议，然后 n - 这个数量，就是答案
	// 同时注意，测试链接中，会议的时间点范围[- 5 * 10 ^ 4 ~ + 5 * 10 ^ 4]
	// 其实就是课上讲的方法，稍微改动一下即可，改动的地方已经加上注释
	public static int eraseOverlapIntervals(int[][] meeting) {
		// 按会议结束时间升序排序
		Arrays.sort(meeting, (a, b) -> a[1] - b[1]);
		int n = meeting.length;
		int ans = 0;
		// cur初始设置为-50001，因为题目数据状况如此
		for (int i = 0, cur = -50001; i < n; i++) {
			// 如果当前会议的开始时间不早于上一个选择会议的结束时间，则选择该会议
			if (cur <= meeting[i][0]) {
				ans++;
				cur = meeting[i][1];
			}
		}
		// 会议总数 - 参加的最大会议数量 = 需要删除的会议数
		return n - ans;
	}

	/**
	 * 暴力方法（用于验证贪心解法的正确性）
	 * 
	 * @param meeting 会议数组
	 * @return 最大可参加的会议数
	 */
	// 暴力方法
	// 为了验证
	// 时间复杂度O(n!)
	public static int maxMeeting1(int[][] meeting) {
		return f(meeting, meeting.length, 0);
	}

	/**
	 * 递归函数：通过全排列找出最大不重叠会议数
	 * 
	 * @param meeting 会议数组
	 * @param n       会议总数
	 * @param i       当前处理到第几个会议
	 * @return 最大可参加的会议数
	 */
	// 把所有会议全排列
	// 其中一定有安排会议次数最多的排列
	public static int f(int[][] meeting, int n, int i) {
		int ans = 0;
		if (i == n) {
			// 计算当前排列下最多能参加多少会议
			for (int j = 0, cur = -1; j < n; j++) {
				if (cur <= meeting[j][0]) {
					ans++;
					cur = meeting[j][1];
				}
			}
		} else {
			// 全排列
			for (int j = i; j < n; j++) {
				swap(meeting, i, j);
				ans = Math.max(ans, f(meeting, n, i + 1));
				swap(meeting, i, j);
			}
		}
		return ans;
	}

	/**
	 * 交换数组中两个元素的位置
	 * 
	 * @param meeting 会议数组
	 * @param i       第一个元素索引
	 * @param j       第二个元素索引
	 */
	public static void swap(int[][] meeting, int i, int j) {
		int[] tmp = meeting[i];
		meeting[i] = meeting[j];
		meeting[j] = tmp;
	}

	/**
	 * 正式方法：贪心算法求解最大可参加会议数
	 * 
	 * @param meeting 会议数组
	 * @return 最大可参加的会议数
	 */
	// 正式方法
	// 时间复杂度O(n*logn)
	public static int maxMeeting2(int[][] meeting) {
		// meeting[i][0] : i号会议开始时间
		// meeting[i][1] : i号会议结束时间
		// 按会议结束时间升序排序
		Arrays.sort(meeting, (a, b) -> a[1] - b[1]);
		int n = meeting.length;
		int ans = 0;
		// 遍历排序后的会议，选择不重叠的会议
		for (int i = 0, cur = -1; i < n; i++) {
			// 如果当前会议的开始时间不早于上一个选择会议的结束时间，则选择该会议
			if (cur <= meeting[i][0]) {
				ans++;
				cur = meeting[i][1];
			}
		}
		return ans;
	}

	/**
	 * 生成随机会议（用于测试）
	 * 
	 * @param n 会议数量
	 * @param m 时间范围
	 * @return 随机生成的会议数组
	 */
	// 为了验证
	// 生成随机会议
	public static int[][] randomMeeting(int n, int m) {
		int[][] ans = new int[n][2];
		for (int i = 0, a, b; i < n; i++) {
			a = (int) (Math.random() * m);
			b = (int) (Math.random() * m);
			if (a == b) {
				ans[i][0] = a;
				ans[i][1] = a + 1;
			} else {
				ans[i][0] = Math.min(a, b);
				ans[i][1] = Math.max(a, b);
			}
		}
		return ans;
	}

	/**
	 * 对数器（用于验证贪心解法的正确性）
	 */
	// 对数器
	// 为了验证
	public static void main(String[] args) {
		int N = 10;
		int M = 12;
		int testTimes = 2000;
		System.out.println("测试开始");
		for (int i = 1; i <= testTimes; i++) {
			int n = (int) (Math.random() * N) + 1;
			int[][] meeting = randomMeeting(n, M);
			int ans1 = maxMeeting1(meeting);
			int ans2 = maxMeeting2(meeting);
			if (ans1 != ans2) {
				// 如果出错了
				// 可以增加打印行为找到一组出错的例子
				// 然后去debug
				System.out.println("出错了！");
			}
			if (i % 100 == 0) {
				System.out.println("测试到第" + i + "组");
			}
		}
		System.out.println("测试结束");
	}

}