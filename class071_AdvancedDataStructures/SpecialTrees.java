import java.util.*;

/**
 * 特殊树结构 Java 实现
 * 包含：
 * 1. 支配树 (Dominator Tree) - 使用Lengauer-Tarjan算法
 * 2. 虚树 (Virtual Tree)
 * 
 * 时间复杂度：
 * - 支配树构建：O(E log V)
 * - 虚树构建：O(M log N)，其中M是关键点数量
 * 
 * 设计要点：
 * 1. 支配树用于表示节点间的支配关系
 * 2. 虚树用于压缩树结构，只保留关键点及其LCA
 * 3. 工程化考量：异常处理、边界检查
 * 
 * 典型应用场景：
 * - 程序分析中的控制流分析
 * - 网络流中的瓶颈分析
 * - 大规模树上的路径问题
 */

public class SpecialTrees {
    
    /**
     * 支配树 (Dominator Tree) 实现
     * 使用Lengauer-Tarjan算法
     */
    public static class DominatorTree {
        private int n;                      // 节点数量
        private int timeStamp;              // 时间戳
        private List<List<Integer>> g;      // 原图
        private List<List<Integer>> rg;     // 反向图
        private List<List<Integer>> bucket; // 桶，用于存储待处理节点
        private int[] semi;                 // 半支配点
        private int[] idom;                 // 直接支配点
        private int[] vertex;               // 时间戳 -> 节点
        private int[] label;                // 并查集标签
        private int[] dfn;                  // 节点 -> 时间戳
        private int[] parent;               // DFS树父节点
        private List<List<Integer>> domTree; // 支配树
        
        /**
         * 构造函数
         * @param n 节点数量（节点编号从0开始）
         */
        public DominatorTree(int n) {
            this.n = n;
            g = new ArrayList<>(n);
            rg = new ArrayList<>(n);
            bucket = new ArrayList<>(n);
            semi = new int[n];
            idom = new int[n];
            vertex = new int[n + 1]; // 时间戳从1开始
            label = new int[n];
            dfn = new int[n];
            parent = new int[n];
            domTree = new ArrayList<>(n);
            
            // 初始化列表
            for (int i = 0; i < n; i++) {
                g.add(new ArrayList<>());
                rg.add(new ArrayList<>());
                bucket.add(new ArrayList<>());
                semi[i] = i;
                label[i] = i;
                dfn[i] = 0;
                parent[i] = -1;
                idom[i] = -1;
                domTree.add(new ArrayList<>());
            }
        }
        
        /**
         * 添加有向边
         * @param u 起点
         * @param v 终点
         */
        public void addEdge(int u, int v) {
            if (u < 0 || u >= n || v < 0 || v >= n) {
                throw new IllegalArgumentException("Node index out of range");
            }
            g.get(u).add(v);
            rg.get(v).add(u);
        }
        
        /**
         * 并查集查找操作，带路径压缩
         * @param x 查找的节点
         * @return 根节点
         */
        private int find(int x) {
            if (label[x] != x) {
                int fx = find(label[x]);
                // 路径压缩时选择semi序更小的节点
                if (dfn[semi[fx]] < dfn[semi[label[x]]]) {
                    label[x] = fx;
                }
            }
            return label[x];
        }
        
        /**
         * 深度优先搜索，建立时间戳和DFS树
         * @param u 当前节点
         */
        private void dfs(int u) {
            timeStamp++;
            dfn[u] = timeStamp;
            vertex[timeStamp] = u;
            
            for (int v : g.get(u)) {
                if (dfn[v] == 0) {
                    parent[v] = u;
                    dfs(v);
                }
            }
        }
        
        /**
         * 构建支配树
         * @param root 根节点
         */
        public void build(int root) {
            if (root < 0 || root >= n) {
                throw new IllegalArgumentException("Root node index out of range");
            }
            
            // 初始化
            timeStamp = 0;
            Arrays.fill(dfn, 0);
            Arrays.fill(idom, -1);
            
            // DFS建立时间戳
            dfs(root);
            
            // 初始化semi和label
            for (int i = 1; i <= timeStamp; i++) {
                int u = vertex[i];
                semi[u] = u;
                label[u] = u;
            }
            
            // 按时间戳逆序处理节点
            for (int i = timeStamp; i >= 2; i--) {
                int u = vertex[i];
                
                // 计算semi[u]
                for (int v : rg.get(u)) {
                    if (dfn[v] == 0) continue;
                    
                    if (dfn[v] < dfn[u]) {
                        if (dfn[semi[u]] > dfn[v]) {
                            semi[u] = v;
                        }
                    } else {
                        find(v);
                        if (dfn[semi[u]] > dfn[semi[label[v]]]) {
                            semi[u] = semi[label[v]];
                        }
                    }
                }
                
                bucket.get(semi[u]).add(u);
                
                // 处理parent[u]的桶中的节点
                for (int v : bucket.get(parent[u])) {
                    find(v);
                    if (semi[label[v]] == semi[v]) {
                        idom[v] = semi[v];
                    } else {
                        idom[v] = label[v];
                    }
                }
                bucket.get(parent[u]).clear();
                
                label[u] = parent[u];
            }
            
            // 处理剩余节点，更新直接支配点
            for (int i = 2; i <= timeStamp; i++) {
                int u = vertex[i];
                if (idom[u] != semi[u]) {
                    idom[u] = idom[idom[u]];
                }
            }
            
            // 构建支配树的邻接表
            for (int i = 0; i < n; i++) {
                if (i == root) continue;
                if (idom[i] != -1) {
                    domTree.get(idom[i]).add(i);
                }
            }
        }
        
