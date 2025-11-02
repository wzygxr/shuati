import java.util.*;

/**
 * 分治法求解数组最大值问题
 * 
 * 问题描述:
 * 给定一个数组，找出其中的最大值。
 * 
 * 解法思路:
 * 使用分治法，将数组不断二分，直到只有一个元素时直接返回，
 * 然后比较左右两部分的最大值，返回较大者。
 * 
 * 算法特点:
 * 1. 分治策略：将大问题分解为小问题
 * 2. 递归实现：通过递归不断分解问题
 * 3. 合并结果：比较子问题的解得到原问题的解
 * 
 * 时间复杂度分析:
 * T(n) = 2*T(n/2) + O(1)
 * 根据主定理，时间复杂度为 O(n)
 * 
 * 空间复杂度分析:
 * 递归调用栈的深度为 O(log n)
 * 空间复杂度为 O(log n)
 * 
 * 相关题目扩展:
 * 1. LeetCode 53. 最大子数组和 (分治解法)
 * 2. 求解数组中最大值和最小值
 * 3. 求解数组中第k大元素
 * 4. 分治法求解最大子矩阵和
 * 
 * 工程化考量:
 * 1. 异常处理：检查空数组情况
 * 2. 边界处理：处理只有一个元素的数组
 * 3. 性能优化：对于小规模数据可直接遍历
 * 4. 可配置性：可扩展为求解任意范围内的最值
 * 
 * 与标准库对比:
 * Java标准库中Collections.max()和Arrays.stream().max()等方法
 * 通常使用迭代而非递归，避免栈溢出风险
 * 
 * 语言特性差异:
 * Java: 使用Math.max()函数
 * C++: 使用std::max()函数
 * Python: 使用内置max()函数或自定义比较
 * 
 * 极端场景考虑:
 * 1. 空数组：需要特殊处理
 * 2. 单元素数组：直接返回
 * 3. 大规模数组：可能栈溢出，需改用迭代
 * 4. 所有元素相同：任一元素都是最大值
 * 
 * 调试技巧:
 * 1. 打印递归调用过程中的中间结果
 * 2. 使用断言验证左右子数组的最值正确性
 * 3. 性能测试：比较不同规模数据的执行时间
 * 
 * 分治法算法思想总结:
 * 分治法(Divide and Conquer)是一种重要的算法设计范式，通过将复杂问题分解为更小、更易解决的子问题，
 * 递归地解决这些子问题，然后将子问题的解合并起来形成原问题的解。
 * 
 * 分治法适合解决的问题特征:
 * 1. 问题可以被分解为若干个规模较小的相同或相似的子问题
 * 2. 子问题的解可以合并成原问题的解
 * 3. 子问题之间相互独立，不存在重叠子问题
 * 
 * 分治法的三个关键步骤:
 * 1. 分解(Divide): 将原问题分解为规模较小的子问题
 * 2. 解决(Conquer): 递归地解决子问题；对于足够小的子问题，直接求解
 * 3. 合并(Combine): 将子问题的解合并成原问题的解
 * 
 * 典型应用场景:
 * 1. 排序算法：归并排序、快速排序
 * 2. 搜索算法：二分查找
 * 3. 选择问题：第k大元素(快速选择)
 * 4. 矩阵运算：Strassen算法
 * 5. 字符串处理：大整数乘法
 * 6. 图论：最近点对问题
 * 7. 计算几何：凸包问题
 */
public class GetMaxValue {

	/**
	 * 入口方法，求数组中的最大值
	 * 
	 * @param arr 输入数组
	 * @return 数组中的最大值
	 * @throws IllegalArgumentException 如果数组为空
	 */
	public static int maxValue(int[] arr) {
		// 异常处理：检查数组是否为空
		if (arr == null || arr.length == 0) {
			throw new IllegalArgumentException("数组不能为空");
		}
		
		// 调用分治方法求解
		return f(arr, 0, arr.length - 1);
	}

	/**
	 * 分治法求解数组指定范围内的最大值
	 * 
	 * @param arr 数组
	 * @param l 左边界（包含）
	 * @param r 右边界（包含）
	 * @return 指定范围内的最大值
	 */
	public static int f(int[] arr, int l, int r) {
		// 基本情况：只有一个元素时直接返回
		if (l == r) {
			return arr[l];
		}
		
		// 分解：计算中点，将数组分为两部分
		int m = (l + r) / 2;
		
		// 递归求解：分别求左右两部分的最大值
		int lmax = f(arr, l, m);
		int rmax = f(arr, m + 1, r);
		
		// 合并：返回左右两部分最大值中的较大者
		return Math.max(lmax, rmax);
	}

