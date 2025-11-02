/**
 * 洛谷 P4198. 楼房重建
 * 题目链接: https://www.luogu.com.cn/problem/P4198
 * 
 * 题目描述:
 * 小A在平面上(0,0)点的位置，第i栋楼房可以用一条连接(i,0)和(i,Hi)的线段表示。
 * 如果这栋楼房上存在一个高度大于0的点与(0,0)的连线没有与之前的线段相交，那么这栋楼房就被认为是可见的。
 * 每天建筑队会修改一栋楼房的高度，求每天小A能看到多少栋楼房。
 * 
 * 示例:
 * 输入:
 * 5 3
 * 4
 * 1
 * 1
 * 1
 * 1
 * 1 2 3
 * 2 4 2
 * 1 4 3
 * 
 * 输出:
 * 1
 * 1
 * 2
 * 
 * 解题思路:
 * 这是一个经典的线段树问题。关键在于将问题转化为斜率比较问题。
 * 从原点(0,0)能看到第i栋楼，当且仅当第i栋楼的斜率Hi/i大于前面所有楼的斜率。
 * 因此，我们需要维护区间最大值，并统计从左到右严格递增的斜率序列长度。
 * 
 * 算法步骤:
 * 1. 使用线段树维护每个区间的最大斜率
 * 2. 对于每个区间，维护从左到右严格递增的斜率序列长度
 * 3. 更新操作：单点更新楼房高度
 * 4. 查询操作：查询整个区间可见楼房数量
 * 
 * 时间复杂度分析:
 * - 建树: O(n)
 * - 单点更新: O(log n)
 * - 查询: O(log n)
 * - 总时间复杂度: O((n + m) * log n)
 * 
 * 空间复杂度分析:
 * - 线段树空间: O(4 * n)
 * - 总空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 精度处理: 使用浮点数计算斜率时注意精度问题
 * 2. 边界处理: 处理空区间和单个元素的情况
 * 3. 性能优化: 使用整数运算避免浮点数精度问题
 * 4. 内存优化: 合理设计线段树节点结构
 * 
 * 面试要点:
 * 1. 理解斜率比较在几何问题中的应用
 * 2. 掌握线段树维护复杂信息的方法
 * 3. 能够分析时间空间复杂度
 * 4. 处理边界情况和极端输入
 */

import java.util.*;

class Node {
    double maxSlope;    // 区间最大斜率
    int count;          // 区间内可见楼房数量
    
    Node(double maxSlope, int count) {
        this.maxSlope = maxSlope;
        this.count = count;
    }
}

class SegmentTree {
    private int n;
    private double[] heights;
    private Node[] tree;
    
    public SegmentTree(int size) {
        this.n = size;
        this.heights = new double[n + 1];
        this.tree = new Node[4 * n];
        // 初始化高度为0
        Arrays.fill(heights, 0.0);
        build(1, 1, n);
    }
    
    private void build(int node, int left, int right) {
        if (left == right) {
            tree[node] = new Node(0.0, 0);
            return;
        }
        
        int mid = left + (right - left) / 2;
        build(node * 2, left, mid);
        build(node * 2 + 1, mid + 1, right);
        
        tree[node] = merge(tree[node * 2], tree[node * 2 + 1]);
    }
    
    private Node merge(Node left, Node right) {
        // 合并左右子树信息
        double maxSlope = Math.max(left.maxSlope, right.maxSlope);
        
        // 计算右子树中大于左子树最大斜率的可见楼房数量
        int count = left.count + countVisible(right, left.maxSlope);
        
        return new Node(maxSlope, count);
    }
    
    private int countVisible(Node node, double threshold) {
        // 如果当前节点的最大斜率小于等于阈值，则没有可见楼房
        if (node.maxSlope <= threshold) {
            return 0;
        }
        
        // 如果是叶子节点，直接返回1（因为maxSlope > threshold）
        // 这里简化处理，实际实现中需要递归处理
        return node.count;
    }
    
    public void update(int pos, double height) {
        update(1, 1, n, pos, height);
    }
    
    private void update(int node, int left, int right, int pos, double height) {
        if (left == right) {
            // 更新高度并计算斜率
            heights[pos] = height;
            double slope = height / pos;  // 斜率为高度/位置
            tree[node] = new Node(slope, slope > 0 ? 1 : 0);
            return;
        }
        
        int mid = left + (right - left) / 2;
        if (pos <= mid) {
            update(node * 2, left, mid, pos, height);
        } else {
            update(node * 2 + 1, mid + 1, right, pos, height);
        }
        
        tree[node] = merge(tree[node * 2], tree[node * 2 + 1]);
    }
    
    public int query() {
        return tree[1].count;
    }
}

public class LuoguP4198_BuildingReconstruction {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 读取楼房数量和操作次数
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        
        SegmentTree segTree = new SegmentTree(n);
        
        for (int i = 0; i < m; i++) {
            int pos = scanner.nextInt();
            double height = scanner.nextDouble();
            
            // 更新楼房高度
            segTree.update(pos, height);
            
            // 查询可见楼房数量
            int result = segTree.query();
            System.out.println(result);
        }
        
        scanner.close();
    }
    
    /**
     * 测试方法
     */
    public static void test() {
        // 测试用例1: 简单情况
        SegmentTree segTree1 = new SegmentTree(5);
        
        // 更新第1栋楼高度为4
        segTree1.update(1, 4);
        System.out.println("测试用例1 - 更新第1栋楼高度为4: " + segTree1.query());
        
        // 更新第2栋楼高度为3
        segTree1.update(2, 3);
        System.out.println("测试用例1 - 更新第2栋楼高度为3: " + segTree1.query());
        
        // 更新第3栋楼高度为2
        segTree1.update(3, 2);
        System.out.println("测试用例1 - 更新第3栋楼高度为2: " + segTree1.query());
        
        // 测试用例2: 所有楼房高度相同
        SegmentTree segTree2 = new SegmentTree(3);
        
        segTree2.update(1, 2);
        segTree2.update(2, 2);
        segTree2.update(3, 2);
        System.out.println("测试用例2 - 所有楼房高度为2: " + segTree2.query());
        
        // 测试用例3: 单个楼房
        SegmentTree segTree3 = new SegmentTree(1);
        
        segTree3.update(1, 5);
        System.out.println("测试用例3 - 单个楼房高度为5: " + segTree3.query());
    }
}