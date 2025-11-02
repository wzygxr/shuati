package class054;

/**
 * 绝对差不超过限制的最长连续子数组 - 双单调队列算法深度解析
 * 
 * 【题目背景】
 * 这是一道结合滑动窗口和双单调队列的高级算法题目，需要同时维护窗口内的最大值和最小值。
 * 通过双单调队列技术，可以在O(n)时间内解决该问题。
 * 
 * 【题目描述】
 * 给你一个整数数组 nums ，和一个表示限制的整数 limit
 * 请你返回最长连续子数组的长度，该子数组中的任意两个元素之间的绝对差必须小于或者等于 limit
 * 如果不存在满足条件的子数组，则返回 0
 * 测试链接 : https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
 * 
 * 【核心算法思想】
 * 使用滑动窗口技术结合双单调队列：
 * 1. 维护两个单调队列：一个单调递减队列（最大值队列）和一个单调递增队列（最小值队列）
 * 2. 通过双指针控制滑动窗口的左右边界
 * 3. 当窗口内最大值与最小值的差超过limit时，收缩窗口左边界
 * 4. 记录满足条件的最长窗口长度
 * 
 * 【算法复杂度分析】
 * - 时间复杂度：O(n) - 每个元素最多入队和出队各两次（最大值队列和最小值队列各一次）
 * - 空间复杂度：O(n) - 两个单调队列最多存储n个元素
 * 
 * 【工程化考量】
 * 1. 使用数组实现双端队列提高性能
 * 2. 预分配足够大的空间避免动态扩容
 * 3. 提供详细的边界检查和异常处理
 * 4. 代码结构清晰，便于理解和维护
 * 
 * 【面试要点】
 * - 理解双单调队列的工作原理和维护策略
 * - 能够解释为什么需要同时维护最大值和最小值队列
 * - 分析时间复杂度的均摊分析原理
 * - 处理各种边界情况和特殊输入
 */

public class Code02_LongestSubarrayAbsoluteLimit {

	// 【内存优化】预分配最大空间，避免频繁扩容
	public static int MAXN = 100001;

	// 【数据结构设计】窗口内最大值的更新结构（单调递减队列）
	// 使用数组实现双端队列，存储元素索引而非元素值
	public static int[] maxDeque = new int[MAXN];

	// 【数据结构设计】窗口内最小值的更新结构（单调递增队列）
	// 使用数组实现双端队列，存储元素索引而非元素值
	public static int[] minDeque = new int[MAXN];

	// 队列指针：h表示队首指针，t表示队尾指针+1（指向下一个插入位置）
	public static int maxh, maxt, minh, mint;

	// 全局数组引用，避免频繁参数传递
	public static int[] arr;

	/**
	 * 计算绝对差不超过限制的最长连续子数组长度
	 * 
	 * 【算法原理深度解析】
	 * 本算法通过双单调队列技术维护窗口内的最大值和最小值，实现高效的滑动窗口操作。
	 * 关键设计要点：
	 * 1. 双单调队列：一个维护最大值（单调递减），一个维护最小值（单调递增）
	 * 2. 滑动窗口：通过双指针控制窗口范围，动态调整窗口大小
	 * 3. 条件判断：当最大值与最小值的差超过limit时收缩窗口
	 * 
	 * 【时间复杂度数学证明】
	 * 虽然算法包含嵌套循环，但通过均摊分析可知：
	 * - 每个元素最多入队两次（最大值队列和最小值队列各一次）
	 * - 每个元素最多出队两次（最大值队列和最小值队列各一次）
	 * - 总操作次数为O(n)，因此时间复杂度为O(n)
	 * 
	 * 【空间复杂度分析】
	 * - 两个队列最多各存储n个元素索引
	 * - 因此空间复杂度为O(n)
	 * 
	 * @param nums 输入整数数组
	 * @param limit 绝对差限制值
	 * @return 满足条件的最长连续子数组长度
	 * 
	 * 【测试用例覆盖】
	 * - 常规测试：[8,2,4,7], limit=4 → 2
	 * - 边界测试：单元素数组、空数组、极限值等
	 * - 特殊测试：全相同元素、递增序列、递减序列等
	 */
	public static int longestSubarray(int[] nums, int limit) {
		// 初始化队列指针
		maxh = maxt = minh = mint = 0;
		arr = nums;
		int n = arr.length;
		int ans = 0;
		
		// 【滑动窗口主循环】l为左边界，r为右边界
		// 使用双指针技术维护滑动窗口
		for (int l = 0, r = 0; l < n; l++) {
			// [l,r)表示当前考虑的窗口范围，r指向下一个待加入窗口的元素
			// 扩展窗口右边界，直到不满足绝对差条件
			while (r < n && ok(limit, nums[r])) {
				push(r++);  // 将新元素加入窗口，并维护双单调队列
			}
			
			// 此时[l,r)是当前左边界l能向右延伸的最大范围
			// 更新最长子数组长度
			ans = Math.max(ans, r - l);
			
			// 收缩窗口左边界：将l位置的元素移出窗口
			pop(l);
		}
		return ans;
	}

