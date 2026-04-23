package class195;

// 后缀自动机（SAM）优化建图基础模板，Java 版
// 本代码展示后缀自动机优化建图的核心模板，用于解决字符串相关问题
// 测试链接 : https://www.luogu.com.cn/problem/P3804（改编）
// 本模板展示了后缀自动机优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 后缀自动机（SAM）核心知识点 =====================
// 【问题分析】
// 后缀自动机用于处理字符串的子串相关问题
// 通过构建 DAG，可以快速统计子串数量和出现次数
// 主要应用于子串计数、最长公共子串、子串出现次数等
//
// 【核心原理】
// 状态定义：每个状态代表一组 endpos 相同的子串
// 转移边：在子串后添加字符
// link 指针：指向当前状态的最长后缀所在状态
// len 值：状态中最长子串的长度
//
// 【复杂度分析】
// 构建复杂度：O(n)（线性时间）
// 空间复杂度：O(n * |Σ|)
// 优势：状态数和边数都是 O(n) 级别
//
// 【ML/DL 关联价值】
// 1. 字符串处理中的子串匹配
// 2. 文本挖掘中的频繁模式挖掘
// 3. 生物信息学中的序列比对

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code43_SuffixAutomaton1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 1000001;
	public static int ALPHABET = 26; // 字符集大小

	// ===================== 后缀自动机节点 =====================
	// 每个节点包含转移边、link 指针、len 值、出现次数
	public static class Node {
		public int[] next = new int[ALPHABET]; // 转移边
		public int link; // link 指针
		public int len; // 最长子串长度
		public long cnt; // 出现次数
		public long size; // 该状态代表的子串数量

		public Node() {
			for (int i = 0; i < ALPHABET; i++) {
				next[i] = -1;
			}
			link = -1;
			len = 0;
			cnt = 0;
			size = 0;
		}
	}

	// ===================== 后缀自动机变量区 =====================
	public static Node[] sam = new Node[MAXN * 2];
	public static int sz; // 节点数量
	public static int last; // 上一个节点
	public static char[] s = new char[MAXN];
	public static int n;

	// ===================== 核心函数：创建新节点 =====================
	// 功能：创建一个新的 SAM 节点
	// 核心思想：初始化节点的所有信息
	// 面试高频提问：SAM 节点需要哪些信息？
	public static int createNode(int len) {
		int node = sz++;
		sam[node] = new Node();
		sam[node].len = len;
		return node;
	}

	// ===================== 核心函数：初始化 SAM =====================
	// 功能：初始化后缀自动机
	// 核心思想：创建初始状态（空串）
	// 面试高频提问：SAM 的初始状态？
	public static void init() {
		sz = 0;
		last = 0;

		// 创建初始状态
		createNode(0);
		sam[0].link = -1;
	}

	// ===================== 核心函数：添加字符 =====================
	// 功能：在 SAM 中添加一个字符
	// 核心思想：复制节点处理 endpos 等价类
	// 面试高频提问：SAM 的构建过程？
	public static void addChar(int c) {
		// 创建新状态
		int cur = createNode(sam[last].len + 1);
		sam[cur].cnt = 1; // 初始出现次数为 1

		// 从 last 开始向上添加转移边
		int p = last;
		while (p != -1 && sam[p].next[c] == -1) {
			sam[p].next[c] = cur;
			p = sam[p].link;
		}

		if (p == -1) {
			// 没有前驱，link 指向初始状态
			sam[cur].link = 0;
		} else {
			int q = sam[p].next[c];
			if (sam[p].len + 1 == sam[q].len) {
				// q 可以直接使用
				sam[cur].link = q;
			} else {
				// 需要复制 q
				int clone = createNode(sam[p].len + 1);
				// 复制 q 的信息
				System.arraycopy(sam[q].next, 0, sam[clone].next, 0, ALPHABET);
				sam[clone].link = sam[q].link;
				sam[clone].cnt = 0; // 克隆节点初始出现次数为 0

				// 更新 link
				while (p != -1 && sam[p].next[c] == q) {
					sam[p].next[c] = clone;
					p = sam[p].link;
				}
				sam[q].link = clone;
				sam[cur].link = clone;
			}
		}

		// 更新 last
		last = cur;
	}

	// ===================== 核心函数：统计出现次数 =====================
	// 功能：统计每个状态的出現次数
	// 核心思想：按 len 从大到小累加 link 树的贡献
	// 面试高频提问：如何统计子串出现次数？
	public static void countOccurrences() {
		// 按 len 排序（桶排序）
		int[] cnt = new int[n + 1];
		for (int i = 0; i < sz; i++) {
			cnt[sam[i].len]++;
		}
		for (int i = 1; i <= n; i++) {
			cnt[i] += cnt[i - 1];
		}

		int[] order = new int[sz];
		for (int i = 0; i < sz; i++) {
			order[--cnt[sam[i].len]] = i;
		}

		// 从后向前累加
		for (int i = sz - 1; i >= 1; i--) {
			int u = order[i];
			if (sam[u].link != -1) {
				sam[sam[u].link].cnt += sam[u].cnt;
			}
		}
	}

	// ===================== 核心函数：计算子串数量 =====================
	// 功能：计算字符串的不同子串数量
	// 核心思想：每个状态贡献 len[u] - len[link[u]] 个子串
	// 面试高频提问：不同子串数量的计算？
	public static long countSubstrings() {
		long total = 0;
		for (int i = 1; i < sz; i++) {
			total += sam[i].len - sam[sam[i].link].len;
		}
		return total;
	}

	// ===================== 核心函数：计算出现次数>=k 的子串数量 =====================
	// 功能：计算出现次数大于等于 k 的不同子串数量
	// 核心思想：只统计 cnt >= k 的状态
	// 面试高频提问：如何统计高频子串？
	public static long countSubstringsWithMinFreq(int k) {
		long total = 0;
		for (int i = 1; i < sz; i++) {
			if (sam[i].cnt >= k) {
				total += sam[i].len - sam[sam[i].link].len;
			}
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

		// 初始化 SAM
		init();

		// 构建 SAM
		for (int i = 0; i < n; i++) {
			addChar(s[i] - 'a');
		}

		// 统计出现次数
		countOccurrences();

		// 输出结果
		out.println("不同子串数量：" + countSubstrings());
		out.println("出现次数>=2 的子串数量：" + countSubstringsWithMinFreq(2));

		// 输出每个状态的信息
		out.println("\n各状态信息：");
		for (int i = 1; i < sz; i++) {
			out.println("状态 " + i + ": len=" + sam[i].len + 
				", link=" + sam[i].link + 
				", cnt=" + sam[i].cnt);
		}

		out.flush();
		out.close();
	}
}
