/**
 * LeetCode 547 - 省份数量
 * https://leetcode-cn.com/problems/number-of-provinces/
 * 
 * 题目描述：
 * 有 n 个城市，其中一些彼此相连，另一些没有相连。如果城市 a 与城市 b 直接相连，且城市 b 与城市 c 直接相连，那么城市 a 与城市 c 间接相连。
 * 
 * 省份是一组直接或间接相连的城市，组内不含其他没有相连的城市。
 * 
 * 给你一个 n x n 的矩阵 isConnected ，其中 isConnected[i][j] = 1 表示第 i 个城市和第 j 个城市直接相连，而 isConnected[i][j] = 0 表示二者不直接相连。
 * 
 * 返回矩阵中省份的数量。
 * 
 * 解题思路：
 * 1. 使用并查集来管理城市之间的连通关系
 * 2. 遍历矩阵，将相连的城市合并到同一个集合中
 * 3. 最后统计集合的数量，即为省份的数量
 * 
 * 时间复杂度分析：
 * - 初始化并查集：O(n)
 * - 遍历矩阵并合并相连的城市：O(n² * α(n))，其中α是阿克曼函数的反函数，近似为常数
 * - 统计集合数量：O(n)
 * - 总体时间复杂度：O(n² * α(n)) ≈ O(n²)
 * 
 * 空间复杂度分析：
 * - 并查集数组：O(n)
 * - 总体空间复杂度：O(n)
 */

#include <iostream>
#include <vector>

using namespace std;

class NumberOfProvinces {
private:
    // 并查集的父节点数组
    vector<int> parent;
    // 并查集的秩数组，用于按秩合并优化
    vector<int> rank;

    /**
     * 查找元素所在集合的根节点，并进行路径压缩
     * @param x 要查找的城市
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
     * @param n 城市数量
     */
    void initUnionFind(int n) {
        parent.resize(n);
        rank.resize(n, 0);

        // 初始化，每个城市的父节点是自己
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

public:
    /**
     * 计算省份的数量
     * @param isConnected 连通矩阵
     * @return 省份数量
     */
    int findCircleNum(vector<vector<int>>& isConnected) {
        int n = isConnected.size();

        // 初始化并查集
        initUnionFind(n);

        // 遍历矩阵，合并相连的城市
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (isConnected[i][j] == 1) {
                    int rootI = find(i);
                    int rootJ = find(j);

                    if (rootI != rootJ) {
                        // 按秩合并：将秩小的树连接到秩大的树下
                        if (rank[rootI] < rank[rootJ]) {
                            parent[rootI] = rootJ;
                        } else if (rank[rootI] > rank[rootJ]) {
                            parent[rootJ] = rootI;
                        } else {
                            // 秩相同时，任选一个作为根，并增加其秩
                            parent[rootJ] = rootI;
                            rank[rootI]++;
                        }
                    }
                }
            }
        }

        // 统计省份数量（即集合的数量）
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (parent[i] == i) {
                count++;
            }
        }

        return count;
    }
};

/**
 * 主函数，用于测试
 */
int main() {
    NumberOfProvinces solution;

    // 测试用例1
    vector<vector<int>> isConnected1 = {
        {1, 1, 0},
        {1, 1, 0},
        {0, 0, 1}
    };
    cout << "测试用例1结果：" << solution.findCircleNum(isConnected1) << endl;
    // 预期输出：2

    // 测试用例2
    vector<vector<int>> isConnected2 = {
        {1, 0, 0},
        {0, 1, 0},
        {0, 0, 1}
    };
    cout << "测试用例2结果：" << solution.findCircleNum(isConnected2) << endl;
    // 预期输出：3

    // 测试用例3：所有城市都相连
    vector<vector<int>> isConnected3 = {
        {1, 1, 1},
        {1, 1, 1},
        {1, 1, 1}
    };
    cout << "测试用例3结果：" << solution.findCircleNum(isConnected3) << endl;
    // 预期输出：1

    // 测试用例4：单个城市
    vector<vector<int>> isConnected4 = {{1}};
    cout << "测试用例4结果：" << solution.findCircleNum(isConnected4) << endl;
    // 预期输出：1

    // 测试用例5：四个城市，形成两个省份
    vector<vector<int>> isConnected5 = {
        {1, 1, 0, 0},
        {1, 1, 0, 0},
        {0, 0, 1, 1},
        {0, 0, 1, 1}
    };
    cout << "测试用例5结果：" << solution.findCircleNum(isConnected5) << endl;
    // 预期输出：2

    return 0;
}

/**
 * C++特定优化：
 * 1. 使用vector容器动态分配并查集数组，避免了固定大小数组的限制
 * 2. 只遍历矩阵的上三角部分，避免重复处理
 * 3. 直接在父节点数组中统计集合数量，不需要额外的哈希表或集合
 * 
 * 注意事项：
 * 1. 在C++中，需要确保vector的大小正确初始化
 * 2. 对于大规模数据，可以考虑使用更高效的路径压缩实现
 * 3. 可以使用模板化的并查集类，以提高代码的可复用性
 */