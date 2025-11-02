package class120;

// 平衡行为 (Balancing Act)
// 题目来源: POJ 1655 http://poj.org/problem?id=1655
// 问题描述: 给定一棵n个节点的树，找到树的重心
// 树的重心定义: 找到一个点，其所有的子树中最大的子树节点数最少
// 算法思路: 
// 1. 使用DFS遍历树，计算每个节点的子树大小
// 2. 对于每个节点，计算删除该节点后形成的各个连通块的大小
// 3. 找到使最大连通块大小最小的节点，即为重心
// 时间复杂度: O(n)，每个节点访问一次
// 空间复杂度: O(n)，用于存储邻接表和递归栈
// 提交说明: 提交时请把类名改成"Main"，可以直接通过

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code01_BalancingAct {

    // 最大节点数，根据题目限制设置
    public static int MAXN = 20001;

    // 节点数量
    public static int n;

    // 邻接表的链式前向星表示法
    // head[i]表示节点i的第一条边的索引
    public static int[] head = new int[MAXN];

    // next[i]表示第i条边的下一条边的索引
    public static int[] next = new int[MAXN << 1];

    // to[i]表示第i条边指向的节点
    public static int[] to = new int[MAXN << 1];

    // 边的计数器，从1开始编号
    public static int cnt;

    // size[i]表示以节点i为根的子树的节点数量
    public static int[] size = new int[MAXN];

    // 记录找到的重心节点编号
    public static int center;

    // 记录重心节点最大子树的节点数
    public static int best;

    // 初始化函数，重置邻接表和相关变量
    public static void build() {
        cnt = 1;  // 边的索引从1开始
        // 将head数组从索引1到n+1初始化为0
        Arrays.fill(head, 1, n + 1, 0);
        // 初始化best为最大值，用于后续比较
        best = Integer.MAX_VALUE;
    }

    // 添加无向边的函数
    // u和v之间添加一条边
    public static void addEdge(int u, int v) {
        // 将新边添加到邻接表中
        next[cnt] = head[u];  // 新边的下一条边指向原来u节点的第一条边
        to[cnt] = v;          // 新边指向节点v
        head[u] = cnt++;      // u节点的第一条边更新为新边，然后cnt自增
    }

    // 深度优先搜索函数，用于计算子树大小和找到重心
    // u: 当前访问的节点
    // f: u的父节点，避免回到父节点形成环
    public static void dfs(int u, int f) {
        // 初始化当前节点u的子树大小为1（包含节点u本身）
        size[u] = 1;
        
        // 以当前节点u做根节点，最大的子树有多少节点
        int maxsub = 0;
        
        // 遍历u的所有邻接节点
        for (int e = head[u], v; e != 0; e = next[e]) {
            v = to[e];  // 获取当前边指向的节点
            
            // 如果不是父节点，则继续DFS
            if (v != f) {
                // 递归访问子节点v
                dfs(v, u);
                
                // 将子节点v的子树大小加到当前节点u的子树大小中
                size[u] += size[v];
                
                // 更新以u为根时的最大子树大小
                maxsub = Math.max(maxsub, size[v]);
            }
        }
        
        // 计算父节点方向的子树大小（即整棵树去掉以u为根的子树后剩余的部分）
        maxsub = Math.max(maxsub, n - size[u]);
        
        // 更新重心：如果当前节点的最大子树更小，或者子树大小相同但节点编号更小
        // 题目要求找到编号最小的重心
        if (maxsub < best || (maxsub == best && u < center)) {
            best = maxsub;    // 更新最小的最大子树大小
            center = u;       // 更新重心节点
        }
    }

    public static void main(String[] args) throws IOException {
        // 使用BufferedReader提高输入效率
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        // 使用StreamTokenizer解析输入
        StreamTokenizer in = new StreamTokenizer(br);
        // 使用PrintWriter提高输出效率
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取测试用例数量
        in.nextToken();
        int testCase = (int) in.nval;
        
        // 处理每个测试用例
        for (int t = 1; t <= testCase; t++) {
            // 读取节点数量
            in.nextToken();
            n = (int) in.nval;
            
            // 初始化数据结构
            build();
            
            // 读取n-1条边
            for (int i = 1, u, v; i < n; i++) {
                in.nextToken();
                u = (int) in.nval;
                in.nextToken();
                v = (int) in.nval;
                
                // 添加无向边
                addEdge(u, v);
                addEdge(v, u);
            }
            
            // 从节点1开始DFS，父节点为0（表示没有父节点）
            dfs(1, 0);
            
            // 输出重心节点编号和最大子树的节点数
            out.println(center + " " + best);
        }
        
        // 刷新输出缓冲区并关闭资源
        out.flush();
        out.close();
        br.close();
    }
}