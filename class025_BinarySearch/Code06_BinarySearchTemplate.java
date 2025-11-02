package class006;

// 二分查找算法的通用模板和优化技巧
// 本文件总结了二分查找的各种模板、优化方法以及常见问题的解决方案
// 相关题目:
// 1. LeetCode 704. 二分查找
// 2. LeetCode 34. 在排序数组中查找元素的第一个和最后一个位置
// 3. LeetCode 33. 搜索旋转排序数组
// 4. LeetCode 153. 寻找旋转排序数组中的最小值
// 5. LeetCode 167. 两数之和 II - 输入有序数组
// 6. LeetCode 287. 寻找重复数
public class Code06_BinarySearchTemplate {

	// 模板一：标准二分查找（查找精确值）
	// 适用场景：查找某个特定值是否存在于有序数组中
	// 时间复杂度: O(log n)
	// 空间复杂度: O(1)
	public static int binarySearchStandard(int[] nums, int target) {
		if (nums == null || nums.length == 0) {
			return -1;
		}
		
		int left = 0;
		int right = nums.length - 1;
		
		while (left <= right) {
			// 避免整数溢出
			int mid = left + ((right - left) >> 1);
			
			if (nums[mid] == target) {
				return mid; // 找到目标值
			} else if (nums[mid] < target) {
				left = mid + 1; // 目标值在右半部分
			} else {
				right = mid - 1; // 目标值在左半部分
			}
		}
		
		return -1; // 未找到目标值
	}
	
	// 模板二：查找左边界（第一个大于等于target的元素位置）
	// 适用场景：
	// 1. 查找第一个出现的目标值
	// 2. 查找插入位置
	// 3. 查找下界
	// 时间复杂度: O(log n)
	// 空间复杂度: O(1)
	public static int binarySearchLeftBound(int[] nums, int target) {
		if (nums == null || nums.length == 0) {
			return 0;
		}
		
		int left = 0;
		int right = nums.length; // 注意这里是nums.length而不是nums.length-1
		
		while (left < right) {
			int mid = left + ((right - left) >> 1);
			
			if (nums[mid] < target) {
				left = mid + 1;
			} else {
				right = mid; // 不立即返回，继续向左查找
			}
		}
		
		// left == right，即为左边界
		return left;
	}
	
	// 模板三：查找右边界（最后一个小于等于target的元素位置）
	// 适用场景：
	// 1. 查找最后一个出现的目标值
	// 2. 查找上界
	// 时间复杂度: O(log n)
	// 空间复杂度: O(1)
	public static int binarySearchRightBound(int[] nums, int target) {
		if (nums == null || nums.length == 0) {
			return -1;
		}
		
		int left = 0;
		int right = nums.length - 1;
		int ans = -1; // 初始化为-1，表示未找到
		
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			
			if (nums[mid] <= target) {
				ans = mid; // 更新答案，但继续向右查找
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		
		return ans;
	}
	
	// 模板四：寻找旋转排序数组中的最小值（无重复元素）
	// 时间复杂度: O(log n)
	// 空间复杂度: O(1)
	public static int findMinInRotatedArray(int[] nums) {
		if (nums == null || nums.length == 0) {
			throw new IllegalArgumentException("Input array cannot be empty");
		}
		
		int left = 0;
		int right = nums.length - 1;
		
		// 如果数组没有旋转，直接返回第一个元素
		if (nums[left] < nums[right]) {
			return nums[left];
		}
		
		while (left < right) {
			int mid = left + ((right - left) >> 1);
			
			if (nums[mid] > nums[right]) {
				// 最小值在右半部分
				left = mid + 1;
			} else {
				// 最小值在左半部分（包括mid）
				right = mid;
			}
		}
		
		return nums[left];
	}
	
	// 模板五：在旋转排序数组中搜索（无重复元素）
	// 时间复杂度: O(log n)
	// 空间复杂度: O(1)
	public static int searchInRotatedArray(int[] nums, int target) {
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
				if (nums[left] <= target && target < nums[mid]) {
					// 目标值在左半部分
					right = mid - 1;
				} else {
					// 目标值在右半部分
					left = mid + 1;
				}
			} else {
				// 右半部分有序
				if (nums[mid] < target && target <= nums[right]) {
					// 目标值在右半部分
					left = mid + 1;
				} else {
					// 目标值在左半部分
					right = mid - 1;
				}
			}
		}
		
