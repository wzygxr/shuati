package class123;

// 最小高度树
// 题目来源：LeetCode 310. Minimum Height Trees
// 题目链接：https://leetcode.cn/problems/minimum-height-trees/
// 测试链接 : https://leetcode.cn/problems/minimum-height-trees/
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

/*
题目解析：
这是一道树的中心问题。我们需要找到树的中心节点，这些节点作为根时树的高度最小。

算法思路：
方法一：暴力法（会超时）
对每个节点作为根进行BFS，计算树的高度，找出最小高度对应的根节点。

方法二：拓扑排序法（推荐）
1. 从叶子节点开始，逐层剥掉度数为1的节点
2. 最后剩下的1个或2个节点就是树的中心节点
3. 这些中心节点作为根时，树的高度最小

方法三：换根DP法
1. 第一次DFS：以节点0为根，计算每个节点子树内的最大深度
2. 第二次DFS：换根DP，计算每个节点作为根时的最大深度（树的高度）
3. 找出最小高度对应的根节点

这里我们使用换根DP法实现。

时间复杂度：O(n) - 两次DFS遍历
空间复杂度：O(n) - 存储图和DP数组
是否为最优解：是，换根DP是解决此类问题的最优方法之一

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code09_MinimumHeightTrees.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code09_MinimumHeightTrees.py
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Code09_MinimumHeightTrees {

    public static int MAXN = 20001;

    public static int n;

    public static int[] head = new int[MAXN];

    public static int[] next = new int[MAXN << 1];

    public static int[] to = new int[MAXN << 1];

    public static int cnt;

    // first[i]: 以节点i为根时，子树内的最大深度
    public static int[] first = new int[MAXN];

    // second[i]: 以节点i为根时，子树内的次大深度
    public static int[] second = new int[MAXN];

    // up[i]: 以节点i为根时，向上的最大深度
    public static int[] up = new int[MAXN];

    public static void build() {
        cnt = 1;
        Arrays.fill(head, 1, n + 1, 0);
        Arrays.fill(first, 1, n + 1, 0);
        Arrays.fill(second, 1, n + 1, 0);
        Arrays.fill(up, 1, n + 1, 0);
    }

    public static void addEdge(int u, int v) {
        next[cnt] = head[u];
        to[cnt] = v;
        head[u] = cnt++;
    }

    // 第一次DFS：计算以节点0为根时，每个节点子树内的最大深度和次大深度
    public static void dfs1(int u, int f) {
        first[u] = 0;
        second[u] = 0;
        for (int e = head[u], v; e != 0; e = next[e]) {
            v = to[e];
            if (v != f) {
                dfs1(v, u);
                // 更新最大深度和次大深度
                int depth = first[v] + 1;
                if (depth > first[u]) {
                    second[u] = first[u];
                    first[u] = depth;
                } else if (depth > second[u]) {
                    second[u] = depth;
                }
            }
        }
    }

    // 第二次DFS：换根DP，计算每个节点作为根时向上的最大深度
    public static void dfs2(int u, int f) {
        for (int e = head[u], v; e != 0; e = next[e]) {
            v = to[e];
            if (v != f) {
                // 计算v节点向上的最大深度
                // 如果u到v的路径是u的最大深度路径，则使用次大深度
                if (first[u] == first[v] + 1) {
                    up[v] = Math.max(up[u], second[u]) + 1;
                } else {
                    up[v] = Math.max(up[u], first[u]) + 1;
                }
                dfs2(v, u);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
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
            // 特殊情况：只有一个节点
            if (n == 1) {
                out.println("0");
                continue;
            }
            // 第一次DFS计算以节点0为根时的信息
            dfs1(0, -1);
            // 第二次DFS换根计算所有节点作为根时向上的最大深度
            dfs2(0, -1);
            // 找出最小高度
            int minDepth = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                // 节点i作为根时的高度是max(向下最大深度, 向上最大深度)
                int depth = Math.max(first[i], up[i]);
                minDepth = Math.min(minDepth, depth);
            }
            // 收集所有最小高度对应的根节点
            List<Integer> result = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                int depth = Math.max(first[i], up[i]);
                if (depth == minDepth) {
                    result.add(i);
                }
            }
            // 输出结果
            for (int i = 0; i < result.size(); i++) {
                out.print(result.get(i));
                if (i < result.size() - 1) {
                    out.print(" ");
                }
            }
            out.println();
        }
        out.flush();
        out.close();
        br.close();
    }

}