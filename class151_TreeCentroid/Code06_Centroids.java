package class120;

// Centroids (重心)
// 题目来源: Codeforces 708C https://codeforces.com/contest/708/problem/C
// 问题描述: 给定一棵树，对于每个点，我们删掉任意一条边，再连上任意一条边，
// 求这样操作后可以使这个点为重心的点数
// 树的重心定义：删除这个点后最大连通块的结点数最小
// 算法思路:
// 1. 对于每个节点，首先计算其作为重心时的最大连通块大小
// 2. 如果最大连通块大小不超过n/2，则该节点本身就是重心
// 3. 否则，检查是否可以通过调整一条边使其成为重心
//    调整策略：将最大的子树移动到其他位置，使得调整后最大连通块大小不超过n/2
// 时间复杂度：O(n)，需要两次DFS遍历
// 空间复杂度：O(n)，用于存储树结构和递归栈
// 提交说明: 提交时请把类名改成"Main"，可以直接通过

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Arrays;

public class Code06_Centroids {

    // 最大节点数，根据题目限制设置
    public static int MAXN = 400001;

    // 节点数量
    public static int n;

    // 邻接表存储树结构，adj[i]表示与节点i相邻的所有节点列表
    public static ArrayList<Integer>[] adj = new ArrayList[MAXN];

    // size[i]表示以节点i为根的子树的节点数量
    public static int[] size = new int[MAXN];

    // maxSub[i]表示以节点i为根时的最大子树大小
    public static int[] maxSub = new int[MAXN];

    // secondMaxSub[i]表示以节点i为根时的次大子树大小
    public static int[] secondMaxSub = new int[MAXN];

    // upSub[i]表示节点i向上（父节点方向）的子树大小，即整棵树去掉以i为根的子树后剩余的部分
    public static int[] upSub = new int[MAXN];

    // 答案数组，ans[i]=1表示节点i可以通过调整一条边成为重心，ans[i]=0表示不可以
    public static int[] ans = new int[MAXN];

    // 静态初始化块，在类加载时执行一次
    static {
        // 初始化邻接表，为每个节点创建一个空的ArrayList
        for (int i = 0; i < MAXN; i++) {
            adj[i] = new ArrayList<>();
        }
    }

    // 第一次DFS，计算每个节点的子树大小、最大子树大小和次大子树大小
    // u: 当前访问的节点
    // father: u的父节点，避免回到父节点形成环
    public static void dfs1(int u, int father) {
        // 初始化当前节点u的子树大小为1（包含节点u本身）
        size[u] = 1;
        // 初始化当前节点u的最大子树大小为0
        maxSub[u] = 0;
        // 初始化当前节点u的次大子树大小为0
        secondMaxSub[u] = 0;

        // 遍历所有与节点u相邻的节点
        for (int v : adj[u]) {
            // 如果不是父节点，则继续DFS
            if (v != father) {
                // 递归访问子节点v，父节点为u
                dfs1(v, u);
                
                // 将子节点v的子树大小加到当前节点u的子树大小中
                size[u] += size[v];

                // 更新最大和次大子树大小
                if (size[v] > maxSub[u]) {
                    // 如果当前子树大小大于原最大子树大小
                    // 原最大子树大小变为次大子树大小
                    secondMaxSub[u] = maxSub[u];
                    // 当前子树大小变为最大子树大小
                    maxSub[u] = size[v];
                } else if (size[v] > secondMaxSub[u]) {
                    // 如果当前子树大小大于原次大子树大小但不大于最大子树大小
                    // 当前子树大小变为次大子树大小
                    secondMaxSub[u] = size[v];
                }
            }
        }
    }

    // 第二次DFS，计算向上子树的大小并判断每个节点是否可以成为重心
    // u: 当前访问的节点
    // father: u的父节点，避免回到父节点形成环
    public static void dfs2(int u, int father) {
        // 计算向上子树的大小，即整棵树去掉以u为根的子树后剩余的部分
        upSub[u] = n - size[u];

        // 判断当前节点是否可以成为重心
        // 当前节点作为根时的最大连通块大小
        int maxComponent = Math.max(upSub[u], maxSub[u]);
        
        if (maxComponent <= n / 2) {
            // 如果最大连通块大小不超过总节点数的一半，则当前节点本身就是重心
            ans[u] = 1;
        } else {
            // 否则，需要通过调整边使当前节点成为重心
            // 调整策略：将最大的子树移动到其他位置
            
            // 标记是否可以通过调整使当前节点成为重心
            boolean canMakeCentroid = false;

            // 检查向上子树（父节点方向的子树）
            if (upSub[u] <= n / 2) {
                // 如果向上子树大小不超过n/2，则可以通过调整使当前节点成为重心
                canMakeCentroid = true;
            }

            // 检查各个子树
            for (int v : adj[u]) {
                if (v != father) {
                    // 如果v是最大子树
                    if (size[v] == maxSub[u]) {
                        // 使用次大子树进行调整
                        // 调整后的最大连通块大小为n - maxSub[u]（即去掉最大子树后剩余的部分）
                        if (n - maxSub[u] <= n / 2) {
                            canMakeCentroid = true;
                            break;
                        }
                    } else {
                        // 使用最大子树进行调整
                        // 调整后的最大连通块大小为n - size[v]（即去掉子树v后剩余的部分）
                        if (n - size[v] <= n / 2) {
                            canMakeCentroid = true;
                            break;
                        }
                    }
                }
            }

            if (canMakeCentroid) {
                // 如果可以通过调整使当前节点成为重心，则标记为1
                ans[u] = 1;
            }
        }

        // 递归处理子节点
        for (int v : adj[u]) {
            if (v != father) {
                dfs2(v, u);
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
        // 树有n-1条边
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

        // 第二次DFS计算答案
        // 从节点1开始DFS，父节点为0（表示没有父节点）
        dfs2(1, 0);

        // 输出结果
        // 对于每个节点，输出1表示可以通过调整边使其成为重心，0表示不可以
        for (int i = 1; i <= n; i++) {
            out.print(ans[i] + " ");
        }
        out.println();

        // 刷新输出缓冲区并关闭资源
        out.flush();
        out.close();
        br.close();
    }
}