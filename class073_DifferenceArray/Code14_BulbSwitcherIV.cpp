#include <iostream>
#include <string>
#include <chrono>

/**
 * LeetCode 1529. 灯泡开关 IV (Bulb Switcher IV)
 * 
 * 题目描述:
 * 房间中有 n 个灯泡，编号从 0 到 n-1，初始时都处于关闭状态。
 * 你的任务是按照灯泡的编号顺序，对每个灯泡进行一次操作：切换该灯泡以及之后所有灯泡的状态（关闭变打开，打开变关闭）。
 * 例如，第 0 号灯泡是第一个被操作的，它会切换所有灯泡的状态。
 * 第 1 号灯泡是第二个被操作的，它会切换 1 号及之后的灯泡的状态。以此类推。
 * 但是，现在我们有一个目标状态 target，表示每个灯泡最终是打开还是关闭。
 * 请你计算至少需要多少次操作才能使灯泡达到目标状态。
 * 
 * 示例1:
 * 输入: target = "10111"
 * 输出: 3
 * 
 * 示例2:
 * 输入: target = "101"
 * 输出: 3
 * 
 * 示例3:
 * 输入: target = "00000"
 * 输出: 0
 * 
 * 提示:
 * 1. 1 <= target.length <= 10^5
 * 2. target[i] 是 '0' 或 '1'
 * 
 * 题目链接: https://leetcode.com/problems/bulb-switcher-iv/
 * 
 * 解题思路:
 * 这个问题可以通过观察灯泡状态的变化规律来解决：
 * 1. 初始时所有灯泡都是关闭的（状态为0）
 * 2. 每一次操作都会切换当前灯泡以及之后所有灯泡的状态
 * 3. 注意到，灯泡状态的切换是相互影响的，并且后面的操作会覆盖前面的部分操作效果
 * 
 * 可以采用贪心的策略来解决：
 * 1. 从左到右遍历目标字符串
 * 2. 每次遇到状态变化（与前一个灯泡状态不同），就增加操作次数
 * 3. 特别地，如果第一个灯泡的目标状态是1，需要进行一次初始操作
 * 
 * 这种方法的直觉是：
 * - 灯泡状态的变化只能由操作导致
 * - 每个灯泡的最终状态取决于它被切换的次数是奇数还是偶数
 * - 最优策略是在状态变化的位置执行操作，这样可以一次性改变后面所有灯泡的状态
 * 
 * 时间复杂度: O(n)，其中 n 是目标字符串的长度
 * 空间复杂度: O(1)，只使用了常数级别的额外空间
 * 
 * 这是最优解，因为我们需要至少遍历一次整个字符串，时间复杂度无法更低。
 */

/**
 * 计算将灯泡从初始状态调整到目标状态所需的最少操作次数
 * 
 * @param target 目标状态字符串，每个字符是 '0' 或 '1'
 * @return 最少操作次数
 */
int minFlips(std::string target) {
    // 参数校验
    if (target.empty()) {
        return 0;
    }
    
    int flips = 0;
    // 当前灯泡的期望状态，初始为0（所有灯泡都是关闭的）
    char current = '0';
    
    // 遍历目标字符串的每个字符
    for (char c : target) {
        // 如果当前字符与期望状态不同，需要进行一次操作
        if (c != current) {
            flips++;
            // 切换期望状态（因为一次操作会改变当前位置及之后所有灯泡的状态）
            current = current == '0' ? '1' : '0';
        }
        // 如果相同，不需要操作，继续检查下一个灯泡
    }
    
    return flips;
}

/**
 * 另一种实现方式，直接根据目标字符串中相邻字符的变化次数来计算
 * 并且考虑第一个字符是否为1
 * 
 * @param target 目标状态字符串
 * @return 最少操作次数
 */
