/**
 * 剪枝体系 (Pruning Techniques)
 * 
 * 算法原理：
 * 剪枝是一种优化技术，通过提前终止不可能产生最优解的搜索分支来减少搜索空间，
 * 从而提高算法效率。剪枝技术广泛应用于回溯算法、博弈树搜索、分支限界等场景。
 * 
 * 算法特点：
 * 1. 减少搜索空间，提高算法效率
 * 2. 不影响最终结果的正确性
 * 3. 需要设计合适的剪枝条件
 * 4. 剪枝效果与问题特性密切相关
 * 
 * 应用场景：
 * - 回溯算法（N皇后、数独等）
 * - 博弈树搜索（Alpha-Beta剪枝）
 * - 分支限界算法
 * - 组合优化问题
 * 
 * 剪枝类型：
 * 1. 可行性剪枝：提前判断当前分支是否可能产生可行解
 * 2. 最优性剪枝：提前判断当前分支是否可能产生更优解
 * 3. 记忆化剪枝：避免重复计算相同子问题
 * 4. 启发式剪枝：基于启发信息进行剪枝
 * 
 * 时间复杂度：取决于具体问题和剪枝效果
 * 空间复杂度：取决于具体实现
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <map>
#include <climits>
#include <chrono>

using namespace std;

class PruningTechniques {
public:
    /**
     * N皇后问题 - 可行性剪枝示例
     * 
     * @param n 皇后数量
     * @return 所有解的数量
     */
    static int solveNQueens(int n) {
        vector<int> queens(n, 0); // queens[i]表示第i行皇后所在的列
        return backtrack(queens, 0, n);
    }
    
    /**
     * 回溯算法求解N皇后问题
     * 
     * @param queens 皇后位置数组
     * @param row 当前行
     * @param n 皇后数量
     * @return 解的数量
     */
    static int backtrack(vector<int>& queens, int row, int n) {
        // 递归终止条件
        if (row == n) {
            return 1; // 找到一个解
        }
        
        int count = 0;
        
        // 在当前行尝试每一列
        for (int col = 0; col < n; col++) {
            // 可行性剪枝：检查当前位置是否合法
            if (isValid(queens, row, col)) {
                queens[row] = col; // 放置皇后
                count += backtrack(queens, row + 1, n); // 递归处理下一行
                // 回溯时不需要显式重置，因为下一次循环会覆盖
            }
            // 如果不合法，直接剪枝，不继续递归
        }
        
        return count;
    }
    
    /**
     * 检查在(row, col)位置放置皇后是否合法
     * 
     * @param queens 皇后位置数组
     * @param row 行号
     * @param col 列号
     * @return 是否合法
     */
    static bool isValid(const vector<int>& queens, int row, int col) {
        // 检查之前行的皇后是否与当前位置冲突
        for (int i = 0; i < row; i++) {
            // 检查列冲突
            if (queens[i] == col) {
                return false;
            }
            
            // 检查对角线冲突
            if (abs(queens[i] - col) == abs(i - row)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 0-1背包问题相关结构
     */
    struct KnapsackItem {
        int weight;
        int value;
        
        KnapsackItem(int w, int v) : weight(w), value(v) {}
        
        // 按价值密度排序
        bool operator<(const KnapsackItem& other) const {
            return (double)value / weight > (double)other.value / other.weight;
        }
    };
    
    /**
     * 0-1背包问题求解（带剪枝优化）
     * 
     * @param items 物品列表
     * @param capacity 背包容量
     * @return 最大价值
     */
    static int knapsackWithPruning(vector<KnapsackItem> items, int capacity) {
        // 按价值密度排序，用于剪枝
        sort(items.begin(), items.end());
        
        return knapsackBacktrack(items, capacity, 0, 0, 0, 0);
    }
    
    /**
     * 0-1背包回溯算法（带剪枝）
     * 
     * @param items 物品列表
     * @param capacity 背包容量
     * @param currentIndex 当前物品索引
     * @param currentWeight 当前重量
     * @param currentValue 当前价值
     * @param bestValue 当前最优价值
     * @return 最大价值
     */
    static int knapsackBacktrack(const vector<KnapsackItem>& items, int capacity, 
                               int currentIndex, int currentWeight, 
                               int currentValue, int bestValue) {
        // 更新最优解
        bestValue = max(bestValue, currentValue);
        
        // 递归终止条件
        if (currentIndex == items.size()) {
            return bestValue;
        }
        
        // 最优性剪枝：计算上界
        int upperBound = calculateUpperBound(items, capacity, currentIndex, 
                                           currentWeight, currentValue);
        
        // 如果上界不大于当前最优值，则剪枝
        if (upperBound <= bestValue) {
            return bestValue;
        }
        
        const KnapsackItem& currentItem = items[currentIndex];
        
        // 选择当前物品（可行性剪枝）
        if (currentWeight + currentItem.weight <= capacity) {
            bestValue = knapsackBacktrack(items, capacity, currentIndex + 1, 
                                        currentWeight + currentItem.weight, 
                                        currentValue + currentItem.value, 
                                        bestValue);
        }
        
        // 不选择当前物品
        bestValue = knapsackBacktrack(items, capacity, currentIndex + 1, 
                                    currentWeight, currentValue, bestValue);
        
        return bestValue;
    }
    
    /**
     * 计算0-1背包问题的上界（用于最优性剪枝）
     * 
     * @param items 物品列表
     * @param capacity 背包容量
     * @param currentIndex 当前物品索引
     * @param currentWeight 当前重量
     * @param currentValue 当前价值
     * @return 上界估计值
     */
    static int calculateUpperBound(const vector<KnapsackItem>& items, int capacity, 
                                 int currentIndex, int currentWeight, 
                                 int currentValue) {
        int remainingCapacity = capacity - currentWeight;
        int bound = currentValue;
        
        // 贪心法计算上界：按价值密度选择物品
        for (size_t i = currentIndex; i < items.size() && remainingCapacity > 0; i++) {
            const KnapsackItem& item = items[i];
            if (item.weight <= remainingCapacity) {
                // 完全装入
                bound += item.value;
                remainingCapacity -= item.weight;
            } else {
                // 部分装入
                bound += (int)((double)item.value / item.weight * remainingCapacity);
                remainingCapacity = 0;
            }
        }
        
        return bound;
    }
    
    /**
     * 记忆化斐波那契数列 - 记忆化剪枝示例
     */
    class FibonacciWithMemoization {
    private:
        map<int, long long> memo;
        
    public:
        /**
         * 计算第n个斐波那契数（带记忆化）
         * 
         * @param n 序号
         * @return 第n个斐波那契数
         */
        long long fibonacci(int n) {
            // 基础情况
            if (n <= 1) {
                return n;
            }
            
            // 记忆化剪枝：如果已经计算过，直接返回
            if (memo.find(n) != memo.end()) {
                return memo[n];
            }
            
            // 递归计算并存储结果
            long long result = fibonacci(n - 1) + fibonacci(n - 2);
            memo[n] = result;
            
            return result;
        }
        
        /**
         * 清空记忆化缓存
         */
        void clearMemo() {
            memo.clear();
        }
    };
    
    /**
     * Alpha-Beta剪枝 - 博弈树搜索剪枝示例
     */
    class AlphaBetaPruning {
    public:
        static const int MAX_DEPTH = 6; // 最大搜索深度
        static const int WIN_SCORE = 10000; // 获胜分数
        static const int LOSE_SCORE = -10000; // 失败分数
        
        /**
         * Alpha-Beta剪枝搜索
         * 
         * @param board 棋盘状态
         * @param depth 当前深度
         * @param alpha Alpha值
         * @param beta Beta值
         * @param isMaximizing 是否最大化玩家
         * @return 评估分数
         */
        static int alphaBetaSearch(vector<vector<int>>& board, int depth, int alpha, int beta, 
                                 bool isMaximizing) {
            // 终止条件：达到最大深度或游戏结束
            if (depth == 0 || isGameOver(board)) {
                return evaluateBoard(board);
            }
            
            if (isMaximizing) {
                int maxEval = INT_MIN;
                vector<vector<int>> moves = generateMoves(board, true);
                
                for (const auto& move : moves) {
                    // 执行移动
                    makeMove(board, move, true);
                    
                    // 递归搜索
                    int eval = alphaBetaSearch(board, depth - 1, alpha, beta, false);
                    
                    // 撤销移动
                    undoMove(board, move);
                    
                    maxEval = max(maxEval, eval);
                    alpha = max(alpha, eval);
                    
                    // Alpha-Beta剪枝
                    if (beta <= alpha) {
                        break; // beta剪枝
                    }
                }
                
                return maxEval;
            } else {
                int minEval = INT_MAX;
                vector<vector<int>> moves = generateMoves(board, false);
                
                for (const auto& move : moves) {
                    // 执行移动
                    makeMove(board, move, false);
                    
                    // 递归搜索
                    int eval = alphaBetaSearch(board, depth - 1, alpha, beta, true);
                    
                    // 撤销移动
                    undoMove(board, move);
                    
                    minEval = min(minEval, eval);
                    beta = min(beta, eval);
                    
                    // Alpha-Beta剪枝
                    if (beta <= alpha) {
                        break; // alpha剪枝
                    }
                }
                
                return minEval;
            }
        }
        
    private:
        /**
         * 检查游戏是否结束
         * 
         * @param board 棋盘
         * @return 是否结束
         */
        static bool isGameOver(const vector<vector<int>>& board) {
            // 简化实现，实际游戏中需要根据具体规则判断
            return false;
        }
        
        /**
         * 评估棋盘状态
         * 
         * @param board 棋盘
         * @return 评估分数
         */
        static int evaluateBoard(const vector<vector<int>>& board) {
            // 简化实现，实际评估函数需要根据具体游戏设计
            return 0;
        }
        
        /**
         * 生成所有可能的移动
         * 
         * @param board 棋盘
         * @param isMaximizing 是否最大化玩家
         * @return 移动列表
         */
        static vector<vector<int>> generateMoves(const vector<vector<int>>& board, bool isMaximizing) {
            // 简化实现，实际需要根据具体游戏生成移动
            return vector<vector<int>>();
        }
        
        /**
         * 执行移动
         * 
         * @param board 棋盘
         * @param move 移动
         * @param isMaximizing 是否最大化玩家
         */
        static void makeMove(vector<vector<int>>& board, const vector<int>& move, bool isMaximizing) {
            // 简化实现，实际需要根据具体游戏执行移动
        }
        
        /**
         * 撤销移动
         * 
         * @param board 棋盘
         * @param move 移动
         */
        static void undoMove(vector<vector<int>>& board, const vector<int>& move) {
            // 简化实现，实际需要根据具体游戏撤销移动
        }
    };
};

/**
 * 测试示例
 */
int main() {
    cout << "=== 剪枝技术测试 ===" << endl;
    
    // 测试N皇后问题
    cout << "\n1. N皇后问题剪枝测试:" << endl;
    vector<int> nValues = {4, 8};
    for (int n : nValues) {
        auto startTime = chrono::high_resolution_clock::now();
        int solutions = PruningTechniques::solveNQueens(n);
        auto endTime = chrono::high_resolution_clock::now();
        
        auto duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
        printf("%d皇后问题: %d个解, 时间: %ld μs\n", n, solutions, duration.count());
    }
    
    // 测试0-1背包问题
    cout << "\n2. 0-1背包问题剪枝测试:" << endl;
    vector<PruningTechniques::KnapsackItem> items = {
        PruningTechniques::KnapsackItem(10, 60),
        PruningTechniques::KnapsackItem(20, 100),
        PruningTechniques::KnapsackItem(30, 120),
        PruningTechniques::KnapsackItem(15, 80),
        PruningTechniques::KnapsackItem(25, 90)
    };
    int capacity = 50;
    
    auto startTime = chrono::high_resolution_clock::now();
    int maxValue = PruningTechniques::knapsackWithPruning(items, capacity);
    auto endTime = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    printf("背包容量: %d, 最大价值: %d, 时间: %ld μs\n", capacity, maxValue, duration.count());
    
    // 测试记忆化斐波那契
    cout << "\n3. 记忆化斐波那契剪枝测试:" << endl;
    PruningTechniques::FibonacciWithMemoization fib;
    
    vector<int> fibIndices = {30, 35, 40};
    for (int n : fibIndices) {
        fib.clearMemo(); // 清空缓存
        auto startTime2 = chrono::high_resolution_clock::now();
        long long result = fib.fibonacci(n);
        auto endTime2 = chrono::high_resolution_clock::now();
        
        auto duration2 = chrono::duration_cast<chrono::microseconds>(endTime2 - startTime2);
        printf("F(%d) = %lld, 时间: %ld μs\n", n, result, duration2.count());
    }
    
    // 测试Alpha-Beta剪枝（概念演示）
    cout << "\n4. Alpha-Beta剪枝概念演示:" << endl;
    cout << "Alpha-Beta剪枝在博弈树搜索中能有效减少节点访问数量" << endl;
    cout << "对于深度为d的完全二叉树，不剪枝需要访问O(b^d)个节点" << endl;
    cout << "使用Alpha-Beta剪枝后，最好情况下只需要访问O(b^(d/2))个节点" << endl;
    
    return 0;
}