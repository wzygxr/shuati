package class176;

// 树上带修莫队 (树上莫队应用 - 带修改)
// 题目来源: 洛谷P4074 [WC2013] 糖果公园
// 题目链接: https://www.luogu.com.cn/problem/P4074
// 题意: 给定一棵树，每个节点有一个糖果类型，支持两种操作：
// 1. 修改某个节点的糖果类型
// 2. 查询树上两点间路径的愉悦指数（路径上每种糖果的美味指数与新奇指数乘积之和）
// 算法思路: 使用树上带修莫队算法，结合树上莫队和带修莫队的思想
// 时间复杂度: O(n^(5/3))
// 空间复杂度: O(n)
// 适用场景: 树上路径查询，支持单点修改

import java.io.*;
import java.util.*;

public class P4074_TreeMoWithModify_Java {
    
    static class Edge {
        int to, next;
        
        Edge(int to, int next) {
            this.to = to;
            this.next = next;
        }
    }
    
    static class Query {
        int u, v, lca, t, id;
        
        Query(int u, int v, int lca, int t, int id) {
            this.u = u;
            this.v = v;
            this.lca = lca;
            this.t = t;
            this.id = id;
        }
    }
    
    static class Update {
        int pos, val, preVal;
        
        Update(int pos, int val, int preVal) {
            this.pos = pos;
            this.val = val;
            this.preVal = preVal;
        }
    }
    
    static final int MAXN = 100001;
    static final int MAXM = 200001;
    
    // 链式前向星存图
    static Edge[] edges = new Edge[MAXM];
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
    
    // 树上带修莫队相关
    static int[] arr = new int[MAXN]; // 节点的糖果类型
    static int[] block = new int[MAXN * 2];
    static int[] cnt = new int[MAXN]; // 每种糖果类型的出现次数
    static long[] V = new long[MAXN]; // 美味指数
    static long[] W = new long[MAXN]; // 新奇指数
    static int blockSize;
    static long answer = 0;
    static long[] results;
    
    // 修改操作
    static int[] candyType = new int[MAXN]; // 每个节点的糖果类型
    
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
    
    // 更新愉悦指数
    static void updateAnswer(int candy, int delta) {
        // 如果是增加操作
        if (delta > 0) {
            answer += V[candy] * W[cnt[candy] + 1];
        } else {
            // 如果是减少操作
            answer -= V[candy] * W[cnt[candy]];
        }
        cnt[candy] += delta;
    }
    
    // 执行或撤销修改操作
    static void moveTime(int u, int v, int lcaPos, int tim, Update[] updates, boolean[] visited) {
        int pos = updates[tim].pos;
        int val = updates[tim].val;
        
        // 如果修改的节点在当前查询路径上，需要更新答案
        if ((first[pos] >= first[u] && first[pos] <= first[v]) || 
            (first[pos] >= first[v] && first[pos] <= first[u])) {
            // 先移除旧的糖果类型对答案的贡献
            if (visited[pos]) {
                updateAnswer(candyType[pos], -1);
            }
            // 再添加新的糖果类型对答案的贡献
            candyType[pos] = val;
            if (visited[pos]) {
                updateAnswer(candyType[pos], 1);
            }
        } else {
            // 如果不在路径上，直接修改
            candyType[pos] = val;
        }
        
        // 交换值用于下次操作
        int tmp = updates[tim].val;
        updates[tim].val = updates[tim].preVal;
        updates[tim].preVal = tmp;
    }
    
