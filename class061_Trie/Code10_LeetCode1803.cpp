// LeetCode 1803. 统计异或值在范围内的数对有多少 - C++实现
// 
// 题目描述：
// 给定一个整数数组nums和两个整数low和high，统计有多少数对(i, j)满足i < j且low <= (nums[i] XOR nums[j]) <= high。
// 
// 测试链接：https://leetcode.cn/problems/count-pairs-with-xor-in-a-range/
// 
// 算法思路：
// 1. 使用前缀异或和与前缀树，通过两次查询（<=high和<low）得到结果
// 2. 构建二进制前缀树，支持统计异或值在特定范围内的数对数量
// 3. 利用前缀树的高效查询特性，避免暴力枚举
// 
// 时间复杂度分析：
// - 构建前缀树：O(N * 32)，其中N是数组长度，32是整数的位数
// - 查询过程：O(N * 32)
// - 总体时间复杂度：O(N * 32)
// 
// 空间复杂度分析：
// - 前缀树空间：O(N * 32)
// - 总体空间复杂度：O(N * 32)
// 
// 是否最优解：是
// 理由：使用前缀树可以在线性时间内统计异或值在范围内的数对数量
// 
// 工程化考虑：
// 1. 异常处理：处理空数组和非法范围
// 2. 边界情况：数组长度小于2或范围无效的情况
// 3. 极端输入：大量数据或大数值的情况
// 4. 内存管理：合理管理前缀树内存
// 
// 语言特性差异：
// C++：使用指针实现前缀树，性能高且节省空间
// Java：使用数组实现，更安全但空间固定
// Python：使用字典实现，代码更简洁
// 
// 调试技巧：
// 1. 使用小规模数据验证算法正确性
// 2. 打印中间结果调试查询过程
// 3. 单元测试覆盖各种边界条件

#include <iostream>
#include <vector>
#include <memory>
#include <cassert>
#include <chrono>
#include <random>
#include <climits>

using namespace std;

/**
 * 二进制前缀树节点类
 */
class BinaryTrieNode {
public:
    unique_ptr<BinaryTrieNode> children[2]; // 0和1两个子节点
    int count;
    
    BinaryTrieNode() : count(0) {
        children[0] = nullptr;
        children[1] = nullptr;
    }
};

/**
 * 二进制前缀树类
 */
class BinaryTrie {
private:
    unique_ptr<BinaryTrieNode> root;
    
public:
    BinaryTrie() : root(make_unique<BinaryTrieNode>()) {}
    
    /**
     * 向前缀树中插入数字
     */
    void insert(int num) {
        BinaryTrieNode* node = root.get();
        node->count++;
        
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1;
            if (node->children[bit] == nullptr) {
                node->children[bit] = make_unique<BinaryTrieNode>();
            }
            node = node->children[bit].get();
            node->count++;
        }
    }
    
    /**
     * 统计与num异或值<=target的数字数量
     */
    int countLessEqual(int num, int target) {
        if (target < 0) {
            return 0;
        }
        
        BinaryTrieNode* node = root.get();
        int count = 0;
        
        for (int i = 31; i >= 0; i--) {
            int numBit = (num >> i) & 1;
            int targetBit = (target >> i) & 1;
            int opposite = 1 - numBit;
            
            if (targetBit == 1) {
                // 如果target当前位为1，那么选择相同位的所有数字都满足条件
                if (node->children[numBit] != nullptr) {
                    count += node->children[numBit]->count;
                }
                // 继续在相反位搜索
                if (node->children[opposite] != nullptr) {
                    node = node->children[opposite].get();
                } else {
                    return count;
                }
            } else {
                // 如果target当前位为0，只能选择相同位
                if (node->children[numBit] != nullptr) {
                    node = node->children[numBit].get();
                } else {
                    return count;
                }
            }
        }
        
        // 处理最后一位
        count += node->count;
        return count;
    }
};

/**
 * 主函数：统计异或值在[low, high]范围内的数对数量
 */
