package class046;

/**
 * Prefix Sum Queries (CSES 2166)
 * 
 * 题目描述:
 * 给定一个数组，支持两种操作：
 * 1. 更新位置k的值为u
 * 2. 查询区间[a,b]内的最大前缀和
 * 
 * 示例:
 * 输入:
 * 5 3
 * 2 2 2 2 2
 * 2 1 5
 * 1 3 4
 * 2 1 5
 * 输出:
 * 10
 * 12
 * 
 * 提示:
 * 1 <= n, q <= 2 * 10^5
 * -10^9 <= x <= 10^9
 * 
 * 题目链接: https://cses.fi/problemset/task/2166
 * 
 * 解题思路:
 * 使用线段树维护区间信息，每个节点存储：
 * 1. 区间和
 * 2. 区间最大前缀和
 * 3. 区间最大后缀和
 * 4. 区间最大子段和
 * 
 * 时间复杂度: 
 * - 初始化: O(n) - 需要遍历整个数组构建线段树
 * - 更新: O(log n) - 每次更新操作的时间复杂度
 * - 查询: O(log n) - 每次查询操作的时间复杂度
 * 空间复杂度: O(n) - 线段树需要额外的空间
 * 
 * 工程化考量:
 * 1. 边界条件处理：空数组、单元素数组
 * 2. 性能优化：使用线段树提供高效的区间查询和更新
 * 3. 数据结构选择：线段树适合频繁的区间操作
 * 4. 大数处理：元素值可能很大，需要确保整数范围
 * 
 * 最优解分析:
 * 这是最优解，因为需要支持动态更新和查询操作，线段树提供了O(log n)的时间复杂度。
 * 对于频繁的区间操作，线段树是最佳选择。
 * 
 * 算法核心:
 * 线段树的合并操作：
 * - 区间和 = 左子树区间和 + 右子树区间和
 * - 区间最大前缀和 = max(左子树最大前缀和, 左子树区间和 + 右子树最大前缀和)
 * - 区间最大后缀和 = max(右子树最大后缀和, 右子树区间和 + 左子树最大后缀和)
 * - 区间最大子段和 = max(左子树最大子段和, 右子树最大子段和, 左子树最大后缀和 + 右子树最大前缀和)
 * 
 * 算法调试技巧:
 * 1. 打印中间过程：显示线段树的构建和更新过程
 * 2. 边界测试：测试空数组、单元素数组等特殊情况
 * 3. 性能测试：测试大规模数组下的性能表现
 * 
 * 语言特性差异:
 * Java中数组是对象，可以直接访问length属性。
 * 与C++相比，Java有自动内存管理，无需手动释放内存。
 * 与Python相比，Java是静态类型语言，需要显式声明类型。
 */

import java.io.*;
import java.util.*;

public class Code22_PrefixSumQueries {
    static class SegmentTreeNode {
        long sum;           // 区间和
        long maxPrefixSum;  // 区间最大前缀和
        long maxSuffixSum;  // 区间最大后缀和
        long maxSubarraySum; // 区间最大子段和
        
        SegmentTreeNode() {
            sum = 0;
            maxPrefixSum = 0;
            maxSuffixSum = 0;
            maxSubarraySum = 0;
        }
    }
    
    static class SegmentTree {
        private SegmentTreeNode[] tree;
        private long[] arr;
        private int n;
        
        /**
         * 构造函数，初始化线段树
         * 
         * @param array 输入数组
         */
        public SegmentTree(long[] array) {
            this.n = array.length;
            this.arr = array.clone();
            // 线段树数组大小通常为4*n
            this.tree = new SegmentTreeNode[4 * n];
            for (int i = 0; i < 4 * n; i++) {
                tree[i] = new SegmentTreeNode();
            }
            // 构建线段树
            build(1, 0, n - 1);
        }
        
        /**
         * 构建线段树
         * 
         * @param node 当前节点索引
         * @param start 区间起始位置
         * @param end 区间结束位置
         */
        private void build(int node, int start, int end) {
            // 叶子节点
            if (start == end) {
                tree[node].sum = arr[start];
                tree[node].maxPrefixSum = arr[start];
                tree[node].maxSuffixSum = arr[start];
                tree[node].maxSubarraySum = arr[start];
                return;
            }
            
            int mid = (start + end) / 2;
            // 递归构建左右子树
            build(2 * node, start, mid);
            build(2 * node + 1, mid + 1, end);
            
            // 合并左右子树的信息
            merge(node);
        }
        
