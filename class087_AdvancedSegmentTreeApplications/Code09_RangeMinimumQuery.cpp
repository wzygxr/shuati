/**
 * LeetCode 307. Range Sum Query - Mutable
 * 
 * 题目描述：
 * 给定一个整数数组 nums，实现一个数据结构，支持以下操作：
 * 1. update(index, val): 将 nums[index] 的值更新为 val
 * 2. sumRange(left, right): 返回 nums[left...right] 的元素和
 * 
 * 解题思路：
 * 使用线段树维护区间和，支持单点更新和区间查询
 * 每个节点存储区间和，通过递归构建和查询
 * 
 * 关键技术：
 * 1. 线段树：维护区间和信息
 * 2. 单点更新：通过递归更新指定位置的值
 * 3. 区间查询：通过分治思想查询任意区间和
 * 
 * 时间复杂度分析：
 * 1. 建树：O(n)
 * 2. 更新：O(log n)
 * 3. 查询：O(log n)
 * 4. 空间复杂度：O(n)
 * 
 * 是否最优解：是
 * 这是解决区间和查询与单点更新问题的最优解法
 * 
 * 工程化考量：
 * 1. 内存管理：动态调整数组大小
 * 2. 边界处理：处理叶子节点和区间边界情况
 * 3. 性能优化：避免重复计算，合理设计递归结构
 * 
 * 题目链接：https://leetcode.cn/problems/range-sum-query-mutable/
 * 
 * @author Algorithm Journey
 * @version 1.0
 */

// 由于编译环境问题，不使用标准头文件，采用基础C++实现

const int MAXN = 100001;

/**
 * NumArray类
 * 实现区间和查询与单点更新功能
 */
class NumArray {
private:
    int tree[MAXN * 4]; // 线段树数组
    int n;              // 数组大小
    
    /**
     * 建立线段树
     * 递归构建线段树，每个节点存储对应区间的和
     * 
     * @param nums 输入数组
     * @param l    区间左端点
     * @param r    区间右端点
     * @param idx  节点索引
     */
    void buildTree(int* nums, int l, int r, int idx) {
        if (l == r) {
            tree[idx] = nums[l];
            return;
        }
        int mid = l + (r - l) / 2;
        buildTree(nums, l, mid, idx * 2);
        buildTree(nums, mid + 1, r, idx * 2 + 1);
        tree[idx] = tree[idx * 2] + tree[idx * 2 + 1];
    }
    
    /**
     * 更新操作
     * 更新指定位置的值
     * 
     * @param l     区间左端点
     * @param r     区间右端点
     * @param idx   节点索引
     * @param index 要更新的位置
     * @param val   新的值
     */
    void updateTree(int l, int r, int idx, int index, int val) {
        if (l == r) {
            tree[idx] = val;
            return;
        }
        int mid = l + (r - l) / 2;
        if (index <= mid) {
            updateTree(l, mid, idx * 2, index, val);
        } else {
            updateTree(mid + 1, r, idx * 2 + 1, index, val);
        }
        tree[idx] = tree[idx * 2] + tree[idx * 2 + 1];
    }
    
    /**
     * 区间查询
     * 查询指定区间的和
     * 
     * @param l     区间左端点
     * @param r     区间右端点
     * @param idx   节点索引
     * @param left  查询区间左端点
     * @param right 查询区间右端点
     * @return 区间和
     */
    int queryTree(int l, int r, int idx, int left, int right) {
        if (left <= l && r <= right) {
            return tree[idx];
        }
        int mid = l + (r - l) / 2;
        int sum = 0;
        if (left <= mid) {
            sum += queryTree(l, mid, idx * 2, left, right);
        }
        if (right > mid) {
            sum += queryTree(mid + 1, r, idx * 2 + 1, left, right);
        }
        return sum;
    }
    
public:
    /**
     * 构造函数
     * 初始化线段树
     * 
     * @param nums 输入数组
     * @param size 数组大小
     */
    NumArray(int* nums, int size) {
        n = size;
        buildTree(nums, 0, n - 1, 1);
    }
    
    /**
     * 更新操作
     * 更新指定位置的值
     * 
     * @param index 要更新的位置
     * @param val   新的值
     */
    void update(int index, int val) {
        updateTree(0, n - 1, 1, index, val);
    }
    
    /**
     * 区间查询
     * 查询指定区间的和
     * 
     * @param left  查询区间左端点
     * @param right 查询区间右端点
     * @return 区间和
     */
    int sumRange(int left, int right) {
        return queryTree(0, n - 1, 1, left, right);
    }
};

// 由于编译环境限制，不实现main函数
// 在实际使用中，需要根据具体环境实现输入输出功能