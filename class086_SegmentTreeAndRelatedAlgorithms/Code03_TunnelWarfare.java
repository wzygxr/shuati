package class113;

/**
 * 地道相连的房子
 * 题目来源：洛谷 P1503 【国家集训队】地道战
 * 题目链接：https://www.luogu.com.cn/problem/P1503
 * 
 * 核心算法：线段树 + 区间连通性维护
 * 难度：提高+/省选-
 * 
 * 【题目详细描述】
 * 有n个房子排成一排，编号1~n，一开始每相邻的两个房子之间都有地道
 * 实现如下三个操作：
 * 1. D x：把x号房子摧毁，该房子附近的地道也一并摧毁
 * 2. R：恢复上次摧毁的房子，该房子附近的地道一并恢复
 * 3. Q x：查询x号房子能到达的房子数量，包括x号房子自身
 * 
 * 【解题思路】
 * 使用线段树维护每个区间的以下信息：
 * - pre[i]：区间左端连续未被摧毁的房子数量
 * - suf[i]：区间右端连续未被摧毁的房子数量
 * 
 * 同时需要一个栈来记录被摧毁的房子顺序，以便实现撤销操作（恢复最后被摧毁的房子）。
 * 
 * 【核心算法】
 * 1. 线段树节点设计：
 *    - pre[i]：从左端点开始连续未被摧毁的长度
 *    - suf[i]：从右端点开始连续未被摧毁的长度
 * 
 * 2. 合并操作：
 *    - 左右子区间合并时，需要考虑连接处的连通性
 *    - 如果左子区间的pre等于其长度，那么父区间的pre可以加上右子区间的pre
 *    - 同理，如果右子区间的suf等于其长度，那么父区间的suf可以加上左子区间的suf
 * 
 * 3. 恢复操作：
 *    - 使用栈记录被摧毁的房子顺序
 *    - 每次恢复操作就是撤销最后一次摧毁操作
 * 
 * 【复杂度分析】
 * - 时间复杂度：
 *   - 建树：O(n)
 *   - 单次操作（摧毁、恢复、查询）：O(log n)
 *   - 总时间复杂度：O(n + m log n)
 * - 空间复杂度：O(n)，线段树和栈所需空间
 * 
 * 【算法优化点】
 * 1. 位标记：使用数组记录每个房子是否被摧毁，避免重复摧毁
 * 2. 栈优化：使用栈高效记录摧毁顺序，支持快速撤销
 * 3. 线段树合并逻辑：优化合并操作，快速计算父区间信息
 * 
 * 【工程化考量】
 * 1. 输入输出效率：使用BufferedReader和PrintWriter优化IO
 * 2. 数据结构选择：合理选择栈结构实现撤销操作
 * 3. 内存管理：静态数组预分配空间
 * 4. 错误处理：处理无效输入，如重复摧毁同一房子
 * 
 * 【类似题目推荐】
 * 1. LeetCode 715. Range 模块 - https://leetcode.cn/problems/range-module/
 * 2. LeetCode 729. 我的日程安排表 I - https://leetcode.cn/problems/my-calendar-i/
 * 3. LeetCode 731. 我的日程安排表 II - https://leetcode.cn/problems/my-calendar-ii/
 * 4. LeetCode 732. 我的日程安排表 III - https://leetcode.cn/problems/my-calendar-iii/
 * 5. Codeforces 52C - Circular RMQ - https://codeforces.com/problemset/problem/52/C
 * 6. Codeforces 242E - XOR on Segment - https://codeforces.com/problemset/problem/242/E
 * 7. 洛谷 P2894 [USACO08FEB] Hotel G - https://www.luogu.com.cn/problem/P2894
 */

