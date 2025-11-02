#include <iostream>
#include <vector>
#include <algorithm>
#include <chrono>

using namespace std;

/**
 * LeetCode 370. 区间加法
 * 
 * 问题描述：
 * 假设你有一个长度为 n 的数组，初始情况下所有的数字均为 0，
 * 你将会被给出 k 个更新操作。其中，每个操作会被表示为一个三元组：
 * [startIndex, endIndex, inc]，你需要将子数组 A[startIndex ... endIndex]
 * （包括 startIndex 和 endIndex）增加 inc。
 * 请你返回 k 次操作后的数组。
 * 
 * 算法思路：
 * 使用差分数组来优化区间更新操作。差分数组的核心思想是：
 * 1. 对于区间 [l, r] 增加 val，我们只需要在差分数组的 l 位置加 val，
 *    在 r+1 位置减 val
 * 2. 最后通过计算差分数组的前缀和来得到最终结果
 * 
 * 时间复杂度：
 * - 区间更新：O(1)
 * - 获取结果数组：O(n)
 * - 总时间复杂度：O(k + n)
 * 空间复杂度：O(n)
 * 
 * 应用场景：
 * 1. 数组操作优化：批量更新处理
 * 2. 前缀和计算：快速计算区间和
 * 3. 算法竞赛：区间操作问题的优化
 * 
 * 相关题目：
 * 1. LeetCode 1094. 拼车
 * 2. LeetCode 1109. 航班预订统计
 * 3. LeetCode 1893. 检查是否区域内所有整数都被覆盖
 */

class LeetCode370RangeAddition {
public:
    /**
     * 使用差分数组解决区间加法问题
     * @param length 数组长度
     * @param updates 更新操作数组，每个操作是 [startIndex, endIndex, inc]
     * @return k 次操作后的数组
     */
    vector<int> getModifiedArray(int length, vector<vector<int>>& updates) {
        // 创建差分数组，大小为 length + 1 便于处理边界
        vector<int> diff(length + 1, 0);
        
        // 处理每个更新操作
        for (const auto& update : updates) {
            int start = update[0];
            int end = update[1];
            int inc = update[2];
            
            // 在差分数组中标记区间更新
            diff[start] += inc;
            if (end + 1 < length) {
                diff[end + 1] -= inc;
            }
        }
        
        // 通过计算差分数组的前缀和来得到最终结果
        vector<int> result(length);
        result[0] = diff[0];
        for (int i = 1; i < length; i++) {
            result[i] = result[i - 1] + diff[i];
        }
        
        return result;
    }
    
    /**
     * 暴力解法（用于对比）
     * 时间复杂度：O(k * n)
     * 空间复杂度：O(1)
     */
    vector<int> getModifiedArrayBruteForce(int length, vector<vector<int>>& updates) {
        vector<int> result(length, 0);
        
        // 处理每个更新操作
        for (const auto& update : updates) {
            int start = update[0];
            int end = update[1];
            int inc = update[2];
            
            // 直接更新区间内的每个元素
            for (int i = start; i <= end; i++) {
                result[i] += inc;
            }
        }
        
        return result;
    }
};

/**
 * 测试函数
 */
void testRangeAddition() {
    LeetCode370RangeAddition solution;
    
    cout << "=== 测试 LeetCode 370. 区间加法 ===" << endl;
    
    // 测试用例1
    int length1 = 5;
    vector<vector<int>> updates1 = {{1, 3, 2}, {2, 4, 3}, {0, 2, -2}};
    cout << "测试用例1:" << endl;
    cout << "数组长度: " << length1 << endl;
    cout << "更新操作: ";
    for (const auto& update : updates1) {
        cout << "[" << update[0] << "," << update[1] << "," << update[2] << "] ";
    }
    cout << endl;
    
    auto result1 = solution.getModifiedArray(length1, updates1);
    cout << "差分数组结果: ";
    for (int val : result1) {
        cout << val << " ";
    }
    cout << endl;
    
    auto result1_brute = solution.getModifiedArrayBruteForce(length1, updates1);
    cout << "暴力解法结果: ";
    for (int val : result1_brute) {
        cout << val << " ";
    }
    cout << endl;
    
    // 测试用例2
    int length2 = 10;
    vector<vector<int>> updates2 = {{2, 4, 6}, {0, 6, -2}, {5, 8, 1}};
    cout << "\n测试用例2:" << endl;
    cout << "数组长度: " << length2 << endl;
    cout << "更新操作: ";
    for (const auto& update : updates2) {
        cout << "[" << update[0] << "," << update[1] << "," << update[2] << "] ";
    }
    cout << endl;
    
    auto result2 = solution.getModifiedArray(length2, updates2);
    cout << "差分数组结果: ";
    for (int val : result2) {
        cout << val << " ";
    }
    cout << endl;
    
    auto result2_brute = solution.getModifiedArrayBruteForce(length2, updates2);
    cout << "暴力解法结果: ";
    for (int val : result2_brute) {
        cout << val << " ";
    }
    cout << endl;
    
    // 性能测试
    cout << "\n=== 性能测试 ===" << endl;
    int length3 = 10000;
    int k = 1000;
    vector<vector<int>> updates3(k, vector<int>(3));
    for (int i = 0; i < k; i++) {
        updates3[i][0] = i % length3;
        updates3[i][1] = min(updates3[i][0] + 100, length3 - 1);
        updates3[i][2] = i % 10 - 5;
    }
    
    auto startTime = chrono::high_resolution_clock::now();
    solution.getModifiedArray(length3, updates3);
    auto endTime = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    cout << "差分数组法处理长度为" << length3 << "的数组，" << k << "次更新操作时间: " << duration.count() / 1000.0 << " ms" << endl;
    
    startTime = chrono::high_resolution_clock::now();
    solution.getModifiedArrayBruteForce(length3, updates3);
    endTime = chrono::high_resolution_clock::now();
    duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    cout << "暴力解法处理长度为" << length3 << "的数组，" << k << "次更新操作时间: " << duration.count() / 1000.0 << " ms" << endl;
}

int main() {
    testRangeAddition();
    return 0;
}