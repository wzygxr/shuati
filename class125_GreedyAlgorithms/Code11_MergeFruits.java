package class094;

import java.util.PriorityQueue;

// 合并果子 (Merge Fruits)
// 在一个果园里，多多已经将所有的果子打了下来，而且按果子的不同种类分成了不同的堆。
// 多多决定把所有的果子合成一堆。
// 每一次合并，多多可以把两堆果子合并到一起，消耗的体力等于两堆果子的重量之和。
// 可以看出，所有的果子经过 n-1 次合并之后，就只剩下一堆了。
// 多多在合并果子时总共消耗的体力等于每次合并所耗体力之和。
// 因为还要花大力气把这些果子搬回家，所以多多在合并果子时要尽可能地节省体力。
// 假定每个果子重量都为 1，并且已知果子的种类数和每种果子的数目，
// 你的任务是设计出合并的次序方案，使多多耗费的体力最少，并输出这个最小的体力耗费值。
// 
// 算法标签: 贪心算法(Greedy Algorithm)、哈夫曼编码(Huffman Coding)、最小堆(Min Heap)
// 时间复杂度: O(n*log(n))，其中n是果子种类数
// 空间复杂度: O(n)，最小堆的空间
// 测试链接 : https://www.luogu.com.cn/problem/P1090
// 相关题目: LeetCode 1046. 最后一块石头的重量、LeetCode 703. 数据流中的第K大元素
// 贪心算法专题 - 堆与哈夫曼编码问题集合
public class Code11_MergeFruits {

	/*
	 * 算法思路详解：
	 * 1. 贪心策略核心：哈夫曼编码思想，每次选择重量最小的两堆果子合并，
	 *    这样可以确保重量大的果子堆参与合并的次数最少，
	 *    从而最小化总体力消耗
	 * 2. 数据结构选择：使用最小堆维护所有果子堆，
	 *    便于高效获取当前重量最小的两堆果子
	 * 3. 合并过程：每次取出重量最小的两堆果子合并，
	 *    合并后的重量重新放入堆中
	 * 4. 结果累加：重复合并过程直到只剩下一堆果子，
	 *    累加每次合并的体力消耗
	 *
	 * 时间复杂度分析：
	 * - O(n*log(n))，其中n是果子种类数
	 * - 初始建堆：O(n*log(n))
	 * - n-1次合并操作：每次操作包含2次poll和1次offer，均为O(log(n))
	 * 空间复杂度分析：
	 * - O(n)，最小堆最多存储n个元素
	 * 是否最优解：是，这是处理此类哈夫曼编码问题的最优解法
	 *
	 * 工程化最佳实践：
	 * 1. 输入验证：严格检查输入参数的有效性，防止空指针异常
	 * 2. 边界处理：妥善处理各种边界情况，如空数组、单元素数组等
	 * 3. 性能优化：采用优先队列维护最小值，避免重复排序
	 * 4. 代码可读性：使用语义明确的变量名和详尽的注释
	 * 5. 资源管理：及时释放不再使用的对象
	 *
	 * 极端场景与边界情况处理：
	 * 1. 空输入场景：fruits为空数组或null时直接返回0
	 * 2. 单堆场景：只有一堆果子时不需要合并
	 * 3. 相同重量场景：多个果子堆重量相同时的处理
	 * 4. 有序序列场景：果子堆重量已排序的情况
	 * 5. 极值场景：重量差异极大的果子堆
	 *
	 * 跨语言实现差异与优化：
	 * 1. Java实现：使用PriorityQueue实现最小堆，注意比较器设置
	 * 2. C++实现：使用priority_queue实现最小堆，注意模板参数
	 * 3. Python实现：使用heapq实现最小堆，注意默认为最小堆
	 * 4. 内存管理：不同语言的垃圾回收机制对性能的影响
	 *
	 * 调试与测试策略：
	 * 1. 过程可视化：在关键节点打印当前合并的果子堆和体力消耗
	 * 2. 断言验证：在每次合并后添加断言确保堆中元素数量正确
	 * 3. 性能监控：跟踪堆操作的实际执行时间
	 * 4. 边界测试：设计覆盖所有边界条件的测试用例
	 * 5. 压力测试：使用大规模数据验证算法稳定性
	 *
	 * 实际应用场景与拓展：
	 * 1. 数据压缩：哈夫曼编码构建最优前缀码
	 * 2. 文件合并：多个有序文件合并为一个文件
	 * 3. 任务调度：多个任务合并执行的最优策略
	 * 4. 网络传输：多个数据包合并传输的优化
	 * 5. 存储管理：多个小文件合并存储的策略
	 *
	 * 算法深入解析：
	 * 1. 哈夫曼编码原理：通过贪心策略构建最优二叉树
	 * 2. 最优性证明：通过数学归纳法可以证明贪心策略的正确性
	 * 3. 策略变体：可扩展为多路合并等变体问题
	 * 4. 问题转换：最小体力消耗 = 所有内部节点权重之和
	 */
	public static int mergeFruits(int[] fruits) {
		// 异常处理：检查输入是否为空
		if (fruits == null || fruits.length == 0) {
			return 0;
		}
		
		// 边界条件：只有一堆果子，不需要合并
		if (fruits.length == 1) {
			return 0;
		}
		
		// 使用最小堆维护所有果子堆
		PriorityQueue<Integer> minHeap = new PriorityQueue<>();
		
		// 将所有果子堆加入最小堆
		for (int fruit : fruits) {
			minHeap.offer(fruit);
		}
		
		int totalCost = 0;  // 总体力消耗
		
		// 重复合并直到只剩下一堆果子
		while (minHeap.size() > 1) {
			// 取出重量最小的两堆果子
			int first = minHeap.poll();
			int second = minHeap.poll();
			
			// 合并两堆果子
			int cost = first + second;
			totalCost += cost;
			
			// 将合并后的果子堆重新放入堆中
			minHeap.offer(cost);
		}
		
		return totalCost;
	}
	
