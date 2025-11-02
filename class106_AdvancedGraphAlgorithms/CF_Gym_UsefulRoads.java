package class184;

import java.util.*;
import java.io.*;

/**
 * Codeforces Gym - Useful Roads 解决方案
 * 
 * 题目链接: https://codeforces.com/gym/100513/problem/L
 * 题目描述: 给定一个有向图和一些指定路径，找出在所有指定路径中都使用的边（有用的边）
 * 解题思路: 构建支配树和后支配树，判断边是否在所有路径中都被使用
 * 
 * 时间复杂度: O((V+E)log(V+E))
 * 空间复杂度: O(V+E)
 */
public class CF_Gym_UsefulRoads {
    
    /**
     * 支配树实现类
     */
    static class DominatorTree {
        private int n;
        private int root;
        private List<List<Integer>> graph;
        private List<List<Integer>> reverseGraph;
        private int[] dfn;
        private int[] id;
        private int[] fa;
        private int[] semi;
        private int[] idom;
        private int[] best;
        private int dfsClock;
        private List<List<Integer>> bucket;
        private List<List<Integer>> tree;
        
        public DominatorTree(int n, int root) {
            this.n = n;
            this.root = root;
            this.graph = new ArrayList<>();
            this.reverseGraph = new ArrayList<>();
            this.bucket = new ArrayList<>();
            this.tree = new ArrayList<>();
            
            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
                reverseGraph.add(new ArrayList<>());
                bucket.add(new ArrayList<>());
                tree.add(new ArrayList<>());
            }
            
            this.dfn = new int[n];
            this.id = new int[n];
            this.fa = new int[n];
            this.semi = new int[n];
            this.idom = new int[n];
            this.best = new int[n];
            
            Arrays.fill(dfn, -1);
            Arrays.fill(semi, -1);
            for (int i = 0; i < n; i++) {
                best[i] = i;
            }
            
            this.dfsClock = 0;
        }
        
        public void addEdge(int u, int v) {
            graph.get(u).add(v);
            reverseGraph.get(v).add(u);
        }
        
        private void dfs(int u) {
            dfn[u] = dfsClock;
            id[dfsClock] = u;
            dfsClock++;
            
            for (int v : graph.get(u)) {
                if (dfn[v] == -1) {
                    fa[v] = u;
                    dfs(v);
                }
            }
        }
        
        private int find(int x) {
            if (x == fa[x]) {
                return x;
            }
            
            int root = find(fa[x]);
            
            if (semi[best[fa[x]]] < semi[best[x]]) {
                best[x] = best[fa[x]];
            }
            
            return fa[x] = root;
        }
        
        public void build() {
            dfs(root);
            
            for (int i = dfsClock - 1; i >= 0; i--) {
                int u = id[i];
                
                for (int v : reverseGraph.get(u)) {
                    if (dfn[v] == -1) continue;
                    
                    if (dfn[v] < dfn[u]) {
                        semi[u] = Math.min(semi[u] == -1 ? dfn[v] : semi[u], dfn[v]);
                    } else {
                        find(v);
                        semi[u] = Math.min(semi[u] == -1 ? semi[best[v]] : semi[u], semi[best[v]]);
                    }
                }
                
                if (i > 0) {
                    bucket.get(id[semi[u]]).add(u);
                    
                    int w = fa[u];
                    for (int v : bucket.get(w)) {
                        find(v);
                        if (semi[best[v]] == semi[v]) {
                            idom[v] = w;
                        } else {
                            idom[v] = best[v];
                        }
                    }
                    
                    bucket.get(w).clear();
                }
            }
            
            for (int i = 1; i < dfsClock; i++) {
                int u = id[i];
                if (idom[u] != id[semi[u]]) {
                    idom[u] = idom[idom[u]];
                }
            }
            
            for (int i = 1; i < dfsClock; i++) {
                int u = id[i];
                tree.get(idom[u]).add(u);
            }
        }
        
        public boolean dominates(int u, int v) {
            if (dfn[u] == -1 || dfn[v] == -1) return false;
            
            int current = v;
            while (current != root && current != -1) {
                if (current == u) return true;
                current = idom[current];
            }
            
            return current == u;
        }
        
