package class091;

import java.util.List;
import java.util.TreeSet;

// 最小区间
// 你有k个非递减排列的整数列表
// 找到一个最小区间，使得k个列表中的每个列表至少有一个数包含在其中
// 测试链接 : https://leetcode.cn/problems/smallest-range-covering-elements-from-k-lists/
public class Code02_SmallestRange {

	public static class Node {
		public int v; // 值
		public int i; // 当前值来自哪个数组
		public int j; // 当前值来自i号数组的什么位置

		public Node(int a, int b, int c) {
			v = a;
			i = b;
			j = c;
		}
	}

	/**
	 * 找到最小区间，使得k个列表中的每个列表至少有一个数包含在其中
	 * 
	 * 算法思路：
	 * 使用滑动窗口 + TreeSet的贪心策略：
	 * 1. 将每个数组的第一个元素加入TreeSet
	 * 2. 每次取出最小值，将其对应数组的下一个元素加入TreeSet
	 * 3. 在过程中记录最小的区间
	 * 
	 * 时间复杂度：O(n*logk) - n是所有元素总数，k是数组数量
	 * 空间复杂度：O(k) - TreeSet中最多存储k个元素
	 * 
	 * @param nums k个非递减排列的整数列表
	 * @return 最小区间 [start, end]
	 */
	public static int[] smallestRange(List<List<Integer>> nums) {
		int k = nums.size();
		
		// 根据值排序
		// 为什么排序的时候i要参与
		// 因为有序表中比较相等的样本只会保留一个
		// 为了值一样的元素都保留，于是i要参与排序
		// 在有序表中的所有元素i必然都不同
		TreeSet<Node> set = new TreeSet<>((a, b) -> a.v != b.v ? (a.v - b.v) : (a.i - b.i));
		
		// 初始化：将每个数组的第一个元素加入TreeSet
		for (int i = 0; i < k; i++) {
			set.add(new Node(nums.get(i).get(0), i, 0));
		}
		
		int r = Integer.MAX_VALUE; // 记录最窄区间的宽度
		int a = 0; // 记录最窄区间的开头
		int b = 0; // 记录最窄区间的结尾
		
		Node max, min;
		// 当TreeSet中有k个元素时继续循环
		while (set.size() == k) {
			max = set.last(); // 在有序表中，值最大的记录
			min = set.pollFirst(); // 在有序表中，值最小的记录，并弹出
			
			// 更新最小区间
			if (max.v - min.v < r) {
				r = max.v - min.v;
				a = min.v;
				b = max.v;
			}
			
			// 如果min所在数组还有下一个元素，则将其加入TreeSet
			if (min.j + 1 < nums.get(min.i).size()) {
				set.add(new Node(nums.get(min.i).get(min.j + 1), min.i, min.j + 1));
			}
		}
		return new int[] { a, b };
	}

	// 测试用例
	public static void main(String[] args) {
		// 测试用例: [[4,10,15,24,26], [0,9,12,20], [5,18,22,30]]
		// 期望结果: [20,24]
		List<List<Integer>> nums = new java.util.ArrayList<>();
		nums.add(java.util.Arrays.asList(4, 10, 15, 24, 26));
		nums.add(java.util.Arrays.asList(0, 9, 12, 20));
		nums.add(java.util.Arrays.asList(5, 18, 22, 30));
		
		int[] result = smallestRange(nums);
		System.out.println("测试用例: [[4,10,15,24,26], [0,9,12,20], [5,18,22,30]]");
		System.out.println("结果: [" + result[0] + ", " + result[1] + "]"); // 期望输出: [20, 24]
	}
}