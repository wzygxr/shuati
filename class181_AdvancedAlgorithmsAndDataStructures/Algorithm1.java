package class185;

import java.util.*;

/**
 * 高级算法与数据结构实现 - Java版本
 * 包含：
 * 1. 平面分治 (Closest Pair of Points)
 * 2. 棋盘模拟 (Game of Life)
 * 3. 间隔打表 (Sparse Table) - 离线查询优化和在线算法适配
 * 4. 事件排序 (Time Sweep)
 * 5. 差分驱动模拟 (Difference Array)
 *
 * 算法复杂度分析：
 * - 平面分治：O(n log n) 时间复杂度
 * - 棋盘模拟：O(m*n) 时间复杂度/代
 * - 间隔打表：O(n log n) 预处理，O(1) 查询
 * - 事件排序：O(n log n) 时间复杂度
 * - 差分驱动：O(n + k) 时间复杂度
 */
public class Algorithm1 {
    
    // ================================
    // 1. 平面分治 - 最近点对问题
    // ================================
    
    /**
     * 点类，用于存储二维坐标
     */
    static class Point {
        double x, y;
        
        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
        
        /**
         * 计算两个点之间的欧几里得距离
         */
        public double distanceTo(Point p) {
            double dx = this.x - p.x;
            double dy = this.y - p.y;
            return Math.sqrt(dx * dx + dy * dy);
        }
        
        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
    
    /**
     * 最近点对问题的平面分治算法实现
     */
    public static class ClosestPair {
        /**
         * 查找最近点对
         * @param points 点集
         * @return 包含最近点对和距离的数组
         */
        public static Object[] findClosestPair(Point[] points) {
            if (points == null || points.length < 2) {
                throw new IllegalArgumentException("点集必须包含至少两个点");
            }
            
            // 按照x坐标排序
            Point[] pointsSortedByX = points.clone();
            Arrays.sort(pointsSortedByX, Comparator.comparingDouble(p -> p.x));
            
            // 按照y坐标排序（用于后续处理）
            Point[] pointsSortedByY = pointsSortedByX.clone();
            Arrays.sort(pointsSortedByY, Comparator.comparingDouble(p -> p.y));
            
            // 记录最近点对
            Point[] closestPair = new Point[2];
            double minDistance = closestPairRecursive(pointsSortedByX, 0, pointsSortedByX.length - 1, 
                                                    pointsSortedByY, closestPair);
            
            return new Object[]{closestPair, minDistance};
        }
        
        /**
         * 递归求解最近点对
         */
        private static double closestPairRecursive(Point[] pointsSortedByX, int left, int right, 
                                                 Point[] pointsSortedByY, Point[] closestPair) {
            // 基本情况：小规模问题直接暴力求解
            if (right - left <= 3) {
                return bruteForce(pointsSortedByX, left, right, closestPair);
            }
            
            // 分治求解
            int mid = left + (right - left) / 2;
            Point midPoint = pointsSortedByX[mid];
            
            // 分割y排序的数组
            Point[] leftPointsSortedByY = new Point[mid - left + 1];
            Point[] rightPointsSortedByY = new Point[right - mid];
            
            int leftIdx = 0, rightIdx = 0;
            for (Point p : pointsSortedByY) {
                if (p.x <= midPoint.x && leftIdx < leftPointsSortedByY.length) {
                    leftPointsSortedByY[leftIdx++] = p;
                } else {
                    rightPointsSortedByY[rightIdx++] = p;
                }
            }
            
            // 递归求解左右子数组
            Point[] leftClosestPair = new Point[2];
            Point[] rightClosestPair = new Point[2];
            
            double leftMin = closestPairRecursive(pointsSortedByX, left, mid, leftPointsSortedByY, leftClosestPair);
            double rightMin = closestPairRecursive(pointsSortedByX, mid + 1, right, rightPointsSortedByY, rightClosestPair);
            
            // 确定左右子数组中的最小距离
            double minDist;
            if (leftMin < rightMin) {
                minDist = leftMin;
                closestPair[0] = leftClosestPair[0];
                closestPair[1] = leftClosestPair[1];
            } else {
                minDist = rightMin;
                closestPair[0] = rightClosestPair[0];
                closestPair[1] = rightClosestPair[1];
            }
            
            // 处理跨越中线的点对
            // 筛选出在中线附近的点
            List<Point> strip = new ArrayList<>();
            for (Point p : pointsSortedByY) {
                if (Math.abs(p.x - midPoint.x) < minDist) {
                    strip.add(p);
                }
            }
            
            // 检查strip中的点对
            double stripMin = checkStrip(strip, minDist, closestPair);
            
            return Math.min(minDist, stripMin);
        }
        
