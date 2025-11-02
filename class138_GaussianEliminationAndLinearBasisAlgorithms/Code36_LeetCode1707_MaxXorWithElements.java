package class135;

import java.util.*;

/**
 * LeetCode 1707. 与数组中元素的最大异或值
 * 题目链接: https://leetcode.com/problems/maximum-xor-with-an-element-from-array/
 * 
 * 题目描述:
 * 给你一个由非负整数组成的数组 nums 。另有一个查询数组 queries ，其中 queries[i] = [xi, mi] 。
 * 第 i 个查询的答案是 xi 和任何 nums 数组中不超过 mi 的元素按位异或（XOR）得到的最大值。
 * 换句话说，答案是 max(nums[j] XOR xi) ，其中所有 j 均满足 nums[j] <= mi 。
 * 如果 nums 中的元素都大于 mi，最终答案为 -1 。
 * 
 * 解题思路:
 * 1. 使用离线查询 + 线性基（高斯消元法）
 * 2. 将查询按照 mi 排序，同时将 nums 排序
 * 3. 逐步构建线性基，对于每个查询，使用当前可用的数字构建线性基
 * 4. 在线性基中查询与 xi 异或的最大值
 * 
 * 时间复杂度: O((n + q) * log(max_value))
 * 空间复杂度: O(n + q)
 * 
 * 工程化考虑:
 * 1. 使用离线查询优化，避免重复构建线性基
 * 2. 处理边界情况：当没有满足条件的元素时返回-1
 * 3. 使用高效的线性基实现
 */

public class Code36_LeetCode1707_MaxXorWithElements {
    
    /**
     * 线性基类
     */
    static class LinearBasis {
        private static final int MAX_BIT = 31; // 32位整数
        private int[] basis;
        
        public LinearBasis() {
            basis = new int[MAX_BIT + 1];
        }
        
        /**
         * 插入一个数字到线性基中
         * @param x 要插入的数字
         */
        public void insert(int x) {
            for (int i = MAX_BIT; i >= 0; i--) {
                if (((x >> i) & 1) == 0) continue;
                
                if (basis[i] == 0) {
                    basis[i] = x;
                    return;
                }
                x ^= basis[i];
            }
        }
        
        /**
         * 查询与x异或的最大值
         * @param x 查询值
         * @return 最大异或值
         */
        public int queryMaxXor(int x) {
            int res = x;
            for (int i = MAX_BIT; i >= 0; i--) {
                if (((res >> i) & 1) == 0 && basis[i] != 0) {
                    res ^= basis[i];
                }
            }
            return res;
        }
        
        /**
         * 清空线性基
         */
        public void clear() {
            Arrays.fill(basis, 0);
        }
    }
    
    /**
     * 主解法：离线查询 + 线性基
     * @param nums 数字数组
     * @param queries 查询数组，每个查询为[xi, mi]
     * @return 每个查询的答案
     */
    public int[] maximizeXor(int[] nums, int[][] queries) {
        int n = nums.length;
        int q = queries.length;
        
        // 对nums排序
        Arrays.sort(nums);
        
        // 创建查询索引数组，用于离线处理
        Integer[] indices = new Integer[q];
        for (int i = 0; i < q; i++) {
            indices[i] = i;
        }
        
        // 按照mi对查询排序
        Arrays.sort(indices, (i, j) -> Integer.compare(queries[i][1], queries[j][1]));
        
        int[] result = new int[q];
        LinearBasis lb = new LinearBasis();
        int idx = 0; // nums数组的指针
        
        // 离线处理每个查询
        for (int i : indices) {
            int xi = queries[i][0];
            int mi = queries[i][1];
            
            // 将满足 nums[j] <= mi 的数字加入线性基
            while (idx < n && nums[idx] <= mi) {
                lb.insert(nums[idx]);
                idx++;
            }
            
            // 如果没有满足条件的数字，返回-1
            if (idx == 0) {
                result[i] = -1;
            } else {
                result[i] = lb.queryMaxXor(xi);
            }
        }
        
        return result;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code36_LeetCode1707_MaxXorWithElements solution = new Code36_LeetCode1707_MaxXorWithElements();
        
        // 测试用例1
        int[] nums1 = {0, 1, 2, 3, 4};
        int[][] queries1 = {
            {3, 1}, {1, 3}, {5, 6}
        };
        int[] result1 = solution.maximizeXor(nums1, queries1);
        System.out.println("测试用例1: " + Arrays.toString(result1));
        
        // 测试用例2
        int[] nums2 = {5, 2, 4, 6, 6, 3};
        int[][] queries2 = {
            {12, 4}, {8, 1}, {6, 3}
        };
        int[] result2 = solution.maximizeXor(nums2, queries2);
        System.out.println("测试用例2: " + Arrays.toString(result2));
        
        // 边界测试：空数组
        int[] nums3 = {};
        int[][] queries3 = {{1, 1}};
        int[] result3 = solution.maximizeXor(nums3, queries3);
        System.out.println("边界测试（空数组）: " + Arrays.toString(result3));
        
        // 性能测试：大规模数据
        int size = 10000;
        int[] nums4 = new int[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            nums4[i] = rand.nextInt(1000000);
        }
        
        int[][] queries4 = new int[1000][2];
        for (int i = 0; i < 1000; i++) {
            queries4[i][0] = rand.nextInt(1000000);
            queries4[i][1] = rand.nextInt(1000000);
        }
        
        long startTime = System.currentTimeMillis();
        int[] result4 = solution.maximizeXor(nums4, queries4);
        long endTime = System.currentTimeMillis();
        System.out.println("性能测试 - 数据规模: " + size + ", 查询数: 1000, 耗时: " + (endTime - startTime) + "ms");
        
        // 验证算法正确性
        System.out.println("\n=== 算法正确性验证 ===");
        int[] nums5 = {3, 10, 5, 25, 2, 8};
        int[][] queries5 = {{3, 5}, {25, 30}, {0, 1}};
        int[] expected5 = {7, 28, -1};
        int[] result5 = solution.maximizeXor(nums5, queries5);
        
        boolean correct = Arrays.equals(result5, expected5);
        System.out.println("测试用例5 - 期望: " + Arrays.toString(expected5) + ", 实际: " + Arrays.toString(result5));
        System.out.println("正确性验证: " + (correct ? "通过" : "失败"));
    }
    
    /**
     * 复杂度分析:
     * 1. 时间复杂度:
     *    - 排序nums: O(n log n)
     *    - 排序查询: O(q log q)
     *    - 构建线性基: O(n * 32)
     *    - 查询: O(q * 32)
     *    - 总时间复杂度: O((n + q) * log(max_value))
     * 
     * 2. 空间复杂度:
     *    - 线性基: O(32)
     *    - 排序索引: O(q)
     *    - 总空间复杂度: O(n + q)
     * 
     * 3. 算法优势:
     *    - 离线查询避免重复计算
     *    - 线性基高效处理异或最大值
     *    - 适合大规模数据查询
     * 
     * 4. 工程化建议:
     *    - 对于小规模数据，可以使用暴力解法
     *    - 对于大规模数据，推荐使用离线查询+线性基
     *    - 注意处理边界情况和异常输入
     */
}