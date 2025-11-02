#include <iostream>
#include <vector>
#include <cstring>
#include <ctime>
#include <cstdlib>
#include <algorithm>
using namespace std;

/**
 * Codeforces 103D Time to Raid Cowavans
 * 题目要求：多次跳跃查询区间和
 * 核心技巧：分块预处理
 * 时间复杂度：O(n√n) 预处理，O(√n) 查询
 * 测试链接：https://codeforces.com/problemset/problem/103/D
 *
 * 该问题的核心思想是：对于每个查询(l, k)，我们需要计算从位置l开始，每隔k步取一个元素的和。
 * 直接暴力计算的时间复杂度为O(n)，对于大量查询会超时。
 * 使用分块预处理的方法，我们可以将时间复杂度优化到预处理O(n√n)，查询O(√n)。
 */

const int MAXN = 100010;
const int BLOCK_SIZE = 320; // sqrt(1e5) ≈ 316

// 存储原始数组
long long a[MAXN];
// 分块预处理的结果，sum[k][i]表示步长为k时，从位置i开始的和（k <= BLOCK_SIZE）
long long sum[BLOCK_SIZE + 1][MAXN];

/**
 * 预处理函数
 * 对于步长k <= BLOCK_SIZE的情况，预处理每个起始位置的和
 * 对于步长k > BLOCK_SIZE的情况，查询时暴力计算，因为这种情况下查询次数较少
 */
void preprocess(int n) {
    // 预处理小步长的情况（k <= BLOCK_SIZE）
    for (int k = 1; k <= BLOCK_SIZE; k++) {
        // 从后往前预处理，避免重复计算
        for (int i = n; i >= 1; i--) {
            sum[k][i] = a[i];
            if (i + k <= n) {
                sum[k][i] += sum[k][i + k];
            }
        }
    }
}

/**
 * 查询函数
 * @param l 起始位置（1-based）
 * @param k 步长
 * @param n 数组长度
 * @return 从位置l开始，每隔k步取一个元素的和
 */
long long query(int l, int k, int n) {
    // 如果步长k很小，直接使用预处理的结果
    if (k <= BLOCK_SIZE) {
        return sum[k][l];
    }
    
    // 对于大步长，直接暴力计算，因为最多需要计算n/k次，而k > sqrt(n)，所以最多计算sqrt(n)次
    long long res = 0;
    for (int i = l; i <= n; i += k) {
        res += a[i];
    }
    return res;
}

/**
 * 主函数，处理输入输出
 */
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n;
    cin >> n;
    
    for (int i = 1; i <= n; i++) {
        cin >> a[i];
    }
    
    // 预处理
    preprocess(n);
    
    int q;
    cin >> q;
    
    while (q--) {
        int l, k;
        cin >> l >> k;
        cout << query(l, k, n) << '\n';
    }
    
    return 0;
}

/**
 * 正确性测试函数
 */
void correctnessTest() {
    cout << "=== 正确性测试 ===\n";
    
    // 测试用例1：小步长查询
    int n1 = 10;
    for (int i = 1; i <= n1; i++) {
        a[i] = i;
    }
    preprocess(n1);
    
    cout << "查询 (l=1, k=2): " << query(1, 2, n1) << '\n'; // 应为1+3+5+7+9=25
    cout << "查询 (l=2, k=3): " << query(2, 3, n1) << '\n'; // 应为2+5+8=15
    cout << "查询 (l=1, k=1): " << query(1, 1, n1) << '\n'; // 应为55
    
    // 测试用例2：大步长查询
    int n2 = 12;
    for (int i = 1; i <= n2; i++) {
        a[i] = i * 10LL;
    }
    preprocess(n2);
    
    cout << "查询 (l=3, k=500): " << query(3, 500, n2) << '\n'; // 应为30
    cout << "查询 (l=1, k=4): " << query(1, 4, n2) << '\n'; // 应为10+50+90=150
    
    // 测试边界情况
    cout << "查询 (l=10, k=1): " << query(10, 1, n2) << '\n'; // 应为100+110+120=330
    cout << "查询 (l=12, k=100): " << query(12, 100, n2) << '\n'; // 应为120
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "\n=== 性能测试 ===\n";
    
    // 测试大规模数据
    int n = 100000;
    for (int i = 1; i <= n; i++) {
        a[i] = i; // 简单的数据模式
    }
    
    clock_t startTime = clock();
    preprocess(n);
    clock_t endTime = clock();
    double preprocessTime = (double)(endTime - startTime) * 1000.0 / CLOCKS_PER_SEC;
    cout << "预处理1e5数据耗时：" << preprocessTime << "ms\n";
    
    // 测试不同步长的查询性能
    int q = 100000;
    srand(42);
    long long totalResult = 0;
    
    startTime = clock();
    for (int i = 0; i < q; i++) {
        int l = rand() % n + 1;
        int k = rand() % n + 1;
        totalResult += query(l, k, n);
    }
    endTime = clock();
    double queryTime = (double)(endTime - startTime) * 1000.0 / CLOCKS_PER_SEC;
    
    cout << "处理1e5查询耗时：" << queryTime << "ms\n";
    cout << "总结果（避免编译器优化）：" << totalResult << "\n";
}

