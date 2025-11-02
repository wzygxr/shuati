package class089;

import java.util.PriorityQueue;

/**
 * 连接棒材的最低费用 - 贪心算法 + 最小堆解决方案（LeetCode版本）
 * 
 * 题目描述：
 * 你有一些长度为正整数的棍子，这些长度以数组sticks的形式给出
 * sticks[i]是第i个木棍的长度
 * 你可以通过支付x+y的成本将任意两个长度为x和y的棍子连接成一个棍子
 * 你必须连接所有的棍子，直到剩下一个棍子
 * 返回以这种方式将所有给定的棍子连接成一个棍子的最小成本
 * 
 * 测试链接：https://leetcode.cn/problems/minimum-cost-to-connect-sticks/
 * 
 * 算法思想：
 * 贪心算法 + 最小堆（优先队列）
 * 1. 使用最小堆存储所有棍子的长度
 * 2. 每次从堆中取出两根最短的棍子进行连接
 * 3. 将连接后的新棍子放回堆中
 * 4. 重复直到只剩下一根棍子
 * 
 * 时间复杂度分析：
 * O(n*logn) - 每个棍子进出堆一次需要O(logn)，总共需要n-1次连接操作
 * 
 * 空间复杂度分析：
 * O(n) - 堆的大小为n
 * 
 * 是否为最优解：
 * 是，这是解决该问题的最优解（哈夫曼编码思想）
 * 
 * 工程化考量：
 * 1. 边界条件处理：处理空数组、单个棍子等特殊情况
 * 2. 输入验证：检查棍子长度是否为正整数
 * 3. 异常处理：对非法输入进行检查
 * 4. 可读性：使用清晰的变量命名和详细的注释
 * 
 * 贪心策略证明：
 * 这是经典的哈夫曼编码问题，每次选择最小的两个元素合并可以保证总成本最小
 * 这种策略满足贪心选择性质和最优子结构性质
 */
public class Code06_MinimumCostToConnectSticks1 {

	/**
	 * 计算连接所有棍子的最小成本
	 * 
	 * @param sticks 棍子长度数组
	 * @return 最小连接成本
	 * 
	 * 算法步骤：
	 * 1. 将棍子长度加入最小堆
	 * 2. 当堆中棍子数量大于1时：
	 *    - 取出两根最短的棍子
	 *    - 计算连接成本并累加
	 *    - 将连接后的新棍子放回堆中
	 * 3. 返回总成本
	 */
	public static int connectSticks(int[] sticks) {
		// 输入验证
		if (sticks == null || sticks.length == 0) {
			return 0;
		}
		
		// 单个棍子不需要连接
		if (sticks.length == 1) {
			return 0;
		}
		
		// 最小堆，存储棍子长度
		PriorityQueue<Integer> heap = new PriorityQueue<>();
		for (int stick : sticks) {
			// 验证棍子长度有效性
			if (stick <= 0) {
				throw new IllegalArgumentException("棍子长度必须为正整数");
			}
			heap.add(stick);
		}
		
		int totalCost = 0;  // 总连接成本
		
		// 当堆中还有多于一根棍子时继续连接
		while (heap.size() > 1) {
			// 取出两根最短的棍子
			int first = heap.poll();
			int second = heap.poll();
			
			// 计算连接成本
			int cost = first + second;
			totalCost += cost;
			
			// 将连接后的新棍子放回堆中
			heap.add(cost);
		}
		
		return totalCost;
	}

	/**
	 * 调试版本：打印计算过程中的中间结果
	 * 
	 * @param sticks 棍子长度数组
	 * @return 最小连接成本
	 */
	public static int debugConnectSticks(int[] sticks) {
		if (sticks == null || sticks.length == 0) {
			System.out.println("空数组，不需要连接");
			return 0;
		}
		
		if (sticks.length == 1) {
			System.out.println("单个棍子，不需要连接");
			return 0;
		}
		
		System.out.println("原始棍子长度: ");
		for (int i = 0; i < sticks.length; i++) {
			System.out.print(sticks[i] + " ");
		}
		System.out.println();
		
		PriorityQueue<Integer> heap = new PriorityQueue<>();
		for (int stick : sticks) {
			heap.add(stick);
		}
		
		int totalCost = 0;
		int step = 1;
		
		System.out.println("连接过程:");
		while (heap.size() > 1) {
			System.out.println("步骤 " + step + ":");
			
			// 取出两根最短的棍子
			int first = heap.poll();
			int second = heap.poll();
			System.out.println("  取出两根最短的棍子: " + first + " 和 " + second);
			
			// 计算连接成本
			int cost = first + second;
			totalCost += cost;
			System.out.println("  连接成本: " + first + " + " + second + " = " + cost);
			System.out.println("  累计总成本: " + totalCost);
			
			// 将连接后的新棍子放回堆中
			heap.add(cost);
			System.out.println("  将新棍子(" + cost + ")放回堆中");
			
			// 打印当前堆的状态
			System.out.print("  当前堆中棍子: ");
			PriorityQueue<Integer> temp = new PriorityQueue<>(heap);
			while (!temp.isEmpty()) {
				System.out.print(temp.poll() + " ");
			}
			System.out.println();
			
			step++;
		}
		
		System.out.println("最终结果: 最小连接成本 = " + totalCost);
		return totalCost;
	}

