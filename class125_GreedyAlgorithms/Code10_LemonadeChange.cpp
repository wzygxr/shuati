// 柠檬水找零
// 在柠檬水摊上，每一杯柠檬水的售价为 5 美元。顾客排队购买你的产品，（按账单 bills 支付的顺序）一次购买一杯。
// 每位顾客只买一杯柠檬水，然后向你付 5 美元、10 美元或 20 美元。
// 你必须给每个顾客正确找零，也就是说净交易是每位顾客向你支付 5 美元。
// 注意，一开始你手头没有任何零钱。
// 给你一个整数数组 bills ，其中 bills[i] 是第 i 位顾客付的账。
// 如果你能给每位顾客正确找零，返回 true，否则返回 false。
// 测试链接 : https://leetcode.cn/problems/lemonade-change/
// 贪心算法专题 - 资源分配与最优选择问题集合

/*
 * 算法思路：
 * 1. 贪心策略：找零时优先使用大面额纸币
 * 2. 维护5美元和10美元纸币的数量
 * 3. 收到5美元：5美元数量加1
 * 4. 收到10美元：5美元数量减1，10美元数量加1
 * 5. 收到20美元：优先用一张10美元和一张5美元找零，如果没有10美元则用三张5美元找零
 *
 * 时间复杂度：O(n) - n是数组长度
 * 空间复杂度：O(1) - 只使用了常数额外空间
 * 是否最优解：是，这是处理此类问题的最优解法
 *
 * 工程化考量：
 * 1. 异常处理：检查输入是否为空
 * 2. 边界条件：处理空数组、单个元素等特殊情况
 * 3. 性能优化：一次遍历完成计算
 * 4. 可读性：清晰的变量命名和注释
 *
 * 极端场景与边界场景：
 * 1. 空输入：bills为空数组
 * 2. 极端值：只有一种面额的纸币
 * 3. 重复数据：多个相同面额的纸币
 * 4. 有序/逆序数据：纸币面额按顺序排列
 *
 * 跨语言场景与语言特性差异：
 * 1. Java：使用增强for循环遍历数组
 * 2. C++：使用传统for循环
 * 3. Python：使用for循环
 *
 * 调试能力构建：
 * 1. 打印中间过程：在循环中打印当前纸币面额和零钱数量
 * 2. 用断言验证中间结果：确保零钱数量不为负
 * 3. 性能退化排查：检查是否只遍历了一次数组
 *
 * 与机器学习、图像处理、自然语言处理的联系与应用：
 * 1. 在资源管理问题中，贪心算法可用于简单的资源分配策略
 * 2. 在模拟系统中，可以用于模拟交易过程
 * 3. 在游戏开发中，可以用于简单的经济系统设计
 */

// 柠檬水找零主函数
int lemonadeChange(int bills[], int billsSize) {
    // 异常处理：检查输入是否为空
    if (bills == 0 || billsSize == 0) {
        return 1;  // 1表示true，空数组表示没有顾客，返回true
    }
    
    int five = 0;   // 5美元纸币数量
    int ten = 0;    // 10美元纸币数量
    
    // 遍历顾客支付的纸币
    for (int i = 0; i < billsSize; i++) {
        int bill = bills[i];
        if (bill == 5) {
            // 收到5美元，不需要找零
            five++;
        } else if (bill == 10) {
            // 收到10美元，需要找零5美元
            if (five > 0) {
                five--;
                ten++;
            } else {
                // 没有5美元找零，返回false
                return 0;  // 0表示false
            }
        } else if (bill == 20) {
            // 收到20美元，需要找零15美元
            // 贪心策略：优先使用一张10美元和一张5美元找零
            if (ten > 0 && five > 0) {
                ten--;
                five--;
            } else if (five >= 3) {
                // 如果没有10美元，则用三张5美元找零
                five -= 3;
            } else {
                // 无法找零，返回false
                return 0;  // 0表示false
            }
        }
    }
    
    // 所有顾客都能正确找零
    return 1;  // 1表示true
}

// 补充题目1: LeetCode 455. 分发饼干
// 题目描述: 假设你是一位很棒的家长，想要给你的孩子们一些小饼干。但是，每个孩子最多只能给一块饼干。
// 对每个孩子 i，都有一个胃口值 g[i]，这是能让孩子们满足胃口的饼干的最小尺寸；
// 并且每块饼干 j，都有一个尺寸 s[j]。如果 s[j] >= g[i]，我们可以将这个饼干 j 分配给孩子 i，
// 这个孩子会得到满足。你的目标是尽可能满足越多数量的孩子，并输出这个最大数值。
// 链接: https://leetcode.cn/problems/assign-cookies/
#include <vector>
#include <algorithm>

