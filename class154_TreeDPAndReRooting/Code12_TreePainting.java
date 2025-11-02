package class123;

// Tree Painting
// 题目来源：Codeforces 1187E Tree Painting
// 题目链接：https://codeforces.com/problemset/problem/1187/E
// 测试链接 : https://codeforces.com/problemset/problem/1187/E
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

/*
题目解析：
这是一道树上换根DP问题。我们要选择一个节点作为第一个染色的点，使得染色过程中获得的总收益最大。
每次染色一个白点时，收益等于该点所在的白色连通块大小。

算法思路：
1. 第一次DFS：计算以节点1为根时，每个子树染色的收益
   - 对于节点u，先递归计算所有子树的收益
   - 然后计算u子树的总收益：dp[u] = sum(dp[v]) + size[u]
     * sum(dp[v])是所有子树的收益和
     * size[u]是因为染色u节点会使得子树中每个节点都获得1点收益

2. 第二次DFS：换根DP，计算每个节点作为起始点时的总收益
   - 当从节点u换根到节点v时：
     * 原来v子树外的所有节点都变成v子树内的节点
     * 原来v子树内的节点都变成v子树外的节点
     * 收益变化为：dp[v] = dp[u] + (n - size[v]) - size[v] = dp[u] + n - 2*size[v]

时间复杂度：O(n) - 两次DFS遍历
空间复杂度：O(n) - 存储图和DP数组
是否为最优解：是，换根DP是解决此类问题的最优方法

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code12_TreePainting.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code12_TreePainting.py
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code12_TreePainting {

    public static int MAXN = 200001;

    public static int n;

    public static int[] head = new int[MAXN];

    public static int[] next = new int[MAXN << 1];

    public static int[] to = new int[MAXN << 1];

    public static int cnt;

    public static int[] size = new int[MAXN];

    public static long[] dp = new long[MAXN];

    public static void build() {
        cnt = 1;
        Arrays.fill(head, 1, n + 1, 0);
        Arrays.fill(size, 1, n + 1, 0);
        Arrays.fill(dp, 1, n + 1, 0);
    }

    public static void addEdge(int u, int v) {
        next[cnt] = head[u];
        to[cnt] = v;
        head[u] = cnt++;
    }

    // dp[i]更新成
    // 节点i作为自己这棵子树最先染的点，染完子树后，收益是多少
    public static void dfs1(int u, int f) {
        size[u] = 1;
        for (int e = head[u], v; e != 0; e = next[e]) {
            v = to[e];
            if (v != f) {
                dfs1(v, u);
                size[u] += size[v];
                dp[u] += dp[v];
            }
        }
        // 染色节点u会使得子树中每个节点都获得1点收益
        dp[u] += size[u];
    }

    // dp[i]更新成
    // 节点i作为整棵树最先染的点，染完整棵树后，收益是多少
    public static void dfs2(int u, int f) {
        for (int e = head[u], v; e != 0; e = next[e]) {
            v = to[e];
            if (v != f) {
                // 换根公式：从u换根到v
                // v子树外的节点数为(n - size[v])，v子树内的节点数为size[v]
                // 收益变化为：(n - size[v]) - size[v] = n - 2*size[v]
                dp[v] = dp[u] + n - size[v] - size[v];
                dfs2(v, u);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        in.nextToken();
        n = (int) in.nval;
        build();
        for (int i = 1, u, v; i < n; i++) {
            in.nextToken();
            u = (int) in.nval;
            in.nextToken();
            v = (int) in.nval;
            addEdge(u, v);
            addEdge(v, u);
        }
        // 第一次DFS计算以节点1为根时的收益
        dfs1(1, 0);
        // 第二次DFS换根计算所有节点作为起始点时的收益
        dfs2(1, 0);
        // 找到最大收益
        long ans = 0;
        for (int i = 1; i <= n; i++) {
            ans = Math.max(ans, dp[i]);
        }
        out.println(ans);
        out.flush();
        out.close();
        br.close();
    }

}