package class177;

// 动态逆序对问题 (带修莫队应用)
// 给定一个长度为n的排列，有m次操作，每次操作会修改一个位置的值
// 每次操作后，需要输出当前排列的逆序对数量
// 1 <= n, m <= 50000
// 测试链接 : https://www.luogu.com.cn/problem/P3157

// 带修莫队的经典应用之一：动态维护逆序对数量
// 核心思想：
// 1. 将修改操作和查询操作统一处理
// 2. 使用莫队算法的状态转移来维护逆序对数量
// 3. 通过时间维度处理修改操作

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

public class Code09_DynamicInversePairs {

	public static int MAXN = 50001;
	public static int MAXM = 50001;
	
	public static int n, m;
	// 原始数组
	public static int[] arr = new int[MAXN];
	// pos[i]表示数字i在数组中的位置
	public static int[] pos = new int[MAXN];
	
	// 修改操作: time, pos, newVal, oldVal
	public static int[][] updates = new int[MAXM][4];
	// 查询操作: l, r, time, id
	public static int[][] queries = new int[MAXM][4];
	
	public static int updateTime = 0;
	public static int queryTime = 0;
	
	// 分块大小
	public static int blockSize;
	public static int blockNum;
	
	// 每个位置属于哪个块
	public static int[] belong = new int[MAXN];
	
	// 树状数组用于维护前缀和
	public static int[] tree = new int[MAXN];
	
	// 当前逆序对数量
	public static long currentPairs = 0;
	public static long[] answers = new long[MAXM];
	
	// 带修莫队排序规则
	public static class QueryComparator implements Comparator<int[]> {
		@Override
		public int compare(int[] a, int[] b) {
			// 按照左端点所在块排序
			if (belong[a[0]] != belong[b[0]]) {
				return belong[a[0]] - belong[b[0]];
			}
			// 按照右端点排序
			if (a[1] != b[1]) {
				return a[1] - b[1];
			}
			// 按照时间排序
			return a[2] - b[2];
		}
	}
	
	// 计算x的最低位1所代表的值
	public static int lowbit(int x) {
		return x & (-x);
	}
	
	// 树状数组单点更新
	public static void add(int x, int delta) {
		for (int i = x; i <= n; i += lowbit(i)) {
			tree[i] += delta;
		}
	}
	
	// 树状数组前缀和查询
	public static int sum(int x) {
		int res = 0;
		for (int i = x; i > 0; i -= lowbit(i)) {
			res += tree[i];
		}
		return res;
	}
	
	// 添加元素到窗口中
	public static void addElement(int idx) {
		int val = arr[idx];
		// 计算该元素对逆序对的贡献
		// 左边比它大的元素个数 + 右边比它小的元素个数
		currentPairs += (sum(n) - sum(val));  // 右边比它小的元素个数
		currentPairs += (idx - 1 - sum(val - 1));  // 左边比它大的元素个数
		add(val, 1);
	}
	
	// 从窗口中删除元素
	public static void removeElement(int idx) {
		int val = arr[idx];
		add(val, -1);
		// 减去该元素对逆序对的贡献
		currentPairs -= (sum(n) - sum(val));  // 右边比它小的元素个数
		currentPairs -= (idx - 1 - sum(val - 1));  // 左边比它大的元素个数
	}
	
	// 应用修改操作
	public static void applyUpdate(int time) {
		int position = updates[time][1];
		int newVal = updates[time][2];
		int oldVal = updates[time][3];
		
		// 更新数组值
		arr[position] = newVal;
		
		// 更新位置映射
		pos[oldVal] = 0;
		pos[newVal] = position;
		
		// 如果该位置在当前窗口中，重新计算贡献
		removeElement(position);
		addElement(position);
	}
	
	// 撤销修改操作
	public static void undoUpdate(int time) {
		int position = updates[time][1];
		int newVal = updates[time][2];
		int oldVal = updates[time][3];
		
		// 恢复数组值
		arr[position] = oldVal;
		
		// 更新位置映射
		pos[oldVal] = position;
		pos[newVal] = 0;
		
		// 如果该位置在当前窗口中，重新计算贡献
		removeElement(position);
		addElement(position);
	}
	
	// 主计算函数
	public static void compute() {
		// 初始化树状数组
		Arrays.fill(tree, 0);
		currentPairs = 0;
		
		// 计算初始逆序对数量
		for (int i = 1; i <= n; i++) {
			addElement(i);
		}
		
		int l = 1, r = n;
		int currentTime = 0;
		
		for (int i = 1; i <= queryTime; i++) {
			int ql = queries[i][0];
			int qr = queries[i][1];
			int qt = queries[i][2];
			int id = queries[i][3];
			
			// 调整区间边界
			while (l > ql) addElement(--l);
			while (r < qr) addElement(++r);
			while (l < ql) removeElement(l++);
			while (r > qr) removeElement(r--);
			
			// 处理时间维度的修改操作
			while (currentTime < qt) {
				currentTime++;
				applyUpdate(currentTime);
			}
			while (currentTime > qt) {
				undoUpdate(currentTime--);
			}
			
			answers[id] = currentPairs;
		}
	}
	
	// 预处理函数
	public static void prepare() {
		// 计算分块大小
		blockSize = (int) Math.pow(n, 2.0 / 3.0);
		blockNum = (n + blockSize - 1) / blockSize;
		
		// 计算每个位置所属的块
		for (int i = 1; i <= n; i++) {
			belong[i] = (i - 1) / blockSize + 1;
		}
		
		// 建立值到位置的映射
		for (int i = 1; i <= n; i++) {
			pos[arr[i]] = i;
		}
		
		// 对查询进行排序
		Arrays.sort(queries, 1, queryTime + 1, new QueryComparator());
	}
	
	public static void main(String[] args) throws IOException {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		n = in.nextInt();
		m = in.nextInt();
		
		// 读取初始数组
		for (int i = 1; i <= n; i++) {
			arr[i] = in.nextInt();
		}
		
		// 处理修改操作，构造查询
		for (int i = 1; i <= m; i++) {
			int pos = in.nextInt();
			int val = in.nextInt();
			
			updateTime++;
			updates[updateTime][0] = updateTime;
			updates[updateTime][1] = pos;
			updates[updateTime][2] = val;
			updates[updateTime][3] = arr[pos];
			
			// 每次修改后都要查询逆序对数量
			queryTime++;
			queries[queryTime][0] = 1;      // 查询区间左端点
			queries[queryTime][1] = n;      // 查询区间右端点
			queries[queryTime][2] = updateTime;  // 时间戳
			queries[queryTime][3] = queryTime;   // 查询编号
		}
		
		prepare();
		compute();
		
		// 输出结果
		for (int i = 1; i <= queryTime; i++) {
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