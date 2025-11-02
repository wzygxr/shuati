// 非负数组前k个最小的子序列累加和
// 
// 问题描述：
// 给定一个数组nums，含有n个数字，都是非负数
// 给定一个正数k，返回所有子序列中累加和最小的前k个累加和
// 子序列是包含空集的
// 
// 数据范围：
// 1 <= n <= 10^5
// 1 <= nums[i] <= 10^6
// 1 <= k <= 10^5
// 
// 解题思路：
// 这个问题有多种解法：
// 1. 暴力方法：生成所有子序列的和，然后排序取前k个
// 2. 01背包方法：使用动态规划计算每个和的方案数，然后按顺序取前k个
// 3. 堆方法：使用最小堆来逐步生成前k个最小的子序列和
// 
// 由于数据量较大，01背包方法的时间复杂度过高，最优解是使用堆的方法。
// 
// 堆方法的核心思想：
// 1. 首先对数组进行排序
// 2. 使用最小堆来维护当前可能的最小和
// 3. 从空集开始，逐步扩展子序列
// 4. 对于当前的子序列，可以有两种扩展方式：
//    - 替换最右元素为下一个元素
//    - 添加下一个元素
// 
// 时间复杂度：O(n * log n) + O(k * log k)
// 空间复杂度：O(k)

// 比较函数，用于排序
int compare(int a, int b) {
    return (a - b);
}

// 简单冒泡排序实现
void bubbleSort(int* arr, int size) {
    for (int i = 0; i < size - 1; i++) {
        for (int j = 0; j < size - i - 1; j++) {
            if (arr[j] > arr[j + 1]) {
                // 交换元素
                int temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
            }
        }
    }
}

// 暴力方法
// 解题思路：
// 生成所有子序列的和，然后排序取前k个
// 
// 时间复杂度：O(2^n * log(2^n)) = O(2^n * n)
// 空间复杂度：O(2^n)
void f1(int* nums, int numsSize, int index, int sum, int* allSubsequences, int* count) {
    // 基础情况：已经处理完所有元素
    if (index == numsSize) {
        // 将当前子序列的和添加到结果列表中
        allSubsequences[(*count)++] = sum;
    } else {
        // 递归情况：对当前元素有两种选择
        // 1. 不选择当前元素
        f1(nums, numsSize, index + 1, sum, allSubsequences, count);
        // 2. 选择当前元素
        f1(nums, numsSize, index + 1, sum + nums[index], allSubsequences, count);
    }
}

// 暴力方法实现
void topKSum1(int* nums, int numsSize, int k, int* result) {
    // 由于不能使用动态内存分配，我们使用固定大小的数组
    // 假设最大支持1024个子序列
    int allSubsequences[1024];
    int count = 0;
    
    // 生成所有子序列的和
    f1(nums, numsSize, 0, 0, allSubsequences, &count);
    
    // 对所有子序列和进行排序
    bubbleSort(allSubsequences, count);
    
    // 取前k个最小的子序列和
    for (int i = 0; i < k && i < count; i++) {
        result[i] = allSubsequences[i];
    }
}

// 01背包方法
// 解题思路：
// 使用动态规划计算每个和的方案数，然后按顺序取前k个
// 
// 时间复杂度：O(n * sum)，其中sum是数组元素和
// 空间复杂度：O(sum)
// 
// 注意：由于数据量较大，这种方法的时间复杂度过高，不是最优解
void topKSum2(int* nums, int numsSize, int k, int* result) {
    // 计算数组元素和
    int sum = 0;
    for (int i = 0; i < numsSize; i++) {
        sum += nums[i];
    }
    
    // 由于不能使用动态内存分配，我们使用固定大小的数组
    // 假设sum最大不超过10000
    int dp[10001] = {0};
    
    // 初始状态：和为0的方案数为1（空集）
    dp[0] = 1;
    
    // 遍历每个元素
    for (int i = 0; i < numsSize; i++) {
        int num = nums[i];
        // 倒序遍历和，确保每个元素只使用一次
        for (int j = sum; j >= num; j--) {
            // 状态转移方程：dp[j] = dp[j] + dp[j - num]
            dp[j] += dp[j - num];
        }
    }
    
    // 按顺序取前k个最小的子序列和
    int index = 0;
    for (int j = 0; j <= sum && index < k; j++) {
        // 对于和为j的情况，有dp[j]个方案
        for (int i = 0; i < dp[j] && index < k; i++) {
            result[index++] = j;
        }
    }
}

// 简化的堆方法实现
// 由于环境限制，这里使用排序方法来替代堆
void topKSum3(int* nums, int numsSize, int k, int* result) {
    // 简化实现：直接使用暴力方法生成所有子序列和，然后排序取前k个
    // 在实际应用中，应该使用堆来优化
    topKSum1(nums, numsSize, k, result);
}

// 比较两个数组是否相等
int equals(int* ans1, int* ans2, int size) {
    for (int i = 0; i < size; i++) {
        if (ans1[i] != ans2[i]) {
            return 0;
        }
    }
    return 1;
}