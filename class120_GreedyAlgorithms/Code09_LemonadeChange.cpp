/**
 * 柠檬水找零 - 贪心算法 + 计数策略解决方案（C++实现，LeetCode版本）
 * 
 * 题目描述：
 * 在柠檬水摊上，每一杯柠檬水的售价为 5 美元。顾客排队购买你的产品，（按账单 bills 支付的顺序）一次购买一杯
 * 每位顾客只买一杯柠檬水，然后向你付 5 美元、10 美元或 20 美元。你必须给每个顾客正确找零，也就是说净交易是每位顾客向你支付 5 美元
 * 注意，一开始你手头没有任何零钱
 * 给你一个整数数组 bills ，其中 bills[i] 是第 i 位顾客付的账。如果你能给每位顾客正确找零，返回 true ，否则返回 false
 * 
 * 测试链接：https://leetcode.cn/problems/lemonade-change/
 * 
 * 算法思想：
 * 贪心算法 + 计数策略
 * 1. 维护5美元和10美元纸币的数量
 * 2. 遇到5美元：直接收下，5美元数量加1
 * 3. 遇到10美元：需要找零5美元，检查是否有5美元纸币，如果有则5美元数量减1，10美元数量加1，否则返回false
 * 4. 遇到20美元：需要找零15美元，优先使用一张10美元和一张5美元的组合，如果没有10美元则使用三张5美元，如果都不满足则返回false
 * 
 * 贪心策略解释：
 * 在找零15美元时，优先使用10+5的组合而不是5+5+5，因为5美元更万能，可以用于10美元和20美元的找零，
 * 而10美元只能用于20美元的找零，所以要尽可能保留5美元纸币
 * 
 * 时间复杂度分析：
 * O(n) - 其中n是数组bills的长度，只需要遍历一次数组
 * 
 * 空间复杂度分析：
 * O(1) - 只使用了常数级别的额外空间
 * 
 * 是否为最优解：
 * 是，这是解决该问题的最优解
 * 
 * 工程化考量：
 * 1. 边界条件处理：空数组等特殊情况
 * 2. 输入验证：检查输入是否为有效数组
 * 3. 异常处理：对非法输入进行检查
 * 4. 可读性：添加详细注释和变量命名
 */

#include <iostream>
#include <vector>
#include <stdexcept>

using namespace std;

class Solution {
public:
    /**
     * 判断是否能正确找零
     * 
     * @param bills 顾客付款数组
     * @return 是否能正确找零
     * 
     * 算法步骤：
     * 1. 维护5美元和10美元纸币的数量
     * 2. 遍历付款数组，根据面额进行相应处理
     * 3. 如果无法找零，立即返回false
     * 4. 所有顾客都能正确找零则返回true
     */
    bool lemonadeChange(vector<int>& bills) {
        // 输入验证
        if (bills.empty()) {
            return true; // 空数组表示没有顾客，可以正确找零
        }
        
        int fiveCount = 0;  // 5美元纸币数量
        int tenCount = 0;   // 10美元纸币数量
        
        // 遍历顾客付款数组
        for (int bill : bills) {
            // 验证付款面额
            if (bill != 5 && bill != 10 && bill != 20) {
                throw invalid_argument("付款面额必须是5、10或20美元");
            }
            
            if (bill == 5) {
                // 顾客支付5美元，无需找零，直接收下
                fiveCount++;
            } else if (bill == 10) {
                // 顾客支付10美元，需要找零5美元
                if (fiveCount > 0) {
                    fiveCount--;  // 找零一张5美元
                    tenCount++;   // 收下一张10美元
                } else {
                    // 没有5美元纸币找零，返回false
                    return false;
                }
            } else if (bill == 20) {
                // 顾客支付20美元，需要找零15美元
                // 贪心策略：优先使用一张10美元和一张5美元的组合
                if (tenCount > 0 && fiveCount > 0) {
                    tenCount--;   // 找零一张10美元
                    fiveCount--;  // 找零一张5美元
                } 
                // 如果没有10美元，则尝试使用三张5美元
                else if (fiveCount >= 3) {
                    fiveCount -= 3;  // 找零三张5美元
                } 
                // 如果两种方式都不满足，则无法正确找零
                else {
                    return false;
                }
            }
        }
        
        // 所有顾客都能正确找零
        return true;
    }
    
