package class046;

import java.util.HashMap;

/**
 * 和可被K整除的子数组 (Subarray Sums Divisible by K)
 * 
 * 题目描述:
 * 给定一个整数数组 A，返回其中元素之和可被 K 整除的（连续、非空）子数组的数目。
 * 
 * 示例:
 * 输入: A = [4,5,0,-2,-3,1], K = 5
 * 输出: 7
 * 解释: 有 7 个子数组满足其元素之和可被 K = 5 整除。
 * 
 * 输入: A = [5], K = 9
 * 输出: 0
 * 
 * 提示:
 * 1 <= A.length <= 3 * 10^4
 * -10^4 <= A[i] <= 10^4
 * 2 <= K <= 10^4
 * 
 * 题目链接: https://leetcode.com/problems/subarray-sums-divisible-by-k/
 * 
 * 解题思路:
 * 1. 利用前缀和的性质：如果两个前缀和除以K的余数相同，那么这两个位置之间的子数组和可被K整除
 * 2. 使用前缀和 + 哈希表的方法
 * 3. 遍历数组，计算前缀和并对K取余
 * 4. 使用哈希表记录每个余数出现的次数
 * 5. 对于相同的余数，任意两个位置之间的子数组都满足条件
 * 
 * 时间复杂度: O(n) - 需要遍历数组一次
 * 空间复杂度: O(min(n, K)) - 哈希表最多存储K个不同的余数或n个前缀和
 * 
 * 工程化考量:
 * 1. 边界条件处理：空数组、K=0、K=1等特殊情况
 * 2. 负数取模处理：Java中负数取模结果为负，需要转换为正数
 * 3. 哈希表选择：HashMap提供O(1)的平均查找时间
 * 4. 整数溢出：使用long避免大数溢出
 * 
 * 最优解分析:
 * 这是最优解，因为必须遍历所有元素才能统计所有子数组。
 * 哈希表方法将时间复杂度从O(n^2)优化到O(n)。
 * 
 * 算法核心:
 * 设prefix[i]为前i个元素的和，则子数组[i,j]的和为prefix[j] - prefix[i-1]。
 * 当(prefix[j] - prefix[i-1]) % K = 0时，即prefix[j] % K = prefix[i-1] % K。
 * 因此统计相同余数的前缀和对数即可。
 * 
 * 算法调试技巧:
 * 1. 打印中间过程：可以在循环中打印每个位置的前缀和和余数
 * 2. 边界测试：测试K=0、K=1、负数等情况
 * 3. 性能测试：测试大规模数据下的性能表现
 * 
 * 语言特性差异:
 * Java的负数取模需要特殊处理，而Python的负数取模结果为正。
 * 与C++相比，Java有自动内存管理，无需手动释放哈希表内存。
 */
public class Code04_SubarraySumsDivisibleByK {

    /**
     * 计算和可被K整除的子数组数目
     * 
     * @param A 输入数组
     * @param K 除数
     * @return 和可被K整除的子数组数目
     * 
     * 异常场景处理:
     * - 空数组：返回0
     * - K=0：返回0（除数不能为0）
     * - K=1：所有子数组都满足条件
     * - 数组元素包含负数：算法本身支持
     * 
     * 边界条件:
     * - K=0的情况
     * - K=1的情况（所有子数组都满足）
     * - 数组元素全为0
     */
    public static int subarraysDivByK(int[] A, int K) {
        // 边界情况处理
        if (A == null || A.length == 0 || K == 0) {
            return 0;
        }
        
        // 如果K=1，所有子数组都满足条件
        if (K == 1) {
            int n = A.length;
            return n * (n + 1) / 2;
        }
        
        // 使用HashMap记录每个余数出现的次数
        HashMap<Integer, Integer> map = new HashMap<>();
        // 初始化：余数为0出现1次（表示空前缀）
        map.put(0, 1);
        
        int count = 0;          // 结果计数
        long prefixSum = 0;     // 当前前缀和，使用long避免溢出
        
        // 遍历数组
        for (int num : A) {
            // 更新前缀和
            prefixSum += num;
            
            // 计算前缀和对K的余数（处理负数情况）
            // Java中负数取模结果为负，需要转换为[0, K-1]范围内的正数
            int remainder = (int) (prefixSum % K);
            if (remainder < 0) {
                remainder += K;
            }
            
            // 调试打印：显示中间过程
            // System.out.println("前缀和 = " + prefixSum + ", 余数 = " + remainder);
            
            // 如果该余数之前出现过，说明存在满足条件的子数组
            if (map.containsKey(remainder)) {
                count += map.get(remainder);
                // 调试打印：找到符合条件的子数组
                // System.out.println("找到子数组，当前计数: " + count);
            }
            
            // 更新该余数的出现次数
            map.put(remainder, map.getOrDefault(remainder, 0) + 1);
            
            // 调试打印：哈希表状态
            // System.out.println("哈希表更新: " + remainder + " -> " + map.get(remainder));
        }
        
        return count;
    }

