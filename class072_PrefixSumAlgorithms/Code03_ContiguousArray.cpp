/**
 * 连续数组 (Contiguous Array)
 * 
 * 题目描述:
 * 给定一个二进制数组 nums，找到含有相同数量的 0 和 1 的最长连续子数组，并返回该子数组的长度。
 * 
 * 示例:
 * 输入: nums = [0,1]
 * 输出: 2
 * 说明: [0, 1] 是具有相同数量0和1的最长连续子数组。
 * 
 * 输入: nums = [0,1,0]
 * 输出: 2
 * 说明: [0, 1] (或 [1, 0]) 是具有相同数量0和1的最长连续子数组。
 * 
 * 提示:
 * 1 <= nums.length <= 10^5
 * nums[i] 不是 0 就是 1
 * 
 * 题目链接: https://leetcode.com/problems/contiguous-array/
 * 
 * 解题思路:
 * 1. 将0看作-1，问题转化为求和为0的最长子数组
 * 2. 使用前缀和 + 哈希表的方法
 * 3. 遍历数组，计算前缀和
 * 4. 如果某个前缀和之前出现过，说明这两个位置之间的子数组和为0
 * 5. 使用哈希表记录每个前缀和第一次出现的位置
 * 
 * 时间复杂度: O(n) - 需要遍历数组一次
 * 空间复杂度: O(n) - 哈希表最多存储n个不同的前缀和
 * 
 * 工程化考量:
 * 1. 边界条件处理：空数组、单元素数组
 * 2. 哈希表初始化：前缀和为0在位置-1出现，便于计算长度
 * 3. 映射技巧：0→-1, 1→1的转换是关键
 * 4. 性能优化：使用unordered_map的O(1)查找时间
 * 
 * 最优解分析:
 * 这是最优解，因为必须遍历所有元素才能找到最长子数组。
 * 哈希表方法将时间复杂度从O(n^2)优化到O(n)。
 * 
 * 算法核心:
 * 设count为前缀和（0→-1, 1→1），当count[i] = count[j]时，子数组[i+1,j]的和为0。
 * 即0和1的数量相等。
 */

#include <vector>
#include <unordered_map>
#include <iostream>
#include <chrono>

using namespace std;

class Solution {
public:
    /**
     * 找到含有相同数量0和1的最长连续子数组的长度
     * 
     * @param nums 输入的二进制数组
     * @return 最长连续子数组的长度
     * 
     * 异常场景处理:
     * - 空数组：返回0
     * - 单元素数组：返回0（不可能有相同数量的0和1）
     * - 全0或全1数组：返回0
     */
    int findMaxLength(vector<int>& nums) {
        // 边界情况处理
        if (nums.empty() || nums.size() <= 1) {
            return 0;
        }
        
        // 使用unordered_map记录前缀和及其第一次出现的位置
        unordered_map<int, int> map;
        // 初始化：前缀和为0在位置-1出现（便于计算长度）
        map[0] = -1;
        
        int maxLength = 0;  // 最长子数组长度
        int count = 0;      // 当前前缀和（0看作-1，1看作1）
        
        // 遍历数组
        for (int i = 0; i < nums.size(); i++) {
            // 更新前缀和：0看作-1，1看作1
            count += (nums[i] == 0) ? -1 : 1;
            
            // 如果当前前缀和之前出现过，更新最大长度
            if (map.find(count) != map.end()) {
                int length = i - map[count];
                maxLength = max(maxLength, length);
            } else {
                // 记录当前前缀和第一次出现的位置
                map[count] = i;
            }
        }
        
        return maxLength;
    }
};

/**
 * 测试函数
 */
void testFindMaxLength() {
    Solution solution;
    
    // 测试用例1：基础情况
    vector<int> nums1 = {0, 1};
    int result1 = solution.findMaxLength(nums1);
    cout << "测试用例1 [0,1]: " << result1 << " (预期: 2)" << endl;
    
    // 测试用例2：三个元素
    vector<int> nums2 = {0, 1, 0};
    int result2 = solution.findMaxLength(nums2);
    cout << "测试用例2 [0,1,0]: " << result2 << " (预期: 2)" << endl;
    
    // 测试用例3：复杂情况
    vector<int> nums3 = {0, 0, 1, 0, 0, 1, 1, 0};
    int result3 = solution.findMaxLength(nums3);
    cout << "测试用例3 [0,0,1,0,0,1,1,0]: " << result3 << " (预期: 6)" << endl;
    
    // 测试用例4：全0数组
    vector<int> nums4 = {0, 0, 0};
    int result4 = solution.findMaxLength(nums4);
    cout << "测试用例4 [0,0,0]: " << result4 << " (预期: 0)" << endl;
    
    // 测试用例5：全1数组
    vector<int> nums5 = {1, 1, 1};
    int result5 = solution.findMaxLength(nums5);
    cout << "测试用例5 [1,1,1]: " << result5 << " (预期: 0)" << endl;
    
    // 测试用例6：空数组
    vector<int> nums6 = {};
    int result6 = solution.findMaxLength(nums6);
    cout << "测试用例6 []: " << result6 << " (预期: 0)" << endl;
    
    // 测试用例7：单元素数组
    vector<int> nums7 = {0};
    int result7 = solution.findMaxLength(nums7);
    cout << "测试用例7 [0]: " << result7 << " (预期: 0)" << endl;
    
    // 测试用例8：交替数组
    vector<int> nums8 = {0, 1, 0, 1, 0, 1};
    int result8 = solution.findMaxLength(nums8);
    cout << "测试用例8 [0,1,0,1,0,1]: " << result8 << " (预期: 6)" << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "\n=== 性能测试 ===" << endl;
    Solution solution;
    int size = 100000; // 10万元素
    vector<int> largeArray(size);
    
    // 初始化大数组（交替0和1）
    for (int i = 0; i < size; i++) {
        largeArray[i] = i % 2;
    }
    
    auto startTime = chrono::high_resolution_clock::now();
    int result = solution.findMaxLength(largeArray);
    auto endTime = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
    cout << "处理 " << size << " 个元素，最长子数组长度: " << result << ", 耗时: " << duration.count() << "ms" << endl;
}

/**
 * 主函数
 */
int main() {
    cout << "=== 连续数组测试 ===" << endl;
    testFindMaxLength();
    
    // 运行性能测试（可选）
    // performanceTest();
    
    cout << "\n=== 测试完成 ===" << endl;
    return 0;
}