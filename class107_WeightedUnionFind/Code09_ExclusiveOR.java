package class156;

// 异或关系
// 一共n个数，编号0 ~ n-1，实现如下三种类型的操作，一共调用m次
// I x v        : 声明 第x个数 = v
// I x y v      : 声明 第x个数 ^ 第y个数 = v
// Q k a1 .. ak : 查询 一共k个数，编号为a1 .. ak，这些数字异或起来的值是多少
// 对每个Q的操作打印答案，如果根据之前的声明无法推出答案，打印"I don't know."
// 如果处理到第s条声明，发现了矛盾，打印"The first s facts are conflicting."
// 注意只有声明操作出现，s才会增加，查询操作不占用声明操作的计数
// 发现矛盾之后，所有的操作都不再处理，更多的细节可以打开测试链接查看题目
// 1 <= n <= 20000    1 <= m <= 40000    1 <= k <= 15
// 测试链接 : https://acm.hdu.edu.cn/showproblem.php?pid=3234
// 测试链接 : https://www.luogu.com.cn/problem/UVA12232
// 测试链接 : https://vjudge.net/problem/UVA-12232
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * 带权并查集解决异或关系问题
 * 
 * 问题分析：
 * 维护变量之间的异或关系，支持声明和查询操作，并检测矛盾
 * 
 * 核心思想：
 * 1. 将异或关系转化为带权并查集
 * 2. 对于每个变量i，维护其到根节点的异或值dist[i]
 * 3. 如果i ^ j = v，则i和j在同一集合中，且dist[i] ^ dist[j] = v
 * 4. 为了处理单个变量的赋值，引入一个虚拟根节点n
 * 
 * 时间复杂度分析：
 * - prepare: O(n)
 * - find: O(α(n)) 近似O(1)
 * - opi: O(α(n)) 近似O(1)
 * - opq: O(k * α(n) + k * log(k))
 * - 总体: O(n + m * α(n) + Σk * log(k))
 * 
 * 空间复杂度: O(n + k)
 * 
 * 应用场景：
 * - 异或关系维护
 * - 逻辑一致性验证
 * - 位运算问题
 */
public class Code09_ExclusiveOR {

	public static int MAXN = 20002;

	public static int MAXK = 21;

	public static int t, n, m;

	// 是否发现矛盾
	public static boolean conflict;

	// 声明操作计数器
	public static int cnti;

	// father[i] 表示节点i的父节点
	public static int[] father = new int[MAXN];

	// dist[i] 表示节点i到根节点的异或值
	public static int[] exclu = new int[MAXN];

	// 查询用的临时数组
	public static int[] nums = new int[MAXK];

	// 查询用的临时数组
	public static int[] fas = new int[MAXK];

	/**
	 * 初始化并查集
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n)
	 */
	public static void prepare() {
		// 重置状态
		conflict = false;
		cnti = 0;
		// 初始化每个节点为自己所在集合的代表
		for (int i = 0; i <= n; i++) {
			father[i] = i;
			// 初始时每个节点到根节点的异或值为0
			exclu[i] = 0;
		}
	}

	/**
	 * 查找节点i所在集合的代表，并进行路径压缩
	 * 同时更新exclu[i]为节点i到根节点的异或值
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param i 要查找的节点
	 * @return 节点i所在集合的根节点
	 */
	public static int find(int i) {
		// 如果不是根节点
		if (i != father[i]) {
			// 保存父节点
			int tmp = father[i];
			// 递归查找根节点，同时进行路径压缩
			father[i] = find(tmp);
			// 更新异或值：当前节点到根节点的异或值 = 当前节点到父节点的异或值 ^ 父节点到根节点的异或值
			exclu[i] ^= exclu[tmp];
		}
		return father[i];
	}

	/**
	 * 处理声明操作
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param l 左侧变量编号
	 * @param r 右侧变量编号
	 * @param v 异或值
	 * @return 如果操作成功返回true，如果发现矛盾返回false
	 */
	public static boolean opi(int l, int r, int v) {
		// 声明操作计数器加1
		cnti++;
		// 查找两个变量的根节点
		int lf = find(l), rf = find(r);
		// 如果在同一集合中
		if (lf == rf) {
			// 检查是否与已有关系矛盾
			// l ^ r = (l ^ root) ^ (r ^ root) = exclu[l] ^ exclu[r]
			if ((exclu[l] ^ exclu[r]) != v) {
				// 发现矛盾
				conflict = true;
				return false;
			}
		} else {
			// 如果l所在集合的根节点是虚拟根节点
			if (lf == n) {
				// 交换，确保l所在集合不是虚拟根节点
				lf = rf;
				rf = n;
			}
			// 合并两个集合
			father[lf] = rf;
			// 更新异或关系：
			// l ^ r = v
			// l ^ root_l = exclu[l], r ^ root_r = exclu[r]
			// root_l ^ root_r = exclu[l] ^ exclu[r] ^ v
			// 因此 exclu[lf] = exclu[r] ^ exclu[l] ^ v
			exclu[lf] = exclu[r] ^ exclu[l] ^ v;
		}
		return true;
	}

