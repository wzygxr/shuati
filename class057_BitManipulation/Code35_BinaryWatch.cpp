#include <iostream>
#include <vector>
#include <string>
#include <bitset>
#include <algorithm>

using namespace std;

/**
 * 二进制手表
 * 测试链接：https://leetcode.cn/problems/binary-watch/
 * 
 * 题目描述：
 * 二进制手表顶部有 4 个 LED 代表 小时（0-11），底部的 6 个 LED 代表 分钟（0-59）。
 * 每个 LED 代表一个 0 或 1，最低位在右侧。
 * 给你一个整数 turnedOn ，表示当前亮着的 LED 的数量，返回所有可能的时间。
 * 你可以按任意顺序返回答案。
 * 
 * 解题思路：
 * 1. 枚举法：枚举所有可能的小时和分钟组合
 * 2. 位运算法：利用bitset计算亮灯数量
 * 3. 回溯法：递归选择亮灯的位置
 * 
 * 时间复杂度分析：
 * - 枚举法：O(12 * 60) = O(720)，常数时间
 * - 位运算法：O(2^10) = O(1024)，常数时间
 * 
 * 空间复杂度分析：
 * - 枚举法：O(n)，n为结果数量
 * - 位运算法：O(n)，n为结果数量
 */
class Solution {
public:
    /**
     * 方法1：枚举法（推荐）
     * 枚举所有可能的小时和分钟，检查亮灯数量是否匹配
     * 时间复杂度：O(12 * 60) = O(720)
     * 空间复杂度：O(n)，n为结果数量
     */
    vector<string> readBinaryWatch1(int turnedOn) {
        vector<string> result;
        
        // 枚举所有可能的小时（0-11）和分钟（0-59）
        for (int hour = 0; hour < 12; hour++) {
            for (int minute = 0; minute < 60; minute++) {
                // 计算小时和分钟的二进制中1的个数
                if (bitset<4>(hour).count() + bitset<6>(minute).count() == turnedOn) {
                    result.push_back(formatTime(hour, minute));
                }
            }
        }
        
        return result;
    }
    
    /**
     * 方法2：位运算法
     * 枚举所有10位二进制数（4位小时 + 6位分钟）
     * 时间复杂度：O(2^10) = O(1024)
     * 空间复杂度：O(n)，n为结果数量
     */
    vector<string> readBinaryWatch2(int turnedOn) {
        vector<string> result;
        
        // 枚举所有10位二进制数（0-1023）
        for (int i = 0; i < 1024; i++) {
            // 高4位表示小时，低6位表示分钟
            int hour = i >> 6;
            int minute = i & 0x3F; // 0x3F = 63，取低6位
            
            // 检查小时和分钟是否在有效范围内
            if (hour < 12 && minute < 60 && bitset<10>(i).count() == turnedOn) {
                result.push_back(formatTime(hour, minute));
            }
        }
        
        return result;
    }
    
    /**
     * 方法3：使用__builtin_popcount（GCC扩展）
     * 时间复杂度：O(12 * 60) = O(720)
     * 空间复杂度：O(n)，n为结果数量
     */
    vector<string> readBinaryWatch3(int turnedOn) {
        vector<string> result;
        
        for (int hour = 0; hour < 12; hour++) {
            for (int minute = 0; minute < 60; minute++) {
                // 使用GCC内置函数计算1的个数
                if (__builtin_popcount(hour) + __builtin_popcount(minute) == turnedOn) {
                    result.push_back(formatTime(hour, minute));
                }
            }
        }
        
        return result;
    }
    
    /**
     * 方法4：回溯法
     * 递归选择亮灯的位置
     * 时间复杂度：O(C(10, turnedOn))，组合数
     * 空间复杂度：O(n)，递归深度和结果数量
     */
    vector<string> readBinaryWatch4(int turnedOn) {
        vector<string> result;
        if (turnedOn < 0 || turnedOn > 10) {
            return result;
        }
        
        // 回溯选择亮灯位置
        backtrack(result, 0, 0, turnedOn, 0);
        return result;
    }
    
private:
    /**
     * 格式化时间输出
     */
    string formatTime(int hour, int minute) {
        char buffer[6];
        snprintf(buffer, sizeof(buffer), "%d:%02d", hour, minute);
        return string(buffer);
    }
    
