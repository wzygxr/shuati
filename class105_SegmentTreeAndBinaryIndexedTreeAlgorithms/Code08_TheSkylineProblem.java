package class132;

import java.util.*;

// LeetCode 218. 天际线问题
// 城市的天际线是从远处观看该城市中所有建筑物形成的轮廓的外部轮廓。
// 给你所有建筑物的位置和高度，请返回由这些建筑物形成的天际线。
// 每个建筑物的几何信息由数组 buildings 表示，其中三元组 buildings[i] = [lefti, righti, heighti] 表示：
// lefti 是第 i 座建筑物左边缘的 x 坐标。
// righti 是第 i 座建筑物右边缘的 x 坐标。
// heighti 是第 i 座建筑物的高度。
// 你可以假设所有的建筑都是完美的长方形，在高度为 0 的绝对平坦的表面上。
// 天际线应该表示为由“关键点”组成的列表，格式 [[x1,y1],[x2,y2],...] ，并按 x 坐标进行排序。
// 关键点是水平线段的左端点。列表中最后一个点是最右侧建筑物的终点，y 坐标始终为 0，仅用于标记天际线的终点。
// 此外，任何地面或建筑物上都不应有零长度的线段。
// 测试链接: https://leetcode.cn/problems/the-skyline-problem/

public class Code08_TheSkylineProblem {

    /**
     * 使用线段树解决天际线问题
     * 
     * 解题思路:
     * 1. 将建筑物的左右边界作为事件处理，使用扫描线算法
     * 2. 对所有x坐标进行离散化处理
     * 3. 使用线段树维护区间最大值，表示该区间的最大高度
     * 4. 处理每个事件点，更新线段树并查询当前关键点
     * 
     * 时间复杂度分析:
     * - 离散化: O(n log n)
     * - 处理事件: O(n log n)
     * - 总时间复杂度: O(n log n)
     * 
     * 空间复杂度分析:
     * - 线段树: O(n)
     * - 离散化数组: O(n)
     * - 事件列表: O(n)
     * - 总空间复杂度: O(n)
     * 
     * 工程化考量:
     * 1. 离散化处理大坐标范围
     * 2. 扫描线算法优化
     * 3. 线段树区间更新和查询
     * 4. 边界条件处理
     * 5. 详细注释和变量命名
     */

    // 线段树节点
    static class SegmentTreeNode {
        int start, end;
        int maxHeight;
        SegmentTreeNode left, right;
        
        SegmentTreeNode(int start, int end) {
            this.start = start;
            this.end = end;
            this.maxHeight = 0;
        }
    }
    
    // 线段树类（支持区间更新）
    static class SegmentTree {
        private SegmentTreeNode root;
        
        SegmentTree(int size) {
            root = new SegmentTreeNode(0, size);
        }
        
        // 区间更新：在区间[updateStart, updateEnd]增加高度height
        public void update(int updateStart, int updateEnd, int height) {
            update(root, updateStart, updateEnd, height);
        }
        
        private void update(SegmentTreeNode node, int updateStart, int updateEnd, int height) {
            // 如果当前节点区间与更新区间无交集
            if (node.start > updateEnd || node.end < updateStart) {
                return;
            }
            
            // 如果当前节点是叶子节点
            if (node.start == node.end) {
                node.maxHeight = Math.max(node.maxHeight, height);
                return;
            }
            
            // 如果当前节点区间完全包含在更新区间内
            if (updateStart <= node.start && node.end <= updateEnd) {
                // 这里我们采用懒惰更新的方式，直接更新当前节点
                node.maxHeight = Math.max(node.maxHeight, height);
                return;
            }
            
            int mid = node.start + (node.end - node.start) / 2;
            
            // 创建子节点（如果不存在）
            if (node.left == null) {
                node.left = new SegmentTreeNode(node.start, mid);
            }
            if (node.right == null) {
                node.right = new SegmentTreeNode(mid + 1, node.end);
            }
            
            // 递归更新子节点
            update(node.left, updateStart, updateEnd, height);
            update(node.right, updateStart, updateEnd, height);
        }
        
        // 查询点index的高度
        public int query(int index) {
            return query(root, index);
        }
        
        private int query(SegmentTreeNode node, int index) {
            // 如果节点为空或索引超出范围
            if (node == null || index < node.start || index > node.end) {
                return 0;
            }
            
            // 如果是叶子节点
            if (node.start == node.end) {
                return node.maxHeight;
            }
            
            // 向下传递懒惰标记（如果有的话）
            int mid = node.start + (node.end - node.start) / 2;
            
            // 递归查询并结合当前节点的高度
            if (index <= mid) {
                return Math.max(node.maxHeight, query(node.left, index));
            } else {
                return Math.max(node.maxHeight, query(node.right, index));
            }
        }
    }

