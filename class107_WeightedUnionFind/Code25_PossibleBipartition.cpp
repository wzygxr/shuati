/**
 * LeetCode 886 - 可能的二分法
 * https://leetcode-cn.com/problems/possible-bipartition/
 * 
 * 题目描述：
 * 给定一组 n 个人（编号为 1, 2, ..., n），我们想把每个人分进任意大小的两组。每个人都可能不喜欢其他人，那么他们不应该属于同一组。
 * 
 * 给定不喜欢的人对列表 dislikes，其中 dislikes[i] = [a, b]，表示不允许将编号为 a 和 b 的人归入同一组。当可以用这种方法将所有人分进两组时，返回 true；否则返回 false。
 * 
 * 解题思路：
 * 这是一个典型的二分图判定问题，可以使用并查集或者DFS/BFS来解决。
 * 这里我们使用并查集的方法：
 * 1. 对于每个人来说，如果他不喜欢某个人，那么他应该和这个人的所有不喜欢的人属于同一组
 * 2. 我们可以使用一个邻接表来记录每个人的不喜欢列表
 * 3. 对于每个人，我们将他不喜欢的人的不喜欢列表中的所有人合并到同一个集合中
 * 4. 最后，我们检查是否存在任何人与其不喜欢的人在同一个集合中
 * 
 * 时间复杂度分析：
 * - 构建不喜欢列表：O(m)，其中m是dislikes数组的长度
 * - 并查集操作：O(m * α(n))，其中α是阿克曼函数的反函数，近似为常数
 * - 检查冲突：O(m)
 * - 总体时间复杂度：O(m * α(n)) ≈ O(m)
 * 
 * 空间复杂度分析：
 * - 并查集数组：O(n)
 * - 不喜欢列表：O(m)
 * - 总体空间复杂度：O(n + m)
 */

#include <iostream>
#include <vector>
#include <unordered_map>

using namespace std;

class PossibleBipartition {
private:
    // 并查集的父节点数组
    vector<int> parent;
    // 不喜欢列表，记录每个人不喜欢的所有人
    vector<vector<int>> dislikeList;

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
     * 合并两个元素所在的集合
     * @param x 第一个元素
     * @param y 第二个元素
     */
    void unite(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX != rootY) {
            parent[rootY] = rootX;
        }
    }

    /**
     * 初始化不喜欢列表
     * @param n 人数
     * @param dislikes 不喜欢的人对列表
     */
    void initDislikeList(int n, const vector<vector<int>>& dislikes) {
        dislikeList.resize(n + 1);

        for (const auto& pair : dislikes) {
            int a = pair[0];
            int b = pair[1];
            dislikeList[a].push_back(b);
            dislikeList[b].push_back(a);
        }
    }

public:
    /**
     * 判断是否可以将所有人分成两组
     * @param n 人数
     * @param dislikes 不喜欢的人对列表
     * @return 是否可以分成两组
     */
    bool possibleBipartition(int n, vector<vector<int>>& dislikes) {
        // 初始化并查集
        parent.resize(n + 1);
        for (int i = 0; i <= n; i++) {
            parent[i] = i;
        }

        // 初始化不喜欢列表
        initDislikeList(n, dislikes);

        // 对于每个人，将他不喜欢的人的不喜欢列表中的所有人合并到同一个集合中
        for (int i = 1; i <= n; i++) {
            const vector<int>& dislikesOfI = dislikeList[i];
            if (dislikesOfI.empty()) continue;

            // 第一个不喜欢的人
            int firstDislike = dislikesOfI[0];

            // 将i的所有不喜欢的人合并到同一个集合
            for (size_t j = 1; j < dislikesOfI.size(); j++) {
                unite(firstDislike, dislikesOfI[j]);
            }
        }

        // 检查是否存在冲突：如果一个人与其不喜欢的人在同一个集合中，则无法二分
        for (int i = 1; i <= n; i++) {
            for (int dislike : dislikeList[i]) {
                if (find(i) == find(dislike)) {
                    return false;
                }
            }
        }

        return true;
    }
};

/**
 * 主函数，用于测试
 */
int main() {
    PossibleBipartition solution;

    // 测试用例1
    int n1 = 4;
    vector<vector<int>> dislikes1 = {{1, 2}, {1, 3}, {2, 4}};
    cout << "测试用例1结果：" << (solution.possibleBipartition(n1, dislikes1) ? "true" : "false") << endl;
    // 预期输出：true

    // 测试用例2
    int n2 = 3;
    vector<vector<int>> dislikes2 = {{1, 2}, {1, 3}, {2, 3}};
    cout << "测试用例2结果：" << (solution.possibleBipartition(n2, dislikes2) ? "true" : "false") << endl;
    // 预期输出：false

    // 测试用例3
    int n3 = 5;
    vector<vector<int>> dislikes3 = {{1, 2}, {2, 3}, {3, 4}, {4, 5}, {1, 5}};
    cout << "测试用例3结果：" << (solution.possibleBipartition(n3, dislikes3) ? "true" : "false") << endl;
    // 预期输出：false

    // 测试用例4：空的不喜欢列表
    int n4 = 5;
    vector<vector<int>> dislikes4 = {};
    cout << "测试用例4结果：" << (solution.possibleBipartition(n4, dislikes4) ? "true" : "false") << endl;
    // 预期输出：true

    // 测试用例5：只有一个人
    int n5 = 1;
    vector<vector<int>> dislikes5 = {};
    cout << "测试用例5结果：" << (solution.possibleBipartition(n5, dislikes5) ? "true" : "false") << endl;
    // 预期输出：true

    return 0;
}

/**
 * C++特定优化：
 * 1. 使用vector作为邻接表，提供快速的随机访问
 * 2. 使用const引用传递参数，避免不必要的拷贝
 * 3. 使用size_t类型处理索引，避免潜在的类型转换问题
 * 4. 方法名使用unite而不是union，避免与C++关键字冲突
 * 
 * 注意事项：
 * 1. 人的编号从1开始，所以并查集和不喜欢列表的大小需要是n+1
 * 2. 对于空的不喜欢列表，应该直接返回true
 * 3. 可以考虑使用DFS/BFS染色法作为另一种实现方式，可能在某些情况下更高效
 */