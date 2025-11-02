/**
 * LeetCode 1319 - 连通网络的操作次数
 * https://leetcode-cn.com/problems/number-of-operations-to-make-network-connected/
 * 
 * 题目描述：
 * 用以太网线缆将 n 台计算机连接成一个网络，计算机的编号从 0 到 n-1。线缆用 connections 表示，其中 connections[i] = [a, b] 表示连接了计算机 a 和 b。
 * 
 * 网络中的任何一台计算机都可以通过网络直接或者间接访问同一个网络中其他任意一台计算机。
 * 
 * 给你这个计算机网络的初始布线 connections，你可以拔开任意两台直连计算机之间的线缆，并用它连接一对未直连的计算机。请你计算并返回使所有计算机都连通所需的最少操作次数。如果不可能，则返回 -1 。
 * 
 * 解题思路：
 * 1. 使用并查集来计算网络中的连通分量数量
 * 2. 首先，我们需要检查线缆数量是否足够：至少需要n-1条线缆才能连接n台计算机
 * 3. 使用并查集统计连通分量的数量count
 * 4. 将所有计算机连通所需的最少操作次数为count - 1
 * 
 * 时间复杂度分析：
 * - 初始化并查集：O(n)
 * - 处理所有连接：O(m * α(n))，其中m是connections数组的长度，α是阿克曼函数的反函数，近似为常数
 * - 计算连通分量：O(n)
 * - 总体时间复杂度：O(n + m * α(n)) ≈ O(n + m)
 * 
 * 空间复杂度分析：
 * - 并查集数组：O(n)
 * - 总体空间复杂度：O(n)
 */

#include <iostream>
#include <vector>

using namespace std;

class NumberOfOperationsToMakeNetworkConnected {
private:
    // 并查集的父节点数组
    vector<int> parent;
    // 并查集的秩数组，用于按秩合并优化
    vector<int> rank;
    // 连通分量的数量
    int count;

    /**
     * 查找元素所在集合的根节点，并进行路径压缩
     * @param x 要查找的元素
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
     * @param n 计算机数量
     */
    void initUnionFind(int n) {
        parent.resize(n);
        rank.resize(n, 0);
        count = n; // 初始时，每个计算机都是一个独立的连通分量

        // 初始化，每个元素的父节点是自己
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

public:
    /**
     * 计算使所有计算机都连通所需的最少操作次数
     * @param n 计算机数量
     * @param connections 连接列表
     * @return 最少操作次数，如果不可能则返回-1
     */
    int makeConnected(int n, vector<vector<int>>& connections) {
        // 检查线缆数量是否足够：至少需要n-1条线缆
        if (connections.size() < n - 1) {
            return -1;
        }

        // 初始化并查集
        initUnionFind(n);

        // 处理所有连接
        for (const auto& connection : connections) {
            int a = connection[0];
            int b = connection[1];
            
            int rootA = find(a);
            int rootB = find(b);

            if (rootA != rootB) {
                // 按秩合并：将秩小的树连接到秩大的树下
                if (rank[rootA] < rank[rootB]) {
                    parent[rootA] = rootB;
                } else if (rank[rootA] > rank[rootB]) {
                    parent[rootB] = rootA;
                } else {
                    // 秩相同时，任选一个作为根，并增加其秩
                    parent[rootB] = rootA;
                    rank[rootA]++;
                }
                // 合并后，连通分量数量减1
                count--;
            }
        }

        // 将所有计算机连通所需的最少操作次数为连通分量数量减1
        return count - 1;
    }
};

/**
 * 主函数，用于测试
 */
int main() {
    NumberOfOperationsToMakeNetworkConnected solution;

    // 测试用例1
    int n1 = 4;
    vector<vector<int>> connections1 = {{0, 1}, {0, 2}, {1, 2}};
    cout << "测试用例1结果：" << solution.makeConnected(n1, connections1) << endl;
    // 预期输出：1

    // 测试用例2
    int n2 = 6;
    vector<vector<int>> connections2 = {{0, 1}, {0, 2}, {0, 3}, {1, 2}, {1, 3}};
    cout << "测试用例2结果：" << solution.makeConnected(n2, connections2) << endl;
    // 预期输出：2

    // 测试用例3
    int n3 = 6;
    vector<vector<int>> connections3 = {{0, 1}, {0, 2}, {0, 3}, {1, 2}};
    cout << "测试用例3结果：" << solution.makeConnected(n3, connections3) << endl;
    // 预期输出：-1

    // 测试用例4：已经连通的情况
    int n4 = 5;
    vector<vector<int>> connections4 = {{0, 1}, {1, 2}, {2, 3}, {3, 4}};
    cout << "测试用例4结果：" << solution.makeConnected(n4, connections4) << endl;
    // 预期输出：0

    // 测试用例5：只有一台计算机
    int n5 = 1;
    vector<vector<int>> connections5 = {};
    cout << "测试用例5结果：" << solution.makeConnected(n5, connections5) << endl;
    // 预期输出：0

    return 0;
}

/**
 * C++特定优化：
 * 1. 使用vector作为并查集的底层存储结构，提供动态大小和高效访问
 * 2. 使用const引用传递参数，避免不必要的拷贝
 * 3. 使用auto关键字简化类型声明，提高代码可读性
 * 4. 在构造函数中初始化成员变量，而不是在每个方法中重复初始化
 * 
 * 注意事项：
 * 1. 需要正确处理单台计算机的边界情况
 * 2. 当线缆数量不足时，必须返回-1
 * 3. 路径压缩和按秩合并是并查集优化的关键，可以显著提高性能
 */