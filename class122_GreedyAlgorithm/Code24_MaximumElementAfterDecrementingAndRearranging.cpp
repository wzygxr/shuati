#include <iostream>
#include <vector>
#include <algorithm>

// 减小和重新排列数组后的最大元素
// 给你一个正整数数组 arr 。请你对 arr 执行一些操作（也可以不进行任何操作），使得数组满足以下条件：
// - arr 中第一个元素必须是 1 。
// - 任意相邻两个元素的差的绝对值小于等于 1 ，也就是说，对于任意的 1 <= i < arr.length ，都满足 |arr[i] - arr[i-1]| <= 1 。
// 你可以执行以下 2 种操作任意次：
// 1. 减小 arr 中任意元素的值，使其变为一个更小的正整数。
// 2. 重新排列 arr 中的元素，你可以以任意顺序重新排列。
// 请你返回执行以上操作后，在满足前文所述的条件下，arr 中可能的最大值。
// 测试链接 : https://leetcode.cn/problems/maximum-element-after-decreasing-and-rearranging/
using namespace std;

class Solution {
public:
    /**
     * 减小和重新排列数组后的最大元素问题
     * 
     * 算法思路：
     * 使用贪心策略：
     * 1. 首先将数组排序（从小到大）
     * 2. 设置第一个元素为1（如果不是1的话）
     * 3. 遍历排序后的数组，对于每个元素，如果它与前一个元素的差大于1，则将其调整为前一个元素加1
     * 4. 最后一个元素就是可能的最大值
     * 
     * 正确性分析：
     * 1. 排序可以保证我们按照从小到大的顺序处理元素
     * 2. 第一个元素必须为1，这是题目要求
     * 3. 对于后续元素，我们尽可能让它们保持较大的值，但又要满足与前一个元素的差不超过1的条件
     * 4. 这样贪心的处理可以保证最终的最大值是最大的可能值
     * 
     * 时间复杂度：O(n*logn) - 主要是排序的时间复杂度
     * 空间复杂度：O(1) - 除了输入数组外，只使用了常数级别的额外空间
     * 
     * @param arr 正整数数组
     * @return 满足条件的数组中可能的最大值
     */
    int maximumElementAfterDecrementingAndRearranging(vector<int>& arr) {
        // 边界检查
        if (arr.empty()) {
            return 0;
        }
        
        // 排序数组
        sort(arr.begin(), arr.end());
        
        // 设置第一个元素为1
        arr[0] = 1;
        
        // 遍历数组，调整每个元素
        for (int i = 1; i < arr.size(); i++) {
            // 如果当前元素与前一个元素的差大于1，则将当前元素调整为前一个元素加1
            if (arr[i] - arr[i - 1] > 1) {
                arr[i] = arr[i - 1] + 1;
            }
            // 否则保持不变，因为可以减小但不能增大
        }
        
        // 返回最后一个元素，即最大的可能值
        return arr.back();
    }
};

// 打印数组辅助函数
void printArray(const vector<int>& arr) {
    for (int num : arr) {
        cout << num << " ";
    }
    cout << endl;
}

// 测试函数
void test() {
    Solution solution;
    
    // 测试用例1: arr = [2,2,1,2,1] -> 输出: 2
    // 排序后: [1,1,2,2,2]
    // 调整后: [1,2,2,2,2] 或者 [1,1,2,2,2]，最大值为2
    vector<int> arr1 = {2, 2, 1, 2, 1};
    cout << "测试用例1:" << endl;
    cout << "原数组: ";
    printArray(arr1);
    cout << "最大可能值: " << solution.maximumElementAfterDecrementingAndRearranging(arr1) << endl; // 期望输出: 2
    
    // 测试用例2: arr = [100,1,1000] -> 输出: 3
    // 排序后: [1,100,1000]
    // 调整后: [1,2,3]，最大值为3
    vector<int> arr2 = {100, 1, 1000};
    cout << "\n测试用例2:" << endl;
    cout << "原数组: ";
    printArray(arr2);
    cout << "最大可能值: " << solution.maximumElementAfterDecrementingAndRearranging(arr2) << endl; // 期望输出: 3
    
    // 测试用例3: arr = [1,2,3,4,5] -> 输出: 5
    // 排序后已经满足条件，无需调整
    vector<int> arr3 = {1, 2, 3, 4, 5};
    cout << "\n测试用例3:" << endl;
    cout << "原数组: ";
    printArray(arr3);
    cout << "最大可能值: " << solution.maximumElementAfterDecrementingAndRearranging(arr3) << endl; // 期望输出: 5
}

int main() {
    test();
    return 0;
}