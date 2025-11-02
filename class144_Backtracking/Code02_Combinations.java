package class038;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 给你一个整数数组 nums ，其中可能包含重复元素，请你返回该数组所有可能的组合
// 答案 不能 包含重复的组合。返回的答案中，组合可以按 任意顺序 排列
// 注意其实要求返回的不是子集，因为子集一定是不包含相同元素的，要返回的其实是不重复的组合
// 比如输入：nums = [1,2,2]
// 输出：[[],[1],[1,2],[1,2,2],[2],[2,2]]
// 测试链接 : https://leetcode.cn/problems/subsets-ii/
public class Code02_Combinations {

	/**
	 * 返回数组所有可能的不重复组合
	 * 
	 * 算法思路：
	 * 1. 首先对数组排序，使相同元素相邻
	 * 2. 使用回溯算法，对于每组相同元素，统一处理选择0个、1个、2个...的情况
	 * 3. 通过跳过相同元素避免重复组合
	 * 
	 * 时间复杂度：O(2^n * n)，其中n为数组长度
	 * 空间复杂度：O(n)，递归栈深度
	 * 
	 * @param nums 输入数组
	 * @return 所有不重复的组合
	 */
	public static List<List<Integer>> subsetsWithDup(int[] nums) {
		List<List<Integer>> ans = new ArrayList<>();
		// 排序是去重的关键步骤
		Arrays.sort(nums);
		f(nums, 0, new int[nums.length], 0, ans);
		return ans;
	}

	/**
	 * 递归函数生成所有组合
	 * 
	 * @param nums 输入数组
	 * @param i 当前处理到的索引
	 * @param path 当前路径（组合）
	 * @param size 当前路径的大小
	 * @param ans 结果集合
	 */
	public static void f(int[] nums, int i, int[] path, int size, List<List<Integer>> ans) {
		if (i == nums.length) {
			// 将当前路径加入结果集
			ArrayList<Integer> cur = new ArrayList<>();
			for (int j = 0; j < size; j++) {
				cur.add(path[j]);
			}
			ans.add(cur);
		} else {
			// 找到下一组第一个不同元素的位置
			int j = i + 1;
			while (j < nums.length && nums[i] == nums[j]) {
				j++;
			}
			// 当前数nums[i]，选择0个
			f(nums, j, path, size, ans);
			// 当前数nums[i]，选择1个、2个、3个...都尝试
			for (; i < j; i++) {
				path[size++] = nums[i];
				f(nums, j, path, size, ans);
			}
		}
	}
	
	// 测试方法
	public static void main(String[] args) {
		// 测试用例1
		int[] test1 = {1, 2, 2};
		List<List<Integer>> result1 = subsetsWithDup(test1);
		System.out.println("输入: [1,2,2]");
		System.out.println("输出: " + result1);
		
		// 测试用例2
		int[] test2 = {0};
		List<List<Integer>> result2 = subsetsWithDup(test2);
		System.out.println("\n输入: [0]");
		System.out.println("输出: " + result2);
	}

}