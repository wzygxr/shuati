#include <iostream>
#include <vector>
#include <string>

using namespace std;

/**
 * LeetCode 77. 组合
 * 
 * 题目描述：
 * 给定两个整数 n 和 k，返回范围 [1, n] 中所有可能的 k 个数的组合。
 * 你可以按任何顺序返回答案。
 * 
 * 示例：
 * 输入：n = 4, k = 2
 * 输出：[[1,2],[1,3],[1,4],[2,3],[2,4],[3,4]]
 * 
 * 输入：n = 1, k = 1
 * 输出：[[1]]
 * 
 * 提示：
 * 1 <= n <= 20
 * 1 <= k <= n
 * 
 * 链接：https://leetcode.cn/problems/combinations/
 * 
 * 算法思路：
 * 1. 使用回溯算法生成所有可能的组合
 * 2. 从1开始，每次选择一个数字，然后递归选择下一个数字
 * 3. 当组合长度达到k时，将其加入结果集
 * 4. 通过控制起始位置避免重复组合
 * 
 * 时间复杂度：O(C(n, k) * k)，其中C(n, k)是组合数，每个组合需要O(k)时间复制
 * 空间复杂度：O(k)，递归栈深度和存储路径的空间
 */
class Solution {
public:
    vector<vector<int>> combine(int n, int k) {
        vector<vector<int>> result;
        vector<int> path;
        backtrack(n, k, 1, path, result);
        return result;
    }

private:
    void backtrack(int n, int k, int start, vector<int>& path, vector<vector<int>>& result) {
        // 终止条件：组合长度达到k
        if (path.size() == k) {
            result.push_back(path);
            return;
        }
        
        // 剪枝优化：如果剩余的数字不足以填满组合，提前终止
        // 还需要选择的数字个数：k - path.size()
        // 从start到n至少要有这么多个数字：n - start + 1 >= k - path.size()
        // 所以 start <= n - (k - path.size()) + 1
        for (int i = start; i <= n - (k - path.size()) + 1; i++) {
            path.push_back(i);  // 选择当前数字
            backtrack(n, k, i + 1, path, result);  // 递归选择下一个数字
            path.pop_back();  // 撤销选择
        }
    }
};

// 测试函数
void testCombinations() {
    Solution solution;
    
    // 测试用例1
    int n1 = 4, k1 = 2;
    vector<vector<int>> result1 = solution.combine(n1, k1);
    cout << "输入: n = " << n1 << ", k = " << k1 << endl;
    cout << "输出: [";
    for (int i = 0; i < result1.size(); i++) {
        cout << "[";
        for (int j = 0; j < result1[i].size(); j++) {
            cout << result1[i][j];
            if (j < result1[i].size() - 1) cout << ",";
        }
        cout << "]";
        if (i < result1.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    
    // 测试用例2
    int n2 = 1, k2 = 1;
    vector<vector<int>> result2 = solution.combine(n2, k2);
    cout << "\n输入: n = " << n2 << ", k = " << k2 << endl;
    cout << "输出: [";
    for (int i = 0; i < result2.size(); i++) {
        cout << "[";
        for (int j = 0; j < result2[i].size(); j++) {
            cout << result2[i][j];
            if (j < result2[i].size() - 1) cout << ",";
        }
        cout << "]";
        if (i < result2.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
}

int main() {
    testCombinations();
    return 0;
}