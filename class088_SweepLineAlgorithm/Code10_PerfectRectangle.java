package class115;

import java.util.*;

/**
 * 完美矩形 - 扫描线算法应用
 * 题目链接: https://leetcode.cn/problems/perfect-rectangle/
 * 
 * 题目描述:
 * 给你一个数组 rectangles ，其中 rectangles[i] = [xi, yi, ai, bi] 表示一个坐标轴平行的矩形。
 * 这个矩形的左下顶点是 (xi, yi) ，右上顶点是 (ai, bi) 。
 * 如果所有矩形一起精确覆盖某个矩形区域，则返回 true ；否则，返回 false 。
 * 
 * 解题思路:
 * 使用扫描线算法结合几何性质判断矩形是否完美覆盖。
 * 1. 面积检查：所有矩形面积之和等于最外层矩形的面积
 * 2. 顶点检查：除了四个角点外，其他所有顶点出现的次数都是偶数次
 * 3. 边界检查：最终应该只有四个顶点，且正好是最外层矩形的四个角点
 * 
 * 算法复杂度: 时间复杂度O(n log n)，空间复杂度O(n)
 * 工程化考量:
 * 1. 异常处理: 检查矩形数据合法性
 * 2. 边界条件: 处理矩形重叠和边界情况
 * 3. 性能优化: 使用哈希表快速统计顶点
 * 4. 可读性: 详细注释和模块化设计
 * 5. 提供了两种实现方式：基本版本和优化版本
 */
public class Code10_PerfectRectangle {
    