	// ==================== 题目1：LeetCode 53. 最大子数组和 (分治解法) ====================
	/**
	 * 题目来源：LeetCode 53. Maximum Subarray
	 * 题目链接：https://leetcode.com/problems/maximum-subarray/
	 * 中文链接：https://leetcode.cn/problems/maximum-subarray/
	 * 
	 * 题目描述：
	 * 给定一个整数数组 nums，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
	 * 
	 * 示例 1：
	 * 输入：nums = [-2,1,-3,4,-1,2,1,-5,4]
	 * 输出：6
	 * 解释：连续子数组 [4,-1,2,1] 的和最大，为 6。
	 * 
	 * 示例 2：
	 * 输入：nums = [1]
	 * 输出：1
	 * 
	 * 示例 3：
	 * 输入：nums = [5,4,-1,7,8]
	 * 输出：23
	 * 
	 * 约束条件：
	 * 1 <= nums.length <= 10^5
	 * -10^4 <= nums[i] <= 10^4
	 * 
	 * 解题思路（分治法）：
	 * 1. 将数组从中间分为左右两部分
	 * 2. 最大子数组和可能出现在三个位置：
	 *    - 完全在左半部分
	 *    - 完全在右半部分
	 *    - 跨越中点（一部分在左，一部分在右）
	 * 3. 递归求解左右两部分的最大子数组和
	 * 4. 计算跨越中点的最大子数组和：
	 *    - 从中点向左扫描，找到最大和
	 *    - 从中点+1向右扫描，找到最大和
	 *    - 两者相加即为跨越中点的最大和
	 * 5. 返回三者中的最大值
	 * 
	 * 时间复杂度分析：
	 * T(n) = 2*T(n/2) + O(n)
	 * 根据主定理：a=2, b=2, f(n)=O(n)
	 * log_b(a) = log_2(2) = 1
	 * f(n) = Θ(n^1) = Θ(n^log_b(a))
	 * 因此 T(n) = Θ(n*log n)
	 * 
	 * 空间复杂度分析：
	 * 递归调用栈深度为 O(log n)
	 * 空间复杂度：O(log n)
	 * 
	 * 是否最优解：
	 * 分治法的时间复杂度为 O(n*log n)，空间复杂度为 O(log n)
	 * 最优解是动态规划（Kadane算法），时间复杂度 O(n)，空间复杂度 O(1)
	 * 分治法不是最优解，但展示了分治思想的应用
	 * 
	 * 最优解（Kadane算法）：
	 * - 维护两个变量：当前最大和(curSum)、全局最大和(maxSum)
	 * - 遍历数组，对每个元素：
	 *   curSum = max(nums[i], curSum + nums[i])
	 *   maxSum = max(maxSum, curSum)
	 * - 时间复杂度 O(n)，空间复杂度 O(1)
	 * 
	 * 调试技巧：
	 * 1. 打印每次递归的区间范围和返回值
	 * 2. 验证跨中点的左右扫描过程
	 * 3. 使用小数组手动推演过程
	 * 
	 * 边界场景：
	 * 1. 单元素数组：直接返回该元素
	 * 2. 全负数数组：返回最大的负数
	 * 3. 全正数数组：返回所有元素之和
	 * 4. 混合正负数：需要仔细计算
	 */
	public static int maxSubArray(int[] nums) {
		if (nums == null || nums.length == 0) {
			throw new IllegalArgumentException("数组不能为空");
		}
		return maxSubArrayDivide(nums, 0, nums.length - 1);
	}

	/**
	 * 分治法求解最大子数组和
	 * @param nums 数组
	 * @param left 左边界
	 * @param right 右边界
	 * @return 最大子数组和
	 */
	private static int maxSubArrayDivide(int[] nums, int left, int right) {
		// 基本情况：只有一个元素
		if (left == right) {
			return nums[left];
		}

		// 分解：计算中点
		int mid = left + (right - left) / 2;

		// 递归求解左右两部分的最大子数组和
		int leftMax = maxSubArrayDivide(nums, left, mid);
		int rightMax = maxSubArrayDivide(nums, mid + 1, right);

		// 计算跨越中点的最大子数组和
		// 从中点向左扫描
		int leftCrossMax = Integer.MIN_VALUE;
		int leftSum = 0;
		for (int i = mid; i >= left; i--) {
			leftSum += nums[i];
			leftCrossMax = Math.max(leftCrossMax, leftSum);
		}

		// 从中点+1向右扫描
		int rightCrossMax = Integer.MIN_VALUE;
		int rightSum = 0;
		for (int i = mid + 1; i <= right; i++) {
			rightSum += nums[i];
			rightCrossMax = Math.max(rightCrossMax, rightSum);
		}

		// 跨越中点的最大和
		int crossMax = leftCrossMax + rightCrossMax;

		// 返回三者中的最大值
		return Math.max(Math.max(leftMax, rightMax), crossMax);
	}

	/**
	 * 最优解：Kadane算法（动态规划）
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(1)
	 */
	public static int maxSubArrayOptimal(int[] nums) {
		if (nums == null || nums.length == 0) {
			throw new IllegalArgumentException("数组不能为空");
		}

		int maxSum = nums[0];  // 全局最大和
		int curSum = nums[0];  // 当前最大和

		for (int i = 1; i < nums.length; i++) {
			// 当前元素要么单独成为新的子数组，要么加入之前的子数组
			curSum = Math.max(nums[i], curSum + nums[i]);
			// 更新全局最大和
			maxSum = Math.max(maxSum, curSum);
		}

		return maxSum;
	}

	// ==================== 题目2：LeetCode 169. 多数元素 (分治解法) ====================
	/**
	 * 题目来源：LeetCode 169. Majority Element
	 * 题目链接：https://leetcode.com/problems/majority-element/
	 * 中文链接：https://leetcode.cn/problems/majority-element/
	 * 
	 * 题目描述：
	 * 给定一个大小为 n 的数组 nums，返回其中的多数元素。
	 * 多数元素是指在数组中出现次数大于 ⌊n/2⌋ 的元素。
	 * 你可以假设数组是非空的，并且给定的数组总是存在多数元素。
	 * 
	 * 示例 1：
	 * 输入：nums = [3,2,3]
	 * 输出：3
	 * 
	 * 示例 2：
	 * 输入：nums = [2,2,1,1,1,2,2]
	 * 输出：2
	 * 
	 * 约束条件：
	 * n == nums.length
	 * 1 <= n <= 5 * 10^4
	 * -10^9 <= nums[i] <= 10^9
	 * 
	 * 解题思路（分治法）：
	 * 1. 如果一个元素是整个数组的多数元素，那么它必定是左半部分或右半部分的多数元素
	 * 2. 递归求解左右两部分的多数元素
	 * 3. 如果左右两部分的多数元素相同，直接返回
	 * 4. 如果不同，需要统计两个候选元素在整个区间的出现次数，返回次数多的
	 * 
	 * 时间复杂度分析：
	 * T(n) = 2*T(n/2) + O(n)  // 分治 + 统计次数
	 * 根据主定理：T(n) = O(n*log n)
	 * 
	 * 空间复杂度分析：
	 * 递归调用栈深度：O(log n)
	 * 
	 * 是否最优解：
	 * 分治法时间复杂度 O(n*log n)，空间复杂度 O(log n)
	 * 最优解是摩尔投票算法（Boyer-Moore Voting Algorithm）
	 * 时间复杂度 O(n)，空间复杂度 O(1)
	 * 分治法不是最优解
	 * 
	 * 最优解（摩尔投票算法）：
	 * - 维护候选元素和计数器
	 * - 遍历数组，遇到相同元素计数+1，不同元素计数-1
	 * - 计数为0时更换候选元素
	 * - 最后的候选元素即为多数元素
	 * 
	 * 其他解法：
	 * 1. 哈希表统计：O(n) 时间，O(n) 空间
	 * 2. 排序取中位数：O(n*log n) 时间，O(1) 或 O(n) 空间
	 * 3. 随机化算法：期望 O(n) 时间
	 */
	public static int majorityElement(int[] nums) {
		if (nums == null || nums.length == 0) {
			throw new IllegalArgumentException("数组不能为空");
		}
		return majorityElementDivide(nums, 0, nums.length - 1);
	}

