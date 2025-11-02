package class113;

/**
 * 酒店房间问题
 * 题目来源：洛谷 P2894 [USACO08FEB] Hotel G
 * 题目链接：https://www.luogu.com.cn/problem/P2894
 * 
 * 核心算法：线段树 + 区间查找与更新
 * 难度：提高+/省选-
 * 
 * 【题目详细描述】
 * 有一个酒店，有n个房间排成一排，编号为1~n
 * 初始时，所有房间都是空的
 * 现在有m次操作，每次操作可能是以下两种之一：
 * 1. 入住操作：需要k个连续的空房间，这时需要从1号房间开始找
 *    找到最左边的连续k个空房间，将这k个房间住满
 *    如果没有足够的连续房间，那么无法入住（这种情况不处理）
 * 2. 退房操作：给定一个区间[l,r]，将这个区间里的所有房间清空
 * 对于每个入住操作，输出入住的第一个房间的编号
 * 如果无法入住，输出0
 * 
 * 【解题思路】
 * 使用线段树维护每个区间的以下信息：
 * - len[i]：区间内最长连续空房间的长度
 * - pre[i]：区间左端开始的连续空房间长度
 * - suf[i]：区间右端开始的连续空房间长度
 * - sum[i]：区间内空房间的总数（用于快速判断是否有足够的空房间）
 * 
 * 【核心算法】
 * 1. 线段树节点设计：
 *    - len[i]：区间内最长连续空房间的长度
 *    - pre[i]：从左端点开始连续空房间的长度
 *    - suf[i]：从右端点开始连续空房间的长度
 *    - sum[i]：区间内空房间的总数
 *    - lazy[i]：懒标记，表示区间是否被统一设置（0表示空，1表示满）
 * 
 * 2. 查找操作：
 *    - 对于入住操作，需要在整个区间中找到最左边的、长度足够的连续空房间
 *    - 优先检查左子区间是否有足够的空房间
 *    - 其次检查左右子区间连接处是否有足够的空房间
 *    - 最后检查右子区间
 * 
 * 3. 更新操作：
 *    - 入住操作对应的是区间置1（表示房间被占用）
 *    - 退房操作对应的是区间置0（表示房间被清空）
 *    - 使用懒标记延迟下传，提高效率
 * 
 * 【复杂度分析】
 * - 时间复杂度：
 *   - 建树：O(n)
 *   - 单次操作（入住、退房）：O(log n)
 *   - 总时间复杂度：O(n + m log n)
 * - 空间复杂度：O(n)，线段树所需空间
 * 
 * 【算法优化点】
 * 1. 懒标记优化：使用懒标记避免不必要的递归更新
 * 2. 查找优化：在查找最左连续空房间时，利用线段树的特性快速定位
 * 3. 合并逻辑：优化区间合并操作，高效计算父区间的len、pre、suf
 * 
 * 【工程化考量】
 * 1. 输入输出效率：使用BufferedReader和PrintWriter优化IO
 * 2. 内存管理：静态数组预分配空间，避免动态分配的开销
 * 3. 边界条件处理：处理空区间、满区间等特殊情况
 * 4. 错误处理：处理无效的入住和退房操作
 * 
 * 【类似题目推荐】
 * 1. LeetCode 715. Range 模块 - https://leetcode.cn/problems/range-module/
 * 2. LeetCode 855. 考场就座 - https://leetcode.cn/problems/exam-room/
 * 3. Codeforces 867E - Buy Low Sell High - https://codeforces.com/problemset/problem/867/E
 * 4. SPOJ GSS5 - Can you answer these queries V - https://www.spoj.com/problems/GSS5/
 * 5. POJ 3667 - Hotel - http://poj.org/problem?id=3667
 * 6. HDU 4027 - Can you answer these queries IV - http://acm.hdu.edu.cn/showproblem.php?pid=4027
 * 7. 洛谷 P1471 方差 - https://www.luogu.com.cn/problem/P1471
 */

