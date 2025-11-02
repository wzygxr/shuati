package class094;

import java.util.Arrays;
import java.util.PriorityQueue;

// 雇佣K名工人的最低成本
// 有n名工人，给定两个数组quality和wage
// 其中quality[i]表示第i名工人的工作质量，其最低期望工资为wage[i]
// 现在我们想雇佣k名工人组成一个工资组
// 在雇佣一组k名工人时，我们必须按照下述规则向他们支付工资：
// 对工资组中的每名工人，应当按其工作质量与同组其他工人的工作质量的比例来支付工资
// 工资组中的每名工人至少应当得到他们的最低期望工资
// 给定整数k，返回组成满足上述条件的付费群体所需的最小金额
// 测试链接 : https://leetcode.cn/problems/minimum-cost-to-hire-k-workers/

/*
 * 题目解析：
 * 这是一个优化问题，要求在满足约束条件下找到雇佣k名工人的最低成本。
 * 关键约束条件：
 * 1. 工资按工作质量比例分配
 * 2. 每名工人至少得到最低期望工资
 * 3. 需要雇佣恰好k名工人
 *
 * 解题思路：
 * 1. 关键洞察：在一组工人中，实际支付比例必须是这组工人中最大的比例
 * 2. 贪心策略：枚举每个工人作为基准，选择质量总和最小的k个工人
 * 3. 使用优先队列维护最优解
 * 4. 遍历所有可能的组合找到最小成本
 */
public class Code04_MinimumCostToHireWorkers {

	public static class Employee {
		public double ratio; // 薪水 / 质量的比例
		public int quality;

		public Employee(double r, int q) {
			ratio = r;
			quality = q;
		}
	}

	/*
	 * 算法思路：
	 * 1. 关键洞察：在一组工人中，实际支付比例必须是这组工人中最大的比例（以满足最低期望工资要求）
	 * 2. 贪心策略：
	 *    - 按照薪水/质量比例排序
	 *    - 枚举每个工人作为基准（具有最大比例）
	 *    - 在该工人之前选择质量总和最小的k-1个工人
	 * 3. 使用最大堆维护当前质量最小的k个工人
	 * 4. 遍历排序后的工人，维护堆并计算最小成本
	 *
	 * 时间复杂度：O(n * logn) - 主要是排序和堆操作的复杂度
	 * 空间复杂度：O(n) - 存储员工数组和堆
	 * 是否最优解：是，这是处理此类问题的最优解法
	 *
	 * 工程化考量：
	 * 1. 异常处理：检查输入是否为空或长度不一致
	 * 2. 边界条件：处理k大于n等特殊情况
	 * 3. 性能优化：使用最大堆维护最小质量总和
	 * 4. 可读性：清晰的变量命名和注释
	 *
	 * 算法详解：
	 * 1. 比例计算：计算每个工人的薪水/质量比例
	 * 2. 排序策略：按比例升序排列
	 * 3. 基准选择：枚举每个工人作为支付比例基准
	 * 4. 质量优化：使用最大堆维护最小质量总和
	 * 5. 成本计算：总质量 × 基准比例
	 */
	public static double mincostToHireWorkers(int[] quality, int[] wage, int k) {
		// 异常处理：检查输入是否为空或长度不一致
		if (quality == null || wage == null || quality.length != wage.length) {
			return 0;
		}
		
		int n = quality.length;
		
		// 边界条件：k大于n
		if (k > n) {
			return 0;
		}
		
		// 创建员工数组，存储每个员工的比例和质量
		Employee[] employees = new Employee[n];
		for (int i = 0; i < n; i++) {
			// 计算薪水/质量比例，用于后续排序和成本计算
			employees[i] = new Employee((double) wage[i] / quality[i], quality[i]);
		}
		
		// 根据比例排序，比例小的在前，比例大的在后
		// 使用自定义比较器实现升序排列
		Arrays.sort(employees, (a, b) -> a.ratio <= b.ratio ? -1 : 1);
		
		// 大根堆，用来收集最小的前k个质量数值
		// 使用自定义比较器 (a, b) -> b - a 实现最大堆
		PriorityQueue<Integer> heap = new PriorityQueue<Integer>((a, b) -> b - a);
		
		// qualitySum：堆中k个员工的质量总和
		int qualitySum = 0;
		// ans：记录最小成本，初始化为最大值
		double ans = Double.MAX_VALUE;
		
		// 遍历排序后的员工数组
		for (int i = 0, curQuality; i < n; i++) {
			curQuality = employees[i].quality; // 当前员工的质量
			
			if (heap.size() < k) { // 堆没满，直接添加
				qualitySum += curQuality; // 累加质量
				heap.add(curQuality);     // 添加到堆中
				
				// 当堆大小等于k时，计算当前成本
				if (heap.size() == k) {
					// 成本 = 质量总和 × 当前员工的比例（最大比例）
					ans = Math.min(ans, qualitySum * employees[i].ratio);
				}
			} else { // 堆满了，需要判断是否替换
				// 如果当前员工质量小于堆顶质量，则替换
				if (heap.peek() > curQuality) {
					// 更新质量总和：减去堆顶质量，加上当前质量
					qualitySum += curQuality - heap.poll();
					// 添加当前员工质量到堆中
					heap.add(curQuality);
					// 计算并更新最小成本
					ans = Math.min(ans, qualitySum * employees[i].ratio);
				}
			}
		}
		
		return ans; // 返回最小成本
	}
	
