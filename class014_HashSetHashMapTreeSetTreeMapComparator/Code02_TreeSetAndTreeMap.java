import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * TreeSet和TreeMap相关题目与解析
 * 
 * TreeSet基于TreeMap实现，底层使用红黑树，查询时间复杂度O(log n)，元素有序
 * TreeMap基于红黑树实现，查询时间复杂度O(log n)，键值对按键有序
 * 
 * 相关平台题目：
 * 1. LeetCode 220. Contains Duplicate III (存在重复元素 III) - https://leetcode.com/problems/contains-duplicate-iii/
 * 2. LeetCode 349. Intersection of Two Arrays (两个数组的交集) - https://leetcode.com/problems/intersection-of-two-arrays/
 * 3. LeetCode 352. Data Stream as Disjoint Intervals (将数据流变为多个不相交区间) - https://leetcode.com/problems/data-stream-as-disjoint-intervals/
 * 4. LeetCode 363. Max Sum of Rectangle No Larger Than K (矩形区域不超过 K 的最大数值和) - https://leetcode.com/problems/max-sum-of-rectangle-no-larger-than-k/
 * 5. LeetCode 456. 132 Pattern (132 模式) - https://leetcode.com/problems/132-pattern/
 * 6. LeetCode 632. Smallest Range Covering Elements from K Lists (最小区间) - https://leetcode.com/problems/smallest-range-covering-elements-from-k-lists/
 * 7. LeetCode 699. Falling Squares (掉落的方块) - https://leetcode.com/problems/falling-squares/
 * 8. LeetCode 715. Range Module (Range 模块) - https://leetcode.com/problems/range-module/
 * 9. LeetCode 729. My Calendar I (我的日程安排表 I) - https://leetcode.com/problems/my-calendar-i/
 * 10. LeetCode 846. Hand of Straights (一手顺子) - https://leetcode.com/problems/hand-of-straights/
 * 11. LeetCode 855. Exam Room (考场就座) - https://leetcode.com/problems/exam-room/
 * 12. LeetCode 933. Number of Recent Calls (最近的请求次数) - https://leetcode.com/problems/number-of-recent-calls/
 * 13. LeetCode 975. Odd Even Jump (奇偶跳) - https://leetcode.com/problems/odd-even-jump/
 * 14. LeetCode 981. Time Based Key-Value Store (基于时间的键值存储) - https://leetcode.com/problems/time-based-key-value-store/
 * 15. LeetCode 1244. Design A Leaderboard (力扣排行榜) - https://leetcode.com/problems/design-a-leaderboard/
 * 16. HackerRank Java TreeSet (Java树集) - https://www.hackerrank.com/challenges/java-tree-set
 * 17. LeetCode 315. Count of Smaller Numbers After Self (计算右侧小于当前元素的个数) - https://leetcode.com/problems/count-of-smaller-numbers-after-self/
 * 18. LeetCode 327. Count of Range Sum (区间和的个数) - https://leetcode.com/problems/count-of-range-sum/
 * 19. LeetCode 493. Reverse Pairs (翻转对) - https://leetcode.com/problems/reverse-pairs/
 * 20. LeetCode 987. Vertical Order Traversal of a Binary Tree (二叉树的垂序遍历) - https://leetcode.com/problems/vertical-order-traversal-of-a-binary-tree/
 * 21. Codeforces 4C. Registration System (注册系统) - https://codeforces.com/problemset/problem/4/C
 * 22. AtCoder ABC 217 D - Cutting Woods (切割木材) - https://atcoder.jp/contests/abc217/tasks/abc217_d
 * 23. USACO Silver: Why Did the Cow Cross the Road (为什么奶牛要过马路) - http://www.usaco.org/index.php?page=viewproblem2&cpid=714
 * 24. 洛谷 P3374 【模板】树状数组 1 (模板树状数组1) - https://www.luogu.com.cn/problem/P3374
 * 25. CodeChef STFOOD (街头食物) - https://www.codechef.com/problems/STFOOD
 * 26. SPOJ ANARC09A - Seinfeld (宋飞正传) - https://www.spoj.com/problems/ANARC09A/
 * 27. Project Euler Problem 1: Multiples of 3 and 5 (3和5的倍数) - https://projecteuler.net/problem=1
 * 28. HackerRank Frequency Queries (频率查询) - https://www.hackerrank.com/challenges/frequency-queries
 * 29. 计蒜客 T1100: 计算2的N次方 (计算2的N次方) - https://www.jisuanke.com/t/T1100
 * 30. 杭电 OJ 1002: A + B Problem II (A+B问题II) - http://acm.hdu.edu.cn/showproblem.php?pid=1002
 * 31. 牛客网 剑指Offer 03: 数组中重复的数字 (数组中重复的数字) - https://www.nowcoder.com/practice/623a5ac0ea5b4e5f95552655361ae0a8
 * 32. acwing 799. 最长连续不重复子序列 (最长连续不重复子序列) - https://www.acwing.com/problem/content/801/
 * 33. POJ 1002: 487-3279 (电话号码) - http://poj.org/problem?id=1002
 * 34. UVa OJ 100: The 3n + 1 problem (3n+1问题) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=36
 * 35. Timus OJ 1001: Reverse Root (反转平方根) - https://acm.timus.ru/problem.aspx?space=1&num=1001
 * 36. Aizu OJ ALDS1_4_C: Dictionary (字典) - http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_4_C
 * 37. Comet OJ Contest #0: 热身赛 A. 签到题 (签到题) - https://cometoj.com/contest/0/problem/A
 * 38. MarsCode 火星编程竞赛: 字符串去重排序 (字符串去重排序) - https://www.marscode.cn/contest/1/problem/1001
 * 39. ZOJ 1001: A + B Problem (A+B问题) - http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1001
 * 40. LOJ 100: 顺序的分数 (顺序的分数) - https://loj.ac/p/100
 * 41. 各大高校OJ: 清华大学OJ 1000: A+B Problem (A+B问题) - http://dsa.cs.tsinghua.edu.cn/oj/problem.shtml?id=1000
 */

