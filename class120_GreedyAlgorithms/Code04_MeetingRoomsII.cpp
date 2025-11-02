/**
 * 会议室II - 贪心算法 + 最小堆解决方案（C++实现）
 * 
 * 题目描述：
 * 给你一个会议时间安排的数组 intervals
 * 每个会议时间都会包括开始和结束的时间intervals[i]=[starti, endi]
 * 返回所需会议室的最小数量
 * 
 * 测试链接：https://leetcode.cn/problems/meeting-rooms-ii/
 * 
 * 算法思想：
 * 贪心算法 + 最小堆（优先队列）
 * 1. 按照会议开始时间对会议进行排序
 * 2. 使用最小堆来存储当前正在进行的会议的结束时间
 * 3. 对于每个会议：
 *    - 如果堆顶的会议已经结束（结束时间 <= 当前会议开始时间），则弹出堆顶
 *    - 将当前会议的结束时间加入堆中
 *    - 更新所需会议室的最大数量
 * 
 * 时间复杂度分析：
 * O(n*logn) - 排序需要O(n*logn)，每个会议进出堆一次需要O(logn)
 * 
 * 空间复杂度分析：
 * O(n) - 最坏情况下所有会议都需要同时进行，堆的大小为n
 * 
 * 是否为最优解：
 * 是，这是解决该问题的最优解
 * 
 * 工程化考量：
 * 1. 边界条件处理：处理空数组、单个会议等特殊情况
 * 2. 输入验证：检查会议时间是否有效（开始时间 < 结束时间）
 * 3. 异常处理：对非法输入进行检查
 * 4. 可读性：使用清晰的变量命名和详细的注释
 * 
 * 贪心策略证明：
 * 每次选择最早结束的会议室来安排新会议，这样可以最大化会议室的使用效率
 * 这种策略可以保证所需会议室数量最少
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <queue>
#include <stdexcept>

using namespace std;

/**
 * 计算所需会议室的最小数量
 * 
 * @param intervals 会议时间数组，每个元素是[starti, endi]
 * @return 所需会议室的最小数量
 * 
 * 算法步骤：
 * 1. 按照会议开始时间排序
 * 2. 使用最小堆存储当前会议的结束时间
 * 3. 遍历每个会议，释放已结束的会议室，分配新的会议室
 * 4. 跟踪所需会议室的最大数量
 */
int minMeetingRooms(vector<vector<int>>& intervals) {
    // 输入验证
    if (intervals.empty()) {
        return 0;
    }
    
    int n = intervals.size();
    
    // 按照会议开始时间排序
    sort(intervals.begin(), intervals.end(), [](const vector<int>& a, const vector<int>& b) {
        return a[0] < b[0];
    });
    
    // 最小堆，存储当前正在进行的会议的结束时间
    priority_queue<int, vector<int>, greater<int>> heap;
    int maxRooms = 0;  // 所需会议室的最大数量
    
    for (int i = 0; i < n; i++) {
        int start = intervals[i][0];
        int end = intervals[i][1];
        
        // 验证会议时间有效性
        if (start >= end) {
            throw invalid_argument("会议开始时间必须小于结束时间");
        }
        
        // 释放已经结束的会议室（结束时间 <= 当前会议开始时间）
        while (!heap.empty() && heap.top() <= start) {
            heap.pop();
        }
        
        // 分配新的会议室
        heap.push(end);
        
        // 更新所需会议室的最大数量
        maxRooms = max(maxRooms, (int)heap.size());
    }
    
    return maxRooms;
}

/**
 * 调试版本：打印计算过程中的中间结果
 * 
 * @param intervals 会议时间数组
 * @return 所需会议室的最小数量
 */
int debugMinMeetingRooms(vector<vector<int>>& intervals) {
    if (intervals.empty()) {
        cout << "空数组，不需要会议室" << endl;
        return 0;
    }
    
    int n = intervals.size();
    
    cout << "原始会议安排:" << endl;
    for (int i = 0; i < n; i++) {
        cout << "会议" << i << ": [" << intervals[i][0] << ", " << intervals[i][1] << "]" << endl;
    }
    
    // 按照会议开始时间排序
    sort(intervals.begin(), intervals.end(), [](const vector<int>& a, const vector<int>& b) {
        return a[0] < b[0];
    });
    
    cout << "\n排序后的会议安排:" << endl;
    for (int i = 0; i < n; i++) {
        cout << "会议" << i << ": [" << intervals[i][0] << ", " << intervals[i][1] << "]" << endl;
    }
    
    priority_queue<int, vector<int>, greater<int>> heap;
    int maxRooms = 0;
    
    cout << "\n分配过程:" << endl;
    for (int i = 0; i < n; i++) {
        int start = intervals[i][0];
        int end = intervals[i][1];
        
        cout << "\n处理会议" << i << ": [" << start << ", " << end << "]" << endl;
        
        // 释放已经结束的会议室
        cout << "释放会议室: ";
        bool released = false;
        while (!heap.empty() && heap.top() <= start) {
            int finished = heap.top();
            heap.pop();
            cout << "结束时间" << finished << " ";
            released = true;
        }
        if (!released) {
            cout << "无会议室可释放";
        }
        cout << endl;
        
        // 分配新的会议室
        heap.push(end);
        cout << "分配新会议室，当前会议室数量: " << heap.size() << endl;
        
        // 更新最大数量
        if (heap.size() > maxRooms) {
            maxRooms = heap.size();
            cout << "更新最大会议室数量: " << maxRooms << endl;
        }
    }
    
    cout << "\n最终结果: " << maxRooms << " 个会议室" << endl;
    return maxRooms;
}

