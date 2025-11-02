#include <iostream>
#include <vector>
#include <unordered_map>
#include <unordered_set>
#include <algorithm>
#include <climits>
using namespace std;

/**
 * LeetCode 128. 最长连续序列
 * 链接: https://leetcode.cn/problems/longest-consecutive-sequence/
 * 难度: 中等
 * 
 * 题目描述:
 * 给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
 * 请你设计并实现时间复杂度为 O(n) 的算法解决此问题。
 * 
 * 示例 1:
 * 输入: nums = [100,4,200,1,3,2]
 * 输出: 4
 * 解释: 最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4。
 * 
 * 示例 2:
 * 输入: nums = [0,3,7,2,5,8,4,6,0,1]
 * 输出: 9
 * 
 * 约束条件:
 * 0 <= nums.length <= 10^5
 * -10^9 <= nums[i] <= 10^9
 */

class UnionFind {
private:
    unordered_map<int, int> parent;
    unordered_map<int, int> size;
    int maxSize;

public:
    UnionFind(const vector<int>& nums) {
        maxSize = 0;
        for (int num : nums) {
            parent[num] = num;
            size[num] = 1;
        }
        if (!nums.empty()) {
            maxSize = 1;
        }
    }
    
    bool contains(int num) {
        return parent.find(num) != parent.end();
    }
    
    int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);  // 路径压缩
        }
        return parent[x];
    }
    
    void unionSets(int x, int y) {
        if (!contains(x) || !contains(y)) {
            return;
        }
        
        int rootX = find(x);
        int rootY = find(y);
        
        if (rootX != rootY) {
            // 按大小合并，小树合并到大树下
            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
                maxSize = max(maxSize, size[rootY]);
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
                maxSize = max(maxSize, size[rootX]);
            }
        }
    }
    
    int getMaxSize() {
        return maxSize;
    }
};

class Solution {
public:
    /**
     * 方法1: 使用并查集解决最长连续序列问题
     * 时间复杂度: O(n * α(n)) ≈ O(n)，其中α是阿克曼函数的反函数
     * 空间复杂度: O(n)
     * 
     * 解题思路:
     * 1. 使用哈希表记录每个数字对应的并查集节点
     * 2. 对于每个数字，检查其相邻数字是否存在，如果存在则合并集合
     * 3. 记录每个集合的大小，返回最大集合的大小
     */
    int longestConsecutive(vector<int>& nums) {
        if (nums.empty()) {
            return 0;
        }
        
        UnionFind uf(nums);
        
        // 遍历数组，合并相邻数字
        for (int num : nums) {
            // 如果存在num-1，则合并num和num-1
            if (uf.contains(num - 1)) {
                uf.unionSets(num, num - 1);
            }
            // 如果存在num+1，则合并num和num+1
            if (uf.contains(num + 1)) {
                uf.unionSets(num, num + 1);
            }
        }
        
        return uf.getMaxSize();
    }
    
    /**
     * 方法2: 使用哈希表 + 遍历的优化解法
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     * 
     * 解题思路:
     * 1. 将所有数字存入哈希表
     * 2. 遍历数组，对于每个数字，如果它是序列的起点（即num-1不存在），则向后查找连续序列
     * 3. 记录最长序列长度
     */
    int longestConsecutiveHashSet(vector<int>& nums) {
        if (nums.empty()) {
            return 0;
        }
        
        unordered_set<int> numSet(nums.begin(), nums.end());
        int longestStreak = 0;
        
        for (int num : numSet) {
            // 只有当num是序列的起点时才进行查找
            if (numSet.find(num - 1) == numSet.end()) {
                int currentNum = num;
                int currentStreak = 1;
                
                // 向后查找连续序列
                while (numSet.find(currentNum + 1) != numSet.end()) {
                    currentNum++;
                    currentStreak++;
                }
                
                longestStreak = max(longestStreak, currentStreak);
            }
        }
        
        return longestStreak;
    }
    
    /**
     * 方法3: 排序解法（不满足O(n)时间复杂度要求，但思路简单）
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(1) 或 O(n)（取决于排序算法）
     */
    int longestConsecutiveSort(vector<int>& nums) {
        if (nums.empty()) {
            return 0;
        }
        
        sort(nums.begin(), nums.end());
        
        int longestStreak = 1;
        int currentStreak = 1;
        
        for (int i = 1; i < nums.size(); i++) {
            // 处理重复数字
            if (nums[i] != nums[i - 1]) {
                // 检查是否连续
                if (nums[i] == nums[i - 1] + 1) {
                    currentStreak++;
                } else {
                    longestStreak = max(longestStreak, currentStreak);
                    currentStreak = 1;
                }
            }
            // 如果数字重复，保持currentStreak不变
        }
        
        return max(longestStreak, currentStreak);
    }
};

// 测试函数
void testSolution() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {100, 4, 200, 1, 3, 2};
    cout << "测试用例1 - 并查集解法: " << solution.longestConsecutive(nums1) << endl; // 预期: 4
    cout << "测试用例1 - 哈希表解法: " << solution.longestConsecutiveHashSet(nums1) << endl; // 预期: 4
    cout << "测试用例1 - 排序解法: " << solution.longestConsecutiveSort(nums1) << endl; // 预期: 4
    
    // 测试用例2
    vector<int> nums2 = {0, 3, 7, 2, 5, 8, 4, 6, 0, 1};
    cout << "测试用例2 - 并查集解法: " << solution.longestConsecutive(nums2) << endl; // 预期: 9
    cout << "测试用例2 - 哈希表解法: " << solution.longestConsecutiveHashSet(nums2) << endl; // 预期: 9
    cout << "测试用例2 - 排序解法: " << solution.longestConsecutiveSort(nums2) << endl; // 预期: 9
    
    // 测试用例3: 空数组
    vector<int> nums3 = {};
    cout << "测试用例3 - 并查集解法: " << solution.longestConsecutive(nums3) << endl; // 预期: 0
    cout << "测试用例3 - 哈希表解法: " << solution.longestConsecutiveHashSet(nums3) << endl; // 预期: 0
    cout << "测试用例3 - 排序解法: " << solution.longestConsecutiveSort(nums3) << endl; // 预期: 0
    
    // 测试用例4: 单个元素
    vector<int> nums4 = {5};
    cout << "测试用例4 - 并查集解法: " << solution.longestConsecutive(nums4) << endl; // 预期: 1
    cout << "测试用例4 - 哈希表解法: " << solution.longestConsecutiveHashSet(nums4) << endl; // 预期: 1
    cout << "测试用例4 - 排序解法: " << solution.longestConsecutiveSort(nums4) << endl; // 预期: 1
    
    // 测试用例5: 重复元素
    vector<int> nums5 = {1, 2, 0, 1};
    cout << "测试用例5 - 并查集解法: " << solution.longestConsecutive(nums5) << endl; // 预期: 3
    cout << "测试用例5 - 哈希表解法: " << solution.longestConsecutiveHashSet(nums5) << endl; // 预期: 3
    cout << "测试用例5 - 排序解法: " << solution.longestConsecutiveSort(nums5) << endl; // 预期: 3
}

int main() {
    testSolution();
    return 0;
}