package class094;

import java.util.Arrays;

// 排队接水 (Queue Water)
// 有 n 个人在一个水龙头前排队接水，假如每个人接水的时间为 Ti，
// 请编程找出这 n 个人排队的一种顺序，使得 n 个人的平均等待时间最小。
// 一个人的等待时间不包括他的接水时间。
// 如果两个人接水的时间相同，编号更小的人应当排在前面。
// 
// 算法标签: 贪心算法(Greedy Algorithm)、排序(Sorting)、平均等待时间优化(Average Waiting Time Optimization)
// 时间复杂度: O(n*log(n))，其中n是人数
// 空间复杂度: O(n)，存储人员信息数组
// 测试链接 : https://www.luogu.com.cn/problem/P1223
// 相关题目: 任务调度、进程调度优化问题
// 贪心算法专题 - 排队与调度问题集合
public class Code12_QueueWater {

	/*
	 * 算法思路详解：
	 * 1. 贪心策略核心：按接水时间升序排列人员，
	 *    接水时间短的人排在前面可以最小化后续人员的累计等待时间
	 * 2. 排序优化：通过排序预处理，将问题转化为确定性最优解
	 * 3. 等待时间计算：第i个人的等待时间等于前i-1个人接水时间之和
	 * 4. 平均时间统计：总等待时间除以人数得到平均等待时间
	 *
	 * 时间复杂度分析：
	 * - O(n*log(n))，其中n是人数
	 * - 排序阶段：O(n*log(n))
	 * - 等待时间计算：O(n)
	 * 空间复杂度分析：
	 * - O(n)，需要额外数组存储人员信息和结果
	 * 是否最优解：是，这是处理此类排队优化问题的最优解法
	 *
	 * 工程化最佳实践：
	 * 1. 输入验证：严格检查输入参数的有效性，防止空指针异常
	 * 2. 边界处理：妥善处理各种边界情况，如空数组、单人等
	 * 3. 性能优化：采用贪心策略避免穷举所有排列组合
	 * 4. 代码可读性：使用语义明确的变量名和详尽的注释
	 * 5. 结果精度：使用long类型避免整数溢出
	 *
	 * 极端场景与边界情况处理：
	 * 1. 空输入场景：times为空数组或null时返回默认结果
	 * 2. 单人场景：只有一个人时平均等待时间为0
	 * 3. 相同时间场景：多人接水时间相同时按编号排序
	 * 4. 有序序列场景：接水时间已排序的情况
	 * 5. 极值场景：接水时间差异极大的情况
	 *
	 * 跨语言实现差异与优化：
	 * 1. Java实现：使用Arrays.sort和自定义比较器
	 * 2. C++实现：使用std::sort和lambda表达式
	 * 3. Python实现：使用sorted函数和key参数
	 * 4. 内存管理：不同语言的垃圾回收机制对性能的影响
	 *
	 * 调试与测试策略：
	 * 1. 过程可视化：在关键节点打印当前排列顺序和等待时间
	 * 2. 断言验证：在排序后添加断言确保时间升序
	 * 3. 性能监控：跟踪排序和计算的实际执行时间
	 * 4. 边界测试：设计覆盖所有边界条件的测试用例
	 * 5. 压力测试：使用大规模数据验证算法稳定性
	 *
	 * 实际应用场景与拓展：
	 * 1. 操作系统：进程调度算法优化
	 * 2. 网络通信：数据包传输顺序优化
	 * 3. 生产制造：任务加工顺序优化
	 * 4. 服务行业：客户服务顺序优化
	 * 5. 交通运输：车辆通行顺序优化
	 *
	 * 算法深入解析：
	 * 1. 贪心策略原理：短作业优先(SJF)调度算法思想
	 * 2. 最优性证明：通过交换论证法可以证明贪心策略的正确性
	 * 3. 数学推导：平均等待时间 = Σ(前i-1个人接水时间)/n
	 * 4. 策略变体：可扩展为多服务台排队问题
	 */
	public static class Person {
		int id;      // 人员编号
		int time;    // 接水时间
		
		public Person(int id, int time) {
			this.id = id;
			this.time = time;
		}
	}
	
	public static double[] queueWater(int[] times) {
		// 异常处理：检查输入是否为空
		if (times == null || times.length == 0) {
			return new double[]{0.0};
		}
		
		// 边界条件：只有一个人
		if (times.length == 1) {
			return new double[]{1, 0.0};
		}
		
		int n = times.length;
		
		// 创建人员数组，保存编号和接水时间
		Person[] people = new Person[n];
		for (int i = 0; i < n; i++) {
			people[i] = new Person(i + 1, times[i]);  // 编号从1开始
		}
		
		// 按接水时间升序排序，时间相同时按编号升序排序
		Arrays.sort(people, (a, b) -> {
			if (a.time != b.time) {
				return a.time - b.time;
			}
			return a.id - b.id;
		});
		
		// 计算平均等待时间
		long totalWaitTime = 0;
		long waitTime = 0;
		
		// 计算每个人的等待时间
		for (int i = 0; i < n - 1; i++) {  // 最后一个人没有等待时间
			waitTime += people[i].time;
			totalWaitTime += waitTime;
		}
		
		// 返回排列顺序和平均等待时间
		double[] result = new double[n + 1];
		for (int i = 0; i < n; i++) {
			result[i] = people[i].id;
		}
		result[n] = (double) totalWaitTime / n;
		
		return result;
	}
	
	// 测试函数
	public static void main(String[] args) {
		// 测试用例1
		int[] times1 = {1, 2, 3};
		double[] result1 = queueWater(times1);
		System.out.print("测试用例1排列顺序: ");
		for (int i = 0; i < result1.length - 1; i++) {
			System.out.print((int) result1[i] + " ");
		}
		System.out.println();
		System.out.println("测试用例1平均等待时间: " + String.format("%.2f", result1[result1.length - 1])); // 期望输出: 1 2 3, 平均等待时间: 1.33
		
		// 测试用例2
		int[] times2 = {5, 1, 3, 2};
		double[] result2 = queueWater(times2);
		System.out.print("测试用例2排列顺序: ");
		for (int i = 0; i < result2.length - 1; i++) {
			System.out.print((int) result2[i] + " ");
		}
		System.out.println();
		System.out.println("测试用例2平均等待时间: " + String.format("%.2f", result2[result2.length - 1])); // 期望输出: 2 4 3 1, 平均等待时间: 2.75
		
		// 测试用例3：边界情况
		int[] times3 = {10};
		double[] result3 = queueWater(times3);
		System.out.print("测试用例3排列顺序: ");
		for (int i = 0; i < result3.length - 1; i++) {
			System.out.print((int) result3[i] + " ");
		}
		System.out.println();
		System.out.println("测试用例3平均等待时间: " + String.format("%.2f", result3[result3.length - 1])); // 期望输出: 1, 平均等待时间: 0.00
		
		// 测试用例4：相同时间
		int[] times4 = {3, 3, 3};
		double[] result4 = queueWater(times4);
		System.out.print("测试用例4排列顺序: ");
		for (int i = 0; i < result4.length - 1; i++) {
			System.out.print((int) result4[i] + " ");
		}
		System.out.println();
		System.out.println("测试用例4平均等待时间: " + String.format("%.2f", result4[result4.length - 1])); // 期望输出: 1 2 3, 平均等待时间: 4.00
	}
}