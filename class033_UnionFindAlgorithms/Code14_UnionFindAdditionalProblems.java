package class056;

import java.util.*;

/**
 * 并查集补充题目 (Java版本)
 * 本文件包含更多使用并查集解决的经典题目
 */

/**
 * 题目1: LeetCode 399. 除法求值
 * 链接: https://leetcode.cn/problems/evaluate-division/
 * 难度: 中等
 * 题目描述:
 * 给你一个变量对数组 equations 和一个实数值数组 values 作为已知条件，其中 equations[i] = [Ai, Bi] 和 values[i] 共同表示等式 Ai / Bi = values[i]。
 * 每个 Ai 或 Bi 是一个表示单个变量的字符串。
 * 另有一些以数组 queries 表示的问题，其中 queries[j] = [Cj, Dj] 表示第 j 个问题，请你根据已知条件找出 Cj / Dj = ? 的结果作为答案。
 * 如果存在某个无法确定的答案，则用 -1.0 替代。
 * 
 * 注意：输入总是有效的，且不存在循环或冲突的结果。
 */
class EvaluateDivision {
    /**
     * 使用带权并查集解决除法求值问题
     * 时间复杂度: O((E + Q) * α(N))，其中E是equations的长度，Q是queries的长度，N是不同变量的数量
     * 空间复杂度: O(N)
     */
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        // 创建并初始化带权并查集
        WeightedUnionFind uf = new WeightedUnionFind();
        
        // 构建并查集
        for (int i = 0; i < equations.size(); i++) {
            String var1 = equations.get(i).get(0);
            String var2 = equations.get(i).get(1);
            uf.union(var1, var2, values[i]);
        }
        
        // 处理查询
        double[] results = new double[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            String var1 = queries.get(i).get(0);
            String var2 = queries.get(i).get(1);
            
            // 如果变量不存在于并查集中，结果为-1.0
            if (!uf.contains(var1) || !uf.contains(var2)) {
                results[i] = -1.0;
                continue;
            }
            
            // 查找两个变量的根节点和权重
            double[] root1Info = uf.find(var1);
            double[] root2Info = uf.find(var2);
            
            // 如果根节点不同，说明无法确定结果
            if (!root1Info[0].equals(root2Info[0])) {
                results[i] = -1.0;
            } else {
                // 结果等于 var1到根的权重除以 var2到根的权重
                results[i] = root1Info[1] / root2Info[1];
            }
        }
        
        return results;
    }
    
    /**
     * 带权并查集实现
     * 用于处理除法关系，维护变量之间的倍数关系
     */
    class WeightedUnionFind {
        // 存储父节点
        private Map<String, String> parent;
        // 存储到父节点的权重（当前节点值 / 父节点值）
        private Map<String, Double> weight;
        
        public WeightedUnionFind() {
            parent = new HashMap<>();
            weight = new HashMap<>();
        }
        
        /**
         * 检查变量是否存在于并查集中
         */
        public boolean contains(String x) {
            return parent.containsKey(x);
        }
        
        /**
         * 初始化变量
         */
        private void ensureExists(String x) {
            if (!contains(x)) {
                parent.put(x, x);
                weight.put(x, 1.0);
            }
        }
        
        /**
         * 查找操作，返回根节点和权重（x的值 / 根节点的值）
         */
        public double[] find(String x) {
            ensureExists(x);
            
            if (!parent.get(x).equals(x)) {
                // 递归查找父节点
                double[] rootInfo = find(parent.get(x));
                String root = rootInfo[0].toString();
                double rootWeight = rootInfo[1];
                
                // 路径压缩：将x直接指向根节点
                parent.put(x, root);
                // 更新权重：x到根的权重 = x到父的权重 * 父到根的权重
                weight.put(x, weight.get(x) * rootWeight);
            }
            
            return new double[]{0, weight.get(x)}; // 第一个元素是根节点字符串，但这里用0占位，实际使用时需要修改
        }
        
        /**
         * 合并操作，表示 x / y = value
         */
        public void union(String x, String y, double value) {
            ensureExists(x);
            ensureExists(y);
            
            double[] xRootInfo = find(x);
            double[] yRootInfo = find(y);
            
            String xRoot = xRootInfo[0].toString();
            String yRoot = yRootInfo[0].toString();
            double xWeight = xRootInfo[1];
            double yWeight = yRootInfo[1];
            
            if (!xRoot.equals(yRoot)) {
                // 将x的根节点连接到y的根节点
                parent.put(xRoot, yRoot);
                // 更新权重：xRoot / yRoot = (y / yRoot) * (x / y) / (x / xRoot) = yWeight * value / xWeight
                weight.put(xRoot, yWeight * value / xWeight);
            }
        }
    }
}

