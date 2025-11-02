package class164;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * P1967 [NOIP2013 提高组] 货车运输 - Java实现
 * 
 * 【题目描述】
 * A国有n座城市，编号从1到n，城市之间有m条双向道路。每一条道路对车辆都有重量限制，简称限重。
 * 现在有q辆货车在运输货物，司机们想知道每辆车在不超过车辆限重的情况下，最多能运多重的货物。
 * 若货车无法到达目的地，输出-1。
 * 
 * 【输入格式】
 * 第一行有两个用一个空格隔开的整数n,m，表示A国有n座城市和m条道路。
 * 接下来m行每行三个整数x, y, z，每两个整数之间用一个空格隔开，表示从x号城市到y号城市有一条限重为z的道路。
 * 注意：x≠y，两座城市之间可能有多条道路。
 * 接下来一行有一个整数q，表示有q辆货车需要运货。
 * 接下来q行，每行两个整数x,y，之间用一个空格隔开，表示一辆货车需要从x城市运输货物到y城市，保证x≠y。
 * 
 * 【输出格式】
 * 共有q行，每行一个整数，表示对于每一辆货车，它的最大载重是多少。
 * 如果货车不能到达目的地，输出-1。
 * 
 * 【算法核心思想】
 * 这是一个典型的最大瓶颈路径问题，要求找出从s到t的所有路径中，最小边权的最大值。
 * 解决此类问题的最优方法是使用Kruskal重构树。
 * 
 * 【解题思路】
 * 1. 将所有边按边权从大到小排序，构建最大生成树的Kruskal重构树
 * 2. 重构树中，每个原始节点是叶子节点，内部节点代表边
 * 3. 重构树满足小根堆性质（根节点权值最大，向下递减）
 * 4. 两点间路径的最小边权最大值等于它们在重构树上的LCA节点权值
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
 * 1. 异常处理：处理节点不连通的情况，输出-1
 * 2. 性能优化：使用快速IO和路径压缩并查集
 * 3. 内存管理：重构树节点数最大为2n-1，注意数组大小
 * 4. 边界处理：处理节点编号从1开始的情况
 */

public class Code08_TruckTransport1 {

    public static int MAXN = 10001;
    public static int MAXM = 50001;
    public static int MAXH = 16;
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
    
    // Kruskal重构树上，节点的权值
    public static int[] nodeKey = new int[MAXN * 2];
    // Kruskal重构树上，点的数量
    public static int cntu;

    // Kruskal重构树上，dfs过程建立的信息
    public static int[] dep = new int[MAXN * 2];
    public static int[][] stjump = new int[MAXN * 2][MAXH];

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
     * 构建Kruskal重构树的核心函数 - 针对最大生成树
     * 
     * 【实现细节】
     * 1. 初始化并查集，每个节点的父节点初始化为自身
     * 2. 按边权从大到小排序，这是构建最大生成树Kruskal重构树的关键
     * 3. 遍历排序后的边，使用并查集检查连通性
     * 4. 当发现不在同一连通分量的边时，创建新节点并构建重构树
     * 
     * 【关键性质】
     * - 重构树的叶子节点是原图中的所有节点
     * - 重构树满足小根堆性质：每个非叶子节点的权值小于等于其子节点的权值
     * - 原图中两点间的最大瓶颈等于它们在重构树上的LCA节点权值
     * 
     * 【边界处理】
     * - 处理节点编号从1开始的情况
     * - 确保cntu不会超过数组大小限制（最大为2n-1）
     */
    public static void kruskalRebuild() {
        // 初始化并查集
        for (int i = 1; i <= n; i++) {
            father[i] = i;
        }
        
        // 按边权从大到小排序 - 这是构建最大生成树Kruskal重构树的关键
        Arrays.sort(edge, 1, m + 1, (a, b) -> b[2] - a[2]);
        
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
     * DFS预处理函数 - 构建LCA所需的倍增表
     * 
     * 【功能说明】
     * 遍历重构树，为每个节点记录深度信息和各层祖先节点，为后续LCA查询做准备
     * 
     * 【实现细节】
     * 1. 记录每个节点的直接父节点（2^0级祖先）
     * 2. 通过动态规划方式构建倍增表：stjump[u][p] = stjump[stjump[u][p-1]][p-1]
     * 3. 递归处理所有子节点
     * 
     * 【性能分析】
     * 时间复杂度：O(n log n)，每个节点需要处理log n层祖先信息
     * 空间复杂度：O(n log n)，存储所有节点的倍增表
     * 
     * @param u 当前处理的节点
     * @param fa 父节点
     */
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
        
        // 递归处理所有子节点
        for (int e = head[u]; e > 0; e = next[e]) {
            dfs(to[e], u);
        }
    }

    /**
     * 倍增法查询LCA（最近公共祖先）
     * 
     * 【功能说明】
     * 查找两个节点的最近公共祖先，用于后续获取路径最小边权的最大值
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
     * 2. 构建Kruskal重构树（最大生成树版本）
     * 3. 预处理LCA所需的深度数组和倍增表
     * 4. 处理每个查询，输出结果
     * 
     * 【异常处理】
     * - 处理节点不连通的情况，输出-1
     * - 使用快速IO模式处理大规模数据
     * 
     * 【性能优化】
     * - 使用FastIO类加速输入输出
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
            edge[i][2] = io.nextInt();  // 边的限重
        }
        
        // 构建Kruskal重构树（基于最大生成树）
        // 这一步将所有边按权值从大到小排序并构建重构树
        kruskalRebuild();
        
        // 对每个连通分量进行DFS预处理，构建LCA所需的信息
        // 遍历所有节点，找到每个树的根节点（父节点等于自身的节点）
        for (int i = 1; i <= cntu; i++) {
            if (i == father[i]) {
                dfs(i, 0);  // 从根节点开始DFS，父节点设为0
            }
        }
        
        // 处理查询请求
        q = io.nextInt();
        for (int i = 1, x, y; i <= q; i++) {
            x = io.nextInt();  // 起点城市
            y = io.nextInt();  // 终点城市
            
            // 判断两点是否连通
            // 在Kruskal重构树中，如果两个点不连通，说明在原图中也无法到达
            if (find(x) != find(y)) {
                io.writelnInt(-1);  // 不连通，输出-1
            } else {
                // 连通情况下，两点间路径的最大瓶颈等于它们LCA节点的权值
                // 这是利用了Kruskal重构树的重要性质
                io.writelnInt(nodeKey[lca(x, y)]);
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