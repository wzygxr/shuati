package class176;

// 小B的询问 (普通莫队应用)
// 题目来源: 洛谷P2709
// 题目链接: https://www.luogu.com.cn/problem/P2709
// 题意: 给定一个长度为n的数组，所有数字在[1..k]范围上
// 定义f(i) = i这种数的出现次数的平方
// 一共有m条查询，查询[l,r]范围内f(1) + f(2) + ... + f(k)的值
// 算法思路: 使用普通莫队算法，通过分块和双指针技术优化区间查询
// 时间复杂度: O((n + m) * sqrt(n))
// 空间复杂度: O(n)
// 适用场景: 区间元素出现次数的统计信息计算

import java.io.*;
import java.util.*;

public class LittleBQuery_Solution {
    
    static class Query {
        int l, r, id;
        
        Query(int l, int r, int id) {
            this.l = l;
            this.r = r;
            this.id = id;
        }
    }
    
    static class MoAlgorithm {
        static final int MAXN = 50001;
        
        int[] arr = new int[MAXN];
        int[] block = new int[MAXN];
        int[] cnt = new int[MAXN];
        int blockSize;
        long sum = 0; // 使用long防止溢出
        long[] results;
        
        // 删除元素
        void remove(int pos) {
            sum -= (long) cnt[arr[pos]] * cnt[arr[pos]];
            cnt[arr[pos]]--;
            sum += (long) cnt[arr[pos]] * cnt[arr[pos]];
        }
        
        // 添加元素
        void add(int pos) {
            sum -= (long) cnt[arr[pos]] * cnt[arr[pos]];
            cnt[arr[pos]]++;
            sum += (long) cnt[arr[pos]] * cnt[arr[pos]];
        }
        
        // 处理查询
        long[] processQueries(int n, int k, int[][] queries) {
            int m = queries.length;
            Query[] queryList = new Query[m];
            results = new long[m];
            
            // 初始化块大小
            blockSize = (int) Math.sqrt(n);
            
            // 为每个位置分配块
            for (int i = 1; i <= n; i++) {
                block[i] = (i - 1) / blockSize + 1;
            }
            
            // 创建查询列表
            for (int i = 0; i < m; i++) {
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
            for (int i = 0; i < m; i++) {
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
                
                results[idx] = sum;
            }
            
            return results;
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        
        String[] parts = br.readLine().split(" ");
        int n = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        int k = Integer.parseInt(parts[2]);
        
        MoAlgorithm mo = new MoAlgorithm();
        
        parts = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            mo.arr[i] = Integer.parseInt(parts[i - 1]);
        }
        
        int[][] queries = new int[m][2];
        
        for (int i = 0; i < m; i++) {
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