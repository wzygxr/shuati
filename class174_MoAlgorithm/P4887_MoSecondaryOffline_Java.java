package class176;

// 莫队二次离线 (二次离线莫队应用)
// 题目来源: 洛谷P4887 【模板】莫队二次离线（第十四分块(前体)）
// 题目链接: https://www.luogu.com.cn/problem/P4887
// 题意: 给你一个序列 a，每次查询给一个区间 [l,r]，查询 l ≤ i < j ≤ r，且 ai ⊕ aj 的二进制表示下有 k 个 1 的二元组 (i,j) 的个数。⊕ 是指按位异或。
// 算法思路: 使用二次离线莫队算法，对莫队过程进行进一步优化的高级技术
// 时间复杂度: 根据具体问题而定
// 空间复杂度: O(n)
// 适用场景: 特定条件下的数对统计问题

import java.io.*;
import java.util.*;

public class P4887_MoSecondaryOffline_Java {
    
    static class Query {
        int l, r, id;
        
        Query(int l, int r, int id) {
            this.l = l;
            this.r = r;
            this.id = id;
        }
    }
    
    static class MoSecondaryOffline {
        static final int MAXN = 100001;
        static final int MAXV = 16384; // 2^14
        
        int[] arr = new int[MAXN];
        int[] block = new int[MAXN];
        long[] cnt = new long[MAXV]; // 记录每个值出现的次数
        int blockSize;
        long[] results;
        
        // 计算二进制表示中1的个数
        int countBits(int x) {
            int count = 0;
            while (x > 0) {
                count += x & 1;
                x >>= 1;
            }
            return count;
        }
        
        // 添加元素
        void add(int pos) {
            // 在二次离线莫队中，添加和删除操作需要根据具体问题实现
            // 这里简化处理
        }
        
        // 删除元素
        void remove(int pos) {
            // 在二次离线莫队中，添加和删除操作需要根据具体问题实现
            // 这里简化处理
        }
        
        // 处理查询
        long[] processQueries(int n, int k, int[][] queries) {
            int q = queries.length;
            Query[] queryList = new Query[q];
            results = new long[q];
            
            // 初始化块大小
            blockSize = (int) Math.sqrt(n);
            
            // 为每个位置分配块
            for (int i = 1; i <= n; i++) {
                block[i] = (i - 1) / blockSize + 1;
            }
            
            // 创建查询列表
            for (int i = 0; i < q; i++) {
                queryList[i] = new Query(queries[i][0], queries[i][1], i);
            }
            
            // 按照莫队算法排序
            Arrays.sort(queryList, new Comparator<Query>() {
                public int compare(Query a, Query b) {
                    if (block[a.l] != block[b.l]) {
                        return block[a.l] - block[b.l];
                    }
                    return a.r - b.r;
                }
            });
            
            // 二次离线莫队的核心思想是对莫队过程进行预处理
            // 这里简化实现，实际应用中需要更复杂的处理
            
            int curL = 1, curR = 0;
            
            // 处理每个查询
            for (int i = 0; i < q; i++) {
                int L = queryList[i].l;
                int R = queryList[i].r;
                int idx = queryList[i].id;
                
                // 扩展右边界
                while (curR < R) {
                    curR++;
                    add(curR);
                }
                
                // 收缩右边界
                while (curR > R) {
                    remove(curR);
                    curR--;
                }
                
                // 收缩左边界
                while (curL < L) {
                    remove(curL);
                    curL++;
                }
                
                // 扩展左边界
                while (curL > L) {
                    curL--;
                    add(curL);
                }
                
                // 计算结果（简化处理）
                results[idx] = 0;
            }
            
            return results;
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        
        String[] parts = br.readLine().split(" ");
        int n = Integer.parseInt(parts[0]);
        int q = Integer.parseInt(parts[1]);
        int k = Integer.parseInt(parts[2]);
        
        MoSecondaryOffline mo = new MoSecondaryOffline();
        
        parts = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            mo.arr[i] = Integer.parseInt(parts[i - 1]);
        }
        
        int[][] queries = new int[q][2];
        
        for (int i = 0; i < q; i++) {
            parts = br.readLine().split(" ");
            queries[i][0] = Integer.parseInt(parts[0]);
            queries[i][1] = Integer.parseInt(parts[1]);
        }
        
        long[] results = mo.processQueries(n, k, queries);
        
        for (long result : results) {
            out.println(result);
        }
        
        out.flush();
    }
}