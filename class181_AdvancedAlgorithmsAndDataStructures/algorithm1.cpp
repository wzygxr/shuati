#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <stdexcept>
#include <memory>
#include <limits>
#include <queue>
#include <tuple>
#include <string>
#include <unordered_set>
#include <unordered_map>
#include <functional>

/**
 * C++版本综合算法与数据结构实现
 * 包含：
 * 1. 平面分治 (Closest Pair of Points)
 * 2. 棋盘模拟 (Game of Life)
 * 3. 间隔打表 (Sparse Table)
 * 4. 事件排序 (Time Sweep)
 * 5. 差分驱动模拟 (Difference Array)
 * 
 * 时间复杂度分析：
 * - 平面分治: O(n log n)
 * - 棋盘模拟: O(m*n)
 * - 间隔打表: O(n log n) 预处理, O(1) 查询
 * - 事件排序: O(n log n)
 * - 差分驱动: O(1) 区间更新, O(n) 获取结果
 */

// ================================
// 1. 平面分治 - 最近点对问题
// ================================

/**
 * 平面点类，表示二维平面上的一个点
 */
struct Point {
    double x, y;
    
    /**
     * 构造函数
     * @param x 点的x坐标
     * @param y 点的y坐标
     */
    Point(double x = 0, double y = 0) : x(x), y(y) {}
    
    /**
     * 计算与另一个点的欧几里得距离
     * @param other 另一个点
     * @return 两点间的距离
     */
    double distanceTo(const Point& other) const {
        double dx = this->x - other.x;
        double dy = this->y - other.y;
        return std::sqrt(dx * dx + dy * dy);
    }
    
    /**
     * 按x坐标排序的比较函数
     * @param other 另一个点
     * @return 如果当前点的x坐标小于另一个点，则返回true
     */
    bool operator<(const Point& other) const {
        return x < other.x;
    }
    
    /**
     * 输出运算符重载
     */
    friend std::ostream& operator<<(std::ostream& os, const Point& p) {
        return os << "Point(" << p.x << ", " << p.y << ")";
    }
};

/**
 * 点对距离结果类，存储最近点对及其距离
 */
struct PairDistance {
    double distance;  // 两点间距离
    Point p1, p2;     // 两个点
    
    /**
     * 构造函数
     * @param distance 距离
     * @param p1 第一个点
     * @param p2 第二个点
     */
    PairDistance(double distance = 0, Point p1 = Point(), Point p2 = Point()) 
        : distance(distance), p1(p1), p2(p2) {}
};

/**
 * 按y坐标排序的比较函数
 * @param a 第一个点
 * @param b 第二个点
 * @return 如果a的y坐标小于b的y坐标，则返回true
 */
bool compareByY(const Point& a, const Point& b) {
    return a.y < b.y;
}

/**
 * 暴力法计算最近点对
 * 时间复杂度：O(n^2)
 * @param points 点的集合
 * @return 最近点对及其距离
 */
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

/**
 * 计算带内（跨越中线的区域）的最近点对
 * 时间复杂度：O(n) - 由于每个点最多检查常数个点
 * @param strip 带内的点集
 * @param currentMin 当前已知的最小距离
 * @return 带内的最近点对
 */
