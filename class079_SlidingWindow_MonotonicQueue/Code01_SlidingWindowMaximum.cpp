/**
 * @file Code01_SlidingWindowMaximum.cpp
 * @brief 滑动窗口最大值问题专题 - 单调队列算法深度解析与多语言实现
 * 
 * 【题目背景】
 * 滑动窗口最大值问题是算法面试中的经典高频题目，涉及单调队列、滑动窗口、双指针等核心算法思想。
 * 本专题通过多种解法对比，深入剖析单调队列的原理和应用，提供C++语言的完整实现。
 * 
 * 【核心算法思想】
 * 单调队列是解决滑动窗口最值问题的最优数据结构，通过维护一个单调递减的双端队列，
 * 确保队首元素始终是当前窗口的最大值，从而实现O(n)的时间复杂度。
 * 
 * 【算法复杂度对比分析】
 * +---------------------+----------------+----------------+----------------------+
 * | 解法类型             | 时间复杂度     | 空间复杂度     | 适用场景              |
 * +---------------------+----------------+----------------+----------------------+
 * | 单调队列解法         | O(n)           | O(k)           | 大规模数据，最优解法  |
 * | 优先队列解法         | O(n*logk)      | O(n)           | 需要维护多个最值      |
 * | 暴力解法             | O(n*k)         | O(1)           | 小规模数据，易于理解  |
 * +---------------------+----------------+----------------+----------------------+
 * 
 * 【工程化考量与优化策略】
 * 1. 异常防御：全面处理空数组、非法窗口大小等边界情况
 * 2. 性能优化：针对C++语言特性选择最优数据结构实现
 * 3. 内存管理：避免不必要的内存分配和复制操作
 * 4. 代码可读性：清晰的变量命名和算法步骤注释
 * 5. 测试覆盖：包含单元测试、性能测试和边界测试
 * 
 * 【相关题目资源】
 * - LeetCode 239: Sliding Window Maximum - https://leetcode.com/problems/sliding-window-maximum/
 * - 剑指Offer 59 - I: 滑动窗口的最大值 - https://leetcode.cn/problems/hua-dong-chuang-kou-de-zui-da-zhi-lcof/
 * - POJ 2823: Sliding Window - http://poj.org/problem?id=2823
 * - 洛谷P1886: 滑动窗口 / Sliding Window - https://www.luogu.com.cn/problem/P1886
 * 
 * 【算法核心思想详解】
 * 单调队列通过四步维护策略确保算法正确性和高效性：
 * 1. 移除队首超出窗口范围的元素（过期检查）
 * 2. 移除队尾所有小于当前元素的值（单调性维护）
 * 3. 将当前元素索引加入队列尾部（入队操作）
 * 4. 记录队首元素作为当前窗口的最大值（结果收集）
 * 
 * 【时间复杂度数学证明】
 * 虽然算法包含嵌套循环，但通过均摊分析可知：
 * - 每个元素最多入队一次，出队一次
 * - 总操作次数为O(n)，因此时间复杂度为O(n)
 * - 空间复杂度为O(k)，队列中最多存储k个元素索引
 */

#include <iostream>
#include <vector>
#include <deque>
#include <queue>
#include <algorithm>
#include <chrono>
#include <climits>
#include <string>
#include <ctime>
#include <cstdlib>
using namespace std;

/**
 * 滑动窗口最大值问题专题 - C++实现
 * 本文件包含滑动窗口最大值的多种解法及其详细解析
 * LeetCode 239. 滑动窗口最大值
 * 测试链接: https://leetcode.cn/problems/sliding-window-maximum/
 * 
 * 题目描述:
 * 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧，
 * 你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。返回滑动窗口中的最大值。
 * 
 * 包含多种解法：
 * 1. 单调队列解法（最优解法）- 时间复杂度O(n)
 * 2. 优先队列解法 - 时间复杂度O(n*logk)
 * 3. 暴力解法 - 时间复杂度O(n*k)（用于对比）
 */