/**
 * 打印二维数组的辅助函数
 * 
 * @param intervals 会议时间数组
 */
void printIntervals(const vector<vector<int>>& intervals) {
    cout << "[";
    for (int i = 0; i < intervals.size(); i++) {
        cout << "[" << intervals[i][0] << "," << intervals[i][1] << "]";
        if (i < intervals.size() - 1) cout << ",";
    }
    cout << "]";
}

/**
 * 测试函数：验证会议室分配算法的正确性
 */
void testMinMeetingRooms() {
    cout << "会议室II算法测试开始" << endl;
    cout << "===================" << endl;
    
    // 测试用例1: [[0,30],[5,10],[15,20]]
    vector<vector<int>> intervals1 = {{0,30}, {5,10}, {15,20}};
    int result1 = minMeetingRooms(intervals1);
    cout << "输入: ";
    printIntervals(intervals1);
    cout << endl;
    cout << "输出: " << result1 << endl;
    cout << "预期: 2" << endl;
    cout << (result1 == 2 ? "✓ 通过" : "✗ 失败") << endl << endl;
    
    // 测试用例2: [[7,10],[2,4]]
    vector<vector<int>> intervals2 = {{7,10}, {2,4}};
    int result2 = minMeetingRooms(intervals2);
    cout << "输入: ";
    printIntervals(intervals2);
    cout << endl;
    cout << "输出: " << result2 << endl;
    cout << "预期: 1" << endl;
    cout << (result2 == 1 ? "✓ 通过" : "✗ 失败") << endl << endl;
    
    // 测试用例3: 空数组
    vector<vector<int>> intervals3 = {};
    int result3 = minMeetingRooms(intervals3);
    cout << "输入: []" << endl;
    cout << "输出: " << result3 << endl;
    cout << "预期: 0" << endl;
    cout << (result3 == 0 ? "✓ 通过" : "✗ 失败") << endl << endl;
    
    // 测试用例4: [[1,5],[8,9],[8,9]]
    vector<vector<int>> intervals4 = {{1,5}, {8,9}, {8,9}};
    int result4 = minMeetingRooms(intervals4);
    cout << "输入: [[1,5],[8,9],[8,9]]" << endl;
    cout << "输出: " << result4 << endl;
    cout << "预期: 2" << endl;
    cout << (result4 == 2 ? "✓ 通过" : "✗ 失败") << endl << endl;
    
    cout << "测试结束" << endl;
}

/**
 * 性能测试：测试算法在大规模数据下的表现
 */
void performanceTest() {
    cout << "性能测试开始" << endl;
    cout << "============" << endl;
    
    // 生成大规模测试数据
    int n = 10000;
    vector<vector<int>> intervals;
    for (int i = 0; i < n; i++) {
        int start = i * 10;
        int end = start + (rand() % 10) + 1;
        intervals.push_back({start, end});
    }
    
    clock_t start = clock();
    int result = minMeetingRooms(intervals);
    clock_t end = clock();
    
    double duration = double(end - start) / CLOCKS_PER_SEC * 1000; // 转换为毫秒
    
    cout << "数据规模: " << n << " 个会议" << endl;
    cout << "执行时间: " << duration << " 毫秒" << endl;
    cout << "所需会议室数量: " << result << endl;
    cout << "性能测试结束" << endl;
}

/**
 * 主函数：运行测试
 */
int main() {
    cout << "会议室II - 贪心算法 + 最小堆解决方案（C++实现）" << endl;
    cout << "===========================================" << endl;
    
    // 运行基础测试
    testMinMeetingRooms();
    
    cout << endl << "调试模式示例:" << endl;
    vector<vector<int>> debugIntervals = {{0,30}, {5,10}, {15,20}};
    cout << "对测试用例 [[0,30],[5,10],[15,20]] 进行调试跟踪:" << endl;
    int debugResult = debugMinMeetingRooms(debugIntervals);
    cout << "最终结果: " << debugResult << endl;
    
    cout << endl << "算法分析:" << endl;
    cout << "- 时间复杂度: O(n*logn) - 排序需要O(n*logn)，每个会议进出堆一次需要O(logn)" << endl;
    cout << "- 空间复杂度: O(n) - 最坏情况下所有会议都需要同时进行，堆的大小为n" << endl;
    cout << "- 贪心策略: 每次选择最早结束的会议室来安排新会议" << endl;
    cout << "- 最优性: 这种贪心策略能够得到全局最优解" << endl;
    cout << "- 数据结构: 使用最小堆（优先队列）来高效管理会议室" << endl;
    
    // 可选：运行性能测试
    // cout << endl << "性能测试:" << endl;
    // performanceTest();
    
    return 0;
}