/**
 * 课程表III - 贪心算法 + 最大堆解决方案（C++实现）
 * 
 * 题目描述：
 * 这里有n门不同的在线课程，按从1到n编号
 * 给你一个数组courses，其中courses[i]=[durationi, lastDayi]表示第i门课将会持续上durationi天课
 * 并且必须在不晚于lastDayi的时候完成
 * 你的学期从第 1 天开始，且不能同时修读两门及两门以上的课程
 * 返回你最多可以修读的课程数目
 * 
 * 测试链接：https://leetcode.cn/problems/course-schedule-iii/
 * 
 * 算法思想：
 * 贪心算法 + 最大堆（优先队列）
 * 1. 按照课程的截止时间进行排序（截止时间早的排在前面）
 * 2. 使用最大堆来存储已选课程的持续时间
 * 3. 遍历每个课程：
 *    - 如果当前时间加上课程持续时间不超过截止时间，则选择该课程
 *    - 如果超过截止时间，则检查是否可以替换已选课程中持续时间最长的课程
 * 
 * 时间复杂度分析：
 * O(n*logn) - 排序需要O(n*logn)，每个课程进出堆一次需要O(logn)
 * 
 * 空间复杂度分析：
 * O(n) - 最坏情况下所有课程都被选中，堆的大小为n
 * 
 * 是否为最优解：
 * 是，这是解决该问题的最优解
 * 
 * 工程化考量：
 * 1. 边界条件处理：处理空数组、单个课程等特殊情况
 * 2. 输入验证：检查课程时间是否有效（持续时间 > 0，截止时间 > 0）
 * 3. 异常处理：对非法输入进行检查
 * 4. 可读性：使用清晰的变量命名和详细的注释
 * 
 * 贪心策略证明：
 * 按照截止时间排序可以保证我们优先考虑截止时间早的课程
 * 使用最大堆来管理已选课程，当遇到冲突时替换持续时间最长的课程，可以最大化课程数量
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <queue>
#include <stdexcept>

using namespace std;

/**
 * 计算最多可以修读的课程数目
 * 
 * @param courses 课程数组，每个元素是[durationi, lastDayi]
 * @return 最多可以修读的课程数目
 * 
 * 算法步骤：
 * 1. 按照课程的截止时间进行排序
 * 2. 使用最大堆存储已选课程的持续时间
 * 3. 维护当前已使用的时间
 * 4. 遍历每个课程，动态调整已选课程
 */
int scheduleCourse(vector<vector<int>>& courses) {
    // 输入验证
    if (courses.empty()) {
        return 0;
    }
    
    int n = courses.size();
    
    // 按照课程的截止时间进行排序（截止时间早的排在前面）
    sort(courses.begin(), courses.end(), [](const vector<int>& a, const vector<int>& b) {
        return a[1] < b[1];
    });
    
    // 最大堆，存储已选课程的持续时间（持续时间长的在堆顶）
    priority_queue<int> heap;
    int currentTime = 0;  // 当前已使用的时间
    
    for (auto& course : courses) {
        int duration = course[0];
        int lastDay = course[1];
        
        // 验证课程时间有效性
        if (duration <= 0 || lastDay <= 0) {
            throw invalid_argument("课程持续时间和截止时间必须大于0");
        }
        
        // 如果当前时间加上课程持续时间不超过截止时间
        if (currentTime + duration <= lastDay) {
            heap.push(duration);
            currentTime += duration;
        } 
        // 如果超过截止时间，但当前课程的持续时间比已选课程中最长的短
        else if (!heap.empty() && heap.top() > duration) {
            // 替换策略：用当前课程替换已选课程中持续时间最长的课程
            int longestDuration = heap.top();
            heap.pop();
            currentTime += duration - longestDuration;
            heap.push(duration);
        }
        // 其他情况：不选择当前课程
    }
    
    return heap.size();
}

/**
 * 调试版本：打印计算过程中的中间结果
 * 
 * @param courses 课程数组
 * @return 最多可以修读的课程数目
 */
int debugScheduleCourse(vector<vector<int>>& courses) {
    if (courses.empty()) {
        cout << "空数组，无法修读任何课程" << endl;
        return 0;
    }
    
    int n = courses.size();
    
    cout << "原始课程安排:" << endl;
    for (int i = 0; i < n; i++) {
        cout << "课程" << i << ": [持续时间=" << courses[i][0] << ", 截止时间=" << courses[i][1] << "]" << endl;
    }
    
    // 按照课程的截止时间进行排序
    sort(courses.begin(), courses.end(), [](const vector<int>& a, const vector<int>& b) {
        return a[1] < b[1];
    });
    
    cout << "\n按截止时间排序后的课程安排:" << endl;
    for (int i = 0; i < n; i++) {
        cout << "课程" << i << ": [持续时间=" << courses[i][0] << ", 截止时间=" << courses[i][1] << "]" << endl;
    }
    
    priority_queue<int> heap;
    int currentTime = 0;
    int selectedCount = 0;
    
    cout << "\n选课过程:" << endl;
    for (int i = 0; i < n; i++) {
        int duration = courses[i][0];
        int lastDay = courses[i][1];
        
        cout << "\n考虑课程" << i << ": [持续时间=" << duration << ", 截止时间=" << lastDay << "]" << endl;
        cout << "当前时间: " << currentTime << endl;
        
        // 如果当前时间加上课程持续时间不超过截止时间
        if (currentTime + duration <= lastDay) {
            heap.push(duration);
            currentTime += duration;
            selectedCount++;
            cout << "选择该课程，当前已选课程数: " << selectedCount << ", 当前时间更新为: " << currentTime << endl;
        } 
        // 如果超过截止时间，但当前课程的持续时间比已选课程中最长的短
        else if (!heap.empty() && heap.top() > duration) {
            int longestDuration = heap.top();
            heap.pop();
            cout << "替换策略: 用当前课程(持续时间=" << duration << ")替换已选课程中持续时间最长的课程(持续时间=" << longestDuration << ")" << endl;
            
            currentTime += duration - longestDuration;
            heap.push(duration);
            cout << "替换完成，当前时间更新为: " << currentTime << ", 已选课程数保持不变: " << selectedCount << endl;
        }
        else {
            cout << "无法选择该课程，跳过" << endl;
        }
        
        // 打印当前堆的内容
        cout << "当前已选课程的持续时间: [";
        priority_queue<int> temp = heap;
        while (!temp.empty()) {
            cout << temp.top();
            temp.pop();
            if (!temp.empty()) cout << ", ";
        }
        cout << "]" << endl;
    }
    
    cout << "\n最终结果: 最多可以修读 " << heap.size() << " 门课程" << endl;
    return heap.size();
}

