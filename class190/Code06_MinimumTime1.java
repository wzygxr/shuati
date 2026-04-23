package class190; // 定义包名为class190

// 缩点最短路，java版
// 给定一张n个点，m条边的有向图，每条边有非负边权
// 如果两点属同一个强连通分量，认为彼此到达的代价为0
// 找到从1号点到n号点的路径，打印最小的边权累加和，如果不能到达打印-1
// 1 <= n <= 2 * 10^5
// 1 <= m <= 10^6
// 测试链接 : https://www.luogu.com.cn/problem/P2169
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException; // 导入IO异常处理类
import java.io.InputStream; // 导入输入流类
import java.io.OutputStreamWriter; // 导入输出流写入类
import java.io.PrintWriter; // 导入打印写入类
import java.util.PriorityQueue; // 导入优先队列类

public class Code06_MinimumTime1 { // 定义公共类Code06_MinimumTime1

	public static int MAXN = 200001; // 定义最大节点数常量，最多200000个节点
	public static int MAXM = 1000001; // 定义最大边数常量，最多1000000条边
	public static int INF = 1000000001; // 定义无穷大常量
	public static int n, m; // n为节点数，m为边数

	public static int[] a = new int[MAXM]; // 存储每条边的起点
	public static int[] b = new int[MAXM]; // 存储每条边的终点
	public static int[] val = new int[MAXM]; // 存储每条边的权值

	public static int[] head = new int[MAXN]; // 邻接表的头指针数组
	public static int[] nxt = new int[MAXM]; // 邻接表的next数组
	public static int[] to = new int[MAXM]; // 邻接表的to数组
	public static int[] weight = new int[MAXM]; // 邻接表的边权数组
	public static int cntg; // 图的边计数器

	public static int[] dfn = new int[MAXN]; // Tarjan算法中的深度优先序号数组
	public static int[] low = new int[MAXN]; // Tarjan算法中的low值数组
	public static int cntd; // dfn序号计数器

	public static int[] sta = new int[MAXN]; // Tarjan算法中的节点栈
	public static int top; // 栈顶指针

	public static int[] belong = new int[MAXN]; // 记录每个节点属于哪个SCC
	public static int sccCnt; // 强连通分量计数器

	public static int[] dist = new int[MAXN]; // 最短距离数组，dist[i]表示从起点到SCC i的最短距离
	public static boolean[] vis = new boolean[MAXN]; // 标记数组，vis[i]表示SCC i是否已确定最短距离
	// 优先队列，存储[节点, 距离]对，按距离从小到大排序
	public static PriorityQueue<int[]> heap = new PriorityQueue<>((x, y) -> x[1] - y[1]);

	// 迭代版需要的栈，讲解118讲了递归改迭代的技巧
	public static int[][] stack = new int[MAXN][3]; // 模拟递归栈
	public static int u, status, e; // 当前处理的节点、状态、边索引
	public static int stacksize; // 栈大小

	public static void push(int u, int status, int e) { // 将元素压入模拟栈
		stack[stacksize][0] = u; // 存储当前节点u
		stack[stacksize][1] = status; // 存储当前状态
		stack[stacksize][2] = e; // 存储当前边索引
		stacksize++; // 栈大小加1
	}

	public static void pop() { // 从模拟栈弹出元素
		stacksize--; // 栈大小减1
		u = stack[stacksize][0]; // 获取弹出的节点
		status = stack[stacksize][1]; // 获取弹出的状态
		e = stack[stacksize][2]; // 获取弹出的边索引
	}

	public static void addEdge(int u, int v, int w) { // 添加一条从u到v、权值为w的有向边
		nxt[++cntg] = head[u]; // 新边的next指向u原来的第一条边
		to[cntg] = v; // 新边指向节点v
		weight[cntg] = w; // 设置新边的权重为w
		head[u] = cntg; // 更新u的头指针指向新边
	}

	// 递归版，java会爆栈，C++可以通过
	public static void tarjan1(int u) { // 递归版Tarjan算法
		dfn[u] = low[u] = ++cntd; // 初始化dfn和low值
		sta[++top] = u; // 将u压入栈
		for (int e = head[u]; e > 0; e = nxt[e]) { // 遍历u的所有邻接边
			int v = to[e]; // 获取邻接节点v
			if (dfn[v] == 0) { // 如果v未被访问
				tarjan1(v); // 递归访问v
				low[u] = Math.min(low[u], low[v]); // 更新low值
			} else { // 如果v已被访问
				if (belong[v] == 0) { // 如果v还在栈中
					low[u] = Math.min(low[u], dfn[v]); // 更新low值
				}
			}
		}
		if (dfn[u] == low[u]) { // 如果u是SCC的根节点
			sccCnt++; // SCC计数加1
			int pop; // 弹出的节点
			do { // 弹出栈中属于该SCC的所有节点
				pop = sta[top--]; // 弹出栈顶
				belong[pop] = sccCnt; // 标记所属SCC
			} while (pop != u); // 直到弹出u
		}
	}

