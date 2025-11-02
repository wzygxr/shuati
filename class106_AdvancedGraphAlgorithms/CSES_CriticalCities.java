package class184;

import java.util.*;
import java.io.*;

/**
 * CSES - Critical Cities 解决方案
 * 
 * 题目链接: https://cses.fi/problemset/task/1703
 * 题目描述: 给定一个有向图，找出从节点1到节点n的所有路径上都必须经过的城市（关键城市）
 * 解题思路: 构建支配树，从节点n向上追溯到根节点1的所有节点即为关键城市
 * 
 * 时间复杂度: O((V+E)log(V+E))
 * 空间复杂度: O(V+E)
 */
public class CSES_CriticalCities {
    
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
        
        public List<Integer> getCriticalCities(int target) {
            List<Integer> result = new ArrayList<>();
            int current = target;
            
            while (current != -1) {
                result.add(current + 1); // 转换为1-based索引
                current = idom[current];
            }
            
            Collections.sort(result);
            return result;
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
        
        // 构建图
        DominatorTree dt = new DominatorTree(n, 0); // 节点0作为根节点(1-based转0-based)
        
        for (int i = 0; i < m; i++) {
            line = br.readLine().split(" ");
            int u = Integer.parseInt(line[0]) - 1; // 转换为0-based索引
            int v = Integer.parseInt(line[1]) - 1;
            dt.addEdge(u, v);
        }
        
        // 构建支配树
        dt.build();
        
        // 获取关键城市
        List<Integer> criticalCities = dt.getCriticalCities(n - 1); // n-1是目标节点(0-based)
        
        // 输出结果
        System.out.println(criticalCities.size());
        for (int i = 0; i < criticalCities.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(criticalCities.get(i));
        }
        System.out.println();
    }
}