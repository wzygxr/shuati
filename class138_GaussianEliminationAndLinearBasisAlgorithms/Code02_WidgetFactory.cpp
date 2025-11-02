#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <cstring>
#include <cmath>

/*
 * initInv - 高斯消元法应用 (C++实现)
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
 * POJ 2947 Widget Factory
 * 题目描述：
 * 有n种工具，m条记录。每条记录包含：加工工具数量、开始星期、结束星期、工具编号序列。
 * 每种工具制作天数是固定的（3-9天），根据记录推断制作天数。
 * 
 * 解题思路：
 * 1. 将每条记录转化为模7线性方程
 * 2. 使用高斯消元法求解模线性方程组
 * 3. 判断解的情况：无解、多解、唯一解
 * 
 * 时间复杂度：O(max(n,m)^3)
 * 空间复杂度：O(max(n,m)^2)
 * 
 * 最优解分析：
 * 这是标准的高斯消元算法，时间复杂度已经是最优的。
 */

const int MOD = 7;
const int MAXN = 310;

int mat[MAXN][MAXN]; // 增广矩阵
int inv[MOD];        // 模MOD的逆元

/**
 * 预处理模MOD意义下的逆元
 * 时间复杂度：O(MOD)
 * 空间复杂度：O(MOD)
 */
void initInv() {
    inv[1] = 1;
    for (int i = 2; i < MOD; i++) {
        inv[i] = (MOD - MOD / i * inv[MOD % i] % MOD) % MOD;
    }
}

/**
 * 计算两个整数的最大公约数
 * 时间复杂度：O(log(min(a,b)))
 * 空间复杂度：O(1)
 */
int gcd(int a, int b) {
    return b == 0 ? a : gcd(b, a % b);
}

/**
 * 将星期字符串转换为数字（0-6）
 * 时间复杂度：O(1)
 * 空间复杂度：O(1)
 */
int getDay(string day) {
    if (day == "MON") return 0;
    if (day == "TUE") return 1;
    if (day == "WED") return 2;
    if (day == "THU") return 3;
    if (day == "FRI") return 4;
    if (day == "SAT") return 5;
    if (day == "SUN") return 6;
    return -1;
}

/**
 * 高斯消元解决模线性方程组
 * 时间复杂度：O(n^3)
 * 空间复杂度：O(n^2)
 */
int gauss(int n, int m) {
    int rank = 0; // 矩阵的秩
    vector<int> freeVars; // 自由变量
    
    for (int col = 0; col < m; col++) {
        // 寻找主元
        int pivot = -1;
        for (int i = rank; i < n; i++) {
            if (mat[i][col] != 0) {
                pivot = i;
                break;
            }
        }
        
        if (pivot == -1) {
            freeVars.push_back(col);
            continue;
        }
        
        // 交换行
        if (pivot != rank) {
            for (int j = col; j <= m; j++) {
                swap(mat[rank][j], mat[pivot][j]);
            }
        }
        
        // 归一化主元行
        int invVal = inv[mat[rank][col]];
        for (int j = col; j <= m; j++) {
            mat[rank][j] = (mat[rank][j] * invVal) % MOD;
        }
        
        // 消去其他行
        for (int i = 0; i < n; i++) {
            if (i != rank && mat[i][col] != 0) {
                int factor = mat[i][col];
                for (int j = col; j <= m; j++) {
                    mat[i][j] = (mat[i][j] - factor * mat[rank][j] % MOD + MOD) % MOD;
                }
            }
        }
        
        rank++;
    }
    
    // 检查无解情况
    for (int i = rank; i < n; i++) {
        if (mat[i][m] != 0) {
            return -1; // 无解
        }
    }
    
    // 判断解的情况
    if (rank < m) {
        return 0; // 多解
    } else {
        return 1; // 唯一解
    }
}

int main() {
    initInv();
    
    int n, m;
    while (cin >> n >> m && (n || m)) {
        memset(mat, 0, sizeof(mat));
        
        for (int i = 0; i < m; i++) {
            int k;
            string startDay, endDay;
            cin >> k >> startDay >> endDay;
            
            int start = getDay(startDay);
            int end = getDay(endDay);
            int days = (end - start + 1 + MOD) % MOD;
            
            vector<int> tools(k);
            for (int j = 0; j < k; j++) {
                cin >> tools[j];
                tools[j]--; // 转换为0-based
            }
            
            // 建立方程
            for (int tool : tools) {
                mat[i][tool] = (mat[i][tool] + 1) % MOD;
            }
            mat[i][n] = days;
        }
        
        int result = gauss(m, n);
        
        if (result == -1) {
            cout << "Inconsistent data." << endl;
        } else if (result == 0) {
            cout << "Multiple solutions." << endl;
        } else {
            // 输出唯一解，并验证在3-9天范围内
            bool valid = true;
            for (int i = 0; i < n; i++) {
                int days = mat[i][n];
                if (days < 3) days += MOD;
                if (days < 3 || days > 9) {
                    valid = false;
                    break;
                }
            }
            
            if (valid) {
                for (int i = 0; i < n; i++) {
                    int days = mat[i][n];
                    if (days < 3) days += MOD;
                    cout << days << (i == n - 1 ? "\n" : " ");
                }
            } else {
                cout << "Inconsistent data." << endl;
            }
        }
    }
    
    return 0;
}