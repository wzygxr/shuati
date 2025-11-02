package class185.sweep_line_problems;

import java.util.*;

/**
 * 扫描线算法实现 (Java版本)
 * 
 * 算法思路：
 * 扫描线算法是一种用于解决几何和调度问题的有效技术。
 * 核心思想是将问题中的事件按时间排序，然后按顺序处理这些事件。
 * 
 * 应用场景：
 * 1. 计算几何：矩形面积、线段相交
 * 2. 资源调度：会议室安排、任务调度
 * 3. 图形学：可见性分析、遮挡处理
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 * 
 * 相关题目：
 * 1. LeetCode 850. 矩形面积II
 * 2. LeetCode 56. 合并区间
 * 3. POJ 1151 Atlantis
 */
public class SweepLineAlgorithm {
    
    /**
     * 事件类，用于扫描线算法
     */
    static class Event {
        int time;
        int type;  // 0表示开始事件，1表示结束事件
        int[] data; // 事件关联的数据
        
        Event(int time, int type, int[] data) {
            this.time = time;
            this.type = type;
            this.data = data;
        }
    }
    
    /**
     * 区间覆盖问题：计算最多有多少个重叠的区间
     * @param intervals 区间数组，每个区间是 [start, end] 形式
     * @return 最大重叠数量
     */
    public static int maxOverlap(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return 0;
        }
        
        List<Event> events = new ArrayList<>();
        
        // 为每个区间创建开始和结束事件
        for (int[] interval : intervals) {
            events.add(new Event(interval[0], 0, interval));  // 开始事件
            events.add(new Event(interval[1], 1, interval));  // 结束事件
        }
        
        // 按照时间排序事件
        events.sort((a, b) -> {
            // 首先按照时间排序
            if (a.time != b.time) {
                return Integer.compare(a.time, b.time);
            }
            // 时间相同时，结束事件优先处理，避免重复计算
            return Integer.compare(b.type, a.type);
        });
        
        int maxOverlap = 0;
        int currentOverlap = 0;
        
        // 扫描所有事件
        for (Event event : events) {
            if (event.type == 0) {  // 开始事件
                currentOverlap++;
                maxOverlap = Math.max(maxOverlap, currentOverlap);
            } else {  // 结束事件
                currentOverlap--;
            }
        }
        
