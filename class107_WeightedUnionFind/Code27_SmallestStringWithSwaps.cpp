/**
 * LeetCode 1202 - 交换字符串中的元素
 * https://leetcode-cn.com/problems/smallest-string-with-swaps/
 * 
 * 题目描述：
 * 给你一个字符串 s，以及该字符串中的一些「索引对」数组 pairs，其中 pairs[i] = [a, b] 表示字符串中的两个索引（编号从 0 开始）。
 * 
 * 你可以多次交换在 pairs 中任意一对索引处的字符。
 * 
 * 返回在经过若干次交换后，该字符串可以变成的按字典序最小的字符串。
 * 
 * 解题思路：
 * 1. 使用并查集将可以互相交换的字符的索引归为一个连通分量
 * 2. 对于每个连通分量，将其对应的字符收集起来并排序
 * 3. 按照排序后的字符顺序重新填充原字符串
 * 
 * 时间复杂度分析：
 * - 初始化并查集：O(n)
 * - 处理所有索引对：O(m * α(n))，其中m是pairs数组的长度
 * - 收集字符并排序：O(n * log n)
 * - 重建字符串：O(n)
 * - 总体时间复杂度：O(n * log n + m * α(n)) ≈ O(n * log n + m)
 * 
 * 空间复杂度分析：
 * - 并查集数组：O(n)
 * - 存储连通分量的映射：O(n)
 * - 存储排序后的字符：O(n)
 * - 总体空间复杂度：O(n)
 */

#include <iostream>
#include <string>
#include <vector>
#include <unordered_map>
#include <queue>

using namespace std;

class SmallestStringWithSwaps {
private:
    // 并查集的父节点数组
    vector<int> parent;
    // 并查集的秩数组，用于按秩合并优化
    vector<int> rank;

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
     * @param n 字符串长度
     */
    void initUnionFind(int n) {
        parent.resize(n);
        rank.resize(n, 0);

        // 初始化，每个元素的父节点是自己
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

public:
    /**
     * 交换字符串中的元素，使得结果字典序最小
     * @param s 原始字符串
     * @param pairs 索引对数组
     * @return 字典序最小的字符串
     */
    string smallestStringWithSwaps(string s, vector<vector<int>>& pairs) {
        int n = s.length();

        // 初始化并查集
        initUnionFind(n);

        // 处理所有索引对，将可以互相交换的索引归为一个连通分量
        for (const auto& pair : pairs) {
            int a = pair[0];
            int b = pair[1];
            
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
            }
        }

        // 使用unordered_map将每个连通分量的根节点映射到对应的字符优先队列
        unordered_map<int, priority_queue<char, vector<char>, greater<char>>> componentMap;
        for (int i = 0; i < n; i++) {
            int root = find(i);
            // 将字符添加到对应的优先队列中
            componentMap[root].push(s[i]);
        }

        // 重建字符串
        string result;
        for (int i = 0; i < n; i++) {
            int root = find(i);
            // 从对应的优先队列中取出最小的字符
            result += componentMap[root].top();
            componentMap[root].pop();
        }

        return result;
    }
};

/**
 * 主函数，用于测试
 */
int main() {
    SmallestStringWithSwaps solution;

    // 测试用例1
    string s1 = "dcab";
    vector<vector<int>> pairs1 = {{0, 3}, {1, 2}};
    cout << "测试用例1结果：" << solution.smallestStringWithSwaps(s1, pairs1) << endl;
    // 预期输出：bacd

    // 测试用例2
    string s2 = "dcab";
    vector<vector<int>> pairs2 = {{0, 3}, {1, 2}, {0, 2}};
    cout << "测试用例2结果：" << solution.smallestStringWithSwaps(s2, pairs2) << endl;
    // 预期输出：abcd

    // 测试用例3：没有交换对的情况
    string s3 = "dcba";
    vector<vector<int>> pairs3 = {};
    cout << "测试用例3结果：" << solution.smallestStringWithSwaps(s3, pairs3) << endl;
    // 预期输出：dcba

    // 测试用例4：所有字符都可以交换的情况
    string s4 = "dcba";
    vector<vector<int>> pairs4 = {{0, 1}, {1, 2}, {2, 3}};
    cout << "测试用例4结果：" << solution.smallestStringWithSwaps(s4, pairs4) << endl;
    // 预期输出：abcd

    return 0;
}

/**
 * C++特定优化：
 * 1. 使用priority_queue<char, vector<char>, greater<char>>作为最小堆，自动保持字符有序
 * 2. 使用unordered_map代替HashMap，提供更高效的查找性能
 * 3. 使用const引用传递参数，避免不必要的拷贝
 * 4. 直接操作string的字符，提高性能
 * 
 * 注意事项：
 * 1. 在C++中，优先队列默认是最大堆，需要使用greater<char>来创建最小堆
 * 2. 对于大规模数据，可以考虑使用更高效的排序算法
 * 3. 可以使用vector<char>替代string来操作字符，可能在某些情况下更高效
 */