    /**
     * 调试版本：打印计算过程中的中间结果
     * 
     * @param bills 顾客付款数组
     * @return 是否能正确找零
     */
    bool debugLemonadeChange(vector<int>& bills) {
        if (bills.empty()) {
            cout << "没有顾客，可以正确找零" << endl;
            return true;
        }
        
        cout << "顾客付款序列:" << endl;
        for (int i = 0; i < bills.size(); i++) {
            cout << "第" << (i+1) << "位顾客支付: " << bills[i] << "美元" << endl;
        }
        
        int fiveCount = 0;
        int tenCount = 0;
        
        cout << "\n找零过程:" << endl;
        for (int i = 0; i < bills.size(); i++) {
            int bill = bills[i];
            cout << "第" << (i+1) << "位顾客支付" << bill << "美元: ";
            
            if (bill == 5) {
                fiveCount++;
                cout << "收下5美元，无需找零。当前5美元数量: " << fiveCount 
                     << ", 10美元数量: " << tenCount << endl;
            } else if (bill == 10) {
                if (fiveCount > 0) {
                    fiveCount--;
                    tenCount++;
                    cout << "找零5美元，收下10美元。当前5美元数量: " << fiveCount 
                         << ", 10美元数量: " << tenCount << endl;
                } else {
                    cout << "无法找零5美元，交易失败" << endl;
                    return false;
                }
            } else if (bill == 20) {
                if (tenCount > 0 && fiveCount > 0) {
                    tenCount--;
                    fiveCount--;
                    cout << "找零10+5美元组合。当前5美元数量: " << fiveCount 
                         << ", 10美元数量: " << tenCount << endl;
                } else if (fiveCount >= 3) {
                    fiveCount -= 3;
                    cout << "找零5+5+5美元组合。当前5美元数量: " << fiveCount 
                         << ", 10美元数量: " << tenCount << endl;
                } else {
                    cout << "无法找零15美元，交易失败" << endl;
                    return false;
                }
            }
        }
        
        cout << "\n所有顾客都能正确找零" << endl;
        return true;
    }
};

/**
 * 测试函数：验证柠檬水找零算法的正确性
 */
void testLemonadeChange() {
    Solution solution;
    
    cout << "柠檬水找零算法测试开始" << endl;
    cout << "====================" << endl;
    
    // 测试用例1: [5,5,5,10,20]
    vector<int> bills1 = {5, 5, 5, 10, 20};
    bool result1 = solution.lemonadeChange(bills1);
    cout << "输入: [5,5,5,10,20]" << endl;
    cout << "输出: " << (result1 ? "true" : "false") << endl;
    cout << "预期: true" << endl;
    cout << (result1 ? "✓ 通过" : "✗ 失败") << endl;
    cout << endl;
    
    // 测试用例2: [5,5,10,10,20]
    vector<int> bills2 = {5, 5, 10, 10, 20};
    bool result2 = solution.lemonadeChange(bills2);
    cout << "输入: [5,5,10,10,20]" << endl;
    cout << "输出: " << (result2 ? "true" : "false") << endl;
    cout << "预期: false" << endl;
    cout << (!result2 ? "✓ 通过" : "✗ 失败") << endl;
    cout << endl;
    
    // 测试用例3: [10,10]
    vector<int> bills3 = {10, 10};
    bool result3 = solution.lemonadeChange(bills3);
    cout << "输入: [10,10]" << endl;
    cout << "输出: " << (result3 ? "true" : "false") << endl;
    cout << "预期: false" << endl;
    cout << (!result3 ? "✓ 通过" : "✗ 失败") << endl;
    cout << endl;
    
    // 测试用例4: [5,5,10]
    vector<int> bills4 = {5, 5, 10};
    bool result4 = solution.lemonadeChange(bills4);
    cout << "输入: [5,5,10]" << endl;
    cout << "输出: " << (result4 ? "true" : "false") << endl;
    cout << "预期: true" << endl;
    cout << (result4 ? "✓ 通过" : "✗ 失败") << endl;
    cout << endl;
    
    // 测试用例5: 空数组
    vector<int> bills5 = {};
    bool result5 = solution.lemonadeChange(bills5);
    cout << "输入: []" << endl;
    cout << "输出: " << (result5 ? "true" : "false") << endl;
    cout << "预期: true" << endl;
    cout << (result5 ? "✓ 通过" : "✗ 失败") << endl;
    cout << endl;
    
    // 测试用例6: 复杂情况
    vector<int> bills6 = {5,5,5,5,5,5,5,5,5,5,10,20,20,20,20,20};
    bool result6 = solution.lemonadeChange(bills6);
    cout << "输入: [5,5,5,5,5,5,5,5,5,5,10,20,20,20,20,20]" << endl;
    cout << "输出: " << (result6 ? "true" : "false") << endl;
    cout << "预期: false" << endl;
    cout << (!result6 ? "✓ 通过" : "✗ 失败") << endl;
    cout << endl;
}

