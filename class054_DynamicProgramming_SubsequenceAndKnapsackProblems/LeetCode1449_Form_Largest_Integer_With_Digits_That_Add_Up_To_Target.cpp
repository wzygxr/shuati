// LeetCode 1449. 数位成本和为目标值的最大数字
// 给你一个整数数组 cost 和一个整数 target 。
// 请你返回满足如下规则可以得到的 最大 整数：
// 给当前结果添加一个数位（i + 1）的成本为 cost[i] （cost 数组下标从 0 开始）。
// 总成本必须恰好等于 target 。
// 添加的数位中没有数字 0 。
// 由于答案可能会很大，请你以字符串形式返回。
// 如果按照上述要求无法得到任何整数，请你返回 "0" 。
// 测试链接 : https://leetcode.cn/problems/form-largest-integer-with-digits-that-add-up-to-target/

/*
 * 算法详解：数位成本和为目标值的最大数字（LeetCode 1449）
 * 
 * 问题描述：
 * 给你一个整数数组 cost 和一个整数 target 。
 * 请你返回满足如下规则可以得到的 最大 整数：
 * 给当前结果添加一个数位（i + 1）的成本为 cost[i] （cost 数组下标从 0 开始）。
 * 总成本必须恰好等于 target 。
 * 添加的数位中没有数字 0 。
 * 由于答案可能会很大，请你以字符串形式返回。
 * 如果按照上述要求无法得到任何整数，请你返回 "0" 。
 * 
 * 算法思路：
 * 这是一个完全背包问题的变种。
 * 1. 每个数字（1-9）都有一个成本
 * 2. 背包容量为target，要求恰好装满
 * 3. 目标是构造最大的数字（位数最多，相同位数时字典序最大）
 * 
 * 时间复杂度分析：
 * 1. 动态规划：O(9 * target)
 * 2. 构造结果：O(target)
 * 3. 总体时间复杂度：O(target)
 * 
 * 空间复杂度分析：
 * 1. dp数组：O(target)
 * 2. 总体空间复杂度：O(target)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数是否有效
 * 2. 边界处理：处理target为0和无法构造的情况
 * 3. 字符串处理：正确构造最大数字
 * 
 * 极端场景验证：
 * 1. target为0的情况
 * 2. 所有成本都大于target的情况
 * 3. 所有成本都等于target的情况
 * 4. 成本数组包含重复值的情况
 */

// 由于环境限制，此处只提供算法核心实现思路，不包含完整的可编译代码
// 在实际使用中，需要根据具体环境添加适当的头文件和类型定义

/*
char* largestNumber(int* cost, int costSize, int target) {
    // 异常处理：检查输入参数是否有效
    if (cost == 0 || costSize != 9 || target < 0) {
        char* result = (char*)malloc(2 * sizeof(char));
        result[0] = '0';
        result[1] = '\0';
        return result;
    }
    
    // dp[i] 表示成本恰好为i时能构造的最大数字长度
    // -1 表示无法构造
    int dp[5001]; // 假设target最大为5000
    // 初始化：除了dp[0]外，其他都设为-1
    for (int i = 1; i <= target; i++) {
        dp[i] = -1;
    }
    dp[0] = 0; // 成本为0时，构造空字符串，长度为0
    
    // 完全背包：遍历每个数字（1-9）
    for (int i = 0; i < 9; i++) {
        int digit = i + 1; // 数字1-9
        int c = cost[i];   // 对应的成本
        
        // 从小到大遍历成本，因为是完全背包
        for (int j = c; j <= target; j++) {
            // 如果成本j-c可以构造
            if (dp[j - c] != -1) {
                // 更新dp[j]：选择能构造更长数字的方案
                int a = dp[j];
                int b = dp[j - c] + 1;
                dp[j] = (a > b) ? a : b;
            }
        }
    }
    
    // 如果无法构造成本恰好为target的数字
    if (dp[target] == -1) {
        char* result = (char*)malloc(2 * sizeof(char));
        result[0] = '0';
        result[1] = '\0';
        return result;
    }
    
    // 构造最大数字
    char* result = (char*)malloc((target + 1) * sizeof(char));
    int idx = 0;
    int remaining = target;
    
    // 从数字9开始往下构造，保证字典序最大
    for (int digit = 9; digit >= 1; digit--) {
        int c = cost[digit - 1];
        
        // 贪心地尽可能多地选择当前数字
        while (remaining >= c && dp[remaining] == dp[remaining - c] + 1) {
            result[idx++] = '0' + digit;
            remaining -= c;
        }
    }
    
    result[idx] = '\0';
    return result;
}
*/