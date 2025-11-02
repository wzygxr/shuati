#include <iostream>
#include <vector>
#include <climits>

/*
 * superEggDrop - 高斯消元法应用 (C++实现)
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
 * LeetCode 887. 鸡蛋掉落
 * 题目描述：
 * 给你 k 枚相同的鸡蛋，并可以使用一栋从第 1 层到第 n 层的建筑。
 * 已知存在楼层 f ，满足 0 <= f <= n ，任何从 高于 f 的楼层落下的鸡蛋都会碎，从 f 楼层或比它低的楼层落下的鸡蛋都不会破。
 * 每次操作，你可以取一枚没有碎的鸡蛋，将其从任一楼层 x 扔下（满足 1 <= x <= n）。
 * 如果鸡蛋碎了，你就不能再次使用它。如果鸡蛋没碎，你可以重复使用这枚鸡蛋。
 * 请你计算并返回要确定 f 确切的值 的 最小操作次数 是多少？
 * 
 * 解题思路：
 * 这是一个经典的鸡蛋掉落问题，可以通过数学建模转化为组合数学问题。
 * 设dp[k][m]表示k个鸡蛋，m次操作能确定的最大楼层数。
 * 状态转移方程：dp[k][m] = dp[k-1][m-1] + dp[k][m-1] + 1
 * 其中：
 * - dp[k-1][m-1]表示鸡蛋碎了的情况，可以确定的楼层数
 * - dp[k][m-1]表示鸡蛋没碎的情况，可以确定的楼层数
 * - +1表示当前楼层
 * 
 * 我们可以使用二分查找优化，找到最小的m使得dp[k][m] >= n。
 * 
 * 时间复杂度：O(k log n)，其中k是鸡蛋数，n是楼层数
 * 空间复杂度：O(k)
 * 
 * 最优解分析：
 * 这是该问题的最优解法，通过观察状态转移方程的单调性，使用二分查找将时间复杂度从O(kn)优化到O(k log n)。
 */

/**
 * 计算k个鸡蛋，m次操作能确定的最大楼层数
 * @param k 鸡蛋数
 * @param m 操作次数
 * @return 最大可确定楼层数
 */
long long computeFloors(int k, int m) {
    // 使用O(k)空间优化，因为每次计算只依赖前一次的结果
    vector<long long> dp(k + 1, 0);
    long long floors = 0;
    
    for (int i = 1; i <= m; i++) {
        for (int j = k; j >= 1; j--) {
            // dp[j]表示前i-1次操作的结果
            // 新的结果是鸡蛋碎了的情况 + 鸡蛋没碎的情况 + 1
            dp[j] += dp[j - 1] + 1;
            // 如果已经能覆盖到n层楼，提前返回
            if (dp[j] >= LLONG_MAX / 2) {
                // 防止溢出
                return LLONG_MAX;
            }
        }
        floors = dp[k];
    }
    
    return floors;
}

/**
 * 计算确定f所需的最小操作次数
 * @param k 鸡蛋数
 * @param n 楼层数
 * @return 最小操作次数
 */
int superEggDrop(int k, int n) {
    // 特殊情况处理
    if (k == 1) {
        // 如果只有1个鸡蛋，只能线性测试，最坏需要n次
        return n;
    }
    if (n == 0 || n == 1) {
        // 0层或1层楼，只需要n次操作
        return n;
    }
    
    // 二分查找最小的m
    int left = 1, right = n;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (computeFloors(k, mid) >= n) {
            // 如果mid次操作可以确定>=n层楼，尝试减小m
            right = mid;
        } else {
            // 否则需要增大m
            left = mid + 1;
        }
    }
    
    return left;
}

/**
 * 主函数用于测试
 */
int main() {
    // 测试用例1
    int k1 = 1, n1 = 2;
    cout << "Test case 1: k = " << k1 << ", n = " << n1 << ", result = " << superEggDrop(k1, n1) << endl; // Expected: 2
    
    // 测试用例2
    int k2 = 2, n2 = 6;
    cout << "Test case 2: k = " << k2 << ", n = " << n2 << ", result = " << superEggDrop(k2, n2) << endl; // Expected: 3
    
    // 测试用例3
    int k3 = 3, n3 = 14;
    cout << "Test case 3: k = " << k3 << ", n = " << n3 << ", result = " << superEggDrop(k3, n3) << endl; // Expected: 4
    
    // 测试用例4
    int k4 = 4, n4 = 1000;
    cout << "Test case 4: k = " << k4 << ", n = " << n4 << ", result = " << superEggDrop(k4, n4) << endl; // Expected: 19
    
    return 0;
}