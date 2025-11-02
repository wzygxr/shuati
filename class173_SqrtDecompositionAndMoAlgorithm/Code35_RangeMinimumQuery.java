// 区间最小值查询 - 分块算法实现 (Java版本)
// 题目来源: AizuOJ 2442
// 题目链接: https://onlinejudge.u-aizu.ac.jp/problems/2442
// 题目大意: 实现一个数据结构，支持单点更新和区间最小值查询
// 约束条件: 数组长度n ≤ 10^5，操作次数q ≤ 10^5

import java.io.*;
import java.util.*;

public class Code35_RangeMinimumQuery {
    static final long INF = 1000000000000000000L;
    
    static int n, q, blen; // blen为块的大小
    static long[] arr; // 原始数组
    static long[] blockMin; // 每个块的最小值
    
    // 初始化分块结构
    static void init() {
        blen = (int)Math.sqrt(n);
        if (blen == 0) blen = 1;
        
        int blockCount = (n + blen - 1) / blen;
        blockMin = new long[blockCount];
        
        // 初始化每个块的最小值
        for (int i = 0; i < blockCount; i++) {
            long minVal = INF;
            for (int j = 0; j < blen; j++) {
                int idx = i * blen + j;
                if (idx >= n) break;
                if (arr[idx] < minVal) {
                    minVal = arr[idx];
                }
            }
            blockMin[i] = minVal;
        }
    }
    
    // 单点更新操作
    static void update(int pos, long val) {
        int blockIdx = pos / blen;
        arr[pos] = val;
        
        // 更新对应块的最小值
        long minVal = INF;
        int start = blockIdx * blen;
        int end = Math.min((blockIdx + 1) * blen, n);
        
        for (int i = start; i < end; i++) {
            if (arr[i] < minVal) {
                minVal = arr[i];
            }
        }
        blockMin[blockIdx] = minVal;
    }
    
    // 区间最小值查询
    static long queryMin(int l, int r) {
        long minVal = INF;
        
        // 处理左边不完整的块
        int leftBlock = l / blen;
        int rightBlock = r / blen;
        
        if (leftBlock == rightBlock) {
            // 所有元素都在同一个块内，直接暴力查询
            for (int i = l; i <= r; i++) {
                if (arr[i] < minVal) {
                    minVal = arr[i];
                }
            }
            return minVal;
        }
        
        // 处理左边不完整的块
        for (int i = l; i < (leftBlock + 1) * blen; i++) {
            if (arr[i] < minVal) {
                minVal = arr[i];
            }
        }
        
        // 处理中间完整的块
        for (int i = leftBlock + 1; i < rightBlock; i++) {
            if (blockMin[i] < minVal) {
                minVal = blockMin[i];
            }
        }
        
        // 处理右边不完整的块
        for (int i = rightBlock * blen; i <= r; i++) {
            if (arr[i] < minVal) {
                minVal = arr[i];
            }
        }
        
        return minVal;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));
        
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());
        
        arr = new long[n];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            arr[i] = Long.parseLong(st.nextToken());
        }
        
        init();
        
        for (int i = 0; i < q; i++) {
            st = new StringTokenizer(br.readLine());
            int type = Integer.parseInt(st.nextToken());
            
            if (type == 0) { // 更新操作
                int pos = Integer.parseInt(st.nextToken());
                long val = Long.parseLong(st.nextToken());
                update(pos, val);
            } else { // 查询操作
                int l = Integer.parseInt(st.nextToken());
                int r = Integer.parseInt(st.nextToken());
                pw.println(queryMin(l, r));
            }
        }
        
        pw.flush();
        pw.close();
        br.close();
    }
    
    /*
     * 时间复杂度分析：
     * - 初始化：O(n)
     * - 更新操作：O(√n)
     * - 查询操作：O(√n)
     * - 对于q次操作，总体时间复杂度：O(n + q√n)
     * 
     * 空间复杂度分析：
     * - 数组arr：O(n)
     * - 块最小值数组blockMin：O(√n)
     * - 总体空间复杂度：O(n + √n) = O(n)
     * 
     * Java语言特性注意事项：
     * 1. 使用long类型避免整数溢出
     * 2. 使用BufferedReader和PrintWriter提高输入输出效率
     * 3. 使用StringTokenizer解析输入
     * 4. 注意数组索引的处理，这里使用0-based索引
     * 
     * 算法说明：
     * 分块算法是一种将数组分成多个大小相近的块的方法，通过预处理每个块的信息，
     * 可以在O(√n)的时间复杂度内处理区间查询和单点更新操作。
     * 
     * 优化说明：
     * 1. 块的大小选择为√n，这是分块算法的最佳实践
     * 2. 预处理每个块的最小值，使得查询完整块时可以O(1)时间获取最小值
     * 3. 对于不完整的块，使用暴力遍历的方式查询
     * 
     * 与其他数据结构的对比：
     * - 线段树：时间复杂度O(log n)，实现较复杂
     * - 稀疏表：查询O(1)，但不支持动态更新
     * - 分块算法：实现简单，时间复杂度O(√n)，支持动态更新
     */
}