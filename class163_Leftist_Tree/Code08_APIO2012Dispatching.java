package class155;

import java.io.*;
import java.util.*;

/**
 * APIO2012 派遣
 * 
 * 题目描述：
 * 在一个忍者的帮派里，一些忍者们被选中派遣给顾客，然后依据自己的工作获取报偿。
 * 在这个帮派里，有一名忍者被称之为Master。除了Master以外，每名忍者都有且仅有一个上级。
 * 为保密，同时增强忍者们的领导力，所有与他们工作相关的指令总是由上级发送给他的直接下属，
 * 而不允许通过其他的方式发送。
 * 
 * 现在你要招募一批忍者，并把它们派遣给顾客。你需要为每个被派遣的忍者支付一定的薪水，
 * 同时使得支付的薪水总额不超过你的预算。另外，为了发送指令，你需要选择一名忍者作为管理者，
 * 要求这个管理者可以向所有被派遣的忍者发送指令，在发送指令时，任何忍者（不管是否被派遣）
 * 都可以作为消息的传递人。管理者自己可以被派遣，也可以不被派遣。当然，如果管理者没有被派遣，
 * 你就不需要支付管理者的薪水。
 * 
 * 你的目标是在预算内使顾客的满意度最大。这里定义顾客的满意度为派遣的忍者总数乘以管理者的领导力水平，
 * 其中每个忍者的领导力水平也是一定的。
 * 
 * 写一个程序，给定每一个忍者i的上级Bi，薪水Ci，领导力Li，以及支付给忍者们的薪水总预算M，
 * 输出在预算内满足上述要求时顾客满意度的最大值。
 * 
 * 解题思路：
 * 这是一道经典的树形DP+左偏树优化的题目。
 * 1. 建立树形结构，以Master为根节点
 * 2. 从叶子节点向上进行DFS，对于每个节点维护一个大根堆（左偏树）
 * 3. 堆中存储以该节点为根的子树中所有忍者的薪水
 * 4. 当堆中薪水总和超过预算M时，不断弹出薪水最大的忍者，直到总和不超过M
 * 5. 计算以当前节点为管理者时的满意度：忍者数量 * 领导力
 * 6. 向上传递时，将当前节点的左偏树与其所有子节点的左偏树合并
 * 
 * 时间复杂度分析：
 * - 树形DFS: O(N)
 * - 左偏树合并: O(N log N)
 * - 左偏树删除: O(N log N)
 * - 总体复杂度: O(N log N)
 * 
 * 空间复杂度分析:
 * - 树形结构存储: O(N)
 * - 左偏树节点存储: O(N)
 * - 总体空间复杂度: O(N)
 */
public class Code08_APIO2012Dispatching {
    
    // 左偏树节点定义
    static class Node {
        long val;       // 节点权值（忍者薪水）
        int dist;       // 节点距离（到最近外节点的距离）
        int index;      // 节点索引
        Node left;      // 左子节点
        Node right;     // 右子节点
        
        Node(long val, int index) {
            this.val = val;
            this.index = index;
            this.dist = 0;
            this.left = null;
            this.right = null;
        }
    }
    
    static int MAXN = 100010;
    static Node[] nodes = new Node[MAXN];  // 节点数组
    static int nodeCount = 0;              // 节点计数器
    
    // 树形结构
    static int[] boss = new int[MAXN];     // 上级忍者
    static long[] salary = new long[MAXN]; // 薪水
    static long[] leadership = new long[MAXN]; // 领导力
    static int[] head = new int[MAXN];     // 邻接表头
    static int[] next = new int[MAXN];     // 邻接表next指针
    static int[] to = new int[MAXN];       // 邻接表边指向的节点
    static int edgeCount = 0;              // 边计数器
    
    // DFS相关
    static int[] roots = new int[MAXN];    // 每个节点对应的左偏树根
    static long[] sum = new long[MAXN];    // 每个左偏树的薪水总和
    static int[] size = new int[MAXN];     // 每个左偏树的节点数量
    static long budget;                    // 预算
    static long maxSatisfaction = 0;       // 最大满意度
    
    /**
     * 添加边
     * @param u 起点
     * @param v 终点
     */
    static void addEdge(int u, int v) {
        to[edgeCount] = v;
        next[edgeCount] = head[u];
        head[u] = edgeCount++;
    }
    