    /**
     * 回溯函数
     */
    void backtrack(vector<string>& result, int hour, int minute, 
                   int remaining, int start) {
        // 如果剩余亮灯数为0，检查是否有效
        if (remaining == 0) {
            if (hour < 12 && minute < 60) {
                result.push_back(formatTime(hour, minute));
            }
            return;
        }
        
        // 从start位置开始选择亮灯
        for (int i = start; i < 10; i++) {
            int newHour = hour;
            int newMinute = minute;
            
            // 根据位置设置小时或分钟
            if (i < 4) { // 小时位（0-3）
                newHour = hour | (1 << (3 - i));
            } else { // 分钟位（4-9）
                newMinute = minute | (1 << (9 - i));
            }
            
            backtrack(result, newHour, newMinute, remaining - 1, i + 1);
        }
    }
};

/**
 * 测试函数
 */
int main() {
    Solution solution;
    
    // 测试用例1：亮1盏灯
    vector<string> result1 = solution.readBinaryWatch1(1);
    cout << "测试用例1 - turnedOn = 1" << endl;
    cout << "结果数量: " << result1.size() << endl;
    cout << "前5个结果: ";
    for (int i = 0; i < min(5, (int)result1.size()); i++) {
        cout << result1[i] << " ";
    }
    cout << endl;
    
    // 测试用例2：亮2盏灯
    vector<string> result2 = solution.readBinaryWatch1(2);
    cout << "测试用例2 - turnedOn = 2" << endl;
    cout << "结果数量: " << result2.size() << endl;
    
    // 测试用例3：亮9盏灯（应该为空）
    vector<string> result3 = solution.readBinaryWatch1(9);
    cout << "测试用例3 - turnedOn = 9" << endl;
    cout << "结果数量: " << result3.size() << endl;
    cout << "结果: ";
    for (const auto& time : result3) {
        cout << time << " ";
    }
    cout << endl;
    
    // 测试用例4：亮0盏灯
    vector<string> result4 = solution.readBinaryWatch1(0);
    cout << "测试用例4 - turnedOn = 0" << endl;
    cout << "结果数量: " << result4.size() << endl;
    cout << "结果: ";
    for (const auto& time : result4) {
        cout << time << " ";
    }
    cout << endl;
    
    // 性能比较
    auto start = chrono::high_resolution_clock::now();
    vector<string> result5 = solution.readBinaryWatch1(3);
    auto end = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "方法1性能 - 耗时: " << duration.count() << "微秒" << endl;
    
    start = chrono::high_resolution_clock::now();
    vector<string> result6 = solution.readBinaryWatch2(3);
    end = chrono::high_resolution_clock::now();
    duration = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "方法2性能 - 耗时: " << duration.count() << "微秒" << endl;
    
    // 复杂度分析
    cout << "\n=== 复杂度分析 ===" << endl;
    cout << "方法1 - 枚举法:" << endl;
    cout << "  时间复杂度: O(12 * 60) = O(720)" << endl;
    cout << "  空间复杂度: O(n)，n为结果数量" << endl;
    
    cout << "方法2 - 位运算法:" << endl;
    cout << "  时间复杂度: O(2^10) = O(1024)" << endl;
    cout << "  空间复杂度: O(n)，n为结果数量" << endl;
    
    cout << "方法3 - GCC内置函数法:" << endl;
    cout << "  时间复杂度: O(12 * 60) = O(720)" << endl;
    cout << "  空间复杂度: O(n)，n为结果数量" << endl;
    
    cout << "方法4 - 回溯法:" << endl;
    cout << "  时间复杂度: O(C(10, turnedOn))" << endl;
    cout << "  空间复杂度: O(n)，递归深度和结果数量" << endl;
    
    // 工程化考量
    cout << "\n=== 工程化考量 ===" << endl;
    cout << "1. 算法选择：方法1最简单实用" << endl;
    cout << "2. 边界处理：检查turnedOn范围（0-10）" << endl;
    cout << "3. 性能优化：720次枚举足够快" << endl;
    cout << "4. 可移植性：方法1不依赖编译器特定功能" << endl;
    
    // 算法技巧总结
    cout << "\n=== 算法技巧总结 ===" << endl;
    cout << "1. bitset::count(): 计算二进制中1的个数" << endl;
    cout << "2. 位运算：>> 和 & 操作提取高低位" << endl;
    cout << "3. 枚举法：当数据范围较小时，直接枚举所有可能" << endl;
    cout << "4. 格式化输出：确保时间格式正确" << endl;
    
    return 0;
}