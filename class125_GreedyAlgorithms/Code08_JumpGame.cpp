// 跳跃游戏
// 给定一个非负整数数组 nums ，你最初位于数组的第一个下标。
// 数组中的每个元素代表你在该位置可以跳跃的最大长度。
// 判断你是否能够到达最后一个下标。
// 测试链接 : https://leetcode.cn/problems/jump-game/

/*
 * 算法思路：
 * 1. 贪心策略：维护能到达的最远位置
 * 2. 遍历数组，更新能到达的最远位置
 * 3. 如果当前位置超过了能到达的最远位置，则无法到达终点
 * 4. 如果能到达的最远位置大于等于最后一个下标，则能到达终点
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
 * 1. 空输入：nums为空数组
 * 2. 极端值：只有一个元素、所有元素都是0
 * 3. 重复数据：多个元素相同
 * 4. 有序/逆序数据：元素值递增或递减
 *
 * 跨语言场景与语言特性差异：
 * 1. Java：使用增强for循环遍历数组
 * 2. C++：使用传统for循环
 * 3. Python：使用for循环或enumerate
 *
 * 调试能力构建：
 * 1. 打印中间过程：在循环中打印当前位置和最远可达位置
 * 2. 用断言验证中间结果：确保最远位置不减小
 * 3. 性能退化排查：检查是否只遍历了一次数组
 *
 * 与机器学习、图像处理、自然语言处理的联系与应用：
 * 1. 在路径规划问题中，贪心算法可用于快速判断可达性
 * 2. 在图论算法中，可以作为初始解提供给更复杂的算法
 * 3. 在网络路由中，可以用于快速判断连通性
 */

// 跳跃游戏主函数
int canJump(int nums[], int numsSize) {
    // 异常处理：检查输入是否为空
    if (nums == 0 || numsSize == 0) {
        return 0;  // 0表示false
    }
    
    // 边界条件：只有一个元素，肯定能到达
    if (numsSize == 1) {
        return 1;  // 1表示true
    }
    
    int maxReach = 0;  // 能到达的最远位置
    
    // 遍历数组
    for (int i = 0; i < numsSize; i++) {
        // 如果当前位置超过了能到达的最远位置，则无法到达终点
        if (i > maxReach) {
            return 0;  // 0表示false
        }
        
        // 更新能到达的最远位置
        int currentReach = i + nums[i];
        if (currentReach > maxReach) {
            maxReach = currentReach;
        }
        
        // 如果能到达的最远位置大于等于最后一个下标，则能到达终点
        if (maxReach >= numsSize - 1) {
            return 1;  // 1表示true
        }
    }
    
    return 0;  // 0表示false
}

// 补充题目1: LeetCode 134. 加油站
// 题目描述: 在一条环路上有 n 个加油站，其中第 i 个加油站有汽油 gas[i] 升。
// 你有一辆油箱容量无限的的汽车，从第 i 个加油站开往第 i+1 个加油站需要消耗汽油 cost[i] 升。
// 你从其中的一个加油站出发，开始时油箱为空。
// 如果你可以绕环路行驶一周，则返回出发时加油站的编号，否则返回 -1。
// 注意：如果题目有解，该答案即为唯一答案。
// 链接: https://leetcode.cn/problems/gas-station/

int canCompleteCircuit(int* gas, int gasSize, int* cost, int costSize) {
    if (gas == NULL || cost == NULL || gasSize != costSize) {
        return -1; // 输入不合法
    }
    
    int n = gasSize;
    int totalGas = 0;    // 总油量
    int totalCost = 0;   // 总消耗
    int currentGas = 0;  // 当前剩余油量
    int startStation = 0; // 起始加油站
    
    // 贪心策略：如果从A到B的路上没油了，那么A到B之间的任何一个站点都不能作为起点
    for (int i = 0; i < n; i++) {
        totalGas += gas[i];
        totalCost += cost[i];
        currentGas += gas[i] - cost[i];
        
        // 如果当前剩余油量为负，说明从startStation到i的路径不可行
        if (currentGas < 0) {
            startStation = i + 1; // 从下一个站点重新开始计算
            currentGas = 0;       // 重置当前剩余油量
        }
    }
    
    // 如果总油量小于总消耗，那么无论如何都不可能绕行一周
    if (totalGas < totalCost) {
        return -1;
    }
    
    // 否则，startStation就是答案
    return startStation;
}

// 补充题目2: LeetCode 561. 数组拆分 I
// 题目描述: 给定长度为 2n 的整数数组 nums ，你的任务是将这些数分成 n 对，
// 例如 (a1, b1), (a2, b2), ..., (an, bn) ，使得从 1 到 n 的 min(ai, bi) 总和最大。
// 返回该 最大总和 。
// 链接: https://leetcode.cn/problems/array-partition-i/

