// 无重叠区间
// 给定一个区间的集合，找到需要移除区间的最小数量，使剩余区间互不重叠。
// 测试链接 : https://leetcode.cn/problems/non-overlapping-intervals/
// 贪心算法专题 - 区间调度问题集合

/*
 * 算法思路：
 * 1. 贪心策略：按区间结束位置排序，优先选择结束位置早的区间
 * 2. 排序后遍历区间，统计不重叠的区间数量
 * 3. 总区间数减去不重叠区间数就是需要移除的区间数
 *
 * 时间复杂度：O(n * logn) - 主要是排序的时间复杂度
 * 空间复杂度：O(1) - 只使用了常数额外空间
 * 是否最优解：是，这是处理此类问题的最优解法
 *
 * 工程化考量：
 * 1. 异常处理：检查输入是否为空
 * 2. 边界条件：处理空数组、单个区间等特殊情况
 * 3. 性能优化：使用贪心策略避免动态规划
 * 4. 可读性：清晰的变量命名和注释
 *
 * 极端场景与边界场景：
 * 1. 空输入：intervals为空数组
 * 2. 极端值：只有一个区间、所有区间相同
 * 3. 重复数据：多个区间相同
 * 4. 有序/逆序数据：区间按开始位置或结束位置排序
 *
 * 跨语言场景与语言特性差异：
 * 1. Java：使用Arrays.sort进行排序
 * 2. C++：使用std::sort进行排序
 * 3. Python：使用sorted函数或list.sort()方法
 *
 * 调试能力构建：
 * 1. 打印中间过程：在循环中打印当前区间和上一个选择的区间
 * 2. 用断言验证中间结果：确保选择的区间不重叠
 * 3. 性能退化排查：检查排序和遍历的时间复杂度
 *
 * 与机器学习、图像处理、自然语言处理的联系与应用：
 * 1. 在时间调度问题中，贪心算法可用于优化资源分配
 * 2. 在计算机视觉中，可用于非极大值抑制(NMS)算法
 * 3. 在自然语言处理中，可用于实体识别中的重叠实体处理
 */

// 简单的区间结构体
struct Interval {
    int start;
    int end;
};

// 简单的排序函数实现（冒泡排序）
void bubbleSort(Interval arr[], int n) {
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            if (arr[j].end > arr[j + 1].end) {
                // 交换元素
                Interval temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
            }
        }
    }
}

// 无重叠区间主函数
int eraseOverlapIntervals(Interval intervals[], int intervalsSize) {
    // 异常处理：检查输入是否为空
    if (intervals == 0 || intervalsSize == 0) {
        return 0;
    }
    
    // 边界条件：只有一个区间，不需要移除
    if (intervalsSize == 1) {
        return 0;
    }
    
    // 使用冒泡排序按区间结束位置排序
    bubbleSort(intervals, intervalsSize);
    
    int count = 1;                    // 不重叠区间数量，初始选择第一个区间
    int end = intervals[0].end;       // 上一个选择区间的结束位置
    
    // 遍历排序后的区间
    for (int i = 1; i < intervalsSize; i++) {
        // 如果当前区间与上一个选择的区间不重叠
        if (intervals[i].start >= end) {
            count++;                  // 不重叠区间数加1
            end = intervals[i].end;   // 更新结束位置
        }
    }
    
    // 需要移除的区间数 = 总区间数 - 不重叠区间数
    return intervalsSize - count;
}

// 补充题目1: LeetCode 56. 合并区间
// 题目描述: 以数组 intervals 表示若干个区间的集合，其中单个区间为 intervals[i] = [starti, endi] 。
// 请你合并所有重叠的区间，并返回一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间。
// 链接: https://leetcode.cn/problems/merge-intervals/

