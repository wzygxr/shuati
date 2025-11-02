import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * HashSet和HashMap相关题目与解析
 * 
 * HashSet基于HashMap实现，底层使用哈希表，查询时间复杂度O(1)，元素无序
 * HashMap基于哈希表实现，查询时间复杂度O(1)，键值对无序
 * 
 * 相关平台题目：
 * 1. LeetCode 1. Two Sum (两数之和) - https://leetcode.com/problems/two-sum/
 * 2. LeetCode 3. Longest Substring Without Repeating Characters (无重复字符的最长子串) - https://leetcode.com/problems/longest-substring-without-repeating-characters/
 * 3. LeetCode 36. Valid Sudoku (有效的数独) - https://leetcode.com/problems/valid-sudoku/
 * 4. LeetCode 136. Single Number (只出现一次的数字) - https://leetcode.com/problems/single-number/
 * 5. LeetCode 202. Happy Number (快乐数) - https://leetcode.com/problems/happy-number/
 * 6. LeetCode 217. Contains Duplicate (存在重复元素) - https://leetcode.com/problems/contains-duplicate/
 * 7. LeetCode 219. Contains Duplicate II (存在重复元素 II) - https://leetcode.com/problems/contains-duplicate-ii/
 * 8. LeetCode 242. Valid Anagram (有效的字母异位词) - https://leetcode.com/problems/valid-anagram/
 * 9. LeetCode 349. Intersection of Two Arrays (两个数组的交集) - https://leetcode.com/problems/intersection-of-two-arrays/
 * 10. LeetCode 350. Intersection of Two Arrays II (两个数组的交集 II) - https://leetcode.com/problems/intersection-of-two-arrays-ii/
 * 11. LeetCode 387. First Unique Character in a String (字符串中的第一个唯一字符) - https://leetcode.com/problems/first-unique-character-in-a-string/
 * 12. LeetCode 448. Find All Numbers Disappeared in an Array (找到所有数组中消失的数字) - https://leetcode.com/problems/find-all-numbers-disappeared-in-an-array/
 * 13. LeetCode 575. Distribute Candies (分糖果) - https://leetcode.com/problems/distribute-candies/
 * 14. LeetCode 811. Subdomain Visit Count (子域名访问计数) - https://leetcode.com/problems/subdomain-visit-count/
 * 15. LeetCode 705. Design HashSet (设计哈希集合) - https://leetcode.com/problems/design-hashset/
 * 16. LeetCode 706. Design HashMap (设计哈希映射) - https://leetcode.com/problems/design-hashmap/
 * 17. HackerRank Java Hashset (Java哈希集) - https://www.hackerrank.com/challenges/java-hashset
 * 18. LeetCode 128. Longest Consecutive Sequence (最长连续序列) - https://leetcode.com/problems/longest-consecutive-sequence/
 * 19. LeetCode 49. Group Anagrams (字母异位词分组) - https://leetcode.com/problems/group-anagrams/
 * 20. LeetCode 347. Top K Frequent Elements (前 K 个高频元素) - https://leetcode.com/problems/top-k-frequent-elements/
 * 21. LeetCode 3. Longest Substring Without Repeating Characters (无重复字符的最长子串) - https://leetcode.com/problems/longest-substring-without-repeating-characters/
 * 22. LeetCode 36. Valid Sudoku (有效的数独) - https://leetcode.com/problems/valid-sudoku/
 * 23. LeetCode 141. Linked List Cycle (环形链表) - https://leetcode.com/problems/linked-list-cycle/
 * 24. LeetCode 160. Intersection of Two Linked Lists (相交链表) - https://leetcode.com/problems/intersection-of-two-linked-lists/
 * 25. LintCode 547. Intersection of Two Arrays (两个数组的交集) - https://www.lintcode.com/problem/intersection-of-two-arrays/
 * 26. Codeforces 4C. Registration System (注册系统) - https://codeforces.com/problemset/problem/4/C
 * 27. AtCoder ABC 217 D - Cutting Woods (切割木材) - https://atcoder.jp/contests/abc217/tasks/abc217_d
 * 28. USACO Silver: Why Did the Cow Cross the Road (为什么奶牛要过马路) - http://www.usaco.org/index.php?page=viewproblem2&cpid=714
 * 29. 洛谷 P3374 【模板】树状数组 1 (模板树状数组1) - https://www.luogu.com.cn/problem/P3374
 * 30. CodeChef STFOOD (街头食物) - https://www.codechef.com/problems/STFOOD
 * 31. SPOJ ANARC09A - Seinfeld (宋飞正传) - https://www.spoj.com/problems/ANARC09A/
 * 32. Project Euler Problem 1: Multiples of 3 and 5 (3和5的倍数) - https://projecteuler.net/problem=1
 * 33. HackerRank Frequency Queries (频率查询) - https://www.hackerrank.com/challenges/frequency-queries
 * 34. 计蒜客 T1100: 计算2的N次方 (计算2的N次方) - https://www.jisuanke.com/t/T1100
 * 35. 杭电 OJ 1002: A + B Problem II (A+B问题II) - http://acm.hdu.edu.cn/showproblem.php?pid=1002
 * 36. 牛客网 剑指Offer 03: 数组中重复的数字 (数组中重复的数字) - https://www.nowcoder.com/practice/623a5ac0ea5b4e5f95552655361ae0a8
 * 37. acwing 799. 最长连续不重复子序列 (最长连续不重复子序列) - https://www.acwing.com/problem/content/801/
 * 38. POJ 1002: 487-3279 (电话号码) - http://poj.org/problem?id=1002
 * 39. UVa OJ 100: The 3n + 1 problem (3n+1问题) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=36
 * 40. Timus OJ 1001: Reverse Root (反转平方根) - https://acm.timus.ru/problem.aspx?space=1&num=1001
 * 41. Aizu OJ ALDS1_4_C: Dictionary (字典) - http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_4_C
 * 42. Comet OJ Contest #0: 热身赛 A. 签到题 (签到题) - https://cometoj.com/contest/0/problem/A
 * 43. MarsCode 火星编程竞赛: 字符串去重排序 (字符串去重排序) - https://www.marscode.cn/contest/1/problem/1001
 * 44. ZOJ 1001: A + B Problem (A+B问题) - http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1001
 * 45. LOJ 100: 顺序的分数 (顺序的分数) - https://loj.ac/p/100
 * 46. 各大高校OJ: 清华大学OJ 1000: A+B Problem (A+B问题) - http://dsa.cs.tsinghua.edu.cn/oj/problem.shtml?id=1000
 */

