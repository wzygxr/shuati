// 493. 翻转对
// 平台: LeetCode
// 难度: 困难
// 标签: CDQ分治, 分治
// 链接: https://leetcode.cn/problems/reverse-pairs/

#include <bits/stdc++.h>
using namespace std;

class Solution {
public:
    int reversePairs(vector<int>& nums) {
        if (nums.empty()) return 0;
        
        vector<int> temp(nums.size());
        return mergeSort(nums, temp, 0, nums.size() - 1);
    }
    
private:
    int mergeSort(vector<int>& nums, vector<int>& temp, int left, int right) {
        if (left >= right) return 0;
        
        int mid = left + (right - left) / 2;
        int count = mergeSort(nums, temp, left, mid) + mergeSort(nums, temp, mid + 1, right);
        
        // 统计翻转对数量
        int i = left, j = mid + 1;
        while (i <= mid && j <= right) {
            if ((long long)nums[i] > 2 * (long long)nums[j]) {
                count += mid - i + 1;
                j++;
            } else {
                i++;
            }
        }
        
        // 归并排序
        i = left;
        j = mid + 1;
        int k = left;
        
        while (i <= mid && j <= right) {
            if (nums[i] <= nums[j]) {
                temp[k++] = nums[i++];
            } else {
                temp[k++] = nums[j++];
            }
        }
        
        while (i <= mid) {
            temp[k++] = nums[i++];
        }
        
        while (j <= right) {
            temp[k++] = nums[j++];
        }
        
        for (int idx = left; idx <= right; idx++) {
            nums[idx] = temp[idx];
        }
        
        return count;
    }
};

int main() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {1, 3, 2, 3, 1};
    cout << "测试用例1 [1,3,2,3,1]: " << solution.reversePairs(nums1) << endl;
    
    // 测试用例2
    vector<int> nums2 = {2, 4, 3, 5, 1};
    cout << "测试用例2 [2,4,3,5,1]: " << solution.reversePairs(nums2) << endl;
    
    // 测试用例3
    vector<int> nums3 = {5, 4, 3, 2, 1};
    cout << "测试用例3 [5,4,3,2,1]: " << solution.reversePairs(nums3) << endl;
    
    return 0;
}
