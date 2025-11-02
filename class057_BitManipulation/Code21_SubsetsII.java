package class031;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 子集II
 * 测试链接：https://leetcode.cn/problems/subsets-ii/
 * 
 * 题目描述：
 * 给你一个整数数组 nums ，其中可能包含重复元素，请你返回该数组所有可能的子集（幂集）。
 * 解集不能包含重复的子集。返回的解集中，子集可以按任意顺序排列。
 * 
 * 示例：
 * 输入：nums = [1,2,2]
 * 输出：[[],[1],[1,2],[1,2,2],[2],[2,2]]
 * 
 * 输入：nums = [0]
 * 输出：[[],[0]]
 * 
 * 提示：
 * 1 <= nums.length <= 10
 * -10 <= nums[i] <= 10
 * 
 * 解题思路：
 * 由于数组可能包含重复元素，直接使用位运算会产生重复子集。
 * 需要先对数组排序，然后使用回溯法，在递归时跳过重复元素。
 * 
 * 时间复杂度：O(n * 2^n) - 最坏情况下需要生成2^n个子集
 * 空间复杂度：O(n) - 递归深度为n
 */
public class Code21_SubsetsII {
    
    /**
     * 使用回溯法生成所有不重复子集
     * @param nums 输入数组（可能包含重复元素）
     * @return 所有不重复子集的列表
     */
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        // 先排序，便于跳过重复元素
        Arrays.sort(nums);
        backtrack(nums, 0, new ArrayList<>(), result);
        return result;
    }
    
    /**
     * 回溯辅助函数
     * @param nums 排序后的数组
     * @param start 当前起始位置
     * @param current 当前子集
     * @param result 结果列表
     */
    private void backtrack(int[] nums, int start, List<Integer> current, List<List<Integer>> result) {
        // 添加当前子集到结果
        result.add(new ArrayList<>(current));
        
        for (int i = start; i < nums.length; i++) {
            // 跳过重复元素，避免生成重复子集
            if (i > start && nums[i] == nums[i - 1]) {
                continue;
            }
            
            current.add(nums[i]);
            backtrack(nums, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }
    
    /**
     * 使用位运算的变种方法（处理重复元素）
     * 这种方法先统计每个数字的出现次数，然后根据出现次数生成子集
     * @param nums 输入数组
     * @return 所有不重复子集的列表
     */
    public List<List<Integer>> subsetsWithDupBitmask(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        
        int n = nums.length;
        int totalSubsets = 1 << n;
        
        for (int mask = 0; mask < totalSubsets; mask++) {
            List<Integer> subset = new ArrayList<>();
            boolean valid = true;
            
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    // 检查是否跳过重复元素
                    if (i > 0 && nums[i] == nums[i - 1] && (mask & (1 << (i - 1))) == 0) {
                        valid = false;
                        break;
                    }
                    subset.add(nums[i]);
                }
            }
            
            if (valid) {
                result.add(subset);
            }
        }
        
        return result;
    }
    
    // 测试方法
    public static void main(String[] args) {
        Code21_SubsetsII solution = new Code21_SubsetsII();
        
        int[] test1 = {1, 2, 2};
        int[] test2 = {0};
        int[] test3 = {1, 1, 2};
        
        System.out.println("Test 1: " + solution.subsetsWithDup(test1));
        System.out.println("Test 2: " + solution.subsetsWithDup(test2));
        System.out.println("Test 3: " + solution.subsetsWithDup(test3));
    }
}