int arrayPairSum(int* nums, int numsSize) {
    if (nums == NULL || numsSize % 2 != 0) {
        return 0; // 输入不合法
    }
    
    // 贪心策略：将数组排序后，每两个相邻的数分为一组，取较小的那个（即每对中的第一个数）
    sort(nums, nums + numsSize);
    int maxSum = 0;
    
    // 每隔一个元素取一个（即每对中的第一个元素）
    for (int i = 0; i < numsSize; i += 2) {
        maxSum += nums[i];
    }
    
    return maxSum;
}

// 补充题目3: LeetCode 402. 移掉K位数字
// 题目描述: 给你一个以字符串表示的非负整数 num 和一个整数 k ，
// 移除这个数中的 k 位数字，使得剩下的数字最小。
// 请你以字符串形式返回这个最小的数字。
// 链接: https://leetcode.cn/problems/remove-k-digits/

char* removeKdigits(char* num, int k) {
    if (num == NULL || strlen(num) == 0 || k >= strlen(num)) {
        char* result = (char*)malloc(2 * sizeof(char));
        result[0] = '0';
        result[1] = '\0';
        return result;
    }
    
    int n = strlen(num);
    char* stack = (char*)malloc((n + 1) * sizeof(char)); // 使用栈来存储需要保留的数字
    int top = 0; // 栈顶指针
    
    // 贪心策略：从左到右遍历，如果当前数字小于栈顶数字，且还有删除次数，则弹出栈顶数字
    for (int i = 0; i < n; i++) {
        char digit = num[i];
        // 当栈不为空，当前数字小于栈顶数字，且还有删除次数时，弹出栈顶数字
        while (top > 0 && digit < stack[top - 1] && k > 0) {
            top--;
            k--;
        }
        // 将当前数字入栈
        stack[top++] = digit;
    }
    
    // 如果还有删除次数，从栈顶删除
    while (k > 0 && top > 0) {
        top--;
        k--;
    }
    
    // 去除前导零
    int start = 0;
    while (start < top && stack[start] == '0') {
        start++;
    }
    
    // 构建结果字符串
    char* result;
    if (start == top) {
        // 如果全是零，返回"0"
        result = (char*)malloc(2 * sizeof(char));
        result[0] = '0';
        result[1] = '\0';
    } else {
        result = (char*)malloc((top - start + 1) * sizeof(char));
        strncpy(result, stack + start, top - start);
        result[top - start] = '\0';
    }
    
    free(stack);
    return result;
}

// 补充题目4: LeetCode 122. 买卖股票的最佳时机 II（另一种贪心实现）
// 题目描述: 给定一个数组 prices ，其中 prices[i] 表示股票第 i 天的价格。
// 在每一天，你可能会决定购买和/或出售股票。你在任何时候 最多 只能持有 一股 股票。
// 你也可以购买它，然后在 同一天 出售。
// 返回 你能获得的 最大 利润 。
// 链接: https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/

int maxProfit(int* prices, int pricesSize) {
    if (prices == NULL || pricesSize <= 1) {
        return 0; // 无法交易
    }
    
    int maxProfit = 0;
    // 贪心策略：只要后一天的价格比前一天高，就进行一次买卖
    for (int i = 1; i < pricesSize; i++) {
        // 如果当天价格比前一天高，就进行交易
        if (prices[i] > prices[i - 1]) {
            maxProfit += prices[i] - prices[i - 1];
        }
    }
    
    return maxProfit;
}

// 补充题目5: LeetCode 665. 非递减数列
// 题目描述: 给你一个长度为 n 的整数数组 nums ，请你判断在 最多 改变 1 个元素的情况下，
// 该数组能否变成一个非递减数列。
// 非递减数列的定义是：对于数组中任意的 i (0 <= i <= n-2)，总满足 nums[i] <= nums[i+1]。
// 链接: https://leetcode.cn/problems/non-decreasing-array/

bool checkPossibility(int* nums, int numsSize) {
    if (nums == NULL || numsSize <= 1) {
        return true; // 空数组或只有一个元素是非递减的
    }
    
    int count = 0; // 记录需要修改的次数
    
    for (int i = 0; i < numsSize - 1; i++) {
        if (nums[i] > nums[i + 1]) {
            count++;
            if (count > 1) {
                return false; // 需要修改超过1次
            }
            
            // 贪心策略：尽可能修改nums[i]而不是nums[i+1]，这样对后续影响更小
            // 但是如果nums[i-1] > nums[i+1]，则必须修改nums[i+1]
            if (i > 0 && nums[i - 1] > nums[i + 1]) {
                nums[i + 1] = nums[i]; // 修改nums[i+1]
            } else {
                nums[i] = nums[i + 1]; // 修改nums[i]
            }
        }
    }
    
    return true;
}