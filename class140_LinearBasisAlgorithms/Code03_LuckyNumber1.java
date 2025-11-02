package class137;

import java.io.*;
import java.util.*;

/**
 * 幸运数字(递归版)
 * 
 * 题目描述：
 * 一共有n个点，编号1~n，由n-1条边连成一棵树，每个点上有数字
 * 一共有q条查询，每次返回a到b的路径上，可以随意选择数字，能得到的最大异或和
 * 
 * 算法思路：
 * 1. 使用树上倍增算法预处理每个节点到根节点路径上的线性基
 * 2. 对于每次查询，找到两个节点的最近公共祖先(LCA)
 * 3. 合并两个节点到LCA路径上的线性基，计算最大异或和
 * 
 * 时间复杂度：
 * - 预处理：O(n * log n * BIT)
 * - 查询：O(log n * BIT)
 * 
 * 空间复杂度：O(n * log n * BIT)
 * 
 * 优化点：
 * - 使用特殊的线性基插入方法，记录每个基向量来自的深度
 * - Java递归版本可能因为递归层数太多而爆栈，C++版本可以正常通过
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P3292
 * 提交时请把类名改成"Main"
 */
public class Code03_LuckyNumber1 {

    // 节点个数上限
    public static int MAXN = 20002;

    // 树上倍增的次方上限
    public static int LIMIT = 16;

    // 节点值最大的位数
    public static int BIT = 60;

    // 每个节点值的数组
    public static long[] arr = new long[MAXN];

    // 链式前向星 - 图的邻接表表示
    public static int[] head = new int[MAXN];
    public static int[] next = new int[MAXN << 1];
    public static int[] to = new int[MAXN << 1];
    public static int cnt;

    // 树上倍增需要的深度表
    public static int[] deep = new int[MAXN];

    // 树上倍增需要的倍增表，stjump[i][j]表示节点i向上跳2^j步到达的节点
    public static int[][] stjump = new int[MAXN][LIMIT];

    // 树上倍增根据实际节点个数确定的次方
    public static int power;

    // bases[i][j]表示：
    // 头节点到i节点路径上的数字，建立异或空间线性基，其中j位的线性基是哪个数字
    public static long[][] bases = new long[MAXN][BIT + 1];

    // levels[i][j]表示：
    // 头节点到i节点路径上的数字，建立异或空间线性基，其中j位的线性基来自哪一层(深度)
    public static int[][] levels = new int[MAXN][BIT + 1];

    /**
     * 初始化函数
     * @param n 节点个数
     */
    public static void prepare(int n) {
        cnt = 1;
        // 初始化链式前向星的头指针数组
        Arrays.fill(head, 1, n + 1, 0);
        // 计算树上倍增的最大次方
        power = log2(n);
    }

    /**
     * 计算log2(n)的值
     * @param n 输入数值
     * @return log2(n)的整数部分
     */
    public static int log2(int n) {
        int ans = 0;
        // 找到满足 2^ans <= n/2 的最大ans值
        while ((1 << ans) <= (n >> 1)) {
            ans++;
        }
        return ans;
    }

    /**
     * 向图中添加边
     * @param u 起点
     * @param v 终点
     */
    public static void addEdge(int u, int v) {
        // 使用链式前向星添加边
        next[cnt] = head[u];
        to[cnt] = v;
        head[u] = cnt++;
    }

    /**
     * 深度优先搜索，预处理树上倍增和线性基信息
     * @param u 当前节点
     * @param f 父节点
     */
    public static void dfs(int u, int f) {
        // 计算当前节点的深度
        deep[u] = deep[f] + 1;
        // 初始化倍增表的第一列(向上跳1步)
        stjump[u][0] = f;
        
        // 填充倍增表的其他列
        for (int p = 1; p <= power; p++) {
            stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
        }
        
        // 从父节点继承线性基信息
        for (int i = 0; i <= BIT; i++) {
            bases[u][i] = bases[f][i];
            levels[u][i] = levels[f][i];
        }
        
        // 将当前节点的值插入线性基
        insertReplace(arr[u], deep[u], bases[u], levels[u]);
        
        // 递归处理子节点
        for (int e = head[u]; e != 0; e = next[e]) {
            if (to[e] != f) {
                dfs(to[e], u);
            }
        }
    }

