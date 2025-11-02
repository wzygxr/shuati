#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

// 分发饼干
// 假设你是一位很棒的家长，想要给你的孩子们一些小饼干。但是，每个孩子最多只能给一块饼干。
// 对每个孩子 i，都有一个胃口值 g[i]，这是能让孩子们满足胃口的饼干的最小尺寸；
// 每块饼干 j 都有一个尺寸 s[j]。如果 s[j] >= g[i]，我们可以将这个饼干 j 分配给孩子 i，
// 这个孩子会得到满足。目标是尽可能满足越多数量的孩子，并输出这个最大数值。
// 测试链接: https://leetcode.cn/problems/assign-cookies/

class Code07_AssignCookies {
public:
    /**
     * 分发饼干问题的贪心解法
     * 
     * 解题思路：
     * 1. 将孩子胃口值数组g和饼干尺寸数组s都按升序排序
     * 2. 使用双指针技术，分别指向当前孩子和当前饼干
     * 3. 从胃口最小的孩子和尺寸最小的饼干开始匹配
     * 4. 如果当前饼干能满足当前孩子，则两个指针都向前移动
     * 5. 如果不能满足，则尝试用更大的饼干（移动饼干指针）
     * 
     * 贪心策略的正确性：
     * 1. 对于胃口小的孩子，优先用小饼干满足，这样能保留大饼干给胃口大的孩子
     * 2. 对于小饼干，优先满足胃口小的孩子，因为大饼干可以满足更大胃口的孩子
     * 
     * 时间复杂度：O(m*log(m) + n*log(n))，其中m是孩子数量，n是饼干数量
     * 主要消耗在排序上，双指针遍历只需要O(m+n)
     * 
     * 空间复杂度：O(1)，只使用了常数个额外变量
     * 
     * @param g 孩子们的胃口值数组
     * @param s 饼干的尺寸数组
     * @return 能够满足的孩子的最大数量
     */
    static int findContentChildren(vector<int>& g, vector<int>& s) {
        // 边界条件处理：如果孩子或饼干数组为空，则无法满足任何孩子
        if (g.empty() || s.empty()) {
            return 0;
        }

        // 1. 对孩子胃口值和饼干尺寸进行升序排序
        // 这是贪心策略的基础：优先满足胃口小的孩子，用小饼干满足他们
        sort(g.begin(), g.end());
        sort(s.begin(), s.end());

        // 2. 初始化双指针
        int childIndex = 0;    // 指向当前需要满足的孩子
        int cookieIndex = 0;   // 指向当前可用的饼干
        int satisfiedChildren = 0; // 记录满足的孩子数量

        // 3. 双指针遍历过程
        // 当孩子和饼干都未遍历完时继续
        while (childIndex < g.size() && cookieIndex < s.size()) {
            // 4. 如果当前饼干能满足当前孩子
            if (s[cookieIndex] >= g[childIndex]) {
                // 满足孩子数增加
                satisfiedChildren++;
                // 移动到下一个孩子
                childIndex++;
                // 移动到下一块饼干（当前饼干已使用）
                cookieIndex++;
            } else {
                // 5. 当前饼干不能满足当前孩子
                // 需要尝试更大的饼干，移动饼干指针
                cookieIndex++;
                // 孩子指针不移动，因为当前孩子还未被满足
            }
        }

        // 6. 返回满足的孩子总数
        return satisfiedChildren;
    }
};

// 测试方法
int main() {
    // 测试用例1
    // 输入: g = [1,2,3], s = [1,1]
    // 输出: 1
    vector<int> g1 = {1, 2, 3};
    vector<int> s1 = {1, 1};
    cout << "测试用例1结果: " << Code07_AssignCookies::findContentChildren(g1, s1) << endl; // 期望输出: 1

    // 测试用例2
    // 输入: g = [1,2], s = [1,2,3]
    // 输出: 2
    vector<int> g2 = {1, 2};
    vector<int> s2 = {1, 2, 3};
    cout << "测试用例2结果: " << Code07_AssignCookies::findContentChildren(g2, s2) << endl; // 期望输出: 2

    // 测试用例3：边界情况
    // 输入: g = [], s = [1,2,3]
    // 输出: 0
    vector<int> g3 = {};
    vector<int> s3 = {1, 2, 3};
    cout << "测试用例3结果: " << Code07_AssignCookies::findContentChildren(g3, s3) << endl; // 期望输出: 0

    // 测试用例4：复杂情况
    // 输入: g = [1,2,7,8,9], s = [1,3,5,9,10]
    // 输出: 4
    vector<int> g4 = {1, 2, 7, 8, 9};
    vector<int> s4 = {1, 3, 5, 9, 10};
    cout << "测试用例4结果: " << Code07_AssignCookies::findContentChildren(g4, s4) << endl; // 期望输出: 4

    return 0;
}