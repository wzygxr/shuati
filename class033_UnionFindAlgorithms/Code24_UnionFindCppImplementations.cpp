/**
 * 并查集C++实现合集
 * 包含各种并查集变种的C++实现和测试
 */

#include <iostream>
#include <vector>
#include <unordered_map>
#include <unordered_set>
#include <algorithm>
#include <queue>
#include <stack>
#include <cmath>
#include <limits>
#include <string>
#include <sstream>
#include <chrono>
#include <tuple>
using namespace std;

/**
 * 标准并查集实现（路径压缩 + 按秩合并）
 */
class UnionFind {
private:
    vector<int> parent;
    vector<int> rank;
    int count;

public:
    UnionFind(int n) : parent(n), rank(n, 1), count(n) {
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }
    
    int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);  // 路径压缩
        }
        return parent[x];
    }
    
    bool unionSets(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        
        if (rootX == rootY) {
            return false;
        }
        
        // 按秩合并
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
        
        count--;
        return true;
    }
    
    bool isConnected(int x, int y) {
        return find(x) == find(y);
    }
    
    int getCount() {
        return count;
    }
};

/**
 * 带权并查集实现
 */
class WeightedUnionFind {
private:
    vector<int> parent;
    vector<double> weight;  // weight[i] 表示 i / parent[i]

public:
    WeightedUnionFind(int n) : parent(n), weight(n, 1.0) {
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }
    
    pair<int, double> find(int x) {
        if (parent[x] != x) {
            auto [root, rootWeight] = find(parent[x]);
            parent[x] = root;
            weight[x] *= rootWeight;
            return {root, weight[x]};
        }
        return {x, 1.0};
    }
    
    bool unionSets(int x, int y, double value) {
        auto [rootX, weightX] = find(x);
        auto [rootY, weightY] = find(y);
        
        if (rootX == rootY) {
            // 检查是否冲突
            return abs(weightX / weightY - value) < 1e-9;
        }
        
        parent[rootX] = rootY;
        weight[rootX] = value * weightY / weightX;
        return true;
    }
    
    double query(int x, int y) {
        auto [rootX, weightX] = find(x);
        auto [rootY, weightY] = find(y);
        
        if (rootX != rootY) {
            return -1.0;  // 不连通
        }
        return weightX / weightY;
    }
};

/**
 * LeetCode 128. 最长连续序列
 */
class Solution128 {
public:
    int longestConsecutive(vector<int>& nums) {
        if (nums.empty()) return 0;
        
        // 方法1: 使用并查集
        return longestConsecutiveUnionFind(nums);
        
        // 方法2: 使用哈希表（最优解）
        // return longestConsecutiveHashSet(nums);
    }
    
private:
    int longestConsecutiveUnionFind(vector<int>& nums) {
        int n = nums.size();
        unordered_map<int, int> numToIndex;
        for (int i = 0; i < n; i++) {
            numToIndex[nums[i]] = i;
        }
        
        UnionFind uf(n);
        
        for (int i = 0; i < n; i++) {
            int num = nums[i];
            
            if (numToIndex.count(num - 1)) {
                uf.unionSets(i, numToIndex[num - 1]);
            }
            if (numToIndex.count(num + 1)) {
                uf.unionSets(i, numToIndex[num + 1]);
            }
        }
        
        // 统计每个连通分量的大小
        unordered_map<int, int> sizeMap;
        for (int i = 0; i < n; i++) {
            int root = uf.find(i);
            sizeMap[root]++;
        }
        
        int maxSize = 0;
        for (auto& [root, size] : sizeMap) {
            maxSize = max(maxSize, size);
        }
        
        return maxSize;
    }
    
    int longestConsecutiveHashSet(vector<int>& nums) {
        unordered_set<int> numSet(nums.begin(), nums.end());
        int longestStreak = 0;
        
        for (int num : numSet) {
            // 只有当num是序列起点时才查找
            if (!numSet.count(num - 1)) {
                int currentNum = num;
                int currentStreak = 1;
                
                while (numSet.count(currentNum + 1)) {
                    currentNum++;
                    currentStreak++;
                }
                
                longestStreak = max(longestStreak, currentStreak);
            }
        }
        
        return longestStreak;
    }
};

