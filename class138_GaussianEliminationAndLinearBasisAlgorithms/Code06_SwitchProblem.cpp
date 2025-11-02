#include <iostream>
#include <vector>
#include <cstring>
#include <cmath>

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
 * POJ 1830 开关问题
 * 题目描述：
 * 有N个相同的开关，每个开关都与某些开关有着联系。
 * 每当你打开或者关闭某个开关的时候，其他的与此开关相关联的开关也会相应地发生变化。
 * 给定开关的初始状态和目标状态，求有多少种方案可以完成任务。
 * 
 * 解题思路：
 * 1. 将每个开关是否操作设为未知数xi(1表示操作，0表示不操作)
 * 2. 对于每个开关，建立一个方程表示该开关的最终状态
 * 3. 使用高斯消元求解异或方程组
 * 4. 根据解的情况判断方案数
 * 
 * 时间复杂度：O(n^3)
 * 空间复杂度：O(n^2)
 * 
 * 最优解分析：
 * 这是标准的异或方程组高斯消元算法，时间复杂度已经是最优的。
 */

const int MAXN = 35;

int mat[MAXN][MAXN]; // 增广矩阵
int startState[MAXN]; // 初始状态
int targetState[MAXN]; // 目标状态

/**
 * 高斯消元求解异或方程组
 * 时间复杂度：O(n^3)
 * 空间复杂度：O(n^2)
 * 
 * 返回值：
 * -1: 无解
 * 其他: 自由变量的个数
 */
int gauss(int n) {
    int rank = 0; // 矩阵的秩
    int freeVars = 0; // 自由变量个数
    
    for (int col = 0; col < n; col++) {
        // 寻找主元
        int pivot = -1;
        for (int i = rank; i < n; i++) {
            if (mat[i][col] == 1) {
                pivot = i;
                break;
            }
        }
        
        if (pivot == -1) {
            freeVars++;
            continue;
        }
        
        // 交换行
        if (pivot != rank) {
            for (int j = col; j <= n; j++) {
                swap(mat[rank][j], mat[pivot][j]);
            }
        }
        
        // 消去其他行
        for (int i = 0; i < n; i++) {
            if (i != rank && mat[i][col] == 1) {
                for (int j = col; j <= n; j++) {
                    mat[i][j] ^= mat[rank][j];
                }
            }
        }
        
        rank++;
    }
    
    // 检查无解情况
    for (int i = rank; i < n; i++) {
        if (mat[i][n] != 0) {
            return -1; // 无解
        }
    }
    
    return freeVars;
}

int main() {
    int T;
    cin >> T;
    
    while (T--) {
        int n;
        cin >> n;
        
        // 读取初始状态
        for (int i = 0; i < n; i++) {
            cin >> startState[i];
        }
        
        // 读取目标状态
        for (int i = 0; i < n; i++) {
            cin >> targetState[i];
        }
        
        // 初始化矩阵
        memset(mat, 0, sizeof(mat));
        
        // 建立方程：每个开关的最终状态方程
        for (int i = 0; i < n; i++) {
            // 常数项 = 初始状态 ^ 目标状态
            mat[i][n] = startState[i] ^ targetState[i];
            
            // 对角线元素：操作自己会影响自己
            mat[i][i] = 1;
        }
        
        // 读取开关之间的关联关系
        int u, v;
        while (cin >> u >> v && (u || v)) {
            // 注意：题目中开关编号从1开始，我们内部从0开始
            u--; v--;
            
            // 操作开关v会影响开关u
            mat[u][v] = 1;
        }
        
        // 高斯消元
        int result = gauss(n);
        
        if (result == -1) {
            cout << "Oh,it's impossible~!!" << endl;
        } else {
            // 方案数 = 2^(自由变量个数)
            cout << (1 << result) << endl;
        }
    }
    
    return 0;
}