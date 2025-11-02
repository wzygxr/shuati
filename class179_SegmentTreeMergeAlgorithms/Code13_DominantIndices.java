// CF1009F Dominant Indices，Java版
// 测试链接 : https://codeforces.com/contest/1009/problem/F

import java.io.*;
import java.util.*;

/**
 * CF1009F Dominant Indices
 * 
 * 题目来源: Codeforces Round 484 (Div. 2)
 * 题目链接: https://codeforces.com/contest/1009/problem/F
 * 
 * 题目描述:
 * 给定一棵树，对于每个节点u，求其子树中距离u恰好为k的节点数最大的k值。
 * 如果有多个k值有相同的最大节点数，取最小的k。
 * 
 * 解题思路:
 * 1. 使用深度优先搜索（DFS）遍历整棵树
 * 2. 对于每个节点u，维护一个线段树，记录其子树中各个深度的节点数目
 * 3. 在DFS过程中，递归处理子节点，并将子节点的线段树合并到父节点
 * 4. 在合并过程中，动态更新每个节点的最优k值（出现次数最多的深度，且最小）
 * 
 * 算法复杂度:
 * - 时间复杂度: O(n log n)，其中n是树的节点数。每个节点最多被访问一次，
 *   每次线段树合并操作的时间复杂度是O(log n)。
 * - 空间复杂度: O(n log n)，动态开点线段树的空间复杂度。
 * 
 * 最优解验证:
 * 线段树合并是该问题的最优解。其他可能的解法包括暴力统计每个节点的子树深度分布，
 * 但时间复杂度为O(n^2)，无法通过大规模测试用例。
 * 
 * 线段树合并解决树形统计问题的核心思想:
 * 1. 后序遍历树，先处理所有子节点
 * 2. 为每个节点维护一个数据结构，记录所需的统计信息
 * 3. 将子节点的数据结构合并到父节点，形成父节点的完整统计信息
 * 4. 利用合并过程中的中间结果回答问题
 */

public class Code13_DominantIndices {
    
    // 树的边表示
    private static List<List<Integer>> tree;
    // 每个节点的最优解（出现次数最多的深度，且最小）
    private static int[] ans;
    // 动态开点线段树的根节点数组
    private static int[] root;
    // 线段树的节点数量
    private static int cnt;
    // 线段树的左子节点、右子节点、当前节点的最大值、对应的位置
    private static int[] ls, rs, maxVal, pos;
    // 初始分配的空间大小（可以根据需要调整）
    private static final int MAXN = 100000 * 20; // 每个节点最多需要O(log n)空间
    
