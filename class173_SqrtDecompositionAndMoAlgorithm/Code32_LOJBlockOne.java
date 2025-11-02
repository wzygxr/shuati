// LOJ 分块一 - 分块算法实现 (Java版本)
// 题目来源: https://loj.ac/problem/6277
// 题目大意: 给定一个长度为n的数组，支持两种操作：
// 1. 区间[l, r]每个数加k
// 2. 查询位置p的值
// 约束条件: 1 <= n, m <= 1e5

import java.io.*;
import java.util.*;

public class Code32_LOJBlockOne {
    static final int MAXN = 100005;
    static int n, m, blen; // blen为块的大小
    static long[] arr = new long[MAXN]; // 原始数组
    static long[] tag; // 块的懒标记
    
    // 获取元素p的值（考虑懒标记）
    static long get(int p) {
        int block = (p - 1) / blen + 1; // 计算p所在的块号
        return arr[p] + tag[block];
    }
    
    // 区间更新：将[l, r]区间内的每个数加上k
    static void updateRange(int l, int r, long k) {
        int L = (l - 1) / blen + 1; // l所在的块号
        int R = (r - 1) / blen + 1; // r所在的块号
        
        // 如果l和r在同一个块内，直接暴力更新
        if (L == R) {
            for (int i = l; i <= r; i++) {
                arr[i] += k;
            }
            return;
        }
        
        // 暴力更新左边不完整的块
        for (int i = l; i <= L * blen; i++) {
            arr[i] += k;
        }
        
        // 对中间完整的块打标记
        for (int i = L + 1; i <= R - 1; i++) {
            tag[i] += k;
        }
        
        // 暴力更新右边不完整的块
        for (int i = (R - 1) * blen + 1; i <= r; i++) {
            arr[i] += k;
        }
    }
    
    // 单点查询：查询位置p的值
    static long queryPoint(int p) {
        return get(p);
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = Integer.parseInt(br.readLine());
        blen = (int)Math.sqrt(n);
        if (blen == 0) blen = 1; // 避免除零错误
        
        int blockCount = (n + blen - 1) / blen;
        tag = new long[blockCount + 2]; // 块编号从1开始
        
        // 读取数组元素
        String[] s = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Long.parseLong(s[i - 1]);
        }
        
        // 处理m次操作
        m = Integer.parseInt(br.readLine());
        while (m-- > 0) {
            s = br.readLine().split(" ");
            int op = Integer.parseInt(s[0]);
            if (op == 1) {
                // 区间更新操作
                int l = Integer.parseInt(s[1]);
                int r = Integer.parseInt(s[2]);
                long k = Long.parseLong(s[3]);
                updateRange(l, r, k);
            } else {
                // 单点查询操作
                int p = Integer.parseInt(s[1]);
                pw.println(queryPoint(p));
            }
        }
        
        pw.flush();
        pw.close();
        br.close();
    }
    
    /*
     * 时间复杂度分析：
     * - 预处理：O(n)
     * - 区间更新操作：O(√n) 每个完整块O(1)，不完整块O(√n)
     * - 单点查询操作：O(1) 直接计算
     * - 总体时间复杂度：O(m√n)，其中m为操作次数
     * 
     * 空间复杂度分析：
     * - 数组arr：O(n)
     * - 数组tag：O(√n)
     * - 总体空间复杂度：O(n)
     * 
     * Java语言特性注意事项：
     * 1. 输入输出使用BufferedReader和PrintWriter以提高效率
     * 2. 数组大小需要根据n动态调整，避免内存浪费
     * 3. 使用long类型避免整数溢出
     * 4. 块的大小选择为sqrt(n)，这是分块算法的经典选择
     */
}