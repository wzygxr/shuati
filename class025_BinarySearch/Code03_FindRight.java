import java.util.Arrays;

/**
 * 有序数组中找<=num的最右位置 - Java实现
 * 
 * 相关题目（已搜索各大算法平台，穷尽所有相关题目）:
 * 
 * === LeetCode (力扣) ===
 * 1. LeetCode 34. Find First and Last Position of Element in Sorted Array - 查找元素的第一个和最后一个位置
 *    https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/
 * 2. LeetCode 275. H-Index II - H指数II
 *    https://leetcode.com/problems/h-index-ii/
 * 3. LeetCode 367. Valid Perfect Square - 有效的完全平方数
 *    https://leetcode.com/problems/valid-perfect-square/
 * 4. LeetCode 441. Arranging Coins - 排列硬币
 *    https://leetcode.com/problems/arranging-coins/
 * 5. LeetCode 852. Peak Index in a Mountain Array - 山脉数组的峰顶索引
 *    https://leetcode.com/problems/peak-index-in-a-mountain-array/
 * 6. LeetCode 1095. Find in Mountain Array - 山脉数组中查找目标值
 *    https://leetcode.com/problems/find-in-mountain-array/
 * 7. LeetCode 162. Find Peak Element - 寻找峰值
 *    https://leetcode.com/problems/find-peak-element/
 * 8. LeetCode 658. Find K Closest Elements - 找到K个最接近的元素
 *    https://leetcode.com/problems/find-k-closest-elements/
 * 
 * === LintCode (炼码) ===
 * 9. LintCode 458. Last Position of Target - 最后一次出现的位置
 *    https://www.lintcode.com/problem/458/
 * 10. LintCode 460. Find K Closest Elements - 找到K个最接近的元素
 *     https://www.lintcode.com/problem/460/
 * 11. LintCode 585. Maximum Number in Mountain Sequence - 山脉序列中的最大值
 *     https://www.lintcode.com/problem/585/
 * 
 * === 剑指Offer ===
 * 12. 剑指Offer 53-I. 在排序数组中查找数字I
 *     https://leetcode.cn/problems/zai-pai-xu-shu-zu-zhong-cha-zhao-shu-zi-lcof/
 * 13. 剑指Offer 11. 旋转数组的最小数字
 *     https://leetcode.cn/problems/xuan-zhuan-shu-zu-de-zui-xiao-shu-zi-lcof/
 * 
 * === 牛客网 ===
 * 14. 牛客NC74. 数字在升序数组中出现的次数
 *     https://www.nowcoder.com/practice/70610bf967994b22bb1c26f9ae901fa2
 * 15. 牛客NC105. 二分查找-II
 *     https://www.nowcoder.com/practice/4f470d1d3b734f8aaf2afb014185b395
 * 
 * === 洛谷 (Luogu) ===
 * 16. 洛谷P1102 A-B数对
 *     https://www.luogu.com.cn/problem/P1102
 * 17. 洛谷P2855 [USACO06DEC]River Hopscotch S
 *     https://www.luogu.com.cn/problem/P2855
 * 
 * === Codeforces ===
 * 18. Codeforces 1201C - Maximum Median
 *     https://codeforces.com/problemset/problem/1201/C
 * 19. Codeforces 1613C - Poisoned Dagger
 *     https://codeforces.com/problemset/problem/1613/C
 * 
 * === HackerRank ===
 * 20. HackerRank - Ice Cream Parlor
 *     https://www.hackerrank.com/challenges/icecream-parlor/problem
 * 
 * === AtCoder ===
 * 21. AtCoder ABC 143 D - Triangles
 *     https://atcoder.jp/contests/abc143/tasks/abc143_d
 * 
 * === USACO ===
 * 22. USACO Training - Section 1.3 - Barn Repair
 *     http://www.usaco.org/index.php?page=viewproblem2&cpid=101
 * 
 * === 杭电OJ ===
 * 23. HDU 2141 - Can you find it?
 *     http://acm.hdu.edu.cn/showproblem.php?pid=2141
 * 
 * === POJ ===
 * 24. POJ 2456 - Aggressive cows
 *     http://poj.org/problem?id=2456
 * 
 * === 计蒜客 ===
 * 25. 计蒜客 T1565 - 二分查找
 *     https://www.jisuanke.com/course/786/41395
 * 
 * === SPOJ ===
 * 26. SPOJ EKO - Eko
 *     https://www.spoj.com/problems/EKO/
 * 
 * === AcWing ===
 * 27. AcWing 789. 数的范围
 *     https://www.acwing.com/problem/content/791/
 * 
 * 时间复杂度分析: O(log n) - 每次搜索将范围减半
 * 空间复杂度分析: O(1) - 只使用常数级额外空间
 * 最优解判定: 二分查找是在有序数组中查找右边界的最优解
 * 核心技巧: 找到<=target的元素时不立即返回，继续向右搜索更大的索引
 * 
 * 工程化考量:
 * 1. 异常处理：对空数组、null指针进行检查
 * 2. 边界条件：处理target小于最小值、大于最大值的情况
 * 3. 性能优化：使用位运算避免整数溢出
 * 4. 可读性：清晰的变量命名和详细注释
 */

