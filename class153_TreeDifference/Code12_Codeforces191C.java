package class122;

/**
 * Codeforces 191C - Fools and Roads（树上边差分）
 * 
 * 题目来源：Codeforces
 * 题目链接：https://codeforces.com/contest/191/problem/C
 * 
 * 题目描述：
 * 给定一棵包含N个节点的树，以及K对节点(u,v)。
 * 对于每对节点，它们之间的路径上的每条边都会被经过一次。
 * 求每条边被经过的次数。
 * 
 * 算法原理：树上边差分 + LCA (最近公共祖先)
 * 树上边差分是处理树上路径操作的一种高效技术。
 * 对于每对节点(u,v)之间的路径，我们需要让路径上的所有边计数加1。
 * 通过边差分，我们可以：
 * 1. diff[u]++
 * 2. diff[v]++
 * 3. diff[lca(u,v)] -= 2
 * 最后通过一次DFS回溯累加子节点的差分标记，得到每条边的最终计数。
 * 
 * 时间复杂度分析：
 * - 预处理LCA：O(N log N)
 * - 差分标记：O(K log N)，其中K是操作次数
 * - DFS回溯统计：O(N)
 * 总时间复杂度：O(N log N + K log N)
 * 
 * 空间复杂度分析：
 * - 树的存储：O(N)
 * - LCA倍增数组：O(N log N)
 * - 差分数组：O(N)
 * - 答案数组：O(N)
 * 总空间复杂度：O(N log N)
 * 
 * 工程化考量：
 * 1. 使用链式前向星存储树结构，提高空间效率和遍历速度
 * 2. 使用BufferedReader和PrintWriter进行高效输入输出
 * 3. 预处理log2值，优化倍增数组的大小
 * 4. 通过edgeId数组将边与答案数组关联，便于输出
 * 
 * 最优解分析：
 * 树上边差分是解决此类路径覆盖问题的最优解，相比暴力遍历每条路径的O(N*K)复杂度，
 * 树上边差分可以将时间复杂度优化到O(N log N + K log N)，在大规模数据下效率提升显著。
 */

import java.io.*;
import java.util.*;

public class Code12_Codeforces191C {
    
    /**
     * 最大节点数，根据题目数据范围设定
     * 题目中N最大为1e5，设置为100001以避免越界
     */
    public static int MAXN = 100001;
    
    /**
     * 倍增数组的大小限制
     * 2^17 = 131,072 > 1e5，足够处理最大节点数
     */
    public static int LIMIT = 17;
    
    /**
     * 链式前向星建图
     * head[u]: 节点u的第一条边的索引
     * next[e]: 边e的下一条边的索引
     * to[e]: 边e指向的节点
     * edgeId[e]: 边e的编号（用于关联答案数组）
     * cnt: 边的计数器
     */
    public static int[] head = new int[MAXN];
    public static int[] next = new int[MAXN << 1];
    public static int[] to = new int[MAXN << 1];
    public static int[] edgeId = new int[MAXN << 1];
    public static int cnt = 1;
    
    /**
     * LCA相关数组
     * depth[u]: 节点u的深度
     * stjump[u][p]: 节点u的2^p级祖先
     */
    public static int[] depth = new int[MAXN];
    public static int[][] stjump = new int[MAXN][LIMIT];
    
    /**
     * 差分数组
     * diff[u]: 节点u到其父节点的边被路径覆盖的次数的差分值
     */
    public static int[] diff = new int[MAXN];
    
    /**
     * 答案数组
     * ans[i]: 第i条边被经过的次数
     */
    public static int[] ans = new int[MAXN];
    
    /**
     * 向链式前向星中添加一条边
     * 
     * @param u 边的起始节点
     * @param v 边的结束节点
     * @param id 边的编号
     */
    public static void addEdge(int u, int v, int id) {
        // 添加u到v的边
        next[cnt] = head[u];
        to[cnt] = v;
        edgeId[cnt] = id;  // 记录边的编号
        head[u] = cnt++;
    }
    
    /**
     * 第一次DFS，预处理每个节点的深度和倍增跳跃数组
     * 该DFS构建LCA所需的数据结构
     * 
     * @param u 当前处理的节点
     * @param fa 当前节点的父节点
     */
    public static void dfs(int u, int fa) {
        // 设置当前节点的深度（父节点深度+1）
        depth[u] = depth[fa] + 1;
        // 设置当前节点的直接父节点
        stjump[u][0] = fa;
        
        // 预处理倍增数组，stjump[u][p]表示u的2^p级祖先
        // 利用动态规划的思想：u的2^p级祖先 = u的2^(p-1)级祖先的2^(p-1)级祖先
        for (int p = 1; p < LIMIT; p++) {
            stjump[u][p] = stjump[stjump[u][p-1]][p-1];
        }
        
        // 深度优先遍历所有子节点
        for (int e = head[u]; e != 0; e = next[e]) {
            int v = to[e];
            // 避免回到父节点，造成无限递归
            if (v != fa) {
                dfs(v, u);
            }
        }
    }
    
