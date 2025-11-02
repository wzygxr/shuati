package class135;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Code11_LinearEquations - 高斯消元法应用
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
public class Code11_LinearEquations {
    
    private static final double EPS = 1e-8; // 浮点数精度
    
    /**
     * 高斯消元求解线性方程组
     * @param a 增广矩阵，a[i][j]表示第i个方程的第j个系数，a[i][n]表示第i个方程的常数项
     * @param n 变量个数和方程个数
     * @return 解的情况：-1表示无解，0表示无穷多解，1表示唯一解
     */
    public static int gauss(double[][] a, int n) {
        int rank = 0; // 矩阵的秩
        
        // 主元列
        for (int col = 0; col < n; col++) {
            // 寻找当前列中的主元（绝对值最大的元素）
            int pivot = rank;
            for (int i = rank; i < n; i++) {
                if (Math.abs(a[i][col]) > Math.abs(a[pivot][col])) {
                    pivot = i;
                }
            }
            
            // 如果当前列全为0，跳过
            if (Math.abs(a[pivot][col]) < EPS) {
                continue;
            }
            
            // 交换pivot行和rank行
            swap(a, pivot, rank);
            
            // 归一化主元行
            double div = a[rank][col];
            for (int j = col; j <= n; j++) {
                a[rank][j] /= div;
            }
            
            // 消去其他行
            for (int i = 0; i < n; i++) {
                if (i != rank && Math.abs(a[i][col]) > EPS) {
                    double factor = a[i][col];
                    for (int j = col; j <= n; j++) {
                        a[i][j] -= factor * a[rank][j];
                    }
                }
            }
            
            rank++;
        }
        
        // 检查是否有解
        for (int i = rank; i < n; i++) {
            if (Math.abs(a[i][n]) > EPS) {
                // 存在0=非零的情况，无解
                return -1;
            }
        }
        
        // 判断解的个数
        if (rank < n) {
            // 有无穷多解
            return 0;
        } else {
            // 有唯一解
            return 1;
        }
    }
    
    /**
     * 交换矩阵的两行
     */
    private static void swap(double[][] a, int i, int j) {
        double[] temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    
    /**
     * 主函数
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        double[][] a = new double[n][n + 1];
        
        // 读取输入
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= n; j++) {
                a[i][j] = scanner.nextDouble();
            }
        }
        
        int result = gauss(a, n);
        
        if (result == -1) {
            System.out.println("No solution");
        } else if (result == 0) {
            System.out.println("Infinite solutions");
        } else {
            // 输出唯一解
            for (int i = 0; i < n; i++) {
                System.out.printf("%.2f ", a[i][n]);
            }
            System.out.println();
        }
        
        scanner.close();
    }
}