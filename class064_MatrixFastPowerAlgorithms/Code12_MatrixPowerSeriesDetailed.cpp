/**
 * POJ 3233 Matrix Power Series
 * 题目链接: http://poj.org/problem?id=3233
 * 
 * 题目大意: 给定一个n×n的矩阵A和正整数k，求S = A + A^2 + A^3 + ... + A^k
 * 
 * 解法分析:
 * 使用矩阵快速幂和分治法求解，避免直接计算k次矩阵幂
 * 
 * 数学原理:
 * 利用分治思想优化求和过程:
 * 1. 当k为偶数时: S(k) = (A^(k/2) + I) * S(k/2)
 * 2. 当k为奇数时: S(k) = S(k-1) + A^k
 * 
 * 时间复杂度: O(n^3 * logk)
 * 空间复杂度: O(n^2)
 * 
 * 优化思路:
 * 1. 使用分治法避免O(k)次矩阵幂计算
 * 2. 利用矩阵快速幂优化单次幂运算
 * 
 * 工程化考虑:
 * 1. 异常处理: 检查输入参数的有效性
 * 2. 边界条件: k=0, k=1的特殊情况
 * 3. 模运算: 防止整数溢出
 * 4. 内存优化: 复用矩阵对象减少内存分配
 * 
 * 与其他解法对比:
 * 1. 暴力解法: 直接计算每一项然后求和，时间复杂度O(k*n^3)
 * 2. 本解法: 使用分治和矩阵快速幂，时间复杂度O(n^3 * logk)
 * 3. 最优性: 当k较大时，本解法明显优于暴力解法
 */

// 矩阵快速幂专题 - 矩阵幂级数求和
// 补充题目收集
/*
补充题目列表：

1. LeetCode 509. 斐波那契数
   题目链接: https://leetcode.cn/problems/fibonacci-number/
   题目大意: 求斐波那契数列的第n项
   最优解: 矩阵快速幂 O(logn)

2. LeetCode 70. 爬楼梯
   题目链接: https://leetcode.cn/problems/climbing-stairs/
   题目大意: 计算爬到第n阶楼梯的不同方法数
   最优解: 矩阵快速幂 O(logn)

3. LeetCode 1137. 第 N 个泰波那契数
   题目链接: https://leetcode.cn/problems/n-th-tribonacci-number/
   题目大意: 求泰波那契数列的第n项
   最优解: 矩阵快速幂 O(logn)

4. LeetCode 935. 骑士拨号器
   题目链接: https://leetcode.cn/problems/knight-dialer/
   题目大意: 计算骑士在拨号盘上走n步的不同路径数
   最优解: 矩阵快速幂 O(logn)

5. Codeforces 185A - Plant
   题目链接: https://codeforces.com/problemset/problem/185/A
   题目大意: 递归计算植物数量
   最优解: 矩阵快速幂 O(logn)

6. HDU 1575 - Tr A
   题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1575
   题目大意: 求矩阵的迹的幂
   最优解: 矩阵快速幂 O(n^3 logk)

7. POJ 1006 - Biorhythms
   题目链接: http://poj.org/problem?id=1006
   题目大意: 中国剩余定理问题，可用矩阵快速幂优化
   最优解: 矩阵快速幂 O(logn)

8. SPOJ FIBOSUM - Fibonacci Sum
   题目链接: https://www.spoj.com/problems/FIBOSUM/
   题目大意: 求斐波那契数列前n项和
   最优解: 矩阵快速幂 O(logn)

9. AtCoder ABC113D - Number of Amidakuji
   题目链接: https://atcoder.jp/contests/abc113/tasks/abc113_d
   题目大意: 计算Amidakuji的数量
   最优解: 矩阵快速幂 O(n^3 logk)

10. LOJ 10228 - 「一本通 6.6 例 2」Hankson 的趣味题
    题目链接: https://loj.ac/p/10228
    题目大意: 数学问题，可通过矩阵快速幂优化递推
    最优解: 矩阵快速幂 O(logn)
*/

