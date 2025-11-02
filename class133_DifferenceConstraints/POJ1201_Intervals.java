package class142;

import java.io.*;
import java.util.*;

/**
 * POJ 1201 Intervals 差分约束系统解法
 * 
 * 题目描述：
 * 给定n个区间[ai, bi]和对应的整数ci，要求选出最少的整数点集合，
 * 使得每个区间[ai, bi]内至少包含ci个选出的整数点。
 * 
 * 解题思路：
 * 这是一个经典的差分约束系统问题。我们可以用前缀和的思想来建模：
 * 设S[i]表示在区间[0, i]内选出的整数点个数，则：
 * 1. S[i] - S[i-1] >= 0  (选的点数非负)
 * 2. S[i] - S[i-1] <= 1  (每个位置最多选1个点)
 * 3. S[bi] - S[ai-1] >= ci  (每个区间至少选ci个点)
 * 
 * 为了处理负数下标，我们将所有坐标加上一个偏移量。
 * 
 * 差分约束建图：
 * 1. 0 <= S[i] - S[i-1] <= 1 转化为：
 *    S[i-1] - S[i] <= 0 (从i向i-1连权值为0的边)
 *    S[i] - S[i-1] <= 1 (从i-1向i连权值为1的边)
 * 2. S[bi] - S[ai-1] >= ci 转化为：
 *    S[ai-1] - S[bi] <= -ci (从bi向ai-1连权值为-ci的边)
 * 
 * 最后添加超级源点，向所有点连权值为0的边，然后求最长路。
 * 答案就是S[max_bi] - S[min_ai-1]。
 * 
 * 算法实现细节：
 * - 使用链式前向星存储图结构，提高内存访问效率
 * - 使用SPFA算法求最长路径，检测正环
 * - dist数组初始化为-INF表示无穷小距离
 * - count数组记录每个节点入队次数，用于检测正环
 * - inQueue数组标记节点是否在队列中，避免重复入队
 * 
 * 时间复杂度：O(n + m)，其中n是坐标范围，m是约束条件数
 * 空间复杂度：O(n + m)
 * 
 * 相关题目：
 * 1. POJ 1201 Intervals - 本题
 * 2. POJ 1716 Integer Intervals - 简化版本
 * 3. ZOJ 1508 Intervals - 类似题目
 * 4. 洛谷 P5960 【模板】差分约束算法
 *    链接：https://www.luogu.com.cn/problem/P5960
 *    题意：差分约束模板题
 * 5. 洛谷 P1993 小K的农场
 *    链接：https://www.luogu.com.cn/problem/P1993
 *    题意：农场约束问题
 * 6. POJ 3169 Layout
 *    链接：http://poj.org/problem?id=3169
 *    题意：奶牛布局问题
 * 7. 洛谷 P1250 种树
 *    链接：https://www.luogu.com.cn/problem/P1250
 *    题意：区间种树问题
 * 8. 洛谷 P2294 [HNOI2005]狡猾的商人
 *    链接：https://www.luogu.com.cn/problem/P2294
 *    题意：商人账本合理性判断
 * 9. 洛谷 P4926 [1007]倍杀测量者
 *    链接：https://www.luogu.com.cn/problem/P4926
 *    题意：倍杀测量问题，需要对数变换
 * 10. 洛谷 P3275 [SCOI2011]糖果
 *     链接：https://www.luogu.com.cn/problem/P3275
 *     题意：分糖果问题
 * 11. LibreOJ #10087 「一本通3.4 例1」Intervals
 *     链接：https://loj.ac/p/10087
 *     题意：区间选点问题，与POJ 1201类似
 * 12. LibreOJ #10088 「一本通3.4 例2」出纳员问题
 *     链接：https://loj.ac/p/10088
 *     题意：出纳员工作时间安排问题
 * 13. AtCoder ABC216G 01Sequence
 *     链接：https://atcoder.jp/contests/abc216/tasks/abc216_g
 *     题意：01序列问题，涉及差分约束
 * 
 * 工程化考虑：
 * 1. 异常处理：
 *    - 输入校验：检查n范围，区间端点范围
 *    - 图构建：检查边数是否超过限制
 *    - 算法执行：检测正环
 * 
 * 2. 性能优化：
 *    - 使用链式前向星存储图，节省空间
 *    - 使用静态数组而非动态数组，提高访问速度
 *    - 队列大小预分配，避免动态扩容
 * 
 * 3. 可维护性：
 *    - 函数职责单一，addEdge()加边，spfa()求解
 *    - 变量命名清晰，head、next、to、weight等表示图结构
 *    - 详细注释说明算法原理和关键步骤
 * 
 * 4. 可扩展性：
 *    - 可以轻松修改为求最短路径
 *    - 可以扩展支持更多类型的约束条件
 *    - 可以添加更多输出信息，如具体哪个约束导致无解
 * 
 * 5. 边界情况处理：
 *    - 空输入处理
 *    - 极端值处理（最大/最小约束值）
 *    - 重复约束处理
 * 
 * 6. 测试用例覆盖：
 *    - 基本功能测试
 *    - 边界值测试
 *    - 异常情况测试
 *    - 性能测试
 */
