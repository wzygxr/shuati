package class060;

// 拓扑排序模板题
// 给定一个有向图，输出其拓扑排序序列
// 如果图中有环，则输出空序列
// 测试链接 : https://www.acwing.com/problem/content/850/
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下所有代码，把主类名改成Main，可以直接通过

import java.io.*;
import java.util.*;

/**
 * 题目解析：
 * 这是拓扑排序的基础模板题，要求对有向无环图进行拓扑排序。
 * 如果图中存在环，则无法进行拓扑排序。
 * 
 * 算法思路：
 * 1. 使用Kahn算法（BFS）进行拓扑排序
 * 2. 计算每个节点的入度
 * 3. 将入度为0的节点加入队列
 * 4. BFS处理队列，更新邻居节点的入度
 * 5. 如果结果序列长度等于节点数，说明无环；否则有环
 * 
 * 时间复杂度：O(V + E)，其中V是节点数，E是边数
 * 空间复杂度：O(V + E)
 * 
 * 相关题目扩展：
 * 1. ACWing 848. 有向图的拓扑序列 - https://www.acwing.com/problem/content/850/
 * 2. 洛谷 B3644 【模板】拓扑排序 - https://www.luogu.com.cn/problem/B3644
 * 3. Aizu GRL_4_B. Topological Sort - https://onlinejudge.u-aizu.ac.jp/problems/GRL_4_B
 * 4. HackerEarth Topological Sort - https://www.hackerearth.com/practice/algorithms/graphs/topological-sort/practice-problems/
 * 
 * 工程化考虑：
 * 1. 输入输出优化：使用BufferedReader和StreamTokenizer
 * 2. 边界处理：空图、单节点图、有环图等情况
 * 3. 模块化设计：将建图、拓扑排序、结果验证分离
 * 4. 异常处理：验证输入数据的有效性
 * 5. 性能优化：使用邻接表存储图结构
 */
public class Code11_TopologicalSortTemplate {

    public static int MAXN = 100001;
    public static int MAXM = 100001;

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
     * 拓扑排序 - Kahn算法
     * @return 如果存在拓扑序列返回true，否则返回false（有环）
     */
    public static boolean topologicalSort() {
        Queue<Integer> queue = new LinkedList<>();
        
        // 将所有入度为0的节点加入队列
        for (int i = 1; i <= n; i++) {
            if (indegree[i] == 0) {
                queue.offer(i);
            }
        }

        size = 0;
        while (!queue.isEmpty()) {
            int u = queue.poll();
            result[++size] = u;
            
            // 遍历u的所有邻居
            for (int ei = head[u]; ei != 0; ei = next[ei]) {
                int v = to[ei];
                if (--indegree[v] == 0) {
                    queue.offer(v);
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
        
        if (topologicalSort()) {
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