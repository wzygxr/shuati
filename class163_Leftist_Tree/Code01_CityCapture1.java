package class155;

import java.io.IOException;
import java.io.PrintWriter;

// 城池攻占，java版
// 一共有n个城市，1号城市是城市树的头，每个城市都有防御值、上级城市编号、奖励类型、奖励值
// 如果奖励类型为0，任何骑士攻克这个城市后，攻击力会加上奖励值
// 如果奖励类型为1，任何骑士攻克这个城市后，攻击力会乘以奖励值
// 任何城市的上级编号 < 这座城市的编号，1号城市没有上级城市编号、奖励类型、奖励值
// 一共有m个骑士，每个骑士都有攻击力、第一次攻击的城市
// 如果骑士攻击力 >= 城市防御值，当前城市会被攻占，骑士获得奖励，继续攻击上级城市
// 如果骑士攻击力  < 城市防御值，那么骑士会在该城市牺牲，没有后续动作了
// 所有骑士都是独立的，不会影响其他骑士攻击这座城池的结果
// 打印每个城市牺牲的骑士数量，打印每个骑士攻占的城市数量
// 1 <= n、m <= 3 * 10^5，攻击值的增加也不会超过long类型范围
// 测试链接 : https://www.luogu.com.cn/problem/P3261
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

/**
 * JLOI2015 城池攻占 - 左偏树解法
 * 
 * 题目链接: https://www.luogu.com.cn/problem/P3261
 * 
 * 题目描述：
 * 小铭铭最近获得了一副新的桌游，游戏中需要用m个骑士攻占n个城池。这n个城池用1到n的整数表示，
 * 除1号城池外，城池i会受到另一座城池fi的管辖，其中fi<i。也就是说，所有城池构成了一棵有根树，1号城池为根。
 * 游戏开始前，所有城池都会有一个防御值hi。如果一个骑士的初始战斗力si大于等于城池的防御值，那么该骑士就能占领该城池。
 * 骑士的战斗力会因为占领城池而改变，每个城池i有两种属性：
 * 1. ai=0时，战斗力会加上vi
 * 2. ai=1时，战斗力会乘以vi
 * 骑士们按照1到m的顺序依次攻占城池。每个骑士会按照如下方法攻占城池：
 * 1. 选择一个城池i作为起点
 * 2. 如果当前战斗力大于等于城池防御值，则占领该城池并按规则改变战斗力
 * 3. 然后前往管辖该城池的城池fi，重复步骤2
 * 4. 直到无法占领某个城池或到达根节点为止
 * 你需要计算：
 * 1. 每个城池各有多少个骑士牺牲（无法占领该城池）
 * 2. 每个骑士各攻占了多少个城池
 * 
 * 解题思路：
 * 这是一道经典的树形结构+左偏树优化的题目：
 * 1. 建立城池的树形结构，以1号城池为根
 * 2. 对于每个城池，维护一个左偏树，存储当前在该城池的骑士
 * 3. 左偏树需要支持延迟标记，用于处理战斗力的加法和乘法操作
 * 4. 按照骑士编号顺序处理每个骑士：
 *    - 将骑士放入起始城池的左偏树中
 *    - 从起始城池开始向上爬树，直到无法占领某个城池
 *    - 在每个城池中，如果骑士战斗力大于等于防御值，则占领并更新战斗力
 *    - 否则骑士牺牲，统计牺牲人数
 * 5. 为了优化效率，使用延迟标记和标记下传技术
 * 
 * 时间复杂度分析：
 * - 树形遍历: O(N)
 * - 左偏树操作: O(M log M)
 * - 延迟标记处理: O(N log M)
 * - 总体复杂度: O((N+M) log M)
 * 
 * 空间复杂度分析:
 * - 树形结构存储: O(N)
 * - 左偏树节点存储: O(M)
 * - 延迟标记存储: O(N)
 * - 总体空间复杂度: O(N+M)
 */
public class Code01_CityCapture1 {

	public static int MAXN = 300001;

	public static int n, m;

	// 城市防御值
	public static long[] defend = new long[MAXN];

	// 上级城市编号
	public static int[] belong = new int[MAXN];

	// 奖励类型
	public static int[] type = new int[MAXN];

	// 奖励值
	public static long[] gain = new long[MAXN];

	// 骑士攻击力
	public static long[] attack = new long[MAXN];

	// 骑士第一次攻击的城市
	public static int[] first = new int[MAXN];

	// 城市在城市树中的深度
	public static int[] deep = new int[MAXN];

	// 城市拥有的骑士列表，用小根堆左偏树组织，最弱的骑士是头
	public static int[] top = new int[MAXN];

	// 每个城市牺牲人数统计
	public static int[] sacrifice = new int[MAXN];

	// 每个骑士死在了什么城市
	public static int[] die = new int[MAXN];

	// 左偏树需要
	public static int[] left = new int[MAXN];

	public static int[] right = new int[MAXN];

	public static int[] dist = new int[MAXN];

	// 懒更新信息，攻击力应该乘多少
	public static long[] mul = new long[MAXN];

	// 懒更新信息，攻击力应该加多少
	public static long[] add = new long[MAXN];

	public static void prepare() {
		dist[0] = -1;
		for (int i = 1; i <= m; i++) {
			left[i] = right[i] = dist[i] = 0;
			mul[i] = 1;
			add[i] = 0;
		}
		for (int i = 1; i <= n; i++) {
			sacrifice[i] = top[i] = 0;
		}
	}

