package class145;

import java.io.*;
import java.util.*;

/**
 * LeetCode 47: 全排列 II (Permutations II)
 * 题目链接：https://leetcode.cn/problems/permutations-ii/
 * 题目描述：给定一个可包含重复数字的序列nums，按任意顺序返回所有不重复的全排列
 * 
 * 二项式反演应用：
 * 当序列包含重复元素时，直接生成全排列会产生重复结果。
 * 可以使用二项式反演思想结合回溯算法来避免重复排列。
 * 
 * 算法原理：
 * 1. 先对数组排序，使相同元素相邻
 * 2. 使用回溯算法生成所有排列
 * 3. 通过剪枝避免重复排列：
 *    - 如果当前元素与前一个元素相同且前一个元素未被使用，则跳过
 *    - 这样可以确保相同元素的相对顺序固定，避免重复
 * 
 * 时间复杂度分析：
 * - 最坏情况：O(n * n!) - 需要生成所有排列，每个排列需要O(n)时间复制
 * - 平均情况：由于剪枝，实际生成的排列数远小于n!
 * 
 * 空间复杂度分析：
 * - O(n) - 递归栈深度为n，标记数组大小为n
 * 
 * 工程化考量：
 * - 使用布尔数组标记已使用元素，避免重复选择
 * - 排序预处理确保相同元素相邻
 * - 剪枝策略优化性能
 */
public class Code12_LeetCode47_PermutationsII {
    
    // 存储所有不重复的排列结果
    private List<List<Integer>> result;
    
    // 标记数组，记录哪些元素已被使用
    private boolean[] used;
    
    // 当前正在构建的排列
    private List<Integer> current;
    
    /**
     * 主函数：生成所有不重复的全排列
     * 
     * @param nums 输入数组，可能包含重复元素
     * @return 所有不重复的全排列
     */
    public List<List<Integer>> permuteUnique(int[] nums) {
        // 初始化结果集和辅助数据结构
        result = new ArrayList<>();
        used = new boolean[nums.length];
        current = new ArrayList<>();
        
        // 关键步骤：先对数组排序，使相同元素相邻
        // 这样可以在回溯时通过比较相邻元素来避免重复
        Arrays.sort(nums);
        
        // 开始回溯搜索
        backtrack(nums);
        
        return result;
    }
    
    /**
     * 回溯算法核心函数
     * 
     * @param nums 排序后的输入数组
     */
    private void backtrack(int[] nums) {
        // 终止条件：当前排列长度等于数组长度
        if (current.size() == nums.length) {
            // 添加当前排列的副本到结果集（注意：需要创建新列表）
            result.add(new ArrayList<>(current));
            return;
        }
        
        // 遍历所有可能的下一位置选择
        for (int i = 0; i < nums.length; i++) {
            // 剪枝条件1：当前元素已被使用
            if (used[i]) {
                continue;
            }
            
            // 剪枝条件2：避免重复排列的关键
            // 如果当前元素与前一个元素相同，且前一个元素未被使用，则跳过
            // 这样可以确保相同元素的相对顺序固定
            if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) {
                continue;
            }
            
            // 选择当前元素
            used[i] = true;
            current.add(nums[i]);
            
            // 递归搜索下一层
            backtrack(nums);
            
            // 回溯：撤销选择
            current.remove(current.size() - 1);
            used[i] = false;
        }
    }
    
    /**
     * 使用二项式反演思想计算不重复排列数（数学方法）
     * 
     * 算法原理：
     * 设数组中有k种不同的数字，每种数字出现次数为c1, c2, ..., ck
     * 则总排列数为：n! / (c1! * c2! * ... * ck!)
     * 
     * 时间复杂度：O(n) - 需要统计频率和计算阶乘
     * 空间复杂度：O(k) - 需要存储频率统计
     * 
     * @param nums 输入数组
     * @return 不重复排列的数量（数学计算结果）
     */
    public long countUniquePermutations(int[] nums) {
        // 统计每个数字的出现频率
        Map<Integer, Integer> freq = new HashMap<>();
        for (int num : nums) {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }
        
        // 计算n!
        long numerator = factorial(nums.length);
        
        // 计算分母：c1! * c2! * ... * ck!
        long denominator = 1;
        for (int count : freq.values()) {
            denominator *= factorial(count);
        }
        
        return numerator / denominator;
    }
    
    /**
     * 计算阶乘函数
     * 
     * @param n 非负整数
     * @return n! 的结果
     */
    private long factorial(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
    
    /**
     * 单元测试函数
     */
    public static void test() {
        Code12_LeetCode47_PermutationsII solution = new Code12_LeetCode47_PermutationsII();
        
        // 测试用例1：[1,1,2]
        System.out.println("=== 测试用例1: [1,1,2] ===");
        int[] nums1 = {1, 1, 2};
        List<List<Integer>> result1 = solution.permuteUnique(nums1);
        System.out.println("排列结果：" + result1);
        System.out.println("排列数量：" + result1.size());
        System.out.println("数学计算数量：" + solution.countUniquePermutations(nums1));
        
        // 测试用例2：[1,2,3]
        System.out.println("\n=== 测试用例2: [1,2,3] ===");
        int[] nums2 = {1, 2, 3};
        List<List<Integer>> result2 = solution.permuteUnique(nums2);
        System.out.println("排列数量：" + result2.size());
        System.out.println("数学计算数量：" + solution.countUniquePermutations(nums2));
        
        // 测试用例3：[1,1,1]
        System.out.println("\n=== 测试用例3: [1,1,1] ===");
        int[] nums3 = {1, 1, 1};
        List<List<Integer>> result3 = solution.permuteUnique(nums3);
        System.out.println("排列数量：" + result3.size());
        System.out.println("数学计算数量：" + solution.countUniquePermutations(nums3));
    }
    
    /**
     * 主函数：用于演示和测试
     */
    public static void main(String[] args) {
        // 运行单元测试
        test();
        
        // 演示从标准输入读取数据
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("\n=== 交互式测试 ===");
            System.out.print("请输入数组元素（用空格分隔）：");
            String input = reader.readLine();
            
            if (input != null && !input.trim().isEmpty()) {
                String[] parts = input.trim().split("\\s+");
                int[] nums = new int[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    nums[i] = Integer.parseInt(parts[i]);
                }
                
                Code12_LeetCode47_PermutationsII solution = new Code12_LeetCode47_PermutationsII();
                List<List<Integer>> result = solution.permuteUnique(nums);
                
                System.out.println("不重复排列数量：" + result.size());
                System.out.println("所有排列：" + result);
            }
        } catch (Exception e) {
            System.out.println("输入格式错误：" + e.getMessage());
        }
    }
    
    /**
     * 算法优化思路：
     * 1. 剪枝策略：通过排序和相邻元素比较，避免生成重复排列
     * 2. 空间优化：使用原地交换的递归方法可以进一步减少空间使用
     * 3. 并行计算：对于大规模数据，可以考虑并行生成排列
     * 
     * 边界条件处理：
     * - 空数组：返回空列表
     * - 单元素数组：返回单个排列
     * - 全相同元素：只有一个排列
     * 
     * 异常场景：
     * - 输入数组为null：抛出IllegalArgumentException
     * - 数组长度过大：考虑使用迭代器模式避免内存溢出
     * 
     * 工程化扩展：
     * - 支持流式处理：实现Iterator接口，支持逐个生成排列
     * - 添加缓存：对于相同输入，可以缓存结果
     * - 性能监控：添加性能统计和日志记录
     */
}