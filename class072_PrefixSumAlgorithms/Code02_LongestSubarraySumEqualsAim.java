package class046;

/**
 * 最长子数组和等于给定值 (Longest Subarray Sum Equals Aim)
 * 
 * 题目描述:
 * 给定一个无序数组arr，其中元素可正、可负、可0
 * 给定一个整数aim
 * 求arr所有子数组中累加和为aim的最长子数组长度
 * 
 * 示例:
 * 输入: arr = [1, -1, 5, -2, 3], aim = 3
 * 输出: 4
 * 解释: 子数组 [1, -1, 5, -2] 和等于 3，且长度最长。
 * 
 * 输入: arr = [-2, -1, 2, 1], aim = 1
 * 输出: 2
 * 解释: 子数组 [-1, 2] 和等于 1，且长度最长。
 * 
 * 提示:
 * 1 <= arr.length <= 10^5
 * -10^4 <= arr[i] <= 10^4
 * -10^5 <= aim <= 10^5
 * 
 * 题目链接: https://www.nowcoder.com/practice/36fb0fd3c656480c92b569258a1223d5
 * 
 * 解题思路:
 * 使用前缀和 + 哈希表的方法。
 * 1. 遍历数组，计算前缀和
 * 2. 对于当前位置的前缀和sum，查找是否存在前缀和为(sum - aim)的历史记录
 * 3. 如果存在，则说明存在子数组和为aim
 * 4. 使用哈希表记录每个前缀和第一次出现的位置
 * 
 * 时间复杂度: O(n) - 需要遍历数组一次
 * 空间复杂度: O(n) - 哈希表最多存储n个不同的前缀和
 * 
 * 工程化考量:
 * 1. 边界条件处理：空数组、aim值极端情况
 * 2. 哈希表选择：HashMap提供O(1)的平均查找时间
 * 3. 位置记录：记录每个前缀和第一次出现的位置
 * 4. 性能优化：一次遍历完成所有计算
 * 
 * 最优解分析:
 * 这是最优解，因为必须遍历所有元素才能找到最长子数组。
 * 哈希表方法将时间复杂度从O(n^2)优化到O(n)。
 * 
 * 算法核心:
 * 设prefix[i]为前i个元素的和，则子数组[i,j]的和为prefix[j] - prefix[i-1] = aim
 * 即prefix[j] - aim = prefix[i-1]，因此统计prefix[j] - aim第一次出现的位置即可。
 * 
 * 算法调试技巧:
 * 1. 打印中间过程：可以在循环中打印每个位置的前缀和和哈希表状态
 * 2. 边界测试：测试空数组、aim=0、负数等情况
 * 3. 性能测试：测试大规模数据下的性能表现
 * 
 * 语言特性差异:
 * Java的HashMap自动处理哈希冲突，但需要注意哈希函数的选择。
 * 与C++相比，Java有自动内存管理，无需手动释放哈希表内存。
 * 与Python相比，Java是静态类型语言，需要显式声明类型。
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.HashMap;

public class Code02_LongestSubarraySumEqualsAim {

	public static int MAXN = 100001;

	public static int[] arr = new int[MAXN];

	public static int n, aim;

	/**
	 * HashMap用于存储前缀和及其第一次出现的位置
	 * key : 某个前缀和
	 * value : 这个前缀和最早出现的位置
	 */
	public static HashMap<Integer, Integer> map = new HashMap<>();

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			n = (int) in.nval;
			in.nextToken();
			aim = (int) in.nval;
			for (int i = 0; i < n; i++) {
				in.nextToken();
				arr[i] = (int) in.nval;
			}
			out.println(compute());
		}
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * 计算和为aim的最长子数组长度
	 * 
	 * 异常场景处理:
	 * - 空数组：返回0
	 * - aim值极端：可能为极大值或极小值
	 * - 数组元素包含负数：算法本身支持
	 * 
	 * 边界条件:
	 * - 数组长度为0
	 * - aim=0的情况
	 * - 数组元素全为0且aim=0
	 */
	public static int compute() {
		map.clear();
		// 初始化：前缀和为0在位置-1出现（便于计算长度）
		// 这样当sum=aim时，长度计算为i - (-1) = i+1
		map.put(0, -1);
		
		int ans = 0;      // 最长子数组长度
		int sum = 0;      // 当前前缀和
		
		// 遍历数组
		for (int i = 0; i < n; i++) {
			// 更新前缀和
			sum += arr[i];
			
			// 调试打印：显示中间过程
			// System.out.println("位置 " + i + ": 值 = " + arr[i] + ", 前缀和 = " + sum);
			
			// 如果当前前缀和-aim之前出现过，更新最大长度
			if (map.containsKey(sum - aim)) {
				int length = i - map.get(sum - aim);
				ans = Math.max(ans, length);
				// 调试打印：找到符合条件的子数组
				// System.out.println("找到子数组: 位置 " + (map.get(sum - aim) + 1) + " 到 " + i + ", 长度 = " + length);
			}
			
			// 如果当前前缀和之前没有出现过，记录第一次出现的位置
			if (!map.containsKey(sum)) {
				map.put(sum, i);
				// 调试打印：记录新前缀和
				// System.out.println("记录新前缀和: " + sum + " -> " + i);
			}
		}
		
		return ans;
	}

}
