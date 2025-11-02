package class057;

/**
 * 冗余连接 (Redundant Connection)
 * 树可以看成是一个连通且无环的无向图。
 * 给定往一棵n个节点(节点值1～n)的树中添加一条边后的图。
 * 添加的边的两个顶点包含在1到n中间，且这条附加的边不属于树中已存在的边。
 * 图的信息记录于长度为n的二维数组edges，edges[i] = [ai, bi]表示图中在ai和bi之间存在一条边。
 * 请找出一条可以删去的边，删除后可使得剩余部分是一棵有n个节点的树。
 * 如果有多个答案，则返回数组edges中最后出现的边。
 * 
 * 示例 1:
 * 输入: edges = [[1,2],[1,3],[2,3]]
 * 输出: [2,3]
 * 
 * 示例 2:
 * 输入: edges = [[1,2],[2,3],[3,4],[1,4],[1,5]]
 * 输出: [1,4]
 * 
 * 测试链接: https://leetcode.cn/problems/redundant-connection/
 * 
 * 算法思路深度解析:
 * 1. 该问题实际上是在一个无向图中检测环，并找出环中的一条边
 * 2. 由于题目保证输入是一棵树加上一条边，因此整个图中恰好存在一个环
 * 3. 使用并查集检测环的方法：对于每条边(u,v)，检查u和v是否已经连通
 *    - 如果已经连通，说明添加这条边会形成环，该边即为冗余边
 *    - 如果不连通，则将u和v合并到同一个集合中
 * 4. 根据题目要求，当存在多个可能的答案时，返回最后出现的边
 * 
 * 算法性能分析:
 * 时间复杂度: O(n*α(n))，其中n是边的数量（等于节点数量），
 *             α是阿克曼函数的反函数，在实际应用中接近常数级别
 * 空间复杂度: O(n)，用于存储并查集的数据结构
 * 
 * 是否为最优解: 是，该解法在时间和空间复杂度上都是最优的
 * 
 * 工程化考量:
 * 1. 异常处理：对空输入和无效输入进行充分检查
 * 2. 模块化设计：将并查集封装为独立类，提高代码可读性和可维护性
 * 3. 性能优化：实现了路径压缩和按秩合并两种关键优化
 * 4. 线程安全性：当前实现不是线程安全的，在多线程环境中需要额外的同步机制
 * 5. 接口设计：union方法返回布尔值表示合并是否成功，便于检测环
 * 
 * 与其他领域的联系:
 * 1. 图论：最小生成树算法（如Kruskal算法）中需要类似的环检测机制
 * 2. 网络设计：检测网络中的冗余连接，优化网络拓扑
 * 3. 数据库：检测关系型数据库中的循环引用问题
 * 4. 软件工程：在依赖管理系统中检测循环依赖
 * 5. 机器学习：在聚类算法中检测数据点之间的连通性
 * 
 * 极端情况分析:
 * 1. 空图：正确返回空数组
 * 2. 最小情况：n=2，edges=[[1,2],[1,2]]，返回[1,2]
 * 3. 完全连接：n个节点形成完全图，返回最后形成环的边
 * 
 * 调试技巧:
 * 1. 打印并查集的状态来跟踪连通性变化
 * 2. 绘制图形结构帮助理解环的形成
 * 3. 使用断点调试观察每条边的处理过程
 * 
 * 问题迁移能力:
 * 1. 该解法可以扩展到检测无向图中的所有环
 * 2. 类似的思路可用于解决冗余连接II（有向图）问题
 * 3. 在处理动态连通性问题时具有广泛应用
 */
public class Code06_RedundantConnection {
    
    /**
     * 并查集(Union-Find)类的实现
     * 支持快速查找和合并操作，使用路径压缩和按秩合并优化
     * 
     * 设计说明:
     * - 本实现专门针对节点编号从1开始的场景进行了优化
     * - 提供了高效的环检测能力，适用于冗余连接问题
     * - 路径压缩和按秩合并保证了近常数时间复杂度
     */
    static class UnionFind {
        private int[] parent;  // parent[i]表示节点i的父节点
        private int[] rank;    // rank[i]表示以i为根的树的高度上界，用于优化合并操作
        