	// 测试函数
	public static void main(String[] args) {
		// 测试用例1
		int[] fruits1 = {1, 2, 9};
		System.out.println("测试用例1结果: " + mergeFruits(fruits1)); // 期望输出: 15
		
		// 测试用例2
		int[] fruits2 = {3, 5, 7, 9};
		System.out.println("测试用例2结果: " + mergeFruits(fruits2)); // 期望输出: 45
		
		// 测试用例3
		int[] fruits3 = {1, 1, 1, 1, 1};
		System.out.println("测试用例3结果: " + mergeFruits(fruits3)); // 期望输出: 12
		
		// 测试用例4：边界情况
		int[] fruits4 = {5};
		System.out.println("测试用例4结果: " + mergeFruits(fruits4)); // 期望输出: 0
		
		// 测试用例5：极端情况
		int[] fruits5 = {1, 100};
		System.out.println("测试用例5结果: " + mergeFruits(fruits5)); // 期望输出: 101
		
		// 测试补充题目1: 最小堆实现的K个最小元素
		System.out.println("\n测试补充题目1: 最小堆实现的K个最小元素");
		int[] nums1 = {3, 2, 1, 5, 6, 4};
		int k1 = 2;
		int[] result1 = findKthSmallestElements(nums1, k1);
		System.out.print("测试用例1结果: [");
		for (int i = 0; i < result1.length; i++) {
			System.out.print(result1[i]);
			if (i < result1.length - 1) {
				System.out.print(", ");
			}
		}
		System.out.println("]"); // 期望输出: [1, 2]
		
		// 测试补充题目2: 最大堆实现的K个最大元素
		System.out.println("\n测试补充题目2: 最大堆实现的K个最大元素");
		int[] nums2 = {3, 2, 1, 5, 6, 4};
		int k2 = 3;
		int[] result2 = findKthLargestElements(nums2, k2);
		System.out.print("测试用例1结果: [");
		for (int i = 0; i < result2.length; i++) {
			System.out.print(result2[i]);
			if (i < result2.length - 1) {
				System.out.print(", ");
			}
		}
		System.out.println("]"); // 期望输出: [4, 5, 6]
		
		// 测试补充题目3: 最后一块石头的重量
		System.out.println("\n测试补充题目3: 最后一块石头的重量");
		int[] stones1 = {2, 7, 4, 1, 8, 1};
		System.out.println("测试用例1结果: " + lastStoneWeight(stones1)); // 期望输出: 1
		
		int[] stones2 = {1};
		System.out.println("测试用例2结果: " + lastStoneWeight(stones2)); // 期望输出: 1
		
		// 测试补充题目4: 数据流中的第K大元素
		System.out.println("\n测试补充题目4: 数据流中的第K大元素");
		KthLargest kthLargest = new KthLargest(3, new int[]{4, 5, 8, 2});
		System.out.println("添加3后，第3大元素: " + kthLargest.add(3));  // 返回 4
		System.out.println("添加5后，第3大元素: " + kthLargest.add(5));  // 返回 5
		System.out.println("添加10后，第3大元素: " + kthLargest.add(10)); // 返回 5
		System.out.println("添加9后，第3大元素: " + kthLargest.add(9));  // 返回 8
		System.out.println("添加4后，第3大元素: " + kthLargest.add(4));  // 返回 8
		
		// 测试补充题目5: 最小的K个数（优化版本）
		System.out.println("\n测试补充题目5: 最小的K个数（优化版本）");
		int[] nums3 = {3, 2, 1, 5, 6, 4};
		int k3 = 2;
		int[] result3 = getLeastNumbers(nums3, k3);
		System.out.print("测试用例1结果: [");
		for (int i = 0; i < result3.length; i++) {
			System.out.print(result3[i]);
			if (i < result3.length - 1) {
				System.out.print(", ");
			}
		}
		System.out.println("]"); // 期望输出: [1, 2]
	}
	
