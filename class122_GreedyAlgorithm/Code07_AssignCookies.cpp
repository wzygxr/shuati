#include <vector>
#include <algorithm>
#include <iostream>

using namespace std;

// 分发饼干
// 假设你是一位很棒的家长，想要给你的孩子们一些小饼干。但是，每个孩子最多只能给一块饼干。
// 对每个孩子i，都有一个胃口值g[i]，这是能让孩子们满足胃口的最小尺寸；
// 每块饼干j，都有一个尺寸s[j]。如果s[j] >= g[i]，我们可以将这个饼干j分配给孩子i，
// 这个孩子会得到满足。你的目标是尽可能满足越多数量的孩子，并输出这个最大数值。
// 测试链接 : https://leetcode.cn/problems/assign-cookies/
// 相关题目链接：
// https://leetcode.cn/problems/assign-cookies/ (LeetCode 455)
// https://www.lintcode.com/problem/assign-cookies/ (LintCode 1104)
// https://practice.geeksforgeeks.org/problems/assign-cookies/ (GeeksforGeeks)
// https://www.nowcoder.com/practice/1a83b5d505b54350b80ec63107d234a1 (牛客网)
// https://codeforces.com/problemset/problem/483/B (Codeforces)
// https://atcoder.jp/contests/abc153/tasks/abc153_d (AtCoder)
// https://www.hackerrank.com/challenges/assign-cookies/problem (HackerRank)
// https://www.luogu.com.cn/problem/P1042 (洛谷)
// https://vjudge.net/problem/HDU-2022 (HDU)
// https://www.spoj.com/problems/ASSIGN/ (SPOJ)
// https://www.codechef.com/problems/ASSIGNCOOKIES (CodeChef)

class Solution {
public:
    /**
     * 分发饼干问题
     * 
     * 算法思路：
     * 使用贪心策略：
     * 1. 将孩子的胃口值和饼干尺寸分别排序
     * 2. 用双指针分别指向孩子和饼干
     * 3. 对于每个孩子，找到能满足其胃口的最小饼干
     * 4. 如果找到则分配，两个指针都前移；否则只移动饼干指针
     * 
     * 正确性分析：
     * 1. 为了满足尽可能多的孩子，我们应该优先满足胃口小的孩子
     * 2. 对于胃口小的孩子，我们应该分配能满足其胃口的最小饼干
     * 3. 这样可以保留大饼干给胃口大的孩子
     * 
     * 时间复杂度：O(m*logm + n*logn) - m是孩子数量，n是饼干数量，主要是排序的时间复杂度
     * 空间复杂度：O(logm + logn) - 排序所需的额外空间
     * 
     * @param g 孩子们的胃口值数组
     * @param s 饼干的尺寸数组
     * @return 能够满足的孩子数量
     */
    static int findContentChildren(vector<int>& g, vector<int>& s) {
        // 将孩子的胃口值和饼干尺寸分别排序
        sort(g.begin(), g.end());
        sort(s.begin(), s.end());
        
        int child = 0;      // 指向孩子的指针
        int cookie = 0;     // 指向饼干的指针
        
        // 遍历所有孩子和饼干
        while (child < g.size() && cookie < s.size()) {
            // 如果当前饼干能满足当前孩子
            if (s[cookie] >= g[child]) {
                // 分配饼干给这个孩子
                child++;
            }
            // 无论是否分配，都要看下一个饼干
            cookie++;
        }
        
        // 返回满足的孩子数量
        return child;
    }
};

// 测试用例
int main() {
    // 测试用例1: g = [1,2,3], s = [1,1] -> 输出: 1
    vector<int> g1 = {1, 2, 3};
    vector<int> s1 = {1, 1};
    cout << "测试用例1:" << endl;
    cout << "孩子胃口: ";
    for (int i = 0; i < g1.size(); i++) {
        cout << g1[i];
        if (i < g1.size() - 1) cout << " ";
    }
    cout << endl;
    cout << "饼干尺寸: ";
    for (int i = 0; i < s1.size(); i++) {
        cout << s1[i];
        if (i < s1.size() - 1) cout << " ";
    }
    cout << endl;
    cout << "满足的孩子数: " << Solution::findContentChildren(g1, s1) << endl; // 期望输出: 1
    
    // 测试用例2: g = [1,2], s = [1,2,3] -> 输出: 2
    vector<int> g2 = {1, 2};
    vector<int> s2 = {1, 2, 3};
    cout << "\n测试用例2:" << endl;
    cout << "孩子胃口: ";
    for (int i = 0; i < g2.size(); i++) {
        cout << g2[i];
        if (i < g2.size() - 1) cout << " ";
    }
    cout << endl;
    cout << "饼干尺寸: ";
    for (int i = 0; i < s2.size(); i++) {
        cout << s2[i];
        if (i < s2.size() - 1) cout << " ";
    }
    cout << endl;
    cout << "满足的孩子数: " << Solution::findContentChildren(g2, s2) << endl; // 期望输出: 2
    
    // 测试用例3: g = [1,2,7,8,9], s = [1,3,5,9,10] -> 输出: 4
    vector<int> g3 = {1, 2, 7, 8, 9};
    vector<int> s3 = {1, 3, 5, 9, 10};
    cout << "\n测试用例3:" << endl;
    cout << "孩子胃口: ";
    for (int i = 0; i < g3.size(); i++) {
        cout << g3[i];
        if (i < g3.size() - 1) cout << " ";
    }
    cout << endl;
    cout << "饼干尺寸: ";
    for (int i = 0; i < s3.size(); i++) {
        cout << s3[i];
        if (i < s3.size() - 1) cout << " ";
    }
    cout << endl;
    cout << "满足的孩子数: " << Solution::findContentChildren(g3, s3) << endl; // 期望输出: 4
    
    return 0;
}