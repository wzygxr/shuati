package class157;

import java.io.*;
import java.util.*;

/**
 * SPOJ COT2 - Count on a tree II
 * 
 * 题目来源: SPOJ COT2
 * 题目链接: https://www.spoj.com/problems/COT2/
 * 
 * 题目描述:
 * 给你一棵有N个节点的树。树的节点编号从1到N。每个节点都有一个整数权重。
 * 我们将要求你执行以下操作:
 * u v : 询问从u到v的路径上有多少个不同的整数表示节点的权重。
 * 
 * 解题思路:
 * 使用树上莫队算法解决树上路径不同元素个数问题。
 * 1. 使用欧拉序将树上路径问题转化为序列问题
 * 2. 对欧拉序上的区间进行莫队算法处理
 * 3. 对于路径u到v的查询，根据u和v在欧拉序中的位置关系确定对应的区间
 * 
 * 时间复杂度: O((n + m) * sqrt(n))
 * 空间复杂度: O(n)
 * 
 * 约束条件:
 * N, M <= 40000
 * 
 * 示例:
 * 输入:
 * 8 4
 * 1 2 3 4 5 6 7 8
 * 1 2
 * 2 3
 * 2 4
 * 3 5
 * 3 6
 * 4 7
 * 4 8
 * 1 8
 * 3 5
 * 2 7
 * 5 8
 * 
 * 输出:
 * 6
 * 3
 * 4
 * 6
 */
public class SPOJ_COT2_CountOnTreeII {

    public static int MAXN = 40010;
    public static int MAXM = 100010;
    
    // 树的存储
    public static int[] head = new int[MAXN];
    public static int[] edge = new int[MAXN * 2];
    public static int[] next = new int[MAXN * 2];
    public static int edgeCnt = 0;
    
    // 节点权重
    public static int[] weight = new int[MAXN];
    public static int[] sortedWeights = new int[MAXN];
    
    // DFS相关
    public static int[] dfn = new int[MAXN];  // 欧拉序
    public static int[] dep = new int[MAXN];  // 深度
    public static int[] fa = new int[MAXN];   // 父亲节点
    public static int[] first = new int[MAXN]; // 第一次出现位置
    public static int[] second = new int[MAXN]; // 第二次出现位置
    public static int timestamp = 0;
    
    // LCA相关
    public static int[][] dp = new int[MAXN][20];
    
    // 莫队相关
    public static int blockSize;
    public static int[] cnt = new int[MAXN];  // 权值计数
    public static int nowAns = 0;  // 当前答案
    
    // 离散化相关
    public static int[] values = new int[MAXN];
    public static int valueCnt = 0;
    
    // 查询
    static class Query {
        int l, r, lca, id;
        
        Query(int l, int r, int lca, int id) {
            this.l = l;
            this.r = r;
            this.lca = lca;
            this.id = id;
        }
    }
    
    public static List<Query> queries = new ArrayList<>();
    public static int[] ans = new int[MAXM];
    
    /**
     * 添加边
     */
    public static void addEdge(int u, int v) {
        edge[edgeCnt] = v;
        next[edgeCnt] = head[u];
        head[u] = edgeCnt++;
    }
    
    /**
     * DFS生成欧拉序
     */
    public static void dfs(int u, int father, int depth) {
        fa[u] = father;
        dep[u] = depth;
        first[u] = ++timestamp;
        dfn[timestamp] = u;
        
        // 倍增计算LCA
        dp[u][0] = father;
        for (int i = 1; (1 << i) <= dep[u]; i++) {
            dp[u][i] = dp[dp[u][i-1]][i-1];
        }
        
        // 遍历子节点
        for (int i = head[u]; i != -1; i = next[i]) {
            int v = edge[i];
            if (v != father) {
                dfs(v, u, depth + 1);
            }
        }
        
        second[u] = ++timestamp;
        dfn[timestamp] = u;
    }
    
