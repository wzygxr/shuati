// DZY Loves Colors - 分块算法实现 (Java版本)
// 题目来源: http://codeforces.com/contest/444/problem/C
// 题目大意: 给定一个长度为n的数组，初始时每个位置i的颜色为i，颜色变化量为0
// 支持两种操作：
// 1. 将区间[l, r]的所有位置染成颜色x，并累加每个位置的颜色变化量为|原来的颜色 - x|
// 2. 查询区间[l, r]的颜色变化量总和
// 约束条件: 1 <= n, m <= 1e5

import java.io.*;
import java.util.*;

public class Code31_DZYLovesColors {
    static final int MAXN = 100005;
    static int n, m, blen; // blen为块的大小
    static int[] arr = new int[MAXN]; // 存储每个位置的当前颜色
    static long[] sum = new long[MAXN]; // 存储每个位置的颜色变化量
    static long[] tag; // 每个块的整体颜色变化量标记
    static int[] color_tag; // 每个块的颜色标记，如果块内颜色相同则非-1
    
    // 查询操作：查询区间[l, r]的颜色变化量总和
    static long query(int l, int r) {
        long ans = 0;
        int L = (l - 1) / blen + 1; // l所在的块号
        int R = (r - 1) / blen + 1; // r所在的块号
        
        // 处理左边不完整的块
        if (L == R) {
            for (int i = l; i <= r; i++) {
                ans += sum[i] + tag[L];
            }
            return ans;
        }
        
        // 处理左边不完整的块
        for (int i = l; i <= L * blen; i++) {
            ans += sum[i] + tag[L];
        }
        
        // 处理中间完整的块
        for (int i = L + 1; i <= R - 1; i++) {
            ans += tag[i] * blen;
        }
        
        // 处理右边不完整的块
        for (int i = (R - 1) * blen + 1; i <= r; i++) {
            ans += sum[i] + tag[R];
        }
        
        return ans;
    }
    
