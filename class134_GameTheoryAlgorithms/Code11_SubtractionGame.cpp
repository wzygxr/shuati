// 减法游戏 (Subtraction Game)
// 减法游戏是取石子游戏的一个通用变种，也称为Take-away Game
// 游戏规则：
// 1. 有一堆石子，数量为n
// 2. 玩家轮流从堆中取石子，每次可以取的石子数必须属于一个给定的集合S
// 3. 无法取石子的玩家输
// 
// 算法思路：
// 1. 使用动态规划计算每个石子数量对应的必胜态（winning position）和必败态（losing position）
// 2. dp[i] = true 表示当石子数为i时，当前玩家处于必胜态
// 3. 状态转移方程：dp[i] = 存在某个s∈S，使得 i >= s 且 dp[i-s] = false
// 4. 边界条件：dp[0] = false（没有石子时，当前玩家无法操作，必败）
// 
// 时间复杂度：O(n*k)，其中n是石子数量上限，k是集合S的大小
// 空间复杂度：O(n)，用于存储dp数组
//
// 适用场景和解题技巧：
// 1. 适用场景：
//    - 具有特定移动规则的取石子游戏
//    - 需要预处理所有可能状态的博弈问题
//    - 可以作为其他复杂博弈问题的子问题
// 2. 解题技巧：
//    - 识别问题是否符合减法游戏模型
//    - 确定允许的移动集合S
//    - 通过动态规划预处理所有可能的状态
// 3. 变种和扩展：
//    - 标准巴什博弈是减法游戏的特例，其中S = {1, 2, ..., m}
//    - 可以扩展到多堆石子的情况，结合SG函数进行分析

#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
#include <stdexcept>

/**
 * 计算减法游戏中每个石子数量对应的胜负状态
 * @param maxN 最大石子数量
 * @param moves 允许的移动集合，表示每次可以取的石子数
 * @return 一个布尔向量，dp[i]表示石子数为i时是否为必胜态
 * @throws std::invalid_argument 当参数无效时抛出
 */
std::vector<bool> calculateWinningPositions(int maxN, const std::vector<int>& moves) {
    // 参数校验
    if (maxN < 0) {
        throw std::invalid_argument("最大石子数量不能为负数");
    }
    if (moves.empty()) {
        throw std::invalid_argument("移动集合不能为空");
    }
    
    // 确保移动集合中的元素都是正整数且不重复
    std::set<int> moveSet;
    for (int move : moves) {
        if (move <= 0) {
            throw std::invalid_argument("移动集合中的元素必须为正整数");
        }
        moveSet.insert(move);
    }
    
    // 转换为向量以便排序（虽然set已经是排序的，但为了保持一致的接口）
    std::vector<int> sortedMoves(moveSet.begin(), moveSet.end());
    
    // 初始化dp数组
    std::vector<bool> dp(maxN + 1, false);
    dp[0] = false; // 边界条件：0个石子时必败
    
    // 动态规划计算每个状态
    for (int i = 1; i <= maxN; i++) {
        bool canWin = false;
        // 尝试所有可能的移动
        for (int move : sortedMoves) {
            if (move > i) {
                // 当前移动需要的石子数超过了现有石子数，无法进行
                break; // 由于已排序，可以提前退出
            }
            // 如果存在某个移动，使得对手处于必败态，则当前状态为必胜态
            if (!dp[i - move]) {
                canWin = true;
                break; // 找到一个必胜策略即可退出
            }
        }
        dp[i] = canWin;
    }
    
    return dp;
}

/**
 * 判断在给定石子数和移动集合的情况下，当前玩家是否有必胜策略
 * @param n 当前石子数量
 * @param moves 允许的移动集合
 * @return 如果当前玩家有必胜策略，返回true；否则返回false
 * @throws std::invalid_argument 当参数无效时抛出
 */
bool canWin(int n, const std::vector<int>& moves) {
    // 参数校验
    if (n < 0) {
        throw std::invalid_argument("石子数量不能为负数");
    }
    
    // 计算胜负状态
    std::vector<bool> dp = calculateWinningPositions(n, moves);
    return dp[n];
}

/**
 * 寻找当前状态下的必胜策略
 * @param n 当前石子数量
 * @param moves 允许的移动集合
 * @return 如果存在必胜策略，返回一个可以取的石子数；否则返回-1
 * @throws std::invalid_argument 当参数无效时抛出
 */
int findWinningMove(int n, const std::vector<int>& moves) {
    // 参数校验
    if (n < 0) {
        throw std::invalid_argument("石子数量不能为负数");
    }
    if (moves.empty()) {
        throw std::invalid_argument("移动集合不能为空");
    }
    
    // 确保移动集合中的元素都是正整数且不重复
    std::set<int> moveSet;
    for (int move : moves) {
        if (move > 0) {
            moveSet.insert(move);
        }
    }
    
    // 尝试所有可能的移动
    for (int move : moveSet) {
        if (move <= n) {
            // 检查取走move个石子后，对手是否处于必败态
            std::vector<bool> dp = calculateWinningPositions(n - move, moves);
            if (!dp[n - move]) {
                return move; // 找到一个必胜策略
            }
        }
    }
    
    return -1; // 不存在必胜策略
}

