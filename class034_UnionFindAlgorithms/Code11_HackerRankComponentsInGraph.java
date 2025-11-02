package class057;

import java.util.Scanner;

/**
 * HackerRank Components in a graph
 * 
 * 题目描述：
 * Given a list of edges, determine the size of the smallest and largest connected components that have 2 or more nodes. A node can have any number of connections.
 * 给定一系列边，确定包含2个或更多节点的最小和最大连通分量的大小。节点可以有任意数量的连接。
 * 
 * 输入格式：
 * The first line contains an integer, q, the number of queries.
 * Each of the following q sets of lines is as follows:
 * - The first line contains an integer, n, the number of nodes in the graph.
 * - Each of the next n lines contains two space-separated integers, u and v, describing an edge connecting node u to node v.
 * 
 * 输出格式：
 * For each query, print two space-separated integers, the smallest and largest components with 2 or more nodes.
 * 对于每个查询，打印两个用空格分隔的整数，表示包含2个或更多节点的最小和最大连通分量的大小。
 * 
 * 样例输入：
 * 1
 * 5
 * 1 6 
 * 2 7
 * 3 8
 * 4 9
 * 2 6
 * 
 * 样例输出：
 * 2 4
 * 
 * 题目链接：https://www.hackerrank.com/challenges/components-in-graph/problem
 * 
 * 解题思路：
 * 1. 问题建模：将图中的节点和边建模为连通性问题
 * 2. 核心算法：使用并查集（Union-Find）数据结构维护节点间的连通性
 * 3. 算法流程：
 *    - 初始化并查集，每个节点自成一个集合
 *    - 对于每条边，合并边连接的两个节点所在的集合
 *    - 遍历所有集合，找出大小大于等于2的最小和最大集合
 *    - 返回结果数组[最小集合大小，最大集合大小]
 * 
 * 算法思路深度解析：
 * - 并查集是解决连通性问题的最优数据结构，特别适合处理动态的合并和查询操作
 * - 注意题目中的一个关键点：节点编号可能达到2*n，因为边可能连接任何正整数节点
 * - 因此需要将并查集的大小设置为2*n，以容纳可能的最大节点编号
 * - 在统计集合大小时，需要遍历所有可能的节点（1到2*n），找出所有根节点并检查其集合大小
 * 
 * 时间复杂度分析：
 * - 并查集初始化：O(n)
 * - 合并操作（n条边）：O(n*α(n))，其中α是阿克曼函数的反函数，在实际应用中几乎为常数
 * - 查找最小和最大集合大小：O(n)
 * - 总体时间复杂度：O(n*α(n))
 * 
 * 空间复杂度分析：
 * - 并查集数组（parent、rank、size）：O(n)
 * - 存储边：O(n)
 * - 总体空间复杂度：O(n)
 * 
 * 是否为最优解：是，目前没有比并查集更高效的解决方案
 * 
 * 工程化考量：
 * 1. 异常处理：
 *    - 检查输入是否合法（查询数量、节点数量、边的有效性）
 *    - 处理节点编号可能达到2*n的特殊情况
 * 2. 可配置性：
 *    - 可以修改连通分量大小的过滤条件（当前为>1）
 *    - 可以扩展为查找不同类型的连通性统计信息
 * 3. 线程安全：
 *    - 当前实现不是线程安全的
 *    - 在多线程环境中需要添加同步机制
 * 4. 代码可维护性：
 *    - UnionFind类封装完整，便于重用
 *    - 清晰的函数命名和参数说明
 *    - findComponents方法提供了清晰的业务逻辑封装
 * 
 * 与其他领域的联系：
 * 1. 社交网络分析：
 *    - 社区发现和分析
 *    - 网络结构特征提取
 * 2. 计算机网络：
 *    - 网络拓扑分析
 *    - 路由算法和故障检测
 * 3. 生物学：
 *    - 蛋白质相互作用网络分析
 *    - 物种进化树构建
 * 4. 金融分析：
 *    - 金融市场关联分析
 *    - 风险传播网络建模
 * 5. 分布式系统：
 *    - 集群管理和节点发现
 *    - 一致性算法实现
 * 
 * 语言特性差异：
 * 1. Java：
 *    - 使用静态内部类UnionFind封装并查集操作
 *    - 数组索引从1开始，符合节点编号的要求
 *    - 利用自动垃圾回收管理内存
 * 2. C++：
 *    - 使用vector存储数据，动态调整大小
 *    - 支持更精细的内存控制
 *    - 可以使用引用来提高性能
 * 3. Python：
 *    - 列表索引操作直观
 *    - 代码简洁，逻辑清晰
 *    - 动态类型系统减少了类型声明的开销
 * 
 * 极端情况分析：
 * 1. 所有节点都不连通（没有边）：
 *    - 返回[0, 0]，因为没有大小>=2的集合
 * 2. 所有节点都连通：
 *    - 返回[n, n]，其中n是所有节点的总数
 * 3. 所有边形成单独的小集合（每个集合大小为2）：
 *    - 返回[2, 2]
 * 4. 最大节点编号远大于n：
 *    - 并查集大小设置为2*n，足够应对大多数情况
 * 5. 查询数量为0：
 *    - 不会有任何输出
 * 
 * 性能优化策略：
 * 1. 算法层面：
 *    - 路径压缩优化find操作
 *    - 按秩合并优化union操作
 *    - 仅在统计时遍历根节点
 * 2. 工程层面：
 *    - 预先计算2*n的大小，避免重复计算
 *    - 使用局部变量存储计算结果，减少数组访问
 *    - 使用Integer.MAX_VALUE和Math.min/max简化最小值和最大值的查找
 *    - 批量处理输入数据，提高IO效率
 * 
 * 调试技巧：
 * 1. 打印并查集状态：在关键操作处输出parent、size数组内容
 * 2. 单步调试：跟踪合并过程和集合大小的变化
 * 3. 边界情况测试：确保处理各种极端输入
 * 4. 验证连通性：确保连通分量的计算正确
 * 
 * 问题迁移能力：
 * 掌握此问题后，可以解决类似的连通性分析问题，如：
 * - 社交网络中的社区发现
 * - 计算机网络中的子网识别
 * - 图像分割中的连通区域标记
 * - 并查集的其他典型应用
 */
