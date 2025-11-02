package class175;

// 给你一棵树问题 - 分块算法优化动态规划 (Java版本)
// 题目来源: https://www.luogu.com.cn/problem/CF1039D
// 题目来源: https://codeforces.com/problemset/problem/1039/D
// 题目大意: 一共有n个节点，给定n-1条边，所有节点连成一棵树
// 树的路径是指，从端点x到端点y的简单路径，k路径是指，路径的节点数正好为k
// 整棵树希望分解成尽量多的k路径，k路径的节点不能复用，所有k路径不要求包含所有点
// 打印k = 1, 2, 3..n时，k路径有最多有几条
// 约束条件: 1 <= n <= 200000

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code07_GivenTree1 {

    // 定义最大数组长度
    public static int MAXN = 200001;
    
    // n: 节点数量, blen: 块大小
    public static int n, blen;
    
    // 邻接表存储树的结构
    // head[i]: 节点i的邻接表头节点
    public static int[] head = new int[MAXN];
    
    // next[i]: 邻接表中第i个节点的下一个节点
    public static int[] next = new int[MAXN << 1];
    
    // to[i]: 邻接表中第i个节点存储的相邻节点
    public static int[] to = new int[MAXN << 1];
    
    // cntg: 邻接表节点计数器
    public static int cntg = 0;

    // fa[i]: 节点i的父节点编号
    public static int[] fa = new int[MAXN];
    
    // dfnOrder: 根据dfn序，依次收集上来的节点编号
    public static int[] dfnOrder = new int[MAXN];
    
    // cntd: dfn序计数器
    public static int cntd = 0;

    // len[i]: 当前i号节点只能往下走，没分配成路径的最长链的长度
    public static int[] len = new int[MAXN];
    
    // max1[i]: 最大值 { len[a], len[b], len[c] ... }，其中a、b、c..是i的子节点
    public static int[] max1 = new int[MAXN];
    
    // max2[i]: 次大值 { len[a], len[b], len[c] ... }，其中a、b、c..是i的子节点
    public static int[] max2 = new int[MAXN];

    // ans: 存储每个k对应的答案
    public static int[] ans = new int[MAXN];

    /**
     * 添加边到邻接表
     * @param u 节点u
     * @param v 节点v
     */
    public static void addEdge(int u, int v) {
        // 添加u->v的边
        next[++cntg] = head[u];
        to[cntg] = v;
        head[u] = cntg;
        
        // 添加v->u的边
        next[++cntg] = head[v];
        to[cntg] = u;
        head[v] = cntg;
    }

    /**
     * DFS遍历树，生成dfn序
     * @param u 当前节点
     * @param f 父节点
     */
    public static void dfs(int u, int f) {
        // 记录父节点
        fa[u] = f;
        
        // 记录dfn序
        dfnOrder[++cntd] = u;
        
        // 遍历所有子节点
        for (int e = head[u]; e > 0; e = next[e]) {
            // 避免回到父节点
            if (to[e] != f) {
                dfs(to[e], u);
            }
        }
    }

    /**
     * 查询当路径长度为k时，最多能分解成几条路径
     * @param k 路径长度
     * @return 最多路径数
     */
    public static int query(int k) {
        int cnt = 0;
        
        // 按照dfn序的逆序处理节点
        for (int i = n, cur, father; i >= 1; i--) {
            cur = dfnOrder[i];
            father = fa[cur];
            
            // 如果当前节点的最长链和次长链之和+1 >= k
            // 说明可以形成一条长度为k的路径
            if (max1[cur] + max2[cur] + 1 >= k) {
                cnt++; // 路径数+1
                len[cur] = 0; // 当前节点的最长链长度重置为0
            } else {
                // 否则更新当前节点的最长链长度
                len[cur] = max1[cur] + 1;
            }
            
            // 更新父节点的最长链和次长链
            if (len[cur] > max1[father]) {
                max2[father] = max1[father];
                max1[father] = len[cur];
            } else if (len[cur] > max2[father]) {
                max2[father] = len[cur];
            }
        }
        
        // 重置数组
        for (int i = 1; i <= n; i++) {
            len[i] = max1[i] = max2[i] = 0;
        }
        return cnt;
    }

    /**
     * 跳跃函数，用于优化计算
     * @param l 左边界
     * @param r 右边界
     * @param curAns 当前答案
     * @return 下一个需要计算的位置
     */
    public static int jump(int l, int r, int curAns) {
        int find = l;
        while (l <= r) {
            int mid = (l + r) >> 1;
            int check = query(mid);
            
            if (check < curAns) {
                r = mid - 1;
            } else if (check > curAns) {
                l = mid + 1;
            } else {
                find = mid;
                l = mid + 1;
            }
        }
        return find + 1;
    }

    /**
     * 计算所有答案
     */
    public static void compute() {
        // 对于k <= sqrt(n)的情况，直接计算
        for (int i = 1; i <= blen; i++) {
            ans[i] = query(i);
        }
        
        // 对于k > sqrt(n)的情况，使用跳跃优化
        for (int i = blen + 1; i <= n; i = jump(i, n, ans[i])) {
            ans[i] = query(i);
        }
    }

    /**
     * 预处理函数
     */
    public static void prepare() {
        // 计算块大小，选择sqrt(n * log2(n))以优化性能
        int log2n = 0;
        while ((1 << log2n) <= (n >> 1)) {
            log2n++;
        }
        blen = Math.max(1, (int) Math.sqrt(n * log2n));
        
        // 初始化答案数组为-1，表示未计算
        Arrays.fill(ans, 1, n + 1, -1);
    }

    public static void main(String[] args) throws Exception {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(System.out);
        
        // 读取节点数量n
        n = in.nextInt();
        
        // 读取n-1条边
        for (int i = 1, u, v; i < n; i++) {
            u = in.nextInt();
            v = in.nextInt();
            addEdge(u, v);
        }
        
        // DFS生成dfn序
        dfs(1, 0);
        
        // 进行预处理
        prepare();
        
        // 计算所有答案
        compute();
        
        // 输出所有答案
        for (int i = 1; i <= n; i++) {
            // 如果答案未计算，则继承前一个答案
            if (ans[i] == -1) {
                ans[i] = ans[i - 1];
            }
            out.println(ans[i]);
        }
        
        out.flush();
        out.close();
    }

    // 高效读取工具类，用于加快输入输出速度
    static class FastReader {
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;
        private final InputStream in;

        FastReader(InputStream in) {
            this.in = in;
        }

        private int readByte() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++];
        }

        int nextInt() throws IOException {
            int c;
            do {
                c = readByte();
            } while (c <= ' ' && c != -1);
            boolean neg = false;
            if (c == '-') {
                neg = true;
                c = readByte();
            }
            int val = 0;
            while (c > ' ' && c != -1) {
                val = val * 10 + (c - '0');
                c = readByte();
            }
            return neg ? -val : val;
        }
    }

}