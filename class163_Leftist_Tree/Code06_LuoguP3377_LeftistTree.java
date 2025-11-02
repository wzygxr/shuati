package class155;

import java.io.*;
import java.util.*;

/**
 * 洛谷P3377 【模板】左偏树/可并堆
 * 题目链接: https://www.luogu.com.cn/problem/P3377
 * 
 * 题目描述：
 * 如题，一开始有n个小根堆，每个堆包含且仅包含一个数。接下来需要支持两种操作：
 * 1. 1 x y：将第x个数和第y个数所在的小根堆合并（若第x或第y个数已经被删除或第x和第y个数在同一个堆内，则无视此操作）
 * 2. 2 x：输出第x个数所在的堆最小数，并将这个最小数删除（若有多个最小数，优先删除先输入的；若第x个数已经被删除，则输出-1并无视删除操作）
 * 
 * 解题思路：
 * 使用左偏树实现可并堆，支持快速合并操作和删除最小值操作。
 * 1. 使用左偏树维护每个小根堆
 * 2. 使用并查集维护每个节点所属的堆
 * 3. 对于操作1：合并两个堆
 * 4. 对于操作2：删除堆顶元素
 * 
 * 左偏树核心性质：
 * 1. 堆性质：父节点的值小于等于子节点的值
 * 2. 左偏性质：左子节点的距离大于等于右子节点的距离
 * 3. 距离定义：从节点到最近的外节点（空节点）的边数
 * 
 * 算法优势：
 * 1. 合并操作时间复杂度为O(log n)
 * 2. 插入和删除操作时间复杂度为O(log n)
 * 3. 支持高效处理动态集合合并问题
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
 * 
 * 相关题目：
 * - Java实现：Code06_LuoguP3377_LeftistTree.java
 * - Python实现：Code06_LuoguP3377_LeftistTree.py
 * - C++实现：Code06_LuoguP3377_LeftistTree.cpp
 */
public class Code06_LuoguP3377_LeftistTree {
    
    // 左偏树节点定义
    static class Node {
        int val;        // 节点权值
        int dist;       // 节点距离（到最近外节点的距离）
        int index;      // 节点索引
        Node left;      // 左子节点
        Node right;     // 右子节点
        int time;       // 输入时间，用于处理相同值时的优先级
        
        /**
         * 构造函数
         * @param val 节点权值
         * @param index 节点索引
         * @param time 输入时间
         */
        Node(int val, int index, int time) {
            this.val = val;
            this.index = index;
            this.time = time;
            this.dist = 0;
            this.left = null;
            this.right = null;
        }
    }
    
    static int MAXN = 100010;
    static Node[] nodes = new Node[MAXN];  // 节点数组
    static int[] parent = new int[MAXN];   // 并查集父节点数组
    static boolean[] deleted = new boolean[MAXN]; // 标记节点是否被删除
    static int nodeCount = 0;              // 节点计数器
    static int currentTime = 0;            // 时间戳
    
    /**
     * 初始化节点
     * @param val 节点权值
     * @return 节点索引
     */
    static int initNode(int val) {
        nodes[++nodeCount] = new Node(val, nodeCount, ++currentTime);
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
        // 如果值相同，优先选择输入时间早的
        if (nodes[a].val > nodes[b].val || 
            (nodes[a].val == nodes[b].val && nodes[a].time > nodes[b].time)) {
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
     * 输入格式：
     * 第一行包含两个整数n和m，分别表示初始节点数和操作数
     * 第二行包含n个整数，表示每个节点的初始值
     * 接下来m行，每行包含一个操作：
     *   1 x y：合并x和y所在的堆
     *   2 x：删除x所在堆的最小元素并输出
     * 输出格式：
     * 对于每个操作2，输出删除的最小元素，如果x已被删除则输出-1
     */
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取输入
        String[] line = reader.readLine().trim().split("\\s+");
        int n = Integer.parseInt(line[0]);  // 节点数量
        int m = Integer.parseInt(line[1]);  // 操作数量
        
        // 初始化
        nodeCount = 0;
        int[] roots = new int[n + 1];  // 每个堆对应的左偏树根节点
        
        // 读取每个节点的初始值
        String[] values = reader.readLine().trim().split("\\s+");
        for (int i = 1; i <= n; i++) {
            int val = Integer.parseInt(values[i - 1]);
            int nodeIndex = initNode(val);
            roots[i] = nodeIndex;
            parent[i] = i;  // 初始化并查集
            deleted[i] = false;
        }
        
        // 处理每次操作
        for (int i = 0; i < m; i++) {
            line = reader.readLine().trim().split("\\s+");
            int op = Integer.parseInt(line[0]);
            
            if (op == 1) {
                // 合并操作
                int x = Integer.parseInt(line[1]);
                int y = Integer.parseInt(line[2]);
                
                // 检查节点是否被删除
                if (deleted[x] || deleted[y]) {
                    continue;
                }
                
                // 查找两个节点所属的堆
                int rootX = find(x);
                int rootY = find(y);
                
                // 如果已经在同一个堆中，无需合并
                if (rootX == rootY) {
                    continue;
                }
                
                // 合并两个堆
                int mergedRoot = merge(roots[rootX], roots[rootY]);
                
                // 更新并查集和根节点信息
                if (mergedRoot > 0) {
                    parent[rootX] = rootY;
                    roots[rootY] = mergedRoot;
                }
            } else if (op == 2) {
                // 删除最小值操作
                int x = Integer.parseInt(line[1]);
                
                // 检查节点是否被删除
                if (deleted[x]) {
                    writer.println(-1);
                    continue;
                }
                
                // 查找节点所属的堆
                int rootX = find(x);
                
                // 输出堆顶元素
                writer.println(nodes[roots[rootX]].val);
                
                // 标记堆顶元素为已删除
                deleted[nodes[roots[rootX]].index] = true;
                
                // 删除堆顶元素
                int newRoot = pop(roots[rootX]);
                roots[rootX] = newRoot;
            }
        }
        
        writer.flush();
        writer.close();
        reader.close();
    }
}
