package class181;

// Dominant Indices - CF1009F
// 测试链接 : https://codeforces.com/contest/1009/problem/F
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * Dominant Indices问题
 * 
 * 题目来源: Codeforces 1009F
 * 题目链接: https://codeforces.com/contest/1009/problem/F
 * 
 * 题目描述:
 * 给定一棵 n 个节点的树，根节点为1。对于每个节点 u，定义其深度数组为一个无限序列，
 * 其中第 d 项表示 u 的子树中深度为 d 的节点数量。求每个节点的深度数组中最大值的下标。
 * 如果有多个最大值，输出最小的下标。
 * 
 * 解题思路:
 * 1. 使用线段树合并技术解决树上统计问题
 * 2. 为每个节点建立一棵深度线段树，维护子树中各深度的节点数量
 * 3. 从叶子节点开始，自底向上合并子树的线段树
 * 4. 查询当前节点线段树中节点数量最多的深度
 * 
 * 算法复杂度:
 * - 时间复杂度: O(n log n)
 * - 空间复杂度: O(n log n)
 * 
 * 线段树合并核心思想:
 * 1. 对于两棵线段树的对应节点，如果只有一棵树有该节点，则直接使用该节点
 * 2. 如果两棵树都有该节点，则递归合并左右子树，并更新当前节点信息
 * 3. 合并过程类似于可并堆的合并方式
 */
public class Code06_DominantIndices {

    public static int MAXN = 1000001;
    public static int MAXT = MAXN * 2; // 注意空间，因为每个节点最多log个节点

    public static int n;

    // 邻接表存储树
    public static int[] head = new int[MAXN];
    public static int[] nxt = new int[MAXN << 1];
    public static int[] to = new int[MAXN << 1];
    public static int cntg;

    // 线段树相关数组
    public static int[] ls = new int[MAXT];     // 左子节点
    public static int[] rs = new int[MAXT];     // 右子节点
    public static int[] maxDep = new int[MAXT]; // 最大深度
    public static int[] maxCnt = new int[MAXT]; // 最大深度的节点数
    public static int cntt;

    // 答案数组
    public static int[] ans = new int[MAXN];

    /**
     * 添加边到邻接表
     * @param u 起点
     * @param v 终点
     */
    public static void addEdge(int u, int v) {
        nxt[++cntg] = head[u];
        to[cntg] = v;
        head[u] = cntg;
    }

    /**
     * 更新节点信息
     * @param i 节点索引
     */
    public static void up(int i) {
        // 左子树信息更深一层，所以要比较右子树和左子树+1
        if (maxCnt[rs[i]] > maxCnt[ls[i]]) {
            maxCnt[i] = maxCnt[rs[i]];
            maxDep[i] = maxDep[rs[i]];
        } else {
            maxCnt[i] = maxCnt[ls[i]];
            maxDep[i] = maxDep[ls[i]] + 1;
        }
    }

    /**
     * 创建新节点
     * @return 新节点索引
     */
    public static int newNode() {
        ++cntt;
        ls[cntt] = rs[cntt] = maxDep[cntt] = maxCnt[cntt] = 0;
        return cntt;
    }

    /**
     * 在深度d处增加计数
     * @param d 深度
     * @param l 区间左端点
     * @param r 区间右端点
     * @param i 当前节点索引
     * @return 更新后的节点索引
     */
    public static int add(int d, int l, int r, int i) {
        int rt = i;
        if (rt == 0) {
            rt = newNode(); // 动态开点
        }
        if (l == r) {
            maxCnt[rt]++;   // 叶子节点计数加1
            maxDep[rt] = l; // 更新最大深度
        } else {
            int mid = (l + r) >> 1;
            if (d <= mid) {
                ls[rt] = add(d, l, mid, ls[rt]); // 递归更新左子树
            } else {
                rs[rt] = add(d, mid + 1, r, rs[rt]); // 递归更新右子树
            }
            up(rt); // 更新当前节点信息
        }
        return rt;
    }

    /**
     * 合并两棵线段树
     * @param l 区间左端点
     * @param r 区间右端点
     * @param t1 第一棵线段树根节点
     * @param t2 第二棵线段树根节点
     * @return 合并后的线段树根节点
     */
    public static int merge(int l, int r, int t1, int t2) {
        // 边界条件：如果其中一个节点为空，返回另一个节点
        if (t1 == 0 || t2 == 0) {
            return t1 + t2;
        }
        // 叶子节点：合并节点信息
        if (l == r) {
            maxCnt[t1] += maxCnt[t2]; // 累加计数
            maxDep[t1] = l;           // 更新最大深度
        } else {
            // 递归合并左右子树
            int mid = (l + r) >> 1;
            ls[t1] = merge(l, mid, ls[t1], ls[t2]);
            rs[t1] = merge(mid + 1, r, rs[t1], rs[t2]);
            up(t1); // 更新当前节点信息
        }
        return t1;
    }

    // DFS遍历树并计算答案
    public static int[] root = new int[MAXN];

    /**
     * DFS遍历树并计算答案
     * @param u 当前节点
     * @param fa 父节点
     */
    public static void dfs(int u, int fa) {
        // 先递归处理所有子节点
        for (int e = head[u]; e > 0; e = nxt[e]) {
            int v = to[e];
            if (v != fa) {
                dfs(v, u);
            }
        }

        // 将所有子节点的线段树合并到当前节点
        root[u] = newNode();      // 创建当前节点的线段树根节点
        maxCnt[root[u]] = 1;      // 当前节点自身贡献1个深度为0的节点
        maxDep[root[u]] = 0;      // 当前节点深度为0
        
        for (int e = head[u]; e > 0; e = nxt[e]) {
            int v = to[e];
            if (v != fa) {
                root[u] = merge(0, n, root[u], root[v]); // 合并子节点线段树
            }
        }
        
        // 当前节点的答案就是最大深度
        ans[u] = maxDep[root[u]];
    }

    public static void main(String[] args) throws Exception {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = in.nextInt();
        for (int i = 1, u, v; i < n; i++) {
            u = in.nextInt();
            v = in.nextInt();
            addEdge(u, v);
            addEdge(v, u);
        }
        
        dfs(1, 0);
        
        for (int i = 1; i <= n; i++) {
            out.println(ans[i]);
        }
        
        out.flush();
        out.close();
    }

    // 读写工具类
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