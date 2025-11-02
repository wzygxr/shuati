#include <iostream>
#include <vector>
#include <algorithm>
#include <chrono>
#include <random>

using namespace std;

/**
 * LeetCode 1109. 航班预订统计 (Corporate Flight Bookings) - C++版本
 * 
 * 题目来源：https://leetcode.cn/problems/corporate-flight-bookings/
 * 
 * 题目描述：
 * 这里有 n 个航班，它们分别从 1 到 n 进行编号。
 * 有一份航班预订表 bookings，表中第 i 条预订记录 bookings[i] = [firsti, lasti, seatsi]
 * 意味着在从 firsti 到 lasti（包含 firsti 和 lasti）的每个航班上预订了 seatsi 个座位。
 * 请你返回一个长度为 n 的数组 answer，里面的元素是每个航班预定的座位总数。
 * 
 * 算法思路：
 * 这个问题可以通过以下方法解决：
 * 1. 差分数组：记录每个位置的座位变化
 * 2. 暴力法：直接更新每个位置的座位数
 * 
 * 使用差分数组的方法：
 * 1. 对于每个预订记录，在差分数组的起始位置增加座位数，在结束位置的下一个位置减少座位数
 * 2. 计算差分数组的前缀和得到最终结果
 * 
 * 时间复杂度：
 * - 差分数组：O(n + m)
 * - 暴力法：O(n * m)
 * - 空间复杂度：O(n)
 * 
 * 应用场景：
 * 1. 资源分配：统计资源使用情况
 * 2. 数据分析：区间数据统计
 * 3. 项目管理：任务分配统计
 * 
 * 相关题目：
 * 1. LeetCode 370. 区间加法
 * 2. LeetCode 1094. 拼车
 * 3. LeetCode 732. 我的日程安排表 III
 */

/**
 * 方法1：差分数组
 * 时间复杂度：O(n + m)
 * 空间复杂度：O(n)
 * @param bookings 预订记录数组
 * @param n 航班数量
 * @return 每个航班预定的座位总数
 */
vector<int> corpFlightBookingsDifferenceArray(vector<vector<int>>& bookings, int n) {
    // 创建差分数组，大小为n+1，方便处理边界
    vector<int> diff(n + 1, 0);
    
    // 处理每个预订记录
    for (const auto& booking : bookings) {
        int first = booking[0];
        int last = booking[1];
        int seats = booking[2];
        
        // 在差分数组中标记座位变化
        diff[first - 1] += seats;  // 航班编号从1开始，数组索引从0开始
        diff[last] -= seats;
    }
    
    // 计算差分数组的前缀和得到最终结果
    vector<int> result(n, 0);
    result[0] = diff[0];
    for (int i = 1; i < n; i++) {
        result[i] = result[i - 1] + diff[i];
    }
    
    return result;
}

/**
 * 方法2：暴力法（用于对比）
 * 时间复杂度：O(n * m)
 * 空间复杂度：O(n)
 * @param bookings 预订记录数组
 * @param n 航班数量
 * @return 每个航班预定的座位总数
 */
vector<int> corpFlightBookingsBruteForce(vector<vector<int>>& bookings, int n) {
    // 创建结果数组
    vector<int> result(n, 0);
    
    // 处理每个预订记录
    for (const auto& booking : bookings) {
        int first = booking[0];
        int last = booking[1];
        int seats = booking[2];
        
        // 直接更新每个航班的座位数
        for (int i = first - 1; i < last; i++) {  // 航班编号从1开始，数组索引从0开始
            result[i] += seats;
        }
    }
    
    return result;
}

/**
 * 测试函数
 */
