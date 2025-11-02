package class134;

/**
 * 线性基应用专题 - SPOJ XMAX XOR Maximization + HDU 3949 XOR + 牛客网 NC14533 异或和
 * 
 * 题目1：SPOJ XMAX - XOR Maximization
 * 题目描述：
 * 给定一个整数集合S，定义函数X(S)为集合中所有元素的异或值，
 * 求X(S)的最大值。
 * 
 * 输入约束：
 * 1 <= |S| <= 10^5
 * 0 <= ai <= 10^18
 * 
 * 测试链接：https://www.spoj.com/problems/XMAX/
 * 
 * 题目2：HDU 3949 XOR
 * 题目描述：
 * 给定n个整数，求它们的所有子集异或和中第k小的异或值。
 * 
 * 输入约束：
 * 1 <= n <= 10000
 * 0 <= ai <= 1e18
 * 
 * 测试链接：https://acm.hdu.edu.cn/showproblem.php?pid=3949
 * 
 * 题目3：牛客网 NC14533 异或和
 * 题目描述：
 * 给定一个长度为n的数组a，求所有连续子数组的异或和的和。
 * 
 * 测试链接：https://ac.nowcoder.com/acm/problem/14533
 * 
 * 算法原理详解：
 * 1. 线性基构造：
 *    - 线性基是一组基向量，可以表示原集合中所有元素的线性组合（在异或运算下）
 *    - 构造方法：从高位到低位依次处理每个数，尝试将其插入线性基
 *    - 如果当前位已经有基向量，则用异或操作消去当前位的系数
 * 2. 异或最大值：
 *    - 从高位到低位贪心选择，如果当前位可以取1则取1
 *    - 贪心策略：高位取1比低位取1对结果的贡献更大
 * 3. 第k小异或值：
 *    - 将线性基转化为简化行阶梯形矩阵
 *    - 将k的二进制表示与线性基的基向量对应位进行组合
 * 4. 异或和求和：
 *    - 按位考虑每个二进制位对总和的贡献
 *    - 使用前缀异或和和线性基统计每个位的贡献次数
 * 
 * 时间复杂度分析：
 * - 线性基构造：O(n * log(max_value)) ≈ O(10^5 * 60) ≈ 6,000,000
 * - 异或最大值：O(log(max_value)) ≈ O(60)
 * - 第k小异或值：O(log(max_value)) ≈ O(60)
 * 
 * 空间复杂度分析：
 * - 线性基数组：O(log(max_value)) ≈ O(60)
 * - 总空间：O(log(max_value)) 非常高效
 * 
 * 工程化考量：
 * 1. 性能优化：使用位运算优化所有操作
 * 2. 内存管理：线性基占用空间极小，适合处理大规模数据
 * 3. 边界处理：处理空集合、k=0等特殊情况
 * 4. 可读性：详细注释和变量命名规范
 * 
 * 关键优化点：
 * - 使用线性基高效处理异或相关问题
 * - 贪心策略求解异或最大值
 * - 二进制分解求解第k小异或值
 * - 按位统计求解异或和求和
 */

import java.io.*;
import java.util.*;

/**
 * 线性基解决异或最大值问题 - SPOJ XMAX
 * 
 * 题目解析：
 * 本题要求从给定的整数集合中选择一些数，使得它们的异或值最大。
 * 这是一个典型的线性基问题，可以用高斯消元的思想来解决。
 * 
 * 解题思路：
 * 1. 线性基构造：
 *    - 从高位到低位依次考虑每个二进制位
 *    - 对于每个二进制位，维护一个基向量，表示该位可以被表示的数
 * 2. 贪心选择：
 *    - 从高位到低位贪心地选择是否将对应的基向量加入结果
 *    - 如果加入后结果变大，则加入；否则不加入
 * 
 * 时间复杂度：O(n * log(max_value))
 * 空间复杂度：O(log(max_value))
 */
public class Code07_XMAX {

    public static int MAXL = 64;  // 64位整数
    
    // 线性基数组
    public static long[] basis = new long[MAXL];
    
    /**
     * 插入一个数到线性基中
     * 
     * 线性基构造过程：
     * 1. 从高位到低位遍历该数的二进制位
     * 2. 对于每个为1的位：
     *    - 如果该位在线性基中还没有基向量，则直接插入
     *    - 否则用已有的基向量消去该位，继续处理
     * 
     * @param x 要插入的数
     */
    public static void insert(long x) {
        for (int i = MAXL - 1; i >= 0; i--) {
            if (((x >> i) & 1) == 0) continue;  // 如果第i位是0，跳过
            
            if (basis[i] == 0) {
                // 如果basis[i]为空，直接插入
                basis[i] = x;
                break;
            }
            
            // 否则用basis[i]消去x的第i位
            x ^= basis[i];
        }
    }
    
    /**
     * 查询最大异或值
     * 
     * 贪心策略：
     * 从高位到低位贪心地选择是否加入basis[i]
     * 如果加入后结果变大，则加入；否则不加入
     * 
     * @return 最大异或值
     */
    public static long queryMax() {
        long result = 0;
        for (int i = MAXL - 1; i >= 0; i--) {
            // 贪心地选择是否加入basis[i]
            if (((result >> i) & 1) == 0) {  // 如果结果的第i位是0
                result ^= basis[i];  // 加入basis[i]可能会使结果更大
            }
        }
        return result;
    }
    
    /**
     * 线性基重组，用于第k大查询
     * 将线性基转换为标准基，即每个基向量的最高位唯一
     */
    public static void rebuild() {
        // 从低位到高位处理，确保每个基向量的最高位只有自己有1
        for (int i = 0; i < MAXL; i++) {
            for (int j = i - 1; j >= 0; j--) {
                if ((basis[i] >> j & 1) == 1) {
                    basis[i] ^= basis[j];
                }
            }
        }
    }
    
