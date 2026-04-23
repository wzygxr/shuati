// 有向图的欧拉路径，增强注释版
// 图中有n个点，m条有向边，每条边给出两个端点
// 如果存在欧拉路径，输出字典序最小的结果，如果不存在打印No
// 1 <= n <= 10^5
// 1 <= m <= 2 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P7771
// 此版本包含详细的中文注释，解释算法逻辑和笔试面试要点

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

public class EulerPath_Directed_Graph_Enhanced {

	// 自定义边的比较器，用于对边按起点和终点排序，确保字典序最小
	// 笔试中需要掌握自定义排序方法，这是实现字典序最小的关键
	// 面试中可能问到：为什么要对边排序？答：为了保证输出的路径是字典序最小的
	// ML/DL关联：在图神经网络中，节点的访问顺序会影响信息聚合，排序策略影响模型效果
	public static class EdgeCmp implements Comparator<int[]> {
		@Override
		// 比较函数：先比较起点，再比较终点，实现字典序排序
		// 该比较逻辑是实现字典序最小的关键，面试需说明其原理
		public int compare(int[] e1, int[] e2) {
			return e1[0] != e2[0] ? (e1[0] - e2[0]) : (e1[1] - e2[1]);
		}
	}

	public static int MAXN = 100001; // 最大节点数，笔试中需根据数据范围调整，面试需说明数组大小设定原因
	public static int MAXM = 200002; // 最大边数，笔试中需根据数据范围调整，面试需说明数组大小设定原因
	public static int n, m; // n为节点数，m为边数，笔试中需注意变量含义
	public static int[][] edgeArr = new int[MAXM][2]; // 存储所有边的信息，[起点，终点]，ML中图数据的基础表示

	public static int[] head = new int[MAXN]; // 链式前向星的头节点数组，存储每个节点的第一条边，面试高频考察点
	public static int[] nxt = new int[MAXM]; // 链式前向星的next指针，连接同一起点的边，笔试需掌握图的存储方式
	public static int[] to = new int[MAXM]; // 链式前向星的目标节点，存储边的终点，图论基础数据结构
	public static int cntg; // 链式前向星的边计数器，用于分配边的唯一ID，面试需说明链式前向星原理

	public static int[] cur = new int[MAXN]; // 当前遍历的边指针，用于Hierholzer算法，避免重复访问边，算法核心变量
	public static int[] outDeg = new int[MAXN]; // 每个节点的出度，欧拉路径判定核心数据，面试高频考察点
	public static int[] inDeg = new int[MAXN]; // 每个节点的入度，欧拉路径判定核心数据，面试高频考察点

	public static int[] path = new int[MAXM]; // 存储欧拉路径的结果，可作为图的序列特征用于ML模型，面试需说明用途
	public static int cntp; // 路径节点计数器，记录路径长度，算法执行进度跟踪

	// 添加边的函数：使用链式前向星存储图结构
	// 笔试中需掌握链式前向星的实现，这是图论题目的基础数据结构
	// 面试中常问链式前向星与邻接矩阵、邻接表的区别：空间效率更高，适合稀疏图
	// ML/DL关联：图神经网络中邻接表的存储方式影响内存占用和计算效率
	public static void addEdge(int u, int v) {
		nxt[++cntg] = head[u]; // 新边的下一个边指向原来第一条边
		to[cntg] = v; // 新边的终点是v
		head[u] = cntg; // 头节点指向新边
	}

	// 构建图的邻接表表示，同时计算每个节点的入度和出度
	// 笔试中这是图论题目的基础步骤，面试需说明每个步骤的作用
	// ML/DL关联：图的预处理是图神经网络的输入准备步骤
	public static void connect() {
		// 首先对所有边按起点和终点排序，确保字典序最小
		Arrays.sort(edgeArr, 1, m + 1, new EdgeCmp());

		// 遍历排序后的边，构建邻接表并统计度数
		for (int l = 1, r = 1; l <= m; l = ++r) {
			// 找到所有具有相同起点的边的区间 [l, r]
			while (r + 1 <= m && edgeArr[l][0] == edgeArr[r + 1][0]) {
				r++;
			}
			// 将这些边逆序添加到邻接表中，这样遍历时是按字典序的
			for (int i = r, u, v; i >= l; i--) {
				u = edgeArr[i][0]; // 起点
				v = edgeArr[i][1]; // 终点
				outDeg[u]++; // u的出度加1
				inDeg[v]++; // v的入度加1
				addEdge(u, v); // 添加边(u,v)到邻接表
			}
		}
		// 初始化当前边指针，用于Hierholzer算法遍历
		for (int i = 1; i <= n; i++) {
			cur[i] = head[i];
		}
	}

