package class184;

import java.util.*;

/**
 * LeetCode 850. Rectangle Area II 解决方案
 * 
 * 题目链接: https://leetcode.com/problems/rectangle-area-ii/
 * 题目描述: 计算多个矩形的总面积（重叠部分只计算一次）
 * 解题思路: 使用扫描线算法处理矩形重叠
 * 
 * 时间复杂度: O(n^2 log n) - n个矩形
 * 空间复杂度: O(n)
 */
public class LeetCode_RectangleArea {
    
    /**
     * 事件类，用于扫描线算法
     */
    static class Event implements Comparable<Event> {
        int x;        // 事件发生的x坐标
        int type;     // 事件类型：0表示矩形开始，1表示矩形结束
        int y1, y2;   // y坐标的范围
        
        Event(int x, int type, int y1, int y2) {
            this.x = x;
            this.type = type;
            this.y1 = y1;
            this.y2 = y2;
        }
        
        @Override
        public int compareTo(Event other) {
            // 首先按照x坐标排序
            if (this.x != other.x) {
                return Integer.compare(this.x, other.x);
            }
            // x坐标相同时，矩形开始事件优先处理
            return Integer.compare(this.type, other.type);
        }
    }
    
    /**
     * 计算多个矩形的总面积（不重复计算重叠部分）
     * @param rectangles 矩形数组，每个矩形是 [x1, y1, x2, y2] 形式
     * @return 矩形覆盖的总面积
     */
    public int rectangleArea(int[][] rectangles) {
        // 检查输入有效性
        if (rectangles == null || rectangles.length == 0) {
            return 0;
        }
        
        // 创建扫描线事件
        List<Event> events = new ArrayList<>();
        
        // 为每个矩形创建开始和结束事件
        for (int[] rect : rectangles) {
            int x1 = rect[0];
            int y1 = rect[1];
            int x2 = rect[2];
            int y2 = rect[3];
            
            // 添加开始和结束事件
            events.add(new Event(x1, 0, y1, y2));  // 开始事件
            events.add(new Event(x2, 1, y1, y2));  // 结束事件
        }
        
        // 按照x坐标排序事件
        Collections.sort(events);
        
        // 用于跟踪当前活动的y区间
        List<int[]> activeIntervals = new ArrayList<>();
        long totalArea = 0;
        int prevX = events.get(0).x;
        
        // 处理每个事件
        for (Event event : events) {
            int currentX = event.x;
            
            // 计算当前扫描线和前一条扫描线之间的面积
            if (currentX > prevX) {
                // 计算当前活动的y区间的总长度
                long activeLength = calculateActiveLength(activeIntervals);
                // 面积 = 宽度 * 高度
                totalArea += (long)(currentX - prevX) * activeLength;
            }
            
            // 更新活动区间
            if (event.type == 0) {
                // 矩形开始事件，添加y区间
                activeIntervals.add(new int[]{event.y1, event.y2});
            } else {
                // 矩形结束事件，移除对应的y区间
                activeIntervals.removeIf(interval -> 
                    interval[0] == event.y1 && interval[1] == event.y2);
            }
            
            prevX = currentX;
        }
        
        // 返回结果，对10^9 + 7取模
        return (int)(totalArea % 1000000007);
    }
    
    /**
     * 计算当前活动的y区间总长度
     * @param intervals 活动的y区间列表
     * @return 总长度
     */
    private long calculateActiveLength(List<int[]> intervals) {
        if (intervals.isEmpty()) {
            return 0;
        }
        
        // 对区间按照起始位置排序
        List<int[]> sortedIntervals = new ArrayList<>(intervals);
        sortedIntervals.sort(Comparator.comparingInt(a -> a[0]));
        
        // 合并重叠的区间
        long totalLength = 0;
        int currentStart = sortedIntervals.get(0)[0];
        int currentEnd = sortedIntervals.get(0)[1];
        
        for (int i = 1; i < sortedIntervals.size(); i++) {
            int[] interval = sortedIntervals.get(i);
            if (interval[0] <= currentEnd) {
                // 重叠，合并区间
                currentEnd = Math.max(currentEnd, interval[1]);
            } else {
                // 不重叠，计算长度并更新当前区间
                totalLength += currentEnd - currentStart;
                currentStart = interval[0];
                currentEnd = interval[1];
            }
        }
        
        // 加上最后一个区间
        totalLength += currentEnd - currentStart;
        
        return totalLength;
    }
    