        public int getImmediateDominator(int u) {
            if (dfn[u] == -1 || u == root) return -1;
            return idom[u];
        }
    }
    
    /**
     * 后支配树实现类
     */
    static class PostDominatorTree {
        private int n;
        private int root;
        private List<List<Integer>> graph;
        private List<List<Integer>> reverseGraph;
        private int[] dfn;
        private int[] id;
        private int[] fa;
        private int[] semi;
        private int[] idom;
        private int[] best;
        private int dfsClock;
        private List<List<Integer>> bucket;
        private List<List<Integer>> tree;
        
        public PostDominatorTree(int n, int root) {
            this.n = n;
            this.root = root;
            this.graph = new ArrayList<>();
            this.reverseGraph = new ArrayList<>();
            this.bucket = new ArrayList<>();
            this.tree = new ArrayList<>();
            
            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
                reverseGraph.add(new ArrayList<>());
                bucket.add(new ArrayList<>());
                tree.add(new ArrayList<>());
            }
            
            this.dfn = new int[n];
            this.id = new int[n];
            this.fa = new int[n];
            this.semi = new int[n];
            this.idom = new int[n];
            this.best = new int[n];
            
            Arrays.fill(dfn, -1);
            Arrays.fill(semi, -1);
            for (int i = 0; i < n; i++) {
                best[i] = i;
            }
            
            this.dfsClock = 0;
        }
        
        public void addEdge(int u, int v) {
            reverseGraph.get(u).add(v); // 反转边的方向
            graph.get(v).add(u);
        }
        
        private void dfs(int u) {
            dfn[u] = dfsClock;
            id[dfsClock] = u;
            dfsClock++;
            
            for (int v : graph.get(u)) {
                if (dfn[v] == -1) {
                    fa[v] = u;
                    dfs(v);
                }
            }
        }
        
        private int find(int x) {
            if (x == fa[x]) {
                return x;
            }
            
            int root = find(fa[x]);
            
            if (semi[best[fa[x]]] < semi[best[x]]) {
                best[x] = best[fa[x]];
            }
            
            return fa[x] = root;
        }
        
        public void build() {
            dfs(root);
            
            for (int i = dfsClock - 1; i >= 0; i--) {
                int u = id[i];
                
                for (int v : reverseGraph.get(u)) {
                    if (dfn[v] == -1) continue;
                    
                    if (dfn[v] < dfn[u]) {
                        semi[u] = Math.min(semi[u] == -1 ? dfn[v] : semi[u], dfn[v]);
                    } else {
                        find(v);
                        semi[u] = Math.min(semi[u] == -1 ? semi[best[v]] : semi[u], semi[best[v]]);
                    }
                }
                
                if (i > 0) {
                    bucket.get(id[semi[u]]).add(u);
                    
                    int w = fa[u];
                    for (int v : bucket.get(w)) {
                        find(v);
                        if (semi[best[v]] == semi[v]) {
                            idom[v] = w;
                        } else {
                            idom[v] = best[v];
                        }
                    }
                    
                    bucket.get(w).clear();
                }
            }
            
            for (int i = 1; i < dfsClock; i++) {
                int u = id[i];
                if (idom[u] != id[semi[u]]) {
                    idom[u] = idom[idom[u]];
                }
            }
            
            for (int i = 1; i < dfsClock; i++) {
                int u = id[i];
                tree.get(idom[u]).add(u);
            }
        }
        
        public boolean postDominates(int u, int v) {
            if (dfn[u] == -1 || dfn[v] == -1) return false;
            
            int current = v;
            while (current != root && current != -1) {
                if (current == u) return true;
                current = idom[current];
            }
            
            return current == u;
        }
    }
    
    /**
     * 主函数
     */
    public static void main(String[] args) throws IOException {
        // 读取输入
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] line = br.readLine().split(" ");
        int n = Integer.parseInt(line[0]);
        int m = Integer.parseInt(line[1]);
        
        // 存储边的信息
        List<int[]> edges = new ArrayList<>();
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int i = 0; i < m; i++) {
            line = br.readLine().split(" ");
            int u = Integer.parseInt(line[0]) - 1; // 转换为0-based索引
            int v = Integer.parseInt(line[1]) - 1;
            edges.add(new int[]{u, v});
            graph.get(u).add(v);
        }
        
        line = br.readLine().split(" ");
        int k = Integer.parseInt(line[0]);
        
        List<int[]> paths = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            line = br.readLine().split(" ");
            int s = Integer.parseInt(line[0]) - 1; // 转换为0-based索引
            int t = Integer.parseInt(line[1]) - 1;
            paths.add(new int[]{s, t});
        }
        
        // 构建支配树和后支配树
        DominatorTree dt = new DominatorTree(n, 0);
        PostDominatorTree pdt = new PostDominatorTree(n, n - 1);
        
        for (int[] edge : edges) {
            dt.addEdge(edge[0], edge[1]);
            pdt.addEdge(edge[0], edge[1]);
        }
        
        dt.build();
        pdt.build();
        
        // 判断每条边是否为有用的边
        List<Integer> usefulEdges = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int[] edge = edges.get(i);
            int u = edge[0];
            int v = edge[1];
            
            boolean isUseful = true;
            for (int[] path : paths) {
                int s = path[0];
                int t = path[1];
                
                // 检查边(u,v)是否在从s到t的所有路径上
                // 这等价于s支配u且v后支配t
                if (!dt.dominates(s, u) || !pdt.postDominates(v, t)) {
                    isUseful = false;
                    break;
                }
            }
            
            if (isUseful) {
                usefulEdges.add(i + 1); // 转换为1-based索引
            }
        }
        
        // 输出结果
        System.out.println(usefulEdges.size());
        if (!usefulEdges.isEmpty()) {
            for (int i = 0; i < usefulEdges.size(); i++) {
                if (i > 0) System.out.print(" ");
                System.out.print(usefulEdges.get(i));
            }
            System.out.println();
        }
    }
}