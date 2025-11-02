#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

/**
 * HackerRank Array Manipulation
 * 
 * 题目描述:
 * 给定一个大小为 n 的数组，初始值都为 0。有 m 次操作，
 * 每次操作给出三个数 a, b, k，表示将数组下标从 a 到 b 的所有元素都加上 k。
 * 求执行完所有操作后数组中的最大值。
 * 
 * 示例:
 * 输入: n = 5, queries = [[1,2,100],[2,5,100],[3,4,100]]
 * 输出: 200
 * 解释: 
 * 初始数组: [0, 0, 0, 0, 0]
 * 操作1: [100, 100, 0, 0, 0]
 * 操作2: [100, 200, 100, 100, 100]
 * 操作3: [100, 200, 200, 200, 100]
 * 最大值: 200
 * 
 * 提示:
 * 3 <= n <= 10^7
 * 1 <= m <= 2*10^5
 * 1 <= a <= b <= n
 * 0 <= k <= 10^9
 * 
 * 题目链接: https://www.hackerrank.com/challenges/crush/problem
 * 
 * 解题思路:
 * 使用差分数组技巧结合前缀和来优化区间更新操作。
 * 1. 创建一个差分数组diff，大小为n+1
 * 2. 对于每个操作[a,b,k]，执行diff[a-1] += k和diff[b] -= k
 * 3. 对差分数组计算前缀和，得到最终数组
 * 4. 在计算前缀和的过程中记录最大值
 * 
 * 时间复杂度: O(n + m) - 需要遍历所有操作和数组一次
 * 空间复杂度: O(n) - 需要额外的差分数组空间
 * 
 * 这是最优解，因为需要处理所有操作，而且数组大小可能很大，不能使用暴力方法。
 */

class HackerRankArrayManipulation {
public:
    /**
     * 计算数组操作后的最大值
     * 
     * @param n 数组长度
     * @param queries 操作数组，每个操作包含[起始索引, 结束索引, 增加值]
     * @return 操作后数组的最大值
     */
    static long long arrayManipulation(int n, vector<vector<int>>& queries) {
        // 边界情况处理
        if (n <= 0 || queries.empty()) {
            return 0;
        }
        
        // 创建差分数组，大小为n+1以便处理边界情况
        vector<long long> diff(n + 1, 0);
        
        // 处理每个操作
        for (const auto& query : queries) {
            int a = query[0];      // 起始索引（1-based）
            int b = query[1];      // 结束索引（1-based）
            int k = query[2];      // 增加值
            
            // 在差分数组中标记区间更新
            diff[a - 1] += k;      // 在起始位置增加k
            if (b < n) {
                diff[b] -= k;      // 在结束位置之后减少k
            }
        }
        
        // 通过计算差分数组的前缀和得到最终数组，并记录最大值
        long long maxVal = LLONG_MIN;
        long long currentSum = 0;
        
        for (int i = 0; i < n; i++) {
            currentSum += diff[i];
            maxVal = max(maxVal, currentSum);
        }
        
        return maxVal;
    }
};

/**
 * 测试用例
 */
int main() {
    // 测试用例1
    vector<vector<int>> queries1 = {{1, 2, 100}, {2, 5, 100}, {3, 4, 100}};
    long long result1 = HackerRankArrayManipulation::arrayManipulation(5, queries1);
    // 预期输出: 200
    cout << "测试用例1: " << result1 << endl;

    // 测试用例2
    vector<vector<int>> queries2 = {{2, 6, 8}, {3, 5, 7}, {1, 8, 1}, {5, 9, 15}};
    long long result2 = HackerRankArrayManipulation::arrayManipulation(10, queries2);
    // 预期输出: 31
    cout << "测试用例2: " << result2 << endl;
    
    // 测试用例3
    vector<vector<int>> queries3 = {{1, 2, 5}, {2, 4, 10}, {1, 3, 3}};
    long long result3 = HackerRankArrayManipulation::arrayManipulation(4, queries3);
    // 预期输出: 18
    cout << "测试用例3: " << result3 << endl;
    
    return 0;
}