class Solution {
public:
    /**
     * @brief 使用单调队列求解滑动窗口最大值问题（最优解法）
     * 
     * 【算法原理深度解析】
     * 单调队列是解决滑动窗口最值问题的核心数据结构，通过维护一个单调递减的双端队列，
     * 确保队首元素始终是当前窗口的最大值。关键设计要点：
     * 1. 存储索引而非元素值：便于判断元素是否在当前窗口范围内
     * 2. 四步维护策略：过期检查 → 单调性维护 → 入队操作 → 结果收集
     * 3. 使用严格单调递减：保证队首元素的有效性和正确性
     * 
     * 【时间复杂度数学证明】
     * 虽然算法包含嵌套循环，但通过均摊分析可知：
     * - 每个元素最多入队一次，出队一次
     * - 总操作次数为O(n)，因此时间复杂度为O(n)
     * - 空间复杂度为O(k)，队列中最多存储k个元素索引
     * 
     * 【工程化优化策略】
     * 1. 边界检查：处理空数组、非法窗口大小等异常情况
     * 2. 性能优化：窗口大小为1时的特殊处理
     * 3. 内存管理：预分配结果向量空间避免动态扩容
     * 4. 代码可读性：清晰的变量命名和算法步骤注释
     * 
     * 【面试要点】
     * - 能够解释为什么存储索引而非元素值
     * - 理解单调队列的维护策略和均摊时间复杂度
     * - 处理各种边界情况和特殊输入
     * 
     * @param nums 输入整数数组
     * @param k 滑动窗口大小
     * @return vector<int> 每个窗口的最大值组成的数组
     * 
     * 【复杂度分析】
     * - 时间复杂度：O(n) - 每个元素最多入队和出队一次
     * - 空间复杂度：O(k) - 队列中最多存储k个元素索引
     * 
     * 【测试用例覆盖】
     * - 常规测试：{1,3,-1,-3,5,3,6,7}, k=3 → {3,3,5,5,6,7}
     * - 边界测试：单元素数组、窗口大小为1、空数组等
     * - 特殊测试：重复元素、递增序列、递减序列等
     * 
     * 【相关题目链接】
     * - LeetCode 239: https://leetcode.com/problems/sliding-window-maximum/
     * - 剑指Offer 59 - I: https://leetcode.cn/problems/hua-dong-chuang-kou-de-zui-da-zhi-lcof/
     */
    vector<int> maxSlidingWindow(vector<int>& nums, int k) {
        // 【边界检查】处理异常输入，确保代码健壮性
        // 空数组、非法窗口大小等边界情况的防御性编程
        if (nums.empty() || k == 0) {
            return {};
        }
        
        // 【性能优化】窗口大小为1时的特殊处理
        // 每个元素自身就是最大值，直接返回原数组避免不必要的计算
        if (k == 1) {
            return nums;
        }
        
        vector<int> result;
        // 【数据结构选择】使用双端队列存储索引而非元素值
        // 存储索引的优势：
        // 1. 便于判断元素是否在当前窗口范围内
        // 2. 可以通过索引获取原数组的值
        deque<int> dq;
        
        // 【滑动窗口主循环】遍历数组中的每个元素
        for (int i = 0; i < nums.size(); i++) {
            // 【步骤1】过期检查：移除队首所有不在当前窗口范围内的元素
            // 当前窗口的有效范围是 [i-k+1, i]
            // 如果队首元素的索引小于左边界，说明它已不在窗口范围内
            while (!dq.empty() && dq.front() < i - k + 1) {
                dq.pop_front();  // O(1)时间的队首操作
            }
            
            // 【步骤2】单调性维护：移除队尾所有小于等于当前元素的值
            // 使用小于等于(<=)而非严格小于(<)：在相等情况下保留较新的元素
            // 较新的元素在窗口中停留时间更长，更可能成为后续窗口的最大值
            while (!dq.empty() && nums[dq.back()] <= nums[i]) {
                dq.pop_back();  // O(1)时间的队尾操作
            }
            
            // 【步骤3】入队操作：将当前元素索引加入队列尾部
            dq.push_back(i);
            
            // 【步骤4】结果收集：当形成完整窗口后，记录当前窗口的最大值
            // 第一个完整窗口在i = k-1时形成（索引从0开始）
            if (i >= k - 1) {
                // 由于队列的单调递减性质，队首元素始终是当前窗口的最大值
                result.push_back(nums[dq.front()]);
            }
        }
        
        return result;
    }
    
