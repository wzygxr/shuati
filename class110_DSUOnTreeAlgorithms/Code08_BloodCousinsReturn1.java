package class163;

// Blood Cousins Return, Java版本
// 题目来源: Codeforces 246E
// 链接: https://www.luogu.com.cn/problem/CF246E
// 
// 题目大意:
// 给定一棵家族树，n个人，每个人有一个名字和直接祖先(0表示没有祖先)
// 定义k级祖先和k级儿子关系
// m次查询，每次查询某个人的所有k级儿子中不同名字的个数
//
// 解题思路:
// 使用DSU on Tree(树上启发式合并)算法
// 1. 建树，处理出每个节点的深度、子树大小、重儿子等信息
// 2. 对每个节点，维护其子树中每个深度上的不同名字集合
// 3. 使用树上启发式合并优化，保证每个节点最多被访问O(logn)次
// 4. 离线处理所有查询
//
// 时间复杂度: O(n log n)
// 空间复杂度: O(n)
//
// 算法详解:
// DSU on Tree是一种优化的暴力算法，通过重链剖分的思想，将轻重儿子的信息合并过程进行优化
// 使得每个节点最多被访问O(log n)次，从而将时间复杂度从O(n²)优化到O(n log n)
//
// 核心思想:
// 1. 重链剖分预处理：计算每个节点的子树大小，确定重儿子
// 2. 启发式合并处理：
//    - 先处理轻儿子的信息，然后清除贡献
//    - 再处理重儿子的信息并保留贡献
//    - 最后重新计算轻儿子的贡献
// 3. 通过这种方式，保证每个节点最多被访问O(log n)次
//
// 深度处理:
// 1. 维护各深度上的名字集合
// 2. 通过相对深度计算查询结果
// 3. 使用HashSet快速统计不同名字数量
//
// 工程化实现要点:
// 1. 边界处理：注意空树、单节点树等特殊情况
// 2. 内存优化：合理使用全局数组，避免重复分配内存
// 3. 常数优化：使用位运算、减少函数调用等优化常数
// 4. 可扩展性：设计通用模板，便于适应不同类型的查询问题

import java.io.*;
import java.util.*;

public class Code08_BloodCousinsReturn1 {
    public static int MAXN = 100001;
    
    // 树相关数据结构
    public static int n, m;
    public static String[] name = new String[MAXN];
    public static int[] father = new int[MAXN];
    public static int[] head = new int[MAXN];
    public static int[] next = new int[MAXN << 1];
    public static int[] to = new int[MAXN << 1];
    public static int cnt = 0;
    
    // 树链剖分相关
    public static int[] size = new int[MAXN];  // 子树大小
    public static int[] dep = new int[MAXN];   // 深度
    public static int[] son = new int[MAXN];   // 重儿子
    public static int[] fa = new int[MAXN];    // 父亲节点
    
    // DSU on Tree相关
    public static HashMap<String, Integer> nameMap = new HashMap<>(); // 名字离散化
    public static int nameCnt = 0;
    public static int[] nameId = new int[MAXN]; // 每个节点的名字ID
    
    // 每个深度上的名字集合
    public static HashSet<Integer>[] depthNames = new HashSet[MAXN];
    
    // 查询相关
    public static ArrayList<Query>[] queries = new ArrayList[MAXN];
    public static int[] ans = new int[MAXN];
    
    static class Query {
        int k, id;
        Query(int k, int id) {
            this.k = k;
            this.id = id;
        }
    }
    
    public static void addEdge(int u, int v) {
        next[++cnt] = head[u];
        to[cnt] = v;
        head[u] = cnt;
    }
    
    // 第一次DFS，计算子树大小、深度、重儿子
    public static void dfs1(int u, int f, int depth) {
        fa[u] = f;
        dep[u] = depth;
        size[u] = 1;
        son[u] = 0;
        
        for (int e = head[u], v; e > 0; e = next[e]) {
            v = to[e];
            if (v != f) {
                dfs1(v, u, depth + 1);
                size[u] += size[v];
                if (son[u] == 0 || size[son[u]] < size[v]) {
                    son[u] = v;
                }
            }
        }
    }
    
