/*
LeetCode 401. 二进制手表

二进制手表顶部有 4 个 LED 代表小时（0-11），底部的 6 个 LED 代表分钟（0-59）。
每个 LED 代表一个 0 或 1，最低位在右侧。
给你一个整数 turnedOn，表示当前亮着的 LED 的数量，返回二进制手表可以表示的所有可能时间。

算法思路：
使用回溯算法生成所有可能的LED点亮组合，然后验证这些组合是否能构成有效的时间。

时间复杂度：O(2^10) = O(1024)，因为总共有10个LED
空间复杂度：O(1)，不考虑结果数组的空间
*/

#include <vector>
#include <string>
#include <iostream>
#include <sstream>
#include <iomanip>
using namespace std;

class Solution {
private:
    // LED代表的数值，前4个是小时，后6个是分钟
    vector<int> LED_VALUES = {8, 4, 2, 1, 32, 16, 8, 4, 2, 1};
    
    /**
     * 回溯函数
     * @param turnedOn 剩余需要点亮的LED数量
     * @param start 当前考虑的LED位置
     * @param hour 当前小时数
     * @param minute 当前分钟数
     * @param result 结果列表
     */
    void backtrack(int turnedOn, int start, int hour, int minute, vector<string>& result) {
        // 剪枝：如果小时或分钟超出范围，直接返回
        if (hour > 11 || minute > 59) {
            return;
        }
        
        // 终止条件：所有LED都已考虑完
        if (turnedOn == 0) {
            // 格式化时间字符串
            string time = to_string(hour) + ":" + (minute < 10 ? "0" : "") + to_string(minute);
            result.push_back(time);
            return;
        }
        
        // 遍历剩余的LED
        for (int i = start; i < LED_VALUES.size(); i++) {
            if (i < 4) {
                // 处理小时LED
                backtrack(turnedOn - 1, i + 1, hour + LED_VALUES[i], minute, result);
            } else {
                // 处理分钟LED
                backtrack(turnedOn - 1, i + 1, hour, minute + LED_VALUES[i], result);
            }
        }
    }
    
public:
    /**
     * 返回二进制手表可以表示的所有可能时间
     * @param turnedOn 当前亮着的 LED 的数量
     * @return 所有可能的时间列表
     */
    vector<string> readBinaryWatch(int turnedOn) {
        vector<string> result;
        backtrack(turnedOn, 0, 0, 0, result);
        return result;
    }
};

// 测试代码
int main() {
    Solution solution;
    
    // 测试用例1
    cout << "turnedOn = 1:" << endl;
    vector<string> result1 = solution.readBinaryWatch(1);
    for (const string& time : result1) {
        cout << time << " ";
    }
    cout << endl;
    
    // 测试用例2
    cout << "\nturnedOn = 9:" << endl;
    vector<string> result2 = solution.readBinaryWatch(9);
    for (const string& time : result2) {
        cout << time << " ";
    }
    cout << endl;
    
    return 0;
}