#include <iostream>
#include <vector>
using namespace std;

/**
 * LeetCode 307. 区域和检索 - 数组可修改
 * 
 * 题目描述：
 * 给你一个数组 nums ，请你完成两类查询。
 * 1. 其中一类查询要求更新数组 nums 下标对应的值
 * 2. 另一类查询要求返回数组 nums 中索引 left 和索引 right 之间（包含）的nums元素的和，其中 left <= right
 * 
 * 实现 NumArray 类：
 * - NumArray(int[] nums) 用整数数组 nums 初始化对象
 * - void update(int index, int val) 将 nums[index] 的值更新为 val
 * - int sumRange(int left, int right) 返回数组 nums 中索引 left 和索引 right 之间（包含）的nums元素的和
 * 
 * 解题思路：
 * 使用线段树来解决这个问题。线段树是一种非常适合处理区间查询和更新操作的数据结构。
 * 1. 构建线段树：将数组元素组织成一棵二叉树，每个节点存储对应区间的和
 * 2. 更新操作：从叶子节点开始向上更新所有包含该元素的区间和
 * 3. 查询操作：根据查询区间与当前节点区间的重叠关系进行递归查询
 * 
 * 时间复杂度分析：
 * - 初始化：O(n)，需要构建线段树
 * - 更新操作：O(log n)，最多需要更新从叶子节点到根节点路径上的所有节点
 * - 查询操作：O(log n)，最多需要访问O(log n)个节点
 * 
 * 空间复杂度分析：
 * - O(n)，线段树需要4*n的空间来存储节点信息
 * 
 * 链接：https://leetcode.cn/problems/range-sum-query-mutable
 */
class LeetCode307_SegmentTree {
private:
    vector<int> tree;  // 线段树数组，存储区间和
    vector<int> nums;  // 原数组
    int n;             // 数组长度
    
    /**
     * 构建线段树
     * @param start 区间起始位置
     * @param end 区间结束位置
     * @param node 当前节点在tree数组中的索引
     */
    void buildTree(int start, int end, int node) {
        // 如果是叶子节点，直接赋值
        if (start == end) {
            tree[node] = nums[start];
            return;
        }
        
        // 计算中点
        int mid = start + (end - start) / 2;
        // 递归构建左右子树
        buildTree(start, mid, node * 2);
        buildTree(mid + 1, end, node * 2 + 1);
        // 合并左右子树信息
        tree[node] = tree[node * 2] + tree[node * 2 + 1];
    }
    
    /**
     * 更新线段树中的值
     * @param start 当前节点区间起始位置
     * @param end 当前节点区间结束位置
     * @param node 当前节点在tree数组中的索引
     * @param index 要更新的位置
     * @param diff 差值
     */
    void updateTree(int start, int end, int node, int index, int diff) {
        // 如果index不在当前区间范围内，直接返回
        if (start > index || end < index) {
            return;
        }
        
        // 更新当前节点的值
        tree[node] += diff;
        
        // 如果不是叶子节点，递归更新子节点
        if (start != end) {
            int mid = start + (end - start) / 2;
            updateTree(start, mid, node * 2, index, diff);
            updateTree(mid + 1, end, node * 2 + 1, index, diff);
        }
    }
    
    /**
     * 查询线段树中指定区间的和
     * @param start 当前节点区间起始位置
     * @param end 当前节点区间结束位置
     * @param node 当前节点在tree数组中的索引
     * @param left 查询区间左边界
     * @param right 查询区间右边界
     * @return 区间和
     */
    int queryTree(int start, int end, int node, int left, int right) {
        // 如果当前区间与查询区间无重叠，返回0
        if (start > right || end < left) {
            return 0;
        }
        
        // 如果当前区间完全包含在查询区间内，返回当前节点的值
        if (start >= left && end <= right) {
            return tree[node];
        }
        
        // 递归查询左右子树
        int mid = start + (end - start) / 2;
        int leftSum = queryTree(start, mid, node * 2, left, right);
        int rightSum = queryTree(mid + 1, end, node * 2 + 1, left, right);
        
        return leftSum + rightSum;
    }

public:
    /**
     * 构造函数，初始化线段树
     * @param nums 输入数组
     */
    LeetCode307_SegmentTree(vector<int>& nums) {
        this->n = nums.size();
        this->nums = nums;
        // 线段树数组大小通常为4*n，确保足够容纳所有节点
        this->tree.resize(n << 2);
        // 构建线段树
        buildTree(0, n - 1, 1);
    }
    
    /**
     * 更新数组中指定位置的值
     * @param index 要更新的位置
     * @param val 新的值
     */
    void update(int index, int val) {
        // 计算差值
        int diff = val - nums[index];
        // 更新原数组
        nums[index] = val;
        // 更新线段树
        updateTree(0, n - 1, 1, index, diff);
    }
    
    /**
     * 查询区间和
     * @param left 查询区间左边界
     * @param right 查询区间右边界
     * @return 区间和
     */
    int sumRange(int left, int right) {
        return queryTree(0, n - 1, 1, left, right);
    }
};

// 测试函数
int main() {
    cout << "测试LeetCode 307实现..." << endl;
    
    // 测试用例
    vector<int> nums = {1, 3, 5};
    LeetCode307_SegmentTree numArray(nums);
    
    cout << "初始数组: [1, 3, 5]" << endl;
    cout << "查询区间[0,2]的和: " << numArray.sumRange(0, 2) << endl; // 应该输出9
    
    // 更新索引1的值为2
    numArray.update(1, 2);
    cout << "将索引1的值更新为2后:" << endl;
    cout << "查询区间[0,2]的和: " << numArray.sumRange(0, 2) << endl; // 应该输出8
    
    cout << "LeetCode 307测试完成！" << endl;
    
    return 0;
}