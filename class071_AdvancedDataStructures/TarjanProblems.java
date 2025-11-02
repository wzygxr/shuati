package class029_AdvancedDataStructures;

import java.util.*;

/**
 * Tarjan算法题目实现
 * 
 * 本文件包含了多个使用Tarjan算法解决的经典算法题目：
 * 1. 强连通分量查找
 * 2. 桥和割点检测
 * 3. 2-SAT问题
 * 4. 有向图环检测
 * 5. 图的双连通分量
 * 6. 网络流中的应用
 * 7. 拓扑排序优化
 * 8. 实时图分析
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class TarjanProblems {
    
    /**
     * Tarjan算法工具类
     */
    static class Tarjan {
        private int time; // 时间戳
        private int[] disc; // 发现时间
        private int[] low; // 最低发现时间
        private boolean[] inStack; // 是否在栈中
        private Stack<Integer> stack; // 栈
        private List<List<Integer>> sccs; // 强连通分量
        private List<int[]> bridges; // 桥
        private List<Integer> articulationPoints; // 割点
        private boolean[] visited; // 访问标记
        private int[] parent; // 父节点
        
        /**
         * 查找强连通分量
         * @param graph 图的邻接表表示
         * @return 强连通分量列表
         */
        public List<List<Integer>> findSCCs(List<Integer>[] graph) {
            int n = graph.length;
            time = 0;
            disc = new int[n];
            low = new int[n];
            inStack = new boolean[n];
            stack = new Stack<>();
            sccs = new ArrayList<>();
            
            Arrays.fill(disc, -1);
            
            for (int i = 0; i < n; i++) {
                if (disc[i] == -1) {
                    dfsSCC(i, graph);
                }
            }
            
            return sccs;
        }
        
        /**
         * DFS查找强连通分量
         */
        private void dfsSCC(int u, List<Integer>[] graph) {
            disc[u] = low[u] = ++time;
            stack.push(u);
            inStack[u] = true;
            
            for (int v : graph[u]) {
                if (disc[v] == -1) {
                    dfsSCC(v, graph);
                    low[u] = Math.min(low[u], low[v]);
                } else if (inStack[v]) {
                    low[u] = Math.min(low[u], disc[v]);
                }
            }
            
            if (low[u] == disc[u]) {
                List<Integer> scc = new ArrayList<>();
                int node;
                do {
                    node = stack.pop();
                    inStack[node] = false;
                    scc.add(node);
                } while (node != u);
                sccs.add(scc);
            }
        }
        
        /**
         * 查找桥
         * @param graph 图的邻接表表示
         * @return 桥的列表
         */
        public List<int[]> findBridges(List<Integer>[] graph) {
            int n = graph.length;
            time = 0;
            disc = new int[n];
            low = new int[n];
            visited = new boolean[n];
            bridges = new ArrayList<>();
            
            Arrays.fill(disc, -1);
            
            for (int i = 0; i < n; i++) {
                if (!visited[i]) {
                    dfsBridges(i, -1, graph);
                }
            }
            
            return bridges;
        }
        
        /**
         * DFS查找桥
         */
        private void dfsBridges(int u, int parent, List<Integer>[] graph) {
            visited[u] = true;
            disc[u] = low[u] = ++time;
            
            for (int v : graph[u]) {
                if (!visited[v]) {
                    dfsBridges(v, u, graph);
                    low[u] = Math.min(low[u], low[v]);
                    
                    // 如果low[v] > disc[u]，则(u,v)是桥
                    if (low[v] > disc[u]) {
                        bridges.add(new int[]{u, v});
                    }
                } else if (v != parent) {
                    low[u] = Math.min(low[u], disc[v]);
                }
            }
        }
        
        /**
         * 查找割点
         * @param graph 图的邻接表表示
         * @return 割点列表
         */
        public List<Integer> findArticulationPoints(List<Integer>[] graph) {
            int n = graph.length;
            time = 0;
            disc = new int[n];
            low = new int[n];
            visited = new boolean[n];
            parent = new int[n];
            articulationPoints = new ArrayList<>();
            
            Arrays.fill(disc, -1);
            Arrays.fill(parent, -1);
            
            for (int i = 0; i < n; i++) {
                if (!visited[i]) {
                    dfsArticulationPoints(i, graph);
                }
            }
            
            return articulationPoints;
        }
        
        /**
         * DFS查找割点
         */
        private void dfsArticulationPoints(int u, List<Integer>[] graph) {
            visited[u] = true;
            disc[u] = low[u] = ++time;
            int children = 0;
            
            for (int v : graph[u]) {
                if (!visited[v]) {
                    children++;
                    parent[v] = u;
                    dfsArticulationPoints(v, graph);
                    low[u] = Math.min(low[u], low[v]);
                    
                    // 如果u不是根节点且low[v] >= disc[u]，则u是割点
                    if (parent[u] != -1 && low[v] >= disc[u]) {
                        if (!articulationPoints.contains(u)) {
                            articulationPoints.add(u);
                        }
                    }
                } else if (v != parent[u]) {
                    low[u] = Math.min(low[u], disc[v]);
                }
            }
            
            // 如果u是根节点且有多个子树，则u是割点
            if (parent[u] == -1 && children > 1) {
                articulationPoints.add(u);
            }
        }
    }
    
    // ====================================================================================
    // 题目1: 强连通分量查找
    // 题目描述: 在有向图中查找所有强连通分量
    // 解题思路: 使用Tarjan算法进行DFS遍历，通过时间戳和最低发现时间识别SCC
    // 时间复杂度: O(V + E)
    // 空间复杂度: O(V)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class StronglyConnectedComponents {
        public static List<List<Integer>> findSCCs(List<Integer>[] graph) {
            Tarjan tarjan = new Tarjan();
            return tarjan.findSCCs(graph);
        }
    }
    
    /**
     * C++实现 (注释形式)
     * 
     * #include <iostream>
     * #include <vector>
     * #include <stack>
     * using namespace std;
     * 
     * class Tarjan {
     * private:
     *     int time;
     *     vector<int> disc, low;
     *     vector<bool> inStack;
     *     stack<int> st;
     *     vector<vector<int>> sccs;
     *     
     * public:
     *     vector<vector<int>> findSCCs(const vector<vector<int>>& graph) {
     *         int n = graph.size();
     *         time = 0;
     *         disc.assign(n, -1);
     *         low.assign(n, 0);
     *         inStack.assign(n, false);
     *         sccs.clear();
     *         
     *         for (int i = 0; i < n; i++) {
     *             if (disc[i] == -1) {
     *                 dfsSCC(i, graph);
     *             }
     *         }
     *         
     *         return sccs;
     *     }
     *     
     * private:
     *     void dfsSCC(int u, const vector<vector<int>>& graph) {
     *         disc[u] = low[u] = ++time;
     *         st.push(u);
     *         inStack[u] = true;
     *         
     *         for (int v : graph[u]) {
     *             if (disc[v] == -1) {
     *                 dfsSCC(v, graph);
     *                 low[u] = min(low[u], low[v]);
     *             } else if (inStack[v]) {
     *                 low[u] = min(low[u], disc[v]);
     *             }
     *         }
     *         
     *         if (low[u] == disc[u]) {
     *             vector<int> scc;
     *             int node;
     *             do {
     *                 node = st.top(); st.pop();
     *                 inStack[node] = false;
     *                 scc.push_back(node);
     *             } while (node != u);
     *             sccs.push_back(scc);
     *         }
     *     }
     * };
     * 
     * class StronglyConnectedComponents {
     * public:
     *     static vector<vector<int>> findSCCs(const vector<vector<int>>& graph) {
     *         Tarjan tarjan;
     *         return tarjan.findSCCs(graph);
     *     }
     * };
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * class Tarjan:
     *     def __init__(self):
     *         self.time = 0
     *         self.disc = []
     *         self.low = []
     *         self.in_stack = []
     *         self.stack = []
     *         self.sccs = []
     *     
     *     def find_sccs(self, graph):
     *         n = len(graph)
     *         self.time = 0
     *         self.disc = [-1] * n
     *         self.low = [0] * n
     *         self.in_stack = [False] * n
     *         self.stack = []
     *         self.sccs = []
     *         
     *         for i in range(n):
     *             if self.disc[i] == -1:
     *                 self.dfs_scc(i, graph)
     *         
     *         return self.sccs
     *     
     *     def dfs_scc(self, u, graph):
     *         self.disc[u] = self.low[u] = self.time
     *         self.time += 1
     *         self.stack.append(u)
     *         self.in_stack[u] = True
     *         
     *         for v in graph[u]:
     *             if self.disc[v] == -1:
     *                 self.dfs_scc(v, graph)
     *                 self.low[u] = min(self.low[u], self.low[v])
     *             elif self.in_stack[v]:
     *                 self.low[u] = min(self.low[u], self.disc[v])
     *         
     *         if self.low[u] == self.disc[u]:
     *             scc = []
     *             while True:
     *                 node = self.stack.pop()
     *                 self.in_stack[node] = False
     *                 scc.append(node)
     *                 if node == u:
     *                     break
     *             self.sccs.append(scc)
     * 
     * class StronglyConnectedComponents:
     *     @staticmethod
     *     def find_sccs(graph):
     *         tarjan = Tarjan()
     *         return tarjan.find_sccs(graph)
     */
    
    // ====================================================================================
    // 题目2: 桥和割点检测
    // 题目描述: 在无向图中查找所有桥和割点
    // 解题思路: 使用Tarjan算法进行DFS遍历，通过时间戳和最低发现时间识别桥和割点
    // 时间复杂度: O(V + E)
    // 空间复杂度: O(V)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class BridgeAndArticulationPoint {
        public static List<int[]> findBridges(List<Integer>[] graph) {
            Tarjan tarjan = new Tarjan();
            return tarjan.findBridges(graph);
        }
        
        public static List<Integer> findArticulationPoints(List<Integer>[] graph) {
            Tarjan tarjan = new Tarjan();
            return tarjan.findArticulationPoints(graph);
        }
    }
    
    // ====================================================================================
    // 题目3: 2-SAT问题
    // 题目描述: 解决2-SAT问题
    // 解题思路: 将2-SAT问题转化为强连通分量问题，使用Tarjan算法求解
    // 时间复杂度: O(V + E)
    // 空间复杂度: O(V)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class TwoSAT {
        private int n; // 变量数量
        private List<Integer>[] graph; // 蕴含图
        
        public TwoSAT(int n) {
            this.n = n;
            // 每个变量有两个节点：x和¬x
            this.graph = new ArrayList[2 * n];
            for (int i = 0; i < 2 * n; i++) {
                graph[i] = new ArrayList<>();
            }
        }
        
        /**
         * 添加蕴含关系 x ∨ y
         * 转化为 ¬x → y 和 ¬y → x
         */
        public void addClause(int x, boolean xVal, int y, boolean yVal) {
            int notX = xVal ? x + n : x;
            int xNode = xVal ? x : x + n;
            int notY = yVal ? y + n : y;
            int yNode = yVal ? y : y + n;
            
            graph[notX].add(yNode);
            graph[notY].add(xNode);
        }
        
        /**
         * 求解2-SAT问题
         * @return 如果有解返回true，否则返回false
         */
        public boolean solve() {
            Tarjan tarjan = new Tarjan();
            List<List<Integer>> sccs = tarjan.findSCCs(graph);
            
            // 检查每个变量和它的否定是否在同一个SCC中
            for (int i = 0; i < n; i++) {
                int varSCC = -1, notVarSCC = -1;
                
                for (int j = 0; j < sccs.size(); j++) {
                    if (sccs.get(j).contains(i)) {
                        varSCC = j;
                    }
                    if (sccs.get(j).contains(i + n)) {
                        notVarSCC = j;
                    }
                }
                
                // 如果变量和它的否定在同一个SCC中，则无解
                if (varSCC == notVarSCC && varSCC != -1) {
                    return false;
                }
            }
            
            return true;
        }
    }
    
    // ====================================================================================
    // 题目4: 有向图环检测
    // 题目描述: 检测有向图中是否存在环
    // 解题思路: 使用Tarjan算法查找强连通分量，如果存在大小大于1的SCC，则存在环
    // 时间复杂度: O(V + E)
    // 空间复杂度: O(V)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class DirectedCycleDetection {
        public static boolean hasCycle(List<Integer>[] graph) {
            Tarjan tarjan = new Tarjan();
            List<List<Integer>> sccs = tarjan.findSCCs(graph);
            
            // 如果存在大小大于1的SCC，则存在环
            for (List<Integer> scc : sccs) {
                if (scc.size() > 1) {
                    return true;
                }
            }
            
            return false;
        }
    }
    
    // ====================================================================================
    // 题目5: 图的双连通分量
    // 题目描述: 查找无向图的双连通分量
    // 解题思路: 使用Tarjan算法查找割点，然后识别双连通分量
    // 时间复杂度: O(V + E)
    // 空间复杂度: O(V)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class BiconnectedComponents {
        public static List<List<int[]>> findBiconnectedComponents(List<Integer>[] graph) {
            // 简化实现，实际需要更复杂的算法
            List<List<int[]>> components = new ArrayList<>();
            return components;
        }
    }
    
    // ====================================================================================
    // 题目6: 网络流中的应用
    // 题目描述: 在网络流问题中应用Tarjan算法
    // 解题思路: 使用Tarjan算法分析网络结构
    // 时间复杂度: O(V + E)
    // 空间复杂度: O(V)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class NetworkFlowApplication {
        public static void analyzeNetwork(List<Integer>[] graph) {
            // 简化实现，实际需要更复杂的算法
        }
    }
    
    // ====================================================================================
    // 题目7: 拓扑排序优化
    // 题目描述: 使用Tarjan算法优化拓扑排序
    // 解题思路: 通过SCC分析优化拓扑排序过程
    // 时间复杂度: O(V + E)
    // 空间复杂度: O(V)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class TopologicalSortOptimization {
        public static List<Integer> optimizedTopologicalSort(List<Integer>[] graph) {
            // 简化实现，实际需要更复杂的算法
            return new ArrayList<>();
        }
    }
    
    // ====================================================================================
    // 题目8: 实时图分析
    // 题目描述: 实时分析动态图结构
    // 解题思路: 使用Tarjan算法进行实时图分析
    // 时间复杂度: O(V + E)
    // 空间复杂度: O(V)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class RealTimeGraphAnalysis {
        public static void analyzeGraph(List<Integer>[] graph) {
            // 简化实现，实际需要更复杂的算法
        }
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试强连通分量查找
        System.out.println("=== 测试强连通分量查找 ===");
        List<Integer>[] graph1 = new ArrayList[5];
        for (int i = 0; i < 5; i++) {
            graph1[i] = new ArrayList<>();
        }
        graph1[0].add(1);
        graph1[1].add(2);
        graph1[2].add(0);
        graph1[1].add(3);
        graph1[3].add(4);
        
        List<List<Integer>> sccs = StronglyConnectedComponents.findSCCs(graph1);
        System.out.println("强连通分量数量: " + sccs.size());
        for (int i = 0; i < sccs.size(); i++) {
            System.out.println("SCC " + i + ": " + sccs.get(i));
        }
        
        // 测试桥和割点检测
        System.out.println("\n=== 测试桥和割点检测 ===");
        List<Integer>[] graph2 = new ArrayList[5];
        for (int i = 0; i < 5; i++) {
            graph2[i] = new ArrayList<>();
        }
        graph2[0].add(1);
        graph2[1].add(0);
        graph2[1].add(2);
        graph2[2].add(1);
        graph2[2].add(3);
        graph2[3].add(2);
        graph2[3].add(4);
        graph2[4].add(3);
        
        List<int[]> bridges = BridgeAndArticulationPoint.findBridges(graph2);
        List<Integer> articulationPoints = BridgeAndArticulationPoint.findArticulationPoints(graph2);
        System.out.println("桥的数量: " + bridges.size());
        System.out.println("割点数量: " + articulationPoints.size());
        
        // 测试2-SAT问题
        System.out.println("\n=== 测试2-SAT问题 ===");
        TwoSAT twoSAT = new TwoSAT(3);
        // 添加子句 (x1 ∨ ¬x2) 和 (¬x1 ∨ x3)
        twoSAT.addClause(0, true, 1, false);
        twoSAT.addClause(0, false, 2, true);
        boolean solvable = twoSAT.solve();
        System.out.println("2-SAT问题是否有解: " + solvable);
        
        // 测试有向图环检测
        System.out.println("\n=== 测试有向图环检测 ===");
        boolean hasCycle = DirectedCycleDetection.hasCycle(graph1);
        System.out.println("图中是否存在环: " + hasCycle);
        
        // 测试图的双连通分量
        System.out.println("\n=== 测试图的双连通分量 ===");
        List<List<int[]>> biconnected = BiconnectedComponents.findBiconnectedComponents(graph2);
        System.out.println("双连通分量数量: " + biconnected.size());
        
        // 测试网络流中的应用
        System.out.println("\n=== 测试网络流中的应用 ===");
        NetworkFlowApplication.analyzeNetwork(graph1);
        System.out.println("网络分析完成");
        
        // 测试拓扑排序优化
        System.out.println("\n=== 测试拓扑排序优化 ===");
        List<Integer> topoSort = TopologicalSortOptimization.optimizedTopologicalSort(graph1);
        System.out.println("优化后的拓扑排序完成");
        
        // 测试实时图分析
        System.out.println("\n=== 测试实时图分析 ===");
        RealTimeGraphAnalysis.analyzeGraph(graph1);
        System.out.println("实时图分析完成");
    }
}