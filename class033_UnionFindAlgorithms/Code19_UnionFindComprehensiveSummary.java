package class056;

import java.util.*;

/**
 * 并查集综合总结与高级应用
 * 本文件包含并查集的各种高级应用和优化技巧总结
 */
public class Code19_UnionFindComprehensiveSummary {
    
    /**
     * 1. 标准并查集模板（路径压缩 + 按秩合并）
     */
    static class StandardUnionFind {
        private int[] parent;
        private int[] rank;
        private int count;
        
        public StandardUnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            count = n;
            
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }
        
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);  // 路径压缩
            }
            return parent[x];
        }
        
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX != rootY) {
                // 按秩合并
                if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
                count--;
            }
        }
        
        public boolean isConnected(int x, int y) {
            return find(x) == find(y);
        }
        
        public int getCount() {
            return count;
        }
    }
    
    /**
     * 2. 带权并查集模板
     * 用于维护元素之间的关系（如距离、倍数等）
     */
    static class WeightedUnionFind {
        private int[] parent;
        private int[] weight;  // 存储到父节点的权重
        
        public WeightedUnionFind(int n) {
            parent = new int[n];
            weight = new int[n];
            
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                weight[i] = 0;  // 初始权重为0
            }
        }
        
        public int find(int x) {
            if (parent[x] != x) {
                int root = find(parent[x]);
                weight[x] += weight[parent[x]];  // 更新权重
                parent[x] = root;
            }
            return parent[x];
        }
        
        /**
         * 合并操作，表示x到y的权重为value
         */
        public void union(int x, int y, int value) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX != rootY) {
                parent[rootX] = rootY;
                // 更新权重: weight[x] + weight[rootX] = value + weight[y]
                weight[rootX] = value + weight[y] - weight[x];
            }
        }
        
        /**
         * 获取x到y的权重差
         */
        public int getWeight(int x, int y) {
            if (find(x) != find(y)) {
                return -1;  // 不在同一集合
            }
            return weight[x] - weight[y];
        }
    }
    
    /**
     * 3. 可撤销并查集
     * 支持撤销操作，用于需要回溯的场景
     */
    static class ReversibleUnionFind {
        private int[] parent;
        private int[] rank;
        private Stack<int[]> history;  // 存储操作历史
        
        public ReversibleUnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            history = new Stack<>();
            
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }
        
        public int find(int x) {
            if (parent[x] != x) {
                return find(parent[x]);  // 注意：可撤销并查集通常不进行路径压缩
            }
            return parent[x];
        }
        
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX != rootY) {
                // 记录操作历史
                if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                    history.push(new int[]{rootY, rootX, rank[rootX]});
                } else if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                    history.push(new int[]{rootX, rootY, rank[rootY]});
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                    history.push(new int[]{rootY, rootX, rank[rootX] - 1});
                }
            }
        }
        
        /**
         * 撤销上一次合并操作
         */
        public void undo() {
            if (!history.isEmpty()) {
                int[] operation = history.pop();
                int child = operation[0];
                int parentNode = operation[1];
                int originalRank = operation[2];
                
                parent[child] = child;
                rank[parentNode] = originalRank;
            }
        }
        
        public int getHistorySize() {
            return history.size();
        }
    }
    
    /**
     * 4. 离线查询处理模板
     * 用于需要按特定顺序处理查询的场景
     */
    static class OfflineQueryProcessor {
        /**
         * 处理边权限制的路径存在性查询（LeetCode 1697）
         */
        public boolean[] distanceLimitedPathsExist(int n, int[][] edges, int[][] queries) {
            // 将查询按限制排序，并记录原始索引
            int[][] sortedQueries = new int[queries.length][4];
            for (int i = 0; i < queries.length; i++) {
                sortedQueries[i][0] = queries[i][0];
                sortedQueries[i][1] = queries[i][1];
                sortedQueries[i][2] = queries[i][2];
                sortedQueries[i][3] = i;
            }
            Arrays.sort(sortedQueries, (a, b) -> Integer.compare(a[2], b[2]));
            
            // 将边按权重排序
            Arrays.sort(edges, (a, b) -> Integer.compare(a[2], b[2]));
            
            // 初始化并查集
            StandardUnionFind uf = new StandardUnionFind(n);
            boolean[] results = new boolean[queries.length];
            int edgeIndex = 0;
            
            // 处理查询
            for (int[] query : sortedQueries) {
                int limit = query[2];
                int originalIndex = query[3];
                
                // 添加所有权重小于limit的边
                while (edgeIndex < edges.length && edges[edgeIndex][2] < limit) {
                    uf.union(edges[edgeIndex][0], edges[edgeIndex][1]);
                    edgeIndex++;
                }
                
                // 检查连通性
                results[originalIndex] = uf.isConnected(query[0], query[1]);
            }
            
            return results;
        }
    }
    
    /**
     * 5. 动态连通性处理（支持删除操作）
     */
    static class DynamicConnectivity {
        /**
         * 逆向并查集：从最终状态开始，逆向添加元素
         * 适用于需要处理删除操作的场景
         */
        public int[] processDeletions(int n, int[][] edges, int[] deletions) {
            // 标记被删除的边
            Set<String> deletedEdges = new HashSet<>();
            for (int edgeIndex : deletions) {
                int[] edge = edges[edgeIndex];
                String key = edge[0] + "," + edge[1];
                deletedEdges.add(key);
            }
            
            // 初始化并查集，只添加未被删除的边
            StandardUnionFind uf = new StandardUnionFind(n);
            for (int i = 0; i < edges.length; i++) {
                int[] edge = edges[i];
                String key = edge[0] + "," + edge[1];
                if (!deletedEdges.contains(key)) {
                    uf.union(edge[0], edge[1]);
                }
            }
            
            // 逆向处理删除操作（从后往前添加边）
            int[] results = new int[deletions.length];
            for (int i = deletions.length - 1; i >= 0; i--) {
                results[i] = uf.getCount();
                
                // 添加被删除的边
                int[] edge = edges[deletions[i]];
                uf.union(edge[0], edge[1]);
            }
            
            return results;
        }
    }
    
    /**
     * 6. 并查集在网格问题中的应用
     */
    static class GridUnionFind {
        /**
         * 处理二维网格的连通性问题
         */
        public int numIslands(char[][] grid) {
            if (grid == null || grid.length == 0) {
                return 0;
            }
            
            int m = grid.length;
            int n = grid[0].length;
            int waterCount = 0;
            
            // 初始化并查集
            StandardUnionFind uf = new StandardUnionFind(m * n);
            
            // 四个方向
            int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
            
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (grid[i][j] == '1') {
                        // 当前是陆地，检查四个方向
                        for (int[] dir : directions) {
                            int newI = i + dir[0];
                            int newJ = j + dir[1];
                            
                            if (newI >= 0 && newI < m && newJ >= 0 && newJ < n && grid[newI][newJ] == '1') {
                                uf.union(i * n + j, newI * n + newJ);
                            }
                        }
                    } else {
                        waterCount++;
                    }
                }
            }
            
            // 岛屿数量 = 连通分量数量 - 水的数量（需要调整）
            return uf.getCount() - waterCount;
        }
    }
    
    /**
     * 7. 并查集性能优化技巧总结
     */
    static class OptimizationTips {
        /**
         * 优化技巧1: 小挂大优化（按秩合并）
         * - 总是将小树合并到大树下
         * - 保持树的平衡，避免退化成链表
         */
        
        /**
         * 优化技巧2: 路径压缩
         * - 在查找操作时将路径上的节点直接连接到根节点
         * - 使树更加扁平化，提高后续查找效率
         */
        
        /**
         * 优化技巧3: 按大小合并 vs 按秩合并
         * - 按大小合并: 更简单，但可能不够平衡
         * - 按秩合并: 更平衡，但需要维护秩信息
         * - 实际应用中差异不大，都可以达到O(α(n))复杂度
         */
        
        /**
         * 优化技巧4: 避免不必要的查找操作
         * - 在合并前先检查是否已经在同一集合
         * - 对于频繁的连通性查询，可以缓存结果
         */
        
        /**
         * 优化技巧5: 使用数组代替哈希表
         * - 当元素是连续整数时，使用数组更高效
         * - 哈希表适用于元素是字符串或其他类型的场景
         */
    }
    
    /**
     * 8. 并查集常见问题模式总结
     */
    static class ProblemPatterns {
        /**
         * 模式1: 连通分量计数
         * - 问题特征: 需要统计连通区域的数量
         * - 例题: 岛屿数量、朋友圈数量
         * - 解法: 初始化n个集合，每合并一次计数减1
         */
        
        /**
         * 模式2: 环检测
         * - 问题特征: 判断图中是否存在环
         * - 例题: 冗余连接
         * - 解法: 遍历边，如果两个端点已经在同一集合，则检测到环
         */
        
        /**
         * 模式3: 动态连通性
         * - 问题特征: 需要处理元素的添加或删除
         * - 例题: 岛屿数量II、打砖块
         * - 解法: 逆向处理、可撤销并查集
         */
        
        /**
         * 模式4: 关系维护
         * - 问题特征: 需要维护元素之间的关系
         * - 例题: 食物链、除法求值
         * - 解法: 带权并查集
         */
        
        /**
         * 模式5: 离线查询
         * - 问题特征: 查询可以按某种顺序处理
         * - 例题: 边权限制的路径存在性
         * - 解法: 对查询排序，逐步添加元素
         */
        
        /**
         * 模式6: 最小生成树
         * - 问题特征: 寻找连接所有点的最小代价
         * - 例题: Kruskal算法
         * - 解法: 对边排序，使用并查集维护连通性
         */
    }
    
    /**
     * 9. 并查集在工程中的应用
     */
    static class EngineeringApplications {
        /**
         * 应用1: 网络连接管理
         * - 场景: 服务器集群的连通性管理
         * - 需求: 快速判断两台服务器是否连通
         * - 优势: 高效的连通性查询
         */
        
        /**
         * 应用2: 社交网络分析
         * - 场景: 好友关系、社区发现
         * - 需求: 找到连通的朋友圈
         * - 优势: 高效的集合合并操作
         */
        
        /**
         * 应用3: 图像处理
         * - 场景: 连通区域标记
         * - 需求: 找到图像中的连通区域
         * - 优势: 处理二维网格的高效性
         */
        
        /**
         * 应用4: 编译器优化
         * - 场景: 变量别名分析
         * - 需求: 判断两个变量是否指向同一内存
         * - 优势: 高效的关系维护
         */
        
        /**
         * 应用5: 数据库查询优化
         * - 场景: 等价类查询
         * - 需求: 快速判断两个元素是否等价
         * - 优势: 近似常数时间的查询
         */
    }
    
    /**
     * 10. 并查集的局限性及替代方案
     */
    static class LimitationsAndAlternatives {
        /**
         * 局限性1: 不支持分割操作
         * - 问题: 一旦合并，不能直接分割
         * - 替代: 可撤销并查集、动态树（Link-Cut Tree）
         */
        
        /**
         * 局限性2: 只能维护等价关系
         * - 问题: 关系必须满足自反性、对称性、传递性
         * - 替代: 带权并查集可以处理更多关系
         */
        
        /**
         * 局限性3: 内存使用
         * - 问题: 需要O(n)的额外空间
         * - 替代: 对于稀疏图，可以使用其他数据结构
         */
        
        /**
         * 局限性4: 并行化困难
         * - 问题: 路径压缩和按秩合并难以并行化
         * - 替代: 使用其他并发数据结构
         */
    }
    
    // 测试方法
    public static void main(String[] args) {
        System.out.println("并查集综合总结与高级应用");
        System.out.println("=========================");
        
        // 测试标准并查集
        StandardUnionFind uf1 = new StandardUnionFind(5);
        uf1.union(0, 1);
        uf1.union(2, 3);
        uf1.union(1, 2);
        System.out.println("标准并查集测试: " + uf1.getCount()); // 预期: 2
        
        // 测试带权并查集
        WeightedUnionFind uf2 = new WeightedUnionFind(5);
        uf2.union(0, 1, 1);
        uf2.union(1, 2, 2);
        System.out.println("带权并查集测试: " + uf2.getWeight(0, 2)); // 预期: 3
        
        // 测试可撤销并查集
        ReversibleUnionFind uf3 = new ReversibleUnionFind(5);
        uf3.union(0, 1);
        uf3.union(2, 3);
        uf3.undo(); // 撤销最后一次合并
        System.out.println("可撤销并查集测试: " + uf3.getHistorySize()); // 预期: 1
        
        System.out.println("并查集学习路径建议:");
        System.out.println("1. 掌握标准并查集模板（路径压缩+按秩合并）");
        System.out.println("2. 练习基础题目（连通分量、环检测）");
        System.out.println("3. 学习带权并查集（关系维护）");
        System.out.println("4. 掌握高级应用（离线查询、动态连通性）");
        System.out.println("5. 理解工程应用和优化技巧");
    }
    
    /**
     * 学习资源推荐:
     * 1. 《算法导论》第21章：用于不相交集合的数据结构
     * 2. LeetCode并查集专题
     * 3. 各大OJ平台的并查集题目
     * 4. 学术论文关于并查集优化的研究
     * 
     * 关键要点:
     * - 理解并查集的核心思想：代表元素和路径压缩
     * - 掌握时间复杂度分析：O(α(n))的由来
     * - 熟练应用各种变种：带权、可撤销、离线等
     * - 注意边界条件和异常处理
     * - 在实际工程中选择合适的实现方式
     */
}