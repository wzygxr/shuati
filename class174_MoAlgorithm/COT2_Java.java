package class176;

// COT2 - Count on a tree II (SPOJ SP10707) - 树上莫队
// 题目来源: SPOJ SP10707
// 题目链接: https://www.spoj.com/problems/COT2/
// 洛谷链接: https://www.luogu.com.cn/problem/SP10707
// 题意: 给定一棵树，每个节点有一个权值，每次询问两个节点之间的路径上有多少种不同的权值
// 
// 算法思路:
// 1. 使用树上莫队算法，通过欧拉序将树上问题转化为序列问题
// 2. 利用DFS序构造欧拉序，每个节点在进入和退出时都会被记录
// 3. 对于树上两点u,v的路径查询，转化为欧拉序上的区间查询
// 4. 使用莫队算法处理欧拉序上的区间查询
// 
// 时间复杂度分析:
// - 预处理DFS序和LCA: O(n)
// - 排序查询: O(q * log q)
// - 莫队算法处理: O((n + q) * sqrt(n))
// - 总体复杂度: O((n + q) * sqrt(n) + q * log q)
// 
// 空间复杂度分析:
// - 存储树结构: O(n)
// - 存储欧拉序: O(n)
// - 存储查询结果: O(q)
// - 总体空间复杂度: O(n)
// 适用场景: 树上路径不同节点值个数查询问题

import java.io.*;
import java.util.*;

public class COT2_Java {
    
    static class Edge {
        int to, next;
        
        Edge(int to, int next) {
            this.to = to;
            this.next = next;
        }
    }
    
    static class Query {
        int u, v, lca, id;
        
        Query(int u, int v, int lca, int id) {
            this.u = u;
            this.v = v;
            this.lca = lca;
            this.id = id;
        }
    }
    
    static final int MAXN = 40001;
    static final int MAXM = 100001;
    
    // 链式前向星存图
    static Edge[] edges = new Edge[MAXM * 2];
    static int[] head = new int[MAXN];
    static int edgeCnt = 0;
    
    // 树上信息
    static int[] depth = new int[MAXN];
    static int[] fa = new int[MAXN];
    static int[][] up = new int[MAXN][20]; // 倍增祖先
    
    // 欧拉序
    static int[] euler = new int[MAXN * 2];
    static int[] first = new int[MAXN];
    static int eulerCnt = 0;
    
    // 莫队相关
    static int[] arr = new int[MAXN * 2];
    static int[] block = new int[MAXN * 2];
    static int[] cnt = new int[MAXN * 2];
    static int blockSize;
    static int answer = 0;
    static int[] results;
    
    // 添加边
    static void addEdge(int u, int v) {
        edges[edgeCnt] = new Edge(v, head[u]);
        head[u] = edgeCnt++;
        edges[edgeCnt] = new Edge(u, head[v]);
        head[v] = edgeCnt++;
    }
    
    // DFS预处理欧拉序和LCA
    static void dfs(int u, int father, int dep) {
        fa[u] = father;
        depth[u] = dep;
        euler[++eulerCnt] = u;
        first[u] = eulerCnt;
        
        for (int i = head[u]; i != -1; i = edges[i].next) {
            int v = edges[i].to;
            if (v != father) {
                dfs(v, u, dep + 1);
                euler[++eulerCnt] = u;
            }
        }
    }
    
    // 预处理倍增祖先
    static void initLCA(int n) {
        for (int i = 1; i <= n; i++) {
            up[i][0] = fa[i];
        }
        
        for (int j = 1; (1 << j) <= n; j++) {
            for (int i = 1; i <= n; i++) {
                if (up[i][j - 1] != -1) {
                    up[i][j] = up[up[i][j - 1]][j - 1];
                }
            }
        }
    }
    
    // 求LCA
    static int lca(int u, int v) {
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        for (int i = 19; i >= 0; i--) {
            if (depth[u] - (1 << i) >= depth[v]) {
                u = up[u][i];
            }
        }
        
        if (u == v) return u;
        
        for (int i = 19; i >= 0; i--) {
            if (up[u][i] != -1 && up[u][i] != up[v][i]) {
                u = up[u][i];
                v = up[v][i];
            }
        }
        
        return fa[u];
    }
    
    // 添加元素
    static void add(int pos) {
        if (cnt[arr[pos]] == 0) {
            answer++;
        }
        cnt[arr[pos]]++;
    }
    
