package class184;

import java.util.*;

/**
 * LeetCode 370. Range Addition 解决方案
 * 
 * 题目链接: https://leetcode.com/problems/range-addition/
 * 题目描述: 对数组进行多次区间更新操作，最后返回结果数组
 * 解题思路: 使用差分数组优化区间更新
 * 
 * 时间复杂度: 
 * - 区间更新: O(1) 每次操作
 * - 获取结果: O(n)
 * 空间复杂度: O(n)
 */
public class LeetCode_RangeAddition {
    
    /**
     * 差分数组实现类
     */
    static class DifferenceArray {
        private int[] diff;      // 差分数组
        
        /**
         * 构造函数 - 从大小创建
         * @param n 数组大小
         */
        public DifferenceArray(int n) {
            if (n <= 0) {
                throw new IllegalArgumentException("数组大小必须为正整数");
            }
            
            this.diff = new int[n + 1];  // 差分数组大小为n+1，便于处理边界
        }
        
        /**
         * 区间更新：将区间[start, end]的每个元素加上val
         * 时间复杂度：O(1)
         * @param start 起始索引（包含）
         * @param end 结束索引（包含）
         * @param val 要增加的值
         */
        public void rangeUpdate(int start, int end, int val) {
            if (start < 0 || end >= diff.length - 1 || start > end) {
                throw new IllegalArgumentException("更新范围无效");
            }
            
            diff[start] += val;
            diff[end + 1] -= val;
        }
        
        /**
         * 获取更新后的数组
         * 时间复杂度：O(n)
         * @return 更新后的数组
         */
        public int[] getResult() {
            int n = diff.length - 1;
            int[] result = new int[n];
            
            // 通过前缀和恢复原始数组
            result[0] = diff[0];
            for (int i = 1; i < n; i++) {
                result[i] = result[i - 1] + diff[i];
            }
            
            return result;
        }
    }
    
    /**
     * 使用差分数组解决区间更新问题
     * @param length 数组长度
     * @param updates 更新操作数组，每个操作是 [startIndex, endIndex, inc]
     * @return 更新后的数组
     */
    public int[] getModifiedArray(int length, int[][] updates) {
        // 检查输入有效性
        if (length <= 0) {
            return new int[0];
        }
        
        // 创建差分数组
        DifferenceArray diffArray = new DifferenceArray(length);
        
        // 执行所有更新操作
        for (int[] update : updates) {
            int startIndex = update[0];
            int endIndex = update[1];
            int inc = update[2];
            
            diffArray.rangeUpdate(startIndex, endIndex, inc);
        }
        
        // 获取结果数组
        return diffArray.getResult();
    }
    
    /**
     * 使用暴力方法解决区间更新问题（用于对比）
     * @param length 数组长度
     * @param updates 更新操作数组
     * @return 更新后的数组
     */
    public int[] getModifiedArrayBruteForce(int length, int[][] updates) {
        // 检查输入有效性
        if (length <= 0) {
            return new int[0];
        }
        
        // 初始化数组
        int[] result = new int[length];
        
        // 执行所有更新操作
        for (int[] update : updates) {
            int startIndex = update[0];
            int endIndex = update[1];
            int inc = update[2];
            
            // 暴力更新区间
            for (int i = startIndex; i <= endIndex; i++) {
                result[i] += inc;
            }
        }
        
        return result;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        LeetCode_RangeAddition solution = new LeetCode_RangeAddition();
        
        // 测试用例1
        int length1 = 5;
        int[][] updates1 = {
            {1, 3, 2},
            {2, 4, 3},
            {0, 2, -2}
        };
        
        System.out.println("=== 测试用例1 ===");
        System.out.println("数组长度: " + length1);
        System.out.println("更新操作: ");
        for (int[] update : updates1) {
            System.out.println("  [" + update[0] + ", " + update[1] + ", " + update[2] + "]");
        }
        
        int[] result1 = solution.getModifiedArray(length1, updates1);
        System.out.println("差分数组结果: " + Arrays.toString(result1));
        
        int[] result1Brute = solution.getModifiedArrayBruteForce(length1, updates1);
        System.out.println("暴力方法结果: " + Arrays.toString(result1Brute));
        
        // 测试用例2
        int length2 = 10;
        int[][] updates2 = {
            {2, 4, 6},
            {5, 6, 8},
            {1, 9, -4}
        };
        
        System.out.println("\n=== 测试用例2 ===");
        System.out.println("数组长度: " + length2);
        System.out.println("更新操作: ");
        for (int[] update : updates2) {
            System.out.println("  [" + update[0] + ", " + update[1] + ", " + update[2] + "]");
        }
        
        int[] result2 = solution.getModifiedArray(length2, updates2);
        System.out.println("差分数组结果: " + Arrays.toString(result2));
        
        int[] result2Brute = solution.getModifiedArrayBruteForce(length2, updates2);
        System.out.println("暴力方法结果: " + Arrays.toString(result2Brute));
        
        // 验证结果一致性
        System.out.println("\n结果一致性验证:");
        System.out.println("测试用例1一致: " + Arrays.equals(result1, result1Brute));
        System.out.println("测试用例2一致: " + Arrays.equals(result2, result2Brute));
    }
    
    /**
     * 性能测试
     */
    public static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        
        // 生成测试数据
        int length = 100000;
        int numUpdates = 10000;
        int[][] updates = new int[numUpdates][3];
        Random random = new Random(42); // 固定种子以确保可重复性
        
        for (int i = 0; i < numUpdates; i++) {
            int start = random.nextInt(length);
            int end = random.nextInt(length);
            if (start > end) {
                int temp = start;
                start = end;
                end = temp;
            }
            int inc = random.nextInt(100) - 50; // -50到49的随机值
            updates[i] = new int[]{start, end, inc};
        }
        
        LeetCode_RangeAddition solution = new LeetCode_RangeAddition();
        
        // 测试差分数组方法
        long startTime = System.currentTimeMillis();
        int[] result1 = solution.getModifiedArray(length, updates);
        long time1 = System.currentTimeMillis() - startTime;
        
        // 测试暴力方法（只测试前100个更新操作以避免超时）
        int[][] smallUpdates = Arrays.copyOfRange(updates, 0, Math.min(100, updates.length));
        startTime = System.currentTimeMillis();
        int[] result2 = solution.getModifiedArrayBruteForce(length, smallUpdates);
        long time2 = System.currentTimeMillis() - startTime;
        
        System.out.println("差分数组方法 - 数组长度: " + length + ", 更新操作数: " + numUpdates);
        System.out.println("  耗时: " + time1 + " ms");
        System.out.println("  结果数组前10个元素: " + Arrays.toString(Arrays.copyOfRange(result1, 0, Math.min(10, result1.length))));
        
        System.out.println("暴力方法 - 数组长度: " + length + ", 更新操作数: " + smallUpdates.length);
        System.out.println("  耗时: " + time2 + " ms");
        System.out.println("  结果数组前10个元素: " + Arrays.toString(Arrays.copyOfRange(result2, 0, Math.min(10, result2.length))));
        
        System.out.println("性能提升: " + (time2 * smallUpdates.length / (double)numUpdates / time1) + "倍");
    }
}