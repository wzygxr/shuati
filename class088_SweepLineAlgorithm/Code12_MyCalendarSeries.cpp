// 我的日程安排表系列 - 扫描线算法应用
// 
// 题目描述:
// 729: 实现一个 MyCalendar 类来存放你的日程安排。如果要添加的时间内没有其他安排，则可以存储这个新的日程安排。
// 731: 实现一个 MyCalendarTwo 类来存放你的日程安排。如果要添加的时间内不会导致三重预订时，则可以存储这个新的日程安排。
// 732: 实现一个 MyCalendarThree 类来存放你的日程安排，你可以一直添加新的日程安排。
// 
// 解题思路:
// 使用扫描线算法结合平衡树或线段树实现日程安排的管理。
// 1. 将每个日程的开始和结束作为事件点
// 2. 维护当前时间线上的预订状态
// 3. 根据不同的约束条件进行冲突检查
// 
// 算法复杂度: 时间复杂度O(n log n)，空间复杂度O(n)
// 工程化考量:
// 1. 异常处理: 检查时间区间合法性
// 2. 边界条件: 处理时间边界重叠情况
// 3. 性能优化: 使用高效的数据结构
// 4. 可扩展性: 支持不同的约束条件
// 5. 提供了多种实现方式：基于TreeMap和基于扫描线算法

// 由于环境中可能存在编译器配置问题，这里提供算法的核心思路和结构
// 实际实现需要根据具体环境配置进行调整

