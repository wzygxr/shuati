// C++标准库头文件
#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

// Aggressive Cows (SPOJ)
// Farmer John has built a new long barn, with N (2 <= N <= 100,000) stalls. 
// The stalls are located along a straight line at positions x1,...,xN.
// His C (2 <= C <= N) cows don't like this barn layout and become aggressive towards each other 
// once put into a stall. To prevent the cows from hurting each other, 
// FJ wants to assign the cows to the stalls, such that the minimum distance between any two of them is as large as possible.
// What is the largest minimum distance?
// Problem Link: https://www.spoj.com/problems/AGGRCOW/

class Solution {
public:
    // 时间复杂度O(n * log(max-min))，额外空间复杂度O(1)
    int aggressiveCows(vector<int>& stalls, int cows) {
        // 先对牛棚位置进行排序
        sort(stalls.begin(), stalls.end());
        
        // 二分答案的范围：最小距离为0，最大距离为最远两个牛棚的距离
        int left = 0;
        int right = stalls[stalls.size() - 1] - stalls[0];
        int result = 0;
        
        // 二分搜索最大的最小距离
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            // 检查是否能以mid为最小距离放置所有奶牛
            if (canPlaceCows(stalls, cows, mid)) {
                result = mid;  // 记录可行解
                left = mid + 1;  // 尝试更大的最小距离
            } else {
                right = mid - 1;  // 减小最小距离
            }
        }
        
        return result;
    }
    
private:
    // 检查是否能以minDist为最小距离放置所有奶牛
    bool canPlaceCows(vector<int>& stalls, int cows, int minDist) {
        int count = 1;  // 第一个奶牛放在第一个牛棚
        int lastPosition = stalls[0];
        
        // 遍历所有牛棚，尝试放置剩余的奶牛
        for (int i = 1; i < stalls.size(); i++) {
            // 如果当前牛棚与上一个奶牛的距离大于等于minDist，则可以放置奶牛
            if (stalls[i] - lastPosition >= minDist) {
                count++;
                lastPosition = stalls[i];
                
                // 如果所有奶牛都已放置完毕，返回true
                if (count == cows) {
                    return true;
                }
            }
        }
        
        // 无法放置所有奶牛
        return false;
    }
};

/*
 * 补充说明：
 * 
 * 问题解析：
 * 这是一个经典的二分答案问题，也被称为"最大化最小值"问题。目标是在给定的牛棚中放置奶牛，
 * 使得任意两头奶牛之间的最小距离尽可能大。
 * 
 * 解题思路：
 * 1. 确定答案范围：最小距离为0，最大距离为最远两个牛棚的距离
 * 2. 二分搜索：在[left, right]范围内二分搜索最大的最小距离
 * 3. 判断函数：canPlaceCows(stalls, cows, minDist)检查是否能以minDist为最小距离放置所有奶牛
 * 4. 贪心策略：在判断函数中采用贪心策略，尽可能早地放置奶牛
 * 
 * 时间复杂度分析：
 * 1. 排序时间复杂度：O(n * log(n))
 * 2. 二分搜索范围是[0, max-min]，二分次数是O(log(max-min))
 * 3. 每次二分需要调用canPlaceCows函数，该函数遍历数组一次，时间复杂度是O(n)
 * 4. 总时间复杂度：O(n * log(n) + n * log(max-min)) = O(n * log(max-min))
 * 
 * 空间复杂度分析：
 * 1. 排序需要O(log(n))的递归栈空间
 * 2. 其他只使用了常数个额外变量
 * 3. 总空间复杂度：O(log(n))
 * 
 * 工程化考虑：
 * 1. 边界条件处理：注意数组为空或奶牛数量为0的情况
 * 2. 贪心策略：在canPlaceCows函数中采用贪心策略，尽可能早地放置奶牛
 * 3. 位运算优化：(right - left) >> 1 等价于 (right - left) / 2，但效率略高
 * 
 * 相关题目扩展：
 * 1. SPOJ AGGRCOW - Aggressive Cows - https://www.spoj.com/problems/AGGRCOW/
 * 2. LeetCode 1011. 在D天内送达包裹的能力 - https://leetcode.cn/problems/capacity-to-ship-packages-within-d-days/
 * 3. LeetCode 875. 爱吃香蕉的珂珂 - https://leetcode.cn/problems/koko-eating-bananas/
 * 4. LeetCode 410. 分割数组的最大值 - https://leetcode.cn/problems/split-array-largest-sum/
 * 5. LeetCode 1231. 分享巧克力 - https://leetcode.cn/problems/divide-chocolate/
 * 6. LeetCode 1482. 制作m束花所需的时间 - https://leetcode.cn/problems/minimum-number-of-days-to-make-m-bouquets/
 * 7. 牛客网 NC163 机器人跳跃问题 - https://www.nowcoder.com/practice/7037a3d57bbd4336856b8e16a9cafd71
 * 8. Codeforces 460C - Present - https://codeforces.com/problemset/problem/460/C
 * 9. HackerRank - Fair Rations - https://www.hackerrank.com/challenges/fair-rations/problem
 * 10. AtCoder ABC146 - C - Buy an Integer - https://atcoder.jp/contests/abc146/tasks/abc146_c
 */

// 测试代码
// int main() {
//     Solution solution;
//     vector<int> stalls = {1, 2, 4, 8, 9};
//     int cows = 3;
//     cout << "Aggressive Cows Result: " << solution.aggressiveCows(stalls, cows) << endl;
//     return 0;
// }