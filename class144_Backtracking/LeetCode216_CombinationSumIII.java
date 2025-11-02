/**
 * LeetCode 216. 组合总和 III
 * 
 * 题目描述：
 * 找出所有相加之和为 n 的 k 个数的组合。
 * 只能使用数字1到9，每个数字最多使用一次。
 * 返回所有可能的有效组合的列表。该列表不能包含相同的组合两次。
 * 
 * 示例：
 * 输入: k = 3, n = 7
 * 输出: [[1,2,4]]
 * 解释: 1 + 2 + 4 = 7，没有其他符合的组合了。
 * 
 * 输入: k = 3, n = 9
 * 输出: [[1,2,6], [1,3,5], [2,3,4]]
 * 
 * 输入: k = 4, n = 1
 * 输出: []
 * 解释: 不存在有效的组合。在[1,9]范围内使用4个不同的数字，我们可以得到的最小和是1+2+3+4 = 10，因为10 > 1，没有有效的组合。
 * 
 * 提示：
 * 2 <= k <= 9
 * 1 <= n <= 60
 * 
 * 链接：https://leetcode.cn/problems/combination-sum-iii/
 * 
 * 算法思路：
 * 1. 使用回溯算法生成所有可能的组合
 * 2. 从数字1开始，依次尝试每个数字
 * 3. 对于每个数字，有两种选择：选择或不选择
 * 4. 通过递归和回溯生成所有满足条件的组合
 * 5. 剪枝优化：提前终止不可能产生有效解的分支
 * 
 * 剪枝策略：
 * 1. 可行性剪枝：当当前和超过目标值时剪枝
 * 2. 最优性剪枝：当已选择的数字个数超过k时剪枝
 * 3. 边界剪枝：当剩余可选数字不足时剪枝
 * 4. 范围剪枝：只在1-9范围内选择数字
 * 
 * 时间复杂度：O(C(9,k))，其中C(9,k)是组合数
 * 空间复杂度：O(k)，递归栈深度和存储路径的空间
 * 
 * 工程化考量：
 * 1. 边界处理：处理k=0或n=0的特殊情况
 * 2. 参数验证：验证输入参数的有效性
 * 3. 性能优化：通过剪枝减少不必要的计算
 * 4. 内存管理：合理使用数据结构减少内存占用
 * 5. 可读性：添加详细注释和变量命名
 * 6. 异常处理：处理可能的异常情况
 * 7. 模块化设计：将核心逻辑封装成独立方法
 * 8. 可维护性：添加详细注释和文档说明
 */
import java.util.ArrayList;
import java.util.List;

public class LeetCode216_CombinationSumIII {
    
    /**
     * 找出所有相加之和为 n 的 k 个数的组合
     * 
     * @param k 组合中数字的个数
     * @param n 目标和
     * @return 所有满足条件的组合
     */
    public List<List<Integer>> combinationSum3(int k, int n) {
        List<List<Integer>> result = new ArrayList<>();
        
        // 边界条件检查
        if (k <= 0 || n <= 0 || k > 9 || n > 45) {  // 1-9的最大和是45
            return result;
        }
        
        List<Integer> path = new ArrayList<>();
        backtrack(k, n, 1, 0, path, result);
        return result;
    }
    
    /**
     * 回溯函数生成组合
     * 
     * @param k 组合中数字的个数
     * @param n 目标和
     * @param start 当前起始数字
     * @param currentSum 当前和
     * @param path 当前路径
     * @param result 结果列表
     */
    private void backtrack(int k, int n, int start, int currentSum, List<Integer> path, List<List<Integer>> result) {
        // 终止条件：已选择k个数字
        if (path.size() == k) {
            // 检查和是否等于目标值
            if (currentSum == n) {
                result.add(new ArrayList<>(path));
            }
            return;
        }
        
        // 剪枝：如果已选择的数字个数超过k或当前和超过目标值，提前终止
        if (path.size() > k || currentSum > n) {
            return;
        }
        
        // 从start开始尝试数字1-9
        for (int i = start; i <= 9; i++) {
            // 剪枝：如果加上当前数字后和超过目标值，由于数字递增，后面的数字更大，直接跳出循环
            if (currentSum + i > n) {
                break;
            }
            
            // 剪枝：如果剩余可选数字不足，提前终止
            if (9 - i + 1 < k - path.size()) {
                break;
            }
            
            // 选择当前数字
            path.add(i);
            
            // 递归处理下一个数字
            backtrack(k, n, i + 1, currentSum + i, path, result);
            
            // 回溯：撤销选择
            path.remove(path.size() - 1);
        }
    }
    
    /**
     * 解法二：使用位运算枚举所有可能的组合
     * 
     * @param k 组合中数字的个数
     * @param n 目标和
     * @return 所有满足条件的组合
     */
    public List<List<Integer>> combinationSum3_2(int k, int n) {
        List<List<Integer>> result = new ArrayList<>();
        
        // 边界条件检查
        if (k <= 0 || n <= 0 || k > 9 || n > 45) {
            return result;
        }
        
        // 枚举所有可能的组合（1-9的子集）
        for (int mask = 0; mask < (1 << 9); mask++) {
            // 检查组合中数字的个数是否为k
            if (Integer.bitCount(mask) == k) {
                List<Integer> combination = new ArrayList<>();
                int sum = 0;
                
                // 构造组合并计算和
                for (int i = 0; i < 9; i++) {
                    if ((mask & (1 << i)) != 0) {
                        combination.add(i + 1);
                        sum += i + 1;
                    }
                }
                
                // 检查和是否等于目标值
                if (sum == n) {
                    result.add(combination);
                }
            }
        }
        
        return result;
    }
    
    // 测试方法
    public static void main(String[] args) {
        LeetCode216_CombinationSumIII solution = new LeetCode216_CombinationSumIII();
        
        // 测试用例1
        int k1 = 3, n1 = 7;
        List<List<Integer>> result1 = solution.combinationSum3(k1, n1);
        System.out.println("输入: k = " + k1 + ", n = " + n1);
        System.out.println("输出: " + result1);
        
        // 测试用例2
        int k2 = 3, n2 = 9;
        List<List<Integer>> result2 = solution.combinationSum3(k2, n2);
        System.out.println("\n输入: k = " + k2 + ", n = " + n2);
        System.out.println("输出: " + result2);
        
        // 测试用例3
        int k3 = 4, n3 = 1;
        List<List<Integer>> result3 = solution.combinationSum3(k3, n3);
        System.out.println("\n输入: k = " + k3 + ", n = " + n3);
        System.out.println("输出: " + result3);
        
        // 测试解法二
        System.out.println("\n=== 解法二测试 ===");
        List<List<Integer>> result4 = solution.combinationSum3_2(k1, n1);
        System.out.println("输入: k = " + k1 + ", n = " + n1);
        System.out.println("输出: " + result4);
    }
}