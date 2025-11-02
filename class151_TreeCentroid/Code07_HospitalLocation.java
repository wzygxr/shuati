package class120;

// 医院设置
// 题目来源: 洛谷 P1364 https://www.luogu.com.cn/problem/P1364
// 问题描述: 在一棵树上找一个点，使得该点到其他点距离之和最小
// 算法思路:
// 1. 利用树的重心的性质：树中所有点到某个点的距离和中，到重心的距离和最小
// 2. 使用换根DP（动态规划）技术计算每个节点作为医院时的总距离
// 3. 首先以节点1为根计算距离和，然后通过换根技术计算其他节点的距离和
// 时间复杂度：O(n)，需要三次DFS遍历
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

public class Code07_HospitalLocation {

    // 最大节点数，根据题目限制设置
    public static int MAXN = 101;

    // 节点数量
    public static int n;

    // 邻接表存储树结构，adj[i]表示与节点i相邻的所有节点列表
    public static ArrayList<Integer>[] adj = new ArrayList[MAXN];

    // people[i]表示节点i上的人数
    public static int[] people = new int[MAXN];

    // size[i]表示以节点i为根的子树的总人数
    public static int[] size = new int[MAXN];

    // distSum[i]表示以节点i为医院时，所有人的总距离
    public static long[] distSum = new long[MAXN];

    // 静态初始化块，在类加载时执行一次
    static {
        // 初始化邻接表，为每个节点创建一个空的ArrayList
        for (int i = 0; i < MAXN; i++) {
            adj[i] = new ArrayList<>();
        }
    }

    // 第一次DFS，计算每个节点的子树总人数
    // u: 当前访问的节点
    // father: u的父节点，避免回到父节点形成环
    public static void dfs1(int u, int father) {
        // 初始化当前节点u的子树总人数为该节点的人数
        size[u] = people[u];
        
        // 遍历所有与节点u相邻的节点
        for (int v : adj[u]) {
            // 如果不是父节点，则继续DFS
            if (v != father) {
                // 递归访问子节点v，父节点为u
                dfs1(v, u);
                
                // 将子节点v的子树总人数加到当前节点u的子树总人数中
                size[u] += size[v];
            }
        }
    }

    // 第二次DFS，计算以节点1为根时的距离和
    // u: 当前访问的节点
    // father: u的父节点，避免回到父节点形成环
    public static void dfs2(int u, int father) {
        // 计算从u到所有节点的距离和
        for (int v : adj[u]) {
            // 如果不是父节点，则继续DFS
            if (v != father) {
                // 递归访问子节点v，父节点为u
                dfs2(v, u);
                
                // 从v子树中的每个节点到u的距离比到v的距离多1
                // 因此总距离增加：distSum[v]（v子树内节点到v的距离和）+ size[v]（v子树的总人数，每个距离都增加1）
                distSum[u] += distSum[v] + size[v];
            }
        }
    }

    // 第三次DFS，换根DP计算所有节点的距离和
    // 换根DP的核心思想：当根从u换到v时，重新计算以v为根时的距离和
    // u: 当前访问的节点
    // father: u的父节点，避免回到父节点形成环
    public static void dfs3(int u, int father) {
        // 遍历所有与节点u相邻的节点
        for (int v : adj[u]) {
            // 如果不是父节点，则继续DFS
            if (v != father) {
                // 当根从u换到v时：
                // 1. v子树中的节点到v的距离比到u的距离少1，总共减少size[v]个距离单位
                // 2. 其他节点（整棵树去掉v子树）到v的距离比到u的距离多1，总共增加(size[1]-size[v])个距离单位
                // 因此：distSum[v] = distSum[u] + (size[1]-size[v]) - size[v] = distSum[u] + size[1] - 2*size[v]
                distSum[v] = distSum[u] + (size[1] - size[v]) - size[v];
                
                // 递归处理子节点v
                dfs3(v, u);
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

        // 读取每个节点的人数和邻接关系
        for (int i = 1; i <= n; i++) {
            // 读取节点i上的人数
            in.nextToken();
            people[i] = (int) in.nval;
            
            // 读取邻接关系（邻接矩阵形式）
            for (int j = 1; j <= n; j++) {
                in.nextToken();
                int connected = (int) in.nval;
                // 如果节点i和节点j相邻，则添加到邻接表中
                if (connected == 1) {
                    adj[i].add(j);
                }
            }
        }

        // 第一次DFS计算每个节点的子树总人数
        // 从节点1开始DFS，父节点为0（表示没有父节点）
        dfs1(1, 0);

        // 第二次DFS计算以节点1为根时的距离和
        // 从节点1开始DFS，父节点为0（表示没有父节点）
        dfs2(1, 0);

        // 第三次DFS换根DP计算所有节点的距离和
        // 从节点1开始DFS，父节点为0（表示没有父节点）
        dfs3(1, 0);

        // 找到距离和最小的节点
        long minDistSum = distSum[1];
        for (int i = 2; i <= n; i++) {
            minDistSum = Math.min(minDistSum, distSum[i]);
        }

        // 输出最小距离和
        out.println(minDistSum);

        // 刷新输出缓冲区并关闭资源
        out.flush();
        out.close();
        br.close();
    }
}