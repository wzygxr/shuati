package class122;

/**
 * AtCoder ABC187 E - Through Path（树上差分）
 * 
 * 题目来源：AtCoder
 * 题目链接：https://atcoder.jp/contests/abc187/tasks/abc187_e
 * 
 * 题目描述：
 * 给定一棵树，有Q次操作，每次操作有两种类型：
 * 1. 选择一条边(a,b)，给所有从a出发不经过b能到达的节点加上x
 * 2. 选择一条边(a,b)，给所有从b出发不经过a能到达的节点加上x
 * 所有操作完成后，输出每个节点的值
 * 
 * 算法原理：树上差分 + DFS
 * 树上差分是处理树上区间操作的一种高效技术。
 * 对于每条边(a,b)，它将树分为两个连通分量。
 * 通过差分技术，我们可以在根节点和特定节点打标记，然后通过DFS计算子树和得到最终结果。
 * 
 * 解题思路：
 * 1. 对于每条边(a,b)，将树分为两个连通分量
 * 2. 操作1：给a所在的连通分量（不包含b）的所有节点加上x
 * 3. 操作2：给b所在的连通分量（不包含a）的所有节点加上x
 * 4. 使用树上差分技术，在根节点处打标记，通过DFS计算子树和
 * 
 * 时间复杂度分析：
 * - 建图和预处理：O(N)
 * - 处理操作：O(Q)
 * - DFS计算结果：O(N)
 * 总时间复杂度：O(N + Q)
 * 
 * 空间复杂度分析：
 * - 树的存储：O(N)
 * - 边信息存储：O(N)
 * - 差分数组：O(N)
 * - DFS相关信息：O(N)
 * 总空间复杂度：O(N)
 * 
 * 工程化考量：
 * 1. 使用链式前向星存储树结构，提高空间效率和遍历速度
 * 2. 使用BufferedReader和PrintWriter进行高效输入输出
 * 3. 通过预处理确定父子关系，简化操作逻辑
 * 4. 使用long类型避免整数溢出
 * 
 * 最优解分析：
 * 树上差分是解决此类问题的最优解，通过O(1)的操作标记区间修改，
 * 避免了暴力遍历每个连通分量的所有节点，时间复杂度比暴力方法的O(N*Q)有极大提升。
 */

import java.io.*;
import java.util.*;

public class Code11_AtCoderABC187E {
    
    /**
     * 最大节点数，根据题目数据范围设定
     * 题目中N最大为2e5，设置为200001以避免越界
     */
    public static int MAXN = 200001;
    
    /**
     * 链式前向星建图
     * head[u]: 节点u的第一条边的索引
     * next[e]: 边e的下一条边的索引
     * to[e]: 边e指向的节点
     * cnt: 边的计数器
     */
    public static int[] head = new int[MAXN];
    public static int[] next = new int[MAXN << 1];
    public static int[] to = new int[MAXN << 1];
    public static int cnt = 1;
    
    /**
     * 存储边的信息
     * edges[i][0]和edges[i][1]分别表示第i条边的两个端点
     */
    public static int[][] edges = new int[MAXN][2];
    
    /**
     * 差分数组
     * diff[u]表示节点u的差分标记值
     */
    public static long[] diff = new long[MAXN];
    
    /**
     * DFS相关数组
     * parent[u]: 节点u的父节点
     * depth[u]: 节点u的深度
     * size[u]: 以节点u为根的子树大小
     */
    public static int[] parent = new int[MAXN];
    public static int[] depth = new int[MAXN];
    public static int[] size = new int[MAXN];
    
    /**
     * 向链式前向星中添加一条边
     * 
     * @param u 边的起始节点
     * @param v 边的结束节点
     */
    public static void addEdge(int u, int v) {
        // 添加u到v的边
        next[cnt] = head[u];
        to[cnt] = v;
        head[u] = cnt++;
    }
    
