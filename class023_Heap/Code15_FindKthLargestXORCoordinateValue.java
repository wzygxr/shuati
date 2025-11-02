package class027;

import java.util.*;

/**
 * LeetCode 1738: 寻找第K大的异或坐标值
 *
 * 解题思路：
 * 1. 使用二维前缀异或和计算每个坐标的异或值
 * 2. 使用最小堆维护前K个最大的异或值
 * 3. 最终堆顶元素即为第K大的异或值
 *
 * 时间复杂度：O(m*n log k)
 * 空间复杂度：O(k)
 */
public class Code15_FindKthLargestXORCoordinateValue {

    /**
     * 寻找第K大的异或坐标值的主解决方案类
     * 使用最小堆维护前K大的元素
     */
    public static class Solution {
        /**
         * 计算二维矩阵中所有可能的子矩阵的异或和，并返回第K大的值
         *
         * @param matrix 二维整数数组
         * @param k 需要返回的第K大元素的索引
         * @return 所有可能的子矩阵异或和中第K大的值
         * @throws IllegalArgumentException 当输入参数无效时抛出
         */
        public int kthLargestValue(int[][] matrix, int k) {
            // 输入参数校验
            if (matrix == null || matrix.length == 0 || matrix[0].length == 0 || k <= 0) {
                throw new IllegalArgumentException("输入参数无效：矩阵不能为空且k必须为正整数");
            }

            int m = matrix.length;
            int n = matrix[0].length;

            // 检查k是否超过可能的子矩阵数量
            if (k > m * n) {
                throw new IllegalArgumentException("k值超过了矩阵中可能的子矩阵数量");
            }

            // 初始化前缀异或和矩阵
            int[][] pre_xor = new int[m + 1][n + 1];

            // 最小堆，用于维护前K个最大的异或值
            PriorityQueue<Integer> minHeap = new PriorityQueue<>(k);

            // 计算前缀异或和并维护最小堆
            for (int i = 1; i <= m; i++) {
                for (int j = 1; j <= n; j++) {
                    // 计算当前位置的前缀异或和
                    // pre_xor[i][j] 表示从 (0,0) 到 (i-1,j-1) 的子矩阵的异或和
                    pre_xor[i][j] = pre_xor[i-1][j-1] ^ pre_xor[i-1][j] ^ pre_xor[i][j-1] ^ matrix[i-1][j-1];

                    // 将当前异或值添加到最小堆
                    minHeap.offer(pre_xor[i][j]);

                    // 如果堆的大小超过k，弹出最小元素
                    if (minHeap.size() > k) {
                        minHeap.poll();
                    }
                }
            }

            // 堆顶元素即为第K大的异或值
            return minHeap.peek();
        }
    }

    /**
     * 寻找第K大的异或坐标值的替代解决方案类
     * 收集所有异或值后排序获取第K大的元素
     */
    public static class AlternativeSolution {
        /**
         * 计算二维矩阵中所有可能的子矩阵的异或和，并返回第K大的值
         *
         * @param matrix 二维整数数组
         * @param k 需要返回的第K大元素的索引
         * @return 所有可能的子矩阵异或和中第K大的值
         * @throws IllegalArgumentException 当输入参数无效时抛出
         */
        public int kthLargestValue(int[][] matrix, int k) {
            // 输入参数校验
            if (matrix == null || matrix.length == 0 || matrix[0].length == 0 || k <= 0) {
                throw new IllegalArgumentException("输入参数无效：矩阵不能为空且k必须为正整数");
            }

            int m = matrix.length;
            int n = matrix[0].length;

            // 检查k是否超过可能的子矩阵数量
            if (k > m * n) {
                throw new IllegalArgumentException("k值超过了矩阵中可能的子矩阵数量");
            }

            // 初始化前缀异或和矩阵
            int[][] pre_xor = new int[m + 1][n + 1];

            // 存储所有异或值的列表
            List<Integer> values = new ArrayList<>(m * n);

            // 计算前缀异或和并收集所有异或值
            for (int i = 1; i <= m; i++) {
                for (int j = 1; j <= n; j++) {
                    pre_xor[i][j] = pre_xor[i-1][j-1] ^ pre_xor[i-1][j] ^ pre_xor[i][j-1] ^ matrix[i-1][j-1];
                    values.add(pre_xor[i][j]);
                }
            }

            // 排序并返回第K大的值
            Collections.sort(values, Collections.reverseOrder());
            return values.get(k - 1);
        }
    }

