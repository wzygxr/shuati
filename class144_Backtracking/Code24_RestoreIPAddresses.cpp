/**
 * LeetCode 93. 复原 IP 地址
 * 
 * 题目描述：
 * 有效 IP 地址 正好由四个整数（每个整数位于 0 到 255 之间组成，且不能含有前导 0），整数之间用 '.' 分隔。
 * 例如："0.1.2.201" 和 "192.168.1.1" 是 有效 IP 地址，
 * 但是 "0.011.255.245"、"192.168.1.312" 和 "192.168@1.1" 是 无效 IP 地址。
 * 给定一个只包含数字的字符串 s ，用以表示一个 IP 地址，返回所有可能的有效 IP 地址。
 * 
 * 示例：
 * 输入：s = "25525511135"
 * 输出：["255.255.11.135","255.255.111.35"]
 * 
 * 输入：s = "0000"
 * 输出：["0.0.0.0"]
 * 
 * 输入：s = "101023"
 * 输出：["1.0.10.23","1.0.102.3","10.1.0.23","10.10.2.3","101.0.2.3"]
 * 
 * 提示：
 * 1 <= s.length <= 20
 * s 仅由数字组成
 * 
 * 链接：https://leetcode.cn/problems/restore-ip-addresses/
 * 
 * 算法思路：
 * 1. 使用回溯算法分割字符串为4个部分
 * 2. 每个部分必须满足：0-255之间，不能有前导0（除非是0本身）
 * 3. 当分割完成4个部分且字符串用完时，加入结果集
 * 4. 使用剪枝优化：剩余字符串长度不足以填满剩余部分时提前终止
 * 
 * 时间复杂度：O(3^4) = O(81)，每个部分最多3位数字，共4个部分
 * 空间复杂度：O(n)，递归栈深度
 */

#include <iostream>
#include <vector>
#include <string>
using namespace std;

class Solution {
public:
    /**
     * 复原IP地址
     * 
     * @param s 输入字符串
     * @return 所有可能的有效IP地址
     */
    std::vector<std::string> restoreIpAddresses(std::string s) {
        std::vector<std::string> result;
        // 边界条件检查
        if (s.empty() || s.length() < 4 || s.length() > 12) {
            return result;
        }
        std::vector<std::string> path;
        backtrack(s, 0, path, result);
        return result;
    }

private:
    /**
     * 回溯函数生成IP地址
     * 
     * @param s 输入字符串
     * @param start 当前起始位置
     * @param path 当前路径（已分割的部分）
     * @param result 结果列表
     */
    void backtrack(const std::string& s, int start, std::vector<std::string>& path, std::vector<std::string>& result) {
        // 终止条件：已分割成4部分且处理完所有字符
        if (path.size() == 4) {
            if (start == s.length()) {
                std::string ip;
                for (int i = 0; i < 4; i++) {
                    ip += path[i];
                    if (i < 3) ip += ".";
                }
                result.push_back(ip);
            }
            return;
        }
        
        // 剪枝：剩余字符串长度不足以填满剩余部分
        // 剩余部分数：4 - path.size()
        // 每个部分最少1位，最多3位
        int min_remaining = 4 - path.size();
        int max_remaining = 3 * (4 - path.size());
        int remaining_length = s.length() - start;
        
        if (remaining_length < min_remaining || remaining_length > max_remaining) {
            return;
        }
        
        // 尝试分割1-3个字符
        for (int len = 1; len <= 3; len++) {
            if (start + len > s.length()) {
                break;
            }
            
            std::string segment = s.substr(start, len);
            
            // 检查分割部分是否合法
            if (isValidSegment(segment)) {
                path.push_back(segment);
                backtrack(s, start + len, path, result);
                path.pop_back();  // 回溯
            }
        }
    }

    /**
     * 检查IP地址分段是否合法
     * 
     * @param segment IP地址分段
     * @return 是否合法
     */
    bool isValidSegment(const std::string& segment) {
        // 长度检查
        if (segment.empty() || segment.length() > 3) {
            return false;
        }
        
        // 前导0检查：如果长度大于1且以0开头，不合法
        if (segment.length() > 1 && segment[0] == '0') {
            return false;
        }
        
        // 数值范围检查：必须在0-255之间
        int value = stoi(segment);
        return value >= 0 && value <= 255;
    }
    
public:
    // 解法二：使用迭代法生成所有可能的分割方案
    // 通过三层循环枚举所有可能的分割点
    std::vector<std::string> restoreIpAddresses2(std::string s) {
        std::vector<std::string> result;
        int n = s.length();
        
        // 枚举三个分割点的位置
        for (int i = 1; i <= 3 && i <= n - 3; i++) {
            for (int j = i + 1; j <= i + 3 && j <= n - 2; j++) {
                for (int k = j + 1; k <= j + 3 && k <= n - 1; k++) {
                    std::string seg1 = s.substr(0, i);
                    std::string seg2 = s.substr(i, j - i);
                    std::string seg3 = s.substr(j, k - j);
                    std::string seg4 = s.substr(k);
                    
                    // 检查每个分段是否合法
                    if (isValidSegment(seg1) && isValidSegment(seg2) && 
                        isValidSegment(seg3) && isValidSegment(seg4)) {
                        result.push_back(seg1 + "." + seg2 + "." + seg3 + "." + seg4);
                    }
                }
            }
        }
        
        return result;
    }
};

// 辅助函数：检查IP地址分段是否合法
bool isValidSegmentHelper(const std::string& segment) {
    // 长度检查
    if (segment.empty() || segment.length() > 3) {
        return false;
    }
    
    // 前导0检查：如果长度大于1且以0开头，不合法
    if (segment.length() > 1 && segment[0] == '0') {
        return false;
    }
    
    // 数值范围检查：必须在0-255之间
    int value = stoi(segment);
    return value >= 0 && value <= 255;
}

// 测试方法
int main() {
    Solution solution;
    
    // 测试用例1
    std::string s1 = "25525511135";
    std::vector<std::string> result1 = solution.restoreIpAddresses(s1);
    printf("输入: s = \"%s\"\n", s1.c_str());
    printf("输出: [");
    for (int i = 0; i < result1.size(); i++) {
        printf("\"%s\"", result1[i].c_str());
        if (i < result1.size() - 1) printf(", ");
    }
    printf("]\n");
    
    // 测试用例2
    std::string s2 = "0000";
    std::vector<std::string> result2 = solution.restoreIpAddresses(s2);
    printf("\n输入: s = \"%s\"\n", s2.c_str());
    printf("输出: [");
    for (int i = 0; i < result2.size(); i++) {
        printf("\"%s\"", result2[i].c_str());
        if (i < result2.size() - 1) printf(", ");
    }
    printf("]\n");
    
    // 测试用例3
    std::string s3 = "101023";
    std::vector<std::string> result3 = solution.restoreIpAddresses(s3);
    printf("\n输入: s = \"%s\"\n", s3.c_str());
    printf("输出: [");
    for (int i = 0; i < result3.size(); i++) {
        printf("\"%s\"", result3[i].c_str());
        if (i < result3.size() - 1) printf(", ");
    }
    printf("]\n");
    
    // 测试解法二
    printf("\n=== 解法二测试 ===\n");
    std::vector<std::string> result4 = solution.restoreIpAddresses2(s1);
    printf("输入: s = \"%s\"\n", s1.c_str());
    printf("输出: [");
    for (int i = 0; i < result4.size(); i++) {
        printf("\"%s\"", result4[i].c_str());
        if (i < result4.size() - 1) printf(", ");
    }
    printf("]\n");
    
    return 0;
}