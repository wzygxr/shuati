package class193;

/**
 * 矿场搭建问题，Java版
 * 题目描述：
 * 一共n个地点，地点至少2个，每个地点都有人，m条双向道路连通所有地点
 * 地震会发生在任何一个地点，地震发生时，其他地点的人都要去往救援点
 * 你可以在任何地点设立救援点，但是发生地震的地点，道路和救援点都会失效
 * 打印至少需要几个救援点，打印设立救援点的方案总数，方案认为是无序集合
 * 数据范围：1 <= n <= 1000，1 <= m <= 1000
 * 测试链接：https://www.luogu.com.cn/problem/P3225
 * 提交说明：提交以下代码，提交时请把类名改成"Main"，可以通过所有测试用例
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code07_MiningFarm1 {

	/**
	 * 最大顶点数，题目中n最大为1000
	 */
	public static int MAXN = 1001;
	
	/**
	 * 最大边数，题目中m最大为1000
	 */
	public static int MAXM = 1001;
	
	/**
	 * 测试用例编号t，顶点数n和边数m
	 */
	public static int t, n, m;

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
	 * 顶点栈，用于存储当前路径上的顶点
	 */
	public static int[] sta = new int[MAXN];
	
	/**
	 * 栈顶指针
	 */
	public static int top;

	/**
	 * cutVertex数组，记录每个顶点是否为割点
	 */
	public static boolean[] cutVertex = new boolean[MAXN];
	
	/**
	 * vbccSiz数组，记录每个点双连通分量的大小
	 */
	public static int[] vbccSiz = new int[MAXN];
	
	/**
	 * vbccArr数组，存储所有点双连通分量的节点
	 */
	public static int[] vbccArr = new int[MAXN << 1];
	
	/**
	 * vbccl数组，记录每个点双连通分量在vbccArr中的起始位置
	 */
	public static int[] vbccl = new int[MAXN];
	
	/**
	 * vbccr数组，记录每个点双连通分量在vbccArr中的结束位置
	 */
	public static int[] vbccr = new int[MAXN];
	
	/**
	 * vbccArr数组的当前索引
	 */
	public static int idx;
	
	/**
	 * 点双连通分量计数器
	 */
	public static int vbccCnt;

	/**
	 * ans1表示至少需要的救援点数量，ans2表示设立救援点的方案总数
	 */
	public static long ans1, ans2;

	/**
	 * prepare函数，初始化所有数据结构
	 */
	public static void prepare() {
		cntg = cntd = top = idx = vbccCnt = 0;  // 初始化计数器和栈顶指针
		for (int i = 1; i < MAXN; i++) {
			head[i] = dfn[i] = low[i] = 0;  // 初始化邻接表、dfn和low数组
			cutVertex[i] = false;  // 初始化割点数组
		}
		n = 0;  // 初始化顶点数n
	}

	/**
	 * 向邻接表中添加一条边
	 * @param u 边的起点
	 * @param v 边的终点
	 */
	public static void addEdge(int u, int v) {
		nxt[++cntg] = head[u];  // 将新边的nxt指向当前head[u]
		to[cntg] = v;  // 设置新边的终点为v
		head[u] = cntg;  // 更新head[u]为新边的索引
	}

	/**
	 * Tarjan算法求解点双连通分量和割点
	 * @param u 当前访问的顶点
	 * @param root 是否为根节点
	 */
	public static void tarjan(int u, boolean root) {
		dfn[u] = low[u] = ++cntd;  // 初始化dfn和low值为当前时间戳
		sta[++top] = u;  // 将当前顶点压入顶点栈
		int son = 0;  // 记录子节点数量
		
		// 遍历当前顶点的所有邻边
		for (int e = head[u]; e > 0; e = nxt[e]) {
			int v = to[e];  // 获取边的终点
			
			if (dfn[v] == 0) {  // 如果v未被访问过
				son++;  // 子节点数量加1
				tarjan(v, false);  // 递归访问v
				low[u] = Math.min(low[u], low[v]);  // 更新low[u]为low[u]和low[v]的最小值
				
				// 如果low[v] >= dfn[u]，说明v无法通过非父子边回溯到u的祖先
				if (low[v] >= dfn[u]) {
					// 如果不是根节点或者根节点有至少2个子节点，则u是割点
					if (!root || son >= 2) {
						cutVertex[u] = true;
					}
					
					vbccCnt++;  // 点双连通分量计数器加1
					vbccSiz[vbccCnt] = 1;  // 初始化点双连通分量大小为1（包含u）
					vbccArr[++idx] = u;  // 将u添加到vbccArr数组
					vbccl[vbccCnt] = idx;  // 记录点双连通分量的起始位置
					
					// 弹出顶点栈中的顶点，直到弹出v
					int pop;
					do {
						pop = sta[top--];  // 弹出顶点栈顶元素
						vbccSiz[vbccCnt]++;  // 点双连通分量大小加1
						vbccArr[++idx] = pop;  // 将弹出的顶点添加到vbccArr数组
					} while (pop != v);  // 直到弹出v为止
					
					vbccr[vbccCnt] = idx;  // 记录点双连通分量的结束位置
				}
			} else {  // 如果v已被访问过，说明是回边
				low[u] = Math.min(low[u], dfn[v]);  // 更新low[u]为low[u]和dfn[v]的最小值
			}
		}
	}

	/**
	 * compute函数，计算至少需要的救援点数量和设立救援点的方案总数
	 */
	public static void compute() {
		if (vbccCnt == 1) {  // 如果只有一个点双连通分量
			ans1 = 2;  // 至少需要2个救援点
			ans2 = n * (n - 1) / 2;  // 方案总数为n*(n-1)/2
		} else {  // 如果有多个点双连通分量
			ans1 = 0;  // 初始化至少需要的救援点数量
			ans2 = 1;  // 初始化方案总数
			for (int i = 1; i <= vbccCnt; i++) {  // 遍历每个点双连通分量
				int siz = vbccSiz[i], cut = 0;  // 获取点双连通分量大小和割点数量
				for (int j = vbccl[i]; j <= vbccr[i]; j++) {  // 遍历点双连通分量中的每个顶点
					if (cutVertex[vbccArr[j]]) {  // 如果是割点
						cut++;  // 割点数量加1
					}
				}
				if (cut == 1) {  // 如果割点数量为1
					ans1 += 1;  // 至少需要的救援点数量加1
					ans2 = ans2 * (siz - 1);  // 方案总数乘以（点双连通分量大小-1）
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
		FastReader in = new FastReader(System.in);  // 创建FastReader对象
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));  // 创建PrintWriter对象
		t = 0;  // 初始化测试用例编号
		m = in.nextInt();  // 读取边数m
		
		while (m != 0) {  // 当m不为0时
			prepare();  // 初始化数据结构
			for (int i = 1, u, v; i <= m; i++) {  // 读取m条边
				u = in.nextInt();  // 读取边的起点u
				v = in.nextInt();  // 读取边的终点v
				n = Math.max(n, u);  // 更新顶点数n
				n = Math.max(n, v);  // 更新顶点数n
				addEdge(u, v);  // 添加边u->v
				addEdge(v, u);  // 添加边v->u（无向图）
			}
			tarjan(1, true);  // 调用Tarjan算法
			compute();  // 计算结果
			out.println("Case " + (++t) + ": " + ans1 + " " + ans2);  // 输出结果
			m = in.nextInt();  // 读取下一个测试用例的边数m
		}
		
		out.flush();  // 刷新输出缓冲区
		out.close();  // 关闭输出流
	}

	/**
	 * FastReader类，用于快速读取输入
	 */
	static class FastReader {
		private final byte[] buffer = new byte[1 << 16];  // 输入缓冲区
		private int ptr = 0, len = 0;  // 缓冲区指针和长度
		private final InputStream in;  // 输入流

		/**
		 * FastReader构造函数
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
			if (ptr >= len) {
				len = in.read(buffer);  // 从输入流读取数据到缓冲区
				ptr = 0;
				if (len <= 0)
					return -1;
			}
			return buffer[ptr++];  // 返回缓冲区中的字节
		}

		/**
		 * 读取一个整数
		 * @return 读取的整数
		 * @throws IOException 可能抛出的IO异常
		 */
		int nextInt() throws IOException {
			int c;
			do {
				c = readByte();  // 读取字节，跳过空白字符
			} while (c <= ' ' && c != -1);
			
			boolean neg = false;
			if (c == '-') {  // 处理负数
				neg = true;
				c = readByte();
			}
			
			int val = 0;
			while (c > ' ' && c != -1) {  // 读取数字字符
				val = val * 10 + (c - '0');  // 计算整数值
				c = readByte();
			}
			
			return neg ? -val : val;  // 返回整数，负数则取反
		}
	}

}
