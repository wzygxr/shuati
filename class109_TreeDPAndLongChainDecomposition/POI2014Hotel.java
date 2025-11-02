package class162;

import java.io.*;
import java.util.*;

public class POI2014Hotel {
    static final int MAXN = 100005;
    
    // 链式前向星存储树
    static int[] head = new int[MAXN];
    static int[] next = new int[MAXN << 1];
    static int[] to = new int[MAXN << 1];
    static int cnt = 0;
    
    // 长链剖分相关数组
    static int[] dep = new int[MAXN];     // 每个节点的深度
    static int[] son = new int[MAXN];     // 每个节点的重儿子
    static int[] maxlen = new int[MAXN];  // 每个节点子树中的最大深度
    static int[] dfn = new int[MAXN];     // dfs序
    static int dfntot = 0;
    
    // DP相关数组
    static long ans = 0;                  // 答案
    static long[][] f = new long[MAXN][]; // f[u][d]表示u子树中到u距离为d的点数
    static long[][] g = new long[MAXN][]; // g[u][d]表示可组成的三元组数
    static int[] fptr = new int[MAXN];    // f数组的指针位置
    static int[] gptr = new int[MAXN];    // g数组的指针位置
    
    // 添加边
    static void addEdge(int u, int v) {
        next[++cnt] = head[u];
        to[cnt] = v;
        head[u] = cnt;
    }
    
    // 第一次DFS：计算每个节点的深度和重儿子
    static void dfs1(int u, int fa) {
        dep[u] = dep[fa] + 1;
        maxlen[u] = 0;
        son[u] = 0;
        
        // 遍历所有子节点
        for (int i = head[u]; i != 0; i = next[i]) {
            int v = to[i];
            if (v == fa) continue;
            
            dfs1(v, u);
            
            // 更新最大深度和重儿子
            if (maxlen[v] > maxlen[u]) {
                maxlen[u] = maxlen[v];
                son[u] = v;
            }
        }
        maxlen[u]++; // 加上自己这一层
    }
    
    // 第二次DFS：长链剖分和DP计算
    static void dfs2(int u, int fa) {
        dfn[u] = ++dfntot;
        
        // 如果有重儿子，先处理重儿子
        if (son[u] != 0) {
            dfs2(son[u], u);
            // 继承重儿子的DP数组
            fptr[u] = Math.max(0, fptr[son[u]] - 1);
            gptr[u] = Math.max(0, gptr[son[u]] - 1);
            f[u] = f[son[u]];
            g[u] = g[son[u]];
        } else {
            // 叶子节点，分配新的DP数组
            f[u] = new long[maxlen[u] + 2];
            g[u] = new long[maxlen[u] + 2];
            fptr[u] = maxlen[u];
            gptr[u] = maxlen[u];
        }
        
        // 自己这一层的贡献
        if (fptr[u] >= 0 && fptr[u] < f[u].length) {
            f[u][fptr[u]] = 1;
        }
        
        // 处理所有轻儿子
        for (int i = head[u]; i != 0; i = next[i]) {
            int v = to[i];
            if (v == fa || v == son[u]) continue;
            
            dfs2(v, u);
            
            // 计算轻儿子对答案的贡献
            for (int j = 0; j < maxlen[v]; j++) {
                // 更新答案
                if (gptr[u] + j + 1 >= 0 && gptr[u] + j + 1 < g[u].length && 
                    fptr[v] + j >= 0 && fptr[v] + j < f[v].length) {
                    ans += g[u][gptr[u] + j + 1] * f[v][fptr[v] + j];
                }
                if (fptr[u] + j + 1 >= 0 && fptr[u] + j + 1 < f[u].length && 
                    fptr[v] + j >= 0 && fptr[v] + j < f[v].length) {
                    ans += f[u][fptr[u] + j + 1] * g[v][fptr[v] + j];
                }
            }
            
            // 合并轻儿子的信息到当前节点
            for (int j = 0; j < maxlen[v]; j++) {
                if (gptr[u] + j + 1 >= 0 && gptr[u] + j + 1 < g[u].length && 
                    fptr[u] + j + 1 >= 0 && fptr[u] + j + 1 < f[u].length &&
                    fptr[v] + j >= 0 && fptr[v] + j < f[v].length) {
                    g[u][gptr[u] + j + 1] += f[u][fptr[u] + j + 1] * f[v][fptr[v] + j];
                    f[u][fptr[u] + j + 1] += f[v][fptr[v] + j];
                }
            }
        }
        
        // 更新g数组
        for (int i = 0; i < maxlen[u]; i++) {
            if (gptr[u] + i >= 0 && gptr[u] + i < g[u].length && 
                fptr[u] + i >= 0 && fptr[u] + i < f[u].length) {
                g[u][gptr[u] + i] += f[u][fptr[u] + i];
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        // 为了测试，我们直接在代码中设置输入
        // 测试用例: 4个节点的链 1-2-3-4
        int n = 4;
        
        // 添加边
        addEdge(1, 2);
        addEdge(2, 1);
        addEdge(2, 3);
        addEdge(3, 2);
        addEdge(3, 4);
        addEdge(4, 3);
        
        // 进行长链剖分和DP计算
        dfs1(1, 0);
        dfs2(1, 0);
        
        // 输出答案
        System.out.println("测试结果: " + ans);
        
        // 正常的输入输出方式（注释掉用于测试）
        /*
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        int n = Integer.parseInt(br.readLine());
        
        // 读入边
        for (int i = 1; i < n; i++) {
            String[] parts = br.readLine().split(" ");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            addEdge(u, v);
            addEdge(v, u);
        }
        
        // 进行长链剖分和DP计算
        dfs1(1, 0);
        dfs2(1, 0);
        
        // 输出答案
        out.println(ans);
        
        out.flush();
        out.close();
        */
    }
}