package class193;

/**
 * 割点模板题，Java版
 * 题目描述：给定一张无向图，包含n个顶点和m条边，图不一定连通
 * 要求：输出图中割点的数量，并按从小到大的顺序输出所有割点的编号
 * 数据范围：1 <= n <= 2 * 10^4，1 <= m <= 10^5
 * 测试链接：https://www.luogu.com.cn/problem/P3388
 * 提交说明：提交时请将类名改为"Main"，可以通过所有测试用例
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * 割点问题求解类
 * 包含Tarjan算法的递归版和迭代版实现
 */
public class Code01_CutVertex1 {

	/**
	 * 最大顶点数，题目中n最大为20000，所以设置为20001
	 */
	public static int MAXN = 20001;
	
	/**
	 * 最大边数，题目中m最大为100000，无向图每条边存储两次，所以设置为100001
	 */
	public static int MAXM = 100001;
	
	/**
	 * 顶点数n和边数m
	 */
	public static int n, m;

	/**
	 * 邻接表表头数组，head[u]表示以u为起点的第一条边的索引
	 */
	public static int[] head = new int[MAXN];
	
	/**
	 * 邻接表下一条边索引数组，nxt[e]表示与边e同起点的下一条边的索引
	 */
	public static int[] nxt = new int[MAXM << 1];
	
	/**
	 * 邻接表边的终点数组，to[e]表示边e的终点
	 */
	public static int[] to = new int[MAXM << 1];
	
	/**
	 * 边计数器，记录当前已添加的边数
	 */
	public static int cntg;

	/**
	 * dfn数组，记录每个顶点的深度优先搜索时间戳
	 */
	public static int[] dfn = new int[MAXN];
	
	/**
	 * low数组，记录每个顶点能通过非父子边回溯到的最早祖先的时间戳
	 */
	public static int[] low = new int[MAXN];
	
	/**
	 * 时间戳计数器，记录当前的时间戳
	 */
	public static int cntd;

	/**
	 * cutVertex数组，标记每个顶点是否为割点
	 */
	public static boolean[] cutVertex = new boolean[MAXN];

	/**
	 * 迭代版Tarjan算法使用的栈
	 * 每个栈元素包含5个信息：当前顶点u、是否为根节点root、子节点数son、状态status、当前边索引e
	 */
	public static int[][] stack = new int[MAXN][5];
	
	/**
	 * 栈操作相关变量
	 */
	public static int u, root, son, status, e;
	
	/**
	 * 栈大小计数器
	 */
	public static int stacksize;

	/**
	 * 将元素压入栈中
	 * @param u 当前顶点
	 * @param root 是否为根节点（1表示是，0表示否）
	 * @param son 子节点数
	 * @param status 当前状态（-1表示首次访问，0表示处理子节点返回，1表示处理回边）
	 * @param e 当前边索引
	 */
	public static void push(int u, int root, int son, int status, int e) {
		stack[stacksize][0] = u;
		stack[stacksize][1] = root;
		stack[stacksize][2] = son;
		stack[stacksize][3] = status;
		stack[stacksize][4] = e;
		stacksize++;
	}

	/**
	 * 从栈中弹出元素
	 */
	public static void pop() {
		stacksize--;
		u = stack[stacksize][0];
		root = stack[stacksize][1];
		son = stack[stacksize][2];
		status = stack[stacksize][3];
		e = stack[stacksize][4];
	}

	/**
	 * 向邻接表中添加一条边
	 * @param u 边的起点
	 * @param v 边的终点
	 */
	public static void addEdge(int u, int v) {
		nxt[++cntg] = head[u]; // 将新边的nxt指向当前head[u]
		to[cntg] = v; // 设置新边的终点为v
		head[u] = cntg; // 更新head[u]为新边的索引
	}

	/**
	 * 递归版Tarjan算法求解割点
	 * @param u 当前访问的顶点
	 * @param root 是否为根节点
	 */
	public static void tarjan1(int u, boolean root) {
		dfn[u] = low[u] = ++cntd; // 初始化dfn和low值为当前时间戳
		int son = 0; // 记录当前顶点的子节点数
		
		// 遍历当前顶点的所有邻边
		for (int e = head[u]; e > 0; e = nxt[e]) {
			int v = to[e]; // 获取边的终点
			
			if (dfn[v] == 0) { // 如果v未被访问过
				son++; // 子节点数加1
				tarjan1(v, false); // 递归访问v
				low[u] = Math.min(low[u], low[v]); // 更新low[u]为low[u]和low[v]的最小值
				
				// 如果low[v] >= dfn[u]，说明v无法通过非父子边回溯到u的祖先
				if (low[v] >= dfn[u]) {
					// 如果不是根节点，或者是根节点且子节点数>=2，则u是割点
					if (!root || son >= 2) {
						cutVertex[u] = true;
					}
				}
			} else { // 如果v已被访问过，说明是回边
				low[u] = Math.min(low[u], dfn[v]); // 更新low[u]为low[u]和dfn[v]的最小值
			}
		}
	}

