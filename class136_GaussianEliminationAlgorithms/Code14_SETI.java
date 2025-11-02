package class133;

// POJ 2065 SETI
// 题目链接：http://poj.org/problem?id=2065
// 题目大意：根据给定的模数p和字符串，建立模线性方程组求解多项式系数
// 字符串中每个字符代表方程的常数项，求多项式的系数
//
// 解题思路：
// 1. 根据题目描述建立模线性方程组
// 2. 对于字符串中的每个字符，建立一个方程
// 3. 方程的形式为：a1*x1 + a2*x2 + ... + an*xn ≡ b (mod p)
// 4. 通过高斯消元法求解模线性方程组

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Code14_SETI {

    public static int MAXN = 80;
    
    // 增广矩阵，用于高斯消元求解模线性方程组
    // mat[i][j] 表示第i个方程中第j个变量的系数
    // mat[i][n+1] 表示第i个方程的常数项
    public static long[][] mat = new long[MAXN][MAXN];
    
    // 结果数组，result[i] 表示第i个变量的值
    public static long[] result = new long[MAXN];
    
    // 模数和变量数量
    public static int p, n;
    
    /**
     * 扩展欧几里得算法
     * 求解 ax + by = gcd(a, b) 的整数解
     * @param a 系数a
     * @param b 系数b
     * @return 包含gcd和解的数组，[0]为gcd，[1]为x，[2]为y
     */
    public static long[] exgcd(long a, long b) {
        if (b == 0) {
            return new long[]{a, 1, 0}; // gcd, x, y
        }
        long[] res = exgcd(b, a % b);
        long gcd = res[0];
        long x = res[2];
        long y = res[1] - (a / b) * res[2];
        return new long[]{gcd, x, y};
    }
    
    /**
     * 求解模线性方程 ax ≡ b (mod n)
     * @param a 系数a
     * @param b 等式右边
     * @param n 模数
     * @return 解，无解返回-1
     */
    public static long modLinearEquation(long a, long b, long n) {
        long[] res = exgcd(a, n);
        long gcd = res[0];
        long x = res[1];
        
        // 如果b不能被gcd整除，则无解
        if (b % gcd != 0) {
            return -1; // 无解
        }
        
        // 计算解
        long mod = n / gcd;
        long sol = ((x * (b / gcd)) % mod + mod) % mod;
        return sol;
    }
    
    /**
     * 快速幂运算
     * @param base 底数
     * @param exp 指数
     * @param mod 模数
     * @return (base^exp) % mod
     */
    public static long power(long base, long exp, long mod) {
        long result = 1;
        base %= mod;
        while (exp > 0) {
            if (exp % 2 == 1) {
                result = (result * base) % mod;
            }
            base = (base * base) % mod;
            exp /= 2;
        }
        return result;
    }
    
    /**
     * 求两个数的最大公约数
     * @param a 第一个数
     * @param b 第二个数
     * @return a和b的最大公约数
     */
    public static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    /**
     * 高斯消元法求解模线性方程组
     * 时间复杂度: O(n^3)
     * 空间复杂度: O(n^2)
     * 
     * 数学原理：
     * 模线性方程组形式：
     * a11*x1 + a12*x2 + ... + a1n*xn ≡ b1 (mod p)
     * a21*x1 + a22*x2 + ... + a2n*xn ≡ b2 (mod p)
     * ...
     * an1*x1 + an2*x2 + ... + ann*xn ≡ bn (mod p)
     * 
     * 其中：
     * - xi 表示多项式的系数
     * - aij 表示j^i mod p
     * - bi 表示字符串中第i个字符对应的数值(*=0, a=1, b=2, ..., z=26)
     * 
     * 算法步骤：
     * 1. 对于每一列col，找到一个行pivotRow使得mat[pivotRow][col] != 0
     * 2. 将该行与第col行交换
     * 3. 用第col行消除其他所有行的第col列系数
     * 4. 回代求解
     * 
     * @return 是否有解
     */
    public static boolean gauss() {
        // 对每一列进行处理
        for (int col = 1; col <= n && col <= n; col++) {
            // 寻找第col列中系数不为0的行，将其交换到第col行
            int pivotRow = col;
            for (int i = col; i <= n; i++) {
                if (mat[i][col] != 0) {
                    pivotRow = i;
                    break;
                }
            }
            
            // 如果找不到系数不为0的行，继续处理下一列
            if (mat[pivotRow][col] == 0) {
                continue;
            }
            
            // 将找到的行与第col行交换
            if (pivotRow != col) {
                for (int j = 1; j <= n + 1; j++) {
                    long tmp = mat[col][j];
                    mat[col][j] = mat[pivotRow][j];
                    mat[pivotRow][j] = tmp;
                }
            }
            
            // 用第col行消除其他行的第col列系数
            for (int i = 1; i <= n; i++) {
                if (i != col && mat[i][col] != 0) {
                    // 计算最小公倍数
                    long lcm = mat[col][col] * mat[i][col] / gcd(Math.abs(mat[col][col]), Math.abs(mat[i][col]));
                    long rate1 = lcm / mat[col][col];
                    long rate2 = lcm / mat[i][col];
                    
                    // 消元操作
                    for (int j = 1; j <= n + 1; j++) {
                        mat[i][j] = (mat[i][j] * rate2 - mat[col][j] * rate1) % p;
                        // 确保结果为正数
                        if (mat[i][j] < 0) {
                            mat[i][j] += p;
                        }
                    }
                }
            }
        }
        
        // 回代求解
        for (int i = n; i >= 1; i--) {
            long sum = mat[i][n + 1];
            // 计算已知变量对当前方程的贡献
            for (int j = i + 1; j <= n; j++) {
                sum = (sum - mat[i][j] * result[j] % p + p) % p;
            }
            
            // 求解 mat[i][i] * result[i] ≡ sum (mod p)
            long sol = modLinearEquation(mat[i][i], sum, p);
            if (sol == -1) {
                return false; // 无解
            }
            result[i] = sol;
        }
        
        return true; // 有解
    }
    
    /**
     * 主函数
     * 读取输入数据，构建系数矩阵，调用高斯消元法求解，输出结果
     * 
     * 算法流程：
     * 1. 读取测试用例数量
     * 2. 对于每个测试用例：
     *    a. 读取模数p和字符串
     *    b. 初始化增广矩阵
     *    c. 构造系数矩阵和常数项
     *    d. 使用高斯消元法求解
     *    e. 输出结果
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        int cases = Integer.parseInt(br.readLine().trim());
        
        for (int t = 1; t <= cases; t++) {
            StringTokenizer st = new StringTokenizer(br.readLine().trim());
            p = Integer.parseInt(st.nextToken());
            String str = st.nextToken();
            n = str.length();
            
            // 初始化矩阵，将所有元素置为0
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n + 1; j++) {
                    mat[i][j] = 0;
                }
            }
            
            // 构造系数矩阵和常数项
            for (int i = 1; i <= n; i++) {
                char ch = str.charAt(i - 1);
                // 将字符转换为数值
                long value = 0;
                if (ch == '*') {
                    value = 0;
                } else {
                    value = ch - 'a' + 1;
                }
                mat[i][n + 1] = value; // 设置常数项
                
                // 构造系数矩阵，第i行第j列表示j^i mod p
                for (int j = 1; j <= n; j++) {
                    mat[i][j] = power(j, i - 1, p);
                }
            }
            
            // 使用高斯消元法求解模线性方程组
            if (gauss()) {
                // 输出结果
                for (int i = 1; i <= n; i++) {
                    if (i > 1) {
                        out.print(" ");
                    }
                    out.print(result[i]);
                }
                out.println();
            }
            
            if (t < cases) {
                out.println();
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}