package class120;

// LeetCode 310. 最小高度树
// 题目来源：LeetCode 310 https://leetcode.cn/problems/minimum-height-trees/
// 题目描述：对于一个具有 n 个节点的树，给定 n-1 条边，找到所有可能的最小高度树的根节点。
// 算法思想：最小高度树的根节点就是树的重心
// 解题思路：
// 1. 树的高度定义为从根节点到最远叶子节点的边数
// 2. 最小高度树的根节点就是树的重心，即删除该节点后最大连通分量最小的节点
// 3. 通过一次DFS计算每个节点的最大子树大小，找到具有最小最大子树大小的所有节点
// 时间复杂度：O(n)，只需要一次DFS遍历
// 空间复杂度：O(n)，用于存储树结构和递归栈

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Code16_LeetCode310 {

    // 树的最大节点数，根据题目限制设置
    public static final int MAXN = 20001;
    
    // 邻接表存储树结构，graph[i]表示与节点i相邻的所有节点列表
    public static List<Integer>[] graph = new ArrayList[MAXN];
    
    // size[i]表示以节点i为根的子树的节点数量
    public static int[] size = new int[MAXN];
    
    // maxSub[i]表示以节点i为根时的最大子树大小
    public static int[] maxSub = new int[MAXN];
    
    // 静态初始化块，在类加载时执行一次
    static {
        // 初始化邻接表，为每个节点创建一个空的ArrayList
        for (int i = 0; i < MAXN; i++) {
            graph[i] = new ArrayList<>();
        }
    }
    
    // 计算子树大小和最大子树大小
    // u: 当前访问的节点
    // parent: u的父节点，避免回到父节点形成环
    public static void dfs(int u, int parent) {
        // 初始化当前节点u的子树大小为1（包含节点u本身）
        size[u] = 1;
        // 初始化当前节点u的最大子树大小为0
        maxSub[u] = 0;
        
        // 遍历节点u的所有邻接节点
        for (int v : graph[u]) {
            // 如果不是父节点，则继续DFS
            if (v != parent) {
                // 递归访问子节点v，父节点为u
                dfs(v, u);
                
                // 将子节点v的子树大小加到当前节点u的子树大小中
                size[u] += size[v];
                
                // 更新以u为根时的最大子树大小
                maxSub[u] = Math.max(maxSub[u], size[v]);
            }
        }
        
        // 计算父节点方向的子树大小（即整棵树去掉以u为根的子树后剩余的部分）
        maxSub[u] = Math.max(maxSub[u], size[0] - size[u]);
    }
    
    // 寻找最小高度树的根节点（即树的重心）
    // n: 节点数量
    // edges: 边的数组，每条边用两个节点表示
    // 返回值: 所有最小高度树的根节点列表
    public List<Integer> findMinHeightTrees(int n, int[][] edges) {
        // 边界情况处理
        if (n == 1) {
            // 只有一个节点时，该节点就是最小高度树的根
            return Arrays.asList(0);
        }
        if (n == 2) {
            // 只有两个节点时，两个节点都是最小高度树的根
            return Arrays.asList(0, 1);
        }
        
        // 初始化图结构
        for (int i = 0; i < n; i++) {
            graph[i].clear();    // 清空邻接表
            size[i] = 0;         // 初始化子树大小
            maxSub[i] = 0;       // 初始化最大子树大小
        }
        
        // 构建图（邻接表）
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            // 由于是无根树，添加无向边
            graph[u].add(v);
            graph[v].add(u);
        }
        
        // 设置总节点数
        size[0] = n;
        
        // 第一次DFS计算子树信息
        // 从节点0开始DFS，父节点为-1（表示没有父节点）
        dfs(0, -1);
        
        // 找到最小的最大子树大小
        // 遍历所有节点，找到最小的maxSub值
        int minMaxSub = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            minMaxSub = Math.min(minMaxSub, maxSub[i]);
        }
        
        // 收集所有重心（最小高度树的根）
        // 这些节点具有最小的最大子树大小
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (maxSub[i] == minMaxSub) {
                result.add(i);
            }
        }
        
        return result;
    }
    
    // 主方法用于测试
    public static void main(String[] args) {
        Code16_LeetCode310 solution = new Code16_LeetCode310();
        
        // 测试用例1
        int n1 = 4;
        int[][] edges1 = {{1, 0}, {1, 2}, {1, 3}};
        System.out.println("Test Case 1: " + solution.findMinHeightTrees(n1, edges1)); // Expected: [1]
        
        // 测试用例2
        int n2 = 6;
        int[][] edges2 = {{3, 0}, {3, 1}, {3, 2}, {3, 4}, {5, 4}};
        System.out.println("Test Case 2: " + solution.findMinHeightTrees(n2, edges2)); // Expected: [3, 4]
        
        // 边界测试用例
        int n3 = 1;
        int[][] edges3 = {};
        System.out.println("Test Case 3: " + solution.findMinHeightTrees(n3, edges3)); // Expected: [0]
    }
}