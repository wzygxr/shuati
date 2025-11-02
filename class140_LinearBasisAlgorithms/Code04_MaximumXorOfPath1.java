package class137;

import java.io.*;
import java.util.*;

/**
 * 路径最大异或和(递归版)
 * 
 * 题目描述：
 * 一共有n个点，编号1~n，由m条无向边连接
 * 每条边有权值，输入保证图是连通的，可能有环
 * 找到1到n的一条路径，路径可以重复经过某些点或边
 * 当一条边在路径中出现了多次时，异或的时候也要算多次
 * 希望找到一条从1到n的路径，所有边权异或和尽量大，返回这个最大异或和
 * 
 * 算法思路：
 * 1. 图中任意一条从1到n的路径都可以表示为一条固定路径加上若干个环的异或和
 * 2. 使用DFS找到图中所有环的异或和，构建线性基
 * 3. 贪心地选择线性基中的元素来最大化从1到n的路径异或和
 * 
 * 时间复杂度：O((n + m) * BIT)
 * 空间复杂度：O(n + BIT)
 * 
 * 优化点：
 * - Java递归版本可能因为递归层数太多而爆栈，C++版本可以正常通过
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P4151
 * 提交时请把类名改成"Main"
 */
public class Code04_MaximumXorOfPath1 {

    public static int MAXN = 50001;

    public static int MAXM = 200002;

    public static int BIT = 60;

    // 链式前向星 - 图的邻接表表示
    public static int[] head = new int[MAXN];
    public static int[] next = new int[MAXM];
    public static int[] to = new int[MAXM];
    public static long[] weight = new long[MAXM];
    public static int cnt;

    // 所有环的异或和构建的线性基
    public static long[] basis = new long[BIT + 1];

    // 某个节点在dfs过程中是否被访问过
    public static boolean[] visited = new boolean[MAXN];

    // 从头结点到当前节点的异或和
    public static long[] path = new long[MAXN];

    /**
     * 初始化函数
     * @param n 节点个数
     */
    public static void prepare(int n) {
        cnt = 1;
        // 初始化链式前向星的头指针数组
        Arrays.fill(head, 1, n + 1, 0);
        // 清空线性基数组
        Arrays.fill(basis, 0);
    }

    /**
     * 向图中添加边
     * @param u 起点
     * @param v 终点
     * @param w 边权
     */
    public static void addEdge(int u, int v, long w) {
        // 使用链式前向星添加边
        next[cnt] = head[u];
        to[cnt] = v;
        weight[cnt] = w;
        head[u] = cnt++;
    }

    /**
     * 将数字插入线性基
     * 
     * 算法思路：
     * 1. 从高位到低位扫描
     * 2. 如果当前位为1且线性基中该位为空，则直接插入
     * 3. 否则用线性基中该位的数异或当前数，继续处理
     * 
     * @param num 要插入的数字
     * @return 如果成功插入返回true，否则返回false
     */
    public static boolean insert(long num) {
        for (int i = BIT; i >= 0; i--)
            if (num >> i == 1) {
                if (basis[i] == 0) {
                    basis[i] = num;
                    return true;
                }
                num ^= basis[i];
            }
        return false;
    }

    /**
     * 深度优先搜索，找到图中所有环的异或和并构建线性基
     * 
     * 算法思路：
     * 1. 从节点1开始DFS遍历图
     * 2. 对于每个节点，记录从起点到该节点的路径异或和
     * 3. 如果访问到已访问过的节点，说明找到了一个环
     * 4. 计算环的异或和并插入线性基
     * 
     * @param u 当前节点
     * @param f 父节点
     * @param p 从起点到当前节点的路径异或和
     */
    public static void dfs(int u, int f, long p) {
        // 记录从起点到当前节点的路径异或和
        path[u] = p;
        // 标记当前节点已访问
        visited[u] = true;
        
        // 遍历当前节点的所有邻接节点
        for (int e = head[u]; e != 0; e = next[e]) {
            int v = to[e];
            // 不回到父节点
            if (v != f) {
                // 计算到邻接节点的路径异或和
                long xor = p ^ weight[e];
                
                // 如果邻接节点已访问，说明找到了一个环
                if (visited[v]) {
                    // 计算环的异或和并插入线性基
                    // 环的异或和 = 从起点到u的异或和 ^ 从起点到v的异或和 ^ 边权
                    insert(xor ^ path[v]);
                } else {
                    // 递归访问未访问过的邻接节点
                    dfs(v, u, xor);
                }
            }
        }
    }

    /**
     * 查询最大异或和
     * 
     * 算法思路：
     * 1. 从线性基中贪心地选择元素来最大化异或和
     * 2. 对于每一位，如果异或后结果更大，则选择异或
     * 
     * @param init 初始异或和(从1到n的任意一条路径的异或和)
     * @return 最大异或和
     */
    public static long query(long init) {
        // 从高位到低位贪心选择
        for (int i = BIT; i >= 0; i--) {
            // 如果异或后结果更大，则选择异或
            init = Math.max(init, init ^ basis[i]);
        }
        return init;
    }

    /**
     * 主函数
     * 读取输入数据，DFS找环构建线性基，计算并输出最大异或和
     */
    public static void main(String[] args) {
        Kattio io = new Kattio();
        int n = io.nextInt();  // 节点数
        int m = io.nextInt();  // 边数
        prepare(n);
        
        // 读取边信息，构建图
        for (int i = 1; i <= m; i++) {
            int u = io.nextInt();
            int v = io.nextInt();
            long w = io.nextLong();
            addEdge(u, v, w);
            addEdge(v, u, w);  // 无向边，需要添加两次
        }
        
        // 深度优先搜索，找环构建线性基
        dfs(1, 0, 0);
        
        // 计算并输出从1到n的最大异或和
        io.println(query(path[n]));
        io.flush();
        io.close();
    }

    /**
     * Kattio类IO效率很好，但还是不如StreamTokenizer
     * 只有StreamTokenizer无法正确处理时，才考虑使用这个类
     * 参考链接 : https://oi-wiki.org/lang/java-pro/
     */
    public static class Kattio extends PrintWriter {
        private BufferedReader r;
        private StringTokenizer st;

        public Kattio() {
            this(System.in, System.out);
        }

        public Kattio(InputStream i, OutputStream o) {
            super(o);
            r = new BufferedReader(new InputStreamReader(i));
        }

        public Kattio(String intput, String output) throws IOException {
            super(output);
            r = new BufferedReader(new FileReader(intput));
        }

        public String next() {
            try {
                while (st == null || !st.hasMoreTokens())
                    st = new StringTokenizer(r.readLine());
                return st.nextToken();
            } catch (Exception e) {
            }
            return null;
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public double nextDouble() {
            return Double.parseDouble(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

}