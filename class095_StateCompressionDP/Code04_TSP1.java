package class080;

// 售货员的难题 (TSP问题)
// 某售货员要到n个城市去推销商品，已知各城市之间的路程(或旅费)。
// 他要选定一条从驻地出发，经过每个城市一次，最后回到驻地的路线，
// 使总的路程(或旅费)最小。
// 测试链接 : https://www.luogu.com.cn/problem/P1171
public class Code04_TSP1 {

    // 使用状态压缩动态规划解决旅行商问题(TSP)
    // 核心思想：用二进制位表示已访问城市的集合，通过状态转移找到最短路径
    // 时间复杂度: O(n^2 * 2^n)
    // 空间复杂度: O(n * 2^n)
    public static int tsp(int[][] graph) {
        int n = graph.length;
        
        // dp[mask][i] 表示访问了mask代表的城市集合，当前在城市i时的最短路径长度
        int[][] dp = new int[1 << n][n];
        
        // 初始化：将所有状态设为最大值
        for (int i = 0; i < (1 << n); i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j] = Integer.MAX_VALUE;
            }
        }
        
        // 初始状态：从城市0开始，只访问了城市0
        dp[1][0] = 0;
        
        // 状态转移：枚举所有可能的状态
        for (int mask = 1; mask < (1 << n); mask++) {
            // 枚举当前所在的城市
            for (int u = 0; u < n; u++) {
                // 如果城市u不在当前状态中，跳过
                if ((mask & (1 << u)) == 0) {
                    continue;
                }
                
                // 如果当前状态不可达，跳过
                if (dp[mask][u] == Integer.MAX_VALUE) {
                    continue;
                }
                
                // 枚举下一个要访问的城市
                for (int v = 0; v < n; v++) {
                    // 如果城市v已经访问过，跳过
                    if ((mask & (1 << v)) != 0) {
                        continue;
                    }
                    
                    // 更新状态：从城市u到城市v
                    int newMask = mask | (1 << v);
                    if (dp[newMask][v] > dp[mask][u] + graph[u][v]) {
                        dp[newMask][v] = dp[mask][u] + graph[u][v];
                    }
                }
            }
        }
        
        // 计算最终结果：从任意城市回到起点0的最短路径
        int result = Integer.MAX_VALUE;
        int fullMask = (1 << n) - 1;
        for (int i = 0; i < n; i++) {
            if (dp[fullMask][i] != Integer.MAX_VALUE) {
                result = Math.min(result, dp[fullMask][i] + graph[i][0]);
            }
        }
        
        return result;
    }

}