/**
 * 计算SG函数值
 * @param maxN 最大石子数量
 * @param moves 允许的移动集合
 * @return 一个整数向量，sg[i]表示石子数为i时的SG函数值
 * @throws std::invalid_argument 当参数无效时抛出
 */
std::vector<int> calculateSG(int maxN, const std::vector<int>& moves) {
    // 参数校验
    if (maxN < 0) {
        throw std::invalid_argument("最大石子数量不能为负数");
    }
    if (moves.empty()) {
        throw std::invalid_argument("移动集合不能为空");
    }
    
    // 确保移动集合中的元素都是正整数且不重复
    std::set<int> moveSet;
    for (int move : moves) {
        if (move > 0) {
            moveSet.insert(move);
        }
    }
    
    // 转换为向量以便排序
    std::vector<int> sortedMoves(moveSet.begin(), moveSet.end());
    
    // 初始化SG数组
    std::vector<int> sg(maxN + 1, 0);
    sg[0] = 0; // 边界条件：0个石子时SG值为0
    
    // 计算每个状态的SG值
    for (int i = 1; i <= maxN; i++) {
        std::set<int> reachableSG;
        // 收集所有可达状态的SG值
        for (int move : sortedMoves) {
            if (move <= i) {
                reachableSG.insert(sg[i - move]);
            }
        }
        // 找到最小的未出现的非负整数
        int mex = 0; // mex表示最小非负整数
        while (reachableSG.find(mex) != reachableSG.end()) {
            mex++;
        }
        sg[i] = mex;
    }
    
    return sg;
}

/**
 * 打印胜负状态表
 * @param dp 胜负状态向量
 */
void printWinningTable(const std::vector<bool>& dp) {
    std::cout << "石子数\t状态" << std::endl;
    std::cout << "----\t----" << std::endl;
    for (size_t i = 0; i < dp.size(); i++) {
        std::cout << i << "\t" << (dp[i] ? "必胜态" : "必败态") << std::endl;
    }
}

/**
 * 打印SG函数值表
 * @param sg SG函数值向量
 */
void printSGTable(const std::vector<int>& sg) {
    std::cout << "石子数\tSG值" << std::endl;
    std::cout << "----\t----" << std::endl;
    for (size_t i = 0; i < sg.size(); i++) {
        std::cout << i << "\t" << sg[i] << std::endl;
    }
}

/**
 * 测试减法游戏
 */
int main() {
    try {
        // 测试用例1：标准巴什博弈，每次可以取1-3个石子
        std::cout << "测试用例1：标准巴什博弈（每次取1-3个石子）" << std::endl;
        std::vector<int> moves1 = {1, 2, 3};
        int maxN1 = 10;
        std::vector<bool> dp1 = calculateWinningPositions(maxN1, moves1);
        printWinningTable(dp1);
        
        // 测试特定石子数的胜负状态
        int n1 = 4;
        std::cout << "\n石子数为" << n1 << "时，" 
                  << (canWin(n1, moves1) ? "先手必胜" : "先手必败") << std::endl;
        int winningMove1 = findWinningMove(n1, moves1);
        if (winningMove1 != -1) {
            std::cout << "必胜策略：取" << winningMove1 << "个石子" << std::endl;
        } else {
            std::cout << "无必胜策略" << std::endl;
        }
        
        // 计算SG函数值
        std::vector<int> sg1 = calculateSG(maxN1, moves1);
        std::cout << "\nSG函数值表：" << std::endl;
        printSGTable(sg1);
        
        // 测试用例2：只能取奇数个石子
        std::cout << "\n\n测试用例2：只能取1、3、5个石子" << std::endl;
        std::vector<int> moves2 = {1, 3, 5};
        int maxN2 = 10;
        std::vector<bool> dp2 = calculateWinningPositions(maxN2, moves2);
        printWinningTable(dp2);
        
        // 测试用例3：只能取2的幂次方个石子
        std::cout << "\n\n测试用例3：只能取1、2、4、8个石子（2的幂次方）" << std::endl;
        std::vector<int> moves3 = {1, 2, 4, 8};
        int maxN3 = 10;
        std::vector<bool> dp3 = calculateWinningPositions(maxN3, moves3);
        printWinningTable(dp3);
        
        // 测试用例4：异常处理测试
        std::cout << "\n\n测试用例4：异常处理" << std::endl;
        try {
            calculateWinningPositions(-1, moves1);
        } catch (const std::invalid_argument& e) {
            std::cout << "预期的异常：" << e.what() << std::endl;
        }
        
        try {
            calculateWinningPositions(5, std::vector<int>());
        } catch (const std::invalid_argument& e) {
            std::cout << "预期的异常：" << e.what() << std::endl;
        }
        
        try {
            std::vector<int> invalidMoves = {0, 1};
            calculateWinningPositions(5, invalidMoves);
        } catch (const std::invalid_argument& e) {
            std::cout << "预期的异常：" << e.what() << std::endl;
        }
        
    } catch (const std::exception& e) {
        std::cerr << "错误：" << e.what() << std::endl;
        return 1;
    }
    
    return 0;
}