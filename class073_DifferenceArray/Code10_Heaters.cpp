#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>

/**
 * LeetCode 475. 供暖器 (Heaters)
 * 
 * 题目描述:
 * 冬季已经来临。你的任务是设计一个有固定加热半径的供暖器，使得所有房屋都可以被供暖。
 * 现在，给出位于一条水平线上的房屋和供暖器的位置，找到可以覆盖所有房屋的最小加热半径。
 * 所以，你的输入将会是房屋和供暖器的位置。你将输出供暖器的最小加热半径。
 * 
 * 示例1:
 * 输入: houses = [1,2,3], heaters = [2]
 * 输出: 1
 * 解释: 仅在位置2上有一个供暖器。如果我们将加热半径设为1，那么所有房屋就都能得到供暖。
 * 
 * 示例2:
 * 输入: houses = [1,2,3,4], heaters = [1,4]
 * 输出: 1
 * 解释: 在位置1和4上有两个供暖器。我们需要将加热半径设为1，这样房屋2和3就都能得到供暖。
 * 
 * 示例3:
 * 输入: houses = [1,5], heaters = [2]
 * 输出: 3
 * 解释: 供暖器在位置2，需要半径3才能覆盖房屋1和房屋5。
 * 
 * 提示:
 * 1. 给出的房屋和供暖器的数目是非负数且不会超过 25000。
 * 2. 给出的房屋和供暖器的位置均是非负数且不会超过 10^9。
 * 3. 只要房屋位于供暖器的半径内（包括在边缘上），它就可以得到供暖。
 * 4. 所有供暖器都遵循你的半径标准，加热的半径也一样。
 * 
 * 题目链接: https://leetcode.com/problems/heaters/
 * 
 * 解题思路:
 * 这个问题可以使用二分查找来解决：
 * 1. 首先对供暖器的位置进行排序，以便使用二分查找
 * 2. 对于每个房屋，找到离它最近的供暖器
 * 3. 计算房屋到最近供暖器的距离，并更新最大距离
 * 4. 最终的最大距离就是所需的最小加热半径
 * 
 * 具体步骤：
 * 1. 对供暖器数组进行排序
 * 2. 遍历每个房屋位置
 * 3. 对每个房屋位置，使用二分查找找到其左右两侧最近的供暖器
 * 4. 计算房屋到这两个供暖器的距离，取较小值
 * 5. 更新全局最大距离
 * 
 * 时间复杂度: O(n log n + m log n) - n是供暖器数量，m是房屋数量，排序需要O(n log n)，每个房屋的二分查找需要O(log n)
 * 空间复杂度: O(1) - 只需要常数级的额外空间
 * 
 * 这是最优解，因为我们需要遍历每个房屋并为每个房屋进行二分查找，这已经是理论上的最优复杂度。
 */
using namespace std;

class Solution {
public:
    /**
     * 计算供暖器的最小加热半径
     * 
     * @param houses 房屋位置数组
     * @param heaters 供暖器位置数组
     * @return 最小加热半径
     */
    int findRadius(vector<int>& houses, vector<int>& heaters) {
        if (houses.empty()) {
            return 0;
        }
        if (heaters.empty()) {
            // 没有供暖器，无法供暖，但根据题意，供暖器数量不会为0
            return -1;
        }
        
        // 对供暖器位置进行排序，以便使用二分查找
        sort(heaters.begin(), heaters.end());
        
        int maxRadius = 0;
        
        // 遍历每个房屋
        for (int house : houses) {
            // 找到离当前房屋最近的供暖器
            int closestHeaterDistance = findClosestHeater(house, heaters);
            
            // 更新最大半径
            maxRadius = max(maxRadius, closestHeaterDistance);
        }
        
        return maxRadius;
    }
    
