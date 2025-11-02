package class114;

/**
 * LeetCode 699. Falling Squares
 * 
 * 题目链接: https://leetcode.cn/problems/falling-squares/
 * 
 * 题目描述:
 * 在二维平面上的 x 轴上，放置着一些方块。给你一个二维整数数组 positions，
 * 其中 positions[i] = [lefti, sideLengthi] 表示：第 i 个方块边长为 sideLengthi，
 * 其左侧边与 x 轴上坐标点 lefti 对齐。每个方块都从一个比目前所有的落地方块更高的高度掉落而下。
 * 方块沿 y 轴负方向下落，直到着陆到另一个正方形的顶边或者是 x 轴上。一旦着陆，它就会固定在原地，无法移动。
 * 在每个方块掉落后，你必须记录目前所有已经落稳的方块堆叠的最高高度。返回一个整数数组 ans，
 * 其中 ans[i] 表示在第 i 块方块掉落后堆叠的最高高度。
 * 
 * 解题思路:
 * 使用动态开点线段树维护区间最大值。对于每个掉落的方块：
 * 1. 查询其底部区间的最大高度
 * 2. 计算方块顶部的高度（底部最大高度 + 方块边长）
 * 3. 更新区间为方块顶部高度
 * 4. 记录当前全局最大高度
 * 
 * 关键技术:
 * 1. 动态开点线段树：处理大数据范围
 * 2. 区间更新：将区间更新为固定值
 * 3. 区间查询：查询区间最大值
 * 
 * 时间复杂度分析:
 * 1. 建树：O(1) - 按需创建
 * 2. 区间更新：O(log n)
 * 3. 区间查询：O(log n)
 * 4. 总体复杂度：O(n log n)
 * 5. 空间复杂度：O(n log n)
 * 
 * 是否最优解：是
 * 动态开点线段树是处理此类区间操作问题的最优解法
 * 
 * 工程化考量:
 * 1. 动态内存分配：按需创建节点
 * 2. 懒惰标记优化：延迟更新子区间
 * 3. 边界处理：处理节点创建和查询边界情况
 */

import java.util.*;

public class Code14_FallingSquares {
    
    /**
     * 线段树节点类
     */
    static class Node {
        int max;      // 当前区间的最大高度
        int lazy;     // 懒惰标记，表示待下传的固定值
        Node left;    // 左子节点
        Node right;   // 右子节点
    }
    
    private Node root;  // 线段树根节点
    private final int MAX_RANGE = 1000000000;  // 最大坐标范围
    
    /**
     * 构造函数
     */
    public Code14_FallingSquares() {
        root = new Node();
    }
    
    /**
     * 处理掉落的方块，返回每次掉落后的最大高度列表
     * 
     * @param positions 方块的位置列表，每个元素为 [left, side_length]
     * @return 每次掉落后的最大高度列表
     */
    public List<Integer> fallingSquares(int[][] positions) {
        List<Integer> result = new ArrayList<>();
        int maxHeight = 0;
        
        for (int[] pos : positions) {
            int left = pos[0];
            int sideLength = pos[1];
            int right = left + sideLength - 1;
            
            // 查询当前区间的最大高度
            int currentMax = query(root, 0, MAX_RANGE, left, right);
            
            // 计算新方块的顶部高度
            int newHeight = currentMax + sideLength;
            
            // 更新区间高度
            update(root, 0, MAX_RANGE, left, right, newHeight);
            
            // 更新全局最大高度
            maxHeight = Math.max(maxHeight, newHeight);
            result.add(maxHeight);
        }
        
        return result;
    }
    
    /**
     * 区间更新：将 [L, R] 区间内的所有值更新为 val
     * 
     * @param node  当前节点
     * @param start 当前区间左端点
     * @param end   当前区间右端点
     * @param L     更新区间左端点
     * @param R     更新区间右端点
     * @param val   更新的值
     */
    private void update(Node node, int start, int end, int L, int R, int val) {
        // 如果当前区间完全包含在目标区间内
        if (L <= start && end <= R) {
            node.max = val;
            node.lazy = val;
            return;
        }
        
        // 下传懒惰标记
        pushDown(node, start, end);
        
        int mid = start + (end - start) / 2;
        // 更新左子区间
        if (L <= mid) {
            if (node.left == null) node.left = new Node();
            update(node.left, start, mid, L, R, val);
        }
        // 更新右子区间
        if (R > mid) {
            if (node.right == null) node.right = new Node();
            update(node.right, mid + 1, end, L, R, val);
        }
        
        // 上传信息
        node.max = Math.max(
            (node.left != null ? node.left.max : 0),
            (node.right != null ? node.right.max : 0)
        );
    }
    
    /**
     * 查询区间 [L, R] 内的最大值
     * 
     * @param node  当前节点
     * @param start 当前区间左端点
     * @param end   当前区间右端点
     * @param L     查询区间左端点
     * @param R     查询区间右端点
     * @return 区间最大值
     */
    private int query(Node node, int start, int end, int L, int R) {
        // 如果当前节点为空，返回0
        if (node == null) return 0;
        
        // 如果当前区间完全包含在目标区间内
        if (L <= start && end <= R) {
            return node.max;
        }
        
        // 下传懒惰标记
        pushDown(node, start, end);
        
        int mid = start + (end - start) / 2;
        int maxVal = 0;
        
        // 查询左子区间
        if (L <= mid) {
            maxVal = Math.max(maxVal, query(node.left, start, mid, L, R));
        }
        // 查询右子区间
        if (R > mid) {
            maxVal = Math.max(maxVal, query(node.right, mid + 1, end, L, R));
        }
        
        return maxVal;
    }
    
    /**
     * 下传懒惰标记
     * 
     * @param node  当前节点
     * @param start 当前区间左端点
     * @param end   当前区间右端点
     */
    private void pushDown(Node node, int start, int end) {
        if (node.lazy != 0) {
            // 为左子节点创建并传递标记
            if (node.left == null) node.left = new Node();
            node.left.max = node.lazy;
            node.left.lazy = node.lazy;
            
            // 为右子节点创建并传递标记
            if (node.right == null) node.right = new Node();
            node.right.max = node.lazy;
            node.right.lazy = node.lazy;
            
            // 清除当前节点的懒惰标记
            node.lazy = 0;
        }
    }
    
    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        Code14_FallingSquares solution = new Code14_FallingSquares();
        
        // 测试用例1
        int[][] positions1 = {{1, 2}, {2, 3}, {6, 1}};
        System.out.println("测试用例1结果: " + solution.fallingSquares(positions1)); // 输出: [2, 5, 5]
        
        // 测试用例2
        int[][] positions2 = {{100, 100}, {200, 100}};
        System.out.println("测试用例2结果: " + solution.fallingSquares(positions2)); // 输出: [100, 100]
    }
}