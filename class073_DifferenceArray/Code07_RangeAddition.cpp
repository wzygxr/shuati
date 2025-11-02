#include <iostream>
#include <vector>
using namespace std;

/**
 * LeetCode 370. 区间加法 (Range Addition)
 * 
 * 题目描述:
 * 假设你有一个长度为 n 的数组，初始情况下所有的数字均为 0，
 * 你将会被给出 k 个更新的操作。其中，每个操作会被表示为一个三元组：
 * [startIndex, endIndex, inc]，你需要将子数组 A[startIndex ... endIndex]
 * （包括 startIndex 和 endIndex）增加 inc。
 * 请你返回 k 次操作后的数组。
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
 * 使用差分数组技巧来处理区间更新操作。
 * 1. 创建一个差分数组diff，大小为length
 * 2. 对于每个操作[start, end, inc]，在差分数组中执行diff[start] += inc和diff[end+1] -= inc
 * 3. 对差分数组计算前缀和，得到最终结果数组
 * 
 * 时间复杂度: O(n + k) - n是数组长度，k是操作次数
 * 空间复杂度: O(n) - 需要额外的差分数组空间
 * 
 * 这是最优解，因为需要处理所有操作，而且数组长度可能很大。
 */

class RangeAddition {
public:
    /**
     * 执行区间加法操作并返回结果数组
     * 
     * @param length 数组长度
     * @param updates 操作数组，每个操作包含[起始索引, 结束索引, 增加值]
     * @return 操作后的数组
     */
    static vector<int> getModifiedArray(int length, vector<vector<int>>& updates) {
        // 边界情况处理
        if (length <= 0) {
            return {};
        }
        
        if (updates.empty()) {
            return vector<int>(length, 0);
        }
        
        // 创建差分数组
        vector<int> diff(length, 0);
        
        // 处理每个操作
        for (const auto& update : updates) {
            int start = update[0];   // 起始索引
            int end = update[1];     // 结束索引
            int inc = update[2];     // 增加值
            
            // 在差分数组中标记区间更新
            diff[start] += inc;      // 在起始位置增加inc
            
            // 在结束位置之后减少inc（如果end+1在数组范围内）
            if (end + 1 < length) {
                diff[end + 1] -= inc;
            }
        }
        
        // 通过计算差分数组的前缀和得到最终数组
        for (int i = 1; i < length; i++) {
            diff[i] += diff[i - 1];
        }
        
        return diff;
    }
};

/**
 * 测试用例
 */
int main() {
    // 测试用例1
    vector<vector<int>> updates1 = {{1, 3, 2}, {2, 4, 3}, {0, 2, -2}};
    vector<int> result1 = RangeAddition::getModifiedArray(5, updates1);
    // 预期输出: [-2, 0, 3, 5, 3]
    cout << "测试用例1: ";
    for (int i = 0; i < result1.size(); i++) {
        cout << result1[i] << (i == result1.size() - 1 ? "\n" : ", ");
    }

    // 测试用例2
    vector<vector<int>> updates2 = {{2, 4, 6}, {0, 6, -4}, {5, 7, 3}};
    vector<int> result2 = RangeAddition::getModifiedArray(10, updates2);
    // 预期输出: [-4, -4, 2, 2, 2, -1, -1, -1, 0, 0]
    cout << "测试用例2: ";
    for (int i = 0; i < result2.size(); i++) {
        cout << result2[i] << (i == result2.size() - 1 ? "\n" : ", ");
    }
    
    // 测试用例3
    vector<vector<int>> updates3 = {{0, 2, 1}};
    vector<int> result3 = RangeAddition::getModifiedArray(3, updates3);
    // 预期输出: [1, 1, 1]
    cout << "测试用例3: ";
    for (int i = 0; i < result3.size(); i++) {
        cout << result3[i] << (i == result3.size() - 1 ? "\n" : ", ");
    }
    
    return 0;
}