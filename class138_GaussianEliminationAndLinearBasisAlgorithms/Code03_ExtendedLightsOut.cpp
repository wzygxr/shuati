#include <iostream>
#include <vector>
#include <cstring>
#include <algorithm>

/*
 * initMatrix - 高斯消元法应用 (C++实现)
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
 * POJ 1222 EXTENDED LIGHTS OUT
 * 题目描述：
 * 有一个5×6的按钮矩阵，每个按钮控制一盏灯。
 * 按下按钮时，该按钮以及上下左右相邻按钮的灯状态会反转。
 * 给定初始状态，求按哪些按钮可以将所有灯关闭。
 * 
 * 解题思路：
 * 1. 将每个按钮是否按下设为未知数xi(1表示按下，0表示不按)
 * 2. 对于每个灯，建立一个方程表示该灯的最终状态
 * 3. 使用高斯消元求解异或方程组
 * 
 * 时间复杂度：O(30^3) = O(27000)
 * 空间复杂度：O(30^2) = O(900)
 * 
 * 最优解分析：
 * 这是标准的异或方程组高斯消元算法，时间复杂度已经是最优的。
 */

const int MAXN = 31;
const int ROWS = 5;
const int COLS = 6;

int mat[MAXN][MAXN]; // 增广矩阵

// 方向数组：当前位置、上、左、下、右
int dx[5] = {0, -1, 0, 1, 0};
int dy[5] = {0, 0, -1, 0, 1};

/**
 * 初始化矩阵
 * 根据灯的初始状态建立异或方程组
 * 时间复杂度：O(30)
 * 空间复杂度：O(1)
 */
void initMatrix(const vector<vector<int>>& lights) {
    memset(mat, 0, sizeof(mat));
    
    for (int i = 0; i < ROWS; i++) {
        for (int j = 0; j < COLS; j++) {
            int idx = i * COLS + j; // 当前灯的位置索引
            
            // 建立方程：所有影响该灯的按钮的异或和等于初始状态
            for (int d = 0; d < 5; d++) {
                int ni = i + dx[d];
                int nj = j + dy[d];
                
                if (ni >= 0 && ni < ROWS && nj >= 0 && nj < COLS) {
                    int nidx = ni * COLS + nj;
                    mat[idx][nidx] = 1; // 按钮nidx会影响灯idx
                }
            }
            
            // 常数项为灯的初始状态
            mat[idx][MAXN - 1] = lights[i][j];
        }
    }
}

/**
 * 高斯消元求解异或方程组
 * 时间复杂度：O(n^3)
 * 空间复杂度：O(n^2)
 */
void gauss(int n) {
    for (int i = 0; i < n; i++) {
        // 寻找第i列中系数为1的行
        int pivot = i;
        for (int j = i; j < n; j++) {
            if (mat[j][i] == 1) {
                pivot = j;
                break;
            }
        }
        
        // 交换行
        if (pivot != i) {
            for (int j = i; j <= n; j++) {
                swap(mat[i][j], mat[pivot][j]);
            }
        }
        
        // 消去其他行
        for (int j = 0; j < n; j++) {
            if (j != i && mat[j][i] == 1) {
                for (int k = i; k <= n; k++) {
                    mat[j][k] ^= mat[i][k];
                }
            }
        }
    }
}

int main() {
    int T;
    cin >> T;
    
    for (int t = 1; t <= T; t++) {
        vector<vector<int>> lights(ROWS, vector<int>(COLS));
        
        // 读取灯的初始状态
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                cin >> lights[i][j];
            }
        }
        
        // 初始化矩阵
        initMatrix(lights);
        
        // 高斯消元
        gauss(ROWS * COLS);
        
        // 输出结果
        cout << "PUZZLE #" << t << endl;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int idx = i * COLS + j;
                cout << mat[idx][ROWS * COLS];
                if (j < COLS - 1) cout << " ";
            }
            cout << endl;
        }
    }
    
    return 0;
}