PairDistance stripClosest(std::vector<Point>& strip, PairDistance currentMin) {
    double minDist = currentMin.distance;
    Point p1 = currentMin.p1;
    Point p2 = currentMin.p2;
    
    // 按y坐标排序已经完成
    int size = strip.size();
    
    // 对于带内的每个点，只需要检查后面y坐标相差不超过min_dist的点
    // 理论上只需要检查最多6个点，这是平面分治算法的关键优化点
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

/**
 * 递归实现平面分治算法
 * @param pointsSortedByX 按x坐标排序的点集
 * @param pointsSortedByY 按y坐标排序的点集
 * @return 最近点对及其距离
 */
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

/**
 * 平面分治算法主函数 - 最近点对问题
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 * @param points 点的集合
 * @return 最近点对及其距离
 * @throws std::invalid_argument 当输入无效时抛出异常
 */
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

/**
 * 生命游戏模拟器类
 * 实现康威生命游戏的模拟
 */
class GameOfLife {
private:
    std::vector<std::vector<int>> board;  // 棋盘
    int rows;                             // 行数
    int cols;                             // 列数
    
    /**
     * 计算指定位置的邻居数量
     * @param board 当前棋盘
     * @param row 行索引
     * @param col 列索引
     * @return 邻居数量
     */
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
    /**
     * 构造函数
     * @param initialBoard 初始棋盘状态
     * @throws std::invalid_argument 当输入棋盘为空时抛出异常
     */
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
    
    /**
     * 标准方法计算下一代状态（使用额外空间）
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(m*n)
     * @return 更新后的棋盘状态
     */
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
    
    /**
     * 原地计算下一代状态（节省空间）
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(1)
     * @return 更新后的棋盘状态
     */
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
    
    /**
     * 模拟多代生命游戏
     * @param generations 模拟的代数
     * @param inplace 是否使用原地算法
     * @return 模拟后的棋盘状态
     */
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
    
    /**
     * 获取当前棋盘状态的副本
     * @return 棋盘状态的深拷贝
     */
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
    
    /**
     * 打印当前棋盘状态
     */
    void printBoard() const {
        for (const auto& row : board) {
            for (int cell : row) {
                std::cout << (cell == 1 ? '█' : ' ') << ' ';
            }
            std::cout << std::endl;
        }
        std::cout << std::endl;
    }
};

// ================================
// 3. 间隔打表 - 稀疏表
// ================================

/**
 * 稀疏表（Sparse Table）类
 * 用于高效地回答区间查询问题，如区间最小值、最大值等
 * 预处理时间复杂度：O(n log n)
 * 查询时间复杂度：O(1)
 */
class SparseTable {
private:
    std::vector<int> data;                    // 原始数据
    std::vector<std::vector<int>> st;         // 稀疏表结构
    std::vector<int> logTable;                // 预计算的log表
    
    // 用于区间查询的函数类型
    using MergeFunc = std::function<int(int, int)>;
    MergeFunc mergeFunc;                      // 区间合并函数
    
    /**
     * 构建log表，用于快速计算区间长度对应的k值
     * @return log表
     */
    std::vector<int> buildLogTable() {
        int n = data.size();
        std::vector<int> logTable(n + 1, 0);
        for (int i = 2; i <= n; i++) {
            logTable[i] = logTable[i / 2] + 1;
        }
        return logTable;
    }
    
    /**
     * 构建稀疏表
     * @return 稀疏表结构
     */
    std::vector<std::vector<int>> buildSparseTable() {
        int n = data.size();
        int kMax = logTable[n] + 1;
        std::vector<std::vector<int>> st(kMax, std::vector<int>(n));
        
        // 初始化k=0的情况（长度为1的区间）
        for (int i = 0; i < n; i++) {
            st[0][i] = data[i];
        }
        
        // 动态规划构建其他k值
        // st[k][i] 表示从i开始，长度为2^k的区间的合并结果
        for (int k = 1; (1 << k) <= n; k++) {
            for (int i = 0; i + (1 << k) <= n; i++) {
                // 合并两个长度为2^(k-1)的区间
                st[k][i] = mergeFunc(
                    st[k-1][i],                     // 左侧区间
                    st[k-1][i + (1 << (k-1))]        // 右侧区间
                );
            }
        }
        
        return st;
    }
    
public:
    /**
     * 构造函数
     * @param data 原始数据数组
     * @param func 区间合并函数（默认使用最小值函数）
     * @throws std::invalid_argument 当输入数据为空时抛出异常
     */
    SparseTable(const std::vector<int>& data, MergeFunc func) 
        : data(data), mergeFunc(func) {
        if (data.empty()) {
            throw std::invalid_argument("输入数据不能为空");
        }
        
        this->logTable = buildLogTable();
        this->st = buildSparseTable();
    }
    
    /**
     * 默认构造函数，使用最小值作为合并函数
     * @param data 原始数据数组
     */
    SparseTable(const std::vector<int>& data) 
        : SparseTable(data, [](int a, int b) { return std::min(a, b); }) {}
    
    /**
     * 查询区间[l, r]的合并结果
     * 时间复杂度：O(1)
     * @param l 区间左端点（包含）
     * @param r 区间右端点（包含）
     * @return 区间合并结果
     * @throws std::invalid_argument 当区间无效时抛出异常
     */
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
        // 第一个区间从l开始，长度为2^k
        // 第二个区间从r-2^k+1开始，长度为2^k
        // 两个区间的并集正好覆盖[l, r]
        return mergeFunc(
            st[k][l],
            st[k][r - (1 << k) + 1]
        );
    }
    
    /**
     * 批量查询多个区间
     * @param queries 查询的区间列表
     * @return 查询结果列表
     */
    std::vector<int> batchQuery(const std::vector<std::pair<int, int>>& queries) {
        std::vector<int> results;
        results.reserve(queries.size());
        for (const auto& query : queries) {
            results.push_back(queryRange(query.first, query.second));
        }
        return results;
    }
    
    /**
     * 检查区间内所有元素是否相同
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 如果区间内所有元素相同则返回true
     */
    bool isRangeAllSame(int l, int r) {
        if (l == r) {
            return true;
        }
        return queryRange(l, r) == data[l];
    }
    
    /**
     * 获取区间的极值（取决于mergeFunc）
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 区间极值
     */
    int getRangeExtreme(int l, int r) {
        return queryRange(l, r);
    }
};

