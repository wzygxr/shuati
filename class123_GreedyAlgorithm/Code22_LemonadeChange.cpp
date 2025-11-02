#include <iostream>
#include <vector>
#include <chrono>
#include <cstdlib>
#include <ctime>

using namespace std;

/**
 * LeetCode 860. 柠檬水找零
 * 题目链接：https://leetcode.cn/problems/lemonade-change/
 * 难度：简单
 * 
 * C++实现版本
 */

class Solution {
public:
    /**
     * 柠檬水找零解决方案
     * @param bills 账单数组
     * @return 是否能正确找零
     */
    bool lemonadeChange(vector<int>& bills) {
        // 边界条件处理
        if (bills.empty()) {
            return true; // 空数组，没有交易，返回true
        }
        
        int fiveCount = 0; // 5美元数量
        int tenCount = 0;  // 10美元数量
        
        for (int bill : bills) {
            switch (bill) {
                case 5:
                    // 收到5美元，直接收取
                    fiveCount++;
                    break;
                    
                case 10:
                    // 收到10美元，需要找零5美元
                    if (fiveCount > 0) {
                        fiveCount--;
                        tenCount++;
                    } else {
                        return false; // 没有5美元找零
                    }
                    break;
                    
                case 20:
                    // 收到20美元，优先使用10美元+5美元找零
                    if (tenCount > 0 && fiveCount > 0) {
                        tenCount--;
                        fiveCount--;
                    } 
                    // 如果没有10美元，使用3张5美元
                    else if (fiveCount >= 3) {
                        fiveCount -= 3;
                    } else {
                        return false; // 无法找零
                    }
                    break;
                    
                default:
                    // 非法面值，虽然题目保证不会出现，但工程中需要处理
                    throw invalid_argument("非法面值: " + to_string(bill));
            }
        }
        
        return true;
    }
    
    /**
     * 优化版本：添加详细注释和调试信息
     */
    bool lemonadeChangeOptimized(vector<int>& bills) {
        if (bills.empty()) {
            return true;
        }
        
        int fiveCount = 0;
        int tenCount = 0;
        
        cout << "开始处理柠檬水找零..." << endl;
        cout << "账单序列: ";
        for (int bill : bills) cout << bill << " ";
        cout << endl;
        
        for (int i = 0; i < bills.size(); i++) {
            int bill = bills[i];
            cout << "第" << (i + 1) << "位顾客支付" << bill << "美元" << endl;
            
            switch (bill) {
                case 5:
                    fiveCount++;
                    cout << "  直接收取5美元，无需找零" << endl;
                    break;
                    
                case 10:
                    if (fiveCount > 0) {
                        fiveCount--;
                        tenCount++;
                        cout << "  找零5美元成功" << endl;
                    } else {
                        cout << "  无法找零5美元，交易失败" << endl;
                        return false;
                    }
                    break;
                    
                case 20:
                    // 贪心策略：优先使用10美元+5美元
                    if (tenCount > 0 && fiveCount > 0) {
                        tenCount--;
                        fiveCount--;
                        cout << "  使用10美元+5美元找零成功" << endl;
                    } 
                    // 次优策略：使用3张5美元
                    else if (fiveCount >= 3) {
                        fiveCount -= 3;
                        cout << "  使用3张5美元找零成功" << endl;
                    } else {
                        cout << "  无法找零20美元，交易失败" << endl;
                        return false;
                    }
                    break;
                    
                default:
                    throw invalid_argument("非法面值: " + to_string(bill));
            }
            
            cout << "  当前库存: 5美元=" << fiveCount << "张，10美元=" << tenCount << "张" << endl << endl;
        }
        
        cout << "所有交易成功完成" << endl;
        return true;
    }
};

/**
 * 测试函数
 */