        /**
         * 判断u是否支配v
         * @param u 支配点
         * @param v 被支配点
         * @return 是否支配
         */
        public boolean isDominating(int u, int v) {
            if (u == v) return true;
            if (idom[v] == -1) return false;
            
            int current = v;
            while (current != -1 && current != u) {
                current = idom[current];
            }
            return current == u;
        }
        
        /**
         * 获取直接支配点
         * @param v 节点
         * @return 直接支配点
         */
        public int getIdom(int v) {
            if (v < 0 || v >= n) {
                throw new IllegalArgumentException("Node index out of range");
            }
            return idom[v];
        }
        
        /**
         * 打印支配树
         */
        public void printDomTree() {
            for (int u = 0; u < n; u++) {
                if (!domTree.get(u).isEmpty()) {
                    System.out.print("Node " + u + " -> ");
                    for (int v : domTree.get(u)) {
                        System.out.print(v + " ");
                    }
                    System.out.println();
                }
            }
        }
    }
    
    /**
     * 虚树 (Virtual Tree) 实现
     */
    public static class VirtualTree {
        private int n;                      // 原树节点数量
        private List<List<Integer>> g;      // 原树邻接表
        private int[] depth;                // 节点深度
        private int[][] up;                 // 倍增表，用于LCA查询
        private int[] dfn;                  // 时间戳
        private int timeStamp;              // 时间戳计数器
        private int logN;                   // log2(n)的上界
        
        /**
         * 构造函数
         * @param n 原树节点数量
         */
        public VirtualTree(int n) {
            this.n = n;
            g = new ArrayList<>(n);
            depth = new int[n];
            dfn = new int[n];
            
            // 计算log2(n)的上界
            logN = 1;
            while ((1 << logN) <= n) {
                logN++;
            }
            
            up = new int[logN][n];
            
            // 初始化邻接表
            for (int i = 0; i < n; i++) {
                g.add(new ArrayList<>());
                depth[i] = 0;
                dfn[i] = 0;
            }
        }
        
        /**
         * 添加原树的无向边
         * @param u 节点u
         * @param v 节点v
         */
        public void addEdge(int u, int v) {
            if (u < 0 || u >= n || v < 0 || v >= n) {
                throw new IllegalArgumentException("Node index out of range");
            }
            g.get(u).add(v);
            g.get(v).add(u);
        }
        
        /**
         * 深度优先搜索，预处理LCA所需信息
         * @param u 当前节点
         * @param parentNode 父节点
         */
        private void dfs(int u, int parentNode) {
            timeStamp++;
            dfn[u] = timeStamp;
            up[0][u] = parentNode;
            
            // 初始化倍增表
            for (int k = 1; k < logN; k++) {
                up[k][u] = up[k-1][up[k-1][u]];
            }
            
            // 递归处理子节点
            for (int v : g.get(u)) {
                if (v != parentNode) {
                    depth[v] = depth[u] + 1;
                    dfs(v, u);
                }
            }
        }
        
        /**
         * 构建LCA所需的数据结构
         * @param root 原树的根节点
         */
        public void buildLCA(int root) {
            if (root < 0 || root >= n) {
                throw new IllegalArgumentException("Root node index out of range");
            }
            
            timeStamp = 0;
            depth[root] = 0;
            dfs(root, root);
        }
        
        /**
         * 计算两个节点的最近公共祖先
         * @param u 节点u
         * @param v 节点v
         * @return 最近公共祖先
         */
        private int lca(int u, int v) {
            if (depth[u] < depth[v]) {
                int temp = u;
                u = v;
                v = temp;
            }
            
            // 将u提升到与v同一深度
            for (int k = logN - 1; k >= 0; k--) {
                if (depth[u] - (1 << k) >= depth[v]) {
                    u = up[k][u];
                }
            }
            
            if (u == v) return u;
            
            // 同时提升u和v
            for (int k = logN - 1; k >= 0; k--) {
                if (up[k][u] != up[k][v]) {
                    u = up[k][u];
                    v = up[k][v];
                }
            }
            
            return up[0][u];
        }
        
