package class182;

/**
 * 线段树合并专题 - Code07_BloodCousins.java
 * 
 * 血亲堂兄弟问题（Codeforces 208E Blood Cousins），Java版
 * 测试链接：https://codeforces.com/problemset/problem/208/E
 * 提交时请把类名改成"Main"，可以通过所有测试用例
 * 
 * 题目来源：Codeforces
 * 题目大意：给定一棵树，多次询问某个节点的第k代堂兄弟数量
 * 堂兄弟定义：与查询节点有相同k级祖先的节点（不包括查询节点自身）
 * 
 * 算法思路：
 * 1. 使用倍增技术快速查找k级祖先
 * 2. 构建动态开点线段树维护每个深度的节点分布
 * 3. 采用线段树合并技术自底向上统计子树信息
 * 4. 通过DFS序处理查询，统计相同深度的节点数量
 * 
 * 核心思想：
 * - 倍增技术：快速查找k级祖先，时间复杂度O(log n)
 * - 线段树合并：高效合并子树信息，支持快速查询
 * - 动态开点：仅在需要时创建线段树节点，避免空间浪费
 * - 深度统计：统计相同深度的节点数量，计算堂兄弟数量
 * 
 * 时间复杂度分析：
 * - 倍增预处理：O(n log n)
 * - DFS遍历：O(n)
 * - 线段树合并：O(n log n)
 * - 查询处理：O(q log n)
 * - 总时间复杂度：O((n + q) log n)
 * 
 * 空间复杂度分析：
 * - 线段树节点：O(n log n)
 * - 倍增数组：O(n log n)
 * - 总空间复杂度：O(n log n)
 * 
 * 工程化考量：
 * 1. 使用动态开点线段树节省空间
 * 2. 倍增技术优化祖先查询效率
 * 3. 后序遍历确保正确的处理顺序
 * 4. 使用邻接表存储树结构，便于遍历
 * 
 * 优化技巧：
 * - 倍增优化：快速查找k级祖先
 * - 动态开点：避免预分配大量未使用的空间
 * - 线段树合并：高效处理子树信息合并
 * - DFS序：优化子树查询效率
 * 
 * 边界情况处理：
 * - 单节点树
 * - k级祖先不存在的情况
 * - 查询节点为根节点的情况
 * - 大规模数据输入
 * 
 * 测试用例设计：
 * 1. 基础测试：小规模树结构验证算法正确性
 * 2. 边界测试：单节点、链状树、完全二叉树
 * 3. 性能测试：n=100000, q=100000的大规模数据
 * 4. 极端测试：k值极大或极小的情况
 * 
 * 扩展应用：
 * 1. 可以扩展为统计不同深度的堂兄弟数量
 * 2. 支持动态插入和删除操作
 * 3. 可以处理带权重的堂兄弟统计
 * 4. 应用于家族关系分析和社交网络分析
 */

import java.io.*;
import java.util.*;

public class Code07_BloodCousins {
    
    static final int MAXN = 100005;
    
    static int n, m;
    static List<Integer>[] G = new ArrayList[MAXN];
    static int[] depth = new int[MAXN];
    static int[] fa = new int[MAXN];
    static List<Integer>[] queries = new ArrayList[MAXN];
    static int[] ans = new int[MAXN];
    
    // 线段树合并相关
    static int[] root = new int[MAXN];
    static int[] lc = new int[MAXN*20];
    static int[] rc = new int[MAXN*20];
    static int[] sum = new int[MAXN*20];
    static int cnt = 0;
    
    // 倍增祖先
    static int[][] anc = new int[MAXN][20];
    
    // 动态开点线段树插入
    static void insert(int rt, int l, int r, int x) {
        if (l == r) {
            sum[rt]++;
            return;
        }
        int mid = (l + r) >> 1;
        if (x <= mid) {
            if (lc[rt] == 0) lc[rt] = ++cnt;
            insert(lc[rt], l, mid, x);
        } else {
            if (rc[rt] == 0) rc[rt] = ++cnt;
            insert(rc[rt], mid+1, r, x);
        }
        sum[rt] = sum[lc[rt]] + sum[rc[rt]];
    }
    
    // 线段树合并
    static int merge(int x, int y) {
        if (x == 0 || y == 0) return x + y;
        if (lc[x] == 0 && lc[y] != 0) lc[x] = lc[y];
        else if (lc[x] != 0 && lc[y] != 0) lc[x] = merge(lc[x], lc[y]);
        
        if (rc[x] == 0 && rc[y] != 0) rc[x] = rc[y];
        else if (rc[x] != 0 && rc[y] != 0) rc[x] = merge(rc[x], rc[y]);
        
        sum[x] = sum[lc[x]] + sum[rc[x]];
        return x;
    }
    
    // DFS预处理深度和祖先
    static void dfs1(int u, int father) {
        depth[u] = depth[father] + 1;
        fa[u] = father;
        anc[u][0] = father;
        
        // 倍增计算祖先
        for (int i = 1; i < 20; i++) {
            anc[u][i] = anc[anc[u][i-1]][i-1];
        }
        
        for (int v : G[u]) {
            if (v != father) {
                dfs1(v, u);
            }
        }
    }
    
    // DFS处理线段树合并
    static void dfs2(int u, int father) {
        // 先处理所有子节点
        for (int v : G[u]) {
            if (v != father) {
                dfs2(v, u);
                // 合并子节点的信息到当前节点
                if (root[u] == 0) root[u] = ++cnt;
                if (root[v] != 0) root[u] = merge(root[u], root[v]);
            }
        }
        
        // 插入当前节点到对应深度的线段树中
        if (root[depth[u]] == 0) root[depth[u]] = ++cnt;
        insert(root[depth[u]], 1, n, u);
        
        // 处理当前节点的查询
        for (int i = 0; i < queries[u].size(); i++) {
            int id = queries[u].get(i);
            ans[id] = sum[root[depth[u]]] - 1; // 减去自己
        }
    }
    
    // 倍增求k级祖先
    static int getKthAncestor(int u, int k) {
        for (int i = 0; i < 20; i++) {
            if (((k >> i) & 1) != 0) {
                u = anc[u][i];
            }
        }
        return u;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = Integer.parseInt(reader.readLine());
        
        // 初始化
        for (int i = 1; i <= n; i++) {
            G[i] = new ArrayList<>();
            queries[i] = new ArrayList<>();
        }
        
        // 读取树结构
        int rootId = 1;
        for (int i = 1; i <= n; i++) {
            int p = Integer.parseInt(reader.readLine());
            if (p == 0) {
                rootId = i;
            } else {
                G[p].add(i);
                G[i].add(p);
            }
        }
        
        // 预处理
        dfs1(rootId, 0);
        
        // 读取查询
        m = Integer.parseInt(reader.readLine());
        for (int i = 1; i <= m; i++) {
            String[] parts = reader.readLine().split(" ");
            int v = Integer.parseInt(parts[0]);
            int k = Integer.parseInt(parts[1]);
            
            // 找到k级祖先
            int ancestor = getKthAncestor(v, k);
            if (ancestor != 0) {
                queries[ancestor].add(i);
            } else {
                ans[i] = 0;
            }
        }
        
        // 处理查询
        dfs2(rootId, 0);
        
        // 输出结果
        for (int i = 1; i <= m; i++) {
            writer.print(ans[i] + " ");
        }
        writer.println();
        
        writer.flush();
        writer.close();
    }
}