	// 补充题目1: LeetCode 703. 数据流中的第K大元素（最小堆应用）
	// 题目描述: 设计一个找到数据流中第K大元素的类（class）。注意是排序后的第K大元素，不是第K个不同的元素。
	// 请实现 KthLargest 类:
	// KthLargest(int k, int[] nums) 使用整数 k 和整数流 nums 初始化对象。
	// int add(int val) 将 val 插入数据流 nums 后，返回当前数据流中第K大的元素。
	// 链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
	public static class KthLargest {
		private PriorityQueue<Integer> minHeap; // 维护K个最大元素的最小堆
		private int k;
		
		/**
		 * 构造函数
		 * 
		 * @param k 第K大的元素
		 * @param nums 初始化的数据流
		 * 
		 * 算法思路详解:
		 * 1. 数据结构选择：使用容量为k的最小堆维护K个最大元素，
		 *    堆顶元素即为第K大元素
		 * 2. 初始化过程：遍历初始数据流，通过add方法逐个添加元素
		 * 3. 堆维护策略：始终保持堆中元素个数不超过k
		 * 
		 * 时间复杂度分析:
		 * - 构造函数: O(n*log(k))，其中n是初始数组长度
		 * - add操作: O(log(k))，堆操作的时间复杂度
		 * 空间复杂度分析:
		 * - O(k)，堆的大小始终不超过k
		 * 是否最优解: 是，这是解决动态第K大元素问题的最优方法
		 *
		 * 工程化考量:
		 * 1. 参数验证：检查k的有效性和数组的合法性
		 * 2. 内存管理：预设堆容量避免动态扩容
		 * 3. 代码复用：复用add方法简化初始化逻辑
		 *
		 * 实际应用场景:
		 * 1. 实时排行榜：维护用户积分排名
		 * 2. 数据分析：实时监控关键指标
		 * 3. 游戏开发：实时更新玩家排名
		 * 4. 金融系统：监控交易量排名
		 */
		public KthLargest(int k, int[] nums) {
			this.k = k;
			this.minHeap = new PriorityQueue<>(k); // 容量为k的最小堆
			
			// 将数组中的元素添加到堆中
			for (int num : nums) {
				add(num); // 复用add方法
			}
		}
		
		/**
		 * 添加元素到数据流中，并返回当前第K大的元素
		 * 
		 * @param val 要添加的元素
		 * @return 当前数据流中第K大的元素
		 *
		 * 算法详解与策略分析:
		 * 1. 插入策略：
		 *    - 若堆未满：直接插入新元素
		 *    - 若堆满且新元素大于堆顶：替换堆顶元素
		 *    - 否则：忽略新元素
		 * 2. 堆维护：通过堆的自动调整维护K个最大元素
		 * 3. 结果返回：堆顶元素即为第K大元素
		 *
		 * 时间复杂度：O(log(k))，堆操作的时间复杂度
		 * 空间复杂度：O(1)，不考虑堆本身的空间
		 *
		 * 优化策略：
		 * 1. 提前过滤：对于小于堆顶的元素直接忽略
		 * 2. 边界处理：妥善处理堆为空和堆满的情况
		 */
		public int add(int val) {
			// 如果堆的大小小于k，直接添加
			if (minHeap.size() < k) {
				minHeap.offer(val);
			} 
			// 如果当前元素比堆顶元素大，替换堆顶元素
			else if (val > minHeap.peek()) {
				minHeap.poll(); // 移除堆顶元素（最小的元素）
				minHeap.offer(val); // 添加新元素
			}
			// 堆顶元素即为第K大元素
			return minHeap.peek();
		}
	}
	