// ================================
// 4. 事件排序 - 扫描线算法
// ================================

/**
 * 事件排序（扫描线）算法实现类
 */
class EventSweep {
public:
    /**
     * 区间重叠结果类
     */
    struct OverlapResult {
        int maxOverlap;                      // 最大重叠次数
        std::vector<std::pair<int, int>> overlappingIntervals;  // 重叠的区间
        
        OverlapResult(int maxOverlap = 0, 
                     const std::vector<std::pair<int, int>>& overlappingIntervals = {}) 
            : maxOverlap(maxOverlap), overlappingIntervals(overlappingIntervals) {}
    };
    
    /**
     * 区间事件类，用于排序
     */
    struct Event {
        int pos;    // 事件位置
        int type;   // 1表示开始，-1表示结束
        int index;  // 对应的区间索引
        
        Event(int pos = 0, int type = 0, int index = 0) 
            : pos(pos), type(type), index(index) {}
        
        // 按位置排序，当位置相同时，结束事件（type=-1）优先
        bool operator<(const Event& other) const {
            if (this->pos != other.pos) {
                return this->pos < other.pos;
            }
            return this->type < other.type;  // 结束事件（type=-1）优先于开始事件（type=1）
        }
    };
    
    /**
     * 计算区间集合中的最大重叠次数和重叠的区间
     * 时间复杂度：O(n log n)，主要来自排序
     * 空间复杂度：O(n)
     * @param intervals 区间集合，每个区间表示为[start, end]
     * @return 重叠结果
     * @throws std::invalid_argument 当区间无效时抛出异常
     */
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
        std::unordered_set<int> activeIntervals;      // 当前活动的区间索引集合
        std::unordered_set<int> maxOverlapIntervalIndices;  // 达到最大重叠时的区间索引集合
        
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
    
    /**
     * 矩形事件类，用于矩形面积计算
     */
    struct RectangleEvent {
        int x;       // x坐标
        bool isStart; // 是否是矩形的左边界
        int y1, y2;  // y区间
        
        RectangleEvent(int x = 0, bool isStart = false, int y1 = 0, int y2 = 0) 
            : x(x), isStart(isStart), y1(y1), y2(y2) {}
        
        // 按x坐标排序
        bool operator<(const RectangleEvent& other) const {
            return this->x < other.x;
        }
    };
    
    /**
     * 计算多个矩形覆盖的总面积（去重）
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     * @param rectangles 矩形集合，每个矩形表示为(x1, y1, x2, y2)
     * @return 覆盖的总面积
     * @throws std::invalid_argument 当矩形无效时抛出异常
     */
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
            events.emplace_back(x1, true, y1, y2);  // 左边界（开始）
            events.emplace_back(x2, false, y1, y2); // 右边界（结束）
            xCoords.insert(x1);
            xCoords.insert(x2);
        }
        
        // 按x坐标排序事件
        std::sort(events.begin(), events.end());
        
        std::vector<int> sortedX(xCoords.begin(), xCoords.end());
        std::sort(sortedX.begin(), sortedX.end());
        
        std::vector<std::pair<int, int>> activeIntervals;  // 当前活动的y区间
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
    /**
     * 合并重叠的区间并计算总长度
     * @param intervals 区间集合
     * @return 合并后的总长度
     */
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

