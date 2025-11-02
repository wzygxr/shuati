package class065;

import java.util.*;

/**
 * 差分约束系统求解 - Bellman-Ford算法应用
 * 
 * 问题描述: 求解一组形如 xj - xi ≤ ck 的不等式组
 * 算法思路: 将不等式转化为图论问题，使用Bellman-Ford求解
 * 
 * 转换方法:
 * 对于每个不等式 xj - xi ≤ ck，添加一条边 i->j，权重为ck
 * 添加超级源点0，到所有点的边权重为0
 * 运行Bellman-Ford算法，如果存在负环则无解，否则距离数组即为解
 * 
 * 时间复杂度: O(N×E)
 * 空间复杂度: O(N+E)
 */
public class Code12_DifferenceConstraints {
    
    /**
     * 求解差分约束系统
     * 
     * @param n 变量数量
     * @param constraints 约束条件数组，每个元素为[xi, xj, ck]表示xj - xi ≤ ck
     * @return 解数组，如果无解返回null
     */
    public static int[] solveDifferenceConstraints(int n, int[][] constraints) {
        // 构建图（包含超级源点0）
        List<int[]> edges = new ArrayList<>();
        
        // 添加约束边
        for (int[] constraint : constraints) {
            // 注意：变量编号从1开始，转换为从0开始
            edges.add(new int[]{constraint[0]-1, constraint[1]-1, constraint[2]});
        }
        
        // 添加超级源点边
        for (int i = 0; i < n; i++) {
            edges.add(new int[]{n, i, 0}); // 超级源点n到所有点的边权重为0
        }
        
        // 运行Bellman-Ford算法
        int[] distance = new int[n + 1];
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[n] = 0; // 超级源点距离为0
        
        // n轮松弛（n+1个节点）
        for (int i = 0; i <= n; i++) {
            boolean updated = false;
            for (int[] edge : edges) {
                int u = edge[0], v = edge[1], w = edge[2];
                if (distance[u] != Integer.MAX_VALUE && 
                    distance[u] + w < distance[v]) {
                    distance[v] = distance[u] + w;
                    updated = true;
                }
            }
            // 如果某轮没有更新，提前结束
            if (!updated) break;
        }
        
        // 检测负环
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            if (distance[u] != Integer.MAX_VALUE && 
                distance[u] + w < distance[v]) {
                return null; // 存在负环，无解
            }
        }
        
        // 返回解（去掉超级源点）
        int[] result = new int[n];
        System.arraycopy(distance, 0, result, 0, n);
        return result;
    }
    
    // 测试函数
    public static void main(String[] args) {
        // 测试用例1：有解的差分约束系统
        // 约束条件: x1 - x0 <= 2, x2 - x1 <= 3, x0 - x2 <= -4
        int n1 = 3;
        int[][] constraints1 = {{0,1,2},{1,2,3},{2,0,-4}};
        int[] result1 = solveDifferenceConstraints(n1, constraints1);
        System.out.println("测试用例1结果: " + (result1 != null ? Arrays.toString(result1) : "无解"));
        
        // 测试用例2：无解的差分约束系统
        // 约束条件: x1 - x0 <= 1, x0 - x1 <= -2
        int n2 = 2;
        int[][] constraints2 = {{0,1,1},{1,0,-2}};
        int[] result2 = solveDifferenceConstraints(n2, constraints2);
        System.out.println("测试用例2结果: " + (result2 != null ? Arrays.toString(result2) : "无解"));
    }
}