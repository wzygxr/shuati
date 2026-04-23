package class190; // 定义包名为class190

// 缩点结合动态规划模版题，java版
// 给定一张n个点，m条边的有向图，每个点给定非负点权
// 如果重复经过一个点，点权只获得一次
// 找到一条路径，使得点权累加和最大，打印这个值
// 1 <= n <= 10^4
// 1 <= m <= 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P3387
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException; // 导入IO异常处理类
import java.io.InputStream; // 导入输入流类
import java.io.OutputStreamWriter; // 导入输出流写入类
import java.io.PrintWriter; // 导入打印写入类

public class Code01_CondenseTopo1 { // 定义公共类Code01_CondenseTopo1

	public static int MAXN = 10001; // 定义最大节点数常量，最多10000个节点
	public static int MAXM = 100001; // 定义最大边数常量，最多100000条边
	public static int n, m; // n为实际节点数，m为实际边数

	public static int[] arr = new int[MAXN]; // 存储每个节点的点权值
	public static int[] a = new int[MAXM]; // 存储每条边的起点
	public static int[] b = new int[MAXM]; // 存储每条边的终点

	public static int[] head = new int[MAXN]; // 邻接表的头指针数组，head[i]表示节点i的第一条边
	public static int[] nxt = new int[MAXM]; // 邻接表的next数组，存储下一条边的索引
	public static int[] to = new int[MAXM]; // 邻接表的to数组，存储边指向的节点
	public static int cntg; // 图的边计数器，用于添加边时分配索引

	public static int[] dfn = new int[MAXN]; // Tarjan算法中记录每个节点的深度优先序号(discovery time)
	public static int[] low = new int[MAXN]; // Tarjan算法中记录每个节点能到达的最小dfn值
	public static int cntd; // dfn序号计数器，每次访问新节点时递增

	public static int[] sta = new int[MAXN]; // Tarjan算法中用于存储当前DFS路径上的节点栈
	public static int top; // 栈顶指针，指向栈顶元素的位置

	public static int[] belong = new int[MAXN]; // 记录每个节点属于哪个强连通分量(SCC)
	public static int[] sum = new int[MAXN]; // 记录每个SCC内部所有节点的点权之和
	public static int sccCnt; // 强连通分量的计数器

	public static int[] indegree = new int[MAXN]; // 缩点后DAG中每个SCC的入度
	public static int[] que = new int[MAXN]; // 拓扑排序中使用的队列数组
	public static int[] dp = new int[MAXN]; // 动态规划数组，dp[i]表示到达SCC i的最大点权和

	public static void addEdge(int u, int v) { // 添加一条从u到v的有向边
		nxt[++cntg] = head[u]; // 新边的next指向u节点原来的第一条边
		to[cntg] = v; // 新边指向节点v
		head[u] = cntg; // 更新u节点的头指针指向新边
	}

	public static void tarjan(int u) { // Tarjan算法求强连通分量，u为当前节点
		dfn[u] = low[u] = ++cntd; // 初始化u的dfn和low值为当前序号
		sta[++top] = u; // 将u压入栈中
		for (int e = head[u]; e > 0; e = nxt[e]) { // 遍历u的所有邻接边
			int v = to[e]; // v为u通过边e到达的节点
			if (dfn[v] == 0) { // 如果v未被访问过（dfn为0表示未访问）
				tarjan(v); // 递归访问v
				low[u] = Math.min(low[u], low[v]); // u的low值取min(当前值, v的low值)
			} else { // 如果v已被访问过
				if (belong[v] == 0) { // 如果v还在栈中（belong为0表示未出栈）
					low[u] = Math.min(low[u], dfn[v]); // 用v的dfn更新u的low值
				}
			}
		}
		if (dfn[u] == low[u]) { // 如果u的dfn等于low，说明u是一个SCC的根节点
			sccCnt++; // 发现一个新的强连通分量
			int pop; // 用于存储弹出的节点
			do { // 开始弹出栈中属于该SCC的所有节点
				pop = sta[top--]; // 弹出栈顶节点
				belong[pop] = sccCnt; // 标记pop属于当前SCC
				sum[sccCnt] += arr[pop]; // 将该节点的点权累加到SCC的总权值中
			} while (pop != u); // 直到弹出u本身为止
		}
	}

	public static void condense() { // 缩点操作：将SCC缩成单个节点，构建DAG
		cntg = 0; // 重置边计数器，准备重新建图
		for (int i = 1; i <= sccCnt; i++) { // 初始化新图的每个SCC节点的头指针
			head[i] = 0; // 清零头指针，表示没有边
		}
		for (int i = 1; i <= m; i++) { // 遍历原图的每条边
			int scc1 = belong[a[i]]; // 获取边i起点的SCC编号
			int scc2 = belong[b[i]]; // 获取边i终点的SCC编号
			if (scc1 != scc2) { // 如果两个端点属于不同的SCC，则在DAG中添加边
				indegree[scc2]++; // 终点SCC的入度加1
				addEdge(scc1, scc2); // 添加从scc1到scc2的有向边
			}
		}
	}

