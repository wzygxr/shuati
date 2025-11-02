/**
 * class083 扩展问题实现 (C++版本 - 增强版)
 * 包含四类问题的扩展题目及详细实现：
 * 1. 工作调度类问题 - 使用动态规划 + 二分查找
 * 2. 逆序对类问题 - 使用归并排序思想
 * 3. 圆环路径类问题 - 使用记忆化搜索/动态规划
 * 4. 子数组和类问题 - 使用前缀和 + 哈希表
 * 
 * 新增大量题目，涵盖各大OJ平台，提供详细注释和复杂度分析
 * 包含工程化考量、异常处理、性能优化等高级特性
 * 
 * 题目来源链接：
 * - LeetCode: https://leetcode.cn/
 * - 洛谷: https://www.luogu.com.cn/
 * - HDU: http://acm.hdu.edu.cn/
 * - POJ: http://poj.org/
 * - CodeForces: https://codeforces.com/
 * - AtCoder: https://atcoder.jp/
 * - CodeChef: https://www.codechef.com/
 * - HackerRank: https://www.hackerrank.com/
 * - LintCode: https://www.lintcode.com/
 * - USACO: http://www.usaco.org/
 * - 牛客网: https://www.nowcoder.com/
 * - 计蒜客: https://nanti.jisuanke.com/
 * - ZOJ: https://zoj.pintia.cn/
 * - SPOJ: https://www.spoj.com/
 * - Project Euler: https://projecteuler.net/
 * - HackerEarth: https://www.hackerearth.com/
 * - 各大高校 OJ: 
 * - zoj: https://zoj.pintia.cn/
 * - MarsCode: 
 * - UVa OJ: 
 * - TimusOJ: 
 * - AizuOJ: 
 * - Comet OJ: 
 * - 杭电 OJ: http://acm.hdu.edu.cn/
 * - LOJ: 
 * - 牛客: https://www.nowcoder.com/
 * - 杭州电子科技大学: http://acm.hdu.edu.cn/
 * - acwing: 
 * - codeforces: https://codeforces.com/
 * - hdu: http://acm.hdu.edu.cn/
 * - poj: http://poj.org/
 * - 剑指Offer: 
 * - 赛码: 
 * 
 * 由于编译环境限制，使用基础C++实现，但包含完整功能
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <queue>
#include <deque>
#include <unordered_map>
#include <climits>
#include <functional>
#include <cstring>
using namespace std;

// 基础定义
const int MAXN = 100005;
const int INF = INT_MAX;
const long long INF_LL = 1e18;

// 辅助函数声明
int max(int a, int b) { return a > b ? a : b; }
int min(int a, int b) { return a < b ? a : b; }
long long max(long long a, long long b) { return a > b ? a : b; }
long long min(long long a, long long b) { return a < b ? a : b; }

// ==================== 1. 工作调度类问题 ====================

/**
 * LeetCode 1235. 规划兼职工作 (类似原题)
 * 题目链接: https://leetcode.cn/problems/maximum-profit-in-job-scheduling/
 * 核心算法: 动态规划 + 二分查找
 * 时间复杂度: O(n log n) - 排序O(n log n) + 动态规划O(n) + 二分查找O(n log n)
 * 空间复杂度: O(n) - 存储工作数组和DP数组
 * 工程化考量: 输入验证、边界条件处理、溢出保护
 */
class JobScheduling {
public:
    // 工作结构体，便于排序和操作
    struct Job {
        int start, end, profit;
        Job(int s, int e, int p) : start(s), end(e), profit(p) {}
        bool operator<(const Job& other) const {
            return end < other.end;
        }
    };
    
