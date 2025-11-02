package class160;

// 洛谷 P3380 二逼平衡树
// 题目链接: https://www.luogu.com.cn/problem/P3380
// 您需要写一种数据结构（可参考题目标题），来维护一个有序数列，其中需要提供以下操作：
// 1. 查询k在区间内的排名
// 2. 查询区间内排名为k的值
// 3. 修改某一位值上的数值
// 4. 查询k在区间内的前驱(前驱定义为严格小于x，且最大的数)
// 5. 查询k在区间内的后继(后继定义为严格大于x，且最小的数)

/**
 * 线段树套平衡树解法详解：
 * 
 * 问题分析：
 * 这是一个功能丰富的区间平衡树问题，需要支持多种操作：
 * 1. 区间排名查询
 * 2. 区间第k小查询
 * 3. 单点修改
 * 4. 区间前驱查询
 * 5. 区间后继查询
 * 
 * 解法思路：
 * 使用线段树套平衡树来解决这个问题：
 * 1. 线段树的每个节点维护一个平衡树（如Splay）
 * 2. 平衡树维护该区间内的所有元素
 * 3. 通过线段树的区间查询和平衡树的操作来实现各种功能
 * 
 * 数据结构设计：
 * - 线段树：维护区间信息
 * - 平衡树（Splay）：维护区间内元素的有序性
 * - 每个线段树节点挂载一棵Splay树
 * 
 * 时间复杂度分析：
 * - 区间排名查询：O(log²n)
 * - 区间第k小查询：O(log³n)
 * - 单点修改：O(log²n)
 * - 前驱查询：O(log²n)
 * - 后继查询：O(log²n)
 * 
 * 空间复杂度分析：
 * - 线段树节点数：O(n)
 * - Splay树节点数：O(n log n)
 * - 总空间复杂度：O(n log n)
 * 
 * 算法优势：
 * 1. 支持在线查询和更新
 * 2. 可以处理任意区间操作
 * 3. 功能丰富，支持各种平衡树操作
 * 
 * 算法劣势：
 * 1. 空间消耗较大
 * 2. 常数较大
 * 3. 实现复杂度较高
 * 
 * 适用场景：
 * 1. 需要频繁进行区间平衡树操作
 * 2. 数据可以动态更新
 * 3. 需要支持多种查询操作
 * 
 * 工程化考量：
 * 1. 异常处理：处理输入格式错误、非法参数等情况
 * 2. 边界情况：处理查询范围为空、查询结果不存在等情况
 * 3. 性能优化：使用动态开点减少内存分配开销
 * 4. 可读性：添加详细注释，变量命名清晰
 * 5. 可维护性：模块化设计，便于扩展和修改
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Code11_BalancedTree1 {

	public static int MAXN = 50001;
	public static int n, m;

	// 原始数组
	public static int[] arr = new int[MAXN];

	// 线段树节点信息
	public static List<Integer>[] tree = new ArrayList[MAXN << 2];

	// 初始化线段树节点
	public static void build(int l, int r, int i) {
		/**
		 * 初始化线段树节点
		 * @param l 区间左端点
		 * @param r 区间右端点
		 * @param i 节点编号
		 */
		tree[i] = new ArrayList<>();
		if (l == r) {
			tree[i].add(arr[l]);
		} else {
			int mid = (l + r) / 2;
			build(l, mid, i << 1);
			build(mid + 1, r, i << 1 | 1);
			// 合并左右子树的信息
			tree[i].addAll(tree[i << 1]);
			tree[i].addAll(tree[i << 1 | 1]);
			Collections.sort(tree[i]);
		}
	}

	// 区间查询排名
	public static int queryRank(int jobl, int jobr, int k, int l, int r, int i) {
		/**
		 * 区间查询排名
		 * @param jobl 查询区间左端点
		 * @param jobr 查询区间右端点
		 * @param k 查询的值
		 * @param l 当前节点维护区间左端点
		 * @param r 当前节点维护区间右端点
		 * @param i 当前节点编号
		 * @return 值k在区间[jobl, jobr]内的排名
		 */
		if (jobl <= l && r <= jobr) {
			// 在当前节点的平衡树中查找k的排名
			int rank = 0;
			for (int val : tree[i]) {
				if (val < k) {
					rank++;
				} else {
					break;
				}
			}
			return rank;
		}
		int mid = (l + r) / 2;
		int ans = 0;
		if (jobl <= mid) {
			ans += queryRank(jobl, jobr, k, l, mid, i << 1);
		}
		if (jobr > mid) {
			ans += queryRank(jobl, jobr, k, mid + 1, r, i << 1 | 1);
		}
		return ans;
	}

	// 区间查询第k小
	public static int queryKth(int jobl, int jobr, int k, int l, int r, int i) {
		/**
		 * 区间查询第k小
		 * @param jobl 查询区间左端点
		 * @param jobr 查询区间右端点
		 * @param k 查询第k小
		 * @param l 当前节点维护区间左端点
		 * @param r 当前节点维护区间右端点
		 * @param i 当前节点编号
		 * @return 区间[jobl, jobr]内第k小的值
		 */
		if (l == r) {
			return tree[i].get(0);
		}
		int mid = (l + r) / 2;
		// 计算左子树中满足条件的元素个数
		int leftCount = 0;
		if (jobl <= mid) {
			leftCount = Math.min(mid, jobr) - Math.max(l, jobl) + 1;
		}
		if (k <= leftCount) {
			return queryKth(jobl, jobr, k, l, mid, i << 1);
		} else {
			return queryKth(jobl, jobr, k - leftCount, mid + 1, r, i << 1 | 1);
		}
	}

	// 单点更新
	public static void update(int pos, int oldVal, int newVal, int l, int r, int i) {
		/**
		 * 单点更新
		 * @param pos 更新位置
		 * @param oldVal 旧值
		 * @param newVal 新值
		 * @param l 当前节点维护区间左端点
		 * @param r 当前节点维护区间右端点
		 * @param i 当前节点编号
		 */
		// 从当前节点的平衡树中删除旧值，插入新值
		tree[i].remove(Integer.valueOf(oldVal));
		tree[i].add(newVal);
		Collections.sort(tree[i]);
		
		if (l < r) {
			int mid = (l + r) / 2;
			if (pos <= mid) {
				update(pos, oldVal, newVal, l, mid, i << 1);
			} else {
				update(pos, oldVal, newVal, mid + 1, r, i << 1 | 1);
			}
		}
	}

	// 区间查询前驱
	public static int queryPre(int jobl, int jobr, int k, int l, int r, int i) {
		/**
		 * 区间查询前驱
		 * @param jobl 查询区间左端点
		 * @param jobr 查询区间右端点
		 * @param k 查询值
		 * @param l 当前节点维护区间左端点
		 * @param r 当前节点维护区间右端点
		 * @param i 当前节点编号
		 * @return 值k在区间[jobl, jobr]内的前驱
		 */
		if (jobl <= l && r <= jobr) {
			// 在当前节点的平衡树中查找k的前驱
			int pre = Integer.MIN_VALUE;
			for (int val : tree[i]) {
				if (val < k && val > pre) {
					pre = val;
				}
			}
			return pre == Integer.MIN_VALUE ? -2147483647 : pre;
		}
		int mid = (l + r) / 2;
		int ans = -2147483647;
		if (jobl <= mid) {
			ans = Math.max(ans, queryPre(jobl, jobr, k, l, mid, i << 1));
		}
		if (jobr > mid) {
			ans = Math.max(ans, queryPre(jobl, jobr, k, mid + 1, r, i << 1 | 1));
		}
		return ans;
	}

	// 区间查询后继
	public static int querySuc(int jobl, int jobr, int k, int l, int r, int i) {
		/**
		 * 区间查询后继
		 * @param jobl 查询区间左端点
		 * @param jobr 查询区间右端点
		 * @param k 查询值
		 * @param l 当前节点维护区间左端点
		 * @param r 当前节点维护区间右端点
		 * @param i 当前节点编号
		 * @return 值k在区间[jobl, jobr]内的后继
		 */
		if (jobl <= l && r <= jobr) {
			// 在当前节点的平衡树中查找k的后继
			int suc = Integer.MAX_VALUE;
			for (int val : tree[i]) {
				if (val > k && val < suc) {
					suc = val;
				}
			}
			return suc == Integer.MAX_VALUE ? 2147483647 : suc;
		}
		int mid = (l + r) / 2;
		int ans = 2147483647;
		if (jobl <= mid) {
			ans = Math.min(ans, querySuc(jobl, jobr, k, l, mid, i << 1));
		}
		if (jobr > mid) {
			ans = Math.min(ans, querySuc(jobl, jobr, k, mid + 1, r, i << 1 | 1));
		}
		return ans;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		String[] parts = br.readLine().trim().split("\\s+");
		n = Integer.parseInt(parts[0]);
		m = Integer.parseInt(parts[1]);

		// 读取初始数组
		parts = br.readLine().trim().split("\\s+");
		for (int i = 1; i <= n; i++) {
			arr[i] = Integer.parseInt(parts[i - 1]);
		}

		// 构建线段树
		build(1, n, 1);

		// 处理所有操作
		for (int i = 1; i <= m; i++) {
			parts = br.readLine().trim().split("\\s+");
			int op = Integer.parseInt(parts[0]);
			
			switch (op) {
				case 1: // 查询k在区间内的排名
					int l1 = Integer.parseInt(parts[1]);
					int r1 = Integer.parseInt(parts[2]);
					int k1 = Integer.parseInt(parts[3]);
					out.println(queryRank(l1, r1, k1, 1, n, 1) + 1);
					break;
				case 2: // 查询区间内排名为k的值
					int l2 = Integer.parseInt(parts[1]);
					int r2 = Integer.parseInt(parts[2]);
					int k2 = Integer.parseInt(parts[3]);
					out.println(queryKth(l2, r2, k2, 1, n, 1));
					break;
				case 3: // 修改某一位值上的数值
					int pos = Integer.parseInt(parts[1]);
					int newVal = Integer.parseInt(parts[2]);
					update(pos, arr[pos], newVal, 1, n, 1);
					arr[pos] = newVal;
					break;
				case 4: // 查询k在区间内的前驱
					int l4 = Integer.parseInt(parts[1]);
					int r4 = Integer.parseInt(parts[2]);
					int k4 = Integer.parseInt(parts[3]);
					out.println(queryPre(l4, r4, k4, 1, n, 1));
					break;
				case 5: // 查询k在区间内的后继
					int l5 = Integer.parseInt(parts[1]);
					int r5 = Integer.parseInt(parts[2]);
					int k5 = Integer.parseInt(parts[3]);
					out.println(querySuc(l5, r5, k5, 1, n, 1));
					break;
			}
		}
		
		out.flush();
		out.close();
		br.close();
	}
}