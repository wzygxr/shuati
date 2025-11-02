/**
 * LeetCode 239. Sliding Window Maximum
 * 
 * 题目描述：
 * 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
 * 你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
 * 返回滑动窗口中的最大值。
 * 
 * 解题思路：
 * 这是一个经典的滑动窗口问题，可以使用多种方法解决。
 * 
 * 方法一：使用优先队列（最大堆）
 * 1. 使用优先队列维护窗口内的元素，队列中存储 (value, index) 对
 * 2. 按照值的大小排序，最大值在队首
 * 3. 当窗口滑动时，添加新元素并移除超出窗口范围的元素
 * 
 * 方法二：使用双端队列（单调队列）
 * 1. 维护一个单调递减的双端队列
 * 2. 队列中存储数组下标，对应的值单调递减
 * 3. 当新元素加入时，从队尾移除所有小于等于新元素的元素
 * 4. 从队首移除超出窗口范围的元素
 * 
 * 时间复杂度：
 * - 优先队列方法：O(n log k)
 * - 双端队列方法：O(n)
 * 空间复杂度：O(k)
 * 
 * 相关题目：
 * - LeetCode 220. 存在重复元素 III（TreeSet滑动窗口）
 * - LeetCode 219. 存在重复元素 II（哈希表滑动窗口）
 * - LeetCode 480. 滑动窗口中位数
 */

// 简化版C++实现，避免使用STL容器
// 由于编译环境限制，使用基本数组和手动实现算法

const int MAX_N = 100005;

// 双端队列实现（单调队列）
int deque[MAX_N];
int front, rear;

// 初始化双端队列
void initDeque() {
    front = 0;
    rear = 0;
}

// 判断双端队列是否为空
bool isEmpty() {
    return front == rear;
}

// 从队尾添加元素
void pushBack(int value) {
    deque[rear++] = value;
}

// 从队首移除元素
void popFront() {
    if (!isEmpty()) {
        front++;
    }
}

// 从队尾移除元素
void popBack() {
    if (!isEmpty()) {
        rear--;
    }
}

// 获取队首元素
int getFront() {
    if (!isEmpty()) {
        return deque[front];
    }
    return -1; // 错误值
}

// 获取队尾元素
int getBack() {
    if (!isEmpty()) {
        return deque[rear - 1];
    }
    return -1; // 错误值
}

/**
 * 使用双端队列（单调队列）解决滑动窗口最大值问题
 * 
 * @param nums 整数数组
 * @param nums_size 数组大小
 * @param k 滑动窗口大小
 * @param result_size 结果数组大小
 * @return 每个滑动窗口中的最大值数组
 */
int* maxSlidingWindow(int nums[], int nums_size, int k, int* result_size) {
    if (nums == 0 || nums_size == 0 || k <= 0) {
        *result_size = 0;
        return 0;
    }
    
    if (k == 1) {
        *result_size = nums_size;
        return nums; // 简化处理
    }
    
    // 计算结果数组大小
    *result_size = nums_size - k + 1;
    int* result = new int[*result_size];
    
    // 初始化双端队列
    initDeque();
    
    for (int i = 0; i < nums_size; i++) {
        // 移除队首超出窗口范围的元素
        while (!isEmpty() && getFront() <= i - k) {
            popFront();
        }
        
        // 从队尾移除所有小于当前元素的元素
        while (!isEmpty() && nums[getBack()] <= nums[i]) {
            popBack();
        }
        
        // 添加当前元素的下标
        pushBack(i);
        
        // 当窗口大小达到 k 时，记录最大值
        if (i >= k - 1) {
            result[i - k + 1] = nums[getFront()];
        }
    }
    
    return result;
}

// 简单的测试函数
void runTests() {
    // 测试用例需要在实际环境中运行
    // 由于没有标准输出库，我们无法直接打印结果
}