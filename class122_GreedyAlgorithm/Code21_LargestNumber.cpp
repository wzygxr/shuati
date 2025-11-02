#include <iostream>
#include <vector>
#include <string>
#include <algorithm>

// 最大数
// 给定一组非负整数nums，重新排列每个数的顺序（每个数不可拆分）使之组成一个最大的整数。
// 测试链接 : https://leetcode.cn/problems/largest-number/
using namespace std;

class Solution {
public:
    /**
     * 最大数问题
     * 
     * 算法思路：
     * 使用贪心策略结合自定义排序规则：
     * 1. 将所有数字转换为字符串
     * 2. 自定义比较器：对于两个字符串a和b，比较a+b和b+a的大小
     * 3. 如果a+b > b+a，则a应该排在b前面
     * 4. 排序后拼接所有字符串
     * 5. 处理特殊情况：如果排序后的第一个元素是"0"，则结果只能是"0"
     * 
     * 正确性分析：
     * 1. 排序规则保证了对于任意两个数字的相对顺序是最优的
     * 2. 通过传递性可以证明整个排序后的数组拼接起来是最大的
     * 3. 特殊情况处理确保了当所有数字都是0时不会返回多个0
     * 
     * 时间复杂度：O(n*logn) - 主要是排序的时间复杂度，排序中比较两个字符串的时间是O(k)，k是字符串长度，但可以视为常数
     * 空间复杂度：O(n) - 需要额外的字符串数组来存储转换后的数字
     * 
     * @param nums 非负整数数组
     * @return 拼接后的最大整数的字符串表示
     */
    string largestNumber(vector<int>& nums) {
        // 边界检查
        if (nums.empty()) {
            return "0";
        }
        
        // 将整数转换为字符串
        vector<string> strs;
        for (int num : nums) {
            strs.push_back(to_string(num));
        }
        
        // 自定义排序：比较a+b和b+a哪个更大
        // 这里使用lambda表达式定义比较函数
        sort(strs.begin(), strs.end(), [](const string& a, const string& b) {
            // 注意：这里需要使用b+a和a+b比较，以实现降序排列
            return b + a > a + b;
        });
        
        // 特殊情况：如果排序后的第一个数是0，则说明所有数都是0
        if (strs[0] == "0") {
            return "0";
        }
        
        // 拼接所有字符串
        string result;
        for (const string& str : strs) {
            result += str;
        }
        
        return result;
    }
};

// 测试函数
void test() {
    Solution solution;
    
    // 测试用例1: nums = [10,2] -> 输出: "210"
    vector<int> nums1 = {10, 2};
    cout << "测试用例1:" << endl;
    cout << "输入数组: [10, 2]" << endl;
    cout << "最大数: " << solution.largestNumber(nums1) << endl; // 期望输出: "210"
    
    // 测试用例2: nums = [3,30,34,5,9] -> 输出: "9534330"
    vector<int> nums2 = {3, 30, 34, 5, 9};
    cout << "\n测试用例2:" << endl;
    cout << "输入数组: [3, 30, 34, 5, 9]" << endl;
    cout << "最大数: " << solution.largestNumber(nums2) << endl; // 期望输出: "9534330"
    
    // 测试用例3: nums = [0,0] -> 输出: "0"
    vector<int> nums3 = {0, 0};
    cout << "\n测试用例3:" << endl;
    cout << "输入数组: [0, 0]" << endl;
    cout << "最大数: " << solution.largestNumber(nums3) << endl; // 期望输出: "0"
    
    // 测试用例4: nums = [1] -> 输出: "1"
    vector<int> nums4 = {1};
    cout << "\n测试用例4:" << endl;
    cout << "输入数组: [1]" << endl;
    cout << "最大数: " << solution.largestNumber(nums4) << endl; // 期望输出: "1"
    
    // 测试用例5: nums = [1000000000, 1000000001] -> 输出: "1000000001100000000"
    vector<int> nums5 = {1000000000, 1000000001};
    cout << "\n测试用例5:" << endl;
    cout << "输入数组: [1000000000, 1000000001]" << endl;
    cout << "最大数: " << solution.largestNumber(nums5) << endl; // 期望输出: "1000000001100000000"
}

int main() {
    test();
    return 0;
}