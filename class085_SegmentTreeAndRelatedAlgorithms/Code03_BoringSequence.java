package class112;

// 无聊的数列 - 线段树 + 差分数组实现
// 题目来源：洛谷 P1438 https://www.luogu.com.cn/problem/P1438
// 
// 题目描述：
// 给定一个长度为n的数组arr，实现如下两种操作
// 操作 1 l r k d : arr[l..r]范围上的数依次加上等差数列，首项k，公差d
// 操作 2 p       : 查询arr[p]的值
// 
// 解题思路：
// 使用差分数组+线段树维护区间更新和单点查询
// 1. 利用差分数组的性质：对原数组区间[l,r]加上等差数列，等价于对差分数组进行特定位置的修改
// 2. 使用线段树维护差分数组，支持区间加法和前缀和查询
// 
// 核心思想：
// 差分数组的性质：
// 1. 差分数组diff[i] = arr[i] - arr[i-1] (i>0), diff[0] = arr[0]
// 2. 原数组可以通过差分数组的前缀和恢复：arr[i] = sum(diff[0..i])
// 3. 对原数组区间[l,r]加上值v，等价于对差分数组进行以下操作：
//    - diff[l] += v
//    - diff[r+1] -= v (如果r+1在数组范围内)
// 
// 对于在arr[l..r]范围上加上首项为k、公差为d的等差数列：
// 等差数列为：k, k+d, k+2d, ..., k+(r-l)d
// - 在差分数组的l位置加上k
// - 在差分数组的[l+1,r]范围上加上d
// - 在差分数组的r+1位置减去k+d*(r-l)
// 
// 查询arr[p]的值即为差分数组的前缀和query(1,p)
// 
// 时间复杂度分析：
// - 建树：O(n)
// - 区间更新：O(log n)
// - 单点查询：O(log n)
// 空间复杂度：O(n)
//
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code03_BoringSequence {

	// 最大节点数，设置为100001*4以确保足够的空间
	public static int MAXN = 100001;

	// diff[i] 表示原数组的差分数组，diff[i] = arr[i] - arr[i-1]
	public static int[] diff = new int[MAXN];

	// sum[i] 表示线段树节点i维护的区间内差分值的和
	public static long[] sum = new long[MAXN << 2];

	// add[i] 表示线段树节点i的懒惰标记，记录区间需要加上的值
	public static long[] add = new long[MAXN << 2];

	/**
	 * 向上更新函数
	 * 更新当前节点的值为左右子节点值的和
	 * 在线段树中，父节点的值通常由子节点的值计算得出
	 * 对于本问题，父节点维护的区间差分值之和等于左右子节点维护区间差分值之和
	 * @param i 当前节点在线段树数组中的索引
	 */
	public static void up(int i) {
		sum[i] = sum[i << 1] + sum[i << 1 | 1];
	}

	/**
	 * 向下传递懒惰标记
	 * 将当前节点的懒惰标记传递给左右子节点
	 * 懒惰传播是线段树优化的重要技术，用于延迟更新操作
	 * 只有在真正需要访问子节点时才将更新操作传递下去，避免不必要的计算
	 * @param i 当前节点在线段树数组中的索引
	 * @param ln 左子树节点数量
	 * @param rn 右子树节点数量
	 */
	public static void down(int i, int ln, int rn) {
		if (add[i] != 0) {
			// 将加法标记传递给左右子节点
			// 左子树区间长度为ln，右子树区间长度为rn
			lazy(i << 1, add[i], ln);
			lazy(i << 1 | 1, add[i], rn);
			// 清除当前节点的加法标记
			// 标记已传递，当前节点的懒惰标记清零
			add[i] = 0;
		}
	}

	/**
	 * 懒惰标记处理函数
	 * 对节点i维护的区间加上值v
	 * 这是区间加法操作的核心实现
	 * 当对长度为n的区间加上值v时：
	 * 1. 更新懒惰标记：add[i] += v
	 * 2. 更新节点值：sum[i] += v * n (因为区间内每个元素都加v，总共加v*n)
	 * @param i 要更新的节点在线段树数组中的索引
	 * @param v 要加上的值
	 * @param n 该节点维护的区间长度
	 */
	public static void lazy(int i, long v, int n) {
		add[i] += v;           // 更新懒惰标记，记录区间需要加上的值
		sum[i] += v * n;       // 更新节点值，区间内每个元素都加v，总共加v*n
	}

	/**
	 * 构建线段树
	 * 采用递归方式构建线段树，每个节点维护一个区间的信息
	 * 叶子节点对应数组中的单个元素，非叶子节点对应区间的合并结果
	 * @param l 区间左边界
	 * @param r 区间右边界
	 * @param i 当前节点在线段树数组中的索引
	 */
	public static void build(int l, int r, int i) {
		if (l == r) {
			// 叶子节点，初始化为差分数组的值
			// 叶子节点对应差分数组中的一个具体元素
			sum[i] = diff[l];
		} else {
			int mid = (l + r) >> 1;
			// 递归构建左子树
			build(l, mid, i << 1);
			// 递归构建右子树
			build(mid + 1, r, i << 1 | 1);
			// 向上更新当前节点的值
			// 将左右子节点的值合并到当前节点
			up(i);
		}
		// 初始化懒惰标记为0
		// 初始时没有任何区间更新操作，懒惰标记为0
		add[i] = 0;
	}

	/**
	 * 区间加法操作
	 * 对区间[jobl, jobr]内的所有元素加上值jobv
	 * 利用懒惰传播优化，避免对每个元素逐一加法
	 * @param jobl 操作区间左边界
	 * @param jobr 操作区间右边界
	 * @param jobv 要加上的值
	 * @param l 当前节点维护的区间左边界
	 * @param r 当前节点维护的区间右边界
	 * @param i 当前节点在线段树数组中的索引
	 */
	public static void add(int jobl, int jobr, long jobv, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			// 当前节点维护的区间完全包含在操作区间内，直接打懒惰标记
			// 这是懒惰传播的关键：只标记不立即执行
			// 区间长度为r-l+1，要加上的值为jobv
			lazy(i, jobv, r - l + 1);
		} else {
			int mid = (l + r) >> 1;
			// 向下传递懒惰标记
			// 在递归处理子节点之前，需要确保当前节点的懒惰标记已经传递
			down(i, mid - l + 1, r - mid);
			// 递归处理左子树
			// 只有当操作区间与左子树区间有交集时才继续处理
			if (jobl <= mid) {
				add(jobl, jobr, jobv, l, mid, i << 1);
			}
			// 递归处理右子树
			// 只有当操作区间与右子树区间有交集时才继续处理
			if (jobr > mid) {
				add(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
			}
			// 向上更新当前节点的值
			// 将子节点的更新结果合并到当前节点
			up(i);
		}
	}

	/**
	 * 区间查询操作
	 * 查询区间[jobl, jobr]内元素的和
	 * 在查询过程中需要确保懒惰标记已经正确传递
	 * @param jobl 查询区间左边界
	 * @param jobr 查询区间右边界
	 * @param l 当前节点维护的区间左边界
	 * @param r 当前节点维护的区间右边界
	 * @param i 当前节点在线段树数组中的索引
	 * @return 区间内元素的和
	 */
	public static long query(int jobl, int jobr, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			// 当前节点维护的区间完全包含在查询区间内，直接返回节点值
			// 这是线段树查询的优化点：如果当前区间完全在查询区间内，直接返回结果
			return sum[i];
		}
		int mid = (l + r) >> 1;
		// 向下传递懒惰标记
		// 在查询时必须确保懒惰标记已经传递，以保证结果正确
		down(i, mid - l + 1, r - mid);
		long ans = 0;
		// 递归查询左子树
		// 只有当查询区间与左子树区间有交集时才继续查询
		if (jobl <= mid) {
			ans += query(jobl, jobr, l, mid, i << 1);
		}
		// 递归查询右子树
		// 只有当查询区间与右子树区间有交集时才继续查询
		if (jobr > mid) {
			ans += query(jobl, jobr, mid + 1, r, i << 1 | 1);
		}
		return ans;
	}

	public static void main(String[] args) throws IOException {
		// 使用高效的IO处理方式
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取输入参数
		in.nextToken();
		int n = (int) in.nval;  // 数组长度
		in.nextToken();
		int m = (int) in.nval;  // 操作数量
		
		// 构造差分数组
		// 差分数组diff[i] = arr[i] - arr[i-1] (i>0), diff[0] = arr[0]
		// 通过差分数组可以快速进行区间更新操作
		for (int i = 1, pre = 0, cur; i <= n; i++) {
			in.nextToken();
			cur = (int) in.nval;
			diff[i] = cur - pre;  // 计算差分值
			pre = cur;
		}
		
		// 构建线段树
		// 用差分数组的值初始化线段树，支持区间加法和前缀和查询
		build(1, n, 1);
		
		// 处理m个操作
		for (int i = 1, op; i <= m; i++) {
			in.nextToken();
			op = (int) in.nval;  // 操作类型：1表示区间加等差数列，2表示单点查询
			
			if (op == 1) {
				// 区间加等差数列操作
				// 在arr[jobl..jobr]范围上加上首项为k、公差为d的等差数列
				in.nextToken();
				int jobl = (int) in.nval;   // 操作区间左边界
				in.nextToken();
				int jobr = (int) in.nval;   // 操作区间右边界
				in.nextToken();
				long k = (long) in.nval;    // 等差数列首项
				in.nextToken();
				long d = (long) in.nval;    // 等差数列公差
				
				// 计算等差数列末项值
				// 等差数列为：k, k+d, k+2d, ..., k+(jobr-jobl)d
				long e = k + d * (jobr - jobl);
				
				// 根据差分数组的性质，对原数组区间加上等差数列等价于对差分数组进行以下操作：
				// 1. 在差分数组的jobl位置加上k
				add(jobl, jobl, k, 1, n, 1);
				// 2. 在差分数组的[jobl+1,jobr]范围上加上d
				if (jobl + 1 <= jobr) {
					add(jobl + 1, jobr, d, 1, n, 1);
				}
				// 3. 在差分数组的jobr+1位置减去末项值
				if (jobr < n) {
					add(jobr + 1, jobr + 1, -e, 1, n, 1);
				}
			} else {
				// 单点查询操作：查询arr[p]的值即为差分数组的前缀和
				// 根据差分数组的性质：arr[i] = sum(diff[0..i])
				in.nextToken();
				int p = (int) in.nval;  // 查询位置
				// 查询差分数组[1,p]范围的和，即为arr[p]的值
				out.println(query(1, p, 1, n, 1));
			}
		}
		
		// 刷新输出缓冲区并关闭资源
		out.flush();
		out.close();
		br.close();
	}

}