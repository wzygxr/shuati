package class057;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 洛谷 P1551 亲戚
 * 
 * 题目描述：
 * 若某个家族人员过于庞大，要判断两个人是否是亲戚，确实还很不容易，现在给出某个亲戚关系图，求任意给出的两个人是否具有亲戚关系。
 * 亲戚关系具有传递性：如果A和B是亲戚，B和C是亲戚，那么A和C也是亲戚。
 * 
 * 输入格式：
 * 第一行：三个整数n,m,p，(n<=5000,m<=5000,p<=5000)，分别表示有n个人，m个亲戚关系，询问p对亲戚关系。
 * 以下m行：每行两个数Mi，Mj，1<=Mi,Mj<=N，表示Mi和Mj具有亲戚关系。
 * 接下来p行：每行两个数Pi，Pj，询问Pi和Pj是否具有亲戚关系。
 * 
 * 输出格式：
 * 对于每个询问，输出"YES"或"NO"。
 * 
 * 样例输入：
 * 6 5 3
 * 1 2
 * 1 5
 * 3 4
 * 5 2
 * 1 3
 * 1 4
 * 2 3
 * 5 6
 * 
 * 样例输出：
 * YES
 * NO
 * NO
 * 
 * 题目链接：https://www.luogu.com.cn/problem/P1551
 * 
 * 解题思路：
 * 1. 问题建模：将每个人看作一个节点，将亲戚关系建模为边
 * 2. 核心算法：使用并查集（Union-Find）数据结构来维护人员之间的连通性
 * 3. 算法流程：
 *    - 初始化并查集，每个人自成一个集合
 *    - 对于每对亲戚关系，将他们合并到同一个集合中
 *    - 对于每个查询，判断两个人是否在同一个集合中
 *    - 根据判断结果返回"YES"或"NO"
 * 
 * 算法思路深度解析：
 * - 并查集是解决这类等价关系判断问题的最优数据结构
 * - 亲戚关系的传递性正好对应并查集的等价关系特性
 * - 路径压缩和按秩合并的结合使用保证了接近O(1)的均摊操作时间复杂度
 * - isConnected方法是并查集的自然扩展，方便直接判断两个节点是否连通
 * 
 * 时间复杂度分析：
 * - 并查集初始化：O(n)
 * - 合并操作（m对亲戚关系）：O(m*α(n))，其中α是阿克曼函数的反函数，在实际应用中几乎为常数
 * - 查询操作（p个查询）：O(p*α(n))
 * - 总体时间复杂度：O(n + (m+p)*α(n)) = O((m+p)*α(n))
 * 
 * 空间复杂度分析：
 * - 并查集数组（parent、rank）：O(n)
 * - 存储亲戚关系和查询：O(m + p)
 * - 存储查询结果：O(p)
 * - 总体空间复杂度：O(n + m + p) = O(n + p)（因为m,p <= 5000）
 * 
 * 是否为最优解：是，目前没有比并查集更高效的解决方案
 * 
 * 工程化考量：
 * 1. 异常处理：
 *    - 检查输入是否合法（n、m、p的取值范围）
 *    - 处理人员编号从1开始的特殊情况
 * 2. 可配置性：
 *    - 可以修改亲戚关系的定义
 *    - 可以扩展为处理不同类型的关系
 * 3. 线程安全：
 *    - 当前实现不是线程安全的
 *    - 在多线程环境中需要添加同步机制
 * 4. 代码可维护性：
 *    - UnionFind类封装完整，便于重用
 *    - 清晰的函数命名和参数说明
 *    - checkRelatives方法提供了清晰的业务逻辑封装
 * 
 * 与其他领域的联系：
 * 1. 社交网络分析：
 *    - 社交关系网络中的好友关系查询
 *    - 二度人脉、三度人脉等查找
 * 2. 计算机网络：
 *    - 网络节点间的连通性检测
 *    - 故障诊断和网络拓扑分析
 * 3. 数据库：
 *    - 等价关系查询优化
 *    - 联合查询中的集合管理
 * 4. 图像处理：
 *    - 连通区域判断
 *    - 图像分割中的等价关系维护
 * 
 * 语言特性差异：
 * 1. Java：
 *    - 使用类封装并查集操作，提供面向对象的接口
 *    - 利用自动垃圾回收管理内存
 *    - 需要注意人员编号从1开始的索引调整
 * 2. C++：
 *    - 使用vector存储数据，更加灵活
 *    - 支持更精细的内存控制
 *    - 可以使用引用传递提高性能
 * 3. Python：
 *    - 代码简洁，逻辑清晰
 *    - 使用列表实现并查集，索引操作直观
 *    - 字符串处理和输出格式化更加便捷
 * 
 * 极端情况分析：
 * 1. 没有亲戚关系（m=0）：
 *    - 每个人都自成一个集合
 *    - 除了自己与自己外，所有查询都返回"NO"
 * 2. 所有人都是亲戚：
 *    - 所有查询都返回"YES"
 * 3. 每个人只和自己是亲戚：
 *    - 除了自己与自己外，所有查询都返回"NO"
 * 4. 最大规模输入（n=5000, m=5000, p=5000）：
 *    - 算法仍能高效运行，不会超出内存限制
 * 5. 所有查询都是同一个人：
 *    - 所有查询都返回"YES"
 * 
 * 性能优化策略：
 * 1. 算法层面：
 *    - 路径压缩优化find操作
 *    - 按秩合并优化union操作
 * 2. 工程层面：
 *    - 预先分配数组大小，避免动态扩容
 *    - 使用局部变量减少方法调用开销
 *    - 批量处理输入数据，提高IO效率
 *    - 字符串结果预先计算并存储，避免重复处理
 * 
 * 调试技巧：
 * 1. 打印并查集状态：在关键操作处输出parent数组内容
 * 2. 单步调试：跟踪合并过程和查询过程
 * 3. 边界情况测试：确保处理各种极端输入
 * 4. 验证传递性：确保亲戚关系的传递性正确实现
 * 
 * 问题迁移能力：
 * 掌握此问题后，可以解决类似的等价关系判断问题，如：
 * - 社交网络中的好友关系查询
 * - 图论中的连通性判断
 * - 数据库中的等价类划分
 * - 并查集的其他典型应用
 */
