/**
 * 最大数 - 贪心算法解决方案（C++实现）
 * 
 * 题目描述：
 * 给定一组非负整数nums，重新排列每个数的顺序（每个数不可拆分）使之组成一个最大的整数
 * 
 * 测试链接：https://leetcode.cn/problems/largest-number/
 * 
 * 算法思想：
 * 使用贪心算法 + 自定义排序，关键是比较两个字符串a和b时，比较a+b和b+a的大小
 * 如果a+b > b+a，则a应该排在b前面，这样拼接后的结果最大
 * 
 * 时间复杂度分析：
 * O(n*logn*m) - 其中n是数组长度，m是数字的平均位数
 * - 排序需要O(n*logn)次比较
 * - 每次比较需要O(m)时间（字符串拼接和比较）
 * 
 * 空间复杂度分析：
 * O(n*m) - 需要将整数转换为字符串存储
 * 
 * 是否为最优解：
 * 是，这是解决该问题的最优解
 * 
 * 工程化考量：
 * 1. 边界条件处理：处理全为0的特殊情况
 * 2. 内存管理：及时释放动态分配的内存
 * 3. 异常处理：对非法输入进行检查
 * 4. 可读性：使用清晰的变量命名和详细的注释
 * 
 * 贪心策略证明：
 * 对于任意两个数字a和b，如果a+b > b+a，则a应该排在b前面
 * 这种排序方式满足传递性，因此可以得到全局最优解
 */

#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <sstream>

using namespace std;

/**
 * 自定义比较函数，用于排序
 * 比较规则：比较a+b和b+a的大小
 * 如果a+b > b+a，则a应该排在b前面（降序排列）
 * 
 * @param a 第一个字符串
 * @param b 第二个字符串
 * @return 如果a应该排在b前面返回true，否则返回false
 */
bool compare(const string& a, const string& b) {
    return (a + b) > (b + a);
}

/**
 * 解决最大数问题的核心方法
 * 
 * @param nums 非负整数数组
 * @return 重新排列后组成的最大整数（字符串形式）
 * 
 * 算法步骤：
 * 1. 将整数数组转换为字符串数组
 * 2. 使用自定义比较器对字符串数组进行排序
 * 3. 处理全为0的特殊情况
 * 4. 拼接排序后的字符串
 * 
 * 特殊处理：
 * 如果排序后的第一个字符串是"0"，说明所有数字都是0，直接返回"0"
 * 避免出现"000"这样的结果，应该返回"0"
 */
string largestNumber(vector<int>& nums) {
    // 输入验证
    if (nums.empty()) {
        return "0";
    }
    
    // 将整数转换为字符串
    vector<string> strs;
    for (int num : nums) {
        strs.push_back(to_string(num));
    }
    
    // 自定义排序：比较a+b和b+a的大小
    sort(strs.begin(), strs.end(), compare);
    
    // 处理全为0的特殊情况
    if (strs[0] == "0") {
        return "0";
    }
    
    // 拼接所有字符串
    string result;
    for (const string& s : strs) {
        result += s;
    }
    
    return result;
}

/**
 * 测试函数：验证最大数算法的正确性
 */