	/**
	 * 分治法求解多数元素
	 */
	private static int majorityElementDivide(int[] nums, int left, int right) {
		// 基本情况：只有一个元素
		if (left == right) {
			return nums[left];
		}

		// 分解：计算中点
		int mid = left + (right - left) / 2;

		// 递归求解左右两部分的多数元素
		int leftMajor = majorityElementDivide(nums, left, mid);
		int rightMajor = majorityElementDivide(nums, mid + 1, right);

		// 如果左右多数元素相同，直接返回
		if (leftMajor == rightMajor) {
			return leftMajor;
		}

		// 如果不同，统计两个候选元素在当前区间的出现次数
		int leftCount = countInRange(nums, leftMajor, left, right);
		int rightCount = countInRange(nums, rightMajor, left, right);

		// 返回出现次数多的元素
		return leftCount > rightCount ? leftMajor : rightMajor;
	}

	/**
	 * 统计元素在指定范围内的出现次数
	 */
	private static int countInRange(int[] nums, int target, int left, int right) {
		int count = 0;
		for (int i = left; i <= right; i++) {
			if (nums[i] == target) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 最优解：摩尔投票算法
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(1)
	 */
	public static int majorityElementOptimal(int[] nums) {
		if (nums == null || nums.length == 0) {
			throw new IllegalArgumentException("数组不能为空");
		}

		int candidate = nums[0];
		int count = 1;

		for (int i = 1; i < nums.length; i++) {
			if (count == 0) {
				candidate = nums[i];
				count = 1;
			} else if (nums[i] == candidate) {
				count++;
			} else {
				count--;
			}
		}

		return candidate;
	}

	// ==================== 题目3：LeetCode 215. 数组中的第K个最大元素 (快速选择 - 分治思想) ====================
	/**
	 * 题目来源：LeetCode 215. Kth Largest Element in an Array
	 * 题目链接：https://leetcode.com/problems/kth-largest-element-in-an-array/
	 * 中文链接：https://leetcode.cn/problems/kth-largest-element-in-an-array/
	 * 
	 * 题目描述：
	 * 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
	 * 请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
	 * 你必须设计并实现时间复杂度为 O(n) 的算法解决此问题。
	 * 
	 * 示例 1：
	 * 输入: [3,2,1,5,6,4], k = 2
	 * 输出: 5
	 * 
	 * 示例 2：
	 * 输入: [3,2,3,1,2,4,5,5,6], k = 4
	 * 输出: 4
	 * 
	 * 约束条件：
	 * 1 <= k <= nums.length <= 10^5
	 * -10^4 <= nums[i] <= 10^4
	 * 
	 * 解题思路（快速选择算法 - 基于分治）：
	 * 1. 快速选择是快速排序的变体，基于分治思想
	 * 2. 选择一个枢轴元素，将数组分为三部分：小于、等于、大于枢轴
	 * 3. 根据枢轴的位置，决定在左半部分还是右半部分继续查找
	 * 4. 平均情况下只需要递归一半，不需要对两部分都递归
	 * 
	 * 时间复杂度分析：
	 * 平均情况：T(n) = T(n/2) + O(n) = O(n)
	 * 最坏情况：T(n) = T(n-1) + O(n) = O(n^2)（每次都选到最值）
	 * 通过随机化选择枢轴，可以将期望时间复杂度降到 O(n)
	 * 
	 * 空间复杂度分析：
	 * 递归调用栈：平均 O(log n)，最坏 O(n)
	 * 
	 * 是否最优解：
	 * 快速选择的平均时间复杂度 O(n) 是最优的
	 * 其他解法：
	 * 1. 排序后取第k个：O(n*log n) 时间
	 * 2. 小顶堆维护k个元素：O(n*log k) 时间，O(k) 空间
	 * 3. 大顶堆：O(n + k*log n) 时间，O(n) 空间
	 * 
	 * 快速选择是最优解（平均情况）
	 * 
	 * 调试技巧：
	 * 1. 打印每次分区后的枢轴位置
	 * 2. 验证分区后的数组是否满足左<=中<=右
	 * 3. 测试极端情况：全相同、有序、逆序
	 */
	public static int findKthLargest(int[] nums, int k) {
		if (nums == null || nums.length == 0 || k < 1 || k > nums.length) {
			throw new IllegalArgumentException("参数非法");
		}
		// 第k大元素 = 第(n-k)小元素（0-indexed）
		return quickSelect(nums, 0, nums.length - 1, nums.length - k);
	}

	/**
	 * 快速选择算法
	 * @param nums 数组
	 * @param left 左边界
	 * @param right 右边界
	 * @param k 查找第k小的元素（0-indexed）
	 */
	private static int quickSelect(int[] nums, int left, int right, int k) {
		if (left == right) {
			return nums[left];
		}

		// 随机选择枢轴，避免最坏情况
		Random random = new Random();
		int pivotIndex = left + random.nextInt(right - left + 1);
		pivotIndex = partition(nums, left, right, pivotIndex);

		// 根据枢轴位置决定在哪一半继续查找
		if (k == pivotIndex) {
			return nums[k];
		} else if (k < pivotIndex) {
			return quickSelect(nums, left, pivotIndex - 1, k);
		} else {
			return quickSelect(nums, pivotIndex + 1, right, k);
		}
	}

	/**
	 * 分区操作：将数组分为小于、等于、大于枢轴三部分
	 * @return 枢轴最终位置
	 */
	private static int partition(int[] nums, int left, int right, int pivotIndex) {
		int pivotValue = nums[pivotIndex];
		
		// 将枢轴移到最右边
		swap(nums, pivotIndex, right);
		
		// 分区：小于枢轴的放左边
		int storeIndex = left;
		for (int i = left; i < right; i++) {
			if (nums[i] < pivotValue) {
				swap(nums, storeIndex, i);
				storeIndex++;
			}
		}
		
		// 将枢轴放到最终位置
		swap(nums, storeIndex, right);
		
		return storeIndex;
	}

	private static void swap(int[] nums, int i, int j) {
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}

	// ==================== 题目4：LeetCode 240. 搜索二维矩阵 II ====================
	/**
	 * 题目来源：LeetCode 240. Search a 2D Matrix II
	 * 链接：https://leetcode.com/problems/search-a-2d-matrix-ii/
	 * 中文：https://leetcode.cn/problems/search-a-2d-matrix-ii/
	 * 
	 * 最优解：从右上角或左下角搜索，时间O(m+n)，空间O(1)
	 */
	public static boolean searchMatrix(int[][] matrix, int target) {
		if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
			return false;
		}
		
		// 从右上角开始搜索
		int row = 0;
		int col = matrix[0].length - 1;
		
		while (row < matrix.length && col >= 0) {
			if (matrix[row][col] == target) {
				return true;
			} else if (matrix[row][col] > target) {
				col--;  // 当前值大于目标，向左移动
			} else {
				row++;  // 当前值小于目标，向下移动
			}
		}
		
		return false;
	}

	/**
	 * 测试方法
	 */
	public static void main(String[] args) {
		System.out.println("========== 原始测试：分治求数组最大值 ==========");
		System.out.println("========== 原始测试：分治求数组最大值 ==========");
		// 测试用例1：普通数组
		int[] arr1 = { 3, 8, 7, 6, 4, 5, 1, 2 };
		System.out.println("数组最大值 : " + maxValue(arr1));
		
		// 测试用例2：单元素数组
		int[] arr2 = { 42 };
		System.out.println("单元素数组最大值 : " + maxValue(arr2));
		
		// 测试用例3：负数数组
		int[] arr3 = { -5, -2, -8, -1 };
		System.out.println("负数数组最大值 : " + maxValue(arr3));
		
		// 测试用例4：相同元素数组
		int[] arr4 = { 5, 5, 5, 5 };
		System.out.println("相同元素数组最大值 : " + maxValue(arr4));
		
		// 测试用例5：大规模数组
		int[] arr5 = new int[10000];
		for (int i = 0; i < arr5.length; i++) {
			arr5[i] = i;
		}
		System.out.println("大规模数组最大值 : " + maxValue(arr5));
		
		// 异常测试：空数组
		try {
			int[] arr6 = {};
			maxValue(arr6);
		} catch (IllegalArgumentException e) {
			System.out.println("空数组异常处理: " + e.getMessage());
		}
		
		// 异常测试：null数组
		try {
			maxValue(null);
		} catch (IllegalArgumentException e) {
			System.out.println("null数组异常处理: " + e.getMessage());
		}
		
		System.out.println("\n========== 题目1测试：LeetCode 53 最大子数组和 ==========");
		int[] nums1 = {-2,1,-3,4,-1,2,1,-5,4};
		System.out.println("分治法结果: " + maxSubArray(nums1));
		System.out.println("最优解(Kadane)结果: " + maxSubArrayOptimal(nums1));
		
		int[] nums2 = {5,4,-1,7,8};
		System.out.println("测试用例2: " + maxSubArrayOptimal(nums2));
		
		System.out.println("\n========== 题目2测试：LeetCode 169 多数元素 ==========");
		int[] nums3 = {3,2,3};
		System.out.println("分治法结果: " + majorityElement(nums3));
		System.out.println("最优解(摩尔投票)结果: " + majorityElementOptimal(nums3));
		
		int[] nums4 = {2,2,1,1,1,2,2};
		System.out.println("测试用例2: " + majorityElementOptimal(nums4));
		
		System.out.println("\n========== 题目3测试：LeetCode 215 第K大元素 ==========");
		int[] nums5 = {3,2,1,5,6,4};
		System.out.println("第2大元素: " + findKthLargest(nums5, 2));
		
		int[] nums6 = {3,2,3,1,2,4,5,5,6};
		System.out.println("第4大元素: " + findKthLargest(nums6, 4));
		
		System.out.println("\n========== 题目4测试：LeetCode 240 搜索矩阵 ==========");
		int[][] matrix = {
			{1,4,7,11,15},
			{2,5,8,12,19},
			{3,6,9,16,22},
			{10,13,14,17,24},
			{18,21,23,26,30}
		};
		System.out.println("搜索5: " + searchMatrix(matrix, 5));
		System.out.println("搜索20: " + searchMatrix(matrix, 20));
	}

	// ==================== 补充题目1：归并排序（经典分治算法） ====================
	/**
	 * 题目来源：经典排序算法
	 * 问题描述：使用归并排序对数组进行排序
	 * 
	 * 解题思路：
	 * 1. 将数组分成左右两半
	 * 2. 递归排序左右两半
	 * 3. 合并两个有序数组
	 * 
	 * 时间复杂度：O(n log n) - 最优、平均、最坏情况下都是
	 * 空间复杂度：O(n) - 需要额外空间存储临时数组
	 * 
	 * 是否最优解：对于排序问题，基于比较的排序算法无法突破O(n log n)的时间复杂度下限，
	 * 归并排序在理论上已经是最优的。
	 */
	public static void mergeSort(int[] nums) {
		if (nums == null || nums.length <= 1) {
			return;
		}
		int[] temp = new int[nums.length];
		mergeSortHelper(nums, 0, nums.length - 1, temp);
	}

	private static void mergeSortHelper(int[] nums, int left, int right, int[] temp) {
		if (left < right) {
			int mid = left + (right - left) / 2;
			mergeSortHelper(nums, left, mid, temp);
			mergeSortHelper(nums, mid + 1, right, temp);
			merge(nums, left, mid, right, temp);
		}
	}

	private static void merge(int[] nums, int left, int mid, int right, int[] temp) {
		int i = left;  // 左半部分起始索引
		int j = mid + 1;  // 右半部分起始索引
		int k = left;  // 临时数组索引

		// 比较左右两部分元素，将较小的元素放入临时数组
		while (i <= mid && j <= right) {
			if (nums[i] <= nums[j]) {
				temp[k++] = nums[i++];
			} else {
				temp[k++] = nums[j++];
			}
		}

		// 处理剩余元素
		while (i <= mid) {
			temp[k++] = nums[i++];
		}
		while (j <= right) {
			temp[k++] = nums[j++];
		}

		// 将临时数组复制回原数组
		for (i = left; i <= right; i++) {
			nums[i] = temp[i];
		}
	}

	// ==================== 补充题目2：二分查找（分治思想应用） ====================
	/**
	 * 题目来源：LeetCode 704. Binary Search
	 * 题目链接：https://leetcode.com/problems/binary-search/
	 * 中文链接：https://leetcode.cn/problems/binary-search/
	 * 
	 * 问题描述：给定一个有序数组和一个目标值，找出目标值在数组中的索引。
	 * 如果目标值不存在于数组中，返回-1。
	 * 
	 * 解题思路：
	 * 1. 将区间分为两半
	 * 2. 比较中间元素与目标值
	 * 3. 根据比较结果决定在左半区间还是右半区间继续查找
	 * 
	 * 时间复杂度：O(log n)
	 * 空间复杂度：递归实现 O(log n)，迭代实现 O(1)
	 * 
	 * 是否最优解：二分查找是有序数组查找问题的最优解
	 */
	public static int binarySearch(int[] nums, int target) {
		if (nums == null || nums.length == 0) {
			return -1;
		}
		return binarySearchHelper(nums, 0, nums.length - 1, target);
	}

	private static int binarySearchHelper(int[] nums, int left, int right, int target) {
		if (left > right) {
			return -1;
		}

		int mid = left + (right - left) / 2;

		if (nums[mid] == target) {
			return mid;
		} else if (nums[mid] > target) {
			return binarySearchHelper(nums, left, mid - 1, target);
		} else {
			return binarySearchHelper(nums, mid + 1, right, target);
		}
	}

	// 迭代版本的二分查找（最优解，空间复杂度O(1)）
	public static int binarySearchIterative(int[] nums, int target) {
		if (nums == null || nums.length == 0) {
			return -1;
		}

		int left = 0;
		int right = nums.length - 1;

		while (left <= right) {
			int mid = left + (right - left) / 2;

			if (nums[mid] == target) {
				return mid;
			} else if (nums[mid] > target) {
				right = mid - 1;
			} else {
				left = mid + 1;
			}
		}

		return -1;
	}

	// ==================== 补充题目3：快速幂算法（分治思想） ====================
	/**
	 * 题目来源：LeetCode 50. Pow(x, n)
	 * 题目链接：https://leetcode.com/problems/powx-n/
	 * 中文链接：https://leetcode.cn/problems/powx-n/
	 * 
	 * 问题描述：实现 pow(x, n) 计算 x 的 n 次幂函数
	 * 
	 * 解题思路：
	 * 1. 分治：将幂运算分解为更小的幂运算
	 * 2. 如果n是偶数：x^n = x^(n/2) * x^(n/2)
	 * 3. 如果n是奇数：x^n = x^(n/2) * x^(n/2) * x
	 * 
	 * 时间复杂度：O(log n)
	 * 空间复杂度：递归实现 O(log n)，迭代实现 O(1)
	 * 
	 * 是否最优解：快速幂算法是计算幂函数的最优解
	 */
	public static double myPow(double x, int n) {
		// 处理特殊情况
		if (n == 0) {
			return 1.0;
		}
		if (n == 1) {
			return x;
		}
		if (n == -1) {
			return 1.0 / x;
		}

		// 处理负数幂
		long N = n;
		if (N < 0) {
			x = 1.0 / x;
			N = -N;
		}

		return powHelper(x, N);
	}

	private static double powHelper(double x, long n) {
		if (n == 0) {
			return 1.0;
		}

		// 分治：计算一半的幂
		double half = powHelper(x, n / 2);

		// 合并结果
		if (n % 2 == 0) {
			return half * half;
		} else {
			return half * half * x;
		}
	}

	// ==================== 补充题目4：最大子矩阵和（二维分治法） ====================
	/**
	 * 题目来源：LeetCode 363. Max Sum of Rectangle No Larger Than K
	 * 类似题目：二维最大子数组和
	 * 
	 * 问题描述：给定一个二维矩阵，找出一个子矩阵，使得其元素和最大
	 * 
	 * 解题思路（二维分治法）：
	 * 1. 将矩阵按列分割成左右两部分
	 * 2. 递归求解左右两部分的最大子矩阵和
	 * 3. 计算跨越中间列的最大子矩阵和
	 * 4. 返回三者中的最大值
	 * 
	 * 时间复杂度：O(n^3 log n)
	 * 空间复杂度：O(n^2)
	 * 
	 * 是否最优解：对于二维最大子矩阵和，存在O(n^3)的动态规划解法
	 */
	public static int maxSubmatrixSum(int[][] matrix) {
		if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
			return 0;
		}

		int rows = matrix.length;
		int cols = matrix[0].length;

		return maxSubmatrixSumHelper(matrix, 0, cols - 1, rows);
	}

