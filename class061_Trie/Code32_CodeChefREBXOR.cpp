/**
 * CodeChef REBXOR - Nikitosh and xor
 * 
 * 题目描述：
 * 给定一个长度为N的数组A，要求将数组分成两个非空的连续子数组，使得这两个子数组的异或和的异或值最大。
 * 
 * 解题思路：
 * 这是一个经典的01Trie应用问题。我们可以使用以下方法：
 * 1. 预处理前缀异或数组
 * 2. 对于每个分割点，计算左边子数组的最大异或值和右边子数组的最大异或值
 * 3. 使用01Trie来高效计算最大异或值
 * 
 * 具体步骤：
 * 1. 计算前缀异或数组prefix_xor，其中prefix_xor[i]表示A[0]到A[i-1]的异或和
 * 2. 对于每个位置i，计算从A[0]到A[i-1]中某个子数组的最大异或值，存储在left_max数组中
 * 3. 对于每个位置i，计算从A[i]到A[n-1]中某个子数组的最大异或值，存储在right_max数组中
 * 4. 枚举所有分割点，计算left_max[i] XOR right_max[i]的最大值
 * 
 * 时间复杂度：O(N * log(max_value))
 * 空间复杂度：O(N * log(max_value))
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define max(a,b) ((a) > (b) ? (a) : (b))

using namespace std;

/**
 * 01Trie树节点结构
 */
struct TrieNode {
    TrieNode* children[2];  // 01Trie只有0和1两个子节点
    int count;              // 经过该节点的数字数量
    
    TrieNode() {
        children[0] = children[1] = nullptr;
        count = 0;
    }
    
    ~TrieNode() {
        delete children[0];
        delete children[1];
    }
};

/**
 * 01Trie树类
 */
class Trie {
private:
    TrieNode* root;
    
public:
    Trie() {
        root = new TrieNode();
    }
    
    ~Trie() {
        delete root;
    }
    
    /**
     * 向01Trie中插入一个数字
     * @param num 要插入的数字
     */
    void insert(int num) {
        TrieNode* node = root;
        // 从最高位开始处理（假设数字不超过30位）
        for (int i = 30; i >= 0; i--) {
            int bit = (num >> i) & 1;  // 获取第i位的值（0或1）
            if (node->children[bit] == nullptr) {
                node->children[bit] = new TrieNode();
            }
            node = node->children[bit];
            node->count++;
        }
    }
    
    /**
     * 查询与给定数字异或值最大的数字的异或结果
     * @param num 给定的数字
     * @return 最大异或值
     */
    int queryMaxXor(int num) {
        if (root->children[0] == nullptr && root->children[1] == nullptr) {
            return 0;
        }
        
        TrieNode* node = root;
        int result = 0;
        
        // 从最高位开始处理，贪心地选择能使异或结果最大的路径
        for (int i = 30; i >= 0; i--) {
            int bit = (num >> i) & 1;  // 获取第i位的值
            // 贪心策略：优先选择与当前位相反的路径（使异或结果为1）
            if (node->children[1 - bit] != nullptr && node->children[1 - bit]->count > 0) {
                result |= (1 << i);  // 设置第i位为1
                node = node->children[1 - bit];
            } else {
                node = node->children[bit];
            }
        }
        
        return result;
    }
};

/**
 * 解决REBXOR问题
 * @param arr 输入数组
 * @param n 数组长度
 * @return 最大异或值
 */
int solveRebxor(int* arr, int n) {
    if (n < 2) {
        return 0;
    }
    
    // 计算前缀异或数组
    int* prefixXor = new int[n + 1];
    for (int i = 0; i < n + 1; i++) prefixXor[i] = 0;
    for (int i = 0; i < n; i++) {
        prefixXor[i + 1] = prefixXor[i] ^ arr[i];
    }
    
    // 计算left_max数组：left_max[i]表示前i个元素中某个子数组的最大异或值
    int* leftMax = new int[n + 1];
    for (int i = 0; i < n + 1; i++) leftMax[i] = 0;
    Trie* trie = new Trie();
    trie->insert(0);  // 插入0，处理从第一个元素开始的子数组
    
    for (int i = 1; i < n; i++) {
        // 查询以第i个元素结尾的子数组的最大异或值
        leftMax[i] = max(leftMax[i - 1], trie->queryMaxXor(prefixXor[i]));
        // 将prefixXor[i]插入Trie
        trie->insert(prefixXor[i]);
    }
    
    // 计算right_max数组：right_max[i]表示从第i个元素到最后一个元素中某个子数组的最大异或值
    int* rightMax = new int[n + 1];
    for (int i = 0; i < n + 1; i++) rightMax[i] = 0;
    delete trie;
    trie = new Trie();
    trie->insert(0);  // 插入0，处理以最后一个元素结尾的子数组
    
    for (int i = n - 1; i > 0; i--) {
        // 查询以第i个元素开头的子数组的最大异或值
        rightMax[i] = max(rightMax[i + 1], trie->queryMaxXor(prefixXor[i]));
        // 将prefixXor[i]插入Trie
        trie->insert(prefixXor[i]);
    }
    
    // 枚举所有分割点，计算最大异或值
    int maxXor = 0;
    for (int i = 1; i < n; i++) {
        maxXor = max(maxXor, leftMax[i] + rightMax[i + 1]);
    }
    
    // 释放内存
    delete[] prefixXor;
    delete[] leftMax;
    delete[] rightMax;
    delete trie;
    
    return maxXor;
}

/**
 * 主函数
 */
int main() {
    int n;
    scanf("%d", &n);
    int* arr = (int*)malloc(sizeof(int) * n);
    for (int i = 0; i < n; i++) {
        scanf("%d", &arr[i]);
    }
    
    int result = solveRebxor(arr, n);
    printf("%d\n", result);
    
    free(arr);
    return 0;
}