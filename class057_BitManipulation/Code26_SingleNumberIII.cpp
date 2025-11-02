/**
 * 只出现一次的数字 III
 * 测试链接：https://leetcode.cn/problems/single-number-iii/
 * 
 * 题目描述：
 * 给定一个整数数组 nums，其中恰好有两个元素只出现一次，其余所有元素均出现两次。
 * 找出只出现一次的那两个元素。你可以按任意顺序返回答案。
 * 
 * 解题思路：
 * 1. 首先对所有数字进行异或操作，得到两个不同数字的异或结果
 * 2. 找到异或结果中最低位的1，这个位置表示两个数字在该位不同
 * 3. 根据这个位将数组分成两组，分别进行异或操作得到两个结果
 * 
 * 时间复杂度：O(n) - 遍历数组两次
 * 空间复杂度：O(1) - 只使用常数个变量
 */
#include <iostream>
#include <vector>
#include <stdexcept>
using namespace std;

class Code26_SingleNumberIII {
public:
    /**
     * 找出只出现一次的两个数字
     * @param nums 整数数组
     * @return 只出现一次的两个数字
     */
    vector<int> singleNumber(vector<int>& nums) {
        if (nums.size() < 2) {
            throw invalid_argument("数组长度必须至少为2");
        }
        
        // 第一步：计算所有数字的异或结果
        int xorResult = 0;
        for (int num : nums) {
            xorResult ^= num;
        }
        
        // 第二步：找到异或结果中最低位的1
        // 技巧：xorResult & (-xorResult) 可以快速找到最低位的1
        // 注意：对于负数，需要确保正确处理
        int lowestOneBit = xorResult & (-xorResult);
        
        // 第三步：根据最低位的1将数组分成两组
        vector<int> result(2, 0);
        for (int num : nums) {
            // 根据该位是否为0进行分组
            if ((num & lowestOneBit) == 0) {
                result[0] ^= num;  // 第一组
            } else {
                result[1] ^= num;  // 第二组
            }
        }
        
        return result;
    }
    
    /**
     * 测试方法
     */
    static void test() {
        Code26_SingleNumberIII solution;
        
        // 测试用例1：正常情况
        vector<int> nums1 = {1, 2, 1, 3, 2, 5};
        vector<int> result1 = solution.singleNumber(nums1);
        cout << "测试用例1结果: [" << result1[0] << ", " << result1[1] << "]" << endl;
        // 预期输出: [3, 5] 或 [5, 3]
        
        // 测试用例2：包含负数
        vector<int> nums2 = {-1, 0, -1, 2, 0, 3};
        vector<int> result2 = solution.singleNumber(nums2);
        cout << "测试用例2结果: [" << result2[0] << ", " << result2[1] << "]" << endl;
        // 预期输出: [2, 3] 或 [3, 2]
        
        // 测试用例3：边界情况
        vector<int> nums3 = {0, 1};
        vector<int> result3 = solution.singleNumber(nums3);
        cout << "测试用例3结果: [" << result3[0] << ", " << result3[1] << "]" << endl;
        // 预期输出: [0, 1] 或 [1, 0]
        
        // 复杂度分析
        cout << "\n=== 复杂度分析 ===" << endl;
        cout << "时间复杂度: O(n) - 遍历数组两次" << endl;
        cout << "空间复杂度: O(1) - 只使用常数个变量" << endl;
        
        // 工程化考量
        cout << "\n=== 工程化考量 ===" << endl;
        cout << "1. 输入验证：检查数组长度" << endl;
        cout << "2. 边界处理：处理负数情况" << endl;
        cout << "3. 性能优化：使用位运算替代算术运算" << endl;
        cout << "4. 可读性：添加详细注释说明算法原理" << endl;
        cout << "5. 异常处理：使用异常处理输入错误" << endl;
    }
};

int main() {
    Code26_SingleNumberIII::test();
    return 0;
}