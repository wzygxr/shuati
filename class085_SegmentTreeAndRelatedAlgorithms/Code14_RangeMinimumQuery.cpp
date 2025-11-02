// 307. 区域和检索 - 数组可修改
// 给你一个数组 nums ，请你完成两类查询。
// 其中一类查询要求更新数组 nums 下标对应的值
// 另一类查询要求返回数组 nums 中索引 left 和索引 right 之间（包含）的nums元素的和，其中 left <= right
// 实现 NumArray 类：
// NumArray(int[] nums) 用整数数组 nums 初始化对象
// void update(int index, int val) 将 nums[index] 的值更新为 val
// int sumRange(int left, int right) 返回数组 nums 中索引 left 和索引 right 之间（包含）的nums元素的和
// 测试链接 : https://leetcode.cn/problems/range-sum-query-mutable/
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

class Code14_RangeMinimumQuery {
private:
    // 线段树实现
    class SegmentTree {
    private:
        vector<int> tree;
        int n;
        
        // 构建线段树
        // 时间复杂度: O(n)
        // 空间复杂度: O(n)
        void buildTree(const vector<int>& nums, int node, int start, int end) {
            if (start == end) {
                // 叶子节点
                tree[node] = nums[start];
            } else {
                int mid = (start + end) / 2;
                // 递归构建左子树
                buildTree(nums, 2 * node + 1, start, mid);
                // 递归构建右子树
                buildTree(nums, 2 * node + 2, mid + 1, end);
                // 合并左右子树的结果
                tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
            }
        }
        
        // 更新辅助函数
        // 时间复杂度: O(log n)
        void updateHelper(int node, int start, int end, int index, int val) {
            if (start == end) {
                // 找到叶子节点，更新值
                tree[node] = val;
            } else {
                int mid = (start + end) / 2;
                if (index <= mid) {
                    // 在左子树中更新
                    updateHelper(2 * node + 1, start, mid, index, val);
                } else {
                    // 在右子树中更新
                    updateHelper(2 * node + 2, mid + 1, end, index, val);
                }
                // 更新父节点的值
                tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
            }
        }
        
        // 查询区间和辅助函数
        // 时间复杂度: O(log n)
        int sumRangeHelper(int node, int start, int end, int left, int right) {
            if (right < start || end < left) {
                // 查询区间与当前区间无交集
                return 0;
            }
            if (left <= start && end <= right) {
                // 当前区间完全包含在查询区间内
                return tree[node];
            }
            // 部分重叠，递归查询左右子树
            int mid = (start + end) / 2;
            return sumRangeHelper(2 * node + 1, start, mid, left, right) +
                   sumRangeHelper(2 * node + 2, mid + 1, end, left, right);
        }
        
    public:
        SegmentTree(const vector<int>& nums) {
            n = nums.size();
            tree.resize(n * 4); // 线段树通常需要4倍空间
            buildTree(nums, 0, 0, n - 1);
        }
        
        // 更新数组中某个位置的值
        // 时间复杂度: O(log n)
        void update(int index, int val) {
            updateHelper(0, 0, n - 1, index, val);
        }
        
        // 查询区间和
        // 时间复杂度: O(log n)
        int sumRange(int left, int right) {
            return sumRangeHelper(0, 0, n - 1, left, right);
        }
    };
    
    SegmentTree st;
    
public:
    // 构造函数
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    Code14_RangeMinimumQuery(vector<int>& nums) : st(nums) {}
    
    // 更新数组中某个位置的值
    // 时间复杂度: O(log n)
    void update(int index, int val) {
        st.update(index, val);
    }
    
    // 查询区间和
    // 时间复杂度: O(log n)
    int sumRange(int left, int right) {
        return st.sumRange(left, right);
    }
};

// 测试方法
int main() {
    // 测试用例:
    // ["NumArray", "sumRange", "update", "sumRange"]
    // [[[1, 3, 5]], [0, 2], [1, 2], [0, 2]]
    // 输出:
    // [null, 9, null, 8]
    
    vector<int> nums = {1, 3, 5};
    Code14_RangeMinimumQuery numArray(nums);
    cout << numArray.sumRange(0, 2) << endl; // 应该输出 9
    numArray.update(1, 2);   // nums = [1,2,5]
    cout << numArray.sumRange(0, 2) << endl; // 应该输出 8
    
    return 0;
}