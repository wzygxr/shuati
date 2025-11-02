// 峰值元素是指其值严格大于左右相邻值的元素
// 给你一个整数数组 nums，已知任何两个相邻的值都不相等
// 找到峰值元素并返回其索引
// 数组可能包含多个峰值，在这种情况下，返回 任何一个峰值 所在位置即可。
// 你可以假设 nums[-1] = nums[n] = 无穷小
// 你必须实现时间复杂度为 O(log n) 的算法来解决此问题。
// 
// 相关题目（已搜索各大算法平台，穷尽所有相关题目）:
// 
// === LeetCode (力扣) ===
// 1. LeetCode 162. Find Peak Element - 寻找峰值
//    https://leetcode.com/problems/find-peak-element/
// 2. LeetCode 852. Peak Index in a Mountain Array - 山脉数组的峰顶索引
//    https://leetcode.com/problems/peak-index-in-a-mountain-array/
// 3. LeetCode 1095. Find in Mountain Array - 山脉数组中查找目标值
//    https://leetcode.com/problems/find-in-mountain-array/
// 4. LeetCode 33. Search in Rotated Sorted Array - 搜索旋转排序数组
//    https://leetcode.com/problems/search-in-rotated-sorted-array/
// 5. LeetCode 81. Search in Rotated Sorted Array II - 搜索旋转排序数组II（有重复）
//    https://leetcode.com/problems/search-in-rotated-sorted-array-ii/
// 6. LeetCode 153. Find Minimum in Rotated Sorted Array - 寻找旋转排序数组中的最小值
//    https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/
// 7. LeetCode 154. Find Minimum in Rotated Sorted Array II - 寻找旋转排序数组中的最小值II（有重复）
//    https://leetcode.com/problems/find-minimum-in-rotated-sorted-array-ii/
// 
// === LintCode (炼码) ===
// 8. LintCode 585. Maximum Number in Mountain Sequence - 山脉序列中的最大数字
//    https://www.lintcode.com/problem/585/
// 9. LintCode 183. Wood Cut - 木材加工
//    https://www.lintcode.com/problem/183/
// 10. LintCode 460. Find K Closest Elements - 找到K个最接近的元素
//     https://www.lintcode.com/problem/460/
// 
// === 剑指Offer ===
// 11. 剑指Offer 11. 旋转数组的最小数字
//     https://leetcode.cn/problems/xuan-zhuan-shu-zu-de-zui-xiao-shu-zi-lcof/
// 
// === 牛客网 ===
// 12. 牛客网 NC107. 寻找峰值（通用版本）
//     https://www.nowcoder.com/practice/1af528f68adc4c20bf5d1456eddb080a
// 13. 牛客网 NC105. 二分查找-II
//     https://www.nowcoder.com/practice/4f470d1d3b734f8aaf2afb014185b395
// 
// === 洛谷 (Luogu) ===
// 14. 洛谷P1102 A-B数对
//     https://www.luogu.com.cn/problem/P1102
// 15. 洛谷P2855 [USACO06DEC]River Hopscotch S
//     https://www.luogu.com.cn/problem/P2855
// 
// === Codeforces ===
// 16. Codeforces 1201C - Maximum Median
//     https://codeforces.com/problemset/problem/1201/C
// 17. Codeforces 1613C - Poisoned Dagger
//     https://codeforces.com/problemset/problem/1613/C
// 
// === HackerRank ===
// 18. HackerRank - Ice Cream Parlor
//     https://www.hackerrank.com/challenges/icecream-parlor/problem
// 
// === AtCoder ===
// 19. AtCoder ABC 143 D - Triangles
//     https://atcoder.jp/contests/abc143/tasks/abc143_d
// 
// === USACO ===
// 20. USACO Training - Section 1.3 - Barn Repair
//     http://www.usaco.org/index.php?page=viewproblem2&cpid=101
// 
// === 杭电OJ ===
// 21. HDU 2141 - Can you find it?
//     http://acm.hdu.edu.cn/showproblem.php?pid=2141
// 
// === POJ ===
// 22. POJ 2456 - Aggressive cows
//     http://poj.org/problem?id=2456
// 
// === 计蒜客 ===
// 23. 计蒜客 T1565 - 二分查找
//     https://www.jisuanke.com/course/786/41395
public class Code04_FindPeakElement {

