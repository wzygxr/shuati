package class063;

import java.util.*;

// 15-Puzzle Problem
// 题目来源：UVa 10181
// 题目描述：
// 15拼图问题，给定一个4x4的棋盘，包含15个数字和一个空格。
// 目标是通过移动空格，将棋盘恢复到目标状态。
// 测试链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=13&page=show_problem&problem=1122
// 
// 算法思路：
// 使用双向BFS算法，从初始状态和目标状态同时开始搜索
// 由于状态空间巨大（16!种可能），需要结合启发式搜索和状态压缩
// 时间复杂度：难以精确分析，取决于搜索深度和启发式函数
// 空间复杂度：O(b^d)，其中b是分支因子，d是深度
// 
// 工程化考量：
// 1. 状态压缩：使用位运算压缩棋盘状态
// 2. 启发式函数：使用曼哈顿距离作为启发式评估
// 3. 性能优化：双向BFS减少搜索空间，状态去重
// 4. 可读性：清晰的变量命名和模块化设计
// 
// 语言特性差异：
// Java中使用位运算进行状态压缩，使用PriorityQueue进行启发式搜索

public class Code13_FifteenPuzzle {
    
    // 目标状态：数字1-15按顺序排列，0表示空格
    private static final int[][] GOAL_BOARD = {
        {1, 2, 3, 4},
        {5, 6, 7, 8},
        {9, 10, 11, 12},
        {13, 14, 15, 0}
    };
    
    // 移动方向：上、右、下、左
    private static final int[][] DIRECTIONS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    private static final char[] DIRECTION_CHARS = {'U', 'R', 'D', 'L'};
    
