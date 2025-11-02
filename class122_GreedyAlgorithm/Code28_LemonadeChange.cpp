#include <iostream>
#include <vector>
#include <stdexcept>
#include <chrono>
#include <random>
#include <string>

using namespace std;

/**
 * 柠檬水找零
 * 
 * 题目描述：
 * 在柠檬水摊上，每一杯柠檬水的售价为 5 美元。
 * 顾客排队购买你的产品，一次购买一杯。
 * 每位顾客只买一杯柠檬水，然后向你支付 5 美元、10 美元或 20 美元。
 * 你必须给每个顾客正确找零。
 * 注意，一开始你手头没有任何零钱。
 * 如果你能给每位顾客正确找零，返回 true ，否则返回 false 。
 * 
 * 来源：LeetCode 860
 * 链接：https://leetcode.cn/problems/lemonade-change/
 * 
 * 算法思路：
 * 使用贪心算法：
 * 1. 维护两个变量：fiveCount（5美元数量）和 tenCount（10美元数量）
 * 2. 遍历每个顾客的支付金额：
 *    - 如果支付5美元：直接收下，fiveCount++
 *    - 如果支付10美元：需要找零5美元，检查是否有足够的5美元
 *    - 如果支付20美元：优先使用10美元+5美元找零（贪心策略），如果没有10美元则使用3张5美元
 * 3. 如果无法找零，返回false；否则处理完所有顾客后返回true
 * 
 * 时间复杂度：O(n) - 只需要遍历一次顾客数组
 * 空间复杂度：O(1) - 只使用常数空间存储5美元和10美元的数量
 * 
 * 关键点分析：
 * - 贪心策略：支付20美元时优先使用10美元+5美元的组合
 * - 边界处理：检查零钱是否足够
 * - 异常场景：大额支付无法找零的情况
 * 
 * 工程化考量：
 * - 输入验证：检查账单数组是否为空或包含非法面额
 * - 边界处理：处理第一个顾客支付20美元的情况
 * - 性能优化：使用基本类型而非容器
 * - 内存安全：避免内存泄漏
 */
class Code28_LemonadeChange {
public:
    /**
     * 判断是否能给所有顾客正确找零
     * 
     * @param bills 顾客支付的账单数组
     * @return 是否能正确找零
     * @throws invalid_argument 如果账单包含非法面额
     */
    static bool lemonadeChange(vector<int>& bills) {
        // 输入验证
        if (bills.empty()) {
            return true;
        }
        
        int fiveCount = 0; // 5美元数量
        int tenCount = 0;   // 10美元数量
        
        for (size_t i = 0; i < bills.size(); i++) {
            int bill = bills[i];
            
            // 验证账单面额合法性
            if (bill != 5 && bill != 10 && bill != 20) {
                throw invalid_argument("非法账单面额: " + to_string(bill) + "，只支持5、10、20美元");
            }
            
            switch (bill) {
                case 5:
                    // 支付5美元，直接收下
                    fiveCount++;
                    break;
                    
                case 10:
                    // 支付10美元，需要找零5美元
                    if (fiveCount > 0) {
                        fiveCount--;
                        tenCount++;
                    } else {
                        // 没有5美元找零
                        return false;
                    }
                    break;
                    
                case 20:
                    // 支付20美元，需要找零15美元
                    // 贪心策略：优先使用10美元+5美元的组合
                    if (tenCount > 0 && fiveCount > 0) {
                        tenCount--;
                        fiveCount--;
                    } else if (fiveCount >= 3) {
                        // 如果没有10美元，使用3张5美元
                        fiveCount -= 3;
                    } else {
                        // 无法找零
                        return false;
                    }
                    break;
            }
            
            // 调试信息：打印当前零钱状态（实际工程中可移除）
            // cout << "处理账单 " << bill << " 后: 5美元=" << fiveCount << ", 10美元=" << tenCount << endl;
        }
        
        return true;
    }
    
