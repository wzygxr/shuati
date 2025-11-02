package class057;

import java.util.Scanner;

/**
 * HDU 1213 How Many Tables
 * 
 * 题目描述：
 * 今天是Ignatius的生日，他邀请了很多朋友。现在是晚餐时间，Ignatius想知道他至少需要准备多少张桌子。你需要注意，并不是所有朋友都互相认识，而且所有朋友都不想和陌生人坐在一起。
 * 这个问题的一个重要规则是：如果我告诉你A认识B，B认识C，那么这意味着A、B、C互相认识，因此他们可以坐在同一张桌子上。
 * 例如：如果我告诉你A认识B，B认识C，D认识E，那么A、B、C可以坐在一张桌子上，D、E必须坐在另一张桌子上。因此Ignatius至少需要2张桌子。
 * 
 * 输入格式：
 * 输入以整数T(1<=T<=25)开始，表示测试用例的数量。然后是T个测试用例。每个测试用例以两个整数N和M(1<=N,M<=1000)开始。N表示朋友的数量，朋友编号从1到N。然后是M行，每行包含两个整数A和B(A!=B)，表示朋友A和朋友B互相认识。
 * 
 * 输出格式：
 * 对于每个测试用例，只需输出Ignatius至少需要准备的桌子数量。
 * 
 * 样例输入：
 * 2
 * 5 3
 * 1 2
 * 2 3
 * 4 5
 * 3 3
 * 1 3
 * 2 3
 * 3 1
 * 
 * 样例输出：
 * 2
 * 1
 * 
 * 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=1213
 * 
 * 解题思路：
 * 1. 问题建模：将每个朋友看作一个节点，将朋友间的认识关系建模为边
 * 2. 核心算法：使用并查集（Union-Find）数据结构来维护朋友间的连通性
 * 3. 算法流程：
 *    - 初始化并查集，每个朋友自成一个集合
 *    - 对于每对认识的朋友，将他们合并到同一个集合中
 *    - 最终，集合的数量即为需要准备的桌子数量
 * 
 * 算法思路深度解析：
 * - 并查集非常适合此类问题，因为我们需要频繁地合并集合并最终统计独立集合的数量
 * - 维护components变量记录当前连通分量的数量，比最后再遍历查找所有根节点更高效
 * - 路径压缩和按秩合并的结合使用保证了接近O(1)的均摊操作时间复杂度
 * 
 * 时间复杂度分析：
 * - 并查集初始化：O(N)
 * - 合并操作（M对朋友关系）：O(M*α(N))，其中α是阿克曼函数的反函数，在实际应用中几乎为常数
 * - 总体时间复杂度：O(N + M*α(N)) = O(M*α(N))
 * 
 * 空间复杂度分析：
 * - 并查集数组（parent、rank）：O(N)
 * - 存储输入数据：O(M)
 * - 总体空间复杂度：O(N + M) = O(N)
 * 
 * 是否为最优解：是，目前没有比并查集更高效的解决方案
 * 
 * 工程化考量：
 * 1. 异常处理：
 *    - 检查输入是否合法（T、N、M的取值范围）
 *    - 处理朋友编号从1开始的特殊情况
 * 2. 可配置性：
 *    - 可以修改朋友关系的定义
 *    - 可以扩展为处理不同类型的关系
 * 3. 线程安全：
 *    - 当前实现不是线程安全的
 *    - 在多线程环境中需要添加同步机制
 * 4. 代码可维护性：
 *    - UnionFind类封装完整，便于重用
 *    - 清晰的函数命名和参数说明
 * 
 * 与其他领域的联系：
 * 1. 社交网络分析：
 *    - 识别社区结构和社交圈子
 *    - 确定社交网络中的连通分量
 * 2. 推荐系统：
 *    - 基于朋友关系的推荐算法
 *    - 社交推荐中的信任传递
 * 3. 计算机网络：
 *    - 网络拓扑分析
 *    - 连通性问题
 * 4. 图像处理：
 *    - 连通区域标记
 *    - 图像分割
 * 
 * 语言特性差异：
 * 1. Java：
 *    - 使用类封装并查集操作，提供面向对象的接口
 *    - 利用自动垃圾回收管理内存
 *    - 需要注意朋友编号从1开始的索引调整
 * 2. C++：
 *    - 使用vector存储数据，更加灵活
 *    - 支持更精细的内存控制
 *    - 可以使用引用传递提高性能
 * 3. Python：
 *    - 代码简洁，逻辑清晰
 *    - 使用列表实现并查集，索引操作直观
 *    - 性能相对较低，但对于题目规模足够
 * 
 * 极端情况分析：
 * 1. 没有朋友关系（M=0）：
 *    - 需要N张桌子
 * 2. 所有朋友相互认识：
 *    - 只需要1张桌子
 * 3. 每个朋友都只认识自己：
 *    - 需要N张桌子
 * 4. 朋友数量达到上限（N=1000）：
 *    - 算法仍能高效运行
 * 5. 测试用例数量达到上限（T=25）：
 *    - 多次创建并查集对象，内存使用合理
 * 
 * 性能优化策略：
 * 1. 算法层面：
 *    - 路径压缩优化find操作
 *    - 按秩合并优化union操作
 *    - 维护components变量避免重复计算
 * 2. 工程层面：
 *    - 预先分配数组大小，避免动态扩容
 *    - 使用局部变量减少方法调用开销
 *    - 批量处理输入数据，提高IO效率
 * 
 * 调试技巧：
 * 1. 打印并查集状态：在关键操作处输出parent数组和components值
 * 2. 单步调试：跟踪合并过程中的连通分量变化
 * 3. 边界情况测试：确保处理各种极端输入
 * 
 * 问题迁移能力：
 * 掌握此问题后，可以解决类似的连通性问题，如：
 * - 社交网络中的好友分组
 * - 网络连接中的主机分组
 * - 图像中的连通区域计数
 * - 并查集的其他典型应用
 */