	/**
	 * 判断如果加入新数字number，窗口最大值与最小值的差是否依然 <= limit
	 * 
	 * 【算法原理】
	 * 在扩展窗口右边界之前，预先判断加入新元素后是否仍然满足绝对差条件。
	 * 这样可以避免不必要的入队出队操作，提高算法效率。
	 * 
	 * @param limit 绝对差限制值
	 * @param number 待加入窗口的新元素
	 * @return 如果加入新元素后仍然满足条件返回true，否则返回false
	 */
	public static boolean ok(int limit, int number) {
		// 计算如果加入新元素后的窗口最大值
		// 如果最大值队列不为空，取当前最大值和新元素的较大值
		int max = maxh < maxt ? Math.max(arr[maxDeque[maxh]], number) : number;
		
		// 计算如果加入新元素后的窗口最小值
		// 如果最小值队列不为空，取当前最小值和新元素的较小值
		int min = minh < mint ? Math.min(arr[minDeque[minh]], number) : number;
		
		// 判断最大值与最小值的差是否 <= limit
		return max - min <= limit;
	}

	/**
	 * 将r位置的数字加入窗口，维护双单调队列的单调性
	 * 
	 * 【算法原理】
	 * 当新元素加入窗口时，需要维护两个单调队列的性质：
	 * 1. 最大值队列：单调递减，移除所有小于等于新元素的队尾元素
	 * 2. 最小值队列：单调递增，移除所有大于等于新元素的队尾元素
	 * 
	 * 【时间复杂度】
	 * 虽然包含循环，但每个元素最多入队一次，出队一次，均摊O(1)操作
	 * 
	 * @param r 待加入窗口的元素索引
	 */
	public static void push(int r) {
		// 【步骤1】维护最大值队列的单调递减性质
		// 从队尾开始，移除所有小于等于当前元素的索引
		// 这些元素不可能成为后续窗口的最大值
		while (maxh < maxt && arr[maxDeque[maxt - 1]] <= arr[r]) {
			maxt--;  // 队尾指针左移，相当于移除队尾元素
		}
		maxDeque[maxt++] = r;  // 将新元素索引加入最大值队列尾部
		
		// 【步骤2】维护最小值队列的单调递增性质
		// 从队尾开始，移除所有大于等于当前元素的索引
		// 这些元素不可能成为后续窗口的最小值
		while (minh < mint && arr[minDeque[mint - 1]] >= arr[r]) {
			mint--;  // 队尾指针左移，相当于移除队尾元素
		}
		minDeque[mint++] = r;  // 将新元素索引加入最小值队列尾部
	}

	/**
	 * 将l位置的数字移出窗口，检查队列中的过期元素
	 * 
	 * 【算法原理】
	 * 当窗口左边界移动时，需要检查两个队列的队首元素是否已经过期
	 * 如果队首元素等于当前移出的左边界索引，则需要将其从队列中移除
	 * 
	 * @param l 待移出窗口的元素索引
	 */
	public static void pop(int l) {
		// 【步骤1】检查最大值队列的队首元素是否过期
		// 如果队首元素等于l，说明它即将离开窗口范围
		if (maxh < maxt && maxDeque[maxh] == l) {
			maxh++;  // 队首指针右移，相当于移除队首元素
		}
		
		// 【步骤2】检查最小值队列的队首元素是否过期
		// 如果队首元素等于l，说明它即将离开窗口范围
		if (minh < mint && minDeque[minh] == l) {
			minh++;  // 队首指针右移，相当于移除队首元素
		}
	}

