package class137;

import java.io.*;
import java.util.*;

/**
 * CodeChef 111506 - XOR AND OR Problem
 * 
 * 问题描述：
 * 给定一个长度为n的数组，求所有可能的子序列的异或和的最大值。
 * 
 * 算法分析：
 * - 使用线性基数据结构来解决这个问题
 * - 线性基可以高效地处理异或相关的最大值查询问题
 * - 时间复杂度：O(n * log(max_value))，其中log(max_value)是处理每一位的时间
 * - 空间复杂度：O(log(max_value))，用于存储线性基
 * 
 * 优化技巧：
 * - 从高位到低位构建线性基，确保贪心策略的正确性
 * - 对于每一位，尽可能保留最高位的1，这样可以得到最大的异或结果
 * 
 * 相关知识点：
 * - 线性基的构建与应用
 * - 异或运算的性质
 * - 贪心算法
 * 
 * 作者：Linear Basis Team
 * 日期：2023-11-15
 */

public class CodeChef111506_XORANDOR {
    // 定义线性基数组的最大位数（这里假设处理64位整数）
    private static final int MAX_BIT = 63;
    // 线性基数组，basis[i]表示最高位为i的基
    private static long[] basis = new long[MAX_BIT + 1];
    
    /**
     * 向线性基中插入一个数
     * 
     * @param num 要插入的数
     * @return 如果成功插入（该数与现有基线性无关）返回true，否则返回false
     */
    public static boolean insert(long num) {
        // 从最高位开始处理
        for (int i = MAX_BIT; i >= 0; i--) {
            // 如果当前位是1
            if (((num >> i) & 1) == 1) {
                // 如果该位的基不存在，则直接插入
                if (basis[i] == 0) {
                    basis[i] = num;
                    return true;
                }
                // 否则，用当前基异或num，继续处理
                num ^= basis[i];
            }
        }
        // 如果num变成了0，说明它可以由现有的基异或得到，即线性相关
        return false;
    }
    
    /**
     * 查询线性基能表示的最大异或和
     * 
     * @return 最大的异或和
     */
    public static long queryMax() {
        long res = 0;
        // 从最高位开始，贪心选择是否异或当前基
        for (int i = MAX_BIT; i >= 0; i--) {
            // 如果异或后结果更大，则选择异或
            if ((res ^ basis[i]) > res) {
                res ^= basis[i];
            }
        }
        return res;
    }
    
    /**
     * 重置线性基
     */
    public static void resetBasis() {
        Arrays.fill(basis, 0);
    }
    
    /**
     * 主函数，读取输入并处理
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));
        StringTokenizer st;
        
        // 读取测试用例数量
        int t = Integer.parseInt(br.readLine());
        
        for (int caseNum = 0; caseNum < t; caseNum++) {
            // 重置线性基
            resetBasis();
            
            // 读取数组长度
            int n = Integer.parseInt(br.readLine());
            
            // 读取数组元素并插入线性基
            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < n; i++) {
                long num = Long.parseLong(st.nextToken());
                insert(num);
            }
            
            // 查询并输出最大异或和
            pw.println(queryMax());
        }
        
        pw.flush();
        pw.close();
        br.close();
    }
}