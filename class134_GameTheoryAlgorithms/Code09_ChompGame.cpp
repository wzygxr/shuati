// Chomp游戏 (Chomp Game)
// Chomp是一个经典的公平组合游戏，通常用巧克力块来描述
// 游戏规则：
// 1. 游戏在一个m×n的矩形巧克力板上进行
// 2. 玩家轮流选择一个巧克力块(x,y)，并吃掉该块及其右下角的所有巧克力块
// 3. 左上角的巧克力块(1,1)是有毒的，吃到它的人输
// 
// 算法思路：
// 1. 数学定理：对于任何大小m×n的巧克力板(m,n>1)，先手都有必胜策略
// 2. 这个定理是非构造性的，它证明了必胜策略的存在，但没有给出具体如何操作
// 3. 实际实现中，我们可以使用动态规划或记忆化搜索来求解具体的必胜态
// 4. 使用位掩码表示棋盘状态，或者使用二维数组表示
// 
// 时间复杂度：O(2^(m*n)) - 最坏情况下需要遍历所有可能的状态
// 空间复杂度：O(2^(m*n)) - 存储所有状态的胜负情况
//
// 适用场景和解题技巧：
// 1. 适用场景：
//    - 组合博弈理论研究
//    - 棋盘覆盖问题
//    - 非构造性证明的示例
// 2. 解题技巧：
//    - 对于小棋盘，可以使用记忆化搜索枚举所有可能的移动
//    - 对于大棋盘，利用对称性或其他性质寻找规律
//    - 利用Sprague-Grundy定理分析游戏状态
// 3. 数学意义：
//    - 说明了存在性证明和构造性证明的区别
//    - 在策梅洛定理的应用实例

#include <iostream>
#include <vector>
#include <unordered_map>
#include <string>
#include <sstream>
#include <functional>

// 为vector<vector<bool>>创建哈希函数以便在unordered_map中使用
struct VectorHash {
    size_t operator()(const std::vector<std::vector<bool>>& v) const {
        size_t seed = 0;
        for (const auto& row : v) {
            size_t row_hash = 0;
            for (bool b : row) {
                row_hash = row_hash * 2 + (b ? 1 : 0);
            }
            // 使用异或操作合并行哈希值
            seed ^= row_hash + 0x9e3779b9 + (seed << 6) + (seed >> 2);
        }
        return seed;
    }
};

// 检查当前状态是否为必败态
// 根据Chomp游戏的定理，任何m×n(m,n>1)的棋盘，先手都有必胜策略
// 只有1×1的棋盘，先手必输
bool isLosingPosition(int m, int n) {
    return m == 1 && n == 1;
}

// 记忆化搜索辅助函数
bool canWinHelper(std::vector<std::vector<bool>>& board, int m, int n, 
                 std::unordered_map<std::vector<std::vector<bool>>, bool, VectorHash>& memo) {
    // 检查是否已经计算过该状态
    if (memo.find(board) != memo.end()) {
        return memo[board];
    }
    
    // 尝试所有可能的移动
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            // 只有巧克力存在的位置才能被选择
            if (board[i][j]) {
                // 创建新的棋盘状态
                std::vector<std::vector<bool>> newBoard = board;
                
                // 吃掉该位置及其右下角的所有巧克力
                for (int x = i; x < m; x++) {
                    for (int y = j; y < n; y++) {
                        newBoard[x][y] = false;
                    }
                }
                
                // 检查左上角是否被吃掉（此时游戏结束，当前玩家获胜）
                if (!newBoard[0][0]) {
                    memo[board] = true;
                    return true;
                }
                
                // 如果对手处于必败态，则当前玩家必胜
                if (!canWinHelper(newBoard, m, n, memo)) {
                    memo[board] = true;
                    return true;
                }
            }
        }
    }
    
    // 所有可能的移动都导致对手获胜，当前玩家必败
    memo[board] = false;
    return false;
}

// 对于小棋盘的具体实现，使用记忆化搜索
bool canWin(int m, int n) {
    // 1×1的棋盘，当前玩家必输
    if (m == 1 && n == 1) {
        return false;
    }
    
    // 创建初始棋盘状态
    std::vector<std::vector<bool>> board(m, std::vector<bool>(n, true));
    
    // 创建记忆化搜索的缓存
    std::unordered_map<std::vector<std::vector<bool>>, bool, VectorHash> memo;
    
    return canWinHelper(board, m, n, memo);
}

// 2×n棋盘的必胜策略
bool canWin2xN(int n) {
    // 根据定理，任何2×n(n>1)的棋盘，先手都有必胜策略
    return n > 1;
}