int countPairs(vector<int>& nums, int low, int high) {
    if (nums.size() < 2) {
        return 0;
    }
    
    if (low > high) {
        return 0;
    }
    
    BinaryTrie trie;
    int count = 0;
    
    for (int num : nums) {
        // 查询与之前数字的异或值在[low, high]范围内的数量
        int highCount = trie.countLessEqual(num, high);
        int lowCount = trie.countLessEqual(num, low - 1);
        count += (highCount - lowCount);
        
        // 插入当前数字到前缀树
        trie.insert(num);
    }
    
    return count;
}

/**
 * 暴力解法（用于验证正确性）
 */
int countPairsBruteForce(vector<int>& nums, int low, int high) {
    if (nums.size() < 2) {
        return 0;
    }
    
    int count = 0;
    int n = nums.size();
    
    for (int i = 0; i < n; i++) {
        for (int j = i + 1; j < n; j++) {
            int xorVal = nums[i] ^ nums[j];
            if (xorVal >= low && xorVal <= high) {
                count++;
            }
        }
    }
    
    return count;
}

/**
 * 单元测试函数
 */
void testCountPairs() {
    // 测试用例1：基础测试
    vector<int> nums1 = {1, 4, 2, 7};
    int low1 = 2, high1 = 6;
    int result1 = countPairs(nums1, low1, high1);
    int expected1 = countPairsBruteForce(nums1, low1, high1);
    assert(result1 == expected1);
    
    // 测试用例2：空数组
    vector<int> nums2 = {};
    int result2 = countPairs(nums2, 0, 10);
    assert(result2 == 0);
    
    // 测试用例3：单个元素
    vector<int> nums3 = {5};
    int result3 = countPairs(nums3, 0, 10);
    assert(result3 == 0);
    
    // 测试用例4：无效范围
    vector<int> nums4 = {1, 2, 3};
    int result4 = countPairs(nums4, 5, 1);
    assert(result4 == 0);
    
    // 测试用例5：相同数字
    vector<int> nums5 = {1, 1, 1};
    int result5 = countPairs(nums5, 0, 0);
    int expected5 = countPairsBruteForce(nums5, 0, 0);
    assert(result5 == expected5);
    
    cout << "所有单元测试通过！" << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    // 生成大规模测试数据
    int n = 10000;
    vector<int> nums(n);
    
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<> dis(0, 1000000);
    
    for (int i = 0; i < n; i++) {
        nums[i] = dis(gen);
    }
    
    int low = 1000;
    int high = 10000;
    
    auto start = chrono::high_resolution_clock::now();
    int result = countPairs(nums, low, high);
    auto end = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "优化算法结果: " << result << " 个数对" << endl;
    cout << "优化算法耗时: " << duration.count() << "ms" << endl;
    cout << "处理了 " << n << " 个数字" << endl;
    
    // 暴力解法测试（小规模验证）
    if (n <= 1000) {
        auto bruteStart = chrono::high_resolution_clock::now();
        int bruteResult = countPairsBruteForce(nums, low, high);
        auto bruteEnd = chrono::high_resolution_clock::now();
        auto bruteDuration = chrono::duration_cast<chrono::milliseconds>(bruteEnd - bruteStart);
        
        cout << "暴力解法结果: " << bruteResult << " 个数对" << endl;
        cout << "暴力解法耗时: " << bruteDuration.count() << "ms" << endl;
        
        assert(result == bruteResult);
        cout << "结果验证通过！" << endl;
    }
}

/**
 * 边界情况测试函数
 */
void edgeCaseTest() {
    cout << "开始边界情况测试..." << endl;
    
    // 测试最小数组
    vector<int> numsMin = {1, 2};
    int resultMin = countPairs(numsMin, 0, 3);
    assert(resultMin == 1);
    
    // 测试全零数组
    vector<int> numsZero = {0, 0, 0};
    int resultZero = countPairs(numsZero, 0, 0);
    assert(resultZero == 3); // C(3,2)=3
    
    // 测试最大范围
    vector<int> numsMax = {1, 2, 3};
    int resultMax = countPairs(numsMax, 0, INT_MAX);
    int expectedMax = countPairsBruteForce(numsMax, 0, INT_MAX);
    assert(resultMax == expectedMax);
    
    cout << "边界情况测试通过！" << endl;
}

int main() {
    // 运行单元测试
    testCountPairs();
    
    // 运行边界情况测试
    edgeCaseTest();
    
    // 运行性能测试
    performanceTest();
    
    return 0;
}