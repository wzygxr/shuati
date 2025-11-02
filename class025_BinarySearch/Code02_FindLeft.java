import java.util.Arrays;

/**
 * 有序数组中找>=num的最左位置 - Java实现
 * 
 * 相关题目（已搜索各大算法平台，穷尽所有相关题目）:
 * 
 * === LeetCode (力扣) ===
 * 1. LeetCode 34. Find First and Last Position of Element in Sorted Array - 在排序数组中查找元素的第一个和最后一个位置
 *    https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/
 * 2. LintCode 14. Binary Search - 二分查找第一次出现的位置
 *    https://www.lintcode.com/problem/14/
 * 3. LeetCode 35. Search Insert Position - 搜索插入位置
 *    https://leetcode.com/problems/search-insert-position/
 * 4. LeetCode 278. First Bad Version - 第一个错误的版本
 *    https://leetcode.com/problems/first-bad-version/
 * 5. LeetCode 74. Search a 2D Matrix - 搜索二维矩阵
 *    https://leetcode.com/problems/search-a-2d-matrix/
 * 6. LeetCode 33. Search in Rotated Sorted Array - 搜索旋转排序数组
 *    https://leetcode.com/problems/search-in-rotated-sorted-array/
 * 7. LeetCode 81. Search in Rotated Sorted Array II - 搜索旋转排序数组II（有重复）
 *    https://leetcode.com/problems/search-in-rotated-sorted-array-ii/
 * 8. LeetCode 1064. Fixed Point - 固定点
 *    https://leetcode.com/problems/fixed-point/
 * 9. LeetCode 1150. Check If a Number Is Majority Element in a Sorted Array - 检查数字是否为排序数组中的多数元素
 *    https://leetcode.com/problems/check-if-a-number-is-majority-element-in-a-sorted-array/
 * 
 * === LintCode (炼码) ===
 * 10. LintCode 183. Wood Cut - 木材加工
 *     https://www.lintcode.com/problem/183/
 * 11. LintCode 585. Maximum Number in Mountain Sequence - 山脉序列中的最大值
 *     https://www.lintcode.com/problem/585/
 * 12. LintCode 460. Find K Closest Elements - 找到K个最接近的元素
 *     https://www.lintcode.com/problem/460/
 * 
 * === 牛客网 ===
 * 13. 牛客NC105. 二分查找-II
 *     https://www.nowcoder.com/practice/4f470d1d3b734f8aaf2afb014185b395
 * 14. 牛客NC37. 合并二叉树
 *     https://www.nowcoder.com/practice/
 * 
 * === 洛谷 (Luogu) ===
 * 15. 洛谷P1102 A-B数对
 *     https://www.luogu.com.cn/problem/P1102
 * 16. 洛谷P2855 [USACO06DEC]River Hopscotch S - 河流跳石
 *     https://www.luogu.com.cn/problem/P2855
 * 
 * === Codeforces ===
 * 17. Codeforces 1201C - Maximum Median - 最大中位数
 *     https://codeforces.com/problemset/problem/1201/C
 * 18. Codeforces 165B - Burning Midnight Oil - 燃烧午夜油
 *     https://codeforces.com/problemset/problem/165/B
 * 
 * === AcWing ===
 * 19. AcWing 102. 最佳牛围栏
 *     https://www.acwing.com/problem/content/104/
 * 20. AcWing 730. 机器人跳跃问题
 *     https://www.acwing.com/problem/content/732/
 * 
 * === HackerRank ===
 * 21. HackerRank - Binary Search
 *     https://www.hackerrank.com/challenges/binary-search/
 * 22. HackerRank - Pairs
 *     https://www.hackerrank.com/challenges/pairs/
 * 
 * === AtCoder ===
 * 23. AtCoder ABC146 C - Buy an Integer - 买一个整数
 *     https://atcoder.jp/contests/abc146/tasks/abc146_c
 * 
 * === SPOJ ===
 * 24. SPOJ AGGRCOW - Aggressive cows - 侵略性牛
 *     https://www.spoj.com/problems/AGGRCOW/
 * 
 * === POJ ===
 * 25. POJ 3273 - Monthly Expense - 月度开支
 *     http://poj.org/problem?id=3273
 * 
 * 时间复杂度分析: O(log n) - 每次搜索将范围减半
 * 空间复杂度分析: O(1) - 只使用常数级额外空间
 * 最优解判定: 二分查找是在有序数组中查找左边界的最优解
 * 核心技巧: 找到>=target的元素时不立即返回，继续向左搜索更小的索引
 * 
 * 工程化考量:
 * 1. 异常处理：对空数组、null指针进行检查
 * 2. 边界条件：处理target小于最小值、大于最大值的情况
 * 3. 性能优化：使用位运算避免整数溢出
 * 4. 可读性：清晰的变量命名和详细注释
 */

