// 合并果子
// 在一个果园里，多多已经将所有的果子打了下来，而且按果子的不同种类分成了不同的堆。
// 多多决定把所有的果子合成一堆。
// 每一次合并，多多可以把两堆果子合并到一起，消耗的体力等于两堆果子的重量之和。
// 可以看出，所有的果子经过 n-1 次合并之后，就只剩下一堆了。
// 多多在合并果子时总共消耗的体力等于每次合并所耗体力之和。
// 因为还要花大力气把这些果子搬回家，所以多多在合并果子时要尽可能地节省体力。
// 假定每个果子重量都为 1，并且已知果子的种类数和每种果子的数目，
// 你的任务是设计出合并的次序方案，使多多耗费的体力最少，并输出这个最小的体力耗费值。
// 测试链接 : https://www.luogu.com.cn/problem/P1090
// 贪心算法专题 - 堆与哈夫曼编码问题集合

/*
 * 算法思路：
 * 1. 贪心策略：哈夫曼编码思想，每次选择重量最小的两堆果子合并
 * 2. 使用最小堆维护所有果子堆
 * 3. 每次取出重量最小的两堆果子合并，合并后的重量重新放入堆中
 * 4. 重复直到只剩下一堆果子，累加合并过程中的体力消耗
 *
 * 时间复杂度：O(n * logn) - n是果子种类数
 * 空间复杂度：O(n) - 最小堆的空间
 * 是否最优解：是，这是处理此类问题的最优解法
 *
 * 工程化考量：
 * 1. 异常处理：检查输入是否为空
 * 2. 边界条件：处理空数组、单个元素等特殊情况
 * 3. 性能优化：使用优先队列维护最小值
 * 4. 可读性：清晰的变量命名和注释
 *
 * 极端场景与边界场景：
 * 1. 空输入：fruits为空数组
 * 2. 极端值：只有一种果子、所有果子重量相同
 * 3. 重复数据：多个果子堆重量相同
 * 4. 有序/逆序数据：果子堆重量按顺序排列
 *
 * 跨语言场景与语言特性差异：
 * 1. Java：使用PriorityQueue实现最小堆
 * 2. C++：使用priority_queue实现最小堆
 * 3. Python：使用heapq实现最小堆
 *
 * 调试能力构建：
 * 1. 打印中间过程：在循环中打印当前合并的两堆果子和合并后的重量
 * 2. 用断言验证中间结果：确保每次合并后堆中元素数量正确
 * 3. 性能退化排查：检查堆操作的时间复杂度
 *
 * 与机器学习、图像处理、自然语言处理的联系与应用：
 * 1. 在数据压缩中，哈夫曼编码是经典的贪心算法应用
 * 2. 在决策树构建中，可用于特征选择的贪心策略
 * 3. 在聚类算法中，可用于层次聚类的合并策略
 */

// 简单的最小堆实现（数组方式）
#define MAX_HEAP_SIZE 10000
int heap[MAX_HEAP_SIZE];
int heapSize = 0;

// 向最小堆中插入元素
void heapPush(int value) {
    if (heapSize >= MAX_HEAP_SIZE) return;
    
    heap[heapSize] = value;
    int current = heapSize++;
    
    // 向上调整
    while (current > 0) {
        int parent = (current - 1) / 2;
        if (heap[current] >= heap[parent]) break;
        
        // 交换
        int temp = heap[current];
        heap[current] = heap[parent];
        heap[parent] = temp;
        
        current = parent;
    }
}

// 从最小堆中取出最小元素
int heapPop() {
    if (heapSize <= 0) return -1;
    
    int result = heap[0];
    heap[0] = heap[--heapSize];
    
    // 向下调整
    int current = 0;
    while (1) {
        int left = current * 2 + 1;
        int right = current * 2 + 2;
        int smallest = current;
        
        if (left < heapSize && heap[left] < heap[smallest]) {
            smallest = left;
        }
        
        if (right < heapSize && heap[right] < heap[smallest]) {
            smallest = right;
        }
        
        if (smallest == current) break;
        
        // 交换
        int temp = heap[current];
        heap[current] = heap[smallest];
        heap[smallest] = temp;
        
        current = smallest;
    }
    
    return result;
}

