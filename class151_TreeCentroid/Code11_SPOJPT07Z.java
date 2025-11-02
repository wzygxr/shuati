package class120;

// SPOJ PT07Z Longest path in a tree (树中的最长路径)
// 题目来源: SPOJ PT07Z https://www.spoj.com/problems/PT07Z/
// 问题描述: 求树的直径，即树中任意两点之间最长的简单路径
// 算法思路:
// 1. 树的直径可以通过两次BFS或DFS求解
// 2. 第一次从任意节点（如节点1）开始BFS，找到距离它最远的节点
// 3. 第二次从第一步找到的最远节点开始BFS，找到距离它最远的节点
// 4. 第二次BFS中找到的最远距离就是树的直径
// 与重心的关系: 树的直径与重心密切相关，直径的中点（可能是一个节点或一条边的中点）通常与重心有关
// 时间复杂度：O(n)，需要两次BFS遍历
// 空间复杂度：O(n)，用于存储树结构和BFS队列
// 提交说明: 提交时请把类名改成"Main"，可以直接通过

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Code11_SPOJPT07Z {

    // 最大节点数，根据题目限制设置
    public static int MAXN = 10001;

    // 节点数量
    public static int n;

    // 邻接表存储树结构，adj[i]表示与节点i相邻的所有节点列表
    public static ArrayList<Integer>[] adj = new ArrayList[MAXN];

    // dist[i]表示从起始节点到节点i的距离
    public static int[] dist = new int[MAXN];

    // 静态初始化块，在类加载时执行一次
    static {
        // 初始化邻接表，为每个节点创建一个空的ArrayList
        for (int i = 0; i < MAXN; i++) {
            adj[i] = new ArrayList<>();
        }
    }

    // BFS求最远节点
    // start: BFS的起始节点
    // 返回值: 距离起始节点最远的节点
    public static int bfs(int start) {
        // 初始化距离数组，-1表示未访问
        Arrays.fill(dist, -1);
        
        // 创建BFS队列
        Queue<Integer> queue = new LinkedList<>();
        // 将起始节点加入队列
        queue.offer(start);
        // 起始节点的距离为0
        dist[start] = 0;
        
        // 记录最远节点和最大距离
        int farthestNode = start;
        int maxDist = 0;
        
        // BFS遍历
        while (!queue.isEmpty()) {
            // 取出队首节点
            int u = queue.poll();
            
            // 更新最远节点和最大距离
            if (dist[u] > maxDist) {
                maxDist = dist[u];
                farthestNode = u;
            }
            
            // 遍历节点u的所有邻接节点
            for (int v : adj[u]) {
                // 如果节点v未被访问过
                if (dist[v] == -1) {
                    // 设置节点v的距离为节点u的距离加1
                    dist[v] = dist[u] + 1;
                    // 将节点v加入队列
                    queue.offer(v);
                }
            }
        }
        
        // 返回距离起始节点最远的节点
        return farthestNode;
    }

    // 计算树的直径
    // 树的直径定义：树中任意两点之间最长的简单路径
    public static int treeDiameter() {
        // 第一次BFS，从节点1开始找到距离它最远的节点
        int farthestNode = bfs(1);
        
        // 第二次BFS，从第一次找到的最远节点开始BFS，找到真正的最远节点
        // 根据树的性质，这样找到的距离就是树的直径
        int diameterNode = bfs(farthestNode);
        
        // 返回直径（最远节点的距离）
        return dist[diameterNode];
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

        // 计算树的直径
        int diameter = treeDiameter();

        // 输出树的直径
        out.println(diameter);

        // 刷新输出缓冲区并关闭资源
        out.flush();
        out.close();
        br.close();
    }
}