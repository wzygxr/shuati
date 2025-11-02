// Points on Plane问题 - 二维前缀和实现 (C++版本)
// 题目来源: https://codeforces.com/problemset/problem/1181/C
// 题目大意: 给定一个N×N的网格，每个格点上有一些点，多次查询矩形区域内点的数量
// 约束条件: 1 <= N <= 10^3, 1 <= Q <= 10^5

#include <cstdio>
using namespace std;

const int MAXN = 1001;
int n, q;
int arr[MAXN][MAXN];
int sum[MAXN][MAXN]; // 二维前缀和数组

/**
 * 构建二维前缀和数组
 * 时间复杂度: O(N^2)
 * 设计思路: 利用二维前缀和公式计算每个位置的前缀和
 * sum[i][j] = arr[i][j] + sum[i-1][j] + sum[i][j-1] - sum[i-1][j-1]
 * 这样可以避免重复计算，提高查询效率
 */
void build() {
    // 遍历网格的每个位置
    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= n; j++) {
            // 应用二维前缀和公式
            sum[i][j] = arr[i][j] + sum[i - 1][j] + sum[i][j - 1] - sum[i - 1][j - 1];
        }
    }
}

/**
 * 查询矩形区域[r1,c1]到[r2,c2]内点的数量
 * 时间复杂度: O(1)
 * 设计思路: 利用二维前缀和数组快速计算矩形区域和
 * 通过容斥原理计算矩形区域和：
 * 区域和 = sum[r2][c2] - sum[r1-1][c2] - sum[r2][c1-1] + sum[r1-1][c1-1]
 * @param r1 起始行
 * @param c1 起始列
 * @param r2 结束行
 * @param c2 结束列
 * @return 矩形区域内点的数量
 */
int query(int r1, int c1, int r2, int c2) {
    // 应用容斥原理计算矩形区域和
    return sum[r2][c2] - sum[r1 - 1][c2] - sum[r2][c1 - 1] + sum[r1 - 1][c1 - 1];
}

int main() {
    // 读取网格大小n和查询次数q
    scanf("%d%d", &n, &q);
    
    // 读取网格数据
    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= n; j++) {
            scanf("%d", &arr[i][j]);
        }
    }

    // 构建前缀和数组
    build();

    // 处理q次查询
    for (int i = 1; i <= q; i++) {
        int r1, c1, r2, c2;
        scanf("%d%d%d%d", &r1, &c1, &r2, &c2);
        // 输出查询结果
        printf("%d\n", query(r1, c1, r2, c2));
    }
    
    return 0;
}

// 算法说明：
// 1. 使用二维前缀和解决矩形区域查询问题
// 2. 预处理时间复杂度：O(N^2)
// 3. 查询时间复杂度：O(1)
// 4. 空间复杂度：O(N^2)
// 5. 核心思想：通过预处理前缀和数组，将区间查询的复杂度从O(N^2)降低到O(1)