public class Code10_LuoguP1551Relatives {
    
    /**
     * 并查集类
     */
    static class UnionFind {
        private int[] parent;  // parent[i]表示节点i的父节点
        private int[] rank;    // rank[i]表示以i为根的树的高度上界
        
        /**
         * 初始化并查集
         * @param n 节点数量
         */
        public UnionFind(int n) {
            parent = new int[n + 1];  // 人员编号从1开始
            rank = new int[n + 1];
            
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
        }
        
        /**
         * 判断两个节点是否在同一个集合中
         * @param x 第一个节点
         * @param y 第二个节点
         * @return 如果在同一个集合中返回true，否则返回false
         */
        public boolean isConnected(int x, int y) {
            return find(x) == find(y);
        }
    }
    
    /**
     * 判断两个人是否是亲戚
     * @param n 人员数量
     * @param relations 亲戚关系数组，每个元素是两个整数表示的亲戚对
     * @param queries 查询数组，每个元素是两个整数表示的查询对
     * @return 查询结果数组，每个元素是"YES"或"NO"
     * @throws IllegalArgumentException 如果输入参数不合法
     */
    public static String[] checkRelatives(int n, int[][] relations, int[][] queries) {
        // 参数验证
        if (n < 1 || n > 5000) {
            throw new IllegalArgumentException("人员数量必须在1到5000之间");
        }
        if (relations == null || queries == null) {
            throw new IllegalArgumentException("亲戚关系和查询数组不能为null");
        }
        
        // 创建并查集
        UnionFind uf = new UnionFind(n);
        
        // 处理每个亲戚关系
        for (int[] relation : relations) {
            if (relation == null || relation.length != 2) {
                throw new IllegalArgumentException("亲戚关系格式错误");
            }
            int x = relation[0];
            int y = relation[1];
            if (x < 1 || x > n || y < 1 || y > n) {
                throw new IllegalArgumentException("人员编号必须在1到" + n + "之间");
            }
            uf.union(x, y);
        }
        
        // 处理每个查询
        String[] results = new String[queries.length];
        for (int i = 0; i < queries.length; i++) {
            if (queries[i] == null || queries[i].length != 2) {
                throw new IllegalArgumentException("查询格式错误");
            }
            int x = queries[i][0];
            int y = queries[i][1];
            if (x < 1 || x > n || y < 1 || y > n) {
                throw new IllegalArgumentException("人员编号必须在1到" + n + "之间");
            }
            results[i] = uf.isConnected(x, y) ? "YES" : "NO";
        }
        
        return results;
    }
    
    /**
     * 主方法，用于运行所有测试用例
     */
    public static void main(String[] args) {
        // 运行基本功能测试
        runBasicTests();
        
        // 运行边界情况测试
        runBoundaryTests();
        
        // 运行特殊情况测试
        runSpecialTests();
        
        // 运行异常处理测试
        runExceptionTests();
    }
    