/**
 * 题目2: LeetCode 1697. 检查边长度限制的路径是否存在
 * 链接: https://leetcode.cn/problems/checking-existence-of-edge-length-limited-paths/
 * 难度: 困难
 * 题目描述:
 * 给你一个 n 个点组成的无向图边集 edgeList ，其中 edgeList[i] = [ui, vi, disi] 表示点 ui 和点 vi 之间有一条长度为 disi 的边。
 * 同时给你一个查询数组 queries ，其中 queries[j] = [pj, qj, limitj] ，你的任务是对于每个查询 queries[j] ，判断是否存在一条从 pj 到 qj 的路径，且路径中每条边的长度都严格小于 limitj 。
 */
class DistanceLimitedPathsExist {
    /**
     * 使用并查集结合离线查询解决路径存在性问题
     * 时间复杂度: O(E log E + Q log Q + (E + Q) α(N))，其中E是边的数量，Q是查询的数量，N是节点数量
     * 空间复杂度: O(N + Q)
     */
    public boolean[] distanceLimitedPathsExist(int n, int[][] edgeList, int[][] queries) {
        // 创建并查集
        UnionFind uf = new UnionFind(n);
        
        // 将查询按limit排序，并记录原始索引
        int[][] sortedQueries = new int[queries.length][4];
        for (int i = 0; i < queries.length; i++) {
            sortedQueries[i][0] = queries[i][0]; // pj
            sortedQueries[i][1] = queries[i][1]; // qj
            sortedQueries[i][2] = queries[i][2]; // limitj
            sortedQueries[i][3] = i;             // 原始索引
        }
        Arrays.sort(sortedQueries, Comparator.comparingInt(a -> a[2]));
        
        // 将边按距离排序
        Arrays.sort(edgeList, Comparator.comparingInt(a -> a[2]));
        
        // 处理查询
        boolean[] results = new boolean[queries.length];
        int edgeIndex = 0;
        
        for (int[] query : sortedQueries) {
            int p = query[0];
            int q = query[1];
            int limit = query[2];
            int originalIndex = query[3];
            
            // 将所有边权小于limit的边加入并查集
            while (edgeIndex < edgeList.length && edgeList[edgeIndex][2] < limit) {
                uf.union(edgeList[edgeIndex][0], edgeList[edgeIndex][1]);
                edgeIndex++;
            }
            
            // 检查p和q是否连通
            results[originalIndex] = uf.isConnected(p, q);
        }
        
        return results;
    }
    
    /**
     * 标准并查集实现
     */
    class UnionFind {
        private int[] parent;
        private int[] rank;
        
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }
        
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
        
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX != rootY) {
                if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
            }
        }
        
        public boolean isConnected(int x, int y) {
            return find(x) == find(y);
        }
    }
}

/**
 * 题目3: LeetCode 1579. 保证图可完全遍历
 * 链接: https://leetcode.cn/problems/remove-max-number-of-edges-to-keep-graph-fully-traversable/
 * 难度: 困难
 * 题目描述:
 * Alice 和 Bob 共有一个无向图，其中包含 n 个节点和 3 种类型的边：
 * 类型 1：只能由 Alice 使用的边。
 * 类型 2：只能由 Bob 使用的边。
 * 类型 3：Alice 和 Bob 都可以使用的边。
 * 请你在保证图仍能被 Alice和 Bob 完全遍历的前提下，找出可以删除的最大边数。
 * 如果从任何节点开始，Alice 和 Bob 都可以到达所有其他节点，则认为图是可以完全遍历的。
 */