int** merge(int** intervals, int intervalsSize, int* intervalsColSize, int* returnSize, int** returnColumnSizes) {
    if (intervals == NULL || intervalsSize <= 1) {
        *returnSize = intervalsSize;
        *returnColumnSizes = (int*)malloc(intervalsSize * sizeof(int));
        for (int i = 0; i < intervalsSize; i++) {
            (*returnColumnSizes)[i] = 2;
        }
        return intervals; // 空数组或只有一个区间，无需合并
    }
    
    // 贪心策略：按区间起始位置排序，然后依次合并重叠区间
    sort(intervals, intervals + intervalsSize, [](int* a, int* b) {
        return a[0] < b[0];
    });
    
    // 使用vector存储合并后的区间
    vector<int*> merged;
    merged.push_back(intervals[0]); // 添加第一个区间
    
    // 遍历其余区间
    for (int i = 1; i < intervalsSize; i++) {
        int* last = merged.back(); // 获取上一个合并后的区间
        int* current = intervals[i]; // 当前区间
        
        // 如果当前区间与上一个合并后的区间重叠，则合并它们
        if (current[0] <= last[1]) {
            // 更新合并后的区间结束位置为两个区间结束位置的较大值
            last[1] = max(last[1], current[1]);
        } else {
            // 否则直接添加当前区间
            merged.push_back(current);
        }
    }
    
    // 准备返回结果
    *returnSize = merged.size();
    *returnColumnSizes = (int*)malloc(*returnSize * sizeof(int));
    int** result = (int**)malloc(*returnSize * sizeof(int*));
    for (int i = 0; i < *returnSize; i++) {
        result[i] = (int*)malloc(2 * sizeof(int));
        result[i][0] = merged[i][0];
        result[i][1] = merged[i][1];
        (*returnColumnSizes)[i] = 2;
    }
    
    return result;
}

// 补充题目2: LeetCode 57. 插入区间
// 题目描述: 给你一个 无重叠的 ，按照区间起始端点排序的区间列表。
// 在列表中插入一个新的区间，你需要确保列表中的区间仍然有序且不重叠（如果有必要的话，可以合并区间）。
// 链接: https://leetcode.cn/problems/insert-interval/

int** insert(int** intervals, int intervalsSize, int* intervalsColSize, int* newInterval, int newIntervalSize, int* returnSize, int** returnColumnSizes) {
    vector<int*> result;
    
    int i = 0;
    int n = intervalsSize;
    
    // 添加所有在新区间之前且不重叠的区间
    while (i < n && intervals[i][1] < newInterval[0]) {
        result.push_back(intervals[i]);
        i++;
    }
    
    // 合并所有与新区间重叠的区间
    while (i < n && intervals[i][0] <= newInterval[1]) {
        newInterval[0] = min(newInterval[0], intervals[i][0]);
        newInterval[1] = max(newInterval[1], intervals[i][1]);
        i++;
    }
    
    // 添加合并后的区间
    result.push_back(newInterval);
    
    // 添加剩余的区间
    while (i < n) {
        result.push_back(intervals[i]);
        i++;
    }
    
    // 准备返回结果
    *returnSize = result.size();
    *returnColumnSizes = (int*)malloc(*returnSize * sizeof(int));
    int** returnIntervals = (int**)malloc(*returnSize * sizeof(int*));
    for (int j = 0; j < *returnSize; j++) {
        returnIntervals[j] = (int*)malloc(2 * sizeof(int));
        returnIntervals[j][0] = result[j][0];
        returnIntervals[j][1] = result[j][1];
        (*returnColumnSizes)[j] = 2;
    }
    
    return returnIntervals;
}

// 补充题目3: LeetCode 452. 用最少数量的箭引爆气球
// 题目描述: 有一些球形气球贴在一堵用XY平面表示的墙面上。气球可以用区间表示为 [start, end]，
// 飞镖必须从整个区间的内部穿过才能引爆气球。求解把所有气球射爆所需的最小飞镖数。
// 链接: https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/

