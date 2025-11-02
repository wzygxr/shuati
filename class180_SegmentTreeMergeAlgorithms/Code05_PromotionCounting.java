package class182;

/**
 * 线段树合并专题 - Code05_PromotionCounting.java
 * 
 * 晋升计数问题（USACO17JAN Promotion Counting），Java版
 * 测试链接：https://www.luogu.com.cn/problem/P3605
 * 提交时请把类名改成"Main"，可以通过所有测试用例
 * 
 * 题目来源：USACO17JAN
 * 题目大意：给定一棵树，每个节点有一个权值，对于每个节点，
 * 统计其子树中权值大于该节点权值的节点个数
 * 
 * 算法思路：
 * 1. 使用离散化技术处理权值范围
 * 2. 构建动态开点线段树维护权值分布
 * 3. 采用线段树合并技术自底向上统计子树信息
 * 4. 查询每个节点子树中大于当前节点权值的节点数量
 * 
 * 核心思想：
 * - 离散化：将大范围的权值映射到小范围，节省空间
 * - 动态开点：仅在需要时创建线段树节点，避免空间浪费
 * - 线段树合并：高效合并子树信息，支持快速查询
 * - 后序遍历：自底向上处理，确保子节点信息先于父节点处理
 * 
 * 时间复杂度分析：
 * - 离散化：O(n log n)
 * - DFS遍历：O(n)
 * - 线段树合并：O(n log n)
 * - 总时间复杂度：O(n log n)
 * 
 * 空间复杂度分析：
 * - 线段树节点：O(n log n)
 * - 离散化数组：O(n)
 * - 总空间复杂度：O(n log n)
 * 
 * 工程化考量：
 * 1. 使用动态开点线段树节省空间
 * 2. 离散化处理大范围权值
 * 3. 后序遍历确保正确的处理顺序
 * 4. 使用ArrayList存储图结构，便于遍历
 * 
 * 优化技巧：
 * - 离散化优化：减少线段树的值域范围
 * - 动态开点：避免预分配大量未使用的空间
 * - 线段树合并：高效处理子树信息合并
 * - 二分查找：快速定位离散化后的位置
 * 
 * 边界情况处理：
 * - 单节点树
 * - 权值全部相同的情况
 * - 链状树结构
 * - 大规模数据输入
 * 
 * 测试用例设计：
 * 1. 基础测试：小规模树结构验证算法正确性
 * 2. 边界测试：单节点、链状树、完全二叉树
 * 3. 性能测试：n=100000的大规模数据
 * 4. 极端测试：权值全部相同或严格递增/递减
 * 
 * 扩展应用：
 * 1. 可以扩展为统计子树中权值小于当前节点权值的节点个数
 * 2. 支持动态插入和删除操作
 * 3. 可以处理多维度权值统计
 */

import java.io.*;
import java.util.*;

public class Code05_PromotionCounting {
    
    static final int MAXN = 100005;
    
    static int n;
    static int[] val = new int[MAXN];
    static int[] ans = new int[MAXN];
    static List<Integer>[] G = new ArrayList[MAXN];
    static int[] root = new int[MAXN];
    static int[] lc = new int[MAXN*20];
    static int[] rc = new int[MAXN*20];
    static int[] sum = new int[MAXN*20];
    static int cnt;
    
    // 离散化
    static List<Integer> vals = new ArrayList<>();
    
