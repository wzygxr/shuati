package class008_AdvancedAlgorithmsAndDataStructures.sparse_table_problems;

import java.util.*;

/**
 * LeetCode 1521. Find a Value of a Mysterious Function Closest to Target
 * 
 * 题目描述：
 * 给你一个整数数组 arr 和一个整数 target 。
 * 请你返回一个整数 value ，使得 abs(f(arr, value) - target) 最小。
 * 其中 f(arr, value) 是对 arr 进行按位与操作后所有可能子数组的结果进行按位或操作。
 * 
 * 解题思路：
 * 这个问题可以转化为：找到一个值 value，使得 arr 中所有以 value 为起点的后缀的按位与值
 * 与 target 的差的绝对值最小。
 * 
 * 由于按位与操作具有单调性，我们可以预处理所有后缀的按位与值，然后使用稀疏表来优化查询。
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n log n)
 */
public class LeetCode_1521_FindValueOfPartition {
    
    static class Solution {
        public int closestToTarget(int[] arr, int target) {
            if (arr == null || arr.length == 0) {
                return target;
            }
            
            int n = arr.length;
            
            // 预处理后缀按位与值
            int[][] andSuffix = new int[n][20];
            
            // 初始化第一列
            for (int i = 0; i < n; i++) {
                andSuffix[i][0] = arr[i];
            }
            
            // 填充其他列
            for (int j = 1; (1 << j) <= n; j++) {
                for (int i = 0; i + (1 << j) <= n; i++) {
                    andSuffix[i][j] = andSuffix[i][j - 1] & andSuffix[i + (1 << (j - 1))][j - 1];
                }
            }
            
            int minDiff = Integer.MAX_VALUE;
            
            // 对每个位置计算以该位置为起点的所有后缀的按位与值
            for (int i = 0; i < n; i++) {
                int currentAnd = -1; // -1 表示全1，即按位与的单位元
                
                for (int j = i; j < n; j++) {
                    // 计算 arr[i..j] 的按位与值
                    currentAnd = (currentAnd == -1) ? arr[j] : currentAnd & arr[j];
                    
                    // 计算与 target 的差值
                    int diff = Math.abs(currentAnd - target);
                    minDiff = Math.min(minDiff, diff);
                    
                    // 如果差值为0，可以直接返回
                    if (minDiff == 0) {
                        return 0;
                    }
                    
                    // 如果 currentAnd <= target，后续的按位与值只会更小或相等，差值只会更大
                    if (currentAnd <= target) {
                        break;
                    }
                }
            }
            
            return minDiff;
        }
        
        // 使用稀疏表优化的解法
        public int closestToTarget2(int[] arr, int target) {
            if (arr == null || arr.length == 0) {
                return target;
            }
            
            int n = arr.length;
            
            // 构建稀疏表用于按位与查询
            SparseTableAnd st = new SparseTableAnd(arr);
            
            int minDiff = Integer.MAX_VALUE;
            
            // 对每个位置计算以该位置为起点的所有后缀的按位与值
            for (int i = 0; i < n; i++) {
                for (int j = i; j < n; j++) {
                    // 使用稀疏表查询 arr[i..j] 的按位与值
                    int andValue = st.queryAnd(i, j);
                    
                    // 计算与 target 的差值
                    int diff = Math.abs(andValue - target);
                    minDiff = Math.min(minDiff, diff);
                    
                    // 如果差值为0，可以直接返回
                    if (minDiff == 0) {
                        return 0;
                    }
                    
                    // 如果 andValue <= target，后续的按位与值只会更小或相等，差值只会更大
                    if (andValue <= target) {
                        break;
                    }
                }
            }
            
            return minDiff;
        }
    }
    
    // 用于按位与查询的稀疏表实现
    static class SparseTableAnd {
        private int[][] st;      // 稀疏表数组
        private int[] logTable;  // 预计算的log值表
        private int[] data;      // 原始数据
        
        /**
         * 构造函数
         * @param data 输入数组
         */
        public SparseTableAnd(int[] data) {
            if (data == null || data.length == 0) {
                throw new IllegalArgumentException("输入数组不能为空");
            }
            
            this.data = data;
            int n = data.length;
            
            // 计算log表
            precomputeLogTable(n);
            
            // 计算稀疏表
            int k = logTable[n] + 1;
            st = new int[n][k];
            
            // 初始化第一列（区间长度为1）
            for (int i = 0; i < n; i++) {
                st[i][0] = data[i];
            }
            
            // 填充其他列
            for (int j = 1; j < k; j++) {
                for (int i = 0; i <= n - (1 << j); i++) {
                    st[i][j] = st[i][j - 1] & st[i + (1 << (j - 1))][j - 1];
                }
            }
        }
        
        /**
         * 预计算log2值表
         */
        private void precomputeLogTable(int n) {
            logTable = new int[n + 1];
            logTable[1] = 0;
            for (int i = 2; i <= n; i++) {
                logTable[i] = logTable[i / 2] + 1;
            }
        }
        
        /**
         * 区间按位与查询操作
         * 时间复杂度：O(1)
         * @param l 左边界（包含）
         * @param r 右边界（包含）
         * @return 区间内的按位与值
         */
        public int queryAnd(int l, int r) {
            if (l < 0 || r >= data.length || l > r) {
                throw new IllegalArgumentException("查询范围无效");
            }
            
            int length = r - l + 1;
            int k = logTable[length];
            
            return st[l][k] & st[r - (1 << k) + 1][k];
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例1
        int[] arr1 = {9,12,3,7,15};
        int target1 = 5;
        System.out.println("测试用例1:");
        System.out.println("数组: " + Arrays.toString(arr1));
        System.out.println("目标值: " + target1);
        System.out.println("最接近的值: " + solution.closestToTarget(arr1, target1));
        System.out.println("另一种解法结果: " + solution.closestToTarget2(arr1, target1));
        System.out.println();
        
        // 测试用例2
        int[] arr2 = {1000000,1000000,1000000};
        int target2 = 1;
        System.out.println("测试用例2:");
        System.out.println("数组: " + Arrays.toString(arr2));
        System.out.println("目标值: " + target2);
        System.out.println("最接近的值: " + solution.closestToTarget(arr2, target2));
        System.out.println("另一种解法结果: " + solution.closestToTarget2(arr2, target2));
        System.out.println();
        
        // 测试用例3
        int[] arr3 = {1,2,4,8,16};
        int target3 = 10;
        System.out.println("测试用例3:");
        System.out.println("数组: " + Arrays.toString(arr3));
        System.out.println("目标值: " + target3);
        System.out.println("最接近的值: " + solution.closestToTarget(arr3, target3));
        System.out.println("另一种解法结果: " + solution.closestToTarget2(arr3, target3));
    }
}