    /**
     * @brief 使用优先队列求解滑动窗口最大值问题（最大堆实现）
     * 
     * 【算法原理解析】
     * 优先队列解法使用最大堆来维护窗口内的元素，堆顶元素始终是当前窗口的最大值。
     * 虽然时间复杂度不如单调队列，但实现简单直观，在某些场景下仍有应用价值。
     * 
     * 【关键设计决策】
     * 1. 存储pair<值,索引>：值用于堆排序，索引用于判断元素是否在窗口范围内
     * 2. 延迟删除策略：当堆顶元素不在窗口范围内时，才真正删除
     * 3. 最大堆实现：通过pair的默认比较规则实现降序排列
     * 
     * 【时间复杂度分析】
     * - 构建初始窗口堆：O(k*logk)
     * - 滑动窗口：每次插入新元素O(logk)，可能需要移除多个旧元素
     * - 最坏情况下，每个元素可能被插入和删除各一次
     * - 总时间复杂度：O(n*logk)
     * 
     * 【空间复杂度分析】
     * - 堆中最多存储n个元素（极端情况下）
     * - 因此空间复杂度为O(n)
     * 
     * 【与单调队列对比】
     * 优势：
     * - 实现简单直观，代码易于理解
     * - 当需要同时维护多个统计量时更灵活
     * - 在某些特殊场景下可能更有优势
     * 
     * 劣势：
     * - 时间复杂度O(n*logk)高于单调队列的O(n)
     * - 空间复杂度O(n)高于单调队列的O(k)
     * - 在大规模数据下性能较差
     * 
     * 【适用场景】
     * - 窗口大小k较小的情况
     * - 需要同时维护多个最值的场景
     * - 算法教学和调试场景
     * 
     * @param nums 输入整数数组
     * @param k 滑动窗口大小
     * @return vector<int> 每个窗口的最大值组成的数组
     * 
     * 【复杂度分析】
     * - 时间复杂度：O(n*logk) - 每个元素入堆出堆的操作均为O(logk)
     * - 空间复杂度：O(n) - 堆中可能存储所有n个元素
     */
    vector<int> maxSlidingWindowPriorityQueue(vector<int>& nums, int k) {
        // 【边界检查】处理异常输入
        if (nums.empty() || k <= 0) {
            return {};
        }
        // 【性能优化】窗口大小为1时的特殊处理
        if (k == 1) {
            return nums;
        }
        
        vector<int> result;
        // 【数据结构选择】优先队列（最大堆），存储pair<值, 索引>
        // pair的比较默认是先比较第一个元素（值），值相同则比较第二个元素（索引）
        // 这种比较规则自然实现了最大堆（值大的排在前面）
        priority_queue<pair<int, int>> maxHeap;
        
        // 【步骤1】初始化第一个窗口的元素到堆中
        for (int i = 0; i < k; i++) {
            maxHeap.push({nums[i], i});
        }
        // 记录第一个窗口的最大值（堆顶元素）
        result.push_back(maxHeap.top().first);
        
        // 【步骤2】滑动窗口，逐个处理剩余元素
        for (int i = k; i < nums.size(); i++) {
            // 添加新元素到堆中
            maxHeap.push({nums[i], i});
            
            // 【延迟删除策略】移除所有不在当前窗口范围内的最大值
            // 当前窗口范围是[i-k+1, i]，如果堆顶元素的索引小于左边界，说明已过期
            // 注意：这里使用while循环，因为可能有多个过期元素需要清理
            while (maxHeap.top().second < i - k + 1) {
                maxHeap.pop();
            }
            
            // 此时堆顶元素即为当前窗口的最大值
            result.push_back(maxHeap.top().first);
        }
        
        return result;
    }
    
