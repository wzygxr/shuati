// POJ 1830 开关问题
// 题目描述：
// 有N个相同的开关，每个开关都与某些开关有着联系，每当你打开或者关闭某个开关的时候，
// 其他的与此开关相关联的开关也会相应地发生变化，即这些相联系的开关的状态会改变。
// 给出所有开关的初始状态和目标状态，求有多少种操作方法可以达到目标状态。
// 1 <= N <= 29
// 测试链接 : http://poj.org/problem?id=1830

// 纯C实现，避免编译问题
extern "C" {
    int scanf(const char*, ...);
    int printf(const char*, ...);
}

const int MAXN = 35;

// 增广矩阵，mat[i][j]表示第i个方程中第j个未知数的系数，mat[i][n+1]表示第i个方程的常数项
int mat[MAXN][MAXN];

int n;

/**
 * 高斯消元解决异或方程组
 * 
 * 算法步骤：
 * 1. 构造增广矩阵：将方程组的系数和常数项组成增广矩阵
 * 2. 消元过程：
 *    - 从第一行开始，选择主元（该列系数为1的行）
 *    - 将主元行交换到当前行
 *    - 用主元行消去其他行的当前列系数（通过异或运算）
 * 3. 判断解的情况：
 *    - 唯一解：系数矩阵可化为单位矩阵
 *    - 无解：出现形如 0 = 1 的矛盾方程
 *    - 无穷解：出现形如 0 = 0 的自由元方程
 * 
 * @param n 未知数个数
 * @return 0表示有唯一解，1表示有无穷多解，-1表示无解
 */
int gauss(int n) {
    int r = 1; // 当前行
    int c = 1; // 当前列

    // 消元过程
    for (; r <= n && c <= n; r++, c++) {
        int pivot = r;

        // 寻找主元（当前列中系数为1的行）
        for (int i = r; i <= n; i++) {
            if (mat[i][c] == 1) {
                pivot = i;
                break;
            }
        }

        // 如果找不到主元，说明当前列全为0，跳到下一列
        if (mat[pivot][c] == 0) {
            r--; // 保持当前行不变
            continue;
        }

        // 交换第r行和第pivot行
        if (pivot != r) {
            for (int j = 1; j <= n + 1; j++) {
                int temp = mat[r][j];
                mat[r][j] = mat[pivot][j];
                mat[pivot][j] = temp;
            }
        }

        // 消去其他行的当前列系数
        for (int i = 1; i <= n; i++) {
            if (i != r && mat[i][c] == 1) {
                // 第i行异或第r行
                for (int j = c; j <= n + 1; j++) {
                    mat[i][j] ^= mat[r][j];
                }
            }
        }
    }

    // 判断解的情况
    // 检查是否有形如 0 = 1 的矛盾方程
    for (int i = r; i <= n; i++) {
        if (mat[i][n + 1] == 1) {
            return -1; // 无解
        }
    }

    // 判断是否有自由元（形如 0 = 0 的方程）
    if (r <= n) {
        return 1; // 有无穷多解
    }

    return 0; // 有唯一解
}

/**
 * 快速幂运算
 * 
 * @param base 底数
 * @param exp  指数
 * @return base^exp
 */
int power(int base, int exp) {
    int result = 1;
    while (exp > 0) {
        if (exp % 2 == 1) {
            result *= base;
        }
        base *= base;
        exp /= 2;
    }
    return result;
}

int main() {
    int k;
    scanf("%d", &k);

    for (int t = 0; t < k; t++) {
        scanf("%d", &n);

        // 读取初始状态
        int start[MAXN];
        for (int i = 1; i <= n; i++) {
            scanf("%d", &start[i]);
        }

        // 读取目标状态
        int end[MAXN];
        for (int i = 1; i <= n; i++) {
            scanf("%d", &end[i]);
        }

        // 初始化矩阵
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n + 1; j++) {
                mat[i][j] = 0;
            }
            // 自己对自己有影响
            mat[i][i] = 1;
            // 常数项为初始状态与目标状态的异或值
            mat[i][n + 1] = start[i] ^ end[i];
        }

        // 读取开关关系
        int i, j;
        // 读取开关关系，由于POJ的输入格式问题，这里简化处理
        // 实际应用中需要根据具体输入格式调整
        int relation_count = 0;
        // 假设最多有100个关系
        for (int rel = 0; rel < 100; rel++) {
            if (scanf("%d%d", &i, &j) != 2) break;
            if (i == 0 && j == 0) break;
            // 操作开关j会影响开关i
            mat[i][j] = 1;
            relation_count++;
        }

        // 高斯消元
        int result = gauss(n);

        if (result == -1) {
            printf("Oh,it's impossible~!!\n");
        } else if (result == 0) {
            printf("1\n");
        } else {
            // 计算自由元个数
            int free = 0;
            for (int i = 1; i <= n; i++) {
                if (mat[i][i] == 0) {
                    free++;
                }
            }
            printf("%d\n", power(2, free));
        }
    }

    return 0;
}