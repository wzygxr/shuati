package class143;

import java.util.*;

/**
 * LeetCode 773 - 滑动谜题
 * 题目描述：
 * 在一个 2x3 的板上（board）有 5 个砖块，以及一个空的格子。
 * 一次移动定义为选择空的格子与一个相邻的数字（上下左右）进行交换。
 * 最后当板 board 的结果是 [[1,2,3],[4,5,0]] 时，返回最少的移动次数；如果不存在这样的结果，返回 -1。
 * 
 * 算法：BFS算法
 * 时间复杂度：O(6! * 4) = O(720 * 4) = O(2880)，因为2x3的板共有6个位置，所以状态总数为6!
 * 空间复杂度：O(6!) = O(720)
 * 
 * 相关题目链接：
 * 1. LeetCode 773. 滑动谜题 - https://leetcode.cn/problems/sliding-puzzle/
 * 2. LeetCode 542. 01 矩阵 - https://leetcode.cn/problems/01-matrix/
 * 3. LeetCode 994. 腐烂的橘子 - https://leetcode.cn/problems/rotting-oranges/
 * 4. LeetCode 286. 墙与门 - https://leetcode.cn/problems/walls-and-gates/
 * 5. LeetCode 317. 离建筑物最近的距离 - https://leetcode.cn/problems/shortest-distance-from-all-buildings/
 * 6. LeetCode 417. 太平洋大西洋水流问题 - https://leetcode.cn/problems/pacific-atlantic-water-flow/
 * 7. LeetCode 529. 扫雷游戏 - https://leetcode.cn/problems/minesweeper/
 * 8. LeetCode 695. 岛屿的最大面积 - https://leetcode.cn/problems/max-area-of-island/
 * 9. LeetCode 733. 图像渲染 - https://leetcode.cn/problems/flood-fill/
 * 10. LeetCode 934. 最短的桥 - https://leetcode.cn/problems/shortest-bridge/
 * 11. 洛谷 P1162 走迷宫 - https://www.luogu.com.cn/problem/P1162
 * 12. 洛谷 P1443 马的遍历 - https://www.luogu.com.cn/problem/P1443
 * 13. POJ 3620 Avoid The Lakes - http://poj.org/problem?id=3620
 * 14. HDU 1241 Oil Deposits - https://acm.hdu.edu.cn/showproblem.php?pid=1241
 * 15. AtCoder ABC007 C - 幅優先探索 - https://atcoder.jp/contests/abc007/tasks/abc007_3
 */
public class LeetCode773_SlidingPuzzle {
    // 目标状态
    private static final String TARGET = "123450";
    // 记录每个位置可以移动到的位置（0-5对应board中的每个位置）
    // 将2x3的棋盘按行优先顺序编号为0-5：
    // 0 1 2
    // 3 4 5
    private static final int[][] DIRECTIONS = {
        {1, 3},       // 位置0可以移动到位置1和3
        {0, 2, 4},    // 位置1可以移动到位置0、2和4
        {1, 5},       // 位置2可以移动到位置1和5
        {0, 4},       // 位置3可以移动到位置0和4
        {1, 3, 5},    // 位置4可以移动到位置1、3和5
        {2, 4}        // 位置5可以移动到位置2和4
    };
    
    /**
     * 解决滑动谜题问题的主函数
     * 算法思路：
     * 1. 这是一个典型的BFS最短路径问题
     * 2. 将棋盘状态转换为字符串，便于存储和比较
     * 3. 使用BFS从初始状态开始搜索，直到找到目标状态
     * 4. 每一层BFS代表一步移动，所以层数就是移动次数
     * 
     * 具体实现：
     * 1. 将二维棋盘转换为一维字符串表示
     * 2. 使用队列存储待处理的状态
     * 3. 使用集合记录已访问的状态，避免重复处理
     * 4. 对每个状态，找到空格位置并尝试所有可能的移动
     * 5. 生成新状态并检查是否为目标状态
     * 
     * @param board 2x3的棋盘
     * @return 最少的移动次数，如果无解则返回-1
     */
    public int slidingPuzzle(int[][] board) {
        // 将棋盘转换为字符串表示
        // 按行优先顺序将二维数组转换为字符串
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(board[i][j]);
            }
        }
        String start = sb.toString();
        
        // 如果初始状态就是目标状态，直接返回0
        if (TARGET.equals(start)) {
            return 0;
        }
        
        // 使用双端队列实现BFS
        // 由于每次移动的代价都是1，所以这里可以简化为普通BFS
        Deque<String> deque = new LinkedList<>();
        // 记录已经访问过的状态，避免重复访问和无限循环
        Set<String> visited = new HashSet<>();
        
        // 将初始状态加入队列和已访问集合
        deque.offer(start);
        visited.add(start);
        
        int steps = 0; // 记录移动步数
        
        // BFS搜索
        while (!deque.isEmpty()) {
            // 获取当前层的状态数量
            int size = deque.size();
            steps++; // 增加步数
            
            // 处理当前层的所有状态
            for (int i = 0; i < size; i++) {
                // 取出一个状态
                String current = deque.poll();
                
                // 找到空格（0）的位置
                int zeroPos = current.indexOf('0');
                
                // 尝试所有可能的移动方向
                for (int dir : DIRECTIONS[zeroPos]) {
                    // 生成新的状态：将空格与相邻数字交换
                    String next = swap(current, zeroPos, dir);
                    
                    // 如果是目标状态，返回步数
                    if (TARGET.equals(next)) {
                        return steps;
                    }
                    
                    // 如果是新状态（未访问过），加入队列和已访问集合
                    if (!visited.contains(next)) {
                        visited.add(next);
                        deque.offer(next);
                    }
                }
            }
        }
        
        // 如果无法到达目标状态，返回-1
        return -1;
    }
    
    /**
     * 交换字符串中两个字符的位置
     * @param s 原始字符串
     * @param i 第一个位置
     * @param j 第二个位置
     * @return 交换后的新字符串
     */
    private String swap(String s, int i, int j) {
        // 将字符串转换为字符数组以便修改
        char[] chars = s.toCharArray();
        // 交换两个位置的字符
        char temp = chars[i];
        chars[i] = chars[j];
        chars[j] = temp;
        // 返回新的字符串
        return new String(chars);
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        LeetCode773_SlidingPuzzle solution = new LeetCode773_SlidingPuzzle();
        
        // 测试用例1
        int[][] board1 = {{1, 2, 3}, {4, 0, 5}};
        System.out.println("测试用例1结果: " + solution.slidingPuzzle(board1)); // 预期输出: 1
        
        // 测试用例2
        int[][] board2 = {{1, 2, 3}, {5, 4, 0}};
        System.out.println("测试用例2结果: " + solution.slidingPuzzle(board2)); // 预期输出: -1
        
        // 测试用例3
        int[][] board3 = {{4, 1, 2}, {5, 0, 3}};
        System.out.println("测试用例3结果: " + solution.slidingPuzzle(board3)); // 预期输出: 5
    }
}