public class Code01_HashSetAndHashMap {

	/**
	 * LeetCode 1. Two Sum (两数之和)
	 * 
	 * 题目描述：
	 * 给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值 target 的那两个整数，
	 * 并返回它们的数组下标。你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。
	 * 
	 * 示例：
	 * 输入：nums = [2,7,11,15], target = 9
	 * 输出：[0,1]
	 * 解释：因为 nums[0] + nums[1] == 9 ，返回 [0, 1] 。
	 * 
	 * 约束条件：
	 * 2 <= nums.length <= 10^4
	 * -10^9 <= nums[i] <= 10^9
	 * -10^9 <= target <= 10^9
	 * 只会存在一个有效答案
	 * 
	 * 进阶：你可以想出一个时间复杂度小于 O(n^2) 的算法吗？
	 * 
	 * 解题思路：
	 * 使用HashMap存储已经遍历过的数字及其索引，对于每个数字，检查target - 当前数字是否存在于HashMap中，
	 * 如果存在则返回两个数字的索引，否则将当前数字和索引存入HashMap。
	 * 
	 * 时间复杂度：O(n)，其中n是数组长度，我们只需要遍历数组一次
	 * 空间复杂度：O(n)，最坏情况下需要存储数组中所有元素及其索引
	 * 
	 * @param nums 整数数组
	 * @param target 目标值
	 * @return 两个整数的数组下标
	 */
	public static int[] twoSum(int[] nums, int target) {
		// 创建HashMap存储数字和其索引
		HashMap<Integer, Integer> map = new HashMap<>();
		
		// 遍历数组
		for (int i = 0; i < nums.length; i++) {
			// 计算需要找到的另一个数字
			int complement = target - nums[i];
			
			// 检查该数字是否已存在于HashMap中
			if (map.containsKey(complement)) {
				// 如果存在，返回两个数字的索引
				return new int[] { map.get(complement), i };
			}
			
			// 将当前数字和索引存入HashMap
			map.put(nums[i], i);
		}
		
		// 根据题目保证总会有一个解，这里仅为避免编译错误
		throw new IllegalArgumentException("No two sum solution");
	}

	/**
	 * LeetCode 242. Valid Anagram (有效的字母异位词)
	 * 
	 * 题目描述：
	 * 给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的字母异位词。
	 * 注意：若 s 和 t 中每个字符出现的次数都相同，则称 s 和 t 互为字母异位词。
	 * 
	 * 示例：
	 * 输入: s = "anagram", t = "nagaram"
	 * 输出: true
	 * 
	 * 输入: s = "rat", t = "car"
	 * 输出: false
	 * 
	 * 约束条件：
	 * 1 <= s.length, t.length <= 5 * 10^4
	 * s 和 t 仅包含小写字母
	 * 
	 * 进阶: 如果输入字符串包含 unicode 字符怎么办？你能否调整你的解法来应对这种情况？
	 * 
	 * 解题思路：
	 * 使用HashMap记录字符串s中每个字符出现的次数，然后遍历字符串t，对每个字符在HashMap中对应的计数减1，
	 * 如果某个字符不存在或计数小于0，则返回false。最后检查HashMap中是否所有字符计数都为0。
	 * 
	 * 时间复杂度：O(n)，其中n是字符串长度，需要遍历两个字符串
	 * 空间复杂度：O(1)，因为字符集大小固定（小写字母26个），所以空间复杂度是常数
	 * 
	 * @param s 字符串s
	 * @param t 字符串t
	 * @return 如果t是s的字母异位词返回true，否则返回false
	 */
	public static boolean isAnagram(String s, String t) {
		// 如果两个字符串长度不同，肯定不是字母异位词
		if (s.length() != t.length()) {
			return false;
		}
		
		// 创建HashMap记录字符出现次数
		HashMap<Character, Integer> charCount = new HashMap<>();
		
		// 遍历字符串s，统计每个字符出现次数
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			charCount.put(c, charCount.getOrDefault(c, 0) + 1);
		}
		
