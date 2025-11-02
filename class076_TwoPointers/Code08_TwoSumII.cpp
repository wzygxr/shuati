#include <vector>
#include <iostream>
using namespace std;

/**
 * 两数之和 II - 输入有序数组
 * 
 * 题目描述：
 * 给你一个下标从 1 开始的整数数组 numbers ，该数组已按非递减顺序排列。
 * 请你从数组中找出满足相加之和等于目标数 target 的两个数。
 * 如果设这两个数分别是 numbers[index1] 和 numbers[index2] ，则 1 <= index1 < index2 <= numbers.length 。
 * 以长度为 2 的整数数组 [index1, index2] 的形式返回这两个整数的下标 index1 和 index2。
 * 你可以假设每个输入只对应唯一的答案，而且你不能重复使用相同的元素。
 * 你所设计的解决方案必须只使用常量级的额外空间。
 * 
 * 示例：
 * 输入：numbers = [2,7,11,15], target = 9
 * 输出：[1,2]
 * 解释：2 与 7 之和等于目标数 9 。因此 index1 = 1, index2 = 2 。返回 [1, 2] 。
 * 
 * 输入：numbers = [2,3,4], target = 6
 * 输出：[1,3]
 * 解释：2 与 4 之和等于目标数 6 。因此 index1 = 1, index2 = 3 。返回 [1, 3] 。
 * 
 * 输入：numbers = [-1,0], target = -1
 * 输出：[1,2]
 * 解释：-1 与 0 之和等于目标数 -1 。因此 index1 = 1, index2 = 2 。返回 [1, 2] 。
 * 
 * 解题思路：
 * 使用双指针法。由于数组已经排序，我们可以使用两个指针分别指向数组的开始和结束。
 * 如果两个指针指向的数字之和等于目标值，则返回它们的索引（注意题目要求索引从1开始）。
 * 如果和小于目标值，则将左指针右移以增大和。
 * 如果和大于目标值，则将右指针左移以减小和。
 * 
 * 时间复杂度：O(n) - 最多遍历一次数组
 * 空间复杂度：O(1) - 只使用了常数级别的额外空间
 * 是否最优解：是 - 基于比较的算法下界为O(n)，本算法已达到最优
 * 
 * 相关题目：
 * 1. LeetCode 1 - 两数之和（无序数组，使用哈希表）
 * 2. LeetCode 167 - 两数之和 II - 输入有序数组（当前题目）
 * 3. LeetCode 15 - 三数之和（排序+双指针）
 * 4. LeetCode 18 - 四数之和（排序+双指针）
 * 
 * 工程化考虑：
 * 1. 输入验证：检查数组是否为空或长度小于2
 * 2. 异常处理：题目保证有唯一解，但在实际工程中可能需要处理无解的情况
 * 3. 边界条件：处理负数、零和正数混合的情况
 * 
 * 语言特性差异：
 * Java: 使用数组索引访问，需要手动处理索引偏移（题目要求索引从1开始）
 * C++: 可使用vector，指针运算更灵活
 * Python: 可使用列表，支持负索引访问
 * 
 * 极端输入场景：
 * 1. 数组包含负数
 * 2. 目标值为0
 * 3. 数组长度为2
 * 4. 解在数组两端
 * 
 * 与机器学习等领域的联系：
 * 1. 在特征选择中，可能需要找到两个特征的组合满足特定条件
 * 2. 在推荐系统中，可能需要找到两个物品的组合满足用户偏好
 */
class Solution {
public:
    /**
     * 使用双指针法查找两个数的索引，使得它们的和等于目标值
     * 
     * @param numbers 已排序的整数数组
     * @param target  目标和
     * @return 包含两个索引的数组（索引从1开始）
     */
    vector<int> twoSum(vector<int>& numbers, int target) {
        // 边界条件检查
        if (numbers.size() < 2) {
            vector<int> result = {-1, -1};
            return result;
        }
        
        // 初始化双指针
        int left = 0;                     // 左指针指向数组开始
        int right = numbers.size() - 1;   // 右指针指向数组结束
        
        // 当左指针小于右指针时继续循环
        while (left < right) {
            // 计算当前两个指针指向元素的和
            int sum = numbers[left] + numbers[right];
            
            // 如果和等于目标值，返回索引（注意题目要求索引从1开始）
            if (sum == target) {
                vector<int> result = {left + 1, right + 1};
                return result;
            } 
            // 如果和小于目标值，将左指针右移以增大和
            else if (sum < target) {
                left++;
            } 
            // 如果和大于目标值，将右指针左移以减小和
            else {
                right--;
            }
        }
        
        // 根据题目描述，保证有唯一解，此行理论上不会执行到
        vector<int> result = {-1, -1};
        return result;
    }
};

/**
 * 测试函数
 */
/*
void testTwoSum() {
    Solution solution;
    
    // 测试用例1: [2,7,11,15], target = 9 -> [1,2]
    vector<int> numbers1 = {2, 7, 11, 15};
    int target1 = 9;
    vector<int> result1 = solution.twoSum(numbers1, target1);
    cout << "Test 1: numbers=[";
    for (size_t i = 0; i < numbers1.size(); ++i) {
        cout << numbers1[i];
        if (i < numbers1.size() - 1) cout << ",";
    }
    cout << "], target=" << target1 << ", result=[" << result1[0] << "," << result1[1] << "]" << endl;
    
    // 测试用例2: [2,3,4], target = 6 -> [1,3]
    vector<int> numbers2 = {2, 3, 4};
    int target2 = 6;
    vector<int> result2 = solution.twoSum(numbers2, target2);
    cout << "Test 2: numbers=[";
    for (size_t i = 0; i < numbers2.size(); ++i) {
        cout << numbers2[i];
        if (i < numbers2.size() - 1) cout << ",";
    }
    cout << "], target=" << target2 << ", result=[" << result2[0] << "," << result2[1] << "]" << endl;
    
    // 测试用例3: [-1,0], target = -1 -> [1,2]
    vector<int> numbers3 = {-1, 0};
    int target3 = -1;
    vector<int> result3 = solution.twoSum(numbers3, target3);
    cout << "Test 3: numbers=[";
    for (size_t i = 0; i < numbers3.size(); ++i) {
        cout << numbers3[i];
        if (i < numbers3.size() - 1) cout << ",";
    }
    cout << "], target=" << target3 << ", result=[" << result3[0] << "," << result3[1] << "]" << endl;
}

int main() {
    testTwoSum();
    return 0;
}
*/