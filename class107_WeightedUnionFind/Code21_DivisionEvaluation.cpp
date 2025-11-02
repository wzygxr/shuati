/**
 * LeetCode 399 - 除法求值
 * https://leetcode-cn.com/problems/evaluate-division/
 * 
 * 题目描述：
 * 给你一个变量对数组 equations 和一个实数值数组 values 作为已知条件，其中 equations[i] = [Ai, Bi] 和 values[i] 共同表示等式 Ai / Bi = values[i]。
 * 每个 Ai 或 Bi 是一个表示单个变量的字符串。
 * 
 * 另有一些以数组 queries 表示的问题，其中 queries[j] = [Cj, Dj] 表示第 j 个问题，请你根据已知条件，返回 Cj / Dj = ? 的结果作为答案。
 * 如果无法确定结果，请返回 -1.0。
 * 
 * 注意：输入总是有效的。你可以假设除法运算中不会出现除数为0的情况，且不存在任何矛盾的结果。
 * 
 * 解题思路：
 * 1. 使用带权并查集来解决这个问题
 * 2. 权值表示从当前节点到父节点的商（即父节点 / 当前节点的值）
 * 3. find 操作时进行路径压缩，并同时更新权值
 * 4. union 操作时合并两个节点，并维护权值关系
 * 5. 查询时，如果两个节点不在同一集合，返回 -1.0；否则返回它们的权值比
 * 
 * 时间复杂度分析：
 * - 构建并查集：O(n * α(m))，其中n是equations的长度，m是不同变量的数量，α是阿克曼函数的反函数，近似为常数
 * - 处理查询：O(q * α(m))，其中q是queries的长度
 * - 总体时间复杂度：O((n+q) * α(m))
 * 
 * 空间复杂度分析：
 * - 存储并查集：O(m)
 * - 总体空间复杂度：O(m)
 */

#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <iomanip>

using namespace std;

class DivisionEvaluation {
private:
    // 并查集的父节点映射
    unordered_map<string, string> parent;
    // 并查集的权值映射，表示从当前节点到父节点的商（parent / current）
    unordered_map<string, double> weight;

public:
    DivisionEvaluation() {
        // 构造函数，初始化在使用时动态添加
    }

    /**
     * 查找节点的根节点，并进行路径压缩，同时更新权值
     * @param x 要查找的节点
     * @return 根节点
     */
    string find(const string& x) {
        // 如果节点不存在于并查集中，将其加入并查集
        if (parent.find(x) == parent.end()) {
            parent[x] = x;
            weight[x] = 1.0; // 自己到自己的商为1
            return x;
        }

        // 如果x不是根节点，需要进行路径压缩
        if (parent[x] != x) {
            string originParent = parent[x];
            // 递归查找父节点的根节点，同时更新父节点的权值
            string root = find(parent[x]);
            // 更新x的父节点为根节点（路径压缩）
            parent[x] = root;
            // 更新x的权值：x到根节点的权值 = x到原父节点的权值 * 原父节点到根节点的权值
            weight[x] *= weight[originParent];
        }
        return parent[x];
    }

    /**
     * 合并两个节点，并维护权值关系
     * @param x 第一个节点
     * @param y 第二个节点
     * @param value x / y 的值
     */
    void unite(const string& x, const string& y, double value) {
        // 查找x和y的根节点
        string rootX = find(x);
        string rootY = find(y);

        // 如果x和y已经在同一个集合中，不需要合并
        if (rootX == rootY) {
            return;
        }

        // 合并x的集合到y的集合
        parent[rootX] = rootY;
        // 维护权值关系：
        // 已知x / y = value
        // 需要确定 rootX / rootY 的值
        // x到rootX的权值是 weight[x]，即 rootX / x
        // y到rootY的权值是 weight[y]，即 rootY / y
        // 所以 rootX / rootY = (rootX / x) * (x / y) * (y / rootY) = weight[x] * value * (1 / weight[y])
        weight[rootX] = weight[x] * value / weight[y];
    }

    /**
     * 计算除法求值问题
     * @param equations 等式数组
     * @param values 等式结果数组
     * @param queries 查询数组
     * @return 查询结果数组
     */
    vector<double> calcEquation(vector<vector<string>>& equations, vector<double>& values, vector<vector<string>>& queries) {
        // 清空并查集
        parent.clear();
        weight.clear();

        // 构建并查集
        for (int i = 0; i < equations.size(); i++) {
            string x = equations[i][0];
            string y = equations[i][1];
            double value = values[i]; // x / y = value
            unite(x, y, value);
        }

        // 处理查询
        vector<double> results;
        results.reserve(queries.size()); // 预分配空间

        for (const auto& query : queries) {
            string x = query[0];
            string y = query[1];

            // 如果x或y不存在于并查集中，无法计算
            if (parent.find(x) == parent.end() || parent.find(y) == parent.end()) {
                results.push_back(-1.0);
                continue;
            }

            string rootX = find(x);
            string rootY = find(y);

            // 如果x和y不在同一个集合中，无法计算
            if (rootX != rootY) {
                results.push_back(-1.0);
            } else {
                // x / y = (x到根节点的权值倒数) / (y到根节点的权值倒数) = weight[y] / weight[x]
                // 因为weight存储的是 root / node，所以 node = root / weight[node]
                results.push_back(weight[y] / weight[x]);
            }
        }

        return results;
    }
};

/**
 * 主函数，用于测试
 */
int main() {
    DivisionEvaluation solution;

    // 测试用例1
    vector<vector<string>> equations1 = {
        {"a", "b"},
        {"b", "c"}
    };
    vector<double> values1 = {2.0, 3.0};
    vector<vector<string>> queries1 = {
        {"a", "c"},
        {"b", "a"},
        {"a", "e"},
        {"a", "a"},
        {"x", "x"}
    };

    vector<double> results1 = solution.calcEquation(equations1, values1, queries1);
    cout << "测试用例1结果：" << endl;
    cout << fixed << setprecision(5);
    for (double result : results1) {
        cout << result << " ";
    }
    cout << endl;

    // 测试用例2
    vector<vector<string>> equations2 = {
        {"a", "b"},
        {"b", "c"},
        {"bc", "cd"}
    };
    vector<double> values2 = {1.5, 2.5, 5.0};
    vector<vector<string>> queries2 = {
        {"a", "c"},
        {"c", "b"},
        {"bc", "cd"},
        {"cd", "bc"}
    };

    vector<double> results2 = solution.calcEquation(equations2, values2, queries2);
    cout << "测试用例2结果：" << endl;
    for (double result : results2) {
        cout << result << " ";
    }
    cout << endl;

    return 0;
}

/**
 * 异常处理考虑：
 * 1. 输入参数校验：equations和values长度是否一致，queries是否合法
 * 2. 处理不存在的变量：当查询中包含未在equations中出现的变量时，返回-1.0
 * 3. 处理自环查询：如a/a返回1.0
 * 4. 精度问题：浮点数计算可能存在精度误差，这里直接使用double类型
 * 
 * C++特定优化：
 * 1. 使用unordered_map代替map以获得更好的平均查找性能
 * 2. 使用reserve预分配容器空间，减少动态扩容开销
 * 3. 使用const引用传递参数，避免不必要的拷贝
 * 4. 使用fixed和setprecision控制输出精度
 */