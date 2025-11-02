package class111.supplementary_problems;

// Range Add Query (区间加法更新)
// 题目描述:
// 实现一个支持区间加法更新和单点查询的数据结构
// 支持以下操作：
// 1. 对区间[l, r]内所有元素加上一个值val
// 2. 查询某个位置的值
// 测试链接: https://leetcode.cn/problems/range-addition/
//
// 解题思路:
// 1. 使用带懒惰传播的线段树实现区间更新和单点查询
// 2. 懒惰传播用于延迟更新，避免不必要的计算
// 3. 区间更新时，只在必要时才将更新操作传递给子节点
// 4. 查询时确保所有相关的懒惰标记都被处理
//
// 时间复杂度: 
// - 区间更新: O(log n)
// - 单点查询: O(log n)
// 空间复杂度: O(n)

public class Code08_RangeAddQuery {
    
    // 带懒惰传播的线段树实现
    private static class SegmentTree {
        private int[] tree;     // 线段树节点值（存储区间和）
        private int[] lazy;     // 懒惰标记数组
        private int n;          // 数组长度
        
        public SegmentTree(int size) {
            this.n = size;
            // 线段树需要4*n的空间
            this.tree = new int[4 * n];
            this.lazy = new int[4 * n];
        }
        
        // 区间加法更新 [l, r] 区间内每个元素加上 val
        public void rangeAdd(int l, int r, int val) {
            rangeAddHelper(0, 0, n - 1, l, r, val);
        }
        
        // 区间加法更新辅助函数
        private void rangeAddHelper(int node, int start, int end, int l, int r, int val) {
            // 1. 先处理懒惰标记
            pushDown(node, start, end);
            
            // 2. 当前区间与更新区间无交集
            if (start > r || end < l) {
                return;
            }
            
            // 3. 当前区间完全包含在更新区间内
            if (start >= l && end <= r) {
                // 更新当前节点的值
                tree[node] += val * (end - start + 1);
                // 如果不是叶子节点，设置懒惰标记
                if (start != end) {
                    lazy[2 * node + 1] += val;
                    lazy[2 * node + 2] += val;
                }
                return;
            }
            
            // 4. 当前区间与更新区间有部分交集，递归处理左右子树
            int mid = (start + end) / 2;
            rangeAddHelper(2 * node + 1, start, mid, l, r, val);
            rangeAddHelper(2 * node + 2, mid + 1, end, l, r, val);
            
            // 更新当前节点的值
            tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
        }
        
        // 查询单点的值
        public int query(int index) {
            return queryHelper(0, 0, n - 1, index);
        }
        
        // 查询单点值辅助函数
        private int queryHelper(int node, int start, int end, int index) {
            // 1. 先处理懒惰标记
            pushDown(node, start, end);
            
            // 2. 找到叶子节点
            if (start == end) {
                return tree[node];
            }
            
            // 3. 递归查询左右子树
            int mid = (start + end) / 2;
            if (index <= mid) {
                return queryHelper(2 * node + 1, start, mid, index);
            } else {
                return queryHelper(2 * node + 2, mid + 1, end, index);
            }
        }
        
        // 下推懒惰标记
        private void pushDown(int node, int start, int end) {
            // 如果当前节点没有懒惰标记，直接返回
            if (lazy[node] == 0) {
                return;
            }
            
            // 将懒惰标记下推到子节点
            int mid = (start + end) / 2;
            // 更新左子节点
            tree[2 * node + 1] += lazy[node] * (mid - start + 1);
            // 更新右子节点
            tree[2 * node + 2] += lazy[node] * (end - mid);
            
            // 如果子节点不是叶子节点，继续传递懒惰标记
            if (start != mid) {
                lazy[2 * node + 1] += lazy[node];
            }
            if (mid + 1 != end) {
                lazy[2 * node + 2] += lazy[node];
            }
            
            // 清除当前节点的懒惰标记
            lazy[node] = 0;
        }
    }
    
    // 主类实现
    private SegmentTree st;
    private int n;
    
    public Code08_RangeAddQuery(int size) {
        this.n = size;
        this.st = new SegmentTree(size);
    }
    
    // 对区间[l, r]内所有元素加上val
    public void rangeAdd(int l, int r, int val) {
        st.rangeAdd(l, r, val);
    }
    
    // 查询索引index处的值
    public int query(int index) {
        return st.query(index);
    }
    
    // 测试方法
    public static void main(String[] args) {
        Code08_RangeAddQuery raq = new Code08_RangeAddQuery(5);
        
        // 对区间[1, 3]内所有元素加上2
        raq.rangeAdd(1, 3, 2);
        
        // 查询各个位置的值
        // 初始数组为[0, 0, 0, 0, 0]
        // 操作后变为[0, 2, 2, 2, 0]
        System.out.println(raq.query(0)); // 输出: 0
        System.out.println(raq.query(1)); // 输出: 2
        System.out.println(raq.query(2)); // 输出: 2
        System.out.println(raq.query(3)); // 输出: 2
        System.out.println(raq.query(4)); // 输出: 0
        
        // 对区间[2, 4]内所有元素加上3
        raq.rangeAdd(2, 4, 3);
        
        // 查询各个位置的值
        // 数组变为[0, 2, 5, 5, 3]
        System.out.println(raq.query(0)); // 输出: 0
        System.out.println(raq.query(1)); // 输出: 2
        System.out.println(raq.query(2)); // 输出: 5
        System.out.println(raq.query(3)); // 输出: 5
        System.out.println(raq.query(4)); // 输出: 3
    }
}