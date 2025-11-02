#include <iostream>
#include <vector>
#include <queue>
#include <unordered_map>
#include <algorithm>

/**
 * LeetCode 621. 任务调度器
 * 题目链接：https://leetcode.cn/problems/task-scheduler/
 * 难度：中等
 * 
 * 问题描述：
 * 给你一个用字符数组 tasks 表示的 CPU 需要执行的任务列表。其中每个字母表示一种不同种类的任务。任务可以以任意顺序执行，并且每个任务都可以在 1 个单位时间内执行完。在任何一个单位时间，CPU 可以完成一个任务，或者处于待命状态。
 * 然而，两个相同种类的任务之间必须有长度为整数 n 的冷却时间，因此至少有连续 n 个单位时间内 CPU 在执行不同的任务，或者在待命状态。
 * 你需要计算完成所有任务所需要的最短时间。
 * 
 * 示例：
 * 输入：tasks = ["A","A","A","B","B","B"], n = 2
 * 输出：8
 * 解释：A -> B -> (待命) -> A -> B -> (待命) -> A -> B
 *      在本示例中，两个相同类型任务之间必须间隔长度为 n = 2 的冷却时间，而执行一个任务只需要一个单位时间，所以中间出现了（待命）状态。 
 * 
 * 解题思路：
 * 贪心算法 + 优先队列（最大堆）
 * 1. 统计每个任务的频率
 * 2. 使用最大堆存储任务频率，确保每次优先处理频率最高的任务
 * 3. 维护一个时间计数器，在每个时间单位：
 *    a. 从堆中取出最多n+1个任务（确保同类型任务间隔n个时间单位）
 *    b. 将取出的任务频率减1，如果还有剩余频率则暂时保存
 *    c. 计算该时间片实际消耗的时间（如果有任务执行，消耗的时间等于取出的任务数；否则消耗1个时间单位）
 *    d. 将还有剩余频率的任务重新放回堆中
 * 4. 重复步骤3直到堆为空
 * 
 * 时间复杂度分析：
 * - 统计频率：O(n)
 * - 堆操作：O(m log k)，其中m是任务总数，k是不同任务的数量
 * 总体时间复杂度：O(n + m log k)
 * 
 * 空间复杂度分析：
 * - 统计频率的哈希表：O(k)
 * - 最大堆：O(k)
 * 总体空间复杂度：O(k)
 * 
 * 最优性证明：
 * 贪心策略确保每次处理剩余频率最高的任务，这样可以最小化空闲时间。通过优先处理高频任务，可以最大程度地填充任务之间的冷却时间，避免不必要的等待。
 */

class TaskScheduler {
private:
    /**
     * 基于优先队列的解法
     */
    int leastIntervalHeap(const std::vector<char>& tasks, int n) {
        // 特殊情况处理
        if (tasks.empty()) {
            return 0;
        }
        
        if (n == 0) {
            return tasks.size();  // 没有冷却时间，直接返回任务数量
        }
        
        // 统计每个任务的频率
        std::unordered_map<char, int> taskCounts;
        for (char task : tasks) {
            taskCounts[task]++;
        }
        
        // 使用最大堆存储任务频率
        std::priority_queue<int> maxHeap;
        for (const auto& pair : taskCounts) {
            maxHeap.push(pair.second);
        }
        
        int time = 0;  // 总时间计数器
        
        // 当堆不为空时继续处理
        while (!maxHeap.empty()) {
            int currentTimeSlot = 0;  // 当前时间片中处理的任务数
            std::vector<int> temp;  // 临时保存本时间片中处理过的任务频率
            
            // 尝试在当前时间片（n+1个连续时间单位）中处理尽可能多的任务
            while (currentTimeSlot <= n && !maxHeap.empty()) {
                int count = maxHeap.top();  // 取出频率最高的任务
                maxHeap.pop();
                
                count--;  // 执行一次该任务，频率减1
                
                if (count > 0) {  // 如果任务还有剩余次数，保存到临时列表
                    temp.push_back(count);
                }
                
                currentTimeSlot++;  // 当前时间片处理的任务数加1
            }
            
            // 将剩余任务放回堆中
            for (int count : temp) {
                maxHeap.push(count);
            }
            
            // 计算本次时间片消耗的时间：
            // 如果堆不为空，说明还有任务需要处理，本次时间片消耗n+1个时间单位
            // 如果堆为空，说明所有任务都处理完了，本次时间片只消耗实际处理的任务数
            if (!maxHeap.empty()) {
                time += (n + 1);
            } else {
                time += currentTimeSlot;
            }
        }
        
        return time;
    }
    
