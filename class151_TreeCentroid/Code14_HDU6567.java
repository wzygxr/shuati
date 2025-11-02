package class120;

// HDU 6567 Cotree
// 给定两棵树，然后加上一条边使得成为一棵树，并且新树上的所有的任意两点的距离最小
// 利用树的重心的性质：树中所有点到某个点的距离和中，到重心的距离和是最小的
// 测试链接 : http://acm.hdu.edu.cn/showproblem.php?pid=6567
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
import java.util.LinkedList;
import java.util.Queue;

public class Code14_HDU6567 {

    public static int MAXN = 300001;

    public static int n;

    // 邻接表存储树
    public static ArrayList<Integer>[] adj = new ArrayList[MAXN];

    // 并查集
    public static int[] parent = new int[MAXN];

    // 子树大小
    public static int[] size = new int[MAXN];

    // 距离和
    public static long[] distSum = new long[MAXN];

    // 标记节点属于哪棵树
    public static int[] treeId = new int[MAXN];

    // 初始化
    static {
        for (int i = 0; i < MAXN; i++) {
            adj[i] = new ArrayList<>();
        }
    }

    // 并查集初始化
    public static void initUnionFind() {
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
        }
    }

    // 并查集查找
    public static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    // 并查集合并
    public static void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX != rootY) {
            parent[rootX] = rootY;
        }
    }

    // BFS分离两棵树
    public static void separateTrees() {
        Arrays.fill(treeId, 0);
        
        int treeCount = 0;
        for (int i = 1; i <= n; i++) {
            if (treeId[i] == 0) {
                treeCount++;
                Queue<Integer> queue = new LinkedList<>();
                queue.offer(i);
                treeId[i] = treeCount;
                
                while (!queue.isEmpty()) {
                    int u = queue.poll();
                    for (int v : adj[u]) {
                        if (treeId[v] == 0) {
                            treeId[v] = treeCount;
                            queue.offer(v);
                        }
                    }
                }
            }
        }
    }

    // 计算树的重心
    public static int findCentroid(int startNode) {
        // 找到连通分量的节点数
        boolean[] visited = new boolean[n + 1];
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(startNode);
        visited[startNode] = true;
        int nodeCount = 1;
        
        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v : adj[u]) {
                if (!visited[v]) {
                    visited[v] = true;
                    queue.offer(v);
                    nodeCount++;
                }
            }
        }
        
        // 计算重心
        Arrays.fill(size, 0);
        int centroid = 0;
        int minMaxSub = n;
        
        // 第一次DFS计算子树大小
        dfs1(startNode, 0, visited);
        
        // 找到重心
        findCentroidHelper(startNode, 0, nodeCount, visited, minMaxSub, centroid);
        
        return centroid;
    }

    // 第一次DFS计算子树大小
    public static void dfs1(int u, int father, boolean[] visited) {
        size[u] = 1;
        for (int v : adj[u]) {
            if (v != father && visited[v]) {
                dfs1(v, u, visited);
                size[u] += size[v];
            }
        }
    }

    // 找到重心的辅助函数
    public static void findCentroidHelper(int u, int father, int totalNodes, boolean[] visited, int minMaxSub, int centroid) {
        int maxSub = 0;
        for (int v : adj[u]) {
            if (v != father && visited[v]) {
                findCentroidHelper(v, u, totalNodes, visited, minMaxSub, centroid);
                maxSub = Math.max(maxSub, size[v]);
            }
        }
        maxSub = Math.max(maxSub, totalNodes - size[u]);
        
        if (maxSub < minMaxSub) {
            minMaxSub = maxSub;
            centroid = u;
        }
    }

    // 计算两点间距离和
    public static long calculateDistanceSum(int centroid1, int centroid2) {
        // 连接两棵树的重心
        // 新树的任意两点距离和等于两棵原子树的距离和加上连接边带来的额外距离
        
        // 计算第一棵树的距离和
        long sum1 = calculateTreeDistanceSum(centroid1);
        
        // 计算第二棵树的距离和
        long sum2 = calculateTreeDistanceSum(centroid2);
        
        // 计算连接边带来的额外距离
        // 第一棵树的节点数
        int size1 = getSize(centroid1);
        // 第二棵树的节点数
        int size2 = getSize(centroid2);
        
        // 连接边带来的额外距离是size1 * size2
        long extra = (long) size1 * size2;
        
        return sum1 + sum2 + extra;
    }

    // 计算以centroid为根的树的距离和
    public static long calculateTreeDistanceSum(int centroid) {
        Arrays.fill(distSum, 0);
        dfs2(centroid, 0);
        return distSum[centroid];
    }

    // 计算子树大小
    public static int getSize(int centroid) {
        Arrays.fill(size, 0);
        dfs3(centroid, 0);
        return size[centroid];
    }

    // 第二次DFS计算距离和
    public static void dfs2(int u, int father) {
        size[u] = 1;
        distSum[u] = 0;
        for (int v : adj[u]) {
            if (v != father) {
                dfs2(v, u);
                size[u] += size[v];
                distSum[u] += distSum[v] + size[v];
            }
        }
    }

    // 第三次DFS计算子树大小
    public static void dfs3(int u, int father) {
        size[u] = 1;
        for (int v : adj[u]) {
            if (v != father) {
                dfs3(v, u);
                size[u] += size[v];
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
            for (int i = 1; i <= n - 2; i++) {
                in.nextToken();
                int u = (int) in.nval;
                in.nextToken();
                int v = (int) in.nval;
                adj[u].add(v);
                adj[v].add(u);
            }

            // 分离两棵树
            separateTrees();

            // 找到两棵树的重心
            int centroid1 = 0, centroid2 = 0;
            for (int i = 1; i <= n; i++) {
                if (treeId[i] == 1 && centroid1 == 0) {
                    centroid1 = findCentroid(i);
                }
                if (treeId[i] == 2 && centroid2 == 0) {
                    centroid2 = findCentroid(i);
                }
            }

            // 计算最小距离和
            long result = calculateDistanceSum(centroid1, centroid2);

            out.println(result);
        }

        out.flush();
        out.close();
        br.close();
    }
}