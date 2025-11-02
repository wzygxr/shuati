package class114;

/**
 * HDU 5306 Gorgeous Sequence - 区间最值操作（吉司机线段树）
 * 
 * 题目描述：
 * 给定一个长度为n的数组arr，实现支持以下三种操作的结构
 * 操作 0 l r x : 把arr[l..r]范围的每个数v，更新成min(v, x)
 * 操作 1 l r   : 查询arr[l..r]范围上的最大值
 * 操作 2 l r   : 查询arr[l..r]范围上的累加和
 * 
 * 解题思路：
 * 使用吉司机线段树（Segment Tree Beats）处理区间最值操作。
 * 每个节点维护最大值、次大值、最大值个数和区间和，利用势能分析法保证时间复杂度。
 * 
 * 关键技术：
 * 1. 吉司机线段树：维护区间最大值、次大值和最大值个数
 * 2. 势能分析法：通过分析势能变化证明均摊时间复杂度
 * 3. 三种更新情况分类讨论：
 *    a. 更新值 >= 最大值：无需更新
 *    b. 次大值 < 更新值 < 最大值：直接更新最大值
 *    c. 更新值 <= 次大值：递归处理子区间
 * 
 * 时间复杂度分析：
 * 1. 建树：O(n)
 * 2. 区间最值操作：O(n log² n) 均摊
 * 3. 区间查询：O(log n)
 * 4. 空间复杂度：O(n)
 * 
 * 是否最优解：是
 * 吉司机线段树是处理区间最值操作问题的最优解法
 * 
 * 工程化考量：
 * 1. 对数器验证：通过随机数据验证实现正确性
 * 2. 边界处理：处理最大值、次大值相等的特殊情况
 * 3. 性能优化：分类讨论减少不必要的递归
 * 
 * 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=5306
 * 
 * @author Algorithm Journey
 * @version 1.0
 */
public class Code03_SegmentTreeSetminQueryMaxSum1 {

	public static int MAXN = 100001;

	// 假设初始数组中的值不会出现比LOWEST还小的值
	// 假设更新操作时jobv的数值也不会出现比LOWEST还小的值
	public static int LOWEST = -100001;

	// 原始数组
	public static int[] arr = new int[MAXN];

	// 累加和
	public static long[] sum = new long[MAXN << 2];

	// 最大值(既是查询信息也是懒更新信息，课上已经讲解了)
	public static int[] max = new int[MAXN << 2];

	// 最大值个数
	public static int[] cnt = new int[MAXN << 2];

	// 严格次大值(second max)
	public static int[] sem = new int[MAXN << 2];

	/**
	 * 向上更新节点信息
	 * 将左右子节点的信息合并到父节点
	 * 
	 * @param i 节点索引
	 */
	public static void up(int i) {
		int l = i << 1;
		int r = i << 1 | 1;
		sum[i] = sum[l] + sum[r];
		max[i] = Math.max(max[l], max[r]);
		if (max[l] > max[r]) {
			cnt[i] = cnt[l];
			sem[i] = Math.max(sem[l], max[r]);
		} else if (max[l] < max[r]) {
			cnt[i] = cnt[r];
			sem[i] = Math.max(max[l], sem[r]);
		} else {
			cnt[i] = cnt[l] + cnt[r];
			sem[i] = Math.max(sem[l], sem[r]);
		}
	}

	/**
	 * 向下传递懒惰标记
	 * 在访问子节点前，将当前节点的懒惰标记传递给子节点
	 * 
	 * @param i 节点索引
	 */
	public static void down(int i) {
		lazy(i << 1, max[i]);
		lazy(i << 1 | 1, max[i]);
	}

	/**
	 * 懒惰标记应用
	 * 将懒惰标记应用到指定节点
	 * 
	 * 一定是没有颠覆掉次大值的懒更新信息下发，也就是说：
	 * 最大值被压成v，并且v > 严格次大值的情况下
	 * sum和max怎么调整
	 * 
	 * @param i 节点索引
	 * @param v 懒惰标记值
	 */
	public static void lazy(int i, int v) {
		if (v < max[i]) {
			sum[i] -= ((long) max[i] - v) * cnt[i];
			max[i] = v;
		}
	}

	/**
	 * 建立线段树
	 * 
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @param i 节点索引
	 */
	public static void build(int l, int r, int i) {
		if (l == r) {
			sum[i] = max[i] = arr[l];
			cnt[i] = 1;
			sem[i] = LOWEST;
		} else {
			int mid = (l + r) >> 1;
			build(l, mid, i << 1);
			build(mid + 1, r, i << 1 | 1);
			up(i);
		}
	}