int findContentChildren(int* g, int gSize, int* s, int sSize) {
    // 异常处理：检查输入是否为空
    if (g == nullptr || s == nullptr || gSize <= 0 || sSize <= 0) {
        return 0;  // 空数组，无法满足任何孩子
    }
    
    // 将数组转换为vector以便使用STL的sort函数
    std::vector<int> greed(g, g + gSize);
    std::vector<int> cookie(s, s + sSize);
    
    // 贪心策略：对胃口值和饼干尺寸进行排序，尽可能用最小的能满足孩子胃口的饼干
    std::sort(greed.begin(), greed.end());  // 对孩子的胃口排序
    std::sort(cookie.begin(), cookie.end());  // 对饼干尺寸排序
    
    int childIndex = 0;  // 当前考虑的孩子索引
    int cookieIndex = 0;  // 当前考虑的饼干索引
    
    // 遍历饼干和孩子
    while (childIndex < gSize && cookieIndex < sSize) {
        // 如果当前饼干能满足当前孩子的胃口
        if (cookie[cookieIndex] >= greed[childIndex]) {
            childIndex++;  // 孩子得到满足，移动到下一个孩子
        }
        // 无论如何都移动到下一个饼干
        cookieIndex++;
    }
    
    // 返回满足的孩子数量
    return childIndex;
}

// 补充题目2: LeetCode 135. 分发糖果
// 题目描述: n 个孩子站成一排。给你一个整数数组 ratings 表示每个孩子的评分。
// 你需要按照以下要求，给这些孩子分发糖果：
// 1. 每个孩子至少分配到 1 个糖果
// 2. 相邻两个孩子评分更高的孩子会获得更多的糖果
// 请你给每个孩子分发糖果，计算并返回需要准备的最小糖果数目。
// 链接: https://leetcode.cn/problems/candy/
int candy(int* ratings, int ratingsSize) {
    // 异常处理：检查输入是否为空
    if (ratings == nullptr || ratingsSize <= 0) {
        return 0;  // 空数组，不需要糖果
    }
    
    // 将数组转换为vector
    std::vector<int> rate(ratings, ratings + ratingsSize);
    std::vector<int> candies(ratingsSize, 1);  // 初始化：每个孩子至少有1个糖果
    
    // 贪心策略：从左到右遍历，确保右边评分高的孩子得到更多糖果
    for (int i = 1; i < ratingsSize; i++) {
        if (rate[i] > rate[i - 1]) {
            candies[i] = candies[i - 1] + 1;
        }
    }
    
    // 贪心策略：从右到左遍历，确保左边评分高的孩子得到更多糖果
    for (int i = ratingsSize - 2; i >= 0; i--) {
        if (rate[i] > rate[i + 1]) {
            candies[i] = std::max(candies[i], candies[i + 1] + 1);
        }
    }
    
    // 计算总糖果数
    int total = 0;
    for (int candy : candies) {
        total += candy;
    }
    
    return total;
}

// 补充题目3: LeetCode 605. 种花问题
// 题目描述: 假设有一个很长的花坛，一部分地块种植了花，另一部分却没有。
// 可是，花不能种植在相邻的地块上，它们会争夺水源，两者都会死去。
// 给你一个整数数组 flowerbed 表示花坛，由若干 0 和 1 组成，其中 0 表示没种植花，1 表示种植了花。
// 另有一个数 n ，能否在不打破种植规则的情况下种入 n 朵花？能则返回 true ，不能则返回 false。
// 链接: https://leetcode.cn/problems/can-place-flowers/
bool canPlaceFlowers(int* flowerbed, int flowerbedSize, int n) {
    // 异常处理：检查输入是否为空
    if (flowerbed == nullptr) {
        return n <= 0;  // 空数组，只能种0朵花
    }
    
    if (n <= 0) {
        return true;  // 需要种0朵花，直接返回true
    }
    
    int count = 0;  // 可以种植的花的数量
    
    // 贪心策略：遍历每个位置，如果可以种花就种
    for (int i = 0; i < flowerbedSize; i++) {
        // 检查当前位置、前一个位置和后一个位置是否都没有花
        bool leftEmpty = (i == 0) || (flowerbed[i - 1] == 0);
        bool rightEmpty = (i == flowerbedSize - 1) || (flowerbed[i + 1] == 0);
        
        if (flowerbed[i] == 0 && leftEmpty && rightEmpty) {
            count++;  // 可以种花
            flowerbed[i] = 1;  // 标记为已种花
            
            // 提前结束，如果已经能满足需求
            if (count >= n) {
                return true;
            }
        }
    }
    
    return count >= n;
}

