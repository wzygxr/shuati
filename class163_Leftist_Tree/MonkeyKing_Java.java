package class155;

import java.io.*;
import java.util.*;

/**
 * HDU 1512 Monkey King - 左偏树解法
 * 
 * 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1512
 * 
 * 问题描述：
 * 有N只猴子，每只猴子有一个武力值。初始时，所有猴子互不相识。
 * 当两只互不相识的猴子发生冲突时，他们会各自邀请自己朋友圈中武力值最高的猴子进行决斗。
 * 决斗后，两只参战猴子的武力值减半（向下取整），并且两个朋友圈合并为一个。
 * 给定M次冲突，每次查询输出冲突后朋友圈中的最大武力值，如果两只猴子已经相识则输出-1。
 * 
 * 解题思路：
 * 1. 使用左偏树维护每个朋友圈的最大值（大根堆）
 * 2. 使用并查集维护朋友圈的连通性
 * 3. 对于每次冲突：
 *    - 检查两只猴子是否已经相识（并查集）
 *    - 如果不相识，找出各自朋友圈的最大值猴子
 *    - 将这两只猴子的武力值减半
 *    - 从各自左偏树中删除这两个节点
 *    - 将减半后的节点重新插入对应左偏树
 *    - 合并两个左偏树
 *    - 输出合并后左偏树的根节点值（最大值）
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
public class MonkeyKing_Java {
    
    // 左偏树节点定义
    static class Node {
        int val;        // 节点权值（猴子武力值）
        int dist;       // 节点距离（到最近外节点的距离）
        Node left;      // 左子节点
        Node right;     // 右子节点
        int index;      // 节点索引
        
        Node(int val, int index) {
            this.val = val;
            this.index = index;
            this.dist = 0;
            this.left = null;
            this.right = null;
        }
    }
    
    static int MAXN = 100010;
    static Node[] nodes = new Node[MAXN];  // 节点数组
    static int[] parent = new int[MAXN];   // 并查集父节点数组
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
     * 合并两个并查集
     * @param x 第一个节点索引
     * @param y 第二个节点索引
     */
    static void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX != rootY) {
            parent[rootX] = rootY;
        }
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
        
        // 确保a节点权值 >= b节点权值（大根堆）
        if (nodes[a].val < nodes[b].val) {
            int temp = a;
            a = b;
            b = temp;
        }
        
        // 递归合并右子树和b树
        nodes[a].right = nodes[a].right == null ? null : nodes[a].right;
        int rightIndex = nodes[a].right == null ? 0 : nodes[a].right.index;
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
        String line;
        
        // 多组测试数据
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            int n = Integer.parseInt(line.trim());  // 猴子数量
            
            // 初始化
            nodeCount = 0;
            int[] roots = new int[n + 1];  // 每个朋友圈对应的左偏树根节点
            
            // 读取每只猴子的武力值
            String[] powers = reader.readLine().trim().split("\\s+");
            for (int i = 1; i <= n; i++) {
                int power = Integer.parseInt(powers[i - 1]);
                int nodeIndex = initNode(power);
                roots[i] = nodeIndex;
                parent[i] = i;  // 初始化并查集
            }
            
            int m = Integer.parseInt(reader.readLine().trim());  // 冲突次数
            
            // 处理每次冲突
            for (int i = 0; i < m; i++) {
                String[] conflict = reader.readLine().trim().split("\\s+");
                int x = Integer.parseInt(conflict[0]);
                int y = Integer.parseInt(conflict[1]);
                
                // 查找两只猴子所属的朋友圈
                int rootX = find(x);
                int rootY = find(y);
                
                // 如果两只猴子已经相识
                if (rootX == rootY) {
                    System.out.println(-1);
                    continue;
                }
                
                // 获取两个朋友圈的最大武力值猴子
                int maxX = roots[rootX];
                int maxY = roots[rootY];
                
                // 将两只猴子的武力值减半
                nodes[maxX].val /= 2;
                nodes[maxY].val /= 2;
                
                // 从各自左偏树中删除根节点
                int newRootX = pop(maxX);
                int newRootY = pop(maxY);
                
                // 将减半后的节点重新插入
                int newNodeX = initNode(nodes[maxX].val);
                int newNodeY = initNode(nodes[maxY].val);
                
                // 合并操作
                int mergedXY = merge(newRootX, newNodeX);
                int mergedYY = merge(newRootY, newNodeY);
                int finalMerged = merge(mergedXY, mergedYY);
                
                // 更新朋友圈根节点和并查集
                roots[rootX] = finalMerged;
                union(rootX, rootY);
                roots[find(rootX)] = finalMerged;
                
                // 输出合并后朋友圈的最大武力值
                System.out.println(nodes[finalMerged].val);
            }
        }
    }
}
