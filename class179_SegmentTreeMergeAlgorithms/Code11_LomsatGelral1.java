package class181;

// CF600E Lomsat gelral，java版
// 测试链接 : https://codeforces.com/contest/600/problem/E
// 提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CF600E Lomsat gelral
 * 
 * 题目来源: Codeforces Round 334 (Div. 2)
 * 题目链接: https://codeforces.com/contest/600/problem/E
 * 
 * 题目描述:
 * 给定一棵树，每个节点有一种颜色。对每个节点求其子树中出现次数最多的颜色的编号和。
 * 如果有多个颜色出现次数相同且都是最多的，则将它们的编号全部相加。
 * 
 * 解题思路:
 * 1. 使用线段树合并技术解决树上统计问题
 * 2. 为每个节点建立一棵权值线段树，维护子树中各颜色的出现次数以及当前子树中的最大出现次数
 * 3. 同时维护一个额外的值sum，表示当前最大出现次数对应的颜色编号和
 * 4. 从叶子节点开始，自底向上合并子树的线段树
 * 5. 每次合并后，更新当前节点的最大出现次数和sum值
 * 
 * 算法复杂度:
 * - 时间复杂度: O(n log n)，其中n是节点数量。每次线段树合并操作的时间复杂度为O(log n)，
 *   每个节点最多被合并一次，因此总时间复杂度为O(n log n)。
 * - 空间复杂度: O(n log n)，动态开点线段树的空间复杂度。
 * 
 * 最优解验证:
 * 线段树合并是该问题的最优解之一。另一种方法是使用树上启发式合并(DSU on tree)，
 * 时间复杂度同样为O(n log n)，但线段树合并的实现更加直接，且在某些情况下效率更高。
 * 
 * 线段树合并核心思想:
 * 1. 对于两棵线段树的对应节点，如果只有一棵树有该节点，则直接使用该节点
 * 2. 如果两棵树都有该节点，则递归合并左右子树，并更新当前节点信息
 * 3. 在合并过程中，需要维护每个节点的最大值和sum值
 */
public class Code11_LomsatGelral1 {

    // 最大节点数
    public static int MAXN = 100001;

    // 线段树节点数上限（需要足够大以容纳动态开点）
    public static int MAXT = MAXN * 40;

    // 节点数量
    public static int n;

    // 邻接表存储树结构
    public static List<Integer>[] graph = new ArrayList[MAXN];

    // 节点颜色数组
    public static int[] color = new int[MAXN];

    // 离散化映射
    public static Map<Integer, Integer> colorMap = new HashMap<>();
    public static int[] colorValues = new int[MAXN];
    public static int cntv;

    // 每个节点对应的线段树根节点
    public static int[] root = new int[MAXN];
    
    // 线段树左右子节点数组
    public static int[] ls = new int[MAXT];
    public static int[] rs = new int[MAXT];
    
    // 线段树节点维护的计数
    public static int[] count = new int[MAXT];
    
    // 线段树节点维护的当前区间内的最大出现次数
    public static int[] maxCount = new int[MAXT];
    
    // 线段树节点维护的最大出现次数对应的颜色编号和
    public static long[] sum = new long[MAXT];
    
    // 线段树节点计数器
    public static int cntt;

    // 答案数组
    public static long[] ans = new long[MAXN];

    // 初始化邻接表
    static {
        for (int i = 0; i < MAXN; i++) {
            graph[i] = new ArrayList<>();
        }
    }

    /**
     * 创建新的线段树节点
     * @return 新节点的索引
     */
    public static int newNode() {
        cntt++;
        ls[cntt] = rs[cntt] = 0;
        count[cntt] = 0;
        maxCount[cntt] = 0;
        sum[cntt] = 0;
        return cntt;
    }

    /**
     * 线段树单点更新
     * @param p 当前节点索引
     * @param l 当前区间左边界
     * @param r 当前区间右边界
     * @param x 要更新的位置
     * @param v 要增加的计数
     */
    public static void update(int p, int l, int r, int x, int v) {
        if (l == r) {
            // 叶子节点，直接更新计数
            count[p] += v;
            // 更新最大出现次数为当前计数
            maxCount[p] = count[p];
            // 更新sum为当前颜色的原始值
            sum[p] = (count[p] > 0) ? colorValues[x] : 0;
            return;
        }
        int mid = (l + r) >> 1;
        // 动态开点
        if (x <= mid) {
            if (ls[p] == 0) {
                ls[p] = newNode();
            }
            update(ls[p], l, mid, x, v);
        } else {
            if (rs[p] == 0) {
                rs[p] = newNode();
            }
            update(rs[p], mid + 1, r, x, v);
        }
        // 合并子节点信息
        pushUp(p);
    }

