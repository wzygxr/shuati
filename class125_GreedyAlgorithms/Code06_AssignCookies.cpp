#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#ifndef NULL
#define NULL 0
#endif

// 替代fmax函数
// 算法思路：比较两个double值，返回较大的一个
// 时间复杂度：O(1)
// 空间复杂度：O(1)
double my_fmax(double a, double b) {
    return (a > b) ? a : b;
}

// 替代strlen函数
// 算法思路：遍历字符串计算长度
// 时间复杂度：O(n)，n为字符串长度
// 空间复杂度：O(1)
int my_strlen(const char* str) {
    int len = 0;
    while (str[len] != '\0') {
        len++;
    }
    return len;
}

// 自定义内存分配函数
// 算法思路：封装系统malloc函数，便于统一管理内存分配
// 时间复杂度：O(1)
// 空间复杂度：O(1)
void* my_malloc(size_t size) {
    return malloc(size);
}

// 自定义内存释放函数
// 算法思路：封装系统free函数，便于统一管理内存释放
// 时间复杂度：O(1)
// 空间复杂度：O(1)
void my_free(void* ptr) {
    free(ptr);
}

// 替代sort函数，使用冒泡排序对二维数组按第二个元素升序排序
// 算法思路：冒泡排序，比较相邻元素的第二个值，如果前面的大则交换
// 时间复杂度：O(n^2)，n为数组长度
// 空间复杂度：O(1)
void my_sort_points(int** points, int pointsSize) {
    for (int i = 0; i < pointsSize - 1; i++) {
        for (int j = 0; j < pointsSize - i - 1; j++) {
            if (points[j][1] > points[j + 1][1]) {
                // 交换指针
                int* temp = points[j];
                points[j] = points[j + 1];
                points[j + 1] = temp;
            }
        }
    }
}

// 替代sort函数，用于people数组排序（身高降序，k升序）
// 算法思路：冒泡排序，按身高降序排列，身高相同时按k值升序排列
// 时间复杂度：O(n^2)，n为数组长度
// 空间复杂度：O(1)
void my_sort_people(int** people, int peopleSize) {
    for (int i = 0; i < peopleSize - 1; i++) {
        for (int j = 0; j < peopleSize - i - 1; j++) {
            // 身高降序，k升序
            if (people[j][0] < people[j + 1][0] || 
                (people[j][0] == people[j + 1][0] && people[j][1] > people[j + 1][1])) {
                // 交换指针
                int* temp = people[j];
                people[j] = people[j + 1];
                people[j + 1] = temp;
            }
        }
    }
}

// 分发饼干 (Assign Cookies)
// 假设你是一位很棒的家长，想要给你的孩子们一些小饼干。但是，每个孩子最多只能给一块饼干。
// 对每个孩子 i，都有一个胃口值 g[i]，这是能让孩子们满足胃口的饼干的最小尺寸；
// 并且每块饼干 j，都有一个尺寸 s[j] 。
// 如果 s[j] >= g[i]，我们可以将这个饼干 j 分配给孩子 i ，这个孩子会得到满足。
// 你的目标是尽可能满足越多数量的孩子，并输出这个最大数值。
// 
// 算法标签: 贪心算法(Greedy Algorithm)、双指针(Two Pointers)、排序(Sorting)
// 时间复杂度: O(m*log(m) + n*log(n))，其中m是孩子数量，n是饼干数量
// 空间复杂度: O(1)，仅使用常数额外空间
// 测试链接 : https://leetcode.cn/problems/assign-cookies/
// 相关题目: LeetCode 452. 用最少数量的箭引爆气球、LeetCode 763. 划分字母区间
// 贪心算法专题 - 补充题目收集与详解

