#include <stdio.h>
#include <stdlib.h>
#include <limits.h>

#ifndef NULL
#define NULL 0
#endif

// 替代fmax函数
// 算法思路：比较两个int值，返回较大的一个
// 时间复杂度：O(1)
// 空间复杂度：O(1)
int my_fmax(int a, int b) {
    return (a > b) ? a : b;
}

// 买卖股票的最佳时机 II
// 给定一个数组，它的第 i 个元素是一支给定股票第 i 天的价格。
// 设计一个算法来计算你所能获取的最大利润。
// 你可以尽可能地完成更多的交易（多次买卖一支股票）。
// 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
// 
// 算法标签: 贪心算法(Greedy Algorithm)、数组遍历(Array Traversal)
// 时间复杂度: O(n)，其中n是价格数组长度
// 空间复杂度: O(1)，仅使用常数额外空间
// 测试链接 : https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/
// 相关题目: LeetCode 121. 买卖股票的最佳时机、LeetCode 123. 买卖股票的最佳时机 III
// 贪心算法专题 - 补充题目收集与详解

/*
 * 算法思路详解：
 * 1. 贪心策略：只要明天价格比今天高，就在今天买入明天卖出
 *    - 这个策略的核心思想是抓住每一个上涨的机会
 *    - 将所有正的价格差累加就是最大利润
 *    - 等价于在所有局部最低点买入，在所有局部最高点卖出
 *
 * 2. 遍历价格数组，计算相邻两天的价格差
 *    - 通过一次遍历完成所有计算
 *    - 只需要比较相邻两天的价格
 *
 * 3. 如果价格差为正，则累加到总利润中
 *    - 正的价格差表示可以获利
 *    - 累加所有正的价格差得到最大利润
 *
 * 时间复杂度分析：
 * - 遍历时间复杂度：O(n)，其中n是价格数组长度
 * - 总体时间复杂度：O(n)
 *
 * 空间复杂度分析：
 * - 只使用了常数额外空间存储变量
 * - 空间复杂度：O(1)
 *
 * 是否最优解：
 * - 是，这是处理此类问题的最优解法
 * - 贪心策略保证了局部最优解能导致全局最优解
 *
 * 工程化最佳实践：
 * 1. 异常处理：检查输入是否为空或长度不足
 * 2. 边界条件：处理空数组、单个元素等特殊情况
 * 3. 性能优化：一次遍历完成计算，避免重复操作
 * 4. 可读性：清晰的变量命名和详细注释，便于维护
 *
 * 极端场景与边界情况处理：
 * 1. 空输入：prices为空数组或长度为0
 * 2. 极端值：只有一个价格、所有价格相同
 * 3. 重复数据：多个价格相同的情况
 * 4. 有序/逆序数据：价格持续上涨或下跌的情况
 *
 * 跨语言实现差异与优化：
 * 1. Java：使用增强for循环遍历数组，代码更简洁
 * 2. C++：使用传统for循环或范围for循环，性能更优
 * 3. Python：使用for循环或列表推导式，语法更灵活
 *
 * 调试与测试策略：
 * 1. 打印中间过程：在循环中打印每天的价格和利润变化
 * 2. 用断言验证中间结果：确保利润不为负
 * 3. 性能退化排查：检查是否只遍历了一次数组
 * 4. 边界测试：测试空数组、单元素等边界情况
 *
 * 实际应用场景与拓展：
 * 1. 金融数据分析：在简单的交易策略中应用贪心算法
 * 2. 时间序列预测：作为基线策略进行对比分析
 * 3. 强化学习：作为初始策略提供给智能体
 *
 * 算法深入解析：
 * 贪心算法在股票交易问题中的应用体现了其核心思想：
 * 1. 局部最优选择：每次选择当前能获得的最大利润
 * 2. 无后效性：当前的选择不会影响之前的状态
 * 3. 最优子结构：问题的最优解包含子问题的最优解
 * 这个问题的关键洞察是，多次交易的最大利润等于所有正的价格差之和。
 */


// 买卖股票的最佳时机 II 主函数
// 算法思路：贪心算法
// 1. 遍历价格数组，计算相邻两天的价格差
// 2. 如果价格差为正，则累加到总利润中
// 时间复杂度：O(n)，n是价格数组长度
// 空间复杂度：O(1)
int maxProfit(int prices[], int pricesSize) {
    // 异常处理：检查输入是否为空或长度不足
    if (prices == NULL || pricesSize <= 1) {
        return 0;
    }
    
    int maxProfit = 0;  // 存储最大利润
    
    // 遍历价格数组，计算相邻两天的价格差
    // 时间复杂度：O(n)
    for (int i = 1; i < pricesSize; i++) {
        // 如果明天价格比今天高，则累加利润
        // 这体现了贪心策略：抓住每一个上涨的机会
        if (prices[i] > prices[i - 1]) {
            maxProfit += prices[i] - prices[i - 1];
        }
    }
    
    return maxProfit;  // 返回最大利润
}

