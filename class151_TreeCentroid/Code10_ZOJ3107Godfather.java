package class120;

// ZOJ 3107 Godfather
// 找到树的所有重心
// 树的重心定义：删除这个点后，剩余各个连通块中点数的最大值不超过总节点数的一半
// 测试链接 : https://zoj.pintia.cn/problem-sets/91827364500/problems/91827367606
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

public class Code10_ZOJ3107Godfather {

    public static int MAXN = 50001;

    public static int n;

    // 邻接表存储树
    public static ArrayList<Integer>[] adj = new ArrayList[MAXN];

    // 子树大小
    public static int[] size = new int[MAXN];

    // 每个节点的最大子树大小
    public static int[] maxSub = new int[MAXN];

    // 重心列表
    public static ArrayList<Integer> centroids = new ArrayList<>();

    // 初始化
    static {
        for (int i = 0; i < MAXN; i++) {
            adj[i] = new ArrayList<>();
        }
    }

    // 第一次DFS，计算每个节点的子树大小和最大子树大小
    public static void dfs1(int u, int father) {
        size[u] = 1;
        maxSub[u] = 0;

        // 遍历所有子节点
        for (int v : adj[u]) {
            if (v != father) {
                dfs1(v, u);
                size[u] += size[v];
                maxSub[u] = Math.max(maxSub[u], size[v]);
            }
        }
        
        // 计算父节点方向的子树大小
        maxSub[u] = Math.max(maxSub[u], n - size[u]);
    }

    // 找到所有重心
    public static void findCentroids() {
        int minMaxSub = n; // 初始化为最大值
        
        // 找到最小的最大子树大小
        for (int i = 1; i <= n; i++) {
            minMaxSub = Math.min(minMaxSub, maxSub[i]);
        }
        
        // 收集所有具有最小最大子树大小的节点
        for (int i = 1; i <= n; i++) {
            if (maxSub[i] == minMaxSub) {
                centroids.add(i);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            n = (int) in.nval;

            // 初始化
            for (int i = 1; i <= n; i++) {
                adj[i].clear();
            }

            // 读取边信息并构建树
            for (int i = 1; i < n; i++) {
                in.nextToken();
                int u = (int) in.nval;
                in.nextToken();
                int v = (int) in.nval;
                adj[u].add(v);
                adj[v].add(u);
            }

            // 第一次DFS计算子树信息
            dfs1(1, 0);

            // 找到所有重心
            centroids.clear();
            findCentroids();

            // 输出结果
            boolean first = true;
            for (int centroid : centroids) {
                if (!first) {
                    out.print(" ");
                }
                out.print(centroid);
                first = false;
            }
            out.println();
        }

        out.flush();
        out.close();
        br.close();
    }
}