/*
 * 算法思路：
 * 1. 贪心策略：优先满足胃口小的孩子
 * 2. 将孩子胃口数组和饼干尺寸数组都排序
 * 3. 用双指针分别遍历两个数组
 * 4. 当前饼干能满足当前孩子时，两个指针都后移
 * 5. 当前饼干不能满足当前孩子时，饼干指针后移，寻找更大的饼干
 *
 * 时间复杂度：O(m * logm + n * logn) - m是孩子数量，n是饼干数量，主要是排序的时间复杂度
 * 空间复杂度：O(1) - 只使用了常数额外空间
 * 是否最优解：是，这是处理此类问题的最优解法
 *
 * 工程化考量：
 * 1. 异常处理：检查输入是否为空
 * 2. 边界条件：处理空数组、单个元素等特殊情况
 * 3. 性能优化：使用双指针避免重复遍历
 * 4. 可读性：清晰的变量命名和注释
 *
 * 极端场景与边界场景：
 * 1. 空输入：g或s为空数组
 * 2. 极端值：只有一个孩子或一块饼干
 * 3. 重复数据：多个孩子胃口相同或多个饼干尺寸相同
 * 4. 有序/逆序数据：孩子胃口和饼干尺寸都已排序
 *
 * 跨语言场景与语言特性差异：
 * 1. Java：使用Arrays.sort进行排序
 * 2. C++：使用std::sort进行排序
 * 3. Python：使用sorted函数或list.sort()方法
 *
 * 调试能力构建：
 * 1. 打印中间过程：在循环中打印指针位置和当前匹配情况
 * 2. 用断言验证中间结果：确保每次匹配都满足s[j] >= g[i]
 * 3. 性能退化排查：检查排序和遍历的时间复杂度
 *
 * 与机器学习、图像处理、自然语言处理的联系与应用：
 * 1. 在资源分配问题中，贪心算法可以作为初始解提供给更复杂的优化算法
 * 2. 在推荐系统中，可以使用贪心策略为用户推荐最匹配的物品
 * 3. 在图像处理中，贪心算法可用于图像分割的初始区域划分
 */

// 简单的排序函数实现（冒泡排序）
// 算法思路：冒泡排序，比较相邻元素，如果前面的大则交换
// 时间复杂度：O(n^2)，n为数组长度
// 空间复杂度：O(1)
void bubbleSort(int arr[], int n) {
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            if (arr[j] > arr[j + 1]) {
                // 交换元素
                int temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
            }
        }
    }
}

// 分发饼干主函数
// 算法思路：贪心算法，优先满足胃口小的孩子
// 1. 对孩子胃口数组和饼干尺寸数组排序
// 2. 使用双指针遍历两个数组
// 3. 当前饼干能满足当前孩子时，两个指针都后移
// 4. 当前饼干不能满足当前孩子时，饼干指针后移，寻找更大的饼干
// 时间复杂度：O(m*log(m) + n*log(n))，主要是排序的时间复杂度
// 空间复杂度：O(1)
int findContentChildren(int g[], int gSize, int s[], int sSize) {
    // 异常处理：检查输入是否为空
    if (g == 0 || s == 0 || gSize == 0 || sSize == 0) {
        return 0;
    }
    
    // 使用冒泡排序对孩子胃口数组和饼干尺寸数组排序
    bubbleSort(g, gSize);
    bubbleSort(s, sSize);
    
    int child = 0;    // 孩子指针
    int cookie = 0;   // 饼干指针
    
    // 双指针遍历
    while (child < gSize && cookie < sSize) {
        // 如果当前饼干能满足当前孩子
        if (s[cookie] >= g[child]) {
            child++;   // 满足的孩子数加1
        }
        cookie++;      // 饼干指针后移
    }
    
    return child;      // 返回满足的孩子数
}