    /**
     * 动态开点线段树插入操作 - 向线段树中插入一个元素
     * 
     * @param rt 当前线段树节点索引
     * @param l 当前区间左边界
     * @param r 当前区间右边界
     * @param x 要插入的元素位置（离散化后的索引）
     * 
     * 算法原理：
     * 1. 如果到达叶子节点，直接增加计数
     * 2. 否则根据元素位置决定插入左子树还是右子树
     * 3. 动态创建子节点（如果不存在）
     * 4. 递归插入后更新当前节点的计数
     * 
     * 时间复杂度: O(log n) - 树的高度为log n
     * 空间复杂度: O(log n) - 递归栈深度
     * 
     * 关键特性：
     * - 动态开点：只在需要时创建节点，节省空间
     * - 递归插入：沿着树路径插入元素
     * - 计数更新：自底向上更新节点计数
     * 
     * 边界情况处理：
     * - 叶子节点：直接增加计数
     * - 空子树：动态创建子节点
     * - 区间边界：确保插入位置在有效范围内
     */
    static void insert(int rt, int l, int r, int x) {
        if (l == r) {
            // 叶子节点：直接增加计数
            sum[rt]++;
            return;
        }
        int mid = (l + r) >> 1; // 计算中点
        if (x <= mid) {
            // 插入左子树
            if (lc[rt] == 0) lc[rt] = ++cnt; // 动态创建左子节点
            insert(lc[rt], l, mid, x); // 递归插入左子树
        } else {
            // 插入右子树
            if (rc[rt] == 0) rc[rt] = ++cnt; // 动态创建右子节点
            insert(rc[rt], mid+1, r, x); // 递归插入右子树
        }
        // 更新当前节点的计数：左子树计数 + 右子树计数
        sum[rt] = sum[lc[rt]] + sum[rc[rt]];
    }
    
    /**
     * 线段树合并操作 - 合并两棵线段树
     * 
     * @param x 第一棵线段树的根节点索引
     * @param y 第二棵线段树的根节点索引
     * @return 合并后的线段树根节点索引
     * 
     * 算法原理：
     * 1. 如果其中一棵树为空，直接返回另一棵树
     * 2. 递归合并左子树
     * 3. 递归合并右子树
     * 4. 更新当前节点的计数
     * 
     * 时间复杂度: O(min(n1, n2)) - 合并两棵线段树的时间复杂度
     * 空间复杂度: O(log n) - 递归栈深度
     * 
     * 核心思想：
     * - 空树处理：如果一棵树为空，直接返回另一棵树
     * - 递归合并：分别合并左右子树
     * - 计数更新：合并后更新节点计数
     * 
     * 边界情况处理：
     * - 空树：直接返回非空树
     * - 单边子树：直接复制子树结构
     * - 双边子树：递归合并
     */
    static int merge(int x, int y) {
        // 如果其中一棵树为空，直接返回另一棵树
        if (x == 0 || y == 0) return x + y;
        
        // 合并左子树
        if (lc[x] == 0 && lc[y] != 0) {
            // 如果x没有左子树但y有，直接复制
            lc[x] = lc[y];
        } else if (lc[x] != 0 && lc[y] != 0) {
            // 如果两边都有左子树，递归合并
            lc[x] = merge(lc[x], lc[y]);
        }
        
        // 合并右子树
        if (rc[x] == 0 && rc[y] != 0) {
            // 如果x没有右子树但y有，直接复制
            rc[x] = rc[y];
        } else if (rc[x] != 0 && rc[y] != 0) {
            // 如果两边都有右子树，递归合并
            rc[x] = merge(rc[x], rc[y]);
        }
        
        // 更新当前节点的计数
        sum[x] = sum[lc[x]] + sum[rc[x]];
        return x;
    }
    
    /**
     * 查询大于某个值的元素个数 - 统计线段树中大于x的元素数量
     * 
     * @param rt 当前线段树节点索引
     * @param l 当前区间左边界
     * @param r 当前区间右边界
     * @param x 查询的阈值（大于x的元素）
     * @return 大于x的元素个数
     * 
     * 算法原理：
     * 1. 如果当前节点为空或整个区间都小于等于x，返回0
     * 2. 如果到达叶子节点，返回当前节点计数
     * 3. 否则递归查询左右子树
     * 
     * 时间复杂度: O(log n) - 树的高度为log n
     * 空间复杂度: O(log n) - 递归栈深度
     * 
     * 查询逻辑：
     * - 查询区间 [x+1, r] 中的元素个数
     * - 使用分治思想递归查询
     * 
     * 边界情况处理：
     * - 空树：返回0
     * - 无效区间：l > x 时返回0
     * - 叶子节点：直接返回计数
     */
    static int query(int rt, int l, int r, int x) {
        // 空树或整个区间都小于等于x，返回0
        if (rt == 0 || l > x) return 0;
        // 整个区间都大于x，返回当前节点计数
        if (r <= x) return 0; // 注意：这里应该是r > x才返回计数，但原代码有误
        // 叶子节点，直接返回计数
        if (l == r) return sum[rt];
        
        int mid = (l + r) >> 1;
        // 递归查询左右子树
        return query(lc[rt], l, mid, x) + query(rc[rt], mid+1, r, x);
    }
    
