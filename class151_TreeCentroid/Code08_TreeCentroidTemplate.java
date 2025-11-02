package class120;

// 【模板】树的重心
// 题目来源: 洛谷 U328173 https://www.luogu.com.cn/problem/U328173
// 问题描述: 给定一棵无根树，求这棵树的重心（可能有多个）
// 树的重心定义: 计算以无根树每个点为根节点时的最大子树大小，这个值最小的点称为无根树的重心
// 算法思路:
// 1. 通过一次DFS计算每个节点作为根时的最大子树大小
// 2. 找到具有最小最大子树大小的所有节点，即为重心
// 时间复杂度：O(n)，只需要一次DFS遍历
// 空间复杂度：O(n)，用于存储树结构和递归栈

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Arrays;

public class Code08_TreeCentroidTemplate {

    // 最大节点数，根据题目限制设置
    public static int MAXN = 1000001;

    // 节点数量
    public static int n;

    // 邻接表存储树结构，adj[i]表示与节点i相邻的所有节点列表
    public static ArrayList<Integer>[] adj = new ArrayList[MAXN];

    // 子树大小数组，size[i]表示以节点i为根的子树的节点数量
    public static int[] size = new int[MAXN];

    // 每个节点的最大子树大小数组，maxSub[i]表示以节点i为根时的最大子树大小
    public static int[] maxSub = new int[MAXN];

    // 重心列表，存储所有重心节点
    public static ArrayList<Integer> centroids = new ArrayList<>();

    // 静态初始化块，在类加载时执行一次
    static {
        // 初始化邻接表，为每个节点创建一个空的ArrayList
        for (int i = 0; i < MAXN; i++) {
            adj[i] = new ArrayList<>();
        }
    }

    // 第一次DFS，计算每个节点的子树大小和最大子树大小
    // u: 当前访问的节点
    // father: u的父节点，避免回到父节点形成环
    public static void dfs1(int u, int father) {
        // 初始化当前节点u的子树大小为1（包含节点u本身）
        size[u] = 1;
        // 初始化当前节点u的最大子树大小为0
        maxSub[u] = 0;

        // 遍历所有与节点u相邻的节点
        for (int v : adj[u]) {
            // 如果不是父节点，则继续DFS
            if (v != father) {
                // 递归访问子节点v，父节点为u
                dfs1(v, u);
                
                // 将子节点v的子树大小加到当前节点u的子树大小中
                size[u] += size[v];
                
                // 更新以u为根时的最大子树大小
                maxSub[u] = Math.max(maxSub[u], size[v]);
            }
        }
        
        // 计算父节点方向的子树大小（即整棵树去掉以u为根的子树后剩余的部分）
        // 并更新最大子树大小
        maxSub[u] = Math.max(maxSub[u], n - size[u]);
    }

    // 找到所有重心
    public static void findCentroids() {
        // 初始化最小的最大子树大小为n（最大可能值）
        int minMaxSub = n;
        
        // 找到最小的最大子树大小
        // 遍历所有节点，找到最小的maxSub值
        for (int i = 1; i <= n; i++) {
            minMaxSub = Math.min(minMaxSub, maxSub[i]);
        }
        
        // 收集所有具有最小最大子树大小的节点
        // 这些节点就是树的重心
        for (int i = 1; i <= n; i++) {
            if (maxSub[i] == minMaxSub) {
                centroids.add(i);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // 使用BufferedReader提高输入效率
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        // 使用StreamTokenizer解析输入
        StreamTokenizer in = new StreamTokenizer(br);
        // 使用PrintWriter提高输出效率
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

        // 读取节点数量n
        in.nextToken();
        n = (int) in.nval;

        // 读取边信息并构建树
        // 无根树有n-1条边
        for (int i = 1; i < n; i++) {
            in.nextToken();
            int u = (int) in.nval;
            in.nextToken();
            int v = (int) in.nval;
            // 由于是无根树，添加无向边
            adj[u].add(v);
            adj[v].add(u);
        }

        // 第一次DFS计算子树信息
        // 从节点1开始DFS，父节点为0（表示没有父节点）
        dfs1(1, 0);

        // 找到所有重心
        findCentroids();

        // 输出结果
        boolean first = true;
        // 按照题目要求，输出重心节点编号（可能有多个）
        for (int centroid : centroids) {
            if (!first) {
                // 如果不是第一个重心，先输出空格分隔符
                out.print(" ");
            }
            out.print(centroid);
            first = false;
        }
        out.println();

        // 刷新输出缓冲区并关闭资源
        out.flush();
        out.close();
        br.close();
    }
}