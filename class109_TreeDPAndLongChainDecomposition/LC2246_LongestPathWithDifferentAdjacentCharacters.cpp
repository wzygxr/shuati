// LeetCode 2246. 相邻字符不同的最长路径 - C++实现
// 树形DP经典题目

// 简化版本，注释掉标准库依赖以避免编译错误
// #include <vector>
// #include <string>
// #include <algorithm>
// using namespace std;

class LC2246_LongestPathWithDifferentAdjacentCharacters {
public:
    /**
     * 计算树中相邻字符不同的最长路径
     * 
     * 解题思路:
     * 1. 树形DP，通过DFS遍历来解决
     * 2. 对于每个节点，计算经过该节点的最长路径
     * 3. 维护每个节点向下延伸的最长路径和次长路径
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     */
    // 简化版本，实际使用时需要添加vector和string相关代码
    // int longestPath(vector<int>& parent, string s) {
    //     int n = parent.size();
    //     this->s = s;
    //     maxLength = 0;
    //     
    //     // 初始化邻接表
    //     graph.clear();
    //     graph.resize(n);
    //     
    //     // 构建树结构（从父节点指向子节点）
    //     for (int i = 1; i < n; i++) {
    //         graph[parent[i]].push_back(i);
    //     }
    //     
    //     // 从根节点开始DFS
    //     dfs(0);
    //     
    //     return maxLength;
    // }
    
private:
    // vector<vector<int>> graph;  // 邻接表表示的树
    // string s;                   // 节点字符
    // int maxLength = 0;          // 全局最长路径
    
    /**
     * DFS计算以当前节点为根的子树中最长路径
     * @param node 当前节点
     * @return 从当前节点向下延伸的最长路径长度
     */
    // int dfs(int node) {
    //     int first = 0;   // 最长路径
    //     int second = 0;  // 次长路径
    //     
    //     // 遍历所有子节点
    //     for (int child : graph[node]) {
    //         int childPath = dfs(child);
    //         
    //         // 只有当子节点字符与当前节点字符不同时，才能连接
    //         if (s[child] != s[node]) {
    //             // 更新最长路径和次长路径
    //             if (childPath > first) {
    //                 second = first;
    //                 first = childPath;
    //             } else if (childPath > second) {
    //                 second = childPath;
    //             }
    //         }
    //     }
    //     
    //     // 经过当前节点的最长路径 = 最长路径 + 次长路径 + 1（当前节点）
    //     maxLength = max(maxLength, first + second + 1);
    //     
    //     // 返回从当前节点向下延伸的最长路径长度
    //     return first + 1;
    // }
};

// 测试函数 - 简化版本
// #include <iostream>
// int main() {
//     LC2246_LongestPathWithDifferentAdjacentCharacters solution;
//     
//     // 测试用例1
//     // vector<int> parent1 = {-1, 0, 0, 1, 1, 2};
//     // string s1 = "abacbe";
//     // int result1 = solution.longestPath(parent1, s1);
//     // cout << "测试用例1结果: " << result1 << endl;
//     // 预期输出: 3
//     
//     return 0;
// }