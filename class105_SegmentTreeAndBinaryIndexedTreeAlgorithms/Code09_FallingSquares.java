package class132;

import java.util.*;

// LeetCode 699. 掉落的方块
// 在无限长的数轴（坐标轴）上，我们根据给定的顺序放置“方块”。
// 第 i 个方块的边长是 squares[i] = [left, sideLength]，其中 left 表示该方块最左边的 x 坐标，
// sideLength 表示边长。
// 每个方块从一个比目前所有落下的方块更高的高度掉落而下，直到着陆到另一个正方形的顶边或者数轴上。
// 我们可以认为只有一个方块的底部边平行于数轴。
// 返回一个数组 ans，其中 ans[i] 表示在第 i 个方块掉落后，当前所有落下的方块堆叠的最高高度。
// 测试链接: https://leetcode.cn/problems/falling-squares/

public class Code09_FallingSquares {

    /**
     * 使用线段树解决掉落的方块问题
     * 
     * 解题思路:
     * 1. 这是一个动态区间最值查询问题
     * 2. 每个方块掉落时，需要查询其底部区间内的最大高度
     * 3. 然后更新该区间的高度为底部最大高度+方块高度
     * 4. 使用线段树支持区间最值查询和区间更新
     * 
     * 时间复杂度分析:
     * - 离散化: O(n log n)
     * - 每次查询和更新: O(log n)
     * - 总时间复杂度: O(n log n)
     * 
     * 空间复杂度分析:
     * - 线段树: O(n)
     * - 离散化数组: O(n)
     * - 总空间复杂度: O(n)
     * 
     * 工程化考量:
     * 1. 离散化处理大坐标范围
     * 2. 懒惰传播优化区间更新
     * 3. 边界条件处理
     * 4. 详细注释和变量命名
     */

    // 线段树节点
    static class SegmentTreeNode {
        int start, end;
        int maxHeight;     // 区间最大高度
        int lazy;          // 懒惰标记
        SegmentTreeNode left, right;
        
        SegmentTreeNode(int start, int end) {
            this.start = start;
            this.end = end;
            this.maxHeight = 0;
            this.lazy = 0;
        }
    }
    
    // 支持懒惰传播的线段树
    static class SegmentTree {
        private SegmentTreeNode root;
        
        SegmentTree(int size) {
            root = new SegmentTreeNode(0, size);
        }
        
        // 区间更新：将区间[updateStart, updateEnd]的高度设置为height
        public void update(int updateStart, int updateEnd, int height) {
            update(root, updateStart, updateEnd, height);
        }
        
        private void update(SegmentTreeNode node, int updateStart, int updateEnd, int height) {
            // 如果当前节点区间与更新区间无交集
            if (node.start > updateEnd || node.end < updateStart) {
                return;
            }
            
            // 如果当前节点区间完全包含在更新区间内
            if (updateStart <= node.start && node.end <= updateEnd) {
                node.maxHeight = height;
                node.lazy = height;
                return;
            }
            
            // 如果不是叶子节点，下推懒惰标记
            pushDown(node);
            
            // 递归更新子节点
            update(node.left, updateStart, updateEnd, height);
            update(node.right, updateStart, updateEnd, height);
            
            // 更新当前节点的值
            node.maxHeight = Math.max(node.left.maxHeight, node.right.maxHeight);
        }
        
        // 区间查询：查询区间[queryStart, queryEnd]的最大高度
        public int query(int queryStart, int queryEnd) {
            return query(root, queryStart, queryEnd);
        }
        
        private int query(SegmentTreeNode node, int queryStart, int queryEnd) {
            // 如果当前节点区间与查询区间无交集
            if (node.start > queryEnd || node.end < queryStart) {
                return 0;
            }
            
            // 如果当前节点区间完全包含在查询区间内
            if (queryStart <= node.start && node.end <= queryEnd) {
                return node.maxHeight;
            }
            
            // 如果不是叶子节点，下推懒惰标记
            pushDown(node);
            
            // 递归查询子节点并返回最大值
            return Math.max(query(node.left, queryStart, queryEnd), 
                           query(node.right, queryStart, queryEnd));
        }
        
        // 下推懒惰标记
        private void pushDown(SegmentTreeNode node) {
            // 如果是叶子节点，无法下推
            if (node.start == node.end) {
                return;
            }
            
            // 创建子节点（如果不存在）
            int mid = node.start + (node.end - node.start) / 2;
            if (node.left == null) {
                node.left = new SegmentTreeNode(node.start, mid);
            }
            if (node.right == null) {
                node.right = new SegmentTreeNode(mid + 1, node.end);
            }
            
            // 如果有懒惰标记，下推给子节点
            if (node.lazy > 0) {
                node.left.maxHeight = node.lazy;
                node.left.lazy = node.lazy;
                
                node.right.maxHeight = node.lazy;
                node.right.lazy = node.lazy;
                
                node.lazy = 0;
            }
        }
    }

