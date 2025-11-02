package class185.sweep_line_problems;

import java.util.*;

/**
 * POJ 1151 Atlantis (矩形面积并)
 * 
 * 题目来源：http://poj.org/problem?id=1151
 * 
 * 题目描述：
 * 给定一些矩形，求这些矩形的总面积（重叠部分只计算一次）。
 * 
 * 输入格式：
 * 输入包含多个测试用例。每个测试用例以一个整数n开始，表示矩形的数量。
 * 接下来n行，每行包含四个实数x1, y1, x2, y2，表示一个矩形的左下角和右上角坐标。
 * 当n=0时输入结束。
 * 
 * 输出格式：
 * 对于每个测试用例，输出一行"Test case #k"，其中k是测试用例编号。
 * 然后输出一行"Total explored area: a"，其中a是总面积，保留两位小数。
 * 每个测试用例后输出一个空行。
 * 
 * 示例输入：
 * 2
 * 10 10 20 20
 * 15 15 25 25.5
 * 0
 * 
 * 示例输出：
 * Test case #1
 * Total explored area: 180.00
 * 
 * 解题思路：
 * 使用扫描线算法解决矩形面积并问题。核心思想是：
 * 1. 将每个矩形的左右边界转换为垂直扫描线事件
 * 2. 对所有事件按x坐标排序
 * 3. 在每个x区间内，计算y方向的覆盖长度
 * 4. 累加每个区间的面积
 * 
 * 时间复杂度：O(n^2 log n)，其中 n 是矩形的数量
 * 空间复杂度：O(n)
 * 
 * 相关题目：
 * - LeetCode 850. 矩形面积II
 * - LeetCode 218. 天际线问题
 */
public class POJ_1151_Atlantis {
    
    /**
     * 矩形面积并的扫描线解法
     * @param rectangles 矩形数组，每个矩形是 [x1, y1, x2, y2] 形式
     * @return 覆盖的总面积
     */
    public static double rectangleAreaUnion(double[][] rectangles) {
        if (rectangles == null || rectangles.length == 0) {
            return 0.0;
        }
        
        // 创建垂直扫描线事件
        List<double[]> events = new ArrayList<>();
        Set<Double> yCoordinates = new HashSet<>();
        
        for (double[] rect : rectangles) {
            double x1 = rect[0];
            double y1 = rect[1];
            double x2 = rect[2];
            double y2 = rect[3];
            
            // 添加开始和结束事件
            events.add(new double[]{x1, 0, y1, y2});  // 开始事件
            events.add(new double[]{x2, 1, y1, y2});  // 结束事件
            
            // 收集所有y坐标
            yCoordinates.add(y1);
            yCoordinates.add(y2);
        }
        
        // 排序事件
        events.sort((a, b) -> Double.compare(a[0], b[0]));
        
        // 对y坐标排序
        List<Double> sortedY = new ArrayList<>(yCoordinates);
        Collections.sort(sortedY);
        
        // 用于跟踪当前活动的矩形
        List<double[]> activeIntervals = new ArrayList<>();
        double totalArea = 0.0;
        double prevX = events.get(0)[0];
        
        // 处理每个事件
        for (double[] event : events) {
            double currentX = event[0];
            double width = currentX - prevX;
            
            if (width > 0) {
                // 计算当前活动的y区间总长度
                double height = calculateActiveHeight(activeIntervals, sortedY);
                
                // 增加面积
                totalArea += width * height;
            }
            
            // 更新活动区间
            if (event[1] == 0) {
                activeIntervals.add(new double[]{event[2], event[3]});
            } else {
                activeIntervals.removeIf(interval -> 
                    interval[0] == event[2] && interval[1] == event[3]);
            }
            
            prevX = currentX;
        }
        
        return totalArea;
    }
    
    /**
     * 计算当前活动的y区间总长度
     * @param activeIntervals 活动区间列表
     * @param sortedY 排序后的y坐标
     * @return 总长度
     */
    private static double calculateActiveHeight(List<double[]> activeIntervals, List<Double> sortedY) {
        if (activeIntervals.isEmpty()) {
            return 0.0;
        }
        
        // 合并重叠的y区间
        List<double[]> intervals = new ArrayList<>(activeIntervals);
        intervals.sort((a, b) -> Double.compare(a[0], b[0]));
        
        double totalHeight = 0.0;
        double currentStart = intervals.get(0)[0];
        double currentEnd = intervals.get(0)[1];
        
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
     * 测试矩形面积并解法
     */
    public static void main(String[] args) {
        System.out.println("=== POJ 1151 Atlantis ===");
        
        // 测试用例1
        System.out.println("测试用例1:");
        double[][] rectangles1 = {
            {10, 10, 20, 20},
            {15, 15, 25, 25.5}
        };
        double result1 = rectangleAreaUnion(rectangles1);
        System.out.println("输入: " + Arrays.deepToString(rectangles1));
        System.out.printf("输出: %.2f\n", result1);
        System.out.println("期望: 180.00");
        System.out.println();
        
        // 测试用例2
        System.out.println("测试用例2:");
        double[][] rectangles2 = {
            {0, 0, 10, 10},
            {5, 5, 15, 15}
        };
        double result2 = rectangleAreaUnion(rectangles2);
        System.out.println("输入: " + Arrays.deepToString(rectangles2));
        System.out.printf("输出: %.2f\n", result2);
        System.out.println("期望: 175.00");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        Random random = new Random(42);
        int n = 100;
        double[][] rectangles = new double[n][4];
        
        for (int i = 0; i < n; i++) {
            double x1 = random.nextDouble() * 1000;
            double y1 = random.nextDouble() * 1000;
            double x2 = x1 + random.nextDouble() * 100 + 1;
            double y2 = y1 + random.nextDouble() * 100 + 1;
            rectangles[i][0] = x1;
            rectangles[i][1] = y1;
            rectangles[i][2] = x2;
            rectangles[i][3] = y2;
        }
        
        long startTime = System.nanoTime();
        double result = rectangleAreaUnion(rectangles);
        long endTime = System.nanoTime();
        
        System.out.println("100个随机矩形的总面积计算完成");
        System.out.printf("总面积: %.2f\n", result);
        System.out.println("运行时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
    }
}