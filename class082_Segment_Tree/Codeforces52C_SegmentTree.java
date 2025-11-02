package class109;

/**
 * Codeforces 52C Circular RMQ
 * 
 * 题目描述：
 * 给定一个长度为n的环形数组（即a[0]的前一个元素是a[n-1]，a[n-1]的后一个元素是a[0]），
 * 需要处理以下两种操作：
 * 1. 将区间[l, r]中的每个元素都加上v（如果l > r，则表示环形区间[l, n-1]和[0, r]）
 * 2. 查询区间[l, r]中所有元素的最小值（如果l > r，则表示环形区间[l, n-1]和[0, r]）
 * 
 * 解题思路：
 * 这是一个环形线段树问题，需要处理环形区间操作。
 * 1. 对于环形区间操作，如果l > r，可以将其拆分为两个普通区间[l, n-1]和[0, r]
 * 2. 使用线段树配合懒标记来处理区间更新和区间最值查询
 * 
 * 时间复杂度分析：
 * - 初始化：O(n)
 * - 区间更新：O(log n)
 * - 区间查询：O(log n)
 * 
 * 空间复杂度分析：
 * - O(n)，线段树需要4*n的空间来存储节点信息
 * 
 * 链接：https://codeforces.com/contest/52/problem/C
 */
public class Codeforces52C_SegmentTree {
    
    // 线段树数组，存储区间最小值
    private long[] tree;
    // 懒标记数组，存储区间延迟更新的值
    private long[] lazy;
    // 原数组
    private int[] nums;
    // 数组长度
    private int n;
    
    /**
     * 构造函数，初始化线段树
     * @param nums 输入数组
     */
    public Codeforces52C_SegmentTree(int[] nums) {
        this.n = nums.length;
        this.nums = nums;
        // 线段树数组大小通常为4*n，确保足够容纳所有节点
        this.tree = new long[n << 2];
        this.lazy = new long[n << 2];
        // 初始化为最大值
        for (int i = 0; i < (n << 2); i++) {
            tree[i] = Long.MAX_VALUE;
            lazy[i] = 0;
        }
        // 构建线段树
        buildTree(0, n - 1, 1);
    }
    
    /**
     * 构建线段树
     * @param start 区间起始位置
     * @param end 区间结束位置
     * @param node 当前节点在tree数组中的索引
     */
    private void buildTree(int start, int end, int node) {
        // 清空懒标记
        lazy[node] = 0;
        
        // 如果是叶子节点，直接赋值
        if (start == end) {
            tree[node] = nums[start];
            return;
        }
        
        // 计算中点
        int mid = (start + end) / 2;
        // 递归构建左右子树
        buildTree(start, mid, node * 2);
        buildTree(mid + 1, end, node * 2 + 1);
        // 合并左右子树信息，取最小值
        tree[node] = Math.min(tree[node * 2], tree[node * 2 + 1]);
    }
    
    /**
     * 下推懒标记
     * @param node 当前节点
     * @param start 当前节点区间起始位置
     * @param end 当前节点区间结束位置
     */
    private void pushDown(int node, int start, int end) {
        if (lazy[node] != 0) {
            // 将懒标记下推到左右子节点
            lazy[node * 2] += lazy[node];
            lazy[node * 2 + 1] += lazy[node];
            
            // 如果不是叶子节点，更新子节点的值
            if (start != end) {
                tree[node * 2] += lazy[node];
                tree[node * 2 + 1] += lazy[node];
            }
            
            // 清空当前节点的懒标记
            lazy[node] = 0;
        }
    }
    
    /**
     * 区间更新
     * @param start 当前节点区间起始位置
     * @param end 当前节点区间结束位置
     * @param node 当前节点在tree数组中的索引
     * @param left 更新区间左边界
     * @param right 更新区间右边界
     * @param val 更新值
     */
    private void updateRange(int start, int end, int node, int left, int right, long val) {
        // 如果当前区间与更新区间无重叠，直接返回
        if (start > right || end < left) {
            return;
        }
        
        // 如果当前区间完全包含在更新区间内
        if (start >= left && end <= right) {
            // 更新当前节点的值
            tree[node] += val;
            // 设置懒标记
            if (start != end) {
                lazy[node] += val;
            }
            return;
        }
        
        // 下推懒标记
        pushDown(node, start, end);
        
        // 递归更新左右子树
        int mid = (start + end) / 2;
        updateRange(start, mid, node * 2, left, right, val);
        updateRange(mid + 1, end, node * 2 + 1, left, right, val);
        
        // 合并左右子树信息，取最小值
        tree[node] = Math.min(tree[node * 2], tree[node * 2 + 1]);
    }
    
    /**
     * 区间更新接口（处理环形区间）
     * @param left 更新区间左边界
     * @param right 更新区间右边界
     * @param val 更新值
     */
    public void update(int left, int right, long val) {
        // 处理环形区间
        if (left <= right) {
            updateRange(0, n - 1, 1, left, right, val);
        } else {
            // 环形区间拆分为两个普通区间
            updateRange(0, n - 1, 1, left, n - 1, val);
            updateRange(0, n - 1, 1, 0, right, val);
        }
    }
    
    /**
     * 区间查询
     * @param start 当前节点区间起始位置
     * @param end 当前节点区间结束位置
     * @param node 当前节点在tree数组中的索引
     * @param left 查询区间左边界
     * @param right 查询区间右边界
     * @return 区间最小值
     */
    private long queryRange(int start, int end, int node, int left, int right) {
        // 如果当前区间与查询区间无重叠，返回最大值
        if (start > right || end < left) {
            return Long.MAX_VALUE;
        }
        
        // 如果当前区间完全包含在查询区间内
        if (start >= left && end <= right) {
            return tree[node];
        }
        
        // 下推懒标记
        pushDown(node, start, end);
        
        // 递归查询左右子树
        int mid = (start + end) / 2;
        long leftMin = queryRange(start, mid, node * 2, left, right);
        long rightMin = queryRange(mid + 1, end, node * 2 + 1, left, right);
        
        return Math.min(leftMin, rightMin);
    }
    
    /**
     * 区间查询接口（处理环形区间）
     * @param left 查询区间左边界
     * @param right 查询区间右边界
     * @return 区间最小值
     */
    public long query(int left, int right) {
        // 处理环形区间
        if (left <= right) {
            return queryRange(0, n - 1, 1, left, right);
        } else {
            // 环形区间拆分为两个普通区间
            long min1 = queryRange(0, n - 1, 1, left, n - 1);
            long min2 = queryRange(0, n - 1, 1, 0, right);
            return Math.min(min1, min2);
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        System.out.println("测试Codeforces 52C实现...");
        
        // 测试用例
        int[] nums = {1, 2, 3, 4, 5};
        Codeforces52C_SegmentTree segTree = new Codeforces52C_SegmentTree(nums);
        
        System.out.println("初始数组: [1, 2, 3, 4, 5]");
        System.out.println("查询区间[1,3]的最小值: " + segTree.query(1, 3)); // 应该输出2
        
        // 区间更新：将区间[1,3]中的每个元素都加上2
        segTree.update(1, 3, 2);
        System.out.println("将区间[1,3]中的每个元素都加上2后:");
        System.out.println("查询区间[1,3]的最小值: " + segTree.query(1, 3)); // 应该输出4
        
        // 环形区间查询：查询区间[3,1]（环形）
        System.out.println("环形区间[3,1]的最小值: " + segTree.query(3, 1)); // 应该输出3
        
        System.out.println("Codeforces 52C测试完成！");
    }
}