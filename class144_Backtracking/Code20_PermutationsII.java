package class038;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LeetCode 47. 全排列 II
 * 
 * 题目描述：
 * 给定一个可包含重复数字的序列 nums ，按任意顺序返回所有不重复的全排列。
 * 
 * 示例：
 * 输入：nums = [1,1,2]
 * 输出：[[1,1,2],[1,2,1],[2,1,1]]
 * 
 * 输入：nums = [1,2,3]
 * 输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
 * 
 * 提示：
 * 1 <= nums.length <= 8
 * -10 <= nums[i] <= 10
 * 
 * 链接：https://leetcode.cn/problems/permutations-ii/
 * 
 * 算法思路：
 * 1. 先对数组进行排序，使相同元素相邻
 * 2. 使用回溯算法生成所有排列
 * 3. 使用布尔数组标记已使用的元素
 * 4. 对于重复元素，确保相同元素的相对顺序，避免生成重复排列
 * 
 * 时间复杂度：O(n * n!)，其中n是数组长度，共有n!个排列，每个排列需要O(n)时间复制
 * 空间复杂度：O(n)，递归栈深度和存储路径的空间
 */
public class Code20_PermutationsII {

    /**
     * 生成包含重复元素的数组的所有不重复全排列
     * 
     * @param nums 输入数组（可能包含重复元素）
     * @return 所有不重复的全排列
     */
    public static List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        // 先排序，使相同元素相邻
        Arrays.sort(nums);
        boolean[] used = new boolean[nums.length];
        backtrack(nums, used, new ArrayList<>(), result);
        return result;
    }

    /**
     * 回溯函数生成排列
     * 
     * @param nums 输入数组
     * @param used 标记已使用元素的数组
     * @param path 当前路径
     * @param result 结果列表
     */
    private static void backtrack(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> result) {
        // 终止条件：排列长度等于数组长度
        if (path.size() == nums.length) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        for (int i = 0; i < nums.length; i++) {
            // 跳过已使用的元素
            if (used[i]) {
                continue;
            }
            
            // 去重关键：如果当前元素与前一个相同，且前一个元素未被使用，则跳过
            // 这样可以确保相同元素的相对顺序，避免生成重复排列
            if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) {
                continue;
            }
            
            used[i] = true;
            path.add(nums[i]);
            backtrack(nums, used, path, result);
            path.remove(path.size() - 1);
            used[i] = false;
        }
    }

    /**
     * 解法二：使用交换元素的方式生成排列
     * 通过交换元素实现原地排列，减少空间使用
     * 
     * @param nums 输入数组
     * @return 所有不重复的全排列
     */
    public static List<List<Integer>> permuteUnique2(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        backtrack2(nums, 0, result);
        return result;
    }

    private static void backtrack2(int[] nums, int start, List<List<Integer>> result) {
        if (start == nums.length) {
            List<Integer> permutation = new ArrayList<>();
            for (int num : nums) {
                permutation.add(num);
            }
            result.add(permutation);
            return;
        }
        
        for (int i = start; i < nums.length; i++) {
            // 去重关键：如果当前元素与前面某个元素相同，且该元素已经被交换过，则跳过
            if (i != start && nums[i] == nums[start] && i > start) {
                continue;
            }
            
            // 检查是否应该交换
            boolean shouldSwap = true;
            for (int j = start; j < i; j++) {
                if (nums[j] == nums[i]) {
                    shouldSwap = false;
                    break;
                }
            }
            
            if (shouldSwap) {
                swap(nums, start, i);
                backtrack2(nums, start + 1, result);
                swap(nums, start, i);
            }
        }
    }

    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 1, 2};
        List<List<Integer>> result1 = permuteUnique(nums1);
        System.out.println("输入: nums = [1, 1, 2]");
        System.out.println("输出: " + result1);
        
        // 测试用例2
        int[] nums2 = {1, 2, 3};
        List<List<Integer>> result2 = permuteUnique(nums2);
        System.out.println("\n输入: nums = [1, 2, 3]");
        System.out.println("输出: " + result2);
        
        // 测试用例3
        int[] nums3 = {2, 2, 1, 1};
        List<List<Integer>> result3 = permuteUnique(nums3);
        System.out.println("\n输入: nums = [2, 2, 1, 1]");
        System.out.println("输出: " + result3);
        
        // 测试解法二
        System.out.println("\n=== 解法二测试 ===");
        List<List<Integer>> result4 = permuteUnique2(nums1);
        System.out.println("输入: nums = [1, 1, 2]");
        System.out.println("输出: " + result4);
    }
}