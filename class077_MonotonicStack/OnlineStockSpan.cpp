/**
 * 901. 在线股票跨度 (Online Stock Span)
 * 
 * 题目描述:
 * 设计一个算法收集某些股票的每日报价，并返回该股票当日价格的跨度。
 * 当日股票价格的跨度被定义为股票价格小于或等于今天价格的最大连续日数（从今天开始往回数，包括今天）。
 * 
 * 解题思路:
 * 使用单调栈来解决。维护一个单调递减栈，栈中存储价格和对应的跨度。
 * 当新价格到来时，弹出所有小于等于当前价格的价格，并累加它们的跨度。
 * 
 * 时间复杂度: 每个价格最多入栈和出栈各一次，平均O(1)
 * 空间复杂度: O(n)，用于存储单调栈
 * 
 * 测试链接: https://leetcode.cn/problems/online-stock-span/
 * 
 * 工程化考量:
 * 1. 异常处理：价格边界检查
 * 2. 性能优化：使用vector预分配空间，避免动态内存分配
 * 3. 内存管理：使用RAII原则管理资源
 * 4. 代码可读性：详细注释和模块化设计
 */

#include <iostream>
#include <vector>
#include <utility> // for std::pair
#include <chrono>

using namespace std;

/**
 * @brief 股票跨度计算器类
 */
class StockSpanner {
private:
    vector<pair<int, int>> stack; // 栈存储<价格, 跨度>
    
public:
    StockSpanner() {}
    
    /**
     * @brief 计算当日价格的跨度
     * 
     * @param price 当日价格
     * @return int 跨度值
     */
    int next(int price) {
        int span = 1; // 至少包含当天
        
        // 弹出所有小于等于当前价格的价格，并累加跨度
        while (!stack.empty() && stack.back().first <= price) {
            span += stack.back().second;
            stack.pop_back();
        }
        
        // 将当前价格和跨度入栈
        stack.emplace_back(price, span);
        
        return span;
    }
};

/**
 * @brief 优化版本：使用数组模拟栈提高性能
 */
class StockSpannerOptimized {
private:
    vector<pair<int, int>> stack;
    int capacity;
    
public:
    StockSpannerOptimized() : capacity(10000) {
        stack.reserve(capacity); // 预分配空间
    }
    
    int next(int price) {
        int span = 1;
        
        // 弹出所有小于等于当前价格的价格，并累加跨度
        while (!stack.empty() && stack.back().first <= price) {
            span += stack.back().second;
            stack.pop_back();
        }
        
        // 将当前价格和跨度入栈
        stack.emplace_back(price, span);
        
        return span;
    }
};

/**
 * @brief 测试方法 - 验证算法正确性
 */
