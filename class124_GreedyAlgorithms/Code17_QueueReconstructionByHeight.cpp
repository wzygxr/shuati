// 根据身高重建队列（Queue Reconstruction by Height）
// 题目来源：LeetCode 406
// 题目链接：https://leetcode.cn/problems/queue-reconstruction-by-height/
// 
// 问题描述：
// 假设有打乱顺序的一群人站成一个队列，每个人由一个整数对(h, k)表示，
// 其中h是这个人的身高，k是排在这个人前面且身高大于或等于h的人数。
// 请重建这个队列，使其满足上述要求。
// 
// 算法思路：
// 使用贪心策略，按照身高降序、k值升序排序：
// 1. 将人群按照身高降序、k值升序排序
// 2. 按照排序后的顺序，将每个人插入到结果队列的第k个位置
// 3. 这样能保证前面身高更高的人先被放置，后面插入的人不会影响前面人的k值
// 
// 时间复杂度：O(n²) - 插入操作的时间复杂度
// 空间复杂度：O(n) - 需要存储结果队列
// 
// 是否最优解：是。这是该问题的最优解法。
// 
// 适用场景：
// 1. 队列重建问题
// 2. 带约束的排序问题
// 
// 异常处理：
// 1. 处理空数组情况
// 2. 处理单元素数组
// 
// 工程化考量：
// 1. 输入验证：检查数组是否为空
// 2. 边界条件：处理单元素和双元素数组
// 3. 性能优化：使用链表提高插入效率
// 
// 相关题目：
// 1. LeetCode 135. 分发糖果 - 双向约束问题
// 2. LeetCode 56. 合并区间 - 区间合并问题
// 3. LeetCode 252. 会议室 - 区间重叠判断
// 4. 牛客网 NC140 排序 - 各种排序算法实现
// 5. LintCode 391. 数飞机 - 区间调度相关
// 6. HackerRank - Jim and the Orders - 贪心调度问题
// 7. CodeChef - TACHSTCK - 区间配对问题
// 8. AtCoder ABC104C - All Green - 动态规划相关
// 9. Codeforces 1363C - Game On Leaves - 博弈论相关
// 10. POJ 3169 - Layout - 差分约束系统

#include <vector>
#include <algorithm>
#include <list>
using namespace std;

/**
 * 重建队列
 * 
 * @param people 人群数组，每个元素是[h, k]
 * @return 重建后的队列
 */
vector<vector<int>> reconstructQueue(vector<vector<int>>& people) {
    // 边界条件检查
    if (people.empty()) {
        return {};
    }
    
    int n = people.size();
    if (n == 1) {
        return people; // 只有一个人，直接返回
    }
    
    // 按照身高降序、k值升序排序
    sort(people.begin(), people.end(), [](const vector<int>& a, const vector<int>& b) {
        if (a[0] == b[0]) {
            return a[1] < b[1]; // 身高相同，按k值升序
        }
        return a[0] > b[0]; // 身高降序
    });
    
    // 使用链表提高插入效率
    list<vector<int>> resultList;
    
    for (const auto& person : people) {
        auto it = resultList.begin();
        advance(it, person[1]); // 移动到第k个位置
        resultList.insert(it, person);
    }
    
    // 将链表转换为向量
    vector<vector<int>> result(resultList.begin(), resultList.end());
    return result;
}

// 测试函数
int main() {
    // 测试用例1: 基本情况
    vector<vector<int>> people1 = {{7, 0}, {4, 4}, {7, 1}, {5, 0}, {6, 1}, {5, 2}};
    vector<vector<int>> result1 = reconstructQueue(people1);
    
    // 测试用例2: 简单情况
    vector<vector<int>> people2 = {{6, 0}, {5, 0}, {4, 0}, {3, 2}, {2, 2}, {1, 4}};
    vector<vector<int>> result2 = reconstructQueue(people2);
    
    // 测试用例3: 边界情况 - 单元素数组
    vector<vector<int>> people3 = {{5, 0}};
    vector<vector<int>> result3 = reconstructQueue(people3);
    
    // 测试用例4: 边界情况 - 空数组
    vector<vector<int>> people4 = {};
    vector<vector<int>> result4 = reconstructQueue(people4);
    
    // 测试用例5: 复杂情况 - 相同身高
    vector<vector<int>> people5 = {{5, 2}, {5, 0}, {5, 1}, {4, 0}, {4, 1}};
    vector<vector<int>> result5 = reconstructQueue(people5);
    
    return 0;
}