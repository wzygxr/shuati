package class195;

// 莫队算法优化建图基础模板，Java 版
// 本代码展示莫队算法优化建图的核心模板，用于解决离线区间查询问题
// 测试链接 : https://www.luogu.com.cn/problem/P1972（改编）
// 本模板展示了莫队算法优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 莫队算法优化建图核心知识点 =====================
// 【问题分析】
// 莫队算法用于解决离线区间查询问题
// 通过分块排序和双指针移动，优化区间查询复杂度
// 主要应用于区间不同数个数、区间众数、区间逆序对等
//
// 【核心原理】
// 分块排序：将查询按左端点所在块排序，同块内按右端点排序
// 双指针移动：通过移动左右指针维护区间信息
// 复杂度优化：O(n√n) 的总复杂度
// 奇偶优化：奇数块和偶数块右端点排序方向相反
//
// 【复杂度分析】
// 时间复杂度：O(n√n)（普通莫队）
// 空间复杂度：O(n)
// 优势：实现简单，适用于多种区间查询问题
//
// 【ML/DL 关联价值】
// 1. 离线批量查询的优化策略
// 2. 数据重排序提升缓存命中率
// 3. 滑动窗口中的增量更新思想

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code29_MoAlgorithm1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 100001;
	public static int MAXM = 200001;

	// ===================== 查询结构体 =====================
	// 存储每个查询的信息
	// 包含左右端点、查询编号、所在块等信息
	public static class Query implements Comparable<Query> {
		public int l; // 左端点
		public int r; // 右端点
		public int id; // 查询编号
		public int block; // 左端点所在块的编号

		public Query(int l, int r, int id, int blockSize) {
			this.l = l;
			this.r = r;
			this.id = id;
			this.block = (l - 1) / blockSize;
		}

		// 比较函数：莫队排序规则
		// 先按块排序，同块内按右端点排序
		// 使用奇偶优化提升性能
		@Override
		public int compareTo(Query other) {
			if (this.block != other.block) {
				return this.block - other.block;
			}
			// 奇偶优化：奇数块右端点递增，偶数块右端点递减
			return (this.block & 1) == 1 ? (this.r - other.r) : (other.r - this.r);
		}
	}

	// ===================== 数组变量区 =====================
	public static int[] a = new int[MAXN]; // 原始数组
	public static int[] cnt = new int[MAXN]; // 每个数的出现次数
	public static int[] ans = new int[MAXM]; // 每个查询的答案
	public static Query[] queries = new Query[MAXM]; // 查询数组
	public static int n, m;
	public static int blockSize; // 块的大小

	// ===================== 当前区间信息 =====================
	public static int curL, curR; // 当前区间的左右端点
	public static int curAns; // 当前区间的答案

	// ===================== 核心函数：添加元素 =====================
	// 功能：向当前区间添加一个元素
	// 核心思想：更新该元素的计数，如果从 0 变 1 则答案加 1
	// 面试高频提问：如何维护区间不同数的个数？
	public static void add(int pos) {
		int val = a[pos];
		if (cnt[val] == 0) {
			// 该数第一次出现，不同数的个数加 1
			curAns++;
		}
		cnt[val]++;
	}

	// ===================== 核心函数：删除元素 =====================
	// 功能：从当前区间删除一个元素
	// 核心思想：更新该元素的计数，如果从 1 变 0 则答案减 1
	// 面试高频提问：删除操作的注意事项？
	public static void remove(int pos) {
		int val = a[pos];
		cnt[val]--;
		if (cnt[val] == 0) {
			// 该数不再出现，不同数的个数减 1
			curAns--;
		}
	}

	// ===================== 核心函数：莫队算法主流程 =====================
	// 功能：使用莫队算法处理所有查询
	// 核心思想：通过移动左右指针维护区间信息
	// 面试高频提问：莫队算法的复杂度如何推导？
	public static void moAlgorithm() {
		// 计算块大小
		blockSize = (int) Math.sqrt(n);
		if (blockSize == 0) {
			blockSize = 1;
		}

		// 对所有查询排序
		Arrays.sort(queries, 0, m);

		// 初始化当前区间为空
		curL = 1;
		curR = 0;
		curAns = 0;

		// 处理每个查询
		for (int i = 0; i < m; i++) {
			int L = queries[i].l;
			int R = queries[i].r;

			// 移动左指针
			while (curL > L) {
				curL--;
				add(curL);
			}
			while (curR < R) {
				curR++;
				add(curR);
			}
			while (curL < L) {
				remove(curL);
				curL++;
			}
			while (curR > R) {
				remove(curR);
				curR--;
			}

			// 保存答案
			ans[queries[i].id] = curAns;
		}
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
	}

	// ===================== 主函数 =====================
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

		// 读入数组大小
		n = in.nextInt();

		// 读入数组元素
		for (int i = 1; i <= n; i++) {
			a[i] = in.nextInt();
		}

		// 读入查询
		m = in.nextInt();
		for (int i = 0; i < m; i++) {
			int l = in.nextInt();
			int r = in.nextInt();
			queries[i] = new Query(l, r, i, blockSize);
		}

		// 执行莫队算法
		moAlgorithm();

		// 输出所有查询的答案
		for (int i = 0; i < m; i++) {
			out.println("查询 " + (i + 1) + " 的答案：" + ans[i]);
		}

		out.flush();
		out.close();
	}
}