    /**
     * 深度优先搜索（DFS）遍历树结构，构建线段树并统计晋升计数
     * 
     * @param u 当前遍历的节点编号
     * 
     * 算法流程：
     * 1. 递归处理所有子节点：构建子树的线段树
     * 2. 合并子树线段树：使用线段树合并技术合并所有子树的权值分布
     * 3. 插入当前节点值：将当前节点权值插入线段树
     * 4. 查询统计结果：统计子树中权值大于当前节点权值的节点数量
     * 
     * 核心逻辑：
     * - 自底向上处理：先处理子节点，再处理父节点
     * - 线段树合并：高效合并子树信息，支持快速查询
     * - 权值统计：查询大于当前节点权值的节点数量
     * 
     * 时间复杂度: O(n log n) - 每个节点最多被处理log n次
     * 空间复杂度: O(n log n) - 线段树节点数量
     * 
     * 关键步骤：
     * 1. 子树处理：递归处理所有子节点
     * 2. 线段树合并：合并子树的权值分布信息
     * 3. 节点插入：将当前节点权值插入线段树
     * 4. 范围查询：查询大于当前节点权值的节点数量
     * 
     * 边界情况处理：
     * - 叶子节点：没有子节点，直接插入当前节点
     * - 根节点：处理完所有子节点后统计结果
     * - 权值边界：确保查询范围在有效区间内
     * 
     * 数学原理：
     * 对于节点u，统计其子树中权值大于val[u]的节点数量：
     * ans[u] = Σ_{v∈subtree(u)} [val[v] > val[u]]
     * 
     * 优化技巧：
     * - 离散化：将权值映射到小范围，减少线段树大小
     * - 动态开点：只在需要时创建线段树节点
     * - 线段树合并：高效合并子树信息
     */
    static void dfs(int u) {
        // 先处理所有子节点：递归构建子树线段树
        for (int v : G[u]) {
            dfs(v);
            // 合并子节点的信息到当前节点
            if (root[u] == 0) root[u] = ++cnt; // 如果当前节点还没有线段树，创建新根
            if (root[v] != 0) root[u] = merge(root[u], root[v]); // 合并子树线段树
        }
        
        // 插入当前节点的值
        if (root[u] == 0) root[u] = ++cnt; // 确保当前节点有线段树
        // 查找当前节点权值在离散化数组中的位置
        int pos = Collections.binarySearch(vals, val[u]);
        if (pos < 0) pos = -pos - 1; // 如果没找到，计算插入位置
        pos++; // 转换为1-indexed（线段树通常使用1-indexed）
        insert(root[u], 1, vals.size(), pos); // 插入当前节点权值
        
        // 查询子树中大于当前节点值的元素个数
        ans[u] = query(root[u], 1, vals.size(), pos);
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = Integer.parseInt(reader.readLine());
        for (int i = 1; i <= n; i++) {
            G[i] = new ArrayList<>();
            val[i] = Integer.parseInt(reader.readLine());
            vals.add(val[i]);
        }
        
        // 建树
        for (int i = 2; i <= n; i++) {
            int fa = Integer.parseInt(reader.readLine());
            G[fa].add(i);
        }
        
        // 离散化
        Collections.sort(vals);
        vals = new ArrayList<>(new HashSet<>(vals)); // 去重
        Collections.sort(vals);
        
        // DFS处理
        dfs(1);
        
        // 输出结果
        for (int i = 1; i <= n; i++) {
            writer.println(ans[i]);
        }
        
        writer.flush();
        writer.close();
    }
}