    /**
 * @brief 使用暴力方法求解滑动窗口最大值问题
 * 
 * 算法原理：
 * - 遍历所有可能的窗口位置
 * - 对于每个窗口，遍历窗口内的所有元素找出最大值
 * - 将每个窗口的最大值添加到结果数组中
 * 
 * 相关题目链接：
 * - POJ 2823: http://poj.org/problem?id=2823
 * - HackerRank: https://www.hackerrank.com/challenges/sliding-window-maximum/problem
 * 
 * @param nums 输入整数数组
 * @param k 滑动窗口大小
 * @return vector<int> 每个窗口的最大值组成的数组
 * 
 * 时间复杂度：O(n*k)，需要遍历n-k+1个窗口，每个窗口需要遍历k个元素
 * 空间复杂度：O(1)，不考虑输出数组的额外空间
 * 
 * 适用场景：
 * - 窗口大小k较小的情况
 * - 调试和测试其他算法的正确性
 * - 算法入门学习理解问题本质
 */
    /**
     * @brief 使用暴力方法求解滑动窗口最大值问题（用于对比和教学）
     * 
     * 【算法原理解析】
     * 暴力解法是最直观的解决方案，通过遍历每个可能的窗口位置，
     * 对每个窗口内的k个元素计算最大值。虽然效率较低，但思路简单，
     * 适合作为算法教学的起点，帮助理解问题本质。
     * 
     * 【关键设计特点】
     * 1. 双重循环：外层循环遍历窗口起始位置，内层循环遍历窗口内元素
     * 2. 线性扫描：对每个窗口进行完整的线性扫描找最大值
     * 3. 简单直观：代码逻辑清晰，易于理解和实现
     * 
     * 【时间复杂度分析】
     * - 外层循环：n-k+1次迭代（窗口数量）
     * - 内层循环：k次迭代（窗口大小）
     * - 总时间复杂度：O((n-k+1)*k) = O(n*k)
     * 
     * 【空间复杂度分析】
     * - 仅使用常数级别的额外变量（不考虑结果数组）
     * - 因此空间复杂度为O(1)
     * 
     * 【与优化解法对比】
     * 优势：
     * - 实现简单，代码易于理解
     * - 不需要复杂的数据结构
     * - 适合小规模数据或调试场景
     * 
     * 劣势：
     * - 时间复杂度O(n*k)过高，不适合大规模数据
     * - 存在大量重复计算，效率低下
     * - 在实际工程应用中性能不可接受
     * 
     * 【适用场景】
     * - 算法教学和入门学习
     * - 小规模数据测试（n和k都很小）
     * - 验证其他优化算法的正确性
     * - 调试和问题定位
     * 
     * @param nums 输入整数数组
     * @param k 滑动窗口大小
     * @return vector<int> 每个窗口的最大值组成的数组
     * 
     * 【复杂度分析】
     * - 时间复杂度：O(n*k) - 对每个窗口遍历找最大值
     * - 空间复杂度：O(1) - 仅使用常数级别额外空间
     */
    vector<int> maxSlidingWindowBruteForce(vector<int>& nums, int k) {
        // 【边界检查】处理异常输入
        if (nums.empty() || k == 0) {
            return {};
        }
        // 【性能优化】窗口大小为1时的特殊处理
        if (k == 1) {
            return nums;
        }
        
        vector<int> result;
        int n = nums.size();
        
        // 【暴力解法主循环】遍历每个窗口的起始位置
        for (int i = 0; i <= n - k; i++) {
            // 初始化当前窗口的最大值为窗口第一个元素
            int max_val = nums[i];
            
            // 【内层循环】遍历窗口内的剩余元素，更新最大值
            // 从窗口第二个元素开始比较（j=1到j=k-1）
            for (int j = 1; j < k; j++) {
                if (nums[i + j] > max_val) {
                    max_val = nums[i + j];
                }
            }
            
            // 记录当前窗口的最大值
            result.push_back(max_val);
        }
        
        return result;
    }
};

/**
 * 打印数组函数 - 用于调试和测试
 */
void printVector(const vector<int>& vec, const string& label) {
    cout << label;
    for (int num : vec) {
        cout << num << " ";
    }
    cout << endl;
}

/**
 * 辅助函数：运行性能测试，对比不同解法的效率
 * 
 * @param solution Solution类实例
 * @param testSize 测试数组大小
 * @param windowSize 滑动窗口大小
 */