		// 遍历字符串t，减少对应字符计数
		for (int i = 0; i < t.length(); i++) {
			char c = t.charAt(i);
			// 如果字符不存在或计数已为0，返回false
			if (!charCount.containsKey(c) || charCount.get(c) == 0) {
				return false;
			}
			// 减少字符计数
			charCount.put(c, charCount.get(c) - 1);
		}
		
		// 检查所有字符计数是否为0
		for (int count : charCount.values()) {
			if (count != 0) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * LeetCode 349. Intersection of Two Arrays (两个数组的交集)
	 * 
	 * 题目描述：
	 * 给定两个数组 nums1 和 nums2 ，返回它们的交集。输出结果中的每个元素一定是唯一的。
	 * 我们可以不考虑输出结果的顺序。
	 * 
	 * 示例：
	 * 输入：nums1 = [1,2,2,1], nums2 = [2,2]
	 * 输出：[2]
	 * 
	 * 输入：nums1 = [4,9,5], nums2 = [9,4,9,8,4]
	 * 输出：[9,4]
	 * 解释：[4,9] 也是可通过的
	 * 
	 * 约束条件：
	 * 1 <= nums1.length, nums2.length <= 1000
	 * 0 <= nums1[i], nums2[i] <= 1000
	 * 
	 * 解题思路：
	 * 使用HashSet存储nums1中的所有元素，然后遍历nums2，检查每个元素是否存在于HashSet中，
	 * 如果存在则加入结果集，并从HashSet中移除该元素以避免重复。
	 * 
	 * 时间复杂度：O(m+n)，其中m和n分别是两个数组的长度
	 * 空间复杂度：O(m)，用于存储nums1中的元素
	 * 
	 * @param nums1 数组1
	 * @param nums2 数组2
	 * @return 交集数组
	 */
	public static int[] intersection(int[] nums1, int[] nums2) {
		// 使用HashSet存储nums1中的元素
		HashSet<Integer> set1 = new HashSet<>();
		for (int num : nums1) {
			set1.add(num);
		}
		
		// 使用HashSet存储交集结果，自动去重
		HashSet<Integer> intersection = new HashSet<>();
		
		// 遍历nums2，查找交集
		for (int num : nums2) {
			if (set1.contains(num)) {
				intersection.add(num);
			}
		}
		
		// 将结果转换为数组
		int[] result = new int[intersection.size()];
		int index = 0;
		for (int num : intersection) {
			result[index++] = num;
		}
		
		return result;
	}

	/**
	 * LeetCode 705. Design HashSet (设计哈希集合)
	 * 
	 * 题目描述：
	 * 不使用任何内建的哈希表库设计一个哈希集合（HashSet）。
	 * 实现 MyHashSet 类：
	 * void add(key) 向哈希集合中插入值 key 。
	 * bool contains(key) 返回哈希集合中是否存在这个值 key 。
	 * void remove(key) 将给定值 key 从哈希集合中删除。如果哈希集合中没有这个值，什么也不做。
	 * 
	 * 示例：
	 * 输入：
	 * ["MyHashSet", "add", "add", "contains", "contains", "add", "contains", "remove", "contains"]
	 * [[], [1], [2], [1], [3], [2], [2], [2], [2]]
	 * 输出：
	 * [null, null, null, true, false, null, true, null, false]
	 * 
	 * 约束条件：
	 * 0 <= key <= 10^6
	 * 最多调用 10^4 次 add、remove 和 contains
	 * 
	 * 解题思路：
	 * 使用链地址法实现哈希表，创建一个固定大小的数组，每个数组元素是一个链表。
	 * 当发生哈希冲突时，将元素添加到对应位置的链表中。
	 * 
	 * 时间复杂度：O(n/b)，其中n是元素个数，b是桶数（在实际实现中我们使用10000作为桶数）
	 * 空间复杂度：O(n)，存储所有元素
	 */
	static class MyHashSet {
		private static final int BASE = 10000;
		private LinkedList<Integer>[] data;

		/** Initialize your data structure here. */
		public MyHashSet() {
			data = new LinkedList[BASE];
			for (int i = 0; i < BASE; ++i) {
				data[i] = new LinkedList<Integer>();
			}
		}

		public void add(int key) {
			int h = hash(key);
			Iterator<Integer> iterator = data[h].iterator();
			while (iterator.hasNext()) {
				Integer element = iterator.next();
				if (element == key) {
					return;
				}
			}
			data[h].offerLast(key);
		}

		public void remove(int key) {
			int h = hash(key);
			Iterator<Integer> iterator = data[h].iterator();
			while (iterator.hasNext()) {
				Integer element = iterator.next();
				if (element == key) {
					iterator.remove();
					return;
				}
			}
		}

		/** Returns true if this set contains the specified element */
		public boolean contains(int key) {
			int h = hash(key);
			Iterator<Integer> iterator = data[h].iterator();
			while (iterator.hasNext()) {
				Integer element = iterator.next();
				if (element == key) {
					return true;
				}
			}
			return false;
		}

		private static int hash(int key) {
			return key % BASE;
		}
	}

	/**
	 * LeetCode 706. Design HashMap (设计哈希映射)
	 * 
	 * 题目描述：
	 * 不使用任何内建的哈希表库设计一个哈希映射（HashMap）。
	 * 实现 MyHashMap 类：
	 * MyHashMap() 用空映射初始化对象
	 * void put(int key, int value) 向 HashMap 插入一个键值对 (key, value) 。如果 key 已经存在于映射中，则更新其对应的值 value 。
	 * int get(int key) 返回特定的 key 所映射的 value ；如果映射中不包含 key 的映射，返回 -1 。
	 * void remove(key) 如果映射中存在 key 的映射，则移除 key 和它所对应的 value 。
	 * 
	 * 示例：
	 * 输入：
	 * ["MyHashMap", "put", "put", "get", "get", "put", "get", "remove", "get"]
	 * [[], [1, 1], [2, 2], [1], [3], [2, 1], [2], [2], [2]]
	 * 输出：
	 * [null, null, null, 1, -1, null, 1, null, -1]
	 * 
	 * 约束条件：
	 * 0 <= key, value <= 10^6
	 * 最多调用 10^4 次 put、get 和 remove 方法
	 * 
	 * 解题思路：
	 * 使用链地址法实现哈希表，创建一个固定大小的数组，每个数组元素是一个链表。
	 * 每个链表节点存储键值对，当发生哈希冲突时，将节点添加到对应位置的链表中。
	 * 
	 * 时间复杂度：O(n/b)，其中n是元素个数，b是桶数（在实际实现中我们使用10000作为桶数）
	 * 空间复杂度：O(n)，存储所有键值对
	 */
	static class MyHashMap {
		private static final int BASE = 10000;
		private LinkedList<Pair>[] data;

		/** Initialize your data structure here. */
		public MyHashMap() {
			data = new LinkedList[BASE];
			for (int i = 0; i < BASE; ++i) {
				data[i] = new LinkedList<Pair>();
			}
		}

		/** value will always be non-negative. */
		public void put(int key, int value) {
			int h = hash(key);
			Iterator<Pair> iterator = data[h].iterator();
			while (iterator.hasNext()) {
				Pair pair = iterator.next();
				if (pair.getKey() == key) {
					pair.setValue(value);
					return;
				}
			}
			data[h].offerLast(new Pair(key, value));
		}

		/** Returns the value to which the specified key is mapped, or -1 if this map contains no mapping for the key */
		public int get(int key) {
			int h = hash(key);
			Iterator<Pair> iterator = data[h].iterator();
			while (iterator.hasNext()) {
				Pair pair = iterator.next();
				if (pair.getKey() == key) {
					return pair.getValue();
				}
			}
			return -1;
		}

		/** Removes the mapping of the specified value key if this map contains a mapping for the key */
		public void remove(int key) {
			int h = hash(key);
			Iterator<Pair> iterator = data[h].iterator();
			while (iterator.hasNext()) {
				Pair pair = iterator.next();
				if (pair.getKey() == key) {
					iterator.remove();
					return;
				}
			}
		}

		private static int hash(int key) {
			return key % BASE;
		}

		private class Pair {
			private int key;
			private int value;

			public Pair(int key, int value) {
				this.key = key;
				this.value = value;
			}

			public int getKey() {
				return key;
			}

			public int getValue() {
				return value;
			}

			public void setValue(int value) {
				this.value = value;
			}
		}
	}

	/**
	 * HackerRank Java Hashset (Java哈希集)
	 * 
	 * 题目描述：
	 * 给定n对字符串，每对字符串由两个部分组成。找出有多少对独特的字符串对。
	 * 
	 * 示例：
	 * 输入：
	 * 5
	 * john tom
	 * john mary
	 * john tom
	 * mary anna
	 * mary anna
	 * 输出：
	 * 3
	 * 
	 * 解题思路：
	 * 使用HashSet存储字符串对，由于HashSet的特性，重复的元素只会存储一次。
	 * 最后返回HashSet的大小即可。
	 * 
	 * 时间复杂度：O(n)，其中n是字符串对的数量
	 * 空间复杂度：O(n)，用于存储所有独特的字符串对
	 * 
	 * @param pairs 字符串对数组
	 * @return 独特字符串对的数量
	 */
	public static int countUniquePairs(String[][] pairs) {
		HashSet<String> set = new HashSet<>();
		for (String[] pair : pairs) {
			// 将两个字符串连接作为唯一标识
			set.add(pair[0] + " " + pair[1]);
		}
		return set.size();
	}

	/**
	 * LeetCode 128. Longest Consecutive Sequence (最长连续序列)
	 * 
	 * 题目描述：
	 * 给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
	 * 请你设计并实现时间复杂度为 O(n) 的算法解决此问题。
	 * 
	 * 示例：
	 * 输入：nums = [100,4,200,1,3,2]
	 * 输出：4
	 * 解释：最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4。
	 * 
	 * 输入：nums = [0,3,7,2,5,8,4,6,0,1]
	 * 输出：9
	 * 
	 * 约束条件：
	 * 0 <= nums.length <= 10^5
	 * -10^9 <= nums[i] <= 10^9
	 * 
	 * 解题思路：
	 * 使用HashSet存储所有数字，然后对于每个数字，如果它是某个序列的开始（即num-1不在HashSet中），
	 * 则从该数字开始向后查找连续的数字，计算序列长度。
	 * 
	 * 时间复杂度：O(n)，虽然有嵌套循环，但每个元素最多被访问两次
	 * 空间复杂度：O(n)，用于存储HashSet
	 * 
	 * @param nums 整数数组
	 * @return 最长连续序列的长度
	 */
	public static int longestConsecutive(int[] nums) {
		HashSet<Integer> numSet = new HashSet<>();
		for (int num : nums) {
			numSet.add(num);
		}

		int longestStreak = 0;

		for (int num : numSet) {
			// 只有当num-1不存在时，num才是一个序列的开始
			if (!numSet.contains(num - 1)) {
				int currentNum = num;
				int currentStreak = 1;

				// 向后查找连续的数字
				while (numSet.contains(currentNum + 1)) {
					currentNum += 1;
					currentStreak += 1;
				}

				longestStreak = Math.max(longestStreak, currentStreak);
			}
		}

		return longestStreak;
	}

	/**
	 * LeetCode 49. Group Anagrams (字母异位词分组)
	 * 
	 * 题目描述：
	 * 给你一个字符串数组，请你将字母异位词组合在一起。可以按任意顺序返回结果列表。
	 * 字母异位词是由重新排列源单词的所有字母得到的一个新单词。
	 * 
	 * 示例：
	 * 输入: strs = ["eat", "tea", "tan", "ate", "nat", "bat"]
	 * 输出: [["bat"],["nat","tan"],["ate","eat","tea"]]
	 * 
	 * 输入: strs = [""]
	 * 输出: [[""]]
	 * 
	 * 输入: strs = ["a"]
	 * 输出: [["a"]]
	 * 
	 * 约束条件：
	 * 1 <= strs.length <= 10^4
	 * 0 <= strs[i].length <= 100
	 * strs[i] 仅包含小写字母
	 * 
	 * 解题思路：
	 * 使用HashMap存储分组结果，键为排序后的字符串，值为该组的所有字符串。
	 * 对于每个字符串，将其字符排序后作为键，将原字符串添加到对应的列表中。
	 * 
	 * 时间复杂度：O(N*K*logK)，其中N是字符串数组的长度，K是字符串的最大长度
	 * 空间复杂度：O(N*K)，用于存储所有字符串
	 * 
	 * @param strs 字符串数组
	 * @return 字母异位词分组结果
	 */
	public static List<List<String>> groupAnagrams(String[] strs) {
		HashMap<String, List<String>> map = new HashMap<>();

		for (String str : strs) {
			// 将字符串转换为字符数组并排序
			char[] chars = str.toCharArray();
			Arrays.sort(chars);
			String key = new String(chars);

			// 如果键不存在，创建新的列表
			if (!map.containsKey(key)) {
				map.put(key, new ArrayList<>());
			}
			// 将原字符串添加到对应的列表中
			map.get(key).add(str);
		}

		return new ArrayList<>(map.values());
	}

	/**
	 * LeetCode 347. Top K Frequent Elements (前 K 个高频元素)
	 * 
	 * 题目描述：
	 * 给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。你可以按任意顺序返回答案。
	 * 
	 * 示例：
	 * 输入: nums = [1,1,1,2,2,3], k = 2
	 * 输出: [1,2]
	 * 
	 * 输入: nums = [1], k = 1
	 * 输出: [1]
	 * 
	 * 约束条件：
	 * 1 <= nums.length <= 10^5
	 * k 的取值范围是 [1, 数组中不相同的元素的个数]
	 * 题目数据保证答案唯一
	 * 
	 * 解题思路：
	 * 1. 使用HashMap统计每个元素的频率
	 * 2. 使用最小堆维护前k个高频元素
	 * 3. 遍历HashMap，维护堆的大小为k
	 * 4. 最后从堆中取出所有元素
	 * 
	 * 时间复杂度：O(N*logK)，其中N是数组长度
	 * 空间复杂度：O(N)，用于存储HashMap和堆
	 * 
	 * @param nums 整数数组
	 * @param k 需要返回的元素个数
	 * @return 前k个高频元素
	 */
	public static int[] topKFrequent(int[] nums, int k) {
		// 统计每个元素的频率
		HashMap<Integer, Integer> freqMap = new HashMap<>();
		for (int num : nums) {
			freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
		}

		// 使用最小堆维护前k个高频元素
		PriorityQueue<Integer> heap = new PriorityQueue<>((a, b) -> freqMap.get(a) - freqMap.get(b));

		// 遍历频率映射，维护堆的大小为k
		for (int key : freqMap.keySet()) {
			heap.add(key);
			if (heap.size() > k) {
				heap.poll();
			}
		}

		// 从堆中取出所有元素
		int[] result = new int[k];
		for (int i = 0; i < k; i++) {
			result[i] = heap.poll();
		}

		return result;
	}

	/**
	 * LeetCode 219. Contains Duplicate II (存在重复元素 II)
	 * 
	 * 题目描述：
	 * 给定一个整数数组和一个整数 k，判断数组中是否存在两个不同的索引 i 和 j，
	 * 使得 nums[i] == nums[j]，并且 i 和 j 的差的绝对值至多为 k。
	 * 
	 * 示例：
	 * 输入: nums = [1,2,3,1], k = 3
	 * 输出: true
	 * 
	 * 输入: nums = [1,0,1,1], k = 1
	 * 输出: true
	 * 
	 * 输入: nums = [1,2,3,1,2,3], k = 2
	 * 输出: false
	 * 
	 * 约束条件：
	 * 1 <= nums.length <= 10^5
	 * -10^9 <= nums[i] <= 10^9
	 * 0 <= k <= 10^5
	 * 
	 * 解题思路：
	 * 使用HashMap存储每个元素最后一次出现的索引，遍历数组时，检查当前元素是否在HashMap中存在，
	 * 如果存在且当前索引与存储的索引之差的绝对值小于等于k，则返回true；
	 * 否则更新HashMap中该元素的索引为当前索引。
	 * 
	 * 时间复杂度：O(n)，其中n是数组长度，我们只需要遍历数组一次
	 * 空间复杂度：O(min(n,k))，最坏情况下需要存储min(n,k)个元素
	 */
	public static boolean containsNearbyDuplicate(int[] nums, int k) {
		HashMap<Integer, Integer> map = new HashMap<>();
		for (int i = 0; i < nums.length; i++) {
			if (map.containsKey(nums[i])) {
				int prevIndex = map.get(nums[i]);
				if (i - prevIndex <= k) {
					return true;
				}
			}
			map.put(nums[i], i);
		}
		return false;
	}

	/**
	 * LeetCode 3. Longest Substring Without Repeating Characters (无重复字符的最长子串)
	 * 
	 * 题目描述：
	 * 给定一个字符串 s，请你找出其中不含有重复字符的最长子串的长度。
	 * 
	 * 示例：
	 * 输入: s = "abcabcbb"
	 * 输出: 3
	 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
	 * 
	 * 输入: s = "bbbbb"
	 * 输出: 1
	 * 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
	 * 
	 * 输入: s = "pwwkew"
	 * 输出: 3
	 * 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
	 * 注意，你的答案必须是子串的长度，"pwke" 是一个子序列，不是子串。
	 * 
	 * 约束条件：
	 * 0 <= s.length <= 5 * 10^4
	 * s 由英文字母、数字、符号和空格组成
	 * 
	 * 解题思路：
	 * 使用滑动窗口和HashMap，HashMap记录每个字符最后一次出现的位置。
	 * 维护一个左边界left，当遇到重复字符时，将left更新为重复字符上一次出现位置的下一个位置。
	 * 计算当前窗口长度i-left+1，并更新最大长度。
	 * 
	 * 时间复杂度：O(n)，其中n是字符串长度
	 * 空间复杂度：O(min(n, m))，其中m是字符集大小
	 */
	public static int lengthOfLongestSubstring(String s) {
		HashMap<Character, Integer> map = new HashMap<>();
		int maxLength = 0;
		int left = 0;
		
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			// 如果字符已存在且在当前窗口内，更新左边界
			if (map.containsKey(c) && map.get(c) >= left) {
				left = map.get(c) + 1;
			}
			// 更新字符位置
			map.put(c, i);
			// 更新最大长度
			maxLength = Math.max(maxLength, i - left + 1);
		}
		
		return maxLength;
	}

