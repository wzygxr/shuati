package class115;

import java.util.*;

/**
 * 天际线问题 - 扫描线算法实现
 * 题目链接: https://leetcode.cn/problems/the-skyline-problem/
 * 
 * 题目描述:
 * 城市的天际线是从远处观看该城市中所有建筑物形成的轮廓的外部轮廓。
 * 给你所有建筑物的位置和高度，请返回由这些建筑物形成的天际线。
 * 
 * 每个建筑物由三个整数 [left, right, height] 表示：
 * - left 是建筑物左边缘的 x 坐标
 * - right 是建筑物右边缘的 x 坐标
 * - height 是建筑物的高度
 * 
 * 天际线应该表示为由关键点组成的列表，格式为 [[x1, y1], [x2, y2], ...]，
 * 并按 x 坐标进行排序。每个关键点是天际线中的一个转折点，y 坐标表示该点的高度。
 * 
 * 解题思路:
 * 使用扫描线算法结合最大堆实现天际线问题的求解。
 * 1. 将建筑物的左右边界作为事件点
 * 2. 按x坐标排序所有事件点
 * 3. 使用最大堆维护当前活动建筑物的高度
 * 4. 扫描过程中记录高度变化的关键点
 * 
 * 算法复杂度: 时间复杂度O(n log n)，空间复杂度O(n)
 * 工程化考量:
 * 1. 异常处理: 检查建筑物数据合法性
 * 2. 边界条件: 处理建筑物边界重叠情况
 * 3. 性能优化: 使用延迟删除优化堆操作
 * 4. 可读性: 详细注释和模块化设计
 * 5. 提供了两种实现方式：基于PriorityQueue和基于TreeMap
 */
public class Code09_SkylineProblem {
    
    /**
     * 事件点类
     * 表示建筑物的左边界（开始事件）或右边界（结束事件）
     */
    static class Event implements Comparable<Event> {
        int x; // x坐标(事件位置)
        int height; // 建筑物高度
        boolean isStart; // 是否是开始事件(true表示左边界，false表示右边界)
        
        Event(int x, int height, boolean isStart) {
            this.x = x;
            this.height = height;
            this.isStart = isStart;
        }
        
        @Override
        public int compareTo(Event other) {
            // 按x坐标排序，相同x坐标时开始事件优先
            if (this.x != other.x) {
                return Integer.compare(this.x, other.x);
            }
            // 相同x坐标时，开始事件优先于结束事件
            // 这样可以正确处理边界情况，避免重复计算
            if (this.isStart && !other.isStart) {
                return -1;
            }
            if (!this.isStart && other.isStart) {
                return 1;
            }
            // 都是开始事件时，高度高的优先
            // 这样可以确保高的建筑物先被处理
            if (this.isStart && other.isStart) {
                return Integer.compare(other.height, this.height);
            }
            // 都是结束事件时，高度低的优先
            // 这样可以确保低的建筑物先结束
            return Integer.compare(this.height, other.height);
        }
    }
    
