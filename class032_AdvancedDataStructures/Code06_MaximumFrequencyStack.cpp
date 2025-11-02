#include <unordered_map>
#include <vector>
#include <stdexcept>
#include <iostream>

// 最大频率栈
/*
 * 一、题目解析
 * 实现一个类似栈的数据结构，支持以下操作：
 * 1. push(val): 将一个整数val压入栈顶
 * 2. pop(): 删除并返回栈中出现频率最高的元素
 *    如果出现频率最高的元素不只一个，则移除并返回最接近栈顶的元素
 * 
 * 二、算法思路
 * 使用两个哈希表来维护数据：
 * 1. valueTimes: 记录每个值的出现频率
 * 2. cntValues: 记录每个频率对应的值列表（使用vector实现）
 * 3. topTimes: 记录当前最大频率
 * 
 * push操作：
 * 1. 更新值的频率
 * 2. 将值添加到对应频率的列表中
 * 3. 更新最大频率
 * 
 * pop操作：
 * 1. 从最大频率对应的列表中移除最后一个元素
 * 2. 更新该元素的频率
 * 3. 如果最大频率列表为空，则减少最大频率
 * 
 * 三、时间复杂度分析
 * push操作: O(1) - 哈希表操作和列表操作都是O(1)
 * pop操作: O(1) - 哈希表操作和列表操作都是O(1)
 * 
 * 四、空间复杂度分析
 * O(n) - n为push操作的次数，需要存储所有元素及其频率信息
 * 
 * 五、工程化考量
 * 1. 异常处理: 处理空栈的pop操作
 * 2. 边界场景: 空栈、单元素栈等
 * 3. 内存管理: C++需要手动管理内存
 * 
 * 六、相关题目扩展
 * 1. LeetCode 895. 最大频率栈 (本题)
 * 2. 牛客网: 最大频率栈
 * 3. 剑指Offer相关栈题目
 */

class FreqStack {
private:
    // 出现的最大次数
    int topTimes;
    // 每层节点，频率到值列表的映射
    std::unordered_map<int, std::vector<int>> cntValues;
    // 每一个数出现了几次，值到频率的映射
    std::unordered_map<int, int> valueTimes;

public:
    // 构造函数
    FreqStack() : topTimes(0) {}
    
    /*
     * 压入元素到栈中
     * @param val 要压入的元素
     * 时间复杂度: O(1)
     */
    void push(int val) {
        // 更新值的频率
        valueTimes[val] = valueTimes.count(val) ? valueTimes[val] + 1 : 1;
        int curTopTimes = valueTimes[val];
        // 将值添加到对应频率的列表中
        cntValues[curTopTimes].push_back(val);
        // 更新最大频率
        topTimes = std::max(topTimes, curTopTimes);
    }

    /*
     * 弹出频率最高的元素
     * @return 频率最高的元素，如果有多个则返回最接近栈顶的
     * 时间复杂度: O(1)
     */
    int pop() {
        // 检查栈是否为空
        if (topTimes == 0) {
            throw std::runtime_error("栈为空，无法执行pop操作");
        }
        // 从最大频率对应的列表中移除最后一个元素
        std::vector<int>& topTimeValues = cntValues[topTimes];
        int ans = topTimeValues.back();
        topTimeValues.pop_back();
        // 如果最大频率列表为空，则减少最大频率
        if (topTimeValues.empty()) {
            cntValues.erase(topTimes--);
        }
        // 更新弹出元素的频率
        int times = valueTimes[ans];
        if (times == 1) {
            valueTimes.erase(ans);
        } else {
            valueTimes[ans] = times - 1;
        }
        return ans;
    }
};

// 测试代码
int main() {
    FreqStack freqStack;
    
    // 测试用例: ["FreqStack","push","push","push","push","push","push","pop","pop","pop","pop"]
    //           [[],[5],[7],[5],[7],[4],[5],[],[],[],[]]
    
    freqStack.push(5); // 堆栈为 [5]
    freqStack.push(7); // 堆栈是 [5,7]
    freqStack.push(5); // 堆栈是 [5,7,5]
    freqStack.push(7); // 堆栈是 [5,7,5,7]
    freqStack.push(4); // 堆栈是 [5,7,5,7,4]
    freqStack.push(5); // 堆栈是 [5,7,5,7,4,5]
    
    std::cout << "pop(): " << freqStack.pop() << std::endl; // 返回 5
    std::cout << "pop(): " << freqStack.pop() << std::endl; // 返回 7
    std::cout << "pop(): " << freqStack.pop() << std::endl; // 返回 5
    std::cout << "pop(): " << freqStack.pop() << std::endl; // 返回 4
    
    return 0;
}