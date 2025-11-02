package class164;

/**
 * U92652 【模板】kruskal重构树 - Java实现
 * 
 * 题目描述：
 * 给出一个有 n 个结点， m 条边的无向图，每条边有一个边权。
 * 求结点 x,y 之间所有路径的中，最长的边最小值是多少，若这两个点之间没有任何路径，输出 -1 。
 * 共有 Q 组询问。
 * 
 * 输入格式：
 * 第一行三个整数 n,m,Q 。
 * 接下来 m 行每行三个整数 x,y,z(1 ≤ x,y ≤ n,1 ≤ z ≤ 1000000) ，表示有一条连接 x 和 y 长度为 z 的边。
 * 接下来 Q 行每行两个整数 x,y(x ≠ y) ，表示一组询问。
 * 
 * 输出格式：
 * Q 行，每行一个整数，表示一组询问的答案。
 * 
 * 【算法核心思想】
 * Kruskal重构树是一种将图论中的路径极值问题转化为树上LCA问题的数据结构。
 * 其关键性质是：原图中两点间所有路径的最大边权的最小值等于重构树上两点LCA的点权。
 * 
 * 【工程化考量】
 * 1. 异常处理：处理两点不连通的情况，输出-1
 * 2. 性能优化：使用快速IO和路径压缩并查集
 * 3. 内存管理：重构树节点数最大为2n-1，注意数组大小
 * 4. 边界处理：处理节点编号从1开始的情况
 * 5. 线程安全：该实现不是线程安全的，如需多线程使用需要加锁保护共享数据
 */
//
/**
 * 【解题思路深度解析】
 * 这是一道Kruskal重构树的模板题。核心问题是求两点间所有路径中最大边权的最小值，这等价于在最小生成树上求两点间路径上的最大边权。
 * 
 * Kruskal重构树构建过程：
 * 1. 将所有边按边权从小到大排序
 * 2. 使用并查集维护连通性
 * 3. 遍历边，当边的两个端点不在同一连通分量时：
 *    a. 创建一个新节点，权值为当前边的边权
 *    b. 将两个连通分量的根节点作为新节点的左右子节点
 *    c. 更新并查集，将新节点作为新的根
 * 
 * 【数据结构设计】
 * - edge：存储所有边的信息（起点、终点、边权）
 * - father：并查集数组，用于维护连通性
 * - head/next/to：邻接表，存储重构树的结构
 * - nodeKey：存储重构树节点的权值
 * - dep/stjump：深度数组和倍增表，用于LCA查询
 * 
 * 【复杂度分析】
 * - 时间复杂度：
 *   - 边排序：O(m log m)
 *   - 构建重构树：O(m α(n))，其中α是阿克曼函数的反函数，近似常数
 *   - DFS预处理：O(n log n)，需要构建倍增表
 *   - 查询处理：O(q log n)，每次LCA查询是O(log n)
 *   - 总时间复杂度：O(m log m + n log n + q log n)
 * 
 * - 空间复杂度：
 *   - 边存储：O(m)
 *   - 并查集：O(n)
 *   - 重构树：O(n)（节点数最多2n-1）
 *   - 倍增表：O(n log n)（每个节点需要log n个祖先信息）
 *   - 总空间复杂度：O(n log n + m)
 * 
 * 【算法优化点】
 * 1. 使用路径压缩优化并查集查询效率
 * 2. 使用快速IO处理大规模数据输入输出
 * 3. 预分配足够空间避免动态扩容开销
 * 
 * 【与标准库实现对比】
 * Java中没有内置的Kruskal重构树实现，但可以利用Collections.sort进行边排序，
 * 并使用自定义的并查集和邻接表实现整个算法。
 * 
 * 【测试与调试建议】
 * 1. 边界测试：n=1，m=0，q=0等特殊情况
 * 2. 连通性测试：不连通的两点查询
 * 3. 性能测试：大规模数据下的运行时间
 * 4. 使用断言验证中间结果，如LCA的正确性
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class Code09_KruskalRebuildTemplate1 {

    public static int MAXN = 300001;
    public static int MAXM = 300001;
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
    public static void kruskalRebuild() {
        // 初始化并查集
        for (int i = 1; i <= n; i++) {
            father[i] = i;
        }
        
        // 按边权从小到大排序 - 这是构建最小生成树Kruskal重构树的关键
        Arrays.sort(edge, 1, m + 1, (a, b) -> a[2] - b[2]);
        
        cntu = n; // 初始节点数为原图节点数
        
        // 遍历所有边
        for (int i = 1, fx, fy; i <= m; i++) {
            // 查找两个端点所在集合的根
            fx = find(edge[i][0]);
            fy = find(edge[i][1]);
            
            // 如果不在同一连通分量
            if (fx != fy) {
                // 合并两个连通分量：创建新节点作为父节点
                cntu++; // 新节点编号从n+1开始
                father[fx] = cntu; // 左子节点连接
                father[fy] = cntu; // 右子节点连接
                father[cntu] = cntu; // 根节点指向自己
                
                // 新节点的权值为当前边的边权
                nodeKey[cntu] = edge[i][2];
                
                // 建立重构树的父子关系 - 无向树，但这里用有向表示父子
                addEdge(cntu, fx); // 父节点到左子节点
                addEdge(cntu, fy); // 父节点到右子节点
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
            // 遍历邻接表中的所有子节点并递归处理
            dfs(to[e], u);
        }
    }

    /**
     * 倍增法查询LCA（最近公共祖先）
     * 
     * 【功能说明】
     * 查找两个节点的最近公共祖先，用于后续获取路径最大边权最小值
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
            // 如果提升2^p级后仍不低于b的深度，则提升
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
     * 3. 预处理LCA所需的深度数组和倍增表
     * 4. 处理每个查询，输出结果
     * 
     * 【异常处理】
     * - 处理节点不连通的情况，输出-1
     * 
     * 【性能优化】
     * - 使用快速IO处理大规模数据
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 初始化快速IO
        FastIO io = new FastIO(System.in, System.out);
        
        // 读取输入数据
        n = io.nextInt();  // 节点数
        m = io.nextInt();  // 边数
        q = io.nextInt();  // 查询数
        
        // 读取所有边的信息
        for (int i = 1; i <= m; i++) {
            edge[i][0] = io.nextInt();  // 起点
            edge[i][1] = io.nextInt();  // 终点
            edge[i][2] = io.nextInt();  // 边权
        }
        
        // 构建Kruskal重构树 - 核心算法
        kruskalRebuild();
        
        // 对每个连通分量进行DFS预处理，构建LCA所需的倍增表
        // 注意：重构树可能由多个树组成（原图不连通时）
        for (int i = 1; i <= cntu; i++) {
            // 找到每个树的根节点（父节点等于自身）
            if (i == father[i]) {
                dfs(i, 0);  // 根节点的父节点设为0
            }
        }
        
        // 处理每个查询
        for (int i = 1, x, y; i <= q; i++) {
            x = io.nextInt();  // 查询节点x
            y = io.nextInt();  // 查询节点y
            
            // 异常处理：检查两个节点是否连通
            if (find(x) != find(y)) {
                io.writelnInt(-1);  // 不连通时输出-1
            } else {
                // 核心结论：原图中两点间路径最大边权的最小值等于重构树上LCA的点权
                int ancestor = lca(x, y);
                io.writelnInt(nodeKey[ancestor]);
            }
        }
        
        // 刷新输出流，确保所有数据都被写入
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