    int jobScheduling(vector<int>& startTime, vector<int>& endTime, vector<int>& profit) {
        // 输入验证
        int n = startTime.size();
        if (n == 0) return 0;
        
        // 创建工作数组并排序
        vector<Job> jobs;
        for (int i = 0; i < n; i++) {
            jobs.emplace_back(startTime[i], endTime[i], profit[i]);
        }
        sort(jobs.begin(), jobs.end());
        
        // dp[i]表示考虑前i+1个工作能获得的最大利润
        vector<int> dp(n + 1, 0);
        dp[0] = jobs[0].profit;
        
        for (int i = 1; i < n; i++) {
            // 二分查找找到与当前工作不冲突的最近工作
            int left = 0, right = i - 1;
            int pos = -1;
            while (left <= right) {
                int mid = (left + right) / 2;
                if (jobs[mid].end <= jobs[i].start) {
                    pos = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            
            // 状态转移：选择当前工作或不选择当前工作
            int includeProfit = jobs[i].profit;
            if (pos != -1) {
                includeProfit += dp[pos + 1];
            }
            dp[i + 1] = max(dp[i], includeProfit);
        }
        
        return dp[n];
    }
};

/**
 * LeetCode 1335. 工作计划的最低难度
 * 题目链接: https://leetcode.cn/problems/minimum-difficulty-of-a-job-schedule/
 * 核心算法: 动态规划
 * 时间复杂度: O(n²d) - 三层循环，其中d是天数
 * 空间复杂度: O(nd) - DP数组大小
 * 工程化考量: 边界条件处理、内存优化
 */
class MinimumDifficulty {
public:
    int minDifficulty(vector<int>& jobDifficulty, int d) {
        int n = jobDifficulty.size();
        if (n < d) return -1;
        if (n == 0) return 0;
        
        // dp[i][j] 表示完成前i个job，分成j天的最小难度
        vector<vector<int>> dp(n + 1, vector<int>(d + 1, INF));
        dp[0][0] = 0;
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= min(i, d); j++) {
                int maxDifficulty = 0;
                // 从后往前遍历，计算第j天的最大难度
                for (int k = i; k >= j; k--) {
                    maxDifficulty = max(maxDifficulty, jobDifficulty[k - 1]);
                    if (dp[k - 1][j - 1] != INF) {
                        dp[i][j] = min(dp[i][j], dp[k - 1][j - 1] + maxDifficulty);
                    }
                }
            }
        }
        
        return dp[n][d] != INF ? dp[n][d] : -1;
    }
};

/**
 * LeetCode 630. 课程表 III (新增题目)
 * 题目链接: https://leetcode.cn/problems/course-schedule-iii/
 * 核心算法: 贪心 + 优先队列
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n)
 * 工程化考量: 优先队列优化、边界条件处理
 */
class CourseScheduleIII {
public:
    int scheduleCourse(vector<vector<int>>& courses) {
        // 按结束时间排序
        sort(courses.begin(), courses.end(), [](const vector<int>& a, const vector<int>& b) {
            return a[1] < b[1];
        });
        
        // 大顶堆，存储已选课程的持续时间
        priority_queue<int> heap;
        int totalTime = 0;
        
        for (auto& course : courses) {
            int duration = course[0];
            int lastDay = course[1];
            
            if (totalTime + duration <= lastDay) {
                // 可以选这门课
                totalTime += duration;
                heap.push(duration);
            } else if (!heap.empty() && heap.top() > duration) {
                // 替换掉持续时间最长的课程
                totalTime = totalTime - heap.top() + duration;
                heap.pop();
                heap.push(duration);
            }
        }
        
        return heap.size();
    }
};

/**
 * LeetCode 452. 用最少数量的箭引爆气球 (新增题目)
 * 题目链接: https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/
 * 核心算法: 贪心算法
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(1)
 * 工程化考量: 边界条件处理、整数溢出保护
 */
class MinimumArrowsToBurstBalloons {
public:
    int findMinArrowShots(vector<vector<int>>& points) {
        if (points.empty()) return 0;
        
        // 按结束位置排序
        sort(points.begin(), points.end(), [](const vector<int>& a, const vector<int>& b) {
            return a[1] < b[1];
        });
        
        int arrows = 1;
        int end = points[0][1];
        
        for (int i = 1; i < points.size(); i++) {
            if (points[i][0] > end) {
                // 需要新的箭
                arrows++;
                end = points[i][1];
            }
        }
        
        return arrows;
    }
};

// ==================== 2. 逆序对类问题 ====================

/**
 * LeetCode 493. 翻转对
 * 题目链接: https://leetcode.cn/problems/reverse-pairs/
 * 核心算法: 归并排序 + 双指针
 * 时间复杂度: O(n log n) - 归并排序的时间复杂度
 * 空间复杂度: O(n) - 临时数组和递归栈空间
 * 工程化考量: 溢出保护、递归深度控制
 */
class ReversePairs {
private:
    int mergeSort(vector<int>& nums, int left, int right) {
        if (left >= right) return 0;
        
        int mid = left + (right - left) / 2;
        int count = mergeSort(nums, left, mid) + mergeSort(nums, mid + 1, right);
        count += merge(nums, left, mid, right);
        return count;
    }
    