    /**
     * 插入和替换线性基，本题最重要的函数
     * 
     * 算法思路：
     * 1. 从高位到低位扫描当前值
     * 2. 如果当前位为1且线性基中该位为空，则直接插入
     * 3. 如果线性基中该位已有元素，比较深度，深度大的保留在线性基中
     * 4. 用线性基中该位的数异或当前数，继续处理
     * 
     * @param curv 当前要插入的值
     * @param curl 当前值所在的深度
     * @param basis 线性基数组
     * @param level 线性基元素对应的深度数组
     * @return 如果成功插入返回true，否则返回false
     */
    public static boolean insertReplace(long curv, int curl, long[] basis, int[] level) {
        for (int i = BIT; i >= 0; i--) {
            if (curv >> i == 1) {
                if (basis[i] == 0) {
                    // 线性基中该位为空，直接插入
                    basis[i] = curv;
                    level[i] = curl;
                    return true;
                }
                // 比较深度，深度大的保留在线性基中
                if (curl > level[i]) {
                    long tmp1 = curv;
                    curv = basis[i];
                    basis[i] = tmp1;
                    int tmp2 = level[i];
                    level[i] = curl;
                    curl = tmp2;
                }
                // 用线性基中该位的数异或当前数，继续处理
                curv ^= basis[i];
            }
        }
        return false;
    }

    /**
     * 计算两个节点的最近公共祖先(LCA)
     * 
     * 算法思路：
     * 1. 先将深度较大的节点向上跳到相同深度
     * 2. 同时向上跳，直到两个节点相遇
     * 
     * @param x 第一个节点
     * @param y 第二个节点
     * @return 两个节点的最近公共祖先
     */
    public static int lca(int x, int y) {
        // 确保x是深度较大的节点
        if (deep[x] < deep[y]) {
            int tmp = x;
            x = y;
            y = tmp;
        }
        
        // 将x向上跳到与y相同深度
        for (int p = power; p >= 0; p--) {
            if (deep[stjump[x][p]] >= deep[y]) {
                x = stjump[x][p];
            }
        }
        
        // 如果x和y已经相同，说明y就是LCA
        if (x == y) {
            return x;
        }
        
        // 同时向上跳，直到两个节点相遇
        for (int p = power; p >= 0; p--) {
            if (stjump[x][p] != stjump[y][p]) {
                x = stjump[x][p];
                y = stjump[y][p];
            }
        }
        
        // 返回LCA(父节点)
        return stjump[x][0];
    }

    // 临时线性基数组，用于查询时合并线性基
    public static long[] basis = new long[BIT + 1];

    /**
     * 查询两个节点路径上数字能异或出的最大值
     * 
     * 算法思路：
     * 1. 找到两个节点的LCA
     * 2. 合并两个节点到LCA路径上的线性基
     * 3. 贪心地选择线性基中的元素来最大化异或和
     * 
     * @param x 第一个节点
     * @param y 第二个节点
     * @return 路径上数字能异或出的最大值
     */
    public static long query(int x, int y) {
        // 找到LCA
        int lca = lca(x, y);
        
        // 获取两个节点的线性基和深度信息
        long[] basisx = bases[x];
        int[] levelx = levels[x];
        long[] basisy = bases[y];
        int[] levely = levels[y];
        
        // 清空临时线性基
        Arrays.fill(basis, 0);
        
        // 将x到LCA路径上的线性基合并到临时线性基中
        for (int i = BIT; i >= 0; i--) {
            long num = basisx[i];
            // 只有深度大于等于LCA深度的元素才在路径上
            if (levelx[i] >= deep[lca] && num != 0) {
                basis[i] = num;
            }
        }
        
        // 将y到LCA路径上的线性基合并到临时线性基中
        for (int i = BIT; i >= 0; i--) {
            long num = basisy[i];
            // 只有深度大于等于LCA深度的元素才在路径上
            if (levely[i] >= deep[lca] && num != 0) {
                // 插入到临时线性基中
                for (int j = i; j >= 0; j--) {
                    if (num >> j == 1) {
                        if (basis[j] == 0) {
                            basis[j] = num;
                            break;
                        }
                        num ^= basis[j];
                    }
                }
            }
        }
        
        // 贪心地选择元素来最大化异或和
        long ans = 0;
        for (int i = BIT; i >= 0; i--) {
            ans = Math.max(ans, ans ^ basis[i]);
        }
        
        return ans;
    }

    /**
     * 主函数
     * 读取输入数据，预处理树上信息，处理查询，输出结果
     */
    public static void main(String[] args) throws IOException {
        Kattio io = new Kattio();
        int n = io.nextInt();  // 节点数
        int q = io.nextInt();  // 查询数
        prepare(n);
        
        // 读取每个节点的值
        for (int i = 1; i <= n; i++) {
            arr[i] = io.nextLong();
        }
        
        // 读取边信息，构建树
        for (int i = 1, u, v; i < n; i++) {
            u = io.nextInt();
            v = io.nextInt();
            addEdge(u, v);
            addEdge(v, u);  // 无向边，需要添加两次
        }
        
        // 深度优先搜索，预处理树上倍增和线性基信息
        dfs(1, 0);
        
        // 处理查询
        for (int i = 1, x, y; i <= q; i++) {
            x = io.nextInt();
            y = io.nextInt();
            io.println(query(x, y));
        }
        
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