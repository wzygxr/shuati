#include <iostream>
#include <vector>
#include <string>
using namespace std;

// 划分字母区间
// 题目描述：字符串 S 由小写字母组成。我们要把这个字符串划分为尽可能多的片段，同一字母最多出现在一个片段中。
// 返回一个表示每个字符串片段的长度的列表。
// 测试链接: https://leetcode.cn/problems/partition-labels/

class Code17_PartitionLabels {
public:
    /**
     * 划分字母区间的贪心解法
     * 
     * 解题思路：
     * 1. 首先遍历字符串，记录每个字母最后出现的位置
     * 2. 再次遍历字符串，维护当前区间的起始位置和结束位置
     * 3. 对于当前遍历到的字符，更新当前区间的结束位置为当前字符的最后出现位置与当前结束位置的较大值
     * 4. 当遍历到当前区间的结束位置时，划分一个区间，并更新新的起始位置
     * 
     * 贪心策略的正确性：
     * 通过记录每个字符的最后出现位置，确保在划分区间时，当前区间内的所有字符的最后出现位置都不超过该区间的结束位置
     * 这样保证了同一字母只出现在一个区间中，同时尽可能多地划分区间
     * 
     * 时间复杂度：O(n)，其中n是字符串的长度，需要遍历字符串两次
     * 
     * 空间复杂度：O(1)，使用固定大小的数组（最多26个小写字母）
     * 
     * @param s 输入的字符串
     * @return 每个字符串片段的长度列表
     */
    static vector<int> partitionLabels(string s) {
        // 边界条件处理
        if (s.empty()) {
            return {};
        }

        // 记录每个字母最后出现的位置
        vector<int> lastPos(26, -1);
        for (int i = 0; i < s.size(); i++) {
            lastPos[s[i] - 'a'] = i;
        }

        vector<int> result; // 存储每个区间的长度
        int start = 0;      // 当前区间的起始位置
        int end = 0;        // 当前区间的结束位置

        // 遍历字符串，划分区间
        for (int i = 0; i < s.size(); i++) {
            // 更新当前区间的结束位置
            end = max(end, lastPos[s[i] - 'a']);

            // 如果遍历到当前区间的结束位置，划分一个区间
            if (i == end) {
                result.push_back(end - start + 1);
                start = i + 1; // 更新新的起始位置
            }
        }

        return result;
    }

    // 打印向量的辅助函数
    static void printVector(const vector<int>& vec) {
        cout << "[";
        for (size_t i = 0; i < vec.size(); i++) {
            cout << vec[i];
            if (i < vec.size() - 1) {
                cout << ", ";
            }
        }
        cout << "]" << endl;
    }
};

// 测试方法
int main() {
    // 测试用例1
    // 输入: s = "ababcbacadefegdehijhklij"
    // 输出: [9,7,8]
    // 解释:
    // 划分结果为 "ababcbaca", "defegde", "hijhklij"。
    // 每个字母最多出现在一个片段中。
    string s1 = "ababcbacadefegdehijhklij";
    vector<int> result1 = Code17_PartitionLabels::partitionLabels(s1);
    cout << "测试用例1结果: ";
    Code17_PartitionLabels::printVector(result1); // 期望输出: [9, 7, 8]

    // 测试用例2
    // 输入: s = "eccbbbbdec"
    // 输出: [10]
    // 解释: 所有字母都在一个区间中
    string s2 = "eccbbbbdec";
    vector<int> result2 = Code17_PartitionLabels::partitionLabels(s2);
    cout << "测试用例2结果: ";
    Code17_PartitionLabels::printVector(result2); // 期望输出: [10]

    // 测试用例3：边界情况 - 空字符串
    // 输入: s = ""
    // 输出: []
    string s3 = "";
    vector<int> result3 = Code17_PartitionLabels::partitionLabels(s3);
    cout << "测试用例3结果: ";
    Code17_PartitionLabels::printVector(result3); // 期望输出: []

    // 测试用例4：边界情况 - 只有一个字符
    // 输入: s = "a"
    // 输出: [1]
    string s4 = "a";
    vector<int> result4 = Code17_PartitionLabels::partitionLabels(s4);
    cout << "测试用例4结果: ";
    Code17_PartitionLabels::printVector(result4); // 期望输出: [1]

    // 测试用例5：更复杂的情况
    // 输入: s = "abcdefghijklmnopqrstuvwxyz"
    // 输出: [1,1,1,...,1] (26个1)
    string s5 = "abcdefghijklmnopqrstuvwxyz";
    vector<int> result5 = Code17_PartitionLabels::partitionLabels(s5);
    cout << "测试用例5结果: ";
    Code17_PartitionLabels::printVector(result5); // 期望输出: 26个1

    // 测试用例6：重复字符较多的情况
    // 输入: s = "aaaabbbccd"
    // 输出: [9,1]
    string s6 = "aaaabbbccd";
    vector<int> result6 = Code17_PartitionLabels::partitionLabels(s6);
    cout << "测试用例6结果: ";
    Code17_PartitionLabels::printVector(result6); // 期望输出: [9, 1]

    return 0;
}