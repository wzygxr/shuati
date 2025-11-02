package class121;

// CSES 1131 Tree Diameter
// 题目：给定一棵树，求树的直径（树中任意两点间最长的简单路径）
// 来源：CSES Problem Set - Tree Algorithms
// 链接：https://cses.fi/problemset/task/1131
// 提交时请把类名改成"Main"

// 算法标签：树、广度优先搜索、两次BFS
// 难度：简单
// 时间复杂度：O(n)，其中n是树中节点的数量
// 空间复杂度：O(n)，用于存储邻接表和辅助数组

// 相关题目：
// - LeetCode 543. 二叉树的直径
// - LeetCode 1245. Tree Diameter (无向树的直径)
// - LeetCode 1522. Diameter of N-Ary Tree (N叉树的直径)
// - SPOJ PT07Z - Longest path in a tree (树中最长路径)
// - 51Nod 2602 - 树的直径
// - 洛谷 U81904 树的直径
// - AtCoder ABC221F - Diameter Set

// 解题思路：
// 使用两次BFS法求解树的直径：
// 1. 从任意一点开始，找到距离它最远的点s
// 2. 从s开始，找到距离它最远的点t
// 3. s到t的距离即为树的直径

import java.io.*;
import java.util.*;

public class CSES1131_TreeDiameter {
    
    static final int MAXN = 200001;
    
    // 邻接表存储树
    static ArrayList<Integer>[] graph;
    static int n;  // 节点数
    
    // BFS方法求从起点开始的最远节点和距离
    static class Pair {
        int node;
        int distance;
        
        Pair(int node, int distance) {
            this.node = node;
            this.distance = distance;
        }
    }
    
    /**
     * BFS求从起点开始的最远节点
     * 
     * 算法思路：
     * 1. 从指定起点开始进行广度优先搜索
     * 2. 记录访问过的节点，避免重复访问
     * 3. 记录每一层的节点，直到遍历完所有节点
     * 4. 返回最后一层的节点（最远节点）和距离
     * 
     * @param start 起点
     * @return Pair对象，包含最远节点和距离
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    static Pair bfs(int start) {
        boolean[] visited = new boolean[n + 1];
        Queue<Integer> queue = new LinkedList<>();
        
        visited[start] = true;
        queue.offer(start);
        
        int lastNode = start;
        int maxDistance = 0;
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int current = queue.poll();
                lastNode = current;
                
                // 遍历当前节点的所有邻居
                for (int neighbor : graph[current]) {
                    if (!visited[neighbor]) {
                        visited[neighbor] = true;
                        queue.offer(neighbor);
                    }
                }
            }
            if (!queue.isEmpty()) {
                maxDistance++;
            }
        }
        
        return new Pair(lastNode, maxDistance);
    }
    
    /**
     * 使用两次BFS法求树的直径
     * 
     * 算法思路：
     * 1. 第一次BFS，从任意节点（如节点1）开始找到最远节点
     * 2. 第二次BFS，从第一次找到的最远节点开始找到另一个最远节点
     * 3. 第二次BFS的距离就是树的直径
     * 
     * @return 树的直径
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    static int findDiameter() {
        // 第一次BFS，从节点1开始找到最远节点
        Pair firstBFS = bfs(1);
        
        // 第二次BFS，从第一次找到的最远节点开始找到另一个最远节点
        Pair secondBFS = bfs(firstBFS.node);
        
        // 第二次BFS的距离就是树的直径
        return secondBFS.distance;
    }
    
    /**
     * 主方法
     * 
     * 输入格式：
     * - 第一行包含一个整数n，表示树中节点的数量
     * - 接下来n-1行，每行包含两个整数u和v，表示节点u和v之间有一条边
     * 
     * 输出格式：
     * - 输出一个整数，表示树的直径
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取节点数
        n = Integer.parseInt(br.readLine());
        
        // 初始化邻接表
        graph = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        // 读取边信息
        for (int i = 1; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            graph[u].add(v);
            graph[v].add(u);
        }
        
        // 计算并输出树的直径
        out.println(findDiameter());
        out.flush();
        out.close();
        br.close();
    }
}