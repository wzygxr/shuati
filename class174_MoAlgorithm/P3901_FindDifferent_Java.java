package class176;

// 数列找不同 (普通莫队应用)
// 题目来源: 洛谷P3901 数列找不同
// 题目链接: https://www.luogu.com.cn/problem/P3901
// 题意: 现有数列 A1,A2,...,AN，Q 个询问 (Li,Ri)，询问 ALi,ALi+1,...,ARi 是否互不相同。
// 算法思路: 使用普通莫队算法，通过分块和双指针技术优化区间查询
// 时间复杂度: O((n + q) * sqrt(n))
// 空间复杂度: O(n)
// 适用场景: 区间元素互异性判断问题

import java.io.*;
import java.util.*;

public class P3901_FindDifferent_Java {
    
    static class Query {
        int l, r, id;
        
        Query(int l, int r, int id) {
            this.l = l;
            this.r = r;
            this.id = id;
        }
    }
    
    static class MoAlgorithm {
        static final int MAXN = 100001;
        
        int[] arr = new int[MAXN];
        int[] block = new int[MAXN];
        int[] cnt = new int[MAXN];
        int blockSize;
        int differentCount = 0;
        boolean[] results;
        
        // 添加元素
        void add(int pos) {
            if (cnt[arr[pos]] == 0) {
                differentCount++;
            }
            cnt[arr[pos]]++;
        }
        
        // 删除元素
        void remove(int pos) {
            cnt[arr[pos]]--;
            if (cnt[arr[pos]] == 0) {
                differentCount--;
            }
        }
        
        // 处理查询
        boolean[] processQueries(int n, int[][] queries) {
            int q = queries.length;
            Query[] queryList = new Query[q];
            results = new boolean[q];
            
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
                
                // 判断区间内元素是否互不相同
                results[idx] = (differentCount == (R - L + 1));
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
        
        MoAlgorithm mo = new MoAlgorithm();
        
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
        
        boolean[] results = mo.processQueries(n, queries);
        
        for (boolean result : results) {
            out.println(result ? "Yes" : "No");
        }
        
        out.flush();
    }
}