    /**
     * 求解天际线问题
     * 算法核心思想：
     * 1. 将每个建筑物的左右边界作为事件点
     * 2. 按x坐标排序所有事件点
     * 3. 使用最大堆维护当前活动建筑物的高度
     * 4. 扫描过程中记录高度变化的关键点
     * 
     * @param buildings 建筑物数组，每个元素为 [left, right, height]
     * @return 天际线的关键点列表
     * @throws IllegalArgumentException 当建筑物数据不合法时抛出异常
     */
    public List<List<Integer>> getSkyline(int[][] buildings) {
        // 边界条件检查
        if (buildings == null || buildings.length == 0) {
            return new ArrayList<>();
        }
        
        // 创建事件列表
        List<Event> events = new ArrayList<>();
        for (int[] building : buildings) {
            if (building.length != 3) {
                throw new IllegalArgumentException("Invalid building format");
            }
            
            int left = building[0], right = building[1], height = building[2];
            
            // 检查建筑物数据合法性
            if (left >= right || height <= 0) {
                throw new IllegalArgumentException("Invalid building coordinates");
            }
            
            // 添加开始事件(建筑物左边界)和结束事件(建筑物右边界)
            events.add(new Event(left, height, true));
            events.add(new Event(right, height, false));
        }
        
        // 对事件按x坐标排序
        Collections.sort(events);
        
        // 使用最大堆维护当前活动建筑物的高度
        // 使用PriorityQueue作为最大堆（通过比较器反转）
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        // 记录当前高度，用于检测高度变化
        int prevHeight = 0;
        maxHeap.offer(0); // 添加地面高度(高度为0)
        
        // 使用延迟删除技术优化堆操作
        // 由于PriorityQueue不支持直接删除任意元素，我们使用Map记录需要删除的元素
        Map<Integer, Integer> delayed = new HashMap<>();
        
        List<List<Integer>> result = new ArrayList<>();
        
        for (Event event : events) {
            if (event.isStart) {
                // 开始事件：将建筑物高度加入堆
                maxHeap.offer(event.height);
            } else {
                // 结束事件：标记建筑物高度需要删除(延迟删除)
                delayed.put(event.height, delayed.getOrDefault(event.height, 0) + 1);
            }
            
            // 清理堆顶的无效元素（延迟删除）
            // 移除所有已经被标记为删除但仍在堆顶的元素
            while (!maxHeap.isEmpty()) {
                int top = maxHeap.peek();
                if (delayed.containsKey(top)) {
                    delayed.put(top, delayed.get(top) - 1);
                    if (delayed.get(top) == 0) {
                        delayed.remove(top);
                    }
                    maxHeap.poll();
                } else {
                    break;
                }
            }
            
            // 获取当前最大高度(堆顶元素)
            int currentHeight = maxHeap.isEmpty() ? 0 : maxHeap.peek();
            
            // 如果高度发生变化，记录关键点
            if (currentHeight != prevHeight) {
                List<Integer> point = new ArrayList<>();
                point.add(event.x);
                point.add(currentHeight);
                result.add(point);
                prevHeight = currentHeight;
            }
        }
        
        return result;
    }
    
    /**
     * 优化版本：使用TreeMap替代PriorityQueue，避免延迟删除
     * TreeMap可以更高效地维护高度及其出现次数
     * 
     * @param buildings 建筑物数组，每个元素为 [left, right, height]
     * @return 天际线的关键点列表
     */
    public List<List<Integer>> getSkylineOptimized(int[][] buildings) {
        if (buildings == null || buildings.length == 0) {
            return new ArrayList<>();
        }
        
        // 创建事件列表
        // 使用int[]数组表示事件，[x坐标, 高度]
        // 高度为负数表示开始事件，正数表示结束事件
        List<int[]> events = new ArrayList<>();
        for (int[] building : buildings) {
            // 开始事件：高度取负数
            events.add(new int[]{building[0], -building[2]});
            // 结束事件：高度为正数
            events.add(new int[]{building[1], building[2]});
        }
        
        // 按x坐标排序，相同x坐标时开始事件优先
        // 由于开始事件的高度为负数，结束事件的高度为正数
        // 负数小于正数，所以开始事件会优先于结束事件
        events.sort((a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            }
            return Integer.compare(a[1], b[1]);
        });
        
        // 使用TreeMap维护高度及其出现次数
        // TreeMap按高度降序排列，便于获取最大高度
        TreeMap<Integer, Integer> heightMap = new TreeMap<>(Collections.reverseOrder());
        heightMap.put(0, 1); // 地面高度(高度为0，出现1次)
        int prevHeight = 0;
        
        List<List<Integer>> result = new ArrayList<>();
        