public class Code09_Hdu1213HowManyTables {
    
    /**
     * 并查集类
     */
    static class UnionFind {
        private int[] parent;  // parent[i]表示节点i的父节点
        private int[] rank;    // rank[i]表示以i为根的树的高度上界
        private int components; // 当前连通分量的数量
        
        /**
         * 初始化并查集
         * @param n 节点数量
         */
        public UnionFind(int n) {
            parent = new int[n + 1];  // 朋友编号从1开始
            rank = new int[n + 1];
            components = n;
            
            // 初始时每个节点都是自己的父节点
            for (int i = 1; i <= n; i++) {
                parent[i] = i;
                rank[i] = 1;
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
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            
            // 连通分量数量减1
            components--;
        }
        
        /**
         * 获取当前连通分量的数量
         * @return 连通分量数量
         */
        public int getComponents() {
            return components;
        }
    }
    
    /**
     * 计算需要的桌子数量
     * @param n 朋友数量
     * @param relations 朋友关系
     * @return 需要的桌子数量
     */
    public static int countTables(int n, int[][] relations) {
        // 创建并查集
        UnionFind uf = new UnionFind(n);
        
        // 处理每个朋友关系
        for (int[] relation : relations) {
            uf.union(relation[0], relation[1]);
        }
        
        // 返回连通分量数量
        return uf.getComponents();
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1：标准情况
        testCase1();
        
        // 测试用例2：所有朋友互相认识
        testCase2();
        
        // 测试用例3：没有朋友关系
        testCase3();
        
        // 测试用例4：单个朋友
        testCase4();
    }
    
    /**
     * 测试用例1：标准情况
     * 输入：5 3
     * 1 2
     * 2 3
     * 4 5
     * 预期输出：2
     */
    private static void testCase1() {
        System.out.println("测试用例1：");
        int n = 5;
        int[][] relations = {
            {1, 2},
            {2, 3},
            {4, 5}
        };
        int result = countTables(n, relations);
        System.out.println("结果：" + result + "，预期：2，" + (result == 2 ? "通过" : "失败"));
        System.out.println();
    }
    
    /**
     * 测试用例2：所有朋友互相认识
     * 输入：3 3
     * 1 2
     * 2 3
     * 1 3
     * 预期输出：1
     */
    private static void testCase2() {
        System.out.println("测试用例2：");
        int n = 3;
        int[][] relations = {
            {1, 2},
            {2, 3},
            {1, 3}
        };
        int result = countTables(n, relations);
        System.out.println("结果：" + result + "，预期：1，" + (result == 1 ? "通过" : "失败"));
        System.out.println();
    }
    
    /**
     * 测试用例3：没有朋友关系
     * 输入：4 0
     * 预期输出：4
     */
    private static void testCase3() {
        System.out.println("测试用例3：");
        int n = 4;
        int[][] relations = {};
        int result = countTables(n, relations);
        System.out.println("结果：" + result + "，预期：4，" + (result == 4 ? "通过" : "失败"));
        System.out.println();
    }
    
    /**
     * 测试用例4：单个朋友
     * 输入：1 0
     * 预期输出：1
     */
    private static void testCase4() {
        System.out.println("测试用例4：");
        int n = 1;
        int[][] relations = {};
        int result = countTables(n, relations);
        System.out.println("结果：" + result + "，预期：1，" + (result == 1 ? "通过" : "失败"));
        System.out.println();
    }
    
