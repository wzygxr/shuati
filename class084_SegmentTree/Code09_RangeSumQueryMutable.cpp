// Range Sum Query - Mutable (区间求和 - 可变)
// 题目来源: LeetCode 307. Range Sum Query - Mutable
// 题目链接: https://leetcode.cn/problems/range-sum-query-mutable
// 题目链接: https://leetcode.com/problems/range-sum-query-mutable
// 
// 题目描述:
// 给你一个数组 nums ，请你完成两类查询：
// 1. 一类查询要求更新数组 nums 下标对应的值
// 2. 一类查询要求返回数组 nums 中，索引 left 和 right 之间的元素之和，包含 left 和 right 两点
// 实现 NumArray 类：
// NumArray(int[] nums) 用整数数组 nums 初始化对象
// void update(int index, int val) 将 nums[index] 的值更新为 val
// int sumRange(int left, int right) 返回数组 nums 中索引 left 和索引 right 之间
// (包含)的元素之和
//
// 解题思路:
// 1. 使用线段树维护数组区间和信息
// 2. 支持单点更新和区间查询操作
// 3. 线段树的每个节点存储对应区间的元素和
// 4. 更新操作从根节点到叶子节点递归更新路径上的所有节点
// 5. 查询操作根据查询区间与节点区间的关系进行递归查询
//
// 时间复杂度: 
// - 构建: O(n)
// - 更新: O(log n)
// - 查询: O(log n)
// 空间复杂度: O(n)

#include <vector>
#include <algorithm>
using namespace std;

// 由于编译环境限制，不使用<iostream>等标准库头文件
// 使用简单的数组实现

const int MAXN = 100005;
int nums[MAXN];
int tree[4 * MAXN];
int n;

class SegmentTree {
private:
    // 构建线段树
    // node是线段树节点的索引
    // start和end是数组区间
    void buildTree(int node, int start, int end) {
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
        // 更新当前节点的值为左右子节点值的和
        tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
    }

public:
    SegmentTree(int arr[], int size) {
        n = size;
        // 复制数组
        for (int i = 0; i < size; i++) {
            nums[i] = arr[i];
        }
        // 构建线段树
        buildTree(0, 0, n - 1);
    }

    // 更新数组中某个索引的值
    void update(int index, int val) {
        updateHelper(0, 0, n - 1, index, val);
    }

    // 更新辅助函数
    void updateHelper(int node, int start, int end, int index, int val) {
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

        // 更新当前节点的值
        tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
    }

    // 查询区间和
    int sumRange(int left, int right) {
        return sumRangeHelper(0, 0, n - 1, left, right);
    }

    // 查询区间和辅助函数
    int sumRangeHelper(int node, int start, int end, int left, int right) {
        // 当前区间与查询区间无交集
        if (right < start || left > end) {
            return 0;
        }

        // 当前区间完全包含在查询区间内
        if (left <= start && end <= right) {
            return tree[node];
        }

        // 当前区间与查询区间有部分交集，递归查询左右子树
        int mid = (start + end) / 2;
        int leftSum = sumRangeHelper(2 * node + 1, start, mid, left, right);
        int rightSum = sumRangeHelper(2 * node + 2, mid + 1, end, left, right);
        return leftSum + rightSum;
    }
};

// 由于编译环境限制，不提供main函数测试
// 可以通过创建SegmentTree对象并调用其方法来使用