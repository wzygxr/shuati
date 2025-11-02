package class031;

import java.util.ArrayList;
import java.util.List;

/**
 * 子集
 * 测试链接：https://leetcode.cn/problems/subsets/
 * 
 * 题目描述：
 * 给你一个整数数组 nums ，数组中的元素互不相同。返回该数组所有可能的子集（幂集）。
 * 解集不能包含重复的子集。你可以按任意顺序返回解集。
 * 
 * 示例：
 * 输入：nums = [1,2,3]
 * 输出：[[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
 * 
 * 输入：nums = [0]
 * 输出：[[],[0]]
 * 
 * 提示：
 * 1 <= nums.length <= 10
 * -10 <= nums[i] <= 10
 * nums 中的所有元素互不相同
 * 
 * 解题思路：
 * 使用位运算来生成所有子集。对于长度为n的数组，共有2^n个子集。
 * 每个子集可以用一个n位的二进制数表示，其中第i位为1表示包含nums[i]，为0表示不包含。
 * 
 * 时间复杂度：O(n * 2^n) - 需要生成2^n个子集，每个子集需要O(n)时间构建
 * 空间复杂度：O(n) - 不考虑输出空间，递归深度为n
 */
public class Code20_Subsets {
    
    /**
     * 使用位运算生成所有子集
     * @param nums 输入数组
     * @return 所有子集的列表
     */
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        int n = nums.length;
        int totalSubsets = 1 << n; // 2^n个子集
        
        // 遍历所有可能的二进制掩码
        for (int mask = 0; mask < totalSubsets; mask++) {
            List<Integer> subset = new ArrayList<>();
            
            // 检查每个位，如果为1则添加对应元素
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    subset.add(nums[i]);
                }
            }
            result.add(subset);
        }
        
        return result;
    }
    
    /**
     * 回溯法实现（备选方案）
     * @param nums 输入数组
     * @return 所有子集的列表
     */
    public List<List<Integer>> subsetsBacktrack(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(nums, 0, new ArrayList<>(), result);
        return result;
    }
    
    private void backtrack(int[] nums, int start, List<Integer> current, List<List<Integer>> result) {
        result.add(new ArrayList<>(current));
        
        for (int i = start; i < nums.length; i++) {
            current.add(nums[i]);
            backtrack(nums, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        Code20_Subsets solution = new Code20_Subsets();
        
        int[] test1 = {1, 2, 3};
        int[] test2 = {0};
        int[] test3 = {1, 2};
        
        System.out.println("Test 1: " + solution.subsets(test1));
        System.out.println("Test 2: " + solution.subsets(test2));
        System.out.println("Test 3: " + solution.subsets(test3));
    }
}