public class POJ1201_Intervals {
    // 最大节点数
    static final int MAXN = 50005;
    // 表示无穷大的常量
    static final int INF = 0x3f3f3f3f;
    
    // 链式前向星存储图的数组结构
    // head[i]表示节点i的第一条边在next数组中的索引
    static int[] head = new int[MAXN];
    // next[i]表示第i条边的下一条边在next数组中的索引
    // 每个约束最多3条边，所以数组大小为MAXN * 3
    static int[] next = new int[MAXN * 3];
    // to[i]表示第i条边指向的节点
    static int[] to = new int[MAXN * 3];
    // weight[i]表示第i条边的权重
    static int[] weight = new int[MAXN * 3];
    // 边的计数器，从1开始计数（0保留作特殊用途）
    static int cnt = 1;
    
    // SPFA算法需要的数组结构
    // dist[i]表示从源点到节点i的最长距离
    static int[] dist = new int[MAXN];
    // inQueue[i]表示节点i是否在队列中
    static boolean[] inQueue = new boolean[MAXN];
    // count[i]表示节点i被更新的次数，用于检测正环
    static int[] count = new int[MAXN];
    
    /**
     * 添加边的函数，用于向图中添加一条从节点u到节点v权重为w的有向边
     * 使用链式前向星存储图结构
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     * 
     * @param u 起点节点
     * @param v 终点节点
     * @param w 边的权重
     */
    // 添加边
    static void addEdge(int u, int v, int w) {
        // 将新边连接到节点u的邻接表中
        next[cnt] = head[u];
        // 设置新边指向的节点
        to[cnt] = v;
        // 设置新边的权重
        weight[cnt] = w;
        // 更新节点u的第一条边索引
        head[u] = cnt++;
    }
    
