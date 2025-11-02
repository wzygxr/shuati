package class107;

import java.util.*;
import java.io.*;

// 蓄水池采样
// 假设有一个不停吐出球的机器，每次吐出1号球、2号球、3号球...
// 有一个袋子只能装下10个球，每次机器吐出的球，要么放入袋子，要么永远扔掉
// 如何做到机器吐出每一个球之后，所有吐出的球都等概率被放进袋子里

/**
 * 蓄水池采样算法详解：
 * 
 * 算法目标：从一个未知大小的数据流中随机选取k个元素，使得每个元素被选中的概率相等。
 * 
 * 算法原理：
 * 1. 保存前k个元素
 * 2. 对于第i个元素(i>k)，以k/i的概率决定是否将其加入蓄水池
 * 3. 如果决定加入，则随机替换蓄水池中的一个元素
 * 
 * 算法正确性证明：
 * 对于第i个元素，被选中的概率是k/i
 * 对于前k个元素中的任意一个，在第i轮不被替换的概率是：
 * 1. 第i个元素不被选中：(i-k)/i
 * 2. 第i个元素被选中但没有替换到当前元素：k/i * (k-1)/k = (k-1)/i
 * 所以当前元素在第i轮仍被保留的概率是：(i-k)/i + (k-1)/i = (i-1)/i
 * 
 * 最终每个元素被选中的概率为：k/k * k/(k+1) * (k+1)/(k+2) * ... * (n-1)/n = k/n
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(k)
 * 
 * 应用场景：
 * 1. 数据流处理（如网络数据包采样）
 * 2. 大数据集采样（无法完全加载到内存）
 * 3. 链表随机节点选择
 * 4. 文件行随机采样
 * 5. 推荐系统中的随机采样
 * 6. 分布式系统中的数据采样
 */

public class Code01_ReservoirSampling {

	public static class Pool {

		private int size;

		public int[] bag;

		public Pool(int s) {
			size = s;
			bag = new int[s];
		}

		// 是否要i号球
		// size/i的几率决定要
		// 剩下的几率决定不要
		private boolean pick(int i) {
			return (int) (Math.random() * i) < size;
		}

		// 袋子里0...size-1个位置
		// 哪个空间的球扔掉，让i号球进来
		private int where() {
			return (int) (Math.random() * size);
		}

		public void enter(int i) {
			if (i <= size) {
				bag[i - 1] = i;
			} else {
				if (pick(i)) {
					bag[where()] = i;
				}
			}
		}

		public int[] getBag() {
			return bag;
		}

	}

	/**
	 * LeetCode 382. 链表随机节点
	 * 题目描述：给定一个单链表，随机选择链表的一个节点，并返回相应的节点值。每个节点被选中的概率一样。
	 * 
	 * 解题思路：使用蓄水池采样算法，k=1的情况
	 * 1. 保存第一个节点的值
	 * 2. 遍历后续节点，对于第i个节点，以1/i的概率决定是否替换结果
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(1)
	 */
	static class ListNode {
		int val;
		ListNode next;
		ListNode() {}
		ListNode(int val) { this.val = val; }
		ListNode(int val, ListNode next) { this.val = val; this.next = next; }
	}
	
	static class SolutionLinkedList {
		private ListNode head;
		
		public SolutionLinkedList(ListNode head) {
			this.head = head;
		}
		
		public int getRandom() {
			// 蓄水池采样 k=1
			ListNode current = head;
			int result = current.val;
			int count = 1;
			
			while (current != null) {
				// 以 1/count 的概率选择当前节点
				if (Math.random() < 1.0 / count) {
					result = current.val;
				}
				count++;
				current = current.next;
			}
			
			return result;
		}
	}

	/**
	 * LeetCode 398. 随机数索引
	 * 题目描述：给定一个可能含有重复元素的整数数组，随机输出给定目标数字的索引。
	 * 
	 * 解题思路：使用蓄水池采样算法
	 * 1. 遍历数组，找到所有等于target的元素
	 * 2. 对于第k个等于target的元素，以1/k的概率决定是否选择它作为结果
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(1)
	 */
	static class SolutionRandomPickIndex {
		private int[] nums;
		
		public SolutionRandomPickIndex(int[] nums) {
			this.nums = nums;
		}
		
		public int pick(int target) {
			int result = -1;
			int count = 0;
			
			for (int i = 0; i < nums.length; i++) {
				if (nums[i] == target) {
					count++;
					// 以 1/count 的概率选择当前索引
					if (Math.random() < 1.0 / count) {
						result = i;
					}
				}
			}
			
			return result;
		}
	}

