package class164;

/**
 * Codeforces 1706E Qpwoeirut and Vertices - Java实现
 * 
 * 【题目链接】
 * https://codeforces.com/contest/1706/problem/E
 * 
 * 【题目描述】
 * 给定一个包含n个节点和m条边的无向图，以及q个查询。
 * 每个查询给出一个区间[l,r]，要求找出使得区间[l,r]内所有节点都连通的最少边数。
 * 注意：这些边必须是原图中编号从1到某个值的连续边。
 * 
 * 【输入格式】
 * 第一行包含一个整数t，表示测试用例数量。
 * 每个测试用例的第一行包含三个整数n, m, q (2≤n≤10^5, 1≤m,q≤2⋅10^5)。
 * 接下来m行，每行包含两个整数u, v (1≤u,v≤n, u≠v)，表示一条边。
 * 接下来q行，每行包含两个整数l, r (1≤l≤r≤n)，表示一个查询。
 * 
 * 【输出格式】
 * 对于每个查询，输出一个整数表示答案。
 * 
 * 【算法核心思想】
 * 这是一道典型的Kruskal重构树应用题。由于要求的是使得区间[l,r]内所有节点都连通的最少边数，
 * 我们可以将边按照编号排序，然后构建Kruskal重构树。
 * 
 * 【解题思路】
 * 1. 构建Kruskal重构树，将边按照编号排序
 * 2. 对于每个节点，记录它在重构树中的叶子节点
 * 3. 对于每个查询[l,r]，找到包含这些节点的最小连通子树
 * 4. 这可以通过找到这些节点在重构树中的LCA来实现
 * 
 * 【关键性质】
 * - 在Kruskal重构树中，任意两个节点的LCA节点权值等于使这两个节点连通所需的最少边数
 * - 对于多个节点，它们的最小连通子树的根节点权值等于使这些节点都连通所需的最少边数
 * 
 * 【时间复杂度分析】
 * - 构建Kruskal重构树：O(m log m) - 主要是排序的复杂度
 * - DFS预处理：O(n) - 每个节点访问一次
 * - 每次查询：O((r-l+1) * log n) - 需要计算多个节点的LCA
 * 总复杂度：O(m log m + q * (r-l+1) * log n)
 * 
 * 【空间复杂度分析】
 * - 存储边：O(m)
 * - 存储图和重构树：O(n)
 * - 倍增表：O(n log n)
 * 总空间复杂度：O(n log n + m)
 * 
 * 【工程化考量】
 * 1. 异常处理：处理节点不连通的情况
 * 2. 性能优化：使用快速IO和路径压缩并查集
 * 3. 内存管理：重构树节点数最大为2n-1，注意数组大小
 * 4. 边界处理：处理节点编号从1开始的情况
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class Code10_QpwoeirutAndVertices1 {

    public static int MAXN = 200001;
    public static int MAXM = 200001;
    public static int MAXH = 20;
    public static int n, m, q;

    // 每条边有三个信息，节点u、节点v、边编号i
    public static int[][] edge = new int[MAXM][3];

    // 并查集
    public static int[] father = new int[MAXN * 2];
    
    // Kruskal重构树的建图
    public static int[] head = new int[MAXN * 2];
    public static int[] next = new int[MAXN * 2];
    public static int[] to = new int[MAXN * 2];
    public static int cntg = 0;
    
    // Kruskal重构树上，节点的权值（边编号）
    public static int[] nodeKey = new int[MAXN * 2];
    // Kruskal重构树上，点的数量
    public static int cntu;
    
    // 每个原始节点在重构树中对应的叶子节点
    public static int[] leaf = new int[MAXN];

    // Kruskal重构树上，dfs过程建立的信息
    public static int[] dep = new int[MAXN * 2];
    public static int[][] stjump = new int[MAXN * 2][MAXH];
    public static int[] dfn = new int[MAXN * 2];
    public static int[] size = new int[MAXN * 2];
    public static int dfntime = 0;

    /**
     * 并查集查找函数 - 带路径压缩优化
     * 时间复杂度：近似O(1)，均摊复杂度为α(n)，其中α是阿克曼函数的反函数
     * @param i 要查找的节点
     * @return 节点所在集合的根节点
     */
    public static int find(int i) {
        if (i != father[i]) {
            // 路径压缩：将查询路径上的每个节点直接连到根节点
            father[i] = find(father[i]);
        }
        return father[i];
    }

    /**
     * 邻接表添加边函数
     * 采用头插法构建邻接表
     * @param u 边的起点
     * @param v 边的终点
     */
    public static void addEdge(int u, int v) {
        next[++cntg] = head[u];
        to[cntg] = v;
        head[u] = cntg;
    }

    /**
     * 构建Kruskal重构树的核心函数
     * 
     * 【实现细节】
     * 1. 初始化并查集，每个节点的父节点初始化为自身
     * 2. 按边编号从小到大排序，这是构建Kruskal重构树的关键
     * 3. 遍历排序后的边，使用并查集检查连通性
     * 4. 当发现不在同一连通分量的边时，创建新节点并构建重构树
     * 
     * 【关键性质】
     * - 重构树的叶子节点是原图中的所有节点
     * - 重构树满足大根堆性质：每个非叶子节点的权值大于等于其子节点的权值
     * - 原图中两点间的最小瓶颈等于它们在重构树上的LCA节点权值
     * 
     * 【边界处理】
     * - 处理节点编号从1开始的情况
     * - 确保cntu不会超过数组大小限制（最大为2n-1）
     */
    // 构建Kruskal重构树
    // 按边编号从小到大排序
    public static void kruskalRebuild() {
        // 初始化并查集
        for (int i = 1; i <= n; i++) {
            father[i] = i;
        }
        
        // 按边编号从小到大排序 - 这是构建Kruskal重构树的关键
        Arrays.sort(edge, 1, m + 1, (a, b) -> a[2] - b[2]);
        
        // 初始化重构树的节点数目为原图的节点数目
        cntu = n;
        
        // 遍历所有边
        for (int i = 1, fx, fy; i <= m; i++) {
            // 查找两个端点所在集合的根
            fx = find(edge[i][0]);
            fy = find(edge[i][1]);
            
            // 如果不在同一连通分量
            if (fx != fy) {
                // 创建新节点，合并两个连通分量
                father[fx] = father[fy] = ++cntu;
                father[cntu] = cntu;
                
                // 新节点的权值为边编号
                nodeKey[cntu] = edge[i][2];
                
                // 建立重构树的父子关系
                addEdge(cntu, fx);
                addEdge(cntu, fy);
            }
        }
    }

    /**
     * DFS预处理函数 - 构建LCA所需的倍增表
     * 
     * 【功能说明】
     * 遍历重构树，为每个节点记录深度信息和各层祖先节点，为后续LCA查询做准备
     * 同时记录DFS序和子树大小，用于后续优化
     * 
     * 【实现细节】
     * 1. 记录每个节点的直接父节点（2^0级祖先）
     * 2. 通过动态规划方式构建倍增表：stjump[u][p] = stjump[stjump[u][p-1]][p-1]
     * 3. 记录DFS序和子树大小
     * 4. 递归处理所有子节点
     * 
     * 【性能分析】
     * 时间复杂度：O(n log n)，每个节点需要处理log n层祖先信息
     * 空间复杂度：O(n log n)，存储所有节点的倍增表
     * 
     * @param u 当前处理的节点
     * @param fa 父节点
     */
    // DFS预处理，构建倍增表
    public static void dfs(int u, int fa) {
        // 记录深度，根节点深度为1
        dep[u] = dep[fa] + 1;
        // 记录父节点（即2^0级祖先）
        stjump[u][0] = fa;
        // 记录DFS序
        dfn[u] = ++dfntime;
        
        // 构建倍增表 - 通过动态规划递推各层祖先
        for (int p = 1; p < MAXH; p++) {
            // 状态转移方程：节点u的2^p级祖先等于其2^(p-1)级祖先的2^(p-1)级祖先
            stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
        }
        
        // 记录子树大小
        size[u] = (u <= n) ? 1 : 0; // 叶子节点size为1
        // 递归处理所有子节点
        for (int e = head[u]; e > 0; e = next[e]) {
            dfs(to[e], u);
            size[u] += size[to[e]];
        }
    }

    /**
     * 倍增法查询LCA（最近公共祖先）
     * 
     * 【功能说明】
     * 查找两个节点的最近公共祖先，用于后续获取使两点连通所需的最少边数
     * 
     * 【实现步骤】
     * 1. 将较深的节点提升到较浅节点的深度
     * 2. 如果此时两节点相同，则为LCA
     * 3. 否则，同时提升两个节点直到它们的父节点相同
     * 4. 返回共同的父节点
     * 
     * 【性能分析】
     * 时间复杂度：O(log n)，每次查询需要O(log n)次操作
     * 
     * @param a 第一个节点
     * @param b 第二个节点
     * @return 两节点的最近公共祖先
     */
    // 计算两点的最近公共祖先(LCA)
    public static int lca(int a, int b) {
        // 保证a在更深的位置
        if (dep[a] < dep[b]) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        
        // 将a提升到和b同一深度 - 使用二进制拆分思想
        for (int p = MAXH - 1; p >= 0; p--) {
            if (dep[stjump[a][p]] >= dep[b]) {
                a = stjump[a][p];
            }
        }
        
        // 如果此时a==b，说明b是a的祖先，直接返回
        if (a == b) {
            return a;
        }
        
        // 同时向上提升a和b，直到它们的父节点相同
        for (int p = MAXH - 1; p >= 0; p--) {
            if (stjump[a][p] != stjump[b][p]) {
                a = stjump[a][p];
                b = stjump[b][p];
            }
        }
        
        // 返回共同的父节点，即为LCA
        return stjump[a][0];
    }

    /**
     * 找到包含[l,r]区间内所有节点的最小连通子树的根节点
     * 
     * 【功能说明】
     * 对于区间[l,r]内的所有节点，找到使它们都连通的最小边数
     * 
     * 【实现步骤】
     * 1. 特殊情况处理：只有一个节点时直接返回该节点在重构树中的叶子节点
     * 2. 对于多个节点，依次计算它们的LCA
     * 3. 返回最终的LCA节点，其权值即为答案
     * 
     * 【性能分析】
     * 时间复杂度：O((r-l+1) * log n)
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 包含区间内所有节点的最小连通子树的根节点
     */
    // 找到包含[l,r]区间内所有节点的最小连通子树的根节点
    public static int findSubtreeRoot(int l, int r) {
        // 特殊情况：只有一个节点
        if (l == r) {
            return leaf[l];
        }
        
        // 找到[l,r]区间内节点的LCA
        int root = leaf[l];
        for (int i = l + 1; i <= r; i++) {
            root = lca(root, leaf[i]);
        }
        
        return root;
    }

    /**
     * 主函数 - 程序入口
     * 
     * 【执行流程】
     * 1. 输入数据：读取测试用例数、图的节点数、边数和查询数
     * 2. 构建Kruskal重构树
     * 3. 预处理LCA所需的深度数组和倍增表
     * 4. 处理每个查询，输出结果
     * 
     * 【异常处理】
     * - 处理多个测试用例
     * - 使用快速IO模式处理大规模数据
     * 
     * 【性能优化】
     * - 使用快速IO类加速输入输出
     * - 预处理LCA信息以支持高效查询
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 初始化快速IO工具
        FastIO io = new FastIO(System.in, System.out);
        // 读取测试用例数
        int t = io.nextInt();
        
        for (int cases = 0; cases < t; cases++) {
            // 读取节点数、边数和查询数
            n = io.nextInt();
            m = io.nextInt();
            q = io.nextInt();
            
            // 初始化
            cntg = 0;
            dfntime = 0;
            Arrays.fill(head, 1, cntu + 1, 0);
            
            // 读取所有边的信息
            for (int i = 1; i <= m; i++) {
                edge[i][0] = io.nextInt();  // 边的起点
                edge[i][1] = io.nextInt();  // 边的终点
                edge[i][2] = i; // 边编号就是i
            }
            
            // 构建Kruskal重构树
            // 这一步将所有边按编号从小到大排序并构建重构树
            kruskalRebuild();
            
            // 对每个连通分量进行DFS预处理，构建LCA所需的信息
            // 遍历所有节点，找到每个树的根节点（父节点等于自身的节点）
            for (int i = 1; i <= cntu; i++) {
                if (i == father[i]) {
                    dfs(i, 0);  // 从根节点开始DFS，父节点设为0
                }
            }
            
            // 记录每个原始节点在重构树中对应的叶子节点
            for (int i = 1; i <= n; i++) {
                leaf[i] = i;
            }
            
            // 处理查询请求
            for (int i = 1, l, r; i <= q; i++) {
                l = io.nextInt();  // 区间左端点
                r = io.nextInt();  // 区间右端点
                
                // 找到包含[l,r]区间内所有节点的最小连通子树的根节点
                int root = findSubtreeRoot(l, r);
                
                // 输出该根节点对应的边编号（即最少边数）
                io.writelnInt(nodeKey[root]);
            }
        }
        // 确保所有输出都被写入
        io.flush();
    }

    // 读写工具类
    static class FastIO {
        private final InputStream is;
        private final OutputStream os;
        private final byte[] inbuf = new byte[1 << 16];
        private int lenbuf = 0;
        private int ptrbuf = 0;
        private final StringBuilder outBuf = new StringBuilder();

        public FastIO(InputStream is, OutputStream os) {
            this.is = is;
            this.os = os;
        }

        private int readByte() {
            if (ptrbuf >= lenbuf) {
                ptrbuf = 0;
                try {
                    lenbuf = is.read(inbuf);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (lenbuf == -1) {
                    return -1;
                }
            }
            return inbuf[ptrbuf++] & 0xff;
        }

        private int skip() {
            int b;
            while ((b = readByte()) != -1) {
                if (b > ' ') {
                    return b;
                }
            }
            return -1;
        }

        public int nextInt() {
            int b = skip();
            if (b == -1) {
                throw new RuntimeException("No more integers (EOF)");
            }
            boolean negative = false;
            if (b == '-') {
                negative = true;
                b = readByte();
            }
            int val = 0;
            while (b >= '0' && b <= '9') {
                val = val * 10 + (b - '0');
                b = readByte();
            }
            return negative ? -val : val;
        }

        public void write(String s) {
            outBuf.append(s);
        }

        public void writeInt(int x) {
            outBuf.append(x);
        }

        public void writelnInt(int x) {
            outBuf.append(x).append('\n');
        }

        public void flush() {
            try {
                os.write(outBuf.toString().getBytes());
                os.flush();
                outBuf.setLength(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}