	// 有向图中找到一个起点，去生成欧拉回路 或者 欧拉路径
	// 这是欧拉路径判定的核心函数，面试必考知识点
	// 判定定理：对于有向图，存在欧拉路径当且仅当：
	// 1. 除去起点和终点外，每个点的入度等于出度
	// 2. 起点的出度比入度多1，终点的入度比出度多1，或者所有点入度等于出度（欧拉回路）
	// ML/DL关联：图的结构性质分析有助于图神经网络的设计
	public static int directedStart() {
		int start = -1, end = -1; // start为起点，end为终点

		// 遍历所有节点，检查度数差值
		for (int i = 1; i <= n; i++) {
			int v = outDeg[i] - inDeg[i]; // 计算出度与入度的差值

			// 检查度数差值是否合法：
			// 差值只能是0（普通节点）、1（起点）、-1（终点）
			// 且最多只有一个起点和一个终点
			if (v < -1 || v > 1 || (v == 1 && start != -1) || (v == -1 && end != -1)) {
				return -1; // 不满足欧拉路径存在的条件
			}

			if (v == 1) {
				start = i; // 出度比入度多1的点是起点
			}
			if (v == -1) {
				end = i; // 入度比出度多1的点是终点
			}
		}

		// 检查起点和终点的配对情况
		// 要么都没有（欧拉回路），要么都存在（欧拉路径）
		if ((start == -1) ^ (end == -1)) { // 异或运算，要么都存在要么都不存在
			return -1; // 起点和终点不成对，不符合欧拉路径条件
		}

		// 如果存在欧拉路径，返回起点
		if (start != -1) {
			return start; // 欧拉路径的起点
		}

		// 如果是欧拉回路，从任意有出边的点开始
		for (int i = 1; i <= n; i++) {
			if (outDeg[i] > 0) {
				return i;
			}
		}

		return -1; // 没有找到合适的起点
	}

	// Hierholzer算法递归版，java会爆栈，C++可以通过
	// 该算法用于在已知存在欧拉路径的图中构造具体的路径
	// 时间复杂度O(E)，每条边只访问一次，面试高频考察点
	// 递归版本容易理解但可能导致栈溢出，面试需说明优缺点
	// ML/DL关联：该算法将图结构转换为序列，可用于序列模型输入
	public static void euler1(int u) {
		// 遍历当前节点u的所有未访问的出边
		for (int e = cur[u]; e > 0; e = cur[u]) {
			cur[u] = nxt[e]; // 移动到下一条边
			euler1(to[e]); // 递归访问目标节点
		}
		// 回溯时将当前节点加入路径（后序遍历）
		// 这是Hierholzer算法的关键：先走完所有子路径再记录当前节点
		path[++cntp] = u;
	}

	// Hierholzer算法迭代版（推荐使用）
	// 使用显式栈避免递归深度过大导致栈溢出
	// 笔试中遇到大数据量必须使用迭代版，面试常问递归vs迭代的选择
	// 时间复杂度O(E)，空间复杂度O(V)，适用于大规模图
	// ML/DL关联：迭代算法更适合GPU并行计算
	public static int[] sta = new int[MAXM]; // 显式栈，存储遍历路径
	public static int top; // 栈顶指针

	public static void euler2(int node) {
		top = 0; // 初始化栈
		sta[++top] = node; // 将起始节点压入栈

		while (top > 0) { // 当栈不为空时继续遍历
			int u = sta[top--]; // 弹出栈顶节点
			int e = cur[u]; // 获取当前节点的当前边

			if (e != 0) { // 如果还有未访问的边
				cur[u] = nxt[e]; // 移动到下一条边
				sta[++top] = u; // 将当前节点重新压入栈（待处理）
				sta[++top] = to[e]; // 将目标节点压入栈（待访问）
			} else { // 如果没有未访问的边
				path[++cntp] = u; // 将节点加入路径（已完成所有子路径）
			}
		}
	}

	// 主函数：程序入口
	// 笔试中需要熟悉FastReader等快速输入输出方式，提高程序效率
	// 面试中可能会问到整个算法流程的复杂度分析
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

		n = in.nextInt(); // 读取节点数
		m = in.nextInt(); // 读取边数

		// 读取所有边的信息
		for (int i = 1; i <= m; i++) {
			edgeArr[i][0] = in.nextInt(); // 起点
			edgeArr[i][1] = in.nextInt(); // 终点
		}

		connect(); // 构建图的邻接表表示
		int start = directedStart(); // 判断是否存在欧拉路径并找到起点

		if (start == -1) { // 不存在欧拉路径
			out.println("No");
		} else {
			// euler1(start); // 递归版（可能导致栈溢出）
			euler2(start); // 迭代版（推荐）

			// 检查是否所有边都被遍历（验证欧拉路径存在性）
			if (cntp != m + 1) { // 路径应该包含m+1个节点（m条边）
				out.println("No");
			} else {
				// 输出欧拉路径
				for (int i = cntp; i >= 1; i--) {
					out.print(path[i] + " ");
				}
				out.println();
			}
		}

		out.flush();
		out.close();
	}

	// 快速读入类，笔试必备技能
	// 面试中可能问到：为什么需要快速读入？答：普通Scanner效率低，大数据量会超时
	// ML/DL关联：高效的I/O处理在大数据处理中很重要
	static class FastReader {
		private final byte[] buffer = new byte[1 << 16]; // 缓冲区大小65536字节
		private int ptr = 0, len = 0;
		private final InputStream in;

		FastReader(InputStream in) {
			this.in = in;
		}

		private int readByte() throws IOException {
			if (ptr >= len) { // 缓冲区用完，重新读取
				len = in.read(buffer);
				ptr = 0;
				if (len <= 0) // 读取完毕
					return -1;
			}
			return buffer[ptr++]; // 返回当前字节并移动指针
		}

		int nextInt() throws IOException {
			int c;
			// 跳过空白字符
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);

			boolean neg = false; // 是否为负数
			if (c == '-') {
				neg = true;
				c = readByte();
			}

			int val = 0; // 数值
			// 读取数字字符
			while (c > ' ' && c != -1) {
				val = val * 10 + (c - '0'); // 转换字符为数字
				c = readByte();
			}
			return neg ? -val : val; // 返回正负值
		}
	}
}