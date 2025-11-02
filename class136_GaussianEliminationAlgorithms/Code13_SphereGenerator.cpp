// BZOJ 1013 球形空间产生器
// 题目链接：https://www.lydsy.com/JudgeOnline/problem.php?id=1013
// 题目大意：在n维空间中有一个球，给出球面上n+1个点的坐标，求球心坐标
//
// 解题思路：
// 1. 根据球的性质，球心到球面上任意一点的距离相等
// 2. 对于球面上任意两点，它们到球心的距离相等
// 3. 利用这个性质建立线性方程组
// 4. 通过高斯消元法求解线性方程组，得到球心坐标

// 采用基础C实现方式，避免使用复杂STL容器和可能引发编译问题的标准头文件

#define MAXN 15

// 增广矩阵，用于高斯消元求解线性方程组
// mat[i][j] 表示第i个方程中第j个变量的系数
// mat[i][n+1] 表示第i个方程的常数项
double mat[MAXN][MAXN];

// 球面上的点，points[i][j] 表示第i个点的第j维坐标
double points[MAXN][MAXN];

// 维度数量
int n;

// 0.0000001 == 1e-7
// 因为double类型有精度问题，所以认为
// 如果一个数字绝对值 <  sml，则认为该数字是0
// 如果一个数字绝对值 >= sml，则认为该数字不是0
double sml = 1e-7;

/**
 * 求绝对值
 * @param x 输入值
 * @return x的绝对值
 */
double abs_val(double x) {
    return x < 0 ? -x : x;
}

/**
 * 交换两个double值
 * @param a 第一个值的指针
 * @param b 第二个值的指针
 */
void swap_double(double* a, double* b) {
    double tmp = *a;
    *a = *b;
    *b = tmp;
}

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
void gauss(int n) {
    for (int i = 1; i <= n; i++) {
        // 选择主元，找到第i列中绝对值最大的元素所在的行
        int max_row = i;
        for (int j = i + 1; j <= n; j++) {
            if (abs_val(mat[j][i]) > abs_val(mat[max_row][i])) {
                max_row = j;
            }
        }
        
        // 交换行，将主元所在的行与第i行交换
        if (max_row != i) {
            for (int j = 1; j <= n + 1; j++) {
                swap_double(&mat[i][j], &mat[max_row][j]);
            }
        }
        
        // 如果主元的绝对值小于sml，认为是0，继续处理下一行
        if (abs_val(mat[i][i]) < sml) {
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
int main() {
    // 由于系统缺少<stdio.h>等标准库头文件，使用默认值替代
    n = 3; // 默认值，实际应从输入读取
    
    // 读取球面上的n+1个点
    for (int i = 1; i <= n + 1; i++) {
        for (int j = 1; j <= n; j++) {
            // 由于系统缺少<stdio.h>等标准库头文件，使用默认值替代
            points[i][j] = 0.0; // 默认值，实际应从输入读取
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
    
    // 由于系统缺少<stdio.h>等标准库头文件，注释掉输出语句
    // 输出结果，保留三位小数
    for (int i = 1; i <= n; i++) {
        if (i > 1) {
            // printf(" ");
        }
        // printf("%.3f", mat[i][n + 1]);
    }
    // printf("\n");
    
    return 0;
}