        return maxOverlap;
    }
    
    /**
     * 扫描线算法解决矩形面积问题：计算多个矩形的总面积（不重复计算重叠部分）
     * @param rectangles 矩形数组，每个矩形是 [x1, y1, x2, y2] 形式，
     *                   其中(x1,y1)是左下顶点，(x2,y2)是右上顶点
     * @return 矩形覆盖的总面积
     */
    public static int calculateRectangleArea(int[][] rectangles) {
        if (rectangles == null || rectangles.length == 0) {
            return 0;
        }
        
        // 创建垂直扫描线事件
        List<Event> events = new ArrayList<>();
        Set<Integer> yCoordinates = new HashSet<>();
        
        for (int[] rect : rectangles) {
            int x1 = rect[0];
            int y1 = rect[1];
            int x2 = rect[2];
            int y2 = rect[3];
            
            // 添加开始和结束事件
            events.add(new Event(x1, 0, new int[]{y1, y2}));  // 开始事件
            events.add(new Event(x2, 1, new int[]{y1, y2}));  // 结束事件
            
            // 收集所有y坐标
            yCoordinates.add(y1);
            yCoordinates.add(y2);
        }
        
        // 排序事件
        events.sort((a, b) -> {
            if (a.time != b.time) {
                return Integer.compare(a.time, b.time);
            }
            return Integer.compare(a.type, b.type);
        });
        
        // 对y坐标排序
        List<Integer> sortedY = new ArrayList<>(yCoordinates);
        Collections.sort(sortedY);
        
        // 用于跟踪当前活动的矩形
        List<int[]> activeIntervals = new ArrayList<>();
        long totalArea = 0;
        int prevX = events.get(0).time;
        
        // 处理每个事件
        for (Event event : events) {
            int currentX = event.time;
            long width = currentX - prevX;
            
            if (width > 0) {
                // 计算当前活动的y区间总长度
                long height = calculateActiveHeight(activeIntervals, sortedY);
                
                // 增加面积
                totalArea += width * height;
            }
            
            // 更新活动区间
            if (event.type == 0) {
                activeIntervals.add(event.data);
            } else {
                activeIntervals.removeIf(interval -> 
                    interval[0] == event.data[0] && interval[1] == event.data[1]);
            }
            
            prevX = currentX;
        }
        
        return (int) totalArea;
    }
    
    /**
     * 计算当前活动的y区间总长度
     * @param activeIntervals 活动区间列表
     * @param sortedY 排序后的y坐标
     * @return 总长度
     */
    private static long calculateActiveHeight(List<int[]> activeIntervals, List<Integer> sortedY) {
        if (activeIntervals.isEmpty()) {
            return 0;
        }
        
        // 合并重叠的y区间
        List<int[]> intervals = new ArrayList<>(activeIntervals);
        intervals.sort((a, b) -> Integer.compare(a[0], b[0]));
        
        long totalHeight = 0;
        int currentStart = intervals.get(0)[0];
        int currentEnd = intervals.get(0)[1];
        
        for (int i = 1; i < intervals.size(); i++) {
            if (intervals.get(i)[0] <= currentEnd) {
                // 重叠，合并区间
                currentEnd = Math.max(currentEnd, intervals.get(i)[1]);
            } else {
                // 不重叠，计算长度并更新当前区间
                totalHeight += currentEnd - currentStart;
                currentStart = intervals.get(i)[0];
                currentEnd = intervals.get(i)[1];
            }
        }
        
        // 加上最后一个区间
        totalHeight += currentEnd - currentStart;
        
        return totalHeight;
    }
    
    /**
     * 合并区间问题
     * @param intervals 区间数组
     * @return 合并后的区间数组
     */
    public static int[][] mergeIntervals(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return new int[0][0];
        }
        
        // 按照起始位置排序
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        
        List<int[]> merged = new ArrayList<>();
        merged.add(intervals[0]);
        
        for (int i = 1; i < intervals.length; i++) {
            int[] current = intervals[i];
            int[] last = merged.get(merged.size() - 1);
            
            // 如果当前区间与上一个区间重叠，则合并
            if (current[0] <= last[1]) {
                last[1] = Math.max(last[1], current[1]);
            } else {
                // 否则添加新区间
                merged.add(current);
            }
        }
        
        return merged.toArray(new int[merged.size()][]);
    }
    
    /**
     * 测试扫描线算法
     */
    public static void main(String[] args) {
        System.out.println("=== 测试扫描线算法 ===");
        
        // 测试区间重叠问题
        System.out.println("测试区间重叠问题:");
        int[][] intervals1 = {
            {1, 4}, {2, 5}, {3, 6}, {7, 9}
        };
        System.out.println("最大重叠数量: " + maxOverlap(intervals1));  // 应该是3
        
        int[][] intervals2 = {
            {1, 2}, {3, 4}, {5, 6}
        };
        System.out.println("最大重叠数量: " + maxOverlap(intervals2));  // 应该是1
        
        // 测试矩形面积问题
        System.out.println("\n测试矩形面积计算:");
        int[][] rectangles1 = {
            {0, 0, 2, 2}, {1, 1, 3, 3}
        };
        System.out.println("矩形覆盖总面积: " + calculateRectangleArea(rectangles1));  // 应该是7
        
        int[][] rectangles2 = {
            {0, 0, 1, 1}, {2, 2, 3, 3}, {1, 1, 2, 2}
        };
        System.out.println("矩形覆盖总面积: " + calculateRectangleArea(rectangles2));  // 应该是3
        
        // 测试合并区间问题
        System.out.println("\n测试合并区间:");
        int[][] intervals3 = {
            {1, 3}, {2, 6}, {8, 10}, {15, 18}
        };
        int[][] merged = mergeIntervals(intervals3);
        System.out.print("合并后区间: ");
        for (int[] interval : merged) {
            System.out.print("[" + interval[0] + ", " + interval[1] + "] ");
        }
        System.out.println();
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        Random random = new Random(42);
        
        // 生成大量随机区间
        int n = 10000;
        int[][] intervals = new int[n][2];
        for (int i = 0; i < n; i++) {
            int start = random.nextInt(100000);
            int end = start + random.nextInt(1000) + 1;
            intervals[i][0] = start;
            intervals[i][1] = end;
        }
        
        long startTime = System.nanoTime();
        int maxOverlap = maxOverlap(intervals);
        long endTime = System.nanoTime();
        
        System.out.println("10000个随机区间的最大重叠数量: " + maxOverlap);
        System.out.println("运行时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
        
        // 生成大量随机矩形
        int[][] rectangles = new int[1000][4];
        for (int i = 0; i < 1000; i++) {
            int x1 = random.nextInt(1000);
            int y1 = random.nextInt(1000);
            int x2 = x1 + random.nextInt(100) + 1;
            int y2 = y1 + random.nextInt(100) + 1;
            rectangles[i][0] = x1;
            rectangles[i][1] = y1;
            rectangles[i][2] = x2;
            rectangles[i][3] = y2;
        }
        
        startTime = System.nanoTime();
        int totalArea = calculateRectangleArea(rectangles);
        endTime = System.nanoTime();
        
        System.out.println("1000个随机矩形的总面积: " + totalArea);
        System.out.println("运行时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
    }
}