#include <vector>
#include <algorithm>
#include <iostream>
#include <climits>

// 平均值最小累加和
// 给定一个数组arr，长度为n
// 再给定一个数字k，表示一定要将arr划分成k个集合
// 每个数字只能进一个集合
// 返回每个集合的平均值都累加起来的最小值
// 平均值向下取整
// 1 <= n <= 10^5
// 0 <= arr[i] <= 10^5
// 1 <= k <= n
// 来自真实大厂笔试，没有在线测试，对数器验证

class Solution {
public:
    /**
     * 计算划分数组后的最小平均值累加和
     * 
     * 算法思路：
     * 贪心策略：
     * 1. 将数组排序
     * 2. 前k-1个最小元素各自成一组（因为单个元素的平均值就是元素值本身）
     * 3. 剩余元素组成最后一组
     * 
     * 正确性证明：
     * 1. 平均值的计算是向下取整，所以元素越少的组，单个元素对平均值的影响越大
     * 2. 为了最小化总和，应该让较小的元素尽可能独立成组
     * 3. 由于必须分成k组，所以前k-1个最小元素各自成组是最优策略
     * 
     * 时间复杂度：O(n * logn) - 主要是排序的时间复杂度
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 
     * @param arr 输入数组
     * @param k 划分的组数
     * @return 最小平均值累加和
     */
    static int minAverageSum(std::vector<int>& arr, int k) {
        std::sort(arr.begin(), arr.end());
        int ans = 0;
        
        // 最小的k-1个数，每个数独自成一个集合
        for (int i = 0; i < k - 1; i++) {
            ans += arr[i];
        }
        
        // 剩余元素组成最后一组
        int sum = 0;
        for (int i = k - 1; i < arr.size(); i++) {
            sum += arr[i];
        }
        ans += sum / (arr.size() - k + 1);
        
        return ans;
    }
};

// 测试用例
int main() {
    // 额外测试用例
    std::vector<int> testArr = {1, 2, 3, 4, 5};
    int testK = 3;
    
    std::cout << "\n额外测试用例:" << std::endl;
    std::cout << "数组: [";
    for (int i = 0; i < testArr.size(); i++) {
        std::cout << testArr[i];
        if (i < testArr.size() - 1) std::cout << ", ";
    }
    std::cout << "]" << std::endl;
    std::cout << "划分组数: " << testK << std::endl;
    std::cout << "最小平均值累加和: " << Solution::minAverageSum(testArr, testK) << std::endl;
    
    return 0;
}