// 滑动窗口中位数
// 中位数是有序序列最中间的那个数。如果序列的长度是偶数，则没有最中间的数；
// 此时中位数是最中间的两个数的平均数。
// 例如：
// [2,3,4]，中位数是 3
// [2,3]，中位数是 (2 + 3) / 2 = 2.5
// 给你一个数组 nums，有一个长度为 k 的窗口从最左端滑动到最右端。
// 窗口中有 k 个数，每次窗口向右移动 1 位。
// 你的任务是找出每次窗口移动后得到的新窗口中元素的中位数，并输出由它们组成的数组。
// 测试链接：https://leetcode.cn/problems/sliding-window-median/
//
// 题目解析：
// 这是滑动窗口的另一个变种，需要维护窗口内元素的有序性以便快速获取中位数。
// 虽然不是直接使用单调队列，但可以使用类似的思想来优化。
//
// 算法思路：
// 1. 使用两个堆（优先队列）来维护窗口内的元素：
//    - maxHeap：最大堆，存储较小的一半元素
//    - minHeap：最小堆，存储较大的一半元素
// 2. 保持两个堆的大小平衡，使得中位数可以快速获取
// 3. 滑动窗口移动时，添加新元素并移除旧元素
// 4. 每次移动后计算并记录中位数
//
// 时间复杂度：O(n log k) - 每次操作堆需要O(log k)时间，共n次操作
// 空间复杂度：O(k) - 两个堆总共存储k个元素

#include <vector>
#include <queue>
#include <set>
using namespace std;

class Solution {
public:
    // 计算滑动窗口中位数
    // nums: 输入数组
    // k: 窗口大小
    // 返回: 每个窗口的中位数组成的数组
    vector<double> medianSlidingWindow(vector<int>& nums, int k) {
        // 使用multiset模拟双堆结构，保持有序性
        multiset<long long> window;
        vector<double> result;
        
        for (int i = 0; i < nums.size(); i++) {
            // 添加当前元素到窗口中
            window.insert(nums[i]);
            
            // 如果窗口大小超过k，需要移除最左边的元素
            if (i >= k) {
                window.erase(window.find(nums[i - k]));
            }
            
            // 如果窗口大小达到k，计算中位数
            if (i >= k - 1) {
                // 计算中位数
                auto mid_it = next(window.begin(), k / 2);
                if (k % 2 == 1) {
                    // 奇数个元素，返回中间元素
                    result.push_back(*mid_it);
                } else {
                    // 偶数个元素，返回中间两个元素的平均值
                    auto left_it = next(window.begin(), k / 2 - 1);
                    result.push_back((double)(*left_it + *mid_it) / 2.0);
                }
            }
        }
        
        return result;
    }
};