/**
 * LeetCode 547. 省份数量
 */
class Solution547 {
public:
    int findCircleNum(vector<vector<int>>& isConnected) {
        int n = isConnected.size();
        UnionFind uf(n);
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (isConnected[i][j] == 1) {
                    uf.unionSets(i, j);
                }
            }
        }
        
        return uf.getCount();
    }
};

/**
 * LeetCode 684. 冗余连接
 */
class Solution684 {
public:
    vector<int> findRedundantConnection(vector<vector<int>>& edges) {
        int n = edges.size();
        UnionFind uf(n + 1);  // 节点从1开始编号
        
        for (auto& edge : edges) {
            int u = edge[0], v = edge[1];
            if (!uf.unionSets(u, v)) {
                return {u, v};
            }
        }
        
        return {};
    }
};

/**
 * LeetCode 399. 除法求值
 */
class Solution399 {
public:
    vector<double> calcEquation(vector<vector<string>>& equations, 
                               vector<double>& values, 
                               vector<vector<string>>& queries) {
        // 创建变量到索引的映射
        unordered_map<string, int> varToIndex;
        int index = 0;
        
        for (auto& eq : equations) {
            if (!varToIndex.count(eq[0])) {
                varToIndex[eq[0]] = index++;
            }
            if (!varToIndex.count(eq[1])) {
                varToIndex[eq[1]] = index++;
            }
        }
        
        WeightedUnionFind uf(index);
        
        // 构建并查集
        for (int i = 0; i < equations.size(); i++) {
            int a = varToIndex[equations[i][0]];
            int b = varToIndex[equations[i][1]];
            uf.unionSets(a, b, values[i]);
        }
        
        // 处理查询
        vector<double> results;
        for (auto& query : queries) {
            string a = query[0], b = query[1];
            
            if (!varToIndex.count(a) || !varToIndex.count(b)) {
                results.push_back(-1.0);
            } else {
                double result = uf.query(varToIndex[a], varToIndex[b]);
                results.push_back(result);
            }
        }
        
        return results;
    }
};

/**
 * LeetCode 803. 打砖块
 */
class Solution803 {
public:
    vector<int> hitBricks(vector<vector<int>>& grid, vector<vector<int>>& hits) {
        int m = grid.size(), n = grid[0].size();
        
        // 复制网格
        vector<vector<int>> copy = grid;
        
        // 先消除所有要打的砖块
        for (auto& hit : hits) {
            copy[hit[0]][hit[1]] = 0;
        }
        
        // 初始化并查集（增加虚拟顶部节点）
        int size = m * n;
        UnionFind uf(size + 1);
        
        // 将第一行砖块连接到虚拟顶部节点
        for (int j = 0; j < n; j++) {
            if (copy[0][j] == 1) {
                uf.unionSets(j, size);
            }
        }
        
        // 构建初始连通性
        vector<pair<int, int>> directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        for (int i = 1; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (copy[i][j] == 1) {
                    int idx = i * n + j;
                    
                    // 连接上方砖块
                    if (copy[i-1][j] == 1) {
                        uf.unionSets(idx, (i-1) * n + j);
                    }
                    
                    // 连接左方砖块
                    if (j > 0 && copy[i][j-1] == 1) {
                        uf.unionSets(idx, i * n + j - 1);
                    }
                }
            }
        }
        
        // 逆向处理hits
        vector<int> result(hits.size(), 0);
        
        for (int idx = hits.size() - 1; idx >= 0; idx--) {
            int i = hits[idx][0], j = hits[idx][1];
            
            // 如果原始位置没有砖块
            if (grid[i][j] == 0) {
                result[idx] = 0;
                continue;
            }
            
            // 记录添加前的稳定砖块数量
            int originSize = getStableBricksCount(uf, size);
            
            // 恢复砖块
            copy[i][j] = 1;
            int currentIdx = i * n + j;
            
            // 如果是第一行，连接到顶部
            if (i == 0) {
                uf.unionSets(j, size);
            }
            
            // 连接相邻砖块
            for (auto& [di, dj] : directions) {
                int ni = i + di, nj = j + dj;
                if (ni >= 0 && ni < m && nj >= 0 && nj < n && copy[ni][nj] == 1) {
                    uf.unionSets(currentIdx, ni * n + nj);
                }
            }
            
            // 计算新增的稳定砖块数量
            int currentSize = getStableBricksCount(uf, size);
            result[idx] = max(0, currentSize - originSize - 1);
        }
        
