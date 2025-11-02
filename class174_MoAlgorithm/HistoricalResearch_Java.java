package class176;

// 歴史の研究 (AtCoder AT1219) - 回滚莫队
// 题目来源: AtCoder AT1219
// 题目链接: https://www.luogu.com.cn/problem/AT1219
// 题意: 给定一个长度为n的序列，每次询问给定一个区间，定义一种颜色的价值为它的大小乘上它在这个区间内的出现次数，
// 求所有颜色最大的价值。
// 
// 算法思路:
// 1. 使用回滚莫队算法，适用于只能添加不能删除或者只能删除不能添加的区间问题
// 2. 对于左右端点在同一块内的查询，直接暴力计算
// 3. 对于跨块的查询，先扩展右边界到R，然后收缩左边界到L，最后恢复状态
// 
// 时间复杂度分析:
// - 排序: O(q * log q)
// - 左指针移动: O(q * sqrt(n))
// - 右指针移动: O(n * sqrt(n))
// - 总体复杂度: O((n + q) * sqrt(n))
// 
// 空间复杂度分析:
// - 存储原数组: O(n)
// - 存储计数数组: O(n)
// - 存储查询结果: O(q)
// - 总体空间复杂度: O(n)
// 适用场景: 区间众数相关问题、最大值维护问题

import java.io.*;
import java.util.*;

public class HistoricalResearch_Java {
    
    static class Query {
        int l, r, id;
        
        Query(int l, int r, int id) {
            this.l = l;
            this.r = r;
            this.id = id;
        }
    }
    
    static class RollbackMo {
        static final int MAXN = 100001;
        
        long[] arr = new long[MAXN];
        int[] block = new int[MAXN];
        int[] cnt = new int[MAXN];
        int blockSize;
        long maxVal = 0;
        long[] results;
        
        // 添加元素
        void add(int pos) {
            cnt[(int)arr[pos]]++;
            maxVal = Math.max(maxVal, (long)arr[pos] * cnt[(int)arr[pos]]);
        }
        
        // 删除元素（不更新maxVal，用于回滚）
        void removeWithoutUpdate(int pos) {
            cnt[(int)arr[pos]]--;
        }
        
        // 处理查询
        long[] processQueries(int n, int[][] queries) {
            int q = queries.length;
            Query[] queryList = new Query[q];
            results = new long[q];
            
            // 离散化
            long[] sorted = new long[n + 1];
            for (int i = 1; i <= n; i++) {
                sorted[i] = arr[i];
            }
            Arrays.sort(sorted, 1, n + 1);
            int len = 1;
            for (int i = 2; i <= n; i++) {
                if (sorted[len] != sorted[i]) {
                    sorted[++len] = sorted[i];
                }
            }
            for (int i = 1; i <= n; i++) {
                arr[i] = Arrays.binarySearch(sorted, 1, len + 1, arr[i]) - 1;
            }
            
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
            
            // 按照回滚莫队算法排序
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
                
                // 如果左右端点在同一块内，暴力计算
                if (block[L] == block[R]) {
                    long tempMax = 0;
                    int[] tempCnt = new int[MAXN];
                    for (int j = L; j <= R; j++) {
                        tempCnt[(int)arr[j]]++;
                        tempMax = Math.max(tempMax, (long)arr[j] * tempCnt[(int)arr[j]]);
                    }
                    results[idx] = tempMax;
                    continue;
                }
                
                // 扩展右边界到R
                while (curR < R) {
                    curR++;
                    add(curR);
                }
                
                // 保存当前状态
                long savedMax = maxVal;
                int savedR = curR;
                
                // 收缩左边界到L
                while (curL < L) {
                    removeWithoutUpdate(curL);
                    curL++;
                }
                
                results[idx] = maxVal;
                
                // 恢复状态
                while (curL > block[L] * blockSize + 1) {
                    curL--;
                    add(curL);
                }
                
                // 恢复右边界
                while (curR > savedR) {
                    removeWithoutUpdate(curR);
                    curR--;
                }
                maxVal = savedMax;
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
        
        RollbackMo mo = new RollbackMo();
        
        parts = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            mo.arr[i] = Long.parseLong(parts[i - 1]);
        }
        
        int[][] queries = new int[q][2];
        
        for (int i = 0; i < q; i++) {
            parts = br.readLine().split(" ");
            queries[i][0] = Integer.parseInt(parts[0]);
            queries[i][1] = Integer.parseInt(parts[1]);
        }
        
        long[] results = mo.processQueries(n, queries);
        
        for (long result : results) {
            out.println(result);
        }
        
        out.flush();
    }
}