package class164;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Kruskal重构树模版题 - Java实现
 * 
 * 题目来源：洛谷P2245 星际导航
 * 题目链接：https://www.luogu.com.cn/problem/P2245
 * 
 * 题目描述：
 * 图里有n个点，m条无向边，每条边给定边权，图里可能有若干个连通的部分
 * 一共有q条查询，每条查询都是如下的格式
 * 查询 x y : 点x和点y希望连通起来，其中的最大边权希望尽量小，打印这个值
 *            如果怎样都无法联通，打印"impossible"
 * 
 * 算法核心思想：
 * Kruskal重构树是一种将图论中的路径极值问题转化为树上LCA问题的数据结构。
 * 其关键性质是：原图中两点间所有路径的最大边权的最小值等于重构树上两点LCA的点权。
 * 
 * 解题思路：
 * 1. 将所有边按边权从小到大排序
 * 2. 使用Kruskal算法构建最小生成树，在构建过程中建立重构树
 * 3. 重构树中，原始节点是叶子节点，内部节点代表边，节点权值为边权
 * 4. 重构树满足大根堆性质：每个非叶子节点的权值大于等于其子节点的权值
 * 5. 对于查询x,y，如果两点不连通则输出"impossible"，否则输出它们LCA的节点权值
 * 
 * 时间复杂度分析：
 * - 构建Kruskal重构树：O(m log m) - 主要是排序的复杂度
 * - DFS预处理：O(n) - 毞个节点访问一次
 * - 每次查询：O(log n) - 倍增LCA的复杂度
 * 总复杂度：O(m log m + q log n)
 * 
 * 空间复杂度分析：
 * - 存储边：O(m)
 * - 存储图和重构树：O(n)
 * - 倍增表：O(n log n)
 * 总空间复杂度：O(n log n + m)
 * 
 * 工程化考量：
 * 1. 异常处理：处理节点不连通的情况，输出"impossible"
 * 2. 性能优化：使用快速IO和路径压缩并查集
 * 3. 内存管理：重构树节点数最大为2n-1，注意数组大小
 * 4. 边界处理：处理节点编号从1开始的情况
 * 5. 递归优化：Java中DFS递归可能爆栈，使用迭代版本
 */
public class Code01_KruskalRebuild1 {

    // 常量定义
    public static int MAXK = 200001;  // 最大节点数（重构树节点数最多为2n-1）
    public static int MAXM = 300001;  // 最大边数
    public static int MAXH = 20;      // 倍增表最大层数(2^20 > 1e5)
    public static int n, m, q;        // 节点数、边数、查询数

    // 每条边有三个信息，节点u、节点v、边权w
    public static int[][] edge = new int[MAXM][3];

    // 并查集 - 用于维护连通性
    public static int[] father = new int[MAXK];
    
    // Kruskal重构树的建图 - 邻接表存储
    public static int[] head = new int[MAXK];  // 邻接表头节点
    public static int[] next = new int[MAXK];  // 下一条边
    public static int[] to = new int[MAXK];    // 边的目标节点
    public static int cntg = 0;                // 边计数器
    
    // Kruskal重构树上，节点的权值（对应原图中的边权）
    public static int[] nodeKey = new int[MAXK];
    
    // Kruskal重构树上，点的数量（重构树节点总数）
    public static int cntu;

    // Kruskal重构树上，dfs过程建立的信息
    public static int[] dep = new int[MAXK];          // 节点深度
    public static int[][] stjump = new int[MAXK][MAXH]; // 倍增表，stjump[u][p]表示u的2^p级祖先

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
     * 实现细节：
     * 1. 初始化并查集，每个节点的父节点初始化为自身
     * 2. 按边权从小到大排序，这是构建最小生成树Kruskal重构树的关键
     * 3. 遍历排序后的边，使用并查集检查连通性
     * 4. 当发现不在同一连通分量的边时，创建新节点并构建重构树
     * 
     * 关键性质：
     * - 重构树的叶子节点是原图中的所有节点
     * - 重构树满足大根堆性质：每个非叶子节点的权值大于等于其子节点的权值
     * - 原图中两点间的最小瓶颈等于它们在重构树上的LCA节点权值
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
                father[fx] = father[fy] = ++cntu;
                father[cntu] = cntu;
                
                // 新节点的权值为当前边的边权
                nodeKey[cntu] = edge[i][2];
                
                // 建立重构树的父子关系
                addEdge(cntu, fx);
                addEdge(cntu, fy);
            }
        }
    }

    // dfs1是递归函数，需要改成迭代版，不然会爆栈，C++实现不需要
    public static void dfs1(int u, int fa) {
        dep[u] = dep[fa] + 1;
        stjump[u][0] = fa;
        for (int p = 1; p < MAXH; p++) {
            stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
        }
        for (int e = head[u]; e > 0; e = next[e]) {
            dfs1(to[e], u);
        }
    }

    public static int[][] ufe = new int[MAXK][3];

    public static int stacksize, u, f, e;

    public static void push(int u, int f, int e) {
        ufe[stacksize][0] = u;
        ufe[stacksize][1] = f;
        ufe[stacksize][2] = e;
        stacksize++;
    }

    public static void pop() {
        --stacksize;
        u = ufe[stacksize][0];
        f = ufe[stacksize][1];
        e = ufe[stacksize][2];
    }

    // dfs2是dfs1的迭代版
    public static void dfs2(int cur, int fa) {
        stacksize = 0;
        push(cur, fa, -1);
        while (stacksize > 0) {
            pop();
            if (e == -1) {
                dep[u] = dep[f] + 1;
                stjump[u][0] = f;
                for (int p = 1; p < MAXH; p++) {
                    stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
                }
                e = head[u];
            } else {
                e = next[e];
            }
            if (e != 0) {
                push(u, f, e);
                push(to[e], u, -1);
            }
        }
    }

    /**
     * 倍增法查询LCA（最近公共祖先）
     * 
     * 功能说明：
     * 查找两个节点的最近公共祖先，用于后续获取路径最大边权最小值
     * 
     * 实现步骤：
     * 1. 将较深的节点提升到较浅节点的深度
     * 2. 如果此时两节点相同，则为LCA
     * 3. 否则，同时提升两个节点直到它们的父节点相同
     * 4. 返回共同的父节点
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

    public static void main(String[] args) {
        FastIO io = new FastIO(System.in, System.out);
        n = io.nextInt();
        m = io.nextInt();
        for (int i = 1; i <= m; i++) {
            edge[i][0] = io.nextInt();
            edge[i][1] = io.nextInt();
            edge[i][2] = io.nextInt();
        }
        kruskalRebuild();
        for (int i = 1; i <= cntu; i++) {
            if (i == father[i]) {
                dfs2(i, 0);
            }
        }
        q = io.nextInt();
        for (int i = 1, x, y; i <= q; i++) {
            x = io.nextInt();
            y = io.nextInt();
            if (find(x) != find(y)) {
                io.write("impossible\n");
            } else {
                io.writelnInt(nodeKey[lca(x, y)]);
            }
        }
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