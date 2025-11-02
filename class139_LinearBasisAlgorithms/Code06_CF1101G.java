// Codeforces 1101G (Zero XOR Subset)-less问题（线性基应用）
// 题目来源：Codeforces 1101G (Zero XOR Subset)-less
// 题目链接：https://codeforces.com/problemset/problem/1101/G
// 题目描述：给定一个长度为n的数组，将数组分成尽可能多的段，
// 使得每一段的异或和都不为0，求最多能分成多少段
// 算法：线性基
// 时间复杂度：O(n * log(max_value))
// 空间复杂度：O(n + log(max_value))
// 测试链接 : https://codeforces.com/problemset/problem/1101/G

package class136;

import java.util.*;
import java.io.*;

public class Code06_CF1101G {
    public static int[] arr;      // 输入数组
    public static int[] prefix;   // 前缀异或和数组
    public static long[] basis;   // 线性基数组
    public static int n;          // 数组长度
    public static final int BIT = 30;  // 最大位数

    // 线性基里插入num，如果线性基增加了返回true，否则返回false
    public static boolean insert(long num) {
        for (int i = BIT; i >= 0; i--) {
            if ((num >> i & 1) == 1) {
                if (basis[i] == 0) {
                    basis[i] = num;
                    return true;
                }
                num ^= basis[i];
            }
        }
        return false;
    }

    // 普通消元法构建线性基
    // 返回线性基的大小
    // 算法思路：
    // 1. 清空线性基
    // 2. 将所有前缀异或和插入线性基
    // 3. 返回线性基的大小
    public static int compute() {
        // 清空线性基
        Arrays.fill(basis, 0);
        
        int size = 0;
        // 将所有前缀异或和插入线性基
        for (int i = 0; i <= n; i++) {
            if (insert(prefix[i])) {
                size++;
            }
        }
        return size;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = Integer.parseInt(br.readLine());
        arr = new int[n];
        prefix = new int[n + 1];
        basis = new long[BIT + 1];
        
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }
        
        // 计算前缀异或和
        prefix[0] = 0;
        for (int i = 1; i <= n; i++) {
            prefix[i] = prefix[i - 1] ^ arr[i - 1];
        }
        
        // 特殊情况处理：如果整个数组的异或和为0，则无法分割，返回-1
        if (prefix[n] == 0) {
            out.println(-1);
        } else {
            // 否则答案为线性基大小减1（因为线性基中包含0）
            int result = compute() - 1;
            out.println(result);
        }
        
        out.flush();
        out.close();
        br.close();
    }
}