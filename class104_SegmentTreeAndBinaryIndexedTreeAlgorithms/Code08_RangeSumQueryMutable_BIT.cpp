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
 * 
 * 工程化考量:
 * 1. 性能优化: 树状数组查询和更新都是O(log n)
 * 2. 内存优化: 树状数组空间复杂度O(n)
 * 3. 边界处理: 处理空数组和非法索引
 * 4. 可读性: 清晰的变量命名和注释
 */

#include <iostream>
#include <vector>
#include <stdexcept>

using namespace std;

class NumArray {
private:
    vector<int> tree;  // 树状数组，用于维护前缀和
    vector<int> nums;  // 原始数组，用于记录原始值以便计算更新差值
    int n;            // 数组长度
    
    /** 
     * 计算x的最低位1所代表的值
     * 这是树状数组的核心操作，用于确定节点的父节点和子节点关系
     * 
     * @param x 输入值
     * @return 最低位1的值
     */
    int lowbit(int x) {
        return x & -x;
    }
    
    /**
     * 树状数组单点更新操作
     * 将位置index的值增加delta
     * 
     * @param index 要更新的位置（树状数组索引从1开始）
     * @param delta 增量值
     */
    void add(int index, int delta) {
        while (index <= n) {
            tree[index] += delta;
            index += lowbit(index);
        }
    }
    
    /**
     * 树状数组前缀和查询
     * 查询前index个元素的和
     * 
     * @param index 查询结束位置（树状数组索引从1开始）
     * @return 前缀和
     */
    int prefixSum(int index) {
        int sum = 0;
        while (index > 0) {
            sum += tree[index];
            index -= lowbit(index);
        }
        return sum;
    }
    
public:
    /** 
     * 构造函数，根据给定数组构建树状数组
     * 
     * @param nums 初始数组
     */
    NumArray(vector<int>& nums) {
        this->n = nums.size();
        this->nums = nums;
        // 树状数组索引从1开始，所以需要n+1的长度
        tree.resize(n + 1, 0);
        
        // 初始化树状数组，将每个元素添加到树状数组中
        for (int i = 0; i < n; i++) {
            add(i + 1, nums[i]);
        }
    }
    
    /**
     * 单点更新操作
     * 将位置index的值更新为val
     * 
     * @param index 要更新的位置（数组索引从0开始）
     * @param val   新的值
     */
    void update(int index, int val) {
        // 参数检查
        if (index < 0 || index >= n) {
            throw out_of_range("Index out of range");
        }
        
        // 计算增量值
        int delta = val - nums[index];
        nums[index] = val;
        
        // 更新树状数组
        add(index + 1, delta);
    }
    
    /**
     * 区间求和操作
     * 计算区间[left, right]内元素的和
     * 
     * @param left  区间左边界
     * @param right 区间右边界
     * @return      区间和
     */
    int sumRange(int left, int right) {
        // 参数检查
        if (left < 0 || right >= n || left > right) {
            throw out_of_range("Invalid range");
        }
        
        // 使用前缀和差值计算区间和
        // sumRange(left, right) = prefixSum(right+1) - prefixSum(left)
        return prefixSum(right + 1) - prefixSum(left);
    }
    
    /**
     * 获取树状数组状态（用于调试）
     * 
     * @return 树状数组内容
     */
    vector<int> getTree() const {
        return tree;
    }
    
    /**
     * 获取原始数组状态（用于调试）
     * 
     * @return 原始数组内容
     */
    vector<int> getNums() const {
        return nums;
    }
};

/**
 * 测试函数，验证算法正确性
 */
void testNumArray() {
    cout << "开始测试树状数组区域和查询..." << endl;
    
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
    
    // 测试用例4: 边界情况
    vector<int> nums4 = {1, 2, 3, 4, 5};
    NumArray numArray4(nums4);
    
    cout << "测试用例4: {1, 2, 3, 4, 5}" << endl;
    cout << "sumRange(1, 3) = " << numArray4.sumRange(1, 3) << " (期望: 9)" << endl;
    assert(numArray4.sumRange(1, 3) == 9 && "测试用例4失败");
    
    cout << "所有测试用例通过！" << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "开始性能测试..." << endl;
    
    // 大规模数据测试
    vector<int> large_nums;
    for (int i = 0; i < 10000; i++) {
        large_nums.push_back(i);
    }
    
    NumArray numArray(large_nums);
    
    // 测试查询性能
    auto start = chrono::high_resolution_clock::now();
    for (int i = 0; i < 1000; i++) {
        numArray.sumRange(0, 9999);
    }
    auto end = chrono::high_resolution_clock::now();
    auto query_time = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    // 测试更新性能
    start = chrono::high_resolution_clock::now();
    for (int i = 0; i < 1000; i++) {
        numArray.update(i % 10000, i);
    }
    end = chrono::high_resolution_clock::now();
    auto update_time = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "大规模测试: 数组长度" << large_nums.size() << endl;
    cout << "1000次查询耗时: " << query_time.count() << "毫秒" << endl;
    cout << "1000次更新耗时: " << update_time.count() << "毫秒" << endl;
}

int main() {
    // 运行测试
    testNumArray();
    
    // 性能测试
    performanceTest();
    
    return 0;
}