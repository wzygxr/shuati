package class136;

// HDU 3949 XOR问题（线性基求第k小异或和）
// 题目来源：HDU 3949 XOR
// 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=3949
// 题目描述：给定n个数，求这些数能异或出的第k小值
// 算法：线性基（高斯消元法）
// 时间复杂度：构建线性基O(n * log(max_value))，单次查询O(log(max_value))
// 空间复杂度：O(log(max_value))
// 测试链接 : http://acm.hdu.edu.cn/showproblem.php?pid=3949

import java.util.*;
import java.io.*;

public class Code05_Hdu3949Xor {
    public static long[] arr;      // 输入数组
    public static long[] basis;    // 线性基数组
    public static int len;         // 线性基的大小
    public static boolean zero;    // 是否能异或出0
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

    // 高斯消元构建线性基
    // 算法思路：
    // 1. 先用普通消元法构建线性基
    // 2. 再用高斯消元法整理线性基，使其具有阶梯状结构
    // 3. 重新整理basis数组，把非0的元素移到前面
    // 4. 判断是否能异或出0
    public static void compute() {
        // 清空线性基
        Arrays.fill(basis, 0);
        len = 0;
        zero = false;
        
        // 先用普通消元法构建线性基
        for (int i = 0; i < n; i++) {
            insert(arr[i]);
        }
        
        // 再用高斯消元法整理线性基，使其具有阶梯状结构
        for (int i = 0; i <= BIT; i++) {
            for (int j = i + 1; j <= BIT; j++) {
                if ((basis[j] & (1L << i)) != 0) {
                    basis[j] ^= basis[i];
                }
            }
        }
        
        // 重新整理basis数组，把非0的元素移到前面
        long[] tempBasis = new long[BIT + 1];
        int tempLen = 0;
        for (int i = 0; i <= BIT; i++) {
            if (basis[i] != 0) {
                tempBasis[tempLen++] = basis[i];
            }
        }
        
        // 复制回原数组
        Arrays.fill(basis, 0);
        System.arraycopy(tempBasis, 0, basis, 0, tempLen);
        len = tempLen;
        
        // 判断是否能异或出0
        zero = (len != n);
    }

    // 返回第k小的异或和
    // 算法思路：
    // 1. 特殊情况处理：如果能异或出0，0是第1小的
    // 2. 如果能异或出0，且k=1，返回0
    // 3. 如果能异或出0，且k>1，将k减1后继续处理
    // 4. 根据k的二进制表示选择线性基中的元素进行异或
    public static long query(long k) {
        // 异常处理：k必须大于0
        if (k <= 0) {
            throw new IllegalArgumentException("k must be positive");
        }
        
        // 如果能异或出0，那么0是第1小的
        if (zero) {
            if (k == 1) {
                return 0;
            }
            k--;  // 跳过0
        }
        
        // 如果k超过了可能的异或和数量，返回-1
        if (k > (1L << len)) {
            return -1;
        }
        
        long ans = 0;
        // 根据k的二进制表示选择线性基中的元素进行异或
        for (int i = 0; i < len; i++) {
            if ((k & (1L << i)) != 0) {
                ans ^= basis[i];
            }
        }
        return ans;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        int t = Integer.parseInt(br.readLine());
        
        for (int cases = 1; cases <= t; cases++) {
            out.println("Case #" + cases + ":");
            
            n = Integer.parseInt(br.readLine());
            arr = new long[n];
            basis = new long[BIT + 1];
            
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int i = 0; i < n; i++) {
                arr[i] = Long.parseLong(st.nextToken());
            }
            
            compute();
            
            int q = Integer.parseInt(br.readLine());
            for (int i = 0; i < q; i++) {
                long k = Long.parseLong(br.readLine());
                try {
                    out.println(query(k));
                } catch (IllegalArgumentException e) {
                    out.println(-1);  // 发生异常时输出-1
                }
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}
