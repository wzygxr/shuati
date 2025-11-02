package class147;

/**
 * 叶子节点数的期望 - 卡特兰数概率应用
 * 
 * 问题描述：
 * 一共有n个节点，认为节点之间无差别，能形成很多不同结构的二叉树
 * 假设所有不同结构的二叉树，等概率出现一棵，返回叶子节点的期望
 * 
 * 数学背景：
 * 这是卡特兰数在概率论中的一个应用，计算二叉树叶子节点数的期望值。
 * 涉及生成函数和概率论的知识。
 * 
 * 解法思路：
 * 1. 使用数学公式直接计算：n * (n + 1) / ((2 * n - 1) * 2)
 * 2. 基于卡特兰数的生成函数和概率分析
 * 
 * 相关题目链接：
 * - 洛谷 P3978 叶子节点期望: https://www.luogu.com.cn/problem/P3978
 * - LeetCode 96. 不同的二叉搜索树: https://leetcode.cn/problems/unique-binary-search-trees/
 * - LeetCode 95. 不同的二叉搜索树 II: https://leetcode.cn/problems/unique-binary-search-trees-ii/
 * 
 * 时间复杂度分析：
 * - 直接计算：O(1)
 * 
 * 空间复杂度分析：
 * - 常数空间：O(1)
 * 
 * 工程化考量：
 * - 1 <= n <= 10^9
 * - 答案误差小于10的-9次方
 * - 提交时请把类名改成"Main"，可以通过所有测试用例
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code09_LeafExpectation {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		double n = in.nval;
		out.printf("%.9f", n * (n + 1) / ((2 * n - 1) * 2));
		out.flush();
		out.close();
		br.close();
	}

}
