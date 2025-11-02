package class136;

// 最大异或和问题
// 题目来源：洛谷P3812 【模板】线性基
// 题目链接：https://www.luogu.com.cn/problem/P3812
// 题目描述：给定n个整数（数字可能重复），求在这些数中选取任意个，使得他们的异或和最大。
// 算法：线性基（普通消元法）
// 时间复杂度：O(n * log(max_value))
// 空间复杂度：O(log(max_value))
// 测试链接 : https://www.luogu.com.cn/problem/P3812
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code01_MaximumXor {

    public static int MAXN = 51;        // 最大数组长度

    public static int BIT = 50;         // 最大位数，因为arr[i] <= 2^50

    public static long[] arr = new long[MAXN];  // 存储输入数组

    public static long[] basis = new long[BIT + 1];  // 线性基数组，basis[i]表示第i位的基

    public static int n;                // 数组长度

    /**
     * 普通消元法构建线性基并计算最大异或和
     * 
     * 算法思路：
     * 1. 从最高位开始扫描每个数
     * 2. 对于每个数，尝试将其插入到线性基中
     * 3. 插入过程：从高位到低位扫描，如果当前位为1且线性基中该位为空，则直接插入；否则用线性基中该位的数异或当前数，继续处理
     * 4. 构建完成后，从高位到低位贪心地选择线性基中的元素，使异或和最大
     * 
     * 时间复杂度分析：
     * - 构建线性基：O(n * BIT) = O(n * log(max_value))
     * - 查询最大异或值：O(BIT) = O(log(max_value))
     * - 总时间复杂度：O(n * log(max_value))
     * 
     * 空间复杂度分析：
     * - 线性基数组：O(BIT) = O(log(max_value))
     * - 输入数组：O(n)
     * - 总空间复杂度：O(n + log(max_value))
     * 
     * 最优解证明：
     * 线性基算法是解决最大异或和问题的最优解，因为：
     * 1. 线性基能够表示原数组的所有异或组合
     * 2. 贪心选择保证了最大异或值的正确性
     * 3. 时间复杂度已经达到理论下界
     * 
     * @return 最大异或和
     */
    public static long compute() {
        // 构建线性基：将数组中的每个数插入线性基
        for (int i = 1; i <= n; i++) {
            insert(arr[i]);
        }
        
        // 贪心选择：从高位到低位，尽可能让每一位为1
        long ans = 0;
        for (int i = BIT; i >= 0; i--) {
            // 如果当前位为0，尝试异或basis[i]使其变为1
            // 如果当前位为1，保持不动（因为1比0大）
            ans = Math.max(ans, ans ^ basis[i]);
        }
        return ans;
    }

    /**
     * 线性基插入操作
     * 
     * 算法思路：
     * 1. 从最高位到最低位扫描数字的二进制位
     * 2. 如果当前位为1：
     *    - 如果线性基中该位为空，则将当前数插入该位置
     *    - 否则用线性基中该位的数异或当前数，继续处理低位
     * 3. 如果成功插入返回true，否则返回false（表示该数可由现有线性基表示）
     * 
     * 时间复杂度：O(BIT) = O(log(max_value))
     * 空间复杂度：O(1)
     * 
     * 关键细节：
     * - 从高位到低位处理：保证线性基中每个基的最高位唯一
     * - 异或操作：消除当前数中与现有基重叠的部分
     * - 返回值：true表示线性基增加了新基，false表示该数线性相关
     * 
     * 异常场景处理：
     * - 输入为0：直接返回false（0可由空集表示）
     * - 输入为负数：Java中右移操作会保留符号位，需要特别注意
     * 
     * @param num 要插入的数字
     * @return 如果线性基增加了新基返回true，否则返回false
     */
    public static boolean insert(long num) {
        // 边界情况：如果num为0，直接返回false（0可由空集表示）
        if (num == 0) {
            return false;
        }
        
        // 从最高位到最低位扫描
        for (int i = BIT; i >= 0; i--) {
            // 检查第i位是否为1（注意：这里应该使用位与操作，不是相等判断）
            if ((num >> i & 1) == 1) {
                if (basis[i] == 0) {
                    // 当前位为空，插入新基
                    basis[i] = num;
                    return true;
                } else {
                    // 当前位已有基，用该基异或当前数，消除当前位
                    num ^= basis[i];
                }
            }
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        in.nextToken();
        n = (int) in.nval;
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            arr[i] = (long) in.nval;
        }
        // 清空线性基数组
        for (int i = 0; i <= BIT; i++) {
            basis[i] = 0;
        }
        out.println(compute());
        out.flush();
        out.close();
        br.close();
    }
    
    // ============== LeetCode 421. 数组中两个数的最大异或值 ==============
    // 题目链接：https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
    // 题目描述：给你一个整数数组 nums，返回 nums[i] XOR nums[j] 的最大运算结果，其中 0 ≤ i ≤ j < n。
    // 算法：线性基 / 字典树
    // 时间复杂度：O(n * 32)
    // 空间复杂度：O(32)
    // 这个题目可以用线性基解决，也可以用字典树解决
    public static int findMaximumXORLeetCode421(int[] nums) {
        // 构建线性基
        long[] basis421 = new long[32]; // 因为nums[i] <= 2^31 - 1
        
        // 插入所有数字到线性基
        for (int num : nums) {
            long x = num;
            for (int i = 31; i >= 0; i--) {
                if ((x >> i & 1) == 1) {
                    if (basis421[i] == 0) {
                        basis421[i] = x;
                        break;
                    } else {
                        x ^= basis421[i];
                    }
                }
            }
        }
        
        // 计算最大异或值
        int maxXor = 0;
        for (int num : nums) {
            long current = num;
            for (int i = 31; i >= 0; i--) {
                // 尝试让当前位为1
                if ((current >> i & 1) == 0 && basis421[i] != 0) {
                    current ^= basis421[i];
                }
            }
            maxXor = Math.max(maxXor, (int)current);
        }
        
        return maxXor;
    }
    
    // ============== LeetCode 1738. 找出第 K 大的异或坐标值 ==============
    // 题目链接：https://leetcode.cn/problems/find-kth-largest-xor-coordinate-value/
    // 题目描述：给你一个二维矩阵 matrix 和一个整数 k ，矩阵大小为 m x n 由非负整数组成。
    // 要求找到矩阵中所有可能的异或坐标值中的第 k 大的值。
    // 算法：二维前缀异或和 + 排序
    // 时间复杂度：O(m*n + m*n*log(m*n))
    // 空间复杂度：O(m*n)
    public static int kthLargestValueLeetCode1738(int[][] matrix, int k) {
        int m = matrix.length;
        int n = matrix[0].length;
        int[][] xorSum = new int[m][n];
        
        // 计算二维前缀异或和
        xorSum[0][0] = matrix[0][0];
        for (int i = 1; i < m; i++) {
            xorSum[i][0] = xorSum[i-1][0] ^ matrix[i][0];
        }
        for (int j = 1; j < n; j++) {
            xorSum[0][j] = xorSum[0][j-1] ^ matrix[0][j];
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                xorSum[i][j] = xorSum[i-1][j-1] ^ xorSum[i-1][j] ^ xorSum[i][j-1] ^ matrix[i][j];
            }
        }
        
        // 收集所有异或值并排序
        int[] allValues = new int[m * n];
        int index = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                allValues[index++] = xorSum[i][j];
            }
        }
        
        // 排序并返回第k大的值
        java.util.Arrays.sort(allValues);
        return allValues[m * n - k];
    }

    /*
     * 线性基算法详解
     * 
     * 线性基（Linear Basis）是一种处理异或问题的重要数据结构，主要用于解决以下几类问题：
     * 1. 求n个数中选取任意个数异或能得到的最大值
     * 2. 求n个数中选取任意个数异或能得到的第k小值
     * 3. 判断一个数是否能由给定数组中的数异或得到
     * 4. 求能异或得到的数的个数
     * 
     * 核心思想
     * 
     * 线性基类似于线性代数中的基向量概念，它是一组线性无关的向量集合，
     * 能够表示原集合中所有数的异或组合。线性基有以下重要性质：
     * 
     * 1. 原序列中的任意一个数都可以由线性基中的某些数异或得到
     * 2. 线性基中的任意一些数异或起来都不能得到0
     * 3. 在保持性质1的前提下，线性基中的数的个数是最少的
     * 4. 线性基中每个元素的二进制最高位互不相同
     * 
     * 线性基的构建方法
     * 
     * 线性基的构建主要有两种方法：普通消元法和高斯消元法。
     * 
     * 普通消元法
     * 
     * 普通消元法是最常用的构建线性基的方法，其基本思路是：
     * 
     * 1. 从最高位开始扫描
     * 2. 对于每个数，尝试将其插入到线性基中
     * 3. 插入过程：从高位到低位扫描，如果当前位为1且线性基中该位为空，
     *    则直接插入；否则用线性基中该位的数异或当前数，继续处理
     * 
     * 时间复杂度分析：
     * - 构建线性基：O(n * log(max_value))，其中n为数组长度，max_value为数组中的最大值
     * - 查询最大异或值：O(log(max_value))
     * 
     * 空间复杂度分析：
     * - O(log(max_value))，用于存储线性基
     * 
     * 相关题目：
     * 1. https://www.luogu.com.cn/problem/P3812 - 线性基模板题（最大异或和）
     * 2. https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/ - LeetCode 421. 数组中两个数的最大异或值
     * 3. https://leetcode.cn/problems/find-kth-largest-xor-coordinate-value/ - LeetCode 1738. 找出第 K 大的异或坐标值
     * 4. https://loj.ac/p/114 - 第k小异或和
     * 5. https://www.luogu.com.cn/problem/P4570 - 元素（线性基+贪心）
     * 6. https://www.luogu.com.cn/problem/P3857 - 彩灯（线性基应用）
     * 7. https://www.luogu.com.cn/problem/P4151 - 最大XOR和路径
     * 8. https://www.luogu.com.cn/problem/P4301 - 最大异或和（可持久化线性基）
     * 9. https://www.luogu.com.cn/problem/P3292 - 幸运数字（线性基+倍增）
     * 10. http://acm.hdu.edu.cn/showproblem.php?pid=3949 - HDU 3949 XOR
     * 11. https://codeforces.com/problemset/problem/1101/G - Codeforces 1101G (Zero XOR Subset)-less
     */
}