    /**
     * 寻找第K大的异或坐标值的优化解决方案类
     * 使用一维数组优化空间复杂度
     */
    public static class OptimizedSolution {
        /**
         * 计算二维矩阵中所有可能的子矩阵的异或和，并返回第K大的值（优化版本）
         *
         * @param matrix 二维整数数组
         * @param k 需要返回的第K大元素的索引
         * @return 所有可能的子矩阵异或和中第K大的值
         * @throws IllegalArgumentException 当输入参数无效时抛出
         */
        public int kthLargestValue(int[][] matrix, int k) {
            // 输入参数校验
            if (matrix == null || matrix.length == 0 || matrix[0].length == 0 || k <= 0) {
                throw new IllegalArgumentException("输入参数无效：矩阵不能为空且k必须为正整数");
            }

            int m = matrix.length;
            int n = matrix[0].length;

            // 检查k是否超过可能的子矩阵数量
            if (k > m * n) {
                throw new IllegalArgumentException("k值超过了矩阵中可能的子矩阵数量");
            }

            // 使用一维数组存储当前行的前缀异或和，节省空间
            int[] prevRow = new int[n + 1];
            PriorityQueue<Integer> minHeap = new PriorityQueue<>(k);

            // 遍历每一行
            for (int i = 0; i < m; i++) {
                // 当前行的前缀异或和
                int[] currRow = new int[n + 1];
                for (int j = 0; j < n; j++) {
                    // 计算当前位置的前缀异或和
                    currRow[j + 1] = currRow[j] ^ prevRow[j + 1] ^ prevRow[j] ^ matrix[i][j];

                    // 维护最小堆
                    minHeap.offer(currRow[j + 1]);
                    if (minHeap.size() > k) {
                        minHeap.poll();
                    }
                }

                // 更新前一行的前缀异或和
                prevRow = currRow;
            }

            return minHeap.peek();
        }
    }

    /**
     * 测试代码
     */
    public static void main(String[] args) {
        testKthLargestValue();
    }

    /**
     * 测试寻找第K大的异或坐标值的函数
     */
    private static void testKthLargestValue() {
        // 测试用例1：基本用例
        int[][] matrix1 = {{5, 2}, {1, 6}};
        int k1 = 1;
        System.out.println("测试用例1：");
        System.out.println("矩阵: [[5, 2], [1, 6]], k: " + k1);
        Solution solution = new Solution();
        int result1 = solution.kthLargestValue(matrix1, k1);
        System.out.println("结果: " + result1);
        System.out.println("预期结果: 7, 测试" + (result1 == 7 ? "通过" : "失败"));
        System.out.println();

        // 测试用例2：k=2
        int k2 = 2;
        int result2 = solution.kthLargestValue(matrix1, k2);
        System.out.println("测试用例2：");
        System.out.println("矩阵: [[5, 2], [1, 6]], k: " + k2);
        System.out.println("结果: " + result2);
        System.out.println("预期结果: 7, 测试" + (result2 == 7 ? "通过" : "失败"));
        System.out.println();

        // 测试用例3：k=3
        int k3 = 3;
        int result3 = solution.kthLargestValue(matrix1, k3);
        System.out.println("测试用例3：");
        System.out.println("矩阵: [[5, 2], [1, 6]], k: " + k3);
        System.out.println("结果: " + result3);
        System.out.println("预期结果: 6, 测试" + (result3 == 6 ? "通过" : "失败"));
        System.out.println();

        // 测试用例4：3x3矩阵
        int[][] matrix4 = {{10, 8, 6}, {3, 5, 7}, {4, 9, 2}};
        int k4 = 4;
        AlternativeSolution solution4 = new AlternativeSolution();
        int result4 = solution4.kthLargestValue(matrix4, k4);
        System.out.println("测试用例4：");
        System.out.println("3x3矩阵, k: " + k4);
        System.out.println("结果: " + result4);
        System.out.println();

        // 测试用例5：单元素矩阵
        int[][] matrix5 = {{42}};
        int k5 = 1;
        OptimizedSolution solution5 = new OptimizedSolution();
        int result5 = solution5.kthLargestValue(matrix5, k5);
        System.out.println("测试用例5：");
        System.out.println("单元素矩阵, k: " + k5);
        System.out.println("结果: " + result5);
        System.out.println("预期结果: 42, 测试" + (result5 == 42 ? "通过" : "失败"));
        System.out.println();

        // 测试异常处理
        try {
            solution.kthLargestValue(new int[0][0], 1);
            System.out.println("测试用例6：空矩阵异常处理 - 失败");
        } catch (IllegalArgumentException e) {
            System.out.println("测试用例6：空矩阵异常处理 - 通过");
        }

        try {
            solution.kthLargestValue(matrix1, 0);
            System.out.println("测试用例7：k=0异常处理 - 失败");
        } catch (IllegalArgumentException e) {
            System.out.println("测试用例7：k=0异常处理 - 通过");
        }
    }
}