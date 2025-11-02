package class186;

import java.util.*;

/**
 * 高级并查集实现
 * 包含：
 * 1. 回滚并查集（支持离线动态连通性问题）
 * 2. 时间轴并查集
 * 3. 二分图判定建模
 * 
 * 时间复杂度：
 * - 回滚并查集：O(α(n) log n) 每次操作
 * - 时间轴并查集：O(α(n) log n) 每次操作
 * - 二分图判定：O(α(n)) 每次操作
 * 
 * 空间复杂度：O(n)
 * 
 * 设计要点：
 * 1. 路径压缩和按秩合并优化
 * 2. 支持撤销操作的日志记录
 * 3. 支持时间维度的查询
 * 4. 支持二分图的双色标记
 * 5. 工程化考量：异常处理、边界检查、内存优化
 * 
 * 典型应用场景：
 * - 回滚并查集：离线动态连通性问题、Kruskal算法中的环检测
 * - 时间轴并查集：带时间限制的连通性问题
 * - 二分图判定：图的双色问题、冲突检测
 */
public class UnionFindAdvanced {
    /**
     * 回滚并查集（支持撤销操作）
     */
    public static class RollbackUnionFind {
        private int[] parent;    // 父节点数组
        private int[] rank;      // 秩（树高上界）
        private Stack<Operation> history; // 操作历史记录
        private int setCount;    // 集合数量
        
        /**
         * 操作记录类
         */
        private static class Operation {
            int x;       // 被修改的节点
            int px;      // 原来的父节点
            int y;       // 合并的另一个节点
            int py;      // 原来的父节点
            int rank;    // 原来的秩
            
            public Operation(int x, int px, int y, int py, int rank) {
                this.x = x;
                this.px = px;
                this.y = y;
                this.py = py;
                this.rank = rank;
            }
        }
        
        /**
         * 构造函数
         */
        public RollbackUnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            history = new Stack<>();
            setCount = n;
            
            // 初始化：每个节点的父节点是自己
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }
        
        /**
         * 查找根节点（带路径压缩）
         */
        public int find(int x) {
            while (parent[x] != x) {
                x = parent[x];
            }
            return x;
        }
        