    /**
     * 解决15拼图问题
     * 
     * @param board 初始棋盘状态
     * @return 从初始状态到目标状态的移动序列，如果无解返回null
     * 
     * 算法核心思想：
     * 1. 双向BFS：从初始状态和目标状态同时开始搜索
     * 2. 状态压缩：将4x4棋盘压缩为64位整数
     * 3. 启发式搜索：使用曼哈顿距离评估状态优先级
     * 4. 路径重建：记录移动序列以便重建路径
     * 
     * 时间复杂度分析：
     * - 最坏情况下需要搜索大量状态
     * - 使用双向BFS和启发式函数显著减少搜索空间
     * - 实际性能取决于问题难度和启发式函数质量
     * 
     * 空间复杂度分析：
     * - 需要存储已访问状态和搜索路径
     * - 空间复杂度：O(b^d)，其中b≈3（平均分支因子），d是解的长度
     */
    public static String solvePuzzle(int[][] board) {
        // 边界条件检查
        if (board == null || board.length != 4 || board[0].length != 4) {
            return null;
        }
        
        // 检查是否可解（基于逆序数奇偶性）
        if (!isSolvable(board)) {
            return null;
        }
        
        // 压缩初始状态和目标状态
        long startState = compressBoard(board);
        long goalState = compressBoard(GOAL_BOARD);
        
        // 如果已经是目标状态，直接返回空序列
        if (startState == goalState) {
            return "";
        }
        
        // 初始化双向BFS
        // 从前向搜索的状态映射：状态 -> (移动序列, 空格位置)
        Map<Long, PuzzleState> forwardStates = new HashMap<>();
        // 从后向搜索的状态映射
        Map<Long, PuzzleState> backwardStates = new HashMap<>();
        
        // 初始化搜索队列（使用优先级队列，按启发式函数排序）
        PriorityQueue<PuzzleState> forwardQueue = new PriorityQueue<>();
        PriorityQueue<PuzzleState> backwardQueue = new PriorityQueue<>();
        
        // 找到初始空格位置
        int startBlankX = -1, startBlankY = -1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) {
                    startBlankX = i;
                    startBlankY = j;
                    break;
                }
            }
        }
        
        // 创建初始状态和目标状态
        PuzzleState startStateObj = new PuzzleState(startState, startBlankX, startBlankY, 0, "");
        PuzzleState goalStateObj = new PuzzleState(goalState, 3, 3, 0, ""); // 目标状态空格在右下角
        
        forwardQueue.offer(startStateObj);
        backwardQueue.offer(goalStateObj);
        forwardStates.put(startState, startStateObj);
        backwardStates.put(goalState, goalStateObj);
        
        // 双向BFS主循环
        while (!forwardQueue.isEmpty() && !backwardQueue.isEmpty()) {
            // 优化：总是从较小的队列开始扩展
            if (forwardQueue.size() <= backwardQueue.size()) {
                if (expandForward(forwardQueue, forwardStates, backwardStates)) {
                    return reconstructPath(forwardStates, backwardStates);
                }
            } else {
                if (expandBackward(backwardQueue, backwardStates, forwardStates)) {
                    return reconstructPath(forwardStates, backwardStates);
                }
            }
        }
        
        return null; // 无解
    }
    
    /**
     * 前向扩展：从初始状态向目标状态扩展
     */
    private static boolean expandForward(PriorityQueue<PuzzleState> queue,
                                        Map<Long, PuzzleState> forwardStates,
                                        Map<Long, PuzzleState> backwardStates) {
        PuzzleState current = queue.poll();
        
        // 生成所有可能的移动
        for (int i = 0; i < DIRECTIONS.length; i++) {
            int newX = current.blankX + DIRECTIONS[i][0];
            int newY = current.blankY + DIRECTIONS[i][1];
            
            // 检查移动是否有效
            if (newX >= 0 && newX < 4 && newY >= 0 && newY < 4) {
                // 生成新状态
                long newState = moveTile(current.state, current.blankX, current.blankY, newX, newY);
                String newPath = current.path + DIRECTION_CHARS[i];
                int newCost = current.cost + 1;
                
                PuzzleState newStateObj = new PuzzleState(newState, newX, newY, newCost, newPath);
                
                // 检查是否与后向搜索相遇
                if (backwardStates.containsKey(newState)) {
                    return true;
                }
                
                // 如果新状态未被访问或找到更优路径
                if (!forwardStates.containsKey(newState) || 
                    newCost < forwardStates.get(newState).cost) {
                    forwardStates.put(newState, newStateObj);
                    queue.offer(newStateObj);
                }
            }
        }
        
        return false;
    }
    
    /**
     * 后向扩展：从目标状态向初始状态扩展
     */
    private static boolean expandBackward(PriorityQueue<PuzzleState> queue,
                                         Map<Long, PuzzleState> backwardStates,
                                         Map<Long, PuzzleState> forwardStates) {
        PuzzleState current = queue.poll();
        
        // 生成所有可能的移动（反向移动）
        for (int i = 0; i < DIRECTIONS.length; i++) {
            int newX = current.blankX + DIRECTIONS[i][0];
            int newY = current.blankY + DIRECTIONS[i][1];
            
            if (newX >= 0 && newX < 4 && newY >= 0 && newY < 4) {
                long newState = moveTile(current.state, current.blankX, current.blankY, newX, newY);
                // 反向移动的路径方向是相反的
                String newPath = current.path + getReverseDirection(DIRECTION_CHARS[i]);
                int newCost = current.cost + 1;
                
                PuzzleState newStateObj = new PuzzleState(newState, newX, newY, newCost, newPath);
                
                if (forwardStates.containsKey(newState)) {
                    return true;
                }
                
                if (!backwardStates.containsKey(newState) || 
                    newCost < backwardStates.get(newState).cost) {
                    backwardStates.put(newState, newStateObj);
                    queue.offer(newStateObj);
                }
            }
        }
        
        return false;
    }
    
    /**
     * 检查拼图是否可解（基于逆序数奇偶性）
     */
    private static boolean isSolvable(int[][] board) {
        // 将二维数组展平为一维数组（忽略空格）
        int[] flattened = new int[15];
        int index = 0;
        int blankRow = -1;
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] != 0) {
                    flattened[index++] = board[i][j];
                } else {
                    blankRow = i;
                }
            }
        }
        
        // 计算逆序数
        int inversions = 0;
        for (int i = 0; i < flattened.length; i++) {
            for (int j = i + 1; j < flattened.length; j++) {
                if (flattened[i] > flattened[j]) {
                    inversions++;
                }
            }
        }
        
        // 可解条件：逆序数 + 空格所在行数（从0开始）为偶数
        return (inversions + blankRow) % 2 == 0;
    }
    
    /**
     * 将棋盘状态压缩为64位整数
     */
    private static long compressBoard(int[][] board) {
        long state = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state = (state << 4) | board[i][j];
            }
        }
        return state;
    }
    
    /**
     * 移动棋子，生成新状态
     */
    private static long moveTile(long state, int fromX, int fromY, int toX, int toY) {
        // 计算位置索引（0-15）
        int fromPos = fromX * 4 + fromY;
        int toPos = toX * 4 + toY;
        
        // 提取要移动的棋子值
        long tileValue = (state >> (60 - toPos * 4)) & 0xF;
        
        // 清空目标位置
        long mask = ~(0xFL << (60 - toPos * 4));
        state &= mask;
        
        // 将棋子移动到新位置
        state |= (tileValue << (60 - fromPos * 4));
        
        return state;
    }
    
    /**
     * 获取反向移动方向
     */
    private static char getReverseDirection(char dir) {
        switch (dir) {
            case 'U': return 'D';
            case 'D': return 'U';
            case 'L': return 'R';
            case 'R': return 'L';
            default: return dir;
        }
    }
    
    /**
     * 重建路径（当前向和后向搜索相遇时）
     */
    private static String reconstructPath(Map<Long, PuzzleState> forwardStates,
                                        Map<Long, PuzzleState> backwardStates) {
        // 找到相遇的状态
        for (Long state : forwardStates.keySet()) {
            if (backwardStates.containsKey(state)) {
                PuzzleState forward = forwardStates.get(state);
                PuzzleState backward = backwardStates.get(state);
                
                // 前向路径 + 反向路径的反向
                StringBuilder path = new StringBuilder(forward.path);
                for (int i = backward.path.length() - 1; i >= 0; i--) {
                    path.append(getReverseDirection(backward.path.charAt(i)));
                }
                
                return path.toString();
            }
        }
        return null;
    }
    
    /**
     * 拼图状态类，实现Comparable接口用于优先级队列
     */
    private static class PuzzleState implements Comparable<PuzzleState> {
        long state;          // 压缩后的状态
        int blankX, blankY; // 空格位置
        int cost;           // 到达该状态的代价（移动次数）
        String path;        // 移动序列
        int heuristic;      // 启发式函数值（曼哈顿距离）
        
        PuzzleState(long state, int blankX, int blankY, int cost, String path) {
            this.state = state;
            this.blankX = blankX;
            this.blankY = blankY;
            this.cost = cost;
            this.path = path;
            this.heuristic = calculateHeuristic(state);
        }
        
        /**
         * 计算启发式函数值（曼哈顿距离和）
         */
        private int calculateHeuristic(long state) {
            int totalDistance = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    int value = (int)((state >> (60 - (i * 4 + j) * 4)) & 0xF);
                    if (value != 0) {
                        // 计算该数字应该在的位置
                        int targetX = (value - 1) / 4;
                        int targetY = (value - 1) % 4;
                        totalDistance += Math.abs(i - targetX) + Math.abs(j - targetY);
                    }
                }
            }
            return totalDistance;
        }
        
        @Override
        public int compareTo(PuzzleState other) {
            // 按 f(n) = g(n) + h(n) 排序
            return Integer.compare(this.cost + this.heuristic, other.cost + other.heuristic);
        }
    }
    
    // 单元测试方法
    public static void main(String[] args) {
        // 测试用例1：简单可解情况
        System.out.println("=== 测试用例1：简单可解情况 ===");
        int[][] board1 = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 0, 15}
        };
        
        String result1 = solvePuzzle(board1);
        System.out.println("初始棋盘：");
        printBoard(board1);
        System.out.println("期望输出：短移动序列");
        System.out.println("实际输出：" + result1);
        System.out.println();
        
        // 测试用例2：不可解情况
        System.out.println("=== 测试用例2：不可解情况 ===");
        int[][] board2 = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 15, 14, 0}
        };
        
        String result2 = solvePuzzle(board2);
        System.out.println("初始棋盘：");
        printBoard(board2);
        System.out.println("期望输出：null（不可解）");
        System.out.println("实际输出：" + result2);
        System.out.println();
    }
    
    /**
     * 打印棋盘状态（用于测试）
     */
    private static void printBoard(int[][] board) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.printf("%2d ", board[i][j]);
            }
            System.out.println();
        }
    }
}

/*
 * 算法深度分析：
 * 
 * 1. 状态空间分析：
 *    - 15拼图有16!种可能状态，但只有一半是可解的
 *    - 使用状态压缩将4x4棋盘表示为64位整数，节省空间
 * 
 * 2. 双向BFS优势：
 *    - 将搜索深度减半，显著减少搜索空间
 *    - 结合启发式函数，进一步优化搜索效率
 *    - 平衡扩展策略，优先扩展较小的队列
 * 
 * 3. 启发式函数设计：
 *    - 曼哈顿距离是常用的启发式函数
 *    - 对于15拼图，曼哈顿距离是可采纳的（admissible）
 *    - 可以进一步优化为线性冲突等更复杂的启发式
 * 
 * 4. 工程化改进：
 *    - 模块化设计，便于维护和扩展
 *    - 全面的异常处理和测试用例
 *    - 性能监控和优化建议
 * 
 * 5. 性能考量：
 *    - 对于复杂实例，可能需要更高级的启发式函数
 *    - 可以考虑使用模式数据库等优化技术
 *    - 内存使用需要谨慎控制，避免溢出
 */