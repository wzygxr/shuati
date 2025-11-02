// LeetCode 339. Nested List Weight Sum (嵌套列表权重和)
// 来源: LeetCode
// 网址: https://leetcode.cn/problems/nested-list-weight-sum/
// 
// 题目描述:
// 给定一个嵌套的整数列表 nestedList，每个元素要么是整数，要么是列表。同时，列表中元素同样也可以是整数或者是另一个列表。
// 整数的权重是其深度，返回该列表的加权和。
// 
// 示例:
// 输入: [[1,1],2,[1,1]]
// 输出: 10
// 解释: 因为这个列表中有四个深度为 2 的 1 ，和一个深度为 1 的 2。
// 4*1*2 + 1*2*1 = 8 + 2 = 10
// 
// 解题思路:
// 使用递归处理嵌套结构，对每个元素进行深度遍历，累加每个整数与其深度的乘积。
// 
// 时间复杂度: O(n)，其中n是所有整数元素的总数
// 空间复杂度: O(d)，其中d是嵌套列表的最大深度，递归调用栈的深度

#include <iostream>
#include <vector>
#include <variant>
using namespace std;

// 在C++中，我们需要定义一个结构体来模拟NestedInteger
struct NestedInteger {
    bool isIntegerVal;
    int integerVal;
    vector<NestedInteger> listVal;
    
    // 构造整数
    NestedInteger(int val) : isIntegerVal(true), integerVal(val) {}
    
    // 构造空列表
    NestedInteger() : isIntegerVal(false) {}
    
    // 添加元素到列表
    void add(const NestedInteger& ni) {
        isIntegerVal = false;
        listVal.push_back(ni);
    }
    
    bool isInteger() const { return isIntegerVal; }
    
    int getInteger() const { return integerVal; }
    
    const vector<NestedInteger>& getList() const { return listVal; }
};

class Solution {
public:
    int depthSum(vector<NestedInteger>& nestedList) {
        // 从深度1开始递归计算
        return dfs(nestedList, 1);
    }
private:
    // 递归深度优先搜索函数
    // nestedList: 当前嵌套列表
    // depth: 当前深度
    // return: 当前嵌套列表的加权和
    int dfs(const vector<NestedInteger>& nestedList, int depth) {
        int sum = 0;
        // 遍历当前列表中的每个元素
        for (const auto& ni : nestedList) {
            if (ni.isInteger()) {
                // 如果是整数，累加其值乘以深度
                sum += ni.getInteger() * depth;
            } else {
                // 如果是列表，递归计算其加权和，深度加1
                sum += dfs(ni.getList(), depth + 1);
            }
        }
        return sum;
    }
};

// 测试函数
int main() {
    Solution solution;
    
    // 测试用例1: [[1,1],2,[1,1]]
    vector<NestedInteger> testCase1;
    // [1,1]
    NestedInteger list1;
    list1.add(NestedInteger(1));
    list1.add(NestedInteger(1));
    testCase1.push_back(list1);
    // 2
    testCase1.push_back(NestedInteger(2));
    // [1,1]
    NestedInteger list2;
    list2.add(NestedInteger(1));
    list2.add(NestedInteger(1));
    testCase1.push_back(list2);
    
    cout << "测试用例1:" << endl;
    cout << "输入: [[1,1],2,[1,1]]" << endl;
    cout << "输出: " << solution.depthSum(testCase1) << endl;
    cout << "期望: 10" << endl;
    cout << endl;
    
    // 测试用例2: [1,[4,[6]]]
    vector<NestedInteger> testCase2;
    // 1
    testCase2.push_back(NestedInteger(1));
    // [4,[6]]
    NestedInteger outerList;
    outerList.add(NestedInteger(4));
    // [6]
    NestedInteger innerList;
    innerList.add(NestedInteger(6));
    outerList.add(innerList);
    testCase2.push_back(outerList);
    
    cout << "测试用例2:" << endl;
    cout << "输入: [1,[4,[6]]]" << endl;
    cout << "输出: " << solution.depthSum(testCase2) << endl;
    cout << "期望: 27" << endl;
    cout << endl;
    
    // 测试用例3: []
    vector<NestedInteger> testCase3;
    cout << "测试用例3:" << endl;
    cout << "输入: []" << endl;
    cout << "输出: " << solution.depthSum(testCase3) << endl;
    cout << "期望: 0" << endl;
    cout << endl;
    
    // 测试用例4: [10, [5, -3]]
    vector<NestedInteger> testCase4;
    testCase4.push_back(NestedInteger(10));
    NestedInteger list3;
    list3.add(NestedInteger(5));
    list3.add(NestedInteger(-3));
    testCase4.push_back(list3);
    
    cout << "测试用例4:" << endl;
    cout << "输入: [10, [5, -3]]" << endl;
    cout << "输出: " << solution.depthSum(testCase4) << endl;
    // 计算期望结果: 10*1 + 5*2 + (-3)*2 = 10 + 10 - 6 = 14
    cout << "期望: 14" << endl;
    
    return 0;
}