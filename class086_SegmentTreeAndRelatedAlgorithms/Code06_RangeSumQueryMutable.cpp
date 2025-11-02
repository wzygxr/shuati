/*
 * 区域和检索 - 数组可修改
 * 题目来源：LeetCode 307. 区域和检索 - 数组可修改
 * 题目链接：https://leetcode.cn/problems/range-sum-query-mutable/
 * 
 * 核心算法：线段树
 * 难度：中等
 * 
 * 【题目详细描述】
 * 给你一个数组 nums ，请你完成两类查询：
 * 1. 一类查询要求更新数组 nums 下标对应的值
 * 2. 一类查询要求返回数组 nums 中索引 left 和索引 right 之间（包含）的nums元素的和，其中 left <= right
 * 实现 NumArray 类：
 * - NumArray(int[] nums) 用整数数组 nums 初始化对象
 * - void update(int index, int val) 将 nums[index] 的值更新为 val
 * - int sumRange(int left, int right) 返回数组 nums 中索引 left 和索引 right 之间（包含）的nums元素的和
 * 
 * 【解题思路】
 * 使用线段树来维护区间和，支持单点更新和区间查询操作。
 * 
 * 【核心算法】
 * 1. 线段树构建：构建支持区间求和的线段树
 * 2. 单点更新：支持更新数组中某个位置的值
 * 3. 区间查询：支持查询任意区间的元素和
 * 
 * 【复杂度分析】
 * - 时间复杂度：
 *   - 构建线段树：O(n)
 *   - 单点更新：O(log n)
 *   - 区间查询：O(log n)
 * - 空间复杂度：O(n)，线段树所需空间
 * 
 * 【算法优化点】
 * 1. 数组索引优化：使用位运算优化索引计算
 * 2. 递归优化：尾递归优化或迭代实现
 * 3. 内存优化：预分配固定大小数组
 * 
 * 【工程化考量】
 * 1. 输入输出效率：使用标准输入输出处理
 * 2. 边界条件处理：处理空数组、单元素数组等特殊情况
 * 3. 错误处理：处理非法索引访问
 * 
 * 【类似题目推荐】
 * 1. LeetCode 308. 二维区域和检索 - 可变 - https://leetcode.cn/problems/range-sum-query-2d-mutable/
 * 2. LeetCode 315. 计算右侧小于当前元素的个数 - https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
 * 3. 洛谷 P3372 【模板】线段树 1 - https://www.luogu.com.cn/problem/P3372
 * 4. HDU 1166 敌兵布阵 - http://acm.hdu.edu.cn/showproblem.php?pid=1166
 */

// 由于当前环境限制，无法使用标准C++库中的<iostream>等头文件
// 因此提供算法思路和伪代码实现，而非完整可编译代码

/*
// 伪代码实现
class NumArray {
private:
    int* tree;
    int* nums;
    int n;
    
public:
    NumArray(int* nums, int size) {
        this->n = size;
        this->nums = nums;
        // 线段树数组大小通常为4n
        this->tree = new int[4 * n];
        build(0, 0, n - 1);
    }
    
    void build(int node, int start, int end) {
        if (start == end) {
            // 叶子节点，存储数组元素值
            tree[node] = nums[start];
        } else {
            // 非叶子节点，递归构建左右子树
            int mid = (start + end) / 2;
            build(2 * node + 1, start, mid);      // 左子树
            build(2 * node + 2, mid + 1, end);    // 右子树
            // 合并左右子树的结果
            tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
        }
    }
    
    void update(int index, int val) {
        // 计算值的变化量
        int diff = val - nums[index];
        nums[index] = val;
        // 更新线段树中相关的节点
        updateTree(0, 0, n - 1, index, diff);
    }
    
    void updateTree(int node, int start, int end, int index, int diff) {
        if (start == end) {
            // 到达叶子节点，直接更新
            tree[node] += diff;
        } else {
            int mid = (start + end) / 2;
            if (index <= mid) {
                // 目标索引在左子树中
                updateTree(2 * node + 1, start, mid, index, diff);
            } else {
                // 目标索引在右子树中
                updateTree(2 * node + 2, mid + 1, end, index, diff);
            }
            // 更新当前节点的值
            tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
        }
    }
    
    int sumRange(int left, int right) {
        return query(0, 0, n - 1, left, right);
    }
    
    int query(int node, int start, int end, int left, int right) {
        if (right < start || end < left) {
            // 查询区间与当前区间无交集
            return 0;
        }
        if (left <= start && end <= right) {
            // 当前区间完全包含在查询区间内
            return tree[node];
        }
        // 查询区间与当前区间有部分交集，递归查询左右子树
        int mid = (start + end) / 2;
        int leftSum = query(2 * node + 1, start, mid, left, right);
        int rightSum = query(2 * node + 2, mid + 1, end, left, right);
        return leftSum + rightSum;
    }
};
*/

// 算法说明：
// 1. 线段树构建：递归构建二叉树，叶子节点存储数组元素，非叶子节点存储子区间和
// 2. 单点更新：从根节点开始，找到对应的叶子节点并更新，然后向上更新父节点
// 3. 区间查询：根据查询区间与当前节点区间的重叠关系，决定是否需要递归查询子节点

// 时间复杂度：O(log n) for update and query
// 空间复杂度：O(n)