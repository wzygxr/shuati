package class140;

// 无法组成的最大值
// 一共有a、b两种面值的硬币，a和b一定互质，每种硬币都有无限个
// 返回a和b无法组成的钱数中，最大值是多少
// 题目的输入保证存在最大的无法组成的钱数
// 1 <= a、b <= 10^9
// 测试链接 : https://www.luogu.com.cn/problem/P3951
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

/**
 * 无法组成的最大值问题（赛瓦维斯特定理应用）
 * 
 * 问题描述：
 * 给定两种面值为a和b的硬币（a和b互质），每种硬币有无限个，求无法用这两种硬币组成的最大钱数
 * 
 * 解题思路：
 * 1. 根据赛瓦维斯特定理（Chicken McNugget Theorem），当a和b互质时，
 *    无法表示的最大整数是a*b-a-b
 * 2. 这是因为当a和b互质时，所有大于a*b-a-b的整数都可以表示为ax+by的形式（x,y>=0）
 * 
 * 数学原理：
 * 1. 赛瓦维斯特定理（Chicken McNugget Theorem）：
 *    当正整数a和b互质时，不能表示为ax+by（x,y>=0）的最大整数是ab-a-b
 * 2. 这个定理是数论中的经典结果，与线性丢番图方程密切相关
 * 3. 该定理可以推广到多个数的情况，但形式更复杂
 * 
 * 时间复杂度：O(log(min(a,b)))，主要消耗在求最大公约数上（验证互质）
 * 空间复杂度：O(1)
 * 
 * 相关题目：
 * 1. 洛谷 P3951 [NOIP2017 提高组] 小凯的疑惑
 *    链接：https://www.luogu.com.cn/problem/P3951
 *    这是本题的来源，是一道经典题
 * 
 * 2. LeetCode 1250. 检查「好数组」
 *    链接：https://leetcode.cn/problems/check-if-it-is-a-good-array/
 *    本题用到了裴蜀定理，如果数组中所有元素的最大公约数为1，则为好数组
 * 
 * 3. HDU 1792 A New Change Problem
 *    链接：https://acm.hdu.edu.cn/showproblem.php?pid=1792
 *    本题是小凯的疑惑的变形，求无法表示的最大数和无法表示的数的个数
 * 
 * 4. AtCoder Beginner Contest 161 - Problem D
 *    链接：https://atcoder.jp/contests/abc161/tasks/abc161_d
 *    本题涉及最大公约数的应用
 * 
 * 5. Codeforces 1244C. The Football Stage
 *    链接：https://codeforces.com/problemset/problem/1244/C
 *    本题需要求解线性丢番图方程wx + dy = p，其中w和d是给定的，p是变量
 * 
 * 工程化考虑：
 * 1. 异常处理：需要处理输入非法等情况
 * 2. 边界条件：需要考虑a或b为1的情况
 * 3. 性能优化：对于大数据，要注意防止溢出
 * 4. 可读性：添加详细注释，变量命名清晰
 * 
 * 算法要点：
 * 1. 赛瓦维斯特定理是解决此类问题的关键
 * 2. 理解互质的含义和重要性
 * 3. 注意防止整数溢出
 */
public class Code05_LargestUnattainable {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		long a = (long) in.nval;
		in.nextToken();
		long b = (long) in.nval;
		out.println(a * b - a - b);
		out.flush();
		out.close();
		br.close();
	}

}