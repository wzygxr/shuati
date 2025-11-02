package class177;

// 经典莫队应用：异或和为k的子区间个数
// 给定一个长度为n的数组和一个值k，有m次查询
// 每次查询[l,r]区间内，有多少个子区间[l'<=l, r'>=r]满足异或和等于k
// 1 <= n, m <= 100000
// 1 <= k, arr[i] <= 1000000
// 测试链接 : https://codeforces.com/contest/617/problem/E

// 这是普通莫队的经典应用
// 核心思想：
// 1. 使用前缀异或和将问题转化为"区间内两个相等元素的个数"问题
// 2. 如果 pre[i] ^ pre[j] = k，则 pre[i] ^ k = pre[j]
// 3. 所以我们只需要统计有多少对 (i,j) 满足 pre[i] ^ k = pre[j]

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

public class Code10_XorAndFavoriteNumber {

	public static int MAXN = 100001;
	public static int MAXV = 1 << 20; // 2^20 > 1000000
	
	public static int n, m, k;
	// 原始数组
	public static int[] arr = new int[MAXN];
	// 前缀异或和数组
	public static int[] prefix = new int[MAXN];
	// 查询: l, r, id
	public static int[][] queries = new int[MAXN][3];
	
	// 分块相关
	public static int blockSize;
	public static int blockNum;
	public static int[] belong = new int[MAXN];
	public static int[] blockRight = new int[MAXN];
	
	// 计数数组，记录每个异或值出现的次数
	public static int[] count = new int[MAXV];
	// 当前答案
	public static long currentAnswer = 0;
	public static long[] answers = new long[MAXN];
	
	// 普通莫队排序规则
	public static class QueryComparator implements Comparator<int[]> {
		@Override
		public int compare(int[] a, int[] b) {
			// 按照左端点所在块排序
			if (belong[a[0]] != belong[b[0]]) {
				return belong[a[0]] - belong[b[0]];
			}
			// 同一块内按照右端点排序
			return a[1] - b[1];
		}
	}
	
	// 添加元素到窗口右侧
	public static void addRight(int pos) {
		int val = prefix[pos];
		// 增加与当前值异或为k的值的配对数
		currentAnswer += count[val ^ k];
		// 更新计数
		count[val]++;
	}
	
	// 从窗口右侧删除元素
	public static void removeRight(int pos) {
		int val = prefix[pos];
		// 更新计数
		count[val]--;
		// 减少与当前值异或为k的值的配对数
		currentAnswer -= count[val ^ k];
	}
	
	// 添加元素到窗口左侧
	public static void addLeft(int pos) {
		int val = prefix[pos - 1];
		// 增加与当前值异或为k的值的配对数
		currentAnswer += count[val ^ k];
		// 更新计数
		count[val]++;
	}
	
	// 从窗口左侧删除元素
	public static void removeLeft(int pos) {
		int val = prefix[pos - 1];
		// 更新计数
		count[val]--;
		// 减少与当前值异或为k的值的配对数
		currentAnswer -= count[val ^ k];
	}
	
	// 主计算函数
	public static void compute() {
		// 初始化计数数组
		Arrays.fill(count, 0);
		currentAnswer = 0;
		
		int l = 1, r = 0;
		
		for (int i = 1; i <= m; i++) {
			int ql = queries[i][0];
			int qr = queries[i][1];
			int id = queries[i][2];
			
			// 调整窗口边界
			while (r < qr) addRight(++r);
			while (r > qr) removeRight(r--);
			while (l < ql) removeLeft(l++);
			while (l > ql) addLeft(--l);
			
			answers[id] = currentAnswer;
		}
	}
	
	// 预处理函数
	public static void prepare() {
		// 计算前缀异或和
		prefix[0] = 0;
		for (int i = 1; i <= n; i++) {
			prefix[i] = prefix[i - 1] ^ arr[i];
		}
		
		// 计算分块大小
		blockSize = (int) Math.sqrt(n);
		blockNum = (n + blockSize - 1) / blockSize;
		
		// 计算每个位置所属的块和块的右边界
		for (int i = 1; i <= n; i++) {
			belong[i] = (i - 1) / blockSize + 1;
		}
		for (int i = 1; i <= blockNum; i++) {
			blockRight[i] = Math.min(i * blockSize, n);
		}
		
		// 对查询进行排序
		Arrays.sort(queries, 1, m + 1, new QueryComparator());
	}
	
	public static void main(String[] args) throws IOException {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		n = in.nextInt();
		m = in.nextInt();
		k = in.nextInt();
		
		// 读取数组
		for (int i = 1; i <= n; i++) {
			arr[i] = in.nextInt();
		}
		
		// 读取查询
		for (int i = 1; i <= m; i++) {
			queries[i][0] = in.nextInt();
			queries[i][1] = in.nextInt();
			queries[i][2] = i;
		}
		
		prepare();
		compute();
		
		// 输出结果
		for (int i = 1; i <= m; i++) {
			out.println(answers[i]);
		}
		
		out.flush();
		out.close();
	}
	
	// 读写工具类
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
				if (len <= 0)
					return -1;
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
}