package class038;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LeetCode 40. 组合总和 II
 * 
 * 题目描述：
 * 给定一个候选人编号的集合 candidates 和一个目标数 target ，找出 candidates 中所有可以使数字和为 target 的组合。
 * candidates 中的每个数字在每个组合中只能使用一次。
 * 注意：解集不能包含重复的组合。 
 * 
 * 示例：
 * 输入: candidates = [10,1,2,7,6,1,5], target = 8
 * 输出: [[1,1,6],[1,2,5],[1,7],[2,6]]
 * 
 * 输入: candidates = [2,5,2,1,2], target = 5
 * 输出: [[1,2,2],[5]]
 * 
 * 提示：
 * 1 <= candidates.length <= 100
 * 1 <= candidates[i] <= 50
 * 1 <= target <= 30
 * 
 * 链接：https://leetcode.cn/problems/combination-sum-ii/
 * 
 * 算法思路：
 * 1. 先对数组进行排序，使相同元素相邻
 * 2. 使用回溯算法生成所有组合
 * 3. 对于重复元素，确保相同元素的相对顺序，避免生成重复组合
 * 4. 使用剪枝优化：当当前和超过target时提前终止
 * 
 * 时间复杂度：O(2^n)，其中n是数组长度
 * 空间复杂度：O(n)，递归栈深度
 */
public class Code21_CombinationSumII {

    /**
     * 生成所有和为target的组合（每个数字只能使用一次）
     * 
     * @param candidates 候选数组
     * @param target 目标和
     * @return 所有满足条件的组合
     */
    public static List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(candidates);
        backtrack(candidates, target, 0, new ArrayList<>(), result);
        return result;
    }

    /**
     * 回溯函数生成组合
     * 
     * @param candidates 候选数组
     * @param target 剩余目标值
     * @param start 当前起始位置
     * @param path 当前路径
     * @param result 结果列表
     */
    private static void backtrack(int[] candidates, int target, int start, List<Integer> path, List<List<Integer>> result) {
        // 终止条件：目标值为0
        if (target == 0) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        for (int i = start; i < candidates.length; i++) {
            // 剪枝：如果当前数字已经大于剩余目标值，提前终止
            if (candidates[i] > target) {
                break;
            }
            
            // 去重关键：跳过重复元素（不是第一个出现的重复元素）
            if (i > start && candidates[i] == candidates[i - 1]) {
                continue;
            }
            
            path.add(candidates[i]);
            // 注意：这里传入i+1，因为每个数字只能使用一次
            backtrack(candidates, target - candidates[i], i + 1, path, result);
            path.remove(path.size() - 1);
        }
    }

    /**
     * 解法二：使用计数法处理重复元素
     * 对于重复元素，我们可以选择0个、1个、2个...直到不超过目标值的最大个数
     * 
     * @param candidates 候选数组
     * @param target 目标和
     * @return 所有满足条件的组合
     */
    public static List<List<Integer>> combinationSum2_2(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(candidates);
        backtrack2(candidates, target, 0, new ArrayList<>(), result);
        return result;
    }

    private static void backtrack2(int[] candidates, int target, int start, List<Integer> path, List<List<Integer>> result) {
        if (target == 0) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        if (start >= candidates.length || target < 0) {
            return;
        }
        
        // 统计当前元素出现的次数
        int count = 1;
        int i = start + 1;
        while (i < candidates.length && candidates[i] == candidates[start]) {
            count++;
            i++;
        }
        
        // 对于当前元素，可以选择0个、1个、2个...最多不超过目标值的个数
        for (int j = 0; j <= count; j++) {
            // 如果选择j个当前元素已经超过目标值，提前终止
            if (j * candidates[start] > target) {
                break;
            }
            
            // 添加j个当前元素
            for (int k = 0; k < j; k++) {
                path.add(candidates[start]);
            }
            
            backtrack2(candidates, target - j * candidates[start], start + count, path, result);
            
            // 回溯，移除添加的元素
            for (int k = 0; k < j; k++) {
                path.remove(path.size() - 1);
            }
        }
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] candidates1 = {10, 1, 2, 7, 6, 1, 5};
        int target1 = 8;
        List<List<Integer>> result1 = combinationSum2(candidates1, target1);
        System.out.println("输入: candidates = [10,1,2,7,6,1,5], target = " + target1);
        System.out.println("输出: " + result1);
        
        // 测试用例2
        int[] candidates2 = {2, 5, 2, 1, 2};
        int target2 = 5;
        List<List<Integer>> result2 = combinationSum2(candidates2, target2);
        System.out.println("\n输入: candidates = [2,5,2,1,2], target = " + target2);
        System.out.println("输出: " + result2);
        
        // 测试解法二
        System.out.println("\n=== 解法二测试 ===");
        List<List<Integer>> result3 = combinationSum2_2(candidates1, target1);
        System.out.println("输入: candidates = [10,1,2,7,6,1,5], target = " + target1);
        System.out.println("输出: " + result3);
    }
}