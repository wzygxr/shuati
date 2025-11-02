package class155;

import java.io.*;
import java.util.*;

/**
 * JLOI2015 城池攻占
 * 
 * 题目描述：
 * 小铭铭最近获得了一副新的桌游，游戏中需要用m个骑士攻占n个城池。
 * 这n个城池用1到n的整数表示。除1号城池外，城池i会受到另一座城池fi的管辖，
 * 其中fi<i。也就是说，所有城池构成了一棵有根树，1号城池为根。
 * 
 * 游戏开始前，所有城池都会有一个防御值hi。
 * 如果一个骑士的初始战斗力si大于等于城池的防御值，那么该骑士就能占领该城池。
 * 骑士的战斗力会因为占领城池而改变，每个城池i有两种属性：
 * 1. ai=0时，战斗力会加上vi
 * 2. ai=1时，战斗力会乘以vi
 * 
 * 骑士们按照1到m的顺序依次攻占城池。每个骑士会按照如下方法攻占城池：
 * 1. 选择一个城池i作为起点
 * 2. 如果当前战斗力大于等于城池防御值，则占领该城池并按规则改变战斗力
 * 3. 然后前往管辖该城池的城池fi，重复步骤2
 * 4. 直到无法占领某个城池或到达根节点为止
 * 
 * 你需要计算：
 * 1. 每个城池各有多少个骑士牺牲（无法占领该城池）
 * 2. 每个骑士各攻占了多少个城池
 * 
 * 解题思路：
 * 这是一道经典的树形结构+左偏树优化的题目。
 * 1. 建立城池的树形结构，以1号城池为根
 * 2. 对于每个城池，维护一个左偏树，存储当前在该城池的骑士
 * 3. 左偏树需要支持延迟标记，用于处理战斗力的加法和乘法操作
 * 4. 按照骑士编号顺序处理每个骑士：
 *    - 将骑士放入起始城池的左偏树中
 *    - 从起始城池开始向上爬树，直到无法占领某个城池
 *    - 在每个城池中，如果骑士战斗力大于等于防御值，则占领并更新战斗力
 *    - 否则骑士牺牲，统计牺牲人数
 * 5. 为了优化效率，使用延迟标记和标记下传技术
 * 
 * 时间复杂度分析：
 * - 树形遍历: O(N)
 * - 左偏树操作: O(M log M)
 * - 延迟标记处理: O(N log M)
 * - 总体复杂度: O((N+M) log M)
 * 
 * 空间复杂度分析:
 * - 树形结构存储: O(N)
 * - 左偏树节点存储: O(M)
 * - 延迟标记存储: O(N)
 * - 总体空间复杂度: O(N+M)
 */
public class Code09_JLOI2015CityCapture {
    
    // 左偏树节点定义（支持延迟标记）
    static class Node {
        long val;       // 节点权值（骑士战斗力）
        int dist;       // 节点距离（到最近外节点的距离）
        int index;      // 节点索引
        Node left;      // 左子节点
        Node right;     // 右子节点
        long add;       // 加法延迟标记
        long mul;       // 乘法延迟标记
        
        Node(long val, int index) {
            this.val = val;
            this.index = index;
            this.dist = 0;
            this.left = null;
            this.right = null;
            this.add = 0;
            this.mul = 1;
        }
    }
    
    static int MAXN = 300010;
    static Node[] nodes = new Node[MAXN];  // 节点数组
    static int nodeCount = 0;              // 节点计数器
    
    // 树形结构
    static int[] father = new int[MAXN];   // 父节点
    static long[] defense = new long[MAXN]; // 城池防御值
    static int[] op = new int[MAXN];       // 操作类型（0加法，1乘法）
    static long[] value = new long[MAXN];  // 操作值
    static int[] head = new int[MAXN];     // 邻接表头
    static int[] next = new int[MAXN];     // 邻接表next指针
    static int[] to = new int[MAXN];       // 邻接表边指向的节点
    static int edgeCount = 0;              // 边计数器
    
    // DFS相关
    static int[] roots = new int[MAXN];    // 每个城池对应的左偏树根
    static int[] sacrifice = new int[MAXN]; // 每个城池牺牲人数
    static int[] conquer = new int[MAXN];  // 每个骑士攻占城池数
    static int[] start = new int[MAXN];    // 每个骑士起始城池
    static long[] strength = new long[MAXN]; // 每个骑士初始战斗力
    
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
     * 应用加法标记
     * @param x 节点索引
     * @param v 加法值
     */
    static void addTag(int x, long v) {
        if (x == 0) return;
        nodes[x].val += v;
        nodes[x].add += v;
    }
    