    /**
     * 优化解法：数学公式推导
     */
    int leastIntervalOptimal(const std::vector<char>& tasks, int n) {
        // 特殊情况处理
        if (tasks.empty()) {
            return 0;
        }
        
        if (n == 0) {
            return tasks.size();  // 没有冷却时间，直接返回任务数量
        }
        
        // 统计每个任务的频率
        int counts[26] = {0};  // 假设任务只有26个大写字母
        int maxFreq = 0;  // 最高频率
        int maxFreqCount = 0;  // 具有最高频率的任务数量
        
        for (char task : tasks) {
            counts[task - 'A']++;
            maxFreq = std::max(maxFreq, counts[task - 'A']);
        }
        
        // 统计有多少个任务具有最高频率
        for (int count : counts) {
            if (count == maxFreq) {
                maxFreqCount++;
            }
        }
        
        // 计算最小时间：由最高频率任务决定的最小时间
        int minTime = (maxFreq - 1) * (n + 1) + maxFreqCount;
        
        // 最终结果取任务总数和最小时间的较大值
        return std::max(minTime, static_cast<int>(tasks.size()));
    }
    
public:
    /**
     * 计算完成所有任务所需的最短时间
     * @param tasks 任务数组
     * @param n 冷却时间
     * @param useOptimal 是否使用优化解法
     * @return 最短时间
     */
    int leastInterval(const std::vector<char>& tasks, int n, bool useOptimal = true) {
        if (useOptimal) {
            return leastIntervalOptimal(tasks, n);
        } else {
            return leastIntervalHeap(tasks, n);
        }
    }
};

/**
 * 辅助函数：打印向量内容
 */