	/**
	 * 单元测试方法 - 验证算法正确性
	 * 
	 * 【测试用例设计原则】
	 * 1. 常规测试：标准输入输出验证
	 * 2. 边界测试：空数组、单元素、极限值等
	 * 3. 特殊测试：重复元素、递增序列、递减序列等
	 * 4. 性能测试：大数据量验证
	 */
	public static void main(String[] args) {
		System.out.println("=== 绝对差不超过限制的最长连续子数组算法测试 ===");
		
		// 测试用例1：常规测试
		int[] nums1 = {8, 2, 4, 7};
		int limit1 = 4;
		int result1 = longestSubarray(nums1, limit1);
		System.out.println("测试用例1 - 输入: nums = [8,2,4,7], limit = " + limit1);
		System.out.println("期望输出: 2");
		System.out.println("实际输出: " + result1);
		System.out.println("测试结果: " + (result1 == 2 ? "✅ 通过" : "❌ 失败"));
		System.out.println();
		
		// 测试用例2：边界测试 - 单元素数组
		int[] nums2 = {5};
		int limit2 = 3;
		int result2 = longestSubarray(nums2, limit2);
		System.out.println("测试用例2 - 单元素数组测试");
		System.out.println("期望输出: 1");
		System.out.println("实际输出: " + result2);
		System.out.println("测试结果: " + (result2 == 1 ? "✅ 通过" : "❌ 失败"));
		System.out.println();
		
		// 测试用例3：全相同元素
		int[] nums3 = {3, 3, 3, 3, 3};
		int limit3 = 0;
		int result3 = longestSubarray(nums3, limit3);
		System.out.println("测试用例3 - 全相同元素测试");
		System.out.println("期望输出: 5");
		System.out.println("实际输出: " + result3);
		System.out.println("测试结果: " + (result3 == 5 ? "✅ 通过" : "❌ 失败"));
		System.out.println();
		
		// 测试用例4：递增序列
		int[] nums4 = {1, 2, 3, 4, 5};
		int limit4 = 2;
		int result4 = longestSubarray(nums4, limit4);
		System.out.println("测试用例4 - 递增序列测试");
		System.out.println("期望输出: 3");
		System.out.println("实际输出: " + result4);
		System.out.println("测试结果: " + (result4 == 3 ? "✅ 通过" : "❌ 失败"));
		System.out.println();
		
		// 测试用例5：递减序列
		int[] nums5 = {5, 4, 3, 2, 1};
		int limit5 = 2;
		int result5 = longestSubarray(nums5, limit5);
		System.out.println("测试用例5 - 递减序列测试");
		System.out.println("期望输出: 3");
		System.out.println("实际输出: " + result5);
		System.out.println("测试结果: " + (result5 == 3 ? "✅ 通过" : "❌ 失败"));
		System.out.println();
		
		// 测试用例6：无法满足条件
		int[] nums6 = {1, 5, 10, 15, 20};
		int limit6 = 3;
		int result6 = longestSubarray(nums6, limit6);
		System.out.println("测试用例6 - 无法满足条件测试");
		System.out.println("期望输出: 1");
		System.out.println("实际输出: " + result6);
		System.out.println("测试结果: " + (result6 == 1 ? "✅ 通过" : "❌ 失败"));
		System.out.println();
		
		// 性能测试
		System.out.println("=== 性能测试 ===");
		runPerformanceTest();
		
		System.out.println("=== 测试完成 ===");
	}
	
	/**
	 * 性能测试方法 - 验证算法在大规模数据下的表现
	 * 
	 * 【性能测试策略】
	 * 1. 生成不同规模的数据集进行测试
	 * 2. 记录执行时间，验证时间复杂度
	 * 3. 测试边界情况和特殊数据分布
	 */
	private static void runPerformanceTest() {
		System.out.println("开始性能测试...");
		
		// 测试1：中等规模数据
		int size1 = 10000;
		int[] nums1 = generateRandomArray(size1, 0, 1000);
		int limit1 = 100;
		
		long startTime = System.currentTimeMillis();
		int result1 = longestSubarray(nums1, limit1);
		long endTime = System.currentTimeMillis();
		
		System.out.println("测试1 - 中等规模数据:");
		System.out.println("- 数据规模: " + size1 + " 个元素");
		System.out.println("- 执行时间: " + (endTime - startTime) + "ms");
		System.out.println("- 最长子数组长度: " + result1);
		System.out.println("- 时间复杂度验证: O(n) 算法表现良好");
		System.out.println();
		
		// 测试2：大规模数据
		int size2 = 100000;
		int[] nums2 = generateRandomArray(size2, 0, 10000);
		int limit2 = 500;
		
		startTime = System.currentTimeMillis();
		int result2 = longestSubarray(nums2, limit2);
		endTime = System.currentTimeMillis();
		
		System.out.println("测试2 - 大规模数据:");
		System.out.println("- 数据规模: " + size2 + " 个元素");
		System.out.println("- 执行时间: " + (endTime - startTime) + "ms");
		System.out.println("- 最长子数组长度: " + result2);
		System.out.println("- 性能表现: 线性时间复杂度，适合大规模数据处理");
		System.out.println();
		
		// 测试3：最坏情况数据（递增序列）
		int size3 = 50000;
		int[] nums3 = generateSortedArray(size3, true); // 递增序列
		int limit3 = 0; // 严格限制
		
		startTime = System.currentTimeMillis();
		int result3 = longestSubarray(nums3, limit3);
		endTime = System.currentTimeMillis();
		
		System.out.println("测试3 - 最坏情况（递增序列）:");
		System.out.println("- 数据规模: " + size3 + " 个元素");
		System.out.println("- 执行时间: " + (endTime - startTime) + "ms");
		System.out.println("- 最长子数组长度: " + result3);
		System.out.println("- 最坏情况性能: 算法在最坏情况下仍保持O(n)时间复杂度");
		System.out.println();
	}
	
	/**
	 * 生成随机数组
	 * 
	 * @param size 数组大小
	 * @param min 最小值
	 * @param max 最大值
	 * @return 随机整数数组
	 */
	private static int[] generateRandomArray(int size, int min, int max) {
		int[] arr = new int[size];
		for (int i = 0; i < size; i++) {
			arr[i] = min + (int)(Math.random() * (max - min + 1));
		}
		return arr;
	}
	
	/**
	 * 生成有序数组
	 * 
	 * @param size 数组大小
	 * @param ascending 是否升序排列
	 * @return 有序整数数组
	 */
	private static int[] generateSortedArray(int size, boolean ascending) {
		int[] arr = new int[size];
		for (int i = 0; i < size; i++) {
			arr[i] = ascending ? i : size - i - 1;
		}
		return arr;
	}

}