#include <vector>
#include <set>
#include <climits>
#include <iostream>
#include <algorithm>

// 最小区间
// 你有k个非递减排列的整数列表
// 找到一个最小区间，使得k个列表中的每个列表至少有一个数包含在其中
// 测试链接 : https://leetcode.cn/problems/smallest-range-covering-elements-from-k-lists/

struct Node {
    int v; // 值
    int i; // 当前值来自哪个数组
    int j; // 当前值来自i号数组的什么位置

    Node(int a, int b, int c) : v(a), i(b), j(c) {}
};

// 自定义比较函数
struct Compare {
    bool operator()(const Node& a, const Node& b) const {
        if (a.v != b.v) {
            return a.v < b.v;
        }
        return a.i < b.i;
    }
};

class Solution {
public:
    /**
     * 找到最小区间，使得k个列表中的每个列表至少有一个数包含在其中
     * 
     * 算法思路：
     * 使用滑动窗口 + TreeSet的贪心策略：
     * 1. 将每个数组的第一个元素加入TreeSet
     * 2. 每次取出最小值，将其对应数组的下一个元素加入TreeSet
     * 3. 在过程中记录最小的区间
     * 
     * 时间复杂度：O(n*logk) - n是所有元素总数，k是数组数量
     * 空间复杂度：O(k) - TreeSet中最多存储k个元素
     * 
     * @param nums k个非递减排列的整数列表
     * @return 最小区间 [start, end]
     */
    static std::vector<int> smallestRange(std::vector<std::vector<int>>& nums) {
        int k = nums.size();
        
        // 使用set模拟TreeSet，根据值排序
        std::set<Node, Compare> set;
        
        // 初始化：将每个数组的第一个元素加入set
        for (int i = 0; i < k; i++) {
            set.insert(Node(nums[i][0], i, 0));
        }
        
        int range = INT_MAX; // 记录最窄区间的宽度
        int a = 0; // 记录最窄区间的开头
        int b = 0; // 记录最窄区间的结尾
        
        // 当set中有k个元素时继续循环
        while (set.size() == k) {
            Node max_node = *set.rbegin(); // 在有序表中，值最大的记录
            Node min_node = *set.begin();  // 在有序表中，值最小的记录
            
            // 从set中移除最小元素
            set.erase(set.begin());
            
            // 更新最小区间
            if (max_node.v - min_node.v < range) {
                range = max_node.v - min_node.v;
                a = min_node.v;
                b = max_node.v;
            }
            
            // 如果min_node所在数组还有下一个元素，则将其加入set
            if (min_node.j + 1 < nums[min_node.i].size()) {
                set.insert(Node(nums[min_node.i][min_node.j + 1], min_node.i, min_node.j + 1));
            }
        }
        
        return {a, b};
    }
};

// 测试用例
int main() {
    // 测试用例: [[4,10,15,24,26], [0,9,12,20], [5,18,22,30]]
    // 期望结果: [20,24]
    std::vector<std::vector<int>> nums = {
        {4, 10, 15, 24, 26},
        {0, 9, 12, 20},
        {5, 18, 22, 30}
    };
    
    std::vector<int> result = Solution::smallestRange(nums);
    std::cout << "测试用例: [[4,10,15,24,26], [0,9,12,20], [5,18,22,30]]" << std::endl;
    std::cout << "结果: [" << result[0] << ", " << result[1] << "]" << std::endl; // 期望输出: [20, 24]
    
    return 0;
}