    /**
     * 运行基本功能测试
     */
    private static void runBasicTests() {
        System.out.println("=== 基本功能测试 ===");
        testCase1();
        testCase3();
    }
    
    /**
     * 运行边界情况测试
     */
    private static void runBoundaryTests() {
        System.out.println("=== 边界情况测试 ===");
        testCase2();
        testCase5(); // 单个人
        testCase6(); // 最大规模输入
    }
    
    /**
     * 运行特殊情况测试
     */
    private static void runSpecialTests() {
        System.out.println("=== 特殊情况测试 ===");
        testCase4();
        testCase7(); // 重复的亲戚关系
    }
    
    /**
     * 运行异常处理测试
     */
    private static void runExceptionTests() {
        System.out.println("=== 异常处理测试 ===");
        testCase8(); // 参数验证测试
    }
    
    /**
     * 测试用例1：标准情况（样例输入）
     * 输入：
     * 6 5 3
     * 1 2
     * 1 5
     * 3 4
     * 5 2
     * 1 3
     * 1 4
     * 2 3
     * 5 6
     * 预期输出：
     * YES
     * NO
     * NO
     */
    private static void testCase1() {
        System.out.println("测试用例1：标准情况");
        int n = 6;
        int[][] relations = {
            {1, 2},
            {1, 5},
            {3, 4},
            {5, 2},
            {1, 3}
        };
        int[][] queries = {
            {1, 4},
            {2, 3},
            {5, 6}
        };
        String[] expected = {"YES", "NO", "NO"};
        String[] results = checkRelatives(n, relations, queries);
        
        for (int i = 0; i < results.length; i++) {
            System.out.println("查询 " + (i + 1) + "：" + results[i] + ", 预期：" + expected[i] + ", " + 
                              (results[i].equals(expected[i]) ? "通过" : "失败"));
        }
        System.out.println();
    }
    
    /**
     * 测试用例2：没有亲戚关系
     * 输入：
     * 4 0 2
     * 1 2
     * 3 4
     * 预期输出：
     * NO
     * NO
     */
    private static void testCase2() {
        System.out.println("测试用例2：没有亲戚关系");
        int n = 4;
        int[][] relations = {};
        int[][] queries = {
            {1, 2},
            {3, 4}
        };
        String[] expected = {"NO", "NO"};
        String[] results = checkRelatives(n, relations, queries);
        
        for (int i = 0; i < results.length; i++) {
            System.out.println("查询 " + (i + 1) + "：" + results[i] + ", 预期：" + expected[i] + ", " + 
                              (results[i].equals(expected[i]) ? "通过" : "失败"));
        }
        System.out.println();
    }
    
    /**
     * 测试用例3：所有人都是亲戚
     * 输入：
     * 5 4 3
     * 1 2
     * 2 3
     * 3 4
     * 4 5
     * 1 5
     * 2 4
     * 3 5
     * 预期输出：
     * YES
     * YES
     * YES
     */
    private static void testCase3() {
        System.out.println("测试用例3：所有人都是亲戚");
        int n = 5;
        int[][] relations = {
            {1, 2},
            {2, 3},
            {3, 4},
            {4, 5}
        };
        int[][] queries = {
            {1, 5},
            {2, 4},
            {3, 5}
        };
        String[] expected = {"YES", "YES", "YES"};
        String[] results = checkRelatives(n, relations, queries);
        
        for (int i = 0; i < results.length; i++) {
            System.out.println("查询 " + (i + 1) + "：" + results[i] + ", 预期：" + expected[i] + ", " + 
                              (results[i].equals(expected[i]) ? "通过" : "失败"));
        }
        System.out.println();
    }
    
    /**
     * 测试用例4：自己查询自己
     * 输入：
     * 3 1 2
     * 1 2
     * 1 1
     * 3 3
     * 预期输出：
     * YES
     * YES
     */
    private static void testCase4() {
        System.out.println("测试用例4：自己查询自己");
        int n = 3;
        int[][] relations = {
            {1, 2}
        };
        int[][] queries = {
            {1, 1},
            {3, 3}
        };
        String[] expected = {"YES", "YES"};
        String[] results = checkRelatives(n, relations, queries);
        
        for (int i = 0; i < results.length; i++) {
            System.out.println("查询 " + (i + 1) + "：" + results[i] + ", 预期：" + expected[i] + ", " + 
                              (results[i].equals(expected[i]) ? "通过" : "失败"));
        }
        System.out.println();
    }
    
