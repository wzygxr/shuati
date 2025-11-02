package class136;

// 彩灯问题（线性基应用）
// 题目来源：洛谷P3857 [TJOI2008] 彩灯
// 题目链接：https://www.luogu.com.cn/problem/P3857
// 题目描述：一共有n个灯泡，开始都是不亮的状态，有m个开关
// 每个开关能改变若干灯泡的状态，改变是指，亮变不亮、不亮变亮
// 比如n=5，某个开关为XXOOO，表示这个开关只能改变后3个灯泡的状态
// 可以随意使用开关，返回有多少种亮灯的组合，全不亮也算一种组合
// 答案可能很大，对 2008 取模
// 算法：线性基
// 时间复杂度：O(m * n)
// 空间复杂度：O(n)
// 测试链接 : https://www.luogu.com.cn/problem/P3857

import java.util.*;

public class Code04_Lanterns {
    public static long[] switches;  // 开关影响数组
    public static long[] basis;     // 线性基数组
    public static int n, m;         // 灯泡数量和开关数量
    public static final int MOD = 2008;  // 模数

    // 线性基里插入num，如果线性基增加了返回true，否则返回false
    public static boolean insert(long num) {
        for (int i = n - 1; i >= 0; i--) {
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

    // 计算线性基的大小
    // 算法思路：
    // 1. 清空线性基
    // 2. 将所有开关模式插入线性基
    // 3. 返回线性基的大小
    public static int compute() {
        // 清空线性基
        Arrays.fill(basis, 0);
        
        int size = 0;
        for (int i = 0; i < m; i++) {
            if (insert(switches[i])) {
                size++;
            }
        }
        
        return size;
    }

    // 计算结果
    // 算法思路：
    // 1. 计算线性基的大小
    // 2. 答案就是2^(线性基大小) % MOD
    public static int calculateResult() {
        int size = compute();
        // 计算2^size % MOD
        int result = 1;
        for (int i = 0; i < size; i++) {
            result = (result * 2) % MOD;
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        m = scanner.nextInt();
        
        switches = new long[m];
        basis = new long[n];
        
        for (int i = 0; i < m; i++) {
            String pattern = scanner.next();
            long num = 0;
            for (int j = 0; j < n; j++) {
                if (pattern.charAt(j) == 'O') {
                    num |= 1L << j;
                }
            }
            switches[i] = num;
        }
        
        System.out.println(calculateResult());
        scanner.close();
    }
}
