// HDU 4418 Time travel
// 在一个有向图中，从起点到终点的期望步数
// 测试链接 : http://acm.hdu.edu.cn/showproblem.php?pid=4418

/*
 * 题目解析:
 * 这是一个期望DP问题，需要使用高斯消元求解线性方程组。
 * 
 * 解题思路:
 * 1. 将图转换为状态转移方程
 * 2. 建立线性方程组表示期望步数
 * 3. 使用高斯消元求解线性方程组
 * 
 * 时间复杂度: O(n^3)
 * 空间复杂度: O(n^2)
 * 
 * 工程化考虑:
 * 1. 正确处理期望DP的状态转移
 * 2. 特殊处理终点状态
 * 3. 输入输出处理
 */

#include <stdio.h>
#include <string.h>
#include <math.h>
#include <vector>

using namespace std;

const int MAXN = 205;
const double EPS = 1e-10; // 浮点数精度

// 邻接表存储图
vector<int> graph[MAXN];

// 增广矩阵
double mat[MAXN][MAXN];

int n, m, start, end, d;
int prob[MAXN];

/*
 * 初始化图
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 */
void initGraph() {
    for (int i = 0; i < n; i++) {
        graph[i].clear();
    }
}

/*
 * 建立方程组
 * 时间复杂度: O(n^2)
 * 空间复杂度: O(n^2)
 */
void buildEquations() {
    // 初始化矩阵
    for (int i = 0; i < n; i++) {
        for (int j = 0; j <= n; j++) {
            mat[i][j] = 0.0;
        }
    }

    // 对于每个状态建立方程
    for (int i = 0; i < n; i++) {
        // 特殊处理终点状态
        if (i == end) {
            mat[i][i] = 1.0;
            mat[i][n] = 0.0;
            continue;
        }

        // 对于其他状态，根据状态转移建立方程
        mat[i][i] = 1.0; // 自身系数为1
        double totalProb = 0.0;
        
        // 遍历所有可能的转移
        for (int j = 1; j <= d; j++) {
            if (prob[j] > 0) {
                int next = (i + j) % n;
                mat[i][next] -= prob[j] / 100.0; // 转移概率
                totalProb += prob[j] / 100.0;
            }
        }
        
        // 常数项为1加上各项转移概率的期望
        mat[i][n] = 1.0 + totalProb;
    }
}

/*
 * 高斯消元解决浮点数线性方程组
 * 时间复杂度: O(n^3)
 * 空间复杂度: O(n^2)
 */
void gauss() {
    for (int i = 0; i < n; i++) {
        // 找到第i列系数绝对值最大的行
        int maxRow = i;
        for (int j = i + 1; j < n; j++) {
            if (fabs(mat[j][i]) > fabs(mat[maxRow][i])) {
                maxRow = j;
            }
        }

        // 交换行
        if (maxRow != i) {
            for (int k = 0; k <= n; k++) {
                double temp = mat[i][k];
                mat[i][k] = mat[maxRow][k];
                mat[maxRow][k] = temp;
            }
        }

        // 如果主元为0，说明矩阵奇异
        if (fabs(mat[i][i]) < EPS) {
            continue;
        }

        // 将第i行主元系数化为1
        double pivot = mat[i][i];
        for (int k = i; k <= n; k++) {
            mat[i][k] /= pivot;
        }

        // 消去其他行的第i列系数
        for (int j = 0; j < n; j++) {
            if (i != j && fabs(mat[j][i]) > EPS) {
                double factor = mat[j][i];
                for (int k = i; k <= n; k++) {
                    mat[j][k] -= factor * mat[i][k];
                }
            }
        }
    }
}

int main() {
    int testCases;
    scanf("%d", &testCases);

    for (int t = 1; t <= testCases; t++) {
        scanf("%d%d%d%d%d", &n, &m, &start, &end, &d);

        // 初始化图
        initGraph();

        // 读取概率分布
        for (int i = 1; i <= d; i++) {
            scanf("%d", &prob[i]);
        }

        // 建立方程组
        buildEquations();

        // 高斯消元求解
        gauss();

        // 输出结果
        if (fabs(mat[start][start] - 1.0) < EPS) {
            printf("Impossible !\n");
        } else {
            printf("%.2f\n", mat[start][n]);
        }
    }

    return 0;
}