	public static void upgrade(int i, int t, long v) {
		if (t == 0) {
			attack[i] += v;
			add[i] += v;
		} else {
			attack[i] *= v;
			mul[i] *= v;
			add[i] *= v;
		}
	}

	public static void down(int i) {
		if (mul[i] != 1 || add[i] != 0) {
			int l = left[i];
			int r = right[i];
			if (l != 0) {
				attack[l] = attack[l] * mul[i] + add[i];
				mul[l] = mul[l] * mul[i];
				add[l] = add[l] * mul[i] + add[i];
			}
			if (r != 0) {
				attack[r] = attack[r] * mul[i] + add[i];
				mul[r] = mul[r] * mul[i];
				add[r] = add[r] * mul[i] + add[i];
			}
			mul[i] = 1;
			add[i] = 0;
		}
	}

	public static int merge(int i, int j) {
		if (i == 0 || j == 0) {
			return i + j;
		}
		int tmp;
		if (attack[i] > attack[j]) {
			tmp = i;
			i = j;
			j = tmp;
		}
		down(i);
		right[i] = merge(right[i], j);
		if (dist[left[i]] < dist[right[i]]) {
			tmp = left[i];
			left[i] = right[i];
			right[i] = tmp;
		}
		dist[i] = dist[right[i]] + 1;
		return i;
	}

	public static int pop(int i) {
		down(i);
		int ans = merge(left[i], right[i]);
		left[i] = right[i] = dist[i] = 0;
		return ans;
	}

	public static void compute() {
		deep[1] = 1;
		for (int i = 2; i <= n; i++) {
			deep[i] = deep[belong[i]] + 1;
		}
		for (int i = 1; i <= m; i++) {
			if (top[first[i]] == 0) {
				top[first[i]] = i;
			} else {
				top[first[i]] = merge(top[first[i]], i);
			}
		}
		for (int i = n; i >= 1; i--) {
			while (top[i] != 0 && attack[top[i]] < defend[i]) {
				die[top[i]] = i;
				sacrifice[i]++;
				top[i] = pop(top[i]);
			}
			if (top[i] != 0) {
				upgrade(top[i], type[i], gain[i]);
				if (top[belong[i]] == 0) {
					top[belong[i]] = top[i];
				} else {
					top[belong[i]] = merge(top[belong[i]], top[i]);
				}
			}
		}
	}

	public static void main(String[] args) {
		ReaderWriter io = new ReaderWriter();
		n = io.nextInt();
		m = io.nextInt();
		prepare();
		for (int i = 1; i <= n; i++) {
			defend[i] = io.nextLong();
		}
		for (int i = 2; i <= n; i++) {
			belong[i] = io.nextInt();
			type[i] = io.nextInt();
			gain[i] = io.nextLong();
		}
		for (int i = 1; i <= m; i++) {
			attack[i] = io.nextLong();
			first[i] = io.nextInt();
		}
		compute();
		for (int i = 1; i <= n; i++) {
			io.println(sacrifice[i]);
		}
		for (int i = 1; i <= m; i++) {
			io.println(deep[first[i]] - deep[die[i]]);
		}
		io.flush();
		io.close();
	}

	// 读写工具类
	public static class ReaderWriter extends PrintWriter {
		byte[] buf = new byte[1 << 16];
		int bId = 0, bSize = 0;
		boolean eof = false;

		public ReaderWriter() {
			super(System.out);
		}

		private byte getByte() {
			if (bId >= bSize) {
				try {
					bSize = System.in.read(buf);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (bSize == -1)
					eof = true;
				bId = 0;
			}
			return buf[bId++];
		}

		byte c;

		public boolean hasNext() {
			if (eof)
				return false;
			while ((c = getByte()) <= 32 && !eof)
				;
			if (eof)
				return false;
			bId--;
			return true;
		}

		public String next() {
			if (!hasNext())
				return null;
			byte c = getByte();
			while (c <= 32)
				c = getByte();
			StringBuilder sb = new StringBuilder();
			while (c > 32) {
				sb.append((char) c);
				c = getByte();
			}
			return sb.toString();
		}

		public int nextInt() {
			if (!hasNext())
				return Integer.MIN_VALUE;
			int sign = 1;
			byte c = getByte();
			while (c <= 32)
				c = getByte();
			if (c == '-') {
				sign = -1;
				c = getByte();
			}
			int val = 0;
			while (c >= '0' && c <= '9') {
				val = val * 10 + (c - '0');
				c = getByte();
			}
			bId--;
			return val * sign;
		}

		public long nextLong() {
			if (!hasNext())
				return Long.MIN_VALUE;
			int sign = 1;
			byte c = getByte();
			while (c <= 32)
				c = getByte();
			if (c == '-') {
				sign = -1;
				c = getByte();
			}
			long val = 0;
			while (c >= '0' && c <= '9') {
				val = val * 10 + (c - '0');
				c = getByte();
			}
			bId--;
			return val * sign;
		}

		public double nextDouble() {
			if (!hasNext())
				return Double.NaN;
			int sign = 1;
			byte c = getByte();
			while (c <= 32)
				c = getByte();
			if (c == '-') {
				sign = -1;
				c = getByte();
			}
			double val = 0;
			while (c >= '0' && c <= '9') {
				val = val * 10 + (c - '0');
				c = getByte();
			}
			if (c == '.') {
				double mul = 1;
				c = getByte();
				while (c >= '0' && c <= '9') {
					mul *= 0.1;
					val += (c - '0') * mul;
					c = getByte();
				}
			}
			bId--;
			return val * sign;
		}
	}

}