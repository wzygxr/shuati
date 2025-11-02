package class120;

// AtCoder ABC222 F - Expensive Expense
// 给定一棵树，边权为路费，点权为观光费。从u去v旅游的费用定义为路费加上v点的观光费
// 求从每个点出发到其它点旅游的最大费用
// 换根DP，与树的重心相关
// 测试链接 : https://atcoder.jp/contests/abc222/tasks/abc222_f
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过
// 时间复杂度：O(n)
// 空间复杂度：O(n)

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Arrays;

public class Code13_ABC222F {

    public static int MAXN = 200001;

    public static int n;

    // 邻接表存储树
    public static ArrayList<int[]>[] adj = new ArrayList[MAXN];

    // 点权（观光费）
    public static int[] D = new int[MAXN];

    // 以u为根的子树中，从u出发到子树节点的最大费用
    public static long[] maxDown = new long[MAXN];

    // 从u出发到所有节点的最大费用
    public static long[] maxCost = new long[MAXN];

    // 初始化
    static {
        for (int i = 0; i < MAXN; i++) {
            adj[i] = new ArrayList<>();
        }
    }

    // 第一次DFS，计算向下最大费用
    public static void dfs1(int u, int father) {
        maxDown[u] = D[u]; // 至少包含自己的观光费

        // 遍历所有子节点
        for (int[] edge : adj[u]) {
            int v = edge[0];
            int w = edge[1];
            
            if (v != father) {
                dfs1(v, u);
                // 更新从u出发向下的最大费用
                maxDown[u] = Math.max(maxDown[u], maxDown[v] + w);
            }
        }
    }

    // 第二次DFS，换根DP计算答案
    public static void dfs2(int u, int father, long fatherCost) {
        // 从u出发的最大费用是向下最大费用和从父节点来的最大费用的最大值
        maxCost[u] = Math.max(maxDown[u], fatherCost + D[u]);

        // 计算从u到各个子节点的最大费用
        // 找到最大值和次大值
        long max1 = -1, max2 = -1;
        int max1Child = -1;
        
        for (int[] edge : adj[u]) {
            int v = edge[0];
            int w = edge[1];
            
            if (v != father) {
                long cost = maxDown[v] + w;
                if (cost > max1) {
                    max2 = max1;
                    max1 = cost;
                    max1Child = v;
                } else if (cost > max2) {
                    max2 = cost;
                }
            }
        }

        // 递归处理子节点
        for (int[] edge : adj[u]) {
            int v = edge[0];
            int w = edge[1];
            
            if (v != father) {
                // 计算从v向上看的最大费用
                long upCost = fatherCost + w; // 从父节点来的费用
                
                // 如果v不是产生最大费用的子节点，可以加上最大费用
                // 否则加上次大费用
                if (v == max1Child) {
                    upCost = Math.max(upCost, max2 + w);
                } else {
                    upCost = Math.max(upCost, max1 + w);
                }
                
                // 加上v节点的观光费
                upCost += D[u];
                
                dfs2(v, u, upCost);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

        in.nextToken();
        n = (int) in.nval;

        // 读取点权（观光费）
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            D[i] = (int) in.nval;
        }

        // 读取边信息并构建树
        for (int i = 1; i < n; i++) {
            in.nextToken();
            int u = (int) in.nval;
            in.nextToken();
            int v = (int) in.nval;
            in.nextToken();
            int w = (int) in.nval;
            adj[u].add(new int[]{v, w});
            adj[v].add(new int[]{u, w});
        }

        // 第一次DFS计算向下最大费用
        dfs1(1, 0);

        // 第二次DFS换根DP计算答案
        dfs2(1, 0, 0);

        // 输出结果
        for (int i = 1; i <= n; i++) {
            out.println(maxCost[i]);
        }

        out.flush();
        out.close();
        br.close();
    }
}