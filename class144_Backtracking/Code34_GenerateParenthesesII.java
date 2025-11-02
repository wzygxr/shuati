package class038;

import java.util.*;

/**
 * LeetCode 22. 括号生成 (增强版)
 * 
 * 数字 n 代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且有效的括号组合。
 * 增强版：除了生成n对括号的所有组合外，还要求计算每个组合中连续括号的最大长度。
 * 
 * 算法思路：
 * 使用回溯算法生成所有有效的括号组合，在生成过程中同时计算连续括号的最大长度。
 * 
 * 时间复杂度：O(4^n / sqrt(n))，第n个卡塔兰数
 * 空间复杂度：O(4^n / sqrt(n))
 */
public class Code34_GenerateParenthesesII {
    
    /**
     * 生成所有可能的并且有效的括号组合，并计算每个组合中连续括号的最大长度
     * @param n 括号对数
     * @return 包含括号组合和对应最大连续长度的列表
     */
    public List<Map.Entry<String, Integer>> generateParenthesisWithMaxConsecutive(int n) {
        List<Map.Entry<String, Integer>> result = new ArrayList<>();
        backtrack(n, n, "", 0, 0, result);
        return result;
    }
    
    /**
     * 回溯函数
     * @param left 剩余左括号数量
     * @param right 剩余右括号数量
     * @param current 当前生成的括号字符串
     * @param consecutive 当前连续括号长度
     * @param maxConsecutive 当前最大连续括号长度
     * @param result 结果列表
     */
    private void backtrack(int left, int right, String current, int consecutive, int maxConsecutive, List<Map.Entry<String, Integer>> result) {
        // 终止条件：所有括号都已使用完
        if (left == 0 && right == 0) {
            result.add(new AbstractMap.SimpleEntry<>(current, maxConsecutive));
            return;
        }
        
        // 剪枝：右括号不能比左括号多
        if (left > right) {
            return;
        }
        
        // 添加左括号
        if (left > 0) {
            backtrack(left - 1, right, current + "(", consecutive + 1, Math.max(maxConsecutive, consecutive + 1), result);
        }
        
        // 添加右括号
        if (right > 0) {
            backtrack(left, right - 1, current + ")", consecutive > 0 ? consecutive - 1 : 0, maxConsecutive, result);
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        Code34_GenerateParenthesesII solution = new Code34_GenerateParenthesesII();
        
        // 测试用例1
        System.out.println("n = 3:");
        List<Map.Entry<String, Integer>> result1 = solution.generateParenthesisWithMaxConsecutive(3);
        for (Map.Entry<String, Integer> entry : result1) {
            System.out.println(entry.getKey() + " -> Max consecutive: " + entry.getValue());
        }
        
        // 测试用例2
        System.out.println("\nn = 2:");
        List<Map.Entry<String, Integer>> result2 = solution.generateParenthesisWithMaxConsecutive(2);
        for (Map.Entry<String, Integer> entry : result2) {
            System.out.println(entry.getKey() + " -> Max consecutive: " + entry.getValue());
        }
    }
}