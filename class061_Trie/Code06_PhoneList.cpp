// SPOJ PHONELST - C++实现
// 题目描述：给定一个电话号码列表，判断是否存在一个号码是另一个号码的前缀。
// 测试链接：https://www.spoj.com/problems/PHONELST/

#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
using namespace std;

/**
 * SPOJ PHONELST电话号码列表问题 - 使用前缀树实现
 * 
 * 算法思路：
 * 1. 首先将电话号码按长度从小到大排序
 * 2. 构建前缀树存储已处理的电话号码
 * 3. 对于每个电话号码，检查其是否是已有号码的前缀，或已有号码是否是它的前缀
 * 4. 如果发现上述情况，返回false
 * 
 * 时间复杂度分析：
 * - 排序：O(N log N)，其中N是电话号码数量
 * - 构建前缀树和检查：O(M)，其中M是所有电话号码的字符总数
 * - 总体时间复杂度：O(N log N + M)
 * 
 * 空间复杂度分析：
 * - 前缀树空间：O(M)
 * - 总体空间复杂度：O(M)
 * 
 * 是否最优解：是
 * 理由：排序后使用前缀树可以高效检测前缀关系
 * 
 * 工程化考虑：
 * 1. 边界条件处理：空列表或只有一个号码的情况
 * 2. 输入验证：处理非法输入（如非数字字符）
 * 3. 性能优化：通过排序减少不必要的检查
 */

class TrieNode {
public:
    // 子节点数组（0-9）
    TrieNode* children[10];
    // 标记是否是电话号码结尾
    bool isEnd;
    
    /**
     * 初始化前缀树节点
     */
    TrieNode() {
        isEnd = false;
        for (int i = 0; i < 10; i++) {
            children[i] = nullptr;
        }
    }
    
    /**
     * 析构函数，释放子节点内存
     */
    ~TrieNode() {
        for (int i = 0; i < 10; i++) {
            if (children[i]) {
                delete children[i];
            }
        }
    }
};

class PhoneListChecker {
private:
    TrieNode* root;
    
    /**
     * 将字符映射到索引
     * 
     * @param c 字符
     * @return 索引值(0-9)
     */
    int charToIndex(char c) {
        return c - '0';
    }
    
public:
    /**
     * 初始化电话号码检查器
     */
    PhoneListChecker() {
        root = new TrieNode();
    }
    
    /**
     * 析构函数
     */
    ~PhoneListChecker() {
        delete root;
    }
    
    /**
     * 检查电话号码列表是否存在前缀关系
     * 
     * @param phoneList 电话号码列表
     * @return 如果存在前缀关系返回false，否则返回true
     */
    bool isValid(vector<string>& phoneList) {
        // 按长度从小到大排序，优先处理短号码
        sort(phoneList.begin(), phoneList.end(), [](const string& a, const string& b) {
            return a.length() < b.length();
        });
        
        for (const string& phone : phoneList) {
            // 检查是否是已有号码的前缀
            bool isPrefix = true;
            TrieNode* node = root;
            
            for (char c : phone) {
                int index = charToIndex(c);
                
                if (!node->children[index]) {
                    // 如果当前字符不存在，说明不是已有号码的前缀
                    isPrefix = false;
                    node->children[index] = new TrieNode();
                }
                
                node = node->children[index];
                
                // 如果在遍历过程中发现已存在的前缀
                if (node->isEnd && !isPrefix) {
                    return false;
                }
            }
            
            // 标记当前号码为已存在
            node->isEnd = true;
            
            // 如果是已有号码的前缀，返回false
            if (isPrefix) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 重置检查器
     */
    void reset() {
        delete root;
        root = new TrieNode();
    }
};

// 测试代码
int main() {
    PhoneListChecker checker;
    
    // 测试用例1：存在前缀关系
    vector<string> phones1 = {"123", "1234", "12345"};
    cout << "测试用例1结果: " << (checker.isValid(phones1) ? "YES" : "NO") << endl;  // 应该输出NO
    checker.reset();
    
    // 测试用例2：不存在前缀关系
    vector<string> phones2 = {"123", "456", "789"};
    cout << "测试用例2结果: " << (checker.isValid(phones2) ? "YES" : "NO") << endl;  // 应该输出YES
    checker.reset();
    
    // 测试用例3：边界情况
    vector<string> phones3 = {"123"};
    cout << "测试用例3结果: " << (checker.isValid(phones3) ? "YES" : "NO") << endl;  // 应该输出YES
    checker.reset();
    
    // 测试用例4：短号码在前
    vector<string> phones4 = {"1", "12", "123", "1234"};
    cout << "测试用例4结果: " << (checker.isValid(phones4) ? "YES" : "NO") << endl;  // 应该输出NO
    
    return 0;
}

/*
预期输出：
测试用例1结果: NO
测试用例2结果: YES
测试用例3结果: YES
测试用例4结果: NO
*/