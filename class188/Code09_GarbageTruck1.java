// 垃圾车，java版
// 一共有n个点，m条无向边，所有点不保证连通
// 每条边的属性为 u v s t : u和v是端点，s是初始状态，t是最终状态
// 状态的数值只有0和1两种，当一辆车通过一条无向边，那么状态会翻转
// 一辆车的路线中，可以指定一个起点，最终回到起点，沿途的其他点不能重复经过
// 所有的边都要达成最终状态，所以需要若干辆车来完成这个目标
// 如果存在方案，提供任何一种方案即可，首先打印需要几辆车
// 然后对每辆车，先打印通过的边数，再打印依次到达了哪些点
// 如果不存在方案，打印"NIE"
// 1 <= n <= 10^5    1 <= m <= 10^6
// 测试链接 : https://www.luogu.com.cn/problem/P3520
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code09_GarbageTruck1 {

	// 最大节点数常量，根据题目限制设置
	public static int MAXN = 100001;
	// 最大边数常量，根据题目限制设置
	public static int MAXM = 2000001;
	// 节点数和边数
	public static int n, m;

	// 邻接表头指针数组，用于链式前向星存储图
	public static int[] head = new int[MAXN];
	// 邻接表边的下一个指针数组，用于链式前向星
	public static int[] nxt = new int[MAXM];
	// 邻接表边的目标节点数组，用于链式前向星
	public static int[] to = new int[MAXM];
	// 边ID数组，用于追踪原始边
	public static int[] eid = new int[MAXM];
	// 图的边计数器，用于链式前向星的边编号
	public static int cntg;

	// 节点度数数组，deg[i]表示节点i的度数
	public static int[] deg = new int[MAXN];
	// 当前节点的当前边指针数组，用于Hierholzer算法
	public static int[] cur = new int[MAXN];

	// 区分多个连通区
	public static boolean[] visNode = new boolean[MAXN];

	// 标记无向边是否已经使用
	public static boolean[] visEdge = new boolean[MAXM];

	// 路径标记数组，标记节点是否在当前路径中
	public static boolean[] inpath = new boolean[MAXN];
	// 欧拉路径结果数组，存储最终的路径节点
	public static int[] path = new int[MAXM];
	// 路径节点计数器，记录路径中节点的数量
	public static int cntp;

	// 答案数组，存储所有垃圾车路径的节点
	public static int[] ansArr = new int[MAXM];
	// 每个垃圾车路径的起始位置
	public static int[] ansl = new int[MAXM];
	// 每个垃圾车路径的结束位置
	public static int[] ansr = new int[MAXM];
	// 答案数组的索引
	public static int idx;
	// 垃圾车数量计数器
	public static int cnta;

	// 添加边到链式前向星图结构中
	// 功能：向无向图中添加边
	// 面试考点：链式前向星存储结构
	// 边界条件：处理重边
	// ML/DL关联：图的邻接表表示
	public static void addEdge(int u, int v, int id) {
		// 将新边插入到邻接表的头部，形成链表结构
		nxt[++cntg] = head[u];
		// to[cntg]记录新边的目标节点
		to[cntg] = v;
		// eid[cntg]记录新边对应的原始边ID
		eid[cntg] = id;
		// head[u]更新为新边的索引
		head[u] = cntg;
	}

	// 弹出环形路径
	// 功能：从路径中提取一个环并存储到答案数组中
	// 面试考点：欧拉路径分解为环
	// 边界条件：确保路径形成闭环
	// ML/DL关联：图的环分解算法
	public static void popCircle(int u) {
		// 增加垃圾车数量计数
		cnta++;
		// 将当前节点加入答案数组
		ansArr[++idx] = u;
		// 记录当前环的起始位置
		ansl[cnta] = idx;
		// 从路径栈中弹出节点，直到遇到起点u
		for (int pop = path[cntp--]; pop != u; pop = path[cntp--]) {
			// 将弹出的节点从路径中标记为不在路径中
			inpath[pop] = false;
			// 将弹出的节点加入答案数组
			ansArr[++idx] = pop;
		}
		// 将起点u从路径中标记为不在路径中
		inpath[u] = false;
		// 将起点u再次加入答案数组（形成闭环）
		ansArr[++idx] = u;
		// 记录当前环的结束位置
		ansr[cnta] = idx;
	}

	// 递归版Hierholzer算法
	// 功能：寻找欧拉路径/回路
	// 面试考点：Hierholzer算法递归实现
	// 边界条件：处理孤立点和不连通图
	// ML/DL关联：图遍历算法在路径规划中的应用
	public static void euler1(int u) {
		// 标记当前节点已访问
		visNode[u] = true;
		// 遍历从节点u出发的所有未访问边
		for (int e = cur[u]; e > 0; e = cur[u]) {
			// 更新当前边指针，跳过已访问的边
			cur[u] = nxt[e];
			// 检查当前边是否已被访问
			if (!visEdge[eid[e]]) {
				// 标记当前边为已访问
				visEdge[eid[e]] = true;
				// 递归访问下一个节点
				euler1(to[e]);
			}
		}
		// 如果当前节点已在路径中，则弹出环
		if (inpath[u]) {
			// 弹出包含当前节点的环
			popCircle(u);
		}
		// 将当前节点标记为在路径中
		inpath[u] = true;
		// 将当前节点加入路径
		path[++cntp] = u;
	}

	// 用于Hierholzer算法迭代版本的栈
	// 功能：避免递归深度过大导致栈溢出
	// 面试考点：显式栈的使用替代递归
	// 边界条件：栈溢出风险控制
	// ML/DL关联：循环神经网络中的显式状态管理
	public static int[] sta = new int[MAXM];
	// 栈顶指针，用于Hierholzer算法迭代版本
	// 功能：栈的基本操作
	// 面试考点：指针管理技巧
	// ML/DL关联：状态栈在序列建模中的作用
	public static int top;

	// 迭代版Hierholzer算法
	// 功能：非递归方式实现欧拉路径，适用于大图
	// 面试考点：递归转迭代技巧、显式栈管理
	// 边界条件：栈溢出风险控制
	// ML/DL关联：内存受限环境下的图遍历优化
	public static void euler2(int node) {
		// 初始化栈，将起始节点压入栈
		top = 0;
		sta[++top] = node;
		// 当栈不为空时继续处理
		while (top > 0) {
			// 取栈顶节点
			int u = sta[top--];
			// 标记当前节点已访问
			visNode[u] = true;
			// 获取当前节点的当前边
			int e = cur[u];
			// 如果还有未访问的边
			if (e != 0) {
				// 更新当前边指针
				cur[u] = nxt[e];
				// 检查当前边是否已被访问
				if (!visEdge[eid[e]]) {
					// 标记当前边为已访问
					visEdge[eid[e]] = true;
					// 将当前节点重新压入栈（稍后需要输出）
					sta[++top] = u;
					// 将下一个节点压入栈
					sta[++top] = to[e];
				} else {
					// 如果边已被访问，将当前节点重新压入栈
					sta[++top] = u;
				}
			} else {
				// 如果没有未访问的边
				// 如果当前节点已在路径中，则弹出环
				if (inpath[u]) {
					// 弹出包含当前节点的环
					popCircle(u);
				}
				// 将当前节点标记为在路径中
				inpath[u] = true;
				// 将当前节点加入路径
				path[++cntp] = u;
			}
		}
	}

	// 主函数：读取输入、处理数据、输出结果
	// 功能：解决垃圾车问题，通过欧拉回路找到最优路径
	// 面试考点：欧拉回路应用、图论建模
	// 边界条件：处理不连通图
	// ML/DL关联：路径优化问题
	public static void main(String[] args) throws Exception {
		// 创建快速读取器，提高IO效率
		FastReader in = new FastReader(System.in);
		// 创建打印写入器，用于格式化输出
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		// 读取节点数和边数
		n = in.nextInt();
		m = in.nextInt();
		// 读取所有边的信息
		for (int i = 1, u, v, s, t; i <= m; i++) {
			// 读取边的两个端点和初始状态、最终状态
			u = in.nextInt();
			v = in.nextInt();
			s = in.nextInt();
			t = in.nextInt();
			// 如果初始状态和最终状态不同，需要改变状态
			if (s != t) {
				// 更新两个端点的度数
				deg[u]++;
				deg[v]++;
				// 向图中添加双向边
				addEdge(u, v, i);
				addEdge(v, u, i);
			}
		}
		// 初始化每个节点的当前边指针
		for (int i = 1; i <= n; i++) {
			// cur[i]初始化为head[i]，表示从第一条边开始访问
			cur[i] = head[i];
		}
		// 检查是否存在解（所有节点的度数必须为偶数）
		boolean check = true;
		for (int i = 1; i <= n; i++) {
			// 使用位运算检查度数是否为奇数
			if ((deg[i] & 1) == 1) {
				// 如果存在度数为奇数的节点，无解
				check = false;
				break;
			}
		}
		// 如果不存在解
		if (!check) {
			// 输出"NIE"
			out.println("NIE");
		} else {
			// 对每个未访问的连通分量执行欧拉回路算法
			for (int i = 1; i <= n; i++) {
				// 如果节点未被访问
				if (!visNode[i]) {
					// euler1(i); // 递归版（可能栈溢出）
					// 使用迭代版欧拉路径算法
					euler2(i);
				}
			}
			// 输出垃圾车数量
			out.println(cnta);
			// 输出每辆垃圾车的路径
			for (int i = 1; i <= cnta; i++) {
				// 输出当前垃圾车经过的节点数
				out.print((ansr[i] - ansl[i]) + " ");
				// 输出当前垃圾车经过的所有节点
				for (int j = ansl[i]; j <= ansr[i]; j++) {
					// 输出路径上的每个节点
					out.print(ansArr[j] + " ");
				}
				// 换行
				out.println();
			}
		}
		// 刷新输出缓冲区
		out.flush();
		// 关闭输出流
		out.close();
	}

	// 读写工具类
	// 功能：提供高效的输入输出操作
	// 面试考点：IO优化技术、缓冲区机制
	// 边界条件：EOF处理、异常处理
	// ML/DL关联：高效数据加载器、批处理输入解析
	static class FastReader {
		// 输入缓冲区，用于批量读取数据
		private final byte[] buffer = new byte[1 << 16];
		// 缓冲区当前指针位置和有效数据长度
		private int ptr = 0, len = 0;
		// 输入流对象
		private final InputStream in;

		// 构造函数，初始化输入流
		FastReader(InputStream in) {
			// 保存输入流引用
			this.in = in;
		}

		// 读取下一个字节
		private int readByte() throws IOException {
			// 如果缓冲区已用完，重新填充
			if (ptr >= len) {
				// 从输入流读取数据到缓冲区
				len = in.read(buffer);
				// 重置指针
				ptr = 0;
				// 检查是否到达流末尾
				if (len <= 0)
					// 返回-1表示已到达流末尾
					return -1;
			}
			// 返回当前字节并移动指针
			return buffer[ptr++];
		}

		// 读取下一个整数
		int nextInt() throws IOException {
			// 临时变量存储当前字符
			int c;
			// 跳过空白字符，直到找到数字或符号
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			// 标记是否为负数
			boolean neg = false;
			// 检查符号
			if (c == '-') {
				// 设置负数标志
				neg = true;
				// 读取下一个字符
				c = readByte();
			}
			// 存储数值结果
			int val = 0;
			// 读取数字字符并转换为整数
			while (c > ' ' && c != -1) {
				// 将当前字符转换为数字并累加到结果中
				val = val * 10 + (c - '0');
				// 读取下一个字符
				c = readByte();
			}
			// 根据符号标志返回正数或负数
			return neg ? -val : val;
		}
	}

}