    /**
     * 注意：以下是C++和Python的实现代码块，实际运行时请单独保存为对应格式的文件
     * 
     * C++实现代码：
     * #include <iostream>
     * #include <vector>
     * using namespace std;
     * 
     * class UnionFind {
     * private:
     *     vector<int> parent;
     *     vector<int> rank;
     *     int components;
     * 
     * public:
     *     // 初始化并查集
     *     UnionFind(int n) {
     *         parent.resize(n + 1); // 朋友编号从1开始
     *         rank.resize(n + 1, 1);
     *         components = n;
     *         
     *         for (int i = 1; i <= n; ++i) {
     *             parent[i] = i;
     *         }
     *     }
     * 
     *     // 查找根节点（路径压缩）
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
     *         } else if (rank[rootX] < rank[rootY]) {
     *             parent[rootX] = rootY;
     *         } else {
     *             parent[rootY] = rootX;
     *             rank[rootX]++;
     *         }
     *         
     *         components--;
     *     }
     * 
     *     // 获取连通分量数量
     *     int getComponents() const {
     *         return components;
     *     }
     * };
     * 
     * int countTables(int n, const vector<pair<int, int>>& relations) {
     *     UnionFind uf(n);
     *     
     *     for (const auto& relation : relations) {
     *         uf.union_sets(relation.first, relation.second);
     *     }
     *     
     *     return uf.getComponents();
     * }
     * 
     * int main() {
     *     int t;
     *     cin >> t;
     *     
     *     while (t--) {
     *         int n, m;
     *         cin >> n >> m;
     *         
     *         vector<pair<int, int>> relations;
     *         for (int i = 0; i < m; ++i) {
     *             int a, b;
     *             cin >> a >> b;
     *             relations.emplace_back(a, b);
     *         }
     *         
     *         cout << countTables(n, relations) << endl;
     *     }
     *     
     *     return 0;
     * }
     * 
     * Python实现代码：
     * class UnionFind:
     *     def __init__(self, n):
     *         # 初始化父节点数组，朋友编号从1开始
     *         self.parent = list(range(n + 1))
     *         # 初始化秩数组，用于按秩合并
     *         self.rank = [1] * (n + 1)
     *         # 初始化连通分量数量
     *         self.components = n
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
     *         elif self.rank[root_x] < self.rank[root_y]:
     *             self.parent[root_x] = root_y
     *         else:
     *             self.parent[root_y] = root_x
     *             self.rank[root_x] += 1
     *         
     *         # 连通分量数量减1
     *         self.components -= 1
     *     
     *     def get_components(self):
     *         """获取当前连通分量的数量"""
     *         return self.components
     * 
     * def count_tables(n, relations):
     *     """计算需要的桌子数量"""
     *     uf = UnionFind(n)
     *     
     *     for a, b in relations:
     *         uf.union(a, b)
     *     
     *     return uf.get_components()
     * 
     * def main():
     *     import sys
     *     input = sys.stdin.read().split()
     *     ptr = 0
     *     
     *     t = int(input[ptr])
     *     ptr += 1
     *     
     *     for _ in range(t):
     *         n = int(input[ptr])
     *         m = int(input[ptr + 1])
     *         ptr += 2
     *         
     *         relations = []
     *         for __ in range(m):
     *             a = int(input[ptr])
     *             b = int(input[ptr + 1])
     *             ptr += 2
     *             relations.append((a, b))
     *         
     *         print(count_tables(n, relations))
     * 
     * if __name__ == "__main__":
     *     main()
     *     
     *     # 测试用例
     *     def run_tests():
     *         # 测试用例1：标准情况
     *         n1 = 5
     *         relations1 = [(1, 2), (2, 3), (4, 5)]
     *         result1 = count_tables(n1, relations1)
     *         print(f"测试用例1: 结果={result1}, 预期=2, {'通过' if result1 == 2 else '失败'}")
     *         
     *         # 测试用例2：所有朋友互相认识
     *         n2 = 3
     *         relations2 = [(1, 2), (2, 3), (1, 3)]
     *         result2 = count_tables(n2, relations2)
     *         print(f"测试用例2: 结果={result2}, 预期=1, {'通过' if result2 == 1 else '失败'}")
     *         
     *         # 测试用例3：没有朋友关系
     *         n3 = 4
     *         relations3 = []
     *         result3 = count_tables(n3, relations3)
     *         print(f"测试用例3: 结果={result3}, 预期=4, {'通过' if result3 == 4 else '失败'}")
     *         
     *         # 测试用例4：单个朋友
     *         n4 = 1
     *         relations4 = []
     *         result4 = count_tables(n4, relations4)
     *         print(f"测试用例4: 结果={result4}, 预期=1, {'通过' if result4 == 1 else '失败'}")
     *     
     *     # 运行测试
     *     run_tests()
     */
}