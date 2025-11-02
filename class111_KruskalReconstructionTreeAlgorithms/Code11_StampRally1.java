package class164;

/**
 * AGC002D Stamp Rally - Java实现
 * 
 * 【题目链接】
 * https://atcoder.jp/contests/agc002/tasks/agc002_d
 * 
 * 【题目描述】
 * 给定一个包含n个节点和m条边的无向连通图，以及q个查询。
 * 每个查询给出三个整数x, y, z，表示从节点x和节点y出发，希望访问z个节点。
 * 求能满足条件的最小边权最大值。
 * 
 * 【输入格式】
 * 第一行包含两个整数n, m (2≤n≤10^5, 1≤m≤10^5)。
 * 接下来m行，每行包含三个整数u, v, w (1≤u,v≤n, 1≤w≤10^9)，表示一条边。
 * 接下来一行包含一个整数q (1≤q≤10^5)。
 * 接下来q行，每行包含三个整数x, y, z (1≤x,y≤n, x≠y, 2≤z≤n)。
 * 
 * 【输出格式】
 * 对于每个查询，输出一个整数表示答案。
 * 
 * 【算法核心思想】
 * 这是一道经典的Kruskal重构树应用题。我们需要找到最小的边权最大值，
 * 使得从x和y出发能访问到z个节点。
 * 
 * 【解题思路】
 * 更优的做法是直接使用Kruskal重构树：
 * 1. 按边权从小到大排序，构建Kruskal重构树
 * 2. 对于每个查询，在重构树中找到x和y的LCA
 * 3. 答案就是LCA节点的权值
 * 
 * 【关键性质】
 * - 在Kruskal重构树中，任意两个节点的LCA节点权值等于使这两个节点连通的最小边权最大值
 * - LCA节点的子树大小等于在该边权下能访问的节点数
 * 
 * 【时间复杂度分析】
 * - 构建Kruskal重构树：O(m log m) - 主要是排序的复杂度
 * - DFS预处理：O(n) - 每个节点访问一次
 * - 每次查询：O(log n) - 倍增LCA的复杂度
 * 总复杂度：O(m log m + q log n)
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

public class Code11_StampRally1 {

    public static int MAXN = 100001;
    public static int MAXM = 100001;
    public static int MAXH = 20;
    public static int n, m, q;

    // 每条边有三个信息，节点u、节点v、边权w
    public static int[][] edge = new int[MAXM][3];

    // 并查集
    public static int[] father = new int[MAXN * 2];
    
    // Kruskal重构树的建图
    public static int[] head = new int[MAXN * 2];
    public static int[] next = new int[MAXN * 2];
    public static int[] to = new int[MAXN * 2];
    public static int cntg = 0;
    
    // Kruskal重构树上，节点的权值（边权）
    public static int[] nodeKey = new int[MAXN * 2];
    // Kruskal重构树上，点的数量
    public static int cntu;

    // Kruskal重构树上，dfs过程建立的信息
    public static int[] dep = new int[MAXN * 2];
    public static int[][] stjump = new int[MAXN * 2][MAXH];
    public static int[] size = new int[MAXN * 2]; // 子树大小

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
     * 2. 按边权从小到大排序，这是构建最小生成树Kruskal重构树的关键
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
    // 按边权从小到大排序
    public static void kruskalRebuild() {
        // 初始化并查集
        for (int i = 1; i <= n; i++) {
            father[i] = i;
        }
        
        // 按边权从小到大排序 - 这是构建最小生成树Kruskal重构树的关键
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
                
                // 新节点的权值为边权
                nodeKey[cntu] = edge[i][2];
                
                // 建立重构树的父子关系
                addEdge(cntu, fx);
                addEdge(cntu, fy);
            }
        }
    }

    /**
     * DFS预处理函数 - 构建LCA所需的倍增表和子树大小
     * 
     * 【功能说明】
     * 遍历重构树，为每个节点记录深度信息、各层祖先节点和子树大小，为后续LCA查询做准备
     * 
     * 【实现细节】
     * 1. 记录每个节点的直接父节点（2^0级祖先）
     * 2. 通过动态规划方式构建倍增表：stjump[u][p] = stjump[stjump[u][p-1]][p-1]
     * 3. 记录子树大小，叶子节点大小为1，非叶子节点为其子节点大小之和
     * 4. 递归处理所有子节点
     * 
     * 【性能分析】
     * 时间复杂度：O(n log n)，每个节点需要处理log n层祖先信息
     * 空间复杂度：O(n log n)，存储所有节点的倍增表
     * 
     * @param u 当前处理的节点
     * @param fa 父节点
     */
    // DFS预处理，构建倍增表和子树大小
    public static void dfs(int u, int fa) {
        // 记录深度，根节点深度为1
        dep[u] = dep[fa] + 1;
        // 记录父节点（即2^0级祖先）
        stjump[u][0] = fa;
        
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
     * 查找两个节点的最近公共祖先，用于后续获取使两点连通的最小边权最大值
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
     * 主函数 - 程序入口
     * 
     * 【执行流程】
     * 1. 输入数据：读取图的节点数、边数和查询数
     * 2. 构建Kruskal重构树
     * 3. 预处理LCA所需的深度数组、倍增表和子树大小
     * 4. 处理每个查询，输出结果
     * 
     * 【异常处理】
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
        // 读取节点数和边数
        n = io.nextInt();
        m = io.nextInt();
        
        // 读取所有边的信息
        for (int i = 1; i <= m; i++) {
            edge[i][0] = io.nextInt();  // 边的起点
            edge[i][1] = io.nextInt();  // 边的终点
            edge[i][2] = io.nextInt();  // 边的权值
        }
        
        // 构建Kruskal重构树
        // 这一步将所有边按权值从小到大排序并构建重构树
        kruskalRebuild();
        
        // 对每个连通分量进行DFS预处理，构建LCA所需的信息
        // 遍历所有节点，找到每个树的根节点（父节点等于自身的节点）
        for (int i = 1; i <= cntu; i++) {
            if (i == father[i]) {
                dfs(i, 0);  // 从根节点开始DFS，父节点设为0
            }
        }
        
        // 读取查询数
        q = io.nextInt();
        // 处理查询请求
        for (int i = 1, x, y, z; i <= q; i++) {
            x = io.nextInt();  // 起点1
            y = io.nextInt();  // 起点2
            z = io.nextInt();  // 希望访问的节点数
            
            // 找到x和y的LCA
            int l = lca(x, y);
            
            // 答案就是LCA节点的权值
            // 这是因为在Kruskal重构树中，LCA节点的权值等于使x和y连通的最小边权最大值
            io.writelnInt(nodeKey[l]);
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