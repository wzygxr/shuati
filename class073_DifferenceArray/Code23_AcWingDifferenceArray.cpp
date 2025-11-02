#include <iostream>
#include <vector>

using namespace std;

/**
 * AcWing 797. 差分
 * 
 * 题目描述:
 * 输入一个长度为 n 的整数序列。
 * 接下来输入 m 个操作，每个操作包含三个整数 l, r, c，表示将序列中 [l, r] 之间的每个数加上 c。
 * 请你输出进行完所有操作后的序列。
 * 
 * 示例:
 * 输入:
 * 6 3
 * 1 2 2 1 2 1
 * 1 3 1
 * 3 5 1
 * 1 6 1
 * 
 * 输出:
 * 3 4 5 3 4 2
 * 
 * 题目链接: https://www.acwing.com/problem/content/799/
 * 
 * 解题思路:
 * 使用差分数组技巧来处理区间更新操作。
 * 1. 根据原数组构造差分数组
 * 2. 对于每个操作[l, r, c]，在差分数组中执行b[l] += c和b[r+1] -= c
 * 3. 对差分数组计算前缀和，得到更新后的原数组
 * 
 * 时间复杂度: O(n + m) - 需要遍历所有操作和数组一次
 * 空间复杂度: O(n) - 需要额外的差分数组空间
 * 
 * 这是最优解，因为需要处理所有操作，而且数组大小可能很大。
 */
class Solution {
public:
    vector<int> differenceArray(int n, int m, vector<int>& arr, vector<vector<int>>& operations) {
        // 边界情况处理
        if (n <= 0 || arr.size() != n) {
            return vector<int>();
        }
        
        // 创建差分数组
        vector<int> diff(n + 2, 0); // 多分配空间处理边界
        
        // 构造差分数组: diff[i] = arr[i-1] - arr[i-2]
        diff[1] = arr[0];
        for (int i = 2; i <= n; i++) {
            diff[i] = arr[i - 1] - arr[i - 2];
        }
        
        // 处理每个操作
        for (auto& op : operations) {
            int l = op[0];
            int r = op[1];
            int c = op[2];
            
            // 在差分数组中标记区间更新
            diff[l] += c;
            if (r + 1 <= n) {
                diff[r + 1] -= c;
            }
        }
        
        // 通过计算差分数组的前缀和得到最终数组
        vector<int> result(n);
        result[0] = diff[1];
        for (int i = 1; i < n; i++) {
            result[i] = result[i - 1] + diff[i + 1];
        }
        
        return result;
    }
};

/**
 * 测试用例
 */
int main() {
    Solution solution;
    
    // 测试用例1: 题目示例
    int n1 = 6, m1 = 3;
    vector<int> arr1 = {1, 2, 2, 1, 2, 1};
    vector<vector<int>> operations1 = {
        {1, 3, 1},
        {3, 5, 1},
        {1, 6, 1}
    };
    
    vector<int> result1 = solution.differenceArray(n1, m1, arr1, operations1);
    cout << "测试用例1: ";
    for (int num : result1) {
        cout << num << " ";
    }
    cout << endl; // 预期输出: 3 4 5 3 4 2

    // 测试用例2: 简单情况
    int n2 = 5, m2 = 2;
    vector<int> arr2 = {0, 0, 0, 0, 0};
    vector<vector<int>> operations2 = {
        {1, 3, 5},
        {2, 4, 3}
    };
    
    vector<int> result2 = solution.differenceArray(n2, m2, arr2, operations2);
    cout << "测试用例2: ";
    for (int num : result2) {
        cout << num << " ";
    }
    cout << endl; // 预期输出: 5 8 8 3 0
    
    // 测试用例3: 边界情况
    int n3 = 1, m3 = 1;
    vector<int> arr3 = {10};
    vector<vector<int>> operations3 = {
        {1, 1, 5}
    };
    
    vector<int> result3 = solution.differenceArray(n3, m3, arr3, operations3);
    cout << "测试用例3: ";
    for (int num : result3) {
        cout << num << " ";
    }
    cout << endl; // 预期输出: 15
    
    return 0;
}