	/**
	 * 迭代版Tarjan算法求解割点
	 * @param node 起始节点
	 * @param rt 是否为根节点
	 */
	public static void tarjan2(int node, boolean rt) {
		stacksize = 0; // 初始化栈大小
		push(node, rt ? 1 : 0, 0, -1, -1); // 将起始节点压入栈中
		int v;
		
		// 栈不为空时循环
		while (stacksize > 0) {
			pop(); // 弹出栈顶元素
			
			if (status == -1) { // 首次访问当前节点
				dfn[u] = low[u] = ++cntd; // 初始化dfn和low值
				e = head[u]; // 获取当前节点的第一条边
			} else { // 处理子节点返回或回边
				v = to[e]; // 获取边的终点
				if (status == 0) { // 处理子节点返回
					low[u] = Math.min(low[u], low[v]); // 更新low[u]
					
					// 判断是否为割点
					if (low[v] >= dfn[u]) {
						if (root == 0 || son >= 2) {
							cutVertex[u] = true;
						}
					}
				} else { // 处理回边
					low[u] = Math.min(low[u], dfn[v]); // 更新low[u]
				}
				e = nxt[e]; // 获取下一条边
			}
			
			// 如果还有未处理的边
			if (e != 0) {
				v = to[e]; // 获取边的终点
				if (dfn[v] == 0) { // 如果v未被访问过
					son++; // 子节点数加1
					push(u, root, son, 0, e); // 将当前节点状态压入栈
					push(v, 0, 0, -1, -1); // 将v压入栈，准备访问
				} else { // 如果v已被访问过，处理回边
					push(u, root, son, 1, e); // 将当前节点状态压入栈
				}
			}
		}
	}

	/**
	 * 主函数，程序入口
	 * @param args 命令行参数
	 * @throws Exception 可能抛出的异常
	 */
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in); // 创建快速输入流
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out)); // 创建输出流
		
		n = in.nextInt(); // 读取顶点数n
		m = in.nextInt(); // 读取边数m
		
		// 读取m条边并添加到邻接表
		for (int i = 1, u, v; i <= m; i++) {
			u = in.nextInt(); // 读取边的起点u
			v = in.nextInt(); // 读取边的终点v
			addEdge(u, v); // 添加边u->v
			addEdge(v, u); // 添加边v->u（无向图）
		}
		
		// 遍历所有顶点，处理每个连通分量
		for (int i = 1; i <= n; i++) {
			if (dfn[i] == 0) { // 如果顶点i未被访问过
				// tarjan1(i, true); // 使用递归版Tarjan算法
				tarjan2(i, true); // 使用迭代版Tarjan算法
			}
		}
		
		// 统计割点数量
		int cntCnt = 0;
		for (int i = 1; i <= n; i++) {
			if (cutVertex[i]) {
				cntCnt++;
			}
		}
		
		out.println(cntCnt); // 输出割点数量
		
		// 按从小到大的顺序输出所有割点
		for (int i = 1; i <= n; i++) {
			if (cutVertex[i]) {
				out.print(i + " ");
			}
		}
		
		out.println(); // 换行
		out.flush(); // 刷新输出流
		out.close(); // 关闭输出流
	}

	/**
	 * 快速输入工具类
	 * 用于高效读取输入数据
	 */
	static class FastReader {
		private final byte[] buffer = new byte[1 << 16]; // 输入缓冲区
		private int ptr = 0, len = 0; // 缓冲区指针和已读取长度
		private final InputStream in; // 输入流

		/**
		 * 构造函数
		 * @param in 输入流
		 */
		FastReader(InputStream in) {
			this.in = in;
		}

		/**
		 * 读取一个字节
		 * @return 读取的字节
		 * @throws IOException 可能抛出的IO异常
		 */
		private int readByte() throws IOException {
			if (ptr >= len) { // 如果缓冲区已读完
				len = in.read(buffer); // 从输入流读取新数据到缓冲区
				ptr = 0; // 重置指针
				if (len <= 0) // 如果读取失败
					return -1;
			}
			return buffer[ptr++]; // 返回当前指针位置的字节，并移动指针
		}

		/**
		 * 读取一个整数
		 * @return 读取的整数
		 * @throws IOException 可能抛出的IO异常
		 */
		int nextInt() throws IOException {
			int c;
			// 跳过空白字符
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			
			boolean neg = false; // 标记是否为负数
			if (c == '-') { // 如果是负号
				neg = true;
				c = readByte(); // 读取下一个字符
			}
			
			int val = 0; // 存储读取的整数
			// 读取数字字符并转换为整数
			while (c > ' ' && c != -1) {
				val = val * 10 + (c - '0');
				c = readByte();
			}
			
			return neg ? -val : val; // 返回结果，负数则取反
		}
	}

}
