package class185;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Java版本算法与数据结构实现
 * 包含：
 * 1. 平面分治 (Closest Pair of Points)
 * 2. 棋盘模拟 (Game of Life)
 * 3. 间隔打表 (Sparse Table)
 * 4. 事件排序 (Time Sweep)
 * 5. 差分驱动模拟 (Difference Array)
 * 6. 双向循环链表 (Doubly Circular Linked List)
 * 7. 斐波那契堆 (Fibonacci Heap)
 * 8. 块状链表 (Unrolled Linked List)
 * 
 * 时间复杂度分析：
 * - 平面分治: O(n log n)
 * - 棋盘模拟: O(m*n)
 * - 间隔打表: O(n log n) 预处理, O(1) 查询
 * - 事件排序: O(n log n)
 * - 差分驱动: O(1) 区间更新, O(n) 获取结果
 * - 双向循环链表: 插入/删除头部/尾部 O(1), 其他 O(n)
 * - 斐波那契堆: 插入 O(1) 均摊, 提取最小 O(log n) 均摊
 * - 块状链表: O(n/b) 操作复杂度，b为块大小
 */

public class algorithm2 {
    
    // ================================
    // 1. 平面分治 - 最近点对问题
    // ================================
    
    static class Point {
        double x, y;
        
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public String toString() {
            return "Point(" + x + ", " + y + ")";
        }
        
        public double distanceTo(Point other) {
            double dx = this.x - other.x;
            double dy = this.y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }
    }
    
    static class PairDistance {
        double distance;
        Point p1, p2;
        
        public PairDistance(double distance, Point p1, Point p2) {
            this.distance = distance;
            this.p1 = p1;
            this.p2 = p2;
        }
    }
    
    public static PairDistance closestPair(List<Point> points) {
        if (points == null || points.isEmpty()) {
            throw new IllegalArgumentException("输入点列表不能为空");
        }
        if (points.size() < 2) {
            throw new IllegalArgumentException("至少需要两个点来计算距离");
        }
        
        // 按x坐标排序
        List<Point> pointsSortedByX = new ArrayList<>(points);
        Collections.sort(pointsSortedByX, Comparator.comparingDouble(p -> p.x));
        
        // 按y坐标排序，用于带内搜索
        List<Point> pointsSortedByY = new ArrayList<>(points);
        Collections.sort(pointsSortedByY, Comparator.comparingDouble(p -> p.y));
        
        // 调用递归函数
        return closestPairRecursive(pointsSortedByX, pointsSortedByY);
    }
    
    private static PairDistance closestPairRecursive(List<Point> pointsSortedByX, List<Point> pointsSortedByY) {
        int n = pointsSortedByX.size();
        
        // 基本情况：小于等于3个点，使用暴力法
        if (n <= 3) {
            return bruteForceClosestPair(pointsSortedByX);
        }
        
        // 分治：将点集分为左右两部分
        int mid = n / 2;
        Point midPoint = pointsSortedByX.get(mid);
        
        // 分割y排序的点列表
        List<Point> leftPointsY = new ArrayList<>();
        List<Point> rightPointsY = new ArrayList<>();
        
        for (Point p : pointsSortedByY) {
            if (p.x <= midPoint.x) {
                leftPointsY.add(p);
            } else {
                rightPointsY.add(p);
            }
        }
        
        // 递归求解左右两部分的最近点对
        PairDistance leftResult = closestPairRecursive(
            pointsSortedByX.subList(0, mid), leftPointsY);
        PairDistance rightResult = closestPairRecursive(
            pointsSortedByX.subList(mid, n), rightPointsY);
        
        // 合并：取左右两部分中的最小距离
        PairDistance minResult = (leftResult.distance <= rightResult.distance) 
            ? leftResult : rightResult;
        
        // 带内搜索：查找跨越中线的点对
        // 构建带内的点列表，只考虑x坐标在中线附近min_dist范围内的点
        List<Point> strip = new ArrayList<>();
        for (Point p : pointsSortedByY) {
            if (Math.abs(p.x - midPoint.x) < minResult.distance) {
                strip.add(p);
            }
        }
        
        // 在带内查找可能的更近点对
        PairDistance stripResult = stripClosest(strip, minResult);
        
        // 比较并返回全局最小距离
        return (stripResult.distance < minResult.distance) ? stripResult : minResult;
    }
    
