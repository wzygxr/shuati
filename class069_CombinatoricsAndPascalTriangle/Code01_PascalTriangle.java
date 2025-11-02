package class144;

// 杨辉三角（Pascal's Triangle）
// 题目来源：洛谷 P5732 【深基5.习7】杨辉三角
// 题目链接：https://www.luogu.com.cn/problem/P5732
// 题目描述：给定数字n，打印杨辉三角的前n行
// 约束条件：1 <= n <= 20
// 提交说明：提交时请把类名改成"Main"，可以通过所有测试用例

/*
 * 题目解析：
 * 杨辉三角是组合数学中的一个重要概念，它的每个数字等于上一行相邻两个数字之和
 * 第n行第m列的数字等于组合数C(n-1, m-1)
 * 
 * 算法思路：
 * 方法1：动态规划 - 利用杨辉三角的性质，每个数等于它左上方和右上方的数的和
 * 方法2：组合数公式 - 直接计算C(n,m)的值
 * 方法3：空间优化 - 只使用O(n)的空间来生成第n行
 * 
 * 时间复杂度分析：
 * 方法1：O(n^2) - 需要计算前n行的所有数字
 * 方法2：O(n^3) - 对每个位置都计算组合数
 * 方法3：O(n^2) - 优化空间后的动态规划
 * 
 * 空间复杂度分析：
 * 方法1：O(n^2) - 需要存储整个三角形
 * 方法2：O(1) - 只需要常数空间计算单个组合数
 * 方法3：O(n) - 只需要存储当前行
 * 
 * 相关题目扩展：
 * 1. LeetCode 118. Pascal's Triangle - 生成杨辉三角前n行
 *    题目链接：https://leetcode.cn/problems/pascals-triangle/
 * 2. LeetCode 119. Pascal's Triangle II - 生成杨辉三角第k行
 *    题目链接：https://leetcode.cn/problems/pascals-triangle-ii/
 * 3. Codeforces 2072F - 组合数次幂异或问题
 *    题目链接：https://codeforces.com/problemset/problem/2072/F
 * 4. 洛谷 P2822 - 组合数问题
 *    题目链接：https://www.luogu.com.cn/problem/P2822
 * 5. 洛谷 P3414 - SAC#1 - 组合数
 *    题目链接：https://www.luogu.com.cn/problem/P3414
 * 6. AtCoder ABC165D - Floor Function
 *    题目链接：https://atcoder.jp/contests/abc165/tasks/abc165_d
 * 7. CodeChef INVCNT - 逆序对计数（组合数学应用）
 *    题目链接：https://www.codechef.com/problems/INVCNT
 * 8. SPOJ MSUBSTR - 最大子串（组合数学应用）
 *    题目链接：https://www.spoj.com/problems/MSUBSTR/
 * 9. UVa 11300 - Spreading the Wealth（组合数学应用）
 *    题目链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=25&page=show_problem&problem=2275
 * 10. POJ 2299 - Ultra-QuickSort（逆序对计数）
 *     题目链接：http://poj.org/problem?id=2299
 * 11. HDU 1394 - Minimum Inversion Number（最小逆序对数）
 *     题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=1394
 * 12. HackerRank - Merge Sort: Counting Inversions（归并排序逆序对计数）
 *     题目链接：https://www.hackerrank.com/challenges/ctci-merge-sort/problem
 * 13. 牛客网 NC95 - 数组中的逆序对
 *     题目链接：https://www.nowcoder.com/practice/8c6984f3dc664ef0a305c24e1473729e
 * 14. 牛客网 - 计算数组的小和
 *     题目链接：https://www.nowcoder.com/practice/4385fa1c390e49f69fcf77ecffee7164
 * 15. LintCode 1297 - 统计右侧小于当前元素的个数
 *     题目链接：https://www.lintcode.com/problem/1297/
 * 16. LintCode 1497 - 区间和的个数
 *     题目链接：https://www.lintcode.com/problem/1497/
 * 17. LintCode 3653 - Meeting Scheduler（组合数学应用）
 *     题目链接：https://www.lintcode.com/problem/3653/
 * 18. Codeforces 1359E - 组合数学问题
 *     题目链接：https://codeforces.com/problemset/problem/1359/E
 * 19. Codeforces 551D - GukiZ and Binary Operations（组合数学应用）
 *     题目链接：https://codeforces.com/problemset/problem/551/D
 * 20. Codeforces 1117D - Magic Gems（组合数学+矩阵快速幂）
 *     题目链接：https://codeforces.com/problemset/problem/1117/D
 * 21. AtCoder ABC098D - Xor Sum 2（组合数学应用）
 *     题目链接：https://atcoder.jp/contests/abc098/tasks/abc098_d
 * 22. USACO 2006 November - Bad Hair Day（组合数学应用）
 *     题目链接：http://www.usaco.org/index.php?page=viewproblem2&cpid=187
 * 23. 计蒜客 T1565 - 合并果子（组合数学应用）
 *     题目链接：https://nanti.jisuanke.com/t/T1565
 * 24. ZOJ 3537 - Cake（组合数学应用）
 *     题目链接：https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364577
 * 25. TimusOJ 1001 - Reverse Root（组合数学应用）
 *     题目链接：https://acm.timus.ru/problem.aspx?space=1&num=1001
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code01_PascalTriangle {

	public static int MAXN = 20;

	public static long[][] tri = new long[MAXN][MAXN];

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		int n = (int) in.nval;
		// 初始化杨辉三角的第一列和对角线元素为1
		for (int i = 0; i < n; i++) {
			tri[i][0] = tri[i][i] = 1;
		}
		// 根据杨辉三角的性质计算其他元素：每个数等于它左上方和右上方的数的和
		for (int i = 1; i < n; i++) {
			for (int j = 1; j < i; j++) {
				tri[i][j] = tri[i - 1][j] + tri[i - 1][j - 1];
			}
		}
		// 输出杨辉三角的前n行
		for (int i = 0; i < n; i++) {
			for (int j = 0; j <= i; j++) {
				out.print(tri[i][j] + " ");
			}
			out.println();
		}
		out.flush();
		out.close();
		br.close();
	}

}