package class190; // 定义包名为class190

// 最大半连通子图，java版
// 有向图中节点u和v，只要其中一点能到达另一点，就说两点是半连通的
// 如果一个有向图，任意两点都是半连通的，这样的有向图就是半连通图
// 有向图中的一个点集，该点集中只要某两点在原图中有边，那么这条边就保留，则可以得到一个子图
// 如果该子图既是半连通图，又有节点数量最多，那么这个子图就是原图的最大半连通子图
// 给定一张n个点，m条边的有向图，打印最大半连通子图的大小
// 可能存在多个最大半连通子图，打印这个数量，数量对给定的数字x取余
// 1 <= n <= 10^5
// 1 <= m <= 10^6
// 测试链接 : https://www.luogu.com.cn/problem/P2272
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException; // 导入IO异常处理类
import java.io.InputStream; // 导入输入流类
import java.io.OutputStreamWriter; // 导入输出流写入类
import java.io.PrintWriter; // 导入打印写入类
import java.util.Arrays; // 导入数组工具类

public class Code05_MaximumSemi1 { // 定义公共类Code05_MaximumSemi1

	public static int MAXN = 100001; // 定义最大节点数常量，最多100000个节点
	public static int MAXM = 1000001; // 定义最大边数常量，最多1000000条边
	public static int n, m, x; // n为节点数，m为边数，x为取模数
	public static int[] a = new int[MAXM]; // 存储每条边的起点
	public static int[] b = new int[MAXM]; // 存储每条边的终点

	public static int[] head = new int[MAXN]; // 邻接表的头指针数组
	public static int[] nxt = new int[MAXM]; // 邻接表的next数组
	public static int[] to = new int[MAXM]; // 邻接表的to数组
	public static int cntg; // 图的边计数器

	public static int[] dfn = new int[MAXN]; // Tarjan算法中的深度优先序号数组
	public static int[] low = new int[MAXN]; // Tarjan算法中的low值数组
	public static int cntd; // dfn序号计数器

	public static int[] sta = new int[MAXN]; // Tarjan算法中的节点栈
	public static int top; // 栈顶指针

	public static int[] belong = new int[MAXN]; // 记录每个节点属于哪个SCC
	public static int[] sccSiz = new int[MAXN]; // 记录每个SCC的大小（包含的节点数）
	public static int sccCnt; // 强连通分量计数器

	public static long[] edgeArr = new long[MAXM]; // 用于存储缩点后的边，用long打包两个int
	public static int cnte; // 缩点后边的计数器

	public static int[] indegree = new int[MAXN]; // 缩点后DAG中每个SCC的入度
	public static int[] dpSum = new int[MAXN]; // DP数组，dpSum[i]表示以SCC i结尾的最大半连通子图的节点数
	public static int[] dpCnt = new int[MAXN]; // DP数组，dpCnt[i]表示以SCC i结尾的最大半连通子图的数量

	public static int ans1, ans2; // ans1为最大半连通子图的大小，ans2为数量

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

