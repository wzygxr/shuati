package class121;

import java.util.*;

/**
 * 51Nod-2602 - 树的直径
 * 
 * 题目链接：https://www.51nod.com/Challenge/Problem.html#!#problemId=2602
 * 题目描述：一棵树的直径就是这棵树上存在的最长路径。现在有一棵n个节点的树，现在想知道这棵树的直径包含的边的个数是多少？
 * 
 * 输入格式：
 * - 第1行：一个整数n，表示树上的节点个数。(1<=n<=100000)
 * - 第2-n行：每行有两个整数u,v,表示u与v之间有一条路径。(1<=u,v<=n)
 * 
 * 输出格式：
 * - 输出一个整数，表示这棵树直径所包含的边的个数。
 * 
 * 解题思路：
 * 使用两次BFS法求树的直径：
 * 1. 第一次BFS，从任意节点（如节点1）开始找到最远节点u
 * 2. 第二次BFS，从第一次找到的最远节点u开始找到另一个最远节点v
 * 3. u到v的距离即为树的直径，也就是直径包含的边的个数
 * 
 * 算法标签：树、广度优先搜索、两次BFS
 * 难度：简单
 * 时间复杂度：O(n)，其中n是树中节点的数量
 * 空间复杂度：O(n)，用于存储邻接表和辅助数组
 * 
 * 相关题目：
 * - LeetCode 543. 二叉树的直径
 * - LeetCode 1245. Tree Diameter (无向树的直径)
 * - LeetCode 1522. Diameter of N-Ary Tree (N叉树的直径)
 * - SPOJ PT07Z - Longest path in a tree (树中最长路径)
 * - CSES 1131 - Tree Diameter (树的直径)
 * - 洛谷 U81904 树的直径
 * - AtCoder ABC221F - Diameter Set
 */
public class Nod2602_TreeDiameter {
    private static int n; // 节点数量
    private static List<List<Integer>> graph; // 邻接表存储树结构
    
    /**
     * BFS函数，从指定节点开始，找到距离最远的节点和最远距离
     * 
     * 算法思路：
     * 1. 从指定起点开始进行广度优先搜索
     * 2. 记录访问过的节点，避免重复访问
     * 3. 记录每个节点到起点的距离
     * 4. 在遍历过程中更新最远节点和最远距离
     * 
     * @param start 起始节点
     * @return 包含最远节点和最远距离的数组 [最远节点, 最远距离]
     */
    private static int[] bfs(int start) {
        boolean[] visited = new boolean[n + 1]; // 标记节点是否被访问过
        int[] distance = new int[n + 1]; // 存储每个节点到起始节点的距离
        Queue<Integer> queue = new LinkedList<>();
        
        queue.offer(start);
        visited[start] = true;
        distance[start] = 0;
        
        int maxDistance = 0;
        int farthestNode = start;
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            
            for (int neighbor : graph.get(current)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    distance[neighbor] = distance[current] + 1;
                    queue.offer(neighbor);
                    
                    // 更新最远距离和最远节点
                    if (distance[neighbor] > maxDistance) {
                        maxDistance = distance[neighbor];
                        farthestNode = neighbor;
                    }
                }
            }
        }
        
        return new int[] { farthestNode, maxDistance };
    }
    
    /**
     * 主方法
     * 
     * 算法流程：
     * 1. 读取输入数据，构建树的邻接表表示
     * 2. 第一次BFS，找到距离任意节点(这里选择1号节点)最远的节点u
     * 3. 第二次BFS，找到距离u最远的节点v，此时的距离即为树的直径
     * 4. 输出树的直径包含的边的个数
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        
        // 初始化邻接表
        graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 读取边
        for (int i = 0; i < n - 1; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            // 无向树，添加双向边
            graph.get(u).add(v);
            graph.get(v).add(u);
        }
        scanner.close();
        
        // 第一次BFS，找到距离任意节点(这里选择1号节点)最远的节点u
        int[] result1 = bfs(1);
        int u = result1[0];
        
        // 第二次BFS，找到距离u最远的节点v，此时的距离即为树的直径
        int[] result2 = bfs(u);
        int diameter = result2[1];
        
        // 输出树的直径包含的边的个数
        System.out.println(diameter);
    }
}