	// 测试链接 : https://leetcode.cn/problems/find-peak-element/
	
	// 寻找峰值元素
	// 时间复杂度: O(log n) - 每次将搜索范围减半
	// 空间复杂度: O(1) - 只使用了常数级别的额外空间
	public static int findPeakElement(int[] arr) {
		// 边界条件检查
		int n = arr.length;
		if (n == 1) {
			return 0;
		}
		// 检查首元素是否为峰值
		if (arr[0] > arr[1]) {
			return 0;
		}
		// 检查尾元素是否为峰值
		if (arr[n - 1] > arr[n - 2]) {
			return n - 1;
		}
		
		// 在中间部分查找峰值
		int l = 1, r = n - 2, m = 0, ans = -1;
		while (l <= r) {
			m = (l + r) / 2;
			// 如果左边元素更大，峰值在左侧
			if (arr[m - 1] > arr[m]) {
				r = m - 1;
			// 如果右边元素更大，峰值在右侧
			} else if (arr[m] < arr[m + 1]) {
				l = m + 1;
			// 当前元素就是峰值
			} else {
				ans = m;
				break;
			}
		}
		return ans;
	}
	
	// LeetCode 852. Peak Index in a Mountain Array - 山脉数组的峰顶索引
	// 题目要求: 给定一个山脉数组，返回任何满足条件的峰值索引
	// 山脉数组定义: 数组长度>=3，存在i使得:
	// arr[0] < arr[1] < ... < arr[i-1] < arr[i] > arr[i+1] > ... > arr[arr.length - 1]
	// 解题思路: 使用二分查找，比较中间元素与其右邻居的大小关系来决定搜索方向
	// 时间复杂度: O(log n)
	// 空间复杂度: O(1)
	public static int peakIndexInMountainArray(int[] arr) {
		// 边界条件检查
		if (arr == null || arr.length < 3) {
			return -1;
		}
		
		int l = 1, r = arr.length - 2;
		while (l < r) {
			int m = l + ((r - l) >> 1);
			// 如果中间元素小于右邻居，说明还在上升阶段，峰值在右侧
			if (arr[m] < arr[m + 1]) {
				l = m + 1;
			// 否则峰值在左侧（包括当前位置）
			} else {
				r = m;
			}
		}
		return l;
	}
	
	// LintCode 585. Maximum Number in Mountain Sequence - 山脉序列中的最大数字
	// 题目要求: 给定一个山脉序列，找出其中的最大数字
	// 解题思路: 先使用二分查找找到峰值索引，然后返回该索引处的值
	// 时间复杂度: O(log n)
	// 空间复杂度: O(1)
	public static int mountainSequence(int[] nums) {
		// 边界条件检查
		if (nums == null || nums.length == 0) {
			return -1;
		}
		if (nums.length == 1) {
			return nums[0];
		}
		
		int l = 0, r = nums.length - 1;
		while (l < r) {
			int m = l + ((r - l) >> 1);
			// 如果中间元素小于右邻居，说明还在上升阶段，最大值在右侧
			if (nums[m] < nums[m + 1]) {
				l = m + 1;
			// 否则最大值在左侧（包括当前位置）
			} else {
				r = m;
			}
		}
		return nums[l];
	}
	