    /**
     * 查询第k小的异或值
     * 注意：调用前需要先调用rebuild()
     * 
     * 算法思路：
     * 1. 将k转换为二进制表示
     * 2. 根据二进制位选择对应的基向量进行异或
     * 
     * @param k 第k小
     * @return 第k小的异或值
     */
    public static long queryKth(long k) {
        // 统计非零基的数量
        int cnt = 0;
        long[] tmp = new long[MAXL];
        for (int i = 0; i < MAXL; i++) {
            if (basis[i] != 0) {
                tmp[cnt++] = basis[i];
            }
        }
        
        // 如果k超过可能的子集数量，返回-1
        if (k >= (1L << cnt)) {
            return -1;
        }
        
        long res = 0;
        for (int i = 0; i < cnt; i++) {
            if ((k >> i & 1) == 1) {
                res ^= tmp[i];
            }
        }
        return res;
    }
    
    /**
     * 计算所有连续子数组的异或和的和
     * 利用前缀异或性质：区间[l,r]的异或和等于prefix[r] ^ prefix[l-1]
     * 
     * 算法思路：
     * 1. 计算前缀异或数组
     * 2. 对于每一位，统计有多少个区间的异或和在该位上是1
     * 3. 该位的总贡献为 1的个数 * 2^bit
     * 
     * @param nums 输入数组
     * @return 所有连续子数组异或和的和
     */
    public static long subarrayXorSum(long[] nums) {
        int n = nums.length;
        long[] prefix = new long[n + 1];
        
        // 计算前缀异或数组
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] ^ nums[i];
        }
        
        long result = 0;
        // 统计每一位在所有子数组异或和中出现的次数
        for (int bit = 0; bit < MAXL; bit++) {
            long count = 0; // 统计前缀异或中第bit位为0的数量
            long one = 0;   // 统计前缀异或中第bit位为1的数量
            
            for (int i = 0; i <= n; i++) {
                if ((prefix[i] >> bit & 1) == 0) {
                    count++;
                } else {
                    one++;
                }
            }
            
            // 第bit位的贡献是 count * one * 2^bit
            result += count * one * (1L << bit);
        }
        
        return result;
    }
    
    // SPOJ XMAX - 异或最大值问题的主方法
    public static void main(String[] args) throws IOException {
        // 主方法处理SPOJ XMAX问题
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        int n = Integer.parseInt(br.readLine());
        
        // 初始化线性基
        Arrays.fill(basis, 0);
        
        // 读取输入数据并插入到线性基中
        for (int i = 0; i < n; i++) {
            long x = Long.parseLong(br.readLine());
            insert(x);
        }
        
        // 查询最大异或值
        long result = queryMax();
        out.println(result);
        
        out.flush();
        out.close();
        br.close();
    }
    
    // HDU 3949 XOR - 第k小异或值问题的解决方案
    public static void solveHDU3949() throws IOException {
        /**
         * HDU 3949 XOR 解题思路：
         * 1. 问题分析：
         *    - 需要求所有子集异或和中的第k小值
         *    - 线性基可以表示所有可能的子集异或和
         * 
         * 2. 解题步骤：
         *    - 构建线性基
         *    - 重组线性基，使其每个基向量的最高位唯一
         *    - 将k转换为二进制，根据二进制位选择对应的基向量
         * 
         * 3. 特殊情况处理：
         *    - 如果线性基的秩小于原数组长度，说明存在全0的情况
         *    - 需要考虑空集的情况（异或和为0）
         */
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        int T = Integer.parseInt(br.readLine());
        while (T-- > 0) {
            int n = Integer.parseInt(br.readLine());
            String[] parts = br.readLine().split(" ");
            
            // 初始化线性基
            Arrays.fill(basis, 0);
            
            // 构建线性基
            for (int i = 0; i < n; i++) {
                long x = Long.parseLong(parts[i]);
                insert(x);
            }
            
            // 重组线性基
            rebuild();
            
            // 判断是否有0解（即是否存在线性相关）
            boolean hasZero = false;
            for (int i = 0; i < MAXL; i++) {
                if (basis[i] == 0) {
                    hasZero = true;
                    break;
                }
            }
            
            int m = Integer.parseInt(br.readLine());
            while (m-- > 0) {
                long k = Long.parseLong(br.readLine());
                
                // 如果有0解，第k小相当于k--
                if (hasZero) {
                    if (k == 1) {
                        out.println(0);
                        continue;
                    }
                    k--;
                }
                
                long ans = queryKth(k);
                out.println(ans);
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
    
    // 牛客网 NC14533 异或和问题的解决方案
    public static void solveNiuke14533() throws IOException {
        /**
         * 牛客网 NC14533 异或和 解题思路：
         * 1. 问题分析：
         *    - 求所有连续子数组的异或和的和
         *    - 直接计算所有子数组的异或和会超时
         * 
         * 2. 优化方法：
         *    - 利用前缀异或性质：区间[l,r]的异或和等于prefix[r] ^ prefix[l-1]
         *    - 对于每一位，统计有多少个区间的异或和在该位上是1
         *    - 该位的总贡献为 1的个数 * 2^bit
         * 
         * 3. 复杂度分析：
         *    - 时间复杂度：O(n * log(max_value))
         *    - 空间复杂度：O(n)
         */
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        int n = Integer.parseInt(br.readLine());
        String[] parts = br.readLine().split(" ");
        long[] nums = new long[n];
        
        for (int i = 0; i < n; i++) {
            nums[i] = Long.parseLong(parts[i]);
        }
        
        long result = subarrayXorSum(nums);
        out.println(result);
        
        out.flush();
        out.close();
        br.close();
    }
}