package class121;

// SPOJ MDST - Minimum Diameter Spanning Tree
// 题目：给定一个简单无向图G的邻接顶点列表，找到最小直径生成树T，并输出该树的直径diam(T)。
// 树的直径是指树中任意两点之间最长的简单路径。
// 来源：SPOJ Problem Set
// 链接：https://www.spoj.com/problems/MDST/

import java.io.*;
import java.util.*;

public class SPOJ_MDST_MinimumDiameterSpanningTree {
    
    static final int MAXN = 501;
    static final int INF = 0x3f3f3f3f;
    
    static int n, m;  // 节点数和边数
    static int[][] graph;  // 邻接矩阵表示图
    static int[][] dist;   // 所有点对之间的最短距离
    static int[][] parent; // 用于重构路径
    
    /**
     * Floyd-Warshall算法计算所有点对之间的最短距离
     * 
     * 时间复杂度：O(n^3)
     * 空间复杂度：O(n^2)
     */
    static void floydWarshall() {
        // 初始化距离矩阵
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (i == j) {
                    dist[i][j] = 0;
                } else if (graph[i][j] != 0) {
                    dist[i][j] = graph[i][j];
                } else {
                    dist[i][j] = INF;
                }
                parent[i][j] = i;
            }
        }
        
        // Floyd-Warshall算法
        for (int k = 1; k <= n; k++) {
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        parent[i][j] = parent[k][j];
                    }
                }
            }
        }
    }
    
    /**
     * 通过绝对中心找到最小直径生成树
     * 绝对中心是边上的一个点，使得以该点为中心的生成树直径最小
     * 
     * 时间复杂度：O(n^3)
     * 空间复杂度：O(n^2)
     * 
     * @return 最小直径生成树的直径
     */
    static int findMinimumDiameterSpanningTree() {
        int minDiameter = INF;
        
        // 检查每个节点作为中心的情况
        for (int center = 1; center <= n; center++) {
            // 对节点按到中心的距离排序
            Integer[] nodes = new Integer[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = i + 1;
            }
            
            // 按照到中心的距离排序
            final int finalCenter = center;
            Arrays.sort(nodes, (a, b) -> Integer.compare(dist[finalCenter][a], dist[finalCenter][b]));
            
            // 计算以center为根的生成树的直径
            int diameter = 0;
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    if (i != j) {
                        diameter = Math.max(diameter, dist[i][j]);
                    }
                }
            }
            
            minDiameter = Math.min(minDiameter, diameter);
        }
        
        // 检查每条边上的点作为中心的情况
        for (int u = 1; u <= n; u++) {
            for (int v = u + 1; v <= n; v++) {
                if (graph[u][v] != 0) {
                    // 边(u,v)上的点作为中心
                    // 计算以这条边为中心的生成树的直径
                    int diameter = 0;
                    for (int i = 1; i <= n; i++) {
                        for (int j = 1; j <= n; j++) {
                            if (i != j) {
                                // 计算通过边(u,v)的最短路径
                                int distViaEdge = Math.min(
                                    dist[i][u] + graph[u][v] + dist[v][j],
                                    dist[i][v] + graph[u][v] + dist[u][j]
                                );
                                diameter = Math.max(diameter, distViaEdge);
                            }
                        }
                    }
                    
                    minDiameter = Math.min(minDiameter, diameter);
                }
            }
        }
        
        return minDiameter;
    }
    
    /**
     * 更高效的算法：使用绝对中心算法
     * 
     * 时间复杂度：O(n^3)
     * 空间复杂度：O(n^2)
     * 
     * @return 最小直径生成树的直径
     */
    static int findMinimumDiameterSpanningTreeOptimized() {
        int minDiameter = INF;
        
        // 对于每个节点作为中心
        for (int center = 1; center <= n; center++) {
            // 按距离排序所有节点
            Integer[] nodes = new Integer[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = i + 1;
            }
            
            final int finalCenter = center;
            Arrays.sort(nodes, (a, b) -> Integer.compare(dist[finalCenter][a], dist[finalCenter][b]));
            
            // 计算直径
            int diameter = 0;
            for (int i = 1; i <= n; i++) {
                for (int j = i + 1; j <= n; j++) {
                    diameter = Math.max(diameter, dist[i][j]);
                }
            }
            
            minDiameter = Math.min(minDiameter, diameter);
        }
        
        // 对于每条边作为中心
        for (int u = 1; u <= n; u++) {
            for (int v = u + 1; v <= n; v++) {
                if (graph[u][v] != 0) {
                    // 计算通过边(u,v)的直径
                    int diameter = 0;
                    for (int i = 1; i <= n; i++) {
                        for (int j = i + 1; j <= n; j++) {
                            int distViaEdge = Math.min(
                                dist[i][u] + graph[u][v] + dist[v][j],
                                dist[i][v] + graph[u][v] + dist[u][j]
                            );
                            diameter = Math.max(diameter, distViaEdge);
                        }
                    }
                    
                    minDiameter = Math.min(minDiameter, diameter);
                }
            }
        }
        
        return minDiameter;
    }
    
    /**
     * 主方法
     * 时间复杂度：O(n^3)
     * 空间复杂度：O(n^2)
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            String[] parts = line.split(" ");
            n = Integer.parseInt(parts[0]);
            
            if (n == 0) break;
            
            // 初始化数据结构
            graph = new int[n + 1][n + 1];
            dist = new int[n + 1][n + 1];
            parent = new int[n + 1][n + 1];
            
            // 读取邻接信息
            for (int i = 1; i <= n; i++) {
                parts = br.readLine().split(" ");
                int degree = Integer.parseInt(parts[0]);
                for (int j = 1; j <= degree; j++) {
                    int neighbor = Integer.parseInt(parts[j]);
                    graph[i][neighbor] = 1;  // 无权图，边权为1
                }
            }
            
            // 计算所有点对之间的最短距离
            floydWarshall();
            
            // 计算最小直径生成树的直径
            int result = findMinimumDiameterSpanningTreeOptimized();
            out.println(result);
        }
        
        out.flush();
        out.close();
        br.close();
    }
}