    // 删除元素
    static void remove(int pos) {
        cnt[arr[pos]]--;
        if (cnt[arr[pos]] == 0) {
            answer--;
        }
    }
    
    // 处理查询
    static int[] processQueries(int n, int[][] queries, int[] values) {
        int q = queries.length;
        Query[] queryList = new Query[q];
        results = new int[q];
        
        // 初始化欧拉序
        eulerCnt = 0;
        dfs(1, -1, 0);
        initLCA(n);
        
        // 构造莫队数组
        for (int i = 1; i <= eulerCnt; i++) {
            arr[i] = values[euler[i]];
        }
        
        // 初始化块大小
        blockSize = (int) Math.sqrt(eulerCnt);
        
        // 为每个位置分配块
        for (int i = 1; i <= eulerCnt; i++) {
            block[i] = (i - 1) / blockSize + 1;
        }
        
        // 创建查询列表
        for (int i = 0; i < q; i++) {
            int u = queries[i][0];
            int v = queries[i][1];
            int lcaNode = lca(u, v);
            
            // 树上莫队的特殊处理
            if (first[u] > first[v]) {
                int temp = u;
                u = v;
                v = temp;
            }
            
            queryList[i] = new Query(first[u], first[v], first[lcaNode], i);
        }
        
        // 按照莫队算法排序
        Arrays.sort(queryList, new Comparator<Query>() {
            public int compare(Query a, Query b) {
                if (block[a.u] != block[b.u]) {
                    return block[a.u] - block[b.u];
                }
                return a.v - b.v;
            }
        });
        
        int curL = 1, curR = 0;
        boolean[] visited = new boolean[MAXN];
        
        // 处理每个查询
        for (int i = 0; i < q; i++) {
            int L = queryList[i].u;
            int R = queryList[i].v;
            int lcaPos = queryList[i].lca;
            int idx = queryList[i].id;
            
            // 扩展右边界
            while (curR < R) {
                curR++;
                if (visited[euler[curR]]) {
                    remove(curR);
                } else {
                    add(curR);
                }
                visited[euler[curR]] = !visited[euler[curR]];
            }
            
            // 收缩左边界
            while (curL > L) {
                curL--;
                if (visited[euler[curL]]) {
                    remove(curL);
                } else {
                    add(curL);
                }
                visited[euler[curL]] = !visited[euler[curL]];
            }
            
            // 收缩右边界
            while (curR > R) {
                if (visited[euler[curR]]) {
                    remove(curR);
                } else {
                    add(curR);
                }
                visited[euler[curR]] = !visited[euler[curR]];
                curR--;
            }
            
            // 扩展左边界
            while (curL < L) {
                if (visited[euler[curL]]) {
                    remove(curL);
                } else {
                    add(curL);
                }
                visited[euler[curL]] = !visited[euler[curL]];
                curL++;
            }
            
            // 特殊处理LCA
            if (lcaPos != L && lcaPos != R) {
                if (visited[euler[lcaPos]]) {
                    remove(lcaPos);
                } else {
                    add(lcaPos);
                }
                visited[euler[lcaPos]] = !visited[euler[lcaPos]];
            }
            
            results[idx] = answer;
            
            // 恢复LCA状态
            if (lcaPos != L && lcaPos != R) {
                visited[euler[lcaPos]] = !visited[euler[lcaPos]];
            }
        }
        
        return results;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        
        String[] parts = br.readLine().split(" ");
        int n = Integer.parseInt(parts[0]);
        int q = Integer.parseInt(parts[1]);
        
        // 初始化链式前向星
        Arrays.fill(head, -1);
        
        parts = br.readLine().split(" ");
        int[] values = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            values[i] = Integer.parseInt(parts[i - 1]);
        }
        
        for (int i = 1; i < n; i++) {
            parts = br.readLine().split(" ");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            addEdge(u, v);
        }
        
        int[][] queries = new int[q][2];
        
        for (int i = 0; i < q; i++) {
            parts = br.readLine().split(" ");
            queries[i][0] = Integer.parseInt(parts[0]);
            queries[i][1] = Integer.parseInt(parts[1]);
        }
        
        int[] results = processQueries(n, queries, values);
        
        for (int result : results) {
            out.println(result);
        }
        
        out.flush();
    }
}