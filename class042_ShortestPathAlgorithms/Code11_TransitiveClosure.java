package class065;

import java.util.*;

/**
 * Floyd算法应用: 计算有向图的传递闭包
 * 
 * 传递闭包定义: 如果存在从i到j的路径，则闭包矩阵[i][j]为true
 * 算法思路: 将Floyd算法中的距离计算改为布尔运算
 * 状态转移: reachable[i][j] = reachable[i][j] || (reachable[i][k] && reachable[k][j])
 * 
 * 时间复杂度: O(N³)
 * 空间复杂度: O(N²)
 */
public class Code11_TransitiveClosure {
    
    /**
     * 计算有向图的传递闭包
     * 
     * @param n 节点数量
     * @param edges 边数组，每个元素为[起点, 终点]
     * @return 传递闭包矩阵，reachable[i][j]为true表示存在从i到j的路径
     */
    public static boolean[][] computeTransitiveClosure(int n, int[][] edges) {
        // 初始化可达性矩阵
        boolean[][] reachable = new boolean[n][n];
        
        // 初始化: 节点到自身可达，直接边可达
        for (int i = 0; i < n; i++) {
            reachable[i][i] = true;
        }
        for (int[] edge : edges) {
            reachable[edge[0]][edge[1]] = true;
        }
        
        // Floyd-Warshall算法计算传递闭包
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    reachable[i][j] = reachable[i][j] || 
                        (reachable[i][k] && reachable[k][j]);
                }
            }
        }
        
        return reachable;
    }
    
    // 测试函数
    public static void main(String[] args) {
        // 测试用例1：简单有向图
        int n1 = 4;
        int[][] edges1 = {{0,1},{1,2},{2,3}};
        boolean[][] result1 = computeTransitiveClosure(n1, edges1);
        System.out.println("测试用例1结果:");
        for (int i = 0; i < n1; i++) {
            for (int j = 0; j < n1; j++) {
                System.out.print(result1[i][j] ? "1 " : "0 ");
            }
            System.out.println();
        }
        
        // 测试用例2：复杂有向图
        int n2 = 3;
        int[][] edges2 = {{0,1},{1,2},{2,0}};
        boolean[][] result2 = computeTransitiveClosure(n2, edges2);
        System.out.println("测试用例2结果:");
        for (int i = 0; i < n2; i++) {
            for (int j = 0; j < n2; j++) {
                System.out.print(result2[i][j] ? "1 " : "0 ");
            }
            System.out.println();
        }
    }
}