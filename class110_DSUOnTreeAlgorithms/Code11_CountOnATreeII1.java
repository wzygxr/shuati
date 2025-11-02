package class163;

// Count on a Tree II (SPOJ COT2)实现
// 题目来源: SPOJ COT2
// 题目链接: https://www.spoj.com/problems/COT2/
// 
// 题目大意:
// 给定一棵n个节点的树，每个节点有一个颜色值。
// 有m个查询，每个查询给定两个节点u和v，
// 要求统计u到v路径上不同颜色的数量。
//
// 解题思路:
// 使用树上莫队算法
// 1. 建树，处理出每个节点的深度、父节点等信息
// 2. 生成欧拉序，用于将树上路径问题转化为区间问题
// 3. 使用LCA算法计算最近公共祖先
// 4. 使用莫队算法处理区间查询
//
// 时间复杂度: O(n√n)
// 空间复杂度: O(n)
//
// 算法详解:
// 树上莫队是一种将树上路径问题转化为区间问题的算法
// 通过欧拉序将树上路径问题转化为区间问题，然后使用莫队算法处理
//
// 核心思想:
// 1. 欧拉序生成：通过DFS生成欧拉序，记录每个节点的首次和末次出现位置
// 2. LCA计算：使用倍增法计算两个节点的最近公共祖先
// 3. 莫队算法：将查询按照莫队排序规则排序，然后使用莫队算法处理
//
// 欧拉序处理:
// 1. 对于两个节点u和v，如果u是v的祖先，则路径为first[u]到first[v]
// 2. 对于两个节点u和v，如果u不是v的祖先，则路径为last[u]到first[v]，并加上LCA
//
// 工程化实现要点:
// 1. 边界处理：注意空树、单节点树等特殊情况
// 2. 内存优化：合理使用全局数组，避免重复分配内存
// 3. 常数优化：使用位运算、减少函数调用等优化常数
// 4. 可扩展性：设计通用模板，便于适应不同类型的查询问题

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;

public class Code11_CountOnATreeII1 {
    
    // 最大节点数
    public static int MAXN = 40001;
    
    // 节点数和查询数
    public static int n, m;
    
    // 每个节点的颜色
    public static int[] color = new int[MAXN];
    
    // 邻接表存储树
    public static List<Integer>[] tree = new ArrayList[MAXN];
    
    // 欧拉序相关
    public static int[] euler = new int[MAXN << 1];  // 欧拉序
    public static int[] first = new int[MAXN];       // 第一次出现位置
    public static int[] last = new int[MAXN];        // 最后一次出现位置
    public static int[] depth = new int[MAXN];       // 深度
    public static int eulerCnt = 0;                  // 欧拉序计数
    
    // LCA相关
    public static int[][] st = new int[MAXN][20];    // 倍增表
    public static int[] log2 = new int[MAXN];        // log2预处理
    
    // 莫队相关
    public static int[] cnt = new int[MAXN];         // 颜色计数
    public static int nowAns = 0;                    // 当前答案
    public static int[] ans = new int[MAXN];         // 查询答案
    
    // 查询结构
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
    
    // 初始化
    static {
        for (int i = 0; i < MAXN; i++) {
            tree[i] = new ArrayList<>();
        }
    }
    
    // DFS生成欧拉序
    public static void dfs(int u, int fa, int dep) {
        euler[++eulerCnt] = u;
        first[u] = eulerCnt;
        depth[u] = dep;
        st[u][0] = fa;
        
        // 预处理倍增表
        for (int i = 1; (1 << i) <= dep; i++) {
            st[u][i] = st[st[u][i - 1]][i - 1];
        }
        
        for (int v : tree[u]) {
            if (v != fa) {
                dfs(v, u, dep + 1);
            }
        }
        
        euler[++eulerCnt] = u;
        last[u] = eulerCnt;
    }
    
    // 计算LCA
    public static int lca(int u, int v) {
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        // 将u提升到与v同一深度
        for (int i = 19; i >= 0; i--) {
            if (depth[u] - (1 << i) >= depth[v]) {
                u = st[u][i];
            }
        }
        
        if (u == v) return u;
        
        // 同时提升u和v直到相遇
        for (int i = 19; i >= 0; i--) {
            if (st[u][i] != st[v][i]) {
                u = st[u][i];
                v = st[v][i];
            }
        }
        
        return st[u][0];
    }
    
    // 莫队添加元素
    public static void add(int u) {
        cnt[color[u]]++;
        if (cnt[color[u]] == 1) {
            nowAns++;
        }
    }
    
    // 莫队删除元素
    public static void del(int u) {
        cnt[color[u]]--;
        if (cnt[color[u]] == 0) {
            nowAns--;
        }
    }
    
    // 莫队算法处理查询
    public static void moAlgorithm() {
        // 按照莫队排序规则排序查询
        queries.sort((a, b) -> {
            int blockA = a.l / 300;
            int blockB = b.l / 300;
            if (blockA != blockB) {
                return blockA - blockB;
            }
            return a.r - b.r;
        });
        
        int l = 1, r = 0;
        for (Query q : queries) {
            // 扩展右边界
            while (r < q.r) {
                r++;
                add(euler[r]);
            }
            // 收缩右边界
            while (r > q.r) {
                del(euler[r]);
                r--;
            }
            // 收缩左边界
            while (l < q.l) {
                del(euler[l]);
                l++;
            }
            // 扩展左边界
            while (l > q.l) {
                l--;
                add(euler[l]);
            }
            
            // 处理LCA
            if (q.lca != 0) {
                add(q.lca);
                ans[q.id] = nowAns;
                del(q.lca);
            } else {
                ans[q.id] = nowAns;
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取节点数和查询数
        in.nextToken();
        n = (int) in.nval;
        in.nextToken();
        m = (int) in.nval;
        
        // 读取每个节点的颜色
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            color[i] = (int) in.nval;
        }
        
        // 读取边信息，构建树
        for (int i = 1; i < n; i++) {
            in.nextToken();
            int u = (int) in.nval;
            in.nextToken();
            int v = (int) in.nval;
            tree[u].add(v);
            tree[v].add(u);
        }
        
        // 生成欧拉序
        dfs(1, 0, 1);
        
        // 处理查询
        for (int i = 1; i <= m; i++) {
            in.nextToken();
            int u = (int) in.nval;
            in.nextToken();
            int v = (int) in.nval;
            int lcaNode = lca(u, v);
            
            // 根据欧拉序特性构造查询区间
            if (first[u] > first[v]) {
                int temp = u;
                u = v;
                v = temp;
            }
            
            if (u == lcaNode) {
                queries.add(new Query(first[u], first[v], 0, i));
            } else {
                queries.add(new Query(last[u], first[v], lcaNode, i));
            }
        }
        
        // 执行莫队算法
        moAlgorithm();
        
        // 输出结果
        for (int i = 1; i <= m; i++) {
            out.println(ans[i]);
        }
        
        out.flush();
        out.close();
        br.close();
    }
}