	// 迭代版
	public static void tarjan2(int node) { // 迭代版Tarjan算法
		stacksize = 0; // 初始化模拟栈为空
		push(node, -1, -1); // 将起始节点压入栈，状态-1表示首次访问
		int v; // 临时变量存储邻接节点
		while (stacksize > 0) { // 当栈不为空时循环
			pop(); // 弹出栈顶元素
			if (status == -1) { // 状态-1：首次访问该节点
				dfn[u] = low[u] = ++cntd; // 设置dfn和low值
				sta[++top] = u; // 压入Tarjan栈
				e = head[u]; // 获取第一条边
			} else { // 状态0或1：从子节点返回
				v = to[e]; // 获取之前访问的邻接节点
				if (status == 0) { // 状态0：从树边返回
					low[u] = Math.min(low[u], low[v]); // 用子节点的low更新
				}
				if (status == 1 && belong[v] == 0) { // 状态1：从回边返回且v在栈中
					low[u] = Math.min(low[u], dfn[v]); // 用v的dfn更新
				}
				e = nxt[e]; // 移动到下一条边
			}
			if (e != 0) { // 如果还有边未处理
				v = to[e]; // 获取当前边的终点
				if (dfn[v] == 0) { // 如果v未访问（树边）
					push(u, 0, e); // 当前节点压栈，状态0表示等待子节点返回
					push(v, -1, -1); // 子节点压栈，状态-1表示首次访问
				} else { // 如果v已访问（回边）
					push(u, 1, e); // 当前节点压栈，状态1表示回边处理
				}
			} else { // 所有边处理完毕
				if (dfn[u] == low[u]) { // 如果u是SCC的根
					sccCnt++; // SCC计数加1
					int pop; // 弹出的节点
					do { // 弹出栈中所有属于该SCC的节点
						pop = sta[top--]; // 弹出栈顶
						belong[pop] = sccCnt; // 标记所属SCC
					} while (pop != u); // 直到弹出u
				}
			}
		}
	}

	public static void condense() { // 缩点操作，构建缩点后的DAG
		cntg = 0; // 重置边计数器
		for (int i = 1; i <= sccCnt; i++) { // 初始化SCC的头指针
			head[i] = 0; // 清零
		}
		for (int i = 1; i <= m; i++) { // 遍历所有边
			int scc1 = belong[a[i]]; // 边起点的SCC编号
			int scc2 = belong[b[i]]; // 边终点的SCC编号
			// 如果两个端点都可达且属于不同SCC，则添加带权边
			if (scc1 > 0 && scc2 > 0 && scc1 != scc2) {
				addEdge(scc1, scc2, val[i]); // 在DAG中添加边，权值为原边权
			}
		}
	}

	public static int dijkstra() { // Dijkstra算法求最短路径
		for (int i = 1; i <= sccCnt; i++) { // 初始化距离数组和访问标记
			dist[i] = INF; // 距离初始化为无穷大
			vis[i] = false; // 访问标记初始化为false
		}
		dist[belong[1]] = 0; // 起点（1号点所在SCC）的距离为0
		heap.add(new int[] { belong[1], 0 }); // 将起点加入优先队列
		while (!heap.isEmpty()) { // 当优先队列不为空时循环
			int[] cur = heap.poll(); // 取出距离最小的节点
			int u = cur[0]; // 当前节点
			int d = cur[1]; // 当前距离
			if (!vis[u]) { // 如果该节点未被处理过
				vis[u] = true; // 标记为已处理
				for (int e = head[u]; e > 0; e = nxt[e]) { // 遍历u的所有出边
					int v = to[e]; // 邻接节点v
					int w = weight[e]; // 边权
					// 如果v未被处理且经过u到v的距离更短
					if (!vis[v] && dist[v] > d + w) {
						dist[v] = d + w; // 更新v的最短距离
						heap.add(new int[] { v, dist[v] }); // 将v加入优先队列
					}
				}
			}
		}
		return dist[belong[n]]; // 返回终点（n号点所在SCC）的最短距离
	}

	public static void main(String[] args) throws Exception { // 主程序入口
		FastReader in = new FastReader(System.in); // 创建快速读取对象
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out)); // 创建输出对象
		n = in.nextInt(); // 读取节点数
		m = in.nextInt(); // 读取边数
		for (int i = 1; i <= m; i++) { // 读取每条边
			a[i] = in.nextInt(); // 起点
			b[i] = in.nextInt(); // 终点
			val[i] = in.nextInt(); // 边权
			addEdge(a[i], b[i], val[i]); // 添加边到原图
		}
		// tarjan1(1); // 递归版（可能爆栈）
		tarjan2(1); // 使用迭代版Tarjan算法
		if (belong[n] == 0) { // 如果n号点不可达（不属于任何SCC）
			out.println(-1); // 输出-1表示不可达
		} else { // 如果n号点可达
			condense(); // 执行缩点操作
			int ans = dijkstra(); // 使用Dijkstra算法求最短路径
			out.println(ans); // 输出答案
		}
		out.flush(); // 刷新输出缓冲区
		out.close(); // 关闭输出流
	}

	// 读写工具类
	static class FastReader { // 快速读取类
		private final byte[] buffer = new byte[1 << 16]; // 输入缓冲区，64KB
		private int ptr = 0, len = 0; // 当前位置和缓冲区长度
		private final InputStream in; // 输入流

		FastReader(InputStream in) { // 构造方法
			this.in = in; // 保存输入流
		}

		private int readByte() throws IOException { // 读取一个字节
			if (ptr >= len) { // 如果缓冲区已读完
				len = in.read(buffer); // 重新填充缓冲区
				ptr = 0; // 重置指针
				if (len <= 0) // 如果读不到数据
					return -1; // 返回-1表示结束
			}
			return buffer[ptr++]; // 返回当前字节并后移指针
		}

		int nextInt() throws IOException { // 读取整数
			int c; // 当前字符
			do { // 跳过空白字符
				c = readByte();
			} while (c <= ' ' && c != -1);
			boolean neg = false; // 负数标记
			if (c == '-') { // 如果是负号
				neg = true; // 设置标记
				c = readByte(); // 读取下一个字符
			}
			int val = 0; // 结果值
			while (c > ' ' && c != -1) { // 读取数字字符
				val = val * 10 + (c - '0'); // 累加数字
				c = readByte(); // 读取下一个字符
			}
			return neg ? -val : val; // 返回结果
		}
	}

}