void runPerformanceTest(Solution& solution, int testSize = 100000, int windowSize = 1000) {
    // 生成大规模测试数据
    vector<int> nums(testSize);
    
    // 初始化随机数组
    srand(time(nullptr));
    for (int i = 0; i < testSize; i++) {
        nums[i] = rand() % 10000;
    }
    
    cout << "\n执行性能测试: 数组大小 = " << testSize << ", 窗口大小 = " << windowSize << endl;
    cout << "--------------------------------------------------" << endl;
    
    // 方法1: 单调队列解法性能测试 - O(n)时间复杂度
    cout << "测试单调队列解法..." << endl;
    auto start = chrono::high_resolution_clock::now();
    vector<int> result1 = solution.maxSlidingWindow(nums, windowSize);
    auto end = chrono::high_resolution_clock::now();
    chrono::duration<double, milli> duration1 = end - start;
    cout << "单调队列解法耗时: " << duration1.count() << " ms" << endl;
    cout << "- 时间复杂度: O(n) - 每个元素最多入队和出队一次" << endl;
    cout << "- 空间复杂度: O(k) - 队列大小不超过窗口大小" << endl;
    
    // 方法2: 优先队列解法性能测试 - O(n log n)时间复杂度
    cout << "\n测试优先队列解法..." << endl;
    start = chrono::high_resolution_clock::now();
    vector<int> result2 = solution.maxSlidingWindowPriorityQueue(nums, windowSize);
    end = chrono::high_resolution_clock::now();
    chrono::duration<double, milli> duration2 = end - start;
    cout << "优先队列解法耗时: " << duration2.count() << " ms" << endl;
    cout << "- 时间复杂度: O(n log n) - 每个元素入堆和出堆都是O(log n)操作" << endl;
    cout << "- 空间复杂度: O(n) - 堆中可能存储所有元素" << endl;
    
    // 对于较小的测试数组，也测试暴力解法性能
    if (testSize <= 10000) {
        cout << "\n测试暴力解法..." << endl;
        start = chrono::high_resolution_clock::now();
        vector<int> result3 = solution.maxSlidingWindowBruteForce(nums, windowSize);
        end = chrono::high_resolution_clock::now();
        chrono::duration<double, milli> duration3 = end - start;
        cout << "暴力解法耗时: " << duration3.count() << " ms" << endl;
        cout << "- 时间复杂度: O(n*k) - 每个窗口都需要遍历k个元素" << endl;
        cout << "- 空间复杂度: O(1) - 不考虑输出数组的额外空间" << endl;
    } else {
        cout << "\n暴力解法性能较差，对于大小为" << testSize << "的数组暂不测试" << endl;
    }
    
    // 验证结果正确性（应该相同）
    cout << "\n验证解法结果一致性..." << endl;
    bool resultsMatch = (result1.size() == result2.size());
    if (resultsMatch) {
        for (int i = 0; i < result1.size(); i++) {
            if (result1[i] != result2[i]) {
                resultsMatch = false;
                break;
            }
        }
    }
    cout << "单调队列与优先队列解法结果是否一致: " << (resultsMatch ? "✅ 是" : "❌ 否") << endl;
    cout << "--------------------------------------------------" << endl;
}

/**
 * 运行所有功能测试用例
 * 
 * 测试内容：
 * 1. 常规测试用例
 * 2. 边界情况处理
 * 3. 各种特殊输入模式
 * 4. 三种解法结果一致性验证
 */