// 补充题目1: LeetCode 55. 跳跃游戏
// 题目描述: 给定一个非负整数数组 nums ，你最初位于数组的 第一个下标 。
// 数组中的每个元素代表你在该位置可以跳跃的最大长度。
// 判断你是否能够到达最后一个下标。
// 链接: https://leetcode.cn/problems/jump-game/

// 跳跃游戏
// 算法思路：贪心算法
// 1. 维护能到达的最远位置
// 2. 遍历数组，更新最远位置
// 3. 如果当前位置超过了最远位置，无法继续前进
// 时间复杂度：O(n)，n是数组长度
// 空间复杂度：O(1)
bool canJump(int* nums, int numsSize) {
    // 异常处理：检查输入是否为空
    if (nums == NULL || numsSize == 0) {
        return true; // 空数组视为可以到达
    }
    
    int maxReach = 0; // 当前能到达的最远位置
    
    // 贪心策略：维护能到达的最远位置
    // 时间复杂度：O(n)
    for (int i = 0; i < numsSize; i++) {
        // 如果当前位置超过了能到达的最远位置，无法继续前进
        if (i > maxReach) {
            return false;
        }
        // 更新能到达的最远位置
        maxReach = my_fmax(maxReach, i + nums[i]);
        // 如果已经能到达或超过最后一个位置，直接返回true
        if (maxReach >= numsSize - 1) {
            return true;
        }
    }
    
    return maxReach >= numsSize - 1;
}

// 补充题目2: LeetCode 45. 跳跃游戏 II
// 题目描述: 给定一个非负整数数组，你最初位于数组的第一个位置。
// 数组中的每个元素代表你在该位置可以跳跃的最大长度。
// 你的目标是使用最少的跳跃次数到达数组的最后一个位置。
// 假设你总是可以到达数组的最后一个位置。
// 链接: https://leetcode.cn/problems/jump-game-ii/

// 跳跃游戏 II
// 算法思路：贪心算法
// 1. 每次在可到达范围内选择能跳得最远的位置
// 2. 维护当前跳跃能到达的边界和最远位置
// 时间复杂度：O(n)，n是数组长度
// 空间复杂度：O(1)
int jump(int* nums, int numsSize) {
    // 异常处理：检查输入是否为空或长度不足
    if (nums == NULL || numsSize <= 1) {
        return 0; // 空数组或只有一个元素不需要跳跃
    }
    
    int jumps = 0;        // 跳跃次数
    int currentEnd = 0;   // 当前跳跃能到达的边界
    int farthest = 0;     // 在进行下次跳跃前能到达的最远位置
    
    // 贪心策略：每次在可到达范围内选择能跳得最远的位置
    // 时间复杂度：O(n)
    for (int i = 0; i < numsSize - 1; i++) {
        // 更新能到达的最远位置
        farthest = my_fmax(farthest, i + nums[i]);
        
        // 到达当前跳跃的边界，需要进行一次跳跃
        if (i == currentEnd) {
            jumps++;
            currentEnd = farthest;
            
            // 如果已经能到达最后位置，可以提前结束
            if (currentEnd >= numsSize - 1) {
                break;
            }
        }
    }
    
    return jumps;
}

// 补充题目3: LeetCode 605. 种花问题
// 题目描述: 假设有一个很长的花坛，一部分地块种植了花，另一部分却没有。
// 可是，花不能种植在相邻的地块上，它们会争夺水源，两者都会死去。
// 给你一个整数数组 flowerbed 表示花坛，由若干 0 和 1 组成，其中 0 表示没种植花，1 表示种植了花。
// 另有一个数 n ，能否在不打破种植规则的情况下种入 n 朵花？
// 能则返回 true ，不能则返回 false 。
// 链接: https://leetcode.cn/problems/can-place-flowers/