	// 拓扑排序的写法
	public static int topo() { // 使用拓扑排序进行DP求解最大点权和
		int l = 1, r = 0; // 初始化队列的左右指针，l为队头，r为队尾
		for (int i = 1; i <= sccCnt; i++) { // 将所有入度为0的SCC加入队列
			if (indegree[i] == 0) { // 如果SCC i的入度为0
				dp[i] = sum[i]; // 初始化dp[i]为其自身的点权和
				que[++r] = i; // 将i加入队列尾部
			}
		}
		while (l <= r) { // 当队列不为空时继续处理
			int u = que[l++]; // 取出队头元素u
			for (int e = head[u]; e > 0; e = nxt[e]) { // 遍历u的所有出边
				int v = to[e]; // v为u指向的节点
				dp[v] = Math.max(dp[v], dp[u] + sum[v]); // 更新v的dp值，取最大值
				if (--indegree[v] == 0) { // 将v的入度减1，如果变为0则加入队列
					que[++r] = v; // 将v加入队列
				}
			}
		}
		int ans = 0; // 初始化答案为0
		for (int i = 1; i <= sccCnt; i++) { // 遍历所有SCC，找到最大dp值
			ans = Math.max(ans, dp[i]); // 更新最大值
		}
		return ans; // 返回最大点权和
	}

	// 直接转移的写法
	public static int dpOnDAG() { // 直接在DAG上进行DP，不依赖拓扑序
		for (int u = sccCnt; u > 0; u--) { // 按照SCC编号的逆序进行DP
			if (indegree[u] == 0) { // 如果u是入度为0的起点
				dp[u] = sum[u]; // 初始化dp[u]为其自身的点权和
			}
			for (int e = head[u]; e > 0; e = nxt[e]) { // 遍历u的所有出边
				int v = to[e]; // v为u指向的节点
				dp[v] = Math.max(dp[v], dp[u] + sum[v]); // 更新v的dp值
			}
		}
		int ans = 0; // 初始化答案为0
		for (int u = 1; u <= sccCnt; u++) { // 遍历所有SCC
			ans = Math.max(ans, dp[u]); // 更新最大值
		}
		return ans; // 返回最大点权和
	}

	public static void main(String[] args) throws Exception { // 主程序入口
		FastReader in = new FastReader(System.in); // 创建快速读取对象
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out)); // 创建打印输出对象
		n = in.nextInt(); // 读取节点数n
		m = in.nextInt(); // 读取边数m
		for (int i = 1; i <= n; i++) { // 读取每个节点的点权
			arr[i] = in.nextInt(); // 读取节点i的点权值
		}
		for (int i = 1; i <= m; i++) { // 读取每条边的信息
			a[i] = in.nextInt(); // 读取第i条边的起点
			b[i] = in.nextInt(); // 读取第i条边的终点
			addEdge(a[i], b[i]); // 添加边到原图中
		}
		for (int i = 1; i <= n; i++) { // 对所有未访问的节点执行Tarjan算法
			if (dfn[i] == 0) { // 如果节点i未被访问过（dfn为0）
				tarjan(i); // 从节点i开始执行Tarjan算法
			}
		}
		condense(); // 执行缩点操作，构建DAG
		// int ans = topo(); // 可以使用拓扑排序求解（此行被注释）
		int ans = dpOnDAG(); // 使用直接DP方法求解最大点权和
		out.println(ans); // 输出答案
		out.flush(); // 刷新输出缓冲区
		out.close(); // 关闭输出流
	}

	// 读写工具类
	static class FastReader { // 定义快速读取类，用于加速输入
		private final byte[] buffer = new byte[1 << 16]; // 定义输入缓冲区，大小为65536字节
		private int ptr = 0, len = 0; // ptr为当前读取位置，len为缓冲区实际长度
		private final InputStream in; // 输入流对象

		FastReader(InputStream in) { // 构造方法，传入输入流
			this.in = in; // 保存输入流引用
		}

		private int readByte() throws IOException { // 从缓冲区读取一个字节
			if (ptr >= len) { // 如果当前位置已经到达或超过缓冲区末尾
				len = in.read(buffer); // 从输入流读取数据到缓冲区
				ptr = 0; // 重置读取指针到缓冲区开头
				if (len <= 0) // 如果没有读到数据（到达文件末尾）
					return -1; // 返回-1表示结束
			}
			return buffer[ptr++]; // 返回当前字节并将指针后移
		}

		int nextInt() throws IOException { // 读取下一个整数
			int c; // 存储读取到的字符
			do { // 跳过前导空白字符（空格、换行等）
				c = readByte(); // 读取一个字节
			} while (c <= ' ' && c != -1); // 如果字符是空白则继续读取
			boolean neg = false; // 标记是否为负数
			if (c == '-') { // 如果读取到负号
				neg = true; // 设置负数标记
				c = readByte(); // 读取下一个字符（数字部分）
			}
			int val = 0; // 存储最终整数值
			while (c > ' ' && c != -1) { // 当字符不是空白时继续读取数字
				val = val * 10 + (c - '0'); // 将数字字符转换为数值并累加
				c = readByte(); // 读取下一个字符
			}
			return neg ? -val : val; // 如果是负数则返回负值，否则返回正值
		}
	}

}