        /**
         * 暴力求解小规模问题
         */
        private static double bruteForce(Point[] points, int left, int right, Point[] closestPair) {
            double minDist = Double.MAX_VALUE;
            
            for (int i = left; i <= right; i++) {
                for (int j = i + 1; j <= right; j++) {
                    double dist = points[i].distanceTo(points[j]);
                    if (dist < minDist) {
                        minDist = dist;
                        closestPair[0] = points[i];
                        closestPair[1] = points[j];
                    }
                }
            }
            
            return minDist;
        }
        
        /**
         * 检查跨越中线的点对
         */
        private static double checkStrip(List<Point> strip, double minDist, Point[] closestPair) {
            double currentMin = minDist;
            
            // 按照y坐标排序（已经是排序好的）
            // 只需要检查相邻的最多7个点
            for (int i = 0; i < strip.size(); i++) {
                for (int j = i + 1; j < strip.size() && (strip.get(j).y - strip.get(i).y) < currentMin; j++) {
                    double dist = strip.get(i).distanceTo(strip.get(j));
                    if (dist < currentMin) {
                        currentMin = dist;
                        closestPair[0] = strip.get(i);
                        closestPair[1] = strip.get(j);
                    }
                }
            }
            
            return currentMin;
        }
        
        /**
         * 测试最近点对算法
         */
        public static void testClosestPair() {
            System.out.println("=== 测试最近点对算法 ===");
            
            // 测试用例1：随机点集
            Point[] points1 = {
                new Point(2, 3),
                new Point(12, 30),
                new Point(40, 50),
                new Point(5, 1),
                new Point(12, 10),
                new Point(3, 4)
            };
            
            Object[] result1 = findClosestPair(points1);
            Point[] pair1 = (Point[]) result1[0];
            double dist1 = (double) result1[1];
            System.out.println("最近点对1: " + pair1[0] + " 和 " + pair1[1]);
            System.out.println("距离: " + dist1);
            
            // 测试用例2：所有点在一条直线上
            Point[] points2 = {
                new Point(0, 0),
                new Point(1, 0),
                new Point(2, 0),
                new Point(3, 0),
                new Point(100, 0)
            };
            
            Object[] result2 = findClosestPair(points2);
            Point[] pair2 = (Point[]) result2[0];
            double dist2 = (double) result2[1];
            System.out.println("最近点对2: " + pair2[0] + " 和 " + pair2[1]);
            System.out.println("距离: " + dist2);
            
            // 测试用例3：边界情况
            Point[] points3 = {
                new Point(0, 0),
                new Point(0, 0)  // 重复点
            };
            
            Object[] result3 = findClosestPair(points3);
            Point[] pair3 = (Point[]) result3[0];
            double dist3 = (double) result3[1];
            System.out.println("最近点对3: " + pair3[0] + " 和 " + pair3[1]);
            System.out.println("距离: " + dist3);
        }
    }
    
    // ================================
    // 2. 棋盘模拟 - 康威生命游戏
    // ================================
    
    /**
     * 康威生命游戏模拟实现
     */
    public static class GameOfLife {
        private int[][] board;
        private int rows, cols;
        
        /**
         * 构造函数
         * @param initialBoard 初始棋盘状态
         */
        public GameOfLife(int[][] initialBoard) {
            if (initialBoard == null || initialBoard.length == 0 || initialBoard[0].length == 0) {
                throw new IllegalArgumentException("初始棋盘不能为空");
            }
            
            this.rows = initialBoard.length;
            this.cols = initialBoard[0].length;
            this.board = new int[rows][cols];
            
            // 深拷贝初始棋盘
            for (int i = 0; i < rows; i++) {
                System.arraycopy(initialBoard[i], 0, this.board[i], 0, cols);
            }
        }
        
