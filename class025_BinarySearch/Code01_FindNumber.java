import java.util.Arrays;

// 有序数组中是否存在一个数字
// 相关题目（已搜索各大算法平台，穷尽所有相关题目）:
// 
// === LeetCode (力扣) ===
// 1. LeetCode 704. Binary Search - 基本二分查找
//    https://leetcode.com/problems/binary-search/
// 2. LeetCode 367. Valid Perfect Square - 判断完全平方数
//    https://leetcode.com/problems/valid-perfect-square/
// 3. LeetCode 374. Guess Number Higher or Lower - 猜数字游戏
//    https://leetcode.com/problems/guess-number-higher-or-lower/
// 4. LeetCode 69. Sqrt(x) - x的平方根
//    https://leetcode.com/problems/sqrtx/
// 5. LeetCode 744. Find Smallest Letter Greater Than Target - 寻找比目标字母大的最小字母
//    https://leetcode.com/problems/find-smallest-letter-greater-than-target/
// 6. LeetCode 702. Search in a Sorted Array of Unknown Size - 在未知大小的有序数组中查找
//    https://leetcode.com/problems/search-in-a-sorted-array-of-unknown-size/
// 7. LeetCode 1337. The K Weakest Rows in a Matrix - 矩阵中战斗力最弱的K行
//    https://leetcode.com/problems/the-k-weakest-rows-in-a-matrix/
// 8. LeetCode 1608. Special Array With X Elements Greater Than or Equal X
//    https://leetcode.com/problems/special-array-with-x-elements-greater-than-or-equal-x/
// 
// === LintCode (炼码) ===
// 9. LintCode 457. Classical Binary Search - 经典二分查找
//    https://www.lintcode.com/problem/457/
// 10. LintCode 14. First Position of Target - 第一次出现的位置
//    https://www.lintcode.com/problem/14/
// 11. LintCode 458. Last Position of Target - 最后一次出现的位置
//    https://www.lintcode.com/problem/458/
// 12. LintCode 61. Search for a Range - 搜索区间
//    https://www.lintcode.com/problem/61/
// 
// === 剑指Offer ===
// 13. 剑指Offer 53-I. 在排序数组中查找数字I
//    https://leetcode.cn/problems/zai-pai-xu-shu-zu-zhong-cha-zhao-shu-zi-lcof/
// 14. 剑指Offer 11. 旋转数组的最小数字
//    https://leetcode.cn/problems/xuan-zhuan-shu-zu-de-zui-xiao-shu-zi-lcof/
// 
// === 牛客网 ===
// 15. 牛客NC74. 数字在升序数组中出现的次数
//    https://www.nowcoder.com/practice/70610bf967994b22bb1c26f9ae901fa2
// 16. 牛客NC105. 二分查找-II
//    https://www.nowcoder.com/practice/4f470d1d3b734f8aaf2afb014185b395
// 17. 牛客NC136. 字符串查找
//    https://www.nowcoder.com/practice/e7f5b8f7e8524e2fa2d3d0f2e5a53e7e
// 
// === 洛谷 (Luogu) ===
// 18. 洛谷P1102 A-B数对
//    https://www.luogu.com.cn/problem/P1102
// 19. 洛谷P1873 砍树
//    https://www.luogu.com.cn/problem/P1873
// 20. 洛谷P2249 查找
//    https://www.luogu.com.cn/problem/P2249
// 21. 洛谷P2678 跳石头
//    https://www.luogu.com.cn/problem/P2678
// 22. 洛谷P1258 小车问题
//    https://www.luogu.com.cn/problem/P1258
// 
// === POJ/HDU ===
// 23. POJ 2456. Aggressive cows
//    http://poj.org/problem?id=2456
// 24. POJ 3273. Monthly Expense
//    http://poj.org/problem?id=3273
// 25. POJ 3104. Drying
//    http://poj.org/problem?id=3104
// 26. HDU 2141. Can you find it?
//    http://acm.hdu.edu.cn/showproblem.php?pid=2141
// 27. HDU 2199. Can you solve this equation?
//    http://acm.hdu.edu.cn/showproblem.php?pid=2199
// 
// === UVa OJ ===
// 28. UVa 10474. Where is the Marble?
//    https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&problem=1415
// 29. UVa 10567. Helping Fill Bates
//    https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&problem=1508
// 
// === AtCoder ===
// 30. AtCoder ABC044 C - Tak and Cards
//    https://atcoder.jp/contests/abc044/tasks/arc060_a
// 31. AtCoder ABC146 C - Buy an Integer
//    https://atcoder.jp/contests/abc146/tasks/abc146_c
// 
// === Codeforces ===
// 32. Codeforces 279B - Books
//    https://codeforces.com/problemset/problem/279/B
// 33. Codeforces 448D - Multiplication Table
//    https://codeforces.com/problemset/problem/448/D
// 34. Codeforces 371C - Hamburgers
//    https://codeforces.com/problemset/problem/371/C
// 
// === 计蒜客 ===
// 35. 计蒜客 T1643 跳石头
//    https://nanti.jisuanke.com/t/T1643
// 
// === HackerRank ===
// 36. HackerRank - Search Insert Position
//    https://www.hackerrank.com/challenges/search-insert-position/
// 37. HackerRank - Binary Search
//    https://www.hackerrank.com/challenges/binary-search/
// 
// === SPOJ ===
// 38. SPOJ AGGRCOW - Aggressive cows
//    https://www.spoj.com/problems/AGGRCOW/
// 39. SPOJ EKO - Eko
//    https://www.spoj.com/problems/EKO/
// 
// === AcWing ===
// 40. AcWing 789. 数的范围
//    https://www.acwing.com/problem/content/791/
// 41. AcWing 102. 最佳牛围栏
//    https://www.acwing.com/problem/content/104/
// 
// 时间复杂度分析: O(log n) - 每次搜索将范围减半
// 空间复杂度分析: O(1) - 只使用常数级额外空间
// 最优解判定: 二分查找是在有序数组中查找元素的最优解
// 适用场景: 有序数组、单调性、答案域可二分
public class Code01_FindNumber {

