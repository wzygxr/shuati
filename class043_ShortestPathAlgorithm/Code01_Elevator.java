package class143;

/**
 * 跳楼机问题 - 同余最短路算法应用
 * 
 * 问题描述：
 * 一座大楼一共有h层，楼层编号1~h，有如下四种移动方式：
 * 1. 向上移动x层
 * 2. 向上移动y层
 * 3. 向上移动z层
 * 4. 回到1层
 * 假设你正在第1层，请问大楼里有多少楼层你可以到达
 * 
 * 输入约束：
 * 1 <= h <= 2^63 - 1
 * 1 <= x、y、z <= 10^5
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P3403
 * 提交时请把类名改成"Main"，可以通过所有测试用例
 * 
 * 核心算法：同余最短路 + Dijkstra算法
 * 算法思想：将问题转化为图论问题，在模x意义下构建最短路图
 * 
 * 时间复杂度：O(x * log x)
 * 空间复杂度：O(x)
 * 
 * 工程化考量：
 * 1. 异常处理：处理输入边界值，确保算法鲁棒性
 * 2. 内存优化：使用链式前向星存储图结构，减少内存占用
 * 3. 性能优化：使用优先队列实现Dijkstra算法，保证时间复杂度
 * 4. 可读性：详细注释和模块化设计
 * 
 * 面试要点：
 * - 理解同余最短路的核心思想：将无限状态空间转化为有限状态
 * - 掌握Dijkstra算法在特殊图结构中的应用
 * - 能够分析算法的时间复杂度和空间复杂度
 * - 了解算法在工程实践中的优化策略
 */