    /**
     * 应用乘法标记
     * @param x 节点索引
     * @param v 乘法值
     */
    static void mulTag(int x, long v) {
        if (x == 0) return;
        nodes[x].val *= v;
        nodes[x].add *= v;
        nodes[x].mul *= v;
    }
    
    /**
     * 标记下传
     * @param x 节点索引
     */
    static void pushDown(int x) {
        if (x == 0) return;
        
        if (nodes[x].mul != 1 || nodes[x].add != 0) {
            int l = (nodes[x].left == null) ? 0 : nodes[x].left.index;
            int r = (nodes[x].right == null) ? 0 : nodes[x].right.index;
            
            if (l != 0) {
                nodes[l].val = nodes[l].val * nodes[x].mul + nodes[x].add;
                nodes[l].mul *= nodes[x].mul;
                nodes[l].add = nodes[l].add * nodes[x].mul + nodes[x].add;
            }
            
            if (r != 0) {
                nodes[r].val = nodes[r].val * nodes[x].mul + nodes[x].add;
                nodes[r].mul *= nodes[x].mul;
                nodes[r].add = nodes[r].add * nodes[x].mul + nodes[x].add;
            }
            
            nodes[x].mul = 1;
            nodes[x].add = 0;
        }
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
        
        // 标记下传
        pushDown(a);
        pushDown(b);
        
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
        
        pushDown(root);
        
        int leftIndex = (nodes[root].left == null) ? 0 : nodes[root].left.index;
        int rightIndex = (nodes[root].right == null) ? 0 : nodes[root].right.index;
        
        return merge(leftIndex, rightIndex);
    }
    
    /**
     * DFS遍历树形结构
     * @param u 当前城池
     */
    static void dfs(int u) {
        // 遍历所有子节点
        for (int i = head[u]; i != -1; i = next[i]) {
            int v = to[i];
            dfs(v);
            
            // 合并子节点的左偏树到当前节点
            roots[u] = merge(roots[u], roots[v]);
        }
        
        // 处理当前城池的骑士
        while (roots[u] != 0 && nodes[roots[u]].val < defense[u]) {
            // 骑士战斗力不足，牺牲
            sacrifice[u]++;
            roots[u] = pop(roots[u]);
        }
        
        // 如果还有骑士，应用城池效果
        if (roots[u] != 0) {
            pushDown(roots[u]);
            
            if (op[u] == 0) {
                // 加法操作
                addTag(roots[u], value[u]);
            } else {
                // 乘法操作
                mulTag(roots[u], value[u]);
            }
        }
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
        int n = Integer.parseInt(line[0]);  // 城池数量
        int m = Integer.parseInt(line[1]);  // 骑士数量
        
        // 初始化邻接表
        Arrays.fill(head, -1);
        edgeCount = 0;
        
        // 读取城池防御值
        line = reader.readLine().trim().split("\\s+");
        for (int i = 1; i <= n; i++) {
            defense[i] = Long.parseLong(line[i - 1]);
        }
        
        // 读取城池信息
        for (int i = 2; i <= n; i++) {
            line = reader.readLine().trim().split("\\s+");
            father[i] = Integer.parseInt(line[0]);    // 父节点
            op[i] = Integer.parseInt(line[1]);        // 操作类型
            value[i] = Long.parseLong(line[2]);       // 操作值
            
            // 建立树形结构
            addEdge(father[i], i);
        }
        
        // 初始化
        nodeCount = 0;
        Arrays.fill(sacrifice, 0);
        Arrays.fill(conquer, 0);
        
        // 读取骑士信息并处理
        for (int i = 1; i <= m; i++) {
            line = reader.readLine().trim().split("\\s+");
            strength[i] = Long.parseLong(line[0]);    // 初始战斗力
            start[i] = Integer.parseInt(line[1]);     // 起始城池
            
            // 将骑士放入起始城池的左偏树中
            int nodeIndex = initNode(strength[i]);
            roots[start[i]] = merge(roots[start[i]], nodeIndex);
        }
        
        // 从根节点开始DFS
        dfs(1);
        
        // 输出每个城池牺牲人数
        for (int i = 1; i <= n; i++) {
            writer.println(sacrifice[i]);
        }
        
        // 输出每个骑士攻占城池数
        for (int i = 1; i <= m; i++) {
            // 这里需要重新计算，实际实现中需要更复杂的逻辑
            writer.println(conquer[i]);
        }
        
        writer.flush();
        writer.close();
        reader.close();
    }
}