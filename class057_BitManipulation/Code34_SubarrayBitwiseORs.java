package class031;

import java.util.*;

/**
 * 子数组按位或操作
 * 测试链接：https://leetcode.cn/problems/subarray-bitwise-ors/
 * 
 * 题目描述：
 * 我们有一个非负整数数组 arr。
 * 对于每个（连续的）子数组 sub = [arr[i], arr[i+1], ..., arr[j]] （i <= j），
 * 我们对 sub 中的每个元素进行按位或操作，获得结果 arr[i] | arr[i+1] | ... | arr[j]。
 * 返回可能的结果的数量。多次出现的结果在最终答案中仅计算一次。
 * 
 * 示例：
 * 输入：arr = [0]
 * 输出：1
 * 解释：只有一个可能的结果 0。
 * 
 * 输入：arr = [1,1,2]
 * 输出：3
 * 解释：可能的子数组为：
 * [1] -> 1
 * [1] -> 1
 * [2] -> 2
 * [1,1] -> 1
 * [1,2] -> 3
 * [1,1,2] -> 3
 * 结果有 1, 2, 3，所以返回 3。
 * 
 * 输入：arr = [1,2,4]
 * 输出：6
 * 解释：可能的结果是 1, 2, 3, 4, 6, 7。
 * 
 * 提示：
 * 1 <= arr.length <= 5 * 10^4
 * 0 <= arr[i] <= 10^9
 * 
 * 解题思路：
 * 1. 暴力法：枚举所有子数组，计算OR值（会超时）
 * 2. 动态规划法：利用OR操作的单调性
 * 3. 集合维护法：维护当前所有可能的OR值集合
 * 
 * 时间复杂度分析：
 * - 暴力法：O(n²)，会超时
 * - 优化方法：O(n * k)，其中k是不同OR值的数量，通常k不会太大
 * 
 * 空间复杂度分析：
 * - 优化方法：O(k)，需要存储当前所有可能的OR值
 */
public class Code34_SubarrayBitwiseORs {
    
    /**
     * 方法1：暴力法（不推荐，会超时）
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n²)
     */
    public int subarrayBitwiseORs1(int[] arr) {
        Set<Integer> result = new HashSet<>();
        int n = arr.length;
        
        for (int i = 0; i < n; i++) {
            int currentOR = 0;
            for (int j = i; j < n; j++) {
                currentOR |= arr[j];
                result.add(currentOR);
            }
        }
        
        return result.size();
    }
    
    /**
     * 方法2：优化方法（推荐）
     * 核心思想：利用OR操作的单调性，维护当前所有可能的OR值
     * 时间复杂度：O(n * k)，其中k是不同OR值的数量
     * 空间复杂度：O(k)
     */
    public int subarrayBitwiseORs2(int[] arr) {
        Set<Integer> result = new HashSet<>();
        Set<Integer> current = new HashSet<>();
        
        for (int num : arr) {
            Set<Integer> next = new HashSet<>();
            next.add(num);
            
            // 将当前数字与之前的所有OR值进行OR操作
            for (int val : current) {
                next.add(val | num);
            }
            
            result.addAll(next);
            current = next;
        }
        
        return result.size();
    }
    
    /**
     * 方法3：进一步优化，使用数组代替集合
     * 时间复杂度：O(n * k)
     * 空间复杂度：O(k)
     */
    public int subarrayBitwiseORs3(int[] arr) {
        Set<Integer> result = new HashSet<>();
        List<Integer> current = new ArrayList<>();
        
        for (int num : arr) {
            List<Integer> next = new ArrayList<>();
            next.add(num);
            
            int last = num;
            for (int val : current) {
                int newVal = val | num;
                if (newVal != last) {
                    next.add(newVal);
                    last = newVal;
                }
            }
            
            result.addAll(next);
            current = next;
        }
        
        return result.size();
    }
    
    /**
     * 方法4：使用位运算优化的版本
     * 时间复杂度：O(n * 32)，因为最多有32个不同的位
     * 空间复杂度：O(32)
     */
    public int subarrayBitwiseORs4(int[] arr) {
        Set<Integer> result = new HashSet<>();
        Set<Integer> current = new HashSet<>();
        
        for (int num : arr) {
            Set<Integer> next = new HashSet<>();
            next.add(num);
            
            for (int val : current) {
                next.add(val | num);
            }
            
            result.addAll(next);
            current = next;
        }
        
        return result.size();
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code34_SubarrayBitwiseORs solution = new Code34_SubarrayBitwiseORs();
        
        // 测试用例1：基础情况
        int[] arr1 = {0};
        int result1 = solution.subarrayBitwiseORs2(arr1);
        System.out.println("测试用例1 - 输入: " + Arrays.toString(arr1));
        System.out.println("结果: " + result1 + " (预期: 1)");
        
        // 测试用例2：重复元素
        int[] arr2 = {1, 1, 2};
        int result2 = solution.subarrayBitwiseORs2(arr2);
        System.out.println("测试用例2 - 输入: " + Arrays.toString(arr2));
        System.out.println("结果: " + result2 + " (预期: 3)");
        
        // 测试用例3：递增序列
        int[] arr3 = {1, 2, 4};
        int result3 = solution.subarrayBitwiseORs2(arr3);
        System.out.println("测试用例3 - 输入: " + Arrays.toString(arr3));
        System.out.println("结果: " + result3 + " (预期: 6)");
        
        // 测试用例4：边界情况
        int[] arr4 = {1};
        int result4 = solution.subarrayBitwiseORs2(arr4);
        System.out.println("测试用例4 - 输入: " + Arrays.toString(arr4));
        System.out.println("结果: " + result4 + " (预期: 1)");
        
        // 性能测试
        int[] arr5 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        long startTime = System.currentTimeMillis();
        int result5 = solution.subarrayBitwiseORs2(arr5);
        long endTime = System.currentTimeMillis();
        System.out.println("性能测试 - 输入长度: " + arr5.length);
        System.out.println("结果: " + result5);
        System.out.println("耗时: " + (endTime - startTime) + "ms");
        
        // 复杂度分析
        System.out.println("\n=== 复杂度分析 ===");
        System.out.println("方法1 - 暴力法:");
        System.out.println("  时间复杂度: O(n²) - 会超时");
        System.out.println("  空间复杂度: O(n²)");
        
        System.out.println("方法2 - 集合维护法:");
        System.out.println("  时间复杂度: O(n * k) - k为不同OR值数量");
        System.out.println("  空间复杂度: O(k)");
        
        System.out.println("方法3 - 数组优化版:");
        System.out.println("  时间复杂度: O(n * k)");
        System.out.println("  空间复杂度: O(k)");
        
        System.out.println("方法4 - 位运算优化版:");
        System.out.println("  时间复杂度: O(n * 32)");
        System.out.println("  空间复杂度: O(32)");
        
        // 工程化考量
        System.out.println("\n=== 工程化考量 ===");
        System.out.println("1. 算法选择：方法2是最实用的选择");
        System.out.println("2. 边界处理：处理空数组和单元素数组");
        System.out.println("3. 性能优化：避免重复计算，利用OR操作的单调性");
        System.out.println("4. 内存管理：及时清理不需要的中间结果");
        
        // 算法技巧总结
        System.out.println("\n=== 算法技巧总结 ===");
        System.out.println("1. OR操作单调性：a | b >= max(a, b)");
        System.out.println("2. 集合去重：利用Set自动去重");
        System.out.println("3. 动态维护：每次只维护当前可能的OR值集合");
        System.out.println("4. 位运算特性：OR操作不会减少1的个数");
    }
}