package class078;

// 树的重心 (Tree Centroid)
// 题目描述:
// 找到一个点，其所有的子树中最大的子树节点数最少
// 换句话说，删除这个点后，剩余的最大子树的节点数最小
//
// 解题思路:
// 1. 使用树形动态规划（Tree DP）的方法
// 2. 对于每个节点，我们需要知道以下信息：
//    - 以该节点为根的子树的节点数
//    - 删除该节点后，剩余的最大子树节点数
// 3. 递归处理子树，综合计算当前节点的信息
// 4. 树的重心就是使得删除该点后，剩余的最大子树节点数最少的点
//
// 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点
// 空间复杂度: O(n) - 存储树结构和递归调用栈
// 是否为最优解: 是，这是计算树的重心的标准方法
//
// 相关题目:
// - POJ 1655 Balancing Act
// - ZOJ 3107 Godfather

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Arrays;

public class Code10_TreeCentroid {
    // 树的最大节点数
    public static int MAXN = 100001;
    
    // 树的邻接表表示
    public static ArrayList<Integer>[] tree = new ArrayList[MAXN];
    
    // 以每个节点为根的子树节点数
    public static int[] subtreeSize = new int[MAXN];
    
    // 树的重心和对应的最小最大子树节点数
    public static int centroid = 0;
    public static int minMaxSubtreeSize = Integer.MAX_VALUE;
    
    static {
        // 初始化邻接表
        for (int i = 0; i < MAXN; i++) {
            tree[i] = new ArrayList<>();
        }
    }
    
    // 构建树结构
    public static void buildTree(int n) {
        // 清空邻接表
        for (int i = 1; i <= n; i++) {
            tree[i].clear();
        }
        // 初始化重心和最小最大子树节点数
        centroid = 0;
        minMaxSubtreeSize = Integer.MAX_VALUE;
    }
    
    // 添加边
    public static void addEdge(int u, int v) {
        tree[u].add(v);
        tree[v].add(u);
    }
    
    // 第一次DFS：计算每个节点的子树大小
    public static int dfs1(int u, int parent) {
        subtreeSize[u] = 1;
        
        // 遍历当前节点的所有子节点
        for (int v : tree[u]) {
            // 避免回到父节点
            if (v != parent) {
                // 递归计算子树大小
                subtreeSize[u] += dfs1(v, u);
            }
        }
        
        return subtreeSize[u];
    }
    
    // 第二次DFS：找到树的重心
    public static void dfs2(int u, int parent, int totalNodes) {
        // 计算删除节点u后，剩余的最大子树节点数
        int maxSubtreeSize = 0;
        
        // 遍历当前节点的所有子节点
        for (int v : tree[u]) {
            // 避免回到父节点
            if (v != parent) {
                // 更新最大子树节点数
                maxSubtreeSize = Math.max(maxSubtreeSize, subtreeSize[v]);
            }
        }
        
        // 计算父节点方向的子树大小（即除了当前子树外的其他节点数）
        int parentSubtreeSize = totalNodes - subtreeSize[u];
        maxSubtreeSize = Math.max(maxSubtreeSize, parentSubtreeSize);
        
        // 更新重心
        if (maxSubtreeSize < minMaxSubtreeSize) {
            minMaxSubtreeSize = maxSubtreeSize;
            centroid = u;
        }
        
        // 递归处理子节点
        for (int v : tree[u]) {
            // 避免回到父节点
            if (v != parent) {
                dfs2(v, u, totalNodes);
            }
        }
    }
    
    // 计算树的重心
    public static int findCentroid(int n) {
        // 第一次DFS计算子树大小
        dfs1(1, -1);
        
        // 第二次DFS找到重心
        dfs2(1, -1, n);
        
        return centroid;
    }
    
    // 主函数 - 用于测试
    public static void main(String[] args) throws IOException {
        // 注意：提交时请把类名改成"Main"
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int n = (int) in.nval;
            buildTree(n);
            
            // 读取边
            for (int i = 1, u, v; i < n; i++) {
                in.nextToken();
                u = (int) in.nval;
                in.nextToken();
                v = (int) in.nval;
                addEdge(u, v);
            }
            
            // 计算树的重心
            int result = findCentroid(n);
            out.println(result + " " + minMaxSubtreeSize);
            out.flush();
        }
        
        out.close();
        br.close();
    }
}