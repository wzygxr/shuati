package class122;

/**
 * 异象石 (LOJ 10132)
 * 
 * 题目来源：LibreOJ
 * 题目链接：https://loj.ac/problem/10132
 * 
 * 题目描述：
 * 在一个圆上有n个点，按顺时针编号为0到n-1。
 * 有m次操作，每次操作会在两个点之间连一条弦。
 * 每次操作后，求所有弦将圆分割成多少个区域。
 * 
 * 算法原理：虚树 + LCA
 * 这是一个结合了虚树和LCA的高级树上算法问题。
 * 
 * 解题思路：
 * 1. 将圆上的点按照顺时针顺序构建一棵树（实际上是环的生成树）
 * 2. 使用虚树技术维护被选中的点集合
 * 3. 通过计算虚树上所有边的长度之和来确定区域数量
 * 4. 区域数量 = 边长之和/2 + 1
 * 
 * 时间复杂度分析：
 * - 预处理LCA：O(N log N)
 * - 每次操作构建虚树：O(K log N)，其中K是被选中点的数量
 * 总时间复杂度：O(N log N + M * K log N)
 * 
 * 空间复杂度分析：
 * - 树的存储：O(N)
 * - LCA倍增数组：O(N log N)
 * - 虚树相关数组：O(N)
 * 总空间复杂度：O(N log N)
 * 
 * 工程化考量：
 * 1. 使用链式前向星存储树结构，提高空间效率
 * 2. 使用虚树技术减少不必要的计算
 * 3. 使用StreamTokenizer进行高效输入
 * 4. 使用HashSet维护被选中的点集合
 * 
 * 最优解分析：
 * 虚树是解决此类动态点集问题的最优解，相比每次重新构建整个结构，
 * 虚树只关注关键点，大大提高了效率。
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Code09_AlienStone {

    /**
     * 最大节点数
     * 题目中N最大为1e5，设置为100001以避免越界
     */
    public static int MAXN = 100001;
    
    /**
     * 倍增数组的大小限制
     * 2^17 = 131072 > 1e5，足够处理题目中的最大节点数
     */
    public static int LIMIT = 17;
    
    /**
     * 节点数量和最大幂次
     */
    public static int n, power;
    
    /**
     * 链式前向星存储树结构
     * head[u]: 节点u的第一条边的索引
     * next[e]: 边e的下一条边的索引
     * to[e]: 边e指向的节点
     * cnt: 当前可用的边索引
     */
    public static int[] head = new int[MAXN];
    public static int[] next = new int[MAXN << 1];
    public static int[] to = new int[MAXN << 1];
    public static int cnt;
    
    /**
     * LCA相关数组
     * deep[u]: 节点u的深度
     * stjump[u][p]: 节点u的2^p级祖先
     */
    public static int[] deep = new int[MAXN];
    public static int[][] stjump = new int[MAXN][LIMIT];
    
    /**
     * 子树大小数组
     * size[u]: 以节点u为根的子树大小
     */
    public static int[] size = new int[MAXN];
    
    /**
     * DFS序数组
     * dfn[u]: 节点u的进入时间戳
     * dfn2[u]: 节点u的离开时间戳
     * dfc: 时间戳计数器
     */
    public static int[] dfn = new int[MAXN];
    public static int[] dfn2 = new int[MAXN];
    public static int dfc;
    
    /**
     * 虚树相关数组
     * stack: 虚树构建过程中的栈
     * top: 栈顶指针
     */
    public static int[] stack = new int[MAXN];
    public static int top;
    
    /**
     * 被选中的点集合
     */
    public static Set<Integer> chosen = new HashSet<>();
    
    /**
     * 计算log2(n)的整数部分
     * 
     * @param n 输入整数
     * @return log2(n)的整数部分
     */
    public static int log2(int n) {
        int ans = 0;
        while ((1 << ans) <= (n >> 1)) {
            ans++;
        }
        return ans;
    }
    
    /**
     * 初始化算法所需的数据结构
     * 设置数组初始值，准备处理新的测试用例
     */
    public static void build() {
        power = log2(n);
        // 边索引从1开始，0表示没有边
        cnt = 1;
        // 初始化链式前向星的head数组
        Arrays.fill(head, 1, n + 1, 0);
        // 重置时间戳计数器
        dfc = 0;
        // 清空被选中的点集合
        chosen.clear();
    }
    
    /**
     * 向链式前向星中添加一条无向边
     * 
     * @param u 边的一个端点
     * @param v 边的另一个端点
     */
    public static void addEdge(int u, int v) {
        // 添加u到v的边
        next[cnt] = head[u];
        to[cnt] = v;
        head[u] = cnt++;
    }
    
    /**
     * 第一次DFS，预处理深度、DFS序和子树大小
     * 
     * @param u 当前处理的节点
     * @param f 当前节点的父节点
     */
    public static void dfs1(int u, int f) {
        // 设置当前节点的深度，父节点深度+1
        deep[u] = deep[f] + 1;
        // 设置当前节点的直接父节点（2^0级祖先）
        stjump[u][0] = f;
        // 设置进入时间戳
        dfn[u] = ++dfc;
        // 初始化子树大小
        size[u] = 1;
        
        // 预处理倍增数组
        // 利用动态规划思想：u的2^p级祖先 = u的2^(p-1)级祖先的2^(p-1)级祖先
        for (int p = 1; p <= power; p++) {
            stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
        }
        
        // 遍历所有子节点
        for (int e = head[u]; e != 0; e = next[e]) {
            if (to[e] != f) {
                dfs1(to[e], u);
                // 累加子节点的子树大小
                size[u] += size[to[e]];
            }
        }
        
        // 设置离开时间戳
        dfn2[u] = dfc;
    }
    
    /**
     * 使用倍增法计算两个节点的最近公共祖先
     * 
     * @param a 第一个节点
     * @param b 第二个节点
     * @return a和b的最近公共祖先
     * 
     * 时间复杂度：O(log N)
     * 空间复杂度：O(1)
     */
    public static int lca(int a, int b) {
        // 确保a的深度不小于b
        if (deep[a] < deep[b]) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        
        // 将a向上跳到与b同一深度
        for (int p = power; p >= 0; p--) {
            if (deep[stjump[a][p]] >= deep[b]) {
                a = stjump[a][p];
            }
        }
        
        // 如果a和b在同一位置，说明b是a的祖先
        if (a == b) {
            return a;
        }
        
        // 同时向上跳，直到找到最近公共祖先
        for (int p = power; p >= 0; p--) {
            if (stjump[a][p] != stjump[b][p]) {
                a = stjump[a][p];
                b = stjump[b][p];
            }
        }
        
        // 返回它们的父节点作为LCA
        return stjump[a][0];
    }
    
    /**
     * 比较两个节点的DFS序
     * 
     * @param a 第一个节点
     * @param b 第二个节点
     * @return a的DFS序是否小于b的DFS序
     */
    public static boolean cmp(int a, int b) {
        return dfn[a] < dfn[b];
    }
    
    /**
     * 计算两点间的距离
     * 
     * @param a 第一个节点
     * @param b 第二个节点
     * @return a和b之间的距离
     */
    public static int dis(int a, int b) {
        // 计算a和b的LCA
        int l = lca(a, b);
        // 距离 = depth[a] + depth[b] - 2 * depth[lca]
        return deep[a] + deep[b] - 2 * deep[l];
    }
    
    /**
     * 主函数，处理输入输出并调用相应的算法函数
     * 
     * @param args 命令行参数
     * @throws IOException 输入输出异常
     */
    public static void main(String[] args) throws IOException {
        // 使用高效的输入方式
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        // 使用高效的输出方式
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取节点数量
        in.nextToken();
        n = (int) in.nval;
        
        // 初始化数据结构
        build();
        
        // 读取树的边
        for (int i = 1, u, v; i < n; i++) {
            in.nextToken();
            u = (int) in.nval;
            in.nextToken();
            v = (int) in.nval;
            // 添加无向边
            addEdge(u, v);
            addEdge(v, u);
        }
        
        // 预处理深度、DFS序和子树大小
        dfs1(1, 0);
        
        // 读取操作数量
        in.nextToken();
        int m = (int) in.nval;
        
        // 处理每次操作
        for (int i = 1; i <= m; i++) {
            in.nextToken();
            String op = in.sval;
            
            if (op.equals("+")) {
                // 添加点操作
                in.nextToken();
                int x = (int) in.nval;
                chosen.add(x);
            } else if (op.equals("-")) {
                // 删除点操作
                in.nextToken();
                int x = (int) in.nval;
                chosen.remove(x);
            } else { // op.equals("?")
                // 查询操作
                if (chosen.size() <= 1) {
                    // 特殊情况：选中点数小于等于1
                    out.println(chosen.size());
                } else {
                    /**
                     * 构建虚树并计算答案
                     * 虚树是原树的一个简化版本，只包含关键点和它们的LCA
                     * 区域数量 = 虚树上所有边的长度之和/2 + 1
                     */
                    // 获取所有被选中的点
                    int[] nodes = chosen.stream().mapToInt(Integer::intValue).toArray();
                    // 按节点编号排序
                    Arrays.sort(nodes);
                    
                    // 按DFS序排序
                    Integer[] sorted = new Integer[nodes.length];
                    for (int j = 0; j < nodes.length; j++) {
                        sorted[j] = nodes[j];
                    }
                    Arrays.sort(sorted, (a, b) -> dfn[a] - dfn[b]);
                    
                    // 构建虚树
                    stack[0] = 1;  // 根节点入栈
                    top = 1;       // 栈顶指针
                    long ans = 0;  // 边长之和
                    
                    // 遍历所有排序后的节点
                    for (int j = 0; j < sorted.length; j++) {
                        int u = sorted[j];
                        if (top == 1) {
                            // 栈中只有一个元素，直接入栈
                            stack[top++] = u;
                        } else {
                            // 计算栈顶元素和当前节点的LCA
                            int l = lca(stack[top - 1], u);
                            // 维护虚树结构
                            while (top > 1 && deep[stack[top - 1]] > deep[l]) {
                                if (deep[stack[top - 2]] <= deep[l]) {
                                    // 栈顶元素到LCA的距离加入答案
                                    ans += dis(stack[top - 1], l);
                                    // 更新栈顶元素为LCA
                                    stack[top - 1] = l;
                                    break;
                                } else {
                                    // 栈顶元素到其父节点的距离加入答案
                                    ans += dis(stack[top - 1], stack[top - 2]);
                                    // 出栈
                                    top--;
                                }
                            }
                            // 如果当前节点不是栈顶元素，则入栈
                            if (stack[top - 1] != u) {
                                stack[top++] = u;
                            }
                        }
                    }
                    
                    // 处理栈中剩余元素
                    while (top > 1) {
                        // 栈顶元素到其父节点的距离加入答案
                        ans += dis(stack[top - 1], stack[top - 2]);
                        // 出栈
                        top--;
                    }
                    
                    // 输出结果：区域数量 = 边长之和/2 + 1
                    out.println(ans / 2 + 1);
                }
            }
        }
        
        // 确保输出被刷新
        out.flush();
        // 关闭资源
        out.close();
        br.close();
    }
}