	/**
	 * 测试函数：验证连接棒材算法的正确性
	 */
	public static void testConnectSticks() {
		System.out.println("连接棒材的最低费用算法测试开始");
		System.out.println("============================");
		
		// 测试用例1: [2,4,3]
		int[] sticks1 = {2, 4, 3};
		int result1 = connectSticks(sticks1);
		System.out.println("输入: [2,4,3]");
		System.out.println("输出: " + result1);
		System.out.println("预期: 14");
		System.out.println((result1 == 14 ? "✓ 通过" : "✗ 失败"));
		System.out.println();
		
		// 测试用例2: [1,8,3,5]
		int[] sticks2 = {1, 8, 3, 5};
		int result2 = connectSticks(sticks2);
		System.out.println("输入: [1,8,3,5]");
		System.out.println("输出: " + result2);
		System.out.println("预期: 30");
		System.out.println((result2 == 30 ? "✓ 通过" : "✗ 失败"));
		System.out.println();
		
		// 测试用例3: 空数组
		int[] sticks3 = {};
		int result3 = connectSticks(sticks3);
		System.out.println("输入: []");
		System.out.println("输出: " + result3);
		System.out.println("预期: 0");
		System.out.println((result3 == 0 ? "✓ 通过" : "✗ 失败"));
		System.out.println();
		
		// 测试用例4: 单个棍子
		int[] sticks4 = {5};
		int result4 = connectSticks(sticks4);
		System.out.println("输入: [5]");
		System.out.println("输出: " + result4);
		System.out.println("预期: 0");
		System.out.println((result4 == 0 ? "✓ 通过" : "✗ 失败"));
		System.out.println();
		
		System.out.println("测试结束");
	}

	/**
	 * 性能测试：测试算法在大规模数据下的表现
	 */
	public static void performanceTest() {
		System.out.println("性能测试开始");
		System.out.println("============");
		
		// 生成大规模测试数据
		int n = 10000;
		int[] sticks = new int[n];
		for (int i = 0; i < n; i++) {
			sticks[i] = (int)(Math.random() * 1000) + 1;  // 1-1000的随机数
		}
		
		long startTime = System.currentTimeMillis();
		int result = connectSticks(sticks);
		long endTime = System.currentTimeMillis();
		
		System.out.println("数据规模: " + n + " 根棍子");
		System.out.println("执行时间: " + (endTime - startTime) + " 毫秒");
		System.out.println("最小连接成本: " + result);
		System.out.println("性能测试结束");
	}

	/**
	 * 主函数：运行测试
	 */
	public static void main(String[] args) {
		System.out.println("连接棒材的最低费用 - 贪心算法 + 最小堆解决方案（LeetCode版本）");
		System.out.println("======================================================");
		
		// 运行基础测试
		testConnectSticks();
		
		System.out.println("调试模式示例:");
		int[] debugSticks = {2, 4, 3};
		System.out.println("对测试用例 [2,4,3] 进行调试跟踪:");
		int debugResult = debugConnectSticks(debugSticks);
		System.out.println("最终结果: " + debugResult);
		
		System.out.println("算法分析:");
		System.out.println("- 时间复杂度: O(n*logn) - 每个棍子进出堆一次需要O(logn)，总共需要n-1次连接操作");
		System.out.println("- 空间复杂度: O(n) - 堆的大小为n");
		System.out.println("- 贪心策略: 每次选择最短的两根棍子进行连接（哈夫曼编码思想）");
		System.out.println("- 最优性: 这种贪心策略能够得到全局最优解");
		System.out.println("- 数学原理: 这是经典的哈夫曼编码问题");
		
		// 可选：运行性能测试
		// System.out.println("性能测试:");
		// performanceTest();
	}
}