// 补充题目1: LeetCode 452. 用最少数量的箭引爆气球
// 题目描述: 有一些球形气球贴在一堵用XY平面表示的墙面上。墙面上的气球记录在整数数组points，
// 其中points[i] = [xstart, xend]表示水平直径在xstart和xend之间的气球。
// 你不知道气球的确切y坐标。一支弓箭可以沿着x轴从不同点完全垂直地射出。
// 在坐标x处射出一支箭，若有一个气球的直径的开始和结束坐标为xstart，xend，
// 且满足xstart ≤ x ≤ xend，则该气球会被引爆。
// 可以射出的弓箭的数量没有限制。弓箭一旦被射出之后，可以无限地前进。
// 我们想找到使得所有气球全部被引爆，所需的弓箭的最小数量。
// 链接: https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/

// 比较函数，用于排序二维数组（按第二个元素升序）
// 算法思路：比较两个一维数组的第二个元素
// 时间复杂度：O(1)
// 空间复杂度：O(1)
bool comparePoints(const int* a, const int* b) {
    return a[1] < b[1];
}

// 用最少数量的箭引爆气球
// 算法思路：贪心算法
// 1. 按气球结束位置排序
// 2. 贪心策略：尽可能用一支箭射更多的气球
// 3. 当前气球的开始位置大于箭的位置时，需要新的箭
// 时间复杂度：O(n*log(n))，主要是排序的时间复杂度
// 空间复杂度：O(1)
int findMinArrowShots(int** points, int pointsSize, int* pointsColSize) {
    if (points == NULL || pointsSize == 0) {
        return 0;
    }
    
    // 按气球结束位置排序
    my_sort_points(points, pointsSize);
    
    int arrows = 1;
    int end = points[0][1];
    
    // 贪心策略：尽可能用一支箭射更多的气球
    for (int i = 1; i < pointsSize; i++) {
        if (points[i][0] > end) {
            // 需要新的箭
            arrows++;
            end = points[i][1];
        }
    }
    
    return arrows;
}

// 补充题目2: LeetCode 763. 划分字母区间
// 题目描述: 字符串 S 由小写字母组成。我们要把这个字符串划分为尽可能多的片段，
// 同一字母最多出现在一个片段中。返回一个表示每个字符串片段的长度的列表。
// 链接: https://leetcode.cn/problems/partition-labels/

// 划分字母区间
// 算法思路：贪心算法
// 1. 记录每个字符最后出现的位置
// 2. 贪心策略：扩展片段直到包含所有字符的最后出现位置
// 3. 当前索引等于结束位置时，找到一个完整片段
// 时间复杂度：O(n)，n为字符串长度
// 空间复杂度：O(1)
int* partitionLabels(char * s, int* returnSize) {
    *returnSize = 0;
    if (s == NULL || my_strlen(s) == 0) {
        return NULL;
    }
    
    // 记录每个字符最后出现的位置
    int lastPos[26] = {0};
    for (int i = 0; i < my_strlen(s); i++) {
        lastPos[s[i] - 'a'] = i;
    }
    
    // 动态分配结果数组
    int* result = (int*)my_malloc(sizeof(int) * my_strlen(s)); // 最大可能有my_strlen(s)个片段
    int start = 0, end = 0;
    
    // 贪心策略：扩展片段直到包含所有字符的最后出现位置
    for (int i = 0; i < my_strlen(s); i++) {
        end = my_fmax(end, lastPos[s[i] - 'a']);
        if (i == end) {
            // 找到一个完整片段
            result[(*returnSize)++] = end - start + 1;
            start = end + 1;
        }
    }
    
    // 重新分配内存以节省空间
    int* new_result = (int*)my_malloc(sizeof(int) * (*returnSize));
    for (int i = 0; i < *returnSize; i++) {
        new_result[i] = result[i];
    }
    my_free(result);
    result = new_result;
    return result;
}

// 补充题目3: LeetCode 135. 分发糖果
// 题目描述: n 个孩子站成一排。给你一个整数数组 ratings 表示每个孩子的评分。
// 你需要按照以下要求，给这些孩子分发糖果：
// 1. 每个孩子至少分配到 1 个糖果。
// 2. 相邻两个孩子评分更高的孩子会获得更多的糖果。
// 请你给每个孩子分发糖果，计算并返回需要准备的最少糖果数目。
// 链接: https://leetcode.cn/problems/candy/

