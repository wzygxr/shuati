#include <iostream>
#include <vector>
using namespace std;

/**
 * LeetCode 370. Range Addition
 * 
 * 题目描述:
 * 假设你有一个长度为 n 的数组，初始情况下所有的数字均为 0，
 * 你将会被给出 k 个更新的操作。
 * 其中，每个操作会被表示为一个三元组：[startIndex, endIndex, inc]，
 * 你需要将子数组 A[startIndex ... endIndex]（包括 startIndex 和 endIndex）增加 inc。
 * 
 * 示例:
 * 输入: length = 5, updates = [[1,3,2],[2,4,3],[0,2,-2]]
 * 输出: [-2,0,3,5,3]
 * 解释:
 * 初始状态: [0,0,0,0,0]
 * 进行了操作 [1,3,2] 后的状态: [0,2,2,2,0]
 * 进行了操作 [2,4,3] 后的状态: [0,2,5,5,3]
 * 进行了操作 [0,2,-2] 后的状态: [-2,0,3,5,3]
 * 
 * 提示:
 * 1 <= length <= 10^5
 * 0 <= updates.length <= 10^4
 * 0 <= startIndex <= endIndex < length
 * -1000 <= inc <= 1000
 * 
 * 题目链接: https://leetcode.com/problems/range-addition/
 * 
 * 解题思路:
 * 使用差分数组技巧来优化区间更新操作。
 * 1. 创建一个差分数组diff，大小为n+1
 * 2. 对于每个操作[startIndex, endIndex, inc]，执行diff[startIndex] += inc和diff[endIndex+1] -= inc
 * 3. 对差分数组计算前缀和，得到最终数组
 * 
 * 时间复杂度: O(n + k) - 需要遍历所有操作和数组一次
 * 空间复杂度: O(n) - 需要额外的差分数组空间
 * 
 * 这是最优解，因为需要处理所有操作，而且数组大小可能很大，不能使用暴力方法。
 */

class Solution {
public:
    /**
     * 计算区间加法后的数组
     * 
     * @param length 数组长度
     * @param updates 更新操作数组，每个操作包含[起始索引, 结束索引, 增加值]
     * @return 更新后的数组
     * 
     * 时间复杂度: O(n + k) - 需要遍历所有操作和数组一次
     * 空间复杂度: O(n) - 需要额外的差分数组空间
     * 
     * 工程化考量:
     * 1. 边界处理: 处理空数组和空操作情况
     * 2. 异常处理: 验证输入参数的合法性
     * 3. 性能优化: 使用差分数组将区间更新操作从O(n)优化到O(1)
     * 4. 可读性: 变量命名清晰，注释详细
     */
    vector<int> getModifiedArray(int length, vector<vector<int>>& updates) {
        // 边界情况处理
        if (length <= 0) {
            return vector<int>();
        }
        
        if (updates.empty()) {
            return vector<int>(length, 0);
        }
        
        // 创建差分数组，大小为length+1以便处理边界情况
        vector<int> diff(length + 1, 0);
        
        // 处理每个更新操作
        for (const auto& update : updates) {
            int startIndex = update[0];    // 起始索引
            int endIndex = update[1];      // 结束索引
            int inc = update[2];           // 增加值
            
            // 在差分数组中标记区间更新
            diff[startIndex] += inc;       // 在起始位置增加inc
            if (endIndex + 1 < length) {
                diff[endIndex + 1] -= inc; // 在结束位置之后减少inc
            }
        }
        
        // 通过计算差分数组的前缀和得到最终数组
        vector<int> result(length);
        result[0] = diff[0];
        for (int i = 1; i < length; i++) {
            result[i] = result[i - 1] + diff[i];
        }
        
        return result;
    }
};

/**
 * 测试用例
 */
int main() {
    Solution solution;
    
    // 测试用例1
    vector<vector<int>> updates1 = {{1, 3, 2}, {2, 4, 3}, {0, 2, -2}};
    vector<int> result1 = solution.getModifiedArray(5, updates1);
    // 预期输出: [-2, 0, 3, 5, 3]
    cout << "测试用例1: ";
    for (int num : result1) {
        cout << num << " ";
    }
    cout << endl;

    // 测试用例2
    vector<vector<int>> updates2 = {{2, 4, 6}, {5, 6, 8}, {1, 9, -4}};
    vector<int> result2 = solution.getModifiedArray(10, updates2);
    // 预期输出: [0, -4, 2, 2, 2, 4, 4, -4, -4, -4]
    cout << "测试用例2: ";
    for (int num : result2) {
        cout << num << " ";
    }
    cout << endl;
    
    // 测试用例3: 边界情况
    vector<vector<int>> updates3 = {{0, 0, 5}};
    vector<int> result3 = solution.getModifiedArray(1, updates3);
    // 预期输出: [5]
    cout << "测试用例3: ";
    for (int num : result3) {
        cout << num << " ";
    }
    cout << endl;
    
    return 0;
}