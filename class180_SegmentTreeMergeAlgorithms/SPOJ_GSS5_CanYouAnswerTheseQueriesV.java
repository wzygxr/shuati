import java.io.*;
import java.util.*;

/**
 * SPOJ GSS5 - Can you answer these queries V
 * 
 * 题目描述:
 * 给定一个整数数组，多次查询某个区间内的最大子段和，查询区间可能不连续。
 * 
 * 解题思路:
 * 这是一个线段树的经典应用。与GSS1不同的是，GSS5的查询区间可能不连续，需要特殊处理。
 * 
 * 关键点:
 * 1. 使用线段树维护区间最大子段和信息
 * 2. 对于每个查询(x1, y1, x2, y2)，需要找到在区间[x1, y1]中以y1结尾的最大子段和，
 *    以及在区间[x2, y2]中以x2开头的最大子段和
 * 3. 如果y1 < x2，则结果为区间[y1+1, x2-1]的总和加上前面和后面的部分
 * 4. 如果y1 >= x2，则需要特殊处理重叠区间
 * 
 * 时间复杂度: O(n + q * log n)
 * 空间复杂度: O(n)
 */
public class SPOJ_GSS5_CanYouAnswerTheseQueriesV {
    static class SegmentTreeNode {
        long maxSum;      // 区间最大子段和
        long prefixSum;   // 包含左端点的最大子段和
        long suffixSum;   // 包含右端点的最大子段和
        long totalSum;    // 区间总和
        
        SegmentTreeNode() {
            this.maxSum = 0;
            this.prefixSum = 0;
            this.suffixSum = 0;
            this.totalSum = 0;
        }
        
        SegmentTreeNode(long maxSum, long prefixSum, long suffixSum, long totalSum) {
            this.maxSum = maxSum;
            this.prefixSum = prefixSum;
            this.suffixSum = suffixSum;
            this.totalSum = totalSum;
        }
    }
    
    static class SegmentTree {
        private int n;
        private long[] arr;
        private SegmentTreeNode[] tree;
        
        public SegmentTree(int size, long[] array) {
            this.n = size;
            this.arr = array;
            this.tree = new SegmentTreeNode[4 * size];
            build(1, 1, n);
        }
        
        /**
         * 构建线段树
         * 
         * @param i 当前节点索引
         * @param l 当前区间左端点
         * @param r 当前区间右端点
         */
        private void build(int i, int l, int r) {
            if (l == r) {
                // 叶子节点
                tree[i] = new SegmentTreeNode(arr[l], arr[l], arr[l], arr[l]);
            } else {
                // 非叶子节点
                int mid = (l + r) >> 1;
                build(i << 1, l, mid);
                build(i << 1 | 1, mid + 1, r);
                pushUp(i);
            }
        }
        
        /**
         * 上推操作
         * 
         * @param i 当前节点索引
         */
        private void pushUp(int i) {
            SegmentTreeNode left = tree[i << 1];
            SegmentTreeNode right = tree[i << 1 | 1];
            
            tree[i] = new SegmentTreeNode(
                Math.max(Math.max(left.maxSum, right.maxSum), left.suffixSum + right.prefixSum),
                Math.max(left.prefixSum, left.totalSum + right.prefixSum),
                Math.max(right.suffixSum, right.totalSum + left.suffixSum),
                left.totalSum + right.totalSum
            );
        }
        
        /**
         * 区间最大子段和查询
         * 
         * @param jobl 查询区间左端点
         * @param jobr 查询区间右端点
         * @param l 当前区间左端点
         * @param r 当前区间右端点
         * @param i 当前节点索引
         * @return 区间最大子段和信息
         */
        public SegmentTreeNode queryRange(int jobl, int jobr, int l, int r, int i) {
            if (jobl <= l && r <= jobr) {
                // 当前区间完全包含在查询区间内
                return tree[i];
            } else {
                // 需要继续向下递归
                int mid = (l + r) >> 1;
                if (jobr <= mid) {
                    // 完全在左子树
                    return queryRange(jobl, jobr, l, mid, i << 1);
                } else if (jobl > mid) {
                    // 完全在右子树
                    return queryRange(jobl, jobr, mid + 1, r, i << 1 | 1);
                } else {
                    // 跨越左右子树
                    SegmentTreeNode leftResult = queryRange(jobl, jobr, l, mid, i << 1);
                    SegmentTreeNode rightResult = queryRange(jobl, jobr, mid + 1, r, i << 1 | 1);
                    
                    return new SegmentTreeNode(
                        Math.max(Math.max(leftResult.maxSum, rightResult.maxSum), leftResult.suffixSum + rightResult.prefixSum),
                        Math.max(leftResult.prefixSum, leftResult.totalSum + rightResult.prefixSum),
                        Math.max(rightResult.suffixSum, rightResult.totalSum + leftResult.suffixSum),
                        leftResult.totalSum + rightResult.totalSum
                    );
                }
            }
        }
        
