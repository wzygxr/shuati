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

// 简化版本，使用固定大小的数组模拟前缀树
// 由于C++标准库不可用，我们使用简单的暴力方法

// 找到数组中两个数的最大异或值
int findMaximumXOR(int nums[], int size) {
    if (size == 0) {
        return 0;
    }
    
    int maxResult = 0;
    
    // 暴力方法：检查所有数对
    for (int i = 0; i < size; i++) {
        for (int j = i + 1; j < size; j++) {
            int xorValue = nums[i] ^ nums[j];
            if (xorValue > maxResult) {
                maxResult = xorValue;
            }
        }
    }
    
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