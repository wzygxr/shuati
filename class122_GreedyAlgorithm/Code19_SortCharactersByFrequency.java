package class091;

import java.util.*;

// 根据字符出现频率排序
// 给定一个字符串 s ，根据字符出现的 频率 对其进行 降序排序 。
// 一个字符出现的 频率 是它出现在字符串中的次数。
// 返回 已排序的字符串 。如果有多个答案，返回其中任何一个。
// 测试链接 : https://leetcode.cn/problems/sort-characters-by-frequency/
public class Code19_SortCharactersByFrequency {

	/**
	 * 根据字符出现频率排序
	 * 
	 * 算法思路：
	 * 使用贪心策略：
	 * 1. 统计每个字符出现的频率
	 * 2. 将字符和频率存入优先队列（最大堆），按频率降序排序
	 * 3. 从优先队列中依次取出字符，构建结果字符串
	 * 
	 * 正确性分析：
	 * 1. 我们需要按频率降序排列字符
	 * 2. 使用优先队列可以高效地获取频率最高的字符
	 * 3. 贪心选择频率最高的字符，可以得到正确的结果
	 * 
	 * 时间复杂度：O(n + k*logk) - n是字符串长度，k是字符集大小
	 * 空间复杂度：O(k) - 需要存储字符频率和优先队列
	 * 
	 * @param s 输入字符串
	 * @return 按频率排序后的字符串
	 */
	public static String frequencySort(String s) {
		// 边界情况处理
		if (s == null || s.length() == 0) {
			return s;
		}
		
		// 统计每个字符出现的频率
		Map<Character, Integer> frequencyMap = new HashMap<>();
		for (char c : s.toCharArray()) {
			frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
		}
		
		// 使用优先队列（最大堆）按频率降序排序
		PriorityQueue<Map.Entry<Character, Integer>> maxHeap = 
			new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());
		maxHeap.addAll(frequencyMap.entrySet());
		
		// 构建结果字符串
		StringBuilder result = new StringBuilder();
		while (!maxHeap.isEmpty()) {
			Map.Entry<Character, Integer> entry = maxHeap.poll();
			char c = entry.getKey();
			int frequency = entry.getValue();
			
			// 将字符按频率添加到结果中
			for (int i = 0; i < frequency; i++) {
				result.append(c);
			}
		}
		
		return result.toString();
	}

	// 测试用例
	public static void main(String[] args) {
		// 测试用例1: s = "tree" -> 输出: "eert" 或 "eetr"
		String s1 = "tree";
		System.out.println("测试用例1:");
		System.out.println("字符串: " + s1);
		System.out.println("排序结果: " + frequencySort(s1)); // 期望输出: "eert" 或 "eetr"
		
		// 测试用例2: s = "cccaaa" -> 输出: "cccaaa" 或 "aaaccc"
		String s2 = "cccaaa";
		System.out.println("\n测试用例2:");
		System.out.println("字符串: " + s2);
		System.out.println("排序结果: " + frequencySort(s2)); // 期望输出: "cccaaa" 或 "aaaccc"
		
		// 测试用例3: s = "Aabb" -> 输出: "bbAa" 或 "bbaA"
		String s3 = "Aabb";
		System.out.println("\n测试用例3:");
		System.out.println("字符串: " + s3);
		System.out.println("排序结果: " + frequencySort(s3)); // 期望输出: "bbAa" 或 "bbaA"
		
		// 测试用例4: s = "abcdefg" -> 输出: "abcdefg" 或其他排列
		String s4 = "abcdefg";
		System.out.println("\n测试用例4:");
		System.out.println("字符串: " + s4);
		System.out.println("排序结果: " + frequencySort(s4)); // 期望输出: "abcdefg" 或其他排列
	}
}