int findMinArrowShots(int** points, int pointsSize, int* pointsColSize) {
    if (points == NULL || pointsSize == 0) {
        return 0; // 没有气球，需要0支箭
    }
    
    // 贪心策略：按区间结束位置排序，尽可能引爆更多气球
    sort(points, points + pointsSize, [](int* a, int* b) {
        // 注意处理整数溢出，使用long long比较
        return (long long)a[1] < (long long)b[1];
    });
    
    int count = 1; // 需要的箭数，初始为1
    long long end = points[0][1]; // 第一支箭的位置，使用long long防止溢出
    
    // 遍历排序后的区间
    for (int i = 1; i < pointsSize; i++) {
        // 如果当前气球的开始位置大于上一支箭的位置，需要一支新箭
        if ((long long)points[i][0] > end) {
            count++;
            end = points[i][1];
        }
        // 否则，当前气球会被上一支箭引爆，不需要额外的箭
    }
    
    return count;
}

// 补充题目4: LeetCode 986. 区间列表的交集
// 题目描述: 给定两个由一些 闭区间 组成的列表，firstList 和 secondList，
// 其中 firstList[i] = [starti, endi] 而 secondList[j] = [startj, endj]。
// 每个列表中的区间是不相交的，并且已经排序。
// 返回这 两个区间列表的交集 。
// 链接: https://leetcode.cn/problems/interval-list-intersections/

int** intervalIntersection(int** firstList, int firstListSize, int* firstListColSize, int** secondList, int secondListSize, int* secondListColSize, int* returnSize, int** returnColumnSizes) {
    if (firstList == NULL || secondList == NULL || firstListSize == 0 || secondListSize == 0) {
        *returnSize = 0;
        return NULL; // 任一列表为空，交集为空
    }
    
    vector<int*> result;
    int i = 0, j = 0; // 两个指针分别指向两个列表
    int m = firstListSize, n = secondListSize;
    
    // 双指针遍历两个列表
    while (i < m && j < n) {
        int start = max(firstList[i][0], secondList[j][0]); // 交集的起始位置
        int end = min(firstList[i][1], secondList[j][1]);   // 交集的结束位置
        
        // 如果有交集
        if (start <= end) {
            int* interval = (int*)malloc(2 * sizeof(int));
            interval[0] = start;
            interval[1] = end;
            result.push_back(interval);
        }
        
        // 移动结束位置较小的区间的指针
        if (firstList[i][1] < secondList[j][1]) {
            i++;
        } else {
            j++;
        }
    }
    
    // 准备返回结果
    *returnSize = result.size();
    *returnColumnSizes = (int*)malloc(*returnSize * sizeof(int));
    int** returnIntervals = (int**)malloc(*returnSize * sizeof(int*));
    for (int k = 0; k < *returnSize; k++) {
        returnIntervals[k] = result[k];
        (*returnColumnSizes)[k] = 2;
    }
    
    return returnIntervals;
}

// 补充题目5: LeetCode 1288. 删除被覆盖区间
// 题目描述: 给你一个区间列表，请你删除列表中被其他区间完全覆盖的区间。
// 只有当 c <= a 且 b <= d 时，我们才认为区间 [a,b] 被区间 [c,d] 覆盖。
// 在完成所有删除操作后，请你返回列表中剩余区间的数目。
// 链接: https://leetcode.cn/problems/remove-covered-intervals/

int removeCoveredIntervals(int** intervals, int intervalsSize, int* intervalsColSize) {
    if (intervals == NULL || intervalsSize <= 1) {
        return intervals == NULL ? 0 : intervalsSize; // 空数组或只有一个区间
    }
    
    // 贪心策略：按起始位置升序排序，起始位置相同时按结束位置降序排序
    sort(intervals, intervals + intervalsSize, [](int* a, int* b) {
        if (a[0] != b[0]) {
            return a[0] < b[0];
        } else {
            return b[1] < a[1]; // 起始位置相同时，结束位置降序
        }
    });
    
    int count = 1; // 剩余区间数，至少有一个区间
    int end = intervals[0][1]; // 当前最大的结束位置
    
    // 遍历排序后的区间
    for (int i = 1; i < intervalsSize; i++) {
        // 如果当前区间的结束位置大于最大结束位置，说明不被覆盖
        if (intervals[i][1] > end) {
            count++;
            end = intervals[i][1];
        }
    }
    
    return count;
}