	/**
	 * 处理查询操作
	 * 时间复杂度: O(k * α(n) + k * log(k))
	 * 
	 * @param k     查询变量数量
	 * @return 查询结果，如果无法确定返回-1
	 */
	public static int opq(int k) {
		int ans = 0;
		// 处理所有查询变量
		for (int i = 1, fa; i <= k; i++) {
			// 查找根节点
			fa = find(nums[i]);
			// 累计异或值
			ans ^= exclu[nums[i]];
			// 记录根节点
			fas[i] = fa;
		}
		// 排序根节点，便于检查是否所有变量在同一集合中
		Arrays.sort(fas, 1, k + 1);
		// 检查连通性
		for (int l = 1, r = 1; l <= k; l = ++r) {
			// 找到相同根节点的连续段
			while (r + 1 <= k && fas[r + 1] == fas[l]) {
				r++;
			}
			// 如果这一段的长度是奇数且根节点不是虚拟根节点
			if ((r - l + 1) % 2 != 0 && fas[l] != n) {
				// 无法确定结果
				return -1;
			}
		}
		return ans;
	}

	public static void main(String[] args) throws IOException {
		FastReader in = new FastReader();
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		t = 0;
		n = in.nextInt();
		m = in.nextInt();
		// 处理多个测试用例
		while (n != 0 || m != 0) {
			prepare();
			out.println("Case " + (++t) + ":");
			// 处理m个操作
			for (int i = 1; i <= m; i++) {
				String op = in.next();
				if (op.equals("I")) {
					int l, r, v;
					in.numbers();
					// 如果尚未发现矛盾
					if (!conflict) {
						// 根据参数数量处理不同类型的声明
						if (in.size == 2) {
							// 单变量赋值：I x v
							l = in.a;
							r = n; // 使用虚拟根节点
							v = in.b;
						} else {
							// 双变量异或：I x y v
							l = in.a;
							r = in.b;
							v = in.c;
						}
						// 处理声明
						if (!opi(l, r, v)) {
							// 发现矛盾，输出提示信息
							out.println("The first " + cnti + " facts are conflicting.");
						}
					}
				} else {
					// 查询操作：Q k a1 .. ak
					int k = in.nextInt();
					for (int j = 1; j <= k; j++) {
						nums[j] = in.nextInt();
					}
					// 如果尚未发现矛盾
					if (!conflict) {
						// 处理查询
						int ans = opq(k);
						if (ans == -1) {
							// 无法确定结果
							out.println("I don't know.");
						} else {
							// 输出查询结果
							out.println(ans);
						}
					}
				}
			}
			out.println();
			// 读取下一组测试用例
			n = in.nextInt();
			m = in.nextInt();
		}
		out.flush();
		out.close();
		in.close();
	}

	// 读写工具类
	static class FastReader {
		final private int BUFFER_SIZE = 1 << 16;
		private final InputStream in;
		private final byte[] buffer;
		private int pointer, bytesRead;

		public int a;
		public int b;
		public int c;
		public int size;

		public FastReader() {
			in = System.in;
			buffer = new byte[BUFFER_SIZE];
			pointer = bytesRead = 0;
		}

		private byte read() throws IOException {
			if (pointer >= bytesRead) {
				fillBuffer();
				if (bytesRead == -1) {
					return -1;
				}
			}
			return buffer[pointer++];
		}

		private void fillBuffer() throws IOException {
			bytesRead = in.read(buffer, 0, BUFFER_SIZE);
			pointer = 0;
		}

		private void skipWhiteSpace() throws IOException {
			byte c;
			while ((c = read()) != -1) {
				if (c > ' ') {
					pointer--;
					break;
				}
			}
		}

		private String readLine() throws IOException {
			StringBuilder sb = new StringBuilder();
			while (true) {
				byte c = read();
				if (c == -1 || c == '\n') {
					break;
				}
				if (c == '\r') {
					byte nextc = read();
					if (nextc != '\n') {
						pointer--;
					}
					break;
				}
				sb.append((char) c);
			}
			if (sb.length() == 0 && bytesRead == -1) {
				return null;
			}
			return sb.toString();
		}

		public String next() throws IOException {
			skipWhiteSpace();
			if (bytesRead == -1) {
				return null;
			}
			StringBuilder sb = new StringBuilder();
			byte c = read();
			while (c != -1 && c > ' ') {
				sb.append((char) c);
				c = read();
			}
			return sb.toString();
		}

		public int nextInt() throws IOException {
			skipWhiteSpace();
			if (bytesRead == -1) {
				throw new IOException("No more data to read (EOF)");
			}
			boolean negative = false;
			int result = 0;
			byte c = read();
			if (c == '-') {
				negative = true;
				c = read();
			}
			while (c >= '0' && c <= '9') {
				result = result * 10 + (c - '0');
				c = read();
			}
			if (c != -1 && c > ' ') {
				pointer--;
			}
			return negative ? -result : result;
		}

		public void numbers() throws IOException {
			a = b = c = size = 0;
			String line = readLine();
			if (line == null) {
				return;
			}
			String[] parts = line.trim().split("\\s+");
			if (parts.length == 0) {
				return;
			}
			size = Math.min(parts.length, 3);
			if (size >= 1) {
				a = Integer.parseInt(parts[0]);
			}
			if (size >= 2) {
				b = Integer.parseInt(parts[1]);
			}
			if (size >= 3) {
				c = Integer.parseInt(parts[2]);
			}
		}

		public void close() throws IOException {
			if (in != null) {
				in.close();
			}
		}
	}

}