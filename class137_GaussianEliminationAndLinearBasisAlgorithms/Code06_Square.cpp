// UVa 11542 Square
// 题目描述：
// 给定n个正整数，每个数的素因子都不超过500，从中选出1个或多个数，
// 使得选出的数的乘积是完全平方数，求有多少种选法。
// 1 <= n <= 100
// 1 <= xi <= 10^15
// 测试链接 : https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2577

// 纯C实现，避免编译问题
extern "C" {
    int scanf(const char*, ...);
    int printf(const char*, ...);
}

/**
 * 高斯消元解决异或方程组 - UVa 11542 Square
 * 
 * 题目解析：
 * 本题要求从给定的n个数中选出若干个数，使得它们的乘积是完全平方数。
 * 一个数是完全平方数当且仅当它的每个素因子的指数都是偶数。
 * 因此，我们需要选择一些数，使得每个素因子在所选数的乘积中的指数都是偶数。
 * 
 * 解题思路：
 * 1. 素因子分解：
 *    - 首先筛出500以内的所有素数
 *    - 对每个输入的数进行素因子分解，记录每个素因子的指数的奇偶性
 * 2. 建立异或方程组：
 *    - 每个素数对应一个方程
 *    - 每个数对应一个未知数
 *    - 系数矩阵A[i][j]表示第j个数中第i个素因子的指数的奇偶性
 *    - 常数项为0（因为我们要求所有素因子的指数都是偶数）
 * 3. 高斯消元：
 *    - 对系数矩阵进行高斯消元
 *    - 统计自由元的个数
 * 4. 计算方案数：
 *    - 方案数为2^(自由元个数) - 1（减1是因为不能一个都不选）
 * 
 * 时间复杂度：O(n * π(500) + π(500)^3)
 * 空间复杂度：O(n * π(500))
 * 其中π(500)表示500以内的素数个数，约为95
 */

const int MAXP = 505;  // 素数上限
const int MAXN = 105;  // 数组大小

// 素数相关
int isPrime[MAXP];
int primes[MAXP];
int primeCount = 0;

// 系数矩阵，mat[i][j]表示第i个素数在第j个数中的指数奇偶性
int mat[MAXP][MAXN];

// 输入的数
long long numbers[MAXN];

/**
 * 手动实现memset功能
 * 
 * @param ptr 指向要填充的内存区域的指针
 * @param value 要设置的值
 * @param num 要设置的字节数
 */
void my_memset(int* ptr, int value, int num) {
    for (int i = 0; i < num; i++) {
        ptr[i] = value;
    }
}

/**
 * 线性筛法求素数
 * 
 * 算法原理：
 * 线性筛法是一种高效的素数筛法，每个合数只会被其最小的质因子筛掉一次，
 * 因此时间复杂度为O(n)。
 * 
 * @param n 筛法上限
 */
void sieve(int n) {
    my_memset(isPrime, 1, MAXP);
    isPrime[0] = isPrime[1] = 0;
    
    for (int i = 2; i <= n; i++) {
        if (isPrime[i]) {
            primes[primeCount++] = i;
        }
        for (int j = 0; j < primeCount && i * primes[j] <= n; j++) {
            isPrime[i * primes[j]] = 0;
            if (i % primes[j] == 0) {
                break;
            }
        }
    }
}

/**
 * 对一个数进行素因子分解，记录每个素因子指数的奇偶性
 * 
 * 算法思路：
 * 1. 遍历所有素数
 * 2. 对于每个素数，统计它在该数中的出现次数
 * 3. 记录指数的奇偶性（奇数为1，偶数为0）
 * 
 * @param num 要分解的数
 * @param col 系数矩阵的列号
 * @param n 素数个数
 */
void factorize(long long num, int col, int n) {
    for (int i = 0; i < n; i++) {
        int cnt = 0;
        while (num % primes[i] == 0) {
            cnt++;
            num /= primes[i];
        }
        mat[i][col] = cnt % 2;  // 记录指数的奇偶性
    }
}

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
 * @param rows 方程个数（素数个数）
 * @param cols 未知数个数（输入数的个数）
 * @return 自由元个数
 */
int gauss(int rows, int cols) {
    int r = 0; // 当前行
    int c = 0; // 当前列

    // 消元过程
    for (; r < rows && c < cols; r++, c++) {
        int pivot = r;

        // 寻找主元（当前列中系数为1的行）
        for (int i = r; i < rows; i++) {
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
            for (int j = 0; j <= cols; j++) {
                int temp = mat[r][j];
                mat[r][j] = mat[pivot][j];
                mat[pivot][j] = temp;
            }
        }

        // 消去其他行的当前列系数
        for (int i = 0; i < rows; i++) {
            if (i != r && mat[i][c] == 1) {
                // 第i行异或第r行
                for (int j = c; j <= cols; j++) {
                    mat[i][j] ^= mat[r][j];
                }
            }
        }
    }

    // 返回自由元个数
    return cols - r;
}

/**
 * 快速幂运算
 * 
 * @param base 底数
 * @param exp  指数
 * @return base^exp
 */
long long power(long long base, int exp) {
    long long result = 1;
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
    // 预处理素数
    sieve(500);
    
    int T;
    scanf("%d", &T);
    
    for (int t = 0; t < T; t++) {
        int n;
        scanf("%d", &n);
        
        // 读取输入数据
        for (int i = 0; i < n; i++) {
            scanf("%lld", &numbers[i]);
        }
        
        // 初始化矩阵
        for (int i = 0; i < primeCount; i++) {
            for (int j = 0; j <= n; j++) {
                mat[i][j] = 0;
            }
        }
        
        // 对每个数进行素因子分解
        for (int i = 0; i < n; i++) {
            factorize(numbers[i], i, primeCount);
        }
        
        // 高斯消元
        int free = gauss(primeCount, n);
        
        // 计算方案数：2^(自由元个数) - 1（减1是因为不能一个都不选）
        long long result = power(2, free) - 1;
        printf("%lld\n", result);
    }
    
    return 0;
}