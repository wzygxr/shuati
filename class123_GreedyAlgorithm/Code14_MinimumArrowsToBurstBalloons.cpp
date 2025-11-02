#include <iostream>
#include <vector>
#include <algorithm>

/**
 * LeetCode 452. 用最少数量的箭引爆气球
 * 题目链接：https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/
 * 难度：中等
 * 
 * 问题描述：
 * 有一些球形气球贴在一堵用XY平面表示的墙面上。墙面上的气球记录在整数数组 points ，其中points[i] = [xstart, xend] 表示水平直径在xstart和xend之间的气球。
 * 你不知道气球的确切y坐标。一支弓箭可以沿着x轴从不同点完全垂直地射出。在坐标x处射出一支箭，若有一个气球的直径的开始和结束坐标为 xstart，xend，且满足 xstart ≤ x ≤ xend，则该气球会被引爆。
 * 可以射出的弓箭的数量没有限制。弓箭一旦被射出之后，可以无限地前进。我们想找到使得所有气球全部被引爆，所需的弓箭的最小数量。
 * 
 * 解题思路：
 * 贪心算法 + 区间排序
 * 1. 按气球的右边界进行排序
 * 2. 每次尽可能用一支箭引爆最多的气球
 * 3. 维护当前箭的位置为当前气球的右边界
 * 4. 遍历所有气球，如果当前气球的左边界大于箭的位置，则需要一支新箭，并更新箭的位置
 * 
 * 时间复杂度分析：
 * - 排序的时间复杂度：O(n log n)，其中n是气球的数量
 * - 遍历的时间复杂度：O(n)
 * - 总的时间复杂度：O(n log n)
 * 
 * 空间复杂度分析：
 * - 排序所需的额外空间：O(log n)
 * - 总的空间复杂度：O(log n)
 * 
 * 最优性证明：
 * 这是一个区间调度问题的变种。通过按右边界排序，我们能够保证每次选择的箭尽可能多地引爆气球，从而得到最优解。
 * 因为如果有一个更优的解使用更少的箭，那么在某个区间一定存在一支箭可以同时引爆更多的气球，这与我们的贪心策略矛盾。
 */

class Solution {
public:
    /**
     * 计算引爆所有气球所需的最小箭数
     * @param points 气球的坐标数组，每个元素为[xstart, xend]
     * @return 最小箭数
     */
    int findMinArrowShots(std::vector<std::vector<int>>& points) {
        // 处理边界情况
        if (points.empty()) {
            return 0;
        }
        if (points.size() == 1) {
            return 1;
        }
        
        // 按气球的右边界排序
        // 使用lambda表达式定义排序规则，按每个区间的结束位置排序
        sort(points.begin(), points.end(), [](const std::vector<int>& a, const std::vector<int>& b) {
            // 注意：这里直接比较可能会有溢出问题，但在实际应用中可以使用long long转换
            return a[1] < b[1];
        });
        
        // 初始化箭数为1，第一支箭的位置为第一个气球的右边界
        int arrows = 1;
        int currentEnd = points[0][1];
        
        // 遍历所有气球
        for (size_t i = 1; i < points.size(); i++) {
            // 如果当前气球的左边界大于箭的位置，需要一支新箭
            if (points[i][0] > currentEnd) {
                arrows++;
                // 更新箭的位置为当前气球的右边界
                currentEnd = points[i][1];
            }
            // 否则，当前箭可以引爆这个气球，不需要额外操作
        }
        
        return arrows;
    }
};

/**
 * 主函数，用于测试
 */
int main() {
    Solution solution;
    
    // 测试用例1：基本用例
    std::vector<std::vector<int>> points1 = {{10, 16}, {2, 8}, {1, 6}, {7, 12}};
    std::cout << "测试用例1结果：" << solution.findMinArrowShots(points1) << std::endl; // 预期输出：2
    
    // 测试用例2：无重叠的气球
    std::vector<std::vector<int>> points2 = {{1, 2}, {3, 4}, {5, 6}, {7, 8}};
    std::cout << "测试用例2结果：" << solution.findMinArrowShots(points2) << std::endl; // 预期输出：4
    
    // 测试用例3：完全重叠的气球
    std::vector<std::vector<int>> points3 = {{1, 5}, {2, 3}, {3, 4}, {4, 5}};
    std::cout << "测试用例3结果：" << solution.findMinArrowShots(points3) << std::endl; // 预期输出：1
    
    // 测试用例4：边界情况 - 空数组
    std::vector<std::vector<int>> points4 = {};
    std::cout << "测试用例4结果：" << solution.findMinArrowShots(points4) << std::endl; // 预期输出：0
    
    // 测试用例5：边界情况 - 单气球
    std::vector<std::vector<int>> points5 = {{1, 2}};
    std::cout << "测试用例5结果：" << solution.findMinArrowShots(points5) << std::endl; // 预期输出：1
    
    // 测试用例6：负数坐标
    std::vector<std::vector<int>> points6 = {{-10, -5}, {-8, -3}, {-6, 0}, {-4, 2}};
    std::cout << "测试用例6结果：" << solution.findMinArrowShots(points6) << std::endl; // 预期输出：2
    
    return 0;
}

/*
工程化考量：
1. 边界条件处理：
   - 空数组返回0
   - 单元素数组返回1

2. 异常处理：
   - 使用标准C++的错误处理方式
   - 可以根据需要添加try-catch块

3. 性能优化：
   - 排序算法使用C++标准库的sort函数，效率较高
   - 一次遍历完成计算

4. 代码可读性：
   - 使用命名空间std减少前缀
   - 变量命名清晰
   - 函数和参数有明确的注释

5. 潜在问题：
   - 在排序时，直接比较int可能会有溢出问题
   - 对于非常大的坐标值，应该使用long long类型

6. 调试技巧：
   - 可以在排序后打印points数组，验证排序是否正确
   - 使用调试器跟踪currentEnd和arrows的变化

7. C++特有的优化：
   - 可以使用reserve方法预先分配vector空间，减少动态扩容
   - 对于大规模数据，可以考虑使用移动语义优化性能
*/