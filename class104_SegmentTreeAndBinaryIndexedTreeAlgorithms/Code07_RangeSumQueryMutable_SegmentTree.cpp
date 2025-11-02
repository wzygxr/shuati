/**
 * LeetCode 307. Range Sum Query - Mutable (区域和检索 - 数组可修改)
 * 题目链接: https://leetcode.cn/problems/range-sum-query-mutable/
 * 
 * 题目描述: 
 * 给你一个数组 nums ，请你完成两类查询：
 * 1. 更新数组 nums 下标对应的值
 * 2. 求数组 nums 中索引 left 和 right 之间的元素和，包含 left 和 right 两点
 * 
 * 解题思路:
 * 使用线段树实现，支持单点更新和区间查询
 * 线段树每个节点存储对应区间的元素和
 * 
 * 时间复杂度分析:
 * - 构建线段树: O(n)
 * - 单点更新: O(log n)
 * - 区间查询: O(log n)
 * 空间复杂度: O(4n) 线段树需要约4n的空间
 * 
 * 工程化考量:
 * 1. 性能优化: 线段树查询和更新都是O(log n)
 * 2. 内存优化: 动态分配节点，避免内存浪费
 * 3. 边界处理: 处理空数组和非法索引
 * 4. 可读性: 清晰的变量命名和注释
 */

#include <iostream>
#include <vector>
#include <stdexcept>

using namespace std;

/** 
 * 线段树节点定义
 * 每个节点表示数组的一个区间[start, end]，并存储该区间内所有元素的和
 */
class SegmentTreeNode {
public:
    int start, end;           // 节点表示的区间范围
    SegmentTreeNode* left;    // 左子节点
    SegmentTreeNode* right;   // 右子节点
    int sum;                  // 区间内元素的和
    
    /** 
     * 构造函数
     * 
     * @param start 区间起始位置
     * @param end   区间结束位置
     */
    SegmentTreeNode(int start, int end) : start(start), end(end), left(nullptr), right(nullptr), sum(0) {}
    
    /**
     * 析构函数，递归删除子节点
     */
    ~SegmentTreeNode() {
        delete left;
        delete right;
    }
};

class NumArray {
private:
    SegmentTreeNode* root;  // 线段树根节点
    vector<int> nums;       // 原始数组副本
    
    /**
     * 构建线段树
     * 
     * @param start 区间起始位置
     * @param end   区间结束位置
     * @param nums  原始数组
     * @return      线段树节点
     */
    SegmentTreeNode* buildTree(int start, int end, const vector<int>& nums) {
        if (start > end) {
            return nullptr;
        }
        
        SegmentTreeNode* node = new SegmentTreeNode(start, end);
        
        if (start == end) {
            // 叶节点，直接存储数组元素值
            node->sum = nums[start];
        } else {
            // 递归构建左右子树
            int mid = start + (end - start) / 2;
            node->left = buildTree(start, mid, nums);
            node->right = buildTree(mid + 1, end, nums);
            
            // 计算当前节点的和
            node->sum = (node->left ? node->left->sum : 0) + 
                       (node->right ? node->right->sum : 0);
        }
        
        return node;
    }
    
    /**
     * 线段树单点更新
     * 
     * @param node  当前节点
     * @param index 要更新的位置
     * @param val   新的值
     */
    void updateTree(SegmentTreeNode* node, int index, int val) {
        if (!node || index < node->start || index > node->end) {
            return;
        }
        
        if (node->start == node->end && node->start == index) {
            // 找到目标叶节点
            node->sum = val;
        } else {
            // 递归更新左右子树
            int mid = node->start + (node->end - node->start) / 2;
            if (index <= mid) {
                updateTree(node->left, index, val);
            } else {
                updateTree(node->right, index, val);
            }
            
            // 更新当前节点的和
            node->sum = (node->left ? node->left->sum : 0) + 
                       (node->right ? node->right->sum : 0);
        }
    }
    
