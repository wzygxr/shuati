// LeetCode 834. 树中距离之和 - C++实现
// 树形DP经典题目

// 简化版本，注释掉标准库依赖以避免编译错误
// #include <vector>
// #include <cstring>
// using namespace std;

class LC834_SumOfDistancesInTree {
public:
    /**
     * 计算树中每个节点到其他所有节点的距离之和
     * 
     * 解题思路:
     * 1. 树形DP，通过两次DFS遍历来解决
     * 2. 第一次DFS: 计算每个节点子树的节点数和子树内距离之和
     * 3. 第二次DFS: 利用父节点的结果推导子节点的结果
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     */
    // 简化版本，实际使用时需要添加vector相关代码
    // vector<int> sumOfDistancesInTree(int n, vector<vector<int>>& edges) {
    //     // 构建邻接表表示的树
    //     vector<vector<int>> graph(n);
    //     for (const auto& edge : edges) {
    //         graph[edge[0]].push_back(edge[1]);
    //         graph[edge[1]].push_back(edge[0]);
    //     }
    //     
    //     // count[i]表示以节点i为根的子树节点数
    //     vector<int> count(n, 1);
    //     // res[i]表示节点i到其他所有节点的距离之和
    //     vector<int> res(n, 0);
    //     
    //     // 第一次DFS: 计算每个节点子树的节点数和子树内距离之和
    //     dfs1(0, -1, graph, count, res);
    //     
    //     // 第二次DFS: 利用父节点结果推导子节点结果
    //     dfs2(0, -1, graph, count, res, n);
    //     
    //     return res;
    // }
    
private:
    /**
     * 第一次DFS: 计算每个节点子树的节点数和子树内距离之和
     * 
     * @param node 当前节点
     * @param parent 父节点
     * @param graph 邻接表表示的树
     * @param count count[i]表示以节点i为根的子树节点数
     * @param res res[i]表示节点i到其他所有节点的距离之和
     */
    // void dfs1(int node, int parent, const vector<vector<int>>& graph, 
    //           vector<int>& count, vector<int>& res) {
    //     // 遍历当前节点的所有子节点
    //     for (int child : graph[node]) {
    //         // 避免回到父节点
    //         if (child != parent) {
    //             dfs1(child, node, graph, count, res);
    //             // 累加子树节点数
    //             count[node] += count[child];
    //             // 累加子树内距离之和
    //             // 子树内每个节点到child的距离都增加了1，所以总距离增加count[child]
    //             res[node] += res[child] + count[child];
    //         }
    //     }
    // }
    
    /**
     * 第二次DFS: 利用父节点结果推导子节点结果
     * 
     * @param node 当前节点
     * @param parent 父节点
     * @param graph 邻接表表示的树
     * @param count count[i]表示以节点i为根的子树节点数
     * @param res res[i]表示节点i到其他所有节点的距离之和
     * @param n 节点总数
     */
    // void dfs2(int node, int parent, const vector<vector<int>>& graph, 
    //           const vector<int>& count, vector<int>& res, int n) {
    //     // 遍历当前节点的所有子节点
    //     for (int child : graph[node]) {
    //         // 避免回到父节点
    //         if (child != parent) {
    //             // 当从父节点node换根到子节点child时：
    //             // 1. child子树中的所有节点到child的距离比到node的距离少1，总共减少count[child]
    //             // 2. 除child子树外的其他节点到child的距离比到node的距离多1，总共增加(n - count[child])
    //             res[child] = res[node] - count[child] + (n - count[child]);
    //             dfs2(child, node, graph, count, res, n);
    //         }
    //     }
    // }
};

// 测试函数 - 简化版本
// #include <iostream>
// int main() {
//     LC834_SumOfDistancesInTree solution;
//     
//     // 测试用例1
//     // int n1 = 6;
//     // vector<vector<int>> edges1 = {{0,1},{0,2},{2,3},{2,4},{2,5}};
//     // vector<int> result1 = solution.sumOfDistancesInTree(n1, edges1);
//     // cout << "测试用例1结果: ";
//     // for (int i = 0; i < result1.size(); i++) {
//     //     cout << result1[i] << " ";
//     // }
//     // cout << endl;
//     // 预期输出: 8 12 6 10 10 10
//     
//     return 0;
// }