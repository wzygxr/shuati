// BZOJ 2460 元素问题（线性基+贪心）
// 题目来源：BZOJ 2460 元素
// 题目链接：https://www.lydsy.com/JudgeOnline/problem.php?id=2460
// 题目描述：有n个二元组(ai, bi)，要求选出一些二元组，
// 使得这些二元组的a值异或和不为0，且b值和最大
// 算法：线性基 + 贪心
// 时间复杂度：O(n * log(n) + n * log(max_value))
// 空间复杂度：O(n + log(max_value))
// 测试链接 : https://www.lydsy.com/JudgeOnline/problem.php?id=2460

package class136;

import java.util.*;
import java.io.*;

public class Code07_Bzoj2460 {
    public static long[][] arr;    // [a, b]
    public static long[] basis;    // 线性基数组
    public static int n;           // 数组长度
    public static final int BIT = 60;  // 最大位数

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
    // 返回最大b值和
    // 算法思路：
    // 1. 按b值从大到小排序，贪心选择
    // 2. 清空线性基
    // 3. 依次尝试插入每个元素的a值
    // 4. 如果能成功插入，则选择该二元组
    public static long compute() {
        // 按b值从大到小排序
        Arrays.sort(arr, 0, n, (a, b) -> Long.compare(b[1], a[1]));
        
        // 清空线性基
        Arrays.fill(basis, 0);
        
        long ans = 0;
        // 依次尝试插入每个元素
        for (int i = 0; i < n; i++) {
            if (insert(arr[i][0])) {
                ans += arr[i][1];
            }
        }
        return ans;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = Integer.parseInt(br.readLine());
        arr = new long[n][2];
        basis = new long[BIT + 1];
        
        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            arr[i][0] = Long.parseLong(st.nextToken());  // a值
            arr[i][1] = Long.parseLong(st.nextToken());  // b值
        }
        
        out.println(compute());
        
        out.flush();
        out.close();
        br.close();
    }
}