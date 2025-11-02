package class038;

import java.util.*;

/**
 * LeetCode 46. 全排列
 * 
 * 题目描述：
 * 给定一个不含重复数字的数组 nums，返回其所有可能的全排列。你可以按任意顺序返回答案。
 * 
 * 示例：
 * 输入：nums = [1,2,3]
 * 输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
 * 
 * 输入：nums = [0,1]
 * 输出：[[0,1],[1,0]]
 * 
 * 输入：nums = [1]
 * 输出：[[1]]
 * 
 * 提示：
 * 1 <= nums.length <= 6
 * -10 <= nums[i] <= 10
 * nums 中的所有整数互不相同
 * 
 * 链接：https://leetcode.cn/problems/permutations/
 */
public class Code16_Permutations {
    
    /**
     * 生成数组的所有可能全排列
     * 
     * 算法思路：
     * 1. 使用回溯算法生成所有排列
     * 2. 使用一个used数组来标记每个数字是否已经被使用
     * 3. 对于每个位置，尝试将未使用的数字放入当前位置
     * 4. 递归处理下一个位置
     * 5. 回溯时，将当前数字标记为未使用，尝试其他选择
     * 
     * 时间复杂度：O(N * N!)，其中N是数组长度。生成N!个排列，每个排列需要O(N)时间复制。
     * 空间复杂度：O(N)，递归栈的深度加上used数组的大小。
     * 
     * @param nums 输入数组
     * @return 所有可能的全排列
     */
    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return result;
        }
        
        boolean[] used = new boolean[nums.length]; // 标记数字是否已使用
        List<Integer> path = new ArrayList<>(); // 当前排列路径
        
        backtrack(nums, used, path, result);
        return result;
    }
    
    /**
     * 回溯函数生成排列
     * 
     * @param nums 输入数组
     * @param used 标记数组，记录数字是否已使用
     * @param path 当前排列路径
     * @param result 结果列表
     */
    private static void backtrack(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> result) {
        // 终止条件：当前排列长度等于数组长度
        if (path.size() == nums.length) {
            result.add(new ArrayList<>(path)); // 深拷贝当前路径
            return;
        }
        
        // 尝试将每个未使用的数字放入当前位置
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) {
                continue; // 跳过已使用的数字
            }
            
            // 选择当前数字
            used[i] = true;
            path.add(nums[i]);
            
            // 递归处理下一个位置
            backtrack(nums, used, path, result);
            
            // 回溯：撤销选择
            path.remove(path.size() - 1);
            used[i] = false;
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 2, 3};
        List<List<Integer>> result1 = permute(nums1);
        System.out.println("输入: nums = [1,2,3]");
        System.out.println("输出: " + result1);
        
        // 测试用例2
        int[] nums2 = {0, 1};
        List<List<Integer>> result2 = permute(nums2);
        System.out.println("\n输入: nums = [0,1]");
        System.out.println("输出: " + result2);
        
        // 测试用例3
        int[] nums3 = {1};
        List<List<Integer>> result3 = permute(nums3);
        System.out.println("\n输入: nums = [1]");
        System.out.println("输出: " + result3);
    }
}