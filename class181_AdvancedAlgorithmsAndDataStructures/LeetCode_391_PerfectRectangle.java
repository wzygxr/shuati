package class008_AdvancedAlgorithmsAndDataStructures.sweep_line_problems;

import java.util.*;

/**
 * LeetCode 391. Perfect Rectangle
 * 
 * 题目描述：
 * 给定 N 个与坐标轴对齐的矩形，其中每个矩形由 [xi, yi, ai, bi] 表示，
 * 其中 (xi, yi) 是该矩形左下角的坐标，(ai, bi) 是该矩形右上角的坐标。
 * 判断所有矩形是否能精确覆盖某个矩形区域。
 * 
 * 解题思路：
 * 使用扫描线算法的思想，我们需要验证两个条件：
 * 1. 所有矩形的总面积等于最外层矩形的面积
 * 2. 除了最外层矩形的四个顶点外，其他所有顶点都出现了偶数次（被相互抵消）
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 */
public class LeetCode_391_PerfectRectangle {
    
    static class Solution {
        public boolean isRectangleCover(int[][] rectangles) {
            if (rectangles == null || rectangles.length == 0) {
                return false;
            }
            
            // 记录所有顶点出现的次数
            Set<String> points = new HashSet<>();
            
            // 计算总面积
            int totalArea = 0;
            
            // 记录最外层矩形的边界
            int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
            
            // 遍历所有矩形
            for (int[] rect : rectangles) {
                int x1 = rect[0], y1 = rect[1], x2 = rect[2], y2 = rect[3];
                
                // 更新边界
                minX = Math.min(minX, x1);
                minY = Math.min(minY, y1);
                maxX = Math.max(maxX, x2);
                maxY = Math.max(maxY, y2);
                
                // 累加面积
                totalArea += (x2 - x1) * (y2 - y1);
                
                // 记录四个顶点
                String[] corners = {
                    x1 + "," + y1,  // 左下
                    x1 + "," + y2,  // 左上
                    x2 + "," + y1,  // 右下
                    x2 + "," + y2   // 右上
                };
                
                // 如果顶点已存在则删除，否则添加
                for (String corner : corners) {
                    if (points.contains(corner)) {
                        points.remove(corner);
                    } else {
                        points.add(corner);
                    }
                }
            }
            
            // 验证条件1：总面积是否等于最外层矩形面积
            int expectedArea = (maxX - minX) * (maxY - minY);
            if (totalArea != expectedArea) {
                return false;
            }
            
            // 验证条件2：只有四个顶点
            if (points.size() != 4) {
                return false;
            }
            
            // 验证这四个顶点是否就是最外层矩形的四个顶点
            Set<String> expectedCorners = new HashSet<>();
            expectedCorners.add(minX + "," + minY);
            expectedCorners.add(minX + "," + maxY);
            expectedCorners.add(maxX + "," + minY);
            expectedCorners.add(maxX + "," + maxY);
            
            return points.equals(expectedCorners);
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例1：完美矩形
        int[][] rectangles1 = {{1,1,3,3},{3,1,4,2},{3,2,4,4},{1,3,2,4},{2,3,3,4}};
        System.out.println("测试用例1:");
        System.out.println("矩形: " + Arrays.deepToString(rectangles1));
        System.out.println("是否为完美矩形: " + solution.isRectangleCover(rectangles1));
        System.out.println();
        
        // 测试用例2：非完美矩形（有重叠）
        int[][] rectangles2 = {{1,1,2,3},{1,3,2,4},{3,1,4,2},{3,2,4,4}};
        System.out.println("测试用例2:");
        System.out.println("矩形: " + Arrays.deepToString(rectangles2));
        System.out.println("是否为完美矩形: " + solution.isRectangleCover(rectangles2));
        System.out.println();
        
        // 测试用例3：非完美矩形（有空隙）
        int[][] rectangles3 = {{1,1,3,3},{3,1,4,2},{4,2,5,4}};
        System.out.println("测试用例3:");
        System.out.println("矩形: " + Arrays.deepToString(rectangles3));
        System.out.println("是否为完美矩形: " + solution.isRectangleCover(rectangles3));
        System.out.println();
        
        // 测试用例4：单个矩形
        int[][] rectangles4 = {{0,0,1,1}};
        System.out.println("测试用例4:");
        System.out.println("矩形: " + Arrays.deepToString(rectangles4));
        System.out.println("是否为完美矩形: " + solution.isRectangleCover(rectangles4));
    }
}