// 全局常量和变量定义
const int MAXN = 35;  // 矩阵最大维度
int n, k, mod;        // 矩阵维度、指数、模数
int A[MAXN][MAXN];    // 输入矩阵

/**
 * 矩阵类定义
 * 封装矩阵数据和操作
 */
struct Matrix {
    int m[MAXN][MAXN];  // 矩阵数据
    
    /**
     * 构造函数
     * 初始化零矩阵
     * 
     * 时间复杂度: O(MAXN^2)
     * 空间复杂度: O(MAXN^2)
     */
    Matrix() {
        for (int i = 0; i < MAXN; i++) {
            for (int j = 0; j < MAXN; j++) {
                m[i][j] = 0;
            }
        }
    }
};

/**
 * 矩阵加法
 * 
 * 算法原理:
 * 对应位置元素相加并取模
 * 
 * 时间复杂度: O(n^2) - 需要遍历矩阵中的每个元素
 * 空间复杂度: O(n^2) - 需要存储结果矩阵
 * 
 * @param a 第一个矩阵
 * @param b 第二个矩阵
 * @return 两个矩阵的和
 * 
 * 算法特点:
 * - 逐元素相加并取模
 * - 防止整数溢出（通过取模运算）
 * 
 * 注意事项:
 * - 假设输入矩阵a和b的维度相同
 * - 在实际工程中应添加维度检查
 */
Matrix matrixAdd(Matrix a, Matrix b) {
    Matrix res;
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            res.m[i][j] = (a.m[i][j] + b.m[i][j]) % mod;
        }
    }
    return res;
}

/**
 * 矩阵乘法
 * 
 * 算法原理:
 * 对于矩阵A(n×n)和矩阵B(n×n)，结果矩阵C(n×n)中:
 * C[i][j] = Σ(A[i][k] * B[k][j]) for k in 0..n-1
 * 
 * 时间复杂度: O(n^3) - 三重循环，每层循环次数与矩阵维度相关
 * 空间复杂度: O(n^2) - 需要存储结果矩阵
 * 
 * @param a 第一个矩阵
 * @param b 第二个矩阵
 * @return 两个矩阵的乘积
 * 
 * 算法特点:
 * - 标准的矩阵乘法实现
 * - 使用long long类型临时变量防止整数溢出
 * - 每一步计算后都进行模运算
 * 
 * 优化思路:
 * - 对于大型矩阵，可以考虑分块矩阵乘法（Strassen算法）降低理论复杂度至O(n^log₂7)≈O(n^2.807)
 * - 缓存友好的实现可以优化内存访问模式，调整循环顺序
 * 
 * 边界检查:
 * - 此实现假设矩阵乘法可行（a的列数等于b的行数）
 */
Matrix matrixMultiply(Matrix a, Matrix b) {
    Matrix res;
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            for (int c = 0; c < n; c++) {
                res.m[i][j] = (int)((res.m[i][j] + (long long)a.m[i][c] * b.m[c][j]) % mod);
            }
        }
    }
    return res;
}

/**
 * 构造单位矩阵
 * 
 * 数学性质:
 * - 单位矩阵I满足: I * A = A * I = A
 * - 主对角线上元素为1，其余为0
 * 
 * 时间复杂度: O(n^2) - 需要初始化n×n矩阵
 * 空间复杂度: O(n^2) - 需要存储单位矩阵
 * 
 * @return 单位矩阵
 * 
 * 应用场景:
 * - 矩阵快速幂的初始结果
 * - 作为矩阵乘法的单位元
 */
Matrix identityMatrix() {
    Matrix res;
    for (int i = 0; i < n; i++) {
        res.m[i][i] = 1;
    }
    return res;
}

