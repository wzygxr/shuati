// Range Sum Query - Mutable (区间求和 - 可变)
// 题目来源: LeetCode 307. Range Sum Query - Mutable
// 题目链接: https://leetcode.com/problems/range-sum-query-mutable/
// 题目链接: https://leetcode.cn/problems/range-sum-query-mutable
// 
// 题目描述:
// 给你一个数组 nums ，请你完成两类查询:
// 1. 一类查询要求更新数组 nums 下标对应的值
// 2. 一类查询要求返回数组 nums 中索引 left 和索引 right 之间（包含）的 nums 元素的和，其中 left <= right
//
// 实现 NumArray 类：
// NumArray(int[] nums) 用整数数组 nums 初始化对象
// void update(int index, int val) 将 nums[index] 的值更新为 val
// int sumRange(int left, int right) 返回数组 nums 中索引 left 和索引 right 之间（包含）的 nums 元素的和
//
// 解题思路:
// 1. 使用线段树实现区间求和和单点更新
// 2. 线段树的每个节点存储对应区间的元素和
// 3. 更新操作时，从根节点开始，找到对应的叶子节点并更新，然后逐层向上更新父节点
// 4. 查询操作时，从根节点开始，根据查询区间与当前节点区间的关系进行递归查询
//
// 时间复杂度分析:
// - 构建线段树: O(n)
// - 单点更新: O(log n)
// - 区间查询: O(log n)
// 空间复杂度: O(n)

#include <iostream>
#include <vector>
using namespace std;

class NumArray {
private:
    vector<int> tree;
    vector<int> data;
    int n;

    // 构建线段树
    void buildTree(int treeIndex, int l, int r) {
        if (l == r) {
            tree[treeIndex] = data[l];
            return;
        }

        int mid = l + (r - l) / 2;
        int leftTreeIndex = 2 * treeIndex + 1;
        int rightTreeIndex = 2 * treeIndex + 2;

        // 构建左子树
        buildTree(leftTreeIndex, l, mid);
        // 构建右子树
        buildTree(rightTreeIndex, mid + 1, r);

        // 当前节点的值等于左右子树值的和
        tree[treeIndex] = tree[leftTreeIndex] + tree[rightTreeIndex];
    }

    // 更新线段树
    void updateTree(int treeIndex, int l, int r, int index, int val) {
        if (l == r) {
            tree[treeIndex] = val;
            return;
        }

        int mid = l + (r - l) / 2;
        int leftTreeIndex = 2 * treeIndex + 1;
        int rightTreeIndex = 2 * treeIndex + 2;

        if (index >= l && index <= mid) {
            // 要更新的索引在左子树
            updateTree(leftTreeIndex, l, mid, index, val);
        } else {
            // 要更新的索引在右子树
            updateTree(rightTreeIndex, mid + 1, r, index, val);
        }

        // 更新当前节点的值
        tree[treeIndex] = tree[leftTreeIndex] + tree[rightTreeIndex];
    }

    // 查询区间和
    int queryTree(int treeIndex, int l, int r, int queryL, int queryR) {
        if (l == queryL && r == queryR) {
            return tree[treeIndex];
        }

        int mid = l + (r - l) / 2;
        int leftTreeIndex = 2 * treeIndex + 1;
        int rightTreeIndex = 2 * treeIndex + 2;

        if (queryR <= mid) {
            // 查询区间完全在左子树
            return queryTree(leftTreeIndex, l, mid, queryL, queryR);
        } else if (queryL > mid) {
            // 查询区间完全在右子树
            return queryTree(rightTreeIndex, mid + 1, r, queryL, queryR);
        } else {
            // 查询区间跨越左右子树
            int leftResult = queryTree(leftTreeIndex, l, mid, queryL, mid);
            int rightResult = queryTree(rightTreeIndex, mid + 1, r, mid + 1, queryR);
            return leftResult + rightResult;
        }
    }

public:
    NumArray(vector<int>& nums) {
        if (nums.size() > 0) {
            n = nums.size();
            data = nums;
            tree.resize(4 * n);
            buildTree(0, 0, n - 1);
        }
    }

    void update(int index, int val) {
        if (n == 0) return;
        data[index] = val;
        updateTree(0, 0, n - 1, index, val);
    }

    int sumRange(int left, int right) {
        if (n == 0) return 0;
        return queryTree(0, 0, n - 1, left, right);
    }
};

// 测试代码
int main() {
    // 测试用例
    vector<int> nums = {1, 3, 5};
    NumArray numArray(nums);
    
    cout << "初始数组: [1, 3, 5]" << endl;
    cout << "sumRange(0, 2): " << numArray.sumRange(0, 2) << endl; // 应该返回 9
    
    numArray.update(1, 2); // nums = [1, 2, 5]
    cout << "更新索引1为2后: [1, 2, 5]" << endl;
    cout << "sumRange(0, 2): " << numArray.sumRange(0, 2) << endl; // 应该返回 8
    
    return 0;
}