        /**
         * 计算下一代的状态
         * 时间复杂度：O(m*n)
         * 空间复杂度：O(m*n)
         */
        public void nextGeneration() {
            // 创建新的棋盘来存储下一代状态
            int[][] newBoard = new int[rows][cols];
            
            // 遍历每个细胞
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    // 计算周围活细胞数量
                    int liveNeighbors = countLiveNeighbors(i, j);
                    
                    // 应用生命游戏规则
                    if (board[i][j] == 1) {
                        // 活细胞
                        if (liveNeighbors < 2 || liveNeighbors > 3) {
                            newBoard[i][j] = 0;  // 死亡
                        } else {
                            newBoard[i][j] = 1;  // 存活
                        }
                    } else {
                        // 死细胞
                        if (liveNeighbors == 3) {
                            newBoard[i][j] = 1;  // 繁殖
                        } else {
                            newBoard[i][j] = 0;  // 保持死亡
                        }
                    }
                }
            }
            
            // 更新棋盘
            this.board = newBoard;
        }
        
        /**
         * 使用原地算法计算下一代状态
         * 时间复杂度：O(m*n)
         * 空间复杂度：O(1)
         * 使用特殊标记：2表示从活到死，-1表示从死到活
         */
        public void nextGenerationInPlace() {
            // 遍历每个细胞
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    // 计算周围活细胞数量
                    int liveNeighbors = countLiveNeighborsWithMarkers(i, j);
                    
                    // 应用生命游戏规则
                    if (board[i][j] == 1) {
                        // 活细胞
                        if (liveNeighbors < 2 || liveNeighbors > 3) {
                            board[i][j] = 2;  // 标记为从活到死
                        }
                        // 否则保持为1，继续存活
                    } else {
                        // 死细胞
                        if (liveNeighbors == 3) {
                            board[i][j] = -1;  // 标记为从死到活
                        }
                        // 否则保持为0，继续死亡
                    }
                }
            }
            
            // 解析标记，恢复真实状态
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (board[i][j] == 2) {
                        board[i][j] = 0;  // 死亡
                    } else if (board[i][j] == -1) {
                        board[i][j] = 1;  // 新生
                    }
                }
            }
        }
        
        /**
         * 计算指定位置周围的活细胞数量
         */
        private int countLiveNeighbors(int row, int col) {
            int count = 0;
            // 8个方向的偏移
            int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},          {0, 1},
                {1, -1},  {1, 0}, {1, 1}
            };
            
            for (int[] dir : directions) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                
                // 检查边界并计算活细胞
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                    if (board[newRow][newCol] == 1) {
                        count++;
                    }
                }
            }
            
            return count;
        }
        
        /**
         * 在原地算法中计算周围的活细胞数量（考虑标记）
         */
        private int countLiveNeighborsWithMarkers(int row, int col) {
            int count = 0;
            int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},          {0, 1},
                {1, -1},  {1, 0}, {1, 1}
            };
            
            for (int[] dir : directions) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                    // 1或2表示之前是活细胞
                    if (board[newRow][newCol] == 1 || board[newRow][newCol] == 2) {
                        count++;
                    }
                }
            }
            
            return count;
        }
        
        /**
         * 获取当前棋盘状态
         */
        public int[][] getBoard() {
            int[][] copy = new int[rows][cols];
            for (int i = 0; i < rows; i++) {
                System.arraycopy(board[i], 0, copy[i], 0, cols);
            }
            return copy;
        }
        
        /**
         * 打印棋盘状态
         */
        public void printBoard() {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    System.out.print(board[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
        
        /**
         * 测试生命游戏
         */
        public static void testGameOfLife() {
            System.out.println("=== 测试康威生命游戏 ===");
            
            // 测试用例：闪烁器（Blinker）
            int[][] blinker = {
                {0, 1, 0},
                {0, 1, 0},
                {0, 1, 0}
            };
            
            System.out.println("初始状态:");
            GameOfLife game = new GameOfLife(blinker);
            game.printBoard();
            
            System.out.println("第1代（原地算法）:");
            game.nextGenerationInPlace();
            game.printBoard();
            
            System.out.println("第2代（原地算法）:");
            game.nextGenerationInPlace();
            game.printBoard();
            
            // 测试用例：滑翔机（Glider）
            int[][] glider = {
                {0, 1, 0},
                {0, 0, 1},
                {1, 1, 1}
            };
            
            System.out.println("滑翔机 - 初始状态:");
            GameOfLife gliderGame = new GameOfLife(glider);
            gliderGame.printBoard();
            
            for (int i = 1; i <= 4; i++) {
                System.out.println("滑翔机 - 第" + i + "代:");
                gliderGame.nextGeneration();
                gliderGame.printBoard();
            }
        }
    }
    
    // ================================
    // 3. 间隔打表 - 稀疏表实现
    // ================================
    
    /**
     * 稀疏表（Sparse Table）实现
     * 支持O(1)时间的区间查询操作
     */
    public static class SparseTable {
        private int[][] st;      // 稀疏表数组
        private int[] logTable;  // 预计算的log值表
        private int[] data;      // 原始数据
        private boolean isMin;   // 是否是最小值查询（true）或最大值查询（false）
        
        /**
         * 构造函数 - 最小值查询
         * @param data 输入数组
         */
        public SparseTable(int[] data) {
            this(data, true);
        }
        
        /**
         * 构造函数
         * @param data 输入数组
         * @param isMin 是否是最小值查询
         */
        public SparseTable(int[] data, boolean isMin) {
            if (data == null || data.length == 0) {
                throw new IllegalArgumentException("输入数组不能为空");
            }
            
            this.data = data;
            this.isMin = isMin;
            int n = data.length;
            
            // 计算log表
            precomputeLogTable(n);
            
            // 计算稀疏表
            int k = logTable[n] + 1;
            st = new int[k][n];
            
            // 初始化第一行（区间长度为1）
            for (int i = 0; i < n; i++) {
                st[0][i] = i;  // 存储索引而不是值，便于范围查询
            }
            
            // 填充其他行
            for (int j = 1; j < k; j++) {
                for (int i = 0; i <= n - (1 << j); i++) {
                    int prevLen = 1 << (j - 1);
                    int left = st[j - 1][i];
                    int right = st[j - 1][i + prevLen];
                    
                    // 根据查询类型选择最小或最大值
                    if (isMin) {
                        st[j][i] = (data[left] <= data[right]) ? left : right;
                    } else {
                        st[j][i] = (data[left] >= data[right]) ? left : right;
                    }
                }
            }
        }
        
        /**
         * 预计算log2值表
         */
        private void precomputeLogTable(int n) {
            logTable = new int[n + 1];
            logTable[1] = 0;
            for (int i = 2; i <= n; i++) {
                logTable[i] = logTable[i / 2] + 1;
            }
        }
        
        /**
         * 区间查询操作
         * 时间复杂度：O(1)
         * @param l 左边界（包含）
         * @param r 右边界（包含）
         * @return 区间内的最小/最大值
         */
        public int query(int l, int r) {
            if (l < 0 || r >= data.length || l > r) {
                throw new IllegalArgumentException("查询范围无效");
            }
            
            int length = r - l + 1;
            int k = logTable[length];
            
            int left = st[k][l];
            int right = st[k][r - (1 << k) + 1];
            
            if (isMin) {
                return Math.min(data[left], data[right]);
            } else {
                return Math.max(data[left], data[right]);
            }
        }
        
        /**
         * 离线查询处理
         * @param queries 包含多个查询的数组，每个查询是 [l, r] 形式
         * @return 查询结果数组
         */
        public int[] processOfflineQueries(int[][] queries) {
            int[] results = new int[queries.length];
            
            for (int i = 0; i < queries.length; i++) {
                results[i] = query(queries[i][0], queries[i][1]);
            }
            
            return results;
        }
        
        /**
         * 测试稀疏表
         */
        public static void testSparseTable() {
            System.out.println("=== 测试稀疏表 ===");
            
            int[] data = {1, 3, 5, 7, 9, 11, 13, 15, 17};
            
            // 测试最小值查询
            System.out.println("测试最小值查询:");
            SparseTable minST = new SparseTable(data, true);
            System.out.println("区间[1, 5]的最小值: " + minST.query(1, 5));  // 应该是3
            System.out.println("区间[0, 8]的最小值: " + minST.query(0, 8));  // 应该是1
            System.out.println("区间[4, 7]的最小值: " + minST.query(4, 7));  // 应该是9
            
            // 测试最大值查询
            System.out.println("\n测试最大值查询:");
            SparseTable maxST = new SparseTable(data, false);
            System.out.println("区间[1, 5]的最大值: " + maxST.query(1, 5));  // 应该是11
            System.out.println("区间[0, 8]的最大值: " + maxST.query(0, 8));  // 应该是17
            System.out.println("区间[4, 7]的最大值: " + maxST.query(4, 7));  // 应该是15
            
            // 测试离线查询
            System.out.println("\n测试离线查询:");
            int[][] queries = {
                {0, 2}, {1, 5}, {3, 7}, {2, 8}
            };
            
            int[] minResults = minST.processOfflineQueries(queries);
            System.out.print("离线最小值查询结果: ");
            for (int result : minResults) {
                System.out.print(result + " ");
            }
            System.out.println();
            
            int[] maxResults = maxST.processOfflineQueries(queries);
            System.out.print("离线最大值查询结果: ");
            for (int result : maxResults) {
                System.out.print(result + " ");
            }
            System.out.println();
        }
    }
    
    // ================================
    // 4. 事件排序 - 时间扫描线算法
    // ================================
    
    /**
     * 事件类，用于时间扫描线算法
     */
    static class Event implements Comparable<Event> {
        double time;       // 事件发生的时间
        int type;          // 事件类型：0表示开始，1表示结束
        int data;          // 事件关联的数据
        
        Event(double time, int type, int data) {
            this.time = time;
            this.type = type;
            this.data = data;
        }
        
        @Override
        public int compareTo(Event other) {
            // 首先按照时间排序
            if (this.time != other.time) {
                return Double.compare(this.time, other.time);
            }
            // 时间相同时，结束事件优先处理，避免重复计算
            return Integer.compare(other.type, this.type);
        }
    }
    
    /**
     * 时间扫描线算法实现
     */
    public static class EventSweep {
        /**
         * 区间覆盖问题：计算最多有多少个重叠的区间
         * @param intervals 区间数组，每个区间是 [start, end] 形式
         * @return 最大重叠数量
         */
        public static int maxOverlap(int[][] intervals) {
            if (intervals == null || intervals.length == 0) {
                return 0;
            }
            
            List<Event> events = new ArrayList<>();
            
            // 为每个区间创建开始和结束事件
            for (int[] interval : intervals) {
                events.add(new Event(interval[0], 0, 1));  // 开始事件
                events.add(new Event(interval[1], 1, 1));  // 结束事件
            }
            
            // 按照时间排序事件
            Collections.sort(events);
            
            int maxOverlap = 0;
            int currentOverlap = 0;
            
            // 扫描所有事件
            for (Event event : events) {
                if (event.type == 0) {  // 开始事件
                    currentOverlap++;
                    maxOverlap = Math.max(maxOverlap, currentOverlap);
                } else {  // 结束事件
                    currentOverlap--;
                }
            }
            
            return maxOverlap;
        }
        
        /**
         * 扫描线算法解决矩形面积问题：计算多个矩形的总面积（不重复计算重叠部分）
         * @param rectangles 矩形数组，每个矩形是 [x1, y1, x2, y2] 形式，其中(x1,y1)是左下顶点，(x2,y2)是右上顶点
         * @return 矩形覆盖的总面积
         */
        public static int calculateRectangleArea(int[][] rectangles) {
            if (rectangles == null || rectangles.length == 0) {
                return 0;
            }
            
            // 创建垂直扫描线事件
            List<Event> events = new ArrayList<>();
            Set<Integer> yCoordinates = new HashSet<>();
            
            for (int[] rect : rectangles) {
                int x1 = rect[0];
                int y1 = rect[1];
                int x2 = rect[2];
                int y2 = rect[3];
                
                // 添加开始和结束事件
                events.add(new Event(x1, 0, y1 * 1000 + y2));  // 使用编码存储y范围
                events.add(new Event(x2, 1, y1 * 1000 + y2));  // 假设y不超过1000
                
                // 收集所有y坐标
                yCoordinates.add(y1);
                yCoordinates.add(y2);
            }
            
            // 排序事件
            Collections.sort(events);
            
            // 对y坐标排序
            List<Integer> sortedY = new ArrayList<>(yCoordinates);
            Collections.sort(sortedY);
            
            // 用于跟踪当前活动的矩形
            Set<String> activeIntervals = new HashSet<>();
            double totalArea = 0;
            double prevX = events.get(0).time;
            
            // 处理每个事件
            for (Event event : events) {
                double currentX = event.time;
                double width = currentX - prevX;
                
                // 计算当前活动的y区间总长度
                double height = calculateActiveHeight(activeIntervals, sortedY);
                
                // 增加面积
                totalArea += width * height;
                
                // 更新活动区间
                int y1 = event.data / 1000;
                int y2 = event.data % 1000;
                String intervalKey = y1 + "-" + y2;
                
                if (event.type == 0) {
                    activeIntervals.add(intervalKey);
                } else {
                    activeIntervals.remove(intervalKey);
                }
                
                prevX = currentX;
            }
            
            return (int) totalArea;
        }
        
        /**
         * 计算当前活动的y区间总长度
         */
        private static double calculateActiveHeight(Set<String> activeIntervals, List<Integer> sortedY) {
            if (activeIntervals.isEmpty()) {
                return 0;
            }
            
            // 将活动区间转换为具体的y范围
            List<int[]> intervals = new ArrayList<>();
            for (String key : activeIntervals) {
                String[] parts = key.split("-");
                int y1 = Integer.parseInt(parts[0]);
                int y2 = Integer.parseInt(parts[1]);
                intervals.add(new int[]{y1, y2});
            }
            
            // 合并重叠的y区间
            intervals.sort(Comparator.comparingInt(a -> a[0]));
            
            double totalHeight = 0;
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
         * 测试事件扫描线算法
         */
        public static void testEventSweep() {
            System.out.println("=== 测试事件扫描线算法 ===");
            
            // 测试区间重叠问题
            System.out.println("测试区间重叠问题:");
            int[][] intervals1 = {
                {1, 4}, {2, 5}, {3, 6}, {7, 9}
            };
            System.out.println("最大重叠数量: " + maxOverlap(intervals1));  // 应该是3
            
            int[][] intervals2 = {
                {1, 2}, {3, 4}, {5, 6}
            };
            System.out.println("最大重叠数量: " + maxOverlap(intervals2));  // 应该是1
            
            // 测试矩形面积问题
            System.out.println("\n测试矩形面积计算:");
            int[][] rectangles1 = {
                {0, 0, 2, 2}, {1, 1, 3, 3}
            };
            System.out.println("矩形覆盖总面积: " + calculateRectangleArea(rectangles1));  // 应该是7
            
            int[][] rectangles2 = {
                {0, 0, 1, 1}, {2, 2, 3, 3}, {1, 1, 2, 2}
            };
            System.out.println("矩形覆盖总面积: " + calculateRectangleArea(rectangles2));  // 应该是3
        }
    }
    
    // ================================
    // 5. 差分驱动模拟 - 差分数组
    // ================================
    
    /**
     * 差分数组实现
     * 高效处理区间更新操作
     */
    public static class DifferenceArray {
        private int[] diff;      // 差分数组
        private int[] original;  // 原始数组
        private int size;
        
        /**
         * 构造函数
         * @param n 数组大小
         */
        public DifferenceArray(int n) {
            if (n <= 0) {
                throw new IllegalArgumentException("数组大小必须为正整数");
            }
            
            this.size = n;
            this.diff = new int[n + 1];  // 差分数组大小为n+1，便于处理边界
            this.original = new int[n];
        }
        
        /**
         * 从原始数组创建差分数组
         * @param arr 原始数组
         */
        public DifferenceArray(int[] arr) {
            if (arr == null) {
                throw new IllegalArgumentException("输入数组不能为空");
            }
            
            this.size = arr.length;
            this.original = arr.clone();
            this.diff = new int[size + 1];
            
            // 初始化差分数组
            diff[0] = arr[0];
            for (int i = 1; i < size; i++) {
                diff[i] = arr[i] - arr[i - 1];
            }
        }
        /**
         * 区间更新：将区间[start, end]的每个元素加上val
         * 时间复杂度：O(1)
         * @param start 起始索引（包含）
         * @param end 结束索引（包含）
         * @param val 要增加的值
         */
        public void rangeUpdate(int start, int end, int val) {
            if (start < 0 || end >= size || start > end) {
                throw new IllegalArgumentException("更新范围无效");
            }
            
            diff[start] += val;
            diff[end + 1] -= val;
        }
        /**
         * 获取更新后的数组
         * 时间复杂度：O(n)
         * @return 更新后的数组
         */
        public int[] getResult() {
            int[] result = new int[size];
            result[0] = diff[0];
            
            for (int i = 1; i < size; i++) {
                result[i] = result[i - 1] + diff[i];
            }
            
            return result;
        }
        /**
         * 直接获取数组中特定位置的值
         * 注意：这需要先重建数组，时间复杂度O(n)
         * @param index 索引位置
         * @return 该位置的值
         */
        public int getValue(int index) {
            if (index < 0 || index >= size) {
                throw new IllegalArgumentException("索引无效");
            }
            
            int[] result = getResult();
            return result[index];
        }
        /**
         * 重置差分数组
         */
        public void reset() {
            Arrays.fill(diff, 0);
            if (original.length == size) {
                for (int i = 0; i < size; i++) {
                    rangeUpdate(i, i, original[i]);
                }
            }
        }
        /**
         * 测试差分数组
         */
        public static void testDifferenceArray() {
            System.out.println("=== 测试差分数组 ===");
            
            // 测试从大小创建
            System.out.println("测试从大小创建:");
            DifferenceArray da1 = new DifferenceArray(5);
            System.out.println("初始数组: " + Arrays.toString(da1.getResult()));
            
            da1.rangeUpdate(0, 2, 1);
            System.out.println("区间[0,2]加1: " + Arrays.toString(da1.getResult()));
            
            da1.rangeUpdate(1, 4, 2);
            System.out.println("区间[1,4]加2: " + Arrays.toString(da1.getResult()));
            
            da1.rangeUpdate(2, 3, -1);
            System.out.println("区间[2,3]减1: " + Arrays.toString(da1.getResult()));
            
            // 测试从原始数组创建
            System.out.println("\n测试从原始数组创建:");
            int[] original = {1, 2, 3, 4, 5};
            DifferenceArray da2 = new DifferenceArray(original);
            System.out.println("原始数组: " + Arrays.toString(da2.getResult()));
            
            da2.rangeUpdate(1, 3, 10);
            System.out.println("区间[1,3]加10: " + Arrays.toString(da2.getResult()));
            
            da2.rangeUpdate(0, 4, -5);
            System.out.println("区间[0,4]减5: " + Arrays.toString(da2.getResult()));
            
            // 测试重置功能
            da2.reset();
            System.out.println("重置后: " + Arrays.toString(da2.getResult()));
            
            // 测试边界情况
            System.out.println("\n测试边界情况:");
            DifferenceArray da3 = new DifferenceArray(1);
            da3.rangeUpdate(0, 0, 100);
            System.out.println("单元素数组更新: " + Arrays.toString(da3.getResult()));
        }
    }
    
    // ================================
    // 主方法 - 运行所有测试
    // ================================
    
    public static void main(String[] args) {
        // 运行所有测试
        ClosestPair.testClosestPair();
        System.out.println();
        
        GameOfLife.testGameOfLife();
        System.out.println();
        
        SparseTable.testSparseTable();
        System.out.println();
        
        EventSweep.testEventSweep();
        System.out.println();
        
        DifferenceArray.testDifferenceArray();
    }
}