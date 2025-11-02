package class137;

// 最大异或和 (线性基模板题)
// 给定一个长度为n的数组arr，arr中都是long类型的非负数，可能有重复值
// 在这些数中选取任意个，使得异或和最大，返回最大的异或和
// 1 <= n <= 50
// 0 <= arr[i] <= 2^50
// 测试链接 : https://www.luogu.com.cn/problem/P3812
// 请务必在原有代码基础上增加详细注释，确保代码可以编译运行且没有错误

import java.io.*;
import java.util.*;

public class P3812_MaximumXor {
    
    public static int MAXN = 51;
    public static int BIT = 50;
    
    public static long[] arr = new long[MAXN];
    public static long[] basis = new long[BIT + 1];
    public static int n;
    
    /**
     * 计算最大异或和
     * 算法思路：
     * 1. 构建线性基
     * 2. 贪心地从高位到低位选择线性基中的元素来最大化结果
     * 时间复杂度：O(n * BIT)
     * 空间复杂度：O(BIT)
     * @return 最大的异或和
     */
    public static long compute() {
        // 构建线性基
        for (int i = 1; i <= n; i++) {
            insert(arr[i]);
        }
        
        // 贪心地选择元素来最大化异或和
        long ans = 0;
        for (int i = BIT; i >= 0; i--) {
            ans = Math.max(ans, ans ^ basis[i]);
        }
        
        return ans;
    }
    
    /**
     * 将数字插入线性基
     * 算法思路：
     * 1. 从高位到低位扫描
     * 2. 如果当前位为1且线性基中该位为空，则直接插入
     * 3. 否则用线性基中该位的数异或当前数，继续处理
     * @param num 要插入的数字
     * @return 如果成功插入返回true，否则返回false
     */
    public static boolean insert(long num) {
        for (int i = BIT; i >= 0; i--) {
            if (((num >> i) & 1) != 0) {
                if (basis[i] == 0) {
                    basis[i] = num;
                    return true;
                }
                num ^= basis[i];
            }
        }
        return false;
    }
    
    /**
     * 主函数
     * 读取输入数据，调用计算函数，输出结果
     */
    public static void main(String[] args) throws IOException {
        // 使用BufferedReader提高输入效率
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        
        // 读取输入
        n = Integer.parseInt(br.readLine());
        st = new StringTokenizer(br.readLine());
        
        // 将输入数据存储到arr数组中
        for (int i = 1; i <= n; i++) {
            arr[i] = Long.parseLong(st.nextToken());
        }
        
        // 清空线性基数组
        Arrays.fill(basis, 0);
        
        // 计算并输出结果
        System.out.println(compute());
    }
}