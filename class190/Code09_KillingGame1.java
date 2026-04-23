package class190; // 定义包名为class190

// 杀人游戏，java版
// 一共n个人，只有一个杀手，每个人是杀手的概率均等，其他人都是平民
// 给定m个知晓关系，如果x知晓y，那么y是不是杀手，x就知道情况了
// 知晓关系是单向且可传递的，比如a知晓b，b知晓c，那么a知晓c
// 你可以盘问任何人，不仅能知道对方身份，并且对方知晓的所有情况都能获得
// 但是如果你直接盘问到杀手的话，杀手会原地爆炸，炸死所有人
// 你一定要确定所有人的身份，而且你充分了解知晓关系网，会用最优的盘问策略
// 返回最优盘问策略下，杀手不爆炸还能被揪出来的概率，保留小数点后面6位
// 1 <= n <= 10^5
// 0 <= m <= 3 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P4819
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException; // 导入IO异常处理类
import java.io.InputStream; // 导入输入流类
import java.io.OutputStreamWriter; // 导入输出流写入类
import java.io.PrintWriter; // 导入打印写入类
import java.util.Arrays; // 导入数组工具类

public class Code09_KillingGame1 { // 定义公共类Code09_KillingGame1

	public static int MAXN = 100001; // 定义最大节点数常量，最多100000个人
	public static int MAXM = 300001; // 定义最大边数常量，最多300000条边
	public static int n, m; // n为人数，m为知晓关系数
	public static int[] a = new int[MAXM]; // 存储每条知晓关系的起点
	public static int[] b = new int[MAXM]; // 存储每条知晓关系的终点

	public static int[] head = new int[MAXN]; // 邻接表的头指针数组
	public static int[] nxt = new int[MAXM]; // 邻接表的next数组
	public static int[] to = new int[MAXM]; // 邻接表的to数组
	public static int cntg; // 图的边计数器

	public static int[] dfn = new int[MAXN]; // Tarjan算法中的深度优先序号数组
	public static int[] low = new int[MAXN]; // Tarjan算法中的low值数组
	public static int cntd; // dfn序号计数器

	public static int[] sta = new int[MAXN]; // Tarjan算法中的节点栈
	public static int top; // 栈顶指针

	public static int[] belong = new int[MAXN]; // 记录每个人属于哪个SCC
	public static int[] sccSiz = new int[MAXN]; // 记录每个SCC的大小
	public static int sccCnt; // 强连通分量计数器

	public static long[] edgeArr = new long[MAXM]; // 用于存储缩点后的边，用long打包两个int
	public static int cnte; // 缩点后边的计数器

	public static int[] indegree = new int[MAXN]; // 缩点后DAG中每个SCC的入度

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
				sccSiz[sccCnt]++; // 该SCC的人数加1
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
						sccSiz[sccCnt]++; // 该SCC的人数加1
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

	public static boolean isolated(int i) { // 判断SCC i是否是孤立的（可以不盘问而确定身份）
		if (indegree[i] > 0 || sccSiz[i] > 1) { // 如果有入度或大小大于1（强连通）
			return false; // 不是孤立的
		}
		if (head[i] == 0) { // 如果没有出边（孤立的点）
			return true; // 是孤立的
		}
		for (int e = head[i]; e > 0; e = nxt[e]) { // 遍历所有出边
			int v = to[e]; // 邻接SCC v
			if (indegree[v] == 1) { // 如果v只有i这一个入边
				return false; // 不是孤立的（必须通过盘问i来确定v）
			}
		}
		return true; // 是孤立的
	}

	public static double compute() { // 计算最优策略下杀手不爆炸的概率
		int inZero = 0; // 统计缩点后DAG中入度为0的SCC数量
		for (int i = 1; i <= sccCnt; i++) { // 遍历所有SCC
			if (indegree[i] == 0) { // 如果入度为0
				inZero++; // 计数加1
			}
		}
		for (int i = 1; i <= sccCnt; i++) { // 尝试找到一个孤立点来减少盘问次数
			if (isolated(i)) { // 如果找到孤立的SCC
				inZero--; // 可以减少一次盘问
				break; // 只能减少一次
			}
		}
		// 返回概率：1 - (需要盘问的SCC数 / 总人数)
		// 需要盘问的SCC数 = 入度为0的SCC数（减去可能的孤立点）
		return 1.0 - (double) inZero / n;
	}

	public static void main(String[] args) throws Exception { // 主程序入口
		FastReader in = new FastReader(System.in); // 创建快速读取对象
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out)); // 创建输出对象
		n = in.nextInt(); // 读取人数
		m = in.nextInt(); // 读取知晓关系数
		for (int i = 1; i <= m; i++) { // 读取每条知晓关系
			a[i] = in.nextInt(); // 起点
			b[i] = in.nextInt(); // 终点（a知晓b）
			addEdge(a[i], b[i]); // 添加边到原图
		}
		for (int i = 1; i <= n; i++) { // 对所有未访问的人执行Tarjan
			if (dfn[i] == 0) { // 如果人i未被访问
				// tarjan1(i); // 递归版（可能爆栈）
				tarjan2(i); // 使用迭代版
			}
		}
		condense(); // 执行缩点操作
		double ans = compute(); // 计算最优策略下的概率
		out.printf("%.6f\n", ans); // 输出结果，保留6位小数
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