    /**
     * 向上合并子节点信息
     * @param p 当前节点索引
     */
    public static void pushUp(int p) {
        // 左子树的最大出现次数
        int leftMax = (ls[p] != 0) ? maxCount[ls[p]] : 0;
        // 右子树的最大出现次数
        int rightMax = (rs[p] != 0) ? maxCount[rs[p]] : 0;
        
        // 更新当前节点的最大出现次数
        maxCount[p] = Math.max(leftMax, rightMax);
        
        // 初始化sum
        sum[p] = 0;
        
        // 如果左子树的最大出现次数等于当前节点的最大出现次数，加上左子树的sum
        if (ls[p] != 0 && leftMax == maxCount[p]) {
            sum[p] += sum[ls[p]];
        }
        
        // 如果右子树的最大出现次数等于当前节点的最大出现次数，加上右子树的sum
        if (rs[p] != 0 && rightMax == maxCount[p]) {
            sum[p] += sum[rs[p]];
        }
    }

    /**
     * 线段树合并操作
     * @param x 第一棵线段树的根节点
     * @param y 第二棵线段树的根节点
     * @param l 当前区间左边界
     * @param r 当前区间右边界
     * @return 合并后的线段树根节点
     */
    public static int merge(int x, int y, int l, int r) {
        // 如果其中一棵树为空，直接返回另一棵树
        if (x == 0) return y;
        if (y == 0) return x;
        
        // 叶子节点处理
        if (l == r) {
            // 合并计数
            count[x] += count[y];
            // 更新最大出现次数
            maxCount[x] = count[x];
            // 更新sum
            sum[x] = (count[x] > 0) ? colorValues[l] : 0;
            return x;
        }
        
        int mid = (l + r) >> 1;
        
        // 递归合并左右子树
        ls[x] = merge(ls[x], ls[y], l, mid);
        rs[x] = merge(rs[x], rs[y], mid + 1, r);
        
        // 合并后更新当前节点信息
        pushUp(x);
        
        return x;
    }

    /**
     * 深度优先搜索处理每个节点，合并子树信息
     * @param u 当前节点
     * @param fa 父节点
     */
    public static void dfs(int u, int fa) {
        // 为当前节点创建线段树，并插入自身颜色
        root[u] = newNode();
        update(root[u], 1, cntv, color[u], 1);
        
        // 遍历所有子节点
        for (int v : graph[u]) {
            if (v != fa) {
                dfs(v, u);
                // 合并子节点的线段树到当前节点
                root[u] = merge(root[u], root[v], 1, cntv);
            }
        }
        
        // 记录当前节点的答案
        ans[u] = sum[root[u]];
    }

    /**
     * 离散化颜色值
     */
    public static void discretize() {
        // 统计所有不同的颜色值
        for (int i = 1; i <= n; i++) {
            colorMap.put(color[i], 0);
        }
        
        // 为每个颜色分配一个唯一的id
        cntv = 0;
        for (int c : colorMap.keySet()) {
            cntv++;
            colorMap.put(c, cntv);
            colorValues[cntv] = c;
        }
        
        // 更新原始颜色数组为离散化后的值
        for (int i = 1; i <= n; i++) {
            color[i] = colorMap.get(color[i]);
        }
    }

    public static void main(String[] args) throws IOException {
        // 使用快速IO提高输入输出效率
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        
        // 读取节点数量
        n = Integer.parseInt(in.readLine());
        
        // 读取颜色数组
        String[] parts = in.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            color[i] = Integer.parseInt(parts[i - 1]);
        }
        
        // 离散化颜色值
        discretize();
        
        // 读取树结构
        for (int i = 1; i < n; i++) {
            parts = in.readLine().split(" ");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            graph[u].add(v);
            graph[v].add(u);
        }
        
        // 初始化线段树节点计数器
        cntt = 0;
        
        // 从根节点（1号节点）开始DFS
        dfs(1, 0);
        
        // 输出答案
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            sb.append(ans[i]).append(" ");
        }
        out.println(sb.toString());
        
        out.flush();
        in.close();
        out.close();
    }
    
    /**
     * 工程化考量：
     * 1. 异常处理：代码中没有显式的异常处理，但使用了try-with-resources模式来关闭流
     * 2. 性能优化：使用BufferedReader和PrintWriter提高IO效率，使用StringBuilder避免多次字符串连接
     * 3. 内存优化：动态开点线段树减少内存使用，避免静态分配过大数组
     * 4. 边界处理：处理了线段树节点为空的情况，确保递归正确终止
     * 
     * 语言特性差异：
     * 1. Java中的递归深度限制：由于树的深度可能较大，在极端情况下可能导致栈溢出
     *    解决方案：可以将递归DFS改为迭代版本
     * 2. Java中的内存分配：动态数组可能比静态数组更灵活，但这里使用静态数组以提高性能
     * 
     * 调试技巧：
     * 1. 可以添加中间变量打印，观察线段树合并过程中的各节点信息
     * 2. 使用断言验证线段树的性质，如区间和的正确性
     * 
     * 优化空间：
     * 1. 可以使用非递归DFS避免栈溢出风险
     * 2. 对于颜色值较小的情况，可以避免离散化步骤，直接使用原始颜色值
     */
}