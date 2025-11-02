package class008_AdvancedAlgorithmsAndDataStructures.dominator_tree_problems;

import java.util.*;

/**
 * 支配树 (Dominator Tree) 实现
 * 
 * 支配树是图论中的一个重要概念，主要用于程序优化和静态分析。
 * 在控制流图中，如果从入口节点到节点 v 的每条路径都经过节点 u，
 * 则称节点 u 支配节点 v。支配树是一种表示支配关系的树结构。
 * 
 * 应用场景：
 * 1. 编译器优化：死代码消除、循环优化
 * 2. 程序分析：数据流分析、控制流分析
 * 3. 网络分析：关键路径分析
 * 
 * 算法思路：
 * 使用 Lengauer-Tarjan 算法构建支配树：
 * 1. 对图进行深度优先搜索，构建 DFS 树
 * 2. 计算半支配点 (semi-dominator)
 * 3. 计算支配点 (immediate dominator)
 * 
 * 时间复杂度：O((V+E) log V)
 * 空间复杂度：O(V+E)
 */
public class DominatorTreeImplementation {
    
    static class DominatorTree {
        private int n; // 节点数
        private List<List<Integer>> graph; // 原图的邻接表
        private List<List<Integer>> reverseGraph; // 原图的反向图
        private int[] parent; // DFS树中的父节点
        private int[] semi; // 半支配点
        private int[] idom; // 立即支配点
        private int[] dfn; // DFS序
        private int[] id; // dfn的反向映射
        private int dfsTime; // DFS时间戳
        
        // 用于Lengauer-Tarjan算法的数据结构
        private List<List<Integer>> bucket; // bucket[v]存储semi[v] = w的所有节点v
        private int[] ancestor; // 并查集的父节点
        private int[] label; // 并查集中用于路径压缩的标签
        
        public DominatorTree(int n) {
            this.n = n;
            this.graph = new ArrayList<>();
            this.reverseGraph = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
                reverseGraph.add(new ArrayList<>());
            }
            
            this.parent = new int[n];
            this.semi = new int[n];
            this.idom = new int[n];
            this.dfn = new int[n];
            this.id = new int[n];
            this.bucket = new ArrayList<>();
            this.ancestor = new int[n];
            this.label = new int[n];
            
            Arrays.fill(parent, -1);
            Arrays.fill(semi, -1);
            Arrays.fill(idom, -1);
            Arrays.fill(dfn, -1);
            Arrays.fill(ancestor, -1);
            
            for (int i = 0; i < n; i++) {
                bucket.add(new ArrayList<>());
            }
        }
        
        // 添加边
        public void addEdge(int u, int v) {
            graph.get(u).add(v);
            reverseGraph.get(v).add(u);
        }
        
        // 构建支配树
        public void buildDominatorTree(int root) {
            dfsTime = 0;
            Arrays.fill(dfn, -1);
            
            // 第一步：DFS遍历，构建DFS树
            dfs(root);
            
            // 初始化semi和label数组
            for (int i = 0; i < n; i++) {
                semi[i] = dfn[i];
                label[i] = i;
            }
            
            // 第二步：从后向前计算半支配点
            for (int i = n - 1; i >= 1; i--) {
                int w = id[i];
                
                // 计算semi[w]
                for (int v : reverseGraph.get(w)) {
                    if (dfn[v] == -1) continue; // 跳过不在DFS树中的节点
                    int u = eval(v);
                    if (semi[u] < semi[w]) {
                        semi[w] = semi[u];
                    }
                }
                
                bucket.get(id[semi[w]]).add(w);
                link(parent[w], w);
                
                // 处理bucket[parent[w]]
                for (int v : bucket.get(parent[w])) {
                    int u = eval(v);
                    if (semi[u] < semi[v]) {
                        idom[v] = u;
                    } else {
                        idom[v] = parent[w];
                    }
                }
                
                bucket.get(parent[w]).clear();
            }
            
            // 第三步：计算立即支配点
            for (int i = 1; i < n; i++) {
                int w = id[i];
                if (idom[w] != id[semi[w]]) {
                    idom[w] = idom[idom[w]];
                }
            }
            
            idom[root] = root;
        }
        