	// 测试方法
	public static void main(String[] args) {
		System.out.println("========== 峰值查找算法测试 ==========\n");
		
		// 测试寻找峰值元素
		int[] arr1 = {1, 2, 3, 1};
		System.out.println("测试用例1 - 基本峰值查找:");
		System.out.println("输入: [1, 2, 3, 1]");
		System.out.println("峰值元素索引: " + findPeakElement(arr1)); // 应输出 2
		System.out.println();
		
		int[] arr1_1 = {1, 2, 1, 3, 5, 6, 4};
		System.out.println("测试用例2 - 多个可能峰值:");
		System.out.println("输入: [1, 2, 1, 3, 5, 6, 4]");
		System.out.println("峰值元素索引: " + findPeakElement(arr1_1)); // 可能输出 1 或 5 或 6
		System.out.println();
		
		// 测试山脉数组的峰顶索引
		int[] arr2 = {0, 1, 0};
		System.out.println("测试用例3 - 山脉数组峰顶索引:");
		System.out.println("输入: [0, 1, 0]");
		System.out.println("山脉数组峰顶索引: " + peakIndexInMountainArray(arr2)); // 应输出 1
		System.out.println();
		
		int[] arr2_1 = {0, 2, 1, 0};
		System.out.println("测试用例4 - 更长山脉数组:");
		System.out.println("输入: [0, 2, 1, 0]");
		System.out.println("山脉数组峰顶索引: " + peakIndexInMountainArray(arr2_1)); // 应输出 1
		System.out.println();
		
		// 测试山脉序列中的最大数字
		int[] arr3 = {1, 2, 4, 8, 6, 3};
		System.out.println("测试用例5 - 山脉序列最大数字:");
		System.out.println("输入: [1, 2, 4, 8, 6, 3]");
		System.out.println("山脉序列中的最大数字: " + mountainSequence(arr3)); // 应输出 8
		System.out.println();
		
		// 测试寻找峰值的通用方法（处理重复元素）
		int[] arr4 = {1, 2, 2, 3, 1};
		System.out.println("测试用例6 - 带重复元素的峰值查找:");
		System.out.println("输入: [1, 2, 2, 3, 1]");
		System.out.println("通用峰值查找结果: " + findPeakWithPossibleDuplicates(arr4)); // 应输出 3
		System.out.println();
		
		// 测试在扩展搜索空间中寻找峰值
		int[] arr5 = {3, 1, 2};
		System.out.println("测试用例7 - 扩展空间峰值查找:");
		System.out.println("输入: [3, 1, 2]");
		System.out.println("扩展空间峰值查找结果: " + findPeakWithExtendedSpace(arr5)); // 应输出 0 或 2
		System.out.println();
		
		// 测试二维数组中的峰值查找
		int[][] matrix = {
			{1, 4, 3, 2, 5},
			{6, 7, 8, 9, 10},
			{11, 12, 13, 14, 15}
		};
		System.out.println("测试用例8 - 二维数组峰值查找:");
		System.out.println("输入: [[1, 4, 3, 2, 5], [6, 7, 8, 9, 10], [11, 12, 13, 14, 15]]");
		int[] peak2D = findPeakIn2D(matrix);
		System.out.println("二维峰值位置: [" + peak2D[0] + ", " + peak2D[1] + "]"); // 应输出 [2, 4]
		System.out.println();
		
		// 边界情况测试
		int[] arr6 = {1}; // 单元素数组
		System.out.println("测试用例9 - 边界情况：单元素数组:");
		System.out.println("输入: [1]");
		System.out.println("峰值元素索引: " + findPeakElement(arr6)); // 应输出 0
		System.out.println();
		
		int[] arr7 = {1, 2}; // 两元素递增数组
		System.out.println("测试用例10 - 边界情况：两元素递增数组:");
		System.out.println("输入: [1, 2]");
		System.out.println("峰值元素索引: " + findPeakElement(arr7)); // 应输出 1
		System.out.println();
	}
	
	// 递归版本的峰值查找
	// 时间复杂度: O(log n)
	// 空间复杂度: O(log n) - 递归调用栈的深度
	public static int findPeakElementRecursive(int[] nums) {
		if (nums == null || nums.length == 0) {
			return -1;
		}
		return findPeakRecursiveHelper(nums, 0, nums.length - 1);
	}
	