int minFlipsAlternative(std::string target) {
    // 参数校验
    if (target.empty()) {
        return 0;
    }
    
    int flips = 0;
    
    // 如果第一个字符是1，需要一次初始操作
    if (target[0] == '1') {
        flips = 1;
    }
    
    // 遍历字符串，统计相邻字符变化的次数
    for (size_t i = 1; i < target.length(); i++) {
        // 如果当前字符与前一个字符不同，说明需要一次操作
        if (target[i] != target[i - 1]) {
            flips++;
        }
    }
    
    return flips;
}

// 主函数，用于测试
int main() {
    // 测试用例1
    std::string target1 = "10111";
    std::cout << "测试用例1 - minFlips(\"10111\"): " << minFlips(target1) << std::endl; // 预期输出: 3
    std::cout << "测试用例1 - minFlipsAlternative(\"10111\"): " << minFlipsAlternative(target1) << std::endl; // 预期输出: 3
    
    // 测试用例2
    std::string target2 = "101";
    std::cout << "测试用例2 - minFlips(\"101\"): " << minFlips(target2) << std::endl; // 预期输出: 3
    std::cout << "测试用例2 - minFlipsAlternative(\"101\"): " << minFlipsAlternative(target2) << std::endl; // 预期输出: 3
    
    // 测试用例3
    std::string target3 = "00000";
    std::cout << "测试用例3 - minFlips(\"00000\"): " << minFlips(target3) << std::endl; // 预期输出: 0
    std::cout << "测试用例3 - minFlipsAlternative(\"00000\"): " << minFlipsAlternative(target3) << std::endl; // 预期输出: 0
    
    // 测试用例4 - 全为1的情况
    std::string target4 = "11111";
    std::cout << "测试用例4 - minFlips(\"11111\"): " << minFlips(target4) << std::endl; // 预期输出: 1
    std::cout << "测试用例4 - minFlipsAlternative(\"11111\"): " << minFlipsAlternative(target4) << std::endl; // 预期输出: 1
    
    // 测试用例5 - 交替的情况
    std::string target5 = "1010101010";
    std::cout << "测试用例5 - minFlips(\"1010101010\"): " << minFlips(target5) << std::endl; // 预期输出: 10
    std::cout << "测试用例5 - minFlipsAlternative(\"1010101010\"): " << minFlipsAlternative(target5) << std::endl; // 预期输出: 10
    
    // 测试用例6 - 边界情况：单个字符
    std::string target6 = "1";
    std::cout << "测试用例6 - minFlips(\"1\"): " << minFlips(target6) << std::endl; // 预期输出: 1
    std::cout << "测试用例6 - minFlipsAlternative(\"1\"): " << minFlipsAlternative(target6) << std::endl; // 预期输出: 1
    
    std::string target7 = "0";
    std::cout << "测试用例7 - minFlips(\"0\"): " << minFlips(target7) << std::endl; // 预期输出: 0
    std::cout << "测试用例7 - minFlipsAlternative(\"0\"): " << minFlipsAlternative(target7) << std::endl; // 预期输出: 0
    
    // 性能测试
    std::cout << "\n性能测试:" << std::endl;
    // 生成一个大的目标字符串
    std::string largeTarget;
    for (int i = 0; i < 100000; i++) {
        largeTarget += (i % 2) ? '1' : '0';
    }
    
    auto startTime = std::chrono::high_resolution_clock::now();
    int result1 = minFlips(largeTarget);
    auto endTime = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
    std::cout << "大目标字符串 - minFlips 结果: " << result1 << std::endl;
    std::cout << "大目标字符串 - minFlips 耗时: " << duration.count() << "ms" << std::endl;
    
    startTime = std::chrono::high_resolution_clock::now();
    int result2 = minFlipsAlternative(largeTarget);
    endTime = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
    std::cout << "大目标字符串 - minFlipsAlternative 结果: " << result2 << std::endl;
    std::cout << "大目标字符串 - minFlipsAlternative 耗时: " << duration.count() << "ms" << std::endl;
    
    return 0;
}