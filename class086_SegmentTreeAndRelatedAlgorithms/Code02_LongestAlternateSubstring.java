package class113;

/**
 * 最长LR交替子串
 * 题目来源：洛谷 P6492
 * 题目链接：https://www.luogu.com.cn/problem/P6492
 * 
 * 核心算法：线段树 + 区间信息维护
 * 难度：中等
 * 
 * 【题目详细描述】
 * 给定一个长度为n的字符串，一开始字符串中全是'L'字符
 * 有q次修改，每次指定一个位置i
 * 如果i位置是'L'字符那么改成'R'字符
 * 如果i位置是'R'字符那么改成'L'字符
 * 如果一个子串是两种字符不停交替出现的样子，也就是LRLR... 或者RLRL...
 * 那么说这个子串是有效子串
 * 每次修改后，都打印当前整个字符串中最长交替子串的长度
 * 
 * 【解题思路】
 * 使用线段树维护每个区间的最长交替子串长度，以及前缀和后缀的最长交替长度。
 * 
 * 【核心算法】
 * 线段树的每个节点维护三个关键信息：
 * 1. len[i]：区间内最长交替子串的长度
 * 2. pre[i]：区间左端开始的最长交替前缀长度
 * 3. suf[i]：区间右端结束的最长交替后缀长度
 * 
 * 合并两个相邻区间时，需要考虑左子区间的后缀和右子区间的前缀是否可以拼接成更长的交替子串。
 * 关键在于判断中间两个字符是否不同（arr[mid] != arr[mid + 1]）。
 * 
 * 【复杂度分析】
 * - 时间复杂度：
 *   - 建树：O(n)
 *   - 单次操作：O(log n)
 *   - 总时间复杂度：O(n + q log n)
 * - 空间复杂度：O(n)，线段树所需空间为4n
 * 
 * 【算法优化点】
 * 1. 位运算优化：使用异或操作（arr[jobi] ^= 1）高效实现字符翻转
 * 2. 懒惰标记优化：虽然本题没有使用，但对于需要区间更新的类似问题可以考虑
 * 3. 空间优化：使用数组而非对象存储线段树信息，减少内存开销
 * 
 * 【工程化考量】
 * 1. 输入输出效率：使用BufferedReader、StreamTokenizer和PrintWriter实现高效输入输出
 * 2. 边界条件处理：初始化全为'L'的情况，单元素区间的处理
 * 3. 数据类型选择：使用int数组存储字符（0表示'L'，1表示'R'），提高效率
 * 4. 代码可读性：函数命名清晰，逻辑模块化
 * 5. 内存管理：使用static数组预分配空间，避免动态分配
 * 
 * 【使用注意】
 * 提交时请把类名改成"Main"，可以直接通过
 */

/*
题目解析:
给定一个字符串，初始全为'L'，每次操作翻转一个位置的字符，求每次操作后最长的LR交替子串长度。

解题思路:
使用线段树维护每个区间的最长交替子串长度，以及前缀和后缀的最长交替长度。

关键技术点:
1. 区间合并时需要判断中间连接处是否可以连接
2. 单点更新时需要重新计算区间信息

复杂度分析:
- 时间复杂度：
  - 建树：O(n)
  - 单次操作：O(log n)
  - 总时间复杂度：O(n + q log n)
- 空间复杂度：O(n)

线段树常见变种:
1. 动态开点线段树：适用场景：数据范围很大，但实际使用较少的情况
2. 可持久化线段树（主席树）：适用场景：需要保存历史版本的信息
3. 扫描线 + 线段树：适用场景：平面几何问题，如矩形面积并
4. 树链剖分 + 线段树：适用场景：树上路径操作
5. 线段树合并：适用场景：动态维护多个集合的信息

补充题目:
1. LeetCode 315. 计算右侧小于当前元素的个数 - https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
2. LeetCode 327. 区间和的个数 - https://leetcode.cn/problems/count-of-range-sum/
3. LeetCode 493. 翻转对 - https://leetcode.cn/problems/reverse-pairs/
4. Codeforces 339D - Xenia and Bit Operations - https://codeforces.com/problemset/problem/339/D
5. Codeforces 380C - Sereja and Brackets - https://codeforces.com/problemset/problem/380/C
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code02_LongestAlternateSubstring {

	public static int MAXN = 200001;

	// 原始数组
	public static int[] arr = new int[MAXN];

	// 交替最长子串长度
	public static int[] len = new int[MAXN << 2];

	// 交替最长前缀长度
	public static int[] pre = new int[MAXN << 2];

	// 交替最长后缀长度
	public static int[] suf = new int[MAXN << 2];

	public static void up(int l, int r, int i) {
		len[i] = Math.max(len[i << 1], len[i << 1 | 1]);
		pre[i] = pre[i << 1];
		suf[i] = suf[i << 1 | 1];
		int mid = (l + r) >> 1;
		int ln = mid - l + 1;
		int rn = r - mid;
		if (arr[mid] != arr[mid + 1]) {
			len[i] = Math.max(len[i], suf[i << 1] + pre[i << 1 | 1]);
			if (len[i << 1] == ln) {
				pre[i] = ln + pre[i << 1 | 1];
			}
			if (len[i << 1 | 1] == rn) {
				suf[i] = rn + suf[i << 1];
			}
		}
	}

	public static void build(int l, int r, int i) {
		if (l == r) {
			len[i] = 1;
			pre[i] = 1;
			suf[i] = 1;
		} else {
			int mid = (l + r) >> 1;
			build(l, mid, i << 1);
			build(mid + 1, r, i << 1 | 1);
			up(l, r, i);
		}
	}

	public static void reverse(int jobi, int l, int r, int i) {
		if (l == r) {
			arr[jobi] ^= 1;
		} else {
			int mid = (l + r) >> 1;
			if (jobi <= mid) {
				reverse(jobi, l, mid, i << 1);
			} else {
				reverse(jobi, mid + 1, r, i << 1 | 1);
			}
			up(l, r, i);
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		int n = (int) in.nval;
		in.nextToken();
		int q = (int) in.nval;
		build(1, n, 1);
		for (int i = 1, index; i <= q; i++) {
			in.nextToken();
			index = (int) in.nval;
			reverse(index, 1, n, 1);
			out.println(len[1]);
		}
		out.flush();
		out.close();
		br.close();
	}

}