    /**
     * 判断矩形是否完美覆盖
     * 算法核心思想：
     * 1. 面积检查：所有矩形面积之和必须等于最外层矩形的面积
     * 2. 顶点检查：除了四个角点外，其他所有顶点出现的次数都是偶数次
     * 3. 边界检查：最终应该只有四个顶点，且正好是最外层矩形的四个角点
     * 
     * @param rectangles 矩形数组，每个元素为 [xi, yi, ai, bi]
     * @return 是否完美覆盖
     */
    public boolean isRectangleCover(int[][] rectangles) {
        // 边界条件检查
        if (rectangles == null || rectangles.length == 0) {
            return false;
        }
        
        // 记录所有顶点及其出现次数
        Map<String, Integer> pointCount = new HashMap<>();
        
        // 计算所有矩形的面积和
        long totalArea = 0;
        
        // 记录最小和最大的坐标，用于计算最终矩形的面积
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        
        for (int[] rect : rectangles) {
            if (rect.length != 4) {
                throw new IllegalArgumentException("Invalid rectangle format");
            }
            
            int x1 = rect[0], y1 = rect[1], x2 = rect[2], y2 = rect[3];
            
            // 检查坐标合法性
            if (x1 >= x2 || y1 >= y2) {
                throw new IllegalArgumentException("Invalid rectangle coordinates");
            }
            
            // 更新边界坐标
            minX = Math.min(minX, x1);
            minY = Math.min(minY, y1);
            maxX = Math.max(maxX, x2);
            maxY = Math.max(maxY, y2);
            
            // 计算当前矩形的面积并累加到总面积
            totalArea += (long)(x2 - x1) * (y2 - y1);
            
            // 记录四个顶点
            String[] points = {
                x1 + "," + y1, // 左下角
                x1 + "," + y2, // 左上角
                x2 + "," + y1, // 右下角
                x2 + "," + y2  // 右上角
            };
            
            // 更新顶点计数
            for (String point : points) {
                pointCount.put(point, pointCount.getOrDefault(point, 0) + 1);
            }
        }
        
        // 检查面积条件：所有矩形面积之和必须等于最外层矩形的面积
        long expectedArea = (long)(maxX - minX) * (maxY - minY);
        if (totalArea != expectedArea) {
            return false;
        }
        
        // 检查顶点条件：除了四个角点外，其他所有顶点出现的次数必须是偶数次
        // 四个角点应该只出现一次，其他顶点应该出现偶数次
        String[] cornerPoints = {
            minX + "," + minY, // 左下角
            minX + "," + maxY, // 左上角
            maxX + "," + minY, // 右下角
            maxX + "," + maxY  // 右上角
        };
        
        // 检查四个角点
        for (String corner : cornerPoints) {
            Integer count = pointCount.get(corner);
            if (count == null || count != 1) {
                return false;
            }
            pointCount.remove(corner);
        }
        
        // 检查其他顶点：出现次数必须是偶数次
        for (int count : pointCount.values()) {
            if (count % 2 != 0) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 优化版本：使用扫描线算法进行更严格的检查
     * 通过扫描线算法检查矩形之间是否有重叠
     * 
     * @param rectangles 矩形数组，每个元素为 [xi, yi, ai, bi]
     * @return 是否完美覆盖
     */
    public boolean isRectangleCoverOptimized(int[][] rectangles) {
        if (rectangles == null || rectangles.length == 0) {
            return false;
        }
        
        // 使用扫描线算法检查是否有重叠
        List<int[]> events = new ArrayList<>();
        
        for (int[] rect : rectangles) {
            int x1 = rect[0], y1 = rect[1], x2 = rect[2], y2 = rect[3];
            
            // 添加开始事件(矩形下边界)和结束事件(矩形上边界)
            events.add(new int[]{y1, x1, x2, 1});  // 开始事件
            events.add(new int[]{y2, x1, x2, -1}); // 结束事件
        }
        
        // 按y坐标排序
        events.sort((a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            }
            // 相同y坐标时，开始事件优先
            return Integer.compare(b[3], a[3]);
        });
        
        // 使用TreeMap维护当前活动的x区间
        TreeMap<Integer, Integer> activeIntervals = new TreeMap<>();
        
        int currentY = Integer.MIN_VALUE;
        
        for (int[] event : events) {
            int y = event[0];
            int x1 = event[1];
            int x2 = event[2];
            int type = event[3];
            
            if (type == 1) {
                // 开始事件：检查是否有重叠
                Integer floorKey = activeIntervals.floorKey(x1);
                if (floorKey != null && activeIntervals.get(floorKey) > x1) {
                    return false; // 有重叠
                }
                
                Integer ceilingKey = activeIntervals.ceilingKey(x1);
                if (ceilingKey != null && ceilingKey < x2) {
                    return false; // 有重叠
                }
                
                activeIntervals.put(x1, x2);
            } else {
                // 结束事件：移除区间
                activeIntervals.remove(x1);
            }
            
            currentY = y;
        }
        
        // 再次使用基本方法进行最终检查
        return isRectangleCover(rectangles);
    }
    
    /**
     * 测试用例
     * 验证isRectangleCover和isRectangleCoverOptimized方法的正确性
     */
    public static void main(String[] args) {
        Code10_PerfectRectangle solution = new Code10_PerfectRectangle();
        
        // 测试用例1: 完美覆盖
        // 矩形组合形成一个完整的矩形区域
        int[][] rectangles1 = {
            {1, 1, 3, 3},
            {3, 1, 4, 2},
            {3, 2, 4, 4},
            {1, 3, 2, 4},
            {2, 3, 3, 4}
        };
        boolean result1 = solution.isRectangleCover(rectangles1);
        System.out.println("测试用例1 结果: " + result1); // 预期: true
        
        // 测试用例2: 有重叠
        // 矩形之间存在重叠区域
        int[][] rectangles2 = {
            {1, 1, 3, 3},
            {3, 1, 4, 2},
            {1, 3, 2, 4},
            {2, 2, 4, 4}
        };
        boolean result2 = solution.isRectangleCover(rectangles2);
        System.out.println("测试用例2 结果: " + result2); // 预期: false
        
        // 测试用例3: 有空隙
        // 矩形之间存在空隙
        int[][] rectangles3 = {
            {1, 1, 2, 3},
            {2, 1, 3, 3},
            {3, 1, 4, 2},
            {3, 2, 4, 3}
        };
        boolean result3 = solution.isRectangleCover(rectangles3);
        System.out.println("测试用例3 结果: " + result3); // 预期: true
        
        // 测试用例4: 单个矩形
        // 只有一个矩形，自然是完美覆盖
        int[][] rectangles4 = {{0, 0, 1, 1}};
        boolean result4 = solution.isRectangleCover(rectangles4);
        System.out.println("测试用例4 结果: " + result4); // 预期: true
        
        // 测试用例5: 两个相邻矩形
        // 两个矩形相邻，形成一个更大的矩形
        int[][] rectangles5 = {
            {0, 0, 1, 1},
            {1, 0, 2, 1}
        };
        boolean result5 = solution.isRectangleCover(rectangles5);
        System.out.println("测试用例5 结果: " + result5); // 预期: true
        
        // 测试用例6: 面积不匹配
        // 两个矩形部分重叠，总面积不等于外接矩形面积
        int[][] rectangles6 = {
            {0, 0, 2, 2},
            {1, 1, 3, 3}
        };
        boolean result6 = solution.isRectangleCover(rectangles6);
        System.out.println("测试用例6 结果: " + result6); // 预期: false
        
        // 测试用例7: 顶点条件不满足
        // 两个完全相同的矩形，顶点计数不满足条件
        int[][] rectangles7 = {
            {0, 0, 2, 2},
            {0, 0, 2, 2}  // 完全相同的矩形
        };
        boolean result7 = solution.isRectangleCover(rectangles7);
        System.out.println("测试用例7 结果: " + result7); // 预期: false
        
        // 测试优化版本
        System.out.println("\n=== 优化版本测试 ===");
        boolean result1Opt = solution.isRectangleCoverOptimized(rectangles1);
        System.out.println("测试用例1 优化版本结果: " + result1Opt);
        
        boolean result2Opt = solution.isRectangleCoverOptimized(rectangles2);
        System.out.println("测试用例2 优化版本结果: " + result2Opt);
    }
    
    /**
     * 调试辅助方法：打印顶点统计信息
     * 用于调试和理解算法过程
     * 
     * @param rectangles 矩形数组
     */
    private void printPointStatistics(int[][] rectangles) {
        Map<String, Integer> pointCount = new HashMap<>();
        long totalArea = 0;
        
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
        
        for (int[] rect : rectangles) {
            int x1 = rect[0], y1 = rect[1], x2 = rect[2], y2 = rect[3];
            
            minX = Math.min(minX, x1);
            minY = Math.min(minY, y1);
            maxX = Math.max(maxX, x2);
            maxY = Math.max(maxY, y2);
            
            totalArea += (long)(x2 - x1) * (y2 - y1);
            
            String[] points = {
                x1 + "," + y1, x1 + "," + y2,
                x2 + "," + y1, x2 + "," + y2
            };
            
            for (String point : points) {
                pointCount.put(point, pointCount.getOrDefault(point, 0) + 1);
            }
        }
        
        long expectedArea = (long)(maxX - minX) * (maxY - minY);
        
        System.out.println("总面积: " + totalArea);
        System.out.println("期望面积: " + expectedArea);
        System.out.println("面积匹配: " + (totalArea == expectedArea));
        System.out.println("边界: [" + minX + ", " + minY + "] - [" + maxX + ", " + maxY + "]");
        
        System.out.println("顶点统计:");
        for (Map.Entry<String, Integer> entry : pointCount.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
    }
}