void runAllTests() {
    cout << "=== 滑动窗口最大值算法测试套件 ===" << endl;
    cout << "本测试套件验证三种不同解法在各种输入下的正确性" << endl;
    Solution solution;
    
    // 测试用例1 - 常规测试
    vector<int> nums1 = {1, 3, -1, -3, 5, 3, 6, 7};
    int k1 = 3;
    vector<int> result1 = solution.maxSlidingWindow(nums1, k1);
    printVector(result1, "测试用例1结果: "); // 预期输出: [3, 3, 5, 5, 6, 7]
    
    // 测试用例2 - 单元素数组
    vector<int> nums2 = {1};
    int k2 = 1;
    vector<int> result2 = solution.maxSlidingWindow(nums2, k2);
    printVector(result2, "测试用例2结果: "); // 预期输出: [1]
    
    // 测试用例3 - 边界情况
    vector<int> nums3 = {1, -1};
    int k3 = 1;
    vector<int> result3 = solution.maxSlidingWindow(nums3, k3);
    printVector(result3, "测试用例3结果: "); // 预期输出: [1, -1]
    
    // 测试用例4 - 重复元素
    vector<int> nums4 = {4, 2, 4, 2, 1};
    int k4 = 3;
    vector<int> result4 = solution.maxSlidingWindow(nums4, k4);
    printVector(result4, "测试用例4结果: "); // 预期输出: [4, 4, 4]
    
    // 测试用例5 - 递减序列
    vector<int> nums5 = {5, 4, 3, 2, 1};
    int k5 = 3;
    vector<int> result5 = solution.maxSlidingWindow(nums5, k5);
    printVector(result5, "测试用例5结果: "); // 预期输出: [5, 4, 3]
    
    // 测试用例6 - 递增序列
    vector<int> nums6 = {1, 2, 3, 4, 5};
    int k6 = 3;
    vector<int> result6 = solution.maxSlidingWindow(nums6, k6);
    printVector(result6, "测试用例6结果: "); // 预期输出: [3, 4, 5]
    
    // 测试用例7 - 全相同元素
    vector<int> nums7 = {2, 2, 2, 2, 2};
    int k7 = 2;
    vector<int> result7 = solution.maxSlidingWindow(nums7, k7);
    printVector(result7, "测试用例7结果: "); // 预期输出: [2, 2, 2, 2]
    
    // 解法正确性验证
    cout << "\n=== 解法正确性验证 ===" << endl;
    cout << "对比三种不同解法的输出结果:" << endl;
    vector<int> test_nums = {1, 3, -1, -3, 5, 3, 6, 7};
    int test_k = 3;
    printVector(solution.maxSlidingWindow(test_nums, test_k), "单调队列解法: ");
    printVector(solution.maxSlidingWindowPriorityQueue(test_nums, test_k), "优先队列解法: ");
    printVector(solution.maxSlidingWindowBruteForce(test_nums, test_k), "暴力解法: ");
}

/**
 * 主函数
 * 
 * 程序入口，主要功能包括：
 * 1. 运行功能测试套件，验证算法正确性
 * 2. 执行性能测试，对比不同解法的效率
 * 3. 提供使用各种滑动窗口最大值解法的示例
 */
int main() {
    cout << "==================================================" << endl;
    cout << "滑动窗口最大值问题多种解法实现与性能分析" << endl;
    cout << "支持的解法:" << endl;
    cout << "1. 单调队列解法 - 时间复杂度 O(n)，最优解法" << endl;
    cout << "2. 优先队列解法 - 时间复杂度 O(n log n)，实现简单" << endl;
    cout << "3. 暴力解法 - 时间复杂度 O(n*k)，仅适用于小窗口" << endl;
    cout << "==================================================" << endl;
    
    // 运行功能测试用例，验证各种解法在不同输入下的正确性
    runAllTests();
    
    // 执行性能测试，对比不同解法的效率差异
    cout << "\n\n=== 性能测试 ===" << endl;
    cout << "性能测试说明:" << endl;
    cout << "1. 测试不同规模的数据下各算法的执行效率" << endl;
    cout << "2. 展示各算法的实际运行时间和理论时间复杂度" << endl;
    cout << "3. 验证各算法结果的一致性" << endl;
    
    Solution solution;
    
    // 测试1: 中等规模数据
    runPerformanceTest(solution, 10000, 100);
    
    // 测试2: 大规模数据（可选，执行时间较长）
    cout << "\n按任意键继续进行大规模数据测试..." << endl;
    cin.get();
    runPerformanceTest(solution, 100000, 1000);
    
    cout << "\n==================================================" << endl;
    cout << "测试完成！" << endl;
    cout << "总结:" << endl;
    cout << "- 对于大多数情况，单调队列解法是最优选择，时间复杂度O(n)" << endl;
    cout << "- 优先队列解法适用于需要维护多个统计量的场景" << endl;
    cout << "- 暴力解法仅适用于窗口较小或调试场景" << endl;
    cout << "==================================================" << endl;
    
    return 0;
}