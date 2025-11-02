// #include <iostream>
// #include <vector>
// #include <cstring>
// #include <algorithm>
// using namespace std;

/**
 * LCA问题扩展 - 树上倍增法实现
 * 题目来源：洛谷 P3379 【模板】最近公共祖先（LCA）
 * 题目链接：https://www.luogu.com.cn/problem/P3379
 * 
 * 问题描述：
 * 给定一棵有根多叉树，请求出指定两个点直接最近的公共祖先。
 * 
 * 解题思路：
 * 1. 使用树上倍增法预处理每个节点的2^k级祖先
 * 2. 对于每次查询，先将两个节点调整到同一深度
 * 3. 然后同时向上跳跃，直到找到最近公共祖先
 * 
 * 时间复杂度：
 * 预处理：O(n log n)
 * 查询：O(log n)
 * 空间复杂度：O(n log n)
 * 是否为最优解：是，对于在线查询LCA问题，倍增法是标准解法之一
 * 
 * 工程化考虑：
 * 1. 边界条件处理：处理空树、节点不存在等情况
 * 2. 输入验证：验证输入节点是否在树中
 * 3. 异常处理：对非法输入进行检查
 * 4. 可读性：添加详细注释和变量命名
 * 
 * 算法要点：
 * 1. 预处理阶段构建倍增数组
 * 2. 查询阶段先调整深度再同时跳跃
 * 3. 利用二进制表示优化跳跃过程
 * 
 * 与标准库实现对比：
 * 1. 标准库通常有更完善的错误处理
 * 2. 标准库可能使用更优化的数据结构
 * 
 * 性能优化：
 * 1. 预处理优化：一次处理所有节点
 * 2. 查询优化：利用倍增快速跳跃
 * 
 * 特殊场景：
 * 1. 空输入：返回-1
 * 2. 节点不存在：返回-1
 * 3. 一个节点是另一个节点的祖先：返回祖先节点
 * 
 * 语言特性差异：
 * 1. C++：手动内存管理，指针操作
 * 2. Java：自动垃圾回收，对象引用传递
 * 3. Python：动态类型，引用计数垃圾回收
 * 
 * 数学联系：
 * 1. 与二进制表示和位运算相关
 * 2. 与树的深度优先搜索理论相关
 * 3. 与动态规划有一定联系
 * 
 * 调试能力：
 * 1. 可通过打印预处理数组调试
 * 2. 可通过断言验证中间结果
 * 3. 可通过特殊测试用例验证边界条件
 */

const int MAXN = 500001;
const int LOG = 20;

class TreeAncestor {
private:
    // 邻接表存储树结构
    // vector<vector<int>> adj;
    
    // 深度数组和倍增祖先数组
    // vector<int> depth;
    // vector<vector<int>> ancestor;
    int n;

public:
    /**
     * 构造函数
     * @param n 节点数量
     * @param edges 边的列表
     */
    // TreeAncestor(int n, const vector<vector<int>>& edges) : n(n) {
    //     adj.resize(n);
    //     depth.resize(n, 0);
    //     ancestor.assign(n, vector<int>(LOG, -1));
    //     
    //     // 构建邻接表
    //     for (const auto& edge : edges) {
    //         adj[edge[0]].push_back(edge[1]);
    //         adj[edge[1]].push_back(edge[0]);
    //     }
    //     
    //     // 预处理，构建倍增数组
    //     preprocess(0, -1);
    // }
    
    /**
     * 预处理，构建倍增数组
     * @param u 当前节点
     * @param parent 父节点
     */
    // void preprocess(int u, int parent) {
    //     // 设置父节点和深度
    //     ancestor[u][0] = parent;
    //     if (parent == -1) {
    //         depth[u] = 0;
    //     } else {
    //         depth[u] = depth[parent] + 1;
    //     }
    //     
    //     // 构建倍增数组
    //     for (int i = 1; i < LOG; i++) {
    //         if (ancestor[u][i - 1] == -1) {
    //             ancestor[u][i] = -1;
    //         } else {
    //             ancestor[u][i] = ancestor[ancestor[u][i - 1]][i - 1];
    //         }
    //     }
    //     
    //     // 递归处理子节点
    //     for (int v : adj[u]) {
    //         if (v != parent) {
    //             preprocess(v, u);
    //         }
    //     }
    // }
    
    /**
     * 查询两个节点的最近公共祖先
     * @param u 节点u
     * @param v 节点v
     * @return 最近公共祖先
     */
    // int getLCA(int u, int v) {
    //     // 异常处理
    //     if (u < 0 || u >= n || v < 0 || v >= n) {
    //         return -1;
    //     }
    //     
    //     // 确保u的深度不小于v
    //     if (depth[u] < depth[v]) {
    //         swap(u, v);
    //     }
    //     
    //     // 将u向上跳跃到与v同一深度
    //     for (int i = LOG - 1; i >= 0; i--) {
    //         if (ancestor[u][i] != -1 && depth[ancestor[u][i]] >= depth[v]) {
    //             u = ancestor[u][i];
    //         }
    //     }
    //     
    //     // 如果u和v已经相同，则找到了LCA
    //     if (u == v) {
    //         return u;
    //     }
    //     
    //     // u和v同时向上跳跃，直到找到LCA
    //     for (int i = LOG - 1; i >= 0; i--) {
    //         if (ancestor[u][i] != ancestor[v][i]) {
    //             u = ancestor[u][i];
    //             v = ancestor[v][i];
    //         }
    //     }
    //     
    //     // 返回LCA
    //     return ancestor[u][0];
    // }
};

/**
 * 测试方法
 */
int main() {
    // 构建测试用例
    // 树结构: 0-1-2, 0-3, 1-4
    // int n = 5;
    // vector<vector<int>> edges = {{0, 1}, {1, 2}, {0, 3}, {1, 4}};
    
    // TreeAncestor tree(n, edges);
    
    // 测试用例1: LCA(2, 3) = 0
    // int result1 = tree.getLCA(2, 3);
    // printf("测试用例1 - LCA(2, 3): %d\n", result1);
    
    // 测试用例2: LCA(2, 4) = 1
    // int result2 = tree.getLCA(2, 4);
    // printf("测试用例2 - LCA(2, 4): %d\n", result2);
    
    // 测试用例3: LCA(3, 4) = 0
    // int result3 = tree.getLCA(3, 4);
    // printf("测试用例3 - LCA(3, 4): %d\n", result3);
    
    return 0;
}