	private static int maxSubmatrixSumHelper(int[][] matrix, int leftCol, int rightCol, int rows) {
		// 基本情况：只有一列
		if (leftCol == rightCol) {
			return maxColumnSum(matrix, leftCol, rows);
		}

		// 分解：计算中间列
		int midCol = leftCol + (rightCol - leftCol) / 2;

		// 递归求解左右两部分
		int leftMax = maxSubmatrixSumHelper(matrix, leftCol, midCol, rows);
		int rightMax = maxSubmatrixSumHelper(matrix, midCol + 1, rightCol, rows);

		// 计算跨越中间列的最大子矩阵和
		int crossMax = maxCrossMatrixSum(matrix, leftCol, midCol, rightCol, rows);

		// 返回三者中的最大值
		return Math.max(Math.max(leftMax, rightMax), crossMax);
	}

	private static int maxColumnSum(int[][] matrix, int col, int rows) {
		int maxSum = Integer.MIN_VALUE;
		int currentSum = 0;

		for (int i = 0; i < rows; i++) {
			currentSum = Math.max(matrix[i][col], currentSum + matrix[i][col]);
			maxSum = Math.max(maxSum, currentSum);
		}

		return maxSum;
	}

	private static int maxCrossMatrixSum(int[][] matrix, int leftCol, int midCol, int rightCol, int rows) {
		int maxSum = Integer.MIN_VALUE;

		// 枚举左边界
		for (int i = leftCol; i <= midCol; i++) {
			// 枚举右边界
			for (int j = midCol + 1; j <= rightCol; j++) {
				// 计算当前列范围[i,j]的列和
				int[] columnSums = new int[rows];
				for (int k = 0; k < rows; k++) {
					int sum = 0;
					for (int c = i; c <= j; c++) {
						sum += matrix[k][c];
					}
					columnSums[k] = sum;
				}

				// 对列和应用Kadane算法求最大子数组和
				int currentSum = 0;
				for (int sum : columnSums) {
					currentSum = Math.max(sum, currentSum + sum);
					maxSum = Math.max(maxSum, currentSum);
				}
			}
		}

		return maxSum;
	}