/**
 * 差分数组类
 * 用于高效地进行区间更新操作
 * 区间更新时间复杂度：O(1)
 * 获取结果时间复杂度：O(n)
 */
class DifferenceArray {
private:
    std::vector<int> diff;  // 差分数组
    int size;              // 数组大小
    
public:
    /**
     * 构造函数，创建指定大小的差分数组（初始化为全0）
     * @param size 数组大小
     * @throws std::invalid_argument 当大小无效时抛出异常
     */
    DifferenceArray(int size) {
        if (size <= 0) {
            throw std::invalid_argument("数组大小必须为正数");
        }
        this->size = size;
        this->diff.resize(size, 0);  // 初始化为全0数组的差分数组
    }
    
    /**
     * 构造函数，从初始数组构建差分数组
     * @param initialArray 初始数组
     * @throws std::invalid_argument 当初始数组为空时抛出异常
     */
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
    
    /**
     * 对区间[l, r]加上值val
     * 时间复杂度：O(1)
     * @param l 区间左端点（包含）
     * @param r 区间右端点（包含）
     * @param val 要加的值
     * @throws std::invalid_argument 当区间无效时抛出异常
     */
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
    
    /**
     * 获取差分数组对应的原始数组
     * 时间复杂度：O(n)
     * @return 原始数组
     */
    std::vector<int> getResult() {
        std::vector<int> res(size);
        res[0] = diff[0];
        
        // 前缀和恢复原始数组
        for (int i = 1; i < size; i++) {
            res[i] = res[i-1] + diff[i];
        }
        
        return res;
    }
    
    /**
     * 获取差分数组的副本
     * @return 差分数组副本
     */
    std::vector<int> getDifferenceArray() const {
        return diff;  // 返回副本
    }
    
    /**
     * 执行多个区间更新操作
     * @param updates 更新操作列表，每个更新为(l, r, val)
     */
    void multipleRangeUpdates(const std::vector<std::tuple<int, int, int>>& updates) {
        for (const auto& update : updates) {
            rangeAdd(std::get<0>(update), std::get<1>(update), std::get<2>(update));
        }
    }
    
    /**
     * 重置差分数组为全0
     */
    void reset() {
        std::fill(diff.begin(), diff.end(), 0);
    }
};

// ================================
// 主函数，用于测试各种算法
// ================================

/**
 * 测试函数：打印算法测试结果
 */
