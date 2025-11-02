package class047;

import java.util.*;

/**
 * AcWing 797. 差分
 * 
 * 题目描述:
 * 输入一个长度为 n 的整数序列。
 * 接下来输入 m 个操作，每个操作包含三个整数 l, r, c，表示将序列中 [l, r] 之间的每个数加上 c。
 * 请你输出进行完所有操作后的序列。
 * 
 * 示例:
 * 输入:
 * 6 3
 * 1 2 2 1 2 1
 * 1 3 1
 * 3 5 1
 * 1 6 1
 * 
 * 输出:
 * 3 4 5 3 4 2
 * 
 * 题目链接: https://www.acwing.com/problem/content/799/
 * 
 * 解题思路:
 * 使用差分数组技巧来处理区间更新操作。
 * 1. 根据原数组构造差分数组
 * 2. 对于每个操作[l, r, c]，在差分数组中执行b[l] += c和b[r+1] -= c
 * 3. 对差分数组计算前缀和，得到更新后的原数组
 * 
 * 时间复杂度: O(n + m) - 需要遍历所有操作和数组一次
 * 空间复杂度: O(n) - 需要额外的差分数组空间
 * 
 * 这是最优解，因为需要处理所有操作，而且数组大小可能很大。
 */
public class Code23_AcWingDifferenceArray {

    /**
     * 执行差分数组操作
     * 
     * @param n 数组长度
     * @param m 操作数量
     * @param arr 原数组
     * @param operations 操作数组，每个操作包含[l, r, c]
     * @return 操作后的数组
     */
    public static int[] differenceArray(int n, int m, int[] arr, int[][] operations) {
        // 边界情况处理
        if (n <= 0 || arr == null || arr.length != n) {
            return new int[0];
        }
        
        // 创建差分数组
        int[] diff = new int[n + 2]; // 多分配空间处理边界
        
        // 构造差分数组: diff[i] = arr[i] - arr[i-1]
        diff[1] = arr[0];
        for (int i = 2; i <= n; i++) {
            diff[i] = arr[i - 1] - arr[i - 2];
        }
        
        // 处理每个操作
        for (int[] op : operations) {
            int l = op[0];
            int r = op[1];
            int c = op[2];
            
            // 在差分数组中标记区间更新
            diff[l] += c;
            if (r + 1 <= n) {
                diff[r + 1] -= c;
            }
        }
        
        // 通过计算差分数组的前缀和得到最终数组
        int[] result = new int[n];
        result[0] = diff[1];
        for (int i = 1; i < n; i++) {
            result[i] = result[i - 1] + diff[i + 1];
        }
        
        return result;
    }

    /**
     * 测试用例
     */
    public static void main(String[] args) {
        // 测试用例1: 题目示例
        int n1 = 6, m1 = 3;
        int[] arr1 = {1, 2, 2, 1, 2, 1};
        int[][] operations1 = {
            {1, 3, 1},
            {3, 5, 1},
            {1, 6, 1}
        };
        
        int[] result1 = differenceArray(n1, m1, arr1, operations1);
        System.out.print("测试用例1: ");
        for (int num : result1) {
            System.out.print(num + " ");
        }
        System.out.println(); // 预期输出: 3 4 5 3 4 2

        // 测试用例2: 简单情况
        int n2 = 5, m2 = 2;
        int[] arr2 = {0, 0, 0, 0, 0};
        int[][] operations2 = {
            {1, 3, 5},
            {2, 4, 3}
        };
        
        int[] result2 = differenceArray(n2, m2, arr2, operations2);
        System.out.print("测试用例2: ");
        for (int num : result2) {
            System.out.print(num + " ");
        }
        System.out.println(); // 预期输出: 5 8 8 3 0
        
        // 测试用例3: 边界情况
        int n3 = 1, m3 = 1;
        int[] arr3 = {10};
        int[][] operations3 = {
            {1, 1, 5}
        };
        
        int[] result3 = differenceArray(n3, m3, arr3, operations3);
        System.out.print("测试用例3: ");
        for (int num : result3) {
            System.out.print(num + " ");
        }
        System.out.println(); // 预期输出: 15
    }
    
    /**
     * 工程化考量:
     * 1. 异常处理: 验证输入参数的合法性
     * 2. 边界检查: 确保索引不越界
     * 3. 性能优化: 使用差分数组减少时间复杂度
     * 4. 可读性: 清晰的变量命名和注释
     */
    
    /**
     * 时间复杂度分析:
     * - 构造差分数组: O(n)
     * - 处理操作: O(m)
     * - 计算前缀和: O(n)
     * 总时间复杂度: O(n + m)
     * 
     * 空间复杂度分析:
     * - 差分数组: O(n)
     * - 结果数组: O(n)
     * 总空间复杂度: O(n)
     */
    
    /**
     * 算法调试技巧:
     * 1. 打印中间结果: 可以打印差分数组来验证逻辑
     * 2. 小规模测试: 使用简单测试用例验证算法正确性
     * 3. 边界测试: 测试n=1, m=1等边界情况
     */
    
    /**
     * 与标准差分数组的区别:
     * 1. 这里需要处理非零初始数组
     * 2. 需要先构造差分数组，再进行区间更新
     * 3. 核心思想相同，都是利用差分数组优化区间更新
     */
}