	// ==================== 补充题目5：Strassen矩阵乘法（优化分治算法） ====================
	/**
	 * 题目来源：经典算法问题
	 * 
	 * 问题描述：实现Strassen算法计算两个n×n矩阵的乘积
	 * 
	 * 解题思路：
	 * 传统矩阵乘法的时间复杂度为O(n³)，Strassen算法通过巧妙地将矩阵乘法分解为7个而不是8个子矩阵乘法，
	 * 将时间复杂度降低到约O(n^2.807)。
	 * 
	 * 算法步骤：
	 * 1. 将矩阵分为4个子矩阵
	 * 2. 计算7个中间矩阵
	 * 3. 用中间矩阵构造结果矩阵
	 * 
	 * 时间复杂度：O(n^2.807)
	 * 空间复杂度：O(n²)
	 * 
	 * 是否最优解：Strassen算法比传统矩阵乘法更优，但存在更优的矩阵乘法算法
	 */
	public static int[][] strassenMatrixMultiply(int[][] A, int[][] B) {
		int n = A.length;
		
		// 处理2x2的基础情况
		if (n == 2) {
			return multiply2x2(A, B);
		}

		// 将矩阵分成4个子矩阵
		int newSize = n / 2;
		int[][] A11 = new int[newSize][newSize];
		int[][] A12 = new int[newSize][newSize];
		int[][] A21 = new int[newSize][newSize];
		int[][] A22 = new int[newSize][newSize];
		int[][] B11 = new int[newSize][newSize];
		int[][] B12 = new int[newSize][newSize];
		int[][] B21 = new int[newSize][newSize];
		int[][] B22 = new int[newSize][newSize];

		// 填充子矩阵
		for (int i = 0; i < newSize; i++) {
			for (int j = 0; j < newSize; j++) {
				A11[i][j] = A[i][j];
				A12[i][j] = A[i][j + newSize];
				A21[i][j] = A[i + newSize][j];
				A22[i][j] = A[i + newSize][j + newSize];
				B11[i][j] = B[i][j];
				B12[i][j] = B[i][j + newSize];
				B21[i][j] = B[i + newSize][j];
				B22[i][j] = B[i + newSize][j + newSize];
			}
		}

		// 计算7个中间矩阵
		int[][] M1 = strassenMatrixMultiply(addMatrices(A11, A22), addMatrices(B11, B22));
		int[][] M2 = strassenMatrixMultiply(addMatrices(A21, A22), B11);
		int[][] M3 = strassenMatrixMultiply(A11, subtractMatrices(B12, B22));
		int[][] M4 = strassenMatrixMultiply(A22, subtractMatrices(B21, B11));
		int[][] M5 = strassenMatrixMultiply(addMatrices(A11, A12), B22);
		int[][] M6 = strassenMatrixMultiply(subtractMatrices(A21, A11), addMatrices(B11, B12));
		int[][] M7 = strassenMatrixMultiply(subtractMatrices(A12, A22), addMatrices(B21, B22));

		// 计算结果矩阵的子矩阵
		int[][] C11 = addMatrices(subtractMatrices(addMatrices(M1, M4), M5), M7);
		int[][] C12 = addMatrices(M3, M5);
		int[][] C21 = addMatrices(M2, M4);
		int[][] C22 = addMatrices(subtractMatrices(addMatrices(M1, M3), M2), M6);

		// 合并结果矩阵
		int[][] result = new int[n][n];
		for (int i = 0; i < newSize; i++) {
			for (int j = 0; j < newSize; j++) {
				result[i][j] = C11[i][j];
				result[i][j + newSize] = C12[i][j];
				result[i + newSize][j] = C21[i][j];
				result[i + newSize][j + newSize] = C22[i][j];
			}
		}

		return result;
	}

