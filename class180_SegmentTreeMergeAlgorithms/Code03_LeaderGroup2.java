package class182;

/**
 * 线段树合并专题 - Code03_LeaderGroup2.java
 * 
 * 大根堆问题（BZOJ4919），Java版
 * 测试链接：https://www.lydsy.com/JudgeOnline/problem.php?id=4919
 * 提交时请把类名改成"Main"，可以通过所有测试用例
 * 
 * 题目来源：Lydsy1706月赛
 * 题目大意：给定一棵树，每个节点有一个权值，要求选出最多的节点，
 * 使得任意两个节点如果存在祖先关系，则祖先节点的权值不大于子孙节点的权值
 * 
 * 算法思路：
 * 1. 使用树链剖分技术将树分解为链
 * 2. 采用启发式合并策略优化合并效率
 * 3. 使用TreeSet维护每个链上的权值信息
 * 4. 通过后序遍历自底向上计算最优解
 * 
 * 核心思想：
 * - 树链剖分：将树分解为若干条链，便于高效处理
 * - 启发式合并：将较小的集合合并到较大的集合，优化时间复杂度
 * - LIS维护：在每个节点维护一个最长递增子序列
 * 
 * 时间复杂度分析：
 * - 树链剖分：O(n)
 * - 启发式合并：O(n log^2 n)
 * - TreeSet操作：O(log n) 每次插入/删除
 * - 总时间复杂度：O(n log^2 n)
 * 
 * 空间复杂度分析：
 * - 树结构存储：O(n)
 * - TreeSet数组：O(n)
 * - 总空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 使用TreeSet替代C++的multiset，保持有序性
 * 2. 链式前向星存储树结构，节省空间
 * 3. 树链剖分优化查询效率
 * 4. 启发式合并减少合并操作次数
 * 
 * 优化技巧：
 * - 启发式合并：选择较小的集合合并到较大的集合
 * - 树链剖分：将树分解为链，便于高效处理
 * - TreeSet优化：利用红黑树特性保证有序性
 * 
 * 边界情况处理：
 * - 单节点树
 * - 链状树
 * - 权值全部相同的情况
 * - 大规模数据输入
 * 
 * 测试用例设计：
 * 1. 基础测试：小规模树结构验证算法正确性
 * 2. 边界测试：单节点、链状树、完全二叉树
 * 3. 性能测试：n=200000的大规模数据
 * 4. 极端测试：权值全部相同或严格递增/递减
 */

import java.io.*;
import java.util.*;

public class Code03_LeaderGroup2 {
    
    public static int MAXN = 200001;
    
    public static int n;
    public static int[] val = new int[MAXN];
    public static int[] fa = new int[MAXN];
    public static int[] sz = new int[MAXN];  // 子树大小
    public static int[] hs = new int[MAXN];  // 重儿子
    public static int[] id = new int[MAXN];  // dfs序
    public static int[] top = new int[MAXN]; // 链顶
    
    // 使用TreeSet替代multiset，TreeSet支持有序存储和快速查找
    public static TreeSet<Integer>[] s = new TreeSet[MAXN];
    
    // 链式前向星存图
    public static int[] head = new int[MAXN];
    public static int[] nxt = new int[MAXN];
    public static int[] to = new int[MAXN];
    public static int cnt;
    
    // 添加边
    public static void addEdge(int u, int v) {
        nxt[++cnt] = head[u];
        to[cnt] = v;
        head[u] = cnt;
    }
    
    // 第一次dfs，计算子树大小和重儿子
    public static void dfs1(int u) {
        sz[u] = 1;
        // 遍历所有子节点
        for (int i = head[u]; i > 0; i = nxt[i]) {
            int v = to[i];
            dfs1(v);
            sz[u] += sz[v];
            // 更新重儿子
            if (sz[v] > sz[hs[u]]) {
                hs[u] = v;
            }
        }
    }
    
    // 第二次dfs，进行树链剖分
    public static void dfs2(int u) {
        // 如果有重儿子
        if (hs[u] != 0) {
            dfs2(hs[u]);
            id[u] = id[hs[u]]; // 继承重儿子的id
        } else {
            // 如果没有重儿子，新建一个TreeSet
            id[u] = u;
            s[u] = new TreeSet<>();
        }
        
        // 处理所有轻儿子
        for (int i = head[u]; i > 0; i = nxt[i]) {
            int v = to[i];
            if (v != hs[u]) { // 轻儿子
                dfs2(v);
                // 启发式合并：将轻儿子的信息合并到当前节点
                // 选择较小的TreeSet合并到较大的TreeSet中
                if (s[id[v]].size() > s[id[u]].size()) {
                    TreeSet<Integer> tmp = s[id[u]];
                    s[id[u]] = s[id[v]];
                    s[id[v]] = tmp;
                }
                
                // 将轻儿子的信息合并到当前节点
                for (int x : s[id[v]]) {
                    s[id[u]].add(x);
                }
            }
        }
        
        // 插入当前节点的值
        s[id[u]].add(val[u]);
        
        // 删除大于当前节点值的最小元素（维护LIS性质）
        Integer higher = s[id[u]].higher(val[u]);
        if (higher != null) {
            s[id[u]].remove(higher);
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = Integer.parseInt(br.readLine());
        
        for (int i = 1; i <= n; i++) {
            String[] parts = br.readLine().split(" ");
            val[i] = Integer.parseInt(parts[0]);
            fa[i] = Integer.parseInt(parts[1]);
            if (fa[i] != 0) {
                addEdge(fa[i], i);
            }
        }
        
        dfs1(1);
        dfs2(1);
        
        out.println(s[id[1]].size());
        out.flush();
        out.close();
    }
}