	/**
	 * LeetCode 36. Valid Sudoku (有效的数独)
	 * 
	 * 题目描述：
	 * 请你判断一个 9 x 9 的数独是否有效。只需要 根据以下规则 ，验证已经填入的数字是否有效即可。
	 * 1. 数字 1-9 在每一行只能出现一次。
	 * 2. 数字 1-9 在每一列只能出现一次。
	 * 3. 数字 1-9 在每一个以粗实线分隔的 3x3 宫内只能出现一次。
	 * 
	 * 注意：
	 * - 一个有效的数独（部分已被填充）不一定是可解的。
	 * - 只需要根据以上规则，验证已经填入的数字是否有效即可。
	 * - 空白格用 '.' 表示。
	 * 
	 * 示例：
	 * 输入：board = 
	 * [
	 *   ["5","3",".",".","7",".",".",".","."],
	 *   ["6",".",".","1","9","5",".",".","."],
	 *   [".","9","8",".",".",".",".","6","."],
	 *   ["8",".",".",".","6",".",".",".","3"],
	 *   ["4",".",".","8",".","3",".",".","1"],
	 *   ["7",".",".",".","2",".",".",".","6"],
	 *   [".","6",".",".",".",".","2","8","."],
	 *   [".",".",".","4","1","9",".",".","5"],
	 *   [".",".",".",".","8",".",".","7","9"]
	 * ]
	 * 输出：true
	 * 
	 * 约束条件：
	 * board.length == 9
	 * board[i].length == 9
	 * board[i][j] 是一位数字或者 '.'
	 * 
	 * 解题思路：
	 * 使用三个HashSet数组分别记录每一行、每一列和每一个3x3宫格中出现过的数字。
	 * 遍历数独，对于每个非空白字符，检查是否已经在对应的行、列或宫格中出现过。
	 * 宫格索引可以通过公式：boxIndex = (row / 3) * 3 + (col / 3) 计算得到。
	 * 
	 * 时间复杂度：O(1)，因为数独大小固定为9x9
	 * 空间复杂度：O(1)，固定大小的HashSet数组
	 */
	public static boolean isValidSudoku(char[][] board) {
		HashSet<Character>[] rows = new HashSet[9];
		HashSet<Character>[] cols = new HashSet[9];
		HashSet<Character>[] boxes = new HashSet[9];
		
		// 初始化所有HashSet
		for (int i = 0; i < 9; i++) {
			rows[i] = new HashSet<>();
			cols[i] = new HashSet<>();
			boxes[i] = new HashSet<>();
		}
		
		// 遍历数独
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				char c = board[row][col];
				// 跳过空白格
				if (c == '.') {
					continue;
				}
				
				// 计算宫格索引
				int boxIndex = (row / 3) * 3 + (col / 3);
				
				// 检查是否重复
				if (rows[row].contains(c) || cols[col].contains(c) || boxes[boxIndex].contains(c)) {
					return false;
				}
				
				// 添加到对应的HashSet中
				rows[row].add(c);
				cols[col].add(c);
				boxes[boxIndex].add(c);
			}
		}
		
		return true;
	}

	/**
	 * LeetCode 141. Linked List Cycle (环形链表)
	 * 
	 * 题目描述：
	 * 给定一个链表，判断链表中是否有环。
	 * 如果链表中存在环，则返回 true 。 否则，返回 false 。
	 * 
	 * 进阶：
	 * 你能否不使用额外空间解决此题？
	 * 
	 * 解题思路1（使用HashSet）：
	 * 遍历链表，将每个节点存入HashSet中，如果遇到已存在的节点，则说明有环。
	 * 
	 * 时间复杂度：O(n)，其中n是链表长度
	 * 空间复杂度：O(n)，需要存储所有节点
	 * 
	 * 注意：为了简化实现，这里定义一个简单的ListNode类
	 */
	public static class ListNode {
		int val;
		ListNode next;
		ListNode(int x) {
			val = x;
			next = null;
		}
	}

	public static boolean hasCycle(ListNode head) {
		HashSet<ListNode> seen = new HashSet<>();
		ListNode current = head;
		
		while (current != null) {
			if (seen.contains(current)) {
				return true; // 发现环
			}
			seen.add(current);
			current = current.next;
		}
		
		return false; // 没有环
	}

	/**
	 * LeetCode 160. Intersection of Two Linked Lists (相交链表)
	 * 
	 * 题目描述：
	 * 给你两个单链表的头节点 headA 和 headB ，请你找出并返回两个单链表相交的起始节点。如果两个链表没有交点，返回 null 。
	 * 
	 * 解题思路1（使用HashSet）：
	 * 遍历链表A，将每个节点存入HashSet中，然后遍历链表B，检查节点是否存在于HashSet中。
	 * 
	 * 时间复杂度：O(m+n)，其中m和n分别是两个链表的长度
	 * 空间复杂度：O(m)，需要存储链表A的所有节点
	 */
	public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
		HashSet<ListNode> seen = new HashSet<>();
		ListNode current = headA;
		
		// 将链表A的所有节点存入HashSet
		while (current != null) {
			seen.add(current);
			current = current.next;
		}
		
		// 遍历链表B，查找交集
		current = headB;
		while (current != null) {
			if (seen.contains(current)) {
				return current; // 找到交点
			}
			current = current.next;
		}
		
		return null; // 没有交点
	}

	public static void main(String[] args) {
		// Integer、Long、Double、Float
		// Byte、Short、Character、Boolean
		// String等都有这个特征
		String str1 = new String("Hello");
		String str2 = new String("Hello");
		// false，因为不同的内存地址
		System.out.println(str1 == str2);
		// true，因为它们的值是相同的
		System.out.println(str1.equals(str2));

		HashSet<String> set = new HashSet<>();
		set.add(str1);
		System.out.println(set.contains("Hello"));
		System.out.println(set.contains(str2));
		set.add(str2);
		System.out.println(set.size());
		set.remove(str1);
		set.clear();
		System.out.println(set.isEmpty());

		System.out.println("===========");

		HashMap<String, String> map1 = new HashMap<>();
		map1.put(str1, "World");
		System.out.println(map1.containsKey("Hello"));
		System.out.println(map1.containsKey(str2));
		System.out.println(map1.get(str2));
		System.out.println(map1.get("你好") == null);
		map1.remove("Hello");
		System.out.println(map1.size());
		map1.clear();
		System.out.println(map1.isEmpty());

		System.out.println("===========");

		// 一般在笔试中，未必需要申请哈希表
		HashMap<Integer, Integer> map2 = new HashMap<>();
		map2.put(56, 7285);
		map2.put(34, 3671263);
		map2.put(17, 716311);
		map2.put(24, 1263161);
		// 上面的map2行为，可以被如下数组的行为替代
		int[] arr = new int[100];
		arr[56] = 7285;
		arr[34] = 3671263;
		arr[17] = 716311;
		arr[24] = 1263161;
		// 哈希表的增、删、改、查，都可以被数组替代，前提是key的范围是固定的、可控的
		System.out.println("在笔试场合中哈希表往往会被数组替代");

		System.out.println("===========");

		Student s1 = new Student(17, "张三");
		Student s2 = new Student(17, "张三");
		HashMap<Student, String> map3 = new HashMap<>();
		map3.put(s1, "这是张三");
		System.out.println(map3.containsKey(s1));
		System.out.println(map3.containsKey(s2));
		map3.put(s2, "这是另一个张三");
		System.out.println(map3.size());
		System.out.println(map3.get(s1));
		System.out.println(map3.get(s2));

		// 测试添加的题目
		System.out.println("===========");
		System.out.println("测试两数之和:");
		int[] nums = {2, 7, 11, 15};
		int target = 9;
		int[] result = twoSum(nums, target);
		System.out.println("[" + result[0] + ", " + result[1] + "]");

		System.out.println("测试有效的字母异位词:");
		System.out.println(isAnagram("anagram", "nagaram")); // true
		System.out.println(isAnagram("rat", "car")); // false

		System.out.println("测试两个数组的交集:");
		int[] nums1 = {1, 2, 2, 1};
		int[] nums2 = {2, 2};
		int[] intersectResult = intersection(nums1, nums2);
		System.out.print("交集: [");
		for (int i = 0; i < intersectResult.length; i++) {
			System.out.print(intersectResult[i]);
			if (i < intersectResult.length - 1) {
				System.out.print(", ");
			}
		}
		System.out.println("]");

		// 测试设计哈希集合
		System.out.println("测试设计哈希集合:");
		MyHashSet myHashSet = new MyHashSet();
		myHashSet.add(1);
		myHashSet.add(2);
		System.out.println(myHashSet.contains(1)); // true
		System.out.println(myHashSet.contains(3)); // false
		myHashSet.add(2);
		System.out.println(myHashSet.contains(2)); // true
		myHashSet.remove(2);
		System.out.println(myHashSet.contains(2)); // false

		// 测试设计哈希映射
		System.out.println("测试设计哈希映射:");
		MyHashMap myHashMap = new MyHashMap();
		myHashMap.put(1, 1);
		myHashMap.put(2, 2);
		System.out.println(myHashMap.get(1)); // 1
		System.out.println(myHashMap.get(3)); // -1
		myHashMap.put(2, 1);
		System.out.println(myHashMap.get(2)); // 1
		myHashMap.remove(2);
		System.out.println(myHashMap.get(2)); // -1

		// 测试HackerRank Java Hashset
		System.out.println("测试HackerRank Java Hashset:");
		String[][] pairs = {{"john", "tom"}, {"john", "mary"}, {"john", "tom"}, {"mary", "anna"}, {"mary", "anna"}};
		System.out.println(countUniquePairs(pairs)); // 3

		// 测试最长连续序列
		System.out.println("测试最长连续序列:");
		int[] nums3 = {100, 4, 200, 1, 3, 2};
		System.out.println(longestConsecutive(nums3)); // 4

		// 测试字母异位词分组
		System.out.println("测试字母异位词分组:");
		String[] strs = {"eat", "tea", "tan", "ate", "nat", "bat"};
		List<List<String>> groups = groupAnagrams(strs);
		System.out.println(groups);

		// 测试前K个高频元素
		System.out.println("测试前K个高频元素:");
		int[] nums4 = {1, 1, 1, 2, 2, 3};
		int k = 2;
		int[] topK = topKFrequent(nums4, k);
		System.out.println(Arrays.toString(topK)); // [1, 2]
	}

	public static class Student {
		public int age;
		public String name;

		public Student(int a, String b) {
			age = a;
			name = b;
		}
	}
}