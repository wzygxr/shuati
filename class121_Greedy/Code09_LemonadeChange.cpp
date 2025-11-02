/**
 * 柠檬水找零
 * 
 * 题目描述：
 * 在柠檬水摊上，每一杯柠檬水的售价为 5 美元。顾客排队购买你的产品，（按账单 bills 支付的顺序）一次购买一杯。
 * 每位顾客只买一杯柠檬水，然后向你付 5 美元、10 美元或 20 美元。
 * 你必须给每个顾客正确找零，也就是说净交易是每位顾客向你支付 5 美元。
 * 注意，一开始你手头没有任何零钱。
 * 给你一个整数数组 bills ，其中 bills[i] 是第 i 位顾客付的账。
 * 如果你能给每位顾客正确找零，返回 true，否则返回 false。
 * 
 * 解题思路：
 * 1. 维护两个变量分别记录手中5美元和10美元的数量
 * 2. 遍历账单数组：
 *    - 收到5美元：直接增加5美元数量
 *    - 收到10美元：需要找零5美元，检查是否有足够的5美元
 *    - 收到20美元：需要找零15美元，优先使用一张10美元+一张5美元，其次使用三张5美元
 * 
 * 贪心策略的正确性：
 * 当需要找零15美元时，优先使用一张10美元+一张5美元，而不是三张5美元，
 * 因为10美元只能用于找零20美元，而5美元可以用于找零10美元和20美元，更加通用。
 * 
 * 时间复杂度：O(n)，只需要遍历数组一次
 * 空间复杂度：O(1)，只使用了常数个额外变量
 * 
 * 相关题目：
 * - LeetCode 860: https://leetcode.cn/problems/lemonade-change/
 */

#include <iostream>
#include <vector>
using namespace std;

/**
 * 柠檬水找零问题的贪心解法
 * 
 * @param bills 顾客支付账单的数组
 * @return bool 是否能给每位顾客正确找零
 */
bool lemonadeChange(vector<int>& bills) {
    // 边界条件处理：如果账单数组为空，返回true
    if (bills.empty()) {
        return true;
    }

    // 1. 初始化手中5美元和10美元的数量
    int fiveCount = 0;  // 5美元的数量
    int tenCount = 0;   // 10美元的数量

    // 2. 遍历账单数组
    for (int bill : bills) {
        switch (bill) {
            case 5:
                // 收到5美元，无需找零，直接增加5美元数量
                fiveCount++;
                break;
            case 10:
                // 收到10美元，需要找零5美元
                if (fiveCount > 0) {
                    fiveCount--;  // 找零一张5美元
                    tenCount++;   // 增加一张10美元
                } else {
                    // 没有5美元可以找零，返回false
                    return false;
                }
                break;
            case 20:
                // 收到20美元，需要找零15美元
                // 贪心策略：优先使用一张10美元+一张5美元
                if (tenCount > 0 && fiveCount > 0) {
                    tenCount--;   // 找零一张10美元
                    fiveCount--;  // 找零一张5美元
                } 
                // 如果没有10美元，则尝试使用三张5美元
                else if (fiveCount >= 3) {
                    fiveCount -= 3;  // 找零三张5美元
                } 
                // 如果两种方式都不行，无法找零
                else {
                    return false;
                }
                break;
            default:
                // 非法输入，根据题目约束不会出现这种情况
                return false;
        }
    }

    // 3. 所有顾客都能正确找零
    return true;
}

// 测试代码
int main() {
    // 测试用例1
    // 输入: bills = [5,5,5,10,20]
    // 输出: true
    vector<int> bills1 = {5, 5, 5, 10, 20};
    cout << "测试用例1结果: " << (lemonadeChange(bills1) ? "true" : "false") << endl; // 期望输出: true

    // 测试用例2
    // 输入: bills = [5,5,10,10,20]
    // 输出: false
    vector<int> bills2 = {5, 5, 10, 10, 20};
    cout << "测试用例2结果: " << (lemonadeChange(bills2) ? "true" : "false") << endl; // 期望输出: false

    // 测试用例3：边界情况
    // 输入: bills = [5]
    // 输出: true
    vector<int> bills3 = {5};
    cout << "测试用例3结果: " << (lemonadeChange(bills3) ? "true" : "false") << endl; // 期望输出: true

    // 测试用例4：复杂情况
    // 输入: bills = [5,5,10,20,5,5,5,5,5,5,5,5,5,10,5,5,20,5,20,5]
    // 输出: true
    vector<int> bills4 = {5,5,10,20,5,5,5,5,5,5,5,5,5,10,5,5,20,5,20,5};
    cout << "测试用例4结果: " << (lemonadeChange(bills4) ? "true" : "false") << endl; // 期望输出: true

    return 0;
}