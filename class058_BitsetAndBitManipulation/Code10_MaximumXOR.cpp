// LeetCode 421. 数组中两个数的最大异或值
// 题目链接: https://leetcode-cn.com/problems/maximum-xor-of-two-numbers-in-an-array/
// 题目大意:
// 给你一个整数数组 nums ，返回 nums[i] XOR nums[j] 的最大运算结果，其中 0 ≤ i ≤ j < n 。
// 
// 进阶：你可以在 O(n) 的时间解决这个问题吗？
// 
// 示例 1:
// 输入: nums = [3,10,5,25,2,8]
// 输出: 28
// 解释: 最大的结果是 5 XOR 25 = 28.
// 
// 示例 2:
// 输入: nums = [0]
// 输出: 0
// 
// 示例 3:
// 输入: nums = [2,4]
// 输出: 6
// 
// 示例 4:
// 输入: nums = [8,10,2]
// 输出: 10
// 
// 示例 5:
// 输入: nums = [14,70,53,83,49,91,36,80,92,51,66,70]
// 输出: 127
// 
// 提示：
// 1 <= nums.length <= 2 * 10^4
// 0 <= nums[i] <= 2^31 - 1
//
// 解题思路:
// 方法一：暴力解法（O(n²)时间复杂度，不推荐）
// 方法二：前缀树（字典树）优化的位运算方法（O(n)时间复杂度）
// 方法三：基于异或性质的位运算方法（O(n)时间复杂度）
//
// 这里我们主要实现方法二和方法三，它们都是基于位运算的高效解法

#include <iostream>
#include <vector>
#include <unordered_set>
#include <algorithm>
#include <chrono>

// 方法一：暴力解法
int findMaximumXOR1(const std::vector<int>& nums) {
    if (nums.size() <= 1) {
        return 0;
    }
    
    int maxResult = 0;
    
    // 遍历所有可能的数对
    for (size_t i = 0; i < nums.size(); ++i) {
        for (size_t j = i + 1; j < nums.size(); ++j) {
            int currentXOR = nums[i] ^ nums[j];
            maxResult = std::max(maxResult, currentXOR);
        }
    }
    
    return maxResult;
}

// 方法二：基于位运算和集合的方法
int findMaximumXOR2(const std::vector<int>& nums) {
    if (nums.size() <= 1) {
        return 0;
    }
    
    int maxResult = 0;
    int mask = 0;
    
    // 从最高位到最低位依次确定结果的每一位
    for (int i = 30; i >= 0; --i) {  // 31位整数，忽略符号位（因为nums[i] >= 0）
        // 构建当前位的掩码
        mask |= (1 << i);
        
        // 存储所有数在当前掩码下的前缀
        std::unordered_set<int> prefixes;
        for (int num : nums) {
            prefixes.insert(num & mask);
        }
        
        // 假设当前位为1，构造可能的最大值
        int tempMax = maxResult | (1 << i);
        
        // 检查是否存在两个数，它们的前缀异或等于tempMax
        bool found = false;
        for (int prefix : prefixes) {
            // 如果prefix ^ target = tempMax，那么target = prefix ^ tempMax
            if (prefixes.count(prefix ^ tempMax)) {
                // 找到可行的解，设置当前位为1
                maxResult = tempMax;
                found = true;
                break;
            }
        }
        
        // 如果没有找到，当前位保持为0（maxResult不变）
        if (found) {
            // 可以继续处理下一位
        }
    }
    
    return maxResult;
}

// 方法三：前缀树（字典树）方法
class TrieNode {
public:
    TrieNode* children[2]; // 0和1两个子节点
    
    TrieNode() {
        children[0] = nullptr;
        children[1] = nullptr;
    }
    
    ~TrieNode() {
        // 递归释放内存
        if (children[0]) delete children[0];
        if (children[1]) delete children[1];
    }
};