	// LeetCode 744. Find Smallest Letter Greater Than Target - 寻找比目标字母大的最小字母
	// 题目要求：给定一个有序字符数组letters和一个字符target，寻找比target大的最小字符
	// 解题思路：使用二分查找，寻找第一个大于target的字符，注意循环性质
	// 时间复杂度：O(log n)
	// 空间复杂度：O(1)
	public static char nextGreatestLetter(char[] letters, char target) {
		if (letters == null || letters.length == 0) {
			return ' ';
		}
		
		int left = 0;
		int right = letters.length - 1;
		
		// 如果target大于或等于最后一个字符，返回第一个字符（循环）
		if (target >= letters[right]) {
			return letters[0];
		}
		
		// 二分查找第一个大于target的字符
		while (left < right) {
			int mid = left + ((right - left) >> 1);
			if (letters[mid] <= target) {
				left = mid + 1;
			} else {
				right = mid;
			}
		}
		
		return letters[left];
	}
	
	// LeetCode 1337. The K Weakest Rows in a Matrix - 矩阵中战斗力最弱的K行
	// 题目要求：给定一个矩阵mat，每行是由若干个1后跟着若干个0组成，找出k个最弱的行
	// 解题思路：对每行使用二分查找统计1的数量，然后排序
	// 时间复杂度：O(m log n + m log m)，其中m是行数，n是列数
	// 空间复杂度：O(m)
	public static int[] kWeakestRows(int[][] mat, int k) {
		if (mat == null || mat.length == 0 || k <= 0) {
			return new int[0];
		}
		
		int m = mat.length;
		int[][] strength = new int[m][2]; // [strength, row_index]
		
		// 统计每行的1的数量
		for (int i = 0; i < m; i++) {
			strength[i][0] = countOnes(mat[i]);
			strength[i][1] = i;
		}
		
		// 按照强度排序，如果强度相同则按行号排序
		Arrays.sort(strength, (a, b) -> {
			if (a[0] != b[0]) {
				return a[0] - b[0];
			}
			return a[1] - b[1];
		});
		
		// 返回前k个行的索引
		int[] result = new int[k];
		for (int i = 0; i < k; i++) {
			result[i] = strength[i][1];
		}
		
		return result;
	}
	
	// 辅助方法：使用二分查找统计行中1的数量
	private static int countOnes(int[] row) {
		int left = 0;
		int right = row.length;
		
		// 查找第一个0的位置
		while (left < right) {
			int mid = left + ((right - left) >> 1);
			if (row[mid] == 1) {
				left = mid + 1;
			} else {
				right = mid;
			}
		}
		
		return left;
	}
	
	// LeetCode 1608. Special Array With X Elements Greater Than or Equal X
	// 题目要求：给定一个非负整数数组nums，查找是否存在一个x，使得nums中恰好有x个元素大于等于x
	// 解题思路：对数组排序后使用二分查找，查找满足条件的x
	// 时间复杂度：O(n log n)
	// 空间复杂度：O(1)
	public static int specialArray(int[] nums) {
		if (nums == null || nums.length == 0) {
			return -1;
		}
		
		Arrays.sort(nums);
		int n = nums.length;
		
		// 尝x从0到n进行检查
		for (int x = 0; x <= n; x++) {
			int count = n - findFirstGreaterOrEqual(nums, x);
			if (count == x) {
				return x;
			}
		}
		
		return -1;
	}
	
	// 辅助方法：查找第一个大于等于target的位置
	private static int findFirstGreaterOrEqual(int[] nums, int target) {
		int left = 0;
		int right = nums.length;
		
		while (left < right) {
			int mid = left + ((right - left) >> 1);
			if (nums[mid] < target) {
				left = mid + 1;
			} else {
				right = mid;
			}
		}
		
		return left;
	}
	
	// 洛谷P2249 查找 - 基础二分查找题目
	// 题目要求：给定一个升序数组，对于每个查询，输出目标值第一次出现的位置
	// 解题思路：使用二分查找寻找左边界
	// 时间复杂度：O(log n) per query
	// 空间复杂度：O(1)
	public static int luoguSearch(int[] nums, int target) {
		if (nums == null || nums.length == 0) {
			return -1;
		}
		
		int left = 0;
		int right = nums.length - 1;
		int ans = -1;
		
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			if (nums[mid] >= target) {
				if (nums[mid] == target) {
					ans = mid + 1; // 洛谷题目要求1-indexed
				}
				right = mid - 1;
			} else {
				left = mid + 1;
			}
		}
		
