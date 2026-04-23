package class189; // 声明包名

// 强连通分量模版题3，java版
// 一共有n个节点，给定m条边，边的格式 a b t
// 如果t为1，表示a到b的单向边，如果t为2，表示a到b的双向边
// 找到图中最大的强连通分量，先打印大小，然后打印包含的节点，编号从小到大输出
// 如果有多个最大的强连通分量，打印字典序最小的结果
// 1 <= n <= 5 * 10^3
// 0 <= m <= 5 * 10^4
// 测试链接 : https://www.luogu.com.cn/problem/P1726
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException; // IO异常处理
import java.io.InputStream; // 输入流
import java.io.OutputStreamWriter; // 输出流写入器
import java.io.PrintWriter; // 打印写入器

/**
 * Code03_SccThird1 类 - 强连通分量模板题3
 * 
 * 【题目特点】
 * 1. 边有两种类型：单向边(t=1)和双向边(t=2)
 * 2. 需要找到最大的强连通分量
 * 3. 多个最大SCC时，输出字典序最小的
 * 
 * 【解题思路】
 * 1. 根据边的类型建图（双向边需要添加两条单向边）
 * 2. 使用Tarjan算法找出所有强连通分量
 * 3. 统计每个SCC的大小，找出最大的
 * 4. 遍历节点，找到第一个属于最大SCC的节点，输出该SCC的所有节点
 */
public class Code03_SccThird1 {

	// ==================== 常量定义 ====================
	public static int MAXN = 5001;     // 最大节点数 5*10^3+1
	public static int MAXM = 100001;   // 最大边数（双向边占两条）
	public static int n, m;            // 节点数和边数

	// ==================== 邻接表 ====================
	public static int[] head = new int[MAXN];  // 邻接表头
	public static int[] nxt = new int[MAXM];   // 下一条边
	public static int[] to = new int[MAXM];    // 目标节点
	public static int cntg;                    // 边计数器

	// ==================== Tarjan算法 ====================
	public static int[] dfn = new int[MAXN];   // DFS序
	public static int[] low = new int[MAXN];   // Low Link
	public static int cntd;                    // 时间戳计数器
	public static int[] sta = new int[MAXN];   // 栈
	public static int top;                     // 栈顶指针

	// ==================== SCC结果 ====================
	public static int[] belong = new int[MAXN]; // 节点所属SCC
	public static int[] sccSiz = new int[MAXN]; // 每个SCC的大小
	public static int sccCnt;                   // SCC数量

	// ==================== 迭代版辅助结构 ====================
	public static int[][] stack = new int[MAXN][3]; // 模拟递归栈
	public static int u, status, e;                 // 当前状态
	public static int stacksize;                    // 栈大小

	// ==================== 迭代版辅助方法 ====================
	public static void push(int u, int status, int e) {
		stack[stacksize][0] = u;      // 存储节点
		stack[stacksize][1] = status; // 存储状态
		stack[stacksize][2] = e;      // 存储边
		stacksize++;                  // 栈大小加1
	}

	public static void pop() {
		stacksize--;                  // 栈大小减1
		u = stack[stacksize][0];      // 恢复节点
		status = stack[stacksize][1]; // 恢复状态
		e = stack[stacksize][2];      // 恢复边
	}

	// ==================== 添加边 ====================
	/**
	 * 添加边到邻接表
	 * @param u 起点
	 * @param v 终点
	 */
	public static void addEdge(int u, int v) {
		nxt[++cntg] = head[u];  // 新边next指向原头
		to[cntg] = v;           // 设置目标
		head[u] = cntg;         // 更新头指针
	}

	// ==================== 递归版Tarjan ====================
	/**
	 * 递归版Tarjan算法
	 * @param u 当前节点
	 */
	public static void tarjan1(int u) {
		dfn[u] = low[u] = ++cntd;  // 初始化dfn和low
		sta[++top] = u;            // 节点入栈
		// 遍历邻接边
		for (int e = head[u]; e > 0; e = nxt[e]) {
			int v = to[e];         // 邻接节点
			if (dfn[v] == 0) {     // 树边
				tarjan1(v);        // 递归处理
				low[u] = Math.min(low[u], low[v]);  // 更新low
			} else {
				if (belong[v] == 0) {  // 回边
					low[u] = Math.min(low[u], dfn[v]);
				}
			}
		}
		// 检查是否为SCC根
		if (dfn[u] == low[u]) {
			sccCnt++;              // SCC计数加1
			int pop;               // 临时变量
			// 弹出栈中节点
			do {
				pop = sta[top--];  // 弹出
				belong[pop] = sccCnt;  // 标记所属SCC
				sccSiz[sccCnt]++;      // SCC大小加1
			} while (pop != u);
		}
	}