        /**
         * 查询区间总和
         * 
         * @param jobl 查询区间左端点
         * @param jobr 查询区间右端点
         * @param l 当前区间左端点
         * @param r 当前区间右端点
         * @param i 当前节点索引
         * @return 区间总和
         */
        public long querySum(int jobl, int jobr, int l, int r, int i) {
            if (jobl <= l && r <= jobr) {
                // 当前区间完全包含在查询区间内
                return tree[i].totalSum;
            } else {
                // 需要继续向下递归
                int mid = (l + r) >> 1;
                long sum = 0;
                if (jobl <= mid) {
                    sum += querySum(jobl, jobr, l, mid, i << 1);
                }
                if (jobr > mid) {
                    sum += querySum(jobl, jobr, mid + 1, r, i << 1 | 1);
                }
                return sum;
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取测试用例数量
        int t = Integer.parseInt(reader.readLine());
        
        for (int testCase = 0; testCase < t; testCase++) {
            // 读取数组长度
            int n = Integer.parseInt(reader.readLine());
            long[] arr = new long[n + 1];
            StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
            for (int i = 1; i <= n; i++) {
                arr[i] = Long.parseLong(tokenizer.nextToken());
            }
            
            // 构建线段树
            SegmentTree segTree = new SegmentTree(n, arr);
            
            // 读取查询数量
            int q = Integer.parseInt(reader.readLine());
            
            // 处理每个查询
            for (int i = 0; i < q; i++) {
                tokenizer = new StringTokenizer(reader.readLine());
                int x1 = Integer.parseInt(tokenizer.nextToken());
                int y1 = Integer.parseInt(tokenizer.nextToken());
                int x2 = Integer.parseInt(tokenizer.nextToken());
                int y2 = Integer.parseInt(tokenizer.nextToken());
                
                long result = 0;
                
                if (y1 < x2) {
                    // 区间不重叠
                    SegmentTreeNode left = segTree.queryRange(x1, y1, 1, n, 1);
                    SegmentTreeNode right = segTree.queryRange(x2, y2, 1, n, 1);
                    long middleSum = segTree.querySum(y1 + 1, x2 - 1, 1, n, 1);
                    
                    result = left.suffixSum + middleSum + right.prefixSum;
                } else if (y1 >= x2) {
                    // 区间重叠或相邻
                    if (x2 <= y1) {
                        // 有重叠部分
                        SegmentTreeNode overlap = segTree.queryRange(x2, y1, 1, n, 1);
                        long leftMax = 0, rightMax = 0;
                        
                        if (x1 < x2) {
                            SegmentTreeNode left = segTree.queryRange(x1, x2 - 1, 1, n, 1);
                            leftMax = left.suffixSum;
                        }
                        
                        if (y1 < y2) {
                            SegmentTreeNode right = segTree.queryRange(y1 + 1, y2, 1, n, 1);
                            rightMax = right.prefixSum;
                        }
                        
                        result = Math.max(overlap.maxSum, leftMax + overlap.suffixSum);
                        result = Math.max(result, overlap.prefixSum + rightMax);
                        result = Math.max(result, leftMax + overlap.totalSum + rightMax);
                    } else {
                        // 相邻但不重叠
                        SegmentTreeNode left = segTree.queryRange(x1, y1, 1, n, 1);
                        SegmentTreeNode right = segTree.queryRange(x2, y2, 1, n, 1);
                        result = left.suffixSum + right.prefixSum;
                    }
                }
                
                writer.println(result);
            }
        }
        
        writer.flush();
        writer.close();
        reader.close();
    }
}