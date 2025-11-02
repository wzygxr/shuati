package class061;

import java.util.*;

// POJ 2728. Desert King
// 题目链接: http://poj.org/problem?id=2728
// 
// 题目描述:
// 沙漠中有n个村庄，每个村庄有坐标(x, y, z)。需要修建水管连接所有村庄。
// 水管的成本包括两部分：水平距离成本和垂直高度成本。
// 求最小化总成本与总水平距离的比值。
//
// 解题思路:
// 最优比率生成树问题，使用0-1分数规划：
// 1. 二分搜索可能的比率r
// 2. 对于每个r，构建新图，边权为cost - r * dist
// 3. 计算最小生成树，如果总权值小于0，说明r偏大；否则r偏小
// 4. 使用Prim算法计算最小生成树
//
// 时间复杂度: O(n^2 * log(max_ratio))，其中n是村庄数量
// 空间复杂度: O(n^2)
// 是否为最优解: 是，这是解决最优比率生成树问题的标准方法

public class Code18_DesertKing {
    
    static class Village {
        double x, y, z;
        Village(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
    
    static final double EPS = 1e-6;
    static final double INF = 1e9;
    
    public static double prim(int n, Village[] villages, double r) {
        double[][] dist = new double[n][n];
        double[][] cost = new double[n][n];
        
        // 计算距离和成本矩阵
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dx = villages[i].x - villages[j].x;
                double dy = villages[i].y - villages[j].y;
                double dz = villages[i].z - villages[j].z;
                
                dist[i][j] = dist[j][i] = Math.sqrt(dx * dx + dy * dy);
                cost[i][j] = cost[j][i] = Math.abs(dz);
            }
        }
        
        double[] minEdge = new double[n];
        boolean[] visited = new boolean[n];
        Arrays.fill(minEdge, INF);
        minEdge[0] = 0;
        
        double total = 0;
        
        for (int i = 0; i < n; i++) {
            int u = -1;
            // 找到距离MST最近的顶点
            for (int j = 0; j < n; j++) {
                if (!visited[j] && (u == -1 || minEdge[j] < minEdge[u])) {
                    u = j;
                }
            }
            
            if (u == -1) break;
            visited[u] = true;
            total += minEdge[u];
            
            // 更新相邻顶点的距离
            for (int v = 0; v < n; v++) {
                if (!visited[v]) {
                    double edgeCost = cost[u][v] - r * dist[u][v];
                    if (edgeCost < minEdge[v]) {
                        minEdge[v] = edgeCost;
                    }
                }
            }
        }
        
        return total;
    }
    
    public static double desertKing(int n, Village[] villages) {
        double left = 0, right = 100000;
        
        // 二分搜索
        while (right - left > EPS) {
            double mid = (left + right) / 2;
            double total = prim(n, villages, mid);
            
            if (total < 0) {
                right = mid;
            } else {
                left = mid;
            }
        }
        
        return left;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            int n = scanner.nextInt();
            if (n == 0) break;
            
            Village[] villages = new Village[n];
            for (int i = 0; i < n; i++) {
                double x = scanner.nextDouble();
                double y = scanner.nextDouble();
                double z = scanner.nextDouble();
                villages[i] = new Village(x, y, z);
            }
            
            double result = desertKing(n, villages);
            System.out.printf("%.3f\n", result);
        }
        
        scanner.close();
    }
}