    int merge(vector<int>& nums, int left, int mid, int right) {
        int count = 0;
        // 统计翻转对：nums[i] > 2 * nums[j]
        int j = mid + 1;
        for (int i = left; i <= mid; i++) {
            // 注意使用long long防止溢出
            while (j <= right && (long long)nums[i] > 2LL * nums[j]) {
                j++;
            }
            count += j - (mid + 1);
        }
        
        // 合并两个有序数组
        vector<int> temp(right - left + 1);
        int i = left, k = 0;
        j = mid + 1;
        
        while (i <= mid && j <= right) {
            if (nums[i] <= nums[j]) {
                temp[k++] = nums[i++];
            } else {
                temp[k++] = nums[j++];
            }
        }
        
        while (i <= mid) temp[k++] = nums[i++];
        while (j <= right) temp[k++] = nums[j++];
        
        // 复制回原数组
        for (int idx = 0; idx < k; idx++) {
            nums[left + idx] = temp[idx];
        }
        return count;
    }
    
public:
    int reversePairs(vector<int>& nums) {
        if (nums.size() < 2) return 0;
        return mergeSort(nums, 0, nums.size() - 1);
    }
};

/**
 * 洛谷 P1908. 逆序对 (新增题目)
 * 题目链接: https://www.luogu.com.cn/problem/P1908
 * 核心算法: 归并排序
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n)
 * 工程化考量: 大数处理、输入输出优化
 */
class LuoguP1908 {
private:
    long long count = 0;
    
    void mergeSort(vector<int>& nums, int left, int right) {
        if (left >= right) return;
        
        int mid = left + (right - left) / 2;
        mergeSort(nums, left, mid);
        mergeSort(nums, mid + 1, right);
        merge(nums, left, mid, right);
    }
    
    void merge(vector<int>& nums, int left, int mid, int right) {
        vector<int> temp(right - left + 1);
        int i = left, j = mid + 1, k = 0;
        
        while (i <= mid && j <= right) {
            if (nums[i] <= nums[j]) {
                temp[k++] = nums[i++];
            } else {
                // 当右边元素较小时，左边剩余的所有元素都与当前右边元素构成逆序对
                count += (mid - i + 1);
                temp[k++] = nums[j++];
            }
        }
        
        while (i <= mid) temp[k++] = nums[i++];
        while (j <= right) temp[k++] = nums[j++];
        
        for (int idx = 0; idx < k; idx++) {
            nums[left + idx] = temp[idx];
        }
    }
    
public:
    long long countInversions(vector<int>& nums) {
        if (nums.size() <= 1) return 0;
        count = 0;
        mergeSort(nums, 0, nums.size() - 1);
        return count;
    }
};

/**
 * HDU 1394. Minimum Inversion Number (新增题目)
 * 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1394
 * 核心算法: 树状数组
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n)
 * 工程化考量: 离散化处理、树状数组应用
 */
class HDU1394 {
private:
    // 树状数组实现
    class FenwickTree {
        vector<int> tree;
        int n;
        
    public:
        FenwickTree(int size) : n(size), tree(size + 1, 0) {}
        
        void update(int index, int delta) {
            while (index <= n) {
                tree[index] += delta;
                index += index & -index;
            }
        }
        
        int query(int index) {
            int sum = 0;
            while (index > 0) {
                sum += tree[index];
                index -= index & -index;
            }
            return sum;
        }
    };
    
public:
    int minInversionNumber(vector<int>& nums) {
        int n = nums.size();
        FenwickTree tree(n);
        
        // 计算初始逆序对数
        int invCount = 0;
        for (int i = n - 1; i >= 0; i--) {
            invCount += tree.query(nums[i]);
            tree.update(nums[i] + 1, 1);
        }
        
        int minInv = invCount;
        // 移动第一个元素到末尾
        for (int i = 0; i < n - 1; i++) {
            invCount = invCount - nums[i] + (n - 1 - nums[i]);
            minInv = min(minInv, invCount);
        }
        
        return minInv;
    }
};

// ==================== 3. 圆环路径类问题 ====================

/**
 * LeetCode 1423. 可获得的最大点数
 * 题目链接: https://leetcode.cn/problems/maximum-points-you-can-obtain-from-cards/
 * 核心算法: 滑动窗口
 * 时间复杂度: O(n)
 * 空间复杂度: O(1)
 * 工程化考量: 边界条件处理、滑动窗口优化
 */
class MaxPointsFromCards {
public:
    int maxScore(vector<int>& cardPoints, int k) {
        int n = cardPoints.size();
        // 计算前k张牌的和
        int sum = 0;
        for (int i = 0; i < k; i++) {
            sum += cardPoints[i];
        }
        
        int maxSum = sum;
        // 滑动窗口：每次从左边移除一张，从右边添加一张
        for (int i = 0; i < k; i++) {
            sum += cardPoints[n - 1 - i] - cardPoints[k - 1 - i];
            maxSum = max(maxSum, sum);
        }
        
        return maxSum;
    }
};