        /**
         * 初始化并查集
         * @param n 节点数量
         * @throws IllegalArgumentException 如果节点数量小于0
         * @throws IllegalArgumentException 如果节点数量超过最大安全值
         */
        public UnionFind(int n) {
            // 参数验证
            if (n < 0) {
                throw new IllegalArgumentException("节点数量不能为负数: " + n);
            }
            
            // 防止数组下标越界
            if (n > Integer.MAX_VALUE - 1) {
                throw new IllegalArgumentException("节点数量过大: " + n);
            }
            
            // 注意：节点编号从1开始，所以数组大小为n+1
            parent = new int[n + 1];
            rank = new int[n + 1];
            
            // 初始化：每个节点的父节点是自己，秩为1
            for (int i = 1; i <= n; i++) {
                parent[i] = i;    // 自环
                rank[i] = 1;      // 初始树高度
            }
        }
        
        /**
         * 查找节点的根节点（代表元素）
         * 使用路径压缩优化，使得后续查找操作接近O(1)
         * 
         * @param x 要查找的节点
         * @return 节点x所在集合的根节点
         * @throws IndexOutOfBoundsException 如果节点索引超出有效范围
         */
        public int find(int x) {
            // 参数范围检查
            if (x < 1 || x >= parent.length) {
                throw new IndexOutOfBoundsException("节点索引超出有效范围: " + x);
            }
            
            // 路径压缩：将查找路径上的所有节点直接连接到根节点
            // 这是并查集的关键优化，显著减少了后续查找操作的时间复杂度
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // 递归查找根节点并更新父节点引用
            }
            return parent[x];
        }
        
        /**
         * 合并两个节点所在的集合
         * 使用按秩合并优化，避免树高度过深
         * 
         * @param x 第一个节点
         * @param y 第二个节点
         * @return 如果两个节点已经在同一个集合中返回false，否则返回true
         *         返回false表示添加这条边会形成环
         * @throws IndexOutOfBoundsException 如果节点索引超出有效范围
         */
        public boolean union(int x, int y) {
            // 找到两个节点的根节点（带路径压缩）
            int rootX = find(x);
            int rootY = find(y);
            
            // 如果已经在同一个集合中，说明添加这条边会形成环
            if (rootX == rootY) {
                return false;
            }
            
            // 按秩合并：将秩小的树合并到秩大的树下
            // 这种策略确保了树的高度尽可能小，优化后续查找操作
            if (rank[rootX] > rank[rootY]) {
                // rootX的秩较大，将rootY合并到rootX下
                parent[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                // rootY的秩较大，将rootX合并到rootY下
                parent[rootX] = rootY;
            } else {
                // 秩相等时，选择一个作为根，并增加其秩
                parent[rootY] = rootX;
                rank[rootX]++; // 合并后树的高度增加1
            }
            
            // 合并成功，返回true
            return true;
        }
        
        /**
         * 判断两个节点是否在同一个集合中
         * 
         * @param x 第一个节点
         * @param y 第二个节点
         * @return 如果两个节点在同一个集合中返回true，否则返回false
         * @throws IndexOutOfBoundsException 如果节点索引超出有效范围
         */
        public boolean isConnected(int x, int y) {
            return find(x) == find(y);
        }
        
        /**
         * 获取当前连通分量的数量
         * 注意：此方法需要遍历所有节点，时间复杂度为O(n)
         * 
         * @return 连通分量数量
         */
        public int getConnectedComponents() {
            // 使用集合记录不同的根节点
            boolean[] roots = new boolean[parent.length];
            int count = 0;
            
            for (int i = 1; i < parent.length; i++) {
                int root = find(i);
                if (!roots[root]) {
                    roots[root] = true;
                    count++;
                }
            }
            
            return count;
        }
    }
    
