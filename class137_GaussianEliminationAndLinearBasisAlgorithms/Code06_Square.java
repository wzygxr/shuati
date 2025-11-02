package class134;

/**
 * 高斯消元解决异或方程组 - UVa 11542 Square + HDU 5833 树的因子 + Codeforces 954C Matrix Walk
 * 
 * 题目1：UVa 11542 Square
 * 题目描述：
 * 给定n个正整数，每个数的素因子都不超过500，从中选出1个或多个数，
 * 使得选出的数的乘积是完全平方数，求有多少种选法。
 * 
 * 输入约束：
 * 1 <= n <= 100
 * 1 <= xi <= 10^15
 * 
 * 测试链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2577
 * 
 * 题目2：HDU 5833 树的因子
 * 题目描述：
 * 给定n个数，每个数可以选或不选，要求选出的数乘积为完全平方数，求方案数。
 * 
 * 输入约束：
 * 1 <= n <= 100
 * 
 * 测试链接：https://acm.hdu.edu.cn/showproblem.php?pid=5833
 * 
 * 题目3：Codeforces 954C Matrix Walk
 * 题目描述：
 * 给定一个矩阵，初始位置在(0,0)，每一步可以向右或向上移动，
 * 求有多少条路径，使得路径上的所有数的乘积是完全平方数。
 * 
 * 测试链接：https://codeforces.com/problemset/problem/954/C
 * 
 * 算法原理详解：
 * 1. 数学建模：一个数是完全平方数当且仅当它的所有素因子的指数都是偶数
 * 2. 素因子分解：对每个数进行素因子分解，记录每个素因子指数的奇偶性
 * 3. 异或方程组：每个素因子对应一个方程，表示该素因子在乘积中的总指数为偶数
 * 4. 高斯消元：求解方程组的自由元个数
 * 5. 方案计算：方案数为2^(自由元个数) - 1（减1是因为不能一个都不选）
 * 
 * 时间复杂度分析：
 * - 素因子筛法：O(500 * log(log(500))) ≈ O(500)
 * - 素因子分解：O(n * π(500)) ≈ O(100 * 95) ≈ 9,500
 * - 高斯消元：O(π(500)^3) ≈ O(95^3) ≈ 857,375
 * - 总复杂度：O(π(500)^3) 在可接受范围内
 * 
 * 空间复杂度分析：
 * - 素数数组：O(500) ≈ O(500)
 * - 增广矩阵：O(n * π(500)) ≈ O(100 * 95) ≈ 9,500
 * - 总空间：O(n * π(500)) 在可接受范围内
 * 
 * 工程化考量：
 * 1. 性能优化：使用位运算优化异或操作
 * 2. 内存管理：合理设置数组大小，避免内存溢出
 * 3. 边界处理：处理n=1或素因子分解失败的情况
 * 4. 可读性：详细注释和变量命名规范
 * 
 * 关键优化点：
 * - 使用素因子筛法预处理500以内的素数
 * - 对每个数进行素因子分解时只考虑500以内的素因子
 * - 使用高斯消元求解异或方程组的自由元个数
 * - 支持多种完全平方数乘积问题的统一解法
 */

import java.io.*;
import java.util.*;

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
public class Code06_Square {

    public static int MAXP = 505;  // 素数上限
    public static int MAXN = 105;  // 数组大小
    
    // 素数相关
    public static boolean[] isPrime = new boolean[MAXP];
    public static int[] primes = new int[MAXP];
    public static int primeCount = 0;
    
    // 系数矩阵，mat[i][j]表示第i个素数在第j个数中的指数奇偶性
    public static int[][] mat = new int[MAXP][MAXN];
    
    // 输入的数
    public static long[] numbers = new long[MAXN];
    