    // 处理查询
    static long[] processQueries(int n, Query[] queries, Update[] updates, int queryCount, int updateCount) {
        results = new long[queryCount + 1];
        
        // 初始化欧拉序
        eulerCnt = 0;
        dfs(1, -1, 0);
        initLCA(n);
        
        // 初始化块大小
        blockSize = Math.max(1, (int) Math.pow(n, 2.0 / 3));
        
        // 为每个位置分配块
        for (int i = 1; i <= eulerCnt; i++) {
            block[i] = (i - 1) / blockSize + 1;
        }
        
        // 按照树上带修莫队的排序规则排序
        Arrays.sort(queries, 1, queryCount + 1, new Comparator<Query>() {
            public int compare(Query a, Query b) {
                if (block[first[a.u]] != block[first[b.u]]) {
                    return block[first[a.u]] - block[first[b.u]];
                }
                if (block[first[a.v]] != block[first[b.v]]) {
                    return block[first[a.v]] - block[first[b.v]];
                }
                return a.t - b.t;
            }
        });
        
        int curL = 1, curR = 0, curT = 0;
        boolean[] visited = new boolean[MAXN];
        answer = 0;
        Arrays.fill(cnt, 0);
        
        // 处理每个查询
        for (int i = 1; i <= queryCount; i++) {
            int u = queries[i].u;
            int v = queries[i].v;
            int lcaPos = queries[i].lca;
            int tim = queries[i].t;
            int id = queries[i].id;
            
            // 扩展时间戳
            while (curT < tim) {
                curT++;
                moveTime(u, v, lcaPos, curT, updates, visited);
            }
            
            while (curT > tim) {
                moveTime(u, v, lcaPos, curT, updates, visited);
                curT--;
            }
            
            // 树上莫队的标准处理
            // 确保u在v的前面
            if (first[u] > first[v]) {
                int temp = u;
                u = v;
                v = temp;
            }
            
            // 扩展右边界
            while (curR < first[v]) {
                curR++;
                int node = euler[curR];
                if (visited[node]) {
                    updateAnswer(candyType[node], -1);
                } else {
                    updateAnswer(candyType[node], 1);
                }
                visited[node] = !visited[node];
            }
            
            // 收缩左边界
            while (curL > first[u]) {
                curL--;
                int node = euler[curL];
                if (visited[node]) {
                    updateAnswer(candyType[node], -1);
                } else {
                    updateAnswer(candyType[node], 1);
                }
                visited[node] = !visited[node];
            }
            
            // 收缩右边界
            while (curR > first[v]) {
                int node = euler[curR];
                if (visited[node]) {
                    updateAnswer(candyType[node], -1);
                } else {
                    updateAnswer(candyType[node], 1);
                }
                visited[node] = !visited[node];
                curR--;
            }
            
            // 扩展左边界
            while (curL < first[u]) {
                int node = euler[curL];
                if (visited[node]) {
                    updateAnswer(candyType[node], -1);
                } else {
                    updateAnswer(candyType[node], 1);
                }
                visited[node] = !visited[node];
                curL++;
            }
            
            // 特殊处理LCA
            if (lcaPos != u && lcaPos != v) {
                if (visited[lcaPos]) {
                    updateAnswer(candyType[lcaPos], -1);
                } else {
                    updateAnswer(candyType[lcaPos], 1);
                }
                visited[lcaPos] = !visited[lcaPos];
            }
            
            results[id] = answer;
            
            // 恢复LCA状态
            if (lcaPos != u && lcaPos != v) {
                visited[lcaPos] = !visited[lcaPos];
            }
        }
        
        return results;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        
        String[] parts = br.readLine().split(" ");
        int n = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        int q = Integer.parseInt(parts[2]);
        
        // 初始化链式前向星
        Arrays.fill(head, -1);
        
        // 读取美味指数
        parts = br.readLine().split(" ");
        for (int i = 1; i <= m; i++) {
            V[i] = Long.parseLong(parts[i - 1]);
        }
        
        // 读取新奇指数
        parts = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            W[i] = Long.parseLong(parts[i - 1]);
        }
        
        // 读取边
        for (int i = 1; i < n; i++) {
            parts = br.readLine().split(" ");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            addEdge(u, v);
        }
        
        // 读取每个节点的糖果类型
        parts = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            candyType[i] = Integer.parseInt(parts[i - 1]);
        }
        
        Query[] queries = new Query[q + 1];
        Update[] updates = new Update[q + 1];
        int queryCount = 0, updateCount = 0;
        
        // 读取操作
        for (int i = 1; i <= q; i++) {
            parts = br.readLine().split(" ");
            int type = Integer.parseInt(parts[0]);
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            
            if (type == 0) {
                // 修改操作
                updateCount++;
                updates[updateCount] = new Update(x, y, candyType[x]);
            } else {
                // 查询操作
                queryCount++;
                int lcaNode = lca(x, y);
                queries[queryCount] = new Query(x, y, lcaNode, updateCount, queryCount);
            }
        }
        
        long[] results = processQueries(n, queries, updates, queryCount, updateCount);
        
        for (int i = 1; i <= queryCount; i++) {
            out.println(results[i]);
        }
        
        out.flush();
    }
}