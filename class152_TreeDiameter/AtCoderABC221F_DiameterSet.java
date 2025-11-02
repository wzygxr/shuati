package class121;

// AtCoder ABC221F Diameter Set
// 题目：给定一棵N个顶点的树，顶点编号为1到N。
// 选择两个或更多顶点并将其涂成红色的方法数是多少，
// 使得红色顶点之间的最大距离等于树的直径？
// 答案对998244353取模。
// 来源：AtCoder Beginner Contest 221 Problem F
// 链接：https://atcoder.jp/contests/abc221/tasks/abc221_f

// 算法标签：树、广度优先搜索、两次BFS、组合数学、快速幂
// 难度：困难
// 时间复杂度：O(n)，其中n是树中节点的数量
// 空间复杂度：O(n)，用于存储邻接表和辅助数组

// 相关题目：
// - LeetCode 543. 二叉树的直径
// - LeetCode 1245. Tree Diameter (无向树的直径)
// - LeetCode 1522. Diameter of N-Ary Tree (N叉树的直径)
// - SPOJ PT07Z - Longest path in a tree (树中最长路径)
// - CSES 1131 - Tree Diameter (树的直径)
// - 51Nod 2602 - 树的直径
// - 洛谷 U81904 树的直径

// 解题思路：
// 1. 首先计算树的直径
// 2. 根据直径的奇偶性分情况讨论
// 3. 对于偶数直径，有一个中心点；对于奇数直径，有一个中心边
// 4. 使用组合数学计算满足条件的方案数

import java.io.*;
import java.util.*;

public class AtCoderABC221F_DiameterSet {
    
    static final int MAXN = 200001;
    static final int MOD = 998244353;
    
    // 邻接表存储树
    static ArrayList<Integer>[] graph;
    static int n;  // 节点数
    
    // 树的直径相关变量
    static int diameter;  // 树的直径
    static int center1, center2;  // 直径的中心点
    
    // DFS计算子树大小和深度
    static int[] subtreeSize;
    static int[] depth;
    
    /**
     * 快速幂运算
     * 
     * 算法思路：
     * 使用二进制指数法计算(base^exp) % MOD
     * 
     * @param base 底数
     * @param exp 指数
     * @return (base^exp) % MOD
     * 
     * 时间复杂度：O(log exp)
     * 空间复杂度：O(1)
     */
    static long power(long base, long exp) {
        long result = 1;
        while (exp > 0) {
            if (exp % 2 == 1) {
                result = (result * base) % MOD;
            }
            base = (base * base) % MOD;
            exp /= 2;
        }
        return result;
    }
    