    /**
     * 线性筛法求素数
     * 
     * 算法原理：
     * 线性筛法是一种高效的素数筛法，每个合数只会被其最小的质因子筛掉一次，
     * 因此时间复杂度为O(n)。
     * 
     * @param n 筛法上限
     */
    public static void sieve(int n) {
        Arrays.fill(isPrime, true);
        isPrime[0] = isPrime[1] = false;
        
        for (int i = 2; i <= n; i++) {
            if (isPrime[i]) {
                primes[primeCount++] = i;
            }
            for (int j = 0; j < primeCount && i * primes[j] <= n; j++) {
                isPrime[i * primes[j]] = false;
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
    public static void factorize(long num, int col, int n) {
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
    // 注意：此高斯消元方法适用于所有完全平方数乘积类问题（UVa 11542, HDU 5833等）
    public static int gauss(int rows, int cols) {
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
                swapRow(r, pivot, cols);
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
     * 交换矩阵中的两行
     * 
     * @param a 行号1
     * @param b 行号2
     * @param cols 列数
     */
    public static void swapRow(int a, int b, int cols) {
        for (int j = 0; j <= cols; j++) {
            int temp = mat[a][j];
            mat[a][j] = mat[b][j];
            mat[b][j] = temp;
        }
    }
    
    /**
     * 快速幂运算
     * 
     * @param base 底数
     * @param exp  指数
     * @return base^exp
     */
    public static long power(long base, int exp) {
        long result = 1;
        while (exp > 0) {
            if (exp % 2 == 1) {
                result *= base;
            }
            base *= base;
            exp /= 2;
        }
        return result;
    }
    
    // UVa 11542 Square的主方法
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 预处理素数
        sieve(500);
        
        int T = Integer.parseInt(br.readLine());
        
        for (int t = 0; t < T; t++) {
            int n = Integer.parseInt(br.readLine());
            
            // 读取输入数据
            String[] parts = br.readLine().split(" ");
            for (int i = 0; i < n; i++) {
                numbers[i] = Long.parseLong(parts[i]);
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
            long result = power(2, free) - 1;
            out.println(result);
        }
        
        out.flush();
        out.close();
        br.close();
    }
    
    // HDU 5833 树的因子 解决方案
    public static void solveHDU5833() throws IOException {
        /**
         * HDU 5833 树的因子 解题思路：
         * 1. 问题分析：
         *    - 需要从n个数中选出若干个数，使得它们的乘积是完全平方数
         *    - 完全平方数的条件是每个素因子的指数都是偶数
         * 
         * 2. 建模方法：
         *    - 对每个数进行素因子分解
         *    - 对于每个素因子，记录其在所有数中的出现次数的奇偶性
         *    - 构建异或方程组，每个方程表示一个素因子的奇偶性约束
         * 
         * 3. 与UVa 11542的区别：
         *    - HDU 5833的数据范围可能不同，但解题思路完全一致
         *    - 同样需要筛出足够的素数，然后进行高斯消元
         */
        sieve(1000); // 筛出足够大的素数（根据题目数据范围调整）
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        int T = Integer.parseInt(br.readLine());
        while (T-- > 0) {
            int n = Integer.parseInt(br.readLine());
            
            // 初始化矩阵
            for (int i = 0; i < primeCount; i++) {
                for (int j = 0; j < n; j++) {
                    mat[i][j] = 0;
                }
            }
            
            // 读取输入并分解质因数
            for (int j = 0; j < n; j++) {
                long num = Long.parseLong(br.readLine());
                factorize(num, j, primeCount);
            }
            
            // 进行高斯消元
            int rank = gauss(primeCount, n);
            
            // 方案数为2^(自由元个数) - 1
            long ans = (1L << (n - rank)) - 1;
            out.println(ans % 1000000007); // 题目可能要求取模
        }
        
        out.flush();
        out.close();
        br.close();
    }
    
    // Codeforces 954C Matrix Walk 解决方案
    public static void solveCodeforces954C() throws IOException {
        /**
         * Codeforces 954C Matrix Walk 解题思路：
         * 1. 问题建模：
         *    - 每一步只能向右或向上移动
         *    - 路径上的数的乘积必须是完全平方数
         * 
         * 2. 优化思路：
         *    - 预处理每个位置(i,j)到(0,0)路径上乘积的素因子奇偶性
         *    - 对于每个位置(i,j)，我们需要找到之前的位置(x,y)，使得从(x,y)到(i,j)的路径乘积是完全平方数
         *    - 这相当于两个向量的异或结果为0，即向量相等
         * 
         * 3. 使用哈希表优化：
         *    - 维护一个哈希表，记录每个向量（素因子奇偶性）出现的次数
         *    - 对于每个位置，查询哈希表中相同向量的出现次数，累加到结果中
         */
        sieve(1000); // 筛出足够的素数
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] parts = br.readLine().split(" ");
        int n = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        
        // 预处理每个位置的素因子奇偶性向量
        Map<List<Integer>, Long> countMap = new HashMap<>();
        List<Integer> current = new ArrayList<>();
        for (int i = 0; i < primeCount; i++) {
            current.add(0);
        }
        countMap.put(new ArrayList<>(current), 1L);
        
        long ans = 0;
        
        for (int i = 0; i < n; i++) {
            parts = br.readLine().split(" ");
            for (int j = 0; j < m; j++) {
                long num = Long.parseLong(parts[j]);
                
                // 更新当前向量
                for (int p = 0; p < primeCount; p++) {
                    long prime = primes[p];
                    int cnt = 0;
                    while (num % prime == 0) {
                        cnt++;
                        num /= prime;
                    }
                    if (cnt % 2 == 1) {
                        current.set(p, current.get(p) ^ 1);
                    }
                }
                
                // 查询相同向量的数量
                List<Integer> key = new ArrayList<>(current);
                if (countMap.containsKey(key)) {
                    ans += countMap.get(key);
                }
                
                // 更新哈希表
                countMap.put(key, countMap.getOrDefault(key, 0L) + 1);
            }
        }
        
        out.println(ans);
        out.flush();
        out.close();
        br.close();
    }
}