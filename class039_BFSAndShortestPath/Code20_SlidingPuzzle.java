package class062;

import java.util.*;

// 滑动谜题
// 在一个 2 x 3 的板上（board）有 5 块砖瓦，用数字 1~5 来表示, 以及一块空缺用 0 来表示。
// 一次移动定义为选择 0 与一个相邻的数字（上下左右）进行交换。
// 最终当板 board 的结果是 [[1,2,3],[4,5,0]] 谜板被解开。
// 给出一个谜板的初始状态，返回最少可以通过多少次移动解开谜板，如果不能解开谜板，则返回 -1 。
// 测试链接 : https://leetcode.cn/problems/sliding-puzzle/
// 
// 算法思路：
// 使用BFS搜索从初始状态到目标状态的最短路径。将2x3的板状态表示为字符串进行状态搜索。
// 每个状态可以生成最多4个邻居状态（0可以向4个方向移动）。
// 
// 时间复杂度：O(6! * 4) = O(2880)，因为有6! = 720种可能的状态，每个状态最多有4个邻居
// 空间复杂度：O(720)，用于存储队列和访问状态
// 
// 工程化考量：
// 1. 状态表示：将2x3矩阵转换为字符串进行状态搜索
// 2. 邻居生成：根据0的位置生成可能的移动方向
// 3. 预计算移动方向：提高代码可读性和性能
// 4. 边界情况：初始状态就是目标状态
public class Code20_SlidingPuzzle {

    // 目标状态
    private static final String TARGET = "123450";
    
    // 每个位置可以移动到的邻居位置索引（预计算提高效率）
    private static final int[][] NEIGHBORS = {
        {1, 3},     // 位置0的邻居：1, 3
        {0, 2, 4},  // 位置1的邻居：0, 2, 4
        {1, 5},     // 位置2的邻居：1, 5
        {0, 4},     // 位置3的邻居：0, 4
        {1, 3, 5},  // 位置4的邻居：1, 3, 5
        {2, 4}      // 位置5的邻居：2, 4
    };
    
    public static int slidingPuzzle(int[][] board) {
        // 将初始状态转换为字符串
        String start = boardToString(board);
        
        // 边界情况：初始状态就是目标状态
        if (start.equals(TARGET)) {
            return 0;
        }
        
        // BFS队列和访问记录
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        
        queue.offer(start);
        visited.add(start);
        int steps = 0;
        
        while (!queue.isEmpty()) {
            steps++;
            int size = queue.size();
            
            // 处理当前层的所有状态
            for (int i = 0; i < size; i++) {
                String current = queue.poll();
                
                // 生成所有可能的邻居状态
                for (String neighbor : getNeighbors(current)) {
                    // 跳过已访问状态
                    if (visited.contains(neighbor)) {
                        continue;
                    }
                    
                    // 如果找到目标状态
                    if (neighbor.equals(TARGET)) {
                        return steps;
                    }
                    
                    // 加入队列并标记为已访问
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }
        
        // 无法到达目标状态
        return -1;
    }
    
    // 将2x3矩阵转换为字符串
    private static String boardToString(int[][] board) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(board[i][j]);
            }
        }
        return sb.toString();
    }
    
    // 生成当前状态的所有邻居状态
    private static List<String> getNeighbors(String state) {
        List<String> neighbors = new ArrayList<>();
        char[] chars = state.toCharArray();
        
        // 找到0的位置
        int zeroIndex = state.indexOf('0');
        
        // 遍历所有可能的移动方向
        for (int neighborIndex : NEIGHBORS[zeroIndex]) {
            // 交换0和邻居位置
            char[] newChars = chars.clone();
            newChars[zeroIndex] = newChars[neighborIndex];
            newChars[neighborIndex] = '0';
            neighbors.add(new String(newChars));
        }
        
        return neighbors;
    }
    
    // 单元测试
    public static void main(String[] args) {
        // 测试用例1：标准情况
        int[][] board1 = {{1,2,3},{4,0,5}};
        System.out.println("测试用例1 - 最小步数: " + slidingPuzzle(board1)); // 期望输出: 1
        
        // 测试用例2：需要多步
        int[][] board2 = {{1,2,3},{5,4,0}};
        System.out.println("测试用例2 - 最小步数: " + slidingPuzzle(board2)); // 期望输出: -1
        
        // 测试用例3：初始状态就是目标状态
        int[][] board3 = {{1,2,3},{4,5,0}};
        System.out.println("测试用例3 - 最小步数: " + slidingPuzzle(board3)); // 期望输出: 0
        
        // 测试用例4：复杂情况
        int[][] board4 = {{4,1,2},{5,0,3}};
        System.out.println("测试用例4 - 最小步数: " + slidingPuzzle(board4)); // 期望输出: 5
        
        // 测试用例5：无法解开的谜题
        int[][] board5 = {{1,2,3},{5,4,0}};
        System.out.println("测试用例5 - 最小步数: " + slidingPuzzle(board5)); // 期望输出: -1
    }
}