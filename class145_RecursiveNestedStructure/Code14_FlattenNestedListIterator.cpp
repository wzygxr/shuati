// LeetCode 341. Flatten Nested List Iterator (扁平化嵌套列表迭代器)
// 来源: LeetCode
// 网址: https://leetcode.cn/problems/flatten-nested-list-iterator/
// 
// 题目描述:
// 给你一个嵌套的整数列表 nestedList 。每个元素要么是一个整数，要么是一个列表；该列表的元素也可能是整数或者是其他列表。
// 请你实现一个迭代器将其扁平化，使之能够遍历这个列表中的所有整数。
// 实现扁平迭代器接口 NestedIterator ：
// - NestedIterator(List<NestedInteger> nestedList) 用嵌套列表 nestedList 初始化迭代器。
// - int next() 返回嵌套列表的下一个整数。
// - boolean hasNext() 如果仍然存在待迭代的整数，返回 true ；否则，返回 false 。
// 
// 示例:
// 输入: nestedList = [[1,1],2,[1,1]]
// 输出: [1,1,2,1,1]
// 解释: 通过重复调用 next() 直到 hasNext() 返回 false，next() 返回的元素的顺序应该是: [1,1,2,1,1]。
// 
// 解题思路:
// 方法1: 预计算所有整数并存储在列表中
// 方法2: 使用栈进行惰性计算（惰性迭代器）
// 这里使用方法2，更节省空间，符合迭代器的惰性计算原则
// 
// 时间复杂度:
// - 构造函数: O(k)，其中k是嵌套列表中整数的总数量
// - next(): O(1)
// - hasNext(): O(1)，最坏情况下可能为O(k)，但均摊分析仍为O(1)
// 
// 空间复杂度: O(d)，其中d是嵌套列表的最大深度

#include <iostream>
#include <vector>
#include <stack>
#include <memory>
using namespace std;

// 这是题目给定的接口，在实际提交时不需要实现
class NestedInteger {
public:
    // 判断是否为整数
    bool isInteger() const;
    
    // 获取整数值
    int getInteger() const;
    
    // 获取嵌套列表
    const vector<NestedInteger> &getList() const;
};

// 为了测试创建的示例实现类
class NestedIntegerImpl : public NestedInteger {
private:
    bool isIntegerValue;
    int integerValue;
    vector<NestedInteger> listValue;
public:
    // 构造整数类型的NestedInteger
    NestedIntegerImpl(int val) : isIntegerValue(true), integerValue(val) {}
    
    // 构造列表类型的NestedInteger
    NestedIntegerImpl() : isIntegerValue(false) {}
    
    // 向列表中添加元素
    void add(const NestedIntegerImpl& ni) {
        isIntegerValue = false;
        listValue.push_back(ni);
    }
    
    bool isInteger() const override {
        return isIntegerValue;
    }
    
    int getInteger() const override {
        return integerValue;
    }
    
    const vector<NestedInteger>& getList() const override {
        return listValue;
    }
};

// 定义用于栈中存储的结构体，包含迭代器和结束标记
struct IteratorPair {
    vector<NestedInteger>::const_iterator current;
    vector<NestedInteger>::const_iterator end;
    
    IteratorPair(const vector<NestedInteger>& list) 
        : current(list.begin()), end(list.end()) {}
};

class NestedIterator {
private:
    // 使用栈存储嵌套列表的迭代器对
    stack<IteratorPair> iteratorStack;
    // 缓存下一个整数
    int nextInteger;
    // 是否有下一个整数
    bool hasNextInteger;
    
    // 查找下一个整数
    void findNextInteger() {
        hasNextInteger = false;
        
        while (!iteratorStack.empty()) {
            IteratorPair& top = iteratorStack.top();
            
            // 如果当前迭代器已经遍历完，弹出栈顶
            if (top.current == top.end) {
                iteratorStack.pop();
                continue;
            }
            
            // 获取当前嵌套整数
            const NestedInteger& ni = *top.current;
            // 移动迭代器到下一个位置
            ++top.current;
            
            if (ni.isInteger()) {
                // 找到一个整数
                nextInteger = ni.getInteger();
                hasNextInteger = true;
                break;
            } else {
                // 将嵌套列表的迭代器对压入栈中
                const vector<NestedInteger>& list = ni.getList();
                if (!list.empty()) {
                    iteratorStack.push(IteratorPair(list));
                }
            }
        }
    }
public:
    // 构造函数
    NestedIterator(const vector<NestedInteger>& nestedList) {
        if (!nestedList.empty()) {
            iteratorStack.push(IteratorPair(nestedList));
        }
        // 预先查找第一个整数
        findNextInteger();
    }
    
    // 获取下一个整数
    int next() {
        if (!hasNext()) {
            throw runtime_error("No more integers in the nested list");
        }
        
        // 保存当前的nextInteger
        int result = nextInteger;
        // 查找下一个整数
        findNextInteger();
        
        return result;
    }
    