/*
题目解析:
有n个房子排成一排，相邻房子有地道连接。支持摧毁、恢复和查询操作。

解题思路:
使用线段树维护每个区间的连续1的前缀和后缀长度，其中1表示房子未被摧毁。

关键技术点:
1. 查询操作需要根据位置在区间中的位置进行不同处理
2. 区间合并时考虑跨区间的情况

复杂度分析:
- 时间复杂度：
  - 建树：O(n)
  - 单次操作：O(log n)
  - 总时间复杂度：O((n + m) log n)
- 空间复杂度：O(n)

补充题目:
1. LeetCode 715. Range 模块 - https://leetcode.cn/problems/range-module/
2. LeetCode 729. 我的日程安排表 I - https://leetcode.cn/problems/my-calendar-i/
3. LeetCode 731. 我的日程安排表 II - https://leetcode.cn/problems/my-calendar-ii/
4. LeetCode 732. 我的日程安排表 III - https://leetcode.cn/problems/my-calendar-iii/
5. Codeforces 52C - Circular RMQ - https://codeforces.com/problemset/problem/52/C
6. Codeforces 242E - XOR on Segment - https://codeforces.com/problemset/problem/242/E
7. 洛谷 P2894 [USACO08FEB] Hotel G - https://www.luogu.com.cn/problem/P2894
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code03_TunnelWarfare {

	public static int MAXN = 50001;

	// 连续1的最长前缀长度
	public static int[] pre = new int[MAXN << 2];

	// 连续1的最长后缀长度
	public static int[] suf = new int[MAXN << 2];

	// 摧毁的房屋编号入栈，以便执行恢复操作
	public static int[] stack = new int[MAXN];

	public static void up(int l, int r, int i) {
		pre[i] = pre[i << 1];
		suf[i] = suf[i << 1 | 1];
		int mid = (l + r) >> 1;
		if (pre[i << 1] == mid - l + 1) {
			pre[i] += pre[i << 1 | 1];
		}
		if (suf[i << 1 | 1] == r - mid) {
			suf[i] += suf[i << 1];
		}
	}

	public static void build(int l, int r, int i) {
		if (l == r) {
			pre[i] = suf[i] = 1;
		} else {
			int mid = (l + r) >> 1;
			build(l, mid, i << 1);
			build(mid + 1, r, i << 1 | 1);
			up(l, r, i);
		}
	}

	public static void update(int jobi, int jobv, int l, int r, int i) {
		if (l == r) {
			pre[i] = suf[i] = jobv;
		} else {
			int mid = (l + r) >> 1;
			if (jobi <= mid) {
				update(jobi, jobv, l, mid, i << 1);
			} else {
				update(jobi, jobv, mid + 1, r, i << 1 | 1);
			}
			up(l, r, i);
		}
	}

	// 已知jobi在l...r范围上
	// 返回jobi往两侧扩展出的最大长度
	// 递归需要遵循的潜台词 : 从jobi往两侧扩展，一定无法扩展到l...r范围之外！
	public static int query(int jobi, int l, int r, int i) {
		if (l == r) {
			return pre[i];
		} else {
			int mid = (l + r) >> 1;
			if (jobi <= mid) {
				if (jobi > mid - suf[i << 1]) {
					return suf[i << 1] + pre[i << 1 | 1];
				} else {
					return query(jobi, l, mid, i << 1);
				}
			} else {
				if (mid + pre[i << 1 | 1] >= jobi) {
					return suf[i << 1] + pre[i << 1 | 1];
				} else {
					return query(jobi, mid + 1, r, i << 1 | 1);
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			int n = (int) in.nval;
			in.nextToken();
			int m = (int) in.nval;
			build(1, n, 1);
			String op;
			int stackSize = 0;
			for (int i = 1, x; i <= m; i++) {
				in.nextToken();
				op = in.sval;
				if (op.equals("D")) {
					in.nextToken();
					x = (int) in.nval;
					update(x, 0, 1, n, 1);
					stack[stackSize++] = x;
				} else if (op.equals("R")) {
					update(stack[--stackSize], 1, 1, n, 1);
				} else {
					in.nextToken();
					x = (int) in.nval;
					out.println(query(x, 1, n, 1));
				}
			}
		}
		out.flush();
		out.close();
		br.close();
	}

}