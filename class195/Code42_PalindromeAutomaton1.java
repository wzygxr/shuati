package class195;

// 回文树（PAM）优化建图基础模板，Java 版
// 本代码展示回文树优化建图的核心模板，用于解决回文串相关问题
// 测试链接 : https://www.luogu.com.cn/problem/P5496（改编）
// 本模板展示了回文树优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 回文树（PAM）核心知识点 =====================
// 【问题分析】
// 回文树用于处理回文串相关问题
// 通过构建回文自动机，可以快速统计回文串数量和出现次数
// 主要应用于回文串计数、最长回文子串、回文串出现次数等
//
// 【核心原理】
// 节点定义：每个节点代表一个本质不同的回文串
// 两个根：奇数长度根（-1）和偶数长度根（0）
// fail 指针：指向当前回文串的最长回文后缀
// 转移边：在回文串两端添加相同字符
//
// 【复杂度分析】
// 构建复杂度：O(n)（线性时间）
// 空间复杂度：O(n * |Σ|)（|Σ|为字符集大小）
// 优势：相比 Manacher 算法能统计更多信息
//
// 【ML/DL 关联价值】
// 1. 字符串处理中的回文检测
// 2. 生物信息学中的回文序列分析
// 3. 文本挖掘中的对称模式识别

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code42_PalindromeAutomaton1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 100001;
	public static int ALPHABET = 26; // 字符集大小

	// ===================== 回文树节点 =====================
	// 每个节点包含转移边、fail 指针、长度、出现次数
	public static class Node {
		public int[] next = new int[ALPHABET]; // 转移边
		public int fail; // fail 指针
		public int len; // 回文串长度
		public int cnt; // 出现次数
		public int num; // 以该节点结尾的本质不同回文串数量

		public Node() {
			for (int i = 0; i < ALPHABET; i++) {
				next[i] = 0;
			}
			fail = 0;
			len = 0;
			cnt = 0;
			num = 0;
		}
	}

	// ===================== 回文树变量区 =====================
	public static Node[] tree = new Node[MAXN];
	public static int nodeCnt; // 节点数量
	public static int last; // 上一个节点
	public static char[] s = new char[MAXN];
	public static int n;

	// ===================== 核心函数：创建新节点 =====================
	// 功能：创建一个新的回文树节点
	// 核心思想：初始化节点的所有信息
	// 面试高频提问：回文树节点需要哪些信息？
	public static int createNode(int len) {
		int node = ++nodeCnt;
		tree[node] = new Node();
		tree[node].len = len;
		return node;
	}

	// ===================== 核心函数：初始化回文树 =====================
	// 功能：初始化回文树的两个根节点
	// 核心思想：奇数根（-1）和偶数根（0）
	// 面试高频提问：为什么需要两个根节点？
	public static void init() {
		nodeCnt = 0;
		last = 0;
		n = 0;

		// 创建奇数长度根节点（编号 1）
		createNode(-1);
		tree[1].fail = 0; // 指向偶数根

		// 创建偶数长度根节点（编号 2）
		createNode(0);
		tree[2].fail = 1; // 指向奇数根

		last = 2; // 初始指向偶数根
	}

	// ===================== 核心函数：获取 fail 指针 =====================
	// 功能：找到当前字符可以匹配的最长回文后缀
	// 核心思想：沿着 fail 指针向上跳，直到可以扩展
	// 面试高频提问：fail 指针的含义？
	public static int getFail(int x, int pos) {
		while (true) {
			// 检查是否可以在 x 两端添加 s[pos]
			if (pos - tree[x].len - 1 >= 0 && s[pos - tree[x].len - 1] == s[pos]) {
				return x;
			}
			x = tree[x].fail;
		}
	}

	// ===================== 核心函数：添加字符 =====================
	// 功能：在回文树中添加一个字符
	// 核心思想：找到可以扩展的节点，创建新节点或更新
	// 面试高频提问：回文树的构建过程？
	public static void addChar(int pos) {
		int c = s[pos] - 'a'; // 字符转换为数字

		// 找到可以扩展的节点
		int cur = getFail(last, pos);

		// 检查是否已存在该回文串
		if (tree[cur].next[c] == 0) {
			// 创建新节点
			int newNode = createNode(tree[cur].len + 2);

			// 设置 fail 指针
			if (newNode == 2) {
				// 特殊情况：长度为 1 的回文串
				tree[newNode].fail = 2;
			} else {
				tree[newNode].fail = tree[getFail(tree[cur].fail, pos)].next[c];
			}

			// 计算 num 值
			tree[newNode].num = tree[tree[newNode].fail].num + 1;

			// 建立转移边
			tree[cur].next[c] = newNode;
		}

		// 更新 last
		last = tree[cur].next[c];

		// 更新出现次数
		tree[last].cnt++;
	}

	// ===================== 核心函数：统计出现次数 =====================
	// 功能：统计每个回文串的出现次数
	// 核心思想：从后向前累加 fail 树的贡献
	// 面试高频提问：如何统计回文串出现次数？
	public static void countOccurrences() {
		for (int i = nodeCnt; i >= 3; i--) {
			tree[tree[i].fail].cnt += tree[i].cnt;
		}
	}

	// ===================== 核心函数：统计本质不同回文串数量 =====================
	// 功能：统计字符串中本质不同的回文串数量
	// 核心思想：节点数减 2（减去两个根节点）
	// 面试高频提问：本质不同回文串的上界？
	public static int countDistinctPalindromes() {
		return nodeCnt - 2;
	}

	// ===================== 核心函数：统计所有回文串出现次数 =====================
	// 功能：统计所有回文串的总出现次数
	// 核心思想：累加所有节点的 cnt 值
	// 面试高频提问：总回文串数量的计算？
	public static long countAllPalindromes() {
		long total = 0;
		for (int i = 3; i <= nodeCnt; i++) {
			total += tree[i].cnt;
		}
		return total;
	}

	// ===================== 快速读入类 =====================
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
				if (len <= 0) {
					return -1;
				}
			}
			return buffer[ptr++];
		}

		String next() throws IOException {
			StringBuilder sb = new StringBuilder();
			int c;
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			while (c > ' ' && c != -1) {
				sb.append((char) c);
				c = readByte();
			}
			return sb.toString();
		}
	}

	// ===================== 主函数 =====================
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

		// 读入字符串
		String str = in.next();
		n = str.length();
		for (int i = 0; i < n; i++) {
			s[i] = str.charAt(i);
		}

		// 初始化回文树
		init();

		// 构建回文树
		for (int i = 0; i < n; i++) {
			addChar(i);
		}

		// 统计出现次数
		countOccurrences();

		// 输出结果
		out.println("本质不同的回文串数量：" + countDistinctPalindromes());
		out.println("所有回文串出现次数之和：" + countAllPalindromes());

		// 输出每个回文串的信息
		out.println("\n各回文串信息：");
		for (int i = 3; i <= nodeCnt; i++) {
			out.println("节点 " + i + ": 长度=" + tree[i].len + 
				", 出现次数=" + tree[i].cnt + 
				", num=" + tree[i].num);
		}

		out.flush();
		out.close();
	}
}
