#include <iostream>
#include <vector>
#include <climits>

using namespace std;

/**
 * 牛客网 - 数组操作问题
 * 
 * 题目描述:
 * 给定一个长度为 n 的数组，初始值都为 0。
 * 有 m 次操作，每次操作给出三个数 l, r, k，表示将数组下标从 l 到 r 的所有元素都加上 k。
 * 求执行完所有操作后数组中的最大值。
 * 
 * 示例:
 * 输入: n = 5, operations = [[1,2,100],[2,5,100],[3,4,100]]
 * 输出: 200
 * 
 * 解题思路:
 * 使用差分数组技巧来处理区间更新操作。
 * 1. 创建一个差分数组diff，大小为n+1
 * 2. 对于每个操作[l, r, k]，执行diff[l-1] += k和diff[r] -= k
 * 3. 对差分数组计算前缀和，得到最终数组
 * 4. 在计算前缀和的过程中记录最大值
 * 
 * 时间复杂度: O(n + m) - 需要遍历所有操作和数组一次
 * 空间复杂度: O(n) - 需要额外的差分数组空间
 * 
 * 这是最优解，因为需要处理所有操作，而且数组大小可能很大。
 */
class Solution {
public:
    long long maxValueAfterOperations(int n, vector<vector<int>>& operations) {
        // 边界情况处理
        if (n <= 0 || operations.empty()) {
            return 0;
        }
        
        // 创建差分数组，大小为n+1以便处理边界情况
        vector<long long> diff(n + 1, 0);
        
        // 处理每个操作
        for (auto& op : operations) {
            int l = op[0];      // 起始索引（1-based）
            int r = op[1];      // 结束索引（1-based）
            int k = op[2];      // 增加值
            
            // 在差分数组中标记区间更新
            diff[l - 1] += k;      // 在起始位置增加k
            if (r < n) {
                diff[r] -= k;      // 在结束位置之后减少k
            }
        }
        
        // 通过计算差分数组的前缀和得到最终数组，并记录最大值
        long long maxVal = LLONG_MIN;
        long long currentSum = 0;
        
        for (int i = 0; i < n; i++) {
            currentSum += diff[i];
            if (currentSum > maxVal) {
                maxVal = currentSum;
            }
        }
        
        return maxVal;
    }
};

/**
 * 测试用例
 */
int main() {
    Solution solution;
    
    // 测试用例1
    int n1 = 5;
    vector<vector<int>> operations1 = {{1, 2, 100}, {2, 5, 100}, {3, 4, 100}};
    long long result1 = solution.maxValueAfterOperations(n1, operations1);
    // 预期输出: 200
    cout << "测试用例1: " << result1 << endl;

    // 测试用例2
    int n2 = 10;
    vector<vector<int>> operations2 = {{2, 6, 8}, {3, 5, 7}, {1, 8, 1}, {5, 9, 15}};
    long long result2 = solution.maxValueAfterOperations(n2, operations2);
    // 预期输出: 31
    cout << "测试用例2: " << result2 << endl;
    
    // 测试用例3
    int n3 = 4;
    vector<vector<int>> operations3 = {{1, 2, 5}, {2, 4, 10}, {1, 3, 3}};
    long long result3 = solution.maxValueAfterOperations(n3, operations3);
    // 预期输出: 18
    cout << "测试用例3: " << result3 << endl;
    
    // 测试用例4 - 边界情况
    int n4 = 1;
    vector<vector<int>> operations4 = {{1, 1, 100}};
    long long result4 = solution.maxValueAfterOperations(n4, operations4);
    // 预期输出: 100
    cout << "测试用例4: " << result4 << endl;
    
    return 0;
}