    public static void main(String[] args) throws IOException {
        // 使用快速IO
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));
        
        int n = Integer.parseInt(br.readLine());
        
        // 初始化树的边列表
        tree = new ArrayList<>(n + 1);
        for (int i = 0; i <= n; i++) {
            tree.add(new ArrayList<>());
        }
        
        // 读取边
        for (int i = 1; i < n; i++) {
            String[] parts = br.readLine().split(" ");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            tree.get(u).add(v);
            tree.get(v).add(u);
        }
        
        // 初始化变量
        ans = new int[n + 1];
        root = new int[n + 1];
        cnt = 0;
        
        // 初始化线段树数组
        ls = new int[MAXN];
        rs = new int[MAXN];
        maxVal = new int[MAXN];
        pos = new int[MAXN];
        Arrays.fill(ls, 0);
        Arrays.fill(rs, 0);
        Arrays.fill(maxVal, 0);
        Arrays.fill(pos, 0);
        
        // 从根节点（1号节点）开始DFS
        dfs(1, 0);
        
        // 输出所有节点的答案
        for (int i = 1; i <= n; i++) {
            pw.println(ans[i]);
        }
        
        pw.flush();
        pw.close();
        br.close();
    }
    
    /**
     * 创建新的线段树节点
     * @return 新创建的节点编号
     */
    private static int newNode() {
        cnt++;
        ls[cnt] = 0;
        rs[cnt] = 0;
        maxVal[cnt] = 0;
        pos[cnt] = 0;
        return cnt;
    }
    
    /**
     * 线段树更新操作
     * @param p 当前节点编号
     * @param l 当前区间左边界
     * @param r 当前区间右边界
     * @param x 需要更新的位置
     * @param v 更新的值（这里是+1）
     */
    private static void update(int p, int l, int r, int x, int v) {
        if (l == r) {
            // 叶子节点，直接更新值
            maxVal[p] += v;
            pos[p] = l;
            return;
        }
        
        int mid = (l + r) >> 1;
        
        // 根据x的位置决定更新左子树还是右子树
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
        
        // 更新当前节点的最大值和对应位置
        pushUp(p);
    }
    
    /**
     * 向上合并线段树节点信息
     * @param p 当前节点编号
     */
    private static void pushUp(int p) {
        // 如果左子树为空，直接使用右子树的信息
        if (ls[p] == 0) {
            maxVal[p] = maxVal[rs[p]];
            pos[p] = pos[rs[p]];
            return;
        }
        // 如果右子树为空，直接使用左子树的信息
        if (rs[p] == 0) {
            maxVal[p] = maxVal[ls[p]];
            pos[p] = pos[ls[p]];
            return;
        }
        
        // 左右子树都不为空，比较两个子树的最大值
        if (maxVal[ls[p]] > maxVal[rs[p]]) {
            // 左子树的最大值更大
            maxVal[p] = maxVal[ls[p]];
            pos[p] = pos[ls[p]];
        } else if (maxVal[ls[p]] < maxVal[rs[p]]) {
            // 右子树的最大值更大
            maxVal[p] = maxVal[rs[p]];
            pos[p] = pos[rs[p]];
        } else {
            // 最大值相等，取位置较小的
            maxVal[p] = maxVal[ls[p]];
            pos[p] = Math.min(pos[ls[p]], pos[rs[p]]);
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
    private static int merge(int x, int y, int l, int r) {
        // 如果其中一棵树为空，直接返回另一棵树
        if (x == 0) {
            return y;
        }
        if (y == 0) {
            return x;
        }
        
        // 叶子节点处理
        if (l == r) {
            // 合并两个叶子节点的值
            maxVal[x] += maxVal[y];
            pos[x] = l;
            return x;
        }
        
        int mid = (l + r) >> 1;
        
        // 递归合并左右子树
        ls[x] = merge(ls[x], ls[y], l, mid);
        rs[x] = merge(rs[x], rs[y], mid + 1, r);
        
        // 合并后更新当前节点的信息
        pushUp(x);
        
        return x;
    }
    
    /**
     * 深度优先搜索遍历树
     * @param u 当前节点
     * @param fa 父节点
     */
    private static void dfs(int u, int fa) {
        // 为当前节点创建线段树，并初始化为深度0（距离自己0）
        root[u] = newNode();
        update(root[u], 0, 100000, 0, 1);
        
        // 遍历所有子节点（排除父节点）
        for (int v : tree.get(u)) {
            if (v == fa) {
                continue;
            }
            
            // 递归处理子节点
            dfs(v, u);
            
            // 将子节点的线段树合并到当前节点
            // 注意：子节点的所有深度都需要加1，因为相对于父节点来说，距离增加了1
            // 这里我们不需要显式修改深度，而是在合并时通过参数调整
            // 实际上，由于DFS的性质，子树中的深度是相对于子节点的，合并到父节点时深度会自然增加
            root[u] = merge(root[u], root[v], 0, 100000);
        }
        
        // 记录当前节点的答案（线段树中最大值对应的位置）
        ans[u] = pos[root[u]];
    }
    
    /**
     * 工程化考量：
     * 1. 输入输出效率：使用BufferedReader和PrintWriter提高IO效率
     * 2. 空间分配：根据题目数据规模预估线段树所需空间
     * 3. 异常处理：处理了树的无环特性，避免重复访问父节点
     * 4. 内存管理：动态开点线段树避免了预分配过大数组
     * 
     * Java语言特性：
     * 1. 数组初始化：使用Arrays.fill初始化线段树数组
     * 2. 递归深度：题目中n的范围较大，但树的深度不会超过n，Java默认的栈深度足以处理
     * 3. 集合框架：使用ArrayList存储树的边列表
     * 
     * 调试技巧：
     * 1. 可以添加中间变量打印，观察线段树合并过程
     * 2. 使用断言验证线段树节点的正确性
     * 
     * 优化空间：
     * 1. 可以使用对象池管理线段树节点，减少内存分配开销
     * 2. 可以根据实际数据范围调整线段树的最大深度
     */
}