        /**
         * 合并左右子树的信息
         * 
         * @param node 当前节点索引
         */
        private void merge(int node) {
            int leftChild = 2 * node;
            int rightChild = 2 * node + 1;
            
            // 区间和 = 左子树区间和 + 右子树区间和
            tree[node].sum = tree[leftChild].sum + tree[rightChild].sum;
            
            // 区间最大前缀和 = max(左子树最大前缀和, 左子树区间和 + 右子树最大前缀和)
            tree[node].maxPrefixSum = Math.max(
                tree[leftChild].maxPrefixSum,
                tree[leftChild].sum + tree[rightChild].maxPrefixSum
            );
            
            // 区间最大后缀和 = max(右子树最大后缀和, 右子树区间和 + 左子树最大后缀和)
            tree[node].maxSuffixSum = Math.max(
                tree[rightChild].maxSuffixSum,
                tree[rightChild].sum + tree[leftChild].maxSuffixSum
            );
            
            // 区间最大子段和 = max(左子树最大子段和, 右子树最大子段和, 左子树最大后缀和 + 右子树最大前缀和)
            tree[node].maxSubarraySum = Math.max(
                Math.max(tree[leftChild].maxSubarraySum, tree[rightChild].maxSubarraySum),
                tree[leftChild].maxSuffixSum + tree[rightChild].maxPrefixSum
            );
        }
        
        /**
         * 更新数组中某个位置的值
         * 
         * @param index 要更新的位置（0-based）
         * @param value 新的值
         */
        public void update(int index, long value) {
            arr[index] = value;
            update(1, 0, n - 1, index, value);
        }
        
        /**
         * 更新线段树中的值
         * 
         * @param node 当前节点索引
         * @param start 区间起始位置
         * @param end 区间结束位置
         * @param index 要更新的位置
         * @param value 新的值
         */
        private void update(int node, int start, int end, int index, long value) {
            // 叶子节点
            if (start == end) {
                tree[node].sum = value;
                tree[node].maxPrefixSum = value;
                tree[node].maxSuffixSum = value;
                tree[node].maxSubarraySum = value;
                return;
            }
            
            int mid = (start + end) / 2;
            // 根据索引决定更新左子树还是右子树
            if (index <= mid) {
                update(2 * node, start, mid, index, value);
            } else {
                update(2 * node + 1, mid + 1, end, index, value);
            }
            
            // 更新后重新合并信息
            merge(node);
        }
        
        /**
         * 查询区间[0, end]内的最大前缀和
         * 
         * @param end 区间结束位置
         * @return 区间[0, end]内的最大前缀和
         */
        public long queryMaxPrefixSum(int end) {
            return queryMaxPrefixSum(1, 0, n - 1, 0, end);
        }
        
        /**
         * 查询区间[left, right]内的最大前缀和
         * 
         * @param node 当前节点索引
         * @param start 区间起始位置
         * @param end 区间结束位置
         * @param left 查询区间起始位置
         * @param right 查询区间结束位置
         * @return 区间[left, right]内的最大前缀和
         */
        private long queryMaxPrefixSum(int node, int start, int end, int left, int right) {
            // 完全不在查询区间内
            if (start > right || end < left) {
                return Long.MIN_VALUE;
            }
            
            // 完全在查询区间内
            if (start >= left && end <= right) {
                return tree[node].maxPrefixSum;
            }
            
            int mid = (start + end) / 2;
            // 递归查询左右子树
            long leftResult = queryMaxPrefixSum(2 * node, start, mid, left, right);
            long rightResult = queryMaxPrefixSum(2 * node + 1, mid + 1, end, left, right);
            
            // 返回较大值
            return Math.max(leftResult, rightResult);
        }
    }
    
    /**
     * 主函数 - 测试入口
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int q = Integer.parseInt(st.nextToken());
        
        long[] arr = new long[n];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            arr[i] = Long.parseLong(st.nextToken());
        }
        
        SegmentTree segTree = new SegmentTree(arr);
        
        for (int i = 0; i < q; i++) {
            st = new StringTokenizer(br.readLine());
            int type = Integer.parseInt(st.nextToken());
            
            if (type == 1) {
                // 更新操作
                int k = Integer.parseInt(st.nextToken()) - 1; // 转换为0-based索引
                long u = Long.parseLong(st.nextToken());
                segTree.update(k, u);
            } else {
                // 查询操作
                int a = Integer.parseInt(st.nextToken()) - 1; // 转换为0-based索引
                int b = Integer.parseInt(st.nextToken()) - 1; // 转换为0-based索引
                long result = segTree.queryMaxPrefixSum(b);
                out.println(result);
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}