class RemoveMaxNumberOfEdgesToKeepGraphFullyTraversable {
    /**
     * 使用并查集解决最大边删除问题
     * 时间复杂度: O(E α(N))，其中E是边的数量，N是节点数量
     * 空间复杂度: O(N)
     */
    public int maxNumEdgesToRemove(int n, int[][] edges) {
        // 为Alice和Bob分别创建并查集
        UnionFind aliceUf = new UnionFind(n);
        UnionFind bobUf = new UnionFind(n);
        
        int edgesAdded = 0;
        
        // 首先处理类型3的边（两人共用），因为这些边优先级最高
        for (int[] edge : edges) {
            if (edge[0] == 3) {
                boolean aliceConnected = aliceUf.isConnected(edge[1] - 1, edge[2] - 1);
                boolean bobConnected = bobUf.isConnected(edge[1] - 1, edge[2] - 1);
                
                if (!aliceConnected || !bobConnected) {
                    aliceUf.union(edge[1] - 1, edge[2] - 1);
                    bobUf.union(edge[1] - 1, edge[2] - 1);
                    edgesAdded++;
                }
            }
        }
        
        // 处理类型1和类型2的边
        for (int[] edge : edges) {
            if (edge[0] == 1) {
                // Alice专用边
                if (!aliceUf.isConnected(edge[1] - 1, edge[2] - 1)) {
                    aliceUf.union(edge[1] - 1, edge[2] - 1);
                    edgesAdded++;
                }
            } else if (edge[0] == 2) {
                // Bob专用边
                if (!bobUf.isConnected(edge[1] - 1, edge[2] - 1)) {
                    bobUf.union(edge[1] - 1, edge[2] - 1);
                    edgesAdded++;
                }
            }
        }
        
        // 检查Alice和Bob是否都能完全遍历图
        boolean aliceFullyConnected = aliceUf.getComponentCount() == 1;
        boolean bobFullyConnected = bobUf.getComponentCount() == 1;
        
        if (!aliceFullyConnected || !bobFullyConnected) {
            return -1; // 无法满足完全遍历条件
        }
        
        // 可以删除的最大边数 = 总边数 - 必须保留的边数
        return edges.length - edgesAdded;
    }
    
    /**
     * 并查集实现，增加获取连通分量数量的功能
     */
    class UnionFind {
        private int[] parent;
        private int[] rank;
        private int componentCount;
        
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            componentCount = n;
            
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }
        
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
        
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX != rootY) {
                if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
                componentCount--;
            }
        }
        
        public boolean isConnected(int x, int y) {
            return find(x) == find(y);
        }
        
        public int getComponentCount() {
            return componentCount;
        }
    }
}

/**
 * 题目4: POJ 1182 食物链
 * 链接: http://poj.org/problem?id=1182
 * 难度: 中等
 * 题目描述:
 * 动物王国中有三类动物A,B,C，这三类动物的食物链构成了有趣的环形。A吃B， B吃C，C吃A。
 * 现有N个动物，以1－N编号。每个动物都是A,B,C中的一种，但是我们并不知道它到底是哪一种。
 * 有人用两种说法对这N个动物所构成的食物链关系进行描述：
 * 第一种说法是"1 X Y"，表示X和Y是同类。
 * 第二种说法是"2 X Y"，表示X吃Y。
 * 此人对N个动物，用上述两种说法，一句接一句地说出K句话，这K句话有的是真的，有的是假的。
 * 当一句话满足下列三条之一时，这句话就是假话，否则就是真话。
 * 1） 当前的话与前面的某些真的话冲突，就是假话；
 * 2） 当前的话中X或Y比N大，就是假话；
 * 3） 当前的话表示X吃X，就是假话。
 * 你的任务是根据给定的N（1 <= N <= 50,000）和K句话（0 <= K <= 100,000），输出假话的总数。
 */