	// 补充题目2: LeetCode 1046. 最后一块石头的重量（最大堆应用）
	// 题目描述: 有一堆石头，每块石头的重量都是正整数。
	// 每一回合，从中选出两块 最重的 石头，然后将它们一起粉碎。假设石头的重量分别为 x 和 y，且 x <= y。那么粉碎的可能结果如下：
	// 如果 x == y，那么两块石头都会被完全粉碎；
	// 如果 x != y，那么重量为 x 的石头将会完全粉碎，而重量为 y 的石头新重量为 y-x。
	// 最后，最多只会剩下一块石头。返回此石头的重量。如果没有石头剩下，就返回 0。
	// 链接: https://leetcode.cn/problems/last-stone-weight/
	public static int lastStoneWeight(int[] stones) {
		/**
		 * 最后一块石头的重量
		 * 
		 * @param stones 石头重量数组
		 * @return 最后剩下石头的重量，没有则返回0
		 * 
		 * 算法思路详解:
		 * 1. 数据结构选择：使用最大堆维护所有石头的重量，
		 *    便于高效获取重量最大的两块石头
		 * 2. 粉碎过程：每次取出重量最大的两块石头进行粉碎
		 * 3. 剩余处理：若粉碎后有剩余，将剩余重量重新放入堆中
		 * 4. 终止条件：重复粉碎过程直到堆中最多剩下一块石头
		 * 
		 * 时间复杂度分析:
		 * - O(n*log(n))，其中n是石头数量
		 * - 初始建堆：O(n*log(n))
		 * - 最多n-1次操作：每次操作包含2次poll和可能的1次offer，均为O(log(n))
		 * 空间复杂度分析:
		 * - O(n)，最大堆最多存储n个元素
		 * 是否最优解: 是，这是解决此类石头粉碎问题的最优方法
		 *
		 * 工程化最佳实践:
		 * 1. 输入验证：严格检查输入参数的有效性
		 * 2. 边界处理：妥善处理空数组和单元素数组
		 * 3. 资源管理：及时释放不再使用的对象
		 *
		 * 实际应用场景:
		 * 1. 资源合并：多个资源合并为更大的资源
		 * 2. 任务调度：多个任务合并执行
		 * 3. 数据处理：多个数据块合并处理
		 */
		// 异常处理：检查输入是否为空
		if (stones == null || stones.length == 0) {
			return 0;
		}
		
		// 使用最大堆维护所有石头的重量
		PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
		
		// 将所有石头加入最大堆
		for (int stone : stones) {
			maxHeap.offer(stone);
		}
		
		// 重复粉碎过程
		while (maxHeap.size() > 1) {
			// 取出两个最大的石头
			int y = maxHeap.poll(); // 第一大的石头
			int x = maxHeap.poll(); // 第二大的石头
			
			// 如果两块石头重量不同，将剩余重量放回堆中
			if (x != y) {
				maxHeap.offer(y - x);
			}
		}
		
		// 返回最后剩下的石头重量，如果没有则返回0
		return maxHeap.isEmpty() ? 0 : maxHeap.poll();
	}
	
	// 补充题目3: LeetCode 215. 数组中的第K个最大元素（最小堆实现）
	// 题目描述: 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
	// 请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
	// 链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
	public static int findKthLargest(int[] nums, int k) {
		/**
		 * 数组中的第K个最大元素
		 * 
		 * @param nums 整数数组
		 * @param k 第K大的元素
		 * @return 第K大的元素
		 * 
		 * 算法思路详解:
		 * 1. 数据结构选择：使用容量为k的最小堆维护K个最大元素，
		 *    这样堆顶元素即为第K大元素
		 * 2. 初始化策略：前k个元素直接加入堆中
		 * 3. 维护机制：对于剩余元素，若大于堆顶则替换堆顶
		 * 4. 结果返回：堆顶元素即为所求
		 * 
		 * 时间复杂度分析:
		 * - O(n*log(k))，其中n是数组长度
		 * - 前k个元素插入：O(k*log(k))
		 * - 剩余n-k个元素处理：每次操作O(log(k))
		 * 空间复杂度分析:
		 * - O(k)，堆的大小始终为k
		 * 是否最优解: 是，这是解决静态数组第K大元素问题的高效方法之一
		 *
		 * 工程化最佳实践:
		 * 1. 参数验证：严格检查输入参数的有效性
		 * 2. 边界处理：妥善处理k的边界情况
		 * 3. 性能优化：通过堆维护避免全排序
		 *
		 * 算法对比与拓展:
		 * 1. 与快排分区法对比：此方法时间复杂度稳定但空间复杂度较高
		 * 2. 与全排序法对比：此方法在k较小时效率更高
		 */
		// 异常处理：检查输入是否有效
		if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
			throw new IllegalArgumentException("Invalid input");
		}
		