    // 判断是否还有下一个整数
    bool hasNext() {
        return hasNextInteger;
    }
};

/**
 * 方法2: 预计算所有整数并存储在列表中
 * 这是一个更简单但可能占用更多空间的实现
 */
class PreComputeNestedIterator {
private:
    vector<int> flattenedList;
    size_t index;
    
    // 递归展平嵌套列表
    void flatten(const vector<NestedInteger>& nestedList) {
        for (const auto& ni : nestedList) {
            if (ni.isInteger()) {
                flattenedList.push_back(ni.getInteger());
            } else {
                flatten(ni.getList());
            }
        }
    }
public:
    PreComputeNestedIterator(const vector<NestedInteger>& nestedList) {
        index = 0;
        // 预先展平整个嵌套列表
        flatten(nestedList);
    }
    
    int next() {
        if (!hasNext()) {
            throw runtime_error("No more elements");
        }
        return flattenedList[index++];
    }
    
    bool hasNext() {
        return index < flattenedList.size();
    }
};

// 辅助函数：将NestedIntegerImpl转换为NestedInteger的列表
vector<NestedInteger> toNestedIntegerList(const vector<NestedIntegerImpl>& list) {
    vector<NestedInteger> result;
    for (const auto& impl : list) {
        result.push_back(impl);
    }
    return result;
}

// 测试函数
int main() {
    // 测试用例1: [[1,1],2,[1,1]]
    vector<NestedIntegerImpl> testCase1;
    
    // [1,1]
    NestedIntegerImpl list1;
    list1.add(NestedIntegerImpl(1));
    list1.add(NestedIntegerImpl(1));
    testCase1.push_back(list1);
    
    // 2
    testCase1.push_back(NestedIntegerImpl(2));
    
    // [1,1]
    NestedIntegerImpl list2;
    list2.add(NestedIntegerImpl(1));
    list2.add(NestedIntegerImpl(1));
    testCase1.push_back(list2);
    
    // 转换为接口要求的类型
    vector<NestedInteger> testCase1Converted = toNestedIntegerList(testCase1);
    
    cout << "测试用例1 (惰性迭代器):" << endl;
    cout << "输入: [[1,1],2,[1,1]]" << endl;
    cout << "输出: [";
    NestedIterator iterator1(testCase1Converted);
    bool first = true;
    while (iterator1.hasNext()) {
        if (!first) {
            cout << ", ";
        }
        cout << iterator1.next();
        first = false;
    }
    cout << "]" << endl;
    cout << "期望: [1, 1, 2, 1, 1]" << endl;
    cout << endl;
    
    // 重置测试用例
    vector<NestedIntegerImpl> testCase1Again;
    NestedIntegerImpl list1Again;
    list1Again.add(NestedIntegerImpl(1));
    list1Again.add(NestedIntegerImpl(1));
    testCase1Again.push_back(list1Again);
    testCase1Again.push_back(NestedIntegerImpl(2));
    NestedIntegerImpl list2Again;
    list2Again.add(NestedIntegerImpl(1));
    list2Again.add(NestedIntegerImpl(1));
    testCase1Again.push_back(list2Again);
    
    vector<NestedInteger> testCase1AgainConverted = toNestedIntegerList(testCase1Again);
    
    cout << "测试用例1 (预计算迭代器):" << endl;
    cout << "输出: [";
    PreComputeNestedIterator preIterator1(testCase1AgainConverted);
    first = true;
    while (preIterator1.hasNext()) {
        if (!first) {
            cout << ", ";
        }
        cout << preIterator1.next();
        first = false;
    }
    cout << "]" << endl;
    cout << "期望: [1, 1, 2, 1, 1]" << endl;
    cout << endl;
    
    // 测试用例2: [1,[4,[6]]]
    vector<NestedIntegerImpl> testCase2;
    testCase2.push_back(NestedIntegerImpl(1));
    
    NestedIntegerImpl outerList;
    outerList.add(NestedIntegerImpl(4));
    
    NestedIntegerImpl innerList;
    innerList.add(NestedIntegerImpl(6));
    outerList.add(innerList);
    
    testCase2.push_back(outerList);
    
    vector<NestedInteger> testCase2Converted = toNestedIntegerList(testCase2);
    
    cout << "测试用例2 (惰性迭代器):" << endl;
    cout << "输入: [1,[4,[6]]]" << endl;
    cout << "输出: [";
    NestedIterator iterator2(testCase2Converted);
    first = true;
    while (iterator2.hasNext()) {
        if (!first) {
            cout << ", ";
        }
        cout << iterator2.next();
        first = false;
    }
    cout << "]" << endl;
    cout << "期望: [1, 4, 6]" << endl;
    
    return 0;
}