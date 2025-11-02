#include <vector>
#include <algorithm>
#include <queue>
#include <string>
#include <functional>
#include <iostream>

using namespace std;

/**
 * 贪心算法数学相关问题集合 - C++版本
 */

/**
 * 题目1: LeetCode 179. 最大数
 * 算法思路：自定义排序规则，比较两个数字拼接后的结果
 * 时间复杂度: O(n log n)，空间复杂度: O(n)
 */
string largestNumber(vector<int>& nums) {
    if (nums.empty()) return "";
    
    // 将数字转换为字符串数组
    vector<string> strNums;
    for (int num : nums) {
        strNums.push_back(to_string(num));
    }
    
    // 自定义排序：比较a+b和b+a的大小
    sort(strNums.begin(), strNums.end(), [](const string& a, const string& b) {
        return a + b > b + a; // 降序排列
    });
    
    // 处理前导零的情况
    if (strNums[0] == "0") {
        return "0";
    }
    
    // 拼接结果
    string result;
    for (const string& num : strNums) {
        result += num;
    }
    
    return result;
}

/**
 * 题目2: LeetCode 991. 坏了的计算器
 * 算法思路：反向思考，从Y到X，只能进行除2和加1操作
 * 时间复杂度: O(log Y)，空间复杂度: O(1)
 */
int brokenCalc(int X, int Y) {
    if (X >= Y) {
        return X - Y; // 只能减1操作
    }
    
    int operations = 0;
    while (Y > X) {
        operations++;
        if (Y % 2 == 0) {
            Y /= 2;
        } else {
            Y++;
        }
    }
    
    return operations + (X - Y);
}

/**
 * 题目3: LeetCode 910. 最小差值 II
 * 算法思路：排序后寻找最佳分割点
 * 时间复杂度: O(n log n)，空间复杂度: O(1)
 */
int smallestRangeII(vector<int>& nums, int k) {
    if (nums.size() <= 1) return 0;
    
    sort(nums.begin(), nums.end());
    int n = nums.size();
    int result = nums[n - 1] - nums[0]; // 初始差值
    
    // 尝试每个可能的分割点
    for (int i = 0; i < n - 1; i++) {
        int high = max(nums[n - 1] - k, nums[i] + k);
        int low = min(nums[0] + k, nums[i + 1] - k);
        result = min(result, high - low);
    }
    
    return result;
}

/**
 * 题目4: LeetCode 1526. 形成目标数组的子数组最少增加次数
 * 算法思路：相邻元素差值法
 * 时间复杂度: O(n)，空间复杂度: O(1)
 */
int minNumberOperations(vector<int>& target) {
    if (target.empty()) return 0;
    
    int operations = target[0]; // 第一个元素需要的操作次数
    
    for (int i = 1; i < target.size(); i++) {
        if (target[i] > target[i - 1]) {
            operations += target[i] - target[i - 1];
        }
    }
    
    return operations;
}

/**
 * 题目5: LeetCode 1247. 交换字符使得字符串相同
 * 算法思路：统计不匹配的位置类型
 * 时间复杂度: O(n)，空间复杂度: O(1)
 */
int minimumSwap(string s1, string s2) {
    if (s1.length() != s2.length()) return -1;
    
    int xy = 0, yx = 0; // 统计不匹配类型
    
    for (int i = 0; i < s1.length(); i++) {
        char c1 = s1[i];
        char c2 = s2[i];
        
        if (c1 == 'x' && c2 == 'y') {
            xy++;
        } else if (c1 == 'y' && c2 == 'x') {
            yx++;
        }
    }
    
    // 如果总的不匹配数是奇数，无法完成
    if ((xy + yx) % 2 != 0) {
        return -1;
    }
    
    // 计算最少交换次数
    return xy / 2 + yx / 2 + (xy % 2) * 2;
}

/**
 * 题目6: LeetCode 1561. 你可以获得的最大硬币数目
 * 算法思路：排序后每次取第二大的堆
 * 时间复杂度: O(n log n)，空间复杂度: O(1)
 */
int maxCoins(vector<int>& piles) {
    if (piles.empty()) return 0;
    
    sort(piles.begin(), piles.end());
    int result = 0;
    int n = piles.size();
    
    // 每次取第二大的堆（从倒数第二个开始，每隔一个取一个）
    for (int i = n - 2; i >= n / 3; i -= 2) {
        result += piles[i];
    }
    
    return result;
}

/**
 * 题目7: LeetCode 1689. 十-二进制数的最少数目
 * 算法思路：最少数量等于数字中最大的数字
 * 时间复杂度: O(n)，空间复杂度: O(1)
 */