	/**
	 * 扩展题目：LeetCode 710. 黑名单中的随机数
	 * 题目描述：给定一个包含 [0，n) 中不重复整数的黑名单 blacklist ，
	 * 写一个函数，从 [0, n - 1] 范围内的任意整数中选取一个不在黑名单 blacklist 中的随机整数。
	 * 要求每个有效整数被选中的概率相等。
	 * 
	 * 解题思路：将黑名单映射到白名单的末尾
	 * 时间复杂度：O(B) 初始化，O(1) 每次查询，其中 B 是黑名单的大小
	 * 空间复杂度：O(B)
	 */
	static class SolutionBlacklistRandom {
		private int size; // 白名单的大小
		private Map<Integer, Integer> mapping; // 黑名单映射
		private Random random;
		
		public SolutionBlacklistRandom(int n, int[] blacklist) {
			random = new Random();
			size = n - blacklist.length;
			mapping = new HashMap<>();
			
			// 将黑名单中的元素添加到集合中
			Set<Integer> blackSet = new HashSet<>();
			for (int b : blacklist) {
				blackSet.add(b);
			}
			
			// 映射黑名单中的元素到白名单末尾的可用元素
			int last = n - 1;
			for (int b : blacklist) {
				// 如果b已经在末尾区域，不需要映射
				if (b >= size) {
					continue;
				}
				// 找到末尾区域的白名单元素
				while (blackSet.contains(last)) {
					last--;
				}
				mapping.put(b, last--);
			}
		}
		
		public int pick() {
			int index = random.nextInt(size);
			// 如果索引在映射中，返回映射的值
			return mapping.getOrDefault(index, index);
		}
	}
	
