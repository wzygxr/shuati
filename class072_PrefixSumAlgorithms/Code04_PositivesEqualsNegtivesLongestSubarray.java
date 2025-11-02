package class046;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.HashMap;

/**
 * 正负数个数相等的最长子数组 (Positives Equals Negatives Longest Subarray)
 * 
 * 题目描述:
 * 给定一个无序数组arr，其中元素可正、可负、可0
 * 求arr所有子数组中正数与负数个数相等的最长子数组的长度
 * 
 * 示例:
 * 输入: arr = [1, -1, 0, 1, -1]
 * 输出: 4
 * 解释: 子数组 [1, -1, 0, 1] 或 [1, -1, 0, 1, -1] 中正数和负数个数相等。
 * 
 * 输入: arr = [1, 1, -1, -1, 0]
 * 输出: 4
 * 解释: 子数组 [1, 1, -1, -1] 中正数和负数个数相等。
 * 
 * 提示:
 * 1 <= arr.length <= 10^5
 * -10^4 <= arr[i] <= 10^4
 * 
 * 题目链接: https://www.nowcoder.com/practice/545544c060804eceaed0bb84fcd992fb
 * 
 * 解题思路:
 * 1. 将正数看作1，负数看作-1，0看作0，问题转化为求和为0的最长子数组
 * 2. 使用前缀和 + 哈希表的方法
 * 3. 遍历数组，计算前缀和
 * 4. 如果某个前缀和之前出现过，说明这两个位置之间的子数组和为0
 * 5. 使用哈希表记录每个前缀和第一次出现的位置
 * 
 * 时间复杂度: O(n) - 需要遍历数组一次
 * 空间复杂度: O(n) - 哈希表最多存储n个不同的前缀和
 * 
 * 工程化考量:
 * 1. 边界条件处理：空数组、单元素数组
 * 2. 哈希表初始化：前缀和为0在位置-1出现，便于计算长度
 * 3. 映射技巧：正数→1, 负数→-1, 0→0的转换是关键
 * 4. 性能优化：使用HashMap的O(1)查找时间
 * 
 * 最优解分析:
 * 这是最优解，因为必须遍历所有元素才能找到最长子数组。
 * 哈希表方法将时间复杂度从O(n^2)优化到O(n)。
 * 
 * 算法核心:
 * 设count为前缀和（正数→1, 负数→-1, 0→0），当count[i] = count[j]时，子数组[i+1,j]的和为0。
 * 即正数和负数的数量相等。
 * 
 * 算法调试技巧:
 * 1. 打印中间过程：可以在循环中打印每个位置的前缀和和哈希表状态
 * 2. 边界测试：测试全正数、全负数、交替等情况
 * 3. 性能测试：测试大规模数据下的性能表现
 * 
 * 语言特性差异:
 * Java的HashMap自动处理哈希冲突，但需要注意哈希函数的选择。
 * 与C++相比，Java有自动内存管理，无需手动释放哈希表内存。
 * 与Python相比，Java是静态类型语言，需要显式声明类型。
 */
public class Code04_PositivesEqualsNegtivesLongestSubarray {

	public static int MAXN = 100001;

	public static int[] arr = new int[MAXN];

	public static int n;

	public static HashMap<Integer, Integer> map = new HashMap<>();

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			n = (int) in.nval;
			for (int i = 0, num; i < n; i++) {
				in.nextToken();
				num = (int) in.nval;
				arr[i] = num != 0 ? (num > 0 ? 1 : -1) : 0;
			}
			out.println(compute());
		}
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * 计算正数和负数个数相等的最长子数组长度
	 * 
	 * 异常场景处理:
	 * - 空数组：返回0
	 * - 单元素数组：返回0（不可能有相同数量的正数和负数）
	 * - 全正数或全负数数组：返回0
	 * 
	 * 边界条件:
	 * - 数组长度为0或1
	 * - 数组元素全为正数或全为负数
	 * - 数组元素交替出现
	 */
	public static int compute() {
		// 清空哈希表
		map.clear();
		
		// 初始化：前缀和为0在位置-1出现（便于计算长度）
		// 这样当sum=0时，长度计算为i - (-1) = i+1
		map.put(0, -1);
		
		int ans = 0;  // 最长子数组长度
		int sum = 0;  // 当前前缀和（正数看作1，负数看作-1，0看作0）
		
		// 遍历数组
		for (int i = 0; i < n; i++) {
			// 更新前缀和：正数看作1，负数看作-1，0看作0
			sum += arr[i];
			
			// 调试打印：显示中间过程
			// System.out.println("位置 " + i + ": 值 = " + arr[i] + ", 前缀和 = " + sum);
			
			// 如果当前前缀和之前出现过，更新最大长度
			if (map.containsKey(sum)) {
				int length = i - map.get(sum);
				ans = Math.max(ans, length);
				// 调试打印：找到符合条件的子数组
				// System.out.println("找到子数组: 位置 " + (map.get(sum) + 1) + " 到 " + i + ", 长度 = " + length);
			} else {
				// 记录当前前缀和第一次出现的位置
				map.put(sum, i);
				// 调试打印：记录新前缀和
				// System.out.println("记录新前缀和: " + sum + " -> " + i);
			}
		}
		
		return ans;
	}

}
