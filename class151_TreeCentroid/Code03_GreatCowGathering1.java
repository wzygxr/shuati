package class120;

// 牛群聚集(递归版)
// 题目来源: 洛谷 P2986 https://www.luogu.com.cn/problem/P2986
// 问题描述: 给定一棵n个节点的树，每个节点有一定数量的牛，每条边有权值（距离）
// 目标是将所有的牛汇聚在一点，使得走过的总距离最小
// 算法思路:
// 1. 利用树的重心性质：树上的边权如果都>=0，不管边权怎么分布，所有节点都走向重心的总距离和最小
// 2. 首先找到树的重心
// 3. 计算从重心到所有节点的距离
// 4. 总距离 = Σ(每个节点的牛数量 × 从重心到该节点的距离)
// 时间复杂度: O(n)，每个节点访问常数次
// 空间复杂度: O(n)，用于存储邻接表和递归栈
// 注意事项: C++这么写能通过，java会因为递归层数太多而爆栈，java能通过的写法参考Code03_GreatCowGathering2文件
// 提交说明: 提交时请把类名改成"Main"

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code03_GreatCowGathering1 {

    // 最大节点数，根据题目限制设置
    public static int MAXN = 100001;

    // 节点数量
    public static int n;

    // cow[i] : i号农场牛的数量
    public static int[] cow = new int[MAXN];

    // 牛的总数
    public static int cowSum;

    // 邻接表的链式前向星表示法
    // head[i]表示节点i的第一条边的索引
    public static int[] head = new int[MAXN];

    // next[i]表示第i条边的下一条边的索引
    public static int[] next = new int[MAXN << 1];

    // to[i]表示第i条边指向的节点
    public static int[] to = new int[MAXN << 1];

    // weight[i]表示第i条边的权值（距离）
    public static int[] weight = new int[MAXN << 1];

    // 边的计数器，从1开始编号
    public static int cnt;

    // 记录找到的最小最大子树大小
    public static int best;
    // 记录找到的重心节点
    public static int center;

    // size[i] : 从1号节点开始dfs的过程中，以i为头的子树，牛的总量
    public static int[] size = new int[MAXN];

    // path[i] : 从重心节点开始dfs的过程中，从重心到达i节点，距离是多少
    public static int[] path = new int[MAXN];

    // 初始化函数，重置相关变量
    public static void build() {
        cnt = 1;  // 边的索引从1开始
        // 将head数组从索引1到n+1初始化为0
        Arrays.fill(head, 1, n + 1, 0);
        cowSum = 0;  // 初始化牛的总数为0
        best = Integer.MAX_VALUE;  // 初始化best为最大值，用于后续比较
    }

    // 添加带权无向边的函数
    // u和v之间添加一条权值为w的边
    public static void addEdge(int u, int v, int w) {
        // 将新边添加到邻接表中
        next[cnt] = head[u];      // 新边的下一条边指向原来u节点的第一条边
        to[cnt] = v;              // 新边指向节点v
        weight[cnt] = w;          // 新边的权值为w
        head[u] = cnt++;          // u节点的第一条边更新为新边，然后cnt自增
    }

    // 寻找树的重心
    // u: 当前访问的节点
    // f: u的父节点，避免回到父节点形成环
    public static void findCenter(int u, int f) {
        // 初始化当前节点u的子树牛的总量为该节点的牛数量
        size[u] = cow[u];
        
        // 先递归遍历所有子节点
        for (int e = head[u], v; e != 0; e = next[e]) {
            v = to[e];  // 获取当前边指向的节点
            
            // 如果不是父节点，则继续DFS
            if (v != f) {
                findCenter(v, u);
            }
        }
        
        // 遍历完成后再做统计工作
        // 这个写法和之前的逻辑是一样的，为什么要拆开写？
        // 为了后续改迭代版方便
        
        // 计算以u为根时的最大子树大小
        int maxsub = 0;
        for (int e = head[u], v; e != 0; e = next[e]) {
            v = to[e];  // 获取当前边指向的节点
            
            // 如果不是父节点
            if (v != f) {
                // 将子节点v的子树牛的总量加到当前节点u的子树牛的总量中
                size[u] += size[v];
                
                // 更新以u为根时的最大子树大小
                maxsub = Math.max(maxsub, size[v]);
            }
        }
        
        // 计算父节点方向的子树大小（即整棵树去掉以u为根的子树后剩余的部分）
        maxsub = Math.max(maxsub, cowSum - size[u]);
        
        // 更新重心：如果当前节点的最大子树更小
        if (maxsub < best) {
            best = maxsub;    // 更新最小的最大子树大小
            center = u;       // 更新重心节点
        }
    }

    // 设置从重心到所有节点的距离
    // u: 当前访问的节点
    // f: u的父节点，避免回到父节点形成环
    public static void setPath(int u, int f) {
        // 遍历u的所有邻接节点
        for (int e = head[u], v; e != 0; e = next[e]) {
            v = to[e];  // 获取当前边指向的节点
            
            // 如果不是父节点
            if (v != f) {
                // 计算从重心到节点v的距离 = 从重心到节点u的距离 + 边的权值
                path[v] = path[u] + weight[e];
                // 递归设置节点v的子节点的距离
                setPath(v, u);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // 使用BufferedReader提高输入效率
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        // 使用StreamTokenizer解析输入
        StreamTokenizer in = new StreamTokenizer(br);
        // 使用PrintWriter提高输出效率
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取节点数量
        in.nextToken();
        n = (int) in.nval;
        
        // 初始化数据结构
        build();
        
        // 读取每个节点的牛数量
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            cow[i] = (int) in.nval;
        }
        
        // 读取n-1条边
        for (int i = 1, u, v, w; i < n; i++) {
            in.nextToken();
            u = (int) in.nval;
            in.nextToken();
            v = (int) in.nval;
            in.nextToken();
            w = (int) in.nval;
            
            // 添加带权无向边
            addEdge(u, v, w);
            addEdge(v, u, w);
        }
        
        // 计算并输出结果
        out.println(compute());
        
        // 刷新输出缓冲区并关闭资源
        out.flush();
        out.close();
        br.close();
    }

    // 计算最小总距离
    public static long compute() {
        // 计算牛的总数
        for (int i = 1; i <= n; i++) {
            cowSum += cow[i];
        }
        
        // 从节点1开始寻找重心，父节点为0（表示没有父节点）
        findCenter(1, 0);
        
        // 初始化重心到自身的距离为0
        path[center] = 0;
        
        // 设置从重心到所有节点的距离
        setPath(center, 0);
        
        // 计算总距离
        long ans = 0;
        for (int i = 1; i <= n; i++) {
            // 总距离 = Σ(每个节点的牛数量 × 从重心到该节点的距离)
            ans += (long) cow[i] * path[i];
        }
        
        return ans;
    }
}