void testAlgorithms() {
    std::cout << "=== C++高级算法实现测试 ===" << std::endl;
    
    // 测试平面分治算法 - 最近点对
    std::cout << "\n=== 测试平面分治算法 - 最近点对 ===" << std::endl;
    std::vector<Point> points = {Point(0, 0), Point(3, 0), Point(0, 4), Point(1, 1), Point(5, 5), Point(4, 4)};
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
        
        std::cout << "使用原地算法模拟下一代:" << std::endl;
        game.nextGenerationInplace();
        game.printBoard();
        
        std::cout << "使用标准算法模拟下一代:" << std::endl;
        game.nextGenerationStandard();
        game.printBoard();
    } catch (const std::exception& e) {
        std::cerr << "错误: " << e.what() << std::endl;
    }
    
    // 测试稀疏表
    std::cout << "\n=== 测试稀疏表（区间最小值查询） ===" << std::endl;
    std::vector<int> data = {1, 3, 5, 7, 9, 11, 13, 15};
    try {
        SparseTable stMin(data);  // 默认使用最小值
        
        std::cout << "区间[1, 4]的最小值: " << stMin.queryRange(1, 4) << std::endl;
        std::cout << "区间[3, 6]的最小值: " << stMin.queryRange(3, 6) << std::endl;
        
        // 测试最大值查询
        SparseTable stMax(data, [](int a, int b) { return std::max(a, b); });
        std::cout << "区间[1, 4]的最大值: " << stMax.queryRange(1, 4) << std::endl;
    } catch (const std::exception& e) {
        std::cerr << "错误: " << e.what() << std::endl;
    }
    
    // 测试事件排序算法
    std::cout << "\n=== 测试事件排序算法 - 区间重叠 ===" << std::endl;
    std::vector<std::pair<int, int>> intervals = { {1, 3}, {2, 5}, {4, 8}, {6, 10}, {7, 12} };
    try {
        EventSweep::OverlapResult overlapResult = EventSweep::intervalOverlap(intervals);
        std::cout << "最大重叠次数: " << overlapResult.maxOverlap << std::endl;
        std::cout << "重叠的区间: " << std::endl;
        for (const auto& interval : overlapResult.overlappingIntervals) {
            std::cout << "[" << interval.first << ", " << interval.second << "]" << std::endl;
        }
    } catch (const std::exception& e) {
        std::cerr << "错误: " << e.what() << std::endl;
    }
    
    // 测试差分数组
    std::cout << "\n=== 测试差分数组 ===" << std::endl;
    try {
        DifferenceArray diffArray(10);
        
        // 对区间[2, 5]加上3
        diffArray.rangeAdd(2, 5, 3);
        // 对区间[1, 7]加上2
        diffArray.rangeAdd(1, 7, 2);
        
        std::vector<int> result = diffArray.getResult();
        std::cout << "差分数组结果: ";
        for (int val : result) {
            std::cout << val << " ";
        }
        std::cout << std::endl;
    } catch (const std::exception& e) {
        std::cerr << "错误: " << e.what() << std::endl;
    }
    
    // 测试矩形面积计算
    std::cout << "\n=== 测试矩形面积计算 ===" << std::endl;
    std::vector<std::tuple<int, int, int, int>> rectangles = {
        {1, 1, 3, 3},
        {2, 2, 4, 4},
        {5, 5, 7, 7}
    };
    try {
        int area = EventSweep::rectangleArea(rectangles);
        std::cout << "矩形覆盖的总面积: " << area << std::endl;
    } catch (const std::exception& e) {
        std::cerr << "错误: " << e.what() << std::endl;
    }
}

/**
 * 主函数
 */
int main() {
    // 运行算法测试
    testAlgorithms();
    
    return 0;
}

// ================================
// 相关算法题目及思路总结
// ================================

/*

平面分治算法相关题目：
1. LeetCode 973. K Closest Points to Origin
2. LeetCode 826. Most Profit Assigning Work
3. POJ 3714 Raid
4. HDU 1021 Fibonacci Again

思路总结：
- 平面分治适用于需要在二维平面上处理大量点的数据问题
- 核心思想是将平面递归地分割，然后合并子问题的解
- 关键优化是在合并阶段只考虑跨越分割线的有限数量的候选点

生命游戏相关题目：
1. LeetCode 289. Game of Life
2. LeetCode 733. Flood Fill
3. CodeChef - LIFE
4. Codeforces - 961E Tufurama

思路总结：
- 棋盘模拟问题通常涉及状态转移规则
- 关键在于高效地表示和更新状态，避免状态更新相互影响
- 原地算法可以通过编码中间状态来实现

稀疏表相关题目：
1. LeetCode 2448. Minimum Cost to Make Array Equal
2. LeetCode 1439. Find the Kth Smallest Sum of a Matrix With Sorted Rows
3. Codeforces - 1202E. You Are Given Some Strings...
4. AtCoder - ARC098D Donation

思路总结：
- 稀疏表适用于区间查询问题，尤其是静态数据的重复查询
- 预处理时间O(n log n)，查询时间O(1)
- 可以灵活应用于各种区间合并操作，如min、max、gcd等

事件排序相关题目：
1. LeetCode 253. Meeting Rooms II
2. LeetCode 759. Employee Free Time
3. Codeforces - 981E Addition on Segments
4. POJ 3273 Monthly Expense

思路总结：
- 事件排序的核心是将问题转化为事件点的处理
- 通过排序和扫描，可以高效地处理区间覆盖、重叠等问题
- 常用于区间调度、资源分配等场景

差分数组相关题目：
1. LeetCode 1109. Corporate Flight Bookings
2. LeetCode 1094. Car Pooling
3. Codeforces - 445B DZY Loves Chemistry
4. POJ 2352 Stars

思路总结：
- 差分数组是处理区间更新操作的高效工具
- 区间更新时间复杂度O(1)，是处理大量区间操作的理想选择
- 结合前缀和，可以轻松还原原始数组

*/