int minPartitions(string n) {
    if (n.empty()) return 0;
    
    int maxDigit = 0;
    for (char c : n) {
        maxDigit = max(maxDigit, c - '0');
    }
    
    return maxDigit;
}

/**
 * 题目8: LeetCode 1710. 卡车上的最大单元数
 * 算法思路：按单位容量价值降序排序
 * 时间复杂度: O(n log n)，空间复杂度: O(1)
 */
int maximumUnits(vector<vector<int>>& boxTypes, int truckSize) {
    if (boxTypes.empty() || truckSize <= 0) return 0;
    
    // 按每个箱子的单元数降序排序
    sort(boxTypes.begin(), boxTypes.end(), [](const vector<int>& a, const vector<int>& b) {
        return a[1] > b[1];
    });
    
    int totalUnits = 0;
    int remainingSize = truckSize;
    
    for (auto& box : boxTypes) {
        int numberOfBoxes = box[0];
        int unitsPerBox = box[1];
        
        int boxesToTake = min(numberOfBoxes, remainingSize);
        totalUnits += boxesToTake * unitsPerBox;
        remainingSize -= boxesToTake;
        
        if (remainingSize == 0) {
            break;
        }
    }
    
    return totalUnits;
}

/**
 * 题目9: LeetCode 1405. 最长快乐字符串
 * 算法思路：优先使用剩余数量最多的字符
 * 时间复杂度: O(n)，空间复杂度: O(1)
 */
string longestDiverseString(int a, int b, int c) {
    // 使用最大堆存储字符和剩余数量
    priority_queue<pair<int, char>> maxHeap;
    
    if (a > 0) maxHeap.push({a, 'a'});
    if (b > 0) maxHeap.push({b, 'b'});
    if (c > 0) maxHeap.push({c, 'c'});
    
    string result;
    
    while (!maxHeap.empty()) {
        auto first = maxHeap.top();
        maxHeap.pop();
        char char1 = first.second;
        int count1 = first.first;
        
        // 检查是否已经连续使用了两个相同字符
        int len = result.length();
        if (len >= 2 && result[len - 1] == char1 && result[len - 2] == char1) {
            // 需要换一个字符
            if (maxHeap.empty()) {
                break; // 没有其他字符可用
            }
            
            auto second = maxHeap.top();
            maxHeap.pop();
            char char2 = second.second;
            int count2 = second.first;
            
            result += char2;
            count2--;
            
            if (count2 > 0) {
                maxHeap.push({count2, char2});
            }
            maxHeap.push(first); // 把第一个字符放回去
        } else {
            // 可以使用当前字符
            result += char1;
            count1--;
            
            if (count1 > 0) {
                maxHeap.push({count1, char1});
            }
        }
    }
    
    return result;
}

/**
 * 题目10: LeetCode 1665. 完成所有任务的最少初始能量
 * 算法思路：按(最小要求 - 实际消耗)降序排序
 * 时间复杂度: O(n log n)，空间复杂度: O(1)
 */
int minimumEffort(vector<vector<int>>& tasks) {
    if (tasks.empty()) return 0;
    
    // 按(最小要求 - 实际消耗)降序排序
    sort(tasks.begin(), tasks.end(), [](const vector<int>& a, const vector<int>& b) {
        int diffA = a[1] - a[0];
        int diffB = b[1] - b[0];
        return diffB < diffA; // 降序排列
    });
    
    int result = 0;
    int currentEnergy = 0;
    
    for (auto& task : tasks) {
        int actual = task[0];    // 实际消耗
        int minimum = task[1];   // 最小要求
        
        if (currentEnergy < minimum) {
            // 需要补充能量
            int need = minimum - currentEnergy;
            result += need;
            currentEnergy += need;
        }
        
        // 完成任务，消耗能量
        currentEnergy -= actual;
    }
    
    return result;
}

// 测试函数
int main() {
    // 测试最大数
    vector<int> nums1 = {3, 30, 34, 5, 9};
    cout << "最大数测试: " << largestNumber(nums1) << endl; // 期望: "9534330"
    
    // 测试坏了的计算器
    cout << "坏了的计算器测试: " << brokenCalc(2, 3) << endl; // 期望: 2
    
    // 测试最小差值II
    vector<int> nums2 = {1, 3, 6};
    cout << "最小差值II测试: " << smallestRangeII(nums2, 3) << endl; // 期望: 3
    
    // 测试最大硬币数
    vector<int> piles = {2, 4, 1, 2, 7, 8};
    cout << "最大硬币数测试: " << maxCoins(piles) << endl; // 期望: 9
    
    return 0;
}