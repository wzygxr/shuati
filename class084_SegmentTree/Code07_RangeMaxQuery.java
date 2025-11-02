package class111.supplementary_problems;

// Range Max Query (区间最大值查询)
// 题目描述:
// 给定一个数组，实现以下操作：
// 1. 更新数组中某个位置的值
// 2. 查询某个区间内的最大值
// 测试链接: https://leetcode.cn/problems/max-value-of-equation/
//
// 解题思路:
// 1. 使用线段树维护数组区间最大值信息
// 2. 支持单点更新和区间查询操作
// 3. 线段树的每个节点存储对应区间的最大值
// 4. 更新操作从根节点到叶子节点递归更新路径上的所有节点
// 5. 查询操作根据查询区间与节点区间的关系进行递归查询
//
// 时间复杂度: 
// - 构建: O(n)
// - 更新: O(log n)
// - 查询: O(log n)
// 空间复杂度: O(n)

public class Code07_RangeMaxQuery {
    
    // 线段树实现区间最大值查询
    private static class SegmentTree {
        private int[] nums;
        private int[] tree;
        private int n;
        
        public SegmentTree(int[] nums) {
            this.nums = nums;
            this.n = nums.length;
            // 线段树需要4*n的空间
            this.tree = new int[4 * n];
            // 构建线段树
            buildTree(0, 0, n - 1);
        }
        
        // 构建线段树
        // node是线段树节点的索引
        // start和end是数组区间
        private void buildTree(int node, int start, int end) {
            // 叶子节点
            if (start == end) {
                tree[node] = nums[start];
                return;
            }
            
            // 非叶子节点，递归构建左右子树
            int mid = (start + end) / 2;
            // 左子节点索引为2*node+1
            buildTree(2 * node + 1, start, mid);
            // 右子节点索引为2*node+2
            buildTree(2 * node + 2, mid + 1, end);
            // 更新当前节点的值为左右子节点的最大值
            tree[node] = Math.max(tree[2 * node + 1], tree[2 * node + 2]);
        }
        
        // 更新数组中某个索引的值
        public void update(int index, int val) {
            updateHelper(0, 0, n - 1, index, val);
        }
        
        // 更新辅助函数
        private void updateHelper(int node, int start, int end, int index, int val) {
            // 找到叶子节点，更新值
            if (start == end) {
                nums[index] = val;
                tree[node] = val;
                return;
            }
            
            // 在左右子树中查找需要更新的索引
            int mid = (start + end) / 2;
            if (index <= mid) {
                // 在左子树中
                updateHelper(2 * node + 1, start, mid, index, val);
            } else {
                // 在右子树中
                updateHelper(2 * node + 2, mid + 1, end, index, val);
            }
            
            // 更新当前节点的值为左右子节点的最大值
            tree[node] = Math.max(tree[2 * node + 1], tree[2 * node + 2]);
        }
        
        // 查询区间最大值
        public int rangeMax(int left, int right) {
            return rangeMaxHelper(0, 0, n - 1, left, right);
        }
        
        // 查询区间最大值辅助函数
        private int rangeMaxHelper(int node, int start, int end, int left, int right) {
            // 当前区间与查询区间无交集
            if (right < start || left > end) {
                // 返回一个不影响结果的值（对于求最大值操作，返回最小值）
                return Integer.MIN_VALUE;
            }
            
            // 当前区间完全包含在查询区间内
            if (left <= start && end <= right) {
                return tree[node];
            }
            
            // 当前区间与查询区间有部分交集，递归查询左右子树
            int mid = (start + end) / 2;
            int leftMax = rangeMaxHelper(2 * node + 1, start, mid, left, right);
            int rightMax = rangeMaxHelper(2 * node + 2, mid + 1, end, left, right);
            return Math.max(leftMax, rightMax);
        }
    }
    
    // 主类实现
    private SegmentTree st;
    
    public Code07_RangeMaxQuery(int[] nums) {
        st = new SegmentTree(nums);
    }
    
    public void update(int index, int val) {
        st.update(index, val);
    }
    
    public int rangeMax(int left, int right) {
        return st.rangeMax(left, right);
    }
    
    // 测试方法
    public static void main(String[] args) {
        int[] nums = {1, 3, 5, 7, 9, 11};
        Code07_RangeMaxQuery rmq = new Code07_RangeMaxQuery(nums);
        
        // 查询索引1到4之间的最大值: max(3, 5, 7, 9) = 9
        System.out.println(rmq.rangeMax(1, 4)); // 输出: 9
        
        // 更新索引2的值为15，数组变为[1, 3, 15, 7, 9, 11]
        rmq.update(2, 15);
        
        // 查询索引1到4之间的最大值: max(3, 15, 7, 9) = 15
        System.out.println(rmq.rangeMax(1, 4)); // 输出: 15
    }
}