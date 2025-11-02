package class008_AdvancedAlgorithmsAndDataStructures.closest_pair_problems;

import java.util.*;

/**
 * LeetCode 149. Max Points on a Line
 * 
 * 题目描述：
 * 给你一个数组 points ，其中 points[i] = [xi, yi] 表示 X-Y 平面上的一个点。
 * 求最多有多少个点在同一条直线上。
 * 
 * 解题思路：
 * 对于每个点，计算它与其他所有点形成的直线斜率，统计相同斜率的点的数量。
 * 为了避免精度问题，使用分数形式表示斜率，并进行约分。
 * 
 * 时间复杂度：O(n^2)
 * 空间复杂度：O(n)
 */
public class LeetCode_149_MaxPointsOnALine {
    
    static class Solution {
        public int maxPoints(int[][] points) {
            if (points == null || points.length == 0) {
                return 0;
            }
            
            int n = points.length;
            if (n <= 2) {
                return n;
            }
            
            int maxPoints = 2;
            
            // 对于每个点，计算它与其他点形成的直线
            for (int i = 0; i < n; i++) {
                // 使用哈希表统计相同斜率的点数
                Map<String, Integer> slopeCount = new HashMap<>();
                int duplicate = 0; // 重复点的数量
                int currentMax = 0; // 当前点作为起点时的最大点数
                
                for (int j = i + 1; j < n; j++) {
                    int dx = points[j][0] - points[i][0];
                    int dy = points[j][1] - points[i][1];
                    
                    // 处理重复点
                    if (dx == 0 && dy == 0) {
                        duplicate++;
                        continue;
                    }
                    
                    // 计算斜率并约分
                    String slope = getSlope(dx, dy);
                    slopeCount.put(slope, slopeCount.getOrDefault(slope, 0) + 1);
                    currentMax = Math.max(currentMax, slopeCount.get(slope));
                }
                
                // 更新全局最大值
                maxPoints = Math.max(maxPoints, currentMax + duplicate + 1);
            }
            
            return maxPoints;
        }
        
        // 获取斜率的字符串表示（约分后的分数形式）
        private String getSlope(int dx, int dy) {
            if (dx == 0) {
                return "vertical"; // 垂直线
            }
            
            if (dy == 0) {
                return "0"; // 水平线
            }
            
            // 约分
            int gcd = gcd(Math.abs(dx), Math.abs(dy));
            dx /= gcd;
            dy /= gcd;
            
            // 保证分母为正
            if (dx < 0) {
                dx = -dx;
                dy = -dy;
            }
            
            return dy + "/" + dx;
        }
        
        // 计算最大公约数
        private int gcd(int a, int b) {
            return b == 0 ? a : gcd(b, a % b);
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例1
        int[][] points1 = {{1,1},{2,2},{3,3}};
        System.out.println("测试用例1:");
        System.out.println("点集: " + Arrays.deepToString(points1));
        System.out.println("结果: " + solution.maxPoints(points1));
        System.out.println();
        
        // 测试用例2
        int[][] points2 = {{1,1},{3,2},{5,3},{4,1},{2,3},{1,4}};
        System.out.println("测试用例2:");
        System.out.println("点集: " + Arrays.deepToString(points2));
        System.out.println("结果: " + solution.maxPoints(points2));
        System.out.println();
        
        // 测试用例3
        int[][] points3 = {{0,0},{1,1},{0,0}};
        System.out.println("测试用例3:");
        System.out.println("点集: " + Arrays.deepToString(points3));
        System.out.println("结果: " + solution.maxPoints(points3));
        System.out.println();
        
        // 测试用例4
        int[][] points4 = {{1,1},{1,1},{2,2},{2,2}};
        System.out.println("测试用例4:");
        System.out.println("点集: " + Arrays.deepToString(points4));
        System.out.println("结果: " + solution.maxPoints(points4));
    }
}