    /**
     * 另一种实现方式：使用更详细的错误信息
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    static bool lemonadeChangeWithDetails(vector<int>& bills) {
        if (bills.empty()) {
            return true;
        }
        
        int five = 0, ten = 0;
        
        for (size_t i = 0; i < bills.size(); i++) {
            int bill = bills[i];
            
            // 验证输入
            if (bill != 5 && bill != 10 && bill != 20) {
                cerr << "错误：第" << (i+1) << "位顾客支付了非法面额 " << bill << endl;
                return false;
            }
            
            if (bill == 5) {
                five++;
            } else if (bill == 10) {
                if (five == 0) {
                    cerr << "错误：第" << (i+1) << "位顾客支付10美元，但无法找零5美元" << endl;
                    return false;
                }
                five--;
                ten++;
            } else { // bill == 20
                if (ten > 0 && five > 0) {
                    ten--;
                    five--;
                } else if (five >= 3) {
                    five -= 3;
                } else {
                    cerr << "错误：第" << (i+1) << "位顾客支付20美元，但无法找零15美元" << endl;
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * 运行测试用例
     */
    static void runTests() {
        cout << "=== 柠檬水找零测试 ===" << endl;
        
        // 测试用例1: [5,5,5,10,20] -> true
        vector<int> bills1 = {5, 5, 5, 10, 20};
        cout << "测试用例1: ";
        for (int bill : bills1) cout << bill << " ";
        cout << endl;
        cout << "结果: " << (lemonadeChange(bills1) ? "true" : "false") << endl; // 期望: true
        
        // 测试用例2: [5,5,10,10,20] -> false
        vector<int> bills2 = {5, 5, 10, 10, 20};
        cout << "\n测试用例2: ";
        for (int bill : bills2) cout << bill << " ";
        cout << endl;
        cout << "结果: " << (lemonadeChange(bills2) ? "true" : "false") << endl; // 期望: false
        
        // 测试用例3: [5,5,10] -> true
        vector<int> bills3 = {5, 5, 10};
        cout << "\n测试用例3: ";
        for (int bill : bills3) cout << bill << " ";
        cout << endl;
        cout << "结果: " << (lemonadeChange(bills3) ? "true" : "false") << endl; // 期望: true
        
        // 测试用例4: [10,10] -> false (第一个顾客支付10美元就无法找零)
        vector<int> bills4 = {10, 10};
        cout << "\n测试用例4: ";
        for (int bill : bills4) cout << bill << " ";
        cout << endl;
        cout << "结果: " << (lemonadeChange(bills4) ? "true" : "false") << endl; // 期望: false
        
        // 测试用例5: [5,5,10,10,5,20,5,10,5,5] -> true
        vector<int> bills5 = {5,5,10,10,5,20,5,10,5,5};
        cout << "\n测试用例5: ";
        for (int bill : bills5) cout << bill << " ";
        cout << endl;
        cout << "结果: " << (lemonadeChange(bills5) ? "true" : "false") << endl; // 期望: true
        
        // 测试用例6: 空数组 -> true
        vector<int> bills6 = {};
        cout << "\n测试用例6: 空数组" << endl;
        cout << "结果: " << (lemonadeChange(bills6) ? "true" : "false") << endl; // 期望: true
        
        // 边界测试：单个5美元
        vector<int> bills7 = {5};
        cout << "\n测试用例7: 5" << endl;
        cout << "结果: " << (lemonadeChange(bills7) ? "true" : "false") << endl; // 期望: true
        
        // 异常测试：非法面额
        try {
            vector<int> bills8 = {5, 15, 10};
            cout << "\n测试用例8: ";
            for (int bill : bills8) cout << bill << " ";
            cout << endl;
            cout << "结果: " << (lemonadeChange(bills8) ? "true" : "false") << endl;
        } catch (const invalid_argument& e) {
            cout << "异常测试通过: " << e.what() << endl;
        }
    }
    
    /**
     * 性能测试方法
     */
    static void performanceTest() {
        // 生成大规模测试数据
        vector<int> largeBills(1000000);
        random_device rd;
        mt19937 gen(rd());
        uniform_int_distribution<> dis(0, 9);
        
        for (size_t i = 0; i < largeBills.size(); i++) {
            // 随机生成5、10、20美元，比例大致为6:3:1
            int rand = dis(gen);
            if (rand < 6) {
                largeBills[i] = 5;
            } else if (rand < 9) {
                largeBills[i] = 10;
            } else {
                largeBills[i] = 20;
            }
        }
        
        auto startTime = chrono::high_resolution_clock::now();
        bool result = lemonadeChange(largeBills);
        auto endTime = chrono::high_resolution_clock::now();
        
        auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
        
        cout << "大规模测试结果: " << (result ? "true" : "false") << endl;
        cout << "执行时间: " << duration.count() << "ms" << endl;
        cout << "数据规模: " << largeBills.size() << " 个顾客" << endl;
    }
    
    /**
     * 算法正确性验证
     */
    static void correctnessTest() {
        cout << "\n=== 算法正确性验证 ===" << endl;
        
        // 验证贪心策略的正确性
        vector<int> test1 = {5, 5, 10, 20}; // 应该成功
        vector<int> test2 = {5, 5, 5, 20};  // 应该成功
        vector<int> test3 = {5, 10, 10, 20}; // 应该失败（只有一个5美元）
        
        cout << "测试1 [5,5,10,20]: " << (lemonadeChange(test1) ? "true" : "false") << endl; // true
        cout << "测试2 [5,5,5,20]: " << (lemonadeChange(test2) ? "true" : "false") << endl;  // true
        cout << "测试3 [5,10,10,20]: " << (lemonadeChange(test3) ? "true" : "false") << endl; // false
        
        // 验证贪心策略的必要性
        vector<int> test4 = {5, 5, 10, 10, 20}; // 贪心策略能成功
        cout << "测试4 [5,5,10,10,20]: " << (lemonadeChange(test4) ? "true" : "false") << endl; // true
    }
    
    /**
     * 算法复杂度分析
     */
    static void analyzeComplexity() {
        cout << "\n=== 算法复杂度分析 ===" << endl;
        cout << "时间复杂度: O(n)" << endl;
        cout << "- 只需要遍历一次顾客数组" << endl;
        cout << "- 每个顾客的处理时间是常数时间" << endl;
        
        cout << "\n空间复杂度: O(1)" << endl;
        cout << "- 只使用两个整数变量存储5美元和10美元的数量" << endl;
        cout << "- 不随输入规模增长而增长" << endl;
        
        cout << "\n贪心策略证明:" << endl;
        cout << "1. 支付20美元时，优先使用10美元+5美元是最优选择" << endl;
        cout << "2. 这样可以保留更多的5美元用于后续找零" << endl;
        cout << "3. 数学证明：10美元只能用于找零20美元，而5美元可以用于找零10美元和20美元" << endl;
    }
};

int main() {
    Code28_LemonadeChange::runTests();
    Code28_LemonadeChange::performanceTest();
    Code28_LemonadeChange::correctnessTest();
    Code28_LemonadeChange::analyzeComplexity();
    
    return 0;
}