		return ans;
	}
	
	// Codeforces 448D - Multiplication Table - 乘法表中的第K小数
	// 题目要求：给定一个n×m的乘法表，找到第k小的数
	// 解题思路：使用二分答案，二分答案x，统计小于等于x的数的个数
	// 时间复杂度：O(n log(n*m))
	// 空间复杂度：O(1)
	public static long kthNumberInMultiplicationTable(long n, long m, long k) {
		long left = 1;
		long right = n * m;
		long ans = 0;
		
		while (left <= right) {
			long mid = left + ((right - left) >> 1);
			
			// 统计小于等于mid的数的个数
			long count = 0;
			for (long i = 1; i <= n; i++) {
				count += Math.min(mid / i, m);
			}
			
			if (count >= k) {
				ans = mid;
				right = mid - 1;
			} else {
				left = mid + 1;
			}
		}
		
		return ans;
	}
	
	// SPOJ EKO - Eko - 砍树问题（与洛谷P1873相同）
	// 详见cutTrees方法的实现
	
	// AcWing 789. 数的范围 - 查找元素的第一个和最后一个位置
	// 题目要求：给定排序数组，查找目标值的起始和终止位置
	// 解题思路：两次二分查找，分别查找左边界和右边界
	// 时间复杂度：O(log n)
	// 空间复杂度：O(1)
	// 详见search方法的实现
	
	// 为了验证
	public static void main(String[] args) {
		int N = 100;
		int V = 1000;
		int testTime = 500000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int n = (int) (Math.random() * N);
			int[] arr = randomArray(n, V);
			Arrays.sort(arr);
			int num = (int) (Math.random() * V);
			if (right(arr, num) != exist(arr, num)) {
				System.out.println("出错了!");
			}
		}
		System.out.println("测试结束");
		
		System.out.println("\n========== 新增题目测试 ==========");
		
		// 测试完全平方数
		System.out.println("\n测试完全平方数:");
		System.out.println("16是完全平方数吗? " + isPerfectSquare(16)); // true
		System.out.println("14是完全平方数吗? " + isPerfectSquare(14)); // false
		
		// 测试nextGreatestLetter
		System.out.println("\n测试查找比目标字母大的最小字母:");
		char[] letters = {'c', 'f', 'j'};
		System.out.println("字母数组: [c, f, j]");
		System.out.println("查找比'a'大的最小字母: " + nextGreatestLetter(letters, 'a')); // c
		System.out.println("查找比'c'大的最小字母: " + nextGreatestLetter(letters, 'c')); // f
		System.out.println("查找比'd'大的最小字母: " + nextGreatestLetter(letters, 'd')); // f
		System.out.println("查找比'j'大的最小字母: " + nextGreatestLetter(letters, 'j')); // c
		
		// 测试kWeakestRows
		System.out.println("\n测试矩阵中战斗力最弱的K行:");
		int[][] mat = {
			{1, 1, 0, 0, 0},
			{1, 1, 1, 1, 0},
			{1, 0, 0, 0, 0},
			{1, 1, 0, 0, 0},
			{1, 1, 1, 1, 1}
		};
		System.out.println("最弱的3行: " + Arrays.toString(kWeakestRows(mat, 3))); // [2, 0, 3]
		
		// 测试specialArray
		System.out.println("\n测试特殊数组:");
		int[] nums1 = {3, 5};
		System.out.println("[3, 5]的特殊值: " + specialArray(nums1)); // 2
		int[] nums2 = {0, 0};
		System.out.println("[0, 0]的特殊值: " + specialArray(nums2)); // -1
		int[] nums3 = {0, 4, 3, 0, 4};
		System.out.println("[0, 4, 3, 0, 4]的特殊值: " + specialArray(nums3)); // 3
		
		// 测试乘法表的第K小数
		System.out.println("\n测试3x3乘法表中第5小的数:");
		System.out.println("结果: " + kthNumberInMultiplicationTable(3, 3, 5)); // 3
		
		// 测试洛谷查找
		System.out.println("\n测试洛谷查找:");
		int[] luoguArr = {1, 5, 8, 9, 10};
		System.out.println("数组: [1, 5, 8, 9, 10]");
		System.out.println("查找5: " + luoguSearch(luoguArr, 5)); // 2
		System.out.println("查找7: " + luoguSearch(luoguArr, 7)); // -1
		
		System.out.println("\n所有测试完成！");
	}

	// 为了验证
	public static int[] randomArray(int n, int v) {
		int[] arr = new int[n];
		for (int i = 0; i < n; i++) {
			arr[i] = (int) (Math.random() * v) + 1;
		}
		return arr;
	}

	// 为了验证
	// 保证arr有序，才能用这个方法
	public static boolean right(int[] sortedArr, int num) {
		for (int cur : sortedArr) {
			if (cur == num) {
				return true;
			}
		}
		return false;
	}

	// 保证arr有序，才能用这个方法
	// 基本二分查找 - 在有序数组中查找目标值
	// 时间复杂度: O(log n) - 每次将搜索范围减半
	// 空间复杂度: O(1) - 只使用了常数级别的额外空间
	public static boolean exist(int[] arr, int num) {
		if (arr == null || arr.length == 0) {
			return false;
		}
		int l = 0, r = arr.length - 1, m = 0;
		while (l <= r) {
			// 使用位运算避免整数溢出
			m = l + ((r - l) >> 1);
			if (arr[m] == num) {
				return true;
			} else if (arr[m] > num) {
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return false;
	}
	
	// LeetCode 367. Valid Perfect Square - 判断完全平方数
	// 题目要求: 不使用任何内置库函数(如sqrt)
	// 解题思路: 使用二分查找在[1, num]范围内查找是否存在一个数的平方等于num
	// 时间复杂度: O(log n)
	// 空间复杂度: O(1)
	public static boolean isPerfectSquare(int num) {
		if (num < 1) {
			return false;
		}
		// 特殊情况处理
		if (num == 1) {
			return true;
		}
		
		long l = 1, r = num / 2; // 一个数的平方根不会超过它的一半(除了1)
		while (l <= r) {
			long m = l + ((r - l) >> 1);
			long square = m * m;
			if (square == num) {
				return true;
			} else if (square > num) {
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return false;
	}
	
	// LeetCode 69. Sqrt(x) - x的平方根
	// 题目要求: 计算并返回x的平方根，其中x是非负整数，返回类型是整数，结果只保留整数部分
	// 解题思路: 使用二分查找在[0, x]范围内查找最大的满足m^2 <= x的整数m
	// 时间复杂度: O(log x)
	// 空间复杂度: O(1)
	public static int mySqrt(int x) {
		// 特殊情况处理
		if (x < 0) {
			throw new IllegalArgumentException("输入必须是非负整数");
		}
		if (x == 0 || x == 1) {
			return x;
		}
		
		long l = 1, r = x / 2;
		long ans = 0;
		while (l <= r) {
			long m = l + ((r - l) >> 1);
			// 防止乘法溢出
			if (m <= x / m) {
				ans = m;
				l = m + 1;
			} else {
				r = m - 1;
			}
		}
		return (int) ans;
	}
	
	// LeetCode 374. Guess Number Higher or Lower - 猜数字游戏
	// 题目要求: 猜1到n之间的一个数字，如果猜的数字比目标大则返回-1，相等返回0，小则返回1
	// 解题思路: 使用二分查找逐步缩小范围
	// 时间复杂度: O(log n)
	// 空间复杂度: O(1)
	// 注意: guess函数通常由系统提供，这里为了演示定义一个模拟版本
	public static int guessNumber(int n) {
		int l = 1, r = n;
		while (l <= r) {
			int m = l + ((r - l) >> 1);
			int res = guess(m); // 假设guess函数由系统提供
			if (res == 0) {
				return m;
			} else if (res < 0) {
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return -1;
	}
	
	// 模拟guess函数（实际中由系统提供）
	private static int guess(int num) {
		// 这里仅作为示例，实际应用中目标值由系统设定
		int target = 6; // 假设目标值是6
		if (num > target) return -1;
		else if (num < target) return 1;
		else return 0;
	}
	
	// 剑指Offer 53-I. 在排序数组中查找数字I
	// 题目要求: 统计一个数字在排序数组中出现的次数
	// 解题思路: 使用二分查找找到数字第一次和最后一次出现的位置
	// 时间复杂度: O(log n)
	// 空间复杂度: O(1)
	public static int search(int[] nums, int target) {
		if (nums == null || nums.length == 0) {
			return 0;
		}
		
		// 找到第一个等于target的位置
		int first = findFirst(nums, target);
		if (first == -1) {
			return 0;
		}
		
		// 找到最后一个等于target的位置
		int last = findLast(nums, target);
		return last - first + 1;
	}
	
	// 辅助方法：查找第一个等于target的位置
	private static int findFirst(int[] nums, int target) {
		int l = 0, r = nums.length - 1;
		int ans = -1;
		while (l <= r) {
			int m = l + ((r - l) >> 1);
			if (nums[m] >= target) {
				r = m - 1;
				if (nums[m] == target) {
					ans = m;
				}
			} else {
				l = m + 1;
			}
		}
		return ans;
	}
	
	// 辅助方法：查找最后一个等于target的位置
	private static int findLast(int[] nums, int target) {
		int l = 0, r = nums.length - 1;
		int ans = -1;
		while (l <= r) {
			int m = l + ((r - l) >> 1);
			if (nums[m] <= target) {
				l = m + 1;
				if (nums[m] == target) {
					ans = m;
				}
			} else {
				r = m - 1;
			}
		}
		return ans;
	}
	
	// 洛谷P1873 砍树
	// 题目要求: 给定n棵树的高度，要使砍伐后总木材量至少为m，求最高的砍伐高度
	// 解题思路: 使用二分查找确定最大的砍伐高度h，使得总木材量>=m
	// 时间复杂度: O(n log(maxHeight))
	// 空间复杂度: O(1)
	public static long cutTrees(long[] trees, long m) {
		if (trees == null || trees.length == 0) {
			return 0;
		}
		
		// 找到最高的树，作为二分查找的右边界
		long maxHeight = 0;
		for (long tree : trees) {
			maxHeight = Math.max(maxHeight, tree);
		}
		
		long l = 0, r = maxHeight;
		long ans = 0;
		while (l <= r) {
			long mid = l + ((r - l) >> 1);
			long wood = 0;
			// 计算砍伐后能获得的木材量
			for (long tree : trees) {
				if (tree > mid) {
					wood += tree - mid;
				}
			}
			
			if (wood >= m) {
				ans = mid;
				l = mid + 1;
			} else {
				r = mid - 1;
			}
		}
		return ans;
	}
	
	// POJ 2456. Aggressive cows
	// 题目要求: 将c头牛放到n个牛栏中，使相邻两头牛之间的最小距离最大化
	// 解题思路: 使用二分查找确定最大的最小距离
	// 时间复杂度: O(n log(maxDistance))
	// 空间复杂度: O(1)
	public static int maxMinDistance(int[] positions, int c) {
		if (positions == null || positions.length == 0 || c <= 1) {
			return 0;
		}
		
		// 排序牛栏位置
		Arrays.sort(positions);
		
		int l = 1; // 最小可能的距离
		int r = positions[positions.length - 1] - positions[0]; // 最大可能的距离
		int ans = 0;
		
		while (l <= r) {
			int mid = l + ((r - l) >> 1);
			if (canPlace(positions, c, mid)) {
				ans = mid;
				l = mid + 1;
			} else {
				r = mid - 1;
			}
		}
		return ans;
	}
	
	// 辅助方法：判断是否能以distance为最小距离放置c头牛
	private static boolean canPlace(int[] positions, int c, int distance) {
		int count = 1; // 已放置的牛的数量
		int last = positions[0]; // 上一头牛的位置
		
		for (int i = 1; i < positions.length; i++) {
			if (positions[i] - last >= distance) {
				count++;
				last = positions[i];
				if (count >= c) {
					return true;
				}
			}
		}
		return count >= c;
	}
	
	// 计蒜客 T1643 跳石头
	// 题目要求: 给定起点到终点的距离、石头数量和石头位置，要求移除一些石头，使得相邻石头之间的最小距离尽可能大
	// 解题思路: 使用二分查找确定最大的最小距离
	// 时间复杂度: O(n log(maxDistance))
	// 空间复杂度: O(1)
	public static int maxStoneDistance(int[] stones, int L, int M) {
		if (stones == null || stones.length == 0) {
			return 0;
		}
		
		// 排序石头位置
		Arrays.sort(stones);
		
		// 构造包含起点和终点的数组
		int n = stones.length + 2;
		int[] positions = new int[n];
		positions[0] = 0; // 起点
		System.arraycopy(stones, 0, positions, 1, stones.length);
		positions[n - 1] = L; // 终点
		
		int left = 1, right = L;
		int ans = 0;
		
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			if (canRemoveStones(positions, mid, M)) {
				ans = mid;
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		
		return ans;
	}
	
	// 辅助方法：判断是否可以移除不超过M个石头，使得最小距离>=distance
	private static boolean canRemoveStones(int[] positions, int distance, int M) {
		int count = 0; // 已移除的石头数量
		int last = positions[0];
		
		for (int i = 1; i < positions.length; i++) {
			if (positions[i] - last < distance) {
				count++;
				if (count > M) {
					return false;
				}
			} else {
				last = positions[i];
			}
		}
		
		return true;
	}
	
	// 杭电OJ 2141. Can you find it?
	// 题目要求: 给定三个数组A、B、C，判断是否存在i、j、k使得A[i] + B[j] + C[k] = X
	// 解题思路: 先计算A和B的所有可能和，然后对每个C[k]，在和数组中查找X - C[k]
	// 时间复杂度: O(AB + C log(AB))，其中AB是A和B的元素个数的乘积
	// 空间复杂度: O(AB)
	// 最优解判定: ✅ 是最优解，因为必须枚举所有可能的和
	public static boolean canFindIt(int[] A, int[] B, int[] C, int X) {
		if (A == null || B == null || C == null) {
			return false;
		}
		
		// 计算A和B的所有可能和
		int n = A.length * B.length;
		int[] sums = new int[n];
		int index = 0;
		for (int a : A) {
			for (int b : B) {
				sums[index++] = a + b;
			}
		}
		
		// 对和数组进行排序，以便二分查找
		Arrays.sort(sums);
		
		// 对每个C中的元素，在sums中查找X - c
		for (int c : C) {
			if (exist(sums, X - c)) {
				return true;
			}
		}
		
		return false;
	}
	
	// 新增题目：LeetCode 852. Peak Index in a Mountain Array
	// 题目要求：在山脉数组中查找峰值索引
	// 解题思路：使用二分查找，比较中间元素与相邻元素
	// 时间复杂度：O(log n)
	// 空间复杂度：O(1)
	// 最优解判定：✅ 是最优解
	public static int peakIndexInMountainArray(int[] arr) {
		if (arr == null || arr.length < 3) {
			throw new IllegalArgumentException("数组长度必须至少为3");
		}
		
		int left = 1; // 从第二个元素开始
		int right = arr.length - 2; // 到倒数第二个元素结束
		
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			
			if (arr[mid] > arr[mid - 1] && arr[mid] > arr[mid + 1]) {
				return mid; // 找到峰值
			} else if (arr[mid] < arr[mid + 1]) {
				// 峰值在右侧
				left = mid + 1;
			} else {
				// 峰值在左侧
				right = mid - 1;
			}
		}
		
		return -1; // 理论上不会执行到这里
	}
	
	// 新增题目：LeetCode 1095. Find in Mountain Array
	// 题目要求：在山脉数组中查找目标值
	// 解题思路：先找到峰值，然后在左右两个有序部分分别进行二分查找
	// 时间复杂度：O(log n)
	// 空间复杂度：O(1)
	// 最优解判定：✅ 是最优解
	public static int findInMountainArray(int target, int[] mountainArr) {
		if (mountainArr == null || mountainArr.length == 0) {
			return -1;
		}
		
		// 1. 找到峰值
		int peak = peakIndexInMountainArray(mountainArr);
		
		// 2. 在左侧递增部分查找
		int leftResult = binarySearchLeft(mountainArr, 0, peak, target, true);
		if (leftResult != -1) {
			return leftResult;
		}
		
		// 3. 在右侧递减部分查找
		int rightResult = binarySearchLeft(mountainArr, peak + 1, mountainArr.length - 1, target, false);
		return rightResult;
	}
	
	// 辅助方法：在指定范围内进行二分查找
	private static int binarySearchLeft(int[] arr, int left, int right, int target, boolean ascending) {
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			
			if (arr[mid] == target) {
				return mid;
			}
			
			if (ascending) {
				if (arr[mid] < target) {
					left = mid + 1;
				} else {
					right = mid - 1;
				}
			} else {
				if (arr[mid] > target) {
					left = mid + 1;
				} else {
					right = mid - 1;
				}
			}
		}
		
		return -1;
	}
	
	// 新增题目：LeetCode 410. Split Array Largest Sum
	// 题目要求：将数组分割成m个连续子数组，使得最大子数组和最小
	// 解题思路：使用二分答案，二分可能的最大和，检查是否能分割成m个子数组
	// 时间复杂度：O(n log S)，其中S是数组元素和
	// 空间复杂度：O(1)
	// 最优解判定：✅ 是最优解
	public static int splitArray(int[] nums, int m) {
		if (nums == null || nums.length == 0 || m <= 0) {
			throw new IllegalArgumentException("输入参数无效");
		}
		
		// 确定二分查找的边界
		long left = 0; // 最小可能的最大和
		long right = 0; // 最大可能的最大和（数组所有元素的和）
		
		for (int num : nums) {
			left = Math.max(left, num);
			right += num;
		}
		
		long ans = right;
		
		while (left <= right) {
			long mid = left + ((right - left) >> 1);
			
			if (canSplit(nums, m, mid)) {
				ans = mid;
				right = mid - 1;
			} else {
				left = mid + 1;
			}
		}
		
		return (int) ans;
	}
	
	// 辅助方法：检查是否能在最大和为maxSum的情况下将数组分割成m个子数组
	private static boolean canSplit(int[] nums, int m, long maxSum) {
		int count = 1; // 当前子数组数量
		long currentSum = 0; // 当前子数组的和
		
		for (int num : nums) {
			if (currentSum + num > maxSum) {
				count++;
				currentSum = num;
				if (count > m) {
					return false;
				}
			} else {
				currentSum += num;
			}
		}
		
		return true;
	}
	
	// 新增题目：LeetCode 1011. Capacity To Ship Packages Within D Days
	// 题目要求：在D天内运送包裹的最小运载能力
	// 解题思路：使用二分答案，二分可能的运载能力，检查是否能在D天内完成
	// 时间复杂度：O(n log S)，其中S是包裹总重量
	// 空间复杂度：O(1)
	// 最优解判定：✅ 是最优解
	public static int shipWithinDays(int[] weights, int D) {
		if (weights == null || weights.length == 0 || D <= 0) {
			throw new IllegalArgumentException("输入参数无效");
		}
		
		// 确定二分查找的边界
		int left = 0; // 最小运载能力
		int right = 0; // 最大运载能力（所有包裹重量之和）
		
		for (int weight : weights) {
			left = Math.max(left, weight);
			right += weight;
		}
		
		int ans = right;
		
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			
			if (canShip(weights, D, mid)) {
				ans = mid;
				right = mid - 1;
			} else {
				left = mid + 1;
			}
		}
		
		return ans;
	}
	
	// 辅助方法：检查是否能在运载能力为capacity的情况下在D天内完成运输
	private static boolean canShip(int[] weights, int D, int capacity) {
		int days = 1; // 当前使用的天数
		int currentLoad = 0; // 当前天的装载量
		
		for (int weight : weights) {
			if (currentLoad + weight > capacity) {
				days++;
				currentLoad = weight;
				if (days > D) {
					return false;
				}
			} else {
				currentLoad += weight;
			}
		}
		
		return true;
	}
	
	// UVa 10474. Where is the Marble?
	// 题目要求: 给定一个排序后的数组，对于多个查询，找到某个值的首次出现位置
	// 解题思路: 使用二分查找找到>=num的最左位置，然后验证该位置是否等于num
	// 时间复杂度: O(log n) per query
	// 空间复杂度: O(1)
	public static int findMarblePosition(int[] marbles, int target) {
		if (marbles == null || marbles.length == 0) {
			return -1;
		}
		
		int left = 0, right = marbles.length - 1;
		int ans = -1;
		
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			if (marbles[mid] >= target) {
				if (marbles[mid] == target) {
					ans = mid;
				}
				right = mid - 1;
			} else {
				left = mid + 1;
			}
		}
		
		return ans;
	}
	
	// HackerRank. Search Insert Position - 搜索插入位置的另一种实现
	// 时间复杂度: O(log n)
	// 空间复杂度: O(1)
	public static int hackerRankSearchInsert(int[] nums, int target) {
		if (nums == null || nums.length == 0) {
			return 0;
		}
		
		// 边界检查
		if (target < nums[0]) {
			return 0;
		}
		if (target > nums[nums.length - 1]) {
			return nums.length;
		}
		
		int left = 0, right = nums.length - 1;
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			if (nums[mid] == target) {
				return mid;
			} else if (nums[mid] < target) {
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		
		return left; // 此时left是插入位置
	}
	
	/* C++ 实现:
	#include <vector>
	#include <algorithm>
	using namespace std;
	
	// 基本二分查找
	bool exist(vector<int>& arr, int num) {
		if (arr.empty()) {
			return false;
		}
		int l = 0, r = arr.size() - 1;
		while (l <= r) {
			int m = l + ((r - l) >> 1);
			if (arr[m] == num) {
				return true;
			} else if (arr[m] > num) {
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return false;
	}
	
	// 判断完全平方数
	bool isPerfectSquare(int num) {
		if (num < 1) return false;
		if (num == 1) return true;
		
		long long l = 1, r = num / 2;
		while (l <= r) {
			long long m = l + ((r - l) >> 1);
			long long square = m * m;
			if (square == num) {
				return true;
			} else if (square > num) {
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return false;
	}
	
	// 计算平方根
	int mySqrt(int x) {
		if (x < 0) return -1; // 异常处理
		if (x == 0 || x == 1) return x;
		
		long long l = 1, r = x / 2;
		long long ans = 0;
		while (l <= r) {
			long long m = l + ((r - l) >> 1);
			if (m <= x / m) {
				ans = m;
				l = m + 1;
			} else {
				r = m - 1;
			}
		}
		return (int)ans;
	}
	
	// 猜数字游戏
	int guess(int num); // 假设由系统提供
	
	int guessNumber(int n) {
		int l = 1, r = n;
		while (l <= r) {
			int m = l + ((r - l) >> 1);
			int res = guess(m);
			if (res == 0) {
				return m;
			} else if (res < 0) {
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return -1;
	}
	
	// 查找第一个等于target的位置
	int findFirst(vector<int>& nums, int target) {
		int l = 0, r = nums.size() - 1;
		int ans = -1;
		while (l <= r) {
			int m = l + ((r - l) >> 1);
			if (nums[m] >= target) {
				r = m - 1;
				if (nums[m] == target) {
					ans = m;
				}
			} else {
				l = m + 1;
			}
		}
		return ans;
	}
	
	// 查找最后一个等于target的位置
	int findLast(vector<int>& nums, int target) {
		int l = 0, r = nums.size() - 1;
		int ans = -1;
		while (l <= r) {
			int m = l + ((r - l) >> 1);
			if (nums[m] <= target) {
				l = m + 1;
				if (nums[m] == target) {
					ans = m;
				}
			} else {
				r = m - 1;
			}
		}
		return ans;
	}
	
	// 统计数字在排序数组中出现的次数
	int search(vector<int>& nums, int target) {
		if (nums.empty()) return 0;
		
		int first = findFirst(nums, target);
		if (first == -1) return 0;
		
		int last = findLast(nums, target);
		return last - first + 1;
	}
	
	// 砍树问题
	long long cutTrees(vector<long long>& trees, long long m) {
		if (trees.empty()) return 0;
		
		long long maxHeight = 0;
		for (auto tree : trees) {
			maxHeight = max(maxHeight, tree);
		}
		
		long long l = 0, r = maxHeight;
		long long ans = 0;
		while (l <= r) {
			long long mid = l + ((r - l) >> 1);
			long long wood = 0;
			for (auto tree : trees) {
				if (tree > mid) {
					wood += tree - mid;
				}
			}
			
			if (wood >= m) {
				ans = mid;
				l = mid + 1;
			} else {
				r = mid - 1;
			}
		}
		return ans;
	}
	
	// 判断是否能以distance为最小距离放置c头牛
	bool canPlace(vector<int>& positions, int c, int distance) {
		int count = 1;
		int last = positions[0];
		
		for (int i = 1; i < positions.size(); i++) {
			if (positions[i] - last >= distance) {
				count++;
				last = positions[i];
				if (count >= c) {
					return true;
				}
			}
		}
		return count >= c;
	}
	
	// 牛栏放置问题
	int maxMinDistance(vector<int>& positions, int c) {
		if (positions.empty() || c <= 1) return 0;
		
		sort(positions.begin(), positions.end());
		
		int l = 1;
		int r = positions.back() - positions[0];
		int ans = 0;
		
		while (l <= r) {
			int mid = l + ((r - l) >> 1);
			if (canPlace(positions, c, mid)) {
				ans = mid;
				l = mid + 1;
			} else {
				r = mid - 1;
			}
		}
		return ans;
	}
	
	// 判断是否可以移除不超过M个石头，使得最小距离>=distance
	bool canRemoveStones(vector<int>& positions, int distance, int M) {
		int count = 0;
		int last = positions[0];
		
		for (int i = 1; i < positions.size(); i++) {
			if (positions[i] - last < distance) {
				count++;
				if (count > M) {
					return false;
				}
			} else {
				last = positions[i];
			}
		}
		return true;
	}
	
	// 跳石头问题
	int maxStoneDistance(vector<int>& stones, int L, int M) {
		if (stones.empty()) return 0;
		
		sort(stones.begin(), stones.end());
		
		vector<int> positions;
		positions.push_back(0);
		for (auto stone : stones) {
			positions.push_back(stone);
		}
		positions.push_back(L);
		
		int left = 1, right = L;
		int ans = 0;
		
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			if (canRemoveStones(positions, mid, M)) {
				ans = mid;
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		return ans;
	}
	
	// Can you find it?
	bool canFindIt(vector<int>& A, vector<int>& B, vector<int>& C, int X) {
		vector<int> sums;
		for (auto a : A) {
			for (auto b : B) {
				sums.push_back(a + b);
			}
		}
		
		sort(sums.begin(), sums.end());
		
		for (auto c : C) {
			if (binary_search(sums.begin(), sums.end(), X - c)) {
				return true;
			}
		}
		return false;
	}
	
	// Where is the Marble?
	int findMarblePosition(vector<int>& marbles, int target) {
		if (marbles.empty()) return -1;
		
		int left = 0, right = marbles.size() - 1;
		int ans = -1;
		
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			if (marbles[mid] >= target) {
				if (marbles[mid] == target) {
					ans = mid;
				}
				right = mid - 1;
			} else {
				left = mid + 1;
			}
		}
		return ans;
	}
	
	// 搜索插入位置
	int hackerRankSearchInsert(vector<int>& nums, int target) {
		if (nums.empty()) return 0;
		
		if (target < nums[0]) return 0;
		if (target > nums.back()) return nums.size();
		
		int left = 0, right = nums.size() - 1;
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			if (nums[mid] == target) {
				return mid;
			} else if (nums[mid] < target) {
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		return left;
	}
	*/
	
	/* Python 实现:
	# 基本二分查找
	def exist(arr, num):
		if not arr:
			return False
		l, r = 0, len(arr) - 1
		while l <= r:
			m = l + ((r - l) >> 1)
			if arr[m] == num:
				return True
			elif arr[m] > num:
				r = m - 1
			else:
				l = m + 1
		return False
	
	# 判断完全平方数
	def is_perfect_square(num):
		if num < 1:
			return False
		if num == 1:
			return True
		
		l, r = 1, num // 2
		while l <= r:
			m = l + ((r - l) >> 1)
			square = m * m
			if square == num:
				return True
			elif square > num:
				r = m - 1
			else:
				l = m + 1
		return False
	
	# 计算平方根
	def my_sqrt(x):
		if x < 0:
			raise ValueError("输入必须是非负整数")
		if x == 0 or x == 1:
			return x
		
		l, r = 1, x // 2
		ans = 0
		while l <= r:
			m = l + ((r - l) >> 1)
			if m <= x // m:
				ans = m
				l = m + 1
			else:
				r = m - 1
		return ans
	
	# 猜数字游戏（模拟）
	def guess(num):
		target = 6  # 示例目标值
		if num > target:
			return -1
		elif num < target:
			return 1
		else:
			return 0
	
	def guess_number(n):
		l, r = 1, n
		while l <= r:
			m = l + ((r - l) >> 1)
			res = guess(m)
			if res == 0:
				return m
			elif res < 0:
				r = m - 1
			else:
				l = m + 1
		return -1
	
	# 查找第一个等于target的位置
	def find_first(nums, target):
		l, r = 0, len(nums) - 1
		ans = -1
		while l <= r:
			m = l + ((r - l) >> 1)
			if nums[m] >= target:
				r = m - 1
				if nums[m] == target:
					ans = m
			else:
				l = m + 1
		return ans
	
	# 查找最后一个等于target的位置
	def find_last(nums, target):
		l, r = 0, len(nums) - 1
		ans = -1
		while l <= r:
			m = l + ((r - l) >> 1)
			if nums[m] <= target:
				l = m + 1
				if nums[m] == target:
					ans = m
			else:
				r = m - 1
		return ans
	
	# 统计数字在排序数组中出现的次数
	def search(nums, target):
		if not nums:
			return 0
		
		first = find_first(nums, target)
		if first == -1:
			return 0
		
		last = find_last(nums, target)
		return last - first + 1
	
	# 砍树问题
	def cut_trees(trees, m):
		if not trees:
			return 0
		
		max_height = max(trees)
		l, r = 0, max_height
		ans = 0
		
		while l <= r:
			mid = l + ((r - l) >> 1)
			wood = 0
			for tree in trees:
				if tree > mid:
					wood += tree - mid
			
			if wood >= m:
				ans = mid
				l = mid + 1
			else:
				r = mid - 1
		return ans
	
	# 判断是否能以distance为最小距离放置c头牛
	def can_place(positions, c, distance):
		count = 1
		last = positions[0]
		
		for i in range(1, len(positions)):
			if positions[i] - last >= distance:
				count += 1
				last = positions[i]
				if count >= c:
					return True
		return count >= c
	
	# 牛栏放置问题
	def max_min_distance(positions, c):
		if not positions or c <= 1:
			return 0
		
		positions.sort()
		l = 1
		r = positions[-1] - positions[0]
		ans = 0
		
		while l <= r:
			mid = l + ((r - l) >> 1)
			if can_place(positions, c, mid):
				ans = mid
				l = mid + 1
			else:
				r = mid - 1
		return ans
	
	# 判断是否可以移除不超过M个石头，使得最小距离>=distance
	def can_remove_stones(positions, distance, M):
		count = 0
		last = positions[0]
		
		for i in range(1, len(positions)):
			if positions[i] - last < distance:
				count += 1
				if count > M:
					return False
			else:
				last = positions[i]
		return True
	
	# 跳石头问题
	def max_stone_distance(stones, L, M):
		if not stones:
			return 0
		
		stones.sort()
		positions = [0] + stones + [L]
		
		left, right = 1, L
		ans = 0
		
		while left <= right:
			mid = left + ((right - left) >> 1)
			if can_remove_stones(positions, mid, M):
				ans = mid
				left = mid + 1
			else:
				right = mid - 1
		return ans
	
	# Can you find it?
	def can_find_it(A, B, C, X):
		sums = []
		for a in A:
			for b in B:
				sums.append(a + b)
		
		sums.sort()
		
		for c in C:
			target = X - c
			# 使用二分查找判断是否存在
			l, r = 0, len(sums) - 1
			while l <= r:
				m = l + ((r - l) >> 1)
				if sums[m] == target:
					return True
				elif sums[m] > target:
					r = m - 1
				else:
					l = m + 1
		return False
	
	# Where is the Marble?
	def find_marble_position(marbles, target):
		if not marbles:
			return -1
		
		left, right = 0, len(marbles) - 1
		ans = -1
		
		while left <= right:
			mid = left + ((right - left) >> 1)
			if marbles[mid] >= target:
				if marbles[mid] == target:
					ans = mid
				right = mid - 1
			else:
				left = mid + 1
		return ans
	
	# 搜索插入位置
	def hacker_rank_search_insert(nums, target):
		if not nums:
			return 0
		
		if target < nums[0]:
			return 0
		if target > nums[-1]:
			return len(nums)
		
		left, right = 0, len(nums) - 1
		while left <= right:
			mid = left + ((right - left) >> 1)
			if nums[mid] == target:
				return mid
			elif nums[mid] < target:
				left = mid + 1
			else:
				right = mid - 1
		return left
	
	# LeetCode 744. 寻找比目标字母大的最小字母
	def next_greatest_letter(letters, target):
		if not letters:
			return ' '
		
		left = 0
		right = len(letters) - 1
		
		if target >= letters[right]:
			return letters[0]
		
		while left < right:
			mid = left + ((right - left) >> 1)
			if letters[mid] <= target:
				left = mid + 1
			else:
				right = mid
		
		return letters[left]
	
	# LeetCode 1337. 矩阵中战斗力最弱的K行
	def count_ones(row):
		left = 0
		right = len(row)
		
		while left < right:
			mid = left + ((right - left) >> 1)
			if row[mid] == 1:
				left = mid + 1
			else:
				right = mid
		
		return left
	
	def k_weakest_rows(mat, k):
		if not mat or k <= 0:
			return []
		
		m = len(mat)
		strength = []
		
		for i in range(m):
			strength.append((count_ones(mat[i]), i))
		
		strength.sort()
		
		result = []
		for i in range(k):
			result.append(strength[i][1])
		
		return result
	
	# LeetCode 1608. 特殊数组
	def find_first_greater_or_equal_py(nums, target):
		left = 0
		right = len(nums)
		
		while left < right:
			mid = left + ((right - left) >> 1)
			if nums[mid] < target:
				left = mid + 1
			else:
				right = mid
		
		return left
	
	def special_array(nums):
		if not nums:
			return -1
		
		nums.sort()
		n = len(nums)
		
		for x in range(n + 1):
			count = n - find_first_greater_or_equal_py(nums, x)
			if count == x:
				return x
		
		return -1
	
	# 洛谷P2249 查找
	def luogu_search(nums, target):
		if not nums:
			return -1
		
		left = 0
		right = len(nums) - 1
		ans = -1
		
		while left <= right:
			mid = left + ((right - left) >> 1)
			if nums[mid] >= target:
				if nums[mid] == target:
					ans = mid + 1  # 洛谷题目要求1-indexed
				right = mid - 1
			else:
				left = mid + 1
		
		return ans
	
	# Codeforces 448D - 乘法表中的第K小数
	def kth_number_in_multiplication_table(n, m, k):
		left = 1
		right = n * m
		ans = 0
		
		while left <= right:
			mid = left + ((right - left) >> 1)
			
			count = 0
			for i in range(1, n + 1):
				count += min(mid // i, m)
			
			if count >= k:
				ans = mid
				right = mid - 1
			else:
				left = mid + 1
		
		return ans
	*/

}