    /**
     * 测试用例5：单个人
     * 输入：
     * 1 0 1
     * 1 1
     * 预期输出：
     * YES
     */
    private static void testCase5() {
        System.out.println("测试用例5：单个人");
        int n = 1;
        int[][] relations = {};
        int[][] queries = {
            {1, 1}
        };
        String[] expected = {"YES"};
        String[] results = checkRelatives(n, relations, queries);
        
        System.out.println("查询 1：" + results[0] + ", 预期：" + expected[0] + ", " + 
                          (results[0].equals(expected[0]) ? "通过" : "失败"));
        System.out.println();
    }
    
    /**
     * 测试用例6：最大规模输入模拟
     * 注意：为了避免测试时间过长，使用较小的规模模拟
     */
    private static void testCase6() {
        System.out.println("测试用例6：大规模输入模拟");
        int n = 1000; // 模拟大规模，实际限制为5000
        int[][] relations = new int[1000][2];
        for (int i = 0; i < 1000; i++) {
            relations[i] = new int[]{i + 1, (i + 1) % n + 1};
        }
        int[][] queries = {{1, 500}, {1, 1000}};
        String[] expected = {"YES", "YES"};
        
        long startTime = System.currentTimeMillis();
        String[] results = checkRelatives(n, relations, queries);
        long endTime = System.currentTimeMillis();
        
        for (int i = 0; i < results.length; i++) {
            System.out.println("查询 " + (i + 1) + "：" + results[i] + ", 预期：" + expected[i] + ", " + 
                              (results[i].equals(expected[i]) ? "通过" : "失败"));
        }
        System.out.println("执行时间：" + (endTime - startTime) + "ms");
        System.out.println();
    }
    
    /**
     * 测试用例7：重复的亲戚关系
     * 输入：
     * 4 3 2
     * 1 2
     * 1 2
     * 2 3
     * 1 3
     * 2 4
     * 预期输出：
     * YES
     * NO
     */
    private static void testCase7() {
        System.out.println("测试用例7：重复的亲戚关系");
        int n = 4;
        int[][] relations = {
            {1, 2},
            {1, 2}, // 重复的亲戚关系
            {2, 3}
        };
        int[][] queries = {
            {1, 3},
            {2, 4}
        };
        String[] expected = {"YES", "NO"};
        String[] results = checkRelatives(n, relations, queries);
        
        for (int i = 0; i < results.length; i++) {
            System.out.println("查询 " + (i + 1) + "：" + results[i] + ", 预期：" + expected[i] + ", " + 
                              (results[i].equals(expected[i]) ? "通过" : "失败"));
        }
        System.out.println();
    }
    
