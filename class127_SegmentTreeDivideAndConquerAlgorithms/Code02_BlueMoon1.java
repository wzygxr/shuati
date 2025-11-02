package class167;

/**
 * 贪玩蓝月问题 (Blue Moon) - Java实现
 * 
 * 问题描述：
 * - 背包是一个双端队列，可以在两端添加或删除装备
 * - 每件装备有特征值w和战斗力v
 * - 支持五种操作：前端添加、后端添加、前端删除、后端删除、查询最大战斗力
 * - 查询操作要求：选择装备的特征值累加和模p必须在[x, y]范围内，求最大可能的战斗力
 * 
 * 算法思路：线段树分治 + 动态规划（模运算下的背包问题）
 * 
 * 核心思想：
 * 1. 使用线段树分治处理装备的存在时间区间
 * 2. 对于每个装备，计算其存在的时间区间[L, R]，并将其挂载到线段树的相应节点
 * 3. 使用深度优先搜索遍历线段树，在每个节点维护动态规划数组dp[j]表示模p余j时的最大战斗力
 * 4. 回溯时恢复动态规划数组的状态，实现撤销操作
 * 
 * 动态规划状态转移：
 * - dp[j] 表示特征值总和模p等于j时的最大战斗力
 * - 转移方程：dp[(j + w) % p] = max(dp[(j + w) % p], dp[j] + v)
 * 
 * 数据规模限制：
 * - 操作数量m: 1 <= m <= 5 * 10^4
 * - 模数值p: 1 <= p <= 500
 * - 装备特征值和战斗力: 0 <= w, v <= 10^9
 * 
 * 时间复杂度：O(m log m × p)
 * - 线段树分治：O(m log m)
 * - 动态规划转移：O(p) per operation
 * 
 * 空间复杂度：O(m + p log m)
 * - 线段树和动态规划数组：O(m + p log m)
 * 
 * 测试链接：https://loj.ac/p/6515
 * 
 * 使用说明：
 * - 提交时请将类名修改为"Main"
 * - 确保输入输出按照题目要求的格式
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;

public class Code02_BlueMoon1 {

	public static int MAXM = 50001;
	public static int MAXP = 501;
	public static int MAXT = 1000001;
	public static int DEEP = 20;
	public static int m, p;

	public static int[] op = new int[MAXM];
	public static int[] x = new int[MAXM];
	public static int[] y = new int[MAXM];

	// 背包<装备特征值%p、装备战斗力、装备出现时间点>
	public static Deque<int[]> knapsack = new ArrayDeque<>();

	// 时间轴线段树的区间上挂上生效的装备，(特征值 % p)记为w，战斗力记为v
	public static int[] head = new int[MAXM << 2];
	public static int[] next = new int[MAXT];
	public static int[] tow = new int[MAXT];
	public static int[] tov = new int[MAXT];
	public static int cnt = 0;

	// 动态规划表不考虑当前装备的状态，上一行的状态
	public static long[] pre = new long[MAXP];
	// 动态规划表考虑当前装备的状态，本行的状态，需要更新
	public static long[] dp = new long[MAXP];
	// 动态规划表的备份
	public static long[][] backup = new long[DEEP][MAXP];

	// 答案
	public static long[] ans = new long[MAXM];

	/**
	 * 克隆数组内容，用于备份和恢复动态规划状态
	 * 
	 * @param a 目标数组（被复制到的数组）
	 * @param b 源数组（被复制的数组）
	 * 时间复杂度：O(p)，其中p是模数值
	 */
	public static void clone(long[] a, long[] b) {
		for (int i = 0; i < p; i++) {
			a[i] = b[i];
		}
	}

	/**
	 * 向线段树节点添加一条边（装备信息）
	 * 使用链式前向星存储线段树节点上的装备
	 * 
	 * @param i 线段树节点编号
	 * @param w 装备的特征值（已模p处理）
	 * @param v 装备的战斗力
	 */
	public static void addEdge(int i, int w, int v) {
		next[++cnt] = head[i]; // 新边的next指针指向前一条边
		tow[cnt] = w;          // 存储装备特征值
		tov[cnt] = v;          // 存储装备战斗力
		head[i] = cnt;         // 更新头指针
	}

	/**
	 * 线段树分治的核心方法：将装备挂载到对应的线段树节点上
	 * 
	 * @param jobl 装备生效的起始时间
	 * @param jobr 装备生效的结束时间
	 * @param jobw 装备的特征值（已模p处理）
	 * @param jobv 装备的战斗力
	 * @param l 当前线段树节点表示的时间区间左端点
	 * @param r 当前线段树节点表示的时间区间右端点
	 * @param i 当前线段树节点编号
	 * 时间复杂度：O(log m)，其中m是操作数量
	 */
	public static void add(int jobl, int jobr, int jobw, int jobv, int l, int r, int i) {
		// 如果当前节点区间完全包含在装备的有效时间区间内
		if (jobl <= l && r <= jobr) {
			// 将装备直接挂载到当前节点
			addEdge(i, jobw, jobv);
		} else {
			// 否则递归到左右子节点
			int mid = (l + r) >> 1;
			if (jobl <= mid) {
				add(jobl, jobr, jobw, jobv, l, mid, i << 1); // 左子节点
			}
			if (jobr > mid) {
				add(jobl, jobr, jobw, jobv, mid + 1, r, i << 1 | 1); // 右子节点
			}
		}
	}

	/**
	 * 深度优先搜索遍历线段树，处理每个节点上的装备并回答查询
	 * 
	 * @param l 当前线段树节点表示的时间区间左端点
	 * @param r 当前线段树节点表示的时间区间右端点
	 * @param i 当前线段树节点编号
	 * @param dep 当前递归深度（用于状态备份）
	 * 时间复杂度：O(p log m)，其中p是模数值，m是操作数量
	 */
	public static void dfs(int l, int r, int i, int dep) {
		// 备份当前dp状态，用于回溯
		clone(backup[dep], dp);
		
		// 处理当前节点上挂载的所有装备
		for (int e = head[i], w, v; e > 0; e = next[e]) {
			w = tow[e]; // 装备特征值
			v = tov[e]; // 装备战斗力
			
			// 复制当前dp数组到pre数组，避免覆盖影响后续计算
			clone(pre, dp);
			
			// 动态规划状态转移
			for (int j = 0; j < p; j++) {
				if (pre[j] != -1) { // 只有可达状态才进行转移
					// 计算新的余数，并更新最大值
					dp[(j + w) % p] = Math.max(dp[(j + w) % p], pre[j] + v);
				}
			}
		}
		
		// 如果是叶子节点（对应单个操作时间点）
		if (l == r) {
			// 如果是查询操作
			if (op[l] == 5) {
				long ret = -1;
				// 在指定的余数范围内寻找最大战斗力
				for (int j = x[l]; j <= y[l]; j++) {
					ret = Math.max(ret, dp[j]);
				}
				ans[l] = ret; // 保存查询结果
			}
		} else {
			// 非叶子节点，递归处理左右子树
			int mid = (l + r) >> 1;
			dfs(l, mid, i << 1, dep + 1);     // 处理左子树
			dfs(mid + 1, r, i << 1 | 1, dep + 1); // 处理右子树
		}
		
		// 回溯：恢复dp数组状态
		clone(dp, backup[dep]);
	}

	/**
	 * 预处理方法：处理所有操作，计算每个装备的存在时间区间，并初始化动态规划数组
	 * 
	 * 使用双端队列模拟背包操作，记录每个装备的生效时间和失效时间
	 * 时间复杂度：O(m)
	 */
	public static void prepare() {
		int[] equip;
		// 遍历所有操作，模拟背包的添加和删除操作
		for (int i = 1; i <= m; i++) {
			if (op[i] == 1) {
				// 前端添加装备，记录特征值（模p）、战斗力和生效时间
				knapsack.addFirst(new int[] { x[i] % p, y[i], i });
			} else if (op[i] == 2) {
				// 后端添加装备，记录特征值（模p）、战斗力和生效时间
				knapsack.addLast(new int[] { x[i] % p, y[i], i });
			} else if (op[i] == 3) {
				// 前端删除装备，计算其存在时间区间[生效时间, 失效时间-1]
				equip = knapsack.pollFirst();
				add(equip[2], i - 1, equip[0], equip[1], 1, m, 1);
			} else if (op[i] == 4) {
				// 后端删除装备，计算其存在时间区间[生效时间, 失效时间-1]
				equip = knapsack.pollLast();
				add(equip[2], i - 1, equip[0], equip[1], 1, m, 1);
			}
		}
		// 处理操作结束后仍在背包中的装备，它们的存在时间区间到m为止
		while (!knapsack.isEmpty()) {
			equip = knapsack.pollFirst();
			add(equip[2], m, equip[0], equip[1], 1, m, 1);
		}
		// 初始化动态规划数组：-1表示不可达状态，只有dp[0]=0是可达的（不选任何装备）
		for (int i = 0; i < p; i++) {
			dp[i] = -1;
		}
		dp[0] = 0; // 初始状态：不选任何装备时，特征值和为0，战斗力为0
	}

	/**
	 * 主函数：程序入口，负责数据输入、预处理、算法执行和结果输出
	 * 
	 * @param args 命令行参数（未使用）
	 * @throws IOException 可能出现的输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		// 创建高效输入流，用于快速读取大量输入数据
		FastReader in = new FastReader();
		// 创建高效输出流，用于批量输出结果
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取第一个数（未使用，可能是题目输入格式的冗余）
		in.nextInt();
		// 读取操作数量m和模数值p
		m = in.nextInt();
		p = in.nextInt();
		
		// 读取每个操作
		String t;
		for (int i = 1; i <= m; i++) {
			t = in.nextString();
			// 根据操作类型进行不同处理
			if (t.equals("IF")) {
				op[i] = 1; // 前端添加装备
				x[i] = in.nextInt(); // 特征值
				y[i] = in.nextInt(); // 战斗力
			} else if (t.equals("IG")) {
				op[i] = 2; // 后端添加装备
				x[i] = in.nextInt(); // 特征值
				y[i] = in.nextInt(); // 战斗力
			} else if (t.equals("DF")) {
				op[i] = 3; // 前端删除装备
			} else if (t.equals("DG")) {
				op[i] = 4; // 后端删除装备
			} else {
				op[i] = 5; // 查询操作
				x[i] = in.nextInt(); // 查询范围左边界
				y[i] = in.nextInt(); // 查询范围右边界
			}
		}
		
		// 预处理：计算装备的时间区间并初始化线段树
		prepare();
		// 深度优先搜索遍历线段树，处理查询
		dfs(1, m, 1, 1);
		
		// 输出所有查询操作的结果
		for (int i = 1; i <= m; i++) {
			if (op[i] == 5) {
				out.println(ans[i]);
			}
		}
		
		// 刷新输出缓冲区并关闭输出流
		out.flush();
		out.close();
	}

	/**
	 * 高效输入工具类，使用缓冲区优化大规模数据的输入读取
	 * 比Scanner快约10倍，适用于处理大数据量输入的竞赛题目
	 */
	static class FastReader {
		private static final int BUFFER_SIZE = 1 << 16; // 64KB缓冲区
		private final InputStream in;      // 输入流
		private final byte[] buffer;       // 字节缓冲区
		private int ptr, len;              // 指针位置和缓冲区有效长度

		/**
		 * 构造函数：初始化输入流和缓冲区
		 */
		public FastReader() {
			in = System.in;
			buffer = new byte[BUFFER_SIZE];
			ptr = len = 0;
		}

		/**
		 * 检查是否还有下一个字节可读
		 * 如果缓冲区已读完，尝试从输入流读取新的内容
		 * 
		 * @return 是否还有可用字节
		 * @throws IOException 输入异常
		 */
		private boolean hasNextByte() throws IOException {
			if (ptr < len) {
				return true;
			}
			ptr = 0; // 重置指针
			len = in.read(buffer); // 从输入流读取新内容到缓冲区
			return len > 0;
		}

		/**
		 * 读取单个字节
		 * 
		 * @return 读取的字节值
		 * @throws IOException 输入异常
		 */
		private byte readByte() throws IOException {
			if (!hasNextByte()) {
				return -1; // 到达流末尾
			}
			return buffer[ptr++]; // 返回当前字节并移动指针
		}

		/**
		 * 读取下一个整数
		 * 
		 * @return 读取的整数值
		 * @throws IOException 输入异常
		 */
		public int nextInt() throws IOException {
			int num = 0;
			byte b = readByte();
			// 跳过空白字符
			while (isWhitespace(b)) {
				b = readByte();
			}
			// 处理负数符号
			boolean minus = false;
			if (b == '-') {
				minus = true;
				b = readByte();
			}
			// 读取数字部分
			while (!isWhitespace(b) && b != -1) {
				num = num * 10 + (b - '0'); // 逐位构建整数
				b = readByte();
			}
			return minus ? -num : num; // 返回带符号的整数值
		}

		/**
		 * 读取下一个字符串
		 * 
		 * @return 读取的字符串
		 * @throws IOException 输入异常
		 */
		public String nextString() throws IOException {
			byte b = readByte();
			// 跳过空白字符
			while (isWhitespace(b)) {
				b = readByte();
			}
			// 使用StringBuilder高效构建字符串
			StringBuilder sb = new StringBuilder(1000);
			while (!isWhitespace(b) && b != -1) {
				sb.append((char) b);
				b = readByte();
			}
			return sb.toString();
		}

		/**
		 * 判断字节是否为空白字符（空格、换行、回车、制表符）
		 * 
		 * @param b 要检查的字节
		 * @return 是否为空白字符
		 */
		private boolean isWhitespace(byte b) {
			return b == ' ' || b == '\n' || b == '\r' || b == '\t';
		}
	}

}