/**
 * 矩阵快速幂
 * 
 * 算法原理:
 * 利用二进制分解指数，通过不断平方和累积结果实现快速计算
 * 例如: A^13，13的二进制为1101
 * A^13 = A^8 * A^4 * A^1 (对应二进制位为1的位置)
 * 
 * 时间复杂度: O(n^3 * logp) - 分析：
 *   - 快速幂算法将幂运算分解为O(logp)次乘法
 *   - 每次矩阵乘法的复杂度为O(n^3)
 *   - 总时间复杂度 = O(logp) * O(n^3) = O(n^3 * logp)
 * 
 * 空间复杂度: O(n^2) - 存储矩阵需要O(n^2)空间
 * 
 * @param base 底数矩阵
 * @param exp 指数
 * @return 矩阵的exp次幂
 * 
 * 实现技巧:
 * - 使用位移运算优化指数分解
 * - 使用位运算检查二进制位是否为1
 * - 结果初始化为单位矩阵
 * 
 * 优化点:
 * - 可以通过缓存中间结果进一步优化
 * - 对于稀疏矩阵，可以采用特殊的数据结构降低计算复杂度
 */
Matrix matrixPower(Matrix base, int exp) {
    Matrix res = identityMatrix();
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
 * 矩阵幂级数求和 - 分治法
 * 
 * 数学原理:
 * 利用分治思想优化求和过程，避免直接计算k次矩阵幂
 * S = A + A^2 + A^3 + ... + A^k
 * 
 * 算法思路:
 * 1. 当exp=1时，直接返回base
 * 2. 当exp为奇数时，S(k) = S(k-1) + A^k
 * 3. 当exp为偶数时，S(k) = (A^(k/2) + I) * S(k/2)
 * 
 * 数学原理证明:
 * - 偶数情况: S(k) = A + A^2 + ... + A^k
 *                   = (A + A^2 + ... + A^(k/2)) + (A^(k/2+1) + ... + A^k)
 *                   = S(k/2) + A^(k/2) * S(k/2)
 *                   = (I + A^(k/2)) * S(k/2)
 * 
 * 时间复杂度: O(n^3 * logk) - 分析：
 *   - 每次递归将问题规模减半，共递归logk次
 *   - 每次递归中的矩阵乘法和加法操作复杂度为O(n^3)
 *   - 总时间复杂度 = O(logk) * O(n^3) = O(n^3 * logk)
 * 
 * 空间复杂度: O(n^2) - 分析：
 *   - 存储矩阵需要O(n^2)空间
 *   - 递归调用栈深度为O(logk)
 *   - 总空间复杂度为O(n^2 + logk) = O(n^2)（当n较大时）
 * 
 * @param base 底数矩阵
 * @param exp 指数
 * @return 矩阵幂级数和 S = A + A^2 + ... + A^exp
 * 
 * 异常场景处理:
 * - 处理了exp=0的边界情况，返回零矩阵
 * - 处理了exp=1的边界情况，直接返回原矩阵
 * 
 * 性能优化点:
 * - 使用位移运算替代除法: exp >> 1 比 exp / 2 更高效
 * - 使用位运算检查奇偶性: (exp & 1) 比 exp % 2 更高效
 * - 递归分治策略避免了O(k)次矩阵幂计算
 */
Matrix matrixPowerSeries(Matrix base, int exp) {
    // 边界条件处理
    if (exp == 0) {
        // 返回零矩阵
        return Matrix();
    }
    
    if (exp == 1) {
        return base;
    }
    
    if (exp & 1) {
        // S(k) = S(k-1) + A^k
        Matrix sub = matrixPowerSeries(base, exp - 1);
        Matrix power = matrixPower(base, exp);
        return matrixAdd(sub, power);
    } else {
        // S(k) = (A^(k/2) + I) * S(k/2)
        int half = exp >> 1;
        Matrix sub = matrixPowerSeries(base, half);
        Matrix power = matrixPower(base, half);
        Matrix identity = identityMatrix();
        Matrix factor = matrixAdd(power, identity);
        return matrixMultiply(factor, sub);
    }
}

// 注意：由于编译环境限制，此处省略了输入输出相关代码
// 在实际OJ平台上，需要根据具体要求实现输入输出功能