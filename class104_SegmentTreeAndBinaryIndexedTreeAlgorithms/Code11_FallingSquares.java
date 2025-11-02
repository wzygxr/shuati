package class131;

import java.util.*;

/** 
 * LeetCode 699. Falling Squares (掉落的方块)
 * 题目链接: https://leetcode.cn/problems/falling-squares/
 * 
 * 题目描述: 
 * 在无限长的数轴（坐标轴）上，我们放置一些方块。
 * 第i个方块的边长为 squares[i] = [left, sideLength]，其中 left 表示该方块最左边的点，sideLength 表示边长。
 * 每个方块从更高处下落，直到着陆在数轴上。
 * 方块着陆后，它会与之前放置的方块重叠。
 * 我们想记录每个方块掉落后的最高高度。
 * 
 * 解题思路:
 * 使用线段树 + 离散化 + 懒惰传播实现
 * 1. 离散化所有坐标点
 * 2. 使用线段树维护区间最大值
 * 3. 对每个方块，查询其区间最大值，然后更新区间值
 * 
 * 时间复杂度分析:
 * - 离散化: O(n log n)
 * - 每次操作: O(log n)
 * - 总时间复杂度: O(n log n)
 * 空间复杂度: O(n)
 */
public class Code11_FallingSquares {
    
    /** 
     * 线段树节点
     * 每个节点表示一个区间[left, right]，并维护该区间的最大高度
     */
    static class Node {
        int left, right;      // 节点表示的区间范围
        int max;              // 区间内的最大高度
        int lazy;             // 懒惰标记，表示要更新的值
        boolean updated;      // 是否有更新标记
        
        /** 
         * 构造函数
         * 
         * @param l 区间左边界
         * @param r 区间右边界
         */
        Node(int l, int r) {
            left = l;
            right = r;
            max = 0;
            lazy = 0;
            updated = false;
        }
    }
    
    private Map<Integer, Integer> indexMap; // 离散化坐标到索引的映射
    private List<Integer> coords;          // 所有坐标点
    private Node[] tree;                   // 线段树数组
    
    /** 
     * 计算每个方块掉落后的最高高度
     * 
     * @param positions 方块位置数组，每个元素为[left, sideLength]
     * @return          每个方块掉落后的最高高度列表
     */
    public List<Integer> fallingSquares(int[][] positions) {
        List<Integer> result = new ArrayList<>();
        
        // 收集所有坐标点并离散化
        coords = new ArrayList<>();
        for (int[] pos : positions) {
            // 添加方块的左边界和右边界
            coords.add(pos[0]);
            coords.add(pos[0] + pos[1] - 1);
        }
        // 去重并排序
        coords = new ArrayList<>(new HashSet<>(coords)); // 去重
        Collections.sort(coords);
        
        // 建立坐标到索引的映射，用于离散化
        indexMap = new HashMap<>();
        for (int i = 0; i < coords.size(); i++) {
            indexMap.put(coords.get(i), i);
        }
        
        // 初始化线段树
        int n = coords.size();
        tree = new Node[4 * n];
        build(0, 0, n - 1);
        
        int maxHeight = 0;
        // 处理每个方块
        for (int[] pos : positions) {
            int left = pos[0];           // 方块左边界
            int size = pos[1];           // 方块边长
            int right = left + size - 1; // 方块右边界
            
            // 查询当前方块区间内的最大高度
            int currentHeight = query(0, indexMap.get(left), indexMap.get(right));
            // 新的高度等于当前区间最大高度加上方块边长
            int newHeight = currentHeight + size;
            
            // 更新方块区间内的高度
            update(0, indexMap.get(left), indexMap.get(right), newHeight);
            
            // 更新全局最大高度
            maxHeight = Math.max(maxHeight, newHeight);
            result.add(maxHeight);
        }
        
        return result;
    }
    
