package class127;

public class TestMinimumPathSum {
    public static void main(String[] args) {
        // 测试用例1
        int[][] grid1 = {
            {1, 3, 1},
            {1, 5, 1},
            {4, 2, 1}
        };
        System.out.println("测试用例1结果: " + Code09_MinimumPathSum.minPathSum(grid1)); // 预期输出: 7
        
        // 测试用例2
        int[][] grid2 = {
            {1, 2, 3},
            {4, 5, 6}
        };
        System.out.println("测试用例2结果: " + Code09_MinimumPathSum.minPathSum(grid2)); // 预期输出: 12
        
        // 测试用例3
        int[][] grid3 = {
            {1, 2},
            {1, 1}
        };
        System.out.println("测试用例3结果: " + Code09_MinimumPathSum.minPathSum(grid3)); // 预期输出: 3
        
        // 测试用例4
        int[][] grid4 = {
            {1, 3, 1, 2},
            {1, 5, 1, 3},
            {4, 2, 1, 1}
        };
        System.out.println("测试用例4结果: " + Code09_MinimumPathSum.minPathSum(grid4)); // 预期输出: 8
    }
}