        /**
         * 构建虚树
         * @param keyNodes 关键点列表
         * @return 包含虚树邻接表和根节点的数组
         */
        public Object[] buildVirtualTree(List<Integer> keyNodes) {
            if (keyNodes.isEmpty()) {
                return new Object[]{new ArrayList<List<Integer>>(), -1};
            }
            
            // 按时间戳排序关键点
            List<Integer> sortedKeys = new ArrayList<>(new HashSet<>(keyNodes));
            sortedKeys.sort(Comparator.comparingInt(a -> dfn[a]));
            
            // 计算相邻关键点的LCA并添加
            int m = sortedKeys.size();
            List<Integer> temp = new ArrayList<>(sortedKeys);
            for (int i = 0; i < m - 1; i++) {
                temp.add(lca(sortedKeys.get(i), sortedKeys.get(i+1)));
            }
            
            // 再次排序和去重
            temp = new ArrayList<>(new HashSet<>(temp));
            temp.sort(Comparator.comparingInt(a -> dfn[a]));
            sortedKeys = temp;
            
            // 初始化虚树邻接表
            List<List<Integer>> virtualG = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                virtualG.add(new ArrayList<>());
            }
            
            // 使用栈构建虚树
            Deque<Integer> stack = new ArrayDeque<>();
            stack.push(sortedKeys.get(0));
            
            for (int i = 1; i < sortedKeys.size(); i++) {
                int u = sortedKeys.get(i);
                int l = lca(u, stack.peek());
                
                // 弹出栈中深度大于l的节点并建立边
                while (stack.size() > 1 && depth[stack.peek()] > depth[l]) {
                    int v = stack.pop();
                    if (depth[stack.peek()] > depth[l]) {
                        // 连接v和新的栈顶
                        virtualG.get(stack.peek()).add(v);
                        virtualG.get(v).add(stack.peek());
                    } else {
                        // 连接v和l
                        virtualG.get(l).add(v);
                        virtualG.get(v).add(l);
                    }
                }
                
                // 如果栈顶不是l，将l入栈
                if (stack.peek() != l) {
                    stack.push(l);
                }
                stack.push(u);
            }
            
            // 处理栈中剩余的节点
            while (stack.size() > 1) {
                int u = stack.pop();
                virtualG.get(stack.peek()).add(u);
                virtualG.get(u).add(stack.peek());
            }
            
            return new Object[]{virtualG, stack.peek()};
        }
        
        /**
         * 获取节点的深度
         */
        public int getDepth(int u) {
            if (u < 0 || u >= n) {
                throw new IllegalArgumentException("Node index out of range");
            }
            return depth[u];
        }
        
        /**
         * 获取节点的时间戳
         */
        public int getDfn(int u) {
            if (u < 0 || u >= n) {
                throw new IllegalArgumentException("Node index out of range");
            }
            return dfn[u];
        }
    }
    
    // 测试函数
    public static void main(String[] args) {
        System.out.println("===== 测试支配树 =====");
        // 构建一个简单的有向图
        DominatorTree dt = new DominatorTree(7);
        dt.addEdge(0, 1);
        dt.addEdge(0, 2);
        dt.addEdge(1, 3);
        dt.addEdge(2, 3);
        dt.addEdge(3, 4);
        dt.addEdge(3, 5);
        dt.addEdge(4, 6);
        dt.addEdge(5, 6);
        
        dt.build(0);
        System.out.println("支配树结构:");
        dt.printDomTree();
        
        System.out.println("节点0是否支配节点6: " + dt.isDominating(0, 6));
        System.out.println("节点3是否支配节点6: " + dt.isDominating(3, 6));
        System.out.println("节点1是否支配节点5: " + dt.isDominating(1, 5));
        
        System.out.println("\n===== 测试虚树 =====");
        // 构建一个简单的无向树
        VirtualTree vt = new VirtualTree(7);
        vt.addEdge(0, 1);
        vt.addEdge(1, 2);
        vt.addEdge(1, 3);
        vt.addEdge(3, 4);
        vt.addEdge(3, 5);
        vt.addEdge(5, 6);
        
        vt.buildLCA(0);
        
        // 关键点集合
        List<Integer> keyNodes = Arrays.asList(0, 2, 4, 6);
        Object[] result = vt.buildVirtualTree(keyNodes);
        List<List<Integer>> virtualG = (List<List<Integer>>) result[0];
        int root = (int) result[1];
        
        System.out.println("虚树根节点: " + root);
        System.out.println("虚树结构:");
        for (int u = 0; u < 7; u++) {
            if (!virtualG.get(u).isEmpty()) {
                System.out.print("Node " + u + " -> ");
                for (int v : virtualG.get(u)) {
                    System.out.print(v + " ");
                }
                System.out.println();
            }
        }
    }
}