    /**
     * 测试用例8：异常处理测试
     */
    private static void testCase8() {
        System.out.println("测试用例8：异常处理测试");
        
        // 测试非法的人员数量
        try {
            checkRelatives(0, new int[0][0], new int[0][0]);
            System.out.println("测试非法人员数量：失败");
        } catch (IllegalArgumentException e) {
            System.out.println("测试非法人员数量：通过 - " + e.getMessage());
        }
        
        // 测试非法的人员编号
        try {
            checkRelatives(3, new int[][]{{1, 4}}, new int[0][0]);
            System.out.println("测试非法人员编号：失败");
        } catch (IllegalArgumentException e) {
            System.out.println("测试非法人员编号：通过 - " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * 注意：以下是C++和Python的实现代码块，实际运行时请单独保存为对应格式的文件
     * 
     * C++实现代码：
     * #include <iostream>
     * #include <vector>
     * #include <string>
     * #include <stdexcept>
     * using namespace std;
     * 
     * /**
     * 并查集类 - 用于维护集合的并查操作
     * 包含路径压缩和按秩合并优化
     */
     * class UnionFind {
     * private:
     *     vector<int> parent; // 存储每个节点的父节点
     *     vector<int> rank;   // 存储每个根节点对应树的秩（高度上界）
     * 
     * public:
     *     /**
     *     初始化并查集
     *     @param n 节点数量
     *     @throws invalid_argument 如果节点数量不合法
     */
     *     UnionFind(int n) {
     *         if (n <= 0) {
     *             throw invalid_argument("节点数量必须大于0");
     *         }
     *         
     *         parent.resize(n + 1); // 人员编号从1开始
     *         rank.resize(n + 1, 1);
     *         
     *         // 初始时每个节点的父节点是自己
     *         for (int i = 1; i <= n; ++i) {
     *             parent[i] = i;
     *         }
     *     }
     * 
     *     /**
     *     查找节点的根节点（带路径压缩优化）
     *     @param x 要查找的节点
     *     @return 节点x所在集合的根节点
     *     @throws out_of_range 如果节点超出范围
     */
     *     int find(int x) {
     *         if (x < 1 || x >= (int)parent.size()) {
     *             throw out_of_range("节点超出范围");
     *         }
     *         
     *         // 路径压缩：递归地将路径上的所有节点直接连接到根节点
     *         if (parent[x] != x) {
     *             parent[x] = find(parent[x]);
     *         }
     *         return parent[x];
     *     }
     * 
     *     /**
     *     合并两个节点所在的集合（带按秩合并优化）
     *     @param x 第一个节点
     *     @param y 第二个节点
     */
     *     void union_sets(int x, int y) {
     *         int rootX = find(x);
     *         int rootY = find(y);
     *         
     *         // 如果已经在同一个集合中，无需合并
     *         if (rootX == rootY) {
     *             return;
     *         }
     *         
     *         // 按秩合并：将秩小的树合并到秩大的树下
     *         if (rank[rootX] > rank[rootY]) {
     *             parent[rootY] = rootX;
     *         } else if (rank[rootX] < rank[rootY]) {
     *             parent[rootX] = rootY;
     *         } else {
     *             // 秩相等时，任选一个作为根，并增加其秩
     *             parent[rootY] = rootX;
     *             rank[rootX]++;
     *         }
     *     }
     * 
     *     /**
     *     判断两个节点是否在同一个集合中
     *     @param x 第一个节点
     *     @param y 第二个节点
     *     @return 如果在同一个集合中返回true，否则返回false
     */
     *     bool isConnected(int x, int y) {
     *         return find(x) == find(y);
     *     }
     * };
     * 
     * /**
     * 检查亲戚关系
     * @param n 人员数量
     * @param relations 亲戚关系集合
     * @param queries 查询集合
     * @return 查询结果集合
     * @throws invalid_argument 如果输入参数不合法
     */
     * vector<string> checkRelatives(int n, const vector<pair<int, int>>& relations, const vector<pair<int, int>>& queries) {
     *     // 参数验证
     *     if (n < 1 || n > 5000) {
     *         throw invalid_argument("人员数量必须在1到5000之间");
     *     }
     *     
     *     // 创建并初始化并查集
     *     UnionFind uf(n);
     *     
     *     // 处理每个亲戚关系
     *     for (const auto& relation : relations) {
     *         int a = relation.first;
     *         int b = relation.second;
     *         
     *         // 验证人员编号
     *         if (a < 1 || a > n || b < 1 || b > n) {
     *             throw invalid_argument("人员编号必须在1到" + to_string(n) + "之间");
     *         }
     *         
     *         uf.union_sets(a, b);
     *     }
     *     
     *     // 处理每个查询
     *     vector<string> results;
     *     for (const auto& query : queries) {
     *         int a = query.first;
     *         int b = query.second;
     *         
     *         // 验证人员编号
     *         if (a < 1 || a > n || b < 1 || b > n) {
     *             throw invalid_argument("人员编号必须在1到" + to_string(n) + "之间");
     *         }
     *         
     *         if (uf.isConnected(a, b)) {
     *             results.push_back("YES");
     *         } else {
     *             results.push_back("NO");
     *         }
     *     }
     *     
     *     return results;
     * }
     * 
     * /**
     * 运行测试用例
     */
     * void runTests() {
     *     cout << "===== 运行测试用例 =====" << endl;
     *     
     *     // 测试用例1：标准情况
     *     cout << "\n测试用例1：标准情况" << endl;
     *     try {
     *         int n1 = 6;
     *         vector<pair<int, int>> relations1 = {{1, 2}, {1, 5}, {3, 4}, {5, 2}, {1, 3}};
     *         vector<pair<int, int>> queries1 = {{1, 4}, {2, 3}, {5, 6}};
     *         vector<string> expected1 = {"YES", "NO", "NO"};
     *         vector<string> results1 = checkRelatives(n1, relations1, queries1);
     *         
     *         for (size_t i = 0; i < results1.size(); ++i) {
     *             cout << "查询 " << (i + 1) << "：" << results1[i] << ", 预期：" << expected1[i] << ", "
     *                  << (results1[i] == expected1[i] ? "通过" : "失败") << endl;
     *         }
     *     } catch (const exception& e) {
     *         cout << "异常：" << e.what() << endl;
     *     }
     *     
     *     // 测试用例2：没有亲戚关系
     *     cout << "\n测试用例2：没有亲戚关系" << endl;
     *     try {
     *         int n2 = 4;
     *         vector<pair<int, int>> relations2 = {};
     *         vector<pair<int, int>> queries2 = {{1, 2}, {3, 4}};
     *         vector<string> expected2 = {"NO", "NO"};
     *         vector<string> results2 = checkRelatives(n2, relations2, queries2);
     *         
     *         for (size_t i = 0; i < results2.size(); ++i) {
     *             cout << "查询 " << (i + 1) << "：" << results2[i] << ", 预期：" << expected2[i] << ", "
     *                  << (results2[i] == expected2[i] ? "通过" : "失败") << endl;
     *         }
     *     } catch (const exception& e) {
     *         cout << "异常：" << e.what() << endl;
     *     }
     *     
     *     // 测试用例3：异常处理测试
     *     cout << "\n测试用例3：异常处理测试" << endl;
     *     try {
     *         checkRelatives(0, vector<pair<int, int>>(), vector<pair<int, int>>());
     *         cout << "测试非法人员数量：失败" << endl;
     *     } catch (const invalid_argument& e) {
     *         cout << "测试非法人员数量：通过 - " << e.what() << endl;
     *     }
     * }
     * 
     * /**
     * 主函数
     */
     * int main() {
     *     // 运行测试用例
     *     runTests();
     *     
     *     // 实际程序运行部分
     *     try {
     *         int n, m, p;
     *         cin >> n >> m >> p;
     *         
     *         // 读取亲戚关系
     *         vector<pair<int, int>> relations;
     *         for (int i = 0; i < m; ++i) {
     *             int a, b;
     *             cin >> a >> b;
     *             relations.emplace_back(a, b);
     *         }
     *         
     *         // 读取查询
     *         vector<pair<int, int>> queries;
     *         for (int i = 0; i < p; ++i) {
     *             int a, b;
     *             cin >> a >> b;
     *             queries.emplace_back(a, b);
     *         }
     *         
     *         // 计算结果
     *         vector<string> results = checkRelatives(n, relations, queries);
     *         
     *         // 输出结果
     *         for (const string& result : results) {
     *             cout << result << endl;
     *         }
     *     } catch (const exception& e) {
     *         cerr << "错误：" << e.what() << endl;
     *         return 1;
     *     }
     *     
     *     return 0;
     * }
     * 
     * Python实现代码：
     * class UnionFind:
     *     """
     *     并查集类 - 用于维护集合的并查操作
     *     包含路径压缩和按秩合并优化
     *     """
     *     def __init__(self, n):
     *         """
     *         初始化并查集
     *         
     *         @param n: 节点数量
     *         @raises ValueError: 如果节点数量不合法
     *         """
     *         if n <= 0:
     *             raise ValueError("节点数量必须大于0")
     *         
     *         # 初始化父节点数组，人员编号从1开始
     *         self.parent = list(range(n + 1))
     *         # 初始化秩数组，用于按秩合并
     *         self.rank = [1] * (n + 1)
     *     
     *     def find(self, x):
     *         """
     *         查找节点x的根节点（带路径压缩）
     *         
     *         @param x: 要查找的节点
     *         @return: 节点x所在集合的根节点
     *         @raises IndexError: 如果节点超出范围
     *         """
     *         if x < 1 or x >= len(self.parent):
     *             raise IndexError(f"节点 {x} 超出范围")
     *             
     *         if self.parent[x] != x:
     *             # 路径压缩：递归地将路径上的所有节点直接连接到根节点
     *             self.parent[x] = self.find(self.parent[x])
     *         return self.parent[x]
     *     
     *     def union(self, x, y):
     *         """
     *         合并包含节点x和y的集合（按秩合并）
     *         
     *         @param x: 第一个节点
     *         @param y: 第二个节点
     *         """
     *         root_x = self.find(x)
     *         root_y = self.find(y)
     *         
     *         # 如果已经在同一个集合中，无需合并
     *         if root_x == root_y:
     *             return
     *         
     *         # 按秩合并：将秩小的树合并到秩大的树下
     *         if self.rank[root_x] > self.rank[root_y]:
     *             self.parent[root_y] = root_x
     *         elif self.rank[root_x] < self.rank[root_y]:
     *             self.parent[root_x] = root_y
     *         else:
     *             # 秩相等时，任选一个作为根，并增加其秩
     *             self.parent[root_y] = root_x
     *             self.rank[root_x] += 1
     *     
     *     def is_connected(self, x, y):
     *         """
     *         判断两个节点是否连通
     *         
     *         @param x: 第一个节点
     *         @param y: 第二个节点
     *         @return: 如果在同一个集合中返回True，否则返回False
     *         """
     *         return self.find(x) == self.find(y)
     * 
     * def check_relatives(n, relations, queries):
     *     """
     *     检查亲戚关系
     *     
     *     @param n: 人员数量
     *     @param relations: 亲戚关系列表，每个元素是两个整数的元组
     *     @param queries: 查询列表，每个元素是两个整数的元组
     *     @return: 查询结果列表，每个元素是"YES"或"NO"
     *     @raises ValueError: 如果输入参数不合法
     *     """
     *     # 参数验证
     *     if n < 1 or n > 5000:
     *         raise ValueError(f"人员数量必须在1到5000之间，当前为{n}")
     *     
     *     # 创建并初始化并查集
     *     uf = UnionFind(n)
     *     
     *     # 处理每个亲戚关系
     *     for a, b in relations:
     *         # 验证人员编号
     *         if a < 1 or a > n or b < 1 or b > n:
     *             raise ValueError(f"人员编号必须在1到{n}之间")
     *         uf.union(a, b)
     *     
     *     # 处理每个查询
     *     results = []
     *     for a, b in queries:
     *         # 验证人员编号
     *         if a < 1 or a > n or b < 1 or b > n:
     *             raise ValueError(f"人员编号必须在1到{n}之间")
     *         results.append("YES" if uf.is_connected(a, b) else "NO")
     *     
     *     return results
     * 
     * def run_basic_tests():
     *     """
     *     运行基本功能测试
     *     """
     *     print("=== 基本功能测试 ===")
     *     
     *     # 测试用例1：标准情况
     *     print("\n测试用例1：标准情况")
     *     try:
     *         n1 = 6
     *         relations1 = [(1, 2), (1, 5), (3, 4), (5, 2), (1, 3)]
     *         queries1 = [(1, 4), (2, 3), (5, 6)]
     *         expected1 = ["YES", "NO", "NO"]
     *         results1 = check_relatives(n1, relations1, queries1)
     *         
     *         for i, (r, e) in enumerate(zip(results1, expected1)):
     *             print(f"查询 {i+1}: {r}, 预期: {e}, {'通过' if r == e else '失败'}")
     *     except Exception as e:
     *         print(f"异常: {e}")
     *     
     *     # 测试用例2：所有人都是亲戚
     *     print("\n测试用例2：所有人都是亲戚")
     *     try:
     *         n2 = 5
     *         relations2 = [(1, 2), (2, 3), (3, 4), (4, 5)]
     *         queries2 = [(1, 5), (2, 4), (3, 5)]
     *         expected2 = ["YES", "YES", "YES"]
     *         results2 = check_relatives(n2, relations2, queries2)
     *         
     *         for i, (r, e) in enumerate(zip(results2, expected2)):
     *             print(f"查询 {i+1}: {r}, 预期: {e}, {'通过' if r == e else '失败'}")
     *     except Exception as e:
     *         print(f"异常: {e}")
     * 
     * def run_boundary_tests():
     *     """
     *     运行边界情况测试
     *     """
     *     print("\n=== 边界情况测试 ===")
     *     
     *     # 测试用例3：没有亲戚关系
     *     print("\n测试用例3：没有亲戚关系")
     *     try:
     *         n3 = 4
     *         relations3 = []
     *         queries3 = [(1, 2), (3, 4)]
     *         expected3 = ["NO", "NO"]
     *         results3 = check_relatives(n3, relations3, queries3)
     *         
     *         for i, (r, e) in enumerate(zip(results3, expected3)):
     *             print(f"查询 {i+1}: {r}, 预期: {e}, {'通过' if r == e else '失败'}")
     *     except Exception as e:
     *         print(f"异常: {e}")
     *     
     *     # 测试用例4：单个人
     *     print("\n测试用例4：单个人")
     *     try:
     *         n4 = 1
     *         relations4 = []
     *         queries4 = [(1, 1)]
     *         expected4 = ["YES"]
     *         results4 = check_relatives(n4, relations4, queries4)
     *         
     *         print(f"查询 1: {results4[0]}, 预期: {expected4[0]}, {'通过' if results4[0] == expected4[0] else '失败'}")
     *     except Exception as e:
     *         print(f"异常: {e}")
     * 
     * def run_special_tests():
     *     """
     *     运行特殊情况测试
     *     """
     *     print("\n=== 特殊情况测试 ===")
     *     
     *     # 测试用例5：自己查询自己
     *     print("\n测试用例5：自己查询自己")
     *     try:
     *         n5 = 3
     *         relations5 = [(1, 2)]
     *         queries5 = [(1, 1), (3, 3)]
     *         expected5 = ["YES", "YES"]
     *         results5 = check_relatives(n5, relations5, queries5)
     *         
     *         for i, (r, e) in enumerate(zip(results5, expected5)):
     *             print(f"查询 {i+1}: {r}, 预期: {e}, {'通过' if r == e else '失败'}")
     *     except Exception as e:
     *         print(f"异常: {e}")
     *     
     *     # 测试用例6：重复的亲戚关系
     *     print("\n测试用例6：重复的亲戚关系")
     *     try:
     *         n6 = 4
     *         relations6 = [(1, 2), (1, 2), (2, 3)]  # 包含重复关系
     *         queries6 = [(1, 3), (2, 4)]
     *         expected6 = ["YES", "NO"]
     *         results6 = check_relatives(n6, relations6, queries6)
     *         
     *         for i, (r, e) in enumerate(zip(results6, expected6)):
     *             print(f"查询 {i+1}: {r}, 预期: {e}, {'通过' if r == e else '失败'}")
     *     except Exception as e:
     *         print(f"异常: {e}")
     * 
     * def run_exception_tests():
     *     """
     *     运行异常处理测试
     *     """
     *     print("\n=== 异常处理测试 ===")
     *     
     *     # 测试非法的人员数量
     *     print("\n测试非法的人员数量")
     *     try:
     *         check_relatives(0, [], [])
     *         print("测试失败 - 应该抛出ValueError")
     *     except ValueError as e:
     *         print(f"测试通过 - {e}")
     *     except Exception as e:
     *         print(f"测试失败 - 抛出了错误的异常类型: {e}")
     *     
     *     # 测试非法的人员编号
     *     print("\n测试非法的人员编号")
     *     try:
     *         check_relatives(3, [(1, 4)], [])
     *         print("测试失败 - 应该抛出ValueError")
     *     except ValueError as e:
     *         print(f"测试通过 - {e}")
     *     except Exception as e:
     *         print(f"测试失败 - 抛出了错误的异常类型: {e}")
     * 
     * def run_all_tests():
     *     """
     *     运行所有测试用例
     *     """
     *     print("===== 运行所有测试用例 =====")
     *     run_basic_tests()
     *     run_boundary_tests()
     *     run_special_tests()
     *     run_exception_tests()
     * 
     * def main():
     *     """
     *     主函数 - 处理输入输出
     *     """
     *     import sys
     *     try:
     *         # 读取输入数据
     *         input_data = sys.stdin.read().split()
     *         ptr = 0
     *         
     *         # 读取基本信息
     *         n = int(input_data[ptr])
     *         m = int(input_data[ptr + 1])
     *         p = int(input_data[ptr + 2])
     *         ptr += 3
     *         
     *         # 读取亲戚关系
     *         relations = []
     *         for _ in range(m):
     *             a = int(input_data[ptr])
     *             b = int(input_data[ptr + 1])
     *             relations.append((a, b))
     *             ptr += 2
     *         
     *         # 读取查询
     *         queries = []
     *         for _ in range(p):
     *             a = int(input_data[ptr])
     *             b = int(input_data[ptr + 1])
     *             queries.append((a, b))
     *             ptr += 2
     *         
     *         # 计算结果
     *         results = check_relatives(n, relations, queries)
     *         
     *         # 输出结果
     *         for result in results:
     *             print(result)
     *             
     *     except Exception as e:
     *         print(f"错误: {e}", file=sys.stderr)
     *         sys.exit(1)
     * 
     * if __name__ == "__main__":
     *     # 运行所有测试用例
     *     run_all_tests()
     *     
     *     # 提示用户可以输入实际数据
     *     print("\n测试完成。如需运行实际数据，请输入数据（格式：n m p 然后是m行亲戚关系，再是p行查询）")
     *     print("输入示例（与测试用例1相同）:")
     *     print("6 5 3")
     *     print("1 2")
     *     print("1 5")
     *     print("3 4")
     *     print("5 2")
     *     print("1 3")
     *     print("1 4")
     *     print("2 3")
     *     print("5 6")
     *     print("\n请输入数据:")
     *     
     *     # 运行主函数处理用户输入
     *     # 注意：在实际使用时取消下面的注释
     *     # main()
     */
}