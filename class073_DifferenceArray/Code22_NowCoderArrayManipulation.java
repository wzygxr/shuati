package class047;

import java.util.*;

/**
 * 牛客网 - 数组操作问题
 * 
 * 题目描述:
 * 给定一个长度为 n 的数组，初始值都为 0。
 * 有 m 次操作，每次操作给出三个数 l, r, k，表示将数组下标从 l 到 r 的所有元素都加上 k。
 * 求执行完所有操作后数组中的最大值。
 * 
 * 示例:
 * 输入: n = 5, operations = [[1,2,100],[2,5,100],[3,4,100]]
 * 输出: 200
 * 
 * 题目链接: 牛客网类似题目
 * 
 * 解题思路:
 * 使用差分数组技巧来处理区间更新操作。
 * 1. 创建一个差分数组diff，大小为n+1
 * 2. 对于每个操作[l, r, k]，执行diff[l-1] += k和diff[r] -= k
 * 3. 对差分数组计算前缀和，得到最终数组
 * 4. 在计算前缀和的过程中记录最大值
 * 
 * 时间复杂度: O(n + m) - 需要遍历所有操作和数组一次
 * 空间复杂度: O(n) - 需要额外的差分数组空间
 * 
 * 这是最优解，因为需要处理所有操作，而且数组大小可能很大。
 */
public class Code22_NowCoderArrayManipulation {

    /**
     * 计算数组操作后的最大值
     * 
     * @param n 数组长度
     * @param operations 操作数组，每个操作包含[起始索引, 结束索引, 增加值]
     * @return 操作后数组的最大值
     */
    public static long maxValueAfterOperations(int n, int[][] operations) {
        // 边界情况处理
        if (n <= 0 || operations == null || operations.length == 0) {
            return 0;
        }
        
        // 创建差分数组，大小为n+1以便处理边界情况
        long[] diff = new long[n + 1];
        
        // 处理每个操作
        for (int[] op : operations) {
            int l = op[0];      // 起始索引（1-based）
            int r = op[1];      // 结束索引（1-based）
            int k = op[2];      // 增加值
            
            // 在差分数组中标记区间更新
            diff[l - 1] += k;      // 在起始位置增加k
            if (r < n) {
                diff[r] -= k;      // 在结束位置之后减少k
            }
        }
        
        // 通过计算差分数组的前缀和得到最终数组，并记录最大值
        long maxVal = Long.MIN_VALUE;
        long currentSum = 0;
        
        for (int i = 0; i < n; i++) {
            currentSum += diff[i];
            if (currentSum > maxVal) {
                maxVal = currentSum;
            }
        }
        
        return maxVal;
    }

    /**
     * 测试用例
     */
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 5;
        int[][] operations1 = {{1, 2, 100}, {2, 5, 100}, {3, 4, 100}};
        long result1 = maxValueAfterOperations(n1, operations1);
        // 预期输出: 200
        System.out.println("测试用例1: " + result1);

        // 测试用例2
        int n2 = 10;
        int[][] operations2 = {{2, 6, 8}, {3, 5, 7}, {1, 8, 1}, {5, 9, 15}};
        long result2 = maxValueAfterOperations(n2, operations2);
        // 预期输出: 31
        System.out.println("测试用例2: " + result2);
        
        // 测试用例3
        int n3 = 4;
        int[][] operations3 = {{1, 2, 5}, {2, 4, 10}, {1, 3, 3}};
        long result3 = maxValueAfterOperations(n3, operations3);
        // 预期输出: 18
        System.out.println("测试用例3: " + result3);
        
        // 测试用例4 - 边界情况
        int n4 = 1;
        int[][] operations4 = {{1, 1, 100}};
        long result4 = maxValueAfterOperations(n4, operations4);
        // 预期输出: 100
        System.out.println("测试用例4: " + result4);
    }
    
    /**
     * 工程化考量:
     * 1. 异常处理: 验证输入参数的合法性
     * 2. 边界检查: 确保索引不越界
     * 3. 大数处理: 使用long类型防止整数溢出
     * 4. 性能优化: 使用差分数组减少时间复杂度
     * 5. 可读性: 清晰的变量命名和注释
     */
    
    /**
     * 时间复杂度分析:
     * - 处理操作: O(m)
     * - 计算前缀和: O(n)
     * 总时间复杂度: O(n + m)
     * 
     * 空间复杂度分析:
     * - 差分数组: O(n)
     * 总空间复杂度: O(n)
     */
    
    /**
     * 算法调试技巧:
     * 1. 打印中间结果: 可以打印差分数组和前缀和来验证逻辑
     * 2. 小规模测试: 使用简单测试用例验证算法正确性
     * 3. 边界测试: 测试n=1, m=1等边界情况
     */
    
    /**
     * 与HackerRank Array Manipulation的区别:
     * 1. 输入格式略有不同
     * 2. 核心算法思想相同
     * 3. 都是使用差分数组解决区间更新问题
     */
}