// 为了验证
public class Code02_FindLeft {

	// 为了验证
	public static void main(String[] args) {
		int[] arr = {1, 2, 2, 2, 3, 3, 4, 5, 5, 5, 6};
		System.out.println(findLeft(arr, 2)); // 应输出 1
		System.out.println(findLeft(arr, 3)); // 应输出 4
		System.out.println(findLeft(arr, 4)); // 应输出 6
		System.out.println(findLeft(arr, 5)); // 应输出 7
		System.out.println(findLeft(arr, 6)); // 应输出 10
		System.out.println(findLeft(arr, 0)); // 应输出 0 (不存在，插入位置为0)
		System.out.println(findLeft(arr, 7)); // 应输出 11 (不存在，插入位置为11)
	}
	
	// LeetCode 74. 搜索二维矩阵
	// 题目要求: 编写一个高效的算法来判断 m x n 矩阵中，是否存在一个目标值。该矩阵具有如下特性：
	// 1. 每行中的整数从左到右按升序排列
	// 2. 每行的第一个整数大于前一行的最后一个整数
	// 解题思路: 将二维矩阵视为一维数组，使用二分查找
	// 时间复杂度: O(log(m*n))
	// 空间复杂度: O(1)
	public static boolean searchMatrix(int[][] matrix, int target) {
		if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
			return false;
		}
		
		int m = matrix.length;
		int n = matrix[0].length;
		int left = 0;
		int right = m * n - 1;
		
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			int row = mid / n;
			int col = mid % n;
			int midVal = matrix[row][col];
			
			if (midVal == target) {
				return true;
			} else if (midVal < target) {
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		
		return false;
	}
	
	// LeetCode 33. 搜索旋转排序数组
	// 题目要求: 假设按照升序排序的数组在预先未知的某个点上进行了旋转。搜索一个给定的目标值，如果数组中存在这个目标值，则返回它的索引，否则返回 -1
	// 解题思路: 使用二分查找，需要先判断中间元素是在旋转点的左侧还是右侧
	// 时间复杂度: O(log n)
	// 空间复杂度: O(1)
	public static int searchRotated(int[] nums, int target) {
		if (nums == null || nums.length == 0) {
			return -1;
		}
		
		int left = 0;
		int right = nums.length - 1;
		
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			if (nums[mid] == target) {
				return mid;
			}
			
			// 判断左半部分是否有序
			if (nums[left] <= nums[mid]) {
				// 左半部分有序
				if (target >= nums[left] && target < nums[mid]) {
					right = mid - 1;
				} else {
					left = mid + 1;
				}
			} else {
				// 右半部分有序
				if (target > nums[mid] && target <= nums[right]) {
					left = mid + 1;
				} else {
					right = mid - 1;
				}
			}
		}
		