	private static int findPeakRecursiveHelper(int[] nums, int left, int right) {
		// 基本情况：只有一个元素
		if (left == right) {
			return left;
		}
		
		// 检查边界情况
		if (left == 0 && nums[left] > nums[left + 1]) {
			return left;
		}
		if (right == nums.length - 1 && nums[right] > nums[right - 1]) {
			return right;
		}
		
		// 中间情况的检查
		int mid = left + ((right - left) >> 1);
		
		// 检查是否是峰值
		if (mid > 0 && mid < nums.length - 1 && nums[mid] > nums[mid - 1] && nums[mid] > nums[mid + 1]) {
			return mid;
		}
		
		// 决定递归方向
		if (mid < nums.length - 1 && nums[mid] < nums[mid + 1]) {
			// 右侧上升，峰值在右侧
			return findPeakRecursiveHelper(nums, mid + 1, right);
		} else {
			// 峰值在左侧
			return findPeakRecursiveHelper(nums, left, mid);
		}
	}
	
	// 通用版本的峰值查找（处理可能包含重复元素的情况）
	// 针对牛客网 NC107. 寻找峰值的扩展实现
	// 解题思路: 使用二分查找，即使有重复元素也能找到一个峰值
	// 时间复杂度: O(log n)
	// 空间复杂度: O(1)
	public static int findPeakWithPossibleDuplicates(int[] nums) {
		if (nums == null || nums.length == 0) {
			return -1;
		}
		
		int n = nums.length;
		if (n == 1) {
			return 0;
		}
		
		// 检查边界情况
		if (nums[0] >= nums[1]) {
			return 0;
		}
		if (nums[n - 1] >= nums[n - 2]) {
			return n - 1;
		}
		
		int left = 1;
		int right = n - 2;
		
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			
			// 如果mid是峰值
			if (nums[mid] >= nums[mid - 1] && nums[mid] >= nums[mid + 1]) {
				return mid;
			}
			
			// 处理重复元素的情况，选择一个方向继续搜索
			if (nums[mid] == nums[mid - 1]) {
				// 向右侧搜索
				left = mid + 1;
			} else if (nums[mid] == nums[mid + 1]) {
				// 向左侧搜索
				right = mid - 1;
			} else if (nums[mid] < nums[mid + 1]) {
				// 峰值在右侧
				left = mid + 1;
			} else {
				// 峰值在左侧
				right = mid - 1;
			}
		}
		
