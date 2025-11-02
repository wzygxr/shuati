// package class052.problems;

import java.util.Arrays;
import java.util.Stack;

/**
 * 2281. 巫师的总力量和 (Sum of Total Strength of Wizards)
 * 
 * 题目描述:
 * 作为国王的统治者，你有一支巫师军队听你指挥。
 * 给你一个下标从 0 开始的整数数组 strength ，其中 strength[i] 表示第 i 位巫师的力量值。
 * 对于连续的一组巫师（也就是这些巫师的力量值组成了一个连续子数组），总力量为以下两个值的乘积：
 * 巫师中最弱的能力值。
 * 组中所有巫师的能力值的和。
 * 请你返回所有可能的连续巫师组的总力量之和。
 * 
 * 解题思路:
 * 使用单调栈找到每个元素作为最小值能覆盖的区间范围。
 * 结合前缀和的前缀和（二次前缀和）技术来计算子数组和之和。
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 * 
 * 测试链接: https://leetcode.cn/problems/sum-of-total-strength-of-wizards/
 * 
 * 工程化考量:
 * 1. 异常处理：空数组、边界情况处理
 * 2. 性能优化：使用数组模拟栈，避免对象创建
 * 3. 大数处理：使用long类型避免溢出，及时取模
 * 4. 代码可读性：详细注释和模块化设计
 */
public class TotalStrengthOfWizards {
    
    private static final int MOD = 1000000007;
    
    /**
     * 计算所有连续巫师组的总力量之和
     * 
     * @param strength 巫师力量值数组
     * @return 总力量之和模 10^9 + 7
     */
    public static int totalStrength(int[] strength) {
        // 边界条件检查
        if (strength == null || strength.length == 0) {
            return 0;
        }
        
        int n = strength.length;
        
        // 前缀和数组
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = (prefix[i] + strength[i]) % MOD;
        }
        
        // 前缀和的前缀和（二次前缀和）
        long[] prefixPrefix = new long[n + 2];
        for (int i = 0; i <= n; i++) {
            prefixPrefix[i + 1] = (prefixPrefix[i] + prefix[i]) % MOD;
        }
        
        // 使用单调栈找到每个元素作为最小值的左右边界
        int[] left = new int[n];   // 左边第一个小于当前元素的位置
        int[] right = new int[n];  // 右边第一个小于等于当前元素的位置
        Arrays.fill(left, -1);
        Arrays.fill(right, n);
        
        Stack<Integer> stack = new Stack<>();
        