    /**
     * 第一次DFS，预处理树结构信息
     * 
     * @param u 当前处理的节点
     * @param fa 当前节点的父节点
     */
    public static void dfs(int u, int fa) {
        // 设置当前节点的父节点
        parent[u] = fa;
        // 设置当前节点的深度
        depth[u] = depth[fa] + 1;
        // 初始化当前节点的子树大小
        size[u] = 1;
        
        // 遍历当前节点的所有邻接节点
        for (int e = head[u]; e != 0; e = next[e]) {
            int v = to[e];
            // 只处理子节点（避免回到父节点）
            if (v != fa) {
                // 递归处理子节点
                dfs(v, u);
                // 累加子节点的子树大小
                size[u] += size[v];
            }
        }
    }
    
    /**
     * 主函数，处理输入、算法执行和输出
     * 
     * 输入格式：
     * 第一行：节点数n
     * 接下来n-1行：每行两个整数，表示树的边
     * 接下来一行：操作数q
     * 接下来q行：每行三个整数，表示一次操作
     * 
     * 输出格式：
     * n行，每行一个整数，表示每个节点的最终值
     */
    public static void main(String[] args) throws IOException {
        // 使用高效的输入输出方式
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取节点数量
        int n = Integer.parseInt(br.readLine().trim());
        
        // 读入边并构建无向树
        for (int i = 1; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            // 存储边的信息
            edges[i][0] = a;
            edges[i][1] = b;
            // 无向树需要添加双向边
            addEdge(a, b);
            addEdge(b, a);
        }
        
        // 以1为根节点进行DFS，预处理树结构信息
        dfs(1, 0);
        
        // 读取操作数
        int q = Integer.parseInt(br.readLine().trim());
        
        // 处理每次操作
        for (int i = 0; i < q; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int t = Integer.parseInt(st.nextToken());  // 操作类型
            int e = Integer.parseInt(st.nextToken());  // 边的编号
            long x = Long.parseLong(st.nextToken());   // 增加的值
            
            // 获取边的两个端点
            int a = edges[e][0];
            int b = edges[e][1];
            
            // 确保a是b的父节点（通过深度判断）
            if (depth[a] > depth[b]) {
                int temp = a;
                a = b;
                b = temp;
            }
            
            // 根据操作类型执行相应的差分标记
            if (t == 1) {
                /**
                 * 操作1：给a所在的连通分量（不包含b）的所有节点加上x
                 * 实现方法：
                 * 1. 给整棵树加上x（在根节点标记）
                 * 2. 给b的子树减去x（在b节点标记），抵消不需要增加的部分
                 */
                diff[1] += x;    // 给整棵树加上x
                diff[b] -= x;    // 给b的子树减去x
            } else {
                /**
                 * 操作2：给b所在的连通分量（不包含a）的所有节点加上x
                 * 实现方法：
                 * 直接给b的子树加上x（在b节点标记）
                 */
                diff[b] += x;    // 给b的子树加上x
            }
        }
        
        // 通过DFS计算差分数组的累加结果，得到每个节点的最终值
        dfsCalc(1, 0);
        
        // 输出每个节点的最终值
        for (int i = 1; i <= n; i++) {
            out.println(diff[i]);
        }
        
        // 关闭资源
        out.flush();
        out.close();
    }
    
    /**
     * 第二次DFS，通过回溯累加差分标记，计算每个节点的最终值
     * 
     * @param u 当前处理的节点
     * @param fa 当前节点的父节点
     */
    public static void dfsCalc(int u, int fa) {
        // 遍历当前节点的所有邻接节点
        for (int e = head[u]; e != 0; e = next[e]) {
            int v = to[e];
            // 只处理子节点（避免回到父节点）
            if (v != fa) {
                // 将父节点的差分值累加到子节点
                diff[v] += diff[u];
                // 递归处理子节点
                dfsCalc(v, u);
            }
        }
    }
}