// 补充题目4: LeetCode 406. 根据身高重建队列
// 题目描述: 假设有打乱顺序的一群人站成一个队列，数组 people 表示队列中一些人的属性（不一定按顺序）。
// 每个 people[i] = [hi, ki] 表示第 i 个人的身高为 hi ，前面 正好 有 ki 个身高大于或等于 hi 的人。
// 请你重新构造并返回输入数组 people 所表示的队列。返回的队列应该格式化为数组 queue ，
// 其中 queue[j] = [hj, kj] 是队列中第 j 个人的属性（queue[0] 是排在队列前面的人）。
// 链接: https://leetcode.cn/problems/queue-reconstruction-by-height/
#include <list>

// C++实现需要返回一个二维数组，需要动态分配内存
int** reconstructQueue(int** people, int peopleSize, int* peopleColSize, int* returnSize, int** returnColumnSizes) {
    // 异常处理：检查输入是否为空
    if (people == nullptr || peopleSize <= 1) {
        *returnSize = peopleSize;
        *returnColumnSizes = (int*)malloc(sizeof(int) * peopleSize);
        for (int i = 0; i < peopleSize; i++) {
            (*returnColumnSizes)[i] = 2;
        }
        return people;  // 空数组或只有一个人，直接返回
    }
    
    // 将输入数据转换为vector以便排序
    std::vector<std::vector<int>> peopleVec(peopleSize, std::vector<int>(2));
    for (int i = 0; i < peopleSize; i++) {
        peopleVec[i][0] = people[i][0];
        peopleVec[i][1] = people[i][1];
    }
    
    // 贪心策略：按身高降序排序，身高相同时按ki升序排序
    std::sort(peopleVec.begin(), peopleVec.end(), [](const std::vector<int>& a, const std::vector<int>& b) {
        if (a[0] != b[0]) {
            return a[0] > b[0];  // 身高降序
        } else {
            return a[1] < b[1];  // 身高相同时，ki升序
        }
    });
    
    // 使用链表插入，提高插入效率
    std::list<std::vector<int>> resultList;
    
    // 遍历排序后的数组，根据ki插入到相应位置
    for (const auto& person : peopleVec) {
        auto it = resultList.begin();
        // 移动迭代器到第ki个位置
        for (int i = 0; i < person[1]; i++) {
            it++;
        }
        resultList.insert(it, person);  // 插入到索引为ki的位置
    }
    
    // 准备返回值
    *returnSize = peopleSize;
    *returnColumnSizes = (int*)malloc(sizeof(int) * peopleSize);
    int** result = (int**)malloc(sizeof(int*) * peopleSize);
    
    // 将链表转换为二维数组
    int i = 0;
    for (const auto& person : resultList) {
        result[i] = (int*)malloc(sizeof(int) * 2);
        result[i][0] = person[0];
        result[i][1] = person[1];
        (*returnColumnSizes)[i] = 2;
        i++;
    }
    
    return result;
}

// 补充题目5: LeetCode 1005. K 次取反后最大化的数组和
// 题目描述: 给你一个整数数组 nums 和一个整数 k ，按以下方法修改数组：
// 1. 选择某个下标 i 并将 nums[i] 替换为 -nums[i] 。
// 你可以重复这个过程恰好 k 次。你也可以选择同一个下标 i 多次。
// 以这种方式修改数组后，返回数组 可能的最大和 。
// 链接: https://leetcode.cn/problems/maximize-sum-of-array-after-k-negations/
int largestSumAfterKNegations(int* nums, int numsSize, int k) {
    // 异常处理：检查输入是否为空
    if (nums == nullptr || numsSize <= 0) {
        return 0;  // 空数组，和为0
    }
    
    // 将数组转换为vector以便排序
    std::vector<int> numsVec(nums, nums + numsSize);
    
    // 贪心策略：每次将最小的负数变为正数，这样可以最大化数组和
    // 排序数组，从小到大
    std::sort(numsVec.begin(), numsVec.end());
    int i = 0;
    
    // 尽可能将负数变为正数
    while (i < numsSize && k > 0 && numsVec[i] < 0) {
        numsVec[i] = -numsVec[i];  // 取反
        k--;  // 减少取反次数
        i++;  // 移动到下一个元素
    }
    
    // 如果k还有剩余，且k是奇数，需要将最小的正数取反
    if (k > 0 && k % 2 == 1) {
        // 重新排序，找到最小的数
        std::sort(numsVec.begin(), numsVec.end());
        numsVec[0] = -numsVec[0];  // 将最小的数取反
    }
    
    // 计算数组和
    int sum = 0;
    for (int num : numsVec) {
        sum += num;
    }
    
    return sum;
}