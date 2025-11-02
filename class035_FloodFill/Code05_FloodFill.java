package class058;

/**
 * 图像渲染 (Flood Fill)
 * 来源: LeetCode 733
 * 题目链接: https://leetcode.cn/problems/flood-fill/
 * 
 * 题目描述:
 * 有一幅以 m x n 的二维整数数组表示的图画 image ，其中 image[i][j] 表示该图画的像素值大小。
 * 你也被给予三个整数 sr , sc 和 newColor 。你应该从像素 image[sr][sc] 开始对图像进行 上色填充 。
 * 为了完成 上色工作 ，从初始像素开始，记录初始坐标的 上下左右四个方向上 像素值与初始坐标相同的相连像素点，
 * 接着再记录这四个方向上符合条件的像素点与他们对应 四个方向上 像素值与初始坐标相同的相连像素点，……，重复该过程。
 * 将所有有记录的像素点的颜色值改为 newColor 。最后返回 经过上色渲染后的图像 。
 * 
 * 解题思路:
 * 使用Flood Fill算法，从起始点开始进行深度优先搜索(DFS)或广度优先搜索(BFS)，
 * 将所有与起始点相连且颜色相同的像素点修改为新颜色。
 * 
 * 时间复杂度: O(m*n) - 最坏情况下需要遍历整个图像
 * 空间复杂度: O(m*n) - 递归调用栈的深度最多为m*n
 * 是否最优解: 是
 * 
 * 工程化考量:
 * 1. 异常处理：检查输入是否为空，坐标是否越界
 * 2. 特殊情况：如果新颜色与原颜色相同，则直接返回原图像
 * 3. 可配置性：可以扩展支持8个方向的连接
 * 
 * 语言特性差异:
 * Java: 递归实现简洁，有自动内存管理
 * C++: 可以选择递归或使用栈手动实现
 * Python: 递归实现简洁，但有递归深度限制
 * 
 * 极端输入场景:
 * 1. 空图像
 * 2. 单像素图像
 * 3. 所有像素颜色相同
 * 4. 新颜色与原颜色相同
 * 
 * 性能优化:
 * 1. 提前判断新颜色与原颜色是否相同
 * 2. 使用方向数组简化代码
 * 3. 可以用BFS替代DFS避免栈溢出
 */
public class Code05_FloodFill {
    
    // 四个方向的偏移量：上、下、左、右
    private static final int[] dx = {-1, 1, 0, 0};
    private static final int[] dy = {0, 0, -1, 1};
    
    /**
     * 图像渲染主函数
     * 
     * @param image 二维图像数组
     * @param sr 起始行坐标
     * @param sc 起始列坐标
     * @param newColor 新颜色值
     * @return 渲染后的图像
     */
    public static int[][] floodFill(int[][] image, int sr, int sc, int newColor) {
        // 边界条件检查
        if (image == null || image.length == 0 || image[0].length == 0) {
            return image;
        }
        
        int rows = image.length;
        int cols = image[0].length;
        
        // 检查坐标是否越界
        if (sr < 0 || sr >= rows || sc < 0 || sc >= cols) {
            return image;
        }
        
        int originalColor = image[sr][sc];
        
        // 如果新颜色与原颜色相同，直接返回原图像
        if (originalColor == newColor) {
            return image;
        }
        
        // 执行Flood Fill算法
        dfs(image, sr, sc, originalColor, newColor, rows, cols);
        
        return image;
    }
    
    /**
     * 深度优先搜索实现Flood Fill
     * 
     * @param image 图像数组
     * @param x 当前行坐标
     * @param y 当前列坐标
     * @param originalColor 原始颜色
     * @param newColor 新颜色
     * @param rows 行数
     * @param cols 列数
     */
    private static void dfs(int[][] image, int x, int y, int originalColor, int newColor, int rows, int cols) {
        // 边界检查和颜色检查
        if (x < 0 || x >= rows || y < 0 || y >= cols || image[x][y] != originalColor) {
            return;
        }
        
        // 修改当前像素颜色
        image[x][y] = newColor;
        
        // 递归处理四个方向的相邻像素
        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            dfs(image, newX, newY, originalColor, newColor, rows, cols);
        }
    }
    
    /**
     * 广度优先搜索版本（避免递归深度问题）
     * 
     * @param image 图像数组
     * @param sr 起始行坐标
     * @param sc 起始列坐标
     * @param newColor 新颜色值
     * @return 渲染后的图像
     */
    public static int[][] floodFillBFS(int[][] image, int sr, int sc, int newColor) {
        if (image == null || image.length == 0 || image[0].length == 0) {
            return image;
        }
        
        int rows = image.length;
        int cols = image[0].length;
        
        if (sr < 0 || sr >= rows || sc < 0 || sc >= cols) {
            return image;
        }
        
        int originalColor = image[sr][sc];
        
        if (originalColor == newColor) {
            return image;
        }
        
        java.util.Queue<int[]> queue = new java.util.LinkedList<>();
        queue.offer(new int[]{sr, sc});
        image[sr][sc] = newColor;
        
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int x = cell[0], y = cell[1];
            
            for (int i = 0; i < 4; i++) {
                int newX = x + dx[i];
                int newY = y + dy[i];
                
                if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && image[newX][newY] == originalColor) {
                    image[newX][newY] = newColor;
                    queue.offer(new int[]{newX, newY});
                }
            }
        }
        
        return image;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[][] image1 = {{1,1,1},{1,1,0},{1,0,1}};
        System.out.println("测试用例1 - 标准图像渲染:");
        System.out.println("原图像:");
        printImage(image1);
        
        int[][] result1 = floodFill(copyImage(image1), 1, 1, 2);
        System.out.println("DFS版本渲染后:");
        printImage(result1);
        
        int[][] result1BFS = floodFillBFS(copyImage(image1), 1, 1, 2);
        System.out.println("BFS版本渲染后:");
        printImage(result1BFS);
        
        // 测试用例2
        int[][] image2 = {{0,0,0},{0,0,0}};
        System.out.println("\n测试用例2 - 全0图像:");
        System.out.println("原图像:");
        printImage(image2);
        
        int[][] result2 = floodFill(copyImage(image2), 0, 0, 2);
        System.out.println("渲染后:");
        printImage(result2);
    }
    
    // 辅助方法：打印图像
    private static void printImage(int[][] image) {
        if (image == null || image.length == 0) {
            System.out.println("空图像");
            return;
        }
        
        for (int[] row : image) {
            for (int pixel : row) {
                System.out.print(pixel + " ");
            }
            System.out.println();
        }
    }
    
    // 辅助方法：复制图像
    private static int[][] copyImage(int[][] image) {
        if (image == null) return null;
        int[][] copy = new int[image.length][];
        for (int i = 0; i < image.length; i++) {
            copy[i] = image[i].clone();
        }
        return copy;
    }
}