package class145;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

/**
 * 错排问题（Derangement Problem）
 * 题目：洛谷 P1595 信封问题
 * 链接：https://www.luogu.com.cn/problem/P1595
 * 描述：n个人写信，求所有人都没有收到自己信的方案数
 * 
 * 错排问题背景：
 * - 也称为信封问题，是组合数学中的一个经典问题
 * - 要求找到排列中所有元素都不在原来位置上的排列数
 * - 时间限制：1 <= n <= 20（数据范围较小，可以使用长整型计算）
 */
public class Code01_Derangement {
    
    /**
     * 程序主入口
     * 
     * @param args 命令行参数
     * @throws IOException 输入输出异常
     */
    public static void main(String[] args) throws IOException {
        // 使用快速输入方式处理输入数据
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取输入的n值
        in.nextToken();
        int n = (int) in.nval;
        
        // 调用错排计算方法并输出结果
        // 两种方法都可以使用，这里使用二项式反演方法
        out.println(ways2(n));
        
        // 确保输出缓冲区被刷新并关闭资源
        out.flush();
        out.close();
        br.close();
    }

    /**
     * 计算错排数 - 动态规划方法
     * 
     * 算法原理：
     * - 使用递推关系式：D(n) = (n-1) * (D(n-1) + D(n-2))
     * - 推导思路：第n个元素必须放在前n-1个位置中的某一个位置k
     *   1. 如果第k个元素放在第n个位置，则剩下n-2个元素需要错排
     *   2. 如果第k个元素不放在第n个位置，则剩下的问题相当于n-1个元素的错排
     * 
     * 时间复杂度：O(n) - 只需要一次遍历计算每个位置的错排数
     * 空间复杂度：O(n) - 需要一个长度为n+1的数组存储中间结果
     * 
     * @param n 元素个数
     * @return n个元素的错排数
     */
    public static long ways1(int n) {
        // 边界条件检查
        if (n <= 0) {
            return 1; // 0个元素的错排数定义为1（空排列）
        }
        
        // 动态规划数组：dp[i]表示i个元素的错排数
        long[] dp = new long[n + 1];
        
        // 填充dp数组
        for (int i = 1; i <= n; i++) {
            if (i == 1) {
                dp[i] = 0;  // 1个元素无法错排
            } else if (i == 2) {
                dp[i] = 1;  // 2个元素只有一种错排方式：(2,1)
            } else {
                // 应用递推公式
                dp[i] = (i - 1) * (dp[i - 1] + dp[i - 2]);
            }
        }
        
        return dp[n];
    }

    /**
     * 计算错排数 - 二项式反演/容斥原理方法
     * 
     * 算法原理：
     * - 使用容斥原理计算：D(n) = n! * Σ(i=0 to n) ((-1)^i / i!)
     * - 推导思路：计算所有排列数减去至少有一个元素在原位置的排列数
     *   加上至少有两个元素在原位置的排列数，依此类推（容斥原理）
     * 
     * 时间复杂度：O(n) - 计算阶乘和累加各项都只需要O(n)时间
     * 空间复杂度：O(1) - 只需要常数级额外空间
     * 
     * @param n 元素个数
     * @return n个元素的错排数
     */
    public static long ways2(int n) {
        // 边界条件检查
        if (n <= 0) {
            return 1; // 0个元素的错排数定义为1
        }
        
        // 计算n的阶乘
        long facn = 1; // n!
        for (int i = 1; i <= n; i++) {
            facn *= i;
        }
        
        // 初始化为i=0时的项，即n!
        long ans = facn;
        
        // 计算i!并逐项累加
        long faci = 1; // i!
        for (int i = 1; i <= n; i++) {
            // 计算i的阶乘
            faci = faci * i;
            
            // 计算项：(-1)^i * (n! / i!)
            if ((i & 1) == 0) {
                // i为偶数，(-1)^i = 1，加上该值
                ans += facn / faci;
            } else {
                // i为奇数，(-1)^i = -1，减去该值
                ans -= facn / faci;
            }
        }
        
        return ans;
    }
    
    /**
     * 计算错排数 - 空间优化的动态规划方法
     * 
     * 算法原理：基于递推式，但只保存前两个状态
     * 时间复杂度：O(n)
     * 空间复杂度：O(1) - 只需要常数级额外空间
     * 
     * 工程化考虑：
     * - 当n较大时，该方法比ways1更节省内存
     * - 但在本题数据范围(n<=20)内，内存优化效果不明显
     * 
     * @param n 元素个数
     * @return n个元素的错排数
     */
    public static long ways3(int n) {
        // 边界条件处理
        if (n <= 0) return 1;
        if (n == 1) return 0;
        if (n == 2) return 1;
        
        // 只保存前两个状态
        long prev1 = 1; // D(2)
        long prev2 = 0; // D(1)
        long curr = 0;  // 当前计算的D(n)
        
        // 从3开始递推计算
        for (int i = 3; i <= n; i++) {
            curr = (i - 1) * (prev1 + prev2);
            // 更新状态
            prev2 = prev1;
            prev1 = curr;
        }
        
        return curr;
    }
}