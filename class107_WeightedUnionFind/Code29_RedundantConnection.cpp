/**
 * LeetCode 684 - 冗余连接
 * https://leetcode-cn.com/problems/redundant-connection/
 * 
 * 题目描述：
 * 在本问题中，树指的是一个连通且无环的无向图。
 * 
 * 输入一个图，该图由一个有着n个节点（节点值不重复1，2，...，n）的树及一条附加的边构成。附加的边的两个顶点包含在1到n中间，
 * 这条附加的边不属于树中已存在的边。
 * 
 * 结果图是一个以边组成的二维数组edges。每一个边的元素是一对[u, v]，满足u < v，表示连接顶点u和v的无向图的边。
 * 
 * 返回一条可以删去的边，使得结果图是一个有着n个节点的树。如果有多个答案，则返回二维数组中最后出现的边。
 * 
 * 解题思路：
 * 1. 使用并查集来检测环
 * 2. 遍历每一条边，尝试将两个顶点合并
 * 3. 如果两个顶点已经在同一个集合中，说明添加这条边会形成环，这条边就是冗余的
 * 4. 返回最后一条导致环的边
 * 
 * 时间复杂度分析：
 * - 初始化并查集：O(n)
 * - 处理每条边：O(m * α(n))，其中m是边的数量，α是阿克曼函数的反函数，近似为常数
 * - 总体时间复杂度：O(n + m * α(n)) ≈ O(n + m)
 * 
 * 空间复杂度分析：
 * - 并查集数组：O(n)
 * - 总体空间复杂度：O(n)
 */

#include <iostream>
#include <vector>

using namespace std;

class RedundantConnection {
private:
    // 并查集的父节点数组
    vector<int> parent;
    // 并查集的秩数组，用于按秩合并优化
    vector<int> rank;

    /**
     * 查找元素所在集合的根节点，并进行路径压缩
     * @param x 要查找的节点
     * @return 根节点
     */
    int find(int x) {
        if (parent[x] != x) {
            // 路径压缩：将x的父节点直接设置为根节点
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    /**
     * 初始化并查集
     * @param n 节点数量
     */
    void initUnionFind(int n) {
        parent.resize(n + 1); // 节点编号从1开始
        rank.resize(n + 1, 0);

        // 初始化，每个节点的父节点是自己
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
        }
    }

public:
    /**
     * 查找冗余连接
     * @param edges 边的数组
     * @return 冗余的边
     */
    vector<int> findRedundantConnection(vector<vector<int>>& edges) {
        int n = edges.size(); // 节点数量等于边的数量（树有n-1条边，加上一条冗余边）

        // 初始化并查集
        initUnionFind(n);

        // 遍历每一条边
        for (const auto& edge : edges) {
            int u = edge[0];
            int v = edge[1];

            int rootU = find(u);
            int rootV = find(v);

            // 如果两个节点已经在同一个集合中，说明添加这条边会形成环
            if (rootU == rootV) {
                return edge;
            }

            // 按秩合并：将秩小的树连接到秩大的树下
            if (rank[rootU] < rank[rootV]) {
                parent[rootU] = rootV;
            } else if (rank[rootU] > rank[rootV]) {
                parent[rootV] = rootU;
            } else {
                // 秩相同时，任选一个作为根，并增加其秩
                parent[rootV] = rootU;
                rank[rootU]++;
            }
        }

        // 根据题目描述，一定存在冗余边，所以不会执行到这里
        return {};
    }
};

/**
 * 打印结果数组
 */
void printResult(const vector<int>& result) {
    cout << "[" << result[0] << ", " << result[1] << "]";
}

/**
 * 主函数，用于测试
 */
int main() {
    RedundantConnection solution;

    // 测试用例1
    vector<vector<int>> edges1 = {{1, 2}, {1, 3}, {2, 3}};
    vector<int> result1 = solution.findRedundantConnection(edges1);
    cout << "测试用例1结果：";
    printResult(result1);
    cout << endl;
    // 预期输出：[2, 3]

    // 测试用例2
    vector<vector<int>> edges2 = {{1, 2}, {2, 3}, {3, 4}, {1, 4}, {1, 5}};
    vector<int> result2 = solution.findRedundantConnection(edges2);
    cout << "测试用例2结果：";
    printResult(result2);
    cout << endl;
    // 预期输出：[1, 4]

    // 测试用例3
    vector<vector<int>> edges3 = {{1, 2}, {1, 3}, {3, 4}, {2, 4}, {4, 5}};
    vector<int> result3 = solution.findRedundantConnection(edges3);
    cout << "测试用例3结果：";
    printResult(result3);
    cout << endl;
    // 预期输出：[2, 4]

    // 测试用例4：简单情况
    vector<vector<int>> edges4 = {{1, 2}, {2, 1}}; // 自环的情况
    vector<int> result4 = solution.findRedundantConnection(edges4);
    cout << "测试用例4结果：";
    printResult(result4);
    cout << endl;
    // 预期输出：[2, 1]

    return 0;
}

/**
 * C++特定优化：
 * 1. 使用vector容器动态分配并查集数组，避免了固定大小数组的限制
 * 2. 使用const引用参数避免不必要的拷贝操作
 * 3. 使用空的vector作为默认返回值，符合C++的返回值规范
 * 4. 创建辅助函数printResult用于格式化输出
 * 
 * 注意事项：
 * 1. 在C++中，需要注意节点编号从1开始，所以并查集数组的大小设为n+1
 * 2. 对于大规模数据，可以考虑使用更高效的路径压缩实现
 * 3. 可以使用模板化的并查集类，以提高代码的可复用性
 */