    /**
     * 使用二分查找找到离指定房屋最近的供暖器，并返回距离
     * 
     * @param house 房屋位置
     * @param heaters 已排序的供暖器位置数组
     * @return 房屋到最近供暖器的距离
     */
    int findClosestHeater(int house, vector<int>& heaters) {
        int left = 0;
        int right = heaters.size() - 1;
        
        // 处理边界情况：房屋在所有供暖器的左侧
        if (house <= heaters[0]) {
            return heaters[0] - house;
        }
        // 处理边界情况：房屋在所有供暖器的右侧
        if (house >= heaters[right]) {
            return house - heaters[right];
        }
        
        // 二分查找
        while (left < right - 1) {
            int mid = left + (right - left) / 2;
            if (heaters[mid] == house) {
                return 0; // 房屋正好在供暖器位置
            } else if (heaters[mid] < house) {
                left = mid;
            } else {
                right = mid;
            }
        }
        
        // 此时，heaters[left] < house < heaters[right]，计算到两者的距离，取较小值
        return min(house - heaters[left], heaters[right] - house);
    }
    
    /**
     * 另一种实现方式，使用C++标准库的二分查找方法
     */
    int findRadiusAlternative(vector<int>& houses, vector<int>& heaters) {
        if (houses.empty()) {
            return 0;
        }
        if (heaters.empty()) {
            return -1;
        }
        
        sort(heaters.begin(), heaters.end());
        int maxRadius = 0;
        
        for (int house : houses) {
            // 使用lower_bound找到第一个大于等于house的供暖器位置
            auto it = lower_bound(heaters.begin(), heaters.end(), house);
            
            int closestDistance = INT_MAX;
            
            // 检查当前位置（如果不是begin，则检查前一个位置）
            if (it != heaters.begin()) {
                closestDistance = min(closestDistance, house - *(prev(it)));
            }
            
            // 检查当前位置
            if (it != heaters.end()) {
                closestDistance = min(closestDistance, *it - house);
            }
            
            maxRadius = max(maxRadius, closestDistance);
        }
        
        return maxRadius;
    }
};

// 辅助函数：打印向量
void printVector(const vector<int>& vec) {
    cout << "[";
    for (size_t i = 0; i < vec.size(); i++) {
        cout << vec[i];
        if (i < vec.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
}

// 测试代码
int main() {
    Solution solution;
    
    // 测试用例1
    vector<int> houses1 = {1, 2, 3};
    vector<int> heaters1 = {2};
    cout << "测试用例1 结果: " << solution.findRadius(houses1, heaters1) << endl; // 预期输出: 1
    cout << "测试用例1 (替代方法) 结果: " << solution.findRadiusAlternative(houses1, heaters1) << endl; // 预期输出: 1

    // 测试用例2
    vector<int> houses2 = {1, 2, 3, 4};
    vector<int> heaters2 = {1, 4};
    cout << "测试用例2 结果: " << solution.findRadius(houses2, heaters2) << endl; // 预期输出: 1
    cout << "测试用例2 (替代方法) 结果: " << solution.findRadiusAlternative(houses2, heaters2) << endl; // 预期输出: 1

    // 测试用例3
    vector<int> houses3 = {1, 5};
    vector<int> heaters3 = {2};
    cout << "测试用例3 结果: " << solution.findRadius(houses3, heaters3) << endl; // 预期输出: 3
    cout << "测试用例3 (替代方法) 结果: " << solution.findRadiusAlternative(houses3, heaters3) << endl; // 预期输出: 3
    
    // 测试用例4 - 空输入
    vector<int> houses4 = {};
    vector<int> heaters4 = {1, 2, 3};
    cout << "测试用例4 (空房屋) 结果: " << solution.findRadius(houses4, heaters4) << endl; // 预期输出: 0
    
    // 测试用例5 - 供暖器和房屋重叠
    vector<int> houses5 = {1, 1, 1, 1};
    vector<int> heaters5 = {1};
    cout << "测试用例5 (重叠位置) 结果: " << solution.findRadius(houses5, heaters5) << endl; // 预期输出: 0
    
    // 测试用例6 - 大规模数据
    vector<int> houses6 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    vector<int> heaters6 = {3, 7};
    cout << "测试用例6 (大规模数据) 结果: " << solution.findRadius(houses6, heaters6) << endl; // 预期输出: 3
    
    return 0;
}