    /** 
     * 构建线段树
     * 
     * @param node  当前线段树节点索引
     * @param start 区间起始位置
     * @param end   区间结束位置
     */
    private void build(int node, int start, int end) {
        tree[node] = new Node(start, end);
        // 叶子节点
        if (start == end) {
            return;
        }
        // 非叶子节点，递归构建左右子树
        int mid = (start + end) / 2;
        build(2 * node + 1, start, mid);
        build(2 * node + 2, mid + 1, end);
    }
    
    /** 
     * 下传懒惰标记
     * 将当前节点的更新信息传递给子节点
     * 
     * @param node 当前线段树节点索引
     */
    private void pushDown(int node) {
        // 只有当节点有更新标记时才需要下传
        if (tree[node].updated) {
            int leftChild = 2 * node + 1;
            int rightChild = 2 * node + 2;
            
            // 更新子节点的值和懒惰标记
            tree[leftChild].max = tree[node].lazy;
            tree[rightChild].max = tree[node].lazy;
            
            tree[leftChild].lazy = tree[node].lazy;
            tree[rightChild].lazy = tree[node].lazy;
            
            tree[leftChild].updated = true;
            tree[rightChild].updated = true;
            
            // 清除当前节点的更新标记
            tree[node].updated = false;
            tree[node].lazy = 0;
        }
    }
    
    /** 
     * 更新区间值
     * 将区间[start, end]内的所有位置的高度更新为value
     * 
     * @param node  当前线段树节点索引
     * @param start 更新区间起始位置
     * @param end   更新区间结束位置
     * @param value 要更新的值
     */
    private void update(int node, int start, int end, int value) {
        // 当前节点区间与更新区间无交集
        if (start > tree[node].right || end < tree[node].left) {
            return;
        }
        
        // 当前节点区间完全包含在更新区间内
        if (start <= tree[node].left && tree[node].right <= end) {
            tree[node].max = value;
            tree[node].lazy = value;
            tree[node].updated = true;
            return;
        }
        
        // 部分重叠，需要下传懒惰标记并递归处理
        pushDown(node);
        int mid = (tree[node].left + tree[node].right) / 2;
        // 递归更新左子树
        if (start <= mid) {
            update(2 * node + 1, start, end, value);
        }
        // 递归更新右子树
        if (end > mid) {
            update(2 * node + 2, start, end, value);
        }
        
        // 更新当前节点的最大值为左右子树最大值的最大值
        tree[node].max = Math.max(tree[2 * node + 1].max, tree[2 * node + 2].max);
    }
    
    /** 
     * 查询区间最大值
     * 查询区间[start, end]内的最大高度
     * 
     * @param node  当前线段树节点索引
     * @param start 查询区间起始位置
     * @param end   查询区间结束位置
     * @return      区间内的最大高度
     */
    private int query(int node, int start, int end) {
        // 当前节点区间与查询区间无交集
        if (start > tree[node].right || end < tree[node].left) {
            return 0;
        }
        
        // 当前节点区间完全包含在查询区间内
        if (start <= tree[node].left && tree[node].right <= end) {
            return tree[node].max;
        }
        
        // 部分重叠，需要下传懒惰标记并递归查询
        pushDown(node);
        int mid = (tree[node].left + tree[node].right) / 2;
        int leftMax = 0, rightMax = 0;
        // 递归查询左子树
        if (start <= mid) {
            leftMax = query(2 * node + 1, start, end);
        }
        // 递归查询右子树
        if (end > mid) {
            rightMax = query(2 * node + 2, start, end);
        }
        
        // 返回左右子树查询结果的最大值
        return Math.max(leftMax, rightMax);
    }
    
    /** 
     * 测试方法
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        Code11_FallingSquares solution = new Code11_FallingSquares();
        
        // 测试用例1
        int[][] positions1 = {{1, 2}, {2, 3}, {6, 1}};
        System.out.println("Input: " + Arrays.deepToString(positions1));
        System.out.println("Output: " + solution.fallingSquares(positions1)); // 应该输出[2, 5, 5]
        
        // 测试用例2
        int[][] positions2 = {{100, 100}, {200, 100}};
        System.out.println("Input: " + Arrays.deepToString(positions2));
        System.out.println("Output: " + solution.fallingSquares(positions2)); // 应该输出[100, 100]
    }
}