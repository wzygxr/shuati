// LeetCode 364. Nested List Weight Sum II (嵌套列表权重和 II)
// 来源: LeetCode
// 网址: https://leetcode.cn/problems/nested-list-weight-sum-ii/
// 
// 题目描述:
// 给定一个嵌套的整数列表 nestedList，每个元素要么是整数，要么是列表。同时，列表中元素同样也可以是整数或者是另一个列表。
// 整数的权重与其深度成反比，深度最大的整数权重为 1，深度第二大的整数权重为 2，依此类推。
// 返回该列表的加权和。
// 
// 示例:
// 输入: [[1,1],2,[1,1]]
// 输出: 8
// 解释: 四个 1 位于深度为 1 的位置，一个 2 位于深度为 2 的位置。
// 4*1*1 + 1*2*2 = 4 + 4 = 8
// 
// 解题思路:
// 方法1：先计算最大深度，然后使用深度的倒数作为权重
// 方法2：使用迭代方法，每遍历一层，累加当前层的和，并将其加入下一层的权重计算
// 这里使用方法2，更高效且简洁
// 
// 时间复杂度: O(n)，其中n是所有整数元素的总数
// 空间复杂度: O(d)，其中d是嵌套列表的最大深度

#include <iostream>
#include <vector>
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
    // 迭代方法计算反向加权和
    int depthSumInverse(vector<NestedInteger>& nestedList) {
        int sum = 0; // 当前所有层的和
        int weightedSum = 0; // 最终的加权和
        
        // 当嵌套列表不为空时，继续处理
        while (!nestedList.empty()) {
            vector<NestedInteger> nextLevel;
            int levelSum = 0;
            
            // 处理当前层的所有元素
            for (const auto& ni : nestedList) {
                if (ni.isInteger()) {
                    // 如果是整数，加到当前层的和中
                    levelSum += ni.getInteger();
                } else {
                    // 如果是列表，将其元素加入下一层
                    nextLevel.insert(nextLevel.end(), ni.getList().begin(), ni.getList().end());
                }
            }
            
            // 将当前层的和累加到总和中，这样每增加一层，前面层的和就会被多计算一次
            // 这等价于将权重设置为(最大深度 - 当前深度 + 1)
            sum += levelSum;
            weightedSum += sum;
            
            // 处理下一层
            nestedList = move(nextLevel);
        }
        
        return weightedSum;
    }
    
    // 递归方法计算反向加权和
    int depthSumInverseRecursive(vector<NestedInteger>& nestedList) {
        // 步骤1: 计算最大深度
        int maxDepth = getMaxDepth(nestedList);
        
        // 步骤2: 使用递归计算加权和，权重 = maxDepth - depth + 1
        return dfs(nestedList, 1, maxDepth);
    }
private:
    // 计算嵌套列表的最大深度
    int getMaxDepth(const vector<NestedInteger>& nestedList) {
        if (nestedList.empty()) {
            return 0;
        }
        
        int maxDepth = 0;
        for (const auto& ni : nestedList) {
            if (ni.isInteger()) {
                maxDepth = max(maxDepth, 1);
            } else {
                maxDepth = max(maxDepth, 1 + getMaxDepth(ni.getList()));
            }
        }
        
        return maxDepth;
    }
    
    // 递归深度优先搜索函数
    int dfs(const vector<NestedInteger>& nestedList, int currentDepth, int maxDepth) {
        int sum = 0;
        int weight = maxDepth - currentDepth + 1;
        
        for (const auto& ni : nestedList) {
            if (ni.isInteger()) {
                sum += ni.getInteger() * weight;
            } else {
                sum += dfs(ni.getList(), currentDepth + 1, maxDepth);
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
    
    cout << "测试用例1 (迭代方法):" << endl;
    cout << "输入: [[1,1],2,[1,1]]" << endl;
    cout << "输出: " << solution.depthSumInverse(testCase1) << endl;
    cout << "期望: 8" << endl;
    cout << endl;
    
    // 重新创建测试用例，因为上面的testCase1已经被修改
    vector<NestedInteger> testCase1Recursive;
    NestedInteger list1Recursive;
    list1Recursive.add(NestedInteger(1));
    list1Recursive.add(NestedInteger(1));
    testCase1Recursive.push_back(list1Recursive);
    testCase1Recursive.push_back(NestedInteger(2));
    NestedInteger list2Recursive;
    list2Recursive.add(NestedInteger(1));
    list2Recursive.add(NestedInteger(1));
    testCase1Recursive.push_back(list2Recursive);
    
    cout << "测试用例1 (递归方法):" << endl;
    cout << "输入: [[1,1],2,[1,1]]" << endl;
    cout << "输出: " << solution.depthSumInverseRecursive(testCase1Recursive) << endl;
    cout << "期望: 8" << endl;
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
    
    cout << "测试用例2 (迭代方法):" << endl;
    cout << "输入: [1,[4,[6]]]" << endl;
    cout << "输出: " << solution.depthSumInverse(testCase2) << endl;
    // 计算期望结果: 1*3 + 4*2 + 6*1 = 3 + 8 + 6 = 17
    cout << "期望: 17" << endl;
    cout << endl;
    
    // 重新创建测试用例，因为上面的testCase2已经被修改
    vector<NestedInteger> testCase2Recursive;
    testCase2Recursive.push_back(NestedInteger(1));
    NestedInteger outerListRecursive;
    outerListRecursive.add(NestedInteger(4));
    NestedInteger innerListRecursive;
    innerListRecursive.add(NestedInteger(6));
    outerListRecursive.add(innerListRecursive);
    testCase2Recursive.push_back(outerListRecursive);
    
    cout << "测试用例2 (递归方法):" << endl;
    cout << "输入: [1,[4,[6]]]" << endl;
    cout << "输出: " << solution.depthSumInverseRecursive(testCase2Recursive) << endl;
    cout << "期望: 17" << endl;
    
    return 0;
}