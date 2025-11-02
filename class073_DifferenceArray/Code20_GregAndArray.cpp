#include <vector>
#include <iostream>

using namespace std;

/**
 * Codeforces 296C. Greg and Array
 * 
 * 题目描述:
 * Greg 有一个长度为 n 的数组 a，初始值都为 0。
 * 他还有 m 个操作，每个操作是一个三元组 (l, r, d)，表示将区间 [l, r] 中的每个元素加上 d。
 * 然后他有 k 个指令，每个指令是一个二元组 (x, y)，表示执行操作 x 到操作 y 各一次。
 * 请输出执行完所有指令后的数组。
 * 
 * 示例:
 * 输入: n = 3, m = 3, k = 3
 * 操作: 
 * 操作1: (1, 2, 1)
 * 操作2: (1, 3, 2)
 * 操作3: (2, 3, 4)
 * 指令:
 * 指令1: (1, 2)
 * 指令2: (1, 3)
 * 指令3: (2, 3)
 * 输出: [9, 18, 17]
 * 
 * 题目链接: https://codeforces.com/contest/296/problem/C
 * 
 * 解题思路:
 * 使用两层差分数组技巧来处理多层区间更新操作。
 * 1. 第一层差分: 统计每个操作被执行多少次
 * 2. 第二层差分: 根据操作执行次数，计算对原数组的影响
 * 
 * 时间复杂度: O(n + m + k) - 需要处理所有操作和指令
 * 空间复杂度: O(n + m) - 需要存储操作信息和差分数组
 * 
 * 这是最优解，因为需要处理多层区间更新。
 */
class Solution {
public:
    vector<long long> gregAndArray(int n, int m, int k, vector<vector<int>>& operations, vector<vector<int>>& instructions) {
        // 边界情况处理
        if (n <= 0 || m <= 0 || k <= 0) {
            return vector<long long>(n, 0);
        }
        
        // 第一层差分: 统计每个操作被执行多少次
        vector<long long> opCount(m + 2, 0); // 操作索引从1开始
        
        // 处理指令，统计每个操作被执行次数
        for (auto& instruction : instructions) {
            int x = instruction[0]; // 起始操作索引
            int y = instruction[1]; // 结束操作索引
            
            // 使用差分标记指令区间
            opCount[x] += 1;
            if (y + 1 <= m) {
                opCount[y + 1] -= 1;
            }
        }
        
        // 计算每个操作的实际执行次数
        for (int i = 1; i <= m; i++) {
            opCount[i] += opCount[i - 1];
        }
        
        // 第二层差分: 计算对原数组的影响
        vector<long long> diff(n + 2, 0); // 数组索引从1开始
        
        // 根据操作执行次数，计算对原数组的影响
        for (int i = 1; i <= m; i++) {
            vector<int> op = operations[i - 1]; // 操作索引从0开始
            int l = op[0];
            int r = op[1];
            long long d = op[2];
            long long count = opCount[i]; // 该操作执行次数
            
            // 应用操作到差分数组
            diff[l] += d * count;
            if (r + 1 <= n) {
                diff[r + 1] -= d * count;
            }
        }
        
        // 计算最终结果数组
        vector<long long> result(n, 0);
        long long current = 0;
        for (int i = 1; i <= n; i++) {
            current += diff[i];
            result[i - 1] = current;
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
    int n1 = 3, m1 = 3, k1 = 3;
    vector<vector<int>> operations1 = {
        {1, 2, 1},  // 操作1
        {1, 3, 2},  // 操作2
        {2, 3, 4}   // 操作3
    };
    vector<vector<int>> instructions1 = {
        {1, 2},     // 指令1
        {1, 3},     // 指令2
        {2, 3}      // 指令3
    };
    
    vector<long long> result1 = solution.gregAndArray(n1, m1, k1, operations1, instructions1);
    cout << "测试用例1: ";
    for (long long num : result1) {
        cout << num << " ";
    }
    cout << endl; // 预期输出: 9 18 17
    
    // 测试用例2: 简单情况
    int n2 = 5, m2 = 2, k2 = 1;
    vector<vector<int>> operations2 = {
        {1, 3, 2},  // 操作1
        {2, 4, 3}   // 操作2
    };
    vector<vector<int>> instructions2 = {
        {1, 2}      // 指令1
    };
    
    vector<long long> result2 = solution.gregAndArray(n2, m2, k2, operations2, instructions2);
    cout << "测试用例2: ";
    for (long long num : result2) {
        cout << num << " ";
    }
    cout << endl; // 预期输出: 2 5 5 3 0
    
    // 测试用例3: 边界情况
    int n3 = 1, m3 = 1, k3 = 1;
    vector<vector<int>> operations3 = {
        {1, 1, 5}   // 操作1
    };
    vector<vector<int>> instructions3 = {
        {1, 1}      // 指令1
    };
    
    vector<long long> result3 = solution.gregAndArray(n3, m3, k3, operations3, instructions3);
    cout << "测试用例3: ";
    for (long long num : result3) {
        cout << num << " ";
    }
    cout << endl; // 预期输出: 5
    
    return 0;
}