/**
 * LeetCode 128 - 最长连续序列
 * https://leetcode-cn.com/problems/longest-consecutive-sequence/
 * 
 * 题目描述：
 * 给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
 * 
 * 请你设计并实现时间复杂度为 O(n) 的算法解决此问题。
 * 
 * 示例 1：
 * 输入：nums = [100,4,200,1,3,2]
 * 输出：4
 * 解释：最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4。
 * 
 * 示例 2：
 * 输入：nums = [0,3,7,2,5,8,4,6,0,1]
 * 输出：9
 * 
 * 解题思路（使用并查集）：
 * 1. 使用并查集将连续的数字合并到同一个集合中
 * 2. 对于每个数字，如果它的前驱（num-1）存在，就将它们合并
 * 3. 统计每个集合的大小，找出最大的集合大小
 * 
 * 时间复杂度分析：
 * - 初始化并查集：O(n)
 * - 处理每个数字：O(n * α(n))，其中α是阿克曼函数的反函数，近似为常数
 * - 统计最大集合大小：O(n)
 * - 总体时间复杂度：O(n * α(n)) ≈ O(n)
 * 
 * 空间复杂度分析：
 * - 并查集映射和大小映射：O(n)
 * - 总体空间复杂度：O(n)
 */

#include <iostream>
#include <vector>
#include <unordered_map>
#include <algorithm>

using namespace std;

class LongestConsecutiveSequence {
private:
    // 并查集的父节点映射
    unordered_map<int, int> parent;
    // 每个集合的大小映射
    unordered_map<int, int> size;

    /**
     * 查找元素所在集合的根节点，并进行路径压缩
     * @param x 要查找的元素
     * @return 根节点
     */
    int find(int x) {
        if (parent[x] != x) {
            // 路径压缩：将x的父节点直接设置为根节点
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    /**
     * 添加元素到并查集
     * @param x 要添加的元素
     */
    void add(int x) {
        if (parent.find(x) == parent.end()) {
            parent[x] = x; // 初始时，元素的父节点是自己
            size[x] = 1;   // 初始时，集合的大小为1
        }
    }

public:
    /**
     * 找出最长连续序列的长度
     * @param nums 整数数组
     * @return 最长连续序列的长度
     */
    int longestConsecutive(vector<int>& nums) {
        if (nums.empty()) {
            return 0;
        }

        // 初始化并查集（这里实际上是在处理时动态初始化）

        // 将每个数字及其前驱（如果存在）合并到同一个集合中
        for (int num : nums) {
            add(num);
            // 如果num-1存在，将num和num-1合并
            if (parent.find(num - 1) != parent.end()) {
                int rootNum = find(num);
                int rootNumMinus1 = find(num - 1);

                if (rootNum != rootNumMinus1) {
                    // 将较小的集合合并到较大的集合中，以保持树的平衡
                    if (size[rootNum] < size[rootNumMinus1]) {
                        parent[rootNum] = rootNumMinus1;
                        size[rootNumMinus1] += size[rootNum];
                    } else {
                        parent[rootNumMinus1] = rootNum;
                        size[rootNum] += size[rootNumMinus1];
                    }
                }
            }
        }

        // 找出最大的集合大小
        int maxLength = 0;
        for (const auto& pair : size) {
            maxLength = max(maxLength, pair.second);
        }

        return maxLength;
    }

    /**
     * 另一种实现方法（更高效的哈希表方法）
     * @param nums 整数数组
     * @return 最长连续序列的长度
     */
    int longestConsecutiveHash(vector<int>& nums) {
        if (nums.empty()) {
            return 0;
        }

        // 将所有数字存入哈希集合中，以便O(1)时间查找
        unordered_map<int, bool> visited;
        for (int num : nums) {
            visited[num] = false;
        }

        int maxLength = 0;

        // 遍历每个数字
        for (int num : nums) {
            // 如果当前数字已经被访问过，跳过
            if (visited[num]) {
                continue;
            }

            visited[num] = true;
            int currentLength = 1;

            // 向前查找连续数字
            int prev = num - 1;
            while (visited.find(prev) != visited.end() && !visited[prev]) {
                visited[prev] = true;
                currentLength++;
                prev--;
            }

            // 向后查找连续数字
            int next = num + 1;
            while (visited.find(next) != visited.end() && !visited[next]) {
                visited[next] = true;
                currentLength++;
                next++;
            }

            maxLength = max(maxLength, currentLength);
        }

        return maxLength;
    }
};

/**
 * 主函数，用于测试
 */
int main() {
    LongestConsecutiveSequence solution;

    // 测试用例1
    vector<int> nums1 = {100, 4, 200, 1, 3, 2};
    cout << "测试用例1结果（并查集方法）：" << solution.longestConsecutive(nums1) << endl;
    cout << "测试用例1结果（哈希表方法）：" << solution.longestConsecutiveHash(nums1) << endl;
    // 预期输出：4

    // 测试用例2
    vector<int> nums2 = {0, 3, 7, 2, 5, 8, 4, 6, 0, 1};
    cout << "测试用例2结果（并查集方法）：" << solution.longestConsecutive(nums2) << endl;
    cout << "测试用例2结果（哈希表方法）：" << solution.longestConsecutiveHash(nums2) << endl;
    // 预期输出：9

    // 测试用例3：空数组
    vector<int> nums3 = {};
    cout << "测试用例3结果（并查集方法）：" << solution.longestConsecutive(nums3) << endl;
    cout << "测试用例3结果（哈希表方法）：" << solution.longestConsecutiveHash(nums3) << endl;
    // 预期输出：0

    // 测试用例4：单元素数组
    vector<int> nums4 = {1};
    cout << "测试用例4结果（并查集方法）：" << solution.longestConsecutive(nums4) << endl;
    cout << "测试用例4结果（哈希表方法）：" << solution.longestConsecutiveHash(nums4) << endl;
    // 预期输出：1

    // 测试用例5：有重复元素的数组
    vector<int> nums5 = {1, 2, 0, 1};
    cout << "测试用例5结果（并查集方法）：" << solution.longestConsecutive(nums5) << endl;
    cout << "测试用例5结果（哈希表方法）：" << solution.longestConsecutiveHash(nums5) << endl;
    // 预期输出：3

    return 0;
}

/**
 * C++特定优化：
 * 1. 使用unordered_map实现并查集，以处理任意范围的整数
 * 2. 在longestConsecutiveHash方法中实现了更高效的哈希表方法
 * 3. 使用const引用和auto关键字提高代码可读性和效率
 * 
 * 时间复杂度比较：
 * - 并查集方法：O(n * α(n)) ≈ O(n)
 * - 哈希表方法：O(n)
 * 
 * 空间复杂度比较：
 * - 并查集方法：O(n)
 * - 哈希表方法：O(n)
 * 
 * 工程化考量：
 * 1. 在实际应用中，哈希表方法可能更简单直观，且常数因子更小
 * 2. 并查集方法更通用，可以扩展到其他连通性问题
 * 3. 对于大规模数据，两种方法的性能差异可能不明显
 */