	/*
	 * 相关题目1: LeetCode 215. 数组中的第K个最大元素
	 * 题目链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
	 * 题目描述: 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
	 * 请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
	 * 解题思路: 使用最小堆维护最大的k个元素
	 * 时间复杂度: O(n log k)
	 * 空间复杂度: O(k)
	 * 是否最优解: 是，这是处理此类问题的最优解法
	 *
	 * 算法详解：
	 * 1. 数据结构：使用大小为k的最小堆
	 * 2. 维护策略：堆中始终保存最大的k个元素
	 * 3. 替换条件：当新元素大于堆顶时替换
	 * 4. 结果获取：堆顶即为第k大元素
	 */
	public static int findKthLargest(int[] nums, int k) {
		// 最小堆，维护最大的k个元素
		// 堆的大小始终保持为k，堆顶是最小的元素
		PriorityQueue<Integer> minHeap = new PriorityQueue<>();
		
		// 遍历数组中的每个元素
		for (int num : nums) {
			if (minHeap.size() < k) {
				// 堆未满，直接添加元素
				minHeap.offer(num);
			} else if (num > minHeap.peek()) {
				// 堆已满且当前元素大于堆顶元素
				// 移除堆顶（最小元素），添加当前元素
				minHeap.poll();
				minHeap.offer(num);
			}
		}
		
		// 返回堆顶元素，即第k大元素
		return minHeap.peek();
	}
	
	/*
	 * 相关题目2: LeetCode 703. 数据流中的第K大元素
	 * 题目链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
	 * 题目描述: 设计一个找到数据流中第 k 大元素的类（class）。
	 * 注意是排序后的第 k 大元素，不是第 k 个不同的元素。
	 * 请实现 KthLargest 类：
	 * KthLargest(int k, int[] nums) 使用整数 k 和整数流 nums 初始化对象。
	 * int add(int val) 将 val 插入数据流 nums 后，返回当前数据流中第 k 大的元素。
	 * 解题思路: 使用最小堆维护最大的k个元素
	 * 时间复杂度: 初始化O(n log k)，add操作O(log k)
	 * 空间复杂度: O(k)
	 * 是否最优解: 是，这是处理此类问题的最优解法
	 *
	 * 算法详解：
	 * 1. 类设计：封装k值和最小堆
	 * 2. 初始化：处理初始数据流
	 * 3. 动态更新：通过add方法处理新元素
	 * 4. 结果获取：堆顶即为第k大元素
	 */
	public static class KthLargest {
		private int k; // 第k大元素的k值
		private PriorityQueue<Integer> minHeap; // 最小堆，维护最大的k个元素
		
		public KthLargest(int k, int[] nums) {
			this.k = k;
			// 最小堆，维护最大的k个元素
			// 堆的大小始终保持为k，堆顶是最小的元素
			this.minHeap = new PriorityQueue<>();
			
			// 初始化时处理初始数据流
			for (int num : nums) {
				add(num); // 调用add方法处理每个元素
			}
		}
		
		public int add(int val) {
			// 添加新元素到数据流
			if (minHeap.size() < k) {
				// 堆未满，直接添加元素
				minHeap.offer(val);
			} else if (val > minHeap.peek()) {
				// 堆已满且当前元素大于堆顶元素
				// 移除堆顶（最小元素），添加当前元素
				minHeap.poll();
				minHeap.offer(val);
			}
			
			// 返回当前数据流中第k大元素
			return minHeap.peek();
		}
	}
}