    /**
     * 计算LCA
     */
    public static int lca(int u, int v) {
        if (dep[u] < dep[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        // 让u和v在同一深度
        for (int i = 19; i >= 0; i--) {
            if (dep[u] - (1 << i) >= dep[v]) {
                u = dp[u][i];
            }
        }
        
        if (u == v) return u;
        
        // 同时向上跳
        for (int i = 19; i >= 0; i--) {
            if (dp[u][i] != dp[v][i]) {
                u = dp[u][i];
                v = dp[v][i];
            }
        }
        
        return dp[u][0];
    }
    
    /**
     * 离散化权重值
     */
    public static void discretize(int n) {
        for (int i = 1; i <= n; i++) {
            sortedWeights[i] = weight[i];
        }
        Arrays.sort(sortedWeights, 1, n + 1);
        
        valueCnt = 1;
        values[1] = sortedWeights[1];
        for (int i = 2; i <= n; i++) {
            if (sortedWeights[i] != sortedWeights[i-1]) {
                values[++valueCnt] = sortedWeights[i];
            }
        }
    }
    
    /**
     * 二分查找离散化后的索引
     */
    public static int binarySearch(int target) {
        int left = 1, right = valueCnt;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (values[mid] == target) return mid;
            else if (values[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        return -1;
    }
    
    /**
     * 莫队添加元素
     */
    public static void add(int pos) {
        int u = dfn[pos];
        int val = binarySearch(weight[u]);
        cnt[val]++;
        if (cnt[val] == 1) nowAns++;
    }
    
    /**
     * 莫队删除元素
     */
    public static void del(int pos) {
        int u = dfn[pos];
        int val = binarySearch(weight[u]);
        cnt[val]--;
        if (cnt[val] == 0) nowAns--;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] line = in.readLine().split(" ");
        int n = Integer.parseInt(line[0]);
        int m = Integer.parseInt(line[1]);
        
        // 初始化
        Arrays.fill(head, -1);
        
        // 读取节点权重
        line = in.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            weight[i] = Integer.parseInt(line[i - 1]);
        }
        
        // 离散化
        discretize(n);
        
        // 读取边
        for (int i = 1; i < n; i++) {
            line = in.readLine().split(" ");
            int u = Integer.parseInt(line[0]);
            int v = Integer.parseInt(line[1]);
            addEdge(u, v);
            addEdge(v, u);
        }
        
        // DFS生成欧拉序
        dfs(1, 0, 1);
        
        // 设置块大小
        blockSize = (int) Math.sqrt(timestamp);
        
        // 处理查询
        for (int i = 1; i <= m; i++) {
            line = in.readLine().split(" ");
            int u = Integer.parseInt(line[0]);
            int v = Integer.parseInt(line[1]);
            
            int lcaNode = lca(u, v);
            
            // 根据u和v在欧拉序中的位置确定查询区间
            if (first[u] > first[v]) {
                int temp = u;
                u = v;
                v = temp;
            }
            
            if (u == lcaNode) {
                queries.add(new Query(first[u], first[v], 0, i));
            } else {
                // 路径u->v经过lcaNode
                if (first[u] > second[v]) {
                    queries.add(new Query(second[v], first[u], lcaNode, i));
                } else {
                    queries.add(new Query(first[u], first[v], lcaNode, i));
                }
            }
        }
        
        // 莫队排序
        Collections.sort(queries, (a, b) -> {
            int blockA = a.l / blockSize;
            int blockB = b.l / blockSize;
            if (blockA != blockB) return blockA - blockB;
            return a.r - b.r;
        });
        
        // 莫队处理
        int l = 1, r = 0;
        for (Query q : queries) {
            // 扩展右端点
            while (r < q.r) {
                r++;
                add(r);
            }
            // 收缩右端点
            while (r > q.r) {
                del(r);
                r--;
            }
            // 收缩左端点
            while (l < q.l) {
                del(l);
                l++;
            }
            // 扩展左端点
            while (l > q.l) {
                l--;
                add(l);
            }
            
            // 处理LCA
            if (q.lca != 0) {
                int val = binarySearch(weight[q.lca]);
                cnt[val]++;
                if (cnt[val] == 1) nowAns++;
            }
            
            ans[q.id] = nowAns;
            
            // 恢复LCA
            if (q.lca != 0) {
                int val = binarySearch(weight[q.lca]);
                cnt[val]--;
                if (cnt[val] == 0) nowAns--;
            }
        }
        
        // 输出答案
        for (int i = 1; i <= m; i++) {
            out.println(ans[i]);
        }
        
        out.flush();
        out.close();
        in.close();
    }
}