// 使用位掩码优化的版本（仅适用于小棋盘）
// 对于m×n的棋盘，需要m*n位来表示状态
bool canWinBitmask(int m, int n) {
    // 对于较大的棋盘，位掩码可能会溢出，这里只处理小棋盘
    if (m * n > 30) { // 避免溢出
        std::cerr << "警告：棋盘太大，位掩码方法可能溢出，返回基于定理的结果" << std::endl;
        return !(m == 1 && n == 1);
    }
    
    // 1×1的棋盘，当前玩家必输
    if (m == 1 && n == 1) {
        return false;
    }
    
    // 初始状态：所有位都为1（所有巧克力都在）
    unsigned int initialState = (1U << (m * n)) - 1;
    
    // 创建记忆化搜索的缓存
    std::unordered_map<unsigned int, bool> memo;
    
    // 定义递归函数
    std::function<bool(unsigned int)> dfs = [&](unsigned int state) -> bool {
        if (memo.find(state) != memo.end()) {
            return memo[state];
        }
        
        // 尝试所有可能的移动
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int pos = i * n + j;
                // 只有巧克力存在的位置才能被选择
                if (state & (1U << pos)) {
                    // 创建新的状态，吃掉该位置及其右下角的所有巧克力
                    unsigned int newState = state;
                    for (int x = i; x < m; x++) {
                        for (int y = j; y < n; y++) {
                            int newPos = x * n + y;
                            newState &= ~(1U << newPos); // 清除该位
                        }
                    }
                    
                    // 检查左上角是否被吃掉（此时游戏结束，当前玩家获胜）
                    if (!(newState & 1U)) { // 检查第一个位是否为0
                        memo[state] = true;
                        return true;
                    }
                    
                    // 如果对手处于必败态，则当前玩家必胜
                    if (!dfs(newState)) {
                        memo[state] = true;
                        return true;
                    }
                }
            }
        }
        
        // 所有可能的移动都导致对手获胜，当前玩家必败
        memo[state] = false;
        return false;
    };
    
    return dfs(initialState);
}

// 3×n棋盘的分析（对于小n的情况）
void analyze3xN(int maxN = 10) {
    std::cout << "3×n棋盘的胜负情况分析（基于小n的计算）：" << std::endl;
    for (int n = 1; n <= maxN; n++) {
        try {
            bool result;
            // 对于较大的n，使用位掩码方法可能更高效
            if (3 * n <= 30) {
                result = canWinBitmask(3, n);
            } else {
                result = canWin(3, n);
            }
            std::cout << "3×" << n << "棋盘，先手" << (result ? "有" : "无") << "必胜策略" << std::endl;
        } catch (const std::exception& e) {
            std::cerr << "计算3×" << n << "棋盘时出错：" << e.what() << std::endl;
            std::cout << "3×" << n << "棋盘，计算出错" << std::endl;
        }
    }
}

// 测试方法
int main() {
    std::cout << "Chomp游戏定理测试：" << std::endl;
    std::cout << "1×1棋盘，先手必输: " << (isLosingPosition(1, 1) ? "true" : "false") << std::endl;
    std::cout << "1×2棋盘，先手必输: " << (isLosingPosition(1, 2) ? "true" : "false") << std::endl; // 应该返回false
    std::cout << "2×2棋盘，先手必输: " << (isLosingPosition(2, 2) ? "true" : "false") << std::endl; // 应该返回false
    
    std::cout << "\n小棋盘具体计算结果：" << std::endl;
    std::cout << "2×2棋盘，先手" << (canWin(2, 2) ? "有" : "无") << "必胜策略" << std::endl;
    std::cout << "2×3棋盘，先手" << (canWin(2, 3) ? "有" : "无") << "必胜策略" << std::endl;
    
    std::cout << "\n使用位掩码方法计算：" << std::endl;
    std::cout << "2×2棋盘，先手" << (canWinBitmask(2, 2) ? "有" : "无") << "必胜策略" << std::endl;
    std::cout << "2×3棋盘，先手" << (canWinBitmask(2, 3) ? "有" : "无") << "必胜策略" << std::endl;
    
    std::cout << "\n2×n棋盘分析：" << std::endl;
    for (int n = 1; n <= 5; n++) {
        std::cout << "2×" << n << "棋盘，先手" << (canWin2xN(n) ? "有" : "无") << "必胜策略" << std::endl;
    }
    
    std::cout << "\n3×n棋盘分析（可能需要较长时间）：" << std::endl;
    try {
        analyze3xN(5); // 限制为5以避免计算时间过长
    } catch (const std::exception& e) {
        std::cerr << "分析过程中发生错误：" << e.what() << std::endl;
    }
    
    return 0;
}