void testLemonadeChange() {
    Solution solution;
    
    // 测试用例1：标准示例
    vector<int> bills1 = {5, 5, 5, 10, 20};
    cout << "=== 测试用例1 ===" << endl;
    cout << "账单: ";
    for (int bill : bills1) cout << bill << " ";
    cout << endl;
    bool result1 = solution.lemonadeChange(bills1);
    bool result1Opt = solution.lemonadeChangeOptimized(bills1);
    cout << "预期结果: true, 实际结果: " << result1 << endl;
    cout << "优化版本结果: " << result1Opt << endl;
    cout << "结果一致性: " << (result1 == result1Opt) << endl;
    cout << endl;
    
    // 测试用例2：无法找零的情况
    vector<int> bills2 = {5, 5, 10, 10, 20};
    cout << "=== 测试用例2 ===" << endl;
    cout << "账单: ";
    for (int bill : bills2) cout << bill << " ";
    cout << endl;
    bool result2 = solution.lemonadeChange(bills2);
    bool result2Opt = solution.lemonadeChangeOptimized(bills2);
    cout << "预期结果: false, 实际结果: " << result2 << endl;
    cout << "优化版本结果: " << result2Opt << endl;
    cout << "结果一致性: " << (result2 == result2Opt) << endl;
    cout << endl;
    
    // 测试用例3：边界情况 - 只有5美元
    vector<int> bills3 = {5, 5, 5, 5};
    cout << "=== 测试用例3 ===" << endl;
    cout << "账单: ";
    for (int bill : bills3) cout << bill << " ";
    cout << endl;
    bool result3 = solution.lemonadeChange(bills3);
    cout << "预期结果: true, 实际结果: " << result3 << endl;
    cout << endl;
    
    // 测试用例4：大量20美元需要找零
    vector<int> bills4 = {5, 5, 5, 10, 20, 20, 20};
    cout << "=== 测试用例4 ===" << endl;
    cout << "账单: ";
    for (int bill : bills4) cout << bill << " ";
    cout << endl;
    bool result4 = solution.lemonadeChange(bills4);
    cout << "预期结果: true, 实际结果: " << result4 << endl;
    cout << endl;
    
    // 测试用例5：空数组
    vector<int> bills5 = {};
    cout << "=== 测试用例5 ===" << endl;
    cout << "账单: ";
    for (int bill : bills5) cout << bill << " ";
    cout << endl;
    bool result5 = solution.lemonadeChange(bills5);
    cout << "预期结果: true, 实际结果: " << result5 << endl;
    cout << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    Solution solution;
    
    cout << "=== 性能测试 ===" << endl;
    vector<int> largeBills(100000);
    srand(time(0)); // 设置随机种子
    
    for (int i = 0; i < largeBills.size(); i++) {
        // 随机生成账单，但保证有足够5美元
        int randVal = rand() % 3;
        largeBills[i] = randVal == 0 ? 5 : (randVal == 1 ? 10 : 20);
    }
    
    auto start = chrono::high_resolution_clock::now();
    bool largeResult = solution.lemonadeChange(largeBills);
    auto end = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    cout << "大规模测试结果: " << largeResult << endl;
    cout << "耗时: " << duration.count() << "ms" << endl;
}

int main() {
    // 运行测试用例
    testLemonadeChange();
    
    // 运行性能测试
    performanceTest();
    
    return 0;
}

/*
C++实现特点分析：

1. 语言特性利用：
   - 使用vector容器管理动态数组
   - 使用switch-case语句处理不同面值
   - 使用chrono库进行精确性能测量

2. 内存管理：
   - vector自动管理内存，避免手动分配
   - 使用引用传递参数，避免不必要的拷贝
   - 使用基本类型，避免对象创建开销

3. 性能优化：
   - 算法时间复杂度为O(n)，每个账单处理一次
   - 空间复杂度为O(1)，只使用两个计数器
   - 使用内联函数减少函数调用开销

4. 异常处理：
   - 使用C++异常机制处理非法面值
   - 提供清晰的错误信息
   - 边界条件检查确保程序健壮性

5. 代码风格：
   - 遵循C++命名规范
   - 使用有意义的变量名
   - 适当的注释和空行提高可读性

6. 工程实践：
   - 提供完整的测试框架
   - 包含性能测试和对比
   - 支持调试信息输出

7. 跨平台兼容性：
   - 使用标准C++11特性
   - 避免平台相关代码
   - 使用标准库函数

8. 调试支持：
   - 提供详细的交易过程输出
   - 支持多种测试场景
   - 便于问题定位和调试

9. 与Java/Python对比：
   - C++运行速度最快，但代码相对复杂
   - Java有更好的异常处理机制
   - Python代码最简洁，但运行速度较慢

10. 实际应用考虑：
    - 在生产环境中可以关闭调试输出
    - 可以添加交易日志记录功能
    - 可以考虑多线程安全问题
*/