	/**
	 * 区间最值操作
	 * 将区间[jobl, jobr]内的每个数v更新成min(v, jobv)
	 * 
	 * @param jobl 操作区间左端点
	 * @param jobr 操作区间右端点
	 * @param jobv 更新值
	 * @param l    当前节点表示的区间左端点
	 * @param r    当前节点表示的区间右端点
	 * @param i    当前节点索引
	 */
	public static void setMin(int jobl, int jobr, int jobv, int l, int r, int i) {
		// 如果更新值大于等于当前区间最大值，无需更新
		if (jobv >= max[i]) {
			return;
		}
		// 如果操作区间完全包含当前区间且更新值大于次大值，直接更新
		if (jobl <= l && r <= jobr && sem[i] < jobv) {
			lazy(i, jobv);
		} else {
			// 1) 任务没有全包
			// 2) jobv <= sem[i]
			// 需要递归处理子区间
			down(i);
			int mid = (l + r) >> 1;
			if (jobl <= mid) {
				setMin(jobl, jobr, jobv, l, mid, i << 1);
			}
			if (jobr > mid) {
				setMin(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
			}
			up(i);
		}
	}

	/**
	 * 查询区间最大值
	 * 
	 * @param jobl 查询区间左端点
	 * @param jobr 查询区间右端点
	 * @param l    当前节点表示的区间左端点
	 * @param r    当前节点表示的区间右端点
	 * @param i    当前节点索引
	 * @return 区间最大值
	 */
	public static int queryMax(int jobl, int jobr, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			return max[i];
		}
		down(i);
		int mid = (l + r) >> 1;
		int ans = Integer.MIN_VALUE;
		if (jobl <= mid) {
			ans = Math.max(ans, queryMax(jobl, jobr, l, mid, i << 1));
		}
		if (jobr > mid) {
			ans = Math.max(ans, queryMax(jobl, jobr, mid + 1, r, i << 1 | 1));
		}
		return ans;
	}

	/**
	 * 查询区间和
	 * 
	 * @param jobl 查询区间左端点
	 * @param jobr 查询区间右端点
	 * @param l    当前节点表示的区间左端点
	 * @param r    当前节点表示的区间右端点
	 * @param i    当前节点索引
	 * @return 区间和
	 */
	public static long querySum(int jobl, int jobr, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			return sum[i];
		}
		down(i);
		int mid = (l + r) >> 1;
		long ans = 0;
		if (jobl <= mid) {
			ans += querySum(jobl, jobr, l, mid, i << 1);
		}
		if (jobr > mid) {
			ans += querySum(jobl, jobr, mid + 1, r, i << 1 | 1);
		}
		return ans;
	}

	/**
	 * 主方法
	 * 对数器验证实现正确性
	 * 
	 * @param args 命令行参数
	 */
	public static void main(String[] args) {
		System.out.println("测试开始");
		int n = 2000;
		int v = 5000;
		int t = 1000000;
		randomArray(n, v);
		int[] check = new int[n + 1];
		for (int i = 1; i <= n; i++) {
			check[i] = arr[i];
		}
		build(1, n, 1);
		for (int i = 1, op, a, b, jobl, jobr, jobv; i <= t; i++) {
			op = (int) (Math.random() * 3);
			a = (int) (Math.random() * n) + 1;
			b = (int) (Math.random() * n) + 1;
			jobl = Math.min(a, b);
			jobr = Math.max(a, b);
			if (op == 0) {
				jobv = (int) (Math.random() * v * 2) - v;
				setMin(jobl, jobr, jobv, 1, n, 1);
				checkSetMin(check, jobl, jobr, jobv);
			} else if (op == 1) {
				int ans1 = queryMax(jobl, jobr, 1, n, 1);
				int ans2 = checkQueryMax(check, jobl, jobr);
				if (ans1 != ans2) {
					System.out.println("出错了!");
				}
			} else {
				long ans1 = querySum(jobl, jobr, 1, n, 1);
				long ans2 = checkQuerySum(check, jobl, jobr);
				if (ans1 != ans2) {
					System.out.println("出错了!");
				}
			}
		}
		System.out.println("测试结束");
	}

	/**
	 * 随机生成数组
	 * 
	 * @param n 数组长度
	 * @param v 值范围
	 */
	public static void randomArray(int n, int v) {
		for (int i = 1; i <= n; i++) {
			arr[i] = (int) (Math.random() * v * 2) - v;
		}
	}

	/**
	 * 检查区间最值操作正确性
	 * 
	 * @param check 检查数组
	 * @param jobl  操作区间左端点
	 * @param jobr  操作区间右端点
	 * @param jobv  更新值
	 */
	public static void checkSetMin(int[] check, int jobl, int jobr, int jobv) {
		for (int i = jobl; i <= jobr; i++) {
			check[i] = Math.min(check[i], jobv);
		}
	}

	/**
	 * 检查区间最大值查询正确性
	 * 
	 * @param check 检查数组
	 * @param jobl  查询区间左端点
	 * @param jobr  查询区间右端点
	 * @return 区间最大值
	 */
	public static int checkQueryMax(int[] check, int jobl, int jobr) {
		int ans = Integer.MIN_VALUE;
		for (int i = jobl; i <= jobr; i++) {
			ans = Math.max(ans, check[i]);
		}
		return ans;
	}

	/**
	 * 检查区间和查询正确性
	 * 
	 * @param check 检查数组
	 * @param jobl  查询区间左端点
	 * @param jobr  查询区间右端点
	 * @return 区间和
	 */
	public static long checkQuerySum(int[] check, int jobl, int jobr) {
		long ans = 0;
		for (int i = jobl; i <= jobr; i++) {
			ans += check[i];
		}
		return ans;
	}

}