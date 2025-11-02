package class038;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

// 有重复项数组的去重全排列
// 测试链接 : https://leetcode.cn/problems/permutations-ii/
public class Code04_PermutationWithoutRepetition {

	/**
	 * 生成数组的所有不重复全排列（可能包含重复元素）
	 * 
	 * 算法思路：
	 * 1. 使用回溯算法生成排列
	 * 2. 在每个位置，使用HashSet记录已经放置过的元素，避免重复
	 * 3. 对于位置i，只尝试将未在该位置放置过的元素交换到位置i
	 * 
	 * 时间复杂度：O(n! * n)，其中n为数组长度
	 * 空间复杂度：O(n)，递归栈深度
	 * 
	 * @param nums 输入数组（可能包含重复元素）
	 * @return 所有不重复的全排列
	 */
	public static List<List<Integer>> permuteUnique(int[] nums) {
		List<List<Integer>> ans = new ArrayList<>();
		f(nums, 0, ans);
		return ans;
	}

	/**
	 * 递归生成不重复排列
	 * 
	 * @param nums 数组
	 * @param i 当前处理的位置
	 * @param ans 结果集合
	 */
	public static void f(int[] nums, int i, List<List<Integer>> ans) {
		if (i == nums.length) {
			// 已经处理完所有位置，将当前排列加入结果集
			List<Integer> cur = new ArrayList<>();
			for (int num : nums) {
				cur.add(num);
			}
			ans.add(cur);
		} else {
			// 使用HashSet记录在位置i已经放置过的元素
			HashSet<Integer> set = new HashSet<>();
			for (int j = i; j < nums.length; j++) {
				// nums[j]没有来到过i位置，才会去尝试
				if (!set.contains(nums[j])) {
					set.add(nums[j]);
					swap(nums, i, j);
					f(nums, i + 1, ans);
					swap(nums, i, j);
				}
			}
		}
	}

	/**
	 * 交换数组中两个位置的元素
	 * 
	 * @param nums 数组
	 * @param i 位置i
	 * @param j 位置j
	 */
	public static void swap(int[] nums, int i, int j) {
		int tmp = nums[i];
		nums[i] = nums[j];
		nums[j] = tmp;
	}
	
	// 测试方法
	public static void main(String[] args) {
		// 测试用例1
		int[] nums1 = {1, 1, 2};
		List<List<Integer>> ans1 = permuteUnique(nums1);
		System.out.println("输入: [1,1,2]");
		System.out.println("输出: " + ans1);
		
		// 测试用例2
		int[] nums2 = {1, 2, 1, 1};
		List<List<Integer>> ans2 = permuteUnique(nums2);
		System.out.println("\n输入: [1,2,1,1]");
		System.out.println("输出: " + ans2);
	}

}