// 分发糖果
// 算法思路：贪心算法
// 1. 从左到右：处理右孩子评分高于左孩子的情况
// 2. 从右到左：处理左孩子评分高于右孩子的情况，取较大值
// 3. 计算总和
// 时间复杂度：O(n)，n为孩子数量
// 空间复杂度：O(n)
int candy(int* ratings, int ratingsSize) {
    if (ratings == NULL || ratingsSize == 0) {
        return 0;
    }
    
    int n = ratingsSize;
    int* candies = (int*)my_malloc(sizeof(int) * n);
    
    // 初始化：每个孩子至少1个糖果
    for (int i = 0; i < n; i++) {
        candies[i] = 1;
    }
    
    // 从左到右：处理右孩子评分高于左孩子的情况
    for (int i = 1; i < n; i++) {
        if (ratings[i] > ratings[i - 1]) {
            candies[i] = candies[i - 1] + 1;
        }
    }
    
    // 从右到左：处理左孩子评分高于右孩子的情况，取较大值
    for (int i = n - 2; i >= 0; i--) {
        if (ratings[i] > ratings[i + 1]) {
            candies[i] = my_fmax(candies[i], candies[i + 1] + 1);
        }
    }
    
    // 计算总和
    int total = 0;
    for (int i = 0; i < n; i++) {
        total += candies[i];
    }
    
    my_free(candies);
    return total;
}

// 补充题目4: LeetCode 406. 根据身高重建队列
// 题目描述: 假设有打乱顺序的一群人站成一个队列，数组 people 表示队列中一些人的属性（不一定按顺序）。
// 每个 people[i] = [hi, ki] 表示第 i 个人的身高为 hi ，前面 正好 有 ki 个身高大于或等于 hi 的人。
// 请你重新构造并返回输入数组 people 所表示的队列。
// 返回的队列应该格式化为数组 queue ，其中 queue[j] = [hj, kj] 是队列中第 j 个人的属性（queue[0] 是排在队列前面的人）。
// 链接: https://leetcode.cn/problems/queue-reconstruction-by-height/

// 比较函数，用于排序二维数组
// 算法思路：按身高降序，k升序排序
// 时间复杂度：O(1)
// 空间复杂度：O(1)
bool comparePeople(const int* a, const int* b) {
    if (a[0] != b[0]) {
        return b[0] < a[0]; // 身高降序
    } else {
        return a[1] < b[1]; // k升序
    }
}

// 根据身高重建队列
// 算法思路：贪心算法
// 1. 按身高降序，k升序排序
// 2. 贪心策略：按排序后的顺序插入到指定位置
// 时间复杂度：O(n^2)，n为人数
// 空间复杂度：O(n)
int** reconstructQueue(int** people, int peopleSize, int* peopleColSize, int* returnSize, int** returnColumnSizes) {
    *returnSize = 0;
    if (people == NULL || peopleSize == 0) {
        return NULL;
    }
    
    // 按身高降序，k升序排序
    my_sort_people(people, peopleSize);
    
    // 初始化结果数组
    int** result = (int**)my_malloc(sizeof(int*) * peopleSize);
    *returnColumnSizes = (int*)my_malloc(sizeof(int) * peopleSize);
    
    // 贪心策略：按排序后的顺序插入到指定位置
    for (int i = 0; i < peopleSize; i++) {
        // 为当前人分配空间
        result[i] = (int*)my_malloc(sizeof(int) * 2);
        (*returnColumnSizes)[i] = 2;
    }
    
    // 插入排序
    for (int i = 0; i < peopleSize; i++) {
        int pos = people[i][1];
        // 后移元素
        for (int j = (*returnSize); j > pos; j--) {
            result[j][0] = result[j - 1][0];
            result[j][1] = result[j - 1][1];
        }
        // 插入当前人
        result[pos][0] = people[i][0];
        result[pos][1] = people[i][1];
        (*returnSize)++;
    }
    
    return result;
}

