package class120;

// Kay and Snowflake (雪花与凯)
// 题目来源: Codeforces 686D https://codeforces.com/contest/686/problem/D
// 问题描述: 给定一棵有根树，求出每一棵子树的重心是哪一个节点
// 树的重心定义：找到一个点，其所有的子树中最大的子树节点数最少
// 算法思路:
// 1. 首先通过DFS计算每个子树的大小
// 2. 对于每个节点，利用其最大子树的重心信息来快速找到当前子树的重心
// 3. 利用性质：子树的重心要么是最大子树的重心，要么在从最大子树重心到根节点的路径上
// 时间复杂度：O(n)，每个节点最多被访问常数次
// 空间复杂度：O(n)，用于存储树结构和递归栈

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Arrays;

public class Code05_KayAndSnowflake {

    // 最大节点数，根据题目限制设置
    public static int MAXN = 300001;

    // 节点数量和查询数量
    public static int n, q;

    // 邻接表存储树结构，adj[i]表示节点i的所有子节点列表
    public static ArrayList<Integer>[] adj = new ArrayList[MAXN];

    // 父节点数组，parent[i]表示节点i的父节点
    public static int[] parent = new int[MAXN];

    // 子树大小数组，size[i]表示以节点i为根的子树的节点数量
    public static int[] size = new int[MAXN];

    // 每个子树的重心数组，centroid[i]表示以节点i为根的子树的重心
    public static int[] centroid = new int[MAXN];

    // 静态初始化块，在类加载时执行一次
    static {
        // 初始化邻接表，为每个节点创建一个空的ArrayList
        for (int i = 0; i < MAXN; i++) {
            adj[i] = new ArrayList<>();
        }
    }

    // 计算每个子树的大小
    // 使用DFS递归计算以节点u为根的子树大小
    public static void computeSize(int u) {
        // 初始化当前节点u的子树大小为0
        size[u] = 0;
        
        // 递归计算每个子节点的子树大小
        for (int v : adj[u]) {
            computeSize(v);
            size[u] += size[v];
        }
        
        // 加上节点u本身
        size[u]++;
    }

    // 计算每个子树的重心
    // 利用已知的子树重心信息来快速计算当前子树的重心
    public static void computeCentroid(int u) {
        // 如果子树只有一个节点，重心就是它本身
        if (size[u] == 1) {
            centroid[u] = u;
            return;
        }

        // 找到最大的子树
        // 初始化最大子树为第一个子节点
        int largest = adj[u].get(0);
        
        // 遍历所有子节点，找到子树大小最大的子节点
        for (int v : adj[u]) {
            // 递归计算子节点v的重心
            computeCentroid(v);
            
            // 更新最大子树
            if (size[largest] < size[v]) {
                largest = v;
            }
        }

        // 子树大小的一半（向上取整）
        // 这是判断一个节点是否为重心的关键阈值
        int half = (size[u] + 1) / 2;
        
        // 从最大子树的重心开始向上查找
        // 利用性质：子树的重心要么是最大子树的重心，要么在从最大子树重心到根节点的路径上
        int cur = centroid[largest];
        
        // 沿着从最大子树重心到当前节点u的路径向上查找
        while (cur != u) {
            // 如果当前节点的子树大小小于half，说明它不可能是重心，需要继续向上查找
            if (size[cur] < half) {
                cur = parent[cur];
            } else {
                // 如果当前节点的子树大小大于等于half，且父节点方向的子树大小也小于half，则找到重心
                // 父节点方向的子树大小 = 整棵子树大小 - 当前节点子树大小
                if (size[u] - size[cur] < half) {
                    break;
                } else {
                    // 否则继续向上查找
                    cur = parent[cur];
                }
            }
        }
        
        // 记录当前子树的重心
        centroid[u] = cur;
    }

    public static void main(String[] args) throws IOException {
        // 使用BufferedReader提高输入效率
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        // 使用StreamTokenizer解析输入
        StreamTokenizer in = new StreamTokenizer(br);
        // 使用PrintWriter提高输出效率
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取节点数量n和查询数量q
        in.nextToken();
        n = (int) in.nval;
        in.nextToken();
        q = (int) in.nval;

        // 读取父节点信息并构建树
        for (int i = 2; i <= n; i++) {
            in.nextToken();
            parent[i] = (int) in.nval;
            // 添加边，将节点i添加到其父节点的子节点列表中
            adj[parent[i]].add(i);
        }

        // 根节点的父节点设为-1，表示没有父节点
        parent[1] = -1;

        // 计算每个子树的大小
        computeSize(1);
        
        // 计算每个子树的重心
        computeCentroid(1);

        // 处理查询
        for (int i = 0; i < q; i++) {
            in.nextToken();
            int u = (int) in.nval;
            // 输出以节点u为根的子树的重心
            out.println(centroid[u]);
        }

        // 刷新输出缓冲区并关闭资源
        out.flush();
        out.close();
        br.close();
    }
}