int findMaximumXOR3(const std::vector<int>& nums) {
    if (nums.size() <= 1) {
        return 0;
    }
    
    // 构建前缀树
    TrieNode* root = new TrieNode();
    
    // 向前缀树中插入一个数
    auto insert = [](TrieNode* root, int num) {
        TrieNode* node = root;
        
        // 从最高位到最低位插入
        for (int i = 30; i >= 0; --i) {  // 31位整数，忽略符号位
            int bit = (num >> i) & 1;
            
            if (!node->children[bit]) {
                node->children[bit] = new TrieNode();
            }
            
            node = node->children[bit];
        }
    };
    
    // 在已有的前缀树中查找与给定数异或结果最大的数
    auto search = [](TrieNode* root, int num) -> int {
        TrieNode* node = root;
        int xorResult = 0;
        
        // 从最高位到最低位查找
        for (int i = 30; i >= 0; --i) {  // 31位整数，忽略符号位
            int bit = (num >> i) & 1;
            int desiredBit = 1 - bit; // 寻找相反的位以最大化异或结果
            
            if (node->children[desiredBit]) {
                // 可以找到相反的位，异或结果的当前位为1
                xorResult |= (1 << i);
                node = node->children[desiredBit];
            } else if (node->children[bit]) {
                // 找不到相反的位，只能使用相同的位
                node = node->children[bit];
            } else {
                // 两个子节点都不存在，提前结束
                break;
            }
        }
        
        return xorResult;
    };
    
    // 先插入第一个数
    insert(root, nums[0]);
    
    int maxResult = 0;
    
    // 对于每个数，插入并查找能产生最大异或值的数
    for (size_t i = 1; i < nums.size(); ++i) {
        maxResult = std::max(maxResult, search(root, nums[i]));
        insert(root, nums[i]);
    }
    
    // 释放内存
    delete root;
    
    return maxResult;
}

// 方法四：工程化版本，增加异常处理和参数验证
int findMaximumXORWithValidation(const std::vector<int>& nums) {
    try {
        // 参数验证（在C++中，传入空指针会导致未定义行为，所以这里假设nums有效）
        
        // 边界情况处理
        if (nums.size() <= 1) {
            return 0;
        }
        
        // 验证所有元素是否为非负数
        for (int num : nums) {
            if (num < 0) {
                throw std::invalid_argument("All elements must be non-negative integers");
            }
        }
        
        // 使用方法二实现
        return findMaximumXOR2(nums);
    }
    catch (const std::exception& e) {
        // 记录异常（在实际应用中可以使用日志）
        std::cerr << "Error in findMaximumXORWithValidation: " << e.what() << std::endl;
        // 异常情况下返回0
        return 0;
    }
}

// 单元测试
void runTests() {
    std::cout << "Running unit tests..." << std::endl;
    
    struct TestCase {
        std::vector<int> input;
        int expected;
    };
    
    std::vector<TestCase> testCases = {
        {{3, 10, 5, 25, 2, 8}, 28},
        {{0}, 0},
        {{2, 4}, 6},
        {{8, 10, 2}, 10},
        {{14, 70, 53, 83, 49, 91, 36, 80, 92, 51, 66, 70}, 127}
    };
    
    // 定义要测试的函数和名称
    struct Method {
        int (*func)(const std::vector<int>&);
        std::string name;
    };
    
    std::vector<Method> methods = {
        {findMaximumXOR1, "Method 1 (Brute Force)"},
        {findMaximumXOR2, "Method 2 (Bitwise with Set)"},
        {findMaximumXOR3, "Method 3 (Trie)"},
        {findMaximumXORWithValidation, "Method 4 (With Validation)"}
    };
    
    // 测试所有方法
    for (const auto& method : methods) {
        std::cout << "\n" << method.name << ":" << std::endl;
        for (const auto& test : testCases) {
            int result = method.func(test.input);
            const std::string& status = (result == test.expected) ? "PASS" : "FAIL";
            std::cout << "  Input: [";
            for (size_t i = 0; i < test.input.size(); ++i) {
                std::cout << test.input[i];
                if (i < test.input.size() - 1) std::cout << ", ";
            }
            std::cout << "] -> Result: " << result 
                      << " (Expected: " << test.expected 
                      << ") - " << status << std::endl;
        }
    }
}