void testCorpFlightBookings() {
    cout << "=== LeetCode 1109. 航班预订统计 (C++版本) ===" << endl;
    
    // 测试用例1
    cout << "测试用例1:" << endl;
    vector<vector<int>> bookings1 = {{1, 2, 10}, {2, 3, 20}, {2, 5, 25}};
    int n1 = 5;
    cout << "预订记录: [[1,2,10],[2,3,20],[2,5,25]]" << endl;
    cout << "航班数量: " << n1 << endl;
    
    vector<int> result1 = corpFlightBookingsDifferenceArray(bookings1, n1);
    cout << "差分数组结果: [";
    for (int i = 0; i < result1.size(); i++) {
        cout << result1[i];
        if (i < result1.size() - 1) cout << ",";
    }
    cout << "]" << endl;
    
    vector<int> result2 = corpFlightBookingsBruteForce(bookings1, n1);
    cout << "暴力解法结果: [";
    for (int i = 0; i < result2.size(); i++) {
        cout << result2[i];
        if (i < result2.size() - 1) cout << ",";
    }
    cout << "]" << endl;
    
    cout << "期望结果: [10,55,45,25,25]" << endl;
    cout << endl;
    
    // 测试用例2
    cout << "测试用例2:" << endl;
    vector<vector<int>> bookings2 = {{1, 2, 10}, {2, 2, 15}};
    int n2 = 2;
    cout << "预订记录: [[1,2,10],[2,2,15]]" << endl;
    cout << "航班数量: " << n2 << endl;
    
    vector<int> result3 = corpFlightBookingsDifferenceArray(bookings2, n2);
    cout << "差分数组结果: [";
    for (int i = 0; i < result3.size(); i++) {
        cout << result3[i];
        if (i < result3.size() - 1) cout << ",";
    }
    cout << "]" << endl;
    
    vector<int> result4 = corpFlightBookingsBruteForce(bookings2, n2);
    cout << "暴力解法结果: [";
    for (int i = 0; i < result4.size(); i++) {
        cout << result4[i];
        if (i < result4.size() - 1) cout << ",";
    }
    cout << "]" << endl;
    
    cout << "期望结果: [10,25]" << endl;
    cout << endl;
    
    // 性能测试
    cout << "=== 性能测试 ===" << endl;
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<> dis_flight(1, 1000);
    uniform_int_distribution<> dis_seats(1, 100);
    
    int m = 10000;  // 预订记录数
    int n = 10000;  // 航班数
    vector<vector<int>> bookings(m);
    
    for (int i = 0; i < m; i++) {
        int first = dis_flight(gen);
        int last = min(first + dis_flight(gen) % 100, n);
        int seats = dis_seats(gen);
        bookings[i] = {first, last, seats};
    }
    
    auto start_time = chrono::high_resolution_clock::now();
    vector<int> diff_result = corpFlightBookingsDifferenceArray(bookings, n);
    auto end_time = chrono::high_resolution_clock::now();
    auto duration1 = chrono::duration_cast<chrono::microseconds>(end_time - start_time);
    cout << "差分数组法处理" << m << "个预订记录和" << n << "个航班时间: " << duration1.count() / 1000.0 << " ms" << endl;
    
    start_time = chrono::high_resolution_clock::now();
    vector<int> brute_result = corpFlightBookingsBruteForce(bookings, n);
    end_time = chrono::high_resolution_clock::now();
    auto duration2 = chrono::duration_cast<chrono::microseconds>(end_time - start_time);
    cout << "暴力解法处理" << m << "个预订记录和" << n << "个航班时间: " << duration2.count() / 1000.0 << " ms" << endl;
    
    cout << "两种方法结果是否一致: " << (diff_result == brute_result ? "是" : "否") << endl;
    
    // C++语言特性考量
    cout << "\n=== C++语言特性考量 ===" << endl;
    cout << "1. 使用STL容器提高开发效率" << endl;
    cout << "2. 使用const引用避免不必要的拷贝" << endl;
    cout << "3. 使用auto关键字简化类型声明" << endl;
    cout << "4. 使用范围for循环简化遍历" << endl;
    
    // 算法复杂度分析
    cout << "\n=== 算法复杂度分析 ===" << endl;
    cout << "差分数组法:" << endl;
    cout << "  时间复杂度: O(n + m) - n是航班数，m是预订记录数" << endl;
    cout << "  空间复杂度: O(n) - 差分数组和结果数组" << endl;
    cout << "暴力解法:" << endl;
    cout << "  时间复杂度: O(n * m) - 对于每个预订记录都要遍历区间" << endl;
    cout << "  空间复杂度: O(n) - 结果数组" << endl;
}

int main() {
    testCorpFlightBookings();
    return 0;
}