		return -1;
	}
	
	// 模板六：二分查找的两数之和（针对有序数组）
	// 时间复杂度: O(n)
	// 空间复杂度: O(1)
	// 注意：虽然这里使用的是双指针而不是严格的二分查找，但是对于有序数组的两数之和问题，
	// 这是比二分查找更优的解法（每个元素只处理一次）
	public static int[] twoSumSorted(int[] nums, int target) {
		if (nums == null || nums.length < 2) {
			return new int[]{-1, -1};
		}
		
		int left = 0;
		int right = nums.length - 1;
		
		while (left < right) {
			int sum = nums[left] + nums[right];
			
			if (sum == target) {
				return new int[]{left + 1, right + 1}; // 题目要求返回1-indexed
			} else if (sum < target) {
				left++;
			} else {
				right--;
			}
		}
		
		return new int[]{-1, -1}; // 未找到
	}
	
	// 模板七：寻找重复数（Floyd's Tortoise and Hare算法）
	// 虽然不是严格的二分查找，但这是解决该问题的最优算法
	// 时间复杂度: O(n)
	// 空间复杂度: O(1)
	public static int findDuplicate(int[] nums) {
		if (nums == null || nums.length < 2) {
			throw new IllegalArgumentException("Input array must have at least 2 elements");
		}
		
		// 第一阶段：找到环中的一个点
		int tortoise = nums[0];
		int hare = nums[0];
		
		// 这里使用do-while循环是因为初始时tortoise和hare相等
		do {
			tortoise = nums[tortoise];
			hare = nums[nums[hare]];
		} while (tortoise != hare);
		
		// 第二阶段：找到环的入口点
		tortoise = nums[0];
		while (tortoise != hare) {
			tortoise = nums[tortoise];
			hare = nums[hare];
		}
		
		return hare;
	}
	
	// 模板八：二分查找的性能优化版本
	// 1. 避免使用乘法和除法运算
	// 2. 使用位运算优化
	// 3. 预计算边界条件
	// 时间复杂度: O(log n)
	// 空间复杂度: O(1)
	public static int binarySearchOptimized(int[] nums, int target) {
		// 边界条件快速处理
		if (nums == null || nums.length == 0) {
			return -1;
		}
		
		int n = nums.length;
		// 快速检查目标值是否在数组范围内
		if (target < nums[0] || target > nums[n - 1]) {
			return -1;
		}
		
		int left = 0;
		int right = n - 1;
		
		while (left <= right) {
			// 使用位运算计算中间索引，避免乘法溢出
			int mid = left + ((right - left) >> 1);
			
			// 快速路径：检查中间元素
			if (nums[mid] == target) {
				return mid;
			}
			
			// 减少比较次数的优化：先比较目标值与中间值的大小关系
			boolean isRight = (target > nums[mid]);
			if (isRight) {
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		
		return -1;
	}
	
	// 二分查找的优化技巧总结：
	// 1. 避免整数溢出：使用 mid = left + ((right - left) >> 1) 而不是 mid = (left + right) / 2
	// 2. 位运算优化：使用 >> 1 代替 / 2，使用 & 1 代替 % 2
	// 3. 边界条件优化：在进入循环前先检查边界条件，避免不必要的循环
	// 4. 分支预测优化：尽量保持分支的一致性，避免频繁的条件跳转
	// 5. 内存访问优化：连续访问数组元素，提高缓存命中率
	
	// 测试方法
	public static void main(String[] args) {
		System.out.println("========== 二分查找模板测试 ==========\n");
		
		// 测试标准二分查找
		int[] nums1 = {-1, 0, 3, 5, 9, 12};
		System.out.println("测试标准二分查找:");
		System.out.println("输入数组: " + java.util.Arrays.toString(nums1));
		System.out.println("查找9: " + binarySearchStandard(nums1, 9)); // 应输出 4
		System.out.println("查找2: " + binarySearchStandard(nums1, 2)); // 应输出 -1
		System.out.println();
		
		// 测试左边界查找
		int[] nums2 = {1, 2, 2, 2, 3, 4};
		System.out.println("测试左边界查找:");
		System.out.println("输入数组: " + java.util.Arrays.toString(nums2));
		System.out.println("查找2的左边界: " + binarySearchLeftBound(nums2, 2)); // 应输出 1
		System.out.println();
		
		// 测试右边界查找
		System.out.println("测试右边界查找:");
		System.out.println("查找2的右边界: " + binarySearchRightBound(nums2, 2)); // 应输出 3
		System.out.println();
		
		// 测试旋转排序数组中的最小值
		int[] nums3 = {4, 5, 6, 7, 0, 1, 2};
		System.out.println("测试旋转排序数组中的最小值:");
		System.out.println("输入数组: " + java.util.Arrays.toString(nums3));
		System.out.println("最小值: " + findMinInRotatedArray(nums3)); // 应输出 0
		System.out.println();
		
		// 测试旋转排序数组中的搜索
		System.out.println("测试旋转排序数组中的搜索:");
		System.out.println("查找0: " + searchInRotatedArray(nums3, 0)); // 应输出 4
		System.out.println("查找3: " + searchInRotatedArray(nums3, 3)); // 应输出 -1
		System.out.println();
		
		// 测试两数之和
		int[] nums4 = {2, 7, 11, 15};
		System.out.println("测试两数之和:");
		System.out.println("输入数组: " + java.util.Arrays.toString(nums4));
		int[] result = twoSumSorted(nums4, 9);
		System.out.println("和为9的两个数索引: [" + result[0] + ", " + result[1] + "]"); // 应输出 [1, 2]
		System.out.println();
		
		// 测试寻找重复数
		int[] nums5 = {1, 3, 4, 2, 2};
		System.out.println("测试寻找重复数:");
		System.out.println("输入数组: " + java.util.Arrays.toString(nums5));
		System.out.println("重复数: " + findDuplicate(nums5)); // 应输出 2
		System.out.println();
		
		// 测试优化版本的二分查找
		System.out.println("测试优化版本的二分查找:");
		System.out.println("优化版查找9: " + binarySearchOptimized(nums1, 9)); // 应输出 4
		System.out.println();
		
		// 新增测试：边界情况和异常处理
		System.out.println("========== 边界情况测试 ==========\n");
		
		// 测试空数组
		int[] empty = {};
		System.out.println("测试空数组:");
		try {
			System.out.println("空数组查找: " + binarySearchStandard(empty, 5));
		} catch (Exception e) {
			System.out.println("异常处理: " + e.getMessage());
		}
		System.out.println();
		
		// 测试单元素数组
		int[] single = {5};
		System.out.println("测试单元素数组:");
		System.out.println("单元素数组查找5: " + binarySearchStandard(single, 5)); // 应输出 0
		System.out.println("单元素数组查找3: " + binarySearchStandard(single, 3)); // 应输出 -1
		System.out.println();
		
		// 测试大规模数据
		System.out.println("测试大规模数据:");
		int[] largeArray = new int[1000000];
		for (int i = 0; i < largeArray.length; i++) {
			largeArray[i] = i * 2;
		}
		long startTime = System.currentTimeMillis();
		int largeResult = binarySearchOptimized(largeArray, 500000);
		long endTime = System.currentTimeMillis();
		System.out.println("大规模数据查找耗时: " + (endTime - startTime) + "ms");
		System.out.println("查找结果: " + largeResult); // 应输出 250000
		System.out.println();
		
		System.out.println("========== 所有测试完成 ==========");
	}

	/* C++ 实现:
	#include <vector>
	#include <iostream>
	using namespace std;

	// 标准二分查找
	int binarySearchStandard(vector<int>& nums, int target) {
		if (nums.empty()) return -1;
		
		int left = 0;
		int right = nums.size() - 1;
		
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
		
		return -1;
	}
	
	// 查找左边界
	int binarySearchLeftBound(vector<int>& nums, int target) {
		if (nums.empty()) return 0;
		
		int left = 0;
		int right = nums.size();
		
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
	
	// 查找右边界
	int binarySearchRightBound(vector<int>& nums, int target) {
		if (nums.empty()) return -1;
		
		int left = 0;
		int right = nums.size() - 1;
		int ans = -1;
		
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			
			if (nums[mid] <= target) {
				ans = mid;
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		
		return ans;
	}
	
	// 寻找旋转排序数组中的最小值
	int findMinInRotatedArray(vector<int>& nums) {
		if (nums.empty()) {
			throw invalid_argument("Input array cannot be empty");
		}
		
		int left = 0;
		int right = nums.size() - 1;
		
		if (nums[left] < nums[right]) {
			return nums[left];
		}
		
		while (left < right) {
			int mid = left + ((right - left) >> 1);
			
			if (nums[mid] > nums[right]) {
				left = mid + 1;
			} else {
				right = mid;
			}
		}
		
		return nums[left];
	}
	
	// 在旋转排序数组中搜索
	int searchInRotatedArray(vector<int>& nums, int target) {
		if (nums.empty()) return -1;
		
		int left = 0;
		int right = nums.size() - 1;
		
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			
			if (nums[mid] == target) {
				return mid;
			}
			
			if (nums[left] <= nums[mid]) {
				if (nums[left] <= target && target < nums[mid]) {
					right = mid - 1;
				} else {
					left = mid + 1;
				}
			} else {
				if (nums[mid] < target && target <= nums[right]) {
					left = mid + 1;
				} else {
					right = mid - 1;
				}
			}
		}
		
		return -1;
	}
	
	// 二分查找的两数之和
	vector<int> twoSumSorted(vector<int>& nums, int target) {
		if (nums.size() < 2) {
			return {-1, -1};
		}
		
		int left = 0;
		int right = nums.size() - 1;
		
		while (left < right) {
			int sum = nums[left] + nums[right];
			
			if (sum == target) {
				return {left + 1, right + 1};
			} else if (sum < target) {
				left++;
			} else {
				right--;
			}
		}
		
		return {-1, -1};
	}
	
	// 寻找重复数
	int findDuplicate(vector<int>& nums) {
		if (nums.size() < 2) {
			throw invalid_argument("Input array must have at least 2 elements");
		}
		
		int tortoise = nums[0];
		int hare = nums[0];
		
		do {
			tortoise = nums[tortoise];
			hare = nums[nums[hare]];
		} while (tortoise != hare);
		
		tortoise = nums[0];
		while (tortoise != hare) {
			tortoise = nums[tortoise];
			hare = nums[hare];
		}
		
		return hare;
	}
	
	// 优化版本的二分查找
	int binarySearchOptimized(vector<int>& nums, int target) {
		if (nums.empty()) return -1;
		
		int n = nums.size();
		if (target < nums[0] || target > nums[n - 1]) {
			return -1;
		}
		
		int left = 0;
		int right = n - 1;
		
		while (left <= right) {
			int mid = left + ((right - left) >> 1);
			
			if (nums[mid] == target) {
				return mid;
			}
			
			bool isRight = (target > nums[mid]);
			if (isRight) {
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		
		return -1;
	}
	*/

	/* Python 实现:
	# 标准二分查找
	def binary_search_standard(nums, target):
		if not nums:
			return -1
		
		left = 0
		right = len(nums) - 1
		
		while left <= right:
			mid = left + ((right - left) >> 1)
			
			if nums[mid] == target:
				return mid
			elif nums[mid] < target:
				left = mid + 1
			else:
				right = mid - 1
		
		return -1
	
	# 查找左边界
	def binary_search_left_bound(nums, target):
		if not nums:
			return 0
		
		left = 0
		right = len(nums)
		
		while left < right:
			mid = left + ((right - left) >> 1)
			
			if nums[mid] < target:
				left = mid + 1
			else:
				right = mid
		
		return left
	
	# 查找右边界
	def binary_search_right_bound(nums, target):
		if not nums:
			return -1
		
		left = 0
		right = len(nums) - 1
		ans = -1
		
		while left <= right:
			mid = left + ((right - left) >> 1)
			
			if nums[mid] <= target:
				ans = mid
				left = mid + 1
			else:
				right = mid - 1
		
		return ans
	
	# 寻找旋转排序数组中的最小值
	def find_min_in_rotated_array(nums):
		if not nums:
			raise ValueError("Input array cannot be empty")
		
		left = 0
		right = len(nums) - 1
		
		if nums[left] < nums[right]:
			return nums[left]
		
		while left < right:
			mid = left + ((right - left) >> 1)
			
			if nums[mid] > nums[right]:
				left = mid + 1
			else:
				right = mid
		
		return nums[left]
	
	# 在旋转排序数组中搜索
	def search_in_rotated_array(nums, target):
		if not nums:
			return -1
		
		left = 0
		right = len(nums) - 1
		
		while left <= right:
			mid = left + ((right - left) >> 1)
			
			if nums[mid] == target:
				return mid
			
			if nums[left] <= nums[mid]:
				if nums[left] <= target and target < nums[mid]:
					right = mid - 1
				else:
					left = mid + 1
			else:
				if nums[mid] < target and target <= nums[right]:
					left = mid + 1
				else:
					right = mid - 1
		
		return -1
	
	# 二分查找的两数之和
	def two_sum_sorted(nums, target):
		if len(nums) < 2:
			return [-1, -1]
		
		left = 0
		right = len(nums) - 1
		
		while left < right:
			sum_ = nums[left] + nums[right]
			
			if sum_ == target:
				return [left + 1, right + 1]
			elif sum_ < target:
				left += 1
			else:
				right -= 1
		
		return [-1, -1]
	
	# 寻找重复数
	def find_duplicate(nums):
		if len(nums) < 2:
			raise ValueError("Input array must have at least 2 elements")
		
		tortoise = nums[0]
		hare = nums[0]
		
		# 找到环中的一个点
		while True:
			tortoise = nums[tortoise]
			hare = nums[nums[hare]]
			if tortoise == hare:
				break
		
		# 找到环的入口点
		tortoise = nums[0]
		while tortoise != hare:
			tortoise = nums[tortoise]
			hare = nums[hare]
		
		return hare
	
	# 优化版本的二分查找
	def binary_search_optimized(nums, target):
		if not nums:
			return -1
		
		n = len(nums)
		if target < nums[0] or target > nums[n - 1]:
			return -1
		
		left = 0
		right = n - 1
		
		while left <= right:
			mid = left + ((right - left) >> 1)
			
			if nums[mid] == target:
				return mid
			
			is_right = (target > nums[mid])
			if is_right:
				left = mid + 1
			else:
				right = mid - 1
		
		return -1
	*/
}