    public static List<List<Integer>> getSkyline(int[][] buildings) {
        List<List<Integer>> result = new ArrayList<>();
        
        // 1. 收集所有关键点（建筑物的左右边界）
        List<int[]> events = new ArrayList<>(); // {x, height, type} type: 1表示进入，-1表示离开
        
        for (int[] building : buildings) {
            int left = building[0];
            int right = building[1];
            int height = building[2];
            
            // 进入事件（使用负高度表示进入）
            events.add(new int[]{left, -height, 1});
            // 离开事件（使用正高度表示离开）
            events.add(new int[]{right, height, -1});
        }
        
        // 2. 按照x坐标排序
        // 如果x坐标相同，进入事件优先于离开事件，同样类型的事件按高度排序
        events.sort((a, b) -> {
            if (a[0] != b[0]) {
                return a[0] - b[0]; // 按x坐标升序
            }
            return a[1] - b[1]; // 按高度排序（负高度在前）
        });
        
        // 3. 离散化x坐标
        TreeSet<Integer> xCoords = new TreeSet<>();
        for (int[] event : events) {
            xCoords.add(event[0]);
        }
        
        List<Integer> sortedX = new ArrayList<>(xCoords);
        Map<Integer, Integer> xIndexMap = new HashMap<>();
        for (int i = 0; i < sortedX.size(); i++) {
            xIndexMap.put(sortedX.get(i), i);
        }
        
        // 4. 创建线段树
        SegmentTree segmentTree = new SegmentTree(sortedX.size());
        
        // 5. 处理事件
        int prevHeight = 0;
        for (int[] event : events) {
            int x = event[0];
            int height = Math.abs(event[1]);
            int type = event[1] < 0 ? 1 : -1; // 负数表示进入，正数表示离开
            
            if (type == 1) {
                // 建筑物进入，更新线段树
                int endIndex = xIndexMap.get(x);
                // 找到右边界在离散化数组中的索引
                int rightX = findRightBoundary(buildings, x, height);
                int rightIndex = xIndexMap.get(rightX);
                
                segmentTree.update(endIndex, rightIndex - 1, height);
            }
            // 离开事件已经在进入事件中处理了区间更新
            
            // 查询当前点的高度
            int currentIndex = xIndexMap.get(x);
            int currentHeight = segmentTree.query(currentIndex);
            
            // 如果高度发生变化，添加关键点
            if (currentHeight != prevHeight) {
                result.add(Arrays.asList(x, currentHeight));
                prevHeight = currentHeight;
            }
        }
        
        return result;
    }
    
    // 辅助方法：找到指定建筑物的右边界
    private static int findRightBoundary(int[][] buildings, int left, int height) {
        for (int[] building : buildings) {
            if (building[0] == left && building[2] == height) {
                return building[1];
            }
        }
        return -1; // 不应该到达这里
    }
    
    // 更优化的解决方案
    public static List<List<Integer>> getSkylineOptimized(int[][] buildings) {
        List<List<Integer>> result = new ArrayList<>();
        
        // 创建事件列表
        List<int[]> events = new ArrayList<>();
        
        // 收集所有事件点
        for (int[] building : buildings) {
            int left = building[0];
            int right = building[1];
            int height = building[2];
            
            events.add(new int[]{left, -height});  // 起始事件，用负数表示
            events.add(new int[]{right, height});  // 结束事件
        }
        
        // 排序事件
        events.sort((a, b) -> {
            if (a[0] != b[0]) {
                return a[0] - b[0];
            }
            return a[1] - b[1];
        });
        
        // 使用TreeMap维护当前活跃建筑物的高度（自动排序）
        TreeMap<Integer, Integer> heightMap = new TreeMap<>(Collections.reverseOrder());
        heightMap.put(0, 1); // 地面高度
        
        int prevHeight = 0;
        
        for (int[] event : events) {
            int x = event[0];
            int h = event[1];
            
            if (h < 0) {
                // 起始事件，添加建筑物
                heightMap.put(-h, heightMap.getOrDefault(-h, 0) + 1);
            } else {
                // 结束事件，移除建筑物
                heightMap.put(h, heightMap.get(h) - 1);
                if (heightMap.get(h) == 0) {
                    heightMap.remove(h);
                }
            }
            
            // 获取当前最大高度
            int currentHeight = heightMap.firstKey();
            
            // 如果高度发生变化，添加关键点
            if (currentHeight != prevHeight) {
                result.add(Arrays.asList(x, currentHeight));
                prevHeight = currentHeight;
            }
        }
        
        return result;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[][] buildings1 = {{2, 9, 10}, {3, 7, 15}, {5, 12, 12}, {15, 20, 10}, {19, 24, 8}};
        List<List<Integer>> result1 = getSkylineOptimized(buildings1);
        System.out.println("Test case 1:");
        System.out.println("Input: [[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]]");
        System.out.println("Output: " + result1);
        // 期望输出: [[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]
        
        // 测试用例2
        int[][] buildings2 = {{0, 2, 3}, {2, 5, 3}};
        List<List<Integer>> result2 = getSkylineOptimized(buildings2);
        System.out.println("\nTest case 2:");
        System.out.println("Input: [[0,2,3],[2,5,3]]");
        System.out.println("Output: " + result2);
        // 期望输出: [[0,3],[5,0]]
    }
}