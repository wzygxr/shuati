package class162;

// LeetCode 834. 树中距离之和 - Java实现
// 树形DP经典题目

import java.util.*;
import java.io.*;

public class LC834_SumOfDistancesInTree {
    /**
     * 计算树中每个节点到其他所有节点的距离之和
     * 
     * 解题思路:
     * 1. 树形DP，通过两次DFS遍历来解决
     * 2. 第一次DFS: 计算每个节点子树的节点数和子树内距离之和
     * 3. 第二次DFS: 利用父节点的结果推导子节点的结果
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     */
    
    private List<List<Integer>> graph;  // 邻接表表示的树
    private int[] count;                // count[i]表示以节点i为根的子树节点数
    private int[] res;                  // res[i]表示节点i到其他所有节点的距离之和
    
    public int[] sumOfDistancesInTree(int n, int[][] edges) {
        // 初始化
        graph = new ArrayList<>();
        count = new int[n];
        res = new int[n];
        Arrays.fill(count, 1);  // 每个节点本身算一个
        
        // 构建邻接表
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 添加边（无向图）
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }
        
        // 第一次DFS: 计算每个节点子树的节点数和子树内距离之和
        dfs1(0, -1);
        
        // 第二次DFS: 利用父节点结果推导子节点结果
        dfs2(0, -1);
        
        return res;
    }
    
    /**
     * 第一次DFS: 计算每个节点子树的节点数和子树内距离之和
     * @param node 当前节点
     * @param parent 父节点
     */
    private void dfs1(int node, int parent) {
        // 遍历当前节点的所有子节点
        for (int child : graph.get(node)) {
            // 避免回到父节点
            if (child != parent) {
                dfs1(child, node);
                // 累加子树节点数
                count[node] += count[child];
                // 累加子树内距离之和
                // 子树内每个节点到child的距离都增加了1，所以总距离增加count[child]
                res[node] += res[child] + count[child];
            }
        }
    }
    
    /**
     * 第二次DFS: 利用父节点结果推导子节点结果
     * @param node 当前节点
     * @param parent 父节点
     */
    private void dfs2(int node, int parent) {
        // 遍历当前节点的所有子节点
        for (int child : graph.get(node)) {
            // 避免回到父节点
            if (child != parent) {
                // 当从父节点node换根到子节点child时：
                // 1. child子树中的所有节点到child的距离比到node的距离少1，总共减少count[child]
                // 2. 除child子树外的其他节点到child的距离比到node的距离多1，总共增加(n - count[child])
                res[child] = res[node] - count[child] + (count.length - count[child]);
                dfs2(child, node);
            }
        }
    }
    
    // 从文件读取输入并运行程序
    public static void main(String[] args) throws IOException {
        // 使用相对路径读取输入文件
        String filePath = "new_test_input.txt";
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        
        int n = Integer.parseInt(br.readLine().trim());
        int[][] edges = new int[n - 1][2];
        
        for (int i = 0; i < n - 1; i++) {
            String[] parts = br.readLine().trim().split(" ");
            edges[i][0] = Integer.parseInt(parts[0]);
            edges[i][1] = Integer.parseInt(parts[1]);
        }
        
        br.close();
        
        // 运行算法
        LC834_SumOfDistancesInTree solution = new LC834_SumOfDistancesInTree();
        int[] result = solution.sumOfDistancesInTree(n, edges);
        
        // 输出结果
        System.out.println("结果: " + Arrays.toString(result));
    }
}