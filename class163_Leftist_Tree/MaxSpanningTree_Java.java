package class155;

import java.util.*;

/**
 * 牛客 NC15093 最大生成树
 * 题目链接：https://ac.nowcoder.com/acm/problem/15093
 * 
 * 题目描述：
 * 给定一个无向图，要求找到一棵生成树，使得这棵生成树的边权之和最大。
 * 
 * 解题思路：
 * 使用Kruskal算法的变种，通过左偏树来维护并查集结构，实现按秩合并优化。
 * 与传统的Kruskal算法类似，但选择边的顺序是从大到小，以获得最大生成树。
 * 
 * 算法步骤：
 * 1. 将所有边按权重从大到小排序
 * 2. 初始化并查集结构（使用左偏树实现）
 * 3. 遍历排序后的边，如果边的两个端点不在同一集合中，则将该边加入生成树
 * 4. 重复步骤3直到生成树包含V-1条边
 * 
 * 时间复杂度：O(E log V)，其中E是边数，V是顶点数
 * 空间复杂度：O(V + E)
 * 
 * 相关题目：
 * - Java实现：MaxSpanningTree_Java.java
 * - Python实现：MaxSpanningTree_Python.py
 * - C++实现：MaxSpanningTree_Cpp.cpp
 */
public class MaxSpanningTree_Java {
    
    // 边类
    static class Edge {
        int from;   // 起始顶点
        int to;     // 终止顶点
        int weight; // 权重
        
        /**
         * 构造函数
         * @param from 起始顶点
         * @param to 终止顶点
         * @param weight 边的权重
         */
        public Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }
    
    // 左偏树节点类（用于并查集的按秩合并）
    static class LeftistTreeNode {
        int parent; // 父节点（用于并查集）
        int size;   // 子树大小（用于按秩合并）
        int value;  // 节点值（这里存储顶点编号）
        int dist;   // 距离（空路径长度）
        LeftistTreeNode left;
        LeftistTreeNode right;
        
        /**
         * 构造函数
         * @param value 节点值（顶点编号）
         */
        public LeftistTreeNode(int value) {
            this.parent = value; // 初始时父节点是自己
            this.size = 1;       // 初始大小为1
            this.value = value;
            this.dist = 0;
            this.left = null;
            this.right = null;
        }
    }
    
    /**
     * 合并两个左偏树
     * @param a 第一棵左偏树的根节点
     * @param b 第二棵左偏树的根节点
     * @return 合并后的左偏树根节点
     */
    private static LeftistTreeNode merge(LeftistTreeNode a, LeftistTreeNode b) {
        // 处理空树情况
        if (a == null) return b;
        if (b == null) return a;
        
        // 这里不关心具体的顺序，因为我们只是用左偏树来维护并查集
        a.right = merge(a.right, b);
        
        // 维护左偏性质：左子树的距离应大于等于右子树的距离
        if (a.left == null || (a.right != null && a.left.dist < a.right.dist)) {
            LeftistTreeNode temp = a.left;
            a.left = a.right;
            a.right = temp;
        }
        
        // 更新距离：叶子节点距离为0，非叶子节点距离为其右子树距离+1
        a.dist = (a.right == null) ? 0 : a.right.dist + 1;
        return a;
    }
    
    /**
     * 查找根节点（带路径压缩优化）
     * @param nodes 左偏树节点数组
     * @param x 顶点编号
     * @return 顶点x所在集合的根节点
     */
    private static int find(LeftistTreeNode[] nodes, int x) {
        // 路径压缩：将查找路径上的所有节点直接连接到根节点
        if (nodes[x].parent != x) {
            nodes[x].parent = find(nodes, nodes[x].parent);
        }
        return nodes[x].parent;
    }
    
    /**
     * 合并两个集合
     * @param nodes 左偏树节点数组
     * @param x 顶点编号
     * @param y 顶点编号
     */
    private static void union(LeftistTreeNode[] nodes, int x, int y) {
        int rootX = find(nodes, x);
        int rootY = find(nodes, y);
        
        // 如果两个顶点已在同一集合中，无需合并
        if (rootX == rootY) return;
        
        // 按秩合并：将较小的树合并到较大的树上，以保持树的平衡
        if (nodes[rootX].size < nodes[rootY].size) {
            // 交换x和y，确保rootX是较大的树
            int temp = rootX;
            rootX = rootY;
            rootY = temp;
        }
        
        // 将rootY的父节点设为rootX，完成合并
        nodes[rootY].parent = rootX;
        // 更新根节点的大小
        nodes[rootX].size += nodes[rootY].size;
        // 使用左偏树合并两个集合
        nodes[rootX] = merge(nodes[rootX], nodes[rootY]);
    }
    
    /**
     * 计算最大生成树的边权和
     * @param V 顶点数
     * @param edges 边列表
     * @return 最大生成树的边权和
     */
    public static int maxSpanningTree(int V, List<Edge> edges) {
        // 初始化左偏树节点数组，索引0不使用，顶点编号从1开始
        LeftistTreeNode[] nodes = new LeftistTreeNode[V + 1];
        for (int i = 1; i <= V; i++) {
            nodes[i] = new LeftistTreeNode(i);
        }
        
        // 按边权从大到小排序，以获得最大生成树
        edges.sort((a, b) -> b.weight - a.weight);
        
        int totalWeight = 0;  // 最大生成树的总权重
        int edgeCount = 0;    // 已选择的边数
        
        // Kruskal算法：选择最大的边，避免环
        for (Edge edge : edges) {
            // 如果边的两个端点不在同一集合中，则可以安全地添加这条边
            if (find(nodes, edge.from) != find(nodes, edge.to)) {
                union(nodes, edge.from, edge.to);
                totalWeight += edge.weight;
                edgeCount++;
                
                // 生成树有V-1条边，达到这个数量就停止
                if (edgeCount == V - 1) {
                    break;
                }
            }
        }
        
        // 检查是否形成了生成树（所有顶点都在同一集合中）
        // 如果是森林（多个连通分量），则无法形成生成树
        // 根据题目描述，应该保证图是连通的
        return totalWeight;
    }
    
    /**
     * 主函数，读取输入并输出结果
     * 输入格式：
     * 第一行包含两个整数V和E，分别表示顶点数和边数
     * 接下来E行，每行包含三个整数from、to、weight，表示一条边
     * 输出格式：
     * 输出最大生成树的边权和
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int V = scanner.nextInt();  // 顶点数
        int E = scanner.nextInt();  // 边数
        
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < E; i++) {
            int from = scanner.nextInt();    // 起始顶点
            int to = scanner.nextInt();      // 终止顶点
            int weight = scanner.nextInt();  // 边的权重
            edges.add(new Edge(from, to, weight));
        }
        
        int result = maxSpanningTree(V, edges);
        System.out.println(result);
        
        scanner.close();
    }
}