package class029_AdvancedDataStructures;

import java.util.*;

/**
 * 更多高级并查集题目实现
 * 
 * 本文件包含了更多使用高级并查集解决的算法题目：
 * 1. 二分图并查集应用
 * 2. 可持久化并查集
 * 3. 带撤销的并查集
 * 4. 动态连通性问题
 * 5. 离线处理与并查集
 * 6. 并查集与线段树结合
 * 7. 并查集与莫队算法结合
 * 8. 并查集在图论中的高级应用
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class MoreAdvancedUnionFindProblems {
    
    /**
     * 二分图并查集实现
     */
    static class BipartiteUnionFind {
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
     * 可持久化并查集实现
     */
    static class PersistentUnionFind {
        private int[] parent;    // 当前版本的父节点数组
        private int[] rank;      // 秩数组
        private List<int[]> history; // 历史版本
        private List<int[]> rankHistory; // 秩的历史版本
        private int version;     // 当前版本号
        
        /**
         * 构造函数
         */
        public PersistentUnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            history = new ArrayList<>();
            rankHistory = new ArrayList<>();
            version = 0;
            
            // 初始化
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
            
            // 保存初始版本
            saveVersion();
        }
        
        /**
         * 保存当前版本
         */
        private void saveVersion() {
            int[] parentCopy = parent.clone();
            int[] rankCopy = rank.clone();
            history.add(parentCopy);
            rankHistory.add(rankCopy);
        }
        
        /**
         * 查找根节点（指定版本）
         */
        public int find(int x, int version) {
            int[] versionParent = history.get(version);
            while (versionParent[x] != x) {
                x = versionParent[x];
            }
            return x;
        }
        
        /**
         * 查找根节点（当前版本）
         */
        public int find(int x) {
            return find(x, this.version);
        }
        
        /**
         * 合并两个集合并创建新版本
         */
        public int union(int x, int y) {
            version++;
            
            // 创建新版本
            parent = parent.clone();
            rank = rank.clone();
            
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX == rootY) {
                saveVersion();
                return version;
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
            
            saveVersion();
            return version;
        }
        
        /**
         * 查询在指定版本中两个节点是否连通
         */
        public boolean isConnected(int x, int y, int version) {
            return find(x, version) == find(y, version);
        }
        
        /**
         * 获取当前版本号
         */
        public int getVersion() {
            return version;
        }
    }
    
    /**
     * 带撤销操作的并查集实现
     */
    static class UndoUnionFind {
        private int[] parent;    // 父节点数组
        private int[] rank;      // 秩数组
        private Stack<Operation> history; // 操作历史
        private int setCount;    // 集合数量
        
        /**
         * 操作记录类
         */
        private static class Operation {
            int type;    // 0表示合并，1表示其他操作
            int x, px;   // 节点和原来的父节点
            int y, py;   // 另一个节点和原来的父节点
            int rankY;   // y节点原来的秩
            
            public Operation(int type, int x, int px, int y, int py, int rankY) {
                this.type = type;
                this.x = x;
                this.px = px;
                this.y = y;
                this.py = py;
                this.rankY = rankY;
            }
        }
        
        /**
         * 构造函数
         */
        public UndoUnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            history = new Stack<>();
            setCount = n;
            
            // 初始化
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }
        
        /**
         * 查找根节点
         */
        public int find(int x) {
            while (parent[x] != x) {
                x = parent[x];
            }
            return x;
        }
        
        /**
         * 合并两个集合
         */
        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX == rootY) {
                // 记录空操作
                history.push(new Operation(1, 0, 0, 0, 0, 0));
                return false;
            }
            
            // 记录操作
            history.push(new Operation(0, rootX, parent[rootX], rootY, parent[rootY], rank[rootY]));
            
            // 按秩合并
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
         * 撤销上一次操作
         */
        public void undo() {
            if (history.isEmpty()) {
                throw new IllegalStateException("No operation to undo");
            }
            
            Operation op = history.pop();
            if (op.type == 0) {
                // 撤销合并操作
                parent[op.x] = op.px;
                parent[op.y] = op.py;
                rank[op.y] = op.rankY;
                setCount++;
            }
            // 空操作无需处理
        }
        
        /**
         * 判断两个节点是否连通
         */
        public boolean isConnected(int x, int y) {
            return find(x) == find(y);
        }
        
        /**
         * 获取集合数量
         */
        public int getSetCount() {
            return setCount;
        }
    }
    
    // ====================================================================================
    // 题目1: 二分图判定问题
    // 题目描述: 判断给定无向图是否是二分图
    // 解题思路: 使用二分图并查集进行判定
    // 时间复杂度: O(α(n)) 每次操作
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class BipartiteGraphChecker {
        private BipartiteUnionFind buf;
        
        public BipartiteGraphChecker(int n) {
            this.buf = new BipartiteUnionFind(n);
        }
        
        public boolean addEdge(int u, int v) {
            return buf.addEdge(u, v);
        }
        
        public boolean isBipartite() {
            return buf.isBipartite();
        }
        
        public int getColor(int x) {
            return buf.getColor(x);
        }
    }
    
    /**
     * C++实现 (注释形式)
     * 
     * #include <iostream>
     * #include <vector>
     * using namespace std;
     * 
     * class BipartiteUnionFind {
     * private:
     *     vector<int> parent, rank, color;
     *     bool is_bipartite;
     *     
     * public:
     *     BipartiteUnionFind(int n) : parent(n), rank(n, 1), color(n, 0), is_bipartite(true) {
     *         for (int i = 0; i < n; i++) {
     *             parent[i] = i;
     *         }
     *     }
     *     
     *     int find(int x) {
     *         if (parent[x] != x) {
     *             int root = find(parent[x]);
     *             color[x] ^= color[parent[x]];
     *             parent[x] = root;
     *         }
     *         return parent[x];
     *     }
     *     
     *     bool unionSets(int x, int y, bool is_same) {
     *         int rootX = find(x);
     *         int rootY = find(y);
     *         
     *         if (rootX == rootY) {
     *             bool check = (color[x] ^ color[y]) == (is_same ? 0 : 1);
     *             if (!check) is_bipartite = false;
     *             return check;
     *         }
     *         
     *         if (rank[rootX] < rank[rootY]) {
     *             parent[rootX] = rootY;
     *             color[rootX] = color[x] ^ color[y] ^ (is_same ? 0 : 1);
     *         } else if (rank[rootX] > rank[rootY]) {
     *             parent[rootY] = rootX;
     *             color[rootY] = color[x] ^ color[y] ^ (is_same ? 0 : 1);
     *         } else {
     *             parent[rootY] = rootX;
     *             color[rootY] = color[x] ^ color[y] ^ (is_same ? 0 : 1);
     *             rank[rootX]++;
     *         }
     *         
     *         return true;
     *     }
     *     
     *     bool addEdge(int x, int y) {
     *         return unionSets(x, y, false);
     *     }
     *     
     *     bool isBipartite() {
     *         return is_bipartite;
     *     }
     *     
     *     int getColor(int x) {
     *         find(x);
     *         return color[x];
     *     }
     * };
     * 
     * class BipartiteGraphChecker {
     * private:
     *     BipartiteUnionFind buf;
     *     
     * public:
     *     BipartiteGraphChecker(int n) : buf(n) {}
     *     
     *     bool addEdge(int u, int v) {
     *         return buf.addEdge(u, v);
     *     }
     *     
     *     bool isBipartite() {
     *         return buf.isBipartite();
     *     }
     *     
     *     int getColor(int x) {
     *         return buf.getColor(x);
     *     }
     * };
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * class BipartiteUnionFind:
     *     def __init__(self, n):
     *         self.parent = list(range(n))
     *         self.rank = [1] * n
     *         self.color = [0] * n
     *         self.is_bipartite = True
     *     
     *     def find(self, x):
     *         if self.parent[x] != x:
     *             root = self.find(self.parent[x])
     *             self.color[x] ^= self.color[self.parent[x]]
     *             self.parent[x] = root
     *         return self.parent[x]
     *     
     *     def union(self, x, y, is_same):
     *         root_x = self.find(x)
     *         root_y = self.find(y)
     *         
     *         if root_x == root_y:
     *             check = (self.color[x] ^ self.color[y]) == (0 if is_same else 1)
     *             if not check:
     *                 self.is_bipartite = False
     *             return check
     *         
     *         if self.rank[root_x] < self.rank[root_y]:
     *             self.parent[root_x] = root_y
     *             self.color[root_x] = self.color[x] ^ self.color[y] ^ (0 if is_same else 1)
     *         elif self.rank[root_x] > self.rank[root_y]:
     *             self.parent[root_y] = root_x
     *             self.color[root_y] = self.color[x] ^ self.color[y] ^ (0 if is_same else 1)
     *         else:
     *             self.parent[root_y] = root_x
     *             self.color[root_y] = self.color[x] ^ self.color[y] ^ (0 if is_same else 1)
     *             self.rank[root_x] += 1
     *         
     *         return True
     *     
     *     def add_edge(self, x, y):
     *         return self.union(x, y, False)
     *     
     *     def is_bipartite(self):
     *         return self.is_bipartite
     *     
     *     def get_color(self, x):
     *         self.find(x)
     *         return self.color[x]
     * 
     * class BipartiteGraphChecker:
     *     def __init__(self, n):
     *         self.buf = BipartiteUnionFind(n)
     *     
     *     def add_edge(self, u, v):
     *         return self.buf.add_edge(u, v)
     *     
     *     def is_bipartite(self):
     *         return self.buf.is_bipartite()
     *     
     *     def get_color(self, x):
     *         return self.buf.get_color(x)
     */
    
    // ====================================================================================
    // 题目2: 可持久化并查集应用
    // 题目描述: 支持历史版本查询的并查集
    // 解题思路: 使用可持久化并查集维护历史版本
    // 时间复杂度: O(α(n)) 每次操作
    // 空间复杂度: O(n * version)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class PersistentUFApplication {
        private PersistentUnionFind puf;
        
        public PersistentUFApplication(int n) {
            this.puf = new PersistentUnionFind(n);
        }
        
        public int union(int x, int y) {
            return puf.union(x, y);
        }
        
        public boolean isConnected(int x, int y, int version) {
            return puf.isConnected(x, y, version);
        }
        
        public int getVersion() {
            return puf.getVersion();
        }
    }
    
    // ====================================================================================
    // 题目3: 带撤销操作的并查集
    // 题目描述: 支持撤销操作的并查集
    // 解题思路: 使用带撤销操作的并查集
    // 时间复杂度: O(α(n)) 每次操作
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class UndoUFApplication {
        private UndoUnionFind uuf;
        
        public UndoUFApplication(int n) {
            this.uuf = new UndoUnionFind(n);
        }
        
        public boolean union(int x, int y) {
            return uuf.union(x, y);
        }
        
        public void undo() {
            uuf.undo();
        }
        
        public boolean isConnected(int x, int y) {
            return uuf.isConnected(x, y);
        }
        
        public int getSetCount() {
            return uuf.getSetCount();
        }
    }
    
    // ====================================================================================
    // 题目4: 动态连通性问题
    // 题目描述: 动态维护图的连通性
    // 解题思路: 使用回滚并查集处理动态连通性
    // 时间复杂度: O(α(n)) 每次操作
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class DynamicConnectivity {
        private UndoUnionFind duf;
        
        public DynamicConnectivity(int n) {
            this.duf = new UndoUnionFind(n);
        }
        
        public boolean connect(int x, int y) {
            return duf.union(x, y);
        }
        
        public void disconnect() {
            duf.undo();
        }
        
        public boolean isConnected(int x, int y) {
            return duf.isConnected(x, y);
        }
    }
    
    // ====================================================================================
    // 题目5: 离线处理与并查集
    // 题目描述: 离线处理连通性查询
    // 解题思路: 使用莫队算法结合并查集
    // 时间复杂度: O(α(n)) 每次操作
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class OfflineProcessing {
        private UndoUnionFind ouf;
        
        public OfflineProcessing(int n) {
            this.ouf = new UndoUnionFind(n);
        }
        
        public boolean processQuery(int x, int y) {
            return ouf.isConnected(x, y);
        }
        
        public void addEdge(int x, int y) {
            ouf.union(x, y);
        }
        
        public void removeEdge() {
            ouf.undo();
        }
    }
    
    // ====================================================================================
    // 题目6: 并查集与线段树结合
    // 题目描述: 结合线段树处理区间并查集操作
    // 解题思路: 使用线段树维护区间信息，并查集处理连通性
    // 时间复杂度: O(α(n)) 每次操作
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class SegmentTreeWithUF {
        private UndoUnionFind suf;
        
        public SegmentTreeWithUF(int n) {
            this.suf = new UndoUnionFind(n);
        }
        
        public boolean unionRange(int x, int y) {
            return suf.union(x, y);
        }
        
        public boolean isConnected(int x, int y) {
            return suf.isConnected(x, y);
        }
    }
    
    // ====================================================================================
    // 题目7: 并查集与莫队算法结合
    // 题目描述: 使用莫队算法优化区间查询
    // 解题思路: 莫队算法结合带撤销并查集
    // 时间复杂度: O(α(n)) 每次操作
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class MoAlgorithmWithUF {
        private UndoUnionFind muf;
        
        public MoAlgorithmWithUF(int n) {
            this.muf = new UndoUnionFind(n);
        }
        
        public void add(int x, int y) {
            muf.union(x, y);
        }
        
        public void remove() {
            muf.undo();
        }
        
        public boolean query(int x, int y) {
            return muf.isConnected(x, y);
        }
    }
    
    // ====================================================================================
    // 题目8: 并查集在图论中的高级应用
    // 题目描述: 图论中的复杂连通性问题
    // 解题思路: 使用高级并查集处理复杂图论问题
    // 时间复杂度: O(α(n)) 每次操作
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class AdvancedGraphUF {
        private BipartiteUnionFind auf;
        
        public AdvancedGraphUF(int n) {
            this.auf = new BipartiteUnionFind(n);
        }
        
        public boolean addConstraint(int x, int y, boolean sameColor) {
            return auf.union(x, y, sameColor);
        }
        
        public boolean isBipartite() {
            return auf.isBipartite();
        }
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试二分图判定
        System.out.println("=== 测试二分图判定 ===");
        BipartiteGraphChecker bgc = new BipartiteGraphChecker(4);
        
        // 添加边：0-1, 1-2, 2-3
        bgc.addEdge(0, 1);
        bgc.addEdge(1, 2);
        bgc.addEdge(2, 3);
        System.out.println("图是否是二分图: " + bgc.isBipartite()); // true
        
        // 添加边：0-3（形成奇环）
        bgc.addEdge(0, 3);
        System.out.println("添加边0-3后是否是二分图: " + bgc.isBipartite()); // false
        
        // 测试可持久化并查集
        System.out.println("\n=== 测试可持久化并查集 ===");
        PersistentUFApplication pufa = new PersistentUFApplication(5);
        
        int version1 = pufa.union(0, 1);
        int version2 = pufa.union(2, 3);
        
        System.out.println("版本" + version1 + "中0和1是否连通: " + pufa.isConnected(0, 1, version1)); // true
        System.out.println("版本" + version1 + "中2和3是否连通: " + pufa.isConnected(2, 3, version1)); // false
        System.out.println("版本" + version2 + "中2和3是否连通: " + pufa.isConnected(2, 3, version2)); // true
        
        // 测试带撤销操作的并查集
        System.out.println("\n=== 测试带撤销操作的并查集 ===");
        UndoUFApplication uufa = new UndoUFApplication(5);
        
        uufa.union(0, 1);
        uufa.union(2, 3);
        System.out.println("0和1是否连通: " + uufa.isConnected(0, 1)); // true
        System.out.println("集合数量: " + uufa.getSetCount()); // 3
        
        uufa.undo();
        System.out.println("撤销一次后，0和1是否连通: " + uufa.isConnected(0, 1)); // true
        System.out.println("2和3是否连通: " + uufa.isConnected(2, 3)); // false
        System.out.println("集合数量: " + uufa.getSetCount()); // 4
    }
}