	// ==================== 迭代版Tarjan ====================
	/**
	 * 迭代版Tarjan算法
	 * @param node 起始节点
	 */
	public static void tarjan2(int node) {
		stacksize = 0;             // 初始化栈
		push(node, -1, -1);        // 压入起始节点
		int v;                     // 临时变量
		while (stacksize > 0) {    // 当栈不为空
			pop();                 // 弹出栈顶
			if (status == -1) {    // 第一次访问
				dfn[u] = low[u] = ++cntd;  // 初始化
				sta[++top] = u;        // 入栈
				e = head[u];           // 获取第一条边
			} else {               // 从子节点返回
				v = to[e];
				if (status == 0) {   // 树边返回
					low[u] = Math.min(low[u], low[v]);
				}
				if (status == 1 && belong[v] == 0) {  // 回边
					low[u] = Math.min(low[u], dfn[v]);
				}
				e = nxt[e];          // 下一条边
			}
			if (e != 0) {          // 还有边未处理
				v = to[e];
				if (dfn[v] == 0) { // 树边
					push(u, 0, e);
					push(v, -1, -1);
				} else {           // 回边
					push(u, 1, e);
				}
			} else {               // 所有边处理完毕
				if (dfn[u] == low[u]) {  // SCC根
					sccCnt++;
					int pop;
					do {
						pop = sta[top--];
						belong[pop] = sccCnt;
						sccSiz[sccCnt]++;
					} while (pop != u);
				}
			}
		}
	}

	// ==================== 主函数 ====================
	/**
	 * 主函数
	 * @param args 命令行参数
	 * @throws Exception 异常
	 */
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);  // 快速读入
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));  // 快速输出
		
		n = in.nextInt();  // 读取节点数
		m = in.nextInt();  // 读取边数
		
		// 读取m条边
		for (int i = 1, a, b, t; i <= m; i++) {
			a = in.nextInt();  // 起点
			b = in.nextInt();  // 终点
			t = in.nextInt();  // 类型
			if (t == 1) {      // 单向边
				addEdge(a, b);
			} else {           // 双向边，添加两条
				addEdge(a, b);
				addEdge(b, a);
			}
		}
		
		// 运行Tarjan算法
		for (int i = 1; i <= n; i++) {
			if (dfn[i] == 0) {
				tarjan2(i);
			}
		}
		
		// 找出最大的强连通分量
		int largest = 0;  // 最大SCC大小
		for (int i = 1; i <= sccCnt; i++) {
			largest = Math.max(largest, sccSiz[i]);  // 更新最大值
		}
		
		// 输出最大SCC的大小
		out.println(largest);
		
		// 找到并输出字典序最小的最大SCC
		for (int i = 1; i <= n; i++) {
			if (sccSiz[belong[i]] == largest) {  // 找到最大SCC
				int scc = belong[i];  // SCC编号
				// 输出该SCC的所有节点（按编号升序）
				for (int j = i; j <= n; j++) {
					if (belong[j] == scc) {
						out.print(j + " ");
					}
				}
				break;  // 已找到字典序最小的，退出
			}
		}
		out.println();  // 换行
		out.flush();    // 刷新缓冲区
		out.close();    // 关闭输出流
	}

	// ==================== 快速读入 ====================
	static class FastReader {
		private final byte[] buffer = new byte[1 << 16];  // 缓冲区
		private int ptr = 0, len = 0;  // 指针和长度
		private final InputStream in;  // 输入流

		FastReader(InputStream in) {
			this.in = in;
		}

		private int readByte() throws IOException {
			if (ptr >= len) {  // 需要重新填充
				len = in.read(buffer);
				ptr = 0;
				if (len <= 0) return -1;
			}
			return buffer[ptr++];
		}

		int nextInt() throws IOException {
			int c;
			do {  // 跳过空白
				c = readByte();
			} while (c <= ' ' && c != -1);
			boolean neg = false;
			if (c == '-') {  // 负数
				neg = true;
				c = readByte();
			}
			int val = 0;
			while (c > ' ' && c != -1) {  // 读取数字
				val = val * 10 + (c - '0');
				c = readByte();
			}
			return neg ? -val : val;
		}
	}
}
