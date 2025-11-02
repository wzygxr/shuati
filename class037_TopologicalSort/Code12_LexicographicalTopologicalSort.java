package class060;

// 字典序最小的拓扑排序
// 给定一个有向无环图，输出字典序最小的拓扑排序序列
// 测试链接 : https://ac.nowcoder.com/acm/problem/15184
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下所有代码，把主类名改成Main，可以直接通过

import java.io.*;
import java.util.*;

/**
 * 题目解析：
 * 这是字典序最小拓扑排序的经典题目，要求输出字典序最小的拓扑序列。
 * 使用优先队列（最小堆）替代普通队列，每次选择编号最小的入度为0节点。
 * 
 * 算法思路：
 * 1. 使用邻接表存储图结构
 * 2. 计算每个节点的入度
 * 3. 使用优先队列（最小堆）存储入度为0的节点
 * 4. 每次取出编号最小的节点进行处理
 * 5. 更新邻居节点的入度
 * 
 * 时间复杂度：O((V + E) log V)，优先队列操作增加log V因子
 * 空间复杂度：O(V + E)
 * 
 * 相关题目扩展：
 * 1. 牛客网 字典序最小的拓扑序列 - https://ac.nowcoder.com/acm/problem/15184
 * 2. HDU 1285 确定比赛名次 - http://acm.hdu.edu.cn/showproblem.php?pid=1285
 * 3. SPOJ TOPOSORT - https://www.spoj.com/problems/TOPOSORT/
 * 4. Codeforces 510C Fox And Names - https://codeforces.com/problemset/problem/510/C
 * 
 * 工程化考虑：
 * 1. 输入输出优化：使用BufferedReader和StreamTokenizer
 * 2. 边界处理：空图、单节点图等情况
 * 3. 性能优化：使用优先队列实现字典序最小
 * 4. 异常处理：验证输入数据的有效性
 * 5. 模块化设计：分离建图和拓扑排序逻辑
 */
public class Code12_LexicographicalTopologicalSort {

    public static int MAXN = 100001;
    public static int MAXM = 200001;

    // 邻接表存储图
    public static int[] head = new int[MAXN];
    public static int[] next = new int[MAXM];
    public static int[] to = new int[MAXM];
    public static int cnt;

    // 入度数组
    public static int[] indegree = new int[MAXN];

    // 拓扑排序结果
    public static int[] result = new int[MAXN];
    public static int size;

    public static int n, m;

    /**
     * 初始化图结构
     */
    public static void build() {
        cnt = 1;
        size = 0;
        Arrays.fill(head, 1, n + 1, 0);
        Arrays.fill(indegree, 1, n + 1, 0);
    }

    /**
     * 添加边 u -> v
     */
    public static void addEdge(int u, int v) {
        next[cnt] = head[u];
        to[cnt] = v;
        head[u] = cnt++;
        indegree[v]++;
    }

    /**
     * 字典序最小拓扑排序
     * @return 如果存在拓扑序列返回true，否则返回false（有环）
     */
    public static boolean lexicographicalTopologicalSort() {
        // 使用最小堆（优先队列）实现字典序最小
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        
        // 将所有入度为0的节点加入最小堆
        for (int i = 1; i <= n; i++) {
            if (indegree[i] == 0) {
                minHeap.offer(i);
            }
        }

        size = 0;
        while (!minHeap.isEmpty()) {
            int u = minHeap.poll();
            result[++size] = u;
            
            // 遍历u的所有邻居
            for (int ei = head[u]; ei != 0; ei = next[ei]) {
                int v = to[ei];
                if (--indegree[v] == 0) {
                    minHeap.offer(v);
                }
            }
        }

        return size == n;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        in.nextToken();
        n = (int) in.nval;
        in.nextToken();
        m = (int) in.nval;
        
        build();
        
        for (int i = 0; i < m; i++) {
            in.nextToken();
            int u = (int) in.nval;
            in.nextToken();
            int v = (int) in.nval;
            addEdge(u, v);
        }
        
        if (lexicographicalTopologicalSort()) {
            for (int i = 1; i <= n; i++) {
                out.print(result[i] + " ");
            }
            out.println();
        } else {
            out.println("-1");
        }
        
        out.flush();
        out.close();
        br.close();
    }
}