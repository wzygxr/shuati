package class176;

// 数颜色/维护队列 (带修改莫队)
// 题目来源: 洛谷P1903
// 题目链接: https://www.luogu.com.cn/problem/P1903
// 题意: 支持两种操作：
// 1. Q l r: 查询区间[l,r]内不同颜色的个数
// 2. R pos val: 将位置pos的颜色修改为val
// 算法思路: 使用带修改莫队算法，增加时间维度，将修改操作也纳入排序考虑
// 时间复杂度: O(n^(5/3))
// 空间复杂度: O(n)
// 适用场景: 带单点修改的区间查询问题

import java.io.*;
import java.util.*;

public class ColorCountWithModification_Solution {
    
    static class Query {
        int l, r, t, id; // l:左端点, r:右端点, t:时间戳, id:查询编号
        
        Query(int l, int r, int t, int id) {
            this.l = l;
            this.r = r;
            this.t = t;
            this.id = id;
        }
    }
    
    static class Update {
        int pos, val, preVal; // pos:修改位置, val:修改后的值, preVal:修改前的值
        
        Update(int pos, int val, int preVal) {
            this.pos = pos;
            this.val = val;
            this.preVal = preVal;
        }
    }
    
    static class MoWithModification {
        static final int MAXN = 130001;
        static final int MAXV = 1000001;
        
        int[] arr = new int[MAXN];
        int[] block = new int[MAXN];
        int[] cnt = new int[MAXV];
        int blockSize;
        int kind = 0; // 不同颜色的个数
        int[] results;
        
        // 删除元素
        void remove(int num) {
            if (--cnt[num] == 0) {
                kind--;
            }
        }
        
        // 添加元素
        void add(int num) {
            if (++cnt[num] == 1) {
                kind++;
            }
        }
        
        // 执行或撤销修改操作
        // jobL, jobR: 当前查询的区间范围
        // tim: 要执行或撤销的修改操作时间戳
        void moveTime(int jobL, int jobR, int tim, Update[] updates) {
            int pos = updates[tim].pos;
            int val = updates[tim].val;
            
            // 如果修改位置在当前查询区间内，需要更新答案
            if (jobL <= pos && pos <= jobR) {
                remove(arr[pos]);
                add(val);
            }
            
            // 交换数组中的值和修改记录中的值
            int tmp = arr[pos];
            arr[pos] = val;
            updates[tim].val = tmp; // 这里有个技巧，把原值保存到val中，便于下次交换
        }
        
        // 处理查询
        int[] processQueries(int n, Query[] queries, Update[] updates, int queryCount, int updateCount) {
            results = new int[queryCount + 1];
            
            // 初始化块大小，带修改莫队的块大小通常取n^(2/3)
            blockSize = Math.max(1, (int) Math.pow(n, 2.0 / 3));
            
            // 为每个位置分配块
            for (int i = 1; i <= n; i++) {
                block[i] = (i - 1) / blockSize + 1;
            }
            
            // 按照带修改莫队的排序规则排序
            Arrays.sort(queries, 1, queryCount + 1, new Comparator<Query>() {
                public int compare(Query a, Query b) {
                    if (block[a.l] != block[b.l]) {
                        return block[a.l] - block[b.l];
                    }
                    if (block[a.r] != block[b.r]) {
                        return block[a.r] - block[b.r];
                    }
                    return a.t - b.t;
                }
            });
            
            int curL = 1, curR = 0, curT = 0;
            
            // 处理每个查询
            for (int i = 1; i <= queryCount; i++) {
                int jobL = queries[i].l;
                int jobR = queries[i].r;
                int jobT = queries[i].t;
                int id = queries[i].id;
                
                // 扩展右边界
                while (curR < jobR) {
                    curR++;
                    add(arr[curR]);
                }
                
                // 收缩右边界
                while (curR > jobR) {
                    remove(arr[curR]);
                    curR--;
                }
                
                // 收缩左边界
                while (curL < jobL) {
                    remove(arr[curL]);
                    curL++;
                }
                
                // 扩展左边界
                while (curL > jobL) {
                    curL--;
                    add(arr[curL]);
                }
                
                // 处理时间戳
                while (curT < jobT) {
                    curT++;
                    moveTime(jobL, jobR, curT, updates);
                }
                
                while (curT > jobT) {
                    moveTime(jobL, jobR, curT, updates);
                    curT--;
                }
                
                results[id] = kind;
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
        
        MoWithModification mo = new MoWithModification();
        
        parts = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            mo.arr[i] = Integer.parseInt(parts[i - 1]);
        }
        
        Query[] queries = new Query[m + 1];
        Update[] updates = new Update[m + 1];
        int queryCount = 0, updateCount = 0;
        
        for (int i = 1; i <= m; i++) {
            parts = br.readLine().split(" ");
            char op = parts[0].charAt(0);
            
            if (op == 'Q') {
                // 查询操作
                int l = Integer.parseInt(parts[1]);
                int r = Integer.parseInt(parts[2]);
                queryCount++;
                queries[queryCount] = new Query(l, r, updateCount, queryCount);
            } else {
                // 修改操作
                int pos = Integer.parseInt(parts[1]);
                int val = Integer.parseInt(parts[2]);
                updateCount++;
                updates[updateCount] = new Update(pos, val, mo.arr[pos]);
            }
        }
        
        int[] results = mo.processQueries(n, queries, updates, queryCount, updateCount);
        
        for (int i = 1; i <= queryCount; i++) {
            out.println(results[i]);
        }
        
        out.flush();
    }
}