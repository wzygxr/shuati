// Book Allocation Problem (GFG/Interviewbit)
// Given number of pages in n different books and m students. 
// The books are arranged in ascending order of number of pages. 
// Every student is assigned to read some consecutive books. 
// The task is to assign books in such a way that the maximum number of pages assigned to a student is minimum.
// 测试链接 : https://www.geeksforgeeks.org/problems/allocate-minimum-number-of-pages0937/1

#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

class Solution {
public:
    // 时间复杂度O(n * log(sum))，额外空间复杂度O(1)
    int findPages(vector<int>& pages, int students) {
        // 边界条件：学生数为0或书本数为0
        if (pages.empty() || students == 0) {
            return -1;
        }
        
        // 学生数大于书本数，无法分配
        if (students > pages.size()) {
            return -1;
        }
        
        // 确定二分搜索的上下界
        // 下界：书本中的最大页数（至少要能分配最大的那本书）
        // 上界：所有书页数之和（一个学生读完所有书）
        int maxPage = 0;
        int totalPage = 0;
        for (int page : pages) {
            maxPage = max(maxPage, page);
            totalPage += page;
        }
        
        // 如果学生数等于书本数，每个学生读一本书，最大页数就是最大页数
        if (students == pages.size()) {
            return maxPage;
        }
        
        int left = maxPage;
        int right = totalPage;
        int result = totalPage;
        
        // 二分搜索最低的最大页数
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            // 判断以mid为最大页数是否能分配给students个学生
            if (canAllocate(pages, students, mid)) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return result;
    }
    
private:
    // 判断以maxPages为每个学生最多读的页数是否能分配给students个学生
    bool canAllocate(vector<int>& pages, int students, int maxPages) {
        int requiredStudents = 1; // 需要的学生数，初始为1
        int currentPageSum = 0;   // 当前学生读的页数总和
        
        for (int page : pages) {
            // 如果当前书页数加上当前学生已读页数超过了最大页数
            if (currentPageSum + page > maxPages) {
                // 需要增加一个学生，并将当前书分配给下一个学生
                requiredStudents++;
                currentPageSum = page;
                
                // 如果需要的学生数超过了给定学生数，返回false
                if (requiredStudents > students) {
                    return false;
                }
            } else {
                // 否则将当前书加入当前学生的阅读列表
                currentPageSum += page;
            }
        }
        
        return true;
    }
};

/*
 * 补充说明：
 * 
 * 问题解析：
 * 这是一个经典的二分答案问题，也被称为"最小化最大值"问题。目标是将书籍分配给学生，
 * 使得分配给任意一个学生的最大页数尽可能小。
 * 
 * 解题思路：
 * 1. 确定答案范围：
 *    - 下界：书本中的最大页数（至少要能分配最大的那本书）
 *    - 上界：所有书页数之和（一个学生读完所有书）
 * 2. 二分搜索：在[left, right]范围内二分搜索最低的最大页数
 * 3. 判断函数：canAllocate(pages, students, maxPages)判断以maxPages为每个学生最多读的页数是否能分配给students个学生
 * 4. 贪心策略：按顺序分配书籍，尽可能在每个学生中放更多书籍
 * 
 * 时间复杂度分析：
 * 1. 二分搜索范围是[max, sum]，二分次数是O(log(sum))
 * 2. 每次二分需要调用canAllocate函数，该函数遍历数组一次，时间复杂度是O(n)
 * 3. 总时间复杂度：O(n * log(sum))
 * 
 * 空间复杂度分析：
 * 只使用了常数个额外变量，空间复杂度是O(1)
 * 
 * 工程化考虑：
 * 1. 边界条件处理：注意学生数为0、书本数为0、学生数大于书本数等情况
 * 2. 贪心策略：按顺序分配书籍，不重新排序
 * 3. 整数溢出处理：适当使用long long类型（本题数据范围未超过int）
 * 4. 特殊情况优化：当学生数等于书本数时，直接返回最大页数
 * 
 * 相关题目扩展：
 * 1. GeeksforGeeks - Allocate minimum number of pages - https://www.geeksforgeeks.org/problems/allocate-minimum-number-of-pages0937/1
 * 2. LeetCode 1011. 在D天内送达包裹的能力 - https://leetcode.cn/problems/capacity-to-ship-packages-within-d-days/
 * 3. LeetCode 875. 爱吃香蕉的珂珂 - https://leetcode.cn/problems/koko-eating-bananas/
 * 4. LeetCode 410. 分割数组的最大值 - https://leetcode.cn/problems/split-array-largest-sum/
 * 5. LeetCode 1231. 分享巧克力 - https://leetcode.cn/problems/divide-chocolate/
 * 6. SPOJ AGGRCOW - Aggressive Cows - https://www.spoj.com/problems/AGGRCOW/
 * 7. 牛客网 NC163 机器人跳跃问题 - https://www.nowcoder.com/practice/7037a3d57bbd4336856b8e16a9cafd71
 * 8. HackerRank - Fair Rations - https://www.hackerrank.com/challenges/fair-rations/problem
 * 9. Codeforces 460C - Present - https://codeforces.com/problemset/problem/460/C
 * 10. AtCoder ABC146 - C - Buy an Integer - https://atcoder.jp/contests/abc146/tasks/abc146_c
 */

// 测试代码
// int main() {
//     Solution solution;
//     vector<int> pages = {12, 34, 67, 90};
//     int students = 2;
//     cout << "Book Allocation Result: " << solution.findPages(pages, students) << endl;
//     return 0;
// }