    public static List<Integer> fallingSquares(int[][] positions) {
        List<Integer> result = new ArrayList<>();
        
        // 1. 收集所有关键坐标点并离散化
        TreeSet<Integer> coords = new TreeSet<>();
        for (int[] pos : positions) {
            int left = pos[0];
            int right = pos[0] + pos[1] - 1;  // 右边界包含在方块内
            coords.add(left);
            coords.add(right);
            coords.add(left - 1);  // 添加左边界前一个点，用于查询
            coords.add(right + 1);  // 添加右边界后一个点，用于查询
        }
        
        // 2. 建立坐标到索引的映射
        List<Integer> sortedCoords = new ArrayList<>(coords);
        Map<Integer, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < sortedCoords.size(); i++) {
            indexMap.put(sortedCoords.get(i), i);
        }
        
        // 3. 创建线段树
        SegmentTree segmentTree = new SegmentTree(sortedCoords.size());
        
        // 4. 处理每个方块
        int maxHeight = 0;
        for (int[] pos : positions) {
            int left = pos[0];
            int sideLength = pos[1];
            int right = left + sideLength - 1;
            
            // 获取离散化后的索引
            int leftIndex = indexMap.get(left);
            int rightIndex = indexMap.get(right);
            
            // 查询当前方块底部的最大高度
            int currentMaxHeight = segmentTree.query(leftIndex, rightIndex);
            
            // 计算方块堆叠后的高度
            int newHeight = currentMaxHeight + sideLength;
            
            // 更新线段树中该区间的高度
            segmentTree.update(leftIndex, rightIndex, newHeight);
            
            // 更新全局最大高度
            maxHeight = Math.max(maxHeight, newHeight);
            
            // 添加当前最大高度到结果中
            result.add(maxHeight);
        }
        
        return result;
    }
    
    // 优化版本：不需要离散化，使用动态开点线段树
    public static List<Integer> fallingSquaresOptimized(int[][] positions) {
        List<Integer> result = new ArrayList<>();
        
        // 使用TreeMap维护坐标和高度的关系
        TreeMap<Integer, Integer> heights = new TreeMap<>();
        heights.put(0, 0);  // 初始地面高度
        
        int maxHeight = 0;
        
        for (int[] pos : positions) {
            int left = pos[0];
            int side = pos[1];
            int right = left + side;
            
            // 找到覆盖区域内的最大高度
            int currentHeight = 0;
            Integer start = heights.floorKey(left);
            if (start != null) {
                currentHeight = heights.get(start);
            }
            
            // 遍历覆盖区域内的所有点，找到最大高度
            for (Map.Entry<Integer, Integer> entry : heights.subMap(left, right).entrySet()) {
                currentHeight = Math.max(currentHeight, entry.getValue());
            }
            
            // 新的高度
            int newHeight = currentHeight + side;
            
            // 更新最大高度
            maxHeight = Math.max(maxHeight, newHeight);
            result.add(maxHeight);
            
            // 更新高度映射
            // 移除被覆盖的区间
            heights.subMap(left, right).clear();
            // 添加新的区间
            heights.put(left, newHeight);
            heights.put(right, currentHeight);
        }
        
        return result;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[][] positions1 = {{1, 2}, {2, 3}, {6, 1}};
        List<Integer> result1 = fallingSquaresOptimized(positions1);
        System.out.println("Test case 1:");
        System.out.println("Input: [[1, 2], [2, 3], [6, 1]]");
        System.out.println("Output: " + result1);
        // 期望输出: [2, 5, 5]
        
        // 测试用例2
        int[][] positions2 = {{100, 100}, {200, 100}};
        List<Integer> result2 = fallingSquaresOptimized(positions2);
        System.out.println("\nTest case 2:");
        System.out.println("Input: [[100, 100], [200, 100]]");
        System.out.println("Output: " + result2);
        // 期望输出: [100, 100]
        
        // 测试用例3
        int[][] positions3 = {{1, 5}, {2, 2}, {4, 3}};
        List<Integer> result3 = fallingSquaresOptimized(positions3);
        System.out.println("\nTest case 3:");
        System.out.println("Input: [[1, 5], [2, 2], [4, 3]]");
        System.out.println("Output: " + result3);
        // 期望输出: [5, 7, 7]
    }
}