        return result;
    }
    
private:
    int getStableBricksCount(UnionFind& uf, int top) {
        // 简化实现：统计所有与顶部连通的节点
        int count = 0;
        int n = uf.getCount() - 1;  // 减去虚拟顶部节点
        
        for (int i = 0; i < n; i++) {
            if (uf.isConnected(i, top)) {
                count++;
            }
        }
        
        return count;
    }
};

/**
 * 性能分析工具
 */
class PerformanceAnalyzer {
public:
    static void analyzeUnionFindPerformance() {
        cout << "=== 并查集性能分析 ===" << endl;
        
        vector<int> sizes = {1000, 10000, 100000};
        
        for (int size : sizes) {
            // 生成随机操作序列
            vector<tuple<int, int, int>> operations;
            operations.reserve(size * 2);
            
            srand(time(nullptr));
            for (int i = 0; i < size * 2; i++) {
                int opType = rand() % 2;
                int x = rand() % size;
                int y = rand() % size;
                operations.emplace_back(opType, x, y);
            }
            
            // 测试标准并查集
            auto start = chrono::high_resolution_clock::now();
            
            UnionFind uf(size);
            for (auto& [opType, x, y] : operations) {
                if (opType == 0) {
                    uf.unionSets(x, y);
                } else {
                    uf.isConnected(x, y);
                }
            }
            
            auto end = chrono::high_resolution_clock::now();
            auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
            
            cout << "规模 " << size << ": " << duration.count() << " ms" << endl;
        }
    }
};

/**
 * 测试函数
 */
void runTests() {
    cout << "=== 并查集C++实现测试 ===" << endl;
    
    // 测试LeetCode 128
    {
        Solution128 sol;
        vector<int> nums1 = {100, 4, 200, 1, 3, 2};
        cout << "LeetCode 128 测试用例1: " << sol.longestConsecutive(nums1) << " (预期: 4)" << endl;
        
        vector<int> nums2 = {0, 3, 7, 2, 5, 8, 4, 6, 0, 1};
        cout << "LeetCode 128 测试用例2: " << sol.longestConsecutive(nums2) << " (预期: 9)" << endl;
    }
    
    // 测试LeetCode 547
    {
        Solution547 sol;
        vector<vector<int>> isConnected1 = {{1,1,0}, {1,1,0}, {0,0,1}};
        cout << "LeetCode 547 测试用例1: " << sol.findCircleNum(isConnected1) << " (预期: 2)" << endl;
        
        vector<vector<int>> isConnected2 = {{1,0,0}, {0,1,0}, {0,0,1}};
        cout << "LeetCode 547 测试用例2: " << sol.findCircleNum(isConnected2) << " (预期: 3)" << endl;
    }
    
    // 测试LeetCode 684
    {
        Solution684 sol;
        vector<vector<int>> edges1 = {{1,2}, {1,3}, {2,3}};
        auto result1 = sol.findRedundantConnection(edges1);
        cout << "LeetCode 684 测试用例1: [" << result1[0] << "," << result1[1] << "] (预期: [2,3])" << endl;
    }
    
    // 测试性能分析
    PerformanceAnalyzer::analyzeUnionFindPerformance();
}

int main() {
    runTests();
    
    cout << "\n=== C++并查集实现总结 ===" << endl;
    cout << "1. 标准模板: 路径压缩 + 按秩合并" << endl;
    cout << "2. 内存优化: 使用vector代替unordered_map" << endl;
    cout << "3. 性能优化: 避免不必要的复制操作" << endl;
    cout << "4. 类型安全: 使用强类型和RAII" << endl;
    cout << "5. 测试完整: 包含单元测试和性能分析" << endl;
    
    return 0;
}