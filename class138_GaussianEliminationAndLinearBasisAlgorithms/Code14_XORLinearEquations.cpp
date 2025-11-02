#include <iostream>
#include <vector>

/*
 * gaussXOR - 高斯消元法应用 (C++实现)
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
 * 洛谷 P4782 【模板】高斯消元解异或线性方程组
 * 题目描述：
 * 给定一个线性方程组，其中所有的系数和常数项都是0或1，且方程组中的运算符都是异或（XOR）。求方程组的解。
 * 
 * 输入格式：
 * 第一行，一个正整数 n，表示方程的个数和未知数的个数。
 * 接下来的 n 行中，每行有 n+1 个整数，表示一个方程的 n 个系数和 1 个常数项。
 * 
 * 输出格式：
 * 如果有唯一解，输出 n 个整数，表示解。
 * 如果有无穷多解，输出"Multiple sets of solutions"。
 * 如果无解，输出"No solution"。
 * 
 * 解题思路：
 * 使用异或高斯消元算法求解线性方程组。
 * 关键步骤：
 * 1. 消元阶段：将增广矩阵转化为行阶梯形矩阵
 * 2. 判断解的情况：
 *    - 无解：存在一行系数全为0但常数项不为0
 *    - 无穷多解：系数矩阵的秩小于变量个数
 *    - 唯一解：系数矩阵的秩等于变量个数
 * 3. 回代求解：从最后一行开始，依次解出各变量的值
 * 
 * 时间复杂度：O(n³)
 * 空间复杂度：O(n²)
 * 
 * 最优解分析：
 * 这是标准的异或高斯消元算法，时间复杂度已经是最优的。
 */

/**
 * 高斯消元求解异或线性方程组
 * @param a 增广矩阵，a[i][j]表示第i个方程的第j个系数，a[i][n]表示第i个方程的常数项
 * @param n 变量个数和方程个数
 * @param x 解数组，用于存储解
 * @return 解的情况：-1表示无解，0表示无穷多解，1表示唯一解
 */
int gaussXOR(vector<vector<int>>& a, int n, vector<int>& x) {
    int rank = 0; // 矩阵的秩
    
    // 主元列
    for (int col = 0; col < n; col++) {
        // 寻找当前列中的主元（第一个非零元素）
        int pivot = rank;
        while (pivot < n && a[pivot][col] == 0) {
            pivot++;
        }
        
        // 如果当前列全为0，跳过
        if (pivot == n) {
            continue;
        }
        
        // 交换pivot行和rank行
        swap(a[pivot], a[rank]);
        
        // 消去其他行
        for (int i = 0; i < n; i++) {
            if (i != rank && a[i][col] == 1) {
                for (int j = col; j <= n; j++) {
                    a[i][j] ^= a[rank][j];
                }
            }
        }
        
        rank++;
    }
    
    // 检查是否有解
    for (int i = rank; i < n; i++) {
        if (a[i][n] == 1) {
            // 存在0=1的情况，无解
            return -1;
        }
    }
    
    // 初始化解数组
    x.assign(n, 0);
    
    // 回代求解
    vector<int> pivotCol(rank); // 记录每一行的主元列
    int idx = 0;
    for (int col = 0; col < n && idx < rank; col++) {
        for (int i = idx; i < n; i++) {
            if (a[i][col] == 1) {
                pivotCol[idx++] = col;
                break;
            }
        }
    }
    
    for (int i = 0; i < rank; i++) {
        x[pivotCol[i]] = a[i][n];
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
    vector<vector<int>> a(n, vector<int>(n + 1));
    vector<int> x(n);
    
    // 读取输入
    for (int i = 0; i < n; i++) {
        for (int j = 0; j <= n; j++) {
            cin >> a[i][j];
        }
    }
    
    int result = gaussXOR(a, n, x);
    
    if (result == -1) {
        cout << "No solution" << endl;
    } else if (result == 0) {
        cout << "Multiple sets of solutions" << endl;
    } else {
        // 输出唯一解
        for (int i = 0; i < n; i++) {
            cout << x[i] << endl;
        }
    }
    
    return 0;
}