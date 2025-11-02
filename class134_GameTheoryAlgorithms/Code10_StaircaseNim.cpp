// 阶梯博弈 (Staircase Nim)
// 阶梯博弈是Nim游戏的一个重要变种，有着不同的游戏规则和胜负判定
// 游戏规则：
// 1. 游戏在一个由n个阶梯组成的楼梯上进行
// 2. 每个阶梯上有一定数量的石子
// 3. 玩家轮流进行操作，可以选择一个阶梯i上的若干个石子（至少1个）
// 4. 将选中的石子移动到阶梯i-1上（如果i=1，则石子被移出游戏）
// 5. 无法进行操作的玩家输
// 
// 算法思路：
// 1. 阶梯博弈可以转换为Nim游戏：只需要考虑奇数位置的石子数量
// 2. 胜负判定规则：将所有奇数位置的石子数进行异或操作，如果结果不为0，则先手必胜；否则先手必败
// 3. 这个结论的正确性基于游戏的对称性和必胜策略的构造
// 
// 时间复杂度：O(n) - 只需要遍历一次阶梯，计算奇数位置石子数的异或和
// 空间复杂度：O(1) - 只需要常数额外空间
//
// 适用场景和解题技巧：
// 1. 适用场景：
//    - 资源迁移类游戏
//    - 具有层次结构的博弈问题
//    - 需要将复杂博弈转换为Nim游戏的情况
// 2. 解题技巧：
//    - 识别问题是否符合阶梯博弈模型
//    - 确定哪些位置是关键位置（通常是奇数位置）
//    - 应用Nim游戏的胜负判定规则
// 3. 数学意义：
//    - 展示了博弈论中的简化思想
//    - 利用对称性和不变量解决复杂问题

#include <iostream>
#include <vector>
#include <utility>
#include <stdexcept>

/**
 * 判断阶梯博弈的先手是否有必胜策略
 * @param stairs 表示每个阶梯上的石子数量的向量，stairs[i]表示第i+1个阶梯上的石子数
 * @return 如果先手有必胜策略，返回true；否则返回false
 */
bool canWinStaircaseNim(const std::vector<int>& stairs) {
    // 参数校验
    if (stairs.empty()) {
        // 没有阶梯，先手无法操作，必输
        return false;
    }
    
    // 计算所有奇数位置的石子数的异或和
    int xorSum = 0;
    for (size_t i = 0; i < stairs.size(); i++) {
        // 注意：这里的索引i对应阶梯i+1（因为向量从0开始）
        // 所以当i+1为奇数时（即i为偶数时），需要计算异或和
        if (i % 2 == 0) {
            xorSum ^= stairs[i];
        }
    }
    
    // 如果异或和不为0，先手必胜；否则先手必败
    return xorSum != 0;
}

/**
 * 寻找阶梯博弈中的必胜策略
 * @param stairs 当前每个阶梯上的石子数量
 * @return 如果存在必胜策略，返回一个表示操作的pair，其中第一个元素是源阶梯索引，第二个元素是移动的石子数；
 *         如果不存在必胜策略，返回空的optional
 */
std::pair<int, int> findWinningMove(const std::vector<int>& stairs) {
    // 参数校验
    if (stairs.empty()) {
        return {-1, -1}; // 表示没有有效策略
    }
    
    int xorSum = 0;
    for (size_t i = 0; i < stairs.size(); i++) {
        if (i % 2 == 0) {
            xorSum ^= stairs[i];
        }
    }
    
    // 如果异或和为0，没有必胜策略
    if (xorSum == 0) {
        return {-1, -1};
    }
    
    // 寻找可以进行的必胜操作
    for (size_t i = 0; i < stairs.size(); i++) {
        // 只考虑奇数位置
        if (i % 2 == 0) {
            // 计算需要将当前阶梯的石子数变为多少才能使异或和为0
            int target = stairs[i] ^ xorSum;
            
            // 如果target小于当前石子数，说明可以通过移动石子来达到目标
            if (target < stairs[i]) {
                int stonesToMove = stairs[i] - target;
                return {static_cast<int>(i), stonesToMove};
            }
        }
    }
    
    // 理论上不应该到达这里，因为如果xorSum不为0，必定存在必胜策略
    return {-1, -1};
}

/**
 * 模拟执行移动操作
 * @param stairs 当前阶梯状态
 * @param fromStair 源阶梯索引（0-based）
 * @param stonesToMove 移动的石子数量
 * @return 执行移动后的新阶梯状态
 * @throws std::invalid_argument 当移动操作无效时抛出
 */