    // 添加节点u到指定深度的集合中
    public static void addName(int u, int baseDepth) {
        int d = dep[u] - baseDepth; // 相对于根节点的深度
        if (depthNames[d] == null) {
            depthNames[d] = new HashSet<>();
        }
        depthNames[d].add(nameId[u]);
        
        // 递归处理子节点
        for (int e = head[u], v; e > 0; e = next[e]) {
            v = to[e];
            if (v != fa[u]) {
                addName(v, baseDepth);
            }
        }
    }
    
    // 清除指定节点子树的信息
    public static void clearNames(int u) {
        int d = dep[u] - dep[u]; // 相对于自身的深度，即0
        if (depthNames[d] != null) {
            depthNames[d].remove(nameId[u]);
        }
        
        // 递归处理子节点
        for (int e = head[u], v; e > 0; e = next[e]) {
            v = to[e];
            if (v != fa[u]) {
                clearNames(v);
            }
        }
    }
    
    // DSU on Tree 主过程
    public static void dsuOnTree(int u, int keep) {
        // 处理所有轻儿子
        for (int e = head[u], v; e > 0; e = next[e]) {
            v = to[e];
            if (v != fa[u] && v != son[u]) {
                dsuOnTree(v, 0); // 不保留信息
            }
        }
        
        // 处理重儿子
        if (son[u] != 0) {
            dsuOnTree(son[u], 1); // 保留信息
        }
        
        // 将轻儿子的贡献加入
        for (int e = head[u], v; e > 0; e = next[e]) {
            v = to[e];
            if (v != fa[u] && v != son[u]) {
                addName(v, dep[u]); // 将v子树中所有节点按相对深度加入
            }
        }
        
        // 加入当前节点
        int d = dep[u] - dep[u]; // 当前节点相对深度为0
        if (depthNames[d] == null) {
            depthNames[d] = new HashSet<>();
        }
        depthNames[d].add(nameId[u]);
        
        // 处理当前节点的所有查询
        if (queries[u] != null) {
            for (Query q : queries[u]) {
                int k = q.k;
                int queryDepth = k; // 查询k级儿子，即深度为k的节点
                if (depthNames[queryDepth] != null) {
                    ans[q.id] = depthNames[queryDepth].size();
                } else {
                    ans[q.id] = 0;
                }
            }
        }
        
        // 如果不保留信息，则清除
        if (keep == 0) {
            clearNames(u);
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        in.nextToken();
        n = (int) in.nval;
        
        // 初始化查询列表
        for (int i = 1; i <= n; i++) {
            queries[i] = new ArrayList<>();
        }
        
        // 读取每个人的信息
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            name[i] = in.sval;
            
            // 名字离散化
            if (!nameMap.containsKey(name[i])) {
                nameMap.put(name[i], ++nameCnt);
            }
            nameId[i] = nameMap.get(name[i]);
            
            in.nextToken();
            father[i] = (int) in.nval;
            
            if (father[i] != 0) {
                addEdge(father[i], i);
                addEdge(i, father[i]);
            }
        }
        
        // 找到所有根节点并处理
        for (int i = 1; i <= n; i++) {
            if (father[i] == 0) {
                dfs1(i, 0, 1);
                dsuOnTree(i, 0);
            }
        }
        
        in.nextToken();
        m = (int) in.nval;
        
        // 读取查询
        for (int i = 1; i <= m; i++) {
            in.nextToken();
            int v = (int) in.nval;
            in.nextToken();
            int k = (int) in.nval;
            // 将查询挂到对应的节点上
            queries[v].add(new Query(k, i));
        }
        
        // 再次处理所有根节点以处理查询
        for (int i = 1; i <= n; i++) {
            if (father[i] == 0) {
                dsuOnTree(i, 0);
            }
        }
        
        // 输出结果
        for (int i = 1; i <= m; i++) {
            out.print(ans[i] + " ");
        }
        out.println();
        out.flush();
        out.close();
        br.close();
    }
}