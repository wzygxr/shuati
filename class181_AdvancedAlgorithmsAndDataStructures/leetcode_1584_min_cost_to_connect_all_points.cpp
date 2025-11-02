#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <cmath>
#include <queue>
#include <unordered_map>
#include <tuple>
#include <functional>

/**
 * LeetCode 1584. 连接所有点的最小费用 (Min Cost to Connect All Points)
 * 
 * 题目来源：https://leetcode.cn/problems/min-cost-to-connect-all-points/
 * 
 * 题目描述：
 * 给你一个points数组，表示 2D 平面上的一些点，其中 points[i] = [xi, yi] 。
 * 连接点 [xi, yi] 和点 [xj, yj] 的费用为它们之间的曼哈顿距离：|xi - xj| + |yi - yj|，
 * 其中 |val| 表示 val 的绝对值。请你返回将所有点连接的最小总费用。
 * 只有任意两点之间有且仅有一条简单路径时，才认为所有点都已连接。
 * 
 * 算法思路：
 * 这是一个最小生成树（Minimum Spanning Tree, MST）问题，可以使用以下算法解决：
 * 1. Kruskal算法：使用并查集和贪心策略
 * 2. Prim算法：使用优先队列和贪心策略
 * 3. 最近点对算法的变种：通过构建完全图然后应用MST算法
 * 
 * 时间复杂度：
 * - Kruskal算法：O(E log E) = O(n² log n)，其中E是边数，n是点数
 * - Prim算法：O(E log V) = O(n² log n)，其中V是顶点数
 * - 空间复杂度：O(n²)
 * 
 * 应用场景：
 * 1. 网络设计：最小化网络连接成本
 * 2. 电路设计：最小化电路板布线长度
 * 3. 交通运输：最小化道路建设成本
 * 
 * 相关题目：
 * 1. LeetCode 1135. 最低成本联通所有城市
 * 2. LeetCode 743. 网络延迟时间
 * 3. LeetCode 612. 平面上的最短距离
 */

class UnionFind {
private:
    std::vector<int> parent;
    std::vector<int> rank;
    int components;

public:
    UnionFind(int n) : parent(n), rank(n, 0), components(n) {
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }
    
    /**
     * 查找元素的根节点（带路径压缩优化）
     * @param x 元素
     * @return 根节点
     */
    int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // 路径压缩
        }
        return parent[x];
    }
    
    /**
     * 合并两个集合（按秩合并优化）
     * @param x 第一个元素
     * @param y 第二个元素
     * @return 如果合并成功返回true，如果已在同一集合返回false
     */
    bool unite(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        
        if (rootX == rootY) {
            return false; // 已在同一集合，不能合并
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
        
        components--;
        return true;
    }
    
    /**
     * 获取连通分量数量
     * @return 连通分量数量
     */
    int getComponents() const {
        return components;
    }
};