std::vector<int> makeMove(const std::vector<int>& stairs, int fromStair, int stonesToMove) {
    if (fromStair < 0 || static_cast<size_t>(fromStair) >= stairs.size() || 
        stonesToMove <= 0 || stonesToMove > stairs[fromStair]) {
        throw std::invalid_argument("无效的移动操作");
    }
    
    // 创建新的状态数组
    std::vector<int> newStairs = stairs;
    
    // 从源阶梯移除石子
    newStairs[fromStair] -= stonesToMove;
    
    // 如果不是最底部的阶梯，将石子移动到下一个阶梯
    if (fromStair > 0) {
        newStairs[fromStair - 1] += stonesToMove;
    }
    
    return newStairs;
}

/**
 * 打印阶梯状态
 * @param stairs 阶梯状态向量
 */
void printStairs(const std::vector<int>& stairs) {
    std::cout << "当前阶梯状态：" << std::endl;
    // 从顶部到底部打印阶梯
    for (int i = static_cast<int>(stairs.size()) - 1; i >= 0; i--) {
        std::cout << "阶梯 " << (i + 1) << ": " << stairs[i] << " 个石子" << std::endl;
    }
    std::cout << std::endl;
}

/**
 * 测试阶梯博弈
 */
int main() {
    // 测试用例1：先手必胜的情况
    // 阶梯1有3个石子，阶梯2有1个石子，阶梯3有4个石子
    // 奇数位置（阶梯1和阶梯3）的异或和：3 ^ 4 = 7 != 0，所以先手必胜
    std::vector<int> stairs1 = {3, 1, 4};
    std::cout << "测试用例1：" << std::endl;
    printStairs(stairs1);
    std::cout << "先手" << (canWinStaircaseNim(stairs1) ? "有" : "无") << "必胜策略" << std::endl;
    
    std::pair<int, int> winningMove1 = findWinningMove(stairs1);
    if (winningMove1.first != -1) {
        std::cout << "必胜策略：从阶梯 " << (winningMove1.first + 1) << " 移动 " << 
                 winningMove1.second << " 个石子到阶梯 " << winningMove1.first << std::endl;
        try {
            std::vector<int> newStairs1 = makeMove(stairs1, winningMove1.first, winningMove1.second);
            std::cout << "移动后的状态：" << std::endl;
            printStairs(newStairs1);
            std::cout << "此时后手" << (canWinStaircaseNim(newStairs1) ? "有" : "无") << "必胜策略" << std::endl;
        } catch (const std::exception& e) {
            std::cerr << "移动操作失败：" << e.what() << std::endl;
        }
    }
    
    // 测试用例2：先手必败的情况
    // 阶梯1有1个石子，阶梯2有2个石子，阶梯3有1个石子
    // 奇数位置（阶梯1和阶梯3）的异或和：1 ^ 1 = 0，所以先手必败
    std::vector<int> stairs2 = {1, 2, 1};
    std::cout << "\n测试用例2：" << std::endl;
    printStairs(stairs2);
    std::cout << "先手" << (canWinStaircaseNim(stairs2) ? "有" : "无") << "必胜策略" << std::endl;
    
    // 测试用例3：空阶梯
    std::vector<int> stairs3 = {};
    std::cout << "\n测试用例3：" << std::endl;
    printStairs(stairs3);
    std::cout << "先手" << (canWinStaircaseNim(stairs3) ? "有" : "无") << "必胜策略" << std::endl;
    
    // 测试用例4：只有一个阶梯
    std::vector<int> stairs4 = {5};
    std::cout << "\n测试用例4：" << std::endl;
    printStairs(stairs4);
    std::cout << "先手" << (canWinStaircaseNim(stairs4) ? "有" : "无") << "必胜策略" << std::endl;
    
    // 测试用例5：包含零的阶梯
    std::vector<int> stairs5 = {0, 3, 0, 4};
    std::cout << "\n测试用例5：" << std::endl;
    printStairs(stairs5);
    std::cout << "先手" << (canWinStaircaseNim(stairs5) ? "有" : "无") << "必胜策略" << std::endl;
    
    // 测试异常处理
    std::cout << "\n异常处理测试：" << std::endl;
    try {
        std::vector<int> invalidMove = makeMove(stairs1, 0, 4);  // 移动的石子数超过了阶梯上的石子数
        std::cout << "测试失败：应该抛出异常但没有" << std::endl;
    } catch (const std::exception& e) {
        std::cout << "测试成功：成功捕获异常 - " << e.what() << std::endl;
    }
    
    return 0;
}