        // 深度优先搜索
        private void dfs(int u) {
            dfn[u] = dfsTime;
            id[dfsTime] = u;
            dfsTime++;
            
            for (int v : graph.get(u)) {
                if (dfn[v] == -1) {
                    parent[v] = u;
                    dfs(v);
                }
            }
        }
        
        // 并查集的link操作
        private void link(int v, int w) {
            ancestor[w] = v;
        }
        
        // 并查集的eval操作（带路径压缩）
        private int eval(int v) {
            if (ancestor[v] == -1) {
                return v;
            }
            
            compress(v);
            return label[v];
        }
        
        // 路径压缩
        private void compress(int v) {
            if (ancestor[ancestor[v]] == -1) {
                return;
            }
            
            compress(ancestor[v]);
            
            if (semi[label[ancestor[v]]] < semi[label[v]]) {
                label[v] = label[ancestor[v]];
            }
            
            ancestor[v] = ancestor[ancestor[v]];
        }
        
        // 获取节点v的支配点
        public int getDominator(int v) {
            return idom[v];
        }
        
        // 检查节点u是否支配节点v
        public boolean dominates(int u, int v) {
            // 从v沿着支配树向上查找，看是否能到达u
            int current = v;
            while (current != u && current != idom[current]) {
                current = idom[current];
            }
            return current == u;
        }
        
        // 获取支配树的邻接表表示
        public List<List<Integer>> getDominatorTree() {
            List<List<Integer>> domTree = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                domTree.add(new ArrayList<>());
            }
            
            for (int i = 0; i < n; i++) {
                if (idom[i] != i) { // 不是根节点
                    domTree.get(idom[i]).add(i);
                }
            }
            
            return domTree;
        }
        
        // 打印支配树
        public void printDominatorTree() {
            System.out.println("Dominator Tree:");
            List<List<Integer>> domTree = getDominatorTree();
            for (int i = 0; i < n; i++) {
                System.out.print("Node " + i + " is dominated by " + idom[i] + ", dominates: ");
                for (int child : domTree.get(i)) {
                    System.out.print(child + " ");
                }
                System.out.println();
            }
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1：简单的控制流图
        System.out.println("测试用例1: 简单控制流图");
        DominatorTree dt1 = new DominatorTree(6);
        
        // 构建控制流图
        // 0 -> 1, 2
        // 1 -> 3
        // 2 -> 3
        // 3 -> 4, 5
        // 4 -> 3
        // 5 -> 3
        dt1.addEdge(0, 1);
        dt1.addEdge(0, 2);
        dt1.addEdge(1, 3);
        dt1.addEdge(2, 3);
        dt1.addEdge(3, 4);
        dt1.addEdge(3, 5);
        dt1.addEdge(4, 3);
        dt1.addEdge(5, 3);
        
        // 构建支配树
        dt1.buildDominatorTree(0);
        dt1.printDominatorTree();
        
        System.out.println("节点1是否支配节点3: " + dt1.dominates(1, 3));
        System.out.println("节点0是否支配节点3: " + dt1.dominates(0, 3));
        System.out.println();
        
        // 测试用例2：线性结构
        System.out.println("测试用例2: 线性结构");
        DominatorTree dt2 = new DominatorTree(5);
        
        // 构建线性控制流图
        // 0 -> 1 -> 2 -> 3 -> 4
        dt2.addEdge(0, 1);
        dt2.addEdge(1, 2);
        dt2.addEdge(2, 3);
        dt2.addEdge(3, 4);
        
        // 构建支配树
        dt2.buildDominatorTree(0);
        dt2.printDominatorTree();
        System.out.println();
        
        // 测试用例3：循环结构
        System.out.println("测试用例3: 循环结构");
        DominatorTree dt3 = new DominatorTree(4);
        
        // 构建带循环的控制流图
        // 0 -> 1
        // 1 -> 2
        // 2 -> 1, 3
        dt3.addEdge(0, 1);
        dt3.addEdge(1, 2);
        dt3.addEdge(2, 1);
        dt3.addEdge(2, 3);
        
        // 构建支配树
        dt3.buildDominatorTree(0);
        dt3.printDominatorTree();
    }
}