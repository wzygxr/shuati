// POJ 1681 Painter's Problem
// 有一个n*n的正方形网格，每个格子是黄色(Y)或白色(W)
// 当你粉刷一个格子时，该格子以及上下左右相邻格子的颜色都会改变
// 求将所有格子都粉刷成黄色的最少操作次数
// 如果无法完成，输出"inf"
// 测试链接 : http://poj.org/problem?id=1681

/*
 * 题目解析:
 * 这是另一个经典的开关问题，可以用高斯消元解决异或方程组来求解。
 * 
 * 解题思路:
 * 1. 将每个格子是否粉刷设为未知数xi(1表示粉刷，0表示不粉刷)
 * 2. 对于每个格子，建立一个方程表示该格子的最终状态
 * 3. 如果粉刷格子j会影响格子i，则系数aij为1，否则为0
 * 4. 常数项bi为格子i的初始状态与目标状态的异或值
 * 5. 方程形式: ai1*x1 ^ ai2*x2 ^ ... ^ ain*xn = bi
 *    其中^表示异或运算
 * 6. 使用高斯消元求解异或方程组
 * 7. 如果有解，枚举自由元的所有可能取值，找出最少操作次数的解
 * 
 * 时间复杂度: O(n^6) - 高斯消元O(n^6) + 枚举自由元O(2^(自由元个数))
 * 空间复杂度: O(n^4)
 * 
 * 工程化考虑:
 * 1. 正确处理异或运算的性质
 * 2. 位运算优化提高效率
 * 3. 特殊处理无解和多解情况
 * 4. 枚举自由元找出最优解
 */

#include <stdio.h>
#include <string.h>

const int MAXN = 20;
const int INF = 1000000000;

// 增广矩阵，mat[i][j]表示第i个方程中第j个未知数的系数，mat[i][n*n+1]表示第i个方程的常数项
int mat[MAXN * MAXN][MAXN * MAXN + 1];

int n;
char grid[MAXN][MAXN];

// 方向数组，表示当前位置和上下左右五个位置
int dx[] = { 0, -1, 0, 1, 0 };
int dy[] = { 0, 0, -1, 0, 1 };

/*
 * 高斯消元解决异或方程组
 * 时间复杂度: O(n^6)
 * 空间复杂度: O(n^4)
 */
int gauss() {
    int freeNum = 0; // 自由元个数
    int row = 0; // 当前行

    // 高斯消元
    for (int col = 0; col < n * n; col++) {
        int pivotRow = row;
        // 找到第col列中系数为1的行作为主元
        for (int i = row; i < n * n; i++) {
            if (mat[i][col] == 1) {
                pivotRow = i;
                break;
            }
        }

        // 如果第col列全为0，说明是自由元
        if (mat[pivotRow][col] == 0) {
            freeNum++;
            continue;
        }

        // 交换行
        if (pivotRow != row) {
            for (int j = 0; j <= n * n; j++) {
                int temp = mat[row][j];
                mat[row][j] = mat[pivotRow][j];
                mat[pivotRow][j] = temp;
            }
        }

        // 用第row行消去其他行的第col列系数
        for (int i = 0; i < n * n; i++) {
            if (i != row && mat[i][col] == 1) {
                for (int j = col; j <= n * n; j++) {
                    mat[i][j] ^= mat[row][j]; // 异或运算
                }
            }
        }

        row++;
    }

    // 检查是否有解
    for (int i = row; i < n * n; i++) {
        if (mat[i][n * n] != 0) {
            return -1; // 无解
        }
    }

    // 如果没有自由元，直接计算解
    if (freeNum == 0) {
        int ans = 0;
        for (int i = 0; i < n * n; i++) {
            ans += mat[i][n * n];
        }
        return ans;
    }

    // 枚举所有自由元的取值，找出最少操作次数
    int minSteps = INF;
    int freeVars = n * n - row; // 自由元个数

    // 枚举所有可能的自由元取值
    for (int mask = 0; mask < (1 << freeVars); mask++) {
        // 设置自由元的值
        for (int i = 0; i < freeVars; i++) {
            mat[row + i][n * n] = (mask >> i) & 1;
        }

        // 回代求解主元
        for (int i = row - 1; i >= 0; i--) {
            mat[i][n * n] = mat[i][n * n];
            for (int j = i + 1; j < n * n; j++) {
                mat[i][n * n] ^= mat[i][j] & mat[j][n * n];
            }
        }

        // 计算当前解的操作次数
        int steps = 0;
        for (int i = 0; i < n * n; i++) {
            steps += mat[i][n * n];
        }
        if (steps < minSteps) minSteps = steps;
    }

    return minSteps;
}

int main() {
    int testCases;
    scanf("%d", &testCases);

    for (int t = 1; t <= testCases; t++) {
        scanf("%d", &n);

        // 读取网格
        for (int i = 0; i < n; i++) {
            scanf("%s", grid[i]);
        }

        // 初始化矩阵
        memset(mat, 0, sizeof(mat));

        // 建立方程组
        // 对于每个格子位置(i,j)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int id = i * n + j; // 将二维坐标转为一维索引

                // 设置该方程的常数项(初始状态与目标状态的异或值)
                // 初始状态：W=1, Y=0；目标状态：Y=0
                // 所以常数项为1当且仅当初始状态为W
                mat[id][n * n] = (grid[i][j] == 'W') ? 1 : 0;

                // 设置系数矩阵
                // 对于格子(i,j)会影响的5个位置
                for (int k = 0; k < 5; k++) {
                    int x = i + dx[k];
                    int y = j + dy[k];
                    if (x >= 0 && x < n && y >= 0 && y < n) {
                        int pid = x * n + y;
                        mat[id][pid] = 1; // 粉刷格子pid会影响格子id
                    }
                }
            }
        }

        // 高斯消元求解
        int result = gauss();

        // 输出结果
        if (result == -1) {
            printf("inf\n");
        } else {
            printf("%d\n", result);
        }
    }

    return 0;
}