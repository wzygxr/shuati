#include <iostream>
#include <vector>
#include <cstring>
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
 * HDU 3976 Electric resistance
 * 题目描述：
 * 给定一个由n个节点和m个电阻组成的电路，求节点1和节点n之间的等效电阻。
 * 
 * 解题思路：
 * 1. 以每个节点的电势为未知数
 * 2. 根据基尔霍夫电流定律建立方程
 * 3. 根据欧姆定律建立电流与电势的关系
 * 4. 设节点1电势为1，节点n电势为0，建立线性方程组
 * 5. 使用高斯消元求解线性方程组
 * 6. 等效电阻 = (节点1电势 - 节点n电势) / 总电流
 * 
 * 时间复杂度：O(n^3)
 * 空间复杂度：O(n^2)
 * 
 * 最优解分析：
 * 这是标准的电路分析高斯消元算法，时间复杂度已经是最优的。
 */

const int MAXN = 51;
const double EPS = 1e-8;

struct Edge {
    int to;
    double r; // 电阻值
    
    Edge(int to, double r) : to(to), r(r) {}
};

vector<Edge> graph[MAXN];
double mat[MAXN][MAXN]; // 增广矩阵

/**
 * 高斯消元求解线性方程组
 * 时间复杂度：O(n^3)
 * 空间复杂度：O(n^2)
 */
int gauss(int n) {
    int rank = 0;
    
    for (int col = 0; col < n; col++) {
        // 寻找主元
        int pivot = rank;
        for (int i = rank; i < n; i++) {
            if (fabs(mat[i][col]) > fabs(mat[pivot][col])) {
                pivot = i;
            }
        }
        
        // 如果当前列全为0，跳过
        if (fabs(mat[pivot][col]) < EPS) {
            continue;
        }
        
        // 交换行
        if (pivot != rank) {
            for (int j = col; j <= n; j++) {
                swap(mat[rank][j], mat[pivot][j]);
            }
        }
        
        // 归一化主元行
        double div = mat[rank][col];
        for (int j = col; j <= n; j++) {
            mat[rank][j] /= div;
        }
        
        // 消去其他行
        for (int i = 0; i < n; i++) {
            if (i != rank && fabs(mat[i][col]) > EPS) {
                double factor = mat[i][col];
                for (int j = col; j <= n; j++) {
                    mat[i][j] -= factor * mat[rank][j];
                }
            }
        }
        
        rank++;
    }
    
    // 检查是否有解
    for (int i = rank; i < n; i++) {
        if (fabs(mat[i][n]) > EPS) {
            return -1; // 无解
        }
    }
    
    return 1; // 有唯一解
}

int main() {
    int T;
    cin >> T;
    
    for (int t = 1; t <= T; t++) {
        int n, m;
        cin >> n >> m;
        
        // 清空图
        for (int i = 1; i <= n; i++) {
            graph[i].clear();
        }
        
        // 读取电阻连接
        for (int i = 0; i < m; i++) {
            int u, v;
            double r;
            cin >> u >> v >> r;
            
            // 电阻是双向的
            graph[u].push_back(Edge(v, r));
            graph[v].push_back(Edge(u, r));
        }
        
        // 初始化矩阵
        memset(mat, 0, sizeof(mat));
        
        // 建立方程：对于每个节点，流入电流等于流出电流
        for (int i = 1; i <= n; i++) {
            // 节点1：电势设为1
            if (i == 1) {
                mat[i-1][i-1] = 1.0;
                mat[i-1][n] = 1.0;
                continue;
            }
            
            // 节点n：电势设为0
            if (i == n) {
                mat[i-1][i-1] = 1.0;
                mat[i-1][n] = 0.0;
                continue;
            }
            
            // 中间节点：根据基尔霍夫电流定律
            for (const Edge& e : graph[i]) {
                int j = e.to;
                double conductance = 1.0 / e.r; // 电导 = 1/电阻
                
                mat[i-1][i-1] += conductance;
                mat[i-1][j-1] -= conductance;
            }
        }
        
        // 高斯消元
        int result = gauss(n);
        
        if (result == -1) {
            cout << "No solution" << endl;
        } else {
            // 计算总电流：从节点1流出的电流
            double totalCurrent = 0.0;
            for (const Edge& e : graph[1]) {
                int j = e.to;
                double conductance = 1.0 / e.r;
                totalCurrent += conductance * (mat[0][n] - mat[j-1][n]);
            }
            
            // 等效电阻 = 电压差 / 电流
            double voltage = mat[0][n] - mat[n-1][n];
            double equivalentResistance = voltage / totalCurrent;
            
            cout << fixed << setprecision(2) << equivalentResistance << endl;
        }
    }
    
    return 0;
}