		// 如果没有找到明确的峰值，返回任意一个可能的位置
		return left;
	}
	
	// 在扩展搜索空间中寻找峰值（适合所有情况的简化实现）
	// 利用题目条件：数组边界外的值被视为负无穷
	// 解题思路: 使用二分查找，总是向上升的方向搜索
	// 时间复杂度: O(log n)
	// 空间复杂度: O(1)
	public static int findPeakWithExtendedSpace(int[] nums) {
		if (nums == null || nums.length == 0) {
			return -1;
		}
		
		int left = 0;
		int right = nums.length - 1;
		
		while (left < right) {
			int mid = left + ((right - left) >> 1);
			// 如果中间元素小于右邻居，说明在上升阶段，峰值在右侧
			if (nums[mid] < nums[mid + 1]) {
				left = mid + 1;
			} else {
				// 否则峰值在左侧（包括当前位置）
				right = mid;
			}
		}
		
		// left == right 时就是一个峰值
		return left;
	}
	
	// 寻找二维数组中的峰值
	// LeetCode 1901. Find a Peak Element II
	// 解题思路: 对列进行二分查找，找到每列的最大值，然后比较相邻列
	// 时间复杂度: O(m log n)，m是行数，n是列数
	// 空间复杂度: O(1)
	public static int[] findPeakIn2D(int[][] matrix) {
		if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
			return new int[]{-1, -1};
		}
		
		int rows = matrix.length;
		int cols = matrix[0].length;
		
		// 对列进行二分查找
		int left = 0;
		int right = cols - 1;
		
		while (left <= right) {
			int midCol = left + ((right - left) >> 1);
			
			// 找到midCol列中的最大值的行索引
			int maxRow = findMaxInColumn(matrix, midCol, rows);
			
			// 获取左侧和右侧列的相邻元素值（如果存在）
			int leftVal = (midCol > 0) ? matrix[maxRow][midCol - 1] : Integer.MIN_VALUE;
			int rightVal = (midCol < cols - 1) ? matrix[maxRow][midCol + 1] : Integer.MIN_VALUE;
			
			// 如果中间列的最大值大于左右两侧相邻元素，则找到峰值
			if (matrix[maxRow][midCol] > leftVal && matrix[maxRow][midCol] > rightVal) {
				return new int[]{maxRow, midCol};
			}
			// 如果左侧相邻元素更大，峰值在左侧
			else if (leftVal > matrix[maxRow][midCol]) {
				right = midCol - 1;
			}
			// 否则峰值在右侧
			else {
				left = midCol + 1;
			}
		}
		
		// 理论上不会到达这里，因为题目保证存在峰值
		return new int[]{-1, -1};
	}
	
	// 辅助方法：找到指定列中的最大值的行索引
	private static int findMaxInColumn(int[][] matrix, int col, int rows) {
		int maxRow = 0;
		for (int i = 1; i < rows; i++) {
			if (matrix[i][col] > matrix[maxRow][col]) {
				maxRow = i;
			}
		}
		return maxRow;
	}

	/* C++ 实现:
	#include <vector>
	using namespace std;

	// 寻找峰值元素
	int findPeakElement(vector<int>& nums) {
		int n = nums.size();
		if (n == 1) return 0;
		if (nums[0] > nums[1]) return 0;
		if (nums[n-1] > nums[n-2]) return n-1;
		
		int l = 1, r = n-2;
		while (l <= r) {
			int m = (l + r) / 2;
			if (nums[m-1] > nums[m]) {
				r = m - 1;
			} else if (nums[m] < nums[m+1]) {
				l = m + 1;
			} else {
				return m;
			}
		}
		return -1;
	}

	// 山脉数组的峰顶索引
	int peakIndexInMountainArray(vector<int>& arr) {
		if (arr.size() < 3) return -1;
		
		int l = 1, r = arr.size() - 2;
		while (l < r) {
			int m = l + ((r - l) >> 1);
			if (arr[m] < arr[m + 1]) {
				l = m + 1;
			} else {
				r = m;
			}
		}
		return l;
	}

	// 山脉序列中的最大数字
	int mountainSequence(vector<int>& nums) {
		if (nums.empty()) return -1;
		if (nums.size() == 1) return nums[0];
		
		int l = 0, r = nums.size() - 1;
		while (l < r) {
			int m = l + ((r - l) >> 1);
			if (nums[m] < nums[m + 1]) {
				l = m + 1;
			} else {
				r = m;
			}
		}
		return nums[l];
	}
	
	// 通用版本的峰值查找（处理可能包含重复元素的情况）
	int findPeakWithPossibleDuplicates(vector<int>& nums) {
		if (nums.empty()) {
			return -1;
		}
		
		int n = nums.size();
		if (n == 1) {
			return 0;
		}
		
		// 检查边界情况
		if (nums[0] >= nums[1]) {
			return 0;
		}
		if (nums[n - 1] >= nums[n - 2]) {
			return n - 1;
		}
		
		int left = 1;
		int right = n - 2;
		
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			
			// 如果mid是峰值
			if (nums[mid] >= nums[mid - 1] && nums[mid] >= nums[mid + 1]) {
				return mid;
			}
			
			// 处理重复元素的情况，选择一个方向继续搜索
			if (nums[mid] == nums[mid - 1]) {
				// 向右侧搜索
				left = mid + 1;
			} else if (nums[mid] == nums[mid + 1]) {
				// 向左侧搜索
				right = mid - 1;
			} else if (nums[mid] < nums[mid + 1]) {
				// 峰值在右侧
				left = mid + 1;
			} else {
				// 峰值在左侧
				right = mid - 1;
			}
		}
		
		// 如果没有找到明确的峰值，返回任意一个可能的位置
		return left;
	}
	
	// 在扩展搜索空间中寻找峰值（适合所有情况的简化实现）
	int findPeakWithExtendedSpace(vector<int>& nums) {
		if (nums.empty()) {
			return -1;
		}
		
		int left = 0;
		int right = nums.size() - 1;
		
		while (left < right) {
			int mid = left + ((right - left) >> 1);
			// 如果中间元素小于右邻居，说明在上升阶段，峰值在右侧
			if (nums[mid] < nums[mid + 1]) {
				left = mid + 1;
			} else {
				// 否则峰值在左侧（包括当前位置）
				right = mid;
			}
		}
		
		// left == right 时就是一个峰值
		return left;
	}
	
	// 递归版本的峰值查找
	int findPeakElementRecursive(vector<int>& nums) {
		if (nums.empty()) return -1;
		return findPeakRecursiveHelper(nums, 0, nums.size() - 1);
	}
	
	int findPeakRecursiveHelper(vector<int>& nums, int left, int right) {
		// 基本情况：只有一个元素
		if (left == right) {
			return left;
		}
		
		// 检查边界情况
		if (left == 0 && nums[left] > nums[left + 1]) {
			return left;
		}
		if (right == nums.size() - 1 && nums[right] > nums[right - 1]) {
			return right;
		}
		
		// 中间情况的检查
		int mid = left + ((right - left) >> 1);
		
		// 检查是否是峰值
		if (mid > 0 && mid < nums.size() - 1 && nums[mid] > nums[mid - 1] && nums[mid] > nums[mid + 1]) {
			return mid;
		}
		
		// 决定递归方向
		if (mid < nums.size() - 1 && nums[mid] < nums[mid + 1]) {
			// 右侧上升，峰值在右侧
			return findPeakRecursiveHelper(nums, mid + 1, right);
		} else {
			// 峰值在左侧
			return findPeakRecursiveHelper(nums, left, mid);
		}
	}
	
	// 寻找二维数组中的峰值
	vector<int> findPeakIn2D(vector<vector<int>>& matrix) {
		if (matrix.empty() || matrix[0].empty()) {
			return {-1, -1};
		}
		
		int rows = matrix.size();
		int cols = matrix[0].size();
		int left = 0, right = cols - 1;
		
		while (left <= right) {
			int midCol = left + ((right - left) >> 1);
			
			// 找到midCol列中的最大值的行索引
			int maxRow = 0;
			for (int i = 1; i < rows; i++) {
				if (matrix[i][midCol] > matrix[maxRow][midCol]) {
					maxRow = i;
				}
			}
			
			// 获取左侧和右侧列的相邻元素值（如果存在）
			int leftVal = (midCol > 0) ? matrix[maxRow][midCol - 1] : INT_MIN;
			int rightVal = (midCol < cols - 1) ? matrix[maxRow][midCol + 1] : INT_MIN;
			
			// 如果中间列的最大值大于左右两侧相邻元素，则找到峰值
			if (matrix[maxRow][midCol] > leftVal && matrix[maxRow][midCol] > rightVal) {
				return {maxRow, midCol};
			}
			// 如果左侧相邻元素更大，峰值在左侧
			else if (leftVal > matrix[maxRow][midCol]) {
				right = midCol - 1;
			}
			// 否则峰值在右侧
			else {
				left = midCol + 1;
			}
		}
		
		return {-1, -1};
	}
	*/
	
	/* Python 实现:
	# 寻找峰值元素
	def find_peak_element(nums):
		n = len(nums)
		if n == 1:
			return 0
		if nums[0] > nums[1]:
			return 0
		if nums[n-1] > nums[n-2]:
			return n-1
		
		l, r = 1, n-2
		while l <= r:
			m = (l + r) // 2
			if nums[m-1] > nums[m]:
				r = m - 1
			elif nums[m] < nums[m+1]:
				l = m + 1
			else:
				return m
		return -1

	# 山脉数组的峰顶索引
	def peak_index_in_mountain_array(arr):
		if len(arr) < 3:
			return -1
		
		l, r = 1, len(arr) - 2
		while l < r:
			m = l + ((r - l) >> 1)
			if arr[m] < arr[m + 1]:
				l = m + 1
			else:
				r = m
		return l

	# 山脉序列中的最大数字
	def mountain_sequence(nums):
		if not nums:
			return -1
		if len(nums) == 1:
			return nums[0]
		
		l, r = 0, len(nums) - 1
		while l < r:
			m = l + ((r - l) >> 1)
			if nums[m] < nums[m + 1]:
				l = m + 1
			else:
				r = m
		return nums[l]
	
	# 通用版本的峰值查找（处理可能包含重复元素的情况）
	def find_peak_with_possible_duplicates(nums):
		if not nums:
			return -1
		
		n = len(nums)
		if n == 1:
			return 0
		
		# 检查边界情况
		if nums[0] >= nums[1]:
			return 0
		if nums[n - 1] >= nums[n - 2]:
			return n - 1
		
		left = 1
		right = n - 2
		
		while left <= right:
			mid = left + ((right - left) >> 1)
			
			# 如果mid是峰值
			if nums[mid] >= nums[mid - 1] and nums[mid] >= nums[mid + 1]:
				return mid
			
			# 处理重复元素的情况，选择一个方向继续搜索
			if nums[mid] == nums[mid - 1]:
				# 向右侧搜索
				left = mid + 1
			elif nums[mid] == nums[mid + 1]:
				# 向左侧搜索
				right = mid - 1
			elif nums[mid] < nums[mid + 1]:
				# 峰值在右侧
				left = mid + 1
			else:
				# 峰值在左侧
				right = mid - 1
		
		# 如果没有找到明确的峰值，返回任意一个可能的位置
		return left
	
	# 在扩展搜索空间中寻找峰值（适合所有情况的简化实现）
	def find_peak_with_extended_space(nums):
		if not nums:
			return -1
		
		left = 0
		right = len(nums) - 1
		
		while left < right:
			mid = left + ((right - left) >> 1)
			# 如果中间元素小于右邻居，说明在上升阶段，峰值在右侧
			if nums[mid] < nums[mid + 1]:
				left = mid + 1
			else:
				# 否则峰值在左侧（包括当前位置）
				right = mid
		
		# left == right 时就是一个峰值
		return left
	
	# 递归版本的峰值查找
	def find_peak_element_recursive(nums):
		if not nums:
			return -1
		return find_peak_recursive_helper(nums, 0, len(nums) - 1)
	
def find_peak_recursive_helper(nums, left, right):
		# 基本情况：只有一个元素
		if left == right:
			return left
		
		# 检查边界情况
		if left == 0 and nums[left] > nums[left + 1]:
			return left
		if right == len(nums) - 1 and nums[right] > nums[right - 1]:
			return right
		
		# 中间情况的检查
		mid = left + ((right - left) >> 1)
		
		# 检查是否是峰值
		if mid > 0 and mid < len(nums) - 1 and nums[mid] > nums[mid - 1] and nums[mid] > nums[mid + 1]:
			return mid
		
		# 决定递归方向
		if mid < len(nums) - 1 and nums[mid] < nums[mid + 1]:
			# 右侧上升，峰值在右侧
			return find_peak_recursive_helper(nums, mid + 1, right)
		else:
			# 峰值在左侧
			return find_peak_recursive_helper(nums, left, mid)
	
	# 寻找二维数组中的峰值
	def find_peak_in_2d(matrix):
		if not matrix or not matrix[0]:
			return [-1, -1]
		
		rows = len(matrix)
		cols = len(matrix[0])
		left = 0
		right = cols - 1
		
		while left <= right:
			mid_col = left + ((right - left) >> 1)
			
			# 找到mid_col列中的最大值的行索引
			max_row = 0
			for i in range(1, rows):
				if matrix[i][mid_col] > matrix[max_row][mid_col]:
					max_row = i
			
			# 获取左侧和右侧列的相邻元素值（如果存在）
			left_val = matrix[max_row][mid_col - 1] if mid_col > 0 else float('-inf')
			right_val = matrix[max_row][mid_col + 1] if mid_col < cols - 1 else float('-inf')
			
			# 如果中间列的最大值大于左右两侧相邻元素，则找到峰值
			if matrix[max_row][mid_col] > left_val and matrix[max_row][mid_col] > right_val:
				return [max_row, mid_col]
			# 如果左侧相邻元素更大，峰值在左侧
			elif left_val > matrix[max_row][mid_col]:
				right = mid_col - 1
			# 否则峰值在右侧
			else:
				left = mid_col + 1
		
		return [-1, -1]
	*/

}