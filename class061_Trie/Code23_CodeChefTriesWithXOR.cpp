#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>

using namespace std;

/**
 * CodeChef Tries with XOR
 * 
 * 题目描述：
 * 给定一个数组，找出数组中任意两个数的最大异或值。
 * 
 * 解题思路：
 * 1. 使用Trie树存储所有数字的二进制表示
 * 2. 对于每个数字，在Trie树中查找能产生最大异或值的路径
 * 3. 贪心策略：从最高位开始，尽可能选择与当前位相反的位
 * 
 * 时间复杂度：O(N*32) = O(N)，其中N是数组长度，32是整数的位数
 * 空间复杂度：O(N*32) = O(N)
 */

// Trie树节点结构
struct TrieNode {
    TrieNode* children[2]; // 子节点指针数组，对应0和1
    
    TrieNode() {
        for (int i = 0; i < 2; i++) {
            children[i] = nullptr;
        }
    }
};

TrieNode* root = new TrieNode(); // Trie树根节点

/**
 * 向Trie树中插入一个数字的二进制表示
 * @param num 要插入的数字
 */
void insert(int num) {
    TrieNode* node = root;
    // 从最高位开始处理
    for (int i = 31; i >= 0; i--) {
        int bit = (num >> i) & 1; // 获取第i位的值
        if (node->children[bit] == nullptr) {
            node->children[bit] = new TrieNode();
        }
        node = node->children[bit];
    }
}

/**
 * 查找与给定数字异或能得到最大值的数字
 * @param num 给定的数字
 * @return 最大异或值
 */
int findMaxXor(int num) {
    TrieNode* node = root;
    int result = 0;
    
    // 从最高位开始处理
    for (int i = 31; i >= 0; i--) {
        int bit = (num >> i) & 1; // 获取第i位的值
        int oppositeBit = 1 - bit; // 相反位
        
        // 贪心策略：尽可能选择与当前位相反的位
        if (node->children[oppositeBit] != nullptr) {
            result |= (1 << i); // 设置结果的第i位为1
            node = node->children[oppositeBit];
        } else {
            node = node->children[bit];
        }
    }
    
    return result;
}

/**
 * 找出数组中任意两个数的最大异或值
 * @param nums 输入数组
 * @param n 数组长度
 * @return 最大异或值
 */
int findMaximumXOR(vector<int>& nums) {
    // 重新初始化Trie树
    root = new TrieNode();
    
    // 将所有数字插入Trie树
    for (int num : nums) {
        insert(num);
    }
    
    int maxXor = 0;
    // 对于每个数字，查找能产生最大异或值的数字
    for (int num : nums) {
        maxXor = max(maxXor, num ^ findMaxXor(num));
    }
    
    return maxXor;
}

int main() {
    int n;
    cin >> n; // 数组长度
    vector<int> nums(n);
    
    // 读取数组元素
    for (int i = 0; i < n; i++) {
        cin >> nums[i];
    }
    
    // 计算并输出最大异或值
    int result = findMaximumXOR(nums);
    cout << result << endl;
    
    return 0;
}