	/**
	 * 扩展题目：从大文件中随机选择k行
	 * 问题描述：给定一个非常大的文件，无法完全加载到内存，如何随机选择k行？
	 * 
	 * 解题思路：使用标准的蓄水池采样算法
	 * 时间复杂度：O(n)，其中n是文件的行数
	 * 空间复杂度：O(k)
	 */
	static class FileLineSampler {
		public List<String> sampleLines(String filePath, int k) throws IOException {
			List<String> reservoir = new ArrayList<>(k);
			try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
				String line;
				int i = 0;
				Random random = new Random();
				
				// 先填充前k行
				while ((line = reader.readLine()) != null && i < k) {
					reservoir.add(line);
					i++;
				}
				
				// 对后续的行进行采样
				while ((line = reader.readLine()) != null) {
					i++;
					int j = random.nextInt(i); // 0到i-1的随机数
					if (j < k) {
						reservoir.set(j, line);
					}
				}
			}
			return reservoir;
		}
	}
	
	/**
	 * 扩展题目：数据流中随机采样k个元素
	 * 问题描述：实现一个从无限长的数据流中随机选择k个元素的算法
	 * 
	 * 解题思路：标准的蓄水池采样算法
	 * 时间复杂度：O(n)，其中n是已处理的元素数量
	 * 空间复杂度：O(k)
	 */
	static class DataStreamSampler {
		private int[] reservoir;
		private int k;
		private int count;
		private Random random;
		
		public DataStreamSampler(int k) {
			this.k = k;
			this.reservoir = new int[k];
			this.count = 0;
			this.random = new Random();
		}
		
		public void add(int value) {
			if (count < k) {
				reservoir[count] = value;
			} else {
				// 以k/count的概率选择当前元素
				int j = random.nextInt(count + 1);
				if (j < k) {
					reservoir[j] = value;
				}
			}
			count++;
		}
		
		public int[] getSample() {
			return reservoir;
		}
	}
	
	/**
	 * 扩展题目：加权随机采样
	 * 问题描述：从一个加权集合中随机选择一个元素，选择的概率与元素的权重成正比
	 * 
	 * 解题思路：使用别名方法（Alias Method）或随机采样方法
	 * 时间复杂度：O(n) 每次查询
	 * 空间复杂度：O(1)
	 */
	static class WeightedSampler {
		private int[] nums;
		private int[] weights;
		private Random random;
		private int totalWeight;
		
		public WeightedSampler(int[] nums, int[] weights) {
			this.nums = nums;
			this.weights = weights;
			this.random = new Random();
			
			// 计算总权重
			totalWeight = 0;
			for (int w : weights) {
				totalWeight += w;
			}
		}
		
		public int pickIndex() {
			int rand = random.nextInt(totalWeight) + 1; // 1到totalWeight的随机数
			int sum = 0;
			
			for (int i = 0; i < weights.length; i++) {
				sum += weights[i];
				if (rand <= sum) {
					return nums[i];
				}
			}
			
			return nums[0]; // 理论上不会执行到这里
		}
	}
	
	/**
	 * 单元测试辅助方法：验证采样的等概率性
	 */
	private static void validateUniformity(int[] results, int n, int expectedCount) {
		Map<Integer, Integer> countMap = new HashMap<>();
		for (int result : results) {
			countMap.put(result, countMap.getOrDefault(result, 0) + 1);
		}
		
		System.out.println("采样均匀性分析:");
		for (int i = 0; i < n; i++) {
			int actual = countMap.getOrDefault(i, 0);
			// 允许5%的误差
			boolean withinRange = Math.abs(actual - expectedCount) <= expectedCount * 0.05;
			System.out.printf("元素 %d: 期望=%d, 实际=%d, %s\n", 
					i, expectedCount, actual, withinRange ? "通过" : "不通过");
		}
	}

	public static void main(String[] args) {
		System.out.println("=== 基础蓄水池采样测试 ===");
		System.out.println("测试开始");
		int n = 41; // 一共吐出多少球
		int m = 10; // 袋子大小多少
		int testTimes = 10000; // 进行多少次实验
		int[] cnt = new int[n + 1];
		for (int k = 0; k < testTimes; k++) {
			Pool pool = new Pool(m);
			for (int i = 1; i <= n; i++) {
				pool.enter(i);
			}
			int[] bag = pool.getBag();
			for (int num : bag) {
				cnt[num]++;
			}
		}
		System.out.println("机器吐出到" + n + "号球, " + "袋子大小为" + m);
		System.out.println("每个球被选中的概率应该接近" + (double) m / n);
		System.out.println("一共测试" + testTimes + "次");
		for (int i = 1; i <= n; i++) {
			System.out.println(i + "被选中次数 : " + cnt[i] + ", 被选中概率 : " + (double) cnt[i] / testTimes);
		}
		System.out.println("测试结束");
		
		System.out.println("\n=== LeetCode 382. 链表随机节点测试 ===");
		// 构造链表 1->2->3->4->5
		ListNode head = new ListNode(1);
		head.next = new ListNode(2);
		head.next.next = new ListNode(3);
		head.next.next.next = new ListNode(4);
		head.next.next.next.next = new ListNode(5);
		
		SolutionLinkedList solution = new SolutionLinkedList(head);
		System.out.println("随机选择10次链表节点:");
		for (int i = 0; i < 10; i++) {
			System.out.println("选中节点值: " + solution.getRandom());
		}
		
		System.out.println("\n=== LeetCode 398. 随机数索引测试 ===");
		int[] nums = {1, 2, 3, 3, 3};
		SolutionRandomPickIndex solution2 = new SolutionRandomPickIndex(nums);
		System.out.println("随机选择目标数字3的索引10次:");
		for (int i = 0; i < 10; i++) {
			System.out.println("选中索引: " + solution2.pick(3));
		}
		
		System.out.println("\n=== LeetCode 710. 黑名单中的随机数测试 ===");
		int n710 = 10;
		int[] blacklist = {2, 3, 5};
		SolutionBlacklistRandom solution710 = new SolutionBlacklistRandom(n710, blacklist);
		System.out.println("随机选择10次不在黑名单中的数:");
		for (int i = 0; i < 10; i++) {
			System.out.println("选中数字: " + solution710.pick());
		}
		
		System.out.println("\n=== 数据流随机采样测试 ===");
		DataStreamSampler sampler = new DataStreamSampler(5);
		for (int i = 1; i <= 100; i++) {
			sampler.add(i);
		}
		System.out.println("从100个元素中随机采样5个:");
		for (int num : sampler.getSample()) {
			System.out.print(num + " ");
		}
		System.out.println();
		
		System.out.println("\n=== 加权随机采样测试 ===");
		int[] weightedNums = {1, 2, 3};
		int[] weights = {1, 2, 3}; // 权重分别为1,2,3
		WeightedSampler weightedSampler = new WeightedSampler(weightedNums, weights);
		int[] weightedResults = new int[10000];
		for (int i = 0; i < 10000; i++) {
			weightedResults[i] = weightedSampler.pickIndex();
		}
		System.out.println("权重为[1,2,3]的元素采样结果统计:");
		Map<Integer, Integer> weightedCount = new HashMap<>();
		for (int result : weightedResults) {
			weightedCount.put(result, weightedCount.getOrDefault(result, 0) + 1);
		}
		for (Map.Entry<Integer, Integer> entry : weightedCount.entrySet()) {
			System.out.printf("元素 %d: 被选中%d次, 概率%.4f\n", 
					entry.getKey(), entry.getValue(), entry.getValue() / 10000.0);
		}
		
		System.out.println("\n=== 算法时间复杂度分析 ===");
		System.out.println("基础蓄水池采样: O(n) 时间, O(k) 空间");
		System.out.println("链表随机节点: O(n) 时间, O(1) 空间");
		System.out.println("随机数索引: O(n) 时间, O(1) 空间");
		System.out.println("黑名单随机数: O(B) 初始化, O(1) 查询, O(B) 空间");
		System.out.println("加权随机采样: O(n) 查询, O(1) 空间");
		
		System.out.println("\n=== 工程化建议 ===");
		System.out.println("1. 在实际应用中，注意随机数生成器的种子设置，避免产生可预测的随机序列");
		System.out.println("2. 对于大数据集，考虑使用分布式版本的蓄水池采样算法");
		System.out.println("3. 在高并发场景下，注意随机数生成器的线程安全性问题");
		System.out.println("4. 对于性能要求高的场景，可以考虑使用更高效的随机数生成算法");
	}

}