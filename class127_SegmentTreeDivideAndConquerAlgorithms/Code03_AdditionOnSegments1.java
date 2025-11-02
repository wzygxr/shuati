package class167;

/**
 * 打印所有合法数 (Addition On Segments) - Java实现
 * 
 * 问题描述：
 * - 初始有一个长度为n的序列，所有值都是0
 * - 有q条操作，每条操作为[l, r, k]：将序列区间[l..r]的每个数字加k
 * - 每条操作可以选择执行或不执行，每条操作最多执行一次
 * - 如果能让序列中的最大值正好为v，那么v就是一个合法数
 * - 要求找出1~n范围内的所有合法数，并按升序输出
 * 
 * 算法思路：线段树分治 + 位运算动态规划
 * 
 * 核心思想：
 * 1. 将每个操作的区间[l, r]映射到线段树的节点上
 * 2. 使用位集(bitset)表示可达的值，其中dp[i]=1表示值i是可达的
 * 3. 对于每个区间操作，相当于对当前位集进行左移k位再或运算（表示可以选择执行该操作）
 * 4. 通过深度优先搜索遍历线段树，在回溯时维护位集的状态
 * 
 * 位运算优化：
 * - 使用整数数组模拟bitset，提高位操作效率
 * - 采用块级左移而非逐位左移，大大提高效率
 * - 使用克隆和备份数组实现状态恢复
 * 
 * 数据规模限制：
 * - n: 序列长度
 * - q: 操作数量
 * - 1 <= k <= n, q <= 10^4
 * 
 * 时间复杂度：O(q log n + n^2 / w)，其中w是机器字长（这里取32）
 * - 线段树分治：O(q log n)
 * - 位运算操作：O(n / w) per operation
 * 
 * 空间复杂度：O(n log n + n / w)
 * - 线段树和位集数组：O(n log n + n / w)
 * 
 * 测试链接：
 * - Codeforces: https://codeforces.com/problemset/problem/981/E
 * - 洛谷: https://www.luogu.com.cn/problem/CF981E
 * 
 * 使用说明：
 * - 提交时请将类名修改为"Main"
 * - 确保输入输出按照题目要求的格式
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code03_AdditionOnSegments1 {

	public static int MAXN = 10001;
	public static int MAXT = 500001;
	public static int BIT = 10000;
	public static int DEEP = 20;
	public static int INT_BIT = 32;
	public static int LEN = BIT / INT_BIT + 1;
	public static int n, q;

	public static int[] head = new int[MAXN << 2];
	public static int[] next = new int[MAXT];
	public static int[] to = new int[MAXT];
	public static int cnt = 0;

	public static int[] tmp = new int[LEN];
	public static int[] dp = new int[LEN];
	public static int[][] backup = new int[DEEP][LEN];
	public static int[] ans = new int[LEN];

	/**
	 * 清空位集（将所有位设置为0）
	 * 
	 * @param bitset 要清空的位集数组
	 */
	public static void clear(int[] bitset) {
		for (int i = 0; i < LEN; i++) {
			bitset[i] = 0;
		}
	}

	/**
	 * 克隆位集
	 * 
	 * @param set1 目标位集（被复制到的数组）
	 * @param set2 源位集（被复制的数组）
	 */
	public static void clone(int[] set1, int[] set2) {
		for (int i = 0; i < LEN; i++) {
			set1[i] = set2[i];
		}
	}

	/**
	 * 获取位集指定位置的状态
	 * 
	 * @param bitset 位集数组
	 * @param i 要查询的位位置
	 * @return 该位的状态（0或1）
	 */
	public static int getBit(int[] bitset, int i) {
		// 计算所在整数位置和偏移量，然后通过右移和与运算获取该位值
		return (bitset[i / INT_BIT] >> (i % INT_BIT)) & 1;
	}

	/**
	 * 设置位集指定位置的状态
	 * 
	 * @param bitset 位集数组
	 * @param i 要设置的位位置
	 * @param v 要设置的值（0或1）
	 */
	public static void setBit(int[] bitset, int i, int v) {
		if (v == 0) {
			// 如果设置为0，使用与运算和取反操作清除该位
			bitset[i / INT_BIT] &= ~(1 << (i % INT_BIT));
		} else {
			// 如果设置为1，使用或运算设置该位
			bitset[i / INT_BIT] |= 1 << (i % INT_BIT);
		}
	}

	/**
	 * 执行位集的或运算
	 * 
	 * @param set1 第一个位集（结果将存储在这里）
	 * @param set2 第二个位集
	 */
	public static void bitOr(int[] set1, int[] set2) {
		for (int i = 0; i < LEN; i++) {
			set1[i] |= set2[i]; // 逐整数执行或运算
		}
	}

	/**
	 * 高效实现位集左移操作
	 * 不使用逐位左移，而是采用整块左移的方式提高效率
	 * 
	 * @param ret 结果位集
	 * @param bitset 源位集
	 * @param move 左移的位数
	 */
	public static void bitLeft(int[] ret, int[] bitset, int move) {
		clear(ret); // 先清空结果数组
		
		// 特殊情况处理
		if (move > BIT) { // 左移超过最大位数，结果全0
			return;
		}
		if (move <= 0) { // 左移位数≤0，直接返回原位集
			clone(ret, bitset);
			return;
		}
		
		// 计算整数块移动次数和位偏移量
		int shift = move / INT_BIT;    // 需要移动的整数块数
		int offset = move % INT_BIT;   // 每块内部需要移动的位数
		
		if (offset == 0) {
			// 正好移动整数块的倍数
			for (int i = LEN - 1, j = i - shift; j >= 0; i--, j--) {
				ret[i] = bitset[j];
			}
		} else {
			// 非整数倍移动，需要处理进位
			int carry = INT_BIT - offset; // 进位位数
			int high, low;
			// 处理中间块，需要同时考虑当前块的高位移和前一块的低位移
			for (int i = LEN - 1; i > shift; i--) {
				high = bitset[i - shift] << offset;       // 当前块左移offset位
				low = bitset[i - shift - 1] >>> carry;    // 前一块无符号右移carry位
				ret[i] = high | low;                      // 合并高位和低位
			}
			// 处理第一个块（没有前一块）
			ret[shift] = bitset[0] << offset;
		}
		
		// 清除超出范围的高位
		// 我们只关心0到BIT位，共BIT+1个有效位
		int rest = LEN * INT_BIT - (BIT + 1);
		if (rest > 0) {
			// 计算掩码，清除无效的高位
			ret[LEN - 1] &= (1 << (INT_BIT - rest)) - 1;
		}
	}

	/**
	 * 向线段树节点添加一条边（操作k值）
	 * 使用链式前向星存储线段树节点上的操作
	 * 
	 * @param i 线段树节点编号
	 * @param v 操作的k值（要加的数值）
	 */
	public static void addEdge(int i, int v) {
		next[++cnt] = head[i]; // 新边的next指针指向前一条边
		to[cnt] = v;           // 存储操作的k值
		head[i] = cnt;         // 更新头指针
	}

	/**
	 * 线段树分治的核心方法：将操作挂载到对应的线段树节点上
	 * 
	 * @param jobl 操作的区间左端点
	 * @param jobr 操作的区间右端点
	 * @param jobv 操作的k值
	 * @param l 当前线段树节点表示的区间左端点
	 * @param r 当前线段树节点表示的区间右端点
	 * @param i 当前线段树节点编号
	 */
	public static void add(int jobl, int jobr, int jobv, int l, int r, int i) {
		// 如果当前节点区间完全包含在操作区间内
		if (jobl <= l && r <= jobr) {
			// 将操作挂载到当前节点
			addEdge(i, jobv);
		} else {
			// 否则递归到左右子节点
			int mid = (l + r) >> 1;
			if (jobl <= mid) {
				add(jobl, jobr, jobv, l, mid, i << 1); // 左子节点
			}
			if (jobr > mid) {
				add(jobl, jobr, jobv, mid + 1, r, i << 1 | 1); // 右子节点
			}
		}
	}

	/**
	 * 深度优先搜索遍历线段树，执行位运算动态规划
	 * 
	 * @param l 当前线段树节点表示的区间左端点
	 * @param r 当前线段树节点表示的区间右端点
	 * @param i 当前线段树节点编号
	 * @param dep 当前递归深度（用于状态备份）
	 */
	public static void dfs(int l, int r, int i, int dep) {
		// 备份当前dp状态，用于回溯
		clone(backup[dep], dp);
		
		// 处理当前节点上的所有操作
		for (int e = head[i]; e > 0; e = next[e]) {
			// 对当前dp状态左移k位，相当于选择执行该操作
			bitLeft(tmp, dp, to[e]);
			// 执行或运算，将选择或不选择该操作的结果合并
			bitOr(dp, tmp);
		}
		
		// 如果是叶子节点（对应单个位置）
		if (l == r) {
			// 将当前位置的可达值合并到最终结果中
			bitOr(ans, dp);
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
	 * 主函数：程序入口，负责数据输入、预处理、算法执行和结果输出
	 * 
	 * @param args 命令行参数（未使用）
	 * @throws Exception 可能出现的异常
	 */
	public static void main(String[] args) throws Exception {
		// 创建高效输入流，用于快速读取大量输入数据
		FastReader in = new FastReader(System.in);
		// 创建高效输出流，用于批量输出结果
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取序列长度n和操作数量q
		n = in.nextInt();
		q = in.nextInt();
		
		// 读取每个操作并将其添加到线段树中
		for (int i = 1, l, r, k; i <= q; i++) {
			l = in.nextInt(); // 操作区间左端点
			r = in.nextInt(); // 操作区间右端点
			k = in.nextInt(); // 增加的数值k
			// 将操作挂载到线段树的对应节点
			add(l, r, k, 1, n, 1);
		}
		
		// 初始化dp数组：dp[0]=1表示值0是初始可达的（不执行任何操作）
		setBit(dp, 0, 1);
		
		// 深度优先搜索遍历线段树，执行动态规划
		dfs(1, n, 1, 1);
		
		// 统计并输出结果
		int ansCnt = 0;
		for (int i = 1; i <= n; i++) {
			if (getBit(ans, i) == 1) {
				ansCnt++;
			}
		}
		// 输出合法数的数量
		out.println(ansCnt);
		// 输出所有合法数，按升序排列
		for (int i = 1; i <= n; i++) {
			if (getBit(ans, i) == 1) {
				out.print(i + " ");
			}
		}
		out.println();
		
		// 刷新输出缓冲区并关闭输出流
		out.flush();
		out.close();
	}

	/**
	 * 高效输入工具类，使用缓冲区优化大规模数据的输入读取
	 * 比Scanner快约10倍，适用于处理大数据量输入的竞赛题目
	 */
	static class FastReader {
		private final byte[] buffer = new byte[1 << 20]; // 1MB缓冲区
		private int ptr = 0, len = 0; // 指针位置和缓冲区有效长度
		private final InputStream in; // 输入流

		/**
		 * 构造函数：初始化输入流
		 * 
		 * @param in 输入流
		 */
		FastReader(InputStream in) {
			this.in = in;
		}

		/**
		 * 读取单个字节
		 * 如果缓冲区已读完，尝试从输入流读取新的内容
		 * 
		 * @return 读取的字节值，-1表示到达流末尾
		 * @throws IOException 输入异常
		 */
		private int readByte() throws IOException {
			if (ptr >= len) {
				len = in.read(buffer); // 从输入流读取新内容到缓冲区
				ptr = 0;
				if (len <= 0)
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
		int nextInt() throws IOException {
			int c;
			// 跳过空白字符
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			
			// 处理负数符号
			boolean neg = false;
			if (c == '-') {
				neg = true;
				c = readByte();
			}
			
			// 读取数字部分
			int val = 0;
			while (c > ' ' && c != -1) {
				val = val * 10 + (c - '0'); // 逐位构建整数
				c = readByte();
			}
			
			return neg ? -val : val; // 返回带符号的整数值
		}
	}

}