	public static void addEdge(int u, int v) { // 添加一条从u到v的有向边
		nxt[++cntg] = head[u]; // 新边的next指向u原来的第一条边
		to[cntg] = v; // 新边指向节点v
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
			sccSiz[sccCnt] = 0; // 初始化该SCC的大小为0
			int pop; // 弹出的节点
			do { // 弹出栈中属于该SCC的所有节点
				pop = sta[top--]; // 弹出栈顶
				belong[pop] = sccCnt; // 标记所属SCC
				sccSiz[sccCnt]++; // 该SCC的节点数加1
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
					sccSiz[sccCnt] = 0; // 初始化该SCC的大小为0
					int pop; // 弹出的节点
					do { // 弹出栈中所有属于该SCC的节点
						pop = sta[top--]; // 弹出栈顶
						belong[pop] = sccCnt; // 标记所属SCC
						sccSiz[sccCnt]++; // 该SCC的节点数加1
					} while (pop != u); // 直到弹出u
				}
			}
		}
	}

	public static void condense() { // 缩点操作，构建缩点后的DAG并去重边
		cntg = 0; // 重置边计数器
		for (int i = 1; i <= sccCnt; i++) { // 初始化SCC的头指针
			head[i] = 0; // 清零
		}
		for (int i = 1; i <= m; i++) { // 遍历所有边
			int scc1 = belong[a[i]]; // 边起点的SCC编号
			int scc2 = belong[b[i]]; // 边终点的SCC编号
			if (scc1 != scc2) { // 如果边连接不同SCC
				// 将两个int打包成一个long：高32位是scc1，低32位是scc2
				edgeArr[++cnte] = ((long) scc1 << 32) | scc2;
			}
		}
		Arrays.sort(edgeArr, 1, cnte + 1); // 对边进行排序，便于去重
		long pre = 0, cur; // pre记录上一条边，cur记录当前边
		for (int i = 1; i <= cnte; i++) { // 遍历排序后的边
			cur = edgeArr[i]; // 获取当前边
			if (cur != pre) { // 如果当前边与上一条不同（去重）
				int scc1 = (int) (cur >>> 32); // 解压出起点SCC
				int scc2 = (int) (cur & 0xffffffffL); // 解压出终点SCC
				indegree[scc2]++; // 终点SCC的入度加1
				addEdge(scc1, scc2); // 添加边到DAG
				pre = cur; // 更新上一条边
			}
		}
	}

	public static void dpOnDAG() { // 在缩点后的DAG上进行动态规划
		for (int u = sccCnt; u > 0; u--) { // 按SCC编号逆序进行DP（拓扑序）
			if (indegree[u] == 0) { // 如果u是入度为0的起点
				dpSum[u] = sccSiz[u]; // 以u结尾的路径的节点数为u的大小
				dpCnt[u] = 1; // 以u结尾的路径的数量为1
			}
			for (int e = head[u]; e > 0; e = nxt[e]) { // 遍历u的所有出边
				int v = to[e]; // 邻接SCC v
				if (dpSum[v] < dpSum[u] + sccSiz[v]) { // 如果经过u到v能得到更长的路径
					dpSum[v] = dpSum[u] + sccSiz[v]; // 更新以v结尾的最大节点数
					dpCnt[v] = dpCnt[u]; // 更新以v结尾的路径数量
				} else if (dpSum[v] == dpSum[u] + sccSiz[v]) { // 如果路径长度相同
					dpCnt[v] = (dpCnt[v] + dpCnt[u]) % x; // 累加路径数量并取模
				}
			}
		}
		ans1 = ans2 = 0; // 初始化答案
		for (int i = 1; i <= sccCnt; i++) { // 遍历所有SCC
			if (dpSum[i] > ans1) { // 如果找到更大的半连通子图
				ans1 = dpSum[i]; // 更新最大大小
				ans2 = dpCnt[i]; // 更新数量
			} else if (dpSum[i] == ans1) { // 如果大小相同
				ans2 = (ans2 + dpCnt[i]) % x; // 累加数量并取模
			}
		}
	}

	public static void main(String[] args) throws Exception { // 主程序入口
		FastReader in = new FastReader(System.in); // 创建快速读取对象
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out)); // 创建输出对象
		n = in.nextInt(); // 读取节点数
		m = in.nextInt(); // 读取边数
		x = in.nextInt(); // 读取取模数
		for (int i = 1; i <= m; i++) { // 读取每条边
			a[i] = in.nextInt(); // 起点
			b[i] = in.nextInt(); // 终点
			addEdge(a[i], b[i]); // 添加边到原图
		}
		for (int i = 1; i <= n; i++) { // 对所有未访问的节点执行Tarjan
			if (dfn[i] == 0) { // 如果节点i未被访问
				// tarjan1(i); // 递归版（可能爆栈）
				tarjan2(i); // 使用迭代版
			}
		}
		condense(); // 执行缩点操作
		dpOnDAG(); // 在DAG上DP求解
		out.println(ans1); // 输出最大半连通子图的大小
		out.println(ans2); // 输出最大半连通子图的数量
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
