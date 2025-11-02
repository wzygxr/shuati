#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

/**
 * LeetCode 1854. 人口最多的年份 (Maximum Population Year)
 * 
 * 题目描述:
 * 给你一个二维整数数组 logs，其中每个 logs[i] = [birth, death] 表示第 i 个人的出生和死亡年份。
 * 年份 x 的人口定义为这一年期间活着的人的数目。第 i 个人被计入年份 x 的人口需要满足：
 * x 在闭区间 [birth, death - 1] 内。注意，人不应当计入他们死亡当年的人口中。
 * 返回人口最多且最早的年份。
 * 
 * 示例:
 * 输入: logs = [[1993,1999],[2000,2010]]
 * 输出: 1993
 * 解释: 人口最多为 1，而 1993 是人口为 1 的最早年份。
 * 
 * 输入: logs = [[1950,1961],[1960,1971],[1970,1981]]
 * 输出: 1960
 * 解释: 人口最多为 2，分别在 1960 和 1970。其中最早年份是 1960。
 * 
 * 提示:
 * 1 <= logs.length <= 100
 * 1950 <= birth < death <= 2050
 * 
 * 题目链接: https://leetcode.com/problems/maximum-population-year/
 * 
 * 解题思路:
 * 使用差分数组技巧来处理区间更新操作。
 * 1. 创建一个差分数组diff，大小为101（年份范围1950-2050，共101年）
 * 2. 对于每个人[ birth, death ]，在差分数组中执行diff[birth-1950] += 1和diff[death-1950] -= 1
 * 3. 对差分数组计算前缀和，得到每年的实际人口数
 * 4. 找到人口最多且最早的年份
 * 
 * 时间复杂度: O(n + m) - n是logs长度，m是年份范围(101)
 * 空间复杂度: O(m) - 需要额外的差分数组空间
 * 
 * 这是最优解，因为需要处理所有记录，而且年份范围固定较小。
 */

class MaximumPopulationYear {
public:
    /**
     * 计算人口最多的年份
     * 
     * @param logs 人员出生和死亡年份数组
     * @return 人口最多且最早的年份
     */
    static int maximumPopulation(vector<vector<int>>& logs) {
        // 边界情况处理
        if (logs.empty()) {
            return 0;
        }
        
        // 年份范围是1950-2050，共101年
        const int START_YEAR = 1950;
        const int END_YEAR = 2050;
        const int YEAR_RANGE = END_YEAR - START_YEAR + 1;
        
        // 创建差分数组
        vector<int> diff(YEAR_RANGE, 0);
        
        // 处理每个人的生命周期
        for (const auto& log : logs) {
            int birth = log[0];      // 出生年份
            int death = log[1];      // 死亡年份
            
            // 在出生年份增加1个人
            diff[birth - START_YEAR] += 1;
            
            // 在死亡年份减少1个人（死亡当年不计入人口）
            if (death - START_YEAR < YEAR_RANGE) {
                diff[death - START_YEAR] -= 1;
            }
        }
        
        // 通过计算差分数组的前缀和得到每年的实际人口数，并记录最大值和对应年份
        int maxPopulation = 0;
        int earliestYear = START_YEAR;
        int currentPopulation = 0;
        
        for (int i = 0; i < YEAR_RANGE; i++) {
            currentPopulation += diff[i];
            
            // 更新最大人口数和最早年份
            if (currentPopulation > maxPopulation) {
                maxPopulation = currentPopulation;
                earliestYear = START_YEAR + i;
            }
        }
        
        return earliestYear;
    }
};

/**
 * 测试用例
 */
int main() {
    // 测试用例1
    vector<vector<int>> logs1 = {{1993, 1999}, {2000, 2010}};
    int result1 = MaximumPopulationYear::maximumPopulation(logs1);
    // 预期输出: 1993
    cout << "测试用例1: " << result1 << endl;

    // 测试用例2
    vector<vector<int>> logs2 = {{1950, 1961}, {1960, 1971}, {1970, 1981}};
    int result2 = MaximumPopulationYear::maximumPopulation(logs2);
    // 预期输出: 1960
    cout << "测试用例2: " << result2 << endl;
    
    // 测试用例3
    vector<vector<int>> logs3 = {{2008, 2026}, {2004, 2008}, {2034, 2035}, {1999, 2050}, {2049, 2050}, {2011, 2035}, {1966, 2033}};
    int result3 = MaximumPopulationYear::maximumPopulation(logs3);
    // 预期输出: 2011
    cout << "测试用例3: " << result3 << endl;
    
    return 0;
}