// 合并果子主函数
int mergeFruits(int fruits[], int fruitsSize) {
    // 异常处理：检查输入是否为空
    if (fruits == 0 || fruitsSize == 0) {
        return 0;
    }
    
    // 边界条件：只有一堆果子，不需要合并
    if (fruitsSize == 1) {
        return 0;
    }
    
    // 重置堆大小
    heapSize = 0;
    
    // 将所有果子堆加入最小堆
    for (int i = 0; i < fruitsSize; i++) {
        heapPush(fruits[i]);
    }
    
    int totalCost = 0;  // 总体力消耗
    
    // 重复合并直到只剩下一堆果子
    while (heapSize > 1) {
        // 取出重量最小的两堆果子
        int first = heapPop();
        int second = heapPop();
        
        // 合并两堆果子
        int cost = first + second;
        totalCost += cost;
        
        // 将合并后的果子堆重新放入堆中
        heapPush(cost);
    }
    
    return totalCost;
}

/*
 * 补充题目1: LeetCode 703. 数据流中的第K大元素（最小堆应用）
 * 题目描述: 设计一个找到数据流中第K大元素的类（class）。注意是排序后的第K大元素，不是第K个不同的元素。
 * 请实现 KthLargest 类:
 * KthLargest(int k, int[] nums) 使用整数 k 和整数流 nums 初始化对象。
 * int add(int val) 将 val 插入数据流 nums 后，返回当前数据流中第K大的元素。
 * 链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
 * 
 * 算法思路:
 * 1. 使用最小堆维护K个最大元素
 * 2. 堆顶元素即为第K大元素
 * 
 * 时间复杂度: 构造函数 O(n log k)，add操作 O(log k)
 * 空间复杂度: O(k)，堆的大小为k
 * 是否最优解: 是，这是解决此类问题的最优方法
 */

// KthLargest类实现
class KthLargest {
private:
    // 自定义最大堆比较函数，创建最小堆
    class MinHeapComparator {
    public:
        bool operator()(int a, int b) {
            return a > b; // 这样priority_queue会变成最小堆
        }
    };
    
    priority_queue<int, vector<int>, MinHeapComparator> minHeap; // 维护K个最大元素的最小堆
    int k;
    
public:
    // 构造函数
    KthLargest(int k, vector<int>& nums) {
        this->k = k;
        // 将数组中的元素添加到堆中
        for (int num : nums) {
            add(num); // 复用add方法
        }
    }
    
    // 添加元素到数据流中，并返回当前第K大的元素
    int add(int val) {
        // 如果堆的大小小于k，直接添加
        if (minHeap.size() < k) {
            minHeap.push(val);
        }
        // 如果当前元素比堆顶元素大，替换堆顶元素
        else if (val > minHeap.top()) {
            minHeap.pop(); // 移除堆顶元素（最小的元素）
            minHeap.push(val); // 添加新元素
        }
        // 堆顶元素即为第K大元素
        return minHeap.top();
    }
};

/*
 * 补充题目2: LeetCode 1046. 最后一块石头的重量（最大堆应用）
 * 题目描述: 有一堆石头，每块石头的重量都是正整数。
 * 每一回合，从中选出两块 最重的 石头，然后将它们一起粉碎。假设石头的重量分别为 x 和 y，且 x <= y。那么粉碎的可能结果如下：
 * 如果 x == y，那么两块石头都会被完全粉碎；
 * 如果 x != y，那么重量为 x 的石头将会完全粉碎，而重量为 y 的石头新重量为 y-x。
 * 最后，最多只会剩下一块石头。返回此石头的重量。如果没有石头剩下，就返回 0。
 * 链接: https://leetcode.cn/problems/last-stone-weight/
 * 
 * 算法思路:
 * 1. 使用最大堆维护所有石头的重量
 * 2. 每次取出两个最大的石头进行粉碎
 * 3. 如果有剩余，将剩余重量放回堆中
 * 4. 重复直到堆中最多剩下一块石头
 * 
 * 时间复杂度: O(n log n)，其中n是石头数量
 * 空间复杂度: O(n)，堆的大小为n
 * 是否最优解: 是，这是解决此类问题的最优方法
 */

int lastStoneWeight(vector<int>& stones) {
    // 异常处理：检查输入是否为空
    if (stones.empty()) {
        return 0;
    }
    
    // 使用最大堆维护所有石头的重量
    priority_queue<int> maxHeap; // 默认是最大堆
    
    // 将所有石头加入最大堆
    for (int stone : stones) {
        maxHeap.push(stone);
    }
    
    // 重复粉碎过程
    while (maxHeap.size() > 1) {
        // 取出两个最大的石头
        int y = maxHeap.top(); maxHeap.pop(); // 第一大的石头
        int x = maxHeap.top(); maxHeap.pop(); // 第二大的石头
        
        // 如果两块石头重量不同，将剩余重量放回堆中
        if (x != y) {
            maxHeap.push(y - x);
        }
    }
    
    // 返回最后剩下的石头重量，如果没有则返回0
    return maxHeap.empty() ? 0 : maxHeap.top();
}

