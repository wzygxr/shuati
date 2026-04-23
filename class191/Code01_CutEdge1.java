package class191;

// 割边模版题1，java版
// 给定一张无向图，一共n个点、m条边
// 图中可能存在多个连通区，对每个连通区求割边
// 先打印割边的总数量，然后从小到大打印所有割边的序号
// 请保证原图即使有重边和自环，答案依然正确
// 1 <= n <= 5 * 10^5
// 1 <= m <= 2 * 10^6
// 测试链接 : https://www.luogu.com.cn/problem/U582665
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// 导入Java IO包，用于快速读写
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code01_CutEdge1 {

	// 定义常量：最大节点数，用于数组大小声明
	public static int MAXN = 500001;
	// 定义常量：最大边数（乘以2是因为无向图每条边需要存储两个方向）
	public static int MAXM = 2000001;
	// 图的节点数和边数（从输入读取）
	public static int n, m;

	// 邻接表存储：head[i]表示节点i的第一条边的编号
	public static int[] head = new int[MAXN];
	// nxt[i]表示编号为i的边的下一条边（链表结构）
	public static int[] nxt = new int[MAXM << 1];
	// to[i]表示编号为i的边的终点节点
	public static int[] to = new int[MAXM << 1];
	// 当前使用的边的编号（从1开始编号）
	public static int cntg;

	// Tarjan算法核心数组：dfn[u]表示节点u的发现时间（dfs序）
	public static int[] dfn = new int[MAXN];
	// low[u]表示节点u及其子树中所有节点能追溯到的最小dfn值
	public static int[] low = new int[MAXN];
	// 当前dfs的时间戳计数器
	public static int cntd;

	// 标记每条边是否为割边（true表示是割边）
	// 边的索引从1开始，cutEdge[i]对应第i条输入的边
	public static boolean[] cutEdge = new boolean[MAXM];

	// 迭代版需要的栈，讲解118讲了递归改迭代的技巧
	// 使用四维数组模拟函数调用栈：保存(u, preEdge, status, e)四个状态
	public static int[][] stack = new int[MAXN][4];
	// 当前处理的节点u
	public static int u, preEdge, status, e;
	// 模拟栈的栈顶指针
	public static int stacksize;

	/**
	 * 将一个状态压入模拟栈
	 * @param u 当前节点编号
	 * @param preEdge 从父节点来的边编号
	 * @param status 状态标记：-1表示刚进入节点，0表示处理树边回溯，1表示处理回边
	 * @param e 当前正在处理的边编号
	 */
	public static void push(int u, int preEdge, int status, int e) {
		// 将四个参数分别存入栈数组的对应位置
		stack[stacksize][0] = u;
		stack[stacksize][1] = preEdge;
		stack[stacksize][2] = status;
		stack[stacksize][3] = e;
		// 栈顶指针上移
		stacksize++;
	}

	/**
	 * 从模拟栈中弹出一个状态
	 * 将栈顶的四个值分别取出，赋给全局变量供后续使用
	 */
	public static void pop() {
		// 栈顶指针下移
		stacksize--;
		// 取出栈中保存的四个状态值
		u = stack[stacksize][0];
		preEdge = stack[stacksize][1];
		status = stack[stacksize][2];
		e = stack[stacksize][3];
	}

	/**
	 * 添加一条从u到v的无向边（邻接表插入）
	 * 每条无向边会存储为两条有向边（正向和反向）
	 * @param u 边的起点
	 * @param v 边的终点
	 */
	public static void addEdge(int u, int v) {
		// 将新边插入到链表头部（头插法）
		// cntg自增，作为这条边的唯一编号
		nxt[++cntg] = head[u];
		// 设置这条边的终点为v
		to[cntg] = v;
		// 更新节点u的头指针，指向新边
		head[u] = cntg;
	}

	/**
	 * 递归版本的Tarjan算法 - 求割边
	 * 使用dfn和low数组判断割边：对于边(u,v)，如果low[v] > dfn[u]，则该边是割边
	 * @param u 当前访问的节点
	 * @param preEdge 从父节点到u的边编号（用于跳过这条边，避免重复访问）
	 */
	// 递归版
	public static void tarjan1(int u, int preEdge) {
		// 初始化dfn和low为当前时间戳（发现时间）
		dfn[u] = low[u] = ++cntd;
		// 遍历节点u的所有邻接边
		for (int e = head[u]; e > 0; e = nxt[e]) {
			// 如果这条边是来时的边（父子边），则跳过
			// 通过异或1来判断：对于无向边，编号e和e^1是一对反向边
			if ((e ^ 1) == preEdge) { // 从哪条边来的要忽略
				continue;
			}
			// 获取边的终点节点
			int v = to[e];
			if (dfn[v] == 0) { // 树边：v还未被访问过
				// 递归处理子节点v
				tarjan1(v, e);
				// 回溯时，用子节点v的low值更新父节点u的low值
				// low[u] = min(low[u], low[v]) 表示u及其子树能追溯到的最早节点
				low[u] = Math.min(low[u], low[v]);
				// 判断割边条件：low[v] > dfn[u] 意味着v及其子树中所有节点
				// 都不能通过其他路径回到u或u的祖先，因此边(u,v)是割边
				if (low[v] > dfn[u]) {
					cutEdge[e >> 1] = true; // e>>1得到边的原始编号（除以2）
				}
			} else { // 回边或弃边
				// 回边是dfn[v] < dfn[u]，弃边是dfn[v] > dfn[u]
				// 因为low[u]初始值就是dfn[u]，所以弃边出现时，无法更新low[u]
				// 用发现时间更新low值
				low[u] = Math.min(low[u], dfn[v]);
			}
		}
	}

	/**
	 * 迭代版本的Tarjan算法 - 求割边
	 * 手动模拟递归调用栈，避免递归深度过大导致栈溢出
	 * @param node 起始节点编号
	 * @param pree 从父节点来的边编号
	 */
	// 迭代版
	// u表示当前节点，preEdge表示来边
	// e表示u当前处理的边
	//     如果(e ^ 1) == preEdge，跳过当前边，对应递归版中的第一个if
	//     如果e == 0，说明所有边都处理完了
	// status的具体说明如下
	//     如果status == -1，表示u没有遍历过任何儿子
	//     如果status == 0，表示u遍历到儿子v，然后发现dfn[v] == 0
	//         并且执行完了tarjan(v, e)，对应递归版for循环的第二个if
	//     如果status == 1，表示u遍历到儿子v，然后发现dfn[v] != 0
	//         对应递归版for循环中的else分支
	public static void tarjan2(int node, int pree) {
		// 初始化模拟栈
		stacksize = 0;
		// 将初始状态压栈：进入节点node，preEdge为pree，状态为-1表示刚进入
		push(node, pree, -1, -1);
		int v;
		// 模拟递归过程：当栈不为空时循环
		while (stacksize > 0) {
			// 弹出一个状态进行处理
			pop();
			// 根据status判断当前处于哪个阶段
			if (status == -1) {
				// 刚进入节点u，初始化dfn和low
				dfn[u] = low[u] = ++cntd;
				// 从节点u的第一条边开始处理
				e = head[u];
			} else {
				// 处理完子节点后的回溯阶段
				v = to[e];
				if (status == 0) {
					// 子节点v是树边子节点，已完成dfs
					// 用子节点的low值更新父节点的low值
					low[u] = Math.min(low[u], low[v]);
					// 判断割边条件
					if (low[v] > dfn[u]) {
						cutEdge[e >> 1] = true;
					}
				} else {
					// 子节点v是回边或弃边
					// 用dfn[v]更新low[u]
					low[u] = Math.min(low[u], dfn[v]);
				}
				// 处理完当前边后，移动到下一条边
				e = nxt[e];
			}
			// 跳过从父节点来的那条边（避免重复访问）
			if ((e ^ 1) == preEdge) {
				e = nxt[e];
			}
			// 如果还有未处理的边
			if (e != 0) {
				v = to[e];
				if (dfn[v] == 0) {
					// 子节点v未被访问过，是树边
					// 先保存当前边的处理状态（等会回溯用）
					push(u, preEdge, 0, e);
					// 再将子节点v压栈，准备进入子节点
					push(v, e, -1, -1);
				} else {
					// 子节点v已经被访问过，是回边
					push(u, preEdge, 1, e);
				}
			}
		}
	}

	/**
	 * 主函数：程序入口
	 * 读取输入数据，构建图，执行Tarjan算法找割边，输出结果
	 */
	public static void main(String[] args) throws Exception {
		// 创建快速输入读取器
		FastReader in = new FastReader(System.in);
		// 创建快速输出写入器
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		// 边的编号从1开始
		cntg = 1;
		// 读取节点数n和边数m
		n = in.nextInt();
		m = in.nextInt();
		// 读取所有m条边
		for (int i = 1, u, v; i <= m; i++) {
			// 读取边的两个端点
			u = in.nextInt();
			v = in.nextInt();
			// 添加无向边（两条有向边）
			addEdge(u, v);
			addEdge(v, u);
		}
		// 遍历所有节点，处理非连通图的情况
		for (int i = 1; i <= n; i++) {
			// 如果节点i还未被访问过，从它开始执行Tarjan算法
			if (dfn[i] == 0) {
				// tarjan1(i, 0);
				tarjan2(i, 0); // 使用迭代版避免递归深度问题
			}
		}
		// 统计割边的数量
		int ansCnt = 0;
		for (int i = 1; i <= m; i++) {
			if (cutEdge[i]) {
				ansCnt++;
			}
		}
		// 输出割边数量
		out.println(ansCnt);
		// 按编号从小到大输出所有割边的序号
		for (int i = 1; i <= m; i++) {
			if (cutEdge[i]) {
				out.print(i + " ");
			}
		}
		// 输出换行，刷新缓冲区，关闭输出流
		out.println();
		out.flush();
		out.close();
	}

	/**
	 * 快速读写工具类
	 * 使用自定义缓冲区实现高效的字符读取，适用于大数据量输入
	 */
	// 读写工具类
	static class FastReader {
		// 缓冲区，大小为2^16=65536字节
		private final byte[] buffer = new byte[1 << 16];
		// buffer中当前读取位置的指针
		private int ptr = 0, len = 0;
		// 输入流
		private final InputStream in;

		// 构造函数，绑定输入流
		FastReader(InputStream in) {
			this.in = in;
		}

		/**
		 * 从输入流读取一个字节
		 * @return 读取的字节值，如果到达文件末尾返回-1
		 * @throws IOException 可能抛出IO异常
		 */
		private int readByte() throws IOException {
			// 如果指针已经到达缓冲区末尾
			if (ptr >= len) {
				// 重新填充缓冲区
				len = in.read(buffer);
				// 重置指针到缓冲区开头
				ptr = 0;
				// 如果读取失败（到达文件末尾），返回-1
				if (len <= 0)
					return -1;
			}
			// 返回当前字节，并将指针前移
			return buffer[ptr++];
		}

		/**
		 * 读取下一个整数
		 * @return 解析出的整数
		 * @throws IOException 可能抛出IO异常
		 */
		int nextInt() throws IOException {
			int c;
			// 跳过空白字符（空格、制表符、换行符等）
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			// 判断是否为负数
			boolean neg = false;
			if (c == '-') {
				neg = true;
				c = readByte();
			}
			// 解析整数
			int val = 0;
			while (c > ' ' && c != -1) {
				val = val * 10 + (c - '0');
				c = readByte();
			}
			// 如果是负数，返回负值
			return neg ? -val : val;
		}
	}

}