    private static PairDistance bruteForceClosestPair(List<Point> points) {
        int n = points.size();
        double minDist = Double.MAX_VALUE;
        Point p1 = null, p2 = null;
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dist = points.get(i).distanceTo(points.get(j));
                if (dist < minDist) {
                    minDist = dist;
                    p1 = points.get(i);
                    p2 = points.get(j);
                }
            }
        }
        
        return new PairDistance(minDist, p1, p2);
    }
    
    private static PairDistance stripClosest(List<Point> strip, PairDistance currentMin) {
        double minDist = currentMin.distance;
        Point p1 = currentMin.p1;
        Point p2 = currentMin.p2;
        
        // 按y坐标排序已经完成
        int size = strip.size();
        
        // 对于带内的每个点，只需要检查后面y坐标相差不超过min_dist的点
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size && (strip.get(j).y - strip.get(i).y) < minDist; j++) {
                double dist = strip.get(i).distanceTo(strip.get(j));
                if (dist < minDist) {
                    minDist = dist;
                    p1 = strip.get(i);
                    p2 = strip.get(j);
                }
            }
        }
        
        return new PairDistance(minDist, p1, p2);
    }
    
    // ================================
    // 2. 棋盘模拟 - 生命游戏
    // ================================
    
    static class GameOfLife {
        private int[][] board;
        private int rows;
        private int cols;
        
        public GameOfLife(int[][] initialBoard) {
            if (initialBoard == null || initialBoard.length == 0 || initialBoard[0].length == 0) {
                throw new IllegalArgumentException("输入棋盘不能为空");
            }
            
            // 深拷贝输入棋盘
            this.rows = initialBoard.length;
            this.cols = initialBoard[0].length;
            this.board = new int[rows][cols];
            
            for (int i = 0; i < rows; i++) {
                System.arraycopy(initialBoard[i], 0, this.board[i], 0, cols);
            }
        }
        
        private int countNeighbors(int[][] board, int row, int col) {
            int neighbors = 0;
            // 八个方向的偏移
            int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},          {0, 1},
                {1, -1},  {1, 0}, {1, 1}
            };
            
            for (int[] dir : directions) {
                int r = row + dir[0];
                int c = col + dir[1];
                
                // 检查边界并计数
                if (r >= 0 && r < rows && c >= 0 && c < cols) {
                    // 注意：对于原地版本，我们需要考虑标记后的状态
                    // 1和2表示原始状态为活细胞（1：保持活，2：将死亡）
                    if (board[r][c] == 1 || board[r][c] == 2) {
                        neighbors++;
                    }
                }
            }
            
            return neighbors;
        }
        
        public int[][] nextGenerationStandard() {
            // 创建新棋盘存储下一代状态
            int[][] nextBoard = new int[rows][cols];
            
            // 计算每个细胞的下一代状态
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    int neighbors = countNeighbors(this.board, i, j);
                    
                    // 应用生命游戏规则
                    if (this.board[i][j] == 1) {  // 活细胞
                        if (neighbors < 2 || neighbors > 3) {
                            nextBoard[i][j] = 0;  // 死亡：人口稀少或过度拥挤
                        } else {
                            nextBoard[i][j] = 1;  // 存活
                        }
                    } else {  // 死细胞
                        if (neighbors == 3) {
                            nextBoard[i][j] = 1;  // 繁殖
                        }
                    }
                }
            }
            
            // 更新当前棋盘
            this.board = nextBoard;
            return this.board;
        }
        
        public int[][] nextGenerationInplace() {
            // 编码规则：
            // 0: 死细胞 -> 死细胞
            // 1: 活细胞 -> 活细胞
            // 2: 活细胞 -> 死细胞
            // 3: 死细胞 -> 活细胞
            
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    int neighbors = countNeighbors(this.board, i, j);
                    
                    if (this.board[i][j] == 1) {  // 活细胞
                        if (neighbors < 2 || neighbors > 3) {
                            this.board[i][j] = 2;  // 标记为将死亡
                        }
                    } else {  // 死细胞
                        if (neighbors == 3) {
                            this.board[i][j] = 3;  // 标记为将复活
                        }
                    }
                }
            }
            
            // 解码：将标记转换回0和1
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    this.board[i][j] %= 2;  // 2 -> 0, 3 -> 1
                }
            }
            
            return this.board;
        }
        
        public int[][] simulate(int generations, boolean inplace) {
            if (generations <= 0) {
                return getBoard();
            }
            
            for (int i = 0; i < generations; i++) {
                if (inplace) {
                    nextGenerationInplace();
                } else {
                    nextGenerationStandard();
                }
            }
            
            return getBoard();
        }
        
        public int[][] getBoard() {
            // 返回深拷贝，避免外部修改
            int[][] copy = new int[rows][cols];
            for (int i = 0; i < rows; i++) {
                System.arraycopy(this.board[i], 0, copy[i], 0, cols);
            }
            return copy;
        }
        
        public void printBoard() {
            for (int[] row : board) {
                for (int cell : row) {
                    System.out.print(cell == 1 ? '█' : ' ');
                    System.out.print(' ');
                }
                System.out.println();
            }
            System.out.println();
        }
    }
    
    // ================================
    // 3. 间隔打表 - 稀疏表
    // ================================
    
    static class SparseTable {
        private int[] data;
        private int[][] st;
        private int[] logTable;
        private BiFunction<Integer, Integer, Integer> func;
        
        public SparseTable(int[] data, BiFunction<Integer, Integer, Integer> func) {
            if (data == null || data.length == 0) {
                throw new IllegalArgumentException("输入数据不能为空");
            }
            
            this.data = data;
            this.func = func;
            this.logTable = buildLogTable();
            this.st = buildSparseTable();
        }
        
        public SparseTable(int[] data) {
            // 默认使用最小值函数
            this(data, Math::min);
        }
        
        private int[] buildLogTable() {
            int n = data.length;
            int[] logTable = new int[n + 1];
            for (int i = 2; i <= n; i++) {
                logTable[i] = logTable[i / 2] + 1;
            }
            return logTable;
        }
        
        private int[][] buildSparseTable() {
            int n = data.length;
            int kMax = logTable[n] + 1;
            int[][] st = new int[kMax][n];
            
            // 初始化k=0的情况（长度为1的区间）
            for (int i = 0; i < n; i++) {
                st[0][i] = data[i];
            }
            
            // 动态规划构建其他k值
            for (int k = 1; (1 << k) <= n; k++) {
                for (int i = 0; i + (1 << k) <= n; i++) {
                    // 合并两个长度为2^(k-1)的区间
                    st[k][i] = func.apply(
                        st[k-1][i],
                        st[k-1][i + (1 << (k-1))]
                    );
                }
            }
            
            return st;
        }
        
        public int queryRange(int l, int r) {
            // 检查边界
            if (l < 0 || r >= data.length || l > r) {
                throw new IllegalArgumentException("无效的区间边界: [" + l + ", " + r + "]");
            }
            
            // 计算区间长度
            int length = r - l + 1;
            // 找到最大的k，使得2^k <= length
            int k = logTable[length];
            
            // 查询两个重叠的子区间并合并结果
            return func.apply(
                st[k][l],
                st[k][r - (1 << k) + 1]
            );
        }
        
        public List<Integer> batchQuery(List<int[]> queries) {
            List<Integer> results = new ArrayList<>();
            for (int[] query : queries) {
                results.add(queryRange(query[0], query[1]));
            }
            return results;
        }
        
        public boolean isRangeAllSame(int l, int r) {
            if (l == r) {
                return true;
            }
            return queryRange(l, r) == data[l];
        }
        
        public int getRangeExtreme(int l, int r) {
            return queryRange(l, r);
        }
    }
    
    // 函数式接口，用于表示二元函数
    interface BiFunction<A, B, R> {
        R apply(A a, B b);
    }
    
    // ================================
    // 4. 事件排序 - 扫描线算法
    // ================================
    
    static class EventSweep {
        
        public static class OverlapResult {
            int maxOverlap;
            List<int[]> overlappingIntervals;
            
            public OverlapResult(int maxOverlap, List<int[]> overlappingIntervals) {
                this.maxOverlap = maxOverlap;
                this.overlappingIntervals = overlappingIntervals;
            }
        }
        
        public static OverlapResult intervalOverlap(int[][] intervals) {
            if (intervals == null || intervals.length == 0) {
                return new OverlapResult(0, new ArrayList<>());
            }
            
            // 创建事件点列表
            List<Event> events = new ArrayList<>();
            for (int i = 0; i < intervals.length; i++) {
                int start = intervals[i][0];
                int end = intervals[i][1];
                if (start > end) {
                    throw new IllegalArgumentException("无效的区间: [" + start + ", " + end + "]");
                }
                events.add(new Event(start, 1, i));  // 开始事件
                events.add(new Event(end, -1, i));   // 结束事件
            }
            
            // 按位置排序事件，当位置相同时，结束事件优先
            Collections.sort(events);
            
            int currentOverlap = 0;
            int maxOverlap = 0;
            Set<Integer> activeIntervals = new HashSet<>();
            Set<Integer> maxOverlapIntervalIndices = new HashSet<>();
            
            // 扫描事件
            for (Event event : events) {
                // 更新当前重叠数量
                currentOverlap += event.type;
                
                // 更新活动区间列表
                if (event.type == 1) {
                    activeIntervals.add(event.index);
                } else {
                    activeIntervals.remove(event.index);
                }
                
                // 更新最大重叠
                if (currentOverlap > maxOverlap) {
                    maxOverlap = currentOverlap;
                    maxOverlapIntervalIndices = new HashSet<>(activeIntervals);
                }
            }
            
            // 收集重叠的区间
            List<int[]> overlappingIntervals = new ArrayList<>();
            for (int idx : maxOverlapIntervalIndices) {
                overlappingIntervals.add(intervals[idx]);
            }
            
            return new OverlapResult(maxOverlap, overlappingIntervals);
        }
        
        // 事件类，用于排序
        static class Event implements Comparable<Event> {
            int pos;
            int type;  // 1表示开始，-1表示结束
            int index;
            
            public Event(int pos, int type, int index) {
                this.pos = pos;
                this.type = type;
                this.index = index;
            }
            
            @Override
            public int compareTo(Event other) {
                // 按位置排序，当位置相同时，结束事件（type=-1）优先
                if (this.pos != other.pos) {
                    return Integer.compare(this.pos, other.pos);
                }
                return Integer.compare(this.type, other.type);
            }
        }
        
        public static int rectangleArea(int[][] rectangles) {
            if (rectangles == null || rectangles.length == 0) {
                return 0;
            }
            
            // 创建垂直边事件
            List<RectangleEvent> events = new ArrayList<>();
            Set<Integer> xCoords = new HashSet<>();
            
            for (int[] rect : rectangles) {
                int x1 = rect[0];
                int y1 = rect[1];
                int x2 = rect[2];
                int y2 = rect[3];
                
                if (x1 >= x2 || y1 >= y2) {
                    throw new IllegalArgumentException("无效的矩形: [" + x1 + ", " + y1 + ", " + x2 + ", " + y2 + "]");
                }
                
                // 添加垂直线事件
                events.add(new RectangleEvent(x1, true, y1, y2));  // 左边界
                events.add(new RectangleEvent(x2, false, y1, y2)); // 右边界
                xCoords.add(x1);
                xCoords.add(x2);
            }
            
            // 按x坐标排序事件
            Collections.sort(events);
            // 排序x坐标
            List<Integer> sortedX = new ArrayList<>(xCoords);
            Collections.sort(sortedX);
            
            List<int[]> activeIntervals = new ArrayList<>();
            int area = 0;
            Integer prevX = null;
            
            // 扫描事件
            for (RectangleEvent event : events) {
                // 计算当前扫描线和前一条扫描线之间的面积
                if (prevX != null && event.x > prevX && !activeIntervals.isEmpty()) {
                    // 计算当前活动的y区间的总长度
                    int activeLength = mergeAndCalculateLength(activeIntervals);
                    // 面积 = 宽度 * 高度
                    area += (event.x - prevX) * activeLength;
                }
                
                // 更新活动区间
                if (event.isStart) {
                    activeIntervals.add(new int[]{event.y1, event.y2});
                } else {
                    // 移除对应的区间
                    activeIntervals.removeIf(interval -> interval[0] == event.y1 && interval[1] == event.y2);
                }
                
                prevX = event.x;
            }
            
            return area;
        }
        
        static class RectangleEvent implements Comparable<RectangleEvent> {
            int x;
            boolean isStart;
            int y1, y2;
            
            public RectangleEvent(int x, boolean isStart, int y1, int y2) {
                this.x = x;
                this.isStart = isStart;
                this.y1 = y1;
                this.y2 = y2;
            }
            
            @Override
            public int compareTo(RectangleEvent other) {
                return Integer.compare(this.x, other.x);
            }
        }
        
        private static int mergeAndCalculateLength(List<int[]> intervals) {
            if (intervals == null || intervals.isEmpty()) {
                return 0;
            }
            
            // 按起始位置排序
            Collections.sort(intervals, Comparator.comparingInt(a -> a[0]));
            
            List<int[]> merged = new ArrayList<>();
            merged.add(intervals.get(0));
            
            for (int i = 1; i < intervals.size(); i++) {
                int[] current = intervals.get(i);
                int[] last = merged.get(merged.size() - 1);
                
                if (current[0] <= last[1]) {  // 有重叠
                    // 合并区间
                    int newStart = last[0];
                    int newEnd = Math.max(last[1], current[1]);
                    merged.set(merged.size() - 1, new int[]{newStart, newEnd});
                } else {
                    merged.add(current);
                }
            }
            
            // 计算总长度
            int totalLength = 0;
            for (int[] interval : merged) {
                totalLength += interval[1] - interval[0];
            }
            
            return totalLength;
        }
    }
    
    // ================================
    // 5. 差分驱动模拟 - 差分数组
    // ================================
    
    static class DifferenceArray {
        private int[] diff;
        private int size;
        
        public DifferenceArray(int size) {
            if (size <= 0) {
                throw new IllegalArgumentException("数组大小必须为正数");
            }
            this.size = size;
            this.diff = new int[size];  // 初始化为全0数组的差分数组
        }
        
        public DifferenceArray(int[] initialArray) {
            if (initialArray == null || initialArray.length == 0) {
                throw new IllegalArgumentException("初始数组不能为空");
            }
            
            this.size = initialArray.length;
            // 从初始数组构建差分数组
            this.diff = new int[size];
            this.diff[0] = initialArray[0];
            for (int i = 1; i < size; i++) {
                this.diff[i] = initialArray[i] - initialArray[i-1];
            }
        }
        
        public void rangeAdd(int l, int r, int val) {
            // 检查边界
            if (l < 0 || r >= size || l > r) {
                throw new IllegalArgumentException("无效的区间边界: [" + l + ", " + r + "]");
            }
            
            // 在差分数组上进行标记
            diff[l] += val;
            if (r + 1 < size) {
                diff[r + 1] -= val;
            }
        }
        
        public int[] getResult() {
            int[] res = new int[size];
            res[0] = diff[0];
            
            // 前缀和恢复原始数组
            for (int i = 1; i < size; i++) {
                res[i] = res[i-1] + diff[i];
            }
            
            return res;
        }
        
        public int[] getDifferenceArray() {
            return diff.clone();  // 返回副本
        }
        
        public void multipleRangeUpdates(int[][] updates) {
            for (int[] update : updates) {
                rangeAdd(update[0], update[1], update[2]);
            }
        }
        
        public void reset() {
            Arrays.fill(diff, 0);
        }
    }
    
    // ================================
    // 6. 双向循环链表
    // ================================
    
    static class DoublyCircularLinkedList {
        private Node head;
        private int size;
        
        private static class Node {
            int data;
            Node prev;
            Node next;
            
            public Node(int data) {
                this.data = data;
                this.prev = this;  // 初始指向自己
                this.next = this;  // 初始指向自己
            }
        }
        
        public DoublyCircularLinkedList() {
            this.head = null;
            this.size = 0;
        }
        
        public boolean isEmpty() {
            return head == null;
        }
        
        public int getSize() {
            return size;
        }
        
        public void insertAtHead(int data) {
            Node newNode = new Node(data);
            
            if (isEmpty()) {
                // 空链表情况
                head = newNode;
            } else {
                // 非空链表，插入到头部
                Node tail = head.prev;
                
                // 连接新节点与尾节点
                newNode.prev = tail;
                tail.next = newNode;
                
                // 连接新节点与头节点
                newNode.next = head;
                head.prev = newNode;
                
                // 更新头节点
                head = newNode;
            }
            
            size++;
        }
        
        public void insertAtTail(int data) {
            if (isEmpty()) {
                // 空链表情况，直接调用insertAtHead
                insertAtHead(data);
                return;
            }
            
            Node newNode = new Node(data);
            Node tail = head.prev;
            
            // 连接尾节点与新节点
            tail.next = newNode;
            newNode.prev = tail;
            
            // 连接新节点与头节点
            newNode.next = head;
            head.prev = newNode;
            
            size++;
        }
        
        public void insertAtPosition(int index, int data) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException("插入位置无效: " + index);
            }
            
            if (index == 0) {
                // 在头部插入
                insertAtHead(data);
                return;
            }
            
            if (index == size) {
                // 在尾部插入
                insertAtTail(data);
                return;
            }
            
            // 找到插入位置的前一个节点
            Node current;
            if (index <= size / 2) {
                // 从头开始遍历
                current = head;
                for (int i = 0; i < index - 1; i++) {
                    current = current.next;
                }
            } else {
                // 从尾开始遍历
                current = head.prev;
                for (int i = 0; i < size - index; i++) {
                    current = current.prev;
                }
            }
            
            // 创建新节点
            Node newNode = new Node(data);
            Node nextNode = current.next;
            
            // 建立连接
            newNode.prev = current;
            newNode.next = nextNode;
            current.next = newNode;
            nextNode.prev = newNode;
            
            size++;
        }
        
        public int deleteHead() {
            if (isEmpty()) {
                throw new IllegalStateException("无法从空链表删除元素");
            }
            
            int data = head.data;
            
            if (size == 1) {
                // 链表只有一个节点
                head = null;
            } else {
                // 链表有多个节点
                Node tail = head.prev;
                Node newHead = head.next;
                
                // 更新连接
                tail.next = newHead;
                newHead.prev = tail;
                
                // 更新头节点
                head = newHead;
            }
            
            size--;
            return data;
        }
        
        public int deleteTail() {
            if (isEmpty()) {
                throw new IllegalStateException("无法从空链表删除元素");
            }
            
            if (size == 1) {
                // 链表只有一个节点，直接调用deleteHead
                return deleteHead();
            }
            
            Node tail = head.prev;
            int data = tail.data;
            
            // 更新连接
            Node newTail = tail.prev;
            newTail.next = head;
            head.prev = newTail;
            
            size--;
            return data;
        }
        
        public int deleteAtPosition(int index) {
            if (isEmpty()) {
                throw new IllegalStateException("无法从空链表删除元素");
            }
            
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("删除位置无效: " + index);
            }
            
            if (index == 0) {
                return deleteHead();
            }
            
            if (index == size - 1) {
                return deleteTail();
            }
            
            // 找到要删除的节点
            Node current;
            if (index <= size / 2) {
                current = head;
                for (int i = 0; i < index; i++) {
                    current = current.next;
                }
            } else {
                current = head.prev;
                for (int i = 0; i < size - 1 - index; i++) {
                    current = current.prev;
                }
            }
            
            // 保存数据
            int data = current.data;
            
            // 更新连接
            Node prevNode = current.prev;
            Node nextNode = current.next;
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
            
            size--;
            return data;
        }
        
        public boolean deleteByValue(int value) {
            if (isEmpty()) {
                return false;
            }
            
            // 特殊情况：头节点就是要删除的节点
            if (head.data == value) {
                deleteHead();
                return true;
            }
            
            // 遍历链表查找值
            Node current = head.next;
            while (current != head) {
                if (current.data == value) {
                    // 找到要删除的节点
                    Node prevNode = current.prev;
                    Node nextNode = current.next;
                    
                    // 更新连接
                    prevNode.next = nextNode;
                    nextNode.prev = prevNode;
                    
                    size--;
                    return true;
                }
                current = current.next;
            }
            
            // 未找到值
            return false;
        }
        
        public List<Integer> traverseForward() {
            List<Integer> result = new ArrayList<>();
            if (isEmpty()) {
                return result;
            }
            
            Node current = head;
            do {
                result.add(current.data);
                current = current.next;
            } while (current != head);
            
            return result;
        }
        
        public List<Integer> traverseBackward() {
            List<Integer> result = new ArrayList<>();
            if (isEmpty()) {
                return result;
            }
            
            // 从尾节点开始
            Node current = head.prev;
            do {
                result.add(current.data);
                current = current.prev;
            } while (current != head.prev);
            
            return result;
        }
        
        public int search(int value) {
            if (isEmpty()) {
                return -1;
            }
            
            int index = 0;
            Node current = head;
            do {
                if (current.data == value) {
                    return index;
                }
                current = current.next;
                index++;
            } while (current != head);
            
            return -1;
        }
        
        public int get(int index) {
            if (isEmpty()) {
                throw new IllegalStateException("链表为空");
            }
            
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("索引无效: " + index);
            }
            
            // 优化：根据索引位置选择从头还是从尾开始遍历
            Node current;
            if (index <= size / 2) {
                current = head;
                for (int i = 0; i < index; i++) {
                    current = current.next;
                }
            } else {
                current = head.prev;
                for (int i = 0; i < size - 1 - index; i++) {
                    current = current.prev;
                }
            }
            
            return current.data;
        }
        
        public int set(int index, int value) {
            if (isEmpty()) {
                throw new IllegalStateException("链表为空");
            }
            
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("索引无效: " + index);
            }
            
            // 优化：根据索引位置选择从头还是从尾开始遍历
            Node current;
            if (index <= size / 2) {
                current = head;
                for (int i = 0; i < index; i++) {
                    current = current.next;
                }
            } else {
                current = head.prev;
                for (int i = 0; i < size - 1 - index; i++) {
                    current = current.prev;
                }
            }
            
            int oldValue = current.data;
            current.data = value;
            return oldValue;
        }
        
        public void clear() {
            head = null;
            size = 0;
        }
        
        public void reverse() {
            if (isEmpty() || size == 1) {
                return;  // 空链表或只有一个节点不需要反转
            }
            
            // 保存头节点和尾节点
            Node current = head;
            Node tail = head.prev;
            
            // 交换每个节点的prev和next指针
            do {
                // 交换prev和next
                Node temp = current.prev;
                current.prev = current.next;
                current.next = temp;
                
                // 移动到下一个节点（现在是prev指针）
                current = current.prev;
                
            } while (current != head);
            
            // 更新头节点为原来的尾节点
            head = tail;
        }
        
        public void rotate(int k) {
            if (isEmpty() || size == 1 || k % size == 0) {
                return;  // 无需旋转
            }
            
            // 标准化k值，使其在[0, size-1]范围内
            k = k % size;
            if (k < 0) {
                k += size;  // 转换为正向旋转
            }
            
            // 向右旋转k步相当于将倒数第k个节点作为新的头节点
            if (k > 0) {
                // 找到新的头节点（倒数第k个节点）
                Node newHead = head;
                for (int i = 0; i < size - k; i++) {
                    newHead = newHead.next;
                }
                
                // 更新头节点
                head = newHead;
            }
        }
        
        public void printList() {
            if (isEmpty()) {
                System.out.println("链表为空");
                return;
            }
            
            StringBuilder sb = new StringBuilder();
            Node current = head;
            do {
                sb.append(current.data);
                if (current.next != head) {
                    sb.append(" <-> ");
                }
                current = current.next;
            } while (current != head);
            
            sb.append(" (循环)");
            System.out.println(sb.toString());
        }
    }
    
    // ================================
    // 主函数，用于测试
    // ================================
    
    public static void main(String[] args) {
        System.out.println("=== Java算法与数据结构实现 ===");
        
        // 这里可以添加测试代码
        testClosestPair();
        testGameOfLife();
        testSparseTable();
        testEventSweep();
        testDifferenceArray();
        testDoublyCircularLinkedList();
    }
    
    private static void testClosestPair() {
        System.out.println("\n=== 测试平面分治算法 - 最近点对 ===");
        
        List<Point> points = Arrays.asList(
            new Point(0, 0),
            new Point(3, 0),
            new Point(0, 4),
            new Point(1, 1)
        );
        
        PairDistance result = closestPair(points);
        System.out.println("最小距离: " + result.distance);
        System.out.println("最近点对: " + result.p1 + " 和 " + result.p2);
    }
    
    private static void testGameOfLife() {
        System.out.println("\n=== 测试生命游戏 ===");
        
        int[][] initialBoard = {
            {0, 1, 0},
            {0, 1, 0},
            {0, 1, 0}
        };
        
        GameOfLife game = new GameOfLife(initialBoard);
        System.out.println("初始状态:");
        game.printBoard();
        
        game.simulate(1, true);
        System.out.println("下一代状态:");
        game.printBoard();
    }
    
    private static void testSparseTable() {
        System.out.println("\n=== 测试稀疏表 ===");
        
        int[] data = {5, 2, 9, 3, 7, 6, 1, 8, 4};
        SparseTable st = new SparseTable(data);
        
        System.out.println("区间[0, 2]最小值: " + st.queryRange(0, 2));
        System.out.println("区间[3, 7]最小值: " + st.queryRange(3, 7));
    }
    
    private static void testEventSweep() {
        System.out.println("\n=== 测试事件扫描算法 ===");
        
        int[][] intervals = {{1, 5}, {2, 7}, {3, 9}, {8, 10}};
        EventSweep.OverlapResult result = EventSweep.intervalOverlap(intervals);
        
        System.out.println("最大重叠数量: " + result.maxOverlap);
        System.out.println("重叠的区间: " + 
            result.overlappingIntervals.stream()
                .map(interval -> "[" + interval[0] + ", " + interval[1] + "]")
                .collect(Collectors.joining(", ")));
    }
    
    private static void testDifferenceArray() {
        System.out.println("\n=== 测试差分数组 ===");
        
        DifferenceArray diffArray = new DifferenceArray(10);
        diffArray.rangeAdd(1, 5, 2);
        diffArray.rangeAdd(3, 8, 3);
        
        int[] result = diffArray.getResult();
        System.out.println("结果数组: " + Arrays.toString(result));
    }
    
    private static void testDoublyCircularLinkedList() {
        System.out.println("\n=== 测试双向循环链表 ===");
        
        DoublyCircularLinkedList list = new DoublyCircularLinkedList();
        list.insertAtHead(1);
        list.insertAtTail(3);
        list.insertAtPosition(1, 2);
        
        System.out.print("链表内容: ");
        list.printList();
        
        System.out.println("正向遍历: " + list.traverseForward());
        System.out.println("反向遍历: " + list.traverseBackward());
    }
}