    /**
     * 使用另一种方法计算矩形面积（坐标压缩）
     * @param rectangles 矩形数组
     * @return 矩形覆盖的总面积
     */
    public int rectangleAreaCoordinateCompression(int[][] rectangles) {
        // 收集所有x和y坐标
        Set<Integer> xCoords = new HashSet<>();
        Set<Integer> yCoords = new HashSet<>();
        
        for (int[] rect : rectangles) {
            xCoords.add(rect[0]);
            xCoords.add(rect[2]);
            yCoords.add(rect[1]);
            yCoords.add(rect[3]);
        }
        
        // 排序坐标
        List<Integer> sortedX = new ArrayList<>(xCoords);
        List<Integer> sortedY = new ArrayList<>(yCoords);
        Collections.sort(sortedX);
        Collections.sort(sortedY);
        
        // 创建坐标映射
        Map<Integer, Integer> xMap = new HashMap<>();
        Map<Integer, Integer> yMap = new HashMap<>();
        
        for (int i = 0; i < sortedX.size(); i++) {
            xMap.put(sortedX.get(i), i);
        }
        
        for (int i = 0; i < sortedY.size(); i++) {
            yMap.put(sortedY.get(i), i);
        }
        
        // 创建网格标记数组
        boolean[][] grid = new boolean[sortedX.size()][sortedY.size()];
        
        // 标记被矩形覆盖的网格
        for (int[] rect : rectangles) {
            int x1 = xMap.get(rect[0]);
            int x2 = xMap.get(rect[2]);
            int y1 = yMap.get(rect[1]);
            int y2 = yMap.get(rect[3]);
            
            for (int i = x1; i < x2; i++) {
                for (int j = y1; j < y2; j++) {
                    grid[i][j] = true;
                }
            }
        }
        
        // 计算总面积
        long totalArea = 0;
        for (int i = 0; i < sortedX.size() - 1; i++) {
            for (int j = 0; j < sortedY.size() - 1; j++) {
                if (grid[i][j]) {
                    long width = (long)sortedX.get(i + 1) - sortedX.get(i);
                    long height = (long)sortedY.get(j + 1) - sortedY.get(j);
                    totalArea += width * height;
                }
            }
        }
        
        return (int)(totalArea % 1000000007);
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        LeetCode_RectangleArea solution = new LeetCode_RectangleArea();
        
        // 测试用例1
        int[][] rectangles1 = {
            {1, 1, 3, 3},
            {3, 1, 4, 2},
            {3, 2, 4, 4},
            {1, 3, 2, 4},
            {2, 3, 3, 4}
        };
        
        System.out.println("=== 测试用例1 ===");
        System.out.println("输入矩形: ");
        for (int[] rect : rectangles1) {
            System.out.println("  [" + rect[0] + ", " + rect[1] + ", " + rect[2] + ", " + rect[3] + "]");
        }
        
        int result1 = solution.rectangleArea(rectangles1);
        System.out.println("扫描线算法结果: " + result1);
        
        int result1Compressed = solution.rectangleAreaCoordinateCompression(rectangles1);
        System.out.println("坐标压缩算法结果: " + result1Compressed);
        
        // 测试用例2
        int[][] rectangles2 = {
            {1, 1, 2, 2},
            {2, 2, 3, 3}
        };
        
        System.out.println("\n=== 测试用例2 ===");
        System.out.println("输入矩形: ");
        for (int[] rect : rectangles2) {
            System.out.println("  [" + rect[0] + ", " + rect[1] + ", " + rect[2] + ", " + rect[3] + "]");
        }
        
        int result2 = solution.rectangleArea(rectangles2);
        System.out.println("扫描线算法结果: " + result2);
        
        int result2Compressed = solution.rectangleAreaCoordinateCompression(rectangles2);
        System.out.println("坐标压缩算法结果: " + result2Compressed);
        
        // 测试用例3：重叠矩形
        int[][] rectangles3 = {
            {0, 0, 2, 2},
            {1, 1, 3, 3}
        };
        
        System.out.println("\n=== 测试用例3：重叠矩形 ===");
        System.out.println("输入矩形: ");
        for (int[] rect : rectangles3) {
            System.out.println("  [" + rect[0] + ", " + rect[1] + ", " + rect[2] + ", " + rect[3] + "]");
        }
        
        int result3 = solution.rectangleArea(rectangles3);
        System.out.println("扫描线算法结果: " + result3);
        
        int result3Compressed = solution.rectangleAreaCoordinateCompression(rectangles3);
        System.out.println("坐标压缩算法结果: " + result3Compressed);
    }
    
    /**
     * 性能测试
     */
    public static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        
        // 生成随机矩形
        int n = 1000;
        int[][] rectangles = new int[n][4];
        Random random = new Random(42); // 固定种子以确保可重复性
        
        for (int i = 0; i < n; i++) {
            int x1 = random.nextInt(1000);
            int y1 = random.nextInt(1000);
            int width = random.nextInt(100) + 1;
            int height = random.nextInt(100) + 1;
            rectangles[i] = new int[]{x1, y1, x1 + width, y1 + height};
        }
        
        LeetCode_RectangleArea solution = new LeetCode_RectangleArea();
        
        // 测试扫描线算法
        long startTime = System.currentTimeMillis();
        int result1 = solution.rectangleArea(rectangles);
        long time1 = System.currentTimeMillis() - startTime;
        
        // 测试坐标压缩算法
        startTime = System.currentTimeMillis();
        int result2 = solution.rectangleAreaCoordinateCompression(rectangles);
        long time2 = System.currentTimeMillis() - startTime;
        
        System.out.println("扫描线算法结果: " + result1 + "，耗时: " + time1 + " ms");
        System.out.println("坐标压缩算法结果: " + result2 + "，耗时: " + time2 + " ms");
    }
}