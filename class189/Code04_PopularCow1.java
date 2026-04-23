package class189; // 声明包名

// 受欢迎的牛，java版
// 一共有n只牛，牛和牛之间存在喜欢关系，喜欢关系是有向的
// 喜欢关系可以传递，如果a喜欢b，b喜欢c，那么a也喜欢c
// 每只牛都喜欢自己，如果某只牛被所有牛喜欢，那么这只牛是明星
// 给定m个喜欢关系，打印明星的数量
// 1 <= n <= 10^4
// 1 <= m <= 5 * 10^4
// 测试链接 : https://www.luogu.com.cn/problem/P2341
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException; // IO异常
import java.io.InputStream; // 输入流
import java.io.OutputStreamWriter; // 输出流写入器
import java.io.PrintWriter; // 打印写入器

/**
 * Code04_PopularCow1 类 - 受欢迎的牛问题
 * 
 * 【问题分析】
 * 明星牛 = 被所有其他牛喜欢的牛
 * 在有向图中，如果一个节点可以被所有其他节点到达，则该节点对应明星牛
 * 
 * 【关键观察】
 * 1. 缩点后，DAG中出度为0的强连通分量内的所有牛都是明星
 * 2. 如果有多个出度为0的SCC，则不存在明星（无法互相到达）
 * 3. 明星牛数量 = 出度为0的SCC的大小（如果只有一个这样的SCC）
 * 
 * 【解题步骤】
 * 1. 使用Tarjan算法找出所有强连通分量
 * 2. 缩点，统计每个SCC的出度
 * 3. 找出出度为0的SCC
 * 4. 如果只有一个出度为0的SCC，输出其大小；否则输出0
 */
public class Code04_PopularCow1 {

	// ==================== 常量定义 ====================
	public static int MAXN = 10001;   // 最大节点数
	public static int MAXM = 50001;   // 最大边数
	public static int n, m;           // 节点数和边数
	
	// 存储原始边信息（用于缩点后计算出度）
	public static int[] a = new int[MAXM];  // 边的起点
	public static int[] b = new int[MAXM];  // 边的终点

	// ==================== 邻接表 ====================
	public static int[] head = new int[MAXN];  // 邻接表头
	public static int[] nxt = new int[MAXM];   // 下一条边
	public static int[] to = new int[MAXM];    // 目标节点
	public static int cntg;                    // 边计数器

	// ==================== Tarjan算法 ====================
	public static int[] dfn = new int[MAXN];   // DFS序
	public static int[] low = new int[MAXN];   // Low Link
	public static int cntd;                    // 时间戳
	public static int[] sta = new int[MAXN];   // 栈
	public static int top;                     // 栈顶指针

	// ==================== SCC结果 ====================
	public static int[] belong = new int[MAXN]; // 节点所属SCC
	public static int[] sccSiz = new int[MAXN]; // 每个SCC的大小
	public static int sccCnt;                   // SCC数量

	// ==================== 缩点后出度 ====================
	public static int[] outdegree = new int[MAXN]; // 每个SCC的出度

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

	// ==================== Tarjan算法 ====================
	/**
	 * Tarjan算法寻找强连通分量
	 * @param u 当前节点
	 */
	public static void tarjan(int u) {
		dfn[u] = low[u] = ++cntd;  // 初始化dfn和low
		sta[++top] = u;            // 节点入栈
		// 遍历邻接边
		for (int e = head[u]; e > 0; e = nxt[e]) {
			int v = to[e];         // 邻接节点
			if (dfn[v] == 0) {     // 树边
				tarjan(v);         // 递归处理
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

	// ==================== 主函数 ====================
	/**
	 * 主函数
	 * @param args 命令行参数
	 * @throws Exception 异常
	 */
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);  // 快速读入
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));  // 快速输出
		
		n = in.nextInt();  // 读取节点数（牛的数量）
		m = in.nextInt();  // 读取边数（喜欢关系数）
		
		// 读取m条喜欢关系
		for (int i = 1; i <= m; i++) {
			a[i] = in.nextInt();  // 起点
			b[i] = in.nextInt();  // 终点
			addEdge(a[i], b[i]);  // 添加边（a喜欢b）
		}
		
		// 运行Tarjan算法找出所有强连通分量
		for (int i = 1; i <= n; i++) {
			if (dfn[i] == 0) {  // 未访问
				tarjan(i);
			}
		}
		
		// 计算缩点后各SCC的出度
		for (int i = 1; i <= m; i++) {
			int scc1 = belong[a[i]];  // 起点所属SCC
			int scc2 = belong[b[i]];  // 终点所属SCC
			if (scc1 != scc2) {       // 不在同一SCC
				outdegree[scc1]++;    // scc1的出度加1
			}
		}
		
		// 统计出度为0的SCC
		int num = 0;   // 出度为0的SCC数量
		int siz = 0;   // 明星牛数量（出度为0的SCC大小）
		for (int i = 1; i <= sccCnt; i++) {
			if (outdegree[i] == 0) {  // 出度为0
				num++;                // 计数
				siz = sccSiz[i];      // 记录大小
			}
			// 如果有多个出度为0的SCC，不存在明星
			if (num > 1) {
				siz = 0;  // 明星数量为0
				break;
			}
		}
		
		out.println(siz);  // 输出明星牛数量
		out.flush();       // 刷新
		out.close();       // 关闭
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
			if (ptr >= len) {
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
