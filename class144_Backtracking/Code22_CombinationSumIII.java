package class038;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 216. 组合总和 III
 * 
 * 题目描述：
 * 找出所有相加之和为 n 的 k 个数的组合。组合中只允许含有 1 - 9 的正整数，并且每种组合中不存在重复的数字。
 * 
 * 示例：
 * 输入: k = 3, n = 7
 * 输出: [[1,2,4]]
 * 
 * 输入: k = 3, n = 9
 * 输出: [[1,2,6],[1,3,5],[2,3,4]]
 * 
 * 输入: k = 4, n = 1
 * 输出: []
 * 
 * 提示：
 * 2 <= k <= 9
 * 1 <= n <= 60
 * 
 * 链接：https://leetcode.cn/problems/combination-sum-iii/
 * 
 * 算法思路：
 * 1. 使用回溯算法生成所有可能的组合
 * 2. 从1开始，每次选择一个数字，然后递归选择下一个数字
 * 3. 当组合长度达到k且和为n时，将其加入结果集
 * 4. 通过控制起始位置避免重复组合
 * 5. 使用剪枝优化：当当前和已经超过n时提前终止
 * 
 * 时间复杂度：O(C(9, k))，从9个数字中选择k个数字的组合数
 * 空间复杂度：O(k)，递归栈深度
 */
public class Code22_CombinationSumIII {

    /**
     * 生成所有和为n的k个数的组合（数字范围1-9）
     * 
     * @param k 组合大小
     * @param n 目标和
     * @return 所有满足条件的组合
     */
    public static List<List<Integer>> combinationSum3(int k, int n) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(k, n, 1, new ArrayList<>(), result);
        return result;
    }

    /**
     * 回溯函数生成组合
     * 
     * @param k 剩余需要选择的数字个数
     * @param n 剩余目标值
     * @param start 当前起始数字
     * @param path 当前路径
     * @param result 结果列表
     */
    private static void backtrack(int k, int n, int start, List<Integer> path, List<List<Integer>> result) {
        // 终止条件：已选择k个数字
        if (k == 0) {
            // 如果和为n，加入结果集
            if (n == 0) {
                result.add(new ArrayList<>(path));
            }
            return;
        }
        
        // 剪枝优化：如果剩余的数字不足以填满组合，提前终止
        // 还需要选择的数字个数：k
        // 从start到9至少要有这么多个数字：9 - start + 1 >= k
        // 所以 start <= 9 - k + 1
        for (int i = start; i <= 9 - k + 1; i++) {
            // 剪枝：如果当前数字已经大于剩余目标值，提前终止
            if (i > n) {
                break;
            }
            
            path.add(i);
            backtrack(k - 1, n - i, i + 1, path, result);
            path.remove(path.size() - 1);
        }
    }

    /**
     * 解法二：使用迭代法生成组合
     * 使用位运算生成所有可能的组合
     * 
     * @param k 组合大小
     * @param n 目标和
     * @return 所有满足条件的组合
     */
    public static List<List<Integer>> combinationSum3_2(int k, int n) {
        List<List<Integer>> result = new ArrayList<>();
        
        // 遍历所有可能的组合（使用位掩码）
        for (int mask = 0; mask < (1 << 9); mask++) {
            List<Integer> combination = new ArrayList<>();
            int sum = 0;
            
            // 检查当前掩码对应的组合
            for (int i = 0; i < 9; i++) {
                if ((mask & (1 << i)) != 0) {
                    combination.add(i + 1);  // 数字从1开始
                    sum += i + 1;
                }
            }
            
            // 检查是否满足条件
            if (combination.size() == k && sum == n) {
                result.add(combination);
            }
        }
        
        return result;
    }

    /**
     * 解法三：使用动态规划预处理
     * 先计算所有可能的组合，然后筛选满足条件的
     * 
     * @param k 组合大小
     * @param n 目标和
     * @return 所有满足条件的组合
     */
    public static List<List<Integer>> combinationSum3_3(int k, int n) {
        // 先使用回溯生成所有k个数字的组合
        List<List<Integer>> allCombinations = new ArrayList<>();
        generateCombinations(k, 1, new ArrayList<>(), allCombinations);
        
        // 筛选和为n的组合
        List<List<Integer>> result = new ArrayList<>();
        for (List<Integer> comb : allCombinations) {
            int sum = 0;
            for (int num : comb) {
                sum += num;
            }
            if (sum == n) {
                result.add(comb);
            }
        }
        
        return result;
    }

    private static void generateCombinations(int k, int start, List<Integer> path, List<List<Integer>> result) {
        if (k == 0) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        for (int i = start; i <= 9; i++) {
            path.add(i);
            generateCombinations(k - 1, i + 1, path, result);
            path.remove(path.size() - 1);
        }
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int k1 = 3, n1 = 7;
        List<List<Integer>> result1 = combinationSum3(k1, n1);
        System.out.println("输入: k = " + k1 + ", n = " + n1);
        System.out.println("输出: " + result1);
        
        // 测试用例2
        int k2 = 3, n2 = 9;
        List<List<Integer>> result2 = combinationSum3(k2, n2);
        System.out.println("\n输入: k = " + k2 + ", n = " + n2);
        System.out.println("输出: " + result2);
        
        // 测试用例3
        int k3 = 4, n3 = 1;
        List<List<Integer>> result3 = combinationSum3(k3, n3);
        System.out.println("\n输入: k = " + k3 + ", n = " + n3);
        System.out.println("输出: " + result3);
        
        // 测试解法二
        System.out.println("\n=== 解法二测试 ===");
        List<List<Integer>> result4 = combinationSum3_2(k1, n1);
        System.out.println("输入: k = " + k1 + ", n = " + n1);
        System.out.println("输出: " + result4);
    }
}