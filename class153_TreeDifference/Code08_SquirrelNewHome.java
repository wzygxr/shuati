package class122;

/**
 * 松鼠的新家 (洛谷 P3258)
 * 题目来源：洛谷
 * 题目链接：https://www.luogu.com.cn/problem/P3258
 * 
 * 题目描述：
 * 松鼠家族的成员需要在树上移动，从一个节点到另一个节点。
 * 给定一棵包含N个节点的树，以及N-1次移动操作。
 * 每次移动操作表示从节点a移动到节点b，经过的路径上的所有节点（包括起点和终点）都会被访问一次。
 * 求每个节点被访问的次数。
 * 
 * 算法原理：树上点差分
 * 树上差分是一种将路径操作转化为点标记操作的高效算法。
 * 对于树上的路径u->v，我们需要让路径上的所有节点计数加1。
 * 通过点差分，我们可以：
 * 1. diff[u]++
 * 2. diff[v]++
 * 3. diff[lca(u,v)]--
 * 4. diff[parent(lca(u,v))]--
 * 最后通过一次DFS回溯累加子节点的差分标记，得到每个节点的最终计数。
 * 
 * 时间复杂度分析：
 * - 预处理LCA：O(N log N)
 * - 差分标记：O(M)，其中M是操作次数
 * - DFS回溯统计：O(N)
 * 总时间复杂度：O(N log N + M)，对于本题M=N-1，所以总时间复杂度为O(N log N)
 * 
 * 空间复杂度分析：
 * - 树的存储：O(N)
 * - LCA倍增数组：O(N log N)
 * - 差分数组：O(N)
 * 总空间复杂度：O(N log N)
 * 
 * 工程化考量：
 * 1. 使用链式前向星存储树结构，节省空间
 * 2. 采用BufferedReader + StreamTokenizer进行快速输入，提高处理大数据的能力
 * 3. 使用PrintWriter进行高效输出
 * 4. 预处理log2值，优化倍增数组的大小
 * 5. 递归DFS实现简洁明了，但需注意递归深度问题
 * 
 * 最优解分析：
 * 树上差分是解决此类路径覆盖问题的最优解，相比暴力遍历每条路径的O(N*M)复杂度，
 * 树上差分可以将时间复杂度优化到O(N log N)，在大规模数据下效率提升显著。
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code08_SquirrelNewHome {

    /**
     * 最大节点数，根据题目数据范围设定
     * 题目中N最大为3e5，设置为300001以避免越界
     */
    public static final int MAXN = 300001;
    
    /**
     * 倍增数组的大小限制
     * 2^19 = 524,288 > 3e5，足够处理最大节点数
     */
    public static final int LIMIT = 19;
    
    // 节点数量
    public static int n;
    
    // 倍增数组的幂次
    public static int power;
    
    /**
     * 计算log2(n)的整数部分，用于确定倍增数组需要的最大幂次
     * 
     * @param n 输入的节点数
     * @return 最大的k使得2^k <= n/2
     * 
     * 该方法通过位运算高效计算log2值，避免使用Math.log函数带来的精度问题
     */
    public static int log2(int n) {
        int ans = 0;
        while ((1 << ans) <= (n >> 1)) {
            ans++;
        }
        return ans;
    }
    
    // 差分数组，用于记录每个节点被访问的次数
    public static int[] diff = new int[MAXN];
    
    // 链式前向星存储树结构
    public static int[] head = new int[MAXN];
    // 使用next_edge避免与关键字冲突
    public static int[] next_edge = new int[MAXN << 1];
    public static int[] to = new int[MAXN << 1];
    public static int edgeCount;
    
    // 深度数组和倍增跳跃数组，用于LCA计算
    public static int[] depth = new int[MAXN];
    public static int[][] stjump = new int[MAXN][LIMIT];
    
    /**
     * 初始化函数，重置数据结构
     * 
     * 功能：
     * 1. 计算倍增数组所需的最大幂次
     * 2. 初始化差分数组和邻接表
     * 3. 重置边计数器
     */
    public static void build() {
        power = log2(n);
        // 只初始化有效范围的数组元素，提高效率
        Arrays.fill(diff, 1, n + 1, 0);
        edgeCount = 1;  // 边编号从1开始
        Arrays.fill(head, 1, n + 1, 0);
    }
    
    /**
     * 向树中添加一条无向边
     * 
     * @param u 边的起始节点
     * @param v 边的结束节点
     * 
     * 注意：由于树是无向的，需要调用两次addEdge(u, v)和addEdge(v, u)
     */
    public static void addEdge(int u, int v) {
        next_edge[edgeCount] = head[u];
        to[edgeCount] = v;
        head[u] = edgeCount++;
    }
    
    /**
     * 第一次DFS，预处理每个节点的深度和倍增跳跃数组
     * 该DFS构建LCA所需的数据结构
     * 
     * @param u 当前处理的节点
     * @param f 当前节点的父节点
     * 
     * 时间复杂度：O(N log N)，每个节点需要处理log N次倍增跳跃
     */
    public static void dfs1(int u, int f) {
        // 设置当前节点的深度（父节点深度+1）
        depth[u] = depth[f] + 1;
        // 设置当前节点的直接父节点
        stjump[u][0] = f;
        
        // 预处理倍增数组，stjump[u][p]表示u的2^p级祖先
        // 利用动态规划的思想：u的2^p级祖先 = u的2^(p-1)级祖先的2^(p-1)级祖先
        for (int p = 1; p <= power; p++) {
            stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
        }
        
        // 深度优先遍历所有子节点
        for (int e = head[u]; e != 0; e = next_edge[e]) {
            int v = to[e];
            // 避免回到父节点，造成无限递归
            if (v != f) {
                dfs1(v, u);
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
     * 
     * 时间复杂度：O(log N)，每次查询最多跳跃log N次
     */
    public static int lca(int a, int b) {
        // 步骤1：确保a的深度不小于b
        if (depth[a] < depth[b]) {
            // 交换a和b
            int tmp = a;
            a = b;
            b = tmp;
        }
        
        // 步骤2：将a向上跳到与b同一深度
        // 从最高幂次开始尝试跳跃，确保最大步长
        for (int p = power; p >= 0; p--) {
            // 只有当跳跃后的深度不小于b的深度时才跳跃
            if (depth[stjump[a][p]] >= depth[b]) {
                a = stjump[a][p];
            }
        }
        
        // 步骤3：如果a和b相遇，说明找到了LCA
        if (a == b) {
            return a;
        }
        
        // 步骤4：同时向上跳跃，直到找到LCA的直接子节点
        for (int p = power; p >= 0; p--) {
            if (stjump[a][p] != stjump[b][p]) {
                a = stjump[a][p];
                b = stjump[b][p];
            }
        }
        
        // 步骤5：返回它们的父节点作为LCA
        return stjump[a][0];
    }
    
    /**
     * 第二次DFS，通过回溯累加子节点的差分标记，计算每个节点被访问的最终次数
     * 这是树上差分的关键步骤，将局部标记转化为全局计数
     * 
     * @param u 当前处理的节点
     * @param f 当前节点的父节点
     * 
     * 时间复杂度：O(N)，每个节点和边只被访问一次
     * 
     * 注意：该DFS必须在所有差分标记完成后执行
     */
    public static void dfs2(int u, int f) {
        // 步骤1：先递归处理所有子节点
        // 采用后序遍历的方式，确保子节点的计数先计算完成
        for (int e = head[u], v; e != 0; e = next_edge[e]) {
            v = to[e];
            if (v != f) {
                dfs2(v, u);
            }
        }
        
        // 步骤2：将子节点的访问次数累加到当前节点
        // 这一步实现了差分标记的传播和累加
        for (int e = head[u], v; e != 0; e = next_edge[e]) {
            v = to[e];
            if (v != f) {
                diff[u] += diff[v];
            }
        }
        
        // 注意：此时diff[u]已经包含了该节点的所有子树中的差分标记累加结果
        // 对于根节点，其diff值即为整个树的总覆盖次数
    }
    
    /**
     * 主函数，处理输入、算法执行和输出
     * 
     * 输入格式：
     * 第一行：节点数n
     * 第二行：n个整数，表示节点访问顺序
     * 接下来n-1行：每行两个整数，表示树的边
     * 
     * 输出格式：
     * 输出n行，每行一个整数，表示每个节点被访问的次数
     */
    public static void main(String[] args) throws IOException {
// 使用高效的输入输出方式
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取节点数量
        in.nextToken();
        n = (int) in.nval;
        
        // 初始化
        build();
        
        // 读取每个节点的访问顺序
        int[] order = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            order[i] = (int) in.nval;
        }
        
        // 读取树的边，构建无向树
        for (int i = 1, u, v; i < n; i++) {
            in.nextToken();
            u = (int) in.nval;
            in.nextToken();
            v = (int) in.nval;
            // 无向树需要添加双向边
            addEdge(u, v);
            addEdge(v, u);
        }
        
        // 预处理深度和倍增数组（根节点设为1）
        dfs1(1, 0);
        
        // 处理每次移动操作 - 执行树上点差分
        for (int i = 1, u, v, lcaNode, lcaFather; i < n; i++) {
            u = order[i];      // 当前移动的起点
            v = order[i + 1];  // 当前移动的终点
            lcaNode = lca(u, v);   // 计算u和v的最近公共祖先
            lcaFather = stjump[lcaNode][0]; // LCA的父节点
            
            /**
             * 树上点差分核心操作：
             * 对于路径u->v，我们希望路径上的所有节点计数加1
             * 通过差分技巧，我们只需要修改四个点：
             * 1. diff[u]++ - 在起点增加标记
             * 2. diff[v]++ - 在终点增加标记
             * 3. diff[lca_node]-- - 在LCA处抵消一次（因为u和v都会到达LCA）
             * 4. diff[lcafather]-- - 在LCA的父节点处抵消一次
             * 
             * 这样，当执行dfs2回溯累分时，整个路径上的节点都会被正确计数
             */
            diff[u]++;
            diff[v]++;
            diff[lcaNode]--;
            diff[lcaFather]--;
        }
        
        // 执行第二次DFS，通过回溯累加子节点的差分标记，计算每个节点的最终访问次数
        dfs2(1, 0);
        
        // 输出结果，需要注意题目中的特殊处理
        /**
         * 为什么需要特殊处理？
         * 因为题目中松鼠的移动路径是连续的，除了最后一个终点外，
         * 每个节点如果是某次移动的终点，它也会是下一次移动的起点。
         * 但实际上，松鼠在移动时，起点只算一次访问，而不是两次。
         * 因此，除了最后一个节点外，其他节点的访问次数需要减1。
         */
        for (int i = 1; i <= n; i++) {
            // 最后一个节点（即order[n]）不需要减1
            if (i == order[n]) {
                out.println(diff[i]);
            } else {
                // 其他节点需要减1
                out.println(diff[i] - 1);
            }
        }
        
        // 关闭资源
        out.flush();
        out.close();
        br.close();
    }
}