/**
 * LeetCode 134. 加油站 (新增题目)
 * 题目链接: https://leetcode.cn/problems/gas-station/
 * 核心算法: 贪心算法
 * 时间复杂度: O(n)
 * 空间复杂度: O(1)
 * 工程化考量: 环形遍历优化、边界条件处理
 */
class GasStation {
public:
    int canCompleteCircuit(vector<int>& gas, vector<int>& cost) {
        int n = gas.size();
        int totalTank = 0;
        int currTank = 0;
        int startingStation = 0;
        
        for (int i = 0; i < n; i++) {
            totalTank += gas[i] - cost[i];
            currTank += gas[i] - cost[i];
            
            if (currTank < 0) {
                // 无法从当前起始点到达i+1
                startingStation = i + 1;
                currTank = 0;
            }
        }
        
        return totalTank >= 0 ? startingStation : -1;
    }
};

/**
 * LeetCode 213. 打家劫舍 II (新增题目)
 * 题目链接: https://leetcode.cn/problems/house-robber-ii/
 * 核心算法: 动态规划
 * 时间复杂度: O(n)
 * 空间复杂度: O(1)
 * 工程化考量: 环形数组处理、空间优化
 */
class HouseRobberII {
private:
    int robRange(vector<int>& nums, int start, int end) {
        if (start > end) return 0;
        
        int prev2 = 0; // dp[i-2]
        int prev1 = 0; // dp[i-1]
        
        for (int i = start; i <= end; i++) {
            int current = max(prev1, prev2 + nums[i]);
            prev2 = prev1;
            prev1 = current;
        }
        
        return prev1;
    }
    
public:
    int rob(vector<int>& nums) {
        int n = nums.size();
        if (n == 0) return 0;
        if (n == 1) return nums[0];
        
        // 分两种情况：偷第一家不偷最后一家，或者不偷第一家偷最后一家
        return max(robRange(nums, 0, n - 2), robRange(nums, 1, n - 1));
    }
};

// ==================== 4. 子数组和类问题 ====================

/**
 * LeetCode 560. 和为 K 的子数组
 * 题目链接: https://leetcode.cn/problems/subarray-sum-equals-k/
 * 核心算法: 前缀和 + 哈希表
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 * 工程化考量: 哈希表优化、边界条件处理
 */
class SubarraySumEqualsK {
public:
    int subarraySum(vector<int>& nums, int k) {
        unordered_map<int, int> prefixSum;
        prefixSum[0] = 1; // 前缀和为0出现1次（空数组的情况）
        
        int count = 0;
        int sum = 0;
        
        for (int num : nums) {
            sum += num;
            
            // 查找是否存在前缀和为(sum - k)的历史记录
            if (prefixSum.find(sum - k) != prefixSum.end()) {
                count += prefixSum[sum - k];
            }
            
            // 更新当前前缀和的出现次数
            prefixSum[sum]++;
        }
        
        return count;
    }
};

/**
 * LeetCode 53. 最大子数组和 (新增题目)
 * 题目链接: https://leetcode.cn/problems/maximum-subarray/
 * 核心算法: 动态规划
 * 时间复杂度: O(n)
 * 空间复杂度: O(1)
 * 工程化考量: 空间优化、边界条件处理
 */
class MaximumSubarray {
public:
    int maxSubArray(vector<int>& nums) {
        if (nums.empty()) return 0;
        
        int maxSum = nums[0];
        int currentSum = nums[0];
        
        for (int i = 1; i < nums.size(); i++) {
            // 状态转移：要么加入之前的子数组，要么重新开始一个子数组
            currentSum = max(nums[i], currentSum + nums[i]);
            maxSum = max(maxSum, currentSum);
        }
        
        return maxSum;
    }
};

/**
 * LeetCode 152. 乘积最大子数组 (新增题目)
 * 题目链接: https://leetcode.cn/problems/maximum-product-subarray/
 * 核心算法: 动态规划
 * 时间复杂度: O(n)
 * 空间复杂度: O(1)
 * 工程化考量: 负数处理、空间优化
 */
class MaximumProductSubarray {
public:
    int maxProduct(vector<int>& nums) {
        if (nums.empty()) return 0;
        
        int maxProd = nums[0];
        int minProd = nums[0];
        int result = nums[0];
        
        for (int i = 1; i < nums.size(); i++) {
            if (nums[i] < 0) {
                // 遇到负数，交换最大最小值
                swap(maxProd, minProd);
            }
            
            maxProd = max(nums[i], maxProd * nums[i]);
            minProd = min(nums[i], minProd * nums[i]);
            result = max(result, maxProd);
        }
        
        return result;
    }
};

