// 哈希冲突问题 - 分块算法实现 (C++版本)
// 题目来源: https://www.luogu.com.cn/problem/P3396
// 题目大意: 给定一个长度为n的数组arr，支持两种操作：
// 1. 查询操作 A x y: 查询所有满足 i % x == y 的位置i对应的arr[i]之和
// 2. 更新操作 C x y: 将arr[x]的值更新为y
// 约束条件: 1 <= n、m <= 1.5 * 10^5
// 相关解答: 
// - C++版本: Code01_HashCollision.cpp
// - Java版本: Code01_HashCollision1.java, Code01_HashCollision2.java
// - Python版本: Code01_HashCollision.py

#include <cstdio>
#include <cmath>
using namespace std;

// 定义常量
const int MAXN = 150001;    // 最大数组长度
const int MAXB = 401;       // 最大块大小，约等于sqrt(MAXN)

// 全局变量
int n, m;                   // n: 数组长度, m: 操作次数
int blen;                   // 块大小，通常取sqrt(n)
int arr[MAXN];              // 原始数组
long long dp[MAXB][MAXB];   // dp[x][y]: 存储所有满足i % x == y的arr[i]之和，仅预处理x <= blen的情况

/**
 * 查询操作 A x y
 * 查询所有满足 i % x == y 的位置i对应的arr[i]之和
 * 
 * @param x 除数
 * @param y 余数
 * @return 满足条件的位置对应的元素之和
 * 
 * 算法策略:
 * - 对于x <= sqrt(n)的情况: O(1)时间复杂度，直接返回预处理好的dp[x][y]
 * - 对于x > sqrt(n)的情况: O(n/x)时间复杂度，由于x较大，最多执行sqrt(n)次循环
 */
long long query(int x, int y) {
    // 当x较小时(x <= blen)，直接使用预处理结果，O(1)时间
    if (x <= blen) {
        return dp[x][y];
    }
    
    // 当x较大时(x > blen)，暴力枚举所有满足条件的位置
    // 由于x > sqrt(n)，所以最多执行n/x < sqrt(n)次循环，总时间复杂度为O(sqrt(n))
    long long ans = 0;
    for (int i = y; i <= n; i += x) {
        ans += arr[i];
    }
    return ans;
}

/**
 * 更新操作 C x y
 * 将arr[x]的值更新为y，并更新相关的预处理结果
 * 
 * @param i 要更新的位置
 * @param v 新的值
 * 
 * 算法策略:
 * - 计算值的变化量delta
 * - 更新原始数组arr[i]
 * - 更新所有受影响的预处理结果
 * - 时间复杂度: O(sqrt(n))，因为只需要更新x <= sqrt(n)的预处理结果
 */
void update(int i, int v) {
    // 计算值的变化量
    int delta = v - arr[i];
    
    // 更新原始数组
    arr[i] = v;
    
    // 更新所有相关的预处理结果
    // 只需要更新x <= sqrt(n)的情况，因为这些情况被预处理了
    // 对于每个x <= blen，位置i对x的余数是i % x，所以需要更新dp[x][i % x]
    for (int x = 1; x <= blen; x++) {
        dp[x][i % x] += delta;
    }
}

/**
 * 预处理函数
 * 对于所有x <= sqrt(n)的情况，预处理dp[x][y]的值
 * 
 * 预处理策略:
 * - 计算块大小blen = sqrt(n)
 * - 对每个x (1 <= x <= blen)，计算所有可能的余数y (0 <= y < x)对应的arr[i]之和
 * - 时间复杂度: O(n*sqrt(n))，但由于只预处理x <= sqrt(n)的情况，实际复杂度为O(n*sqrt(n))
 */
void prepare() {
    // 计算块大小，通常选择sqrt(n)
    // 使用double类型计算平方根，再转换为int
    blen = (int)sqrt((double)n);
    
    // 全局数组dp默认初始化为0
    
    // 对于每个x <= sqrt(n)，预处理dp[x][y]的值
    for (int x = 1; x <= blen; x++) {
        for (int i = 1; i <= n; i++) {
            // 计算位置i对x取余的结果y
            int y = i % x;
            // 将arr[i]累加到dp[x][y]中
            // 这样dp[x][y]就存储了所有满足i % x == y的arr[i]之和
            dp[x][y] += arr[i];
        }
    }
}

/**
 * 主函数
 * 读取输入数据，初始化数组，预处理数据，处理所有操作
 */
int main() {
    // 读取数组长度n和操作次数m
    scanf("%d%d", &n, &m);
    
    // 读取初始数组元素
    for (int i = 1; i <= n; i++) {
        scanf("%d", &arr[i]);
    }
    
    // 进行预处理，计算dp数组的值
    prepare();
    
    // 处理m次操作
    char op;  // 操作类型
    int x, y; // 操作参数
    for (int i = 1; i <= m; i++) {
        // 读取操作类型和参数
        // 注意scanf前的空格，用于跳过前一个操作后的换行符
        scanf(" %c%d%d", &op, &x, &y);
        
        if (op == 'A') {
            // 查询操作: 计算并输出满足条件的位置对应的元素之和
            printf("%lld\n", query(x, y));
        } else {
            // 更新操作: 将位置x的值更新为y
            update(x, y);
        }
    }
    
    return 0;
}