public class Code02_TreeSetAndTreeMap {

	/**
	 * LeetCode 220. Contains Duplicate III (存在重复元素 III)
	 * 
	 * 题目描述：
	 * 给你一个整数数组 nums 和两个整数 k 和 t 。请你判断是否存在两个不同下标 i 和 j，
	 * 使得 abs(nums[i] - nums[j]) <= t ，同时又满足 abs(i - j) <= k 。
	 * 如果存在则返回 true，不存在返回 false。
	 * 
	 * 示例：
	 * 输入：nums = [1,2,3,1], k = 3, t = 0
	 * 输出：true
	 * 
	 * 输入：nums = [1,0,1,1], k = 1, t = 2
	 * 输出：true
	 * 
	 * 输入：nums = [1,5,9,1,5,9], k = 2, t = 3
	 * 输出：false
	 * 
	 * 约束条件：
	 * 0 <= nums.length <= 2 * 10^4
	 * -2^31 <= nums[i] <= 2^31 - 1
	 * 0 <= k <= 10^4
	 * 0 <= t <= 2^31 - 1
	 * 
	 * 解题思路：
	 * 使用TreeSet维护一个大小为k的滑动窗口，对于每个新元素，在TreeSet中查找是否存在一个元素
	 * 与当前元素的差值不超过t。利用TreeSet的ceiling和floor方法可以高效地找到最接近的元素。
	 * 
	 * 时间复杂度：O(n log min(n,k))，其中n是数组长度
	 * 空间复杂度：O(min(n,k))，用于存储滑动窗口中的元素
	 * 
	 * @param nums 整数数组
	 * @param k 下标差值上限
	 * @param t 元素差值上限
	 * @return 如果存在满足条件的两个元素返回true，否则返回false
	 */
	public static boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
		// 使用TreeSet维护滑动窗口
		TreeSet<Long> set = new TreeSet<>();
		
		for (int i = 0; i < nums.length; i++) {
			// 查找大于等于当前元素的最小元素
			Long ceiling = set.ceiling((long) nums[i]);
			if (ceiling != null && ceiling - nums[i] <= t) {
				return true;
			}
			
			// 查找小于等于当前元素的最大元素
			Long floor = set.floor((long) nums[i]);
			if (floor != null && nums[i] - floor <= t) {
				return true;
			}
			
			// 添加当前元素到TreeSet
			set.add((long) nums[i]);
			
			// 维护滑动窗口大小为k
			if (set.size() > k) {
				set.remove((long) nums[i - k]);
			}
		}
		
