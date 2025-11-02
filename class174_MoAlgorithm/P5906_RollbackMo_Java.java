package class176;

// 回滚莫队模板 (回滚莫队应用 - 最远间隔距离)
// 题目来源: 洛谷P5906 【模板】回滚莫队&不删除莫队
// 题目链接: https://www.luogu.com.cn/problem/P5906
// 题意: 给定一个序列，多次询问一段区间 [l,r]，求区间中相同的数的最远间隔距离。序列中两个元素的间隔距离指的是两个元素下标差的绝对值。
// 算法思路: 使用回滚莫队算法，适用于只能添加不能删除或者只能删除不能添加的区间问题
// 时间复杂度: O((n + q) * sqrt(n))
// 空间复杂度: O(n)
// 适用场景: 区间最远间隔距离查询问题

import java.io.*;
import java.util.*;

public class P5906_RollbackMo_Java {
    
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
        int blockSize;
        int[] lastPos = new int[MAXN]; // 记录每个值最后出现的位置
        int[] maxDist = new int[MAXN]; // 记录每个值当前的最大间隔
        int globalMax = 0; // 全局最大间隔
        int[] results;
        
        // 初始化位置数组
        void initPositions() {
            Arrays.fill(lastPos, -1);
            Arrays.fill(maxDist, 0);
            globalMax = 0;
        }
        
        // 添加元素
        void add(int pos) {
            int val = arr[pos];
            if (lastPos[val] != -1) {
                int dist = pos - lastPos[val];
                maxDist[val] = Math.max(maxDist[val], dist);
                globalMax = Math.max(globalMax, maxDist[val]);
            }
            lastPos[val] = pos;
        }
        
        // 删除元素（不更新答案，用于回滚）
        void removeWithoutUpdate(int pos) {
            // 在回滚莫队中，我们不真正删除元素，而是通过状态恢复来实现
        }
        
        // 处理查询
        int[] processQueries(int n, int[][] queries) {
            int q = queries.length;
            Query[] queryList = new Query[q];
            results = new int[q];
            
            // 离散化
            int[] sorted = new int[n + 1];
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
                    initPositions();
                    int tempMax = 0;
                    for (int j = L; j <= R; j++) {
                        int val = arr[j];
                        if (lastPos[val] != -1) {
                            int dist = j - lastPos[val];
                            tempMax = Math.max(tempMax, dist);
                        }
                        lastPos[val] = j;
                    }
                    results[idx] = tempMax;
                    continue;
                }
                
                // 初始化状态
                initPositions();
                
                // 扩展右边界到R
                while (curR < R) {
                    curR++;
                    add(curR);
                }
                
                // 保存当前状态
                int savedR = curR;
                int savedGlobalMax = globalMax;
                
                // 收缩左边界到L
                while (curL < L) {
                    removeWithoutUpdate(curL);
                    curL++;
                }
                
                results[idx] = globalMax;
                
                // 恢复状态
                initPositions();
                while (curL > block[L] * blockSize + 1) {
                    curL--;
                    add(curL);
                }
                
                // 恢复右边界
                while (curR > savedR) {
                    removeWithoutUpdate(curR);
                    curR--;
                }
                globalMax = savedGlobalMax;
            }
            
            return results;
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        
        int n = Integer.parseInt(br.readLine());
        
        RollbackMo mo = new RollbackMo();
        
        String[] parts = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            mo.arr[i] = Integer.parseInt(parts[i - 1]);
        }
        
        int q = Integer.parseInt(br.readLine());
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