import java.io.*;
import java.util.*;

/**
 * SPOJ GSS2 - Can you answer these queries II
 * 
 * 题目描述:
 * 给定一个整数数组，多次查询某个区间内的最大子段和，但重复元素只计算一次。
 * 
 * 解题思路:
 * 这是一个非常经典的线段树问题，需要使用扫描线算法和历史信息维护。
 * 
 * 关键点:
 * 1. 使用离线处理，将所有查询按右端点排序
 * 2. 使用扫描线从左到右处理数组元素
 * 3. 维护一个线段树，支持区间加法和历史最大值查询
 * 4. 使用last数组记录每个值最后出现的位置
 * 
 * 时间复杂度: O((n + q) * log n)
 * 空间复杂度: O(n)
 */
public class SPOJ_GSS2_CanYouAnswerTheseQueriesII {
    static class SegmentTree {
        private int n;
        private long[] sum;      // 当前区间和
        private long[] maxSum;   // 当前区间最大子段和
        private long[] historyMaxSum; // 历史最大子段和
        private long[] lazy;     // 懒惰标记
        
        public SegmentTree(int size) {
            this.n = size;
            this.sum = new long[4 * size];
            this.maxSum = new long[4 * size];
            this.historyMaxSum = new long[4 * size];
            this.lazy = new long[4 * size];
        }
        
        /**
         * 区间加法更新
         * 
         * @param jobl 任务区间左端点
         * @param jobr 任务区间右端点
         * @param jobv 任务值
         * @param l 当前区间左端点
         * @param r 当前区间右端点
         * @param i 当前节点索引
         */
        public void updateRange(int jobl, int jobr, long jobv, int l, int r, int i) {
            if (jobl <= l && r <= jobr) {
                // 当前区间完全包含在任务区间内
                lazy[i] += jobv;
                sum[i] += jobv;
                historyMaxSum[i] = Math.max(historyMaxSum[i], maxSum[i] + lazy[i]);
                maxSum[i] += jobv;
            } else {
                // 需要继续向下递归
                pushDown(i, l, r);
                int mid = (l + r) >> 1;
                if (jobl <= mid) {
                    updateRange(jobl, jobr, jobv, l, mid, i << 1);
                }
                if (jobr > mid) {
                    updateRange(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
                }
                pushUp(i);
            }
        }
        
        /**
         * 区间最大子段和查询
         * 
         * @param jobl 查询区间左端点
         * @param jobr 查询区间右端点
         * @param l 当前区间左端点
         * @param r 当前区间右端点
         * @param i 当前节点索引
         * @return 区间最大子段和
         */
        public long queryRange(int jobl, int jobr, int l, int r, int i) {
            if (jobl <= l && r <= jobr) {
                // 当前区间完全包含在查询区间内
                return historyMaxSum[i];
            } else {
                // 需要继续向下递归
                pushDown(i, l, r);
                int mid = (l + r) >> 1;
                long result = Long.MIN_VALUE;
                if (jobl <= mid) {
                    result = Math.max(result, queryRange(jobl, jobr, l, mid, i << 1));
                }
                if (jobr > mid) {
                    result = Math.max(result, queryRange(jobl, jobr, mid + 1, r, i << 1 | 1));
                }
                return result;
            }
        }
        
        /**
         * 下推操作
         * 
         * @param i 当前节点索引
         * @param l 当前区间左端点
         * @param r 当前区间右端点
         */
        private void pushDown(int i, int l, int r) {
            if (lazy[i] != 0) {
                // 下推懒惰标记
                lazy[i << 1] += lazy[i];
                lazy[i << 1 | 1] += lazy[i];
                
                // 更新子节点的sum和maxSum
                sum[i << 1] += lazy[i];
                sum[i << 1 | 1] += lazy[i];
                maxSum[i << 1] += lazy[i];
                maxSum[i << 1 | 1] += lazy[i];
                
                // 更新历史最大值
                historyMaxSum[i << 1] = Math.max(historyMaxSum[i << 1], maxSum[i << 1] + lazy[i << 1] - lazy[i]);
                historyMaxSum[i << 1 | 1] = Math.max(historyMaxSum[i << 1 | 1], maxSum[i << 1 | 1] + lazy[i << 1 | 1] - lazy[i]);
                
                lazy[i] = 0;
            }
        }
        
        /**
         * 上推操作
         * 
         * @param i 当前节点索引
         */
        private void pushUp(int i) {
            sum[i] = sum[i << 1] + sum[i << 1 | 1];
            maxSum[i] = Math.max(maxSum[i << 1], maxSum[i << 1 | 1]);
            historyMaxSum[i] = Math.max(historyMaxSum[i << 1], historyMaxSum[i << 1 | 1]);
        }
    }
    
    static class Query {
        int l, r, id;
        
        Query(int l, int r, int id) {
            this.l = l;
            this.r = r;
            this.id = id;
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组长度
        int n = Integer.parseInt(reader.readLine());
        int[] arr = new int[n + 1];
        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(tokenizer.nextToken());
        }
        
        // 读取查询数量
        int q = Integer.parseInt(reader.readLine());
        Query[] queries = new Query[q];
        for (int i = 0; i < q; i++) {
            tokenizer = new StringTokenizer(reader.readLine());
            int l = Integer.parseInt(tokenizer.nextToken());
            int r = Integer.parseInt(tokenizer.nextToken());
            queries[i] = new Query(l, r, i);
        }
        
        // 按右端点排序查询
        Arrays.sort(queries, (a, b) -> Integer.compare(a.r, b.r));
        
        // 初始化线段树
        SegmentTree segTree = new SegmentTree(n);
        long[] results = new long[q];
        int[] last = new int[200001]; // 记录每个值最后出现的位置，偏移100000处理负数
        Arrays.fill(last, 0);
        
        // 扫描线处理
        int queryIndex = 0;
        for (int i = 1; i <= n; i++) {
            int val = arr[i] + 100000; // 偏移处理负数
            // 更新区间[last[val]+1, i]
            segTree.updateRange(last[val] + 1, i, arr[i], 1, n, 1);
            last[val] = i;
            
            // 处理所有右端点为i的查询
            while (queryIndex < q && queries[queryIndex].r == i) {
                results[queries[queryIndex].id] = segTree.queryRange(queries[queryIndex].l, queries[queryIndex].r, 1, n, 1);
                queryIndex++;
            }
        }
        
        // 输出结果
        for (int i = 0; i < q; i++) {
            writer.println(results[i]);
        }
        
        writer.flush();
        writer.close();
        reader.close();
    }
}