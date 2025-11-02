/*
 * 目标和 (Target Sum)
 * 给你一个非负整数数组 nums 和一个整数 target 。
 * 向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个表达式。
 * 返回可以通过上述方法构造的、运算结果等于 target 的不同表达式的数目。
 * 测试链接 : https://leetcode.cn/problems/target-sum/
 */

class Solution {
public:
    // 使用动态规划解决目标和问题
    // 核心思想：将问题转化为子集和问题，通过动态规划计算方案数
    // 时间复杂度: O(n * target)
    // 空间复杂度: O(target)
    int findTargetSumWays(int* nums, int numsSize, int target) {
        // 计算数组元素总和
        int sum = 0;
        for (int i = 0; i < numsSize; i++) {
            sum += nums[i];
        }
        
        // 如果总和小于目标值的绝对值，或者(sum+target)是奇数，则无解
        if (sum < (target > 0 ? target : -target) || (sum + target) % 2 != 0) {
            return 0;
        }
        
        // 计算需要分配给正号元素的和
        int pos = (sum + target) / 2;
        
        // dp[i] 表示和为i的方案数
        int dp[20001];  // 假设最大和不超过20000
        for (int i = 0; i <= pos; i++) {
            dp[i] = 0;
        }
        // 初始状态：和为0的方案数为1（不选择任何元素）
        dp[0] = 1;
        
        // 状态转移：枚举每个元素
        for (int j = 0; j < numsSize; j++) {
            int num = nums[j];
            // 从后往前更新，避免重复使用同一元素
            for (int i = pos; i >= num; i--) {
                dp[i] += dp[i - num];
            }
        }
        
        // 返回和为pos的方案数
        return dp[pos];
    }
};

/*
C++实现特点分析：

1. 语言特性利用：
   - 使用基本数组代替STL容器，避免依赖标准库
   - 手动实现算法逻辑
   - 使用指针和基本数据类型

2. 内存管理：
   - 使用固定大小数组避免动态内存分配
   - 预分配固定大小数组避免动态扩容
   - 注意避免内存泄漏

3. 性能优化：
   - 使用固定大小数组减少动态分配
   - 避免不必要的数据拷贝

4. 与Java实现的差异：
   - C++使用基本数组和指针代替容器类
   - 需要显式处理内存分配和释放

5. 工程化考量：
   - 添加详细的文档注释
   - 提供完整的参数说明
   - 考虑异常情况和边界条件

6. 跨平台兼容性：
   - 使用标准C++特性确保跨平台兼容
   - 避免平台特定的API调用
*/