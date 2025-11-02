/**
 * LeetCode 219. Contains Duplicate II
 * 
 * 题目描述：
 * 给你一个整数数组 nums 和一个整数 k，判断数组中是否存在两个不同的索引 i 和 j，
 * 满足 nums[i] == nums[j] 且 abs(i - j) <= k。
 * 如果存在，返回 true；否则，返回 false。
 * 
 * 解题思路：
 * 这是一个滑动窗口结合哈希表的问题。
 * 
 * 核心思想：
 * 1. 使用滑动窗口维护最多k+1个元素
 * 2. 使用哈希表维护窗口内元素的存在性
 * 3. 当窗口大小超过k+1时，移除最早加入的元素
 * 
 * 具体步骤：
 * 1. 遍历数组，维护一个大小为k+1的滑动窗口
 * 2. 对于每个元素，检查它是否已在当前窗口中存在
 * 3. 如果存在，返回true
 * 4. 当窗口大小超过k+1时，移除最早加入的元素
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(min(n, k))
 * 
 * 相关题目：
 * - LeetCode 220. 存在重复元素 III（TreeSet滑动窗口）
 * - LeetCode 121. 买卖股票的最佳时机（滑动窗口）
 * - LeetCode 239. 滑动窗口最大值（双端队列）
 */

// 简化版C++实现，避免使用STL容器
// 由于编译环境限制，使用基本数组和手动实现算法

const int MAX_N = 100005;

// 简单的哈希集合实现
bool hashSet[MAX_N * 2]; // 使用偏移量处理负数
int windowElements[MAX_N];
int windowSize;

// 初始化哈希集合
void initHashSet() {
    for (int i = 0; i < MAX_N * 2; i++) {
        hashSet[i] = false;
    }
    windowSize = 0;
}

// 哈希函数（简单取模）
int hashFunction(int value) {
    // 处理负数，加上偏移量
    return (value + MAX_N) % (MAX_N * 2);
}

// 检查元素是否存在
bool contains(int value) {
    int index = hashFunction(value);
    return hashSet[index];
}

// 添加元素
void add(int value) {
    int index = hashFunction(value);
    if (!hashSet[index]) {
        hashSet[index] = true;
        windowElements[windowSize++] = value;
    }
}

// 移除元素
void remove(int value) {
    int index = hashFunction(value);
    if (hashSet[index]) {
        hashSet[index] = false;
        // 在实际应用中，我们还需要从windowElements中移除元素
        // 这里简化处理
        windowSize--;
    }
}

/**
 * 判断数组中是否存在两个不同的索引满足条件
 * 
 * @param nums 整数数组
 * @param nums_size 数组大小
 * @param k 索引差的最大值
 * @return 是否存在满足条件的索引对
 */
bool containsNearbyDuplicate(int nums[], int nums_size, int k) {
    // 初始化哈希集合
    initHashSet();
    
    for (int i = 0; i < nums_size; i++) {
        // 如果当前元素已在窗口中存在，返回true
        if (contains(nums[i])) {
            return true;
        }
        
        // 将当前元素加入窗口
        add(nums[i]);
        
        // 如果窗口大小超过k+1，移除最早加入的元素
        if (windowSize > k) {
            remove(nums[i - k]);
        }
    }
    
    return false;
}

// 简单的测试函数
void runTests() {
    // 测试用例需要在实际环境中运行
    // 由于没有标准输出库，我们无法直接打印结果
}