    /**
     * 初始化节点
     * @param val 节点权值
     * @return 节点索引
     */
    static int initNode(long val) {
        nodes[++nodeCount] = new Node(val, nodeCount);
        return nodeCount;
    }
    
    /**
     * 合并两个左偏树
     * @param a 第一棵左偏树根节点索引
     * @param b 第二棵左偏树根节点索引
     * @return 合并后左偏树根节点索引
     */
    static int merge(int a, int b) {
        // 如果其中一个为空，返回另一个
        if (a == 0) return b;
        if (b == 0) return a;
        
        // 确保a节点权值 >= b节点权值（大根堆）
        if (nodes[a].val < nodes[b].val) {
            int temp = a;
            a = b;
            b = temp;
        }
        
        // 递归合并右子树和b树
        int rightIndex = (nodes[a].right == null) ? 0 : nodes[a].right.index;
        int mergedIndex = merge(rightIndex, b);
        
        if (mergedIndex > 0) {
            nodes[a].right = nodes[mergedIndex];
        } else {
            nodes[a].right = null;
        }
        
        // 维护左偏性质：左子树距离 >= 右子树距离
        int leftDist = (nodes[a].left == null) ? -1 : nodes[a].left.dist;
        int rightDist = (nodes[a].right == null) ? -1 : nodes[a].right.dist;
        
        if (leftDist < rightDist) {
            // 交换左右子树
            Node temp = nodes[a].left;
            nodes[a].left = nodes[a].right;
            nodes[a].right = temp;
        }
        
        // 更新距离
        int newRightDist = (nodes[a].right == null) ? -1 : nodes[a].right.dist;
        nodes[a].dist = newRightDist + 1;
        
        return a;
    }
    
    /**
     * 删除左偏树根节点
     * @param root 根节点索引
     * @return 新的根节点索引
     */
    static int pop(int root) {
        if (root == 0) return 0;
        
        int leftIndex = (nodes[root].left == null) ? 0 : nodes[root].left.index;
        int rightIndex = (nodes[root].right == null) ? 0 : nodes[root].right.index;
        
        return merge(leftIndex, rightIndex);
    }
    
    /**
     * DFS遍历树形结构
     * @param u 当前节点
     */
    static void dfs(int u) {
        // 初始化当前节点的左偏树
        roots[u] = initNode(salary[u]);
        sum[u] = salary[u];
        size[u] = 1;
        
        // 遍历所有子节点
        for (int i = head[u]; i != -1; i = next[i]) {
            int v = to[i];
            dfs(v);
            
            // 合并子节点的左偏树到当前节点
            roots[u] = merge(roots[u], roots[v]);
            sum[u] += sum[v];
            size[u] += size[v];
        }
        
        // 当薪水总和超过预算时，不断弹出薪水最大的忍者
        while (sum[u] > budget) {
            sum[u] -= nodes[roots[u]].val;
            size[u]--;
            roots[u] = pop(roots[u]);
        }
        
        // 计算以当前节点为管理者时的满意度
        long satisfaction = (long) size[u] * leadership[u];
        maxSatisfaction = Math.max(maxSatisfaction, satisfaction);
    }
    
    /**
     * 主函数
     * @param args 命令行参数
     */
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取输入
        String[] line = reader.readLine().trim().split("\\s+");
        int n = Integer.parseInt(line[0]);  // 忍者数量
        budget = Long.parseLong(line[1]);   // 预算
        
        // 初始化邻接表
        Arrays.fill(head, -1);
        edgeCount = 0;
        
        int master = 0;  // Master节点编号
        
        // 读取每个忍者的信息
        for (int i = 1; i <= n; i++) {
            line = reader.readLine().trim().split("\\s+");
            boss[i] = Integer.parseInt(line[0]);      // 上级
            salary[i] = Long.parseLong(line[1]);      // 薪水
            leadership[i] = Long.parseLong(line[2]);   // 领导力
            
            // Master节点的上级为0
            if (boss[i] == 0) {
                master = i;
            } else {
                // 建立树形结构
                addEdge(boss[i], i);
            }
        }
        
        // 初始化
        nodeCount = 0;
        maxSatisfaction = 0;
        
        // 从Master节点开始DFS
        dfs(master);
        
        // 输出最大满意度
        writer.println(maxSatisfaction);
        
        writer.flush();
        writer.close();
        reader.close();
    }
}
