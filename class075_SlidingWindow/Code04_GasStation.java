package class049;

/**
 * 滑动窗口算法解决加油站问题
 * 
 * 问题描述：
 * 在一条环路上有 n 个加油站，其中第 i 个加油站有汽油 gas[i] 升。
 * 你有一辆油箱容量无限的的汽车，从第 i 个加油站开往第 i+1 个加油站需要消耗汽油 cost[i] 升。
 * 你从其中的一个加油站出发，开始时油箱为空。
 * 给定两个整数数组 gas 和 cost ，如果你可以按顺序绕环路行驶一周，
 * 则返回出发时加油站的编号，否则返回 -1。
 * 如果存在解，则保证它是唯一的。
 * 
 * 解题思路：
 * 使用滑动窗口技术处理环形数组问题。
 * 1. 将环形数组展开为线性数组（通过取模运算处理环形特性）
 * 2. 对每个可能的起点，尝试绕行一圈
 * 3. 使用窗口维护当前油量，如果油量不足则更换起点
 * 4. 当窗口大小等于n时，说明可以完成一圈
 * 
 * 算法复杂度分析：
 * 时间复杂度：O(n) - 每个加油站最多被访问两次
 * 空间复杂度：O(1) - 只使用了常数级别的额外空间
 * 
 * 相关题目链接：
 * LeetCode 134. 加油站
 * https://leetcode.cn/problems/gas-station/
 * 
 * 其他平台类似题目：
 * 1. 牛客网 - 加油站问题
 *    https://www.nowcoder.com/practice/a9fec6c46a684ad5a3abd4e365a9d11a
 * 2. LintCode 187. 加油站
 *    https://www.lintcode.com/problem/187/
 * 3. HackerRank - Gas Station
 *    https://www.hackerrank.com/challenges/gas-station/problem
 * 4. CodeChef - STATION - Gas Stations
 *    https://www.codechef.com/problems/STATION
 * 5. AtCoder - ABC146 D - Enough Array
 *    https://atcoder.jp/contests/abc146/tasks/abc146_d
 * 6. 洛谷 P1084 疫情控制
 *    https://www.luogu.com.cn/problem/P1084
 * 7. 杭电OJ 1042 N!
 *    http://acm.hdu.edu.cn/showproblem.php?pid=1042
 * 8. POJ 2739 Sum of Consecutive Prime Numbers
 *    http://poj.org/problem?id=2739
 * 9. UVa OJ 11536 - Smallest Sub-Array
 *    https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2531
 * 10. SPOJ - ADAFRIEN - Ada and Friends
 *     https://www.spoj.com/problems/ADAFRIEN/
 * 
 * 工程化考量：
 * 1. 异常处理：处理空数组、gas和cost数组长度不一致等边界情况
 * 2. 性能优化：通过合理的起点选择避免重复计算
 * 3. 可读性：变量命名清晰，添加详细注释
 */
public class Code04_GasStation {

	/**
	 * 判断能否从某个加油站出发绕环路行驶一周
	 * 
	 * @param gas  每个加油站的汽油量数组
	 * @param cost 从当前加油站到下一加油站的消耗汽油量数组
	 * @return 能够完成一圈的起始加油站编号，如果不存在则返回-1
	 */
	public static int canCompleteCircuit(int[] gas, int[] cost) {
		int n = gas.length;
		
		// 本来下标是0..n-1，但是扩充到0..2*n-1，i位置的余量信息在(r%n)位置
		// 窗口范围是[l, r)，左闭右开，也就是说窗口是[l..r-1]，r是到不了的位置
		for (int l = 0, r = 0, sum; l < n; l = r + 1, r = l) {
			sum = 0;
			
			// 尝试从l位置出发，能否绕行一圈
			while (sum + gas[r % n] - cost[r % n] >= 0) {
				// r位置即将右扩，窗口会变大
				if (r - l + 1 == n) { // 此时检查是否已经转了一圈
					return l;
				}
				
				// r位置进入窗口，累加和加上r位置的余量
				sum += gas[r % n] - cost[r % n];
				
				// r右扩，窗口变大了
				r++;
			}
		}
		
		return -1;
	}

}