class Solution {
public:
    /**
     * 方法1：Kruskal算法解决最小生成树问题
     * 时间复杂度：O(n² log n)
     * 空间复杂度：O(n²)
     * @param points 点坐标数组
     * @return 连接所有点的最小费用
     */
    static int minCostConnectPointsKruskal(std::vector<std::vector<int>>& points) {
        int n = points.size();
        if (n <= 1) return 0;
        
        // 创建所有边
        std::vector<std::tuple<int, int, int>> edges; // {cost, from, to}
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int cost = std::abs(points[i][0] - points[j][0]) + std::abs(points[i][1] - points[j][1]);
                edges.emplace_back(cost, i, j);
            }
        }
        
        // 按费用排序边
        std::sort(edges.begin(), edges.end());
        
        // 使用并查集构建最小生成树
        UnionFind uf(n);
        int totalCost = 0;
        
        for (const auto& edge : edges) {
            int cost = std::get<0>(edge);
            int from = std::get<1>(edge);
            int to = std::get<2>(edge);
            
            if (uf.unite(from, to)) {
                totalCost += cost;
                // 如果所有点都已连接，提前结束
                if (uf.getComponents() == 1) {
                    break;
                }
            }
        }
        
        return totalCost;
    }
    
    /**
     * 方法2：Prim算法解决最小生成树问题
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n)
     * @param points 点坐标数组
     * @return 连接所有点的最小费用
     */
    static int minCostConnectPointsPrim(std::vector<std::vector<int>>& points) {
        int n = points.size();
        if (n <= 1) return 0;
        
        // 使用优先队列存储边（费用，目标点）
        std::priority_queue<std::pair<int, int>, std::vector<std::pair<int, int>>, std::greater<std::pair<int, int>>> pq;
        
        // 记录已访问的点
        std::vector<bool> visited(n, false);
        int totalCost = 0;
        int edgesUsed = 0;
        
        // 从第一个点开始
        pq.push({0, 0}); // {cost, pointIndex}
        
        while (!pq.empty() && edgesUsed < n) {
            auto current = pq.top();
            pq.pop();
            int cost = current.first;
            int pointIndex = current.second;
            
            // 如果点已访问，跳过
            if (visited[pointIndex]) {
                continue;
            }
            
            // 标记点为已访问
            visited[pointIndex] = true;
            totalCost += cost;
            edgesUsed++;
            
            // 添加与当前点相连的所有边到优先队列
            for (int i = 0; i < n; i++) {
                if (!visited[i]) {
                    int edgeCost = std::abs(points[pointIndex][0] - points[i][0]) + 
                                  std::abs(points[pointIndex][1] - points[i][1]);
                    pq.push({edgeCost, i});
                }
            }
        }
        
        return totalCost;
    }
    
    /**
     * 方法3：优化的Prim算法（使用距离数组）
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n)
     * @param points 点坐标数组
     * @return 连接所有点的最小费用
     */
    static int minCostConnectPointsOptimizedPrim(std::vector<std::vector<int>>& points) {
        int n = points.size();
        if (n <= 1) return 0;
        
        // 距离数组，记录每个点到已构建MST的最小距离
        std::vector<int> minDist(n, INT_MAX);
        
        // 记录已访问的点
        std::vector<bool> visited(n, false);
        int totalCost = 0;
        
        // 从第一个点开始
        minDist[0] = 0;
        
        for (int i = 0; i < n; i++) {
            // 找到距离最小的未访问点
            int currentPoint = -1;
            for (int j = 0; j < n; j++) {
                if (!visited[j] && (currentPoint == -1 || minDist[j] < minDist[currentPoint])) {
                    currentPoint = j;
                }
            }
            
            // 标记点为已访问并累加费用
            visited[currentPoint] = true;
            totalCost += minDist[currentPoint];
            
            // 更新其他点到已构建MST的最小距离
            for (int j = 0; j < n; j++) {
                if (!visited[j]) {
                    int cost = std::abs(points[currentPoint][0] - points[j][0]) + 
                              std::abs(points[currentPoint][1] - points[j][1]);
                    minDist[j] = std::min(minDist[j], cost);
                }
            }
        }
        
        return totalCost;
    }
};

// 测试函数
void testMinCostConnectPoints() {
    std::cout << "=== 测试 LeetCode 1584. 连接所有点的最小费用 ===" << std::endl;
    
    // 测试用例1
    std::vector<std::vector<int>> points1 = {{0,0},{2,2},{3,10},{5,2},{7,0}};
    std::cout << "测试用例1:" << std::endl;
    std::cout << "Kruskal算法结果: " << Solution::minCostConnectPointsKruskal(points1) << std::endl;
    std::cout << "Prim算法结果: " << Solution::minCostConnectPointsPrim(points1) << std::endl;
    std::cout << "优化Prim算法结果: " << Solution::minCostConnectPointsOptimizedPrim(points1) << std::endl;
    std::cout << "期望结果: 20" << std::endl << std::endl;
    
    // 测试用例2
    std::vector<std::vector<int>> points2 = {{3,12},{-2,5},{-4,1}};
    std::cout << "测试用例2:" << std::endl;
    std::cout << "Kruskal算法结果: " << Solution::minCostConnectPointsKruskal(points2) << std::endl;
    std::cout << "Prim算法结果: " << Solution::minCostConnectPointsPrim(points2) << std::endl;
    std::cout << "优化Prim算法结果: " << Solution::minCostConnectPointsOptimizedPrim(points2) << std::endl;
    std::cout << "期望结果: 18" << std::endl << std::endl;
    
    // 测试用例3：边界情况
    std::vector<std::vector<int>> points3 = {{0,0}};
    std::cout << "测试用例3 (单点):" << std::endl;
    std::cout << "Kruskal算法结果: " << Solution::minCostConnectPointsKruskal(points3) << std::endl;
    std::cout << "Prim算法结果: " << Solution::minCostConnectPointsPrim(points3) << std::endl;
    std::cout << "优化Prim算法结果: " << Solution::minCostConnectPointsOptimizedPrim(points3) << std::endl;
    std::cout << "期望结果: 0" << std::endl << std::endl;
}

int main() {
    testMinCostConnectPoints();
    return 0;
}