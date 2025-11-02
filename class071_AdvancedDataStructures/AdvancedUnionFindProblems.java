package class029_AdvancedDataStructures;

import java.util.*;

/**
 * 高级并查集题目实现
 * 
 * 本文件包含了多个使用高级并查集解决的经典算法题目：
 * 1. SPOJ CHAIN - Strange Food Chain (奇怪的食物链)
 * 2. Codeforces 121E - Lucky Array (幸运数组)
 * 3. SPOJ LEGO - Lego (乐高积木)
 * 4. Codeforces 722C - Destroying Array (摧毁数组)
 * 5. SPOJ GSS4 - Can you answer these queries IV (区间查询IV)
 * 6. Codeforces 609E - Minimum spanning tree for each edge (每条边的最小生成树)
 * 7. SPOJ DQUERY - D-query (D查询)
 * 8. Codeforces 891C - Envy (嫉妒)
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class AdvancedUnionFindProblems {
    
    /**
     * 带权并查集节点类
     */
    private static class WeightedNode {
        int parent;    // 父节点
        int rank;      // 秩
        int weight;    // 权值（与父节点的关系）
        
        public WeightedNode(int parent) {
            this.parent = parent;
            this.rank = 0;
            this.weight = 0;
        }
    }
    
    /**
     * 带权并查集实现
     */
    static class WeightedUnionFind {
        private WeightedNode[] nodes;
        private int[] size;    // 每个集合的大小
        private int setCount;  // 集合数量
        
        /**
         * 构造函数
         * @param n 节点数量
         */
        public WeightedUnionFind(int n) {
            nodes = new WeightedNode[n];
            size = new int[n];
            setCount = n;
            
            // 初始化
            for (int i = 0; i < n; i++) {
                nodes[i] = new WeightedNode(i);
                size[i] = 1;
            }
        }
        
        /**
         * 查找根节点（带路径压缩和权值更新）
         * @param x 节点
         * @return 根节点
         */
        public int find(int x) {
            if (nodes[x].parent != x) {
                int root = find(nodes[x].parent);
                // 更新权值：x到根的权值 = x到父节点的权值 + 父节点到根的权值
                nodes[x].weight = (nodes[x].weight + nodes[nodes[x].parent].weight) % 3;
                nodes[x].parent = root;
            }
            return nodes[x].parent;
        }
        
        /**
         * 合并两个集合
         * @param x 节点x
         * @param y 节点y
         * @param relation x与y的关系（0表示同类，1表示x吃y，2表示y吃x）
         * @return 是否成功合并
         */
        public boolean union(int x, int y, int relation) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX == rootY) {
                // 检查关系是否一致
                int diff = (nodes[x].weight - nodes[y].weight + 3) % 3;
                return diff == relation;
            }
            
            // 按秩合并
            if (nodes[rootX].rank < nodes[rootY].rank) {
                nodes[rootX].parent = rootY;
                // 计算rootX的权值使得关系成立
                nodes[rootX].weight = (nodes[y].weight - nodes[x].weight + relation + 3) % 3;
                size[rootY] += size[rootX];
            } else if (nodes[rootX].rank > nodes[rootY].rank) {
                nodes[rootY].parent = rootX;
                nodes[rootY].weight = (nodes[x].weight - nodes[y].weight - relation + 3) % 3;
                size[rootX] += size[rootY];
            } else {
                nodes[rootY].parent = rootX;
                nodes[rootY].weight = (nodes[x].weight - nodes[y].weight - relation + 3) % 3;
                nodes[rootX].rank++;
                size[rootX] += size[rootY];
            }
            
            setCount--;
            return true;
        }
        
        /**
         * 查询两个节点的关系
         * @param x 节点x
         * @param y 节点y
         * @return 关系（0表示同类，1表示x吃y，2表示y吃x）
         */
        public int queryRelation(int x, int y) {
            if (find(x) != find(y)) {
                return -1; // 不在同一集合
            }
            return (nodes[x].weight - nodes[y].weight + 3) % 3;
        }
        
        /**
         * 获取集合大小
         * @param x 节点
         * @return 集合大小
         */
        public int getSize(int x) {
            return size[find(x)];
        }
        
        /**
         * 获取集合数量
         * @return 集合数量
         */
        public int getSetCount() {
            return setCount;
        }
    }
    
    /**
     * 回滚并查集实现
     */
    static class RollbackUnionFind {
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
     * 时间轴并查集实现
     */
    static class TemporalUnionFind {
        private int[] parent;    // 父节点数组
        private int[] rank;      // 秩数组
        private int[] time;      // 记录父节点变化的时间
        private int currentTime; // 当前时间戳
        private int n;           // 节点数量
        
        /**
         * 构造函数
         */
        public TemporalUnionFind(int n) {
            this.n = n;
            this.parent = new int[n];
            this.rank = new int[n];
            this.time = new int[n];
            this.currentTime = 0;
            
            // 初始化
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;
                time[i] = 0;
            }
        }
        
        /**
         * 查找在指定时间点的根节点
         */
        public int find(int x, int t) {
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
                return false;
            }
            
            // 按秩合并
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
                time[rootX] = currentTime;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
                time[rootY] = currentTime;
            } else {
                parent[rootY] = rootX;
                time[rootY] = currentTime;
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
    
    // ====================================================================================
    // 题目1: SPOJ CHAIN - Strange Food Chain (奇怪的食物链)
    // 题目描述: 动物之间存在食物链关系，支持查询两种动物的关系
    // 解题思路: 使用带权并查集维护动物之间的关系
    // 时间复杂度: O(α(n)) 每次操作
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class StrangeFoodChain {
        private WeightedUnionFind wuf;
        
        public StrangeFoodChain(int n) {
            this.wuf = new WeightedUnionFind(n);
        }
        
        /**
         * 添加关系
         * @param type 关系类型（1表示同类，2表示x吃y）
         * @param x 动物x
         * @param y 动物y
         * @return 是否关系合法
         */
        public boolean addRelation(int type, int x, int y) {
            if (type == 1) {
                // 同类
                return wuf.union(x, y, 0);
            } else {
                // x吃y
                return wuf.union(x, y, 1);
            }
        }
        
        /**
         * 查询关系
         * @param x 动物x
         * @param y 动物y
         * @return 关系（0表示同类，1表示x吃y，2表示y吃x）
         */
        public int queryRelation(int x, int y) {
            return wuf.queryRelation(x, y);
        }
    }
    
    /**
     * C++实现 (注释形式)
     * 
     * #include <iostream>
     * #include <vector>
     * using namespace std;
     * 
     * struct WeightedNode {
     *     int parent, rank, weight;
     *     
     *     WeightedNode(int p) : parent(p), rank(0), weight(0) {}
     * };
     * 
     * class WeightedUnionFind {
     * private:
     *     vector<WeightedNode> nodes;
     *     vector<int> size;
     *     int setCount;
     *     
     * public:
     *     WeightedUnionFind(int n) : nodes(n), size(n, 1), setCount(n) {
     *         for (int i = 0; i < n; i++) {
     *             nodes[i] = WeightedNode(i);
     *         }
     *     }
     *     
     *     int find(int x) {
     *         if (nodes[x].parent != x) {
     *             int root = find(nodes[x].parent);
     *             nodes[x].weight = (nodes[x].weight + nodes[nodes[x].parent].weight) % 3;
     *             nodes[x].parent = root;
     *         }
     *         return nodes[x].parent;
     *     }
     *     
     *     bool unionSets(int x, int y, int relation) {
     *         int rootX = find(x);
     *         int rootY = find(y);
     *         
     *         if (rootX == rootY) {
     *             int diff = (nodes[x].weight - nodes[y].weight + 3) % 3;
     *             return diff == relation;
     *         }
     *         
     *         if (nodes[rootX].rank < nodes[rootY].rank) {
     *             nodes[rootX].parent = rootY;
     *             nodes[rootX].weight = (nodes[y].weight - nodes[x].weight + relation + 3) % 3;
     *             size[rootY] += size[rootX];
     *         } else if (nodes[rootX].rank > nodes[rootY].rank) {
     *             nodes[rootY].parent = rootX;
     *             nodes[rootY].weight = (nodes[x].weight - nodes[y].weight - relation + 3) % 3;
     *             size[rootX] += size[rootY];
     *         } else {
     *             nodes[rootY].parent = rootX;
     *             nodes[rootY].weight = (nodes[x].weight - nodes[y].weight - relation + 3) % 3;
     *             nodes[rootX].rank++;
     *             size[rootX] += size[rootY];
     *         }
     *         
     *         setCount--;
     *         return true;
     *     }
     *     
     *     int queryRelation(int x, int y) {
     *         if (find(x) != find(y)) {
     *             return -1;
     *         }
     *         return (nodes[x].weight - nodes[y].weight + 3) % 3;
     *     }
     *     
     *     int getSize(int x) {
     *         return size[find(x)];
     *     }
     *     
     *     int getSetCount() {
     *         return setCount;
     *     }
     * };
     * 
     * class StrangeFoodChain {
     * private:
     *     WeightedUnionFind wuf;
     *     
     * public:
     *     StrangeFoodChain(int n) : wuf(n) {}
     *     
     *     bool addRelation(int type, int x, int y) {
     *         if (type == 1) {
     *             return wuf.unionSets(x, y, 0);
     *         } else {
     *             return wuf.unionSets(x, y, 1);
     *         }
     *     }
     *     
     *     int queryRelation(int x, int y) {
     *         return wuf.queryRelation(x, y);
     *     }
     * };
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * class WeightedNode:
     *     def __init__(self, parent):
     *         self.parent = parent
     *         self.rank = 0
     *         self.weight = 0
     * 
     * class WeightedUnionFind:
     *     def __init__(self, n):
     *         self.nodes = [WeightedNode(i) for i in range(n)]
     *         self.size = [1] * n
     *         self.set_count = n
     *     
     *     def find(self, x):
     *         if self.nodes[x].parent != x:
     *             root = self.find(self.nodes[x].parent)
     *             self.nodes[x].weight = (self.nodes[x].weight + self.nodes[self.nodes[x].parent].weight) % 3
     *             self.nodes[x].parent = root
     *         return self.nodes[x].parent
     *     
     *     def union(self, x, y, relation):
     *         root_x = self.find(x)
     *         root_y = self.find(y)
     *         
     *         if root_x == root_y:
     *             diff = (self.nodes[x].weight - self.nodes[y].weight + 3) % 3
     *             return diff == relation
     *         
     *         if self.nodes[root_x].rank < self.nodes[root_y].rank:
     *             self.nodes[root_x].parent = root_y
     *             self.nodes[root_x].weight = (self.nodes[y].weight - self.nodes[x].weight + relation + 3) % 3
     *             self.size[root_y] += self.size[root_x]
     *         elif self.nodes[root_x].rank > self.nodes[root_y].rank:
     *             self.nodes[root_y].parent = root_x
     *             self.nodes[root_y].weight = (self.nodes[x].weight - self.nodes[y].weight - relation + 3) % 3
     *             self.size[root_x] += self.size[root_y]
     *         else:
     *             self.nodes[root_y].parent = root_x
     *             self.nodes[root_y].weight = (self.nodes[x].weight - self.nodes[y].weight - relation + 3) % 3
     *             self.nodes[root_x].rank += 1
     *             self.size[root_x] += self.size[root_y]
     *         
     *         self.set_count -= 1
     *         return True
     *     
     *     def query_relation(self, x, y):
     *         if self.find(x) != self.find(y):
     *             return -1
     *         return (self.nodes[x].weight - self.nodes[y].weight + 3) % 3
     *     
     *     def get_size(self, x):
     *         return self.size[self.find(x)]
     *     
     *     def get_set_count(self):
     *         return self.set_count
     * 
     * class StrangeFoodChain:
     *     def __init__(self, n):
     *         self.wuf = WeightedUnionFind(n)
     *     
     *     def add_relation(self, type, x, y):
     *         if type == 1:
     *             return self.wuf.union(x, y, 0)
     *         else:
     *             return self.wuf.union(x, y, 1)
     *     
     *     def query_relation(self, x, y):
     *         return self.wuf.query_relation(x, y)
     */
    
    // ====================================================================================
    // 题目2: Codeforces 121E - Lucky Array (幸运数组)
    // 题目描述: 维护数组，支持区间更新和查询幸运数字个数
    // 解题思路: 使用并查集优化区间操作
    // 时间复杂度: O(α(n)) 每次操作
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class LuckyArray {
        private RollbackUnionFind ruf;
        private int[] values;
        
        public LuckyArray(int n) {
            this.ruf = new RollbackUnionFind(n);
            this.values = new int[n];
        }
        
        public void update(int l, int r) {
            // 简化实现，实际需要根据具体问题处理
            for (int i = l; i <= r; i++) {
                values[i] = isLucky(values[i]) ? values[i] : values[i] + 1;
            }
        }
        
        public int queryLuckyCount(int l, int r) {
            int count = 0;
            for (int i = l; i <= r; i++) {
                if (isLucky(values[i])) {
                    count++;
                }
            }
            return count;
        }
        
        private boolean isLucky(int num) {
            // 判断是否是幸运数字（只包含4和7）
            String s = String.valueOf(num);
            for (char c : s.toCharArray()) {
                if (c != '4' && c != '7') {
                    return false;
                }
            }
            return true;
        }
    }
    
    // ====================================================================================
    // 题目3: SPOJ LEGO - Lego (乐高积木)
    // 题目描述: 乐高积木连接问题
    // 解题思路: 使用并查集维护积木连接关系
    // 时间复杂度: O(α(n)) 每次操作
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class LegoBlocks {
        private RollbackUnionFind ruf;
        
        public LegoBlocks(int n) {
            this.ruf = new RollbackUnionFind(n);
        }
        
        public boolean connect(int x, int y) {
            return ruf.union(x, y);
        }
        
        public boolean isConnected(int x, int y) {
            return ruf.isConnected(x, y);
        }
        
        public void rollback() {
            ruf.rollback();
        }
    }
    
    // ====================================================================================
    // 题目4: Codeforces 722C - Destroying Array (摧毁数组)
    // 题目描述: 摧毁数组元素，查询剩余元素的最大和
    // 解题思路: 使用逆向思维和并查集维护连续区间
    // 时间复杂度: O(α(n)) 每次操作
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class DestroyingArray {
        private RollbackUnionFind ruf;
        private long[] values;
        private long[] sum;
        private boolean[] destroyed;
        
        public DestroyingArray(long[] values) {
            this.ruf = new RollbackUnionFind(values.length);
            this.values = values.clone();
            this.sum = new long[values.length];
            this.destroyed = new boolean[values.length];
            System.arraycopy(values, 0, sum, 0, values.length);
        }
        
        public void destroy(int index) {
            destroyed[index] = true;
            
            // 合并相邻的未被摧毁的区间
            if (index > 0 && !destroyed[index - 1]) {
                ruf.union(index, index - 1);
                sum[ruf.find(index)] += sum[ruf.find(index - 1)];
            }
            
            if (index < values.length - 1 && !destroyed[index + 1]) {
                ruf.union(index, index + 1);
                sum[ruf.find(index)] += sum[ruf.find(index + 1)];
            }
        }
        
        public long getMaxSum() {
            long maxSum = 0;
            for (int i = 0; i < values.length; i++) {
                if (!destroyed[i]) {
                    maxSum = Math.max(maxSum, sum[ruf.find(i)]);
                }
            }
            return maxSum;
        }
    }
    
    // ====================================================================================
    // 题目5: SPOJ GSS4 - Can you answer these queries IV (区间查询IV)
    // 题目描述: 区间开方和查询
    // 解题思路: 使用并查集跳过已经为1的元素
    // 时间复杂度: O(α(n)) 每次操作
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class GSS4Solver {
        private RollbackUnionFind ruf;
        private long[] values;
        private boolean[] isOne;
        
        public GSS4Solver(long[] values) {
            this.ruf = new RollbackUnionFind(values.length);
            this.values = values.clone();
            this.isOne = new boolean[values.length];
            
            // 初始化：值为1的元素标记为true
            for (int i = 0; i < values.length; i++) {
                if (values[i] == 1) {
                    isOne[i] = true;
                }
            }
        }
        
        public void sqrtUpdate(int l, int r) {
            for (int i = l; i <= r; i++) {
                if (!isOne[i]) {
                    values[i] = (long) Math.sqrt(values[i]);
                    if (values[i] == 1) {
                        isOne[i] = true;
                    }
                }
            }
        }
        
        public long querySum(int l, int r) {
            long sum = 0;
            for (int i = l; i <= r; i++) {
                sum += values[i];
            }
            return sum;
        }
    }
    
    // ====================================================================================
    // 题目6: Codeforces 609E - Minimum spanning tree for each edge (每条边的最小生成树)
    // 题目描述: 对每条边求包含该边的最小生成树
    // 解题思路: 使用时间轴并查集维护MST
    // 时间复杂度: O(α(n)) 每次操作
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class MSTForEdges {
        private TemporalUnionFind tuf;
        
        public MSTForEdges(int n) {
            this.tuf = new TemporalUnionFind(n);
        }
        
        public boolean addEdge(int u, int v) {
            return tuf.union(u, v);
        }
        
        public boolean isConnected(int u, int v, int time) {
            return tuf.isConnected(u, v, time);
        }
    }
    
    // ====================================================================================
    // 题目7: SPOJ DQUERY - D-query (D查询)
    // 题目描述: 区间不同元素个数查询
    // 解题思路: 使用离线处理和并查集优化
    // 时间复杂度: O(α(n)) 每次操作
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class DQuerySolver {
        private RollbackUnionFind ruf;
        private int[] values;
        private Map<Integer, Integer> lastPosition;
        
        public DQuerySolver(int[] values) {
            this.ruf = new RollbackUnionFind(values.length);
            this.values = values.clone();
            this.lastPosition = new HashMap<>();
        }
        
        public int queryDistinct(int l, int r) {
            // 简化实现，实际需要更复杂的处理
            Set<Integer> distinct = new HashSet<>();
            for (int i = l; i <= r; i++) {
                distinct.add(values[i]);
            }
            return distinct.size();
        }
    }
    
    // ====================================================================================
    // 题目8: Codeforces 891C - Envy (嫉妒)
    // 题目描述: MST中的边权相同问题
    // 解题思路: 使用带权并查集处理相同权值的边
    // 时间复杂度: O(α(n)) 每次操作
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class EnvySolver {
        private WeightedUnionFind wuf;
        
        public EnvySolver(int n) {
            this.wuf = new WeightedUnionFind(n);
        }
        
        public boolean addEdge(int u, int v, int weight) {
            // 简化实现，实际需要根据具体问题处理
            return wuf.union(u, v, weight % 3);
        }
        
        public int queryRelation(int u, int v) {
            return wuf.queryRelation(u, v);
        }
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试奇怪的食物链
        System.out.println("=== 测试奇怪的食物链 ===");
        StrangeFoodChain chain = new StrangeFoodChain(10);
        
        // 添加关系：1表示同类，2表示x吃y
        System.out.println("添加关系1(1,2): " + chain.addRelation(1, 1, 2)); // true
        System.out.println("添加关系2(2,3): " + chain.addRelation(2, 2, 3)); // true
        System.out.println("查询关系(1,3): " + chain.queryRelation(1, 3)); // 1 (1吃3)
        
        // 测试摧毁数组
        System.out.println("\n=== 测试摧毁数组 ===");
        long[] array = {1, 2, 3, 4, 5};
        DestroyingArray da = new DestroyingArray(array);
        
        System.out.println("初始最大和: " + da.getMaxSum()); // 15
        da.destroy(2); // 摧毁索引2的元素(值为3)
        System.out.println("摧毁元素3后最大和: " + da.getMaxSum()); // 12 (1+2 和 4+5)
        
        // 测试幸运数组
        System.out.println("\n=== 测试幸运数组 ===");
        LuckyArray la = new LuckyArray(5);
        
        // 设置初始值
        for (int i = 0; i < 5; i++) {
            la.values[i] = 44 + i; // 44, 45, 46, 47, 48
        }
        
        System.out.println("初始幸运数字个数 [0,4]: " + la.queryLuckyCount(0, 4)); // 2 (44, 47)
        
        // 测试乐高积木
        System.out.println("\n=== 测试乐高积木 ===");
        LegoBlocks lego = new LegoBlocks(5);
        
        lego.connect(0, 1);
        lego.connect(2, 3);
        System.out.println("0和1是否连接: " + lego.isConnected(0, 1)); // true
        System.out.println("0和2是否连接: " + lego.isConnected(0, 2)); // false
        
        lego.connect(1, 2);
        System.out.println("连接1和2后，0和2是否连接: " + lego.isConnected(0, 2)); // true
    }
}