    // 更新操作：将区间[l, r]染成颜色x
    static void update(int l, int r, int x) {
        int L = (l - 1) / blen + 1; // l所在的块号
        int R = (r - 1) / blen + 1; // r所在的块号
        
        // 处理左边不完整的块
        if (L == R) {
            // 如果当前块有颜色标记（颜色相同）
            if (color_tag[L] != -1) {
                int old_color = color_tag[L];
                // 将整个块的颜色展开
                for (int i = (L - 1) * blen + 1; i <= L * blen && i <= n; i++) {
                    arr[i] = old_color;
                }
                color_tag[L] = -1; // 清除颜色标记
            }
            
            // 暴力更新区间内的元素
            for (int i = l; i <= r; i++) {
                sum[i] += Math.abs(arr[i] - x);
                arr[i] = x;
            }
            
            // 检查更新后是否整个块颜色一致
            boolean same = true;
            int first = arr[(L - 1) * blen + 1];
            for (int i = (L - 1) * blen + 2; i <= L * blen && i <= n; i++) {
                if (arr[i] != first) {
                    same = false;
                    break;
                }
            }
            if (same) {
                color_tag[L] = first;
            }
            return;
        }
        
        // 处理左边不完整的块
        if (color_tag[L] != -1) {
            int old_color = color_tag[L];
            for (int i = (L - 1) * blen + 1; i <= L * blen && i <= n; i++) {
                arr[i] = old_color;
            }
            color_tag[L] = -1;
        }
        for (int i = l; i <= L * blen; i++) {
            sum[i] += Math.abs(arr[i] - x);
            arr[i] = x;
        }
        
        // 检查更新后左边块是否颜色一致
        boolean same_L = true;
        int first_L = arr[(L - 1) * blen + 1];
        for (int i = (L - 1) * blen + 2; i <= L * blen && i <= n; i++) {
            if (arr[i] != first_L) {
                same_L = false;
                break;
            }
        }
        if (same_L) {
            color_tag[L] = first_L;
        }
        
        // 处理中间完整的块
        for (int i = L + 1; i <= R - 1; i++) {
            if (color_tag[i] != -1) {
                // 如果块内颜色相同，直接累加变化量
                tag[i] += Math.abs(color_tag[i] - x);
                color_tag[i] = x; // 更新颜色标记
            } else {
                // 块内颜色不同，需要暴力处理
                for (int j = (i - 1) * blen + 1; j <= i * blen && j <= n; j++) {
                    sum[j] += tag[i]; // 先应用之前的tag
                    sum[j] += Math.abs(arr[j] - x);
                    arr[j] = x;
                }
                tag[i] = 0; // 清除tag
                color_tag[i] = x; // 设置颜色标记
            }
        }
        
        // 处理右边不完整的块
        if (color_tag[R] != -1) {
            int old_color = color_tag[R];
            for (int i = (R - 1) * blen + 1; i <= R * blen && i <= n; i++) {
                arr[i] = old_color;
            }
            color_tag[R] = -1;
        }
        for (int i = (R - 1) * blen + 1; i <= r; i++) {
            sum[i] += Math.abs(arr[i] - x);
            arr[i] = x;
        }
        
        // 检查更新后右边块是否颜色一致
        boolean same_R = true;
        int first_R = arr[(R - 1) * blen + 1];
        for (int i = (R - 1) * blen + 2; i <= R * blen && i <= n; i++) {
            if (arr[i] != first_R) {
                same_R = false;
                break;
            }
        }
        if (same_R) {
            color_tag[R] = first_R;
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] s = br.readLine().split(" ");
        n = Integer.parseInt(s[0]);
        m = Integer.parseInt(s[1]);
        
        blen = (int)Math.sqrt(n);
        if (blen == 0) blen = 1; // 避免除零错误
        
        int block_count = (n + blen - 1) / blen;
        tag = new long[block_count + 2]; // 块编号从1开始
        color_tag = new int[block_count + 2];
        Arrays.fill(color_tag, -1); // -1表示块内颜色不一致
        
        // 初始化数组
        for (int i = 1; i <= n; i++) {
            arr[i] = i; // 初始颜色为位置i
            sum[i] = 0; // 初始颜色变化量为0
        }
        
        // 初始时每个块可能颜色一致
        for (int i = 1; i <= block_count; i++) {
            boolean same = true;
            int first = arr[(i - 1) * blen + 1];
            for (int j = (i - 1) * blen + 2; j <= i * blen && j <= n; j++) {
                if (arr[j] != first) {
                    same = false;
                    break;
                }
            }
            if (same) {
                color_tag[i] = first;
            }
        }
        
        // 处理m次操作
        while (m-- > 0) {
            s = br.readLine().split(" ");
            int op = Integer.parseInt(s[0]);
            if (op == 1) {
                // 更新操作
                int l = Integer.parseInt(s[1]);
                int r = Integer.parseInt(s[2]);
                int x = Integer.parseInt(s[3]);
                update(l, r, x);
            } else {
                // 查询操作
                int l = Integer.parseInt(s[1]);
                int r = Integer.parseInt(s[2]);
                pw.println(query(l, r));
            }
        }
        
        pw.flush();
        pw.close();
        br.close();
    }
    
    /*
     * 时间复杂度分析：
     * - 预处理：O(n)
     * - 更新操作：O(√n) 每个完整块O(1)，不完整块O(√n)
     * - 查询操作：O(√n) 每个完整块O(1)，不完整块O(√n)
     * - 总体时间复杂度：O(m√n)，其中m为操作次数
     * 
     * 空间复杂度分析：
     * - 数组arr：O(n)
     * - 数组sum：O(n)
     * - 数组tag：O(√n)
     * - 数组color_tag：O(√n)
     * - 总体空间复杂度：O(n)
     * 
     * Java语言特性注意事项：
     * 1. 输入输出使用BufferedReader和PrintWriter以提高效率
     * 2. 数组大小需要根据n动态调整，避免内存浪费
     * 3. 注意int和long的转换，避免溢出
     */
}