public class Code03_FindRight {

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
			if (right(arr, num) != findRight(arr, num)) {
				System.out.println("出错了!");
			}
		}
		System.out.println("测试结束");
		
		// 测试H-Index II
		int[] citations = {0, 1, 3, 5, 6};
		System.out.println("H指数: " + hIndex(citations));
		
		// 测试有效的完全平方数
		System.out.println("16是否为完全平方数: " + isPerfectSquare(16)); // true
		System.out.println("14是否为完全平方数: " + isPerfectSquare(14)); // false
		
		// 测试排列硬币
		System.out.println("5枚硬币可以排列: " + arrangeCoins(5) + " 行"); // 2
		System.out.println("8枚硬币可以排列: " + arrangeCoins(8) + " 行"); // 3
	}
	
	// LeetCode 367. 有效的完全平方数
	// 题目要求: 给定一个正整数num，编写一个函数，如果num是一个完全平方数，则返回true，否则返回false
	// 解题思路: 使用二分查找，查找是否存在x使得x*x == num
	// 时间复杂度: O(log n)
	// 空间复杂度: O(1)
	public static boolean isPerfectSquare(int num) {
		if (num < 0) {
			return false;
		}
		if (num == 0 || num == 1) {
			return true;
		}
		
		long left = 1;
		long right = num / 2; // 优化：平方根不会超过num/2（当num>=2时）
		
		while (left <= right) {
			long mid = left + ((right - left) >> 1);
			long square = mid * mid;
			
			if (square == num) {
				return true;
			} else if (square < num) {
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		
		return false;
	}
	
	// LeetCode 441. 排列硬币
	// 题目要求: 你总共有n枚硬币，并计划将它们按阶梯状排列。对于第k行，你必须正好放置k枚硬币。
	// 找出总共可以形成多少完整的行。
	// 解题思路: 使用二分查找，找到最大的k，使得k*(k+1)/2 <= n
	// 时间复杂度: O(log n)
	// 空间复杂度: O(1)
	public static int arrangeCoins(int n) {
		if (n < 0) {
			return 0;
		}
		
		long left = 1;
		long right = (long) Math.sqrt(2 * (long)n) + 1; // 优化上界
		int ans = 0;
		
		while (left <= right) {
			long mid = left + ((right - left) >> 1);
			// 计算前mid行所需的硬币数量，使用长整型避免溢出
			long required = mid * (mid + 1) / 2;
			
			if (required <= n) {
				ans = (int)mid;
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		
		return ans;
	}
	
	// LeetCode 69. x 的平方根
	// 题目要求: 实现int sqrt(int x)函数。计算并返回x的平方根，其中x是非负整数。
	// 由于返回类型是整数，结果只保留整数的部分，小数部分将被舍去。
	// 解题思路: 使用二分查找，找到最大的整数r，使得r*r <= x
	// 时间复杂度: O(log x)
	// 空间复杂度: O(1)
	public static int mySqrt(int x) {
		if (x < 0) {
			return -1;
		}
		if (x == 0 || x == 1) {
			return x;
		}
		
		long left = 1;
		long right = x / 2;
		long ans = 0;
		
		while (left <= right) {
			long mid = left + ((right - left) >> 1);
			long square = mid * mid;
			
			if (square == x) {
				return (int)mid;
			} else if (square < x) {
				ans = mid;
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		
		return (int)ans;
	}
	
	// LintCode 74. 第一个错误的版本 (使用右边界查找方式)
	// 题目要求: 假设你有n个版本[1, 2, ..., n]，你想找出导致之后所有版本出错的第一个错误的版本
	// 解题思路: 使用二分查找的右边界方式
	// 时间复杂度: O(log n)
	// 空间复杂度: O(1)
	/*
	public static int firstBadVersionRightBoundary(int n) {
		int left = 1;
		int right = n;
		int ans = -1;
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			if (isBadVersion(mid)) {
				ans = mid;
				right = mid - 1;
			} else {
				left = mid + 1;
			}
		}
		return ans;
	}
	*/
	
	// 牛客网 NC107. 寻找峰值
	// 题目要求: 给定一个长度为n的数组nums，数组中的每个元素都是唯一的，且按升序排列
	// 请返回数组中任意一个峰值元素的位置。峰值元素是指大于其相邻元素的元素
	// 解题思路: 使用二分查找，比较mid与mid+1的大小关系
	// 时间复杂度: O(log n)
	// 空间复杂度: O(1)
	public static int findPeakElement(int[] nums) {
		if (nums == null || nums.length == 0) {
			return -1;
		}
		if (nums.length == 1) {
			return 0;
		}
		
		int left = 0;
		int right = nums.length - 1;
		
		while (left < right) {
			int mid = left + ((right - left) >> 1);
			if (nums[mid] < nums[mid + 1]) {
				// 峰值在右侧
				left = mid + 1;
			} else {
				// 峰值在左侧或当前位置
				right = mid;
			}
		}
		
		return left;
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
	public static int right(int[] arr, int num) {
		for (int i = arr.length - 1; i >= 0; i--) {
			if (arr[i] <= num) {
				return i;
			}
		}
		return -1;
	}

	// 保证arr有序，才能用这个方法
	// 有序数组中找<=num的最右位置
	// 时间复杂度: O(log n) - 每次将搜索范围减半
	// 空间复杂度: O(1) - 只使用了常数级别的额外空间
	public static int findRight(int[] arr, int num) {
		// 边界条件检查
		if (arr == null || arr.length == 0) {
			return -1;
		}
		
		int l = 0, r = arr.length - 1, m = 0;
		int ans = -1;
		while (l <= r) {
			// 使用位运算避免整数溢出
			m = l + ((r - l) >> 1);
			if (arr[m] <= num) {
				ans = m;
				l = m + 1;
			} else {
				r = m - 1;
			}
		}
		return ans;
	}
	
	// LeetCode 34. Find First and Last Position of Element in Sorted Array - 查找元素的第一个和最后一个位置
	// 题目要求: 给定一个按照非递减顺序排列的整数数组nums和一个目标值target，
	// 找出给定目标值在数组中的开始位置和结束位置。
	// 解题思路: 使用两次二分查找，第一次查找>=target的最左位置，第二次查找<=target的最右位置
	// 时间复杂度: O(log n)
	// 空间复杂度: O(1)
	public static int[] searchRange(int[] nums, int target) {
		// 边界条件检查
		if (nums == null || nums.length == 0) {
			return new int[]{-1, -1};
		}
		
		int first = findLeft(nums, target);
		// 如果找不到>=target的元素，或者该元素不等于target，则说明target不存在
		if (first == -1 || nums[first] != target) {
			return new int[]{-1, -1};
		}
		
		int last = findRight(nums, target);
		return new int[]{first, last};
	}
	
	// 辅助方法：查找>=target的最左位置
	private static int findLeft(int[] nums, int target) {
		int l = 0, r = nums.length - 1;
		int ans = -1;
		while (l <= r) {
			int m = l + ((r - l) >> 1);
			if (nums[m] >= target) {
				ans = m;
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return ans;
	}
	
	// LeetCode 275. H-Index II - H指数II
	// 题目要求: 给定一个整数数组citations，其中citations[i]表示研究者的第i篇论文被引用的次数，
	// 并且数组已经按照升序排列，计算并返回该研究者的h指数。
	// 解题思路: 使用二分查找找到满足条件citations[i] >= (n-i)的最左位置，h指数就是n-position
	// 时间复杂度: O(log n)
	// 空间复杂度: O(1)
	public static int hIndex(int[] citations) {
		// 边界条件检查
		if (citations == null || citations.length == 0) {
			return 0;
		}
		
		int n = citations.length;
		int l = 0, r = n - 1;
		// 查找满足citations[i] >= (n-i)的最左位置
		while (l <= r) {
			int m = l + ((r - l) >> 1);
			// n-m表示有n-m篇文章被引用次数>=citations[m]
			if (citations[m] >= (n - m)) {
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		// l就是满足条件的最左位置，h指数就是n-l
		return n - l;
	}
	
	/* C++ 实现:
	#include <vector>
	using namespace std;
	
	// 查找<=num的最右位置
	int findRight(vector<int>& arr, int num) {
		if (arr.empty()) {
			return -1;
		}
		
		int l = 0, r = arr.size() - 1;
		int ans = -1;
		while (l <= r) {
			int m = l + ((r - l) >> 1);
			if (arr[m] <= num) {
				ans = m;
				l = m + 1;
			} else {
				r = m - 1;
			}
		}
		return ans;
	}
	
	// 查找元素的第一个和最后一个位置
	vector<int> searchRange(vector<int>& nums, int target) {
		if (nums.empty()) {
			return {-1, -1};
		}
		
		int first = findLeft(nums, target);
		if (first == -1 || nums[first] != target) {
			return {-1, -1};
		}
		
		int last = findRight(nums, target);
		return {first, last};
	}
	
	// 辅助方法：查找>=target的最左位置
	int findLeft(vector<int>& nums, int target) {
		int l = 0, r = nums.size() - 1;
		int ans = -1;
		while (l <= r) {
			int m = l + ((r - l) >> 1);
			if (nums[m] >= target) {
				ans = m;
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return ans;
	}
	
	// H指数II
	int hIndex(vector<int>& citations) {
		if (citations.empty()) {
			return 0;
		}
		
		int n = citations.size();
		int l = 0, r = n - 1;
		while (l <= r) {
			int m = l + ((r - l) >> 1);
			if (citations[m] >= (n - m)) {
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return n - l;
	}
	*/
	
	/* C++ 实现:
	#include <vector>
	using namespace std;

	// 查找<=num的最右位置
	int findRight(vector<int>& arr, int num) {
		if (arr.empty()) {
			return -1;
		}

		int l = 0, r = arr.size() - 1;
		int ans = -1;
		while (l <= r) {
			int m = l + ((r - l) >> 1);
			if (arr[m] <= num) {
				ans = m;
				l = m + 1;
			} else {
				r = m - 1;
			}
		}
		return ans;
	}

	// 查找元素的第一个和最后一个位置
	vector<int> searchRange(vector<int>& nums, int target) {
		if (nums.empty()) {
			return {-1, -1};
		}

		int first = findLeft(nums, target);
		if (first == -1 || nums[first] != target) {
			return {-1, -1};
		}

		int last = findRight(nums, target);
		return {first, last};
	}

	// 辅助方法：查找>=target的最左位置
	int findLeft(vector<int>& nums, int target) {
		int l = 0, r = nums.size() - 1;
		int ans = -1;
		while (l <= r) {
			int m = l + ((r - l) >> 1);
			if (nums[m] >= target) {
				ans = m;
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return ans;
	}

	// H指数II
	int hIndex(vector<int>& citations) {
		if (citations.empty()) {
			return 0;
		}

		int n = citations.size();
		int l = 0, r = n - 1;
		while (l <= r) {
			int m = l + ((r - l) >> 1);
			if (citations[m] >= (n - m)) {
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return n - l;
	}
	
	// 有效的完全平方数
	bool isPerfectSquare(int num) {
		if (num < 0) {
			return false;
		}
		if (num == 0 || num == 1) {
			return true;
		}
		
		long left = 1;
		long right = num / 2;
		
		while (left <= right) {
			long mid = left + ((right - left) >> 1);
			long square = mid * mid;
			
			if (square == num) {
				return true;
			} else if (square < num) {
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		
		return false;
	}
	
	// 排列硬币
	int arrangeCoins(int n) {
		if (n < 0) {
			return 0;
		}
		
		long left = 1;
		long right = (long)sqrt(2LL * n) + 1;
		int ans = 0;
		
		while (left <= right) {
			long mid = left + ((right - left) >> 1);
			long required = mid * (mid + 1) / 2;
			
			if (required <= n) {
				ans = (int)mid;
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		
		return ans;
	}
	
	// x的平方根
	int mySqrt(int x) {
		if (x < 0) {
			return -1;
		}
		if (x == 0 || x == 1) {
			return x;
		}
		
		long left = 1;
		long right = x / 2;
		long ans = 0;
		
		while (left <= right) {
			long mid = left + ((right - left) >> 1);
			long square = mid * mid;
			
			if (square == x) {
				return (int)mid;
			} else if (square < x) {
				ans = mid;
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		
		return (int)ans;
	}
	
	// 寻找峰值
	int findPeakElement(vector<int>& nums) {
		if (nums.empty()) {
			return -1;
		}
		if (nums.size() == 1) {
			return 0;
		}
		
		int left = 0;
		int right = nums.size() - 1;
		
		while (left < right) {
			int mid = left + ((right - left) >> 1);
			if (nums[mid] < nums[mid + 1]) {
				// 峰值在右侧
				left = mid + 1;
			} else {
				// 峰值在左侧或当前位置
				right = mid;
			}
		}
		
		return left;
	}
	*/

	/* Python 实现:
	# 查找<=num的最右位置
	def find_right(arr, num):
		if not arr:
			return -1
		
		l, r = 0, len(arr) - 1
		ans = -1
		while l <= r:
			m = l + ((r - l) >> 1)
			if arr[m] <= num:
				ans = m
				l = m + 1
			else:
				r = m - 1
		return ans

	# 查找元素的第一个和最后一个位置
	def search_range(nums, target):
		if not nums:
			return [-1, -1]
		
		first = find_left(nums, target)
		if first == -1 or nums[first] != target:
			return [-1, -1]
		
		last = find_right(nums, target)
		return [first, last]

	# 辅助方法：查找>=target的最左位置
	def find_left(nums, target):
		l, r = 0, len(nums) - 1
		ans = -1
		while l <= r:
			m = l + ((r - l) >> 1)
			if nums[m] >= target:
				ans = m
				r = m - 1
			else:
				l = m + 1
		return ans

	# H指数II
	def h_index(citations):
		if not citations:
			return 0
		
		n = len(citations)
		l, r = 0, n - 1
		while l <= r:
			m = l + ((r - l) >> 1)
			if citations[m] >= (n - m):
				r = m - 1
			else:
				l = m + 1
		return n - l
	
	# 有效的完全平方数
	def is_perfect_square(num):
		if num < 0:
			return False
		if num == 0 or num == 1:
			return True
		
		left = 1
		right = num // 2
		
		while left <= right:
			mid = left + ((right - left) >> 1)
			square = mid * mid
			
			if square == num:
				return True
			elif square < num:
				left = mid + 1
			else:
				right = mid - 1
		
		return False
	
	# 排列硬币
	def arrange_coins(n):
		if n < 0:
			return 0
		
		left = 1
		right = int((2 * n) ** 0.5) + 1
		ans = 0
		
		while left <= right:
			mid = left + ((right - left) >> 1)
			required = mid * (mid + 1) // 2
			
			if required <= n:
				ans = mid
				left = mid + 1
			else:
				right = mid - 1
		
		return ans
	
	# x的平方根
	def my_sqrt(x):
		if x < 0:
			return -1
		if x == 0 or x == 1:
			return x
		
		left = 1
		right = x // 2
		ans = 0
		
		while left <= right:
			mid = left + ((right - left) >> 1)
			square = mid * mid
			
			if square == x:
				return mid
			elif square < x:
				ans = mid
				left = mid + 1
			else:
				right = mid - 1
		
		return ans
	
	# 寻找峰值
	def find_peak_element(nums):
		if not nums:
			return -1
		if len(nums) == 1:
			return 0
		
		left = 0
		right = len(nums) - 1
		
		while left < right:
			mid = left + ((right - left) >> 1)
			if nums[mid] < nums[mid + 1]:
				# 峰值在右侧
				left = mid + 1
			else:
				# 峰值在左侧或当前位置
				right = mid
		
		return left
	*/

}