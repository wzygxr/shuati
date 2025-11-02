package class176;

// Rmq Problem / mex (回滚莫队应用)
// 题目来源: 洛谷P4137 Rmq Problem / mex
// 题目链接: https://www.luogu.com.cn/problem/P4137
// 题意: 有一个长度为 n 的数组 {a1,a2,...,an}。m 次询问，每次询问一个区间内最小没有出现过的自然数。
// 算法思路: 使用回滚莫队算法，适用于只能添加不能删除或者只能删除不能添加的区间问题
// 时间复杂度: O((n + q) * sqrt(n))
// 空间复杂度: O(n)
// 适用场景: 区间Mex查询问题

import java.io.*;
import java.util.*;

public class P4137_RmqProblem_Mex_Java {
    
    static class Query {
        int l, r, id;
        
        Query(int l, int r, int id) {
            this.l = l;
            this.r = r;
            this.id = id;
        }
    }
    
    static class RollbackMo {
        static final int MAXN = 200001;
        
        int[] arr = new int[MAXN];
        int[] block = new int[MAXN];
        int[] cnt = new int[MAXN];
        int blockSize;
        int[] results;
        
        // 添加元素
        void add(int pos) {
            cnt[arr[pos]]++;
        }
        
        // 删除元素（不更新答案，用于回滚）
        void removeWithoutUpdate(int pos) {
            cnt[arr[pos]]--;
        }
        
        // 处理查询
        int[] processQueries(int n, int[][] queries) {
            int q = queries.length;
            Query[] queryList = new Query[q];
            results = new int[q];
            
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
                    int[] tempCnt = new int[MAXN];
                    for (int j = L; j <= R; j++) {
                        tempCnt[arr[j]]++;
                    }
                    // 找到最小的未出现的自然数
                    int mex = 0;
                    while (tempCnt[mex] > 0) {
                        mex++;
                    }
                    results[idx] = mex;
                    continue;
                }
                
                // 扩展右边界到R
                while (curR < R) {
                    curR++;
                    add(curR);
                }
                
                // 保存当前状态
                int savedR = curR;
                
                // 收缩左边界到L
                while (curL < L) {
                    removeWithoutUpdate(curL);
                    curL++;
                }
                
                // 计算Mex
                int mex = 0;
                while (cnt[mex] > 0) {
                    mex++;
                }
                results[idx] = mex;
                
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
            mo.arr[i] = Integer.parseInt(parts[i - 1]);
        }
        
        int[][] queries = new int[q][2];
        
        for (int i = 0; i < q; i++) {
            parts = br.readLine().split(" ");
            queries[i][0] = Integer.parseInt(parts[0]);
            queries[i][1] = Integer.parseInt(parts[1]);
        }
        
        int[] results = mo.processQueries(n, queries);
        
        for (int result : results) {
            out.println(result);
        }
        
        out.flush();
    }
}