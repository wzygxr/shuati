package class109;

/**
 * HDU 1166 敌兵布阵
 * 
 * 题目描述：
 * A国在海岸线沿直线布置了N个工兵营地，每个营地初始有一定数量的士兵。
 * 有两种操作：
 * 1. Add i j: 第i个营地增加j个士兵
 * 2. Query i j: 查询第i到第j个营地之间士兵总数
 * 
 * 解题思路：
 * 这是一个典型的线段树单点更新、区间查询问题。
 * 1. 构建线段树存储每个区间的士兵总数
 * 2. Add操作对应线段树的单点更新
 * 3. Query操作对应线段树的区间查询
 * 
 * 时间复杂度分析：
 * - 初始化：O(n)
 * - 单点更新：O(log n)
 * - 区间查询：O(log n)
 * 
 * 空间复杂度分析：
 * - O(n)，线段树需要4*n的空间来存储节点信息
 * 
 * 链接：http://acm.hdu.edu.cn/showproblem.php?pid=1166
 */
public class HDU1166_SegmentTree {
    
    // 线段树数组，存储区间和
    private int[] tree;
    // 原数组
    private int[] nums;
    // 数组长度
    private int n;
    
    /**
     * 构造函数，初始化线段树
     * @param nums 输入数组
     */
    public HDU1166_SegmentTree(int[] nums) {
        this.n = nums.length;
        this.nums = nums;
        // 线段树数组大小通常为4*n，确保足够容纳所有节点
        this.tree = new int[n << 2];
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
        // 合并左右子树信息
        tree[node] = tree[node * 2] + tree[node * 2 + 1];
    }
    
    /**
     * 单点更新
     * @param start 当前节点区间起始位置
     * @param end 当前节点区间结束位置
     * @param node 当前节点在tree数组中的索引
     * @param index 要更新的位置
     * @param val 增加的值
     */
    private void updatePoint(int start, int end, int node, int index, int val) {
        // 如果index不在当前区间范围内，直接返回
        if (start > index || end < index) {
            return;
        }
        
        // 如果是叶子节点，直接增加计数
        if (start == end) {
            tree[node] += val;
            return;
        }
        
        // 递归更新子节点
        int mid = (start + end) / 2;
        if (index <= mid) {
            updatePoint(start, mid, node * 2, index, val);
        } else {
            updatePoint(mid + 1, end, node * 2 + 1, index, val);
        }
        
        // 合并左右子树信息
        tree[node] = tree[node * 2] + tree[node * 2 + 1];
    }
    
    /**
     * 单点更新接口
     * @param index 要更新的位置
     * @param val 增加的值
     */
    public void add(int index, int val) {
        updatePoint(0, n - 1, 1, index, val);
    }
    
    /**
     * 区间查询
     * @param start 当前节点区间起始位置
     * @param end 当前节点区间结束位置
     * @param node 当前节点在tree数组中的索引
     * @param left 查询区间左边界
     * @param right 查询区间右边界
     * @return 区间和
     */
    private int queryRange(int start, int end, int node, int left, int right) {
        // 如果当前区间与查询区间无重叠，返回0
        if (start > right || end < left) {
            return 0;
        }
        
        // 如果当前区间完全包含在查询区间内，返回当前节点的值
        if (start >= left && end <= right) {
            return tree[node];
        }
        
        // 递归查询左右子树
        int mid = (start + end) / 2;
        int leftSum = queryRange(start, mid, node * 2, left, right);
        int rightSum = queryRange(mid + 1, end, node * 2 + 1, left, right);
        
        return leftSum + rightSum;
    }
    
    /**
     * 区间查询接口
     * @param left 查询区间左边界
     * @param right 查询区间右边界
     * @return 区间和
     */
    public int query(int left, int right) {
        return queryRange(0, n - 1, 1, left, right);
    }
    
    // 测试方法
    public static void main(String[] args) {
        System.out.println("测试HDU 1166实现...");
        
        // 测试用例
        int[] nums = {1, 2, 3, 4, 5};
        HDU1166_SegmentTree segTree = new HDU1166_SegmentTree(nums);
        
        System.out.println("初始数组: [1, 2, 3, 4, 5]");
        System.out.println("查询区间[1,3]的和: " + segTree.query(1, 3)); // 应该输出9
        
        // 单点更新：第2个营地增加3个士兵
        segTree.add(2, 3);
        System.out.println("第2个营地增加3个士兵后:");
        System.out.println("查询区间[1,3]的和: " + segTree.query(1, 3)); // 应该输出12
        
        System.out.println("HDU 1166测试完成！");
    }
}