    /**
     * BFS求从起点开始的最远节点和距离
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
    static int[] bfs(int start) {
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
        
        return new int[]{lastNode, maxDistance};
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
        int[] firstBFS = bfs(1);
        
        // 第二次BFS，从第一次找到的最远节点开始找到另一个最远节点
        int[] secondBFS = bfs(firstBFS[0]);
        
        // 第二次BFS的距离就是树的直径
        return secondBFS[1];
    }
    
    /**
     * DFS计算子树大小
     * 
     * 算法思路：
     * 1. 从指定节点开始进行深度优先搜索
     * 2. 递归计算每个子树的大小
     * 3. 子树大小等于所有子节点子树大小之和加1（当前节点）
     * 
     * @param u 当前节点
     * @param parent 父节点
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    static void dfsSubtreeSize(int u, int parent) {
        subtreeSize[u] = 1;
        for (int v : graph[u]) {
            if (v != parent) {
                dfsSubtreeSize(v, u);
                subtreeSize[u] += subtreeSize[v];
            }
        }
    }
    
    /**
     * DFS计算深度
     * 
     * 算法思路：
     * 1. 从指定节点开始进行深度优先搜索
     * 2. 递归计算每个节点的深度
     * 3. 节点深度等于父节点深度加1
     * 
     * @param u 当前节点
     * @param parent 父节点
     * @param d 当前深度
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    static void dfsDepth(int u, int parent, int d) {
        depth[u] = d;
        for (int v : graph[u]) {
            if (v != parent) {
                dfsDepth(v, u, d + 1);
            }
        }
    }
    
    /**
     * 计算满足条件的方案数
     * 
     * 算法思路：
     * 1. 首先计算树的直径
     * 2. 根据直径的奇偶性分情况讨论
     * 3. 对于偶数直径，有一个中心点；对于奇数直径，有一个中心边
     * 4. 使用组合数学计算满足条件的方案数
     * 
     * @return 满足条件的方案数
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    static long solve() {
        // 计算树的直径
        diameter = findDiameter();
        
        // 特殊情况：直径为0（只有一个节点）
        if (diameter == 0) {
            return 1;  // 只有一种方案：选择这个节点
        }
        
        // 计算子树大小
        subtreeSize = new int[n + 1];
        dfsSubtreeSize(1, 0);
        
        // 计算深度
        depth = new int[n + 1];
        dfsDepth(1, 0, 0);
        
        // 找到深度最大的节点
        int deepestNode = 1;
        for (int i = 2; i <= n; i++) {
            if (depth[i] > depth[deepestNode]) {
                deepestNode = i;
            }
        }
        
        // 从最深节点再次DFS，找到直径的端点
        dfsDepth(deepestNode, 0, 0);
        int endpoint1 = deepestNode;
        for (int i = 1; i <= n; i++) {
            if (depth[i] > depth[endpoint1]) {
                endpoint1 = i;
            }
        }
        
        // 从endpoint1再次DFS，找到另一个端点
        dfsDepth(endpoint1, 0, 0);
        int endpoint2 = 1;
        for (int i = 2; i <= n; i++) {
            if (depth[i] > depth[endpoint2]) {
                endpoint2 = i;
            }
        }
        
        // 计算满足条件的方案数
        // 如果直径是偶数，有一个中心点
        // 如果直径是奇数，有一个中心边
        if (diameter % 2 == 0) {
            // 直径为偶数，有一个中心点
            // 找到中心点
            int center = 0;
            dfsDepth(endpoint1, 0, 0);
            for (int i = 1; i <= n; i++) {
                if (depth[i] == diameter / 2 && 
                    bfs(i)[1] == diameter / 2) {
                    center = i;
                    break;
                }
            }
            
            // 计算以中心点为根的子树中满足条件的方案数
            long result = 1;
            for (int v : graph[center]) {
                // 计算每个子树中满足条件的方案数
                long subtreeWays = power(2, subtreeSize[v]) - 1;
                result = (result * subtreeWays) % MOD;
            }
            
            // 至少选择两个节点
            result = (result - 1 + MOD) % MOD;
            return result;
        } else {
            // 直径为奇数，有一个中心边
            // 找到中心边的两个端点
            dfsDepth(endpoint1, 0, 0);
            int center1 = 0, center2 = 0;
            for (int i = 1; i <= n; i++) {
                if (depth[i] == diameter / 2) {
                    if (center1 == 0) {
                        center1 = i;
                    } else {
                        center2 = i;
                        break;
                    }
                }
            }
            
            // 计算每个部分中满足条件的方案数
            long ways1 = power(2, subtreeSize[center1]) - 1;
            long ways2 = power(2, subtreeSize[center2]) - 1;
            
            long result = (ways1 * ways2) % MOD;
            return result;
        }
    }
    
    /**
     * 主方法
     * 
     * 输入格式：
     * - 第一行包含一个整数N，表示树中节点的数量
     * - 接下来N-1行，每行包含两个整数u和v，表示节点u和v之间有一条边
     * 
     * 输出格式：
     * - 输出一个整数，表示满足条件的方案数，对998244353取模
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
        
        // 计算并输出结果
        out.println(solve());
        out.flush();
        out.close();
        br.close();
    }
}