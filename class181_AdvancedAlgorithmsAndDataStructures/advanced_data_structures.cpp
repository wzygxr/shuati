#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <stdexcept>
#include <memory>
#include <limits>
#include <queue>
#include <tuple>
#include <functional>
#include <unordered_set>
#include <functional>

/**
 * C++版本高级数据结构与算法实现
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

// ================================
// 1. 平面分治 - 最近点对问题
// ================================

struct Point {
    double x, y;
    
    Point(double x = 0, double y = 0) : x(x), y(y) {}
    
    double distanceTo(const Point& other) const {
        double dx = this->x - other.x;
        double dy = this->y - other.y;
        return std::sqrt(dx * dx + dy * dy);
    }
    
    // 按x坐标排序的比较函数
    bool operator<(const Point& other) const {
        return x < other.x;
    }
    
    friend std::ostream& operator<<(std::ostream& os, const Point& p) {
        return os << "Point(" << p.x << ", " << p.y << ")";
    }
};

struct PairDistance {
    double distance;
    Point p1, p2;
    
    PairDistance(double distance = 0, Point p1 = Point(), Point p2 = Point()) 
        : distance(distance), p1(p1), p2(p2) {}
};

// 按y坐标排序的比较函数
bool compareByY(const Point& a, const Point& b) {
    return a.y < b.y;
}

PairDistance bruteForceClosestPair(const std::vector<Point>& points) {
    int n = points.size();
    double minDist = std::numeric_limits<double>::max();
    Point p1, p2;
    
    for (int i = 0; i < n; i++) {
        for (int j = i + 1; j < n; j++) {
            double dist = points[i].distanceTo(points[j]);
            if (dist < minDist) {
                minDist = dist;
                p1 = points[i];
                p2 = points[j];
            }
        }
    }
    
    return PairDistance(minDist, p1, p2);
}

PairDistance stripClosest(std::vector<Point>& strip, PairDistance currentMin) {
    double minDist = currentMin.distance;
    Point p1 = currentMin.p1;
    Point p2 = currentMin.p2;
    
    // 按y坐标排序已经完成
    int size = strip.size();
    
    // 对于带内的每个点，只需要检查后面y坐标相差不超过min_dist的点
    for (int i = 0; i < size; i++) {
        for (int j = i + 1; j < size && (strip[j].y - strip[i].y) < minDist; j++) {
            double dist = strip[i].distanceTo(strip[j]);
            if (dist < minDist) {
                minDist = dist;
                p1 = strip[i];
                p2 = strip[j];
            }
        }
    }
    
    return PairDistance(minDist, p1, p2);
}

PairDistance closestPairRecursive(std::vector<Point>& pointsSortedByX, std::vector<Point>& pointsSortedByY) {
    int n = pointsSortedByX.size();
    
    // 基本情况：小于等于3个点，使用暴力法
    if (n <= 3) {
        return bruteForceClosestPair(pointsSortedByX);
    }
    
    // 分治：将点集分为左右两部分
    int mid = n / 2;
    Point midPoint = pointsSortedByX[mid];
    
    // 分割y排序的点列表
    std::vector<Point> leftPointsY, rightPointsY;
    
    for (const Point& p : pointsSortedByY) {
        if (p.x <= midPoint.x) {
            leftPointsY.push_back(p);
        } else {
            rightPointsY.push_back(p);
        }
    }
    
    // 递归求解左右两部分的最近点对
    std::vector<Point> leftPointsX(pointsSortedByX.begin(), pointsSortedByX.begin() + mid);
    std::vector<Point> rightPointsX(pointsSortedByX.begin() + mid, pointsSortedByX.end());
    
    PairDistance leftResult = closestPairRecursive(leftPointsX, leftPointsY);
    PairDistance rightResult = closestPairRecursive(rightPointsX, rightPointsY);
    
    // 合并：取左右两部分中的最小距离
    PairDistance minResult = (leftResult.distance <= rightResult.distance) 
        ? leftResult : rightResult;
    
    // 带内搜索：查找跨越中线的点对
    // 构建带内的点列表，只考虑x坐标在中线附近min_dist范围内的点
    std::vector<Point> strip;
    for (const Point& p : pointsSortedByY) {
        if (std::abs(p.x - midPoint.x) < minResult.distance) {
            strip.push_back(p);
        }
    }
    
    // 在带内查找可能的更近点对
    PairDistance stripResult = stripClosest(strip, minResult);
    
    // 比较并返回全局最小距离
    return (stripResult.distance < minResult.distance) ? stripResult : minResult;
}

PairDistance closestPair(std::vector<Point> points) {
    if (points.empty()) {
        throw std::invalid_argument("输入点列表不能为空");
    }
    if (points.size() < 2) {
        throw std::invalid_argument("至少需要两个点来计算距离");
    }
    
    // 按x坐标排序
    std::vector<Point> pointsSortedByX = points;
    std::sort(pointsSortedByX.begin(), pointsSortedByX.end());
    
    // 按y坐标排序，用于带内搜索
    std::vector<Point> pointsSortedByY = points;
    std::sort(pointsSortedByY.begin(), pointsSortedByY.end(), compareByY);
    
    // 调用递归函数
    return closestPairRecursive(pointsSortedByX, pointsSortedByY);
}

// ================================
// 2. 棋盘模拟 - 生命游戏
// ================================

class GameOfLife {
private:
    std::vector<std::vector<int>> board;
    int rows;
    int cols;
    
    int countNeighbors(const std::vector<std::vector<int>>& board, int row, int col) const {
        int neighbors = 0;
        // 八个方向的偏移
        std::vector<std::pair<int, int>> directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0}, {1, 1}
        };
        
        for (const auto& dir : directions) {
            int r = row + dir.first;
            int c = col + dir.second;
            
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
    
public:
    GameOfLife(const std::vector<std::vector<int>>& initialBoard) {
        if (initialBoard.empty() || initialBoard[0].empty()) {
            throw std::invalid_argument("输入棋盘不能为空");
        }
        
        // 深拷贝输入棋盘
        this->rows = initialBoard.size();
        this->cols = initialBoard[0].size();
        this->board.resize(rows, std::vector<int>(cols));
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this->board[i][j] = initialBoard[i][j];
            }
        }
    }
    
    std::vector<std::vector<int>> nextGenerationStandard() {
        // 创建新棋盘存储下一代状态
        std::vector<std::vector<int>> nextBoard(rows, std::vector<int>(cols, 0));
        
        // 计算每个细胞的下一代状态
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int neighbors = countNeighbors(this->board, i, j);
                
                // 应用生命游戏规则
                if (this->board[i][j] == 1) {  // 活细胞
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
        this->board = nextBoard;
        return this->board;
    }
    
    std::vector<std::vector<int>> nextGenerationInplace() {
        // 编码规则：
        // 0: 死细胞 -> 死细胞
        // 1: 活细胞 -> 活细胞
        // 2: 活细胞 -> 死细胞
        // 3: 死细胞 -> 活细胞
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int neighbors = countNeighbors(this->board, i, j);
                
                if (this->board[i][j] == 1) {  // 活细胞
                    if (neighbors < 2 || neighbors > 3) {
                        this->board[i][j] = 2;  // 标记为将死亡
                    }
                } else {  // 死细胞
                    if (neighbors == 3) {
                        this->board[i][j] = 3;  // 标记为将复活
                    }
                }
            }
        }
        
        // 解码：将标记转换回0和1
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this->board[i][j] %= 2;  // 2 -> 0, 3 -> 1
            }
        }
        
        return this->board;
    }
    
    std::vector<std::vector<int>> simulate(int generations, bool inplace) {
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
    
    std::vector<std::vector<int>> getBoard() const {
        // 返回深拷贝，避免外部修改
        std::vector<std::vector<int>> copy(rows, std::vector<int>(cols));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                copy[i][j] = this->board[i][j];
            }
        }
        return copy;
    }
    
    void printBoard() const {
        for (const auto& row : board) {
            for (int cell : row) {
                std::cout << (cell == 1 ? '#' : ' ') << ' ';
            }
            std::cout << std::endl;
        }
        std::cout << std::endl;
    }
};

// ================================
// 3. 间隔打表 - 稀疏表
// ================================

class SparseTable {
private:
    std::vector<int> data;
    std::vector<std::vector<int>> st;
    std::vector<int> logTable;
    
    // 用于区间查询的函数
    using MergeFunc = std::function<int(int, int)>;
    MergeFunc mergeFunc;
    
    std::vector<int> buildLogTable() {
        int n = data.size();
        std::vector<int> logTable(n + 1, 0);
        for (int i = 2; i <= n; i++) {
            logTable[i] = logTable[i / 2] + 1;
        }
        return logTable;
    }
    
    std::vector<std::vector<int>> buildSparseTable() {
        int n = data.size();
        int kMax = logTable[n] + 1;
        std::vector<std::vector<int>> st(kMax, std::vector<int>(n));
        
        // 初始化k=0的情况（长度为1的区间）
        for (int i = 0; i < n; i++) {
            st[0][i] = data[i];
        }
        
        // 动态规划构建其他k值
        for (int k = 1; (1 << k) <= n; k++) {
            for (int i = 0; i + (1 << k) <= n; i++) {
                // 合并两个长度为2^(k-1)的区间
                st[k][i] = mergeFunc(
                    st[k-1][i],
                    st[k-1][i + (1 << (k-1))]
                );
            }
        }
        
        return st;
    }
    
public:
    SparseTable(const std::vector<int>& data, MergeFunc func) 
        : data(data), mergeFunc(func) {
        if (data.empty()) {
            throw std::invalid_argument("输入数据不能为空");
        }
        
        this->logTable = buildLogTable();
        this->st = buildSparseTable();
    }
    
    // 默认使用最小值函数
    SparseTable(const std::vector<int>& data) 
        : SparseTable(data, [](int a, int b) { return std::min(a, b); }) {}
    
    int queryRange(int l, int r) {
        // 检查边界
        if (l < 0 || r >= data.size() || l > r) {
            throw std::invalid_argument("无效的区间边界: [" + std::to_string(l) + ", " + std::to_string(r) + "]");
        }
        
        // 计算区间长度
        int length = r - l + 1;
        // 找到最大的k，使得2^k <= length
        int k = logTable[length];
        
        // 查询两个重叠的子区间并合并结果
        return mergeFunc(
            st[k][l],
            st[k][r - (1 << k) + 1]
        );
    }
    
    std::vector<int> batchQuery(const std::vector<std::pair<int, int>>& queries) {
        std::vector<int> results;
        results.reserve(queries.size());
        for (const auto& query : queries) {
            results.push_back(queryRange(query.first, query.second));
        }
        return results;
    }
    
    bool isRangeAllSame(int l, int r) {
        if (l == r) {
            return true;
        }
        return queryRange(l, r) == data[l];
    }
    
    int getRangeExtreme(int l, int r) {
        return queryRange(l, r);
    }
};

// ================================
// 4. 事件排序 - 扫描线算法
// ================================

class EventSweep {
public:
    struct OverlapResult {
        int maxOverlap;
        std::vector<std::pair<int, int>> overlappingIntervals;
        
        OverlapResult(int maxOverlap = 0, 
                     const std::vector<std::pair<int, int>>& overlappingIntervals = {}) 
            : maxOverlap(maxOverlap), overlappingIntervals(overlappingIntervals) {}
    };
    
    // 事件类，用于排序
    struct Event {
        int pos;
        int type;  // 1表示开始，-1表示结束
        int index;
        
        Event(int pos = 0, int type = 0, int index = 0) 
            : pos(pos), type(type), index(index) {}
        
        bool operator<(const Event& other) const {
            // 按位置排序，当位置相同时，结束事件（type=-1）优先
            if (this->pos != other.pos) {
                return this->pos < other.pos;
            }
            return this->type < other.type;
        }
    };
    
    static OverlapResult intervalOverlap(const std::vector<std::pair<int, int>>& intervals) {
        if (intervals.empty()) {
            return OverlapResult(0, {});
        }
        
        // 创建事件点列表
        std::vector<Event> events;
        for (int i = 0; i < intervals.size(); i++) {
            int start = intervals[i].first;
            int end = intervals[i].second;
            if (start > end) {
                throw std::invalid_argument("无效的区间: [" + std::to_string(start) + ", " + std::to_string(end) + "]");
            }
            events.emplace_back(start, 1, i);  // 开始事件
            events.emplace_back(end, -1, i);   // 结束事件
        }
        
        // 按位置排序事件，当位置相同时，结束事件优先
        std::sort(events.begin(), events.end());
        
        int currentOverlap = 0;
        int maxOverlap = 0;
        std::unordered_set<int> activeIntervals;
        std::unordered_set<int> maxOverlapIntervalIndices;
        
        // 扫描事件
        for (const Event& event : events) {
            // 更新当前重叠数量
            currentOverlap += event.type;
            
            // 更新活动区间列表
            if (event.type == 1) {
                activeIntervals.insert(event.index);
            } else {
                activeIntervals.erase(event.index);
            }
            
            // 更新最大重叠
            if (currentOverlap > maxOverlap) {
                maxOverlap = currentOverlap;
                maxOverlapIntervalIndices = activeIntervals;
            }
        }
        
        // 收集重叠的区间
        std::vector<std::pair<int, int>> overlappingIntervals;
        for (int idx : maxOverlapIntervalIndices) {
            overlappingIntervals.push_back(intervals[idx]);
        }
        
        return OverlapResult(maxOverlap, overlappingIntervals);
    }
    
    // 矩形面积计算
    struct RectangleEvent {
        int x;
        bool isStart;
        int y1, y2;
        
        RectangleEvent(int x = 0, bool isStart = false, int y1 = 0, int y2 = 0) 
            : x(x), isStart(isStart), y1(y1), y2(y2) {}
        
        bool operator<(const RectangleEvent& other) const {
            return this->x < other.x;
        }
    };
    
    static int rectangleArea(const std::vector<std::tuple<int, int, int, int>>& rectangles) {
        if (rectangles.empty()) {
            return 0;
        }
        
        // 创建垂直边事件
        std::vector<RectangleEvent> events;
        std::unordered_set<int> xCoords;
        
        for (const auto& rect : rectangles) {
            int x1 = std::get<0>(rect);
            int y1 = std::get<1>(rect);
            int x2 = std::get<2>(rect);
            int y2 = std::get<3>(rect);
            
            if (x1 >= x2 || y1 >= y2) {
                throw std::invalid_argument("无效的矩形: [" + std::to_string(x1) + ", " + 
                                          std::to_string(y1) + ", " + std::to_string(x2) + ", " + 
                                          std::to_string(y2) + "]");
            }
            
            // 添加垂直线事件
            events.emplace_back(x1, true, y1, y2);  // 左边界
            events.emplace_back(x2, false, y1, y2); // 右边界
            xCoords.insert(x1);
            xCoords.insert(x2);
        }
        
        // 按x坐标排序事件
        std::sort(events.begin(), events.end());
        // 排序x坐标
        std::vector<int> sortedX(xCoords.begin(), xCoords.end());
        std::sort(sortedX.begin(), sortedX.end());
        
        std::vector<std::pair<int, int>> activeIntervals;
        int area = 0;
        int prevX = -1;
        bool hasPrev = false;
        
        // 扫描事件
        for (const RectangleEvent& event : events) {
            // 计算当前扫描线和前一条扫描线之间的面积
            if (hasPrev && event.x > prevX && !activeIntervals.empty()) {
                // 计算当前活动的y区间的总长度
                int activeLength = mergeAndCalculateLength(activeIntervals);
                // 面积 = 宽度 * 高度
                area += (event.x - prevX) * activeLength;
            }
            
            // 更新活动区间
            if (event.isStart) {
                activeIntervals.emplace_back(event.y1, event.y2);
            } else {
                // 移除对应的区间
                activeIntervals.erase(
                    std::remove_if(activeIntervals.begin(), activeIntervals.end(),
                                 [&](const std::pair<int, int>& interval) {
                                     return interval.first == event.y1 && interval.second == event.y2;
                                 }),
                    activeIntervals.end()
                );
            }
            
            prevX = event.x;
            hasPrev = true;
        }
        
        return area;
    }
    
private:
    static int mergeAndCalculateLength(std::vector<std::pair<int, int>>& intervals) {
        if (intervals.empty()) {
            return 0;
        }
        
        // 按起始位置排序
        std::sort(intervals.begin(), intervals.end());
        
        std::vector<std::pair<int, int>> merged;
        merged.push_back(intervals[0]);
        
        for (size_t i = 1; i < intervals.size(); i++) {
            const auto& current = intervals[i];
            auto& last = merged.back();
            
            if (current.first <= last.second) {  // 有重叠
                // 合并区间
                int newStart = last.first;
                int newEnd = std::max(last.second, current.second);
                merged.back() = {newStart, newEnd};
            } else {
                merged.push_back(current);
            }
        }
        
        // 计算总长度
        int totalLength = 0;
        for (const auto& interval : merged) {
            totalLength += interval.second - interval.first;
        }
        
        return totalLength;
    }
};

// ================================
// 5. 差分驱动模拟 - 差分数组
// ================================

class DifferenceArray {
private:
    std::vector<int> diff;
    int size;
    
public:
    DifferenceArray(int size) {
        if (size <= 0) {
            throw std::invalid_argument("数组大小必须为正数");
        }
        this->size = size;
        this->diff.resize(size, 0);  // 初始化为全0数组的差分数组
    }
    
    DifferenceArray(const std::vector<int>& initialArray) {
        if (initialArray.empty()) {
            throw std::invalid_argument("初始数组不能为空");
        }
        
        this->size = initialArray.size();
        // 从初始数组构建差分数组
        this->diff.resize(size);
        this->diff[0] = initialArray[0];
        for (int i = 1; i < size; i++) {
            this->diff[i] = initialArray[i] - initialArray[i-1];
        }
    }
    
    void rangeAdd(int l, int r, int val) {
        // 检查边界
        if (l < 0 || r >= size || l > r) {
            throw std::invalid_argument("无效的区间边界: [" + std::to_string(l) + ", " + std::to_string(r) + "]");
        }
        
        // 在差分数组上进行标记
        diff[l] += val;
        if (r + 1 < size) {
            diff[r + 1] -= val;
        }
    }
    
    std::vector<int> getResult() {
        std::vector<int> res(size);
        res[0] = diff[0];
        
        // 前缀和恢复原始数组
        for (int i = 1; i < size; i++) {
            res[i] = res[i-1] + diff[i];
        }
        
        return res;
    }
    
    std::vector<int> getDifferenceArray() const {
        return diff;  // 返回副本
    }
    
    void multipleRangeUpdates(const std::vector<std::tuple<int, int, int>>& updates) {
        for (const auto& update : updates) {
            rangeAdd(std::get<0>(update), std::get<1>(update), std::get<2>(update));
        }
    }
    
    void reset() {
        std::fill(diff.begin(), diff.end(), 0);
    }
};

// ================================
// 6. 双向循环链表
// ================================

class DoublyCircularLinkedList {
private:
    struct Node {
        int data;
        Node* prev;
        Node* next;
        
        Node(int data) : data(data), prev(this), next(this) {}
    };
    
    Node* head;
    int size;
    
public:
    DoublyCircularLinkedList() : head(nullptr), size(0) {}
    
    ~DoublyCircularLinkedList() {
        clear();
    }
    
    bool isEmpty() const {
        return head == nullptr;
    }
    
    int getSize() const {
        return size;
    }
    
    void insertAtHead(int data) {
        Node* newNode = new Node(data);
        
        if (isEmpty()) {
            // 空链表情况
            head = newNode;
        } else {
            // 非空链表，插入到头部
            Node* tail = head->prev;
            
            // 连接新节点与尾节点
            newNode->prev = tail;
            tail->next = newNode;
            
            // 连接新节点与头节点
            newNode->next = head;
            head->prev = newNode;
            
            // 更新头节点
            head = newNode;
        }
        
        size++;
    }
    
    void insertAtTail(int data) {
        if (isEmpty()) {
            // 空链表情况，直接调用insertAtHead
            insertAtHead(data);
            return;
        }
        
        Node* newNode = new Node(data);
        Node* tail = head->prev;
        
        // 连接尾节点与新节点
        tail->next = newNode;
        newNode->prev = tail;
        
        // 连接新节点与头节点
        newNode->next = head;
        head->prev = newNode;
        
        size++;
    }
    
    void insertAtPosition(int index, int data) {
        if (index < 0 || index > size) {
            throw std::out_of_range("插入位置无效: " + std::to_string(index));
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
        Node* current;
        if (index <= size / 2) {
            // 从头开始遍历
            current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current->next;
            }
        } else {
            // 从尾开始遍历
            current = head->prev;
            for (int i = 0; i < size - index; i++) {
                current = current->prev;
            }
        }
        
        // 创建新节点
        Node* newNode = new Node(data);
        Node* nextNode = current->next;
        
        // 建立连接
        newNode->prev = current;
        newNode->next = nextNode;
        current->next = newNode;
        nextNode->prev = newNode;
        
        size++;
    }
    
    int deleteHead() {
        if (isEmpty()) {
            throw std::runtime_error("无法从空链表删除元素");
        }
        
        Node* temp = head;
        int data = temp->data;
        
        if (size == 1) {
            // 链表只有一个节点
            delete temp;
            head = nullptr;
        } else {
            // 链表有多个节点
            Node* tail = head->prev;
            Node* newHead = head->next;
            
            // 更新连接
            tail->next = newHead;
            newHead->prev = tail;
            
            // 删除旧头节点
            delete temp;
            
            // 更新头节点
            head = newHead;
        }
        
        size--;
        return data;
    }
    
    int deleteTail() {
        if (isEmpty()) {
            throw std::runtime_error("无法从空链表删除元素");
        }
        
        if (size == 1) {
            // 链表只有一个节点，直接调用deleteHead
            return deleteHead();
        }
        
        Node* tail = head->prev;
        int data = tail->data;
        
        // 更新连接
        Node* newTail = tail->prev;
        newTail->next = head;
        head->prev = newTail;
        
        // 删除尾节点
        delete tail;
        
        size--;
        return data;
    }
    
    int deleteAtPosition(int index) {
        if (isEmpty()) {
            throw std::runtime_error("无法从空链表删除元素");
        }
        
        if (index < 0 || index >= size) {
            throw std::out_of_range("删除位置无效: " + std::to_string(index));
        }
        
        if (index == 0) {
            return deleteHead();
        }
        
        if (index == size - 1) {
            return deleteTail();
        }
        
        // 找到要删除的节点
        Node* current;
        if (index <= size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current->next;
            }
        } else {
            current = head->prev;
            for (int i = 0; i < size - 1 - index; i++) {
                current = current->prev;
            }
        }
        
        // 保存数据
        int data = current->data;
        
        // 更新连接
        Node* prevNode = current->prev;
        Node* nextNode = current->next;
        prevNode->next = nextNode;
        nextNode->prev = prevNode;
        
        // 删除节点
        delete current;
        
        size--;
        return data;
    }
    
    bool deleteByValue(int value) {
        if (isEmpty()) {
            return false;
        }
        
        // 特殊情况：头节点就是要删除的节点
        if (head->data == value) {
            deleteHead();
            return true;
        }
        
        // 遍历链表查找值
        Node* current = head->next;
        while (current != head) {
            if (current->data == value) {
                // 找到要删除的节点
                Node* prevNode = current->prev;
                Node* nextNode = current->next;
                
                // 更新连接
                prevNode->next = nextNode;
                nextNode->prev = prevNode;
                
                // 删除节点
                delete current;
                
                size--;
                return true;
            }
            current = current->next;
        }
        
        // 未找到值
        return false;
    }
    
    std::vector<int> traverseForward() const {
        std::vector<int> result;
        if (isEmpty()) {
            return result;
        }
        
        result.reserve(size);
        Node* current = head;
        do {
            result.push_back(current->data);
            current = current->next;
        } while (current != head);
        
        return result;
    }
    
    std::vector<int> traverseBackward() const {
        std::vector<int> result;
        if (isEmpty()) {
            return result;
        }
        
        result.reserve(size);
        // 从尾节点开始
        Node* current = head->prev;
        do {
            result.push_back(current->data);
            current = current->prev;
        } while (current != head->prev);
        
        return result;
    }
    
    int search(int value) const {
        if (isEmpty()) {
            return -1;
        }
        
        int index = 0;
        Node* current = head;
        do {
            if (current->data == value) {
                return index;
            }
            current = current->next;
            index++;
        } while (current != head);
        
        return -1;
    }
    
    int get(int index) const {
        if (isEmpty()) {
            throw std::runtime_error("链表为空");
        }
        
        if (index < 0 || index >= size) {
            throw std::out_of_range("索引无效: " + std::to_string(index));
        }
        
        // 优化：根据索引位置选择从头还是从尾开始遍历
        Node* current;
        if (index <= size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current->next;
            }
        } else {
            current = head->prev;
            for (int i = 0; i < size - 1 - index; i++) {
                current = current->prev;
            }
        }
        
        return current->data;
    }
    
    int set(int index, int value) {
        if (isEmpty()) {
            throw std::runtime_error("链表为空");
        }
        
        if (index < 0 || index >= size) {
            throw std::out_of_range("索引无效: " + std::to_string(index));
        }
        
        // 优化：根据索引位置选择从头还是从尾开始遍历
        Node* current;
        if (index <= size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current->next;
            }
        } else {
            current = head->prev;
            for (int i = 0; i < size - 1 - index; i++) {
                current = current->prev;
            }
        }
        
        int oldValue = current->data;
        current->data = value;
        return oldValue;
    }
    
    void clear() {
        if (isEmpty()) {
            return;
        }
        
        Node* current = head;
        do {
            Node* next = current->next;
            delete current;
            current = next;
        } while (current != head);
        
        head = nullptr;
        size = 0;
    }
    
    void reverse() {
        if (isEmpty() || size == 1) {
            return;  // 空链表或只有一个节点不需要反转
        }
        
        // 保存头节点和尾节点
        Node* current = head;
        Node* tail = head->prev;
        
        // 交换每个节点的prev和next指针
        do {
            // 交换prev和next
            std::swap(current->prev, current->next);
            
            // 移动到下一个节点（现在是prev指针）
            current = current->prev;
            
        } while (current != head);
        
        // 更新头节点为原来的尾节点
        head = tail;
    }
    
    void rotate(int k) {
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
            Node* newHead = head;
            for (int i = 0; i < size - k; i++) {
                newHead = newHead->next;
            }
            
            // 更新头节点
            head = newHead;
        }
    }
    
    void printList() const {
        if (isEmpty()) {
            std::cout << "链表为空" << std::endl;
            return;
        }
        
        Node* current = head;
        do {
            std::cout << current->data;
            if (current->next != head) {
                std::cout << " <-> ";
            }
            current = current->next;
        } while (current != head);
        
        std::cout << " (循环)" << std::endl;
    }
};

// ================================
// 7. 斐波那契堆
// ================================

class FibonacciHeap {
private:
    struct Node {
        int key;
        Node* parent;
        Node* child;
        Node* left;
        Node* right;
        int degree;
        bool mark;
        
        Node(int key) : key(key), parent(nullptr), child(nullptr), 
                       left(this), right(this), degree(0), mark(false) {}
    };
    
    Node* min;  // 最小节点
    int size;   // 节点数量
    
    // 将节点x链接到节点y的右侧
    void linkNodes(Node* x, Node* y) {
        x->left->right = x->right;
        x->right->left = x->left;
        
        x->parent = y;
        
        if (y->child == nullptr) {
            y->child = x;
            x->left = x->right = x;
        } else {
            x->right = y->child->right;
            x->left = y->child;
            y->child->right->left = x;
            y->child->right = x;
        }
        
        y->degree++;
        x->mark = false;
    }
    
    // 合并根链表
    void consolidate() {
        // 最大度数不超过log_phi(size)，这里用size作为上限
        std::vector<Node*> A(size + 1, nullptr);
        
        Node* current = min;
        Node* start = min;
        bool end = false;
        
        do {
            Node* next = current->right;
            int d = current->degree;
            
            // 合并度数相同的树
            while (A[d] != nullptr) {
                Node* y = A[d];
                if (current->key > y->key) {
                    std::swap(current, y);
                }
                
                linkNodes(y, current);
                A[d] = nullptr;
                d++;
            }
            
            A[d] = current;
            current = next;
            
            if (current == start) {
                end = true;
            }
        } while (!end);
        
        // 重新设置min节点
        min = nullptr;
        for (int i = 0; i <= size; i++) {
            if (A[i] != nullptr) {
                if (min == nullptr || A[i]->key < min->key) {
                    min = A[i];
                }
            }
        }
    }
    
    // 级联剪枝
    void cascadingCut(Node* y) {
        Node* z = y->parent;
        if (z != nullptr) {
            if (!y->mark) {
                y->mark = true;
            } else {
                cut(y, z);
                cascadingCut(z);
            }
        }
    }
    
    // 剪枝操作
    void cut(Node* x, Node* y) {
        // 从y的子节点中移除x
        x->left->right = x->right;
        x->right->left = x->left;
        y->degree--;
        
        if (y->child == x) {
            y->child = (x->right != x) ? x->right : nullptr;
        }
        
        // 将x添加到根链表
        x->parent = nullptr;
        x->mark = false;
        x->left = x->right = x;
        
        // 链接到min节点
        x->right = min->right;
        x->left = min;
        min->right->left = x;
        min->right = x;
        
        // 更新min节点
        if (x->key < min->key) {
            min = x;
        }
    }
    
    // 析构辅助函数
    void destroyNodes(Node* node) {
        if (node == nullptr) {
            return;
        }
        
        Node* current = node;
        do {
            Node* next = current->right;
            destroyNodes(current->child);
            delete current;
            current = next;
        } while (current != node);
    }
    
public:
    FibonacciHeap() : min(nullptr), size(0) {}
    
    ~FibonacciHeap() {
        if (min != nullptr) {
            destroyNodes(min);
        }
    }
    
    bool isEmpty() const {
        return min == nullptr;
    }
    
    int getSize() const {
        return size;
    }
    
    // 插入操作 - O(1)
    void insert(int key) {
        Node* newNode = new Node(key);
        
        if (min == nullptr) {
            min = newNode;
        } else {
            // 将新节点链接到min节点的右侧
            newNode->right = min->right;
            newNode->left = min;
            min->right->left = newNode;
            min->right = newNode;
            
            // 更新min节点
            if (newNode->key < min->key) {
                min = newNode;
            }
        }
        
        size++;
    }
    
    // 获取最小值 - O(1)
    int getMin() const {
        if (isEmpty()) {
            throw std::runtime_error("堆为空");
        }
        return min->key;
    }
    
    // 提取最小值 - O(log n) 均摊
    int extractMin() {
        if (isEmpty()) {
            throw std::runtime_error("堆为空");
        }
        
        Node* z = min;
        int minKey = z->key;
        
        // 将z的所有子节点添加到根链表
        if (z->child != nullptr) {
            Node* child = z->child;
            do {
                Node* next = child->right;
                
                // 断开与父节点的连接
                child->parent = nullptr;
                
                // 添加到根链表
                child->right = min->right;
                child->left = min;
                min->right->left = child;
                min->right = child;
                
                child = next;
            } while (child != z->child);
        }
        
        // 从根链表中移除z
        z->left->right = z->right;
        z->right->left = z->left;
        
        size--;
        
        if (z == z->right) {
            // 堆为空
            min = nullptr;
        } else {
            min = z->right;
            consolidate();
        }
        
        delete z;
        return minKey;
    }
    
    // 合并两个斐波那契堆 - O(1)
    void merge(FibonacciHeap& other) {
        if (other.isEmpty()) {
            return;
        }
        
        if (isEmpty()) {
            min = other.min;
            size = other.size;
            other.min = nullptr;
            other.size = 0;
            return;
        }
        
        // 合并根链表
        Node* thisRight = min->right;
        Node* otherRight = other.min->right;
        
        min->right = otherRight;
        otherRight->left = min;
        
        other.min->right = thisRight;
        thisRight->left = other.min;
        
        // 更新min节点
        if (other.min->key < min->key) {
            min = other.min;
        }
        
        size += other.size;
        
        // 重置other堆
        other.min = nullptr;
        other.size = 0;
    }
    
    // 减少键值 - O(1) 均摊
    void decreaseKey(Node* x, int newKey) {
        if (newKey > x->key) {
            throw std::invalid_argument("新键值大于当前键值");
        }
        
        x->key = newKey;
        Node* y = x->parent;
        
        if (y != nullptr && x->key < y->key) {
            cut(x, y);
            cascadingCut(y);
        }
        
        if (x->key < min->key) {
            min = x;
        }
    }
    
    // 删除节点 - O(log n) 均摊
    void deleteNode(Node* x) {
        decreaseKey(x, std::numeric_limits<int>::min());
        extractMin();
    }
};

// ================================
// 8. 块状链表 (Unrolled Linked List)
// ================================

class UnrolledLinkedList {
private:
    struct Block {
        std::vector<int> data;
        Block* next;
        
        Block(size_t blockSize) : next(nullptr) {
            data.reserve(blockSize);
        }
        
        bool isFull(size_t blockSize) const {
            return data.size() >= blockSize;
        }
        
        bool isEmpty() const {
            return data.empty();
        }
    };
    
    Block* head;
    Block* tail;
    int size;
    const size_t blockSize;
    const size_t minBlockSize;  // 重平衡时保留的最小元素数量
    
    // 插入到指定块的指定位置
    void insertInBlock(Block* block, size_t pos, int value) {
        if (block->isFull(blockSize)) {
            // 块已满，需要分裂
            Block* newBlock = new Block(blockSize);
            
            // 将后半部分数据移到新块
            size_t mid = block->data.size() / 2;
            newBlock->data.insert(newBlock->data.end(), 
                                block->data.begin() + mid, 
                                block->data.end());
            block->data.erase(block->data.begin() + mid, block->data.end());
            
            // 插入新块到链表中
            newBlock->next = block->next;
            block->next = newBlock;
            
            // 更新tail
            if (block == tail) {
                tail = newBlock;
            }
            
            // 决定在哪个块插入
            if (pos > mid) {
                block = newBlock;
                pos -= mid;
            }
        }
        
        // 在块中的指定位置插入元素
        block->data.insert(block->data.begin() + pos, value);
        size++;
    }
    
    // 从指定块的指定位置删除
    int deleteFromBlock(Block* block, Block* prevBlock, size_t pos) {
        int value = block->data[pos];
        block->data.erase(block->data.begin() + pos);
        size--;
        
        // 检查是否需要合并块
        if (block->data.size() < minBlockSize && block != head) {
            // 尝试从下一个块借一个元素
            if (block->next && block->next->data.size() > minBlockSize) {
                block->data.push_back(block->next->data[0]);
                block->next->data.erase(block->next->data.begin());
            } 
            // 尝试从上一个块借一个元素
            else if (prevBlock && prevBlock->data.size() > minBlockSize) {
                block->data.insert(block->data.begin(), prevBlock->data.back());
                prevBlock->data.pop_back();
            } 
            // 合并到前一个块
            else if (prevBlock) {
                prevBlock->data.insert(prevBlock->data.end(), 
                                     block->data.begin(), 
                                     block->data.end());
                prevBlock->next = block->next;
                
                if (block == tail) {
                    tail = prevBlock;
                }
                
                delete block;
            } 
            // 不能合并（只有一个块）
            else {
                // 单块情况下不做处理
            }
        }
        
        return value;
    }
    
    // 查找元素位置，返回块指针和块内偏移
    std::pair<Block*, size_t> findPosition(int index) const {
        if (index < 0 || index >= size) {
            throw std::out_of_range("索引无效: " + std::to_string(index));
        }
        
        Block* current = head;
        int currentIndex = 0;
        
        while (currentIndex + current->data.size() <= index) {
            currentIndex += current->data.size();
            current = current->next;
        }
        
        return {current, index - currentIndex};
    }
    
public:
    UnrolledLinkedList(size_t blockSize = 16) 
        : head(nullptr), tail(nullptr), size(0), 
          blockSize(blockSize), minBlockSize(std::max(size_t(1), blockSize / 4)) {}
    
    ~UnrolledLinkedList() {
        clear();
    }
    
    bool isEmpty() const {
        return size == 0;
    }
    
    int getSize() const {
        return size;
    }
    
    void insert(int index, int value) {
        if (index < 0 || index > size) {
            throw std::out_of_range("插入位置无效: " + std::to_string(index));
        }
        
        // 空列表情况
        if (isEmpty()) {
            head = new Block(blockSize);
            tail = head;
            head->data.push_back(value);
            size++;
            return;
        }
        
        // 插入到开头
        if (index == 0) {
            insertInBlock(head, 0, value);
            return;
        }
        
        // 插入到末尾
        if (index == size) {
            // 检查最后一个块是否已满
            if (!tail->isFull(blockSize)) {
                tail->data.push_back(value);
                size++;
            } else {
                // 最后一个块已满，先找到位置再插入
                auto result = findPosition(index - 1);
                auto block = result.first;
                auto pos = result.second;
                insertInBlock(block, pos + 1, value);
            }
            return;
        }
        
        // 插入到中间位置
        auto result = findPosition(index);
        auto block = result.first;
        auto pos = result.second;
        insertInBlock(block, pos, value);
    }
    
    int deleteAt(int index) {
        if (isEmpty()) {
            throw std::runtime_error("列表为空");
        }
        
        if (index < 0 || index >= size) {
            throw std::out_of_range("删除位置无效: " + std::to_string(index));
        }
        
        // 找到块和位置
        Block* current = head;
        Block* prev = nullptr;
        int currentIndex = 0;
        
        while (currentIndex + current->data.size() <= index) {
            prev = current;
            currentIndex += current->data.size();
            current = current->next;
        }
        
        size_t posInBlock = index - currentIndex;
        int value = deleteFromBlock(current, prev, posInBlock);
        
        // 检查是否需要更新head
        if (head->isEmpty()) {
            Block* oldHead = head;
            head = head->next;
            delete oldHead;
            
            // 如果删除后为空，更新tail
            if (head == nullptr) {
                tail = nullptr;
            }
        }
        
        return value;
    }
    
    int get(int index) const {
        if (isEmpty()) {
            throw std::runtime_error("列表为空");
        }
        
        auto result = findPosition(index);
        auto block = result.first;
        auto pos = result.second;
        return block->data[pos];
    }
    
    void set(int index, int value) {
        if (isEmpty()) {
            throw std::runtime_error("列表为空");
        }
        
        auto result = findPosition(index);
        auto block = result.first;
        auto pos = result.second;
        block->data[pos] = value;
    }
    
    std::vector<int> toVector() const {
        std::vector<int> result;
        result.reserve(size);
        
        Block* current = head;
        while (current != nullptr) {
            result.insert(result.end(), current->data.begin(), current->data.end());
            current = current->next;
        }
        
        return result;
    }
    
    void clear() {
        Block* current = head;
        while (current != nullptr) {
            Block* next = current->next;
            delete current;
            current = next;
        }
        
        head = tail = nullptr;
        size = 0;
    }
    
    void printList() const {
        if (isEmpty()) {
            std::cout << "列表为空" << std::endl;
            return;
        }
        
        Block* current = head;
        int blockIndex = 0;
        
        while (current != nullptr) {
            std::cout << "块 " << blockIndex++ << ": [";
            for (size_t i = 0; i < current->data.size(); i++) {
                std::cout << current->data[i];
                if (i < current->data.size() - 1) {
                    std::cout << ", ";
                }
            }
            std::cout << "]" << std::endl;
            current = current->next;
        }
    }
};

// ================================
// 主函数，用于测试
// ================================

int main() {
    std::cout << "=== C++高级数据结构与算法实现 ===" << std::endl;
    
    // 测试平面分治算法
    std::cout << "\n=== 测试平面分治算法 - 最近点对 ===" << std::endl;
    std::vector<Point> points = {Point(0, 0), Point(3, 0), Point(0, 4), Point(1, 1)};
    try {
        PairDistance result = closestPair(points);
        std::cout << "最小距离: " << result.distance << std::endl;
        std::cout << "最近点对: " << result.p1 << " 和 " << result.p2 << std::endl;
    } catch (const std::exception& e) {
        std::cerr << "错误: " << e.what() << std::endl;
    }
    
    // 测试生命游戏
    std::cout << "\n=== 测试生命游戏 ===" << std::endl;
    std::vector<std::vector<int>> initialBoard = {
        {0, 1, 0},
        {0, 1, 0},
        {0, 1, 0}
    };
    try {
        GameOfLife game(initialBoard);
        std::cout << "初始状态:" << std::endl;
        game.printBoard();
        
        game.simulate(1, true);
        std::cout << "下一代状态:" << std::endl;
        game.printBoard();
    } catch (const std::exception& e) {
        std::cerr << "错误: " << e.what() << std::endl;
    }
    
    // 测试双向循环链表
    std::cout << "\n=== 测试双向循环链表 ===" << std::endl;
    try {
        DoublyCircularLinkedList list;
        list.insertAtHead(1);
        list.insertAtTail(3);
        list.insertAtPosition(1, 2);
        
        std::cout << "链表内容: ";
        list.printList();
        
        std::cout << "正向遍历: ";
        for (int val : list.traverseForward()) {
            std::cout << val << " ";
        }
        std::cout << std::endl;
        
        std::cout << "反向遍历: ";
        for (int val : list.traverseBackward()) {
            std::cout << val << " ";
        }
        std::cout << std::endl;
    } catch (const std::exception& e) {
        std::cerr << "错误: " << e.what() << std::endl;
    }
    
    // 测试块状链表
    std::cout << "\n=== 测试块状链表 ===" << std::endl;
    try {
        UnrolledLinkedList ull(4);
        ull.insert(0, 1);
        ull.insert(1, 2);
        ull.insert(2, 3);
        ull.insert(3, 4);
        ull.insert(4, 5);
        
        std::cout << "块状链表内容:" << std::endl;
        ull.printList();
        
        std::cout << "转换为数组: ";
        for (int val : ull.toVector()) {
            std::cout << val << " ";
        }
        std::cout << std::endl;
    } catch (const std::exception& e) {
        std::cerr << "错误: " << e.what() << std::endl;
    }
    
    return 0;
}