package class189; // 声明包名

// 稳定婚姻，java版
// 一共有n对婚姻关系，每对婚姻关系给定(女方名, 男方名)，一共2n个名字
// 一共有m对暧昧关系，每对暧昧关系给定(女方名, 男方名)，不会出现新名字
// 如果一对婚姻关系破裂，那么女方或者男方，可能会找暧昧关系的对象私奔
// 私奔会制造更多的婚姻破裂，并且产生进一步的私奔，事情好像多米诺骨牌一样展开
// 如果所有涉事男女都能重新搭配，只要存在这种可能性，那么这对婚姻关系就是不稳定的
// 比如A为女，B为男，婚姻关系(A1, B1)、(A2, B2)，同时暧昧关系(A1, B2)、(A2, B1)
// A1和B1婚姻破裂，导致B1和A2私奔了，导致A2和B2婚姻破裂，导致B2和A1私奔了，这就是重新搭配
// 检查每对婚姻关系，如果不稳定打印"Unsafe"，如果稳定打印"Safe"
// 1 <= n <= 4000    0 <= m <= 20000
// 测试链接 : https://www.luogu.com.cn/problem/P1407
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException; // IO异常
import java.io.InputStream; // 输入流
import java.io.OutputStreamWriter; // 输出流写入器
import java.io.PrintWriter; // 打印写入器
import java.util.HashMap; // 哈希映射

/**
 * Code10_SafeMarriage1 类 - 稳定婚姻问题
 * 
 * 【问题分析】
 * 判断每对婚姻关系是否稳定
 * 不稳定：存在一种重新搭配的可能性，使得所有涉事男女都能配对
 * 
 * 【建模思路】
 * 1. 将婚姻关系和暧昧关系建图
 * 2. 婚姻关系：女 -> 男（表示女方属于男方）
 * 3. 暧昧关系：男 -> 女（表示男方可以找女方私奔）
 * 4. 如果一对婚姻中的男女双方在同一SCC，则不稳定
 * 
 * 【原理】
 * 如果男女在同一SCC，说明存在从女到男和从男到女的路径
 * 这意味着可以通过一系列"私奔"事件重新搭配
 */
public class Code10_SafeMarriage1 {

	// ==================== 常量定义 ====================
	public static int MAXN = 10001;   // 最大节点数（2n）
	public static int MAXM = 30001;   // 最大边数（n + m）
	public static int n, m;           // 婚姻关系数和暧昧关系数
	
	// 存储婚姻关系
	public static int[] a = new int[MAXN];  // 女方编号
	public static int[] b = new int[MAXN];  // 男方编号
	public static int cntn;                 // 当前分配的节点数
	public static HashMap<String, Integer> nameId = new HashMap<>();  // 名字到编号的映射

	// ==================== 邻接表 ====================
	public static int[] head = new int[MAXN];
	public static int[] nxt = new int[MAXM];
	public static int[] to = new int[MAXM];
	public static int cntg;

	// ==================== Tarjan算法 ====================
	public static int[] dfn = new int[MAXN];
	public static int[] low = new int[MAXN];
	public static int cntd;
	public static int[] sta = new int[MAXN];
	public static int top;

	// ==================== SCC结果 ====================
	public static int[] belong = new int[MAXN];
	public static int sccCnt;

	// ==================== 添加边 ====================
	public static void addEdge(int u, int v) {
		nxt[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
	}

	// ==================== Tarjan算法 ====================
	public static void tarjan(int u) {
		dfn[u] = low[u] = ++cntd;
		sta[++top] = u;
		for (int e = head[u]; e > 0; e = nxt[e]) {
			int v = to[e];
			if (dfn[v] == 0) {
				tarjan(v);
				low[u] = Math.min(low[u], low[v]);
			} else if (belong[v] == 0) {
				low[u] = Math.min(low[u], dfn[v]);
			}
		}
		if (dfn[u] == low[u]) {
			sccCnt++;
			int pop;
			do {
				pop = sta[top--];
				belong[pop] = sccCnt;
			} while (pop != u);
		}
	}

	// ==================== 主函数 ====================
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		n = in.nextInt();  // 读取婚姻关系数
		
		String girl, boy;
		// 读取n对婚姻关系
		for (int i = 1; i <= n; i++) {
			girl = in.nextString();  // 女方姓名
			boy = in.nextString();   // 男方姓名
			a[i] = ++cntn;           // 为女方分配编号
			b[i] = ++cntn;           // 为男方分配编号
			nameId.put(girl, a[i]);  // 记录映射
			nameId.put(boy, b[i]);
			addEdge(a[i], b[i]);     // 婚姻关系：女 -> 男
		}
		
		m = in.nextInt();  // 读取暧昧关系数
		
		// 读取m对暧昧关系
		for (int i = 1; i <= m; i++) {
			girl = in.nextString();  // 女方姓名
			boy = in.nextString();   // 男方姓名
			// 暧昧关系：男 -> 女（男方可以找女方私奔）
			addEdge(nameId.get(boy), nameId.get(girl));
		}
		
		// 运行Tarjan算法
		for (int i = 1; i <= cntn; i++) {
			if (dfn[i] == 0) tarjan(i);
		}
		
		// 检查每对婚姻关系
		for (int i = 1; i <= n; i++) {
			// 如果夫妻双方在同一SCC，则不稳定
			if (belong[a[i]] == belong[b[i]]) {
				out.println("Unsafe");
			} else {
				out.println("Safe");
			}
		}
		
		out.flush();
		out.close();
	}

	// ==================== 快速读入（支持字符串） ====================
	static class FastReader {
		private final byte[] buffer = new byte[1 << 16];
		private int ptr = 0, len = 0;
		private final InputStream in;

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
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			boolean neg = false;
			if (c == '-') {
				neg = true;
				c = readByte();
			}
			int val = 0;
			while (c > ' ' && c != -1) {
				val = val * 10 + (c - '0');
				c = readByte();
			}
			return neg ? -val : val;
		}

		// 判断是否为字母
		boolean isLetter(int c) {
			return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
		}

		// 读取字符串（只包含字母）
		String nextString() throws IOException {
			int c;
			do {
				c = readByte();
				if (c == -1) return null;
			} while (!isLetter(c));
			StringBuilder sb = new StringBuilder();
			while (isLetter(c)) {
				sb.append((char) c);
				c = readByte();
				if (c == -1) break;
			}
			return sb.toString();
		}
	}
}