	private static int[][] multiply2x2(int[][] A, int[][] B) {
		int[][] C = new int[2][2];
		C[0][0] = A[0][0] * B[0][0] + A[0][1] * B[1][0];
		C[0][1] = A[0][0] * B[0][1] + A[0][1] * B[1][1];
		C[1][0] = A[1][0] * B[0][0] + A[1][1] * B[1][0];
		C[1][1] = A[1][0] * B[0][1] + A[1][1] * B[1][1];
		return C;
	}

	private static int[][] addMatrices(int[][] A, int[][] B) {
		int n = A.length;
		int[][] C = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				C[i][j] = A[i][j] + B[i][j];
			}
		}
		return C;
	}

	private static int[][] subtractMatrices(int[][] A, int[][] B) {
		int n = A.length;
		int[][] C = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				C[i][j] = A[i][j] - B[i][j];
			}
		}
		return C;
	}

	// ==================== 补充题目6：最近点对问题（分治法） ====================
	/**
	 * 题目来源：经典计算几何问题
	 * 
	 * 问题描述：在平面上有n个点，找出其中距离最近的一对点
	 * 
	 * 解题思路：
	 * 1. 按x坐标排序所有点
	 * 2. 递归地将点集分为左右两部分
	 * 3. 找出左右两部分各自的最近点对
	 * 4. 考虑跨越中线的点对，证明只需要检查中线附近的有限个点
	 * 
	 * 时间复杂度：O(n log n)
	 * 空间复杂度：O(n)
	 * 
	 * 是否最优解：该问题的最优时间复杂度为O(n log n)
	 */
	public static class Point implements Comparable<Point> {
		public int x, y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public int compareTo(Point other) {
			return Integer.compare(this.x, other.x);
		}
	}

	public static double closestPair(Point[] points) {
		if (points == null || points.length < 2) {
			return Double.POSITIVE_INFINITY;
		}

		// 按x坐标排序
		Arrays.sort(points);

		// 为了按y坐标排序，创建一个副本
		Point[] pointsByY = points.clone();
		Arrays.sort(pointsByY, Comparator.comparingInt(p -> p.y));

		return closestPairHelper(points, 0, points.length - 1, pointsByY);
	}

	private static double closestPairHelper(Point[] pointsByX, int left, int right, Point[] pointsByY) {
		// 基本情况：小规模问题直接计算
		int n = right - left + 1;
		if (n <= 3) {
			return bruteForce(pointsByX, left, right);
		}

		// 分解：找到中点
		int mid = left + (right - left) / 2;
		Point midPoint = pointsByX[mid];

		// 准备按y排序的左右两部分点集
		Point[] leftPointsByY = new Point[mid - left + 1];
		Point[] rightPointsByY = new Point[right - mid];
		int leftIndex = 0, rightIndex = 0;

		for (Point p : pointsByY) {
			if (p.x <= midPoint.x) {
				leftPointsByY[leftIndex++] = p;
			} else {
				rightPointsByY[rightIndex++] = p;
			}
		}

		// 递归求解左右两部分
		double leftMin = closestPairHelper(pointsByX, left, mid, leftPointsByY);
		double rightMin = closestPairHelper(pointsByX, mid + 1, right, rightPointsByY);

		// 合并：取左右两部分的最小值
		double minDist = Math.min(leftMin, rightMin);

		// 处理跨越中线的点对
		// 只需要考虑中线附近2*minDist范围内的点
		List<Point> strip = new ArrayList<>();
		for (Point p : pointsByY) {
			if (Math.abs(p.x - midPoint.x) < minDist) {
				strip.add(p);
			}
		}

		// 在strip中检查相邻点，理论上最多只需要检查6个点
		for (int i = 0; i < strip.size(); i++) {
			for (int j = i + 1; j < strip.size() && (strip.get(j).y - strip.get(i).y) < minDist; j++) {
				double dist = distance(strip.get(i), strip.get(j));
				if (dist < minDist) {
					minDist = dist;
				}
			}
		}

		return minDist;
	}

	private static double bruteForce(Point[] points, int left, int right) {
		double minDist = Double.POSITIVE_INFINITY;
		for (int i = left; i <= right; i++) {
			for (int j = i + 1; j <= right; j++) {
				double dist = distance(points[i], points[j]);
				if (dist < minDist) {
					minDist = dist;
				}
			}
		}
		return minDist;
	}

	private static double distance(Point p1, Point p2) {
		int dx = p1.x - p2.x;
		int dy = p1.y - p2.y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	// ==================== 补充题目7：Karatsuba大整数乘法 ====================
	/**
	 * 题目来源：经典算法问题
	 * 
	 * 问题描述：实现Karatsuba算法进行大整数乘法运算
	 * 
	 * 解题思路：
	 * 1. 将两个大整数分别拆分为高位和低位两部分
	 * 2. 使用分治思想，将一次4次乘法减少为3次乘法
	 * 3. 通过巧妙的组合方式计算结果
	 * 
	 * 时间复杂度：O(n^log₂3) ≈ O(n^1.585)
	 * 空间复杂度：O(n)
	 * 
	 * 是否最优解：比传统O(n²)算法更优，但存在更优的FFT算法O(n log n)
	 */
	public static String karatsubaMultiply(String num1, String num2) {
		// 处理特殊情况
		if (num1.equals("0") || num2.equals("0")) {
			return "0";
		}

		// 调用递归辅助函数
		return karatsubaHelper(num1, num2);
	}

	private static String karatsubaHelper(String num1, String num2) {
		// 基本情况：小数字直接计算
		if (num1.length() < 10 || num2.length() < 10) {
			return multiplyStrings(num1, num2);
		}

		// 使两个数字长度相等，用0填充较短的数字
		int n = Math.max(num1.length(), num2.length());
		int half = (n + 1) / 2;

		// 补齐长度
		while (num1.length() < n) num1 = "0" + num1;
		while (num2.length() < n) num2 = "0" + num2;

		// 将数字分为两部分
		String a = num1.substring(0, num1.length() - half);
		String b = num1.substring(num1.length() - half);
		String c = num2.substring(0, num2.length() - half);
		String d = num2.substring(num2.length() - half);

		// 递归计算三个乘积
		String ac = karatsubaHelper(a, c);
		String bd = karatsubaHelper(b, d);
		String abcd = karatsubaHelper(addStrings(a, b), addStrings(c, d));

		// 计算ad + bc = (a+b)(c+d) - ac - bd
		String adPlusBc = subtractStrings(subtractStrings(abcd, ac), bd);

		// 组合结果
		// result = ac * 10^(2*half) + (ad+bc) * 10^half + bd
		String result = addStrings(addStrings(ac + "0".repeat(2 * half), adPlusBc + "0".repeat(half)), bd);

		// 移除前导零
		return removeLeadingZeros(result);
	}

	private static String multiplyStrings(String num1, String num2) {
		int[] result = new int[num1.length() + num2.length()];

		for (int i = num1.length() - 1; i >= 0; i--) {
			for (int j = num2.length() - 1; j >= 0; j--) {
				int mul = (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
				int p1 = i + j, p2 = i + j + 1;
				int sum = mul + result[p2];

				result[p2] = sum % 10;
				result[p1] += sum / 10;
			}
		}

		StringBuilder sb = new StringBuilder();
		for (int p : result) {
			if (!(sb.length() == 0 && p == 0)) {
				sb.append(p);
			}
		}
		return sb.length() == 0 ? "0" : sb.toString();
	}

	private static String addStrings(String num1, String num2) {
		StringBuilder sb = new StringBuilder();
		int carry = 0;
		int i = num1.length() - 1;
		int j = num2.length() - 1;

		while (i >= 0 || j >= 0 || carry != 0) {
			int sum = carry;
			if (i >= 0) sum += num1.charAt(i--) - '0';
			if (j >= 0) sum += num2.charAt(j--) - '0';
			sb.append(sum % 10);
			carry = sum / 10;
		}

		return sb.reverse().toString();
	}

	private static String subtractStrings(String num1, String num2) {
		StringBuilder sb = new StringBuilder();
		int carry = 0;
		int i = num1.length() - 1;
		int j = num2.length() - 1;

		while (i >= 0 || j >= 0) {
			int digit1 = (i >= 0) ? num1.charAt(i--) - '0' : 0;
			int digit2 = (j >= 0) ? num2.charAt(j--) - '0' : 0;
			int diff = digit1 - digit2 - carry;

			if (diff < 0) {
				diff += 10;
				carry = 1;
			} else {
				carry = 0;
			}

			sb.append(diff);
		}

		String result = sb.reverse().toString();
		return removeLeadingZeros(result);
	}

	private static String removeLeadingZeros(String str) {
		int i = 0;
		while (i < str.length() - 1 && str.charAt(i) == '0') {
			i++;
		}
		return str.substring(i);
	}

	// 添加补充题目的测试方法
	public static void testAdditionalProblems() {
		System.out.println("\n========== 补充题目测试 ==========");

		// 测试归并排序
		System.out.println("\n1. 归并排序测试：");
		int[] mergeArray = {9, 3, 7, 1, 5, 8, 2, 6, 4};
		mergeSort(mergeArray);
		System.out.print("排序后数组：");
		for (int num : mergeArray) {
			System.out.print(num + " ");
		}
		System.out.println();

		// 测试二分查找
		System.out.println("\n2. 二分查找测试：");
		int[] sortedArray = {1, 2, 3, 4, 5, 6, 7, 8, 9};
		System.out.println("查找5的索引（递归）：" + binarySearch(sortedArray, 5));
		System.out.println("查找5的索引（迭代）：" + binarySearchIterative(sortedArray, 5));
		System.out.println("查找10的索引：" + binarySearch(sortedArray, 10));

		// 测试快速幂
		System.out.println("\n3. 快速幂测试：");
		System.out.println("2^10 = " + myPow(2, 10));
		System.out.println("2^-2 = " + myPow(2, -2));
		System.out.println("3^5 = " + myPow(3, 5));

		// 测试最大子矩阵和（简化测试）
		System.out.println("\n4. 最大子矩阵和测试：");
		int[][] matrix = {
			{1, -2, 3},
			{-4, 5, -6},
			{7, -8, 9}
		};
		System.out.println("最大子矩阵和：" + maxSubmatrixSum(matrix));

		// 测试最近点对
		System.out.println("\n5. 最近点对测试：");
		Point[] points = {
			new Point(0, 0),
			new Point(3, 0),
			new Point(0, 4),
			new Point(1, 1),
			new Point(2, 2)
		};
		System.out.println("最近点对距离：" + closestPair(points));

		// 测试Karatsuba大整数乘法
		System.out.println("\n6. Karatsuba大整数乘法测试：");
		System.out.println("123456789 * 987654321 = " + karatsubaMultiply("123456789", "987654321"));
		System.out.println("0 * 12345 = " + karatsubaMultiply("0", "12345"));
		System.out.println("9999999999 * 9999999999 = " + karatsubaMultiply("9999999999", "9999999999"));
	}
}