/*
 * 补充题目3: LeetCode 215. 数组中的第K个最大元素（最小堆实现）
 * 题目描述: 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
 * 请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
 * 链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
 * 
 * 算法思路:
 * 1. 使用最小堆维护K个最大元素
 * 2. 堆顶元素即为第K大元素
 * 
 * 时间复杂度: O(n log k)，其中n是数组长度
 * 空间复杂度: O(k)，堆的大小为k
 * 是否最优解: 是，这是解决此类问题的高效方法之一
 */

int findKthLargest(vector<int>& nums, int k) {
    // 异常处理：检查输入是否有效
    if (nums.empty() || k <= 0 || k > nums.size()) {
        throw invalid_argument("Invalid input");
    }
    
    // 自定义比较函数，创建最小堆
    class MinHeapComparator {
    public:
        bool operator()(int a, int b) {
            return a > b; // 这样priority_queue会变成最小堆
        }
    };
    
    // 使用最小堆维护K个最大元素
    priority_queue<int, vector<int>, MinHeapComparator> minHeap;
    
    // 前k个元素直接加入堆中
    for (int i = 0; i < k; i++) {
        minHeap.push(nums[i]);
    }
    
    // 对于剩下的元素，如果比堆顶元素大，则替换堆顶元素
    for (int i = k; i < nums.size(); i++) {
        if (nums[i] > minHeap.top()) {
            minHeap.pop();
            minHeap.push(nums[i]);
        }
    }
    
    // 堆顶元素即为第K大元素
    return minHeap.top();
}

/*
 * 补充题目4: LeetCode 347. 前K个高频元素（最大堆应用）
 * 题目描述: 给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。你可以按 任意顺序 返回答案。
 * 链接: https://leetcode.cn/problems/top-k-frequent-elements/
 * 
 * 算法思路:
 * 1. 使用哈希表统计每个元素出现的频率
 * 2. 使用最大堆根据频率排序
 * 3. 取出前K个元素
 * 
 * 时间复杂度: O(n log n)，其中n是数组长度
 * 空间复杂度: O(n)，哈希表和堆的空间
 * 是否最优解: 是，这是解决此类问题的高效方法之一
 */

vector<int> topKFrequent(vector<int>& nums, int k) {
    // 异常处理：检查输入是否有效
    if (nums.empty() || k <= 0 || k > nums.size()) {
        return vector<int>();
    }
    
    // 使用哈希表统计每个元素出现的频率
    unordered_map<int, int> frequencyMap;
    for (int num : nums) {
        frequencyMap[num]++;
    }
    
    // 自定义比较函数，根据频率创建最大堆
    class FrequencyComparator {
    public:
        bool operator()(const pair<int, int>& a, const pair<int, int>& b) {
            return a.second < b.second; // 这样priority_queue会变成最大堆
        }
    };
    
    // 使用最大堆根据频率排序
    priority_queue<pair<int, int>, vector<pair<int, int>>, FrequencyComparator> maxHeap;
    
    // 将所有元素加入堆中
    for (auto& entry : frequencyMap) {
        maxHeap.push(entry);
    }
    
    // 取出前K个高频元素
    vector<int> result;
    for (int i = 0; i < k; i++) {
        result.push_back(maxHeap.top().first);
        maxHeap.pop();
    }
    
    return result;
}

/*
 * 补充题目5: LeetCode 973. 最接近原点的K个点（最大堆应用）
 * 题目描述: 给定一个数组 points ，其中 points[i] = [xi, yi] 表示 X-Y 平面上的一个点，
 * 并且是一个整数 k ，返回离原点 (0,0) 最近的 k 个点。
 * 这里，平面上两点之间的距离是 欧几里德距离（√(x1^2 + y1^2)）。
 * 你可以按 任何顺序 返回答案。除了点坐标的顺序之外，答案确保是 唯一 的。
 * 链接: https://leetcode.cn/problems/k-closest-points-to-origin/
 * 
 * 算法思路:
 * 1. 使用最大堆维护K个最近的点
 * 2. 堆的比较依据是点到原点的距离平方（避免浮点数运算）
 * 3. 当堆大小超过K时，移除距离最远的点
 * 
 * 时间复杂度: O(n log k)，其中n是点的数量
 * 空间复杂度: O(k)，堆的大小为k
 * 是否最优解: 是，这是解决此类问题的高效方法之一
 */