// 种花问题
// 算法思路：贪心算法
// 1. 遍历花坛，尽可能多地种花
// 2. 检查当前位置是否可以种花
// 时间复杂度：O(n)，n是花坛长度
// 空间复杂度：O(1)
bool canPlaceFlowers(int* flowerbed, int flowerbedSize, int n) {
    // 异常处理：检查输入是否为空
    if (flowerbed == NULL || flowerbedSize == 0) {
        return n == 0;
    }
    
    int count = 0; // 可以种的花的数量
    int len = flowerbedSize;
    
    // 贪心策略：遍历花坛，尽可能多地种花
    // 时间复杂度：O(n)
    for (int i = 0; i < len; i++) {
        // 检查当前位置是否可以种花：当前位置为0，且前后都不是1
        bool canPlant = flowerbed[i] == 0;
        if (i > 0) {
            canPlant = canPlant && (flowerbed[i - 1] == 0);
        }
        if (i < len - 1) {
            canPlant = canPlant && (flowerbed[i + 1] == 0);
        }
        
        if (canPlant) {
            flowerbed[i] = 1; // 在当前位置种花
            count++;
            
            // 如果已经能满足n朵花，提前返回
            if (count >= n) {
                return true;
            }
        }
    }
    
    return count >= n;
}

// 补充题目4: LeetCode 435. 无重叠区间
// 题目描述: 给定一个区间的集合 intervals ，其中 intervals[i] = [starti, endi] 。
// 返回需要移除区间的最小数量，使剩余区间互不重叠。
// 链接: https://leetcode.cn/problems/non-overlapping-intervals/

// 比较函数，用于排序二维数组（按第二个元素升序）
// 算法思路：比较两个一维数组的第二个元素
// 时间复杂度：O(1)
// 空间复杂度：O(1)
bool compareIntervals(const int* a, const int* b) {
    return a[1] < b[1];
}

// 替代sort函数，使用冒泡排序对二维数组按第二个元素升序排序
// 算法思路：冒泡排序，比较相邻元素的第二个值，如果前面的大则交换
// 时间复杂度：O(n^2)，n为数组长度
// 空间复杂度：O(1)
void my_sort_intervals(int** intervals, int intervalsSize) {
    for (int i = 0; i < intervalsSize - 1; i++) {
        for (int j = 0; j < intervalsSize - i - 1; j++) {
            if (intervals[j][1] > intervals[j + 1][1]) {
                // 交换指针
                int* temp = intervals[j];
                intervals[j] = intervals[j + 1];
                intervals[j + 1] = temp;
            }
        }
    }
}

// 无重叠区间
// 算法思路：贪心算法
// 1. 按区间结束位置排序
// 2. 优先保留结束早的区间
// 时间复杂度：O(n*log(n))，主要是排序的时间复杂度
// 空间复杂度：O(1)
int eraseOverlapIntervals(int** intervals, int intervalsSize, int* intervalsColSize) {
    // 异常处理：检查输入是否为空
    if (intervals == NULL || intervalsSize <= 1) {
        return 0;
    }
    
    // 按区间结束位置排序
    my_sort_intervals(intervals, intervalsSize);
    
    int count = 0;       // 保留的区间数量
    int end = INT_MIN;   // 上一个保留的区间的结束位置
    
    // 贪心策略：优先保留结束早的区间
    // 时间复杂度：O(n)
    for (int i = 0; i < intervalsSize; i++) {
        // 如果当前区间的开始位置大于等于上一个保留区间的结束位置，则不重叠
        if (intervals[i][0] >= end) {
            count++;
            end = intervals[i][1];
        }
        // 否则，该区间与上一个保留区间重叠，需要移除
    }
    
    // 需要移除的区间数量 = 总区间数量 - 保留的区间数量
    return intervalsSize - count;
}

// 补充题目5: LeetCode 121. 买卖股票的最佳时机
// 题目描述: 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
// 你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票。
// 设计一个算法来计算你所能获取的最大利润。
// 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。
// 链接: https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/

// 买卖股票的最佳时机
// 算法思路：贪心算法
// 1. 记录到目前为止的最低价格
// 2. 计算当前价格卖出能获得的最大利润
// 时间复杂度：O(n)，n是价格数组长度
// 空间复杂度：O(1)
int maxProfitSingle(int* prices, int pricesSize) {
    // 异常处理：检查输入是否为空或长度不足
    if (prices == NULL || pricesSize <= 1) {
        return 0;
    }
    
    int minPrice = INT_MAX; // 记录到目前为止的最低价格
    int maxProfit = 0;      // 记录最大利润
    
    // 贪心策略：每次记录到目前为止的最低价格，计算当前价格卖出能获得的最大利润
    // 时间复杂度：O(n)
    for (int i = 0; i < pricesSize; i++) {
        // 更新最低价格
        if (prices[i] < minPrice) {
            minPrice = prices[i];
        }
        // 计算当前价格卖出能获得的利润，并更新最大利润
        else if (prices[i] - minPrice > maxProfit) {
            maxProfit = prices[i] - minPrice;
        }
    }
    
    return maxProfit;
}