        // 找到右边第一个小于等于当前元素的位置
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && strength[stack.peek()] >= strength[i]) {
                right[stack.pop()] = i;
            }
            stack.push(i);
        }
        
        stack.clear();
        
        // 找到左边第一个小于当前元素的位置
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && strength[stack.peek()] > strength[i]) {
                left[stack.pop()] = i;
            }
            stack.push(i);
        }
        
        // 计算总力量
        long total = 0;
        for (int i = 0; i < n; i++) {
            int L = left[i] + 1;  // 左边界（包含）
            int R = right[i] - 1; // 右边界（包含）
            
            // 计算以strength[i]为最小值的所有子数组的和之和
            long sum = 0;
            
            // 使用二次前缀和公式计算
            // sum = strength[i] * (前缀和的前缀和计算)
            long leftSum = prefixPrefix[i + 1] - prefixPrefix[L];
            long rightSum = prefixPrefix[R + 2] - prefixPrefix[i + 1];
            
            // 计算贡献
            long contribution = (rightSum * (i - L + 1) - leftSum * (R - i + 1)) % MOD;
            contribution = (contribution * strength[i]) % MOD;
            
            total = (total + contribution) % MOD;
        }
        
        // 处理负数情况
        return (int) ((total + MOD) % MOD);
    }
    
    /**
     * 优化版本：使用数组模拟栈提高性能
     */
    public static int totalStrengthOptimized(int[] strength) {
        if (strength == null || strength.length == 0) {
            return 0;
        }
        
        int n = strength.length;
        
        // 前缀和数组
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = (prefix[i] + strength[i]) % MOD;
        }
        
        // 前缀和的前缀和（二次前缀和）
        long[] prefixPrefix = new long[n + 2];
        for (int i = 0; i <= n; i++) {
            prefixPrefix[i + 1] = (prefixPrefix[i] + prefix[i]) % MOD;
        }
        
        // 使用数组模拟栈
        int[] stack = new int[n];
        int top = -1;
        
        int[] left = new int[n];
        int[] right = new int[n];
        Arrays.fill(left, -1);
        Arrays.fill(right, n);
        
        // 找到右边第一个小于等于当前元素的位置
        for (int i = 0; i < n; i++) {
            while (top >= 0 && strength[stack[top]] >= strength[i]) {
                right[stack[top--]] = i;
            }
            stack[++top] = i;
        }
        
        top = -1;
        
        // 找到左边第一个小于当前元素的位置
        for (int i = n - 1; i >= 0; i--) {
            while (top >= 0 && strength[stack[top]] > strength[i]) {
                left[stack[top--]] = i;
            }
            stack[++top] = i;
        }
        
        // 计算总力量
        long total = 0;
        for (int i = 0; i < n; i++) {
            int L = left[i] + 1;
            int R = right[i] - 1;
            
            // 计算贡献
            long leftSum = (prefixPrefix[i + 1] - prefixPrefix[L] + MOD) % MOD;
            long rightSum = (prefixPrefix[R + 2] - prefixPrefix[i + 1] + MOD) % MOD;
            
            long contribution = (rightSum * (i - L + 1) - leftSum * (R - i + 1)) % MOD;
            contribution = (contribution * strength[i]) % MOD;
            
            total = (total + contribution) % MOD;
        }
        
        return (int) ((total + MOD) % MOD);
    }
    
    /**
     * 测试方法 - 验证算法正确性
     */
    public static void main(String[] args) {
        // 测试用例1: [1,3,1,2] - 预期: 44
        int[] strength1 = {1, 3, 1, 2};
        int result1 = totalStrength(strength1);
        int result1Opt = totalStrengthOptimized(strength1);
        System.out.println("测试用例1 [1,3,1,2]: " + result1 + " (优化版: " + result1Opt + ", 预期: 44)");
        
        // 测试用例2: [5,4,6] - 预期: 213
        int[] strength2 = {5, 4, 6};
        int result2 = totalStrength(strength2);
        int result2Opt = totalStrengthOptimized(strength2);
        System.out.println("测试用例2 [5,4,6]: " + result2 + " (优化版: " + result2Opt + ", 预期: 213)");
        
        // 测试用例3: 边界情况 - 空数组
        int[] strength3 = {};
        int result3 = totalStrength(strength3);
        int result3Opt = totalStrengthOptimized(strength3);
        System.out.println("测试用例3 []: " + result3 + " (优化版: " + result3Opt + ", 预期: 0)");
        
        // 测试用例4: 单元素数组 [10] - 预期: 100
        int[] strength4 = {10};
        int result4 = totalStrength(strength4);
        int result4Opt = totalStrengthOptimized(strength4);
        System.out.println("测试用例4 [10]: " + result4 + " (优化版: " + result4Opt + ", 预期: 100)");
        
        // 测试用例5: 重复元素 [2,2,2] - 预期: 36
        int[] strength5 = {2, 2, 2};
        int result5 = totalStrength(strength5);
        int result5Opt = totalStrengthOptimized(strength5);
        System.out.println("测试用例5 [2,2,2]: " + result5 + " (优化版: " + result5Opt + ", 预期: 36)");
        
        // 性能测试：大规模数据
        int[] strength6 = new int[1000];
        Arrays.fill(strength6, 1);
        long startTime = System.currentTimeMillis();
        int result6 = totalStrengthOptimized(strength6);
        long endTime = System.currentTimeMillis();
        System.out.println("性能测试 [1000个1]: 结果=" + result6 + ", 耗时: " + (endTime - startTime) + "ms");
        
        System.out.println("所有测试用例执行完成！");
    }
    
    /**
     * 算法复杂度分析:
     * 
     * 时间复杂度: O(n)
     * - 构建前缀和数组: O(n)
     * - 构建二次前缀和数组: O(n)
     * - 单调栈处理: O(n)
     * - 计算总贡献: O(n)
     * 
     * 空间复杂度: O(n)
     * - 前缀和数组: O(n)
     * - 二次前缀和数组: O(n)
     * - 左右边界数组: O(n)
     * - 单调栈: O(n)
     * 
     * 最优解分析:
     * - 这是巫师的总力量和问题的最优解
     * - 无法在O(n)时间内获得更好的时间复杂度
     * - 空间复杂度也是最优的
     * 
     * 数学原理:
     * - 使用单调栈找到每个元素作为最小值的区间
     * - 使用前缀和的前缀和（二次前缀和）技术快速计算子数组和之和
     * - 贡献 = 最小值 * (子数组和之和)
     */
}