void printVector(const std::vector<char>& vec) {
    std::cout << "[";
    for (size_t i = 0; i < vec.size(); i++) {
        std::cout << vec[i];
        if (i < vec.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]" << std::endl;
}

/**
 * 主函数，用于测试
 */
int main() {
    TaskScheduler scheduler;
    
    // 测试用例1：基本情况
    std::vector<char> tasks1 = {'A', 'A', 'A', 'B', 'B', 'B'};
    int n1 = 2;
    std::cout << "测试用例1：任务 = ";
    printVector(tasks1);
    std::cout << "冷却时间 n = " << n1 << std::endl;
    
    int result1 = scheduler.leastInterval(tasks1, n1, false);
    int result1Optimal = scheduler.leastInterval(tasks1, n1, true);
    std::cout << "普通解法结果：" << result1 << "，期望：8" << std::endl;
    std::cout << "优化解法结果：" << result1Optimal << "，期望：8" << std::endl;
    std::cout << "解法结果是否一致：" << (result1 == result1Optimal ? "是" : "否") << std::endl;
    std::cout << std::endl;
    
    // 测试用例2：没有冷却时间
    std::vector<char> tasks2 = {'A', 'A', 'A', 'B', 'B', 'B'};
    int n2 = 0;
    std::cout << "测试用例2：任务 = ";
    printVector(tasks2);
    std::cout << "冷却时间 n = " << n2 << std::endl;
    
    int result2 = scheduler.leastInterval(tasks2, n2, false);
    int result2Optimal = scheduler.leastInterval(tasks2, n2, true);
    std::cout << "普通解法结果：" << result2 << "，期望：6" << std::endl;
    std::cout << "优化解法结果：" << result2Optimal << "，期望：6" << std::endl;
    std::cout << "解法结果是否一致：" << (result2 == result2Optimal ? "是" : "否") << std::endl;
    std::cout << std::endl;
    
    // 测试用例3：只有一种任务
    std::vector<char> tasks3 = {'A', 'A', 'A', 'A', 'A', 'A'};
    int n3 = 2;
    std::cout << "测试用例3：任务 = ";
    printVector(tasks3);
    std::cout << "冷却时间 n = " << n3 << std::endl;
    
    int result3 = scheduler.leastInterval(tasks3, n3, false);
    int result3Optimal = scheduler.leastInterval(tasks3, n3, true);
    std::cout << "普通解法结果：" << result3 << "，期望：16" << std::endl;
    std::cout << "优化解法结果：" << result3Optimal << "，期望：16" << std::endl;
    std::cout << "解法结果是否一致：" << (result3 == result3Optimal ? "是" : "否") << std::endl;
    std::cout << std::endl;
    
    // 测试用例4：任务种类足够多，无需等待
    std::vector<char> tasks4 = {'A', 'A', 'A', 'B', 'B', 'B', 'C', 'C', 'D', 'D'};
    int n4 = 2;
    std::cout << "测试用例4：任务 = ";
    printVector(tasks4);
    std::cout << "冷却时间 n = " << n4 << std::endl;
    
    int result4 = scheduler.leastInterval(tasks4, n4, false);
    int result4Optimal = scheduler.leastInterval(tasks4, n4, true);
    std::cout << "普通解法结果：" << result4 << "，期望：10" << std::endl;
    std::cout << "优化解法结果：" << result4Optimal << "，期望：10" << std::endl;
    std::cout << "解法结果是否一致：" << (result4 == result4Optimal ? "是" : "否") << std::endl;
    std::cout << std::endl;
    
    // 测试用例5：边界情况 - 空数组
    std::vector<char> tasks5 = {};
    int n5 = 2;
    std::cout << "测试用例5：任务 = []" << std::endl;
    std::cout << "冷却时间 n = " << n5 << std::endl;
    
    int result5 = scheduler.leastInterval(tasks5, n5, false);
    int result5Optimal = scheduler.leastInterval(tasks5, n5, true);
    std::cout << "普通解法结果：" << result5 << "，期望：0" << std::endl;
    std::cout << "优化解法结果：" << result5Optimal << "，期望：0" << std::endl;
    std::cout << "解法结果是否一致：" << (result5 == result5Optimal ? "是" : "否") << std::endl;
    
    // 性能测试
    std::cout << "\n性能测试：" << std::endl;
    std::vector<char> largeTasks(10000);
    for (int i = 0; i < largeTasks.size(); i++) {
        largeTasks[i] = 'A' + (i % 10);  // 创建10种不同的任务
    }
    int largeN = 5;
    
    // 测量普通解法的性能
    auto start = std::chrono::high_resolution_clock::now();
    int largeResult = scheduler.leastInterval(largeTasks, largeN, false);
    auto end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double, std::milli> heapTime = end - start;
    std::cout << "大规模任务测试（普通解法）结果：" << largeResult 
              << "，耗时：" << heapTime.count() << "ms" << std::endl;
    
    // 测量优化解法的性能
    start = std::chrono::high_resolution_clock::now();
    int largeResultOptimal = scheduler.leastInterval(largeTasks, largeN, true);
    end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double, std::milli> optimalTime = end - start;
    std::cout << "大规模任务测试（优化解法）结果：" << largeResultOptimal 
              << "，耗时：" << optimalTime.count() << "ms" << std::endl;
    
    // 性能提升倍数
    double speedup = heapTime.count() / optimalTime.count();
    std::cout << "优化解法比普通解法快约 " << speedup << " 倍" << std::endl;
    
    return 0;
}

/*
工程化考量：

1. 代码组织与封装：
   - 使用类封装相关功能，提高代码的可复用性
   - 将两种不同的解法封装为私有方法，通过公共接口暴露
   - 提供参数选择使用哪种解法，便于测试和比较

2. 内存管理：
   - 在C++中，优先队列和向量等容器会自动管理内存
   - 对于大规模数据，确保使用高效的内存分配策略

3. 异常处理：
   - 添加了空输入的边界检查
   - 处理了特殊情况如冷却时间为0的情况
   - 在实际应用中可以添加更多的异常处理机制

4. 性能优化：
   - 提供了两种解法，优化解法在时间和空间复杂度上都优于普通解法
   - 普通解法的时间复杂度为O(m log k)，空间复杂度为O(k)
   - 优化解法的时间复杂度为O(m)，空间复杂度为O(1)，使用固定大小的数组而非哈希表

5. 代码可读性：
   - 使用清晰的变量和方法命名
   - 添加了详细的注释解释算法思路和关键步骤
   - 提供辅助函数打印调试信息

6. 测试与验证：
   - 实现了全面的测试用例，包括基本情况、边界情况和特殊情况
   - 进行了性能测试，验证优化解法的效果
   - 比较两种解法的结果确保一致性

7. C++特性应用：
   - 使用标准库容器如vector、priority_queue、unordered_map
   - 使用auto关键字简化类型声明
   - 使用std::chrono进行精确的性能测量
   - 使用static_cast进行类型转换

8. 扩展性考虑：
   - 可以轻松扩展支持更多类型的任务
   - 可以添加任务优先级的处理
   - 可以扩展支持动态任务调度

9. 线程安全：
   - 在多线程环境中需要添加互斥锁保证线程安全
   - 可以考虑使用无锁数据结构提高并发性能

10. 算法调试技巧：
    - 在关键步骤添加打印语句监控算法执行过程
    - 使用调试器设置断点观察变量状态
    - 使用小数据集手动验证算法正确性
*/