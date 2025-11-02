/**
 * 题目: LeetCode 421. Maximum XOR of Two Numbers in an Array
 * 链接: https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
 * 
 * 题目描述:
 * 给你一个整数数组 nums ，返回 nums[i] XOR nums[j] 的最大运算结果，其中 0 ≤ i ≤ j < n 。
 * 
 * 解题思路:
 * 使用前缀树(Trie)结构：
 * 1. 将数组中每个数字的二进制表示插入前缀树中
 * 2. 对于每个数字，在前缀树中查找能与之产生最大异或值的路径
 * 3. 贪心策略：对于每一位，尽量寻找相反的位以最大化异或结果
 * 
 * 时间复杂度: O(n * 32) = O(n) - 每个数字处理32位
 * 空间复杂度: O(n * 32) = O(n) - 前缀树存储
 */

// 前缀树节点结构
typedef struct TrieNode {
    struct TrieNode* children[2]; // 0和1两个子节点
} TrieNode;

// 创建新的前缀树节点
TrieNode* createNode() {
    TrieNode* node = (TrieNode*)malloc(sizeof(TrieNode));
    node->children[0] = NULL;
    node->children[1] = NULL;
    return node;
}

// 向前缀树中插入数字
void insert(TrieNode* root, int num) {
    TrieNode* node = root;
    // 从最高位开始处理
    for (int i = 31; i >= 0; i--) {
        int bit = (num >> i) & 1;
        if (node->children[bit] == NULL) {
            node->children[bit] = createNode();
        }
        node = node->children[bit];
    }
}

// 在前缀树中查找与num异或能得到最大值的数字
int findMaxXor(TrieNode* root, int num) {
    TrieNode* node = root;
    int maxXor = 0;
    // 从最高位开始处理
    for (int i = 31; i >= 0; i--) {
        int bit = (num >> i) & 1;
        // 贪心策略：尽量走相反的位
        int desiredBit = 1 - bit;
        if (node->children[desiredBit] != NULL) {
            // 能走相反的位，该位异或结果为1
            maxXor |= (1 << i);
            node = node->children[desiredBit];
        } else {
            // 只能走相同的位，该位异或结果为0
            node = node->children[bit];
        }
    }
    return maxXor;
}

// 释放前缀树内存
void freeTrie(TrieNode* root) {
    if (root == NULL) return;
    freeTrie(root->children[0]);
    freeTrie(root->children[1]);
    free(root);
}

// 找到数组中两个数的最大异或值
int findMaximumXOR(int nums[], int size) {
    if (size == 0) {
        return 0;
    }
    
    // 构建前缀树
    TrieNode* root = createNode();
    
    // 将所有数字插入前缀树
    for (int i = 0; i < size; i++) {
        insert(root, nums[i]);
    }
    
    int maxResult = 0;
    // 对于每个数字，在前缀树中寻找能产生最大异或值的数字
    for (int i = 0; i < size; i++) {
        int currentMax = findMaxXor(root, nums[i]);
        if (currentMax > maxResult) {
            maxResult = currentMax;
        }
    }
    
    // 释放内存
    freeTrie(root);
    
    return maxResult;
}

// 测试函数
int main() {
    // 测试用例1
    int nums1[] = {3, 10, 5, 25, 2, 8};
    int size1 = 6;
    // 应该输出 28 (5^25)
    int result1 = findMaximumXOR(nums1, size1);
    
    // 测试用例2
    int nums2[] = {0};
    int size2 = 1;
    // 应该输出 0
    int result2 = findMaximumXOR(nums2, size2);
    
    // 测试用例3
    int nums3[] = {2, 4};
    int size3 = 2;
    // 应该输出 6 (2^4)
    int result3 = findMaximumXOR(nums3, size3);
    
    return 0;
}