// 补充题目5: LeetCode 871. 最低加油次数
// 题目描述: 汽车从起点出发驶向目的地，该目的地位于出发位置东面 target 英里处。
// 沿途有加油站，每个 station[i] 代表一个加油站，位于出发位置东面 station[i][0] 英里处，
// 并且有 station[i][1] 升汽油。
// 假设汽车油箱的容量是无限的，其中最初有 startFuel 升燃料。
// 它每行驶 1 英里就会用掉 1 升汽油。
// 当汽车到达加油站时，它可能停下来加油，将所有汽油从加油站转移到汽车中。
// 为了到达目的地，汽车所必要的最低加油次数是多少？如果无法到达目的地，则返回 -1。
// 链接: https://leetcode.cn/problems/minimum-number-of-refueling-stops/

// 最大堆的比较函数（使用数组实现最大堆）
class MaxHeap {
private:
    int heap[1000];  // 假设最多1000个元素
    int size;
public:
    MaxHeap() : size(0) {}
    
    void push(int val) {
        heap[size] = val;
        size++;
        // 向上调整
        int i = size - 1;
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (heap[parent] < heap[i]) {
                // 交换元素
                int temp = heap[parent];
                heap[parent] = heap[i];
                heap[i] = temp;
                i = parent;
            } else {
                break;
            }
        }
    }
    
    int pop() {
        if (size == 0) return -1;
        int top = heap[0];
        heap[0] = heap[size - 1];
        size--;
        // 向下调整
        int i = 0;
        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int largest = i;
            if (left < size && heap[left] > heap[largest]) {
                largest = left;
            }
            if (right < size && heap[right] > heap[largest]) {
                largest = right;
            }
            if (largest != i) {
                // 交换元素
                int temp = heap[i];
                heap[i] = heap[largest];
                heap[largest] = temp;
                i = largest;
            } else {
                break;
            }
        }
        return top;
    }
    
    bool empty() {
        return size == 0;
    }
};

// 最低加油次数
// 算法思路：贪心算法 + 最大堆
// 1. 使用最大堆存储经过的加油站的汽油量
// 2. 如果油量不足，从堆中选择油量最多的加油站加油
// 3. 处理从最后一个加油站到目的地的情况
// 时间复杂度：O(n*log(n))，n为加油站数量
// 空间复杂度：O(n)
int minRefuelStops(int target, int startFuel, int** stations, int stationsSize, int* stationsColSize) {
    // 最大堆，存储经过的加油站的汽油量
    MaxHeap maxHeap;
    long long fuel = startFuel; // 当前油量，使用long long避免溢出
    int stops = 0; // 加油次数
    long long prev = 0; // 上一个位置
    
    // 处理所有加油站
    for (int i = 0; i < stationsSize; i++) {
        long long location = stations[i][0];
        int gas = stations[i][1];
        
        // 从当前位置到加油站需要的油量
        fuel -= location - prev;
        
        // 如果油量不足，需要从之前经过的加油站中选择油量最多的加油
        while (fuel < 0 && !maxHeap.empty()) {
            fuel += maxHeap.pop();
            stops++;
        }
        
        // 如果无法到达当前加油站，返回-1
        if (fuel < 0) {
            return -1;
        }
        
        // 将当前加油站的油量加入堆中
        maxHeap.push(gas);
        prev = location;
    }
    
    // 处理从最后一个加油站到目的地
    fuel -= target - prev;
    while (fuel < 0 && !maxHeap.empty()) {
        fuel += maxHeap.pop();
        stops++;
    }
    
    return fuel >= 0 ? stops : -1;
}