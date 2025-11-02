#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

/**
 * 洛谷U81904 - 树的直径
 * 题目描述：给定一棵树，树中每条边都有一个权值，树中两点之间的距离定义为连接两点的路径边权之和。
 * 树中最远的两个节点之间的距离被称为树的直径，连接这两点的路径被称为树的最长链。
 * 现在让你求出树的最长链的距离。
 * 
 * 输入格式：
 * - 第一行为一个正整数n，表示这颗树有n个节点
 * - 接下来的n−1行，每行三个正整数u,v,w，表示u,v（u,v<=n）有一条权值为w的边相连
 * 
 * 输出格式：输入仅一行，表示树的最长链的距离
 * 
 * 解题思路：使用树形DP法求解，因为边权可能为负
 * 对于每个节点，维护两个值：
 * 1. 该节点到其子树中的最长路径长度
 * 2. 该节点到其子树中的次长路径长度
 * 那么，经过该节点的最长路径就是这两个值的和。遍历所有节点，取最大值即为树的直径
 * 
 * 时间复杂度：O(n)，空间复杂度：O(n)
 */

struct Edge {
    int to;     // 目标节点
    int weight; // 边权
    Edge(int t, int w) : to(t), weight(w) {}
};

int n;                  // 节点数量
vector<vector<Edge>> graph; // 邻接表存储树结构
int maxDiameter;        // 记录树的直径

/**
 * 树形DP函数，计算每个节点的最长路径和次长路径，并更新全局最大直径
 * @param current 当前节点
 * @param parent 当前节点的父节点，避免回到父节点
 * @return 当前节点到其子树中的最长路径长度
 */
int treeDP(int current, int parent) {
    int max1 = 0; // 最长路径
    int max2 = 0; // 次长路径
    
    for (const Edge& edge : graph[current]) {
        int next = edge.to;
        int weight = edge.weight;
        
        // 避免回到父节点
        if (next == parent) {
            continue;
        }
        
        // 递归计算子节点的最长路径
        int depth = treeDP(next, current) + weight;
        
        // 更新最长和次长路径
        if (depth > max1) {
            max2 = max1;
            max1 = depth;
        } else if (depth > max2) {
            max2 = depth;
        }
    }
    
    // 更新全局最大直径
    maxDiameter = max(maxDiameter, max1 + max2);
    
    // 返回当前节点的最长路径
    return max1;
}

int main() {
    cin >> n;
    
    // 初始化邻接表
    graph.resize(n + 1);
    
    // 读取边
    for (int i = 0; i < n - 1; i++) {
        int u, v, w;
        cin >> u >> v >> w;
        // 无向树，添加双向边
        graph[u].emplace_back(v, w);
        graph[v].emplace_back(u, w);
    }
    
    maxDiameter = 0;
    // 从任意节点开始树形DP，这里选择1号节点
    treeDP(1, -1);
    
    // 输出树的直径
    cout << maxDiameter << endl;
    
    return 0;
}