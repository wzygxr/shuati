package class038;

import java.util.*;

/**
 * LeetCode 39. 组合总和
 * 
 * 题目描述：
 * 给你一个 无重复元素 的整数数组 candidates 和一个目标整数 target ，找出 candidates 中可以使数字和为目标数 target 的所有不同组合 ，
 * 并以列表形式返回。你可以按 任意顺序 返回这些组合。
 * candidates 中的 同一个 数字可以 无限制重复被选取 。如果至少一个数字的被选数量不同，则两种组合是不同的。
 * 
 * 对于给定的输入，保证和为 target 的不同组合数少于 150 个。
 * 
 * 示例：
 * 输入：candidates = [2,3,6,7], target = 7
 * 输出：[[2,2,3],[7]]
 * 解释：
 * 2 和 3 可以形成一组候选，2 + 2 + 3 = 7 。注意 2 可以使用多次。
 * 7 也是一个候选，7 = 7 。
 * 仅有这两种组合。
 * 
 * 输入: candidates = [2,3,5], target = 8
 * 输出: [[2,2,2,2],[2,3,3],[3,5]]
 * 
 * 输入: candidates = [2], target = 1
 * 输出: []
 * 
 * 提示：
 * 1 <= candidates.length <= 30
 * 2 <= candidates[i] <= 40
 * candidates 的所有元素 互不相同
 * 1 <= target <= 40
 * 
 * 链接：https://leetcode.cn/problems/combination-sum/
 */
public class Code17_CombinationSum {
    
    /**
     * 找出所有和为target的组合
     * 
     * 算法思路：
     * 1. 使用回溯算法生成所有可能的组合
     * 2. 由于每个数字可以重复使用，所以递归时从当前索引开始，而不是下一个索引
     * 3. 为了避免重复组合，对数组进行排序，并按顺序选取元素
     * 4. 剪枝：当当前和大于target时，停止当前路径的探索
     * 
     * 时间复杂度：O(N^(T/M))，其中N是数组长度，T是target值，M是数组中的最小元素。最坏情况下需要探索的组合数约为N^(T/M)。
     * 空间复杂度：O(T/M)，递归栈的最大深度。
     * 
     * @param candidates 候选数组
     * @param target 目标和
     * @return 所有可能的组合
     */
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if (candidates == null || candidates.length == 0) {
            return result;
        }
        
        // 排序以便剪枝优化
        Arrays.sort(candidates);
        List<Integer> path = new ArrayList<>();
        
        backtrack(candidates, 0, target, 0, path, result);
        return result;
    }
    
    /**
     * 回溯函数生成组合
     * 
     * @param candidates 候选数组
     * @param start 当前选择的起始索引
     * @param target 目标和
     * @param currentSum 当前和
     * @param path 当前组合路径
     * @param result 结果列表
     */
    private static void backtrack(int[] candidates, int start, int target, int currentSum, 
                                 List<Integer> path, List<List<Integer>> result) {
        // 终止条件：找到一个有效组合
        if (currentSum == target) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        // 从start开始选择元素，避免重复组合
        for (int i = start; i < candidates.length; i++) {
            // 剪枝：如果当前元素已经使得和超过target，由于数组已排序，后面的元素更大，直接跳过
            if (currentSum + candidates[i] > target) {
                break;
            }
            
            // 选择当前元素
            path.add(candidates[i]);
            currentSum += candidates[i];
            
            // 递归：由于元素可以重复使用，所以起始索引仍然是i
            backtrack(candidates, i, target, currentSum, path, result);
            
            // 回溯：撤销选择
            currentSum -= candidates[i];
            path.remove(path.size() - 1);
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] candidates1 = {2, 3, 6, 7};
        int target1 = 7;
        List<List<Integer>> result1 = combinationSum(candidates1, target1);
        System.out.println("输入: candidates = [2,3,6,7], target = 7");
        System.out.println("输出: " + result1);
        
        // 测试用例2
        int[] candidates2 = {2, 3, 5};
        int target2 = 8;
        List<List<Integer>> result2 = combinationSum(candidates2, target2);
        System.out.println("\n输入: candidates = [2,3,5], target = 8");
        System.out.println("输出: " + result2);
        
        // 测试用例3
        int[] candidates3 = {2};
        int target3 = 1;
        List<List<Integer>> result3 = combinationSum(candidates3, target3);
        System.out.println("\n输入: candidates = [2], target = 1");
        System.out.println("输出: " + result3);
    }
}