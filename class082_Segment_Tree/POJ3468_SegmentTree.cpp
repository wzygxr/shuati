#include <iostream>
#include <vector>
using namespace std;

/**
 * POJ 3468 A Simple Problem with Integers
 * 
 * 题目描述：
 * 给定一个长度为N的整数数组A，初始时A[i] = 0 (1 <= i <= N)。
 * 需要处理以下两种操作：
 * 1. C a b c: 将区间[a, b]中的每个元素都加上c
 * 2. Q a b: 查询区间[a, b]中所有元素的和
 * 
 * 解题思路：
 * 使用线段树配合懒标记(Lazy Propagation)来解决区间更新问题。
 * 1. 线段树节点维护区间和
 * 2. 懒标记用于延迟区间更新操作，避免每次都更新到叶子节点
 * 3. 在需要访问子节点时，将懒标记下推(push down)
 * 
 * 时间复杂度分析：
 * - 初始化：O(n)
 * - 区间更新：O(log n)
 * - 区间查询：O(log n)
 * 
 * 空间复杂度分析：
 * - O(n)，线段树需要4*n的空间来存储节点信息
 * 
 * 链接：http://poj.org/problem?id=3468
 */
class POJ3468_SegmentTree {
private:
    vector<long long> tree;  // 线段树数组，存储区间和
    vector<long long> lazy;  // 懒标记数组，存储区间延迟更新的值
    vector<int> nums;        // 原数组
    int n;                   // 数组长度
    
    /**
     * 构建线段树
     * @param start 区间起始位置
     * @param end 区间结束位置
     * @param node 当前节点在tree数组中的索引
     */
    void buildTree(int start, int end, int node) {
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
        // 合并左右子树信息
        tree[node] = tree[node * 2] + tree[node * 2 + 1];
    }
    
    /**
     * 下推懒标记
     * @param node 当前节点
     * @param start 当前节点区间起始位置
     * @param end 当前节点区间结束位置
     */
    void pushDown(int node, int start, int end) {
        if (lazy[node] != 0) {
            // 将懒标记下推到左右子节点
            lazy[node * 2] += lazy[node];
            lazy[node * 2 + 1] += lazy[node];
            
            // 如果不是叶子节点，更新子节点的值
            if (start != end) {
                int mid = (start + end) / 2;
                tree[node * 2] += lazy[node] * (mid - start + 1);
                tree[node * 2 + 1] += lazy[node] * (end - mid);
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
    void updateRange(int start, int end, int node, int left, int right, long long val) {
        // 如果当前区间与更新区间无重叠，直接返回
        if (start > right || end < left) {
            return;
        }
        
        // 如果当前区间完全包含在更新区间内
        if (start >= left && end <= right) {
            // 更新当前节点的值
            tree[node] += val * (end - start + 1);
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
        
        // 合并左右子树信息
        tree[node] = tree[node * 2] + tree[node * 2 + 1];
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
    long long queryRange(int start, int end, int node, int left, int right) {
        // 如果当前区间与查询区间无重叠，返回0
        if (start > right || end < left) {
            return 0;
        }
        
        // 如果当前区间完全包含在查询区间内
        if (start >= left && end <= right) {
            return tree[node];
        }
        
        // 下推懒标记
        pushDown(node, start, end);
        
        // 递归查询左右子树
        int mid = (start + end) / 2;
        long long leftSum = queryRange(start, mid, node * 2, left, right);
        long long rightSum = queryRange(mid + 1, end, node * 2 + 1, left, right);
        
        return leftSum + rightSum;
    }

public:
    /**
     * 构造函数，初始化线段树
     * @param nums 输入数组
     */
    POJ3468_SegmentTree(vector<int>& nums) {
        this->n = nums.size();
        this->nums = nums;
        // 线段树数组大小通常为4*n，确保足够容纳所有节点
        this->tree.resize(n << 2);
        this->lazy.resize(n << 2);
        // 构建线段树
        buildTree(0, n - 1, 1);
    }
    
    /**
     * 区间更新接口
     * @param left 更新区间左边界
     * @param right 更新区间右边界
     * @param val 更新值
     */
    void update(int left, int right, long long val) {
        updateRange(0, n - 1, 1, left, right, val);
    }
    
    /**
     * 区间查询接口
     * @param left 查询区间左边界
     * @param right 查询区间右边界
     * @return 区间和
     */
    long long query(int left, int right) {
        return queryRange(0, n - 1, 1, left, right);
    }
};

// 测试函数
int main() {
    cout << "测试POJ 3468实现..." << endl;
    
    // 测试用例
    vector<int> nums = {1, 2, 3, 4, 5};
    POJ3468_SegmentTree segTree(nums);
    
    cout << "初始数组: [1, 2, 3, 4, 5]" << endl;
    cout << "查询区间[1,3]的和: " << segTree.query(1, 3) << endl; // 应该输出9
    
    // 区间更新：将区间[1,3]中的每个元素都加上2
    segTree.update(1, 3, 2);
    cout << "将区间[1,3]中的每个元素都加上2后:" << endl;
    cout << "查询区间[1,3]的和: " << segTree.query(1, 3) << endl; // 应该输出15
    
    cout << "POJ 3468测试完成！" << endl;
    
    return 0;
}