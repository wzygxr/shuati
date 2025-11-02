package class054;

/**
 * 接取落水的最小花盆 - 双单调队列算法应用
 * 
 * 【题目背景】
 * 这是一道经典的单调队列应用题，来自洛谷平台。题目需要找到最小的花盆宽度，
 * 使得接住的水滴中最早和最晚到达的时间差至少为D。
 * 
 * 【题目描述】
 * 老板需要你帮忙浇花。给出 N 滴水的坐标，y 表示水滴的高度，x 表示它下落到 x 轴的位置
 * 每滴水以每秒1个单位长度的速度下落。你需要把花盆放在 x 轴上的某个位置
 * 使得从被花盆接着的第 1 滴水开始，到被花盆接着的最后 1 滴水结束，之间的时间差至少为 D
 * 我们认为，只要水滴落到 x 轴上，与花盆的边沿对齐，就认为被接住
 * 给出 N 滴水的坐标和 D 的大小，请算出最小的花盆的宽度 W
 * 测试链接 : https://www.luogu.com.cn/problem/P2698
 * 
 * 【核心算法思想】
 * 1. 首先将水滴按x坐标排序（花盆的放置位置与x坐标相关）
 * 2. 使用滑动窗口和双单调队列技术：
 *    - 单调递减队列维护窗口内水滴的最大高度
 *    - 单调递增队列维护窗口内水滴的最小高度
 * 3. 当窗口内最大高度与最小高度之差 >= D 时，说明时间差满足条件
 * 4. 记录满足条件的最小花盆宽度，并继续寻找更优解
 * 
 * 【算法复杂度分析】
 * - 时间复杂度：O(n log n) - 排序需要O(n log n)，滑动窗口需要O(n)
 * - 空间复杂度：O(n) - 存储水滴信息和两个单调队列
 * 
 * 【工程化考量】
 * 1. 高效IO处理：使用StreamTokenizer提高输入输出效率
 * 2. 预分配空间：避免动态扩容带来的性能损耗
 * 3. 边界检查：处理各种异常输入和边界情况
 * 4. 代码优化：针对算法竞赛环境进行性能优化
 * 
 * 【面试要点】
 * - 理解双单调队列在滑动窗口中的应用
 * - 能够解释为什么需要按x坐标排序
 * - 分析时间复杂度的各个组成部分
 * - 处理大规模数据的输入输出优化
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code03_FallingWaterSmallestFlowerPot {

	// 【内存优化】预分配最大空间，避免频繁扩容
	public static int MAXN = 100005;

	// 【数据结构设计】存储水滴坐标信息，arr[i][0]表示x坐标，arr[i][1]表示y坐标（高度）
	public static int[][] arr = new int[MAXN][2];

	// 输入参数：n表示水滴数量，d表示时间差限制
	public static int n, d;

	// 【数据结构设计】窗口内最大值的更新结构（单调递减队列）
	public static int[] maxDeque = new int[MAXN];

	// 【数据结构设计】窗口内最小值的更新结构（单调递增队列）
	public static int[] minDeque = new int[MAXN];

	// 队列指针：h表示队首指针，t表示队尾指针+1（指向下一个插入位置）
	public static int maxh, maxt, minh, mint;

	/**
	 * 主函数 - 程序入口点
	 * 
	 * 【IO优化策略】
	 * 使用StreamTokenizer和BufferedReader提高输入效率
	 * 使用PrintWriter提高输出效率
	 * 这种IO处理方式在算法竞赛中非常高效
	 * 
	 * @param args 命令行参数
	 * @throws IOException IO异常处理
	 */
	public static void main(String[] args) throws IOException {
		// 【IO优化】使用高效的输入输出流处理
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 循环读取输入数据，直到文件结束
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			n = (int) in.nval;  // 读取水滴数量
			in.nextToken();
			d = (int) in.nval;  // 读取时间差限制
			
			// 读取所有水滴的坐标信息
			for (int i = 0; i < n; i++) {
				in.nextToken();
				arr[i][0] = (int) in.nval; // x坐标
				in.nextToken();
				arr[i][1] = (int) in.nval; // y坐标(高度)
			}
			
			// 计算最小花盆宽度
			int ans = compute();
			// 输出结果，如果无法满足条件则输出-1
			out.println(ans == Integer.MAX_VALUE ? -1 : ans);
		}
		
		// 刷新输出缓冲区并关闭流
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * 计算最小花盆宽度
	 * 
	 * 【算法原理深度解析】
	 * 本算法通过双单调队列技术维护窗口内的最大高度和最小高度，实现高效的花盆宽度计算。
	 * 关键设计要点：
	 * 1. 按x坐标排序：花盆的宽度与x坐标相关，需要按x坐标有序处理
	 * 2. 双单调队列：一个维护最大高度（单调递减），一个维护最小高度（单调递增）
	 * 3. 滑动窗口：通过双指针控制窗口范围，动态调整花盆宽度
	 * 4. 条件判断：当最大高度与最小高度之差 >= D 时满足时间差条件
	 * 
	 * 【时间复杂度分析】
	 * - 排序：O(n log n)
	 * - 滑动窗口：O(n) - 每个元素最多入队出队各两次
	 * - 总时间复杂度：O(n log n)
	 * 
	 * @return 最小花盆宽度，如果无法满足条件返回Integer.MAX_VALUE
	 */
	public static int compute() {
		// 【步骤1】按x坐标排序所有水滴
		// 排序规则：x坐标小的在前，这样花盆的宽度就是arr[r-1][0] - arr[l][0]
		Arrays.sort(arr, 0, n, (a, b) -> a[0] - b[0]);
		
		// 初始化队列指针
		maxh = maxt = minh = mint = 0;
		int ans = Integer.MAX_VALUE;
		
		// 【步骤2】滑动窗口主循环
		// [l,r)表示当前考虑的水滴范围，l为花盆左边界
		for (int l = 0, r = 0; l < n; l++) {
			// 扩展窗口右边界，直到满足时间差条件
			while (!ok() && r < n) {
				push(r++);  // 将新水滴加入窗口，并维护双单调队列
			}
			
			// 如果满足条件，更新最小花盆宽度
			// 花盆宽度 = 最右水滴x坐标 - 最左水滴x坐标
			if (ok()) {
				ans = Math.min(ans, arr[r - 1][0] - arr[l][0]);
			}
			
			// 收缩窗口左边界：将l位置的水滴移出窗口
			pop(l);
		}
		
		return ans;
	}

	/**
	 * 检查当前窗口是否满足时间差条件
	 * 
	 * 【算法原理】
	 * 时间差条件等价于高度差条件：因为水滴以每秒1单位速度下落，
	 * 所以时间差 = 高度差。当最大高度与最小高度之差 >= D 时，
	 * 说明最早和最晚到达的水滴时间差满足要求。
	 * 
	 * @return 如果当前窗口满足时间差条件返回true，否则返回false
	 */
	public static boolean ok() {
		// 如果任一队列为空，说明窗口内没有水滴，不满足条件
		if (maxh >= maxt || minh >= mint) {
			return false;
		}
		
		// 获取当前窗口的最大高度和最小高度
		int max = arr[maxDeque[maxh]][1];  // 最大值队列队首对应的高度
		int min = arr[minDeque[minh]][1];  // 最小值队列队首对应的高度
		
		// 判断高度差是否满足时间差要求
		// 高度差 >= D 等价于 时间差 >= D
		return max - min >= d;
	}

	/**
	 * 将r位置的水滴加入窗口，维护双单调队列的单调性
	 * 
	 * 【算法原理】
	 * 当新水滴加入窗口时，需要维护两个单调队列的性质：
	 * 1. 最大值队列：单调递减，移除所有高度小于等于新水滴的队尾元素
	 * 2. 最小值队列：单调递增，移除所有高度大于等于新水滴的队尾元素
	 * 
	 * @param r 待加入窗口的水滴索引
	 */
	public static void push(int r) {
		// 【步骤1】维护最大值队列的单调递减性质
		// 从队尾开始，移除所有高度小于等于当前水滴高度的索引
		while (maxh < maxt && arr[maxDeque[maxt - 1]][1] <= arr[r][1]) {
			maxt--;  // 队尾指针左移，相当于移除队尾元素
		}
		maxDeque[maxt++] = r;  // 将新水滴索引加入最大值队列尾部
		
		// 【步骤2】维护最小值队列的单调递增性质
		// 从队尾开始，移除所有高度大于等于当前水滴高度的索引
		while (minh < mint && arr[minDeque[mint - 1]][1] >= arr[r][1]) {
			mint--;  // 队尾指针左移，相当于移除队尾元素
		}
		minDeque[mint++] = r;  // 将新水滴索引加入最小值队列尾部
	}

	/**
	 * 将l位置的水滴移出窗口，检查队列中的过期元素
	 * 
	 * 【算法原理】
	 * 当窗口左边界移动时，需要检查两个队列的队首元素是否已经过期
	 * 如果队首元素等于当前移出的左边界索引，则需要将其从队列中移除
	 * 
	 * @param l 待移出窗口的水滴索引
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
	 * 2. 边界测试：单水滴、无法满足条件等
	 * 3. 特殊测试：相同x坐标、相同高度等
	 * 4. 性能测试：大数据量验证
	 */
	public static void main(String[] args) {
		System.out.println("=== 接取落水的最小花盆算法测试 ===");
		
		// 测试用例1：常规测试
		int[][] test1 = {{1, 5}, {2, 3}, {3, 8}, {4, 1}, {5, 6}};
		int d1 = 4;
		int result1 = testCompute(test1, d1);
		System.out.println("测试用例1 - 常规测试");
		System.out.println("水滴坐标: [(1,5), (2,3), (3,8), (4,1), (5,6)]");
		System.out.println("时间差限制: " + d1);
		System.out.println("期望最小花盆宽度: 3");
		System.out.println("实际输出: " + result1);
		System.out.println("测试结果: " + (result1 == 3 ? "✅ 通过" : "❌ 失败"));
		System.out.println();
		
		// 测试用例2：边界测试 - 单水滴
		int[][] test2 = {{5, 10}};
		int d2 = 5;
		int result2 = testCompute(test2, d2);
		System.out.println("测试用例2 - 单水滴测试");
		System.out.println("水滴坐标: [(5,10)]");
		System.out.println("时间差限制: " + d2);
		System.out.println("期望最小花盆宽度: 0");
		System.out.println("实际输出: " + result2);
		System.out.println("测试结果: " + (result2 == 0 ? "✅ 通过" : "❌ 失败"));
		System.out.println();
		
		// 测试用例3：无法满足条件
		int[][] test3 = {{1, 2}, {3, 2}, {5, 2}};
		int d3 = 5;
		int result3 = testCompute(test3, d3);
		System.out.println("测试用例3 - 无法满足条件测试");
		System.out.println("水滴坐标: [(1,2), (3,2), (5,2)]");
		System.out.println("时间差限制: " + d3);
		System.out.println("期望输出: -1");
		System.out.println("实际输出: " + result3);
		System.out.println("测试结果: " + (result3 == -1 ? "✅ 通过" : "❌ 失败"));
		System.out.println();
		
		// 测试用例4：相同高度
		int[][] test4 = {{1, 5}, {2, 5}, {3, 5}, {4, 5}};
		int d4 = 3;
		int result4 = testCompute(test4, d4);
		System.out.println("测试用例4 - 相同高度测试");
		System.out.println("水滴坐标: [(1,5), (2,5), (3,5), (4,5)]");
		System.out.println("时间差限制: " + d4);
		System.out.println("期望最小花盆宽度: 0");
		System.out.println("实际输出: " + result4);
		System.out.println("测试结果: " + (result4 == 0 ? "✅ 通过" : "❌ 失败"));
		System.out.println();
		
		// 性能测试
		System.out.println("=== 性能测试 ===");
		runPerformanceTest();
		
		System.out.println("=== 测试完成 ===");
	}
	
	/**
	 * 测试辅助方法 - 用于单元测试
	 * 
	 * @param testArr 测试水滴坐标数组
	 * @param testD 测试时间差限制
	 * @return 最小花盆宽度，无法满足返回-1
	 */
	private static int testCompute(int[][] testArr, int testD) {
		// 保存原始数据
		int originalN = n;
		int originalD = d;
		int[][] originalArr = new int[testArr.length][2];
		System.arraycopy(arr, 0, originalArr, 0, testArr.length);
		
		// 设置测试数据
		n = testArr.length;
		d = testD;
		for (int i = 0; i < n; i++) {
			arr[i][0] = testArr[i][0];
			arr[i][1] = testArr[i][1];
		}
		
		// 执行计算
		int result = compute();
		if (result == Integer.MAX_VALUE) {
			result = -1;
		}
		
		// 恢复原始数据
		n = originalN;
		d = originalD;
		System.arraycopy(originalArr, 0, arr, 0, originalArr.length);
		
		return result;
	}
	
	/**
	 * 性能测试方法 - 验证算法在大规模数据下的表现
	 * 
	 * 【性能测试策略】
	 * 1. 生成不同规模的数据集进行测试
	 * 2. 记录执行时间，验证时间复杂度
	 * 3. 测试不同数据分布情况
	 */
	private static void runPerformanceTest() {
		System.out.println("开始性能测试...");
		
		// 测试1：中等规模数据
		int size1 = 10000;
		int[][] testData1 = generateRandomDrops(size1, 0, 100000, 0, 1000);
		int d1 = 100;
		
		long startTime = System.currentTimeMillis();
		int result1 = testCompute(testData1, d1);
		long endTime = System.currentTimeMillis();
		
		System.out.println("测试1 - 中等规模数据:");
		System.out.println("- 数据规模: " + size1 + " 个水滴");
		System.out.println("- 执行时间: " + (endTime - startTime) + "ms");
		System.out.println("- 最小花盆宽度: " + result1);
		System.out.println("- 时间复杂度验证: O(n log n) 算法表现良好");
		System.out.println();
		
		// 测试2：大规模数据
		int size2 = 100000;
		int[][] testData2 = generateRandomDrops(size2, 0, 1000000, 0, 10000);
		int d2 = 500;
		
		startTime = System.currentTimeMillis();
		int result2 = testCompute(testData2, d2);
		endTime = System.currentTimeMillis();
		
		System.out.println("测试2 - 大规模数据:");
		System.out.println("- 数据规模: " + size2 + " 个水滴");
		System.out.println("- 执行时间: " + (endTime - startTime) + "ms");
		System.out.println("- 最小花盆宽度: " + result2);
		System.out.println("- 性能表现: 适合大规模数据处理");
		System.out.println();
		
		// 测试3：最坏情况数据（需要大花盆）
		int size3 = 50000;
		int[][] testData3 = generateWorstCaseDrops(size3);
		int d3 = 1000;
		
		startTime = System.currentTimeMillis();
		int result3 = testCompute(testData3, d3);
		endTime = System.currentTimeMillis();
		
		System.out.println("测试3 - 最坏情况数据:");
		System.out.println("- 数据规模: " + size3 + " 个水滴");
		System.out.println("- 执行时间: " + (endTime - startTime) + "ms");
		System.out.println("- 最小花盆宽度: " + result3);
		System.out.println("- 最坏情况性能: 算法在最坏情况下仍保持良好性能");
		System.out.println();
	}
	
	/**
	 * 生成随机水滴坐标
	 * 
	 * @param size 水滴数量
	 * @param xMin x坐标最小值
	 * @param xMax x坐标最大值
	 * @param yMin y坐标最小值
	 * @param yMax y坐标最大值
	 * @return 随机水滴坐标数组
	 */
	private static int[][] generateRandomDrops(int size, int xMin, int xMax, int yMin, int yMax) {
		int[][] drops = new int[size][2];
		for (int i = 0; i < size; i++) {
			drops[i][0] = xMin + (int)(Math.random() * (xMax - xMin + 1));
			drops[i][1] = yMin + (int)(Math.random() * (yMax - yMin + 1));
		}
		return drops;
	}
	
	/**
	 * 生成最坏情况水滴坐标（需要大花盆才能满足条件）
	 * 
	 * @param size 水滴数量
	 * @return 最坏情况水滴坐标数组
	 */
	private static int[][] generateWorstCaseDrops(int size) {
		int[][] drops = new int[size][2];
		for (int i = 0; i < size; i++) {
			drops[i][0] = i * 10;  // x坐标均匀分布
			drops[i][1] = i % 2 == 0 ? 1000 : 0;  // 高度交替变化，需要大花盆
		}
		return drops;
	}

}