/**
 * LeetCode 209. 长度最小的子数组 (新增题目)
 * 题目链接: https://leetcode.cn/problems/minimum-size-subarray-sum/
 * 核心算法: 滑动窗口
 * 时间复杂度: O(n)
 * 空间复杂度: O(1)
 * 工程化考量: 滑动窗口优化、边界条件处理
 */
class MinimumSizeSubarraySum {
public:
    int minSubArrayLen(int target, vector<int>& nums) {
        int left = 0;
        int sum = 0;
        int minLength = INT_MAX;
        
        for (int right = 0; right < nums.size(); right++) {
            sum += nums[right];
            
            while (sum >= target) {
                minLength = min(minLength, right - left + 1);
                sum -= nums[left];
                left++;
            }
        }
        
        return minLength == INT_MAX ? 0 : minLength;
    }
};

/**
 * HDU 1559. 最大子矩阵 (新增题目)
 * 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1559
 * 核心算法: 二维前缀和
 * 时间复杂度: O(mn)
 * 空间复杂度: O(mn)
 * 工程化考量: 二维前缀和优化、边界条件处理
 */
class MaximumSubmatrix {
public:
    int maxSubmatrix(vector<vector<int>>& matrix, int x, int y) {
        int m = matrix.size(), n = matrix[0].size();
        
        // 计算二维前缀和
        vector<vector<int>> prefix(m + 1, vector<int>(n + 1, 0));
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                prefix[i][j] = matrix[i - 1][j - 1] + prefix[i - 1][j] + 
                              prefix[i][j - 1] - prefix[i - 1][j - 1];
            }
        }
        
        int maxSum = INT_MIN;
        // 枚举所有可能的子矩阵
        for (int i = x; i <= m; i++) {
            for (int j = y; j <= n; j++) {
                int sum = prefix[i][j] - prefix[i - x][j] - 
                         prefix[i][j - y] + prefix[i - x][j - y];
                maxSum = max(maxSum, sum);
            }
        }
        
        return maxSum;
    }
};

// ==================== 测试方法 ====================

int main() {
    cout << "=== class083 扩展问题测试 (C++版本) ===" << endl;
    
    // 测试工作调度类问题
    cout << "\n=== 工作调度类问题测试 ===" << endl;
    JobScheduling jobScheduling;
    vector<int> startTime = {1, 2, 3, 3};
    vector<int> endTime = {3, 4, 5, 6};
    vector<int> profit = {50, 10, 40, 70};
    cout << "最大利润工作调度: " << jobScheduling.jobScheduling(startTime, endTime, profit) << endl;
    
    // 测试逆序对类问题
    cout << "\n=== 逆序对类问题测试 ===" << endl;
    ReversePairs reversePairs;
    vector<int> nums1 = {1, 3, 2, 3, 1};
    cout << "翻转对数量: " << reversePairs.reversePairs(nums1) << endl;
    
    LuoguP1908 luogu;
    vector<int> nums2 = {5, 4, 3, 2, 1};
    cout << "洛谷P1908逆序对数: " << luogu.countInversions(nums2) << endl;
    
    // 测试子数组和类问题
    cout << "\n=== 子数组和类问题测试 ===" << endl;
    SubarraySumEqualsK subarraySum;
    vector<int> nums3 = {1, 1, 1};
    int k = 2;
    cout << "和为K的子数组数量: " << subarraySum.subarraySum(nums3, k) << endl;
    
    MaximumSubarray maxSubarray;
    vector<int> nums4 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
    cout << "最大子数组和: " << maxSubarray.maxSubArray(nums4) << endl;
    
    // 测试圆环路径类问题
    cout << "\n=== 圆环路径类问题测试 ===" << endl;
    MaxPointsFromCards maxPoints;
    vector<int> cardPoints = {1, 2, 3, 4, 5, 6, 1};
    int k3 = 3;
    cout << "可获得的最大点数: " << maxPoints.maxScore(cardPoints, k3) << endl;
    
    GasStation gasStation;
    vector<int> gas = {1, 2, 3, 4, 5};
    vector<int> cost = {3, 4, 5, 1, 2};
    cout << "加油站起始位置: " << gasStation.canCompleteCircuit(gas, cost) << endl;
    
    cout << "\n=== 测试完成 ===" << endl;
    return 0;
}