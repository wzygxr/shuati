package class098;

/**
 * 乘法快速幂模版
 * 
 * 算法原理:
 * 快速幂算法通过二进制分解指数，将幂运算的时间复杂度从O(n)降低到O(logn)
 * 例如计算 a^13，13的二进制为1101，即13=8+4+1=2^3+2^2+2^0
 * 所以 a^13 = a^8 * a^4 * a^1
 * 
 * 算法步骤:
 * 1. 将指数转换为二进制表示
 * 2. 从低位到高位遍历二进制位
 * 3. 对于每一位为1的位，将对应的幂累乘到结果中
 * 4. 底数不断平方以得到更高次幂
 * 
 * 时间复杂度: O(logb)
 * 空间复杂度: O(1)
 * 
 * 应用场景:
 * 1. 大数幂运算取模
 * 2. 密码学中的RSA算法
 * 3. 矩阵快速幂的基础
 * 4. 组合数学中的大数计算
 * 
 * 工程化考虑:
 * 1. 模运算防止整数溢出
 * 2. 位运算优化性能
 * 3. 输入输出优化（使用BufferedReader和PrintWriter）
 * 
 * 测试链接: https://www.luogu.com.cn/problem/P1226
 * 
 * 与其他解法对比:
 * 1. 暴力解法: 时间复杂度O(b)，适用于b较小的情况
 * 2. 快速幂: 时间复杂度O(logb)，适用于b较大的情况
 * 3. 最优性: 当b较大时，快速幂明显优于暴力解法
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code01_QuickPower {

    public static long a, b, p;

    public static void main(String[] args) throws IOException {
        // 使用BufferedReader提高输入效率
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        // 使用PrintWriter提高输出效率
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取输入参数 a, b, p
        in.nextToken();
        a = (int) in.nval;
        in.nextToken();
        b = (int) in.nval;
        in.nextToken();
        p = (int) in.nval;
        
        // 输出结果
        out.println(a + "^" + b + " mod " + p + "=" + power());
        out.flush();
        out.close();
        br.close();
    }

    /**
     * 快速幂算法实现
     * 
     * 算法原理:
     * 利用二进制分解指数，通过不断平方和累积结果实现快速计算
     * 例如: a^13，13的二进制为1101
     * a^13 = a^8 * a^4 * a^1 (对应二进制位为1的位置)
     * 
     * 实现细节:
     * 1. 使用位运算 (b & 1) 检查最低位是否为1
     * 2. 使用位移运算 (b >>= 1) 将指数右移一位
     * 3. 底数不断平方 (a = a * a)
     * 4. 每步都进行模运算防止溢出
     * 
     * 时间复杂度: O(logb)
     * 空间复杂度: O(1)
     * 
     * @return a^b mod p 的结果
     */
    public static int power() {
        long ans = 1;  // 结果初始化为1（乘法单位元）
        
        // 当指数大于0时继续循环
        while (b > 0) {
            // 如果指数的最低位为1，则将当前底数累乘到结果中
            if ((b & 1) == 1) {
                ans = (ans * a) % p;
            }
            // 底数不断平方
            a = (a * a) % p;
            // 指数右移一位（相当于除以2）
            b >>= 1;
        }
        return (int) ans;
    }

}