package class131;

/** 
 * LeetCode 307. Range Sum Query - Mutable (区域和检索 - 数组可修改) - 树状数组解法
 * 题目链接: https://leetcode.cn/problems/range-sum-query-mutable/
 * 
 * 题目描述: 
 * 给你一个数组 nums ，请你完成两类查询：
 * 1. 更新数组 nums 下标对应的值
 * 2. 求数组 nums 中索引 left 和 right 之间的元素和，包含 left 和 right 两点
 * 
 * 解题思路:
 * 使用树状数组（Binary Indexed Tree/Fenwick Tree）实现
 * 树状数组支持单点更新和前缀和查询，通过前缀和差值计算区间和
 * 
 * 时间复杂度分析:
 * - 构建树状数组: O(n log n)
 * - 单点更新: O(log n)
 * - 区间查询: O(log n)
 * 空间复杂度: O(n) 树状数组只需要n+1的空间
 */
public class Code08_RangeSumQueryMutable_BIT {
    
    // 树状数组，用于维护前缀和
    private int[] tree;
    // 原始数组，用于记录原始值以便计算更新差值
    private int[] nums;
    // 数组长度
    private int n;
    
    /** 
     * 构造函数，根据给定数组构建树状数组
     * 
     * @param nums 初始数组
     */
    public Code08_RangeSumQueryMutable_BIT(int[] nums) {
        this.n = nums.length;
        this.nums = nums;
        // 树状数组索引从1开始，所以需要n+1的长度
        this.tree = new int[n + 1];
        // 初始化树状数组，将每个元素添加到树状数组中
        for (int i = 0; i < n; i++) {
            add(i + 1, nums[i]);
        }
    }
    
    /** 
     * 计算x的最低位1所代表的值
     * 这是树状数组的核心操作，用于确定节点的父节点和子节点关系
     * 
     * @param x 输入值
     * @return  x的最低位1所代表的值
     */
    private int lowbit(int x) {
        return x & (-x);
    }
    
    /** 
     * 在位置i上增加v（树状数组操作）
     * 更新所有包含位置i的节点
     * 
     * @param i 要更新的位置（从1开始计数）
     * @param v 要增加的值
     */
    private void add(int i, int v) {
        // 从位置i开始，沿着父节点路径向上更新所有相关节点
        while (i <= n) {
            tree[i] += v;
            // 移动到父节点：i += lowbit(i)
            i += lowbit(i);
        }
    }
    
    /** 
     * 查询前缀和[1, i]
     * 计算从位置1到位置i的所有元素和
     * 
     * @param i 查询的结束位置（从1开始计数）
     * @return  前缀和
     */
    private int query(int i) {
        int sum = 0;
        // 从位置i开始，沿着子节点路径向下累加所有相关节点的值
        while (i > 0) {
            sum += tree[i];
            // 移动到子节点：i -= lowbit(i)
            i -= lowbit(i);
        }
        return sum;
    }
    
    /** 
     * 更新数组中index位置的值为val
     * 
     * @param index 要更新的数组索引（从0开始计数）
     * @param val   新的值
     */
    public void update(int index, int val) {
        // 计算新旧值的差值
        int delta = val - nums[index];
        // 更新原始数组
        nums[index] = val;
        // 更新树状数组，将差值添加到对应位置
        add(index + 1, delta);
    }
    
    /** 
     * 查询区间[left, right]的和
     * 利用前缀和的性质：区间和 = 前缀和[right+1] - 前缀和[left]
     * 
     * @param left  查询区间起始位置（从0开始计数）
     * @param right 查询区间结束位置（从0开始计数）
     * @return      区间[left, right]内元素的和
     */
    public int sumRange(int left, int right) {
        // 利用前缀和计算区间和: sum[0, right] - sum[0, left-1]
        return query(right + 1) - query(left);
    }
    
    /** 
     * 测试方法
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        int[] nums = {1, 3, 5};
        Code08_RangeSumQueryMutable_BIT numArray = new Code08_RangeSumQueryMutable_BIT(nums);
        
        System.out.println("Initial sum from index 0 to 2: " + numArray.sumRange(0, 2)); // 应该输出9
        
        numArray.update(1, 2); // 将索引1的值从3更新为2
        System.out.println("Sum from index 0 to 2 after update: " + numArray.sumRange(0, 2)); // 应该输出8
    }
}