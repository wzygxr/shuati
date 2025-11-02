/*
POJ 1011 Sticks

给定n根火柴棍，每根火柴棍都有一定的长度。要求将这些火柴棍拼成若干根长度相等的火柴棍，
且每根新火柴棍的长度要尽可能大。求这个最大长度。

算法思路：
使用回溯算法，从最大可能的长度开始尝试，逐步减小，直到找到一个可行解。
对于每个尝试的长度，使用回溯算法检查是否能将所有火柴棍拼成该长度的若干根新火柴棍。

时间复杂度：O(2^n * n)
空间复杂度：O(n)
*/

#include <vector>
#include <algorithm>
#include <numeric>
#include <iostream>
using namespace std;

class Solution {
public:
    /**
     * 求最大可能的火柴棍长度
     * @param sticks 火柴棍长度数组
     * @return 最大可能的火柴棍长度
     */
    int maxLenOfSticks(vector<int>& sticks) {
        // 从大到小排序，便于剪枝
        sort(sticks.begin(), sticks.end(), greater<int>());
        
        int sum = accumulate(sticks.begin(), sticks.end(), 0);
        
        // 从最大可能长度开始尝试
        for (int len = sum / sticks.size(); len >= 1; len--) {
            if (sum % len == 0) {  // 只有当总长度能被len整除时才可能
                vector<int> buckets(sum / len, 0);
                if (backtrack(sticks, 0, buckets, len)) {
                    return len;
                }
            }
        }
        
        return 1;  // 最坏情况，每根火柴棍单独作为一根
    }
    
private:
    /**
     * 回溯函数，尝试将火柴棍分配到各个桶中
     * @param sticks 火柴棍长度数组
     * @param index 当前处理的火柴棍索引
     * @param buckets 桶数组，记录每个桶当前的长度
     * @param target 目标长度
     * @return 是否能成功分配
     */
    bool backtrack(vector<int>& sticks, int index, vector<int>& buckets, int target) {
        // 终止条件：所有火柴棍都已处理完
        if (index == sticks.size()) {
            return true;
        }
        
        int stick = sticks[index];
        
        // 尝试将当前火柴棍放入每个桶中
        for (int i = 0; i < buckets.size(); i++) {
            // 剪枝：如果放入当前桶后超过目标长度，则跳过
            if (buckets[i] + stick > target) {
                continue;
            }
            
            buckets[i] += stick;
            if (backtrack(sticks, index + 1, buckets, target)) {
                return true;
            }
            buckets[i] -= stick;
            
            // 剪枝：如果当前桶为空，说明当前火柴棍无法放入任何桶中
            if (buckets[i] == 0) {
                break;
            }
        }
        
        return false;
    }
};

// 测试代码
int main() {
    Solution solution;
    
    // 测试用例1
    vector<int> sticks1 = {5, 2, 1, 5, 2, 1, 5, 2, 1};
    cout << "Input: [5, 2, 1, 5, 2, 1, 5, 2, 1]" << endl;
    cout << "Output: " << solution.maxLenOfSticks(sticks1) << endl;
    
    // 测试用例2
    vector<int> sticks2 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    cout << "\nInput: [1, 2, 3, 4, 5, 6, 7, 8, 9]" << endl;
    cout << "Output: " << solution.maxLenOfSticks(sticks2) << endl;
    
    return 0;
}