    /**
     * 使用倍增法计算两个节点的最近公共祖先(LCA)
     * 
     * @param a 第一个节点
     * @param b 第二个节点
     * @return a和b的最近公共祖先
     * 
     * 算法步骤：
     * 1. 确保a的深度不小于b
     * 2. 将a向上跳跃到与b同一深度
     * 3. 如果此时a==b，则直接返回a作为LCA
     * 4. 否则，a和b同时向上跳跃，直到它们的父节点相同
     * 5. 返回最终的父节点作为LCA
     */
    public static int lca(int a, int b) {
        // 步骤1：确保a的深度不小于b
        if (depth[a] < depth[b]) {
            int temp = a;
            a = b;
            b = temp;
        }
        
        // 步骤2：将a向上跳到与b同一深度
        // 从最高幂次开始尝试跳跃，确保最大步长
        for (int p = LIMIT - 1; p >= 0; p--) {
            if (depth[stjump[a][p]] >= depth[b]) {
                a = stjump[a][p];
            }
        }
        
        // 步骤3：如果a和b相遇，说明找到了LCA
        if (a == b) return a;
        
        // 步骤4：同时向上跳跃，直到找到LCA的直接子节点
        for (int p = LIMIT - 1; p >= 0; p--) {
            if (stjump[a][p] != stjump[b][p]) {
                a = stjump[a][p];
                b = stjump[b][p];
            }
        }
        
        // 步骤5：返回它们的父节点作为LCA
        return stjump[a][0];
    }
    
    /**
     * 第二次DFS，通过回溯累加子节点的差分标记，计算每条边被经过的最终次数
     * 这是树上边差分的关键步骤，将局部标记转化为全局计数
     * 
     * @param u 当前处理的节点
     * @param fa 当前节点的父节点
     */
    public static void dfsCalc(int u, int fa) {
        // 遍历当前节点的所有邻接边
        for (int e = head[u]; e != 0; e = next[e]) {
            int v = to[e];
            // 只处理子节点（避免回到父节点）
            if (v != fa) {
                // 递归处理子节点
                dfsCalc(v, u);
                
                // 将子节点的差分标记累加到当前节点
                diff[u] += diff[v];
                
                // 将子节点的最终计数存储到对应边的答案数组中
                ans[edgeId[e]] = diff[v];
            }
        }
    }
    
    /**
     * 主函数，处理输入、算法执行和输出
     * 
     * 输入格式：
     * 第一行：节点数n
     * 接下来n-1行：每行两个整数，表示树的边
     * 接下来一行：操作数k
     * 接下来k行：每行两个整数，表示一次操作的两个节点
     * 
     * 输出格式：
     * 一行n-1个整数，表示每条边被经过的次数
     */
    public static void main(String[] args) throws IOException {
        // 使用高效的输入输出方式
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取节点数量
        int n = Integer.parseInt(br.readLine().trim());
        
        // 读入边并构建无向树
        for (int i = 1; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            // 无向树需要添加双向边，并记录边的编号
            addEdge(u, v, i);
            addEdge(v, u, i);
        }
        
        // 预处理LCA所需的深度数组和倍增数组
        // 设置根节点的父节点深度为-1，避免越界
        depth[0] = -1;
        dfs(1, 0);
        
        // 读取操作数
        int k = Integer.parseInt(br.readLine().trim());
        
        // 处理每对节点 - 执行树上边差分
        for (int i = 0; i < k; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            
            // 计算u和v的最近公共祖先
            int l = lca(u, v);
            
            /**
             * 树上边差分核心操作：
             * 对于路径u->v，我们希望路径上的所有边计数加1
             * 通过边差分技巧，我们只需要修改三个点：
             * 1. diff[u]++ - 在u点增加标记
             * 2. diff[v]++ - 在v点增加标记
             * 3. diff[lca] -= 2 - 在LCA处抵消多余的标记
             * 
             * 这样，当执行dfsCalc回溯累分时，整个路径上的边都会被正确计数
             */
            diff[u] += 1;
            diff[v] += 1;
            diff[l] -= 2;
        }
        
        // 执行第二次DFS，通过回溯累加子节点的差分标记，计算每条边的最终经过次数
        dfsCalc(1, 0);
        
        // 输出每条边的经过次数
        for (int i = 1; i < n; i++) {
            out.print(ans[i] + " ");
        }
        out.println();
        
        // 关闭资源
        out.flush();
        out.close();
    }
}