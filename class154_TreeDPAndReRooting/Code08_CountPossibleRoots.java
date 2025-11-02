package class123;

// 统计可能的树根数目
// 题目来源：LeetCode 2581. Count Number of Possible Root Nodes
// 题目链接：https://leetcode.cn/problems/count-number-of-possible-root-nodes/
// 测试链接 : https://leetcode.cn/problems/count-number-of-possible-root-nodes/
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

/*
题目解析：
这是一道典型的换根DP问题。我们需要统计有多少个节点可以作为根，使得至少有k个猜测是正确的。

算法思路：
1. 第一次DFS：以节点0为根，计算每个节点子树内的正确猜测数
   - 对于每条边u-v，如果猜测中存在(u,v)，则表示u是v的父节点，这是一个正确猜测
   - 统计以0为根时的正确猜测数

2. 第二次DFS：换根DP，计算每个节点作为根时的正确猜测数
   - 当从节点u换根到节点v时：
     * 原来u是v的父节点，现在v是u的父节点
     * 如果猜测中存在(u,v)，则换根后这个猜测就不再正确
     * 如果猜测中存在(v,u)，则换根后这个猜测就变为正确
     * 因此：dp[v] = dp[u] - (u,v存在?) + (v,u存在?)

时间复杂度：O(n) - 两次DFS遍历
空间复杂度：O(n) - 存储图和DP数组
是否为最优解：是，换根DP是解决此类问题的最优方法

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code08_CountPossibleRoots.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code08_CountPossibleRoots.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code08_CountPossibleRoots.cpp
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Code08_CountPossibleRoots {

    public static int MAXN = 100001;

    public static int n;

    public static int[] head = new int[MAXN];

    public static int[] next = new int[MAXN << 1];

    public static int[] to = new int[MAXN << 1];

    public static int cnt;

    // 存储所有猜测，用于快速查找
    public static Set<Long> guesses = new HashSet<>();

    // dp[i]: 以节点i为根时的正确猜测数
    public static int[] dp = new int[MAXN];

    public static void build() {
        cnt = 1;
        Arrays.fill(head, 1, n + 1, 0);
        guesses.clear();
        Arrays.fill(dp, 1, n + 1, 0);
    }

    public static void addEdge(int u, int v) {
        next[cnt] = head[u];
        to[cnt] = v;
        head[u] = cnt++;
    }

    // 将两个节点编号编码为一个长整数，用于存储在HashSet中
    public static long encode(int u, int v) {
        return (long) u * MAXN + v;
    }

    // 第一次DFS：计算以节点0为根时的正确猜测数
    public static void dfs1(int u, int f) {
        for (int e = head[u], v; e != 0; e = next[e]) {
            v = to[e];
            if (v != f) {
                dfs1(v, u);
                // 如果猜测中存在(u,v)，则这是一个正确的猜测
                if (guesses.contains(encode(u, v))) {
                    dp[0]++;
                }
            }
        }
    }

    // 第二次DFS：换根DP，计算每个节点作为根时的正确猜测数
    public static void dfs2(int u, int f) {
        for (int e = head[u], v; e != 0; e = next[e]) {
            v = to[e];
            if (v != f) {
                // 换根公式：
                // 原来u是v的父节点，现在v是u的父节点
                // 如果猜测中存在(u,v)，则换根后这个猜测就不再正确 (-1)
                // 如果猜测中存在(v,u)，则换根后这个猜测就变为正确 (+1)
                dp[v] = dp[u];
                if (guesses.contains(encode(u, v))) {
                    dp[v]--;
                }
                if (guesses.contains(encode(v, u))) {
                    dp[v]++;
                }
                dfs2(v, u);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        in.nextToken();
        int testCase = (int) in.nval;
        for (int t = 1; t <= testCase; t++) {
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
            in.nextToken();
            int m = (int) in.nval;
            in.nextToken();
            int k = (int) in.nval;
            // 读取所有猜测
            for (int i = 1, u, v; i <= m; i++) {
                in.nextToken();
                u = (int) in.nval;
                in.nextToken();
                v = (int) in.nval;
                guesses.add(encode(u, v));
            }
            // 第一次DFS计算以节点0为根时的正确猜测数
            dfs1(0, -1);
            // 第二次DFS换根计算所有节点作为根时的正确猜测数
            dfs2(0, -1);
            // 统计满足条件的根节点数目
            int ans = 0;
            for (int i = 0; i < n; i++) {
                if (dp[i] >= k) {
                    ans++;
                }
            }
            out.println(ans);
        }
        out.flush();
        out.close();
        br.close();
    }

}