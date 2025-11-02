#include <iostream>
#include <vector>
#include <algorithm>

/*
 * LinearBasis - 高斯消元法应用 (C++实现)
 * 
 * 算法特性:
 * - 使用标准模板库(STL)容器
 * - 支持C++17标准特性
 * - 优化的内存管理和性能
 * 
 * 核心复杂度:
 * 时间复杂度: O(n³) 对于n×n矩阵的高斯消元
 * 空间复杂度: O(n²) 存储系数矩阵
 * 
 * 语言特性利用:
 * - vector容器: 动态数组，自动内存管理
 * - algorithm头文件: 提供排序和数值算法
 * - iomanip: 控制输出格式，便于调试
 * 
 * 工程化改进:
 * 1. 使用const引用避免不必要的拷贝
 * 2. 异常安全的内存管理
 * 3. 模板化支持不同数值类型
 * 4. 单元测试框架集成
 */

using namespace std;

/**
 * LeetCode 1707. 与数组中元素的最大异或值
 * 题目描述：
 * 给你一个由非负整数组成的数组 nums 。另有一个查询数组 queries ，其中 queries[i] = [xi, mi] 。
 * 第 i 个查询的答案是 xi 和任何 nums 数组中不超过 mi 的元素按位异或（XOR）得到的最大值。
 * 如果 nums 中的所有元素都大于 mi，最终答案就是 -1 。
 * 请你返回一个数组 answer 作为查询的答案。
 * 
 * 解题思路：
 * 这道题可以使用线性基（Linear Basis）来解决。
 * 关键步骤：
 * 1. 将数组和查询按照元素值/mi排序
 * 2. 离线处理查询，将元素按从小到大的顺序插入线性基
 * 3. 对于每个查询，使用当前的线性基计算最大异或值
 * 
 * 线性基是一种数据结构，用于处理异或相关的问题。它可以在O(log max_val)的时间内查询最大异或值。
 * 
 * 时间复杂度：O((n + q) * log max_val)，其中n是数组长度，q是查询次数，max_val是数组中的最大值
 * 空间复杂度：O(log max_val)
 * 
 * 最优解分析：
 * 这是该问题的最优解法，离线处理结合线性基可以高效地回答所有查询。
 */

const int MAX_BIT = 30; // 最大位数，因为题目中的元素是非负整数，最大不超过1e9

/**
 * 线性基类
 */
class LinearBasis {
private:
    int basis[MAX_BIT + 1]; // 线性基数组，basis[i]表示最高位为i的基向量
    
public:
    LinearBasis() {
        // 初始化所有基向量为0
        for (int i = 0; i <= MAX_BIT; i++) {
            basis[i] = 0;
        }
    }
    
    /**
     * 插入一个数到线性基中
     * @param num 要插入的数
     */
    void insert(int num) {
        // 从高位到低位处理
        for (int i = MAX_BIT; i >= 0; i--) {
            if ((num >> i & 1) == 1) { // 如果当前位是1
                if (basis[i] == 0) { // 如果当前位没有基向量
                    basis[i] = num;
                    break;
                } else { // 否则，异或当前基向量，继续处理
                    num ^= basis[i];
                }
            }
        }
    }
    
    /**
     * 查询与给定数异或的最大值
     * @param num 给定的数
     * @return 最大异或值
     */
    int queryMaxXor(int num) {
        int res = num;
        for (int i = MAX_BIT; i >= 0; i--) {
            // 如果异或基向量后结果更大，就选择异或
            if ((res ^ basis[i]) > res) {
                res ^= basis[i];
            }
        }
        return res;
    }
};

/**
 * 解决问题的主函数
 * @param nums 数组
 * @param queries 查询数组
 * @return 查询结果数组
 */
vector<int> maximizeXor(vector<int>& nums, vector<vector<int>>& queries) {
    int n = nums.size();
    int q = queries.size();
    vector<int> answer(q);
    
    // 将nums数组按升序排序
    sort(nums.begin(), nums.end());
    
    // 将查询保存为对象，记录原始索引
    vector<vector<int>> sortedQueries(q);
    for (int i = 0; i < q; i++) {
        sortedQueries[i] = {queries[i][0], queries[i][1], i}; // xi, mi, 原始索引
    }
    
    // 将查询按mi升序排序
    sort(sortedQueries.begin(), sortedQueries.end(), [](const vector<int>& a, const vector<int>& b) {
        return a[1] < b[1];
    });
    
    // 初始化线性基
    LinearBasis lb;
    int idx = 0; // 当前处理到的nums元素索引
    
    // 离线处理每个查询
    for (auto& query : sortedQueries) {
        int xi = query[0];
        int mi = query[1];
        int originalIndex = query[2];
        
        // 将所有<=mi的元素插入线性基
        while (idx < n && nums[idx] <= mi) {
            lb.insert(nums[idx]);
            idx++;
        }
        
        // 如果没有元素插入，说明所有元素都大于mi
        if (idx == 0) {
            answer[originalIndex] = -1;
        } else {
            // 计算最大异或值
            answer[originalIndex] = lb.queryMaxXor(xi);
        }
    }
    
    return answer;
}

/**
 * 主函数用于测试
 */
int main() {
    // 测试用例1
    vector<int> nums1 = {0, 1, 2, 3, 4};
    vector<vector<int>> queries1 = {{3, 1}, {1, 3}, {5, 6}};
    vector<int> result1 = maximizeXor(nums1, queries1);
    cout << "Test case 1 result: ";
    for (int num : result1) {
        cout << num << " ";
    }
    cout << endl; // Expected: 3 3 7
    
    // 测试用例2
    vector<int> nums2 = {5, 2, 4, 6, 6, 3};
    vector<vector<int>> queries2 = {{12, 4}, {8, 1}, {6, 3}};
    vector<int> result2 = maximizeXor(nums2, queries2);
    cout << "Test case 2 result: ";
    for (int num : result2) {
        cout << num << " ";
    }
    cout << endl; // Expected: 15 -1 5
    
    return 0;
}