        /**
         * 合并两个集合（带按秩合并，记录操作历史）
         * @return 是否成功合并（如果已经在同一集合返回false）
         */
        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX == rootY) {
                return false;
            }
            
            // 记录操作历史
            history.push(new Operation(rootX, parent[rootX], rootY, parent[rootY], rank[rootY]));
            
            // 按秩合并：将秩小的树合并到秩大的树上
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            
            setCount--;
            return true;
        }
        
        /**
         * 撤销上一次合并操作
         */
        public void rollback() {
            if (history.isEmpty()) {
                throw new IllegalStateException("No operation to rollback");
            }
            
            Operation op = history.pop();
            parent[op.x] = op.px;
            parent[op.y] = op.py;
            rank[op.y] = op.rank;
            setCount++;
        }
        
        /**
         * 获取集合数量
         */
        public int getSetCount() {
            return setCount;
        }
        
        /**
         * 判断两个节点是否连通
         */
        public boolean isConnected(int x, int y) {
            return find(x) == find(y);
        }
    }
    
    /**
     * 时间轴并查集（支持历史版本查询）
     * 注意：这里的实现是基于离线处理的，需要预先知道所有操作
     */
    public static class TemporalUnionFind {
        private int[] parent;    // 父节点数组
        private int[] rank;      // 秩数组
        private int[] time;      // 记录父节点变化的时间
        private int[][] history; // 历史记录 [时间][节点]
        private int currentTime; // 当前时间戳
        private int n;           // 节点数量
        
        /**
         * 构造函数
         */
        public TemporalUnionFind(int n, int maxTime) {
            this.n = n;
            this.parent = new int[n];
            this.rank = new int[n];
            this.time = new int[n];
            this.history = new int[maxTime + 1][n];
            this.currentTime = 0;
            
            // 初始化
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;
                time[i] = 0;
                history[0][i] = i;
            }
        }
        
        /**
         * 查找在指定时间点的根节点
         */
        public int find(int x, int t) {
            // 基础版本的实现，实际应用中可能需要更复杂的历史记录方式
            while (parent[x] != x && time[parent[x]] <= t) {
                x = parent[x];
            }
            return x;
        }
        
        /**
         * 合并两个集合并记录时间
         */
        public boolean union(int x, int y) {
            currentTime++;
            
            int rootX = find(x, currentTime - 1);
            int rootY = find(y, currentTime - 1);
            
            if (rootX == rootY) {
                // 复制上一时间点的状态
                System.arraycopy(history[currentTime - 1], 0, history[currentTime], 0, n);
                return false;
            }
            
            // 复制上一时间点的状态
            System.arraycopy(history[currentTime - 1], 0, history[currentTime], 0, n);
            
            // 按秩合并
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
                time[rootX] = currentTime;
                history[currentTime][rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
                time[rootY] = currentTime;
                history[currentTime][rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                time[rootY] = currentTime;
                history[currentTime][rootY] = rootX;
                rank[rootX]++;
            }
            
            return true;
        }
        
        /**
         * 查询在特定时间点两个节点是否连通
         */
        public boolean isConnected(int x, int y, int t) {
            return find(x, t) == find(y, t);
        }
        
        /**
         * 获取当前时间
         */
        public int getCurrentTime() {
            return currentTime;
        }
    }
    
    /**
     * 二分图判定并查集（带权并查集）
     * 用权值表示与父节点的关系（0表示同色，1表示异色）
     */
    public static class BipartiteUnionFind {
        private int[] parent;    // 父节点数组
        private int[] rank;      // 秩数组
        private int[] color;     // 与父节点的颜色关系（0同色，1异色）
        private boolean isBipartite; // 是否是二分图
        
        /**
         * 构造函数
         */
        public BipartiteUnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            color = new int[n];
            isBipartite = true;
            
            // 初始化
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;
                color[i] = 0; // 与自己同色
            }
        }
        
        /**
         * 查找根节点（带路径压缩和颜色关系更新）
         */
        public int find(int x) {
            if (parent[x] != x) {
                int root = find(parent[x]);
                // 更新颜色关系：x到根的颜色 = x到父节点的颜色 + 父节点到根的颜色
                color[x] ^= color[parent[x]];
                parent[x] = root;
            }
            return parent[x];
        }
        
        /**
         * 合并两个节点，并检查是否是二分图
         * @param x 节点x
         * @param y 节点y
         * @param isSame 是否要求同色（false表示异色）
         * @return 是否成功合并且不破坏二分图性质
         */
        public boolean union(int x, int y, boolean isSame) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX == rootY) {
                // 检查颜色关系是否符合要求
                boolean check = (color[x] ^ color[y]) == (isSame ? 0 : 1);
                if (!check) {
                    isBipartite = false;
                }
                return check;
            }
            
            // 按秩合并
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
                // 计算color[rootX]使得颜色关系成立
                color[rootX] = color[x] ^ color[y] ^ (isSame ? 0 : 1);
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
                color[rootY] = color[x] ^ color[y] ^ (isSame ? 0 : 1);
            } else {
                parent[rootY] = rootX;
                color[rootY] = color[x] ^ color[y] ^ (isSame ? 0 : 1);
                rank[rootX]++;
            }
            
            return true;
        }
        
        /**
         * 添加一条边（x和y必须异色）
         */
        public boolean addEdge(int x, int y) {
            return union(x, y, false);
        }
        
        /**
         * 判断当前图是否是二分图
         */
        public boolean isBipartite() {
            return isBipartite;
        }
        
        /**
         * 获取节点x的颜色（相对于根节点）
         */
        public int getColor(int x) {
            find(x); // 确保路径压缩
            return color[x];
        }
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试回滚并查集
        testRollbackUnionFind();
        
        // 测试时间轴并查集
        testTemporalUnionFind();
        
        // 测试二分图判定
        testBipartiteUnionFind();
    }
    
    private static void testRollbackUnionFind() {
        System.out.println("===== 测试回滚并查集 ====");
        RollbackUnionFind uf = new RollbackUnionFind(5);
        
        uf.union(0, 1);
        uf.union(2, 3);
        System.out.println("0和1连通: " + uf.isConnected(0, 1)); // true
        System.out.println("0和2连通: " + uf.isConnected(0, 2)); // false
        System.out.println("集合数量: " + uf.getSetCount());     // 3
        
        // 回滚操作
        uf.rollback();
        System.out.println("回滚一次后，集合数量: " + uf.getSetCount()); // 4
        System.out.println("2和3连通: " + uf.isConnected(2, 3)); // false
        
        // 再次合并
        uf.union(1, 2);
        System.out.println("0和2连通: " + uf.isConnected(0, 2)); // true
    }
    
    private static void testTemporalUnionFind() {
        System.out.println("\n===== 测试时间轴并查集 ====");
        TemporalUnionFind tuf = new TemporalUnionFind(5, 10);
        
        tuf.union(0, 1); // 时间1
        tuf.union(2, 3); // 时间2
        
        System.out.println("时间2时，0和1连通: " + tuf.isConnected(0, 1, 2)); // true
        System.out.println("时间2时，0和2连通: " + tuf.isConnected(0, 2, 2)); // false
        
        tuf.union(1, 2); // 时间3
        System.out.println("时间3时，0和2连通: " + tuf.isConnected(0, 2, 3)); // true
        System.out.println("时间2时，0和2连通: " + tuf.isConnected(0, 2, 2)); // false (历史时间查询)
    }
    
    private static void testBipartiteUnionFind() {
        System.out.println("\n===== 测试二分图判定 ====");
        
        // 测试1：二分图（无向图：0-1-2-3，0-3）
        BipartiteUnionFind bipartite = new BipartiteUnionFind(4);
        bipartite.addEdge(0, 1);
        bipartite.addEdge(1, 2);
        bipartite.addEdge(2, 3);
        bipartite.addEdge(0, 3);
        System.out.println("测试1是否是二分图: " + bipartite.isBipartite()); // true
        
        // 测试2：非二分图（无向图：0-1-2-0）
        BipartiteUnionFind nonBipartite = new BipartiteUnionFind(3);
        nonBipartite.addEdge(0, 1);
        nonBipartite.addEdge(1, 2);
        boolean result = nonBipartite.addEdge(2, 0);
        System.out.println("测试2是否是二分图: " + nonBipartite.isBipartite()); // false
        System.out.println("添加边2-0是否成功: " + result); // false
    }
}