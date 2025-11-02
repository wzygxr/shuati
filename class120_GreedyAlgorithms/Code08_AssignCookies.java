package class089;

import java.util.Arrays;

// 分发饼干
// 假设你是一位很棒的家长，想要给你的孩子们一些小饼干。但是，每个孩子最多只能给一块饼干
// 对每个孩子i，都有一个胃口值g[i]，这是能让孩子们满足胃口的饼干的最小尺寸
// 并且每块饼干j，都有一个尺寸s[j]。如果s[j] >= g[i]，我们可以将这个饼干j分配给孩子i
// 目标是尽可能满足越多数量的孩子，并输出这个最大数值
// 测试链接 : https://leetcode.cn/problems/assign-cookies/
public class Code08_AssignCookies {

	/**
	 * 使用贪心算法解决分发饼干问题
	 * 
	 * 解题思路:
	 * 贪心算法 + 双指针
	 * 1. 将孩子胃口值数组g和饼干尺寸数组s都按升序排列
	 * 2. 使用双指针分别指向当前考虑的孩子和饼干
	 * 3. 如果当前饼干能满足当前孩子，则两个指针都前移，满足孩子数加1
	 * 4. 如果当前饼干不能满足当前孩子，则饼干指针前移，寻找更大的饼干
	 * 5. 直到其中一个数组遍历完为止
	 * 
	 * 时间复杂度分析:
	 * O(m*logm + n*logn) - 其中m是孩子数量，n是饼干数量
	 * - 对孩子胃口值数组排序需要O(m*logm)
	 * - 对饼干尺寸数组排序需要O(n*logn)
	 * - 双指针遍历需要O(m+n)
	 * 
	 * 空间复杂度分析:
	 * O(1) - 只使用了常数级别的额外空间（不考虑排序所需的额外空间）
	 * 
	 * 是否为最优解:
	 * 是，这是解决该问题的最优解
	 * 
	 * 工程化考量:
	 * 1. 边界条件处理: 空数组、单个元素数组等特殊情况
	 * 2. 输入验证: 检查输入是否为有效数组
	 * 3. 异常处理: 对非法输入进行检查
	 * 4. 可读性: 添加详细注释和变量命名
	 */
	public static int findContentChildren(int[] g, int[] s) {
		// 输入验证
		if (g == null || s == null || g.length == 0 || s.length == 0) {
			return 0;
		}
		
		// 将孩子胃口值数组和饼干尺寸数组都按升序排列
		Arrays.sort(g);
		Arrays.sort(s);
		
		int childIndex = 0;   // 孩子指针
		int cookieIndex = 0;  // 饼干指针
		int satisfiedChildren = 0; // 满足的孩子数
		
		// 双指针遍历
		while (childIndex < g.length && cookieIndex < s.length) {
			// 如果当前饼干能满足当前孩子
			if (s[cookieIndex] >= g[childIndex]) {
				satisfiedChildren++; // 满足孩子数加1
				childIndex++;        // 孩子指针前移
			}
			// 无论是否满足，饼干指针都要前移
			cookieIndex++;
		}
		
		return satisfiedChildren;
	}
	
	// 测试函数
	public static void main(String[] args) {
		// 测试用例1: g = [1,2,3], s = [1,1]
		int[] g1 = {1, 2, 3};
		int[] s1 = {1, 1};
		int result1 = findContentChildren(g1, s1);
		System.out.println("输入: g = [1,2,3], s = [1,1]");
		System.out.println("输出: " + result1);
		System.out.println("预期: 1");
		System.out.println();
		
		// 测试用例2: g = [1,2], s = [1,2,3]
		int[] g2 = {1, 2};
		int[] s2 = {1, 2, 3};
		int result2 = findContentChildren(g2, s2);
		System.out.println("输入: g = [1,2], s = [1,2,3]");
		System.out.println("输出: " + result2);
		System.out.println("预期: 2");
		System.out.println();
		
		// 测试用例3: g = [1,2,7,8,9], s = [1,3,5,9,10]
		int[] g3 = {1, 2, 7, 8, 9};
		int[] s3 = {1, 3, 5, 9, 10};
		int result3 = findContentChildren(g3, s3);
		System.out.println("输入: g = [1,2,7,8,9], s = [1,3,5,9,10]");
		System.out.println("输出: " + result3);
		System.out.println("预期: 4");
		System.out.println();
		
		// 测试用例4: 空孩子数组
		int[] g4 = {};
		int[] s4 = {1, 2, 3};
		int result4 = findContentChildren(g4, s4);
		System.out.println("输入: g = [], s = [1,2,3]");
		System.out.println("输出: " + result4);
		System.out.println("预期: 0");
		System.out.println();
		
		// 测试用例5: 空饼干数组
		int[] g5 = {1, 2, 3};
		int[] s5 = {};
		int result5 = findContentChildren(g5, s5);
		System.out.println("输入: g = [1,2,3], s = []");
		System.out.println("输出: " + result5);
		System.out.println("预期: 0");
		System.out.println();
	}
}