		// 使用最小堆维护K个最大元素
		PriorityQueue<Integer> minHeap = new PriorityQueue<>(k);
		
		// 前k个元素直接加入堆中
		for (int i = 0; i < k; i++) {
			minHeap.offer(nums[i]);
		}
		
		// 对于剩下的元素，如果比堆顶元素大，则替换堆顶元素
		for (int i = k; i < nums.length; i++) {
			if (nums[i] > minHeap.peek()) {
				minHeap.poll();
				minHeap.offer(nums[i]);
			}
		}
		
		// 堆顶元素即为第K大元素
		return minHeap.peek();
	}
	
	// 补充题目4: LeetCode 347. 前K个高频元素（最大堆应用）
	// 题目描述: 给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。你可以按 任意顺序 返回答案。
	// 链接: https://leetcode.cn/problems/top-k-frequent-elements/
	public static int[] topKFrequent(int[] nums, int k) {
		/**
		 * 前K个高频元素
		 * 
		 * @param nums 整数数组
		 * @param k 前K个高频元素
		 * @return 出现频率前K高的元素数组
		 * 
		 * 算法思路详解:
		 * 1. 频率统计：使用哈希表统计每个元素出现的频率，
		 *    时间复杂度O(n)
		 * 2. 数据结构选择：使用最大堆根据频率排序，
		 *    便于高效获取高频元素
		 * 3. 结果提取：取出前K个元素构成结果数组
		 * 
		 * 时间复杂度分析:
		 * - O(n*log(n))，其中n是数组长度
		 * - 频率统计：O(n)
		 * - 建堆：O(m*log(m))，其中m是不同元素个数
		 * - 取前K个：O(k*log(m))
		 * 空间复杂度分析:
		 * - O(n)，哈希表和堆的空间
		 * 是否最优解: 是，这是解决频率统计问题的高效方法之一
		 *
		 * 工程化最佳实践:
		 * 1. 输入验证：严格检查输入参数的有效性
		 * 2. 边界处理：妥善处理k的边界情况
		 * 3. 内存优化：合理使用数据结构避免内存浪费
		 *
		 * 算法优化与变体:
		 * 1. 使用最小堆维护K个高频元素可优化至O(n*log(k))
		 * 2. 使用桶排序可优化至O(n)时间复杂度
		 */
		// 异常处理：检查输入是否有效
		if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
			return new int[0];
		}
		
		// 使用哈希表统计每个元素出现的频率
		java.util.HashMap<Integer, Integer> frequencyMap = new java.util.HashMap<>();
		for (int num : nums) {
			frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
		}
		
		// 使用最大堆根据频率排序
		// 堆中存储的是Map.Entry<Integer, Integer>，比较的是频率值
		PriorityQueue<java.util.Map.Entry<Integer, Integer>> maxHeap = 
			new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());
		
		// 将所有元素加入堆中
		maxHeap.addAll(frequencyMap.entrySet());
		
		// 取出前K个高频元素
		int[] result = new int[k];
		for (int i = 0; i < k; i++) {
			result[i] = maxHeap.poll().getKey();
		}
		
		return result;
	}
	
	// 补充题目5: LeetCode 973. 最接近原点的K个点（最小堆应用）
	// 题目描述: 给定一个数组 points ，其中 points[i] = [xi, yi] 表示 X-Y 平面上的一个点，
	// 并且是一个整数 k ，返回离原点 (0,0) 最近的 k 个点。
	// 这里，平面上两点之间的距离是 欧几里德距离（√(x1^2 + y1^2)）。
	// 你可以按 任何顺序 返回答案。除了点坐标的顺序之外，答案确保是 唯一 的。
	// 链接: https://leetcode.cn/problems/k-closest-points-to-origin/
	public static int[][] kClosest(int[][] points, int k) {
		/**
		 * 最接近原点的K个点
		 * 
		 * @param points 点坐标数组
		 * @param k 需要返回的点数
		 * @return 离原点最近的K个点
		 * 
		 * 算法思路详解:
		 * 1. 数据结构选择：使用最大堆维护K个最近的点，
		 *    堆顶为距离最远的点
		 * 2. 距离计算优化：使用距离平方避免浮点数运算和开方操作
		 * 3. 堆维护策略：当堆大小超过K时，移除距离最远的点
		 * 4. 结果收集：从堆中取出K个点构成结果
		 * 
		 * 时间复杂度分析:
		 * - O(n*log(k))，其中n是点的数量
		 * - 每个点的处理：O(log(k))
		 * 空间复杂度分析:
		 * - O(k)，堆的大小始终为k
		 * 是否最优解: 是，这是解决K近邻问题的高效方法之一
		 *
		 * 工程化最佳实践:
		 * 1. 参数验证：严格检查输入参数的有效性
		 * 2. 边界处理：妥善处理k的边界情况
		 * 3. 计算优化：使用距离平方避免浮点运算
		 *
		 * 实际应用场景:
		 * 1. 机器学习：K近邻算法中的近邻点查找
		 * 2. 计算几何：最近点对问题
		 * 3. 地理信息系统：最近设施查找
		 * 4. 游戏开发：碰撞检测中的近邻物体查找
		 */
		// 异常处理：检查输入是否有效
		if (points == null || points.length == 0 || k <= 0 || k > points.length) {
			return new int[0][0];
		}
		
		// 使用最大堆维护K个最近的点
		// 堆中存储的是点的索引，比较的是点到原点的距离平方
		PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> {
			int distA = points[a][0] * points[a][0] + points[a][1] * points[a][1];
			int distB = points[b][0] * points[b][0] + points[b][1] * points[b][1];
			return distB - distA; // 最大堆，距离大的优先级高
		});
		
		// 将点加入堆中
		for (int i = 0; i < points.length; i++) {
			maxHeap.offer(i);
			// 如果堆的大小超过K，移除距离最远的点
			if (maxHeap.size() > k) {
				maxHeap.poll();
			}
		}
		
		// 收集结果
		int[][] result = new int[k][2];
		for (int i = 0; i < k; i++) {
			int index = maxHeap.poll();
			result[i][0] = points[index][0];
			result[i][1] = points[index][1];
		}
		
		return result;
	}
	
	// 辅助方法：找到数组中最小的K个元素
	public static int[] findKthSmallestElements(int[] nums, int k) {
		if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
			return new int[0];
		}
		
		// 使用最小堆
		PriorityQueue<Integer> minHeap = new PriorityQueue<>();
		for (int num : nums) {
			minHeap.offer(num);
		}
		
		int[] result = new int[k];
		for (int i = 0; i < k; i++) {
			result[i] = minHeap.poll();
		}
		
		return result;
	}
	
	// 辅助方法：找到数组中最大的K个元素
	public static int[] findKthLargestElements(int[] nums, int k) {
		if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
			return new int[0];
		}
		
		// 使用最大堆
		PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
		for (int num : nums) {
			maxHeap.offer(num);
		}
		
		int[] result = new int[k];
		for (int i = 0; i < k; i++) {
			result[i] = maxHeap.poll();
		}
		
		return result;
	}
	
	// 辅助方法：获取数组中最小的K个数（使用最大堆优化）
	public static int[] getLeastNumbers(int[] nums, int k) {
		if (nums == null || nums.length == 0 || k <= 0) {
			return new int[0];
		}
		
		if (k >= nums.length) {
			return nums;
		}
		
		// 使用最大堆维护K个最小元素
		PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
		
		// 前k个元素直接加入堆中
		for (int i = 0; i < k; i++) {
			maxHeap.offer(nums[i]);
		}
		
		// 对于剩下的元素，如果比堆顶元素小，则替换堆顶元素
		for (int i = k; i < nums.length; i++) {
			if (nums[i] < maxHeap.peek()) {
				maxHeap.poll();
				maxHeap.offer(nums[i]);
			}
		}
		
		// 收集结果
		int[] result = new int[k];
		for (int i = 0; i < k; i++) {
			result[i] = maxHeap.poll();
		}
		
		return result;
	}
}