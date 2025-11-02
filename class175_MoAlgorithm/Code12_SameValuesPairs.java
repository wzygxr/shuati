package class177;

// 回滚莫队应用：区间内相同值的数对个数
// 给定一个长度为n的数组，有m次查询
// 每次查询[l,r]区间内，值相同的数对个数
// 数对定义为(i,j)满足l<=i<j<=r且arr[i]=arr[j]
// 1 <= n, m <= 100000
// 1 <= arr[i] <= 1000000

// 回滚莫队的经典应用
// 核心思想：
// 1. 只能扩展右边界，不能收缩右边界
// 2. 可以收缩左边界，但需要通过回滚来恢复
// 3. 利用组合数学，C(n,2) = n*(n-1)/2

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

public class Code12_SameValuesPairs {

	public static int MAXN = 100001;
	public static int MAXV = 1000001;
	
	public static int n, m;
	// 原始数组
	public static int[] arr = new int[MAXN];
	// 离散化后的数组
	public static int[] sorted = new int[MAXN];
	public static int valueCount = 0;
	
	// 查询: l, r, id
	public static int[][] queries = new int[MAXN][3];
	
	// 分块相关
	public static int blockSize;
	public static int blockNum;
	public static int[] belong = new int[MAXN];
	public static int[] blockRight = new int[MAXN];
	
	// 计数和答案
	public static int[] count = new int[MAXV];  // 每个值的出现次数
	public static long currentAnswer = 0;
	public static long[] answers = new long[MAXN];
	
	// 回滚莫队排序规则
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
	
	// 二分查找离散化值
	public static int findIndex(int value) {
		int left = 1, right = valueCount;
		int result = 0;
		while (left <= right) {
			int mid = (left + right) / 2;
			if (sorted[mid] <= value) {
				result = mid;
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		return result;
	}
	
	// 暴力计算区间答案
	public static long bruteForce(int l, int r) {
		long result = 0;
		// 统计每个值的出现次数
		for (int i = l; i <= r; i++) {
			count[arr[i]]++;
		}
		// 计算数对个数
		for (int i = l; i <= r; i++) {
			result += count[arr[i]] - 1;  // 该位置的值能组成的数对数
		}
		// 清除计数
		for (int i = l; i <= r; i++) {
			count[arr[i]] = 0;
		}
		return result / 2;  // 每个数对被计算了两次
	}
	
	// 添加元素到右侧
	public static void add(int value) {
		// 增加该值能组成的数对数
		currentAnswer += count[value];
		count[value]++;
	}
	
	// 从左侧删除元素
	public static void remove(int value) {
		count[value]--;
		// 减少该值能组成的数对数
		currentAnswer -= count[value];
	}
	
	// 主计算函数
	public static void compute() {
		for (int block = 1, queryIndex = 1; block <= blockNum && queryIndex <= m; block++) {
			// 每个块开始时重置状态
			currentAnswer = 0;
			Arrays.fill(count, 0);
			
			// 当前窗口边界
			int windowLeft = blockRight[block] + 1;
			int windowRight = blockRight[block];
			
			// 处理属于当前块的所有查询
			for (; queryIndex <= m && belong[queries[queryIndex][0]] == block; queryIndex++) {
				int queryLeft = queries[queryIndex][0];
				int queryRight = queries[queryIndex][1];
				int id = queries[queryIndex][2];
				
				// 如果查询区间完全在当前块内，使用暴力方法
				if (queryRight <= blockRight[block]) {
					answers[id] = bruteForce(queryLeft, queryRight);
				} else {
					// 否则使用回滚莫队
					// 先扩展右边界到queryRight
					while (windowRight < queryRight) {
						add(arr[++windowRight]);
					}
					
					// 保存当前状态
					long backup = currentAnswer;
					
					// 扩展左边界到queryLeft
					while (windowLeft > queryLeft) {
						add(arr[--windowLeft]);
					}
					
					// 记录答案
					answers[id] = currentAnswer;
					
					// 恢复状态，只保留右边界扩展的结果
					currentAnswer = backup;
					while (windowLeft <= blockRight[block]) {
						remove(arr[windowLeft++]);
					}
				}
			}
		}
	}
	
	// 预处理函数
	public static void prepare() {
		// 离散化
		for (int i = 1; i <= n; i++) {
			sorted[i] = arr[i];
		}
		Arrays.sort(sorted, 1, n + 1);
		valueCount = 1;
		for (int i = 2; i <= n; i++) {
			if (sorted[valueCount] != sorted[i]) {
				sorted[++valueCount] = sorted[i];
			}
		}
		for (int i = 1; i <= n; i++) {
			arr[i] = findIndex(arr[i]);
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