    /**
     * 单元测试方法
     */
    public static void testSubarraysDivByK() {
        System.out.println("=== 和可被K整除的子数组单元测试 ===");
        
        // 测试用例1：经典情况
        int[] A1 = {4, 5, 0, -2, -3, 1};
        int K1 = 5;
        int result1 = subarraysDivByK(A1, K1);
        System.out.println("测试用例1 [4,5,0,-2,-3,1] K=5: " + result1 + " (预期: 7)");
        
        // 测试用例2：单个元素
        int[] A2 = {5};
        int K2 = 9;
        int result2 = subarraysDivByK(A2, K2);
        System.out.println("测试用例2 [5] K=9: " + result2 + " (预期: 0)");
        
        // 测试用例3：K=1的情况
        int[] A3 = {1, 2, 3};
        int K3 = 1;
        int result3 = subarraysDivByK(A3, K3);
        System.out.println("测试用例3 [1,2,3] K=1: " + result3 + " (预期: 6)");
        
        // 测试用例4：空数组
        int[] A4 = {};
        int K4 = 5;
        int result4 = subarraysDivByK(A4, K4);
        System.out.println("测试用例4 [] K=5: " + result4 + " (预期: 0)");
        
        // 测试用例5：K=0的情况
        int[] A5 = {1, 2, 3};
        int K5 = 0;
        int result5 = subarraysDivByK(A5, K5);
        System.out.println("测试用例5 [1,2,3] K=0: " + result5 + " (预期: 0)");
        
        // 测试用例6：全0数组
        int[] A6 = {0, 0, 0};
        int K6 = 2;
        int result6 = subarraysDivByK(A6, K6);
        System.out.println("测试用例6 [0,0,0] K=2: " + result6 + " (预期: 6)");
        
        // 测试用例7：包含负数
        int[] A7 = {-1, 2, 9};
        int K7 = 2;
        int result7 = subarraysDivByK(A7, K7);
        System.out.println("测试用例7 [-1,2,9] K=2: " + result7 + " (预期: 2)");
    }

    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        int size = 30000; // 3万元素（题目上限）
        int[] largeArray = new int[size];
        int K = 1000;     // K的上限
        
        // 初始化大数组
        for (int i = 0; i < size; i++) {
            largeArray[i] = (i % 200) - 100; // 包含正负数
        }
        
        long startTime = System.currentTimeMillis();
        int result = subarraysDivByK(largeArray, K);
        long endTime = System.currentTimeMillis();
        
        System.out.println("处理 " + size + " 个元素，结果: " + result + ", 耗时: " + (endTime - startTime) + "ms");
    }

    /**
     * 主函数 - 测试入口
     */
    public static void main(String[] args) {
        // 运行单元测试
        testSubarraysDivByK();
        
        // 运行性能测试（可选）
        // performanceTest();
        
        System.out.println("\n=== 测试完成 ===");
    }
}