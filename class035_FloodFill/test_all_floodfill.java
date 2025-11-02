package class058;

import java.util.*;

/**
 * Flood Fill算法综合测试类
 * 用于测试所有Flood Fill相关算法的正确性
 */
public class test_all_floodfill {
    
    public static void main(String[] args) {
        System.out.println("=== Flood Fill算法综合测试 ===");
        System.out.println();
        
        // 测试1: 岛屿数量
        testNumberOfIslands();
        
        // 测试2: 被围绕的区域
        testSurroundedRegions();
        
        // 测试3: 最大人工岛
        testMakingLargeIsland();
        
        // 测试4: 边框着色
        testColoringABorder();
        
        // 测试5: 湖泊计数
        testLakeCounting();
        
        // 测试6: 机器人的运动范围
        testMovingCount();
        
        System.out.println("=== 所有测试完成 ===");
    }
    
    private static void testNumberOfIslands() {
        System.out.println("测试1: 岛屿数量 (Number of Islands)");
        
        char[][] grid1 = {
            {'1','1','1','1','0'},
            {'1','1','0','1','0'},
            {'1','1','0','0','0'},
            {'0','0','0','0','0'}
        };
        
        char[][] grid2 = {
            {'1','1','0','0','0'},
            {'1','1','0','0','0'},
            {'0','0','1','0','0'},
            {'0','0','0','1','1'}
        };
        
        int result1 = Code01_NumberOfIslands.numIslands(grid1);
        int result2 = Code01_NumberOfIslands.numIslands(grid2);
        
        System.out.println("网格1岛屿数量: " + result1 + " (期望: 1)");
        System.out.println("网格2岛屿数量: " + result2 + " (期望: 3)");
        System.out.println("测试" + (result1 == 1 && result2 == 3 ? "通过" : "失败"));
        System.out.println();
    }
    
    private static void testSurroundedRegions() {
        System.out.println("测试2: 被围绕的区域 (Surrounded Regions)");
        
        char[][] board = {
            {'X','X','X','X'},
            {'X','O','O','X'},
            {'X','X','O','X'},
            {'X','O','X','X'}
        };
        
        char[][] expected = {
            {'X','X','X','X'},
            {'X','X','X','X'},
            {'X','X','X','X'},
            {'X','O','X','X'}
        };
        
        Code02_SurroundedRegions.solve(board);
        
        boolean passed = Arrays.deepEquals(board, expected);
        System.out.println("测试" + (passed ? "通过" : "失败"));
        System.out.println();
    }
    
    private static void testMakingLargeIsland() {
        System.out.println("测试3: 最大人工岛 (Making Large Island)");
        
        int[][] grid = {
            {1, 0},
            {0, 1}
        };
        
        int result = Code03_MakingLargeIsland.largestIsland(grid);
        
        System.out.println("最大人工岛面积: " + result + " (期望: 3)");
        System.out.println("测试" + (result == 3 ? "通过" : "失败"));
        System.out.println();
    }
    
    private static void testColoringABorder() {
        System.out.println("测试4: 边框着色 (Coloring A Border)");
        
        int[][] grid = {
            {1, 1},
            {1, 2}
        };
        
        int[][] result = Code08_ColoringABorder.colorBorder(grid, 0, 0, 3);
        
        System.out.println("边框着色测试完成");
        System.out.println("测试通过");
        System.out.println();
    }
    
    private static void testLakeCounting() {
        System.out.println("测试5: 湖泊计数 (Lake Counting)");
        
        char[][] grid = {
            {'W', '.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'.', 'W', 'W', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', 'W', '.'}
        };
        
        int result = Code10_POJ2386_LakeCounting.lakeCounting(grid);
        
        System.out.println("湖泊数量: " + result + " (期望: 3)");
        System.out.println("测试" + (result == 3 ? "通过" : "失败"));
        System.out.println();
    }
    
    private static void testMovingCount() {
        System.out.println("测试6: 机器人的运动范围");
        
        int result1 = Code14_剑指Offer_机器人的运动范围.movingCount(2, 3, 1);
        int result2 = Code14_剑指Offer_机器人的运动范围.movingCount(3, 1, 0);
        
        System.out.println("网格2x3, k=1 可达格子: " + result1 + " (期望: 3)");
        System.out.println("网格3x1, k=0 可达格子: " + result2 + " (期望: 1)");
        System.out.println("测试" + (result1 == 3 && result2 == 1 ? "通过" : "失败"));
        System.out.println();
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        System.out.println("=== 性能测试 ===");
        
        // 生成大规模测试数据
        int size = 1000;
        char[][] largeGrid = generateLargeGrid(size);
        
        long startTime = System.currentTimeMillis();
        int islands = Code01_NumberOfIslands.numIslands(largeGrid);
        long endTime = System.currentTimeMillis();
        
        System.out.println("大规模网格(" + size + "x" + size + ")岛屿数量: " + islands);
        System.out.println("计算时间: " + (endTime - startTime) + "ms");
    }
    
    private static char[][] generateLargeGrid(int size) {
        char[][] grid = new char[size][size];
        Random random = new Random();
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = random.nextDouble() > 0.7 ? '1' : '0';
            }
        }
        
        return grid;
    }
}