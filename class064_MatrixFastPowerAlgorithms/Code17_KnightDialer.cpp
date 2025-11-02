#include <iostream>
#include <vector>
using namespace std;

/**
 * LeetCode 935. 骑士拨号器
 * 题目链接: https://leetcode.cn/problems/knight-dialer/
 * 题目大意: 国际象棋中的骑士可以按照"日"字形移动，骑士在电话拨号盘上移动，计算骑士走n步的不同路径数
 * 解法: 使用矩阵快速幂求解
 * 时间复杂度: O(logn)
 * 空间复杂度: O(1)
 * 
 * 数学分析:
 * 1. 电话拨号盘布局:
 *    1 2 3
 *    4 5 6
 *    7 8 9
 *    * 0 #
 * 2. 骑士移动规则: 从每个数字可以移动到特定的其他数字
 * 3. 构建10×10的转移矩阵表示移动可能性
 * 
 * 优化思路:
 * 1. 使用矩阵快速幂将时间复杂度从O(n)降低到O(logn)
 * 2. 注意模运算防止溢出
 * 
 * 工程化考虑:
 * 1. 边界条件处理: n=1的特殊情况
 * 2. 输入验证: 检查n的有效性
 * 3. 模运算: 防止整数溢出
 * 
 * 与其他解法对比:
 * 1. 动态规划: 时间复杂度O(n)，空间复杂度O(1)
 * 2. 矩阵快速幂: 时间复杂度O(logn)，空间复杂度O(1)
 * 3. 最优性: 当n较大时，矩阵快速幂明显优于动态规划
 */

const int MOD = 1000000007;

/**
 * 矩阵乘法
 * 时间复杂度: O(10^3) = O(1000) = O(1)
 * 空间复杂度: O(100) = O(1)
 */
vector<vector<long long>> matrixMultiply(const vector<vector<long long>>& a, const vector<vector<long long>>& b) {
    int size = a.size();
    vector<vector<long long>> res(size, vector<long long>(size, 0));
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            for (int k = 0; k < size; k++) {
                res[i][j] = (res[i][j] + a[i][k] * b[k][j]) % MOD;
            }
        }
    }
    return res;
}

/**
 * 构造单位矩阵
 * 时间复杂度: O(10^2) = O(100) = O(1)
 * 空间复杂度: O(100) = O(1)
 */
vector<vector<long long>> identityMatrix(int size) {
    vector<vector<long long>> res(size, vector<long long>(size, 0));
    for (int i = 0; i < size; i++) {
        res[i][i] = 1;
    }
    return res;
}

/**
 * 矩阵快速幂
 * 时间复杂度: O(10^3 * logn) = O(logn)
 * 空间复杂度: O(100) = O(1)
 */
vector<vector<long long>> matrixPower(vector<vector<long long>> base, int exp) {
    int size = base.size();
    vector<vector<long long>> res = identityMatrix(size);
    while (exp > 0) {
        if (exp & 1) {
            res = matrixMultiply(res, base);
        }
        base = matrixMultiply(base, base);
        exp >>= 1;
    }
    return res;
}

/**
 * 计算骑士在拨号盘上走n步的不同路径数
 * 时间复杂度: O(logn)
 * 空间复杂度: O(1)
 * 
 * 算法思路:
 * 1. 构建转移矩阵表示骑士移动规则
 * 2. 使用矩阵快速幂计算转移矩阵的n-1次幂
 * 3. 结果矩阵的所有元素之和即为答案
 */
int knightDialer(int n) {
    // 特殊情况处理
    if (n == 1) {
        return 10;
    }
    
    // 构建10×10的转移矩阵
    vector<vector<long long>> base(10, vector<long long>(10, 0));
    
    // 从0可以移动到4,6
    base[0][4] = 1;
    base[0][6] = 1;
    
    // 从1可以移动到6,8
    base[1][6] = 1;
    base[1][8] = 1;
    
    // 从2可以移动到7,9
    base[2][7] = 1;
    base[2][9] = 1;
    
    // 从3可以移动到4,8
    base[3][4] = 1;
    base[3][8] = 1;
    
    // 从4可以移动到0,3,9
    base[4][0] = 1;
    base[4][3] = 1;
    base[4][9] = 1;
    
    // 从5不能移动
    // base[5][*] = 0
    
    // 从6可以移动到0,1,7
    base[6][0] = 1;
    base[6][1] = 1;
    base[6][7] = 1;
    
    // 从7可以移动到2,6
    base[7][2] = 1;
    base[7][6] = 1;
    
    // 从8可以移动到1,3
    base[8][1] = 1;
    base[8][3] = 1;
    
    // 从9可以移动到2,4
    base[9][2] = 1;
    base[9][4] = 1;
    
    // 计算转移矩阵的n-1次幂
    vector<vector<long long>> result = matrixPower(base, n - 1);
    
    // 计算结果：所有元素之和
    long long sum = 0;
    for (int i = 0; i < 10; i++) {
        for (int j = 0; j < 10; j++) {
            sum = (sum + result[i][j]) % MOD;
        }
    }
    
    return (int) sum;
}

int main() {
    // 测试用例
    cout << "n=1: " << knightDialer(1) << endl;  // 10
    cout << "n=2: " << knightDialer(2) << endl;  // 20
    cout << "n=3: " << knightDialer(3) << endl;  // 46
    cout << "n=4: " << knightDialer(4) << endl;  // 104
    cout << "n=3131: " << knightDialer(3131) << endl;  // 136006598
    
    return 0;
}