/*
 * 算法思路：
 * 这道题可以转化为图论问题，用Dijkstra算法解决。
 * 将楼层按照模x的值进行分类，构建模x意义下的最短路图。
 * 每个点i表示模x余数为i的所有楼层中到达1层需要的最小步数。
 * 通过y和z操作在不同余数之间建立边，权值为y和z。
 * 最后统计所有可达楼层的数量。
 * 
 * 时间复杂度：O(x * log x)
 * 空间复杂度：O(x)
 * 
 * 题目来源：洛谷P3403 跳楼机 (https://www.luogu.com.cn/problem/P3403)
 * 相关题目：
 * 1. POJ 2387 Til the Cows Come Home - Dijkstra模板题 (http://poj.org/problem?id=2387)
 * 2. Codeforces 20C Dijkstra? - 最短路径模板题 (https://codeforces.com/problemset/problem/20/C)
 * 3. LeetCode 743 Network Delay Time - 网络延迟时间 (https://leetcode.cn/problems/network-delay-time/)
 * 4. 洛谷 P4779 单源最短路径 (https://www.luogu.com.cn/problem/P4779)
 * 5. HDU 2544 最短路 (http://acm.hdu.edu.cn/showproblem.php?pid=2544)
 * 6. AtCoder ABC176_D Wizard in Maze (https://atcoder.jp/contests/abc176/tasks/abc176_d)
 * 7. SPOJ KATHTHI (https://www.spoj.com/problems/KATHTHI/)
 * 8. LeetCode 1368 Minimum Cost to Make at Least One Valid Path in a Grid (https://leetcode.cn/problems/minimum-cost-to-make-at-least-one-valid-path-in-a-grid/)
 * 9. Codeforces 590C Three States (https://codeforces.com/contest/590/problem/C)
 * 10. UVA 11573 Ocean Currents (https://vjudge.net/problem/UVA-11573)
 * 11. LeetCode 2290 Minimum Obstacle Removal to Reach Corner (https://leetcode.cn/problems/minimum-obstacle-removal-to-reach-corner/)
 * 12. LeetCode 1824 Minimum Sideway Jumps (https://leetcode.cn/problems/minimum-sideway-jumps/)
 * 13. LeetCode 1631 Path With Minimum Effort (https://leetcode.cn/problems/path-with-minimum-effort/)
 * 14. LeetCode 847 Shortest Path Visiting All Nodes (https://leetcode.cn/problems/shortest-path-visiting-all-nodes/)
 * 15. LeetCode 773 Sliding Puzzle (https://leetcode.cn/problems/sliding-puzzle/)
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Code01_Elevator {

	// 常量定义 - 根据题目约束设置数组大小
	public static final int MAXN = 100001;  // 最大节点数，对应x的最大值10^5
	public static final int MAXM = 200001;  // 最大边数，每个节点最多2条边

	// 输入参数
	public static long h;  // 楼层高度，注意h可能很大(2^63-1)
	public static int x, y, z;  // 三种移动步长

	// 链式前向星存储图结构 - 内存优化设计
	// 优点：节省内存，适合稀疏图；缺点：访问不如邻接矩阵直观
	public static int[] head = new int[MAXN];  // 每个节点的第一条边索引
	public static int[] next = new int[MAXM];  // 下一条边的索引
	public static int[] to = new int[MAXM];    // 边的终点节点
	public static long[] weight = new long[MAXM]; // 边的权重
	public static int cnt;  // 边的计数器，从1开始

	// Dijkstra算法数据结构
	// 优先队列：存储(节点编号, 距离)对，按距离从小到大排序
	// 注意：使用long[]数组避免对象创建开销
	public static PriorityQueue<long[]> heap = new PriorityQueue<>((a, b) -> {
		// 自定义比较器：按距离升序排列
		// 注意：避免使用a[1] - b[1]可能溢出，使用比较运算符
		if (a[1] < b[1]) return -1;
		if (a[1] > b[1]) return 1;
		return 0;
	});

	// 距离数组：记录从起点到每个节点的最短距离
	public static long[] distance = new long[MAXN];
	// 访问标记数组：避免重复处理节点
	public static boolean[] visited = new boolean[MAXN];

	/**
	 * 初始化函数 - 准备算法运行环境
	 * 工程化考量：
	 * 1. 重置所有数据结构状态，确保多次调用不会相互影响
	 * 2. 使用Arrays.fill高效初始化数组，避免循环开销
	 * 3. 只初始化需要使用的部分(0到x-1)，提高效率
	 * 
	 * 异常场景：
	 * - 如果x > MAXN，会抛出数组越界异常
	 * - 需要确保x在合理范围内(1 <= x <= 10^5)
	 */
	public static void prepare() {
		cnt = 1;  // 边计数器从1开始（0表示空）
		heap.clear();  // 清空优先队列
		Arrays.fill(head, 0, x, 0);  // 初始化头指针数组
		Arrays.fill(distance, 0, x, Long.MAX_VALUE);  // 距离初始化为无穷大
		Arrays.fill(visited, 0, x, false);  // 访问标记初始化为false
	}

	/**
	 * 添加边到图中 - 链式前向星实现
	 * 
	 * @param u 边的起点节点
	 * @param v 边的终点节点  
	 * @param w 边的权重（移动步长）
	 * 
	 * 工程化考量：
	 * 1. 使用头插法，新边插入链表头部，提高插入效率
	 * 2. 边计数器cnt从1开始，避免与0（空指针）混淆
	 * 3. 支持动态添加边，适合图结构构建
	 * 
	 * 算法细节：
	 * - 每条边存储为：起点u -> 终点v，权重w
	 * - 通过head[u]指向u的第一条边，next数组形成链表
	 * - 这种存储方式适合稀疏图，节省内存空间
	 */
	public static void addEdge(int u, int v, long w) {
		next[cnt] = head[u];  // 新边的next指向当前头边
		to[cnt] = v;         // 设置边的终点
		weight[cnt] = w;     // 设置边的权重
		head[u] = cnt++;      // 更新头指针，计数器递增
	}

	/**
	 * Dijkstra算法实现 - 单源最短路径算法
	 * 
	 * 算法思想：贪心策略，每次选择距离起点最近的未访问节点进行松弛操作
	 * 
	 * 时间复杂度：O(x * log x) - 每个节点入队出队一次，优先队列操作log x
	 * 空间复杂度：O(x) - 距离数组和访问标记数组
	 * 
	 * 工程化考量：
	 * 1. 使用优先队列优化，避免O(x^2)的朴素实现
	 * 2. 惰性删除：已访问节点继续留在队列中，通过visited标记跳过
	 * 3. 使用long[]数组而非对象，减少内存分配开销
	 * 
	 * 算法正确性保证：
	 * - 非负权边：移动步长y,z均为正数，满足Dijkstra算法前提
	 * - 最优子结构：最短路径的子路径也是最短路径
	 * 
	 * 调试技巧：
	 * - 打印中间变量：可添加System.out.println输出关键变量值
	 * - 边界测试：测试x=1, y=z=1等边界情况
	 */
	public static void dijkstra() {
		heap.add(new long[] { 0, 0 });  // 起点(0,0)：节点0，距离0
		distance[0] = 0;  // 起点到自身的距离为0
		
		long[] cur;  // 当前处理的节点信息
		int u;       // 当前节点编号
		long w;      // 当前节点到起点的距离
		
		while (!heap.isEmpty()) {
			cur = heap.poll();  // 取出距离最小的节点
			u = (int) cur[0];   // 节点编号
			w = cur[1];         // 当前距离
			
			// 惰性删除：如果节点已被访问过，跳过处理
			if (visited[u]) {
				continue;
			}
			
			visited[u] = true;  // 标记节点为已访问
			
			// 遍历当前节点的所有邻接边
			for (int ei = head[u], v; ei > 0; ei = next[ei]) {
				v = to[ei];  // 邻接节点
				
				// 松弛操作：如果通过u到达v的路径更短，则更新距离
				if (!visited[v] && distance[v] > w + weight[ei]) {
					distance[v] = w + weight[ei];  // 更新最短距离
					heap.add(new long[] { v, distance[v] });  // 新距离入队
				}
			}
		}
	}

	/**
	 * 计算结果 - 统计可达楼层数量
	 * 
	 * 数学原理：
	 * 对于每个余数i，如果从起点到i的最短距离为d，那么所有满足以下条件的楼层k都可到达：
	 * k ≡ i (mod x) 且 k >= d
	 * 这样的楼层数量为：floor((h - d) / x) + 1
	 * 
	 * 时间复杂度：O(x) - 遍历所有余数
	 * 空间复杂度：O(1) - 仅使用常数空间
	 * 
	 * 工程化考量：
	 * 1. 处理大数运算：h可能达到2^63-1，使用long类型避免溢出
	 * 2. 边界处理：确保d <= h时才进行计算
	 * 3. 数学公式验证：通过小例子验证公式正确性
	 * 
	 * 异常场景：
	 * - 如果h < 0，结果可能为负数（但题目约束h>=1）
	 * - 如果x=0，会出现除零异常（但题目约束x>=1）
	 */
	public static long compute() {
		dijkstra();  // 先执行Dijkstra算法计算最短距离
		long ans = 0;  // 可达楼层总数
		
		// 遍历所有余数类（模x的剩余类）
		for (int i = 0; i < x; i++) {
			if (distance[i] <= h) {  // 如果该余数类的最小距离不超过h
				// 计算该余数类中可达楼层的数量
				// 公式：(h - d) / x + 1，表示从d开始，每隔x层有一个可达楼层
				ans += (h - distance[i]) / x + 1;
			}
		}
		
		return ans;
	}

	/**
	 * 主函数 - 程序入口点
	 * 
	 * 执行流程：
	 * 1. 读取输入参数：h, x, y, z
	 * 2. 初始化算法数据结构
	 * 3. 构建图结构：添加y和z操作对应的边
	 * 4. 执行Dijkstra算法计算最短距离
	 * 5. 统计并输出可达楼层数量
	 * 
	 * 工程化考量：
	 * 1. 输入验证：确保参数在合理范围内（题目已约束）
	 * 2. 资源管理：使用try-with-resources或显式关闭IO流
	 * 3. 异常处理：捕获可能的IO异常
	 * 4. 性能优化：使用高效的IO类（Kattio）
	 * 
	 * 测试用例设计：
	 * - 边界测试：h=1, x=y=z=1
	 * - 大数测试：h接近2^63-1
	 * - 特殊测试：x=y=z的情况
	 * - 随机测试：随机生成参数验证正确性
	 */
	public static void main(String[] args) throws IOException {
		Kattio io = new Kattio();  // 使用高效IO类
		
		// 读取输入参数，注意h需要减1（因为题目中楼层从1开始）
		h = io.nextLong() - 1;  // 最大可达楼层高度
		x = io.nextInt();       // 第一种移动步长
		y = io.nextInt();       // 第二种移动步长
		z = io.nextInt();       // 第三种移动步长
		
		// 验证输入参数范围（虽然题目有约束，但工程上应该验证）
		if (h < 0 || x <= 0 || y <= 0 || z <= 0) {
			throw new IllegalArgumentException("输入参数不合法");
		}
		
		prepare();  // 初始化算法数据结构
		
		// 构建图结构：每个节点i通过y和z操作连接到(i+y)%x和(i+z)%x
		// 这体现了同余最短路的核心思想：在模x意义下构建状态转移图
		for (int i = 0; i < x; i++) {
			addEdge(i, (i + y) % x, y);  // 添加y操作边
			addEdge(i, (i + z) % x, z);  // 添加z操作边
		}
		
		// 计算并输出结果
		io.println(compute());
		io.flush();    // 确保输出被刷新
		io.close();    // 关闭IO资源
	}

	// Kattio类IO效率很好，但还是不如StreamTokenizer
	// 只有StreamTokenizer无法正确处理时，才考虑使用这个类
	// 参考链接 : https://oi-wiki.org/lang/java-pro/
	public static class Kattio extends PrintWriter {
		private BufferedReader r;
		private StringTokenizer st;

		public Kattio() {
			this(System.in, System.out);
		}

		public Kattio(InputStream i, OutputStream o) {
			super(o);
			r = new BufferedReader(new InputStreamReader(i));
		}

		public Kattio(String intput, String output) throws IOException {
			super(output);
			r = new BufferedReader(new FileReader(intput));
		}

		public String next() {
			try {
				while (st == null || !st.hasMoreTokens())
					st = new StringTokenizer(r.readLine());
				return st.nextToken();
			} catch (Exception e) {
			}
			return null;
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

		public double nextDouble() {
			return Double.parseDouble(next());
		}

		public long nextLong() {
			return Long.parseLong(next());
		}
	}

}