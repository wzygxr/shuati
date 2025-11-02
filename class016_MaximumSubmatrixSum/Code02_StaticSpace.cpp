// ==================================================================================
// 题目2：子矩阵的最大累加和（ACM风格 - C++版本）
// ==================================================================================
// 题目来源：牛客网 (NowCoder)
// 题目链接：https://www.nowcoder.com/practice/cb82a97dcd0d48a7b1f4ee917e2c0409
//
// ACM风格 - 静态空间版本（最推荐！）
//
// ==================================================================================

#include <iostream>
#include <cstring>      // memset
#include <algorithm>    // max
#include <climits>      // INT_MIN
using namespace std;

// 题目给定的最大数据量
const int MAXN = 201;
const int MAXM = 201;

// 静态空间，复用
int mat[MAXN][MAXM];
int arr[MAXM];
int n, m;

// Kadane算法：求一维数组的最大子数组和
// 时间复杂度：O(m)
// 空间复杂度：O(1)
int kadane() {
    int maxSum = INT_MIN;
    int cur = 0;
    for (int i = 0; i < m; i++) {
        cur += arr[i];
        maxSum = max(maxSum, cur);
        if (cur < 0) cur = 0;
    }
    return maxSum;
}

// 求最大子矩阵和
// 时间复杂度：O(n² × m)
// 空间复杂度：O(m)
int solve() {
    int maxSum = INT_MIN;
    
    for (int i = 0; i < n; i++) {
        // 清空辅助数组（重要！）
        memset(arr, 0, sizeof(arr));
        
        for (int j = i; j < n; j++) {
            // 压缩第j行到辅助数组
            for (int k = 0; k < m; k++) {
                arr[k] += mat[j][k];
            }
            // 对压缩数组应用Kadane算法
            maxSum = max(maxSum, kadane());
        }
    }
    
    return maxSum;
}

int main() {
    // C++快速IO优化
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    while (cin >> n >> m) {
        // 读取矩阵
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                cin >> mat[i][j];
            }
        }
        
        // 输出结果
        cout << solve() << "\n";
    }
    
    return 0;
}
