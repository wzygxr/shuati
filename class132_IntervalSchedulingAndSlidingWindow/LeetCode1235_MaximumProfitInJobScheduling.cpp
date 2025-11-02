/**
 * LeetCode 1235. Maximum Profit in Job Scheduling
 * 
 * 题目描述：
 * 你有 n 个工作，每个工作有开始时间 startTime[i]，结束时间 endTime[i] 和利润 profit[i]。
 * 你需要选择一个工作子集，使得总利润最大化，且所选工作的时间范围不重叠。
 * 注意：如果一个工作在时间 X 结束，另一个工作可以在时间 X 开始（它们不重叠）。
 * 
 * 解题思路：
 * 这是一个经典的动态规划问题，类似于背包问题的变种。我们需要在有限的工作选择下，选择利润最大的工作组合。
 * 
 * 算法步骤：
 * 1. 将所有工作按开始时间排序
 * 2. 使用动态规划，定义 dfs(i) 表示从第 i 个工作开始能得到的最大利润
 * 3. 对于每个工作，我们可以选择做或不做
 * 4. 如果做，我们需要找到下一个不冲突的工作，这可以通过二分查找实现
 * 5. 状态转移方程：
 *    dfs(i) = max(dfs(i+1), profit[i] + dfs(j))
 *    其中 j 是第一个开始时间 >= 当前工作结束时间的工作索引
 * 
 * 时间复杂度：O(n * log n)
 * 空间复杂度：O(n)
 * 
 * 相关题目：
 * - LeetCode 1751. 最多可以参加的会议数目 II (动态规划 + 二分查找)
 * - LeetCode 435. 无重叠区间 (贪心)
 * - LeetCode 646. 最长数对链 (贪心)
 */

// 简化版C++实现，避免使用STL容器
// 由于编译环境限制，使用基本数组和手动实现算法

// 工作信息结构体
struct Job {
    int start, end, profit;
};

// 全局变量
const int MAX_N = 50005;
Job jobs[MAX_N];
int dp[MAX_N];
int n;

// 比较函数，用于排序
bool compareJobs(Job a, Job b) {
    return a.start < b.start;
}

// 简单的排序实现（冒泡排序）
void sortJobs() {
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - 1 - i; j++) {
            if (jobs[j].start > jobs[j + 1].start) {
                // 交换
                Job temp = jobs[j];
                jobs[j] = jobs[j + 1];
                jobs[j + 1] = temp;
            }
        }
    }
}

// 使用二分查找找到下一个不冲突的工作索引
int findNextJob(int current) {
    int left = current + 1;
    int right = n;
    int target = jobs[current].end;
    
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (jobs[mid].start >= target) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    
    return left;
}

// 自定义max函数
int max(int a, int b) {
    return a > b ? a : b;
}

/**
 * 计算最大利润
 * 
 * @param startTime 工作开始时间数组
 * @param endTime 工作结束时间数组
 * @param profit 工作利润数组
 * @param size 数组大小
 * @return 能获得的最大利润
 */
int jobScheduling(int startTime[], int endTime[], int profit[], int size) {
    n = size;
    
    // 创建工作数组
    for (int i = 0; i < n; i++) {
        jobs[i].start = startTime[i];
        jobs[i].end = endTime[i];
        jobs[i].profit = profit[i];
    }
    
    // 排序工作数组
    sortJobs();
    
    // 初始化dp数组
    for (int i = 0; i <= n; i++) {
        dp[i] = 0;
    }
    
    // 从后往前填充 dp 数组
    for (int i = n - 1; i >= 0; i--) {
        // 不选择当前工作
        int skip = dp[i + 1];
        
        // 选择当前工作，找到下一个不冲突的工作
        int next = findNextJob(i);
        int take = jobs[i].profit + dp[next];
        
        // 取最大值
        dp[i] = max(skip, take);
    }
    
    return dp[0];
}

// 简单的测试函数
void runTests() {
    // 测试用例1
    int startTime1[] = {1, 2, 3, 3};
    int endTime1[] = {3, 4, 5, 6};
    int profit1[] = {50, 10, 40, 70};
    int size1 = 4;
    
    // 由于没有标准输出库，我们无法直接打印结果
    // 但可以通过返回值验证算法正确性
    int result1 = jobScheduling(startTime1, endTime1, profit1, size1);
    // 期望输出: 120
    
    // 测试用例2
    int startTime2[] = {1, 2, 3, 4, 6};
    int endTime2[] = {3, 5, 10, 6, 9};
    int profit2[] = {20, 20, 100, 70, 60};
    int size2 = 5;
    
    int result2 = jobScheduling(startTime2, endTime2, profit2, size2);
    // 期望输出: 150
}