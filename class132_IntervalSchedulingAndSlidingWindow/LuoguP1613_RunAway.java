package class129;

import java.util.*;

/**
 * 洛谷 P1613 跑路
 * 
 * 题目描述：
 * 一共有n个节点，编号1~n，一共有m条有向边，每条边1公里
 * 有一个空间跑路器，每秒你都可以直接移动2^k公里，每秒钟可以随意决定k的值
 * 题目保证1到n之间一定可以到达，返回1到n最少用几秒
 * 
 * 解题思路：
 * 这道题是一个结合了倍增思想和最短路径算法的问题。
 * 空间跑路器的特性允许我们在每秒内移动2^k步，这启发我们使用倍增思想来预处理可能的路径。
 * 
 * 核心思想：
 * 1. 预处理：使用倍增思想，计算任意两点之间是否存在长度为2^k的路径
 * 2. 最短路径：在预处理的基础上，使用Floyd算法计算最短路径
 * 
 * 具体步骤：
 * 1. 初始化：对于每条边(u,v)，标记u到v存在长度为2^0=1的路径
 * 2. 倍增预处理：对于k从1到最大值，如果存在i到j长度为2^(k-1)的路径，
 *    且存在j到p长度为2^(k-1)的路径，则i到p存在长度为2^k的路径
 * 3. 最短路径计算：使用Floyd算法，在新图上计算1到n的最短路径
 * 
 * 时间复杂度分析：
 * - 预处理阶段：O(n^3 * log(max_distance))
 *   - 对于每个k值，需要进行O(n^3)的三重循环
 *   - k的最大值通常为log2(max_distance)，这里取64足够处理大部分情况
 * - 最短路径计算：O(n^3)
 * - 总时间复杂度：O(n^3 * log(max_distance))
 * 空间复杂度：O(n^2 * log(max_distance)) - 存储倍增表
 * 
 * 倍增思想与图论结合总结：
 * 1. 倍增思想在图论中的应用扩展了传统算法的能力
 * 2. 常见应用场景：
 *    - 最短路径问题（如本题）
 *    - 可达性问题
 *    - 最小生成树问题
 *    - 图的直径和中心
 * 3. 关键技巧：
 *    - 预处理2^k步的信息
 *    - 利用二进制分解组合不同步长
 *    - 结合经典图算法（如Floyd、Dijkstra等）
 * 4. 优势：
 *    - 可以处理具有特殊移动规则的图问题
 *    - 有时可以将指数级时间复杂度降为对数级
 *    - 能够高效处理大规模图数据
 * 
 * 补充题目汇总：
 * 1. Codeforces 835D - Palindromic characteristics (字符串倍增)
 * 2. POJ 3253 - Fence Repair (贪心 + 倍增思想)
 * 3. HDU 3507 - Print Article (DP斜率优化，也可用倍增思想优化)
 * 4. Codeforces 609E. Minimum spanning tree for each edge
 * 5. LeetCode 834. 树中距离之和
 * 6. AtCoder ABC160F. Distributing Integers
 * 7. HackerEarth - City and Flood
 * 8. 洛谷 P3379 【模板】最近公共祖先（LCA）
 * 9. 牛客网 NC14513. 最短路
 * 10. 杭电OJ 6799. Tree
 * 11. POJ 3728. The merchant
 * 12. UVa 12950. Airport
 * 13. CodeChef - TALAZY
 * 14. SPOJ - MKTHNUM
 * 15. Project Euler 266. Pseudo Square Root
 * 16. HackerEarth - Road Trip
 * 17. 计蒜客 - 最短路径
 * 18. ZOJ 3623. Battle Ships
 * 19. acwing 161. 电话线路
 * 20. LOJ 10130. 黑暗城堡
 * 
 * 工程化考量：
 * 1. 在实际应用中，这种结合了倍增和图论的算法常用于：
 *    - 网络路由算法优化
 *    - 交通网络规划
 *    - 游戏开发中的寻路算法
 *    - 社交网络分析
 * 2. 实现优化：
 *    - 使用位运算加速二进制分解过程
 *    - 考虑稀疏图的表示方法（邻接表）以节省空间
 *    - 使用更高效的最短路径算法（如Dijkstra+堆优化）
 * 3. 内存管理：
 *    - 对于大规模图，需要考虑空间优化
 *    - 可以使用稀疏表或动态规划表来减少内存占用
 * 4. 鲁棒性考虑：
 *    - 处理图中的环和自环
 *    - 处理不可达情况
 *    - 优化极端情况下的性能
 * 5. 可扩展性：
 *    - 支持动态图更新
 *    - 扩展到多维或加权图问题
 *    - 分布式处理大规模图数据
public class LuoguP1613_RunAway {
    
    // 最大节点数
    public static final int MAXN = 61;
    // 最大幂次
    public static final int MAXP = 64;
    // 表示不可达
    public static final int INF = Integer.MAX_VALUE;
    
    // st[i][j][p] : i到j的距离是不是2^p
    public static boolean[][][] st = new boolean[MAXN][MAXN][MAXP + 1];
    
    // time[i][j] : i到j的最短时间
    public static int[][] time = new int[MAXN][MAXN];
    
    public static int n, m;
    
    /**
     * 初始化数据结构
     */
    public static void build() {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                st[i][j][0] = false;
                time[i][j] = INF;
            }
        }
    }
    
    /**
     * 主要计算函数
     * 
     * @return 从节点1到节点n的最短时间
     */
    public static int compute() {
        // 先枚举次方
        // 再枚举跳板
        // 最后枚举每一组(i,j)
        for (int p = 1; p <= MAXP; p++) {
            for (int jump = 1; jump <= n; jump++) {
                for (int i = 1; i <= n; i++) {
                    for (int j = 1; j <= n; j++) {
                        // 如果i到jump有2^(p-1)的路径，jump到j也有2^(p-1)的路径
                        // 那么i到j就有2^p的路径，时间为1秒
                        if (st[i][jump][p - 1] && st[jump][j][p - 1]) {
                            st[i][j][p] = true;
                            time[i][j] = 1;
                        }
                    }
                }
            }
        }
        
        // 如果1到n已经可以直接1秒到达，直接返回
        if (time[1][n] != 1) {
            // 使用Floyd算法计算最短路径
            // 先枚举跳板
            // 最后枚举每一组(i,j)
            for (int jump = 1; jump <= n; jump++) {
                for (int i = 1; i <= n; i++) {
                    for (int j = 1; j <= n; j++) {
                        // 如果i到jump可达，jump到j也可达
                        // 更新i到j的最短时间
                        if (time[i][jump] != INF && time[jump][j] != INF) {
                            time[i][j] = Math.min(time[i][j], time[i][jump] + time[jump][j]);
                        }
                    }
                }
            }
        }
        
        return time[1][n];
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 模拟测试用例
        n = 4;
        m = 4;
        build();
        
        // 添加边: 1->1, 1->2, 2->3, 3->4
        st[1][1][0] = true;
        time[1][1] = 1;
        
        st[1][2][0] = true;
        time[1][2] = 1;
        
        st[2][3][0] = true;
        time[2][3] = 1;
        
        st[3][4][0] = true;
        time[3][4] = 1;
        
        System.out.println("测试用例:");
        System.out.println("节点数: 4, 边数: 4");
        System.out.println("边: 1->1, 1->2, 2->3, 3->4");
        System.out.println("最短时间: " + compute()); // 期望输出: 1
    }
}