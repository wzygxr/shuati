package class038;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 77. 组合
 * 
 * 题目描述：
 * 给定两个整数 n 和 k，返回范围 [1, n] 中所有可能的 k 个数的组合。
 * 你可以按任何顺序返回答案。
 * 
 * 示例：
 * 输入：n = 4, k = 2
 * 输出：[[1,2],[1,3],[1,4],[2,3],[2,4],[3,4]]
 * 
 * 输入：n = 1, k = 1
 * 输出：[[1]]
 * 
 * 提示：
 * 1 <= n <= 20
 * 1 <= k <= n
 * 
 * 链接：https://leetcode.cn/problems/combinations/
 * 
 * 算法思路：
 * 1. 使用回溯算法生成所有可能的组合
 * 2. 从1开始，每次选择一个数字，然后递归选择下一个数字
 * 3. 当组合长度达到k时，将其加入结果集
 * 4. 通过控制起始位置避免重复组合
 * 
 * 时间复杂度：O(C(n, k) * k)，其中C(n, k)是组合数，每个组合需要O(k)时间复制
 * 空间复杂度：O(k)，递归栈深度和存储路径的空间
 */
public class Code19_Combinations {

    /**
     * 生成从1到n中所有k个数的组合
     * 
     * @param n 范围上限
     * @param k 组合大小
     * @return 所有可能的组合
     */
    public static List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(n, k, 1, new ArrayList<>(), result);
        return result;
    }

    /**
     * 回溯函数生成组合
     * 
     * @param n 范围上限
     * @param k 组合大小
     * @param start 当前起始数字
     * @param path 当前路径
     * @param result 结果列表
     */
    private static void backtrack(int n, int k, int start, List<Integer> path, List<List<Integer>> result) {
        // 终止条件：组合长度达到k
        if (path.size() == k) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        // 剪枝优化：如果剩余的数字不足以填满组合，提前终止
        // 还需要选择的数字个数：k - path.size()
        // 从start到n至少要有这么多个数字：n - start + 1 >= k - path.size()
        // 所以 start <= n - (k - path.size()) + 1
        for (int i = start; i <= n - (k - path.size()) + 1; i++) {
            path.add(i);  // 选择当前数字
            backtrack(n, k, i + 1, path, result);  // 递归选择下一个数字
            path.remove(path.size() - 1);  // 撤销选择
        }
    }

    /**
     * 解法二：使用迭代法生成组合
     * 使用字典序组合生成算法
     * 
     * @param n 范围上限
     * @param k 组合大小
     * @return 所有可能的组合
     */
    public static List<List<Integer>> combineIterative(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        // 初始化第一个组合
        List<Integer> combination = new ArrayList<>();
        for (int i = 1; i <= k; i++) {
            combination.add(i);
        }
        
        int i = k - 1;
        while (i >= 0) {
            // 添加当前组合
            result.add(new ArrayList<>(combination));
            
            // 寻找下一个组合
            if (combination.get(i) < n - (k - 1 - i)) {
                combination.set(i, combination.get(i) + 1);
                for (int j = i + 1; j < k; j++) {
                    combination.set(j, combination.get(j - 1) + 1);
                }
            } else {
                i--;
            }
        }
        
        return result;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 4, k1 = 2;
        List<List<Integer>> result1 = combine(n1, k1);
        System.out.println("输入: n = " + n1 + ", k = " + k1);
        System.out.println("输出: " + result1);
        
        // 测试用例2
        int n2 = 1, k2 = 1;
        List<List<Integer>> result2 = combine(n2, k2);
        System.out.println("\n输入: n = " + n2 + ", k = " + k2);
        System.out.println("输出: " + result2);
        
        // 测试用例3
        int n3 = 5, k3 = 3;
        List<List<Integer>> result3 = combine(n3, k3);
        System.out.println("\n输入: n = " + n3 + ", k = " + k3);
        System.out.println("输出: " + result3);
        
        // 测试迭代解法
        System.out.println("\n=== 迭代解法测试 ===");
        List<List<Integer>> result4 = combineIterative(n1, k1);
        System.out.println("输入: n = " + n1 + ", k = " + k1);
        System.out.println("输出: " + result4);
    }
}