void testLargestNumber() {
    cout << "最大数算法测试开始" << endl;
    cout << "==================" << endl;
    
    // 测试用例1: [10,2]
    vector<int> nums1 = {10, 2};
    string result1 = largestNumber(nums1);
    cout << "输入: [10,2]" << endl;
    cout << "输出: " << result1 << endl;
    cout << "预期: \"210\"" << endl;
    cout << (result1 == "210" ? "✓ 通过" : "✗ 失败") << endl << endl;
    
    // 测试用例2: [3,30,34,5,9]
    vector<int> nums2 = {3, 30, 34, 5, 9};
    string result2 = largestNumber(nums2);
    cout << "输入: [3,30,34,5,9]" << endl;
    cout << "输出: " << result2 << endl;
    cout << "预期: \"9534330\"" << endl;
    cout << (result2 == "9534330" ? "✓ 通过" : "✗ 失败") << endl << endl;
    
    // 测试用例3: [0,0,0] - 全为0的特殊情况
    vector<int> nums3 = {0, 0, 0};
    string result3 = largestNumber(nums3);
    cout << "输入: [0,0,0]" << endl;
    cout << "输出: " << result3 << endl;
    cout << "预期: \"0\"" << endl;
    cout << (result3 == "0" ? "✓ 通过" : "✗ 失败") << endl << endl;
    
    // 测试用例4: [1] - 单个元素
    vector<int> nums4 = {1};
    string result4 = largestNumber(nums4);
    cout << "输入: [1]" << endl;
    cout << "输出: " << result4 << endl;
    cout << "预期: \"1\"" << endl;
    cout << (result4 == "1" ? "✓ 通过" : "✗ 失败") << endl << endl;
    
    // 测试用例5: [432,43243] - 复杂比较
    vector<int> nums5 = {432, 43243};
    string result5 = largestNumber(nums5);
    cout << "输入: [432,43243]" << endl;
    cout << "输出: " << result5 << endl;
    cout << "预期: \"43243432\"" << endl;
    cout << (result5 == "43243432" ? "✓ 通过" : "✗ 失败") << endl << endl;
    
    cout << "测试结束" << endl;
}

/**
 * 调试版本：打印排序过程中的中间结果
 * 
 * @param nums 整数数组
 * @return 最大数结果
 */
string debugLargestNumber(vector<int>& nums) {
    if (nums.empty()) {
        return "0";
    }
    
    vector<string> strs;
    for (int num : nums) {
        strs.push_back(to_string(num));
    }
    
    cout << "原始字符串数组: ";
    for (const string& s : strs) {
        cout << s << " ";
    }
    cout << endl;
    
    // 打印比较过程
    cout << "比较过程:" << endl;
    for (int i = 0; i < strs.size(); i++) {
        for (int j = i + 1; j < strs.size(); j++) {
            string ab = strs[i] + strs[j];
            string ba = strs[j] + strs[i];
            cout << "比较 " << strs[i] << " 和 " << strs[j] << ": ";
            cout << ab << " vs " << ba << " -> ";
            if (ab > ba) {
                cout << strs[i] << " 应该在 " << strs[j] << " 前面" << endl;
            } else {
                cout << strs[j] << " 应该在 " << strs[i] << " 前面" << endl;
            }
        }
    }
    
    sort(strs.begin(), strs.end(), compare);
    
    cout << "排序后字符串数组: ";
    for (const string& s : strs) {
        cout << s << " ";
    }
    cout << endl;
    
    if (strs[0] == "0") {
        return "0";
    }
    
    string result;
    for (const string& s : strs) {
        result += s;
    }
    
    return result;
}

/**
 * 主函数：运行测试
 */
int main() {
    cout << "最大数 - 贪心算法解决方案（C++实现）" << endl;
    cout << "===================================" << endl;
    
    // 运行基础测试
    testLargestNumber();
    
    cout << endl << "调试模式示例:" << endl;
    vector<int> debugNums = {3, 30, 34, 5, 9};
    cout << "对数组 [3,30,34,5,9] 进行调试跟踪:" << endl;
    string debugResult = debugLargestNumber(debugNums);
    cout << "最终结果: " << debugResult << endl;
    
    cout << endl << "算法分析:" << endl;
    cout << "- 时间复杂度: O(n*logn*m) - 其中n是数组长度，m是数字的平均位数" << endl;
    cout << "- 空间复杂度: O(n*m) - 需要将整数转换为字符串存储" << endl;
    cout << "- 贪心策略: 比较a+b和b+a的大小，a+b大的排在前面" << endl;
    cout << "- 最优性: 这种贪心策略能够得到全局最优解" << endl;
    
    return 0;
}