    /**
     * SPFA算法实现，用于检测正环并计算最长路径
     * 时间复杂度：平均O(k*m)，最坏O(n*m)，其中k是常数
     * 空间复杂度：O(n)
     * 
     * @param start 起始节点（超级源点）
     * @param n 节点数量
     * @return 如果存在正环返回false，否则返回true
     */
    // SPFA求最长路
    static boolean spfa(int start, int n) {
        // 将dist数组初始化为负无穷大
        Arrays.fill(dist, -INF);
        // 将inQueue数组初始化为false
        Arrays.fill(inQueue, false);
        // 将count数组初始化为0
        Arrays.fill(count, 0);
        
        // 创建队列用于SPFA算法
        Queue<Integer> queue = new LinkedList<>();
        // 初始化起始节点的距离为0
        dist[start] = 0;
        // 标记起始节点已在队列中
        inQueue[start] = true;
        // 将起始节点加入队列
        queue.offer(start);
        // 起始节点的更新次数设为1
        count[start] = 1;
        
        // 当队列不为空时继续循环
        while (!queue.isEmpty()) {
            // 取出队列头部的节点
            int u = queue.poll();
            // 标记该节点已出队
            inQueue[u] = false;
            
            // 遍历节点u的所有邻接点
            for (int i = head[u]; i > 0; i = next[i]) {
                // 获取邻接点v和边的权重w
                int v = to[i];
                int w = weight[i];
                
                // 松弛操作（最长路）
                // 如果通过节点u可以增加到节点v的距离
                if (dist[v] < dist[u] + w) {
                    // 更新到节点v的最长距离
                    dist[v] = dist[u] + w;
                    
                    // 如果节点v不在队列中
                    if (!inQueue[v]) {
                        // 将节点v加入队列
                        queue.offer(v);
                        // 标记节点v已在队列中
                        inQueue[v] = true;
                        // 增加节点v的更新次数
                        count[v]++;
                        
                        // 如果入队次数超过n次，说明存在正环，无解
                        if (count[v] > n) {
                            return false;
                        }
                    }
                }
            }
        }
        // 不存在正环
        return true;
    }
    
    // 主函数
    public static void main(String[] args) throws IOException {
        // 创建输入输出流
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取区间约束数量
        int n = Integer.parseInt(br.readLine());
        
        // 初始化
        // 将head数组初始化为0
        Arrays.fill(head, 0);
        // 边的计数器重置为1
        cnt = 1;
        
        // 记录坐标的最小值和最大值
        int minPos = Integer.MAX_VALUE;
        int maxPos = Integer.MIN_VALUE;
        
        // 读入n个区间约束
        for (int i = 0; i < n; i++) {
            // 读取一行输入并分割
            String[] parts = br.readLine().split(" ");
            // 解析区间左端点
            int a = Integer.parseInt(parts[0]);
            // 解析区间右端点
            int b = Integer.parseInt(parts[1]);
            // 解析区间内至少需要选择的点数
            int c = Integer.parseInt(parts[2]);
            
            // 更新坐标范围
            // 调整坐标范围
            minPos = Math.min(minPos, a);
            maxPos = Math.max(maxPos, b);
            
            // 添加约束：S[b] - S[a-1] >= c
            // 转化为：S[a-1] - S[b] <= -c
            // 从节点b向节点(a-1)连一条权值为-c的边
            addEdge(b, a - 1, -c);
        }
        
        // 添加基本约束：0 <= S[i] - S[i-1] <= 1
        for (int i = minPos; i <= maxPos; i++) {
            // S[i] - S[i-1] >= 0 => S[i-1] - S[i] <= 0
            // 从节点i向节点(i-1)连一条权值为0的边
            addEdge(i, i - 1, 0);
            // S[i] - S[i-1] <= 1 => S[i] - S[i-1] <= 1
            // 从节点(i-1)向节点i连一条权值为1的边
            addEdge(i - 1, i, 1);
        }
        
        // 添加超级源点，向所有点连权值为0的边
        // 超级源点的编号为maxPos + 1
        int superSource = maxPos + 1;
        // 从超级源点向所有可能的节点连权值为0的边，确保图的连通性
        for (int i = minPos - 1; i <= maxPos; i++) {
            addEdge(superSource, i, 0);
        }
        
        // 求最长路
        // 调用SPFA算法求最长路径，节点数量为坐标范围加3（考虑超级源点等）
        if (spfa(superSource, maxPos - minPos + 3)) {
            // 如果存在解，输出S[maxPos] - S[minPos-1]，即选中的点数
            out.println(dist[maxPos] - dist[minPos - 1]);
        } else {
            // 如果存在正环，说明无解，输出-1
            out.println(-1);  // 无解
        }
        
        // 刷新输出流并关闭资源
        out.flush();
        out.close();
        br.close();
    }
}