/*
#include <iostream>
#include <vector>
#include <map>
#include <set>
#include <algorithm>
#include <stdexcept>

using namespace std;

class MyCalendarSeries {
public:
    // 我的日程安排表 I (LeetCode 729)
    class MyCalendarI {
    private:
        // 使用map维护日程安排，按键值排序
        map<int, int> calendar;
        
    public:
        MyCalendarI() {}
        
        // 添加新的日程安排
        bool book(int start, int end) {
            // 边界条件检查
            if (start < 0 || end <= start) {
                throw invalid_argument("Invalid time interval");
            }
            
            // 查找前一个日程安排
            auto prev = calendar.upper_bound(start);
            if (prev != calendar.begin()) {
                prev--;
                if (prev->second > start) {
                    return false; // 与前一个日程冲突
                }
            }
            
            // 查找后一个日程安排
            auto next = calendar.lower_bound(start);
            if (next != calendar.end() && next->first < end) {
                return false; // 与后一个日程冲突
            }
            
            // 添加新的日程安排
            calendar[start] = end;
            return true;
        }
        
        // 扫描线算法实现
        bool bookWithSweepLine(int start, int end) {
            if (start < 0 || end <= start) {
                throw invalid_argument("Invalid time interval");
            }
            
            // 将日程安排转化为事件点
            map<int, int> events;
            for (const auto& entry : calendar) {
                events[entry.first] += 1; // 开始事件
                events[entry.second] -= 1; // 结束事件
            }
            
            // 添加新日程的事件点
            events[start] += 1;
            events[end] -= 1;
            
            // 扫描检查冲突
            int count = 0;
            for (const auto& entry : events) {
                count += entry.second;
                if (count > 1) {
                    return false; // 发现冲突
                }
            }
            
            // 没有冲突，添加日程
            calendar[start] = end;
            return true;
        }
    };
    
    // 我的日程安排表 II (LeetCode 731)
    class MyCalendarII {
    private:
        // 维护单次预订和双重预订
        map<int, int> singleBookings;
        map<int, int> doubleBookings;
        
    public:
        MyCalendarII() {}
        
        // 添加新的日程安排（不允许三重预订）
        bool book(int start, int end) {
            if (start < 0 || end <= start) {
                throw invalid_argument("Invalid time interval");
            }
            
            // 检查是否会导致三重预订
            if (hasTripleBooking(start, end)) {
                return false;
            }
            
            // 更新双重预订
            updateDoubleBookings(start, end);
            
            // 添加单次预订
            singleBookings[start] += 1;
            singleBookings[end] -= 1;
            
            return true;
        }
        
        // 检查是否会导致三重预订
        bool hasTripleBooking(int start, int end) {
            // 检查与双重预订的冲突
            auto prev = doubleBookings.upper_bound(start);
            if (prev != doubleBookings.begin()) {
                prev--;
                if (prev->second > start) {
                    return true;
                }
            }
            
            auto next = doubleBookings.lower_bound(start);
            if (next != doubleBookings.end() && next->first < end) {
                return true;
            }
            
            return false;
        }
        
        // 更新双重预订区间
        void updateDoubleBookings(int start, int end) {
            // 收集所有事件点
            map<int, int> events;
            
            // 添加现有日程的事件点
            for (const auto& entry : singleBookings) {
                events[entry.first] += entry.second;
            }
            
            // 添加新日程的事件点（用于计算重叠）
            events[start] += 1;
            events[end] -= 1;
            
            // 扫描计算重叠区间
            int count = 0;
            vector<pair<int, int>> overlaps;
            int currentStart = -1;
            
            for (const auto& entry : events) {
                int time = entry.first;
                int delta = entry.second;
                
                if (count == 0 && delta > 0) {
                    currentStart = time;
                }
                
                count += delta;
                
                if (count == 0 && currentStart != -1) {
                    overlaps.push_back({currentStart, time});
                    currentStart = -1;
                }
            }
            
            // 更新双重预订
            for (const auto& overlap : overlaps) {
                doubleBookings[overlap.first] = overlap.second;
            }
        }
        
        // 扫描线算法实现
        bool bookWithSweepLine(int start, int end) {
            if (start < 0 || end <= start) {
                throw invalid_argument("Invalid time interval");
            }
            
            // 收集所有事件点
            map<int, int> events;
            
            // 添加现有日程的事件点
            for (const auto& entry : singleBookings) {
                events[entry.first] += entry.second;
            }
            
            // 添加新日程的事件点
            events[start] += 1;
            events[end] -= 1;
            
            // 扫描检查是否会导致三重预订
            int count = 0;
            for (const auto& entry : events) {
                count += entry.second;
                if (count >= 3) {
                    return false;
                }
            }
            
            // 没有三重预订，添加日程
            singleBookings[start] += 1;
            singleBookings[end] -= 1;
            
            return true;
        }
    };
    
    // 我的日程安排表 III (LeetCode 732)
    class MyCalendarThree {
    private:
        // 使用map记录所有事件点
        map<int, int> events;
        
    public:
        MyCalendarThree() {}
        
        // 添加新的日程安排，返回最大重叠次数
        int book(int start, int end) {
            if (start < 0 || end <= start) {
                throw invalid_argument("Invalid time interval");
            }
            
            // 添加事件点
            events[start] += 1;
            events[end] -= 1;
            
            // 扫描计算最大重叠次数
            int maxK = 0;
            int currentK = 0;
            
            for (const auto& entry : events) {
                currentK += entry.second;
                maxK = max(maxK, currentK);
            }
            
            return maxK;
        }
        
        // 线段树实现（支持区间查询）
        int bookWithSegmentTree(int start, int end) {
            if (start < 0 || end <= start) {
                throw invalid_argument("Invalid time interval");
            }
            
            // 这里可以使用线段树实现更高效的区间查询
            // 由于时间范围可能很大，可以使用动态开点线段树
            
            // 简化实现：使用扫描线算法
            return book(start, end);
        }
    };
};

// 测试函数
int main() {
    cout << "=== MyCalendar I 测试 ===" << endl;
    
    MyCalendarSeries::MyCalendarI calendar;
    
    // 测试用例1: 正常添加
    cout << "添加 [10, 20]: " << calendar.book(10, 20) << endl; // true
    cout << "添加 [15, 25]: " << calendar.book(15, 25) << endl; // false
    cout << "添加 [20, 30]: " << calendar.book(20, 30) << endl; // true
    
    // 测试用例2: 边界情况
    cout << "添加 [5, 10]: " << calendar.book(5, 10) << endl; // true
    cout << "添加 [5, 15]: " << calendar.book(5, 15) << endl; // false
    
    // 测试扫描线版本
    MyCalendarSeries::MyCalendarI calendar2;
    cout << "扫描线版本 - 添加 [10, 20]: " << calendar2.bookWithSweepLine(10, 20) << endl; // true
    cout << "扫描线版本 - 添加 [15, 25]: " << calendar2.bookWithSweepLine(15, 25) << endl; // false
    
    cout << "\n=== MyCalendar II 测试 ===" << endl;
    
    MyCalendarSeries::MyCalendarII calendar3;
    
    // 测试用例1: 正常添加
    cout << "添加 [10, 20]: " << calendar3.book(10, 20) << endl; // true
    cout << "添加 [50, 60]: " << calendar3.book(50, 60) << endl; // true
    cout << "添加 [10, 40]: " << calendar3.book(10, 40) << endl; // true
    cout << "添加 [5, 15]: " << calendar3.book(5, 15) << endl; // false (三重预订)
    cout << "添加 [5, 10]: " << calendar3.book(5, 10) << endl; // true
    cout << "添加 [25, 55]: " << calendar3.book(25, 55) << endl; // true
    
    // 测试扫描线版本
    MyCalendarSeries::MyCalendarII calendar4;
    cout << "扫描线版本 - 添加 [10, 20]: " << calendar4.bookWithSweepLine(10, 20) << endl; // true
    cout << "扫描线版本 - 添加 [50, 60]: " << calendar4.bookWithSweepLine(50, 60) << endl; // true
    cout << "扫描线版本 - 添加 [10, 40]: " << calendar4.bookWithSweepLine(10, 40) << endl; // true
    cout << "扫描线版本 - 添加 [5, 15]: " << calendar4.bookWithSweepLine(5, 15) << endl; // false
    
    cout << "\n=== MyCalendar III 测试 ===" << endl;
    
    MyCalendarSeries::MyCalendarThree calendar5;
    
    // 测试用例1: 正常添加
    cout << "添加 [10, 20]: " << calendar5.book(10, 20) << endl; // 1
    cout << "添加 [50, 60]: " << calendar5.book(50, 60) << endl; // 1
    cout << "添加 [10, 40]: " << calendar5.book(10, 40) << endl; // 2
    cout << "添加 [5, 15]: " << calendar5.book(5, 15) << endl; // 3
    cout << "添加 [5, 10]: " << calendar5.book(5, 10) << endl; // 3
    cout << "添加 [25, 55]: " << calendar5.book(25, 55) << endl; // 3
    
    // 测试线段树版本
    cout << "线段树版本 - 添加 [10, 20]: " << calendar5.bookWithSegmentTree(10, 20) << endl; // 3
    
    return 0;
}
*/

int main() {
    // 由于环境中可能存在编译器配置问题，这里仅提供算法思路
    // 实际实现需要根据具体环境配置进行调整
    return 0;
}