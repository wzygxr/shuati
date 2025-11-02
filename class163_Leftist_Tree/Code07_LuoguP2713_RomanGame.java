package class155;

import java.io.*;
import java.util.*;

/**
 * 洛谷P2713 罗马游戏
 * 
 * 题目描述：
 * 罗马皇帝很喜欢玩杀人游戏。他的军队里面有n个士兵，每个士兵都是一个独立的团。
 * 最近举行了一次平面几何测试，每个士兵都得到了一个分数。
 * 皇帝很喜欢平面几何，他对那些得分很低的士兵嗤之以鼻。
 * 
 * 他决定玩这样一个游戏。它可以发两种命令：
 * - M i j 把i所在的团和j所在的团合并成一个团。如果i,j有一个士兵是死人，那么就忽略该命令。
 * - K i 把i所在的团里面得分最低的士兵杀死。如果i这个士兵已经死了，这条命令就忽略。
 * 
 * 皇帝希望他每发布一条K i命令，下面的将军就把被杀的士兵的分数报上来
 * （如果这条命令被忽略，那么就报0分）。
 * 
 * 解题思路：
 * 使用左偏树维护每个团的最小值（小根堆），配合并查集维护团的连通性。
 * 1. 使用左偏树维护每个团的士兵分数（小根堆）
 * 2. 使用并查集维护士兵所属的团
 * 3. 对于M操作：合并两个团
 * 4. 对于K操作：删除团中最小分数的士兵
 * 
 * 时间复杂度分析：
 * - 左偏树合并: O(log n)
 * - 左偏树插入: O(log n)
 * - 左偏树删除: O(log n)
 * - 并查集操作: 近似 O(1)
 * - 总体复杂度: O(M * log N)
 * 
 * 空间复杂度分析:
 * - 左偏树节点存储: O(N)
 * - 并查集存储: O(N)
 * - 总体空间复杂度: O(N)
 */
public class Code07_LuoguP2713_RomanGame {
    
    // 左偏树节点定义
    static class Node {
        int val;        // 节点权值（士兵分数）
        int dist;       // 节点距离（到最近外节点的距离）
        int index;      // 节点索引
        Node left;      // 左子节点
        Node right;     // 右子节点
        
        Node(int val, int index) {
            this.val = val;
            this.index = index;
            this.dist = 0;
            this.left = null;
            this.right = null;
        }
    }
    
    static int MAXN = 1000010;
    static Node[] nodes = new Node[MAXN];  // 节点数组
    static int[] parent = new int[MAXN];   // 并查集父节点数组
    static boolean[] killed = new boolean[MAXN]; // 标记士兵是否被杀死
    static int nodeCount = 0;              // 节点计数器
    
    /**
     * 初始化节点
     * @param val 节点权值
     * @return 节点索引
     */
    static int initNode(int val) {
        nodes[++nodeCount] = new Node(val, nodeCount);
        return nodeCount;
    }
    
    /**
     * 查找并查集根节点（带路径压缩）
     * @param x 节点索引
     * @return 根节点索引
     */
    static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);  // 路径压缩
        }
        return parent[x];
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
        
        // 确保a节点权值 <= b节点权值（小根堆）
        if (nodes[a].val > nodes[b].val) {
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
        
        int leftIndex = (nodes[root].left == null) ? 0 : nodes[root].left.index;
        int rightIndex = (nodes[root].right == null) ? 0 : nodes[root].right.index;
        
        return merge(leftIndex, rightIndex);
    }
    
    /**
     * 主函数
     * @param args 命令行参数
     */
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取士兵数量
        int n = Integer.parseInt(reader.readLine().trim());
        
        // 初始化
        nodeCount = 0;
        int[] roots = new int[n + 1];  // 每个团对应的左偏树根节点
        
        // 读取每个士兵的分数
        String[] scores = reader.readLine().trim().split("\\s+");
        for (int i = 1; i <= n; i++) {
            int score = Integer.parseInt(scores[i - 1]);
            int nodeIndex = initNode(score);
            roots[i] = nodeIndex;
            parent[i] = i;  // 初始化并查集
            killed[i] = false;
        }
        
        // 读取操作数量
        int m = Integer.parseInt(reader.readLine().trim());
        
        // 处理每次操作
        for (int i = 0; i < m; i++) {
            String[] operation = reader.readLine().trim().split("\\s+");
            String op = operation[0];
            
            if (op.equals("M")) {
                // 合并操作
                int x = Integer.parseInt(operation[1]);
                int y = Integer.parseInt(operation[2]);
                
                // 检查士兵是否被杀死
                if (killed[x] || killed[y]) {
                    continue;
                }
                
                // 查找两个士兵所属的团
                int rootX = find(x);
                int rootY = find(y);
                
                // 如果已经在同一个团中，无需合并
                if (rootX == rootY) {
                    continue;
                }
                
                // 合并两个团
                int mergedRoot = merge(roots[rootX], roots[rootY]);
                
                // 更新并查集和根节点信息
                if (mergedRoot > 0) {
                    parent[rootX] = rootY;
                    roots[rootY] = mergedRoot;
                }
            } else if (op.equals("K")) {
                // 杀死最小分数士兵操作
                int x = Integer.parseInt(operation[1]);
                
                // 检查士兵是否被杀死
                if (killed[x]) {
                    writer.println(0);
                    continue;
                }
                
                // 查找士兵所属的团
                int rootX = find(x);
                
                // 输出团中最小分数
                writer.println(nodes[roots[rootX]].val);
                
                // 标记最小分数士兵为已杀死
                killed[nodes[roots[rootX]].index] = true;
                
                // 删除团中最小分数士兵
                int newRoot = pop(roots[rootX]);
                roots[rootX] = newRoot;
            }
        }
        
        writer.flush();
        writer.close();
        reader.close();
    }
}
