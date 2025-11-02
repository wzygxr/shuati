package class091;

// 组团买票找到了在线测试
// 逻辑和课上讲的一样，但是测试中设定的ki为负数
// 实现做了一些小优化，具体可以看注释
// 测试链接 : https://www.luogu.com.cn/problem/P12331
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.util.PriorityQueue;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code03_GroupBuyTickets2 {

	public static class Game {
		public long ki;
		public long bi;
		public int people;

		public Game(long k, long b) {
			ki = k;
			bi = b;
		}

		public long earn() {
			return cost(people + 1) - cost(people);
		}

		public long cost(long p) {
			long price = ki * p + bi;
			if (price < 0) {
				price = 0;
			}
			return p * price;
		}
	}

	/**
	 * 计算组团买票的最少花费（优化版）
	 * 
	 * 算法思路：
	 * 使用优先队列的贪心策略：
	 * 1. 将所有项目加入优先队列，按收益排序（收益最大的在堆顶）
	 * 2. 每次将一个人分配给当前收益最大的项目
	 * 3. 更新该项目的收益并重新加入队列
	 * 4. 重复直到所有人都被分配或没有正收益项目
	 * 
	 * 优化点：
	 * 1. 使用long类型避免整数溢出
	 * 2. 提前过滤收益<=0的项目
	 * 3. 当项目收益<=0时提前结束
	 * 
	 * 时间复杂度：O(n * logm) - n个人，m个项目，每次操作需要logm时间
	 * 空间复杂度：O(m) - 优先队列存储m个项目
	 * 
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		int n = in.nextInt();
		int m = in.nextInt();
		PriorityQueue<Game> heap = new PriorityQueue<>((a, b) -> Long.compare(b.earn(), a.earn()));
		for (int i = 0; i < m; i++) {
			Game cur = new Game(in.nextLong(), in.nextLong());
			// 初始增费<=0的项目直接忽略
			if (cur.earn() > 0) {
				heap.add(cur);
			}
		}
		long ans = 0;
		for (int i = 0; i < n && !heap.isEmpty(); i++) {
			Game cur = heap.poll();
			long money = cur.earn();
			if (money <= 0) {
				// 没有正向增费，那么可以结束了
				break;
			}
			ans += money;
			cur.people++;
			if (cur.earn() > 0) {
				heap.add(cur);
			}
		}
		out.println(ans);
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

		long nextLong() throws IOException {
			int c;
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			boolean neg = false;
			if (c == '-') {
				neg = true;
				c = readByte();
			}
			long val = 0L;
			while (c > ' ' && c != -1) {
				val = val * 10 + (c - '0');
				c = readByte();
			}
			return neg ? -val : val;
		}

	}
}