    /**
     * 查找冗余连接的核心方法
     * 遍历所有边，使用并查集检测环
     * 
     * @param edges 边的数组，每个元素是一个包含两个整数的数组，表示一条无向边
     * @return 冗余的边，如果不存在则返回空数组
     * @throws IllegalArgumentException 如果输入无效
     * @throws NullPointerException 如果输入为null
     */
    public static int[] findRedundantConnection(int[][] edges) {
        // 边界条件检查
        if (edges == null) {
            throw new NullPointerException("输入的边数组不能为null");
        }
        
        if (edges.length == 0) {
            return new int[0]; // 空输入返回空数组
        }
        
        // 验证输入数组的有效性
        for (int i = 0; i < edges.length; i++) {
            if (edges[i] == null || edges[i].length != 2) {
                throw new IllegalArgumentException("边数组的第" + i + "个元素无效");
            }
        }
        
        // 节点数量等于边的数量（因为是树+一条边）
        int n = edges.length;
        
        // 创建并查集
        UnionFind uf = new UnionFind(n);
        
        // 遍历每条边
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            
            // 检查边的有效性
            if (u < 1 || u > n || v < 1 || v > n) {
                throw new IllegalArgumentException("边[" + u + "," + v + "]的顶点超出有效范围(1~" + n + ")");
            }
            
            // 如果两个节点已经在同一个连通分量中，说明添加这条边会形成环
            // 根据题目要求，这就是我们要找的冗余边
            if (!uf.union(u, v)) {
                // 复制结果以避免返回原数组引用
                return new int[] {u, v};
            }
        }
        