    /**
     * 线段树区间查询
     * 
     * @param node  当前节点
     * @param left  查询区间左边界
     * @param right 查询区间右边界
     * @return      区间和
     */
    int queryTree(SegmentTreeNode* node, int left, int right) {
        if (!node || left > node->end || right < node->start) {
            return 0;
        }
        
        if (left <= node->start && node->end <= right) {
            // 当前节点区间完全包含在查询区间内
            return node->sum;
        }
        
        // 递归查询左右子树
        int mid = node->start + (node->end - node->start) / 2;
        int leftSum = 0, rightSum = 0;
        
        if (left <= mid) {
            leftSum = queryTree(node->left, left, right);
        }
        if (right > mid) {
            rightSum = queryTree(node->right, left, right);
        }
        
        return leftSum + rightSum;
    }
    
public:
    /**
     * 构造函数
     * 
     * @param nums 输入数组
     */
    NumArray(vector<int>& nums) {
        this->nums = nums;
        if (!nums.empty()) {
            root = buildTree(0, nums.size() - 1, nums);
        } else {
            root = nullptr;
        }
    }
    
    /**
     * 析构函数
     */
    ~NumArray() {
        delete root;
    }
    
    /**
     * 单点更新操作
     * 
     * @param index 要更新的位置
     * @param val   新的值
     */
    void update(int index, int val) {
        // 参数检查
        if (index < 0 || index >= nums.size()) {
            throw out_of_range("Index out of range");
        }
        
        nums[index] = val;
        if (root) {
            updateTree(root, index, val);
        }
    }
    
    /**
     * 区间求和操作
     * 
     * @param left  区间左边界
     * @param right 区间右边界
     * @return      区间和
     */
    int sumRange(int left, int right) {
        // 参数检查
        if (left < 0 || right >= nums.size() || left > right) {
            throw out_of_range("Invalid range");
        }
        
        if (!root) {
            return 0;
        }
        
        return queryTree(root, left, right);
    }
};

/**
 * 测试函数，验证算法正确性
 */
void testNumArray() {
    cout << "开始测试线段树区域和查询..." << endl;
    
    // 测试用例1: 正常情况
    vector<int> nums1 = {1, 3, 5};
    NumArray numArray1(nums1);
    
    cout << "测试用例1: 初始数组 {1, 3, 5}" << endl;
    cout << "sumRange(0, 2) = " << numArray1.sumRange(0, 2) << " (期望: 9)" << endl;
    assert(numArray1.sumRange(0, 2) == 9 && "测试用例1失败");
    
    numArray1.update(1, 2);
    cout << "更新index=1为2后，sumRange(0, 2) = " << numArray1.sumRange(0, 2) << " (期望: 8)" << endl;
    assert(numArray1.sumRange(0, 2) == 8 && "测试用例1更新失败");
    
    // 测试用例2: 空数组
    vector<int> nums2;
    NumArray numArray2(nums2);
    
    try {
        numArray2.sumRange(0, 0);
        assert(false && "应该抛出异常");
    } catch (const out_of_range& e) {
        cout << "测试用例2: 空数组异常处理通过 - " << e.what() << endl;
    }
    
    // 测试用例3: 单元素数组
    vector<int> nums3 = {7};
    NumArray numArray3(nums3);
    
    cout << "测试用例3: 单元素数组 {7}" << endl;
    cout << "sumRange(0, 0) = " << numArray3.sumRange(0, 0) << " (期望: 7)" << endl;
    assert(numArray3.sumRange(0, 0) == 7 && "测试用例3失败");
    
    numArray3.update(0, 10);
    cout << "更新index=0为10后，sumRange(0, 0) = " << numArray3.sumRange(0, 0) << " (期望: 10)" << endl;
    assert(numArray3.sumRange(0, 0) == 10 && "测试用例3更新失败");
    
    cout << "所有测试用例通过！" << endl;
}

int main() {
    // 运行测试
    testNumArray();
    
    return 0;
}