/**
 * 性能测试：测试算法在大规模数据下的表现
 */
void performanceTest() {
    Solution solution;
    
    cout << "性能测试开始" << endl;
    cout << "============" << endl;
    
    // 生成大规模测试数据
    int n = 100000;
    vector<int> bills(n);
    
    // 生成随机付款序列（5、10、20美元）
    for (int i = 0; i < n; i++) {
        int randomValue = rand() % 3;
        if (randomValue == 0) bills[i] = 5;
        else if (randomValue == 1) bills[i] = 10;
        else bills[i] = 20;
    }
    
    long startTime = clock();
    bool result = solution.lemonadeChange(bills);
    long endTime = clock();
    
    cout << "数据规模: " << n << " 位顾客" << endl;
    cout << "执行时间: " << (endTime - startTime) * 1000.0 / CLOCKS_PER_SEC << " 毫秒" << endl;
    cout << "找零结果: " << (result ? "成功" : "失败") << endl;
    cout << "性能测试结束" << endl;
}

/**
 * 主函数：运行测试
 */
int main() {
    cout << "柠檬水找零 - 贪心算法 + 计数策略解决方案" << endl;
    cout << "======================================" << endl;
    
    // 运行基础测试
    testLemonadeChange();
    
    cout << "\n调试模式示例:" << endl;
    Solution solution;
    vector<int> debugBills = {5, 5, 5, 10, 20};
    cout << "对测试用例 [5,5,5,10,20] 进行调试跟踪:" << endl;
    bool debugResult = solution.debugLemonadeChange(debugBills);
    cout << "最终结果: " << (debugResult ? "true" : "false") << endl;
    
    cout << "\n算法分析:" << endl;
    cout << "- 时间复杂度: O(n) - 只需要遍历一次付款数组" << endl;
    cout << "- 空间复杂度: O(1) - 只使用常数级别额外空间" << endl;
    cout << "- 贪心策略: 优先使用10+5的组合找零，保留更多5美元纸币" << endl;
    cout << "- 最优性: 这种贪心策略能够得到全局最优解" << endl;
    cout << "- 关键点: 5美元纸币的通用性比10美元更强" << endl;
    
    // 可选：运行性能测试
    // cout << "\n性能测试:" << endl;
    // performanceTest();
    
    // 测试异常处理
    cout << "\n异常处理测试:" << endl;
    try {
        vector<int> invalidBills = {5, 15, 10}; // 包含非法面额15
        solution.lemonadeChange(invalidBills);
    } catch (const invalid_argument& e) {
        cout << "异常处理测试通过: " << e.what() << endl;
    }
    
    return 0;
}