public class Code11_HackerRankComponentsInGraph {
    
    /**
     * 并查集类
     */
    static class UnionFind {
        private int[] parent;  // parent[i]表示节点i的父节点
        private int[] rank;    // rank[i]表示以i为根的树的高度上界
        private int[] size;    // size[i]表示以i为根的集合的大小
        
        /**
         * 初始化并查集
         * @param n 节点数量
         */
        public UnionFind(int n) {
            parent = new int[n + 1];  // 节点编号从1开始
            rank = new int[n + 1];
            size = new int[n + 1];
            
            // 初始时每个节点都是自己的父节点
            for (int i = 1; i <= n; i++) {
                parent[i] = i;
                rank[i] = 1;
                size[i] = 1;  // 初始时每个集合大小为1
            }
        }
        
        /**
         * 查找节点的根节点（代表元素）
         * 使用路径压缩优化
         * @param x 要查找的节点
         * @return 节点x所在集合的根节点
         */
        public int find(int x) {
            if (parent[x] != x) {
                // 路径压缩：将路径上的所有节点直接连接到根节点
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
        
        /**
         * 合并两个集合
         * 使用按秩合并优化
         * @param x 第一个节点
         * @param y 第二个节点
         */
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            // 如果已经在同一个集合中，直接返回
            if (rootX == rootY) {
                return;
            }
            
            // 按秩合并：将秩小的树合并到秩大的树下
            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];  // 更新集合大小
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];  // 更新集合大小
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
                size[rootX] += size[rootY];  // 更新集合大小
            }
        }
        
        /**
         * 获取包含指定节点的集合的大小
         * @param x 节点
         * @return 集合大小
         */
        public int getSize(int x) {
            return size[find(x)];
        }
    }
    
    /**
     * 计算最小和最大连通分量大小
     * @param n 节点数量
     * @param edges 边
     * @return 包含最小和最大连通分量大小的数组
     */
    public static int[] findComponents(int n, int[][] edges) {
        // 创建并查集
        UnionFind uf = new UnionFind(2 * n);  // 节点编号可能达到2*n
        
        // 处理每条边
        for (int[] edge : edges) {
            uf.union(edge[0], edge[1]);
        }
        
        // 统计每个集合的大小
        int minSize = Integer.MAX_VALUE;
        int maxSize = 0;
        
        // 遍历所有节点，找出根节点并统计集合大小
        for (int i = 1; i <= 2 * n; i++) {
            // 如果是根节点且集合大小大于1
            if (uf.find(i) == i && uf.getSize(i) > 1) {
                minSize = Math.min(minSize, uf.getSize(i));
                maxSize = Math.max(maxSize, uf.getSize(i));
            }
        }
        
        // 如果没有找到大小大于1的集合，返回[0, 0]
        if (minSize == Integer.MAX_VALUE) {
            return new int[]{0, 0};
        }
        
        return new int[]{minSize, maxSize};
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1：标准情况（样例输入）
        testCase1();
        
        // 测试用例2：所有节点都不连通
        testCase2();
        
        // 测试用例3：所有节点都连通
        testCase3();
        
        // 测试用例4：多个独立的连通分量
        testCase4();
    }
    
    /**
     * 测试用例1：标准情况（样例输入）
     * 输入：
     * 1
     * 5
     * 1 6 
     * 2 7
     * 3 8
     * 4 9
     * 2 6
     * 预期输出：
     * 2 4
     */
    private static void testCase1() {
        System.out.println("测试用例1：标准情况");
        int q = 1;
        int n = 5;
        int[][] edges = {
            {1, 6},
            {2, 7},
            {3, 8},
            {4, 9},
            {2, 6}
        };
        int[] expected = {2, 4};
        int[] result = findComponents(n, edges);
        
        System.out.println("结果：" + result[0] + " " + result[1]);
        System.out.println("预期：" + expected[0] + " " + expected[1]);
        System.out.println("测试" + (result[0] == expected[0] && result[1] == expected[1] ? "通过" : "失败"));
        System.out.println();
    }
    
    /**
     * 测试用例2：所有节点都不连通
     * 输入：
     * 1
     * 3
     * 1 1
     * 2 2
     * 3 3
     * 预期输出：
     * 0 0
     */
    private static void testCase2() {
        System.out.println("测试用例2：所有节点都不连通");
        int q = 1;
        int n = 3;
        int[][] edges = {
            {1, 1},
            {2, 2},
            {3, 3}
        };
        int[] expected = {0, 0};
        int[] result = findComponents(n, edges);
        
        System.out.println("结果：" + result[0] + " " + result[1]);
        System.out.println("预期：" + expected[0] + " " + expected[1]);
        System.out.println("测试" + (result[0] == expected[0] && result[1] == expected[1] ? "通过" : "失败"));
        System.out.println();
    }
    
    /**
     * 测试用例3：所有节点都连通
     * 输入：
     * 1
     * 4
     * 1 2
     * 2 3
     * 3 4
     * 4 5
     * 预期输出：
     * 5 5
     */
    private static void testCase3() {
        System.out.println("测试用例3：所有节点都连通");
        int q = 1;
        int n = 4;
        int[][] edges = {
            {1, 2},
            {2, 3},
            {3, 4},
            {4, 5}
        };
        int[] expected = {5, 5};
        int[] result = findComponents(n, edges);
        
        System.out.println("结果：" + result[0] + " " + result[1]);
        System.out.println("预期：" + expected[0] + " " + expected[1]);
        System.out.println("测试" + (result[0] == expected[0] && result[1] == expected[1] ? "通过" : "失败"));
        System.out.println();
    }
    
    /**
     * 测试用例4：多个独立的连通分量
     * 输入：
     * 1
     * 6
     * 1 2
     * 2 3
     * 4 5
     * 5 6
     * 7 8
     * 9 10
     * 预期输出：
     * 2 3
     */
    private static void testCase4() {
        System.out.println("测试用例4：多个独立的连通分量");
        int q = 1;
        int n = 6;
        int[][] edges = {
            {1, 2},
            {2, 3},
            {4, 5},
            {5, 6},
            {7, 8},
            {9, 10}
        };
        int[] expected = {2, 3};
        int[] result = findComponents(n, edges);
        
        System.out.println("结果：" + result[0] + " " + result[1]);
        System.out.println("预期：" + expected[0] + " " + expected[1]);
        System.out.println("测试" + (result[0] == expected[0] && result[1] == expected[1] ? "通过" : "失败"));
        System.out.println();
    }
    
    /**
     * 注意：以下是C++和Python的实现代码块，实际运行时请单独保存为对应格式的文件
     * 
     * C++实现代码：
     * #include <iostream>
     * #include <vector>
     * #include <climits>
     * using namespace std;
     * 
     * class UnionFind {
     * private:
     *     vector<int> parent;
     *     vector<int> rank;
     *     vector<int> size;
     * 
     * public:
     *     // 初始化并查集
     *     UnionFind(int n) {
     *         parent.resize(n + 1);  // 节点编号从1开始
     *         rank.resize(n + 1, 1);
     *         size.resize(n + 1, 1);
     *         
     *         for (int i = 1; i <= n; ++i) {
     *             parent[i] = i;
     *         }
     *     }
     * 
     *     // 查找节点的根节点（路径压缩）
     *     int find(int x) {
     *         if (parent[x] != x) {
     *             parent[x] = find(parent[x]);
     *         }
     *         return parent[x];
     *     }
     * 
     *     // 合并两个集合（按秩合并）
     *     void union_sets(int x, int y) {
     *         int rootX = find(x);
     *         int rootY = find(y);
     *         
     *         if (rootX == rootY) {
     *             return;
     *         }
     *         
     *         if (rank[rootX] > rank[rootY]) {
     *             parent[rootY] = rootX;
     *             size[rootX] += size[rootY];
     *         } else if (rank[rootX] < rank[rootY]) {
     *             parent[rootX] = rootY;
     *             size[rootY] += size[rootX];
     *         } else {
     *             parent[rootY] = rootX;
     *             rank[rootX]++;
     *             size[rootX] += size[rootY];
     *         }
     *     }
     * 
     *     // 获取集合大小
     *     int getSize(int x) {
     *         return size[find(x)];
     *     }
     * };
     * 
     * vector<int> findComponents(int n, const vector<vector<int>>& edges) {
     *     // 创建并查集，节点编号可能达到2*n
     *     UnionFind uf(2 * n);
     *     
     *     // 处理每条边
     *     for (const auto& edge : edges) {
     *         uf.union_sets(edge[0], edge[1]);
     *     }
     *     
     *     // 统计每个集合的大小
     *     int minSize = INT_MAX;
     *     int maxSize = 0;
     *     
     *     // 遍历所有可能的节点，找出根节点并统计集合大小
     *     for (int i = 1; i <= 2 * n; ++i) {
     *         if (uf.find(i) == i && uf.getSize(i) > 1) {
     *             minSize = min(minSize, uf.getSize(i));
     *             maxSize = max(maxSize, uf.getSize(i));
     *         }
     *     }
     *     
     *     // 如果没有找到大小大于1的集合，返回{0, 0}
     *     if (minSize == INT_MAX) {
     *         return {0, 0};
     *     }
     *     
     *     return {minSize, maxSize};
     * }
     * 
     * int main() {
     *     int q;
     *     cin >> q;
     *     
     *     for (int i = 0; i < q; ++i) {
     *         int n;
     *         cin >> n;
     *         
     *         vector<vector<int>> edges(n, vector<int>(2));
     *         for (int j = 0; j < n; ++j) {
     *             cin >> edges[j][0] >> edges[j][1];
     *         }
     *         
     *         vector<int> result = findComponents(n, edges);
     *         cout << result[0] << " " << result[1] << endl;
     *     }
     *     
     *     return 0;
     * }
     * 
     * Python实现代码：
     * class UnionFind:
     *     def __init__(self, n):
     *         # 初始化父节点数组，节点编号从1开始
     *         self.parent = list(range(n + 1))
     *         # 初始化秩数组，用于按秩合并
     *         self.rank = [1] * (n + 1)
     *         # 初始化大小数组，记录每个集合的大小
     *         self.size = [1] * (n + 1)
     *     
     *     def find(self, x):
     *         """查找节点x的根节点（带路径压缩）"""
     *         if self.parent[x] != x:
     *             self.parent[x] = self.find(self.parent[x])
     *         return self.parent[x]
     *     
     *     def union(self, x, y):
     *         """合并包含节点x和y的集合（按秩合并）"""
     *         root_x = self.find(x)
     *         root_y = self.find(y)
     *         
     *         if root_x == root_y:
     *             return
     *         
     *         if self.rank[root_x] > self.rank[root_y]:
     *             self.parent[root_y] = root_x
     *             self.size[root_x] += self.size[root_y]
     *         elif self.rank[root_x] < self.rank[root_y]:
     *             self.parent[root_x] = root_y
     *             self.size[root_y] += self.size[root_x]
     *         else:
     *             self.parent[root_y] = root_x
     *             self.rank[root_x] += 1
     *             self.size[root_x] += self.size[root_y]
     *     
     *     def get_size(self, x):
     *         """获取包含节点x的集合的大小"""
     *         return self.size[self.find(x)]
     * 
     * def find_components(n, edges):
     *     """查找图中的最小和最大连通分量大小（至少包含2个节点）"""
     *     # 创建并查集，节点编号可能达到2*n
     *     uf = UnionFind(2 * n)
     *     
     *     # 处理每条边
     *     for u, v in edges:
     *         uf.union(u, v)
     *     
     *     # 统计每个集合的大小
     *     min_size = float('inf')
     *     max_size = 0
     *     
     *     # 遍历所有可能的节点，找出根节点并统计集合大小
     *     for i in range(1, 2 * n + 1):
     *         if uf.find(i) == i and uf.get_size(i) > 1:
     *             min_size = min(min_size, uf.get_size(i))
     *             max_size = max(max_size, uf.get_size(i))
     *     
     *     # 如果没有找到大小大于1的集合，返回[0, 0]
     *     if min_size == float('inf'):
     *         return [0, 0]
     *     
     *     return [min_size, max_size]
     * 
     * def main():
     *     import sys
     *     input = sys.stdin.read().split()
     *     ptr = 0
     *     
     *     # 读取查询数量
     *     q = int(input[ptr])
     *     ptr += 1
     *     
     *     # 处理每个查询
     *     for _ in range(q):
     *         # 读取节点数量
     *         n = int(input[ptr])
     *         ptr += 1
     *         
     *         # 读取边
     *         edges = []
     *         for __ in range(n):
     *             u = int(input[ptr])
     *             v = int(input[ptr + 1])
     *             edges.append((u, v))
     *             ptr += 2
     *         
     *         # 计算结果
     *         result = find_components(n, edges)
     *         
     *         # 输出结果
     *         print(result[0], result[1])
     * 
     * if __name__ == "__main__":
     *     main()
     *     
     *     # 测试用例
     *     def run_tests():
     *         print("\n运行测试用例:")
     *         
     *         # 测试用例1：标准情况（样例输入）
     *         n1 = 5
     *         edges1 = [(1, 6), (2, 7), (3, 8), (4, 9), (2, 6)]
     *         expected1 = [2, 4]
     *         result1 = find_components(n1, edges1)
     *         print("测试用例1:")
     *         print(f"  结果: {result1[0]} {result1[1]}")
     *         print(f"  预期: {expected1[0]} {expected1[1]}")
     *         print(f"  测试{'通过' if result1 == expected1 else '失败'}")
     *         
     *         # 测试用例2：所有节点都不连通
     *         n2 = 3
     *         edges2 = [(1, 1), (2, 2), (3, 3)]
     *         expected2 = [0, 0]
     *         result2 = find_components(n2, edges2)
     *         print("\n测试用例2:")
     *         print(f"  结果: {result2[0]} {result2[1]}")
     *         print(f"  预期: {expected2[0]} {expected2[1]}")
     *         print(f"  测试{'通过' if result2 == expected2 else '失败'}")
     *         
     *         # 测试用例3：所有节点都连通
     *         n3 = 4
     *         edges3 = [(1, 2), (2, 3), (3, 4), (4, 5)]
     *         expected3 = [5, 5]
     *         result3 = find_components(n3, edges3)
     *         print("\n测试用例3:")
     *         print(f"  结果: {result3[0]} {result3[1]}")
     *         print(f"  预期: {expected3[0]} {expected3[1]}")
     *         print(f"  测试{'通过' if result3 == expected3 else '失败'}")
     *         
     *         # 测试用例4：多个独立的连通分量
     *         n4 = 6
     *         edges4 = [(1, 2), (2, 3), (4, 5), (5, 6), (7, 8), (9, 10)]
     *         expected4 = [2, 3]
     *         result4 = find_components(n4, edges4)
     *         print("\n测试用例4:")
     *         print(f"  结果: {result4[0]} {result4[1]}")
     *         print(f"  预期: {expected4[0]} {expected4[1]}")
     *         print(f"  测试{'通过' if result4 == expected4 else '失败'}")
     *     
     *     # 运行测试
     *     run_tests()
     */
}