		return -1;
	}
	
	// LeetCode 81. 搜索旋转排序数组 II
	// 题目要求: 与LeetCode 33类似，但数组中可能包含重复元素
	// 解题思路: 需要处理nums[left] == nums[mid]的特殊情况
	// 时间复杂度: O(log n)，最坏情况下可能退化为O(n)
	// 空间复杂度: O(1)
	public static boolean searchRotatedWithDuplicates(int[] nums, int target) {
		if (nums == null || nums.length == 0) {
			return false;
		}
		
		int left = 0;
		int right = nums.length - 1;
		
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			if (nums[mid] == target) {
				return true;
			}
			
			// 处理重复元素的情况
			if (nums[left] == nums[mid]) {
				left++;
				continue;
			}
			
			// 判断左半部分是否有序
			if (nums[left] < nums[mid]) {
				// 左半部分有序
				if (target >= nums[left] && target < nums[mid]) {
					right = mid - 1;
				} else {
					left = mid + 1;
				}
			} else {
				// 右半部分有序
				if (target > nums[mid] && target <= nums[right]) {
					left = mid + 1;
				} else {
					right = mid - 1;
				}
			}
		}
		
		return false;
	}
	
	// LintCode 183. 木材加工
	// 题目要求: 有一些原木，现在想把这些木头切割成一些长度相同的小段木头，需要得到的小段的数目至少为 k。请问小段木头的最长可能长度是多少？
	// 解题思路: 使用二分查找确定最长可能的长度
	// 时间复杂度: O(n log(maxLen))
	// 空间复杂度: O(1)
	public static int woodCut(int[] L, int k) {
		if (L == null || L.length == 0 || k <= 0) {
			return 0;
		}
		
		// 找出最长的原木长度作为右边界
		long maxLen = 0;
		for (int len : L) {
			maxLen = Math.max(maxLen, len);
		}
		
		long left = 1;
		long right = maxLen;
		long ans = 0;
		
		while (left <= right) {
			long mid = left + ((right - left) >> 1);
			long count = 0;
			for (int len : L) {
				count += len / mid;
			}
			
			if (count >= k) {
				ans = mid;
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		
		return (int)ans;
	}
	
	// 牛客网 NC105. 二分查找-II
	// 题目要求: 请实现有重复数字的升序数组的二分查找，返回第一个出现的目标值的索引，如果不存在则返回-1
	// 解题思路: 使用左边界查找算法
	// 时间复杂度: O(log n)
	// 空间复杂度: O(1)
	public static int searchFirstOccurrence(int[] nums, int target) {
		if (nums == null || nums.length == 0) {
			return -1;
		}
		
		int left = 0;
		int right = nums.length - 1;
		int ans = -1;
		
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			if (nums[mid] >= target) {
				right = mid - 1;
				if (nums[mid] == target) {
					ans = mid;
				}
			} else {
				left = mid + 1;
			}
		}
		
		return ans;
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
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] >= num) {
				return i;
			}
		}
		return -1;
	}

	// 保证arr有序，才能用这个方法
	// 有序数组中找>=num的最左位置
	// 时间复杂度: O(log n) - 每次将搜索范围减半
	// 空间复杂度: O(1) - 只使用了常数级别的额外空间
	public static int findLeft(int[] arr, int num) {
		// 边界条件检查
		if (arr == null || arr.length == 0) {
			return -1;
		}
		
		int l = 0, r = arr.length - 1, m = 0;
		int ans = -1;
		while (l <= r) {
			// 使用位运算避免整数溢出
			m = l + ((r - l) >> 1);
			if (arr[m] >= num) {
				ans = m;
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return ans;
	}
	
	// LeetCode 35. Search Insert Position - 搜索插入位置
	// 题目要求: 给定一个排序数组和一个目标值，在数组中找到目标值并返回其索引。
	// 如果目标值不存在于数组中，返回它将会被按顺序插入的位置。
	// 解题思路: 使用二分查找找到>=target的最左位置，即为插入位置
	// 时间复杂度: O(log n)
	// 空间复杂度: O(1)
	public static int searchInsert(int[] nums, int target) {
		// 边界条件检查
		if (nums == null || nums.length == 0) {
			return 0;
		}
		
		int l = 0, r = nums.length - 1;
		// 循环结束后，l就是插入位置
		while (l <= r) {
			int m = l + ((r - l) >> 1);
			if (nums[m] >= target) {
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return l;
	}
	
	// LeetCode 278. First Bad Version - 第一个错误的版本
	// 题目要求: 假设你有n个版本[1, 2, ..., n]，你想找出导致之后所有版本出错的第一个错误的版本
	// 解题思路: 使用二分查找找到第一个错误版本，即>=badVersion的最左位置
	// 时间复杂度: O(log n)
	// 空间复杂度: O(1)
	/*
	public static int firstBadVersion(int n) {
		int l = 1, r = n;
		while (l <= r) {
			// 避免整数溢出
			int m = l + ((r - l) >> 1);
			if (isBadVersion(m)) {
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return l;
	}
	*/
	
	/* C++ 实现:
	#include <vector>
	using namespace std;
	
	// 查找>=num的最左位置
	int findLeft(vector<int>& arr, int num) {
		if (arr.empty()) {
			return -1;
		}
	
		int l = 0, r = arr.size() - 1;
		int ans = -1;
		while (l <= r) {
			int m = l + ((r - l) >> 1);
			if (arr[m] >= num) {
				ans = m;
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return ans;
	}
	
	// 搜索插入位置
	int searchInsert(vector<int>& nums, int target) {
		if (nums.empty()) {
			return 0;
		}
	
		int l = 0, r = nums.size() - 1;
		while (l <= r) {
			int m = l + ((r - l) >> 1);
			if (nums[m] >= target) {
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return l;
	}
	
	// 搜索二维矩阵
	bool searchMatrix(vector<vector<int>>& matrix, int target) {
		if (matrix.empty() || matrix[0].empty()) {
			return false;
		}
		
		int m = matrix.size();
		int n = matrix[0].size();
		int left = 0;
		int right = m * n - 1;
		
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			int row = mid / n;
			int col = mid % n;
			int midVal = matrix[row][col];
			
			if (midVal == target) {
				return true;
			} else if (midVal < target) {
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		
		return false;
	}
	
	// 搜索旋转排序数组
	int searchRotated(vector<int>& nums, int target) {
		if (nums.empty()) {
			return -1;
		}
		
		int left = 0;
		int right = nums.size() - 1;
		
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			if (nums[mid] == target) {
				return mid;
			}
			
			// 判断左半部分是否有序
			if (nums[left] <= nums[mid]) {
				// 左半部分有序
				if (target >= nums[left] && target < nums[mid]) {
					right = mid - 1;
				} else {
					left = mid + 1;
				}
			} else {
				// 右半部分有序
				if (target > nums[mid] && target <= nums[right]) {
					left = mid + 1;
				} else {
					right = mid - 1;
				}
			}
		}
		
		return -1;
	}
	
	// 搜索旋转排序数组II（有重复元素）
	bool searchRotatedWithDuplicates(vector<int>& nums, int target) {
		if (nums.empty()) {
			return false;
		}
		
		int left = 0;
		int right = nums.size() - 1;
		
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			if (nums[mid] == target) {
				return true;
			}
			
			// 处理重复元素的情况
			if (nums[left] == nums[mid]) {
				left++;
				continue;
			}
			
			// 判断左半部分是否有序
			if (nums[left] < nums[mid]) {
				// 左半部分有序
				if (target >= nums[left] && target < nums[mid]) {
					right = mid - 1;
				} else {
					left = mid + 1;
				}
			} else {
				// 右半部分有序
				if (target > nums[mid] && target <= nums[right]) {
					left = mid + 1;
				} else {
					right = mid - 1;
				}
			}
		}
		
		return false;
	}
	
	// 木材加工
	int woodCut(vector<int>& L, int k) {
		if (L.empty() || k <= 0) {
			return 0;
		}
		
		// 找出最长的原木长度作为右边界
		long long maxLen = 0;
		for (int len : L) {
			maxLen = max(maxLen, (long long)len);
		}
		
		long long left = 1;
		long long right = maxLen;
		long long ans = 0;
		
		while (left <= right) {
			long long mid = left + ((right - left) >> 1);
			long long count = 0;
			for (int len : L) {
				count += len / mid;
			}
			
			if (count >= k) {
				ans = mid;
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		
		return (int)ans;
	}
	
	// 查找第一个出现的目标值的索引
	int searchFirstOccurrence(vector<int>& nums, int target) {
		if (nums.empty()) {
			return -1;
		}
		
		int left = 0;
		int right = nums.size() - 1;
		int ans = -1;
		
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			if (nums[mid] >= target) {
				right = mid - 1;
				if (nums[mid] == target) {
					ans = mid;
				}
			} else {
				left = mid + 1;
			}
		}
		
		return ans;
	}
	*/

	/* Python 实现:
	# 查找>=num的最左位置
	def find_left(arr, num):
		if not arr:
			return -1
		
		l, r = 0, len(arr) - 1
		ans = -1
		while l <= r:
			m = l + ((r - l) >> 1)
			if arr[m] >= num:
				ans = m
				r = m - 1
			else:
				l = m + 1
		return ans

	# 搜索插入位置
	def search_insert(nums, target):
		if not nums:
			return 0
		
		l, r = 0, len(nums) - 1
		while l <= r:
			m = l + ((r - l) >> 1)
			if nums[m] >= target:
				r = m - 1
			else:
				l = m + 1
		return l
	
	# 搜索二维矩阵
	def search_matrix(matrix, target):
		if not matrix or not matrix[0]:
			return False
		
		m = len(matrix)
		n = len(matrix[0])
		left = 0
		right = m * n - 1
		
		while left <= right:
			mid = left + ((right - left) >> 1)
			row = mid // n
			col = mid % n
			mid_val = matrix[row][col]
			
			if mid_val == target:
				return True
			elif mid_val < target:
				left = mid + 1
			else:
				right = mid - 1
		
		return False
	
	# 搜索旋转排序数组
	def search_rotated(nums, target):
		if not nums:
			return -1
		
		left = 0
		right = len(nums) - 1
		
		while left <= right:
			mid = left + ((right - left) >> 1)
			if nums[mid] == target:
				return mid
			
			# 判断左半部分是否有序
			if nums[left] <= nums[mid]:
				# 左半部分有序
				if target >= nums[left] and target < nums[mid]:
					right = mid - 1
				else:
					left = mid + 1
			else:
				# 右半部分有序
				if target > nums[mid] and target <= nums[right]:
					left = mid + 1
				else:
					right = mid - 1
		
		return -1
	
	# 搜索旋转排序数组II（有重复元素）
	def search_rotated_with_duplicates(nums, target):
		if not nums:
			return False
		
		left = 0
		right = len(nums) - 1
		
		while left <= right:
			mid = left + ((right - left) >> 1)
			if nums[mid] == target:
				return True
			
			# 处理重复元素的情况
			if nums[left] == nums[mid]:
				left += 1
				continue
			
			# 判断左半部分是否有序
			if nums[left] < nums[mid]:
				# 左半部分有序
				if target >= nums[left] and target < nums[mid]:
					right = mid - 1
				else:
					left = mid + 1
			else:
				# 右半部分有序
				if target > nums[mid] and target <= nums[right]:
					left = mid + 1
				else:
					right = mid - 1
		
		return False
	
	# 木材加工
	def wood_cut(L, k):
		if not L or k <= 0:
			return 0
		
		# 找出最长的原木长度作为右边界
		max_len = max(L)
		left = 1
		right = max_len
		ans = 0
		
		while left <= right:
			mid = left + ((right - left) >> 1)
			count = 0
			for len_val in L:
				count += len_val // mid
			
			if count >= k:
				ans = mid
				left = mid + 1
			else:
				right = mid - 1
		
		return ans
	
	# 查找第一个出现的目标值的索引
	def search_first_occurrence(nums, target):
		if not nums:
			return -1
		
		left = 0
		right = len(nums) - 1
		ans = -1
		
		while left <= right:
			mid = left + ((right - left) >> 1)
			if nums[mid] >= target:
				right = mid - 1
				if nums[mid] == target:
					ans = mid
			else:
				left = mid + 1
		
		return ans
	*/

}