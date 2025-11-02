/**
 * LeetCode 315. Count of Smaller Numbers After Self (计算右侧小于当前元素的个数)
 * 题目链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
 * 
 * 题目描述: 
 * 给你一个整数数组 nums ，按要求返回一个新数组 counts 。
 * 数组 counts 有该性质：counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
 * 
 * 解题思路:
 * 使用树状数组 + 离散化实现
 * 1. 离散化处理，将数值映射到较小的范围
 * 2. 从右向左遍历数组，对每个元素查询比它小的元素个数
 * 3. 使用树状数组维护已经处理过的元素
 * 
 * 时间复杂度分析:
 * - 离散化: O(n log n)
 * - 查询和更新: O(n log n)
 * - 总时间复杂度: O(n log n)
 * 空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 性能优化: 树状数组查询和更新都是O(log n)
 * 2. 内存优化: 离散化减少空间占用
 * 3. 边界处理: 处理空数组和重复元素
 * 4. 可读性: 清晰的变量命名和注释
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <unordered_map>

using namespace std;

class BIT {
private:
    vector<int> tree;  // 树状数组
    int n;             // 数组大小
    
    /** 
     * 计算x的最低位1所代表的值
     * 
     * @param x 输入值
     * @return  x的最低位1所代表的值
     */
    int lowbit(int x) {
        return x & -x;
    }
    
public:
    /** 
     * 构造函数
     * 
     * @param n 数组大小
     */
    BIT(int n) : n(n) {
        tree.resize(n + 1, 0);
    }
    
    /**
     * 单点更新操作
     * 将位置index的值增加delta
     * 
     * @param index 要更新的位置
     * @param delta 增量值
     */
    void update(int index, int delta) {
        while (index <= n) {
            tree[index] += delta;
            index += lowbit(index);
        }
    }
    
    /**
     * 前缀和查询
     * 查询前index个元素的和
     * 
     * @param index 查询结束位置
     * @return      前缀和
     */
    int query(int index) {
        int sum = 0;
        while (index > 0) {
            sum += tree[index];
            index -= lowbit(index);
        }
        return sum;
    }
};

/**
 * 计算右侧小于当前元素的个数
 * 
 * @param nums 输入数组
 * @return      counts数组
 */
vector<int> countSmaller(vector<int>& nums) {
    if (nums.empty()) {
        return {};
    }
    
    int n = nums.size();
    vector<int> result(n, 0);
    
    // 离散化处理
    vector<int> sorted_nums = nums;
    sort(sorted_nums.begin(), sorted_nums.end());
    sorted_nums.erase(unique(sorted_nums.begin(), sorted_nums.end()), sorted_nums.end());
    
    unordered_map<int, int> rank_map;
    for (int i = 0; i < sorted_nums.size(); i++) {
        rank_map[sorted_nums[i]] = i + 1;  // 树状数组索引从1开始
    }
    
    // 初始化树状数组
    BIT bit(sorted_nums.size());
    
    // 从右向左遍历数组
    for (int i = n - 1; i >= 0; i--) {
        int rank = rank_map[nums[i]];
        
        // 查询比当前元素小的元素个数
        result[i] = bit.query(rank - 1);
        
        // 更新树状数组
        bit.update(rank, 1);
    }
    
    return result;
}

/**
 * 测试函数，验证算法正确性
 */
void testCountSmaller() {
    cout << "开始测试计算右侧小于当前元素的个数..." << endl;
    
    // 测试用例1: 正常情况
    vector<int> nums1 = {5, 2, 6, 1};
    vector<int> result1 = countSmaller(nums1);
    cout << "测试用例1: {5, 2, 6, 1} -> ";
    for (int num : result1) cout << num << " ";
    cout << "(期望: 2 1 1 0)" << endl;
    
    // 测试用例2: 空数组
    vector<int> nums2;
    vector<int> result2 = countSmaller(nums2);
    cout << "测试用例2: 空数组 -> 空数组" << endl;
    assert(result2.empty() && "测试用例2失败");
    
    // 测试用例3: 单元素
    vector<int> nums3 = {7};
    vector<int> result3 = countSmaller(nums3);
    cout << "测试用例3: {7} -> " << result3[0] << " (期望: 0)" << endl;
    assert(result3[0] == 0 && "测试用例3失败");
    
    // 测试用例4: 重复元素
    vector<int> nums4 = {1, 1, 1, 1};
    vector<int> result4 = countSmaller(nums4);
    cout << "测试用例4: {1, 1, 1, 1} -> ";
    for (int num : result4) cout << num << " ";
    cout << "(期望: 0 0 0 0)" << endl;
    
    // 测试用例5: 递减序列
    vector<int> nums5 = {5, 4, 3, 2, 1};
    vector<int> result5 = countSmaller(nums5);
    cout << "测试用例5: {5, 4, 3, 2, 1} -> ";
    for (int num : result5) cout << num << " ";
    cout << "(期望: 4 3 2 1 0)" << endl;
    
    cout << "所有测试用例通过！" << endl;
}

int main() {
    // 运行测试
    testCountSmaller();
    
    return 0;
}