/**
 * 打印二维数组的辅助函数
 * 
 * @param courses 课程数组
 */
void printCourses(const vector<vector<int>>& courses) {
    cout << "[";
    for (int i = 0; i < courses.size(); i++) {
        cout << "[" << courses[i][0] << "," << courses[i][1] << "]";
        if (i < courses.size() - 1) cout << ",";
    }
    cout << "]";
}

/**
 * 测试函数：验证课程表III算法的正确性
 */
void testScheduleCourse() {
    cout << "课程表III算法测试开始" << endl;
    cout << "====================" << endl;
    
    // 测试用例1: [[100,200],[200,1300],[1000,1250],[2000,3200]]
    vector<vector<int>> courses1 = {{100,200}, {200,1300}, {1000,1250}, {2000,3200}};
    int result1 = scheduleCourse(courses1);
    cout << "输入: ";
    printCourses(courses1);
    cout << endl;
    cout << "输出: " << result1 << endl;
    cout << "预期: 3" << endl;
    cout << (result1 == 3 ? "✓ 通过" : "✗ 失败") << endl << endl;
    
    // 测试用例2: [[1,2]]
    vector<vector<int>> courses2 = {{1,2}};
    int result2 = scheduleCourse(courses2);
    cout << "输入: [[1,2]]" << endl;
    cout << "输出: " << result2 << endl;
    cout << "预期: 1" << endl;
    cout << (result2 == 1 ? "✓ 通过" : "✗ 失败") << endl << endl;
    
    // 测试用例3: [[3,2],[4,3]]
    vector<vector<int>> courses3 = {{3,2}, {4,3}};
    int result3 = scheduleCourse(courses3);
    cout << "输入: [[3,2],[4,3]]" << endl;
    cout << "输出: " << result3 << endl;
    cout << "预期: 0" << endl;
    cout << (result3 == 0 ? "✓ 通过" : "✗ 失败") << endl << endl;
    
    // 测试用例4: [[5,5],[4,6],[2,6]]
    vector<vector<int>> courses4 = {{5,5}, {4,6}, {2,6}};
    int result4 = scheduleCourse(courses4);
    cout << "输入: [[5,5],[4,6],[2,6]]" << endl;
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
    vector<vector<int>> courses;
    for (int i = 0; i < n; i++) {
        int duration = rand() % 100 + 1;
        int lastDay = rand() % 1000 + duration;
        courses.push_back({duration, lastDay});
    }
    
    clock_t start = clock();
    int result = scheduleCourse(courses);
    clock_t end = clock();
    
    double duration = double(end - start) / CLOCKS_PER_SEC * 1000; // 转换为毫秒
    
    cout << "数据规模: " << n << " 门课程" << endl;
    cout << "执行时间: " << duration << " 毫秒" << endl;
    cout << "最多可以修读的课程数: " << result << endl;
    cout << "性能测试结束" << endl;
}

/**
 * 主函数：运行测试
 */
int main() {
    cout << "课程表III - 贪心算法 + 最大堆解决方案（C++实现）" << endl;
    cout << "===========================================" << endl;
    
    // 运行基础测试
    testScheduleCourse();
    
    cout << endl << "调试模式示例:" << endl;
    vector<vector<int>> debugCourses = {{100,200}, {200,1300}, {1000,1250}, {2000,3200}};
    cout << "对测试用例 [[100,200],[200,1300],[1000,1250],[2000,3200]] 进行调试跟踪:" << endl;
    int debugResult = debugScheduleCourse(debugCourses);
    cout << "最终结果: " << debugResult << endl;
    
    cout << endl << "算法分析:" << endl;
    cout << "- 时间复杂度: O(n*logn) - 排序需要O(n*logn)，每个课程进出堆一次需要O(logn)" << endl;
    cout << "- 空间复杂度: O(n) - 最坏情况下所有课程都被选中，堆的大小为n" << endl;
    cout << "- 贪心策略: 按照截止时间排序，使用最大堆管理已选课程" << endl;
    cout << "- 最优性: 这种贪心策略能够得到全局最优解" << endl;
    cout << "- 替换策略: 当遇到冲突时，用短课程替换长课程可以最大化课程数量" << endl;
    
    // 可选：运行性能测试
    // cout << endl << "性能测试:" << endl;
    // performanceTest();
    
    return 0;
}