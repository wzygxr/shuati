package class185.sweep_line_problems;

import java.util.*;

/**
 * LeetCode 850. 矩形面积II (Rectangle Area II)
 * 
 * 题目来源：https://leetcode.cn/problems/rectangle-area-ii/
 * 
 * 题目描述：
 * 我们给出了一个（轴对齐的）二维矩形列表 rectangles 。
 * 对于 rectangle[i] = [x1, y1, x2, y2] ，其中 (x1, y1) 是矩形 i 左下角的坐标， (x2, y2) 是该矩形右上角的坐标。
 * 计算平面中所有 rectangles 所覆盖的总面积。任何被两个或多个矩形覆盖的区域应只计算一次。
 * 返回总面积。因为答案可能太大，返回 10^9 + 7 的模。
 * 
 * 示例 1：
 * 输入：rectangles = [[1,1,3,3],[3,1,4,2],[3,2,4,4],[1,3,2,4],[2,3,3,4]]
 * 输出：6
 * 解释：如图所示，三个矩形代表三个矩形，红色的为绿色的为蓝色的为。它们共同覆盖了总面积为6的区域。
 * 
 * 示例 2：
 * 输入：rectangles = [[1,1,2,2],[2,2,3,3]]
 * 输出：1
 * 解释：如图所示，两个矩形之间没有重叠区域。
 * 
 * 示例 3：
 * 输入：rectangles = [[0,0,2,2],[1,0,2,3],[1,0,3,1]]
 * 输出：6
 * 解释：如图所示，三个矩形的总面积为6。
 * 
 * 提示：
 * 1 <= rectangles.length <= 200
 * rectangles[i].length == 4
 * 0 <= xi1, yi1, xi2, yi2 <= 10^9
 * 
 * 解题思路：
 * 使用扫描线算法解决矩形面积问题。核心思想是：
 * 1. 将每个矩形的左右边界转换为垂直扫描线事件
 * 2. 对所有事件按x坐标排序
 * 3. 在每个x区间内，计算y方向的覆盖长度
 * 4. 累加每个区间的面积
 * 
 * 时间复杂度：O(n^2 log n)，其中 n 是矩形的数量
 * 空间复杂度：O(n)
 * 
 * 相关题目：
 * - LeetCode 218. 天际线问题
 * - LeetCode 391. 完美矩形
 */
public class LeetCode_850_RectangleAreaII {
    
    private static final int MOD = 1000000007;
    
    /**
     * 矩形面积II的扫描线解法
     * @param rectangles 矩形数组，每个矩形是 [x1, y1, x2, y2] 形式
     * @return 覆盖的总面积
     */
    public static int rectangleArea(int[][] rectangles) {
        // 创建垂直扫描线事件
        List<int[]> events = new ArrayList<>();
        Set<Integer> yCoordinates = new HashSet<>();
        
        for (int[] rect : rectangles) {
            int x1 = rect[0];
            int y1 = rect[1];
            int x2 = rect[2];
            int y2 = rect[3];
            
            // 添加开始和结束事件
            events.add(new int[]{x1, 0, y1, y2});  // 开始事件
            events.add(new int[]{x2, 1, y1, y2});  // 结束事件
            
            // 收集所有y坐标
            yCoordinates.add(y1);
            yCoordinates.add(y2);
        }
        
        // 排序事件
        events.sort((a, b) -> Integer.compare(a[0], b[0]));
        
        // 对y坐标排序
        List<Integer> sortedY = new ArrayList<>(yCoordinates);
        Collections.sort(sortedY);
        
        // 用于跟踪当前活动的矩形
        List<int[]> activeIntervals = new ArrayList<>();
        long totalArea = 0;
        int prevX = events.get(0)[0];
        
        // 处理每个事件
        for (int[] event : events) {
            int currentX = event[0];
            long width = currentX - prevX;
            
            if (width > 0) {
                // 计算当前活动的y区间总长度
                long height = calculateActiveHeight(activeIntervals, sortedY);
                
                // 增加面积
                totalArea = (totalArea + width * height) % MOD;
            }
            
            // 更新活动区间
            if (event[1] == 0) {
                activeIntervals.add(new int[]{event[2], event[3]});
            } else {
                activeIntervals.removeIf(interval -> 
                    interval[0] == event[2] && interval[1] == event[3]);
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
     * 测试矩形面积II解法
     */
    public static void main(String[] args) {
        System.out.println("=== LeetCode 850. 矩形面积II ===");
        
        // 测试用例1
        System.out.println("测试用例1:");
        int[][] rectangles1 = {
            {1,1,3,3},{3,1,4,2},{3,2,4,4},{1,3,2,4},{2,3,3,4}
        };
        int result1 = rectangleArea(rectangles1);
        System.out.println("输入: " + Arrays.deepToString(rectangles1));
        System.out.println("输出: " + result1);
        System.out.println("期望: 6");
        System.out.println();
        
        // 测试用例2
        System.out.println("测试用例2:");
        int[][] rectangles2 = {
            {1,1,2,2},{2,2,3,3}
        };
        int result2 = rectangleArea(rectangles2);
        System.out.println("输入: " + Arrays.deepToString(rectangles2));
        System.out.println("输出: " + result2);
        System.out.println("期望: 1");
        System.out.println();
        
        // 测试用例3
        System.out.println("测试用例3:");
        int[][] rectangles3 = {
            {0,0,2,2},{1,0,2,3},{1,0,3,1}
        };
        int result3 = rectangleArea(rectangles3);
        System.out.println("输入: " + Arrays.deepToString(rectangles3));
        System.out.println("输出: " + result3);
        System.out.println("期望: 6");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        Random random = new Random(42);
        int n = 200;
        int[][] rectangles = new int[n][4];
        
        for (int i = 0; i < n; i++) {
            int x1 = random.nextInt(1000);
            int y1 = random.nextInt(1000);
            int x2 = x1 + random.nextInt(100) + 1;
            int y2 = y1 + random.nextInt(100) + 1;
            rectangles[i][0] = x1;
            rectangles[i][1] = y1;
            rectangles[i][2] = x2;
            rectangles[i][3] = y2;
        }
        
        long startTime = System.nanoTime();
        int result = rectangleArea(rectangles);
        long endTime = System.nanoTime();
        
        System.out.println("200个随机矩形的总面积计算完成");
        System.out.println("总面积: " + result);
        System.out.println("运行时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
    }
}