// 性能测试
void performanceTest() {
    std::cout << "\nRunning performance tests..." << std::endl;
    
    // 生成大规模测试数据
    std::vector<int> largeNums;
    for (int i = 0; i < 10000; ++i) {
        largeNums.push_back(i);
    }
    
    // 仅在小规模数据上测试方法1
    std::vector<int> smallNums = {3, 10, 5, 25, 2, 8};
    auto start = std::chrono::high_resolution_clock::now();
    int result1 = findMaximumXOR1(smallNums);
    auto end = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
    std::cout << "Performance of Method 1 (Brute Force): " << duration << " μs" << std::endl;
    std::cout << "Result: " << result1 << std::endl;
    
    // 测试方法2
    start = std::chrono::high_resolution_clock::now();
    int result2 = findMaximumXOR2(largeNums);
    end = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
    std::cout << "\nPerformance of Method 2 (Bitwise with Set): " << duration << " μs" << std::endl;
    std::cout << "Result: " << result2 << std::endl;
    
    // 测试方法3
    start = std::chrono::high_resolution_clock::now();
    int result3 = findMaximumXOR3(largeNums);
    end = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
    std::cout << "\nPerformance of Method 3 (Trie): " << duration << " μs" << std::endl;
    std::cout << "Result: " << result3 << std::endl;
    
    // 测试方法4
    start = std::chrono::high_resolution_clock::now();
    int result4 = findMaximumXORWithValidation(largeNums);
    end = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
    std::cout << "\nPerformance of Method 4 (With Validation): " << duration << " μs" << std::endl;
    std::cout << "Result: " << result4 << std::endl;
}

// 主函数
int main() {
    std::cout << "LeetCode 421. 数组中两个数的最大异或值" << std::endl;
    std::cout << "Using bitwise operations for optimization" << std::endl;
    
    // 运行单元测试
    runTests();
    
    // 运行性能测试
    performanceTest();
    
    // 复杂度分析
    std::cout << "\n复杂度分析:" << std::endl;
    std::cout << "方法一（暴力解法）:" << std::endl;
    std::cout << "  时间复杂度: O(n²)，其中n是数组长度" << std::endl;
    std::cout << "  空间复杂度: O(1)" << std::endl;
    std::cout << "  优点: 实现简单" << std::endl;
    std::cout << "  缺点: 对于大数组效率低" << std::endl;
    
    std::cout << "\n方法二（基于位运算和集合）:" << std::endl;
    std::cout << "  时间复杂度: O(n)，每个位处理需要O(n)时间，总共处理32个位" << std::endl;
    std::cout << "  空间复杂度: O(n)，用于存储前缀集合" << std::endl;
    std::cout << "  优点: 时间效率高，实现相对简单" << std::endl;
    
    std::cout << "\n方法三（前缀树）:" << std::endl;
    std::cout << "  时间复杂度: O(n)，构建树和查询都是O(n)时间" << std::endl;
    std::cout << "  空间复杂度: O(n)，用于存储前缀树" << std::endl;
    std::cout << "  优点: 位操作思想清晰，扩展性好" << std::endl;
    std::cout << "  注意: 实际实现中需要注意内存管理，避免内存泄漏" << std::endl;
    
    std::cout << "\n适用场景总结:" << std::endl;
    std::cout << "1. 对于小数组，可以使用暴力解法" << std::endl;
    std::cout << "2. 对于大数组，应使用方法二或方法三，它们的时间复杂度都是O(n)" << std::endl;
    std::cout << "3. 在工程实践中，方法二实现更简洁，而方法三更能体现位运算的思想" << std::endl;
    std::cout << "4. 当需要处理大量相似查询时，前缀树方法更具优势" << std::endl;
    
    return 0;
}