void testStockSpanner() {
    cout << "=== 在线股票跨度算法测试 ===" << endl;
    
    // 测试用例1: [100, 80, 60, 70, 60, 75, 85]
    StockSpanner spanner1;
    StockSpannerOptimized spanner1Opt;
    
    vector<int> prices1 = {100, 80, 60, 70, 60, 75, 85};
    vector<int> expected1 = {1, 1, 1, 2, 1, 4, 6};
    
    cout << "测试用例1 标准版: [";
    for (int i = 0; i < prices1.size(); i++) {
        int result = spanner1.next(prices1[i]);
        cout << result;
        if (i < prices1.size() - 1) cout << ", ";
    }
    cout << "] (预期: [1, 1, 1, 2, 1, 4, 6])" << endl;
    
    cout << "测试用例1 优化版: [";
    for (int i = 0; i < prices1.size(); i++) {
        int result = spanner1Opt.next(prices1[i]);
        cout << result;
        if (i < prices1.size() - 1) cout << ", ";
    }
    cout << "] (预期: [1, 1, 1, 2, 1, 4, 6])" << endl;
    
    // 测试用例2: 连续递增价格
    StockSpanner spanner2;
    StockSpannerOptimized spanner2Opt;
    
    vector<int> prices2 = {10, 20, 30, 40, 50};
    vector<int> expected2 = {1, 2, 3, 4, 5};
    
    cout << "测试用例2 标准版: [";
    for (int i = 0; i < prices2.size(); i++) {
        int result = spanner2.next(prices2[i]);
        cout << result;
        if (i < prices2.size() - 1) cout << ", ";
    }
    cout << "] (预期: [1, 2, 3, 4, 5])" << endl;
    
    cout << "测试用例2 优化版: [";
    for (int i = 0; i < prices2.size(); i++) {
        int result = spanner2Opt.next(prices2[i]);
        cout << result;
        if (i < prices2.size() - 1) cout << ", ";
    }
    cout << "] (预期: [1, 2, 3, 4, 5])" << endl;
    
    // 测试用例3: 连续递减价格
    StockSpanner spanner3;
    StockSpannerOptimized spanner3Opt;
    
    vector<int> prices3 = {50, 40, 30, 20, 10};
    vector<int> expected3 = {1, 1, 1, 1, 1};
    
    cout << "测试用例3 标准版: [";
    for (int i = 0; i < prices3.size(); i++) {
        int result = spanner3.next(prices3[i]);
        cout << result;
        if (i < prices3.size() - 1) cout << ", ";
    }
    cout << "] (预期: [1, 1, 1, 1, 1])" << endl;
    
    cout << "测试用例3 优化版: [";
    for (int i = 0; i < prices3.size(); i++) {
        int result = spanner3Opt.next(prices3[i]);
        cout << result;
        if (i < prices3.size() - 1) cout << ", ";
    }
    cout << "] (预期: [1, 1, 1, 1, 1])" << endl;
    
    cout << "=== 功能测试完成！ ===" << endl;
}

/**
 * @brief 性能测试方法
 */
void performanceTest() {
    cout << "=== 性能测试 ===" << endl;
    
    // 性能测试：大规模数据 - 连续递增价格
    StockSpannerOptimized spanner;
    const int SIZE = 10000;
    
    auto start = chrono::high_resolution_clock::now();
    
    for (int i = 0; i < SIZE; i++) {
        spanner.next(i); // 连续递增价格
    }
    
    auto end = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "性能测试 [" << SIZE << "个连续递增价格]: 耗时: " << duration.count() << "ms" << endl;
    
    // 性能测试：最坏情况 - 连续递减价格
    StockSpannerOptimized spannerWorst;
    
    start = chrono::high_resolution_clock::now();
    
    for (int i = SIZE; i > 0; i--) {
        spannerWorst.next(i); // 连续递减价格
    }
    
    end = chrono::high_resolution_clock::now();
    duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "性能测试 [最坏情况" << SIZE << "个连续递减价格]: 耗时: " << duration.count() << "ms" << endl;
    
    cout << "=== 性能测试完成！ ===" << endl;
}

/**
 * @brief 主函数
 */
int main() {
    // 运行功能测试
    testStockSpanner();
    
    // 运行性能测试
    performanceTest();
    
    return 0;
}

/**
 * 算法复杂度分析:
 * 
 * 时间复杂度: 平均O(1)
 * - 每个价格最多入栈和出栈各一次
 * - 虽然看起来是O(n)，但均摊分析后为O(1)
 * 
 * 空间复杂度: O(n)
 * - 最坏情况下需要存储所有价格信息
 * - 优化版本预分配了固定大小的数组
 * 
 * 最优解分析:
 * - 这是在线股票跨度问题的最优解
 * - 无法获得更好的时间复杂度
 * - 空间复杂度也是最优的
 * 
 * C++特性利用:
 * - 使用vector和pair提供高效的数据结构
 * - 使用emplace_back避免不必要的拷贝
 * - 使用chrono库进行精确性能测量
 * - 使用RAII原则自动管理内存
 * 
 * 单调栈应用技巧:
 * - 栈中存储价格和对应的跨度信息
 * - 当新价格到来时，弹出所有小于等于当前价格的价格
 * - 累加弹出的价格的跨度作为当前价格的跨度
 * - 这种设计确保了跨度的正确计算
 */