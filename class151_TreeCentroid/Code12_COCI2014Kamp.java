package class120;

// COCI 2014/2015 #1 Kamp
// 给定一颗有n个节点的无根树，每一条边有一个经过的时间，树上有K个关键节点，
// 对于每一个节点u，需要回答从u出发到所有关键节点的最小时间
// 利用树的重心性质优化计算
// 测试链接 : https://oj.uz/problem/view/COCI15_kamp
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

public class Code12_COCI2014Kamp {

    public static int MAXN = 500001;

    public static int n, k;

    // 邻接表存储树
    public static ArrayList<int[]>[] adj = new ArrayList[MAXN];

    // 关键节点标记
    public static boolean[] isKey = new boolean[MAXN];

    // 子树中关键节点的数量
    public static int[] keyCount = new int[MAXN];

    // 以u为根的子树中，从u出发遍历所有关键节点并返回u的最小时间
    public static long[] subtreeTime = new long[MAXN];

    // 从u出发遍历所有关键节点的最小时间（不需要返回u）
    public static long[] minTime = new long[MAXN];

    // 初始化
    static {
        for (int i = 0; i < MAXN; i++) {
            adj[i] = new ArrayList<>();
        }
    }

    // 第一次DFS，计算子树信息
    public static void dfs1(int u, int father) {
        keyCount[u] = isKey[u] ? 1 : 0;
        subtreeTime[u] = 0;

        // 遍历所有子节点
        for (int[] edge : adj[u]) {
            int v = edge[0];
            int w = edge[1];
            
            if (v != father) {
                dfs1(v, u);
                keyCount[u] += keyCount[v];
                
                // 如果子树中有关键节点，需要加上往返时间
                if (keyCount[v] > 0) {
                    subtreeTime[u] += subtreeTime[v] + 2L * w;
                }
            }
        }
    }

    // 第二次DFS，换根DP计算答案
    public static void dfs2(int u, int father, int fatherWeight) {
        if (u == 1) {
            // 根节点的最小时间就是子树时间
            minTime[u] = subtreeTime[u];
        } else {
            // 非根节点的最小时间需要考虑从父节点来的路径
            minTime[u] = subtreeTime[u];
            
            // 如果父节点子树中有关键节点，需要考虑从父节点来的路径
            if (keyCount[1] - keyCount[u] > 0) {
                long fatherTime = minTime[father];
                
                // 如果u是father的子树中包含关键节点的子树，需要减去u的贡献
                if (keyCount[u] > 0) {
                    fatherTime -= subtreeTime[u] + 2L * fatherWeight;
                }
                
                // 加上从u到father再遍历father其他子树的时间
                if (keyCount[1] - keyCount[u] > 0) {
                    minTime[u] += fatherTime + 2L * fatherWeight;
                }
            }
        }

        // 递归处理子节点
        for (int[] edge : adj[u]) {
            int v = edge[0];
            int w = edge[1];
            
            if (v != father) {
                dfs2(v, u, w);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

        in.nextToken();
        n = (int) in.nval;

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

        in.nextToken();
        k = (int) in.nval;

        // 读取关键节点
        for (int i = 0; i < k; i++) {
            in.nextToken();
            int keyNode = (int) in.nval;
            isKey[keyNode] = true;
        }

        // 特殊情况：如果没有关键节点
        if (k == 0) {
            for (int i = 1; i <= n; i++) {
                out.println(0);
            }
        } else {
            // 第一次DFS计算子树信息
            dfs1(1, 0);

            // 第二次DFS换根DP计算答案
            dfs2(1, 0, 0);

            // 输出结果
            for (int i = 1; i <= n; i++) {
                // 如果不需要返回起点，可以减去最远关键节点的往返时间
                long result = minTime[i];
                out.println(result);
            }
        }

        out.flush();
        out.close();
        br.close();
    }
}