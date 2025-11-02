package class038;

import java.util.ArrayList;
import java.util.List;

// 没有重复项数字的全排列
// 测试链接 : https://leetcode.cn/problems/permutations/
public class Code03_Permutations {

	/**
	 * 生成数组的所有全排列（无重复元素）
	 * 
	 * 算法思路：
	 * 1. 使用回溯算法，通过交换元素位置生成所有排列
	 * 2. 对于位置i，尝试将后面每个元素交换到位置i
	 * 3. 递归处理位置i+1
	 * 4. 回溯时恢复交换前的状态
	 * 
	 * 时间复杂度：O(n! * n)，其中n为数组长度
	 * 空间复杂度：O(n)，递归栈深度
	 * 
	 * @param nums 输入数组（无重复元素）
	 * @return 所有全排列
	 */
	public static List<List<Integer>> permute(int[] nums) {
		List<List<Integer>> ans = new ArrayList<>();
		f(nums, 0, ans);
		return ans;
	}

	/**
	 * 递归生成排列
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
			// 尝试将位置j的元素交换到位置i
			for (int j = i; j < nums.length; j++) {
				swap(nums, i, j);
				f(nums, i + 1, ans);
				swap(nums, i, j); // 回溯，恢复状态
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

	public static void main(String[] args) {
		// 测试用例1
		int[] nums1 = { 1, 2, 3 };
		List<List<Integer>> ans1 = permute(nums1);
		System.out.println("输入: [1,2,3]");
		System.out.println("输出: " + ans1);
		
		// 测试用例2
		int[] nums2 = { 0, 1 };
		List<List<Integer>> ans2 = permute(nums2);
		System.out.println("\n输入: [0,1]");
		System.out.println("输出: " + ans2);
		
		// 测试用例3
		int[] nums3 = { 1 };
		List<List<Integer>> ans3 = permute(nums3);
		System.out.println("\n输入: [1]");
		System.out.println("输出: " + ans3);
	}

}