vector<vector<int>> kClosest(vector<vector<int>>& points, int k) {
    // 异常处理：检查输入是否有效
    if (points.empty() || k <= 0 || k > points.size()) {
        return vector<vector<int>>();
    }
    
    // 计算点到原点的距离平方
    auto distanceSquared = [](const vector<int>& point) {
        return point[0] * point[0] + point[1] * point[1];
    };
    
    // 自定义比较函数，创建最大堆（距离大的优先级高）
    class DistanceComparator {
    public:
        bool operator()(const vector<int>& a, const vector<int>& b) {
            int distA = a[0] * a[0] + a[1] * a[1];
            int distB = b[0] * b[0] + b[1] * b[1];
            return distA < distB; // 这样priority_queue会变成最大堆
        }
    };
    
    // 使用最大堆维护K个最近的点
    priority_queue<vector<int>, vector<vector<int>>, DistanceComparator> maxHeap;
    
    // 将点加入堆中
    for (auto& point : points) {
        maxHeap.push(point);
        // 如果堆的大小超过K，移除距离最远的点
        if (maxHeap.size() > k) {
            maxHeap.pop();
        }
    }
    
    // 收集结果
    vector<vector<int>> result;
    while (!maxHeap.empty()) {
        result.push_back(maxHeap.top());
        maxHeap.pop();
    }
    
    return result;
}

// 辅助函数：打印数组
void printArray(int arr[], int size) {
    cout << "[";
    for (int i = 0; i < size; i++) {
        cout << arr[i];
        if (i < size - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
}

// 辅助函数：打印向量
void printVector(const vector<int>& vec) {
    cout << "[";
    for (int i = 0; i < vec.size(); i++) {
        cout << vec[i];
        if (i < vec.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
}

// 辅助函数：打印二维向量
void printVectorOfVectors(const vector<vector<int>>& vec) {
    cout << "[";
    for (int i = 0; i < vec.size(); i++) {
        cout << "[";
        for (int j = 0; j < vec[i].size(); j++) {
            cout << vec[i][j];
            if (j < vec[i].size() - 1) {
                cout << ", ";
            }
        }
        cout << "]";
        if (i < vec.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
}

// 主函数示例
int main() {
    // 合并果子示例
    int fruits[] = {1, 2, 3, 4, 5};
    int fruitsSize = sizeof(fruits) / sizeof(fruits[0]);
    cout << "合并果子示例: " << mergeFruits(fruits, fruitsSize) << endl; // 期望输出: 33
    
    // 补充题目1: KthLargest示例
    vector<int> nums1 = {3, 2, 1, 5, 6, 4};
    int k1 = 3;
    KthLargest kthLargest(k1, nums1);
    cout << "\n补充题目1: KthLargest示例" << endl;
    cout << "添加8后，第3大元素: " << kthLargest.add(8) << endl; // 应返回 6
    cout << "添加9后，第3大元素: " << kthLargest.add(9) << endl; // 应返回 8
    
    // 补充题目2: 最后一块石头的重量示例
    vector<int> stones = {2, 7, 4, 1, 8, 1};
    cout << "\n补充题目2: 最后一块石头的重量示例" << endl;
    cout << "最后一块石头的重量: " << lastStoneWeight(stones) << endl; // 应返回 1
    
    // 补充题目3: 数组中的第K个最大元素示例
    vector<int> nums2 = {3, 2, 1, 5, 6, 4};
    int k2 = 2;
    cout << "\n补充题目3: 数组中的第K个最大元素示例" << endl;
    cout << "第2大元素: " << findKthLargest(nums2, k2) << endl; // 应返回 5
    
    // 补充题目4: 前K个高频元素示例
    vector<int> nums3 = {1, 1, 1, 2, 2, 3};
    int k3 = 2;
    cout << "\n补充题目4: 前K个高频元素示例" << endl;
    cout << "前2个高频元素: ";
    printVector(topKFrequent(nums3, k3)); // 应返回 [1, 2] 或 [2, 1]
    
    // 补充题目5: 最接近原点的K个点示例
    vector<vector<int>> points = {{1, 3}, {-2, 2}, {5, 8}, {0, 1}};
    int k4 = 2;
    cout << "\n补充题目5: 最接近原点的K个点示例" << endl;
    cout << "最近的2个点: ";
    printVectorOfVectors(kClosest(points, k4)); // 应返回 [[-2, 2], [0, 1]] 或 [[0, 1], [-2, 2]]
    
    return 0;
}