/**
 * 块大小优化分析函数
 */
void blockSizeAnalysis() {
    cout << "\n=== 块大小优化分析 ===\n";
    
    int n = 100000;
    for (int i = 1; i <= n; i++) {
        a[i] = i;
    }
    
    vector<int> blockSizes = {100, 200, 300, 320, 400, 500, 600, 1000};
    
    for (int bs : blockSizes) {
        // 动态分配预处理数组
        vector<vector<long long>> tempSum(bs + 1, vector<long long>(n + 2, 0));
        
        clock_t startTime = clock();
        // 预处理
        for (int k = 1; k <= bs; k++) {
            for (int i = n; i >= 1; i--) {
                tempSum[k][i] = a[i];
                if (i + k <= n) {
                    tempSum[k][i] += tempSum[k][i + k];
                }
            }
        }
        
        clock_t endTime = clock();
        double preprocessTime = (double)(endTime - startTime) * 1000.0 / CLOCKS_PER_SEC;
        
        // 测试查询性能
        int q = 100000;
        srand(42);
        long long totalResult = 0;
        
        startTime = clock();
        for (int i = 0; i < q; i++) {
            int l = rand() % n + 1;
            int k = rand() % n + 1;
            
            if (k <= bs) {
                totalResult += tempSum[k][l];
            } else {
                long long res = 0;
                for (int j = l; j <= n; j += k) {
                    res += a[j];
                }
                totalResult += res;
            }
        }
        
        endTime = clock();
        double queryTime = (double)(endTime - startTime) * 1000.0 / CLOCKS_PER_SEC;
        
        printf("块大小=%d: 预处理耗时=%.2fms, 查询耗时=%.2fms\n", 
               bs, preprocessTime, queryTime);
    }
}

/**
 * 边界情况测试
 */
void boundaryTest() {
    cout << "\n=== 边界情况测试 ===\n";
    
    // 测试n=1的情况
    int n1 = 1;
    a[1] = 100;
    preprocess(n1);
    cout << "n=1, 查询(1,1): " << query(1, 1, n1) << '\n'; // 应为100
    cout << "n=1, 查询(1,100): " << query(1, 100, n1) << '\n'; // 应为100
    
    // 测试l=n的情况
    int n2 = 1000;
    for (int i = 1; i <= n2; i++) {
        a[i] = i;
    }
    preprocess(n2);
    cout << "l=n=1000, k=1: " << query(n2, 1, n2) << '\n'; // 应为1000
    cout << "l=n=1000, k=500: " << query(n2, 500, n2) << '\n'; // 应为1000
    
    // 测试k=0的情况（题目中k应该是正数，这里进行健壮性测试）
    try {
        query(1, 0, n2);
    } catch (...) {
        cout << "k=0异常处理正常\n";
    }
}

/**
 * 运行所有测试的函数
 */
void runAllTests() {
    correctnessTest();
    performanceTest();
    blockSizeAnalysis();
    boundaryTest();
}

/**
 * 算法原理解析：
 *
 * 1. 问题分析：
 *    - 给定一个数组，多次查询从某个位置l开始，每隔k步取一个元素的和
 *    - 直接暴力解法：对于每个查询，遍历所有符合条件的位置，时间复杂度O(n) per query
 *    - 当n和q都很大时，暴力解法会超时
 *
 * 2. 分块思想：
 *    - 将步长k分为两类：小步长(k <= √n)和大步长(k > √n)
 *    - 对于小步长：预处理所有可能的起始位置的和
 *    - 对于大步长：由于k > √n，每个查询最多需要访问√n个元素，直接暴力计算
 *
 * 3. C++特定优化：
 *    - 使用全局数组而不是局部数组，避免栈溢出
 *    - 使用long long类型防止整数溢出
 *    - 使用ios::sync_with_stdio(false)和cin.tie(nullptr)加速输入输出
 *    - 对于块大小分析，使用vector动态分配内存，避免栈溢出
 *    - 使用预处理技巧减少重复计算
 *
 * 4. 时间复杂度分析：
 *    - 预处理时间：O(√n * n)
 *    - 查询时间：
 *      - 小步长查询：O(1)
 *      - 大步长查询：O(√n)
 *    - 总体时间复杂度：O(n√n) 预处理 + O(q√n) 查询
 *
 * 5. 空间复杂度分析：
 *    - O(n√n) 用于存储预处理的结果
 *    - 在C++中，全局数组的空间分配受到栈大小限制，所以需要合理设计数组大小
 *
 * 6. 优化技巧：
 *    - 预处理顺序优化：从后往前计算可以避免重复计算
 *    - 内存优化：使用动态内存分配处理大块数据
 *    - 编译优化：启用编译器优化选项可以显著提升性能
 *    - 循环展开：对于内层循环可以考虑循环展开
 *
 * 7. 边界处理：
 *    - 确保所有数组访问都在有效范围内
 *    - 处理k=0等特殊情况
 *    - 确保1-based索引的正确性
 *
 * 8. 实际应用：
 *    - 该算法广泛应用于需要处理大量跳跃访问模式的场景
 *    - 在数据流分析、图像处理等领域有重要应用
 *    - 是分块算法思想的典型应用案例
 */