        for (int[] event : events) {
            int x = event[0];
            int height = event[1];
            
            if (height < 0) {
                // 开始事件：高度取负，需要转为正数
                height = -height;
                // 增加该高度的建筑物数量
                heightMap.put(height, heightMap.getOrDefault(height, 0) + 1);
            } else {
                // 结束事件：减少该高度的建筑物数量
                int count = heightMap.get(height);
                if (count == 1) {
                    // 如果该高度的建筑物只有1个，直接移除
                    heightMap.remove(height);
                } else {
                    // 如果该高度的建筑物有多个，减少计数
                    heightMap.put(height, count - 1);
                }
            }
            
            // 获取当前最大高度(TreeMap的第一个键)
            int currentHeight = heightMap.firstKey();
            
            // 如果高度发生变化，记录关键点
            if (currentHeight != prevHeight) {
                List<Integer> point = new ArrayList<>();
                point.add(x);
                point.add(currentHeight);
                result.add(point);
                prevHeight = currentHeight;
            }
        }
        
        return result;
    }
    
    /**
     * 测试用例
     * 验证getSkyline和getSkylineOptimized方法的正确性
     */
    public static void main(String[] args) {
        Code09_SkylineProblem solution = new Code09_SkylineProblem();
        
        // 测试用例1: 标准情况
        // 建筑物1: [2, 9, 10]，从x=2到x=9，高度10
        // 建筑物2: [3, 7, 15]，从x=3到x=7，高度15
        // 建筑物3: [5, 12, 12]，从x=5到x=12，高度12
        // 建筑物4: [15, 20, 10]，从x=15到x=20，高度10
        // 建筑物5: [19, 24, 8]，从x=19到x=24，高度8
        int[][] buildings1 = {
            {2, 9, 10},
            {3, 7, 15},
            {5, 12, 12},
            {15, 20, 10},
            {19, 24, 8}
        };
        
        List<List<Integer>> result1 = solution.getSkyline(buildings1);
        System.out.println("测试用例1 结果:");
        for (List<Integer> point : result1) {
            System.out.println("[" + point.get(0) + ", " + point.get(1) + "]");
        }
        // 预期: [[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]
        
        // 测试用例2: 单个建筑物
        // 建筑物: [0, 2, 3]，从x=0到x=2，高度3
        int[][] buildings2 = {{0, 2, 3}};
        List<List<Integer>> result2 = solution.getSkyline(buildings2);
        System.out.println("\n测试用例2 结果:");
        for (List<Integer> point : result2) {
            System.out.println("[" + point.get(0) + ", " + point.get(1) + "]");
        }
        // 预期: [[0,3],[2,0]]
        
        // 测试用例3: 建筑物高度相同
        // 建筑物1: [1, 3, 5]
        // 建筑物2: [2, 4, 5]
        // 建筑物3: [5, 6, 5]
        int[][] buildings3 = {
            {1, 3, 5},
            {2, 4, 5},
            {5, 6, 5}
        };
        List<List<Integer>> result3 = solution.getSkyline(buildings3);
        System.out.println("\n测试用例3 结果:");
        for (List<Integer> point : result3) {
            System.out.println("[" + point.get(0) + ", " + point.get(1) + "]");
        }
        // 预期: [[1,5],[4,0],[5,5],[6,0]]
        
        // 测试用例4: 建筑物完全包含
        // 建筑物1: [1, 5, 10]
        // 建筑物2: [2, 4, 5]
        int[][] buildings4 = {
            {1, 5, 10},
            {2, 4, 5}
        };
        List<List<Integer>> result4 = solution.getSkyline(buildings4);
        System.out.println("\n测试用例4 结果:");
        for (List<Integer> point : result4) {
            System.out.println("[" + point.get(0) + ", " + point.get(1) + "]");
        }
        // 预期: [[1,10],[5,0]]
        
        // 测试用例5: 空数组
        int[][] buildings5 = {};
        List<List<Integer>> result5 = solution.getSkyline(buildings5);
        System.out.println("\n测试用例5 结果: " + result5.size());
        // 预期: 空列表
        
        // 测试优化版本
        System.out.println("\n=== 优化版本测试 ===");
        List<List<Integer>> result1Opt = solution.getSkylineOptimized(buildings1);
        System.out.println("测试用例1 优化版本结果:");
        for (List<Integer> point : result1Opt) {
            System.out.println("[" + point.get(0) + ", " + point.get(1) + "]");
        }
    }
}