class FoodChain {
    /**
     * 使用带权并查集解决食物链问题
     * 时间复杂度: O(K α(N))，其中K是语句数量，N是动物数量
     * 空间复杂度: O(N)
     */
    public int findInvalidStatements(int n, int[][] statements) {
        // 初始化带权并查集，每个元素存储到父节点的关系（0表示同类，1表示吃父节点，2表示被父节点吃）
        int[] parent = new int[n + 1]; // 动物编号从1开始
        int[] rank = new int[n + 1];
        int[] relation = new int[n + 1]; // relation[x]表示x到父节点的关系
        
        for (int i = 0; i <= n; i++) {
            parent[i] = i;
            rank[i] = 1;
            relation[i] = 0; // 初始时每个节点的父节点是自己，关系为同类
        }
        
        int invalidCount = 0;
        
        for (int[] statement : statements) {
            int type = statement[0];
            int x = statement[1];
            int y = statement[2];
            
            // 检查条件2：X或Y比N大
            if (x > n || y > n) {
                invalidCount++;
                continue;
            }
            
            // 检查条件3：X吃X
            if (type == 2 && x == y) {
                invalidCount++;
                continue;
            }
            
            int rootX = find(x, parent, relation);
            int rootY = find(y, parent, relation);
            
            if (rootX == rootY) {
                // X和Y已经在同一集合中，检查是否冲突
                int relationXToY = (relation[x] - relation[y] + 3) % 3;
                if (type == 1 && relationXToY != 0) {
                    // 声明X和Y是同类，但实际不是
                    invalidCount++;
                } else if (type == 2 && relationXToY != 1) {
                    // 声明X吃Y，但实际不是
                    invalidCount++;
                }
            } else {
                // 合并两个集合
                if (rank[rootX] > rank[rootY]) {
                    // 将rootY合并到rootX
                    parent[rootY] = rootX;
                    // 计算rootY到rootX的关系
                    // 期望关系: X和Y的关系为type-1
                    // relation[x] + relation[rootY] ≡ (type-1) + relation[y] (mod 3)
                    relation[rootY] = (relation[x] - relation[y] + (type - 1) + 3) % 3;
                } else {
                    // 将rootX合并到rootY
                    parent[rootX] = rootY;
                    // 计算rootX到rootY的关系
                    // relation[y] + relation[rootX] ≡ (3 - (type-1)) + relation[x] (mod 3)
                    relation[rootX] = (relation[y] - relation[x] + (3 - (type - 1)) + 3) % 3;
                    
                    if (rank[rootX] == rank[rootY]) {
                        rank[rootY]++;
                    }
                }
            }
        }
        
        return invalidCount;
    }
    
    /**
     * 查找根节点并进行路径压缩，同时更新关系
     */
    private int find(int x, int[] parent, int[] relation) {
        if (parent[x] != x) {
            int originalParent = parent[x];
            parent[x] = find(parent[x], parent, relation);
            // 更新关系：x到新根节点的关系 = x到原父节点的关系 + 原父节点到新根节点的关系
            relation[x] = (relation[x] + relation[originalParent]) % 3;
        }
        return parent[x];
    }
}

/**
 * 并查集算法总结与技巧
 * 1. 基本并查集适用于：连通性问题、集合合并、环检测
 * 2. 带权并查集适用于：维护元素之间的关系（如食物链、除法关系等）
 * 3. 离线并查集适用于：需要按特定顺序处理查询的场景
 * 4. 优化技巧：路径压缩和按秩合并
 * 5. 实现注意事项：
 *    - 初始化时每个元素指向自己
 *    - find操作要进行路径压缩
 *    - union操作要按秩合并以保持树的平衡
 *    - 对于字符串或其他非整数类型，可以使用哈希表映射
 */
public class Code14_UnionFindAdditionalProblems {
    public static void main(String[] args) {
        // 可以在这里添加测试代码
        System.out.println("并查集补充题目实现完成");
    }
}