		return false;
	}

	/**
	 * LeetCode 933. Number of Recent Calls (最近的请求次数)
	 * 
	 * 题目描述：
	 * 写一个 RecentCounter 类来计算特定时间范围内最近的请求。
	 * 请你实现 RecentCounter 类：
	 * RecentCounter() 初始化计数器，请求数为 0 。
	 * int ping(int t) 在时间 t 添加一个新请求，其中 t 表示以毫秒为单位的某个时间，
	 * 并返回过去 3000 毫秒内发生的所有请求数（包括新请求）。
	 * 精确地说，返回在 [t-3000, t] 内发生的请求数。
	 * 保证每次对 ping 的调用都使用比之前更大的 t 值。
	 * 
	 * 示例：
	 * 输入：
	 * ["RecentCounter", "ping", "ping", "ping", "ping"]
	 * [[], [1], [100], [3001], [3002]]
	 * 输出：
	 * [null, 1, 2, 3, 3]
	 * 
	 * 约束条件：
	 * 1 <= t <= 10^9
	 * 保证每次对 ping 调用所使用的 t 值都 严格递增
	 * 至多调用 ping 方法 10^4 次
	 * 
	 * 解题思路：
	 * 使用TreeSet存储所有请求的时间戳，每次ping时，使用subSet方法获取[t-3000, t]范围内的请求数量。
	 * 
	 * 时间复杂度：O(log n) 每次ping操作
	 * 空间复杂度：O(n) 存储所有请求
	 */
	static class RecentCounter {
		private TreeSet<Integer> requests;
		
		public RecentCounter() {
			requests = new TreeSet<>();
		}
		
		public int ping(int t) {
			requests.add(t);
			// 获取[t-3000, t]范围内的请求数量
			return requests.subSet(t - 3000, true, t, true).size();
		}
	}

	/**
	 * LeetCode 729. My Calendar I (我的日程安排表 I)
	 * 
	 * 题目描述：
	 * 实现一个 MyCalendar 类来存放你的日程安排。如果要添加的日程安排不会造成 重复预订 ，则可以存储这个新的日程安排。
	 * 当两个日程安排有一些时间上的交叉时（例如两个日程安排都在同一时间内），就会产生 重复预订 。
	 * 日程可以用一对整数 start 和 end 表示，这里的时间是半开区间，即 [start, end)，
	 * 实数 x 的范围为 start <= x < end 。
	 * 
	 * 实现 MyCalendar 类：
	 * MyCalendar() 初始化日历对象。
	 * boolean book(int start, int end) 如果可以将日程安排成功添加到日历中而不会导致重复预订，返回 true 。
	 * 否则，返回 false 并且不要将该日程安排添加到日历中。
	 * 
	 * 示例：
	 * 输入：
	 * ["MyCalendar", "book", "book", "book"]
	 * [[], [10, 20], [15, 25], [20, 30]]
	 * 输出：
	 * [null, true, false, true]
	 * 
	 * 约束条件：
	 * 0 <= start < end <= 10^9
	 * 每个测试用例，调用 book 方法的次数最多不超过 1000 次。
	 * 
	 * 解题思路：
	 * 使用TreeMap存储已预订的日程，key为开始时间，value为结束时间。
	 * 对于新的日程[start, end)，使用TreeMap的floorEntry和ceilingEntry方法查找可能冲突的日程：
	 * 1. 查找开始时间小于等于start的最大日程，检查其结束时间是否大于start
	 * 2. 查找开始时间大于等于start的最小日程，检查其开始时间是否小于end
	 * 如果没有冲突，则添加新日程。
	 * 
	 * 时间复杂度：O(log n) 每次book操作
	 * 空间复杂度：O(n) 存储所有日程
	 */
	static class MyCalendar {
		private TreeMap<Integer, Integer> calendar;
		
		public MyCalendar() {
			calendar = new TreeMap<>();
		}
		
		public boolean book(int start, int end) {
			// 查找开始时间小于等于start的最大日程
			java.util.Map.Entry<Integer, Integer> floor = calendar.floorEntry(start);
			// 查找开始时间大于等于start的最小日程
			java.util.Map.Entry<Integer, Integer> ceiling = calendar.ceilingEntry(start);
			
			// 检查是否与floor日程冲突
			if (floor != null && floor.getValue() > start) {
				return false;
			}
			
			// 检查是否与ceiling日程冲突
			if (ceiling != null && ceiling.getKey() < end) {
				return false;
			}
			
			// 没有冲突，添加新日程
			calendar.put(start, end);
			return true;
		}
	}

	/**
	 * HackerRank Java TreeSet (Java树集)
	 * 
	 * 题目描述：
	 * 给定一个整数列表，找出列表中所有不同的数字，并按升序排列。
	 * 
	 * 示例：
	 * 输入：[1, 2, 3, 2, 1, 4, 5, 4]
	 * 输出：[1, 2, 3, 4, 5]
	 * 
	 * 解题思路：
	 * 使用TreeSet存储所有数字，由于TreeSet的特性，元素会自动排序且去重。
	 * 
	 * 时间复杂度：O(n log n)，其中n是数组长度
	 * 空间复杂度：O(n)，用于存储TreeSet
	 * 
	 * @param nums 整数数组
	 * @return 排序后的不重复元素数组
	 */
	public static int[] sortAndRemoveDuplicates(int[] nums) {
		TreeSet<Integer> set = new TreeSet<>();
		for (int num : nums) {
			set.add(num);
		}
		
		int[] result = new int[set.size()];
		int index = 0;
		for (int num : set) {
			result[index++] = num;
		}
		
		return result;
	}

	/**
	 * LeetCode 315. Count of Smaller Numbers After Self (计算右侧小于当前元素的个数)
	 * 
	 * 题目描述：
	 * 给你一个整数数组 nums ，按要求返回一个新数组 counts 。数组 counts 有该性质： 
	 * counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
	 * 
	 * 示例：
	 * 输入：nums = [5,2,6,1]
	 * 输出：[2,1,1,0]
	 * 
	 * 解释：
	 * 5 的右侧有 2 个更小的元素 (2 和 1)
	 * 2 的右侧仅有 1 个更小的元素 (1)
	 * 6 的右侧有 1 个更小的元素 (1)
	 * 1 的右侧有 0 个更小的元素
	 * 
	 * 约束条件：
	 * 1 <= nums.length <= 10^5
	 * -10^4 <= nums[i] <= 10^4
	 * 
	 * 解题思路：
	 * 从右向左遍历数组，使用TreeSet维护已遍历的元素。
	 * 对于每个元素，在TreeSet中查找小于它的元素个数。
	 * 
	 * 时间复杂度：O(n log n)，其中n是数组长度
	 * 空间复杂度：O(n)，用于存储TreeSet
	 * 
	 * @param nums 整数数组
	 * @return 每个元素右侧小于它的元素数量数组
	 */
	public static List<Integer> countSmaller(int[] nums) {
		List<Integer> result = new ArrayList<>();
		TreeSet<Integer> set = new TreeSet<>();
		
		// 从右向左遍历
		for (int i = nums.length - 1; i >= 0; i--) {
			// 查找小于当前元素的元素个数
			int count = set.headSet(nums[i]).size();
			result.add(0, count); // 在列表开头插入
			set.add(nums[i]);
		}
		
		return result;
	}

	/**
	 * LeetCode 493. Reverse Pairs (翻转对)
	 * 
	 * 题目描述：
	 * 给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] 我们将 (i, j) 称作一个重要翻转对。
	 * 你需要返回给定数组中的重要翻转对的数量。
	 * 
	 * 示例：
	 * 输入: [1,3,2,3,1]
	 * 输出: 2
	 * 
	 * 输入: [2,4,3,5,1]
	 * 输出: 3
	 * 
	 * 约束条件：
	 * 给定数组的长度不会超过50000。
	 * 输入数组中的所有数字都在32位整数的表示范围内。
	 * 
	 * 解题思路：
	 * 使用TreeSet维护已遍历的元素，对于每个新元素，在TreeSet中查找满足条件的元素个数。
	 * 
	 * 时间复杂度：O(n log n)，其中n是数组长度
	 * 空间复杂度：O(n)，用于存储TreeSet
	 * 
	 * @param nums 整数数组
	 * @return 翻转对的数量
	 */
	public static int reversePairs(int[] nums) {
		int count = 0;
		TreeSet<Long> set = new TreeSet<>();
		
		// 从右向左遍历
		for (int i = nums.length - 1; i >= 0; i--) {
			// 查找小于nums[i]/2.0的元素个数
			long target = (long) nums[i] * 2;
			count += set.headSet(target, false).size();
			set.add((long) nums[i]);
		}
		
		return count;
	}

	public static void main(String[] args) {
		// 底层红黑树
		TreeMap<Integer, String> treeMap = new TreeMap<>();
		treeMap.put(5, "这是5");
		treeMap.put(7, "这是7");
		treeMap.put(1, "这是1");
		treeMap.put(2, "这是2");
		treeMap.put(3, "这是3");
		treeMap.put(4, "这是4");
		treeMap.put(8, "这是8");

		System.out.println(treeMap.containsKey(1));
		System.out.println(treeMap.containsKey(10));
		System.out.println(treeMap.get(4));
		treeMap.put(4, "张三是4");
		System.out.println(treeMap.get(4));

		treeMap.remove(4);
		System.out.println(treeMap.get(4) == null);

		System.out.println(treeMap.firstKey());
		System.out.println(treeMap.lastKey());
		// TreeMap中，所有的key，<= 4且最近的key是什么
		System.out.println(treeMap.floorKey(4));
		// TreeMap中，所有的key，>= 4且最近的key是什么
		System.out.println(treeMap.ceilingKey(4));

		System.out.println("========");

		TreeSet<Integer> set = new TreeSet<>();
		set.add(3);
		set.add(3);
		set.add(4);
		set.add(4);
		System.out.println("有序表大小 : " + set.size());
		while (!set.isEmpty()) {
			System.out.println(set.pollFirst());
			// System.out.println(set.pollLast());
		}

		// 堆，默认小根堆、如果要大根堆，定制比较器！
		PriorityQueue<Integer> heap1 = new PriorityQueue<>();
		heap1.add(3);
		heap1.add(3);
		heap1.add(4);
		heap1.add(4);
		System.out.println("堆大小 : " + heap1.size());
		while (!heap1.isEmpty()) {
			System.out.println(heap1.poll());
		}

		// 定制的大根堆，用比较器！
		PriorityQueue<Integer> heap2 = new PriorityQueue<>((a, b) -> b - a);
		heap2.add(3);
		heap2.add(3);
		heap2.add(4);
		heap2.add(4);
		System.out.println("堆大小 : " + heap2.size());
		while (!heap2.isEmpty()) {
			System.out.println(heap2.poll());
		}
		
		// 测试添加的题目
		System.out.println("===========");
		System.out.println("测试存在重复元素 III:");
		int[] nums = {1, 2, 3, 1};
		System.out.println(containsNearbyAlmostDuplicate(nums, 3, 0)); // true
		
		System.out.println("测试最近的请求次数:");
		RecentCounter counter = new RecentCounter();
		System.out.println(counter.ping(1));     // 1
		System.out.println(counter.ping(100));   // 2
		System.out.println(counter.ping(3001));  // 3
		System.out.println(counter.ping(3002));  // 3
		
		System.out.println("测试我的日程安排表:");
		MyCalendar calendar = new MyCalendar();
		System.out.println(calendar.book(10, 20)); // true
		System.out.println(calendar.book(15, 25)); // false
		System.out.println(calendar.book(20, 30)); // true

		// 测试HackerRank Java TreeSet
		System.out.println("测试HackerRank Java TreeSet:");
		int[] nums1 = {1, 2, 3, 2, 1, 4, 5, 4};
		int[] result1 = sortAndRemoveDuplicates(nums1);
		System.out.println(Arrays.toString(result1)); // [1, 2, 3, 4, 5]

		// 测试计算右侧小于当前元素的个数
		System.out.println("测试计算右侧小于当前元素的个数:");
		int[] nums2 = {5, 2, 6, 1};
		List<Integer> result2 = countSmaller(nums2);
		System.out.println(result2); // [2, 1, 1, 0]

		// 测试翻转对
		System.out.println("测试翻转对:");
		int[] nums3 = {1, 3, 2, 3, 1};
		System.out.println(reversePairs(nums3)); // 2
	}
}