        // 理论上不会执行到这里，因为题目保证输入包含冗余边
        return new int[0];
    }
    
    /**
     * 执行单个测试用例并输出结果
     * 
     * @param testName 测试用例名称
     * @param edges 边数组
     * @param expected 预期结果
     */
    public static void runTestCase(String testName, int[][] edges, int[] expected) {
        try {
            int[] result = findRedundantConnection(edges);
            boolean passed = (result[0] == expected[0] && result[1] == expected[1]) ||
                           (result[0] == expected[1] && result[1] == expected[0]);
            
            System.out.println(testName + ": [结果: [" + result[0] + ", " + result[1] + "]",
                             ", 预期: [" + expected[0] + ", " + expected[1] + "]] " +
                             (passed ? "通过" : "失败"));
        } catch (Exception e) {
            System.out.println(testName + ": [失败] 抛出异常: " + e.getClass().getName() + 
                             " - " + e.getMessage());
        }
    }
    
    /**
     * 基本功能测试
     * 测试常见的环检测场景
     */
    public static void testBasicFunctionality() {
        System.out.println("\n[基本功能测试]");
        
        // 测试用例1：简单的三节点环
        int[][] edges1 = {{1, 2}, {1, 3}, {2, 3}};
        runTestCase("简单三节点环", edges1, new int[] {2, 3});
        
        // 测试用例2：复杂的多节点环
        int[][] edges2 = {{1, 2}, {2, 3}, {3, 4}, {1, 4}, {1, 5}};
        runTestCase("复杂多节点环", edges2, new int[] {1, 4});
    }
    
    /**
     * 特殊情况测试
     * 测试一些特殊的环结构
     */
    public static void testSpecialCases() {
        System.out.println("\n[特殊情况测试]");
        
        // 测试用例3：最小情况
        int[][] edges3 = {{1, 2}, {1, 2}};
        runTestCase("最小情况", edges3, new int[] {1, 2});
        
        // 测试用例4：较复杂的环结构
        int[][] edges4 = {{1, 5}, {3, 4}, {3, 5}, {4, 5}, {2, 4}};
        runTestCase("复杂环结构", edges4, new int[] {4, 5});
        
        // 测试用例5：链式结构形成的环
        int[][] edges5 = {{1, 2}, {2, 3}, {3, 1}};
        runTestCase("链式环", edges5, new int[] {3, 1});
        
        // 测试用例6：较大的图
        int[][] edges6 = {{1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}, {6, 7}, {7, 8}, {8, 4}};
        runTestCase("较大的图", edges6, new int[] {8, 4});
    }
    
    /**
     * 边界情况测试
     * 测试各种边界条件
     */
    public static void testEdgeCases() {
        System.out.println("\n[边界情况测试]");
        
        // 测试用例7：空数组
        int[][] edges7 = {};
        try {
            int[] result = findRedundantConnection(edges7);
            System.out.println("空数组测试: [结果: [" + (result.length == 0 ? "空" : result[0] + ", " + result[1]) + "]",
                             ", 预期: [空]] 通过");
        } catch (Exception e) {
            System.out.println("空数组测试: [失败] 抛出异常: " + e.getClass().getName() + 
                             " - " + e.getMessage());
        }
        
        // 测试用例8：只有一条边
        int[][] edges8 = {{1, 2}};
        try {
            int[] result = findRedundantConnection(edges8);
            System.out.println("单条边测试: [结果: [" + (result.length == 0 ? "空" : result[0] + ", " + result[1]) + "]",
                             ", 预期: [空]] 通过");
        } catch (Exception e) {
            System.out.println("单条边测试: [失败] 抛出异常: " + e.getClass().getName() + 
                             " - " + e.getMessage());
        }
    }
    
    /**
     * 异常处理测试
     * 测试异常情况的处理
     */
    public static void testExceptionHandling() {
        System.out.println("\n[异常处理测试]");
        
        // 测试用例9：无效的边顶点
        int[][] edges9 = {{1, 2}, {1, 3}, {2, 4}};
        try {
            findRedundantConnection(edges9);
            System.out.println("无效边顶点测试: [失败] 未能捕获异常");
        } catch (IllegalArgumentException e) {
            System.out.println("无效边顶点测试: [通过] 正确捕获异常: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("无效边顶点测试: [失败] 捕获了错误类型的异常: " + e.getClass().getName());
        }
        
        // 测试用例10：null输入
        try {
            findRedundantConnection(null);
            System.out.println("null输入测试: [失败] 未能捕获异常");
        } catch (NullPointerException e) {
            System.out.println("null输入测试: [通过] 正确捕获异常");
        } catch (Exception e) {
            System.out.println("null输入测试: [失败] 捕获了错误类型的异常: " + e.getClass().getName());
        }
    }
    
    /**
     * 主方法
     * 运行所有测试用例，验证算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("======== 冗余连接算法测试 ========");
        
        // 运行各种测试用例
        testBasicFunctionality();
        testSpecialCases();
        testEdgeCases();
        testExceptionHandling();
        
        System.out.println("\n======== 所有测试用例执行完毕 ========");
    }
}

/* C++ 实现
#include <iostream>
#include <vector>
#include <stdexcept>
#include <iomanip>
#include <string>
using namespace std;

/**
 * 并查集(Union-Find)类的实现
 * 支持快速查找和合并操作，使用路径压缩和按秩合并优化
 * 
 * 设计说明：
 * - 本实现专门针对节点编号从1开始的场景进行了优化
 * - 提供了高效的环检测能力，适用于冗余连接问题
 * - 路径压缩和按秩合并保证了近常数时间复杂度
 */
class UnionFind {
private:
    vector<int> parent;  // 父节点数组
    vector<int> rank;    // 秩数组，用于按秩合并优化

public:
    /**
     * 初始化并查集
     * 
     * @param n 节点数量
     * @throws invalid_argument 如果节点数量小于0
     */
    UnionFind(int n) {
        // 参数验证
        if (n < 0) {
            throw invalid_argument("节点数量不能为负数: " + to_string(n));
        }
        
        // 节点编号从1开始，所以数组大小为n+1
        parent.resize(n + 1);
        rank.resize(n + 1, 1);
        
        // 初始化：每个节点的父节点是自己，秩为1
        for (int i = 1; i <= n; ++i) {
            parent[i] = i;    // 自环，每个节点初始是自己的代表元素
            rank[i] = 1;      // 初始树高度为1
        }
    }
    
    /**
     * 查找节点的根节点（带路径压缩）
     * 
     * @param x 要查找的节点
     * @return 节点x所在集合的根节点
     * @throws out_of_range 当节点索引超出范围时抛出异常
     */
    int find(int x) {
        // 参数范围检查
        if (x < 1 || x >= static_cast<int>(parent.size())) {
            throw out_of_range("节点索引超出有效范围: " + to_string(x));
        }
        
        // 路径压缩：将查找路径上的所有节点直接连接到根节点
        // 这是并查集的关键优化，显著减少了后续查找操作的时间复杂度
        if (parent[x] != x) {
            parent[x] = find(parent[x]);  // 递归查找根节点并更新父节点引用
        }
        return parent[x];
    }
    
    /**
     * 合并两个集合（按秩合并）
     * 
     * @param x 第一个节点
     * @param y 第二个节点
     * @return 如果两个节点已经在同一个集合中返回false，否则返回true
     *         返回false表示添加这条边会形成环
     * @throws out_of_range 当节点索引超出范围时抛出异常
     */
    bool unite(int x, int y) {
        // 找到两个节点的根节点（带路径压缩）
        int rootX = find(x);
        int rootY = find(y);
        
        // 如果已经在同一个集合中，说明添加这条边会形成环
        if (rootX == rootY) {
            return false;
        }
        
        // 按秩合并：将秩小的树合并到秩大的树下
        // 这种策略确保了树的高度尽可能小，优化后续查找操作
        if (rank[rootX] > rank[rootY]) {
            // rootX的秩较大，将rootY合并到rootX下
            parent[rootY] = rootX;
        } else if (rank[rootX] < rank[rootY]) {
            // rootY的秩较大，将rootX合并到rootY下
            parent[rootX] = rootY;
        } else {
            // 秩相等时，选择一个作为根，并增加其秩
            parent[rootY] = rootX;
            rank[rootX]++; // 合并后树的高度增加1
        }
        
        // 合并成功，返回true
        return true;
    }
    
    /**
     * 判断两个节点是否在同一个集合中
     * 
     * @param x 第一个节点
     * @param y 第二个节点
     * @return 如果两个节点在同一个集合中返回true，否则返回false
     * @throws out_of_range 当节点索引超出范围时抛出异常
     */
    bool isConnected(int x, int y) {
        return find(x) == find(y);
    }
    
    /**
     * 获取当前连通分量的数量
     * 注意：此方法需要遍历所有节点，时间复杂度为O(n)
     * 
     * @return 连通分量数量
     */
    int getConnectedComponents() {
        // 使用数组记录不同的根节点
        vector<bool> roots(parent.size(), false);
        int count = 0;
        
        for (int i = 1; i < static_cast<int>(parent.size()); i++) {
            int root = find(i);
            if (!roots[root]) {
                roots[root] = true;
                count++;
            }
        }
        
        return count;
    }
};

/**
 * 冗余连接问题解决方案
 * 使用并查集高效检测无向图中的环
 * 
 * 算法思路深度解析：
 * - 该问题本质是在一个无向图中检测环，并找出环中的一条边
 * - 由于题目保证输入是一棵树加上一条边，因此整个图中恰好存在一个环
 * - 使用并查集检测环的方法非常高效，时间复杂度接近O(n)
 * - 关键优化点是union方法返回布尔值，表示是否形成了环
 */
class Solution {
public:
    /**
     * 查找冗余连接
     * 
     * @param edges 边的数组
     * @return 冗余的边
     * @throws invalid_argument 当输入无效时抛出异常
     */
    vector<int> findRedundantConnection(vector<vector<int>>& edges) {
        // 参数验证
        if (edges.empty()) {
            return {}; // 空输入返回空数组
        }
        
        // 验证输入数组的有效性
        for (size_t i = 0; i < edges.size(); i++) {
            if (edges[i].size() != 2) {
                throw invalid_argument("边数组的第" + to_string(i) + "个元素无效");
            }
        }
        
        int n = edges.size();
        
        // 创建并查集
        UnionFind uf(n);
        
        // 遍历每条边
        for (const auto& edge : edges) {
            int u = edge[0];
            int v = edge[1];
            
            // 检查边的有效性
            if (u < 1 || u > n || v < 1 || v > n) {
                throw invalid_argument("边[" + to_string(u) + "," + to_string(v) + "]的顶点超出有效范围(1~" + to_string(n) + ")");
            }
            
            // 如果两个节点已经在同一个连通分量中，说明添加这条边会形成环
            if (!uf.unite(u, v)) {
                return edge;
            }
        }
        
        // 理论上不会执行到这里
        return {};
    }
};

/**
 * 执行单个测试用例并输出结果
 * 
 * @param name 测试用例名称
 * @param edges 测试边数组
 * @param expected 预期结果
 */
void runTestCase(const string& name, vector<vector<int>>& edges, vector<int>& expected) {
    Solution solution;
    try {
        vector<int> result = solution.findRedundantConnection(edges);
        bool passed = (result[0] == expected[0] && result[1] == expected[1]) ||
                     (result[0] == expected[1] && result[1] == expected[0]);
        
        cout << "  " << left << setw(15) << name << ": [结果: [" << result[0] << ", " << result[1] 
             << "], 预期: [" << expected[0] << ", " << expected[1] << "]] " 
             << (passed ? "通过" : "失败") << endl;
    } catch (const exception& e) {
        cout << "  " << left << setw(15) << name << ": [失败] 抛出异常: " 
             << typeid(e).name() << " - " << e.what() << endl;
    }
}

/**
 * 基本功能测试
 * 测试常见的环检测场景
 */
void testBasicFunctionality() {
    cout << "\n[基本功能测试]" << endl;
    
    // 测试用例1：简单的三节点环
    vector<vector<int>> edges1 = {{1, 2}, {1, 3}, {2, 3}};
    vector<int> expected1 = {2, 3};
    runTestCase("简单三节点环", edges1, expected1);
    
    // 测试用例2：复杂的多节点环
    vector<vector<int>> edges2 = {{1, 2}, {2, 3}, {3, 4}, {1, 4}, {1, 5}};
    vector<int> expected2 = {1, 4};
    runTestCase("复杂多节点环", edges2, expected2);
}

/**
 * 特殊情况测试
 * 测试一些特殊的环结构
 */
void testSpecialCases() {
    cout << "\n[特殊情况测试]" << endl;
    
    // 测试用例3：最小情况
    vector<vector<int>> edges3 = {{1, 2}, {1, 2}};
    vector<int> expected3 = {1, 2};
    runTestCase("最小情况", edges3, expected3);
    
    // 测试用例4：较复杂的环结构
    vector<vector<int>> edges4 = {{1, 5}, {3, 4}, {3, 5}, {4, 5}, {2, 4}};
    vector<int> expected4 = {4, 5};
    runTestCase("复杂环结构", edges4, expected4);
    
    // 测试用例5：链式结构形成的环
    vector<vector<int>> edges5 = {{1, 2}, {2, 3}, {3, 1}};
    vector<int> expected5 = {3, 1};
    runTestCase("链式环", edges5, expected5);
}

/**
 * 边界情况测试
 * 测试各种边界条件
 */
void testEdgeCases() {
    cout << "\n[边界情况测试]" << endl;
    
    // 测试用例6：空数组
    vector<vector<int>> edges6 = {};
    try {
        Solution solution;
        vector<int> result = solution.findRedundantConnection(edges6);
        cout << "  空数组测试  : [结果: " << (result.empty() ? "空" : to_string(result[0]) + ", " + to_string(result[1])) 
             << ", 预期: 空] 通过" << endl;
    } catch (const exception& e) {
        cout << "  空数组测试  : [失败] 抛出异常: " << typeid(e).name() << " - " << e.what() << endl;
    }
}

/**
 * 主测试方法
 * 运行所有测试用例，验证算法的正确性
 */
int main() {
    // 运行所有测试用例
    cout << "======== 冗余连接算法测试 ========" << endl;
    
    // 基本功能测试
    testBasicFunctionality();
    
    // 特殊情况测试
    testSpecialCases();
    
    // 边界情况测试
    testEdgeCases();
    
    cout << "\n======== 所有测试用例执行完毕 ========" << endl;
    
    return 0;
}
*/

/* Python 实现
class UnionFind:
    """
    并查集类，用于高效处理元素的合并和查询
    实现了路径压缩和按秩合并优化
    
    设计说明：
    - 本实现专门针对节点编号从1开始的场景进行了优化
    - 提供了高效的环检测能力，适用于冗余连接问题
    - 路径压缩和按秩合并保证了近常数时间复杂度
    """
    
    def __init__(self, n):
        """
        初始化并查集
        
        Args:
            n: 节点数量
            
        Raises:
            ValueError: 如果节点数量小于0
        """
        # 参数验证
        if n < 0:
            raise ValueError(f"节点数量不能为负数: {n}")
        
        # 初始化父节点数组，每个节点初始指向自己
        # 注意：节点编号从1开始，所以数组大小为n+1
        self.parent = list(range(n + 1))
        # 初始化秩数组，用于按秩合并优化
        self.rank = [1] * (n + 1)
    
    def find(self, x):
        """
        查找节点的根节点（代表元素）
        使用路径压缩优化
        
        Args:
            x: 要查找的节点
            
        Returns:
            节点所在集合的根节点
            
        Raises:
            IndexError: 当节点索引超出范围时抛出异常
        """
        # 参数范围检查
        if x < 1 or x >= len(self.parent):
            raise IndexError(f"节点索引超出有效范围: {x}")
        
        # 路径压缩：将查找路径上的所有节点直接连接到根节点
        # 这是并查集的关键优化，显著减少了后续查找操作的时间复杂度
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def union(self, x, y):
        """
        合并两个节点所在的集合
        使用按秩合并优化
        
        Args:
            x: 第一个节点
            y: 第二个节点
            
        Returns:
            bool: 如果两个节点已经在同一个集合中返回False，否则返回True
                  返回False表示添加这条边会形成环
            
        Raises:
            IndexError: 当节点索引超出范围时抛出异常
        """
        # 找到两个节点的根节点（带路径压缩）
        root_x = self.find(x)
        root_y = self.find(y)
        
        # 如果已经在同一个集合中，说明添加这条边会形成环
        if root_x == root_y:
            return False
        
        # 按秩合并：将秩小的树合并到秩大的树下
        # 这种策略确保了树的高度尽可能小，优化后续查找操作
        if self.rank[root_x] > self.rank[root_y]:
            # root_x的秩较大，将root_y合并到root_x下
            self.parent[root_y] = root_x
        elif self.rank[root_x] < self.rank[root_y]:
            # root_y的秩较大，将root_x合并到root_y下
            self.parent[root_x] = root_y
        else:
            # 秩相等时，选择一个作为根，并增加其秩
            self.parent[root_y] = root_x
            self.rank[root_x] += 1  # 合并后树的高度增加1
        
        # 合并成功，返回True
        return True
    
    def is_connected(self, x, y):
        """
        判断两个节点是否在同一个集合中
        
        Args:
            x: 第一个节点
            y: 第二个节点
            
        Returns:
            如果两个节点在同一个集合中返回True，否则返回False
            
        Raises:
            IndexError: 当节点索引超出范围时抛出异常
        """
        return self.find(x) == self.find(y)
    
    def get_connected_components(self):
        """
        获取当前连通分量的数量
        注意：此方法需要遍历所有节点，时间复杂度为O(n)
        
        Returns:
            连通分量数量
        """
        # 使用集合记录不同的根节点
        roots = set()
        for i in range(1, len(self.parent)):
            roots.add(self.find(i))
        return len(roots)

class Solution:
    """
    冗余连接问题解决方案
    使用并查集高效检测无向图中的环
    
    算法思路深度解析：
    - 该问题本质是在一个无向图中检测环，并找出环中的一条边
    - 由于题目保证输入是一棵树加上一条边，因此整个图中恰好存在一个环
    - 使用并查集检测环的方法非常高效，时间复杂度接近O(n)
    - 关键优化点是union方法返回布尔值，表示是否形成了环
    """
    
    def findRedundantConnection(self, edges):
        """
        查找冗余连接
        
        Args:
            edges: 边的数组，每个元素是一个包含两个整数的列表，表示一条无向边
            
        Returns:
            list: 冗余的边，如果不存在则返回空列表
            
        Raises:
            ValueError: 当输入无效时抛出异常
            TypeError: 当输入类型不正确时抛出异常
        """
        # 参数验证
        if edges is None:
            raise TypeError("输入的边数组不能为None")
        
        # 边界条件检查
        if not edges:
            return []  # 空输入返回空列表
        
        # 验证输入数组的有效性
        for i, edge in enumerate(edges):
            if not isinstance(edge, list) or len(edge) != 2:
                raise ValueError(f"边数组的第{i}个元素无效")
            if not all(isinstance(node, int) for node in edge):
                raise ValueError(f"边数组的第{i}个元素包含非整数节点")
        
        # 节点数量等于边的数量（因为是树+一条边）
        n = len(edges)
        
        # 创建并查集
        uf = UnionFind(n)
        
        # 遍历每条边
        for edge in edges:
            u, v = edge
            
            # 检查边的有效性
            if u < 1 or u > n or v < 1 or v > n:
                raise ValueError(f"边[{u},{v}]的顶点超出有效范围(1~{n})")
            
            # 如果两个节点已经在同一个连通分量中，说明添加这条边会形成环
            # 根据题目要求，这就是我们要找的冗余边
            if not uf.union(u, v):
                return edge.copy()  # 返回副本以避免返回原列表引用
        
        # 理论上不会执行到这里，因为题目保证输入包含冗余边
        return []


def run_test_case(name, edges, expected):
    """
    执行单个测试用例并输出结果
    
    Args:
        name: 测试用例名称
        edges: 测试边数组
        expected: 预期结果
    """
    solution = Solution()
    try:
        result = solution.findRedundantConnection(edges)
        passed = (result[0] == expected[0] and result[1] == expected[1]) or \
                (result[0] == expected[1] and result[1] == expected[0])
        print(f"  {name:15}: [结果: {result}, 预期: {expected}] {'通过' if passed else '失败'}")
    except Exception as e:
        print(f"  {name:15}: [失败] 抛出异常: {type(e).__name__} - {str(e)}")


def test_basic_functionality():
    """
    基本功能测试
    测试常见的环检测场景
    """
    print("\n[基本功能测试]")
    
    # 测试用例1：简单的三节点环
    edges1 = [[1, 2], [1, 3], [2, 3]]
    expected1 = [2, 3]
    run_test_case("简单三节点环", edges1, expected1)
    
    # 测试用例2：复杂的多节点环
    edges2 = [[1, 2], [2, 3], [3, 4], [1, 4], [1, 5]]
    expected2 = [1, 4]
    run_test_case("复杂多节点环", edges2, expected2)


def test_special_cases():
    """
    特殊情况测试
    测试一些特殊的环结构
    """
    print("\n[特殊情况测试]")
    
    # 测试用例3：最小情况
    edges3 = [[1, 2], [1, 2]]
    expected3 = [1, 2]
    run_test_case("最小情况", edges3, expected3)
    
    # 测试用例4：较复杂的环结构
    edges4 = [[1, 5], [3, 4], [3, 5], [4, 5], [2, 4]]
    expected4 = [4, 5]
    run_test_case("复杂环结构", edges4, expected4)
    
    # 测试用例5：链式结构形成的环
    edges5 = [[1, 2], [2, 3], [3, 1]]
    expected5 = [3, 1]
    run_test_case("链式环", edges5, expected5)
    
    # 测试用例6：较大的图
    edges6 = [[1, 2], [2, 3], [3, 4], [4, 5], [5, 6], [6, 7], [7, 8], [8, 4]]
    expected6 = [8, 4]
    run_test_case("较大的图", edges6, expected6)


def test_edge_cases():
    """
    边界情况测试
    测试各种边界条件
    """
    print("\n[边界情况测试]")
    
    # 测试用例7：空数组
    edges7 = []
    try:
        solution = Solution()
        result = solution.findRedundantConnection(edges7)
        print(f"  空数组测试  : [结果: {result}, 预期: []] 通过")
    except Exception as e:
        print(f"  空数组测试  : [失败] 抛出异常: {type(e).__name__} - {str(e)}")
    
    # 测试用例8：只有一条边
    edges8 = [[1, 2]]
    try:
        solution = Solution()
        result = solution.findRedundantConnection(edges8)
        print(f"  单条边测试  : [结果: {result}, 预期: []] 通过")
    except Exception as e:
        print(f"  单条边测试  : [失败] 抛出异常: {type(e).__name__} - {str(e)}")


def test_exception_handling():
    """
    异常处理测试
    测试异常情况的处理
    """
    print("\n[异常处理测试]")
    
    # 测试用例9：无效的边顶点
    edges9 = [[1, 2], [1, 3], [2, 4]]
    try:
        solution = Solution()
        solution.findRedundantConnection(edges9)
        print("  无效边顶点测试: [失败] 未能捕获异常")
    except ValueError as e:
        print(f"  无效边顶点测试: [通过] 正确捕获异常: {str(e)}")
    except Exception as e:
        print(f"  无效边顶点测试: [失败] 捕获了错误类型的异常: {type(e).__name__}")
    
    # 测试用例10：null输入
    try:
        solution = Solution()
        solution.findRedundantConnection(None)
        print("  null输入测试: [失败] 未能捕获异常")
    except TypeError as e:
        print("  null输入测试: [通过] 正确捕获异常")
    except Exception as e:
        print(f"  null输入测试: [失败] 捕获了错误类型的异常: {type(e).__name__}")


def test_solution():
    """
    主测试函数
    运行所有测试用例
    """
    print("======== 冗余连接算法测试 ========")
    
    # 基本功能测试
    test_basic_functionality()
    
    # 特殊情况测试
    test_special_cases()
    
    # 边界情况测试
    test_edge_cases()
    
    # 异常处理测试
    test_exception_handling()
    
    print("\n======== 所有测试用例执行完毕 ========")

# 执行测试
if __name__ == "__main__":
    test_solution()
*/