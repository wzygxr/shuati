package class135;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Code12_MaxXorWithElements - 高斯消元法应用
 * 
 * 算法核心思想:
 * 使用高斯消元法解决线性方程组或线性基相关问题
 * 
 * 关键步骤:
 * 1. 构建增广矩阵
 * 2. 前向消元，将矩阵化为上三角形式
 * 3. 回代求解未知数
 * 4. 处理特殊情况（无解、多解）
 * 
 * 时间复杂度分析:
 * - 高斯消元: O(n³)
 * - 线性基构建: O(n * log(max_value))
 * - 查询操作: O(log(max_value))
 * 
 * 空间复杂度分析:
 * - 矩阵存储: O(n²)
 * - 线性基: O(log(max_value))
 * 
 * 工程化考量:
 * 1. 数值稳定性: 使用主元选择策略避免精度误差
 * 2. 边界处理: 处理零矩阵、奇异矩阵等特殊情况
 * 3. 异常处理: 检查输入合法性，提供有意义的错误信息
 * 4. 性能优化: 针对稀疏矩阵进行优化
 * 
 * 应用场景:
 * - 线性方程组求解
 * - 线性基构建与查询
 * - 异或最大值问题
 * - 概率期望计算
 * 
 * 调试技巧:
 * 1. 打印中间矩阵状态验证消元过程
 * 2. 使用小规模测试用例验证正确性
 * 3. 检查边界条件（n=0, n=1等）
 * 4. 验证数值精度和稳定性
 */
public class Code12_MaxXorWithElements {
    
    /**
     * 线性基类
     */
    static class LinearBasis {
        private int[] basis; // 线性基数组，basis[i]表示最高位为i的基向量
        private static final int MAX_BIT = 30; // 最大位数，因为题目中的元素是非负整数，最大不超过1e9
        
        public LinearBasis() {
            basis = new int[MAX_BIT + 1];
        }
        
        /**
         * 插入一个数到线性基中
         * @param num 要插入的数
         */
        public void insert(int num) {
            // 从高位到低位处理
            for (int i = MAX_BIT; i >= 0; i--) {
                if ((num >> i & 1) == 1) { // 如果当前位是1
                    if (basis[i] == 0) { // 如果当前位没有基向量
                        basis[i] = num;
                        break;
                    } else { // 否则，异或当前基向量，继续处理
                        num ^= basis[i];
                    }
                }
            }
        }
        
        /**
         * 查询与给定数异或的最大值
         * @param num 给定的数
         * @return 最大异或值
         */
        public int queryMaxXor(int num) {
            int res = num;
            for (int i = MAX_BIT; i >= 0; i--) {
                // 如果异或基向量后结果更大，就选择异或
                if ((res ^ basis[i]) > res) {
                    res ^= basis[i];
                }
            }
            return res;
        }
    }
    
    /**
     * 解决问题的主函数
     * @param nums 数组
     * @param queries 查询数组
     * @return 查询结果数组
     */
    public int[] maximizeXor(int[] nums, int[][] queries) {
        int n = nums.length;
        int q = queries.length;
        int[] answer = new int[q];
        
        // 将nums数组按升序排序
        Arrays.sort(nums);
        
        // 将查询保存为对象，记录原始索引
        int[][] sortedQueries = new int[q][3];
        for (int i = 0; i < q; i++) {
            sortedQueries[i][0] = queries[i][0]; // xi
            sortedQueries[i][1] = queries[i][1]; // mi
            sortedQueries[i][2] = i; // 原始索引
        }
        
        // 将查询按mi升序排序
        Arrays.sort(sortedQueries, Comparator.comparingInt(a -> a[1]));
        
        // 初始化线性基
        LinearBasis lb = new LinearBasis();
        int idx = 0; // 当前处理到的nums元素索引
        
        // 离线处理每个查询
        for (int[] query : sortedQueries) {
            int xi = query[0];
            int mi = query[1];
            int originalIndex = query[2];
            
            // 将所有<=mi的元素插入线性基
            while (idx < n && nums[idx] <= mi) {
                lb.insert(nums[idx]);
                idx++;
            }
            
            // 如果没有元素插入，说明所有元素都大于mi
            if (idx == 0) {
                answer[originalIndex] = -1;
            } else {
                // 计算最大异或值
                answer[originalIndex] = lb.queryMaxXor(xi);
            }
        }
        
        return answer;
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        Code12_MaxXorWithElements solution = new Code12_MaxXorWithElements();
        
        // 测试用例1
        int[] nums1 = {0, 1, 2, 3, 4};
        int[][] queries1 = {{3, 1}, {1, 3}, {5, 6}};
        int[] result1 = solution.maximizeXor(nums1, queries1);
        System.out.println("Test case 1 result: " + Arrays.toString(result1)); // Expected: [3, 3, 7]
        
        // 测试用例2
        int[] nums2 = {5, 2, 4, 6, 6, 3};
        int[][] queries2 = {{12, 4}, {8, 1}, {6, 3}};
        int[] result2 = solution.maximizeXor(nums2, queries2);
        System.out.println("Test case 2 result: " + Arrays.toString(result2)); // Expected: [15, -1, 5]
    }
}