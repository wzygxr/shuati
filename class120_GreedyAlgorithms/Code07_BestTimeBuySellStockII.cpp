// 买卖股票的最佳时机 II
// 给你一个整数数组 prices ，其中 prices[i] 表示某支股票第 i 天的价格
// 在每一天，你可以决定是否购买和/或出售股票。你在任何时候 最多 只能持有 一股 股票
// 你也可以先购买，然后在 同一天 出售
// 返回 你能获得的最大利润
// 测试链接 : https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/

#include <iostream>
#include <vector>
#include <string>
#include <sstream>

using namespace std;

/**
 * 使用贪心算法解决股票买卖问题
 * 
 * 解题思路:
 * 贪心算法 + 累加正收益
 * 1. 将问题转化为每天的收益，只要收益为正就累加
 * 2. 只要明天价格比今天高，就在今天买入明天卖出
 * 3. 累加所有正的收益差值
 * 
 * 时间复杂度分析:
 * O(n) - 只需要遍历一次数组
 * 
 * 空间复杂度分析:
 * O(1) - 只使用了常数级别的额外空间
 * 
 * 是否为最优解:
 * 是，这是解决该问题的最优解
 * 
 * 工程化考量:
 * 1. 边界条件处理: 空数组、单个元素数组等特殊情况
 * 2. 输入验证: 检查输入是否为有效数组
 * 3. 异常处理: 对非法输入进行检查
 * 4. 可读性: 添加详细注释和变量命名
 * 
 * 跨语言特性:
 * C++版本使用了vector容器，与Java的ArrayList和Python的list类似
 * 但C++提供了更多的内存控制能力
 * 
 * 调试技巧:
 * 1. 对于错误情况，可以使用cout打印中间变量值
 * 2. 关键循环中可以添加断点观察maxProfit的变化
 * 
 * 与标准库对比:
 * 标准库中的算法通常经过高度优化，但对于这类简单问题，自定义实现效率相当
 */
int maxProfit(vector<int>& prices) {
    // 输入验证：处理边界情况
    if (prices.empty() || prices.size() == 1) {
        return 0;  // 没有交易日或只有一天，无法获得利润
    }
    
    int maxProfit = 0;  // 累计最大利润
    
    // 遍历数组，累加所有正的收益差值
    for (int i = 1; i < prices.size(); i++) {
        // 贪心策略：只要今天价格比昨天高，就累加差值作为利润
        if (prices[i] > prices[i - 1]) {
            maxProfit += prices[i] - prices[i - 1];
        }
    }
    
    return maxProfit;
}

/**
 * 打印数组的辅助函数
 */
string printVector(const vector<int>& v) {
    stringstream ss;
    ss << "[";
    for (size_t i = 0; i < v.size(); i++) {
        ss << v[i];
        if (i < v.size() - 1) {
            ss << ", ";
        }
    }
    ss << "]";
    return ss.str();
}

/**
 * 测试函数：测试各种边界条件和典型用例
 */
void test_maxProfit() {
    // 测试用例1: [7,1,5,3,6,4]
    vector<int> prices1 = {7, 1, 5, 3, 6, 4};
    int result1 = maxProfit(prices1);
    cout << "输入: " << printVector(prices1) << endl;
    cout << "输出: " << result1 << endl;
    cout << "预期: 7" << endl;
    cout << endl;
    
    // 测试用例2: [1,2,3,4,5] - 单调递增数组
    vector<int> prices2 = {1, 2, 3, 4, 5};
    int result2 = maxProfit(prices2);
    cout << "输入: " << printVector(prices2) << endl;
    cout << "输出: " << result2 << endl;
    cout << "预期: 4" << endl;
    cout << endl;
    
    // 测试用例3: [7,6,4,3,1] - 单调递减数组
    vector<int> prices3 = {7, 6, 4, 3, 1};
    int result3 = maxProfit(prices3);
    cout << "输入: " << printVector(prices3) << endl;
    cout << "输出: " << result3 << endl;
    cout << "预期: 0" << endl;
    cout << endl;
    
    // 测试用例4: 空数组
    vector<int> prices4 = {};
    int result4 = maxProfit(prices4);
    cout << "输入: []" << endl;
    cout << "输出: " << result4 << endl;
    cout << "预期: 0" << endl;
    cout << endl;
    
    // 测试用例5: 单个元素
    vector<int> prices5 = {1};
    int result5 = maxProfit(prices5);
    cout << "输入: [1]" << endl;
    cout << "输出: " << result5 << endl;
    cout << "预期: 0" << endl;
    cout << endl;
    
    // 测试用例6: 极端大数组（模拟，使用小规模数组表示）
    vector<int> prices6 = {1, 3, 2, 8, 4, 9};
    int result6 = maxProfit(prices6);
    cout << "输入: " << printVector(prices6) << endl;
    cout << "输出: " << result6 << endl;
    cout << "预期: 13" << endl;
    cout << endl;
}

/**
 * 主函数：运行测试
 */
int main() {
    cout << "买卖股票的最佳时机 II - 贪心算法解决方案" << endl;
    cout << "=====================================" << endl;
    test_maxProfit();
    return 0;
}