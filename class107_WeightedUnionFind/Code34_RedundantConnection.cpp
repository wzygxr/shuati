/**
 * LeetCode 684 - 冗余连接
 * https://leetcode-cn.com/problems/redundant-connection/
 * 
 * 题目描述：
 * 在本问题中，树指的是一个连通且无环的无向图。
 * 
 * 输入一个图，该图由一个有着N个节点（节点值不重复1, 2, ..., N）的树及一条附加的边构成。附加的边的两个顶点包含在1到N中间，这条附加的边不属于树中已存在的边。
 * 
 * 结果图是一个以边组成的二维数组。每一个边的元素是一对[u, v] ，满足 u < v，表示连接顶点u和v的无向图的边。
 * 
 * 返回一条可以删去的边，使得结果图是一个有着N个节点的树。如果有多个答案，则返回二维数组中最后出现的边。
 * 
 * 示例 1：
 * 输入: [[1,2], [1,3], [2,3]]
 * 输出: [2,3]
 * 解释: 给定的无向图为:
 *   1
 *  / \
 * 2 - 3
 * 
 * 示例 2：
 * 输入: [[1,2], [2,3], [3,4], [1,4], [1,5]]
 * 输出: [1,4]
 * 解释: 给定的无向图为:
 * 5 - 1 - 2
 *     |   |
 *     4 - 3
 * 
 * 解题思路（并查集）：
 * 1. 对于每一条边(u, v)，检查u和v是否已经连通
 * 2. 如果已经连通，说明这条边是冗余的，可以形成环
 * 3. 否则，将u和v合并到同一个集合中
 * 4. 返回最后一条导致环的边
 * 
 * 时间复杂度分析：
 * - 并查集操作（find和union）的平均时间复杂度为O(α(n))，其中α是阿克曼函数的反函数
 * - 遍历m条边需要O(m * α(n))时间
 * - 总体时间复杂度：O(m * α(n)) ≈ O(m)
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
    vector<int> parent;
    vector<int> rank; // 用于按秩合并

    /**
     * 初始化并查集
     * @param n 节点数量
     */
    void initUnionFind(int n) {
        parent.resize(n + 1); // 节点编号从1开始
        rank.resize(n + 1, 1);
        for (int i = 0; i <= n; i++) {
            parent[i] = i;
        }
    }

    /**
     * 查找元素所在集合的根节点，并进行路径压缩
     * @param x 要查找的元素
     * @return 根节点
     */
    int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // 路径压缩
        }
        return parent[x];
    }

    /**
     * 合并两个元素所在的集合
     * @param x 第一个元素
     * @param y 第二个元素
     * @return 如果两个元素已经在同一集合中，返回false；否则合并并返回true
     */
    bool unite(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return false; // 已经连通，说明边是冗余的
        }

        // 按秩合并：将较小的树合并到较大的树下
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }

        return true;
    }

public:
    /**
     * 找到冗余连接
     * @param edges 边的数组
     * @return 冗余的边
     */
    vector<int> findRedundantConnection(vector<vector<int>>& edges) {
        int n = edges.size(); // 节点数量为n，因为树有n个节点和n-1条边，加上一条冗余边，总共有n条边

        // 初始化并查集
        initUnionFind(n);

        // 遍历每条边
        for (const auto& edge : edges) {
            int u = edge[0];
            int v = edge[1];

            // 如果u和v已经连通，说明这条边是冗余的
            if (!unite(u, v)) {
                return edge; // 返回最后一条导致环的边
            }
        }

        return {}; // 不应该到达这里
    }
};

/**
 * 打印结果数组
 * @param result 结果数组
 */
void printResult(const vector<int>& result) {
    cout << "[" << result[0] << ", " << result[1] << "]" << endl;
}

/**
 * 主函数，用于测试
 */
int main() {
    RedundantConnection solution;

    // 测试用例1
    vector<vector<int>> edges1 = {
        {1, 2},
        {1, 3},
        {2, 3}
    };
    vector<int> result1 = solution.findRedundantConnection(edges1);
    cout << "测试用例1结果：";
    printResult(result1);
    // 预期输出：[2, 3]

    // 测试用例2
    vector<vector<int>> edges2 = {
        {1, 2},
        {2, 3},
        {3, 4},
        {1, 4},
        {1, 5}
    };
    vector<int> result2 = solution.findRedundantConnection(edges2);
    cout << "测试用例2结果：";
    printResult(result2);
    // 预期输出：[1, 4]

    // 测试用例3
    vector<vector<int>> edges3 = {
        {1, 2},
        {2, 3},
        {3, 4},
        {4, 5},
        {5, 1}
    };
    vector<int> result3 = solution.findRedundantConnection(edges3);
    cout << "测试用例3结果：";
    printResult(result3);
    // 预期输出：[5, 1]

    return 0;
}

/**
 * C++特定优化：
 * 1. 使用vector容器存储并查集和结果
 * 2. 使用const引用提高效率
 * 3. 使用unite命名替代union，避免与C++关键字冲突
 * 4. 实现了路径压缩和按秩合并优化
 * 
 * 时间复杂度分析：
 * - 并查集操作的平均时间复杂度为O(α(n))
 * - 遍历m条边需要O(m * α(n))时间
 * - 总体时间复杂度：O(m * α(n)) ≈ O(m)
 * 
 * 空间复杂度分析：
 * - 并查集数组：O(n)
 * - 总体空间复杂度：O(n)
 * 
 * 算法思路详解：
 * 1. 初始化并查集，每个节点的父节点是自己
 * 2. 遍历每条边(u, v)
 * 3. 查找u和v的根节点
 * 4. 如果根节点相同，说明u和v已经连通，这条边是冗余的
 * 5. 如果根节点不同，将u和v合并到同一个集合
 * 6. 返回最后一条导致环的边
 */