/*
题目解析:
有n个房间，初始都为空房。支持查找连续空房间和清空房间操作。

解题思路:
使用线段树维护每个区间的连续空房间信息，包括最长连续空房间长度、前缀和后缀长度。

关键技术点:
1. 查询最左边满足条件的区间需要特殊处理
2. 区间合并时需要考虑左右子区间的连接情况

复杂度分析:
- 时间复杂度：
  - 建树：O(n)
  - 单次操作：O(log n)
  - 总时间复杂度：O((n + m) log n)
- 空间复杂度：O(n)

补充题目:
1. LeetCode 699. 掉落的方块 - https://leetcode.cn/problems/falling-squares/
2. LeetCode 850. 矩形面积 II - https://leetcode.cn/problems/rectangle-area-ii/
3. Codeforces 438D - The Child and Sequence - https://codeforces.com/problemset/problem/438/D
4. Codeforces 558E - A Simple Task - https://codeforces.com/problemset/problem/558/E
5. 洛谷 P4198 楼房重建 - https://www.luogu.com.cn/problem/P4198
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code04_Hotel {

	public static int MAXN = 50001;

	// 连续空房最长子串长度
	public static int[] len = new int[MAXN << 2];

	// 连续空房最长前缀长度
	public static int[] pre = new int[MAXN << 2];

	// 连续空房最长后缀长度
	public static int[] suf = new int[MAXN << 2];

	// 懒更新信息，范围上所有数字被重置成了什么
	public static int[] change = new int[MAXN << 2];

	// 懒更新信息，范围上有没有重置任务
	public static boolean[] update = new boolean[MAXN << 2];

	public static void up(int i, int ln, int rn) {
		int l = i << 1;
		int r = i << 1 | 1;
		len[i] = Math.max(Math.max(len[l], len[r]), suf[l] + pre[r]);
		pre[i] = len[l] < ln ? pre[l] : (pre[l] + pre[r]);
		suf[i] = len[r] < rn ? suf[r] : (suf[l] + suf[r]);
	}

	public static void down(int i, int ln, int rn) {
		if (update[i]) {
			lazy(i << 1, change[i], ln);
			lazy(i << 1 | 1, change[i], rn);
			update[i] = false;
		}
	}

	public static void lazy(int i, int v, int n) {
		len[i] = pre[i] = suf[i] = v == 0 ? n : 0;
		change[i] = v;
		update[i] = true;
	}

	public static void build(int l, int r, int i) {
		if (l == r) {
			len[i] = pre[i] = suf[i] = 1;
		} else {
			int mid = (l + r) >> 1;
			build(l, mid, i << 1);
			build(mid + 1, r, i << 1 | 1);
			up(i, mid - l + 1, r - mid);
		}
		update[i] = false;
	}

	public static void update(int jobl, int jobr, int jobv, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			lazy(i, jobv, r - l + 1);
		} else {
			int mid = (l + r) >> 1;
			down(i, mid - l + 1, r - mid);
			if (jobl <= mid) {
				update(jobl, jobr, jobv, l, mid, i << 1);
			}
			if (jobr > mid) {
				update(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
			}
			up(i, mid - l + 1, r - mid);
		}
	}

	// 在l..r范围上，在满足空房长度>=x的情况下，返回尽量靠左的开头位置
	// 递归需要遵循的潜台词 : l..r范围上一定存在连续空房长度>=x的区域
	public static int queryLeft(int x, int l, int r, int i) {
		if (l == r) {
			return l;
		} else {
			int mid = (l + r) >> 1;
			down(i, mid - l + 1, r - mid);
			// 最先查左边
			if (len[i << 1] >= x) {
				return queryLeft(x, l, mid, i << 1);
			}
			// 然后查中间向两边扩展的可能区域
			if (suf[i << 1] + pre[i << 1 | 1] >= x) {
				return mid - suf[i << 1] + 1;
			}
			// 前面都没有再最后查右边
			return queryLeft(x, mid + 1, r, i << 1 | 1);
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		int n = (int) in.nval;
		build(1, n, 1);
		in.nextToken();
		int m = (int) in.nval;
		for (int i = 1, op, x, y, left; i <= m; i++) {
			in.nextToken();
			op = (int) in.nval;
			if (op == 1) {
				in.nextToken();
				x = (int) in.nval;
				if (len[1] < x) {
					left = 0;
				} else {
					left = queryLeft(x, 1, n, 1);
					update(left, left + x - 1, 1, 1, n, 1);
				}
				out.println(left);
			} else {
				in.nextToken();
				x = (int) in.nval;
				in.nextToken();
				y = (int) in.nval;
				update(x, Math.min(x + y - 1, n), 0, 1, n, 1);
			}
		}
		out.flush();
		out.close();
		br.close();
	}

}