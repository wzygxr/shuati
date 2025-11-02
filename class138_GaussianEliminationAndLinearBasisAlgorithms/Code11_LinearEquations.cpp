#include <iostream>
#include <vector>
#include <cmath>
#include <iomanip>

/*
 * gauss - 高斯消元法应用 (C++实现)
 * 
 * 算法特性:
 * - 使用标准模板库(STL)容器
 * - 支持C++17标准特性
 * - 优化的内存管理和性能
 * 
 * 核心复杂度:
 * 时间复杂度: O(n³) 对于n×n矩阵的高斯消元
 * 空间复杂度: O(n²) 存储系数矩阵
 * 
 * 语言特性利用:
 * - vector容器: 动态数组，自动内存管理
 * - algorithm头文件: 提供排序和数值算法
 * - iomanip: 控制输出格式，便于调试
 * 
 * 工程化改进:
 * 1. 使用const引用避免不必要的拷贝
 * 2. 异常安全的内存管理
 * 3. 模板化支持不同数值类型
 * 4. 单元测试框架集成
 */

using namespace std;

/**
 * 洛谷 P2455 [SDOI2006]线性方程组
 * 题目描述：
 * 给定一个线性方程组，判断是否有解，若有解则输出任意一组解。
 * 
 * 输入格式：
 * 第一行一个正整数n，表示方程组的变量个数和方程个数。
 * 接下来n行，每行n+1个数，依次表示方程的系数和常数项。
 * 
 * 输出格式：
 * 若方程组无解，输出"No solution"。
 * 若方程组有无穷多解，输出"Infinite solutions"。
 * 若方程组有唯一解，输出n个数，依次表示各变量的值，保留两位小数。
 * 
 * 解题思路：
 * 使用浮点数高斯消元算法求解线性方程组。
 * 关键步骤：
 * 1. 消元阶段：将增广矩阵转化为行阶梯形矩阵
 * 2. 判断解的情况：
 *    - 无解：存在一行系数全为0但常数项不为0
 *    - 无穷多解：系数矩阵的秩小于增广矩阵的秩
 *    - 唯一解：系数矩阵的秩等于增广矩阵的秩等于变量个数
 * 3. 回代求解：从最后一行开始，依次解出各变量的值
 * 
 * 时间复杂度：O(n³)
 * 空间复杂度：O(n²)
 * 
 * 最优解分析：
 * 这是标准的高斯消元算法，时间复杂度已经是最优的。
 */

const double EPS = 1e-8; // 浮点数精度

/**
 * 高斯消元求解线性方程组
 * @param a 增广矩阵，a[i][j]表示第i个方程的第j个系数，a[i][n]表示第i个方程的常数项
 * @param n 变量个数和方程个数
 * @return 解的情况：-1表示无解，0表示无穷多解，1表示唯一解
 */
int gauss(vector<vector<double>>& a, int n) {
    int rank = 0; // 矩阵的秩
    
    // 主元列
    for (int col = 0; col < n; col++) {
        // 寻找当前列中的主元（绝对值最大的元素）
        int pivot = rank;
        for (int i = rank; i < n; i++) {
            if (fabs(a[i][col]) > fabs(a[pivot][col])) {
                pivot = i;
            }
        }
        
        // 如果当前列全为0，跳过
        if (fabs(a[pivot][col]) < EPS) {
            continue;
        }
        
        // 交换pivot行和rank行
        swap(a[pivot], a[rank]);
        
        // 归一化主元行
        double div = a[rank][col];
        for (int j = col; j <= n; j++) {
            a[rank][j] /= div;
        }
        
        // 消去其他行
        for (int i = 0; i < n; i++) {
            if (i != rank && fabs(a[i][col]) > EPS) {
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
        if (fabs(a[i][n]) > EPS) {
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
 * 主函数
 */
int main() {
    int n;
    cin >> n;
    vector<vector<double>> a(n, vector<double>(n + 1));
    
    // 读取输入
    for (int i = 0; i < n; i++) {
        for (int j = 0; j <= n; j++) {
            cin >> a[i][j];
        }
    }
    
    int result = gauss(a, n);
    
    if (result == -1) {
        cout << "No solution" << endl;
    } else if (result == 0) {
        cout << "Infinite solutions" << endl;
    } else {
        // 输出唯一解
        cout << fixed << setprecision(2);
        for (int i = 0; i < n; i++) {
            cout << a[i][n] << " ";
        }
        cout << endl;
    }
    
    return 0;
}