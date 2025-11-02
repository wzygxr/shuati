package class133;

// BZOJ 1013 球形空间产生器
// 题目链接：https://www.lydsy.com/JudgeOnline/problem.php?id=1013
// 题目大意：在n维空间中有一个球，给出球面上n+1个点的坐标，求球心坐标
//
// 解题思路：
// 1. 根据球的性质，球心到球面上任意一点的距离相等
// 2. 对于球面上任意两点，它们到球心的距离相等
// 3. 利用这个性质建立线性方程组
// 4. 通过高斯消元法求解线性方程组，得到球心坐标

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code13_SphereGenerator {

    public static int MAXN = 15;
    
    // 增广矩阵，用于高斯消元求解线性方程组
    // mat[i][j] 表示第i个方程中第j个变量的系数
    // mat[i][n+1] 表示第i个方程的常数项
    public static double[][] mat = new double[MAXN][MAXN];
    
    // 球面上的点，points[i][j] 表示第i个点的第j维坐标
    public static double[][] points = new double[MAXN][MAXN];
    
    // 维度数量
    public static int n;
    
    // 0.0000001 == 1e-7
    // 因为double类型有精度问题，所以认为
    // 如果一个数字绝对值 <  sml，则认为该数字是0
    // 如果一个数字绝对值 >= sml，则认为该数字不是0
    public static double sml = 1e-7;
    
    /**
     * 高斯消元法求解线性方程组
     * 时间复杂度: O(n^3)
     * 空间复杂度: O(n^2)
     * 
     * 数学原理：
     * 线性方程组形式：
     * a11*x1 + a12*x2 + ... + a1n*xn = b1
     * a21*x1 + a22*x2 + ... + a2n*xn = b2
     * ...
     * an1*x1 + an2*x2 + ... + ann*xn = bn
     * 
     * 其中：
     * - xi 表示球心的第i维坐标
     * - aij 和 bi 根据球上任意两点到球心距离相等建立
     * 
     * 算法步骤：
     * 1. 对于每一行i，选择主元（绝对值最大的元素）
     * 2. 将主元所在的行与第i行交换
     * 3. 将第i行的主元系数化为1
     * 4. 用第i行消除其他所有行的第i列系数
     * 
     * @param n 维度数量
     */
    public static void gauss(int n) {
        for (int i = 1; i <= n; i++) {
            // 选择主元，找到第i列中绝对值最大的元素所在的行
            int max = i;
            for (int j = i + 1; j <= n; j++) {
                if (Math.abs(mat[j][i]) > Math.abs(mat[max][i])) {
                    max = j;
                }
            }
            // 交换行，将主元所在的行与第i行交换
            swap(i, max);
            
            // 如果主元的绝对值小于sml，认为是0，继续处理下一行
            if (Math.abs(mat[i][i]) < sml) {
                continue;
            }
            
            // 将第i行的主元系数化为1
            double tmp = mat[i][i];
            for (int j = i; j <= n + 1; j++) {
                mat[i][j] /= tmp;
            }
            
            // 用第i行消除其他所有行的第i列系数
            for (int j = 1; j <= n; j++) {
                if (i != j) {
                    double rate = mat[j][i] / mat[i][i];
                    for (int k = i; k <= n + 1; k++) {
                        mat[j][k] -= mat[i][k] * rate;
                    }
                }
            }
        }
    }
    
    /**
     * 交换矩阵中的两行
     * @param a 第一行的行号
     * @param b 第二行的行号
     */
    public static void swap(int a, int b) {
        double[] tmp = mat[a];
        mat[a] = mat[b];
        mat[b] = tmp;
    }
    
    /**
     * 主函数
     * 读取输入数据，构建系数矩阵，调用高斯消元法求解，输出结果
     * 
     * 算法流程：
     * 1. 读取维度数量n
     * 2. 读取球面上的n+1个点
     * 3. 初始化增广矩阵
     * 4. 根据球上任意两点到球心距离相等建立方程组
     * 5. 使用高斯消元法求解
     * 6. 输出球心坐标
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        in.nextToken();
        n = (int) in.nval;
        
        // 读取球面上的n+1个点
        for (int i = 1; i <= n + 1; i++) {
            for (int j = 1; j <= n; j++) {
                in.nextToken();
                points[i][j] = (double) in.nval;
            }
        }
        
        // 初始化矩阵，将所有元素置为0
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n + 1; j++) {
                mat[i][j] = 0.0;
            }
        }
        
        // 建立方程组
        // 根据球上任意两点到球心距离相等
        // 对于点i和点i+1：(x1-p1[i])^2 + ... + (xn-pn[i])^2 = (x1-p1[i+1])^2 + ... + (xn-pn[i+1])^2
        // 展开并化简得：2*(p1[i+1]-p1[i])*x1 + ... + 2*(pn[i+1]-pn[i])*xn = (p1[i+1]^2 + ... + pn[i+1]^2) - (p1[i]^2 + ... + pn[i]^2)
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                mat[i][j] = 2 * (points[i + 1][j] - points[i][j]);
                mat[i][n + 1] += points[i + 1][j] * points[i + 1][j] - points[i][j] * points[i][j];
            }
        }
        
        // 使用高斯消元法求解线性方程组
        gauss(n);
        
        // 输出结果，保留三位小数
        for (int i = 1; i <= n; i++) {
            if (i > 1) {
                out.print(" ");
            }
            out.printf("%.3f", mat[i][n + 1]);
        }
        out.println();
        
        out.flush();
        out.close();
        br.close();
    }
}